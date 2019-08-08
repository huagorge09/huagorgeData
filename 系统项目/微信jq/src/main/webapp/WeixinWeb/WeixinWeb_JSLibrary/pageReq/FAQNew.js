var myScroll;
$(document).ready(function(e) {

	$(".header .top-a h2").html("常见问题");
	document.title="常见问题";
	myScroll = new IScroll('#wrapper', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true
	});

	var fundId = getUrlParameter('fundId');
	var period = getUrlParameter('period');
	fundId = fundId.replace('#', "");
	period = period.replace('#', "");
	queryFundInfo(fundId,period);

	myScroll.refresh();
	getUserRequest("FAQ");/* 此处subPath为页面内行为 */
});

document.addEventListener('touchmove', function(e) {e.preventDefault();}, false);

function queryFundInfo(fundId,period) {

	$.ajax({
		async : false,
		url : "/WeixinService/business/queryFundInfo.xhtml",
		data : {
			"fundId" : fundId,
			"period" : period
		},
		dataType : "json",
		type : 'post',
		cache : false,
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == '0000') {
				if (data.quesList != null) {
					var quesList = data.quesList;
					/* 产品常见问题 */
					$.each(quesList,function(index, msg) {
						var temp1 = "<div class='box box-FAQ'><h2><span>"+ msg.wWenti+ "</span></h2><div class='box-text'><p>"+ msg.wDaan+ "</p></div></div>";
						$("#FAQSection").append(temp1);
					});
					
					if ($("#FAQSection").html().trim() != "") {
						/*$("#FAQSection div:last-child").addClass("last");*/
						$("#FAQSection div:last").append("<div class='more' onclick='toBack();'><a class='comeback-pro'>返回查看产品信息</a></div>");
						/*
						 * $("#FAQSection div:last").append("<div
						 * class='more'
						 * onclick='goToURL(\"/WeixinService/business/query/fundInfoNew.shtml?fundId="+fundId+"\");'><a
						 * href='#' class='comeback-pro'>返回查看产品信息</a></div>");
						 */
					}

				} else {
					errorRemark("当前产品不存在");
					window.location.href = "/WeixinService/business/query/fundListNew.shtml";
				}
			} else {
				errorRemark(data.returnMsg);
				/* window.location.href="/WeixinService/business/query/fundListNew.shtml"; */
			}
		}
	});

}