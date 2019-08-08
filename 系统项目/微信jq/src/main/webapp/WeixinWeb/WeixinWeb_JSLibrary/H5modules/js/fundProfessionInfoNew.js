$(document).ready(function(e) {
	$(".header .top-a h2").html("风险测评");
	document.title="风险测评";
	var proFesseData = queryParamList("SYSTEM","VOCCODE","");
	var proFesseHtml = "";
	for (var i = 0; i < proFesseData.length; i++) {
		var temp = proFesseData[i];
		proFesseHtml+='<a class="href-point" data-value="'+temp.pmco+'" data-text="'+temp.pmnm+'">'+temp.pmnm+'</a>';
	}
	
	var viewType = GetQueryString("viewType");
	$("#viewType").val(viewType);
	
	var nation = GetQueryString("nation");
	var nationNM = GetQueryString("nationNM");
	$("#nation").val(nation);
	$("#nationNM").val(nationNM);
	var province = GetQueryString("province");
	var provinceNM = GetQueryString("provinceNM");
	$("#province").val(province);
	$("#provinceNM").val(provinceNM);
	
	var address = GetQueryString("address");
	$("#address").val(address);
	
	var city = GetQueryString("city");
	$("#city").val(city);
	
	var cityNM = GetQueryString("cityNM");
	$("#cityNM").val(cityNM);
	
	var dateOfBirth = GetQueryString("dateOfBirth");
	$("#dateOfBirth").val(dateOfBirth);
	var taxResidentType = GetQueryString("taxResidentType");
	$("#taxResidentType").val(taxResidentType);
	var taxResidentTypeNM = GetQueryString("taxResidentTypeNM");
	$("#taxResidentTypeNM").val(taxResidentTypeNM);
	var taxResidentData = GetQueryString("taxResidentData");
	$("#taxResidentData").val(taxResidentData);
    var eventId = GetQueryString("eventId");
	var pageSource = GetQueryString("pageSource");
	$("#section_01 .center-accountset").html(proFesseHtml);
	$("#section_01 a.href-point").click(function(){
		var param ="?";
		var nation= $("#nation").val();
		var nationNM = $("#nationNM").val();
		var province = $("#province").val();
		var provinceNM = $("#provinceNM").val();
		var city = $("#city").val();
		var cityNM = $("#cityNM").val();
		var vocCode = $(this).data("value");
		var vocName = $(this).data("text");
		var address = $("#address").val();
		var dateOfBirth = $("#dateOfBirth").val();
		var taxResidentType = $("#taxResidentType").val();
		var taxResidentTypeNM = $("#taxResidentTypeNM").val();
		var taxResidentData = $("#taxResidentData").val();
		var viewType = $("#viewType").val();
		param += "nation="+nation+"&nationNM="+encodeURI(nationNM)+"&province="+province+"&provinceNM="+encodeURI(provinceNM)
						+"&city="+city+"&cityNM="+encodeURI(cityNM)+"&address="+encodeURI(address)+"&vocCode="+vocCode+"&vocName="+encodeURI(vocName)
						+"&dateOfBirth="+dateOfBirth+"&taxResidentType="+taxResidentType+"&taxResidentTypeNM="+taxResidentTypeNM+"&taxResidentData="+taxResidentData+"&viewType="+viewType
						+"&eventId="+eventId+"&pageSource="+pageSource;
		window.location.href="/WeixinService/H5modules/riskAssessment/fundModRiskHouseAddr.html" + param;
	});
	
	var vocCode = GetQueryString("vocCode");
	$("#vocCode").val(vocCode);
	
	var vocName = GetQueryString("vocName");
	$("#vocName").val(vocName);
	
	var region = GetQueryString("region");
	var address = GetQueryString("address");
	if(region != null && region != ""){
		$("#area").html(region);
	}
	if(address != null && address != ""){
		$("#address").val(address);
	}
	
	var nation = GetQueryString("nation");
	var nationNM = GetQueryString("nationNM");
	$("#nation").val(nation);
	$("#nationNM").val(nationNM);
	var province = GetQueryString("province");
	var provinceNM = GetQueryString("provinceNM");
	$("#province").val(province);
	$("#provinceNM").val(provinceNM);
	var city = GetQueryString("city");
	var cityNM = GetQueryString("cityNM");
	$("#city").val(city);
	$("#cityNM").val(cityNM);
	var addressStr = "";
	if(nationNM != "" && nationNM != null){
		addressStr+=nationNM+" ";
	}
	if(provinceNM != "" && provinceNM != null){
		addressStr+=provinceNM+" ";
	}
	if(cityNM != "" && cityNM != null){
		addressStr+=cityNM;
	}
	if(addressStr != "" && addressStr != null){
		$("#area").html(addressStr);
	}
});

function queryParamList(paramType,paramKey,pmValueOne){
    var data = null;
    $.ajax({
        async: !1,
        url: apiHost + "/api/queryParamList",
        data: {
            paramType :paramType,
            paramKey : paramKey,
            pmValueOne : pmValueOne
        },
        dataType: "json",
        cache: !1,
        type: "get",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {
            if(n != null && n.resp.resultCode =="0000"){
                data = n.resp.data;
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