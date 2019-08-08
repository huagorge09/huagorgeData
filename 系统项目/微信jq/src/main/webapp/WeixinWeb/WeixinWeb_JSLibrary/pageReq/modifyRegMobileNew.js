﻿﻿/* 验证码读秒 */
var timer = 59;
/* 读秒 */
function countDown(){
	if(timer == 0){
		$("#AgetCode").attr('href',"javascript:getMobileVerifyCode()");
		$('#AgetCode').html("重新获取");
		timer = 59;
	}else{
		$('#AgetCode').html(timer+"s重新获取");
		timer--;
		setTimeout('countDown()',1000);
	}
}

$(document).ready(function(e) {
	cmwaOnkeyup("tPassWord",tPswCheckParam);
	cmwaOnkeyup("mobile;smsCode",smsCheckParam);
	cmwaOnkeyup("lPassword;confirmPassword",pswChechParam);
	queryUserInfo();
	$(".header .top-a h2").html("修改手机号码");
	document.title="修改手机号码";
	checkHasSetTpassword();
	getUserRequest("modify-reg-mobile-paypsd");/* 此处subPath为页面内行为 */
});
/* 验证是否设置安全码 */
function checkHasSetTpassword(){
	$.ajax({
		async:false,
		url : "/WeixinService/business/checkHasSetTpassword.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			
		},
		success : function(data){
			var returnCode = data.returnCode;
			if(returnCode == "9001"){
				errorRemark("您还未实名鉴权，请先实名鉴权！");
				//window.setTimeout("redirectUrl('/WeixinService/business/bank/bankAuthNew.shtml')",2000);
				window.setTimeout("redirectUrl('/#/cardinfo')",2000);
			}else if(returnCode == "9000"){
				errorRemark("您还未设置安全码<br>请先设置安全码！");
				window.setTimeout("redirectUrl('/WeixinService/business/user/setTPasswordNew.shtml')",2000);
			}
		}
	});
}	
/* 验证安全码界面 验证输入框有效性 */
function tPswCheckParam(){
	var tPassWord = $("#tPassWord").val();
	if(tPassWord == null || tPassWord == ""){
		$("#modify_btn_01 a").removeClass("act").attr("href","javascript:void(0);");
		return false;
	}else{
		$("#modify_btn_01 a").addClass("act").attr("href","javascript:verifyTPsw();");
	}
}
/* 注册 验证验证码界面 验证输入框有效性 */
function smsCheckParam(){
	var mobile = $("#mobile").val();
	var smsCode = $("#smsCode").val();
	var sessionID = $("#sessionID").val();
	var hasSetLPsw = $("#hasSetLPsw").val();
	
	if(mobile == null || mobile == "" || !isMobile(mobile)){
		$("#modify_btn_02 a").removeClass("act").attr("href","javascript:void(0);");
		return;
	}else if(smsCode == null || smsCode.trim() == ""){
		$("#modify_btn_02 a").removeClass("act").attr("href","javascript:void(0);");
		return;
	}else{
		if(hasSetLPsw != null && hasSetLPsw == "Y"){
			$("#modify_btn_02 a").addClass("act").attr("href","javascript:checkSmsCode();");
		}else{
			$("#modify_btn_02 a").addClass("act").attr("href","javascript:modifyUserMobile();");
		}
	}
}
/* 设置登录密码界面  验证输入框有效性 */
function pswChechParam(){
	var lPassword = $("#lPassword").val();
	var confirmPassword = $("#confirmPassword").val();
	if(lPassword == null || lPassword == ""){
		$("#modify_btn_03 a").removeClass("act").attr("href","javascript:void(0);");
		return;
	}else if(confirmPassword == null || confirmPassword == ""){
		$("#modify_btn_03 a").removeClass("act").attr("href","javascript:void(0);");
		return;
	}else{
		$("#modify_btn_03 a").addClass("act").attr("href","javascript:modifyMobile();");
	}
}
/* 验证安全码 */
function verifyTPsw(){
	getUserRequest("modify-reg-mobile-input");/* 此处subPath为页面内行为 */
	var tPassWord = $("#tPassWord").val();
	if(tPassWord == null || tPassWord.length < 6 || tPassWord.length > 16){
		errorRemark("安全码格式错误");
		$("#tPassWord").val("");
		$("#modify_btn_01 a").removeClass("act").attr("href","javascript:void(0);");
		return;
	}
	if(tPassWord != null && tPassWord != ""){
		$.ajax({
	    	async:true,
	        url: "/WeixinService/business/checkTPswByModifyMobile.xhtml",
	        data: {"tPassWord": tPassWord},
	        dataType: "json",
	        cache: false,
	        type: 'post',
	        error : function(textStatus, errorThrown) {
				errorRemark("网络繁忙，请稍后再试。");
	            return ;
	        }, 
	        success : function (data){
	        	if(data.returnCode != null && data.returnCode == "USR-1I00"){
					$("#section_01").hide();
					$("#section_02").show();
				}else if(data.returnCode == "USR-1I01"){
					errorRemark("错误次数过多<br>3小时后重试");
					$("#tPassWord").val("");
					$("#modify_btn_01 a").removeClass("act").attr("href","javascript:void(0);");
					return;
				}else if(data.returnCode == "USR-1I02"){
					if(data.tPwdErrCount == 1){
						errorRemark("安全码有误");
					}else if(data.tPwdErrCount > 1 && data.tPwdErrCount < 6){
						errorRemark("安全码有误<br>还有"+(6-parseInt(data.tPwdErrCount))+"次机会");
					}
					$("#tPassWord").val("");
					$("#modify_btn_01 a").removeClass("act").attr("href","javascript:void(0);");
					return;
				}else{
					errorRemark(data.returnMsg);
					$("#tPassWord").val("");
					$("#modify_btn_01 a").removeClass("act").attr("href","javascript:void(0);");
					return;
				}
	        }
	    });
	}
}
/* 获取验证码 ，检验手机号码和返回验证码 */
function getMobileVerifyCode(){
	var mobile = $("#mobile").val();
	if(mobile == null || mobile == "" || !isMobile(mobile)){
		errorRemark("请输入正确的手机号码");
		return;
	}else{
	    $.ajax({
	    	async:false,
	        url: "/WeixinService/setUp/getVerifyCodeByModifyMobile.xhtml",
	        data: {"mobile": mobile},
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) { 
				errorRemark("网络繁忙，请稍后再试");
	        }, 
	        success : function (data){
	        	if((data.errorCode=='0000' && data.sessionID != "nullId") || (data.returnCode=='0000' && data.returnCode != "nullId")){
	        		/* 验证码发送成功 */
	        	  	$("#sessionID").val(data.sessionID);
	        	  	$("#hasSetLPsw").val(data.hasSetLPsw);
	        		
	        	  	$("#AgetCode").attr('href',"javascript:void(0)");
	        	  	/* 读秒 */
	        	  	countDown();
	        	  	if(data.hasSetLPsw == "Y"){
	        	  		$("#modify_btn_02 a").attr("href","javascript:checkSmsCode();");
	        	  	}
	        	}else if((data.errorCode=='9999')||(data.returnCode=='9999')){
					errorRemark("网络繁忙，请稍后再试");
	        	}else if((data.errorCode=='USR-A017')||(data.returnCode=='USR-A017')){
					errorRemark("该手机已注册");
	        	}else{
					errorRemark(data.returnMsg);
	        	}
	        }       
	    });
	}
}
/* 验证短信验证码 */
function checkSmsCode(){
	var mobile = $("#mobile").val();    
	var sessionID = $("#sessionID").val();    
	var smsCode = $.trim($("#smsCode").val()); 
	
	if(isMobile(mobile)){
	 	$.ajax({
	    	async:true,
	        url: "/WeixinService/setUp/checkSmsCode.xhtml",
	        data: {
	        	"mobile": mobile,
	        	"sessionID": sessionID,
	        	"smsCode": smsCode
	        },
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) { 
				errorRemark("网络繁忙，请稍后再试");
	        }, 
	        success : function (data){
	        	var returnCode = data.returnCode;
	        	
	        	if(returnCode != null && returnCode == "0000"){
	        		$("#section_02").hide();
	        		$("#section_03").show();
	        	}else if(returnCode == "9999"){
	        		errorRemark("网络繁忙，请稍后再试");
	        	}else if(returnCode == "9998"){
	        		errorRemark("验证码已失效<br>请重新获取");
	        	}else if(returnCode == "9997"){
	        		errorRemark("验证码已失效<br>请重新获取");
	        	}else{
	        		errorRemark("验证码错误");
	        	}
	        }       
	    });
	}
}
/* 修改手机号码  需要设置登录密码 */
function modifyMobile(){
	var mobile = $("#mobile").val();
	var lPassWord = $("#lPassword").val();
	var comfirmLPsw = $("#confirmPassword").val();
	if(mobile == null || mobile == "" || !isMobile(mobile)){
		errorRemark("请输入正确的手机号码");
		return;
	}else if(lPassWord == null || lPassWord == ""){
		errorRemark("请输入登录密码");
		return;
	}else if(lPassWord.length < 6 || lPassWord.length > 16 || isNaN(lPassWord) || !Validater.isPureNumber(lPassWord)){
		errorRemark("请输入正确的登录密码");
		return;
	}else if(comfirmLPsw == null || comfirmLPsw != lPassWord){
		errorRemark("两次密码不一致");
		return;
	}else{
		$.ajax({
	    	async:true,
	        url: "/WeixinService/business/modifyMobile.xhtml",
	        data: {
	        	"mobile":mobile,
	        	"lPassWord":lPassWord
	        },
	        dataType: "json",
	        cache: false,
	        type:"post",
	        error : function(textStatus, errorThrown) {
				errorRemark("网络繁忙，请稍后再试。");  
	        }, 
	        success : function (data){
	        	if(data.returnCode != null && data.returnCode == "0000"){
					errorRemark("修改成功！正在跳转到登录页面！");
					//window.setTimeout("goToLogin()",2000);
					window.setTimeout("redirectUrl('/#/?loginOut=false')",2000);
	        	}else{
	        		errorRemark(data.returnMsg);
	        	}
	        }
	    });
	}
}
/* 验证手机短信验证码 并修改手机验证码 不需要设置登录密码 */
function modifyUserMobile(){
	var mobile = $("#mobile").val();
	var smsCode = $.trim($("#smsCode").val());
	var sessionID = $("#sessionID").val();
	if(mobile == null || mobile == "" || !isMobile(mobile)){
		errorRemark("请输入正确的手机号码");
		return;
	}else if(smsCode == null || smsCode == ""){
		errorRemark("请输入短信验证码");
		return;
	}else if(sessionID == null || sessionID == ""){
		errorRemark("请获取短信验证码");
		return;
	}else{
		$.ajax({
	    	async:true,
	        url: "/WeixinService/business/modifyUserMobile.xhtml",
	        data: {
	        	"mobile":mobile,
	        	"smsCode":smsCode,
	        	"sessionID":sessionID
	        },
	        dataType: "json",
	        cache: false,
	        type:"post",
	        error : function(textStatus, errorThrown) {
				errorRemark("网络繁忙，请稍后再试。");
	        }, 
	        success : function (data){
				if(data.returnCode != null && data.returnCode == "0000"){
					errorRemark("修改成功");
					//window.setTimeout("goToLogin()",2000);
					window.setTimeout("redirectUrl('/#/?loginOut=false')",2000);
				}else if(data.returnCode=='6001'){
	        		errorRemark("手机验证码<br>有误");
	        		$("#smsCode").val("");
					$("#modify_btn_02 a").removeClass("act").attr("href","javascript:void(0);");
	        	}else if(data.returnCode=='6002'){
	        		errorRemark("验证码已失效<br>请重新获取");
	        		$("#smsCode").val("");
					$("#modify_btn_02 a").removeClass("act").attr("href","javascript:void(0);");
	        	}else{
					errorRemark(data.returnMsg);
				}
	        }
	    });
	}
}

function deleteCookie(name){
      var date=new Date();
      date.setTime(date.getTime()-10000);
      document.cookie=name+"=v; expires="+date.toGMTString();
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
					//window.setTimeout("redirectUrl('/WeixinService/business/bank/bankAuthNew.shtml')", 1000);
					window.setTimeout("redirectUrl('/#/cardinfo')", 1000);
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