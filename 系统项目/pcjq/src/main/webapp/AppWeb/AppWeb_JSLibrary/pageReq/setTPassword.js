var timer = 60;
var timer1 = 15;
var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */
$(document).ready(function(){
	$("#logo em").html("设置安全码");
	document.title="设置安全码_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	checkIsLogin();
	$(".nav.fr ul li a").removeClass("current");
	queryUserinfo();
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
				/*未鉴权跳转到鉴权页面*/
				if(data.userType!='30'){
					goToURL("/AppService/business/bank/realName.shtml");
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
		$("#AgetCode").attr('onclick',"sendMsg()");
		timer = 60;
	}else{
		timer--;
		$("#AgetCode").val(timer+"S重新获取");
		setTimeout('countDown()',1000);
	}
}
function queryUserinfo() {
	$.ajax({
		async : false,
		url : "/AppService/business/queryUserinfo.xhtml",
		dataType : "json",
		type : "POST",
		data : {},
		cache : false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			$("#mobile").html(data.mobile);
			$("#successName").html(data.userName);
		}
	});
}
/*获取短信验证码*/
function sendMsg(){
	var bnsType = "4";
	$("#error_tips_01").hide();
    $.ajax({
    	async:false,
        url: "/AppService/setUp/sendMsg.xhtml",
        data: {
	        "bnsType":bnsType
        },
        type: 'post',
        dataType: "json",
        cache: false,
        error : function(textStatus, errorThrown) {
            show_tips("网络繁忙，请稍后再试。");  
        }, 
        success : function (data){
        	var returnCode = data.returnCode;
        	if(returnCode =! null && returnCode == "0000"){
        		$("#sessionID").val(data.sessionID);
        		$("#sendMsg_tips").show();
        		countDown();
        	}else if(returnCode == "9999"){
	            show_tips("网络繁忙，请稍后再试。");  
        	}else if(returnCode == "9000"){
        		show_tips(data.returnMsg);
        		setTimeout('window.location.href = "/login/login.shtml";',1000)
        	}else{
	            show_tips(data.returnMsg);  
        	}
        }
    });
}
/*验证短信验证码*/
function checkVrfCode(){
	var mobile = $("#mobile").val();
	var vrfCode = $.trim($("#vrfCode").val());
	var sessionID = $("#sessionID").val();
	if(sessionID == null || sessionID == ""){
		$("#error_tips_01").removeClass("none").html("请获取手机验证码");
		$("#vrfCode").val("");
		return;
	}else if(vrfCode == null || vrfCode == ""){
		$("#error_tips_01").removeClass("none").html("请输入手机验证码");
		return;
	}else{
		$.ajax({
			async : false,
			url : "/AppService/setUp/checkMobileVrfCode.xhtml",
			data : {
				"sessionID" : sessionID,
				"rvrfcode" : vrfCode
			},
			dataType : "json",
			cache : false,
			type : 'post',
			error : function(textStatus, errorThrown) {
				show_tips("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				
				var returnCode = data.returnCode;
				$("#error_tips_01").addClass("none")
				if(returnCode != null && returnCode == "0000"){
	        		$("#div_01").hide();
	        		$("#div_02").show();
	        		$(".pay-order.schedule02").addClass("current");
				}else if(returnCode != null && (returnCode == "9000" || returnCode == "9301")){
					show_tips("验证码已过期，请重新获取！");
					if (timer != 60) {
						timer = 0;
					}
				}else if(returnCode != null && returnCode == "9999"){
					show_tips("网络繁忙，请稍后再试。");
				}else if(returnCode != null && returnCode == "USR-5201"){
					show_tips("短信验证码有误");
				}else if(returnCode != null && returnCode == "USR-5202"){
					show_tips("验证码已过期，请重新获取！");
				}else{
					show_tips(data.returnMsg);/*data.returnMsg*/
					if (timer != 60) {
						timer = 0;
					}
				}
				$("#vrfCode").val("");
			}
		});
	}
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
					$("#div_02").hide();
					$("#div_03").show();
					$(".schedule03").addClass("current");
				} else if (returnCode == 'USR-1I01') {
					show_tips("设置安全码失败：用户已锁定");
				} else if (returnCode == 'USR-1I95') {
					show_tips("设置安全码失败：非交易用户,请先绑定银行卡");
				} else if (returnCode == '8000') {
					show_tips(returnMsg);
					/* 跳转到登录页面*/
					window.location.href="/login/register.shtml";
				}  else if (returnCode == '9999') {
					show_tips("设置安全码失败：用户已锁定");
				} else{
					show_tips(data.returnMsg);
				}
			}
		});
	}
}