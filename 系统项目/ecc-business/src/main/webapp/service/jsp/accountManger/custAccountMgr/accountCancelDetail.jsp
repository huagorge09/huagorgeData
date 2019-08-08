<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>账户撤单</title>
<style type="text/css">
.table input[type='text'],select,input[type='password']{
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
var accountPath = '<%=context%>service/accountCancelManager/';
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
		<input name="tradeacco" type="hidden" id="tradeacco" value="${dto.tradeacco }"/>
		<input name="oserialno" type="hidden" id="oserialno" value="${serialno }"/>
		
		<div class="page-header" style="margin: 0;">
			<h4 class="page-title">账户撤单</h4>
		</div>
	 	<form name="form" id="form" method="post" action="" >
		<div class="page-body">
			<!-- 个人信息 -->
			<div id="baseInfoContent" class="typeContent">
				<table id="tabPslBaseInfo" class="table table-bordered">
					<colgroup><col width="20%"><col width="30%"><col width="20%"><col width="30%"></colgroup>
					<tbody>
						<tr>
							<td>客户名称：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="invnm" name="invnm">${dto.invnm }</span>
								</span>
							</td>
							<td>客户类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="invtp" name="invtp">${dto.invtpnm }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>证件类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="idtp" name="idtp">${dto.idtpnm }</span>
								</span>
							</td>
							<td>证件号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="idno" name="idno">${dto.idno }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="contname" name="contname">${dto.contact }</span>
								</span>
							</td>
							<td>可用资金：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="validateMoney" name="validateMoney">${dto.availableBalance }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>经办人证件类型：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="contidtp" name="contidtp">${dto.contidtp }</span>
								</span>
							</td>
							<td>经办人证件号码：</td>
							<td class="white-bg">
								<span class="col-sm-12">
									<span id="contidno" name="contidno">${dto.contidno }</span>
								</span>
							</td>
						</tr>
						<tr>
							<td>委托方式：</td>
							<td class="white-bg" colspan="3">
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
							<td>主管工号：</td>
							<td class="white-bg form-inner">
								<div class="col-sm-11 form-inner">
									<input style="display: none;" type="text" id="checkno" name="checkno"/>
									<span class="checkno"></span>
								</div>
							</td>
							<td>主管密码：</td>
							<td class="white-bg form-inner">
								<div class="col-sm-11 form-inner">
									<input style="display: none;" type="text" id="checkpwd" name="checkpwd"/>
									<span class="checkpwd"></span>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="page-footer" style="margin-top: 20px;text-align: center;">
				<button id="audit" type="button" class="btn btn-primary btn-save" value="授权" name="btnGrant">授权</button>
				<button id="btnCancel" type="button" class="btn btn-primary btn-save" value="提交" name="btnCancel" onclick="doSubmit('R')">提交</button>
				<button id="closeBtn" name="claseBtn" type="button" class="btn btn-link" value="取消" onclick="doBack()">取消</button>
			</div>
		</div>
		</form>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/custAccountMgr/accountCancelDetail.js?v="<%=dateStr%>></script>
</body>	
</html>