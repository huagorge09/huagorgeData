/**
 * 
 */
var eventId,pageSource;

$(function(){
	var urlParams = getUrlParams();
    eventId = urlParams['eventId']?urlParams['eventId']:'',
    pageSource = urlParams['pageSource']?urlParams['pageSource']:'';
	
});

function bindTaxResidentTypeClick(){
	$("#section_01 a.href-point").click(function(event){
		clickTaxResidentType(event.target);
	});
}

function clickTaxResidentType(element){
	var oldTaxType=$("#taxResidentType").val();
	var type = $(element).attr("data-value");
	var text = $(element).attr("data-text");
	$("#taxResidentType").val(type);
	$("#taxResidentTypeNM").val(text);
	var taxResidentData = $("#taxResidentData").val();
	$("#sourceType").val("product");
	if(oldTaxType != type){
		taxResidentData = "";
	}
	var param = getAllQueryString() + "&taxResidentData="+taxResidentData;
	if("1" == type){
		redirectUrl('/WeixinService/H5modules/riskAssessment/fundModRiskHouseAddr.html' + param);
	}else{
		redirectUrl('/WeixinService/H5modules/riskAssessment/fundTaxInfo.html' + param);
	}
}

function bindTaxNationClick(){
	$("#section_01 a.href-point").click(function(event){
		clickTaxNation(event.target);
	});
}

function clickTaxNation(element){
	var type = $(element).attr("data-value");
	var text = $(element).attr("data-text");
	var nationType = $("#nationType").val();
	
	var taxResidentData = $("#taxResidentData").val();
	var deTaxResData = decodeURI(taxResidentData);
	var taxResJsonDatas = JSON.parse(deTaxResData);
	var targetId =$("#targetId").val();
	
	for(var i = 0 ;i<taxResJsonDatas.length;i++){
		var taxResJsonData = taxResJsonDatas[i];
		var divId = taxResJsonData.divId;
		if(!targetId || divId == targetId){
			if("birth" == nationType){
				taxResJsonData.taxBirthNation = type;
				taxResJsonData.taxBirthNationNM=text;
				if("1" == type){
					taxResJsonData.taxBirthRegion = type;
				}
			}else if("tax" == nationType){
				taxResJsonData.taxNationality = type;
				taxResJsonData.taxNationalityNM=text;
				if("1" == type){
					taxResJsonData.taxArea = type;
					taxResJsonData.taxAreaNM=text;
				}
			}else if("reside" == nationType){
				taxResJsonData.taxResideNation = type;
				taxResJsonData.taxResideNationNM=text;
				if("1" == type){
					taxResJsonData.taxResideRegion = type;
					taxResJsonData.taxResideRegionNM=text;
				}
			}
		}
	}
	taxResidentData = encodeURI(JSON.stringify(taxResJsonDatas));
	
	var param = getAllQueryString()+"&nationType="+nationType + "&targetId="+targetId+"&taxResidentData="+ taxResidentData
			;
	if("1" == type){
		redirectUrl('/WeixinService/H5modules/riskAssessment/fundTaxInfo.html' + param);
	}else{
		redirectUrl('/WeixinService/H5modules/riskAssessment/fundTaxAreaInfo.html' + param);
	}
}

function bindTaxAreaClick(){
	$("#section_taxArea a.href-point").click(function(event){
		clickTaxArea(event.target);
	});
}

function clickTaxArea(element){
	var type = $(element).attr("data-value");
	var text = $(element).attr("data-text");
	var nationType=$("#nationType").val();
	
	var taxResidentData = $("#taxResidentData").val();
	var deTaxResData = decodeURI(taxResidentData);
	var taxResJsonDatas = JSON.parse(deTaxResData);
	var targetId =$("#targetId").val();
	
	for(var i = 0 ;i<taxResJsonDatas.length;i++){
		var taxResJsonData = taxResJsonDatas[i];
		var divId = taxResJsonData.divId;
		if(!targetId || divId == targetId){
			if("birth" == nationType){
				taxResJsonData.taxBirthRegion = type;
				taxResJsonData.taxBirthRegionNM=text;
			}else if("tax" == nationType){
				taxResJsonData.taxArea = type;
				taxResJsonData.taxAreaNM=text;
			}else if("reside" == nationType){
				taxResJsonData.taxResideRegion = type;
				taxResJsonData.taxResideRegionNM=text;
			}
		}
	}
	taxResidentData = encodeURI(JSON.stringify(taxResJsonDatas));
	
	var param = getAllQueryString() + "&nationType="+nationType +"&taxResidentData="+taxResidentData;
	redirectUrl('/WeixinService/H5modules/riskAssessment/fundTaxInfo.html' + param);
}

