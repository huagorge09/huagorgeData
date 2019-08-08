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
	<title>复制费率折扣-业务管理-直销柜台</title>
	<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
</head>
<body class="sub-page">
<form name="rateDiscountCopyForm" id="rateDiscountCopyForm" method="post" action="<%=context%>service/rateDiscount/copyRateDisc.do" class="form-horizontal">
	<fieldset>
		<div class="page-content">
			<div class="page-header">
				<h4 class="page-title">复制费率折扣信息</h4>
			</div>
			<div class="page-body">
				<div class="form-group">
					<label class="col-sm-2 control-label">源商户产品ID(基金代码)<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select class="form-control use-select2" name="souProductId" id="souProductId"></select>
					</div>
					<label class="col-sm-2 control-label">目的商户产品ID(基金代码)<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<select class="form-control use-select2" name="tarProductId" id="tarProductId"></select>
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
<script type="text/javascript" src="<%=context%>web/js/businessMgr/rateDiscount/rateDiscountMgrCopy.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/rateDiscount";
		setPath(primaryPath,basePath);
</script>
</body>
</html>