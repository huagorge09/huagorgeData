﻿﻿﻿﻿var fundId = "";
var tpassCount = 0;
var riskWarnGoonFlag = true;
//高风险提示 购买读秒
var timer = 15;
var isHasBalence=false;
$(document).ready(function(e) {
	queryUserInfo();
	$(".header h2").html("支付");
	document.title = "支付";
	 var winHeight=$(window).height()
     var buy_btnHeight=$(".buy_btn").height()
     var buy_btnTop=winHeight-buy_btnHeight
     $(".buy_btn").css("top",buy_btnTop);   
	
	queryTradeInfoByTradeNo();
	queryFundInfo();
	//加载购买 高风险提示
	assemblyHighRiskHtml();

	/*20180103crm数据迁移需求, 移除查询旧版PDF合同*/
	/* 新版本合同 */
	queryFundContractById();

	getRevelation();// 页面开始请求中基协风险揭示书改造数据
	
});

/* 查询订单详情 */
function queryTradeInfoByTradeNo() {
	var serialno = getUrlParameter("serialno");
	serialno = serialno.replace("#", "")
	if (serialno == null || serialno == "") {
		errorRemark("没有查询到订单");
		redirectUrl("/WeixinService/business/query/orderListNew.shtml");
		return;
	}
	$.ajax({
		async : false,
		url : "/WeixinService/business/queryTradeInfoByTradeNo.xhtml",
		data : {
			"serialno" : serialno
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			if(data != null && data.returnCode == "9000"){
        		errorRemark("没有查询到订单");
        		redirectUrl("/WeixinService/business/query/orderListNew.shtml");
        		return;
        	}
			var dto, fundInfo;
			if (data != null) {
				dto = data.dto;
			}
			if (dto != null) {
				fundInfo = dto.fundInfoDtoV2;
			}

			fundId = fundInfo.fundId;
			if(fundInfo.typeId=='0110'){
				$('#payMethod').show();
				$('#XnCome').show();
				   $('#XnCome li').on('click', function (e) {
			            var $this = $(e.currentTarget);
			            var index = $this.index();
			            $this.addClass('act');
			            $this.siblings().removeClass('act');
			            $('.inCome-tips').hide();
			            $('#inComeTips' + index).show();
			        });
			}else{
				$('#payMethod').hide();
			}
			
			var typeId= fundInfo.typeId;
        	if(typeId == '0500' || typeId == '0400' || typeId == '0110'){
        		isHasBalence = queryUserWarehouse(fundId);
        	}
        	$('#sartBuying').val(fundInfo.sartBuying);
        	
			$("#fundRisklevel").val(fundInfo.fundRisklevel);
			$("#serialno").val(dto.serialno);
			$("#fundId").val(fundInfo.fundId);
			$("#period").val(fundInfo.period);
			$("#typeId").val(fundInfo.typeId);
			$("#adName").val(fundInfo.adname);
			$("#fundName").html(fundInfo.typeName + "-" + fundInfo.adname);
			if(isHasBalence) {
				$("#moneyZero").val(fundInfo.sartBuying);
				if(parseFloat(fundInfo.moneyStep) >= 10000){
					$("#buyRemark").html(numDiv(fundInfo.sartBuying, 10000) + "万起购，" + numDiv(fundInfo.moneyStep, 10000) + "万递增");
				}else{
					$("#buyRemark").html(numDiv(fundInfo.sartBuying, 10000) + "万起购，" + fundInfo.moneyStep + "元递增");
				}
			}else {
				$("#moneyZero").val(fundInfo.money);
				if(parseFloat(fundInfo.moneyStep) >= 10000){
					$("#buyRemark").html(numDiv(fundInfo.money, 10000) + "万起购，" + numDiv(fundInfo.moneyStep, 10000) + "万递增");
				}else{
					$("#buyRemark").html(numDiv(fundInfo.money, 10000) + "万起购，" + fundInfo.moneyStep + "元递增");
				}
			}
			/* 预约金额 初始值认购起点 */
			$("#money").val(formatNumber(dto.subamt, ','));
			$("#productMoney").val(fundInfo.money);
			$("#moneyStep").val(fundInfo.moneyStep);
			/* 剩余额度 */
			$("#displayLimit").val(fundInfo.displayLimit);
			/* 年化收益 */
			$("#profit").val(fundInfo.profit);
			$("#startDate").val(fundInfo.interestDate);
			$("#endDate").val(fundInfo.maturityDate);
			$("#appointEndDate").val(fundInfo.subdeadLine);
			$("#apkind").val(fundInfo.fundState);
			$("#renew").val(dto.renew);
			var temp = dto.benefit || 0;

			if (temp == 0) {
				$("#inCome").hide();
			} else {
				/* 计提基准 */
				$("#expectInCome").text('--');
			}
			var commro = dto.commro || 0;
			var display = $("#inCome").css("display");
			$("#fundRateInput").val(data.rate);
			if(display == "none"){
				if(data.rate  > 0){
						$('#fee').show();
						$('#fee').html('<span class="fr"><b>认购费：</b><em>￥</em><em id="fundRate">0</em></span>');
						/*
						$("#fundRate").parent().hide();
						$("#XnCome").removeClass("fl").addClass("fr").css("float","left").show("");
						$("#XnCome b").html("认购费：");
						$("#XnCome em").html("￥");
						/*$("#expectInCome").html(data.rate);*/
						$("#fundRate").html(data.rate);
				}else{
						$('#fee').html('<span class="fr" style="float:left;"><b>官网直销无需认购费</b></span>');
				}
			}else{
				if(data.rate > 0){
					$('#fee').show();
					$('#fee').html('<span class="fr"><b>认购费：</b><em>￥</em><em id="fundRate">0</em></span>');
					$("#fundRate").html(data.rate);
				}else{
					$('#fee').hide();
				}
			}
			/*if (dto.commro == 0) {
				$("#fundRate").parent().html("<b>官网直销无需认购费</b>").css("float","left");
				$("#fundRate").parent().hide();
			}else{
				$("#fundRate").parent().hide();
				$("#inCome").removeClass("fl").addClass("fr").css("float","left").show("");
				$("#inCome b").html("认购费：");
				$("#inCome em").html("￥");
				$("#expectInCome").html(data.rate);
			}*/
			$("#commro").val(dto.commro);
			var countMoney = parseFloat(dto.subamt) + parseFloat(dto.commro || 0);
			$("#tradeAmt").val(countMoney);
			$("#countMoney").html(formatNumber(countMoney, ',') + "元");
			var temp_1 = toUpperCase(countMoney);

			if (temp_1 == "errorMoney" || temp_1 == "moneyMax") {
				errorRemark("金额格式有误");
				return;
			} else {
				temp_1 = temp_1.replace("元整", "元");
			}
			$("#countMoneyText").html(temp_1);

			var today = fundInfo.currentWorkdate;
			/* 按钮 */
			if (daysBetween(today, fundInfo.appointEndDate) > 0) {
				$("#buyType").val("2");/* 认购单 */
			} else {
				if (displayLimit == 0) {/* 预约完了 */
					$("#buyType").val("4");/* 排队单 */
				} else {
					$("#buyType").val("1");/* 预约单 */
				}
			}

			if (typeof (fundInfo.templetId) != undefined) {
				$("#templetId").val(fundInfo.templetId);
			}

		}
	});
}

