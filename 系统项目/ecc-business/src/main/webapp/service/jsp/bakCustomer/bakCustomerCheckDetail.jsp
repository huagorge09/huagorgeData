<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>备案客户信息复核</title>
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
</head>
<body class="fixed-nav gray-bg">
	<div class="modal-content" style="border: 0px;box-shadow: 0 0px 0px;">
		<input id="permissionId" type="hidden" value="${ permissionId }"/>
		<input id="operatorId" 	 type="hidden" value="${ operatorId }"/>
		<input id="custno" type="hidden" value="${dto.custno}"  />		
		<div class="page-header" style="margin: 0;">
			<h4 class="page-title">备案客户信息复核明细</h4>
		</div>
	 	<form name="form" id="form" method="post" action="" >
		<div class="page-body">
			<!-- 机构信息 -->
			<div id="orgBaseInfoContent" class="typeContent">
				<table id="tabOrgBaseInfo" class="table table-bordered">
					<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
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
							<th rowspan="15">其他信息</th>
							<td>备案客户角色：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="backCustRole" name="backCustRole">${dto.rcdcustrole}</span>
								</span>
							</td>
							<td>客户风险承受能力：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="riskLevel" name="riskLevel">${dto.riskLevel }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>通讯地址：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="addr" name="addr">${dto.addr }</span>
								</span>
							</td>
							<td>邮政编码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="postcode" name="postcode">${dto.postcode }</span>
								</span>
							</td>
						</tr>
					</tbody>
					
					<tbody>
						 <tr>
						     <th rowspan="1000" >其他证件信息</th>
						 </tr>
	 					<c:forEach var="item" items="${dto.oidlist}" varStatus='counter'>
					     <tr>
							 <td COLSPAN="5" style='text-align: center;'>证件${counter.count}</td>
					  </tr>
						<tr>
							<td>证件类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontactgrant">${item.idtp }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>证件号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontactnation">${item.idno }</span>
								</span>
							</td>
							<td>证件有效期：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="" name="ocontidtp">${item.idvalidate }</span>
								</span>
							</td>
						</tr>
					</c:forEach>
						
					</tbody>
					
					<tbody>
						<tr>
						     <th rowspan="99">其他经办人员信息</th>
						 </tr>
	 					<c:forEach var="item" items="${dto.ocontactlist}" varStatus='counter'>
						 <tr>
							 <td style='text-align: center;' colspan='5'>经办人${counter.count}</td>
						 </tr>
						<tr>
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
				</table>
			</div>
			<div class="page-footer" style="margin-top: 20px;text-align: center;">
				<button id="doY" type="button" class="btn btn-primary btn-save" value="提交" name="doY" onclick="doSubmit('Y')">复核通过</button>
				<button id="doR" type="button" class="btn btn-primary btn-save" value="提交" name="doR" onclick="doSubmit('C')">复核拒绝</button>
				<button id="closeBtn" name="claseBtn" type="button" class="btn btn-link" value="取消"   onclick="window.close();">返回</button>
			</div>
		</div>
		</form>
	</div>
	<script type="text/javascript" src="<%=context%>web/js/bakCustomer/bakCustomerCheckDetail.js?v="<%=dateStr%>></script> 
	<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/bakCustomerManager";
		setPath(primaryPath,basePath);
	</script>
</body>	
</html>