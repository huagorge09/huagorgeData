<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>

<head>

 <!-- 这里引入导航信息header.tpl -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">

<title>招商基金知识管理系统</title>
<style type="text/css">
	.msg-viewed{
		font-weight: normal !important;
	}
	.ui-jqgrid .ui-jqgrid-htable th div{
		text-align: center;
	}
</style>
<script type="text/javascript">
var msgViewPath = '<%=context%>'+'service/msgView/';
//默认加载
$(function(){
	//把访问路径传到js
	setPathPrefix(msgViewPath);
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
        	
        	<div class="form-item">
        		<span class="form-field">状态：</span>
        		<span class="form-input">
            		<select name="q-stat" id="q-stat" class="form-control use-select2" >
						<option value=""></option>
						<option value="INIT">未读</option>
						<option value="VIEWED">已读</option>
						<option value="1">有标记</option>
					</select>
				</span>
        	</div>
        	
        	<div class="form-item">
        		<span class="form-field">日期：</span>
            	<span class="form-input">
	            	<input class="form-control" placeholder="日期" id="q-theDate" name="q-theDate">
	            </span> 
        	</div>
        	<div class="form-item"></div>
        	<div class="form-item"></div>
        	
    	</div>
    	<div class="form-action text-right">
          	<button class="btn btn-primary" type="submit" onclick="queryByCondtion(true)"  id="queryByCondtionBtn" name="queryByCondtionBtn"><i class="fa fa-search"></i>&nbsp;查询</button>
         	<button class="btn btn-outline btn-primary" type="reset" id="clearAllBtn" name="clearAllBtn">清空</button>
    	</div>
	</div>


	<div class="jqGrid_wrapper fullscreen-wrapper">	
    	<table id="msgViewList"></table>
    	<div id="msgViewPage"></div>
	</div>

<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/msg/msgView.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/msg/msgView.search.js"></script>
</body>
</html>

