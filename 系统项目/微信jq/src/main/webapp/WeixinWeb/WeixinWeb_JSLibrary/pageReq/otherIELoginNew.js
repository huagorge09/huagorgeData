﻿﻿var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */
var eventId='event_wx_loginId',pageSource='wx_loginId',pageId='wx_loginId';
$(document).ready(function(e){
	
	var urlParams = getUrlParams();
	 eventId = urlParams['eventId']?urlParams['eventId']:eventId;
	 pageSource = urlParams['pageSource']?urlParams['pageSource']:pageSource;
	if(eventId && 'event_wxModelMess_registerId' == eventId){
		//记录点击1图文消息
		pageId = 'wx_model_registeredId';
		pageSource = 'wx_model_registeredId';
		recordOperation({eventId:eventId,pageId:pageId,pageSource:pageSource});
	}
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
	
	cmwaOnkeyup("userName;passWord;randomCode",checkLoginParam);
	$(".header .top-a h2").html("用户登录");
	document.title="用户登录";
	getLoginPageRandomCode();
	getUserRequest("other-ie-login");/* 此处subPath为页面内行为 */
	myScroll.refresh();
	personageScroll.refresh();
});


function webLogin(){
	var userName = $("#userName").val();
	var passWord = $("#passWord").val();
	var randomCode = $("#randomCode").val();
	randomCode = randomCode.replace(re_halfSpace,"");
	randomCode = randomCode.replace(re_fullSpace,"");
	
	if(userName == null || !(isMobile(userName) || checkIdcard(userName) == "验证通过!")){
		errorRemark("请输入正确的手机号<br>或身份证号码");
		return;
	}else if(passWord == null || passWord == ""){
		errorRemark("密码不能为空");
		return;
	}else if(!/^[\d]{6,16}$/.test(passWord)){
		errorRemark("密码格式有误，请输入<br>6位以上数字登录密码");
		return;
	}else if(randomCode == null || randomCode.trim() == ""){
		errorRemark("请输入验证码");
		return;
	}else if(!$("#ischecked").is(":checked")){
        errorRemark("请阅读并同意用户协议");
        return;
    }else{
		$('#maskDiv').show();
		$.ajax({
			url : "/WeixinService/setUp/webLogin.xhtml",
			data : {
				"userName":userName,
				"passWord":passWord,
				"randomCode":randomCode
			},
			dataType : "json",
			type:"POST",
			cache : false,
			error : function(textStatus,errorThrown){
				$("#maskDiv").hide();
				errorRemark("网络繁忙，请稍后再试。");
			},
			success : function(data){
				$("#maskDiv").hide();
				if(data.returnCode=="0000"){
					pageEventData("04","01");
					recordOperation({eventId:'event_wx_loginId',pageId:pageId,pageSource:pageSource});
					//加载活动
					if(loadActive()){
						return;
					}
					if(data.requestPath==null || data.requestPath==''){
							var url = queryParamList("SYSTEM","weixinLogin","")[0].pmco;
							redirectUrl(url+"?pageSource="+pageSource);
					    }else{
							redirectUrl(data.requestPath);
					    }
				}else{
					if(data.returnCode == "USR-A005"){/* 用户登录已锁定 */
						errorRemark("用户已锁定<br>请半小时后重试");
					}else if(data.returnCode == "USR-A007"){/* 登录用户未注册 */
						errorRemark("请输入正确的手机号<br>或身份证号码");
					}else if(data.returnCode == "USR-A009"){/* 密码错误，但未达到错误次数上限 */
						$("#passWord").val("");
						errorRemark("账号密码不匹配");
					}else if(data.returnCode == "9000" || data.returnCode == "9301"){
						errorRemark("验证码有误");
					}else{
						errorRemark("登录失败，请稍后再试");
					}
					$("#randomCode").val("");
					getLoginPageRandomCode();
					$("#login_btn").removeClass("act").attr("onclick","");
				}
			}
		});
	}
}

/* 图片验证码	登录页面  */
function getLoginPageRandomCode(){
	$("#loginPageRondomCodeImg").attr("src","/WeixinService/buildimageservlet.xhtml");
}
/* 验证输入框的有效性  */
function checkLoginParam(){
	var userName = $("#userName").val();
	var passWord = $("#passWord").val();
	var randomCode = $("#randomCode").val();
	if(userName == null || userName == ""){
		$("#login_btn").removeClass("act").attr("onclick","");
		return;
	}else if(passWord == null || passWord == ""){
		$("#login_btn").removeClass("act").attr("onclick","");
		return;
	}else if(randomCode == null || randomCode.trim() == ""){
		$("#login_btn").removeClass("act").attr("onclick","");
		return;
	}else{
		$("#login_btn").addClass("act").attr("onclick","webLogin()");
	}
}

function  loadActive(){
	
	
	var time = getNowTime(); 
	var pordPlanDate = queryParamList("SYSTEM","PRODPLANDATE","");
	var hlDate = queryParamList("SYSTEM","HELIDATE","");
	var rst = checkIsRiskLevel('PRODPLANDATE');
	
	var quarterDate = queryParamList("SYSTEM","QUARTERDATE","");

	if(quarterDate && time <= quarterDate[0].pmco){
		//季报活动
		if(pageSource && pageSource == 'wx_model_textProRepotId'){
			//登录跳转我的消息
			redirectUrl("/WeixinService/business/query/msgListNew.shtml?pageSource="+pageSource);
			return true;
		}
	}
	//	if(pordPlanDate && time <= pordPlanDate[0].pmco){
//		//财富宝活动
//		if(rst && !rst.flag){
//			//测评过直接跳转
//			redirectUrl("/WeixinService/business/query/wealthTreasureNew.shtml?pageSource="+pageSource);
//			return true;
//		}
//	}
//	//和利
//	if(hlDate && time <= hlDate[0].pmco){
//		if(rst && !rst.flag){
//			redirectUrl("/WeixinService/business/query/heliNew.shtml?pageSource="+pageSource);
//			return true;
//		}
//	}
	//和利2号
	var hlDate = queryParamList("SYSTEM","HELI2WXDATE","");
	if(hlDate && time <= hlDate[0].pmco){
		if(rst && !rst.flag){
			redirectUrl("/WeixinService/business/query/heliNew.shtml?pageSource="+pageSource);
			return true;
		}
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

/* 未风险测评则弹出风险测评弹框  */
function checkIsRiskLevel(type){
	var result = {};
	result.flag=false;
	var urlVal="/WeixinService/business/queryIsNeedTest.xhtml";
    $.ajax({
    	async:false,
		url:urlVal,
		type:"post",
		dataType:'json',
		data:{},
		error:function(){
			errorRemark("网络繁忙，请稍后再试。"); 
		},
		success:function(data, textStatus){
			//data.riskLevel=='0'
			var riskLevel = "";
			var riskEvalDate = "";
			
			if(!!data){
				riskLevel = data.riskLevel;
				riskEvalDate = data.riskEvalDate;
			}
			
			if(!riskEvalDate || "0" == riskLevel){
				//未测评 提示 
				result.flag=true;
			}else if(!!riskEvalDate && !!riskLevel && "0" != riskLevel){
			    //时间已过 则 提示过期
				var nowDate= new Date();
			    var date = new Date(riskEvalDate); 
			    var dataDiff = (nowDate - date) / 86400000;
			    if(dataDiff >= 365){
			    	//过期补充提示
					result.flag=true;
			    }
			}
		}
	});  
    return result;
}

