<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>客户信息列表</title>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<style type="text/css">
.table input[type='text'],select{
	max-width: 250px !important;
}

.select2{
	max-width: 250px !important;
}

.form-item-group table,table td{
	border: 0 !important;
}

</style>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	<div class="form-item-group form-horizontal" role="form">
		<table class="table table-bordered">
			<colgroup>
			  	<col width="15%"><col width="35%"><col width="15%"><col width="35%">
			</colgroup>
			<tbody id="querytable">
				<tr>
					<td style="text-align: center;">客户类别：</td>
					<td class="white-bg" colspan="3">
						<div class="col-sm-11 form-inner">
							<select name="selCustType" id="selCustType" class='form-control select2_init' style="width: 250px;">
								<option value="1">个人客户</option>
								<option value="0" selected>机构客户</option>
							</select>
						</div>
					</td>
				</tr>
			</tbody>
			
			<tbody id="querytable1" style="display: none;border: 0;">
				<tr>
					<td style="text-align: center;">基金账号：</td>
					<td class="white-bg" colspan="3">
						<div class="col-sm-11 form-inner">
							<input type='text' class='form-control' name='inputQueryFundAcc' id='inputQueryFundAcc'  style="width: 250px;"/>
						</div>
					</td>
				</tr>
			</tbody>
			
			<tbody id="querytable2" style="display: none;border: 0;">
				<tr>
					<td style="text-align: center;">客户简称：</td>
					<td class="white-bg">
						<div class="col-sm-11 form-inner">
							<select name="custsimpnm" id="custsimpnm" class='form-control select2_cust' onchange="getSecond('instrepcode',this.value);">
								<option value="--" selected="selected">--</option>
								<c:forEach var="item" items="${custFirst}">
									<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
								</c:forEach>
							</select>
							&nbsp;&nbsp;&nbsp;&nbsp;
							<select name="instrepcode" id="instrepcode" class='form-control select2_cust'>
								<option value="--" selected="selected">--</option>
							</select>
						</div>
					</td>
					<td style="text-align: center;">基金账号：</td>
					<td class="white-bg">
						<div class="col-sm-11 form-inner" style="line-height: 32px;">
							<input type='text' class='form-control' name='inputQueryFundAcc1' id='inputQueryFundAcc1' style="width: 250px;"/>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
    </div>
    <div class="form-action text-right">
        <button class="btn btn-primary" type="submit" id="btnQuery"><i class="fa fa-search"></i>&nbsp;查询</button>
        <button class="btn btn-outline btn-primary" id="resetBtn" type="reset">清空</button>
        <button class="btn btn-primary" type="button" id="btnUpdate"><i class="fa fa-pencil-square-o"></i>&nbsp;批量修改</button>
    </div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
	<input type="hidden" name="method" id="method" value="${type}">
    <table id="accountInfoList"></table>
    <div id="accountInfoPage"></div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/custAccountMgr/custAccountInfoList.js?v="<%=dateStr%>"></script>
<script type="text/javascript">
		var widgetPath = '<%=context%>service/widget/';
		var primaryPath = '<%=context%>service/custInfoManager/';
		setPath(widgetPath,primaryPath);
</script>
</body>
</html>