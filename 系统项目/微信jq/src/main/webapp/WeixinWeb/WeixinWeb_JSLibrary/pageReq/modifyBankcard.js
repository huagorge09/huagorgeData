$(document).ready(function(e) {
	$(".header .top-a h2").html("更换支付银行");
	document.title="更换支付银行";
	 var winHeight=$(window).height()
     var buy_btnHeight=$(".buy_btn").height()
     var buy_btnTop=winHeight-buy_btnHeight
     $(".buy_btn").css("top",buy_btnTop);   
	
	getRandomCode();
	queryTradeInfoByTradeNo();
	getUserRequest("modify-bankcard");/* 此处subPath为页面内行为 */
});
/* 图片验证码	登录页面  */
function getRandomCode(){
	$("#rondomCodeImg").attr("src","/WeixinService/buildimageservlet.xhtml");
}

/* 查询订单列表 */
function queryTradeInfoByTradeNo(){
	var serialno = getUrlParameter("serialno");
	if(serialno == null || serialno == ""){
		errorRemark("没有查询到订单");
		redirectUrl("/WeixinService/business/query/orderList.shtml");
		return;
	}
	$.ajax({
    	async:false,
        url: "/WeixinService/business/queryTradeInfoByTradeNo.xhtml",
        data: {
        	"serialno":serialno
        },
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        }, 
        success : function (data){
        	if(data != null && data.returnCode == "0000"){
        		var dto = data.dto;
        		$("#serialno").val(dto.serialno);
        		$("#tradeAcco").val(dto.tradeacco);
        		$("#fundId").val(dto.fundInfoDtoV2.fundId);
        		$("#orderType").val(dto.orderType);
        		$("#subamt").val(dto.subamt);
        		$("#moneyZero").val(dto.fundInfoDtoV2.money);
        		queryMyBankCard();
        	}else if(data != null && data.returnCode == "9000"){
        		errorRemark("没有查询到订单");
        		redirectUrl("/WeixinService/business/query/orderList.shtml");
        		return;
        	}
        }
    });
}
/* 修改支付银行卡 */
function modifyAppointRequest(){
	var serialno = $("#serialno").val();
	var oldTradeAcco = $("#tradeAcco").val();
	var tradeAcco = $("#bankInfo li.act").attr("data-tradeacco");
	var tPassWord = $("#tPassWord").val();
	var randomCode = $("#randomCode").val();
	var money = parseFloat($("#subamt").val());
	var moneyZero = parseFloat($("#moneyZero").val());
	var balance = parseFloat($("#bankInfo li.act").attr("data-balance"));
	
	if(balance <= 0 && money < moneyZero){
		errorRemark("该银行卡号无产品在途金额<br />请更换银行卡");
    	return;
    }
	
	if(oldTradeAcco == tradeAcco){
		errorRemark("待更换银行卡不能和原订单银行卡一致");
		return;
	}else if(tPassWord == null || tPassWord == ""){
		errorRemark("请输入安全码");
		return;
	}else if(randomCode == null || randomCode == ""){
		errorRemark("请输入验证码");
		return;
	}else{
		$.ajax({
			async:false,
			url : "/WeixinService/business/modifyAppointRequest.xhtml",
			data : {
				"serialno":serialno,
				"tradeAcco":tradeAcco,
				"tPassWord":tPassWord,
				"randomCode":randomCode
			},
			dataType : "json",
			cache : false,
			type:"POST",
			error : function(textStatus,errorThrown){
				errorRemark("网络繁忙，请稍后再试。");  
			},
			success : function(data){
				if(data.returnCode != null && data.returnCode == "0000"){
					errorRemark("修改成功");  
					window.setTimeout("redirectUrl('/WeixinService/business/query/orderDetail.shtml?serialno="+serialno+"');",1000);
				}else if(data.returnCode == "USR-1I01"){
					errorRemark("错误次数过多<br>3小时后重试");
					$("#randomCode,#tPassWord").val("");
					$("#rondomCodeImg").click();
					return;
				}else if(data.returnCode == "USR-1I02"){
					if(data.tPwdErrCount == 1){
						errorRemark("安全码有误");
						$("#tPassWord").val("");
					}else if(data.tPwdErrCount > 1 && data.tPwdErrCount < 6){
						errorRemark("安全码有误<br>还有"+(6-parseInt(data.tPwdErrCount))+"次机会");
					}
					$("#randomCode,#tPassWord").val("");
					$("#rondomCodeImg").click();
					return;
				}else{
					errorRemark(data.returnMsg);
					$("#randomCode").val("");
					$("#rondomCodeImg").click();
					return;
				}
			}
		});
	}
}
/* 我的银行卡信息 */
function queryMyBankCard(){
	var tradeAcco = $("#tradeAcco").val();
	var fundid = $("#fundId").val();
	$.ajax({
		async:false,
		url : "/WeixinService/business/queryMyBankCardNo.xhtml",
		data : { fundid : fundid },
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			errorRemark("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			var htmls = "";
			var className = "";
			var bankName = "";
			var realbankno = "";
			
			if(data.returnCode=='0000'){
		         
		        var htmls = "";
				$.each(data.tradeAcctlist, function(i, item) {
					if(item.tradeAcco == tradeAcco){
						$("#payCardInfo").html(replaceStr2(item.bankAccoDisplay,4,4)+"("+item.bankNm+")");
						htmls += "<li class='act' ";
					}else{
						htmls += "<li ";
					}
					htmls += "id='card_"+i+"' data-bankNm='"+item.bankNm+"' data-bankNo='"+item.bankNo+"' data-bankAccoDisplay='"+item.bankAccoDisplay+"' data-tradeAcco='"+item.tradeAcco+"' data-balance='"+item.balance+"' ";
					htmls += " onclick='selectCard(\"card_"+i+"\")'>";
					htmls += item.bankNm+"（尾号"+(item.bankAccoDisplay||0).substr(item.bankAccoDisplay.length-4)+"）<span>";
					if(item.payMode != null && item.payMode == "7"){/*  线下汇款  线上B2C 线上B2B */
						/*htmls += "仅支持线上支付、线下汇款</span></li>";*/
						htmls += "仅支持线下汇款</span></li>";
					}else if(item.payMode != null && item.payMode == "6"){/*  线下汇款  线上B2C */ 
						/*htmls += "仅支持线上支付、线下汇款</span></li>";*/
						htmls += "仅支持线下汇款</span></li>";
					}else if(item.payMode != null && item.payMode == "5"){/*  线下汇款  线上B2B */
						/*htmls += "仅支持线上支付、线下汇款</span></li>";*/
						htmls += "仅支持线下汇款</span></li>";
					}else if(item.payMode != null && item.payMode == "4"){/*  线下汇款 */
						/*htmls += "仅支持线下汇款</span></li>";*/
						htmls += "仅支持线下汇款</span></li>";
					}else if(item.payMode != null && item.payMode == "3"){/*  线上B2C 线上B2B */
						/*htmls += "仅支持线上支付</span></li>";*/
						htmls += "仅支持线下汇款</span></li>";
					}else if(item.payMode != null && item.payMode == "2"){/*  线上B2C */
						/*htmls += "仅支持线上支付</span></li>";*/
						htmls += "仅支持线下汇款</span></li>";
					}else if(item.payMode != null && item.payMode == "1"){/*  线上B2B */
						/*htmls += "仅支持线上支付</span></li>";*/
						htmls += "仅支持线下汇款</span></li>";
					}else{
						/*htmls += "仅支持线上支付、线下汇款</span></li>";*/
						htmls += "仅支持线下汇款</span></li>";
					}
					
				});
				$("#bankInfo").html(htmls);
			}
		}
	});
}
function selectCard(_id){
	$("#bankInfo li").removeClass("act");
	$("#"+_id).addClass("act");
	$("#payCardInfo").html(replaceStr2($("#"+_id).attr("data-bankAccoDisplay"),4,4)+"("+$("#"+_id).attr("data-bankNm")+")");
	getUserRequest("modify-bankcard-select");/* 此处subPath为页面内行为 */
	setTimeout("$('#showCards').hide();",500);
}
$('.cover-bg').on('click', function() {
	setTimeout("$('.cover-bg').hide()",500);
});
function addBank(){
	var param=getUrlParameter("serialno");
	window.location.href="/WeixinService/business/bank/addBank.shtml?type=1&serialno="+param;
}