<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>
<head>
<title>综合查询查询</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
			
				<div class="form-item-group form-horizontal">
					<div class="form-item">
						<span class="form-field">查询选择：</span>
						<span class="form-input">
							  <select name="selBankName" id="selBankName" class="form-control use-select2" onChange="Process()">
							    <option value="001">账户信息查询</option>
								<option value="002">基金净值查询</option>
								<option value="003">基金余额查询</option>
								<option value="004">定投扣款失败查询</option>
							  </select>
						</span>
					</div>
					<div class="form-item" id="appDatetd" style="display:none;">
			          	<span class="form-field">查询日期：</span>
			           	<span class="form-input">
			           		<input type="hidden" id="hiddenAppDate" value = "${startDate }" />
 			           		<input type='text' readonly="readonly"  class='form-control white-bg' name='appDate' id='appDate' value="" />	
			            </span>
			        </div>
					
					<div class="form-item" id="startEndDate" style="display:none;">
			          	<span class="form-field">起止日期：</span>
			           	<span class="form-input">
			           		<input type="hidden" id="hiddenStartEnd" value="${startEndDate}" />
 			           		<input type='text' readonly="readonly"  class='form-control white-bg' name='startEnd' id='startEnd' value="" />	
			            </span>
			        </div>
			        
			        <div class="form-item" id="fundAcctKey">
			          	<span class="form-field">基金账号：</span>
			           	<span class="form-input">
 			           		<input type="text" name="txtFundAcct" id="txtFundAcct" value="" class='form-control'  />	
			            </span>
			        </div>
			        <div class="form-item" id="idNoKey">
			          	<span class="form-field">证件号码：</span>
			           	<span class="form-input">
 			           		<input type="text" name="txtIdNo" id="txtIdNo" value="" class='form-control '  />	
			            </span>
			        </div>
			        <div class="form-item" id="invNameKey">
			          	<span class="form-field">姓&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;名：</span>
			           	<span class="form-input">
 			           		<input type="text" name="txtInvName" id="txtInvName" value="" class='form-control'  />	
			            </span>
			        </div>
			        
			        <div class="form-item" id="selQueryWay">
						<span class="form-field">交易方式：</span>
						<span class="form-input">
							  <select name="queryWay" id="queryWay" class="form-control use-select2" >
						  			<option value="">全部</option>
						  			<option value="9999">网上</option>
						  			<option value="0001">网下</option>
							  </select>
						</span>
					</div>
					
					<div class="form-item" id="customerStatusDiv">
						<span class="form-field">客户状态：</span>
						<span class="form-input">
							  <select name="customerst" id="customerst" class="form-control use-select2" >
						  			<option value="">全部</option>
						  			<option value="C">C&nbsp;&nbsp;撤销</option>
						  			<option value="N">N&nbsp;&nbsp;正常</option>
							  </select>
						</span>
					</div>
					
					
					<div class="form-item" id="tanokey" style="display:none;">
						<span class="form-field">TA代码：</span>
						<span class="form-input">
							<select name="tano" id="tano" class="form-control use-select2">
					  			<option value="">全部</option>
					  			<option value="17">17</option>
					  			<option value="98">98</option>
  							</select>
						</span>
					</div>
					<div class="form-item" id="netpiontInfo">
						<span class="form-field">网点代码：</span>
						<span class="form-input">
							<select name="netPoint" id="netPoint" class="form-control use-select2">
					  			<option value="">全部</option>
	  							<option value="0755ZYT">招赢通</option>
  							</select>
						</span>
					</div>
					<div class="form-item"></div>
			         <div class="form-item"></div>
			         <div class="form-item"></div>
			</div>		
			<div class="form-action text-right">
					<button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp;查询</button>
					<button class="btn btn-primary" id="exportBtn"  onclick="exportDetail();">导出</button>
					<button class="btn btn-outline btn-primary" id="resetBtn" type="reset" onclick="resetSearchVal()">清空</button>
				</div>	
</div>

	<!-- 账户信息 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id ="accountInfo">
	    <table id="accountInfoList"></table>
	    <div id="accountInfoPage"></div>
	</div>
	<!-- 基金净值 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="fundClear">
	    <table id="fundClearInfoList"></table>
	    <div id="fundClearInfoPage"></div>
	</div>
	<!-- 基金余额 -->	
	<div class="jqGrid_wrapper fullscreen-wrapper" id="fundLast">
	    <table id="fundLastInfoList"></table>
	    <div id="fundLastInfoPage"></div>
	</div>
	<!-- 定投扣款失败 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="castFailure">
	    <table id="castFailureInfoList"></table>
	    <div id="castFailureInfoPage"></div>
	</div>
	<form action="" method="POST" id="exportForm" style="display:none;"></form>
	<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
	<script type="text/javascript" src="<%=context%>web/js/multiple/query.multiple.js?201800703"></script>
	<script type="text/javascript" src="<%=context%>web/js/multiple/query.accountInfo.js?201800703"></script>
	<script type="text/javascript" src="<%=context%>web/js/multiple/query.fundClear.js?201800703"></script>
	<script type="text/javascript" src="<%=context%>web/js/multiple/query.fundLast.js?201800703"></script>
	<script type="text/javascript" src="<%=context%>web/js/multiple/query.castFailure.js?201800703"></script>
	<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/multipleSearch";
		setPath(primaryPath,basePath);
	</script> 

</body>
</html>