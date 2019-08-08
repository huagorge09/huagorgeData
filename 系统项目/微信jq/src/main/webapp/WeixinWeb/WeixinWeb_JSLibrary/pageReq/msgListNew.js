﻿﻿﻿﻿var page = 1;
var totalAmount = -1;
var myScroll;
var pullUpEl, pullUpL;
var Downcount = 0, Upcount = 0;
var loadingStep = 0;
var num = 0;/* 列表当前加载数量 */
var sum = 0;/* 列表总记录数 */
var USERBASEINFO  = null;
var isRealName = true,pageId= 'wx_myMessageId',pageSource='wx_myMessageId';
$(document).ready(function(e) {
	var urlParams = getUrlParams();
	pageSource = urlParams['pageSource']?urlParams['pageSource']:pageSource;
	recordOperation({eventId:'event_wx_MyMsgId',pageId:pageId,pageSource:pageSource});
	$(".header .top-a h2").html("我的消息");
	document.title = "我的消息";
	myScroll = new IScroll('#wrapper', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true,
		click:true,
		preventDefault: false,
		taps:true,
		preventDefaultException: { tagName: /^(INPUT|TEXTAREA|BUTTON|SELECT|A)$/ }
	});
	USERBASEINFO = queryUserinfo();//查询用户
	var result = checkIsRiskLevel();
	//不需要风险测评才需要 展示数据
	if(!!result && !result.flag){
		queryUserMessageList();
		loaded();
		loadActive();
	}
	getUserRequest("msg-list");/* 此处subPath为页面内行为 */
});

function loadActive(){
	var time = getNowTime();
	
	loadYearReport(time);
	loadQuarterReport(time);
}

/**
 * 加载年报活动
 */
function loadYearReport(time){
	var planDate = queryParamList("SYSTEM","PLANDATE","")
	//活动时间内，并且没有关键字信息则弹框
	if(time <= planDate[0].pmco){
		var planMsgKey = queryParamList("SYSTEM","PLANMSGKEY","");
		var title = planMsgKey[0].pmco;//关键字
		
		var isShowTips = queryMsgLikeTitle(title);
		if(isShowTips && isShowTips == 'N'){
			var cookieStr = getCookie(USERBASEINFO.cmfUserId+"msgPlanDate");
			if(cookieStr == null){
				$("#guide").show();
				var expiresDate = getCookieExpiresOneDay();
				//设置cookie
				setCookie(USERBASEINFO.cmfUserId+'msgPlanDate','msgPlanDate',expiresDate);
				guide();
			}
		}
		
	}
}
/**
 * 加载季报活动
 */
function loadQuarterReport(time){
	var quarterDate = queryParamList("SYSTEM","QUARTERDATE","");
	//活动时间内，并且没有关键字信息则弹框
	if(time <= quarterDate[0].pmco){
		var quarterMsgKey = queryParamList("SYSTEM","QUARTERMSGKEY","");
		var title = quarterMsgKey[0].pmco;//关键字
		
		var isShowTips = queryMsgLikeTitle(title);
		if(isShowTips && isShowTips == 'N'){
			var cookieStr = getCookie(USERBASEINFO.cmfUserId+"QUARTERDATE");
			if(cookieStr == null){
				$("#guide").show();
				var expiresDate = getCookieExpiresOneDay();
				//设置cookie
				setCookie(USERBASEINFO.cmfUserId+'QUARTERDATE','QUARTERDATE',expiresDate);
				guide();
			}
		}
		
	}
}

function queryMsgLikeTitle(title){
	   var resultData = "";
	   $.ajax({
	        async: false,
	        url: "/WeixinService/business/queryUserMsgLikeTitle.xhtml",
	        data: {
	        'title' : title
	        },
	        dataType: "json",
	        cache: false,
	        type: "POST",
	        error: function() {
	            show_tips("网络繁忙，请稍后再试。");
	        },
	        success: function(data) {
	        if(!!data){
	            resultData = data.isShowTips;
	        }
	        }
	    });
	   return resultData;
	}

document.addEventListener('touchmove', function(e) {
	e.preventDefault();
}, false);
/* 查询订单列表 */
function queryUserMessageList() {
	$.ajax({
		async : true,
		url : "/WeixinService/business/queryUserMessageList.xhtml",
		data : {
			"page" : page
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				var list = data.list;
				if (list != null && list.length > 0) {
					$(".bulletin").hide();
					var htmls = "";
					$.each(list, function(i, item) {
						htmls += "<div id='" + item.reportId + "' onclick='msgDetail(\"" + item.reportId + "\")' data-fundId='" + item.fundId + "' ";
						htmls += "data-reportTextType='" + item.reportTextType + "' data-reportLinkWX='" + item.reportLinkWX + "'";
						if (item.readFlag != null && parseInt(item.readFlag, 10) == 0) {
							htmls += " class='box-news myinfo'>";
						} else {
							htmls += " class='box-news myinfo act'>";
						}
						htmls += "<dl>";
						htmls += "<dt>" + item.fundLName + "</dt>";
						htmls += "<dd>" + item.date + "</dd>";
						htmls += "</dl>";
						htmls += "</div>";
					});
					if (page == 1) {
						$("#msgList").html(htmls);
					} else {
						$("#msgList").append(htmls);
					}
					page++;
					num = num + list.length;
					if (sum == 0) {
						sum = data.totalAmount;
					}
					myScroll.refresh();
				} else {
					$(".header .top-a h2").html("暂无消息");
					document.title = "暂无消息";
					$(".bulletin").show();
				}
			} else {
				$(".header .top-a h2").html("暂无消息");
				document.title = "暂无消息";
				$(".bulletin").show();
			}
		}
	});
}

