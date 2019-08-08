$(document).ready(function(e) {
	document.title = "加入我们_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	$(".nav.fr ul li a").removeClass("current").eq(3).addClass("current");
	queryRecruitmentDetail();
})

function queryRecruitmentDetail(){
	var informationId = getUrlParameter("informationId");
	informationId = informationId.replace("#", "");
	informationId = removeSpecialStr(informationId);
	
	if(informationId == null || informationId == ""){
		return ;
	}
	$.ajax({
    	async:true,
        url: "/AppService/article/queryRecruitmentDetail.xhtml",
        data: {
        	"informationId":informationId
        },
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {
        	show_tips("网络繁忙，请稍后再试");
        }, 
        success : function (data){
        	var dto =data.dto;
        	var htmls = "";
        	if(data.returnCode != null && data.returnCode == "0000" && dto != null){
        		var informationRecruit = dto.informationRecruit;
        		var typeTemp = "社会招聘";
        		if(informationRecruit == "2"){
        			typeTemp = "校园招聘";
        		}else if(informationRecruit == "3"){
        			typeTemp = "实习生招聘";
        		}
        		$("#left_recruit").html(typeTemp);
        		
				htmls += "<h1 class='title'>"+dto.informationName+"</h1>";
				htmls += "<div class='info'>";
				htmls += "<ul>";
				htmls += "<li class='style-ad'>";
				htmls += "<em>招聘类型：</em>";
				htmls += "<span>"+typeTemp+"</span>";
				htmls += "</li>";
				htmls += "<li class='address'>";
				htmls += "<em>工作地点：</em>";
				htmls += "<span>"+dto.placeName+"</span>";
				htmls += "</li>";
				htmls += "<li class='style-job'>";
				htmls += "<em>职务类型：</em>";
				htmls += "<span>"+dto.positionName+"</span>";
				htmls += "</li>";
				htmls += "<li class='amount'>";
				htmls += "<em>招聘人数：</em>";
				htmls += "<span>"+dto.informationNumber+"</span>";
				htmls += "</li>";
				htmls += "<li class='deal'>";
				htmls += "<em>薪资待遇：</em>";
				htmls += "<span>"+dto.informationTreatment+"</span>";
				htmls += "</li>";
				htmls += "</ul>";
				htmls += "</div>";
				htmls += "<div class='detail'>";
				htmls += "<ul>";
				htmls += "<li>";
				htmls += "<em>工作职责：</em>";
				htmls += "<div class='detail-number'>"+dto.informationDuty+"</div>";
				htmls += "</li>";
				htmls += "<li>";
				htmls += "<em>任职资格：</em>";
				htmls += "<div class='detail-number'>"+dto.informationQualifications+"</div>";
				htmls += "</li>";
				htmls += "<li>";
				htmls += "<em>特别说明：</em>";
				htmls += "<div class='detail-number'>"+dto.informationExplain+"</div>";
				htmls += "</li>";
				htmls += "</ul>";
				htmls += "</div>";
				
				$("#details").html(htmls);
        	}else{
        		show_tips("没有查询到招聘信息");
        		setTimeout("gotoUrl('/AppService/article/aboutUs.shtml#joinUs')",1500);
        	}
        }
	});
}