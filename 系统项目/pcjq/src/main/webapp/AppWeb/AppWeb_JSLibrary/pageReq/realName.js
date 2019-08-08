var i = 0;
var timer = 60;
var timer1 = 60;
var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */

//当前页面pageId
var pageId = "";
//来源页面ID
var pageSourceId = "";
//事件Id
var eventId = "";
// 积分开关变量
var  isOpenMgm="";

$(document).ready(function(){
	getUserRequest("pc_realName_01");
	$(".nav.fr ul li a").removeClass("current");
	document.title = "实名鉴权_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	checkIsLogin();
	cmwaOnkeyup("bankNumberTemp", checkBankNumberLength);
	
	//当前页面pageId
	pageId = $("#pageId").val();
	//来源页面ID
	pageSourceId = getUrlParameter("pageSourceId");
	if(null == pageSourceId || pageSourceId == ""){
		pageSourceId = pageId;
	}
	eventId = getUrlParameter("eventId");
	if(null == eventId || eventId == ""){
		eventId = "event_rating_realNameId";
	}
	operatingRecord(pageSourceId,pageId,eventId,"");
	//积分开关
	integralOpenOrClose();
	//身份证有限期选择
	idCardInit()

});

function idCardInit() { 
	$("input[name=foever]").click(function () {
		var flag = $(this).is(':checked');
		if (flag) {
			$("#idExpireDate").val("2099-12-31")
			$("#idExpireDate").attr("disabled", "disabled")
		} else {
			$("#idExpireDate").removeAttr("disabled")
			$("#idExpireDate").val("")
		}
	})
}

/*判断用户是否登录，如果没登录就跳转到登录页面，如果登录并且已经鉴权，就跳转到用户中心*/
function checkIsLogin(){
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
			if(data.returnCode=='8000'){
				goToURL("/login/login.shtml");
			}else if(data.returnCode=='0000'&&data.userType=='30'){
				goToURL("/AppService/applicationGroups.shtml");
			}
		}
	});
}
/* 银行鉴权 读秒*/
function countDown1(){
	if(timer1 == 0){
		$('#getVerifyCodeByBankCard').val("重新获取验证码");
		$("#getVerifyCodeByBankCard").attr('onclick',"getMobileVerifyCode1()");
		timer1 = 60;
	}else{
		timer1--;
		$("#getVerifyCodeByBankCard").val(timer1+"s重新获取");
		setTimeout('countDown1()',1000);
	}
}

