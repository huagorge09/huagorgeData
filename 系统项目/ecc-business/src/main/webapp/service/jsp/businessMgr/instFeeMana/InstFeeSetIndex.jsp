<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>财富柜台费率设置-业务管理-直销柜台</title>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
</head>
<body class="fixed-nav gray-bg">
<input type="hidden" name="currEmpId" value="${currEmpId}"/>
<div class="clearfix form-multi-col-panel">
	<div class="form-search-group">
	    <a class="btn btn-primary" id="rateDiscAddViewBtn" href="javascript:instFeeFunc.addInstFeeSetView();"><i class="fa fa-plus"></i>&nbsp;新建</a>
	    <div class="permissionBtn" style="display: none;" id="instFeeInfoBtn">
			<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="详情" disabled ><i class="fa fa-file-text-o"></i></a>
		</div>
	    <div class="permissionBtn" style="display: none;" id="instFeeUpdateBtn">
			<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改" disabled ><i class="fa fa-pencil-square-o"></i></a>
		</div>
	    <div class="permissionBtn" style="display: none;" id="instFeeCheckBtn">
			<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="复核" disabled ><i class="iconfont icon-wodeshenpi-copy"></i></a>
		</div>
		<div class="permissionBtn" style="display: none;" id="instFeeDelBtn">
			<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="删除" disabled ><i class="fa fa-trash-o"></i></a>
		</div>
	</div>
	<div class="hr-line-dotted"></div>
	<div class="form-item-group form-horizontal" role="form">
         <div class="form-item">
            <span class="form-field">机构类型：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="q-instType" id="q-instType" param='{"pmst":"SYSTEM","pmky":"INSTTYPE"}' multiple="multiple"></select>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">基金名称：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="q-fundId" id="q-fundId" multiple="multiple"></select>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">业务类型：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="q-apkind" id="q-apkind">
            		<option value="">请选择</option>
            		<option value="020">认购</option>
            		<option value="022">申购</option>
            		<option value="024">赎回</option>
            		<option value="036">转换</option>
            	</select>
            </span>
         </div>
   		 <div class="form-item"></div>
         <div class="form-item"></div>
    </div>
    <div class="form-action text-right">
        <button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp;查询</button>
        <button class="btn btn-outline btn-primary" id="resetBtn" type="reset">清空</button>
    </div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="instFeeSetList"></table>
    <div id="instFeeSetPage"></div>
</div>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/businessMgr/instFeeMana/instFeeSetIndex.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/instFeeSet";
		setPath(primaryPath,basePath);
</script>
</body>
</html>