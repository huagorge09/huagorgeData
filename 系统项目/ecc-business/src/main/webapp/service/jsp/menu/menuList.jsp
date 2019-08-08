<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>

<head>
 <!-- 这里引入导航信息header.tpl -->
 <meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>系统菜单列表</title>
</head>
<script type="text/javascript">
var PATH_PREFIX = '<%=context%>service/menu/';
var MENU_AUTH_PATH_PREFIX='<%=context%>service/menuAuth/';

</script>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
    <div class="form-search-group">
        <a class="btn btn-primary" id="menuAddViewBtn" href="javascript:WASP_MENU.menuAddView();"><i class="fa fa-plus"></i>&nbsp;新建菜单</a>
        <a class="btn btn-primary" id="menuAuthManageViewBtn" href="javascript:WASP_MENU.menuAuthManageView();"><i class="fa fa-plus"></i>&nbsp;菜单权限管理</a>
        <a class="btn btn-primary" id="refreshMenuCacheBtn" href="javascript:WASP_MENU.refreshMenuCache();"><i class="fa fa-refresh"></i>&nbsp;更新菜单缓存</a>
    </div>
     <div class="hr-line-dotted"></div>
  <div class="form-item-group form-horizontal" role="form">
         <div class="form-item">
            <span class="form-field">菜单名称：</span>
            <span class="form-input">
            	<input type="text" placeholder="菜单名称" class="form-control" name="q-name" id="q-name" value="">
            </span>
         </div>
         <div class="form-item"></div>
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
    <table id="menuList"></table>
    <div id="menuPage"></div>
</div>



<input type="hidden" name="currentEmpId" id="currentEmpId" value="${currentEmpId}">
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/menu/menuList.js"></script>
</body>
</html>
