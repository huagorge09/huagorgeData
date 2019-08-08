var myScroll;
var seven ='0';
try{
	seven = queryParamList("SYSTEM","SHOWNETVALUE","")[0].pmco;
}catch(err){
	console.log("开关查询失败");
}
$(document).ready(function(e) {
	$(".header .top-a h2").html("订单详情");
	document.title="订单详情";
	myScroll = new IScroll('#wrapper', {
		scrollbars: true,
		mouseWheel: true,
		interactiveScrollbars: true,
		shrinkScrollbars: 'scale',
		fadeScrollbars: true
	});
	
	queryTradeInfoByTradeNo();

	/*20180103crm数据迁移需求, 移除查询旧版PDF合同*/
	/* 新版本合同 */
	queryFundContractById();

	getUserRequest("order-detail");/* 此处subPath为页面内行为 */
});

document.addEventListener('touchmove', function (e) {e.preventDefault(); }, false);

/* 查询订单详情 */
function queryTradeInfoByTradeNo(){
	var serialno = getUrlParameter("serialno");
	var period = getUrlParameter("period");
	if(serialno == null || serialno == ""){
		errorRemark("没有查询到订单");
		redirectUrl("/WeixinService/business/query/orderListNew.shtml");
		return;
	}
	$.ajax({
    	async:false,
        url: "/WeixinService/business/queryTradeInfoByTradeNo.xhtml",
        data: {
        	"serialno":serialno,
        	"period":period
        },
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        }, 
        success : function (data){

        	if(data.returnCode != null && data.returnCode == "0000"){
        		/* 如果订单信息都为空的话，当前订单可能已经被取消了，返回到列表页面 */
        		if(data.dto == null &&data.tradeList==null){
            		errorRemark("当前订单信息有误");
            		redirectUrl("/WeixinService/business/query/fundListNew.shtml");
            		return;
            	}else{
            		var dto = data.dto;
            		var fundInfo = dto.fundInfoDtoV2;
            		var tradeList = data.tradeList;
            		var htmls = "";
            		serialno = dto.serialno;
            		/* 隐藏input赋值 */
            		$("#serialno").val(dto.serialno);
            		$("#fundId").val(fundInfo.fundId);
            		$("#subamt").val(dto.subamt);
            		$("#typeId").val(fundInfo.typeId);
            		$("#period").val(dto.period);
            		$("#fee").val(dto.fee||0);
            		$("#tradeAcco").val(dto.tradeacco);
            		$("#isSubPrdAppraisement").val(fundInfo.isSubPrdAppraisement);
					$("#outerId").val(fundInfo.outerId);
            		
            		if(tradeList != null && tradeList.length >0){
    	        		$("#bankacco").val(tradeList[0].bankacco);
    	        		$("#realBankName").val(tradeList[0].realBankName);
            		}
            		if (typeof (fundInfo.templetId) != undefined) {
    					$("#templetId").val(fundInfo.templetId);
    				}
            		var orderType = dto.orderType;
            		var period=dto.period;
            		var appointDate = fundInfo.appointDate;/* 预约开始日期 */
    				var appointEndDate = fundInfo.appointEndDate;/* 预约结束日期 */
    				var salesDate = fundInfo.salesDate;/* 发售日 */
    				var subdeadLine = fundInfo.subdeadLine;/* 认购截止 */
    				var interestDate = fundInfo.interestDate;/* 起息日期 */
    				var maturityDate = fundInfo.maturityDate;/* 到期日期 */
    				var paymentDate = fundInfo.paymentDate;/* 产品到期清盘，预计打款日期 */
    				var today = fundInfo.currentWorkdate;/* 当前工作日 */
    				var currentWorkdate = fundInfo.currentWorkdate;/* 当前工作日 */
            		var typeId = fundInfo.typeId;
            		var state = fundInfo.state;
            		var renew = dto.renew;
            		var isSubPrdAppraisement = fundInfo.isSubPrdAppraisement;
    				var prjLName = fundInfo.prjLName;
    				var appPayMoneyText = formatDate1(salesDate) +" - " + formatDate1(subdeadLine) + "15:00前";//预约期打款时间
            		$("#adName").html(fundInfo.adname);
            		
            		if(typeId == '0400') {
						if(renew=="N"){
							$(".fund7DayPlan em").html("赎回到银行卡");
							$("#renewDiv li").eq(1).addClass("act").siblings().removeClass("act")
						}else{
							$(".fund7DayPlan em").html("滚存入下一期");
							$("#renewDiv li").eq(0).addClass("act").siblings().removeClass("act");
						}
						$(".fund7DayPlan").attr("renew",renew);
					}
					// 查询产品净值

					if(fundInfo.latestNewValue!=""){
						queryEstimateByFundId("-1");
						queryEstimateByFundIdByPage(1);
					}
					htmls += "<div class='order-main clear'>";
					var benefit = dto.benefit;/* 业绩报酬计提基准 */
					if (benefit != null && benefit != 0) {
						benefit = "<em>" + format(new Number(dto.benefit || 0)) + "</em><i>元</i>";
					} else {
						benefit = "<em>---</em>";
					}
					var shareAmt = dto.shareAmt;/* 已分配金额 */
					if (shareAmt != null && shareAmt != 0) {
						shareAmt = "<em>" + format(new Number(dto.shareAmt || 0)) + "</em><i>元</i>";
					} else {
						shareAmt = "<em>---</em>";
					}
    				if(typeId!=null&&(typeId=='0100'||typeId=='0300')||typeId=='0400'){/* 固收产品或者浮动收益 0400为7天14天理财*/
    					 /**七天十四天产品**/
						if(typeId=='0400'){
							if(seven == '1'){
								var sevenDayAnnualy="--"
	                        	if(fundInfo.sevenDayAnnualy&&fundInfo.sevenDayAnnualy!="0.00%"){
	                        		sevenDayAnnualy=fundInfo.sevenDayAnnualy
	                        	}
								if(fundInfo.state == '1') {
									sevenDayAnnualy = '--';
								}
	                        	$("#profit").html("<span>七日年化收益：</span><em>"+sevenDayAnnualy+"</em>");
							}else{
								var latestNewValue =fundInfo.latestNewValue?fundInfo.latestNewValue:"1.0000";
						   	    $("#profit").html("<span>最新净值：</span><em>"+latestNewValue+"</em>");
							}
							
							$(".ordinary").hide();
							$(".fund7Day").show();
							if(dto.apkind == '020' || dto.apkind == '720' || dto.apkind == '820'){
								$("#buyTime").html(fundInfo.interestDate);/* 基金成立日期 */
							}else{
								$("#buyTime").html(dto.workdate);/* 购买时间 */
							}
							$("#expireTime").html(fundInfo.maturityDate);/* 首个可赎回日 */
							$("#netExpireTime").html(fundInfo.nextMaturityDate);/* 次个可赎回日 */
                                                        if(fundInfo.term=="14"){
							     $(".fund7Day-text em").html("存续十四天")
							}
							if(orderType=='5'){
								$(".fund7Day-text").html("");
								$("#cirle .cirle").eq(0).remove();
								$("#cirle .cirle").addClass("act");
								$("#line span").addClass("act");
								$(".fund7Day").addClass("readyPeriod");
								$("#netExpireTime").parents("li").remove();
								$("#buyTime").parents("li").find("span").html("购买");
								$("#expireTime").parents("li").find("span").html("赎回");
								$("#expireTime").html(dto.redeemTime);
							}
							if(orderType=="4"){
								$("#renewDiv li:eq(1) span").css({"color":"red","font-size":"1.4rem"}).html("我们将在"+formatDate1(fundInfo.maturityDate)+"为您发起赎回申请，预计资金将于"+formatDate1(fundInfo.arrivalAccountDate)+"到账");
							}

						}else{
							if(parseInt(fundInfo.profit) == fundInfo.profit && parseInt(fundInfo.profit) == 0){
								$("#profit").html("<span>业绩报酬计提基准：</span><em style='font-size: 1.3rem'>浮动收益</em>");
							}else{
								$("#profit").html("<span>计提基准：</span><em>"+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[0]+"</em><b>."+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[1]+"</b><i>%</i>");
							}
						}
                        $("#term").html("<span>理财期限：</span><b>"+fundInfo.term+"</b><i>"+fundInfo.termUnit+"</i>");
                        $("#scale").html("<span>募集规模：</span><b>"+numDiv((fundInfo.scale||0),10000)+"</b><i>万</i>");
                        $("#money").html("<span>起投金额：</span><b>"+numDiv((fundInfo.money||0),10000)+"</b><i>万</i>");
                        $("#saleBar").html("<em style='width:"+numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100)+"%'></em>");
                        $("#saleNo").html(numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100).toFixed(0)+"<i>%</i>");
    					if(orderType!=null&&orderType=='1'){/* 未支付 */ 
    						htmls += "<h2><span class='fl notpaid'>待付款</span>";
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>待支付金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span>";
                            htmls += "<a href='javascript:redirectUrl(\"/WeixinService/business/query/modifyMoneyNew.shtml?serialno="+dto.serialno+"\")' class='fr'>修改</a></li>";
                            htmls += "<li><span class='fl' id='ec" +
                            		"ontract'></span></li>";
                            htmls += "</ul>";
                            if(daysBetween(today,salesDate) >= 0){
                                htmls += "<span class='order-main-point'>请尽快完成汇款，以到账时间锁定产品份额</span>";
                            }else if(daysBetween(today,appointDate) >= 0){
                            	htmls += "<span class='order-main-point'>请您于募集期(<span style='color:#ca132c;'>"+appPayMoneyText+"</span>)完成汇款</span>";
                            }
    					}else if(orderType!=null&&orderType=='2'){/* 已支付 */
    						htmls += "<h2><span class='fl accountpaid'>已付款</span>";
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    						if(fundInfo.typeId == '0400'){
    							htmls += "<div class='order-main-con updateorder-main-con' style='min-height:90px'>";
    						}else{
    							htmls += "<div class='order-main-con updateorder-main-con'>";
    						}
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>支付金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            if(fundInfo.typeId != '0400'){
                            	htmls += "<span class='order-main-point'>已收到来款，"+fundInfo.interestDate.substr(0,4)+"年"+fundInfo.interestDate.substr(5,2)+"月"+fundInfo.interestDate.substr(8,2)+"日产品正式起息</span>";
                            }
    					}else if(orderType!=null&&orderType=='3'){/* 排队中 */
    						htmls += "<h2><span class='fl orderdetaillineup'>排队中</span>";
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>待支付金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span>";
                            htmls += "<a href='javascript:redirectUrl(\"/WeixinService/business/query/modifyMoneyNew.shtml?serialno="+dto.serialno+"\")' class='fr'>修改</a></li>";
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            htmls += "<span class='order-main-point'>您的订单已进入排队等候，如有额度释放将第一时间通知您，额度详情请致电400-8878-555。</span>";
    					}else if(orderType!=null&&orderType=='4'){/* 存续中 */
    						if(typeId == '0400') {
    							$(".fund7DayPlan").attr("mdate",maturityDate)
    							if(renew=="N"){
    								$(".fund7DayPlan em").html("赎回到银行卡");
    								$("#renewDiv li").eq(1).addClass("act").siblings().removeClass("act");
    								$(".fund7DayPlan span:eq(1)").html("将于"+formatDate1(fundInfo.maturityDate)+"发起赎回申请");
    								$(".fund7DayPlan").addClass("redeemSet");
    							}else{
    								$(".fund7DayPlan em").html("滚存入下一期");
    								$("#renewDiv li").eq(0).addClass("act").siblings().removeClass("act");
    							}
    							$(".fund7DayPlan").attr("renew",renew);
    						}
    						htmls += "<h2><span class='fl surviving'>存续中</span>";
    						$("#schedule").html("<span class='service'>客户服务热线：</span><label class='serviceMobileClass'>400-8878-555</label>");
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(dto.subamt||0)+"</em><i>元</i></span></li>";
                            if(typeId=='0300'){
                            	htmls += "<li><span class='fl'>已分配金额：<em>"+shareAmt+"</em></span></li>";
                            }
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            $("#schedule").html("<span class='service'>客户服务热线：</span><label class='serviceMobileClass'>400-8878-555</label>");
                            htmls += "<span class='order-main-point'>产品处于存续期，预计"+fundInfo.maturityDate.substr(0,4)+"年"+fundInfo.maturityDate.substr(5,2)+"月"+fundInfo.maturityDate.substr(8,2)+"日到期</span>";
    					}else if(orderType!=null&&orderType=='5'){/* 已到期 */
    						htmls += "<h2><span class='fl hasexpire'>已到期</span>";
    						$("#schedule").html("<span class='service'>客户服务热线：</span><label class='serviceMobileClass'>400-8878-555</label>");
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(dto.subamt||0)+"</em><i>元</i></span></li>";
                            if(typeId=='0300'){
                            	htmls += "<li><span class='fl'>总分配金额：<em>"+shareAmt+"</em></span></li>";
                            }else{
                            	//htmls += "<li><span class='fl'>投资收益："+benefit+"</span></li>";
                            }
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            $("#schedule").html("<span class='service'>客户服务热线：</span><label class='serviceMobileClass'>400-8878-555</label>");
                            if(typeId =='0110') {
                            	var redemptionShare = (!dto.redemptionShare && dto.renew == "N") ? dto.subquty : dto.redemptionShare;
        						redemptionShare = redemptionShare ? redemptionShare : "0";
        						var nextSubquty = parseFloat(dto.subquty) - parseFloat(redemptionShare);
								    htmls += "<div style='display: inline-block;width: 49%;margin-top: 5px;margin-bottom: 5px;font-size: 14px;text-align: center;'><p style='margin-bottom: 5px;font-size: 14px;color:#999;'>到期赎回</p><p><em style='font-size: 14px;color:#000;'>" +  formatNumber(parseFloat(redemptionShare).toFixed(2)) + "</em><em style='color:#000;'>份</em></p></div>";
                                    htmls += `<p style='border:1px solid #e2e2e2; height:30px;display:inline-block;margin-top: 10px;'></p>`
                                    htmls += "<div style='display: inline-block;width: 50%;margin-top: 5px;margin-bottom: 5px;font-size: 14px;text-align: center;'><p style='margin-bottom: 5px;font-size: 14px;color:#999;'>到期滚存</p><p><em style='font-size: 14px;color:#000;'>" + formatNumber(parseFloat(nextSubquty).toFixed(2)) + "</em><em style='color:#000;'>份</em></p></div>";
                        	}else {
                        		htmls += "<span class='order-main-point'>产品已到期，预计5个工作日内完成收益分配</span>";
                        	}
    					}else if(orderType!=null&&orderType=='6'){/* 已失效 */
    						htmls += "<h2><span class='fl orderdetailexpired'>已失效</span>";
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul class='hui'>";
                            if(dto.payst != null && dto.payst == "Y"){
                                htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                                htmls += "<li><span class='fl'>计提基准：<em>"+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[0]+"</em><b>."+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[1]+"</b><i>%</i></span></li>";
                            }else{
                                htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                            }
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            if(dto.payst != null && dto.payst == "Y"){
                                htmls += "<span class='order-main-point'>买入产品失败，将于5个工作日内返还来款</span>";
                            }else{
                                htmls += "<span class='order-main-point'>买入产品失败，未收到您的来款</span>";
                            }
    					}else if(orderType!=null&&orderType=='7'){/* 待确认 */
    						htmls += "<h2><span class='fl orderdetailconfirm'>待确认</span>";
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                            htmls += "<li><span class='fl'>计提基准：<em>"+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[0]+"</em><b>."+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[1]+"</b><i>%</i></span></li>";
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            htmls += "<span class='order-main-point'>请尽快确认订单，完成汇款</span>";
    					}else if(orderType!=null&&orderType=='8'){/* 受理中 */
    						
    					}
    					if(typeId=="0110"||typeId=="0400"){
    						$(".fund7DayPlan").show();
    					}
						$(".fund7DayPlan").click(function(){
							if(orderType!=="5"){
								if(dto.redeemState == "0"){
									$("#renewDiv").show();
								}
							}
						})
    				}else if(typeId != null && (typeId == "0210" || typeId == "0220")){
    					if(orderType!=null&&(orderType=='4'||orderType=='5')){/* 已到期和存续中订单显示最新净值无需判断 */ 
    						$("#profit").html("<span>最新净值：</span><em>"+fundInfo.latestNewValue+"</em>");
                            $("#term").html("<span>最高净值：</span><b>"+fundInfo.maxNav+"</b>");
                            $("#scale").html("<span>累积收益率：</span><b>"+numMulti((parseFloat(fundInfo.latestNewValue)-dto.confirmNav),100).toFixed(2)+"%</b>");
                            $("#money").html("<span>起投金额：</span><b>"+numDiv((fundInfo.money||0),10000)+"</b><i>万</i>");
                            $("#saleBar").html("<em style='width:"+numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100)+"%'></em>");
                            $("#saleNo").html(numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100).toFixed(0)+"<i>%</i>");
                            if(orderType!=null&&orderType=='4'){/* 存续中 */ 

                            	htmls += "<h2><span class='fl surviving'>存续中</span>";
								htmls += "<span class='fr'><em>编号：</em><i>" + dto.serialno + "</i></span></h2>";
								htmls += "<div class='order-main-con updateorder-main-con'>";
								htmls += "<ul>";
								htmls += "<li><span class='fl'>买入金额：<em>" + formatNumber(dto.subamt || 0) + "</em><i>元</i></span></li>";
								htmls += "<li><span class='fl'>持仓盈亏：" + benefit + "</span></li>";
								htmls += "<li><span class='fl' id='econtract'></span></li>";
								htmls += "</ul>";
								$("#schedule").html("<span class='service'>客户服务热线：</span><label class='serviceMobileClass'>400-8878-555</label>");
                            	htmls += "<span class='order-main-point'>产品处于存续期，预计"+fundInfo.maturityDate.substr(0,4)+"年"+fundInfo.maturityDate.substr(5,2)+"月"+fundInfo.maturityDate.substr(8,2)+"日到期</span>";
                            }else if(orderType!=null&&orderType=='5'){/* 已到期 */ 
                            	htmls += "<h2><span class='fl hasexpire'>已到期</span>";
								htmls += "<span class='fr'><em>编号：</em><i>" + dto.serialno + "</i></span></h2>";
								htmls += "<div class='order-main-con updateorder-main-con'>";
								htmls += "<ul>";
								htmls += "<li><span class='fl'>买入金额：<em>" + formatNumber(dto.subamt || 0) + "</em><i>元</i></span></li>";
								htmls += "<li><span class='fl'>持仓盈亏：" + benefit + "</span></li>";
								htmls += "<li><span class='fl' id='econtract'></span></li>";
								htmls += "</ul>";
								$("#schedule").html("<span class='service'>客户服务热线：</span><label class='serviceMobileClass'>400-8878-555</label>");
								if(typeId =='0110') {
									var redemptionShare = (!dto.redemptionShare && dto.renew == "N") ? dto.subquty : dto.redemptionShare;
	        						redemptionShare = redemptionShare ? redemptionShare : "0";
	        						var nextSubquty = parseFloat(dto.subquty) - parseFloat(redemptionShare);
                                    htmls += "<div style='display: inline-block;width: 49%;margin-top: 5px;margin-bottom: 5px;font-size: 14px;text-align: center;'><p style='margin-bottom: 5px;font-size: 14px;color:#999;'>到期赎回</p><p><em style='font-size: 14px;color:#000;'>" +  formatNumber(parseFloat(redemptionShare).toFixed(2)) + "</em><em style='color:#000;'>份</em></p></div>";
                                    htmls += `<p style='border:1px solid #e2e2e2; height:30px;display:inline-block;margin-top: 10px;'></p>`
                                    htmls += "<div style='display: inline-block;width: 50%;margin-top: 5px;margin-bottom: 5px;font-size: 14px;text-align: center;'><p style='margin-bottom: 5px;font-size: 14px;color:#999;'>到期滚存</p><p><em style='font-size: 14px;color:#000;'>" + formatNumber(parseFloat(nextSubquty).toFixed(2)) + "</em><em style='color:#000;'>份</em></p></div>";
                            	}else {
                            		htmls += "<span class='order-main-point'>产品已到期，预计5个工作日内完成收益分配</span>";
                            	}
                            }
                            
                        	// queryEstimateByFundId("-1");
                        	// queryEstimateByFundIdByPage(1);
                             if("n" == isSubPrdAppraisement.toLowerCase()){
                            	 downText(prjLName);
							 }
    					}else{
    						if((fundInfo.typeId=='0210'||fundInfo.typeId=='0220') && fundInfo.state=='0'){
                                $("#profit").html("<span>最新净值：</span><em>"+fundInfo.latestNewValue+"</em>");
                                $("#term").html("<span>最高净值：</span><b>"+fundInfo.maxNav+"</b>");
                                $("#scale").html("<span>累积收益率：</span><b>"+numMulti((parseFloat(fundInfo.latestNewValue)-dto.confirmNav),100).toFixed(2)+"%</b>");
                                $("#money").html("<span>起投金额：</span><b>"+numDiv((fundInfo.money||0),10000)+"</b><i>万</i>");
                                $("#saleBar").html("<em style='width:"+numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100)+"%'></em>");
                                $("#saleNo").html(numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100).toFixed(0)+"<i>%</i>");
                            }else{
                            	if(parseInt(fundInfo.profit) == fundInfo.profit && parseInt(fundInfo.profit) == 0){
                            		$("#profit").html("<span>计提基准：</span><em style='font-size: 1.3rem'>浮动收益</em>");
                                }else{
                            		$("#profit").html("<span>计提基准：</span><em>"+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[0]+"</em><b>."+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[1]+"</b><i>%</i>");
                                }
                                $("#term").html("<span>理财期限：</span><b>"+fundInfo.term+"</b><i>"+fundInfo.termUnit+"</i>");
                                $("#scale").html("<span>募集规模：</span><b>"+numDiv((fundInfo.scale||0),10000)+"</b><i>万</i>");
                                $("#money").html("<span>起投金额：</span><b>"+numDiv((fundInfo.money||0),10000)+"</b><i>万</i>");
                                $("#saleBar").html("<em style='width:"+numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100)+"%'></em>");
                                $("#saleNo").html(numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100).toFixed(0)+"<i>%</i>");
                            }
    						var netValTrend = true;
    						if(orderType!=null&&orderType=='1'){/* 未支付 */ 
    							htmls += "<h2><span class='fl notpaid'>待付款</span>";
    							htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
                                htmls += "<div class='order-main-con updateorder-main-con'>";
    							htmls += "<ul>";
    	                        htmls += "<li><span class='fl'>待支付金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span>";
    	                        htmls += "<a href='javascript:redirectUrl(\"/WeixinService/business/query/modifyMoneyNew.shtml?serialno="+dto.serialno+"\")' class='fr'>修改</a></li>";
    	                        htmls += "<li><span class='fl' id='econtract'></span></li>";
    	                        htmls += "</ul>";
    	                        if(daysBetween(today,salesDate) >= 0){
    	                            htmls += "<span class='order-main-point'>请尽快完成汇款，以到账时间锁定产品份额</span>";
    	                        }else if(daysBetween(today,appointDate) >= 0){
    	                        	htmls += "<span class='order-main-point'>请您于募集期(<span style='color:#ca132c;'>"+appPayMoneyText+"</span>)完成汇款</span>";
    	                        }
    	                        if(fundInfo.typeId=='0220'){
    	                        	netValTrend = false;
    	                        }
         					}else if(orderType!=null&&orderType=='2'){/* 已支付 */
         						htmls += "<h2><span class='fl accountpaid'>已付款</span>";
         						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
        						if(fundInfo.typeId == '0400'){
        							htmls += "<div class='order-main-con updateorder-main-con' style='min-height:90px'>";
        						}else{
        							htmls += "<div class='order-main-con updateorder-main-con'>";
        						}
         						htmls += "<ul>";
                                htmls += "<li><span class='fl'>支付金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                                htmls += "<li><span class='fl' id='econtract'></span></li>";
                                htmls += "</ul>";
                                if(fundInfo.typeId != '0400'){
                                	htmls += "<span class='order-main-point'>已收到来款，"+fundInfo.interestDate.substr(0,4)+"年"+fundInfo.interestDate.substr(5,2)+"月"+fundInfo.interestDate.substr(8,2)+"日产品正式起息</span>";
                                }else{
                                	$(".updateorder-main-con").css("min-height","90");
                                }
         					}else if(orderType!=null&&orderType=='3'){/* 排队中 */
         						htmls += "<h2><span class='fl orderdetaillineup'>排队中</span>";
         						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
                                htmls += "<div class='order-main-con updateorder-main-con'>";
         						htmls += "<ul>";
                                htmls += "<li><span class='fl'>待支付金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span>";
                                htmls += "<a href='javascript:redirectUrl(\"/WeixinService/business/query/modifyMoneyNew.shtml?serialno="+dto.serialno+"\")' class='fr'>修改</a></li>";
                                htmls += "<li><span class='fl' id='econtract'></span></li>";
                                htmls += "</ul>";
                                htmls += "<span class='order-main-point'>您的订单已进入排队等候，如有额度释放将第一时间通知您，额度详情请致电400-8878-555。</span>";
         					}else if(orderType!=null&&orderType=='6'){/* 已失效 */
         						htmls += "<h2><span class='fl orderdetailexpired'>已失效</span>";
         						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
                                htmls += "<div class='order-main-con updateorder-main-con'>";
         						htmls += "<ul class='hui'>";
                                if(dto.payst != null && dto.payst == "Y"){
                                    htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                                    htmls += "<li><span class='fl'>计提基准：<em>"+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[0]+"</em><b>."+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[1]+"</b><i>%</i></span></li>";
                                }else{
                                    htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                                }
                                htmls += "<li><span class='fl' id='econtract'></span></li>";
                                htmls += "</ul>";
                                if(dto.payst != null && dto.payst == "Y"){
                                    htmls += "<span class='order-main-point'>买入产品失败，将于5个工作日内返还来款</span>";
                                }else{
                                    htmls += "<span class='order-main-point'>买入产品失败，未收到您的来款</span>";
                                }
         					}else if(orderType!=null&&orderType=='7'){/* 待确认 */
         						htmls += "<h2><span class='fl orderdetailconfirm'>待确认</span>";
         						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
                                htmls += "<div class='order-main-con updateorder-main-con'>";
         						htmls += "<ul>";
                                htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                                htmls += "<li><span class='fl'>计提基准：<em>"+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[0]+"</em><b>."+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[1]+"</b><i>%</i></span></li>";
                                htmls += "<li><span class='fl' id='econtract'></span></li>";
                                htmls += "</ul>";
                                htmls += "<span class='order-main-point'>请尽快确认订单，完成汇款</span>";
         					}else if(orderType!=null&&orderType=='8'){/* 受理中 */
         						
         					}
	            			 if(fundInfo.state == '0' && netValTrend){
	            			// 	queryEstimateByFundId("-1");
	                        	// queryEstimateByFundIdByPage(1);
                                  if("n" == isSubPrdAppraisement.toLowerCase()){
	            			 		 downText(prjLName);
								  }
	            			 }
    					}
    				}else if(typeId != null && typeId == "0110"){
    					
    					/* 固收净值型  */
    					var latestNewValue = fundInfo.latestNewValue;
    					if(latestNewValue != ""){
    						latestNewValue = numMulti((parseFloat(fundInfo.latestNewValue)-dto.confirmNav),100).toFixed(2) + "%";
    					}
    					
    					$("#profit").html("<span>最新净值：</span><em>"+fundInfo.latestNewValue+"</em>");
                        $("#term").html("<span>最高净值：</span><b>"+fundInfo.maxNav+"</b>");
                        $("#scale").html("<span>累积收益率：</span><b>"+latestNewValue+"</b>");
                        $("#money").html("<span>起投金额：</span><b>"+numDiv((fundInfo.money||0),10000)+"</b><i>万</i>");
    					
    					
    					
    					/*if(parseInt(fundInfo.profit) == fundInfo.profit && parseInt(fundInfo.profit) == 0){
                            $("#profit").html("<span>计提基准：</span><em>浮动收益</em>");
                        }else{
                            $("#profit").html("<span>计提基准：</span><em>"+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[0]+"</em><b>."+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[1]+"</b><i>%</i>");
                        }
                        $("#term").html("<span>理财期限：</span><b>"+fundInfo.term+"</b><i>"+fundInfo.termUnit+"</i>");
                        if( orderType!='5' && state=='0' || (orderType =='4')){
                        	$("#scale").html("<span>最新净值：</span><b>"+fundInfo.latestNewValue+"</b><i></i>");
                        }else{
                        	$("#scale").html("<span>募集规模：</span><b>"+numDiv((fundInfo.scale||0),10000)+"</b><i>万</i>");
                        }*/
                        $('#maturityText').html('下一到期日');
                        $('#maturityDateText').html('下一到期日');
                        /*$("#money").html("<span>起投金额：</span><b>"+numDiv((fundInfo.money||0),10000)+"</b><i>万</i>");*/
                        $("#saleBar").html("<em style='width:"+numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100)+"%'></em>");
                        $("#saleNo").html(numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100).toFixed(0)+"<i>%</i>");
                    	var renewUpdate="";
    					if(daysBetween(currentWorkdate, maturityDate)==0){
    						renewUpdate="";
    					}else{
    						renewUpdate='<a  href="javascript:redirectUrl(\'/WeixinService/business/query/modifyPaymentWayNew.shtml?serialno='+dto.serialno+'&renew='+dto.renew+'\')" class="fr">修改</a></li>'
    					}
                        var renewText =  "<em style='text-decoration:underline;' onclick='showTip(\""+dto.renew+"\")'>" ;
                         renewText += dto.renew == 'Y'?"到期自动续投":"到期自动赎回" ;
                         renewText+="</em>"
    					if(orderType!=null&&orderType=='1'){/* 未支付 */ 
    						htmls += "<h2><span class='fl notpaid'>待付款</span>";
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>待支付金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span>";
                            htmls += "<a href='javascript:redirectUrl(\"/WeixinService/business/query/modifyMoneyNew.shtml?serialno="+dto.serialno+"\")' class='fr'>修改</a></li>";
                            htmls += "<li class='paymentway'><span class='fl'>续投方式："+renewText+"</span>";
                            htmls += renewUpdate;
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            if(daysBetween(today,salesDate) >= 0){
                                htmls += "<span class='order-main-point'>请尽快完成汇款，以到账时间锁定产品份额</span>";
                            }else if(daysBetween(today,appointDate) >= 0){
                            	htmls += "<span class='order-main-point'>请您于募集期(<span style='color:#ca132c;'>"+appPayMoneyText+"</span>)完成汇款</span>";
                            }
    					}else if(orderType!=null&&orderType=='2'){/* 已支付 */
    						htmls += "<h2><span class='fl accountpaid'>已付款</span>";
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>支付金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                            htmls += "<li class='paymentway'><span class='fl'>续投方式："+renewText+"</span>";
                            htmls += renewUpdate;
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            htmls += "<span class='order-main-point2'>已收到您的来款，产品将于"+fundInfo.interestDate.substr(0,4)+"年"+fundInfo.interestDate.substr(5,2)+"月"+fundInfo.interestDate.substr(8,2)+"日正式起息；</span>";
    					}else if(orderType!=null&&orderType=='3'){/* 排队中 */
    						htmls += "<h2><span class='fl orderdetaillineup'>排队中</span>";
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>待支付金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span>";
                            htmls += "<a href='javascript:redirectUrl(\"/WeixinService/business/query/modifyMoneyNew.shtml?serialno="+dto.serialno+"\")' class='fr'>修改</a></li>";
                            htmls += "<li class='paymentway'><span class='fl'>续投方式："+renewText+"</span>";
                            htmls += renewUpdate;
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            htmls += "<span class='order-main-point'>您的订单已进入排队等候，如有额度释放将第一时间通知您，额度详情请致电400-8878-555。</span>";
    					}else if(orderType!=null&&orderType=='4'){/* 存续中 */
    						htmls += "<h2><span class='fl surviving'>存续中</span>";
    						$("#schedule").html("<span class='service'>客户服务热线：</span><label class='serviceMobileClass'>400-8878-555</label>");
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(dto.subamt||0)+"</em><i>元</i></span></li>";
                            $("#scale").html("<span>最新净值：</span><b>"+fundInfo.latestNewValue+"</b><i></i>");
                            if(typeId=='0300'){
                            	htmls += "<li><span class='fl'>已分配金额：<em>"+shareAmt+"</em></span></li>";
                            }
                            htmls += "<li class='paymentway'><span class='fl'>续投方式："+renewText+"</span>";
                            htmls += renewUpdate;
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            
                            $("#schedule").html("<span class='service'>最新净值日期为<i></span><label class='serviceMobileClass'>" + formatDate1(fundInfo.lastNavDate)+"</label>");
                            htmls += "<span class='order-main-point'>产品处于存续期，预计"+fundInfo.maturityDate.substr(0,4)+"年"+fundInfo.maturityDate.substr(5,2)+"月"+fundInfo.maturityDate.substr(8,2)+"日到期</span>";
    					}else if(orderType!=null&&orderType=='5'){/* 已到期 */
    						htmls += "<h2><span class='fl hasexpire'>已到期</span>";
    						$("#schedule").html("<span class='service'>客户服务热线：</span><label class='serviceMobileClass'>400-8878-555</label>");
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(dto.subamt||0)+"</em><i>元</i></span></li>";
                            if(typeId=='0300'){
                            	htmls += "<li><span class='fl'>总分配金额：<em>"+shareAmt+"</em></span></li>";
                            }else{
                            	htmls += "<li><span class='fl'>投资收益："+benefit+"</span></li>";
                            }
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            $("#schedule").html("<span class='service'>客户服务热线：</span><label class='serviceMobileClass'>400-8878-555</label>");
                            if(renew=='N'){
                            	if(typeId =='0110') {
                            		var redemptionShare = (!dto.redemptionShare && dto.renew == "N") ? dto.subquty : dto.redemptionShare;
            						redemptionShare = redemptionShare ? redemptionShare : "0";
            						var nextSubquty = parseFloat(dto.subquty) - parseFloat(redemptionShare);
                                    htmls += "<div style='display: inline-block;width: 49%;margin-top: 5px;margin-bottom: 5px;font-size: 14px;text-align: center;'><p style='margin-bottom: 5px;font-size: 14px;color:#999;'>到期赎回</p><p><em style='font-size: 14px;color:#000;'>" +  formatNumber(parseFloat(redemptionShare).toFixed(2)) + "</em><em style='color:#000;'>份</em></p></div>";
                                    htmls += `<p style='border:1px solid #e2e2e2; height:30px;display:inline-block;margin-top: 10px;'></p>`
                                    htmls += "<div style='display: inline-block;width: 50%;margin-top: 5px;margin-bottom: 5px;font-size: 14px;text-align: center;'><p style='margin-bottom: 5px;font-size: 14px;color:#999;'>到期滚存</p><p><em style='font-size: 14px;color:#000;'>" + formatNumber(parseFloat(nextSubquty).toFixed(2)) + "</em><em style='color:#000;'>份</em></p></div>";
                            	}else {
                            		
                            		htmls += "<span class='order-main-point1'>产品已到期，预计本金及收益将于5个工作日内回款至您的银行卡";
                            	}
							}else{
								if(typeId =='0110') {
									var redemptionShare = (!dto.redemptionShare && dto.renew == "N") ? dto.subquty : dto.redemptionShare;
            						redemptionShare = redemptionShare ? redemptionShare : "0";
            						var nextSubquty = parseFloat(dto.subquty) - parseFloat(redemptionShare);
                                	htmls += "<div style='display: inline-block;width: 49%;margin-top: 5px;margin-bottom: 5px;font-size: 14px;text-align: center;'><p style='margin-bottom: 5px;font-size: 14px;color:#999;'>到期赎回</p><p><em style='font-size: 14px;color:#000;'>" +  formatNumber(parseFloat(redemptionShare).toFixed(2)) + "</em><em style='color:#000;'>份</em></p></div>";
                                    htmls += `<p style='border:1px solid #e2e2e2; height:30px;display:inline-block;margin-top: 10px;'></p>`
                                    htmls += "<div style='display: inline-block;width: 50%;margin-top: 5px;margin-bottom: 5px;font-size: 14px;text-align: center;'><p style='margin-bottom: 5px;font-size: 14px;color:#999;'>到期滚存</p><p><em style='font-size: 14px;color:#000;'>" + formatNumber(parseFloat(nextSubquty).toFixed(2)) + "</em><em style='color:#000;'>份</em></p></div>";
                            	}else { 
                            		
                            		htmls += "<span class='order-main-point1'>续投成功，本金与收益已自动续投下一期产品，<a href='/WeixinService/business/query/orderDetailNew.shtml?serialno="+serialno+"&period="+period+"' >查看续投订单";
                            	}
							}
                            htmls+="</span>"
    					}else if(orderType!=null&&orderType=='6'){/* 已失效 */
    						htmls += "<h2><span class='fl orderdetailexpired'>已失效</span>";
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul class='hui'>";
                            if(dto.payst != null && dto.payst == "Y"){
                                htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                                var text= renew== 'Y'?"到期自动续投":"到期自动赎回"
                                htmls+=	"<li class='paymentway'><span class='fl'>续投方式："+text+"</span></li>";
                            }else{
                                htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                            }
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
                            htmls += "</ul>";
                            if(dto.payst != null && dto.payst == "Y"){
                                htmls += "<span class='order-main-point'>买入产品失败，将于5个工作日内返还来款</span>";
                            }else{
                                htmls += "<span class='order-main-point'>买入产品失败，未收到您的来款</span>";
                            }
    					}else if(orderType!=null&&orderType=='7'){/* 待确认 */
    						htmls += "<h2><span class='fl orderdetailconfirm'>待确认</span>";
    						htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    	                    htmls += "<div class='order-main-con updateorder-main-con'>";
    	                    htmls += "<ul>";
                            htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(parseFloat(dto.subamt||0)+parseFloat(dto.fee||0))+"</em><i>元</i></span></li>";
                            var text= renew== 'Y'?"到期自动续投":"到期自动赎回"
                            htmls+=	"<li class='paymentway'><span class='fl'>续投方式："+text+"</span></li>";
                            htmls += "<li><span class='fl' id='econtract'></span></li>";
							htmls +="<li><span class='fl'>计提基准：<em>"+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[0]+"</em><b>."+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[1]+"</b><i>%</i></span></li>";
                            htmls += "</ul>";
                            htmls += "<span class='order-main-point'>请尽快确认订单，完成汇款</span>";
    					}else if(orderType!=null&&orderType=='8'){/* 受理中 */
    						
    					}
    				}else if(typeId =='0500'){  //现金产品
                       	var profit = fundInfo.profit;/* 收益率 */
						var sevenDayAnnualy =fundInfo.sevenDayAnnualy;
                        var latestNewValue=fundInfo.latestNewValue?fundInfo.latestNewValue:"1.0000"
                        	
                        var benefitSinceCreated = fundInfo.benefitSinceCreated ? fundInfo.benefitSinceCreated : '0.00';
						if(fundInfo.state=="0"){ //申购
							if(!sevenDayAnnualy){
				               sevenDayAnnualy = '0.00%';
				            }
						}else{
							sevenDayAnnualy = '--';
						}
                        if(typeId =='0400'){
                            var html = "<li class='totalAmount'><p>总金额(元)</p><p id='totalAmount'></p></li>"+
                                "<li><div id='profit'>--</div><div>万份收益</div></li>"+
                                "<li><div id='annualIncome'>" + sevenDayAnnualy + "</div><div>七日年化收益</div></li>"
                        }else{

                           /* var html = "<li class='totalAmount'><p>总金额(元)</p><p id='totalAmount'></p></li>"+
                                "<li><div id='profit'>--</div><div>万份收益</div></li>"+
                                "<li><div id='annualIncome'>" + latestNewValue + "</div><div>最新净值</div></li>"*/
                        	var html = "<li class='totalAmount'><p>总金额(元)</p><p id='totalAmount'></p></li>"+
                            "<li><div id='latestNewValue'>"+benefitSinceCreated+"</div><div>成立以来年化收益率 </div></li>"+
                            "<li><div id='annualIncome'>" + latestNewValue + "</div><div>最新净值</div></li>"
                        }

                        $(".schedule").remove()
						$(".parameter ul").html(html);
						$(".parameter").addClass("fixedProduct")
						$(".parameter li").eq(1).addClass("newValue")
						$(".parameter li").eq(2).addClass("newValue2")
						var flag=queryUserWarehouse(dto.tradeacco,fundInfo.fundId);
						queryProfitByFundCode(fundInfo.fundId);
						if(!flag){ //该用户没有持仓
                            if(typeId =='0400'){
                                $("#totalAmount").html(sevenDayAnnualy)
                                $("#totalAmount").siblings("p").html("七日年化");
							}else{
                               /* $("#totalAmount").html(latestNewValue)
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
							
							if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
								$("#latestNewValue").html("--")
							} else {
								$("#latestNewValue").html(benefitSinceCreated + "<i>%</i>")
									
							}
							$("#latestNewValue").siblings("div").html("成立以来年化收益率");
							
						}
						
						$(".step-bg").remove();
						$(".surplus").hide();
						$(".redeemGo").show();
						$(".tips").show()
					}
    				htmls += "</div>";
					/*针对0210去除产品期限 */
					if(typeId=="0210"){
                         $("#term").html("");
					}
                    $("#tradeMsg").html(htmls);
					if(typeId=="0500"||typeId=="0400"){
					    $(".order-main-con ul li").eq(1).hide()
					}else if(typeId=="0110"){
						$(".order-main-con ul li").eq(1).show()
					}else{
						$(".order-main-con ul li").eq(1).find("span b").html("浮动收益")
						$(".order-main-con ul li").eq(1).find("span em").remove()
						$(".order-main-con ul li").eq(1).find("span i").remove()
					}
    				/* 产品时间属性相关 */
            		$("#appointDate").html(appointDate);
            		$("#salesDate").html(salesDate);
            		$("#subdeadLine").html(subdeadLine);
            		$("#maturityDate").html(maturityDate);
    				
            		if(daysBetween(today,maturityDate) >= 0){
            			$("#line span:lt(3)").addClass("act");
    					$(".cirle .cirle:lt(3)").addClass("act");
    					$(".cirle .cirle:eq(3)").addClass("current");
    					$(".step-text ul li span:lt(4)").addClass("act");
            		}else if(daysBetween(today,subdeadLine) >= 0){
            			$("#line span:lt(2)").addClass("act");
    					$(".cirle .cirle:lt(2)").addClass("act");
    					$(".cirle .cirle:eq(2)").addClass("current");
    					$(".step-text ul li span:lt(3)").addClass("act");
            		}else if(daysBetween(today,salesDate) >= 0){
            			$("#line span:lt(1)").addClass("act");
    					$(".cirle .cirle:lt(1)").addClass("act");
    					$(".cirle .cirle:eq(1)").addClass("current");
    					$(".step-text ul li span:lt(2)").addClass("act");
            		}else if(daysBetween(today,appointDate) >= 0){
            			$("#line span:lt(0)").addClass("act");
    					$(".cirle .cirle:lt(0)").addClass("act");
    					$(".cirle .cirle:eq(0)").addClass("current");
    					$(".step-text ul li span:lt(1)").addClass("act");
            		}else{
            			$("#line span:lt(0)").addClass("act");
    					$(".cirle .cirle:lt(0)").addClass("act");
    					$(".cirle .cirle:eq(0)").addClass("current");
    					$(".step-text ul li span:lt(1)").addClass("act");
            		}
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
							}  else if (msg.type == 210||msg.type == 230||msg.type == 220) {
								temp3 = true;
							}else {

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
					}
    					$("#goToFAQ").attr("onclick","redirectUrl('/WeixinService/business/query/FAQNew.shtml?fundId="+fundInfo.fundId+"&period="+fundInfo.period+"');");
    					
    					if(dto.orderType != null && (dto.orderType == "1" || dto.orderType == "3" || dto.orderType == "7")){
    						var htmls2 = "";
    						htmls2 += "<div class='buy order-bottom' id='orderBtn'>";
    						htmls2 += "<div class='left fl'>";
    						if(dto.orderType == "1"){/* 待付款 */
    							htmls2 += "<a href='javascript:showOrder(\""+dto.orderType+"\")'>查看汇款信息</a>";
    		    			}else if(dto.orderType == "3"){/* 排队中 */
    		    				htmls2 += "<a href='javascript:showOrder(\""+dto.orderType+"\")'>查看排队信息</a>";
    		    			}else if(dto.orderType == "7"){/* 待确认 */
    		    				htmls2 += "<a href='javascript:comfirmOrder(\""+dto.serialno+"\")'>确认订单</a>";
    		    			}
    						htmls2 += "</div>";
    						htmls2 += "<div class='right fr'>";
    						htmls2 += "<a href='javascript:cancleTips();'>取消订单</a>";
    						htmls2 += "</div>";
    						htmls2 += "</div>";
    						
    						$("body section.page").append(htmls2);
    					}
    					
    					if(typeId =="0110"){ 
    						$(".order-main .order-main-con.updateorder-main-con ul .paymentway").hide();
    					}
    				}
					
            	}else if(data != null && data.returnCode == "9000"){
	        		errorRemark("没有查询到订单");
	        		redirectUrl("/WeixinService/business/query/orderListNew.shtml");
	        		return;
            	}
        	myScroll.refresh();
        	
        }
    });
}
/**
 * 投资项目信息入口
 */
