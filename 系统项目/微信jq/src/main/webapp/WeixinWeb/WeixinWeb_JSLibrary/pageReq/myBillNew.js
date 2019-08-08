$(document).ready(function(e) {
	$(".header .top-a h2").html("交易明细");
	document.title="交易明细";
	myScroll = new IScroll('#wrapper', {
		scrollbars: true,
		mouseWheel: true,
		interactiveScrollbars: true,
		shrinkScrollbars: 'scale',
		fadeScrollbars: true
	});

	var item = getUrlParameter('item');
	if(item!=""){
		item = item.replace('#', "");
		showTradeInfo(item);
	}
	queryFundTradeInfo();
	getUserRequest("my-bill");/* 此处subPath为页面内行为 */
});
document.addEventListener('touchmove', function(e) {e.preventDefault();}, false);
/* 全部/未支付/已支付/存续/已到期 互相切换 */
function showTradeInfo(index){

	$(".center-mynav-a ul li a").removeClass("act");
	$(".center-mynav-a ul li:eq("+index+") a").addClass("act");

	$("section section").hide();
	$("section section:eq("+index+")").show();

	myScroll.refresh();
}
/* 查询订单列表 */
function queryFundTradeInfo(){
	$.ajax({
		async:true,
		url: "/WeixinService/business/queryFundTradeInfo.xhtml",
		data: {},
		dataType: "json",
		cache: false,
		type:"post",
		error : function(textStatus, errorThrown) {
		},
		success : function (data){
			var htmls1 = "";/* 全部 */
			var htmls2 = "";/* 买入 sbusinflag == 50 */
			var htmls3 = "";/* 分配 sbusinflag == 74 */
			var htmls4 = "";/* 赎回 sbusinflag == 53 */
			var list;
			if(data.returnCode != null && data.returnCode == "0000"){
				list = data.list;
			}
			var fconfirmbalance = 0;
			if(list != null && list.length > 0){
				$.each(list,function(i, item){
					console.log(item);
	                   if (item.sbusinflag != null &&(item.sbusinflag == "74" || item.sbusinflag =="53" 
	                    	|| item.sbusinflag == "01"||item.sbusinflag == "02"||item.sbusinflag == "03" || item.sbusinflag =="71" 
	                    		|| item.sbusinflag =="13" || item.sbusinflag =="16" || item.sbusinflag =="05" || item.sbusinflag =="06" 
	                    			|| item.sbusinflag =="52"|| item.sbusinflag =="70")){
						fconfirmbalance = parseFloat(numDiv(item.fconfirmbalance,10000));
						if(parseInt(fconfirmbalance) == fconfirmbalance){
							fconfirmbalance = fconfirmbalance;
						}else{
							fconfirmbalance = fconfirmbalance.toFixed(2)
						}

						/**************************全部*************************************/
						htmls1 += "<div class='box-tranrecord clear'>";
						htmls1 += "<ul>";
						htmls1 += "<li>";
						var typeflag="";
						if(item.sbusinflag=="02"||item.sbusinflag=="01"){
							htmls1 += "<span class='fl'>买入</span>";
							typeflag = "买入";
						}else if(item.sbusinflag=="74"){
							htmls1 += "<span class='fl'>收益分配</span>";
							typeflag = "收益分配";
						}else if(item.sbusinflag=="03"){
							htmls1 += "<span class='fl'>赎回</span>";
							typeflag = "赎回";
						}else if(item.sbusinflag == "53"){
							htmls1 += "<span class='fl'>强赎</span>";
							typeflag = "强赎";
						}else if(item.sbusinflag == "70"){
							htmls1 += "<span class='fl'>强制调增</span>";
							typeflag = "强制调增";
						}else if(item.sbusinflag == "71"){
							htmls1 += "<span class='fl'>强制调减</span>";
							typeflag = "强制调减";
						}else if(item.sbusinflag == "13"){
							htmls1 += "<span class='fl'>产品转换出</span>";
							typeflag = "产品转换出";
						}else if(item.sbusinflag == "16"){
							htmls1 += "<span class='fl'>产品转换入</span>";
							typeflag = "产品转换入";
						}else if(item.sbusinflag == "05"){
							htmls1 += "<span class='fl'>托管转入</span>";
							typeflag = "托管转入";
						}else if(item.sbusinflag == "06"){
							htmls1 += "<span class='fl'>托管转出</span>";
							typeflag = "托管转出";
						}else if(item.sbusinflag == "52"){
							htmls1 += "<span class='fl'>产品清盘</span>";
							typeflag = "产品清盘";
						}
						htmls1 += "<span class='fr'>"+formatNumber(fconfirmbalance,",")+"万</span>";
						htmls1 += "</li>";
						htmls1 += "<li>";
						htmls1 += "<span class='fl'>"+item.sfundname+"</span>";
						htmls1 += "</li>";
						htmls1 += "<li>";
						htmls1 += "<span class='fl'><b>销售机构:"+item.sagencyname+"</b>"+item.dcdate.substr(0,10)+"</span>";
						htmls1 += "<span class='fr'>成功</span>";
						htmls1 += "</li>";
						htmls1 += "</ul>";
						htmls1 += "</div>";
						/************************买入***************************************/
						if(item.sbusinflag=="02"|| item.sbusinflag=="01"){
							htmls2 += "<div class='box-tranrecord clear'>";
							htmls2 += "<ul>";
							htmls2 += "<li>";
							htmls2 += "<span class='fl'>"+typeflag+"</span>";
							htmls2 += "<span class='fr'>"+formatNumber(fconfirmbalance,",")+"万</span>";
							htmls2 += "</li>";
							htmls2 += "<li>";
							htmls2 += "<span class='fl'>"+item.sfundname+"</span>";
							htmls2 += "</li>";
							htmls2 += "<li>";
							htmls2 += "<span class='fl'><b>销售机构:"+item.sagencyname+"</b>"+item.dcdate.substr(0,10)+"</span>";
							htmls2 += "<span class='fr'>成功</span>";
							htmls2 += "</li>";
							htmls2 += "</ul>";
							htmls2 += "</div>";
						}
						/*************************分配**************************************/
						if(item.sbusinflag == "74" || item.sbusinflag == "70"
							|| item.sbusinflag == "71" || item.sbusinflag == "13" || item.sbusinflag == "16"
								|| item.sbusinflag == "05"|| item.sbusinflag == "06"){
							htmls3 += "<div class='box-tranrecord clear'>";
							htmls3 += "<ul>";
							htmls3 += "<li>";
							htmls3 += "<span class='fl'>"+typeflag+"</span>";
							htmls3 += "<span class='fr'>"+formatNumber(fconfirmbalance,",")+"万</span>";
							htmls3 += "</li>";
							htmls3 += "<li>";
							htmls3 += "<span class='fl'>"+item.sfundname+"</span>";
							htmls3 += "</li>";
							htmls3 += "<li>";
							htmls3 += "<span class='fl'><b>销售机构:"+item.sagencyname+"</b>"+item.dcdate.substr(0,10)+"</span>";
							htmls3 += "<span class='fr'>成功</span>";
							htmls3 += "</li>";
							htmls3 += "</ul>";
							htmls3 += "</div>";
						}
						/************************赎回***************************************/
						if(item.sbusinflag=="53"||item.sbusinflag=="03" ||item.sbusinflag=="52"){
							htmls4 += "<div class='box-tranrecord clear'>";
							htmls4 += "<ul>";
							htmls4 += "<li>";
							htmls4 += "<span class='fl'>"+typeflag+"</span>";
							htmls4 += "<span class='fr'>"+formatNumber(fconfirmbalance,",")+"万</span>";
							htmls4 += "</li>";
							htmls4 += "<li>";
							htmls4 += "<span class='fl'>"+item.sfundname+"</span>";
							htmls4 += "</li>";
							htmls4 += "<li>";
							htmls4 += "<span class='fl'><b>销售机构:"+item.sagencyname+"</b>"+item.dcdate.substr(0,10)+"</span>";
							htmls4 += "<span class='fr'>成功</span>";
							htmls4 += "</li>";
							htmls4 += "</ul>";
							htmls4 += "</div>";
						}
					}
				});
			}else{
				htmls1+="<div class='bulletin'>";
				htmls1+="<img class='bulletin-icon' src='/WeixinWeb/WeixinWeb_Images/images/none-gray.png' />";
				htmls1+="<span>暂无数据,快去下单吧...</span>";
				htmls1+="</div>";
				htmls2+="<div class='bulletin'>";
				htmls2+="<img class='bulletin-icon' src='/WeixinWeb/WeixinWeb_Images/images/none-gray.png' />";
				htmls2+="<span>暂无数据,快去下单吧...</span>";
				htmls2+="</div>";
				htmls3+="<div class='bulletin'>";
				htmls3+="<img class='bulletin-icon' src='/WeixinWeb/WeixinWeb_Images/images/none-gray.png' />";
				htmls3+="<span>暂无数据,快去下单吧...</span>";
				htmls3+="</div>";
				htmls4+="<div class='bulletin'>";
				htmls4+="<img class='bulletin-icon' src='/WeixinWeb/WeixinWeb_Images/images/none-gray.png' />";
				htmls4+="<span>暂无数据,快去下单吧...</span>";
				htmls4+="</div>";
			}
			if(htmls1==''){
				htmls1+="<div class='bulletin'>";
				htmls1+="<img class='bulletin-icon' src='/WeixinWeb/WeixinWeb_Images/images/none-gray.png' />";
				htmls1+="<span>暂无数据,快去下单吧...</span>";
				htmls1+="</div>";
			}
			if(htmls2==''){
				htmls2+="<div class='bulletin'>";
				htmls2+="<img class='bulletin-icon' src='/WeixinWeb/WeixinWeb_Images/images/none-gray.png' />";
				htmls2+="<span>暂无数据,快去下单吧...</span>";
				htmls2+="</div>";
			}
			if(htmls3==''){
				htmls3+="<div class='bulletin'>";
				htmls3+="<img class='bulletin-icon' src='/WeixinWeb/WeixinWeb_Images/images/none-gray.png' />";
				htmls3+="<span>暂无数据,快去下单吧...</span>";
				htmls3+="</div>";
			}
			if(htmls4==''){
				htmls4+="<div class='bulletin'>";
				htmls4+="<img class='bulletin-icon' src='/WeixinWeb/WeixinWeb_Images/images/none-gray.png' />";
				htmls4+="<span>暂无数据,快去下单吧...</span>";
				htmls4+="</div>";
			}
			$("#section_01").html(htmls1);
			$("#section_02").html(htmls2);
			$("#section_03").html(htmls3);
			$("#section_04").html(htmls4);
			myScroll.refresh();
		}
	});
}