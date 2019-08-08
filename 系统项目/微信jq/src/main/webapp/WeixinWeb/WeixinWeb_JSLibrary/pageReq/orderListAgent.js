﻿var myScroll;
$(document).ready(function(e) {
	$(".header .top-a h2").html("其他在持产品");
	document.title="其他在持产品";
	myScroll = new IScroll('#wrapper', {
		scrollbars: true,
		mouseWheel: true,
		interactiveScrollbars: true,
		shrinkScrollbars: 'scale',
		fadeScrollbars: true
	});
 	queryAgentFundInfo();
 	getUserRequest("order-list-agent");/* 此处subPath为页面内行为 */
});

document.addEventListener('touchmove', function (e) {e.preventDefault(); }, false);
/* 全部/未支付/已支付/存续/已到期 互相切换 */
function showOrderList(index){
	$(".center-mynav-a ul li a").removeClass("act");
	$(".center-mynav-a ul li:eq("+index+") a").addClass("act");
	$("section section").hide();
	$("section section:eq("+index+")").show();
	getUserRequest("order-list-agent");/* 此处subPath为页面内行为 */
	myScroll.refresh();
}
/* 查询订单列表 */
function queryAgentFundInfo(){
	$.ajax({
    	async:true,
        url: "/WeixinService/business/queryAgentFundInfo.xhtml",
        data: {},
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {  
        }, 
        success : function (data){
        	var htmls1 = "";
        	var htmls2 = "";
        	var htmls3 = "";
        	var list = data.list;
        	var gList = data.gList;
        	var zList = data.zList;
        	var amt = 0;
        	var profit = 0;
        	
        	/* 全部订单 */
        	if(list != null && list.length > 0){
        		$.each(list,function(i, item){
        			if(item.hasDetail=='Y'){
        				htmls1 += "<a href='/WeixinService/business/query/otherOrderDetail.shtml?serialno="+item.serialno+"&fundId="+item.fundid+"&channleNo="+item.channleNo+"'><div class='box-order'>";
        			}else{
        				htmls1 += "<div class='box-order'>";
        			}
					htmls1 += "<h2>";
					htmls1 += "<span class='fl'>"+item.fundnm+"</span>";
					if(item.applySt != null && item.applySt == "G"){
						htmls1 += "<span class='fr surviving'>存续中</span>";
					}else{
						htmls1 += "<span class='fr hasexpired'>已到期</span>";
					}
					htmls1 += "</h2>";
					htmls1 += "<div class='income upadateorder-income'>";
					htmls1 += "<dl>";
					
					amt = numDiv(parseFloat(item.bugAmt+item.fee),10000);
					if(parseInt(amt) != amt){
						amt = parseFloat(amt.toFixed(4));
					}
					profit = parseFloat(item.profit).toFixed(2);
        			
					if(item.applySt != null && item.applySt == "G"){
						htmls1 += "<dt>支付金额</dt>";
						htmls1 += "<dd><b>"+amt+"万</b></dd>";
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>买入日期</dt>";
						htmls1 += "<dd><b>"+item.buyDate+"</b></dd>";
						htmls1 += "</dl>";
						var numlate = new Number(item.nav);
						if (item.nav != null &&item.nav != '' &&typeof(item.nav) != 'undefined' && numlate != 0) {
							htmls1 += "<dl>";
							htmls1 += "<dt>最新净值</dt>";
							htmls1 += "<dd><b>"+numlate.toFixed(4)+"</b></dd>";
							htmls1 += "</dl>";
						}
					}else{
						htmls1 += "<dt>支付金额</dt>";
						htmls1 += "<dd><b>"+amt+"万</b></dd>";
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>产品收益</dt>";
						htmls1 += "<dd><b>"+profit+"元</b></dd>";
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>到期日期</dt>";
						htmls1 += "<dd><b>"+item.cycleenddt+"</b></dd>";
						htmls1 += "</dl>";
					}
					amt = 0;
					htmls1 += "</div>";
					htmls1 += "<div class='box-order-bottom clear'>";
					/*htmls1 += "<span class='fl'></span>";
					htmls1 += "<span class='fr'>2016-05-12</span>";*/
					htmls1 += "</div>";
					htmls1 += "</div>";
					if(item.hasDetail=='Y'){
						htmls1 += "</a>";
					}
        		});
        	}else{
            		htmls1+="<div class='bulletin'>";
            		htmls1+="<img class='bulletin-icon' src='/WeixinWeb/WeixinWeb_Images/images/none-gray.png' />";
            		htmls1+="<span>暂无数据,快去下单吧...</span>";
            		htmls1+="</div>";
        	}
        	
        	/* 存续中 */
        	if(gList != null && gList.length > 0){
        		$.each(gList,function(i, item){
        			amt = numDiv(parseFloat(item.bugAmt+item.fee),10000);
        			if(parseInt(amt) != amt){
        				amt = parseFloat(amt.toFixed(4));
        			}
        			if(item.hasDetail=='Y'){
        				htmls2 += "<a href='/WeixinService/business/query/otherOrderDetail.shtml?serialno="+item.serialno+"&fundId="+item.fundid+"&channleNo="+item.channleNo+"'><div class='box-order'>";
        			}else{
        				htmls2 += "<div class='box-order'>";
        			}
					htmls2 += "<h2>";
					htmls2 += "<span class='fl'>"+item.fundnm+"</span>";
					htmls2 += "<span class='fr surviving'>存续中</span>";
					htmls2 += "</h2>";
					htmls2 += "<div class='income upadateorder-income'>";
					htmls2 += "<dl>";
					htmls2 += "<dt>支付金额</dt>";
					htmls2 += "<dd><b>"+amt+"万</b></dd>";
					htmls2 += "</dl>";
					htmls2 += "<dl>";
					htmls2 += "<dt>买入日期</dt>";
					htmls2 += "<dd><b>"+item.buyDate+"</b></dd>";
					htmls2 += "</dl>";
					var numlate = new Number(item.nav);
					if (item.nav != null &&item.nav != '' &&typeof(item.nav) != 'undefined' && numlate != 0){
						htmls2 += "<dl>";
						htmls2 += "<dt>最新净值</dt>";
						htmls2 += "<dd><b>"+numlate.toFixed(4)+"</b></dd>";
						htmls2 += "</dl>";
					}
					amt = 0;
					htmls2 += "</div>";
					htmls2 += "<div class='box-order-bottom clear'>";
					htmls2 += "</div>";
					htmls2 += "</div>";
					if(item.hasDetail=='Y'){
        				htmls2 += "</a>";
        			}
        		});
        	}else{
        		htmls2+="<div class='bulletin'>";
        		htmls2+="<img class='bulletin-icon' src='/WeixinWeb/WeixinWeb_Images/images/none-gray.png' />";
        		htmls2+="<span>暂无数据,快去下单吧...</span>";
        		htmls2+="</div>";
        	}

        	/* 已到期 */
        	if(zList != null && zList.length > 0){
        		$.each(zList,function(i, item){
        			
        			amt = numDiv(parseFloat(item.bugAmt),10000);
        			if(parseInt(amt) != amt){
        				amt = parseFloat(amt.toFixed(4));
        			}
        			profit = parseFloat(item.profit).toFixed(2);
        			if(item.hasDetail=='Y'){
        				htmls3 += "<a href='/WeixinService/business/query/otherOrderDetail.shtml?serialno="+item.serialno+"&fundId="+item.fundid+"&channleNo="+item.channleNo+"'><div class='box-order'>";
        			}else{
        				htmls3 += "<div class='box-order'>";
        			}
					htmls3 += "<h2>";
					htmls3 += "<span class='fl'>"+item.fundnm+"</span>";
					htmls3 += "<span class='fr hasexpired'>已到期</span>";
					htmls3 += "</h2>";
					htmls3 += "<div class='income upadateorder-income'>";
					htmls3 += "<dl>";
					htmls3 += "<dt>支付金额</dt>";
					htmls3 += "<dd><b>"+amt+"万</b></dd>";
					htmls3 += "</dl>";
					htmls3 += "<dl>";
					htmls3 += "<dt>产品收益</dt>";
					htmls3 += "<dd><b>"+profit+"元</b></dd>";
					htmls3 += "</dl>";
					htmls3 += "<dl>";
					htmls3 += "<dt>到期日期</dt>";
					htmls3 += "<dd><b>"+item.cycleenddt+"</b></dd>";
					htmls3 += "</dl>";
					htmls3 += "</div>";
					htmls3 += "<div class='box-order-bottom clear'>";
					/*htmls3 += "<span class='fl'>待销机构：<em>招商银行</em></span>";
					htmls3 += "<span class='fr'>2016-05-12</span>";*/
					htmls3 += "</div>";
					htmls3 += "</div>";
					if(item.hasDetail=='Y'){
        				htmls3 += "</a>";
        			}
        		});
        	}else{
        		htmls3+="<div class='bulletin'>";
        		htmls3+="<img class='bulletin-icon' src='/WeixinWeb/WeixinWeb_Images/images/none-gray.png' />";
        		htmls3+="<span>暂无数据,快去下单吧...</span>";
        		htmls3+="</div>";
        	}
        	
        	
        	$("#section_01").html(htmls1);
        	$("#section_02").html(htmls2);
        	$("#section_03").html(htmls3);
        	
        	myScroll.refresh();
        }
    });
}