/**
 * 指引操作
 */
function guide(){
	$(".know").click(function(){
		$(".guideBox").hide();
	    $(".guideNext").show();
	})
	$(".guideNext").click(function(){
		$("#guide").hide();
	     $(".guideNext").hide();
	})
}
/* 查询消息详情 */
function msgDetail(reportId) {
	addReportReadRecord(reportId);
	var reportTextType = $("#" + reportId).attr("data-reportTextType");
	if (reportTextType != null && reportTextType == "4") {/* 外部url类型，直接跳转到指定url */
		var reportLinkWX = $("#" + reportId).attr("data-reportLinkWX");
		redirectUrl(reportLinkWX);
	} else {/* 进入产品详情页 */
		redirectUrl("/WeixinService/business/query/msgDetailNew.shtml?msgId=" + reportId + "&msgType=" + reportTextType);
	}
}
/* 信批已读保存 */
function addReportReadRecord(reportId) {
	var fundId = $("#" + reportId).attr("data-fundId");
	if(!$("#"+reportId+"").hasClass("act")){
		$.ajax({
			async : true,
			url : "/WeixinService/business/addReportReadRecord.xhtml",
			data : {
				"fundId" : fundId,
				"reportId" : reportId
			},
			dataType : "json",
			cache : false,
			type : "post",
			error : function(textStatus, errorThrown) {
			},
			success : function(data) {

			}
		});
	}
}

function loaded() {

	pullUpEl = $('#pullUp');
	pullUpL = pullUpEl.find('.pullUpLabel');
	pullUpEl['class'] = pullUpEl.attr('class');
	pullUpEl.attr('class', '').hide();
      
	myScroll = new IScroll('#wrapper', {
		probeType : 2,/* probeType：1对性能没有影响。在滚动事件被触发时，滚动轴是不是忙着做它的东西。probeType：2总执行滚动，除了势头，反弹过程中的事件。这类似于原生的onscroll事件。probeType：3发出的滚动事件与到的像素精度。注意，滚动被迫requestAnimationFrame（即：useTransition：假）。 */
		scrollbars : true,/* 有滚动条 */
		mouseWheel : true,/* 允许滑轮滚动 */
		fadeScrollbars : true,/* 滚动时显示滚动条，默认影藏，并且是淡出淡入效果 */
		bounce : true,/* 边界反弹 */
		interactiveScrollbars : true,/* 滚动条可以拖动 */
		shrinkScrollbars : 'scale',/* 当滚动边界之外的滚动条是由少量的收缩。'clip' or 'scale'. */
		click : true,/* 允许点击事件 */
		keyBindings : true,/* 允许使用按键控制 */
		momentum : true
	/* 允许有惯性滑动 */
	});
	myScroll.on('scroll', function() {
		if (sum - num > 0) {
			if (loadingStep == 0 && !pullUpEl.attr('class').match('flip|loading')) {
				if (this.y < (this.maxScrollY - 5)) {
					/* 上拉刷新效果 */
					pullUpEl.attr('class', pullUpEl['class'])
					pullUpEl.show();
					myScroll.refresh();
					pullUpEl.addClass('flip');
					pullUpL.html('上拉加载更多...');
					loadingStep = 1;
				}
			}
		} else {
			if (sum > 10) {
				/* 上拉刷新效果 */
				pullUpEl.attr('class', pullUpEl['class'])
				pullUpEl.show();
				myScroll.refresh();
				pullUpEl.addClass('flip');
				pullUpL.html('小招尽力了，到底了~');
				loadingStep = 0;
				$("#pullUpLabel").html('小招尽力了，到底了~');
			}
		}
	});
	/* 滚动完毕 */
	myScroll.on('scrollEnd', function() {
		if (loadingStep == 1) {
			if (pullUpEl.attr('class').match('flip|loading')) {
				pullUpEl.removeClass('flip').addClass('loading');
				pullUpL.html('Loading...');
				loadingStep = 2;
				pullUpAction();
			}
		}
	});
}
function pullUpAction() {/* 上拉事件 */
	setTimeout(function() {
		queryUserMessageList();
		pullUpEl.removeClass('loading');
		pullUpL.html('上拉加载更多...');
		pullUpEl['class'] = pullUpEl.attr('class');
		pullUpEl.attr('class', '').hide();
		myScroll.refresh();
		loadingStep = 0;
	}, 200);
}

