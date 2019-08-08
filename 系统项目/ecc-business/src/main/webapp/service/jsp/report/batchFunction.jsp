<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>
<head>
<title>批量功能查询</title>
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
						<label><a href="getDsReportLink.xhtml?permissionId=8870" target="_blank"><b>基金认购确认回单(凭证,批量传真)</b></a></label>
				   </div>
			  </td>
			</tr>
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label><a href="getDsReportLink.xhtml?permissionId=8873" target="_blank"><b>基金申购确认回单(凭证,批量传真)</b></a></label>
				   </div>
			  </td>
			</tr>
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8874" target="_blank"><b>基金赎回确认回单(凭证,批量传真)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8875" target="_blank"><b>基金转换确认回单(凭证,批量传真)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8871" target="_blank"><b>收益结转确认回单(凭证,批量传真)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8872" target="_blank"><b>基金红利发放确认回单(凭证,批量传真)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8882" target="_blank"><b>基金认购确认回单(凭证,批量打印)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8885" target="_blank"><b>基金申购确认回单(凭证,批量打印)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8886" target="_blank"><b>基金赎回确认回单(凭证,批量打印)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8887" target="_blank"><b>基金转换确认回单(凭证,批量打印)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8883" target="_blank"><b>收益结转确认回单(凭证,批量打印)</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8884" target="_blank"><b>基金红利发放确认回单(凭证,批量打印)</b></a>
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