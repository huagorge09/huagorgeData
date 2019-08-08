var fundId = "";
var tpassCount = 0;
var riskWarnGoonFlag = true;
var isHasBalence=false;
//高风险提示 购买读秒
var timer = 15;
$(document).ready(function(e) {
	queryUserInfo();
	$(".header .top-a h2").html("支付");
	document.title="支付";
	fundId = getUrlParameter('fundId');
	fundId = fundId.replace('#', "");
	var period="";
	period = getUrlParameter('period');
	 /*var winHeight=$(window).height()
     var buy_btnHeight=$(".buy_btn").height();
     var buy_btnTop=winHeight-buy_btnHeight;
     $(".buy_btn").css("top",buy_btnTop);  */ 
	
	queryFundInfo(fundId,period);
	queryFundRate(fundId,$("#productMoney").val(),'');
	//加载购买 高风险提示
	assemblyHighRiskHtml();

	/*20180103crm数据迁移需求, 移除查询旧版PDF合同*/
	/* 新版本合同 */
	queryFundContractById();

	getUserRequest("buy-money-buyMoneySection");/* 此处subPath为页面内行为 */
	periodFundProSelect();
	setFundMinBuyMoney();

	// MGM需求
	var openMgm=queryParamList("SYSTEM","enableIntegral","")[0].pmco
	if(openMgm=="1"){
	    inviterShow() //判断邀请框是否显示
	}
	

	getRevelation();// 页面开始请求中基协风险揭示书改造数据
});
/**
 * 到期续投选择
 */
function periodFundProSelect(){
	$(".fund7Day .choice-online-pay").click(function(){
		$(this).addClass("act").siblings().removeClass("act");
		var index=$(this).index();
		if(index=="0"){
			$("#reNew").val("N");
			$("#redeemTips").show();
		}else{
			$("#reNew").val("Y");
			$("#redeemTips").hide();
		}
	})
	
	$("#inCome li").click(function(){
		$(this).addClass("act").siblings().removeClass("act");
		var index=$(this).index();
		$('.inCome-tips').hide();
        $('#inComeTips' + index).show();
		if(index=="0"){
			$("#reNew").val("Y");
		}else{
			$("#reNew").val("N");
		}
	})
}

function goToFundInfo() {
	var id = $("#fundId").val();
	redirectUrl("/WeixinService/business/query/fundInfo.shtml?fundId=" + id);
}

function toMyaccount(){
	addCookie('riskUrl',location.href);
	redirectUrl('/WeixinService/business/user/riskLevel.shtml?eventId=event_wx001_reassessId&pageSource=wx_confirmOrderId');
}

