var myScroll;
var refreshCount = 3;
var USERBASEINFO  = null;
var eventId = 'event_wx_fortuneCenterId',pageId = 'wx_fortuneCenterId',pageSource = 'wx_fortuneCenterId';
var flag;
var isHot=false
//是否显示七天开关
var seven ='0';
try{
	seven = queryParamList("SYSTEM","SHOWNETVALUE","")[0].pmco;
}catch(err){
	console.log("开关查询失败");
}
function loaded() {
	queryBanner();
	$('#bannerDiv').flexslider({
		animation : "slide",
		direction : "horizontal",
	});
	$(".flexslider .flex-direction-nav").css('display', 'none');
	$("#menu_f1,#menu_f2,#menu_f3").removeClass("act");
	$("#menu_f1").addClass("act");
	$("#menu_f1").addClass("active");
}
/* 未风险测评则弹出风险测评弹框  */
function checkIsRiskLevel(){
	flag = true;
	var urlVal="/WeixinService/business/queryIsNeedTest.xhtml";
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
			//data.riskLevel=='0'
			var riskLevel = "";
			var riskEvalDate = "";
			
			if(!!data){
				riskLevel = data.riskLevel;
				riskEvalDate = data.riskEvalDate;
			}
			
			if(!riskEvalDate || "0" == riskLevel){
				flag = false;
				//继续测评 标识
				checkUserBaseInfoIsExist(1);
			}
			var nowDate= new Date();
		    var date = new Date(riskEvalDate); 
		    var dataDiff = (nowDate - date) / 86400000;
		    if(dataDiff >= 365){
		    	//时间已过 则 提示过期
		    	checkUserBaseInfoIsExist(2);
		    	flag = false;
		    }
		}
	});   
   
}

function show_question(){
	$('#question_tip').show();
	var height=0.95*window.innerHeight;
	var width=0.9*window.innerWidth;
	var leftwidth=window.innerWidth*0.105/2;
	var leftheight=window.innerHeight*0.053/2;
	console.info(height);
	console.info(width);
	console.info(leftwidth);
	console.info(leftheight);
	var style={'height':height+'px','width': width+'px','margin-left': leftwidth+'px','margin-top':leftheight+'px'}
	$('#question_tip').children().eq(0).css(style);

	//计算span left
	 leftwidth  = ($('.span').parent().width()-$('.span').width())/2
	 leftheight=0.03*window.innerHeight;
	$('.span').css('margin-left',leftwidth+'px');
	$('.span').css('top',leftheight+'px');

	//设置问题匡主题左边距
	var contentleft=0.035*window.innerWidth;
	var contentWidth=0.835*window.innerWidth;
	var contentHeight =0.56*window.innerHeight;
	var contentStyle={'margin-left':contentleft+'px','margin-right':contentleft+'px','width':contentWidth+'px'/*,'height':contentHeight+'px'*/}
	$('#question_tip').children().children().eq(1).css(contentStyle);


	//设置按钮样式
	var buttonTop =0.06*window.innerHeight;
	var buttonStyle={'margin-top':buttonTop+'px'}
	$('#question_tip').children().children().eq(2).css(buttonStyle);
	// $('.foot').hide();
}

document.addEventListener('touchmove', function(e) {
	e.preventDefault();
}, false);

