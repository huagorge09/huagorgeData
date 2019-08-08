//当前页面pageId
var pageId = "";
//来源页面ID
var pageSourceId = "";
//事件Id
var eventId = "";

var userType1 = "";

var isApply= false; //是否提交过投资者信息

$(document).ready(function (e) {
	document.title = "个人信息_招商财富   都市精英的私人银行、高净值人士专业资产管理平台";
	getUserRequest("pc_applicationGroups_queryMySafeInfo");
	localStorage.setItem('pageName', 'safeInfo')
	//当前页面pageId
	pageId = $("#pageId").val();
	//来源页面ID
	pageSourceId = getUrlParameter("pageSourceId");
	if (null == pageSourceId || pageSourceId == "") {
		pageSourceId = pageId;
	}
	eventId = getUrlParameter("eventId");
	if (null == eventId || eventId == "") {
		eventId = "event_ratingId";
	}

	var type = getUrlParameter("type");
	queryIcUploadInfo();
	queryUserinfoAccount();
	type = removeSpecialStr(type);
	if (type != null && type == "riskLevel") {
		toRiskLevel();
	}
	var operation = getUrlParameter("operation");
	if (operation != "" && operation == 'toRiskLevel') {
		toRiskLevel();
	}
	
	$("button[name=prof-confirm]").bind("click", function (e) {
		confirmSubmitProfession();
	});
	
});


