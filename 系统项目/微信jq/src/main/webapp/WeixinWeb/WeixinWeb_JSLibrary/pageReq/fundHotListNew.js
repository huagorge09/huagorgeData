var myScroll;
var refreshCount = 3;

document.addEventListener('touchmove', function(e) {
	e.preventDefault();
}, false);

$(document).ready(function(e) {

	$(".header .top-a h2").html("热销产品");
	document.title = "热销产品";
	var u = navigator.userAgent.toLowerCase();
	if (u.match(/MicroMessenger/i) != "micromessenger") { /* 不是微信端则把顶部的位置空出来 */
		$("#wrapper").css("margin-top","50px");
	}
	myScroll = new IScroll('#wrapper', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true
	});

	queryFundList();/* 查询产品列表 */

	forRefensh();

	/*showAndHideDivFundType(getUrlParameter("item") || 0);*/
	getUserRequest("fund-info-0");/* 此处subPath为页面内行为 */
});

/* 页面初始加载，查询产品列表后，定时刷新滚动条，时间3秒，每1秒刷新1次 */
function forRefensh() {
	if (refreshCount > 0) {
		refreshCount--;
		myScroll.refresh();
		window.setTimeout("forRefensh()", 1000);
	} else {
		return;
	}
}

/* 查询产品列表 */
function queryFundList() {
	var urlVal = "/WeixinService/business/queryHotFundList.xhtml";
	$.ajax({
		async : false,
		url : urlVal,
		type : "post",
		dataType : 'json',
		data : {},
		error : function() {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data, textStatus) {
			if (data.returnCode == '0000') {
				$.each(data.list, function(i, items) {
					commonMenth(i, items); /* 热销产品列表 */
				});
				var temp = "<div style='text-align: center;'><br/>暂无该类别产品，请查看其他产品！</div>";
				if ($("#fundTypeDiv1").html() == "") {
					$("#fundTypeDiv1").html(temp);
				}
			} else {
				errorRemark("网络繁忙，请稍后再试");
			}
		}
	});
}

