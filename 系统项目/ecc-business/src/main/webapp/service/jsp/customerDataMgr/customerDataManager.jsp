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
<title>客户评估数据管理</title>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	<div class="form-item-group form-horizontal">
			<div class="form-item" id="customerNameDiv">
	          	<span class="form-field">客户名称：</span>
	           	<span class="form-input">
		           		<input type='text' class='form-control white-bg' name='idnm' id='idnm' value="" />	
	            </span>
	        </div>
			
			<div class="form-item" id="fundacctDiv">
	          	<span class="form-field">基金账号：</span>
	           	<span class="form-input">
		           		<input type='text'  class='form-control white-bg' name='fundacct' id='fundacct' value="" />	
	            </span>
	        </div>
	        
	        <div class="form-item" id="startDateDiv">
	          	<span class="form-field">评估过期日期开始：</span>
	           	<span class="form-input">
		           		<input type="text" name="startDate" id="startDate" value="" class='form-control'  />	
	            </span>
	        </div>
	        
	        <div class="form-item" id="endDateDiv">
	          	<span class="form-field">评估过期日期结束：</span>
	           	<span class="form-input">
		           		<input type="text" name="endDate" id="endDate" value="" class='form-control'  />	
	            </span>
	        </div>
	        
	        <div class="form-item" id="invtpDiv">
	          	<span class="form-field">客户类型：</span>
	           	<span class="form-input">
	           		 <select name="invtp" id="invtp" class="form-control use-select2">
					    <option value="">全部</option>
						<option value="1">个人客户</option>
						<option value="0">机构客户</option>
					  </select>
	            </span>
	        </div>
	        
	        <div class="form-item" id="invprtpDiv">
	          	<span class="form-field">投资者类型：</span>
	           	<span class="form-input">
					  <select name="invprtp" id="invprtp" class="form-control use-select2">
					    <option value="">全部</option>
						<option value="1">普通投资者</option>
						<option value="0">专业投资者</option>
					  </select>	            
			    </span>
	        </div>
	        
	        <div class="form-item" id="risklevelDiv">
	          	<span class="form-field">风险承受能力：</span>
	           	<span class="form-input">
					  <select name="risklevel" id="risklevel" class="form-control use-select2">
					    <option value="">全部</option>
						<option value="999">未评估客户</option>
					  </select>
				</span>
	        </div>
	        
			<div class="form-item lowest" id="risklevelRadio">
			<span class="form-field" id="risklevelSpan"></span>
	           	<span class="form-input">
   			 		  &nbsp;<label id="lowest"><input  id="specRiskLevel" name="specRiskLevel" value="specRiskLevel" type="checkbox" onclick="changeSpecRiskLevel(this,'risklevel');"/>&nbsp;最低</label>
				</span>
	        </div>	        
	        <div class="form-item"></div>
	        <div class="form-item"></div>
	        <div class="form-item"></div>
        </div>		
		<div class="form-action text-right">
				<button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp;查询</button>
				<button class="btn btn-primary" id="exportBtn"  onclick="exportDetail();">&nbsp;导出</button>
				<button class="btn btn-outline btn-primary" id="resetBtn" type="reset" onclick="resetSearchVal()">清空</button>
		</div>	
</div>
	<!-- 客户评估数据管理  -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id ="customerDataMgr">
	    <table id="customerDataInfoList"></table>
	    <div id="customerDataInfoPage"></div>
	</div>
	<form action="" method="POST" id="exportForm" style="display:none;"></form>
	<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
	<script type="text/javascript" src="<%=context%>web/js/common/common.js?20180620"></script>
	<script type="text/javascript" src="<%=context%>web/js/customerDataManager/customerDataManager.init.js?20180716"></script>	
	<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/customerDataManager";
		setPath(primaryPath,basePath);
	</script> 
</body>
</html>