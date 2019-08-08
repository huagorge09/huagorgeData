﻿function queryRiskHistory() {
	$("#titleBack").click(function(){
		$('#titleBack').attr("href","javascript:redirectUrl('/WeixinService/business/user/riskLevel.shtml')");
	});
	$(".header .top-a h2").html("历史测评记录");
	loadRiskHistroy();
};

function loadRiskHistroy(){
	$.ajax({
        async: !1,
        url: "/WeixinService/business/queryUserRiskHistory.xhtml",
        dataType: "json",
        cache: !1,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(data) {
        	if (!!data) {
        		var jsonArrayData = data;
        		var html = "";
        		for (var i = 0; i < jsonArrayData.length; i++) {
        			var riskData = jsonArrayData[i];
        			html+='<tr class="risk_tr">';
        			html+='<td class="risk_td1">'+riskData.riskEvalDate+'</td>';
        			html+='<td class="risk_td2">'+riskData.riskLevel+'</td></tr>';
        		}
        		$("#riskData").html(html);
        	}
        }
    });
}

/**
 * 返回评级页面
 * @returns
 */
function backRiskPage(){
	window.location.href="/WeixinService/business/user/riskLevel.shtml";
}