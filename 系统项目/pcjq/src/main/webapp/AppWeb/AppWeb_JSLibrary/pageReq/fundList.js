//当前页面pageId
var pageId = "";
//来源页面ID
var pageSourceId = "";
//事件Id
var eventId = "";

var t_delay;

$(document).ready(function(){
	getUserRequest("pc_fundList");
	queryWenTiByCommon();
	$(".nav.fr ul li a").removeClass("current").eq(1).addClass("current");
	$(".FAQ dl dt,.FAQ dl i.arrow").click(function(){
        $(this).parent("dl").toggleClass("act").siblings().removeClass("act");
    });
//	document.title = "财富产品";
	//当前页面pageId
	pageId = $("#pageId").val();
	//来源页面ID
	pageSourceId = getUrlParameter("pageSourceId");
	if(null == pageSourceId || pageSourceId == ""){
		pageSourceId = pageId;
	}
	eventId = getUrlParameter("eventId");
	if(null == eventId || eventId == ""){
		eventId = "event_wealthProductsId";
	}
	
	var flag = checkIsRiskLevel();
	if(flag){
		queryFundList();
	}

	 
})
/*查询产品列表*/
function queryFundList(){
    var urlVal="/AppService/business/queryFundList.xhtml";
    $.ajax({
    	async:false,
		url:urlVal,
		type:"post",
		dataType:'json',
		data:{
			type:"99"
		},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
		},
		success:function(data, textStatus){
			var temp = "";
			var rate = 0;/* 进度条 计算之后的 */
			var profit = 0;/* 年化收益 计算之后的 */
			var fundInfoDtoList = null;
			//开关
			var seven ='0';
			try{
				seven = queryParamList("SYSTEM","SHOWNETVALUE","")[0].pmco;
			}catch(err){
				console.log("开关查询失败");
			}
			if(data.advertDtoList != null && data.advertDtoList.length > 0){
				var htmls2 = "";
				var count = 0;
				$.each(data.advertDtoList,function(i, item){
					if(item.state != null && item.state == '1'){
						htmls2 += "<a class='promarke-banner-img01' style='background: url(\""+item.picture+"\") no-repeat center;' href='"+item.url+"' target='_blank'><img/></a>";
					}else{
						htmls2 += "<a class='promarke-banner-img01' style='background: url(\""+item.picture+"\") no-repeat center;' href='javascript:void(0)'><img/></a>";
					}
					count++;
				});
				$("#promarke-banner_list").html(htmls2);
				babyzone.scroll(count,"promarke-banner_list","promarke-list","promarke-banner_info");
			}
			if(data.fundInfoDtoList != null && data.fundInfoDtoList.length > 0){
				fundInfoDtoList = data.fundInfoDtoList;
				var htmls = "";
				var currentWorkdate = "";
				var subdeadLine = "";
				var salesDate = "";
				var appointDate = "";
				var appointEndDate="";
				var displayLimit = "";

				$.each(data.fundInfoDtoList,function(i, item){
					temp = fundInfoDtoList[i].groupId;
					var period=item.period;
					/*预约开始日期*/
					appointDate = item.appointDate;
					/*预约开始日期*/
					appointEndDate = item.appointEndDate;
					salesDate = item.salesDate;/*发售日*/
					subdeadLine = item.subdeadLine;/*认购截止*/
					currentWorkdate = item.currentWorkdate;/*当前工作日*/
					
					if(daysBetween(currentWorkdate,subdeadLine) > 0){
						rate = 100;
					}else{
						rate = parseFloat(numMulti(numDiv((item.scale-item.displayLimit),item.scale),100)).toFixed(0);
					}
					profit = parseFloat(numMulti(item.profit,100)).toFixed(2);
					displayLimit = item.displayLimit;
					if(isNaN(profit) || profit == 0){
						profit = "<em style='color:#ca132c;font-size:20px;margin-right:0px;'>浮动收益</em>";
					}else{
						profit = "<em style='color:#ca132c;'>" + profit + "</em>%";
					}
					if(item.latestNewValue == null || item.latestNewValue =='' || item.latestNewValue =='0.0000'){
						item.latestNewValue = '1.0000';
					}
					if(i == 0){
						htmls += "<div class='con01'>";
						htmls += "<span class='con-title'>"+item.groupName+"</span>";
						htmls += "<div class='con01-box box01'>";
						if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
							htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"&period="+period+"' id='"+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
						}else if(daysBetween(currentWorkdate,salesDate) >= 0){/*发售日*/
							if(displayLimit > 0){
								htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
							}else{
								htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"' id='"+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
							}
						}else if(daysBetween(currentWorkdate,appointDate) >= 0){/*预约日*/
							if(displayLimit > 0){/* 预约期*/
								htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
							}else{/* 排队中*/
								htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
							}
						}else{
							htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
						}
						htmls += "<div class='con-info'>";
						htmls += "<dl class='conFirst'>";
	
						//活期理财类展示 七日年化收益、理财起点、递增金额
						if(item.typeId == '0500'){
							/*
							 * 开放性净值类产品申购期
							 * 展示最新净值（保留四位小数）、历史最高净值（保留四位小数）、累计收益率（百分数，保留两位小数）、申赎规则（每日、每周、每月）
							 */
							var latestNewValue = item.latestNewValue ? item.latestNewValue : '1.000';							
							
							/*htmls += "<dt><em style='color:#ca132c;'>"+ latestNewValue +"</em></dt>";
							htmls += "<dd>最新净值123</dd>";*/
							
							var benefitSinceCreated = item.benefitSinceCreated ? item.benefitSinceCreated : '0.00';
							if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
								htmls += "<dt><em style='color:#ca132c;'>--</em></dt>";
							}else { 
								htmls += "<dt><em style='color:#ca132c;'>"+ benefitSinceCreated +"</em>%</dt>";
							}
							htmls += "<dd>成立以来年化收益率</dd>";
							
							htmls += "</dl>";
							htmls += "<dl class='conTd'>";
							htmls += "</dl>";
							htmls += "<dl>";
							htmls += "<dt><em>"+numDiv(item.money,10000)+"</em>万起</dt>";
							htmls += "<dd>理财起点</dd>";
							htmls += "</dl>";
							htmls += "<dl>";
							htmls += "</dl>";
							htmls += "<dl>";
							if(item.sartBuying == null || item.sartBuying == '') {
								htmls += "<dt><em>0</em>元起</dt>";
							} else if (item.sartBuying < 10000) {
								htmls += "<dt><em>"+item.sartBuying+"</em>元起</dt>";
							} else {
								htmls += "<dt><em>"+numDiv(item.sartBuying,10000)+"</em>万起</dt>";
							}
							htmls += "<dd>追加金额</dd>";
							htmls += "</dl>";
							
							//认购期  = 当前时间 大于等于 认购起始日
							if(daysBetween(currentWorkdate,salesDate) >= 0){
								htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>购买</a>";
							}else{
								htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
							}
						}else if(item.typeId == '0400'){
							if(seven == '1'){
								/*
								 * 7天管家类产品
								 * 展示七日年化收益、理财起点、递增金额
								 */
								var newValue = '--';
								if(item.sevenDayAnnualy&&"0.00%"!=item.sevenDayAnnualy){
									newValue =item.sevenDayAnnualy;
		                        }
								if(item.state == '1') {
									newValue = '--';
		                        }
								
								htmls += "<dt><em style='color:#ca132c;'>"+newValue+"</em></dt>";
								htmls += "<dd>七日年化收益</dd>";
							}else{
								/*var latestNewValue ='1.0000';
								if(item.latestNewValue){
									latestNewValue = item.latestNewValue;
								}
								htmls += "<dt><em style='color:#ca132c;'>"+latestNewValue+"</em></dt>";
								htmls += "<dd>最新净值</dd>";*/
								var benefitSinceCreated = item.benefitSinceCreated ? item.benefitSinceCreated : '0.00';
								if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
									htmls += "<dt><em style='color:#ca132c;'>--</em></dt>";
								}else { 
									htmls += "<dt><em style='color:#ca132c;'>"+ benefitSinceCreated +"</em>%</dt>";
								}
								htmls += "<dd>成立以来年化收益率</dd>";
							}
							htmls += "</dl>";
							htmls += "<dl class='conTd'>";
							htmls += "</dl>";
							htmls += "<dl>";
							htmls += "<dt><em>"+numDiv(item.money,10000)+"</em>万起</dt>";
							htmls += "<dd>理财起点</dd>";
							htmls += "</dl>";
							htmls += "<dl>";
							htmls += "</dl>";
							htmls += "<dl>";
							if(item.sartBuying == null || item.sartBuying == '') {
								htmls += "<dt><em>0</em>元起</dt>";
							} else if (item.sartBuying < 10000) {
								htmls += "<dt><em>"+item.sartBuying+"</em>元起</dt>";
							} else {
								htmls += "<dt><em>"+numDiv(item.sartBuying,10000)+"</em>万起</dt>";
							}
							htmls += "<dd>追加金额</dd>";
							htmls += "</dl>";				
							//过了截止日 = 募集结束
							if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
								htmls += "<a class='btn_buy fr act' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>募集结束</a>";
							//认购期= 当前时间 大于等于 认购起始日 并且 小于 认购截止日 
							}else if(daysBetween(currentWorkdate,salesDate) >= 0 && daysBetween(currentWorkdate,subdeadLine) <= 0){
								//额度是否足够
								if(displayLimit > 0){
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>购买</a>";
								}else{
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
								}
							//预约期= (预约开始日 <= 当前时间  <= 预约截止日 )
							}else if(daysBetween(currentWorkdate,appointDate) >= 0 && daysBetween(currentWorkdate,appointEndDate) <= 0){
								//额度是否足够
								if(displayLimit > 0){
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
								}else{
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
								}
							}else{
								htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
							}
						}else{
							if ((item.typeId == '0110' || item.typeId == '0210' || item.typeId == '0220') && item.state=='0') {
								/*
								 * 开放性净值类产品申购期
								 * 展示最新净值（保留四位小数）、历史最高净值（保留四位小数）、累计收益率（百分数，保留两位小数）、申赎规则（每日、每周、每月）
								 */
								htmls += "<dt><em style='color:#ca132c;'>"+item.latestNewValue+"</em></dt>";
								htmls += "<dd>最新净值</dd>";
							}else{
								htmls += "<dt>"+profit+"</dt>";
								htmls += "<dd>业绩报酬计提基准</dd>";
							}
							htmls += "</dl>";
							if(temp == "20003" && item.typeId == '0210'){
								htmls += "<dl class='conTd'>";
								htmls += "</dl>";
							}else{
								htmls += "<dl>";
								htmls += "<dt><em>"+item.term+"</em>"+item.termUnit+"</dt>";
								htmls += "<dd>理财期限</dd>";
								htmls += "</dl>";
							}
							htmls += "<dl>";
							htmls += "<dt><em>"+numDiv(item.scale,10000)+"</em>万</dt>";
							htmls += "<dd>发行规模</dd>";
							htmls += "</dl>";
							if(temp == "20003" && item.typeId == '0210'){
								htmls += "<dl>";
								htmls += "</dl>";
							}
							htmls += "<dl>";
							htmls += "<dt><em>"+numDiv(item.money,10000)+"</em>万起</dt>";
							htmls += "<dd>理财起点</dd>";
							htmls += "</dl>";
//							htmls += "<dl>";
//							htmls += "<dt><span class='con-info-time'><b style='width:"+rate+"%'></b></span><em style='margin-right:1px; font-size:18px;'>"+rate+"</em>%</dt>";
//							htmls += "<dd>投资进度</dd>";
//							htmls += "</dl>";
							
							//过了截止日 = 募集结束
							if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
								htmls += "<a class='btn_buy fr act' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>募集结束</a>";
							//认购期= 当前时间 大于等于 认购起始日 并且 小于 认购截止日 
							}else if(daysBetween(currentWorkdate,salesDate) >= 0 && daysBetween(currentWorkdate,subdeadLine) <= 0){
								//额度是否足够
								if(displayLimit > 0){
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>购买</a>";
								}else{
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
								}
							//预约期= (预约开始日 <= 当前时间  <= 预约截止日 )
							}else if(daysBetween(currentWorkdate,appointDate) >= 0 && daysBetween(currentWorkdate,appointEndDate) <= 0){
								//额度是否足够
								if(displayLimit > 0){
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
								}else{
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
								}
							}else{
								htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
							}
						}					
						htmls += "</div>";
						htmls += "</div>";          
					}else{//不是第一条，则需要判断是否需要创建新的组标题，根据上一条标题判断
						if(temp == fundInfoDtoList[i-1].groupId){//当前数据跟上一条是同一组下
							htmls += "<div class='con01-box box02'>";
							if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
								htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"' id='"+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
							}else if(daysBetween(currentWorkdate,salesDate) >= 0){/*发售日*/
								if(displayLimit > 0){
									htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
								}else{
									htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"' id='"+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
								}
							}else if(daysBetween(currentWorkdate,appointDate) >= 0){/*预约日*/
								if(displayLimit > 0){/* 预约期*/
									htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
								}else{/* 排队中*/
									htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
								}
							}else{
								htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
							}
							htmls += "<div class='con-info'>";
							htmls += "<dl class='conFirst'>";
							
							if(item.typeId == '0500'){
								/*
								 * 开放性净值类产品申购期
								 * 展示最新净值（保留四位小数）、历史最高净值（保留四位小数）、累计收益率（百分数，保留两位小数）、申赎规则（每日、每周、每月）
								 */
								var latestNewValue = item.latestNewValue ? item.latestNewValue : '1.0000';

								/*htmls += "<dt><em style='color:#ca132c;'>"+ latestNewValue +"</em></dt>";
								htmls += "<dd>最新净值</dd>";*/
								
								var benefitSinceCreated = item.benefitSinceCreated ? item.benefitSinceCreated : '0.00';
								if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
									htmls += "<dt><em style='color:#ca132c;'>--</em></dt>";
								}else { 
									htmls += "<dt><em style='color:#ca132c;'>"+ benefitSinceCreated +"</em>%</dt>";
								}
								htmls += "<dd>成立以来年化收益率</dd>";
								
								htmls += "</dl>";
								htmls += "<dl class='conTd'>";
								htmls += "</dl>";
								htmls += "<dl>";
								htmls += "<dt><em>"+numDiv(item.money,10000)+"</em>万起</dt>";
								htmls += "<dd>理财起点</dd>";
								htmls += "</dl>";
								htmls += "<dl>";
								htmls += "</dl>";
								htmls += "<dl>";
								if(item.sartBuying == null || item.sartBuying == '') {
									htmls += "<dt><em>0</em>元起</dt>";
								} else if (item.sartBuying < 10000) {
									htmls += "<dt><em>"+item.sartBuying+"</em>元起</dt>";
								} else {
									htmls += "<dt><em>"+numDiv(item.sartBuying,10000)+"</em>万起</dt>";
								}
								htmls += "<dd>追加金额</dd>";
								htmls += "</dl>";
								
								//认购期  = 当前时间 大于等于 认购起始日
								if(daysBetween(currentWorkdate,salesDate) >= 0){
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>购买</a>";
								}else{
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
								}
							}else if(item.typeId == '0400'){
								if(seven == '1'){
									/*
									 * 7天管家类产品
									 * 展示七日年化收益、理财起点、递增金额
									 */
									var newValue = '--';
									if(item.sevenDayAnnualy&&"0.00%"!=item.sevenDayAnnualy){
										newValue =item.sevenDayAnnualy;
			                        }
									if(item.state == '1') {
										newValue = '--';
			                        }
									htmls += "<dt><em style='color:#ca132c;'>"+newValue+"</em></dt>";
									htmls += "<dd>七日年化收益</dd>";
								}else{
									/*var latestNewValue ='1.0000';
									if(item.latestNewValue){
										latestNewValue = item.latestNewValue;
									}
									htmls += "<dt><em style='color:#ca132c;'>"+latestNewValue+"</em></dt>";
									htmls += "<dd>最新净值</dd>";*/
									
									var benefitSinceCreated = item.benefitSinceCreated ? item.benefitSinceCreated : '0.00';
									if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
										htmls += "<dt><em style='color:#ca132c;'>--</em></dt>";
									}else {  
										htmls += "<dt><em style='color:#ca132c;'>"+ benefitSinceCreated +"</em>%</dt>";
									}
									htmls += "<dd>成立以来年化收益率</dd>";
								}
								htmls += "</dl>";
								htmls += "<dl class='conTd'>";
								htmls += "</dl>";
								htmls += "<dl>";
								htmls += "<dt><em>"+numDiv(item.money,10000)+"</em>万起</dt>";
								htmls += "<dd>理财起点</dd>";
								htmls += "</dl>";
								htmls += "<dl>";
								htmls += "</dl>";
								htmls += "<dl>";
								if(item.sartBuying == null || item.sartBuying == '') {
									htmls += "<dt><em>0</em>元起</dt>";
								} else if (item.sartBuying < 10000) {
									htmls += "<dt><em>"+item.sartBuying+"</em>元起</dt>";
								} else {
									htmls += "<dt><em>"+numDiv(item.sartBuying,10000)+"</em>万起</dt>";
								}
								htmls += "<dd>追加金额</dd>";
								htmls += "</dl>";				
								//过了截止日 = 募集结束
								if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
									htmls += "<a class='btn_buy fr act' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>募集结束</a>";
								//认购期= 当前时间 大于等于 认购起始日 并且 小于 认购截止日 
								}else if(daysBetween(currentWorkdate,salesDate) >= 0 && daysBetween(currentWorkdate,subdeadLine) <= 0){
									//额度是否足够
									if(displayLimit > 0){
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>购买</a>";
									}else{
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
									}
								//预约期= (预约开始日 <= 当前时间  <= 预约截止日 )
								}else if(daysBetween(currentWorkdate,appointDate) >= 0 && daysBetween(currentWorkdate,appointEndDate) <= 0){
									//额度是否足够
									if(displayLimit > 0){
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
									}else{
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
									}
								}else{
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
								}
							}else{
								if ((item.typeId == '0110' || item.typeId == '0210' || item.typeId == '0220') && item.state=='0') {
									/*
									 * 开放性净值类产品申购期
									 * 展示最新净值（保留四位小数）、历史最高净值（保留四位小数）、累计收益率（百分数，保留两位小数）、申赎规则（每日、每周、每月）
									 */
									htmls += "<dt><em style='color:#ca132c;'>"+item.latestNewValue+"</em></dt>";
									htmls += "<dd>最新净值</dd>";
								}else{
									htmls += "<dt>"+profit+"</dt>";
									htmls += "<dd>业绩报酬计提基准</dd>";
								}
								htmls += "</dl>";
								if(temp == "20003" && item.typeId == '0210'){
									htmls += "<dl class='conTd'>";
									htmls += "</dl>";
								}else{
									htmls += "<dl>";
									htmls += "<dt><em>"+item.term+"</em>"+item.termUnit+"</dt>";
									htmls += "<dd>理财期限</dd>";
									htmls += "</dl>";
								}
								htmls += "<dl>";
								htmls += "<dt><em>"+numDiv(item.scale,10000)+"</em>万</dt>";
								htmls += "<dd>发行规模</dd>";
								htmls += "</dl>";
								if(temp == "20003" && item.typeId == '0210'){
									htmls += "<dl>";
									htmls += "</dl>";
								}
								htmls += "<dl>";
								htmls += "<dt><em>"+numDiv(item.money,10000)+"</em>万起</dt>";
								htmls += "<dd>理财起点</dd>";
								htmls += "</dl>";
//								htmls += "<dl>";
//								htmls += "<dt><span class='con-info-time'><b style='width:"+rate+"%'></b></span><em style='margin-right:1px; font-size:18px;'>"+rate+"</em>%</dt>";
//								htmls += "<dd>投资进度</dd>";
//								htmls += "</dl>";
								
								//过了截止日 = 募集结束
								if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
									htmls += "<a class='btn_buy fr act' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>募集结束</a>";
								//认购期= 当前时间 大于等于 认购起始日 并且 小于 认购截止日 
								}else if(daysBetween(currentWorkdate,salesDate) >= 0 && daysBetween(currentWorkdate,subdeadLine) <= 0){
									//额度是否足够
									if(displayLimit > 0){
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>购买</a>";
									}else{
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
									}
								//预约期= (预约开始日 <= 当前时间  <= 预约截止日 )
								}else if(daysBetween(currentWorkdate,appointDate) >= 0 && daysBetween(currentWorkdate,appointEndDate) <= 0){
									//额度是否足够
									if(displayLimit > 0){
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
									}else{
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
									}
								}else{
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
								}
							}
							htmls += "</div>";
							htmls += "</div>";          
						}else{//下一条数据跟上一条不是同一组，需要新创建组标题
							htmls += "</div>";
							htmls += "<div class='con01'>";
							htmls += "<span class='con-title'>"+item.groupName+"</span>";
							htmls += "<div class='con01-box box01'>";
							if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
								htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"' id='"+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
							}else if(daysBetween(currentWorkdate,salesDate) >= 0){/*发售日*/
								if(displayLimit > 0){
									htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
								}else{
									htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"' id='"+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
								}
							}else if(daysBetween(currentWorkdate,appointDate) >= 0){/*预约日*/
								if(displayLimit > 0){/* 预约期*/
									htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
								}else{/* 排队中*/
									htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
								}
							}else{
								htmls += "<h3><a href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>"+item.adname+"</a></h3>";
							}
							htmls += "<div class='con-info'>";
							htmls += "<dl class='conFirst'>";
							if(item.typeId == '0500'){
								/*
								 * 开放性净值类产品申购期
								 * 展示最新净值（保留四位小数）、历史最高净值（保留四位小数）、累计收益率（百分数，保留两位小数）、申赎规则（每日、每周、每月）
								 */
								var latestNewValue = item.latestNewValue ? item.latestNewValue : '1.000';

								/*htmls += "<dt><em style='color:#ca132c;'>"+ latestNewValue +"</em></dt>";
								htmls += "<dd>最新净值</dd>";*/
								
								var benefitSinceCreated = item.benefitSinceCreated ? item.benefitSinceCreated : '0.00';
								if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
									htmls += "<dt><em style='color:#ca132c;'>--</em></dt>";
								}else { 
									htmls += "<dt><em style='color:#ca132c;'>"+ benefitSinceCreated +"</em>%</dt>";
								}
								htmls += "<dd>成立以来年化收益率</dd>";
								
								htmls += "</dl>";
								htmls += "<dl class='conTd'>";
								htmls += "</dl>";
								htmls += "<dl>";
								htmls += "<dt><em>"+numDiv(item.money,10000)+"</em>万起</dt>";
								htmls += "<dd>理财起点</dd>";
								htmls += "</dl>";
								htmls += "<dl>";
								htmls += "</dl>";
								htmls += "<dl>";
								if(item.sartBuying == null || item.sartBuying == '') {
									htmls += "<dt><em>0</em>元起</dt>";
								} else if (item.sartBuying < 10000) {
									htmls += "<dt><em>"+item.sartBuying+"</em>元起</dt>";
								} else {
									htmls += "<dt><em>"+numDiv(item.sartBuying,10000)+"</em>万起</dt>";
								}
								htmls += "<dd>追加金额</dd>";
								htmls += "</dl>";
								
								//认购期  = 当前时间 大于等于 认购起始日
								if(daysBetween(currentWorkdate,salesDate) >= 0){
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>购买</a>";
								}else{
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
								}
							}else if(item.typeId == '0400'){
								if(seven == '1'){
									/*
									 * 7天管家类产品
									 * 展示七日年化收益、理财起点、递增金额
									 */
									var newValue = '--';
									if(item.sevenDayAnnualy&&"0.00%"!=item.sevenDayAnnualy){
										newValue =item.sevenDayAnnualy;
			                        }
									if(item.state == '1') {
										newValue = '--';
									}
									htmls += "<dt><em style='color:#ca132c;'>"+newValue+"</em></dt>";
									htmls += "<dd>七日年化收益</dd>";
								}else{
									/*var latestNewValue ='1.0000';
									if(item.latestNewValue){
										latestNewValue = item.latestNewValue;
									}
									htmls += "<dt><em style='color:#ca132c;'>"+latestNewValue+"</em></dt>";
									htmls += "<dd>最新净值</dd>";*/
									
									var benefitSinceCreated = item.benefitSinceCreated ? item.benefitSinceCreated : '0.00';
									if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
										htmls += "<dt><em style='color:#ca132c;'>--</em></dt>";
									}else {  
										htmls += "<dt><em style='color:#ca132c;'>"+ benefitSinceCreated +"</em>%</dt>";
									}
									htmls += "<dd>成立以来年化收益率</dd>";
								}
								htmls += "</dl>";
								htmls += "<dl class='conTd'>";
								htmls += "</dl>";
								htmls += "<dl>";
								htmls += "<dt><em>"+numDiv(item.money,10000)+"</em>万起</dt>";
								htmls += "<dd>理财起点</dd>";
								htmls += "</dl>";
								htmls += "<dl>";
								htmls += "</dl>";
								htmls += "<dl>";
								if(item.sartBuying == null || item.sartBuying == '') {
									htmls += "<dt><em>0</em>元起</dt>";
								} else if (item.sartBuying < 10000) {
									htmls += "<dt><em>"+item.sartBuying+"</em>元起</dt>";
								} else {
									htmls += "<dt><em>"+numDiv(item.sartBuying,10000)+"</em>万起</dt>";
								}
								htmls += "<dd>追加金额</dd>";
								htmls += "</dl>";				
								//过了截止日 = 募集结束
								if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
									htmls += "<a class='btn_buy fr act' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>募集结束</a>";
								//认购期= 当前时间 大于等于 认购起始日 并且 小于 认购截止日 
								}else if(daysBetween(currentWorkdate,salesDate) >= 0 && daysBetween(currentWorkdate,subdeadLine) <= 0){
									//额度是否足够
									if(displayLimit > 0){
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>购买</a>";
									}else{
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
									}
								//预约期= (预约开始日 <= 当前时间  <= 预约截止日 )
								}else if(daysBetween(currentWorkdate,appointDate) >= 0 && daysBetween(currentWorkdate,appointEndDate) <= 0){
									//额度是否足够
									if(displayLimit > 0){
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
									}else{
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
									}
								}else{
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
								}
							}else{
								if ((item.typeId == '0110' || item.typeId == '0210' || item.typeId == '0220') && item.state=='0') {
									/*
									 * 开放性净值类产品申购期
									 * 展示最新净值（保留四位小数）、历史最高净值（保留四位小数）、累计收益率（百分数，保留两位小数）、申赎规则（每日、每周、每月）
									 */
									htmls += "<dt><em style='color:#ca132c;'>"+item.latestNewValue+"</em></dt>";
									htmls += "<dd>最新净值</dd>";
								}else{
									htmls += "<dt>"+profit+"</dt>";
									htmls += "<dd>业绩报酬计提基准</dd>";
								}
								htmls += "</dl>";
								if(temp == "20003" && item.typeId == '0210'){
									htmls += "<dl class='conTd'>";
									htmls += "</dl>";
								}else{
									htmls += "<dl>";
									htmls += "<dt><em>"+item.term+"</em>"+item.termUnit+"</dt>";
									htmls += "<dd>理财期限</dd>";
									htmls += "</dl>";
								}
								htmls += "<dl>";
								htmls += "<dt><em>"+numDiv(item.scale,10000)+"</em>万</dt>";
								htmls += "<dd>发行规模</dd>";
								htmls += "</dl>";
								if(temp == "20003" && item.typeId == '0210'){
									htmls += "<dl>";
									htmls += "</dl>";
								}
								htmls += "<dl>";
								htmls += "<dt><em>"+numDiv(item.money,10000)+"</em>万起</dt>";
								htmls += "<dd>理财起点</dd>";
								htmls += "</dl>";
//								htmls += "<dl>";
//								htmls += "<dt><span class='con-info-time'><b style='width:"+rate+"%'></b></span><em style='margin-right:1px; font-size:18px;'>"+rate+"</em>%</dt>";
//								htmls += "<dd>投资进度</dd>";
//								htmls += "</dl>";
								
								//过了截止日 = 募集结束
								if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
									htmls += "<a class='btn_buy fr act' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>募集结束</a>";
								//认购期= 当前时间 大于等于 认购起始日 并且 小于 认购截止日 
								}else if(daysBetween(currentWorkdate,salesDate) >= 0 && daysBetween(currentWorkdate,subdeadLine) <= 0){
									//额度是否足够
									if(displayLimit > 0){
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>购买</a>";
									}else{
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
									}
								//预约期= (预约开始日 <= 当前时间  <= 预约截止日 )
								}else if(daysBetween(currentWorkdate,appointDate) >= 0 && daysBetween(currentWorkdate,appointEndDate) <= 0){
									//额度是否足够
									if(displayLimit > 0){
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
									}else{
										htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>排队</a>";
									}
								}else{
									htmls += "<a class='btn_buy fr' href='/AppService/business/fund/fundDetail.shtml?fundid="+item.fundId+"&period="+period+"'>预约</a>";
								}						
							}
							htmls += "</div>";
							htmls += "</div>";          
						}
					}
				})
				$("#fundinfoList").html(htmls);
			}
				
		}
	});
}

