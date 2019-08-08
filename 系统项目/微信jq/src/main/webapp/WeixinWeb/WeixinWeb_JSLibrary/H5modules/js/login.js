var re_halfSpace = new RegExp(" ","g");/* 半角空格 */
var re_fullSpace = new RegExp("　","g");/* 全角空格 */
var eventId='event_wx_loginId',pageSource='wx_loginId',pageId='wx_loginId';
$(document).ready(function(e){
	var urlParams = getUrlParams();
 	eventId = urlParams['eventId']?urlParams['eventId']:eventId;
 	pageSource = urlParams['pageSource']?urlParams['pageSource']:pageSource;

	cmwaOnkeyup("userName;passWord;randomCode",checkLoginParam);
	$(".header .top-a h2").html("用户登录");
	document.title="用户登录";
	getUserRequest("login");/* 此处subPath为页面内行为 */
	getLoginPageRandomCode();
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
	myScroll.refresh();
	personageScroll.refresh();
});

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

function webLogin(){
	var userName = $("#userName").val();
	var passWord = $("#passWord").val();
	var randomCode = $("#randomCode").val();
	randomCode = randomCode.replace(re_halfSpace,"");
	randomCode = randomCode.replace(re_fullSpace,"");

	if(userName == null || userName == ""){
		errorRemark("请输入手机号<br>或身份证号");
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
		$.ajax({
			async:false,
			url : apiHost + "/api/userLogin",
			data : {
				"userName":userName,
				"passWord":passWord,
				"randomCode":randomCode,
				"channel":"WXIE",
				"jsessionid":getCookie("JSESSIONID"),
				"userId":localStorage.getItem("userId")
			},
			dataType : "json",
			type:"POST",
			cache : false,
			error : function(textStatus,errorThrown){
				errorRemark("网络繁忙，请稍后再试。");
			},
			success : function(data){
				if(data.success){
					 if(data.resp.returnCode=="0000"){
						 pageEventDataForH5("04","01",data.resp.userInfoExtend.cmfuserid);
						 // addCookie("eventId","event_wxYh91No10_loginId");  //远航5号
						// operatingRecord(getCookie("pageSource"),"event_wxYh91No10_loginId","wx_yh91No10_loginPageId","","","","")//远航5号
						 //addCookie("eventId","event_hlNo5_loginId");  //和利
						 //operatingRecord(getCookie("pageSource"),"event_hlNo5_loginId","wx_hlNo5_loginPageId","","","","")//和利
//						 addCookie("eventId","event_heli6_loginId");  //和利6
//						 operatingRecord(getCookie("pageSource"),"event_heli6_loginId","wx_heli6_loginPageId","","","","")//和利6
						 
						 userAsync(data.resp.userInfoExtend.openid,data.resp.userInfoExtend.cmfuserid);
						 
						 localStorage.setItem("cmfUserId",data.resp.userInfoExtend.cmfuserid);
						 var target=localStorage.getItem("target");
						 if(target=="cfb273"){
                             addCookie("eventId","event_cfb273_loginId");  //财富宝
                             operatingRecord(getCookie("pageSource"),"event_cfb273_loginId","wx_cfb273_loginPageId","","","","")//财富宝
						 }else if(target=="7DaysPro"){
                             addCookie("eventId","event_7DaysPro_loginId");  //七天产品
                             operatingRecord(getCookie("pageSource"),"event_7DaysPro_loginId","wx_7DaysPro_loginPageId","","","","")//七天产品
						 }else if(target=="currentPro"){
                             addCookie("eventId","event_currentPro_loginId");  //活期产品
                             operatingRecord(getCookie("pageSource"),"event_currentPro_loginId","wx_currentPro_loginPageId","","","","")//活期产品
						 }else if(target=="cfb10"){
                             addCookie("eventId","event_cfb10_loginId");  //财富宝
                             operatingRecord(getCookie("pageSource"),"event_cfb10_loginId","wx_cfb10_loginPageId","","","","")//财富宝
                         }else if(target=="cfb126"){
                             addCookie("eventId","event_cfb126_loginId");  //财富宝126天
                             operatingRecord(getCookie("pageSource"),"event_cfb126_loginId","wx_cfb126_loginPageId","","","","")//财富宝
                         }else if(target=="annualBonus"){
                             addCookie("eventId","event_annualBonus_loginId");  //财富宝126天
                             operatingRecord(getCookie("pageSource"),"event_annualBonus_loginId","wx_annualBonus_loginPageId","","","","")//财富宝
                         }else if(target=="cfb28"){
							addCookie("eventId","event_cfb28_loginId");  //财富宝28天
							operatingRecord(getCookie("pageSource"),"event_cfb28_loginId","wx_cfb28_loginPageId","","","","")//财富宝28天
						 }else if(target=="cfbAll"){
							addCookie("eventId","event_cfbAll_loginId");  //财富宝所有
							operatingRecord(getCookie("pageSource"),"event_cfbAll_loginId","wx_cfbAll_loginPageId","","","","")//财富宝28天
						 }else if(target=="cfb203"){
							addCookie("eventId","event_cfb203_loginId");  //财富宝203
							operatingRecord(getCookie("pageSource"),"event_cfb203_loginId","wx_cfb203_loginPageId","","","","")//财富宝28天
						 }else if(target=="cfbQun"){
							addCookie("eventId","event_cfbQun_loginId");  //财富宝203
							operatingRecord(getCookie("pageSource"),"event_cfbQun_loginId","wx_cfbQun_loginPageId","","","","")//财富宝28天
						 }else if(target=="cfb207"){
							addCookie("eventId","event_cfb207_loginId");  //财富宝203
							operatingRecord(getCookie("pageSource"),"event_cfb207_loginId","wx_cfb207_loginPageId","","","","")//财富宝28天
						 }else if(target=="cfb209"){
							addCookie("eventId","event_cfb209_loginId");  //财富宝209
							operatingRecord(getCookie("pageSource"),"event_cfb209_loginId","wx_cfb209_loginPageId","","","","")//财富宝28天
						 }else if(target=="fatherDay"){
							addCookie("eventId","event_fatherDay_loginId");  //父亲节
							operatingRecord(getCookie("pageSource"),"event_fatherDay_loginId","wx_fatherDay_loginPageId","","","","")//父亲节
						 }else if(target=="chenli"){
							addCookie("eventId","event_chenli_loginId");  //父亲节
							operatingRecord(getCookie("pageSource"),"event_chenli_loginId","wx_chenli_loginPageId","","","","")//父亲节
						 }else if(target=="chenrui"){
							addCookie("eventId","event_chenrui_loginId");  //父亲节
							operatingRecord(getCookie("pageSource"),"event_chenrui_loginId","wx_chenrui_loginPageId","","","","")//父亲节
						 }else if(target=="cfbAll2"){
							addCookie("eventId","event_cfbAll2_loginId");  //财富宝所有
							operatingRecord(getCookie("pageSource"),"event_cfbAll2_loginId","wx_cfbAll2_loginPageId","","","","")//财富宝28天
						 }
						 if(target=="proList"){
							 location.href="/WeixinService/index.shtml"
						 }else{
							 location.href="/WeixinService/H5modules/H5Content/"+localStorage.getItem("target")+".html?pageSource="+getCookie("pageSource")+"&eventId="+getCookie("pageSource")
						 }						
					 }else{
					 	if(data.resp.returnCode == "USR-A005"){/* 用户登录已锁定 */
							errorRemark("用户已锁定<br>请半小时后重试");
						}else if(data.resp.returnCode == "USR-A007"){/* 登录用户未注册 */
							errorRemark("请输入正确的手机号<br>或身份证号码");
						}else if(data.resp.returnCode == "USR-A009"){/* 密码错误，但未达到错误次数上限 */
							$("#passWord").val("");
							errorRemark("账户名与密码不匹配<br>请重新输入");
						}else if(data.resp.returnCode == "9000" || data.resp.returnCode == "9301"){
							errorRemark("验证码有误");
						}else{
							errorRemark("登录失败，请稍后再试");
						}
						$("#randomCode").val("");
						getLoginPageRandomCode();
						$("#login_btn").removeClass("act").attr("onclick","");
					 }
				}else{
					errorRemark("登录失败，请稍后再试");
				}
			}
		});
	}
}



