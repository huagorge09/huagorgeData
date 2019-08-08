var i = 0;
var timer = 60;
var timer1 = 60;
var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */
var t_delay;
var isOpenMgm; //是否开启mgm开关

$(document).ready(function(){
	getUserRequest("pc_register_01");
	$(".nav.fr ul li a").removeClass("current");
	getRandomCode();
	cmwaOnkeyup("bankNumberTemp", checkBankNumberLength);
    integralOpenOrClose()  //mgm开关
})
/* 下一张验证码 注册*/
function getRandomCode(){
	$("#rondomCodeImg").attr("src","/AppService/setUp/buildimageservlet.xhtml?count="+i);
	i++;
}

/* 读秒*/
function countDown(){
	if(timer == 0){
		$('#AgetCode').val("重新获取验证码");
		$("#AgetCode").attr('onclick',"getMobileVerifyCode()");
		timer = 60;
	}else{
		timer--;
		$("#AgetCode").val(timer+"s重新获取");
		setTimeout('countDown()',1000);
	}
}
/* 银行鉴权 读秒*/
function countDown1(){
	if(timer1 == 0){
		$('#getVerifyCodeByBankCard').val("重新获取验证码");
		$("#getVerifyCodeByBankCard").attr('onclick',"getMobileVerifyCode1()");
		timer1 = 60;
	}else{
		timer1--;
		$("#getVerifyCodeByBankCard").val(timer1+"s重新获取");
		setTimeout('countDown1()',1000);
	}
}

/* 获取验证码 ，检验手机号码和返回验证码 */
function getMobileVerifyCode(){
	var mobile=$.trim($("#mobile").val());
	var bnsType = "0";
	if(checkMobile(mobile)){
		var params = {
	        "mobile": mobile,
	        "bnsType":bnsType,
	    };
	    var actionUrl = "/AppService/setUp/getMobileVerifyCode.xhtml";
	    $.ajax({
	    	async:false,
	        url: actionUrl,
	        data: params,
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) {  
	            show_tips("网络繁忙，请稍后再试。");  
	        }, 
	        success : function (data){
	        	if(data.sessionID != null && data.sessionID != "" && data.sessionID!="nullId" && data.returnCode == "0000"){
	        		/* 验证码发送成功 */
	        	  	$("#sessionID").val(data.sessionID);
	        	  	$("#AgetCode").val('重新获取验证码').attr('onclick',"javascript:");
	        	  	$("#point-clickafter").show();
	        	  	/* 读秒 */
	        	  	countDown();
	        	}else if(data.returnCode=="USR-A017" || data.returnCode=="USR-A000"){
 					show_tips("手机号码已被注册，进入登录页面进行绑定或者重新输入手机号码继续注册");
	        	}else{
 					show_tips(data.returnMsg);
	        	}
	        }       
	    });
	}
}
/* 验证手机号码 */
function mobileWarn(){
	var bool = false;
	var mobile = $("#mobile").val();
	if(isEmpty(mobile)){
		show_tips("手机号码不能为空");
		bool = false;
	}else if(!checkMobile(mobile)){
		bool = false;
	}else{
		/* 验证手机号码是否已经注册 */
		 $.ajax({
	    	async:false,
	        url: "/AppService/setUp/getMobile.xhtml",
	        data: {
	        	"mobile": mobile
	        },
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) {  
	            show_tips("网络繁忙，请稍后再试。");
	            bool = false;
	        }, 
	        success : function (data){
	        	if(data.returnCode=="USR-A017" || data.returnCode=="USR-A000"){
	        		$("#mobile").next().show();/* 改变样式 */
	    			$("#mobile").next().html("手机号码已被注册");/* 更改提示文字 */
	    			bool = false;
	        	}else{
	        		$("#mobile").next().hide();/* 改变样式 */
	        		bool = true;
	        	}
	        }
	    });
	}
	return bool;
}
/* 密码验证 */
function setPasswordHtml(){
	var flag = false;
	var txtPassword = $("#txtPassword").val();
	var len = parseInt($("#txtPassword").val().length,10);
	if(isEmpty(txtPassword)){
		show_tips("请输入登录密码"); 
		flag = false;
	}else{
		if (isEmpty(txtPassword)|| !/^[\d]{6,16}$/.test($("#txtPassword").val())) {
			$("#txtPassword").next().show();/* 改变样式 */
			flag = false;
		}else{
			$("#txtPassword").next().hide();
			flag = true;
		}
	}
	return flag;
}
/* 确认密码验证 */
function setCfPasswordHtml(){
	var txtPassword = $("#txtPassword").val();
	var txtConfirmPassword = $("#txtConfirmPassword").val();
	var flag = false;
	if(isEmpty(txtConfirmPassword)){
		show_tips("请输入确认登录密码"); 
		flag = false;
	}else{
		if (txtConfirmPassword != txtPassword) {
			$("#txtConfirmPassword").next().show();
			flag = false;
		}else if(txtConfirmPassword == txtPassword){
			$("#txtConfirmPassword").next().hide();
			flag = true;
		}
	}
	return flag;
}
/* 图片验证码校验 */
function checkedRandomCode(){
	var flag = false;
	if(!checkMobileAndImgRandomCode()){
		flag = false;
		return;
	}else{
		var inputCode = $("#validateNumber").val();
	    $.ajax({
	    	async:false,
	        url: "/AppService/setUp/verifyRandomCode.xhtml",
	        data: {
	        	inputCode:$("#validateNumber").val(),
	        	mobile:$("#mobile").val()
	        },
	        dataType: "json",
	        cache: false,
	        type: 'post',
	        error : function(textStatus, errorThrown) { 
	            show_tips("网络繁忙，请稍后再试。图片验证码校验失败！");  
	            result = false;
	        }, 
	        success : function (data){
	        	if(data.returnCode=="0000"){
 					flag = true;
	        	}else if(data.returnCode=="0011"){
 					show_tips("验证码过期，请重新输入！");  
 					getRandomCode();
 					$("#validateNumber").val("");
 					flag = false;
	        	}else{
	        		show_tips("验证码有误");  
 					getRandomCode();
 					$("#validateNumber").val("");
 					flag = false;
	        	}
	        }       
	    }); 
	}
	return flag;
}
/* 图片验证码进行基本验证 */
function checkMobileAndImgRandomCode(){
	var randomCode= $.trim($("#validateNumber").val());
	if (isEmpty(randomCode)) {
		show_tips("请输入验证码");
		return false;
	}
	return true;
	
}



