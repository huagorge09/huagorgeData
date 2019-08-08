﻿var fundId = "";
var period="";
var myScroll;
var hasBalance;
var balance;
var showNetValue;
var host = window.location.protocol + "//" + window.location.host;
var title="";  //标题
var desc="";  //描述
var link="";
var pageSource = '',eventId = '';
var userId='';
var custName="" //姓名
var shareAdname=""//产品名
var imgUrl=host+"/WeixinWeb/WeixinWeb_Images/H5modules/invitation/logo2.png"  //转发图标
$(document).ready(function(e) {
	eventId=getUrlSearchParams("eventId");
    pageSource=getUrlSearchParams("pageSource");
    userId=getUrlSearchParams("userid");
 	if(eventId && pageSource){ 
 		operatingRecord(pageSource,eventId,userId,"","","","","");
	}
    eventBind() //事件绑定
	$(".header .top-a h2").html("产品详情");
	document.title = "产品详情";
	myScroll = new IScroll('#wrapper', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true
	});

	fundId = getUrlParameter('fundId');
	period = getUrlParameter('period');
	fundId = fundId.replace('#', "");
	period = period.replace('#', "");
	try{
		showNetValue = queryParamList("SYSTEM","SHOWNETVALUE","")[0].pmco;
	}catch(error){
		showNetValue = "0";
	}
	queryFundInfo(fundId,period);
	myScroll.refresh();
	getUserRequest("fund-info");/* 此处subPath为页面内行为 */
	// MGM用户实名查询
	queryIntegralDeatail();
});

document.addEventListener('touchmove', function(e) {
	e.preventDefault();
}, false);

function goToFAQ() {
	goToURL("/WeixinService/business/query/FAQ.shtml?fundId="+$("#fundId").val()+"&period="+$("#period").val());
}
function closeCover(){
	$("#lianghua").hide();
}
function goToBuy() {
	var fundid = $("#fundId").val();
	var period = $("#period").val();
    var offLineFund = $("#offLineFund").val();
    
    //先校验 产品时间
	var appointDate = $("input[name=appointDate]:eq(0)").val();
	var currentWorkdate = $("input[name=currentWorkdate]:eq(0)").val();
	if(daysBetween(currentWorkdate,appointDate) < 0){
		var tipText = formatDate1(appointDate) + "开启正式预约通道，敬请关注！";
		errorRemark(tipText);
		return ;
	}
	
    if(fundid != null && (offLineFund != null && offLineFund == "Y")){
        /*量化产品购买提示*/ 
        $("#lianghua").show();
        return;
    }
	goToURL("/WeixinService/business/pay/buyMoney.shtml?fundId=" + fundid+"&period="+period);
}

$(".product-date span").click(function() {
	$(".product-date").hide();
});

