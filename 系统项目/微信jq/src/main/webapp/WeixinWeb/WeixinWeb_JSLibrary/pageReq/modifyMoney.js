var re_halfSpace = new RegExp(" ", "g");/* 半角空格 */
var re_fullSpace = new RegExp("　", "g");/* 全角空格 */
var isHasBalence=false;

$(document).ready(function(e) {
	$(".header .top-a h2").html("修改买入金额");
	document.title = "修改买入金额";
	var winHeight = $(window).height()
	var buy_btnHeight = $(".buy_btn").height()
	var buy_btnTop = winHeight - buy_btnHeight
	$(".buy_btn").css("top", buy_btnTop);
	getRandomCode();
    queryTradeInfoByTradeNo();
	getUserRequest("modify-money");/* 此处subPath为页面内行为 */
});
/* 图片验证码 登录页面 */
function getRandomCode() {
	$("#rondomCodeImg").attr("src", "/WeixinService/buildimageservlet.xhtml");
}

/* 查询订单详情 */
function queryTradeInfoByTradeNo() {
	var serialno = getUrlParameter("serialno");
	if (serialno == null || serialno == "") {
		errorRemark("没有查询到订单");
		redirectUrl("/WeixinService/business/query/orderList.shtml");
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
                $("#sartBuying").val(dto.fundInfoDtoV2.sartBuying);
                $("#typeId").val(dto.fundInfoDtoV2.typeId);
				$("#orderType").val(dto.orderType);

				$("#expectInCome").val('--');
				if (dto.fee != null && dto.fee > 0) {
					$("#fee").html(dto.fee);
				} else {
					$("#feePack").hide();
				}
			}else if(data != null && data.returnCode == "9000"){
        		errorRemark("没有查询到订单");
        		redirectUrl("/WeixinService/business/query/orderList.shtml");
        		return;
        	}
            setFundMinBuyMoney()//查询持仓金额
			//checkedMoney();
		}
	});
}
/* 判断预约金额是否符合格式 */
function checkedMoney() {
	var money = unformat($("#money").val());
	var productMoney = $("#productMoney").val();/* 认购起点 */
	var moneyStep = unformat($("#moneyStep").val());/* 认购步长 */
	var displayLimit = parseFloat(unformat($("#displayLimit").val()));/* 剩余额度 */
	var buyType = $("#orderType").val();/* 购买类型 */
	var scale = $("#scale").val();
	var profit = $("#profit").val();
	var termInDay = $("#termInDay").val();
    var sartBuying = $('#sartBuying').val();
	if (!Validater.isPureNumber(money)) {
		errorRemark('请输入正确的金额');
		$("#inCome,#feePack").hide();
		return false;
    } else if ((parseFloat(money) < parseFloat(productMoney)) || (parseFloat(money) - parseFloat(productMoney)) % (parseFloat(moneyStep)) != 0) {

        var typeId = $('#typeId').val();
        if(isHasBalence){
            if(typeId == '0500' || typeId == '0400' || typeId == '0110') {
                if(parseFloat(money) >= parseFloat(sartBuying)) {
                    if((parseFloat(money) - parseFloat(sartBuying))%(parseFloat(moneyStep)) == 0){
                        return true;
                    }else{
                        if(moneyStep < 10000) {
                            errorRemark("本产品" + numDiv(parseFloat(sartBuying), 10000) + "万起购，" + parseFloat(moneyStep) + "元递增");
                        } else {
                            errorRemark("本产品" + numDiv(parseFloat(sartBuying), 10000) + "万起购，" + numDiv(parseFloat(moneyStep), 10000) + "万递增");
                        }
                        return false;
                    }
                }else {
                	 if(moneyStep < 10000) {
                         errorRemark("本产品" + numDiv(parseFloat(sartBuying), 10000) + "万起购，" + parseFloat(moneyStep) + "元递增");
                     } else {
                         errorRemark("本产品" + numDiv(parseFloat(sartBuying), 10000) + "万起购，" + numDiv(parseFloat(moneyStep), 10000) + "万递增");
                     }
                	return false;
                }
            }else{
                if(moneyStep < 10000) {
                    errorRemark("本产品" + numDiv(parseFloat(productMoney), 10000) + "万起购，" + parseFloat(moneyStep) + "元递增");
                } else {
                    errorRemark("本产品" + numDiv(parseFloat(productMoney), 10000) + "万起购，" + numDiv(parseFloat(moneyStep), 10000) + "万递增");
                }
                $("#inCome,#feePack").hide();
                return false;
            }
        }else{
            if(moneyStep < 10000) {
                errorRemark("本产品" + numDiv(parseFloat(productMoney), 10000) + "万起购，" + parseFloat(moneyStep) + "元递增");
            } else {
                errorRemark("本产品" + numDiv(parseFloat(productMoney), 10000) + "万起购，" + numDiv(parseFloat(moneyStep), 10000) + "万递增");
            }
            $("#inCome,#feePack").hide();
            return false;
        }
	} else if (parseFloat(money) > displayLimit && buyType != "3") {
		errorRemark("仅剩余" + (displayLimit > 10000 ? (numDiv(parseFloat(unformat(displayLimit)), 10000) + "万") : formatNumber(displayLimit, ',')) + "额度<br>不能再高了");
		$("#inCome,#feePack").hide();
		return false;
	} else if (buyType == "3" && parseInt(unformat(money)) > scale) {
		errorRemark("预约金额不能高于产品发售规模");
		$("#inCome,#feePack").hide();
		return false;
	} else {
		$("#money").val(formatNumber(money, ','));

		var temp = numDiv(numMulti(numMulti(money, profit), termInDay), 365);

		if (isNaN(temp) || temp <= 0) {
			$("#inCome").hide();
		} else {
			/* 业绩报酬计提基准 */
			$("#expectInCome").text('--');
			$("#inCome").show();
		}
		queryFundRate();
		return true;
	}
}
/* 查询费率 */
function queryFundRate() {
	var money = unformat($("#money").val());
	var fundId = $("#fundId").val();
	$.ajax({
		async : false,
		url : "/WeixinService/business/queryFeeRates.xhtml",
		data : {
			"fundId" : fundId,
			'money' : encodeURI(money)
		},
		dataType : "json",
		cache : false,
		type : "POST",
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == '0000') {
				if (data.rate != null && !isNaN(data.rate)) {
					$("#fee").html(data.rate);
				} else {
					errorRemark("获取费率失败");
				}
			} else {
				errorRemark(data.returnMsg);
			}

		}
	});
}
/* 修改订单金额 */
function modifyAppointRequest() {
	var serialno = $("#serialno").val();
	var tradeAcco = $("#tradeAcco").val();
	var tradeAmt = unformat($("#money").val());
	var tPassWord = $("#tPassWord").val();
	var randomCode = $("#randomCode").val();
	var fee = $("#fee").text();
	var renew = "";
	var oldMoney = $("#oldMoney").val();
	if (!checkedMoney()) {
		return;
	} else if (tradeAmt == oldMoney) {
		errorRemark("待修改金额不能和原订单金额一致");
		return;
	} else if (tPassWord == null || tPassWord == "") {
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
				"tradeAmt" : tradeAmt,
				"tPassWord" : tPassWord,
				"randomCode" : randomCode,
				"fee" : fee,
				"renew" : renew
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
							"redirectUrl('/WeixinService/business/query/orderDetail.shtml?serialno="
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

function setFundMinBuyMoney() {
    var flag=true;
    var typeId=$("#typeId").val();
    $.ajax({
        async:false,
        url:"/WeixinService/business/queryCustTradeInfo.xhtml",
        type:"post",
        dataType:'json',
        data:{
            "fundCode":$("#fundId").val(),
            "tradeAcco":$("#tradeAcco").val()
        },
        success:function(res){
            if(res.returnCode=="0000"){
                if(res.buySatte=="Y" && (typeId == '0400' || typeId == '0500' || typeId == '0110')){
                    isHasBalence = true;
                }
            }
        },
        error:function(){
            show_tips("网络繁忙，请稍后再试。");
        }
    })
    return flag
}