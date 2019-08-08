<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易类复核</title>
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
</style>
</head>
<body class="sub-page">
<form id="fundInfoForm" action="" name="fundInfoForm"  method="post" class="form-horizontal">
	<div class="modal-content">
		<div>
	        <h4 class="modal-title">交易类复核</h4>
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
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.invnm}</span>
	                		</td>
	                		<td>申请日期：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.apdt}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>银行账户：</td>
	                		<td class="white-bg form-inner" colspan="3">
	                			<span>${tradeCheckDto.bankacco}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金代码：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.fundid}</span>
	                		</td>
	                		<td>基金名称：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.fundnm}</span>
	                		</td>
	                	</tr>
	                	<c:choose>
		                	<c:when test="${tradeCheckDto.dsapkind == '020' || tradeCheckDto.dsapkind == '022'}">
		                	<tr>
		                		<td>申请金额：</td>
		                		<td class="white-bg form-inner" colspan="3">
		                			<span>${tradeCheckDto.subamt}　</span>
		                			<span style="color: red;">${tradeCheckDto.subamtnm}</span>
		                		</td>
		                	</tr>
		                	</c:when>
		                	<c:when test="${tradeCheckDto.dsapkind == '024' || tradeCheckDto.dsapkind == '026' || tradeCheckDto.dsapkind == '027' || tradeCheckDto.dsapkind == '028' || tradeCheckDto.dsapkind == '036'}">
		                	<tr>
		                		<td>申请份额：</td>
		                		<td class="white-bg form-inner" colspan="3">
		                			<span>${tradeCheckDto.subquty}　</span>
		                			<span style="color: red;">${tradeCheckDto.subqutynm}</span>
		                		</td>
		                	</tr>
		                	</c:when>
		                	<c:when test="${tradeCheckDto.dsapkind == '029'}">
		                	<tr>
		                		<td>分红方式：</td>
		                		<td class="white-bg form-inner">
		                			<span>${tradeCheckDto.melonmdnm}</span>
		                		</td>
		                		<td>分红比例：</td>
		                		<td class="white-bg form-inner">
		                			<span>${tradeCheckDto.melonpercent}</span>
		                		</td>
		                	</tr>
		                	</c:when>
		                	<c:otherwise>
		                	<tr>
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
		                	</tr>
		                	</c:otherwise>
	                	</c:choose>
	                	<tr>
	                		<td>委托方式：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.trustTypenm}</span>
	                		</td>
	                		<td>巨额赎回：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.largeflagnm}</span>
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
	                		<td>对方基金代码：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.ofundid}</span>
	                		</td>
	                		<td>对方基金名称：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.ofundnm}</span>
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
	                		<td>对方销售商：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.oseatnm}</span>
	                		</td>
	                		<td>对方网点：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.onetpoint}</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>凭证编号：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.serialno}</span>
	                			<input type="hidden" id="serialno" value="${tradeCheckDto.serialno}">
	                		</td>
	                		<td>原申请编号：</td>
	                		<td class="white-bg form-inner">
	                			<span>${tradeCheckDto.oldserialno}</span>
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
							<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
								<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="doY" name="doY" onclick="doSubmit('Y');">复核通过</button>&nbsp;&nbsp;
								<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="doR" name="doR" onclick="doSubmit('R');">复核驳回</button>&nbsp;&nbsp;
								<c:choose>
									<c:when test="${tradeCheckDto.dsapkind == '003' || tradeCheckDto.dsapkind =='910'}">
										<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" disabled="disabled" id="doC" name="doC" onclick="doSubmit('C');">复核作废</button>&nbsp;&nbsp;
									</c:when>
									<c:otherwise>
										<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="doC" name="doC" onclick="doSubmit('C');">复核作废</button>&nbsp;&nbsp;
									</c:otherwise>
								</c:choose>
								<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear"onclick="window.close();">取消</button>
					   		</td>
			   			</tr>
	                </tbody>
                </table>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript" src="<%=context%>web/js/tradeManager/tradeQry.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>capitalService/server";
		setPath(primaryPath,basePath);
</script>
</body>
</html>