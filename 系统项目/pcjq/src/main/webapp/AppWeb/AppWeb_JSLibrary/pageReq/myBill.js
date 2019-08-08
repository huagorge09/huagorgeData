$(document).ready(function(e) {
	document.title = "我的对账单_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	queryFundTradeInfo();
	getUserRequest("pc_applicationGroups_myBill_01");
});
/* 全部/未支付/已支付/存续/已到期 互相切换 */
function showTradeInfo(index){
	var num = index+1;
	getUserRequest("pc_applicationGroups_myBill_0"+num);
	$(".myorder-tab-nav ul li").removeClass("act");
	$(".myorder-tab-nav ul li:eq("+index+")").addClass("act");

	$(".myorder-tab-info ul").hide();
	$(".myorder-tab-info ul:eq("+index+")").show();
}
/* 查询订单列表 */
function queryFundTradeInfo(){
	$.ajax({
    	async:true,
        url: "/AppService/business/queryFundTradeInfo.xhtml",
        data: {},
        dataType: "json",
        cache: false,
        type:"post",
        error : function(textStatus, errorThrown) {
        },
        success : function (data){
        	debugger;
        	var htmls1 = "";/* 全部 */
        	var htmls2 = "";/* 买入 sbusinflag == 50 02申购买入 */
        	var htmls3 = "";/* 分配 sbusinflag == 74 */
        	var htmls4 = "";/* 赎回 sbusinflag == 53 */
        	var list;
        	if(data.returnCode != null && data.returnCode == "0000"){
        		list = data.list;
        	}
        	if(list != null && list.length > 0){
	        	$.each(list,function(i, item){
                    if (item.sbusinflag != null &&(item.sbusinflag == "74" || item.sbusinflag =="53" 
                    	|| item.sbusinflag == "01"||item.sbusinflag == "02"||item.sbusinflag == "03" || item.sbusinflag =="71" 
                    		|| item.sbusinflag =="13" || item.sbusinflag =="16" || item.sbusinflag =="05" || item.sbusinflag =="06" 
                    			|| item.sbusinflag =="52"|| item.sbusinflag =="70")) {
		        		/**************************全部*************************************/
	        			var typeflag="";
	        			if(item.sbusinflag == "02"||item.sbusinflag == "01"){
	        				typeflag = "买入";
						}else if(item.sbusinflag =="74"){
							typeflag = "收益分配";
						}else if(item.sbusinflag == "03"){
							typeflag = "赎回";
						}else if(item.sbusinflag == "53"){
							typeflag = "强赎";
						}else if(item.sbusinflag == "70"){
							typeflag = "强制调增";
						}else if(item.sbusinflag == "71"){
							typeflag = "强制调减";
						}else if(item.sbusinflag == "13"){
							typeflag = "产品转换出";
						}else if(item.sbusinflag == "16"){
							typeflag = "产品转换入";
						}else if(item.sbusinflag == "05"){
							typeflag = "托管转入";
						}else if(item.sbusinflag == "06"){
							typeflag = "托管转出";
						}else if(item.sbusinflag == "52"){
							typeflag = "产品清盘";
						}
	        			
	        			htmls1 +="<li class='even'>";
	        			htmls1 +="<span class='name'>"+item.sfundname+"</span>";
	        			htmls1 +="<span class='time'><em>"+item.dcdate.substr(0,10)+"</em></span>";
	        			htmls1 +="<span class='style'>"+typeflag+"</span>";
	        			htmls1 +="<span class='money'>"+formatNumber(item.fconfirmbalance,",")+"</span></li>";
		        		/************************买入***************************************/
						if(item.sbusinflag == "02"|| item.sbusinflag == "01"){
							htmls2 +="<li class='even'>";
							htmls2 +="<span class='name'>"+item.sfundname+"</span>";
							htmls2 +="<span class='time'><em>"+item.dcdate.substr(0,10)+"</em></span>";
							htmls2 +="<span class='style'>"+typeflag+"</span>";
							htmls2 +="<span class='money'>"+formatNumber(item.fconfirmbalance,",")+"</span></li>";
						}
		        		/*************************分配**************************************/
						if(item.sbusinflag == "74" || item.sbusinflag == "70"
							|| item.sbusinflag == "71" || item.sbusinflag == "13" || item.sbusinflag == "16"
								|| item.sbusinflag == "05"|| item.sbusinflag == "06"){
							htmls3 +="<li class='even'>";
							htmls3 +="<span class='name'>"+item.sfundname+"</span>";
							htmls3 +="<span class='time'><em>"+item.dcdate.substr(0,10)+"</em></span>";
		        			htmls3 +="<span class='style'>"+typeflag+"</span>";
		        			htmls3 +="<span class='money'>"+formatNumber(item.fconfirmbalance,",")+"</span></li>";
						}
		        		/************************赎回***************************************/
		        		if(item.sbusinflag=="53"||item.sbusinflag=="03" ||item.sbusinflag=="52"){
		        			htmls4 +="<li class='even'>";
		        			htmls4 +="<span class='name'>"+item.sfundname+"</span>";
		        			htmls4 +="<span class='time'><em>"+item.dcdate.substr(0,10)+"</em></span>";
		        			htmls4 +="<span class='style'>"+typeflag+"</span>";
		        			htmls4 +="<span class='money'>"+formatNumber(item.fconfirmbalance,",")+"</span></li>";
						}
	        		}
				});
        	}
        	$("#bill_01").append(htmls1);
        	$("#bill_02").append(htmls2);
        	$("#bill_03").append(htmls3);
        	$("#bill_04").append(htmls4);
        }
    });
}