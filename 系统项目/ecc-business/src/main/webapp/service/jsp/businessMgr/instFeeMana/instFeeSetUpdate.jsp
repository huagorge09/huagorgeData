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
	<title>新增柜台费率折扣-业务管理-直销柜台</title>
	<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
</head>
<body class="sub-page">
<form name="instFeeUpdateForm" id="instFeeUpdateForm" method="post" action="<%=context%>service/instFeeSet/updateInstFeeSetInfo.do" class="form-horizontal">
	<fieldset>
		<div class="page-content">
			<div id="hiddenParam" style="display: none;">
				<input name="serialNo" value="${instFeeVo.serialNo}"/>
				<input name="instType" value="${instFeeVo.instType}"/>
				<input name="instTypeName" value="${instFeeVo.instTypeName}"/>
				
				<input name="fundId" value="${instFeeVo.fundId}"/>
				<input name="fundName" value="${instFeeVo.fundName}"/>
				
				<input name="apkind" value="${instFeeVo.apkind}"/>
				<input name="apkindName" value="${instFeeVo.apkindName}"/>
				
				<input name="hidStatus" value="${instFeeVo.status}"/>
				<input name="hidStatusName" value="${instFeeVo.statusName}"/>
			</div>
			<div class="page-header">
				<h4 class="page-title">增加柜台费率折扣信息</h4>
			</div>
			<div class="page-body">
				<div class="form-group">
					<label class="col-sm-2 control-label">机构类型<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select class="form-control use-select2" name="showInstType" id="showInstType"  disabled="disabled"></select>
					</div>
					<label class="col-sm-2 control-label">基金名称<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select class="form-control use-select2" name="showFundId" id="showFundId" disabled="disabled"></select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">业务类型<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select placeholder="业务类型" class="form-control use-select2" name="showApkind" id="showApkind" disabled="disabled"></select>
					</div>
					<label class="col-sm-2 control-label">费率折扣<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<input type="text" placeholder="费率折扣" class="form-control" name="discount" id="discount" value="${instFeeVo.discount}" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">起始金额(含)<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<input type="text" placeholder="起始金额(含)" class="form-control" name="strAmt" id="strAmt" value="${instFeeVo.strAmt}" />
					</div>
					<label class="col-sm-2 control-label">结束金额(不含)<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<input type="text" placeholder="结束金额(不含)" class="form-control" name="endAmt" id="endAmt" value="${instFeeVo.endAmt}" />
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
<script type="text/javascript" src="<%=context%>web/js/businessMgr/instFeeMana/instFeeSetUpdate.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/instFeeSet";
		setPath(primaryPath,basePath);
</script>
</body>
</html>