/* 弹窗提示显示  无按钮 */
function show_question(){
	$("#hint_tips_btn4").show();
	var innerHeight=window.innerHeight;
	var innerWidth=window.innerWidth;
	if (innerHeight>=955 && innerWidth>=1920) {
		
	}else{
//		var mainH = innerHeight * 0.66;
//		var mainW = innerWidth * 0.45;
//		var mainMT = -(innerHeight*0.36);
//		var mainML =-(innerWidth*0.36)
//		var mainStyle={'width':mainW+'px','height':mainH+'px','margin-top':mainMT+'px','margin-left':mainML+'px'}
//		$('.cover_bg .question_tip').css(mainStyle);
//		
//		var tip1H = innerHeight * 0.25;
//		var tip1MT = -(innerHeight*0.18);
//		var tip1ML =-(innerWidth*0.013)
//		var tip2ML =-(innerWidth*0.40)
//		var tip1Style={'height':tip1H+'px','margin-top':tip1MT+'px','margin-left':tip1ML+'px'};
//		var tip2Style={'height':tip1H+'px','margin-top':tip1MT+'px','margin-left':tip2ML+'px'};
//		$('.cover_bg .pic_tip1').css(tip1Style);
//		$('.cover_bg .pic_tip2').css(tip2Style);
//		var contentH = innerHeight * 0.47;
//		var contentW = innerWidth * 0.36;
//		var contentT = (innerHeight*0.05);
//		var contentL =(innerWidth*0.04)
//		var contentStyle={'width':contentW+'px','height':contentH+'px','top':contentT+'px','left':contentL+'px'}
//		$('.cover_bg .quesiton_content').css(contentStyle);
	}
}
/* 未风险测评则弹出风险测评弹框  */
function checkIsRiskLevel(){
	var urlVal="/AppService/business/queryIsNeedTest.xhtml";
	var flag = false;
	var riskLevel = "";
	var riskEvalDate = "";
	var nowDate= new Date();
    $.ajax({
    	async:false,
		url:urlVal,
		type:"post",
		dataType:'json',
		data:{},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
		},
		success:function(data, textStatus){
			if(data){
				riskLevel = data.riskLevel;
				riskEvalDate = data.riskEvalDate;
			}
		}
	});
    
    //时间为空 则 未测评过 或者 默认等级 需要测评
    if(!riskEvalDate || "0" == riskLevel){
    	show_question();
    	if(pageSourceId != "" & pageId != "" & eventId != ""){
    		operatingRecord(pageSourceId,pageId,eventId,"");
    	}
    	return flag;
    }
    var date = new Date(riskEvalDate); 
    var dataDiff = (nowDate - date) / 86400000;
    //时间已过 则 提示过期
    if(dataDiff >= 365){
    	showAlertInfoByTargetId("noOperation");
    	$('#noOperation .btn').click(function(){
    		$('#noOperation').hide();
    		addCookie('riskUrl',location.href);
    		location.href="/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO&operation=toRiskLevel&pageSourceId="+pageSourceId+"&eventId=event_myMessage_expiredId"
    	})
    	return flag;
    }
    
    //需要测评校验通过
    flag  = true;
    
    return flag;
}

