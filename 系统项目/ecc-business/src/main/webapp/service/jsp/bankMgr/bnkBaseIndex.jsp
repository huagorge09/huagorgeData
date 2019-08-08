<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>

<%
    //String permissionId = TransCodeConstant.TRANS_CODE_9320;//本页面操作权限代码
%>
<input id="transCode" type='hidden' value="9320" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>基本信息管理</title>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
    <div class="form-search-group">
        <a class="btn btn-primary"    href="javascript:openAddDialog()"><i class="fa fa-plus"></i>&nbsp;新建</a>
    </div>
     <div class="hr-line-dotted"></div>
     <div class="form-item-group form-horizontal">
    
	    <div class="form-item" id="banknoDiv">
	      	<span class="form-field">银行代码：</span>
	       	<span class="form-input">
	     		<input type='text' class='form-control white-bg' name='bankno' id='bankno' value="" />	
	        </span>
	    </div>
	    <div class="form-item" id="banknmDiv">
	      	<span class="form-field">银行名称：</span>
	       	<span class="form-input">
	     		<input type='text' class='form-control white-bg' name='banknm' id='banknm' value="" />	
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
    <table id="bankBaseInfoList"></table>
    <div id="bankBaseInfoPage"></div>
</div>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bankMgr/bnkBaseInfo.init.js?20180815"></script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/bankManager";
	setPath(primaryPath,basePath);
</script>

</html>