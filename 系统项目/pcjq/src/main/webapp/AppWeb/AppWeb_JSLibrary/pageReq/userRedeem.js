$(document).ready(function(){
	queryUserinfo();
	var typeId = getUrlParameter("typeId");
	$('#typeId').val(typeId);
	if(typeId == '0110'){
		$("#wrad,.money-one,.redemption-successOne").remove();
		$('#netWorth').prev().text('业绩报酬计提基准');
		$('#maturityDate').prev().text('本期到期时间');
		$('.confirm-order-money > .info-head > .fl').text('到期资金安排');
		anData(getUrlParameter("serialno"));
		queryFundUserTotalBalance();
	}else {
		$('.money-two,.redemption-successTwo').remove();
		queryFund(); /*查询产品详情*/
		queryUserTradeAcctInfoList(getUrlParameter("tradeAcco"),getUrlParameter("fundid"));/*查询交易账号和银行卡信息*/
	}
	
})

// 财富宝标的进入调用
function anData(serialno){
	$.ajax({
		type: "POST",
		async:false,
		url: "/AppService/business/queryTradeInfoByTradeNo.xhtml",
		dataType: "json",
		data: {
			'serialno':serialno 
		},
		cache: false,
		error : function(textStatus, errorThrown) {  
			show_tips("网络繁忙，请稍后再试。");  
		}, 
		success : function (data) {
			var appointRequestDto = data.appointRequestDto;
			var fundInfoDtoV2 = appointRequestDto.fundInfoDtoV2;
			var profit = parseFloat(numMulti((fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
			+ parseFloat(numMulti((fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%";
			$("#productName_01").text(fundInfoDtoV2.adname);
			$("#netWorth").html("<i>" + profit + "</i>");
			$("#maturityDate").text(fundInfoDtoV2.maturityDate);
			var subquty = appointRequestDto.subquty;
			var latestNewValue = fundInfoDtoV2.latestNewValue ? fundInfoDtoV2.latestNewValue : '1.0000';
			var total = subquty * latestNewValue;//总市值
			var redemptionShare = (!appointRequestDto.redemptionShare && appointRequestDto.renew == "N") ? subquty : appointRequestDto.redemptionShare;
			redemptionShare = redemptionShare ? redemptionShare : "0";	
			var redeemTotal = redemptionShare * latestNewValue;//赎回份额市值
			var renewSubquty = parseFloat(subquty) - parseFloat(redemptionShare);
			$("#balance").text(formatNumber(parseFloat(subquty).toFixed(2)));
			$("#bt").text(formatNumber(parseFloat(total).toFixed(2)));
			$("#money").val(formatNumber(parseFloat(redemptionShare).toFixed(2)));
			$("#moneyTotal").text(formatNumber(parseFloat(redeemTotal).toFixed(2)));
			$("#renewBalance").text(formatNumber(parseFloat(total-redeemTotal).toFixed(2)));
			$("#latestNewValue").val(latestNewValue);
			$('#minHoldingMoney').val(fundInfoDtoV2.minHoldingMoney);
			$("#bank_em1").text(appointRequestDto.bankAcco);
			$("#bank_em2").text(appointRequestDto.bankAcnm);
			$("#bank_em3").text(appointRequestDto.bankLongName);
			$("#fundid").val(fundInfoDtoV2.fundId);
			$("#tradeAcco").val(appointRequestDto.tradeacco);
		}
	});
}
// 确认安排调用接口
function confirmationArrangement(){
	var money = $('#money').val().replace(/,/g, "");
	var serialno = getUrlParameter("serialno");
	if(checkedMoney()){
		$.ajax({
			type: "POST",
			async:false,
			url: "/AppService/business/updateOrderRedemptionShare.xhtml",
			dataType: "json",
			data: {
				'serialNo':serialno,
				'tradeAmt':money
			},
			cache: false,
			error : function(textStatus, errorThrown) {  
				show_tips("网络繁忙，请稍后再试。");  
			}, 
			success : function (data) {
				if(data.resultCode == "0000"){
					$("#div_01").hide();
					$("#div_03").show();
					$("#balance_em2").text($("#moneyTotal").text());
					$("#balance_em3").text($("#renewBalance").text());
					countDownHref("10","timeOut","/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO");
				}else{
					show_tips("网络繁忙，请稍后再试。"); 
					countDownHref("3","timeOut","/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO");
				}
			}
		});
	}
	
}
// 全部赎回
$('.redeem-data').click(function(){
	var valp = $('#balance').text();
	var latestNewValue = parseFloat($("#latestNewValue").val() ? $("#latestNewValue").val() : '1.0000');
	$('#money').val(valp);
	$("#renewBalance").text(parseFloat("0").toFixed(2))
	$("#moneyTotal").text(formatNumber(parseFloat(valp.replace(/,/g, "")*latestNewValue).toFixed(2)));
	$("#money_error").hide();
});

///* 查询产品信息*/
function queryFund(){
	var fundId = getUrlParameter("fundid");
	var period = getUrlParameter("period");
	fundId = removeSpecialStr(fundId);
	period = removeSpecialStr(period);
	$.ajax({
    	async:false,
        url: "/AppService/business/queryFund.xhtml",
        dataType: "json",
        type:"POST",
        data: {
        	fundId:fundId,
        	period:period
        },
        cache: false,
        error : function(textStatus, errorThrown) {  
        	show_tips("网络繁忙，请稍后再试。");  
        }, 
        success : function (data) {
        	if(data != null && data.resultCode == "0000"){
            	var fundInfoDto = data.fundInfoDto;
            	$("#templetId").val(fundInfoDto.templetId);
            	

                var latesNewValue=fundInfoDto.latestNewValue

                $("#netWorth").html("<i>" + latesNewValue + "</i>");/*最新净值*/
        		$("#productName_01,#productName_02").html(fundInfoDto.adname).addClass("cursor").addClass("underline").attr("onclick","toFundDetail(\""+fundInfoDto.fundId+"\",\""+fundInfoDto.period+"\");");/*产品名称*/
    			if(fundInfoDto.typeId == "0500" && fundInfoDto.redemptionPloy == "1"){
    				$("#maturityDate").html(formatDate(fundInfoDto.arrivalAccountDate));/* 到账日期*/
    			}else{
    				$("#maturityDate").html(formatDate(fundInfoDto.interestDate1));/* 起息*/
    			}
    			$('#adName').val(fundInfoDto.adname);
    			$('#paymentinter').val(fundInfoDto.paymentinter);
        		$('#latestNewValue').val(fundInfoDto.latestNewValue);//产品最新净值
        		$('#typeId').val(fundInfoDto.typeId);//产品类型
        		$('#minHoldingMoney').val(fundInfoDto.minHoldingMoney);//最低持仓金额
        	}
        }
    });
}




function toFundDetail(fundId,period){
	window.open("/AppService/business/fund/fundDetail.shtml?fundid="+fundId+"&period="+period);
}
/**
 * 点击立即下单
 */
function onceOrder(){
	var flag=checkedMoney();
	var fundid = getUrlParameter("fundid");
	var tradeAcco = $("#tradeAcco").val();
	var money = $("#money").val().replace(/,/g, "");
	
	if(flag){
		   $("#minHoldCheck").hide()	
		   	$("#div_01").hide();
			$("#div_02").show();
			$.ajax({
		    	async:false,
				url:"/AppService/business/queryTradeInfoByCustNo.xhtml",
				type:"post",
				dataType:'json',
				data:{
					"fundid":fundid,
					"tradeAcco":tradeAcco,
					"subquty":money
				},
				success:function(res){
				     if(res.resultCode=="0000"){
				    	 	var tradeinfo = res.data;
				    	 	var bankAcco = tradeinfo.bankAcco;
				    	 	var bankLongName = tradeinfo.bankLongName;
				    	 	var bankAcnm  = tradeinfo.bankAcnm;
				    	 	var bankAcco1 = bankAcco.substring(bankAcco.length-4,bankAcco.length);
				    	 	$('#bank_01 em').text(bankLongName + "(尾号"+bankAcco1+")");
				    	 	$('#bank_02 em').text(bankLongName);
				    	 	$('#bank_03 em').text(bankAcnm);
				    	 	$('#bank_04 em').text(bankAcco);
				    	 	$('#bank_em1').text(bankLongName);
				    	 	$('#bank_em2').text(bankAcnm);
				    	 	$('#bank_em3').text(bankAcco);
				    	 	$('#serialNo').val(tradeinfo.serialno);//订单号
				    	 	$('#tradeAcco').val(tradeinfo.tradeacco);//交易账号
				    	 	$('#custno').val(tradeinfo.custno);//客户编号
				    	 	$('#fundid').val(tradeinfo.fundid);//基金代码
				    	 	$('#money').val(money);//赎回份额
				    	 	$('#bankCardNo').val(bankAcco);//银行卡号
				     }
				},
				error:function(){
					show_tips("网络繁忙，请稍后再试。"); 
			   }
			})
			$(".schedule02").addClass("current")
	
	}
}
/**
 * 用户提交赎回
 */
function fundTrade(){
 	var serialNo = $('#serialNo').val();//订单号
 	var tradeAcco = $('#tradeAcco').val();//交易账号
 	var custno = $('#custno').val();//客户编号
 	var fundid = $('#fundid').val();//基金代码
 	var money = $('#money').val();//赎回份额
 	var fundname = $('#adName').val();//产品名称
 	var paymentinter = $('#paymentinter').val();//赎回T+
 	var bankCardNo = $('#bankCardNo').val();//银行卡号
 	//赎回提交时记录操作日志
 	$.ajax({
		url : "/AppService/business/addOpLog.xhtml",
		data : {
			"custNo":custno,
			"optType":"03"//主动赎回
		},
		dataType : "json",
		cache : false,
		type:"POST",
		success : function(data){
			if(data.returnCode=="0000"){
			}else{
				show_tips("网络繁忙，请稍后再试。"); 
			}
		},
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		}
	})
 	
	$.ajax({
		async:false,
		url : "/AppService/business/redemptionOrder.xhtml",
		data : {
			"serialNo":serialNo,
			"tradeAcco":tradeAcco,
			"custno":custno,
			"fundid":fundid,
			"money":money,
			"paymentinter":paymentinter,
			"bankCardNo":bankCardNo,
			"fundname":fundname
		},
		dataType : "json",
		cache : false,
		type:"POST",
		success : function(data){
			if(data.returnCode=="0000"){
			    $("#div_02").hide();
				$("#div_03").show();
				$(".schedule03").addClass("current");
				countDownHref("10","timeOut","/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO");
			}else{
				show_tips("网络繁忙，请稍后再试。"); 
				countDownHref("3","timeOut","/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO");
			}
		},
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		}
	})

}

//*查询交易账号和银行卡信息信息*/
function queryUserTradeAcctInfoList(tradeacco,fundid){
	$.ajax({
		async:false,
		url : "/AppService/business/queryCanRedeemBankInfoByFundCode.xhtml",
		data : {
			"fundCode":fundid
		},
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			var htmls = '';
			if(data.data == null || data.data.length == 0){
				show_tips2("没有可支付银行卡，是否去添加?","添加银行卡","我再看看","/AppService/business/bank/addBank.shtml","closeTipsAndReturn");
				return;
			}
			var total  = 0;
			$.each(data.data, function(i, item) {
			    htmls += "<option value='"+i+"' data-bankNo='"+item.bankno+"' data-total='"+item.total+"' data-balance='"+item.balance+"' data-tradeAcco='"+item.tradeacco+"'>"
						+item.banklongname+"&nbsp;（尾号"+item.bankaccodisplay.substr(item.bankaccodisplay.length-4)+"）</option>";
			    total += parseFloat(item.balance);
			});
			$("#bank").html(htmls);
			$('#totalbalance').val(total);
			var latestNewValue = parseFloat($("#latestNewValue").val() ? $("#latestNewValue").val() : '1.0000');
			//默认总份额以及可赎回份额
			$('#total').text(data.data[0].total);
			$('#balance').text(formatNumber(parseFloat(data.data[0].balance).toFixed(2)));
			$('#bt1').text(formatNumber(parseFloat(data.data[0].balance*latestNewValue).toFixed(2)));
			$('#tradeAcco').val(data.data[0].tradeacco);
			$('#tradeBalance').val(data.data[0].balance);
			if(tradeacco != null && tradeacco != ''){
				$('#bank').find('option[data-tradeAcco="'+tradeacco+'"]').attr('selected',true);
				$('#total').text($('#bank').find('option[data-tradeAcco="'+tradeacco+'"]').attr('data-total'));
				$('#balance').text($('#bank').find('option[data-tradeAcco="'+tradeacco+'"]').attr('data-balance'));
				$('#balance').text(formatNumber(parseFloat($('#bank').find('option[data-tradeAcco="'+tradeacco+'"]').attr('data-balance')).toFixed(2)));
				$('#bt1').text(formatNumber(parseFloat($('#bank').find('option[data-tradeAcco="'+tradeacco+'"]').attr('data-balance')*latestNewValue).toFixed(2)));
				$('#tradeAcco').val(tradeacco);
				$('#tradeBalance').val($('#bank').find('option[data-tradeAcco="'+tradeacco+'"]').attr('data-balance'));
			}
			/* 下拉列表添加改变事件*/
			$("#bank").change(function(){
				var text=$(this).val();
				var total=$(this).find('option[value="'+text+'"]').attr('data-total');
				var balance=$(this).find('option[value="'+text+'"]').attr('data-balance');
				$('#total').text(total);
				$('#balance').text(balance);
				$('#balance').text(formatNumber(parseFloat(balance).toFixed(2)));
				$('#bt1').text(formatNumber(parseFloat(balance*latestNewValue).toFixed(2)));
				$('#tradeAcco').val($(this).find('option[value="'+text+'"]').attr('data-tradeAcco'));
				$('#money').val('');
				$('#tradeBalance').val(balance);
			});
		}
	});
}

/**
 * 验证赎回金额
 */
function checkedMoney(){
	var balance = $('#balance').text().replace(/,/g, ""); //可赎回份额
	var money = $("#money").val().replace(/,/g, "");    //赎回份额
	var re = /^[0-9]+.?[0-9]*$/; 
	if(money == ""){
		$("#money_error").html("赎回份额不能为空").show();
		return false;
	}
	if(!re.test(money)){
		$("#money_error").html("赎回份额只能输入数字").show();
		return false;
	}
	if(parseFloat(money)>parseFloat(balance)){
		$("#money_error").html("赎回份额不能大于总份额").show();
		return false;
	}else{
		$("#money_error").hide();
	}
	money = parseFloat(money).toFixed(2);
	$("#money").val(formatNumber(unformat(money)));
	$("#money03 dd").html("￥"+formatNumber(money,","));
	var moneyFromat = toUpperCase(money).replace(/分|整/g,"").replace("角","拾");
	if(moneyFromat.substring(moneyFromat.length -1, moneyFromat.length) == '元'){
		moneyFromat = moneyFromat.replace("元","");
	}else{
		moneyFromat = moneyFromat.replace("元","点");
	}
	$("#money03 span").html(moneyFromat+"份");
	
	var typeId = $("#typeId").val();
	var latestNewValue = parseFloat($("#latestNewValue").val() ? $("#latestNewValue").val() : '1.0000');
	var tradeBalance = parseFloat($('#tradeBalance').val());
	if(typeId == "0110"){
		var totalRedeem = queryTotalRedeem();
		tradeBalance = tradeBalance - totalRedeem;
		$("#moneyTotal").text(formatNumber(parseFloat(money*latestNewValue).toFixed(2)));
		$("#renewBalance").text(formatNumber(parseFloat((balance - money)*latestNewValue).toFixed(2)));
	}
	if(typeId == "0500"){
		$("#moneyTotal").text(formatNumber(parseFloat(money*latestNewValue).toFixed(2)));
	}
	//活期类产品输入金额时校验是否低于最低持仓金额
	if(typeId=="0500" || typeId=="0110"){
		var inputBalance = parseFloat($("#money").val().replace(/,/g, ""));//页面输入份额
		var minHoldingMoney = parseFloat($('#minHoldingMoney').val());//最低持仓金额
		var lessHoldMoney = parseFloat(tradeBalance*latestNewValue-inputBalance*latestNewValue).toFixed(2);
		if(minHoldingMoney>lessHoldMoney&&lessHoldMoney!=0){//客户持仓份额*最新净值-客户输入的赎回份额*最新净值< 最低持仓金额 时
			$('#text_01').text($('#custName').val());
			$('#text_02').text(formatNumber(parseFloat(inputBalance).toFixed(2)));
			$('#text_03').text(formatNumber(parseFloat(tradeBalance - inputBalance).toFixed(2)));
			$('#text_04').text(formatNumber(parseFloat(minHoldingMoney).toFixed(2)));
			$("#minHoldCheck").show();
			return false;
		}
	}
	return true;
}

function colseTips(){
	$("#minHoldCheck").hide();
}

function queryUserinfo() {
	$.ajax({
		async : false,
		url : "/AppService/business/queryUserinfo.xhtml",
		data : {},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				$("#custName").val(data.custName);
			}
		}
	});
}

function countDownHref(min, obj, url){
	var count = parseInt(min);
	var interval = setInterval(function(){
		count--;
		$("." + obj).html(count);
		if(count == 0){
			clearInterval(interval);
			location.href = url;
		}
	},1000);
}

function queryFundUserTotalBalance(){
	var fundid = $("#fundid").val();
	var tradeAcco = $("#tradeAcco").val();
	$.ajax({
		async:false,
		url : "/AppService/business/queryCanRedeemBankInfoByFundCode.xhtml",
		data : {
			"fundCode":fundid
		},
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			var total = 0;
			$.each(data.data, function(i, item) {
			    total += parseFloat(item.balance);
			    if(item.tradeacco == tradeAcco){
			    	$('#tradeBalance').val(item.balance);
			    }
			});
			$('#totalbalance').val(total);
		}
	});
}

function queryTotalRedeem(){
	var fundid = $("#fundid").val();
	var tradeAcco = $("#tradeAcco").val();
	var serialno = getUrlParameter("serialno");
	var totalRedeem = 0;
	$.ajax({
		async:false,
		url : "/AppService/business/queryTradeInfoList.xhtml",
		data : {
			fundid : fundid,
			applyst : "G"
		},
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			$.each(data.list, function(i, item) {
			    if(item.tradeacco == tradeAcco && serialno != item.serialno){
			    	item.redemptionShare = (!item.redemptionShare && item.renew == "N") ? item.subquty : item.redemptionShare;
			    	item.redemptionShare = item.redemptionShare ? item.redemptionShare : "0";
			    	totalRedeem += parseFloat(item.redemptionShare);
			    }
			});
		}
	});
	return totalRedeem;
}