/*查QA*/
function queryWenTiByCommon(){
    var urlVal="/AppService/article/queryWenTiByCommon.xhtml";
    $.ajax({
    	async:false,
		url:urlVal,
		type:"post",
		dataType:'json',
		data:{},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
		},
		success:function(data, textStatus){
			var htmls = "";
			if(!!data && !!data.campusTalkDtoList){
				var campusTalkDtoList = data.campusTalkDtoList;
				if(campusTalkDtoList != null && campusTalkDtoList.length > 0){
					htmls += "<div class='FAQ-con'>";
					$.each(campusTalkDtoList,function(i, item){
						htmls += "<dl>";
						htmls += "<dt><p>"+item.wWenti+"</p></dt>";
						htmls += "<i class='arrow'></i>";
						htmls += "<dd>"+item.wDaan+"</dd>";
						htmls += "</dl>";
					});
					htmls += "</div>";
				}
			}
			$("#QA").html(htmls);
		}
	});
}

function showRiskLevelResult(riskLevelName,bearAbility,evalDispDateTime,evalValiDate,custRiskLevel,fundRiskLevel){
	$("#riskLevelName").html(riskLevelName);
	$("#bearAbility").html(bearAbility);
	$("#evalDispDateTime").html(evalDispDateTime);
	$("#evalValiDate").html(evalValiDate);
	$("#custRiskLevel").html(custRiskLevel);
	$("#fundRiskLevel").html(fundRiskLevel);
	$("div[class=riskContent]:eq(0)").hide();
	$("div[class=riskResult]:eq(0)").show();
	//var flag = prodSwitch();
	//if(flag == false){
		queryUserIsRealName();
	//}
}

