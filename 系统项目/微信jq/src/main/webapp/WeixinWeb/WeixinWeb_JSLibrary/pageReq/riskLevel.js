var invprtp = "";
var appst = "";
var flag = true;
var pressButton = false;
var VIEW_TYPE_PRODUCT="product";
var eventId = '',pageId = 'wx_riskLevelId',pageSource = null;
var rOption = {},t_delay;
$(document).ready(function(e) {
	var urlParams = getUrlParams();
	eventId = urlParams['eventId']?urlParams['eventId']:eventId;
	pageSource = urlParams['pageSource']?urlParams['pageSource']:pageSource;
	if(pageSource){
		rOption = {eventId:eventId,pageId:pageId,pageSource:pageSource};
	}
	
	$(".header .top-a h2").html("风险评测");
	document.title="风险评测";
	queryUserName();
	
	/*if(!flag){
		errorRemark("请您完成实名鉴权");
		setTimeout(function(){
			redirectUrl("/WeixinService/business/bank/bankAuth.shtml");
		}, 2000);
	}*/
	
	getUserRequest("risk-level");/* 此处subPath为页面内行为 */
	
	$("#titleBack").click(function(){
		cacelApply();
	});
	
	
	var apptp = GetQueryString("invprtp");
	var appstStr = GetQueryString("appst");
	var birthDate = GetQueryString("birthDate");
	$("input[name='invprtp']").val(apptp);
	$("input[name='appst']").val(appst);
	
	if(!!birthDate){
		$("input[name='birthDate']").val(birthDate);
	}
	//查看历史测评记录
	$("#queryRiskHistory").click(function(){
		window.location.href="/WeixinService/business/user/userRiskHistory.shtml";
	});
	
	var viewType = GetQueryString("viewType");
	
	if(VIEW_TYPE_PRODUCT == viewType){
		
		$("#section_01 a[class=riskevaluation-btn]").attr("href","/WeixinService/business/query/fundList.shtml");
		toRisk();
	}
	
});

function toAuthPage(){
	if(t_delay){
		clearInterval(t_delay);
	}
	redirectUrl('/WeixinService/business/bank/bankAuth.shtml?pageSource='+pageSource);
}

function restRiskLevel(){
	if(t_delay){
		clearInterval(t_delay);
	}
	rOption.eventId = 'event_wx001_reassessId';
	recordOperation(rOption);
	redirectUrl('/WeixinService/business/user/riskLevel.shtml?&pageId='+pageId+'&pageSource='+pageSource);
}
//开始测评
function toRisk() {
	var birthDate = $("input[name='birthDate']").val();
	if(!birthDate){
		redirectUrl('/WeixinService/business/user/updateBirthDate.shtml');
		return;
	}
	//记录日志
	rOption.eventId = 'event_wx_rating';
	recordOperation(rOption);
	
	$("#section_0").hide();
	$("#section_01").show();
	$(".header .top-a h2").html("风险评测");
	document.title="风险评测";
}
/* 第一题 返回到开始页面 */
function toIndex() {
	if(!$("#section_01 li").hasClass("act")){
		$("section section").hide();
		$("#section_0").show();
		$(".header .top-a h2").html("开始评测");
		document.title="开始评测";
		$("section ul li").removeClass("act");
	}
}

/* 做题 选择答案自动下一题 */
$("section ul li").click(
		function() {
			$(this).siblings().removeClass("act");
			$(this).addClass("act");
			
			var dataInput = $(this).attr("data-input");
			if("Y" == dataInput){
				 $(this).parent().find("div[class=risk-option-desc]").show();
			}else{
				$(this).parent().find("div[class=risk-option-desc]").hide();
			}
			
			var sectionId = $(this).parents(".title").attr("id");
			var val = $(this).attr("data-value");
			if(sectionId == "section_14" || sectionId == "section_15" || sectionId == "section_16"){
				if(val == "Y"){
					$("#"+sectionId).hide();
					$("."+sectionId).show();
				}
			}
			
			var mis = 500;
			if("Y" == dataInput){
				mis = 2000;
			}
			/* 最后一题 不需要跳转 */
			if (!($(this).parents(".title").attr("id") == "section_18")) {
				if(sectionId == "section_14" || sectionId == "section_15" || sectionId == "section_16"){
					if(val == "Y"){
						window.setTimeout('next(\"'
								+ $(this).parents(".title").attr("id") + '\")', mis);
					}else{
						window.setTimeout('next2(\"'
								+ $(this).parents(".title").attr("id") + '\")', mis);
					}
				}else{
					window.setTimeout('next(\"'
							+ $(this).parents(".title").attr("id") + '\")', mis);
				}
				
			}
		});