/* 以下是进入实名认证页面============================= */
/* 查询支持的银行卡列表 */
function querySupportBankList() {
	var url = "/AppService/business/getSupportBankDesc.xhtml";
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
				var bankListStr = data.bankListStr;
				if (typeof (bankListStr) != 'undefined') {
					$("#hintBanke").html(bankListStr);
					$("#hintDiv").show();
				} else {
					show_tips("网络繁忙，暂时无法查询支持的银行列表");
				}
			} else {
				show_tips("网络繁忙，暂时无法查询支持的银行列表");
			}
		}
	});
}
/* 关闭支持银行卡列表 */
function closeBank(){
	$("#hintDiv").hide();
}
/* 银行卡格式化 */
function checkBankNumberLength() {
	var temp = $.trim($("#bankNumberTemp").val());
	var bankNumber = temp.replace(re_halfSpace, "");
	bankNumber = bankNumber.replace(re_fullSpace, "");
	if (isNaN(bankNumber)) {
		show_tips("请输入正确的银行卡号");
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
		show_tips("银行卡号不能为空");
		return false;
	}
	if(bankNumber.indexOf('.')>0){
		show_tips("银行卡号有误");
		return false;
	}
	if (isNaN(bankNumber)) {
		show_tips("银行卡号有误");
		return false;
	}
	if (($.trim($("#bankNumberTemp").val())).length > 30) {
		show_tips("银行卡号不能为空");
		return false;
	}
	return true;
}
/* 银行卡号码校验 */
function verifyBankNumber() {
	var idno = $.trim($("#idNo").val());
	var name = $.trim($("#userName").val());
	var idtp = "0";
	var bl = false;
	var bankNumberTemp = $.trim($("#bankNumberTemp").val());
	bankNumberTemp = bankNumberTemp.replace(re_halfSpace, "");
	bankNumberTemp = bankNumberTemp.replace(re_fullSpace, "");
	if (checkBankNumber(bankNumberTemp) && checkName(name) && checkIdNo(idno)) {

		var params = {
			"bankNumber" : bankNumberTemp
		};
		var actionUrl = "/AppService/business/queryBankInfoByBankNumber.xhtml";
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
					var returnMsg = data.returnMsg;
					if (data.listIsNull == "no") {

						if (data.status == "Y") {
							var bankName = data.bankInfoDto.bankName;

							$("#bankNumber").val(bankNumberTemp);
							$("#bankName").val(bankName);
							$("#channelNo").val(data.bankInfoDto.channelNo);
							$("#bankno").val(data.bankInfoDto.bankNo);
							bl = true;
						} else {
							show_tips(data.returnMsg);
							bl = false;
						}
					} else {
						show_tips(data.returnMsg);
						bl = false;
					}

				} else if (data.returnCode == "9999") {
					show_tips("信息验证未通过，请检查输入信息是否有误");
					bl = false;
				} else {
					show_tips("网络繁忙，请稍后再试");
					bl = false;
				}
			}
		});
	}
	return bl;
}
/* 验证身份证号码是否被除了用户之外的其他用户注册 */
function verifyIdNo() {
	getUserRequest("bank-auth-bankVerify");/* 此处subPath为页面内行为 */
	var idno = $.trim($("#idNo").val());
	var idtp = "0";
	var mobile = $.trim($("#bankPhone").val());
	var number = $.trim($("#bankNumberTemp").val());
	var bankNumber = number.subString(number.length - 4, number.length);
	var bankName = $.trim($("#bankName").val());
	var bl = false;
	var params = {
		"idno" : idno,
		"idtp" : idtp,
		"mobile" : mobile,
		"bankNumber" : bankNumber,
		"bankName" : bankName
	};

	var actionUrl = "/AppService/business/checkIdNoByBankAuthentication.xhtml";
	$.ajax({
		async : false,
		url : actionUrl,
		data : params,
		dataType : "json",
		type : 'post',
		cache : false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			/**
			 * 用户已注册 0000 代销/直销/母公司系统注册，本交易系统未注册 USR-A001 用户未注册 USR-A002 参数错误
			 * idNo/idType必填 USR-B002 系统运行时不可知异常 USR-8000
			 */

			/*
			 * USR-A003 用户未注册
			 * ，详见UserInfoexManagerImpl.checkIdNoByBankAuthentication()
			 */
			if ("USR-A002" == data.returnCode || "USR-A001" == data.returnCode
					|| "USR-A003" == data.returnCode) {
				bl = true;
			} else if ("0000" == data.returnCode) {
				show_tips("证件已被注册");
				bl = false;
			} else if (data.returnCode == "8000") {
				window.location.href="/login/register.shtml";
				bl = false;
			} else {
				show_tips("网络繁忙，请稍后再试");
				bl = false;
			}
		}
	});
	return bl
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
function getMobileVerifyCode1() {
	var status=$("#verifyStatus").val();
	/*if(status == 'no'){*/
		var mobile = $.trim($("#bankPhone").val());
		var number = $.trim($("#bankNumberTemp").val()).replace(/\s/g,"");
		var bankNumber = number.subString(number.length - 4, number.length);
		if (checkMobile(mobile)&&verifyBankNumber()) {
			var bankName = $.trim($("#bankName").val());
			bankName = encodeURI(bankName);
			var params = {
				"mobile" : mobile,
				"bankNumber" : bankNumber,
				"bankName" : bankName,
				"msgType" : "1"
			};
			var actionUrl = "/AppService/setUp/getVerifyCodeByAuthAndPay.xhtml";
			$.ajax({
				async : false,
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
						$("#getVerifyCodeByBankCard").attr('onclick', "javascript:void(0)");
						/* 读秒 */
						countDown1();
						$("#point-clickafter").show();
					} else if (data.returnCode == "9000") {
						show_tips(data.returnMsg);
					} else {
						show_tips("网络繁忙，请稍后再试");
					}
				}
			});
		}
	/*}*/
}
function checkVerfyCode(verifyCode){
	if (verifyCode == "" || verifyCode == null) {
		show_tips("请填写手机验证码");
		return false;
	}
	return true;
}
/* 验证手机验证码 */
function checkVrfCode() {
	getUserRequest("pc_realName_02");
	var sessionId = $.trim($("#sessionID").val());
	var mobile = $.trim($("#bankPhone").val());
	var verifyCode = $.trim($("#verifyCode").val());
	var idno = $.trim($("#idNo").val());
	var name = $.trim($("#userName").val());
	var idExpireDate=$.trim($("#idExpireDate").val());
	var bankNumberTemp = $.trim($("#bankNumberTemp").val());
	bankNumberTemp = bankNumberTemp.replace(re_halfSpace, "");
	bankNumberTemp = bankNumberTemp.replace(re_fullSpace, "");
	/* 让获取验证码可以重新获取 */
	if (timer1 != 60) {
		timer1 = 0;
	}
	if (checkBankNumber(bankNumberTemp) && checkIdNo(idno) && checkName(name) && checkMobile(mobile)&&checkedIdExpireDate()) {
		if (checkMobileAndVerifyCode()) {
			if (verifyBankNumber() && verifyIdNo()) {
				var actionUrl = "/AppService/setUp/authCheckVrfCode.xhtml";
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
						show_tips("网络繁忙，请稍后再试。");
					},
					success : function(data) {
						$("#verifyCode").val("");
						if (data.returnCode == "0000") {
							/* 验证码验证通过，调用鉴权接口 */
							bankAuthentication();
						} else {
							var errorMsg = "";
							if (data.returnCode == "USR-5201") {
								errorMsg = "短信验证码有误！";
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
						}
					}
				});
			}
		}
	}
}
/* 验证其他 手机号码、验证码 */
function checkMobileAndVerifyCode() {
	var txtMobile = $.trim($("#bankPhone").val());
	var verifyCode = $.trim($("#verifyCode").val());
	var sessionID = $.trim($("#sessionID").val());
	/* 验证手机号码 */
	if (!checkMobile(txtMobile)) {
		return false;
	}
	if (sessionID == "" || sessionID == null) {
		show_tips("请获取短信验证码");
		$("#verifyCode").val("");
		return false;
	}
	if (verifyCode == "" || verifyCode == null) {
		show_tips("请填写手机验证码");
		return false;
	}
	return true;
}
/*调用鉴权接口*/
function bankAuthentication() {
	var bankNumber = $.trim($("#bankNumber").val());
	var bankName = $.trim($("#bankName").val());
	var channelNo = $.trim($("#channelNo").val());
	var bankNo = $.trim($("#bankno").val());
	var name = $.trim($("#userName").val());
	var idNo = $.trim($("#idNo").val());
	var mobile = $.trim($("#bankPhone").val());
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
				/*工行签约所需*/
				$("#tradeAcct").val(data.userAcctDto.tradeAcct);
				$("#bankno").val(data.userAcctDto.bankNo);
				if(data.userAcctDto.bankNo != null && data.userAcctDto.bankNo == "002"){
					$("#span_01").show();
				}
				
				$("#realNameDiv").hide();
				$("#tPasswordDiv").show();
				$(".schedule02").addClass("current");
			} else if (data.returnCode == "66") {/* 支付渠道接口返回 */
				show_tips("验证失败，身份信息不符！");
				$("#tPasswordDiv").hide();
				$("#realNameDiv").show();
			} else if (data.returnCode == "8000") {
				show_tips(data.returnMsg);
				window.location.href = "/login/login.shtml";
			} else if (data.returnCode == "9003") {
				show_tips(data.returnMsg);
			} else if(data.returnCode == 'CMB01'){
				$("#cmb_01").show();
				var cmbDto = data.cmbDto;
				$("#merchant").val(cmbDto.merchant);
				$("#uin").val(cmbDto.uin);
				$("#url").val(cmbDto.url);
				$("#timeStamp").val(cmbDto.timeStamp);
				$("#checkSum").val(cmbDto.checkSum);
				$("#mcAccountNo").val(cmbDto.mcAccountNo);
				$("#username").val(cmbDto.username);
				$("#reserve1").val(cmbDto.reserve1);
				$("#reserve2").val(cmbDto.reserve2);
				$("#cmbForm").attr("action",cmbDto.cmbSignUrl);
			}else {
				show_tips(data.returnMsg);
			}
		}
	});
}
function checkedIdExpireDate(){
	var idExpireDate = $.trim($("#idExpireDate").val());
	if (idExpireDate == '') {
		show_tips('请选择身份证有效期')
		return false;
	}
	var nowDateTime = Date.parse(new Date().toLocaleDateString());
	var idExpireDateTime = Date.parse(formatDateNum1(idExpireDate));
	if(nowDateTime >= idExpireDateTime){
		show_tips('身份证有效期须大于当前日期');
		return false;
	}
	return true;
}
/* 检查【安全码】 */
function checkedTpsw() {
	var tPassword = $("#Tpassword").val();
	var tpswLwngth = tPassword.length;
	if(tPassword == null || tPassword == ""){
		show_tips("请输入安全码！");
		return false;
	}else if(tpswLwngth < 8 || tpswLwngth > 16){
		show_tips("请设置8位以上数字+字母的安全码");
		return false;
	}else if(!Validater.isTradePassword(tPassword)) {
		show_tips("请设置8-16位数字+字母的安全码");
		return false;
	}else{
		return true;
	}
}
/*  检查【确认密码】 */
function checkedComfirmTpsw() {
	var tPassword = $("#Tpassword").val();
	var confirmTpassword = $("#rTpassword").val();
	if(confirmTpassword==null||confirmTpassword==""){
		show_tips("请输入确认安全码");
		return false;
	}else if (confirmTpassword != tPassword) {
		show_tips("两次密码不一致");
		return false;
	} else {
		return true;
	}
}
/* 设置密码 */
function setPassword() {
	getUserRequest("pc_realName_03");
	var tPassword = $("#Tpassword").val();
	if (!checkedTpsw()) {
		return;
	} else if (!checkedComfirmTpsw()) {
		return;
	} else {
		$.ajax({
			async : false,
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
					$("#successRealnameDiv").show();
					$("#tPasswordDiv").hide();
					$("#successName").html($("#userName").val());
					$(".schedule03").addClass("current");
					//合格投资者认证开关
					var param= queryParamComm("SYSTEM","ACINVCONF","");
					var pmnm="";
					for(var i=0;i<param.length;i++){
						if(param[i].pmco=="MAIN"){
							pmnm=param[i].pmnm;
						}
				    }
					if(pmnm=="1"){
					   qualified()
					}
					// mgm 积分开关
					if(isOpenMgm=="1"){
						queryMyIntegral()
					}

					operatingRecord(pageSourceId,pageId,"event_realName_commit","");
				} else if (returnCode == 'USR-1I01') {
					show_tips("设置安全码失败：用户已锁定");
				} else if (returnCode == 'USR-1I95') {
					show_tips("设置安全码失败：非交易用户,请先绑定银行卡");
				} else if (returnCode == '8000') {
					show_tips(returnMsg);
					/* 跳转到登录页面*/
					window.location.href="/login/register.shtml";
				} else {
					show_tips("网络繁忙，请稍后再试");
				}
			}
		});
	}
}
/*签约*/
function contractSign(){
	var tradeAcct = $("#tradeAcct").val();
	var bankNo = $("#bankno").val();
	var channelNo = $("#channelNo").val();
	if(bankNo == null || bankNo != "002" || tradeAcct == null || tradeAcct == "" || channelNo == null || channelNo == ""){
		return;
	}
	$.ajax({
		async : false,
		url : "/AppService/business/contractSign.xhtml",
		data : {
			"tradeAcct" : tradeAcct,
			"bankNo" : bankNo,
			"channelNo" : channelNo
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
			
			if(returnCode != null && returnCode == "0000"){
				var dto = data.icbcSignParaDto;
				/*$("#order").attr("ACTION",dto.signOnlineUrl);*/
				
				$("#contractSign_interfaceName").val(dto.interfaceName);
				$("#contractSign_interfaceVersion").val(dto.interfaceVersion);
				$("#contractSign_selserialNo").val(dto.selserialNo);
				$("#contractSign_payNo").val(dto.payNo);
				$("#contractSign_selpayType").val(dto.selpayType);
				$("#contractSign_selcorpId").val(dto.selcorpId);
				$("#contractSign_selaccountNo").val(dto.selaccountNo);
				$("#contractSign_regDate").val(dto.regDate);
				$("#contractSign_HSURL").val(dto.HSURL);
				$("#contractSign_merCertID").val(dto.merCertID);
				$("#contractSign_Language").val(dto.language);
				$("#contractSign_certDate").val(dto.certDate);
				$("#contractSign_allowFinalDate").val(dto.allowFinalDate);
				$("#contractSign_accountNo").val(dto.accountNo);
				$("#contractSign_certData").val(dto.certData);
				
				$("#contractSign_order").submit();
				
				$("#contractSign_div_03").show();
			}
			
		}
	});
}
/*查询签约*/
function queryCommandByBankAccoNo(){
	var tradeAcct = $("#tradeAcct").val();
	var bankNo = $("#bankno").val();
	var channelNo = $("#channelNo").val();
	if(bankNo == null || bankNo != "002" || tradeAcct == null || tradeAcct == "" || channelNo == null || channelNo == ""){
		return;
	}
	$.ajax({
		async : false,
		url : "/AppService/business/queryCommandByBankAccoNo.xhtml",
		data : {
			"tradeAcct" : tradeAcct,
			"bankNo" : bankNo,
			"channelNo" : channelNo
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

			if(returnCode != null && returnCode == "0000"){
				show_tips3("签约成功，正在进入产品超市...");
				setTimeout('gotoUrl("/AppService/business/fund/fundList.shtml")',2000);
			}else{
				$("#contractSign_div_02").show();
				$("#contractSign_div_03").hide();
				show_tips(data.returnMsg);
			}
		}
	});
}
function cmb_lose(str){
	$("#"+str).hide();
}
function cmbSubmit(){
	document.cmbForm.submit();
	$("#cmb_01").hide();
	$("#cmb_02").show();
}

