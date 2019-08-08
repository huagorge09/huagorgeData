<%@ page language="java" contentType="text/html; charset=UTF-8" isELIgnored="false" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户资料管理-资料管理-直销柜台</title>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
</head>
<body class="fixed-nav gray-bg">
<input type="hidden" name="currEmpId" value="${currEmpId}"/>
<div class="clearfix form-multi-col-panel">
	<div class="form-search-group">
	    <div class="permissionBtn" style="display: none;" id="custDataUpdateBtn">
			<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改" disabled ><i class="fa fa-pencil-square-o"></i></a>
		</div>
		<div class="permissionBtn" style="display: none;" id="goTradeDataBtn">
			<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="交易资料" >交易资料</a>
		</div>
	</div>
	<div class="hr-line-dotted"></div>
	<div class="form-item-group form-horizontal" role="form">
         <div class="form-item">
            <span class="form-field">开始时间：</span>
            <span class="form-input">
            	<input type="hidden" name="hidStrDate" value="${strDate }" />
            	<input type="text" placeholder="开始时间" class="form-control" name="strDate" id="strDate"/>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">结束时间：</span>
            <span class="form-input">
            	<input type="hidden" name="hidEndDate" value="${endDate }" />
            	<input type="text" placeholder="结束时间" class="form-control" name="endDate" id="endDate"/>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">文件编号：</span>
            <span class="form-input">
            	<input type="text" placeholder="文件编号" class="form-control" name="fileno" id="fileno"/>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">基金账号：</span>
            <span class="form-input">
            	<input type="text" placeholder="基金账号" class="form-control" name="fundacco" id="fundacco"/>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">客户名称：</span>
            <span class="form-input">
            	<input type="text" placeholder="客户名称" class="form-control" name="invnm" id="invnm"/>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">业务类型：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="apkind" id="apkind">
            		<option value="">请选择</option>
            		<option value="001">开户</option>
            		<option value="003">账户信息修改</option>
            		<option value="0B1">银行资料修改</option>
            	</select>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">是否原件：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="isoriginal" id="isoriginal">
            		<option value="">请选择</option>
            		<option value="1">是</option>
            		<option value="0">否</option>
            	</select>
            </span>
         </div>
         
          <div class="form-item">
            <span class="form-field">是否齐全：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="isalldoc" id="isalldoc">
            		<option value="">请选择</option>
            		<option value="1">是</option>
            		<option value="0">否</option>
            	</select>
            </span>
         </div>
         
         <div class="form-item">
            <span class="form-field">是否扫描：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="isscan" id="isscan">
            		<option value="">请选择</option>
            		<option value="Y">是</option>
            		<option value="N">否</option>
            	</select>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">是否归档：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="issaved" id="issaved">
            		<option value="">请选择</option>
            		<option value="1">是</option>
            		<option value="0">否</option>
            	</select>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">归档位置：</span>
            <span class="form-input">
            	<input type="text" placeholder="归档位置" class="form-control" name="keepaddress" id="keepaddress"/>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">是否上传：</span>
            <span class="form-input">
            	<select class="form-control use-select2" name="isupload" id="isupload">
            		<option value="">请选择</option>
            		<option value="1">是</option>
            		<option value="0">否</option>
            	</select>
            </span>
         </div>
         <div class="form-item">
            <span class="form-field">所属客户经理：</span>
            <span class="form-input">
            	<input type="text" placeholder="所属客户经理" class="form-control" name="salesaccmanager" id="salesaccmanager"/>
            </span>
         </div>
         <div class="form-item"></div>
         <div class="form-item"></div>
    </div>
    <div class="form-action text-right">
        <button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp;查询</button>
        <a class="btn btn-primary" id="custDataExportBtn" href="javascript:custDataFunc.custDataExport();">导&nbsp;&nbsp;出</a>
        <button class="btn btn-outline btn-primary" id="resetBtn" type="reset" onclick="resetQueryCondtion()">清空</button>
    </div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="custDataList"></table>
    <div id="custDataPage"></div>
</div>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/dataManager/custDataManager/custDataManagerIndex.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/custDataManager";
		setPath(primaryPath,basePath);
</script>
</body>
</html>