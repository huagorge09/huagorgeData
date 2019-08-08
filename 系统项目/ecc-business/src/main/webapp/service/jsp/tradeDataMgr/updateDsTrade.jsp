<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>修改交易资料-资料管理-直销柜台</title>
</head>
<body class="fixed-nav gray-bg">
	<div class="modal-header">
		<h4 class="page-title">修改交易资料</h4>
	</div>
	<div class="modal-content" style="border: 0px;">
	 	<form name="updateCustDataFrom" id="updateCustDataFrom" method="post" action="<%-- <%=context%>service/custDataManager/updateCustDataManagerInfo.do --%>">
	 	<input type="hidden" name="serialno" id="serialno" value="${dsTradeDto.serialno }"/>
		<div class="page-body">
			<!-- 资料信息 -->
			<div id="documentInfoContent" class="typeContent">
				<table class="table table-bordered maintable" id="documentInfo">
					<colgroup>
					  	<col width="20%">
					  	<col width="30%">
					  	<col width="20%">
					  	<col width="30%">
					</colgroup>
					<tbody>
						<tr>
							<td <c:if test = "${dsTradeDto.apkind != '020' && dsTradeDto.apkind != '022' && dsTradeDto.apkind != '036' }">style="display:none"</c:if>>合同签署：</td>
							<td class="white-bg" <c:if test = "${dsTradeDto.apkind != '020' && dsTradeDto.apkind != '022' && dsTradeDto.apkind != '036' }">style="display:none;"</c:if>>
								<div class="col-sm-11 form-inner">
									<select name="contractsign" id="contractsign" class='form-control user-select2' onchange="showdiv()">
										<option value = ""></option>
										<c:forEach items="${contractList}" var="item">
											<option value="${item.pmco}" pmco = "${item.pmco}" contractsign = '${dsTradeDto.contractsign}' <c:if test="${item.pmco == dsTradeDto.contractsign}">selected</c:if> >${item.pmnm}</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td id = "isoriginaldiv"
								<c:choose> 
									<c:when test = "${dsTradeDto.apkind != '020' && dsTradeDto.apkind != '022' && dsTradeDto.apkind != '036' }">style="display:none"</c:when>
									<c:when test = "${dsTradeDto.contractsign == '0' || dsTradeDto.contractsign == '2' || dsTradeDto.contractsign == '' }">style="display:none;"</c:when>
								</c:choose>
							>合同是否原件：</td>
							<td id =  'isoriginal2' class="white-bg" <c:if test = "${dsTradeDto.apkind != '020' && dsTradeDto.apkind != '022' && dsTradeDto.apkind != '036' }">style="display:none;"</c:if>>	
								<div class="col-sm-11 form-inner">
									<select name="isoriginal" id="isoriginal" class='form-control select2_width'>
 										<option value="" <c:if test="${dsTradeDto.isoriginal == ''}">selected</c:if>></option>
										<option value="N" <c:if test="${dsTradeDto.isoriginal == 'N'}">selected</c:if>>否</option>
										<option value="Y" <c:if test="${dsTradeDto.isoriginal == 'Y'}">selected = 'true'</c:if>>是</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>合同是否移交：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="contractdevolve" id="contractdevolve" class='form-control select2_width'>
										<option value="N" <c:if test="${dsTradeDto.contractdevolve == 'N'}">selected</c:if>>否</option>
										<option value="Y" <c:if test="${dsTradeDto.contractdevolve == 'Y'}">selected</c:if>>是</option>
									</select>
								</div>
							</td>
							<td>是否归档：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="filed" id="filed" class='form-control select2_width'>
										<option value="N" <c:if test="${dsTradeDto.filed == 'N'}">selected</c:if>>否</option>
										<option value="Y" <c:if test="${dsTradeDto.filed == 'Y'}">selected</c:if>>是</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>交易表单是否原件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="istradeform" id="istradeform" class='form-control select2_width'>
										<option value="N" <c:if test="${dsTradeDto.istradeform == 'N'}">selected</c:if>>否</option>
										<option value="Y" <c:if test="${dsTradeDto.istradeform == 'Y'}">selected</c:if>>是</option>
									</select>
								</div>
							</td>
						</tr>
					<tbody>
						<tr>
							<td>备注：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<textarea rows="3" cols="50" class='form-control' name="remark" id="remark">${dsTradeDto.remark}</textarea>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="page-footer" style="margin-top: 20px;">
			<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提交" name='btnSubmit' onclick="doSubmit()">提交</button>
			<button type="button" class="btn btn-link" onclick="window.close();" id="closeBtn" name="closeBtn">取消</button>
		</div>
		</form>
	</div>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js?20180625"></script>
<script type="text/javascript" src="<%=context%>web/js/tradeDataMgr/tradeDataMgr.update.js?20180628"></script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/tradeDataManager";
	setPath(primaryPath,basePath);
</script>
</body>
</html>