﻿var timer = 59;
var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */

$(document).ready(function(e){
	cmwaOnkeyup("mobile;verifyCode",checkBtn2Available);
	cmwaOnkeyup("txtPassword;txtConfirmPassword",checkBtn3Available);
	queryUserInfo();
	$(".header .top-a h2").html("找回安全码");
	document.title="找回安全码";
	queryMyBankCard();
	getUserRequest("reset-TPpassword");/* 此处subPath为页面内行为 */
});

/* 读秒 */
function countDown(){
	
	if(timer == 0){
		$("#AgetCode").attr('href',"javascript:getMobileVerifyCode()");
		$('#AgetCode').html("重新获取");
		timer = 60;
	}else{
		$('#AgetCode').html(timer+"s重新获取");
		timer--;
		setTimeout('countDown()',1000);
	}
}
function toNext(){
	
	var bankNumber = $("#cardNums").attr("data-bankno");
	var str = bankNumber.substring(0,4); 
	str+=" **** **** ";
	str += bankNumber.subString(bankNumber.length-4,bankNumber.length);
	$("#bank_name").html(str+" "+$("#cardNums").attr("data-bankname"));
	$("#div1").hide();
	$("#div2").show();
}
/* 我的银行卡信息 */
function queryMyBankCard(){
	$.ajax({
		async:false,
		url : "/WeixinService/business/queryMyBankCardNo.xhtml",
		data : {
			cleartext : "Y"
		},
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data){
			
			if(data.returnCode=="0000"){
				
				var htmls1 = '';
				var onclickVal = '';
				var valTemp = '';
				if(data.tradeAcctlist!=null){
					
					$.each(data.tradeAcctlist, function(i, item) {
						onclickVal = "sureCard('f"+i+"');";
						if(i == 0){
							var tempBankNumber = item.bankAccoDisplay;
							var str = tempBankNumber.substring(0,4); 
							str+=" **** **** ";
							str += tempBankNumber.subString(tempBankNumber.length-4,tempBankNumber.length);
							
							valTemp = str+'('+item.bankNm+')';
							$("#cardNums").html(valTemp);
							$("#cardNums").attr({"data-bankNo":item.bankAccoDisplay});
							$("#cardNums").attr({"data-bankName":item.bankNm});
							$("#cardNums").attr({"data-channelNo":item.bankNo});
							$("#cardNums").attr({"data-realBankNo":item.realBankNo});
							$("#cardNums").attr({"data-cardId":"f"+i});
							$("#cardNums").attr({"onclick":"showBankCardList()"});

							htmls1 += "<li id=\"f"+i+"\" data-bankNo='"+item.bankAccoDisplay+"' data-bankName='"+item.bankNm+"' data-channelNo='"+item.bankNo+"' data-realBankNo='"+item.realBankNo+"' data-amountOneDay='"+item.dtAmtLimit+"' class=\"act\" onclick=\""+onclickVal+"\" >"+item.bankNm+"(尾号"+item.bankAccoDisplay.subString(item.bankAccoDisplay.length-4)+")<span>仅支持线下汇款</span></li>";	
						}else{
					        htmls1 += "<li id=\"f"+i+"\" data-bankNo='"+item.bankAccoDisplay+"' data-bankName='"+item.bankNm+"' data-channelNo='"+item.bankNo+"' data-realBankNo='"+item.realBankNo+"' data-amountOneDay='"+item.dtAmtLimit+"' onclick=\""+onclickVal+"\">"+item.bankNm+"(尾号"+item.bankAccoDisplay.subString(item.bankAccoDisplay.length-4)+")<span>仅支持线下汇款</span></li>";
						}
					});
					
					$("#bankList_off").html(htmls1);
				}else{
					errorRemark("未绑定银行卡，请先绑定银行卡");
					window.setTimeout('redirectUrl("/WeixinService/business/bank/bankAuth.shtml")',2000);
				}
				
			}else if(data.returnCode=="8000"){
				/* session失效，重新登录 */
				errorRemark(data.returnMsg);
				window.setTimeout("goToLogin()",2000);
			}else{
				errorRemark("网络繁忙，请稍后再试。");
			}
			
		}
	});
}
function showBankCardList(){
	$("#bankcardList").show();
}
/* 确定选择银行卡 */
function sureCard(_id){
	$("#cardNums").attr({"data-bankNo":$("#"+_id).attr("data-bankNo")});
	$("#cardNums").attr({"data-bankName":$("#"+_id).attr("data-bankName")});
	$("#cardNums").attr({"data-channelNo":$("#"+_id).attr("data-channelNo")});
	$("#cardNums").attr({"data-realBankNo":$("#"+_id).attr("data-realBankNo")});
	$("#cardNums").attr({"data-cardId":_id});
	$("#cardNums").html($("#"+_id).attr("data-bankName")+"(尾号"+$("#"+_id).attr("data-bankNo").subString($("#"+_id).attr("data-bankNo").length-4)+")");
	
	$("#bankList_off li").removeClass("act");
	/* $("#bankList_on li").removeClass("active"); */
	$("#"+_id).parents("ul").addClass("isActive");
	/* $("#n"+_id.subString(1)).addClass("active"); */
	$("#f"+_id.subString(1)).addClass("act");
	
	var bankLimitAmountOneDay = parseFloat($("#"+_id).attr("data-amountOneDay"));
	/* 把选中的银行卡的单日限额，放入隐藏的input中 */
	$("#payLimitation").val(bankLimitAmountOneDay);
	
	setTimeout("$('.cover-bg').hide()",500);
}
	
