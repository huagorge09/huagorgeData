﻿var myScroll;
$(document).ready(function (e) {
	$(".header .top-a h2").html("个人信息");
	document.title = "个人信息";
	myScroll = new IScroll('#wrapper', {
		scrollbars: true,
		mouseWheel: true,
		interactiveScrollbars: true,
		shrinkScrollbars: 'scale',
		fadeScrollbars: true
	});
	queryUserinfo();
	myScroll.refresh();
	getUserRequest("user-info");/* 此处subPath为页面内行为 */
	idCardInfoUpdate()
	// 判断是否是 用户信息页面
	localStorage.setItem('page','userInfo')
	click()
});

document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);

var userRiskLevel = "";
var isSubmitAllIcImg='';

function idCardInfoUpdate() {
	var userType = localStorage.getItem('userType');
	if (userType != 30 || userType == '') {
		$('#idImgPoint , #idTImePoint').click(function () {
			errorRemark('请您完成实名认证');
			goBankAuth();
		});
	}
	if (userType == 30) {
		$('#idImgPoint').click(function () {
			$('#recommendPop').show()
			$('.backLayer').show()
		});
	}
}

function closePop() {
	$('#recommendPop').hide()
	$('.backLayer').hide()
}



function closeDialog() {
	$('.dialog-content').hide()
}

