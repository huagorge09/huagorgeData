<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>
<head>
<title>交易类查询</title>
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
						<a href="getDsReportLink.xhtml?permissionId=8803" target="_blank"><b>交易情况统计表</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8819" target="_blank"><b>基金转换确认回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8820" target="_blank"><b>基金转换受理回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8821" target="_blank"><b>基金赎回确认回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8822" target="_blank"><b>基金赎回受理回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8823" target="_blank"><b>基金申购确认回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8824" target="_blank"><b>基金申购受理回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8826" target="_blank"><b>基金认购确认回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8825" target="_blank"><b>基金认购受理回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8831" target="_blank"><b>转托管转出确认回单(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8888" target="_blank"><b>基金申购确认回单(T+1)  (凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8889" target="_blank"><b>基金赎回确认回单(T+1)(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8890" target="_blank"><b>基金转换确认回单(T+1)(凭证)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8891" target="_blank"><b>收益明细表(凭证)</b></a>
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