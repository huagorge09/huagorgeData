<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>

<head>

 <!-- 这里引入导航信息header.tpl -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>综合营销平台</title>
<script type="text/javascript">
var prdPath = '<%=context%>service/mailManager/';
//默认加载
$(function(){
	//把访问路径传到js
	setPathPrefix(prdPath);
});
</script>
</head>

<body class="fixed-nav gray-bg">
	<div class="clearfix form-multi-col-panel">
		<div class="form-item-group form-horizontal" role="form" >
        	<div class="form-item">
        		<span class="form-field">标题：</span>
				<span class="form-input">
            		<input class="form-control" name="q-title"  id="q-title" placeholder="标题"/>
            	</span>
        	</div>
        	<div class="form-item"></div>
        	<div class="form-item"></div>
        	<div class="form-item"></div>
    	</div>
    	<div class="form-action text-right">
          	<button class="btn btn-primary" type="submit" onclick="queryByCondtion(true)"  id="queryByCondtionBtn" name="queryByCondtionBtn"><i class="fa fa-search"></i>&nbsp;查询</button>
         	<button class="btn btn-outline btn-primary" type="reset" id="clearAllBtn" name="clearAllBtn">清空</button>
    	</div>
	</div>
	<div class="jqGrid_wrapper fullscreen-wrapper">	
    	<table id="msgConfList"></table>
    	<div id="msgConfPage"></div>
    	<input type="hidden" name="subGridTBId" id="subGridTBId" value=""/>
		<input type="hidden" name="hiddenmsgconfid" id="hiddenmsgconfid" value=""/>
	</div>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/msg/msgConf.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/msg/msgConf.search.js"></script>
</body>
</html>

