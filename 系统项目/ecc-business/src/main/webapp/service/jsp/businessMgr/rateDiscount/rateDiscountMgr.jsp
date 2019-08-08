<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>费率折扣管理-业务管理-直销柜台</title>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	<div class="form-search-group">
	    <a class="btn btn-primary" id="rateDiscAddViewBtn" href="javascript:rateFunc.addRateDiscount();"><i class="fa fa-plus"></i>&nbsp;新建</a>
	    <a class="btn btn-primary" id="rateDiscCopyViewBtn" href="javascript:rateFunc.copyRateDiscount();"><i class="fa fa-plus"></i>&nbsp;复制</a>
	    <div class="permissionBtn" style="display: none;" id="rateDiscUpdateBtn">
			<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改" disabled ><i class="fa fa-pencil-square-o"></i></a>
		</div>
		<div class="permissionBtn" style="display: none;" id="rateDiscDelBtn">
			<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="删除" disabled ><i class="fa fa-trash-o"></i></a>
		</div>
	</div>
	<div class="hr-line-dotted"></div>
	<div class="form-item-group form-horizontal" role="form">
         <div class="form-item">
            <span class="form-field">银行名称：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="q-bankName" id="q-bankName"></select>
            </span>
         </div>
         <div class="form-item"></div>
         <div class="form-item"></div>
   		 <div class="form-item"></div>
         <div class="form-item"></div>
    </div>
    <div class="form-action text-right">
        <button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp;查询</button>
        <button class="btn btn-outline btn-primary" id="resetBtn" type="reset">清空</button>
    </div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="rateDiscountList"></table>
    <div id="rateDiscountPage"></div>
</div>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/businessMgr/rateDiscount/rateDiscountMgr.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/rateDiscount";
		setPath(primaryPath,basePath);
</script>
</body>
</html>