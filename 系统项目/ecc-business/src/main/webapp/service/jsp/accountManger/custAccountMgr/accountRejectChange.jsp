<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>账户类驳回修改</title>
<style type="text/css">
.table input[type='text'],select{
	max-width: 250px !important;
}

.select2{
	max-width: 250px !important;
}
.btn-query{
	position: absolute;
    margin-left: 20px;
    padding: 3px 10px;
    margin-top: 3px;
}

.btn-add{
	padding: 3px 10px;
    float: right;
    margin-right: 60px;
}
input[type="radio"], input[type="checkbox"]{
	margin: 2px 8px;
}

.select_addr1{
	max-width: 115px !important;
	float: left;
	margin-right: 12px;
}
.select_addr2{
	max-width: 115px !important;
	float: left;
}
.table > tbody + tbody{
	border-top: 0 !important;
}
.table{
	margin-bottom: 0 !important;
}

.ui-jqgrid-hdiv{
	overflow: hidden;
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
</style>
<script type="text/javascript">
var projectPath = '<%=context%>';
var widgetPath = '<%=context%>service/widget/';
var accountPath = '<%=context%>service/accountCheckManager/';
//默认加载
$(function(){
	//把访问路径传到js
	setPath(projectPath,widgetPath,accountPath);
});
</script>
</head>
<body class="fixed-nav gray-bg">
	<div class="modal-content" style="border: 0px;box-shadow: 0 0px 0px;">
		<input id="permissionId" type="hidden" value="${ permissionId }"/>
		<input id="operatorId" 	 type="hidden" value="${ operatorId }"/>
		<input id="custno" 	 type="hidden" value="${dto.custno }"/>
		<input id="pslInvIdtp" 	 type="hidden" value="${ dto.idtp }"/>
		<input id="pslInvIdno" 	 type="hidden" value="${ dto.idno }"/>
		<input id="invtp" 	 	 type="hidden" value="${ dto.invtp }"/>
		<input id="invnm" 	 	 type="hidden" value="${ dto.invnm }"/>
		<input id="accountInfo"	 type="hidden" value='${ userTaxArray }'/>
		<input type="hidden" id="docbusinesstpVal" value="${dto.docbusinesstp }">
		<input id="serialno" value='${serialno }' type="hidden" />
		<input id="selOpenType" type="hidden" value="${dto.invtp }" />
		<input id="invprtp" type="hidden" value="${dto.invprtp}" />
		<input id="preRisklevel" type="hidden" value="dto.custrisklevl" />
		<input id="preInvprtp" type="hidden" value="${dto.invprtp}" />
		<input id="dsapkind" type="hidden" value="${dto.dsapkind }" />
		<input id="dto" type="hidden" value='${dto}' />
		<input id="hiSpecRiskLevel" type="hidden" value='${dto.custrisklevl}' />
		<input id="specriskLevel" value="${dto.specriskLevel }" type="hidden" />
		<input id="tano" type="hidden" value='${dto.tano }' />
		
		<input id="oidlist" value='${dto.oidlist }' type="hidden" />
		<input id="ocontactlist" value='${dto.ocontactlist }' type="hidden" />
		<input id="documentlist" value='${dto.documentlist }' type="hidden" />
		
		
		<select id="comNation" name="comNation" style="display: none;">
			<c:forEach var="item" items="${nationArray}">
				<c:if test="${item.PMCO != '156'}">
					<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
				</c:if>
			</c:forEach>
		</select>
		
		<div class="page-header" style="margin: 0;">
			<h4 class="page-title">账户类驳回修改</h4>
		</div>
	 	<form name="form" id="form" method="post" action="" >
		<div class="page-body">
			<!-- 个人信息 -->
			<div id="baseInfoContent" class="typeContent">
				<table id=tabPslInfo class="table table-bordered">
					<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
					<tbody>
						<tr>
							<th rowspan="3">业务信息</th>
							<td>TA代码：</td>
							<td class="white-bg" colspan="3">
								<span class="col-sm-12">
									<span id="ptano" name="ptano">${dto.tano }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>客户号：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pcustno" name="pcustno">${dto.custno }</span>
								</span>
							</td>
							<td>业务类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pdsapkind" name="pdsapkind">${dto.dsapkindnm }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>基金账号：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pfundacco" name="pfundacco">${dto.fundacct }</span>
								</span>
							</td>
							<td>交易账号：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ptradeacco" name="ptradeacco">${dto.tradeacco }</span>
								</span>
							</td>
						</tr>
					</tbody>
					<tbody id="pidtpinfo">
						<tr>
							<th rowspan="3">证件信息</th>
							<td><font color="red">*</font>投资者名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pinvnm' id='pinvnm' value="${dto.invnm }"/>
								</div>
							</td>
							<td><font color="red">*</font>注册登记证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pidtp" id="pidtp" class='form-control select2_width'>
										<c:forEach var="item" items="${idtpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.idtp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								
								<!-- 隐藏，用于添加其他证件信息时使用 -->
								<div class="orgInvIdtpDiv" style="display: none;">
									<div class="col-sm-11 form-inner" style="line-height: 32px;">
										<select name="orgInvIdtp" id="ooidtp" class='form-control orgInvIdtp'>
											<c:forEach var="item" items="${seatidtpArray}">
												<option value="${item.PMCO}" ${item.PMCO == '11' ?"selected": "" } >${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
											</c:forEach>
										</select>
									</div>
								</div>
								
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>注册登记证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='pidno' id='pidno' value="${dto.idno }"/>
								</div>
							</td>
							<td><font color="red">*</font>注册登记证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='pidvalidate' id='pidvalidate' value="${dto.idvalidate }"/>
									<label style="line-height: 34px;">
										<input class="i-checks" id="changepidvalidate" type="checkbox"
										   onclick="changetime('changepidvalidate','pidvalidate');">长期
									</label><br>
								</div>
							</td>
						</tr>
					</tbody>
					<tbody id="pbankinfo">
						<tr>
							<th rowspan="3">银行信息</th>
							<td><font color="red">*</font>开户银行：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pbnkNO" id="pbnkNO" class='form-control select2_width'>
										<c:forEach var="item" items="${bankBaseList}">
											<option value="${item.bnkNo}" ${item.bnkNo == dto.bnkNo ? "selected" : "" }>${item.bnkNo}&nbsp;&nbsp;${ item.bnkNm }</option>
										</c:forEach>
									</select>
									
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>预留银行全称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='popenName' id='popenName' value="${dto.openName }"/>
								</div>
							</td>
							<td><font color="red">*</font>预留银行开户地：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="popenAddr" id="popenAddr" class='form-control select_addr1' onchange="getCitys(this.value,'1','popenbankcity','hiddenpopenbankcity')">
										<option value="">--</option>
										<c:forEach var="item" items="${provinces}">
											<option value="${item.PMCO}" ${item.PMCO == dto.openAddr ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="popenbankcity" id="popenbankcity" class='form-control select_addr2'><option value="">--</option></select>
									<input type="hidden" name="hiddenpopenbankcity" id="hiddenpopenbankcity" value="${dto.openBankCity }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>预留银行户名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pbankaccoNm' id='pbankaccoNm' value="${dto.bankAccoNm }"/>
								</div>
							</td>
							<td><font color="red">*</font>预留银行账号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pbankAcco' id='pbankAcco' value="${dto.bankAcco }"/>
								</div>
							</td>
						</tr>
					</tbody>
					<tbody id="potherinfo">
						<tr>
							<th rowspan="9" id="potherinfoTh">其他信息</th>
							<td>性别：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="psex" id="psex" class='form-control select2_width'>
										<option value="">请选择</option>
										<option value="1" ${dto.sex == '1' ? "selected" : ""}>男</option>
										<option value="0" ${dto.sex == '0' ? "selected" : ""}>女</option>
									</select>
								</div>
							</td>
							<td>国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pnationalitycode" id="pnationalitycode" class='form-control select2_width'>
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.nationalitycode ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>学历：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pedlevel" id="pedlevel" class='form-control select2_width'>
										<c:forEach var="item" items="${edlevelArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.edlevel ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>职业：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pvoccode" id="pvoccode" class='form-control select2_width'>
										<c:forEach var="item" items="${vacodeArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.voccode ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>年收入：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pincome" id="pincome" class='form-control select2_width'>
										<c:forEach var="item" items="${incomeArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.income ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td><font color="red">*</font>风险承受能力：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pcustrisklevl" id="pcustrisklevl" class='form-control select2_width' style="float: left;">
										<c:forEach var="item" items="${custrisklevelArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.custrisklevl ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<label style="margin-top: 8px;">
										<input id="indSpecRiskLevel" class="i-checks" type="checkbox" name="indSpecRiskLevel" onclick="changeSpecRiskLevel(this,'pcustrisklevl');" >最低
									</label>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>办公电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='ptel' id='ptel' value="${dto.tel }"/>
								</div>
							</td>
							<td>住宅电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='phousetel' id='phousetel' value="${dto.housetel }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>移动电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pmobile' id='pmobile' value="${dto.mobile }"/>
								</div>
							</td>
							<td>传真号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pfax' id='pfax' value="${dto.fax }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>传真委托：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pfaxdelegate" id="pfaxdelegate" class='form-control select2_width'>
										<option value="1" ${dto.faxdelegate == '1' ? "selected" : ""}>是</option>
										<option value="0" ${dto.faxdelegate == '0' ? "selected" : ""}>否</option>
									</select>
								</div>
							</td>
							<td>电子邮件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pemail' id='pemail' value="${dto.email }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>通讯地址:</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='paddr' id='paddr' value="${dto.addr }"/>
								</div>
							</td>
							<td><font color="red">*</font>邮政编码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='ppostcode' id='ppostcode' value="${dto.postcode }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>上交所股东代码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pslShsecacc' id='pslShsecacc' value="${dto.shsecacc }"/>
								</div>
							</td>
							<td>深交所股东代码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pslSzsecacc' id='pslSzsecacc' value="${dto.szsecacc }"/>
								</div>
							</td>
						</tr>
					</tbody>
					
					<tbody id="pcategoryinfo" name="investClassInfo">  
						<tr>
							<th rowspan="9" id="investClassInfoTh">分类信息</th>
							<td>客户简称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pcustabbrcode" id="pcustabbrcode" class='form-control select_q' onchange="getSecond(this.value,'pcustinstreprcode');">
										<option value="">--</option>
										<c:forEach var="item" items="${custFirst}">
											<option value="${item.PMCO}" ${item.PMCO == dto.acctabbr ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="pcustinstreprcode" id="pcustinstreprcode" class='form-control select_q'>
										<option value="">--</option>
									</select>
									<input type="hidden" name="hinstrepcode" id="hinstrepcode" value="${dto.instrepcode }"/>
								</div>
								
								
							</td>
							<td><font color="red">*</font>投资者类型：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="pinvprtp" id="pinvprtp" onchange="interactInvProInfo(this)" class='form-control select2_width'>
										<option value="0" ${dto.invprtp == '0' ? "selected" : "" }>专业投资者</option>
										<option value="1" ${dto.invprtp == '1' ? "selected" : "" }>普通投资者</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>业务类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pbusinesstp" id="pbusinesstp" class='form-control select2_width'>
										<c:forEach var="item" items="${businesstpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.businesstp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>公司类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pcompanytp" id="pcompanytp" class='form-control select2_width'>
										<c:forEach var="item" items="${cmptpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.companytp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>地域类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pregiontp" id="pregiontp" class='form-control select2_width'>
										<c:forEach var="item" items="${regiontpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.regiontp ? "selected" : "" } ${item.PMCO == '0755' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>反洗钱类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pamlrisktype" id="pamlrisktype" class='form-control select2_width'>
										<c:forEach var="item" items="${fxqArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.amlrisktype ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>反洗钱备注：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pfxqremark' id='pfxqremark' value="${dto.fxqremark }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>税收居民身份：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="ptaxType" id="ptaxType" onchange="taxTypeChange(this);" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${taxTypeArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.taxType ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
					 </tbody>  
   					 <tbody class="personInstInvestPro" id="personInstInvestPro" name="personInstInvestPro">
   					 	<tr>
   					 		<th rowspan="10" id="personInstInvestTh">专业投资者信息</th>
   					 		<td>近三年年均收入：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="threeAnnualIncome" id="threeAnnualIncome" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${threeAnnualIncomeArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.threeAnnualIncome ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>金融资产：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="financialAsset" id="financialAsset" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${financialAssetArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.financialAsset ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						<tr>
   					 	</tr>
							<td>投资经历：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="indInvExperience" id="indInvExperience" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${indInvExperienceArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.indInvExperience ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>相关工作经历：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="relatedWorkExp" id="relatedWorkExp" class='form-control select2_width'>
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
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="finProfessions" id="finProfessions" class='form-control select2_width'>
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
			</div>
			
			<!-- 机构信息 -->
			<div id="orgBaseInfoContent" class="typeContent">
				<table id=tabOrgInfo class="table table-bordered maintable">
					<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
					<tbody>
						<tr>
							<th rowspan="3">业务信息</th>
							<td>TA代码：</td>
							<td class="white-bg" colspan="3">
								<span class="col-sm-12">
									<span id="otano" name="otano">${dto.tano }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>客户号：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocustno" name="ocustno">${dto.custno }</span>
								</span>
							</td>
							<td>业务类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="odsapkind" name="odsapkind">${dto.dsapkindnm }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>基金账号：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ofundacco" name="ofundacco">${dto.fundacct }</span>
								</span>
							</td>
							<td>交易账号：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="otradeacco" name="otradeacco">${dto.tradeacco }</span>
								</span>
							</td>
						</tr>
					</tbody>
					<tbody id="idinfo">
						<tr>
							<th rowspan="3">证件信息</th>
							<td><font color="red">*</font>投资者名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='oinvnm' id='oinvnm' value="${dto.invnm }"/>
								</div>
							</td>
							<td><font color="red">*</font>注册登记证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="oidtp" id="oidtp" class='form-control select2_width'>
										<c:forEach var="item" items="${seatidtpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.idtp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>注册登记证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='oidno' id='oidno' value="${dto.idno }"/>
								</div>
							</td>
							<td><font color="red">*</font>注册登记证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='oidvalidate' id='oidvalidate' value="${dto.idvalidate }"/>
									<label style="line-height: 34px;">
										<input class="i-checks" id="changeoidvalidate" type="checkbox"
										   onclick="changetime('changeoidvalidate','oidvalidate');">长期
									</label><br>
								</div>
							</td>
						</tr>
					</tbody>
					<tbody id="obankinfo">
						<tr>
							<th rowspan="3">银行信息</th>
							<td><font color="red">*</font>开户银行：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="obnkNO" id="obnkNO" class='form-control select2_width'>
										<c:forEach var="item" items="${bankBaseList}">
											<option value="${item.bnkNo}"  ${item.bnkNo == dto.bnkNo ? "selected" : "" }>${item.bnkNo}&nbsp;&nbsp;${ item.bnkNm }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>预留银行全称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='oopenName' id='oopenName' value="${dto.openName }"/>
								</div>
							</td>
							<td><font color="red">*</font>预留银行开户地：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="oopenAddr" id="oopenAddr" class='form-control select_addr1' onchange="getCitys(this.value,'0','oopenbankcity','hiddenoopenbankcity')">
										<option value="">--</option>
										<c:forEach var="item" items="${provinces}">
											<option value="${item.PMCO}" ${item.PMCO == dto.openAddr ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="oopenbankcity" id="oopenbankcity" class='form-control select_addr2'><option value="">--</option></select>
									<input type="hidden" name="hiddenoopenbankcity" id="hiddenoopenbankcity" value="${dto.openBankCity }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>预留银行户名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='obankaccoNm' id='obankaccoNm' value="${dto.bankAccoNm }"/>
								</div>
							</td>
							<td><font color="red">*</font>预留银行账号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='obankAcco' id='obankAcco' value="${dto.bankAcco }"/>
								</div>
							</td>
						</tr>
					</tbody>
					
					<tbody id="oinstrepinfo">
						<tr>
							<th rowspan="3">法人信息</th>
							<td><font color="red">*</font>法定代表人姓名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='oinstrepnm' id='oinstrepnm' value="${dto.instrepnm }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>法定代表人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="oinstrepnation" id="oinstrepnation" class='form-control select2_width'>
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.instrepnation ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td><font color="red">*</font>法定代表人证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="oinstrepidtp" id="oinstrepidtp" class='form-control select2_width'>
										<c:forEach var="item" items="${idtpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.instrepidtp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>法定代表人证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="18" class='form-control' name='oinstrepidno' id='oinstrepidno' value="${dto.instrepidno }"/>
								</div>
							</td>
							<td><font color="red">*</font>法定代表人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='oinstrepvalidate' id='oinstrepvalidate' value="${dto.instrepvalidate }"/>
									<label style="line-height: 34px;">
										<input class="i-checks" id="changeorgInstrepidvalidate" type="checkbox"  
											   onclick="changetime('changeorgInstrepidvalidate','oinstrepvalidate');">长期
									</label><br>
								</div>
							</td>
						</tr>
					</tbody>
					
					<tbody id="oprincipalinfo">
						<tr>
							<th rowspan="3">机构负责人信息</th>
							<td><font color="red">*</font>机构负责人姓名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='oprincipalname' id='oprincipalname' value="${dto.principalname }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>机构负责人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="oprincipalnation" id="oprincipalnation" class='form-control select2_width'>
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.principalnation ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td><font color="red">*</font>机构负责人证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="oprincipalidtp" id="oprincipalidtp" class='form-control select2_width'>
										<c:forEach var="item" items="${idtpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.principalidtp ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>机构负责人证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="18" class='form-control' name='oprincipalidno' id='oprincipalidno' value="${dto.principalidno }"/>
								</div>
							</td>
							<td><font color="red">*</font>机构负责人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='oprincipalvalidt' id='oprincipalvalidt' value="${dto.principalvalidt }"/>
									<label style="line-height: 34px;">
										<input class="i-checks" id="changeorgPrincipalidvalidate" type="checkbox" 
											   onclick="changetime('changeorgPrincipalidvalidate','oprincipalvalidt');"/>长期
									</label><br>
								</div>
							</td>
						</tr>
					</tbody>
					
					<tbody id="continfo">
						<tr>
							<th rowspan="6">经办人员信息</th>
							<td><font color="red">*</font>经办人授权范围：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="ocontactgrant" id="ocontactgrant" class='form-control select2_width'>
										<c:forEach var="item" items="${contprivArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.contactgrant ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								
								<div style="display: none;" class="orgContactrightDiv">
									<div class="col-sm-11 form-inner">
										<select name="orgContactright" id="ocontactgrant1" class='form-control orgContactright'>
											<c:forEach var="item" items="${contprivArray}">
												<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</td>
							<td><font color="red">*</font>经办人姓名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="18" class='form-control' name='ocontact' id='ocontact' value="${dto.contact }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>经办人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="ocontactnation" id="ocontactnation" class='form-control select2_width'>
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.contactnation ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								<div class="orgContnationDiv" style="display: none;">
									<div class="col-sm-11 form-inner">
										<select name="orgContnation" class="orgContnation" class='form-control'>
											<c:forEach var="item" items="${nationArray}">
												<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</td>
							<td><font color="red">*</font>经办人证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="ocontidtp" id="ocontidtp" class='form-control select2_width'>
										<c:forEach var="item" items="${idtpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.contidtp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								<div class="orgContidtpDiv" style="display: none;">
									<div class="col-sm-11 form-inner">
										<select name="orgContidtp" class="orgContidtp" class='form-control'>
											<c:forEach var="item" items="${idtpArray}">
												<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>经办人证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="18" class='form-control' name='ocontidno' id='ocontidno' value="${dto.contidno }"/>
								</div>
							</td>
							<td><font color="red">*</font>经办人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='ocontvalidate' id='ocontvalidate' value="${dto.contvalidate }"/>
									<label style="line-height: 34px;">
										<input class="i-checks" id="changeocontvalidate" type="checkbox" 
											   onclick="changetime('changeocontvalidate','ocontvalidate');"/>长期
									</label><br>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>经办人办公电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='ocontphone' id='ocontphone' value="${dto.contphone }"/>
								</div>
							</td>
							<td><font color="red">*</font>经办人传真号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='ocontfax' id='ocontfax' value="${dto.contfax }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>经办人手机号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='ocontmobile' id='ocontmobile' value="${dto.contmobile }"/>
								</div>
							</td>
							<td>经办人电子邮件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='ocontemail' id='ocontemail' value="${dto.contemail }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>经办人通讯地址：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContAddr' id='orgContAddr' value="${dto.contAddr }"/>
								</div>
							</td>
							<td>经办人邮政编码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContPostcode' id='orgContPostcode' maxlength="6" value="${dto.contPostcode }"/>
								</div>
							</td>
						</tr>
					</tbody>
					
					<tbody id="ootherinfo">
						<tr>
							<th rowspan="15" id="ootherinfoTh">其他信息</th>
							<tr>
							<td>控股股东名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='oholdingname' id='oholdingname' value="${dto.holdingname }"/>
								</div>
							</td>
							<td>控股股东证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="oholdingidtp" id="oholdingidtp" class='form-control select2_width'>
										<c:forEach var="item" items="${seatidtpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.holdingidtp ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>控股股东证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='oholdingidno' id='oholdingidno' value="${dto.holdingidno }"/>
								</div>
							</td>
							<td>控股股东证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='oholdingvalidate' id='oholdingvalidate' value="${dto.holdingvalidate }"/>
									<label style="line-height: 34px;margin: 0">
										<input class="i-checks" id="changeoholdingvalidate" type="checkbox" 
											   onclick="changetime('changeoholdingvalidate','oholdingvalidate');"/>长期
									</label><br>
								</div>
							</td>
						</tr>
						<tr>
							<td>基金投资受益人名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='obeneficiary' id='obeneficiary' value="${dto.beneficiary }"/>
								</div>
							</td>
							<td><font color="red">*</font>客户风险承受能力：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner" style="width: 100%;">
									<select name="ocustrisklevl" id="ocustrisklevl" class='form-control select2_width' style="float: left;">
										<c:forEach var="item" items="${custrisklevelArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.custrisklevl ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<label style="margin-top: 8px;">
										<input id="instSpecRiskLevel" class="i-checks" type="checkbox" name="instSpecRiskLevel" 
											   onclick="changeSpecRiskLevel(this,'ocustrisklevl');"
										/>最低
									</label>
								</div>
							</td>
						<tr>
							<td>办公电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='oofficeTel' id='oofficeTel' value="${dto.tel }"/>
								</div>
							</td>
							<td>传真号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='ofax' id='ofax' value="${dto.fax }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>办公地址：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='oaddr' id='oaddr' value="${dto.addr }"/>
								</div>
							</td>
							<td><font color="red">*</font>邮政编码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='opostcode' id='opostcode' maxlength="6" value="${dto.postcode }"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>上交所股东代码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgShsecacc' id='orgShsecacc' maxlength="10" value="${dto.shsecacc }"/>
								</div>
							</td>
							<td>深交所股东代码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgSzsecacc' id='orgSzsecacc' maxlength="10" value="${dto.szsecacc }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>机构类型：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="organType" id="organType" class='form-control select2_width'>
										<c:forEach var="item" items="${organType}">
											<option value="${item.PMCO}" ${item.PMCO == dto.organType ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
					<tbody id="oidinfo">
						<tr>
							<th rowspan="1">其他证件信息</th>
							<td class="white-bg" colspan="4">
								<button id="btnAddId" type="button" class="btn btn-primary btn-add" value="增加">增加</button>
							</td>
						</tr>
					</tbody>
					
					<tbody id="ocontInfo">
						<tr>
							<th rowspan="1">其他经办人信息</th>
							<td class="white-bg" colspan="4">
								<button id="btnAddCont" type="button" class="btn btn-primary btn-add" value="增加">增加</button>
							</td>
						</tr>
					</tbody>
					</tbody>
   					<tbody id="ocategoryinfo" name="instClassInfo">
						<tr>
							<th rowspan="9" id="instClassInfoTh">分类信息</th>
							<td>客户简称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="custabbrcode" id="custabbrcode" class='form-control select_q' onchange="getSecond(this.value,'custinstreprcode');">
										<option value="">--</option>
										<c:forEach var="item" items="${custFirst}">
											<option value="${item.PMCO}" ${item.PMCO == dto.acctabbr ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="custinstreprcode" id="custinstreprcode" class='form-control select_q'>
										<option value="">--</option>
									</select>
									<input type="hidden" name="hinstrepcode" id="hinstrepcode" value="${dto.instrepcode }"/>
								</div>
							</td>
							<td><font color="red">*</font>投资者类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="oinvprtp" id="oinvprtp" onchange="interactInvProInfo(this)" class='form-control select2_width'>
										<option value="0" ${dto.invprtp == '0' ? "selected" : "" }>专业投资者</option>
										<option value="1" ${dto.invprtp == '1' ? "selected" : "" }>普通投资者</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>业务类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="businesstp" id="businesstp" class='form-control select2_width'>
										<c:forEach var="item" items="${businesstpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.businesstp ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>公司类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="companytp" id="companytp" class='form-control select2_width'>
										<c:forEach var="item" items="${cmptpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.companytp ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>地域类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="regiontp" id="regiontp" class='form-control select2_width'>
										<c:forEach var="item" items="${regiontpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.regiontp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>反洗钱类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="amlrisktype" id="amlrisktype" class='form-control select2_width'>
										<c:forEach var="item" items="${fxqArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.amlrisktype ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>反洗钱备注：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='fxqremark' id='fxqremark' value="${dto.fxqremark }"/>
								</div>
							</td>
						</tr>
						<tr class="otherTaxTypeInfoBg">
							<td>税收居民身份：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="otaxType" id="otaxType" onchange="taxTypeChange(this);" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${taxTypeArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.taxType ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr class="instNotResidentBg">
							<td>消极非金融机构：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="negativeNotFinaInst" id="negativeNotFinaInst" onchange="negativeNotFinaChange(this);" class='form-control select2_width'>
										<option value="">--</option>
										<option value="1" ${dto.negativeNotFinaInst == '1' ? "selected" : "" }>居民消极非金融机构</option>
										<option value="2" ${dto.negativeNotFinaInst == '2' ? "selected" : "" }>非居民消极非金融机构</option>
										<option value="3" ${dto.negativeNotFinaInst == '3' ? "selected" : "" }> 其它机构</option>
									</select>
								</div>
							</td>
						</tr>
						<tr class="negativeNotFinaInstBg">
							<td>存在非居民控制人标识：</td>
							<td class="white-bg">
								<input type="hidden" id="controlPerTaxDeclVal" value="${dto.controlPerTaxDecl}">
								<div class="col-sm-11 form-inner">
									<select name="controlPerTaxDecl" id="controlPerTaxDecl" onchange="controllerPerTaxDecl(this);" class='form-control select2_width'>
										<option value="">--</option>
										<option value="1" ${dto.controlPerTaxDecl == '1' ? "selected" : "" }>是</option>
										<option value="0" ${dto.controlPerTaxDecl == '0' ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
						</tr>
					</tbody>
   					<tbody class="instInvestPro" id="instInvestPro" name="instInvestPro">
						<tr>
							<th rowspan="4" id="instInvestProTh">专业投资者信息</th>
							<td>机构类型：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="investProInstType" id="investProInstType" class='form-control select_addr1' onchange="getInvestProInstSecond(this);">
										<c:forEach var="item" items="${investProInstArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.investProInstType ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
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
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="oneYearEndNetAsset" id="oneYearEndNetAsset" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${oneYearEndNetAssetArray}">
											<option value="${item.PMCO}" ${ item.PMCO == dto.oneYearEndNetAsset ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>近1年末金融资产：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="oneYearEndFinAsset" id="oneYearEndFinAsset" class='form-control select2_width'>
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
									<select name="investExperience" id="investExperience" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${investExperienceArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.investExperience ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<!-- 资料信息 -->
			<div id="documentContent" class="typeContent">
				<table id="tabDocument" class="table table-bordered">
					<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
					<tbody id="docinfo">
						<tr>
							<th rowspan="10">资料信息</th>
							<td>业务类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="docbusinesstp" id="docbusinesstp" class='form-control select2_width'>
										<c:forEach var="item" items="${docbusitpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.docbusinesstp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>客户资料是否齐全：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isalldoc" id="isalldoc" class='form-control select2_width'>
										<option value="0" ${dto.ifalldocument == '0' ? "selected" : "" }>否</option>
										<option value="1" ${dto.ifalldocument == '1' ? "selected" : "" }>是</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>客户资料是否原件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="ifOriginal" id="ifOriginal" class='form-control select2_width'>
										<option value="0" ${dto.ifOriginal == '0' ? "selected" : "" }>否</option>
										<option value="1" ${dto.ifOriginal == '1' ? "selected" : "" }>是</option>
									</select>
								</div>
							</td>
							<td>客户资料是否归档：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="issaved" id="issaved" class='form-control select2_width'>
										<option value="0" ${dto.issaved == '0' ? "selected" : "" }>否</option>
										<option value="1" ${dto.issaved == '1' ? "selected" : "" }>是</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>是否上传附件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isupload" id="isupload" class='form-control select2_width'>
										<option value="0" ${dto.isupload == '0' ? "selected" : "" }>否</option>
										<option value="1" ${dto.isupload == '1' ? "selected" : "" }>是</option>
									</select>
								</div>
							</td>
							<td>文件编号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='fileno' id='fileno' disabled="disabled" value="${dto.fileno }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>是否扫描：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isscan" id="isscan" class='form-control select2_width'>
										<option value="N" ${dto.isscan == 'N' ? "selected" : "" }>否</option>
										<option value="Y" ${dto.isscan == 'Y' ? "selected" : "" }>是</option>
									</select>
								</div>
							</td>
							<td>存档位置：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='keepaddress' id='keepaddress' disabled="disabled" value="${dto.keepaddress }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td>所属客户经理：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='salesaccmanager' id='salesaccmanager' value="${dto.salesaccmanager }"/>
								</div>
							</td>
						</tr>
						<tr id="documentinfo">
							<td id="contractListTh">资料列表：</td>
						</tr>
						<tr>
							<td>资料信息备注：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<textarea rows="3" cols="50" class='form-control'  name="remarkinfo" id="remarkinfo">${dto.remarkinfo }</textarea>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<!-- 税收居民信息 -->
			<div id="taxResidentsContent" class="typeContent">
				<!-----------------------------非居民信息与非居民信息S-------------------------->
				<table class="table table-bordered maintable" id="residentType">
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
									<input type='text' class='form-control' disabled="disabled" name='userCustName' id='userCustName' />
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
									<input type="text" name="birthDate" class="form-control" id="birthDate">
								</div>
							</td>
						</tr>
						<tr class="birthAddress">
							<td><font color="red">*</font>出生地：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner" style="float: left;">
									<select name="birth_nation" id="birth_nation" class='form-control select_addr1' onchange="changeNation(this)">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="birth_region" id="birth_region" class='form-control select_addr2' style="float: left;" ><option value="">--</option></select>
								</div>
								<div class="col-sm-11 form-inner" style="float: left;margin-top: -34px;margin-left: 265px;">
									<input type="text" name="birth_address" id="birth_address" class="form-control">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font><span class="resideNation">现居国家</span>：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="reside_nation" class='form-control select_addr1' onchange="changeNation(this)";>
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select class='form-control select_addr2' name="reside_region"><option value="">--</option></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font><span class="resideAddress">现居地址</span>：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="reside_address" class='form-control' id="reside_address" >
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>Present Address：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="reside_address_english" class='form-control' id="reside_address_english">
								</div>
							</td>
						</tr>
						<tr class="taxNationality">
							<td><font color="red">*</font>税收居民国(地区)</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="taxNationality" class='form-control select_addr1' onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="taxArea" class='form-control select_addr2'><option value="">--</option></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>纳税人识别号：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="taxpayerCode" class='form-control' style="float: left;">
									<label style="line-height: 34px;">
										<input class="i-checks isTaxpayerEvent" type="checkbox" onclick="isTaxpayer(this)">无<i class="residentEach"></i>
									</label>
								</div>
							</td>
						</tr>
						<tr style="display: none;">
							<td><font color="red">*</font>无识别号的原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="notCodeCause" class='form-control select2_width' onchange="notCodeCauseEvent(this)">
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
									<select class='form-control select_addr1' name="taxNationality" onchange="changeNation(this)">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="taxArea" class='form-control select_addr2'><option value="">--</option></select>
									<span class="addResident" onclick="addResident();">+</span>
								</div>
							</td>
						</tr>
						<tr class="since" style="display: none;">
							<td><font color="red">*</font>纳税人识别号：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="taxpayerCode" class="form-control" style="float: left;">
									<label style="line-height: 34px;">
										<input name="isTaxpayerEvent" class="i-checks isTaxpayerEvent" type="checkbox" onclick="isTaxpayer(this)">无<i class="residentEach"></i>
									</label>
								</div>
							</td>
						</tr>
						<tr class="since sinceNotCodeCause" style="display: none;">
							<td><font color="red">*</font>无识别号的原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="notCodeCause" class='form-control select2_width' onchange="notCodeCauseEvent(this);">
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
				
				<table class="table table-bordered maintable" id="controllerInfo">
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
									<input type="text" class='form-control' name="ChineseName2" id="ChineseName2">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人英文姓：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="EnglishFamliyName3" id="EnglishFamliyName3">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人英文名：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="EnglishFirstName3" id="EnglishFirstName3">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人类型</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="ControllerType" class='form-control select2_width'>
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
									<input type="text" class='form-control' name="ConNonResiFlag" readonly="readonly" id="ConNonResiFlag">
								</div>
							</td>
						</tr>
						<tr>
							<td>控制人持股比例：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="ConShareRatio" id="ConShareRatio">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人现居国家：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="LivingCountry2" class='form-control select_addr1' onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="LivingCountry21" class='form-control select_addr2'><option value="">--</option></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人现居地址：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="LivingAddress5" id="LivingAddress5">
								</div>		
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人现居地址英文：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="LivingAddress7" id="LivingAddress7">
								</div>		
							</td>
						</tr>
						<tr>
							<td>控制人国籍：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="RegRegionCode2" class='form-control select_addr1' onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="RegRegionCode21" class='form-control select_addr2'><option value="">--</option></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人出生日期：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="BirthDate2" id="BirthDate2"/>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人出生国家：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="BirthCountry2" class='form-control select_addr1' onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="BirthCountry21" class='form-control select_addr2'><option value="">--</option></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人出生城市英文：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="BirthCity2" id="BirthCity2">
								</div>		
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人税收居民国</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="TaxCountry2" class='form-control select_addr1' onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="TaxCountry21" class='form-control select_addr2'><option value="">--</option></select>
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
									</label>
								</div>
							</td>
						</tr>
						<tr style="display: none;">
							<td><font color="red">*</font>无识别号的原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="notCodeCause2" class='form-control select2_width' onchange="notCodeCauseEvent(this);">
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
				<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提&nbsp;交" name="btnSubmit">提&nbsp;交</button>
				<button id="btnBack" name="btnBack" type="button" class="btn btn-link" data-dismiss="modal" value="重置">重置</button>
				<button id="reset" name="reset" type="reset" class="btn btn-link" style="display: none;" value="重置">重置</button>
				<button id="closeBtn" name="claseBtn" type="button" class="btn btn-link" value="取&nbsp;消" onclick="doBack()">取&nbsp;消</button>
			</div>
		</div>
		</form>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/custAccountMgr/accountRejectChange.js?v="<%=dateStr%>></script>
</body>	
</html>