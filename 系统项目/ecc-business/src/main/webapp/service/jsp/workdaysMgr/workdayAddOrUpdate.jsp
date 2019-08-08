<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><c:if test="${method eq 'add' }">新增</c:if><c:if test="${method eq 'update' }">修改</c:if>工作日</title>
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
</style>
</head>
<body class="sub-page">
<form id="workDayForm" action="" name="workDayForm"  method="post" class="form-horizontal">
	<input id="transCode" type='hidden' value="9320" />
	<div class="modal-content">
		<div class="modal-header">
	        <h4 class="modal-title"><c:if test="${requestScope.method eq 'add' }">新增工作日</c:if><c:if test="${requestScope.method eq 'update' }">修改工作日</c:if></h4>
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
	                <c:if test="${method eq 'add' }">
	                <tbody>
	                	<tr>
	                		<td><span class="text-danger">*</span>基金代码</td>
	                		<td class="white-bg form-inner">
	                			<select name="fundid" id="fundid_add" class="form-control use-select2" ></select>
	                		</td>
	                		<td><span class="text-danger">*</span>工作日日期</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="工作日日期" class="form-control" id="workdate_add" name="workdate">
	                		</td>
	                	</tr>
	                	<tr>
							<td class="form-table-td-button" colspan="4" align="center">
								<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs"  data-loading-text="<i class='ico-loading'></i>" id="subBtn" name="subBtn" onclick="saveWorkDay();">提交</button>
								<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear"onclick="window.close();">取消</button>
						   </td>
				   		</tr>
	                </tbody>
	                </c:if>
	                <c:if test="${method eq 'update' }">
	                <tbody>
	                	<tr>
	                		<td><span class="text-danger">*</span>基金代码</td>
	                		<td class="white-bg form-inner">
	                			<input id = "workflag" name="workflag" type="hidden" value="${workdaysVo.workflag}">
	                			<input id = "workdate" name="workdate" type="hidden" value="${workdaysVo.workdate}">
	                			<input type="text" placeholder="基金代码" readonly="readonly" class="form-control" name="fundid" value="${workdaysVo.fundid}">
	                		</td>
	                		<td colspan="2" class="white-bg form-inner"></td>
	                	</tr>
	                	<tr>
	                		<td><span class="text-danger">*</span>工作日名称</td>
	                		<td class="white-bg form-inner">
	                			<select name="workflagnm" id="workflagnm" class="form-control use-select2" >
									<option value="Y" <c:if test="${workdaysVo.workflag=='Y'}">selected</c:if>>工作日</option>
	                				<option value="N" <c:if test="${workdaysVo.workflag!='Y'} ">selected</c:if>>节假日</option>
								</select>
	                		</td>
	                		<td><span class="text-danger">*</span>工作日日期</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="工作日日期" readonly="readonly" class="form-control" name="workdateFmt" value="${workdaysVo.workdateFmt}">
	                		</td>
	                	</tr>
	                	<tr>
							<td class="form-table-td-button" colspan="4" align="center">
								<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs"  data-loading-text="<i class='ico-loading'></i>" id="subBtn" name="subBtn" onclick="updateWorkDay();">提交</button>
								<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear"onclick="window.close();">取消</button>
						   </td>
				   		</tr>
	                </tbody>
	                </c:if>
				</table>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript" src="<%=context%>web/js/workdaysMgr/workdaysManager.js"></script>
<script type="text/javascript" src="<%=context%>web/js/fund/common.js?20180604"></script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/workdaysManager";
	setPath(primaryPath,basePath);
</script>
<script type="text/javascript">
$('#workflagnm').select2({allowClear: false,minimumResultsForSearch:Infinity});
loadFundAllInfo();
$('#fundid_add').select2({allowClear: false});
WASP_WIDGET.initializeSelectVal('fundid_add');
</script>
</body>
</html>