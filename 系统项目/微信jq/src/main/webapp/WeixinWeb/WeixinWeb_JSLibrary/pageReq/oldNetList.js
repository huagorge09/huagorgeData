$(document).ready(function(e){
	queryNetList();
});
/*查询历史净值*/
function queryNetList(){
	var fundId = getUrlParameter('fundId');
	var isSubPrdAppraisement = getUrlParameter('isSub');
	//var outerId = getUrlParameter('outerId');
	//子产品非估值 查询时为 系列产品净值图
	// if("n" == isSubPrdAppraisement.toLowerCase()){
	// 	fundId = outerId;
	// }
	$.ajax({
		async:true,
		url : "/WeixinService/business/queryEstimateByFundId.xhtml",
		data : {
			fundId:fundId
		},
		dataType : "json",
        type:"POST",
		cache : false,
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			var list = data.productEstimateList;
			var htmls = "";
			if(list != null && list.length > 0){
				var moneyHtml = "";
				$.each(list,function(i, item){
					if(item.fluctuate>=0){
						moneyHtml = "<span class='red'>"+parseFloat(numMulti(item.fluctuate,100)).toFixed(2)+"%</span>"
					}else{
						moneyHtml = "<span class='green'>"+parseFloat(numMulti(item.fluctuate,100)).toFixed(2)+"%</span>"
					}
					htmls += "<li>";
					htmls += "<span>"+formatDate(item.eDate)+"</span>";
					htmls += "<span>"+parseFloat(item.netValue).toFixed(4)+"</span>";
					htmls += "<span>"+parseFloat(item.accNetValue).toFixed(4)+"</span>";
					htmls += moneyHtml;
					htmls += "</li>";
				})
				$(".historyNetworthCon ul").html(htmls);
			}
		}
	});
}
