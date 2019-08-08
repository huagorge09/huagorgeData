﻿$(document).ready(function(e) {
	$(".header .top-a h2").html("风险测评");
	document.title="风险测评";
	var proFesseData = queryParamList("SYSTEM","VOCCODE","");
	var proFesseHtml = "";
	for (var i = 0; i < proFesseData.length; i++) {
		var temp = proFesseData[i];
		proFesseHtml+='<a class="href-point" data-value="'+temp.pmco+'" data-text="'+temp.pmnm+'">'+temp.pmnm+'</a>';
	}
	
	var otherVocation = GetQueryString("otherVocation");
	$("#otherVocation").val(otherVocation);
	
	$("#section_01 .center-accountset").html(proFesseHtml);
	$("#section_01 a.href-point").click(function(){
		var vocCode = $(this).data("value");
		var vocName = $(this).data("text");
		$("#vocCode").val(vocCode);
		$("#vocName").val(vocName);
		if(vocCode == "15"){
			showPop();
			return;
		}
		goFundModRiskHouseAddr();
	});
});

function queryParamList(paramType,paramKey,pmValueOne){
    var data = null;
    $.ajax({
        async: !1,
        url: "/WeixinService/business/queryParamList.xhtml",
        data: {
            paramType :paramType,
            paramKey : paramKey,
            pmValueOne : pmValueOne
        },
        dataType: "json",
        cache: !1,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {
            if(n != null && n.resultCode =="0000"){
                data = n.data;
            }
        }
    });
    return data;
}

function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null){
         var val = decodeURI(r[2]);
    	 return  unescape(val);
     }else{
    	 return null;
     }
}

function goFundModRiskHouseAddr(){
	var param ="?";
	var nation= GetQueryString("nation");
	var nationNM = GetQueryString("nationNM");
	var province = GetQueryString("province");
	var provinceNM = GetQueryString("provinceNM");
	var city = GetQueryString("city");
	var cityNM = GetQueryString("cityNM");
	var address = GetQueryString("address");
	var dateOfBirth = GetQueryString("dateOfBirth");
	var taxResidentType = GetQueryString("taxResidentType");
	var taxResidentTypeNM = GetQueryString("taxResidentTypeNM");
	var taxResidentData = GetQueryString("taxResidentData");
	var viewType = GetQueryString("viewType");
	var eventId = GetQueryString("eventId");
	var pageSource = GetQueryString("pageSource");
	var vocCode = $("#vocCode").val();
	var vocName = $("#vocName").val();
	var otherVocation = $("#otherVocation").val();
	// if( !ischinese(otherVocation)){
	// 	errorRemark('请填写正确的职业')
	// 	return;
	// }
	if(vocCode == "15" && otherVocation == ""){
		errorRemark("请填写您的职业");
		return;
	}
	if(vocCode != "15"){
		otherVocation = "";
	}
	param += "nation="+nation+"&nationNM="+encodeURI(nationNM)+"&province="+province+"&provinceNM="+encodeURI(provinceNM)
	+"&city="+city+"&cityNM="+encodeURI(cityNM)+"&address="+encodeURI(address)+"&vocCode="+vocCode+"&vocName="+encodeURI(vocName)
	+"&dateOfBirth="+dateOfBirth+"&taxResidentType="+taxResidentType+"&taxResidentTypeNM="+taxResidentTypeNM+"&taxResidentData="+taxResidentData+"&viewType="+viewType
	+"&otherVocation="+otherVocation+"&eventId="+eventId+"&pageSource="+pageSource;
	window.location.href="/WeixinService/business/query/fundModRiskHouseAddrNew.shtml" + param;
}

function showPop(){
	$("#recommendPop").show();
	$(".backLayer").show();
}

function closePop(){
	$("#recommendPop").hide();
	$(".backLayer").hide();
}

// 校验是否为中文
function ischinese(name){
    return /^[\u4e00-\u9fa5]+$/.test(name)
}  