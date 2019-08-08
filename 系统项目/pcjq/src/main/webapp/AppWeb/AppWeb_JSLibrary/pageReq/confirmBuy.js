var timer = 60;
var timer1 = 10;
var timer2 = 10;
var timer3 = 10;
var riskWarnGoonFlag = true;
/*发送短信验证码 读秒*/
function countDown(){
	if(timer == 0){
		$('#AgetCode').val("获取验证码").css({background:"#F5FCFF"});
		$("#AgetCode").attr('onclick',"getMobileVerifyCode()");
		timer = 60;
	}else{
		timer--;
		$("#AgetCode").val(timer+"S重新获取").css({background:"#F1F1F1"});
		setTimeout('countDown()',1000);
	}
}
/* 支付成功 读秒*/
function countDown1(){
	if(timer1 == 0){
		window.location.href="/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO";
	}else{
		timer1--;
		$("#div_03 span.con03 em").html(timer1);
		setTimeout('countDown1()',1000);
	}
}
/* 成功受理 读秒*/
function countDown2(){
	if(timer2 == 0){
		window.location.href="/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO";
	}else{
		timer2--;
		$("#div_04 span.con03 em").html(timer2);
		setTimeout('countDown2()',1000);
	}
}
/* 等待汇款中 读秒*/
function countDown3(){
	if(timer3 == 0){
		window.location.href="/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO";
	}else{
		timer3--;
		$("#div_05 span.con03 em").html(timer3);
		setTimeout('countDown3()',1000);
	}
}

