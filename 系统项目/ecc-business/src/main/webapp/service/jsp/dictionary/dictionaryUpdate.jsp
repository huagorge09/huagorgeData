<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>

<head>
<!-- 这里引入导航信息header.tpl -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/jquery.upload.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/dictionary.create.js"></script>
</head>

<body class="sub-page">
	<title>修改系统字典</title>
	<!-- 修改系统字典新建模态窗 -->
	<form id="dictionaryUpdateForm" role="form" class="form-horizontal" method="post" action="<%=context%>service/dictionary/updateDictionary.do">
		<!-- 如果将表单禁用，就给fieldset添加disabled属性 -->
		<fieldset>
		 <input type="hidden" name="dctId" value="${dictionaryVo.dctId}"/> 
			<div class="page-content">
				<div class="page-header">
					<h4 class="page-title">修改系统字典</h4>
				</div>
				<div class="page-body">
					<div class="form-group">
						<label class="col-sm-2 control-label">字典根类型<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="字典根类型" class="form-control" name='dctRootType' id='dctRootType' value="${dictionaryVo.dctRootType}" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">字典父类型<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="字典父类型" class="form-control" name='dctFathType' id='dctFathType' value="${dictionaryVo.dctFathType}" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">字典子类型<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="字典子类型" class="form-control" name='dctLeftType' id='dctLeftType' value="${dictionaryVo.dctLeftType}" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">字典名<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="字典名" class="form-control" name='dctName' id='dctName' value="${dictionaryVo.dctName}" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">字典值<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="字典值" class="form-control" name='dctValue' id='dctValue' value="${dictionaryVo.dctValue}" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">排序号<span class="text-danger"></span></label>
						<div class="col-sm-10 form-inner">
							<input type="text" placeholder="排序号" class="form-control" name='dctSortNo' id='dctSortNo' value="${dictionaryVo.dctSortNo}" />
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">是否叶子<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<select name='dctIsLeaf' id='dctIsLeaf' class="form-control" placeholder="是否叶子">
							  <!--  <option value="">全部</option>
						       <option value="Y">是</option>
						       <option value="N">否</option> -->
						    <option value='Y' ${dictionaryVo.dctIsLeaf == 'Y'?"selected":""  }>是</option>
							<option value='N' ${dictionaryVo.dctIsLeaf == 'N'?"selected":""  }>否</option>
								<%-- <c:forEach var="dctIsLeaf" items="${dctDat }">
									<option value="${dctIsLeaf.dctValue }" ${dictionaryVo.dctIsLeaf == dctIsLeaf.dctValue?"selected":"" }>${dctIsLeaf.dctName }</option>
								</c:forEach> --%>
							</select>
						</div>
					</div>

					<div class="form-group">
						<label class="col-sm-2 control-label">是否生效<span class="text-danger">*</span></label>
						<div class="col-sm-10 form-inner">
							<select name='dctStat' id='dctStat' class="form-control" placeholder="是否生效">
							  <!--  <option value="">全部</option>
						       <option value="Y">是</option>
						       <option value="N">否</option> -->
						       <option value='Y' ${dictionaryVo.dctStat == 'Y'?"selected":""  }>是</option>
							   <option value='N' ${dictionaryVo.dctStat == 'N'?"selected":""  }>否</option>
								<%-- <c:forEach var="dctStat" items="${dctDat }">
									<option value="${dctStat.dctValue }" ${dictionaryVo.dctStat == dctStat.dctValue?"selected":"" }>${dctStat.dctName }</option>
								</c:forEach> --%>
							</select>
						</div>
					</div>

				</div>
				<div class="page-footer">
					<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="commitBtn" name="commitBtn" data-loading-text="<i class='ico-loading'></i>" onclick="submitDictionaryUpdateForm();">提交</button>
					<button type="button" class="btn btn-link btn-w-xs" id="cancelBtn" name="cancelBtn" onclick="window.close();">取消</button>
				</div>
			</div>
		</fieldset>
	</form>
</body>