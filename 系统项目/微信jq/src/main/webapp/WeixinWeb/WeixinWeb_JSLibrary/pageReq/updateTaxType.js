/**
 * 
 */


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
	
	if(oldTaxType != type){
		taxResidentData = "";
	}
	var param = "?taxResidentType=" + type+"&taxResidentTypeNM="+text 
				+ "&taxResidentData="+taxResidentData + "&sourceType=user";
	if("1" == type){
		// 中国税收居民 直接返回 
		redirectUrl('/WeixinService/business/user/updateTaxResidentInfo.shtml' + param);
	}else{
		//其他选项 继续选择
		redirectUrl('/WeixinService/business/query/fundTaxInfo.shtml' + param);
	}
}


function saveQueryStringToEle(){
	var taxResidentData = GetQueryString("taxResidentData");
	$("#taxResidentData").val(taxResidentData);
	var sourceType = GetQueryString("sourceType");
	$("#sourceType").val(sourceType);
	var taxResidentType = GetQueryString("taxResidentType");
	$("#taxResidentType").val(taxResidentType);
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