// function prodSwitch(){
// 	var nowDate = new Date();//新增的财富宝开关
// 	var time = nowDate.getFullYear() + "" +((nowDate.getMonth()+1)<10?"0":"")+(nowDate.getMonth()+1)+""+(nowDate.getDate()<10?"0":"")+nowDate.getDate();
// 	var rst = false,userId = queryUserId();
// 	var planDate = queryParamList("SYSTEM","PRODPLANDATE","");
// 	//财富宝
// 	if(time <= planDate[0].pmco){
// 		t_delay = setInterval (function(){
// 			if(pageSourceId != '' && pageSourceId != null && pageSourceId != undefined ){
// 				window.location.href="/AppService/business/fund/wealthTreasure.shtml?pageSourceId="+pageSourceId+"&eventId=event_003_commitQuestionId";
// 			} else {
// 				window.location.href="/AppService/business/fund/wealthTreasure.shtml?pageSourceId=wealthProductsId&eventId=event_003_commitQuestionId";
// 			}
// 		}, 3000);
// 		rst =  true;
// 	}
// 	//合利
// 	planDate = queryParamList("SYSTEM","HELIDATE","");
// 	if(time <= planDate[0].pmco){
// 		t_delay = setInterval (function(){
// 			if(pageSourceId != '' && pageSourceId != null && pageSourceId != undefined ){
// 				window.location.href="/AppService/business/fund/heli.shtml?pageSourceId="+pageSourceId+"&eventId=event_003_commitQuestionId";
// 			} else {
// 				window.location.href="/AppService/business/fund/heli.shtml?pageSourceId=personalInformationId&eventId=event_003_commitQuestionId";
// 			}
// 		}, 3000);
// 		rst =  true;
// 	}
// }

