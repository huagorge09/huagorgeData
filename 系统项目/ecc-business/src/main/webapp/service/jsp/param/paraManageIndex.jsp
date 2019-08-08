<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>参数列表</title>
</head>

<% 
//String permissionId = TransCodeConstant.TRANS_CODE_9340;//本页面操作权限代码
%>
<input id="transCode" type='hidden' value="9340" />
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	
    <div class="hr-line-dotted"></div>
  <div class="form-item-group form-horizontal" role="form">
       <div class="form-item">
            <span class="form-field">参数类型：</span>
            <span class="form-input">
            	<input type="text" placeholder="参数类型" class="form-control" name="allPmnm" id="allPmnm" value="">
            </span>
        </div>
        <div class="form-item">
            <span class="form-field">参数：</span>
            <span class="form-input">
             	<input type="text" placeholder="参数" class="form-control" name="allPmky" id="allPmky" value="">
            </span>
        </div>
        <div class="form-item">
            <span class="form-field">参数值：</span>
            <span class="form-input">
             	<input type="text" placeholder="参数值" class="form-control" name="qpmco" id="qpmco" value="">
            </span>
        </div>
        <div class="form-item">
            <span class="form-field">参数名：</span>
            <span class="form-input">
             	<input type="text" placeholder="参数名" class="form-control" name="qpmnm" id="qpmnm" value="">
            </span>
        </div>
   		 <div class="form-item"></div>
        <div class="form-item"></div>
    </div>
      <div class="form-action text-right">
          <button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp;查询</button>
          <button class="btn btn-outline btn-primary" id="resetBtn" type="reset">清空</button>
      </div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="parameterList"></table>
    <div id="parameterPage"></div>
</div>
</body>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js?20180603"></script>
<script type="text/javascript" src="<%=context%>web/js/param/parameterList.init.js"></script>
<script type="text/javascript" src="<%=context%>web/js/fund/common.js?20180529"></script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/parameterManager";
	setPath(primaryPath,basePath);
</script>

</html>