function queryUserinfo() {
	$.ajax({
		async: false,
		url: "/WeixinService/business/queryUserinfo.xhtml",
		data: "",
		dataType: "json",
		cache: false,
		type: "post",
		error: function (textStatus, errorThrown) {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success: function (data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				var userType = data.userType;
				var isSetTradePassword = data.isSetTradePassword;
				var riskLevel = data.riskLevel;
				userRiskLevel = data.riskLevel;
				$("#mobile").html(data.mobile);
				$("#idType").html(data.idType);
				$("#riskLevel").html(data.riskLevel);
				$("input[name=userType]").val(data.userType);
				localStorage.setItem('userType', userType)
				localStorage.setItem('idExpireDate', data.idExpireDate)
				localStorage.setItem('birthDate', data.birthDate)
				if (userType != null && userType == 30) {
					$("#userName").html(data.userName);
					$("#idNo").html(data.idNo);
					//  身份证有效期
					$('#idTImePoint').attr("href", "javascript:redirectUrl('/WeixinService/business/user/updateIdCardIndateNew.shtml')");
					if (isSetTradePassword != null && isSetTradePassword == "Y") {
						$("#isSetTradePassword").html("已设置").addClass("act");
						$("#isSetTradePassword").parent().attr("href", "javascript:redirectUrl('/WeixinService/business/user/modifyTPasswordNew.shtml')");
						$("#mobile").parent().attr("href", "javascript:redirectUrl('/WeixinService/business/user/modifyRegMobileNew.shtml')");
					} else {
						$("#isSetTradePassword").html("未设置");
						$("#isSetTradePassword").parent().attr("href", "javascript:redirectUrl('/WeixinService/business/user/setTPasswordNew.shtml')");
					}
				}
				var idDate = formatDate1(data.idExpireDate); //身份证有效期 中文
				localStorage.setItem("endTime",idDate)
				if (data.idExpireDate == '' || data.idExpireDate == '99991231') {
					$('#IdCardTime').html('未设置')
				} else {
					$('#IdCardTime').html(idDate)
					$('#IdCardTime').removeClass('icon')
				}
				//20180315 去掉个人信息实名验证
				/*else{
					$("#userName,#idNo").parent().attr("onclick","redirectUrl('/#/cardinfo')");
					$("#mobile").parent().attr("href","javascript:redirectUrl('/#/cardinfo')");
					$("#isSetTradePassword").html("未设置");
					$("#isSetTradePassword").parent().attr("href","javascript:redirectUrl('/#/cardinfo')");
				}*/

				//投资者类型及状态
				var invprtp = data.invprtp;
				var appst = data.appst;
				var invprtpScore = data.invprtpScore;
				var invprtpStr = "";
				var invtp_cancel = "";

				if (invprtp == 0 || invprtp == '0') {
					if (invprtpScore < 60 || invprtpScore == null || invprtpScore == "") {
						invprtpStr = "普通投资者";
					} else {
						invprtpStr = "专业投资者";
					}
				} else {
					invprtpStr = "普通投资者";
				}

				$("#invtpType").text(invprtpStr);
				$("#invtpType").parent().attr("href", "javascript:invtpApplyTask(\"" + invprtp + "\",\"" + appst + "\",\"" + invprtpScore + "\");");


				var riskLevelVal = "";
				if (riskLevel != null && riskLevel == "1") {
					//$("#riskLevel").html("保守型（低风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
					$("#riskLevel").html("C1-保守型（低风险承受能力）");
					riskLevelVal = "C1-保守型";
				} else if (riskLevel == "2") {
					//$("#riskLevel").html("稳健型（中低风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
					$("#riskLevel").html("C2-稳健型（中低风险承受能力）");
					riskLevelVal = "C2-稳健型";
				} else if (riskLevel == "3") {
					//$("#riskLevel").html("平衡型（中风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
					$("#riskLevel").html("C3-平衡型（中风险承受能力）");
					riskLevelVal = "C3-平衡型";
				} else if (riskLevel == "4") {
					//$("#riskLevel").html("成长型（中高风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
					$("#riskLevel").html("C4-成长型（中高风险承受能力）");
					riskLevelVal = "C4-成长型";
				} else if (riskLevel == "5") {
					//$("#riskLevel").html("C5-积极型（高风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
					$("#riskLevel").html("C5-积极型（高风险承受能力）");
					riskLevelVal = "C5-积极型";
				} else {
					//$("#riskLevel").html("保守型（默认低风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
					$("#riskLevel").html("C1-保守型（默认低风险承受能力）");
					riskLevelVal = "C1-保守型";
				}

				$("#riskLevel").parent().attr("href", "javascript:goRiskPage(\"" + data.birthDate + "\");");

				checkIsRiskLevel(riskLevelVal);


				$("input[name='invprtp']").val(invprtp);
				$("input[name='appst']").val(appst);

				var address = "";
				if (data.nationNM != "" && data.nationNM != null) {
					address += data.nationNM;
				}
				if (data.provinceNM != "" && data.provinceNM != null) {
					address += "-" + data.provinceNM;
				}
				if (data.cityNM != "" && data.cityNM != null) {
					address += "-" + data.cityNM;
				}
				if (address != null && address != "") {
					$("input[name='region']").val(address);
				}
				if (data.addr != "" && data.addr != null) {
					$("input[name='addr']").val(data.addr);
				}

				$("#homeAddress").parent().attr("href", "javascript:redirectUrl('/WeixinService/business/user/modifyHouseAddressNew.shtml?region=" + address + "&address=" + data.addr + "')");
				/*if(data.addr != "" && data.addr != null){
					address+="-"+data.addr;
				}*/
				if (address != null && address != "") {
					$("#homeAddress").html(address);
					$("#homeAddress").removeClass("icon");
				}
				if (data.vocCodeNM != "" && data.vocCodeNM != null) {
					if (data.vocCode == 15 && data.otherVocation != '') {
						$("#profession").html(data.otherVocation);
					} else {
						$("#profession").html(data.vocCodeNM);
					}
					$("#profession").removeClass("icon");
				}

				$("#weixinRiskBtn").click(function () {
					$("#tips_01").hide();
				})
				$("#goRiskBtn").click(function () {
					window.location.href = "/WeixinService/business/user/riskLevelNew.shtml?invprtp=1";
				})

				//新增字段
				$("#birthDateTxt").parent().attr("href", "javascript:redirectUrl('/WeixinService/business/user/updateBirthDateNew.shtml?birthDate=" + data.birthDate + "')");
				if (data.birthDate != "" && data.birthDate != null) {
					$("#birthDateTxt").html(data.birthDateNm);
					$("#birthDateTxt").removeClass("icon");
				}
				//新增字段
				$("#taxResidentTypeText").parent().attr("href", "javascript:redirectUrl('/WeixinService/business/user/updateTaxResidentInfoNew.shtml?taxResidentType=" + data.taxResidentType + "&taxResidentTypeNM=" + data.taxResidentTypeNm + "')");
				if (data.taxResidentTypeNm != "" && data.taxResidentTypeNm != null) {
					$("#taxResidentTypeText").html(data.taxResidentTypeNm);
					$("#taxResidentTypeText").removeClass("icon");
				}

			}
		}
	});
}

function checkIsRiskLevel(riskLevelVal) {
	var urlVal = "/WeixinService/business/queryIsNeedTest.xhtml";
	var riskEvalDate = "";
	var nowDate = new Date();
	$.ajax({
		async: false,
		url: urlVal,
		type: "post",
		dataType: 'json',
		data: {},
		error: function () {
			show_tips("网络繁忙，请稍后再试。");
		},
		success: function (data, textStatus) {
			if (data) {
				riskEvalDate = data.riskEvalDate;
			}
		}
	});

	var date = new Date(riskEvalDate);
	var dataDiff = (nowDate - date) / 86400000;
	//时间已过 则 提示过期
	if (dataDiff > 365) {
		$("#riskLevel").html("测评已过有效期，请重新评测");
		$("#riskLevel").css("color", "red");
	}
}


