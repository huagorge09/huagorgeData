var t_delay;

$(document).ready(function(e) {
	document.title = "风险测评_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	/* 风险测评  选项点击控制 */
	$(".risk-con-list ul li span.list").click(function(){
		$(this).removeClass("act").siblings().removeClass("act");
		$(this).addClass("act");
		var dataInput = $(this).attr("data-input");
		if("Y"==dataInput){
			$(this).parent().find("div").show();
		}else{
			$(this).parent().find("div").find("input").val("");
			$(this).parent().find("div").hide();
		}
		var subjoinFlag = true;
		var isControl = $(".risk-con-list ul li:eq(13) span.act input").val();
		var isNotBeneficiary = $(".risk-con-list ul li:eq(14) span.act input").val();
		var isBadHonesty = $(".risk-con-list ul li:eq(15) span.act input").val();
		
		if((undefined != isControl && !isControl) || (undefined != isNotBeneficiary && !isNotBeneficiary) || (undefined != isBadHonesty && !isBadHonesty) ){
			subjoinFlag = false;
		}
		
		//必填 说明
		// href="javascript:riskLevel();" 
		var len = $(".risk-con-list ul li span.list.act").length;
		if(len != null && len >= 18 && subjoinFlag){
			$("a.btn").removeClass("act");
			$("a.btn").attr("href","javascript:riskLevel();");
		}else{
			$("a.btn").addClass("act");
			$("a.btn").attr("href","#");
		}
	});
});

$(function () {
    setTimeout(function () {
    	queryDateOfBirth();
    }, 3000);
});

/*重置选项*/
function resetOption(){
	$(".risk-con-list ul li span.list div").hide();
	$(".risk-con-list ul li span.list div input").val("");
	$(".risk-con-list ul li span.list").removeClass("act");
	$("a.btn").addClass("act");
}
/*传索引值查询对应选中的值--data-score*/
function getThisVal(index){
	return  $(".risk-con-list ul li:eq("+index+") span.act").attr("data-score");
}

/*传索引值查询对应选中的值--data-value*/
function getUserThisValDesc(index){
	var value= $(".risk-con-list ul li:eq("+index+") span.act").attr("data-value");
	if(!!value){
		return  ":"+value;
	}
	return ":";
}

/*传索引值查询对应输入的值-*/
function getUserThisInputVal(index){
	var value = $(".risk-con-list ul li:eq("+index+") span.act input").val();
	if(!!value){
		return  ":"+value;
	}
	return ":";
}