function clickOptionToNextQuestion(element){
	var target = $(element);
	target.parent().find("ul li[class=act]").click();
}

function next(_id) {
	$("#" + _id).hide().next().show().siblings(".title").hide();
}

function next2(_id) {
	$("#" + _id).hide().next().next().show();
}
/* 重做上一题 */
$("section div a.pre_question").click(function() {
	        	var sectionId = $(this).parents(".title").attr("id");
				if(sectionId == "section_15" || sectionId == "section_16" || sectionId == "section_17"){
					$(".title").hide()
					$(this).parents(".title").hide().prev().prev().show().siblings(".title").hide();
				}else{
					$(this).parents(".title").hide().prev().show().siblings(".title").hide();
				}
});


/*传索引值查询对应选中的值--data-value*/
function getThisValDesc(index){
	var value= $("ul li.act").eq(index).attr("data-value");
	if(!!value){
		return  ":"+value;
	}
	return ":";
}

/*传索引值查询对应输入的值-*/
function getThisInputVal(index){
	//var value = $("#common-box-context ul li:eq("+index+") span.act input").val();
	var id = $("ul li.act").eq(index).parent().parent().parent().attr("id");
	var value = $("."+id+" input").val();
	if(!!value){
		return  ":"+value;
	}
	return ":";
}

/*传索引值查询对应选中的值--data-score*/
function getScoreVal(index){
	//return  $(".common-box-context ul li:eq("+index+") span.act").attr("data-score");
	return  $("ul li.act").eq(index).attr("data-score");
}


