$(document).ready(function(e){
    var wHeight = $(window).height();
    $("#maskDiv").css("height",wHeight);
    $("#errorDiv").css("height",wHeight);
	
});
	
/* 页面跳转 */
function redirectUrl(url){
	requestWaitDivShow();
	window.location.href = url;
	window.setTimeout('requestWaitDivClose()',3000);
}

/* 请求等待旋转图片div 显示   遮罩层 */
function requestWaitDivShow(){
	/* 禁用滚动条 */
	if("undefined"!= typeof myScroll ){
		myScroll.disable();
	}else{
		$("html").css("overflow","hidden");
	}
	$('#maskDiv').show();
}

/* 请求等待旋转图片div 隐藏 */ 
function requestWaitDivClose(){
	/* 开启滚动条 */
	if("undefined"!= typeof myScroll ){
		myScroll.enable();
	}else{
		$("html").css("overflow","auto");
	}
	$('#maskDiv').hide();
}

/* 弹窗提示错误信息 */
function errorRemark(msg){
	
	$("#errorRemark").html(msg);
	$("#errorDiv").show();
	$("#errorDiv").fadeOut(5000);
}

function errorRemarkHide(){
	$("#errorDiv").hide();
}

function goToLogin(){
	var url = "/WeixinService/otherIELogin/otherIELoginNew.shtml";
	var u = navigator.userAgent.toLowerCase();  
    if(u.match(/MicroMessenger/i)=="micromessenger"){  /* 微信端 */
    	var url = "/WeixinService/weixinLogin/loginNew.shtml";
    }
	redirectUrl(url);
}
function goToRegister(){
	var url = "/WeixinService/otherIELogin/otherIERegisterNew.shtml";
	var u = navigator.userAgent.toLowerCase();
	if (u.match(/MicroMessenger/i) == "micromessenger") { /* 微信端 */
		var url = "/WeixinService/weixinLogin/registerNew.shtml";
	}
	redirectUrl(url);
}
/* 显示弹窗提示信息 */
function showTips(_id) {
	$("#" + _id).show();
}
/* 关闭弹窗提示信息 */
function closeTips(_id) {
	$("#" + _id).hide();
}
function toBack(){
	var local = document.referrer;
	if(local=="" || local==null){
		redirectUrl('/WeixinService/indexNew.shtml');
	}else{
		history.go(-1);
	}
}
dataStatistics()
function dataStatistics(){
	var url=location.href;
	if(url.indexOf("cmwachina")>-1){
		var _hmt = _hmt || [];
		(function() {
		  var hm = document.createElement("script");
		  hm.src = "https://hm.baidu.com/hm.js?afce23bbabb14d025b1571144242dded";
		  var s = document.getElementsByTagName("script")[0]; 
		  s.parentNode.insertBefore(hm, s);
		})();
	}
}
