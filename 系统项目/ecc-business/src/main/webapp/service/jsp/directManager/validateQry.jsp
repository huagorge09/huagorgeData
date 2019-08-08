<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>过期证件管理-直销管理-直销柜台</title>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
</head>
<body class="fixed-nav gray-bg">
<input type="hidden" name="currEmpId" value="${currEmpId}"/>
<div class="clearfix form-multi-col-panel">
	<div class="form-search-group">
	    <div class="permissionBtn" style="display: none;" id="validateUpdateBtn">
			<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改" disabled ><i class="fa fa-pencil-square-o"></i></a>
		</div>
	</div>
	<div class="form-item-group form-horizontal" role="form">
		<div class="form-item">
            <span class="form-field">查询类别：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="operatorType" id="operatorType" onchange="operatorTypeChange(this)">
            		<option value="0">客户证件有效期</option>
		  			<option value="1">经办人证件有效期</option>
		  			<option value="2">法人证件有效期</option>
		  			<option value="5">其他证件有效期</option>
            	</select>
            </span>
         </div>
		<div class="form-item">
            <span class="form-field">交易账号：</span>
            <span class="form-input">
            	<input name="tradeacco" class="form-control" type="text" placeholder="交易账号"/>
            </span>
         </div>
         
         <div class="form-item">
            <span class="form-field">基金账号：</span>
            <span class="form-input">
            	<input name="fundacco" class="form-control" type="text" placeholder="基金账号" />
            </span>
         </div>
         
        <div class="form-item">
            <span class="form-field">客户类别：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="invtp" id="invtp" param='{"pmst":"SYSTEM","pmky":"INVTP"}' onchange="loadIdtp(this.value)">
            		<option value="">请选择</option>
            	</select>
            </span>
         </div>
		
		<div class="form-item">
            <span class="form-field">证件号码：</span>
            <span class="form-input">
            	<input name="idno" class="form-control" type="text" placeholder="证件号码" />
            </span>
         </div>
         
         <div class="form-item">
            <span class="form-field">客户名称：</span>
            <span class="form-input">
            	<input name="idnm" class="form-control" type="text" placeholder="客户名称" />
            </span>
         </div>
        
        <div class="form-item">
            <span class="form-field">证件类型：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="idtp" id="idtp" ><!-- param='{"pmst":"SYSTEM","pmky":"IDTP","PMV1":"1"}' -->
            		<option value="">请选择</option>
            	</select>
            </span>
         </div>
         
         <div class="form-item">
            <span class="form-field">开始日期：</span>
            <span class="form-input">
            	<input name="begindate" id="begindate" class="form-control" type="text" placeholder="开始日期" value="${begindate}"/>
            	<input type="hidden" name="hidBegindate" value="${begindate}"/>
            </span>
         </div>
         
         <div class="form-item">
            <span class="form-field">结束日期：</span>
            <span class="form-input">
            	<input name="enddate" id="enddate" class="form-control" type="text" placeholder="结束日期" value="${enddate}"/>
            	<input type="hidden" name="hidEnddate" value="${enddate}"/>
            </span>
         </div>
         <div class="form-item">
         	<label>
               		<input type="checkbox" name="defaultEndDat" id="defaultEndDat"/> 长期有效
               </label>
         </div>
    </div>
    <div class="form-action text-right">
        <button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp;查询</button>
        <button class="btn btn-outline btn-primary" id="resetBtn" type="reset">清空</button>
    </div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="custInfoList"></table>
    <div id="custInfoPage"></div>
</div>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/directManager/validateQry.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/dirctManager/validate";
		setPath(primaryPath,basePath);
</script>
</body>
</html>