/**
 * /**
 * 页面操作记录
 * @param buried_PageSource	来源页面Id
 * @param buried_PageId		页面Id
 * @param buried_EventId	事件Id
 * @param buried_GroupId	页面分组Id 默认传空
 */
function operatingRecord(buried_PageSource,buried_PageId,buried_EventId,buried_GroupId){
	$.ajax({
        async: true,
        url: "/AppService/buriedData.xhtml",
        data: {
        	'pageSource' : buried_PageSource,
        	'pageId' : buried_PageId,
            'eventId' :buried_EventId,
            'groupId' : buried_GroupId
        },
        dataType: "json",
        cache: false,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {}
    });
}

/**
 * 合格投资者信息弹窗
 */
function qualified(){
	setTimeout(function(){
		 $("#qualifed").djDialog({
		      width:500,
		      title:"温馨提示",
		      cancel:{
		          text:"线下认证",
		          callBack:function(){
		              $("#qualifed").djDialog("close");
		          }
		      },
		      ok:{
		          text:"线上认证",
		          callBack:function(){
		               location.href="/AppService/business/qualified/qualified.shtml";
		          }
		      }
		 });
		 $(".buttonPop").find("a:nth-child(1)").addClass("popbotton2").removeClass("popbotton1").css("background","#fff");
         $(".buttonPop").find("a:nth-child(2)").addClass("popbotton1").removeClass("popbotton2");
         $(".popClose").remove() 
	},1000)
}

//  查询用户积分信息【*总积分和手机号】
function queryMyIntegral() {
    $.ajax({
        url: '/AppService/business/integral/getIntegralInfo.xhtml',
        data: {},
        dataType: 'json', //服务器返回json格式数据
        type: 'get', //HTTP请求类型
        success: function(data) {
        },
        error: function(xhr, type, errorThrown) {
		console.log('积分信息未请求成功')
        }
    });
}


// 积分功能开关
function integralOpenOrClose() {
    $.ajax({
        url: '/AppService/integral/enableIntegral.xhtml',
        data: {},
        dataType: 'json', //服务器返回json格式数据
        type: 'get', //HTTP请求类型
        success: function(data){
            if (data.data== '1') {
                isOpenMgm='1';
                inviterShow();
            } else if(data.data='0'){
                isOpenMgm='0';
            }
        }

    })
}

/**
 * 日期格式转换 '/'
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