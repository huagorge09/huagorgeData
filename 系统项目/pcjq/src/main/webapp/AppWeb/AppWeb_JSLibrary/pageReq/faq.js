$(document).ready(function(e) {
	getUserRequest("pc_faq");
	$(".nav.fr ul li a").removeClass("current");
	queryWenTiList("1");
	document.title = "常见问题_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
});
function queryWenTiList(pages){
	var title = $("#title").val();
	var page = parseInt(returnPage(pages),10);
	
	$.ajax({
    	async:true,
        url: "/AppService/article/queryWenTiList.xhtml",
        data: {
        	"title":encodeURI(title),
        	"page":page
        },
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        }, 
        success : function (data){
        	var htmls = "";
        	var htmls2 = "";/*分页*/
        	
        	
        	var list = data.list;
        	var currentPage = data.currentPage;/*当前页数*/
        	var count = data.count;/*招聘信息记录数*/
        	var maxPages = data.maxPages;/*最大页数*/
        	
        	$("#page").val(currentPage);
			$("#maxPages").val(maxPages);
			
			if(list != null && list.length > 0){
				$.each(list,function(i, item){
					htmls += "<dl>";
					htmls += "<dt>"+item.wWenti+"</dt>";
					htmls += "<i class='arrow'></i>";
					htmls += "<dd>"+item.wDaan+"</dd>";
					htmls += "</dl>";
	        	});
			}else{
				$("#faqList").html("<span class='bulletin nomsg_tips'>暂无消息，敬请期待...</span>");
				$(".page").html("");
				return;
			}
			$("#faqList").html(htmls);
			
			htmls2 += "<a href='javascript:queryWenTiList(\""+(page-1)+"\")' class='pre'></a>";
			if(page == 1){
				htmls2 += "<a class='act' href='javascript:queryWenTiList(\"1\")'>1</a>";
			}else{
				htmls2 += "<a href='javascript:queryWenTiList(\"1\")'>1</a>";
			}
			
			if(page > 3){/* 左边加...*/
				htmls2 += "<a class='omit'></a>";
			}
			
			if((page-1) >1){/* 上一页*/
				htmls2 += "<a href='javascript:queryWenTiList(\""+(page-1)+"\")'>"+(page-1)+"</a>";
			}
			
			if(page != 1 && page != maxPages){/* 当前页*/
				htmls2 += "<a class='act' href='javascript:queryWenTiList(\""+(page)+"\")'>"+(page)+"</a>";
			}
			
			if(page+1 < maxPages){/*下一页*/
				htmls2 += "<a href='javascript:queryWenTiList(\""+(page+1)+"\")'>"+(page+1)+"</a>";
			}
			
			if((maxPages - page) > 2){/* 右边加...*/
				htmls2 += "<a class='omit'></a>";
			}
			
			if(page != 1){
				if(maxPages == page){
					htmls2 += "<a class='act' href='javascript:queryWenTiList(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}else if(maxPages > page){
					htmls2 += "<a href='javascript:queryWenTiList(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}
			}else{
				if(maxPages == page){
					
				}else if(maxPages > page){
					htmls2 += "<a href='javascript:queryWenTiList(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}
			}
			htmls2 += "<a href='javascript:queryWenTiList(\""+(page+1)+"\")' class='next'></a>";
			$(".page").html(htmls2);
			
			$(".FAQ dl dt,.FAQ dl i.arrow").click(function(){
				$(this).parent("dl").toggleClass("act").siblings().removeClass("act");
			})
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