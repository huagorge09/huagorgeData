<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>
<head>
	<title>菜单权限管理页面</title>
    <meta charset="utf-8">
     <link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/css/plugins/jsTree/style.css" />
</head>
<script type="text/javascript">
	var PATH_PREFIX = '<%=context%>service/menu/';
	var MENU_AUTH_PATH_PREFIX='<%=context%>service/menuAuth/';
</script>
<body class="fixed-nav gray-bg main-page">
    <div class="wrapper wrapper-content">
     <div class="" style="float: left;width: 300px;">
       
       <div class="ibox-content" >
         <form id="menuAuthForm" action="<%=context%>service/menuAuth/saveMenuAuth.do" method="post">
	          <input name="authId" id="authId" type="hidden"  />
	          <input name="srcOrmemberId" id="srcOrmemberId" type="hidden" value="0" />
	          <button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" data-loading-text="<i class='ico-loading'></i>" onclick="ajaxSaveMenuAuth();">保存</button>
          </form>
        </div>
        <div class="ibox-content">
            <div id="organizationList"></div>
            
        </div>
     </div>
     <div class="" style="float: left;width: 300px;">
         <div class="ibox-content" id="menuList-ibox">
             <div id="menuList"></div>
           
         </div>
     </div>
     <div class="" style="float: left;width: 300px;">
         <div class="ibox-content" id="buttonList-ibox">
             <div id="buttonList"></div>
           
         </div>
     </div>
</div>
</div>
<!-- 树状菜单必须引用 -->
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/jsTree/jstree.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/menu/menuAuthManage.js?20160704"></script>
</body>
</html>
