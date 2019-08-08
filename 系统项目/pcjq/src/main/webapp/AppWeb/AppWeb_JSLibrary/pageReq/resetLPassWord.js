
var i = 0;
var timer = 60;
var gapTime = "";
$(document).ready(function(){
	var mobileObj = $("#mobile"); 
	getUserRequest("pc_resetLPassWord_01");
	var type = getUrlParameter("type");
	if(type != null && type == "R"){
		document.title="找回登录密码_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
		mobileObj.removeAttr("readonly");
		mobileObj.bind("blur",function(evt){checkMobile(this.value);});
	}else{
		queryUserinfo();
	}
	$(".nav.fr ul li a").removeClass("current");
	getRandomCode();
})
/*下一张验证码 注册*/ 
function getRandomCode(){
	$("#rondomCodeImg").attr("src","/AppService/setUp/buildimageservlet.xhtml?count="+i);
	i++;
}

/*读秒*/ 
function countDown(){
	$("#AgetCode").attr('onclick',"");
	if(timer == 0){
		$('#AgetCode').val("重新获取验证码");
		$("#AgetCode").attr('onclick',"getMsgByPsw()");
		timer = 60;
	}else{
		timer--;
		$("#AgetCode").val(timer+"S重新获取");
		setTimeout('countDown()',1000);
	}
}

function countDown1(){
	$("#AgetCode").attr('onclick',"");
	var nowDateTime =new Date().getTime();
	var diffSecond = parseInt((gapTime - nowDateTime) /1000);
	
	if(diffSecond <= 0){
		$('#AgetCode').val("重新获取验证码");
		$("#AgetCode").attr('onclick',"getMsgByPsw()");
	}else{
		$("#AgetCode").val(diffSecond +"S重新获取");
		setTimeout('countDown1()',1000);
	}
	
}

