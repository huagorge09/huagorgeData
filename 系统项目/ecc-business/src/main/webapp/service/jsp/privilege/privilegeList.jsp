<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta HTTP-EQUIV="pragma" CONTENT="no-cache"> 
	<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
	<meta HTTP-EQUIV="expires" CONTENT="0">
	<title>数据权限设置</title>
</head>
<body class="fixed-nav gray-bg">
	<div class="form-search-group">
		<div class="permissionBtn" style="display: none;" id="roleUpdateBtn">
			<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改" disabled ><i class="fa fa-pencil-square-o"></i></a>
		</div>
	</div>
	
	<div class="clearfix form-multi-col-panel">
		<div class="form-search-group">
	        <a class="btn btn-primary"  href="javascript:privilegeFunc.updatePrivilegeAuthRel();">&nbsp更新查询权限</a>    
	    </div>
	    <div class="hr-line-dotted"></div>
		<div class="form-item-group form-horizontal" role="form">
			<div class="form-item">
	            <span class="form-field">角色名称：</span>
	            <span class="form-input">
	           		<input class="form-control" placeholder="角色名称" id="q-roleName" name="q-roleName">
	            </span>
	        </div> 
	        <div class="form-item">
	            <span class="form-field">员工姓名：</span>
	            <span class="form-input">
	           		<select class="form-control use-select2" id="q-empId" name="q-empId"></select>
	            </span>
	        </div> 
	        <div class="form-item"></div>
	        <div class="form-item"></div>
	        <div class="form-item"></div>
		</div>
		<div class="form-action text-right">
          <button class="btn btn-primary" type="submit"  id="queryBtn" name="queryBtn"  onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp查询</button>
          <button class="btn btn-outline btn-primary" id="resetBtn" name="resetBtn" type="reset">清空</button>
      	</div> 
	</div>
	
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="roleList"></table>
    <div id="rolePage"></div>
</div>

<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/privilege/privilegeList.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var rolePath = "<%=context%>service/role";
		var primaryPath = "<%=context%>service/privilege";
		setPath(basePath,primaryPath,rolePath);
</script>

</body>
</html>