function goToFundInfo(){
	var id = $("#fundId").val();
	redirectUrl("/WeixinService/business/query/fundInfoNew.shtml?fundId="+id);
}


/*  查询电子合同 */
function queryFundContractById() {
	var fundId = $("#fundId").val();
	var period = $("#period").val();

	$.ajax({
		async : false,
		url : "/WeixinService/business/queryFundContractById.xhtml",
		data : {
			"fundId" : fundId,
			"status" : "N",
			"period" : period
		},
		dataType : "json",
		cache : false,
		type : "POST",
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == '0000') {
				var htmls = "";
				/* 新版本合同 */
				if (data.fundContractDto != null) {
					$("#contractVer").val(data.fundContractDto.version);
					htmls = "<a href='javascript:goToContract(\"/WeixinService/business/query/fundContractNew.shtml?templet=" + fundId + "&period="+period+"\")'>《" + data.fundContractDto.templateName + "》</a>";
				} else {
					$("#contractStatus").val("N");
					errorRemark("未找到产品合同");
				}
				htmls += "<a href='javascript:goToContract(\"/WeixinService/business/query/fundRiskScriptNew.shtml?1=1\")'>《风险揭示函》</a>";
				var invprtp = $("#invprtp").val();
				var invprtpScore = $("#invprtpScore").val();
				if("0" != invprtp || !invprtpScore || 60 > invprtpScore ){
					htmls += "<a href='javascript:goToContract(\"/WeixinService/business/query/commonInvstTradeInfoNew.shtml?1=1\")'>《普通投资者交易告知书》</a>";
				}
				if (htmls != null && htmls != "") {
					$("#econtract").append(htmls);
				}
			} else {
				errorRemark("合同加载失败，请稍后再试");
			}
		}
	});
}

