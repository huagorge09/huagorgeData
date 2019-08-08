var apiHost = "" //生产为相对路径
var pageId="wx_mgmH5_rulesPageId";
$(function() {
	operatingRecordInfo()
})

// 数据埋点
function operatingRecordInfo() {
	var eventId ="wx_mgmH5_rulesId";
	var custserId = localStorage.getItem('custserId')//顾问id
	var userId = localStorage.getItem("cmfUserId");//用户id
	var parm = {
		'event': eventId,
		'pageSource': custserId,
		'page': pageId,
		'userId': userId,
		'trackDate': getNowFormatDate(),
		'trackMillis': new Date().getTime(),
	}
	$.ajax({
		url: apiHost + "/api/wxtrack",
		data: JSON.stringify(parm),
		dataType: "json",
		contentType: "application/json",
		cache: false,
		type: "POST",
		success: function(n) {}

	});
}

/*
 * 获取链接参数
 */
function getUrlSearchParams(_name) {
	var name, value = '';
	var str = window.location.href;
	var num = str.indexOf("?");
	str = str.substr(num + 1);
	var arr = str.split("&");
	for (var i = 0; i < arr.length; i++) {
		num = arr[i].indexOf("=");
		if (num > 0) {
			name = arr[i].substring(0, num);
			if (name.replace(/^\s+|\s+$/g, "") == _name) {
				value = arr[i].substr(num + 1);
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
	var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate +
		" " + date.getHours() + seperator2 + date.getMinutes() +
		seperator2 + date.getSeconds();
	return currentdate;
}
