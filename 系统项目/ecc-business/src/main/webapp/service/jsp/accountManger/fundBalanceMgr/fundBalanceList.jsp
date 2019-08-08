<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>基金余额查询</title>
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
<div class="clearfix form-multi-col-panel" style="text-align: center;">
	<h3>基金余额列表</h3>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
	<input type="hidden" id="fundacct" name="fundacct" value="${fundacct}">
    <table id="fundBalanceList"></table>
    <div id="fundBalancePage"></div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/fundBalanceMgr/fundBalanceList.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = '<%=context%>service/accountManager/';
		setPath(basePath,primaryPath);
</script>
</body>
</html>