/**
 * 获取用户是否实名
 */
function queryUserIsRealName(){
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
				//是否实名
				var userType = data.userType;
				if(userType == null || userType == '10'){
					$("#isToRealName a").attr("href","/AppService/business/bank/realName.shtml?pageSourceId="+pageSourceId+"&eventId=event_user_realNameId");
					$("#isToRealName").show();
					$(".result-bottom .result-bottom-left").remove();
					$(".result-bottom .result-bottom-right").css('margin-left',"130px");
					t_delay = setInterval (function(){
						$("#isToRealName .result-bottom-left a")[0].click();
                	}, 5000);
				}else{
					t_delay = setInterval (function(){
						window.location.href="/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO"
					}, 5000);
				}
			}
		}
	});
}
function detailTips(){
    clearInterval(t_delay);
	$("#detailText").show();
	pageEventData("01","02");
}
function closeDetailText(){
	$("#detailText").hide();
	pageEventData("01","03");
	queryUserIsRealName();
}

function updateUserInvprtpAlert(){
	hideAlertInfoByTargetId("hint_risk_update");
	var urlVal="/AppService/business/updateUserInvprtpAlert.xhtml";
    $.ajax({
    	async:true,
		url:urlVal,
		type:"post",
		dataType:'text',
		data:{},
		error:function(){
			show_tips("网络繁忙，请稍后再试。"); 
		},
		success:function(data, textStatus){
		}
	});
}