function queryFundRate(fundId,money,channelId) {
	$.ajax({
		async : false,
		url : "/WeixinService/business/queryFeeRates.xhtml",
		data : {
			"fundId" : fundId,
			'money' : encodeURI(money),
			'channelId' : channelId
		},
		dataType : "json",
		cache : false,
		type : "POST",
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (!!data && data.returnCode == '0000') {
				if (data.rate != null && !isNaN(data.rate)) {
				    var display = $("#XnCome").css("display");
					$("#fundRateInput").val(data.rate);
					if(display == "none"){
						if(data.rate > 0){
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
					$("#feeMode").val(data.feeMode);
					$("#commro").val(data.commro);
					var countMoney = parseFloat(money) + parseFloat(data.rate);
					$("#tradeAmt").val(countMoney);
					$("#countMoney").html(formatNumber(countMoney, ',') + "元");
					var temp = toUpperCase(countMoney);

					if (temp == "errorMoney" || temp == "moneyMax") {
						errorRemark("金额格式有误");
						return;
					} else {
						temp = temp.replace("元整", "元");
					}
					$("#countMoneyText").html(temp);
				} else {
					errorRemark("获取费率失败");
					redirectUrl("/WeixinService/business/query/fundList.shtml");
				}
			} else {
				errorRemark(data.returnMsg);
				redirectUrl("/WeixinService/business/query/fundList.shtml");
			}

		}
	});
}

function queryFundInfo(fundId,period) {
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
					if(fundInfo.typeId=='0110'){
						$('#payMethod').show();
						$('#inCome').show();
					}else{
						$('#payMethod').hide();
					}
					$("#fundRisklevel").val(fundInfo.fundRisklevel);
					$("#fundId").val(fundInfo.fundId);
					$("#scale").val(fundInfo.scale);
					$("#adName").val(fundInfo.adname);
					$("#typeId").val(fundInfo.typeId);
					$("#fundName").html(fundInfo.typeName + "-" + fundInfo.adname);
					if(fundInfo.moneyStep < 10000) {
						$("#buyRemark").html(numDiv(fundInfo.money, 10000) + "万起购，" + fundInfo.moneyStep + "元递增");
					} else {
						$("#buyRemark").html(numDiv(fundInfo.money, 10000) + "万起购，" + numDiv(fundInfo.moneyStep, 10000) + "万递增");
					}
					/* 预约金额 初始值认购起点 */
					$("#money").val(formatNumber(fundInfo.money, ','));
					$("#productMoney").val(fundInfo.money);
					$("#moneyStep").val(fundInfo.moneyStep);
					/* 剩余额度 */
					$("#displayLimit").val(fundInfo.displayLimit);
					/* 年化收益 */
					$("#profit").val(fundInfo.profit);
					$("#startDate").val(fundInfo.interestDate);
					$("#endDate").val(fundInfo.maturityDate);
					$("#appointEndDate").val(fundInfo.appointEndDate);
					$("#appointDate").val(fundInfo.appointDate);
					$("#salesDate").val(fundInfo.salesDate);
	        		$("#subdeadLine").val(fundInfo.subdeadLine);
					$("#apkind").val(fundInfo.fundState);
					$("#highRiskExplain").val(fundInfo.highRiskExplain);
					$("#termInDay").val(fundInfo.termInDay);
					// 最低起购金额
					$('#sartBuying').val(fundInfo.sartBuying);
					$("#redeemTips em:eq(0)").html(formatDate1(fundInfo.maturityDate));
					$("#redeemTips em:eq(1)").html(formatDate1(fundInfo.arrivalAccountDate));
					var temp = numMulti(fundInfo.money, fundInfo.profit);
					temp = numDiv(temp, 365);

/*					temp = numMulti(temp, daysBetween(fundInfo.maturityDate,fundInfo.interestDate));*/
					temp = numMulti(temp, parseInt(fundInfo.termInDay)||0);
					if (temp == 0) {
						$("#XnCome").hide();
					} else {
						/* 计提基准 */
						/*$("#expectInCome").text(formatNumber(temp.toFixed(2), ','));*/
					}
					var today = fundInfo.currentWorkdate;
					
					var buyType = "1";
		        	var appointDate = fundInfo.appointDate;/*预约开始日期*/
					var appointEndDate = fundInfo.appointEndDate;/*预约结束日期*/
					var salesDate = fundInfo.salesDate;/*发售日*/
					var subdeadLine = fundInfo.subdeadLine;/*认购截止*/
					var currentWorkdate = fundInfo.currentWorkdate;
					var displayLimit = parseFloat(fundInfo.displayLimit);/* 页面展示剩余额度*/
					
					//过了截止日 = 募集结束
					if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
						buyType ="1";
						$("#btn_01").html("募集结束").attr("href","javascript:void(0)");
					//认购期= 当前时间 大于等于 认购起始日 并且 小于 认购截止日 
					}else if(daysBetween(currentWorkdate,salesDate) >= 0 && daysBetween(currentWorkdate,subdeadLine) <= 0){
						//额度是否足够
						if(displayLimit > 0){
							buyType ="2";
						}else{
							buyType ="4";
							$(".header .top-a h2").html("排队信息确认");
							document.title="排队信息确认";
							$("#btn_01").html("参与排队");//.attr("href","javascript:toNext()")
							
							$("#btn_03").attr("href","javascript:fundTradeLineUp()");
							$(".pay-addr-info h2.mid").html("请等待客服人员为您分配额度后，按以下信息，完成汇款。").css("color", "#ca132c");
							$("#offLineSuccessSection h1").html("排队成功");
							$("#offLineSuccessSection .point").html("成功加入排队，请等待客服人员分配额度");
						}
					//预约期= (预约开始日 <= 当前时间  <= 预约截止日 )
					}else if(daysBetween(currentWorkdate,appointDate) >= 0 && daysBetween(currentWorkdate,appointEndDate) <= 0){
						//额度是否足够
						if(displayLimit > 0){
							buyType ="1";
						}else{
							buyType ="4";
							$(".header .top-a h2").html("排队信息确认");
							document.title="排队信息确认";
							$("#btn_01").html("参与排队");//.attr("href","javascript:toNext()")
							
							$("#btn_03").attr("href","javascript:fundTradeLineUp()");
							$(".pay-addr-info h2.mid").html("请等待客服人员为您分配额度后，按以下信息，完成汇款。").css("color", "#ca132c");
							$("#offLineSuccessSection h1").html("排队成功");
							$("#offLineSuccessSection .point").html("成功加入排队，请等待客服人员分配额度");
						}
					}else{
						$("#btn_01").html("预约期未开始").attr("href","javascript:void(0)");
						buyType ="1";
					}
					$("#buyType").val(buyType);
					
					if (typeof (fundInfo.templetId) != undefined) {
						$("#templetId").val(fundInfo.templetId);
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

/* 判断预约金额是否符合格式 */
function checkedMoney() {
	var money = parseFloat(unformat($("#money").val()));
	var moneyZero = parseFloat(unformat($("#moneyZero").val()));/* 认购起点 */
	var moneyStep = parseFloat(unformat($("#moneyStep").val()));/* 认购步长 */
	var displayLimit = parseFloat(unformat($("#displayLimit").val()));/* 剩余额度 */
	var buyType = $("#buyType").val();/* 购买类型 */
	var scale = $("#scale").val();
	if (!Validater.isPureNumber(money)) {
		/* $("#money").val(formatNumber(money)); */
		$("#XnCome").hide();
		$("#fee").hide();
		$("#payMethod").hide();
		$("#countMoneySpan").hide();
		errorRemark('请输入正确的预约金额');
		$("#money").focus();
		return false;
	} else if (money < moneyZero || (money - moneyZero) % moneyStep != 0) {
		$("#money").val(formatNumber(money));
		$("#XnCome").hide();
		$("#fee").hide();
		$("#payMethod").hide();
		$("#countMoneySpan").hide();
		if(moneyStep < 10000) {
			errorRemark("本产品" + numDiv(moneyZero, 10000) + "万起购，" + moneyStep + "元递增");
		} else {
			errorRemark("本产品" + numDiv(moneyZero, 10000) + "万起购，" + numDiv(moneyStep, 10000) + "万递增");
		}
		return false;
	} else if (money > displayLimit && buyType != "4") {
		$("#money").val(formatNumber(money));
		$("#XnCome").hide();
		$("#payMethod").hide();
		$("#fee").hide();
		$("#countMoneySpan").hide();
		errorRemark("仅剩余" + (displayLimit >= 10000 ? (numDiv(displayLimit, 10000) + "万") : formatNumber(displayLimit, ',')) + "额度<br>不能再高了");
		/* $("#money").focus(); */
		return false;
	} else if (buyType == "4" && money > scale) {
		$("#money").val(formatNumber(money));
		$("#XnCome").hide();
		$("#payMethod").hide();
		$("#fee").hide();
		$("#countMoneySpan").hide();
		errorRemark("预约金额不能高于产品发售规模");
		/* $("#money").focus(); */
		return false;
	} else {
		queryFundRate(fundId,money,'');
		moneySuccess()
		return true;
	}
	/**
	 * 金额校验通过执行赋值
	 */
	function moneySuccess(){
		$("#money").val(formatNumber(money, ','));
		$("#tradeAmt").val(parseFloat(unformat($("#money").val())||0) + parseFloat(unformat($("#fundRateInput").val())||0));
		var countMoney = parseFloat(money) + parseFloat($("#fundRateInput").val());
		$("#countMoney").html(formatNumber(countMoney, ',') + "元");
		var temp = toUpperCase(countMoney);
		if (temp == "errorMoney" || temp == "moneyMax") {
			errorRemark("金额格式有误");
			return;
		} else {
			temp = temp.replace("元整", "元");
		}
		$("#countMoneyText").html(temp);
		$("#countMoneySpan").show();
		$("#payMethod").show();
	}
}


/* 查询电子合同 */
function queryFundContractById() {

	var fundId = $("#fundId").val();
	var period="";
	period = getUrlParameter('period');
	$.ajax({
		async : false,
		url : "/WeixinService/business/queryFundContractById.xhtml",
		data : {
			"fundId" : fundId,
			"status" : "N",
			"period":period
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
					htmls = "<a href='javascript:goToContract(\"/WeixinService/business/query/fundContract.shtml?templet=" + fundId + "&period="+period+"\")'>《" + data.fundContractDto.templateName + "》</a>";
				} else {
					$("#contractStatus").val("N");
					errorRemark("未找到产品合同");
				}
				htmls += "<a href='javascript:goToContract(\"/WeixinService/business/query/fundRiskScript.shtml?1=1\")'>《风险揭示函》</a>";
				var invprtp = $("#invprtp").val();
				var invprtpScore = $("#invprtpScore").val();
				if("0" != invprtp || !invprtpScore || 60 > invprtpScore ){
					htmls += "<a href='javascript:goToContract(\"/WeixinService/business/query/commonInvstTradeInfo.shtml?1=1\")'>《普通投资者交易告知书》</a>";
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
	if (!checkedMoney()) {
		return;
	} else {
		var money = unformat($("#money").val());
		window.location.href = url + "&money=" + money;
	}
}

function checkUserRiskLever(){
	var fundId = $("#fundId").val();
	var tradeAmt = unformat($("#money").val()); /* 认购金额 */
	/* var tradeAmt = $("#tradeAmt").val(); 支付金额 */
	var apkind = $("#apkind").val();
	var buyType = $("#buyType").val();
	var fundRate = $.trim($("#fundRateInput").val());
	var commro = $("#commro").val();
	var contractStatus = $("#contractStatus").val();
	var fundRisklevel = $("#fundRisklevel").val();
	var riskLevel = $("#riskLevel").val();
	var invprtp = $("#invprtp").val();
	var invprtpScore = $("#invprtpScore").val();
	var isControl = $("#isControl").val();
	var isNotBeneficiary = $("#isNotBeneficiary").val();
	var isBadHonesty = $("#isBadHonesty").val();
	var specialRiskLevel = $("#specialRiskLevel").val();
	var renew=$('#inCome li.act').attr('data-value');

	if("Y"==isControl || "Y"==isNotBeneficiary || "Y"==isBadHonesty){
		$("#risk_properTips").show();
		return ;
	}
	
	if(!specialRiskLevel){
		specialRiskLevel = "5";
	}
	
	if (!checkedMoney()) {
		return;
	}
	
	if( $('#typeId').val()=='0110' && renew==null || renew== ''){
		errorRemark('请勾选并确认续投方式');
		return;
	}
	
	if (contractStatus == "N") {
		errorRemark('未找到产品合同');
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
				payHighRiskPrompt();
			}
		});
		
	/**中基协风险揭示书改造需求---添加--结束**/

	// 中基协风险揭示书改造需求注释方法是原有逻辑
	// payHighRiskPrompt();
	
	var flag = queryUserTaxResidentType();
	if(!flag){
		 ;
	};
}


/* 普通订单 预下单 */
function fundAppoint() {
	$("#high_risk_tips_02").hide();
	var fundId = $("#fundId").val();
	var tradeAmt = unformat($("#money").val()); /* 认购金额 */
	/* var tradeAmt = $("#tradeAmt").val(); 支付金额 */
	var apkind = $("#apkind").val();
	var buyType = $("#buyType").val();
	var fundRate = $.trim($("#fundRateInput").val());
	var commro = $("#commro").val();
	var contractStatus = $("#contractStatus").val();
	var fundRisklevel = $("#fundRisklevel").val();
	var riskLevel = $("#riskLevel").val();
	
	var renew=$('#inCome li.act').attr('data-value');
	
	if (!checkedMoney()) {
		return;
	}
	
	if($('#typeId').val()=='0110' && (renew==null || renew== '')){
		errorRemark('请勾选并确认续投方式');
		return;
	}
	
	if (contractStatus == "N") {
		errorRemark('未找到产品合同');
		return;
	}
	if ($("#isok").is(":checked") == false) {
		errorRemark('请勾选并确认相关产品合同');
		return;
	}
	/*if(riskLevel < fundRisklevel && riskWarnGoonFlag){
		$("#tips_01").show();
		return;
	}*/
	
	var params = {
		'fundId' : fundId,
		'tradeAmt' : encodeURI(tradeAmt),
		'apkind' : apkind,
		'buyType' : buyType,
		'fee' : fundRate,
		'commro' : commro,
		'payType' : "2",
		"renew" : renew
	};

	var urlVal = "/WeixinService/business/fundAppoint.xhtml";
	$.ajax({
		async : false,
		url : urlVal,
		type : "post",
		dataType : 'json',
		data : params,
		error : function() {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data, textStatus) {
			if (data.returnCode == "0000") {
				var serialNo = data.resultFundTradeDto.serialNo;
				$("#serialNo").val(serialNo);
				$("#buyMoneySection").hide();
				initDiv2();
			} else if (data.returnCode == "8000") {
				errorRemark(data.returnMsg);
				/* 跳转到登陆页面，判断微信和其他ie，分别跳转到不同的登陆页面 */
				window.setTimeout("goToLogin()", 2000);
			} else if (data.returnCode == "9999"){
				errorRemark("网络繁忙，请稍后再试");
		    } else {
				errorRemark(data.returnMsg);
			}
		}
	});
}

$("#money").change(function() {
	checkedMoney();
});


/* ------- 选择银行卡页面js ------- */
function initDiv2() {
	document.title="选择支付方式";
    $(".header h2").html("选择支付方式");
	$("#selectBankSection").show();
	getUserRequest("buy-money-selectBankSection");/* 此处subPath为页面内行为 */
	$("#moneyText").html($("#countMoneySpan").html());
	queryMyBankCard();
	var typeId=$("#typeId").val()
	if(typeId=="0400"){
        $(".fund7Day").show();
        $("#redeemTips").show();
	}else{
		$(".fund7Day").hide();
		$("#redeemTips").hide();
	}
}
/* 我的银行卡信息 */
function queryMyBankCard() {
	var fundid = $("#fundId").val();
	$.ajax({
		async : false,
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
							htmls1 += "<li id=\"f"+ i+ "\" data-channelId = '"+ item.bankNo +"' data-tradeAcc='"+ item.tradeAcco+ "' data-bankNo='"
							+ item.bankAccoDisplay+ "' data-bankName='"+ item.bankNm+ "' data-amountOneDay='"
							+ item.dtAmtLimit+"' data-balance='"+ item.balance+ "' onclick=\""+ onclickVal+ "\">"+ item.bankNm
							+ "(尾号"+ item.bankAccoDisplay.subString(item.bankAccoDisplay.length - 4)+ ")<span>仅支持线下汇款</span></li>";
						}else{
							htmls2 += "<li id=\"f"+ i+ "\" data-channelId = '"+ item.bankNo +"' data-tradeAcc='"+ item.tradeAcco+ "' data-bankNo='"
							+ item.bankAccoDisplay+ "' data-bankName='"+ item.bankNm+ "' data-amountOneDay='"
							+ item.dtAmtLimit+"' data-balance='"+ item.balance+ "' onclick=\""+ onclickVal+ "\">"+ item.bankNm
							+ "(尾号"+ item.bankAccoDisplay.subString(item.bankAccoDisplay.length - 4)+ ")<span>仅支持线下汇款</span></li>";
						}
					});

					$("#bankList_off").html(htmls1 + htmls2);
					$("#bankList_off li:eq(0)").click();
				} else {
					errorRemark("未绑定银行卡，请先绑定银行卡");
					window.setTimeout('redirectUrl("/WeixinService/business/bank/bankAuth.shtml")',2000);
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
		"data-channelId" : $("#" + _id).attr("data-channelId")
	});
	
	$("#cardNums").attr({
		"data-balance" : $("#" + _id).attr("data-balance")
	});
		
	$("#cardNums").html(
	$("#" + _id).attr("data-bankName")+ "(尾号"+ $("#" + _id).attr("data-bankNo").subString($("#" + _id).attr("data-bankNo").length - 4) + ")");
	$("#bankNoElements").html($("#" + _id).attr("data-bankName")+ "(尾号"+ $("#" + _id).attr("data-bankNo").subString($("#" + _id).attr("data-bankNo").length - 4) + ")");
	$("#bankList_off li").removeClass("act");
	/* $("#bankList_on li").removeClass("active"); */
	$("#" + _id).parents("ul").addClass("isActive");
	/* $("#n"+_id.subString(1)).addClass("active"); */
	$("#f" + _id.subString(1)).addClass("act");

	var bankLimitAmountOneDay = parseFloat($("#" + _id).attr("data-amountOneDay")||0);
	/* 把选中的银行卡的单日限额，放入隐藏的input中 */
	$("#payLimitation").val(bankLimitAmountOneDay);
	var typeId = $("#typeId").val();
	var balance = $("#" + _id).attr("data-balance");
	if((typeId == '0500' || typeId == '0400' || typeId == '0110') && parseFloat(balance) > 0){
		$("#moneyZero").val($("#sartBuying").val());
	}else{
		$("#moneyZero").val($("#productMoney").val());
	}
	setTimeout("$('#selection-bank-list').hide()",500);
	
}

