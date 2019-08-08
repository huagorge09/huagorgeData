var timer = 59;
var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */

var eventId = '',pageId = 'wx_realNameId',pageSource = null;
var rOption = {};
var isTrueIdExpireDate='';
$(document).ready(function(e){
	
	var urlParams = getUrlParams();
	pageSource = urlParams['pageSource']?urlParams['pageSource']:pageSource;
	rOption = {pageId:pageId,pageSource:pageSource};
	if(pageSource){
		//记录进入实名操作
		rOption.eventId = 'event_wx_realNameId';
		recordOperation(rOption);
	}
	
	cmwaOnkeyup("bankNumberTemp",checkBankNumberLength);
	cmwaOnkeyup("userName;idNo",checkBtn1Available);
	cmwaOnkeyup("mobile;verifyCode",checkBtn2Available);
	cmwaOnkeyup("txtPassword;txtConfirmPassword",checkBtn3Available);
	// 新增身份证输入校验
	cmwaOnkeyup('idExpireDate',checkIdExpireDate)
	$(".header .top-a h2").html("用户实名");
	document.title="用户实名";
	getUserRequest("bank-auth-input");/* 此处subPath为页面内行为 */
	datePicked()
});

// 时间控件
function datePicked(){


	$("input[name=foever]").click(function () {
		var flag = $(this).is(':checked');
		if (flag) {
			$("#idExpireDate").val("2099-12-31")
			isTrueIdExpireDate = true;
			$("#idExpireDate").attr("disabled", "disabled").css("background", "#fff")
		} else {
			isTrueIdExpireDate = false;
			$("#idExpireDate").removeAttr("disabled").css("background", "#fff");
			$("#idExpireDate").val("")
		}
		checkBtn1Available()
	})

 var calendar = new LCalendar();
//   身份有效期选择范围
 calendar.init({
	 'trigger': '#idExpireDate', //标签id
	 'type': 'date', //date 调出日期选择 datetime 调出日期时间选择 time 调出时间选择 ym 调出年月选择,
	 'minDate': '1989-1-1', //最小日期
	 'maxDate': (new Date().getFullYear()+100) + '-' + 12 + '-' + 31 //最大日期
	 });
}

function checkIdExpireDate(){
	//  校验身份证有效期
	var idExpireDate=$('#idExpireDate').val();
	var nowDateTime = Date.parse(new Date().toLocaleDateString());
	var idExpireDateTime = Date.parse(formatDateNum1(idExpireDate))
	if(nowDateTime >= idExpireDateTime){
		errorRemark("身份证有效期须大于当前日期");
		isTrueIdExpireDate=false;
	}else{
		isTrueIdExpireDate=true;
	}
	checkBtn1Available();
}
// $('#idExpireDate').on('input',function(){

// 	 })

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

