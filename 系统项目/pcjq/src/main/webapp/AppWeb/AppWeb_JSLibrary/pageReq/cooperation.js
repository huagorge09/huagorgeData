$(document).ready(function(e) {
	getUserRequest("pc_cooperation_01");
	$(".nav.fr ul li a").removeClass("current");
	queryCooperation();
	document.title = "合作机构_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
});
function queryCooperation(){
	$.ajax({
		async : true,
		url : "/AppService/article/queryCooperation.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var htmls = "";
			var htmls2 = "";
			var temp = 0;
			var cooperationDtoList = data.cooperationDtoList;
			var consignmentDtoList = data.consignmentDtoList;
			
			if(cooperationDtoList != null && cooperationDtoList.length > 0){
				var tempCId = "";
				htmls += "<span class='con-icon'>项目合作方</span>";
				$.each(cooperationDtoList, function(i, item) {
						if(i == 0){
							temp = 1;
							htmls += "<div class='cooperation-box box"+temp+"'>";
							htmls += "<i>"+item.cName+"</i>";
							htmls += "<ul>";
							htmls += "<li><img src='"+item.picture+"'/></li>";
						}else if(i+1 < cooperationDtoList.length){
							if(tempCId == item.cId){
								htmls += "<li><img src='"+item.picture+"'/></li>";
							}else{
								temp ++;
								htmls += "</ul>";
								htmls += "</div>";
								htmls += "<div class='cooperation-box box"+temp+"'>";
								htmls += "<i>"+item.cName+"</i>";
								htmls += "<ul>";
								htmls += "<li><img src='"+item.picture+"'/></li>";
							}
						}else{
							if(tempCId == item.cId){
								htmls += "<li><img src='"+item.picture+"'/></li>";
							}
							htmls += "</ul>";
							htmls += "</div>";
						}
						
						tempCId = item.cId;
				});
				
			}
			
			if(consignmentDtoList != null && consignmentDtoList.length > 0){
				htmls2 += "<span class='con-icon'>代销机构</span>";
				htmls2 += "<div class='cooperation-box box1'>";
				htmls2 += "<ul>";
				$.each(consignmentDtoList, function(i, item) {
					htmls2 += "<li><img src='"+item.cPicture+"' /></li>";
				});
				htmls2 += "</ul>";
				htmls2 += "</div>";
			}
			
			$("#cooperation").html(htmls);
			$("#consignment").html(htmls2);
			var temp =document.URL;
			if(temp.split("#").length > 1){
				temp = temp.split("#")[1];
				if(temp != null && temp == "consignment"){
					document.getElementById("temp_consignment").click(); 
				}else if(temp != null && temp == "cooperation"){
					document.getElementById("temp_cooperation").click(); 
				}
			}
		}
	});
}