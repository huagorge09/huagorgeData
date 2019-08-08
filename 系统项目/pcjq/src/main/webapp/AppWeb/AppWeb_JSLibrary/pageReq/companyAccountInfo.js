var i = 0;
var k = 0;
$(document).ready(function(e) {
	getUserRequest("pc_applicationGroups_accountInfo");
	getUserRequest("pc_applicationGroups_accountInfo_01");
	document.title = "财富总览_招商财富 都市精英的私人银行、高净值人士专业资产管理平台";
	//当前页面pageId
	//queryAccount();
	//queryTradeInfoList();
	//queryMyBankCard();
	var index = getUrlParameter("item");
	index = removeSpecialStr(index);
	if (index != null && index != "") {
		showOrderList(index);
	}
	$(".myorder-con-nav li").each(function () {
		if($(this).hasClass("act")){
			var index=$(this).index();
		    $(".pag").eq(index).show().siblings().hide();
		}
	})

	$(".nav li").eq(1).find("a").removeClass("current");
	$(".My-center").addClass("act")
	$(window).load(function() {
		//$(".select-text-list").mCustomScrollbar();
	});

});

function showPlanBg(){
	$("#plan_message_bg").show();
		//$('.common-box').css('top', 'initial');
		//$('.common-box').css('left', 'initial');
	
	window.setTimeout(function(){
		$(".common-box").animate({
		    top:'100%',
	        left:"100%",
	        width:"337.5px",
	        height:"250.5px",
	        'margin-left':"-348px",
	        'margin-top':"-261px"
	    },800);
		$(".common-box .close_plan").animate({
			'top':'8px',
			'right':'8px'
		},800);
	},1200);
	//$("#plan_bg_pic").animate({top:"80%",left:"80%"});
	//$("#plan_bg_pic").animate({bottom:"3px",top:"initial",left:"initial",right:"3px"},2000);
}
/* 下一张验证码 注册 */
function getRandomCode() {
	$("#rondomCodeImg").attr("src", "/AppService/setUp/buildimageservlet.xhtml?count=" + i);
	i++;
}
/* 下一张验证码 注册 */
function getRandomCode1() {
	$("#rondomCodeImg1").attr("src", "/AppService/setUp/buildimageservlet.xhtml?count=" + k);
	k++;
}
/* 全部/未支付/已支付/存续/已到期 互相切换 */
function showOrderList(index) {
	getUserRequest("pc_applicationGroups_accountInfo_0"+index);
	$("#orderList .myorder-con-nav ul li").removeClass("act");
	$("#section_01").hide();
	$("#section_02").hide();
	$("#section_03").hide();
	$("#section_04").hide();
	$("#section_05").hide();
	$("#section_0" + (index + 1)).show();

	$(".myorder-con-nav ul li:eq(" + index + ")").addClass("act");
}
/**
 * 保留两位小数，没有小数不做处理
 * 
 * @param num
 * @returns
 */
