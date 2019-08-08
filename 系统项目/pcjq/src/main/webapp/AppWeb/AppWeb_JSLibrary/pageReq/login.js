var i = 0;
$(document).ready(function(){
	getUserRequest("pc_login");
	checkIsLogin();
	$(".nav.fr ul li a").removeClass("current");
	getRandomCode();
//	document.title = "用户登录";
	$(".login-panel .hrefcompany").animate({top:'0px',right:'0px'},800);
	$(".login-panel .hrefcompany").click(function(){
		$(".login-panel .hrefcompany").animate({top:'-66px',right:'0px'},800);
        $(".login-panel .hrefpersonal").animate({top:'0px',right:'0px'},800);
        $(".login-panel.company").delay(2000).removeClass("hidden");
        $(".login-panel.personal").delay(2000).addClass('hidden');
		$("#abs").addClass('hidden')
        getRandomCode();
    });
    $(".company .hrefpersonal").click(function(){
    	$(".login-panel .hrefcompany").animate({top:'0px',right:'0px'},800);
        $(".login-panel .hrefpersonal").animate({top:'-66px',right:'0px'},1000);
        $(".login-panel.personal").delay(2000).removeClass("hidden");
        $(".login-panel.company").delay(2000).addClass('hidden');
		$("#abs").addClass('hidden')
        getRandomCode();
    });
	userTypeInit()
})


/*判断用户是否登录，如果登录就跳转到用户中心*/
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
			if(data.returnCode=='0000'){
				goToURL("/AppService/applicationGroups.shtml");
			}
		}
	});
}
/*下一张验证码 注册*/ 
function getRandomCode(){
	$("#rondomCodeImg_1,#rondomCodeImg_2").attr("src","/AppService/setUp/buildimageservlet.xhtml?count="+i);
	i++;
}
/* 验证手机号码 */
function checkMobile(mobile) {
	if (mobile == null || mobile == "") {
		show_tips("手机号码不能为空");
		return false;
	} else {
		if (!Validater.isMobilePhoneNumber(mobile)) {
			show_tips("请输入正确的手机号");
			return false;
		}
	}
	return true;
}


function checkHasParam(eventId){
	var pageSourceId = getUrlParameter("pageSourceId");
    var uEventId = getUrlParameter("eventId");
    var pageId = $('#pageId').val();
    	if(eventId == 'event_loginId' && pageSourceId=='banner'){
				return true; // 代表该事件是跳转到财富宝页面的
		} else{
			if(pageSourceId==''){
				pageSourceId = pageId;
			}
			operatingRecord(pageSourceId,pageId,eventId,""); // 针对从首页财富宝banner跳转到login页面进行埋点
		}
}

