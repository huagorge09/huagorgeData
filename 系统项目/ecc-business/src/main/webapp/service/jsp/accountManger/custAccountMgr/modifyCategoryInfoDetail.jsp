<%@ page language="java" isELIgnored="false"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>分类信息修改</title>
<style type="text/css">
.table input[type='text'],select{
	max-width: 250px !important;
}

.select2{
	max-width: 250px !important;
}

.btn-query {
	position: absolute;
	margin-left: 20px;
	padding: 3px 10px;
	margin-top: 3px;
}

.btn-add {
	padding: 3px 10px;
	float: right;
	margin-right: 60px;
}

input[type="radio"],input[type="checkbox"] {
	margin: 2px 8px;
}

.select_addr1 {
	width: 115px !important;
	float: left;
	margin-right: 12px;
}

.select_addr2 {
	width: 115px !important;
	float: left;
}

.table>tbody+tbody {
	border-top: 0 !important;
}

.table {
	margin-bottom: 0 !important;
}

.addResident {
	display: inline-block;
	border: 1px #fc6821 solid;
	width: 16px;
	background-color: #fc6821;
	color: #fff;
	height: 16px;
	text-align: center;
	line-height: 11px;
	border-radius: 20px;
	font-size: 20px;
	cursor: pointer;
	margin: 9px;
}

.modal-open .modal {
	top: 150px !important;
	height: auto !important;
}

.ui-jqgrid-hdiv {
	overflow: hidden;
}
</style>
<script type="text/javascript">
var projectPath = '<%=context%>';
var widgetPath = '<%=context%>service/widget/';
var accountPath = '<%=context%>service/custInfoManager/';
	//默认加载
	$(function() {
		//把访问路径传到js
		setPath(projectPath, widgetPath, accountPath);
	});
