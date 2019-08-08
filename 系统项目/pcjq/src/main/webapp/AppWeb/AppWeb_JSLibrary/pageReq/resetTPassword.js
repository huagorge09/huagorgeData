var timer = 59;
var re_halfSpace = new RegExp(" ", "g");/* 半角空格 */
var re_fullSpace = new RegExp("　", "g");/* 全角空格 */
var timer1 = 15;
$(document).ready(function(e) {
	getUserRequest("pc_resetTPassword_01");
	checkIsLogin();
	$(".nav.fr ul li a").removeClass("current");
	document.title = "重置安全码_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	queryUserInfo();
	queryMyBankCard();
	$(".select-value").click(function() {
		$(".select-text").toggleClass("none");
	});
	$(document).bind("click", function(e) {
		var target = $(e.target);
		if (target.closest(".select-value,.select-text").length == 0) {
			$(".select-text").addClass("none");
		}
	});
	$(window).load(function() {
		$(".select-text-list").mCustomScrollbar();
	});
});
function checkIsLogin() {
	$.ajax({
		async : false,
		url : "/AppService/setUp/queryUserinfoCheckLogin.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			if (data.returnCode == '8000') {
				goToURL("/login/login.shtml");
			} else if (data.returnCode == '0000' && data.isSetTradePassword == 'N') {
				/* 如果未设置安全码，已经鉴权跳转到设置安全码页面，未鉴权跳转到鉴权页面 */
				if (data.userType == '30') {
					gotoSetTPassword();
				} else {
					gotoRealName();
				}
			}
		}
	});
}
/* 读秒 */
function countDown() {
	$("#AgetCode").attr('onclick', "");
	if (timer == 0) {
		$("#AgetCode").attr('onclick', "getMobileVerifyCode()");
		$('#AgetCode').val("重新获取验证码").css({
			background : "#FFF"
		});
		timer = 60;
	} else {
		$('#AgetCode').val(timer + "s重新获取");
		timer--;
		setTimeout('countDown()', 1000);
	}
}
/* 读秒 */
function countDown1() {

	if (timer1 == 0) {
		window.location.href = "/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO";
	} else {
		$("#timer1").html(timer1);
		timer1--;
		setTimeout('countDown1()', 1000);
	}
}
/* 第一步进入页面先查询用户信息，将信息放入文本框 */
function queryUserInfo() {
	var url = "/AppService/business/queryOriginalUserinfo.xhtml";
	$.ajax({
		async : false,
		url : url,
		data : "",
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				if (data.userType == null || data.userType != "30") {
					show_tips2("请您完成实名鉴权", "实名认证", "重新操作", "/AppService/business/bank/realName.shtml", "close_tips2");
				} else if (data.isSetTradePassword == null || data.isSetTradePassword != "Y") {
					show_tips2("请先设置安全码", "实名认证", "重新操作", "/AppService/business/bank/realName.shtml", "close_tips2");
				} else {
					$("#userName").val(data.userName);
					$("#idNo").val(data.idNo);
					$("#userNameAll").val(data.userNameOriginal);
					$("#idNoAll").val(data.idNoOriginal);
				}
			} else if (data.returnCode == "8000") {
				show_tips2(data.returnMsg, "用户登录", "重新操作", "/login/login.shtml", "close_tips2");
			} else {
				show_tips("网络繁忙，请稍后再试");
			}
		}
	});
}
/* 我的银行卡信息 */
function queryMyBankCard() {
	$.ajax({
		async : false,
		url : "/AppService/business/queryMyOriginalBankCardNo.xhtml",
		data : {},
		dataType : "json",
		cache : false,
		type : "POST",
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {

			if (data.returnCode == "0000") {
				if (data.tradeAcctlist != null && data.tradeAcctlist.length > 0) {
					var htmls = "";
					$.each(data.tradeAcctlist, function(i, item) {
						if (i == 0) {
							$("#selectBank").attr("data-bankNo", item.bankAccoDisplay);
							$("#selectBank").attr("data-bankname", item.bankNm);
							$("#selectBank").attr("data-channelNo", item.bankNo);
							$("#selectBank").attr("data-bankype", item.realBankNo);
							$("#selectBank").html(item.bankNm + "(尾号" + item.bankAccoDisplay.subString(item.bankAccoDisplay.length - 4) + ")");
							htmls += "<a href='javascript:selectCard(" + i + ");' class='act' id='card_" + i + "' data-bankNo='" + item.bankAccoDisplay + "' data-bankname='" + item.bankNm
									+ "' data-channelNo='" + item.bankNo + "' data-bankype='" + item.realBankNo + "'>" + item.bankNm + "(尾号"
									+ item.bankAccoDisplay.subString(item.bankAccoDisplay.length - 4) + ")</a>";
						} else {
							htmls += "<a href='javascript:selectCard(" + i + ");' id='card_" + i + "' data-bankNo='" + item.bankAccoDisplay + "' data-bankname='" + item.bankNm + "' data-channelNo='"
									+ item.bankNo + "' data-bankype='" + item.realBankNo + "'>" + item.bankNm + "(尾号" + item.bankAccoDisplay.subString(item.bankAccoDisplay.length - 4) + ")</a>";
						}
					});
					$(".select-text-list").html(htmls);
				} else {
					show_tips2("未绑定银行卡，请先绑定银行卡", "实名认证", "重新操作", "/AppService/business/bank/realName.shtml", "close_tips2");
				}
			} else if (data.returnCode == "8000") {
				show_tips2(data.returnMsg, "用户登录", "重新操作", "/login/login.shtml", "close_tips2");
			} else if (data.returnCode == "9005") {
				show_tips2(data.returnMsg, "实名认证", "重新操作", "/AppService/business/bank/realName.shtml", "close_tips2");
			} else {
				show_tips("网络繁忙，请稍后再试。");
			}
		}
	});
}
function selectCard(textId) {
	$("#selectBank").text($("#card_" + textId).html());
	$("#selectBank").attr("data-bankname", $("#card_" + textId).attr("data-bankname"));
	$("#selectBank").attr("data-bankNo", $("#card_" + textId).attr("data-bankNo"));
	$("#selectBank").attr("data-channelNo", $("#card_" + textId).attr("data-channelNo"));
	$("#selectBank").attr("data-bankype", $("#card_" + textId).attr("data-bankype"));
	$("#card_" + textId).addClass("act").siblings().removeClass("act");
	$(".select-text").addClass("none");
}
/* 进入身份信息页面 */
function toNext() {
	getUserRequest("pc_resetTPassword_02");
	$(".incon-banks").prop("src", "/AppWeb/AppWeb_Images/images/" + queryBankTypeClass($("#selectBank").attr("data-bankype")));
	$(".banksNo").html(checkBankNumberLength($("#selectBank").attr("data-bankNo")));
	$("#div1").hide();
	$("#div2").show();
}
/* 验证手机号码 */
function checkMobile(mobile) {
	if (mobile == null || mobile == "") {
		show_tips("手机号码不能为空");
		return false;
	} else {
		if (!Validater.isMobilePhoneNumber(mobile)) {
			show_tips("手机号码有误");
			return false;
		}
	}
	return true;
}
/* 获取验证码 */
function getMobileVerifyCode() {
	var mobile = $.trim($("#mobile").val());
	var number = $.trim($("#selectBank").attr("data-bankNo"));
	var bankNumber = number.subString(number.length - 4, number.length);
	var bankName = $.trim($("#selectBank").attr("data-bankname"));
	bankName = encodeURI(bankName);

	if (checkMobile(mobile)) {
		var params = {
			"mobile" : mobile,
			"bankNumber" : bankNumber,
			"bankName" : bankName,
			"msgType" : "1"
		};
		var actionUrl = "/AppService/setUp/getVerifyCodeByAuthAndPay.xhtml";
		$.ajax({
			async : true,
			url : actionUrl,
			data : params,
			type : 'post',
			dataType : "json",
			cache : false,
			error : function(textStatus, errorThrown) {
				show_tips("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				if (data.returnCode == "0000") {
					/* 验证码发送成功 */
					$("#sessionID").val(data.sessionID);
					/* 读秒 */
					countDown();
					$("#point-clickafter").show();
				} else if (data.returnCode == "9000") {
					show_tips(data.returnMsg);
				} else if (data.returnCode == "9999") {
					show_tips("网络繁忙，请稍后再试");
				} else {
					show_tips(data.returnMsg);
				}
			}
		});
	}
}
/* 返回上一步 */
function toUp() {
	getUserRequest("pc_resetTPassword_02");
	$("#div2").hide();
	$("#div1").show();
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
		show_tips("请先获取验证码");
		$("#verifyCode").val("");
		return false;
	}
	if (verifyCode == "" || verifyCode == null) {
		show_tips("请填写手机验证码");
		return false;
	}
	return true;
}
/* 验证手机验证码 */
function checkVrfCode() {
	getUserRequest("pc_resetTPassword_03");
	var sessionId = $.trim($("#sessionID").val());
	var mobile = $.trim($("#mobile").val());
	var verifyCode = $.trim($("#verifyCode").val());

	if (checkMobileAndVerifyCode()) {

		var actionUrl = "/AppService/setUp/authCheckVrfCode.xhtml";
		$.ajax({
			async : true,
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
				show_tips("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				$("#verifyCode").val("");
				if (data.returnCode == "0000") {
					/* 验证码验证通过，调用鉴权接口 */
					bankAuthentication();
					/* 让获取验证码可以重新获取 */
					if (timer != 60) {
						timer = 0;
					}
					$(".pay-schedule .pay-order.schedule02").addClass("current");
				} else {
					var errorMsg = "";
					if (data.returnCode == "USR-5201") {
						errorMsg = "手机验证码有误";
						if (timer != 60) {
							timer = 0;
						}
					} else if (data.returnCode == "USR-5202") {
						errorMsg = "验证码已失效，请重新获取验证码";
						if (timer != 60) {
							timer = 0;
						}
					} else if (data.returnCode == "USR-5203") {
						errorMsg = "验证码已失效，请重新获取验证码";
						if (timer != 60) {
							timer = 0;
						}
					} else if (data.returnCode == "9002") {
						errorMsg = "当前手机号码不是获取验证码的手机号码";
						if (timer != 60) {
							timer = 0;
						}
					} else if (data.returnCode == "9003") {
						errorMsg = "非法请求";
						if (timer != 60) {
							timer = 0;
						}
					} else if (data.returnCode == "9999") {
						errorMsg = "网络繁忙，请稍后再试";
						if (timer != 60) {
							timer = 0;
						}
					} else {
						errorMsg = data.returnMsg;
						if (timer != 60) {
							timer = 0;
						}
					}
					show_tips(errorMsg);
					$("#div2NextBtn").removeClass("act").attr("onclick", "");
				}
			}
		});
	}
}
/* 调用鉴权接口 */
function bankAuthentication() {
	var bankNumber = $.trim($("#selectBank").attr("data-bankNo"));
	var bankName = $.trim($("#selectBank").attr("data-bankname"));
	var channelNo = $.trim($("#selectBank").attr("data-channelNo"));
	var bankNo = $.trim($("#selectBank").attr("data-bankype"));
	var name = $.trim($("#userNameAll").val());
	var idNo = $.trim($("#idNoAll").val());
	var mobile = $.trim($("#mobile").val());

	bankName = encodeURI(bankName);
	name = encodeURI(name);

	var params = {
		"bankNumber" : bankNumber,
		"bankName" : bankName,
		"channelNo" : channelNo,
		"bankNo" : bankNo,
		"name" : name,
		"idNo" : idNo,
		"mobile" : mobile
	};
	var actionUrl = "/AppService/business/openAccount.xhtml";

	$.ajax({
		async : false,
		url : actionUrl,
		data : params,
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				$("#div2").hide();
				$("#div3").show();
			} else if (data.returnCode == "66") {/* 支付渠道接口返回 */
				show_tips("验证失败，身份信息不符！");
				$("#div3").hide();
				$("#div2").show();
			} else if (data.returnCode == "8000") {
				show_tips(data.returnMsg);
			} else if (data.returnCode == "9003") {
				show_tips(data.returnMsg);
			} else {
				show_tips(data.returnMsg);
			}
		}
	});
}
/* 检查【安全码】 */
function checkedTpsw() {
	var tPassword = $("#txtPassword").val();
	var tpswLwngth = tPassword.length;
	if (tPassword == null || tPassword == "") {
		show_tips("请输入安全码！");
		return false;
	} else if (tpswLwngth < 8 || tpswLwngth > 16) {
		show_tips("请设置8位以上数字+字母的安全码");
		return false;
	} else if (!Validater.isTradePassword(tPassword)) {
		show_tips("请设置8-16位数字+字母的安全码");
		return false;
	} else {
		return true;
	}
}
/* 检查【确认密码】 */
function checkedComfirmTpsw() {
	var tPassword = $("#txtPassword").val();
	var confirmTpassword = $("#txtConfirmPassword").val();
	if (confirmTpassword == null || confirmTpassword == "") {
		show_tips("请输入确认安全码");
		return false;
	} else if (confirmTpassword != tPassword) {
		show_tips("两次密码不一致");
		return false;
	} else {
		return true;
	}
}

