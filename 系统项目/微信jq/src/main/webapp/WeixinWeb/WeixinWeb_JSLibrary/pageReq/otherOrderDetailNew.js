﻿var myScroll;
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
	var fundId = getUrlParameter("fundId");
	var channleNo = getUrlParameter("channleNo");
	serialno = removeSpecialStr(serialno);
	fundId = removeSpecialStr(fundId);
	if(serialno == null || serialno == ""){
		/*errorRemark("没有查询到订单");
		redirectUrl("/WeixinService/business/query/orderListNew.shtml");
		return;*/
	}
	$.ajax({
    	async:false,
        url: "/WeixinService/business/queryOtherDetailOrder.xhtml",
        data: {
        	"serialno":serialno,
        	"fundId":fundId,
        	"channleNo":channleNo
        },
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        }, 
        success : function (data){
        		/* 如果订单信息都为空的话，当前订单可能已经被取消了，返回到列表页面 */
        		if(data.agentFundDto == null){
            		/*errorRemark("当前订单信息有误");
            		redirectUrl("/WeixinService/business/query/fundListNew.shtml");
            		return;*/
            	}else{
            		var dto = data.agentFundDto;
            		var fundInfo = data.fundInfoDtoV2;
            		var htmls = "";
            		/* 隐藏input赋值 */
            		$("#serialno").val(dto.serialno);
            		$("#fundId").val(fundId);
            		$("#period").val(fundInfo.period);
            		$("#subamt").val(dto.bugAmt);
            		$("#fee").val(dto.fee||0);
            		$("#tradeAcco").val(dto.tradeacco);
            		$("#isSubPrdAppraisement").val(fundInfo.isSubPrdAppraisement);
            		$("#outerId").val(fundInfo.outerId);
            		
            		if (typeof (fundInfo.templetId) != undefined) {
    					$("#templetId").val(fundInfo.templetId);
    				}
            		
            		var appointDate = fundInfo.appointDate;/* 预约开始日期 */
    				var appointEndDate = fundInfo.appointEndDate;/* 预约结束日期 */
    				var salesDate = fundInfo.salesDate;/* 发售日 */
    				var subdeadLine = fundInfo.subdeadLine;/* 认购截止 */
    				var interestDate = fundInfo.interestDate;/* 起息日期 */
    				var maturityDate = fundInfo.maturityDate;/* 到期日期 */
    				var paymentDate = fundInfo.paymentDate;/* 产品到期清盘，预计打款日期 */
    				var today = fundInfo.currentWorkdate;/* 当前工作日 */
    				var isSubPrdAppraisement = fundInfo.isSubPrdAppraisement;
    				var prjLName = fundInfo.prjLName;
					if(fundInfo.latestNewValue!=""){
						queryEstimateByFundId("-1");
					}
    				/* 产品基本信息 */
            		$("#adName").html(dto.fundnm);
            		if(fundInfo.typeId=='0110'||fundInfo.typeId=='0210'||fundInfo.typeId=='0220'){
                		$("#profit").html("<span>最新净值：</span><em>"+fundInfo.latestNewValue+"</em>");
                		$("#term").html("<span>最高净值：</span><b>"+fundInfo.maxNav+"</b>");
                		$("#scale").html("<span>累积收益率：</span><b>"+numMulti((parseFloat(fundInfo.latestNewValue)-dto.confirmNav),100).toFixed(2)+"%</b>");
                		$("#money").html("<span>起投金额：</span><b>"+numDiv((fundInfo.money||0),10000)+"</b><i>万</i>");
                		$("#saleBar").html("<em style='width:"+numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100)+"%'></em>");
                		$("#saleNo").html(numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100).toFixed(0)+"<i>%</i>");
            		}else{
            			if(parseInt(fundInfo.profit) == fundInfo.profit && parseInt(fundInfo.profit) == 0){
                			$("#profit").html("<span>业绩报酬计提基准：</span><em>浮动收益</em>");
                		}else{
                			$("#profit").html("<span>业绩报酬计提基准：</span><em>"+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[0]+"</em><b>."+parseFloat(numMulti((fundInfo.profit||0),100)).toFixed(2).split('.')[1]+"</b><i>%</i>");
                		}
                		$("#term").html("<span>理财期限：</span><b>"+fundInfo.term+"</b><i>"+fundInfo.termUnit+"</i>");
                		$("#scale").html("<span>募集规模：</span><b>"+numDiv((fundInfo.scale||0),10000)+"</b><i>万</i>");
                		$("#money").html("<span>起投金额：</span><b>"+numDiv((fundInfo.money||0),10000)+"</b><i>万</i>");
                		$("#saleBar").html("<em style='width:"+numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100)+"%'></em>");
                		$("#saleNo").html(numMulti(numDiv(((fundInfo.scale||0)-(fundInfo.displayLimit||0)),(fundInfo.scale||0)),100).toFixed(0)+"<i>%</i>");
            		}
            		if(dto.applySt!= null && dto.applySt == "G"){/* 存续中 */
            			htmls += "<h2><span class='fl surviving'>存续中</span>";
            		}else if(dto.applySt != null && dto.applySt == "Z"){/* 已到期 */
            			htmls += "<h2><span class='fl hasexpire'>已到期</span>";
            		}
            		
    				htmls += "<span class='fr'><em>编号：</em><i>"+dto.serialno+"</i></span></h2>";
    				htmls += "<div class='order-main-con updateorder-main-con'>";
    				if(dto.applySt!= null && dto.applySt == "G"){/* 存续中 */
    					htmls += "<ul>";
        				htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(parseFloat(dto.bugAmt||0))+"</em><i>元</i></span></li>";
    					if (dto.nav != null ||dto.nav != '' ||typeof(dto.nav) == 'undefined'){
    						if(dto.benefit == 0){
    							htmls += "<li><span class='fl'>持仓盈亏：<em>浮动收益</em></span></li>";
    						}else{
    							htmls += "<li><span class='fl'>持仓盈亏：<em>"+format(parseFloat(dto.profit||0))+"</em><i>元</i></span></li>";
    						}
    					}else{
    						if(dto.benefit == 0){
    							htmls += "<li><span class='fl'>业绩报酬计提基准：<em>浮动收益</em></span></li>";
    						}else{
    							htmls += "<li><span class='fl'>业绩报酬计提基准：<em>"+format(parseFloat(dto.profit||0))+"</em><i>元</i></span></li>";
    						}
    					}
            		}else if(dto.applySt != null && dto.applySt == "Z"){/* 已到期 */
            			htmls += "<ul>";
        				htmls += "<li><span class='fl'>买入金额：<em>"+formatNumber(parseFloat(dto.bugAmt||0))+"</em><i>元</i></span></li>";
    					if (dto.nav != null ||dto.nav != '' ||typeof(dto.nav) == 'undefined'){
    						htmls += "<li><span class='fl'>持仓盈亏：<em>"+format(parseFloat(dto.profit||0))+"</em><i>元</i></span></li>";
    					}else{
    						htmls += "<li><span class='fl'>产品收益：<em>"+format(parseFloat(dto.profit||0))+"</em><i>元</i></span></li>";
    					}
            		}
    				htmls += "<li><span class='fl' id='econtract'></span></li>";
    				htmls += "</ul>";
    				if(dto.applySt!= null && dto.applySt == "G"){/* 存续中 */
    					htmls += "<span class='order-main-point'>产品处于存续期，预计"+fundInfo.maturityDate.substr(0,4)+"年"+fundInfo.maturityDate.substr(5,2)+"月"+fundInfo.maturityDate.substr(8,2)+"日到期</span>";
            		}else if(dto.applySt != null && dto.applySt == "Z"){/* 已到期 */
            			htmls += "<span class='order-main-point'>产品已到期，预计5个工作日内完成收益分配</span>";
            		}
    				htmls += "</div>";
    				$("#tradeMsg").html(htmls);
    				
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
            		
            		if("n" == isSubPrdAppraisement.toLowerCase()){
            			downText(prjLName);
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
							} else if (msg.type == 210||msg.type == 230||msg.type == 220) {
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
            	}
        	myScroll.refresh();
        }
    });
}
function cancleTips(){
	$("#cancelOrderDiv").show();
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
			"period" : 1
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
					htmls = "产品合同：<a class='contact' href='javascript:goToContract(\"/WeixinService/business/query/fundContractNew.shtml?templet=" + fundId  + "&period="+period+"\")'>《" + data.fundContractDto.templateName + "》</a><br/>";
				} else {
					$("#contractStatus").val("N");
					errorRemark("未找到产品合同");
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
	var money = unformat($("#subamt").val());
	window.location.href = url + "&money=" + money;
}

