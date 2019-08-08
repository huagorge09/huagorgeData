﻿var timer = 59;
var re_halfSpace = new RegExp(" ", "g");/* 半角空格 */
var re_fullSpace = new RegExp("　", "g");/* 全角空格 */

$(document).ready(function(e) {
	cmwaOnkeyup("bankNumberTemp", checkBankNumberLength);
	cmwaOnkeyup("mobile;verifyCode", checkBtn2Available);
	$(".header .top-a h2").html("添加银行卡");
	document.title = "添加银行卡";
	queryUserInfo();
	queryUser();
	getUserRequest("add-bank-input");/* 此处subPath为页面内行为 */
});

/* 读秒 */
function countDown() {
	if (timer == 0) {
		$("#AgetCode").attr('href', "javascript:getMobileVerifyCode()");
		$('#AgetCode').html("重新获取");
		timer = 60;
	} else {
		$('#AgetCode').html(timer + "s重新获取");
		timer--;
		setTimeout('countDown()', 1000);
	}
}

function queryUser() {
	var url = "/WeixinService/business/queryUserinfo.xhtml";
	$.ajax({
		async : false,
		url : url,
		data : "",
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				$("#userNameFormat").html(data.userName);
				$("#idNoFormat").html(data.idNo);
			} else if (data.returnCode == "8000") {
				errorRemark(data.returnMsg);
				/* 跳转到登陆页面，判断微信和其他ie，分别跳转到不同的登陆页面 */
				window.setTimeout("goToLogin()", 2000);
			} else {
				errorRemark("网络繁忙，请稍后再试");
			}
		}
	});
}

/* 查询支持的银行卡列表 */
function querySupportBankList() {
	var url = "/WeixinService/business/getSupportBankDesc.xhtml";
	$.ajax({
		async : false,
		url : url,
		data : "",
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				var bankListStr = data.bankListStr;
				if (typeof (bankListStr) != 'undefined') {
					$("#supportBankList").html(bankListStr);
					showTips("hintDiv");
				} else {
					errorRemark("网络繁忙，暂时无法查询支持的银行列表");
				}
			} else {
				errorRemark("网络繁忙，暂时无法查询支持的银行列表");
			}
		}
	});
}

/* 下一步按钮是否可点击 */
function checkBtn1Available() {
	var bankNumber = $.trim($("#bankNumberTemp").val());
	if (bankNumber != "") {
		$("#div1NextBtn").addClass("act");
		$("#div1NextBtn").attr("href", "javascript:verifyBankNumber()");
	} else {
		$("#div1NextBtn").removeClass("act");
		$("#div1NextBtn").attr("href", "javascript:void(0)");
	}
}

/* 银行卡格式化 */
function checkBankNumberLength() {
	checkBtn1Available();

	var temp = $.trim($("#bankNumberTemp").val());
	var bankNumber = temp.replace(re_halfSpace, "");
	bankNumber = bankNumber.replace(re_fullSpace, "");
	if (isNaN(bankNumber)) {
		errorRemark("请输入正确的银行卡号");
		return false;
	}
	var length = bankNumber.length;
	if (temp.length > 30) {
		temp = temp.substring(0, 30);
		$("#bankNumberTemp").val(temp);
		return false;
	}

	var tempAdd = "";

	for (var i = 0; i < length; i++) {
		if (i % 4 == 0 && i != 0) {
			tempAdd = tempAdd + " " + bankNumber.charAt(i);
		} else {
			tempAdd += bankNumber.charAt(i);
		}
	}

	$("#bankNumberTemp").val(tempAdd);

	return true;
}

