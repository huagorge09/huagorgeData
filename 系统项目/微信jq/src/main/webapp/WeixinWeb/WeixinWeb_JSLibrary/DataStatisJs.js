﻿//js版本时间戳 避免js缓存
var jsversion = new Date().getTime();

function getUserRequest(subPath){
	var curTime = getNowFmtDate();//当前时间
	var curTimeStamp = Date.parse(new Date());//时间戳
	var path = location.pathname;//页面地址
	var subPath = subPath;//页面内行为
	var urlParameter = "";//页面地址后的参数
	var userId = $("#cmf_userId").val();//cmfuserId
	var channel = "03";
	var type = "11";
	var others = "";
    var reqSource = document.referrer;
    	
	if(location.href.split("?").length > 1){
		urlParameter = location.href.split("?")[1];
	}
	
	var ua = navigator.userAgent.toLowerCase();  
	if(ua.match(/MicroMessenger/i)=="micromessenger") {  
		others = "0301";//微信浏览器访问
	} else {  
		others = "0302";//其他浏览器访问
	}
	
	$.ajax({
		async:true,
		url: "/WeixinService/log/getUserRequest.stat",
		dataType: "text",
        type:"POST",
		data: {
			"curTime":curTime,
			"curTimeStamp":curTimeStamp,
			"path":path,
			"subPath":subPath,
			"urlParameter":urlParameter,
			"userId":userId,
			"channel":channel,
			"type":type,
			"others":others,
			"reqSource":reqSource
		},
		cache: false,
		error : function(textStatus, errorThrown) {  
 			
		}, 
		success : function (data){

		}
	}); 
	
}


function activityStatisticalLog(pageIndex){
	var activityId = $("#eventId").val();//活动页面id
	var url = encodeURI(location.href);
	
	if(activityId==null || activityId=='' || pageIndex==null || pageIndex==''){
		return false;
	}
	
	$.ajax({
		async:false,
		url: "/WeixinService/log/activityAccessStatistical.stat",
		dataType: "json",
        type:"POST",
		data: {
			"activityId":activityId,
			"pageIndex":pageIndex,
			"url":url
		},
		cache: false,
		error : function(textStatus, errorThrown) {  
 			
		}, 
		success : function (data){

		}
	}); 
}


/**
 * 获取当前的时间并返回
 * 
 * @returns {String} YYYYMMDD-HHMISS
 */
function getNowFmtDate(){
	var d = new Date();
	var month = d.getMonth()+1;
	var date = d.getDate();
	var hours = d.getHours();
	var minutes = d.getMinutes();
	var second = d.getSeconds();
	
	if(parseInt(month) >= 0 && parseInt(month) <= 9){
		month = "0"+month;
	}
	if(parseInt(date) >= 0 && parseInt(date) <= 9){
		date = "0"+date;
	}
	
	if(parseInt(hours) >= 1 && parseInt(hours) <= 9){
		hours = "0"+hours;
	}else if(parseInt(hours) == 0){
		hours = "00";
	}
	if(parseInt(minutes) >= 1 && parseInt(minutes) <= 9){
		minutes = "0"+minutes;
	}else if(parseInt(minutes) == 0){
		minutes = "00";
	}
	if(parseInt(second) >= 1 && parseInt(second) <= 9){
		second = "0"+second;
	}else if(parseInt(second) == 0){
		second = "00";
	}
	
	var currDateTime = ""+d.getFullYear()+month+date+"-"+hours+minutes+second;
	
	return currDateTime;
}