function login(){
	var eventId = $('#person').attr('data-event');
	var mobile = $("#mobile").val();
	var password = $("#password").val();
	var vrfCode_1 = $("#vrfCode_1").val();
	var randomCode = $("#vrfCode_1").val();
	var agreeCheckbox = $("#agreeCheckbox");
	mobile = mobile.toUpperCase();
	var isIdNo = legion.regValidater.isIdCardNumber4ZSJJ(mobile);
	
	if(""==mobile){
		show_tips("手机号码或身份证号码不能为空");
		return;
	}
	if(mobile.length == 11){
		if(!checkMobile(mobile)){
			show_tips("请输入正确的手机号码");
	 		return;
		}
	}
	if(mobile.length != 11&&mobile.length != 15&&mobile.length != 18){
		show_tips("请输入正确的手机号码或身份证号码");
 		return;
	}
	if(mobile.length == 15 || mobile.length == 18){
		if(!(isIdNo==true)){
			show_tips("请输入正确的身份证号码");
			return;
		}
	}
	if(""==password){
		show_tips("密码不能为空");
		return;
	}
	if(!/^[\d]{6,16}$/.test(password)){
		show_tips("密码格式有误，请输入6位以上数字登录密码");
		return;
	}
	if(""==randomCode){
		show_tips("请输入验证码");
		return;
	}
	if(randomCode.length!=4){
		show_tips("验证码有误");
		$("#vrfCode_1").val("");
		return;
	}
	if(!agreeCheckbox.attr('checked')){
		show_tips("请阅读并同意用户协议");
		return;
	}
	$.ajax({
		async : false,
		url : "/AppService/setUp/login.xhtml",
		data : {
			"mobile" : mobile,
			"password" : password,
			"rvfcode" : vrfCode_1
		},
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			var returnCode = data.returnCode;
			if (returnCode != null && (returnCode == "9000" || returnCode == "9301")) {
				show_tips(data.returnMsg);
				$("#vrfCode_1").val("");
				getRandomCode();
			} else if (returnCode == "0000") {/* 如果登录成功 跳转 */ 
				pageEventData("01","01");
				/* 登陆成功之后先做埋点再检测如果已做风险测评跳转到财富宝h5页面*/
				//var url = "/AppService/business/fund/wealthTreasure.shtml?pageSourceId=loginId&eventId=event_loginId";
				var url = "/AppService/business/fund/heli.shtml?pageSourceId=loginId&eventId=event_loginId";
				var isCfbFlag = checkHasParam(eventId);
				if(isCfbFlag == true){
					//url = "/AppService/business/fund/wealthTreasure.shtml?pageSourceId=banner&eventId=event_loginId";
					  url = "/AppService/business/fund/heli.shtml?pageSourceId=banner&eventId=event_loginId";
				}
				var flag = prodPlanSwitch(); // 查询财富宝开关是否处于有效期
				var riskLevel = queryIsNeedTest();  // 查询用户是否已做风险测评
				if(flag == true && riskLevel != '0'){ 
					gotoUrl(url);
				}  else {
					$("#mobile").val('');
					$("#password").val('');
					$("#vrfCode_1").val('');
					getRandomCode();
					var requestUrl = data.requestUrl;
					if (requestUrl != null && requestUrl != undefined && requestUrl != "") {
						gotoUrl(requestUrl);
					} else {
						gotoUrl("/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO");
					}
					
				}
				
															
			} else if (returnCode == "USR-A009") {
				show_tips("账户名与密码不匹配，请重新输入");
				$("#vrfCode_1").val("");
				getRandomCode();
			} else if (returnCode == "USR-A005") {
				show_tips("用户已锁定");
				$("#vrfCode_1").val("");
				getRandomCode();
			} else if (returnCode == "USR-A007") {
				show_tips("登录用户不存在，请重新输入");
				$("#vrfCode_1").val("");
				getRandomCode();
			} else {
				show_tips(data.returnMsg);
				$("#vrfCode_1").val("");
				getRandomCode();
			}
			
			
		}
	});
}

function prodPlanSwitch(){
	var nowDate = new Date();//财富宝开关
	var time = nowDate.getFullYear() + "" +((nowDate.getMonth()+1)<10?"0":"")+(nowDate.getMonth()+1)+""+(nowDate.getDate()<10?"0":"")+nowDate.getDate();
	var planDate = queryParamList("SYSTEM","HELIDATE","");
	if(time <= planDate[0].pmco){
		return true;
	} else {
		return false;
	}
}

function queryIsNeedTest(){
	var riskLevel="";
	$.ajax({ 
		async : false,
		url : "/AppService/business/queryIsNeedTest.xhtml",
		data : {},
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			riskLevel = data.riskLevel;
		}
	});
	return riskLevel;
}

function queryParamList(paramType,paramKey,pmValueOne){
    var data = null;
    $.ajax({
        async: !1,
        url: "/AppService/business/queryParamList.xhtml",
        data: {
            paramType :paramType,
            paramKey : paramKey,
            pmValueOne : pmValueOne
        },
        dataType: "json",
        cache: !1,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {
            if(n != null && n.resultCode =="0000"){
                data = n.data;
            }
        }
    });
    return data;
}