/* 图片验证码	登录页面  */
function getLoginPageRandomCode(){
	$("#loginPageRondomCodeImg").attr("src",apiHost + "/api/verify?jsessionid="+getCookie("JSESSIONID"));
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

function onceRegister(){
	location.href="/WeixinService/H5modules/userRegister.html&pageSource="+getUrlSearchParams("pageSource")
}
/**
 * 用户信息同步
 * @param {Object} openid
 * @param {Object} cmfuserid
 */
function userAsync(openid,cmfuserid){
	$.ajax({
		async:false,
		url : apiHost + "/api/userLogin",
		data : {
			"openid":openid,
			"cmfuserid":cmfuserid
		},
		dataType : "json",
		type:"POST",
		cache : false,
		success : function(data){
			
		},
		error : function(textStatus,errorThrown){
			errorRemark("网络繁忙，请稍后再试。");
		},
	})
	 
}

/**
 * 插入用户操作记录
 * @param registmark 登录渠道
 * @param type 用户操作类型
 */
function pageEventDataForH5(registmark,type,cmfuserid){
	var param = {
	"loginChannel":registmark,
	"type":type,
	"cmfuserid":cmfuserid
	};
	$.ajax({
		async : false,
		url : "/WeixinService/log/insertUserOperateLog.xhtml",
		data : JSON.stringify(param),
		dataType : "json",
		contentType:"application/json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
		}
	});
}