function radioed(data){
	if($(data).val() == 'N'){
		$(data).parent().find("span").html('（到期后资金赎回到银行卡）');
		$("#redemptionTips").show();
	} else if($(data).val() == 'Y'){
		$(data).parent().find("span").html('（到期后自动买入下一期）');
		$("#redemptionTips").hide();
	}
}
$(document).ready(function(){
	getUserRequest("pc_confirmBuy_01");
	document.title = "支付_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	$(".nav.fr ul li a").removeClass("current");
	/* 线上线下支付方式切换*/
//	$("#online-pay-way").click(function(){
//		$(this).attr("checked","checked");
//        $("#offline-pay-way").removeAttr();
//        $(".online").delay(700).slideDown("show");
//        $(".online-pwd").addClass("block").removeClass("none");
//        $(".identify").addClass("block").removeClass("none");
//        $(".offline-pay-bank").addClass("none").removeClass("block");
//	});
//	$("#offline-pay-way").click(function(){
//		$(this).attr("checked","checked");
//        $("#online-pay-way").removeAttr();
//        $(".online").delay(700).slideDown("show");
//        $(".online-pwd").addClass("block").removeClass("none");
//        $(".identify").addClass("none").removeClass("block");
//        $(".offline-pay-bank").addClass("block").removeClass("none");
//	});
	
	
	$("#serType").val("");
	
	queryTradeInfoByTradeNo();/*查询订单详情*/
	queryFund();
	getRevelation();// 页面开始请求中基协风险揭示书改造数据
	checkedUserLever();

	/*20180103crm数据迁移需求, 移除查询旧版PDF合同*/
	queryEcontrantByFundIdN();/*查询 产品合同 txt*/

	getSupportPayBankDesc();
	queryFeeRateList();
})
$("#money").focus(function(){
	$("#money_error").hide();
});
$("#mobile").focus(function(){
	$("#mobile_error").hide();
});
$("#ischecked").click(function(){
	if($("#ischecked").is(":checked")){
		$("#check_error").hide();
	}else{
		$("#check_error").show();
	}
});
function checkMobile(mobile){
	var t = $("#mobile").val();
	if(!Validater.isMobilePhoneNumber(t)){
		$("#mobile_error").html("请输入正确的手机号").show();
	}
}
/* 查询订单信息*/
function queryTradeInfoByTradeNo(){
	var serialno = getUrlParameter("serialno");
	serialno = removeSpecialStr(serialno);
    $.ajax({
    	async:false,
		url:"/AppService/business/queryTradeInfoByTradeNo.xhtml",
		type:"post",
		dataType:'json',
		data:{
			serialno:serialno
		},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
		},
        success : function (data) {
        	if(data == null || data == "" || data.appointRequestDto == null){
        		show_tips("没有查询到订单。");
        		setTimeout('gotoUrl("/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO")',1500);
        		return;
	    	}
        	var appointRequestDto = data.appointRequestDto;
        	var fundInfoDto = data.appointRequestDto.fundInfoDtoV2;
        	var buyType = "1";
        	$("#templetId").val(fundInfoDto.templetId);
        	$("#apkind").val(fundInfoDto.fundState);
        	$("#fundid").val(appointRequestDto.fundid);
        	$("#period").val(fundInfoDto.period);
        	$("#serType").val("A2T");
        	
        	var today = ""+fundInfoDto.currentWorkdate;
        	var appointEndDate = fundInfoDto.appointEndDate;/*预约结束日期*/
    		/*购买类型 1，预约；2，认购；4，排队*/
			if (daysBetween(today, appointEndDate) > 0) {
				buyType = "2";
				$("#buyType").val("2");/* 认购单 */
			} else {
				if (fundInfoDto.displayLimit == 0) {/* 排队单 */
					buyType = "4";
					$("#buyType").val("4");
				} else {
					$("#buyType").val("1");/* 预约单 */
				}
			}
			
        	var maybe = 0;
        	var term = fundInfoDto.term;
        	var typeId= fundInfoDto.typeId;
        	if(typeId == '0500' || typeId == '0400' || typeId == '0110'){
        		queryUserWarehouse(appointRequestDto.fundid);
        	}
        	if (typeId=='0110') {
				$('#puySubMethod').show();
			}
        	if(typeId=='0400'){
        		$('#puySubMethod1').show();
        	}
        	$('#typeId').val(typeId);
        	/*var money = fundInfoDto.money;*/
        	var profit = fundInfoDto.profit;
        	var benefit = appointRequestDto.benefit;
        	var subamt = parseFloat(appointRequestDto.subamt);
        	var fee = parseFloat(appointRequestDto.fee);
        	var latestNewValue = fundInfoDto.latestNewValue;
        	var sevenDayAnnualy = "--";
        	if(data != null && data.resultCode == "0000"){
        		
        		$("#productName_01,#productName_02").html(fundInfoDto.adname).addClass("cursor").addClass("underline").attr("onclick","toFundDetail(\""+fundInfoDto.fundId+"\");");/*产品名称*/
        		if(typeId=='0500'){
        			$("#inputType").html('最新净值');
                    var latestNewValue = fundInfoDto.latestNewValue ? fundInfoDto.latestNewValue : '1.0000';
        			$("#profit").html("<i>" + latestNewValue + "</i>");/*最新净值*/

        		}else if(typeId=='0400'){
        			//开关
    				var seven ='0';
    				try{
    					seven = queryParamComm("SYSTEM","SHOWNETVALUE","")[0].pmco;
    				}catch(err){
    					console.log("开关查询失败");
    				}
					if(seven == '1'){
						/*
						 * 7天管家类产品
						 * 展示七日年化收益、理财起点、递增金额
						 */
	        			$("#inputType").html('七日年化收益');
	                    if(fundInfoDto.sevenDayAnnualy&&"0.00%"!=fundInfoDto.sevenDayAnnualy){
	                    	sevenDayAnnualy =fundInfoDto.sevenDayAnnualy;
	                    }
	                    $("#profit").html("<i>" + sevenDayAnnualy +"</i>");/*七日年化收益*/
					}else{
						var latestNewValue ='1.0000';
						$("#inputType").html('最新净值');
						if(fundInfoDto.latestNewValue){
							latestNewValue = fundInfoDto.latestNewValue;
						}
						$("#profit").html("<i>" + latestNewValue +"</i>");/*最新净值*/
					}
        		}else{
        			$("#inputType").html('业绩报酬计提基准');
        			if(profit == null || profit == "" || isNaN(profit) || profit <= 0){
            			$("#profit").html("<span>浮动收益</span><i></i>");/*浮动收益*/
            		}else{
            			$("#profit").html(numMulti(profit,100)+"%");/*年化收益*/
            		}
        		}
        		
        		$("#payMoney").val(money + parseFloat(unformat($("#rate").val())));
        		$("#money02 b").html("￥"+formatNumber((subamt+fee),","));
        		$("#money02 em").html(toUpperCase(subamt+fee).replace(/整/g,""));
        		$("#money03 dd").html("￥"+formatNumber((subamt+fee),","));
        		$("#money03 span").html(toUpperCase(subamt+fee).replace(/整/g,""));
        		/*if(profit == null || profit == "" || profit == 0){
        			$("#profit").html("<span>浮动收益</span><i></i>");浮动收益
        		}*/
        		if(typeId=='0400' || typeId=='0500'){
				    $(".confirm-order-info-con dl").eq(2).find("dt").text("预估起息时间")
        			if(appointRequestDto.apkind =='720' || appointRequestDto.apkind =='020' || appointRequestDto.apkind =='820'){
        				$("#interestDate").html(formatDate(fundInfoDto.interestDate));/* 起息*/
        			}else{
        				$("#interestDate").html(formatDate(fundInfoDto.interestDate1));/* 起息*/
        			}
        		}else{
        			$("#interestDate").html(formatDate(fundInfoDto.interestDate));/* 起息*/
        		}
        		if(fundInfoDto.maturityDate){
        			$("#maturityDate").html(formatDate(fundInfoDto.maturityDate));/* 到期*/
        		}else{
        			$("#maturityDate").html("--");/* 到期*/
        		}
        		
        		if(typeId=='0500'){
        			$(".order-last").hide();
        			$(".confirm-order-info-con dl").css('width','30%');
        		}
        		
        		$("#fundRisklevel").val(fundInfoDto.fundRisklevel);
        		
        		$("#money").val(formatNumber(subamt,','));
        		
        		$("#scale").val(fundInfoDto.scale);
        		$("#moneyStep").val(fundInfoDto.moneyStep);
        		$("#displayLimit").val(fundInfoDto.displayLimit);
        		$("#fundid").val(fundInfoDto.fundId);/* 产品ID*/
        		$("#fundState").val(fundInfoDto.state);/* 产品状态*/
        		$("#adName").val(fundInfoDto.adname);
        		$("#appointEndDate").val(fundInfoDto.subdeadLine);
    			if(fee == null || fee == "" || fee == "0"){
    				$("#viewFee").html("");
    			}else{
    				$("#viewFee").html("+<em>"+formatNumber(rate,',')+"</em>元（认购费）");
    				$("#rate").val(fee);
    			}
        		$("#serialNo").val(serialno);
        		/* 保存在页面的产品相关数据，计算时候需要用到的*/

        	}
        }
    });
}/*
function checkedUserLever(){
	获取用户基本信息
	$.ajax({
		async:true,
		url : "/AppService/business/queryOriginalUserinfo.xhtml",
		data : "",
		dataType : "json",
        type:"POST",
		cache : false,
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			if(data == null || data.userType != "30"){
				gotoRealName();
			}else if(data.isSetTradePassword != "Y"){
				gotoSetTPassword();
			}else if(data.returnCode=='0000' && data.isSetTradePassword == "Y"){
				$("#mobile").val(data.mobileOriginal);
				$("#riskLevel").val(data.riskLevel);
				$("#specialRiskLevel").val(data.specialRiskLevel);
				$("#invprtp").val(data.invprtp);
				$("#invprtpScore").val(data.invprtpScore);
				//是否存在控制关系
				$("#isControl").val(data.isControl);
				//是否不是实际受益人
				$("#isNotBeneficiary").val(data.isNotBeneficiary);
				//是否有不良诚信
				$("#isBadHonesty").val(data.isBadHonesty);
				$("#specialRiskLevel").val(data.specialRiskLevel);
				
				var riskLevel = data.riskLevel;
				var specialRiskLevel = !data.specialRiskLevel? "5": data.specialRiskLevel;
				
				var riskHtml = "";
				if(riskLevel == "0" || "1" == specialRiskLevel){保守   默认    未评级的
					riskHtml = "产品与您的风险等级（C1-保守型）不匹配，您可以购买其他产品或重新进行测评？";
			   	}else if(riskLevel != null && riskLevel == "1"){保守型
					riskHtml = "产品与您的风险等级（C1-保守型）不匹配，是否继续购买？";
			   	}else if(riskLevel == "2"){稳健型
					riskHtml = "产品与您的风险等级（C2-稳健型）不匹配，是否继续购买？";
			   	}else if(riskLevel == "3"){平衡型
					riskHtml = "产品与您的风险等级（C3-平衡型）不匹配，是否继续购买？";
			   	}else if(riskLevel == "4"){C4-成长型
					riskHtml = "产品与您的风险等级（C4-成长型）不匹配，是否继续购买？";
			   	}else if(riskLevel == "5"){积极型
					riskHtml = "产品与您的风险等级（C5-积极型）不匹配，是否继续购买？";
			   	}
				
				$("#tips div span.cover-con-text").html(riskHtml);
			}
		}
	});
}
*/
function checkedUserLever(){
	/*获取用户基本信息*/
	$.ajax({
		async:false,
		url : "/AppService/business/queryOriginalUserinfo.xhtml",
		data : "",
		dataType : "json",
        type:"POST",
		cache : false,
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			if(data == null || data.userType != "30"){
				gotoRealName();
			}else if(data.isSetTradePassword != "Y"){
				gotoSetTPassword();
			}else if(data.returnCode=='0000' && data.isSetTradePassword == "Y"){
				$("#mobile").val(data.mobileOriginal);
				$("#riskLevel").val(data.riskLevel);
				$("#specialRiskLevel").val(data.specialRiskLevel);
				$("#invprtp").val(data.invprtp);
				$("#invprtpScore").val(data.invprtpScore);
				//是否存在控制关系
				$("#isControl").val(data.isControl);
				//是否不是实际受益人
				$("#isNotBeneficiary").val(data.isNotBeneficiary);
				//是否有不良诚信
				$("#isBadHonesty").val(data.isBadHonesty);
				$("#specialRiskLevel").val(data.specialRiskLevel);
				
				var riskLevel = data.riskLevel;
				var specialRiskLevel = !data.specialRiskLevel? "5": data.specialRiskLevel;
				
				var riskHtml = "";
				if(riskLevel == "0" || "1" == specialRiskLevel){/*保守   默认    未评级的*/
					riskHtml = "产品与您的风险等级（C1-保守型）不匹配，您可以购买其他产品或重新进行测评？";
			   	}else if(riskLevel != null && riskLevel == "1"){/*保守型*/
					riskHtml = "产品与您的风险等级（C1-保守型）不匹配，是否继续购买？";
			   	}else if(riskLevel == "2"){/*稳健型*/
					riskHtml = "产品与您的风险等级（C2-稳健型）不匹配，是否继续购买？";
			   	}else if(riskLevel == "3"){/*平衡型*/
					riskHtml = "产品与您的风险等级（C3-平衡型）不匹配，是否继续购买？";
			   	}else if(riskLevel == "4"){/*C4-成长型*/
					riskHtml = "产品与您的风险等级（C4-成长型）不匹配，是否继续购买？";
			   	}else if(riskLevel == "5"){/*积极型*/
					riskHtml = "产品与您的风险等级（C5-积极型）不匹配，是否继续购买？";
			   	}
				//最低等级客户 回退到详情页面
				var riskWarnGoon = $("#tips .risk-warning-goon");
				var riskAgainWarnGoon = $("#risk_againTips .risk-warning-goon");
				
                if(1 >= specialRiskLevel){
                	riskWarnGoon.val("再看看");
                	riskWarnGoon.bind("click",function(){
                		history.go(-1);
                	});
                }else{
                	//
                	riskWarnGoon.val("继续购买");
                	riskWarnGoon.bind("click",function(){
                		close_tips('tips');
                		$("#risk_againTips").show();
                	});
                	
                	riskAgainWarnGoon.val("继续购买");
                	riskAgainWarnGoon.bind("click",function(){
                		riskWarnGoonFlag = false;
                		$("#risk_againTips").hide();
                		payHighRiskPrompt();
                	});
                }
				$("#tips div span.cover-con-text").html(riskHtml);
			}
		}
	});
}

