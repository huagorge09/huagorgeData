<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>账户类复核</title>
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
		<input id="pslInvIdtp" 	 type="hidden" value="${ dto.idtp }"/>
		<input id="pslInvIdno" 	 type="hidden" value="${ dto.idno }"/>
		<input id="invtp" 	 	 type="hidden" value="${ dto.invtp }"/>
		<input id="invnm" 	 	 type="hidden" value="${ dto.invnm }"/>
		<input id="invprtpVal" value="${dto.invprtp }" type="hidden" />
		<input id="accountInfo"	 type="hidden" value='${ userTaxArray }'/>
		<input id="documentlist" value='${dto.documentlist }' type="hidden" />
		<input id="serialno" value='${serialno }' type="hidden" />
		
		<select id="comNation" name="comNation" style="display: none;">
			<c:forEach var="item" items="${nationArray}">
				<c:if test="${item.PMCO != '156'}">
					<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
				</c:if>
			</c:forEach>
		</select>
		
		<div class="page-header" style="margin: 0;">
			<h4 class="page-title">账户类复核</h4>
		</div>
	 	<form name="form" id="form" method="post" action="" >
		<div class="page-body">
			<!-- 个人信息 -->
			<div id="baseInfoContent" class="typeContent">
			<c:if test="${dto.invtp == '1' }">
				<table id="tabPslBaseInfo" class="table table-bordered">
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
					<tbody>
						<tr>
							<th rowspan="3">证件信息</th>
							<td>投资者名称：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pinvnm" name="pinvnm">${dto.invnm }</span>
								</span>
							</td>
							<td>注册登记证件类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pidtp" name="pidtp">${dto.idtpnm }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>注册登记证件号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pidno" name="pidno">${dto.idno }</span>
								</span>
							</td>
							<td>注册登记证件有效期：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pidvalidate" name="pidvalidate">${dto.idvalidate }</span>
								</span>
							</td>
						</tr>
					</tbody>
					<tbody>
						<tr>
							<th rowspan="3">银行信息</th>
							<td>开户银行：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pbnkNO" name="pbnkNO">${dto.bnkNo }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>预留银行全称：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="popenName" name="popenName">${dto.openName }</span>
								</span>
							</td>
							<td>预留银行开户地：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="popenAddr" name="popenAddr">${dto.openAddr } - ${dto.openBankCity }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>预留银行户名：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pbankaccoNm" name="pbankaccoNm">${dto.bankAccoNm }</span>
								</span>
							</td>
							<td>预留银行账号：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pbankAcco" name="pbankAcco">${dto.bankAcco }</span>
								</span>
							</td>
						</tr>
					</tbody>
					<tbody>
						<tr>
							<th rowspan="15">其他信息</th>
							<td>客户简称：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pacctabbr" name="pacctabbr">${dto.acctabbr } - ${dto.instrepcode }</span>
								</span>
							</td>
							<td>投资者类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pinvprtp" name="pinvprtp">${dto.invprtpnm }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>业务类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pbusinesstp" name="pbusinesstp">${dto.businesstp }</span>
								</span>
							</td>
							<td>公司类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pcompanytp" name="pcompanytp">${dto.companytp }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>地域类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pregiontp" name="pregiontp">${dto.regiontp }</span>
								</span>
							</td>
							<td>反洗钱类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pamlrisktype" name="pamlrisktype">${dto.amlrisktype }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>反洗钱备注：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pfxqremark" name="pfxqremark">${dto.fxqremark }</span>
								</span>
							</td>
							<td>近三年年均收入：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="threeAnnualIncome" name="threeAnnualIncome">${dto.threeAnnualIncome }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>税收居民身份：</td>
							<td class="white-bg" colspan="3">
								<span class="col-sm-12">
									<span id="taxType" name="taxType">${dto.taxType }</span>
									<input type="hidden" id="ptaxType" value="${dto.taxTypeDecl}">
								</span>
							</td>
						</tr>
						<tr>
							<td>金融资产：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="financialAsset" name="financialAsset">${dto.financialAsset }</span>
								</span>
							</td>
							<td>投资经历：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="indInvExperience" name="indInvExperience">${dto.indInvExperience }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>相关工作经历：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="relatedWorkExp" name="relatedWorkExp">${dto.relatedWorkExp }</span>
								</span>
							</td>
							<td>金融职业：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="finProfessions" name="finProfessions">${dto.finProfessions }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>性别：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="psex" name="psex">${dto.sex }</span>
								</span>
							</td>
							<td>国籍：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pnationalitycode" name="pnationalitycode">${dto.nationalitycode }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>学历：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pedlevel" name="pedlevel">${dto.edlevel }</span>
								</span>
							</td>
							<td>职业：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pvoccode" name="pvoccode">${dto.voccode }</span>
								</span>
							</td>
						</tr>
						<c:if test="${dto.invprtp != '0' }">
						<tr>
							<td>年收入：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pincome" name="pincome">${dto.income }</span>
								</span>
							</td>
							<td>风险承受能力：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<c:if test="${dto.specriskLevel != '1' }">
										<span id="pcustrisklevl" name="pcustrisklevl">${dto.custrisklevlnm }</span>
									</c:if>
									<c:if test="${dto.specriskLevel == '1' }">
										<span id="pcustrisklevl" name="pcustrisklevl">C0-最低</span>
									</c:if>
								</span>
							</td>
						</tr>
						</c:if>
						<tr>
							<td>办公电话：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ptel" name="ptel">${dto.tel }</span>
								</span>
							</td>
							<td>住宅电话：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="phousetel" name="phousetel">${dto.housetel }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>移动电话：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pmobile" name="pmobile">${dto.mobile }</span>
								</span>
							</td>
							<td>传真号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pfax" name="pfax">${dto.fax }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>传真委托：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pfaxdelegate" name="pfaxdelegate">${dto.faxdelegate }</span>
								</span>
							</td>
							<td>电子邮件：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pemail" name="pemail">${dto.email }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>通讯地址:</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="paddr" name="paddr">${dto.addr }</span>
								</span>
							</td>
							<td>邮政编码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ppostcode" name="ppostcode">${dto.postcode }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>上交所股东代码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pslShsecacc" name="pslShsecacc">${dto.shsecacc }</span>
								</span>
							</td>
							<td>深交所股东代码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="pslSzsecacc" name="pslSzsecacc">${dto.szsecacc }</span>
								</span>
							</td>
						</tr>
					</tbody>
				</table>
			</c:if>
			</div>
			
			<!-- 机构信息 -->
			<div id="orgBaseInfoContent" class="typeContent">
				<c:if test="${dto.invtp == '0' }">
				<table id="tabOrgBaseInfo" class="table table-bordered">
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
					<tbody>
						<tr>
							<th rowspan="3">证件信息</th>
							<td>投资者名称：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oinvnm" name="oinvnm">${dto.invnm }</span>
								</span>
							</td>
							<td>注册登记证件类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oidtp" name="oidtp">${dto.idtpnm }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>注册登记证件号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oidno" name="oidno">${dto.idno }</span>
								</span>
							</td>
							<td>注册登记证件有效期：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oidvalidate" name="oidvalidate">${dto.idvalidate }</span>
								</span>
							</td>
						</tr>
					</tbody>
					<tbody>
						<tr>
							<th rowspan="3">银行信息</th>
							<td>开户银行：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="obnkNO" name="obnkNO">${dto.bnkNo }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>预留银行全称：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oopenName" name="oopenName">${dto.openName }</span>
								</span>
							</td>
							<td>预留银行开户地：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oopenAddr" name="oopenAddr">${dto.openAddr } - ${dto.openBankCity }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>预留银行户名：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="obankaccoNm" name="obankaccoNm">${dto.bankAccoNm }</span>
								</span>
							</td>
							<td>预留银行账号：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="obankAcco" name="obankAcco">${dto.bankAcco }</span>
								</span>
							</td>
						</tr>
					</tbody>
					
					<tbody>
						<tr>
							<th rowspan="3">法人信息</th>
							<td>法定代表人姓名：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oinstrepnm" name="oinstrepnm">${dto.instrepnm }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>法定代表人国籍：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oinstrepnation" name="oinstrepnation">${dto.instrepnation }</span>
								</span>
							</td>
							<td>法定代表人证件类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oinstrepidtp" name="oinstrepidtp">${dto.instrepidtp }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>法定代表人证件号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oinstrepidno" name="oinstrepidno">${dto.instrepidno }</span>
								</span>
							</td>
							<td>法定代表人证件有效期：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oinstrepvalidate" name="oinstrepvalidate">${dto.instrepvalidate }</span>
								</span>
							</td>
						</tr>
					</tbody>
					
					<tbody>
						<tr>
							<th rowspan="3">机构负责人信息</th>
							<td>机构负责人姓名：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oprincipalname" name="oprincipalname">${dto.principalname }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>机构负责人国籍：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oprincipalnation" name="oprincipalnation">${dto.principalnation }</span>
								</span>
							</td>
							<td>机构负责人证件类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oprincipalidtp" name="oprincipalidtp">${dto.principalidtp }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>机构负责人证件号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oprincipalidno" name="oprincipalidno">${dto.principalidno }</span>
								</span>
							</td>
							<td>机构负责人证件有效期：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oprincipalvalidt" name="oprincipalvalidt">${dto.principalvalidt }</span>
								</span>
							</td>
						</tr>
					</tbody>
					
					<tbody>
						<tr>
							<th rowspan="6">经办人员信息</th>
							<td>经办人授权范围：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontactgrant" name="ocontactgrant">${dto.contactgrant }</span>
								</span>
							</td>
							<td>经办人姓名：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontact" name="ocontact">${dto.contact }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人国籍：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontactnation" name="ocontactnation">${dto.contactnation }</span>
								</span>
							</td>
							<td>经办人证件类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontidtp" name="ocontidtp">${dto.contidtp }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人证件号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontidno" name="ocontidno">${dto.contidno }</span>
								</span>
							</td>
							<td>经办人证件有效期：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontvalidate" name="ocontvalidate">${dto.contvalidate }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人办公电话：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontphone" name="ocontphone">${dto.contphone }</span>
								</span>
							</td>
							<td>经办人传真号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontfax" name="ocontfax">${dto.contfax }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人手机号：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontmobile" name="ocontmobile">${dto.contmobile }</span>
								</span>
							</td>
							<td>经办人电子邮件：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontemail" name="ocontemail">${dto.contemail }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人联系地址：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontAddr" name="ocontAddr">${dto.contAddr }</span>
								</span>
							</td>
							<td>经办人邮政编码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocontPostcode" name="ocontPostcode">${dto.contPostcode }</span>
								</span>
							</td>
						</tr>
					</tbody>
					<tbody>
						<tr>
							<th rowspan="999">其他经办人员信息</th>
						<c:forEach var="item" items="${dto.ocontactlist}">
							<td>经办人授权范围：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontactgrant">${item.contactgrant }</span>
								</span>
							</td>
							<td>经办人姓名：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontact">${item.contact }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人国籍：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontactnation">${item.contactnation }</span>
								</span>
							</td>
							<td>经办人证件类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontidtp">${item.contidtp }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人证件号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontidno">${item.contidno }</span>
								</span>
							</td>
							<td>经办人证件有效期：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontvalidate">${item.contvalidate }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人办公电话：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontphone">${item.contphone }</span>
								</span>
							</td>
							<td>经办人传真号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontfax">${item.contfax }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人手机号：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontmobile">${item.contmobile }</span>
								</span>
							</td>
							<td>经办人电子邮件：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontemail">${item.contemail }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人联系地址：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontAddr">${item.contAddr }</span>
								</span>
							</td>
							<td>经办人邮政编码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontPostcode">${item.contPostcode }</span>
								</span>
							</td>
						</tr>
						</c:forEach>
					</tbody>
					
					<tbody>
						<tr>
							<th rowspan="15">其他信息</th>
							<td>客户简称：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oacctabbr" name="oacctabbr">${dto.acctabbr } - ${dto.instrepcode }</span>
								</span>
							</td>
							<td>投资者类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oinvprtp" name="oinvprtp">${dto.invprtpnm }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>业务类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="obusinesstp" name="obusinesstp">${dto.businesstp }</span>
								</span>
							</td>
							<td>公司类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ocompanytp" name="ocompanytp">${dto.companytp }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>地域类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oregiontp" name="oregiontp">${dto.regiontp }</span>
								</span>
							</td>
							<td>反洗钱类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oamlrisktype" name="oamlrisktype">${dto.amlrisktype }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>反洗钱备注：</td>
							<td class="white-bg" colspan="3">
								<span class="col-sm-12">
									<span id="ofxqremark" name="ofxqremark">${dto.fxqremark }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>税收居民身份：</td>
							<td class="white-bg" colspan="3">
								<span class="col-sm-12">
									<span id="taxType" name="taxType">${dto.taxType }</span>
									<input type="hidden" id="ptaxType" value="${dto.taxTypeDecl}">
								</span>
							</td>
						</tr>
						<tr>
							<td>机构类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="investProInstType" name="investProInstType">${dto.investProInstType } - ${dto.investProInstSecond }</span>
								</span>
							</td>
							<td>投资经历：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="investExperience" name="investExperience">${dto.investExperience }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>近1年末净资产：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oneYearEndNetAsset" name="oneYearEndNetAsset">${dto.oneYearEndNetAsset }</span>
								</span>
							</td>
							<td>近1年末金融资产：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oneYearEndFinAsset" name="oneYearEndFinAsset">${dto.oneYearEndFinAsset }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>消极非金融机构：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<c:if test="${dto.negativeNotFinaInst == '1' }">
										<span id="oneYearEndFinAsset" name="oneYearEndFinAsset">居民消极非金融机构</span>
									</c:if>
									<c:if test="${dto.negativeNotFinaInst == '2' }">
										<span id="oneYearEndFinAsset" name="oneYearEndFinAsset">非居民消极非金融机构</span>
									</c:if>
									<c:if test="${dto.negativeNotFinaInst == '3' }">
										<span id="oneYearEndFinAsset" name="oneYearEndFinAsset">其它机构</span>
									</c:if>
								</span>
							</td>
							<td>存在非居民控制人标识：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<c:if test="${dto.controlPerTaxDecl == '1' }">
										<span id="controlPerTaxDecl" name="controlPerTaxDecl">是</span>
									</c:if>
									<c:if test="${dto.controlPerTaxDecl == '0' }">
										<span id="controlPerTaxDecl" name="controlPerTaxDecl">否</span>
									</c:if>
									
									<input type="hidden" id="controlPerTaxDeclStr" value="${dto.controlPerTaxDecl }">
								</span>
							</td>
						</tr>
						<tr>
							<td>办公电话：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oofficeTel" name="oofficeTel">${dto.tel }</span>
								</span>
							</td>
							<td>传真号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="ofax" name="ofax">${dto.fax }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>办公地址：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oaddr" name="oaddr">${dto.addr }</span>
								</span>
							</td>
							<td>邮政编码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="opostcode" name="opostcode">${dto.postcode }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>控股股东名称：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oholdingname" name="oholdingname">${dto.holdingname }</span>
								</span>
							</td>
							<td>控股股东证件类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oholdingidtp" name="oholdingidtp">${dto.holdingidtp }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>控股股东证件号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oholdingidno" name="oholdingidno">${dto.holdingidno }</span>
								</span>
							</td>
							<td>控股股东证件有效期：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="oholdingvalidate" name="oholdingvalidate">${dto.holdingvalidate }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>基金投资受益人名称：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="obeneficiary" name="obeneficiary">${dto.beneficiary }</span>
								</span>
							</td>
							<c:if test="${dto.invprtp != '0' }">
							<td>客户风险承受能力：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<c:if test="${dto.specriskLevel != '1' }">
										<span id="ocustrisklevl" name="ocustrisklevl">${dto.custrisklevlnm }</span>
									</c:if>
									<c:if test="${dto.specriskLevel == '1' }">
										<span id="ocustrisklevl" name="ocustrisklevl">C0-最低</span>
									</c:if>
								</span>
							</td>
							</c:if>
						</tr>
						<tr>
							<td>上交所股东代码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="orgShsecacc" name="orgShsecacc">${dto.shsecacc }</span>
								</span>
							</td>
							<td>深交所股东代码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="orgSzsecacc" name="orgSzsecacc">${dto.szsecacc }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>机构类型：</td>
							<td class="white-bg" colspan="3">
								<span class="col-sm-12">
									<span id="organType" name="organType">${dto.organType }</span>
								</span>
							</td>
						</tr>
					</tbody>
				</table>
				</c:if>
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
								<span class="col-sm-12">
									<input type="hidden" id="docbusinesstpVal" value="${dto.docbusinesstp }">
									<span id="docbusinesstp" name="docbusinesstp">${dto.docbusinesstpnm }</span>
								</span>
							</td>
							<td>客户资料是否齐全：</td>
							<td class="white-bg">
								<input type="hidden" id="isalldoc" value="${dto.ifalldocument }">
								<span class="col-sm-12">
									<c:if test="${dto.ifalldocument == '0' }">
										<span id="" name="isalldoc">否</span>
									</c:if>
									<c:if test="${dto.ifalldocument == '1' }">
										<span id="" name="isalldoc">是</span>
									</c:if>
								</span>
							</td>
						</tr>
						<tr>
							<td>客户资料是否原件：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<c:if test="${dto.ifOriginal == '0' }">
										<span id="isoriginal" name="isoriginal">否</span>
									</c:if>
									<c:if test="${dto.ifOriginal == '1' }">
										<span id="isoriginal" name="isoriginal">是</span>
									</c:if>
								</span>
							</td>
							<td>客户资料是否归档：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<c:if test="${dto.ifsaved == '0' }">
										<span id="issaved" name="issaved">否</span>
									</c:if>
									<c:if test="${dto.ifsaved == '1' }">
										<span id="issaved" name="issaved">是</span>
									</c:if>
								</span>
							</td>
						</tr>
						<tr>
							<td>是否上传附件：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<c:if test="${dto.isupload == '0' }">
										<span id="isupload" name="isupload">否</span>
									</c:if>
									<c:if test="${dto.isupload == '1' }">
										<span id="isupload" name="isupload">是</span>
									</c:if>
								</span>
							</td>
							<td>文件编号：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="fileno" name="fileno">${dto.fileno }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>是否扫描：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<c:if test="${dto.isscan == 'N' }">
										<span id="isscan" name="isscan">否</span>
									</c:if>
									<c:if test="${dto.isscan == 'Y' }">
										<span id="isscan" name="isscan">是</span>
									</c:if>
								</span>
							</td>
							<td>存档位置：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="keepaddress" name="keepaddress">${dto.keepaddress }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>所属客户经理：</td>
							<td class="white-bg" colspan="3">
								<span class="col-sm-12">
									<span id="salesaccmanager" name="salesaccmanager">${dto.salesaccmanager }</span>
								</span>
							</td>
						</tr>
						<tr id="documentinfo">
							<td id="contractListTh">资料列表：</td>
						</tr>
						<tr>
							<td>资料信息备注：</td>
							<td class="white-bg" colspan="3">
								<span class="col-sm-12">
									<span id="remarkinfo" name="remarkinfo">${dto.remarkinfo }</span>
								</span>
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
									<select name="birth_region" id="birth_region" class='form-control select_addr2' style="float: left;" ></select>
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
									<select class='form-control select_addr2' name="reside_region"></select>
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
									<select name="taxArea" class='form-control select_addr2'></select>
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
									<select name="taxArea" class='form-control select_addr2'></select>
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
									<select name="LivingCountry21" class='form-control select_addr2'></select>
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
									<select name="RegRegionCode21" class='form-control select_addr2'></select>
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
									<select name="BirthCountry21" class='form-control select_addr2'></select>
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
									<select name="TaxCountry21" class='form-control select_addr2'></select>
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
				<button id="doY" type="button" class="btn btn-primary btn-save" value="提交" name="doY" onclick="doSubmit('Y')">复核通过</button>
				<button id="doR" type="button" class="btn btn-primary btn-save" value="提交" name="doR" onclick="doSubmit('R')">复核驳回</button>
				<button id="doC" type="button" class="btn btn-primary btn-save" value="提交" name="doC" onclick="doSubmit('C')">复核作废</button>
				<button id="closeBtn" name="claseBtn" type="button" class="btn btn-link" value="取消"   onclick="doBack()">取消</button>
			</div>
		</div>
		</form>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/custAccountMgr/checkAccountDetail.js?v="<%=dateStr%>></script>
</body>	
</html>