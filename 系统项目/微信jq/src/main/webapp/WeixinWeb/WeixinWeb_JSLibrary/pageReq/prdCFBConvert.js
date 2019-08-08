//活动产品中间界面js
var pageSource = '',pageId = '',eventId = '';
$(function(){
	//模板消息和图文消息
	var urlParams = getUrlParams();
	eventId = urlParams['eventId']?urlParams['eventId']:eventId;
	pageSource = urlParams['pageSource']?urlParams['pageSource']:pageSource;
	pageId = urlParams['pageId']?urlParams['pageId']:pageId;
	//recordOperation({eventId:eventId,pageId:pageId,pageSource:pageSource});
	var result = checkIsRiskLevel();
	if(!result.flag){
		if(pageSource == 'wx_cfbId'){
			//跳往财富宝活动页面
			redirectUrl("/WeixinService/business/query/wealthTreasure.shtml?pageSource="+pageSource);
		}else if(pageSource == 'wx_model_HL02_02Id' || pageSource == 'wx_model_HL02_01Id'){
			//跳往和利活动页面
			redirectUrl("/WeixinService/business/query/heli.shtml?pageSource="+pageSource);
		}else{
                     redirectUrl("/WeixinService/business/user/queryAccount.shtml?pageSource="+pageSource);
                }
	}
});

/* 未风险测评则弹出风险测评弹框  */
function checkIsRiskLevel(){
	var result = {};
	result.flag=false;
	var urlVal="/WeixinService/business/queryIsNeedTest.xhtml";
    $.ajax({
    	async:false,
		url:urlVal,
		type:"post",
		dataType:'json',
		data:{},
		error:function(){
			errorRemark("网络繁忙，请稍后再试。"); 
		},
		success:function(data, textStatus){
			//data.riskLevel=='0'
			var riskLevel = "";
			var riskEvalDate = "";
			
			if(!!data){
				riskLevel = data.riskLevel;
				riskEvalDate = data.riskEvalDate;
			}
			
			if(!riskEvalDate || "0" == riskLevel){
				//未测评 提示 
				$('#goRisk').unbind("click");
				$('#goRisk').click(function(){
					checkUserBaseInfoIsExist('fundModRiskHouseAddr');
				});
				//展示风险测评框
				showTips("noOperation");
				showTips("riskContent");
				result.flag=true;
			}else if(!!riskEvalDate && !!riskLevel && "0" != riskLevel){
				var nowDate= new Date();
			    var date = new Date(riskEvalDate); 
			    var dataDiff = (nowDate - date) / 86400000;
			    //时间已过 则 提示过期
			    if(dataDiff >= 365){
			    	//过期补充提示
			    	$('#reRisk').unbind("click");
					$('#reRisk').click(function(){
						checkUserBaseInfoIsExist('riskLevel');
					});
					showTips("noOperation");
					showTips("reRiskContent");
					result.flag=true;
			    }
			}
			
			
		}
	}); 
    
    if(result.flag){
    	$("#scroller").innerHeight(window.innerHeight);
    }
    return result;
}

//点击适当性提示 后去操作的页面
function checkUserBaseInfoIsExist(target){
	var resultData = queryUserinfo();
	if("riskLevel" == target){
		addCookie('riskUrl',location.href);
		redirectUrl("/WeixinService/business/user/riskLevel.shtml?pageSource="+pageSource);
	}else{
		//选择地址 带入默认值
		var param ="?";
		var nation = resultData.nation;
		var nationNM = resultData.nationNM;
		var province = resultData.province;
		var provinceNM = resultData.provinceNM;
		var city = resultData.city;
		var cityNM = resultData.cityNM;
		var vocCode = resultData.vocCode;
		var vocName = resultData.vocName;
		var address = resultData.addr;
		var taxResidentType = resultData.taxResidentType;
		var taxResidentTypeNm = resultData.taxResidentTypeNm;
		var birthDate = resultData.birthDate;
		param += "nation="+nation+"&nationNM="+encodeURI(nationNM)+"&province="+province+"&provinceNM="+encodeURI(provinceNM)
						+"&city="+city+"&cityNM="+encodeURI(cityNM)+"&address="+encodeURI(address)+"&vocCode="+vocCode+"&vocName="+encodeURI(vocName)
						+"&taxResidentType="+taxResidentType+"&taxResidentTypeNM="+taxResidentTypeNm + "&dateOfBirth="+birthDate
						+"&pageSource="+pageSource;
		redirectUrl('/WeixinService/business/query/fundModRiskHouseAddr.shtml'+param);
	}
}