/* 下一步按钮是否可点击  */
function checkBtn1Available() {
	var bankNumber = $.trim($("#bankNumberTemp").val());
	var userName = $.trim($("#userName").val());
	var idNo = $.trim($("#idNo").val());
	var idExpireDate=$.trim($('#idExpireDate').val());

	if (bankNumber != "" && userName != "" && idNo != "" && idExpireDate!="" && isTrueIdExpireDate) {
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
	if(bankNumber.indexOf('.')>0){
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
 *将银行卡格式化为1234 **** **** 5678
 *
 */
function formatBankNumber(bankNumber){
	var str = bankNumber.substring(0,4); 
	str+=" **** **** ";
	str += bankNumber.subString(bankNumber.length-4,bankNumber.length);
	return str;
}

/* 银行卡号码校验 */
function verifyBankNumber() {
	var idno = $.trim($("#idNo").val());
	var name = $.trim($("#userName").val());
	var idtp = "0";
	var bankNumberTemp = $.trim($("#bankNumberTemp").val());
	bankNumberTemp = bankNumberTemp.replace(re_halfSpace, "");
	bankNumberTemp = bankNumberTemp.replace(re_fullSpace, "");
	if (checkBankNumber(bankNumberTemp) && checkName(name) && checkIdNo(idno)) {

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

							verifyIdNo();
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

/* 验证身份证号码是否被除了用户之外的其他用户注册 */
function verifyIdNo() {
	getUserRequest("bank-auth-bankVerify");/* 此处subPath为页面内行为 */
	var idno = $.trim($("#idNo").val());
	var idtp = "0";
	var params = {
		"idno" : idno,
		"idtp" : idtp
	};

	var actionUrl = "/WeixinService/business/checkIdNoByBankAuthentication.xhtml";
	requestWaitDivShow();
	$.ajax({
		async : false,
		url : actionUrl,
		data : params,
		dataType : "json",
		type : 'post',
		cache : false,
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			/**
			 * 用户已注册 0000 代销/直销/母公司系统注册，本交易系统未注册 USR-A001 用户未注册 USR-A002 参数错误
			 * idNo/idType必填 USR-B002 系统运行时不可知异常 USR-8000
			 */

			/*
			 USR-A003 用户未注册
			   ，详见UserInfoexManagerImpl.checkIdNoByBankAuthentication()
			 */
			if ("USR-A002" == data.returnCode || "USR-A001" == data.returnCode
					|| "USR-A003" == data.returnCode) {

				/* 验证通过 */
				$("#div1").hide();

				$("#formatedBankNum").html(
						formatBankNumber($.trim($("#bankNumber").val())));
				$("#bank_name").html($.trim($("#bankName").val()));
				$("#div2").show();
			} else if ("0000" == data.returnCode) {
				errorRemark("证件已被注册");
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
			async : false,
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
		requestWaitDivShow();
		var actionUrl = "/WeixinService/setUp/authCheckVrfCode.xhtml";
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
					}
				} else {
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
					$("#div2NextBtn").removeClass("act").attr("onclick","");
				}
				requestWaitDivClose();
			}
		});
	}
}

function bankAuthentication() {
	getUserRequest("bank-auth-setPayPsd");/* 此处subPath为页面内行为 */
	var bankNumber = $.trim($("#bankNumber").val());
	var bankName = $.trim($("#bankName").val());
	var channelNo = $.trim($("#channelNo").val());
	var bankNo = $.trim($("#bankNo").val());
	var name = $.trim($("#userName").val());
	var idNo = $.trim($("#idNo").val());
	var mobile = $.trim($("#mobile").val());
	var idExpireDate = $.trim($("#idExpireDate").val());

	bankName = encodeURI(bankName);
	name = encodeURI(name);

	var params = {
		"bankNumber" : bankNumber,
		"bankName" : bankName,
		"channelNo" : channelNo,
		"bankNo" : bankNo,
		"name" : name,
		"idNo" : idNo,
		"mobile" : mobile,
		"idExpireDate" : idExpireDate
	};
	var actionUrl = "/WeixinService/business/openAccount.xhtml";

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
				$("#div2").hide();
				$(".header .top-a h2").html("设置安全码");
				document.title="设置安全码";
				$("#div3").show();
			} else if (data.returnCode == "66") {/* 支付渠道接口返回 */
				errorRemark("您输入的身份信息和银行卡信息不匹配，请核对后重新输入");
				$("#div2").hide();
				$("#div1").show();
			} else if (data.returnCode == "8000") {
				errorRemark(data.returnMsg);
			} else if (data.returnCode == "9003") {
				errorRemark(data.returnMsg);
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
		}
	});
}

function checkBtn3Available() {
	var pass1 = $.trim($("#txtPassword").val());
	var pass2 = $.trim($("#txtConfirmPassword").val());

	if (pass1 != "" && pass2 != "") {
		$("#div3NextBtn").addClass("act");
		$("#div3NextBtn").attr("href", "javascript:setPassword()");
	} else {
		$("#div3NextBtn").removeClass("act");
		$("#div3NextBtn").attr("href", "javascript:void(0)");
	}
}