/*风险测评提交*/
function riskLevel(){
	var len = $(".risk-con-list ul li span.list.act").length;
	var evalAnswer = 0;/* 临时变量*/
	var val = 0;/* 相加的值*/
	if(len != null && len == 18){
		/* 提交*/
		for(var i = 0; i < len; i++){
			evalAnswer = evalAnswer != "" ? evalAnswer + ',' + parseInt(getThisVal(i),10) + getUserThisValDesc(i) + getUserThisInputVal(i) : parseInt(getThisVal(i),10) + getUserThisValDesc(i) + getUserThisInputVal(i);
			val = parseInt(getThisVal(i),10) + parseInt(val,10);
		}
        var riskLevel = "";	/*等级：风险级别 1-C1-保守型,2-稳健型,3-C5-积极型(0-20:2,21-60:3,61-100:4)*/
        var channelCode = "NET";		/*渠道代码 NET-网站，IVR-自动语音，DIR-直销  (网上交易填写 NET)*/ 
        var evalType = "M";	/*测评类型 T-问卷测评，M-客户自评,网上交易填写 M*/
        var evalFormno = val;		/*填写用户分数*/
        var status = "N";		/*填写N*/
        
      //请问您是否具有完全民事行为能力
        var isCompAbility = $(".risk-con-list ul li:eq(16) span.act").attr("data-value");
        //请问您是否没有风险容忍度或者不愿承受任何投资损失
        var isEndureLoss = $(".risk-con-list ul li:eq(17) span.act").attr("data-value");
        
        if("N" == isCompAbility || "Y" == isEndureLoss){
        	val = 0;
        	evalFormno = val;
        }
        
        /*(20-40:1;41-60:2;61-100:3)*/
        if(val >= 24 && val <=34){
        	riskLevel = "1";
        }else if(val >= 35 && val <=48){
        	riskLevel = "2";
        }else if(val >= 49 && val <=62){
        	riskLevel = "3";
        }else if(val >= 63 && val <=80){
        	riskLevel = "4";
        }else if(val > 81){
        	riskLevel = "5";
        }else{
        	riskLevel = "1";
        }
        
        var revenue = $("#risk_cover_checkbox").get(0).checked;
    	if(!revenue){
    		show_tips("请阅读并勾选声明！");
    		return;
    	}
        
		$.ajax({
			type: "POST",
        	async:false,
            url: "/AppService/business/setUserRiskLevel.xhtml",
            dataType: "json",
            data: {
                "riskLevel":riskLevel,		/*等级：风险级别 1-C1-保守型,2-稳健型,3-C5-积极型(21-40:1,41-60:2,61-100:3)*/
                "channelCode":channelCode,		/*渠道代码 NET-网站，IVR-自动语音，DIR-直销  (网上交易填写 NET)*/ 
                "evalType":evalType,		/*测评类型 T-问卷测评，M-客户自评,网上交易填写 M*/
                "evalFormno":	evalFormno,		/*填写用户分数*/
                "status":	status,			/*填写N*/
                "evalAnswer": evalAnswer
            },
            cache: false,
            error : function(textStatus, errorThrown) {  
                show_tips("网络繁忙，请稍后再试。");  
            }, 
            success : function (data) {
            	var htmls = "";
            	var temp = "";
            	var tempName = "";
            	if(riskLevel == "1"){
            		temp = "C1-保守型（低风险承受能力）";
            		tempName = "低风险产品";
            	}else if(riskLevel == "2"){
            		temp = "C2-稳健型（中低风险承受能力）";
            		tempName = "中低风险及以下产品";
            	}else if(riskLevel == "3"){
            		temp = "C3-平衡型（中风险承受能力）";
            		tempName = "中风险及以下产品";
            	}else if(riskLevel == "4"){
            		temp = "C4-成长型（中高风险承受能力）";
            		tempName = "中高风险及以下产品";
            	}else if(riskLevel == "5"){
            		temp = "C5-积极型（高风险承受能力）";
            		tempName = "高风险及以下产品";
            	}else{
            		tempName = "低风险及以下产品";
            	}
            	if(data != null && data.returnCode == "0000"){
            		$("#riskLeverCustLever_test").html(temp);
            		$("#evalValiDate_test").html("问卷有效期："+data.evalValiDate);
            		$("#evalDate_test").html("测评时间&nbsp;&nbsp;&nbsp;&nbsp;："+data.evalDispDateTime);
            		$("#riskEvel_test").html("适宜产品等级："+tempName);            		
            		$("#risk_div_01").hide();
            		$("#risk_div_02").show();
            		//var flag = switchNo();//财富宝的开关
            		operatingRecord(pageSourceId,pageId,"event_001_commitQuestionId","");
					queryUserIsRealName();               	
					scroll(0,0);/* 回到顶部*/

            	}else{
            		show_tips("评级失败："+data.returnMsg);
            	}
            }
		});
	}else{
		show_tips("请答完题再提交！");
	}
}

//财富宝开关
// function switchNo(){
//
// 	var nowDate = new Date();
// 	var time = nowDate.getFullYear() + "" +((nowDate.getMonth()+1)<10?"0":"")+(nowDate.getMonth()+1)+""+(nowDate.getDate()<10?"0":"")+nowDate.getDate();
// 	var rst = false,userId = queryUserId();
//
// 	var planDate = queryParamList("SYSTEM","PRODPLANDATE","");
// 	//财富宝
// 	if(time <= planDate[0].pmco){
// 		t_delay = setInterval (function(){
// 			if(pageSourceId != '' && pageSourceId != null && pageSourceId != undefined ){
// 				window.location.href="/AppService/business/fund/wealthTreasure.shtml?pageSourceId="+pageSourceId+"&eventId=event_001_commitQuestionId";
// 			} else {
// 				window.location.href="/AppService/business/fund/wealthTreasure.shtml?pageSourceId=personalInformationId&eventId=event_001_commitQuestionId";
// 			}
// 		}, 3000);
// 		rst =  true;
// 	}
// 	//合利
// 	planDate = queryParamList("SYSTEM","HELIDATE","");
// 	if(time <= planDate[0].pmco){
// 		t_delay = setInterval (function(){
// 			if(pageSourceId != '' && pageSourceId != null && pageSourceId != undefined ){
// 				window.location.href="/AppService/business/fund/heli.shtml?pageSourceId="+pageSourceId+"&eventId=event_001_commitQuestionId";
// 			} else {
// 				window.location.href="/AppService/business/fund/heli.shtml?pageSourceId=personalInformationId&eventId=event_001_commitQuestionId";
// 			}
// 		}, 3000);
// 		rst =  true;
// 	}
//    return rst;
//
// }

