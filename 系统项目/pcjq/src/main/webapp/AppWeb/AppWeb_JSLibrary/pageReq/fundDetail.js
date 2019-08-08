$(document).ready(function(){
	getUserRequest("pc_fundDetail");
/* 	checkedUserLever();*/
	$(".nav.fr ul li a").removeClass("current").eq(1).addClass("current");
	queryFund();
	document.title = "产品详情_"+$("#adname").text();
	$(".box-third .content .box01").click(function(){
	    $(this).toggleClass("act").siblings().removeClass("act");
	})
	queryFeeRateList();
	/* 产品详情底部问答 b */
	$(".wordlimit").each(function(i) {
		var divH = $(this).height();
		var $p = $("p", $(this)).eq(0);
		while ($p.outerHeight() > divH) {
			$p.text($p.text().replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
		}
	});
	$(".box-third .content .box01").toggle(
		function() {
			$(this).addClass("act").siblings().removeClass("act");
			var ellipsisText = $(this).find(".right p").attr("data-title");
			$(this).find(".right p").html(ellipsisText);
		}, 
		function() {
			if ($(this).hasClass("act")) {
				$(this).removeClass("act").siblings().removeClass("act");
				$(".wordlimit").each(function(i) {
					var divH = $(this).height();
					var $p = $("p", $(this)).eq(0);
					while ($p.outerHeight() > divH) {
						$p.text($p.text().replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
					}
				});
			} else {
				$(this).addClass("act").siblings().removeClass("act");
	            var ellipsisText=$(this).find(".right p").attr("data-title");
	            $(this).find(".right p").html(ellipsisText);
			}
		}
	);
})

/* 查询产品信息 */
function queryFund(){
	var fundId = getUrlParameter("fundid");	
	var period = getUrlParameter("period");	
	fundId = fundId.replace("#", "");
	period = period.replace("#", "");
	period = removeSpecialStr(period);
	fundId = removeSpecialStr(fundId);
	$.ajax({
		async:false,
		url : "/AppService/business/queryFund.xhtml",
		data : {
			fundId:fundId,
			period:period
		},
		dataType : "json",
        type:"POST",
		cache : false,
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			if(data.resultCode == "0000"){
				var fundInfoDto = data.fundInfoDto;
				//开关
				var seven ='0';
				try{
					seven = queryParamComm("SYSTEM","SHOWNETVALUE","")[0].pmco;
				}catch(err){
					console.log("开关查询失败");
				}
				if(fundInfoDto != null){
					$("#offLineFund").val(fundInfoDto.offLineFund);
					var typeId = fundInfoDto.typeId;
					$("#typeId").val(typeId);
					$("#scale").val(fundInfoDto.scale);/*发行规模*/
					$("#moneyZero").val(fundInfoDto.money);/*认购起点*/
					$("#moneyStep").val(fundInfoDto.moneyStep);/*认购步长*/
					$("#sartBuying").val(fundInfoDto.sartBuying);/*追加购买金额 */
					$("#displayLimit").val(fundInfoDto.displayLimit);/*剩余额度*/
					
					var profit = fundInfoDto.profit;/* 产品业绩报酬计提基准率*/
					var termInDay = fundInfoDto.termInDay;/* 存续期限，以天为单位*/
					var term = fundInfoDto.term;/* 存续期限，存展现，不带单位*/
					var termUnit = fundInfoDto.termUnit;/* 存续期限单位*/
					var scale = parseFloat(fundInfoDto.scale);/* 产品规模*/
					var displayLimit = parseFloat(fundInfoDto.displayLimit);/* 页面展示剩余额度*/
					
					var money = parseFloat(fundInfoDto.money);/* 最低认购金额*/
					var moneyStep = parseFloat(fundInfoDto.moneyStep);/* 认购步长*/
					
					var currentWorkdate = fundInfoDto.currentWorkdate;/*当前工作日*/
					var subdeadLine = fundInfoDto.subdeadLine;/*认购截止*/
					var sellPercent = "";/* 产品额度(销售进度百分比)*/
					var isSubPrdAppraisement = fundInfoDto.isSubPrdAppraisement;
					var prjLName = fundInfoDto.prjLName;
					
					
					if(daysBetween(currentWorkdate,subdeadLine) > 0){
						sellPercent = 100;
					}else{
						sellPercent = numMulti(numDiv((scale-displayLimit),scale),100).toFixed(0);
					}
					var profitTemp = numMulti(parseFloat(profit),100).toFixed(2)+"";
					var profitText = 0;
					if(isNaN(profit) || profit == 0){
						profitText = "浮动收益</em>";
					}else{
						profitText = parseFloat(numDiv(numMulti(numMulti(money,profit),termInDay),365)).toFixed(2) + "</em>元";
					}
					
					$("#profit_input").val(profit);
					$("#termInDay").val(termInDay);
					
					if("n" == isSubPrdAppraisement.toLowerCase()){
						$('#text1').html("注:该净值为《"+prjLName+"》 份额净值");
						$('#text1').show();
						$('#highchart1').css('margin','0 auto 4px');
					}
					queryEstimate()
					/*-------------------- 产品详情  个性化展示  S --------------------*/
					if(typeId != null && typeId == "0100"){/* 固定收益*/
						$("#top_left_01").show();
						$("#top_left_02,#top_left_03,#top_left_04,#top_left_05").remove();
						
						/*$("#profit_text").html("业绩报酬计提基准：<em>"+profitText+"");*/
						$("#profit_text").html("业绩报酬计提基准：<em>---");
						
						if(isNaN(profitTemp) || profitTemp == 0){
							$("#profit").html("<i></i>浮动收益");
						}else{
							$("#profit").html("<i>"+profitTemp.split(".")[0]+".</i>"+profitTemp.split(".")[1]+"%");
						}
						$("#term").html("<em>"+term+"</em>"+termUnit);
						$("#scaleText").html("<em>"+numDiv(scale,10000)+"</em>万");
						
						//$("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:"+sellPercent+"%'></b></span><i>"+sellPercent+"%</i>");/* 产品额度*/
						if(displayLimit > 0){
							if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
								$("#displayLimitShow").html("<em></em>已售罄");/* 产品额度提示语*/
							}else{
								$("#displayLimitShow").html("只剩<em>"+numDiv(displayLimit,10000)+"万</em>产品额度");/* 产品额度提示语*/
							}
						}else{
							$("#displayLimitShow").html("<em></em>已售罄");/* 产品额度提示语*/
						}
						
					}else if(typeId!=null&& typeId=="0110" ){
						/* 固定收益(净值)类产品 */
						queryUserWarehouse(fundInfoDto.fundId);
						var buyState = $('#buyState').val();
						if(buyState == 'Y'){
							//已有当前产品持仓，起购金额取后台配置追加购买金额
							money = parseFloat(fundInfoDto.sartBuying);
							// $(".float-right").show().html("到期资金安排").addClass("redeemGoStatus");
							$(".float-right").show();
							$(".float-rightA").attr("href","/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO&fundid="+fundId+"&applyst=G");
						}
						$('#maturityDateText').html('下一到期日');
						if (fundInfoDto.state == "0") { /* 申购 	*/
							$("#top_left_04").show();
							$("#top_left_02,#top_left_03,#top_left_01,#top_left_05").remove();
							//$("#netval").show();
							/*$("#profit_text").html("业绩报酬计提基准：<em>"+profitText+"");*/
							$("#profit_text").html("业绩报酬计提基准：<em>---");
							$("#profit").siblings("dd").html("业绩报酬计提基准")
							if(isNaN(profitTemp) || profitTemp == 0){
								$("#profit").html("<i></i>浮动收益");
							}else{
								$("#profit").html("<i>"+profitTemp.split(".")[0]+".</i>"+profitTemp.split(".")[1]+"%");
							}
							$("#top_left_04 #terms").html("<em>"+term+"</em>"+termUnit);
							
							//$("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:"+sellPercent+"%'></b></span><i>"+sellPercent+"%</i>");/* 产品额度*/
							if(displayLimit > 0){
								if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
									$("#displayLimitShow").html("<em></em>已售罄");/* 产品额度提示语*/
								}else{
									$("#displayLimitShow").html("只剩<em>"+numDiv(displayLimit,10000)+"万</em>产品额度");/* 产品额度提示语*/
								}
							}else{
								$("#displayLimitShow").html("<em></em>已售罄");/* 产品额度提示语*/
							}
							
							if(fundInfoDto.latestNewValue){
								$("#top_left_04 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);/*最新净值*/
							}else{
								$("#top_left_04 #netval_new").html("<i>1.0000</i>");/*最新净值*/
							}
							
							
							$("#terms").html("<em>"+term+"</em>"+termUnit);
							if("n" == isSubPrdAppraisement.toLowerCase()){
								$('#text1').html("注:该净值为《"+prjLName+"》 份额净值");
								$('#text1').show();
								$('#highchart1').css('margin','0 auto 4px');
							}
							
							//$("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:"+sellPercent+"%'></b></span><i>"+sellPercent+"%</i>");/* 产品额度*/
							if(displayLimit > 0){
								if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
									$("#displayLimitShow").html("<em></em>已售罄");/* 产品额度提示语*/
								}else{
									$("#displayLimitShow").html("只剩<em>"+numDiv(displayLimit,10000)+"万</em>产品额度");/* 产品额度提示语*/
								}
							}else{
								$("#displayLimitShow").html("<em></em>已售罄");/* 产品额度提示语*/
							}
						}else{/* 认购  */
							$("#top_left_01").show();
							$("#top_left_02,#top_left_03,#top_left_04,#top_left_05").remove();
							
							/*$("#profit_text").html("业绩报酬计提基准：<em>"+profitText+"");*/
							$("#profit_text").html("业绩报酬计提基准：<em>---");
							
							if(isNaN(profitTemp) || profitTemp == 0){
								$("#profit").html("<i></i>浮动收益");
							}else{
								$("#profit").html("<i>"+profitTemp.split(".")[0]+".</i>"+profitTemp.split(".")[1]+"%");
							}
							$("#term").html("<em>"+term+"</em>"+termUnit);
							$("#scaleText").html("<em>"+numDiv(scale,10000)+"</em>万");
							
							//$("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:"+sellPercent+"%'></b></span><i>"+sellPercent+"%</i>");/* 产品额度*/
							if(displayLimit > 0){
								if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
									$("#displayLimitShow").html("<em></em>已售罄");/* 产品额度提示语*/
								}else{
									$("#displayLimitShow").html("只剩<em>"+numDiv(displayLimit,10000)+"万</em>产品额度");/* 产品额度提示语*/
								}
							}else{
								$("#displayLimitShow").html("<em></em>已售罄");/* 产品额度提示语*/
							}
						}
					}else if(typeId!=null&& typeId=="0400" ){
						  //七天管家类产品
						queryUserWarehouse(fundInfoDto.fundId);
						var sevenDayAnnualy = '--';
						var benefitSinceCreated = '';
						if(seven == '1'){
							/*
							 * 7天管家类产品
							 * 展示七日年化收益、理财起点、递增金额
							 */
							$(".start dd").text("七日年化收益");
	                        if(fundInfoDto.sevenDayAnnualy&&"0.00%"!=fundInfoDto.sevenDayAnnualy){
	                        	sevenDayAnnualy =fundInfoDto.sevenDayAnnualy;
	                        }
							if(fundInfoDto.state == '1') {
	                        	sevenDayAnnualy = '--';
	                        }
						}else{
							/*$(".start dd").text("最新净值");
							sevenDayAnnualy ='1.0000';
							if(fundInfoDto.latestNewValue){
								sevenDayAnnualy = fundInfoDto.latestNewValue;
							}*/
							$(".start dd").text("成立以来年化收益率");
							benefitSinceCreated = fundInfoDto.benefitSinceCreated ? fundInfoDto.benefitSinceCreated : '0.00';
						}
						$(".center dd").text("递增金额");
						$(".end dd").text("起购金额");
                        //$(".tips").show();
						
//						$("#redeemGo").show();
						$("#money_val").html("<em>"+numDiv(scale,10000)+"</em>万起");
						$("#displayLimitShow").hide();
						$("#timeAxis").hide();
						if(fundInfoDto.moneyStep == null || fundInfoDto.moneyStep == '') {
							$("#moneyStep_val").html("<em></em>元");
							$("#moneyStep_val em").html(fundInfoDto.moneyStep);/*认购步长*/
						} else if (fundInfoDto.moneyStep < 10000){
							$("#moneyStep_val").html("<em></em>元");
							$("#moneyStep_val em").html(fundInfoDto.moneyStep);/*认购步长*/
						} else {
							$("#moneyStep_val").html("<em></em>万");
							$("#moneyStep_val em").html(numDiv(fundInfoDto.moneyStep,10000));/*认购步长*/
						}
						//$('#sevenDayAnnualy_val').html("<em style='color:#ca132c;'>" + sevenDayAnnualy + "</em>" );/*七日年化收益*/
						if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
							$('#sevenDayAnnualy_val').html("<em style='color:#ca132c;'>--</em>" );/*成立以来年化收益率*/
						}else {
							
							//$('#sevenDayAnnualy_val').html("<em style='color:#ca132c;'>" + benefitSinceCreated + "%</em>" );/*成立以来年化收益率*/
							 $("#sevenDayAnnualy_val").addClass("floatincome")
							$('#sevenDayAnnualy_val').html("<i>"+benefitSinceCreated.split(".")[0]+".</i>"+benefitSinceCreated.split(".")[1]+"%");/*成立以来年化收益率*/
						}
						
						var buyState = $('#buyState').val();
						var total = $('#total').val();
						var balance = $('#balance').val();
						var custno = $('#custNo').val();
						if(buyState == 'Y'){//已有当前产品持仓，起购金额取后台配置追加购买金额
							money = parseFloat(fundInfoDto.sartBuying);
							$("#moneyZero").val(fundInfoDto.sartBuying);/*认购起点*/
						}else{
							$("#moneyZero").val(fundInfoDto.money);/*认购起点*/
						}
						$("#money_val em").html(numDiv(money,10000));/*认购起点*/
						
						$("#top_left_05").show();
						$("#top_left_01,#top_left_02,#top_left_03,#top_left_04").remove();
						$("#profit_text").html("官网直销免认购费");
                        if(!fundInfoDto.latestNewValue){
                        	fundInfoDto.latestNewValue ='1.0000';
                       }
					}else{/* 浮动收益/封闭净值开放净值类产品/*/
						if((typeId == "0210" || typeId == "0220") && fundInfoDto.state == "0"){/*申购*/
//							$("#top_left_03 #netval_profit").html("<em>"+numMulti((parseFloat(list[0].accNetValue)-1),100).toFixed(2)+"</em>%");
							$("#top_left_03").show();
							$("#top_left_01,#top_left_02,#top_left_04,#top_left_05").remove();

							$("#profit_text").html("官网直销免认购费");
							
                            if(fundInfoDto.latestNewValue){
                            	 $("#top_left_03 #netval_new").html("<i>" + fundInfoDto.latestNewValue.split(".")[0] + "</i>." + fundInfoDto.latestNewValue.split(".")[1]);/*最新净值*/
							     $("#top_left_03 #netval_profit").html("<em>" + numMulti((parseFloat(fundInfoDto.latestNewValue) - 1), 100).toFixed(2) + "</em>%");/*累计收益率*/
                            }else{
                                 $("#top_left_03 #netval_new").html("<i></i>");/*最新净值*/
							     $("#top_left_03 #netval_profit").html("<em></em>");/*累计收益率*/
                            }
						
							
							$("#top_left_03 #netval_text").html("<em><b>" + $("#adname").text() + "最新净值日期为<i>" + fundInfoDto.lastNavDate + "</i></b></em>");/*描述*/
							
							if(fundInfoDto.maxNav){
								$("#top_left_03 #maxNetValue").html(parseFloat(fundInfoDto.maxNav).toFixed(4));/*历史最高净值*/
							}else{
								$("#top_left_03 #maxNetValue").html("");/*历史最高净值*/
							}
							if("n" == isSubPrdAppraisement.toLowerCase()){
								$('#text1').html("注:该净值为《"+prjLName+"》 份额净值");
								$('#text1').show();
								$('#highchart1').css('margin','0 auto 4px');
							}
						}else if(typeId == "0500"){  //活期类产品
							queryUserWarehouse(fundInfoDto.fundId);
							$("#top_left_01").show();
							$("#top_left_02,#top_left_03,#top_left_04,#top_left_05").remove();
							$(".start dd").text("成立以来年化收益率");
							$(".center dd").text("递增金额");
							$(".end dd").text("起购金额");
                            $(".tips").show();
							var latestNewValue = '1.0000';

                            if(fundInfoDto.state != '0'){
								latestNewValue = '1.0000';
                            }
                            if(fundInfoDto.state == '0' && fundInfoDto.latestNewValue){
                                latestNewValue = fundInfoDto.latestNewValue;
                            }
                                                      
							var benefitSinceCreated = fundInfoDto.benefitSinceCreated ? fundInfoDto.benefitSinceCreated : '0.00';

							if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
								$('#profit').html("<em style='color:#ca132c;'>--</em>" ); /*成立以来年化收益率*/
							}else {
								
								//$('#profit').html("<em style='color:#ca132c;'>" + benefitSinceCreated + "</em>%" ); /*成立以来年化收益率*/
								$('#profit').html("<i>"+benefitSinceCreated.split(".")[0]+".</i>"+benefitSinceCreated.split(".")[1]+"%"); /*成立以来年化收益率*/
							}
                            
							$("#redeemGo").show();
							$("#scaleText").html("<em>"+numDiv(scale,10000)+"</em>万起");
							$("#displayLimitShow").hide();
							$("#timeAxis").hide();
							if(fundInfoDto.moneyStep == null || fundInfoDto.moneyStep == '') {
								$("#term").html("<em></em>元");
								$("#term em").html("1");/*认购步长*/
							} else if (fundInfoDto.moneyStep < 10000){
								$("#term").html("<em></em>元");
								$("#term em").html(fundInfoDto.moneyStep );/*认购步长*/
							} else {
								$("#term").html("<em></em>万");
								$("#term em").html(numDiv(fundInfoDto.moneyStep,10000));/*认购步长*/
							}
							var buyState = $('#buyState').val();
							var total = $('#total').val();
							var balance = $('#balance').val();
							var custno = $('#custNo').val();
							var tradeAcco = $('#tradeAcco').val();
							if(buyState == 'Y'){
								money = parseFloat(fundInfoDto.sartBuying);
								$("#moneyZero").val(fundInfoDto.sartBuying);/*认购起点*/
                                                                $("#redeemGo").addClass("redeemGoStatus")
								$("#redeemGo").attr("href","/AppService/business/fund/userRedeem.shtml?fundid="+ fundId +"&period="+ period);
							}else{
								$("#redeemGo").attr("href","javascript:void(0)");
							}
							$("#scaleText em").html(numDiv(money,10000));/*认购起点*/
						}else{
							$("#top_left_02").show();
							$("#top_left_01,#top_left_03,#top_left_04,#top_left_05").remove();
							
							if(isNaN(profitTemp) || profitTemp == 0){
								$("#profit").html("<i></i>浮动收益");
							}else{
								$("#profit").html("<i>"+profitTemp.split(".")[0]+".</i>"+profitTemp.split(".")[1]+"%");
							}
							$("#term").html("<em>"+term+"</em>"+termUnit);
							if(fundInfoDto.groupId == "20003" && typeId == "0210"){
								$("#term").parent().html("");
							}
							$("#scaleText").html("<em>"+numDiv(scale,10000)+"</em>万");
							
							$("#profit_text").html("官网直销免认购费");
							
							//$("#sellPercent").html("<em>投资进度</em><span><b class='act' style='width:"+sellPercent+"%'></b></span><i>"+sellPercent+"%</i>");/* 产品额度*/
							if(displayLimit > 0){
								if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
									$("#displayLimitShow").html("<em></em>已售罄");/* 产品额度提示语*/
								}else{
									$("#displayLimitShow").html("只剩<em>"+numDiv(displayLimit,10000)+"万</em>产品额度");/* 产品额度提示语*/
								}
							}else{
								$("#displayLimitShow").html("<em></em>已售罄");/* 产品额度提示语*/
							}
						}
					}
					/*-------------------- 产品详情  个性化展示  E --------------------*/
					
					/*-------------------- 产品详情  通用展示  S --------------------*/
					$("#adname").html(fundInfoDto.adname);/* 产品名称*/
					$("#typeName").html(fundInfoDto.typeName);/* 产品类型名称*/
					$("#adtext").html(fundInfoDto.adtext);/* 广告语*/
					
					var appointDate = fundInfoDto.appointDate;/*预约开始日期*/
					var appointEndDate = fundInfoDto.appointEndDate;/*预约结束日期*/
					var salesDate = fundInfoDto.salesDate;/*发售日*/
					var subdeadLine = fundInfoDto.subdeadLine;/*认购截止*/
					var interestDate = fundInfoDto.interestDate;/*起息日期*/
					var maturityDate = fundInfoDto.maturityDate;/*到期日期*/
					var paymentDate = fundInfoDto.paymentDate;/*产品到期清盘，预计打款日期*/
					var currentWorkdate = fundInfoDto.currentWorkdate;/*当前工作日*/
					var appPayMoneyText = formatDate1(salesDate) +" - " + formatDate1(subdeadLine) + "15:00前";//预约期打款时间
					$("#appoint_date_tips").html(appPayMoneyText);
					var headaccount = fundInfoDto.headaccount;
					var maxNumber = fundInfoDto.maxNumber;/*产品有限人数*/
					var buyerNumber = fundInfoDto.buyerNumber;/*产品已购买人数*/
					
					var buyType = "1";
					$("#appointDate").html(formatDate(appointDate));/*预约开始日期*/
					$("#salesDate").html(formatDate(salesDate));/*发售日*/
					$("#subdeadLine").html(formatDate(subdeadLine));/*认购截止*/
					$("#maturityDate").html(formatDate(maturityDate));/*到期日期*/
					$("#buyMoney").val(formatNumber(money));/* 认购起点--进入页面默认输入的值*/
					var tempMoneyStep = moneyStep < 10000 ? (moneyStep+"元") : (numDiv(moneyStep,10000)+"万")
					$("#moneyAndStep").html(numDiv(money,10000)+"万起售，"+tempMoneyStep+"递增");
					$("input[name=appointDate]:eq(0)").val(appointDate);
					$("input[name=appointEndDate]:eq(0)").val(appointEndDate);
					$("input[name=salesDate]:eq(0)").val(salesDate);
					$("input[name=subdeadLine]:eq(0)").val(subdeadLine);
					$("input[name=currentWorkdate]:eq(0)").val(currentWorkdate);

					// 7天十四天页面展示
					if(typeId=="0400"){
                       $("#timeAxis").hide();
						$(".fund7Day").show();
						if(fundInfoDto.state == '0'){
							$("#buyTime").html(fundInfoDto.currentWorkdate);/* 购买时间 */
						}else if(fundInfoDto.state == '1' && daysBetween(fundInfoDto.interestDate,fundInfoDto.currentWorkdate) > 0){
							$("#buyTime").html(fundInfoDto.interestDate);/* 基金成立日期 */
						}else{
							$("#buyTime").html(fundInfoDto.currentWorkdate);/* 购买时间 */
						}
						$("#expireTime").html(fundInfoDto.maturityDate);/* 首个可赎回日 */
						$("#netExpireTime").html(fundInfoDto.nextMaturityDate);/* 次个可赎回日 */
                                                if(fundInfoDto.term=="14"){
							$(".fund7Day-text em").html("存续十四天")
						}
					}

                    if(typeId=="0500"){   /**币类产品*/
                    	if(daysBetween(currentWorkdate,salesDate) >= 0){
                    		
                    		$(".box-head-right.fl h1").html("认购中…");
                    		$("#buyBtn").html("立即购买");
							$("#buyTips").html("下单成功后，请尽快完成支付，锁定产品份额！");
							if(headaccount==1){
                    			//判断是否新客户
                        		if(queryUserHasProOreder(fundInfoDto.fundId) == 0){
                        			if(maxNumber <= buyerNumber){
                            			$("#buyBtn").html("人数已满").addClass("act").attr("href","javascript:void(0)");
        							}
                        		}
                    		}
                    	}else{
                		    $(".box-head-right.fl h1").html("预约中…"); 
							$("#buyBtn").html("立即预约");
							$("#buyTips").html("预约成功后，请您于募集期（<span style='color:#ca132c;'>"+appPayMoneyText+"</span>）进行打款操作。");
							buyType ="1";
                    	}
                    	if(fundInfoDto.openPloy == "1"){
                    		$(".tipsMsg").css("margin","7px 0 5px");
                    		$("#profit_text").html("工作日" + formatWeek(fundInfoDto.openCycle) + (fundInfoDto.state == "0" ? "15:00" : "17:00") + "前可购买");
                    		if(fundInfoDto.openCycleFlag == "false"){
                    			$("#buyBtn").addClass("act").attr("href","javascript:void(0)");
                        		$("#buyTips").remove();
                    		}
                    	}else{
                    		$(".tipsMsg").remove();
                    	}
                    	$(".input_money").css("margin-bottom","10px");
                    }else{
						//到期日 = 募集结束
						if(daysBetween(currentWorkdate,maturityDate) >= 0){
							$(".box-head-right.fl h1").html("募集结束…"); 
							$("#buyBtn").html("募集结束").addClass("act").attr("href","javascript:void(0)");
							timeAxisProcess(4);
						//过了截止日 = 募集结束
						}else if(daysBetween(currentWorkdate,subdeadLine) > 0 && daysBetween(currentWorkdate,maturityDate) < 0){/*认购截止日期*/
							$(".box-head-right.fl h1").html("募集结束…"); 
							$("#buyBtn").html("募集结束").addClass("act").attr("href","javascript:void(0)");
							timeAxisProcess(3);
						//认购期= 当前时间 大于等于 认购起始日 并且 小于 认购截止日 
						}else if(daysBetween(currentWorkdate,salesDate) >= 0 && daysBetween(currentWorkdate,subdeadLine) <= 0){
							timeAxisProcess(2);
							//额度是否足够
							if(displayLimit > 0){
								$(".box-head-right.fl h1").html("认购中…");
								$("#buyBtn").html("立即购买");
								$("#buyTips").html("下单成功后，请尽快完成支付，锁定产品份额！");
								buyType ="2";
								if(headaccount==1){
	                    			//判断是否新客户
	                        		if(queryUserHasProOreder(fundInfoDto.fundId) == 0){
	                        			if(maxNumber <= buyerNumber){
	                            			$("#buyBtn").html("人数已满").addClass("act").attr("href","javascript:void(0)");
	        							}
	                        		}
	                    		}
							}else{
								$(".box-head-right.fl h1").addClass("lineup").html("排队中…");
								$("#buyBtn").html("参与排队");
								$("#buyTips").html("已有"+fundInfoDto.lineUpNums+"名客户在排队中，赶紧加入吧！");
								buyType ="4";
							}
						//预约期= (预约开始日 <= 当前时间  <= 预约截止日 )
						}else if(daysBetween(currentWorkdate,appointDate) >= 0 && daysBetween(currentWorkdate,appointEndDate) <= 0){
							timeAxisProcess(1);
							//额度是否足够
							if(displayLimit > 0){
								$(".box-head-right.fl h1").html("预约中…"); 
								$("#buyBtn").html("立即预约");
								$("#buyTips").html("预约成功后，请您于募集期（<span style='color:#ca132c;'>"+appPayMoneyText+"</span>）进行打款操作。");
								buyType ="1";
							}else{
								$(".box-head-right.fl h1").addClass("lineup").html("排队中…");
								$("#buyBtn").html("参与排队");
								$("#buyTips").html("已有"+fundInfoDto.lineUpNums+"名客户在排队中，赶紧加入吧！");
								$("#displayLimitShow").html("预约额已满");
								buyType ="4";
							}
						}else{
							timeAxisProcess(1);
							$(".box-head-right.fl h1").html("预约中…"); 
							$("#buyBtn").html("立即预约");
							$("#buyTips").html("预约成功后，请您于募集期（<span style='color:#ca132c;'>"+appPayMoneyText+"</span>）进行打款操作。");
							buyType ="1";
						}
						$("#buyType").val(buyType);
					}
					
					
				    var elementList = fundInfoDto.elementList;
					if(elementList != null && elementList.length > 0){
						var htmls1 = "";
						var htmls2 = "";
						var htmls3 = "";
						/* var htmls4 = ""; */
						var bl = false;
						$.each(elementList, function(i, item) {
							if(item.type > 100 && item.type < 200 ){/* 产品基本信息： 110-小标题； 120-大标题； 130-图片*/
								
								if(item.type == 110){
									bl = true;
									htmls1 += "<li class='align'><span class='litter_title'>"+item.title+"</span><em>"+item.content+"</em></li>";
								}else if(item.type == 120){
									htmls2 += "<div class='box'>";
									htmls2 += "<span class='left'>"+item.title+"</span>";
									htmls2 += "<span class='right wordlimit'><p style='word-break: break-all'>"+item.content+"</p></span>";
									htmls2 += "</div>";
								}
							}
							if(item.type > 200 && item.type < 300 ){/* 投资项目信息： 210-小标题； 220-大标题； 230-图片*/
								
								if(item.type == 220){
									htmls3 += "<div class='box'>";
									htmls3 += "<span class='left'>"+item.title+"</span>";
									htmls3 += "<span class='right wordlimit'><p style='word-break: break-all'>"+item.content+"</p></span>";
									htmls3 += "</div>";
								}else if(item.type == 230){
									htmls3 += "<div class='box'>";
						            htmls3 += "<span class='left'>"+item.title+"</span>";
						            htmls3 += "<span class='right'><img src='"+item.picUrl+"' height='479' width='763' alt=''></span>";
						        	htmls3 += "</div>";
								}
							}
							if((i+1) == elementList.length){
								if(!bl){
									$("#productInfo div.content .box01").hide();
								}else{
									$("#productInfo div.content .box01 ul").html(htmls1);
								}
								$("#productInfo div.content").append(htmls2);
								if(htmls1 != "" || htmls2 != ""){
									$("#productInfo").show();
								}
								if(htmls3!=""){
									$("#projectInfo div.content").html(htmls3).parent().show();
								}
							}
						});
					}
					
					var quesList = data.quesList;
					if(quesList != null && quesList.length > 0){
						var htmls4 = "";
						$.each(quesList, function(i, item) {
						/* Q&A： 310-小标题； 320-大标题； 330-图片 */ 
							htmls4 += "<div class='box01'>";
				            htmls4 += "<span class='left'>"+item.wWenti+"</span>";
				            htmls4 += "<span class='right act wordlimit'><p style='word-break: break-all' data-title='"+item.wDaan+"'>"+item.wDaan+"</p></span>";
				            htmls4 += "<i></i>";
				        	htmls4 += "</div>";
						});
			        	$("#qa div.content").html(htmls4).parent().show();

			        	$(".wordlimit").each(function(i) {
			        		var divH = $(this).height();
			        		var $p = $("p", $(this)).eq(0);
			        		while ($p.outerHeight() > divH) {
			        			$p.text($p.text().replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
			        		}
			        	});
			        	$(".box-third .content .box01").toggle(
			        		function() {
			        			$(this).addClass("act").siblings().removeClass("act");
			        			var ellipsisText = $(this).find(".right p").attr("data-title");
			        			$(this).find(".right p").html(ellipsisText);
			        		}, 
			        		function() {
			        			if ($(this).hasClass("act")) {
			        				$(this).removeClass("act").siblings().removeClass("act");
			        				$(".wordlimit").each(function(i) {
			        					var divH = $(this).height();
			        					var $p = $("p", $(this)).eq(0);
			        					while ($p.outerHeight() > divH) {
			        						$p.text($p.text().replace(/(\s)*([a-zA-Z0-9]+|\W)(\.\.\.)?$/, "..."));
			        					}
			        				});
			        			} else {
			        				$(this).addClass("act").siblings().removeClass("act");
			        	            var ellipsisText=$(this).find(".right p").attr("data-title");
			        	            $(this).find(".right p").html(ellipsisText);
			        			}
			        		}
			        	);
					}
					/*-------------------- 产品详情  通用展示  E --------------------*/
				}else{
					show_tips("没有找到该产品！")
				}
				adjustCss();
			}else if(data.resultCode == "9999"){
				show_tips("网络繁忙，请稍后再试！");
			}else{
				show_tips(data.resultMsg);
			}
		}
	});
}

