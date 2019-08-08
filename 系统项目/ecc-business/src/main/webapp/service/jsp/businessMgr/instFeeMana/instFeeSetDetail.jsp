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
	<title>柜台费率折扣详细信息-业务管理-直销柜台</title>
	<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
</head>
<body class="sub-page">
<form name="" id="" method="post" action="" class="form-horizontal">
		<div class="page-content">
			<div class="page-header">
				<h4 class="page-title">柜台费率折扣详细信息</h4>
			</div>
			<div class="page-body">
				<div class="form-group">
					<label class="col-sm-2 control-label">机构类型<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<p class="form-control">${instFeeVo.instTypeName}</p>
					</div>
					<label class="col-sm-2 control-label">基金名称<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<p class="form-control">${instFeeVo.fundName}</p>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">业务类型<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<p class="form-control">${instFeeVo.apkindName}</p>
					</div>
					<label class="col-sm-2 control-label">费率折扣<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<p class="form-control">${instFeeVo.discount}</p>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">起始金额(含)<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<p class="form-control">${instFeeVo.strAmt}</p>
					</div>
					<label class="col-sm-2 control-label">结束金额(不含)<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<p class="form-control">${instFeeVo.endAmt}</p>
					</div>
				</div>
				
				<div class="form-group">
					<label class="col-sm-2 control-label">状态<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<p class="form-control">${instFeeVo.statusName}</p>
					</div>
					<label class="col-sm-2 control-label">复核状态<span class="text-danger">*</span></label>
					<div class="col-sm-4 form-inner">
						<p class="form-control">${instFeeVo.checkStatusName}</p>
					</div>
				</div>
			</div>
			
			<div class="page-footer">
		         <button type="button" class="btn btn-primary btn-save" onclick="window.close();" id="closeBtn" name="closeBtn">关闭</button>
		    </div>
		</div>
	</form>
</body>
</html>