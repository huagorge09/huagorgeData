<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>综合资料修改</title>
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
.modal-open .modal{
	top:150px !important;
	height: auto !important;
}
.ui-jqgrid-hdiv{
	overflow: hidden;
}
</style>
<script type="text/javascript">
var projectPath = '<%=context%>';
var widgetPath = '<%=context%>service/widget/';
var accountPath = '<%=context%>service/custInfoManager/';
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
		<input id="custno" 	 type="hidden" value="${ custno }"/>
		<input id="invnm" 	 type="hidden" value="${ dto.invnm }"/>
		<input id="idno" 	 type="hidden" value="${ dto.idno }"/>
		<input id="invtp" 	 type="hidden" value="${ invtp }"/>
		<input id="specriskLevel" value="${dto.specriskLevel }" type="hidden" />
		
		<input id="selOpenType" type="hidden" value="${dto.invtp }" />
		<input id="custnos" type="hidden" value="${custno }" />
		<input class="custsimpnm" type="hidden" value="${dto.acctabbr }" />
		<input type="hidden" id="preRisklevel" value="${dto.custrisklevl }">
		<input id="invprtpVal" value="${dto.invprtp }" type="hidden" />
		<input id="oidlist" value='${dto.oidlist }' type="hidden" />
		<input id="ocontactlist" value='${dto.ocontactlist }' type="hidden" />
		<input id="documentlist" value='${dto.documentlist }' type="hidden" />
		<input type=hidden id="tradeaccos" name="tradeaccos" value="${ tradeAcco }"/>
		
		<div class="page-header" style="margin: 0;">
			<h4 class="page-title">综合资料修改</h4>
			<select id="comNation" name="comNation" style="display: none;">
				<c:forEach var="item" items="${nationArray}">
					<c:if test="${item.PMCO != '156'}">
						<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
					</c:if>
				</c:forEach>
			</select>
		</div>
		<div class="clearfix">
		    <ul class="nav nav-tabs" role="tablist">
		   		<li role="presentation" class="active">
		        	<a href="#baseInfo_show_content" onClick="showTypeContent('baseInfoContent','btnBaseInfo')" id="baseInfo_show_content-tab btnBaseInfo" data-toggle="tab" 
		        		aria-controls="baseInfo_show_content">基本信息</a>
		        </li>
		        <li role="presentation">
		        	<a href="#extInfo_show_content" onclick="showTypeContent('extInfoContent','btnExtInfo')" id="extInfo_show_content-tab btnExtInfo" data-toggle="tab" 
		        		aria-controls="extInfo_show_content" aria-expanded="true" role="tab" >附加信息</a>
		        </li>
		        <li role="presentation">
		        	<a href="#documentInfo_show_content" onclick="showTypeContent('documentInfoContent','btnDocumentInfo')" id="documentInfo_show_content-tab btnDocumentInfo" data-toggle="tab" 
		        		aria-controls="documentInfo_show_content" aria-expanded="true" role="tab" >资料信息</a>
		        </li>
		        <li role="presentation">
		        	<a href="#bankInfo_show_content" onClick="showTypeContent('bankInfoContent','btnBankInfo')" id="baseInfo_show_content-tab btnBankInfo" data-toggle="tab" 
		        		aria-controls="bankInfo_show_content">银行信息</a>
		        </li>
		        <li role="presentation">
		        	<a href="#cotegoryInfo_show_content" onclick="showTypeContent('cotegoryInfoContent','btnCotegoryInfo')" id="documentInfo_show_content-tab btnCotegoryInfo" data-toggle="tab" 
		        		aria-controls="cotegoryInfo_show_content" aria-expanded="true" role="tab" >分类信息</a>
		        </li>
		    </ul>
	 	</div><br>
	 	<form name="openCustomFrom" id="openCustomFrom" method="post" action="" >
		<div class="page-body">
			<!-- 基本信息 -->
			<div id="baseInfoContent" class="typeContent">
				<table class="table table-bordered" id="tabPslBaseInfo" style="display: none;">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody>
						<tr>
							<td>委托方式：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="plstrustType" id="plstrustType" class='form-control select2_width'>
										<c:forEach var="item" items="${trustTypeArray}">
											<option value="${item.PMCO}" ${item.PMCO == '3' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>修改信息列表：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-10 form-inner updateList">
									<span id="pchidinfodiv">
										<label><input class="i-checks" type="checkbox" id="pchidinfo" name="pchidinfo" >证件信息</label>
									</span>
										<label><input class="i-checks" type="checkbox" id="pchotherinfo" name="" >其他信息</label><br>
								</div>
							</td>
						</tr>
					</tbody>
					<tbody id="pidinfo" style="display:none;">
						<tr>
							<th rowspan="2">证件信息</th>
							<td><font color="red">*</font>投资者名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pslInvNm' id='pslInvNm' value="${dto.invnm }"/>
								</div>
							</td>
							<td><font color="red">*</font>注册登记证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="pslInvIdtp" id="pslInvIdtp" class='form-control select2_width'>
										<c:forEach var="item" items="${idtpArray}">
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
									<input type='text' maxlength="30" class='form-control' name='pslInvIdno' id='pslInvIdno' value="${dto.idno }"/>
								</div>
							</td>
							<td><font color="red">*</font>注册登记证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='pslInvIdValidate' id='pslInvIdValidate'  value="${dto.idvalidate }"/>
									<label style="line-height: 34px;">
										<input class="i-checks" id="changepslInvIdValidate" type="checkbox"
										   onclick="changetime('changepslInvIdValidate','pslInvIdValidate');">长期
									</label><br>
								</div>
							</td>
						</tr>
					</tbody>
					<tbody id="potherinfo" style="display:none;">
						<tr>
							<td rowspan="5">其他信息</td>
							<td>办公电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pslInvOfficeTel' id='pslInvOfficeTel'  value="${dto.tel }"/>
								</div>
							</td>
							<td>住宅电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pslInvHomeTel' id='pslInvHomeTel'  value="${dto.housetel }"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>移动电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pslInvMobile' id='pslInvMobile'  value="${ dto.mobile}"/>
								</div>
							</td>
							<td>传真号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pslInvFax' id='pslInvFax'  value="${dto.fax }"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>传真委托：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="plsFaxDelegate" id="plsFaxDelegate" class='form-control select2_width'>
										<option value="1" ${dto.faxdelegate == 1 ? "selected" : "" }>是</option>
										<option value="0" ${dto.faxdelegate == 0 ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
							<td>电子邮件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pslInvEmail' id='pslInvEmail'  value="${dto.email }"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>通讯地址：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='plsAddr' id='plsAddr'  value="${dto.addr }"/>
								</div>
							</td>
							<td><font color="red">*</font>邮政编码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='plsPostCode' id='plsPostCode' maxlength="6" value="${dto.postcode }"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>上交所股东代码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pslShsecacc' id='pslShsecacc' maxlength="10" value="${dto.shsecacc }"/>
								</div>
							</td>
							<td>深交所股东代码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='pslSzsecacc' id='pslSzsecacc' maxlength="10" value="${dto.szsecacc }"/>
								</div>
							</td>
						</tr>
					</tbody>
					
				</table>
				
				<table class="table table-bordered" id="tabOrgBaseInfo" style="display: none;">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody>
						<tr>
							<td>委托方式：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="orgtrustType" id="orgtrustType" class='form-control select2_width'>
										<c:forEach var="item" items="${trustTypeArray}">
											<option value="${item.PMCO}" ${item.PMCO == '3' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>修改信息列表：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-10 form-inner updateList">
									<span id="orgchidinfodiv">
										<label><input class="i-checks" type="checkbox" id="orgchidinfo" name="orgchidinfo" >证件信息</label>
									</span>
										<label><input class="i-checks" type="checkbox" id="orgchinstrepinfo">法人信息</label>
										<label><input class="i-checks" type="checkbox" id="orgchprincipalinfo">机构负责人信息</label>
										<label><input class="i-checks" type="checkbox" id="orgchcontinfo">经办人信息</label>
										<label><input class="i-checks" type="checkbox" id="orgchotherinfo">其他信息</label>
								</div>
							</td>
						</tr>
					</tbody>
					<tbody id="orgidinfo" style="display:none;">
						<tr>
							<th rowspan="2">证件信息</th>
							<td><font color="red">*</font>投资者名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgInvNm' id='orgInvNm' onchange="queryOpenUserInfo();" value="${dto.invnm }"/>
								</div>
							</td>
							<td><font color="red">*</font>注册登记证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgInvIdtp" id="orgInvIdtp" class='form-control select2_width'>
										<c:forEach var="item" items="${seatidtpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.idtp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								
								<!-- 隐藏，用于添加其他证件信息时使用 -->
								<div class="orgInvIdtpDiv" style="display: none;">
									<div class="col-sm-11 form-inner" style="line-height: 32px;">
										<select name="orgInvIdtp" id="ooidtp" class='form-control orgInvIdtp'>
											<c:forEach var="item" items="${seatidtpArray}">
												<option value="${item.PMCO}" ${item.PMCO == '156' ?"selected": "" } >${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
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
									<input type='text' maxlength="30" class='form-control' name='orgInvIdno' id='orgInvIdno' value="${dto.idno }"/>
								</div>
							</td>
							<td><font color="red">*</font>注册登记证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgInvIdvalidate' id='orgInvIdvalidate' value="${dto.idvalidate }" />
									<label style="line-height: 34px;">
										<input class="i-checks" id="changeorgInvIdvalidate" type="checkbox"
										   onclick="changetime('changeorgInvIdvalidate','orgInvIdvalidate');">长期
									</label><br>
								</div>
							</td>
						</tr>
					</tbody>
					<tbody id="orginstrepinfo" style="display:none;">
						<tr>
							<td rowspan="3">法人信息</td>
							<td><font color="red">*</font>法定代表人姓名：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgInstrepnm' id='orgInstrepnm' value="${dto.instrepnm }"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>法定代表人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgInstrepnation" id="orgInstrepnation" class='form-control select2_width'>
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.instrepnation ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td><font color="red">*</font>法定代表人证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgInstrepidtp" id="orgInstrepidtp" class='form-control select2_width'>
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
									<input type='text' maxlength="18" class='form-control' name='orgInstrepidno' id='orgInstrepidno' value="${dto.instrepidno }"/>
								</div>
							</td>
							<td><font color="red">*</font>法定代表人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgInstrepidvalidate' id='orgInstrepidvalidate' value="${dto.instrepvalidate }" />
									<label style="line-height: 34px;">
										<input class="i-checks" id="changeorgInstrepidvalidate" type="checkbox"  
											   onclick="changetime('changeorgInstrepidvalidate','orgInstrepidvalidate');">长期
									</label><br>
								</div>
							</td>
						</tr>
					</tbody>
					<tbody id="orgprincipalinfo" style="display:none;">
						<tr>
							<td rowspan="3">机构负责人信息</td>
							<td><font color="red">*</font>机构负责人姓名：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgPrincipalname' id='orgPrincipalname' value="${dto.principalname }"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>机构负责人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgPrincipalnation" id="orgPrincipalnation" class='form-control select2_width'>
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.principalnation ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td><font color="red">*</font>机构负责人证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgPrincipalidtp" id="orgPrincipalidtp" class='form-control select2_width'>
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
									<input type='text' maxlength="18" class='form-control' name='orgPrincipalidno' id='orgPrincipalidno' value="${dto.principalidno }"/>
								</div>
							</td>
							<td><font color="red">*</font>机构负责人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgPrincipalidvalidate' id='orgPrincipalidvalidate' value="${dto.principalvalidt }" />
									<label style="line-height: 34px;">
										<input class="i-checks" id="changeorgPrincipalidvalidate" type="checkbox" 
											   onclick="changetime('changeorgPrincipalidvalidate','orgPrincipalidvalidate');"/>长期
									</label><br>
								</div>
							</td>
						</tr>
					</tbody>
					<tbody id="orgcontinfo" style="display:none;">
						<tr>
							<td rowspan="6">经办人员信息</td>
							<td><font color="red">*</font>经办人授权范围：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgContactright" id="orgContactright" class='form-control select2_width'>
										<c:forEach var="item" items="${bokerprivArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.contactgrant ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								<div style="display: none;" class="orgContactrightDiv">
									<div class="col-sm-11 form-inner">
										<select name="orgContactright" id="oocontactgrant" class='form-control orgContactright'>
											<c:forEach var="item" items="${bokerprivArray}">
												<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</td>
							<td><font color="red">*</font>经办人姓名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="18" class='form-control' name='orgContnm' id='orgContnm' value="${dto.contact }"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>经办人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgContnation" id="orgContnation" class='form-control select2_width'>
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
									<select name="orgContidtp" id="orgContidtp" class='form-control select2_width'>
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
									<input type='text' maxlength="18" class='form-control' name='orgContidno' id='orgContidno' value="${dto.contidno }" />
								</div>
							</td>
							<td><font color="red">*</font>经办人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgContidvalidate' id='orgContidvalidate' value="${dto.contvalidate }"/>
									<label style="line-height: 34px;">
										<input class="i-checks" id="changeorgContidvalidate" type="checkbox" 
											   onclick="changetime('changeorgContidvalidate','orgContidvalidate');"/>长期
									</label><br>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>经办人办公电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContphone' id='orgContphone' value="${dto.contphone }" />
								</div>
							</td>
							<td><font color="red">*</font>经办人传真号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContfax' id='orgContfax' value="${dto.contfax }" />
								</div>
							</td>
						</tr>
						
						<tr>
							<td>经办人手机号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContmobile' id='orgContmobile' value="${dto.contmobile }" />
								</div>
							</td>
							<td>经办人电子邮件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContemail' id='orgContemail' value="${dto.contemail }" />
								</div>
							</td>
						</tr>
						
						<tr>
							<td>经办人通讯地址：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContAddr' id='orgContAddr' value="${dto.contAddr }" />
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
					<tbody id="orgotherinfo" style="display:none;">
						<tr>
							<td rowspan="4">其他信息</td>
							<td>办公电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgInvOfficeTel' id='orgInvOfficeTel' value="${dto.tel }" />
								</div>
							</td>
							<td>传真号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgInvFax' id='orgInvFax' value="${dto.fax }" />
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>通讯地址：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgAddr' id='orgAddr' value="${dto.addr }" />
								</div>
							</td>
							<td><font color="red">*</font>邮政编码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgPostcode' id='orgPostcode' maxlength="6" value="${dto.postcode }"/>
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
											<option value="${item.PMCO}" ${item.PMCO == dto.organType ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<!-- 附加信息 -->
			<div id="extInfoContent" class="typeContent" style="display: none;">
				<table class="table table-bordered maintable" id="tabPslExtInfo">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody>
						<tr>
							<td rowspan="3" id="extInfoOtherTh">其他信息</td>
							<td>性别：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="plsSex" id="plsSex" class='form-control select2_width'>
										<c:forEach var="item" items="${sexArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.sex ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="plsInvNation" id="plsInvNation" class='form-control select2_width'>
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
									<select name="plsInvEducation" id="plsInvEducation" class='form-control select2_width'>
										<c:forEach var="item" items="${edlevelArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.edlevel ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>职业：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="plsInvJob" id="plsInvJob" class='form-control select2_width'>
										<c:forEach var="item" items="${vacodeArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.voccode ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<c:if test="${dto.invprtp != '0' }">
						<tr>
							<td>年收入：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="plsInvIncome" id="plsInvIncome" class='form-control select2_width'>
										<c:forEach var="item" items="${incomeArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.income ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>风险承受能力：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="plsInvRisk" id="plsInvRisk" class='form-control select2_width' style="float: left;">
										<c:forEach var="item" items="${custrisklevelArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.custrisklevl ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<label style="margin-top: 8px;">
										<input id="indSpecRiskLevel" class="i-checks" type="checkbox" name="indSpecRiskLevel" onclick="changeSpecRiskLevel(this,'plsInvRisk');" >最低
									</label>
								</div>
							</td>
						</tr>
						</c:if>
						<c:if test="${dto.invprtp == '0' }">
							<select name="plsInvIncome" id="plsInvIncome" class='form-control' style="display: none;">
								<c:forEach var="item" items="${incomeArray}">
									<option value="${item.PMCO}" ${item.PMCO == dto.income ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
								</c:forEach>
							</select>
							<select name="plsInvRisk" id="plsInvRisk" class='form-control' style="display: none;">
								<c:forEach var="item" items="${custrisklevelArray}">
									<option value="${item.PMCO}" ${item.PMCO == dto.custrisklevl ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
								</c:forEach>
							</select>
						</c:if>
					</tbody>
				</table>
				<table class="table table-bordered maintable" id="tabOrgExtInfo" style="display: none;">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody>
						<tr>
							<th rowspan="5">其他信息</th>
							<td>实际控制人或控股股东名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgHoldingname' id='orgHoldingname' value="${dto.holdingname }"/>
								</div>
							</td>
							<td>控股股东证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgHoldingidtp" id="orgHoldingidtp" class='form-control select2_width'>
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
									<input type='text' maxlength="30" class='form-control' name='orgHoldingidno' id='orgHoldingidno' value="${dto.holdingidno }"/>
								</div>
							</td>
							<td>控股股东证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgHoldingidvalidate' id='orgHoldingidvalidate'  value="${dto.holdingvalidate }"/>
									<label style="line-height: 34px;">
										<input class="i-checks" id="changeorgHoldingidvalidate" type="checkbox" 
											   onclick="changetime('changeorgHoldingidvalidate','orgHoldingidvalidate');"/>长期
									</label><br>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>基金投资受益人名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgBeneficiarynm' id='orgBeneficiarynm'  value="${dto.beneficiary }"/>
								</div>
							</td>
							<c:if test="${dto.invprtp != '0' }">
							<td>客户风险承受能力：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner" style="width: 100%;">
									<select name="orgRiskLevel" id="orgRiskLevel" class='form-control select2_width' style="float: left;">
										<c:forEach var="item" items="${custrisklevelArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.custrisklevl ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<label style="margin-top: 8px;">
										<input id="specRiskLevel" class="i-checks" type="checkbox" name="specRiskLevel" 
											   onclick="changeSpecRiskLevel(this,'orgRiskLevel');"
										/>最低
									</label>
								</div>
							</td>
							</c:if>
							<c:if test="${dto.invprtp == '0' }">
								<select name="orgRiskLevel" id="orgRiskLevel" class='form-control' style="display: none;">
									<c:forEach var="item" items="${custrisklevelArray}">
										<option value="${item.PMCO}" ${item.PMCO == dto.custrisklevl ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</c:if>
						</tr>
					</tbody>
					
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
					
				</table>
			</div>
			
			<!-- 资料信息 -->
			<div id="documentInfoContent" class="typeContent" style="display: none;">
				<table class="table table-bordered maintable" id="documentInfo">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					
					<tbody>
						<tr>
							<td rowspan="99">资料信息</td>
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
										<option value="1" ${dto.ifalldocument == '1' ? "selected" : "" }>是</option>
										<option value="0" ${dto.ifalldocument == '0' ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>客户资料是否原件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isoriginal" id="isoriginal" class='form-control select2_width'>
										<option value="1" ${dto.ifOriginal == '1' ? "selected" : "" }>是</option>
										<option value="0" ${dto.ifOriginal == '0' ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
							<td>客户资料是否归档：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="issaved" id="issaved" class='form-control select2_width'>
										<option value="1" ${dto.ifsaved == '1' ? "selected" : "" }>是</option>
										<option value="0" ${dto.ifsaved == '0' ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>是否上传附件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isupload" id="isupload" class='form-control select2_width'>
										<option value="1" ${dto.isupload == '1' ? "selected" : "" }>是</option>
										<option value="0" ${dto.isupload == '0' ? "selected" : "" }>否</option>
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
										<option value="Y" ${dto.isscan == 'Y' ? "selected" : "" }>是</option>
										<option value="N" ${dto.isscan == 'N' ? "selected" : "" }>否</option>
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
									<textarea rows="3" cols="50" class='form-control'  name="remarkinfo" id="remarkinfo"> ${dto.remarkinfo }</textarea>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<input type=hidden id="tano" name="tano" />
			</div>
			
			<!-- 银行信息 -->
			<div id="bankInfoContent" class="typeContent" style="display: none;">
				<c:if test="${isMultiple == '1' }">
				<table class="table table-bordered" id="querytable1">
					<colgroup>
					  	<col width="20%">
					  	<col width="30%">
					  	<col width="20%">
					  	<col width="30%">
					</colgroup>
					<tbody>
						<tr>
							<td>委托方式：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="trustType" id="trustType" class='form-control select2_width'>
										<c:forEach var="item" items="${trustTypeArray}">
											<option value="${item.PMCO}" ${item.PMCO == '3' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>银行编号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="bnkNo" id="bnkNo" class='form-control select2_width'>
										<c:forEach var="item" items="${bankArray}">
											<option value="${item.bnkNo}" ${item.bnkNo == dto.bnkNo ? "selected" : "" }>${item.bnkNo}&nbsp;&nbsp;${ item.bnkNm }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>开户银行：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='openName' id='openName' value=""/>
								</div>
							</td>
							<td><font color="red">*</font>开户地：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="openAddr" id="openAddr" class='form-control select_addr1' onchange="getCitys(this.value,'1')">
										<option value="" selected="selected">--</option>
										<c:forEach var="item" items="${provinces}">
											<option value="${item.PMCO}" ${item.PMCO == dto.openAddr ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="openbankcity" id="openbankcity" class='form-control select_addr2'></select>
									<input type="hidden" name="hiddencity" id="hiddencity" value="${dto.openBankCity }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>银行户名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='bankAccoNm' id='bankAccoNm'  value=""/>
								</div>
							</td>
							<td><font color="red">*</font>银行账号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='bankAcco' id='bankAcco'  value=""/>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</c:if>
			<c:if test="${isMultiple == '0' }">
				<table class="table table-bordered" id="querytable1">
					<colgroup>
					  	<col width="20%">
					  	<col width="30%">
					  	<col width="20%">
					  	<col width="30%">
					</colgroup>
					<tbody>
						<tr>
							<td>委托方式：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="trustType" id="trustType" class='form-control select2_width'>
										<c:forEach var="item" items="${trustTypeArray}">
											<option value="${item.PMCO}" ${item.PMCO == '3' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>客户名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									${dto.invnm }
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
					<tbody>
						<tr>
							<td><font color="red">*</font>银行编号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="bnkNo" id="bnkNo" class='form-control select2_width'>
										<c:forEach var="item" items="${bankArray}">
											<option value="${item.bnkNo}" ${item.bnkNo == dto.bnkNo ? "selected" : "" }>${item.bnkNo}&nbsp;&nbsp;${ item.bnkNm }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>银行户名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='bankAccoNm' id='bankAccoNm'  value="${dto.bankAccoNm }"/>
								</div>
							</td>
							<td><font color="red">*</font>银行账号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='bankAcco' id='bankAcco'  value="${dto.bankAcco }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>开户银行：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='openName' id='openName' value="${dto.openName }"/>
								</div>
							</td>
							<td><font color="red">*</font>开户地：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="openAddr" id="openAddr" class='form-control select_addr1' onchange="getCitys(this.value,'1')">
										<option value="" selected="selected">--</option>
										<c:forEach var="item" items="${provinces}">
											<option value="${item.PMCO}" ${item.PMCO == dto.openAddr ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="openbankcity" id="openbankcity" class='form-control select_addr2'></select>
									<input type="hidden" name="hiddencity" id="hiddencity" value="${dto.openBankCity }"/>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</c:if>
			</div>
			
			<!-- 分类信息 -->
			<div id="cotegoryInfoContent" class="typeContent" style="display: none;">
				<table class="table table-bordered maintable" id="showTable">
					<colgroup>
						<col width="20%"><col width="30%"><col width="20%"><col width="30%">
					</colgroup>
					<tbody>
						<div class="col-sm-11 form-inner" style="display: none;">
							<select name="comNation" id="comNation" class='form-control select2_width' style="display: none;">
								<c:forEach var="item" items="${nationArray}">
									<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
								</c:forEach>
							</select>
						</div>
						
						<tr>
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
						<col width="20%">
						<col width="30%">
						<col width="20%">
						<col width="30%">
					</colgroup>
					<tbody id="investClassInfo">
					<c:if test="${invtp == '0' }">
						<tr class="custsimpnmTr">
							<td>客户简称：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type="hidden" id="custsimpnmVal" value="${dto.acctabbr }">
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
							<td>业务类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="businessTp" id="businessTp"
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
							<td>地域类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="regionTp" id="regionTp"
										class='form-control select2_width'>
										<c:forEach var="item" items="${regiontpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.regiontp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
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
						</tr>
						<tr>
							<td>反洗钱备注：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='fxqDesc' id='fxqDesc' value="${dto.fxqremark }"/>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="page-footer" style="margin-top: 20px;text-align: center;">
				<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提交" name='btnSubmit'>提交</button>
				<button id="claseBtn" name="claseBtn" type="button" class="btn btn-link" data-dismiss="modal" value="重置">重置</button>
				<button id="reset" name="reset" type="reset" class="btn btn-link" style="display: none;" value="重置">重置</button>
			</div>
		</div>
		</form>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/custAccountMgr/modifySyntheSizeInfoDetail.js?v="<%=dateStr%>></script>
</body>	
</html>