/* 跳转到新版产品合同详情页 */
function goToContract(url) {
	var money = unformat($("#money").val());
	window.location.href = url + "&money=" + money;
}

$("#money").change( function() {
	checkedMoney();
});

/* ------- 选择银行卡页面js ------- */

function initDiv2() {
	document.title="选择支付方式";
    $(".header h2").html("选择支付方式");
	$("#selectBankSection").show();
	$("#moneyText").html($("#countMoneySpan").html());
	queryMyBankCard();
}


/* 我的银行卡信息 */
function queryMyBankCard() {
	var fundid = $("#fundId").val();
	$.ajax({
		async : true,
		url : "/WeixinService/business/queryMyBankCardNo.xhtml",
		data : { fundid : fundid },
		dataType : "json",
		cache : false,
		type : "POST",
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {

			if (data.returnCode == "0000") {

				var htmls1 = '';
				var htmls2 = '';
				var onclickVal = '';
				if (data.tradeAcctlist != null) {

					$.each(data.tradeAcctlist,function(i, item) {
						onclickVal = "sureCard('f" + i+ "');";
						if(parseFloat(item.balance) > 0){
							htmls1 += "<li id=\"f"+ i+ "\" data-tradeAcc='"+ item.tradeAcco+ "' data-bankNo='"
							+ item.bankAccoDisplay+ "' data-bankName='"+ item.bankNm+ "' data-amountOneDay='"
							+ item.dtAmtLimit+"' data-balance='"+ item.balance+ "' onclick=\""+ onclickVal+ "\">"+ item.bankNm
							+ "(尾号"+ item.bankAccoDisplay.subString(item.bankAccoDisplay.length - 4)+ ")<span>仅支持线下汇款</span></li>";
						}else{
							htmls2 += "<li id=\"f"+ i+ "\" data-tradeAcc='"+ item.tradeAcco+ "' data-bankNo='"
							+ item.bankAccoDisplay+ "' data-bankName='"+ item.bankNm+ "' data-amountOneDay='"
							+ item.dtAmtLimit+"' data-balance='"+ item.balance+ "' onclick=\""+ onclickVal+ "\">"+ item.bankNm
							+ "(尾号"+ item.bankAccoDisplay.subString(item.bankAccoDisplay.length - 4)+ ")<span>仅支持线下汇款</span></li>";
						}
					});

					$("#bankList_off").html(htmls1 + htmls2);
					$("#bankList_off li:eq(0)").click();
				} else {
					errorRemark("未绑定银行卡，请先绑定银行卡");
					window.setTimeout('redirectUrl("/WeixinService/business/bank/bankAuthNew.shtml")',2000);
				}

			} else if (data.returnCode == "8000") {
				/* session失效，重新登录 */
				errorRemark(data.returnMsg);
				window.setTimeout("goToLogin()", 2000);
			} else {
				errorRemark("网络繁忙，请稍后再试。");
			}

		}
	});
}

