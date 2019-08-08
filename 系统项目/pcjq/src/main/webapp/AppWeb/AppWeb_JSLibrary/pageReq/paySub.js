﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿﻿var timer = 60;
var timer1 = 10;
var timer2 = 10;
var timer3 = 10;
var riskWarnGoonFlag = true;
var  isOpenMgm;//积分功能开关
var pageId = $("#pageId").val();

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
	}else if($(data).val() == 'Y'){
		$(data).parent().find("span").html('（到期后自动买入下一期）');
		$("#redemptionTips").hide();
	}
}



$(document).ready(function(){
	integralOpenOrClose();//mgm 积分开关
	getUserRequest("pc_paySub_01");
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
	checkedUserLever();
	var money = getUrlParameter("money").trim();
	money = removeSpecialStr(money);
	
	if(fundid != null && fundid != ""){
		$("#fundid").val(fundid);
	}else{
		show_tips("该产品不存在或已下架。");
		window.location.href="/AppService/business/fund/fundList.shtml";
		return;
	}
	queryFund();/*查询产品详情*/
	getRevelation();// 页面开始请求中基协风险揭示书改造数据
	if(money != null && money != "" && parseFloat(money) > 0){
		$("#money").val(formatNumber(money));/*如果上一个页面有传入金额，则认购金额默认为传入的金额*/
	}
	checkedMoney('money');

	/*20180103crm数据迁移需求, 移除查询旧版PDF合同*/
	queryEcontrantByFundIdN();/*查询 产品合同 txt*/
	
	queryFeeRateList();/* 查询费率和折扣*/
	getSupportPayBankDesc();
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
	}else if(money > displayLimit && buyType != "4"){
		$("#money_error").html("仅剩下"+numDiv(displayLimit,10000)+"万元份额").show();
		return false;
	}else if(buyType == "4" && money > scale){
		$("#money_error").html("预约金额不能高于产品发售规模").show();
		return false;
	}else {
		$("#money_error").hide();
		$("#money").val(formatNumber(unformat(money),","));
		$("#payMoney").val(money + parseFloat(unformat($("#rate").val())));
		
		queryFeeRateList();
		
		$("#money02 b").html("￥"+formatNumber((money+parseFloat(unformat($("#rate").val()))),","));
		$("#money02 em").html(toUpperCase(money+parseFloat(unformat($("#rate").val()))).replace(/整/g,""));
		$("#money03 dd").html("￥"+formatNumber((money+parseFloat(unformat($("#rate").val()))),","));
		$("#money03 span").html(toUpperCase(money+parseFloat(unformat($("#rate").val()))).replace(/整/g,""));
		return true;
	}
}
/* 查询产品信息*/
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
        	$("#templetId").val(fundInfoDto.templetId);
        	$("#apkind").val(fundInfoDto.fundState);
        	var today = ""+fundInfoDto.currentWorkdate;
        	//这里对产品日期校验
        	var buyType = "1";
        	var appointDate = fundInfoDto.appointDate;/*预约开始日期*/
			var appointEndDate = fundInfoDto.appointEndDate;/*预约结束日期*/
			var salesDate = fundInfoDto.salesDate;/*发售日*/
			var subdeadLine = fundInfoDto.subdeadLine;/*认购截止*/
			var currentWorkdate = fundInfoDto.currentWorkdate;
			var displayLimit = parseFloat(fundInfoDto.displayLimit);/* 页面展示剩余额度*/
			var type=fundInfoDto.typeId;
			var buyState ='';
			if(type == '0500' || type == '0400' || type == '0110'){
				queryUserWarehouse(fundInfoDto.fundId);
				buyState = $('#buyState').val();
			}
			if(type == '0500'){
				
				$(".confirm-order-info-con dl").eq(2).find("dt").text("预估起息时间")
				//认购期
				if(daysBetween(currentWorkdate,salesDate) >= 0){
					//额度是否足够
					if(displayLimit > 0){
						buyType = "2";
					}else{
						buyType = "4";
						document.title="排队信息确认";
						$("#btn_01").val("参与排队").attr("onclick","javascript:toNext()");
						$("#btn_02").attr("onclick","fundTradeLineUp()");
						$("#div_05 h2").html("排队成功");
						$("#div_05 span.con01").html("成功加入排队，请等待客服人员分配额度");
					}
				//预约期
				}else{
					//额度是否足够
					if(displayLimit > 0){
						buyType = "1";
					}else{
						buyType = "4";
						document.title="排队信息确认";
						$("#btn_01").val("参与排队").attr("onclick","javascript:toNext()");
						
						$("#btn_02").attr("onclick","fundTradeLineUp()");
						$("#div_05 h2").html("排队成功");
						$("#div_05 span.con01").html("成功加入排队，请等待客服人员分配额度");
					}
				}
			}else{
				//过了截止日 = 募集结束
				if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
					show_tips("该产品不存在或已下架。");
					$("#buyType").val(buyType);
					$("#btn_01").val("该产品不存在或已下架。").attr("onclick","javascript:void(0)");
					return;
				//认购期= 当前时间 大于等于 认购起始日 并且 小于 认购截止日 
				}else if(daysBetween(currentWorkdate,salesDate) >= 0 && daysBetween(currentWorkdate,subdeadLine) <= 0){
					//额度是否足够
					if(displayLimit > 0){
						buyType = "2";
					}else{
						buyType = "4";
						document.title="排队信息确认";
						$("#btn_01").val("参与排队").attr("onclick","javascript:toNext()");
						$("#btn_02").attr("onclick","fundTradeLineUp()");
						$("#div_05 h2").html("排队成功");
						$("#div_05 span.con01").html("成功加入排队，请等待客服人员分配额度");
					}
				//预约期= (预约开始日 <= 当前时间  <= 预约截止日 )
				}else if(daysBetween(currentWorkdate,appointDate) >= 0 && daysBetween(currentWorkdate,appointEndDate) <= 0){
					//额度是否足够
					if(displayLimit > 0){
						buyType = "1";
					}else{
						buyType = "4";
						document.title="排队信息确认";
						$("#btn_01").val("参与排队").attr("onclick","javascript:toNext()");
						
						$("#btn_02").attr("onclick","fundTradeLineUp()");
						$("#div_05 h2").html("排队成功");
						$("#div_05 span.con01").html("成功加入排队，请等待客服人员分配额度");
					}
				}else{
					show_tips("该产品预约期未开始！");
					$("#econtrant_bg").remove();
					$("#money_amount_bg").remove();
					$("#buyType").val(buyType);
					$("#btn_01").val("预约期未开始").attr("onclick","javascript:void(0)");
					return;
				}
			}

			$("#buyType").val(buyType);
			if (type=='0110') {
				$('#puySubMethod').show();
			}
			if(type=='0400'){
				$('#puySubMethod1').show();
				$(".confirm-order-info-con dl").eq(2).find("dt").text("预估起息时间");
				$("#redemptionTips").html("我们将在"+formatDate1(fundInfoDto.maturityDate)+"为您发起赎回申请，预计资金将于"+formatDate1(fundInfoDto.arrivalAccountDate)+"到账").show();
			}
			$('#typeId').val(type);
        	var maybe = 0;
        	var term = fundInfoDto.term;
        	var money = fundInfoDto.money;
        	var profit = fundInfoDto.profit;
        	var latestNewValue = fundInfoDto.latestNewValue;
        	var sevenDayAnnualy = '--';
        	if(data != null && data.resultCode == "0000"){
        		
        		$("#productName_01,#productName_02").html(fundInfoDto.adname).addClass("cursor").addClass("underline").attr("onclick","toFundDetail(\""+fundInfoDto.fundId+"\",\""+fundInfoDto.period+"\");");/*产品名称*/

        		if(type=='0500'){
        			$("#inputType").html('最新净值');
					if(!latestNewValue){
						latestNewValue ='1.0000';
					}
					$("#profit").html("<i>" + latestNewValue.split(".")[0] + "</i>." + latestNewValue.split(".")[1]);/*最新净值*/
        		}else if(type=='0400'){
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
	        				sevenDayAnnualy = fundInfoDto.sevenDayAnnualy;
	        			}
						if(fundInfoDto.state == '1'){
	        				sevenDayAnnualy = '--'
	        			}
	        			$("#profit").html("<i>" + sevenDayAnnualy +"</i>");/*申购期七日年化收益为*/
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
        		if((type=='0400' || type=='0500') && fundInfoDto.state == '0'){
        			$("#interestDate").html(formatDate(fundInfoDto.interestDate1));/* 起息*/
        		}else{
        			$("#interestDate").html(formatDate(fundInfoDto.interestDate));/* 起息*/
        		}

        		$("#maturityDate").html(formatDate(fundInfoDto.maturityDate));/* 到期*/
        		if(type=='0500'){
        			$(".order-last").hide();
        			$(".confirm-order-info-con dl").css('width','30%');
        		}
        		$("#fundRisklevel").val(fundInfoDto.fundRisklevel);
        		
        		var money = getUrlParameter("money");
        		money = removeSpecialStr(money);
        		if(money == null || money == "" || parseFloat(money) <= 0){
	        		$("#money").val(formatNumber(fundInfoDto.money,','));
        		}
        		$("#adName").val(fundInfoDto.adname);
        		$("#scale").val(fundInfoDto.scale);
        		$("#moneyStep").val(fundInfoDto.moneyStep);
        		$("#sartBuying").val(fundInfoDto.sartBuying);
        		$("#defaultMoney").val(fundInfoDto.money);
				if(buyState == 'Y'){
					$("#moneyZero").val(fundInfoDto.sartBuying);
				}else{
					$("#moneyZero").val(fundInfoDto.money);
				}
        		$("#displayLimit").val(fundInfoDto.displayLimit);
        		$("#appointDate").val(fundInfoDto.appointDate);
        		$("#appointEndDate").val(fundInfoDto.appointEndDate);
        		$("#salesDate").val(fundInfoDto.salesDate);
        		$("#subdeadLine").val(fundInfoDto.subdeadLine);
        		$("#fundid").val(fundInfoDto.fundId);/* 产品ID*/
        		$("#fundState").val(fundInfoDto.state);/* 产品状态*/
        		$("#fundState").val(fundInfoDto.state);/* 产品状态*/
        		$("#highRiskExplain").val(fundInfoDto.highRiskExplain);
        		/* 保存在页面的产品相关数据，计算时候需要用到的*/

        	}
        }
    });
}
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
				gotoRealNamePage("confirmOrderId","event_pay_realNameId");
			}else if(data.isSetTradePassword != "Y"){
				gotoSetTPassword();
			}else if(data.returnCode=='0000' && data.isSetTradePassword == "Y"){
				//合格投资者认证开关
			    var param=queryParamComm("SYSTEM","ACINVCONF","");
				var pmnm="";
				for(var i=0;i<param.length;i++){
					if(param[i].pmco=="MAIN"){
						pmnm=param[i].pmnm;
					}
			    }
				if(pmnm=="1"){
				   qualified()
				}
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

function queryUserTaxResidentType(){
	var flag = true;
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
				if(data.taxResidentType == ""){
				       show_tips("请先完善“税收居民类型”再进行风险测评。");
                                       $("#tips_btn3_btn").remove()
					 setTimeout(function(){
				    	 window.location.href="/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO&taxResident=true"
				       },2000)
					flag = false;
				}
			}
		}
	});
	return flag;
}


