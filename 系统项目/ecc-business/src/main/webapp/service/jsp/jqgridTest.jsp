<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>
<script type="text/javascript">
	var path = <%=context%> +'service/test/';
</script>
<head>
 <!-- 这里引入导航信息header.tpl -->
 <meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>测试页面</title>
</head>

<body class="fixed-nav gray-bg">     
<div class="clearfix form-multi-col-panel">
  	<div class="form-item-group form-horizontal" role="form">
	    <div class="form-item">
	    	<span class="form-field">产品简称：</span>
	      	<span class="form-input"><input class="form-control" placeholder="产品简称" name="fundSName" id="fundSName"></span>
	    </div>
    </div> 
    <div class="form-action text-right">
		<button class="btn btn-primary" type="submit"  id="queryBtn" name="&nbsp;" onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp;查询</button>
		<button class="btn btn-outline btn-primary" id="resetBtn" name="resetBtn" type="reset" >清空</button>
    </div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="investProductList"></table>
    <div id="investProductPage"></div>
</div>
</body>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/investProduct.js?20170221533"></script>
</html>