/* 银行卡号非空和数字校验 */
function checkBankNumber(bankNumber) {

	if ($.trim(bankNumber) == '') {
		errorRemark("银行卡号不能为空");
		$("#verifyStatus").val("no");/* 标识银行卡号校验未通过，不可进入下一页 */
		return false;
	}
	if (bankNumber.indexOf('.') > 0) {
		errorRemark("请输入正确的银行卡号");
		$("#verifyStatus").val("no");
		return false;
	}
	if (isNaN(bankNumber)) {
		$("#div1Error").html("<i class='redColor'>银行卡号只能为数字</i>");
		errorRemark("请输入正确的银行卡号");
		$("#verifyStatus").val("no");
		return false;
	}
	if (($.trim($("#bankNumberTemp").val())).length > 30) {
		errorRemark("银行卡号不能为空");
		$("#verifyStatus").val("no");
		return false;
	}
	return true;
}

/**
 * 将银行卡格式化为1234 **** **** 5678
 * 
 */
function formatBankNumber(bankNumber) {
	var str = bankNumber.substring(0, 4);
	str += " **** **** ";
	str += bankNumber.subString(bankNumber.length - 4, bankNumber.length);
	return str;
}

/* 银行卡号码校验 */
function verifyBankNumber() {
	getUserRequest("add-bank-verify");/* 此处subPath为页面内行为 */
	var idtp = "0";

	var bankNumberTemp = $.trim($("#bankNumberTemp").val());
	bankNumberTemp = bankNumberTemp.replace(re_halfSpace, "");
	bankNumberTemp = bankNumberTemp.replace(re_fullSpace, "");
	if (checkBankNumber(bankNumberTemp)) {

		var params = {
			"bankNumber" : bankNumberTemp
		};
		var actionUrl = "/WeixinService/business/queryBankInfoByBankNumber.xhtml";
		$.ajax({
			async : false,
			url : actionUrl,
			data : params,
			dataType : "json",
			cache : false,
			type : 'post',
			error : function(textStatus, errorThrown) {
				errorRemark("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				if (data.returnCode == "0000") {
					var returnMsg = data.returnMsg;
					if (data.listIsNull == "no") {

						if (data.status == "Y") {
							var bankName = data.bankInfoDto.bankName;

							$("#bankNumber").val(bankNumberTemp);
							$("#bankName").val(bankName);
							$("#channelNo").val(data.bankInfoDto.channelNo);
							$("#bankNo").val(data.bankInfoDto.bankNo);
							$("#verifyStatus").val("yes");/* 标识银行卡号校验通过， */

							/* 验证通过 */
							$("#div1").hide();

							$("#formatedBankNum").html(formatBankNumber($.trim($("#bankNumber").val())));
							$("#bank_name").html($.trim($("#bankName").val()));
							$("#div2").show();
						} else {
							$("#verifyStatus").val("no");/* 标识银行卡号校验未通过，不可进入下一页 */
							errorRemark(returnMsg);
						}
					} else {
						$("#verifyStatus").val("no");/* 标识银行卡号校验未通过，不可进入下一页 */
						errorRemark(returnMsg);
					}

				} else if (data.returnCode == "9999") {
					$("#verifyStatus").val("no");/* 标识银行卡号校验未通过，不可进入下一页 */
					errorRemark("信息验证未通过，请检查输入信息是否有误");
				} else {
					$("#verifyStatus").val("no");
					errorRemark("网络繁忙，请稍后再试");
				}

			}
		});
	}
}

/* 验证手机号码 */
function checkMobile(mobile) {
	if (mobile == null || mobile == "") {
		errorRemark("手机号码不能为空");
		return false;
	} else {
		if (!Validater.isMobilePhoneNumber(mobile)) {
			errorRemark("请输入正确的手机号");
			return false;
		}
	}
	return true;
}

function checkBtn2Available() {
	var mobile = $.trim($("#mobile").val());
	var verifyCode = $.trim($("#verifyCode").val());

	if (mobile != "" && verifyCode != "") {
		$("#div2NextBtn").addClass("act");
		$("#div2NextBtn").attr("href", "javascript:checkVrfCode()");
	} else {
		$("#div2NextBtn").removeClass("act");
		$("#div2NextBtn").attr("href", "javascript:void(0)");
	}
}

