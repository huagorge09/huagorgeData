<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>基金账号销户</title>
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
		<input name="tradeacco" type="hidden" id="tradeacco"/>
		<input name="hidtano" type="hidden" id="hidtano"/>
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
	 	<form name="openCustomFrom" id="openCustomFrom" method="post" action="" >
		<div class="page-body">
			<table id="inputTable" class="table table-bordered" style="display: none;">
				<colgroup><col width="25%"><col width="25%"><col width="25%"><col width="25%"></colgroup>
				<tbody>
					<tr>
						<td>请选择基金账号：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="seltradeacco" id="seltradeacco" class='form-control select2_init'></select>
							</div>
						</td>
						<td>委托方式：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="trustType" id="trustType" class='form-control select2_init'>
									<c:forEach var="item" items="${trustTypeArray}">
										<option value="${item.PMCO}" ${item.PMCO == 3 ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			
			<table id="showPersonalTable" class="table table-bordered" style="display: none;">
				<colgroup><col width="25%"><col width="25%"><col width="25%"><col width="25%"></colgroup>
				<tbody>
					<tr>
						<td>投资者名称：</td>
						<td class="white-bg" colspan="3">
							<span class="col-sm-12">
								<span id="custnm" name="custnm"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>证件类型：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="idtp" name="idtp"></span>
							</span>
						</td>
						<td>证件号码：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="idno" name="idno"></span>
							</span>
						</td>
					</tr>
					<tr>
						<td>TA号码：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="tano" name="tano"></span>
							</span>
						</td>
						<td>TA名称：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="taname" name="taname"></span>
							</span>
						</td>
					</tr>
				</tbody>
			</table>
			<table class="table table-bordered">
				<tbody id="continfo"></tbody>
			</table>
		</div>
		</form>
		<div class="page-footer" id="submitTable" style="margin-top: 20px;text-align: center;display: none;">
			<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提交" name='btnSubmit'>提交</button>
		</div>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/fundAccountMgr/deleteFundAccount.js?v="<%=dateStr%>></script>
</body>	
</html>