/* 检查【安全码】 */
function checkedTpsw() {
	var tPassword = $("#txtPassword").val();
	var tpswLwngth = tPassword.length;
	if(tPassword == null || tPassword == ""){
		errorRemark("请输入安全码！");
		return false;
	}else if(tpswLwngth < 8 || tpswLwngth > 16){
		errorRemark("请设置8位以上数字<br>+字母的安全码");
		return false;
	}else if(Validater.hasNullCharacter(tPassword)) {
		errorRemark("不支持空字符，建议使用数字符号字母组合！");
		return false;
	} else if(!Validater.isTradePassword(tPassword)){
		errorRemark("请设置8位以上数字<br>+字母的安全码");
		return false;
	}else{
		return true;
	}
}

/*  检查【确认安全码】 */
function checkedComfirmTpsw() {
	var tPassword = $("#txtPassword").val();
	var confirmTpassword = $("#txtConfirmPassword").val();
	if (confirmTpassword != tPassword) {
		errorRemark("两次安全码不一致");
		return false;
	} else {
		return true;
	}
}

/*  设置安全码 */
function setPassword() {
	getUserRequest("bank-auth-success");/* 此处subPath为页面内行为 */
	var tPassword = $("#txtPassword").val();
	if (!checkedTpsw()) {
		return;
	} else if (!checkedComfirmTpsw()) {
		return;
	} else {
		$.ajax({
			async : false,
			url : "/WeixinService/business/setTpassword.xhtml",
			data : {
				tPassword : tPassword
			},
			type : "POST",
			dataType : "json",
			cache : false,
			error : function(textStatus, errorThrown) {
				errorRemark("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				var returnCode = data.returnCode;
				var returnMsg = data.returnMsg;

				if (returnCode == 'USR-1I00') {
					$("#div3").hide();
					$(".header .top-a h2").html("用户注册");
					document.title="用户注册";
					$("#div4").show();
					
					//实名完成
					rOption.eventId='event_wx_RealNamecommit';
					recordOperation(rOption);
					
					//合格投资者认证开关star
					var param=queryParamList("SYSTEM","ACINVCONF","");
					var pmnm=""
					for(var i=0;i<param.length;i++){
						if(param[i].pmco=="MAIN"){
							pmnm=param[i].pmnm;
						}
				    }
					if(pmnm=="1"){  //开启认证
					   qualified()
					}
					//合格投资者认证开关end

					// MGM为用户创建积分记录STAR
					var openMgm=queryParamList("SYSTEM","enableIntegral","")[0].pmco
					if(openMgm=="1"){
					  	queryUserDeatail()
					}
         	// MGM为用户创建积分记录END

				} else if (returnCode == 'USR-1I01') {
					errorRemark("设置安全码失败：用户已锁定");
				} else if (returnCode == 'USR-1I95') {
					errorRemark("设置安全码失败：非交易用户,请先绑定银行卡");
				} else if (returnCode == '8000') {
					errorRemark(returnMsg);
					/* 跳转到登陆页面，判断微信和其他ie，分别跳转到不同的登陆页面 */
					window.setTimeout("goToLogin()", 2000);
				} else {
					errorRemark("网络繁忙，请稍后再试");
				}
			}
		});
	}
}
/**
 * 合格投资者信息弹窗
 */
function qualified(){
	setTimeout(function(){
        var dialog=$(document).dialog({
	        type : 'confirm',
	        closeBtnShow: true,
	        content: '根据监管要求，需要您完成合格投资者认证',
	        buttonTextConfirm:"线下认证",
	        buttonTextCancel:"线上认证",
	        onClickConfirmBtn: function(){
	             dialog.close()
	        },
	        onClickCancelBtn : function(){
	             location.href="/WeixinService/business/qualified/qualifiedNew.shtml";
	        }
	    });  
	},1000)
}

//  为用户创建积分信息
function queryUserDeatail() {
	$.ajax({
		url: '/WeixinService/business/integral/getIntegralInfo.xhtml',
		data: {},
		dataType: 'json', //服务器返回json格式数据
		type: 'get', //HTTP请求类型
		success: function(data) {
		}
	})
}

/**
 * 日期格式转换 '-'
 * @param date
 * @returns
 */
function formatDateNum1(date) {
  if (date == null || date == '') {
    return "";
  }
  date = unformat1(date);
  return parseInt(date.substr(0, 4), 10) + "/" + parseInt(date.substr(4, 2), 10) + "/" + parseInt(date.substr(6, 2), 10);
}
