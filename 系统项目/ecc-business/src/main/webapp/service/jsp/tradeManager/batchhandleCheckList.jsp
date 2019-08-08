<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易类批量复核</title>
<style type="text/css">
.form-control{
	width: 250px !important;
}
.select2-container{
	width: 250px !important;
}
</style>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	<div class="permissionBtn" style="display: none;" id="batTradeDetailBtn">
		<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="查看" disabled ><i class="fa fa-file-text-o"></i></a>
	</div>
	<div class="permissionBtn" style="display: none;" id="batTradeReCheckBtn">
		<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="复核" disabled ><i class="iconfont icon-wodeshenpi-copy"></i></a>
	</div>
  	<div class="form-item-group form-horizontal" style="width: auto;max-width: 1123px;" srole="form">
        <div class="form-item">
        	<span class="form-field">业务名称：</span>
            <span class="form-input">
            	<select name="dsapkind" id="dsapkind" placeholder="全部"  class="select2 use-select2 form-input clearText"></select>
            </span>
        </div>
        <div class="form-item">
        	<span class="form-field">基金名称：</span>
            <span class="form-input">
            	<select name="fundid" id="fundid" placeholder="全部"  class="select2 use-select2 form-input clearText"></select>
            </span>
        </div>
        <div class="form-item">
        	<span class="form-field">复核状态：</span>
            <span class="form-input">
            	<select name="checkst" id="checkst" placeholder="全部"  class="select2 use-select2 form-input clearText"></select>
            </span>
        </div>
        <div class="form-item">
         	<span class="form-field">开始日期：</span>
            <span class="form-input">
            	<input type="text" placeholder="开始日期" class="form-control changeText" id="begindate" name="begindate">
            </span>
        </div>
        <div class="form-item">
         	<span class="form-field">结束日期：</span>
            <span class="form-input">
            	<input type="text" placeholder="结束日期" class="form-control changeText" id="enddate" name="enddate">
            </span>
        </div>
        <div class="form-item"></div>
    </div>
	<div class="form-action text-right">
		<button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondition(true);"><i class="fa fa-search"></i>&nbsp;查询</button>
		<button class="btn btn-outline btn-primary" onclick="clearValue();" id="resetBtn" type="reset">清空</button>
	</div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="tradeQryList"></table>
    <div id="tradeQryPage"></div>
</div>
</body>
<script type="text/javascript" src="<%=context%>web/js/tradeManager/batchhandleCheckList.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>capitalService/server";
		setPath(primaryPath,basePath);
</script>
</html>