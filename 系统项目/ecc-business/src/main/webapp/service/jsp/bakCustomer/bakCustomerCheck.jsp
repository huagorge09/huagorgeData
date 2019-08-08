<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%
	String operatorId = SessionUtils.getEmployee().getID();					
%>
<title>备案客户复核管理</title>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	<input type="hidden" id="operatorId" value="<%= operatorId %>" />
   	<div class="form-item-group form-horizontal">
	    <div class="form-item" id="customerNameDiv">
	      	<span class="form-field">客户编号：</span>
	       	<span class="form-input">
	     		<input type='text' class='form-control white-bg' name='custno' id='custno' value="" />	
	        </span>
	    </div>
	    <div class="form-item" id="opertpDiv">
	      	<span class="form-field">操作类型：</span>
	       	<span class="form-input">
		       	<select class="form-control use-select2" name="opertp" id="opertp">
            		<option value="">全部</option>
		    		<option value="I">I 新增</option>
		    		<option value="U">U 修改</option>
		    		<option value="D">D 删除</option>
	           	</select>
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
    <table id="bnkCustomerCheckInfoList"></table>
    <div id="bnkCustomerCheckInfoPage"></div>
</div>
</body>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bakCustomer/bakCustomerCheck.js?20180726"></script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/bakCustomerManager";
	setPath(primaryPath,basePath);
</script>
</html>