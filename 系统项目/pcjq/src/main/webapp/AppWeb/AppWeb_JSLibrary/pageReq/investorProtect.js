var fileType,cataLog,page,name;
$(document).ready(function(e) {
	getUserRequest("pc_newsList_01");
	document.title="投资者教育_招商财富 都市精英的私人银行、高净值人士专业资产管理平台"
	$(".nav.fr ul li a").removeClass("current").eq(4).addClass("current");
	$(".newnotice").eq(0).find("a").addClass("act").parents("li").siblings().find("a").removeClass("act");
	var type=getUrlParameter("type");
	if(type==2){
	    $("#list,.page").html("");
		name="";
		$("#name").val("");
		$(".newnotice").eq(1).find("a").addClass("act").parents("li").siblings().find("a").removeClass("act");
		fileType=$(".newnotice").eq(1).find("a").attr("fileType");
		queryInvertorFile("1");
		triangleSwitch()
	}else if(type==3){
		$("#list,.page").html("");
		name="";
		$("#name").val("");
		$(".newnotice").eq(2).find("a").addClass("act").parents("li").siblings().find("a").removeClass("act");
		cataLog=$(".newnotice").eq(2).find("a").attr("cataLog");
		queryInvertorArticle("1");
		triangleSwitch()
	}else{
		queryInvertorFile("1");
	}
});



function investorProtect(ele){
	$("#list,.page").html("");
	name="";
	$("#name").val("");
	$(ele).addClass("act").parents("li").siblings().find("a").removeClass("act");
	cataLog=$(ele).attr("cataLog");
	queryInvertorArticle("1");
	triangleSwitch()
}
function investorProtectFile(ele){
	$("#list,.page").html("");
	name="";
	$("#name").val("");
	$(ele).addClass("act").parents("li").siblings().find("a").removeClass("act");
	fileType=$(ele).attr("fileType");
	queryInvertorFile("1");
	triangleSwitch()
}

function triangleSwitch(){
	if($(".newnotice").eq(0).find("a").hasClass("act")){
		$(".information-nav-icon").css("top","23px")
	}else if($(".newnotice").eq(1).find("a").hasClass("act")){
		$(".information-nav-icon").css("top","83px")
	}else{
		$(".information-nav-icon").css("top","142px")
	}
}

/* 搜索 */
function searchByName(){
	$("#list,.page").html("");
	if($(".information-left ul li").eq(0).find("a").hasClass("act")){
		fileType=$(".information-left ul li").eq(0).find("a").attr("filetype")
		queryInvertorFile("1")
	}else if($(".information-left ul li").eq(1).find("a").hasClass("act")){
		fileType=$(".information-left ul li").eq(1).find("a").attr("filetype")
		queryInvertorFile("1")
	}else{
		queryInvertorArticle("1");
	}
}

/**
 * 查询行业法规跟反洗钱
 * @param {Object} pages
 */
