var index = 2;
$(document).ready(function(e) {
	document.title="七夕活动-权益问答";
	activityStatisticalLog("02");
});
/*返回开始答题页面*/
function toIndex() {
	goToURL("/WeixinService/activity/magpieFestivalActivity/leadStart.shtml");
}
$("section ul li").click(
		function() {
			$(this).siblings().removeClass("act");
			$(this).addClass("act");
			/* 最后一题 不需要跳转 */
			if (!($(this).parents(".title").attr("id") == "section_08")) {
				window.setTimeout('next(\"'+ $(this).parents(".title").attr("id") + '\")', 500);
			}
		});
function next(_id) {
	index = index+1;
	activityStatisticalLog("0"+index);/* 此处subPath为页面内行为 */
	$("#" + _id).hide().next().show();
}
/* 重做上一题 */
$("section div a.pre_question").click(function() {
	index = index-1;
	activityStatisticalLog("0"+index);/* 此处subPath为页面内行为 */
	$(this).parents(".title").hide().prev().show();
});
/* 提交 */
function comit() {
	index = 10;
	activityStatisticalLog(index);/* 此处subPath为页面内行为 */
	var len = $("ul li.act").length;
	if (len == null || len != 8) {
		errorRemark("还有未做题目");
		return;
	}
	var score = 0;
	var num = "";
	var temp = 0;
	for (var i = 0; i < len; i++) {
		var res = $("ul li.act").eq(i).attr("data-score");
		num = num != "" ? num + ',' + res : num + res;
	}
	$.ajax({
		async : true,
		url : "/WeixinService/activity/magpieFestivalQA.xhtml",
		data : {
			"answer" : num
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			$("#section_08").hide();
			$("#successQA").show();
			$(".score-value em").html(data.answer);
		}
	});
}
/*领取电影券*/
function lead(){
	$.ajax({
		async : true,
		url : "/WeixinService/activity/magpieFestivalDrawTicket.xhtml",
		data : {},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			if(data.returnCode=="0000"){
        		/*领取成功,电影券,以逗号隔开*/
				index =11;
				activityStatisticalLog(index);/* 此处subPath为页面内行为 */
        		redirectUrl("/WeixinService/activity/magpieFestivalActivity/leadSuccess.shtml");
        	}else if(data.returnCode=="0030"){
        		/*奖品已领完*/
        		index =12;
        		activityStatisticalLog(index);/* 此处subPath为页面内行为 */
        		redirectUrl("/WeixinService/activity/magpieFestivalActivity/leadEnd.shtml");
        	}else if(data.returnCode=="0020"){
        		/*已经领取过*/
        		index =13;
        		activityStatisticalLog(index);/* 此处subPath为页面内行为 */
				redirectUrl("/WeixinService/activity/magpieFestivalActivity/leadPast.shtml");
        	}else if(data.returnCode=="9998"){
        		errorRemark("关键参数为空");
        	}else if(data.returnCode=="9999"){
        		errorRemark(data.returnMsg);
        	}else{
        		errorRemark("网络繁忙，请稍后再试!");
        	}
		}
	});
}