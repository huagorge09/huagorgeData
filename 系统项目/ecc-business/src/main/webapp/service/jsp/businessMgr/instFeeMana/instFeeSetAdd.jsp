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
<form name="instFeeAddForm" id="instFeeAddForm" method="post" action="<%=context%>service/instFeeSet/addInstFeeSetInfo.do" class="form-horizontal">
	<fieldset>
		<div class="page-content">
			<div class="page-header">
				<h4 class="page-title">增加柜台费率折扣信息</h4>
			</div>
			<div class="page-body">
				<div class="form-group">
					<label class="col-sm-2 control-label">机构类型<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select class="form-control use-select2" name="instType" id="instType" param='{"pmst":"SYSTEM","pmky":"INSTTYPE"}'></select>
					</div>
					<label class="col-sm-2 control-label">基金名称<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select class="form-control use-select2" name="fundId" id="fundId"></select>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">业务类型<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select placeholder="业务类型" class="form-control use-select2" name="apkind" id="apkind" >
							<option value="020">认购</option>
		            		<option value="022">申购</option>
		            		<option value="024">赎回</option>
		            		<option value="036">转换</option>
						</select>
					</div>
					<label class="col-sm-2 control-label">费率折扣<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<input type="text" placeholder="费率折扣" class="form-control" name="discount" id="discount" value="" />
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">起始金额(含)<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<input type="text" placeholder="起始金额(含)" class="form-control" name="strAmt" id="strAmt" value="0" />
					</div>
					<label class="col-sm-2 control-label">结束金额(不含)<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<input type="text" placeholder="结束金额(不含)" class="form-control" name="endAmt" id="endAmt" value="500000" />
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
<script type="text/javascript" src="<%=context%>web/js/businessMgr/instFeeMana/instFeeSetAdd.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/instFeeSet";
		setPath(primaryPath,basePath);
</script>
</body>
</html>