$(document).ready(function(e) {
	
	
	var urlParams = getUrlParams();
	eventId = urlParams['eventId']?urlParams['eventId']:eventId;
	pageSource = urlParams['pageSource']?urlParams['pageSource']:pageSource;
	if(eventId && 'event_wxModelMess_ProCenterId' == eventId){
		//记录点击3图文消息
		pageSource = 'wx_model_productId';
		recordOperation({eventId:eventId,pageId:pageSource,pageSource:pageSource});
	}
	
	$(".header .top-a h2").html("产品中心");
	document.title = "产品中心";
	myScroll = new IScroll('#wrapper', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true
	});
	
	//将测评标识赋值
	var riskContinue = GetQueryString("riskContinue");
	$("input[name=fundRiskContinue]:eq(0)").val(riskContinue);
	
	USERBASEINFO = queryUserinfo();
	
	if(!!USERBASEINFO && "Y" != riskContinue){
		if(!!USERBASEINFO.syncInvprtpAlert && "Y" == USERBASEINFO.syncInvprtpAlert){
			showTips("tips_invest_update");
		}
	}
	
	checkIsRiskLevel();
	//风评 后的用户才能显示产品列表
	if(flag && "Y" != riskContinue){
		queryFundList();/* 查询产品列表 */
	}

	forRefensh();
	
	var targetEle = document.getElementsByClassName("boxs-btn")[0].children[0];
	
	targetEle.addEventListener("touchend",function(event){
		// 如果这个元素的位置内只有一个手指的话
	    if (event.targetTouches.length == 1) {
	    	event.preventDefault();// 阻止浏览器默认事件，重要
	    	checkUserBaseInfoIsExist();
	    }
	});
	
	showAndHideDivFundType(getUrlParameter("item") || 0);
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
	var urlVal = "/WeixinService/business/queryFundList.xhtml";
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
				var list1 = data.list.gruopId20001; //定期理财
				var list2 = data.list.gruopId20032; //活期理财
				var list3 = data.list.gruopId20002; //另类投资
				var list4 = data.list.gruopId20003; //权益投资
				if (list1 != 'undefined' && list1 != null) {
					/* 定期理财 */
					$.each(list1, function(i, item) {
						commonMenth(i, item, 1); /* 产品列表 */
					});
				}
				if (list2 != 'undefined' && list2 != null) {
					/* 活期理财 */
					$.each(list2, function(i, item) {
						commonMenth(i, item, 2); /* 产品列表 */
					});
				}
				if (list3 != 'undefined' && list3 != null) {
					/* 另类投资 */
					$.each(list3, function(i, item) {
						commonMenth(i, item, 3); /* 产品列表 */
					});
				}
				if (list4 != 'undefined' && list4 != null) {
					/* 权益投资 */
					$.each(list4, function(i, item) {
						commonMenth(i, item, 4); /* 产品列表 */
					});
				}
				var temp = "<div style='text-align: center;'><br/>暂无该类别产品，请查看其他产品！</div>";
				if ($("#fundTypeDiv1").html() == "") {
					$("#fundTypeDiv1").html(temp);
				}
				if ($("#fundTypeDiv2").html() == "") {
					$("#fundTypeDiv2").html(temp);
				}
				if ($("#fundTypeDiv3").html() == "") {
					$("#fundTypeDiv3").html(temp);
				}
				if ($("#fundTypeDiv4").html() == "") {
					$("#fundTypeDiv4").html(temp);
				}	
				queryHotFundList();
			} else {
				errorRemark("网络繁忙，请稍后再试");
			}
		}
	});
}


/* 查询热销产品列表 */
function queryHotFundList() { 
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
				var list = data.list
				if (list != 'undefined' && list != null) {
					/* 热销产品 */
					$.each(list, function(i, item) {
						commonMenth(i, item, 5); /* 产品列表 */
					});
				
				}
				if(!list.length>0){
					$(".swiper-slide").eq(0).remove()
					$("#fundTypeDiv5").remove();
					showAndHideDivFundType('1');
					$("#type1").addClass("act");
					$("#fundTypeDiv1").show();
					$("#fundTypeDiv2").hide();
					isHot=true
				}

			} else {
				errorRemark("网络繁忙，请稍后再试");
			}
		}
	});
}