/* 查询产品净值(曲线图)*/
function queryEstimateByFundId(dateTime){
	var fundId = getUrlParameter("fundid");
	fundId = removeSpecialStr(fundId);
	$("#highchart .highchart_nav ul li").removeClass("act");
	if(parseInt(dateTime,10) == -1){
		$("#highchart .highchart_nav ul li:eq(0)").addClass("act");
	}else if(parseInt(dateTime,10) == -3){
		$("#highchart .highchart_nav ul li:eq(1)").addClass("act");
	}else if(parseInt(dateTime,10) == -6){
		$("#highchart .highchart_nav ul li:eq(2)").addClass("act");
	}else if(parseInt(dateTime,10) == -12){
		$("#highchart .highchart_nav ul li:eq(3)").addClass("act");
	}
	$.ajax({
		async:true,
		url : "/AppService/business/queryEstimateByFundId.xhtml",
		data : {
			fundId:fundId,
			dateTime:dateTime,
		},
		dataType : "json",
        type:"POST",
		cache : false,
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			var xVals = "";
			var yVals = "";
			var netVals = "";
			var fluctuates = "";
			var htmls_1 = "";
			var productEstimateList = "";
			var stepNum = 1;
			if(data != null){
				productEstimateList = data.productEstimateList;
			}
			if (productEstimateList != null && productEstimateList != "" && productEstimateList.length > 0) {
				stepNum = Math.ceil(numDiv(parseInt(productEstimateList.length,10),20));
				
				var maxVal = productEstimateList[0].maxNetValue;
				var yVarMaxlti = 0;
				var yValMinlti=0;
				
				var newNetVal = 0;
				var oldNetVal = 0;
				
//				var yVals = 0.8 + ";" +(numDiv(yValMilti,4)*1).toFixed(4) + ";" + (numDiv(yValMilti,4)*2).toFixed(4) + ";" + (numDiv(yValMilti,4)*3).toFixed(4) + ";" + (numDiv(yValMilti,4)*4).toFixed(4);
				
				
				
				$.each(productEstimateList,function(i, item){
					xVals += item.eDate + ";";
					netVals += item.netValue + ";";
					fluctuates += item.fluctuate + ";";
					if (yValMinlti>item.netValue) {
						yValMinlti = item.netValue;
					}
					if (yVarMaxlti < item.netValue) {
						yVarMaxlti = item.netValue;
					}
					if(i == 0){
						newNetVal = item.netValue;
						yValMinlti = item.netValue;
						yVarMaxlti = item.netValue;
					}else if(i == productEstimateList.length-1){
						oldNetVal = item.netValue;
					}
				})
				yVarMaxlti=numMulti(yVarMaxlti,1.01)
				var min = (numDiv(yValMinlti,1)*1*0.99).toFixed(4);
				var max=yVarMaxlti;
				
				var dispartity=max-min;
				var first=numAdd(min,(numDiv(dispartity,4)*1).toFixed(4));
				var second=numAdd(min,(numDiv(dispartity,4)*2).toFixed(4));
				var third=numAdd(min,(numDiv(dispartity,4)*3).toFixed(4));
				var yVals = min+ ";" + first  +";"+second +";"+third + ";" + max.toFixed(4);
				
				if(xVals.substr(xVals.length-1) == ";"){
					xVals = xVals.substr(0,xVals.length-1);
				}
				if(netVals.substr(netVals.length-1) == ";"){
					netVals = netVals.substr(0,netVals.length-1);
				}
				if(fluctuates.substr(fluctuates.length-1) == ";"){
					fluctuates = fluctuates.substr(0,fluctuates.length-1);
				}
				var temp = "";
				if(oldNetVal == null || oldNetVal == "" || oldNetVal==0){
					temp = "<em>--</em>";
				}else{
					temp = numMulti(numDiv((newNetVal-oldNetVal),oldNetVal),100);
					if (temp < 0) {
						temp = "<em class='green'>" + numMulti(numDiv((newNetVal - oldNetVal), oldNetVal), 100).toFixed(2) + "%</em>";
					} else if(temp == 0){
						temp = "<em style='color:#666666'>" + numMulti(numDiv((newNetVal - oldNetVal), oldNetVal), 100).toFixed(2) + "%</em>";
					} else {
						temp = "<em>+" + numMulti(numDiv((newNetVal - oldNetVal), oldNetVal), 100).toFixed(2) + "%</em>";
					}
				}
				if(dateTime != null && dateTime == -1){
					htmls_1 += "最近1个月涨跌幅"+temp;
				}else if(dateTime != null && dateTime == -3){
					htmls_1 += "最近3个月涨跌幅"+temp;
				}else if(dateTime != null && dateTime == -6){
					htmls_1 += "最近6个月涨跌幅"+temp;
				}else if(dateTime != null && dateTime == -12){
					htmls_1 += "最近1年涨跌幅"+temp;
				}
				$("#profitByDate").html(htmls_1);

				showHighchart1(xVals,yVals,netVals,fluctuates,stepNum);
			}else{
				if(dateTime != null && dateTime == -1){
					htmls_1 += "最近1个月涨跌幅<em>--</em>";
				}else if(dateTime != null && dateTime == -3){
					htmls_1 += "最近3个月涨跌幅<em>--</em>";
				}else if(dateTime != null && dateTime == -6){
					htmls_1 += "最近6个月涨跌幅<em>--</em>";
				}else if(dateTime != null && dateTime == -12){
					htmls_1 += "最近1年涨跌幅<em>--</em>";
				}
				$("#profitByDate").html(htmls_1);
				showHighchart1("0","0","0","0",stepNum);
			}
		}
	})
}
/* 查询产品净值*/
function queryEstimateByFundIdByPage(pages){
	var fundId = getUrlParameter("fundid");
	fundId = removeSpecialStr(fundId);
	var pageInput = $("#page").val();
	var page = returnPage(pages);
	
	$.ajax({
		async:true,
		url : "/AppService/business/queryEstimateByFundIdByPage.xhtml",
		data : {
			fundId:fundId,
			page:page
		},
		dataType : "json",
        type:"POST",
		cache : false,
		error : function(textStatus,errorThrown){
			show_tips("网络繁忙，请稍后再试。");  
		},
		success : function(data){
			var htmls = "";
			var maxPages = data.maxPages;
			var page = data.page;
			$("#maxPages").val(maxPages);
			$("#page").val(page);
			
			var list = data.productEstimates;
			
			if(list != null && list.length > 0){
				htmls += "<div id='tableContent'>";
				htmls += "<div class='highchart01-data'>";
				htmls += "<table width='880' border='1'>";
				htmls += "<tr>";
				htmls += "<th height='30' align='center' bgcolor='#fafafa' scope='col'>日期</th>";
				htmls += "<th align='center' bgcolor='#fafafa' scope='col'>单位净值</th>";
				htmls += "<th align='center' bgcolor='#fafafa' scope='col'>累计净值</th>";
				htmls += "<th align='center' bgcolor='#fafafa' scope='col'>区间涨幅</th>";
				htmls += "</tr>";
				
				$.each(list,function(i, item){
					htmls += "<tr>";
					htmls += "<td height='30' align='center'>"+formatDate(item.eDate)+"</td>";
					htmls += "<td align='center'>"+item.netValue+"</td>";
					htmls += "<td align='center'>"+item.accNetValue+"</td>";
					if(parseFloat(item.fluctuate) > 0){
						htmls += "<td align='center' style='color: #ca132c;'>+" + parseFloat(numMulti(item.fluctuate, 100)).toFixed(2) + "%</td>";
					}else if(parseFloat(item.fluctuate) < 0){
						htmls += "<td align='center' style='color: #57BA4D;'>" + parseFloat(numMulti(item.fluctuate, 100)).toFixed(2) + "%</td>";
					}else{
						htmls += "<td align='center'>" + parseFloat(numMulti(item.fluctuate, 100)).toFixed(2) + "%</td>";
					}
					htmls += "</tr>";
				})

				htmls += "</table>";
				htmls += "</div>";
				
				htmls += "<div class='nav-href estimate'>";
				htmls += "<a href='javascript:queryEstimateByFundIdByPage(\""+(page-1)+"\")' class='pre'></a>";
				if(page == 1){
					htmls += "<a class='act' href='javascript:queryEstimateByFundIdByPage(\"1\")'>1</a>";
				}else{
					htmls += "<a href='javascript:queryEstimateByFundIdByPage(\"1\")'>1</a>";
				}
				if(page > 3){/* 左边加...*/
					htmls += "<a class='more'>...</a>";
				}
				
				if((page-1) >1){/* 上一页*/
					htmls += "<a href='javascript:queryEstimateByFundIdByPage(\""+(page-1)+"\")'>"+(page-1)+"</a>";
				}
				
				if(page != 1 && page != maxPages){/* 当前页*/
					htmls += "<a class='act' href='javascript:queryEstimateByFundIdByPage(\""+(page)+"\")'>"+(page)+"</a>";
				}
				
				if(page+1 < maxPages){/*下一页*/
					htmls += "<a href='javascript:queryEstimateByFundIdByPage(\""+(page+1)+"\")'>"+(page+1)+"</a>";
				}
				
				if((maxPages - page) > 2){/* 右边加...*/
					htmls += "<a class='more'>...</a>";
				}
				if(maxPages == page){
					htmls += "<a class='act' href='javascript:queryEstimateByFundIdByPage(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}else if(maxPages > page){
					htmls += "<a href='javascript:queryEstimateByFundIdByPage(\""+(maxPages)+"\")'>"+maxPages+"</a>";
				}
				htmls += "<a href='javascript:queryEstimateByFundIdByPage(\""+(page+1)+"\")' class='next'></a>";
				htmls += "</div>";
				htmls += "</div>";
				
				$("#tableContent").remove();
				$("#netvalContent").append(htmls);
				$("#netval").show();
			}
		}
	})
}
/*查询费率和折扣*/
function queryFeeRateList(){
	var fundId = getUrlParameter("fundid");
	fundId = removeSpecialStr(fundId);
	var channelNoList = "";
	var custLevel = "";
	var money = unformat($("#buyMoney").val());
    $.ajax({
    	async:false,
		url:"/AppService/business/queryFeeRateList.xhtml",
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
				var typeId = $("#typeId").val();
				var rate = data.rate;
				if(rate == null || rate == "" || rate == "0"){/* 认购费：<em>1000</em>元*/
					$("#viewFee").remove();
					$("#profit_text").addClass("center");
				}else{
					if(typeId == "0100"){
						$("#viewFee").html("认购费：<em>"+data.rate+"</em>元");
					}else{
						$("#viewFee").html("认购费：<em>"+data.rate+"</em>元").addClass("center");
						$("#profit_text").remove();
					}
				}
			}else{
				$("#viewFee").remove();
				$("#profit_text").addClass("center");
			}
		}
	});
}
function returnPage(pages){
	var page;
	var maxPages = $("#maxPages").val();
	
	if(parseInt(pages,10) >= parseInt(maxPages,10)){
		page = maxPages;	
	}else if(parseInt(pages,10) <= 0){
		page = "1";
	}else{
		page = pages; 
	}
	return page;
}
function adjustCss(){
	for(var n=1; n < $(".box-first .content .box01 ul li").length+1; n++){
		if(n%3 == 0){
			$(".box-first .content .box01 ul li:eq("+(n-1)+")").find("span").css({
				textAlign:"left",
				marginLeft:"50px",
				width:"110px",
			});
		}
	}
}
/* 最近1个月*/
function showHighchart1(xVals,yVals,netVals,fluctuates,stepNum){
	var xArray = new Array();
	for(var i = xVals.split(";").length-1;i >= 0;i--){
		xArray.push((xVals.split(";")[i]).substr(4,2)+"-"+(xVals.split(";")[i]).substr(6,2));
	}
	var yArray = new Array();
	$.each(yVals.split(";"),function(i, item){
		yArray.push(yVals.split(";")[i]);
	})	
	var netArray = new Array();
	for(var i = netVals.split(";").length-1;i >= 0;i--){
		netArray.push(parseFloat(netVals.split(";")[i]));
	}
	var fluctuateArray = new Array();
	for(var i = fluctuates.split(";").length-1;i >= 0;i--){
		fluctuateArray.push(numMulti(parseFloat(fluctuates.split(";")[i]),100).toFixed(2));
	}
	var str = ""; 
	
	/* 将查询出来的要展示在tooltip位置的值转化为json格式的字符串*/
	str += "[";
	for(var i = 0;i < netArray.length && i < fluctuateArray.length; i++){
		str += '{"y":'+netArray[i]+',"fluctuate":'+fluctuateArray[i]+'},';
	}
	str += "]";
	str = str.substr(0,str.lastIndexOf(","))+str.substr(str.lastIndexOf(",")+1);
	
	/* 将json格式字符串转化为json数组*/
	var json = eval('('+str+')');
	
    $('#highchart1').highcharts({
        chart: {
            type: 'line',
			width:"923",
			height:"318"
        },
        title: {
            text: false,
            x: false,
        },
        subtitle: {
            text: false,
            x: -20
        },
        xAxis: {
            categories: xArray,
            tickmarkPlacement: 'on',
            tickLength: 5,
            gridLineWidth:1,
            gridLineDashStyle:"Dot",/* 竖网格线样式*/
            gridLineColor:"#ccc", /* 竖网格线颜色*/
            gridLineWidth:0,
            labels: {
                step: stepNum,/* 间隔步长*/
                staggerLines:1,/* 显示x轴的行数*/
				overflow:'justify',
            },       
        },
        yAxis: {
            title: {
                text: false,
            },
            tickPositions:yArray,
            gridLineColor:"#ebebeb",
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        credits:{
            enabled:false,
        },
		tooltip: {
            shared: true,
            borderWidth:0,
            followTouchMove:true,
            formatter:function(){
            	var a = '<b>' + this.x + '<b><br/>当期净值：' + this.y + '<br/>涨跌幅度：' + this.points[0].point.fluctuate +"%";
            	return a;
            }
        },
        legend: {
            enabled:false,
        },
        series: [{
            color: '#FF8A00',
            name: false,
            data: json
        }]
    });
}
/*4个时间节点点击显示事件*/
function show_fundInfo(_id){
	$("#fundInfo_tips").show();
	if ($("#typeId").val()=="0110" && _id == 'maturityDate_info') {
		$("#maturityDate_T").siblings().hide();
		$("#maturityDate_T").show();
	}else{
		$("#" + _id).siblings().hide();
		$("#" + _id).show();
	}
}
/*4个时间节点点击隐藏事件*/
function close_fundInfo(_id){
	$("#"+_id).hide();
	$("#"+_id).children().hide();
}
/*进入支付页面*/
function intoPay(){
	var fundid = getUrlParameter("fundid");
	var period = getUrlParameter("period");
	var offLineFund = $("#offLineFund").val();
	//先校验 产品时间
	var appointDate = $("input[name=appointDate]:eq(0)").val();
	var currentWorkdate = $("input[name=currentWorkdate]:eq(0)").val();
	if(daysBetween(currentWorkdate,appointDate) < 0){
		var tipText = formatDate1(appointDate) + "开启正式预约通道，敬请关注！";
		show_tips(tipText,"我知道了");
		return ;
	}
	fundid = removeSpecialStr(fundid);
	if(fundid != null && (offLineFund != null && offLineFund == "Y")){
		/*量化产品购买提示*/ 
		$("#quantitative").show();
		return;
	}
	var money = unformat($("#buyMoney").val());
	if (!checkedMoney(money)) {
		return;
	}
	gotoUrl("/AppService/business/fund/paySub.shtml?fundid="+fundid+"&money="+money+"&period="+period);
}
function checkedMoney() {
	var scale =  parseFloat(unformat($("#scale").val()));
	var moneyZero = parseFloat(unformat($("#moneyZero").val()));/* 认购起点 */
	var money = parseFloat(unformat($.trim($("#buyMoney").val())));/* 认购金额 */
	var moneyStep = parseFloat(unformat($("#moneyStep").val()));/* 认购步长 */
	var displayLimit = parseFloat(unformat($("#displayLimit").val()));/* 剩余额度 */
	var sartBuying = parseFloat(unformat($("#sartBuying").val()));/* 追加购买额度 */
	var buyState = $("#buyState").val();
	if(buyState == 'Y'){
		moneyZero = sartBuying;
	}
	var buyType = $("#buyType").val();
	
	var profit = parseFloat(unformat($("#profit_input").val()));
	var termInDay = $("#termInDay").val();

	if (money >= 1000000000) {
		show_tips("输入金额过大，请重新输入");
		return false;
	} else if (money < moneyZero || (money - moneyZero) % moneyStep != 0) {
		if(moneyStep < 10000) {
	      show_tips("本产品" + formatNumber(numDiv(moneyZero, 10000), ',') + "万起售，" + moneyStep + "元递增");	
		} else {
		  show_tips("本产品" + formatNumber(numDiv(moneyZero, 10000), ',') + "万起售，" + formatNumber(numDiv(moneyStep, 10000), ',') + "万递增");
		}		
		return false;
	} else if(money > displayLimit && buyType != "4"){
		show_tips("仅剩下" + numDiv(displayLimit, 10000) + "万元份额");
		return false;
	}else if(buyType == "4" && money > scale){
		show_tips("预约金额不能高于产品发售规模");
		return false;
	}else {
		var profit_text = $("#profit_text").html();
		if(profit_text != null && typeof(profit_text) != undefined && profit_text.indexOf("业绩报酬计提基准") >= 0){
			/*$("#profit_text").html("业绩报酬计提基准：<em>"+parseFloat(numDiv(numMulti(numMulti(money,profit),termInDay),365)).toFixed(2) + "</em>元");*/
			$("#profit_text").html("业绩报酬计提基准：<em>---</em>");
		}
		queryFeeRateList();
		return true;
	}
}
function toContract(fundid,money){
	var period = getUrlParameter("period")
	var m = unformat($("#money").val());
	var link = "/AppService/business/fund/contract.shtml?period="+period+"&fundId="+fundid+"&money="+m;
	window.open(link);
}

function timeAxisProcess(int){
	$("#timeAxis .process-right.fl dl").removeClass("act");
	$("#timeAxis .process-right.fl dl:lt("+int+")").addClass("act");
};

function queryEstimate(){
	queryEstimateByFundId("-1");
	queryEstimateByFundIdByPage("1");
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
		    		var fundinfo = res.data;
		    		$("#buyState").val('Y');
		    		$("#total").val(fundinfo.total);
		    		$("#balance").val(fundinfo.balance);
		    		$("#custNo").val(fundinfo.custno);
		    		$("#tradeAcco").val(fundinfo.tradeAcco);
		    	}
		     }
		},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
	   }
	})
}

/**
 * 查询用户是否持有该产品订单
 */
function queryUserHasProOreder(fundId){
	 var result; 
	 $.ajax({
    	async:false,
		url:"/AppService/business/queryUserHasProOreder.xhtml",
		type:"post",
		dataType:'json',
		data:{
			"fundCode":fundId
		},
		success:function(res){
		     if(res.returnCode=="0000"){
		    	 result = res.number;
		     }
		},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
	   }
	})
	return result;
}
function getFrmDate(date){
	return date;
}

function formatWeek(weeks){
	var week = ["周日","周一","周二","周三","周四","周五","周六"];
	var weekStr = "";
	if(weeks != null && weeks != ""){
		weeks = weeks.split(",");
		for (var i = 0; i < weeks.length; i++) {
			weekStr += ("、" + week[weeks[i]]);
		}
	}
	return weekStr.length > 0 ? weekStr.subString(1) : "";
}
