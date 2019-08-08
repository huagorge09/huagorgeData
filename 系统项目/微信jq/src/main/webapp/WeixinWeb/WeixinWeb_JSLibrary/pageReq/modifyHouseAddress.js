﻿function queryAddressInfo() {
	$("#titleBack").click(function(){
		$('#titleBack').attr("href","javascript:redirectUrl('/WeixinService/business/user/userInfo.shtml')");
	});
	$(".header .top-a h2").html("选择地区");
	document.title="选择地区";
	var region = GetQueryString("region");
	var address = GetQueryString("address");
	if(region != null && region != ""){
		$("#area").val(region);
	}
	if(address != null && address != ""){
		$("#detailAddress").val(address);
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
		$("#area").val(addressStr);
	}
};

function saveAddress(){
	var region = GetQueryString("region");
	var address = GetQueryString("address");
	var nation = $("#nation").val();
	var province = $("#province").val();
	var city = $("#city").val();
	var detailAddress = $("#detailAddress").val();
	var area = $("#area").val();
	if(area == '' || area== null || area == "请选择"){
		errorRemark("请选择地区");
		return;
	}
	if(area == region && detailAddress == address){
		setTimeout(function(){window.location.href="/WeixinService/business/user/userInfo.shtml";},3000);
	}else{
		$.ajax({
	        async: !1,
	        url: "/WeixinService/business/updateCmfUserBaseInfo.xhtml",
	        data: {
	        	nation : nation,
	        	province : province,
	        	city : city,
	        	addr : detailAddress,
	        	voccode : ""
	        },
	        dataType: "json",
	        cache: !1,
	        type: "POST",
	        error: function() {
	        	errorRemark("网络繁忙，请稍后再试。");
	        },
	        success: function(n) {
	        	if(n != null && n.resultCode =="0000"){
	        		window.location.href="/WeixinService/business/user/userInfo.shtml";
	            }else{
	            	errorRemark("修改地址失败");
	            }
	        }
	    });
	}
}

function cacelAddressUpdate(){
	window.location.href="/WeixinService/business/user/userInfo.shtml";
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

function loadNationInfo(){
	$(".header .top-a h2").html("选择地区");
	document.title="选择地区";
	var nationalityData = queryParamList("DS","DS_NATION","");
	var nationHtml = "";
	for (var i = 0; i < nationalityData.length; i++) {
		var temp = nationalityData[i];
		if(temp.pmnm.indexOf("中国") > -1){
			nationHtml+='<a class="href-point" data-value="'+temp.pmco+'" data-text="'+temp.pmnm+'">'+temp.pmnm+'<i id="area"></i><em></em></a>';
		}else{
			nationHtml+='<a class="href-point" data-value="'+temp.pmco+'" data-text="'+temp.pmnm+'">'+temp.pmnm+'<i id="area"></i></a>';
		}
	}
	$("#section_01 .center-accountset").html(nationHtml);
	$("#section_01 a.href-point").click(function(){
		var val = $(this).data("value");
		var text = $(this).data("text");
		var emLen = $(this).find("em").length;
		if(emLen > 0){
			window.location.href="/WeixinService/business/user/provinceInfo.shtml?nation="+val+"&nationNM="+encodeURI(text);
		}else{
			window.location.href="/WeixinService/business/user/modifyHouseAddress.shtml?nation="+val+"&nationNM="+encodeURI(text);
		}
	})
};

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


function loadProvinceInfo() {
	$(".header .top-a h2").html("选择地区");
	document.title="选择地区";
	var nation = GetQueryString("nation");
	var nationNM = GetQueryString("nationNM");
	$("#nation").val(nation);
	$("#nationNM").val(nationNM);
	var provinceData = queryParamList("SYSTEM","DS_PROVINCE",nation);
	var provinceHtml = "";
	for (var i = 0; i < provinceData.length; i++) {
		var temp = provinceData[i];
		if(temp.leafNum > 1){
			provinceHtml+='<a class="href-point" data-value="'+temp.pmco+'" data-text="'+temp.pmnm+'">'+temp.pmnm+'<i id="area"></i><em></em></a>';
		}else{
			provinceHtml+='<a class="href-point" data-value="'+temp.pmco+'" data-text="'+temp.pmnm+'">'+temp.pmnm+'<i id="area"></i></a>';
		}
	}
	$("#section_02 .center-accountset").html(provinceHtml);
	$("#section_02 a.href-point").click(function(){
		var val = $(this).data("value");
		var text = $(this).data("text");
		var emLen = $(this).find("em").length;
		if(emLen > 0){
			window.location.href="/WeixinService/business/user/cityInfo.shtml?nation="+nation+"&nationNM="+encodeURI(nationNM)+"&province="+val+"&provinceNM="+encodeURI(text);
		}else{
			window.location.href="/WeixinService/business/user/modifyHouseAddress.shtml?nation="+nation+"&nationNM="+encodeURI(nationNM)+"&province="+val+"&provinceNM="+encodeURI(text);
		}
	})
};

function loadCityInfo() {
	$(".header .top-a h2").html("选择地区");
	document.title="选择地区";
	var nation = GetQueryString("nation");
	var nationNM = GetQueryString("nationNM");
	$("#nation").val(nation);
	$("#nationNM").val(nationNM);
	var province = GetQueryString("province");
	var provinceNM = GetQueryString("provinceNM");
	$("#province").val(province);
	$("#provinceNM").val(provinceNM);
	var cityData = queryParamList("SYSTEM","DS_CITYCODE",province);
	var cityHtml = "";
	for (var i = 0; i < cityData.length; i++) {
		var temp = cityData[i];
		cityHtml+='<a class="href-point" data-value="'+temp.pmco+'" data-text="'+temp.pmnm+'">'+temp.pmnm+'<i id="area"></i></a>';
	}
	$("#section_03 .center-accountset").html(cityHtml);
	$("#section_03 a.href-point").click(function(){
		var val = $(this).data("value");
		var text = $(this).data("text");
		window.location.href="/WeixinService/business/user/modifyHouseAddress.shtml?nation="+nation+"&nationNM="+encodeURI(nationNM)+"&province="+province+"&provinceNM="+encodeURI(provinceNM)+"&city="+val+"&cityNM="+encodeURI(text);
	});
};