﻿﻿var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */
var gapTime = "";
/* 验证码读秒 */
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

function countDown1(){
	$("#AgetCode").attr('onclick',"");
	var nowDateTime =new Date().getTime();
	var diffSecond = parseInt((gapTime - nowDateTime) /1000);
	
	if(diffSecond <= 0){
		$("#AgetCode").attr('href',"javascript:getMobileVerifyCode()");
		$('#AgetCode').html("重新获取");
	}else{
		$('#AgetCode').html(diffSecond+"s重新获取");
		setTimeout('countDown1()',1000);
	}
	
}

$(document).ready(function(e) {
	cmwaOnkeyup("mobile;randomCode",resetCheckParam);
	cmwaOnkeyup("smsCode",smsCheckParam);
	cmwaOnkeyup("passWord;confirmPassWord",pswCheckParam);
	$(".header .top-a h2").html("重置登录密码");
	document.title="重置登录密码";
	getRandomCode();
	getUserRequest("reset-password-oldmobile");/* 此处subPath为页面内行为 */
});
/* 图片验证码	登录页面  */
function getRandomCode() {
	$("#rondomCodeImg").attr("src","/WeixinService/buildimageservlet.xhtml");
}
function resetCheckParam(){
	var mobile = $("#mobile").val();
	var randomCode = $.trim($("#randomCode").val());
	
	if(mobile == null || mobile == ""){
		$("#reg_btn_01").removeClass("act").attr("onclick","");
		return;
	}else if(randomCode == null || randomCode == ""){
		$("#reg_btn_01").removeClass("act").attr("onclick","");
		return;
	}else{
		$("#reg_btn_01").addClass("act").attr("onclick","checkRandomCode();");
	}
}
/* 重置密码页面发送短信验证码界面验证数据有效性 */
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
/* 重置密码页面设置登录密码界面验证数据有效性 */
function pswCheckParam(){
	var passWord = $("#passWord").val();
	var confirmPassWord = $("#confirmPassWord").val();
	
	if(passWord == null || passWord == ""){
		$("#reg_btn_03").removeClass("act").attr("onclick","");
		return;
	}else if(confirmPassWord == null || confirmPassWord == ""){
		$("#reg_btn_03").removeClass("act").attr("onclick","");
		return;
	}else {
		$("#reg_btn_03").addClass("act").attr("onclick","resetLPassWord();");
	}
}
/* 验证图片验证码 */
function checkRandomCode(){
	var mobile = $("#mobile").val();
	var randomCode = $("#randomCode").val();
	randomCode = randomCode.replace(re_halfSpace,"");
	randomCode = randomCode.replace(re_fullSpace,"");
	
	if(mobile == null || mobile == "" || !isMobile(mobile)){
		errorRemark("请输入正确的手机号");
		return;
	}else if(randomCode == null || randomCode.trim() == ""){
		errorRemark("请输入验证码");
		$("#reg_btn_01").removeClass("act").attr("onclick","");
		return;
	}else{
		$.ajax({
			async:false,
			url : "/WeixinService/verifyRandomCode.xhtml",
			data : {
				"inputCode":randomCode,
				"randomChannel":"resetRandom"
			},
			dataType : "json",
			type:"POST",
			cache : false,
			error : function(textStatus,errorThrown){
				errorRemark("网络繁忙，请稍后再试。");
			},
			success : function(data){
				if(data.returnCode=="success"){
	        		getRandomCode();
	        		verifyMobile();
	        	}else if(data.returnCode=="timeOut"){
					errorRemark("验证码已失效<br>请重新获取");
	        		getRandomCode();
	        		$("#randomCode").val("");
	        		$("#reg_btn_01").removeClass("act").attr("onclick","");
	        	}else if(data.returnCode=='failed'){
					errorRemark("验证码错误");
	        		getRandomCode();
	        		$("#randomCode").val("");
	        		$("#reg_btn_01").removeClass("act").attr("onclick","");
	        	}else{
					errorRemark("验证码错误");
	        		getRandomCode();
	        		$("#randomCode").val("");
	        		$("#reg_btn_01").removeClass("act").attr("onclick","");
	        	}
			}
		});
	}
}
/* 验证手机号码是否已使用 */
function verifyMobile(){
	var mobile = $("#mobile").val();
    $.ajax({
    	async:true,
        url: "/WeixinService/setUp/verifyMobile.xhtml",
        data: {"mobile": mobile},
        dataType: "json",
        cache: false,
        type: 'post',
        error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
            return ;
        }, 
        success : function (data){
        	if(data.returnCode=="0000"){
				errorRemark("该账号不存在<br>请立即注册</a>");
        		getRandomCode();
        		$("#randomCode").val("");
        		$("#reg_btn_01").removeClass("act").attr("onclick","");
        	}else if(data.returnCode=="USR-A017" || data.returnCode=="USR-A000"){
        		$("#mobileByMsg").html($("#mobile").val().substr(0,3)+"****"+$("#mobile").val().substr($("#mobile").val().length-4));
        		getRandomCode();
        		$("#randomCode").val("");
        		$("#section_01").hide();
        		$("#section_02").show();
        		getUserRequest("reset-password-verify");/* 此处subPath为页面内行为 */
        	}else{
				errorRemark("网络繁忙，请稍后再试");
        		getRandomCode();
        		$("#randomCode").val("");
        		$("#reg_btn_01").removeClass("act").attr("onclick","");
        	}
        }       
    }); 
}
/* 获取验证码 ，检验手机号码和返回验证码 */
function getMobileVerifyCode(){
	var mobile=$("#mobile").val();
	if(isMobile(mobile)){
	    $.ajax({
	    	async:true,
	        url: "/WeixinService/setUp/getVerifyCodeByResetLpsw.xhtml",
	        data: {"mobile": mobile},
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) { 
				errorRemark("网络繁忙，请稍后再试");
	        }, 
	        success : function (data){
	        	if(data.sessionTime=='null' || data.sessionTime=='timeOut'){/* 图片验证码session时间过时 */
	        		/* 跳回图片验证码页面  提示session时间过时 重新验证图片验证码 */
					errorRemark("页面已过期<br/>请重新操作");

	        		$("#randomCode").val("");
	        	    /* 重新获取验证码图片 */
	        	    getRandomCode();
	        		$("#section_01").show();
	        		$("#section_02").hide();
	        	}else{
		        	if(data.errorCode=='0000' && data.sessionID != "nullId"){
		        		/* 验证码发送成功 */
		        	  	$("#sessionID").val(data.sessionID);
		        		
		        	  	$("#AgetCode").attr('href',"javascript:void(0)");
		        	  	if(!data.resetLPWMsgTime){//返回空值则 设置 当前时间 后 60秒
		        			gapTime = new Date().getTime() + (60 * 1000);
		        		}else{
		        			gapTime = data.resetLPWMsgTime;
		        		}
		        	  	/* 读秒 */
		        	  	countDown1();
		        	}else if(!!data && '0000'!= data.errorCode){
		        		errorRemark(data.errorMsg);
		        	}else{
						errorRemark("网络繁忙，请稍后再试");
		        	}
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
	        		errorRemark("验证码错误");
	        		$("#smsCode").val("");
					$("#reg_btn_02").removeClass("act").attr("onclick","");
	        	}
	        }       
	    });
	}
}
function resetLPassWord(){
	getUserRequest("reset-password-newPsd");/* 此处subPath为页面内行为 */
	var mobile = $("#mobile").val();
	var passWord = $("#passWord").val();
	var confirmPassWord = $("#confirmPassWord").val();
	
	if(mobile == null || mobile == "" || !isMobile(mobile)){
		errorRemark("请输入正确的手机号");
		return;
	}else if(passWord == null || passWord.length < 6 || passWord.length > 16 || isNaN(passWord) || !Validater.isPureNumber(passWord)){
		errorRemark("请设置6-16位<br>数字登录密码");
		return;
	}else if(confirmPassWord == null || confirmPassWord != passWord){
		errorRemark("两次密码不一致");
		return;
	}else{
		$.ajax({
	    	async:true,
	        url: "/WeixinService/setUp/resetLPassWord.xhtml",
	        data: {
	        	"mobile": mobile,
	        	"passWord": passWord
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
					errorRemark("登录密码重置成功,请重新登陆~");
					window.setTimeout('redirectUrl("/WeixinService/business/query/fundList.shtml")',3000);
	        	}else if(returnCode == "USR-A024"){
					errorRemark("该手机号<br/>尚未注册");
	        	}else if(returnCode == "USR-A022"){
					errorRemark("修改失败");
	        	}else{
					errorRemark("修改失败");
	        	}
	        }       
	    });
	}
}