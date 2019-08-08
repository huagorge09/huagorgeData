<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>权限设置信息</title>
<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/css/plugins/multiselect/multiselect.css" />
<style type="text/css">
	table {
	    font-family: "Microsoft Yahei", '微软雅黑', Helvetica, Arial, sans-serif;
	    font-size: 12px; 
	}
</style>
	
</head>
<body class="sub-page">
	<form id="privilegeUpdateForm" name="privilegeUpdateForm"  method="post" class="form-horizontal">
		<!-- 隐藏属性区域 -->
		<input type="hidden"  name="roleId" value="${roleVo.roleId }"/>
		<input type="hidden"  name="resId"  id="resId" />
		
		<div class="modal-content">
			<div class="modal-header">
		        <h4 class="modal-title">修改权限设置</h4>
		    </div>
		    <div class="modal-body">
		    	<div class="table-responsive">
		    		<table class="table table-bordered">
		    			<colgroup>
		                    <col width="20%">
		                    <col width="30%">
		                    <col width="20%">
		                    <col width="30%">
		                </colgroup>
		                <tbody>
		                	<tr>
		                		<td>角色名称&nbsp;<span class="text-danger">*</span></td>
		                		<td colspan="3" class="white-bg">
		                			${roleVo.roleName }
		                		</td>
		                	</tr>
		                	<tr>
		                		<td>员工姓名&nbsp;<span class="text-danger">*</span></td>
		                		<td colspan="3" class="white-bg">
		                			<c:if test="${!empty roleVo.empsName}">
									<div  style="line-height: 30px">
										<c:forEach items="${roleVo.empsName}" var="vcl">
										    <c:if test="${!empty vcl}">
												<span class="text-wrapper">${vcl}</span>
										    </c:if>
										</c:forEach>
									</div>
								</c:if>
								
		                		</td>
		                	</tr>
		                	<tr>
		                		<td>分享权限列表&nbsp;</td>
		                		<td colspan="3" class="white-bg">
									<c:forEach var="shareItem" items="${shareRoleList}">
										<div>&nbsp;${shareItem.roleName} &nbsp;</div>
									</c:forEach>
		                		</td>
		                	</tr>
		                	
		                	<tr>
	                        <td>资源列表</td>
	                        <td colspan="3" class="white-bg">
								<c:forEach var="resourceItem"  varStatus="status"  items="${resourceList}">
									<div>&nbsp;${resourceItem.resName} &nbsp;</div>
								</c:forEach>
	                        </td>
	                    </tr>
		                </tbody>
		            </table>
		         </div>
		    </div>
		</div>    
	</form>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/privilege/privilegeUpdate.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/privilege";
		setPath(basePath,primaryPath);
</script>
</body>
</html>