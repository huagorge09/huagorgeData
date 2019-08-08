var timer = 60;
var timer1 = 15;
var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */
$(document).ready(function(){
	getUserRequest("pc_modifyTPassword_01");
	checkIsLogin();
	$(".nav.fr ul li a").removeClass("current");
	document.title = "修改安全码_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
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
		$("#AgetCode").attr('onclick',"sendMsg()");
		timer = 60;
	}else{
		timer--;
		$("#AgetCode").val(timer+"S重新获取");
		setTimeout('countDown()',1000);
	}
}
function countDown1(){
	if(timer1 == 0){
		goToURL("/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO");
	}else{
		timer1--;
		$("#timer1").html(timer1);
		setTimeout('countDown1()',1000);
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
		}
	});
}
/*获取短信验证码*/
function sendMsg(){
	var bnsType = "4";
	$("#error_tips_01").addClass("none");
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
	getUserRequest("pc_modifyTPassword_02");
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
				$("#error_tips_01").addClass("none");
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
function checkedTpsw(tPassword) {
	var tpswLwngth = tPassword.length;
	if(tPassword == null || tPassword == ""){
		$("#error_tips_03").removeClass("none").html("请设置新的安全码！");
		return false;
	}else if(tpswLwngth < 8 || tpswLwngth > 16){
		$("#error_tips_03").removeClass("none").html("请设置8-16位数字+字母的安全码");
		return false;
	}else if(!Validater.isTradePassword(tPassword)) {
		$("#error_tips_03").removeClass("none").html("请设置8-16位数字+字母的安全码");
		return false;
	}else{
		return true;
	}
}
/* 修改安全码 */
function modifyTPassword() {
	getUserRequest("pc_modifyTPassword_03");
	var tPassword = $("#tPassword").val();
	var newTPassword = $("#newTPassword").val();
	var reNewTPassword = $("#reNewTPassword").val();

	if (tPassword == null || tPassword == "") {
		$("#error_tips_02").removeClass("none");
		$("#error_tips_03").addClass("none");
		$("#error_tips_04").addClass("none");
		return;
	} else if (!checkedTpsw(newTPassword)) {
		$("#error_tips_02").addClass("none");
		$("#error_tips_04").addClass("none");
		return;
	} else if (reNewTPassword == null || reNewTPassword == "") {
		$("#error_tips_02").addClass("none");
		$("#error_tips_03").addClass("none");
		$("#error_tips_04").removeClass("none").html("确认安全码不能为空");
		return;
	}else if(reNewTPassword != newTPassword){
		$("#error_tips_02").addClass("none");
		$("#error_tips_03").addClass("none");
		$("#error_tips_04").removeClass("none").html("两次密码不一致");
		return;
	}else if(tPassword == newTPassword){
		show_tips("新安全码和原安全码不能一致");
		return;
	}
	$.ajax({
		async : false,
		url : "/AppService/business/modifyTPassword.xhtml",
		data : {
			"tPassword" : tPassword,
			"newTPassword" : newTPassword
		},
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {

			var returnCode = data.returnCode;
			$("#error_tips_02,#error_tips_03,#error_tips_04").addClass("none");
			if (returnCode == "USR-1I00") {
				$(".pay-order.schedule03").addClass("current");
				$("#div_02").hide();
				$("#div_03").show();
				countDown1();
			} else if (returnCode == "USR-1I02") {
				show_tips("当前安全码错误！");
				return;
			} else if (returnCode == "USR-1I01") {
				show_tips("用户已锁定！");/* 更改提示文字 */
				return;
			} else if (returnCode == "USR-1I95") {
				show_tips("非交易用户！");
				return;
			} else if (returnCode == "9333") {
				show_tips("还未设置安全码，不能修改！");
				return;
			} else {
				show_tips("修改失败,请稍后再试！");
				return;
			}
		}
	});
}