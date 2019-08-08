$(document).ready(function(e) {
	getUserRequest("pc_openAccountProcess");
	document.title = "购买指南_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	$(".nav.fr ul li a").removeClass("current");
	
	queryOpenAccountProcess();
	
	$(".other-operation span.check-more").toggle(function(){
        var el = $(this).parent(".other-operation").find("ul");
        curHeight = el.height(),
        autoHeight = el.css('height','auto').height();
        el.height(curHeight).animate({height: autoHeight}, 800);
        $(this).find("img.down").hide();
        $(this).find("img.up").show();
      },
      function(){
        $(this).parent(".other-operation").find("ul").animate({height:"366px"}, 800);
        $(this).find("img.down").show();
        $(this).find("img.up").hide();
    }
    );
});
function queryOpenAccountProcess(){
	$.ajax({
    	async:false,
        url: "/AppService/article/queryOpenAccountProcess.xhtml",
        data: {},
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        }, 
        success : function (data){
        	var list = data.list;
        	var htmls = "";
        	var tempLength1 = 0;
        	
        	htmls += "<h3 class='flow-title'>其它业务流程说明</h3>";
        	htmls += "<ul>";
			$.each(list,function(i, item){
				htmls += "<li>";
                htmls += "<a href='"+item.fileDownload+"' target='_blank'>";
                htmls += "<span>"+item.fileName+"</span>";
                htmls += "<em>"+item.updateDate+"</em>";
                htmls += "</a>";
                htmls += "</li>";
			});
			htmls += "</ul>";
			if(tempLength1 > 6){
				htmls += "<span class='check-more'>展开查看更多";
	            htmls += "<img class='down' src='/AppWeb/AppWeb_Images/images/download_center_showMore.png' height='15' width='15' alt=''>";
	            htmls += "<img class='up none' src='/AppWeb/AppWeb_Images/images/download_center_showMore_up.png' height='15' width='15' alt=''>";
	            htmls += "</span>";
			}

			$.each(list,function(i, item){
				
			});
			
			$("#infos").html(htmls)
        }
	});
}

/*function queryFileDownloadCenterDetail(fileId){
	var url = "/AppService/article/queryFileDownloadCenterDetail.xhtml?fileId="+fileId;
	window.open(url);
}*/