/* 设置密码 */
function setPassword() {
	var tPassword = $("#txtPassword").val();
	if (!checkedTpsw()) {
		return;
	} else if (!checkedComfirmTpsw()) {
		return;
	} else {
		$.ajax({
			async : true,
			url : "/AppService/business/setTpassword.xhtml",
			data : {
				tPassword : tPassword
			},
			type : "POST",
			dataType : "json",
			cache : false,
			error : function(textStatus, errorThrown) {
				show_tips("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				var returnCode = data.returnCode;
				var returnMsg = data.returnMsg;
				if (returnCode == 'USR-1I00') {
					$(".pay-schedule .pay-order.schedule03").addClass("current");
					$("#div3").hide();
					$("#div4").show();
					countDown1();
				} else if (returnCode == 'USR-1I01') {
					show_tips("设置安全码失败：用户已锁定");
				} else if (returnCode == 'USR-1I95') {
					show_tips("设置安全码失败：非交易用户,请先绑定银行卡");
				} else if (returnCode == '8000') {
					show_tips(returnMsg);
				} else if (returnCode == '9003') {
					show_tips(data.returnMsg);
				} else if (returnCode == '9999') {
					show_tips("网络繁忙，请稍后再试");
				} else {
					show_tips(data.returnMsg);
				}
			}
		});
	}
}
function queryBankTypeClass(bankype) {
	if (bankype == '007') {/* 招商银行 */
		imgName = 'card_color_zhaohang.png';
	} else if (bankype == '003') {/* 农业银行 */
		imgName = 'card_color_nonghang.png';
	} else if (bankype == '005') {/* 建设银行 */
		imgName = 'card_color_jianhang.png';
	} else if (bankype == '008') {/* 中信银行 */
		imgName = 'card_color_zhongxin.png';
	} else if (bankype == '004') {/* 中国银行 */
		imgName = 'card_color_zhongguo.png';
	} else if (bankype == '012') {/* 光大银行 */
		imgName = 'card_color_guangda.png';
	} else if (bankype == '006') {/* 交通银行 */
		imgName = 'card_color_jiaotong.png';
	} else if (bankype == '002') {/* 工商银行 */
		imgName = 'card_color_gonghang.png';
	} else if (bankype == '011') {/* 兴业银行 */
		imgName = 'card_color_xingye.png';
	} else if (bankype == '601') {/* 平安银行 */
		imgName = 'card_color_pingan.png';
	} else if (bankype == '015') {/* 邮储银行 */
		imgName = 'card_color_youzheng.png';
	} else if (bankype == '009') {/* 浦发银行 */
		imgName = 'card_color_pufa.png';
	} else if (bankype == '060') {/* 广发银行 */
		imgName = 'card_color_guangfa.png';
	} else if (bankype == '014') {/* 民生银行 */
		imgName = 'card_color_minsheng.png';
	} else if (bankype == '017') {/* 华夏银行 */
		imgName = 'card_color_huaxia.png';
	} else if (bankype == '032') {/* 天津银行 */
		imgName = 'card_color_tianjin.png';
	} else {/* 默认的银行卡样式 */
		imgName = 'icon_bank.png';
	}
	return imgName;
}
