<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/menu/menu.update.js"></script>
<script type="text/javascript">
	var PATH_PREFIX = '<%=context%>service/menu/';
</script>
</head>

<body class="sub-page">
	<title>修改菜单基本信息</title>
	<!-- 系列产品新建模态窗 -->
	<form id="menuUpdateForm" role="form" class="form-horizontal" method="post" action="<%=context%>service/menu/updateMenu.do">
		<input type="hidden" name="menuId" id="menuId" value="${menuVo.menuId}">
		<input type="hidden" name="status" id="status" value="${menuVo.status}">
		<!-- 如果将表单禁用，就给fieldset添加disabled属性 -->
		<fieldset>

			<div class="page-content">
				<div class="page-header">
					<h4 class="page-title">修改菜单基本信息</h4>
				</div>
				<div class="page-body">
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单名称<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入菜单名称" class="form-control" name="name" id="name" value="${menuVo.name}" />
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单链接<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入菜单链接" class="form-control" name="url" id="url" value="${menuVo.url}" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单图标</label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入菜单图标" class="form-control" name="icon" id="icon" value="${menuVo.icon}" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单序号<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入菜单序号" class="form-control" name="sequence" id="sequence" value="${menuVo.sequence}" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单类型<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<select name="type" id="type" class="form-control use-select2" >
								<option value="0" <c:if test="${menuVo.type==0}">selected</c:if>>普通菜单</option>
								<%-- <option value="1" <c:if test="${menuVo.type==1}">selected</c:if>>普通按钮</option> --%>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label">所属系统<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<select name="systemClassify" id="systemClassify" class="form-control use-select2" >
								<option value="BUSINESS" <c:if test="${menuVo.systemClassify == 'BUSINESS'}">selected</c:if>>直销柜台(business)</option>
								<option value="CAPITAL"  <c:if test="${menuVo.systemClassify == 'CAPITAL'}">selected</c:if>>清算平台(capital)</option>
							</select>
						</div>
					</div>
					
					<div class="form-group">
						<label class="col-sm-2 control-label">父菜单<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<select name="parentId" id="parentId" class="form-control use-select2" >
								<option value="0" <c:if test="${menuVo.parentId==0}">selected</c:if> >无</option>
								<c:forEach var="obj" items="${rootMenu }">
									<option value="${obj.menuId }" <c:if test="${menuVo.parentId==obj.menuId}">selected</c:if>>${obj.name }</option>
									<c:forEach var="o" items="${obj.childs }">
										<option value="${o.menuId }" <c:if test="${menuVo.parentId==o.menuId}">selected</c:if>>|-&nbsp;${o.name }</option>
										<c:forEach var="c" items="${o.childs }">
											<option value="${c.menuId }" <c:if test="${menuVo.parentId==c.menuId}">selected</c:if> > &nbsp;&nbsp;&nbsp;|--&nbsp;${c.name }</option>
										</c:forEach>
									</c:forEach>
								</c:forEach>
							</select>
						</div>
					</div>
					<%-- <div class="form-group"  id="descGroup" >
						<label class="col-sm-2 control-label">按钮描述<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入按钮描述" class="form-control" name="desc" id="desc" value="${menuVo.desc}" />
						</div>
					</div>
					<div class="form-group"  id="codeGroup">
						<label class="col-sm-2 control-label">按钮代码<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入按钮代码" class="form-control" name="code" id="code" value="${menuVo.code}" />
						</div>
					</div> --%>
					
				</div>
				<div class="page-footer">
					<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" data-loading-text="<i class='ico-loading'></i>" onclick="submitMenuAddForm();">提交</button>
					<button type="button" class="btn btn-link btn-w-xs" onclick="window.close();">取消</button>
				</div>
			</div>
		</fieldset>
	</form>

</body>