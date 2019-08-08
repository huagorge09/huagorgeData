<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta HTTP-EQUIV="pragma" CONTENT="no-cache"> 
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
<meta HTTP-EQUIV="expires" CONTENT="0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>凭证导出-资金管理-直销柜台</title>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
</head>
<body>
<form name="expVoucherDataForm" id="expVoucherDataForm" method="post" action="" class="form-horizontal">
	<fieldset>
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">凭证导出</h4>
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
					<tbody>
						<tr>
							<td>日期</td>
							<td class="white-bg">
								<input type="text" name="workDate" id="workDate" class="form-control"/>
							</td>
	
							<td>批次号</td>
							<td class="white-bg">
								<select name="batId" id="batId" class="form-control user-select2">
									<option value="">无</option>
									<option value="1">1</option>
									<option value="2">2</option>
									<option value="3">3</option>
									<option value="4">4</option>
									<option value="5">5</option>
									<option value="6">6</option>
									<option value="7">7</option>
									<option value="8">8</option>
									<option value="9">9</option>
									<option value="10">10</option>
									<option value="99">99</option>
								</select>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="modal-footer">
	         <button type="button" class="btn btn-primary btn-save" id="submitBtn" name="submitBtn" onclick="doSubmit()">导出</button>
	    </div>
	</div>
	</fieldset>
</form>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/capitalManager/expVoucherData.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/capitalManager";
		setPath(primaryPath,basePath);
</script>
</body>
</html>