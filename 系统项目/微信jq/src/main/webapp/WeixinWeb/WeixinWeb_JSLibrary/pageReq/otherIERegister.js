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
    // mgm开关
    mgmInit()  
});

document.addEventListener('touchmove', function(e) {e.preventDefault();}, false);

/* 图片验证码	登录页面  */
function getRandomCode(){
    $("#loginPageRondomCodeImg").attr("src","/WeixinService/buildimageservlet.xhtml");
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
    var inventer_mobile= $("#inventer_mobile").val();  //邀请人手机号码
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
    }else if(inventer_mobile!==""){
    	if(!isMobile(inventer_mobile)){
    		errorRemark("邀请手机号码格式有误");
	        return;
    	}
    }
    $.ajax({
        async:false,
        url : "/WeixinService/verifyRandomCode.xhtml",
        data : {
            "inputCode":randomCode,
            "randomChannel":"regRandom"
        },
        dataType : "json",
        type:"POST",
        cache : false,
        error : function(textStatus,errorThrown){
            errorRemark("网络繁忙，请稍后再试。");
        },
        success : function(data){
            if(data.returnCode=="success"){
                verifyMobile();
            }else if(data.returnCode=="timeOut"){
                errorRemark("验证码已失效<br>请重新获取");
                getRandomCode();
                $("#randomCode").val("");
                $("#reg_btn_01").removeClass("act").attr("onclick","");
            }else if(data.returnCode=='failed'){
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
    });
    
}
/* 验证手机号码是否已使用 */
function verifyMobile(){
    var mobile = $("#mobile").val();
    $.ajax({
        async:false,
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
                $("#mobileByMsg").html($("#mobile").val().substr(0,3)+"****"+$("#mobile").val().substr($("#mobile").val().length-4));
                $("#section_01").hide();
                $("#section_02").show();


            }else if(data.returnCode=="USR-A017" || data.returnCode=="USR-A000"){
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
    });
}
/* 获取验证码 ，检验手机号码和返回验证码 */
function getMobileVerifyCode(){
    var mobile=$("#mobile").val();
    if(isMobile(mobile)){
        $.ajax({
            async:false,
            url: "/WeixinService/setUp/getMobileVerifyCode.xhtml",
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
                    errorRemark("图片验证码已失效<br/>请重新获取");

                    $("#randomCode").val("");
                    /* 重新获取验证码图片 */
                    getRandomCode();
                    $("#section_02").hide();
                    $("#section_01").show();
                }else{
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
        errorRemark("请阅读并同意用户协议");
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
            url: "/WeixinService/setUp/webRegister.xhtml",
            data: {
                "mobile": mobile,
                "passWord": passWord,
                "channel": "otherIE",
                "sessionID": sessionID,
                "smsCode": smsCode
            },
            type: 'post',
            dataType: "json",
            cache: false,
            error: function(textStatus, errorThrown) {
                errorRemark("网络繁忙，请稍后再试");
            },
            success: function(data){
                /* 注册成功或者通过三要素找到两个以上的未注册代销用户,通过三要素找到两个以上的已注册代销或已注册直销用户 */
                var returnCode = data.returnCode;
                if(returnCode == "0000"){
                    pageEventData("04","00");
                    $("#section_02").hide();
                    $("#section_03").show();

                    var mgmOpen=enableIntegral();
					if(mgmOpen=="1"){  //开启合格投资者验证
				       bindingReferrer()
                    } 
                    
                    t_delay = setInterval (function(){
                        redirectUrl('/WeixinService/business/query/fundList.shtml?eventId=event_wx_registeredId&pageSource=wx_userRegisteredId');
                    }, 5000);

                }else if(returnCode == "9998"){/* 未获取到发送验证码时，session中保存的手机号码 */
                    errorRemark("验证码已失效<br>请重新获取");
                    $("#reg_btn_02").removeClass("act").attr("onclick","");
                    $("#smsCode").val("");
                }else if(returnCode == "9997"){/* 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致 */
                    errorRemark("验证码已失效<br>请重新获取");
                    $("#reg_btn_02").removeClass("act").attr("onclick","");
                    $("#smsCode").val("");
                }else if(returnCode == "9996"){/* 手机短信验证码验证失败 */
                    errorRemark("手机验证码<br>有误");
                    $("#reg_btn_02").removeClass("act").attr("onclick","");
                    $("#smsCode").val("");
                }else if(returnCode == "USR-A015"){/* 待绑定的手机号码已被使用 */
                    errorRemark("手机号码已被注册<br>请登录");
                    $("#reg_btn_02").removeClass("act").attr("onclick","");
                }else{
                    errorRemark("网络繁忙，请稍后再试");
                    $("#reg_btn_02").removeClass("act").attr("onclick","");
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
    redirectUrl('/WeixinService/business/query/fundList.shtml?eventId=event_wx_registeredId&pageSource=wx_userRegisteredId');
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

//mgm初始化页面
function mgmInit(){
	var param=enableIntegral();
	if(param=="1"){  //开启合格投资者验证
		var invitFlag=getUrlParams();
		if(!!invitFlag.invitation){  //该用户是通过邀请链接
			localStorage.setItem("invitation",invitFlag.invitation)
			$(".invitation").hide()
		}else{                        //该用户不是通过邀请链接
			$(".invitation").show();
		}
	}else{           //关闭合格投资者
		$(".invitation").hide()
	}
	
}

//mgm总开关
function enableIntegral(){
    var enableIntegral = "";
    $.ajax({
        url: "/WeixinService/integral/enableIntegral.xhtml",
        dataType: "json",
        type: "get",
        async:false,
        success: function(n) {
            enableIntegral=n.data;
        }
    });
    return enableIntegral;
}
/**
 * 绑定邀请人手机号码
 */
function bindingReferrer(){
	 var invitation=localStorage.getItem("invitation")  //邀请码
	 if(invitation){
	 	 var url="/WeixinService/business/integral/bindingReferrer.xhtml"
	 	 var data={
		 	"invitationCode":invitation
		 }
	 }else{
	 	 var phoneNumber=$.trim($("#inventer_mobile").val()); //邀请人手机号码
	 	 var url="/WeixinService/business/integral/bindingReferrerByPhoneNumber.xhtml";
	 	 var data={
		 	 "phoneNumber":phoneNumber
		 }
	 }
	  $.ajax({
        url:url,
        data: data,
        dataType: "json",
        type: "POST",
        error: function() {
            errorRemark("网络繁忙，请稍后再试。");
        },
        success: function(n) {
           localStorage.removeItem("invitation")  //邀请码
           if(n.data=="0"){
           	    errorRemark("输入的邀请人手机号码有误，可在下单页面再次输入");
           }
        }
    });
}