/* 确定选择银行卡 */
function sureCard(_id) {
	$("#cardNums").attr({
		"data-tradeAcc" : $("#" + _id).attr("data-tradeAcc")
	});
	$("#cardNums").attr({
		"data-bankNo" : $("#" + _id).attr("data-bankNo")
	});
	$("#cardNums").attr({
		"data-bankName" : $("#" + _id).attr("data-bankName")
	});
	$("#cardNums").attr({
		"data-cardId" : _id
	});
	$("#cardNums").attr({
		"data-balance" : $("#" + _id).attr("data-balance")
	});
	$("#cardNums").html(
	$("#" + _id).attr("data-bankName")+ "(尾号"+ $("#" + _id).attr("data-bankNo").subString($("#" + _id).attr("data-bankNo").length - 4) + ")");

	$("#bankList_off li").removeClass("act");
	/* $("#bankList_on li").removeClass("active"); */
	$("#" + _id).parents("ul").addClass("isActive");
	/* $("#n"+_id.subString(1)).addClass("active"); */
	$("#f" + _id.subString(1)).addClass("act");

	var bankLimitAmountOneDay = parseFloat($("#" + _id).attr("data-amountOneDay") || 0);
	/* 把选中的银行卡的单日限额，放入隐藏的input中 */
	$("#payLimitation").val(bankLimitAmountOneDay);
	var typeId = $("#typeId").val();
	var balance = $("#" + _id).attr("data-balance");
	if((typeId == '0500' || typeId == '0400' || typeId == '0110') && parseFloat(balance) > 0){
		$("#moneyZero").val($("#sartBuying").val());
	}else{
		$("#moneyZero").val($("#productMoney").val());
	}
	setTimeout("$('.cover-bg').hide()",500);
}

$('#bank_x').on('click', function() {
	$('.cover-bg').not("#tips_01,#high_risk_tips_02,#risk_properTips,#risk_againTips").show();
});

$('#cover-content-x').on('click', function() {
	$('.cover-bg').hide();
});

/*$('.cover-bg').on('click', function() {
	setTimeout("$('.cover-bg').hide()",500);
});*/

/* 去验证密码页面 */
function goToVerifyPassword() {
	var money = parseFloat(unformat($("#money").val()));
	var moneyZero = parseFloat(unformat($("#moneyZero").val()));/* 认购起点 */
	var balance = parseFloat($("#cardNums").attr("data-balance"));
	
	if(balance <= 0 && money < moneyZero){
		errorRemark("该银行卡号无产品在途金额<br />请更换银行卡");
		return;
	}
	
	var payType = $("#payType").val();
	if (payType == "0") {
		errorRemark('暂未开通线上支付，请选择线下支付');
		return;
	}

	$("#selectBankSection").hide();
	$("#verifyPasswordSection").show();
	document.title="提交订单";
    $(".header h2").html("提交订单");
}

/* 购买 */
function fundTrade() {
	var fundId = $("#fundId").val();
	var tradeAcco = $.trim($("#cardNums").attr("data-tradeAcc"));
	var serialno = $("#serialno").val();
	var tradeAmt = $("#productMoney").val(); /* 认购金额 */
	/* var tradeAmt = $("#tradeAmt").val(); */
	var fee = $.trim($("#fundRateInput").val());
	var commro = $("#commro").val();
	var contractVer = $("#contractVer").val();
//	var tPassword = $("#tPassword").val();
	var payType = $("#payType").val();
//	if(!checkedTpsw(tPassword)){
//		return;
//	}
	 var renew=$('#payMethod li.act').attr('data-value');
	 if(!!!renew){
		 renew = 'Y';
	 }

	 if(!checkedMoney()){
			return ;
	}
	 
	 if( $('#typeId').val()=='0110' && renew==null || renew== ''){
			errorRemark('请勾选并确认续投方式');
			return;
		}
	if (payType == "0") {
		errorRemark('暂未开通线上支付，请选择线下支付');
		return;
	}

	var params = {
		'tradeAcco' : tradeAcco,
//		'tPassword' : tPassword,
		'fundId' : fundId,
		'serialno' : serialno,
		'apkind' : 'A2T',
		'tradeAmt' : encodeURI(tradeAmt),
		'fee' : fee,
		'commro' : commro,
		'payType' : payType,
		'contractVer' : contractVer,
		'renew' : renew
	};

	$.ajax({
		async : false,
		url : "/WeixinService/business/fundTrade.xhtml",
		data : params,
		type : "post",
		dataType : 'json',
		cache : false,
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data, textStatus) {
			var returnCode = data.returnCode;
			if (returnCode == "0000") {
				tpassCount = 0;/* 输入正确，将错误次数清零 */
				$("#verifyPasswordSection").hide();

				if (payType != null && payType == 1) {/* 线下 */
					var tempCardNo = $("#cardNums").attr("data-bankNo").subString($("#cardNums").attr("data-bankNo").length - 4);
					$("#offLineBankNo").text(tempCardNo);
					$("#offLineBankName").text($("#cardNums").attr("data-bankName"));

					$("#offLineSuccessSection").show();

					sendSmsMsg();
				} else {/* 线上 */
					/* 暂未开通线上支付 */
					/* $("#verifyCardSuccess").show(); */
				}

			} else if (returnCode == "USR-1I01") {
				errorRemark("错误次数过多<br>3小时后重试！");
			} else if (returnCode == "USR-1I02") {
				tpassCount++;
				if (tpassCount < 2) {
					errorRemark("安全码有误");
				} else {
					errorRemark("安全码有误<br>还有" + (6 - tpassCount) + "次机会");
				}
			} else if (returnCode == "USR-1I95") {
				errorRemark("非交易用户，请完善信息并绑定银行卡");
			} else if (data.returnCode == "8000") {
				errorRemark(data.returnMsg);
				/* 跳转到登陆页面，写一个公用的方法，判断微信和其他ie，分别跳转到不同的登陆页面 */
				window.setTimeout("goToLogin()", 2000);
			} else {
				errorRemark("交易失败:" + data.returnMsg);
			}
		}
	});
}

