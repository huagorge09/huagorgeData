﻿var myScroll;
var refreshCount = 3;
$(document).ready(function(e) {
	getUserRequest("index");/* 此处subPath为页面内行为 */
	$(".foot .nav-a ul li a").removeClass("act");
	$(".foot .nav-a ul li:eq(0) a").addClass("act");
	$(".header .top-a h2").html("首页");
	document.title="首页";
	checkUserIsLogin();
});

function prodocType(type){
	redirectUrl('/WeixinService/business/query/fundList.shtml?item='+type+'&eventId=event_wx_InvestmentTypeId&pageSource=wx_indexId');
}


document.addEventListener('touchmove', function(e) {e.preventDefault();}, false);

function loaded(){
	$("#menu_f1,#menu_f2,#menu_f3").removeClass("act");
	$("#menu_f1").addClass("act");
	$("#menu_f1").addClass("active");
	myScroll = new IScroll('#wrapper', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true
	});
    
    forRefensh();
}
/* 页面初始加载，查询产品列表后，定时刷新滚动条，时间3秒，每1秒刷新1次 */
function forRefensh() {
	if (refreshCount > 0) {
		refreshCount--;
		myScroll.refresh();
		window.setTimeout("forRefensh()", 1000);
	} else {
		return;
	}
}

function checkUserIsLogin() {
	var urlVal = "/WeixinService/setUp/checkUserIsLogin.xhtml";
	$.ajax({
		async : false,
		url : urlVal,
		type : "post",
		dataType : 'json',
		data : {},
		error : function() {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data, textStatus) {
			if(data.returnCode != null && data.returnCode == "0000"){
				$("#loginType_01").hide();
				$("#loginType_02").show();
			}else{
				$("#loginType_01").show();
				$("#loginType_02").hide();
			
			}
		}
	});
}
/* 退出登录 */
function exit(){
	$.ajax({
    	async:false,
        url: "/WeixinService/business/exit.xhtml",
        data: "",
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
            
        }, 
        success : function (data){
        	$("#loginType_01").show();
			$("#loginType_02").hide();
			localStorage.clear();
        	redirectUrl(data.url);
        }
    });
}
