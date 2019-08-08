var apiHost = "" //生产为相对路径
var title="";  //标题
var desc="";  //描述
var host = window.location.protocol + "//" + window.location.host;
var imgUrl=host+"/WeixinWeb/WeixinWeb_Images/mgm/share.jpg"  //转发图标
var link='';   //转发链接
var custserId = localStorage.getItem('custserId'); //顾问
$(function() {
	operatingRecordInfo()
	sharePageStyle()
})

// 数据埋点
function operatingRecordInfo(pageSource, event, pageId, userId) {	
	if(custserId=='' ||custserId==null){
		custserId=getUrlSearchParams('custserId')
	}
	pageId = "wx_mgm_rulesPageId";
	var eventId = "wx_mgm_rulesId";
	//  获取顾问id 传值
	var userId = localStorage.getItem("userId");
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

// 修改分享链接样式
function sharePageStyle(){
	link=host+'/WeixinService/business/mgm/mgmPromotionAct.shtml';
	custName=localStorage.getItem('custName')
	title = custName + "向您推荐招商财富"; //标题
	desc = "招商财富，值得信赖"; //描述
}