/* 确认线下汇款，给用户发送通知信息  */
function sendSmsMsg() {

	var bankNumber = $.trim($("#cardNums").attr("data-bankNo"));
	var bankName = $.trim($("#cardNums").attr("data-bankName"));
	bankName = encodeURI(bankName);
	var money = $("#tradeAmt").val();
	var fundName = $("#adName").val();
	fundName = encodeURI(fundName);
	var buyType = $.trim($("#buyType").val());
	var appointEndDate = $.trim($("#appointEndDate").val());
	var msgType = "11";
	if (buyType == "1") {
		msgType = "10";
		appointEndDate = dateformat(appointEndDate);
		var apkind = $("#apkind").val();
		if (apkind == "0") {
			appointEndDate += "15点";
		} else {
			appointEndDate += "17点";
		}
		appointEndDate = encodeURI(appointEndDate);
	}

	var params = {
		"bankNumber" : bankNumber,
		"bankName" : bankName,
		"money" : money,
		"fundName" : fundName,
		"msgType" : msgType,
		"appointEndDate" : appointEndDate,
		"serialNo" : getUrlParameter("serialno")
	};

	var url = "/WeixinService/setUp/sendSmsMsg.xhtml";

	$.ajax({
		async : false,
		url : url,
		data : params,
		type : 'post',
		dataType : "json",
		cache : false,
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				/* alert("线下购买通知消息发送成功"); */

			} else {
				/* alert("线下购买通知消息发送失败"); */

			}
		}
	});

}

function toMyaccount(){
	addCookie('riskUrl',location.href);
	redirectUrl('/WeixinService/business/user/riskLevelNew.shtml');
}

