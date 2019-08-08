<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>新增备案客户</title>
<style type="text/css">
/* ul{
	margin:0 auto;
} */
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
    margin-right: 21px;
}
input[type="radio"], input[type="checkbox"]{
	margin: 2px 8px;
}

.select_addr1{
	width: 115px !important;
	float: left;
	margin-right: 12px;
}
.select_addr2{
	width: 115px !important;
	float: left;
}
.table > tbody + tbody{
	border-top: 0 !important;
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
</head>
<body class="fixed-nav gray-bg">
	<div class="modal-content" style="border: 0px;box-shadow: 0 0px 0px;">
		<input id="permissionId" type="hidden" value="${ permissionId }"/>
		<input id="operatorId" 	 type="hidden" value="${ operatorId }"/>
		<input id="documentlist" value='' type="hidden" />
		<div class="page-header" style="margin: 0;">
			<h4 class="page-title">新增备案客户</h4>
			
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
		    </ul>
	 	</div><br>
	 	<form name="openCustomFrom" id="openCustomFrom" method="post" action="" >
		<div class="page-body">
			<!-- 基本信息 -->
			<div id="baseInfoContent" class="typeContent">
			<!-- 机构备案开户 -->
			<table class="table table-bordered" id="tabOrgBaseInfo">
					<colgroup>
						<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody id="idinfo">
						<tr>
							<td rowspan="2">证件信息</td>
							<td><font color="red">*</font>投资者名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgInvNm' id='orgInvNm' style="float: left;"/>
								</div>
							</td>
							<td><font color="red">*</font>注册登记证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner" style="line-height: 32px;">
									<select name="orgInvIdtp" id="orgInvIdtp" class='form-control select2_width'>
										<c:forEach var="item" items="${seatidtpArray}">
											<option value="${item.PMCO}" ${item.PMCO == '11' ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>注册登记证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='orgInvIdno' id='orgInvIdno' />
								</div>
							</td>
							<td><font color="red">*</font>注册登记证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgInvIdvalidate' id='orgInvIdvalidate' />
									<label style="line-height: 34px;margin: 0;">
									<input class="i-checks" id="changepslInvIdValidate" type="checkbox" onclick="changetime('changepslInvIdValidate','orgInvIdvalidate');">长期</label><br>
								</div>
							</td>
						</tr>
						
						<tbody >
						<tr>
							<td rowspan="3">法人信息</td>
							<td><font color="red">*</font>法定代表人姓名：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgInstrepnm' id='orgInstrepnm' onKeyUp="changeInstrep(this,'orgPrincipalname',false)"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>法定代表人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgInstrepnation" id="orgInstrepnation" class='form-control select2_width' onchange="changeInstrep(this,'orgPrincipalnation',true)">
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td><font color="red">*</font>法定代表人证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgInstrepidtp" id="orgInstrepidtp" class='form-control select2_width' onchange="changeInstrep(this,'orgPrincipalidtp',true)">
										<c:forEach var="item" items="${idtpArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>法定代表人证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="18" class='form-control' name='orgInstrepidno' id='orgInstrepidno'
										onKeyUp="changeInstrep(this,'orgPrincipalidno',false);"/>
								</div>
							</td>
							<td><font color="red">*</font>法定代表人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgInstrepidvalidate' id='orgInstrepidvalidate' onchange="changeInstrep(this,'orgPrincipalidvalidate',false)"/>
									<label style="line-height: 34px;margin: 0;">
										<input class="i-checks" id="changeorgInstrepidvalidate" type="checkbox"  
											   onclick="changetime('changeorgInstrepidvalidate','orgInstrepidvalidate');">长期
									</label><br>
								</div>
							</td>
						</tr>
						
						<tr>
							<td rowspan="3">机构负责人信息</td>
							<td><font color="red">*</font>机构负责人姓名：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgPrincipalname' id='orgPrincipalname'/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>机构负责人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgPrincipalnation" id="orgPrincipalnation" class='form-control select2_width'>
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == '156' ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td><font color="red">*</font>机构负责人证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgPrincipalidtp" id="orgPrincipalidtp" class='form-control select2_width'>
										<c:forEach var="item" items="${idtpArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>机构负责人证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="18" class='form-control' name='orgPrincipalidno' id='orgPrincipalidno'/>
								</div>
							</td>
							<td><font color="red">*</font>机构负责人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgPrincipalidvalidate' id='orgPrincipalidvalidate' />
									<label style="line-height: 34px;margin: 0;">
										<input class="i-checks" id="changeorgPrincipalidvalidate" type="checkbox" 
											   onclick="changetime('changeorgPrincipalidvalidate','orgPrincipalidvalidate');"/>长期
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
									<select name="orgContactright" id="orgContactright" class='form-control select2_width'>
										<c:forEach var="item" items="${contprivArray}">
											<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								<div style="display: none;" class="orgContactrightDiv">
									<div class="col-sm-11 form-inner">
										<select name="orgContactright" id="oocontactgrant" class='form-control orgContactright'>
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
									<input type='text' maxlength="18" class='form-control' name='orgContnm' id='orgContnm'/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>经办人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgContnation" id="orgContnation" class='form-control select2_width'>
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								<div class="orgContnationDiv" style="display: none;">
									<div class="col-sm-11 form-inner">
										<select name="orgContnation" class="orgContnation" class='form-control'>
											<c:forEach var="item" items="${nationArray}">
												<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected = selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
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
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
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
									<input type='text' maxlength="18" class='form-control' name='orgContidno' id='orgContidno' />
								</div>
							</td>
							<td><font color="red">*</font>经办人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgContidvalidate' id='orgContidvalidate' />
									<label style="line-height: 34px;margin: 0;">
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
									<input type='text' class='form-control' name='orgContphone' id='orgContphone' />
								</div>
							</td>
							<td><font color="red">*</font>经办人传真号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContfax' id='orgContfax' />
								</div>
							</td>
						</tr>
						
						<tr>
							<td>经办人手机号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContmobile' id='orgContmobile' />
								</div>
							</td>
							<td>经办人电子邮件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContemail' id='orgContemail' />
								</div>
							</td>
						</tr>
						
						<tr>
							<td>经办人联系地址：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContAddr' id='orgContAddr' />
								</div>
							</td>
							<td>经办人邮政编码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContPostcode' id='orgContPostcode' maxlength="6"/>
								</div>
							</td>
						</tr>
					</tbody>
					
					<tbody>
						<tr>
							<td rowspan="4">其他信息</td>
							<td>备案客户角色：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="bakCustRole" id="bakCustRole" class='form-control select2_width'>
										<c:forEach var="item" items="${custRoleArray}">
											<option value="${item.PMCO}" ${item.PMCO == 'I' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>客户风险承受能力：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="riskLevel" id="riskLevel" class='form-control select2_width'>
										<c:forEach var="item" items="${custrisklevelArray}">
											<option value="${item.PMCO}" ${item.PMCO == '1' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>通讯地址：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgAddr' id='orgAddr' />
								</div>
							</td>
							<td><font color="red">*</font>邮政编码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgPostcode' id='orgPostcode' maxlength="6"/>
								</div>
							</td>
						</tr>
						
						
					</tbody>
				</table>
			</div>
			 <!-- 隐藏，用于添加其他证件信息时使用 -->
			<div class="orgInvIdtpDiv" style="display: none;">
				<div class="col-sm-11 form-inner" style="line-height: 32px;">
					<select name="orgInvIdtp" id="ooidtp" class='form-control orgInvIdtp'>
						<c:forEach var="item" items="${seatidtpArray}">
							<option value="${item.PMCO}" ${item.PMCO == '10' ?"selected": "" } >${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
						</c:forEach>
					</select>
				</div>
			</div> 
			<!-- 附加信息 -->
			<div id="extInfoContent" class="typeContent" style="display: none;">
				<table class="table table-bordered maintable" id="tabOrgExtInfo" style="display: none;">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody id="oidinfo">
						<tr>
							<th rowspan="1">其他证件信息</th>
							<td class="white-bg" colspan="4">
								<button id="btnDelId" type="button" class="btn btn-primary btn-add" value="删除" onclick = 'delIdRow();'>删除</button>
								<button id="btnAddId" type="button" class="btn btn-primary btn-add" value="增加" >增加</button>
							</td>
						</tr>
					</tbody>
					
					<tbody id="ocontInfo">
						<tr>
							<th rowspan="1">其他经办人信息</th>
							<td class="white-bg" colspan="4">
								<button id="btnDelCont" type="button" class="btn btn-primary btn-add" value="删除" onclick="delContRow();">删除</button>
								<button id="btnAddCont" type="button" class="btn btn-primary btn-add" value="增加">增加</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
		</div>
		<div class="page-footer" style="margin-top: 20px;text-align: center;">
			<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提交" name='btnSubmit'>提交</button>
			<button id="claseBtn" name="claseBtn" type="button" class="btn btn-primary" data-dismiss="modal" value="重置">重置</button>
			<button id="reset" name="reset" type="reset" class="btn btn-link" style="display: none;" value="重置">重置</button>
            <button type="button" class="btn btn-link"  onclick="window.close();"  id="closeBtn" name="closeBtn">取消</button>
		</div>
		
		</form>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/bakCustomer/bakCustomerAdd.js?v="<%=dateStr%>></script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/bakCustomerManager";
	setAddPath(primaryPath,basePath);
</script>
</body>	
</html>