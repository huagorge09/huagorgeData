$(document).ready(function(e) {
	getUserRequest("pc_newsList_01");
	document.title = "新闻动态_招商财富 公司新闻、公司动态、新闻资讯、招商财富动态";
	$(".nav.fr ul li a").removeClass("current").eq(2).addClass("current");
	//noticeList();
	queryNewsArticlNum("1");
	queryCompanyArticleNum("1");
    queryOriginaArticleNum("1")
    var type=getUrlParameter("type")
    if(type=="2"){
    	newsList()
    }else if(type=="3"){
    	originalArticleList()
    }else{
    	noticeList()
    }
});
/* 搜索 */
function searchByName(){
	$(".information-left ul li").each(function(){
		if($(this).find("a").hasClass("act")){
			var index=$(this).index();
			if(index=="0"){
				queryNewsArticl("1");
			}else if(index=="1"){
				queryCompanyArticle("1");
			}else{
				queryNewsOriginalArticle("1");
			}
		}
	})
}
/* 公司公告/最新公告列表切换 */
function noticeList(){
	getUserRequest("pc_newsList_01");
	if($(".information-left ul li.newnotice a").hasClass("act")){
		return;
	}
	$("#name").val("");
	$(".information-left ul li a").removeClass("act");
	$(".information-left ul li.newnotice a").addClass("act");
	var liaLeft = $(".information-left ul li.newnotice a").position().top;
	$(".information-right .information-nav-icon").css("top",liaLeft+23);
	queryNewsArticl("1");
}
/* 新闻动态列表切换 */
function newsList(){
	getUserRequest("pc_newsList_02");
	if($(".information-left ul li.newsinfo a").hasClass("act")){
		return;
	}
	$("#name").val("");
	$(".information-left ul li a").removeClass("act");
	$(".information-left ul li.newsinfo a").addClass("act");
	var liaLeft = $(".information-left ul li.newsinfo a").position().top;
	$(".information-right .information-nav-icon").css("top",liaLeft+23);
	queryCompanyArticle("1");
}
/*原创文章切换*/
function originalArticleList(){
	getUserRequest("pc_newsList_03");
	if($(".information-left ul li.newsoriginal a").hasClass("act")){
		return;
	}
	$("#name").val("");
	$(".information-left ul li a").removeClass("act");
	$(".information-left ul li.newsoriginal a").addClass("act");
	var liaLeft = $(".information-left ul li.newsoriginal a").position().top;
	$(".information-right .information-nav-icon").css("top",liaLeft+23);
    queryNewsOriginalArticle("1");
}