function toNext() {

	var invprtp = $("#invprtp").val();
	var invprtpScore = $("#invprtpScore").val();
	var isControl = $("#isControl").val();
	var isNotBeneficiary = $("#isNotBeneficiary").val();
	var isBadHonesty = $("#isBadHonesty").val();
	var specialRiskLevel = $("#specialRiskLevel").val();
	var contractStatus = $("#contractStatus").val();
	if (contractStatus == "N") {
		errorRemark('未找到产品合同');
		return;
	}
	if("Y"==isControl || "Y"==isNotBeneficiary || "Y"==isBadHonesty){
		$("#risk_properTips").show();
		return ;
	}
	
	if(!specialRiskLevel){
		specialRiskLevel = "5";
	}
	
	if(!checkedMoney()){
		return ;
	}
	
	 var renew=$('#payMethod li.act').attr('data-value');

	 if( $('#typeId').val()=='0110' && renew==null || renew== ''){
			errorRemark('请勾选并确认续投方式');
			return;
		}
	if ($("#isok").is(":checked") == false) {
		errorRemark('请勾选并确认相关产品合同');
		return;
	}
	
	//（用户级别<产品级别 ||  特殊用户等级 < 产品等级） 且 需要提示 且 不是专业级
	if((riskLevel < fundRisklevel || specialRiskLevel < fundRisklevel)  && riskWarnGoonFlag && ("0" != invprtp || 60 > invprtpScore)){
		$("#tips_01").show();
		return;
	}
	
	/**中基协风险揭示书改造需求---添加--开始**/
	$("#div_06,#bocPay_div_03").show();	
	$('.confirm').click(function(){
		var valueData = nextStep();
		if(valueData && ($('.confirm').hasClass('valData'))){
			$("#buyMoneySection").hide();
			initDiv2();
		}
	});
	/**中基协风险揭示书改造需求---添加--结束**/

	// 中基协风险揭示书改造需求注释方法是原有逻辑
	// $("#buyMoneySection").hide();
	// initDiv2();
}
/*查询用户信息  此处主要查询是否为30用户*/
function queryUserInfo() {
	var url = "/WeixinService/business/queryUserinfo.xhtml";
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
				$("#riskLevel").val(data.riskLevel);
				$("#invprtp").val(data.invprtp);
				$("#invprtpScore").val(data.invprtpScore);
				$("#isControl").val(data.isControl);
				$("#isNotBeneficiary").val(data.isNotBeneficiary);
				$("#isBadHonesty").val(data.isBadHonesty);
				$("#specialRiskLevel").val(data.specialRiskLevel);
				if(data.userType == null || data.userType != "30"){
					errorRemark("请您完成实名鉴权");
					window.setTimeout("redirectUrl('/WeixinService/business/bank/bankAuthNew.shtml')", 1000);
				}else if(data.isSetTradePassword == null || data.isSetTradePassword != "Y"){
					errorRemark("请先设置安全码");
					window.setTimeout("redirectUrl('/WeixinService/business/user/setTPasswordNew.shtml')", 1000);
				}
			} else if (data.returnCode == "8000") {
				errorRemark(data.returnMsg);
				/* 跳转到登陆页面，判断微信和其他ie，分别跳转到不同的登陆页面 */
				window.setTimeout("goToLogin()", 1000);
			} else {
				errorRemark("网络繁忙，请稍后再试");
			}
		}
	});
}
/* 检查【安全码】 */
function checkedTpsw(tPassword){
	var tpswLwngth = tPassword.length;
	if(tPassword == null || tPassword == ""){
		errorRemark("请输入安全码！");
		return false;
	}else if(tpswLwngth < 6 || tpswLwngth > 16){
		errorRemark("请输入6位以上数字<br>+字母的安全码");
		return false;
	}else if(Validater.hasNullCharacter(tPassword)) {
		errorRemark("不支持空字符，建议使用数字符号字母组合！");
		return false;
	} else if(!Validater.isTradePasswordNew(tPassword)){
		errorRemark("请输入6位以上数字<br>+字母的安全码");
		return false;
	}else{
		return true;
	}
}
function goToAddBank(){
	var param=getUrlParameter("serialno");
	window.location.href="/WeixinService/business/bank/addBankNew.shtml?serialno="+param;
}

/**
 * 高风险 购买 风险提示
 */
function assemblyHighRiskHtml(){
	var riskLevel = $("#riskLevel").val();
	var fundRiskLevel = $("#fundRisklevel").val();
	var specialRiskLevel = $("#specialRiskLevel").val();
	//设置风险提示框内容
	var riskHtml = "";
	if(riskLevel == "0" || "1" == specialRiskLevel){/*保守   默认    未评级的*/
		riskHtml = "产品与您的风险等级（C1-保守型）不匹配，您可以购买其他产品或重新进行测评？";
   	}else if(riskLevel != null && riskLevel == "1"){/*保守型*/
		riskHtml = "产品与您的风险等级（C1-保守型）不匹配，是否继续购买？";
   	}else if(riskLevel == "2"){/*稳健型*/
		riskHtml = "产品与您的风险等级（C2-稳健型）不匹配，是否继续购买？";
   	}else if(riskLevel == "3"){/*平衡型*/
		riskHtml = "产品与您的风险等级（C3-平衡型）不匹配，是否继续购买？";
   	}else if(riskLevel == "4"){/*成长型*/
		riskHtml = "产品与您的风险等级（C4-成长型）不匹配，是否继续购买？";
   	}else if(riskLevel == "5"){/*积极型*/
		riskHtml = "产品与您的风险等级（C5-积极型）不匹配，是否继续购买？";
   	}
	
	if(!specialRiskLevel){
		specialRiskLevel = "5";
	}
	//最低等级客户 回退到详情页面
	var riskWarnGoon = $("#weixinRiskBtn");
	var riskAgainBtn = $("#riskAgainBtn");
    if(1 >= specialRiskLevel ){
    	riskWarnGoon.html("再看看");
    	riskWarnGoon.bind("click",function(){
    		history.go(-1);
    	});
    }else{
    	riskWarnGoon.html("继续购买");
    	riskWarnGoon.bind("click",function(){
    		cancleTips("tips_01");
    		showTips("risk_againTips");
    	});
    	
    	riskAgainBtn.html("继续购买");
    	riskAgainBtn.bind("click",function(){
    		riskWarnGoonFlag = false;
    		cancleTips("risk_againTips");
    		payHighRiskPrompt();
    	});
    }
    
    if(riskLevel >= fundRiskLevel){
    	riskWarnGoonFlag = false;
    }
    
	$("#weixinRiskHtml").html(riskHtml);
}


