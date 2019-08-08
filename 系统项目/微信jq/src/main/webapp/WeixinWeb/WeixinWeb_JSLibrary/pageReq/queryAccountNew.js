var myScroll;
var USERBASEINFO  = null;
var isRealName = true;
var eventId = '',pageId = 'wx_accountId',pageSource = 'wx_accountId';
var tips1 = ['event_textProRepotId'],tips2 = ['event_wx_batchSendId','toMyMessage'];
function loaded(){
	$("#menu_f1,#menu_f2,#menu_f3").removeClass("act");
	$("#menu_f3").addClass("act");
	$("#menu_f3").addClass("active");
    //合格投资者认证开关
	openQualified()
	// mgm功能开关
	openMgm()
}
$(document).ready(function(e) {

	USERBASEINFO = queryUserinfo();
	var urlParams = getUrlParams();
	eventId = urlParams['eventId']?urlParams['eventId']:eventId;
	pageSource = urlParams['pageSource']?urlParams['pageSource']:pageSource;
	if(eventId && ('event_wxModelMess_reportId' == eventId || 'event_textProRepotId' == eventId 
			|| 'event_wx_batchSendId' == eventId)){
		//记录点击2图文消息||模板消息||群发消息（阅读原文）
		pageSource = '';
		if('event_wx_batchSendId' == eventId){
			//群发
			pageSource = 'wx_model_batchSendId';
		}else if('event_wxModelMess_reportId' == eventId){
			//点击图文消息2 
			pageSource = 'wx_model_reportId';
		}else if('event_textProRepotId' == eventId){
			//模板消息
			pageSource = 'wx_model_textProRepotId';
		}
		recordOperation({eventId:eventId,pageId:pageId,pageSource:pageSource});
		checkIsRiskLevel(eventId);
	}
	//加载活动
	loadActive();
	
	$(".foot .nav-a ul li a").removeClass("act");
	$(".foot .nav-a ul li:eq(2) a").addClass("act");
	myScroll = new IScroll('#wrapper', {
		scrollbars: true,
		mouseWheel: true,
		interactiveScrollbars: true,
		shrinkScrollbars: 'scale',
		fadeScrollbars: true
	});
	$(".header .top-a h2").html("我的财富");
	document.title="我的财富";
	queryAccount();
    
    getUserRequest("query-account");/* 此处subPath为页面内行为 */
    
    
});

/**
 * 加载活动
 */
function loadActive(){
	
	var time = getNowTime();
	
	loadPrdCFB(time);//加载活动产品财富宝
	loadYearReport(time);//年报活动
	loadQuarterReport(time)//季报活动
}

function loadPrdCFB(time){
	var pordPlanDate = queryParamList("SYSTEM","PRODPLANDATE","");
	if(time <= pordPlanDate[0].pmco){
		var cookieStr = getCookie(USERBASEINFO.cmfUserId+"PRODPLANDATE");
		if(cookieStr == null){
			var expiresDate = getCookieExpiresOneDay();//获取一天的有效期
			//设置cookie
			setCookie(USERBASEINFO.cmfUserId+'PRODPLANDATE','PRODPLANDATE',expiresDate)
			
			//财富宝活动
			checkIsRiskLevel('PRODPLANDATE');
		}
	}
}
/**
 * 加载年报活动
 */
function loadYearReport(time){
	var planDate = queryParamList("SYSTEM","PLANDATE","");//年报活动时间
	if(time <= planDate[0].pmco){
		var planMsgKey = queryParamList("SYSTEM","PLANMSGKEY","");
		var title = planMsgKey[0].pmco;//年报关键字
		//是否展示
		var isShowTips = queryMsgLikeTitle(title);
		if(isShowTips && isShowTips == 'Y'){
			$('#bt_msg_new').attr('src','/WeixinWeb/WeixinWeb_Images/images/bt_new.png').show();
		}
	}
}
/**
 * 加载季报活动
 */
function loadQuarterReport(time){
	var quarterDate = queryParamList("SYSTEM","QUARTERDATE","");//季报活动时间
	if(time <= quarterDate[0].pmco){
		var quarterMsgKey = queryParamList("SYSTEM","QUARTERMSGKEY","");
		var title = quarterMsgKey[0].pmco;//季报关键字
		//是否展示
		var isShowTips = queryMsgLikeTitle(title);
		if(isShowTips && isShowTips == 'Y'){
			$('#bt_msg_new').attr('src','/WeixinWeb/WeixinWeb_Images/images/bt_new_quarter.png').show();
		}
	}
}





function queryMsgLikeTitle(title){
	   var resultData = "";
	   $.ajax({
	        async: false,
	        url: "/WeixinService/business/queryUserMsgLikeTitle.xhtml",
	        data: {
	        'title' : title
	        },
	        dataType: "json",
	        cache: false,
	        type: "POST",
	        error: function() {
	            show_tips("网络繁忙，请稍后再试。");
	        },
	        success: function(data) {
	        if(!!data){
	            resultData = data.isShowTips;
	        }
	        }
	    });
	   return resultData;
	}