function queryUserinfoAccount() {
	$.ajax({
		async: false,
		url: "/AppService/business/queryUserinfo.xhtml",
		data: "",
		dataType: "json",
		cache: false,
		type: "post",
		error: function (textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success: function (data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				var idExpireDate2 = data.idExpireDate;
				console.log(idExpireDate2)
				// 身份证有效期
				$('#idCardIndate').val(formatDateNum(idExpireDate2));
				$('#icDate').html(formatDateChinese(idExpireDate2));
                localStorage.setItem("idExpireDate",data.idExpireDate)
				$("#mobileStatus").addClass(data.mobile == null || data.mobile == '' ? "" : "act");
				$("#mobileStatus dl dd").html(data.mobile == null || data.mobile == '' ? "未绑定" : "已绑定");
				$("#realNameStatus").addClass(data.userType == null || data.userType == '10' ? "" : "act");
				$("#realNameStatus dl dd").html(data.userType == null || data.userType == '10' ? "未认证" : "已认证");
				$("#tradePasswordStatus").addClass(data.isSetTradePassword != 'Y' ? "" : "act");
				$("#tradePasswordStatus dl dd").html(data.isSetTradePassword != 'Y' ? "未设置" : "已设置");
				$("#username").html(data.userName == '' ? "" : data.userName);
				$("#realNameStatusEm").addClass(data.userType == null || data.userType == '10' ? "" : "act").html(data.userType == null || data.userType == '10' ? "未认证" : "已认证").next().html(
					data.userType == null || data.userType == '10' ? "去鉴权" : "").prop("href",
						data.userType == null || data.userType == '10' ? "/AppService/business/bank/realName.shtml" : "javascript:void(0);");
				$("#mobile").html(data.mobile);
				$("#mobileStatusEm").addClass(data.userType == null || data.userType == '10' ? "" : "act").html(data.userType == null || data.userType == '10' ? "未认证" : "已认证").next().html(
					data.userType == null || data.userType == '10' ? "去鉴权" : "更改手机号").prop("href",
						data.userType == null || data.userType == '10' ? "/AppService/business/bank/realName.shtml" : "/AppService/business/account/modifyRegMobile.shtml");
				$("#idNo").html(data.idNo);
				$("#idNoStatusEm").addClass(data.userType == null || data.userType == '10' ? "" : "act").html(data.userType == null || data.userType == '10' ? "未绑定" : "已绑定").next().html(
					data.userType == null || data.userType == '10' ? "去鉴权" : "").prop("href",
						data.userType == null || data.userType == '10' ? "/AppService/business/bank/realName.shtml" : "javascript:void(0);");
				$("#TpasswordStatusEm").addClass(data.isSetTradePassword != 'Y' ? "" : "act");
				$("#TpasswordStatusEm").html(data.isSetTradePassword != 'Y' ? "未设置" : "已设置").next().html(data.isSetTradePassword != 'Y' ? data.userType == '30' ? "设置密码" : "去鉴权" : "立即修改").prop(
					"href",
					data.isSetTradePassword != 'Y' ? data.userType == '30' ? "/AppService/business/account/setTPassword.shtml" : "/AppService/business/bank/realName.shtml"
						: "/AppService/business/account/modifyTPassword.shtml");
				$("#passwordStatusEm").addClass(data.idSetPassword != 'Y' ? "" : "act");
				$("#passwordStatusEm").html(data.idSetPassword != 'Y' ? "未设置" : "已设置");
				localStorage.setItem('userType', data.userType)//用户是否实名
				$('#idCardSet').next().html(!data.idExpireDate ? "去设置" : "重新设置");
				if(data.userType == "30"){
					$("#idCardImgSet").next().click(function(){
						$('#recommendPop').show();
						$('.backLayer').show();
					});
					$("#idCardSet").next().click(function(){
						$('#idCard-select-pop').show();
					});
				}else{
					$("#idCardImgSet").next().prop("href", "/AppService/business/bank/realName.shtml").html("去鉴权");
					$("#idCardSet").next().prop("href", "/AppService/business/bank/realName.shtml").html("去鉴权");
				}
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
				/*if(data.addr != "" && data.addr != null){
					address+="-"+data.addr;
				}*/
				$("#address").html(address);
				$("input[name='nation']").val(data.nation);
				$("input[name='province']").val(data.province);
				$("input[name='city']").val(data.city);
				$("input[name='addr']").val(data.addr);
				$("#addressStatusEm").addClass(data.nation == "" ? "" : "act");
				$("#addressStatusEm").html(data.nation == "" ? "未设置" : "已设置");
				//职业数据
				$("#professionEm").addClass(!data.vocCode ? "" : "act");
				$("#professionEm").html(!data.vocCode ? "未设置" : "已设置");
				$("#profession").attr("data-profid", data.vocCode);
				$("#otherVocation").val(data.otherVocation);
				// 身份证有效期
				$('#idCardSet').html(!data.idExpireDate ? "未设置" : "已设置");
				$("#idCardSet").addClass(!data.idExpireDate ? "" : "act");
				if (data.vocCode == 15) {
					if (data.otherVocation == '') {
						$("#profession").html(data.vocCodeNM);
					} else {
						$("#profession").html(data.otherVocation);
					}
				} else {
					$("#profession").html(data.vocCodeNM);
				}

				//投资者类型及状态
				var invprtp = data.invprtp;
				var appst = data.appst;
				var invprtpStr = "";
				var invprtpScore = data.invprtpScore;

				$("#invprtTpCancel").remove();
				if (invprtp == "" || invprtp == null) {
					invprtpStr = '<a id="invprtp_title" href="javascript:showInvprtpDiv(\'0\');">（可申请为专业投资者）</a><em class="showWare">' +
						'<img src="/AppWeb/AppWeb_Images/images/ic_help.png" height="14" width="14" alt="">' +
						'<span class="markedWords" style="font-size:12px;">专业投资者可更客观地评价自身的风险承受能力。</span>' +
						'</em>';
					$("#invprtTp").html("普通投资者" + invprtpStr);
				} else if (invprtp == 1 || invprtp == '1') {
					if (appst == 'N' || appst == 'I') {
						invprtpStr = '<a id="invprtp_title" href="javascript:showInvprtpDiv(\'N\');">（专业投资者申请中）</a>';
						$("#invprtTpEm").append('<a id="invprtTpCancel" href="javascript:invprtpCancelConfirm();">取消申请</a>');
					} else if (appst == 'C') {
						invprtpStr = '<a id="invprtp_title" href="javascript:showInvprtpDiv(\'C\');">（材料未通过，再次申请为专业投资者）</a>';
					} else {
						invprtpStr = '<a id="invprtp_title" href="javascript:showInvprtpDiv(\'0\');">（可申请为专业投资者）</a><em class="showWare">' +
							'<img src="/AppWeb/AppWeb_Images/images/ic_help.png" height="14" width="14" alt="">' +
							'<span class="markedWords" style="font-size:12px;">专业投资者可更客观地评价自身的风险承受能力。</span>' +
							'</em>';
					}
					$("#invprtTp").html("普通投资者" + invprtpStr);
				} else if (invprtp == 0 || invprtp == "0") {
					invprtpScore = parseInt(invprtpScore);
					if (invprtpScore < 60 || invprtpScore == null || invprtpScore == "") {
						invprtpStr = '<a id="invprtp_title" href="javascript:showInvprtpDiv(\'E\');">（专业投资者证明材料已审核通过，请进行投资知识评估）</a>';
						seeDetails = '<img src="/AppWeb/AppWeb_Images/images/ic_alert-transparent1.png" height="20" width="20" onclick="showApplyIng(\'\',\'1\');" title="查看详情">';
						$("#invprtTp").html("普通投资者" + invprtpStr + seeDetails);
						$("#invprtTpEm").append('<a id="invprtTpCancel" href="javascript:invprtpCancelConfirm();">取消申请</a>');
					} else {
						invprtpStr = '<a id="invprtp_title" href="javascript:showInvprtpDiv(\'Y\');">（申请转为普通投资者）</a><em class="showWare">' +
							'<img src="/AppWeb/AppWeb_Images/images/ic_help.png" height="14" width="14" alt="">' +
							'<span class="markedWords" style="font-size:12px;">普通投资者在信息告知、风险警示、适当性匹配等方面享有特别保护。</span>' +
							'</em>';
						$("#invprtTp").html("专业投资者" + invprtpStr);
					}
				}
				var riskLevel = data.riskLevel;
				var riskLevelVal = "";


				$("#riskLevelEm").html(riskLevel == 0 || riskLevel == '0' ? "未评级" : "已评级").addClass(riskLevel == 0 || riskLevel == '0' ? "" : "act");
				$("#toRiskLevel").attr("href", riskLevel == null || riskLevel == '' ? "javascript:void(0)" : "javascript:toRiskLevel()");
				if (riskLevel != null && riskLevel == "1") {
					riskLevelVal = "C1-保守型";
					$("#riskLevel").html("C1-保守型（低风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
				} else if (riskLevel == "2") {
					$("#riskLevel").html("C2-稳健型（中低风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
					riskLevelVal = "C2-稳健型";
				} else if (riskLevel == "3") {
					$("#riskLevel").html("C3-平衡型（中风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
					riskLevelVal = "C3-平衡型";
				} else if (riskLevel == "4") {
					$("#riskLevel").html("C4-成长型（中高风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
					riskLevelVal = "C4-成长型";
				} else if (riskLevel == "5") {
					$("#riskLevel").html("C5-积极型（高风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
					riskLevelVal = "C5-积极型";
				} else {
					$("#riskLevel").html("C1-保守型（默认低风险承受能力）　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
					riskLevelVal = "C1-保守型";
				}
				/*if (invprtp == "0" || invprtp == 0) {
					$("#riskLevel").html("专业型");
				} */
				checkIsRiskLevel(riskLevelVal);

				/**
				 * 新增出生日期和税收居民类型字段
				 */
				$("#birthDateEm").addClass(data.birthDate == "" ? "" : "act");
				$("#birthDateEm").html(data.birthDate == "" ? "未设置" : "已设置");
				$("#birthDate").html(data.birthDateNm);
				$("#birthDateFrm").val(data.birthDate);

				$("#taxResidentTypeEm").addClass(data.taxResidentType == "" ? "" : "act");
				$("#taxResidentTypeEm").html(data.taxResidentType == "" ? "未设置" : "已设置");
				$("#taxResidentTypeText").html(data.taxResidentTypeNm);
				$("input[name='taxResidentType']").val(data.taxResidentType);
			}
		}
	});
}


/* 未风险测评则弹出风险测评弹框  */
function checkIsRiskLevel(riskLevelVal) {
	var urlVal = "/AppService/business/queryIsNeedTest.xhtml";
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
	if (dataDiff >= 365) {
		$("#riskLevel").html("<a id='invprtp_title' href='javascript:toRiskLevel();'>" + riskLevelVal + "（测评已过有效期，请重新评测）</a>　<a id='invprtp_title' href='javascript:showHistory();'>查看历史测评记录</a>");
	}
}

function toRiskLevel() {
	getUserRequest("pc_applicationGroups_riskLevel");
	checkIsLogin();
	$("#div_01").hide();
	$("#div_02").show();
	$("#risk_cover_checkbox").attr("checked", false);
	queryDateOfBirth();
	operatingRecord(pageSourceId, pageId, eventId, "");
	scrollTo(0, 0);
}


//最低年龄限制 民事行为能力 只能选 否
var mayAgeToDay = 16;
function queryDateOfBirth() {
	$(".w_text").val("");
	//出生日期
	var thisVal = $("#birthDateFrm").val().replace(/-/g, "");
	var nowDate = new Date();
	var nowYear = nowDate.getFullYear();
	var nowMonth = nowDate.getMonth() + 1;
	var nowDate = nowDate.getDate();
	if (nowMonth < 10) {
		nowMonth = "0" + nowMonth;
	}
	if (nowDate < 10) {
		nowDate = "0" + nowDate;
	}

	var limitAge = (parseInt(nowYear) - parseInt(mayAgeToDay)) + "" + nowMonth + nowDate;

	var dataDiff = daysBetween(limitAge, thisVal);
	//时间差
	if (dataDiff < 0 || !thisVal || isNaN(dataDiff)) {
		$(".risk-con-list ul li:eq(16) span.list:eq(1) div p").html("年龄不满16岁，不具有完全民事行为能力。<a href='/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_INFO'>请根据实际情况填写出生日期。</a>");
		$(".risk-con-list ul li:eq(16) span.list:eq(0)").unbind("click");
		$(".risk-con-list ul li:eq(16) span.list:eq(1)").bind("click");
		$(".risk-con-list ul li:eq(16) span.list:eq(1)").click();
	} else {
		$(".risk-con-list ul li:eq(16) span.list").removeClass("act");
		$(".risk-con-list ul li:eq(16) span.list").bind("click", function (e) { optionBindClickEvent(e) });
	}
}


function optionBindClickEvent(e) {
	$(e.target).removeClass("act").siblings().removeClass("act");
	$(e.target).addClass("act");
	var dataInput = $(e.target).attr("data-input");
	if ("Y" == dataInput) {
		$(e.target).parent().find("div[class=risk-option-desc]").show();
	} else {
		$(e.target).parent().find("div[class=risk-option-desc]").hide();
	}
}


function checkIsLogin() {
	$.ajax({
		async: false,
		url: "/AppService/setUp/queryUserinfoCheckLogin.xhtml",
		data: "",
		dataType: "json",
		cache: false,
		type: "post",
		error: function (textStatus, errorThrown) {
		},
		success: function (data) {
			if (data.returnCode == '8000') {
				goToURL("/login/login.shtml");
			}/*else if(data.returnCode=='0000'&&data.isSetTradePassword=='N'){
				如果未设置安全码，已经鉴权跳转到设置安全码页面，未鉴权跳转到鉴权页面
				if(data.userType!='30'){
					gotoRealName();
				}
			}*/
		}
	});
}

//弹窗设置职业
function toProfession() {
	assemblyProfessionData();
	$(".profession-value").bind("click", function (e) {
		selectProfession(this);
	});

	$(".choose-box .profession_bg_close").bind("click", function (e) {
		$("#div_profession_bg").hide();
	});
	$("#div_profession_bg").show();
}

function assemblyProfessionData() {
	var profData = queryParamList("SYSTEM", "VOCCODE", "");
	if (null != profData) {
		var profs = "";
		var reProfId = $("#profession").attr("data-profid");
		$.each(profData, function (index, data) {
			var isActive = "notactive";
			if (reProfId == data.pmco) {
				isActive = "active";
			}
			var prof = "<li class=\"" + isActive + " profession-value\" data-profid=\"" + data.pmco + "\"><span>" + data.pmnm + "</span></li>";
			profs += prof;
		});
		$(".profession-list").html(profs);
		if(reProfId == "15"){
			$(".otherVocation").show();
		}
	}
}

function selectProfession(ele) {
	var element = $(ele);
	element.siblings().removeClass("active").addClass("notactive");
	element.removeClass("notactive").addClass("active");
	if(element.attr("data-profid") == "15"){
		$(".otherVocation").show();
	}else{
		$(".otherVocation").hide();
	}
}

function confirmSubmitProfession() {
	var selectList = $(".profession-list .active");
	
	if (selectList.length <= 0) {
		show_tips("请选择您的职业");
		//		$("#div_profession_bg").hide();
		return;
	}

	//只取一个
	var select = $(selectList[0]);
	var profId = select.attr("data-profid");
	var profName = select[0].innerText;
	var isSucc = false;
	var otherVocation = $.trim($("#otherVocation").val());
	if(profId == "15" && otherVocation == ""){
		show_tips("请填写您的职业");
		return;
	}
	
	// else if(!ischinese(otherVocation) && profId=='15'){
	// 	show_tips('请填写正确的职业')
	// 	return;
	// }
	if(profId != "15"){
		otherVocation = "";
	}
	$.ajax({
		async: false,
		url: "/AppService/business/updateCmfUserBaseInfo.xhtml",
		data: {
			nation: "",
			province: "",
			city: "",
			addr: "",
			voccode: profId,
			otherVocation: otherVocation
		},
		dataType: "json",
		cache: false,
		type: "post",
		error: function () {
			show_tips("网络繁忙，请稍后再试。");
		},
		success: function (n) {
			if (n != null && n.resultCode == "0000") {
				isSucc = true;
			} else {
				show_tips(n.resultMsg);
			}
		}
	});

	if (isSucc) {
		$("#professionEm").addClass(!profId ? "" : "act");
		$("#professionEm").html(!profId ? "未设置" : "已设置");
		$("#profession").attr("data-profid", profId);
		$("#profession").html(profName);
		$("#div_profession_bg").hide();
		if(profId == "15"){
			$("#profession").html(otherVocation);
		}
	}
}

// 查询合格投资者是否提交过
function queryUserAlreadySub(){
	$.ajax({
		url: '/AppService/business/queryQualifiedUserInfoByIdno.xhtml',
		type: 'POST',
		async: false,
		dataType: 'json',
		success: function (data) {
			if (data.returnCode == "0000") {
				if (data.data && data.data.length > 0) {
					isApply = true;
				}
			}
		},
		error:function(){

		}
	})

} 

// 提交身份者证有效期时间
function submitIdCardTime() {
	var idExpireDate = $.trim($('#idCardIndate').val());
	if (idExpireDate == '') {
		show_tips('请选择身份证有效期')
		return;
	}
	var nowDateTime = Date.parse(new Date().toLocaleDateString());
	var idExpireDateTime = Date.parse(formatDateNum1(idExpireDate));
	if (idExpireDateTime > nowDateTime) {
		$.ajax({
			url: '/AppService/business/updateIdExpireDateByCustNo.xhtml',
			data: {
				'idExpireDate': idExpireDate
			},
			dataType: 'json',//服务器返回json格式数据
			type: 'post',//HTTP请求类型
			success: function (data) {
				// 重新查询用户信息
				if (data.resultCode == '9999') {
					show_tips(data.resultMsg)
					setTimeout(function () {
						$('#idCard-select-pop').hide()
					}, 1500)
					// 修改成功
				} else if (data.resultCode == '0000') {
					$('#idCard-select-pop').hide()
					show_tips("修改成功");
					queryUserinfoAccount()
					$('#idCardSet').html('已设置');
					$('#idCardSet').next().html('重新设置');
					$('#idCardSet').addClass('act');

				}

			},
			error: function () {
				show_tips("网络繁忙，请稍后再试。");
			}
		});
	} else {
		show_tips('身份证有效期须大于当前日期')
		return;
	}
}

// 查询 身份证上传记录
function queryIcUploadInfo() {
	$.ajax({
		url: '/AppService/business/queryFileUploadRecord.xhtml',
		type: 'GET',
		dataType: 'json',
		async: true,
		success: function (data) {
			if (data.data.length > 0) {
				var extendInfo = dataReorganization(data.data);
				if (extendInfo.idCardPortraitificate.length>0 && extendInfo.idCardNationalEmblemcate.length>0) {
					$('#idCardImgSet').html('已上传');
					$('#idCardImgSet').addClass('act');
					$('#idCardImgSet').next().html("重新上传");
				}else if(! extendInfo.idCardPortraitificate || !extendInfo.idCardNationalEmblemcate){
					$('#idCardImgSet').html('未上传');
					$('#idCardImgSet').removeClass('act');
					$('#idCardImgSet').next().html("去上传");
				}

			}
		},
		error: function () {
			show_tips("网络繁忙，请稍后再试。");
		}
	});
}


// 关闭身份证选择框
function cancelSub() {
	$('#idCard-select-pop').hide();
}

/**
 * 日期格式转换()中文
 * @param date
 * @returns
 */
function formatDateChinese(date) {
	if (date == null || date == '') {
		return "";
	}
	var y = date.substr(0, 4);
	var m = date.substr(4, 2);
	var d = date.substr(6, 2);
	return y + "年" + m + "月" + d + "日";
}
/**
 * 日期格式转换 '-'
 * @param date
 * @returns
 */
function formatDateNum(date) {
	if (date == null || date == '') {
		return "";
	}
	var y = date.substr(0, 4);
	var m = date.substr(4, 2);
	var d = date.substr(6, 2);
	return y + "-" + m + "-" + d;
}

/**
	 * 重新组装json数据
	 * @param {Object} obj
	 */
function dataReorganization(obj) {
	var dataReorganization = {};
	var financialCertificate = [];
	var investmentCertificate = [];
	var idCardPortraitificate = [];
	var idCardNationalEmblemcate = [];
	obj.forEach(function (e) {
		var qualified = {};
		qualified.filename = e.fileName;
		qualified.key = e.fileKey;
		qualified.recordId = e.recordId;
		if (e.fileType == "financialCertificate") {
			financialCertificate.push(qualified)
		} else if (e.fileType == "investCertificate") {
			investmentCertificate.push(qualified)
			// 身份证人像图
		} else if (e.fileType == "idCardPortrait") {
			idCardPortraitificate.push(qualified)
		} else if (e.fileType == "idCardNationalEmblem") {
			idCardNationalEmblemcate.push(qualified)
		}
	})
	dataReorganization.financialCertificate = financialCertificate
	dataReorganization.investmentCertificate = investmentCertificate
	dataReorganization.idCardPortraitificate = idCardPortraitificate
	dataReorganization.idCardNationalEmblemcate = idCardNationalEmblemcate
	return dataReorganization;
}

/**
* 日期格式转换 '-'
* @param date
* @returns
*/
function formatDateNum1(date) {
	if (date == null || date == '') {
		return "";
	}
	date = unformat1(date);
	return parseInt(date.substr(0, 4), 10) + "/" + parseInt(date.substr(4, 2), 10) + "/" + parseInt(date.substr(6, 2), 10);
}

// 关闭上传身份证弹框
function closePop() {
	$('#recommendPop').hide()
	$('.backLayer').hide()
}

// 校验是否为中文
function ischinese(name){
    return /^[\u4e00-\u9fa5]+$/.test(name)
}  

// 查看身份证上传示例
 //展示身份证示例 
 function showIcExam(){
	$('#icExampleBox').show()
	$('.backLayer').show()
}

function closeExample1(){
	$('#icExampleBox').hide()
}