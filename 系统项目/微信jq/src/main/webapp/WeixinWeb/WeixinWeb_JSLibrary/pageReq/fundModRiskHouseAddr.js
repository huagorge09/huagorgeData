﻿var eventId, pageSource;
$(function () {
	var urlParams = getUrlParams();
	eventId = urlParams['eventId'] ? urlParams['eventId'] : '',
		pageSource = urlParams['pageSource'] ? urlParams['pageSource'] : '';
});

function queryAddressInfo() {
	$(".header #titleBack").unbind("click");
	$(".header #titleBack").bind("click", function () {
		cacelAddressUpdate();
	});

	$(".header .top-a h2").html("风险测评");
	document.title = "风险测评";

	var viewType = GetQueryString("viewType");
	$("#viewType").val(viewType);


	if (!!viewType && "prodcut" == viewType) {
		$("a[class=cancel-address]").remove();
	}

	var vocCode = GetQueryString("vocCode");
	$("#vocCode").val(vocCode);
	//选择职业完成后会有修改链接和隐藏域的值

	var otherVocation = GetQueryString("otherVocation");
	$("#otherVocation").val(otherVocation);
	
	var vocName = GetQueryString("vocName");
	$("#vocName").val(vocName);
	$("#vocNameText").html(!vocName ? "请选择" : vocCode == "15" ? otherVocation : vocName);


	var region = GetQueryString("region");
	var address = GetQueryString("address");
	if (region != null && region != "") {
		$("#area").val(region);
	}
	if (address != null && address != "") {
		$("#address").val(address);
	}

	var nation = GetQueryString("nation");
	var nationNM = GetQueryString("nationNM");
	$("#nation").val(nation);
	$("#nationNM").val(nationNM);
	var province = GetQueryString("province");
	var provinceNM = GetQueryString("provinceNM");
	$("#province").val(province);
	$("#provinceNM").val(provinceNM);
	var city = GetQueryString("city");
	var cityNM = GetQueryString("cityNM");
	$("#city").val(city);
	$("#cityNM").val(cityNM);
	var addressStr = "";
	if (nationNM != "" && nationNM != null) {
		addressStr += nationNM + " ";
	}
	if (provinceNM != "" && provinceNM != null) {
		addressStr += provinceNM + " ";
	}
	if (cityNM != "" && cityNM != null) {
		addressStr += cityNM;
	}
	if (addressStr != "" && addressStr != null) {
		$("#area").val(addressStr);
	}

	var dateOfBirth = GetQueryString("dateOfBirth");
	$("input[name=dateOfBirth]").val(dateOfBirth);
	var taxResidentType = GetQueryString("taxResidentType");
	var taxResidentTypeNM = GetQueryString("taxResidentTypeNM");
	$("#taxResidentType").val(taxResidentType);
	$("#taxResidentTypeNM").val(taxResidentTypeNM);
	$("#taxResidentText").html(!taxResidentTypeNM ? "请选择" : taxResidentTypeNM);

	var taxResidentData = GetQueryString("taxResidentData");
	$("#taxResidentData").val(taxResidentData);

	// 地址栏 没有数据则 从数据库获取
	if (!!taxResidentType && !taxResidentData) {
		taxResidentData = queryUserTaxInfo();
		$("#taxResidentData").val(taxResidentData);
	}

	var taxResidentDataVal = $("#taxResidentData").val();

	taxResidentDataVal = eval(taxResidentDataVal);
	if (!!taxResidentDataVal) {
		// 加载英文姓及英文名
		$("#englishSurname").val(taxResidentDataVal[0].englishSurname);
		$("#englishName").val(taxResidentDataVal[0].englishName);

		// 加载居住地国家及居住地址和居住地址（英文）
		if (taxResidentDataVal[0].taxResideNation != '5') {
			$("#resideNationText").html(!taxResidentDataVal[0].taxResideNationNM ? "请选择" : taxResidentDataVal[0].taxResideNationNM);
		} else {
			$("#resideNationText").html(!taxResidentDataVal[0].taxResideNationNM ? "请选择" : taxResidentDataVal[0].taxResideNationNM);
		}
		$("#reside_nation").val(taxResidentDataVal[0].taxResideNation);
		$("#reside_region").val(taxResidentDataVal[0].taxResideRegion);

		$("#reside_nation_nm").val(taxResidentData[0].taxResideNationNM);
		$("#reside_region_nm").val(taxResidentData[0].taxResideRegionNM);

		$("#reside_address").val(taxResidentDataVal[0].taxResideAddress);
		$("#reside_address_english").val(taxResidentDataVal[0].taxResideAddressEnglish);
	}
};