/**
 * 查询用户是否实名认证
 */
function queryUserIsRealName(){
	$.ajax({
		async : false,
		url : "/AppService/business/queryUserinfo.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				//是否实名
				var userType = data.userType;
				if(userType == null || userType == '10'){
					$("#risk_isRealName a").attr("href","/AppService/business/bank/realName.shtml?pageSourceId="+pageSourceId+"&eventId=event_rating_realNameId");
					$("#risk_isRealName").show();
					$("#risk_div_02 .btn .btn-left").remove();
					t_delay = setInterval (function(){
						$("#risk_isRealName a")[0].click();
                	}, 10000);
				}else{
					$("#risk_isRealName").hide();
					var riskUrl = getCookie("riskUrl");
					if(!riskUrl){
						riskUrl = "/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO";
					}
					addCookie('riskUrl','');
					t_delay = setInterval (function(){
						window.location.href=riskUrl
					},3000);
					
				}
			}
		}
	});
}

function detailTips(){
	clearInterval(t_delay);
	$("#detailText").show();
	pageEventData("01","02");
}
function closeDetailText(){
	$("#detailText").hide();
	pageEventData("01","03");
	queryUserIsRealName();
}

/**
 * 时间格式化年月日
 * @param date
 * @returns
 */
function formatDate(date){
	if(date == null || date == ""){
		return "";
	}
	date = date + "";
	date = unformat1(date);
	return date.substr(0,4)+"年"+date.substr(4,2)+"月"+date.substr(6,2)+"日"+date.substr(8,2)+"时"+date.substr(10,2)+"分";
}

/*重新评测*/
function riskLevel_again(){
	resetOption();
	$("#risk_div_01").show();
	$("#risk_div_02").hide();
	$("#risk_cover_checkbox").attr("checked",false);
	clearInterval(t_delay);
	queryDateOfBirth();
	operatingRecord(pageSourceId,pageId,"event_001_reassessId","");
	scroll(0,0);/* 回到顶部*/
}

//最低年龄限制 民事行为能力 只能选 否
var mayAgeToDay = 16;
function queryDateOfBirth(){
	$(".w_text").val("");
	//出生日期
	var thisVal = $("#birthDateFrm").val().replace(/-/g,"");
	var nowDate= new Date();
	var nowYear =nowDate.getFullYear();
	var nowMonth = nowDate.getMonth() + 1;
	var nowDate = nowDate.getDate();
	if(nowMonth < 10){
		nowMonth = "0" + nowMonth;
	}
	if(nowDate < 10){
		nowDate = "0" + nowDate;
	}

	var limitAge = (parseInt(nowYear)-parseInt(mayAgeToDay)) + "" + nowMonth + nowDate;
	var dataDiff = daysBetween(limitAge,thisVal);
	//时间差
	if(dataDiff < 0){
		$(".risk-con-list ul li:eq(16) span.list:eq(1) div p").html("年龄不满16岁，不具有完全民事行为能力。<a href='/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO'>请根据实际情况填写出生日期。</a>");
		$(".risk-con-list ul li:eq(16) span.list:eq(0)").unbind("click");
		$(".risk-con-list ul li:eq(16) span.list:eq(1)").bind("click");
		$(".risk-con-list ul li:eq(16) span.list:eq(1)").click();
	}else{
		$(".risk-con-list ul li:eq(16) span.list").removeClass("act");
		$(".risk-con-list ul li:eq(16) span.list").bind("click",function(e){optionBindClickEvent(e)});
	}
}
function subjoinChange(element){
	var target = $(element);
	target.parent().click();
	console.log(".")
}