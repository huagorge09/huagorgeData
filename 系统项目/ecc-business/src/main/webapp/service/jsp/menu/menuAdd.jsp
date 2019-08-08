<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/menu/menu.create.js"></script>
<script type="text/javascript">
	var PATH_PREFIX = '<%=context%>service/menu/';
</script>
</head>

<body class="sub-page">
	<title>新增菜单基本信息</title>
	<!-- 系列产品新建模态窗 -->
	<form id="menuAddForm" role="form" class="form-horizontal" method="post" action="<%=context%>service/menu/saveMenu.do">
		<input type="hidden" name="status" id="status" value="1">
		<!-- 如果将表单禁用，就给fieldset添加disabled属性 -->
		<fieldset>

			<div class="page-content">
				<div class="page-header">
					<h4 class="page-title">新增菜单基本信息</h4>
				</div>
				<div class="page-body">
					<div class="form-group">
						<label class="col-sm-2 control-label">复制菜单</label>
						<div class="col-sm-4">
							<input type="hidden" name="copyMenuId" id="copyMenuId" value="">
							<div class="input-group">
								<select type="text" name="srcMenuId" id="srcMenuId"></select>
								<p class="input-group-btn">
									<button type="button" class="btn btn-primary" name="btnCopyMenu" onclick="copyMenu();">复制</button>
								</p>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单名称<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入菜单名称" class="form-control" name="name" id="name" value="" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单链接<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入菜单链接" class="form-control" name="url" id="url" value="" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单图标</label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入菜单图标" class="form-control" name="icon" id="icon" value="" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单序号<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入菜单序号" class="form-control" name="sequence" id="sequence" value="${sequence}" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">菜单类型<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<select name="type" id="type" class="form-control use-select2" >
								<option value="0">普通菜单</option>
								<!-- <option value="1">普通按钮</option> -->
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">所属系统<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<select name="systemClassify" id="systemClassify" class="form-control use-select2" >
								<option value="BUSINESS">直销柜台(business)</option>
								<option value="CAPITAL">清算平台(capital)</option>
							</select>
						</div>
					</div>
					<div class="form-group">
						<label class="col-sm-2 control-label">父菜单<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<select name="parentId" id="parentId" class="form-control use-select2" >
								<option value="0">无</option>
								<c:forEach var="obj" items="${rootMenu }">
									<option value="${obj.menuId }">${obj.name }</option>
									<c:forEach var="o" items="${obj.childs }">
										<option value="${o.menuId }">|-&nbsp;${o.name }</option>
										<c:forEach var="c" items="${o.childs }">
											<option value="${c.menuId }">&nbsp;&nbsp;&nbsp;|--&nbsp;${c.name }</option>
										</c:forEach>
									</c:forEach>
								</c:forEach>
							</select>
						</div>
					</div>
					<!-- <div class="form-group"  id="descGroup" >
						<label class="col-sm-2 control-label">按钮描述<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入按钮描述" class="form-control" name="desc" id="desc" value="" />
						</div>
					</div>
					<div class="form-group"  id="codeGroup">
						<label class="col-sm-2 control-label">按钮代码<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="请输入按钮代码" class="form-control" name="code" id="code" value="" />
						</div>
					</div> -->
				</div>
				<div class="page-footer">
					<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" data-loading-text="<i class='ico-loading'></i>" onclick="submitMenuAddForm();">提交</button>
					<button type="button" class="btn btn-link btn-w-xs" onclick="window.close();">取消</button>
				</div>
			</div>
		</fieldset>
	</form>

</body>