function dateAlert(title, content) {
	var typeid=$('#typeId').val();
	if(title=="到期日期"&&$("#cfbDate").html()=="下一开放日"){
		$("#dateTitle").html("下一开放日");
		$("#dateContent").html("当日产品到期，可选择续期或者赎回。不操作情况下，会默认续投下一期。");
		$(".product-date").show();
	}else if( typeid== '0110' && title=='到期日期'){
		$("#dateTitle").html('到期日期');
		$("#dateContent").html('当日产品到期，系统自动续投或赎回本息至银行卡。');
		$(".product-date").show();
	}else{
		$("#dateTitle").html(title);
		$("#dateContent").html(content);
		$(".product-date").show();
	}
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
					shareAdname=data.fundInfo.adname  //赋值产品名
					$("#offLineFund").val(fundInfo.offLineFund);
					$("#fundId").val(fundInfo.fundId);
					$("#period").val(fundInfo.period);

					$("#typeName").text(fundInfo.typeName);
					$("#adName").text(fundInfo.adname);
					$("#typeId").val(fundInfo.typeId);
					var typeId = fundInfo.typeId;
                    var quesList=data.quesList;
					var scale = fundInfo.scale;/* 规模 */
                                        if(!quesList||quesList.length==0||quesList[0]==null){
                                           $("#goToFAQ").hide()
                                        }
					if(fundInfo.latestNewValue!=""){
						queryEstimateByFundId("-1");
					}
					if (typeId!=null&&typeId == '0100') {/* 固定收益类 */
						var profit = fundInfo.profit;/* 收益率 */
						var profitTemp = 0;
						if (parseInt(profit) == profit && parseInt(profit) == 0) {
							profitTemp = "<em style='font-size: 1.3rem'>浮动收益</em>";
						} else {
							profitTemp = numMulti(parseFloat(profit), 100).toFixed(2) + "";
							profitTemp = "<em>" + profitTemp.split('.')[0] + "</em><b>." + profitTemp.split('.')[1] + "</b><i>%</i>";
						}

						var html = "<li><span>业绩报酬计提基准：</span><em>" + profitTemp + "</em></li>" + "<li><span>理财期限：</span><b>" + fundInfo.term + "</b><i>" + fundInfo.termUnit + "</i></li>"
								+ "<li><span>募集规模：</span><b>" + numDiv(scale, 10000) + "</b><i>万</i></li>" + "<li><span>起投金额：</span><b>" + numDiv(fundInfo.money, 10000) + "</b><i>万</i></li>";

						$("#fundTypeInfo").html(html);
					} else if (typeId == '0300') {/* 浮动收益类 */

						var html = "<li><span>业绩报酬计提基准：</span><em style='font-size: 1.3rem'>浮动收益</em></li>" + "<li><span>理财期限：</span><b>" + fundInfo.term + "</b><i>" + fundInfo.termUnit + "</i></li>" + "<li><span>募集规模：</span><b>"
								+ numDiv(scale, 10000) + "</b><i>万</i></li>" + "<li><span>起投金额：</span><b>" + numDiv(fundInfo.money, 10000) + "</b><i>万</i></li>";

						$("#fundTypeInfo").html(html);
					} else if (typeId!=null&&(typeId == '0210'||typeId == '0220')) {/* 开放性净值类 */

						if(typeId == '0210'){
							var html = "<li><span>业绩报酬计提基准：</span><em style='font-size: 1.3rem'>浮动收益</em></li>" + "<li></li>" + "<li><span>募集规模：</span><b>"
							+ numDiv(scale, 10000) + "</b><i>万</i></li>" + "<li><span>起投金额：</span><b>" + numDiv(fundInfo.money, 10000) + "</b><i>万</i></li>";
						
						}else{
							var html = "<li><span>业绩报酬计提基准：</span><em style='font-size: 1.3rem'>浮动收益</em></li>" + "<li><span>理财期限：</span><b>" + fundInfo.term + "</b><i>" + fundInfo.termUnit + "</i></li>" + "<li><span>募集规模：</span><b>"
							+ numDiv(scale, 10000) + "</b><i>万</i></li>" + "<li><span>起投金额：</span><b>" + numDiv(fundInfo.money, 10000) + "</b><i>万</i></li>";
						}

						if(typeId == '0210' && fundInfo.fundState == '0'){
							html = "<li><span>最新净值：</span><em id='newNet'>"+fundInfo.latestNewValue+"</em></li>" + "<li><span>最高净值：</span><b id='maxNet'>" + fundInfo.maxNav + "</b></li>" + "<li><span>累计收益率：</span><b id='sumNet'>"
							+ numMulti((parseFloat(fundInfo.latestNewValue?fundInfo.latestNewValue:1)-1),100).toFixed(2) + "%</b></li>" + "<li><span>起投金额：</span><b id='money'>" + numDiv(fundInfo.money, 10000) + "</b><i>万</i></li>";
						}


						$("#fundTypeInfoJinzhi").html(html);
						$("#fundTypeInfoJinzhi").show();
						$("#fundTypeInfo").hide();
					} else if (typeId == '0110') {/* 固定收益(净值)财富宝*/
						$("#cfbDate").html("下一到期日");
						var profit = fundInfo.profit;/* 收益率 */
						var profitTemp = 0;
						if (parseInt(profit) == profit && parseInt(profit) == 0) {
							profitTemp = "<em style='font-size: 1.3rem'>浮动收益</em>";
						} else {
							profitTemp = numMulti(parseFloat(profit), 100).toFixed(2) + "";
							profitTemp = "<em>" + profitTemp.split('.')[0] + "</em><b>." + profitTemp.split('.')[1] + "</b><i>%</i>";
						}
						if(fundInfo.state=='0'){/*申购*/
							var html = "<li><span>业绩报酬计提基准：</span><em>" + profitTemp + "</em></li>" + "<li><span>理财期限：</span><b>" + fundInfo.term + "</b><i>" + fundInfo.termUnit + "</i></li>"
							+ "<li><span>最新净值：</span><b>" + fundInfo.latestNewValue + "</b><i></i></li>" + "<li><span>起投金额：</span><b>" + numDiv(fundInfo.money, 10000) + "</b><i>万</i></li>";
						}else{
							var html = "<li><span>业绩报酬计提基准：</span><em>" + profitTemp + "</em></li>" + "<li><span>理财期限：</span><b>" + fundInfo.term + "</b><i>" + fundInfo.termUnit + "</i></li>"
							+ "<li><span>募集规模：</span><b>" + numDiv(scale, 10000) + "</b><i>万</i></li>" + "<li><span>起投金额：</span><b>" + numDiv(fundInfo.money, 10000) + "</b><i>万</i></li>";
						}

						$("#fundTypeInfo").html(html);
					} else if(typeId =='0500' || typeId == '0400'){  //现金产品
						var profit = fundInfo.profit;/* 收益率 */
						var sevenDayAnnualy="--"
                        var latestNewValue=fundInfo.latestNewValue?fundInfo.latestNewValue:"1.0000"
						if(fundInfo.sevenDayAnnualy&&fundInfo.sevenDayAnnualy!="0.00%"){
							sevenDayAnnualy=fundInfo.sevenDayAnnualy
						}
                        var estimatey=queryEstimateByFundId("-1")?queryEstimateByFundId("-1"):"--"
                        var benefitSinceCreated = fundInfo.benefitSinceCreated ? fundInfo.benefitSinceCreated : '0.00';
                        
						if(typeId =='0400'){
                           /* var html = "<li class='totalAmount'><p>总金额(元)</p><p id='totalAmount'></p></li>"+
                                "<li><div>"+estimatey+"</div><div>最近一个月涨跌幅</div></li>"+
                                "<li><div id='annualIncome'>" + sevenDayAnnualy + "</div><div>七日年化收益</div></li>"*/
							 var html = "<li class='totalAmount'><p>总金额(元)</p><p id='totalAmount'></p></li>"+
                             "<li><div id='latestNewValue'>"+latestNewValue+"</div><div>最新净值</div></li>"+
                             "<li><div id='annualIncome'>" + sevenDayAnnualy + "</div><div>七日年化收益</div></li>"
                        }else{
							/*var html = "<li class='totalAmount'><p>总金额(元)</p><p id='totalAmount'></p></li>"+
                                "<li><div>"+estimatey+"</div><div>最近一个月涨跌幅</div></li>"+
                                "<li><div id='annualIncome'>" + latestNewValue + "</div><div>最新净值</div></li>"*/
                        	var html = "<li class='totalAmount'><p>总金额(元)</p><p id='totalAmount'></p></li>"+
                            "<li><div id='latestNewValue'>"+benefitSinceCreated+"</div><div>成立以来年化收益率 </div></li>"+
                            "<li><div id='annualIncome'>" + latestNewValue + "</div><div>最新净值</div></li>"
                        	
						}

                      
                        $(".schedule").remove()
						$("#fundTypeInfo").html(html);
						$(".parameter").addClass("fixedProduct")
						$("#fundTypeInfo li").eq(1).addClass("newValue")
						$("#fundTypeInfo li").eq(2).addClass("newValue2")
						var flag = queryUserWarehouse(getUrlParameter('tradeacco'),fundInfo.fundId);
						queryProfitByFundCode(fundInfo.fundId);
						if(!flag){ //该用户没有持仓
							if(fundInfo.state == '1' && typeId == '0400') {
								sevenDayAnnualy = '--'
							    $('#profit').html('--');
							}
                            if(typeId =='0400'){
                                $("#totalAmount").html(sevenDayAnnualy)
                                $("#totalAmount").siblings("p").html("七日年化");
							}else{
                                /*$("#totalAmount").html(latestNewValue)
                                $("#totalAmount").siblings("p").html("最新净值");*/
								
								if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
									$("#totalAmount").html("--") 
								}else {
									$("#totalAmount").html(benefitSinceCreated + "<i>%</i>")
									
								}
	                             $("#totalAmount").siblings("p").html("成立以来年化收益率");
	                             
	                             if(!fundInfo.latestNewValue || fundInfo.latestNewValue == '0.00%') {
										$("#latestNewValue").html("--")
									} else {
										$("#latestNewValue").html(fundInfo.latestNewValue)
										
									}
									$("#latestNewValue").siblings("div").html("最新净值");
							}

							$("#annualIncome").html(numDiv(fundInfo.money,10000)+"万")
							$("#annualIncome").siblings("div").html("起购金额");
						} else {
							// 有持仓展示该产品总持仓
							$('#totalAmount').html(formatNumber((balance*(!fundInfo.latestNewValue?1:fundInfo.latestNewValue)).toFixed(2),','));
							hasBalance = true;
							if(fundInfo.state == '1' && typeId == '0400') {
								$('#annualIncome').html('--');
							    $('#profit').html('--');
							}
							
							if(typeId == '0500') {
								if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
									$("#latestNewValue").html("--")
								} else {
									$("#latestNewValue").html(benefitSinceCreated + "<i>%</i>")
									
								}
								$("#latestNewValue").siblings("div").html("成立以来年化收益率");
							}
						}
						
						if(showNetValue == '0' && typeId == '0400') {
							if(hasBalance) {
								if(!fundInfo.latestNewValue || fundInfo.latestNewValue == '0.00%') {
									$("#annualIncome").html("--")
								} else {
									$("#annualIncome").html(fundInfo.latestNewValue)
								}
								$("#annualIncome").siblings("div").html("最新净值");
								
								if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
									$("#latestNewValue").html("--")
								} else {
									$("#latestNewValue").html(benefitSinceCreated + "<i>%</i>")
									
								}
								$("#latestNewValue").siblings("div").html("成立以来年化收益率");
							} else {
								/*if(!fundInfo.latestNewValue || fundInfo.latestNewValue == '0.00%') {
									$("#totalAmount").html("--")
								} else {
									$("#totalAmount").html(fundInfo.latestNewValue)
								}
								$("#totalAmount").siblings("p").html("最新净值");*/
								if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
									$("#totalAmount").html("--")
								} else {
									$("#totalAmount").html(benefitSinceCreated + "<i>%</i>")
									
								}
								$("#totalAmount").siblings("p").html("成立以来年化收益率");
							}
							
						}
						
						$('.fixedProduct').siblings("tips").show();
						if(typeId == '0500') {
							$(".step-bg").remove();
							$(".surplus").hide();
							$(".redeemGo").show();
							$(".tips").show();
						}
                                                if(typeId == '0400') {$(".tips").show();}
					}
					else {
						errorRemark("错误的产品类别");
						window.location.href = "/WeixinService/business/query/fundList.shtml";
					}

					var displayLimit = parseFloat(fundInfo.displayLimit || 0);/* 展示剩余额度 */
					if (displayLimit == null) {
						displayLimit = scale;
					}
					var reservePro = numMulti(numDiv((scale - displayLimit), scale), 100);
					var showDisplayLimit = formatNumber(numDiv(displayLimit, 10000), ',');/* 剩余额度 */
					if (daysBetween(today, subdeadLine) > 0 || reservePro == 100) {
						reservePro = 100;
						showDisplayLimit = 0;
					}
					/* 投资进度百分比 86.542 */
					$("#proBar").css("width", reservePro + "%");/* 进度条 */
					$("#investPro").text(parseFloat(reservePro).toFixed(0));/*
					 * 投资进度
					 * 86.54
					 */
					$("#showDisplayLimit").html(showDisplayLimit + "<i>万元</i>");
					if(typeId == '0400'){
						$(".ordinary").hide();
						$(".fund7Day").show();
						if(fundInfo.state=="1" && daysBetween(fundInfo.interestDate, fundInfo.currentWorkdate) >= 0){
							$("#buyTime").html(fundInfo.interestDate);/* 购买时间 */
						}else{
							$("#buyTime").html(fundInfo.currentWorkdate);/* 购买时间 */
						}
						$("#expireTime").html(fundInfo.maturityDate);/* 首个可赎回日 */
						$("#netExpireTime").html(fundInfo.nextMaturityDate);/* 次个可赎回日 */
					}else{
						var appointDate = fundInfo.appointDate;/* 预约开始日期 */
						var appointEndDate = fundInfo.appointEndDate;/* 预约结束日期 */
						var salesDate = fundInfo.salesDate;/* 发售日 */
						var subdeadLine = fundInfo.subdeadLine;/* 认购截止 */
						var interestDate = fundInfo.interestDate;/* 起息日期 */
						var maturityDate = fundInfo.maturityDate;/* 到期日期 */
						var paymentDate = fundInfo.paymentDate;/* 产品到期清盘，预计打款日期 */
						var today = fundInfo.currentWorkdate;/* 当前工作日 */
						if (daysBetween(today, maturityDate) >= 0) {
							$("#line span:lt(3)").addClass("act");
							$(".cirle .cirle:lt(3)").addClass("act");
							$(".cirle .cirle:eq(3)").addClass("current");
							$(".step-text ul li span:lt(4)").addClass("act");
						} else if (daysBetween(today, subdeadLine) >= 0) {
							$("#line span:lt(2)").addClass("act");
							$(".cirle .cirle:lt(2)").addClass("act");
							$(".cirle .cirle:eq(2)").addClass("current");
							$(".step-text ul li span:lt(3)").addClass("act");
						} else if (daysBetween(today, salesDate) >= 0) {
							$("#line span:lt(1)").addClass("act");
							$(".cirle .cirle:lt(1)").addClass("act");
							$(".cirle .cirle:eq(1)").addClass("current");
							$(".step-text ul li span:lt(2)").addClass("act");
						} else if (daysBetween(today, appointDate) >= 0) {
							$("#line span:lt(0)").addClass("act");
							$(".cirle .cirle:lt(0)").addClass("act");
							$(".cirle .cirle:eq(0)").addClass("current");
							$(".step-text ul li span:lt(1)").addClass("act");

						} else {
							$("#line span:lt(0)").addClass("act");
							$(".cirle .cirle:lt(0)").addClass("act");
							$(".cirle .cirle:eq(0)").addClass("current");
							$(".step-text ul li span:lt(1)").addClass("act");
						}

						$("#appointDate").html(dataFormat1(appointDate));/* 预约开始日期 */
						$("#salesDate").html(dataFormat1(salesDate));/* 发售日 */
						$("#subdeadLine").html(dataFormat1(subdeadLine));/* 认购截止 */
						$("#maturityDate").html(dataFormat1(maturityDate));/* 到期日期 */
						localStorage.setItem('maturityDate',maturityDate);
					}
					$("input[name=appointDate]:eq(0)").val(appointDate);
					$("input[name=appointEndDate]:eq(0)").val(appointEndDate);
					$("input[name=salesDate]:eq(0)").val(salesDate);
					$("input[name=subdeadLine]:eq(0)").val(subdeadLine);
					$("input[name=currentWorkdate]:eq(0)").val(fundInfo.currentWorkdate);
					
					/* 产品内容介绍 */
					if (typeof fundInfo.elementList != 'undefined') {

						var templateStart = "<div class='box'><h2><span>产品基本信息</span></h2><div class='box-text'><ul>";
						var templateEnd = "</ul></div></div>";

						var temp1 = "";
						var temp2 = "";
						var temp3 = false;
						$.each(fundInfo.elementList, function(index, msg) {
							if (msg.type == 110) {
								temp1 += "<li><span>" + msg.title + "：</span><em>" + msg.content + "</em></li>";
							}else if (msg.type == 120) {
								temp2 += "<div class='box'><h2><span>" + msg.title + "</span></h2><div class='box-text'><p>" + msg.content + "</p></div></div>";
							} else if (msg.type == 210||msg.type == 230||msg.type == 220) {
								temp3 = true;
							} else {

							}
						});

						if (temp1.trim() != "") {
							$("#fundInfoBox").append(templateStart + temp1 + templateEnd);
						}
						if (temp2.trim() != "") {
							$("#fundInfoBox").append(temp2);
						}
						if(!temp3){
							$("#otherElement").hide();
						}
						
						
						if(typeId != null && typeId != '0110') {
							$("#showRedBox").hide();
						}else {
							var flag = queryUserWarehouse(getUrlParameter('tradeacco'),fundInfo.fundId);
							if(!flag) {
								$("#showRedBox").hide();
							} else {
								
								$("#showRedBox").show();
								var redeemAmount = 0;  //到期赎回份额
								var renewAmount = 0;		//到期续投份额
								var totalSubQuty = 0;
								/* 查询财富宝产品续存中的订单列表 */
								$.ajax({
									async: false,
									url: "/WeixinService/business/queryTradeInfoList.xhtml",
									data : {
										"fundId" : fundId,
										//"period" : period,
										"applyst" : 'G'
									},
									dataType: "json",
									cache: false,
									type: "post",
									error: function (textStatus, errorThrown) {
										show_tips("网络繁忙，请稍后再试。");
									},
									success: function (data) {
										var list = data.list;
										if (list != null && list.length > 0) {
											$.each(list, function (i, item) {
												item.redemptionShare = (!item.redemptionShare && item.renew == "N") ? item.subquty : item.redemptionShare;
												item.redemptionShare = item.redemptionShare ? item.redemptionShare : "0";
												redeemAmount +=  parseFloat(item.redemptionShare);
												totalSubQuty +=  parseFloat(item.subquty);
												
											});
										}
									}
								});
								//renewAmount = parseFloat(balance)-redeemAmount;
								renewAmount = totalSubQuty - redeemAmount;
								if(redeemAmount == 0 && renewAmount == 0) {
									$("#showRedBox").hide();
								}
								$('#showRedemption').html(formatNumber(parseFloat(redeemAmount).toFixed(2)));
								$('#showRenew').html(formatNumber(parseFloat(renewAmount).toFixed(2)));
							}
						}
					}	
					
					/* 购买按钮变化 */
					var currentWorkdate = fundInfo.currentWorkdate;
					var salesDate = fundInfo.salesDate;
					var subdeadLine = fundInfo.subdeadLine;
					var appointDate = fundInfo.appointDate;
					var appointEndDate = fundInfo.appointEndDate;
					var maxNumber = fundInfo.maxNumber;
					var buyerNumber = fundInfo.buyerNumber;
					var headaccount = fundInfo.headaccount;
					var openPloy = fundInfo.openPloy;//开放策略(0:每日开放;1:指定工作日开放)
					var openCycle = fundInfo.openCycle;
					//过了截止日 = 募集结束
					if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
						$("#button1").attr("href", "javascript:void(0)");
						$("#button1").css("background", "#999");
						$("#button1").html("募集结束");
						$("#disDiv2").hide();
					//认购期= 当前时间 大于等于 认购起始日 并且 小于 认购截止日 
					}else if(daysBetween(currentWorkdate,salesDate) >= 0 && daysBetween(currentWorkdate,subdeadLine) <= 0){
						//额度是否足够
						if(displayLimit > 0){
							$("#button1").html("立即购买");
						}else{
							$("#button1").html("参与排队").attr("href", "javascript:goToBuy();");
						}
						
					//预约期= (预约开始日 <= 当前时间  <= 预约截止日 )
					}else if(daysBetween(currentWorkdate,appointDate) >= 0 && daysBetween(currentWorkdate,appointEndDate) <= 0){
						//额度是否足够
						if(displayLimit > 0){
							$("#button1").html("立即预约");
						}else{
							$("#button1").html("参与排队").attr("href", "javascript:goToBuy();");
						}
					}else{
						$("#button1").html("立即预约");
					}

				} else {
					errorRemark("当前产品不存在");
					redirectUrl("/WeixinService/business/query/fundList.shtml");
				}
				//购买人数控制
				if(headaccount == 1){
					if(queryUserHasProOreder(fundInfo.fundId) == 0){
						if(maxNumber <= buyerNumber){
							$("#button1").attr("href", "javascript:void(0)");
							$("#button1").css("background", "#999");
							$("#button1").html("人数已满");
						}
					}
				}
				if(openPloy == '1' && openCycle != null && openCycle!= ''){
					var strs= new Array(); //定义一数组 
					strs=openCycle.split(","); //字符分割 
					var week = "";
					for(i=0;i<strs.length;i++){
						week += getWeek(parseInt(strs[i]));
						if(i!=strs.length-1){
							week +="、";
						}
					}
					var date = fundInfo.state == '0'?'15:00':'17:00';
					$("#disDiv").html("工作日"+ week + date +"前可购买");
					$("#disDiv").show();
					$(".buy").css("height","90px");
					if(openCycle.indexOf(new Date().getDay()) != -1){
						if(fundInfo.state == '0'){
							if(!dateComparison(1)){
								$("#button1").attr("href", "javascript:void(0)");
								$("#button1").css("background", "#999");
							}
						}else if(fundInfo.state == '1'){
							if(!dateComparison(2)){
								$("#button1").attr("href", "javascript:void(0)");
								$("#button1").css("background", "#999");
							}
						}	
					}else{
						if(fundInfo.state == '0'){
							$("#button1").attr("href", "javascript:void(0)");
							$("#button1").css("background", "#999");
						}else if(fundInfo.state == '1'){
							$("#button1").attr("href", "javascript:void(0)");
							$("#button1").css("background", "#999");
							
						}	
					}
				}
			} else {
				errorRemark(data.returnMsg);
				redirectUrl("/WeixinService/business/query/fundList.shtml");
			}
		}
	});
}
/**
 * 投资项目信息入口
 */