$('#bank_x').on('click',function(){
	$('.cover-bg').show();
});

$('#cover-content-x').on('click',function(){
	$('.cover-bg').hide();
});

$('.cover-bg').on('click',function(){
	setTimeout("$('.cover-bg').hide()",500);
});
	
/* 查询支持的银行卡列表 */
function querySupportBankList(){
	var url = "/WeixinService/business/getSupportBankDesc.xhtml";
	$.ajax({
    	async:false,
        url: url,
        data: "",
        dataType: "json",
        cache: false,
        type: 'post',
        error : function(textStatus, errorThrown) {  
        	errorRemark("网络繁忙，请稍后再试。");  
        }, 
        success : function (data)
        {
        	if(data.returnCode=="0000"){
        		var bankListStr = data.bankListStr;
        		if(typeof(bankListStr)!='undefined'){
	        		$("#supportBankList").html(bankListStr);
	        		showTips("hintDiv");
        		}else{
        			errorRemark("网络繁忙，暂时无法查询支持的银行列表");
        		}
        	}else{
        		errorRemark("网络繁忙，暂时无法查询支持的银行列表");
        	}	
    	} 
    }); 
}
	
/* 验证手机号码 */
function checkMobile(mobile){
	if(mobile == null || mobile == "")
	{
		errorRemark("手机号码不能为空");
		return false;
	}
	else
	{	
		if(!Validater.isMobilePhoneNumber(mobile))
		{
			errorRemark("请输入正确的手机号");
			return false;
		}
	}
	return true;
}


function checkBtn2Available(){
	var mobile = $.trim($("#mobile").val());
	var verifyCode = $.trim($("#verifyCode").val());
	
	if(mobile!="" && verifyCode!=""){
		$("#div2NextBtn").addClass("act");
		$("#div2NextBtn").attr("href","javascript:checkVrfCode()");
	}else{
		$("#div2NextBtn").removeClass("act");
		$("#div2NextBtn").attr("href","javascript:void(0)");
	}
}


/* 获取验证码 */
function getMobileVerifyCode(){
	var mobile=$.trim($("#mobile").val());
	var number = $.trim($("#cardNums").attr('data-bankno'));
	var bankNumber = number.subString(number.length-4,number.length);
	var bankName = $.trim($("#cardNums").attr('data-bankname'));
	bankName = encodeURI(bankName);
	
	if(checkMobile(mobile)){
		var params = {
		        "mobile": mobile,
		        "bankNumber": bankNumber,
		        "bankName": bankName,
		        "msgType":"1"
		    };
	    var actionUrl = "/WeixinService/setUp/getVerifyCodeByAuthAndPay.xhtml";
		requestWaitDivShow();
	    $.ajax({
	    	async:true,
	        url: actionUrl,
	        data: params,
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) {  
	        	errorRemark("网络繁忙，请稍后再试。");  
	        }, 
	        success : function (data)
	        {
	        	if(data.returnCode=="0000"){
	        		/* 验证码发送成功 */
	        	  	$("#sessionID").val(data.sessionID);
	        	  	$("#AgetCode").attr('href',"javascript:void(0)");
	        	  	/* 读秒 */
	        	  	countDown();
	        	}else if(data.returnCode=="9000"){
	        		errorRemark(data.returnMsg);
	        	}else if(data.returnCode=="9999"){
	        		errorRemark("网络繁忙，请稍后再试");
	        	}else{
	        		errorRemark(data.returnMsg);
	        	}
	        }       
	    });
	}
}
	