/* 获取验证码 */
function getMobileVerifyCode() {
	var mobile = $.trim($("#mobile").val());
	var number = $.trim($("#bankNumber").val());
	var bankNumber = number.subString(number.length - 4, number.length);
	var bankName = $.trim($("#bankName").val());
	bankName = encodeURI(bankName);
	var $dom = $("#AgetCode");
	if($dom.hasClass('request')){
		errorRemark("请等待请求完毕之后再获取验证码")
		return;
	}
	if (checkMobile(mobile)) {
		var params = {
			"mobile" : mobile,
			"bankNumber" : bankNumber,
			"bankName" : bankName,
			"msgType" : "1"
		};
		var actionUrl = "/WeixinService/setUp/getVerifyCodeByAuthAndPay.xhtml";
		requestWaitDivShow();
		$.ajax({
			async : true,
			url : actionUrl,
			data : params,
			type : 'post',
			dataType : "json",
			cache : false,
			error : function(textStatus, errorThrown) {
				errorRemark("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				if (data.returnCode == "0000") {
					/* 验证码发送成功 */
					$("#sessionID").val(data.sessionID);
					$("#AgetCode").attr('href', "javascript:void(0)");
					/* 读秒 */
					countDown();
				} else if (data.returnCode == "9000") {
					errorRemark(data.returnMsg);
				} else {
					errorRemark("网络繁忙，请稍后再试");
				}
			}
		});
	}
}

/* 验证其他 手机号码、验证码 */
function checkMobileAndVerifyCode() {
	var txtMobile = $.trim($("#mobile").val());
	var verifyCode = $.trim($("#verifyCode").val());
	var sessionID = $.trim($("#sessionID").val());
	/* 验证手机号码 */
	if (!checkMobile(txtMobile)) {
		return false;
	}
	if (sessionID == "" || sessionID == null) {
		errorRemark("请先获取验证码");
		return false;
	}
	if (verifyCode == "" || verifyCode == null) {
		errorRemark("请填写手机验证码");
		return false;
	}
	return true;
}