function goToElement(){
	redirectUrl("/WeixinService/business/query/fundElement.shtml?orally=fund&fundId="+$("#fundId").val()+"&period="+$("#period").val());
}
/**
 * 财富宝续存中的购买订单列表
 */
function goToRedeemList(){
	//redirectUrl("/WeixinService/business/query/orderList.shtml?fundId="+$("#fundId").val()+"&period="+$("#period").val()+"&applyst=G");
	redirectUrl("/WeixinService/business/query/orderList.shtml?fundId="+$("#fundId").val()+"&period="+"&applyst=G");
	//redirectUrl("/WeixinService/business/query/orderList.shtml?fundId="+$("#fundId").val()+"&period=1"+"&applyst=G");
}

/**
 * 历史净值页面入口
 */
function goToOldNet(){
	redirectUrl("/WeixinService/business/query/oldNetList.shtml?fundId="+$("#fundId").val());
}
/*查询产品净值(曲线图)*/
function queryEstimateByFundId(dateTime){
	$("#highchart").show();
	$(".head .right a").show();
	$(".head .right span").hide();
	var fundId = $("#fundId").val();
	$(".highchart-box .highchart_nav ul li").removeClass("act");
	if(parseInt(dateTime) == -1){
		$(".highchart-box .highchart_nav ul li:eq(0)").addClass("act");
	}else if(parseInt(dateTime) == -3){
		$(".highchart-box .highchart_nav ul li:eq(1)").addClass("act");
	}else if(parseInt(dateTime) == -6){
		$(".highchart-box .highchart_nav ul li:eq(2)").addClass("act");
	}else if(parseInt(dateTime) == -12){
		$(".highchart-box .highchart_nav ul li:eq(3)").addClass("act");
	}
	var tempValue=""
	$.ajax({
		async:false,
		url : "/WeixinService/business/queryEstimateByFundId.xhtml",
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
			var productEstimateList = data.productEstimateList;
			var stepNum = 1;
			if(productEstimateList != null && productEstimateList.length > 0){
				stepNum = Math.ceil(numDiv(parseInt(productEstimateList.length,10),5));
				var maxVal = "1";
				if(productEstimateList != null && productEstimateList.length > 0){
					maxVal = productEstimateList[0].maxNetValue;
				}
				
				
				
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
					temp = "<i>--</i>";
				}else{
					temp = numMulti(numDiv((newNetVal-oldNetVal),oldNetVal),100);
					if(temp < 0){
						temp = "<i class='green'>"+numMulti(numDiv((newNetVal-oldNetVal),oldNetVal),100).toFixed(2)+"%</i>";
					}else {
						temp = "<i> +"+numMulti(numDiv((newNetVal-oldNetVal),oldNetVal),100).toFixed(2)+"%</i>";
					}
					tempValue=numMulti(numDiv((newNetVal-oldNetVal),oldNetVal),100).toFixed(2)+"%";
				}
				if(dateTime != null && dateTime == -1){
					htmls_1 += "最近1个月涨跌幅:"+temp;
				}else if(dateTime != null && dateTime == -3){
					htmls_1 += "最近3个月涨跌幅:"+temp;
				}else if(dateTime != null && dateTime == -6){
					htmls_1 += "最近6个月涨跌幅:"+temp;
				}else if(dateTime != null && dateTime == -12){
					htmls_1 += "最近1年涨跌幅:"+temp;
				}
				$(".head .left").html(htmls_1);
				showHighchart1(xVals,yVals,netVals,fluctuates,stepNum);
                
			}else{
				if(dateTime != null && dateTime == -1){
					htmls_1 += "最近1个月涨跌幅:<i>--</i>";
				}else if(dateTime != null && dateTime == -3){
					htmls_1 += "最近3个月涨跌幅:<i>--</i>";
				}else if(dateTime != null && dateTime == -6){
					htmls_1 += "最近6个月涨跌幅:<i>--</i>";
				}else if(dateTime != null && dateTime == -12){
					htmls_1 += "最近1年涨跌幅:<i>--</i>";
				}
				$(".head .left").html(htmls_1);
				showHighchart1("0","0","0","0",stepNum);
			}
		}
	})
	return tempValue;
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
		netArray.push(netVals.split(";")[i]);
	}
	var fluctuateArray = new Array();
	for(var i = fluctuates.split(";").length-1;i >= 0;i--){
		fluctuateArray.push(parseFloat(numMulti(fluctuates.split(";")[i],100)).toFixed(2));
	}
	/*$(".head .left").html("单位净值：<i>"+netArray[netArray.length-1].split(".")[0]+"</i>."+netArray[netArray.length-1].split(".")[1]);*/
	//$("#fundTypeInfoJinzhi #newNet").html(netArray[netArray.length-1].split(".")[0]+"."+netArray[netArray.length-1].split(".")[1]);
	//$("#fundTypeInfoJinzhi #sumNet").html("<em>"+numMulti((parseFloat(netArray[netArray.length-1])-1),100).toFixed(2)+"</em>%");
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
            type: 'area',
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
            	$(".head .right a").hide();
            	$(".head .left").html("单位净值：<i>"+parseFloat(this.y).toFixed(4)+"</i>");
            	$(".head .right span").show().html(this.x);
            	var a = '<b>' + this.x + '<b><br/>当期净值：' + parseFloat(this.y).toFixed(4) + '<br/>涨跌幅度：' + parseFloat(this.points[0].point.fluctuate).toFixed(2) +"%";
            	return a;
            }
        },
        legend: {
            enabled:false,
        },
        series: [{
            color: '#EFBEC4',
            name: false,
            data: json
        }]
    });
}
/* 查询产品净值*/
/*function queryEstimateByFundIdByPage(pages){
	var fundId = $("#fundId").val();
	var pageInput = $("#page").val();
	var page = returnPage(pages);
	$.ajax({
		async:true,
		url : "/WeixinService/business/queryEstimateByFundIdByPage.xhtml",
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
			var list = data.productEstimates;
			if(list != null && list.length > 0){
				$("#fundTypeInfoJinzhi #maxNet").html(parseFloat(list[0].maxNetValue).toFixed(4));
			}
		}
	})
}*/
/*4个时间节点点击显示事件*/
function show_fundInfo(_id){
	$("#fundInfo_tips").show();
	$("#"+_id).siblings().hide();
	$("#"+_id).show();
}
/*4个时间节点点击隐藏事件*/
function close_fundInfo(_id){
	$("#"+_id).hide();
	$("#"+_id).children().hide();
}
function returnPage(pages){
	var page;
	var maxPages = $("#maxPages").val();
	
	if(parseInt(pages) >= parseInt(maxPages)){
		page = maxPages;	
	}else if(parseInt(pages) <= 0){
		page = "1";
	}else{
		page = pages; 
	}
	return page;
}
/**
 * 查询用户是否有持仓
 */
