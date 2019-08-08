$(document).ready(function(e) {
	var $title='1'==getUrlParameter("type")?"新闻动态":"最新公告";
	$(".header .top-a h2").html($title);
	document.title=$title;
	myScroll = new IScroll('#wrapper', {
		scrollbars: true,
		mouseWheel: true,
		interactiveScrollbars: true,
		shrinkScrollbars: 'scale',
		fadeScrollbars: true
	});
	queryArticleContent();
	getUserRequest("article-content");/* 此处subPath为页面内行为 */
});
document.addEventListener('touchmove', function(e) {e.preventDefault();}, false);

function queryArticleContent(){
	var articleId = getUrlParameter("articleId");
	$.ajax({
    	async:false,
        url: "/WeixinService/article/queryArticleContent.xhtml",
        data: {
        	"articleId":articleId
        },
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        }, 
        success : function (data){
        	var htmls = "";
        	
			htmls += "<div class='box-news'>";
			htmls += "<h2>";
			htmls += "<span>"+((typeof(data.title) == "undefined")?"":data.title)+"</span>";
			htmls += "<i>"+((typeof(data.publishDate) == "undefined")?"":data.publishDate)+"</i>";
			htmls += "</h2>";
			htmls += "<div class='box-news-detail'>"+((typeof(data.content) == "undefined")?"":data.content);
			htmls += "</div>";
			htmls += "</div>";
        
			$("#articleDetail").html(htmls);
			
        	myScroll.refresh();
        }
    });
}