/* 验证手机号码格式是否正确 */
function checkMobile(mobile){
	$("#error_tips_01,#error_tips_02").addClass("none");
	if(mobile == null || mobile == ""){
		$("#error_tips_02").addClass("none");
	}else if(!Validater.isMobilePhoneNumber(mobile)){
		$("#error_tips_02").removeClass("none").html("手机号码有误");
	}else{
		$("#error_tips_01").removeClass("none");
		$("#error_tips_02").addClass("none");
	}
}
/*验证手机号码是否注册和验证短信验证码*/
function checkVrfCode(){
	getUserRequest("pc_resetLPassWord_02");
	var mobile = $("#mobile").val();
	var vrfCode = $("#vrfCode").val();
	var type = getUrlParameter("type");
	if(mobile == null || mobile == ""){
		$("#error_tips_02").removeClass("none").html("请输入手机号码");
		return;
	}else if("R" == type && !Validater.isMobilePhoneNumber(mobile)){
		$("#error_tips_02").removeClass("none").html("手机号码有误");
		return;
	}
	else if(vrfCode == null || vrfCode == ""){
		$("#error_tips_03").removeClass("none").html("请输入验证码");
		return;
	}else{
		$.ajax({
			async : false,
			url : "/AppService/setUp/checkVrfCodeAndMobile.xhtml",
			data : {
				"mobile" : mobile,
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
				$("#error_tips_03").addClass("none");
				if(returnCode != null && returnCode == "9000"){
					show_tips("验证码已过期，请重新获取！");
				}else if(returnCode != null && returnCode == "9301"){
					show_tips("验证码有误，请重新填写！");
				}else if(returnCode != null && returnCode == "0000"){/*未注册用户*/
					show_tips2("该账号不存在，请立即注册！","立即注册","重新填写","/login/register.shtml","page_close_tips");
				}else if(returnCode != null && (returnCode == "USR-A000" || returnCode == "USR-A017")){
					$("#mobileText").html(mobile.substr(0,3)+" "+mobile.substr(3,4)+" "+mobile.substr(7,4));
					$("#cover_bg_01").show();
				}else if(returnCode != null && returnCode == "9999"){
					show_tips("网络繁忙，请稍后再试。");
				}else{
					show_tips("验证码已过期，请重新获取！");/*data.returnMsg*/
				}
				getRandomCode();
				$("#vrfCode").val("");
			}
		});
	}
}
/* 双按钮  重新填写点击回调函数 */
function page_close_tips(){
	$("#mobile,#vrfCode").val("");
	$("#error_tips_01,#hint_tips_btn2").hide();
}
/*获取短信验证码*/
function getMsgByPsw(){
	var mobile=$.trim($("#mobile").val());
	var bnsType = "4";
	var type = getUrlParameter("type");
	if("R" == type && !isMobile(mobile)){
		return ;
	}
    $.ajax({
    	async:false,
        url: "/AppService/setUp/getMsgByPsw.xhtml",
        data: {
        	"mobile": mobile,
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
        		if(!data.resetLPWMsgTime){//返回空值则 设置 当前时间 后 60秒
        			gapTime = new Date().getTime() + (60 * 1000);
        		}else{
        			gapTime = data.resetLPWMsgTime;
        		}
        		$("#sendMsg_tips .point-clickbefore").addClass("none");
        		$("#sendMsg_tips .point-clickafter").removeClass("none");
        		countDown1();
        	}else if(returnCode == "9999"){
	            show_tips("网络繁忙，请稍后再试。");  
        	}else if(returnCode == "9300"){
        		show_tips2("该账号不存在，请立即注册！","立即注册","重新填写","/login/register.shtml","page_close_tips");
        	}else if(returnCode == "9002"){
        		show_tips("图片验证码超时，请重新验证图片验证码");
        	}else{
	            show_tips(data.returnMsg);  
        	}
        }
    });
}
/* 验证短信验证码 */
function checkMobileCode(){
	var mobile = $("#mobile").val();
	var verifyCode = $("#verifyCode").val();
	var sessionID = $("#sessionID").val();
	
	if(sessionID == null || sessionID == ""){
		show_tips("请获取手机验证码");
		$("#verifyCode").val("");
		return;
	}else if(verifyCode == null || verifyCode == ""){
		show_tips("请输入手机验证码");
		return;
	}
	verifyCode = $.trim(verifyCode);
	
	$.ajax({
		async : false,
		url : "/AppService/setUp/checkVrfCode.xhtml",
		data : {
			"sessionID" : sessionID,
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
				if (timer != 60) {
					timer = 0;
				}
				close_tips("cover_bg_01");
        		$("#div_01").hide();
        		getUserRequest("pc_resetLPassWord_03");
        		$("#div_02").show();
        		$(".pay-order.schedule02").addClass("current");
			} else {
				var errorMsg = "";
				if (data.returnCode == "USR-5201") {
					errorMsg = "手机验证码有误";
				} else if (data.returnCode == "USR-5202") {
					errorMsg = "验证码已失效，请重新获取";
					if (timer != 60) {
						timer = 0;
					}
				} else if (data.returnCode == "USR-5203") {
					errorMsg = "验证码已失效，请重新获取";
					if (timer != 60) {
						timer = 0;
					}
				} else if (data.returnCode == "9002") {
					errorMsg = "当前手机号码不是获取验证码的手机号码";
					$("#cover_bg_01").hide();
					$("#verifyCode").val("");
				} else {
					errorMsg = "网络繁忙，请稍后再试";
					if (timer != 60) {
						timer = 0;
					}
				}
				show_tips(errorMsg);
			}
			/* 让获取验证码可以重新获取 */
			
		}
	});
}
/* 重置登录密码 */
function resetLPws(){
	getUserRequest("pc_resetLPassWord_04");
	var mobile = $("#mobile").val();
	var password = $("#password").val();
	var rePassword = $("#rePassword").val();
	
	if(password == null || password == ""){
		$("#psw_tips1").removeClass("none").html("请输入6-16位数字作为登录密码");
		$("#psw_tips2").addClass("none");
		return;
	}else if(isNaN(password) || !/^[\d]{6,16}$/.test(password)){
		$("#psw_tips1").removeClass("none").html("密码格式有误");
		$("#psw_tips2").addClass("none");
		return;
	}else if(rePassword == null || rePassword == ""){
		$("#psw_tips1").addClass("none");
		$("#psw_tips2").removeClass("none").html("请输入确认密码");
		return;
	}else if(rePassword != password){
		$("#psw_tips1").addClass("none");
		$("#psw_tips2").removeClass("none").html("两次密码不一致");
		return;
	}
	
	$.ajax({
		async : false,
		url : "/AppService/setUp/resetLPws.xhtml",
		data : {
			"txtMobile" : mobile,
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
				$("#div_02").hide();
				$("#div_03").show();
        		$(".pay-order.schedule03").addClass("current");
			}else if(returnCode == "9999"){
				show_tips("网络繁忙，请稍后再试。");
			}else{
				show_tips(data.returnMsg);
			}
		}
	});
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
			$("#mobile").val(data.mobile);
		}
	});
}