function queryUserWarehouse(tradeacco,fundId){
	var flag=true;
    $.ajax({
    	async:false,
		url:"/WeixinService/business/queryCustTradeInfo.xhtml",
		type:"post",
		dataType:'json',
		data:{
			"fundCode":fundId,
			"tradeAcco":tradeacco
		},
		success:function(res){
		     if(res.returnCode=="0000"){
		    	if(res.buySatte=="Y"){
		    		var fundinfo = res.data;
		    		balance = fundinfo.balance;
		    		var totalAmount=formatNumber(fundinfo.balance, ',');
		    		$("#totalAmount").html(totalAmount);
		    		$(".redeemGo a").attr("href","/WeixinService/business/query/userRedeem.shtml?fundId="+fundId+"&period="+period+"&tradeacco="+tradeacco);
		    		$(".redeemGo a").addClass("redeemGoStatus");
		    		hasBalance = true;
		    	}else{
		    		$("#totalAmount").html("0");
		    		$(".redeemGo a").attr("href","javascript:void(0)");
		    		flag=false;
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
 * 查询用户是否持有该产品订单
 */
function queryUserHasProOreder(fundId){
	 var result; 
	 $.ajax({
    	async:false,
		url:"/WeixinService/business/queryUserHasProOreder.xhtml",
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

/**
 * 查询七日年化及万份收益
 * @param {Object} fundId
 */
function queryProfitByFundCode(fundId){
	 $.ajax({
    	async:false,
		url:"/WeixinService/business/queryProfitByFundCode.xhtml",
		type:"post",
		dataType:'json',
		data:{
			"fundCode":fundId
		},
		success:function(res){
		     if(res.returnCode=="0000"){
		     	if(res.data){
		     		$("#profit").text(res.data.per_share_income);
		    	    $("#annualIncome").text(res.data.seven_day_annualy);
		     	}
		     }
		},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
	   }
	})
}
//*********************MGM***********************



//查询邀请码 同时是否开启积分功能
function queryIntegralDeatail(){
	var openMgm=queryParamList("SYSTEM","enableIntegral","")[0].pmco
	if(openMgm=="1"){
		var param=getUrlParams();
		if(param.isShare=="Invitation"){  //说明该用户是通过mgm入口分享而来
			$("#mask").show();
		}
		if(queryUserToRealName()){
			$.ajax({
				url:'/WeixinService/business/integral/getIntegralInfo.xhtml',
				data:{},
				dataType:'json',//服务器返回json格式数据
				type:'get',//HTTP请求类型
				async:false,
				success:function(data){
					title=custName+"向您推荐"+shareAdname;  //标题
					desc="招商体系，值得信赖";  //描述
				 	if(eventId=='event_fundListTemplateMsgGuide' && pageSource){ 
				 		link=host+"/WeixinService/weixinLogin/register.shtml?invitation="+data.invitationCode+"&fundId="+param.fundId+"&period="+param.period
				 			+'&eventId=event_shareTemplateMsg'+'&pageSource='+pageSource+'&userid='+userId  //转发链接
				 	}else{
				 		link=host+"/WeixinService/weixinLogin/register.shtml?invitation="+data.invitationCode+"&fundId="+param.fundId+"&period="+param.period  //转发链接
				 	}
				}
			});
		}else{
			link=host+"/WeixinService/weixinLogin/register.shtml?fundId="+param.fundId+"&period="+param.period  //转发链接
		}
	}else{
		$(".invitation").hide()
	}
 }
// 查询用户信息 【判断是否实名】
function queryUserToRealName() {
	   var flag=false;
	   $.ajax({
		   async:false,
		   url: "/WeixinService/business/queryUserinfo.xhtml",
		   data: "",
		   dataType: "json",
		   cache: false,
		   type:"post",
		   error : function(textStatus, errorThrown) {  
			   errorRemark("网络繁忙，请稍后再试。");  
		   }, 
		   success : function (data){
			   if(data.userType == "30"){
				   flag=true;
				   custName=data.custName;
			   }   
		   }
	   });
	   return flag;
}

function operatingRecord (pageSource,event,userId,page,openid,group,unionid,data){
	  var parm={
	           'data' : data,
	            'event' : event,
	            'group' :group,
	            'openid' : "",
	            'pageSource':pageSource,
	            'page':page,
	            'trackDate':getNowFormatDate(),
	            'trackMillis':new Date().getTime(),
	            'unionid':unionid,
	            'userId':userId
	   }
	   $.ajax({
	   	    async:false,
	        url:apiHost+"/api/wxtrack",
	        data:JSON.stringify(parm),
	        dataType: "json",
	        contentType:"application/json",
	        cache: false,
	        type: "POST",
	        success: function(n) {}
	        
	    });
}

function eventBind(){
	$("#mask").click(function(){
		$("#mask").hide();
	})
	$(".invitation").click(function(){
		$("#mask").show();
	})
}

function getWeek(number) {
    if (number == "") {
        return;
    } else {
            text = "";
        switch (number) {
            case 1:
                text = "周一";
                break;
            case 2:
                text = "周二";
                break;
            case 3:
                text = "周三";
                break;
            case 4:
                text = "周四";
                break;
            case 5:
                text = "周五";
                break;
        }
     return text;
    }
}

function dateComparison(type){
	var date = new Date();
	var hour = Appendzero(date.getHours());
	var minute = Appendzero(date.getMinutes());
	var second = Appendzero(date.getSeconds());
	var current = parseInt(hour +''+minute+''+second);
	if(type == '1'){
		if(current <= 150000){
			return true;
		}
	}else if(type == '2'){
		if(current <= 170000){
			return true;
		}
	}	
	return false;
}

function Appendzero (obj) {
   if (obj < 10) return "0" + obj; else return obj;
}