/* 产品列表公用拼接方法 */
function commonMenth(i, item, Number) {
	var listhtml = "";/* 产品列表 */
	if(item.typeId=="0210"){
		listhtml += '<div class="pro-box type0210" onclick="redirectUrl(\'/WeixinService/business/query/fundInfoNew.shtml?fundId=' + item.fundId + '&period='+item.period+'\')">';
	}else{
		listhtml += '<div class="pro-box" onclick="redirectUrl(\'/WeixinService/business/query/fundInfoNew.shtml?fundId=' + item.fundId + '&period='+item.period+'\')">';
	}
	
	var currentWorkdate = item.currentWorkdate;/* 当前工作日 */
	var appointDate = item.appointDate;/* 预约开始日期 */
	var appointEndDate = item.appointEndDate;/* 预约结束日期 */
	var salesDate = item.salesDate;/* 认购日 */
	var subdeadLine = item.subdeadLine;/* 认购截止日 */
	var cirle = parseFloat(numMulti(numDiv((item.scale - item.displayLimit), item.scale), 100)).toFixed(0);
	//过了截止日 = 募集结束
	if(daysBetween(currentWorkdate,subdeadLine) > 0){/*认购截止日期*/
		listhtml += '<span class="prompt act"><b>募集结束</b></span>';
	//认购期= 当前时间 大于等于 认购起始日 并且 小于 认购截止日 
	}else if(daysBetween(currentWorkdate,salesDate) >= 0 && daysBetween(currentWorkdate,subdeadLine) <= 0){
		//额度是否足够
		if(cirle < 100){
			listhtml += '<span class="prompt"><b>购买中</b></span>';
		}else{
			listhtml += '<span class="prompt"><b>排队中</b></span>';
		}
	//预约期= (预约开始日 <= 当前时间  <= 预约截止日 )
	}else if(daysBetween(currentWorkdate,appointDate) >= 0 && daysBetween(currentWorkdate,appointEndDate) <= 0){
		//额度是否足够
		if(cirle < 100){
			listhtml += '<span class="prompt"><b>预约中</b></span>';
		}else{
			listhtml += '<span class="prompt"><b>排队中</b></span>';
		}
	}else{
		listhtml += '<span class="prompt"><b>预约中</b></span>';
	}
	/*
	if (daysBetween(currentWorkdate, appointDate) >= 0 && daysBetween(currentWorkdate, subdeadLine) <= 0) {
		if (daysBetween(currentWorkdate, appointDate) >= 0 && daysBetween(currentWorkdate, appointEndDate) <= 0) { 预约期 
			if (cirle < 100) {
				listhtml += '<span class="prompt"><b>募集中</b></span>';
			} else {
				listhtml += '<span class="prompt"><b>排队中</b></span>';
			}
		} else { 认购期 
			if (cirle < 100) {
				listhtml += '<span class="prompt"><b>募集中</b></span>';
			} else {
				listhtml += '<span class="prompt"><b>募集结束</b></span>';
			}
		}
	} else {
		cirle = 100;
		listhtml += '<span class="prompt act"><b>募集结束</b></span>';
	}*/

	listhtml += '<h2><a>' + item.adname + '</a><i>' + item.typeName + '</i></h2>';
	
	if(item.typeId=="0500" || item.typeId=='0400'){//电商货币类型产品
		listhtml += '<div class="asset currentProduct">';
	}else{
		listhtml += '<div class="asset">';
	}
	listhtml += '<dl class="income upadateorder-income">';

	/* 产品类型id 0100:固定收益 0210：开放性净值类产品 0220：封闭净值 0300：浮动收益 */
	if (item.typeId == '0100'||item.typeId == '0110') {/* 固定收益 年化收益（百分数，保留两位小数）、理财期限、募集规模、投资进度 */
		var profitValue = numMulti(parseFloat(item.profit || 0), 100);
		profitValue = profitValue.toFixed(2);
		var str = profitValue.split(".");
		if (parseInt(profitValue) == profitValue && parseInt(profitValue) == 0) {
			listhtml += '<dt><i>浮动收益</i></dt>';
		} else {
			listhtml += '<dt>' + str[0] + '<i>.' + str[1] + '</i><b>%</b></dt>';
		}
		if(item.typeId == '0110'){
            listhtml += ' <dd>计提基准</dd>';
		}else{
            listhtml += ' <dd>计提基准</dd>';
		}
	} else if (item.typeId == '0210' && item.state=='0'||item.typeId == '0400') {
		/*
		 * 开放性净值类产品申购期
		 * 展示最新净值（保留四位小数）、历史最高净值（保留四位小数）、累计收益率（百分数，保留两位小数）、申赎规则（每日、每周、每月）
		 */

		if(item.typeId == '0400') {
			if(seven == '1'){
				var sevenDayAnnualy="--"
				if(item.sevenDayAnnualy&&"0.00%"!=item.sevenDayAnnualy){
					sevenDayAnnualy=item.sevenDayAnnualy
				}
				if(item.state == '1') {
					sevenDayAnnualy = '--';
				}
	            listhtml += '<dt><i>'+sevenDayAnnualy+'</i></dt>';
				listhtml += '<dd>七日年化收益</dd>';
			}else{
				/*var str = item.latestNewValue;
				if(str){
					var str1 = item.latestNewValue.split(".");
					listhtml += '<dt>' + str1[0] + '<i>.' + str1[1] + '</i></dt>';
				}else{
					listhtml += '<dt>1<i>.0000</i></dt>';
				}
				listhtml += '<dd>最新净值</dd>';*/
				
				 var benefitSinceCreated = item.benefitSinceCreated ? item.benefitSinceCreated : '0.00';
				 if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
					 listhtml += '<dt><i>' + '--' + '</i></dt>';
				 }else {	
					 var str1 = benefitSinceCreated.split(".");
					 listhtml += '<dt>' + str1[0] + '<i>.' + str1[1] + '</i><b>%</b></dt>';
						
					// listhtml += '<dt><i>' + benefitSinceCreated + '</i><b>%</b></dt>';
				 }
				 listhtml += '<dd>成立以来年化收益率</dd>';
			}
			listhtml += '</dl>';
			listhtml += '<dl>';
			listhtml += '<dt><i>' + numDiv(item.money,10000)+ '</i><b>万</b></dt>';
			listhtml += ' <dd>起购金额</dd>';
			listhtml += '</dl>';
			listhtml += '<dl>';
			if(item.sartBuying < 10000) {
				listhtml += '<dt><i>' + item.sartBuying+ '</i><b>元起</b></dt>';
			} else {
				listhtml += '<dt><i>' + numDiv(item.sartBuying,10000)+ '</i><b>万起</b></dt>';
			}
			listhtml += '<dd>追加金额</dd>';
			listhtml += '</dl>';
		} else {
			var str = item.latestNewValue;
			if(str){
				var str1 = item.latestNewValue.split(".");
				listhtml += '<dt>' + str1[0] + '<i>.' + str1[1] + '</i></dt>';
			}else{
				listhtml += '<dt>1.0000</i></dt>';
			}
			listhtml += '<dd>最新净值</dd>';
		}
	} else if (item.typeId == '0220'||(item.typeId == '0210'&& item.state!='0')) {
		/*
		 * 浮动收益 封闭净值类产品
		 * 展示年化收益（浮动收益）、理财期限、募集规模、投资进度
		 */
		listhtml += '<dt><i>浮动收益</i></dt>';
		listhtml += '<dd>计提基准</dd>';
	} else if (item.typeId == '0300') {
		/*
		 * 浮动收益 封闭净值类产品
		 * 年化收益（浮动收益）、理财期限、募集规模、投资进度。
		 */
		listhtml += '<dt><i>浮动收益</i></dt>';
		listhtml += '<dd>计提基准</dd>';
	} else if (item.typeId == '0500') {  //电商货币类型产品
		/*var latestNewValue=item.latestNewValue?item.latestNewValue:"1.0000"
        listhtml += '<dt><i>'+latestNewValue+'</i></dt>';
		listhtml += '<dd>最新净值</dd>';*/
		var benefitSinceCreated = item.benefitSinceCreated ? item.benefitSinceCreated : '0.00';
		if(isNaN(benefitSinceCreated) || benefitSinceCreated == 0) {
			listhtml += '<dt><i>' + '--' + '</i></dt>';
		}else {			
			var str1 = benefitSinceCreated.split(".");
			 listhtml += '<dt>' + str1[0] + '<i>.' + str1[1] + '</i><b>%</b></dt>';
			//listhtml += '<dt><i>' + benefitSinceCreated + '</i><b>%</b></dt>';
		}
		listhtml += '<dd>成立以来年化收益率</dd>';
		listhtml += '</dl>';
		listhtml += '<dl>';
		listhtml += '<dt><i>' + numDiv(item.money,10000)+ '</i><b>万</b></dt>';
		listhtml += ' <dd>起购金额</dd>';
		listhtml += '</dl>';
		listhtml += '<dl>';
		if(item.sartBuying < 10000) {
			listhtml += '<dt><i>' + item.sartBuying+ '</i><b>元起</b></dt>';
		} else {
			listhtml += '<dt><i>' + numDiv(item.sartBuying,10000)+ '</i><b>万起</b></dt>';
		}
		listhtml += '<dd>追加金额</dd>';
		listhtml += '</dl>';
	}
	if(item.typeId!= '0500' && item.typeId != '0400'){
		listhtml += '</dl>';
		if(item.typeId != '0210'){
			listhtml += '<dl>';
			listhtml += '<dt><i>' + item.term + '</i><b>' + item.termUnit + '</b></dt>';
			listhtml += ' <dd>理财期限</dd>';
			listhtml += '</dl>';
		}	
		listhtml += '<dl>';
		listhtml += '<dt><i>' + numDiv(item.scale, 10000) + '</i><b>万</b></dt>';
		listhtml += '<dd>募集规模</dd>';
		listhtml += '</dl>';
		listhtml += '<dl class="schedule">';
		var speed = parseFloat(numMulti(numDiv((item.scale - item.displayLimit), item.scale), 100)).toFixed(0);
		var isExpired = speed;
		if(daysBetween(currentWorkdate, appointDate) >= 0 && daysBetween(currentWorkdate, subdeadLine) <= 0){
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
		}else{
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
	}
	
	if (Number == 1) {
		$("#fundTypeDiv1").append(listhtml);
	}
	if (Number == 2) {
		$("#fundTypeDiv2").append(listhtml);
	}
	if (Number == 3) {
		$("#fundTypeDiv3").append(listhtml);
	}
    if (Number == 4) {
		$("#fundTypeDiv4").append(listhtml);
	}
    if (Number == 5) {
		$("#fundTypeDiv5").append(listhtml);
	}

}