// 验证邀请人手机号
function inviterNumber(){
	if (isOpenMgm=="1"){
        var bool;
        var phoneNumber = $("#inviterPhone").val();
        var number = phoneNumber.replace(/\s*/g,"");
        if(number != ''){
            if(!checkMobileTwo(number)){
                bool = false;
            }else{
                bool = true;
            }
        }else{
            bool = true;
        }
        return bool;
	}
}
function checkMobileTwo(obj) {
	if (isOpenMgm=="1"){
        if(!Validater.isMobilePhoneNumber(obj)){
            show_tips("邀请人手机号码有误");
            return false;
        }
        return true;
	}
}
/*邀请人手机号接口*/
function inviter(){
	var phoneNumber = $("#inviterPhone").val();
	var number = phoneNumber.replace(/\s*/g,"");
	var isTrueInventPhone= inviterNumber();
	console.log(isTrueInventPhone,"是否是正确的手机号")
	if(isTrueInventPhone==true && isOpenMgm=="1"){
        $.ajax({
            async: false,
            url: "/AppService/business/integral/bindingReferrerByPhoneNumber.xhtml",
            data: {
                'phoneNumber': number
            },
            type: 'post',
            dataType: "json",
            cache: false,
            error: function(textStatus, errorThrown) {
                show_tips("网络繁忙，请稍后再试");
            },
            success: function(data){
                if(data == '0'){
                    show_tips("邀请手机失败，还可以在支付页面邀请！");
                }

            }
        });
	}
}


/* 进入手机验证码页面 */
function intoVerifyMobileCode(){
	getUserRequest("pc_register_02");
	$(".error-point").hide();
	if(!mobileWarn()){
		return;
	}else if(!setPasswordHtml()){
		return;
	}else if(!setCfPasswordHtml()){
		return;
	}else if(!checkedRandomCode()) {
        return;
    }else if(!inviterNumber()&&isOpenMgm=="1"){  //mgm开关控制
		return;
	}else if(!$("#ischecked").is(":checked")){
        show_tips("请阅读并同意用户协议");
        return;
    }else{
        $("#verifyMobile").html($("#mobile").val());
        $("#basicMsg").hide();
        $("#verifyMobileDiv").show();
        $('.schedule02').addClass('current');
    }

}