/*查询电子合同 TXT*/
function queryEcontrantByFundIdN(){
	var fundid = $("#fundid").val();
	var money = $("#money").val();
	var period = $("#period").val();
	$.ajax({
		async:false,
		url : "/AppService/business/queryFundContractById.xhtml",
		data : {
			"fundId":fundid,
			"period" :period
		},
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			var invprtp = $("#invprtp").val();
			var invprtpScore = $("#invprtpScore").val();
			var htmls = "";
			if(data.fundContractDto != null){
				$("#contractVer").val(data.fundContractDto.version);
				htmls += "我已经阅读并同意";
				htmls += "<a href='javascript:toContract(\""+fundid+"\",\""+unformat($("#money").val())+"\");'>《"+data.fundContractDto.templateName+"》</a>";
			}
			htmls += "<a href='/AppService/business/fund/paySubRiskScript.shtml' target=\"_blank\">《风险揭示函》<\/a>";
			if("0" != invprtp || !invprtpScore || 60 > invprtpScore ){
				htmls += "<a href='/AppService/business/fund/commonInvstTradeInfo.shtml' target=\"_blank\">《普通投资者交易告知书》<\/a>";
			}
			if(htmls != null && htmls != ""){
				$("#econtrant").append(htmls);
			}
		}
	});
}
/*判断预约金额是否符合格式*/
function checkedMoney(_id){
	var scale =  parseFloat(unformat($("#scale").val()));
	var moneyZero = parseFloat(unformat($("#moneyZero").val()));/*认购起点*/
	var money = parseFloat(unformat($.trim($("#"+_id).val())));/*认购金额*/
	var moneyStep = parseFloat(unformat($("#moneyStep").val()));/*认购步长*/
	var displayLimit = parseFloat(unformat($("#displayLimit").val()));/*剩余额度*/
	var buyType = $("#buyType").val();

	if(money >= 1000000000){
		$("#money_error").html("输入金额过大，请重新输入").show();
		return false;
	}else if(money < moneyZero || (money - moneyZero) % moneyStep != 0){
		if(moneyStep >= 10000) {
			$("#money_error").html("本产品"+formatNumber(numDiv(moneyZero,10000),',')+"万起售，"+formatNumber(numDiv(moneyStep,10000),',')+"万递增").show();
		} else {
			$("#money_error").html("本产品"+formatNumber(numDiv(moneyZero,10000),',')+"万起售，"+moneyStep+"元递增").show();
		}
		return false;
	}else {
		$("#money_error").hide();
		$("#money").val(formatNumber(unformat(money),","));
		
		$("#money02 b").html("￥"+formatNumber((money+parseFloat(unformat($("#rate").val()))),","));
		$("#money02 em").html(toUpperCase(money+parseFloat(unformat($("#rate").val()))).replace(/整/g,""));
		$("#money03 dd").html("￥"+formatNumber((money+parseFloat(unformat($("#rate").val()))),","));
		$("#money03 span").html(toUpperCase(money+parseFloat(unformat($("#rate").val()))).replace(/整/g,""));
		return true;
	}
}
/*查询交易账号和银行卡信息信息*/
function queryUserTradeAcctInfoList(){
	var fundid = $("#fundid").val();
	$.ajax({
		async:false,
		url : "/AppService/business/queryUserTradeAcctInfoList2.xhtml",
		data : { fundid : fundid },
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			var htmls1 = "";
			var htmls2 = "";
			if(data.tradeAcctList == null || data.tradeAcctList.length == 0){
				show_tips2("没有可支付银行卡，是否去添加?","添加银行卡","我再看看","/AppService/business/bank/addBank.shtml","closeTipsAndReturn");
				return;
			}
			var temp = 0;
			$.each(data.tradeAcctList, function(i, item) {
				if(parseFloat(item.balance) > 0){
					htmls1 += "<option value='"+temp+"' data-mobile='"+item.mobile+"' data-payMode='"+item.payMode+"' data-bankNo='"+item.bankNo+"' data-tradeAcco='"+item.tradeAcco+"' data-realBankNo='"+item.realBankNo+"'"
					+" data-bankAccoDisplay='"+item.bankAccoDisplay+"' data-dtAmtLimit='"+item.dtAmtLimit+"' data-bankNm='"+item.bankNm+"' data-protoNo='"+item.protoNo+"' data-balance='"+item.balance+"'>"
					+item.bankNm+"&nbsp;（尾号"+item.bankAccoDisplay.substr(item.bankAccoDisplay.length-4)+"）</option>";
				}else{
					htmls2 += "<option value='"+temp+"' data-mobile='"+item.mobile+"' data-payMode='"+item.payMode+"' data-bankNo='"+item.bankNo+"' data-tradeAcco='"+item.tradeAcco+"' data-realBankNo='"+item.realBankNo+"'"
					+" data-bankAccoDisplay='"+item.bankAccoDisplay+"' data-dtAmtLimit='"+item.dtAmtLimit+"' data-bankNm='"+item.bankNm+"' data-protoNo='"+item.protoNo+"' data-balance='"+item.balance+"'>"
					+item.bankNm+"&nbsp;（尾号"+item.bankAccoDisplay.substr(item.bankAccoDisplay.length-4)+"）</option>";
				}
			});
			$("#bank").html(htmls1 + htmls2);
			/* 下拉列表添加改变事件*/
			$("#bank").change(function (){
				selectCard();
//				selectPaytype(false);
			});
			$("#bank").val("0").change();
		}
	});
}
/*选择银行卡*/
function selectCard(){
	
	$("#bankCardNo").val($("#bank").find("option:selected").attr("data-bankAccoDisplay"))/* 银行卡号*/
	$("#bankName").val($("#bank").find("option:selected").attr("data-bankNm"))/* 银行卡号*/
	$("#tradeAcco").val($("#bank").find("option:selected").attr("data-tradeAcco"))/* 交易账号*/
	$("#payMobile").val($("#bank").find("option:selected").attr("data-mobile"))/* 预留手机号码*/
	
	var payMode = $("#bank").find("option:selected").attr("data-payMode");/* 银行卡支持支付方式*/
	var dtAmtLimit = parseFloat($("#bank").find("option:selected").attr("data-dtAmtLimit"));/* 银行卡当日累计 限额*/
	var money = parseFloat(unformat($("#money").val()));
	
	var bankNo = $("#bank").find("option:selected").attr("data-bankNo");
	var protoNo = $("#bank").find("option:selected").attr("data-protoNo");
	/* 默认支持线下汇款 */
	$("#payType").val("1");
	$("#paytypeList #online-pay-way").parent().hide();
	$("#paytypeList #offline-pay-way").click().change().parent().show();
	$("#spanTips_01,#spanTips_02").hide();
	var typeId = $("#typeId").val();
	var balance = $("#bank").find("option:selected").attr("data-balance");// 交易账号下的持仓份额
	if((typeId == "0110" || typeId == "0400" || typeId == "0500") && parseFloat(balance) > 0){
		$("#moneyZero").val($("#sartBuying").val());
	}else{
		$("#moneyZero").val($("#defaultMoney").val());
	}
}
/*选择支付方式*/
function selectPaytype(flag){
	$("#payType").val($("#paytypeList li input:checked").val());
	var payType = $("#payType").val();
	if(payType != null && payType == "1"){
		$("#btn_02").val("提交订单");
	}else{
		$("#btn_02").val("确认支付");
	}
	if(flag){
	 	var bankNo = $("#bank").find("option:selected").attr("data-bankNo");
	 	var protoNo = $("#bank").find("option:selected").attr("data-protoNo");
	 	
		var payMode = $("#bank").find("option:selected").attr("data-payMode");/* 银行卡支持支付方式*/
		var dtAmtLimit = parseFloat($("#bank").find("option:selected").attr("data-dtAmtLimit"));/* 银行卡当日累计 限额*/
		if(payMode != null && payMode == "7"){/* 线下汇款  线上B2C 线上B2B*/
			if(payType == "0"){/* 线上*/
				$("#spanTips_01").html("线上支付   限额："+numDiv(dtAmtLimit,10000)+"万元").show();
				$("#spanTips_02,#spanTips_03").hide();

				if(bankNo != null && bankNo == "002"){/* 工行渠道鉴权鉴权 */
					if(protoNo == null || typeof(protoNo) == "undefined" || protoNo == "" || $.trim(protoNo).length == 0){
						$("#contractSign_div_02").show();
					}
				}
			}else if(payType == "1"){/* 线下*/
				$("#spanTips_03").show();
				$("#spanTips_01,#spanTips_02").hide();
			}
		}else if(payMode != null && payMode == "6"){/* 线下汇款  线上B2C*/ 
			if(payType == "0"){/* 线上*/
				$("#spanTips_01").html("线上支付   限额："+numDiv(dtAmtLimit,10000)+"万元").show();
				$("#spanTips_02,#spanTips_03").hide();

				if(bankNo != null && bankNo == "002"){/* 工行渠道鉴权鉴权 */
					if(protoNo == null || typeof(protoNo) == "undefined" || protoNo == "" || $.trim(protoNo).length == 0){
						$("#contractSign_div_02").show();
					}
				}
			}else if(payType == "1"){/* 线下*/
				$("#spanTips_03").show();
				$("#spanTips_01,#spanTips_02").hide();
			}
		}else if(payMode != null && payMode == "5"){/* 线下汇款  线上B2B*/
			if(payType == "0"){/* 线上*/
				$("#spanTips_01").html("线上支付   限额："+numDiv(dtAmtLimit,10000)+"万元").show();
				$("#spanTips_02,#spanTips_03").hide();

				if(bankNo != null && bankNo == "002"){/* 工行渠道鉴权鉴权 */
					if(protoNo == null || typeof(protoNo) == "undefined" || protoNo == "" || $.trim(protoNo).length == 0){
						$("#contractSign_div_02").show();
					}
				}
			}else if(payType == "1"){/* 线下*/
				$("#spanTips_03").show();
				$("#spanTips_01,#spanTips_02").hide();
			}
		}else if(payMode != null && payMode == "4"){/* 线下汇款*/
			if(payType == "0"){/* 线上*/
				/*$("#spanTips_01").html("线上支付   限额："+numDiv(dtAmtLimit,10000)+"万元");*/
			}else if(payType == "1"){/* 线下*/
				$("#spanTips_02").show();
				$("#spanTips_01,#spanTips_03").hide();
			}
		}else if(payMode != null && payMode == "3"){/* 线上B2C 线上B2B*/
			if(payType == "0"){/* 线上*/
				$("#spanTips_01").html("线上支付   限额："+numDiv(dtAmtLimit,10000)+"万元").show();
				$("#spanTips_02,#spanTips_03").hide();

				if(bankNo != null && bankNo == "002"){/* 工行渠道鉴权鉴权 */
					if(protoNo == null || typeof(protoNo) == "undefined" || protoNo == "" || $.trim(protoNo).length == 0){
						$("#contractSign_div_02").show();
					}
				}
			}else if(payType == "1"){/* 线下*/
				/*$("#spanTips_01").html("请按提示信息进行线下汇款");*/
			}
		}else if(payMode != null && payMode == "2"){/* 线上B2C*/
			if(payType == "0"){/* 线上*/
				$("#spanTips_01").html("线上支付   限额："+numDiv(dtAmtLimit,10000)+"万元").show();
				$("#spanTips_02,#spanTips_03").hide();

				if(bankNo != null && bankNo == "002"){/* 工行渠道鉴权鉴权 */
					if(protoNo == null || typeof(protoNo) == "undefined" || protoNo == "" || $.trim(protoNo).length == 0){
						$("#contractSign_div_02").show();
					}
				}
			}else if(payType == "1"){/* 线下*/
				/*$("#spanTips_01").html("请按提示信息进行线下汇款");*/
			}
		}else if(payMode != null && payMode == "1"){/* 线上B2B*/
			if(payType == "0"){/* 线上*/
				$("#spanTips_01").html("线上支付   限额："+numDiv(dtAmtLimit,10000)+"万元").show();
				$("#spanTips_02,#spanTips_03").hide();

				if(bankNo != null && bankNo == "002"){/* 工行渠道鉴权鉴权 */
					if(protoNo == null || typeof(protoNo) == "undefined" || protoNo == "" || $.trim(protoNo).length == 0){
						$("#contractSign_div_02").show();
					}
				}
			}else if(payType == "1"){/* 线下*/
 				/*$("#spanTips_01").html("请按提示信息进行线下汇款");*/
			}
		}else{/* 默认支持线下汇款 */
			if(payType == "0"){/* 线上*/
 				/*$("#spanTips_01").html("线上支付   限额："+numDiv(dtAmtLimit,10000)+"万元");*/
			}else if(payType == "1"){/* 线下*/
				$("#spanTips_02").show();
				$("#spanTips_01,#spanTips_03").hide();
			}
		}
	}
}
/*发送短信验证码*/
function getMobileVerifyCode(){
	var mobile = $.trim($("#payMobile").val());
	var money = unformat($.trim($("#payMoney").val()));
	var bankcardNo = $.trim($("#bankCardNo").val());
	var bankName = $.trim($("#bankName").val());
	var bnsType = "5";
	bankName = encodeURI(bankName);
	money = encodeURI(money);
	if(Validater.isMobilePhoneNumber(mobile)){
		var params = {
	        "mobile": mobile,
	        "money":money,
	        "bankcardNo":bankcardNo,
	        "bankName":bankName,
	        "bnsType":bnsType
	    };
	    var actionUrl = "/AppService/business/getVerifyCodeForTrade.xhtml";
	    $.ajax({
	    	async:false,
	        url: actionUrl,
	        data: params,
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) {  
	        	show_tips("网络繁忙，请稍后再试。");  
	        }, 
	        success : function (data){
	        	if(data.sessionID != null && data.sessionID != "" && data.sessionID!="nullId" && data.returnCode == "0000"){
	        		/*验证码发送成功*/
	        	  	$("#sessionID").val(data.sessionID);
	        	  	$("#AgetCode").val('重新获取验证码').attr('onclick',"");
	        	  	/*读秒*/
	        	  	countDown();
	        	  	$("#msgMobile").html($("#payMobile").val());
	        	  	$("#sendMsgTips").show();
	        	}else{
	        		show_tips(data.returnMsg);
	        	}
	        }       
	    });
	}
}
/*购买(A2T)*/
function fundTrade(){
	queryFeeRateList();
	getUserRequest("pc_confirmBuy_03");
	var fundid = $("#fundid").val();
	var money = parseFloat(unformat($("#money").val())); /*详情*/
	var productMoney = parseFloat(unformat($("#moneyZero").val()));
	var moneyStep = parseFloat(unformat($("#moneyStep").val()));/*认购步长*/
	var mobile = $("#mobile").val();/* 用户手机号码*/
	var payMobile = $("#payMobile").val();/* 银行预留手机号码*/
	var typeId=$('#typeId').val();
	var renew=$('#puySubMethod input[name="renew"]:checked ').val(); /* 产品是否续投 */
	if(typeId =='0400'){
		renew=$('#puySubMethod1 input[name="renew"]:checked ').val(); /* 产品是否续投 */
	}
	var paytype = $("#payType").val();/*支付方式*/
	var tradeAcco = $("#tradeAcco").val();/*交易账号*/
	var serialNo = $("#serialNo").val();/*交易流水号*/
	var fundState = $("#fundState").val();/*产品状态*/
	var serType = $("#serType").val();/*业务类型*/
	
//	var tpassword = $("#tpassword").val();/* 交易密码*/
	var verifyCode = $("#verifyCode").val();/* 短信验证码*/
	var sessionID = $("#sessionID").val(); 
	
	var fee = $("#rate").val();/*认购费用*/
	var commro = $("#commro").val();/*折扣率*/
	var feeMode = $("#feeMode").val();/*费率类型*/
	if(!fee){
		fee ="0";
	}
	if(!commro){
		commro="1";
	}
	if(!feeMode){
		feeMode="0";
	}
	
	var bankNo = $("#bank").find("option:selected").attr("data-bankNo");
	if(!checkedMoney("money")){
		var balance = parseFloat($("#bank").find("option:selected").attr("data-balance"));
		if(balance <= 0 && money < productMoney){
			show_tips("该银行卡号无产品在途金额<br />请更换银行卡");
			return;
		}
		show_tips("请输入正确的购买金额");
		return ;
	}else if(!$("#ischecked").is(":checked")){
		show_tips("请勾选阅读并同意上述产品合同和说明");
		return;
	}else if(!Validater.isMobilePhoneNumber(mobile)){
		show_tips("请输入正确的电话号码");
		return;
	}else if(tradeAcco == null || tradeAcco == ""){
		show_tips('请选择银行卡');
		return;
	}else{
		if(paytype != null && paytype == "0"){/* 如果是在线支付   则要先验证是否有发送短信验证码*/
			if(isEmpty(verifyCode)){
				show_tips("请输入验证码！");
				return ;
			}else if(isEmpty(sessionID)){
				show_tips("请获取验证码！");
				return ;
			}
		}
		var params = {
			'tradeAcco':tradeAcco,
			'fundState':fundState,
			'fundId':fundid,
			'tradeAmt':encodeURI(money),
			'payType':paytype,/*支付方式('0':网上付款，'1':网下汇款)*/
			'contractVer':$("#contractVer").val(),
			'serialno':serialNo,/*交易流水号，*/
			'serType':serType,
			'mobile':mobile,
			'payMobile':payMobile,
			'verifyCode':verifyCode,
			'sessionID':sessionID,
			'fee':fee,
			'commro':commro,
			'feeMode':feeMode,
			'renew':renew
		};
		$("#tpassword,#verifyCode").val("");
		if(paytype == "0"){
			$("#pay_wait_tips").show();
		}
	    var urlVal="/AppService/business/fundTrade.xhtml";
	    $.ajax({
			url:urlVal,
			type:"post",
			dataType:'json',
			data:params,
			error:function(){
				show_tips("网络繁忙，请稍后再试。"); 
				$("#pay_wait_tips").hide();
			},
			success:function(data, textStatus){
				$("#pay_wait_tips").hide();
				if(paytype == "0"){/* 线上*/
					if(data.resultCode=="0000"){/* 支付成功*/
						if(bankNo != null && bankNo == "004"){
							$("#bocPay_div_02").show();
							var bocSignParaDto = null;
							if(data.bocSignParaDto!=null&&data.bocSignParaDto!='undefined'){
								bocSignParaDto = data.bocSignParaDto;
								var signData = bocSignParaDto.signData;
								$("#merchantNo").val(bocSignParaDto.merchantNo);
								$("#orderNo").val(bocSignParaDto.orderNo);
								$("#curCode").val(bocSignParaDto.curCode);
								$("#orderAmount").val(bocSignParaDto.orderAmount);
								$("#orderTime").val(bocSignParaDto.orderTime);
								$("#orderNote").val(bocSignParaDto.orderNote);
								$("#orderUrl").val("https://www.cmwachina.com");
								$("#signData").val(signData.replace(/[\r\n]/g,""));
								$("#identityType").val(bocSignParaDto.identityType);
								$("#identityNumber").val(bocSignParaDto.identityNumber);
								$("#holderName").val(bocSignParaDto.holderName);
								$("#acctNo").val(bocSignParaDto.acctNo);
								$("#noBindPayUrl").val(bocSignParaDto.noBindPayUrl);
							}
						}else{
							$("#div_03 span.con01 em").html(formatNumber(parseFloat(unformat($("#money").val()))+parseFloat(unformat($("#rate").val()))));
							$("#div_02").hide();
							$("#div_03").show();
							countDown1();
							$("#wrad .schedule03").addClass("current");
						}
					}else if(data.resultCode=="9999"){
						show_tips("网络繁忙，请稍后再试。"); 
					}else if(data.resultCode=="6001"){/* 验证短信验证码失败*/
						show_tips(data.resultMsg);
						$("#verifyCode").css("border","1px solid #CA132C");
					}else if(data.resultCode=="6000"){/* 您输入的安全码有误，请重新输入*/
						show_tips("您输入的安全码有误，请重新输入");
						$("#tpassword").css("border","1px solid #CA132C");
					}else if(data.resultCode=="USR-1I01"){/* 用户已锁定-- 密码错误次数过多，您的账户已锁定，请3小时后再次尝试*/
						show_tips("密码错误次数过多，您的账户已锁定，请3小时后再次尝试");
						$("#tpassword").css("border","1px solid #CA132C");
					}else if(data.resultCode=="USR-1I02"){/* 旧密码错误，但未达到错误次数上限*/
						var tPwdErrCount = data.tPwdErrCount;
						if(data.tPwdErrCount == 1){
							show_tips("您输入的安全码有误，请重新输入");
						}else if(data.tPwdErrCount > 1 && data.tPwdErrCount < 6){
							show_tips("您还有"+(6-parseInt(data.tPwdErrCount,10))+"次机会");
						}else if(data.tPwdErrCount == 6){
							show_tips("密码错误次数过多，您的账户已锁定，请3小时后再次尝试");
						}
						$("#tpassword").css("border","1px solid #CA132C");
					}else if(data.resultCode=="YB001"){/* 成功受理*/
						$("#div_02").hide();
						$("#div_04").show();
						
						countDown2();
						$("#wrad div.schedule03").addClass("current");
					}else{
						$("#payFail div span.cover-con-text").html(data.resultMsg+"，请核实后重新支付！");
						$("#payFail").show();
					}
				}else if(paytype == "1"){/* 线下*/
					if(data.resultCode=="0000"){
						$("#div_02").hide();
						$("#div_05 span.con01").html("下单成功！请使用尾号为"+$("#bank").find("option:selected").text().replace("）","").replace(")","").substr($("#bank").find("option:selected").text().replace("）","").replace(")","").length-4)+
								"的"+$("#bank").find("option:selected").attr("data-banknm")+"卡尽快完成汇款。");
						$("#div_05").show();
						/* 支付成功则给用户发送*/
						/*sendMsgByOffline();*/

						countDown3();
						$("#wrad .schedule03").addClass("current");
						sendSmsMsg();
					}else if(data.resultCode=="9999"){
						show_tips("网络繁忙，请稍后再试。"); 
					}else{
						if(data.resultCode=="6000"){/* 您输入的安全码有误，请重新输入*/
							show_tips("您输入的安全码有误，请重新输入");
							$("#tpassword").css("border","1px solid #CA132C");
						}else if(data.resultCode=="USR-1I01"){/* 用户已锁定-- 密码错误次数过多，您的账户已锁定，请3小时后再次尝试*/
							show_tips("密码错误次数过多，您的账户已锁定，请3小时后再次尝试");
							$("#tpassword").css("border","1px solid #CA132C");
						}else if(data.resultCode=="USR-1I02"){/* 旧密码错误，但未达到错误次数上限*/
							var tPwdErrCount = data.tPwdErrCount;
							if(data.tPwdErrCount == 1){
								show_tips("您输入的安全码有误，请重新输入");
							}else if(data.tPwdErrCount > 1 && data.tPwdErrCount < 6){
								show_tips("您还有"+(6-parseInt(data.tPwdErrCount,10))+"次机会");
							}else if(data.tPwdErrCount == 6){
								show_tips("密码错误次数过多，您的账户已锁定，请3小时后再次尝试");
							}
							$("#tpassword").css("border","1px solid #CA132C");
						}else{
							$("#payFail div span.cover-con-text").html(data.resultMsg+"，请核实后重新支付！");
							$("#payFail").show();
						}
					}
				}
			}
		});
	}
}
function changeBg(_id){
	$("#"+_id).attr("style","");
}
function toContract(fundid,money){
	if(!checkedMoney('money')){
		return;
	}
	var period = $("#period").val();
	var m = unformat($("#money").val());
	var link = "/AppService/business/fund/contract.shtml?period="+period+"&fundId="+fundid+"&money="+m;
	window.open(link);
}
/* 下一步 */
function toNext() {
	getUserRequest("pc_confirmBuy_02");
	var riskLevel = $("#riskLevel").val();
	var specialRiskLevel = $("#specialRiskLevel").val();
	var fundRisklevel = $("#fundRisklevel").val();
	var invprtp = $("#invprtp").val();
	var invprtpScore = $("#invprtpScore").val();
	var isControl = $("#isControl").val();
	var isNotBeneficiary = $("#isNotBeneficiary").val();
	var isBadHonesty = $("#isBadHonesty").val();
	var mobile = $("#mobile").val();
	var typeId = $("#typeId").val();
	var renew=$('#puySubMethod input[name="renew"]:checked ').val();
	if(typeId == '0400'){
		renew=$('#puySubMethod1 input[name="renew"]:checked ').val();
	}
	if("Y"==isControl || "Y"==isNotBeneficiary || "Y"==isBadHonesty){
		$("#risk_properTips").show();
		return ;
	}
	//为空 给默认值
	if(!specialRiskLevel){
		specialRiskLevel = "5";
	}
	
	if(!checkedMoney("money")){
		return ;
	}else if(typeId=='0110' && renew == null || renew ==''){
		show_tips("请选择续投方式");
		return;
	}else if(!Validater.isMobilePhoneNumber(mobile)){
		$("#mobile_error").html("请输入正确的手机号").show();
		return;
	}else if(!$("#ischecked").is(":checked")){
		$("#check_error").html("请勾选阅读并同意产品合同").show();
		return;
	}else if(fundRisklevel == "" || (fundRisklevel != "1" && fundRisklevel != "2" && fundRisklevel != "3"&& fundRisklevel != "4"&& fundRisklevel != "5")){
		show_tips("产品状态异常！");
		window.location.href="/AppService/index.xhtml";
	}else if((riskLevel < fundRisklevel || specialRiskLevel < fundRisklevel) && riskWarnGoonFlag && ("0" != invprtp || 60 >= invprtpScore)){
		$("#tips").show();
		return;
	}else{
		$('#renew').val(renew);
		// $("#div_01").hide();
		// $("#div_02").show();
		// $("#wrad .schedule02").addClass("current");
		// queryUserTradeAcctInfoList();/*查询交易账号和银行卡信息*/

		/**中基协风险揭示书改造需求---添加--开始**/
		$("#div_06,#bocPay_div_033").show();
		// var valueData = nextStep();
		$('.confirm').click(function(){
			var valueData = nextStep();
			if(valueData && ($('.confirm').hasClass('valData'))){
				$("#div_01").hide();
				$("#div_02").show();
				$("#wrad .schedule02").addClass("current");
				queryUserTradeAcctInfoList();
			}
		});
	}
}
/* 确认线下汇款，给用户发送通知信息 */
function sendSmsMsg() {
    var bankNumber = $.trim($("#bank").find("option:selected").attr("data-bankaccodisplay"));
    var bankName = $.trim($("#bank").find("option:selected").attr("data-banknm"));
    bankName = encodeURI(bankName);
    var money = unformat($("#money").val());
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
    var url = "/AppService/setUp/sendSmsMsg.xhtml";
    $.ajax({
        async : false,
        url : url,
        data : params,
        type : 'post',
        dataType : "json",
        cache : false,
        error : function(textStatus, errorThrown) {
        	show_tips("网络繁忙，请稍后再试。");
        },
        success : function(data) {
            if (data.returnCode == "0000") {
                /* show_tips("线下购买通知消息发送成功"); */
            } else {
                /* show_tips("线下购买通知消息发送失败"); */
            }
        }
    });
}
function toFundDetail(fundId){
	window.open("/AppService/business/fund/fundDetail.shtml?fundid="+fundId);
}
function toMyaccount(){
	addCookie('riskUrl',location.href);
	window.location.href = "/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO&type=riskLevel";
}
/*签约*/
function contractSign(){
	var tradeAcct = $("#tradeAcco").val();
	var bankNo = $("#bank").find("option:selected").attr("data-bankNo");/*银行代码*/
	var channelNo = $("#bank").find("option:selected").attr("data-realBankNo");/*渠道代码*/
	if(bankNo == null || bankNo != "002" || tradeAcct == null || tradeAcct == "" || channelNo == null || channelNo == ""){
		return;
	}
	$.ajax({
		async : false,
		url : "/AppService/business/contractSign.xhtml",
		data : {
			"tradeAcct" : tradeAcct,
			"bankNo" : bankNo,
			"channelNo" : channelNo
		},
		type : "POST",
		dataType : "json",
		cache : false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			var returnCode = data.returnCode;
			var returnMsg = data.returnMsg;
			
			if(returnCode != null && returnCode == "0000"){
				var dto = data.icbcSignParaDto;
				/*$("#order").attr("ACTION",dto.signOnlineUrl);*/
				
				$("#contractSign_interfaceName").val(dto.interfaceName);
				$("#contractSign_interfaceVersion").val(dto.interfaceVersion);
				$("#contractSign_selserialNo").val(dto.selserialNo);
				$("#contractSign_payNo").val(dto.payNo);
				$("#contractSign_selcorpId").val(dto.selcorpId);
				$("#contractSign_selaccountNo").val(dto.selaccountNo);
				$("#contractSign_regDate").val(dto.regDate);
				$("#contractSign_HSURL").val(dto.HSURL);
				$("#contractSign_merCertID").val(dto.merCertID);
				$("#contractSign_Language").val(dto.language);
				$("#contractSign_certDate").val(dto.certDate);
				$("#contractSign_allowFinalDate").val(dto.allowFinalDate);
				$("#contractSign_accountNo").val(dto.accountNo);
				$("#contractSign_certData").val(dto.certData);
				
				$("#contractSign_order").submit();

				$("#contractSign_div_02").hide();
				$("#contractSign_div_03").show();
			}
			
		}
	});
}
/*查询签约*/
function queryCommandByBankAccoNo(){
	var tradeAcct = $("#tradeAcco").val();
	var bankNo = $("#bank").find("option:selected").attr("data-bankNo");/*银行代码*/
	var channelNo = $("#bank").find("option:selected").attr("data-realBankNo");/*渠道代码*/
	if(bankNo == null || bankNo != "002" || tradeAcct == null || tradeAcct == "" || channelNo == null || channelNo == ""){
		return;
	}
	$.ajax({
		async : false,
		url : "/AppService/business/queryCommandByBankAccoNo.xhtml",
		data : {
			"tradeAcct" : tradeAcct,
			"bankNo" : bankNo,
			"channelNo" : channelNo
		},
		type : "POST",
		dataType : "json",
		cache : false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			var returnCode = data.returnCode;
			var returnMsg = data.returnMsg;

			if(returnCode != null && returnCode == "0000"){
				show_tips("签约成功");
				queryUserTradeAcctInfoList();/*查询交易账号和银行卡信息*/
				$("#contractSign_div_03").hide();
			}else{
				$("#contractSign_div_02").show();
				$("#contractSign_div_03").hide();
				show_tips(data.returnMsg);
			}
		}
	});
}
function contractSign_close(_id){
	$("#"+_id).hide();
	$("#paytypeList #offline-pay-way").click().change().parent().show();
}
/*查询支持的在线支付的银行描述*/
function getSupportPayBankDesc(){
	$.ajax({
		async : false,
		url : "/AppService/business/getSupportPayBankDesc.xhtml",
		data : {},
		type : "POST",
		dataType : "json",
		cache : false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			var returnCode = data.returnCode;
			var returnMsg = data.returnMsg;

			if(returnCode != null && returnCode == "0000"){
				$("#payBankDesc").text(data.payList);
			}
		}
	});
}
/**
 * 中行支付提交POST
 */
