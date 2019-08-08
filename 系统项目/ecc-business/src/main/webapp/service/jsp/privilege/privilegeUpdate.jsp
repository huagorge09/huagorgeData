<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>修改权限设置</title>
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
	                        <td colspan="3">
								<!-- 选框控件 -->
								<div class="multiselect clearfix" style="width:100%;">
									<!-- 待选列表 -->
									<div class="multiselect-options" style="width:45%;">
										<div class="options-header">
										搜索：
											<input type="text" id="resName" name="resName" class="form-control multiselct-input" value="">
											<a href="javascript:void(0)" id="queryBtn" class="btn btn-primary btn-mulit-search">查询</a>
										</div>
										<div class="options-body">
										</div>
									</div>
									<!-- 移位按钮 -->
									<div class="multiselect-actions" style="width:10%;">
										<a href="javascript:void(0)" class="btn btn-default btn-sm multiselect-toright" title="置右"><i class="iconfont icon-shideright39"></i></a>
					        	 		<a href="javascript:void(0)" class="btn btn-default btn-sm multiselect-goright" title="右移"><i class="iconfont icon-jiantouarrow487"></i></a>
					        	 		<a href="javascript:void(0)" class="btn btn-default btn-sm multiselect-goleft" title="左移"><i class="iconfont icon-jiantouarrowhead7"></i></a>
					        	 		<a href="javascript:void(0)" class="btn btn-default btn-sm multiselect-toleft" title="置左"><i class="iconfont icon-jiantouarrowheads3"></i></a>
									</div>
									<!-- 已选列表 -->
									<div class="multiselect-selected" style="width:45%;">
										<div class="selected-header">已选区</div>
					           	    	<div class="selected-body">
					           	    	<c:forEach var="resourceItem"  varStatus="status"  items="${resourceList}">
											<div class="multiselect-item" id="${resourceItem.resId}" sNames="${resourceItem.resName}">${resourceItem.resName}<button type="button" class="mulitselect-remove">×</button></div>
										</c:forEach>
					           	    	</div>
					           	    	<div class="selected-actions clearfix">
					           	    		<button type="button" class="pull-right multiselect-totop" title="置顶"><i class="iconfont icon-angledoubleup"></i></button>
					           	    		<button type="button" class="pull-right multiselect-godown" title="下移"><i class="iconfont icon-angledown"></i></button>
					           	    		<button type="button" class="pull-right multiselect-goup" title="上移"><i class="iconfont icon-angleup"></i></button>
					           	    		<button type="button" class="pull-right multiselect-tobottom" title="置底"><i class="iconfont icon-angledoubledown"></i></button>
					           	    	</div>
									</div>
								</div>
	                        </td>
	                    </tr>
	                    
		                	<tr class="modal-buttDiv">
								<td class="form-table-td-button" colspan="4" align="left">
									<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs"  data-loading-text="<i class='ico-loading'></i>" id="subBtn" name="subBtn" onclick="save();">提交</button>
									<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear"onclick="window.close();">取消</button>
							   </td>
					   		</tr>
		                </tbody>
		            </table>
		         </div>
		    </div>
		</div>    
	</form>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/multiselect/index.js"></script>
<script type="text/javascript" src="<%=context%>web/js/common/MapAndSet.js"></script>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/privilege/privilegeUpdate.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/privilege";
		setPath(basePath,primaryPath);
</script>
</body>
</html>