/* 提交 */
function comit() {
	var invprtp = $("input[name='invprtp']").val();
	var len = $("ul li.act").length;
	if (len == null || len != 18) {
		errorRemark("还有未做题目");
		return;
	}
	var score = 0;
	var num = "";
	var temp = 0;
	for (var i = 0; i < len; i++) {
		var res = $("ul li.act").eq(i).attr("data-score");
		/*score = score + parseFloat($("ul li.act").eq(i).attr("data-score"));
		num = num != "" ? num + ',' + res : num + res;*/
		//答案
		num = num != "" ? num + ',' + parseInt(getScoreVal(i),10) + getThisValDesc(i) + getThisInputVal(i) : parseInt(getScoreVal(i),10) + getThisValDesc(i) + getThisInputVal(i);
		//成绩
		score = parseInt(getScoreVal(i),10) + parseInt(score,10);
	}
	
	//请问您是否具有完全民事行为能力
    var isCompAbility = $("ul li.act").eq(16).attr("data-value");
    //请问您是否没有风险容忍度或者不愿承受任何投资损失
    var isEndureLoss = $("ul li.act").eq(17).attr("data-value");
    
    if("N" == isCompAbility || "Y" == isEndureLoss){
    	score = 0;
    }
	
    var revenue = $("#risk_cover_checkbox").get(0).checked;
	if(!revenue){
		errorRemark("请阅读并勾选声明！");
		return;
	}
	
	
	//提交测评
	$.ajax({
		async : true,
		url : "/WeixinService/business/riskRating.xhtml",
		data : {
			"score" : score,
			"num" : num,
			"apptp" : invprtp
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var returnCode = data.returnCode;
			if (returnCode != null && returnCode == "9001") {
				goToLogin();
			} else if (returnCode != null && returnCode == "9001") {
				goToLogin();
			} else if (returnCode != null && returnCode == "0000") {
				$("#section_18").hide();
				$("#section_20").show();
				$(".header .top-a h2").html("风险评测");
				document.title="风险评测";
				var riskLevelDto = data.riskLevelDto;
				var riskLevel = riskLevelDto.riskLevel;
				var temp = "";
				var tempStr = "";
	        	var tempName = "";
				
	        	//记录日志
	        	rOption.eventId = 'event_wx001_commitQuestionId';
	        	recordOperation(rOption);
	        	if(riskLevel == "1"){
	        		temp = "C1-保守型";
	        		tempStr = "（低风险承受能力）";
	        		tempName = "低风险产品";
	        	}else if(riskLevel == "2"){
	        		temp = "C2-稳健型";
	        		tempStr = "（中低风险承受能力）";
	        		tempName = "中低风险及以下产品";
	        	}else if(riskLevel == "3"){
	        		temp = "C3-平衡型";
	        		tempStr = "（中风险承受能力）";
	        		tempName = "中风险及以下产品";
	        	}else if(riskLevel == "4"){
	        		temp = "C4-成长型";
	        		tempStr = "（中高风险承受能力）";
	        		tempName = "中高风险及以下产品";
	        	}else if(riskLevel == "5"){
	        		temp = "C5-积极型";
	        		tempStr = "（高风险承受能力）";
	        		tempName = "高风险及以下产品";
	        	}else{
	        		temp = "C1-保守型";
	        		tempStr = "（低风险承受能力）";
	        		tempName = "低风险产品";
	        	}
	        	
	        	$(".invtpLevl").html(temp);
	        	$(".riskLevl").html(tempStr);
	        	$(".testDate").html(data.riskEvalDate);
	        	$(".valiDate").html(data.evalValiDate);
	        	$(".riskLevlNm").html(tempName);

				if(!flag){
					var openMgm=queryParamList("SYSTEM","enableIntegral","")[0].pmco
					// 是否开启MGM功能
					if(openMgm=="1"){
						// MGM逻辑
						var mgmFundId=localStorage.getItem("mgmFundId");
						var mgmPeriod=localStorage.getItem("mgmPeriod");
						if(mgmFundId){
							localStorage.removeItem("mgmFundId");
						    localStorage.removeItem("mgmPeriod");
							$("#myTreasure").html("查看产品")
							$("#myTreasure").attr("href",'/WeixinService/business/query/fundInfo.shtml?fundId='+mgmFundId+'&period='+mgmPeriod+'')
						}else{
							//需要实名
							$('#isAuth').show();
							$('#myTreasure').hide();
							t_delay = setInterval (function(){
								redirectUrl("/WeixinService/business/bank/bankAuth.shtml?pageSource="+pageSource);
							}, 5000); 
						}
					}else{
						//需要实名
						$('#isAuth').show();
						$('#myTreasure').hide();
						t_delay = setInterval (function(){
							redirectUrl("/WeixinService/business/bank/bankAuth.shtml?pageSource="+pageSource);
						}, 5000); 
					}
				}
				if(flag){
					var openMgm=queryParamList("SYSTEM","enableIntegral","")[0].pmco
					// 是否开启MGM功能
					if(openMgm=="1"){
						var mgmFundId=localStorage.getItem("mgmFundId");
						var mgmPeriod=localStorage.getItem("mgmPeriod");
						if(mgmFundId){
							localStorage.removeItem("mgmFundId");
						    localStorage.removeItem("mgmPeriod");
							$("#myTreasure").html("查看产品")
							$("#myTreasure").attr("href",'/WeixinService/business/query/fundInfo.shtml?fundId='+mgmFundId+'&period='+mgmPeriod+'')
						}else{
							//如果已经实名则自动跳转到我的财富
							var riskUrl = getCookie("riskUrl");
							if(!riskUrl){
								riskUrl = "/WeixinService/business/user/queryAccount.shtml?pageSource="+pageSource;
							}
							addCookie('riskUrl','');
							t_delay = setInterval (function(){
								redirectUrl(riskUrl);
							}, 3000);
						}
					}else{
						//如果已经实名则自动跳转到我的财富
						var riskUrl = getCookie("riskUrl");
						if(!riskUrl){
							riskUrl = "/WeixinService/business/user/queryAccount.shtml?pageSource="+pageSource;
						}
						addCookie('riskUrl','');
						t_delay = setInterval (function(){
							redirectUrl(riskUrl);
						}, 3000);
					}
				}
			} else {
				errorRemark("风险评测失败<br>请稍后再试");
			}
		}
	});
}


function toFundList(){
	if(t_delay){
		clearInterval(t_delay);
	}
	redirectUrl('/WeixinService/business/user/queryAccount.shtml');
}

/**
 * 格式化年月日
 * @param date
 * @returns
 */
function formatDate(date){
	if(date == null || date == ""){
		return "";
	}
	date = date + "";
	date = unformat1(date);
	return date.substr(0,4)+"年"+date.substr(4,2)+"月"+date.substr(6,2)+"日"+date.substr(8,2)+"时"+date.substr(10,2)+"分";
}