/* 注册 */
function register(){
	getUserRequest("pc_register_03");
	var mobile = $("#mobile").val();
	var passWord = $("#txtPassword").val();
	var confirmPassWord = $("#txtConfirmPassword").val();
	
	var sessionID = $("#sessionID").val();    
	var smsCode = $.trim($("#mobileCode").val()); 
	
	if(mobile == null || mobile == ""){
		show_tips("手机号码不能为空");
		return;
	}else if(!checkMobile(mobile)){
		show_tips("请输入正确的手机号");
		return;
	}else if(passWord == null || isNaN(passWord) ||!/^[\d]{6,16}$/.test($("#txtPassword").val())){
		show_tips("请设置6-16位数字密码");
		return;
	}else if(confirmPassWord == null){
		show_tips("请输入登录密码");
		return;
	}else if(confirmPassWord != passWord){
		show_tips("两次密码不一致");
		return;
	}else if(!$("#ischecked").is(":checked")){
		show_tips("请阅读并同意用户协议");
		return;
	}else if(sessionID == null || sessionID == ""){
		show_tips("请获取短信验证码");
		$("#mobileCode").val("");
		return;
	}else if(smsCode == null || smsCode.trim() == ""){
		show_tips("请输入短信验证码");
		return;
	}else{
        $.ajax({
            async: false,
            url: "/AppService/setUp/userRegister.xhtml",
            data: {
           		"mobile": mobile,
                "passWord": passWord,
                "channel": "PC",
                "sessionID": sessionID,
                "smsCode": smsCode
            },
            type: 'post',
            dataType: "json",
            cache: false,
            error: function(textStatus, errorThrown) {
				show_tips("网络繁忙，请稍后再试");
            },
            success: function(data){
                /* 注册成功或者通过三要素找到两个以上的未注册代销用户,通过三要素找到两个以上的已注册代销或已注册直销用户 */
                var returnCode = data.returnCode;
                if(returnCode == "0000"){
					pageEventData("01","00");
                	var newMobile=mobile.substr(0,3)+"****"+mobile.substr(7,4);
                	$("#successMobile").html(newMobile);
                	$("#verifyMobileDiv").hide();
                	$("#registerSuccess").show();
                	t_delay = setInterval (function(){
                		// 积分功能开启才调用积分接口
						if(isOpenMgm=="1"){
                            inviter();
						}
                		var pageId = $("#pageId").val();
						window.location.href="/AppService/business/fund/fundList.shtml?pageSourceId="+pageId+"&eventId=event_registeredId";
                	}, 3000);
                	$("#sessionID").val("");
					$('.schedule03').addClass('current')
                }else if(returnCode == "9998"){/* 未获取到发送验证码时，session中保存的手机号码 */
					show_tips("验证码已失效，请重新获取！");
                	$("#mobileCode").val("");
                }else if(returnCode == "9997"){/* 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致 */
                	show_tips("验证码已失效，请重新获取！");
                	$("#mobileCode").val("");
                }else if(returnCode == "9996"){/* 手机短信验证码验证失败 */
                	show_tips("短信验证码有误");
                	$("#mobileCode").val("");
                }else if(returnCode == "9995"){/* 错误次数过多 */
					show_tips("验证码已失效，请重新获取！");
                	$("#mobileCode").val("");
                }else if(returnCode == "USR-A015"){/* 待绑定的手机号码已被使用 */
                	show_tips("手机号码已被注册请登录");
                }else{
                	show_tips("网络繁忙，请稍后再试");
                }
            }
        });
        
	}
}
/* 以下是进入实名认证页面============================= */
/* 进入实名认证页面 */
function intoRealname(){
	getUserRequest("pc_register_04");
	$("#realNameDiv").show();
	$("#registerSuccess").hide();
	$('#tip1').hide();
	$('#tip2').show();
	$(".schedule04").addClass("current");
}

/**
 * 进入产品风险测评页面
 */
function intoRiskLevel(){
	clearInterval(t_delay);
	var pageId = $("#pageId").val();
	window.location.href="/AppService/business/fund/fundList.shtml?pageSourceId="+pageId+"&eventId=event_registeredId";
	
}