function saveAddress() {
	var region = GetQueryString("region");

	var nation = $("#nation").val();
	var province = $("#province").val();
	var city = $("#city").val();
	var address = $("#address").val();
	var vocode = $("#vocCode").val();
	var dateOfBirth = $("input[name=dateOfBirth]").val();
	var taxResidentType = $("#taxResidentType").val();
	var taxResidentData = $("#taxResidentData").val();
	var otherVocation = $("#otherVocation").val();

	if (!dateOfBirth) {
		errorRemark("请选择出生日期！");
		return;
	}

	dateOfBirth = formatDate(dateOfBirth);
	var flag = isDate(dateOfBirth);
	if (!flag) {
		errorRemark("出生日期格式错误！");
		return;
	}

	if (!vocode) {
		errorRemark("请选择职业！");
		return;
	}

	if (!nation) {
		errorRemark("请选择所在地区！");
		return;
	}

	if (!taxResidentType) {
		errorRemark("请完善税收居民信息！");
		return;
	}

	if (!!taxResidentType && ("1" != taxResidentType && !taxResidentData)) {
		errorRemark("请完善税收居民信息！");
		return;
	}

	var fundRiskDeclConfirm = document.getElementById("fundRiskDeclConfirm").checked;
	if (!fundRiskDeclConfirm) {
		errorRemark("请阅读并勾选同意声明！");
		return;
	}

	$.ajax({
		async: !1,
		url: "/WeixinService/business/updateCmfUserBaseInfo.xhtml",
		data: {
			nation: nation,
			province: province,
			city: city,
			addr: address,
			voccode: vocode,
			birthDate: dateOfBirth,
			taxResidentType: taxResidentType,
			taxResidentData: taxResidentData,
			otherVocation : otherVocation
		},
		dataType: "json",
		cache: !1,
		type: "POST",
		error: function () {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success: function (n) {
			if (n != null && n.resultCode == "0000") {
				// 继续测评流程
				// redirectUrl("/WeixinService/business/query/fundList.shtml?riskContinue=Y");

				redirectUrl("/WeixinService/business/user/riskLevel.shtml?viewType=prodcut&birthDate=" + dateOfBirth
					+ "&eventId=" + eventId + "&pageSource=" + pageSource);
			} else {
				errorRemark("修改地址失败");
			}
		}
	});
}

function cacelAddressUpdate() {
	window.location.href = "/WeixinService/business/query/fundList.shtml";
}

function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null) {
		var val = decodeURI(r[2]);
		return unescape(val);
	} else {
		return null;
	}
}

function loadNationInfo() {
	$(".header .top-a h2").html("风险测评");
	document.title = "风险测评";
	var nationalityData = queryParamList("DS", "DS_NATION", "");
	var nationHtml = "";
	for (var i = 0; i < nationalityData.length; i++) {
		var temp = nationalityData[i];
		if (temp.pmnm.indexOf("中国") > -1) {
			nationHtml += '<a class="href-point" data-value="' + temp.pmco + '" data-text="' + temp.pmnm + '">' + temp.pmnm + '<i id="area"></i><em></em></a>';
		} else {
			nationHtml += '<a class="href-point" data-value="' + temp.pmco + '" data-text="' + temp.pmnm + '">' + temp.pmnm + '<i id="area"></i></a>';
		}
	}
	var address = GetQueryString("address");
	$("#address").val(address);

	var vocCode = GetQueryString("vocCode");
	$("#vocCode").val(vocCode);

	var vocName = GetQueryString("vocName");
	$("#vocName").val(vocName);

	var viewType = GetQueryString("viewType");
	$("#viewType").val(viewType);

	var dateOfBirth = GetQueryString("dateOfBirth");
	$("#dateOfBirth").val(dateOfBirth);
	var taxResidentType = GetQueryString("taxResidentType");
	$("#taxResidentType").val(taxResidentType);
	var taxResidentTypeNM = GetQueryString("taxResidentTypeNM");
	$("#taxResidentTypeNM").val(taxResidentTypeNM);
	var taxResidentData = GetQueryString("taxResidentData");
	$("#taxResidentData").val(taxResidentData);
	
	var otherVocation = GetQueryString("otherVocation");
	$("#otherVocation").val(otherVocation);


	$("#section_01 .center-accountset").html(nationHtml);
	$("#section_01 a.href-point").click(function () {
		var param = "?";
		var nation = $(this).data("value");
		var nationNM = $(this).data("text");
		var vocCode = $("#vocCode").val();
		var vocName = $("#vocName").val();
		var address = $("#address").val();
		var dateOfBirth = $("#dateOfBirth").val();
		var taxResidentType = $("#taxResidentType").val();
		var taxResidentTypeNM = $("#taxResidentTypeNM").val();
		var taxResidentData = $("#taxResidentData").val();
		var viewType = $("#viewType").val();
		var otherVocation = $("#otherVocation").val();
		param += "nation=" + nation + "&nationNM=" + encodeURI(nationNM) + "&address=" + encodeURI(address) + "&vocCode="
			+ vocCode + "&vocName=" + encodeURI(vocName) + "&dateOfBirth=" + dateOfBirth + "&taxResidentType=" + taxResidentType + "&taxResidentTypeNM="
			+ taxResidentTypeNM + "&taxResidentData=" + taxResidentData + "&viewType=" + viewType
			+ "&otherVocation=" + otherVocation + "&eventId=" + eventId + "&pageSource=" + pageSource;

		var emLen = $(this).find("em").length;
		if (emLen > 0) {
			window.location.href = "/WeixinService/business/query/fundProvinceInfo.shtml" + param;
		} else {
			window.location.href = "/WeixinService/business/query/fundModRiskHouseAddr.shtml" + param;
		}
	})
};

