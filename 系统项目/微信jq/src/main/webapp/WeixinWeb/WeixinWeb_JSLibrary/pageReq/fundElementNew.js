$(document).ready(function(e){
	queryElement();
});
function queryElement(){
	var fundId = getUrlParameter('fundId');
	var period = getUrlParameter('period');
	$.ajax({
		async : false,
		url : "/WeixinService/business/queryFundInfo.xhtml",
		data : {
			"fundId" : fundId,
			"period":period
		},
		dataType : "json",
		type : 'post',
		cache : false,
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			var fundInfo = data.fundInfo;
			/* 产品内容介绍 */
			if (typeof fundInfo.elementList != 'undefined') {
				var templateStart = "<div class='box'><div class='box-text'><ul>";
				var templateEnd = "</ul></div></div>";
				var temp1 = "";
				var temp2 = "";
				$.each(fundInfo.elementList, function(index, msg) {
					if (msg.type == 210) {
						temp1 += "<li><span>" + msg.title + "：</span><em>" + msg.content + "</em></li>";
					}else if (msg.type == 220) {
						temp2 += "<div class='box'><h2><span>" + msg.title + "</span></h2><div class='box-text'><p>" + msg.content + "</p></div></div>";
					} else if (msg.type == 240){/*图片类型*/
						temp2 += "<div class='box'><h2><span>" + msg.title + "</span></h2><div class='box-text'><img src=" + msg.picUrl + " width='100%' height='auto'></img></div></div>";
					}
				});
				if (temp1.trim() != "") {
					$("#fundInfoBox").append(templateStart + temp1 + templateEnd);
				}
				if (temp2.trim() != "") {
					$("#fundInfoBox").append(temp2);
				}

			}else if(fundInfo.elementList.length <= 0){
				errorRemark("该产品暂无投资信息，请联系管理员！");
			}
			else{
				errorRemark("该产品暂无投资信息，请联系管理员！");
			}
		}
	});
}
/*返回查看产品信息*/
function goToFund(){
	var fundId = getUrlParameter('fundId');
	var orally = getUrlParameter('orally');
	var period = getUrlParameter('period');
	var serialno = getUrlParameter('serialno');
	var channleNo = getUrlParameter('channleNo');
	debugger;
	if (orally != null && orally == 'order') {
		redirectUrl("/WeixinService/business/query/orderDetailNew.shtml?serialno=" + serialno);
	} else if (orally != null && orally == 'orderOther') {
		redirectUrl("/WeixinService/business/query/otherOrderDetailNew.shtml?serialno="+serialno+"&fundId=" + fundId+"&channleNo="+channleNo);
	} else {
		redirectUrl("/WeixinService/business/query/fundInfoNew.shtml?fundId=" + fundId+"&period="+period);
	}
}