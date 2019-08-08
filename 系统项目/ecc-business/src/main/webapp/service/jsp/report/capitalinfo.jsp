<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>
<head>
<title>账户类查询</title>
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
						<label><a href="getDsReportLink.xhtml?permissionId=8804" target="_blank"><b>认购确认明细表</b></a></label>
				   </div>
			  </td>
			</tr>
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8812" target="_blank"><b>申购确认明细表</b></a></label>
				   </div>
			  </td>
			</tr>
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8805" target="_blank"><b>赎回确认明细表</b></a></label>
				   </div>
			  </td>
			</tr>
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8806" target="_blank"><b>分红确认明细表</b></a></label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8807" target="_blank"><b>资金到账明细表</b></a></label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8827" target="_blank"><b>收益结转确认回单(凭证)</b></a></label>
				   </div>
			  </td>
			</tr>
			
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8828" target="_blank"><b>基金红利发放确认回单(凭证)</b></a></label>
				   </div>
			  </td>
			</tr>
			
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8829" target="_blank"><b>基金分红方式变更确认回单(凭证)</b></a></label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8830" target="_blank"><b>基金分红方式变更受理回单(凭证)</b></a></label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8832" target="_blank"><b>直销资金划款对账表</b></a></label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8833" target="_blank"><b>网下汇款对账查询(历史)</b></a></label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8834" target="_blank"><b>线下汇款业务退款明细表</b></a></label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8835" target="_blank"><b>线下汇款挂账统计表</b></a></label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8836" target="_blank"><b>线下汇款业务认(申)购情况明细表</b></a></label>
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