function countDown(){
	timer--;
	if(timer == 0){
		
		$('#highRiskBtn').html("我知道了").css({color:"#fff"});
		$('#highRiskBtn').css({background:"#ca132c"});
		$("#highRiskBtn").bind("click",function(){
			$("#high_risk_tips_02").hide();
			toNext();
		});
		timer = 15;
	}else{
		$("#highRiskBtn").unbind("click");
		$('#highRiskBtn').html("我知道了("+timer+"s)").css({color:"#4c4c4c"});
		$('#highRiskBtn').css({background:"#ededed"});
		setTimeout('countDown()',1000);
	}
}

function payHighRiskPrompt(){
	var fundRisklevel = $("#fundRisklevel").val();
	var invprtp = $("#invprtp").val();
	var invprtpScore = $("#invprtpScore").val();
	var highRiskExplain = $("#highRiskExplain").val();
	if("5" == fundRisklevel && ("0" != invprtp || 60 > invprtpScore)){
		$('#highRiskBtn').html("我知道了("+timer+"s)").css({color:"#4c4c4c"});
		$("#high_risk_tips_02 #high_risk_content").html(highRiskExplain);
		$("#high_risk_tips_02").show();
		countDown();
	}else{
		toNext();
	};
}

function queryFundInfo() {
	var fundId = $("#fundId").val();
	var period = $("#period").val();
	$.ajax({
		async : false,
		url : "/WeixinService/business/queryFundInfo.xhtml",
		data : {
			"fundId" : fundId,
			"period" : period
		},
		dataType : "json",
		type : 'post',
		cache : false,
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == '0000') {
				if (data.fundInfo != null) {
					var fundInfo = data.fundInfo;
					$("#fundRisklevel").val(fundInfo.fundRisklevel);
					$("#appointEndDate").val(fundInfo.subdeadLine);
					$("#appointDate").val(fundInfo.appointDate);
					$("#salesDate").val(fundInfo.salesDate);
	        		$("#subdeadLine").val(fundInfo.subdeadLine);
					$("#highRiskExplain").val(fundInfo.highRiskExplain);
					var renew = $("#renew").val();
					if(renew == "N"){
						$("#redeemTips em:eq(0)").html(formatDate1(fundInfo.maturityDate));
						$("#redeemTips em:eq(1)").html(formatDate1(fundInfo.arrivalAccountDate));
						$("#redeemTips").show();
					}
				} else {
					errorRemark("当前产品不存在");
				}
			} else {
				errorRemark(data.returnMsg);
			}
		}
	});
}

function cancleTips(_id) {
    $("#"+_id).hide();
}

/**
 * 判断预约金额是否符合格式
 */
function checkedMoney(){	
	var money =parseFloat(unformat($("#money").val()));
	var moneyStep = parseFloat(unformat($("#moneyStep").val()));/* 认购步长 */
	var moneyZero = parseFloat(unformat($("#moneyZero").val()));
	if(money >= 1000000000){
		errorRemark("输入金额过大，请重新输入");
		return false;
	}else if(money < moneyZero || (money - moneyZero) % moneyStep != 0){
		if(parseFloat(moneyStep) >= 10000){
			errorRemark("本产品" + numDiv(moneyZero, 10000) + "万起购，" + numDiv(moneyStep, 10000) + "万递增");
		}else{
			errorRemark("本产品" + numDiv(moneyZero, 10000) + "万起购，" + moneyStep + "元递增");
		}
		return false;
	}else {		
		return true;
	}	
}

