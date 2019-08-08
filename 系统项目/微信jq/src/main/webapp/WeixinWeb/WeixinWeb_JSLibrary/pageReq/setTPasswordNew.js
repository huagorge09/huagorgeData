﻿var timer = 60;
$(document).ready(function(e){
	cmwaOnkeyup("txtPassword;txtConfirmPassword",checkBtn3Available);
	queryUserInfo();
	$(".header .top-a h2").html("设置安全码");
	document.title="设置安全码";
	getUserRequest("set-TPassword");/* 此处subPath为页面内行为 */
});

function checkBtn3Available(){
	var pass1 = $.trim($("#txtPassword").val());
	var pass2 = $.trim($("#txtConfirmPassword").val());
	
	if(pass1!="" && pass2!=""){
		$("#div3NextBtn").addClass("act");
		$("#div3NextBtn").attr("href","javascript:setPassword()");
	}else{
		$("#div3NextBtn").removeClass("act");
		$("#div3NextBtn").attr("href","javascript:void(0)");
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
	var tPassword = $("#txtPassword").val();
	if(!checkedTpsw()){
		return ;
	}else if(!checkedComfirmTpsw()){
		return;
	}else{
		$.ajax({
			async:true,
			url : "/WeixinService/business/setTpassword.xhtml",
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
					errorRemark("安全码设置成功");
					window.setTimeout('redirectUrl("/#/myAccount")',2000);
				}else if(returnCode == 'USR-1I01'){
					errorRemark("设置安全码失败：用户已锁定");
				}else if(returnCode == 'USR-1I95'){
					errorRemark("设置安全码失败：非交易用户,请先绑定银行卡");
				}else if(returnCode == '8000'){
					errorRemark(returnMsg);
			    	/* 跳转到登陆页面，判断微信和其他ie，分别跳转到不同的登陆页面 */
			    	window.setTimeout("goToLogin()",2000);
				}else if(returnCode == '9004'){
					errorRemark("您还未实名鉴权，请先实名鉴权");
					window.setTimeout('redirectUrl("/#/cardinfo")',3000);
				}else{
					errorRemark("网络繁忙，请稍后再试");
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