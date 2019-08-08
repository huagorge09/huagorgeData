<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; UTF-8">
<title>资料上传-资料管理-直销柜台</title>
<style type="text/css">
.control-input{
	background: #fff;
    width: 250px;
    height: 38px;
    border: 1px solid #e7e7e7;
    padding: 0 10px;
}
.btn-primary{
	margin-left: 20px;
	width: 100px;
}
.input-group{
	line-height: 41px;
}
</style>
</head>
<body class="fixed-nav gray-bg">
<form action="/capitalService/server/importData.xhtml" method="post" id="submitFrom" name="submitFrom" enctype="multipart/form-data" >
<div style="display: none">
	<input name="appserialno" type="hidden" value="${appserialno}"/>
	<input name="fundacct" type="hidden" value="${fundacct}"/>
	<input name="custno" type="hidden" value="${custno}"/>
</div>
<div class="permissionBtn" style="display: none;" id="custDataDocDelBtn">
	<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="删除" disabled ><i class="fa fa-trash-o"></i></a>
</div>
<table class="table table-bordered" style="width: 99%;margin: 0 auto;">
	<colgroup>
        <col width="80%">
    </colgroup>
    <tbody>
    	<tr>
    		<td>
    			<h4>添加附件</h4>
    		</td>
    	</tr>
    </tbody>
    <tbody style="border-top: 0px;">
	  	<tr>
	  		<td>
				<div class="col-md-4 input-group" style="width: 600px;">
					<input id="lefile" type="file" name ="lefile" style="display: none">
					<span class="input-group-addon" onclick="$('input[id=lefile]').click();" style="cursor: pointer; background-color: #e7e7e7; max-height: 35px;line-height: 1px;">
					<i class="fa fa-folder-open"></i>选择文件:</span>
					<input id="photoCover" name="todo" readonly="readonly" onclick="$('input[id=lefile]').click();$('#importResult-BG').html('');" class="control-input" type="text">
					<button class="btn btn-primary" type="button" id="importData" onclick="importDataFun();">上&nbsp;&nbsp;传</button>
				</div>
			</td>
	  	</tr>
	</tbody>
</table>
</form>

<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="custDataDocList"></table>
    <div id="custDataDocPage"></div>
</div>

</body>
<script type="text/javascript" src="<%=context%>web/js/dataManager/custDataManager/uploadCustDataDoc.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/custDataManager";
		setPath(primaryPath,basePath);
</script>
</html>