$('#bank_x').on('click', function() {
	$('#selection-bank-list').show();
});

$('#cover-content-x').on('click', function() {
	$('#selection-bank-list').hide();
});

//$('.cover-bg').on('click', function() {
//	setTimeout("$('.cover-bg').hide()",500);
//});

/* 去验证密码页面 */
function goToVerifyPassword() {
	getUserRequest("buy-money-verifyPasswordSection");/* 此处subPath为页面内行为 */
	
	var money = parseFloat(unformat($("#money").val()));
	var moneyZero = parseFloat(unformat($("#moneyZero").val()));/* 认购起点 */
	var balance = parseFloat($("#cardNums").attr("data-balance"));
	
	if(balance <= 0 && money < moneyZero){
		errorRemark("该银行卡号无产品在途金额<br />请更换银行卡");
		return;
	}
	
	var flag = queryUserTaxResidentType();
	if(!flag){
		return;
	};
	var payType = $("#payType").val();
	if (payType == "0") {
		errorRemark('暂未开通线上支付，请选择线下支付');
		return;
	}
	
	queryFundRate($("#fundId").val(),unformat($("#money").val()),$.trim($("#cardNums").attr("data-channelId")));
	$("#selectBankSection").hide();
	$("#verifyPasswordSection").show();
    document.title="提交订单";
    $(".header h2").html("提交订单");
}

