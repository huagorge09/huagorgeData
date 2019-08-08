<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>
<head>
<title>客户信息类查询</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body class="fixed-nav gray-bg">

<form name="frmReportIndex" method="post">
	
	
	<table class="table table-bordered maintable" id="documentInfo" style="display: table;">
		<colgroup>
			<col width="100%">
		</colgroup>
		<tbody>
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8801" target="_blank"><b>客户信息查询</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8802" target="_blank"><b>客户账户资料修改列表</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8813" target="_blank"><b>基金账户销户受理回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8814" target="_blank"><b>基金账户销户确认回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8815" target="_blank"><b>基金账户资料变更受理回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8816" target="_blank"><b>基金账户资料变更确认回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8817" target="_blank"><b>基金开户受理回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8818" target="_blank"><b>基金开户确认回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
		</tbody>
	</table>
	
	<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/accountSearch";
		setPath(primaryPath,basePath);
	</script> 

</body>
</html>