function queryUserinfo(){
	var resultData = {};
	$.ajax({
    	async:false,
        url: "/WeixinService/business/queryUserinfo.xhtml",
        data: "",
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        	errorRemark("网络繁忙，请稍后再试。");  
        }, 
        success : function (data){
        	if(!!data && (data.userType == null || data.userType != "30")){
				isRealName = false;
        	}
			if(!!data && data.returnCode == "0000"){
				var flag = false;
				if(!data.nation || !data.vocCode || !data.taxResidentType || !data.birthDate){
					flag = true;
				}
				resultData.nation = data.nation;
				resultData.nationNM=data.nationNM;
				resultData.province=data.province;
				resultData.provinceNM=data.provinceNM;
				resultData.city=data.city;
				resultData.cityNM=data.cityNM;
				resultData.vocCode=data.vocCode;
				resultData.vocName=data.vocCodeNM;
				resultData.flag=flag;
				resultData.addr=data.addr;
				resultData.taxResidentType = data.taxResidentType;
				resultData.taxResidentTypeNm = data.taxResidentTypeNm;
				resultData.birthDate = data.birthDate;
				resultData.syncInvprtpAlert = data.syncInvprtpAlert;
				resultData.cmfUserId = data.cmfUserId;
			}
        }
    });
	
	return resultData;
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
				if(pageSource == 'wx_model_textProRepotId'){
					$("#riskMsg").html('季报');
				}else{
					$("#riskMsg").html('信披');
				}
				$('#goRisk').unbind("click");
				$('#goRisk').click(function(){
					checkUserBaseInfoIsExist('fundModRiskHouseAddr');
				});
				//展示风险测评框
				showTips("noOperation");
				showTips("riskContent");
				result.flag=true;
			}else if(!!riskEvalDate && !!riskLevel && "0" != riskLevel){
				var nowDate= new Date();
			    var date = new Date(riskEvalDate); 
			    var dataDiff = (nowDate - date) / 86400000;
			    //时间已过 则 提示过期
			    if(dataDiff >= 365){
			    	//过期补充提示
			    	$('#reRisk').unbind("click");
					$('#reRisk').click(function(){
						checkUserBaseInfoIsExist('riskLevel');
					});
					showTips("noOperation");
					showTips("reRiskContent");
					result.flag=true;
			    }
			}
			
			if(!result.flag && !isRealName){
				//未实名
				if(pageSource == 'wx_model_textProRepotId'){
					$("#realNameMsg").html('季报');
				}else{
					$("#realNameMsg").html('信披');
				}
				$('#goAuth').unbind("click");
				$('#goAuth').click(function(){
					redirectUrl('/#/cardinfo')
				});
				showTips("noOperation");
				showTips("realNameContent");
				result.flag=true;
			}
		}
	}); 
    
    if(result.flag){
    	$("#scroller").innerHeight(window.innerHeight);
    }
    return result;
}

//点击适当性提示 后去操作的页面
function checkUserBaseInfoIsExist(target){
	var resultData = USERBASEINFO;
	if("riskLevel" == target){
		addCookie('riskUrl',location.href);
		recordOperation({eventId:'event_wx_myMessageId',pageId:pageId,pageSource:pageSource});
		redirectUrl("/WeixinService/business/user/riskLevelNew.shtml?pageSource="+pageSource);
	}else{
		//选择地址 带入默认值
		var param ="?";
		var nation = resultData.nation;
		var nationNM = resultData.nationNM;
		var province = resultData.province;
		var provinceNM = resultData.provinceNM;
		var city = resultData.city;
		var cityNM = resultData.cityNM;
		var vocCode = resultData.vocCode;
		var vocName = resultData.vocName;
		var address = resultData.addr;
		var taxResidentType = resultData.taxResidentType;
		var taxResidentTypeNm = resultData.taxResidentTypeNm;
		var birthDate = resultData.birthDate;
		param += "nation="+nation+"&nationNM="+encodeURI(nationNM)+"&province="+province+"&provinceNM="+encodeURI(provinceNM)
						+"&city="+city+"&cityNM="+encodeURI(cityNM)+"&address="+encodeURI(address)+"&vocCode="+vocCode+"&vocName="+encodeURI(vocName)
						+"&taxResidentType="+taxResidentType+"&taxResidentTypeNM="+taxResidentTypeNm + "&dateOfBirth="+birthDate
						+"&pageSource="+pageSource;
		recordOperation({eventId:'event_wxMyMessage_expiredId',pageId:pageId,pageSource:pageSource});
		redirectUrl('/WeixinService/business/query/fundModRiskHouseAddrNew.shtml'+param);
	}
}


function showOrHideReportDescContent(){
	//向上三角 triangle-arrow-u.png
	//向下三角 triangle-arrow-d.png
	var target = $("div[class=report-decs-content]");
	var imgTarget = $("a[class=report-decs-title]").find("img");
	//是否隐藏 隐藏 就 展示 反之则反
	if(target.is(":hidden")){
		//展示 并换图标
		target.show();
		imgTarget.attr("src","/WeixinWeb/WeixinWeb_Images/images/triangle-arrow-u.png");
	}else{
		//隐藏 并换图标
		target.hide();
		imgTarget.attr("src","/WeixinWeb/WeixinWeb_Images/images/triangle-arrow-d.png");
	}
}