function bocPaySubmit(){
	$("#bocPay").submit();
	$("#bocPay_div_02").hide();
	$("#bocPay_div_03").show();
}


/**
 * 风险等级匹配时
 * 购买高风险产品 需要 提示
 */
var highRiskIntObj = null;
var highRiskAlerttMillisec = 15;
function payHighRiskPrompt(){
	var fundRisklevel = $("#fundRisklevel").val();
	var invprtp = $("#invprtp").val();
	var invprtpScore = $("#invprtpScore").val();
	var highRiskExplain = $("#highRiskExplain").val();
	var buyType = $.trim($("#buyType").val());
	$(".high_risk_timer").html(" ("+ highRiskAlerttMillisec +"s)");
	//0 < fundRisklevel
	if("5" == fundRisklevel &&  ("0" != invprtp || 60 >= invprtpScore)){
		$("#high_risk_bg #high_risk_content").html(highRiskExplain);
		$("#high_risk_bg").show();
		highRiskIntObj = 
		setInterval(function(){
			if(highRiskAlerttMillisec <= 0){
				$(".high_risk_timer").html("");
				$(".high_risk_know_btn").bind("click",function(){payHighRiskPromptConfirm();});
				$(".high_risk_know_btn").css("background-color","#ca132c");
				$(".high_risk_know_btn").hover(function(){
					$(this).css("background-color","#b60019");
				},function(){
					$(this).css("background-color","#ca132c");
				});
				highRiskAlerttMillisec = 15;
				clearInterval(highRiskIntObj);
			}else{
				$(".high_risk_timer").html(" ("+ highRiskAlerttMillisec +"s)");
				highRiskAlerttMillisec--;
			}
		},1000);
	}else{
		toNext();
	}
}