/* banner事件 */
function onclickBanner(url) {
	var params = paresUrlParams(url);
	if(params && params['prdActive']){
		//记录banner点击
	    recordOperation({eventId:params['eventId'],pageId:pageId,pageSource:pageSource});
	}
	redirectUrl(url);
}



/* 理财类型事件 */
function showAndHideDivFundType(index) {
	$("section div.pro-list.clear").hide();
	$(".swiper-slide").click(function(){
    $(this).find("a").addClass("act");
    $(this).siblings().find("a").removeClass("act")
	})
	if(isHot){
		$("section div.pro-list.clear:eq(" +( index-1) + ")").show();
		getUserRequest("fund-list-" + ( index-1));/* 此处subPath为页面内行为 */	
	}else{
		$("section div.pro-list.clear:eq(" + index + ")").show();
		getUserRequest("fund-list-" + index);/* 此处subPath为页面内行为 */
	}
	myScroll.refresh();
}
/*查询banner*/
function queryBanner(){
	$.ajax({
		async : false,
		url : "/WeixinService/business/queryBanner.xhtml",
		type : "post",
		dataType : 'json',
		data : {},
		error : function() {
			errorRemark("网络繁忙，请稍后再试。");          
		},
		success : function(data) {
			var list = data.advertDtoList;
			var htmls = '';
			$.each(list, function(i, item) {
				if (item.state = '1') {
					htmls += "<li>";
					htmls += "<div id='img"+i+"' class='img' onclick=\"onclickBanner('" + item.url + "')\">";
					htmls += "<img class='bannerImg' src=" + item.picture + " />";
					htmls += "</div>";
					htmls += "</li>";
				}
			});
			$(".slides").html(htmls);
		}
	});
}