document.addEventListener('touchmove', function (e) {e.preventDefault(); }, false);
/* 查询用户总资产和总收益 */
function queryAccount(){
	$.ajax({
    	async:true,
        url: "/WeixinService/business/queryAccount.xhtml",
        data: "",
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        }, 
        success : function (data){
    		if(data.asstesInCome.asstes.subString(0,1)>0){
    			$("#asstes").text(formatNumber(parseFloat(unformat(data.asstesInCome.totalValue)).toFixed(2),","));
    			$('#asstes_title').after('<span id="showHoldTip" onclick="holdAmountTip()"></span>')
			}
			var userName = data.asstesInCome.userName;
			if(userName!=null && userName!=''&& userName != '先生/女士' && typeof(userName)!='undefined'){
				$("#name").text(userName);
			}else{
				$("#name").text(data.asstesInCome.mobile);
			}
			if(data.totalFundBalanceCode != null && "0000"==data.totalFundBalanceCode){
				$("#inCome").text(formatNumber(parseFloat(unformat(data.asstesInCome.inCome)).toFixed(2),","));
			}
			if(data.asstesInCome.userType != null && data.asstesInCome.userType == "10"){
				$("div.center-info a.href-point.bank-manage").attr("href","javascript:redirectUrl('/WeixinService/business/bank/bankAuthNew.shtml?pageSource=wx_activeId')");
			}
			if(data.unRead != null && parseFloat(data.unRead) > 0){
				$("#unRead").show();
				if(data.unRead>=10){
					$("#unRead").html("…");
				}else{
					$("#unRead").html(data.unRead);
				}
			}else{
				$("#unRead").remove();
			}
			/* else{
				if("QRY-U001"!=data.totalFundBalanceCode){
					if(userName!=null && userName!='' && userName!='先生/女士' && typeof(userName)!='undefined'){
						$("#name").text(userName);
					}else{
						$("#name").text(data.asstesInCome.mobile);
					}
					if("10"==data.asstesInCome.userType){
						$("#perfectUrl").show();
					}
				}
			} */
			myScroll.refresh();	
        }
    });
}

/* 未风险测评则弹出风险测评弹框  */
function checkIsRiskLevel(type){
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
				$("#riskMsg").html('信披');
		    	if($.inArray(type, tips1) != -1){
		    		$("#riskMsg").html("季报");
				}else if(type == 'PRODPLANDATE'){
					$("#riskMsg").html("产品")
				}else if(type == 'PRODPLANDATE'){
					$("#riskMsg").html("产品")
				}
		    	
		    	$('#goRisk').unbind("click");
				$('#goRisk').click(function(){
					checkUserBaseInfoIsExist('fundModRiskHouseAddr',type);
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
						checkUserBaseInfoIsExist('riskLevel',type);
					});
					
					showTips("noOperation");
					showTips("reRiskContent");
					result.flag=true;
			    }
			}
			
			if(type != 'PRODPLANDATE'){
				if(!result.flag && !isRealName){
					//未实名
					$("#realNameMsg").html('信披');
					if($.inArray(type, tips1) != -1){
						$("#realNameMsg").html("季报");
					}
					$('#goAuth').unbind("click");
					$('#goAuth').click(function(){
						redirectUrl('/WeixinService/business/bank/bankAuthNew.shtml?pageSource='+pageSource)
					});
					showTips("noOperation");
					showTips("realNameContent");
					result.flag=true;
				}
			}
			
		}
	});  
    return result;
}

//点击适当性提示 后去操作的页面
function checkUserBaseInfoIsExist(target,type){
	var resultData = USERBASEINFO;
	
	if("riskLevel" == target){
		//过期
		eventId='event_wxMyMessage_expiredId';
		if(type == 'toMyMessage' || type == 'PRODPLANDATE'){
    		pageSource='wx_accountId';
		}
		recordOperation({eventId:eventId,pageId:pageId,pageSource:pageSource});
		redirectUrl("/WeixinService/business/user/riskLevelNew.shtml?pageSource="+pageSource);
	}else{
		//未做过测评
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
		eventId='event_wx_myMessageId';
		if(type == 'toMyMessage' || type == 'PRODPLANDATE'){
    		pageSource='wx_accountId';
		}
		recordOperation({eventId:eventId,pageId:pageId,pageSource:pageSource});
		param += "nation="+nation+"&nationNM="+encodeURI(nationNM)+"&province="+province+"&provinceNM="+encodeURI(provinceNM)
						+"&city="+city+"&cityNM="+encodeURI(cityNM)+"&address="+encodeURI(address)+"&vocCode="+vocCode+"&vocName="+encodeURI(vocName)
						+"&taxResidentType="+taxResidentType+"&taxResidentTypeNM="+taxResidentTypeNm + "&dateOfBirth="+birthDate
						+"&pageSource="+pageSource;
		redirectUrl('/WeixinService/business/query/fundModRiskHouseAddrNew.shtml'+param);
	}
}

