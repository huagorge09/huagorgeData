<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
#risklevelRadio{
	position:relative;
}
#lowest{
	position:absolute;
	left : 0px;
	top : 6px;
}
</style>
<title>交易资料管理</title>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	<div class="form-item-group form-horizontal">
			
			<div class="form-item" id="startDateDiv">
	          	<span class="form-field">开始时间：</span>
	           	<span class="form-input">
	           		<input type="hidden" id="hiddenStartDate" value="${startDate }"/>
		           	<input type="text" name="startDate" id="startDate" value="${startDate }" class='form-control'  />	
	            </span>
	        </div>
			
			 <div class="form-item" id="endDateDiv">
	          	<span class="form-field">结束时间：</span>
	           	<span class="form-input">
   		           		<input type="hidden" id="hiddenEndDate" value="${endDate }"/>
		           		<input type="text" name="endDate" id="endDate" value="${endDate }" class='form-control'  />	
	            </span>
	        </div>
			
			<div class="form-item" id="filenoDiv">
	          	<span class="form-field">文件编号：</span>
	           	<span class="form-input">
		           		<input type='text' class='form-control white-bg' name='fileno' id='fileno' value="" />	
	            </span>
	        </div>
			
			<div class="form-item" id="customerNameDiv">
	          	<span class="form-field">客户名称：</span>
	           	<span class="form-input">
		           		<input type='text' class='form-control white-bg' name='invnm' id='invnm' />	
	            </span>
	        </div>
			
			<div class="form-item" id="fundnmDiv">
	          	<span class="form-field">产品名称：</span>
	           	<span class="form-input">
		           		<input type='text'  class='form-control white-bg' name='fundnm' id='fundnm' value="" />	
	            </span>
	        </div>
			
			<div class="form-item" id="tradeAmtDiv">
	          	<span class="form-field">交易金额：</span>
	           	<span class="form-input">
		           		<input type='text'  class='form-control white-bg' name='tradeAmt' id='tradeAmt' value="" />	
	            </span>
	        </div>
	        
	        <div class="form-item" id="contractDiv">
	          	<span class="form-field">合同签署：</span>
	           	<span class="form-input">
	           		 <select name="contract" id="contract" class="form-control use-select2">
 					    <option value=""></option> 
					  </select>
	            </span>
	        </div>
	        <div class="form-item" id="invtpDiv">
	          	<span class="form-field">是否移交：</span>
	           	<span class="form-input">
	           		 <select name="isChange" id="isChange" class="form-control use-select2">
 					    <option value=""></option> 
						<option value="Y">是</option>
						<option value="N">否</option>
					  </select>
	            </span>
	        </div>
	        
	        <div class="form-item" id="invtpDiv">
	          	<span class="form-field">合同是否原件：</span>
	           	<span class="form-input">
	           		 <select name="isOriginal" id="isOriginal" class="form-control use-select2">
 					    <option value=""></option> 
						<option value="Y">是</option>
						<option value="N">否</option>
					  </select>
	            </span>
	        </div>
	        
	        <div class="form-item" id="invtpDiv">
	          	<span class="form-field">是否归档：</span>
	           	<span class="form-input">
	           		 <select name="isFiled" id="isFiled" class="form-control use-select2">
 					    <option value=""></option> 
						<option value="Y">是</option>
						<option value="N">否</option>
					  </select>
	            </span>
	        </div>
	        
	        <div class="form-item"></div>
	        <div class="form-item"></div>
	        <div class="form-item"></div>
        </div>		
		<div class="form-action text-right">
				<button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp;查询</button>
				<button class="btn btn-primary" id="exportBtn"  onclick="exportDetail();">&nbsp;导出</button>
				<button class="btn btn-outline btn-primary" id="resetBtn" type="reset" onclick="resetQueryCondtion()">清空</button>
		</div>	
</div>
	<!-- 交易资料管理  -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id ="tradeDataMgr">
	    <table id="tradeDataInfoList"></table>
	    <div id="tradeDataInfoPage"></div>
	</div>
	<form action="" method="POST" id="exportForm" style="display:none;"></form>
	<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
	<script type="text/javascript" src="<%=context%>web/js/common/common.js?201806252131230"></script>
	<script type="text/javascript" src="<%=context%>web/js/tradeDataMgr/tradeDataMgr.init.js?20180704"></script>	
	<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/tradeDataManager";
		setPath(primaryPath,basePath);
	</script> 
</body>
</html>