function goToElement(){
	var serialno = $("#serialno").val();
	redirectUrl("/WeixinService/business/query/fundElementNew.shtml?orally=order&period="+$("#period").val()+"&fundId="+$("#fundId").val()+"&serialno="+serialno);
}
function cancleTips(){
	$("#cancelOrderDiv").show();
}
/* 取消订单 */
function cancelAppointRequest(){
	var serialno = $("#serialno").val();
	var tradeAcco = $("#tradeAcco").val();
	
	$.ajax({
    	async:false,
        url: "/WeixinService/business/cancelAppointRequest.xhtml",
        data: {
        	"serialno":serialno,
        	"tradeAcco":tradeAcco
        },
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
			closeTips('cancelOrderDiv');
			errorRemark("取消订单失败");
        }, 
        success : function (data){
			if(data.returnCode != null && data.returnCode == "0000"){
				closeTips('cancelOrderDiv');
				errorRemark("取消订单成功");
				setTimeout('redirectUrl("/WeixinService/business/query/orderListNew.shtml")',2000);
			}else{
				closeTips('cancelOrderDiv');
				errorRemark("取消订单失败");
			}
        }
	});
}
function showOrder(orderType){
	var serialno = $("#serialno").val();
	var subamt = $("#subamt").val();
	var fee = $("#fee").val();
	var bankacco = $("#bankacco").val();
	var realBankName = $("#realBankName").val();
	bankacco = (bankacco||"").substr(bankacco.length-4);
	
	var htmls3 = "";
	htmls3 += "<div class='order-cover'>";
	if(orderType != null && orderType == "1"){
		htmls3 += "<h3>线下汇款信息</h3>";
	}else if(orderType != null && orderType == "3"){
		htmls3 += "<h3>排队信息</h3>";
		htmls3 += "<span class='point'>尚未获得产品份额，请勿汇款!</span>";
	}
	
	htmls3 += "<div class='order-cover-con'>";
	htmls3 += "<ul>";
	htmls3 += "<li><span class='fl'>付款账户：<em>"+realBankName+"(尾号"+bankacco+")</em></span>";
	htmls3 += "<a href='javascript:redirectUrl(\"/WeixinService/business/query/modifyBankcardNew.shtml?serialno="+serialno+"\")' class='fr'>更换</a></li>";
	htmls3 += "<li><span class='fl'>付款金额：<em class='money'>"+formatNumber(parseFloat(subamt||0)+parseFloat(fee||0))+"</em><i class='money-text'>元</i></span>";
	htmls3 += "<a href='javascript:redirectUrl(\"/WeixinService/business/query/modifyMoneyNew.shtml?serialno="+serialno+"\")' class='fr'>修改</a></li>";
	htmls3 += "<li><span class='fl'>收款银行：<em>招商银行总行营业部</em></span></li>";
	htmls3 += "<li><span class='fl'>收款户名：<em>招商财富资产管理有限公司</em></span></li>";
	htmls3 += "<li><span class='fl'>收款账户：<em>9551 0827 0000 009</em></span></li>";
	htmls3 += "</ul>";
	htmls3 += "</div>";
	htmls3 += "<div class='order-cover-bottom'><a href='javascript:closeTips(\"tips_01\")'>我知道了</a></div>";
	htmls3 += "</div>";
	
	$("#tips_01").html(htmls3).show();
}
function comfirmOrder(serialno){
	redirectUrl("/WeixinService/business/pay/confirmBuyNew.shtml?serialno="+serialno);
}

