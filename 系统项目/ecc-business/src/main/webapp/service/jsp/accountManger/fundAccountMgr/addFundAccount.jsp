<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>增开基金账号</title>
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
</style>
<script type="text/javascript">
var widgetPath = '<%=context%>service/widget/';
var accountPath = '<%=context%>service/fundAccountManager/';
//默认加载
$(function(){
	//把访问路径传到js
	setPath(widgetPath,accountPath);
});
</script>
</head>
<body class="fixed-nav gray-bg">
	<div class="clearfix form-multi-col-panel">
		<input id="permissionId" type="hidden" value="${ permissionId }"/>
		<input id="operatorId" 	 type="hidden" value="${ operatorId }"/>
		<input name="custno" type="hidden" id="custno"/>
		<input name="tradeacco" type="hidden" id="tradeacco"/>
		<form id="query_account" name="query_account" action="">
			<div class="form-item-group form-horizontal" role="form">
		         <div class="form-item">
		            <span class="form-field">交易账号：</span>
		            <span class="form-input">
		            	<input type='text' class='form-control' name='inputQueryTradeAcc' id='inputQueryTradeAcc' />
		            </span>
		         </div>
		         <div class="form-item">
		            <span class="form-field">基金账号：</span>
		            <span class="form-input">
		            	<input type='text' class='form-control' name='inputQueryFundAcc' id='inputQueryFundAcc' />
		            </span>
		         </div>
		         <div class="form-item"></div>
		   		 <div class="form-item"></div>
		         <div class="form-item"></div>
		    </div>
		    <div class="form-action text-right">
		        <button class="btn btn-primary" type="submit"  id="btnQuery"><i class="fa fa-search"></i>&nbsp;查询</button>
		        <button class="btn btn-outline btn-primary" id="resetBtn" type="reset">清空</button>
		    </div>
		</form>
	</div>
	<div class="modal-content" style="border: 0px;box-shadow: 0 0px 0px;">
		<div class="page-body">
			<table id="inputTable" class="table table-bordered" style="display: none;">
				<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
				<tbody>
					<tr>
						<th rowspan="3">录入信息</th>
						<td>委托方式：</td>
						<td class="white-bg" colspan="3">
							<div class="col-sm-11 form-inner">
								<select name="trustType" id="trustType" class='form-control select2_init'>
									<c:forEach var="item" items="${trustTypeArray}">
										<option value="${item.PMCO}" ${item.PMCO == 3 ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td>TA代码：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="tano" id="tano" class='form-control select2_init'>
						     		<option value="17">17 招商基金注册登记系统</option>
						     		<option value="98">98 LOFTA</option>
								</select>
							</div>
						</td>
						<td>请选择交易账号:</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="seltradeacco" id="seltradeacco" class='form-control select2_init'></select>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			
			<table id="showPersonalTable" class="table table-bordered" style="display: none;">
				<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
				<tbody>
					<tr>
						<th rowspan="4">基本信息</th>
						<td>客户名称：</td>
						<td class="white-bg" colspan="3">
							<span class="col-sm-12">
								<span id="pinvnm" name="pinvnm"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>客户类型：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="pinvtp" name="pinvtp"></span>
							</span>
						</td>
						<td>证件类型：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="pidtp" name="pidtp"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>证件号码：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="pidno" name="pidno"></span>
							</span>
						</td>
						<td>证件有效期：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="pidvalidate" name="pidvalidate"></span>
							</span>
						</td>
					</tr>
				</tbody>
   				<tbody>
   					<tr>
   						<td rowspan="5">银行信息</td>
						<td>开户银行：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="popenName" name="popenName"></span>
							</span>
						</td>
						<td>银行户名：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="pbankAccoNm" name="pbankAccoNm"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>开户地：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="popenAddr" name="popenAddr"></span>
							</span>
						</td>
						<td>银行编号：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="pbnkNo" name="pbnkNo"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>银行帐号：</td>
						<td class="white-bg" colspan="3">
							<span class="col-sm-12">
								<span id="pbankAcco" name="pbankAcco"></span>
							</span>
						</td>
					</tr>
   				
				</tbody>
			</table>
			
			
			<table id="showOrgTable" class="table table-bordered" style="display: none;">
				<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
				<tbody>
					<tr>
						<th rowspan="4">证件信息</th>
						<td>投资者名称：</td>
						<td class="white-bg" colspan="3">
							<span class="col-sm-12">
								<span id="oinvnm" name="oinvnm"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>投资者类型：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oinvtp" name="oinvtp"></span>
							</span>
						</td>
						<td>注册登记证件类型：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oidtp" name="oidtp"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>注册登记证件号码：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oidno" name="oidno"></span>
							</span>
						</td>
						<td>注册登记证件有效期：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oidvalidate" name="oidvalidate"></span>
							</span>
						</td>
					</tr>
				</tbody>
   				<tbody>
   					<tr>
   						<td rowspan="2">银行信息</td>
						<td>预留银行全称：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oopenName" name="oopenName"></span>
							</span>
						</td>
						<td>预留银行开户地：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oopenAddr" name="oopenAddr"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>预留银行户名：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="obankAccoNm" name="obankAccoNm"></span>
							</span>
						</td>
						<td>预留银行帐号：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="obankAcco" name="obankAcco"></span>
							</span>
						</td>
					</tr>
				</tbody>
				<tbody>
   					<tr>
						<td rowspan="3">法人信息</td>
						<td>法定代表人姓名：</td>
						<td class="white-bg" colspan="3">
							<span class="col-sm-12">
								<span id="oinstrepnm" name="oinstrepnm"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>法定代表人国籍：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oinstrepnation" name="oinstrepnation"></span>
							</span>
						</td>
						<td>法定代表人证件类型：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oinstrepidtp" name="oinstrepidtp"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>法定代表人证件号码：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oinstrepidno" name="oinstrepidno"></span>
							</span>
						</td>
						<td>法定代表人证件有效期：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oinstrepvalidate" name="oinstrepvalidate"></span>
							</span>
						</td>
					</tr>
   				</tbody>
				<tbody>
   					<tr>
						<td rowspan="3">机构负责人信息</td>
						<td>机构负责人姓名：</td>
						<td class="white-bg" colspan="3">
							<span class="col-sm-12">
								<span id="oprincipalname" name="oprincipalname"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>机构负责人国籍：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oprincipalnation" name="oprincipalnation"></span>
							</span>
						</td>
						<td>机构负责人证件类型：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oprincipalidtp" name="oprincipalidtp"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>机构负责人证件号码：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oprincipalidno" name="oprincipalidno"></span>
							</span>
						</td>
						<td>机构负责人证件有效期：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="oprincipalvalidt" name="oprincipalvalidt"></span>
							</span>
						</td>
					</tr>
   				</tbody>
				<tbody>
   					<tr>
						<td rowspan="5">经办人信息</td>
						<td>经办人授权范围：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="ocontactgrant" name="ocontactgrant"></span>
							</span>
						</td>
						<td>经办人姓名：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="ocontact" name="ocontact"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>经办人国籍：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="ocontactnation" name="ocontactnation"></span>
							</span>
						</td>
						<td>经办人证件类型：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="ocontidtp" name="ocontidtp"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>经办人证件号码：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="ocontidno" name="ocontidno"></span>
							</span>
						</td>
						<td>经办人证件有效期：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="ocontvalidate" name="ocontvalidate"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>经办人办公电话：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="ocontphone" name="ocontphone"></span>
							</span>
						</td>
						<td>经办人传真号码：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="ocontfax" name="ocontfax"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>经办人手机号：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="ocontmobile" name="ocontmobile"></span>
							</span>
						</td>
						<td>经办人电子邮件：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="ocontemail" name="ocontemail"></span>
							</span>
						</td>
					</tr>
   				</tbody>
			</table>
		</div>
		<div class="page-footer" id="submitTable" style="margin-top: 20px;text-align: center;display: none;">
			<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提交" name='btnSubmit'>提交</button>
		</div>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/fundAccountMgr/addFundAccount.js?v="<%=dateStr%>></script>
</body>	
</html>