function keyLogin(){
	var event = arguments.callee.caller.arguments[0]||window.event;
	if (event.keyCode==13){/*回车键的键值为13*/
		/* 浮动层显现关闭浮动框  */
		if ($('#hint_tips_btn1').is(':visible')) {	
			$('#tips_btn3_btn').focus();
			$('#hint_tips_btn1').hide();
			console.info($('#hint_tips_btn1').is(':visible'));
			if (!$('#hint_tips_btn1').is(':visible')) {
				$('#tips_btn3_btn').focus();
				close_tips('hint_tips_btn1');
			}
			return;
		}
		if($(".hrefpersonal").parent().hasClass("hidden")){
			login();/*调用登录按钮的登录事件*/
		}else{
			branchUserLogin();
		}
	}
}
/*对公用户登录*/
function branchUserLogin(){
	if(getUrlParameter("type") == "abs"){
		var url="/AppService/setUp/absUserLogin.xhtml"
		var branchName = $.trim($("#branchNameAbs").val());
		var branchLicense = $.trim($("#branchLicenseAbs").val());
		var vrfCode_2 = $.trim($("#vrfCode_2Abs").val());
		var type=getUrlParameter("type");
	}else{
		var url="/AppService/setUp/branchUserLogin.xhtml"
		var branchName = $.trim($("#branchName").val());
		var fundAcco = $.trim($("#fundAcco").val());
		var branchLicense = $.trim($("#branchLicense").val());
		var vrfCode_2 = $.trim($("#vrfCode_2").val());
		var type="";
	}
	/* 营业执照名称*/
	if(branchName == null || branchName == ""){
		show_tips("请输入营业执照名称")
		return;
	}
	if(!checkUser(branchName)){
		show_tips("营业执照名称不能包含“！%*”");
		return;
	}
	if(branchName.length > 60){
		show_tips("营业执照名称太长");
		return;
	}
	/* 基金账号*/
	if(type!="abs"){
		if(fundAcco == null || fundAcco == ""){
			show_tips("请输入基金账号")
			return;
		}
		if(!isNumeric(fundAcco)){
			show_tips("基金账号必须为数字");
			return;
		}
		if(fundAcco.length > 20){
			show_tips("基金账号太长");
			return;
		}
	}
	/* 注册号*/
	if(branchLicense == null || branchLicense == ""){
		show_tips("请输入注册号")
		return;
	}
	if(!checkUser(branchLicense)){
		show_tips("注册号不能包含“！%*”");
		return;
	}
	if(branchLicense.length > 30){
		show_tips("注册号太长");
		return;
	}
	/* 验证码*/
	if(vrfCode_2 == null || vrfCode_2 == ""){
		show_tips("请输入验证码。");
		return;
	}
	$.ajax({
		async:false,
		url : url,
		data : {
			"branchName":encodeURI(branchName),
			"fundAcco":fundAcco,
			"branchLicense":encodeURI(branchLicense),
			"vrfCode":vrfCode_2,
			"type":type
		},
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			var returnCode = data.returnCode;
			if(returnCode != null && returnCode == "0000"){
				if(type=="abs"){
					goToURL("/company/companyList.shtml");
				}else{
					goToURL("/company/companyAccountInfo.shtml");	
				}
			}else{
				if(returnCode == "9000"){
					show_tips("请获取验证码");
				}else if(returnCode == "9301"){
					show_tips("验证码错误");
				}else if(returnCode == "QRY-U006"){
					$("#branchName").val("");
					show_tips(data.returnMsg);
				}else if(returnCode == "QRY-U007"){
					$("#fundAcco").val("");
					show_tips(data.returnMsg);
				}else if(returnCode == "QRY-U008"){
					$("#branchLicense").val("");
					show_tips(data.returnMsg);
				}else if(returnCode == "QRY-U001"){
                     show_tips("企业营业执照注册号不正确");
                }else{
					show_tips("网络繁忙，请稍后再试。");
				}
				$("#vrfCode_2").val("");
				$("#rondomCodeImg_2").click();
			}
		}
	});
}
function checkUser(str){
	var pattern = /^[^!%*]+$/;
	return pattern.test(str);
}

function userTypeInit(){
	var type=getUrlParameter("type");
	if(type=="abs"){
		$("#abs").removeClass("hidden");
		$("#personal,#company").addClass("hidden")
	}
}