function queryInvertorFile(pages){
	name = $("#name").val();
	page = returnPage(pages);
	$.ajax({
		async : true,
		url : "/AppService/article/queryInvestorArticleByFileType.xhtml",
		data : {
			"page" : page,
			"name" : encodeURI(name),
			"fileType":fileType?fileType:4
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
					if(item.fileDownload.indexOf("pdf")>-1){
						htmls += "<h3 class='cursor'><a  target=_blank href="+item.fileDownload+">"+item.fileName+"</a></h3>";
					} else if (!item.fileDownload){
						htmls += "<h3 class='cursor'><a href='javascript:show_tips(\"该文件不存在\")' >"+item.fileName+"</a></h3>";
					} else{
						htmls += "<h3 class='cursor'><a href="+item.fileDownload+" >"+item.fileName+"</a></h3>";
					}
		            htmls += "</dl>";
				})
			}else{
				$(".newsinfo").addClass("none");
				//noticeList();
				return;
			}
			var page = data.currentPage;
			var count = data.count;
			var maxPages = data.maxPages;
			$("#page").val(page);
			$("#maxPages").val(maxPages);
			
			htmls2 += "<a href='javascript:queryInvertorFile(\""+(page-1)+"\")' class='pre'></a>";
			if(page == 1){
				htmls2 += "<a class='act' href='javascript:queryInvertorFile(\"1\")'>1</a>";
			}else{
				htmls2 += "<a href='javascript:queryInvertorFile(\"1\")'>1</a>";
			}
			
			if(page > 3){/* 左边加...*/
				htmls2 += "<a class='omit'></a>";
			}
			
			if((page-1) >1){/* 上一页*/
				htmls2 += "<a href='javascript:queryInvertorFile(\""+(page-1)+"\")'>"+(page-1)+"</a>";
			}
			
			if(page != 1 && page != maxPages){/* 当前页*/
				htmls2 += "<a class='act' href='javascript:queryInvertorFile(\""+(page)+"\")'>"+(page)+"</a>";
			}
			
			if(page+1 < maxPages){/*下一页*/
				htmls2 += "<a href='javascript:queryInvertorFile(\""+(page+1)+"\")'>"+(page+1)+"</a>";
			}
			
			if((maxPages - page) > 2){/* 右边加...*/
				htmls2 += "<a class='omit'></a>";
			}
			
			if(page != 1){
				if(maxPages == page){
					htmls2 += "<a class='act' href='javascript:queryInvertorFile(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}else if(maxPages > page){
					htmls2 += "<a href='javascript:queryInvertorFile(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}
			}else{
				if(maxPages == page){
					
				}else if(maxPages > page){
					htmls2 += "<a href='javascript:queryInvertorFile(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}
			}
			htmls2 += "<a href='javascript:queryInvertorFile(\""+(page+1)+"\")' class='next'></a>";
			htmls2 += "</div>";
			
			$("#list").html(htmls);
			if(data.list.length>0){
				$(".page").html(htmls2);
			}
			
		}
	});
}



/**
 * 查询蓝天行动
 * @param {Object} pages
 */
function queryInvertorArticle(pages){
	name = $("#name").val();
	page = returnPage(pages);
	$.ajax({
		async : true,
		url : "/AppService/article/queryInvestorArticle.xhtml",
		data : {
			"page" : page,
			"name" : encodeURI(name),
			"cataLog":cataLog
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
                    htmls += "<h3 class='cursor' onclick='openToUrl(\""+item.linkUrl+"\")'>"+item.title+"</h3>";
	                htmls += "<dt>"+formatDate(item.publishDate)+"</dt>";
					htmls += "<dd>"+item.brief+"</dd>";	
		            htmls += "</dl>";
				})
			}else{
				$(".newsinfo").addClass("none");
				//noticeList();
				return;
			}
			var page = data.currentPage;
			var count = data.count;
			var maxPages = data.maxPages;
			$("#page").val(page);
			$("#maxPages").val(maxPages);
			
			htmls2 += "<a href='javascript:queryInvertorArticle(\""+(page-1)+"\")' class='pre'></a>";
			if(page == 1){
				htmls2 += "<a class='act' href='javascript:queryInvertorArticle(\"1\")'>1</a>";
			}else{
				htmls2 += "<a href='javascript:queryInvertorArticle(\"1\")'>1</a>";
			}
			
			if(page > 3){/* 左边加...*/
				htmls2 += "<a class='omit'></a>";
			}
			
			if((page-1) >1){/* 上一页*/
				htmls2 += "<a href='javascript:queryInvertorArticle(\""+(page-1)+"\")'>"+(page-1)+"</a>";
			}
			
			if(page != 1 && page != maxPages){/* 当前页*/
				htmls2 += "<a class='act' href='javascript:queryInvertorArticle(\""+(page)+"\")'>"+(page)+"</a>";
			}
			
			if(page+1 < maxPages){/*下一页*/
				htmls2 += "<a href='javascript:queryInvertorArticle(\""+(page+1)+"\")'>"+(page+1)+"</a>";
			}
			
			if((maxPages - page) > 2){/* 右边加...*/
				htmls2 += "<a class='omit'></a>";
			}
			
			if(page != 1){
				if(maxPages == page){
					htmls2 += "<a class='act' href='javascript:queryInvertorArticle(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}else if(maxPages > page){
					htmls2 += "<a href='javascript:queryInvertorArticle(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}
			}else{
				if(maxPages == page){
					
				}else if(maxPages > page){
					htmls2 += "<a href='javascript:queryInvertorArticle(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}
			}
			htmls2 += "<a href='javascript:queryInvertorArticle(\""+(page+1)+"\")' class='next'></a>";
			htmls2 += "</div>";
			
			$("#list").html(htmls);
			if(data.list.length>0){
				$(".page").html(htmls2);
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