/* 查询电子合同 */
function queryFundContractById() {
	var fundId = $("#fundId").val();
	var period = $("#period").val();
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
					var fileName = '';
					var str = data.fundContractDto.templateName;
					var repStr = str.substring(10,str.length);
					if (repStr != '' && repStr != 'undefined') {
						fileName = str.replace(repStr,'...');
					}else{
						fileName = str;
					}
					$("#contractVer").val(data.fundContractDto.version);
					htmls = "产品合同：<a class='contact' href='javascript:goToContract(\"/WeixinService/business/query/fundContractNew.shtml?templet=" + fundId + "&period="+period+"\")'>《" + fileName + "》</a><br/>";
				} else {
					$("#contractStatus").val("N");
					/*errorRemark("未找到产品合同");*/
				}
	
				if (htmls != null && htmls != "") {
					$("#econtract").append(htmls);
				}
			} else {
				/*errorRemark("合同加载失败，请稍后再试");*/
			}
	
		}
	});
}
/* 跳转到新版产品合同详情页 */
function goToContract(url) {
	var money = unformat($("#subamt").val());
	window.location.href = url + "&money=" + money;
}

$(".product-date span").click( function () { $(".product-date").hide(); });

function dateAlert(title,content){
	if ($('#typeId').val() =='0110' && title=='到期日期') {
		$("#dateTitle").html('到期日期');
		$("#dateContent").html('当日产品到期，系统自动续投或赎回本息至银行卡。');
		$(".product-date").show();
	}else{
		$("#dateTitle").html(title);
		$("#dateContent").html(content);
		$(".product-date").show();
	}
}
/**
 * 历史净值页面入口
 */