/* 查询支持的银行卡列表 */
function querySupportBankList() {
	var url = "/AppService/business/getSupportBankDesc.xhtml";
	$.ajax({
		async : false,
		url : url,
		data : "",
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				var bankListStr = data.bankListStr;
				if (typeof (bankListStr) != 'undefined') {
					$("#hintBanke").html(bankListStr);
					$("#hintDiv").show();
				} else {
					show_tips("网络繁忙，暂时无法查询支持的银行列表");
				}
			} else {
				show_tips("网络繁忙，暂时无法查询支持的银行列表");
			}
		}
	});
}
/* 关闭支持银行卡列表 */
function closeBank(){
	$("#hintDiv").hide();
}
/* 银行卡格式化 */
function checkBankNumberLength() {
	var temp = $.trim($("#bankNumberTemp").val());
	var bankNumber = temp.replace(re_halfSpace, "");
	bankNumber = bankNumber.replace(re_fullSpace, "");
	if (isNaN(bankNumber)) {
		show_tips("请输入正确的银行卡号");
		return false;
	}
	var length = bankNumber.length;
	if (temp.length > 30) {
		temp = temp.substring(0, 30);
		$("#bankNumberTemp").val(temp);
		return false;
	}

	var tempAdd = "";

	for (var i = 0; i < length; i++) {
		if (i % 4 == 0 && i != 0) {
			tempAdd = tempAdd + " " + bankNumber.charAt(i);
		} else {
			tempAdd += bankNumber.charAt(i);
		}
	}
	$("#bankNumberTemp").val(tempAdd);
	return true;
}
/* 银行卡号非空和数字校验 */
function checkBankNumber(bankNumber) {
	if ($.trim(bankNumber) == '') {
		show_tips("银行卡号不能为空");
		return false;
	}
	if(bankNumber.indexOf('.')>0){
		show_tips("银行卡号有误");
		return false;
	}
	if (isNaN(bankNumber)) {
		show_tips("银行卡号有误");
		return false;
	}
	if (($.trim($("#bankNumberTemp").val())).length > 30) {
		show_tips("银行卡号不能为空");
		return false;
	}
	return true;
}
/* 银行卡号码校验 */
function verifyBankNumber() {
	var idno = $.trim($("#idNo").val());
	var name = $.trim($("#userName").val());
	var idtp = "0";
	var bl = false;
	var bankNumberTemp = $.trim($("#bankNumberTemp").val());
	bankNumberTemp = bankNumberTemp.replace(re_halfSpace, "");
	bankNumberTemp = bankNumberTemp.replace(re_fullSpace, "");
	if (checkBankNumber(bankNumberTemp) && checkName(name) && checkIdNo(idno)) {

		var params = {
			"bankNumber" : bankNumberTemp
		};
		var actionUrl = "/AppService/business/queryBankInfoByBankNumber.xhtml";
		$.ajax({
			async : false,
			url : actionUrl,
			data : params,
			dataType : "json",
			cache : false,
			type : 'post',
			error : function(textStatus, errorThrown) {
				show_tips("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				if (data.returnCode == "0000") {
					var returnMsg = data.returnMsg;
					if (data.listIsNull == "no") {

						if (data.status == "Y") {
							var bankName = data.bankInfoDto.bankName;

							$("#bankNumber").val(bankNumberTemp);
							$("#bankName").val(bankName);
							$("#channelNo").val(data.bankInfoDto.channelNo);
							$("#bankNo").val(data.bankInfoDto.bankNo);
							bl = true;
						} else {
							show_tips(data.returnMsg);
							bl = false;
						}
					} else {
						show_tips(data.returnMsg);
						bl = false;
					}

				} else if (data.returnCode == "9999") {
					show_tips("信息验证未通过，请检查输入信息是否有误");
					bl = false;
				} else {
					show_tips("网络繁忙，请稍后再试");
					bl = false;
				}
			}
		});
	}
	return bl;
}
/* 验证身份证号码是否被除了用户之外的其他用户注册 */
function verifyIdNo() {
	var idno = $.trim($("#idNo").val());
	var idtp = "0";
	var mobile = $.trim($("#bankPhone").val());
	var number = $.trim($("#bankNumberTemp").val());
	var bankNumber = number.subString(number.length - 4, number.length);
	var bankName = $.trim($("#bankName").val());
	var bl = false;
	var params = {
		"idno" : idno,
		"idtp" : idtp,
		"mobile" : mobile,
		"bankNumber" : bankNumber,
		"bankName" : bankName
	};

	var actionUrl = "/AppService/business/checkIdNoByBankAuthentication.xhtml";
	$.ajax({
		async : false,
		url : actionUrl,
		data : params,
		dataType : "json",
		type : 'post',
		cache : false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			/**
			 * 用户已注册 0000 代销/直销/母公司系统注册，本交易系统未注册 USR-A001 用户未注册 USR-A002 参数错误
			 * idNo/idType必填 USR-B002 系统运行时不可知异常 USR-8000
			 */

			/*
			 * USR-A003 用户未注册
			 * ，详见UserInfoexManagerImpl.checkIdNoByBankAuthentication()
			 */
			if ("USR-A002" == data.returnCode || "USR-A001" == data.returnCode
					|| "USR-A003" == data.returnCode) {
				bl = true;
			} else if ("0000" == data.returnCode) {
				show_tips("证件已被注册");
				bl = false;
			} else if (data.returnCode == "8000") {
				window.location.href="/login/register.shtml";
				bl = false;
			} else {
				show_tips("网络繁忙，请稍后再试");
				bl = false;
			}
		}
	});
	return bl
}
/* 验证手机号码 */
function checkMobile(mobile) {

        if (mobile == null || mobile == "") {
            show_tips("手机号码不能为空");
            return false;
        } else {
            if (!Validater.isMobilePhoneNumber(mobile)) {
                show_tips("手机号码有误");
                return false;
            }
        }
        return true;


}
/* 获取验证码 */
function getMobileVerifyCode1() {
	var status=$("#verifyStatus").val();
	/*if(status == 'no'){*/
		var mobile = $.trim($("#bankPhone").val());
		var number = $.trim($("#bankNumberTemp").val());
		var bankNumber = number.subString(number.length - 4, number.length);
		if (checkMobile(mobile)&&verifyBankNumber()) {
			var bankName = $.trim($("#bankName").val());
			bankName = encodeURI(bankName);
			var params = {
				"mobile" : mobile,
				"bankNumber" : bankNumber,
				"bankName" : bankName,
				"msgType" : "1"
			};
			var actionUrl = "/AppService/setUp/getVerifyCodeByAuthAndPay.xhtml";
			$.ajax({
				async : false,
				url : actionUrl,
				data : params,
				type : 'post',
				dataType : "json",
				cache : false,
				error : function(textStatus, errorThrown) {
					show_tips("网络繁忙，请稍后再试。");
				},
				success : function(data) {
					if (data.returnCode == "0000") {
						/* 验证码发送成功 */
						$("#sessionID").val(data.sessionID);
						$("#getVerifyCodeByBankCard").attr('onclick', "javascript:void(0)");
						/* 读秒 */
						countDown1();
						$("#point-clickafter1").show();
					} else if (data.returnCode == "9000") {
						show_tips(data.returnMsg);
					} else {
						show_tips("网络繁忙，请稍后再试");
					}
				}
			});
		}
	/*}*/
}
function checkVerfyCode(verifyCode){
	if (verifyCode == "" || verifyCode == null) {
		show_tips("请填写手机验证码");
		return false;
	}
	return true;
}
/* 验证手机验证码 */
function checkVrfCode() {
	getUserRequest("pc_register_05");
	var sessionId = $.trim($("#sessionID").val());
	var mobile = $.trim($("#bankPhone").val());
	var verifyCode = $.trim($("#verifyCode").val());
	var idno = $.trim($("#idNo").val());
	var name = $.trim($("#userName").val());
	var bankNumberTemp = $.trim($("#bankNumberTemp").val());
	bankNumberTemp = bankNumberTemp.replace(re_halfSpace, "");
	bankNumberTemp = bankNumberTemp.replace(re_fullSpace, "");
	/* 让获取验证码可以重新获取 */
	if (timer1 != 60) {
		timer1 = 0;
	}
	if (checkBankNumber(bankNumberTemp) && checkIdNo(idno) && checkName(name)&&checkMobile(mobile)) {
		if (checkMobileAndVerifyCode()) {
			if (verifyBankNumber() && verifyIdNo()) {
				var actionUrl = "/AppService/setUp/authCheckVrfCode.xhtml";
				$.ajax({
					async : false,
					url : actionUrl,
					data : {
						"sessionID" : sessionId,
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
							/* 验证码验证通过，调用鉴权接口 */
							bankAuthentication();
						} else {
							var errorMsg = "";
							if (data.returnCode == "USR-5201") {
								errorMsg = "短信验证码有误！";
								if (timer != 60) {
									timer = 0;
								}
							} else if (data.returnCode == "USR-5202") {
								errorMsg = "验证码已失效，请重新获取验证码";
								if (timer != 60) {
									timer = 0;
								}
							} else if (data.returnCode == "USR-5203") {
								errorMsg = "验证码已失效，请重新获取验证码";
								if (timer != 60) {
									timer = 0;
								}
							} else if (data.returnCode == "9002") {
								errorMsg = "当前手机号码不是获取验证码的手机号码";
								if (timer != 60) {
									timer = 0;
								}
							} else if (data.returnCode == "9003") {
								errorMsg = "非法请求";
								if (timer != 60) {
									timer = 0;
								}
							} else if (data.returnCode == "9999") {
								errorMsg = "网络繁忙，请稍后再试";
								if (timer != 60) {
									timer = 0;
								}
							} else {
								errorMsg = data.returnMsg;
								if (timer != 60) {
									timer = 0;
								}
							}
							show_tips(errorMsg);
						}
					}
				});
			}
		}
	}
}
/* 验证其他 手机号码、验证码 */
function checkMobileAndVerifyCode() {
	var txtMobile = $.trim($("#bankPhone").val());
	var verifyCode = $.trim($("#verifyCode").val());
	var sessionID = $.trim($("#sessionID").val());
	/* 验证手机号码 */
	if (!checkMobile(txtMobile)) {
		return false;
	}
	if (sessionID == "" || sessionID == null) {
		show_tips("请获取短信验证码");
		$("#verifyCode").val("");
		return false;
	}
	if (verifyCode == "" || verifyCode == null) {
		show_tips("请填写手机验证码");
		return false;
	}
	return true;
}
/*调用鉴权接口*/
function bankAuthentication() {
	var bankNumber = $.trim($("#bankNumber").val());
	var bankName = $.trim($("#bankName").val());
	var channelNo = $.trim($("#channelNo").val());
	var bankNo = $.trim($("#bankNo").val());
	var name = $.trim($("#userName").val());
	var idNo = $.trim($("#idNo").val());
	var mobile = $.trim($("#bankPhone").val());

	bankName = encodeURI(bankName);
	name = encodeURI(name);

	var params = {
		"bankNumber" : bankNumber,
		"bankName" : bankName,
		"channelNo" : channelNo,
		"bankNo" : bankNo,
		"name" : name,
		"idNo" : idNo,
		"mobile" : mobile
	};
	var actionUrl = "/AppService/business/openAccount.xhtml";

	$.ajax({
		async : false,
		url : actionUrl,
		data : params,
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				/*工行签约所需*/
				$("#tradeAcct").val(data.userAcctDto.tradeAcct);
				$("#bankNo").val(data.userAcctDto.bankNo);
				if(data.userAcctDto.bankNo != null && data.userAcctDto.bankNo == "002"){
					$("#contractSign_span_01").show();
				}
				
				$("#realNameDiv").hide();
				$("#tPasswordDiv").show();
				$(".schedule05").addClass('current');
			} else if (data.returnCode == "66") {/* 支付渠道接口返回 */
				show_tips("验证失败，身份信息不符！");
				$("#tPasswordDiv").hide();
				$("#realNameDiv").show();
			} else if (data.returnCode == "8000") {
				show_tips(data.returnMsg);
				window.location.href = "/login/login.shtml";
			} else if (data.returnCode == "9003") {
				show_tips(data.returnMsg);
			}else if(data.returnCode == 'CMB01'){
				$("#cmb_01").show();
				var cmbDto = data.cmbDto;
				$("#merchant").val(cmbDto.merchant);
				$("#uin").val(cmbDto.uin);
				$("#url").val(cmbDto.url);
				$("#timeStamp").val(cmbDto.timeStamp);
				$("#checkSum").val(cmbDto.checkSum);
				$("#mcAccountNo").val(cmbDto.mcAccountNo);
				$("#username").val(cmbDto.username);
				$("#reserve1").val(cmbDto.reserve1);
				$("#reserve2").val(cmbDto.reserve2);
				$("#cmbForm").attr("action",cmbDto.cmbSignUrl);
			} else {
				show_tips(data.returnMsg);
			}
		}
	});
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
	getUserRequest("pc_register_06");
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
					$("#successRealnameDiv").show();
					$("#tPasswordDiv").hide();
					$("#successName").html($("#userName").val());
					$(".schedule06").addClass('current');
				} else if (returnCode == 'USR-1I01') {
					show_tips("设置安全码失败：用户已锁定");
				} else if (returnCode == 'USR-1I95') {
					show_tips("设置安全码失败：非交易用户,请先绑定银行卡");
				} else if (returnCode == '8000') {
					show_tips(returnMsg);
					/* 跳转到登录页面*/
					window.location.href="/login/register.shtml";
				} else {
					show_tips("网络繁忙，请稍后再试");
				}
			}
		});
	}
}
/*签约*/
function contractSign(){
	var tradeAcct = $("#tradeAcct").val();
	var bankNo = $("#bankNo").val();
	var channelNo = $("#channelNo").val();
	if(bankNo == null || bankNo != "002" || tradeAcct == null || tradeAcct == "" || channelNo == null || channelNo == ""){
		return;
	}
	$.ajax({
		async : false,
		url : "/AppService/business/contractSign.xhtml",
		data : {
			"tradeAcct" : tradeAcct,
			"bankNo" : bankNo,
			"channelNo" : channelNo
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
			
			if(returnCode != null && returnCode == "0000"){
				var dto = data.icbcSignParaDto;
				/*$("#order").attr("ACTION",dto.signOnlineUrl);*/
				
				$("#contractSign_interfaceName").val(dto.interfaceName);
				$("#contractSign_interfaceVersion").val(dto.interfaceVersion);
				$("#contractSign_selserialNo").val(dto.selserialNo);
				$("#contractSign_payNo").val(dto.payNo);
				$("#contractSign_selpayType").val(dto.selpayType);
				$("#contractSign_selcorpId").val(dto.selcorpId);
				$("#contractSign_selaccountNo").val(dto.selaccountNo);
				$("#contractSign_regDate").val(dto.regDate);
				$("#contractSign_HSURL").val(dto.HSURL);
				$("#contractSign_merCertID").val(dto.merCertID);
				$("#contractSign_Language").val(dto.language);
				$("#contractSign_certDate").val(dto.certDate);
				$("#contractSign_allowFinalDate").val(dto.allowFinalDate);
				$("#contractSign_accountNo").val(dto.accountNo);
				$("#contractSign_certData").val(dto.certData);
				
				$("#contractSign_order").submit();
				
				$("#contractSign_div_03").show();
			}
			
		}
	});
}