/* 查询用户信息 此处主要查询是否为30用户 */
function queryUserInfo() {
	var url = "/WeixinService/business/queryUserinfo.xhtml";
	$.ajax({
		async : false,
		url : url,
		data : "",
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				
				if (data.userType == null || data.userType != "30") {
					errorRemark("请您完成实名鉴权");
					window.setTimeout("redirectUrl('/WeixinService/business/bank/bankAuth.shtml?pageSource=wx_riskLevelId')",1000);
				}
			} else if (data.returnCode == "8000") {
				errorRemark(data.returnMsg);
				/* 跳转到登陆页面，判断微信和其他ie，分别跳转到不同的登陆页面 */
				window.setTimeout("goToLogin()", 1000);
			} else {
				errorRemark("网络繁忙，请稍后再试");
			}
		}
	});
}


/* 查询用户信息 此处主要查询是否为30用户 */
function queryUserName() {
	var url = "/WeixinService/business/queryUserinfo.xhtml";
	$.ajax({
		async : false,
		url : url,
		data : "",
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				$("input[name='birthDate']").val(data.birthDate);
				if (data.userType == null || data.userType != "30") {
					flag = false;
				}
				invprtp = data.invprtp;
				appst = data.appst;
				queryDateOfBirth(data.birthDate);
			}
		}
	});
}

function loadInvestorInfo(){
	$(".header .top-a h2").html("申请专业投资者");
	document.title="申请专业投资者";
}

/**
 * 继续申请专业投资者
 */
function applyInvestor(){
	queryUserInfo();
	
	$.ajax({
        async: !1,
        url: "/WeixinService/business/appConversionUserInvtp.xhtml",
        data: {
        	apptp :"0"
        },
        dataType: "json",
        cache: !1,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {
            if(n != null && n.resultCode =="0000"){
            	window.location.href="/WeixinService/business/user/applySpecialtyInvSuccess.shtml";
            }else{
            	errorRemark("申请专业投资者异常");
            }
        }
    });
}

/**
 * 放弃申请专业投资者
 */
function cacelApply(){
	window.location.href="/WeixinService/business/user/userInfo.shtml";
}


function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null){
         var val = decodeURI(r[2]);
    	 return  unescape(val);
     }else{
    	 return null;
     }
}

function back(id){
	$("."+id).hide();
	$("#"+id).show();
	$("."+id+" input").val("");
}

function goOn(id){
	var data = $("section[class*="+id+"]").find("input").val();
	if(!data){
		errorRemark("该项为必填项");
		return;
	}
	$("."+id).hide().next().show();
}

function detailTips(){
	window.clearTimeout(t_delay);
	$("#detailText").show();
	pageEventData("04","02");
}

function closeTips(){
	$("#detailText").hide();
	pageEventData("04","03");
	if(!flag){
		$('#isAuth').show();
		$('#myTreasure').hide();
		redirectUrl("/WeixinService/business/bank/bankAuth.shtml?pageSource="+pageSource);		        	
	}
	if(flag){
		//如果已经实名则自动跳转到我的财富
		redirectUrl("/WeixinService/business/user/queryAccount.shtml?pageSource="+pageSource);	
	}
}

//最低年龄限制 民事行为能力 只能选 否
var mayAgeToDay = 16;
function queryDateOfBirth(birthDate){
	//出生日期
	var thisVal = birthDate.replace(/-/g,"");
	var nowDate= new Date();
	var nowYear =nowDate.getFullYear();
	var nowMonth = nowDate.getMonth() + 1;
	var nowDate = nowDate.getDate();
	if(nowMonth < 10){
		nowMonth = "0" + nowMonth;
	}
	if(nowDate < 10){
		nowDate = "0" + nowDate;
	}
	
	var limitAge = (parseInt(nowYear)-parseInt(mayAgeToDay)) + "" + nowMonth + nowDate;
	
	var dataDiff = daysBetween(limitAge,thisVal);
	//时间差
	if(dataDiff < 0){
		$("#section_17 ul li:eq(1)").unbind("click");
//		$("#section_17 ul li:eq(0)").click();
		$("#section_17 ul li:eq(0)").addClass("act");
		$("#section_17 ul li:eq(0)").find("div p").html("年龄不满16岁，不具有完全民事行为能力。<a href='/WeixinService/business/user/userInfo.shtml'>请根据实际情况填写出生日期。</a>");
		$("#section_17 ul li:eq(0)").find("div").show();
	}else{
		$("#section_17 ul li").removeClass("act");
	}
}