function hideAlertInfoByTargetId(targetId){
	$("#"+targetId).hide();
}

function showAlertInfoByTargetId(targetId){
	$("#"+targetId).show();
}


/**
 * 交互元素位置，使活期理财产品在定期理财的后面
 */
initCurrentProduct()
function initCurrentProduct(){
	$(document).ready(function(){
		$(".con01").each(function(){
			if($(this).find("span").text()=="活期理财"){
				if($(this).index()=="0"){
                                   var second=$(".con01 ").eq(1).html()
                                   var current=$(this).html()
                                   $(".con01 ").eq(1).html(current)
                                   $(this).html(second)
				}
			}
		})
	})
}

/**
 * /**
 * 页面操作记录
 * @param buried_PageSource	来源页面Id
 * @param buried_PageId		页面Id
 * @param buried_EventId	事件Id
 * @param buried_GroupId	页面分组Id 默认传空
 */
function operatingRecord(buried_PageSource,buried_PageId,buried_EventId,buried_GroupId){
	$.ajax({
        async: true,
        url: "/AppService/buriedData.xhtml",
        data: {
        	'pageSource' : buried_PageSource,
        	'pageId' : buried_PageId,
            'eventId' :buried_EventId,
            'groupId' : buried_GroupId
        },
        dataType: "json",
        cache: false,
        type: "POST",
        error: function() {
            show_tips("网络繁忙，请稍后再试。");
        },
        success: function(n) {}
    });
}