function bindTaxNotCodeCauseClick(){
	$("#section_notCodeCause a.href-point").click(function(event){
		clickNotCodeCause(event.target);
	});
}

function clickNotCodeCause(element){
	var type = $(element).attr("data-value");
	var text = $(element).attr("data-text");
	var taxResidentData = $("#taxResidentData").val();
	var deTaxResData = decodeURI(taxResidentData);
	var taxResJsonDatas = JSON.parse(deTaxResData);
	var targetId =$("#targetId").val();
	
	for(var i = 0 ;i<taxResJsonDatas.length;i++){
		var taxResJsonData = taxResJsonDatas[i];
		var divId = taxResJsonData.divId;
		if(divId == targetId){
			taxResJsonData.taxNotCodeCause = type;
			taxResJsonData.taxNotCodeCauseNM = text;
		}
	}
	taxResidentData = encodeURI(JSON.stringify(taxResJsonDatas));
	
	var param = getAllQueryString()+"&taxResidentData="+taxResidentData;
	redirectUrl('/WeixinService/H5modules/riskAssessment/fundTaxInfo.html' + param);
}

function loadTaxAreaInfo(){
	var nationType= $("#nationType").val();
	var taxResidentData = $("#taxResidentData").val();
	var deTaxResData = decodeURI(taxResidentData);
	var taxResJsonDatas = JSON.parse(deTaxResData);
	var targetId =$("#targetId").val();
	
	var taxNation = "";
	for(var i = 0 ;i<taxResJsonDatas.length;i++){
		var taxResJsonData = taxResJsonDatas[i];
		var divId = taxResJsonData.divId;
		if(!targetId || divId == targetId){
			taxNation = taxResJsonData.taxNationality;
			if("birth" == nationType){
				taxNation = taxResJsonData.taxBirthNation;
			}else if("reside" == nationType){
				taxNation = taxResJsonData.taxResideNation;
			}
		}
	}
	var target = $("#section_taxArea div");
	var taxAreaList = queryParamList("","NATION","");
	
	if("5" != taxNation){
		target.find("a[data-value!=156-"+taxNation+"]").remove();
	}else{
		target.find("a").remove();
		for( var i = 0 ; i < taxAreaList.length ; i ++ ){
			var nation = taxAreaList[i];
			var pmco = nation.pmco;
			var pmnm = nation.pmnm;
			if("156" == pmco){
				continue;
			}
			var content = "<a class=\"href-point\" data-value=\""+pmco+"\" data-text=\""+pmnm+"\">"+pmnm+"</a>";
			target.append(content);
		}
	}
}

function saveQueryStringToEle(){
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
	var address = GetQueryString("address");
	$("#address").val(address);
	var vocCode = GetQueryString("vocCode");
	var vocName = GetQueryString("vocName");
	$("#vocCode").val(vocCode);
	$("#vocName").val(vocName);
	var dateOfBirth = GetQueryString("dateOfBirth");
	$("#dateOfBirth").val(dateOfBirth);
	var taxResidentType = GetQueryString("taxResidentType");
	var taxResidentTypeNM = GetQueryString("taxResidentTypeNM");
	$("#taxResidentType").val(taxResidentType);
	$("#taxResidentTypeNM").val(taxResidentTypeNM);
	var taxResidentData = GetQueryString("taxResidentData");
	$("#taxResidentData").val(taxResidentData);
	var sourceType = GetQueryString("sourceType");
	$("#sourceType").val(sourceType);
	var viewType = GetQueryString("viewType");
	$("#viewType").val(viewType);
}

function getAllQueryString(){
	var param ="?";
	var nation= $("#nation").val();
	var nationNM = $("#nationNM").val();
	var province = $("#province").val();
	var provinceNM = $("#provinceNM").val();
	var city = $("#city").val();
	var cityNM = $("#cityNM").val();
	var vocCode = $("#vocCode").val();
	var vocName = $("#vocName").val();
	var address = $("#address").val();
	var dateOfBirth = $("#dateOfBirth").val();
	var taxResidentType = $("#taxResidentType").val();
	var taxResidentTypeNM = $("#taxResidentTypeNM").val();
	var sourceType = $("#sourceType").val();
	var viewType = $("#viewType").val();
	param += "nation="+nation+"&nationNM="+encodeURI(nationNM)+"&province="+province+"&provinceNM="+encodeURI(provinceNM)
					+"&city="+city+"&cityNM="+encodeURI(cityNM)+"&address="+encodeURI(address)+"&vocCode="+vocCode
					+"&vocName="+encodeURI(vocName)+"&dateOfBirth="+dateOfBirth+"&taxResidentType="+taxResidentType+"&taxResidentTypeNM="+encodeURI(taxResidentTypeNM)
					+"&sourceType="+sourceType+"&viewType="+viewType+"&eventId="+eventId+"&pageSource="+pageSource
					;
	
	return param;
}