//高风险提示 确认
function payHighRiskPromptConfirm(){
	var buyType = $.trim($("#buyType").val());
	$(".high_risk_know_btn").unbind("click");
	$(".high_risk_know_btn").css("background-color","#666");
	$(".high_risk_know_btn").hover(function(){
		$(this).css("background-color","#666");
	});
	$("#high_risk_bg").hide();
	toNext();
}

//关闭 购买高风险提示框 并清除计时器
$(".close_high_risk").click(function(){
	highRiskAlerttMillisec = 15;
	$(".high_risk_know_btn").unbind("click");
	$(".high_risk_know_btn").css("background-color","#666");
	$(".high_risk_know_btn").hover(function(){
		$(this).css("background-color","#666");
	});
	$("#high_risk_bg").hide();
	clearInterval(highRiskIntObj);
});

/* 查询产品信息*/
function queryFund(){
	var fundId = $("#fundid").val();
	var period = $("#period").val();
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
        	if(data == null || data == "" || data.fundInfoDto == null){
        		show_tips("该产品不存在或已下架。");
	    		/*window.location.href="/AppService/business/fund/fundList.shtml";*/
        		return;
	    	}
        	var fundInfoDto = data.fundInfoDto;
        	if(fundInfoDto.offLineFund != null && fundInfoDto.offLineFund == "Y"){
        		window.location.href = "/AppService/business/fund/fundDetail.shtml?fundid="+fundId;
        		return;
        	}
        	//这里对产品日期校验
			var subdeadLine = fundInfoDto.subdeadLine;/*认购截止*/
			var currentWorkdate = fundInfoDto.currentWorkdate;
			//过了截止日 = 募集结束
			if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
				show_tips("该产品不存在或已下架。");
				$("#btn_01").val("该产品不存在或已下架。").attr("onclick","javascript:void(0)");
			}
			/* 保存在页面的产品相关数据，计算时候需要用到的*/
    		$("#highRiskExplain").val(fundInfoDto.highRiskExplain);
    		$("#sartBuying").val(fundInfoDto.sartBuying);
    		$("#defaultMoney").val(fundInfoDto.money);
    		$("#minHoldingMoney").val(fundInfoDto.minHoldingMoney);
    		var buyState = $("#buyState").val();
    		if(buyState == 'Y'){
    			$("#moneyZero").val(fundInfoDto.sartBuying);
    		}else{
        		$("#moneyZero").val(fundInfoDto.money);
    		}
    		if(fundInfoDto.typeId == "0400"){
    			$("#redemptionTips").html("我们将在"+formatDate1(fundInfoDto.maturityDate)+"为您发起赎回申请，预计资金将于"+formatDate1(fundInfoDto.arrivalAccountDate)+"到账");
    		}
        }
    });
}