/* 验证手机验证码 */
function checkVrfCode() {
	var sessionId = $.trim($("#sessionID").val());
	var mobile = $.trim($("#mobile").val());
	var verifyCode = $.trim($("#verifyCode").val());

	if (checkMobileAndVerifyCode()) {
		var actionUrl = "/WeixinService/setUp/authCheckVrfCode.xhtml";
		requestWaitDivShow();
		$.ajax({
			async : false,
			url : actionUrl,
			data : {
				"sessionID" : sessionId,
				"mobile" : mobile,
				"rvrfcode" : verifyCode
			},
			dataType : "json",
			cache : false,
			type : 'post',
			error : function(textStatus, errorThrown) {
				requestWaitDivClose();
				errorRemark("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				$("#verifyCode").val("");
				if (data.returnCode == "0000") {
					/* 验证码验证通过，调用鉴权接口 */
					bankAuthentication();
					/* 让获取验证码可以重新获取 */
					if (timer != 60) {
						timer = 0;
						$('#AgetCode').addClass('request');
					}
				} else {
					$("#div2NextBtn").removeClass("act").attr("href", "javascript:void(0)");
					var errorMsg = "";
					if (data.returnCode == "USR-5201") {
						errorMsg = "手机验证码有误";
					} else if (data.returnCode == "USR-5202") {
						errorMsg = "验证码已失效，请重新获取验证码";
					} else if (data.returnCode == "USR-5203") {
						errorMsg = "验证码已失效，请重新获取验证码";
					} else if (data.returnCode == "9002") {
						errorMsg = "当前手机号码不是获取验证码的手机号码";
					} else if (data.returnCode == "9003") {
						errorMsg = "非法请求";
					} else {
						errorMsg = "网络繁忙，请稍后再试";
					}
					errorRemark(errorMsg);
				}
				requestWaitDivClose();
			}
		});
	}
}

function bankAuthentication() {
	getUserRequest("add-bank-success");/* 此处subPath为页面内行为 */
	var bankNumber = $.trim($("#bankNumber").val());
	var bankName = $.trim($("#bankName").val());
	var channelNo = $.trim($("#channelNo").val());
	var bankNo = $.trim($("#bankNo").val());
	var mobile = $.trim($("#mobile").val());

	bankName = encodeURI(bankName);
	name = encodeURI(name);

	var params = {
		"bankNumber" : bankNumber,
		"bankName" : bankName,
		"channelNo" : channelNo,
		"bankNo" : bankNo,
		"mobile" : mobile
	};
	var actionUrl = "/WeixinService/business/addBank.xhtml";

	$.ajax({
		async : true,
		url : actionUrl,
		data : params,
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			$('#AgetCode').removeClass('request');
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				var fundId = getUrlParameter("fundId");
				var serialno =getUrlParameter("serialno");
				$("#div2").hide();
				if (fundId != "" && fundId != null) {
					$("#div4").show();
				} else if(serialno != "" && serialno != null){
					$("#div4").show();
				}else {
					$("#div3").show();
				}
			} else if (data.returnCode == "66") {/* 支付渠道接口返回 */
				errorRemark("您输入的身份信息和银行卡信息不匹配，请核对后重新输入");
				$("#div2").hide();
				$("#div1").show();
			} else if (data.returnCode == "8000") {
				errorRemark(data.returnMsg);
			} else if (data.returnCode == "9003") {
				errorRemark(data.returnMsg);
			} else if (data.returnCode == "9999") {
				errorRemark("网络繁忙，请稍后再试");
			}else if(data.returnCode == 'CMB01'){
				$("#cmb_01").show();
				var cmbDto = data.cmbDto;
				$("#merchant").val(cmbDto.merchant);
				$("#uin").val(cmbDto.uin);
				$("#url").val(cmbDto.wxUrl);
				$("#timeStamp").val(cmbDto.timeStamp);
				$("#checkSum").val(cmbDto.checkSum);
				$("#mcAccountNo").val(cmbDto.mcAccountNo);
				$("#username").val(cmbDto.username);
				$("#reserve1").val(cmbDto.reserve1);
				$("#reserve2").val(cmbDto.reserve2);
//				$("#cmbForm").attr("action",cmbDto.cmbSignUrl);
				$("#cmbForm").attr("action",cmbDto.cmbWxSignUrl);
				document.cmbForm.submit();
			} else {
				errorRemark(data.returnMsg);
			}
			$('#AgetCode').removeClass('request');
		}
	});
}
/* 查询用户信息 此处主要查询是否为30用户 */
function queryUserInfo() {
	var url = "/WeixinService/business/queryUserinfo.xhtml";
	$.ajax({
		async : false,
		url : url,
		data : "",
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				if (data.userType == null || data.userType != "30") {
					errorRemark("请您完成实名鉴权");
					window.setTimeout("redirectUrl('/WeixinService/business/bank/bankAuth.shtml')", 1000);
				}
			} else if (data.returnCode == "8000") {
				errorRemark(data.returnMsg);
				/* 跳转到登陆页面，判断微信和其他ie，分别跳转到不同的登陆页面 */
				window.setTimeout("goToLogin()", 1000);
			} else {
				errorRemark("网络繁忙，请稍后再试");
			}
		}
	});
}
/* 继续完成订单 */
function goToBuyOrder() {
	var fundId = getUrlParameter("fundId");
	var serialno =getUrlParameter("serialno");
	var period =getUrlParameter("period");
	var type =getUrlParameter("type");
	if (fundId != "" && fundId != null) {
		window.location.href = "/WeixinService/business/pay/buyMoney.shtml?fundId=" + fundId+"&period="+period;
	} 
	if(serialno != "" && serialno != null){
		if(type == 1){
			window.location.href = "/WeixinService/business/query/orderDetail.shtml?serialno=" + serialno;
		}else{
			window.location.href = "/WeixinService/business/pay/confirmBuy.shtml?serialno=" + serialno;
		}
	}
}