/*查询签约*/
function queryCommandByBankAccoNo(){
	var tradeAcct = $("#tradeAcct").val();
	var bankNo = $("#bankNo").val();
	var channelNo = $("#channelNo").val();
	if(bankNo == null || bankNo != "002" || tradeAcct == null || tradeAcct == "" || channelNo == null || channelNo == ""){
		return;
	}
	$.ajax({
		async : false,
		url : "/AppService/business/queryCommandByBankAccoNo.xhtml",
		data : {
			"tradeAcct" : tradeAcct,
			"bankNo" : bankNo,
			"channelNo" : channelNo
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

			if(returnCode != null && returnCode == "0000"){
				show_tips3("签约成功，正在进入产品超市...");
				setTimeout('gotoUrl("/AppService/business/fund/fundList.shtml")',2000);
			}else{
				$("#contractSign_div_02").show();
				$("#contractSign_div_03").hide();
				show_tips(data.returnMsg);
			}
		}
	});
}
function cmb_lose(str){
	$("#"+str).hide();
}
function cmbSubmit(){
	document.cmbForm.submit();
	$("#cmb_01").hide();
	$("#cmb_02").show();
}



// 积分功能开关
function integralOpenOrClose() {
    $.ajax({
        url: '/AppService/integral/enableIntegral.xhtml',
        data: {},
        dataType: 'json', //服务器返回json格式数据
        type: 'get', //HTTP请求类型
        success: function(data){
            if (data.data== '1') {
                $('#inventPhoneBox').show()
                isOpenMgm='1';
            } else if(data.data='0'){
                $('#inventPhoneBox').hide()
                isOpenMgm='0';
            }
        }

    })
}