/* 验证其他 手机号码、验证码 */
function checkMobileAndVerifyCode(){
	var txtMobile= $.trim($("#mobile").val());
	var verifyCode= $.trim($("#verifyCode").val());
	var sessionID= $.trim($("#sessionID").val());
	/* 验证手机号码 */
	if(!checkMobile(txtMobile)){
		return false;
	}
	if(sessionID==""||sessionID==null){
		errorRemark("请先获取验证码");
		return false;
	}
	if(verifyCode==""||verifyCode==null){
		errorRemark("请填写手机验证码");
		return false;
	} 
	return true;
}
	

/* 验证手机验证码 */
function checkVrfCode(){
	getUserRequest("reset-TPpassword-mobile");/* 此处subPath为页面内行为 */
	var sessionId=$.trim($("#sessionID").val());
	var mobile= $.trim($("#mobile").val());
	var verifyCode= $.trim($("#verifyCode").val());
    
	 if(checkMobileAndVerifyCode()){
		 
	    var actionUrl = "/WeixinService/setUp/authCheckVrfCode.xhtml";
	    $.ajax({
	    	async:true,
	        url: actionUrl,
	        data: {"sessionID": sessionId,
			        "mobile":mobile,
			        "rvrfcode": verifyCode},
	        dataType: "json",
	        cache: false,
	        type: 'post',
	        error : function(textStatus, errorThrown) {  
	        	errorRemark("网络繁忙，请稍后再试。");  
	        }, 
	        success : function (data)
	        {
	        	$("#verifyCode").val("");
	        	if(data.returnCode=="0000"){
	        		/* 验证码验证通过，调用鉴权接口 */
	        		bankAuthentication();
	        		/* 让获取验证码可以重新获取 */
	        		if(timer!=60){
			        	timer = 0;
	        		}
				}else{
					var errorMsg = "";
					if(data.returnCode=="USR-5201"){
						errorMsg = "手机验证码有误";
					}else if(data.returnCode=="USR-5202"){
						errorMsg = "验证码已失效，请重新获取验证码";
					}else if(data.returnCode=="USR-5203"){
						errorMsg = "验证码已失效，请重新获取验证码";
					}else if(data.returnCode=="9002"){
						errorMsg = "当前手机号码不是获取验证码的手机号码";
					}else if(data.returnCode=="9003"){
						errorMsg = "非法请求";
					}else if(data.returnCode=="9999"){
						errorMsg = "网络繁忙，请稍后再试";
					}else{
						errorMsg = data.returnMsg;
					}
					errorRemark(errorMsg);
					$("#div2NextBtn").removeClass("act").attr("onclick","");
				}
					
	        }       
	    }); 
	 }
}

