//var apiHost=""  //var apiHost="https://wxtest1.cmwachina.com/"
var apiHost=""  //https://192.168.8.183:18891
$(document).ready(function(e) {
	var u = navigator.userAgent.toLowerCase();
	
	/*if (u.match(/MicroMessenger/i) == "micromessenger") {  微信端 
		$("#wxIEHeader").show();
		$("#otherIEHeader").html("");
	} else {
		$("#otherIEHeader").show();
		$("#wxIEHeader").html("");
	}*/
	
	$("#otherIEHeader").show();
	$("#wxIEHeader").html("");
	
	var wHeight = $(window).height();
	$("#maskDiv").css("minHeight", wHeight);
	$("#maskDiv").css("height", "100%");
	$("#errorDiv").css("height", wHeight);
});


/* 页面跳转 */
function redirectUrl(url) {
	requestWaitDivShow();
	window.location.href = url;
	window.setTimeout('requestWaitDivClose()', 3000);
}

/* 请求等待旋转图片div 显示 遮罩层 */
function requestWaitDivShow() {
	/* 禁用滚动条 */
	if ("undefined" != typeof myScroll) {
		myScroll.disable();
	} else {
		$("html").css("overflow", "hidden");
	}
	$('#maskDiv').show();
}

/* 请求等待旋转图片div 隐藏 */
function requestWaitDivClose() {
	/* 开启滚动条 */
	if ("undefined" != typeof myScroll) {
		myScroll.enable();
	} else {
		$("html").css("overflow", "auto");
	}
	$('#maskDiv').hide();
}

/* 弹窗提示错误信息 */
function errorRemark(msg) {

	$("#errorRemark").html(msg);
	$("#errorDiv").show();
	$("#errorDiv").fadeOut(5000);
}

function errorRemarkHide() {
	$("#errorDiv").hide();
}

function goToLogin() {
	var url = "/WeixinService/H5modules/login.html";
//	var u = navigator.userAgent.toLowerCase();
//	if (u.match(/MicroMessenger/i) == "micromessenger") { /* 微信端 */
//		var url = "/WeixinService/weixinLogin/login.html";
//	}
	redirectUrl(url);
}

function goToRegister(){
	var url = "/WeixinService/H5modules/userRegister.html";
//	var u = navigator.userAgent.toLowerCase();
//	if (u.match(/MicroMessenger/i) == "micromessenger") { /* 微信端 */
//		var url = "/WeixinService/weixinLogin/registerNew.shtml";
//	}
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


/**
 * 数据埋点
 * @param {Object} buried_PageSource
 * @param {Object} buried_PageId
 * @param {Object} buried_EventId
 * @param {Object} buried_GroupId
 */

function operatingRecord (pageSource,event,page,openid,group,unionid,data){
  var parm={
           'data' : data,
            'event' : event,
            'group' :group,
            'openid' : "",
            'pageSource':pageSource,
            'page':page,
            'trackDate':getNowFormatDate(),
            'trackMillis':new Date().getTime(),
            'unionid':unionid,
            'userId':localStorage.getItem("userId")
   }
   $.ajax({
   	    async:false,
        url:apiHost+"/api/wxtrack",
        data:JSON.stringify(parm),
        dataType: "json",
        contentType:"application/json",
        cache: false,
        type: "POST",
        success: function(n) {}
        
    });
}
 /*
  * 获取链接参数
  */
function getUrlSearchParams(_name){
	  var name,value='';
	    var str=window.location.href;
	    var num=str.indexOf("?");
	    str=str.substr(num+1);
	    var arr=str.split("&");
	    for(var i=0;i < arr.length;i++){
	        num=arr[i].indexOf("=");
	        if(num>0){
	            name=arr[i].substring(0,num);
	            if(name.replace(/^\s+|\s+$/g,"") == _name){
	                value=arr[i].substr(num+1);
	                break;
	            }
	        }
	    }
	    return decodeURI(value);
}

/**
 * 获取系统当前时间 
 */
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
            + " " + date.getHours() + seperator2 + date.getMinutes()
            + seperator2 + date.getSeconds();
    return currentdate;
}

/**
 * 添加cookies
 * @param {Object} name
 * @param {Object} value
 * @param {Object} path
 * @param {Object} expiresHours
 */
function addCookie (name,value,path,expiresHours){
     var cookieString= name+"="+escape(value);
            //判断是否设置过期时间
     if(expiresHours>0){
           var date=new Date();
           date.setTime(date.getTime()+expiresHours*3600*1000);
           cookieString=cookieString+"; expires="+date.toGMTString();
     }
     cookieString +=  "; path=" +( path ? path : "/");
     document.cookie=cookieString;
}
/**
 * 获取cookies
 * @param {Object} name
 */
function getCookie(name){
      var strCookie=document.cookie;
      var arrCookie=strCookie.split("; ");
      for(var i=0;i<arrCookie.length;i++){
                var arr=arrCookie[i].split("=");
                if(arr[0]==name)return arr[1];
      }
      return "";
}
/**
 * 刪除cookies
 * @param name
 */
function deleteCookie(name){
	var date=new Date();
	date.setTime(date.getTime()-10000);
	document.cookie=name+"=v; expires="+date.toGMTString();
}