function checkUserBaseInfoIsExist(type){
	if(type == 1){
		//未测评
		//未测评 提示 
    	$('#goRisk').unbind("click");
		//展示风险测评框
		showTips("noOperation");
		showTips("riskContent");
	}else if(type == 2){
		//过期补充提示
    	$('#reRisk').unbind("click");
		showTips("noOperation");
		showTips("reRiskContent");
	}
	var resultData = USERBASEINFO;
	if(!!resultData && resultData.flag){
		//选择地址 带入默认值
		var param ="?";
		var nation = resultData.nation;
		var nationNM = resultData.nationNM;
		var province = resultData.province;
		var provinceNM = resultData.provinceNM;
		var city = resultData.city;
		var cityNM = resultData.cityNM;
		var vocCode = resultData.vocCode;
		var vocName = resultData.vocName;
		var address = resultData.addr;
		var taxResidentType = resultData.taxResidentType;
		var taxResidentTypeNm = resultData.taxResidentTypeNm;
		var birthDate = resultData.birthDate;
		var otherVocation = resultData.otherVocation;
		param += "viewType=prodcut&nation="+nation+"&nationNM="+encodeURI(nationNM)+"&province="+province+"&provinceNM="+encodeURI(provinceNM)
						+"&city="+city+"&cityNM="+encodeURI(cityNM)+"&address="+encodeURI(address)+"&vocCode="+vocCode+"&vocName="+encodeURI(vocName)
						+"&taxResidentType="+taxResidentType+"&taxResidentTypeNM="+taxResidentTypeNm + "&dateOfBirth="+birthDate
						+"&otherVocation="+otherVocation+"&eventId="+eventId+"&pageSource="+pageSource;
		if(type){
			$('#goRisk,#reRisk').click(function(){
				addCookie('riskUrl',location.href);
				redirectUrl('/WeixinService/business/query/fundModRiskHouseAddrNew.shtml'+param);
			});
		}/*else if(type == 2){
			$('#reRisk').click(function(){
				redirectUrl('/WeixinService/business/query/fundModRiskHouseAddrNew.shtml'+param);
			});
		}*/else{
			redirectUrl('/WeixinService/business/query/fundModRiskHouseAddrNew.shtml'+param);
		}
	}else{
		if(type){
			//为测评
			$('#goRisk,#reRisk').click(function(){
				addCookie('riskUrl',location.href);
				redirectUrl("/WeixinService/business/user/riskLevelNew.shtml?viewType=prodcut&eventId="+eventId+"&pageSource="+pageSource);
			});
		}/*else if(type == 2){
			//测评过期
			$('#reRisk').click(function(){
				redirectUrl("/WeixinService/business/user/riskLevelNew.shtml?viewType=prodcut&eventId="+eventId+"&pageSource="+pageSource);
			});
		}*/else{
			redirectUrl('/WeixinService/business/query/fundModRiskHouseAddrNew.shtml'+param);
		}
	}
}