/* 产品列表公用拼接方法 */
function commonMenth(i, items) {
	var item = items[0];
	var listhtml = "";/* 产品列表 */
	listhtml += '<div class="pro-box" onclick="redirectUrl(\'/WeixinService/business/query/fundInfoNew.shtml?fundId=' + item.fundId + '&period='+item.period+'\')">';
	var currentWorkdate = item.currentWorkdate;/* 当前工作日 */
	var appointDate = item.appointDate;/* 预约开始日期 */
	var appointEndDate = item.appointEndDate;/* 预约结束日期 */
	var subdeadLine = item.subdeadLine;/* 认购截止日 */
	var cirle = parseFloat(numMulti(numDiv((item.scale - item.displayLimit), item.scale), 100)).toFixed(0);

	if (daysBetween(currentWorkdate, appointDate) >= 0 && daysBetween(currentWorkdate, subdeadLine) <= 0) {
		if (daysBetween(currentWorkdate, appointDate) >= 0 && daysBetween(currentWorkdate, appointEndDate) <= 0) {/* 预约期 */
			if (cirle < 100) {
				listhtml += '<span class="prompt"><b>募集中</b></span>';
			} else {
				listhtml += '<span class="prompt"><b>排队中</b></span>';
			}
		} else {/* 认购期 */
			if (cirle < 100) {
				listhtml += '<span class="prompt"><b>募集中</b></span>';
			} else {
				listhtml += '<span class="prompt"><b>募集结束</b></span>';
			}
		}
	} else {
		cirle = 100;
		listhtml += '<span class="prompt act"><b>募集结束</b></span>';
	}

	listhtml += '<h2><a>' + item.adname + '</a><i>' + item.typeName + '</i></h2>';
	listhtml += '<div class="asset">';
	listhtml += '<dl class="income upadateorder-income">';

	/* 产品类型id 0100:固定收益 0210：开放性净值类产品 0220：封闭净值 0300：浮动收益 */
	if (item.typeId == '0100' || item.typeId == '0110') {/* 固定收益 年化收益（百分数，保留两位小数）、理财期限、募集规模、投资进度 */
		var profitValue = numMulti(parseFloat(item.profit || 0), 100);
		profitValue = profitValue.toFixed(2);
		var str = profitValue.split(".");
		if (parseInt(profitValue) == profitValue && parseInt(profitValue) == 0) {
			listhtml += '<dt><i>浮动收益</i></dt>';
		} else {
			listhtml += '<dt>' + str[0] + '<i>.' + str[1] + '</i><b>%</b></dt>';
		}
		listhtml += ' <dd>业绩报酬计提基准</dd>';
	}else if (item.typeId == '0210') {/*
										 * 开放性净值类产品
										 * 展示最新净值（保留四位小数）、历史最高净值（保留四位小数）、累计收益率（百分数，保留两位小数）、申赎规则（每日、每周、每月）
										 */
		var profitValue = parseFloat(item.profit);
		profitValue = profitValue.toFixed(4);
		var str = profitValue.split(".");
		listhtml += '<dt>' + str[0] + '<i>.' + str[1] + '</i></dt>';
		listhtml += '<dd>最新净值</dd>';
	} else if (item.typeId == '0220') {/*
										 * 浮动收益 封闭净值类产品
										 * 展示年化收益（浮动收益）、理财期限、募集规模、投资进度
										 */
		listhtml += '<dt><i>浮动收益</i></dt>';
		listhtml += '<dd>业绩报酬计提基准</dd>';
	} else if (item.typeId == '0300') {/*
										 * 浮动收益 封闭净值类产品
										 * 年化收益（浮动收益）、理财期限、募集规模、投资进度。
										 */
		listhtml += '<dt><i>浮动收益</i></dt>';
		listhtml += '<dd>业绩报酬计提基准</dd>';
	}
	listhtml += '</dl>';
	listhtml += '<dl>';
	listhtml += '<dt><i>' + item.term + '</i><b>' + item.termUnit + '</b></dt>';
	listhtml += ' <dd>理财期限</dd>';
	listhtml += '</dl>';
	listhtml += '<dl>';
	listhtml += '<dt><i>' + numDiv(item.scale, 10000) + '</i><b>万</b></dt>';
	listhtml += '<dd>募集规模</dd>';
	listhtml += '</dl>';
	listhtml += '<dl class="schedule">';
	var speed = parseFloat(numMulti(numDiv((item.scale - item.displayLimit), item.scale), 100)).toFixed(0);
	var isExpired = speed;
	if (daysBetween(currentWorkdate, appointDate) >= 0 && daysBetween(currentWorkdate, subdeadLine) <= 0) {
		if (speed == 0) {
			speed = 0;
		} else if (speed > 0 && speed < 5) {
			speed = 5;
		} else if (speed >= 5 && speed < 10) {
			speed = 10;
		} else if (speed >= 10 && speed < 15) {
			speed = 15;
		} else if (speed >= 15 && speed < 20) {
			speed = 20;
		} else if (speed >= 20 && speed < 25) {
			speed = 25;
		} else if (speed >= 25 && speed < 30) {
			speed = 30;
		} else if (speed >= 30 && speed < 35) {
			speed = 35;
		} else if (speed >= 35 && speed < 40) {
			speed = 40;
		} else if (speed >= 40 && speed < 45) {
			speed = 45;
		} else if (speed >= 45 && speed < 50) {
			speed = 50;
		} else if (speed >= 50 && speed < 55) {
			speed = 55;
		} else if (speed >= 55 && speed < 60) {
			speed = 60;
		} else if (speed >= 60 && speed < 65) {
			speed = 65;
		} else if (speed >= 65 && speed < 70) {
			speed = 70;
		} else if (speed >= 70 && speed < 75) {
			speed = 75;
		} else if (speed >= 75 && speed < 80) {
			speed = 80;
		} else if (speed >= 80 && speed < 85) {
			speed = 85;
		} else if (speed >= 85 && speed < 90) {
			speed = 90;
		} else if (speed >= 90 && speed < 95) {
			speed = 95;
		} else if (speed >= 95 && speed <= 100) {
			speed = 100;
		}
	} else {
		speed = 100;
		isExpired = 100;
	}
	listhtml += '<dt><p class="cirle cirle' + speed + '"></p><i>' + isExpired + '<em style="font-weight: normal;">%</em></i><b></b></dt>';
	listhtml += '<dd>投资进度</dd>';
	listhtml += '</dl>';
	listhtml += '</div>';
	listhtml += '<div class="pro-box-bottom clear">';
	listhtml += '<span>认购起点：' + numDiv(item.money, 10000) + '万起投，' + numDiv(item.moneyStep, 10000) + '万递增</span>';
	listhtml += ' </div>';
	listhtml += ' </div>';
	$("#fundTypeDiv1").append(listhtml);

}

/* 理财类型事件 */
/*function showAndHideDivFundType(index) {
	$(".tab_box ul li a").removeClass("act");
	$("section div.pro-list.clear").hide();
	$(".tab_box ul li a:eq(" + index + ")").addClass("act");
	$("section div.pro-list.clear:eq(" + index + ")").show();
	getUserRequest("fund-list-" + index); 此处subPath为页面内行为 
	myScroll.refresh();
}*/