function toMyaccount(){
	addCookie('riskUrl',location.href);
	window.location.href = "/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO&type=riskLevel&pageSourceId="+pageId+"&eventId=event_payId";
}

/*查询电子合同 TXT*/
function queryEcontrantByFundIdN(){
	var fundid = getUrlParameter("fundid");
	var period = getUrlParameter("period");
	var money = $("#money").val();
	var invprtp = $("#invprtp").val();
	var invprtpScore = $("#invprtpScore").val();
	fundid = removeSpecialStr(fundid);
	$.ajax({
		async:false,
		url : "/AppService/business/queryFundContractById.xhtml",
		data : {
			"fundId":fundid,
			"period":period
		},
		dataType : "json",
		cache : false,
		type:"POST",
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			var htmls = "";
			if(data.fundContractDto != null){
				$("#contractVer").val(data.fundContractDto.version);
				htmls += "我已经阅读并同意";
				htmls += "<a href='javascript:toContract(\""+fundid+"\",\""+unformat($("#money").val())+"\");'>《"+data.fundContractDto.templateName+"》</a>";
				htmls += "<a href='/AppService/business/fund/paySubRiskScript.shtml' target=\"_blank\">《风险揭示函》<\/a>";
				
				if("0" != invprtp || !invprtpScore || 60 > invprtpScore ){
					htmls += "<a href='/AppService/business/fund/commonInvstTradeInfo.shtml' target=\"_blank\">《普通投资者交易告知书》<\/a>";
				}
			}
			if(htmls != null && htmls != ""){
				$("#econtrant").append(htmls);
			}
		}
	});
}
/*判断用户和产品风险等级*/
function checkUserRiskLever(){
	getUserRequest("pc_paySub_02");
	var riskLevel = $("#riskLevel").val();
	var specialRiskLevel = $("#specialRiskLevel").val();
	var fundRisklevel = $("#fundRisklevel").val();
	var invprtp = $("#invprtp").val();
	var invprtpScore = $("#invprtpScore").val();
	var isControl = $("#isControl").val();
	var isNotBeneficiary = $("#isNotBeneficiary").val();
	var isBadHonesty = $("#isBadHonesty").val();
	var mobile = $("#mobile").val();
	var money = parseFloat(unformat($("#money").val())); /*详情*/
	var moneyZero = parseFloat(unformat($("#moneyZero").val()));/*认购起点*/
	var moneyStep = parseFloat(unformat($("#moneyStep").val()));/*认购步长*/
	
	if("Y"==isControl || "Y"==isNotBeneficiary || "Y"==isBadHonesty){
		$("#risk_properTips").show();
		return ;
	}
	//为空 给默认值
	if(!specialRiskLevel){
		specialRiskLevel = "5";
	}
	var flag = queryUserTaxResidentType();
	if(!flag){
		return;
	};
	/* 用户等级不够*/
	if(!checkedMoney("money")){
		return ;
	}else if(!Validater.isMobilePhoneNumber(mobile)){
		$("#mobile_error").html("请输入正确的手机号").show();
		return;
	}else if(!$("#ischecked").is(":checked")){
		$("#check_error").html("请勾选阅读并同意产品合同").show();
		return;
	}else if(fundRisklevel == "" || (fundRisklevel != "1" && fundRisklevel != "2" && fundRisklevel != "3"&& fundRisklevel != "4"&& fundRisklevel != "5")){
		show_tips("产品状态异常！");
		window.location.href="/AppService/index.shtml";
		//用户等级大于产品等级 || 特殊用户等级大于 产品等级
		//&&一次提示标记
		//&&是否专业投资者
	}else if((riskLevel < fundRisklevel || specialRiskLevel < fundRisklevel) && riskWarnGoonFlag && ("0" != invprtp || 60 >= invprtpScore)){
		$("#tips").show();
	}else{
		/**中基协风险揭示书改造需求---添加--开始**/
		$("#div_06,#bocPay_div_03").show();
		// var valueData = nextStep();
		$('.confirm').click(function(){
			var valueData = nextStep();
			if(valueData && ($('.confirm').hasClass('valData'))){
				payHighRiskPrompt();
			}
		});
		
		/**中基协风险揭示书改造需求---添加--结束**/

		// 中基协风险揭示书改造需求注释方法是原有逻辑
		// payHighRiskPrompt();
	};
}