</script>
</head>
<body class="fixed-nav gray-bg">
	<div class="modal-content" style="border: 0px;box-shadow: 0 0px 0px;">
		<input id="permissionId" type="hidden" value="${ permissionId }" />
		<input id="operatorId" type="hidden" value="${ operatorId }" />
		<input id="pslInvIdtp" type="hidden" value="${dto.idtp }" />
		<input id="pslInvIdno" type="hidden" value="${dto.idno }" />
		<input id="selOpenType" type="hidden" value="${dto.invtp }" />
		<input id="invtp" type="hidden" value="${dto.invtp }" />
		<input id="invnm" 	 	 type="hidden" value="${ dto.invnm }"/>
		<input id="custnos" type="hidden" value="${custno }" />
		<input id="preInvprtp" type="hidden" value="${dto.invprtp}"/>
		<input class="custsimpnm" type="hidden" value="${dto.acctabbr }" />
		<div class="page-header" style="margin: 0;">
			<h4 class="page-title">分类信息修改</h4>
			<select id="comNation" name="comNation" style="display: none;">
				<c:forEach var="item" items="${nationArray}">
					<c:if test="${item.PMCO != '156'}">
						<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
					</c:if>
				</c:forEach>
			</select>
		</div>
		<form name="form1" id="form1" method="post" action=""onreset="customReset();">
			<div class="page-body">
				<table class="table table-bordered maintable" id="showTable">
					<colgroup>
						<col width="10%">
						<col width="20%">
						<col width="25%">
						<col width="20%">
						<col width="25%">
					</colgroup>
					<tbody>
						<div class="col-sm-11 form-inner" style="display: none;">
							<select name="comNation" id="comNation" class='form-control select2_width' style="display: none;">
								<c:forEach var="item" items="${nationArray}">
									<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
								</c:forEach>
							</select>
						</div>
						<!-- 税收居民信息 -->
						<textarea name="accountInfo" style="display: none;" id="accountInfo">${userTaxArray}</textarea>
						
						<tr>
							<th rowspan="2">证件信息</th>
							<td>客户名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									${dto.invnm }
									<input type="hidden" id="invNm" value="${dto.invnm}">
								</div>
							</td>
							<td>客户类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									${dto.invtpnm }
								</div>
							</td>
						</tr>
						<tr>
							<td>证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									${dto.idtpnm }
								</div>
							</td>
							<td>证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									${dto.idno }
								</div>
							</td>
						</tr>
						
					</tbody>
				</table>
				</br></br>
				<!-- 分类信息 -->
				<table class="table table-bordered maintable" id="categoryInfo">
					<colgroup>
						<col width="10%">
						<col width="20%">
						<col width="25%">
						<col width="20%">
						<col width="25%">
					</colgroup>
					<tbody id="investClassInfo">
					<c:if test="${invtp == '0' }">
						<tr class="custsimpnmTr">
							<td rowspan="10" id="classInfoTh">分类信息</td>
							<td>客户简称：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="custsimpnm" id="custsimpnm" class='form-control select2-q' onchange="getSecond(this.value);">
										<option value="">--</option>
										<c:forEach var="item" items="${custFirst}">
											<option value="${item.PMCO}" ${item.PMCO == dto.acctabbr ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="instrepcode" id="instrepcode" class='form-control select2-q'>
										<option value="">--</option>
									</select>
									<input type="hidden" name="hinstrepcode" id="hinstrepcode" value="${dto.instrepcode }" />
								</div>
							</td>
						</tr>
					</c:if>
						<tr>
						<c:if test="${invtp != '0' }">
							<td rowspan="10" id="classInfoTh">分类信息</td>
						</c:if>
							<td>业务类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="businesstp" id="businesstp"
										class='form-control select2_width'>
										<c:forEach var="item" items="${businesstpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.businesstp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>公司类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="companyTp" id="companyTp"
										class='form-control select2_width'>
										<c:forEach var="item" items="${cmptpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.industryType ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>投资者类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="invprtp" id="invprtp" onchange="interactInvProInfo();" class='form-control select2_width'>
										<option value="0" ${dto.invprtp == '0' ? "selected" : "" }>专业投资者</option>
										<option value="1" ${dto.invprtp == '1' ? "selected" : "" }>普通投资者</option>
									</select>
								</div>
							</td>
							<td>地域类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="regioncode" id="regioncode"
										class='form-control select2_width'>
										<c:forEach var="item" items="${regiontpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.regiontp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>反洗钱类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="fxqTp" id="fxqTp" class='form-control select2_width'>
										<c:forEach var="item" items="${fundrisklevlArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.amlrisktype ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>反洗钱备注：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='fxqDesc' id='fxqDesc' value="${dto.fxqremark }"/>
								</div>
							</td>
						</tr>

						<tr>
							<td>税收居民身份：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type="hidden" class="taxTypeVal" value="${dto.taxType }">
									<select name="taxType" id="taxType" onchange="taxTypeChange(this);" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${taxTypeArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.taxType ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>

						<%-- <tr class="otherTaxTypeInfoBg" style="display: none;">
							<td>税收居民身份声明：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type="hidden" class="taxTypeDeclVal" value="${dto.taxTypeDecl }">
									<select name="taxTypeDecl" id="taxTypeDecl"
										class='form-control select2_width'>
										<option value="">--</option>
										<option value="1" ${dto.taxTypeDecl == '1' ? "selected" : "" }>是</option>
										<option value="0" ${dto.taxTypeDecl == '0' ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
						</tr> --%>

						<tr class="instNotResidentBg">
							<td>消极非金融机构：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type="hidden" class="negativeNotFinaInstVal" value="${dto.negativeNotFinaInst }">
									<select name="negativeNotFinaInst" id="negativeNotFinaInst" onchange="negativeNotFinaChange(this);" value="${dto.negativeNotFinaInst }" class='form-control select2_width'>
										<option value="1" ${dto.negativeNotFinaInst == '1' ? "" : "" }>居民消极非金融机构</option>
										<option value="2" ${dto.negativeNotFinaInst == '2' ? "" : "" }>非居民消极非金融机构</option>
										<option value="3" ${dto.negativeNotFinaInst == '3' ? "" : "" }>其它机构</option>
									</select>
								</div>
							</td>
						</tr>

						<tr class="negativeNotFinaInstBg">
							<td>存在非居民控制人标识：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type="hidden" id="controlPerTaxDeclVal" class="controlPerTaxDeclVal" value="${dto.controlPerTaxDecl }">
									<select name="controlPerTaxDecl" id="controlPerTaxDecl" onchange="controllerPerTaxDecl(this);" class='form-control select2_width'>
										<option value="">--</option>
										<option value="1" ${dto.controlPerTaxDecl == '1' ? "selected" : "" }>是</option>
										<option value="0" ${dto.controlPerTaxDecl == '0' ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
						</tr>
					</tbody>

					<tbody class="instInvestPro" id="instInvestPro" style="display: none;">
						<tr>
							<td id="instInvestProTh" rowspan="10">专业投资者信息</td>
							<td>机构类型：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type="hidden" class="investProInstTypeVal" value="${dto.investProInstType }">
									<select name="investProInstType" id="investProInstType" class='form-control select_addr1' onchange="getInvestProInstSecond(this);">
										<c:forEach var="item" items="${investProInstArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.organType ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="investProInstSecond" id="investProInstSecond" class='form-control select_addr2'>
										<option value="">--</option>
									</select>
									<input type="hidden" name="hiInvestProInstSecond" id="hiInvestProInstSecond" value="${dto.investProInstSecond }"/>
								</div>
							</td>
						</tr>

						<tr class="instInvestProInfoBg">
							<td>近1年末净资产：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
								<input type="hidden" class="oneYearEndNetAssetVal" value="${dto.oneYearEndNetAsset }">
									<select name="oneYearEndNetAsset" id="oneYearEndNetAsset"
										class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${oneYearEndNetAssetArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.oneYearEndNetAsset ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>

						<tr class="instInvestProInfoBg">
							<td>近1年末金融资产：</td>
							<td class="white-bg" colspan="3">
							<input type="hidden" class="oneYearEndFinAssetVal" value="${dto.oneYearEndFinAsset }">
								<div class="col-sm-11 form-inner">
									<select name="oneYearEndFinAsset" id="oneYearEndFinAsset"
										class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${oneYearEndFinAssetArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.oneYearEndFinAsset ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>

						<tr class="instInvestProInfoBg">
							<td>投资经历：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type="hidden" class="investExperienceVal" value="${dto.investExperience }">
									<select name="investExperience" id="investExperience"
										class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${investExperienceArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.investExperience ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
					</tbody>

					<!-- 个人专业投资者 -->
					<tbody class="personInstInvestPro" id="personInstInvestPro" style="display: none;">
						<tr>
							<td rowspan="10">专业投资者信息</td>
							<td>近三年年均收入：</td>
							<td class="white-bg">
								<input type="hidden" class="threeAnnualIncomeVal" value="${dto.threeAnnualIncome }">
								<div class="col-sm-11 form-inner">
									<select name="threeAnnualIncome" id="threeAnnualIncome"
										class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${threeAnnualIncomeArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.threeAnnualIncome ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>

							<td>金融资产：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
								<input type="hidden" class="financialAssetVal" value="${dto.financialAsset }">
									<select name="financialAsset" id="financialAsset"
										class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${financialAssetArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.financialAsset ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>

						<tr>
							<td>投资经历：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type="hidden" class="indInvExperienceVal" value="${dto.indInvExperience }">
									<select name="indInvExperience" id="indInvExperience"
										class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${indInvExperienceArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.indInvExperience ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>

							<td>相关工作经历：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type="hidden" class="relatedWorkExpVal" value="${dto.relatedWorkExp }">
									<select name="relatedWorkExp" id="relatedWorkExp"
										class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${relatedWorkExpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.relatedWorkExp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>

						<tr>
							<td>金融职业：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type="hidden" class="finProfessionsVal" value="${dto.finProfessions }">
									<select name="finProfessions" id="finProfessions"
										class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${finProfessionsArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.finProfessions ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
					</tbody>
				</table>

				<table class="table table-bordered maintable" id="residentType" style="display: none;">
					<colgroup>
						<col width="10%">
						<col width="20%">
						<col width="25%">
						<col width="20%">
						<col width="25%">
					</colgroup>
					<tbody id="investClassInfo">
						<tr>
							<td rowspan="100" id="residentTitle">税收居民信息</td>
							<td>中文姓名：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' disabled="disabled"
										name='userCustName' id='userCustName' />
								</div>
							</td>
						</tr>
						<tr class="englishNameDiv"></tr>
						<tr class="sexAndbirth">
							<td><font color="red">*</font>性别：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="sex" id="sex" class='form-control select2_width'>
										<option value="">请选择</option>
										<option value="1">男</option>
										<option value="0">女</option>
									</select>
								</div>
							</td>
						</tr>

						<tr class="sexAndbirth">
							<td><font color="red">*</font>出生日期</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="birthDate" class="form-control"
										id="birthDate">
								</div>
							</td>
						</tr>
						<tr class="birthAddress">
							<td><font color="red">*</font>出生地：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner" style="float: left;">
									<select name="birth_nation" id="birth_nation"
										class='form-control select_addr1'
										onchange="changeNation(this)">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select> <select name="birth_region" id="birth_region"
										class='form-control select_addr2' style="float: left;"></select>
								</div>
								<div class="col-sm-11 form-inner"
									style="float: left; margin-top: -34px; margin-left: 265px;">
									<input type="text" name="birth_address" id="birth_address"
										class="form-control">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font><span class="resideNation">现居国家</span>：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="reside_nation" class='form-control select_addr1'
										onchange="changeNation(this)";>
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select> <select class='form-control select_addr2' name="reside_region"></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font><span class="resideAddress">现居地址</span>：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="reside_address" class='form-control'
										id="reside_address">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>Present Address：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="reside_address_english"
										class='form-control' id="reside_address_english">
								</div>
							</td>
						</tr>
						<tr class="taxNationality">
							<td><font color="red">*</font>税收居民国(地区)</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="taxNationality" class='form-control select_addr1'
										onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select> <select name="taxArea" class='form-control select_addr2'></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>纳税人识别号：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="taxpayerCode" class='form-control' style="float: left;">
									<label style="line-height: 34px;">
										<input name="isTaxpayerEvent" class="i-checks isTaxpayerEvent" type="checkbox" onclick="isTaxpayer(this)">无<i class="residentEach"></i>
									</label>
								</div>
							</td>
						</tr>
						<tr style="display: none;">
							<td><font color="red">*</font>无识别号的原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="notCodeCause" class='form-control select2_width'
										onchange="notCodeCauseEvent(this)">
										<option value="">--</option>
										<option value="1">居民国（地区）不发放纳税人识别号</option>
										<option value="2">账号持有人未能取得纳税人识别号</option>
									</select>
								</div>
							</td>
						</tr>
						<tr style="display: none;" class="causeText">
							<td><font color="red">*</font>未取得原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="notGetCause" class="form-control">
								</div>
							</td>
						</tr>
						<tr class="since" style="display: none;">
							<td><font color="red">*</font>税收居民国(地区)</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select class='form-control select_addr1' name="taxNationality"
										onchange="changeNation(this)">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select> <select name="taxArea" class='form-control select_addr2'></select>
									<span class="addResident" onclick="addResident();">+</span>
								</div>
							</td>
						</tr>
						<tr class="since" style="display: none;">
							<td><font color="red">*</font>纳税人识别号：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="taxpayerCode"
										class="form-control" style="float: left;">
									<label style="line-height: 34px;">
										<input name="isTaxpayerEvent" class="i-checks isTaxpayerEvent" type="checkbox" onclick="isTaxpayer(this)">无
										<i class="residentEach"></i>
									</label>
								</div>
							</td>
						</tr>
						<tr class="since sinceNotCodeCause" style="display: none;">
							<td><font color="red">*</font>无识别号的原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="notCodeCause" class='form-control select2_width'
										onchange="notCodeCauseEvent(this);">
										<option value="">--</option>
										<option value="1">居民国（地区）不发放纳税人识别号</option>
										<option value="2">账号持有人未能取得纳税人识别号</option>
									</select>
								</div>
							</td>
						</tr>
						<tr class="since sinceNotCodeCause" style="display: none;">
							<td><font color="red">*</font>未取得原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="notGetCause" class="form-control">
								</div>
							</td>
						</tr>
					</tbody>
					<input type="hidden" name="taxresident" />
				</table>
				<!-----------------------------非居民信息与非居民信息E-------------------------->

				<!-----------------------------非居民控制人S-------------------------->

				<table class="table table-bordered maintable" id="controllerInfo" style="display: none;">
					<colgroup>
						<col width="10%">
						<col width="20%">
						<col width="25%">
						<col width="20%">
						<col width="25%">
					</colgroup>
					<tbody id="controllerClassInfo">
						<tr>
							<th rowspan="1000" id="residentTitle">非居民控制人信息</th>
							<td><font color="red">*</font>控制人中文姓名：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="ChineseName2"
										id="ChineseName2">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人英文姓：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control'
										name="EnglishFamliyName3" id="EnglishFamliyName3">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人英文名：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control'
										name="EnglishFirstName3" id="EnglishFirstName3">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人类型</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="ControllerType"
										class='form-control select2_width'>
										<option value="">--</option>
										<option value="001">法人控制人-所有权</option>
										<option value="002">法人控制人-其他</option>
										<option value="003">法人控制人-高管人员</option>
										<option value="004">信托-委托人</option>
										<option value="005">信托-受托人</option>
										<option value="006">信托-监察人</option>
										<option value="007">信托-受益人</option>
										<option value="008">信托-其他控制人</option>
										<option value="009">其他-等同于委托人</option>
										<option value="010">其他-等同于受托人</option>
										<option value="011">其他-等同于监察人</option>
										<option value="012">其他-等同于受益人</option>
										<option value="013">其他-等同于其他控制人</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人非居民标识：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="ConNonResiFlag"
										readonly="readonly" id="ConNonResiFlag">
								</div>
							</td>
						</tr>
						<tr>
							<td>控制人持股比例：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="ConShareRatio"
										id="ConShareRatio">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人现居国家：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="LivingCountry2" class='form-control select_addr1'
										onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select> <select name="LivingCountry21"
										class='form-control select_addr2'></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人现居地址：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="LivingAddress5"
										id="LivingAddress5">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人现居地址英文：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="LivingAddress7"
										id="LivingAddress7">
								</div>
							</td>
						</tr>
						<tr>
							<td>控制人国籍：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="RegRegionCode2" class='form-control select_addr1'
										onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select> <select name="RegRegionCode21"
										class='form-control select_addr2'></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人出生日期：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="BirthDate2"
										id="BirthDate2" />
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人出生国家：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="BirthCountry2" class='form-control select_addr1'
										onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select> <select name="BirthCountry21"
										class='form-control select_addr2'></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人出生城市英文：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="BirthCity2"
										id="BirthCity2">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人税收居民国</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="TaxCountry2" class='form-control select_addr1'
										onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select> <select name="TaxCountry21" class='form-control select_addr2'></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>纳税人识别号：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="TaxID2" class="form-control" style="float: left;">
									<label style="line-height: 34px;">
										<input name="isTaxpayerEvent2" class="i-checks isTaxpayerEvent" type="checkbox" onclick="isTaxpayer(this)">无
										<i class="residentEach"></i>
									</label>
								</div>
							</td>
						</tr>
						<tr style="display: none;">
							<td><font color="red">*</font>无识别号的原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="notCodeCause2" class='form-control select2_width'
										onchange="notCodeCauseEvent(this);">
										<option value="">--</option>
										<option value="1">居民国（地区）不发放纳税人识别号</option>
										<option value="2">账号持有人未能取得纳税人识别号</option>
									</select>
								</div>
							</td>
						</tr>
						<tr style="display: none;">
							<td><font color="red">*</font>未取得原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="Specification2">
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<!-----------------------------非居民控制人E-------------------------->
			</div>
			<div class="page-footer" style="margin-top: 20px;text-align: center;">
				<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提交" name='btnSubmit'>提交</button>
				<button id="claseBtn" name="claseBtn" type="button" class="btn btn-link" data-dismiss="modal" value="重置">重置</button>
				<button id="reset" name="reset" type="reset" class="btn btn-link" style="display: none;" value="重置">重置</button>
			</div>
		</div>
		</form>
	</div>
	<script type="text/javascript" src="<%=context%>web/js/accountManger/custAccountMgr/modifyCategoryInfoDetail.js?v="+<%=dateStr%>></script>
</body>
</html>