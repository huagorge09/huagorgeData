<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自动生成工作日</title>
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
.selectWid{
	width: 60px;
}
.spanText{
	width: 25px;
    text-align: center;
}
.formCaptionText{
	width: 65px;
}
</style>
</head>
<body class="sub-page">
<form id="workDayForm" action="" name="workDayForm"  method="post" class="form-horizontal">
	<div class="modal-content">
		<input id="transCode" type='hidden' value="9320" />
		<div class="modal-header">
	        <h4 class="modal-title">自动生成工作日</h4>
	    </div>
	    <div class="modal-body">
	    	<div class="table-responsive">
	    		<table id="workDaysTable" class="table table-bordered">
	    			<colgroup><col width="10%"><col width="20%"><col width="60%"><col width="10%"></colgroup>
	                <tbody>
	                	<tr>
	                		<td></td>
	                		<td>年度：</td>
	                		<td class="white-bg form-inner">
	                			<select name="years" id="years" class="select2"></select>
	                		</td>
	                		<td></td>
	                	</tr>
	                	<tr>
	                		<td></td>
	                		<td>是否生成周六周日：</td>
	                		<td class="white-bg form-inner" style="line-height: 20px;">
	                			<div class="radio">
									<label>
										<input name="offday" type="radio" checked="checked" class="ace" value="Y"/>
										<span class="lbl">是</span>
									</label>
									<label>
										<input name="offday" type="radio" class="ace" value="N"/>
										<span class="lbl">否</span>
									</label>
								</div>
	                		</td>
	                		<td></td>
	                	</tr>
	                </tbody>
				</table>
				<table class="table">
	                <tbody>
	                	<tr>
	                		<td style="border: 0px;">
	                			<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="btnClear" name="btnClear"onclick="addForm('',0,0,0,0);">添加自定义区间</button>
	                		</td>
	                		<td colspan="2" style="text-align: center;border: 0px;">注意：每年当中除了节假日就是工作日</td>
	                	</tr>
						<tr>
							<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
								<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="subBtn" name="subBtn" onclick="autoSaveWorkDay();">提交</button>
								<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear"onclick="window.close();">取消</button>
						   </td>
				   		</tr>
		   			</tbody>
				</table>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript" src="<%=context%>web/js/workdaysMgr/workdaysManager.js"></script>
<script type="text/javascript" src="<%=context%>web/js/fund/common.js?20180605"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/workdaysManager";
		setPath(primaryPath,basePath);
</script>
<script type="text/javascript">
	loadYears();
	addForm("元旦",1,1,1,3);
	addForm("春节",2,6,2,12);
	addForm("清明节",4,4,4,6);
	addForm("五一劳动节",5,1,5,3);
	addForm("端午节",6,7,6,9);
	addForm("中秋节",9,13,9,15)
	addForm("国庆节",9,29,10,5);
	$('.select2').select2({allowClear: false,minimumResultsForSearch:Infinity});
</script>
</body>
</html>