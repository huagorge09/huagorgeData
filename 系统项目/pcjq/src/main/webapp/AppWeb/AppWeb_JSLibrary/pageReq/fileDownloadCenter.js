$(document).ready(function(e) {
	getUserRequest("pc_fileDownloadCenter_01");
	$(".nav.fr ul li a").removeClass("current");
	document.title = "下载_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	queryFileDownloadCenter();

	$(".download-con ul li:odd").css("background","#f9f9f9");
	
	$(".download-con span.check-more").toggle(
		function(){
			var el = $(this).parent(".download-con").find("ul");
			curHeight = el.height(),
			autoHeight = el.css('height','auto').height();
			el.height(curHeight).animate({height: autoHeight}, 800);
			$(this).find("img.down").hide();
			$(this).find("img.up").show();
		},
		function(){
			$(this).parent(".download-con").find("ul").animate({height:"474px"}, 800);
			$(this).find("img.down").show();
			$(this).find("img.up").hide();
		}
	);
});
function queryFileDownloadCenter(){
	$.ajax({
    	async:false,
        url: "/AppService/article/queryFileDownloadCenter.xhtml",
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
        	var tempLength2 = 0;
        	
			htmls += "<div class='cooperation-con' id='personal'>";
			htmls += "<span class='con-icon'>个人下载</span>";
			htmls += "<div class='download-con personal'>";
			htmls += "<ul>";
			$.each(list,function(i, item){
				if(item.fileType != null && item.fileType == "1"){
					tempLength1 ++;
					htmls += "<li>";
					htmls += "<em>"+item.fileName+"</em>";
					htmls += "<span>"+item.reserve1+"</span>";
					htmls += "<i>"+item.updateDate+"</i>";
					htmls += "<a href='"+item.fileDownload+"' target='_blank'><img src='/AppWeb/AppWeb_Images/images/download_center_bt.png'></a>";
					htmls += "</li>";
				}
			});
			htmls += "</ul>";
			if(tempLength1 > 6){
				htmls += "<span class='check-more'>展开查看更多";
				htmls += "<img class='down' src='/AppWeb/AppWeb_Images/images/download_center_showMore.png' height='15' width='15' alt=''>";
				htmls += "<img class='up none' src='/AppWeb/AppWeb_Images/images/download_center_showMore_up.png' height='15' width='15' alt=''>";
				htmls += "</span>";
			}
			htmls += "</div>";
			htmls += "</div>";
			htmls += "<div class='cooperation-con cooperation02' id='organization'>";
			htmls += "<span class='con-icon'>机构下载</span>";
			htmls += "<div class='download-con organization'>";
			htmls += "<ul>";

			$.each(list,function(i, item){
				if(item.fileType != null && item.fileType == "2"){
					tempLength2 ++;
					htmls += "<li>";
					htmls += "<em>"+item.fileName+"</em>";
					htmls += "<span>"+item.reserve1+"</span>";
					htmls += "<i>"+item.updateDate+"</i>";
					htmls += "<a href='"+item.fileDownload+"'><img src='/AppWeb/AppWeb_Images/images/download_center_bt.png'></a>";
					htmls += "</li>";
					htmls += "</li>";
				}
			});
			htmls += "</ul>";
			if(tempLength2 > 6){
				htmls += "<span class='check-more'>展开查看更多";
				htmls += "<img class='down' src='/AppWeb/AppWeb_Images/images/download_center_showMore.png' height='15' width='15' alt=''>";
				htmls += "<img class='up none' src='/AppWeb/AppWeb_Images/images/download_center_showMore_up.png' height='15' width='15' alt=''>";
				htmls += "</span>";
			}
			htmls += "</div>";
			htmls += "</div>";
			
			$("#infos").html(htmls);
        }
	});
}

/*function queryFileDownloadCenterDetail(fileId){
	var url = "/AppService/article/queryFileDownloadCenterDetail.xhtml?fileId="+fileId;
	window.open(url);
}*/