function fundTaxNationInfoLoadParam(){
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
	var address = GetQueryString("address");
	$("#address").val(address);
	var vocCode = GetQueryString("vocCode");
	var vocName = GetQueryString("vocName");
	$("#vocCode").val(vocCode);
	$("#vocName").val(vocName);
	var dateOfBirth = GetQueryString("dateOfBirth");
	$("#dateOfBirth").val(dateOfBirth);
	
	var nationType = GetQueryString("nationType");
	$("#nationType").val(nationType);
	
	var viewType = GetQueryString("viewType");
	$("#viewType").val(viewType);
	
	var taxResidentType = GetQueryString("taxResidentType");
	var taxResidentTypeNM = GetQueryString("taxResidentTypeNM");
	$("#taxResidentType").val(taxResidentType);
	$("#taxResidentTypeNM").val(taxResidentTypeNM);
	
	var taxResidentData = GetQueryString("taxResidentData");
	$("#taxResidentData").val(taxResidentData);
	
	var targetId = GetQueryString("targetId");
	$("#targetId").val(targetId);
	
	var sourceType = GetQueryString("sourceType");
	$("#sourceType").val(sourceType);
	
}

function delTaxNationDataByMinTargetId(){
	var taxResidentData = $("#taxResidentData").val();
	var deTaxResData = decodeURI(taxResidentData);
	var taxResJsonDatas = JSON.parse(deTaxResData);
	var targetId =$("#targetId").val();
	if(!targetId){
		return;
	}
	var minDivId = Number.MAX_VALUE;
	for(var i = 0 ;i<taxResJsonDatas.length;i++){
		var taxResJsonData = taxResJsonDatas[i];
		var divId = taxResJsonData.divId.replace(/[^0-9]/ig,"");
		if(divId < minDivId){
			minDivId = divId;
		}
	}
	var minTargetId = targetId.replace(/[^0-9]/ig,"");
	if(minDivId == minTargetId && 1 < taxResJsonDatas.length){
		$("#section_01 a[data-value!=1]").remove();
	}else{
		$("#section_01 a[data-value=1]").remove();
	}
}


function loadTaxAreaParam(){
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
	var address = GetQueryString("address");
	$("#address").val(address);
	var vocCode = GetQueryString("vocCode");
	var vocName = GetQueryString("vocName");
	$("#vocCode").val(vocCode);
	$("#vocName").val(vocName);
	var dateOfBirth = GetQueryString("dateOfBirth");
	$("#dateOfBirth").val(dateOfBirth);
	
	var taxResidentType = GetQueryString("taxResidentType");
	var taxResidentTypeNM = GetQueryString("taxResidentTypeNM");
	$("#taxResidentType").val(taxResidentType);
	$("#taxResidentTypeNM").val(taxResidentTypeNM);
	
	var nationType = GetQueryString("nationType");
	$("#nationType").val(nationType);
	
	var taxResidentData = GetQueryString("taxResidentData");
	$("#taxResidentData").val(taxResidentData);
	
	var targetId = GetQueryString("targetId");
	$("#targetId").val(targetId);
	
	var sourceType = GetQueryString("sourceType");
	$("#sourceType").val(sourceType);
	
	var viewType = GetQueryString("viewType");
	$("#viewType").val(viewType);
}


function loadTaxNotCodeCauseParam(){
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
	var address = GetQueryString("address");
	$("#address").val(address);
	var vocCode = GetQueryString("vocCode");
	var vocName = GetQueryString("vocName");
	$("#vocCode").val(vocCode);
	$("#vocName").val(vocName);
	var dateOfBirth = GetQueryString("dateOfBirth");
	$("#dateOfBirth").val(dateOfBirth);
	var taxResidentType = GetQueryString("taxResidentType");
	var taxResidentTypeNM = GetQueryString("taxResidentTypeNM");
	$("#taxResidentType").val(taxResidentType);
	$("#taxResidentTypeNM").val(taxResidentTypeNM);
	var taxResidentData = GetQueryString("taxResidentData");
	$("#taxResidentData").val(taxResidentData);
	
	var targetId = GetQueryString("targetId");
	$("#targetId").val(targetId);
	
	var sourceType = GetQueryString("sourceType");
	$("#sourceType").val(sourceType);
	
	var viewType = GetQueryString("viewType");
	$("#viewType").val(viewType);
}

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
            errorRemark("网络异常！");
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