/**
 * 
 */
var realNameFlag = true;
﻿function showTaxResidentInfo() {
	$("#titleBack").click(function(){
		$('#titleBack').attr("href","javascript:redirectUrl('/WeixinService/business/user/userInfo.shtml')");
	});
	$(".header .top-a h2").html("选择税收居民类型");
	
	/*queryUserName();
	if(!realNameFlag){
		errorRemark("请您完成实名鉴权");
		setTimeout(function(){
			redirectUrl("/WeixinService/business/bank/bankAuth.shtml");
		}, 2000);
		return ;
	}*/
	
	var taxResidentType = GetQueryString("taxResidentType");
	var taxResidentTypeNm = GetQueryString("taxResidentTypeNM");
	var taxResidentData = GetQueryString("taxResidentData");
	if(!taxResidentData){
		taxResidentData = queryUserTaxInfo();
	}
	$("#taxResidentType").val(taxResidentType);
	$("#taxResidentTypeNM").val(taxResidentTypeNm);
	$("#taxResidentText").html(taxResidentTypeNm);
	$("#taxResidentData").val(taxResidentData);
	
};

/* 查询用户信息 此处主要查询是否为30用户 */
function queryUserName() {
	var url = "/WeixinService/business/queryUserinfo.xhtml";
	var flag = true;
	$.ajax({
		async : false,
		url : url,
		data : "",
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				if (data.userType == null || data.userType != "30") {
					flag = false;
					realNameFlag = false;
				}
			}
		}
	});
	
	return flag;
}

function goFundTaxResidentTypeView(){
	if(!realNameFlag){
		errorRemark("请您完成实名鉴权");
		setTimeout(function(){
			redirectUrl("/WeixinService/business/bank/bankAuth.shtml");
		}, 2000);
		return ;
	}
	var param = getAllQueryString();
	redirectUrl('/WeixinService/business/user/updateTaxResidentType.shtml' +param);
}

/**
 * 取消出生日期選擇事件
 * @returns
 */
function cacelTaxResidentUpdate(){
	window.location.href="/WeixinService/business/user/userInfo.shtml";
}

function getAllQueryString(){
	var param ="?";
	var taxResidentType = $("#taxResidentType").val();
	var taxResidentTypeNM = $("#taxResidentTypeNM").val();
	var taxResidentData = $("#taxResidentData").val();
	param = param+"&taxResidentType="+taxResidentType+"&taxResidentData="+taxResidentData+"&taxResidentTypeNM="+taxResidentTypeNM;
	return param;
}
/**
 * 获取地址栏传递参数
 * @param name
 * @returns
 */
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


function show_taxResidentDecl(){
	$('#taxResidentDecl_tip').show();
	var height=0.84*window.innerHeight;
	var width=0.9*window.innerWidth;
	var leftwidth=window.innerWidth*0.105/2;
	var leftheight=window.innerHeight*0.083;
	console.info(height);
	console.info(width);
	console.info(leftwidth);
	console.info(leftheight);
	var style={'height':height+'px','width': width+'px','margin-left': leftwidth+'px','margin-top':leftheight+'px'}
	$('#taxResidentDecl_tip').children().eq(0).css(style);

	//计算span left
	 leftwidth  = ($('.span').parent().width()-$('.span').width())/2
	 leftheight=0.03*window.innerHeight;
	$('.span').css('margin-left',leftwidth+'px');
	$('.span').css('top',leftheight+'px');
	$('.span').css('height','45px');

	//设置问题匡主题左边距
	var contentleft=0.035*window.innerWidth;
	var contentWidth=0.835*window.innerWidth;
	var contentHeight =0.56*window.innerHeight;
	var contentStyle={'margin-left':contentleft+'px','margin-right':contentleft+'px','width':contentWidth+'px'/*,'height':contentHeight+'px'*/}
	$('#taxResidentDecl_tip').children().children().eq(1).css(contentStyle);


	//设置按钮样式
	var buttonTop =0.03*window.innerHeight;
	var buttonStyle={'margin-top':buttonTop+'px'}
	$('#taxResidentDecl_tip').children().children().eq(2).css(buttonStyle);
	// $('.foot').hide();
}



function saveTaxResidentInfo(){
	var taxResidentType = $("#taxResidentType").val();
	var taxResidentData = $("#taxResidentData").val();
	
	if(!taxResidentType){
		errorRemark("请完善税收居民信息！");
		return ;
	}
	
	if( !!taxResidentType && ( "1" != taxResidentType && !taxResidentData)){
		errorRemark("请完善税收居民信息！");
		return ;
	}
	var fundRiskDeclConfirm = document.getElementById("fundRiskDeclConfirm").checked;
	if(!fundRiskDeclConfirm){
		errorRemark("请阅读并勾选同意声明！");
		return ;
	}
	$.ajax({
        async: !1,
        url: "/WeixinService/business/updateCmfUserBaseInfo.xhtml",
        data: {
        	taxResidentType : taxResidentType,
        	taxResidentData : taxResidentData
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
            	errorRemark("选择税收居民信息失败！");
            }
        }
    });
}

function queryUserTaxInfo(){
	var taxResidentData = "";
	$.ajax({
        async: !1,
        url: "/WeixinService/business/queryUserTaxInfo.xhtml",
        data: {
        },
        dataType: "json",
        cache: !1,
        type: "POST",
        error: function() {
        	errorRemark("网络繁忙，请稍后再试。");
        },
        success: function(data) {
        	if(!!data){
        		taxResidentData = JSON.stringify(data);
        	}
        }
    });
	return taxResidentData;
}