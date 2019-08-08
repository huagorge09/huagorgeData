var timer = 60;
var timer1 = 10;

$(document).ready(function(){
	getUserRequest("pc_modifyRegMobile_01");
	document.title = "修改手机号_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	checkIsLogin();
	$(".nav.fr ul li a").removeClass("current");
})
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
/*读秒*/ 
function countDown(){
	$("#AgetCode").attr('onclick',"");
	if(timer == 0){
		$('#AgetCode').val("重新获取验证码");
		$("#AgetCode").attr('onclick',"getVrfCode()");
		timer = 60;
	}else{
		timer--;
		$("#AgetCode").val(timer+"S重新获取");
		setTimeout('countDown()',1000);
	}
}
/*读秒*/ 
function countDown1(){
	if(timer1 == 0){
		window.location.href="/login/login.shtml";
	}else{
		timer1--;
		$("#time").html(timer1);
		setTimeout('countDown1()',1000);
	}
}
/* 验证安全码 */
function checkTPassword(){
	getUserRequest("pc_modifyRegMobile_02");
	var tPassword = $("#tPassword").val();
	if(tPassword == null || tPassword == ""){
		$("#error_tips_01").html("请输入安全码").show();
		return ;
	}else{
		$("#error_tips_01").hide();
		$.ajax({
			async : false,
			url : "/AppService/business/checkTPassword.xhtml",
			dataType : "json",
			type : "POST",
			data : {
				"tpassword":tPassword
			},
			cache : false,
			error : function(textStatus, errorThrown) {
				show_tips("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				var returnCode = "";
				if(data != null){
					returnCode = data.returnCode;
				}
				
				if(returnCode == "USR-1I00"){
					$("#div_01").hide();
					$("#div_02").show();
					$(".pay-order.schedule02").addClass("current");
				}else if(data.returnCode == "USR-1I01"){
					show_tips("密码错误次数过多，您的账户已锁定，请3小时后再次尝试");
					$("#tPassword").val("");
				}else if(data.returnCode == "USR-1I02"){
					if(data.tPwdErrCount == 1){
						show_tips("您输入的安全码有误，请重新输入");
					}else if(data.tPwdErrCount > 1 && data.tPwdErrCount < 6){
						show_tips("您还有"+(6-parseInt(data.tPwdErrCount,10))+"次机会");
					}else if(data.tPwdErrCount == 6){
						show_tips("密码错误次数过多，您的账户已锁定，请3小时后再次尝试");
					}
					$("#tPassword").val("");
				}else if(data.returnCode == "9999"){
					show_tips("网络繁忙，请稍后再试。");  
				}else{
					show_tips("您输入的安全码有误，请重新输入");
					$("#tPassword").val("");
				}
			}
		});
	}
}
/* 验证手机号码和返回短信验证码*/
function getVrfCode(){
	var mobile = $("#mobile").val();
	if(mobile == null || mobile == ""){
		$("#error_tips_02").html("请输入手机号码").show();
		return;
	}else if(!Validater.isMobilePhoneNumber(mobile)){
		$("#error_tips_02").html("手机号码有误").show();
		return;
	}else{
		$("#error_tips_02,#error_tips_03").hide();
		
		$.ajax({
			async : false,
			url : "/AppService/business/getVrfCode.xhtml",
			data : {
				"mobile" : mobile,
			},
			dataType : "json",
			cache : false,
			type : 'post',
			error : function(textStatus, errorThrown) {
				show_tips("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				var returnCode = data.returnCode;
				if(returnCode == "0000" || returnCode == "USR-A000"){
					$("#sessionID").val(data.sessionID);
					$("#point-clickafter").show();
					countDown();
				}else if(returnCode == "9999"){
		            show_tips("网络繁忙，请稍后再试");  
	        	}else if(returnCode == "9003"){
	        		show_tips("请先验证安全码");  
				}else if(returnCode == "USR-A017"){
	        		show_tips("手机号码已被使用");
	        	}else if(returnCode == "USR-5101"){
	        		show_tips("手机已冻结,超过规定时间内申请次数");
	        	}else if(returnCode == "USR-5102"){
	        		show_tips("手机已冻结，超过规定时间内验证错误次数");
	        	}else{
	        		show_tips(data.returnMsg);
	        	}
			}
		});
	}
}
/* 验证手机短信验证码 */
function checkVrfCode(){
	getUserRequest("pc_modifyRegMobile_03");
	var mobile = $("#mobile").val();
	var sessionID = $("#sessionID").val();
	var vrfCode = $("#vrfCode").val();
	vrfCode = $.trim(vrfCode);
	
	if(mobile == null || mobile == ""){
		$("#error_tips_02").html("请输入手机号码").show();
		$("#error_tips_03").hide();
		return;
	}else if(!Validater.isMobilePhoneNumber(mobile)){
		$("#error_tips_02").html("手机号码有误").show();
		$("#error_tips_03").hide();
		return;
	}else if(sessionID == null || sessionID == "" || sessionID == "null"){
		$("#vrfCode").val("");
		$("#error_tips_02").hide();
		$("#error_tips_03").html("请先获取短信验证码").show();
		return;
	}else if(vrfCode == null || vrfCode == ""){
		$("#error_tips_02").hide();
		$("#error_tips_03").html("请输入短信验证码").show();
		return;
	}else {
		$("#error_tips_02,#error_tips_03").hide();
		$.ajax({
			async : false,
			url : "/AppService/business/checkVrfCodeByModifyMobile.xhtml",
			data : {
				"mobile" : mobile,
				"vrfCode" : vrfCode,
				"sessionID" : sessionID
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
					if (timer != 60) {
						timer = 0;
					}
					if(data.hasCheckSetLPassword != null && data.hasCheckSetLPassword == "Y"){
						$("#div_02").hide();
		        		$("#div_03").show();
					}else {
						$("#div_02").hide();
		        		$("#div_04").show();
		        		$(".pay-order.schedule03").addClass("current");
		        		countDown1();
					}
				} else {
					var errorMsg = "";
					if (data.returnCode == "USR-5201") {
						errorMsg = "手机验证码有误";
					} else if (data.returnCode == "USR-5202" || data.returnCode == "USR-5203") {
						errorMsg = "验证码已失效，请重新获取";
						if (timer != 60) {
							timer = 0;
						}
					} else if (data.returnCode == "9002") {
						errorMsg = "当前手机号码不是获取验证码的手机号码";
						if (timer != 60) {
							timer = 0;
						}
					} else {
						errorMsg = data.returnMsg;
						if (timer != 60) {
							timer = 0;
						}
					}
					$("#vrfCode").val("");
					show_tips(errorMsg);
				}
				
			}
		});
	}
}
/* 修改手机号码并设置安全码 */
function modifyRegMobileAndSetPsw(){
	var mobile = $("#mobile").val();
	var password = $("#password").val();
	var rePassword = $("#rePassword").val();
	if(password == null || password == ""){
		$("#error_tips_04").html("请输入6-16位数字作为登录密码").show();
		$("#error_tips_01,#error_tips_02,#error_tips_03,#error_tips_05").addClass("none");
		return;
	}else if(isNaN(password) || !/^[\d]{6,16}$/.test(password)){
		$("#error_tips_04").html("密码格式有误").show();
		$("#error_tips_01,#error_tips_02,#error_tips_03,#error_tips_05").addClass("none");
		return;
	}else if(rePassword == null || rePassword == ""){
		$("#error_tips_04").html("请输入确认密码").show();
		$("#error_tips_01,#error_tips_02,#error_tips_03,#error_tips_04").addClass("none");
		return;
	}else if(rePassword != password){
		$("#error_tips_04").html("两次密码不一致").show();
		$("#error_tips_01,#error_tips_02,#error_tips_03,#error_tips_04").addClass("none");
		return;
	}
	$.ajax({
		async : false,
		url : "/AppService/business/modifyRegMobileAndSetPsw.xhtml",
		data : {
			"mobile" : mobile,
			"password" : password
		},
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			var returnCode = data.returnCode;
			if(returnCode != null && returnCode == "0000"){
				$("#div_03").hide();
				$("#div_04").show();
        		countDown1();
				$(".pay-order.schedule03").addClass("current");
			}else if(returnCode == "9999"){
				show_tips("网络繁忙，请稍后再试。");
			}else{
				show_tips(data.returnMsg);
			}
		}
	});
}