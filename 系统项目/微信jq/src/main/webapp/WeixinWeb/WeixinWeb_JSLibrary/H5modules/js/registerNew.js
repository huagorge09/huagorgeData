var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */
var myScroll,t_delay;
var personageScroll;
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


$(document).ready(function(e){
	cmwaOnkeyup("mobile;passWord;confirmPassWord;randomCode",regCheckParam);
	cmwaOnkeyup("smsCode",smsCheckParam);
	$(".header .top-a h2").html("用户注册");
	document.title="用户注册";
	myScroll = new IScroll('#wrapper', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true
	});
	
	personageScroll = new IScroll('#wrapper_personage', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true
	});
	
	getRandomCode();
	myScroll.refresh();
	personageScroll.refresh();
	getUserRequest("other-IERegister-input");/* 此处subPath为页面内行为 */
});

document.addEventListener('touchmove', function(e) {e.preventDefault();}, false);

/* 图片验证码	登录页面  */
function getRandomCode(){
	$("#loginPageRondomCodeImg").attr("src",apiHost + "/api/verify?jsessionid="+getCookie("JSESSIONID"));
}
/* 注册 验证手机号码、登录密码、图片验证码界面 验证输入框的有效性  */
function regCheckParam(){
	var mobile = $("#mobile").val();
	var passWord = $("#passWord").val();
	var confirmPassWord = $("#confirmPassWord").val();
	var randomCode = $("#randomCode").val();
	
	if(mobile == null || mobile == ""){
		$("#reg_btn_01").removeClass("act").attr("onclick","");
		return;
	}else if(passWord == null || passWord == ""){
		$("#reg_btn_01").removeClass("act").attr("onclick","");
		return;
	}else if(confirmPassWord == null || confirmPassWord == ""){
		$("#reg_btn_01").removeClass("act").attr("onclick","");
		return;
	}else if(randomCode == null || randomCode.trim() == ""){
		$("#reg_btn_01").removeClass("act").attr("onclick","");
		return;
	}else{
		$("#reg_btn_01").addClass("act").attr("onclick","checkRandomCode();");
	}
}
/* 注册 验证验证码界面 验证输入框有效性 */
function smsCheckParam(){
	var smsCode = $("#smsCode").val();
	var sessionID = $("#sessionID").val();
	
	if(smsCode == null || smsCode.trim() == ""){
		$("#reg_btn_02").removeClass("act").attr("onclick","");
		return;
	}else{
		$("#reg_btn_02").addClass("act").attr("onclick","register();");
	}
}
/* 验证数据有效性和验证图片验证码 */
function checkRandomCode(){
	var mobile = $("#mobile").val();
	var passWord = $("#passWord").val();
	var confirmPassWord = $("#confirmPassWord").val();
	var randomCode = $("#randomCode").val();
	randomCode = randomCode.replace(re_halfSpace,"");
	randomCode = randomCode.replace(re_fullSpace,"");
	
	if(mobile == null || mobile == "" || !isMobile(mobile)){
		errorRemark("请输入正确的<br>手机号");
		return;
	}else if(passWord == null || passWord.length < 6 || passWord.length > 16 || isNaN(passWord) || !Validater.isPureNumber(passWord)){
		errorRemark("请设置6-16位<br>数字密码");
		return;
	}else if(confirmPassWord == null){
		errorRemark("请输入登录密码");
		return;
	}else if(confirmPassWord != passWord){
		errorRemark("两次密码不一致");
		return;
	}else if(randomCode == null || randomCode.trim() == ""){
		errorRemark("请输入验证码");
		return;
	}else if(!$("#ischecked").is(":checked")){
		errorRemark("请阅读并同意用户协议");
		return;
	}else{
		$.ajax({
			async:false,
			url : apiHost + "/api/verifyRandomCode",
			data : {
				"inputCode":randomCode,
				"randomChannel":"regRandom",
				"jsessionid":getCookie("JSESSIONID")
			},
			dataType : "json",
			type:"POST",
			cache : false,
			error : function(textStatus,errorThrown){
				errorRemark("网络繁忙，请稍后再试。");
			},
			success : function(data){
				if(data.success){
					if(data.resp.returnCode=="success"){
						verifyMobile();
		        	}else if(data.resp.returnCode=="timeOut"){
						errorRemark("验证码已失效<br>请重新获取");
		        		getRandomCode();
		        		$("#randomCode").val("");
		        		$("#reg_btn_01").removeClass("act").attr("onclick","");
		        	}else if(data.resp.returnCode=='failed'){
						errorRemark("验证码有误");
		        		getRandomCode();
		        		$("#randomCode").val("");
		        		$("#reg_btn_01").removeClass("act").attr("onclick","");
		        	}else{
						errorRemark("验证码有误");
		        		getRandomCode();
		        		$("#randomCode").val("");
		        		$("#reg_btn_01").removeClass("act").attr("onclick","");
		        	}
				}
			}
		});
	}
}
/* 验证手机号码是否已使用 */
function verifyMobile(){
	var mobile = $("#mobile").val();
    $.ajax({
    	async:false,
        url: apiHost + "/api/verifyMobile",
        data: {"mobile": mobile},
        dataType: "json",
        cache: false,
        type: 'post',
        error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
            return ;
        }, 
        success : function (data){
        	if(data.success){
        		if(data.resp.returnCode=="0000"){
	        		$("#mobileByMsg").html($("#mobile").val().substr(0,3)+"****"+$("#mobile").val().substr($("#mobile").val().length-4));
	        		$("#section_01").hide();
	        		$("#section_02").show();
	        	}else if(data.resp.returnCode=="USR-A017" || data.resp.returnCode=="USR-A000"){
					errorRemark("该手机号码已完成注册<br>请<a href='javascript:goToLogin()' class='redColor'>立即登录</a>");
	        		getRandomCode();
	        		$("#randomCode").val("");
	        		$("#reg_btn_01").removeClass("act").attr("onclick","");
	        	}else{
					errorRemark("网络繁忙，请稍后再试");
	        		getRandomCode();
	        		$("#randomCode").val("");
	        		$("#reg_btn_01").removeClass("act").attr("onclick","");
	        	}
        	}
        }       
    }); 
}
/* 获取验证码 ，检验手机号码和返回验证码 */
function getMobileVerifyCode(){
	var mobile=$("#mobile").val();
	if(isMobile(mobile)){
	    $.ajax({
	    	async:false,
	        url: apiHost + "/api/getMobileVerifyCode",
	        data: {"mobile": mobile,"jsessionid":getCookie("JSESSIONID")},
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) { 
				errorRemark("网络繁忙，请稍后再试");
	        }, 
	        success : function (data){
	        	if(data.resp.sessionTime=='null' || data.resp.sessionTime=='timeOut'){/* 图片验证码session时间过时 */
	        		/* 跳回图片验证码页面  提示session时间过时 重新验证图片验证码 */
					errorRemark("图片验证码已失效<br/>请重新获取");

	        		$("#randomCode").val("");
	        	    /* 重新获取验证码图片 */
	        	    getRandomCode();
	        		$("#section_02").hide();
	        		$("#section_01").show();
	        	}else{
		        	if(data.resp.errorCode=='0000' && data.resp.sessionID != "nullId"){
		        		/* 验证码发送成功 */
		        	  	$("#sessionID").val(data.resp.sessionID);
		        		
		        	  	$("#AgetCode").attr('href',"javascript:void(0)");
		        	  	/* 读秒 */
		        	  	countDown();
		        	}else{
						errorRemark("网络繁忙，请稍后再试");
		        	}
	        	}
	        }       
	    });
	}
}
/* 注册 */
function register(){
	getUserRequest("other-IERegister-success");/* 此处subPath为页面内行为 */
	var mobile = $("#mobile").val();
	var passWord = $("#passWord").val();
	var confirmPassWord = $("#confirmPassWord").val();
	
	var sessionID = $("#sessionID").val();    
	var smsCode = $("#smsCode").val(); 
	
	if(mobile == null || mobile == "" || !isMobile(mobile)){
		errorRemark("请输入正确的<br>手机号");
		return;
	}else if(passWord == null || passWord.length < 6 || passWord.length > 16 || isNaN(passWord) || !Validater.isPureNumber(passWord)){
		errorRemark("请设置6-16位<br>数字密码");
		return;
	}else if(confirmPassWord == null){
		errorRemark("请输入登录密码");
		return;
	}else if(confirmPassWord != passWord){
		errorRemark("两次密码不一致");
		return;
	}else if(!$("#ischecked").is(":checked")){
		errorRemark("请点击同意用户协议");
		return;
	}else if(sessionID == null || sessionID == ""){
		errorRemark("请获取手机短信验证码");
		return;
	}else if(smsCode == null || smsCode.trim() == ""){
		errorRemark("请获取手机短信验证码");
		return;
	}else{
		
        $.ajax({
            async: false,
            url: apiHost + "/api/weixinRegister",
            data: {
           		"mobile": mobile,
                "passWord": passWord,
                "channel": "otherIE",
                "sessionID": sessionID,
                "smsCode": smsCode,
				"userId":localStorage.getItem("userId"),
				"jsessionid":getCookie("JSESSIONID")
            },
            type: 'post',
            dataType: "json",
            cache: false,
            error: function(textStatus, errorThrown) {
				errorRemark("网络繁忙，请稍后再试");
            },
            success: function(data){
                if (data.success) {
                    /* 注册成功或者通过三要素找到两个以上的未注册代销用户,通过三要素找到两个以上的已注册代销或已注册直销用户 */
                    var returnCode = data.resp.returnCode;
                    if (returnCode == "0000") {
                        localStorage.setItem("cmfUserId", data.resp.userInfoExtend.cmfuserid);
                        $("#section_02").hide();
                        var type = localStorage.getItem("type");
                        var target = localStorage.getItem("target");
                        if (type == "cfb273") {
                            addCookie("eventId", "event_cfb273_registerId");  //财富宝
                            operatingRecord(getCookie("pageSource"), "event_cfb273_registerId", "wx_cfb273_registerPageId", "", "", "", "")//财富宝
                        } else if (type == "7DaysPro") {
                            addCookie("eventId", "event_7DaysPro_registerId");  //七天产品
                            operatingRecord(getCookie("pageSource"), "event_7DaysPro_registerId", "wx_7DaysPro_registerPageId", "", "", "", "")//七天产品
                        } else if (type == "currentPro") {
                            addCookie("eventId", "event_currentPro_registerId");  //活期产品
                            operatingRecord(getCookie("pageSource"), "event_currentPro_registerId", "wx_currentPro_registerPageId", "", "", "", "")//活期产品
                        } else if (type == "cfb10") {
                            addCookie("eventId", "event_cfb10_registerId");  //财富宝
                            operatingRecord(getCookie("pageSource"), "event_cfb10_registerId", "wx_cfb10_registerPageId", "", "", "", "")//财富宝
                        } else if (type == "cfb126") {
                            addCookie("eventId", "event_cfb126_registerId");  //财富宝
                            operatingRecord(getCookie("pageSource"), "event_cfb126_registerId", "wx_cfb126_registerPageId", "", "", "", "")//财富宝
                        }else if (type == "annualBonus") {
                            addCookie("eventId", "event_annualBonus_registerId");  //财富宝
                            operatingRecord(getCookie("pageSource"), "event_annualBonus_registerId", "wx_annualBonus_registerPageId", "", "", "", "")//财富宝
                        } else if (type == "cfb28") {
                            addCookie("eventId", "event_cfb28_registerId");  //财富宝
                            operatingRecord(getCookie("pageSource"), "event_cfb28_registerId", "wx_cfb28_registerPageId", "", "", "", "")//财富宝
                        } else if (type == "cfbAll") {
                            addCookie("eventId", "event_cfbAll_registerId");  //财富宝
                            operatingRecord(getCookie("pageSource"), "event_cfbAll_registerId", "wx_cfbAll_registerPageId", "", "", "", "")//财富宝
                        }else if (type == "cfb203") {
                            addCookie("eventId", "event_cfb203_registerId");  //财富宝
                            operatingRecord(getCookie("pageSource"), "event_cfb203_registerId", "wx_cfb203_registerPageId", "", "", "", "")//财富宝
                        }else if (type == "cfbQun") {
                            addCookie("eventId", "event_cfbQun_registerId");  //财富宝
                            operatingRecord(getCookie("pageSource"), "event_cfbQun_registerId", "wx_cfbQun_registerPageId", "", "", "", "")//财富宝
                        }else if (type == "cfb207") {
                            addCookie("eventId", "event_cfb207_registerId");  //财富宝
                            operatingRecord(getCookie("pageSource"), "event_cfb207_registerId", "wx_cfb207_registerPageId", "", "", "", "")//财富宝
                        }else if(type == "cfb209"){
							addCookie("eventId", "event_cfb209_registerId");  //财富宝
                            operatingRecord(getCookie("pageSource"), "event_cfb209_registerId", "wx_cfb209_registerPageId", "", "", "", "")//财富宝
						}else if(type == "fatherDay"){
							addCookie("eventId", "event_fatherDay_registerId");  //父亲节
                            operatingRecord(getCookie("pageSource"), "event_fatherDay_registerId", "wx_fatherDay_registerPageId", "", "", "", "")//父亲节
						}else if(type == "chenli"){
							addCookie("eventId", "event_chenli_registerId");  //父亲节
                            operatingRecord(getCookie("pageSource"), "event_chenli_registerId", "wx_chenli_registerPageId", "", "", "", "")//父亲节
						}else if(type == "chenrui"){
							addCookie("eventId", "event_chenrui_registerId");  //父亲节
                            operatingRecord(getCookie("pageSource"), "event_chenrui_registerId", "wx_chenrui_registerPageId", "", "", "", "")//父亲节
						}else if (type == "cfbAll2") {
                            addCookie("eventId", "event_cfbAll2_registerId");  //财富宝
                            operatingRecord(getCookie("pageSource"), "event_cfbAll2_registerId", "wx_cfbAll2_registerPageId", "", "", "", "")//财富宝
                        }
						
                        //addCookie("eventId","event_wxYh91No10_registerId");  //远航5号
                        //operatingRecord(getCookie("pageSource"),"event_hlNo5_registerId","wx_yh91No10_registerPageId","","","","")//远航5号
                        //addCookie("eventId","event_hlNo5_registerId");  //和利5号
                        //operatingRecord(getCookie("pageSource"),"event_hlNo5_registerId","wx_hlNo5_registerPageId","","","","")//和利5号
                        //addCookie("eventId","event_heli6_registerId");  //财富宝
                        //operatingRecord(getCookie("pageSource"),"event_heli6_registerId","wx_heli6_registerPageId","","","","")//财富宝
                        if (target == "proList") {
                            $("#section_04").show();
                        } else {
                            t_delay = setInterval(function () {
                                redirectUrl('/WeixinService/H5modules/riskAssessment/fundModRiskHouseAddr.html');
                            }, 5000);
                            $("#section_03").show();
                        }
                    } else if (returnCode == "9998") {/* 未获取到发送验证码时，session中保存的手机号码 */
                        errorRemark("验证码已失效<br>请重新获取");
                        $("#reg_btn_02").removeClass("act").attr("onclick", "");
                        $("#smsCode").val("");
                    } else if (returnCode == "9997") {/* 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致 */
                        errorRemark("验证码已失效<br>请重新获取");
                        $("#reg_btn_02").removeClass("act").attr("onclick", "");
                        $("#smsCode").val("");
                    } else if (returnCode == "9996") {/* 手机短信验证码验证失败 */
                        errorRemark("手机验证码<br>有误");
                        $("#reg_btn_02").removeClass("act").attr("onclick", "");
                        $("#smsCode").val("");
                    } else if (returnCode == "USR-A015") {/* 待绑定的手机号码已被使用 */
                        errorRemark("手机号码已被注册<br>请登录");
                        $("#reg_btn_02").removeClass("act").attr("onclick", "");
                    } else {
                        errorRemark("网络繁忙，请稍后再试");
                        $("#reg_btn_02").removeClass("act").attr("onclick", "");
                    }
                } else {
                    errorRemark("网络繁忙，请稍后再试");
                }
               
            }
        });
        
	}

}
function showXy(_id){
	getUserRequest(_id);/* 此处subPath为页面内行为 */
	$("#section,#xy_01,#xy_02,#xy_03").hide();
	$("#"+_id).show();
	myScroll.refresh();
	personageScroll.refresh();
}
function readed(){
	$("#section,#xy_01,#xy_02,#xy_03").hide();
	$("#section").show();
	myScroll.refresh();
	personageScroll.refresh();
}


function toRiskLevel(){
	clearInterval(t_delay);
	redirectUrl('/WeixinService/H5modules/riskAssessment/fundModRiskHouseAddr.html?eventId=event_wx_registeredId&pageSource=wx_userRegisteredId');
}

/*
 浏览器端监听手机屏幕旋转
function orientationChange() {
	switch(window.orientation) {
		case 0: 
			alert("肖像模式 0,screen-width: " + screen.width + "; screen-height:" + screen.height);
			break;
		case -90: 
			alert("左旋 -90,screen-width: " + screen.width + "; screen-height:" + screen.height);
			break;
		case 90:   
			alert("右旋 90,screen-width: " + screen.width + "; screen-height:" + screen.height);
			break;
		case 180:   
			alert("风景模式 180,screen-width: " + screen.width + "; screen-height:" + screen.height);
			break;
	}
}

添加事件监听
addEventListener('load', function(){
	orientationChange();
	window.onorientationchange = orientationChange;
	
});*/