/* 购买 */
function fundTrade() {
	
	getUserRequest("buy-money-offLineSuccessSection");/* 此处subPath为页面内行为 */
	var fundId = $("#fundId").val();
	var tradeAcco = $.trim($("#cardNums").attr("data-tradeAcc"));
	var serialno = $("#serialNo").val();
	var tradeAmt = unformat($("#money").val()); /* 认购金额 */
	/* var tradeAmt = $("#tradeAmt").val(); */
	var fee = $.trim($("#fundRateInput").val());
	var commro = $("#commro").val();
	var feeMode = $("#feeMode").val();
	var contractVer = $("#contractVer").val();
	var renew=$('#reNew').val();
//	var tPassword = $("#tPassword").val();
	var payType = $("#payType").val();

	if (!checkedMoney()) {
		return;
	}
	
	if($('#typeId').val()=='0400' && (renew==null || renew== '')){
		renew="N";
	}
	if($('#typeId').val()=='0110' && (renew==null || renew== '')){
		errorRemark('请勾选并确认续投方式');
		return;
	}
//	if(!checkedTpsw(tPassword)){
//		return;
//	}

    // mgm开关积分Star
    var openMgm=queryParamList("SYSTEM","enableIntegral","")[0].pmco
	if(openMgm=="1"){
		var inviterPhone=$("input[name=inviterPhone]").val();
		if(inviterPhone!=""){
			if(!isMobile(inviterPhone)){
				errorRemark('邀请人手机号码格式不正确');
				return;
			}
		}
	}
	// mgm开关积分end
	
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
		'renew':renew,
		'feeMode' :feeMode
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

				// MGM下单绑定邀请人手机号码
				if(openMgm=="1"){
				   inviter()        
				}                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             
               	// MGM下单绑定邀请人手机号码end


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
/* 确认线下汇款，给用户发送通知信息 */
function sendSmsMsg() {

	var bankNumber = $.trim($("#cardNums").attr("data-bankNo"));
	var bankName = $.trim($("#cardNums").attr("data-bankName"));
	bankName = encodeURI(bankName);
	var money = unformat($("#money").val());
	var fundName = $("#adName").val();
	fundName = encodeURI(fundName);
	var buyType = $.trim($("#buyType").val());
	var appointDate = $.trim($("#appointDate").val());
	var appointEndDate = $.trim($("#appointEndDate").val());
	var salesDate =  $.trim($("#salesDate").val());
    var subdeadLine =  $.trim($("#subdeadLine").val());
    var serialNo = $("#serialNo").val();
	var msgType = "11";
	if (buyType == "1") {
		msgType = "10";
		salesDate = dateformat(salesDate);
        subdeadLine = dateformat(subdeadLine);
        var apkind = $("#apkind").val();
        if(apkind == "0"){
        	appointEndDate = salesDate +" - "+ subdeadLine + "15:00";
        }else{
        	appointEndDate = salesDate +" - "+ subdeadLine + "17:00";
        }
	}

	var params = {
		"bankNumber" : bankNumber,
		"bankName" : bankName,
		"money" : money,
		"fundName" : fundName,
		"msgType" : msgType,
		"appointEndDate" : encodeURI(appointEndDate),
		"serialNo" : serialNo
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

/* 下排队单 */
function fundTradeLineUp() {
	var fundId = $("#fundId").val();
	var tradeAcco = $.trim($("#cardNums").attr("data-tradeAcc"));
	var serialno = $("#serialNo").val();
	var tradeAmt = unformat($("#money").val()); /* 认购金额 */
	var fee = $.trim($("#fundRateInput").val());
	var commro = $("#commro").val();
	var contractVer = $("#contractVer").val();
	var tPassword = $("#tPassword").val();
	var payType = $("#payType").val();
	var apkind = $("#apkind").val();
	var renew=$('#inCome li.act').attr('data-value');
	
	if(!checkedTpsw(tPassword)){
		return;
	}
	var params = {
		'tradeAcco' : tradeAcco,
		'tPassword' : tPassword,
		'fundId' : fundId,
		'serialno' : serialno,
		'apkind' : apkind,
		'tradeAmt' : encodeURI(tradeAmt),
		'fee' : fee,
		'commro' : commro,
		'payType' : payType,
		'contractVer' : contractVer,
		'renew':renew
	};

	$.ajax({
		url : "/WeixinService/business/fundTradeLineUp.xhtml",
		type : "post",
		dataType : 'json',
		data : params,
		error : function() {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data, textStatus) {

			var returnCode = data.returnCode;

			if (returnCode == "0000") {
				tpassCount = 0;/* 输入正确，将错误次数清零 */
				$("#verifyPasswordSection").hide();

				if (payType != null && payType == 1) {/* 线下 */
					var tempCardNo = $("#cardNums").attr("data-bankNo").subString(
					$("#cardNums").attr("data-bankNo").length - 4);
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
function toNext() {
	if (!checkedMoney()) {
		return;
	}
	
	if ($("#isok").is(":checked") == false) {
		errorRemark('请勾选并确认相关产品合同');
		return;
	}
	var renew=$('#inCome li.act').attr('data-value');
	if( $('#typeId').val()=='0110' && renew==null || renew== ''){
		errorRemark('请勾选并确认续投方式');
		return;
	}
	
	$("#buyMoneySection").hide();
	initDiv2();
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
			$("#riskLevel").val(data.riskLevel);
			$("#invprtp").val(data.invprtp);
			$("#invprtpScore").val(data.invprtpScore);
			$("#isControl").val(data.isControl);
			$("#isNotBeneficiary").val(data.isNotBeneficiary);
			$("#isBadHonesty").val(data.isBadHonesty);
			$("#specialRiskLevel").val(data.specialRiskLevel);
			
			if (data.returnCode == "0000") {
				if(data.userType == null || data.userType != "30"){
					errorRemark("请您完成实名鉴权");
					window.setTimeout("redirectUrl('/WeixinService/business/bank/bankAuth.shtml?pageSource=wx_confirmOrderId')", 1000);
				}else if(data.isSetTradePassword == null || data.isSetTradePassword != "Y"){
					errorRemark("请先设置安全码");
					window.setTimeout("redirectUrl('/WeixinService/business/user/setTPassword.shtml')", 1000);
				}else{
					//合格投资者认证开关
					var param=queryParamList("SYSTEM","ACINVCONF","");
					var pmnm=""
					for(var i=0;i<param.length;i++){
						if(param[i].pmco=="MAIN"){
							pmnm=param[i].pmnm;
				}
				    }
					if(pmnm=="1"){  //开启认证
					   qualified()
					}
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
	var param=getUrlParameter("fundId");
	var period=getUrlParameter("period");
	window.location.href="/WeixinService/business/bank/addBank.shtml?fundId="+param+"&period="+period;
}
function cancleTips(_id) {
    $("#"+_id).hide();
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


function queryUserTaxResidentType(){
	var flag = true;
	$.ajax({
		async : false,
		url : "/WeixinService/business/queryUserinfo.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				if(data.taxResidentType == ""){
                                        setTimeout(function(){
				    	     window.location.href="/WeixinService/business/user/updateTaxResidentInfo.shtml"
				        },2000)
					errorRemark("请先完善“税收居民类型”再进行风险测评。");
					flag = false;
				}
			}
		}
	});
	return flag;
}


function countDown(){
	timer--;
	if(timer == 0){
		
		$('#highRiskBtn').html("我知道了").css({color:"#fff"});
		$('#highRiskBtn').css({background:"#ca132c"});
		$("#highRiskBtn").bind("click",function(){
			var buyType = $("#buyType").val();
			if("4" != buyType){
				fundAppoint();
			}else{
				toNext();
			}
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
	var buyType = $("#buyType").val();
	if("5" == fundRisklevel && ("0" != invprtp || 60 > invprtpScore)){
		$('#highRiskBtn').html("我知道了("+timer+"s)").css({color:"#4c4c4c"});
		$("#high_risk_tips_02 #high_risk_content").html(highRiskExplain);
		$("#high_risk_tips_02").show();
		countDown();
	}else{
		if("4" != buyType){
			fundAppoint();
		}else{
			toNext();
		}
	};
}

/**
 * 
 * 设置最小认购金额
 * 
 * */
function setFundMinBuyMoney() {
	var flag=true;
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
		    	var typeId = $('#typeId').val()
		    	var moneyStep = unformat($("#moneyStep").val());/* 认购步长 */
		    	if(res.buySatte=="Y" && (typeId == '0400' || typeId == '0500' || typeId == '0110')){
		    		isHasBalence = true;
		    		var money = $('#sartBuying').val();
		    		var countMoney = parseFloat(money) + parseFloat($("#fundRate").html());
		    		money = formatNumber($('#sartBuying').val(),',');
		    		$('#money').val(money);
					$("#tradeAmt").val(countMoney);
					$("#countMoney").html(formatNumber(countMoney, ',') + "元");
					var temp = toUpperCase(countMoney);
					if (temp == "errorMoney" || temp == "moneyMax") {
						errorRemark("金额格式有误");
						flag=false ;
					} else {
						temp = temp.replace("元整", "元");
					}
					if(moneyStep < 10000) {
						$("#buyRemark").html(numDiv(countMoney, 10000) + "万起购，" + moneyStep +"元递增");
					} else {
						$("#buyRemark").html(numDiv(countMoney, 10000) + "万起购，" + numDiv(moneyStep, 10000) + "万递增");
					}
					$("#countMoneyText").html(temp);
					$("#moneyZero").val($("#sartBuying").val());
		    	}else{
		    		$("#moneyZero").val($("#productMoney").val());
		    	}
		     }
		},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
	   }
	})
    return flag
}


/**
 * 判断用户是否为合格投资者
 */
function qualified(){
	$.ajax({
    	async:false,
		url:"/WeixinService/business/queryQualifiedUserInfoByIdno.xhtml",
		type:"post",
		dataType:'json',
		success:function(data){
		     if(data.returnCode=="0000"){
		     	if(data.data&&data.data.length>0){
                           var status=data.data[0].statusRecord.status
    	  	   	   if(status!="S"){
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
						    	 if(status=="F"){
							    	 	$.ajax({
				    			            url  :'/WeixinService/business/modifyAccreditedInvestorInfoStatus.xhtml',
				    			            type : 'POST',
				    			            data : {"updateStatus":"R"},
				    			            async:false,
				    			            dataType : 'json',
				    			            success : function(data) {
				    			        	  if(data.returnCode=="0000"){
				    			        		   location.href="/WeixinService/business/qualified/qualified.shtml";
				    			        	  }else{
				    			        	  	 $(document).dialog({
													    overlayClose: true,
													    content: data.returnMsg
												 });
				    			        	  }
				    			           }
			    				       })
						    	 }
						         location.href="/WeixinService/business/qualified/qualified.shtml";
						    }
						});   
    	  	   	   }
   	  	   	    }else{
                     //当数据为空的时查询投资两年的经历及资产是否有500万
  	  	   	   	    $.ajax({
				    	async:false,
						url:"/WeixinService/business/queryAccreditedInvestorConditions.xhtml",
						type:"post",
						dataType:'json',
						success:function(data){
						     if(data.returnCode=="0000"){
						     	if(!data.data.financialCertificate||!data.data.investCertificate){
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
										         location.href="/WeixinService/business/qualified/qualified.shtml";
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
				       location.href="/WeixinService/business/bank/bankAuth.shtml";
				    }
				}); 
	    	 }else{
        	  	 DJ.alert(data.returnMsg);
        	 } 
	    }
	})
}


/*判断邀请人输入框是否显示*/
function inviterShow(){
	$.ajax({
		async: false,
		url: "/WeixinService/business/integral/recommendInputBox.xhtml",
		data: {},
		type: 'get',
		dataType: "json",
		cache: false,
		error: function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试");
		},
		success: function(data){
			if(data.show == "1"){
				$('.inviterClass').show();
			}
		}
	});
}

/*邀请人手机号接口*/
function inviter(){
	var phoneNumber = $("input[name=inviterPhone]").val();
	if(phoneNumber!=""){
		$.ajax({
			async: false,
			url: "/WeixinService/business/integral/bindingReferrerByPhoneNumber.xhtml",
			data: {
				'phoneNumber': phoneNumber
			},
			type: 'post',
			dataType: "json",
			cache: false,
			error: function(textStatus, errorThrown) {
				errorRemark("网络繁忙，请稍后再试");
			},
			success: function(data){
				if(data.data == '0'){
					errorRemark("邀请手机失败，请重新绑定");
				}
				
			}
		});
	}
}




// 中基协风险揭示书---获取数据
function getRevelation(){
	// var fundId = getUrlParameter("fundId");
	// var fundId = $("#fundid").val();
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
	// var fundId = $("#fundid").val();
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