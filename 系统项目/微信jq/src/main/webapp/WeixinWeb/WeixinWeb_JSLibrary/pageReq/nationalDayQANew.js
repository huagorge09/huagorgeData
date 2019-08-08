var index = 2;
$(document).ready(function(e) {
	$("#flagShow").val("0");
	document.title="迎接祖国67岁生日";
	activityStatisticalLog("02");
});
/*返回开始答题页面*/
function toIndex() {
	goToURL("/WeixinService/activity/nationalDayActivity/nationalDayStartNew.shtml");
}
$("section ul li").click(
		function() {
			$(this).siblings().removeClass("act");
			$(this).addClass("act");
			/* 最后一题 不需要跳转 */
			if (!($(this).parents(".title").attr("id") == "section_05")) {
				window.setTimeout('next(\"'+ $(this).parents(".title").attr("id") + '\")', 500);
			}
		});
function next(_id) {
	$("#flagShow").val("0");
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
	if (len == null || len != 5) {
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
		url : "/WeixinService/activity/nationalDayQA.xhtml",
		data : {
			"answer" : num
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			$("#flagShow").val("1");
			$("#scoreNum").val(data.answer);
			$("#section_05").hide();
			$("#successQA").show();
			$(".score-value em").html(data.answer);
			if (data.answer >= 0 && data.answer <= 40) {
				$("#resultAnswer").html("赶紧自觉加强爱国主义教育!");
			} else if (data.answer > 40 && data.answer <= 60) {
				$("#resultAnswer").html("还不错,合格的爱国主义好青年!");
			} else if (data.answer > 60 && data.answer <= 80) {
				$("#resultAnswer").html("绝对社会主义好青年!");
			} else if (data.answer > 80 && data.answer <= 100) {
				$("#resultAnswer").html("太完美了,发给朋友震撼一下他们吧!");
			} else {
				$("#resultAnswer").html("赶紧自觉加强爱国主义教育!");
			}
			share();
		}
	});
}
/**
 * 显示/隐藏分享遮罩层
 * @param flag
 */
function shareShow(flag){
	activityStatisticalLog("11");
	if(flag){
		$("#shadow").show();
	}else{
		$("#shadow").hide();
	}
}
