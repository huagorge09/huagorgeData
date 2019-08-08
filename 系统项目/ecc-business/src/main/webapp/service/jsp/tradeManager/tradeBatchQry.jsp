<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易类批量复核</title>
<style type="text/css">
.modal-content{
	border: 0px solid #e2e2e2;
    box-shadow: 0 0px 0px rgba(0, 0, 0, 0.3);
}
.sub-page{
	background-clip: padding-box;
    background-color: #f5f8f8;
    border: 1px solid #e2e2e2;
    box-shadow: 0 0px 0px rgba(0, 0, 0, 0.3);
}
.form-control{
	width: 250px !important;
}
.select2-container{
	width: 250px !important;
}

.act_count{
	width: 100%;
    height: 50px;
    margin-bottom: 15px;
    background-color: #D6D4D4;
}
.pv_con{
	width: 12%;
    float: left;
    background-color: #D6D4D4;
    color: #000;
    text-align: center;
    height: 50px;	
}

</style>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	<div class="permissionBtn" style="display: none;" id="operationBtn">
		<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="进行复核" disabled ><i class="iconfont icon-wodeshenpi-copy"></i></a>
	</div>
	
	<div class="form-action text-left">
		<button class="btn btn-primary" type="submit"  id="batchY"  onclick="doSubmit('Y','复核通过');">批量复核</button>
		<button class="btn btn-primary" type="submit"  id="batchR"  onclick="doSubmit('R','驳回');">批量驳回</button>
		<button class="btn btn-primary" type="submit"  id="batchC"  onclick="doSubmit('C','作废');">批量作废</button>
		<button class="btn btn-outline btn-primary" onclick="closePage();" id="resetBtn" type="reset">返回</button>
		<input type="hidden" id="dsapkind" value="${dsapkind}"/>
		<input type="hidden" id="fundid" value="${fundid}"/>
		<input type="hidden" id="trustType" value="${trustType}"/>
		<input type="hidden" id="begindate" value="${begindate}"/>
		<input type="hidden" id="enddate" value="${enddate}"/>
		<input type="hidden" id="opid" value="${opid}"/>
		<input type="hidden" id="operatorId" value="${operatorId}"/>
	</div>
</div>
<div class="pv">
   <div class="act_count">
       <div class="pv_con left">
           <h4>复核作废</h4>
           <span class="pv_con_count" id="fcount">0</span>
       </div>
       <div class="pv_con left">
           <h4>未处理</h4>
           <span class="pv_con_count" id="ncount">0</span>
       </div>
       <div class="pv_con left">
           <h4>驳回修改</h4>
           <span class="pv_con_count" id="rccount">0</span>
       </div>
       <div class="pv_con left">
           <h4>驳回放弃</h4>
           <span class="pv_con_count" id="rfcount">0</span>
       </div>
       <div class="pv_con left">
           <h4>复核成功</h4>
           <span class="pv_con_count" id="ycount">0</span>
       </div>
       <div class="pv_con left">
           <h4>撤单</h4>
           <span class="pv_con_count" id="cancelcount">0</span>
       </div>
       <div class="pv_con left">
           <h4>总数</h4>
           <span class="pv_con_count" id="totalcount">0</span>
       </div>
   </div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="tradeQryList"></table>
    <div id="tradeQryPage"></div>
</div>
</body>
<script type="text/javascript" src="<%=context%>web/js/tradeManager/tradeBatchQry.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>capitalService/server";
		setPath(primaryPath,basePath);
</script>
</html>