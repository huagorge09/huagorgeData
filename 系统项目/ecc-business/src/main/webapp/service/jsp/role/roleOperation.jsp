<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title><c:if test="${method eq 'add' }">新增</c:if><c:if test="${method eq 'update' }">修改</c:if>角色信息</title>
<style type="text/css">
	table {
	    font-family: "Microsoft Yahei", '微软雅黑', Helvetica, Arial, sans-serif;
	    font-size: 12px; 
	}
</style>
<script type="text/javascript">
	var shareInfoIni = {};
	//非注释，将已经分享的信息加载到JS中
	//<c:forEach var="shareItem" items="${shareIds}">
		shareInfoIni['${shareItem}']={roleId:'${shareItem}'};
	//</c:forEach>
</script>
	
</head>
<body class="sub-page">
	<form id="roleAddForm" name="roleAddForm"  method="post" class="form-horizontal">
		<!-- 隐藏属性区域 -->
		<input type="hidden"  name='method' value="${method }" >
		<input type="hidden"  name='roleId' id='roleId' value="${roleVo.roleId }" >
		<input type="hidden"  name='hiEmpIds' value="${roleVo.empIds }" >
		<input type="hidden"  name='hiEmpsName' value="${roleVo.empsName }" >
		<input type="hidden"  name='hiIsSendMail' value="${roleVo.isSendMail}" >
		
		<div class="modal-content">
			<div class="modal-header">
		        <h4 class="modal-title"><c:if test="${method eq 'add' }">新增</c:if><c:if test="${method eq 'update' }">修改</c:if>角色信息</h4>
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
		                		<td colspan="3" class="white-bg form-inner">
		                			<input name="roleName" type="text" placeholder="角色名称" class="form-control" value="${roleVo.roleName }"/>
		                		</td>
		                	</tr>
		                	<tr>
		                		<td>员工姓名&nbsp;<span class="text-danger">*</span></td>
		                		<td colspan="3" class="white-bg form-inner">
		                			<select name="empIds" id="empIds" placeholder="员工姓名" multiple="multiple" class="form-control use-select2"></select>
		                		</td>
		                	</tr>
		                	<tr>
		                		<td>是否发送邮件</td>
		                		<td colspan="3" class="white-bg form-inner" style="padding-left: 18px;">
		                			<input name="isSendMail" id="isSendMail" type="checkbox" value="Y">是否发送邮件
		                		</td>
		                	</tr>
		                	<tr>
		                		<td>分享权限列表&nbsp;</td>
		                		<td colspan="3" class="white-bg form-inner">
									<div class="col-sm-10 control-label" id="shareInfoContainer">
									</div>
		                		</td>
		                	</tr>
		                	<tr class="modal-buttDiv">
								<td class="form-table-td-button" colspan="4" align="left">
									<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs"  data-loading-text="<i class='ico-loading'></i>" id="subBtn" name="subBtn" onclick="roleAddFunc.submitAddForm();">提交</button>
									<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear"onclick="window.close();">取消</button>
							   </td>
					   		</tr>
		                </tbody>
		            </table>
		         </div>
		    </div>
		</div>    
	</form>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/role/roleOperation.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/role";
		setPath(primaryPath,basePath);
</script>
</body>
</html>