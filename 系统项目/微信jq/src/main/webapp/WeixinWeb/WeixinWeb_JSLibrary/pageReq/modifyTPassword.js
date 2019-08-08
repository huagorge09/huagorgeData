﻿var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */
/* 验证码读秒 */
var timer = 59;
/* 读秒 */
function countDown(){
	if(timer == 0){
		$("#AgetCode").attr('href',"javascript:getSmsByModifyTPsw()");
		$('#AgetCode').html("重新获取");
		timer = 59;
	}else{
		$('#AgetCode').html(timer+"s重新获取");
		timer--;
		setTimeout('countDown()',1000);
	}
}

$(document).ready(function(e) {
	cmwaOnkeyup("tPassWord",checkTPswParam);
	cmwaOnkeyup("smsCode",smsCheckParam);
	cmwaOnkeyup("newTPsw;confirmNewTPsw",setTPswParam);
	queryUserInfo();
	$(".header .top-a h2").html("修改安全码");
	document.title="修改安全码";
	getUserRequest("modify-TPPassword");/* 此处subPath为页面内行为 */
});
/* 验证原安全码验证数据有效性 */
function checkTPswParam(){
	var tPassWord = $("#tPassWord").val();
	if(tPassWord == null || tPassWord == ""){
		$("#reg_btn_01").removeClass("act").attr("onclick","");
		return;
	}else {
		$("#reg_btn_01").addClass("act").attr("onclick","checkTPassword();");
	}
}
/* 修改安全码页面 发送短信验证码界面验证数据有效性 */
function smsCheckParam(){
	var smsCode = $.trim($("#smsCode").val());
	var sessionID = $("#sessionID").val();
	
	if(smsCode == null || smsCode == ""){
		$("#reg_btn_02").removeClass("act").attr("onclick","");
		return;
	}else{
		$("#reg_btn_02").addClass("act").attr("onclick","checkSmsCode();");
	}
}
/* 修改安全码页面 设置安全码界面验证数据有效性 */
function setTPswParam(){
	var newTPsw = $("#newTPsw").val();
	var confirmNewTPsw = $("#confirmNewTPsw").val();
	
	if(newTPsw == null || newTPsw == ""){
		$("#reg_btn_03").removeClass("act").attr("onclick","");
		return;
	}else if(confirmNewTPsw == null || confirmNewTPsw == ""){
		$("#reg_btn_03").removeClass("act").attr("onclick","");
		return;
	}else{
		$("#reg_btn_03").addClass("act").attr("onclick","modifyTPassWord();");
	}
}
/* 验证安全码 */
function checkTPassword(){
	var tPassWord = $("#tPassWord").val();
	
	if(tPassWord == null || tPassWord == ""){
		errorRemark("请输入安全码");
		$("#tPassWord").val("");
		$("#reg_btn_01").removeClass("act").attr("onclick","");
		return;
	}else if(tPassWord.length < 6 || tPassWord.length > 16){
		errorRemark("请输入正确的安全码");
		$("#tPassWord").val("");
		$("#reg_btn_01").removeClass("act").attr("onclick","");
		return;
	}else{
		$.ajax({
			async:false,
			url : "/WeixinService/business/checkTpassword.xhtml",
			data : {
				"tPassWord":tPassWord,
			},
			dataType : "json",
			cache : false,
			type:"POST",
			error : function(textStatus,errorThrown){
				errorRemark("网络繁忙，请稍后再试");
			},
			success : function(data){
				
				var returnCode = data.returnCode;
				if(returnCode != null && returnCode == "USR-1I00"){
					$("#mobileByMsg").html(replaceStr1(data.mobile,3,4));
					$("#mobile").val(data.mobile);
					$("#section_01").hide();
					$("#section_02").show();
					getUserRequest("modify-TPPassword-verify");/* 此处subPath为页面内行为 */
				}else if(returnCode == "USR-1I01"){
					errorRemark("错误次数过多<br>3小时后重试");
					$("#reg_btn_01").removeClass("act").attr("onclick","");
					return;
				}else if(returnCode == "USR-1I02"){
					if(data.tPwdErrCount == 1){
						errorRemark("安全码有误");
						$("#tPassWord").val("");
					}else if(data.tPwdErrCount > 1 && data.tPwdErrCount < 6){
						errorRemark("安全码有误<br>还有"+(6-parseInt(data.tPwdErrCount))+"次机会");
					}
					$("#tPassWord").val("");
					$("#reg_btn_01").removeClass("act").attr("onclick","");
					return;
				}else if(returnCode == "USR-1I95"){
					errorRemark("请您完成实名鉴权");
					redirectUrl("/WeixinService/business/bank/bankAuth.shtml");
					return;
				}else{
					errorRemark("系统繁忙<br>稍后重试");
					$("#reg_btn_01").removeClass("act").attr("onclick","");
					return;
				}
			}
		});
	}
}
/* 获取验证码 ，检验手机号码和返回验证码 */
function getSmsByModifyTPsw(){
	var mobile=$("#mobile").val();
	if(isMobile(mobile)){
	    $.ajax({
	    	async:false,
	        url: "/WeixinService/business/getSmsByModifyTPsw.xhtml",
	        data: {"mobile": mobile},
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) { 
				errorRemark("网络繁忙，请稍后再试");
	        }, 
	        success : function (data){
	        	if(data.errorCode=='0000' && data.sessionID != "nullId"){
	        		/* 验证码发送成功 */
	        	  	$("#sessionID").val(data.sessionID);
	        		
	        	  	$("#AgetCode").attr('href',"javascript:void(0)");
	        	  	/* 读秒 */
	        	  	countDown();
	        	}else{
					errorRemark("网络繁忙，请稍后再试");
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
	if(sessionID == null || sessionID == ""){
		errorRemark("请获取手机验证码");
		$("#smsCode").val("");
		$("#reg_btn_02").removeClass("act").attr("onclick","");
		return;
	}
	if(smsCode == null || smsCode == ""){
		errorRemark("请输入手机验证码");
		$("#smsCode").val("");
		$("#reg_btn_02").removeClass("act").attr("onclick","");
		return;
	}
	if(isMobile(mobile)){
	 	$.ajax({
	    	async:false,
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
	        		$("#smsCode").val("");
					$("#reg_btn_02").removeClass("act").attr("onclick","");
	        	}else if(returnCode == "9998"){
	        		errorRemark("验证码已失效<br>请重新获取");
	        		$("#smsCode").val("");
					$("#reg_btn_02").removeClass("act").attr("onclick","");
	        	}else if(returnCode == "9997"){
	        		errorRemark("验证码已失效<br>请重新获取");
	        		$("#smsCode").val("");
					$("#reg_btn_02").removeClass("act").attr("onclick","");
	        	}else{
	        		errorRemark("手机验证码有误");
	        		$("#smsCode").val("");
					$("#reg_btn_02").removeClass("act").attr("onclick","");
	        	}
	        }       
	    });
	}
}
/* 修改安全码 */
function modifyTPassWord(){
	var newTPsw = $("#newTPsw").val();
	var confirmNewTPsw = $("#confirmNewTPsw").val();
	var tPassWord = $("#tPassWord").val();
	var mobile = $("#mobile").val();
	
	if(newTPsw == null || newTPsw.length < 8 || newTPsw.length > 16){
		errorRemark("请设置8-16位<br>数字+字母的安全码");
		return ;
	}else if(newTPsw == tPassWord){
		errorRemark("和旧安全码一致");
		return ;
	}else if(!Validater.isTradePassword(newTPsw)){
		errorRemark("请设置8-16位<br>数字+字母的安全码");
		return ;
	}else if(confirmNewTPsw == null || confirmNewTPsw != newTPsw){
		errorRemark("两次安全码不一致");
		return;
	}else{
		$.ajax({
	    	async:false,
	        url: "/WeixinService/business/modifyTPassWord.xhtml",
	        data: {
	        	"tPassWord": newTPsw,
	        	"oldTpassWord": tPassWord,
	        	"mobile":mobile
	        },
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) { 
				errorRemark("网络繁忙，请稍后再试");
	        }, 
	        success : function (data){
	        	var returnCode = data.returnCode;
	        	 
		        if(returnCode != null && returnCode == "USR-1I00"){
	 				errorRemark("安全码重置成功");
	 				/* TODO 跳转到账户中心页面 */
	 				redirectUrl("/WeixinService/business/user/queryAccount.shtml");
	        	}else if(returnCode == "USR-1I01"){
		 			errorRemark("错误次数过多<br>3小时后重试！");
	        	}else if(returnCode == "USR-1I02"){
		 			errorRemark("安全码有误");
	        	}else if(returnCode == "USR-1I95"){
					errorRemark("请您完成实名鉴权");
					redirectUrl("/WeixinService/business/bank/bankAuth.shtml");
	        	}else{
		 			errorRemark("修改失败");
	        	}
	        }       
	    });
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