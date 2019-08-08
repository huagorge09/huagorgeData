<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>
<head>
<title>综合类查询</title>
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
						  <a href="getDsReportLink.xhtml?permissionId=8808" target="_blank"><b>业务受理汇总表</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8809" target="_blank"><b>直销手续费收入报表</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8810" target="_blank"><b>销售适用性不符记录汇总表</b></a>
						</label>
				   </div>
			  </td>
			</tr>
			
			<tr>
			   <td class="white-bg">
				   <div class="col-sm-10 form-inner">
						<label>
						<a href="getDsReportLink.xhtml?permissionId=8811" target="_blank"><b>客户证件情况一览表</b></a>
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