/**
 * 新增
 * 
 * 去评级页面
 */
function goRiskPage(birthDate) {
	window.location.href = "/WeixinService/business/user/riskLevelNew.shtml?birthDate=" + birthDate + "&pageSource=wx_personalInformationId&eventId=event_wx_personRiskId";
}

function invtpApplyTask(invprtp, appst, invprtpScore) {
	var userType = $("input[name=userType]").val();

	if (userType == null || userType != "30") {
		errorRemark("请您完成实名认证");
		goBankAuth();
		return;
	}

	if (invprtp == "" || invprtp == null) {
		//invprtpStr = '普通投资者（可申请为专业投资者）';
		//跳转至申请专业投资者页面
		window.location.href = "/WeixinService/business/user/commonInvprExplainNew.shtml";
	} else if (invprtp == 1 || invprtp == '1') {
		if (appst == 'N' || appst == 'I') {
			//invprtpStr = '普通投资者（专业投资者申请中）';
			window.location.href = "/WeixinService/business/user/materialReviewPageNew.shtml";
		} else if (appst == 'C') {
			$("#hint_01").show();
		} else {
			//跳转至申请专业投资者页面
			window.location.href = "/WeixinService/business/user/commonInvprExplainNew.shtml";
		}
	} else if (invprtp == 0 || invprtp == '0') {
		invprtpScore = parseInt(invprtpScore);
		if (invprtpScore < 60 || invprtpScore == null || invprtpScore == "") {
			//invprtpStr = "普通投资者（专业投资者证明材料已审核通过，请进行投资知识评估）";
			window.location.href = "/WeixinService/business/user/materialReviewPageNew.shtml?pic=2";
		} else {
			//invprtpStr = "专业投资者（申请转为普通投资者）";
			window.location.href = "/WeixinService/business/user/specialtyInvprExplainNew.shtml";
		}
	}
}



function openRiskPage() {
	var invprtp = $("input[name='invprtp']").val();
	var appst = $("input[name='appst']").val();
	if (userRiskLevel == 0 || userRiskLevel == "0") {
		appst = 0;
	}
	if ("0" == invprtp) {
		$("#tips_01").show();
	} else if ("C" == appst) {
		$("#hint_01").show();
	} else {
		window.location.href = "/WeixinService/business/user/riskLevelNew.shtml?invprtp=" + encodeURI(invprtp) + "&appst=" + encodeURI(appst);
	}
}

function closeHint() {
	$.ajax({
		async: !1,
		url: "/WeixinService/business/clearAppcvInvp.xhtml",
		data: {
			apptp: "0"
		},
		dataType: "json",
		cache: !1,
		type: "POST",
		error: function () {
			show_tips("网络繁忙，请稍后再试。");
		},
		success: function (n) {
			$("#hint_01").hide();
			queryUserinfo();
		}
	});
}

/* 退出登录 */
function exit() {
	$.ajax({
		async: true,
		url: "/WeixinService/business/exit.xhtml",
		data: "",
		dataType: "json",
		cache: false,
		type: "post",
		error: function (textStatus, errorThrown) {

		},
		success: function (data) {
			localStorage.clear();
			redirectUrl(data.url);
		}
	});
}

//  关闭身份证示例弹框
function closeExample(){
	$('#icExampleBox').hide()
}

/**
 * 日期格式转换
 * @param date
 * @returns
 */
function formatDate1(date) {
	if (date == null || date == '') {
		return "";
	}
	date = unformat1(date);
	return parseInt(date.substr(0, 4), 10) + "年" + date.substr(4, 2) + "月" + date.substr(6, 2) + "日";
}

function click(){
	//  打开示例框
	$('#icCardExShow').click(function(){
		$('#icExampleBox').show()
	})
}

function goBankAuth(){
	$('.backLayer').show();
	setTimeout(function () {
		redirectUrl("/#/cardinfo");
	}, 1000);
}

openQualified()
// 是否开启合格投资者功能
function openQualified(){
    var param=queryParamList("SYSTEM","ACINVCONF","");
	var pmnm=""
	for(var i=0;i<param.length;i++){
		if(param[i].pmco=="MAIN"){
			pmnm=param[i].pmnm;
		}
    }
	if(pmnm=="0"){  //为0时不显示菜单
		$("#idImgPoint").hide()
	}else{
		$("#idImgPoint").show()
	}
}

function clickInvestorNotice(){
	$("#investorNotice").show();
	$("section.page").hide();
	pageEventData("04","08");
}

function closeInvestorNotice(){
	$("#investorNotice").hide();
	$("section.page").show();
}