function unformatNumber(num) {
	if (parseInt(num,10) == num) {
		return parseInt(num,10);
	} else {
		return parseFloat(num).toFixed(2);
	}
}
/* 查询用户总资产和总收益 */
function queryAccount() {
	$.ajax({
		async : false,
		url : "/AppService/setUp/queryCompanyUserFundTotal.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) { 
		console.log(data);
			if(!!data){
				if(data.returnCode=="0000"){
				 	$("#asstes").text(formatNumber(parseFloat(unformat(data.data.totalbill)).toFixed(2), ","));
					$("#profitExp").text(formatNumber(parseFloat(unformat(data.data.totalprofit)).toFixed(2), ","));
                    $("#nextNoFund").show();
					$("#nextLoadFund").hide();
				}
			}
		}
	});
}
/* 查询订单列表 */
function queryTradeInfoList() {
	$.ajax({
		async : false,
		url : "/AppService/business/queryTradeInfoList.xhtml",
		data : {},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var htmls1 = "";
			var htmls2 = "";
			var htmls3 = "";
			var htmls4 = "";
			var htmls5 = "";
			var list = data.list;
			var uList = data.uList;
			var pList = data.pList;
			var gList = data.gList;
			var zList = data.zList;

			/* 全部订单 */
			if (list != null && list.length > 0) {
				$.each(list, function(i, item) {
					var fundInfoDto = item.fundInfoDtoV2;
					var appointDate = fundInfoDto.appointDate;/*预约开始日期*/
					var currentWorkdate = fundInfoDto.currentWorkdate;/*当前工作日*/
					var salesDate = fundInfoDto.salesDate;/*发售日*/
					var subdeadLine = fundInfoDto.subdeadLine;/*截止日*/
					var appPayMoneyText = "今天15:00前";
					var isAppointDate = false;
					
					htmls1 += "<div class='myorder-list-box'>";
					if (item.orderType != null && item.orderType == "1") {/* 待付款 */
						if(daysBetween(currentWorkdate,salesDate) >= 0){/*销售期*/
							htmls1 += "<div class='box-type waiting-pay'>待付款</div>";
						}else if(daysBetween(currentWorkdate,appointDate) >= 0){/*预约期*/
							htmls1 += "<div class='box-type appoint-succ'>预约成功</div>";
							appPayMoneyText = formatDate1(salesDate) +" - " + formatDate1(subdeadLine) + "15:00前";
							isAppointDate = true;
						}else {/*TODO 要确定即不是认购期也不是预约期的订单该怎么显示，现在暂时显示认购中*/
							htmls1 += "<div class='box-type waiting-pay'>待付款</div>";
						}
					} else if (item.orderType != null && item.orderType == "2") {/* 已支付 */
						htmls1 += "<div class='box-type success'>已支付</div>";
					} else if (item.orderType != null && item.orderType == "3") {/* 排队中 */
						htmls1 += "<div class='box-type paidui waiting-pay'>排队中</div>";
					} else if (item.orderType != null && item.orderType == "4") {/* 存续中 */
						htmls1 += "<div class='box-type saveextend'>存续中</div>";
					} else if (item.orderType != null && item.orderType == "5") {/* 已到期 */
						htmls1 += "<div class='box-type timeout'>已到期</div>";
					} else if (item.orderType != null && item.orderType == "6") {/* 已失效 */
						htmls1 += "<div class='box-type subsist'>已失效</div>";
					} else if (item.orderType != null && item.orderType == "7") {/* 待确认 */
						htmls1 += "<div class='box-type verify waiting-pay'>待确认</div>";
					} else if (item.orderType != null && item.orderType == "8") {/* 待确认 */
						htmls1 += "<div class='box-type shouli'>待确认</div>";
					}
					var realSpeed = parseFloat(numMulti(numDiv((item.fundInfoDtoV2.scale - item.fundInfoDtoV2.displayLimit), item.fundInfoDtoV2.scale), 100)).toFixed(0);
					var speed = getSpeed(realSpeed);
					htmls1 += "<h1>";
					htmls1 += "<a href='/AppService/business/fund/orderDetail.shtml?serialno=" + item.serialno + "' target='_blank'>" + item.fundInfoDtoV2.adname + "</a>";
					htmls1 += "</h1>";
					htmls1 += "<span class='date'>" + item.apdt + "</span> <span class='ordernumber'>订单号:<em>" + item.serialno + "</em></span>";
					htmls1 += "<div class='box-parameter'>";
					if (item.orderType != null && item.orderType == "1") {/* 待付款 */
						htmls1 += "<dl>";
						if((item.fundInfoDtoV2.typeId=='0110' || item.fundInfoDtoV2.typeId=='0210' || item.fundInfoDtoV2.typeId=='0220') && item.fundInfoDtoV2.state=='0'){
							htmls1 += "<dt>最新净值</dt>";
							var numlate = new Number(item.fundInfoDtoV2.latestNewValue);
							htmls1 += "<dd>"+numlate.toFixed(4)+"</dd>";
						}else{
							htmls1 += "<dt>业绩报酬计提基准</dt>";
							if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
								htmls1 += "<dd>浮动收益</dd>";
							} else {
								htmls1 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
								+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
							}
						}
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>理财期限</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>起息日</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.interestDate + "</dd>";
						htmls1 += "</dl>";
						if (item.fee != 0 && item.fee != '') {
							htmls1 += "<dl class='addwaitpay'>";
						} else {
							htmls1 += "<dl>";
						}
						if(isAppointDate){
							htmls1 += "<dt>预约金额</dt>";
						}else{
							htmls1 += "<dt>待支付金额</dt>";
						}
						
						htmls1 += "<dd>" + formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em></dd>";
						if (item.fee != ''&&item.fee != 0) {
							htmls1 += "<em>+" + item.fee + "（认购费）</em>";
						}
						htmls1 += "</dl>";
						htmls1 += "<dl class='schedule'>";
						htmls1 += "<p class='cirle cirle01'></p>";
						htmls1 += "<dt>投资进度</dt>";
						htmls1 += "<dd><p class='cirle cirle" + speed + "'></p><i>" + realSpeed + "<em style='font-weight: normal;'>%</em></i><b></b></dd>";
						htmls1 += "</dl>";
					} else if (item.orderType != null && item.orderType == "2") {/* 已支付 */
						htmls1 += "<dl>";
						if((item.fundInfoDtoV2.typeId=='0110' || item.fundInfoDtoV2.typeId=='0210' || item.fundInfoDtoV2.typeId=='0220')&&item.fundInfoDtoV2.state=='0'){
							htmls1 += "<dt>最新净值</dt>";
							var numlate = new Number(item.fundInfoDtoV2.latestNewValue);
							htmls1 += "<dd>"+numlate.toFixed(4)+"</dd>";
						}else{
							htmls1 += "<dt>业绩报酬计提基准</dt>";
							if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
								htmls1 += "<dd>浮动收益</dd>";
							} else {
								htmls1 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
										+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
							}
						}
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>理财期限</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>起息日</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.interestDate + "</dd>";
						htmls1 += "</dl>";
						if (item.fee != 0 && item.fee != '') {
							htmls1 += "<dl class='addwaitpay'>";
						} else {
							htmls1 += "<dl>";
						}
						htmls1 += "<dt>买入金额</dt>";
						htmls1 += "<dd>";
						htmls1 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
						htmls1 += "</dd>";
						if (item.fee != ''&&item.fee != 0) {
							htmls1 += "<em>+" + item.fee + "（认购费）</em>";
						}
						htmls1 += "</dl>";
						htmls1 += "<dl class='schedule'>";
						htmls1 += "<p class='cirle cirle01'></p>";
						htmls1 += "<dt>投资进度</dt>";
						htmls1 += "<dd><p class='cirle cirle" + speed + "'></p><i>" + realSpeed + "<em style='font-weight: normal;'>%</em></i><b></b></dd>";
						htmls1 += "</dl>";
					} else if (item.orderType != null && item.orderType == "3") {/* 排队中 */
						htmls1 += "<dl>";
						if((item.fundInfoDtoV2.typeId=='0110' || item.fundInfoDtoV2.typeId=='0210' || item.fundInfoDtoV2.typeId=='0220')&&item.fundInfoDtoV2.state=='0'){
							htmls1 += "<dt>最新净值</dt>";
							var numlate = new Number(item.fundInfoDtoV2.latestNewValue);
							htmls1 += "<dd>"+numlate.toFixed(4)+"</dd>";
						}else{
							htmls1 += "<dt>业绩报酬计提基准</dt>";
							if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
								htmls1 += "<dd>浮动收益</dd>";
							} else {
								htmls1 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
										+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
							}
						}
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>理财期限</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>起息日</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.interestDate + "</dd>";
						htmls1 += "</dl>";
						if (item.fee != 0 && item.fee != '') {
							htmls1 += "<dl class='addwaitpay'>";
						} else {
							htmls1 += "<dl>";
						}
						htmls1 += "<dt>待支付金额</dt>";
						htmls1 += "<dd>";
						htmls1 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
						htmls1 += "</dd>";
						if (item.fee != ''&&item.fee != 0) {
							htmls1 += "<em>+" + item.fee + "（认购费）</em>";
						}
						htmls1 += "</dl>";
						htmls1 += "<dl class='schedule'>";
						htmls1 += "<p class='cirle cirle01'></p>";
						htmls1 += "<dt>投资进度</dt>";
						htmls1 += "<dd><p class='cirle cirle" + speed + "'></p><i>" + realSpeed + "<em style='font-weight: normal;'>%</em></i><b></b></dd>";
						htmls1 += "</dl>";
					} else if (item.orderType != null && item.orderType == "4") {/* 存续中 */
						htmls1 += "<dl>";
						if(item.fundInfoDtoV2.typeId=='0110' || item.fundInfoDtoV2.typeId=='0210'|| item.fundInfoDtoV2.typeId=='0220'){
							htmls1 += "<dt>最新净值</dt>";
							var numlate = new Number(item.fundInfoDtoV2.latestNewValue);
							htmls1 += "<dd>"+numlate.toFixed(4)+"</dd>";
						}else if(item.fundInfoDtoV2.typeId=='0300'){
							if(item.shareAmt != null && item.shareAmt != "" && item.shareAmt != "0"){
								htmls1 += "<dt>已分配金额</dt>";
								htmls1 += "<dd>"+format(parseFloat(item.shareAmt))+"</dd>";
							}else{
								htmls1 += "<dt>业绩报酬计提基准</dt>";
								htmls1 += "<dd>浮动收益</dd>";
							}
						}else{
							htmls1 += "<dt>业绩报酬计提基准</dt>";
							if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
								htmls1 += "<dd>浮动收益</dd>";
							} else {
								htmls1 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
										+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
							}
						}
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>理财期限</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						if(item.fundInfoDtoV2.typeId=='0110'){
							htmls1 += "<dt>下一到期日</dt>";
						}else{
							htmls1 += "<dt>到期日</dt>";
						}
						htmls1 += "<dd>" + item.fundInfoDtoV2.maturityDate + "</dd>";
						htmls1 += "</dl>";
						if (item.fee != 0 && item.fee != '') {
							htmls1 += "<dl class='addwaitpay'>";
						} else {
							htmls1 += "<dl>";
						}
						if(item.fundInfoDtoV2.typeId=='0110')
							htmls1 += "<dt>买入金额</dt>";
						else
							htmls1 += "<dt>待收本金</dt>";
							
						htmls1 += "<dd>";
						if (item.fundInfoDtoV2.typeId=='0110') {
							htmls1 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
						} else {
							htmls1 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
						}
						htmls1 += "</dd>";
						if (item.fee != ''&&item.fee != 0) {
							htmls1 += "<em>+" + item.fee + "（认购费）</em>";
						}
						htmls1 += "</dl>";
					} else if (item.orderType != null && item.orderType == "5") {/* 已到期 */
						htmls1 += "<dl>";
						if(item.fundInfoDtoV2.typeId=='0110' || item.fundInfoDtoV2.typeId=='0210'||item.fundInfoDtoV2.typeId=='0220'){
							htmls1 += "<dt>最新净值</dt>";
							var numlate = new Number(item.fundInfoDtoV2.latestNewValue);
							htmls1 += "<dd>"+numlate.toFixed(4)+"</dd>";
						}else if(item.fundInfoDtoV2.typeId=='0300'){
							if(item.shareAmt != null && item.shareAmt != "" && item.shareAmt != "0"){
								htmls1 += "<dt>已分配金额</dt>";
								htmls1 += "<dd>"+format(parseFloat(item.shareAmt))+"</dd>";
							}else{
								htmls1 += "<dt>业绩报酬计提基准</dt>";
								htmls1 += "<dd>浮动收益</dd>";
							}
						}else{
							htmls1 += "<dt>业绩报酬计提基准</dt>";
							if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
								htmls1 += "<dd>浮动收益</dd>";
							} else {
								htmls1 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
										+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
							}
						}
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>理财期限</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
						htmls1 += "</dl>";
						if (item.fee != 0 && item.fee != '') {
							htmls1 += "<dl class='addwaitpay'>";
						} else {
							htmls1 += "<dl>";
						}
						
						htmls1 += "<dt>买入金额</dt>";
						
						htmls1 += "<dd>" + formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em></dd>";
						if ( item.fee != '' && item.fee != 0) {
							htmls1 += "<em>+" + item.fee + "（认购费）</em>";
						}
						htmls1 += "</dl>";
						htmls1 += "<dl>"
						htmls1 += "<dt>待收收益</dt>";
						if(isNaN(item.benefit) || isNaN(parseFloat(item.benefit)) || item.benefit == 0){/* 存续中 */
							htmls1 += "<dd>浮动收益</dd>";
						}else{
							htmls1 += "<dd>" + unformatNumber(item.benefit) + "元</dd>";
						}
						htmls1 += "</dl>";
					} else if (item.orderType != null && item.orderType == "6") {/* 已失效 */
						if (item.payst != null && item.payst == "Y") {/* 已支付 */
							htmls1 += "<dl>";
							htmls1 += "<dt>业绩报酬计提基准</dt>";
							if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
								htmls1 += "<dd>浮动收益</dd>";
							} else {
								htmls1 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
										+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
							}
							htmls1 += "</dl>";
							htmls1 += "<dl>";
							htmls1 += "<dt>理财期限</dt>";
							htmls1 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
							htmls1 += "</dl>";
							htmls1 += "<dl>";
							htmls1 += "<dt>起息日</dt>";
							htmls1 += "<dd>" + item.fundInfoDtoV2.interestDate + "</dd>";
							htmls1 += "</dl>";
							if (item.fee != 0 && item.fee != '') {
								htmls1 += "<dl class='addwaitpay'>";
							} else {
								htmls1 += "<dl>";
							}
							htmls1 += "<dt>买入金额</dt>";
							htmls1 += "<dd>";
							htmls1 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
							if (item.fee != ''&&item.fee != 0) {
								htmls1 += "<em>+" + item.fee + "（认购费）</em>";
							}
							htmls1 += "</dd>";
							htmls1 += "</dl>";
							htmls1 += "<dl class='schedule'>";
							htmls1 += "<p class='cirle cirle01'></p>";
							htmls1 += "<dt>投资进度</dt>";
							htmls1 += "<dd><p class='cirle cirle" + speed + "'></p><i>" + realSpeed + "<em style='font-weight: normal;'>%</em></i><b></b></dd>";
							htmls1 += "</dl>";
						} else {/* 待付款 */
							htmls1 += "<dl>";
							htmls1 += "<dt>业绩报酬计提基准</dt>";
							if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
								htmls1 += "<dd>浮动收益</dd>";
							} else {
								htmls1 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
										+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
							}
							htmls1 += "</dl>";
							htmls1 += "<dl>";
							htmls1 += "<dt>理财期限</dt>";
							htmls1 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
							htmls1 += "</dl>";
							htmls1 += "<dl>";
							htmls1 += "<dt>起息日</dt>";
							htmls1 += "<dd>" + item.fundInfoDtoV2.interestDate + "</dd>";
							htmls1 += "</dl>";
							if (item.fee != 0 && item.fee != '') {
								htmls1 += "<dl class='addwaitpay'>";
							} else {
								htmls1 += "<dl>";
							}
							htmls1 += "<dt>待支付金额</dt>";
							htmls1 += "<dd>";
							htmls1 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
							if (item.fee != ''&&item.fee != 0) {
								htmls1 += "<em>+" + item.fee + "（认购费）</em>";
							}
							htmls1 += "</dd>";
							htmls1 += "</dl>";
							htmls1 += "<dl class='schedule'>";
							htmls1 += "<p class='cirle cirle01'></p>";
							htmls1 += "<dt>投资进度</dt>";
							htmls1 += "<dd><p class='cirle cirle" + speed + "'></p><i>" + realSpeed + "<em style='font-weight: normal;'>%</em></i><b></b></dd>";
							htmls1 += "</dl>";
						}
					} else if (item.orderType != null && item.orderType == "7") {/* 待确认 */
						htmls1 += "<dl>";
						htmls1 += "<dt>业绩报酬计提基准</dt>";
						if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
							htmls1 += "<dd>浮动收益</dd>";
						} else {
							htmls1 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
									+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
						}
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>理财期限</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>起息日</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.interestDate + "</dd>";
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>待支付金额</dt>";
						htmls1 += "<dd>";
						htmls1 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
						htmls1 += "</dd>";
						htmls1 += "</dl>";
						htmls1 += "<dl class='schedule'>";
						htmls1 += "<p class='cirle cirle01'></p>";
						htmls1 += "<dt>投资进度</dt>";
						htmls1 += "<dd><p class='cirle cirle" + speed + "'></p><i>" + realSpeed + "<em style='font-weight: normal;'>%</em></i><b></b></dd>";
						htmls1 += "</dl>";
					} else if (item.orderType != null && item.orderType == "8") {/* 受理中 */
						htmls1 += "<dl>";
						if((item.fundInfoDtoV2.typeId=='0110' || item.fundInfoDtoV2.typeId=='0210' ||item.fundInfoDtoV2.typeId=='0220') &&item.fundInfoDtoV2.state=='0'){
							htmls1 += "<dt>最新净值</dt>";
							var numlate = new Number(item.fundInfoDtoV2.latestNewValue);
							htmls1 += "<dd>"+numlate.toFixed(4)+"</dd>";
						}else{
							htmls1 += "<dt>业绩报酬计提基准</dt>";
							if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
								htmls1 += "<dd>浮动收益</dd>";
							} else {
								htmls1 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
										+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
							}
						}
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>理财期限</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
						htmls1 += "</dl>";
						htmls1 += "<dl>";
						htmls1 += "<dt>起息日</dt>";
						htmls1 += "<dd>" + item.fundInfoDtoV2.interestDate + "</dd>";
						htmls1 += "</dl>";
						if (item.fee != 0 && item.fee != '') {
							htmls1 += "<dl class='addwaitpay'>";
						} else {
							htmls1 += "<dl>";
						}
						htmls1 += "<dt>买入金额</dt>";
						htmls1 += "<dd>";
						htmls1 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
						htmls1 += "</dd>";
						if (item.fee != ''&&item.fee != 0) {
							htmls1 += "<em>+" + item.fee + "（认购费）</em>";
						}
						htmls1 += "</dl>";
						htmls1 += "<dl class='schedule'>";
						htmls1 += "<p class='cirle cirle01'></p>";
						htmls1 += "<dt>投资进度</dt>";
						htmls1 += "<dd><p class='cirle cirle" + speed + "'></p><i>" + realSpeed + "<em style='font-weight: normal;'>%</em></i><b></b></dd>";
						htmls1 += "</dl>";
					}
					
					if (item.orderType != null && (item.orderType == "1" || item.orderType == "3")) {/* 待付款 */
						var feeRemit = item.fee || 0;
						var today = "" + item.fundInfoDtoV2.currentWorkdate;
						var appointEndDate = item.fundInfoDtoV2.appointEndDate;/* 预约结束日期 */
						var buyType = '0';
						/* 购买类型 1，预约；2，认购；4，排队 */
						if (daysBetween(today, appointEndDate) > 0) {
							buyType = 2;/* 认购单 */
						} else {
							if (item.fundInfoDtoV2.displayLimit == 0) {
								buyType = 4;/* 排队单 */
							} else {
								buyType = 1;/* 预约单 */
							}
						}
						var bType=item.orderType == '3'?'查看排队信息':'查看汇款信息';
						htmls1 += "<a href='javascript:queryOrder(\"" + item.orderType + "\",\"" + item.serialno + "\",\"" + item.subamt + "\",\"" + feeRemit + "\",\"" + item.tradeacco + "\",\""
								+ buyType + "\",\"" + item.fundInfoDtoV2.scale + "\",\"" + item.fundInfoDtoV2.money + "\",\"" + item.fundInfoDtoV2.moneyStep + "\",\""
								+ item.fundInfoDtoV2.serialnodisplayLimit + "\",\"" + item.fundid + "\")' class='check-remit'>"+bType+"</a>";
					} else if (item.orderType != null && item.orderType == "7") {/* 确认订单 */
						htmls1 += "<a href='/AppService/business/fund/confirmBuy.shtml?serialno=" + item.serialno + "' class='check-remit'>确认订单</a>";
					}
					htmls1 += "</div>";
					if(isAppointDate){ // 前面已经判断过 是否预约期 保持一致
						htmls1 += "<div class='appPayMoneyBg'>"; 
						htmls1 += "<span class='appPayMoneyStrong'>温馨提示：</span><span>请您于募集期（</span><span class='appPayMoneyStrong'>"+appPayMoneyText+"</span><span>）进行打款操作，锁定产品额度。</span>";
						htmls1 += "</div>"; 
					}
					htmls1 += "</div>";
				});
			} else {
				htmls1 += "<span class='myorder-list-none'>暂无订单</span>";
			}
			/* 未支付订单 */
			if (uList != null && uList.length > 0) {
				$.each(uList, function(i, item) {
					var fundInfoDto = item.fundInfoDtoV2;
					var appointDate = fundInfoDto.appointDate;/*预约开始日期*/
					var salesDate = fundInfoDto.salesDate;/*发售日*/
					var subdeadLine = fundInfoDto.subdeadLine;/*截止日*/
					var currentWorkdate = fundInfoDto.currentWorkdate;/*当前工作日*/
					var realSpeed = parseFloat(numMulti(numDiv((item.fundInfoDtoV2.scale - item.fundInfoDtoV2.displayLimit), item.fundInfoDtoV2.scale), 100)).toFixed(0);
					var speed = getSpeed(realSpeed);
					var appPayMoneyText = "今天15:00前";
					var isAppointDate = false;
					htmls2 += "<div class='myorder-list-box'>";
					if (item.orderType != null && item.orderType == "1") {/* 待付款 */
						if(daysBetween(currentWorkdate,salesDate) >= 0){/*销售期*/
							htmls2 += "<div class='box-type waiting-pay'>待付款</div>";
						}else if(daysBetween(currentWorkdate,appointDate) >= 0){/*预约期*/
							htmls2 += "<div class='box-type appoint-succ'>预约成功</div>";
							appPayMoneyText = formatDate1(salesDate) +" - " + formatDate1(subdeadLine) + "15:00前";
							isAppointDate = true;
						}else {/*TODO 要确定即不是认购期也不是预约期的订单该怎么显示，现在暂时显示认购中*/
							htmls2 += "<div class='box-type waiting-pay'>认购中</div>";
						}
					} else if (item.orderType != null && item.orderType == "3") {/* 排队中 */
						htmls2 += "<div class='box-type paidui waiting-pay'>排队中</div>";
					} else if (item.orderType != null && item.orderType == "7") {/* 待确认 */
						htmls2 += "<div class='box-type verify waiting-pay'>待确认</div>";
					} else if (item.orderType != null && item.orderType == "8") {/* 待确认 */
						htmls2 += "<div class='box-type shouli'>待确认</div>";
					}
					htmls2 += "<h1>";
					htmls2 += "<a href='/AppService/business/fund/orderDetail.shtml?serialno=" + item.serialno + "'  target='_blank'>" + item.fundInfoDtoV2.adname + "</a>";
					htmls2 += "</h1>";
					htmls2 += "<span class='date'>" + item.apdt + "</span> <span class='ordernumber'>订单号:<em>" + item.serialno + "</em></span>";
					htmls2 += "<div class='box-parameter'>";
					htmls2 += "<dl>";
					if(item.fundInfoDtoV2.typeId=='0110' || item.fundInfoDtoV2.typeId=='0210' ||item.fundInfoDtoV2.typeId=='0220'){
						htmls2 += "<dt>最新净值</dt>";
						var numlate = new Number(item.fundInfoDtoV2.latestNewValue);
						htmls2 += "<dd>"+numlate.toFixed(4)+"</dd>";
					}else{
						htmls2 += "<dt>业绩报酬计提基准</dt>";
						if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
							htmls2 += "<dd>浮动收益</dd>";
						} else {
							htmls2 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
									+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
						}
					}
					htmls2 += "</dl>";
					htmls2 += "<dl>";
					htmls2 += "<dt>理财期限</dt>";
					htmls2 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
					htmls2 += "</dl>";
					htmls2 += "<dl>";
					htmls2 += "<dt>起息日</dt>";
					htmls2 += "<dd>" + item.fundInfoDtoV2.interestDate + "</dd>";
					htmls2 += "</dl>";
					if (item.fee != 0 && item.fee != '') {
						htmls2 += "<dl class='addwaitpay'>";
					} else {
						htmls2 += "<dl>";
					}
					if (item.orderType != null && item.orderType == "8"){
						htmls2 += "<dt>买入金额</dt>";
					} else{
						if(isAppointDate){
							htmls2 += "<dt>预约金额</dt>";
						}else{
							htmls2 += "<dt>待支付金额</dt>";
						}
					}
					htmls2 += "<dd>";
					htmls2 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
					htmls2 += "</dd>";
					if ( item.fee != '' && item.fee != 0) {
						htmls2 += "<em>+" + item.fee + "（认购费）</em>";
					}
					htmls2 += "</dl>";
					htmls2 += "<dl class='schedule'>";
					htmls2 += "<p class='cirle cirle01'></p>";
					htmls2 += "<dt>投资进度</dt>";
					htmls2 += "<dd><p class='cirle cirle" + speed + "'></p><i>" + realSpeed + "<em style='font-weight: normal;'>%</em></i><b></b></dd>";
					htmls2 += "</dl>";
					if (item.orderType != null && (item.orderType == "1" || item.orderType == "3")) {/* 待付款 */
						var feeRemit = item.fee || 0;
						var today = "" + item.fundInfoDtoV2.currentWorkdate;
						var appointEndDate = item.fundInfoDtoV2.appointEndDate;/* 预约结束日期 */
						var buyType = '0';
						/* 购买类型 1，预约；2，认购；4，排队 */
						if (daysBetween(today, appointEndDate) > 0) {
							buyType = 2;/* 认购单 */
						} else {
							if (item.fundInfoDtoV2.displayLimit == 0) {
								buyType = 4;/* 排队单 */
							} else {
								buyType = 1;/* 预约单 */
							}
						}
						var bType=item.orderType == '3'?'查看排队信息':'查看汇款信息';
						htmls2 += "<a href='javascript:queryOrder(\"" + item.orderType + "\",\"" + item.serialno + "\",\"" + item.subamt + "\",\"" + feeRemit + "\",\"" + item.tradeacco + "\",\""
								+ buyType + "\",\"" + item.fundInfoDtoV2.scale + "\",\"" + item.fundInfoDtoV2.money + "\",\"" + item.fundInfoDtoV2.moneyStep + "\",\""
								+ item.fundInfoDtoV2.serialnodisplayLimit + "\")' class='check-remit'>"+bType+"</a>";
					} else if (item.orderType != null && item.orderType == "7") {/* 待确认 */
						htmls2 += "<a href='/AppService/business/fund/confirmBuy.shtml?serialno=" + item.serialno + "' class='check-remit'>确认订单</a>";
					}
					htmls2 += "</div>";
					if(isAppointDate){ // 前面已经判断过 是否预约期 保持一致
						htmls2 += "<div class='appPayMoneyBg'>"; 
						htmls2 += "<span class='appPayMoneyStrong'>温馨提示：</span><span>请您于募集期（</span><span class='appPayMoneyStrong'>"+appPayMoneyText+"</span><span>）进行打款操作，锁定产品额度。</span>";
						htmls2 += "</div>"; 
					}
					htmls2 += "</div>";
				});
			} else {
				htmls2 += "<span class='myorder-list-none'>暂无订单</span>";
			}

			/* 已支付订单 */
			if (pList != null && pList.length > 0) {
				$.each(pList, function(i, item) {
					var realSpeed = parseFloat(numMulti(numDiv((item.fundInfoDtoV2.scale - item.fundInfoDtoV2.displayLimit), item.fundInfoDtoV2.scale), 100)).toFixed(0);
					var speed = getSpeed(realSpeed);
					htmls3 += "<div class='myorder-list-box'>";
					htmls3 += "<div class='box-type success'>已支付</div>";
					htmls3 += "<h1>";
					htmls3 += "<a href='/AppService/business/fund/orderDetail.shtml?serialno=" + item.serialno + "'  target='_blank'>" + item.fundInfoDtoV2.adname + "</a>";
					htmls3 += "</h1>";
					htmls3 += "<span class='date'>" + item.apdt + "</span> <span class='ordernumber'>订单号:<em>" + item.serialno + "</em></span>";
					htmls3 += "<div class='box-parameter'>";
					htmls3 += "<dl>";
					if(item.fundInfoDtoV2.typeId=='0110' || item.fundInfoDtoV2.typeId=='0210' ||item.fundInfoDtoV2.typeId=='0220'){
						htmls3 += "<dt>最新净值</dt>";
						var numlate = new Number(item.fundInfoDtoV2.latestNewValue);
						htmls3 += "<dd>"+numlate.toFixed(4)+"</dd>";
					}else{
						htmls3 += "<dt>业绩报酬计提基准</dt>";
						if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
							htmls3 += "<dd>浮动收益</dd>";
						} else {
							htmls3 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
									+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
						}
					}
					htmls3 += "</dl>";
					htmls3 += "<dl>";
					htmls3 += "<dt>理财期限</dt>";
					htmls3 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
					htmls3 += "</dl>";
					htmls3 += "<dl>";
					htmls3 += "<dt>起息日</dt>";
					htmls3 += "<dd>" + item.fundInfoDtoV2.interestDate + "</dd>";
					htmls3 += "</dl>";
					if (item.fee != 0 && item.fee != '') {
						htmls3 += "<dl class='addwaitpay'>";
					} else {
						htmls3 += "<dl>";
					}
					htmls3 += "<dt>买入金额</dt>";
					htmls3 += "<dd>";
					htmls3 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
					htmls3 += "</dd>";
					if ( item.fee != '' && item.fee != 0) {
						htmls3 += "<em>+" + item.fee + "（认购费）</em>";
					}
					htmls3 += "</dl>";
					htmls3 += "<dl class='schedule'>";
					htmls3 += "<p class='cirle cirle01'></p>";
					htmls3 += "<dt>投资进度</dt>";
					htmls3 += "<dd><p class='cirle cirle" + speed + "'></p><i>" + realSpeed + "<em style='font-weight: normal;'>%</em></i><b></b></dd>";
					htmls3 += "</dl>";
					htmls3 += "</div>";
					htmls3 += "</div>";
				});
			} else {
				htmls3 += "<span class='myorder-list-none'>暂无订单</span>";
			}

			/* 存续期订单 */
			if (gList != null && gList.length > 0) {

				$.each(gList, function(i, item) {
					var realSpeed = parseFloat(numMulti(numDiv((item.fundInfoDtoV2.scale - item.fundInfoDtoV2.displayLimit), item.fundInfoDtoV2.scale), 100)).toFixed(0);
					var speed = getSpeed(realSpeed);
					htmls4 += "<div class='myorder-list-box'>";
					htmls4 += "<div class='box-type saveextend'>存续中</div>";
					htmls4 += "<h1>";
					htmls4 += "<a href='/AppService/business/fund/orderDetail.shtml?serialno=" + item.serialno + "'  target='_blank'>" + item.fundInfoDtoV2.adname + "</a>";
					htmls4 += "</h1>";
					htmls4 += "<span class='date'>" + item.apdt + "</span> <span class='ordernumber'>订单号:<em>" + item.serialno + "</em></span>";
					htmls4 += "<div class='box-parameter'>";
					htmls4 += "<dl>";
					if(item.fundInfoDtoV2.typeId=='0110' || item.fundInfoDtoV2.typeId=='0210' ||item.fundInfoDtoV2.typeId=='0220'){
						htmls4 += "<dt>最新净值</dt>";
						var numlate = new Number(item.fundInfoDtoV2.latestNewValue);
						htmls4 += "<dd>"+numlate.toFixed(4)+"</dd>";
					}else if(item.fundInfoDtoV2.typeId=='0300'){
						if(item.shareAmt != null && item.shareAmt != "" && item.shareAmt != "0"){
							htmls4 += "<dt>已分配金额</dt>";
							htmls4 += "<dd>"+format(parseFloat(item.shareAmt))+"</dd>";
						}else{
							htmls4 += "<dt>业绩报酬计提基准</dt>";
							htmls4 += "<dd>浮动收益</dd>";
						}
					}else{
						
						htmls4 += "<dt>业绩报酬计提基准</dt>";
						if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
							htmls4 += "<dd>浮动收益</dd>";
						} else {
							htmls4 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
									+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
						}
					}
					htmls4 += "</dl>";
					htmls4 += "<dl>";
					htmls4 += "<dt>理财期限</dt>";
					htmls4 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
					htmls4 += "</dl>";
					htmls4 += "<dl>";
					if(item.fundInfoDtoV2.typeId=='0110'){
						htmls4 += "<dt>下一到期日</dt>";
					}else{
						htmls4 += "<dt>到期日</dt>";
					}
					htmls4 += "<dd>" + item.fundInfoDtoV2.maturityDate + "</dd>";
					htmls4 += "</dl>";
					if (item.fee != 0 && item.fee != '') {
						htmls4 += "<dl class='addwaitpay'>";
					} else {
						htmls4 += "<dl>";
					}
					htmls4 += "<dt>买入金额</dt>";
					htmls4 += "<dd>";
					if (item.fundInfoDtoV2.typeId=='0110') {
						htmls4 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
					} else {
						htmls4 += formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em>";
					}
					htmls4 += "</dd>";
					if ( item.fee != '' && item.fee != 0) {
						htmls4 += "<em>+" + item.fee + "（认购费）</em>";
					}
					htmls4 += "</dl>";
					htmls4 += "</div>";
					htmls4 += "</div>";
				});
			} else {
				htmls4 += "<span class='myorder-list-none'>暂无订单</span>";
			}

			/* 已到期订单 */
			if (zList != null && zList.length > 0) {
				$.each(zList, function(i, item) {
					var realSpeed = parseFloat(numMulti(numDiv((item.fundInfoDtoV2.scale - item.fundInfoDtoV2.displayLimit), item.fundInfoDtoV2.scale), 100)).toFixed(0);
					var speed = getSpeed(realSpeed);
					htmls5 += "<div class='myorder-list-box'>";
					htmls5 += "<div class='box-type timeout'>已到期</div>";
					htmls5 += "<h1>";
					htmls5 += "<a href='/AppService/business/fund/orderDetail.shtml?serialno=" + item.serialno + "'  target='_blank'>" + item.fundInfoDtoV2.adname + "</a>";
					htmls5 += "</h1>";
					htmls5 += "<span class='date'>"+item.apdt+"</span> <span class='ordernumber'>订单号:<em>" + item.serialno + "</em></span>";
					htmls5 += "<div class='box-parameter'>";
					htmls5 += "<dl>";
					if(item.fundInfoDtoV2.typeId=='0110' || item.fundInfoDtoV2.typeId=='0210' ||item.fundInfoDtoV2.typeId=='0220'){
						htmls5 += "<dt>最新净值</dt>";
						var numlate = new Number(item.fundInfoDtoV2.latestNewValue);
						htmls5 += "<dd>"+numlate.toFixed(4)+"</dd>";
					}else if(item.fundInfoDtoV2.typeId=='0300'){
						if(item.shareAmt != null && item.shareAmt != "" && item.shareAmt != "0"){
							htmls5 += "<dt>已分配金额</dt>";
							htmls5 += "<dd>"+format(parseFloat(item.shareAmt))+"</dd>";
						}else{
							htmls5 += "<dt>业绩报酬计提基准</dt>";
							htmls5 += "<dd>浮动收益</dd>";
						}
					}else{
						htmls5 += "<dt>业绩报酬计提基准</dt>";
						if (parseInt(item.fundInfoDtoV2.profit,10) == item.fundInfoDtoV2.profit && parseInt(item.fundInfoDtoV2.profit,10) == 0) {
							htmls5 += "<dd>浮动收益</dd>";
						} else {
							htmls5 += "<dd>" + parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[0] + "."
									+ parseFloat(numMulti((item.fundInfoDtoV2.profit || 0), 100)).toFixed(2).split('.')[1] + "%</dd>";
						}
					}
					if(item.fundInfoDtoV2.typeId=='0110'){
						htmls5 += "</dl>";
						htmls5 += "<dl>";
						htmls5 += "<dt>理财期限</dt>";
						htmls5 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
						htmls5 += "</dl>";
						if (item.fee != 0 && item.fee != '') {
							htmls5 += "<dl class='addwaitpay'>";
						} else {
							htmls5 += "<dl>";
						}
						htmls5 += "<dt>买入金额</dt>";
						htmls5 += "<dd>" + formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em></dd>";
						if ( item.fee != '' && item.fee != 0) {
							htmls5 += "<em>+" + item.fee + "（认购费）</em>";
						}
						htmls5 += "</dl>";
						htmls5 += "<dl>";
						htmls5 += "<dt>待收收益</dt>";
						if(isNaN(item.benefit) || isNaN(parseFloat(item.benefit)) || item.benefit == 0){/* 存续中 */
							htmls5 += "<dd>浮动收益";
						}else{
							htmls5 += "<dd>" + unformatNumber(item.benefit)+"元";
						}
						htmls5 += "</dd>";
						htmls5 += "</dl>";
						htmls5 += "</div>";
						htmls5 += "</div>";
					}else{
						htmls5 += "</dl>";
						htmls5 += "<dl>";
						htmls5 += "<dt>理财期限</dt>";
						htmls5 += "<dd>" + item.fundInfoDtoV2.term + item.fundInfoDtoV2.termUnit + "</dd>";
						htmls5 += "</dl>";
						if (item.fee != 0 && item.fee != '') {
							htmls5 += "<dl class='addwaitpay'>";
						} else {
							htmls5 += "<dl>";
						}
						htmls5 += "<dt>买入金额</dt>";
						htmls5 += "<dd>" + formatNumber(format(numDiv(item.subamt || 0, 10000))) + "<em>万</em></dd>";
						if ( item.fee != '' && item.fee != 0) {
							htmls5 += "<em>+" + item.fee + "（认购费）</em>";
						}
						htmls5 += "</dl>";
						htmls5 += "<dl>";
						htmls5 += "<dt>待收收益</dt>";
						if(isNaN(item.benefit) || isNaN(parseFloat(item.benefit)) || item.benefit == 0){/* 存续中 */
							htmls5 += "<dd>浮动收益";
						}else{
							htmls5 += "<dd>" + unformatNumber(item.benefit)+"元";
						}
						htmls5 += "</dd>";
						htmls5 += "</dl>";
						htmls5 += "</div>";
						htmls5 += "</div>";
					}
				});
			} else {
				htmls5 += "<span class='myorder-list-none'>暂无订单</span>";
			}

			$("#section_01").html(htmls1);
			$("#section_02").html(htmls2);
			$("#section_03").html(htmls3);
			$("#section_04").html(htmls4);
			$("#section_05").html(htmls5);
		}
	});
}
/* 获取进度条数值 */
function getSpeed(speed) {
	var result = 0;
	if (speed == 0) {
		result = 0;
	} else if (speed > 0 && speed < 5) {
		result = 5;
	} else if (speed >= 5 && speed < 10) {
		result = 10;
	} else if (speed >= 10 && speed < 15) {
		result = 15;
	} else if (speed >= 15 && speed < 20) {
		result = 20;
	} else if (speed >= 20 && speed < 25) {
		result = 25;
	} else if (speed >= 25 && speed < 30) {
		result = 30;
	} else if (speed >= 30 && speed < 35) {
		result = 35;
	} else if (speed >= 35 && speed < 40) {
		result = 40;
	} else if (speed >= 40 && speed < 45) {
		result = 45;
	} else if (speed >= 45 && speed < 50) {
		result = 50;
	} else if (speed >= 50 && speed < 55) {
		result = 55;
	} else if (speed >= 55 && speed < 60) {
		result = 60;
	} else if (speed >= 60 && speed < 65) {
		result = 65;
	} else if (speed >= 65 && speed < 70) {
		result = 70;
	} else if (speed >= 70 && speed < 75) {
		result = 75;
	} else if (speed >= 75 && speed < 80) {
		result = 80;
	} else if (speed >= 80 && speed < 85) {
		result = 85;
	} else if (speed >= 85 && speed < 90) {
		result = 90;
	} else if (speed >= 90 && speed < 95) {
		result = 95;
	} else if (speed >= 95 && speed <= 100) {
		result = 100;
	}
	return result;
}
/* 查看汇款信息 */
function queryOrder(orderType, serialno, subamt, fee, tradeacco, buyType, scale, productMoney, moneyStep, displayLimit,fundid) {
	getUserRequest("pc_applicationGroups_queryOrder");
	$.ajax({
		async : true,
		url : "/AppService/business/queryBankInfoByTradeAcco.xhtml",
		data : {
			tradeacco : tradeacco
		},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var bankacco = (data.bankacco || "").substr(data.bankacco.length - 4);
			var htmls = "";
			htmls += "<div class='main-con pay-offline'><input type='hidden' value='" + tradeacco + "' id='tradeacco'><input type='hidden' value='" + serialno
					+ "' id='serialno'><input type='hidden' value='" + subamt + "' id='subamt'><input type='hidden' value='" + fee + "' id='fee'><input type='hidden' value='" + buyType
					+ "' id='buyType'><input type='hidden' value='" + scale + "' id='scale'><input type='hidden' value='" + productMoney + "' id='productMoney'><input type='hidden' value='"
					+ moneyStep + "' id='moneyStep'><input type='hidden' value='" + displayLimit + "' id='displayLimit'><input type='hidden' value='" + fundid + "' id='fundid'>";
			htmls += "<i class='close'></i>";
			if (orderType != null && orderType == "1") {
				htmls += "<h2>线下汇款信息</h2>";
			} else if (orderType != null && orderType == "3") {
				htmls += "<h2>排队信息</h2>";
				htmls += "<span class='point' style='display:block;width:395px;margin:10px auto -20px;'>尚未获得产品份额，请勿汇款!</span>";
			}
			htmls += "<ul class='update'>";
			htmls += "<li><label for='payaccounts'>付款账号：</label> <span>" + data.realBankName + "(尾号" + bankacco + ")</span> <a href='javascript:gotoUpdateBankCard();'>更改银行卡</a></li>";
			htmls += "<li><label for='paymoney'>付款金额：</label> <span>" + formatNumber(parseFloat(subamt || 0) + parseFloat(fee || 0))
					+ "元</span> <a href='javascript:gotoUpdateMoney();'>修改金额</a></li>";
			htmls += "</ul>";
			htmls += "<ul class='paremeter'>";
			htmls += "<li><em>收款银行：</em><span>招商银行总行营业部</span></li>";
			htmls += "<li><em>收款账户：</em><span>招商财富资产管理有限公司</span></li>";
			htmls += "<li><em>收款账号：</em><span>9551 0827 0000 009</span></li>";
			htmls += "</ul>";
			htmls += "<a href='javascript:close_tips(\"updateOrderDiv\");' class='btn'>我知道了</a>";
			htmls += "</div>";
			$("#updateOrderDiv").html(htmls);
			$("#updateOrderDiv").show();
		}
	});
}
function gotoUpdateBankCard(){
	getUserRequest("pc_applicationGroups_updateBank");
	getRandomCode();
	$("#updateOrderBankCardDiv").show();
	$("#updateOrderDiv").hide();
	queryMyBankCard1();
	$(".select-value").click(function() {
		$(".select-text").toggleClass("none");
	});
	$(".cover_bg").bind("click", function(e) {
		var target = $(e.target);
		if (target.closest(".select-value,.select-text").length == 0) {
			$(".select-text").addClass("none");
		}
	});
}
/* 我的银行卡信息 */
function queryMyBankCard() {
	var tradeAcco = $("#tradeacco").val();
	$.ajax({
		async : false,
		url : "/AppService/business/queryMyBankCardNo.xhtml",
		data : {},
		dataType : "json",
		cache : false,
		type : "POST",
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == '0000') {
				var htmlBank = "";
				$.each(data.tradeAcctlist, function(i, item) {
					if (item.tradeAcco == tradeAcco) {
						$(".select-value").text(item.bankNm + "（尾号" + (item.bankAccoDisplay || 0).substr(item.bankAccoDisplay.length - 4) + ")");
						$(".select-value").attr("data-bankNm", item.bankNm);
						$(".select-value").attr("data-bankNo", item.bankNo);
						$(".select-value").attr("data-bankAccoDisplay", item.bankAccoDisplay);
						$(".select-value").attr("data-tradeAcco", item.tradeAcco);
						htmlBank += "<a href='javascript:selectCard(" + i + ")' class='act' id='card_" + i + "'data-bankNm='" + item.bankNm + "' data-bankNo='" + item.bankNo
								+ "' data-bankAccoDisplay='" + item.bankAccoDisplay + "' data-tradeAcco='" + item.tradeAcco + "'>" + item.bankNm + "（尾号"
								+ (item.bankAccoDisplay || 0).substr(item.bankAccoDisplay.length - 4) + "）</a>";
					} else {
						htmlBank += "<a href='javascript:selectCard(" + i + ")' id='card_" + i + "'data-bankNm='" + item.bankNm + "' data-bankNo='" + item.bankNo + "' data-bankAccoDisplay='"
								+ item.bankAccoDisplay + "' data-tradeAcco='" + item.tradeAcco + "'>" + item.bankNm + "（尾号" + (item.bankAccoDisplay || 0).substr(item.bankAccoDisplay.length - 4)
								+ "）</a>";
					}
				});
				$(".select-text-list").html(htmlBank);
			}
		}
	});
}
/* 我的银行卡信息 填充指定银行卡*/
function queryMyBankCard1() {
	var tradeAcco = $("#tradeacco").val();
	$.ajax({
		async : false,
		url : "/AppService/business/queryMyBankCardNo.xhtml",
		data : {},
		dataType : "json",
		cache : false,
		type : "POST",
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == '0000') {
				$.each(data.tradeAcctlist, function(i, item) {
					if (item.tradeAcco == tradeAcco) {
						$(".select-value").text(item.bankNm + "（尾号" + (item.bankAccoDisplay || 0).substr(item.bankAccoDisplay.length - 4) + ")");
						$(".select-value").attr("data-bankNm", item.bankNm);
						$(".select-value").attr("data-bankNo", item.bankNo);
						$(".select-value").attr("data-bankAccoDisplay", item.bankAccoDisplay);
						$(".select-value").attr("data-tradeAcco", item.tradeAcco);
						$(".select-text-list a[data-tradeAcco="+item.tradeAcco+"]").addClass("act");
					}
				});
			}
		}
	});
}
function selectCard(textId) {
	$(".select-value").text($("#card_" + textId).html());
	$(".select-value").attr("data-bankNm", $("#card_" + textId).attr("data-bankNm"));
	$(".select-value").attr("data-bankNo", $("#card_" + textId).attr("data-bankNo"));
	$(".select-value").attr("data-bankAccoDisplay", $("#card_" + textId).attr("data-bankAccoDisplay"));
	$(".select-value").attr("data-tradeAcco", $("#card_" + textId).attr("data-tradeAcco"));
	$("#card_" + textId).addClass("act").siblings().removeClass("act");
	$(".select-text").addClass("none");
}
/* 修改支付银行卡 */
function modifyAppointRequestBankCard() {
	var serialno = $("#serialno").val();
	var oldTradeAcco = $("#tradeacco").val();
	var tradeAcco = $(".select-value").attr("data-tradeAcco");
	var tPassWord = $("#tPassWordBank").val();
	var randomCode = $("#randomCode").val();

	if (oldTradeAcco == tradeAcco) {
		show_tips("待更换银行卡不能和原订单银行卡一致");
		return;
	} else if (tPassWord == null || tPassWord == "") {
		show_tips("请输入安全码");
		return;
	} else if (randomCode == null || randomCode == "") {
		show_tips("请输入验证码");
		return;
	} else {
		$("#tPassWordBank,#randomCode").val("");
		$.ajax({
			async : false,
			url : "/AppService/business/modifyAppointRequest.xhtml",
			data : {
				"serialno" : serialno,
				"tradeAcco" : tradeAcco,
				"tPassWord" : tPassWord,
				"randomCode" : randomCode
			},
			dataType : "json",
			cache : false,
			type : "POST",
			error : function(textStatus, errorThrown) {
				show_tips("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				if (data.returnCode != null && data.returnCode == "0000") {
					$("#updateOrderBankCardDiv").hide();
					show_tips("修改成功");
					setTimeout("window.location.reload();", 2000);
				} else if (data.returnCode == "USR-1I01") {
					show_tips("错误次数过多3小时后重试");
					$("#randomCode,#tPassWordBank").val("");
					$("#rondomCodeImg").click();
					return;
				} else if (data.returnCode == "USR-1I02") {
					if (data.tPwdErrCount == 1) {
						show_tips("安全码有误");
						$("#tPassWordBank").val("");
					} else if (data.tPwdErrCount > 1 && data.tPwdErrCount < 6) {
						show_tips("安全码有误还有" + (6 - parseInt(data.tPwdErrCount,10)) + "次机会");
					}
					$("#randomCode,#tPassWordBank").val("");
					$("#rondomCodeImg").click();
					return;
				} else {
					show_tips(data.returnMsg);
					$("#randomCode").val("");
					$("#rondomCodeImg").click();
					return;
				}
			}
		});
	}
}
/* 关闭修改银行卡弹窗 */
function closeUpdateOrderBankCard() {
	$("#tPassWordBank,#randomCode").val("");
	$("#rondomCodeImg").click();
	$("#updateOrderBankCardDiv").hide();
}
/* 修改金额弹窗 */
function gotoUpdateMoney() {
	getUserRequest("pc_applicationGroups_updateMoney");
	getRandomCode();
	$("#updateOrderDiv").hide();
	$("#updateOrderMoneyDiv").show();
	var money = $("#subamt").val();
	$("#newMoney").val(formatNumber(money, ','));
}
/* 修改订单金额 */
function modifyAppointRequestMoney() {
	var serialno = $("#serialno").val();
	var tradeAcco = $("#tradeacco").val();
	var tradeAmt = unformat($("#newMoney").val());
	var tPassWord = $("#tPassWordOrder").val();
	var randomCode = $("#randomCode1").val();
	var oldMoney = $("#subamt").val();
	if (!checkedMoney()) {
		return;
	} else if (tradeAmt == oldMoney) {
		show_tips("待修改金额不能和原订单金额一致");
		return;
	} else if (tPassWord == null || tPassWord == "") {
		show_tips("请输入安全码");
		return;
	} else if (randomCode == null || randomCode == "") {
		show_tips("请输入验证码");
		return;
	} else {
		var fee = $("#fee").val();
		$("#tPassWordOrder,#randomCode1").val("");
		$.ajax({
			async : false,
			url : "/AppService/business/modifyAppointRequest.xhtml",
			data : {
				"serialno" : serialno,
				"tradeAcco" : tradeAcco,
				"tradeAmt" : tradeAmt,
				"tPassWord" : tPassWord,
				"randomCode" : randomCode,
				"fee" : fee
			},
			dataType : "json",
			cache : false,
			type : "POST",
			error : function(textStatus, errorThrown) {
				show_tips("网络繁忙，请稍后再试。");
			},
			success : function(data) {
				if (data.returnCode != null && data.returnCode == "0000") {
					$("#updateOrderMoneyDiv").hide();
					show_tips("修改成功");
					setTimeout("window.location.reload();", 2000);
				} else if (data.returnCode == "USR-1I01") {
					show_tips("错误次数过多3小时后重试");
					$("#randomCode1,#tPassWordOrder").val("");
					$("#rondomCodeImg1").click();
					return;
				} else if (data.returnCode == "USR-1I02") {
					if (data.tPwdErrCount == 1) {
						show_tips("安全码有误");
					} else if (data.tPwdErrCount > 1 && data.tPwdErrCount < 6) {
						show_tips("安全码有误还有" + (6 - parseInt(data.tPwdErrCount,10)) + "次机会");
					}
					$("#randomCode1,#tPassWordOrder").val("");
					$("#rondomCodeImg1").click();
					return;
				} else {
					show_tips(data.returnMsg);
					$("#randomCode1").val("");
					$("#rondomCodeImg1").click();
					return;
				}
			}
		});
	}
}
/* 关闭修改金额弹窗 */
function closeUpdateMoney() {
	$("#tPassWordOrder,#randomCode1").val("");
	$("#rondomCodeImg1").click();
	$("#updateOrderMoneyDiv").hide();
}
/* 判断预约金额是否符合格式 */
function checkedMoney() {
	var scale = parseFloat(unformat($("#scale").val()));
	var moneyZero = parseFloat(unformat($("#productMoney").val()));/* 认购起点 */
	var money = parseFloat(unformat($.trim($("#newMoney").val())));/* 认购金额 */
	var moneyStep = parseFloat(unformat($("#moneyStep").val()));/* 认购步长 */
	var displayLimit = parseFloat(unformat($("#displayLimit").val()));/* 剩余额度 */
	var oldMoney = $("#subamt").val();
	var buyType = $("#buyType").val();

	if (!Validater.isPureNumber(money)) {
		show_tips("请输入正确的预约额度");
		return false;
	} else if (money >= 1000000000) {
		show_tips("输入金额过大，请重新输入");
		return false;
	} else if (money < moneyZero || (money - moneyZero) % moneyStep != 0) {
		show_tips("本产品" + formatNumber(numDiv(moneyZero, 10000), ',') + "万起售，" + formatNumber(numDiv(moneyStep, 10000), ',') + "万递增");
		return false;
	} else if (money - oldMoney > displayLimit && buyType != "4") {
		show_tips("仅剩下" + numDiv(displayLimit + oldMoney, 10000) + "万元份额");
		return false;
	} else if (buyType == "4" && money > scale) {
		show_tips("预约金额不能高于产品发售规模");
		return false;
	} else {
		queryFeeRateList();
		return true;
	}
}
/*查询费率和折扣*/
function queryFeeRateList(){
	var fundId = $("#fundid").val();
	fundId = removeSpecialStr(fundId);
	var channelNoList = "";
	var custLevel = "";
	var money = unformat($("#newMoney").val());
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
				if(rate == null || rate == "" || rate == "0"){
					/* 认购费：<em>1000</em>元*/
				}else{
					if(typeId == "0100"){
						$("#fee").val(data.rate);
					}else{
						$("#fee").val(data.rate);
					}
				}
			}
		}
	});
}
/* 查询代销订单列表 */
function queryAgentFundInfo(index) {
	$.ajax({
		async : true,
		url : "/AppService/setUp/queryCompanyUserProduct.xhtml",
		data : {},
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			var htmls1 = "";
			var htmls2 = "";
			var htmls3 = "";
			var htmls4 = "";
			var list = data.list;
			var gList = data.gList;
			var zList = data.zList;
			var wList=data.wList;
			var amt = 0;
			var profit = 0;
			var statusFund = "";

			/* 全部订单 */
			if (list != null && list.length > 0) {

				var pageSize=5;  //每页显示的条数
				var totalData =list.length;  //总条数
				var pageCount=Math.ceil(totalData/pageSize);   //总页数
				var pageNo=returnPage(index,pageCount);   //当前页数
				var pageHtml="";

				allOrder(pagination(pageNo, pageSize, list))

				function pagination(pageNo, pageSize, list) {
					var offset = (pageNo - 1) * pageSize;
					return (offset + pageSize >= list.length) ? list.slice(offset, list.length) : list.slice(offset, offset + pageSize);
				}

				var page = parseInt(pageNo);  //当前页数
				var count = parseInt(totalData);     //总条数
				var maxPages = pageCount;  //总页数



				pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page-1)+"\")' class='pre'></a>";
				if(page == 1){
					pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\"1\")'>1</a>";
				}else{
					pageHtml += "<a href='javascript:queryAgentFundInfo(\"1\")'>1</a>";
				}

				if(page > 3){/* 左边加...*/
					pageHtml += "<a class='omit'></a>";
				}

				if((page-1) >1){/* 上一页*/
					pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page-1)+"\")'>"+(page-1)+"</a>";
				}

				if(page != 1 && page != maxPages){/* 当前页*/
					pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\""+(page)+"\")'>"+(page)+"</a>";
				}

				if(page+1 < maxPages){/*下一页*/
					pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page+1)+"\")'>"+(page+1)+"</a>";
				}

				if((maxPages - page) > 2){/* 右边加...*/
					pageHtml += "<a class='omit'></a>";
				}

				if(page != 1){
					if(maxPages == page){
						pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}else if(maxPages > page){
						pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}else{
					if(maxPages == page){

					}else if(maxPages > page){
						pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}
				pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page+1)+"\")' class='next'></a>";
				pageHtml += "</div>";


				$(".page1").html(pageHtml);

			} else {
				htmls1 += "<span class='myorder-list-none'>暂无订单</span>";
				$("#other_01").html(htmls1);
			}
			/* 存续中 */
			if (gList != null && gList.length > 0) {
				var pageSize=5;  //每页显示的条数
				var totalData =gList.length;  //总条数
				var pageCount=Math.ceil(totalData/pageSize);   //总页数
				var pageNo=returnPage(index,pageCount);   //当前页数
				var pageHtml="";

				subsitOrder(pagination(pageNo, pageSize, gList))

				function pagination(pageNo, pageSize, gList) {
					var offset = (pageNo - 1) * pageSize;
					return (offset + pageSize >= gList.length) ? gList.slice(offset, gList.length) : gList.slice(offset, offset + pageSize);
				}

				var page = parseInt(pageNo);  //当前页数
				var count = parseInt(totalData);     //总条数
				var maxPages = pageCount;  //总页数



				pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page-1)+"\")' class='pre'></a>";
				if(page == 1){
					pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\"1\")'>1</a>";
				}else{
					pageHtml += "<a href='javascript:queryAgentFundInfo(\"1\")'>1</a>";
				}

				if(page > 3){/* 左边加...*/
					pageHtml += "<a class='omit'></a>";
				}

				if((page-1) >1){/* 上一页*/
					pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page-1)+"\")'>"+(page-1)+"</a>";
				}

				if(page != 1 && page != maxPages){/* 当前页*/
					pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\""+(page)+"\")'>"+(page)+"</a>";
				}

				if(page+1 < maxPages){/*下一页*/
					pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page+1)+"\")'>"+(page+1)+"</a>";
				}

				if((maxPages - page) > 2){/* 右边加...*/
					pageHtml += "<a class='omit'></a>";
				}

				if(page != 1){
					if(maxPages == page){
						pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}else if(maxPages > page){
						pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}else{
					if(maxPages == page){

					}else if(maxPages > page){
						pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}
				pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page+1)+"\")' class='next'></a>";
				pageHtml += "</div>";


				$(".page2").html(pageHtml);
			} else {
				htmls2 += "<span class='myorder-list-none'>暂无订单</span>";
				$("#other_02").html(htmls2);
			}
			/* 已到期 */
			if (zList != null && zList.length > 0) {
				var pageSize=5;  //每页显示的条数
				var totalData =zList.length;  //总条数
				var pageCount=Math.ceil(totalData/pageSize);   //总页数
				var pageNo=returnPage(index,pageCount);   //当前页数
				var pageHtml="";

				periodOrder(pagination(pageNo, pageSize, zList))

				function pagination(pageNo, pageSize, zList) {
					var offset = (pageNo - 1) * pageSize;
					return (offset + pageSize >= zList.length) ? zList.slice(offset, zList.length) : zList.slice(offset, offset + pageSize);
				}

				var page = parseInt(pageNo);  //当前页数
				var count = parseInt(totalData);     //总条数
				var maxPages = pageCount;  //总页数



				pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page-1)+"\")' class='pre'></a>";
				if(page == 1){
					pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\"1\")'>1</a>";
				}else{
					pageHtml += "<a href='javascript:queryAgentFundInfo(\"1\")'>1</a>";
				}

				if(page > 3){/* 左边加...*/
					pageHtml += "<a class='omit'></a>";
				}

				if((page-1) >1){/* 上一页*/
					pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page-1)+"\")'>"+(page-1)+"</a>";
				}

				if(page != 1 && page != maxPages){/* 当前页*/
					pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\""+(page)+"\")'>"+(page)+"</a>";
				}

				if(page+1 < maxPages){/*下一页*/
					pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page+1)+"\")'>"+(page+1)+"</a>";
				}

				if((maxPages - page) > 2){/* 右边加...*/
					pageHtml += "<a class='omit'></a>";
				}

				if(page != 1){
					if(maxPages == page){
						pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}else if(maxPages > page){
						pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}else{
					if(maxPages == page){

					}else if(maxPages > page){
						pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}
				pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page+1)+"\")' class='next'></a>";
				pageHtml += "</div>";


				$(".page3").html(pageHtml);
			} else {
				htmls3 += "<span class='myorder-list-none'>暂无订单</span>";
				$("#other_03").html(htmls3);
			}
			/* 白名单 */
			if (wList != null && wList.length > 0) {
				$("#black").show();
				$(".myorder.otherorder .myorder-con .myorder-con-nav ul li,.myorder.otherorder .myorder-con .myorder-con-nav ul li a").addClass("foursWidth")
				var pageSize=5;  //每页显示的条数
				var totalData =wList.length;  //总条数
				var pageCount=Math.ceil(totalData/pageSize);   //总页数
				var pageNo=returnPage(index,pageCount);   //当前页数
				var pageHtml="";

				whiteOrder(pagination(pageNo, pageSize, wList))

				function pagination(pageNo, pageSize, wList) {
					var offset = (pageNo - 1) * pageSize;
					return (offset + pageSize >= wList.length) ? wList.slice(offset, wList.length) : wList.slice(offset, offset + pageSize);
				}

				var page = parseInt(pageNo);  //当前页数
				var count = parseInt(totalData);     //总条数
				var maxPages = pageCount;  //总页数



				pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page-1)+"\")' class='pre'></a>";
				if(page == 1){
					pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\"1\")'>1</a>";
				}else{
					pageHtml += "<a href='javascript:queryAgentFundInfo(\"1\")'>1</a>";
				}

				if(page > 3){/* 左边加...*/
					pageHtml += "<a class='omit'></a>";
				}

				if((page-1) >1){/* 上一页*/
					pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page-1)+"\")'>"+(page-1)+"</a>";
				}

				if(page != 1 && page != maxPages){/* 当前页*/
					pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\""+(page)+"\")'>"+(page)+"</a>";
				}

				if(page+1 < maxPages){/*下一页*/
					pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page+1)+"\")'>"+(page+1)+"</a>";
				}

				if((maxPages - page) > 2){/* 右边加...*/
					pageHtml += "<a class='omit'></a>";
				}

				if(page != 1){
					if(maxPages == page){
						pageHtml += "<a class='act' href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}else if(maxPages > page){
						pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}else{
					if(maxPages == page){

					}else if(maxPages > page){
						pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(maxPages)+"\")'>"+maxPages+"</a>";
					}
				}
				pageHtml += "<a href='javascript:queryAgentFundInfo(\""+(page+1)+"\")' class='next'></a>";
				pageHtml += "</div>";


				$(".page4").html(pageHtml);

			} else {
				htmls4 += "<span class='myorder-list-none'>暂无订单</span>";
				$("#other_04").html(htmls4);

			}



		}
	});
}
/* 全部/存续/已到期 互相切换 */
function showOtherOrderList(index) {
	getUserRequest("pc_applicationGroups_otherOrder_0"+index);
	$(".otherorder .myorder-con-nav ul li").removeClass("act");
	$(".otherorder .myorder-con-nav ul li:eq(" + index + ")").addClass("act");

	$("#other_01").hide();
	$("#other_02").hide();
	$("#other_03").hide();
	$("#other_04").hide();
	$("#other_0" + (index + 1)).show();
	$(".myorder-con-nav li").each(function () {
		if($(this).hasClass("act")){
			var index=$(this).index();
			$(".pag").eq(index).show().siblings().hide();
		}
	})
	queryAgentFundInfo(1);

}
changeOrderList(0)
/* 其他代销订单和订单列表切换 */
function changeOrderList(index) {
	// $(".otherorder .morder-con-nav ul li").removeClass("act");
	// $(".otherorder .myorder-con-nav ul li:eq(" + index + ")").addClass("act");
	if (index == '1') {
		getUserRequest("pc_applicationGroups_accountInfo");
		$("#otherOrderList").hide();
		$("#orderList").show();
	} else {
		getUserRequest("pc_applicationGroups_otherOrder");
		queryAgentFundInfo(1);
		$("#otherOrderList").show();
		$("#orderList").hide();
	}
}
//queryAgentFundInfo(1);
checkCompanyUser()
/*查询用户是否已经登录*/
function checkCompanyUser(){
	$.ajax({
		async:false,
		url: "/AppService/setUp/checkCompanyUser.xhtml",
		dataType: "json",
		type:"POST",
		data: {},
		cache: false,
		error : function(textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success : function (data) {
			if(data.returnCode != null && data.returnCode == "0000"){
				$("#loginYesPublic").show();
				$("#login_userinfo_public").html(data.fundAccoEncry).prop("href","/company/companyAccountInfo.shtml");
				$("#loginYes").hide();
				$("#loginNo").hide();
				$(".My-center").attr("href","/company/companyAccountInfo.shtml");

			}else{
				goToURL("/login/login.shtml");
			}
		}
	});
}
/* 退出登录 */
function checkOutLoginForCompany(){
	$.ajax({
		async : true,
		url : "/AppService/setUp/logoutForCompany.xhtml",
		data : "",
		dataType : "json",
		cache : false,
		type : "post",
		error : function(textStatus, errorThrown) {
		},
		success : function(data) {
			window.location.href = "/login/login.shtml";
		}
	});
};

function allOrder(list) {
	var htmls1 = "";
	$.each(list, function(i, item) {
		amt = numDiv(parseFloat(item.bugAmt + item.fee), 10000) || 0;
		statusFund = item.applySt != null && item.applySt == "G" ? "存续中" : "已到期";
		if (parseInt(amt,10) != amt) {
			amt = parseFloat(amt.toFixed(4));
		}
		profit = parseFloat(item.profit).toFixed(2);
		htmls1 += "<div class='myorder-list-box'>";
		htmls1 += "<div class='box-type waiting-pay'>" + statusFund + "</div>";
		var orderType = item.applySt == "G" ? "4" : "5";
		if(item.hasDetail=='Y'){

			if(item.typeName=="whiteList"){
				htmls1 += "<h1><a href='/company/companyOtherOrderWhiteDetail.shtml?serialno="+item.serialno+"&fundId="+item.fundid+"&channleNo="+item.channleNo+"'  target='_blank'>" + item.fundnm + "</a></h1>";
			}else{
				htmls1 += "<h1><a href='/company/companyOtherOrderDetail.shtml?serialno="+item.serialno+"&fundId="+item.fundid+"&channleNo="+item.channleNo+"'  target='_blank'>" + item.fundnm + "</a></h1>";
			}


		}else{
			htmls1 += "<h1><a class='noCursor' style='text-decoration:none' href='javascript:void(0);'>" + item.fundnm + "</a></h1>";
		}
//					htmls1 += "<span class='date'>" + item.buyDate + "</span>";
		htmls1 += "<span class='ordernumber_agent'>代销机构：<em>" + item.channleName + "</em></span>";
		htmls1 += "<div class='box-parameter'>";
		if (item.applySt != null && item.applySt == "G") {
			htmls1 += "<dl class='first'>";
			htmls1 += "<dt>买入金额</dt>";
			htmls1 += "<dd>" + amt + "<em>万</em>";
			htmls1 += "</dd>";
			htmls1 += "</dl>";
			htmls1 += "<dl class='second'>";
			htmls1 += "<dt>买入日期</dt>";
			htmls1 += "<dd>" + item.buyDate + "</dd>";
			htmls1 += "</dl>";
			var numlate = new Number(item.nav);
			if (item.nav != null &&item.nav != '' &&typeof(item.nav) != 'undefined' && numlate != 0)  {
				htmls1 += "<dl class='third'>";
				htmls1 += "<dt>最新净值</dt>";
				var numlate = new Number(item.nav);
				htmls1 += "<dd>" + numlate.toFixed(4) + "</dd>";
				htmls1 += "</dl>";
			}
		} else {
			htmls1 += "<dl class='first'>";
			htmls1 += "<dt>买入金额</dt>";
			htmls1 += "<dd>" + amt + "<em>万</em>";
			htmls1 += "</dd>";
			htmls1 += "</dl>";
			htmls1 += "<dl class='second'>";
			htmls1 += "<dt>产品收益</dt>";
			htmls1 += "<dd>" + profit + "元</dd>";
			htmls1 += "</dl>";
			htmls1 += "<dl class='third'>";
			htmls1 += "<dt>到期日期</dt>";
			htmls1 += "<dd>" + item.cycleenddt + "</dd>";
			htmls1 += "</dl>";
		}
		htmls1 += "</div>";
		htmls1 += "</div>";
	});
	$("#other_01").html(htmls1);
}

function subsitOrder(list){
	var htmls2="";
	$.each(list, function(i, item) {
		amt = numDiv(parseFloat(item.bugAmt + item.fee), 10000) || 0;
		statusFund = item.applySt != null && item.applySt == "G" ? "存续中" : "已到期";
		if (parseInt(amt,10) != amt) {
			amt = parseFloat(amt.toFixed(4));
		}
		profit = parseFloat(item.profit).toFixed(2);
		htmls2 += "<div class='myorder-list-box'>";
		htmls2 += "<div class='box-type waiting-pay'>" + statusFund + "</div>";
		var orderType = item.applySt == "G" ? "4" : "5";
		if(item.hasDetail=='Y'){
			htmls2 += "<h1><a href='/company/companyOtherOrderDetail.shtml?serialno="+item.serialno+"&fundId="+item.fundid+"&channleNo="+item.channleNo+"'  target='_blank'>" + item.fundnm + "</a></h1>";
		}else{
			htmls2 += "<h1><a class='noCursor' style='text-decoration:none' href='javascript:void(0);'>" + item.fundnm + "</a></h1>";
		}
//		htmls2 += "<span class='date'>" + item.buyDate + "</span>";
		htmls2 += "<span class='ordernumber_agent'>代销机构：<em>" + item.channleName + "</em></span>";
		htmls2 += "<div class='box-parameter'>";
		htmls2 += "<dl class='first'>";
		htmls2 += "<dt>买入金额</dt>";
		htmls2 += "<dd>" + amt + "<em>万</em>";
		htmls2 += "</dd>";
		htmls2 += "</dl>";
		htmls2 += "<dl class='second'>";
		htmls2 += "<dt>买入日期</dt>";
		htmls2 += "<dd>" + item.buyDate + "</dd>";
		htmls2 += "</dl>";
		var numlate = new Number(item.nav);
		if (item.nav != null &&item.nav != '' &&typeof(item.nav) != 'undefined' && numlate != 0) {
			htmls2 += "<dl class='third'>";
			htmls2 += "<dt>最新净值</dt>";

			htmls2 += "<dd>" + numlate.toFixed(4) + "</dd>";
			htmls2 += "</dl>";
		}
		htmls2 += "</div>";
		htmls2 += "</div>";
	});
	$("#other_02").html(htmls2);
}

function periodOrder(list){
	var htmls3 = "";
	$.each(list, function(i, item) {
		amt = numDiv(parseFloat(item.bugAmt + item.fee), 10000) || 0;
		statusFund = item.applySt != null && item.applySt == "G" ? "存续中" : "已到期";
		if (parseInt(amt,10) != amt) {
			amt = parseFloat(amt.toFixed(4));
		}

		profit = parseFloat(item.profit).toFixed(2);
		htmls3 += "<div class='myorder-list-box'>";
		htmls3 += "<div class='box-type waiting-pay'>" + statusFund + "</div>";
		if(item.hasDetail=='Y'){
			htmls3 += "<h1><a href='/company/companyOtherOrderDetail.shtml?serialno="+item.serialno+"&fundId="+item.fundid+"&channleNo="+item.channleNo+"'  target='_blank'>" + item.fundnm + "</a></h1>";
		}else{
			htmls3 += "<h1><a class='noCursor' style='text-decoration:none' href='javascript:void(0);'>" + item.fundnm + "</a></h1>";
		}
//					htmls3 += "<span class='date'>" + item.buyDate + "</span>";
		htmls3 += "<span class='ordernumber_agent'>代销机构：<em>" + item.channleName + "</em></span>";
		htmls3 += "<div class='box-parameter'>";
		htmls3 += "<dl class='first'>";
		htmls3 += "<dt>买入金额</dt>";
		htmls3 += "<dd>" + amt + "<em>万</em>";
		htmls3 += "</dd>";
		htmls3 += "</dl>";
		htmls3 += "<dl class='second'>";
		htmls3 += "<dt>产品收益</dt>";
		htmls3 += "<dd>" + profit + "元</dd>";
		htmls3 += "</dl>";
		htmls3 += "<dl class='third'>";
		htmls3 += "<dt>到期日期</dt>";
		htmls3 += "<dd>" + item.cycleenddt + "</dd>";
		htmls3 += "</dl>";
		htmls3 += "</div>";
		htmls3 += "</div>";
	});
	$("#other_03").html(htmls3);
}

function whiteOrder(list) {
	var htmls4 = "";
	$.each(list, function(i, item) {
		amt = numDiv(parseFloat(item.bugAmt + item.fee), 10000) || 0;
		statusFund = item.applySt != null && item.applySt == "G" ? "存续中" : "已到期";
		if (parseInt(amt,10) != amt) {
			amt = parseFloat(amt.toFixed(4));
		}

		profit = parseFloat(item.profit).toFixed(2);
		htmls4 += "<div class='myorder-list-box'>";
		htmls4 += "<div class='box-type waiting-pay'>" + statusFund + "</div>";
		if(item.hasDetail=='Y'){
			htmls4 += "<h1><a href='/company/companyOtherOrderWhiteDetail.shtml?serialno="+item.serialno+"&fundId="+item.fundid+"&channleNo="+item.channleNo+"'  target='_blank'>" + item.fundnm + "</a></h1>";
		}else{
			htmls4 += "<h1><a class='noCursor' style='text-decoration:none' href='javascript:void(0);'>" + item.fundnm + "</a></h1>";
		}
//					htmls3 += "<span class='date'>" + item.buyDate + "</span>";
		htmls4 += "<span class='ordernumber_agent'>代销机构：<em>" + item.channleName + "</em></span>";
		htmls4 += "<div class='box-parameter'>";
		htmls4 += "<dl class='first'>";
		htmls4 += "<dt>买入金额</dt>";
		htmls4 += "<dd>" + amt + "<em>万</em>";
		htmls4 += "</dd>";
		htmls4 += "</dl>";
		htmls4 += "<dl class='second'>";
		htmls4 += "<dt>产品收益</dt>";
		htmls4 += "<dd>" + profit + "元</dd>";
		htmls4 += "</dl>";
		htmls4 += "<dl class='third'>";
		htmls4 += "<dt>到期日期</dt>";
		htmls4 += "<dd>" + item.cycleenddt + "</dd>";
		htmls4 += "</dl>";
		htmls4 += "</div>";
		htmls4 += "</div>";
	});
	$("#other_04").html(htmls4);
}

function returnPage(pages,max){
	var page;
	var maxPages = max;

	if(parseInt(pages,10) >= parseInt(maxPages,10)){
		page = maxPages;
	}else if(parseInt(pages,10) <= 0){
		page = "1";
	}else{
		page = pages;
	}
	return page;
}