function bankAuthentication(){

	var bankNumber = $.trim($("#cardNums").attr("data-bankNo"));
	var bankName = $.trim($("#cardNums").attr("data-bankName"));
	var channelNo = $.trim($("#cardNums").attr("data-channelNo"));
	var bankNo = $.trim($("#cardNums").attr("data-realBankNo"));
	var name = $.trim($("#userName").val());
	var idNo = $.trim($("#idNo").val());
	var mobile= $.trim($("#mobile").val());
	
	bankName = encodeURI(bankName);
	name = encodeURI(name);
	
	var params = {
	        "bankNumber": bankNumber,
	        "bankName": bankName,
	        "channelNo": channelNo,
	        "bankNo": bankNo,
	        "name": name,
	        "idNo": idNo,
	        "mobile": mobile
	    };
	var actionUrl = "/WeixinService/business/bankAuthenticationByResetTPass.xhtml";
	
	$.ajax({
    	async:true,
        url: actionUrl,
        data: params,
        dataType: "json",
        cache: false,
        type: 'post',
        error : function(textStatus, errorThrown) {  
        	errorRemark("网络繁忙，请稍后再试。");  
        }, 
        success : function (data)
        {
        	if(data.returnCode=="0000"){
				$("#div2").hide();
				$("#div3").show();
        	}else if(data.returnCode=="66"){/* 支付渠道接口返回 */
        		errorRemark("您输入的身份信息和银行卡信息不匹配，请核对后重新输入");
        		$("#div2").hide();
				$("#div1").show();
			}else if(data.returnCode=="8000"){
				errorRemark(data.returnMsg);
			}else if(data.returnCode=="9003"){
				errorRemark(data.returnMsg);
			}else if(data.returnCode=="9999"){
				errorRemark("网络繁忙，请稍后再试");
			}else{
				errorRemark(data.returnMsg);
			}
        }       
    }); 
}

function checkBtn3Available(){
	
	var pass1 = $("#txtPassword").val();
	var pass2 = $("#txtConfirmPassword").val();
	
	if(pass1 == null || pass1 == ""){
		$("#div3NextBtn").removeClass("act").attr("onclick","");
		return;
	}else if(pass2 == null || pass2 == ""){
		$("#div3NextBtn").removeClass("act").attr("onclick","");
		return;
	}else {
		$("#div3NextBtn").addClass("act").attr("onclick","setPassword();");
	}
}

/* 检查【安全码】 */
function checkedTpsw(){
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
function checkedComfirmTpsw(){
	var tPassword = $("#txtPassword").val();
	var confirmTpassword = $("#txtConfirmPassword").val();
	if(confirmTpassword != tPassword){
		errorRemark("两次安全码不一致");
		return false;
	}else{
		return true;
	}
}

/*  设置安全码 */
function setPassword(){
	getUserRequest("reset-TPpassword-success");/* 此处subPath为页面内行为 */
	var tPassword = $("#txtPassword").val();
	if(!checkedTpsw()){
		return ;
	}else if(!checkedComfirmTpsw()){
		return;
	}else{
		$.ajax({
			async:true,
			url : "/WeixinService/business/resetTpassword.xhtml",
			data : {
				tPassword:tPassword
			},
			type:"POST",
			dataType : "json",
			cache : false,
			error : function(textStatus,errorThrown){
				errorRemark("网络繁忙，请稍后再试。");  
			},
			success : function(data){
				var returnCode = data.returnCode;
				var returnMsg = data.returnMsg;
				
				if(returnCode == 'USR-1I00'){
					errorRemark("重置安全码成功");
					window.setTimeout("backing()",2000);
				}else if(returnCode == 'USR-1I01'){
					errorRemark("设置安全码失败：用户已锁定");
				}else if(returnCode == 'USR-1I95'){
					errorRemark("设置安全码失败：非交易用户,请先绑定银行卡");
				}else if(returnCode == '8000'){
					errorRemark(returnMsg);
			    	/* 跳转到登陆页面，判断微信和其他ie，分别跳转到不同的登陆页面 */
			    	window.setTimeout("goToLogin()",2000);
				}else if(returnCode == '9003'){
					errorRemark(data.returnMsg);
				}else if(returnCode == '9999'){
					errorRemark("网络繁忙，请稍后再试");
				}else{
					errorRemark(data.returnMsg);
				}
			}
		});
	}
}

function backing(){
	var local = document.referrer;
	if(local=="" || local==null){
		redirectUrl('/WeixinService/index.shtml');
	}else{
		history.go(-1);
	}
}

/*查询用户信息  此处主要查询是否为30用户*/
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
				if(data.userType == null || data.userType != "30"){
					errorRemark("请您完成实名鉴权");
					window.setTimeout("redirectUrl('/WeixinService/business/bank/bankAuth.shtml')", 1000);
				}else if(data.isSetTradePassword == null || data.isSetTradePassword != "Y"){
					errorRemark("请先设置安全码");
					window.setTimeout("redirectUrl('/WeixinService/business/user/setTPassword.shtml')", 1000);
				}else{
					$("#userName").val(data.userName);
					$("#idNo").val(data.idNo);
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