/*确认订单信息   提交按钮   确认订单信息同时会预约产品(预下单)*/
function fundAppoint(){
	$("#high_risk_bg").hide();
	var fundId = getUrlParameter("fundid");
	fundId = removeSpecialStr(fundId);
	var money = parseFloat(unformat($("#money").val())); /*详情*/
	var moneyZero = parseFloat(unformat($("#moneyZero").val()));/*认购起点*/
	var moneyStep = parseFloat(unformat($("#moneyStep").val()));/*认购步长*/
	var apkind = $("#apkind").val();
	var payType = "2";
	var fundState = $("#fundState").val();
	var displayLimit = parseFloat($("#displayLimit").val());
	var serType = "APO";
	var serialno = $("#serialno").val();
	var mobile = $("#mobile").val();
	var fee = $("#rate").val();/*认购费用*/
	var commro = $("#commro").val();/*折扣率*/
	var typeId = $("#typeId").val();
	var renew=$('#puySubMethod input[name="renew"]:checked ').val();
	if(typeId =='0400'){
		renew=$('#puySubMethod1 input[name="renew"]:checked ').val();
	}
	if(!checkedMoney("money")){
		return ;
	}else if(typeId=='0110' && renew == null || renew ==''){
		show_tips("请选择续投方式");
		return;
	}else if(money == null || money == "" || isNaN(parseFloat(unformat(money)))){
		show_tips("请输入正确的金额！");
		return;
	}/*else if((money < moneyZero) || (money - moneyZero) % parseFloat(moneyStep) != 0 || parseFloat(money) > parseFloat(displayLimit)){
		show_tips("抱歉!本产品"+numDiv(parseFloat(moneyZero),10000)+"万起售，"+numDiv(parseFloat(moneyStep),10000)+"万递增");
		return;
	}*/
	else if(!$("#ischecked").is(":checked")){
		$("#check_error").html("请勾选阅读并同意产品合同").show();
		return;
	}else if(!Validater.isMobilePhoneNumber(mobile)){
		show_tips("请输入正确的手机号码");
		return;
	}else{
		$.ajax({
			url:"/AppService/business/fundAppoint.xhtml",
			type:"post",
			dataType:'json',
			data:{
				'fundId':fundId,
				'money':encodeURI(money),
				'payType':payType,
				'fundState':fundState,
				'serType':serType,
				'serialno':serialno,
				'mobile':mobile,
				'fee':fee,
				'commro':commro,
				'renew' : renew
			},
			error:function(){
				show_tips("网络繁忙，请稍后再试。");  
			},
			success:function(data, textStatus){
				var serialNo = '';
				if(data.resultCode=="0000"){
					serialNo = data.resultFundTradeDto.serialNo;
					$("#serialNo").val(serialNo);
					$("#serType").val("A2T");
					
					queryUserTradeAcctInfoList();/*查询交易账号和银行卡信息*/
					
					$("#wrad div.schedule02").addClass("current");
					/* 成功的样式*/
				    $("#div_01").hide();
					$("#div_02").show();
					scroll(0,0);
				}else{
					show_tips("预下单失败："+data.resultMsg);
					return;
				}
			}
	    });
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
				//selectPaytype(false);
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
	
	var buyType = $("#buyType").val();/*排队单*/
	if(buyType != null && buyType == "4"){
		$("#payType").val("1");
		$("#paytypeList #online-pay-way").parent().hide();
		$("#paytypeList #offline-pay-way").click().change().parent().show();
		return;
	}
	
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
	    $.ajax({
	    	async:false,
	        url: "/AppService/business/getVerifyCodeForTrade.xhtml",
	        data: {
	        	"mobile": mobile,
		        "money":money,
		        "bankcardNo":bankcardNo,
		        "bankName":bankName,
		        "bnsType":bnsType
	        },
	        type: 'post',
	        dataType: "json",
	        cache: false,
	        error : function(textStatus, errorThrown) {  
	        	show_tips("网络繁忙，请稍后再试。");  
	        }, 
	        success : function (data){
	        	if(data.sessionID != null && data.sessionID != "" && data.sessionID != "nullId" && data.returnCode == "0000"){
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
	getUserRequest("pc_paySub_03");
	queryFeeRateList();
	var fundid = $("#fundid").val();
	var money = parseFloat(unformat($("#money").val())); /*详情*/
	var productMoney = parseFloat(unformat($("#moneyZero").val()));
	var moneyStep = parseFloat(unformat($("#moneyStep").val()));/*认购步长*/
	var mobile = $("#mobile").val();/* 用户手机号码*/
	var payMobile = $("#payMobile").val();/* 银行预留手机号码*/

	var paytype = $("#payType").val();/*支付方式*/
	var tradeAcco = $("#tradeAcco").val();/*交易账号*/
	var serialNo = $("#serialNo").val();/*交易流水号*/
	var fundState = $("#fundState").val();/*产品状态*/
	var serType = $("#serType").val();/*业务类型*/

	var verifyCode = $("#verifyCode").val();/* 短信验证码*/
	var sessionID = $("#sessionID").val();
	var typeId=$('#typeId').val();
	var renew=$('#puySubMethod input[name="renew"]:checked ').val(); /* 产品是否续投 */
	if(typeId =='0400'){
		renew=$('#puySubMethod1 input[name="renew"]:checked ').val(); /* 产品是否续投 */
	}
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
	}else if(typeId=='0110' && renew == null || renew ==''){
		show_tips("请选择续投方式");
		return;
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
			'renew':renew,
			'commro':commro,
			'feeMode':feeMode
		};
		$("#tpassword,#verifyCode").val("");
		if(paytype == "0"){
			$("#pay_wait_tips").show();
		}

        // MGM需求
		var inviterPhone=$("input[name=inviterPhone]").val() 
		if(inviterPhone!=""){
			if(!isMobile(inviterPhone)){
				show_tips("邀请人手机号码格式不正确");
				return;
			}
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
								$("#orderUrl").val(bocSignParaDto.orderUrl);
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
							$("#wrad div.schedule03").addClass("current");
						}
						if(isOpenMgm=="1"){
                            inviter()  //绑定邀请人
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
						countDown3();
						$("#wrad div.schedule03").addClass("current");
						sendSmsMsg();
						if(isOpenMgm=="1"){ // mgm积分开关
                            inviter()  //绑定邀请人
						}
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
				var rate = data.rate;
				var commro = data.commro;
				var feeMode = data.feeMode;
				if(rate == null || rate == "" || rate == "0"){
					$("#viewFee").html("");
				}else{
					$("#viewFee").html("+<em>"+formatNumber(rate,',')+"</em>元（认购费）");
					$("#rate").val(rate);
				}
				$("#commro").val(commro);
				$("#feeMode").val(feeMode);
			}else{
				$("#viewFee").html("");
			}
		}
	});
}
function changeBg(_id){
	$("#"+_id).attr("style","");
}
function toContract(fundid,money){
	if(!checkedMoney('money')){
		return;
	}
	var period = getUrlParameter("period");
	var m = unformat($("#money").val());
	var link = "/AppService/business/fund/contract.shtml?period="+period+"&fundId="+fundid+"&money="+m;
	window.open(link);
}
/* 排队单  下一步 */
function toNext() {
	getUserRequest("pc_paySub_02");
	var mobile = $("#mobile").val();
	var fundRisklevel = $("#fundRisklevel").val();
	var specialRiskLevel = $("#specialRiskLevel").val();
	var invprtp = $("#invprtp").val();
	var invprtpScore = $("#invprtpScore").val();
	var isControl = $("#isControl").val();
	var isNotBeneficiary = $("#isNotBeneficiary").val();
	var isBadHonesty = $("#isBadHonesty").val();
	var riskLevel = $("#riskLevel").val();
	var renew=$('#puySubMethod input[name="renew"]:checked ').val();
	var typeId=$('#typeId').val();
	if(typeId=='0110' && renew == null || renew ==''){
		show_tips("请选择续投方式");
		return;
	}
	//为空 给默认值
	if(!specialRiskLevel){
		specialRiskLevel = "5";
	}
	
	if(!checkedMoney("money")){
		return ;
	}else if(!Validater.isMobilePhoneNumber(mobile)){
		$("#mobile_error").html("请输入正确的手机号").show();
		return;
	}else if(!$("#ischecked").is(":checked")){
		$("#check_error").html("请勾选阅读并同意产品合同").show();
		return;
	}else if("Y"==isControl || "Y"==isNotBeneficiary || "Y"==isBadHonesty){
		$("#risk_properTips").show();
		return ;
	}else if(fundRisklevel == "" || (fundRisklevel != "1" && fundRisklevel != "2" && fundRisklevel != "3"&& fundRisklevel != "4"&& fundRisklevel != "5")){
		show_tips("产品状态异常！");
		window.location.href="/AppService/index.xhtml";
	}else if((riskLevel < fundRisklevel || specialRiskLevel < fundRisklevel) && riskWarnGoonFlag && ("0" != invprtp || 60 >= invprtpScore)){
		$("#tips").show();
		return;
	}else{
		$('#renewInput').val(renew);
		$("#div_01").hide();
		$("#div_02").show();
		$("#wrad div.schedule02").addClass("current");
		queryUserTradeAcctInfoList();/*查询交易账号和银行卡信息*/
	}
}
/* 下排队单 */
function fundTradeLineUp() {
	getUserRequest("pc_paySub_03");
	var fundid = $("#fundid").val();
	var money = parseFloat(unformat($("#money").val())); /*详情*/
	var productMoney = parseFloat(unformat($("#moneyZero").val()));
	var moneyStep = parseFloat(unformat($("#moneyStep").val()));/*认购步长*/
	var mobile = $("#mobile").val();/* 用户手机号码*/
	var payMobile = $("#payMobile").val();/* 银行预留手机号码*/
	
	var paytype = $("#payType").val();/*支付方式*/
	var tradeAcco = $("#tradeAcco").val();/*交易账号*/
	var serialNo = $("#serialNo").val();/*交易流水号*/
	var fundState = $("#fundState").val();/*产品状态*/
	var serType = $("#serType").val();/*业务类型*/
	var renew = $('#renewInput').val();
	var tpassword = $("#tpassword").val();/* 交易密码*/
	var verifyCode = $("#verifyCode").val();/* 短信验证码*/
	var sessionID = $("#sessionID").val(); 
	
	var fee = $("#rate").val();/*认购费用*/
	var commro = $("#commro").val();/*折扣率*/

	if(!checkedMoney("money")){
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
	}else if(isEmpty(tpassword)){
		show_tips("请输入交易密码！");
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
			'tPassword':tpassword,
			'verifyCode':verifyCode,
			'sessionID':sessionID,
			'fee':fee,
			'commro':commro,
			'renew':renew
		};
		$("#tpassword,#verifyCode").val("");
	    var urlVal="/AppService/business/fundTradeLineUp.xhtml";
	    $.ajax({
			url:urlVal,
			type:"post",
			dataType:'json',
			data:params,
			error:function(){
				show_tips("网络繁忙，请稍后再试。"); 
			},
			success:function(data, textStatus){
				if(paytype == "0"){/* 线上*/
					if(data.resultCode=="0000"){/* 支付成功*/
						$("#div_03 span.con01 em").html(formatNumber(parseFloat(unformat($("#money").val()))+parseFloat(unformat($("#rate").val()))));
						$("#div_02").hide();
						$("#div_03").show();
						countDown1();
						$("#wrad div.schedule03").addClass("current");
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
					}else if(data.errCode=="YB001"){/* 成功受理*/
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
						$("#div_05").show();
						/*支付成功则给用户发送*/
						sendSmsMsg();
						countDown3();
						$("#wrad div.schedule03").addClass("current");
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
    var appointDate = $.trim($("#appointDate").val());
    var salesDate =  $.trim($("#salesDate").val());
    var subdeadLine =  $.trim($("#subdeadLine").val());
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
        "serialNo" : $("#serialNo").val()
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
function toFundDetail(fundId,period){
	window.open("/AppService/business/fund/fundDetail.shtml?fundid="+fundId+"&period="+period);
}
function closeTipsAndReturn(){
	close_tips("hint_tips_btn2");
	$("#serType").val("");
	$("#div_01").show();
	$("#div_02").hide();
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
/*查询中行支付结果*/
function queryPayCommandByBankAccoNo(){
	var tradeAcct = $("#tradeAcco").val();
	var bankNo = $("#bank").find("option:selected").attr("data-bankNo");/*银行代码*/
	var channelNo = $("#bank").find("option:selected").attr("data-realBankNo");/*渠道代码*/
	if(bankNo == null || bankNo != "004" || tradeAcct == null || tradeAcct == "" || channelNo == null || channelNo == ""){
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

			if(returnCode != null && returnCode == "0000"){/*扣款成功*/
				$("#bocPay_div_03").hide();
				$("#div_02").hide();
				$("#bocPayAccept_div_04").show();
				countDown2();
				$("#wrad div.schedule03").addClass("current");
			}else if(returnCode != null && returnCode=="YB001"){/* 成功受理*/
				$("#bocPay_div_03").hide();
				$("#div_02").hide();
				$("#bocPayAccept_div_04").show();
				countDown2();
				$("#wrad div.schedule03").addClass("current");
			}else{
				$("#bocPay_div_03").hide();
				show_tips(data.returnMsg);
			}
		}
	});
}
function contractSign_close(_id){
	$("#"+_id).hide();
	$("#paytypeList #offline-pay-way").click().change().parent().show();
}
function bocPayFail_close(_id){
	$("#"+_id).hide();
	document.location.reload();
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






/** 收益及取出规则 S
 */

/* 弹窗提示显示  无按钮 */
function show_window(){
	$("#hint_tips_btn4").show();
	var innerHeight=window.innerHeight;
	var innerWidth=window.innerWidth;
	if (innerHeight>=955 && innerWidth>=1920) {
		
	}else{

	}
}

function close_window(){
	$("#hint_tips_btn4").hide();
}
/**
 *	 收益及取出规则 E
 */



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
		if("4" != buyType){
			fundAppoint();
		}else{
			toNext();
		}
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
	if("4" != buyType){
		fundAppoint();
	}else{
		toNext();
	}
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

/**
 * 判断用户是否为合格投资者
 */
function qualified(){
	$.ajax({
    	async:false,
		url:"/AppService/business/queryQualifiedUserInfoByIdno.xhtml",
		type:"post",
		dataType:'json',
		success:function(data){
		     if(data.returnCode=="0000"){
		     	if(data.data&&data.data.length>0){
        	  	   	  var status=data.data[0].statusRecord.status
	    	  	   	   if(status!="S"){
	  	   	        	    $("#qualifed").djDialog({
						          width:500,
						          title:"温馨提示",
						          cancel:{
						              text:"线下认证",
						              callBack:function(){
						                  $("#qualifed").djDialog("close");
						              }
						          },
						          ok:{
						              text:"线上认证",
						              callBack:function(){
						              	   if(status=="F"){
							              	    $.ajax({
						    			            url  :'/AppService/business/modifyAccreditedInvestorInfoStatus.xhtml',
						    			            type : 'POST',
						    			            data : {"updateStatus":"R"},
						    			            async:false,
						    			            dataType : 'json',
						    			            success : function(data) {
						    			           }
					    				       })
						              	   }
						                   location.href="/AppService/business/qualified/qualified.shtml";
						              }
						          }
						     });
					         $(".buttonPop").find("a:nth-child(1)").addClass("popbotton2").removeClass("popbotton1").css("background","#fff");
					         $(".buttonPop").find("a:nth-child(2)").addClass("popbotton1").removeClass("popbotton2");
					         $(".popClose").remove() 
	    	  	   	   }
        	  	   	   
   	  	   	    }else{
                     //当数据为空的时查询投资两年的经历及资产是否有500万
  	  	   	   	    $.ajax({
				    	async:false,
						url:"/AppService/business/queryAccreditedInvestorConditions.xhtml",
						type:"post",
						dataType:'json',
						success:function(data){
						     if(data.returnCode=="0000"){
						     	 if(!data.data.financialCertificate||!data.data.investCertificate){
						     	 	$("#qualifed").djDialog({
								          width:500,
								          title:"温馨提示",
								          cancel:{
								              text:"线下认证",
								              callBack:function(){
								                  $("#qualifed").djDialog("close");
								              }
								          },
								          ok:{
								              text:"线上认证",
								              callBack:function(){
								                   location.href="/AppService/business/qualified/qualified.shtml";
								              }
								          }
								     });
	                                  $(".buttonPop").find("a:nth-child(1)").addClass("popbotton2").removeClass("popbotton1").css("background","#fff");
							         $(".buttonPop").find("a:nth-child(2)").addClass("popbotton1").removeClass("popbotton2");
							         $(".popClose").remove() 
						     	 }
						     }
						}
				     })
      	  	   	   }
 
		     }else if(data.returnCode=="9005"){
		      	 DJ.alert(data.returnMsg,'',function(){
		      	 	location.href="/AppService/business/bank/realName.shtml"
		      	 })
		      	 $(".popClose").remove()
        	 }else{
        	 	 DJ.alert(data.returnMsg);
        	 }
	    }
	})
}


/*邀请人手机号接口*/
function inviter(){
    var phoneNumber = $("#inviterPhone").val();
    var number = phoneNumber.replace(/\s*/g,"");
    if(phoneNumber!=""||phoneNumber!=null){
        $.ajax({
            async: false,
            url: "/AppService/business/integral/bindingReferrerByPhoneNumber.xhtml",
            data: {
                'phoneNumber': number
            },
            type: 'post',
            dataType: "json",
            cache: false,
            error: function(textStatus, errorThrown) {
                show_tips("网络繁忙，请稍后再试");
            },
            success: function(data){
                if(data.data == '0'){
                    show_tips("绑定推荐人失败");
                    $("#inviterPhone").val('');
                }else if(data.data=='1'){
                    show_tips("绑定推荐人成功");
                }
            }
        });
    }
}
/*判断邀请人输入框是否显示*/
function inviterShow(){
	$.ajax({
		async: false,
		url: "/AppService/business/integral/recommendInputBox.xhtml",
		data: {},
		type: 'get',
		dataType: "json",
		cache: false,
		error: function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试");
		},
		success: function(data){
			if(data.show == 1){
				$('.inviterClass').show();
			}
		}
	});
}


// 积分功能开关
function integralOpenOrClose() {
    $.ajax({
        url: '/AppService/integral/enableIntegral.xhtml',
        data: {},
        dataType: 'json', //服务器返回json格式数据
        type: 'get', //HTTP请求类型
        success: function(data){
            if (data.data== '1') {
                isOpenMgm='1';
                inviterShow();
            } else if(data.data='0'){
                isOpenMgm='0';
            }
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
		// list_arr.push(`<li><div class="li_input" id="${item.id == 0 ? 'checkedHide':''}"><label><input type="checkbox" ${item.state =='' ? '': 'checked'} name="${item.id == 0 ? '':'checkbox'}" id="${item.id}" value="${item.id}" /> <div class="show-box"></div></label></div><div class="li_p"><p><span id="${item.id == 0 ? 'spanHide':''}">${item.id}, </span>${item.content}</p></div></li>`)
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
	// chk_value数组必须要有值，无值会导致后台报错
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
	// var valueData = result.every((v,i)=>{
	// 	return v
	// })
	var valueData = result.every(function(v,i){
		return v
	})
	return valueData
}
// 确认和取消都需要把选择checkbox的状态返回后台
function returnState(ids,type){
	var idst = ids.join(',');
	// var fundId = getUrlParameter("fundid");
	var fundId = $("#fundid").val();
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