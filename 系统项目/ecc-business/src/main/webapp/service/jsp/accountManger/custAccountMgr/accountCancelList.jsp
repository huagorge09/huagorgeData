<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>账户撤单列表</title>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<style type="text/css">
.table input[type='text'],select{
	max-width: 250px !important;
}

.select2{
	max-width: 250px !important;
}
</style>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	<div class="clearfix form-multi-col-panel">
		<input id="permissionId" type="hidden" value="${ permissionId }"/>
		<input id="operatorId" 	 type="hidden" value="${ operatorId }"/>
		<div class="form-item-group form-horizontal" role="form">
	         <div class="form-item">
	            <span class="form-field">交易账号：</span>
	            <span class="form-input">
	            	<input type='text' style="width: 250px;" class='form-control' name='txtTradeAcc' id='txtTradeAcc' />
	            </span>
	         </div>
	         <div class="form-item"></div>
	         <div class="form-item">
	            <span class="form-field">基金账号：</span>
	            <span class="form-input">
	            	<input type='text' style="width: 250px;" class='form-control' name='txtFundAcc' id='txtFundAcc' />
	            </span>
	         </div>
	   		 <div class="form-item"></div>
	         <div class="form-item"></div>
	    </div>
	    <div class="form-action text-right">
	        <button class="btn btn-primary" onclick="queryByCondtion(true);" type="submit"  id="btnQuery"><i class="fa fa-search"></i>&nbsp;查询</button>
	        <button class="btn btn-outline btn-primary" id="resetBtn" type="reset">清空</button>
	    </div>
	</div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="accountInfoList"></table>
    <div id="accountInfoPage"></div>
</div>
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
<script type="text/javascript" src="<%=context%>web/js/accountManger/custAccountMgr/accountCancelList.js?20180621"></script>
</body>
</html>