var re_halfSpace = new RegExp(" ", "g");/* 半角空格 */
var re_fullSpace = new RegExp("　", "g");/* 全角空格 */

$(document).ready(function(e) {
	$(".header .top-a h2").html("修改续投方式");
	document.title = "修改续投方式";
	var winHeight = $(window).height()
	var buy_btnHeight = $(".buy_btn").height()
	var buy_btnTop = winHeight - buy_btnHeight
	$(".buy_btn").css("top", buy_btnTop);
	getRandomCode();
	queryTradeInfoByTradeNo();
	var renew = getUrlParameter("renew");
	renew = renew.replace('#', "");

	$('#oldRenew').val(renew);
	if (renew=='Y') {
		$('.select-way-box li:eq(0)').addClass('act');
		$('#selectWayTips0').show();
	}else{
		$('.select-way-box li:eq(1)').addClass('act');
		$('#selectWayTips1').show();
	}
	 $('.select-way-box li').on('click', function (e) {
         var $this = $(e.currentTarget);
         var index = $this.index();
         $this.addClass('act');
         $this.siblings().removeClass('act');
         $('.select-way-tips').hide();
         $('#selectWayTips' + index).show();
     });
});
/* 图片验证码 登录页面 */
function getRandomCode() {
	$("#rondomCodeImg").attr("src", "/WeixinService/buildimageservlet.xhtml");
}


function queryTradeByTradeNo(){
	var tradeAcco=$('#tradeacco').val()
	$.ajax({
		async : false,
		url : "/WeixinService/business/queryUserTradeAcctInfo.xhtml",
		type : "post",
		dataType : 'json',
		data : {
			'tradeNo' : tradeAcco
		},
		error : function() {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data, textStatus) {
			var dto=data.tradeAcctList;
			if (dto!=null) {
				$('#mobile').val(dto[0].mobile);
			}
		}
	});
}

/* 查询订单详情 */
function queryTradeInfoByTradeNo() {
	var serialno = getUrlParameter("serialno");
	if (serialno == null || serialno == "") {
		errorRemark("没有查询到订单");
		redirectUrl("/WeixinService/business/query/orderListNew.shtml");
		return;
	}
	$.ajax({
		async : false,
		url : "/WeixinService/business/queryTradeInfoByTradeNo.xhtml",
		data : {
			"serialno" : serialno
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			if (data != null && data.returnCode == "0000") {
				var dto = data.dto;
				$("#serialno").val(dto.serialno);
				$("#tradeAcco").val(dto.tradeacco);
				$("#money").val(formatNumber(dto.subamt, ','));
				$("#oldMoney").val(dto.subamt);
				$("#fundId").val(dto.fundInfoDtoV2.fundId);
				$("#profit").val(dto.fundInfoDtoV2.profit);
				$("#termInDay").val(dto.fundInfoDtoV2.termInDay);
				$("#scale").val(dto.fundInfoDtoV2.scale);
				$("#displayLimit").val(dto.fundInfoDtoV2.displayLimit);
				$("#productMoney").val(dto.fundInfoDtoV2.money);
				$("#moneyStep").val(dto.fundInfoDtoV2.moneyStep);
				$("#orderType").val(dto.orderType);

				queryTradeByTradeNo();
				$("#expectInCome").val('--');
				if (dto.fee != null && dto.fee > 0) {
					$("#fee").html(dto.fee);
				} else {
					$("#feePack").hide();
				}

			}else if(data != null && data.returnCode == "9000"){
        		errorRemark("没有查询到订单");
        		redirectUrl("/WeixinService/business/query/orderListNew.shtml");
        		return;
        	}
		}
	});
}
/* 修改订单金额 */
function modifyAppointRequest() {
	var serialno = $("#serialno").val();
	var tradeAcco = $("#tradeAcco").val();
	var tPassWord = $("#tPassWord").val();
	var randomCode = $("#randomCode").val();
	var oldRenew = $("#oldRenew").val();
	var mobile   = $("#mobile").val();
	var renew=$('.select-way-box li.act').attr('id');
	var renews = $('.select-way-box li.act').attr('data-value');
	
	if (renew == null || renew=='') {
		errorRemark("续投方式不能为空");
		return;
	}else if(oldRenew == renew){
		errorRemark("续投方式与原续投方式不能一致");
		return;
	}else if (tPassWord == null || tPassWord == "") {
		errorRemark("请输入安全码");
		return;
	} else if (randomCode == null || randomCode == "") {
		errorRemark("请输入验证码");
		return;
	} else {
		$.ajax({
			async : false,
			url : "/WeixinService/business/modifyAppointRequest.xhtml",
			data : {
				"serialno" : serialno,
				"tradeAcco" : tradeAcco,
				"tPassWord" : tPassWord,
				"randomCode" : randomCode,
				"renew" : renew,
				"mobile":mobile
			},
			dataType : "json",
			cache : false,
			type : "POST",
			error : function(textStatus, errorThrown) {
				errorRemark("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				if (data.returnCode != null && data.returnCode == "0000") {
					errorRemark("修改成功");
					window.setTimeout(
							"redirectUrl('/WeixinService/business/query/orderDetailNew.shtml?serialno="
									+ serialno + "');", 1000);
				} else if (data.returnCode == "USR-1I01") {
					errorRemark("错误次数过多<br>3小时后重试");
					$("#randomCode,#tPassWord").val("");
					$("#rondomCodeImg").click();
					return;
				} else if (data.returnCode == "USR-1I02") {
					if (data.tPwdErrCount == 1) {
						errorRemark("安全码有误");
					} else if (data.tPwdErrCount > 1 && data.tPwdErrCount < 6) {
						errorRemark("安全码有误<br>还有"
								+ (6 - parseInt(data.tPwdErrCount)) + "次机会");
					}
					$("#randomCode,#tPassWord").val("");
					$("#rondomCodeImg").click();
					return;
				} else {
					errorRemark(data.returnMsg);
					$("#randomCode").val("");
					$("#rondomCodeImg").click();
					return;
				}
			}
		});
	}

}