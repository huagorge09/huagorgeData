<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
#pageTitle{
	text-align:center;
	margin-bottom : 20px;
}
#brokerModifyForm tbody label{
	font-weight: normal;
}
</style>
<title>客户评估数据详情</title>
</head>
<body class="fixed-nav gray-bg">
	<div class="modal-content" style="height:110%;">
		<div align="center">
		<h4 class="modal-title" id="pageTitle">客户评估数据详情</h4>
		<form name="brokerModifyForm" id ="brokerModifyForm" class="form-horizontal" action="" method="post">
			<input type="hidden" id="hidregioncode" name="hidregioncode" value="${riskLevelDto.regioncode}" /> 
			<input type="hidden" id="risklevel" name="risklevel" value="${riskLevelDto.risklevel}" /> 
			<input type="hidden" id="fileno" name="fileno" value="${riskLevelDto.voicerecord }" /> 
			<input type="hidden" id="hidinvtp" name="hidinvtp" value="${riskLevelDto.invtp}" /> 
			<input type="hidden" id="evalScope" name="evalScope"  />
			<input type="hidden" id="evalAnswer" name="evalAnswer"  /> 
			<input type="hidden" id="dtoAnswer" name="dtoAnswer" value="${riskLevelDto.answer}"  /> 
			<input type="hidden" id="specriskLevel" name="specriskLevel"  />
			<input type="hidden" id="hidrisklevel" name="hidrisklevel"  /> 
			<input  type="hidden" id="invprtp" name="invprtp" value="${riskLevelDto.invprtp}" /> 
			<input type="hidden" id="hidappst" name="hidappst" value="${riskLevelDto.appst}" /> 
			<input type="hidden" id="hidapptp" name="hidapptp" value="${riskLevelDto.apptp}" />
			<input type="hidden" id="hidcustno" name="hidcustno" value="${riskLevelDto.custno}" />
			<table class="table table-bordered">
				<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
				<tbody>
					<tr>
						<th rowspan="3" style="text-align:center;">基本信息</th>
						<td width="20%">客户名称：</td>
						<td width="30%" >${riskLevelDto.invnm}</td>
						<td width="20%" >基金账号：</td>
						<td width="30%">${riskLevelDto.fundacc}</td>
					</tr>	
					<tr>
						<td width="20%" class="inputTd">客户类型：</td>
						<td width="30%">${riskLevelDto.invtpName}</td>
					<td width="20%" id="riskTd">风险承受能力：</td>
				    <td width="30%" id="riskSelect">
				  		${riskLevelDto.risklevelName}
				    </td>
					</tr>
					<tr>
						<td width="20%">客户专业类型：</td>
						<td width="30%" class="white-bg">
						<select style="WIDTH: 150px" name="hidinvprtp" id="hidinvprtp" class="form-control use-select2">
								<option value="1" <c:if test="${riskLevelDto.invprtp eq '1'}">selected</c:if> >普通投资者</option>
								<option value="0" <c:if test="${riskLevelDto.invprtp eq '0'}">selected</c:if>>专业投资者</option> 
						</select></td>
						<td id="fileTd" WIDTH="20%">录音文件编号：<br></td>
						<td id="fileTdx" class="white-bg"><input type="text" name="hidfileno"
							id="hidfileno"  value="${riskLevelDto.voicerecord}" class="form-control"/></td>
					</tr>
					<tr>
                		<td colspan="5">
                			<div class="jqGrid_wrapper">
								<input type="hidden">
								<table id="riskChangeLogList"></table>
							<div id="riskChangeLogListPage"></div>
							</div>
						</td>
					</tr>
				</tbody>
				<tbody>
					<tr>
						<td align="center" colspan="5">
							<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear" onclick="window.close();">取消</button>
						</td>
					</tr>
				</tbody>
				</table>
				<div></div>
				<br>
			</form>
		</div>
	</div>
<script type="text/javascript" src="<%=context%>web/js/common/common.js?20181224"></script>
<script type="text/javascript" src="<%=context%>web/js/customerDataManager/customerDataManager.detail.js?20181224"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/customerDataManager";
		setPath(primaryPath,basePath);
</script>
</body>
</html>