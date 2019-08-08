//���Ʒ�м����js
var pageSource = '',pageId = '',eventId = '';
$(function(){
	//ģ����Ϣ��ͼ����Ϣ
	var urlParams = getUrlParams();
	eventId = urlParams['eventId']?urlParams['eventId']:eventId;
	pageSource = urlParams['pageSource']?urlParams['pageSource']:pageSource;
	pageId = urlParams['pageId']?urlParams['pageId']:pageId;
	//recordOperation({eventId:eventId,pageId:pageId,pageSource:pageSource});
	var result = checkIsRiskLevel();
	if(!result.flag){
		if(pageSource == 'wx_cfbId'){
			//�����Ƹ����ҳ��
			redirectUrl("/WeixinService/business/query/wealthTreasureNew.shtml?pageSource="+pageSource);
		}else if(pageSource == 'wx_model_HL02_02Id' || pageSource == 'wx_model_HL02_01Id'){
			//���������ҳ��
			redirectUrl("/WeixinService/business/query/heliNew.shtml?pageSource="+pageSource);
		}else{
                     redirectUrl("/WeixinService/business/user/queryAccountNew.shtml?pageSource="+pageSource);
                }
	}
});

/* δ���ղ����򵯳����ղ�������  */
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
			errorRemark("���緱æ�����Ժ����ԡ�"); 
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
				//δ���� ��ʾ 
				$('#goRisk').unbind("click");
				$('#goRisk').click(function(){
					checkUserBaseInfoIsExist('fundModRiskHouseAddr');
				});
				//չʾ���ղ�����
				showTips("noOperation");
				showTips("riskContent");
				result.flag=true;
			}else if(!!riskEvalDate && !!riskLevel && "0" != riskLevel){
				var nowDate= new Date();
			    var date = new Date(riskEvalDate); 
			    var dataDiff = (nowDate - date) / 86400000;
			    //ʱ���ѹ� �� ��ʾ����
			    if(dataDiff >= 365){
			    	//���ڲ�����ʾ
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

//����ʵ�����ʾ ��ȥ������ҳ��
function checkUserBaseInfoIsExist(target){
	var resultData = queryUserinfo();
	if("riskLevel" == target){
		addCookie('riskUrl',location.href);
		redirectUrl("/WeixinService/business/user/riskLevelNew.shtml?pageSource="+pageSource);
	}else{
		//ѡ���ַ ����Ĭ��ֵ
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
		redirectUrl('/WeixinService/business/query/fundModRiskHouseAddrNew.shtml'+param);
	}
}

