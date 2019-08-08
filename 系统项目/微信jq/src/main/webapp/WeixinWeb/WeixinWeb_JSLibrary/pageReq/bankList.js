﻿var myScroll;

$(document).ready(function(e) {
	$(".header .top-a h2").html("我的银行卡");
	document.title="我的银行卡";
	myScroll = new IScroll('#wrapper', {
		scrollbars : true,
		mouseWheel : true,
		interactiveScrollbars : true,
		shrinkScrollbars : 'scale',
		fadeScrollbars : true
	});

	queryUserInfo();
	queryMyBankCard();
	myScroll.refresh();
	getUserRequest("bank-list");/* 此处subPath为页面内行为 */
});

document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

/* 我的银行卡信息 */
function queryMyBankCard() {
	$.ajax({
		async : false,
		url : "/WeixinService/business/queryMyBankCardNo.xhtml",
		data : {},
		dataType : "json",
		cache : false,
		type : "POST",
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			var htmls = "";
			var className = "";
			var bankName = "";
			var realbankno = "";

			if (data.returnCode == '0000') {
				$.each(data.tradeAcctlist,function(i, item) {
					/* 银行卡信息展示 */
					realbankno = item.realBankNo;
					if (realbankno == '007') {/* 招商银行 */
						className = 'n1';
					} else if (realbankno == '003') {/* 农业银行 */
						className = 'n2';
					} else if (realbankno == '005') {/* 建设银行 */
						className = 'n3';
					} else if (realbankno == '008') {/* 中信银行 */
						className = 'n4';
					} else if (realbankno == '004') {/* 中国银行 */
						className = 'n5';
					} else if (realbankno == '012') {/* 光大银行 */
						className = 'n6';
					} else if (realbankno == '006') {/* 交通银行 */
						className = 'n7';
					} else if (realbankno == '002') {/* 工商银行 */
						className = 'n8';
					} else if (realbankno == '011') {/* 兴业银行 */
						className = 'n9';
					} else if (realbankno == '601') {/* 平安银行 */
						className = 'n10';
					} else if (realbankno == '015') {/* 邮储银行 */
						className = 'n11';
					} else if (realbankno == '009') {/* 浦发银行 */
						className = 'n12';
					} else if (realbankno == '060') {/* 广发银行 */
						className = 'n13';
					} else if (realbankno == '014') {/* 民生银行 */
						className = 'n14';
					} else if (realbankno == '017') {/* 华夏银行 */
						className = 'n15';
					} else if (realbankno == '032') {/* 天津银行 */
						className = 'n16';
					}else {/* 默认的银行卡样式 */
						className = 'n0';
					}
					htmls += "<div class='center-mybank "
							+ className
							+ "'><span class='mybank-icon'></span>";
					htmls += "<h1>"
							+ item.bankNm
							+ "</h1><span class='mybankid'>"
							+ item.bankAccoDisplay+ "</span>";
					htmls += "<span class='mybankid-delect' href='javascript:void(0)'><em onclick='showCanelBindDiv(\""
							+ item.tradeAcco
							+ "\")'>删除卡片</em></span></div>";
				});

				$("#bankList").html(htmls);

				var userType = data.userType;
				if (typeof (userType) != undefined && userType != "") {
					if (userType == '30') {/* 30用户，添加银行卡 */
						$("#addBankCard").attr("href","/WeixinService/business/bank/addBank.shtml");
					} else {/* 非30用户，鉴权 */
						$("#addBankCard").attr("href","/WeixinService/business/bank/bankAuth.shtml");
					}
				}
			} else {
				$("#addBankCard").attr("href","javascript:errorRemark('网络繁忙，请稍后再试')");
			}
		}
	});
}

function showCanelBindDiv(tradeAcco) {
	$("#cancelBindDiv").show();
	$("#cancelBtn").attr("href","javascript:cancelBindBankCard('" + tradeAcco + "')");
	myScroll.disable;
}

function closeCanelBindDiv(){
	$("#cancelBindDiv").hide();
	myScroll.enable;
}

/* 解除绑定银行卡 */
function cancelBindBankCard(tradeAcco) {
	closeCanelBindDiv();

	$.ajax({
		async : false,
		url : "/WeixinService/business/canalBindBankCard.xhtml",
		data : {
			"tradeAcco" : tradeAcco
		},
		dataType : "json",
		type : "POST",
		cache : false,
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				errorRemark("操作成功！");
				queryMyBankCard();
				myScroll.refresh();
			} else if (data.returnCode == "9027") {
				errorRemark("开户当天无法取消或更换此银行卡，请您明天再试!");
			} else if (data.returnCode == "9020") {
				errorRemark(data.returnMsg);
			} else if (data.returnCode == "9000") {
				errorRemark("参数为空！");
			} else if (data.returnCode == "8000") {
				errorRemark(data.returnMsg);
				/* 跳转到登陆页面，判断微信和其他ie，分别跳转到不同的登陆页面 */
				window.setTimeout("goToLogin()", 2000);
			} else if(data.returnCode == "9999"){
				errorRemark("网络繁忙，请稍后再试");
			}else{
				errorRemark(data.returnMsg);
			}
		}
	});
}

/*查询用户信息  此处主要查询是否为30用户*/
function queryUserInfo() {
	var url = "/WeixinService/business/queryUserinfo.xhtml";
	$.ajax({
		async : false,
		url : url,
		data : "",
		dataType : "json",
		cache : false,
		type : 'post',
		error : function(textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success : function(data) {
			if (data.returnCode == "0000") {
				if(data.userType == null || data.userType != "30"){
					errorRemark("请您完成实名鉴权");
					window.setTimeout("redirectUrl('/WeixinService/business/bank/bankAuth.shtml')", 1000);
				}
			} else if (data.returnCode == "8000") {
				errorRemark(data.returnMsg);
				/* 跳转到登陆页面，判断微信和其他ie，分别跳转到不同的登陆页面 */
				window.setTimeout("goToLogin()", 1000);
			} else {
				errorRemark("网络繁忙，请稍后再试");
			}
		}
	});
}