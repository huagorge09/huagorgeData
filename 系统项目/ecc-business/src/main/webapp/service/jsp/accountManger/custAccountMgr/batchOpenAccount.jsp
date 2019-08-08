<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="nocache" content="no-cache">
<title>批量开户</title>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<style type="text/css">
.sub-page .modal-content{
	min-height: 300px;
	height: 450px;
}
.modal{
	background-color: rgba(0, 0, 0, 0.4);
}
.ui-jqgrid .ui-jqgrid-bdiv{
	display: flex;
}
</style>
<script type="text/javascript">
var projectPath = '<%=context%>';
//默认加载
$(function(){
	//把访问路径传到js
	setPath(projectPath);
});
</script>
</head>
<body>
<div style="margin: 0 auto;width: 95%">
	<input type="hidden" id="accountData" name="accountData"/>
	<form action="" method="post" id="submitFrom" name="submitFrom" enctype="multipart/form-data">
		<div class="input-group" style="padding: 18px 20px;">
			<input id="errorFileName" type="hidden">
			<input id="lefile" type="file" name="lefile" style="display: none">
			<span class="input-group-addon" onclick="$('#lefile').click();" style="cursor: pointer; background-color: #e7e7e7; max-height: 35px;line-height: 1px;">
			<i class="fa fa-folder-open"></i>选择文件:</span>
			<input id="photoCover" name="todo" readonly="readonly" onclick="$('#lefile').click();" style="width: 300px;background: #fff;" class="form-control" type="text">
			<button class="btn btn-primary" style="margin-top: 0px;margin-left: 3px;" type="button" id="importData" onclick="importExcel();">上&nbsp;&nbsp;传</button>&nbsp;&nbsp;
			<button class="btn btn-primary" style="margin-top: 0px;margin-left: 3px;" type="button" id="downLoad" onclick="downLoadOrderTemp();">模板下载</button>
		</div>
	</form>
	<div class="jqGrid_wrapper fullscreen-wrapper">
	    <table id="accountInfoList"></table>
	    <div id="accountInfoPage"></div>
	</div>
	<div class="page-footer" style="margin-top: 30px;width: 100%;text-align: center;">
		<button type="button" class="btn btn-primary" onclick="submitData();">确认</button>&nbsp;&nbsp;
		<button type="button" class="btn btn-outline" data-loading-text="<i class='ico-loading'></i>" onclick="window.location.reload();">取消</button>
	</div>
</div>

<!-- 模态弹出窗内容 -->
<div class="modal" id="mymodal-data" aria-labelledby="mySmallModalLabel" aria-hidden="true">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<button type="button" class="close" data-dismiss="modal">
					<span aria-hidden="true">&times;</span><span class="sr-only">Close</span>
				</button>
				<h4 class="modal-title">开户导入结果</h4>
			</div>
			<div class="modal-body">
				<div id="msgDiv" class="col-md-4" style="width: 100%;height: 300px;margin: 20px 0;overflow-x: hidden;overflow-y: auto;">
					
				</div>
			</div>
			<div class="modal-footer" style="text-align: center;">
				<button type="button" class="btn btn-primary" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
<body>
<script type="text/javascript" src="<%=context%>web/js/accountManger/custAccountMgr/batchOpenAccount.js?v="<%=dateStr%>></script>
</html>