<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易类驳回修改</title>
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
.form-control{
	width: 200px !important;
}
.select2-container{
	width: 230px !important;
}
.rowHeigth{
	line-height: 34px !important;
}
.rowHeigth input{
	float: left !important;
	margin-right: 10px !important;
}
label.error{
	margin-top: 0px !important;
}
</style>
</head>
<body class="sub-page">
<form id="tradeModifyDetailForm" action="" name="tradeModifyDetailForm"  method="post" class="form-horizontal">
	<div class="modal-content">
		<div>
	        <h4 class="modal-title">交易类驳回修改</h4>
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
	                	<tr style="display: none;">
	                		<td>
	                			<input type="hidden" name="permissionId" id="permissionId" value="8031">
	                			<input type="hidden" name="modifytype" id="modifytype"/>
	                			<input type="hidden" class="serialno" name="serialno" value="${tradeCheckDto.serialno}" />
								<input type="hidden" class="dsapkind" name="dsapkind" value="${tradeCheckDto.dsapkind}" />
								<input type="hidden" class="custno" name="custno" value="${tradeCheckDto.custno}" />
								<input type="hidden" class="tradeacco" name="tradeacco" value="${tradeCheckDto.tradeacco}" />
								<input type="hidden" class="operatorId" name="operatorId" value="${tradeCheckDto.operatorId}" />
								
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>业务名称：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.dsapkindnm}</span>
	                		</td>
	                		<td>基金账号：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.fundacco}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>交易账号：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.tradeacco}</span>
	                		</td>
	                		<td>客户类别：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.invtpnm}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>客户名称：</td>
	                		<td class="white-bg form-inner" colspan="3">
	                			<span>${tradeCheckDto.invnm}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>委托方式：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.trustTypenm}</span>
	                		</td>
	                		<td>申请日期：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.apdt}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金名称：</td>
	                		<td class="white-bg form-inner">
	                			<select name="fundid" class="select2 use-select2 form-input fundid">
	                			<c:forEach var="dto" items="${fundNameList}">
                				<c:if test="${tradeCheckDto.fundid == dto.fundId}">
	                				<option value="${dto.fundId}" selected="selected">${dto.fundId}　${dto.fundShortNm}</option>
                				</c:if>
                				<c:if test="${tradeCheckDto.fundid != dto.fundId && dto.fundId != '' && dto.fundId != null}">
	                				<option value="${dto.fundId}">${dto.fundId}　${dto.fundShortNm}</option>
                				</c:if>
	                			</c:forEach>
	                			</select>
	                		</td>
	                		<td>对方基金：</td>
	                		<td class="white-bg form-inner">
		                		<c:choose>
					                <c:when test="${tradeCheckDto.dsapkind == '036' || tradeCheckDto.dsapkind == '026' || 
					                			    tradeCheckDto.dsapkind == '027' || tradeCheckDto.dsapkind == '028'}">
										<select name="ofundid" class="select2 use-select2 form-input ofundid">
										<c:forEach var="dto" items="${fundNameList}">
		                				<c:if test="${tradeCheckDto.ofundid == dto.fundId}">
			                				<option value="${dto.fundId}" selected="selected">${dto.fundId}　${dto.fundShortNm}</option>
		                				</c:if>
		                				<c:if test="${tradeCheckDto.fundid != dto.fundId && dto.fundId != '' && dto.fundId != null}">
			                				<option value="${dto.fundId}">${dto.fundId}　${dto.fundShortNm}</option>
		                				</c:if>
			                			</c:forEach>
		                				</select>
					                </c:when>
					                <c:otherwise>
				                		<span>${tradeCheckDto.ofundnm}</span>
				                	</c:otherwise>
				                </c:choose>
	                		</td>
	                	</tr>
	                	<tr>
	                	<c:choose>
	                		<c:when test="${tradeCheckDto.dsapkind == '020' || tradeCheckDto.dsapkind == '022'}">
	                			<td>申请金额：</td>
		                		<td class="white-bg form-inner rowHeigth" colspan="3">
		                			<input name="subamt" class="form-control subamt" value="${tradeCheckDto.subamt}" onKeyUp="onMoneyChange(this);" />
						   			<span id="CapMoneyqianfenwei"></span>&nbsp;&nbsp;
						   			<span style="color: red;" id="CapMoney">${tradeCheckDto.subamtnm}</span>
		                		</td>
	                		</c:when>
	                		<c:when test="${tradeCheckDto.dsapkind == '024' || tradeCheckDto.dsapkind == '026' || tradeCheckDto.dsapkind == '027' || tradeCheckDto.dsapkind == '028' || tradeCheckDto.dsapkind == '036'}">
		                		<td>申请份额：</td>
	                			<c:if test="${tradeCheckDto.dsapkind == '024'}">
		                		<td class="white-bg form-inner rowHeigth">
                					<input name="subquty" class="form-control subquty" value="${tradeCheckDto.subquty}" onKeyUp="onMoneyChange(this);" /><span style="color: red;" id="CapMoneySubquty">${tradeCheckDto.subqutynm}</span>
                				</td>
                				<td>巨额赎回：</td>
                				<td class="white-bg form-inner">
		                			<select name="largeflag" class="select2 use-select2 form-input largeflag">
		                			<c:forEach var="temp" items="${largeflagArray}">
		                				<c:if test="${tradeCheckDto.largeflag == temp.pmco}">
			                				<option value="${temp.pmco}" selected="selected">${temp.pmco}　${temp.pmnm}</option>
		                				</c:if>
		                				<c:if test="${tradeCheckDto.largeflag != temp.pmco && temp.pmco != '' && temp.pmco != null}">
			                				<option value="${temp.pmco}">${temp.pmco}　${temp.pmnm}</option>
		                				</c:if>
			                			</c:forEach>
		                			</select>
		                		</td>
	                			</c:if>
	                			<c:if test="${tradeCheckDto.dsapkind != '024' && tradeCheckDto.dsapkind != '' && tradeCheckDto.dsapkind != null }">
	                			<td class="white-bg form-inner rowHeigth" colspan="3">
		                			<input name="subquty" class="form-control subquty" value="${tradeCheckDto.subquty}" onKeyUp="onMoneyChange(this);" />
						   			<span id="CapMoneyqianfenwei"></span>&nbsp;&nbsp;
						   			<span style="color: red;" id="CapMoney">${tradeCheckDto.subqutynm}</span>
		                		</td>
	                			</c:if>
		                	</c:when>
		                	<c:when test="${tradeCheckDto.dsapkind == '029'}">
		                		<td>分红方式：</td>
		                		<td class="white-bg form-inner">
		                			<select name="melonmd" class="select2 use-select2 form-input melonmd">
		                			<c:forEach var="temp" items="${melonmdArray}">
		                				<c:if test="${tradeCheckDto.melonmd == temp.pmco}">
			                				<option value="${temp.pmco}" selected="selected">${temp.pmco}　${temp.pmnm}</option>
		                				</c:if>
		                				<c:if test="${tradeCheckDto.largeflag != temp.pmco && temp.pmco != '' && temp.pmco != null}">
			                				<option value="${temp.pmco}">${temp.pmco}　${temp.pmnm}</option>
		                				</c:if>
			                		</c:forEach>
		                			</select>
		                		</td>
		                		<td>分红比例：</td>
		                		<td class="white-bg form-inner">
		                			<span>${tradeCheckDto.melonpercent}</span>
		                			<input name="melonpercent" class="form-control melonpercent" value="${tradeCheckDto.melonpercent}" />
		                		</td>
		                	</c:when>
		                	<c:otherwise>
		                		<td>申请金额：</td>
		                		<td class="white-bg form-inner">
		                			<span>${tradeCheckDto.subamt}　</span>
		                			<span style="color: red;">${tradeCheckDto.subamtnm}</span>
		                		</td>
		                		<td>申请份额：</td>
		                		<td class="white-bg form-inner">
		                			<span>${tradeCheckDto.subquty}　</span>
		                			<span style="color: red;">${tradeCheckDto.subqutynm}</span>
		                		</td>
		                	</c:otherwise>
	                	</c:choose>
	                	</tr>
	                	<tr>
	                		<td>凭证编号：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.serialno}</span>
	                			<input type="hidden" class="form-control serialno" value="${tradeCheckDto.serialno}">
	                		</td>
	                		<td>原申请编号：</td>
	                		<td class="white-bg form-inner">
	                		<c:choose>
	                			<c:when test="${tradeCheckDto.dsapkind == '026' || tradeCheckDto.dsapkind == '027' || tradeCheckDto.dsapkind == '028'}">
	                			<input name="oldserialno" class="form-control oldserialno" value="${tradeCheckDto.oldserialno}" />
	                			</c:when>
	                			<c:otherwise>
	                			<span>${tradeCheckDto.oldserialno}</span>
	                			</c:otherwise>
	                		</c:choose>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>对方基金账号：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.ofundacco}</span>
	                		</td>
	                		<td>对方交易账号：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.otradeacco}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>对方销售商：</td>
	                		<td class="white-bg form-inner">
	                		<c:choose>
	                			<c:when test="${tradeCheckDto.dsapkind == '026' || tradeCheckDto.dsapkind == '027' || tradeCheckDto.dsapkind == '028'}">
	                			<select name="oseatno" class="select2 use-select2 form-input clearText oseatno">
		                		<c:forEach var="dto" items="${seatList}">
	                				<c:if test="${tradeCheckDto.oseatno == dto.seatno}">
		                				<option value="${dto.seatno}" selected="selected">${dto.seatno}　${dto.seatnm}</option>
	                				</c:if>
	                				<c:if test="${tradeCheckDto.oseatno != dto.seatno && dto.seatno != '' && dto.seatno != null}">
		                				<option value="${dto.seatno}">${dto.seatno}　${dto.seatnm}</option>
	                				</c:if>
		                		</c:forEach>
		                		</select>
	                			</c:when>
	                			<c:otherwise>
	                			<span>${tradeCheckDto.oseatnm}</span>
	                			</c:otherwise>
	                		</c:choose>
	                		</td>
	                		<td>对方网点：</td>
	                		<td class="white-bg form-inner">
	                		<c:choose>
	                			<c:when test="${tradeCheckDto.dsapkind == '026' || tradeCheckDto.dsapkind == '027' || tradeCheckDto.dsapkind == '028'}">
	                			<input name="onetpoint" class="form-control onetpoint" value="${tradeCheckDto.onetpoint}" />
	                			</c:when>
	                			<c:otherwise>
	                			<span>${tradeCheckDto.onetpoint}</span>
	                			</c:otherwise>
	                		</c:choose>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>可用份额：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.aviable}</span>
	                		</td>
	                		<td>实际份额：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.balance}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>冻结份额：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.abnmfrozen}</span>
	                		</td>
	                		<td>经办人：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.broker}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>操作员：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.operatorId}</span>
	                		</td>
	                		<td>主管：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.checkno}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>网点：</td>
	                		<td class="white-bg form-inner" colspan="3">
	                			<span>${tradeCheckDto.netpointnm}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>备注：</td>
	                		<td class="white-bg form-inner" colspan="3">
	                			<span>${tradeCheckDto.remark}</span>
	                		</td>
	                	</tr>
	                	<tr>
							<td>复核员：</td>
						  	<td class="white-bg form-inner">
						  		<span>${tradeCheckDto.checker}</span>
						  	</td>
							<td>复核状态：</td>
							<td class="white-bg form-inner">
								<span>${tradeCheckDto.chkflagnm}</span>
							</td>
						</tr>
	                	<tr>
							<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
								<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="dopass" name="dopass" onclick="doSubmit('1');">提交修改</button>&nbsp;&nbsp;
								<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="docancel" name="docancel" onclick="doSubmit('0');">驳回放弃</button>&nbsp;&nbsp;
								<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear"onclick="window.close();">取消</button>
					   		</td>
			   			</tr>
	                </tbody>
                </table>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript" src="<%=context%>web/js/tradeManager/tradeModifyDetail.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>capitalService/server";
		setPath(primaryPath,basePath);
</script>
</body>
</html>