$(".product-date span").click( function () { $(".product-date").hide(); });

function dateAlert(title,content){
	$("#dateTitle").html(title);
	$("#dateContent").html(content);
	$(".product-date").show();
}
/**
 * 投资项目信息入口
 */
function goToElement(){
	var serialno = getUrlParameter("serialno");
	var channleNo = getUrlParameter("channleNo");
	redirectUrl("/WeixinService/business/query/fundElementNew.shtml?orally=orderOther&period="+$("#period").val()+"&fundId="+$("#fundId").val()+"&serialno="+serialno+"&channleNo="+channleNo);
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
	// var outerId = $("#outerId").val();
	// //子产品非估值 查询时为 系列产品净值图
	// if("n" == isSubPrdAppraisement.toLowerCase()){
	// 	fundId = outerId;
	// }
	
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
				yVarMaxlti=numMulti(yVarMaxlti,1.2)
				var min = (numDiv(yValMinlti,5)*4).toFixed(4);
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
						temp = "<i>"+numMulti(numDiv((newNetVal-oldNetVal),oldNetVal),100).toFixed(2)+"%</i>";
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


function downText(prjLName){
	$('#text1').html("注:该净值为《"+prjLName+"》份额净值");
	$('#text1').show();
	$('#highchart1').css('margin','0 auto 4px');
}

function showText(html){
	$('#text1').html("注:该净值为《"+prjLName+"》份额净值");
	$('#text1').show();
	$('#highchart1').css('margin','0 auto 4px');
}