/*查询费率和折扣*/
function queryFeeRateList(){
	var fundId = $("#fundid").val();
	var channelNo = $("#bank").find("option:selected").attr("data-bankno");
	var channelNoList = "";  
	if(!!channelNo){
		channelNoList = channelNo;
	}
	var custLevel = "";
	var money = unformat($("#money").val());
    var urlVal="/AppService/business/queryFeeRateList.xhtml";
    $.ajax({
    	async:false,
		url:urlVal,
		type:"post",
		dataType:'json',
		data:{
			'fundId':fundId,
			'channelNoList':channelNoList,
			'custLevel':custLevel,
			'money':money,
		},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
		},
		success:function(data, textStatus){
			if(!!data){
				$("#rate").val( data.rate);
				$("#commro").val(data.commro);
				$("#feeMode").val(data.feeMode);
			}
		}
	});
}

/**
 * 查询用户是否有持仓
 */
function queryUserWarehouse(fundId){
	 $.ajax({
    	async:false,
		url:"/AppService/business/queryCustTradeInfo.xhtml",
		type:"post",
		dataType:'json',
		data:{
			"fundCode":fundId
		},
		success:function(res){
		     if(res.returnCode=="0000"){
		    	if(res.buyState=="Y"){
		    		$("#buyState").val('Y');
		    	}
		     }
		},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
	   }
	})
}






