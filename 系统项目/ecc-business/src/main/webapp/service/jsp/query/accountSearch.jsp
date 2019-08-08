<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>
<head>
<title>账户类查询</title>
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
							    <option value="001">当前账户申请流水</option>
								<option value="002">TA账户确认流水</option>
								<option value="003">二级清算账户确认流水</option>
								<option value="004">历史账户申请流水</option>
								<option value="005">历史二级清算账户确认流水</option>
								<option value="006">客户账户资料修改查询</option>
							  </select>
						</span>
					</div>
					<div class="form-item" id="appDatetd">
			          	<span class="form-field">申请日期：</span>
			           	<span class="form-input">
			           		<input type="hidden" id="hiddenAppDate" value="${startDate}"/>
 			           		<input type='text' readonly="readonly"  class='form-control white-bg' name='appDate' id='appDate' value="" />	
			            </span>
			        </div>
					
					<div class="form-item" id="startEndDate" style="display:none;">
			          	<span class="form-field">起止日期：</span>
			           	<span class="form-input">
     				        <input type="hidden" id="hiddenStartEnd" value="${startEndDate}"/>
 			           		<input type='text' readonly="readonly"  class='form-control white-bg' name='startEnd' id='startEnd'  />	
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
					
					
					<div class="form-item" id="apkindkey">
						<span class="form-field">业务类型：</span>
						<span class="form-input">
							  <select name="apkindapp" id="apkindapp" class="form-control use-select2" >
						  			<option value="">全部</option>
							  </select>
							  <select name="apkind" id="apkindack" class="form-control use-select2" style="display:none;">
					  			<option value="">全部</option>
					  			<option value="101">101 开户</option>
					  			<option value="102">102 销户</option>
					  			<option value="103">103 账户信息修改</option>
					  			<option value="104">104 基金账户冻结</option>
					  			<option value="105">105 基金账户解冻</option>
					  			<option value="106">106 基金账户卡挂失</option>
					  			<option value="107">107 基金账户卡解挂</option>
					  			<option value="108">108 增加交易账户</option>
					  			<option value="109">109 撤销交易账户</option>
					  		  </select>
							  
						</span>
					</div>
					<div class="form-item" id="tanokey">
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

	<!-- 当前账户 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id ="currentAcc">
	    <table id="currentAccInfoList"></table>
	    <div id="currentAccInfoPage"></div>
	</div>
	<!-- TA账户 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="TAAcc">
	    <table id="TAAccInfoList"></table>
	    <div id="TAAccInfoPage"></div>
	</div>
	<!-- 二级清算账户 -->	
	<div class="jqGrid_wrapper fullscreen-wrapper" id="secondCptAcc">
	    <table id="secondCptAccInfoList"></table>
	    <div id="secondCptAccInfoPage"></div>
	</div>
	<!-- 历史账户 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="historyAcc">
	    <table id="historyAccInfoList"></table>
	    <div id="historyAccInfoPage"></div>
	</div>
	<!-- 历史二级清算账户 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="history2ndAcc">
	    <table id="history2ndAccInfoList"></table>
	    <div id="history2ndAccInfoPage"></div>
	</div>
	<!-- 客户账户 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="customerAcc">
	    <table id="customerAccInfoList"></table>
	    <div id="customerAccInfoPage"></div>
	</div>
	
	<form action="" method="POST" id="exportForm" style="display:none;"></form>
	<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
	<script type="text/javascript" src="<%=context%>web/js/query/query.account.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/query/query.currentAcc.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/query/query.TAAcc.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/query/query.2ndCptAcc.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/query/query.historyAcc.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/query/query.history2ndAcc.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/query/query.customerAcc.js?20180703"></script>
	<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/accountSearch";
		setPath(primaryPath,basePath);
	</script> 

</body>
</html>