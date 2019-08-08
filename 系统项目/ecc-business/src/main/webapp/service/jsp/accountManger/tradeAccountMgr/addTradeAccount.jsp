<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>增开交易账号</title>
<style type="text/css">
.table input[type='text'],select{
	max-width: 250px !important;
}

.select2{
	max-width: 250px !important;
}
.btn-query{
	position: absolute;
    margin-left: 20px;
    padding: 3px 10px;
    margin-top: 3px;
}

.btn-add{
	padding: 3px 10px;
    float: right;
    margin-right: 60px;
}
input[type="radio"], input[type="checkbox"]{
	margin: 2px 8px;
}

.select_addr1{
	width: 115px !important;
	float: left;
	margin-right: 12px;
}
.select_addr2{
	width: 115px !important;
	float: left;
}
.table > tbody + tbody{
	border-top: 0 !important;
}
</style>
<script type="text/javascript">
var widgetPath = '<%=context%>service/widget/';
var accountPath = '<%=context%>service/tradeAccountManager/';
//默认加载
$(function(){
	//把访问路径传到js
	setPath(widgetPath,accountPath);
});
</script>
</head>
<body class="fixed-nav gray-bg">
	<div class="clearfix form-multi-col-panel">
		<input id="permissionId" type="hidden" value="${ permissionId }"/>
		<input id="operatorId" 	 type="hidden" value="${ operatorId }"/>
		<input name="custno" type="hidden" id="custno"/>
		<form id="query_account" name="query_account" action="">
			<div class="form-item-group form-horizontal" role="form">
		         <div class="form-item">
		            <span class="form-field">交易账号：</span>
		            <span class="form-input">
		            	<input type='text' class='form-control' name='inputQueryTradeAcc' id='inputQueryTradeAcc' />
		            </span>
		         </div>
		         <div class="form-item">
		            <span class="form-field">基金账号：</span>
		            <span class="form-input">
		            	<input type='text' class='form-control' name='inputQueryFundAcc' id='inputQueryFundAcc' />
		            </span>
		         </div>
		         <div class="form-item"></div>
		   		 <div class="form-item"></div>
		         <div class="form-item"></div>
		    </div>
	    
		    <div class="form-action text-right">
		        <button class="btn btn-primary" type="submit"  id="btnQuery"><i class="fa fa-search"></i>&nbsp;查询</button>
		        <button class="btn btn-outline btn-primary" id="resetBtn" type="reset">清空</button>
		    </div>
	    </form>
	</div>
	
	<div class="modal-content" style="border: 0px;box-shadow: 0 0px 0px;">
	 	<form name="openCustomFrom" id="openCustomFrom" method="post" action="" >
		<div class="page-body">
			<table id="showTable" class="table table-bordered">
				<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
				<tbody>
					<tr>
						<th rowspan="3">基本信息</th>
						<td>客户名称：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="custNm" name="custNm"></span>
							</span>
						</td>
						<td>客户类型：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<span id="custTp" name="custTp"></span>
							</div>
						</td>
					</tr>
					<tr>
						<td>证件类型：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="idTp" name="idTp"></span>
							</span>
						</td>
						<td>证件号码：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<span id="idNo" name="idNo"></span>
							</div>
						</td>
					</tr>
					<tr>
						<td>通讯地址：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<span id="addr" name="addr"></span>
							</span>
						</td>
						<td>邮政编码：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<span id="postcode" name="postcode"></span>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			
			<table id="inputTable" class="table table-bordered">
				<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
				<tbody>
					<tr>
						<th rowspan="4">其他信息</th>
						<td>TA代码：</td>
						<td class="white-bg" colspan="3">
							<div class="col-sm-11 form-inner">
								<select name="tano" id="tano" class='form-control select2'>
									<option value="">--请选择--</option>
						     		<option value="17">17 招商基金注册登记系统</option>
						     		<option value="98">98 LOFTA</option>
								</select>
								<span id="tanospan"></span>
							</div>
						</td>
					</tr>
					<tr>
						<td>委托方式：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="trusttp" id="trusttp" class='form-control select2'>
									<c:forEach var="item" items="${trustTypeArray}">
										<option value="${item.PMCO}" ${item.PMCO == 3 ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
						<td>银行编号：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="bnkNo" id="bnkNo" class='form-control select2'>
									<c:forEach var="item" items="${bankArray}">
										<option value="${item.bnkNo}">${item.bnkNo}&nbsp;&nbsp;${ item.bnkNm }</option>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td>开户银行：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='openName' id='openName'/>
							</div>
						</td>
						<td>开户地：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="openAddr" id="openAddr" class='form-control select_addr1' onchange="getCitys(this.value)">
									<option value="">--</option>
									<c:forEach var="item" items="${provinces}">
										<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
								<select name="openbankcity" id="openbankcity" class='form-control select_addr2'></select>
								<input type="hidden" name="hiddencity" id="hiddencity" />
							</div>
						</td>
					</tr>
					<tr>
						<td>银行户名：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='bankAccoNm' id='bankAccoNm'/>
							</div>
						</td>
						<td>银行帐号：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='bankAcco' id='bankAcco'/>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		</form>
		<div class="page-footer" style="margin-top: 20px;text-align: center;">
			<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提交" name='btnSubmit'>提交</button>
		</div>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/tradeAccountMgr/addTradeAccount.js?v="<%=dateStr%>></script>
</body>	
</html>