// 中基协风险揭示书---获取数据
function getRevelation(){
	var fundId = $("#fundid").val();
	//  fundId = getUrlParameter("fundid");
	var period=getUrlParameter("period");
	$.ajax({
        url: '/AppService/business/queryRiskTermList.xhtml',
        data: {
			fundId: fundId,
			period:period
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
		var ietmId1, ietmId2 , ietmId3 ,ietmId4 ,itemState;
		if(item.id == 0){
			ietmId1 = 'checkedHide';
			ietmId2 = '';
			ietmId3 = 'spanHide';
		}else{
			ietmId1= ''
			ietmId2 = 'checkbox';
			ietmId3 = '';
		}
		if(item.state == ''){
			itemState = '';
		}else{
			itemState='checked';
		}
		list_arr.push('<li>'+'<div class="li_input" id="'+ietmId1+'">'+ '<label>'+'<input type="checkbox"' +' '+itemState+ ' ' +'name="'+ ietmId2 +'" id="'+item.id + '" value=" '+item.id + '" />'+ '<div class="show-box"></div>'+'</label></div>'+'<div class="li_p"><p><span id="' +ietmId3+ '">'+item.id +', </span>'+item.content+'</p></div></li>')
	}
	return list_arr
}
// 全部阅读
$(".allReading").click(function(){
	$('input[name="checkbox"]').attr('checked', true);
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
	$("#div_06,#bocPay_div_033").hide();
});
// 取消
$(".bottom_left").click(function(){
	var chk_value =[];
	var ant;
	$('input[name="checkbox"]:checked').each(function(){  
		ant = $('input[name="checkbox"]:checked')
		chk_value.push($(this).val());    
	});
	// chk_value数组必须要有值，无值会导致后台报错
	if(chk_value.length > 0){
		returnState(chk_value);
	}
	$("#div_06,#bocPay_div_033").hide();
});
// 遍历
$("#revelation_data").on("change",'input[name="checkbox"]',function(){
	var valueData = nextStep();
	if(valueData){
		$('.confirm').show().addClass('valData');
		$('.allReading').hide();
	}else{
		$('.allReading').show();
		$('.confirm').hide().removeClass('valData');
	}
});
function nextStep(){
	var chks = document.querySelectorAll('input[name="checkbox"]');
	var result = [];
	for (var i = 0; i < chks.length; i++) {
		var chk = chks[i];
		result.push(chk.checked);
	} 
	var valueData = result.every(function(v,i){
		return v
	})
	return valueData
}
// 确认和取消都需要把选择checkbox的状态返回后台
function returnState(ids,type){
	var idst = ids.join(',');
	var fundId = $("#fundid").val();
	// fundId = getUrlParameter("fundid");
	var period=getUrlParameter("period");
	$.ajax({
        url: '/AppService/business/saveUserTermsInfo.xhtml',
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