function toMyMessage(){
	var result = checkIsRiskLevel('toMyMessage');
	if(!!result && !result.flag){
		redirectUrl('/WeixinService/business/query/msgListNew.shtml');
	}
}

/**
 * 合格投资者认证
 */
function qualified(){
    var _self=this;
	$.ajax({
        url  :'/WeixinService/business/queryQualifiedUserInfoByIdno.xhtml',
        type : 'POST',
        async:false,
        dataType : 'json',
        success : function(data) {
    	  if(data.returnCode=="0000"){
    	  	   if(data.data&&data.data.length>0){
    	  	   	   var status=data.data[0].statusRecord.status
    	  	   	   if(status=="F"){
						$.ajax({
    			            url  :'/WeixinService/business/modifyAccreditedInvestorInfoStatus.xhtml',
    			            type : 'POST',
    			            data : {"updateStatus":"R"},
    			            async:false,
    			            dataType : 'json',
    			            success : function(data) {
    			        	  if(data.returnCode=="0000"){
    			        		  location.href="/WeixinService/business/qualified/qualifiedNew.shtml";
    			        	  }else{
    			        	  	 $(document).dialog({
									    overlayClose: true,
									    content: data.returnMsg
								 });
    			        	  }
    			           }
				       })
    	  	   	   }else if(status=="U"){
    	  	   	     	$(document).dialog({
						    closeBtnShow: false,
						    overlayClose:false,
						    content: '您的合格投资信息已由客户经理上传，请确认提交审核',
						    onClickConfirmBtn: function(){
						       location.href="/WeixinService/business/qualified/qualifiedNew.shtml";
						    }
						});
    	  	   	   }else if(status=="S"){
    	  	   	   	 $(document).dialog({
						    overlayClose: true,
						    content: '您的合格投资信息已审核通过,您已经是合格投资者',
					 });
    	  	   	      	
    	  	   	   }else if(status=="N"){
    	  	   	   	 $(document).dialog({
						    overlayClose: true,
						    content: '您的合格投资信息正在审核中，请耐心等待',
					 }); 
    	  	   	   }else if(status=="R"){
    	  	   	   	  location.href="/WeixinService/business/qualified/qualifiedNew.shtml";
    	  	   	   }
  	  	   	   }else{
      	  	   	   	$.ajax({
			            url  :'/WeixinService/business/queryAccreditedInvestorConditions.xhtml',
			            type : 'POST',
			            async:false,
			            dataType : 'json',
			            success : function(data) {
			        	  if(data.returnCode=="0000"){
			        	  	   if(data.data.financialCertificate&&data.data.investCertificate){
				        	  	   	$(document).dialog({
									    overlayClose: true,
									    content: '您已经是合格投资者',
									}); 
			        	  	   }else{
				        	  	   var dialog=$(document).dialog({
								        type : 'confirm',
								        closeBtnShow: false,
								        content: '根据监管要求，需要您完成合格投资者认证',
								        buttonTextConfirm:"线下认证",
								        buttonTextCancel:"线上认证",
								        onClickConfirmBtn: function(){
								             dialog.close()
								        },
								        onClickCancelBtn : function(){
								             location.href="/WeixinService/business/qualified/qualifiedNew.shtml";
								        }
								    });     
			        	  	   }
			        	  }
			           }
				       })
      	  	   	   }
    	  }else if(data.returnCode=="9005"){
    	    $(document).dialog({
			    closeBtnShow: false,
			    content: '请进行实名认证',
			    onClickConfirmBtn: function(){
			       location.href="/WeixinService/business/bank/bankAuthNew.shtml";
			    }
			}); 
    	  }else{
    	  	$(document).dialog({
			    overlayClose: true,
			    content: data.returnMsg,
			}); 
    	  } 
       }
   })
}
    // 持仓市值 提示按钮
   function holdAmountTip() {
        $('#back_pop').show()
        $('#holdAmountBox').show()
    }

function closeHoldTip(){
    $('#holdAmountBox').hide()
    $('#back_pop').hide()
}

    

// 是否开启合格投资者功能
function openQualified(){
    var param=queryParamList("SYSTEM","ACINVCONF","");
	var pmnm=""
	for(var i=0;i<param.length;i++){
		if(param[i].pmco=="MAIN"){
			pmnm=param[i].pmnm;
		}
    }
	if(pmnm=="0"){  //为0时不显示菜单
		$(".qualified-info").hide()
	}else{
		$(".qualified-info").show()
	}
}
// 是否开启mgm功能
function openMgm() {
	var openMgm=queryParamList("SYSTEM","enableIntegral","")[0].pmco
	if(openMgm=="1"){
		$(".mygift").show()
	}else{
		$(".mygift").remove()
	}
}