var i = 0;
var timer1 = 60;
var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */
$(document).ready(function(){
	getUserRequest("pc_addBank_01");
	$(".nav.fr ul li a").removeClass("current");
	checkIsLogin();
	cmwaOnkeyup("bankNumberTemp", checkBankNumberLength);
	document.title = "添加银行卡_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	queryUserInfo();
})
/* 下一张验证码 注册*/
function getRandomCode(){
	$("#rondomCodeImg").attr("src","/AppService/setUp/buildimageservlet.xhtml?count="+i);
	i++;
}
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
			}else if(data.returnCode=='0000'&&data.isSetTradePassword=='N'){
				/*如果未设置安全码，已经鉴权跳转到设置安全码页面，未鉴权跳转到鉴权页面*/
				if(data.userType=='30'){
					gotoSetTPassword();
				}else{
					gotoRealName();
				}
			}
		}
	});
}
/* 银行鉴权 读秒*/
function countDown1(){
	$("#getVerifyCodeByBankCard").attr('onclick',"");
	if(timer1 == 0){
		$('#getVerifyCodeByBankCard').val("重新获取验证码");
		$("#getVerifyCodeByBankCard").attr('onclick',"getMobileVerifyCode()");
		timer1 = 60;
	}else{
		timer1--;
		$("#getVerifyCodeByBankCard").val(timer1+"s重新获取");
		setTimeout('countDown1()',1000);
	}
}
/*第一步进入页面先查询用户信息，将信息放入文本框 */
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
				if(data.userType == null || data.userType != "30"){
					show_tips2("请您完成实名鉴权","实名认证","重新操作","/AppService/business/bank/realName.shtml","close_tips2");
				}else if(data.isSetTradePassword == null || data.isSetTradePassword != "Y"){
					show_tips2("请先设置安全码","实名认证","重新操作","/AppService/business/bank/realName.shtml","close_tips2");
				}else{
					$("#userName").html(data.userName);
					$("#idNo").html(data.idNo);
					$("#userNameOld").val(data.userNameOriginal);
					$("#idNoOld").val(data.idNoOriginal);
				}
			} else if (data.returnCode == "8000") {
				show_tips2(data.returnMsg,"用户登录","重新操作","/login/login.shtml","close_tips2");
			} else {
				show_tips("网络繁忙，请稍后再试");
			}
		}
	});
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
/* 银行卡格式化 */
function checkBankNumberLength() {
	var temp = $.trim($("#bankNumberTemp").val());
	var bankNumber = temp.replace(re_halfSpace, "");
	bankNumber = bankNumber.replace(re_fullSpace, "");
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
/* 银行卡号码校验 */
function verifyBankNumber() {
	var idno = $.trim($("#idNoOld").val());
	var name = $.trim($("#userNameOld").val());
	var idtp = "0";
	var bl = false;
	var bankNumberTemp = $.trim($("#bankNumberTemp").val());
	bankNumberTemp = bankNumberTemp.replace(re_halfSpace, "");
	bankNumberTemp = bankNumberTemp.replace(re_fullSpace, "");
	if(!checkBankNumber(bankNumberTemp)){
        bl = false;
	}else{
		$.ajax({
	        async : false,
	        url : "/AppService/business/queryBankInfoByBankNumber.xhtml",
	        data : {
	        	"bankNumber" : bankNumberTemp
	        },
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
	                    	$("#bankno").val(data.bankInfoDto.bankNo);
	                    	$("#bankName").val(data.bankInfoDto.bankName);
	                    	$("#bankNumber").val(bankNumberTemp);
							$("#channelNo").val(data.bankInfoDto.channelNo);
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
function gotoDiv2(){
	getUserRequest("pc_addBank_02");
	if(verifyBankNumber()){
		$("#div1").hide();
		$("#div2").show();
		$(".schedule02").addClass("current");
		$(".incon-banks").prop("src","/AppWeb/AppWeb_Images/images/"+queryBankTypeClass($("#bankno").val()));
		$(".banksNo").html(formatBankNumber($("#bankNumberTemp").val()));
	}
}
/* 获取验证码 */
function getMobileVerifyCode(){
	var mobile=$.trim($("#mobile").val());
	var number = $.trim($("#bankNumberTemp").val()).replace(/\s/g,"");
	var bankNumber = number.subString(number.length-4,number.length);
	var bankName = $("#bankName").val();
	bankName = encodeURI(bankName);
	
	if(checkMobile(mobile)){
		var params = {
		        "mobile": mobile,
		        "bankNumber": bankNumber,
		        "bankName": bankName,
		        "msgType":"1"
		    };
	    var actionUrl = "/AppService/setUp/getVerifyCodeByAuthAndPay.xhtml";
	    $.ajax({
	    	async:true,
	        url: actionUrl,
	        data: params,
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) {  
	        	show_tips("网络繁忙，请稍后再试。");  
	        }, 
	        success : function (data)
	        {
	        	if(data.returnCode=="0000"){
	        		/* 验证码发送成功 */
	        	  	$("#sessionID").val(data.sessionID);
	        	  	$("#point-clickafter").show();
	        	  	/* 读秒 */
	        	  	countDown1();
	        	}else if(data.returnCode=="9000"){
	        		show_tips(data.returnMsg);
	        	}else if(data.returnCode=="9999"){
	        		show_tips("网络繁忙，请稍后再试");
	        	}else{
	        		show_tips(data.returnMsg);
	        	}
	        }       
	    });
	}
}
/* 验证手机号码 */
function checkMobile(mobile){
	if(mobile == null || mobile == "")
	{
		show_tips("手机号码不能为空");
		return false;
	}
	else
	{	
		if(!Validater.isMobilePhoneNumber(mobile))
		{
			show_tips("手机号码有误");
			return false;
		}
	}
	return true;
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
		$("#verifyCode").val("");
		return false;
	}
	return true;
}

/* 验证手机验证码 */
function checkVrfCode() {
	getUserRequest("pc_addBank_03");
	var sessionId = $.trim($("#sessionID").val());
	var mobile = $.trim($("#mobile").val());
	var verifyCode = $.trim($("#verifyCode").val());
	/* 让获取验证码可以重新获取 */
	if (timer1 != 60) {
		timer1 = 0;
	}
	if (checkMobileAndVerifyCode()) {
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
					$("#div2NextBtn").removeClass("act").attr("href", "javascript:void(0)");
					var errorMsg = "";
					if (data.returnCode == "USR-5201") {
						errorMsg = "手机验证码有误";
						$("#verifyCode").val("");
					} else if (data.returnCode == "USR-5202") {
						errorMsg = "验证码已失效，请重新获取验证码";
						$("#verifyCode").val("");
					} else if (data.returnCode == "USR-5203") {
						errorMsg = "验证码已失效，请重新获取验证码";
						$("#verifyCode").val("");
					} else if (data.returnCode == "9002") {
						errorMsg = "当前手机号码不是获取验证码的手机号码";
						$("#verifyCode").val("");
					} else if (data.returnCode == "9003") {
						errorMsg = "非法请求";
						$("#verifyCode").val("");
					} else {
						errorMsg = "网络繁忙，请稍后再试";
						$("#verifyCode").val("");
					}
					show_tips(errorMsg);
				}
			}
		});
	}
}

