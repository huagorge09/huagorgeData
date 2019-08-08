var page = 1;
var totalAmount = -1;
var myScroll;
var pullUpEl, pullUpL;
var Downcount = 0, Upcount = 0;
var loadingStep = 0;
var num = 0;/* 列表当前加载数量 */
var sum = 0;/* 列表总记录数 */

$(document).ready(function(e) {
	$(".header .top-a h2").html("最新公告");
	document.title = "最新公告";
	myScroll = new IScroll('#wrapper', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true
	});
	queryCompanyArticle();
	loaded();
	getUserRequest("affiche-list");/* 此处subPath为页面内行为 */
});

document.addEventListener('touchmove', function(e) {
	e.preventDefault();
}, false);

function queryCompanyArticle() {
	$.ajax({
		async : true,
		url : "/WeixinService/article/queryCompanyArticle.xhtml",
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
					var htmls = "";
					$.each(list, function(i, item) {
						if (item.readFlag != null && item.readFlag == "0") {
							htmls += "<div class='box-news notice'";
							htmls += " onclick='articleDetail(\"" + item.articleId + "\")' ><dl>";
						} else {
							htmls += "<div class='box-news notice act'";
							htmls += " onclick='articleDetailD(\"" + item.articleId + "\")' ><dl>";
						}
						htmls += "<dt>" + item.title + "</dt>";
						htmls += "<dd>" + item.publishDate + "</dd>";
						htmls += "</dl>";
						htmls += "</div>";
					});
					if(page == 1){
						$("#afficheList").html(htmls);
					}else{
						$("#afficheList").append(htmls);
					}
					page++;
					num = num + list.length;
					if (sum == 0) {
						sum = data.count;
					}
				}else{
					$(".header .top-a h2").html("暂无公告");
					document.title = "暂无公告";
					$(".bulletin").show();
				}
			} else {
				$(".header .top-a h2").html("暂无公告");
				document.title = "暂无公告";
				$(".bulletin").show();
			}
			myScroll.refresh();
		}
	});
}
/* 查询消息详情 */
function articleDetail(articleId) {
	addArticleReadRecord(articleId);
	redirectUrl("/WeixinService/article/articleContent.shtml?articleId=" + articleId);
}
/* 查询消息详情 不保存已读 */
function articleDetailD(articleId) {
	redirectUrl("/WeixinService/article/articleContent.shtml?type=2&articleId=" + articleId);
}
/* 信批已读保存 */
function addArticleReadRecord(articleId) {
	$.ajax({
		async : true,
		url : "/WeixinService/article/addArticleReadRecord.xhtml",
		data : {
			"articleId" : articleId
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

	/* 滚动时 */
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
			if(sum>10){
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
		queryCompanyArticle();
		pullUpEl.removeClass('loading');
		pullUpL.html('上拉加载更多...');
		pullUpEl['class'] = pullUpEl.attr('class');
		pullUpEl.attr('class', '').hide();
		myScroll.refresh();
		loadingStep = 0;
	}, 200);
}