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
	checkLogin();
	getUserRequest("login");/* 此处subPath为页面内行为 */
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

/*****授权*****/
var urlParams = getUrlParams();
var userId="";
var userid=urlParams['userid']?urlParams['userid']:localStorage.getItem("userid");
var openId="";
var host=window.location.protocol+"//"+window.location.host;
var apiHost="" //生产为相对路径
initPage();
function initPage(){
	queryUserInfo();
	if(!userid){  //判断用户有没有静默授权
		localStorage.setItem("eventId",getUrlParameter("eventId"))
		localStorage.setItem("pageSource",getUrlParameter("pageSource"))
		location.href=host+"/auth/proxy-silent.html?scope=snsapi_base&target_url="+host+"/WeixinService/weixinLogin/login.shtml";
	}
}
function queryUserInfo(){
	if(!!userid){  //如果有userid
		$.ajax({
			url:apiHost + "/api/wxuserinfo",
			async:false,
			type:"get",
			data:{
				userId:userid
			},
			dataType:"json",
			success:function(res){
				if(res.success){
					userId=res.resp.userId;
					localStorage.setItem("userId",userId);
				}
			}
		})
	}
}
/*****授权end*****/
function checkLogin(){
	var actionUrl = "/WeixinService/setUp/checkLogin.xhtml";
    $.ajax({
    	async:false,
        url: actionUrl,
        data: "",
        dataType: "json",
        cache: false,
        type: 'post',
        error : function(textStatus, errorThrown) {  
            errorRemark("网络繁忙，请稍后再试。");  
        }, 
        success : function (data)
        {
			if(data.errorCode=="0000"){/* 已登录 */
				if(data.requestPath==null || data.requestPath==''){
					redirectUrl("/WeixinService/business/query/fundList.shtml");
			    }else{
					redirectUrl(data.requestPath);
			    }
			}
        }      
    });  
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
		$("#maskDiv").show()
		$.ajax({
			
			url : "/WeixinService/user/weixinLogin.xhtml",
			data : {
				"userName":userName,
				"passWord":passWord,
				"randomCode":randomCode,
				"channel":"WXIE"
			},
			dataType : "json",
			type:"POST",
			cache : false,
			error : function(textStatus,errorThrown){
				$("#maskDiv").hide()
				errorRemark("网络繁忙，请稍后再试。");
			},
			success : function(data){
				$("#maskDiv").hide()
				if(data.returnCode=="0000"){
					pageEventData("04","01");
					recordOperation({eventId:'event_wx_loginId',pageId:pageId,pageSource:getUrlParameter("pageSource")?getUrlParameter("pageSource"):localStorage.getItem("pageSource")});
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
						errorRemark("账户名与密码不匹配<br>请重新输入");
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

function  loadActive(){
	var time = getNowTime();
	
	var quarterDate = queryParamList("SYSTEM","QUARTERDATE","");
	if(quarterDate && time <= quarterDate[0].pmco){
		//季报活动
		if(pageSource && pageSource == 'wx_model_textProRepotId'){
			//登录跳转我的消息
			redirectUrl("/WeixinService/business/query/msgList.shtml?pageSource="+pageSource);
			return true;
		}
	}
	//检测测评
	var rst = checkIsRiskLevel('PRODPLANDATE');

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
function checkIsRiskLevel(){
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