function queryParamList(paramType, paramKey, pmValueOne) {
	var data = null;
	$.ajax({
		async: !1,
		url: "/WeixinService/business/queryParamList.xhtml",
		data: {
			paramType: paramType,
			paramKey: paramKey,
			pmValueOne: pmValueOne
		},
		dataType: "json",
		cache: !1,
		type: "POST",
		error: function () {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success: function (n) {
			if (n != null && n.resultCode == "0000") {
				data = n.data;
			}
		}
	});
	return data;
}


function loadProvinceInfo() {
	$(".header .top-a h2").html("风险测评");
	document.title = "风险测评";
	var nation = GetQueryString("nation");
	var nationNM = GetQueryString("nationNM");
	$("#nation").val(nation);
	$("#nationNM").val(nationNM);

	var address = GetQueryString("address");
	$("#address").val(address);

	var vocCode = GetQueryString("vocCode");
	$("#vocCode").val(vocCode);

	var vocName = GetQueryString("vocName");
	$("#vocName").val(vocName);

	var viewType = GetQueryString("viewType");
	$("#viewType").val(viewType);

	var dateOfBirth = GetQueryString("dateOfBirth");
	$("#dateOfBirth").val(dateOfBirth);
	var taxResidentType = GetQueryString("taxResidentType");
	$("#taxResidentType").val(taxResidentType);
	var taxResidentTypeNM = GetQueryString("taxResidentTypeNM");
	$("#taxResidentTypeNM").val(taxResidentTypeNM);
	var taxResidentData = GetQueryString("taxResidentData");
	$("#taxResidentData").val(taxResidentData);
	
	var otherVocation = GetQueryString("otherVocation");
	$("#otherVocation").val(otherVocation);


	var provinceData = queryParamList("SYSTEM", "DS_PROVINCE", nation);
	var provinceHtml = "";
	for (var i = 0; i < provinceData.length; i++) {
		var temp = provinceData[i];
		if (temp.leafNum > 1) {
			provinceHtml += '<a class="href-point" data-value="' + temp.pmco + '" data-text="' + temp.pmnm + '">' + temp.pmnm + '<i id="area"></i><em></em></a>';
		} else {
			provinceHtml += '<a class="href-point" data-value="' + temp.pmco + '" data-text="' + temp.pmnm + '">' + temp.pmnm + '<i id="area"></i></a>';
		}
	}
	$("#section_02 .center-accountset").html(provinceHtml);
	$("#section_02 a.href-point").click(function () {
		var param = "?";
		var nation = $("#nation").val();
		var nationNM = $("#nationNM").val();
		var province = $(this).data("value");
		var provinceNM = $(this).data("text");
		var vocCode = $("#vocCode").val();
		var vocName = $("#vocName").val();
		var address = $("#address").val();
		var dateOfBirth = $("#dateOfBirth").val();
		var taxResidentType = $("#taxResidentType").val();
		var taxResidentTypeNM = $("#taxResidentTypeNM").val();
		var taxResidentData = $("#taxResidentData").val();
		var viewType = $("#viewType").val();
		var otherVocation = $("#otherVocation").val();
		param += "nation=" + nation + "&nationNM=" + encodeURI(nationNM) + "&province=" + province + "&provinceNM=" + encodeURI(provinceNM) +
			"&address=" + encodeURI(address) + "&vocCode=" + vocCode + "&vocName=" + encodeURI(vocName) + "&dateOfBirth=" + dateOfBirth + "&taxResidentType="
			+ taxResidentType + "&taxResidentTypeNM=" + taxResidentTypeNM + "&taxResidentData=" + taxResidentData + "&viewType=" + viewType
			+ "&otherVocation=" + otherVocation + "&eventId=" + eventId + "&pageSource=" + pageSource;
		var emLen = $(this).find("em").length;
		if (emLen > 0) {
			window.location.href = "/WeixinService/business/query/fundCityInfo.shtml" + param;
		} else {
			window.location.href = "/WeixinService/business/query/fundModRiskHouseAddr.shtml" + param;
		}
	})
};

function loadCityInfo() {
	$(".header .top-a h2").html("风险测评");
	document.title = "风险测评";
	var nation = GetQueryString("nation");
	var nationNM = GetQueryString("nationNM");
	$("#nation").val(nation);
	$("#nationNM").val(nationNM);
	var province = GetQueryString("province");
	var provinceNM = GetQueryString("provinceNM");
	$("#province").val(province);
	$("#provinceNM").val(provinceNM);

	var address = GetQueryString("address");
	$("#address").val(address);

	var vocCode = GetQueryString("vocCode");
	$("#vocCode").val(vocCode);

	var vocName = GetQueryString("vocName");
	$("#vocName").val(vocName);

	var viewType = GetQueryString("viewType");
	$("#viewType").val(viewType);

	var dateOfBirth = GetQueryString("dateOfBirth");
	$("#dateOfBirth").val(dateOfBirth);
	var taxResidentType = GetQueryString("taxResidentType");
	$("#taxResidentType").val(taxResidentType);
	var taxResidentTypeNM = GetQueryString("taxResidentTypeNM");
	$("#taxResidentTypeNM").val(taxResidentTypeNM);
	var taxResidentData = GetQueryString("taxResidentData");
	$("#taxResidentData").val(taxResidentData);

	var otherVocation = GetQueryString("otherVocation");
	$("#otherVocation").val(otherVocation);

	var cityData = queryParamList("SYSTEM", "DS_CITYCODE", province);
	var cityHtml = "";
	for (var i = 0; i < cityData.length; i++) {
		var temp = cityData[i];
		cityHtml += '<a class="href-point" data-value="' + temp.pmco + '" data-text="' + temp.pmnm + '">' + temp.pmnm + '<i id="area"></i></a>';
	}
	$("#section_03 .center-accountset").html(cityHtml);
	$("#section_03 a.href-point").click(function () {
		var param = "?";
		var nation = $("#nation").val();
		var nationNM = $("#nationNM").val();
		var province = $("#province").val();
		var provinceNM = $("#provinceNM").val();
		var city = $(this).data("value");
		var cityNM = $(this).data("text");
		var vocCode = $("#vocCode").val();
		var vocName = $("#vocName").val();
		var address = $("#address").val();
		var dateOfBirth = $("#dateOfBirth").val();
		var taxResidentType = $("#taxResidentType").val();
		var taxResidentTypeNM = $("#taxResidentTypeNM").val();
		var taxResidentData = $("#taxResidentData").val();
		var viewType = $("#viewType").val();
		var otherVocation = $("#otherVocation").val();

		param += "nation=" + nation + "&nationNM=" + encodeURI(nationNM) + "&province=" + province + "&provinceNM=" + encodeURI(provinceNM)
			+ "&city=" + city + "&cityNM=" + encodeURI(cityNM) + "&address=" + encodeURI(address) + "&vocCode=" + vocCode + "&vocName=" + encodeURI(vocName)
			+ "&dateOfBirth=" + dateOfBirth + "&taxResidentType=" + taxResidentType + "&taxResidentTypeNM=" + taxResidentTypeNM + "&taxResidentData=" + taxResidentData + "&viewType=" + viewType
			+ "&otherVocation=" + otherVocation + "&eventId=" + eventId + "&pageSource=" + pageSource;
		window.location.href = "/WeixinService/business/query/fundModRiskHouseAddr.shtml" + param;
	})
};

// 选择 职业
function goFundProfessionInfoView() {
	var param = "?";
	var nation = $("#nation").val();
	var nationNM = $("#nationNM").val();
	var province = $("#province").val();
	var provinceNM = $("#provinceNM").val();
	var city = $("#city").val();
	var cityNM = $("#cityNM").val();
	var vocCode = $("#vocCode").val();
	var vocName = $("#vocName").val();
	var address = $("#address").val();
	var dateOfBirth = $("input[name=dateOfBirth]").val();
	var taxResidentType = $("#taxResidentType").val();
	var taxResidentTypeNM = $("#taxResidentTypeNM").val();
	var taxResidentData = $("#taxResidentData").val();
	var viewType = $("#viewType").val();
	var otherVocation = $("#otherVocation").val();

	param += "viewType=" + viewType + "&nation=" + nation + "&nationNM=" + encodeURI(nationNM) + "&province=" + province + "&provinceNM=" + encodeURI(provinceNM)
		+ "&city=" + city + "&cityNM=" + encodeURI(cityNM) + "&address=" + encodeURI(address) + "&vocCode=" + vocCode + "&vocName=" + encodeURI(vocName)
		+ "&dateOfBirth=" + dateOfBirth + "&taxResidentType=" + taxResidentType + "&taxResidentTypeNM=" + taxResidentTypeNM + "&taxResidentData=" + taxResidentData
		+ "&otherVocation=" + otherVocation + "&eventId=" + eventId + "&pageSource=" + pageSource;

	redirectUrl('/WeixinService/business/query/fundProfessionInfo.shtml' + param)
};


function goFundNationInfoView() {
	var param = "?";
	var vocCode = $("#vocCode").val();
	var vocName = $("#vocName").val();
	var address = $("#address").val();
	var dateOfBirth = $("input[name=dateOfBirth]").val();
	var taxResidentType = $("#taxResidentType").val();
	var taxResidentTypeNM = $("#taxResidentTypeNM").val();
	var taxResidentData = $("#taxResidentData").val();
	var viewType = $("#viewType").val();
	var otherVocation = $("#otherVocation").val();
	param += "viewType=" + viewType + "&address=" + encodeURI(address) + "&vocCode=" + vocCode + "&vocName=" + encodeURI(vocName)
		+ "&dateOfBirth=" + dateOfBirth + "&taxResidentType=" + taxResidentType + "&taxResidentTypeNM=" + taxResidentTypeNM + "&taxResidentData=" + taxResidentData
		+ "&otherVocation=" + otherVocation + "&eventId=" + eventId + "&pageSource=" + pageSource;

	redirectUrl('/WeixinService/business/query/fundNationInfo.shtml' + param);
};

function goFundTaxResidentTypeView() {
	var taxResidentData = $("#taxResidentData").val();
	var param = getAllQueryString();
	param = param + "&taxResidentData=" + taxResidentData + "&eventId=" + eventId + "&pageSource=" + pageSource;
	redirectUrl('/WeixinService/business/query/fundTaxResidentType.shtml' + param);
}

function goFundTaxNationInfoView(nationType, targetId) {
	var taxResidentData = getTaxResidentData();
	if (!targetId) {
		targetId = "";
	}
	var param = getAllQueryString() + "&nationType=" + nationType + "&targetId=" + targetId + "&taxResidentData=" + encodeURI(taxResidentData)
		+ "&eventId=" + eventId + "&pageSource=" + pageSource;
	redirectUrl('/WeixinService/business/query/fundTaxNationInfo.shtml' + param);
};

function goFundTaxNotCodeCauseView(targetId) {
	var taxResidentData = getTaxResidentData();
	if (!targetId) {
		targetId = "";
	}

	var param = getAllQueryString() + "&targetId=" + targetId + "&taxResidentData=" + encodeURI(taxResidentData)
		+ "&eventId=" + eventId + "&pageSource=" + pageSource;
	redirectUrl('/WeixinService/business/query/fundTaxNotCodeCause.shtml' + param);
}

function getAllQueryString() {
	var param = "?";
	var nation = $("#nation").val();
	var nationNM = $("#nationNM").val();
	var province = $("#province").val();
	var provinceNM = $("#provinceNM").val();
	var city = $("#city").val();
	var cityNM = $("#cityNM").val();
	var vocCode = $("#vocCode").val();
	var vocName = $("#vocName").val();
	var address = $("#address").val();
	var dateOfBirth = $("input[name=dateOfBirth]").val();
	var taxResidentType = $("#taxResidentType").val();
	var taxResidentTypeNM = $("#taxResidentTypeNM").val();
	var sourceType = $("#sourceType").val();
	var viewType = $("#viewType").val();
	var otherVocation = $("#otherVocation").val();
	param += "nation=" + nation + "&nationNM=" + encodeURI(nationNM) + "&province=" + province + "&provinceNM=" + encodeURI(provinceNM)
		+ "&city=" + city + "&cityNM=" + encodeURI(cityNM) + "&address=" + encodeURI(address) + "&vocCode=" + vocCode
		+ "&vocName=" + encodeURI(vocName) + "&dateOfBirth=" + dateOfBirth + "&taxResidentType=" + taxResidentType + "&taxResidentTypeNM=" + taxResidentTypeNM
		+ "&sourceType=" + sourceType + "&viewType=" + viewType
		+ "&otherVocation=" + otherVocation + "&eventId=" + eventId + "&pageSource=" + pageSource
		;

	return param;
}

/**
 * 是否 无纳税人识别号
 */
function isTaxpayerEvent(e) {
	var checked = e.checked;
	var target = $(e);
	if (checked) {
		// 如果为 是
		target.parent().prev().attr("readonly", "readonly");
		// target.parent().prev().attr("disabled","disabled");
		target.parent().prev().val(null);
		target.parent().parent().next().show();
	} else {
		target.parent().prev().removeAttr("readonly");
		// target.parent().prev().removeAttr("disabled");
		target.parent().parent().next().hide();
	}
}


function getTaxResidentData() {
	var taxResidentData = "";
	var birthNation = $("#birthNation");
	var birthNationNM = $("#birthNationNM");
	var birthArea = $("#birthArea");
	var birthAddress = $("#birthAddress");
	// 英文姓和英文名
	var englishSurname = $("#englishSurname");
	var englishName = $("#englishName");
	// 现居地址及现居地址英文
	var taxResideNation = $("#reside_nation");
	var taxResideRegion = $("#reside_region");
	var taxResideNationNM = $("#reside_nation_nm");
	var taxResideRegionNM = $("#reside_region_nm");

	var resideAddress = $("#reside_address");
	var resideAddressEnglish = $("#reside_address_english");


	var taxResList = $("div[class=tax_add_div]div[id!=tax_template]");
	var taxResValList = new Array();

	for (var i = 0; i < taxResList.length; i++) {
		var taxResVal = {};
		var taxRes = $(taxResList[i]);
		var taxNationality, taxNationalityNM, taxArea, taxpayerCode, isTaxpayerCode, notCodeCause, notCodeCauseNM, notGetCause;
		var divId = taxRes.attr("id");
		taxNationality = taxRes.find("input[name=taxNationality]:eq(0)");
		taxNationalityNM = taxRes.find("input[name=taxNationalityNM]:eq(0)");

		taxArea = taxRes.find("input[name=taxArea]:eq(0)");

		taxpayerCode = taxRes.find("input[name=taxpayerCode]:eq(0)");
		isTaxpayerCode = taxRes.find("input[name=isTaxpayerCode]:eq(0)");

		notCodeCause = taxRes.find("input[name=notCodeCause]:eq(0)");
		notCodeCauseNM = taxRes.find("input[name=notCodeCauseNM]:eq(0)");

		notGetCause = taxRes.find("input[name=notGetCause]:eq(0)");
		// 序号
		taxResVal.sortNo = i;
		// 纳税居民国
		taxResVal.taxNationality = taxNationality.val();
		taxResVal.taxNationalityNM = taxNationalityNM.val();
		taxResVal.taxArea = taxArea.val();
		if (taxNationality.val() == '1' && taxArea.val() == '1') {
			taxResVal.taxArea = "156-1";
		}
		taxResVal.divId = divId;
		taxResVal.taxBirthNation = birthNation.val();
		taxResVal.taxBirthNationNM = birthNationNM.val();
		taxResVal.taxBirthRegion = birthArea.val();
		taxResVal.taxBirthAddress = birthAddress.val();

		// 英文姓和英文名
		taxResVal.englishSurname = englishSurname.val();
		taxResVal.englishName = englishName.val();
		// 现居地址及现居地址英文
		taxResVal.taxResideNation = taxResideNation.val();
		taxResVal.taxResideNationNM = taxResideNationNM.val();
		taxResVal.taxResideRegion = taxResideRegion.val();
		taxResVal.taxResideRegionNM = taxResideRegionNM.val();
		taxResVal.taxResideAddress = resideAddress.val();
		taxResVal.taxResideAddressEnglish = resideAddressEnglish.val();

		// 无纳税号 是否勾选
		if (!isTaxpayerCode[0].checked) {
			// 不勾选 则纳税号 必填
			taxResVal.taxPayerCode = taxpayerCode.val();
			taxResVal.taxNotCodeCause = "";
			taxResVal.taxNotCodeCauseNM = "";
			taxResVal.taxnotGetCause = "";
		} else {
			// 如果勾选 纳税号 置为空
			taxResVal.taxPayerCode = "";
			taxResVal.taxNotCodeCause = notCodeCause.val();
			taxResVal.taxNotCodeCauseNM = notCodeCauseNM.val();
			taxResVal.taxnotGetCause = "";
			// 如果填的是 未取得 则 未取得原因必填
			if ("2" == notCodeCause.val()) {
				taxResVal.taxnotGetCause = notGetCause.val();
			}
		}
		taxResValList.push(taxResVal);
	}
	taxResidentData = JSON.stringify(taxResValList);
	return taxResidentData;
}

/**
 * 校验 税收信息 是否填写完毕 请完善税收居民信息
 * 
 * @returns 任一条件不满足则返回 false
 */
function checkTaxResidentData() {
	var flag = true;
	var taxResidentType = $("#taxResidentType");
	var birthNation = $("#birthNation");
	var birthArea = $("#birthArea");
	var birthAddress = $("#birthAddress");

	if (!taxResidentType.val()) {
		flag = false;
	}

	// 居民类型为 非居民 则 校验以下信息
	if (flag && "1" != taxResidentType.val()) {
		if (flag && (!birthNation.val() || !birthArea.val())) {
			flag = false;
			return flag;
		}
		var taxResList = $("div[class=tax_add_div]div[id!=tax_template]");
		for (var i = 0; i < taxResList.length; i++) {
			var taxRes = $(taxResList[i]);

			var taxNationality, taxArea, taxpayerCode, isTaxpayerCode, notCodeCause, notGetCause;
			taxNationality = taxRes.find("input[name=taxNationality]:eq(0)");
			taxArea = taxRes.find("input[name=taxArea]:eq(0)");
			taxpayerCode = taxRes.find("input[name=taxpayerCode]:eq(0)");
			isTaxpayerCode = taxRes.find("input[name=isTaxpayerCode]:eq(0)");
			notCodeCause = taxRes.find("input[name=notCodeCause]:eq(0)");
			notGetCause = taxRes.find("input[name=notGetCause]:eq(0)");
			// 纳税居民国是否为空
			if (!taxNationality.val() || !taxArea.val()) {
				flag = false;
			}

			// 无纳税号 是否勾选
			if (!isTaxpayerCode[0].checked) {
				// 不勾选 则纳税号 必填
				if (!taxpayerCode.val()) {
					flag = false;
				}
			} else {
				// 如果勾选 无识别号原因必填
				if (!notCodeCause.val()) {
					flag = false;
				} else {
					// 如果填的是 未取得 则 未取得原因必填
					if ("2" == notCodeCause.val()) {
						if (!notGetCause.val()) {
							flag = false;
						}
					}
				}
			}
		}
	}
	return flag;
}

function addTaxNation() {
	var count = $("div[class=tax_add_div]").length;
	count++;
	// 获取模板内容
	var addContent = $("#tax_template").html();
	// 添加至目标
	var target = $("div[class=tax_add_div]:last");
	// 目标id
	var targetId = "tax_add_" + count;
	// 开始添加元素
	target.after("<div class=\"tax_add_div\" id=\"" + targetId + "\">" + addContent + "</div>");
	// 居民国目标
	$("#" + targetId + " i[name=taxNationEventTar]").attr("onclick", "goFundTaxNationInfoView('tax','" + targetId + "')");;
	// 无识别号 目标
	$("#" + targetId + " a[name=notCodeCauseEventTar]").attr("href", "javascript:goFundTaxNotCodeCauseView('" + targetId + "')");
	// 以下为 修改 按钮参数 和 初始化控件
	$("#" + targetId + " img").attr("src", "/WeixinWeb/WeixinWeb_Images/images/bt_reduction.png").attr("onclick", "delTaxNation('" + targetId + "')");
	return targetId;
}

function delTaxNation(elementId) {
	$("#" + elementId).remove();
}

function taxInfoViewLoadGetParam() {
	var nation = GetQueryString("nation");
	var nationNM = GetQueryString("nationNM");
	$("#nation").val(nation);
	$("#nationNM").val(nationNM);
	var province = GetQueryString("province");
	var provinceNM = GetQueryString("provinceNM");
	$("#province").val(province);
	$("#provinceNM").val(provinceNM);
	var city = GetQueryString("city");
	var cityNM = GetQueryString("cityNM");
	$("#city").val(city);
	$("#cityNM").val(cityNM);
	var vocCode = GetQueryString("vocCode");
	$("#vocCode").val(vocCode);
	var vocName = GetQueryString("vocName");
	$("#vocName").val(vocName);
	var address = GetQueryString("address");
	$("#address").val(address);
	var dateOfBirth = GetQueryString("dateOfBirth");
	$("#dateOfBirth").val(dateOfBirth);
	var taxResidentType = GetQueryString("taxResidentType");
	var taxResidentTypeNM = GetQueryString("taxResidentTypeNM");
	$("#taxResidentType").val(taxResidentType);
	$("#taxResidentTypeNM").val(taxResidentTypeNM);
	var taxResidentData = GetQueryString("taxResidentData");
	$("#taxResidentData").val(taxResidentData);
	var sourceType = GetQueryString("sourceType");
	$("#sourceType").val(sourceType);
	var viewType = GetQueryString("viewType");
	$("#viewType").val(viewType);
	var otherVocation = GetQueryString("otherVocation");
	$("#otherVocation").val(otherVocation);

	taxResidentData = eval(taxResidentData);
	if (!!taxResidentData) {
		// 加载英文姓及英文名
		$("#englishSurname").val(taxResidentData[0].englishSurname);
		$("#englishName").val(taxResidentData[0].englishName);

		// 加载居住地国家及居住地址和居住地址（英文）
		if (taxResidentData[0].taxResideNation != '5') {
			$("#resideNationText").html(!taxResidentData[0].taxResideNationNM ? "请选择" : taxResidentData[0].taxResideNationNM);
		} else {
			$("#resideNationText").html(!taxResidentData[0].taxResideNationNM ? "请选择" : taxResidentData[0].taxResideNationNM);
		}
		$("#reside_nation").val(taxResidentData[0].taxResideNation);
		$("#reside_region").val(taxResidentData[0].taxResideRegion);

		$("#reside_nation_nm").val(taxResidentData[0].taxResideNationNM);
		$("#reside_region_nm").val(taxResidentData[0].taxResideRegionNM);

		$("#reside_address").val(taxResidentData[0].taxResideAddress);
		$("#reside_address_english").val(taxResidentData[0].taxResideAddressEnglish);
	}

}

function loadTaxInfoByTaxResidentType() {
	var taxResidentType = $("#taxResidentType").val();

	var targetId = addTaxNation();
	$("#" + targetId + " img").remove();

	if ("3" == taxResidentType) {
		var newTargetId = addTaxNation();
		$("#" + newTargetId + " img").attr("src", "/WeixinWeb/WeixinWeb_Images/images/bt_add1.png").attr("onclick", "addTaxNation()");
	}
}


function showUserTaxInfo() {
	var taxResidentData = GetQueryString("taxResidentData");
	if (!!taxResidentData) {
		$("#taxResidentData").val(taxResidentData);
		var deTaxResData = decodeURI(taxResidentData);
		var taxResJsonData = JSON.parse(deTaxResData);
		var taxAddDivs = $("div[class=tax_add_div]div[id!=tax_template]");
		for (var i = 0; i < taxResJsonData.length; i++) {
			var taxInfo = taxResJsonData[i];
			$("#birthNation").val(taxInfo.taxBirthNation);
			$("#birthNationNM").val(taxInfo.taxBirthNationNM);
			$("#birthNationText").html(!taxInfo.taxBirthNationNM ? "请选择" : taxInfo.taxBirthNationNM);
			$("#birthArea").val(taxInfo.taxBirthRegion);
			$("#birthAreaNM").val(taxInfo.taxBirthRegionNM);
			$("#birthAddress").val(taxInfo.taxBirthAddress);

			var taxAddDiv = $(taxAddDivs.get(i));
			if (!taxAddDivs.get(i)) {
				var targetId = addTaxNation();
				taxAddDiv = $("#" + targetId);
			}
			taxAddDiv.find("input[name=taxNationality]").val(taxInfo.taxNationality);
			taxAddDiv.find("input[name=taxNationalityNM]").val(taxInfo.taxNationalityNM);
			taxAddDiv.find("#taxNationalityText").html(!taxInfo.taxNationalityNM ? "请选择" : taxInfo.taxNationalityNM);
			taxAddDiv.find("input[name=taxArea]").val(taxInfo.taxArea);
			taxAddDiv.find("input[name=taxpayerCode]").val(taxInfo.taxPayerCode);
			taxAddDiv.find("input[name=notCodeCause]").val(taxInfo.taxNotCodeCause);
			taxAddDiv.find("input[name=notCodeCauseNM]").val(taxInfo.taxNotCodeCauseNM);
			taxAddDiv.find("#notCodeCauseText").html(!taxInfo.taxNotCodeCauseNM ? "请选择" : taxInfo.taxNotCodeCauseNM);
			taxAddDiv.find("input[name=notGetCause]").val(taxInfo.taxnotGetCause);
			if (!taxInfo.taxPayerCode && !!taxInfo.taxNotCodeCause) {
				taxAddDiv.find("input[name=isTaxpayerCode]").click();
			}
			if ("2" == taxInfo.taxNotCodeCause) {
				taxAddDiv.find("input[name=notGetCause]").parent().parent().show();
			}
		}
	}
}

function saveUserTaxInfoToAddrView() {
	var birthAddress = $("#birthAddress").val();
	var sourceType = $("#sourceType").val();
	if (!!birthAddress && birthAddress.length < 5) {
		errorRemark("出生地详细地址不能少于五个字！");
		return;
	}

	var englishSurname = $("#englishSurname").val();
	var englishName = $("#englishName").val();

	if (englishName == "") {
		errorRemark("请填写First Name！");
		return;
	}

	if (englishSurname == "") {
		errorRemark("请填写Last Name！");
		return;
	}

	var re = /^[\u2E80-\u9FFF]+$/;
	if (re.test(englishSurname)) {
		errorRemark("Last Name只能填写英文或拼音！");
		return;
	}
	if (re.test(englishName)) {
		errorRemark("First Name只能填写英文或拼音！");
		return;
	}

	var birthNation = $("#birthNation").val();
	var birthArea = $("#birthArea").val();
	var birthAddress = $("#birthAddress").val();
	if (birthNation == "") {
		errorRemark("请选择出生地国家！");
		return;
	}
	if (birthArea == "") {
		errorRemark("请选择出生地地区！");
		return;
	}

	var reside_nation = $("#reside_nation").val();
	if (reside_nation == "") {
		errorRemark("请选择现居国家！");
		return;
	}
	var reside_address = $("#reside_address").val();
	var reside_address_english = $("#reside_address_english").val();
	if (reside_nation != '5') {
		if (reside_address == "") {
			errorRemark("请填写现居地址！");
			return;
		}
	}
	if (reside_address_english == "") {
		errorRemark("请填写Address！");
		return;
	}

	if (re.test(reside_address_english)) {
		errorRemark("Address请填写英文或拼音地址！");
		return;
	}

	if (!checkTaxResidentData()) {
		errorRemark("请完善税收居民信息");
		return;
	}

	var param = getAllQueryString();
	var taxResidentData = encodeURI(getTaxResidentData());
	param = param + "&taxResidentData=" + taxResidentData + "&eventId=" + eventId + "&pageSource=" + pageSource;
	if ("product" == sourceType) {
		redirectUrl('/WeixinService/business/query/fundModRiskHouseAddr.shtml' + param);
	} else if ("user" == sourceType) {
		redirectUrl('/WeixinService/business/user/updateTaxResidentInfo.shtml' + param);
	}
}


function queryUserTaxInfo() {
	var taxResidentData = "";
	$.ajax({
		async: !1,
		url: "/WeixinService/business/queryUserTaxInfo.xhtml",
		data: {
		},
		dataType: "json",
		cache: !1,
		type: "POST",
		error: function () {
			errorRemark("网络繁忙，请稍后再试。");
		},
		success: function (data) {
			if (!!data) {
				taxResidentData = JSON.stringify(data);
			}
		}
	});
	return taxResidentData;
}

function show_taxResidentDecl() {
	$('#taxResidentDecl_tip').show();
	var height = 0.84 * window.innerHeight;
	var width = 0.9 * window.innerWidth;
	var leftwidth = window.innerWidth * 0.105 / 2;
	var leftheight = window.innerHeight * 0.083;
	console.info(height);
	console.info(width);
	console.info(leftwidth);
	console.info(leftheight);
	var style = { 'height': height + 'px', 'width': width + 'px', 'margin-left': leftwidth + 'px', 'margin-top': leftheight + 'px' }
	$('#taxResidentDecl_tip').children().eq(0).css(style);

	// 计算span left
	leftwidth = ($('.span').parent().width() - $('.span').width()) / 2
	leftheight = 0.03 * window.innerHeight;
	$('.span').css('margin-left', leftwidth + 'px');
	$('.span').css('top', leftheight + 'px');
	$('.span').css('height', '45px');

	// 设置问题匡主题左边距
	var contentleft = 0.035 * window.innerWidth;
	var contentWidth = 0.835 * window.innerWidth;
	var contentHeight = 0.56 * window.innerHeight;
	var contentStyle = { 'margin-left': contentleft + 'px', 'margin-right': contentleft + 'px', 'width': contentWidth + 'px'/* ,'height':contentHeight+'px' */ }
	$('#taxResidentDecl_tip').children().children().eq(1).css(contentStyle);


	// 设置按钮样式
	var buttonTop = 0.03 * window.innerHeight;
	var buttonStyle = { 'margin-top': buttonTop + 'px' }
	$('#taxResidentDecl_tip').children().children().eq(2).css(buttonStyle);
	// $('.foot').hide();
}

function loadPageElementByParam() {
	var sourceType = $("#sourceType").val();
	var continueBtn = $("#continueBtn");
	if ("user" == sourceType) {
		continueBtn.html("继续");
		$(".header .top-a h2").html("");
		document.title = "税收居民信息";
	}
}

/**
 * 查询用户中文姓名
 */
function queryUserName() {
	$.ajax({
		async: false,
		url: "/WeixinService/business/queryUserinfo.xhtml",
		data: "",
		dataType: "json",
		cache: false,
		type: "post",
		error: function (textStatus, errorThrown) {
			show_tips("网络繁忙，请稍后再试。");
		},
		success: function (data) {
			if (data.returnCode != null && data.returnCode == "0000") {
				$("#userCustName").val(data.custName);
			}
		}
	});
}