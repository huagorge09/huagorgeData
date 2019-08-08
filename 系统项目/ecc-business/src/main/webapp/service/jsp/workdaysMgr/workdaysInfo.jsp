<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工作日管理</title>
<style type="text/css">
.ui-jqgrid tr.jqgrow td:first-child, .ui-jqgrid tr.jqgrow td:first-child {
    border-left: 0px;
}
</style>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	<input id="transCode" type='hidden' value="9320" />
    <div class="hr-line-dotted"></div>
    <div class="form-action text-left">
        <button class="btn btn-primary" type="submit"  id="saveWorkDay"  onclick="openAddPage();">增加</button>
        <button class="btn btn-primary" id="exportBtn" onclick="openAutoAddPage();">自动生成工作日</button>
    </div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="workDaysList"></table>
    <div id="workDaysPage"></div>
</div>
</body>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/fund/common.js?20180605"></script>
<script type="text/javascript" src="<%=context%>web/js/workdaysMgr/workdaysManager.js?20180604"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/workdaysManager";
		setPath(primaryPath,basePath);
</script>
</html>