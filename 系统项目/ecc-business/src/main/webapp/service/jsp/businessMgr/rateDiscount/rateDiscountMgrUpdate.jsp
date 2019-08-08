<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta name="viewport" content="width=device-width, initial-scale=1.0">
	<meta HTTP-EQUIV="pragma" CONTENT="no-cache"> 
	<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
	<meta HTTP-EQUIV="expires" CONTENT="0">
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>修改费率折扣-业务管理-直销柜台</title>
	<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
</head>
<body class="sub-page">
<form name="rateDiscountUpdateForm" id="rateDiscountUpdateForm" method="post" action="<%=context%>service/rateDiscount/updateRateDisc.do" class="form-horizontal">
<div style="display: none" id="hiddenParam">
	<input name="bnkNo" value="${rateVo.bnkNo}"/>
	<input name="hidBnkName" value="${rateVo.bnkName}"/>
	<input name="productId" value="${rateVo.productId}"/>
	<input name="hidProductName" value="${rateVo.productName}"/>
	<input name="hidStatus" value="${rateVo.status}"/>
	<input name="hidStatusName" value="${rateVo.statusName}"/>
	<input name="apkind" value="${rateVo.apkind}"/>
	<input name="hidApkindName" value="${rateVo.apkindName}"/>
</div>
	<fieldset>
		<div class="page-content">
			<div class="page-header">
				<h4 class="page-title">修改费率折扣信息</h4>
			</div>
			<div class="page-body">
				<div class="form-group">
					<label class="col-sm-2 control-label">银行名称<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select class="form-control use-select2" name="hidBnkNo" id="hidBnkNo" disabled="disabled"></select>
					</div>
					<label class="col-sm-2 control-label">商户产品ID(基金代码)<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select class="form-control use-select2" name="hidProductId" id="hidProductId" disabled="disabled" ></select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">业务类型<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select type="text" placeholder="业务类型" class="form-control use-select2" name="hidApkind" id="hidApkind" disabled="disabled"></select>
					</div>
					<label class="col-sm-2 control-label">费率折扣<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<input type="text" placeholder="费率折扣" class="form-control" name="discount" id="discount" value="${rateVo.discount}" />
						<div>(如0.6，6折)</div>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">起始日期<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<input type="text" placeholder="起始日期" class="form-control" name="strDate" id="strDate" onchange="dateChange()" value="${rateVo.strDateFormat}" />
					</div>
					<label class="col-sm-2 control-label">结束日期<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<input type="text" placeholder="结束日期" class="form-control" name="endDate" id="endDate" onchange="dateChange()" value="${rateVo.endDateFormat}"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">起始金额(含)<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<input type="text" placeholder="起始金额(含)" class="form-control" name="strAmt" id="strAmt" value="${rateVo.strAmt}" onchange="amtChange()"/>
					</div>
					<label class="col-sm-2 control-label">结束金额(不含)<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<input type="text" placeholder="结束金额(不含)" class="form-control" name="endAmt" id="endAmt" value="${rateVo.endAmt}" onchange="amtChange()"/>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">状态<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select class="form-control use-select2" name="status" id="status" param='{"pmst":"SYSTEM","pmky":"ECCDATAST"}'></select>
					</div>
				</div>
			</div>
			
			<div class="page-footer">
		         <button type="button" class="btn btn-primary btn-save" data-loading-text="保存中..." id="submitBtn" name="submitBtn" onclick="doSubmit()">保存</button>
		         <button type="button" class="btn btn-link" onclick="window.close();" id="closeBtn" name="closeBtn">取消</button>
		    </div>
		</div>
	</fieldset>
</form>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/businessMgr/rateDiscount/rateDiscountMgrUpdate.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/rateDiscount";
		setPath(primaryPath,basePath);
</script>
</body>
</html>