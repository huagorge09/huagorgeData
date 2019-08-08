var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";
var PROJEC_TPATH = "";

function setPath(projectPath,path,accountPath){
	PROJEC_TPATH = projectPath;
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

var invtp = $("#selOpenType").val();
var custsimpnm = $(".custsimpnm").val();

$(function(){
	WASP_WIDGET.triggerDateStyleWithYMD("birthDate");
	WASP_WIDGET.triggerDateStyleWithYMD("BirthDate2");
	
	$('.select2_width').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select2-q').select2();
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	
	$("#invprtp").change();

	$("#btnSubmit").mousedown(function() {
		doSubmit();
	});
	
	if(invtp=='0'){
	   	//加载客户二级简称
	    getSecond(custsimpnm); 
	}
	
	interactInvProInfo();
	
	initVal();
	
	initCategoryInfo();
	
	$("#claseBtn").click(function() {
		setTimeout(function(){
			$("select").change();
		}, 10);
		$("#reset").click();
	});
	
});

function initVal(){
	
		
	var investProInstTypeVal = $(".investProInstTypeVal").val();
	$("#investProInstType").val(investProInstTypeVal);
	$("#investProInstType").change();	
		
	var oneYearEndNetAssetVal = $(".oneYearEndNetAssetVal").val();
	$("#oneYearEndNetAsset").val(oneYearEndNetAssetVal);
	$("#oneYearEndNetAsset").change();
	
	var oneYearEndFinAssetVal = $(".oneYearEndFinAssetVal").val();
	$("#oneYearEndFinAsset").val(oneYearEndFinAssetVal);
	$("#oneYearEndFinAsset").change();
	
	var investExperienceVal = $(".investExperienceVal").val();
	$("#investExperience").val(investExperienceVal);
	$("#investExperience").change();
		
	var threeAnnualIncomeVal = $(".threeAnnualIncomeVal").val();
	$("#threeAnnualIncome").val(threeAnnualIncomeVal);
	$("#threeAnnualIncome").change();
		
	
	var financialAssetVal = $(".financialAssetVal").val();
	$("#financialAsset").val(financialAssetVal);
	$("#financialAsset").change();

	var indInvExperienceVal = $(".indInvExperienceVal").val();
	$("#indInvExperience").val(indInvExperienceVal);
	$("#indInvExperience").change();
		
	var relatedWorkExpVal = $(".relatedWorkExpVal").val();
	$("#relatedWorkExp").val(relatedWorkExpVal);
	$("#relatedWorkExp").change();

	var finProfessionsVal = $(".finProfessionsVal").val();
	$("#finProfessions").val(finProfessionsVal);
	$("#finProfessions").change();
	
	
	var taxTypeVal = $(".taxTypeVal").val();
	$("#taxType").val(taxTypeVal);
	$("#taxType").change();
	$(".taxTypeVal").val("");
	
	var taxTypeDeclVal = $(".taxTypeDeclVal").val();
	$("#taxTypeDecl").val(taxTypeDeclVal);
	$("#taxTypeDecl").change();
	$(".taxTypeDeclVal").val("");	
	
	var negativeNotFinaInstVal = $(".negativeNotFinaInstVal").val();
	$("#negativeNotFinaInst").val(negativeNotFinaInstVal);
	$("#negativeNotFinaInst").change();
		
	var controlPerTaxDeclVal = $(".controlPerTaxDeclVal").val();
	$("#controlPerTaxDecl").val(controlPerTaxDeclVal);
	$("#controlPerTaxDecl").change();
	$(".negativeNotFinaInstVal").val("");
	$(".controlPerTaxDeclVal").val("");
}


function doSubmit()
{
	var qParam = {};
	qParam.custnos = $("#custnos").val();
	if (invtp == '1') {
		qParam.custsimpnm = "";
		qParam.instrepcode = "";
	} else {
		qParam.custsimpnm = $("#custsimpnm").val();
		qParam.instrepcode = $("#instrepcode").val();
	}
	qParam.businessTp = $("#businesstp").val();
	qParam.companyTp = $("#companyTp").val();
	qParam.regionTp = $("#regioncode").val();
	qParam.fxqTp = $("#fxqTp").val();
	qParam.fxqDesc = $("#fxqDesc").val();
	qParam.invprtp = $("#invprtp").val();

	qParam.taxType = $("#taxType").val();
	qParam.taxTypeDecl = $("#taxTypeDecl").val();

	qParam.threeAnnualIncome = "";
	qParam.financialAsset = "";
	qParam.indInvExperience = "";
	qParam.relatedWorkExp = "";
	qParam.finProfessions = "";
	qParam.investProInstType = "";
	qParam.investProInstSecond = "";
	qParam.oneYearEndNetAsset = "";
	qParam.oneYearEndFinAsset = "";
	qParam.investExperience = "";
	qParam.negativeNotFinaInst = "";
	qParam.controlPerTaxDecl = "";
	qParam.preInvprtp = $("#preInvprtp").val();
	// 个人且专业
	if (invtp == '1' && "0" == qParam.invprtp) {
		qParam.threeAnnualIncome = $("#threeAnnualIncome").val();
		qParam.financialAsset = $("#financialAsset").val();
		qParam.indInvExperience = $("#indInvExperience").val();
		qParam.relatedWorkExp = $("#relatedWorkExp").val();
		qParam.finProfessions = $("#finProfessions").val();
	} else if (invtp == '0' && "0" == qParam.invprtp) { // 机构且专业
		qParam.investProInstType = $("#investProInstType").val();
		qParam.investProInstSecond = $("#investProInstSecond").val();
		qParam.oneYearEndNetAsset = $("#oneYearEndNetAsset").val();
		qParam.oneYearEndFinAsset = $("#oneYearEndFinAsset").val();
		qParam.investExperience = $("#investExperience").val();

	}
	
	qParam.negativeNotFinaInst = $("#negativeNotFinaInst").val();
	qParam.controlPerTaxDecl = $("#controlPerTaxDecl").val();
	qParam.permissionId = $("#permissionId").val();
	qParam.operatorId = $("#operatorId").val();
   
	var flag = checkResidentInfo();
	if(!flag){
		return false;
	}
	// 税收居民信息
	qParam.taxresident = $("input[name=taxresident]").val();
   
	$.ajax({
		url: ACCOUNT_PATH+"modifyCategoryInfo.do",  
		dataType: "json",
		type: "POST",
		data:  {
			"baseInfo" : JSON.stringify(qParam)
		},
		cache: false,
		async: false,
		success: function(data) {
			var errcode = data.errcode;
		    var errMsg =data.errmsg;
		    var serialNo = data.serialno;
		    if(errcode == "0000"){
		    	ctools.alert_sweet('申请提交成功！', "success", "申请编号："+serialNo , function(){
		    		window.close();
				});
		    }else{
		    	ctools.alert_sweet('申请提交失败！', "error", "失败原因："+errMsg);
		    }
		}
	});
}


//获取客户二级分组
function getSecond(pmco) {
	var hinstrepcode = $("#hinstrepcode").val();
	var html = "<option value=''>--</option>";
	$("#instrepcode").html(html);
	$("#instrepcode").change();
	if(pmco == ""){
		return;
	}
	$.ajax({
		type : "post",
		url : PRIMARY_PATH+"queryMatchParamList.do?pmst=DSCUSTGROUP&pmky=SECONDGROUP&pmv1="+pmco,
		dataType : "json",
		contentType : 'application/json;charset=utf-8',
		success : function(data) {
			if (!!data) {
				for (var i = 0; i < data.length; i++) {
					var item = data[i];
					if(hinstrepcode == item.PMCO){
						html+="<option value=\""+item.PMCO+"\" selected='selected'>"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}else{
						html+="<option value=\""+item.PMCO+"\">"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}
				}
			}
			$("#instrepcode").html(html);
			$("#instrepcode").val(hinstrepcode);
			$("#instrepcode").select2("destroy");
			$("#instrepcode").select2().trigger('change');
		},
		error : function() {
			ctools.alert("客户二级分组查询失败！","","error");
		}
	});
}

//获取城市集合
function getCitys(pmco, flag) {
	if (flag == '1') {
		city = $("#openbankcity");
		selectCity = "#hopenbankcity";
	} else {
		city = $("#orgopenbankcity");
		selectCity = "#horgopenbankcity";
	}
	var html = "<option value=''>--</option>";
	if(pmco == ""){
		$(city).html(html);
		$(city).change();
		return;
	}
	
	$.ajax({
		type : "post",
		url : PRIMARY_PATH+"queryMatchParamList.do?pmst=SYSTEM&pmky=DS_CITYCODE&pmv1="+pmco,
		dataType : "json",
		contentType : 'application/json;charset=utf-8',
		success : function(data) {
			if (!!data) {
				for (var i = 0; i < data.length; i++) {
					var item = data[i];
					if(item.PMCO == $(selectCity).val()){
						html+="<option value=\""+item.PMCO+"\" selected>"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}else{
						html+="<option value=\""+item.PMCO+"\">"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}
				}
				$(city).html(html);
				$(city).select2("destroy");
				$(city).select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
			}
		},
		error : function() {
			ctools.alert("城市信息查询失败！","","error");
		}
	});
}



function getInvestProInstSecond(element) {
	var hiInvestProInstSecond = $("#hiInvestProInstSecond").val();
	var pmst = "";
	var pmky = "";
	var thisValue = element.value;
	var selOpenType = $("#selOpenType").val();
	var invprtp = $("#invprtp").val();
	var html = '<option value="">--</option>';
	$("#investProInstSecond").html(html);
	$("#investProInstSecond").change();
	if (thisValue == "-" || thisValue == "") {
		return;
	};
	$("#investProInstSecond").find("option").length = 1;
	$("#investProInstSecond").val("");
	$("[class=instInvestProInfoBg]").hide();
	$("[class=instNotResidentBg]").hide();
	if ("0" == thisValue) {
		pmst = "INVPROINSTTYPE";
		pmky = "FINANCIALINST";
	} else if ("1" == thisValue) {
		pmst = "INVPROINSTTYPE";
		pmky = "FINPRODUCT";
	} else if ("2" == thisValue) {
		pmst = "INVPROINSTTYPE";
		pmky = "OTHERFUND";
	} else if ("3" == thisValue) {
		//执行其他法人及组织 
		$("[class=instInvestProInfoBg]").show();
		$("tr[class=instNotResidentBg]").show();
		resetInvestClassTh("investClassInfo", "classInfoTh");
		resetInvestClassTh("instInvestPro", "instInvestProTh");
		$("#investProInstSecond").val(hiInvestProInstSecond);
		$("#investProInstSecond").select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"}).trigger('change');
		return;
	}
	;
	// 其他法人及组织 普通投资者 开户为 机构
	if ((("1" == invprtp) || ("0" == invprtp)) && ("0" == selOpenType)) {
		$("tr[class=instNotResidentBg]").show();
	}

	var html = "<option value=''>--</option>";
	$("#investProInstSecond").html(html);
	$("#investProInstSecond").change();
	$.ajax({
		type : "post",
		url : PRIMARY_PATH+"queryMatchParamList.do?pmst=" + pmst + "&pmky=" + pmky,
		dataType : "json",
		success : function(data) {
			if (!!data) {
				for (var i = 0; i < data.length; i++) {
					var item = data[i];
					if(item.PMCO == hiInvestProInstSecond){
						html+="<option value=\""+item.PMCO+"\" selected>"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}else{
						html+="<option value=\""+item.PMCO+"\">"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}
				}
				$("#investProInstSecond").html(html);
				$("#investProInstSecond").change();
				if ("0" == thisValue) {
					$("#investProInstSecond").val("");
				} else if ("1" == thisValue) {
					$("#investProInstSecond").val("1");
				} else if ("2" == thisValue) {
					$("#investProInstSecond").val("0");
				} else if ("3" == thisValue) {
					$("#investProInstSecond").hide();
				} else {
					$("#investProInstSecond").val("");
				};
			}
			$("#investProInstSecond").select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"}).trigger('change');
			resetInvestClassTh("investClassInfo", "classInfoTh");
			resetInvestClassTh("instInvestPro", "instInvestProTh");
			$("#investProInstSecond").change();
		},
		error : function() {
			ctools.alert("查询失败！","","error");
		}
	});
}
//专业投资者信息 展示 收集
function interactInvProInfo() {
	var openAccType = $("#selOpenType").val();
	var invprtp = $("#invprtp").val();
	var instBg = $("#instInvestPro");
	var perBg = $("#personInstInvestPro");
	var tabPslExtInfo = $("#tabPslExtInfo");
	var tabOrgExtInfo = $("#tabOrgExtInfo");

	//开户类型 机构开户 且 专业投资者 
	$(instBg).hide();
	$(perBg).hide();
	$("tr[class=instNotResidentBg]").hide();
	$("tr[class=negativeNotFinaInstBg]").hide();
	
	var negativeNotFinaInstVal = $("#negativeNotFinaInstVal").val();
	$("tr[class=instNotResidentBg] select").val(negativeNotFinaInstVal);
	$("tr[class=instNotResidentBg] select").change();
	
	//个人-普通 收入 客户风险等级 展示
	$(tabPslExtInfo).find("#plsInvIncome").parent().parent().parent().parent().show();
	$(tabOrgExtInfo).find("#orgRiskLevel").parent().parent().parent().prev().show();;
	$(tabOrgExtInfo).find("#orgRiskLevel").parent().parent().parent().show();
	$("#extInfoOtherTh").attr("rowspan", "3");
	//个人开户 专业投资者
	if ("1" == openAccType && "0" == invprtp) {
		$(perBg).show();
		tabPslExtInfo.find("#plsInvIncome").parent().parent().parent().hide();
		$("#extInfoOtherTh").attr("rowspan", "2");
	}

	//机构开户 专业投资者
	if ("0" == openAccType && "0" == invprtp) {
		$(instBg).show();
		$(tabOrgExtInfo).find("#orgRiskLevel").parent().parent().prev().hide();
		$(tabOrgExtInfo).find("#orgRiskLevel").parent().parent().hide();
	}
	//机构类型为 其他法人及组织  机构开户 普通投资者
	if ("0" == openAccType && ("1" == invprtp || "0" == invprtp )) {
		$("tr[class=instNotResidentBg]").show();
		$("#negativeNotFinaInst").change();
	} else {
		$("tr[class=instNotResidentBg]").hide();
		$("tr[class=negativeNotFinaInstBg]").hide();
	}
	//触发资料变动
	resetInvestClassTh("investClassInfo", "classInfoTh");
	resetInvestClassTh("instInvestPro", "instInvestProTh");
	$("#investProInstType").change();
}

/*********************0414修改***************************************/
function negativeNotFinaChange(element) {
	var value = element.value;
	var controlPerTaxDeclVal = $("#controlPerTaxDeclVal").val();
	$("tr[class=negativeNotFinaInstBg] select").val("");
	if (value == "1" || value == "2") {
		$("[class=negativeNotFinaInstBg]").show();
		if(!!controlPerTaxDeclVal){
			$("select[name=controlPerTaxDecl]").val(controlPerTaxDeclVal);
		}else{
			$("#controlPerTaxDecl").val("0");
		}

	} else {
		$("[class=negativeNotFinaInstBg]").hide();
		$("#controllerInfo").hide();
		$("#controllerInfo").find("input,select").val("");
	}
	$("#controlPerTaxDecl").change();
	$("#controllerInfo").find("select").change();
	resetInvestClassTh("investClassInfo", "classInfoTh");
}
/*********************0414修改***************************************/

function resetInvestClassTh(id, targetTh) {
	//
	var rowspan = 0;
	var trList = $("#" + id).find("tr");
	for (var i = 0; i < trList.length; i++) {
		var tr = trList[i];
		var isHidden = $(tr).is(":hidden");
		if (!isHidden) {
			rowspan = i + 1;
		}
	}
	if (rowspan <= 0) {
		return;
	}
	$("#" + targetTh).attr("rowspan", rowspan);
	$("#" + targetTh).attr("rowspan", rowspan);
}


/*************************************20180412新增*********************************************/
/**
 * 税收居民身份类型选择
 */
function taxTypeChange(element) {
	var value = element.value;
	
	/*$("tr[class=otherTaxTypeInfoBg] select").val("");
	if (!!value && "1" != value) {
		$("[class=otherTaxTypeInfoBg]").show();
	} else {
		$("[class=otherTaxTypeInfoBg]").hide();
	}*/
	resetInvestClassTh("investClassInfo", "classInfoTh");
	
	getTaxType(value, true);
	changeVal();
	var selOpenType = $("#selOpenType").val();
	initEnglishNameTr(selOpenType);
	if (value == '3') {
		var htmlStr = '<option value="">--</option><option value="1">中国</option>';
		$(".taxNationality select[name='taxNationality']").html(htmlStr);
	} else {
		var htmlStr = '<option value="">--</option><option value="1">中国</option><option value="2">中国香港</option><option value="3">中国澳门</option><option value="4">中国台湾</option><option value="5">海外</option>';
		$(".taxNationality select[name='taxNationality']").html(htmlStr);
	}
   	$('.select_addr1').select2("destroy");
	$('.select_addr2').select2("destroy");
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	
	$("#residentType").find("select").change();
	$("#controllerInfo").find("select").change();
}

function changeVal(){
	var selOpenType = $("#selOpenType").val();
	var reg = /^[\u4E00-\u9FA5]+$/;
	if (selOpenType == '1') {
		var pslInvNm = $("#invNm").val();
		if (reg.test(pslInvNm)) {
			$("#userCustName").val(pslInvNm);
		}
	} else if (selOpenType == '2' || selOpenType == '3') {
		var orgInvNm = $("#invNm").val();
		if (reg.test(orgInvNm)) {
			$("#userCustName").val(orgInvNm);
		}
	}
	$("#userCustName").val($("#invnm").val());
}

function initEnglishNameTr(selOpenType) {
	var html = "";
	if (selOpenType == '1') {
		html += '<td><font color="red">*</font>First Name：</td>';
		html += '<td class="white-bg"><div class="col-sm-11 form-inner"><input type="text" name="firstName" id="firstName" class="form-control"></div></td>';
		html += '<td><font color="red">*</font>Last Name：</td>';
		html += '<td class="white-bg" colspan="2">';
		html += '<div class="col-sm-11 form-inner"><input type="text" name="englishName" id="englishName" class="form-control"></div></td>';

		$(".sexAndbirth").show();
		$(".birthAddress").show();
		$(".resideNation").html("现居国家");
		$(".resideAddress").html("现居地址");

	} else {
		html += '<td><font color="red">*</font>English Name：</td>';
		html += '<td class="white-bg" colspan="4">';
		html += '<div class="col-sm-11 form-inner"><input type="text" name="englishName" id="englishName" class="form-control"></div></td>';

		$(".sexAndbirth").hide();
		$(".birthAddress").hide();
		$(".resideNation").html("机构国家");
		$(".resideAddress").html("机构地址");
		
	}
	$(".englishNameDiv").html(html);
}

/**
 * 类型切换
 * @param {Object} value
 */
function getTaxType(value, isClear) {
	var controlPerTaxDecl = $("#controlPerTaxDecl").val();
	if (value == "2") {
		$("#residentType").show();
		$("#residentTitle").text("税收居民信息");
		if (isClear) {
			$("#residentType input,#residentType select").val("");
			cancelTaxpayerCode();
		}
		$(".addResidentEle").remove();
		$(".since").hide();
		getSexAndBirth();
	} else if (value == "3") {
		$("#residentType").show();
		$("#residentTitle").text("税收居民信息");
		if (isClear) {
			$("#residentType input,#residentType select").val("");
			cancelTaxpayerCode();
		}
		$(".since").show();
		$(".sinceNotCodeCause").hide();
		getSexAndBirth();
	}else if(controlPerTaxDecl == '1'){
		$("#residentType").show();
		$("#residentTitle").text("税收居民信息");
		if (isClear) {
			$("#residentType input,#residentType select").val("");
			cancelTaxpayerCode();
		}
		
		$(".addResidentEle").remove();
		$(".since").hide();
		getSexAndBirth();
	} else {
		$("#residentType").hide();
	}
	$(".causeText").hide();
}


/**
 * 获取性别和出生年月
 */
function getSexAndBirth() {
	var pslInvIdtp = $("#pslInvIdtp").val();
	var pslInvIdno = $("#pslInvIdno").val();
	$("input[name=birthDate],select[name=sex]").val("");
	if (pslInvIdtp == '0' && !!pslInvIdno) {
		var Birth = getIdCard.getIdCardInfo(pslInvIdno).birthday;
		var sex = getIdCard.getIdCardInfo(pslInvIdno).gender;
		$("input[name=birthDate]").val(Birth);
		$("select[name=sex]").val(sex);
	}
	$("select[name=sex]").change();
	WASP_WIDGET.triggerDateStyleWithYMD("birthDate");
}

/**
 * 是否 无纳税人识别号
 */
function isTaxpayer(e) {
	var checked = e.checked;
	var target = $(e);
	if (checked) {
		target.parent().prev().attr("readonly", "readonly");
		target.parent().prev().attr("disabled", "disabled");
		target.parent().prev().val("");
		target.parents("tr").next().show();
		target.parents("tr").next().find("select").val("");
		target.parents("tr").next().find("select").change();
	} else {
		target.parent().prev().removeAttr("readonly");
		target.parent().prev().removeAttr("disabled");
		target.parents("tr").next().hide();
		target.parents("tr").next().find("select").val("");
		target.parents("tr").next().find("select").change();
		target.parents("tr").next().next().hide();
		target.parents("tr").next().next().find("input").val("");
	}
}
/**
 * 无识别号事件
 * @param {Object} element
 */
function notCodeCauseEvent(element) {
	var value = element.value;
	if (value == "2") {
		$(element).parents("tr").next("tr").show();
	} else {
		$(element).parents("tr").next("tr").hide();
		$(element).parents("tr").next("tr").find("input").val("");
	}
}

/**
 * 取消纳税人识别号check选中
 */
function cancelTaxpayerCode() {
	var checkObj = $("#residentType input[type=checkbox]");
	checkObj.attr("checked", false);
	checkObj.parent().prev().removeAttr("readonly");
	checkObj.parent().prev().removeAttr("disabled");
	checkObj.parents("tr").next().val("").hide();
	checkObj.parents("tr").next().val("").hide();
}
/**
 * 添加税收元素
 */
var addT=0;
function addResident(obj) {
	var html = '<tr class="since addResidentEle add'+addT+'">'
			+ '<td><font color="red">*</font>税收居民国(地区)</td>'
			+ '<td class="white-bg" colspan="4"><div class="col-sm-11 form-inner">'
			+ '<select class="form-control select_addr1" name="taxNationality" onchange="changeNation(this)">'
			+ '<option value="">--</option>'
			+ '<option value="1">中国</option>'
			+ '<option value="2">中国香港</option>'
			+ '<option value="3">中国澳门</option>'
			+ '<option value="4">中国台湾</option>'
			+ '<option value="5">海外</option>'
			+ '</select>&nbsp;'
			+ '<select class="form-control select_addr2" name="taxArea">'
			+ '<option value="">--</option>'
			+ '</select>'
			+ '<span class="addResident" onclick="minResident(this);">-</span></div></td></tr>'
			+ '<tr class="since addResidentEle add'+addT+'">'
			+ '<td><font color="red">*</font>纳税人识别号：</td>'
			+ '<td class="white-bg" colspan="4"><div class="col-sm-11 form-inner">'
			+ '<input type="text" name="taxpayerCode" class="form-control" style="float: left;">'
			+ '<label style="line-height: 34px;">'
			+ '<input name="isTaxpayerEvent" class="i-checks isTaxpayerEvent" type="checkbox" onclick="isTaxpayer(this)">无<i class="residentEach"></i>'
			+ '</label></div></td></tr>'
			+ '<tr class="since sinceNotCodeCause addResidentEle add'+addT+'" style="display:none">'
			+ '<td><font color="red">*</font>无识别号的原因：</td>'
			+ '<td class="white-bg" colspan="4"><div class="col-sm-11 form-inner">'
			+ '<select class="form-control select2 notCodeCause" name="notCodeCause" onchange="notCodeCauseEvent(this)">'
			+ '<option value="">--</option>'
			+ '<option value="1">居民国（地区）不发放纳税人识别号</option>'
			+ '<option value="2">账号持有人未能取得纳税人识别号</option>'
			+ '</select>'
			+ '</div></td></tr>'
			+ '<tr class="since addResidentEle add'+addT+'" style="display:none">'
			+ '<td><font color="red">*</font>未取得原因：</td>'
			+ '<td class="white-bg" colspan="4"><div class="col-sm-11 form-inner">'
			+ '<input type="text" class="form-control" name="notGetCause"></div></td></tr>';
	$("#residentType").append(html);
	
	$('.notCodeCause').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	addT++;
}
/**
 * 删除税收元素
 * @param {Object} obj
 */
function minResident(obj) {
	$(obj).parents("tr").next().remove();
	$(obj).parents("tr").next().remove();
	$(obj).parents("tr").next().remove();
	$(obj).parents("tr").remove();
}
/**
 * 提交信息组装
 */
function checkResidentInfo() {
	var info = {};
	var re = /^[\u2E80-\u9FFF]+$/;
	var taxType = $("#taxType").val();
	var selOpenType = $("#selOpenType").val();
	/* if (taxType != "1" && !!taxType) { */
	if ($("#residentType").is(":visible")) {
		var userCustName = $("input[name=userCustName]").val(); //中文姓名
		var firstName = $("input[name=firstName]").val(); //firstName
		var englishName = $("input[name=englishName]").val(); //englishName
		if (selOpenType == "1") {
			var sex = $("select[name=sex]").val(); //性别
			var birthDate = $("input[name=birthDate]").val(); //出生日期
		} else {
			var sex = ""; //性别
			var birthDate = ""; //出生日期
		}
		var taxBirthNation = $("select[name=birth_nation]").val(); //出生地  国籍
		var taxBirthRegion = $("select[name=birth_region]").val(); //出生地  省份
		var taxBirthAddress = $("input[name=birth_address]").val(); //出生地  详细地址
		var taxResideNation = $("select[name=reside_nation]").val(); //现居国家 国籍
		var taxResideRegion = $("select[name=reside_region]").val(); //现居国家 省份
		var taxResideAddress = $("input[name=reside_address]").val(); //现居国家 详细地址
		var taxResideAddressEnglish = $(
				"input[name=reside_address_english]").val(); //Present Address：
		info.userCustName = userCustName;
		info.firstName = firstName;
		info.englishName = englishName;
		info.taxBirthNation = taxBirthNation;
		info.taxBirthRegion = taxBirthRegion;
		info.taxBirthAddress = taxBirthAddress;
		info.taxResideNation = taxResideNation;
		info.taxResideRegion = taxResideRegion;
		info.taxResideAddress = taxResideAddress;
		info.taxResideAddressEnglish = taxResideAddressEnglish;
		if (selOpenType == "1") {
			if (firstName == "") {
				ctools.alert("请填写First Name!","","error");
				return false;
			}
			if (re.test(firstName)) {
				ctools.alert("First Name只能填写英文或拼音！","","error");
				return false;
			}
			if (englishName == "") {
				ctools.alert("请填写Last Name！","","error");
				return false;
			}
			if (re.test(englishName)) {
				ctools.alert("Last Name只能填写英文或拼音！","","error");
				return false;
			}
			if (sex == "") {
				ctools.alert("请选择性别！","","error");
				return false;
			}
			if (birthDate == "") {
				ctools.alert("请选择出生日期！","","error");
				return false;
			}
			if (taxBirthNation == "" || taxBirthRegion == "") {
				ctools.alert("请选择出生地！","","error");
				return false;
			}
		} else {
			if (englishName == "") {
				ctools.alert("请填写English Name！","","error");
				return false;
			}
			if (re.test(englishName)) {
				ctools.alert("English Name只能填写英文或拼音！","","error");
				return false;
			}
		}

		if (taxResideNation == "" || taxResideRegion == "") {
			ctools.alert("请选择"+$(".resideNation").html(),"","error");
			return false;
		}
		if (taxResideAddress == "") {
			ctools.alert("请填写"+$(".resideAddress").html(),"","error");
			return false;
		}
		if (taxResideAddressEnglish == "") {
			ctools.alert("请填写Present Address！","","error");
			return false;
		}
		if (re.test(taxResideAddressEnglish) || taxResideAddressEnglish == "") {
			ctools.alert("Present Address请填写英文或拼音地址！","","error");
			return false;
		}
		if (!checkTaxResidentData()) {
			ctools.alert("请填写税收信息！","","error");
			return false;
		} else {
			if ($("#controllerInfo").is(":visible")) {
				var flag = checkControllerInfo(); //校验控制人相关信息
				if (flag) {
					var residentData = JSON.stringify(getTaxResidentData(info));
					$("input[name=taxresident]").val(residentData.replace(/\[]/g, "''"));
				} else {
					return flag;
				}

			} else {
				var residentData = JSON.stringify(getTaxResidentData(info));
				$("input[name=taxresident]").val(residentData.replace(/\[]/g, "''"));
			}

		}
	}
	return true;
}

//校验税收信息
function checkTaxResidentData() {
	var flag = true;
	//根据不同的税收居民身份
	$("select[name=taxNationality]").each(function(i){
		var taxNationality = $("select[name=taxNationality]").eq(i).val();
		var taxArea = $("select[name=taxArea]").eq(i).val();
		if ($("select[name=taxNationality]").eq(i).is(":visible") && $("select[name=taxArea]").eq(i).is(":visible")) {
			if (taxNationality == "" || taxArea == "") {
				flag = false;
				return flag;
			} else {
				$(".isTaxpayerEvent").each(function(i) {
					if ($(".isTaxpayerEvent").eq(i).is(":visible")) {
						if ($(this).is(':checked')) {
							var payerCode = $(this).parents("tr").next().find("select");
							var TaxpayerCode = $(this).parents("tr").next().find("select").val();
							if (TaxpayerCode != "") {
								if (payerCode.val() == "2") {
									if (payerCode.parents("tr").next().find("input").val() == "") {
										flag = false;
									}
								}
							} else {
								flag = false;
							}
						} else {
							if ($(this).parent().parent().parent().find("input[name=taxpayerCode]").val() == "") {
								flag = false;
							}
						}
					}
				});
			}
		}
	});
	return flag;
}

/**
 * 获取 税收信息数据 
 * 多税收信息 按层级获取后封装为json字符串
 * @returns 封装后的税收居民数据
 */
function getTaxResidentData(taxInfo) {
	var residentArr = new Array();
	if ($("#residentType").is(":visible")) {
		$(".residentEach").each(function(i) {
			if ($(".isTaxpayerEvent").eq(i).is(":visible")) {
				var taxInfoResident = {
					firstName : taxInfo.firstName,
					englishName : taxInfo.englishName,
					taxBirthNation : taxInfo.taxBirthNation,
					taxBirthRegion : taxInfo.taxBirthRegion,
					taxBirthAddress : taxInfo.taxBirthAddress,
					taxResideNation : taxInfo.taxResideNation,
					taxResideRegion : taxInfo.taxResideRegion,
					taxResideAddress : taxInfo.taxResideAddress,
					taxResideAddressEnglish : taxInfo.taxResideAddressEnglish
				};
				var selOpenType = $("#selOpenType").val();
				if (selOpenType == "1") {
					taxInfoResident.sex = $("select[name=sex]").val();
					taxInfoResident.birthDate = $("input[name=birthDate]").val();
				} else {
					taxInfoResident.sex = "";
					taxInfoResident.birthDate = "";
				}
				taxInfoResident.sortNo = i;
				taxInfoResident.taxNationality = $(this).parents("tr").prev().find("select[name=taxNationality]").val();
				taxInfoResident.taxArea = $(this).parents("tr").prev().find("select[name=taxArea]").val();
				taxInfoResident.taxPayerCode = $(this).parent().siblings("input").val();
				var isTaxpayerEvent = $(this).siblings("input[type='checkbox']").is(':checked');
				if (isTaxpayerEvent) {
					taxInfoResident.taxNotCodeCause = $(this).parents("tr").next().find("select[name=notCodeCause]").val();
					taxInfoResident.taxnotGetCause = $(this).parents("tr").next().next().find("input[name=notGetCause]").val();
				} else {
					taxInfoResident.taxNotCodeCause = "";
					taxInfoResident.taxnotGetCause = "";
				}

				//控制人信息
				if ($("#selOpenType").val() != '1'&& !!$("#selOpenType").val()) {
					taxInfoResident.chineseName2 = $("input[name=ChineseName2]").val(); //控制人中文姓名：
					taxInfoResident.englishFamliyName3 = $("input[name=EnglishFamliyName3]").val(); //控制人英文姓
					taxInfoResident.englishFirstName3 = $("input[name=EnglishFirstName3]").val(); //控制人英文名
					taxInfoResident.controllerType = $("select[name=ControllerType]").val(); //控制人类型
					taxInfoResident.conNonResiFlag = $("input[name=ConNonResiFlag]").val(); //控制人非居民标识
					taxInfoResident.conShareRatio = $("input[name=ConShareRatio]").val(); //控制人持股比例
					taxInfoResident.livingCountry2Code = $("select[name=LivingCountry2]").val(); //控制人现居国家
					taxInfoResident.livingCountry2 = $("select[name=LivingCountry21]").val(); //控制人现居国家
					taxInfoResident.livingAddress5 = $("input[name=LivingAddress5]").val(); //控制人现居地址
					taxInfoResident.livingAddress7 = $("input[name=LivingAddress7]").val(); //控制人现居地址英文
					taxInfoResident.regRegionCode2Code = $("select[name=RegRegionCode2]").val(); //控制人国籍
					taxInfoResident.regRegionCode2 = $("select[name=RegRegionCode21]").val();//控制人国籍
					taxInfoResident.birthDate2 = $("input[name=BirthDate2]").val(); //控制人出生日期
					taxInfoResident.birthCountry2Code = $("select[name=BirthCountry2]").val(); //控制人出生国家
					taxInfoResident.birthCountry2 = $("select[name=BirthCountry21]").val(); //控制人出生国家
					taxInfoResident.birthCity2 = $("input[name=BirthCity2]").val(); //控制人出生城市英文
					taxInfoResident.taxCountry2Code = $("select[name=TaxCountry2]").val(); //控制人税收居民国
					taxInfoResident.taxCountry2 = $("select[name=TaxCountry21]").val(); //控制人税收居民国
					taxInfoResident.taxID2 = $("input[name=TaxID2]").val(); //纳税人识别号
					var isTaxpayerEvent2 = $("input[name='isTaxpayerEvent2']").is(':checked');
					if (isTaxpayerEvent2) {
						taxInfoResident.specificationCode = $("select[name=notCodeCause2]").val(); //无识别号的原因
						taxInfoResident.specification2 = $("input[name=Specification2]").val(); //未取得原因
					} else {
						taxInfoResident.specificationCode = "";
						taxInfoResident.specification2 = "";
					}
				}
				residentArr.push(taxInfoResident);
			}
		});
	}
	return residentArr;
}

/**
 * 出生地、居住地、税收居民国籍触发事件
 */
function changeNation(ele) {
	var val = $(ele).val();
	var secondEle = $(ele).siblings("select");
	var defaultOption = '<option value="" selected="selected">请选择</option>';
	switch (val) {
	case "1":
		var options = "<option value='156-1'>中国</option>";
		secondEle.html(defaultOption + options);
		break;
	case "2":
		var options = "<option value='156-2'>香港</option>";
		secondEle.html(defaultOption + options);
		break;
	case "3":
		var options = "<option value='156-3'>澳门</option>";
		secondEle.html(defaultOption + options);
		break;
	case "4":
		var options = "<option value='156-4'>台湾</option>";
		secondEle.html(defaultOption + options);
		break;
	case "5":
		var options = $("#comNation").html();
		secondEle.html(defaultOption + options);
		break;
	default:
		secondEle.html(defaultOption);
	}
	secondEle.val("");
	$(secondEle).select2("destroy");
	$(secondEle).select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
}

/*************************************20180412新增*********************************************/

/*************************************20180413新增s*********************************************/
/*
 * 存在非居民控制人
 */
function controllerPerTaxDecl(ele) {
	if (ele.value == "1") {
		$("#residentType").show();
		$("#controllerInfo").show();
	} else {
		$("#residentType").hide();
		$("#residentType").find("input,select").val("");
		$("#controllerInfo").hide();
		$("#controllerInfo").find("input,select").val("");
	}
	$(".controTr").hide();
	$(".controTr").eq(0).prev().find("input[name=TaxID2]").removeAttr(
			"disabled");
	$(".controTr").eq(0).prev().find("input[name=isTaxpayerEvent2]").attr(
			"checked", false);
	$("input[name=ConNonResiFlag]").val("1");
	$("#taxType").change();
}

/**
 * 提交信息校验
 */
function checkControllerInfo() {
	var info = {};
	var re = /^[\u2E80-\u9FFF]+$/;
	var num = /0\.[0-9]+/;
	var flag = true;
	var chineseName2 = $("input[name=ChineseName2]").val(); //中文姓名2
	var englishFamliyName3 = $("input[name=EnglishFamliyName3]").val(); //英文姓3
	var englishFirstName3 = $("input[name=EnglishFirstName3]").val(); //英文名
	var controllerType = $("select[name=ControllerType]").val(); //控制人类型
	var conNonResiFlag = $("input[name=ConNonResiFlag]").val(); //控制人非居民标识
	var conShareRatio = $("input[name=ConShareRatio]").val(); //控制人持股比例
	var livingCountry2 = $("select[name=LivingCountry2]").val(); //现居国家2
	var livingCountry21 = $("select[name=LivingCountry21]").val(); //现居国家2
	var livingAddress5 = $("input[name=LivingAddress5]").val(); //现居地址5
	var livingAddress7 = $("input[name=LivingAddress7]").val(); //现居地址英文7
	var regRegionCode2 = $("select[name=RegRegionCode2]").val(); //国籍
	var regRegionCode21 = $("select[name=RegRegionCode21]").val(); //国籍
	var birthDate2 = $("input[name=BirthDate2]").val(); //出生日期2
	var birthCountry2 = $("select[name=BirthCountry2]").val(); //出生国家2
	var birthCountry21 = $("select[name=BirthCountry21]").val(); //出生国家2
	var birthCity2 = $("input[name=BirthCity2]").val(); //出生城市2
	var isType = $("input[name=taxType]").val();
	if (chineseName2 == "") {
		ctools.alert("请填写控制人中文姓名！","","error");
		flag = false;

	} else if (englishFamliyName3 == "") {
		ctools.alert("请填写控制人英文姓！","","error");
		flag = false;
	} else if (re.test(englishFamliyName3)) {
		ctools.alert("英文姓只能填写英文或拼音！","","error");
		flag = false;
	} else if (englishFirstName3 == "") {
		ctools.alert("请填写控制人英文名！","","error");
		flag = false;
	} else if (re.test(englishFirstName3)) {
		ctools.alert("英文名只能填写英文或拼音！","","error");
		flag = false;
	} else if (controllerType == "") {
		ctools.alert("请选择控制人类型！","","error");
		flag = false;
	} else if (conNonResiFlag == "") {
		ctools.alert("请填写控制人非居民标识！","","error");
		flag = false;
	}/*  else if (conShareRatio == "") {
		ctools.alert("请填写控制人持股比例！","","error");
		flag = false;
	} else if (!num.test(parseFloat(conShareRatio))) {
		ctools.alert("控制人持股比例不正确！","","error");
		flag = false;
	}*/ 
	else if (livingCountry2 == "" || livingCountry21 == "") {
		ctools.alert("请选择控制人现居国家！","","error");
		flag = false;
	} else if (livingAddress5 == "") {
		ctools.alert("请填写控制人现居地址！","","error");
		flag = false;
	} else if (livingAddress7 == "") {
		ctools.alert("请填写控制人现居地址英文！","","error");
		flag = false;
	} else if (re.test(livingAddress7)) {
		ctools.alert("控制人现居地址英文只能填写英文或拼音！","","error");
		flag = false;
	}/*  else if (regRegionCode2 == "" || regRegionCode21 == "") {
		flag = false;
		alert("请填写控制人国籍");
	}  */else if (birthDate2 == "") {
		ctools.alert("请选择控制人出生日期！","","error");
		flag = false;
	} else if (birthCountry2 == "" || birthCountry21 == "") {
		ctools.alert("请选择控制人出生国家！","","error");
		flag = false;
	} else if (birthCity2 == "") {
		ctools.alert("请填写控制人出生城市！","","error");
		flag = false;
	} else if (re.test(birthCity2)) {
		ctools.alert("控制人出生城市英文只能填写英文或拼音！","","error");
		flag = false;
	} else if (!checkControllerData()) {
		ctools.alert("请完善控制人税收信息！","","error");
		flag = false;
	}
	return flag;
}
/**
 * 校验控制人税收
 */
function checkControllerData() {
	var flag = true;
	var taxCountry2 = $("select[name=TaxCountry2]").val();
	var taxCountry21 = $("select[name=TaxCountry21]").val();
	if (taxCountry2 == "" || taxCountry21 == "") {
		flag = false;
	}
	if ($("input[name=isTaxpayerEvent2]").is(':checked')) {
		var notCodeCause2 = $("select[name=notCodeCause2]").val();
		if (notCodeCause2 != "") {
			if (notCodeCause2 == "2") {
				if ($("input[name=Specification2]").val() == "") {
					flag = false;
				}
			}
		} else {
			flag = false;
		}
	} else {
		if ($("input[name=TaxID2]").val() == "") {
			flag = false;
		}
	}
	return flag;
}

/*
 * 获取身份证的信息
 */
var getIdCard = {
	genders : {
		male : "1",
		female : "0"
	},
	getIdCardInfo : function(idCardNo) {
		var idCardInfo = {
			gender : "", //性别
			birthday : "" // 出生日期
		};
		if (idCardNo.length == 15) {
			var aday = '19' + idCardNo.substring(6, 12);
			idCardInfo.birthday = getIdCard.formateDateCN(aday);
			if (parseInt(idCardNo.charAt(14)) % 2 == 0) {
				idCardInfo.gender = getIdCard.genders.female;
			} else {
				idCardInfo.gender = getIdCard.genders.male;
			}
		} else if (idCardNo.length == 18) {
			var aday = idCardNo.substring(6, 14);
			idCardInfo.birthday = getIdCard.formateDateCN(aday);
			if (parseInt(idCardNo.charAt(16)) % 2 == 0) {
				idCardInfo.gender = getIdCard.genders.female;
			} else {
				idCardInfo.gender = getIdCard.genders.male;
			}

		}
		return idCardInfo;
	},
	formateDateCN : function(day) {
		var yyyy = day.substring(0, 4);
		var mm = day.substring(4, 6);
		var dd = day.substring(6);
		return yyyy + "-" + mm + "-" + dd;
	}
};

$("#btnReset").click(function() {
	$("#taxType").change();
});


function initCategoryInfo(){
	$("#residentType").hide();
	$("#controllerInfo").hide();
	var selOpenType = $("#invtp").val();
	initEnglishNameTr(selOpenType);
	getSexAndBirth();
	var arrDate= $("#accountInfo").val();
	var obj=eval(arrDate);
	/**
	 * 分类信息显示逻辑的几种情况
	 * 1.中国税收居民，不显示非居民信息、不显示既是中国又是其他类型税收居民信息、不显示非居民控制人信息(当数组数据为空情况)
	 * 2.仅为居民信息，显示非居民信息信息，不显示既是中国又是其他类型税收居民，显示控制人信息(数据数据只有一条的情况，且控制人的信息字段不为空)
	 * 3.仅为居民信息，显示非居民信息信息，不显示既是中国又是其他类型税收居民信息，不显示控制人信息(数据数据只有一条的情况，且控制人的任意信息字段为空)
	 * 4.既是中国又是其他类型，不显示非居民信息信息，显示既是中国又是其他类型税收居民信息，显示控制人信息(数据信息两条以上，且控制人的信息字段不为空)
	 * 5.既是中国又是其他类型，不显示非居民信息信息，显示既是中国又是其他类型税收居民信息，不显示控制人信息(数据信息两条以上，且控制人的信息字段全部为空)
	 */
	if(arrDate!="[]"){
		residentCom();
		if(obj.length==1){   //仅为居民信息
		     residentOnlyOne();
		     $(".taxNationality select[name=taxNationality]").val(trim(obj[0].taxNationality));
	         $(".taxNationality select[name=taxNationality]").change();
	         $(".taxNationality select[name=taxArea]").val(trim(obj[0].taxArea));
	         $(".taxNationality select[name=taxArea]").change();
		}else if(obj.length==2){ //既是中国又是其他类型
			 residentOnlyTwo();
			 $(".since select[name=taxNationality]").val(trim(obj[1].taxNationality));
	         $(".since select[name=taxNationality]").change();
	         $(".since select[name=taxArea]").val(trim(obj[1].taxArea));
	         $(".since select[name=taxArea]").change();
	         
		}else if(obj.length>2){
		 	 residentOnlyTwo();
		 	 $(".since select[name=taxNationality]").val(trim(obj[1].taxNationality));
	         $(".since select[name=taxNationality]").change();
	         $(".since select[name=taxArea]").val(trim(obj[1].taxArea));
	         $(".since select[name=taxArea]").change();
		 	 residentThree();
		}
		if(obj[0].chineseName2){  //如果有控制人
	     	controlVal();
	    }
	}else{
		$("select[name=taxType]").find("option[value=1]").attr("selected",true);
	}
	
	/*
	 * 居民基础信息
	 */
	function residentCom(){
		$("#residentType").show();
		$("input[name=firstName]").val(obj[0].firstName); //firstName
		$("input[name=englishName]").val(obj[0].englishName); //englishName
		$("select[name=sex]").find("option[value="+obj[0].sex+"]").attr("selected",true);
		$("input[name=birthDate]").val(obj[0].birthDate);
		$("select[name=birth_nation]").find("option[value="+obj[0].taxBirthNation+"]").attr("selected",true);
		$("select[name=birth_nation]").change();
		$("select[name=birth_region]").find("option[value="+obj[0].taxBirthRegion+"]").attr("selected",true);
		$("input[name=birth_address]").val(obj[0].taxBirthAddress);
		$("select[name=reside_nation]").find("option[value="+obj[0].taxResideNation+"]").attr("selected",true);
		$("select[name=reside_nation]").change();
		$("select[name=reside_region]").find("option[value="+obj[0].taxResideRegion+"]").attr("selected",true);
		$("input[name=reside_address]").val(obj[0].taxResideAddress);
		$("input[name=reside_address_english]").val(obj[0].taxResideAddressEnglish);
		$("select[name=taxNationality]").find("option[value="+obj[0].taxNationality+"]").attr("selected",true);
		$("select[name=taxNationality]").change();
		$("select[name=taxArea]").find("option[value="+obj[0].taxArea+"]").attr("selected",true);
	}
	
	/**
	 * 控制人信息赋值
	 */
	function controlVal(){
		$(".negativeNotFinaInstBg,#controllerInfo").show();
		
	    $("input[name=ChineseName2]").val(obj[0].chineseName2);
	    $("input[name=EnglishFamliyName3]").val(obj[0].englishFamliyName3);
	    $("input[name=EnglishFirstName3]").val(obj[0].englishFirstName3);
	    $("select[name=ControllerType]").find("option[value="+obj[0].controllerType+"]").attr("selected",true);
	    $("input[name=ConNonResiFlag]").val("1");
	    $("input[name=ConShareRatio]").val(obj[0].conShareRatio);
	    $("select[name=LivingCountry2]").find("option[value="+obj[0].livingCountry2Code+"]").attr("selected",true);
	    $("select[name=LivingCountry2]").change();
		$("select[name=LivingCountry21]").find("option[value="+obj[0].livingCountry2+"]").attr("selected",true);
	    $("input[name=LivingAddress5]").val(obj[0].livingAddress5);
	    $("input[name=LivingAddress7]").val(obj[0].livingAddress7);
	    $("select[name=RegRegionCode2]").find("option[value="+obj[0].regRegionCode2Code+"]").attr("selected",true);
	    $("select[name=RegRegionCode2]").change();
		$("select[name=RegRegionCode21]").find("option[value="+obj[0].regRegionCode2+"]").attr("selected",true);
		$("input[name=BirthDate2]").val(obj[0].birthDate2);
		$("select[name=BirthCountry2]").find("option[value="+obj[0].birthCountry2Code+"]").attr("selected",true);
		$("select[name=BirthCountry2]").change();
		$("select[name=BirthCountry21]").find("option[value="+obj[0].birthCountry2+"]").attr("selected",true);
		$("input[name=BirthCity2]").val(obj[0].birthCity2);
		$("select[name=TaxCountry2]").find("option[value="+obj[0].taxCountry2Code+"]").attr("selected",true);
		$("select[name=TaxCountry2]").change();
		$("select[name=TaxCountry21]").find("option[value="+obj[0].taxCountry2+"]").attr("selected",true);
		if(obj[0].taxID2=="" || obj[0].taxID2 == undefined){  //如果纳税人识别号 为空
		   	  var isTaxpayerEventNext=$("input[name=isTaxpayerEvent2]").parents("tr").next();
		   	  $("input[name=TaxID2]").eq(0).attr("disabled","disabled");
		   	  $("input[name=isTaxpayerEvent2]").eq(0).attr("checked",true);
		   	  isTaxpayerEventNext.show();
		   	  var taxNotCodeCause=obj[0].specificationCode;
		   	  isTaxpayerEventNext.find("select").find("option[value="+taxNotCodeCause+"]").attr("selected",true);
		   	  isTaxpayerEventNext.find("select").change();
		   	  if(taxNotCodeCause.trim()=="2"){
		   	  	 isTaxpayerEventNext.next().show();
		   	  	 isTaxpayerEventNext.next().find("input").val(obj[0].specification2);
		   	  }
		   }else{
		   	  $("input[name=TaxID2]").val(obj[0].taxID2);
		}
	}
	
	/**
	 * 仅非居民信息只有一条信息
	 */
	function residentOnlyOne(){
		   comResident();
	}
	
	/**
	 * 当居民信息只有两条
	 */
	function residentOnlyTwo(){
		 $(".since").eq(0).show();
		 $(".since").eq(1).show();
		  comResident();
		  if(obj[1].taxPayerCode=="" || obj[1].taxPayerCode==undefined){
		     $(".since").eq(1).find("input[name=taxpayerCode]").attr("disabled","disabled");
		     $(".since").eq(1).find("input[name=isTaxpayerEvent]").removeAttr("checked");
		  	 $(".since").eq(1).find("input[name=isTaxpayerEvent]").prop("checked","checked");
		  	 $(".sinceNotCodeCause").eq(0).show();
		  	 $(".sinceNotCodeCause").eq(0).find("select").find("option[value="+obj[1].taxNotCodeCause+"]").attr("selected",true);
		  	 $(".sinceNotCodeCause").eq(0).find("select").change();
		  	 if($(".sinceNotCodeCause").eq(0).find("select").val()=="2"){
		  	 	 $(".sinceNotCodeCause").eq(1).show();
		  	 	 $(".sinceNotCodeCause").eq(1).find("input").val(obj[1].taxnotGetCause);
		  	 }
		  }else if(obj[1].taxPayerCode!=""){
		  	  $(".since").eq(1).find("input").val(obj[1].taxPayerCode);
		  } 
	}
	/**
	 * 公共税收信息
	 */
	function comResident(){
		 $(".taxNationality select[name=taxNationality]").val(trim(obj[0].taxNationality));
		 $(".taxNationality select[name=taxNationality]").change();
		 $(".taxNationality select[name=taxArea]").val(trim(obj[0].taxArea));
		 $(".taxNationality select[name=taxArea]").change();
		 if(obj[0].taxPayerCode=="" || obj[0].taxPayerCode==undefined){  //如果纳税人识别号 为空
		   	  var isTaxpayerEventNext=$(".isTaxpayerEvent").eq(0).parents("tr").next();
		   	  $("input[name=taxpayerCode]").eq(0).attr("disabled","disabled");
		   	  $(".isTaxpayerEvent").eq(0).attr("checked",true);
		   	  isTaxpayerEventNext.show();
		   	  var taxNotCodeCause=obj[0].taxNotCodeCause;
		   	  isTaxpayerEventNext.find("select").find("option[value="+taxNotCodeCause+"]").attr("selected",true);
		   	  isTaxpayerEventNext.find("select").change();
		   	  if(taxNotCodeCause.trim()=="2"){
		   	  	 isTaxpayerEventNext.next().show();
		   	  	 isTaxpayerEventNext.next().find("input").val(obj[0].taxnotGetCause);
		   	  }
		  }
		  else{
		   	  $("input[name=taxpayerCode]").eq(0).val(obj[0].taxPayerCode);
		  }
	}
	/**
	 * 当数据大于2条时候
	 */
	function  residentThree(){
		var newData=obj.slice(2,arrDate.length);
		for(var i=0;i<newData.length;i++){
			if (newData[i].taxNationality != undefined && newData[i].taxArea != undefined) {
				addResident();
				$(".add"+i).find("select[name=taxNationality]").find("option[value="+newData[i].taxNationality+"]").attr("selected",true);
				$(".add"+i).find("select[name=taxNationality]").change();
				$(".add"+i).find("select[name=taxArea]").find("option[value="+newData[i].taxArea+"]").attr("selected",true);
				if(newData[i].taxPayerCode=="" || newData[i].taxPayerCode==undefined){
					$(".add"+i).eq(1).find("input[name=taxpayerCode]").attr("disabled","disabled");
					$(".add"+i).eq(1).find(".isTaxpayerEvent").attr("checked",true);
					$(".add"+i).eq(2).show();
					$(".add"+i).eq(2).find("select").find("option[value="+newData[i].taxNotCodeCause+"]").attr("selected",true);
					if($(".add"+i).eq(2).find("select").val().trim()=="2"){
			  	 	 	$(".add"+i).eq(3).show();
			  	 	 	$(".add"+i).eq(3).find("input").val(newData[i].taxnotGetCause);
					}
				}else if(newData[i].taxPayerCode!=""){
			  	  $(".add"+i).eq(1).find("input").val(newData[i].taxPayerCode);
				} 
		  	}
	  	}
	}
	
	$('.select2_width').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.notCodeCause').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
}

function customReset() {
	$("#taxType").change();
	$("#invprtp").change();
	$("#investProInstType").change();
	$("#negativeNotFinaInst").change();
	$('.select2_width').select2({
		allowClear : true,
		minimumResultsForSearch : Infinity
	});
	
	$('#negativeNotFinaInst').val("1");
	$('#negativeNotFinaInst').select2("destroy");
	$('#negativeNotFinaInst').select2({
		allowClear : true,
		minimumResultsForSearch : Infinity}).trigger('change');
}