function queryUserinfo(){
	var resultData = {};
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
			if(data.returnCode != null && data.returnCode == "0000"){
				var flag = false;
				if(!data.nation || !data.vocCode || !data.taxResidentType || !data.birthDate){
					flag = true;
				}
				resultData.nation = data.nation;
				resultData.nationNM=data.nationNM;
				resultData.province=data.province;
				resultData.provinceNM=data.provinceNM;
				resultData.city=data.city;
				resultData.cityNM=data.cityNM;
				resultData.vocCode=data.vocCode;
				resultData.vocName=data.vocCodeNM;
				resultData.flag=flag;
				resultData.addr=data.addr;
				resultData.taxResidentType = data.taxResidentType;
				resultData.taxResidentTypeNm = data.taxResidentTypeNm;
				resultData.birthDate = data.birthDate;
				resultData.syncInvprtpAlert = data.syncInvprtpAlert;
				resultData.otherVocation = data.otherVocation;
			}
        }
    });
	
	return resultData;
}

function updateUserInvprtpAlert(){
	closeTips("tips_invest_update");
	var urlVal="/WeixinService/business/updateUserInvprtpAlert.xhtml";
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

function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null){
         var val = decodeURI(r[2]);
    	 return  unescape(val);
     }else{
    	 return null;
     }
}
