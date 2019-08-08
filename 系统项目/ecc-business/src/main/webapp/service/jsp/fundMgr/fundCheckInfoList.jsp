<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>复核基金信息</title>
<style type="text/css">
.ui-jqgrid tr.jqgrow td:first-child, .ui-jqgrid tr.jqgrow td:first-child {
    border-left: 0px;
}
</style>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
  	<div class="form-item-group form-horizontal" role="form">
         <div class="form-item">
            <span class="form-field">基金代码：</span>
            <span class="form-input">
            	<input type="text" placeholder="基金代码" class="form-control" name="fundid" id="fundid" value="">
            	<input id="transCode" type='hidden' value="9320" />
            </span>
         </div>
         <div class="form-item">
         	 <span class="form-field">基金名称：</span>
            <span class="form-input">
            	<input type="text" placeholder="基金名称" class="form-control" name="fundname" id="fundname" value="">
            </span>
         </div>
         <div class="form-item"></div>
   		 <div class="form-item"></div>
         <div class="form-item"></div>
    </div>
    
	<div class="form-action text-right">
		<button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp;查询</button>
		<button class="btn btn-outline btn-primary" id="resetBtn" type="reset">清空</button>
	</div>
</div>

<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="fundCheckInfoList"></table>
    <div id="fundCheckInfoPage"></div>
</div>
</body>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/fund/fundCheckManager.js?20180533"></script>
<script type="text/javascript" src="<%=context%>web/js/fund/common.js?20180529"></script>
<script type="text/javascript">
$(function(){
	var colNames=['','','','基金代码','基金名称','基金状态','基金类别',"注册登记代码","货币类型","更新时间","操作"];
	initGrid(colNames);
});
</script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/fundManager";
	setPath(primaryPath,basePath);
</script>
</html>