/**
 * 查询用户是否有持仓
 */
function queryUserWarehouse(fundId){
	var flag = false;
	 $.ajax({
    	async:false,
		url:"/WeixinService/business/queryCustTradeInfo.xhtml",
		type:"post",
		dataType:'json',
		data:{
			"fundCode":fundId
		},
		success:function(res){
		     if(res.returnCode=="0000"){
		    	if(res.buySatte=="Y"){
		    		flag = true;
		    	}
		     }
		},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
			flag = false;
	   }
	});
	 return flag;
}









// 中基协风险揭示书---获取数据
function getRevelation(){
	// var fundId = getUrlParameter("fundId");
	// var fundId = $("#fundDta").val();
	var period=getUrlParameter("period");
	$.ajax({
        url: '/WeixinService/business/queryRiskTermList.xhtml',
        data: {
			fundId: fundId,
			period: period
		},
        dataType: 'json', //服务器返回json格式数据
		type: 'post', //HTTP请求类型
		cache: false,
        success: function(data){
			$("#revelation_data").append(renderingDom(data.data));

			var valueData = nextStep();
			if(valueData){
				$('.confirm').show().addClass('valData');
				$('.allReading').hide();
			}else{
				$('.allReading').show();
				$('.confirm').hide().removeClass('valData');;
			}
			$("#checkedHide,#spanHide").html('');

        }

    })

}

function renderingDom(arr){
	var list_arr=[];
	for(var i=0;i<arr.length;i++){
		var item = arr[i]
		list_arr.push(`<li><div class="li_input" id="${item.id == 0 ? 'checkedHide':''}"><label><input type="checkbox" ${item.state =='' ? '': 'checked'} name="${item.id == 0 ? '':'checkbox'}" id="${item.id}" value="${item.id}" /> <div class="show-box"></div></label></div><div class="li_p"><p><span id="${item.id == 0 ? 'spanHide':''}">${item.id}, </span>${item.content}</p></div></li>`)
	}
	return list_arr
}
// 全部阅读
$(".allReading").click(function(){
	$('input[name="checkbox"]').prop('checked', true);
	$('.confirm').show().addClass('valData');
	$('.allReading').hide();
});
$(".confirm").click(function(){
	var chk_value =[];
	var submit = 'submit';
	$('input[name="checkbox"]:checked').each(function(){  
		chk_value.push($(this).val());  
	});
	returnState(chk_value, submit);
	$("#div_06,#bocPay_div_03").hide();
});
// 取消
$(".bottom_left").click(function(){
	var chk_value =[];
	var ant;
	$('input[name="checkbox"]:checked').each(function(){  
		ant = $('input[name="checkbox"]:checked')
		chk_value.push($(this).val());    
	});
	// chk_value数组必须要有值，否则后台会报错。
	if(chk_value.length > 0){
		returnState(chk_value);
	}
	$("#div_06,#bocPay_div_03").hide();
});
// 遍历
$("#revelation_data").on("change",'input[name="checkbox"]',function(){
	var valueData = nextStep();
	if(valueData){
		$('.confirm').show().addClass('valData');
		$('.allReading').hide();
	}else{
		$('.allReading').show().removeClass('valData');
		$('.confirm').hide();
	}
});
function nextStep(){
	var chks = document.querySelectorAll('input[name="checkbox"]');
	var result = [];
	for (var i = 0; i < chks.length; i++) {
		var chk = chks[i];
		result.push(chk.checked);
	} 
	var valueData = result.every((v,i)=>{
		return v
	})
	return valueData
}
// 确认和取消都需要把选择checkbox的状态返回后台
function returnState(ids,type){
	var idst = ids.join(',');
	// var fundId = getUrlParameter("fundId");
	// var fundId = $("#fundDta").val();
	var period=getUrlParameter("period");
	$.ajax({
        url: '/WeixinService/business/saveUserTermsInfo.xhtml',
        data: {
			fundId: fundId,
			ids: idst,
			type: type,
			period: period
		},
        dataType: 'json', //服务器返回json格式数据
		type: 'post', //HTTP请求类型
		cache: false,
        success: function(data){

        }

    })

}