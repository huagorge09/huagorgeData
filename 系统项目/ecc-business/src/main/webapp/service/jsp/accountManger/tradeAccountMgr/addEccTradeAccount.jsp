<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>增开ECC交易账号</title>
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
		            <span class="form-field">基金账号：</span>
		            <span class="form-input">
		            	<input type='text' class='form-control' name='inputQueryFundAcc' id='inputQueryFundAcc' />
		            </span>
		         </div>
		         <div class="form-item">
		            <span class="form-field">证件号码：</span>
		            <span class="form-input">
		            	<input type='text' class='form-control' name='inputQueryIdno' id='inputQueryIdno' />
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
	 	<form name="form1" id="form1" method="post" action="" >
		<div class="page-body">
			<table id="showTable" class="table table-bordered">
				<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
				<tbody>
					<tr>
						<th rowspan="3">TA选择</th>
						<td>基金账号：</td>
						<td class="white-bg" colspan="3">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='fundacc' id='fundacc' disabled="disabled"/>
							</div>
						</td>
					</tr>
					<tr>
						<td>委托方式：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="trusttp" id="trusttp" class='form-control select2_init'>
									<c:forEach var="item" items="${trustTypeArray}">
										<option value="${item.PMCO}" ${item.PMCO == 3 ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
						<td><font color="red">*</font>TA类型：</td>
						<td class="white-bg" colspan="3">
							<div class="col-sm-11 form-inner">
								<select name="tano" id="tano" class='form-control select2_init'>
									<option value="">--请选择--</option>
						     		<option value="17">17 招商基金注册登记系统</option>
						     		<option value="98">98 LOFTA</option>
								</select>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			
			<table id="tabPslBaseInfo" class="table table-bordered">
				<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
				<tbody>
					<tr>
						<th rowspan="2">证件信息</th>
						<td><font color="red">*</font>投资者名称：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvNm' id='pslInvNm' disabled="disabled"/>
							</div>
						</td>
						<td><font color="red">*</font>注册登记证件类型：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="pslInvIdtp" id="pslInvIdtp" class='form-control select2_init' disabled="disabled">
									<c:forEach var="item" items="${idtpArray}">
										<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td><font color="red">*</font>注册登记证件号码：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvIdno' id='pslInvIdno' disabled="disabled"/>
							</div>
						</td>
						<td><font color="red">*</font>注册登记证件有效期：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvIdValidate' id='pslInvIdValidate' disabled="disabled"/>
							</div>
						</td>
					</tr>
				</tbody>
				
				<tbody>
					<tr>
						<td rowspan="3">银行信息</td>
						<td><font color="red">*</font>开户银行：</td>
						<td class="white-bg" colspan="3">
							<div class="col-sm-11 form-inner">
								<select name="selbankno" id="selbankno" class='form-control select2_bank'>
									<c:forEach var="item" items="${bankArray}">
										<option value="${item.bnkNo}">${item.bnkNo}&nbsp;&nbsp;${ item.bnkNm }</option>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr>
					<tr>
						<td><font color="red">*</font>预留银行全称：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='openname' id='openname'/>
							</div>
						</td>
						<td><font color="red">*</font>预留银行开户地：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="openlocation" id="openlocation" class='form-control select_addr1' onchange="getCitys(this.value,'1')">
									<option value="">--</option>
									<c:forEach var="item" items="${provinces}">
										<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
								<select name="openbankcity" id="openbankcity" class='form-control select_addr2'></select>
								<input type="hidden" name="hopenbankcity" id="hopenbankcity" />
							</div>
						</td>
					</tr>
					<tr>
						<td><font color="red">*</font>预留银行户名：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='bankacconm' id='bankacconm'/>
							</div>
						</td>
						<td><font color="red">*</font>预留银行账号：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='bankacco' id='bankacco'/>
							</div>
						</td>
					</tr>
				</tbody>
				<tbody>
					<tr>
						<td rowspan="8">其他信息</td>
						<td>办公电话：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvOfficeTel' id='pslInvOfficeTel' disabled="disabled"/>
							</div>
						</td>
						<td>住宅电话：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvHomeTel' id='pslInvHomeTel' disabled="disabled"/>
							</div>
						</td>
					</tr>
					<tr>
						<td>移动电话：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvMobile' id='pslInvMobile' disabled="disabled"/>
							</div>
						</td>
						<td>传真号码：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvFax' id='pslInvFax' disabled="disabled"/>
							</div>
						</td>
					</tr>
					<tr>
						<td>传真委托：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="plsFaxDelegate" id="plsFaxDelegate" class='form-control select2_init'>
									<option value="1">是</option>
									<option value="0">否</option>
								</select>
							</div>
						</td>
						<td>电子邮件：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvEmail' id='pslInvEmail' disabled="disabled"/>
							</div>
						</td>
					</tr>
					<tr>
						<td><font color="red">*</font>通讯地址：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslAddr' id='pslAddr' disabled="disabled"/>
							</div>
						</td>
						<td><font color="red">*</font>邮政编码：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslPostCode' id='pslPostCode' disabled="disabled"/>
							</div>
						</td>
					</tr>
					<tr>
						<td>性别：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="plsSex" id="plsSex" class='form-control select2_init' disabled="disabled">
									<c:forEach var="item" items="${sexArray}">
										<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
						<td>国籍：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="plsInvNation" id="plsInvNation" class='form-control select2_init' disabled="disabled">
									<c:forEach var="item" items="${nationArray}">
										<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr>
				
					<tr>
						<td>学历：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="plsInvEducation" id="plsInvEducation" class='form-control select2_init' disabled="disabled">
									<c:forEach var="item" items="${edlevelArray}">
										<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
						<td>职业：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="plsInvJob" id="plsInvJob" class='form-control select2_init' disabled="disabled">
									<c:forEach var="item" items="${vacodeArray}">
										<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr>
					
					<tr>
						<td>年收入：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="plsInvIncome" id="plsInvIncome" class='form-control select2_init' disabled="disabled">
									<c:forEach var="item" items="${incomeArray}">
										<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
						<td>风险承受能力：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="plsInvRisk" id="plsInvRisk" class='form-control select2_init' disabled="disabled">
									<c:forEach var="item" items="${custrisklevelArray}">
										<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		</form>
		<div class="page-footer" style="margin-top: 20px;text-align: center;">
			<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提交" name='btnSubmit'>提交</button>
			<button id="btnReset" name="btnReset" type="reset" class="btn btn-link" value="重置">重置</button>
		</div>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/tradeAccountMgr/addEccTradeAccount.js?v="<%=dateStr%>></script>
</body>	
</html>