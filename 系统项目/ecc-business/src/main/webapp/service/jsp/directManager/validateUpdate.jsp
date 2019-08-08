<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta HTTP-EQUIV="pragma" CONTENT="no-cache"> 
	<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
	<meta HTTP-EQUIV="expires" CONTENT="0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>证件有效期修改-直销管理-直销柜台</title>
	<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
</head>
<body>
<form name="validateUpdateForm" id="validateUpdateForm" method="post" action="<%=context%>service/dirctManager/validate/updateValidate.do" class="form-horizontal">
	<fieldset>
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">证件有效期修改</h4>
		</div>
		<div class="modal-body">
		<div id="hiddenParam" style="display: none;">
			<input type="hidden" name="operatorType" value="${dto.operatorType}" />
			<input type="hidden" name="custno" value="${dto.custno}" />
			<input type="hidden" name="invtp" value="${dto.invtp}" />
			<input type="hidden" name="idtp" value="${dto.idtp}" />
			<input type="hidden" name="idno" value="${dto.idno}" />
			<input type="hidden" name="begindate" value="${dto.begindate}" />
			<input type="hidden" name="enddate" value="${dto.enddate}" />
			<input type="hidden" name="idvalidate" value="${dto.idvalidate}" />
		</div>
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
							<td>证件持有人姓名</td>
							<td class="white-bg">${dto.idnm}</td>
	
							<td>客户类别</td>
							<td class="white-bg">${dto.invtpName}</td>
						</tr>
						<tr>
							<td>证件类型</td>
							<td class="white-bg">${dto.idtpName}</td>
	
							<td>证件号码</td>
							<td class="white-bg">${dto.idno}</td>
						</tr>
						
						<tr>
							<td>有效期</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-9">
									<input type="hidden" name="hidIdvalidate" value="${dto.idvalidate }"/>
									<input type="text" name="showIdvalidate" id="showIdvalidate" placeholder="有效期" class="form-control" />
								</div>
								<label class="col-sm-3 input-sm">
				                  	<input type="checkbox" name="defaultEndDat" id="defaultEndDat"/> 长期有效
				                  </label>
							</td>
						</tr>
						<tr>
							<td>电话号码</td>
							<td class="white-bg">${dto.tel}</td>
	
							<td>手机号码</td>
							<td class="white-bg">${dto.mobile}</td>
						</tr>
						<tr>
							<td>传真</td>
							<td class="white-bg">${dto.fax}</td>
	
							<td>email</td>
							<td class="white-bg">${dto.email}</td>
						</tr>
						<tr>
							<td>详细地址</td>
							<td class="white-bg" colspan="3">${dto.addr}</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="modal-footer">
	         <button type="button" class="btn btn-primary btn-save" data-loading-text="保存中..." id="submitBtn" name="submitBtn" onclick="doSubmit()">保存</button>
	         <button type="button" class="btn btn-link" onclick="window.close();" id="closeBtn" name="closeBtn">取消</button>
	    </div>
	</div>
	</fieldset>
</form>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/directManager/validateUpdate.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/dirctManager/validate";
		setPath(primaryPath,basePath);
</script>
</body>
</html>