function bankAuthentication() {
	var bankNumber = $.trim($("#bankNumber").val());
	var bankName = $.trim($("#bankName").val());
	var channelNo = $.trim($("#channelNo").val());
	var bankNo = $.trim($("#bankno").val());
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
	var actionUrl = "/AppService/business/addBank.xhtml";

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
					$("#contractSign_span_01").show();
				}
				
				$("#div3").show();
				$("#div2").hide();
				$(".schedule03").addClass("current");
			} else if (data.returnCode == "66") {/* 支付渠道接口返回 */
				show_tips("您输入的身份信息和银行卡信息不匹配，请核对后重新输入");
				$("#div2").hide();
				$("#div1").show();
				$(".schedule02").removeClass("current");
				$(".schedule01").removeClass("current");
			} else if (data.returnCode == "8000") {
				show_tips(data.returnMsg);
			} else if (data.returnCode == "9003") {
				show_tips(data.returnMsg);
			} else if (data.returnCode == "9999") {
				show_tips("网络繁忙，请稍后再试");
			}else if(data.returnCode == 'CMB01'){
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
			} else {
				show_tips(data.returnMsg);
			}
		}
	});
}
/*获取银行卡图标*/
function queryBankTypeClass(bankype){
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
/**
 * 将银行卡格式化为**** **** **** 5678
 * 
 */
function formatBankNumber(bankNumber) {
	var str = "**** **** **** ";
	bankNumber = bankNumber.replace(re_halfSpace, "");
	bankNumber = bankNumber.replace(re_fullSpace, "");
	str += $.trim(bankNumber).subString(bankNumber.length - 4, bankNumber.length);
	return str;
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
				
				$("#contractSign_div_03").show();/*在线签约相关   先关闭入口*/
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
				show_tips3("签约成功，正在回到我的银行卡页面...");
				setTimeout('gotoUrl("/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_BANK")',2000);
			}else{
				$("#contractSign_div_02").show();
				$("#contractSign_div_03").hide();
				show_tips(data.returnMsg);
			}
		}
	});
}
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
function cmb_lose(str){
	$("#"+str).hide();
}
function cmbSubmit(){
	document.cmbForm.submit();
	$("#cmb_01").hide();
	$("#cmb_02").show();
}