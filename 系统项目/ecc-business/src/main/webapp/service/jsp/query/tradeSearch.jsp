<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>
<head>
<title>交易类查询</title>
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
							    <option value="001">当前交易申请流水</option>
								<option value="002">资金流水查询</option>
								<option value="003">二级清算交易确认流水</option>
								<option value="004">TA交易确认流水</option>
								<option value="005">当天分红明细</option>
								<option value="006">历史分红明细</option>
								<option value="007">历史交易申请流水</option>
								<option value="008">历史二级清算交易确认流水</option> 
							  </select>
						</span>
					</div>
					
					<div class="form-item" id="startEndDate" style="display:none;">
			          	<span class="form-field">起止日期：</span>
			           	<span class="form-input">
			           		<input type="hidden" id = "hiddenStartEnd" value = "${startEndDate}"/>
 			           		<input type='text' readonly="readonly"  class='form-control white-bg' name='startEnd' id='startEnd'  />	
			            </span>
			        </div>
					
					<div class="form-item" id="appDatetd">
			          	<span class="form-field">申请日期：</span>
			           	<span class="form-input">
       				        <input type="hidden" id = "hiddenAppDate" value = "${startDate}"/>
 			           		<input type='text' readonly="readonly"  class='form-control white-bg' name='appDate' id='appDate' />	
			            </span>
			        </div>
					
					<div class="form-item" id="apkindkey">
						<span class="form-field">业务类型：</span>
						<span class="form-input">
							  <select name="selApkind" id="selApkindOne" class="form-control use-select2" >
						  			<option value="">全部</option>
						  			<option value='020' >020 认购</option>
									<option value='022' >022 申购</option>
									<option value='039' >039 定期定额申购</option>
									<option value='024' >024 赎回</option>
									<option value='043' >043 红利发放</option>
									<option value='026' >026 转托管</option>
									<option value='027' >027 转托管转入</option>
									<option value='028' >028 转托管转出</option>
									<option value='036' >036 基金转换</option>
									<option value='098' >098 快速赎回</option>
							  </select>
							  <select name="selApkind" id="selApkindTwo" class="form-control use-select2" style="display:none;">
					  			<option value="">全部</option>
					  			<option value='120' >120 认购</option>
								<option value='130' >130 认购结果</option>
								<option value='122' >122 申购</option>
								<option value='139' >139 定期定额申购</option>
								<option value='124' >124 赎回</option>
								<option value='143' >143 红利发放</option>
								<option value='126' >126 转托管</option>
								<option value='127' >127 转托管转入</option>
								<option value='128' >128 转托管转出</option>
								<option value='136' >136 基金转换</option>
								<option value='198' >198 快速赎回</option>
					  		  </select>
							  
						</span>
					</div>
			        
			        <div class="form-item" id="fundaccoid">
			          	<span class="form-field">基金账号：</span>
			           	<span class="form-input">
 			           		<input type="text" name="fundacco" id="fundacco" value="" class='form-control'  />	
			            </span>
			        </div>
			        <div class="form-item" id="custnameid">
			          	<span class="form-field">投资者名称：</span>
			           	<span class="form-input">
 			           		<input type="text" name="custname" id="custname" value="" class='form-control '  />	
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
			        
			        <div class="form-item" id="seltano">
						<span class="form-field">TA代码：</span>
						<span class="form-input">
							<select name="tano" id="tano" class="form-control use-select2">
					  			<option value="">全部</option>
					  			<option value="17">17</option>
					  			<option value="98">98</option>
  							</select>
						</span>
					</div>
			        
			        
			        <div class="form-item" id="productCode">
			          	<span class="form-field">产品代码：</span>
			           	<span class="form-input">
 			           		<input type="text" name="fundCode" id="fundCode" value="" class='form-control'  />	
			            </span>
			        </div>
			        
			        <div class="form-item" id="productName">
			          	<span class="form-field">产品名称：</span>
			           	<span class="form-input">
 			           		<input type="text" name="fundName" id="fundName" value="" class='form-control'  />	
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
					<button class="btn btn-primary" id="exportBtn"  onclick="exportDetail();"><i class="fa fa-search"></i>&nbsp;导出</button>
					<button class="btn btn-outline btn-primary" id="resetBtn" type="reset" onclick="resetSearchVal()">清空</button>
				</div>	
</div>
	<!-- 当前交易 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id ="currentTrade">
	    <table id="currentTradeInfoList"></table>
	    <div id="currentTradeInfoPage"></div>
	</div>
	<!-- 资金流水 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="fundTrade">
	    <table id="fundTradeInfoList"></table>
	    <div id="fundTradeInfoPage"></div>
	</div>
	<!-- 二级清算交易 -->	
	<div class="jqGrid_wrapper fullscreen-wrapper" id="secondCptTrade">
	    <table id="secondCptTradeInfoList"></table>
	    <div id="secondCptTradeInfoPage"></div>
	</div>
	<!-- TA交易 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="TATrade">
	    <table id="TATradeInfoList"></table>
	    <div id="TATradeInfoPage"></div>
	</div>
	<!-- 当天分红 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="curDayMelonmd">
	    <table id="currentDayMelonmdInfoList"></table>
	    <div id="currentDayMelonmdPage"></div>
	</div>
	<!-- 历史分红-->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="hisMelonmd">
	    <table id="historyMelonmdInfoList"></table>
	    <div id="historyMelonmdInfoPage"></div>
	</div>
	<!-- 历史交易 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="hisTrade">
	    <table id="historyTradeInfoList"></table>
	    <div id="historyTradeInfoPage"></div>
	</div>
	<!-- 历史二级交易 -->
	<div class="jqGrid_wrapper fullscreen-wrapper" id="his2ndTrade">
	    <table id="history2ndTradeInfoList"></table>
	    <div id="history2ndTradeInfoPage"></div>
	</div>
	
	<form action="" method="POST" id="exportForm" style="display:none;"></form>
	<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
	<script type="text/javascript" src="<%=context%>web/js/trade/query.trade.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/trade/query.currentTrade.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/trade/query.fundTrade.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/trade/query.2ndCptTrade.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/trade/query.TATrade.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/trade/query.curDayMelonmd.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/trade/query.hisMelonmd.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/trade/query.historyTrade.js?20180703"></script>
	<script type="text/javascript" src="<%=context%>web/js/trade/query.history2ndTrade.js?20180703"></script>
	<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/tradeSearch";
		setPath(primaryPath,basePath);
	</script> 

</body>
</html>