function goToOldNet(){
	redirectUrl("/WeixinService/business/query/oldNetListNew.shtml?fundId="+$("#fundId").val()+"&isSub="+$("#isSubPrdAppraisement").val()+"&outerId="+$("#outerId").val());
}
/*查询产品净值(曲线图)*/
function queryEstimateByFundId(dateTime){
	$("#highchart").show();
	$(".head .right a").show();
	$(".head .right span").hide();
	var fundId = $("#fundId").val();
	var isSubPrdAppraisement = $("#isSubPrdAppraisement").val();
	var outerId = $("#outerId").val();
	//子产品非估值 查询时为 系列产品净值图
	if("n" == isSubPrdAppraisement.toLowerCase()){
		fundId = outerId;
	}
	
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
	$.ajax({
		async:true,
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
						temp = "<i>+"+numMulti(numDiv((newNetVal-oldNetVal),oldNetVal),100).toFixed(2)+"%</i>";
					}
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
	$("#fundTypeInfoJinzhi #newNet").html(netArray[netArray.length-1].split(".")[0]+"."+netArray[netArray.length-1].split(".")[1]);
	$("#fundTypeInfoJinzhi #sumNet").html("<em>"+numMulti((parseFloat(netArray[netArray.length-1])-1),100).toFixed(2)+"</em>%");
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
function queryEstimateByFundIdByPage(pages){
	var fundId = $("#fundId").val();
	var pageInput = $("#page").val();
	var page = returnPage(pages);
	var isSubPrdAppraisement = $("#isSubPrdAppraisement").val();
	//var outerId = $("#outerId").val();
	//子产品非估值 查询时为 系列产品净值图
	// if("n" == isSubPrdAppraisement.toLowerCase()){
	// 	fundId = outerId;
	// }
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
/*修改续投方式弹框*/
function showRenew(renew){
	$("#renewDiv").show();
	$('#renewDiv li').removeClass("act");
	$('#renew'+renew).addClass("act");
}

function showTip(renew){
	var text="";
	if (renew == 'Y') 
		text="产品到期后，本金及收益自动续投下一期产品"
	else
		text="产品到期后，本金及收益自动赎回至银行卡"
	errorRemark(text);
}
function closeRenew(){
	$("#renewDiv").hide();
}


/* 修改订单金额 */
function modifyAppointRequest() {
	var serialno = $("#serialno").val();
	var tradeAcco = $("#tradeAcco").val();
	var renew=$('#renewTips input[id="renew"]:checked ').val();
	$.ajax({
		async : false,
		url : "/AppService/business/modifyAppointRequest.xhtml",
		data : {
			"serialno" : serialno,
			"tradeAcco" : tradeAcco,
			"renew":renew
		},
		dataType : "json",
		cache : false,
		type : "POST",
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				$("#renewTips").hide();
				show_tips("修改成功");
				setTimeout("window.location.reload();", 1000);
			} else if (data.returnCode == "USR-1I01") {
				show_tips("错误次数过多3小时后重试");
				return;
			} else {
				show_tips(data.returnMsg);
				return;
			}
		}
	});
}

function downText(prjLName){
	$('#text1').html("注:该净值为《"+prjLName+"》份额净值");
	$('#text1').show();
	$('#highchart1').css('margin','0 auto 4px');
}


function closeCanelBindDiv(){
	$("#updateFundDiv").hide();
	var renew=$(".fund7DayPlan").attr("renew");
	if(renew=="Y"){
		$(".content-bank li").eq(0).addClass("act").siblings().removeClass("act")
	}else{
		$(".content-bank li").eq(1).addClass("act").siblings().removeClass("act")
	}
}
changeRenew()
function changeRenew(){
	$(".content-bank li").click(function () {
		if(!$(this).hasClass("act")){
			var renew=$(this).attr("data-renew");
			$(this).addClass("act").siblings().removeClass("act")
			if(renew=="N"){
				$("#sourceTypeText").html("“赎回到银行卡”")
			}else{
				$("#sourceTypeText").html("“滚存入下一期”")
			}
			var mdate = $(".fund7DayPlan").attr("mdate");
			if(mdate == null || mdate == '') {
				$('#updateFundDiv .warning .warning-con .dateText').remove();
			} else {
				$("#maturityDateText").html($(".fund7DayPlan").attr("mdate")+"<br>")
			}
			$("#updateFundDiv").show();
			$("#updateFundDiv").attr("renew",renew)
		}
	})
}
function sureUpdateFund(){
	var serialno = $("#serialno").val();
	var tradeAcco = $("#tradeAcco").val();
	var renew=$("#updateFundDiv").attr("renew");
	$.ajax({
		async : false,
		url : "/WeixinService/business/updateDistributionType.xhtml",
		data : {
			"serialno" : serialno,
			"tradeAcco" : tradeAcco,
			"flag":renew
		},
		dataType : "json",
		cache : false,
		type : "POST",
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				setTimeout("window.location.reload();", 500);
			} else if (data.returnCode == "USR-1I01") {
				$("#renewDiv").hide();
				errorRemark("错误次数过多3小时后重试");
			} else if (data.returnCode == "USR-1I01") {
				$("#renewDiv").hide();
				errorRemark(data.returnMsg);
			}
			else {
				$("#renewDiv").hide();
				errorRemark(data.returnMsg);
			}

		}
	});
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
		    		var totalAmount=formatNumber(fundinfo.balance, ',');
		    		$("#totalAmount").html(totalAmount);
		    		$(".redeemGo a").attr("href","/WeixinService/business/query/userRedeemNew.shtml?fundId="+fundId+"&period="+period);
		    		$(".redeemGo a").addClass("redeemGoStatus");
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