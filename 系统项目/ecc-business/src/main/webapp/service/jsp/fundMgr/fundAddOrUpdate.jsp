<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><c:if test="${method eq 'add' }">新增</c:if><c:if test="${method eq 'update' }">修改</c:if><c:if test="${method eq 'check' }">复核</c:if>基金信息</title>
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
<form id="fundInfoForm" action="/service/fundManager/addOrUpdateFundInfo.xhtml" name="fundInfoForm"  method="post" class="form-horizontal">
	<div class="modal-content">
		<div>
	        <h4 class="modal-title"><c:if test="${requestScope.method eq 'add' }">新增</c:if><c:if test="${requestScope.method eq 'update' }">修改</c:if><c:if test="${method eq 'check' }">复核</c:if>基金信息</h4>
	    	<input type="hidden" class="selCycletp"  value="${fundManagerVo.cycletp}">
	    	<input type="hidden" class="txtCyclelen" value="${fundManagerVo.cyclelen}">
			<input id="transCode" type='hidden' value="9320" />
	    </div>
	    <div class="modal-body">
	    	<div class="table-responsive">
	                <c:if test="${method eq 'add' }">
	                <table class="table table-bordered">
	    			<colgroup>
	                    <col width="20%">
	                    <col width="30%">
	                    <col width="20%">
	                    <col width="30%">
	                </colgroup>
	                <tbody>
	                	<tr>
							<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
								<button type="button" class="btn btn-primary btn-w-xs" id="jbInfo" name="jbInfo" onclick="showDiv('divFundBaseInfo');">基本信息</button>&nbsp;&nbsp;
								<button type="button" class="btn btn-primary btn-w-xs" id="fxszInfo" name="fxszInfo" onclick="showDiv('divFundIssueSet');">发行设置</button>&nbsp;&nbsp;
								<button type="button" class="btn btn-primary btn-w-xs" id="fundxe" name="fundxe" onclick="showDiv('divFundLimitSet');">基金限额</button>
						   </td>
				   		</tr>
	                </tbody>
	                <tbody id="divFundBaseInfo" class="fundDiv">
	                	<tr>
	                		<td><span class="text-danger">*</span>基金代码</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金代码" class="form-control" id="txtBIFundId" name="txtBIFundId">
	                			<input type="hidden" class="form-control" id="method" name="method" value="add">
	                		</td>
	                		<td><span class="text-danger">*</span>基金名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金名称" class="form-control" id="txtBIFundNm" name="txtBIFundNm">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td><span class="text-danger">*</span>注册登记代码</td>
	                		<td class="white-bg form-inner">
	                			<select name="txtBITaNO" id="txtBITaNO" class="select2"></select>
	                		</td>
	                		<td><span class="text-danger">*</span>参数代码限制</td>
	                		<td class="white-bg form-inner">
	                			<select name="isUnFund" id="isUnFund" class="select2">
	                				<option value="N">否</option>
	                				<option value="Y">是</option>
	                			</select>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金简称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金简称" class="form-control" id="txtBIFundShortNm" name="txtBIFundShortNm">
	                		</td>
	                		<td>英文名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="英文名称" class="form-control" id="txtBIFundElNm" name="txtBIFundElNm">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>管理人名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="管理人名称" class="form-control" id="txtBIManagerNm" name="txtBIManagerNm">
	                		</td>
	                		<td>托管人名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="托管人名称" class="form-control" id="txtBITrusteeNm" name="txtBITrusteeNm">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金面值</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金面值" class="form-control" id="txtBIFundDenomina" name="txtBIFundDenomina">
	                		</td>
	                		<td>基金单位净值(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金单位净值(元)" class="form-control" id="txtBINav" name="txtBINav">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>货币类型</td>
	                		<td class="white-bg form-inner">
	                			<select name="selBICurrencyType" id="selBICurrencyType" class="select2"></select>
	                		</td>
	                		<td>投资方向</td>
	                		<td class="white-bg form-inner">
	                			<select name="selBIInverstDirect" id="selBIInverstDirect" class="select2"></select>
	                		</td>
	                	</tr>
	                </tbody>
	                
	                <tbody id="divFundIssueSet" class="fundDiv">
	                	<tr>
	                		<td><span class="text-danger">*</span>净值小数位数</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="净值小数位数" class="form-control" id="txtISNavFracNum" name="txtISNavFracNum" value="4">
	                		</td>
	                		<td>净值小数处理方式</td>
	                		<td class="white-bg form-inner">
	                			<select name="selISNavFracMode" id="selISNavFracMode" class="select2"></select>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金发行日</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金发行日" class="form-control initDate" id="txtISIssueDate" name="txtISIssueDate">
	                		</td>
	                		<td>基金成立日期</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金成立日期" class="form-control initDate" id="txtISSetUpDate" name="txtISSetUpDate">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>发行价格</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="发行价格" class="form-control" id="txtISIssuePrice" name="txtISIssuePrice">
	                		</td>
	                		<td>管理费率</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="管理费率" class="form-control" id="txtISManagerRates" name="txtISManagerRates">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金类别</td>
	                		<td class="white-bg form-inner">
	                			<select name="selISFundType" id="selISFundType" class="select2" onchange="setFinanceCycle(this);"></select>
	                		</td>
	                		<td>基金风险等级</td>
	                		<td class="white-bg form-inner">
	                			<select name="selISFundRiskLevel" id="selISFundRiskLevel" class="select2"></select>
	                		</td>
	                	</tr>
	                	
	                	<tr id="financeTR" style="display:none">
						    
						</tr>
	                	
	                	<tr>
	                		<td>基金状态</td>
	                		<td class="white-bg form-inner">
	                			<select name="selISFundSt" id="selISFundSt" class="select2"></select>
	                		</td>
	                		<td>基金评级日期</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金评级日期" class="form-control initDate" id="txtISFundEvalDate" name="txtISFundEvalDate">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金显示类别</td>
	                		<td class="white-bg form-inner">
	                			<select name="selFundDispTp" id="selFundDispTp" class="select2"></select>
	                		</td>
	                		<td>母基金代码</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="母基金代码" class="form-control" id="txtPFundid" name="txtPFundid">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>下一开放日</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="下一开放日" class="form-control initDate" id="txtNextIssueDate" name="txtNextIssueDate">
	                		</td>
	                		<td>产品特点</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="产品特点" class="form-control" id="txtFeature" name="txtFeature">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>产品期限(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="产品期限(天)" class="form-control" id="txtProductTimeLimit" name="txtProductTimeLimit">
	                		</td>
	                		<td></td>
	                		<td class="white-bg form-inner"></td>
	                	</tr>
	                </tbody>
	                
	                <tbody id="divFundLimitSet"  class="fundDiv">
	                	<tr>
	                		<td>最低认购金额(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低认购金额(元)" class="form-control" id="txtLSMinSubAmt" name="txtLSMinSubAmt">
	                		</td>
	                		<td>最低申购金额(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低申购金额(元)" class="form-control" id="txtLSMinPurAmt" name="txtLSMinPurAmt">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>最低持有份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低持有份额(份)" class="form-control" id="txtKeepLimit" name="txtKeepLimit">
	                		</td>
	                		<td>最低赎回份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低赎回份额(份)" class="form-control" id="txtLSMinRedShare" name="txtLSMinRedShare">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>最低转换份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低转换份额(份)" class="form-control" id="txtLSMinConvShare" name="txtLSMinConvShare">
	                		</td>
	                		<td>最低定投金额(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低定投金额(元)" class="form-control" id="txtLSMinRegAmt" name="txtLSMinRegAmt">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>产品预订规模(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="产品预订规模(份)" class="form-control" id="txtFundSize" name="txtFundSize">
	                		</td>
	                		<td>最高认申购份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最高认申购份额(份)" class="form-control" id="txtIndiMaxPurchase" name="txtIndiMaxPurchase">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>网上交易最高认申购限额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="网上交易最高认申购限额(份)" class="form-control" id="txtEcmaxPurchasest" name="txtEcmaxPurchasest">
	                		</td>
	                		<td><span class="text-danger">*</span>赎回款到账日期(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="赎回款到账日期(天)" class="form-control" id="txtLSRedeemToAcctDays" name="txtLSRedeemToAcctDays">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td><span class="text-danger">*</span>分红划款日期(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="分红划款日期(天)" class="form-control" id="txtMelonDays" name="txtMelonDays">
	                		</td>
	                		<td><span class="text-danger">*</span>认申购划款日期(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="认申购划款日期(天)" class="form-control" id="txtSubDays" name="txtSubDays">
	                		</td>
	                	</tr>
	                	<tr>
					    	<td colspan=4>
					    		<span style="color:red">*注：“赎回款到账日期(天)”、“分红划款日期(天)”、“认申购划款日期(天)”是从确认日开始计算；例：QDII基金的赎回确认日为T+2（T为申请日），划款日为T+7，则“赎回款到账日期(天)”为5。</span>
					    	</td>
					  	</tr>
	                </tbody>
	                </table>
					<table class="table">
	                	<tbody>
							<tr>
								<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
									<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="subBtn" name="subBtn" onclick="addOrUpdateFundInfo();">提交</button>
									<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear"onclick="window.close();">取消</button>
						   			<button type="button" style="display:none" class="btn btn-link btn-w-xs" id="syschonizeTD" name="syschonize" onclick="syschonizeSetupDate();">同步成立日期</button>
						   	</td>
				   			</tr>
		   				</tbody>
					</table>
					<script type="text/javascript">
					$(function(){
						addFromValidate();
					})
					</script>
	                </c:if>
	                
	                <c:if test="${method eq 'update' }">
	                <table class="table table-bordered">
	    			<colgroup>
	                    <col width="20%">
	                    <col width="30%">
	                    <col width="20%">
	                    <col width="30%">
	                </colgroup>
	                <tbody>
	                	<tr>
							<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
								<button type="button" class="btn btn-primary btn-w-xs" id="jbInfo" name="jbInfo" onclick="showDiv('divFundBaseInfo');">基本信息</button>&nbsp;&nbsp;
								<button type="button" class="btn btn-primary btn-w-xs" id="fxszInfo" name="fxszInfo" onclick="showDiv('divFundIssueSet');">发行设置</button>&nbsp;&nbsp;
								<button type="button" class="btn btn-primary btn-w-xs" id="fundxe" name="fundxe" onclick="showDiv('divFundLimitSet');">基金限额</button>
						   </td>
				   		</tr>
	                </tbody>
	                <tbody id="divFundBaseInfo" class="fundDiv">
	                	<tr>
	                		<td><span class="text-danger">*</span>基金代码</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金代码" readonly="readonly" class="form-control" id="txtBIFundId" name="txtBIFundId" value="${fundManagerVo.fundId}">
	                			<input type="hidden" class="form-control" id="method" name="method" value="update">
	                		</td>
	                		<td><span class="text-danger">*</span>基金名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金名称" class="form-control" id="txtBIFundNm" name="txtBIFundNm" value="${fundManagerVo.fundNm}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td><span class="text-danger">*</span>注册登记代码</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="txtBITaNO" value="${fundManagerVo.taNo}">
	                			<select name="txtBITaNO" id="txtBITaNO" class="select2"></select>
	                		</td>
	                		<td><span class="text-danger">*</span>参数代码限制</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="isUnFund" value="${fundManagerVo.isUnFund}">
	                			<select name="isUnFund" id="isUnFund" class="select2">
	                				<option value="N">否</option>
	                				<option value="Y">是</option>
	                			</select>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金简称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金简称" class="form-control" id="txtBIFundShortNm" name="txtBIFundShortNm" value="${fundManagerVo.fundShortNm}">
	                		</td>
	                		<td>英文名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="英文名称" class="form-control" id="txtBIFundElNm" name="txtBIFundElNm" value="${fundManagerVo.fundEnglishNm}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>管理人名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="管理人名称" class="form-control" id="txtBIManagerNm" name="txtBIManagerNm" value="${fundManagerVo.managerNm}">
	                		</td>
	                		<td>托管人名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="托管人名称" class="form-control" id="txtBITrusteeNm" name="txtBITrusteeNm" value="${fundManagerVo.trusteeNm}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金面值</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金面值" class="form-control" id="txtBIFundDenomina" name="txtBIFundDenomina" value="${fundManagerVo.fundDenomina}">
	                		</td>
	                		<td>基金单位净值(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金单位净值(元)" class="form-control" id="txtBINav" name="txtBINav" value="${fundManagerVo.nav}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>货币类型</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selBICurrencyType" value="${fundManagerVo.currencyType}">
	                			<select name="selBICurrencyType" id="selBICurrencyType" class="select2"></select>
	                		</td>
	                		<td>投资方向</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selBIInverstDirect" value="${fundManagerVo.investType}">
	                			<select name="selBIInverstDirect" id="selBIInverstDirect" class="select2"></select>
	                		</td>
	                	</tr>
	                </tbody>
	                
	                <tbody id="divFundIssueSet" class="fundDiv">
	                	<tr>
	                		<td><span class="text-danger">*</span>净值小数位数</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="净值小数位数" class="form-control" id="txtISNavFracNum" name="txtISNavFracNum"  value="${fundManagerVo.navFracNum}">
	                		</td>
	                		<td>净值小数处理方式</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selISNavFracMode" value="${fundManagerVo.navFracMode}">
	                			<select name="selISNavFracMode" id="selISNavFracMode" class="select2"></select>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金发行日</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金发行日" class="form-control initDate" id="txtISIssueDate" name="txtISIssueDate" value="${fundManagerVo.issueDate}">
	                		</td>
	                		<td>基金成立日期</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金成立日期" class="form-control initDate" id="txtISSetUpDate" name="txtISSetUpDate" value="${fundManagerVo.setUpDate}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>发行价格</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="发行价格" class="form-control" id="txtISIssuePrice" name="txtISIssuePrice" value="${fundManagerVo.issuePrice}">
	                		</td>
	                		<td>管理费率</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="管理费率" class="form-control" id="txtISManagerRates" name="txtISManagerRates" value="${fundManagerVo.managerRates}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金类别</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selISFundType" value="${fundManagerVo.fundType}">
	                			<select name="selISFundType" id="selISFundType" class="select2" onchange="setFinanceCycle(this);"></select>
	                		</td>
	                		<td>基金风险等级</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selISFundRiskLevel" value="${fundManagerVo.fundRiskLevel}">
	                			<select name="selISFundRiskLevel" id="selISFundRiskLevel" class="select2"></select>
	                		</td>
	                	</tr>
	                	
	                	<tr id="financeTR" style="display:none">
						    
						</tr>
	                	
	                	<tr>
	                		<td>基金状态</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selISFundSt" value="${fundManagerVo.fundSt}">
	                			<select name="selISFundSt" id="selISFundSt" class="select2"></select>
	                		</td>
	                		<td>基金评级日期</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金评级日期" class="form-control initDate" id="txtISFundEvalDate" name="txtISFundEvalDate" value="${fundManagerVo.fundEvalDate}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金显示类别</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selFundDispTp" value="${fundManagerVo.fundDispType}">
	                			<select name="selFundDispTp" id="selFundDispTp" class="select2"></select>
	                		</td>
	                		<td>母基金代码</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="母基金代码" class="form-control" id="txtPFundid" name="txtPFundid" value="${fundManagerVo.parentFundid}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>下一开放日</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="下一开放日" class="form-control initDate" id="txtNextIssueDate" name="txtNextIssueDate" value="${fundManagerVo.nextIssueDate}">
	                		</td>
	                		<td>产品特点</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="产品特点" class="form-control" id="txtFeature" name="txtFeature" value="${fundManagerVo.feature}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>产品期限(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="产品期限(天)" class="form-control" id="txtProductTimeLimit" name="txtProductTimeLimit" value="${fundManagerVo.productTimeLimit}">
	                		</td>
	                		<td></td>
	                		<td class="white-bg form-inner"></td>
	                	</tr>
	                </tbody>
	                
	                <tbody id="divFundLimitSet"  class="fundDiv">
	                	<tr>
	                		<td>最低认购金额(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低认购金额(元)" class="form-control" id="txtLSMinSubAmt" name="txtLSMinSubAmt" value="${fundManagerVo.minSubAmt}">
	                		</td>
	                		<td>最低申购金额(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低申购金额(元)" class="form-control" id="txtLSMinPurAmt" name="txtLSMinPurAmt" value="${fundManagerVo.minBidAmt}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>最低持有份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低持有份额(份)" class="form-control" id="txtKeepLimit" name="txtKeepLimit" value="${fundManagerVo.keepLimit}">
	                		</td>
	                		<td>最低赎回份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低赎回份额(份)" class="form-control" id="txtLSMinRedShare" name="txtLSMinRedShare" value="${fundManagerVo.minRedAmt}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>最低转换份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低转换份额(份)" class="form-control" id="txtLSMinConvShare" name="txtLSMinConvShare" value="${fundManagerVo.minConvAmt}">
	                		</td>
	                		<td>最低定投金额(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低定投金额(元)" class="form-control" id="txtLSMinRegAmt" name="txtLSMinRegAmt" value="${fundManagerVo.minRspAmt}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>产品预订规模(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="产品预订规模(份)" class="form-control" id="txtFundSize" name="txtFundSize" value="${fundManagerVo.fundSize}">
	                		</td>
	                		<td>最高认申购份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最高认申购份额(份)" class="form-control" id="txtIndiMaxPurchase" name="txtIndiMaxPurchase" value="${fundManagerVo.indiMaxPurchase}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>网上交易最高认申购限额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="网上交易最高认申购限额(份)" class="form-control" id="txtEcmaxPurchasest" name="txtEcmaxPurchasest" value="${fundManagerVo.ecmaxPurchase}">
	                		</td>
	                		<td><span class="text-danger">*</span>赎回款到账日期(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="赎回款到账日期(天)" class="form-control" id="txtLSRedeemToAcctDays" name="txtLSRedeemToAcctDays" value="${fundManagerVo.redeemToAcctDays}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>最低转入限额(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="最低转入限额(元)" class="form-control" id="txtInconvertinbyinst" name="txtInconvertinbyinst" value="${fundManagerVo.inconvertinbyinst}">
	                		</td>
	                		<td><span class="text-danger">*</span>分红划款日期(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="分红划款日期(天)" class="form-control" id="txtMelonDays" name="txtMelonDays" value="${fundManagerVo.redMelonDays}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td><span class="text-danger">*</span>认申购划款日期(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="认申购划款日期(天)" class="form-control" id="txtSubDays" name="txtSubDays" value="${fundManagerVo.subDays}">
	                		</td>
	                	</tr>
	                	<tr>
					    	<td colspan=4>
					    		<span style="color:red">*注：“赎回款到账日期(天)”、“分红划款日期(天)”、“认申购划款日期(天)”是从确认日开始计算；例：QDII基金的赎回确认日为T+2（T为申请日），划款日为T+7，则“赎回款到账日期(天)”为5。</span>
					    	</td>
					  	</tr>
	                </tbody>
	                </table>
					<table class="table">
		                <tbody>
							<tr>
								<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
									<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="subBtn" name="subBtn" onclick="addOrUpdateFundInfo();">提交</button>
									<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear"onclick="window.close();">取消</button>
							   		<button type="button" style="display:none" class="btn btn-link btn-w-xs" id="syschonizeTD" name="syschonize" onclick="syschonizeSetupDate();">同步成立日期</button>
							   </td>
					   		</tr>
			   			</tbody>
					</table>
					
					<script type="text/javascript">
					$(function(){
						addFromValidate_upd();
					})
					</script>
	                </c:if>
	                
	                <c:if test="${method eq 'check' }">
	                <table class="table table-bordered">
	    			<colgroup>
	                    <col width="20%">
	                    <col width="30%">
	                    <col width="20%">
	                    <col width="30%">
	                </colgroup>
	                <tbody>
	                	<tr>
							<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
								<button type="button" class="btn btn-primary btn-w-xs" id="jbInfo" name="jbInfo" onclick="showDiv('divFundBaseInfo');">基本信息</button>&nbsp;&nbsp;
								<button type="button" class="btn btn-primary btn-w-xs" id="fxszInfo" name="fxszInfo" onclick="showDiv('divFundIssueSet');">发行设置</button>&nbsp;&nbsp;
								<button type="button" class="btn btn-primary btn-w-xs" id="fundxe" name="fundxe" onclick="showDiv('divFundLimitSet');">基金限额</button>
						   </td>
				   		</tr>
	                </tbody>
	                <tbody id="divFundBaseInfo" class="fundDiv">
	                	<tr>
	                		<td><span class="text-danger">*</span>基金代码</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" readonly="readonly" placeholder="基金代码" class="form-control" id="txtBIFundId" name="txtBIFundId" value="${fundManagerVo.fundId}">
	                			<input type="hidden" class="form-control" id="status" name="status" value=${status}>
	                			<input type="hidden" class="form-control" id="method" name="method" value="check">
	                		</td>
	                		<td><span class="text-danger">*</span>基金名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="基金名称" class="form-control" id="txtBIFundNm" name="txtBIFundNm" value="${fundManagerVo.fundNm}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td><span class="text-danger">*</span>注册登记代码</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="txtBITaNO" value="${fundManagerVo.taNo}">
	                			<select name="txtBITaNO" disabled="disabled" id="txtBITaNO" class="select2"></select>
	                		</td>
	                		<td><span class="text-danger">*</span>参数代码限制</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="isUnFund" value="${fundManagerVo.isUnFund}">
	                			<select name="isUnFund" disabled="disabled" id="isUnFund" class="select2">
	                				<option value="N">否</option>
	                				<option value="Y">是</option>
	                			</select>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金简称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="基金简称" class="form-control" id="txtBIFundShortNm" name="txtBIFundShortNm" value="${fundManagerVo.fundShortNm}">
	                		</td>
	                		<td>英文名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="英文名称" class="form-control" id="txtBIFundElNm" name="txtBIFundElNm" value="${fundManagerVo.fundEnglishNm}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>管理人名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="管理人名称" class="form-control" id="txtBIManagerNm" name="txtBIManagerNm" value="${fundManagerVo.managerNm}">
	                		</td>
	                		<td>托管人名称</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="托管人名称" class="form-control" id="txtBITrusteeNm" name="txtBITrusteeNm" value="${fundManagerVo.trusteeNm}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金面值</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="基金面值" class="form-control" id="txtBIFundDenomina" name="txtBIFundDenomina" value="${fundManagerVo.fundDenomina}">
	                		</td>
	                		<td>基金单位净值(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="基金单位净值(元)" class="form-control" id="txtBINav" name="txtBINav" value="${fundManagerVo.nav}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>货币类型</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selBICurrencyType" value="${fundManagerVo.currencyType}">
	                			<select name="selBICurrencyType" disabled="disabled" id="selBICurrencyType" class="select2"></select>
	                		</td>
	                		<td>投资方向</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selBIInverstDirect" value="${fundManagerVo.investType}">
	                			<select name="selBIInverstDirect" disabled="disabled" id="selBIInverstDirect" class="select2"></select>
	                		</td>
	                	</tr>
	                </tbody>
	                
	                <tbody id="divFundIssueSet" class="fundDiv">
	                	<tr>
	                		<td><span class="text-danger">*</span>净值小数位数</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="净值小数位数" class="form-control" id="txtISNavFracNum" name="txtISNavFracNum"  value="${fundManagerVo.navFracNum}">
	                		</td>
	                		<td>净值小数处理方式</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selISNavFracMode" value="${fundManagerVo.navFracMode}">
	                			<select name="selISNavFracMode" disabled="disabled" id="selISNavFracMode" class="select2"></select>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金发行日</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="基金发行日" class="form-control initDate" id="txtISIssueDate" name="txtISIssueDate" value="${fundManagerVo.issueDate}">
	                		</td>
	                		<td>基金成立日期</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="基金成立日期" class="form-control initDate" id="txtISSetUpDate" name="txtISSetUpDate" value="${fundManagerVo.setUpDate}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>发行价格</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="发行价格" class="form-control" id="txtISIssuePrice" name="txtISIssuePrice" value="${fundManagerVo.issuePrice}">
	                		</td>
	                		<td>管理费率</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="管理费率" class="form-control" id="txtISManagerRates" name="txtISManagerRates" value="${fundManagerVo.managerRates}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金类别</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selISFundType" value="${fundManagerVo.fundType}">
	                			<select name="selISFundType" disabled="disabled" id="selISFundType" class="select2" onchange="setFinanceCycle(this);"></select>
	                		</td>
	                		<td>基金风险等级</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" disabled="disabled" class="selISFundRiskLevel" value="${fundManagerVo.fundRiskLevel}">
	                			<select name="selISFundRiskLevel" disabled="disabled" id="selISFundRiskLevel" class="select2"></select>
	                		</td>
	                	</tr>
	                	
	                	<tr id="financeTR" style="display:none"></tr>
	                	
	                	<tr>
	                		<td>基金状态</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" class="selISFundSt" value="${fundManagerVo.fundSt}">
	                			<select name="selISFundSt" disabled="disabled" id="selISFundSt" class="select2"></select>
	                		</td>
	                		<td>基金评级日期</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="基金评级日期" class="form-control initDate" id="txtISFundEvalDate" name="txtISFundEvalDate" value="${fundManagerVo.fundEvalDate}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>基金显示类别</td>
	                		<td class="white-bg form-inner">
	                			<input type="hidden" disabled="disabled" class="selFundDispTp" value="${fundManagerVo.fundDispType}">
	                			<select name="selFundDispTp" disabled="disabled" id="selFundDispTp" class="select2"></select>
	                		</td>
	                		<td>母基金代码</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="母基金代码" class="form-control" id="txtPFundid" name="txtPFundid" value="${fundManagerVo.parentFundid}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>下一开放日</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="下一开放日" class="form-control initDate" id="txtNextIssueDate" name="txtNextIssueDate" value="${fundManagerVo.nextIssueDate}">
	                		</td>
	                		<td>产品特点</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="产品特点" class="form-control" id="txtFeature" name="txtFeature" value="${fundManagerVo.feature}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>产品期限(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="产品期限(天)" class="form-control" id="txtProductTimeLimit" name="txtProductTimeLimit" value="${fundManagerVo.productTimeLimit}">
	                		</td>
	                		<td></td>
	                		<td class="white-bg form-inner"></td>
	                	</tr>
	                </tbody>
	                
	                <tbody id="divFundLimitSet"  class="fundDiv">
	                	<tr>
	                		<td>最低认购金额(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="最低认购金额(元)" class="form-control" id="txtLSMinSubAmt" name="txtLSMinSubAmt" value="${fundManagerVo.minSubAmt}">
	                		</td>
	                		<td>最低申购金额(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="最低申购金额(元)" class="form-control" id="txtLSMinPurAmt" name="txtLSMinPurAmt" value="${fundManagerVo.minBidAmt}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>最低持有份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="最低持有份额(份)" class="form-control" id="txtKeepLimit" name="txtKeepLimit" value="${fundManagerVo.keepLimit}">
	                		</td>
	                		<td>最低赎回份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="最低赎回份额(份)" class="form-control" id="txtLSMinRedShare" name="txtLSMinRedShare" value="${fundManagerVo.minRedAmt}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>最低转换份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="最低转换份额(份)" class="form-control" id="txtLSMinConvShare" name="txtLSMinConvShare" value="${fundManagerVo.minConvAmt}">
	                		</td>
	                		<td>最低定投金额(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="最低定投金额(元)" class="form-control" id="txtLSMinRegAmt" name="txtLSMinRegAmt" value="${fundManagerVo.minRspAmt}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>产品预订规模(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="产品预订规模(份)" class="form-control" id="txtFundSize" name="txtFundSize" value="${fundManagerVo.fundSize}">
	                		</td>
	                		<td>最高认申购份额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="最高认申购份额(份)" class="form-control" id="txtIndiMaxPurchase" name="txtIndiMaxPurchase" value="${fundManagerVo.indiMaxPurchase}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>网上交易最高认申购限额(份)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="网上交易最高认申购限额(份)" class="form-control" id="txtEcmaxPurchasest" name="txtEcmaxPurchasest" value="${fundManagerVo.ecmaxPurchase}">
	                		</td>
	                		<td><span class="text-danger">*</span>赎回款到账日期(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="赎回款到账日期(天)" class="form-control" id="txtLSRedeemToAcctDays" name="txtLSRedeemToAcctDays" value="${fundManagerVo.redeemToAcctDays}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>最低转入限额(元)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="最低转入限额(元)" class="form-control" id="txtInconvertinbyinst" name="txtInconvertinbyinst" value="${fundManagerVo.inconvertinbyinst}">
	                		</td>
	                		<td><span class="text-danger">*</span>分红划款日期(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="分红划款日期(天)" class="form-control" id="txtMelonDays" name="txtMelonDays" value="${fundManagerVo.redMelonDays}">
	                		</td>
	                	</tr>
	                	<tr>
	                		<td><span class="text-danger">*</span>认申购划款日期(天)</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" disabled="disabled" placeholder="认申购划款日期(天)" class="form-control" id="txtSubDays" name="txtSubDays" value="${fundManagerVo.subDays}">
	                		</td>
	                	</tr>
	                	
	                	
	                	<tr>
					    	<td colspan=4>
					    		<span style="color:red">*注：“赎回款到账日期(天)”、“分红划款日期(天)”、“认申购划款日期(天)”是从确认日开始计算；例：QDII基金的赎回确认日为T+2（T为申请日），划款日为T+7，则“赎回款到账日期(天)”为5。</span>
					    	</td>
					  	</tr>
	                </tbody>
	            </table>
				<table class="table">
	                <tbody>
						<tr>
							<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
								<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="subBtn" name="subBtn" onclick="checkFundInfo();">提交</button>
								<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear"onclick="window.close();">取消</button>
						   </td>
				   		</tr>
		   			</tbody>
				</table>
	            </c:if>
			</div>
		</div>
	</div>
</form>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/fund/fundManager.js?20180716"></script>
<script type="text/javascript" src="<%=context%>web/js/fund/common.js?20180539"></script>

<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/fundManager";
	setPath(primaryPath,basePath);
	loadData();
	$("#jbInfo").click();
	COMMON_INIT.initDateYMDComponent("txtISIssueDate");
	COMMON_INIT.initDateYMDComponent("txtISSetUpDate");
	COMMON_INIT.initDateYMDComponent("txtISFundEvalDate");
	COMMON_INIT.initDateYMDComponent("txtNextIssueDate");
</script>
</body>
</html>