/* 公司公告/最新公告列表查询 */
function queryNewsArticlNum(pages){
	var name = $("#name").val();
	var page = returnPage(pages);
	$.ajax({
		async : true,
		url : "/AppService/article/queryNewsArticle.xhtml",
		data : {
			"page" : page,
			"name" : encodeURI(name)
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			if(data.list == null || data.list.length <= 0){
				$(".newnotice").addClass("none");
				return;
			}
		}
	});
}
/* 新闻动态列表查询 */
function queryCompanyArticleNum(pages){
	var name = $("#name").val();
	var page = returnPage(pages);
	$.ajax({
		async : true,
		url : "/AppService/article/queryCompanyArticle.xhtml",
		data : {
			"page" : page,
			"name" : encodeURI(name)
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			if(data.list == null || data.list.length <= 0){
				$(".newsinfo").addClass("none");
				return;
			}
		}
	});
}
/* 新闻动态列表查询 */
function queryOriginaArticleNum(pages){
    var name = $("#name").val();
    var page = returnPage(pages);
    $.ajax({
        async : true,
        url : "/AppService/article/queryOriginalArticleAndCount.xhtml",
        data : {
            "page" : page,
            "name" : encodeURI(name)
        },
        dataType : "json",
        cache : false,
        type : "post",
        error : function(textStatus, errorThrown) {
        },
        success : function(data) {
            if(data.list == null || data.list.length <= 0){
                $(".newsoriginal").addClass("none");
                return;
            }
        }
    });
}
/* 公司公告/最新公告列表查询 */
function queryNewsArticl(pages){
	var name = $("#name").val();
	var page = returnPage(pages);
	$.ajax({
		async : true,
		url : "/AppService/article/queryNewsArticle.xhtml",
		data : {
			"page" : page,
			"name" : encodeURI(name)
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var htmls = "";
			var htmls2 = "";
			if(data.list != null && data.list.length > 0){
				$.each(data.list,function(i, item){
					htmls += "<dl>";
		            htmls += "<h3 class='cursor' onclick='openToUrl(\"/AppService/article/newsDetail.shtml?articleId="+item.articleId+"&type=company\")'>"+item.title+"</h3>";
		            htmls += "<dt>"+formatDate(item.publishDate)+"</dt>";
		            htmls += "<dd>"+item.brief+"</dd>";
		            htmls += "</dl>";
				})
				var page = data.currentPage;
				var count = data.count;
				var maxPages = data.maxPages;
				$("#page").val(page);
				$("#maxPages").val(maxPages);
	
				htmls2 += "<a href='javascript:queryNewsArticl(\""+(page-1)+"\")' class='pre'></a>";
				if(page == 1){
					htmls2 += "<a class='act' href='javascript:queryNewsArticl(\"1\")'>1</a>";
				}else{
					htmls2 += "<a href='javascript:queryNewsArticl(\"1\")'>1</a>";
				}
	
				if(page > 3){/* 左边加...*/
					htmls2 += "<a class='omit'></a>";
				}
	
				if((page-1) >1){/* 上一页*/
					htmls2 += "<a href='javascript:queryNewsArticl(\""+(page-1)+"\")'>"+(page-1)+"</a>";
				}
	
				if(page != 1 && page != maxPages){/* 当前页*/
					htmls2 += "<a class='act' href='javascript:queryNewsArticl(\""+(page)+"\")'>"+(page)+"</a>";
				}
	
				if(page+1 < maxPages){/*下一页*/
					htmls2 += "<a href='javascript:queryNewsArticl(\""+(page+1)+"\")'>"+(page+1)+"</a>";
				}
	
				if((maxPages - page) > 2){/* 右边加...*/
					htmls2 += "<a class='omit'></a>";
				}
	
				if(page != 1){
					if(maxPages == page){
						htmls2 += "<a class='act' href='javascript:queryNewsArticl(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}else if(maxPages > page){
						htmls2 += "<a href='javascript:queryNewsArticl(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}else{
					if(maxPages == page){
	
					}else if(maxPages > page){
						htmls2 += "<a href='javascript:queryNewsArticl(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}
				htmls2 += "<a href='javascript:queryNewsArticl(\""+(page+1)+"\")' class='next'></a>";
				htmls2 += "</div>";
	
				$("#list").html(htmls);
				$(".page").html(htmls2);
				$(".listNone").hide()
			}else{
				$("#list").html(htmls);
			    $(".page").html(htmls2);
			    $(".listNone").show()
//				if($("#name").val()==""){
//				   $(".newnotice").addClass("none");
//				}else{
//				   $("#name").val("")
//				}
//				noticeList()
//				return;
			}
			
		}
	});
}
/* 新闻动态列表查询 */
function queryCompanyArticle(pages){
	var name = $("#name").val();
	var page = returnPage(pages);
	$.ajax({
		async : true,
		url : "/AppService/article/queryCompanyArticle.xhtml",
		data : {
			"page" : page,
			"name" : encodeURI(name)
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var htmls = "";
			var htmls2 = "";
			if(data.list != null && data.list.length > 0){
				$.each(data.list,function(i, item){
					htmls += "<dl>";
		            htmls += "<h3 class='cursor' onclick='openToUrl(\"/AppService/article/newsDetail.shtml?articleId="+item.articleId+"&type=news\")'>"+item.title+"</h3>";
		            htmls += "<dt>"+formatDate(item.publishDate)+"</dt>";
		            htmls += "<dd>"+item.brief+"</dd>";
		            htmls += "</dl>";
				})
				var page = data.currentPage;
			var count = data.count;
			var maxPages = data.maxPages;
			$("#page").val(page);
			$("#maxPages").val(maxPages);

			htmls2 += "<a href='javascript:queryCompanyArticle(\""+(page-1)+"\")' class='pre'></a>";
			if(page == 1){
				htmls2 += "<a class='act' href='javascript:queryCompanyArticle(\"1\")'>1</a>";
			}else{
				htmls2 += "<a href='javascript:queryCompanyArticle(\"1\")'>1</a>";
			}

			if(page > 3){/* 左边加...*/
				htmls2 += "<a class='omit'></a>";
			}

			if((page-1) >1){/* 上一页*/
				htmls2 += "<a href='javascript:queryCompanyArticle(\""+(page-1)+"\")'>"+(page-1)+"</a>";
			}

			if(page != 1 && page != maxPages){/* 当前页*/
				htmls2 += "<a class='act' href='javascript:queryCompanyArticle(\""+(page)+"\")'>"+(page)+"</a>";
			}

			if(page+1 < maxPages){/*下一页*/
				htmls2 += "<a href='javascript:queryCompanyArticle(\""+(page+1)+"\")'>"+(page+1)+"</a>";
			}

			if((maxPages - page) > 2){/* 右边加...*/
				htmls2 += "<a class='omit'></a>";
			}

			if(page != 1){
				if(maxPages == page){
					htmls2 += "<a class='act' href='javascript:queryCompanyArticle(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}else if(maxPages > page){
					htmls2 += "<a href='javascript:queryCompanyArticle(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}
			}else{
				if(maxPages == page){

				}else if(maxPages > page){
					htmls2 += "<a href='javascript:queryCompanyArticle(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}
			}
			htmls2 += "<a href='javascript:queryCompanyArticle(\""+(page+1)+"\")' class='next'></a>";
			htmls2 += "</div>";

			$("#list").html(htmls);
			$(".page").html(htmls2);
			$(".listNone").hide()
			}else{
				$("#list").html(htmls);
			    $(".page").html(htmls2);
			    $(".listNone").show()
//				if($("#name").val()==""){
//					$(".newsinfo").addClass("none");
//				}else{
//				    $("#name").val("")
//				}
//				newsList();
//				return;
			}

			
		}
	});
}
/* 原创文章列表查询 */
function queryNewsOriginalArticle(pages){
	var name = $("#name").val();
	var page = returnPage(pages);
	$.ajax({
		async : true,
		url : "/AppService/article/queryOriginalArticleAndCount.xhtml",
		data : {
			"page" : page,
			"name" : encodeURI(name)
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var htmls = "";
			var htmls2 = "";
			if(data.list != null && data.list.length > 0){
				$.each(data.list,function(i, item){
					htmls += "<dl>";
		            htmls += "<h3 class='cursor' onclick='openToUrl(\"/AppService/article/newsDetail.shtml?articleId="+item.articleId+"&type=original\")'>"+item.title+"</h3>";
		            htmls += "<dt>"+formatDate(item.publishDate)+"</dt>";
		            htmls += "<dd>"+item.brief+"</dd>";
		            htmls += "</dl>";
				})
				var page = data.currentPage;
			var count = data.count;
			var maxPages = data.maxPages;
			$("#page").val(page);
			$("#maxPages").val(maxPages);

			htmls2 += "<a href='javascript:queryNewsOriginalArticle(\""+(page-1)+"\")' class='pre'></a>";
			if(page == 1){
				htmls2 += "<a class='act' href='javascript:queryNewsOriginalArticle(\"1\")'>1</a>";
			}else{
				htmls2 += "<a href='javascript:queryNewsOriginalArticle(\"1\")'>1</a>";
			}

			if(page > 3){/* 左边加...*/
				htmls2 += "<a class='omit'></a>";
			}

			if((page-1) >1){/* 上一页*/
				htmls2 += "<a href='javascript:queryNewsOriginalArticle(\""+(page-1)+"\")'>"+(page-1)+"</a>";
			}

			if(page != 1 && page != maxPages){/* 当前页*/
				htmls2 += "<a class='act' href='javascript:queryNewsOriginalArticle(\""+(page)+"\")'>"+(page)+"</a>";
			}

			if(page+1 < maxPages){/*下一页*/
				htmls2 += "<a href='javascript:queryNewsOriginalArticle(\""+(page+1)+"\")'>"+(page+1)+"</a>";
			}

			if((maxPages - page) > 2){/* 右边加...*/
				htmls2 += "<a class='omit'></a>";
			}

			if(page != 1){
				if(maxPages == page){
					htmls2 += "<a class='act' href='javascript:queryNewsOriginalArticle(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}else if(maxPages > page){
					htmls2 += "<a href='javascript:queryNewsOriginalArticle(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}
			}else{
				if(maxPages == page){
				}else if(maxPages > page){
					htmls2 += "<a href='javascript:queryNewsOriginalArticle(\""+(maxPages)+"\")'>"+maxPages+"</a>";}
			}
			htmls2 += "<a href='javascript:queryNewsOriginalArticle(\""+(page+1)+"\")' class='next'></a>";
			htmls2 += "</div>";
			$("#list").html(htmls);
			$(".page").html(htmls2);
			$(".listNone").hide()
			}else{
				$("#list").html(htmls);
			    $(".page").html(htmls2);
			    $(".listNone").show()
//				if($("#name").val()==""){
//					$(".newsoriginal").addClass("none");
//				}else{
//				   $("#name").val("")
//				}
//				originalArticleList()
//				return;
			}

			
		}
	});
}




function returnPage(pages){
	var page;
	var maxPages = $("#maxPages").val();

	if(parseInt(pages,10) >= parseInt(maxPages,10)){
		page = maxPages;
	}else if(parseInt(pages,10) <= 0){
		page = "1";
	}else{
		page = pages;
	}
	return page;
}
