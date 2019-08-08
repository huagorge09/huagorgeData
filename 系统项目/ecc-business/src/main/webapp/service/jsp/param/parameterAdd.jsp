<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%
/*
String permissionId = TransCodeConstant.TRANS_CODE_9340;//本页面操作权限代码
if("ALLPARA".equals(queryType)){
	permissionId = TransCodeConstant.TRANS_CODE_9340;//本页面操作权限代码（系统管理）
}else{
	permissionId = TransCodeConstant.TRANS_CODE_9610;//本页面操作权限代码(业务管理)
}*/
%>
<input id="transCode" type='hidden' value="9340" />
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加参数</title>
</head>
<body class="sub-page">
<form id="parameterForm" role="form" class="form-horizontal" method="post" action="">
 <!-- 如果将表单禁用，就给fieldset添加disabled属性 -->
 <fieldset>
<div class="page-content">
    <div class="page-header">
        <h4 class="page-title">当前参数类型:${opmnm}</h4>
    </div>
    <div class="page-body">
    	<input type="hidden" name="pmst" id="pmst" value="${pmst}">
    	<input type="hidden" name="pmky" id="pmky" value="${pmky}">
          <div class="form-group">
               <label class="col-sm-2 control-label">参数值<span class="text-danger">*</span></label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="参数值：" class="form-control" name="pmco" id="pmco" value="${payChannelDto.pmco}"  />
               </div>
                <label class="col-sm-2 control-label">参数名<span class="text-danger">*</span></label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="参数名：" class="form-control" name="pmnm" id="pmnm" value="${payChannelDto.pmnm}" />
               </div>
          </div>
          
    <div class="page-footer">
         <button type="button" class="btn btn-primary btn-save"  data-loading-text="保存中..." id="submitBtn" name="submitBtn" onclick="doSubmit('add')">保存</button>
          <button type="button" class="btn btn-link"  onclick="window.close();"  id="closeBtn" name="closeBtn">取消</button>
    </div>
</div>
</fieldset>
</form>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js?20180603"></script>
<script type="text/javascript" src="<%=context%>web/js/param/parameter.create.js"></script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/parameterManager";
	setPath(primaryPath,basePath);
</script>

</body>
</html>