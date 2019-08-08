<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>开户</title>
<style type="text/css">
.table input[type='text'],select{
	max-width: 250px !important;
}

.select2{
	max-width: 250px !important;
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
.addResident {
	display: inline-block;
    border: 1px #fc6821 solid;
    width: 16px;
    background-color: #fc6821;
    color: #fff;
    height: 16px;
    text-align: center;
    line-height: 11px;
    border-radius: 20px;
    font-size: 20px;
    cursor: pointer;
    margin: 9px;
}
.modal-open .modal{
	top:150px !important;
	height: auto !important;
}
.ui-jqgrid-hdiv{
	overflow: hidden;
}
</style>
<script type="text/javascript">
var projectPath = '<%=context%>';
var widgetPath = '<%=context%>service/widget/';
var accountPath = '<%=context%>service/accountManager/';
//默认加载
$(function(){
	//把访问路径传到js
	setPath(projectPath,widgetPath,accountPath);
});
</script>
</head>
<body class="fixed-nav gray-bg">
	<div class="modal-content" style="border: 0px;box-shadow: 0 0px 0px;">
		<input id="permissionId" type="hidden" value="${ permissionId }"/>
		<input id="operatorId" 	 type="hidden" value="${ operatorId }"/>
		<input id="documentlist" value='' type="hidden" />
		<div class="page-header" style="margin: 0;">
			<h4 class="page-title">账户开户</h4>
			<select id="comNation" name="comNation" style="display: none;">
				<c:forEach var="item" items="${nationArray}">
					<c:if test="${item.PMCO != '156'}">
						<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
					</c:if>
				</c:forEach>
			</select>
		</div>
		<div class="clearfix">
		    <ul class="nav nav-tabs" role="tablist">
		   		<li role="presentation" class="active">
		        	<a href="#baseInfo_show_content" onClick="showTypeContent('baseInfoContent','btnBaseInfo')" id="baseInfo_show_content-tab btnBaseInfo" data-toggle="tab" 
		        		aria-controls="baseInfo_show_content">基本信息</a>
		        </li>
		        <li role="presentation">
		        	<a href="#categoryInfo_show_content" onclick="showTypeContent('categoryInfoContent','btnCategoryInfo')" id="categoryInfo_show_content-tab btnCategoryInfo" data-toggle="tab" 
		        		aria-controls="categoryInfo_show_content" aria-expanded="true" role="tab">分类信息</a>
		        </li>
		        <li role="presentation">
		        	<a href="#extInfo_show_content" onclick="showTypeContent('extInfoContent','btnExtInfo')" id="extInfo_show_content-tab btnExtInfo" data-toggle="tab" 
		        		aria-controls="extInfo_show_content" aria-expanded="true" role="tab" >附加信息</a>
		        </li>
		        <li role="presentation">
		        	<a href="#documentInfo_show_content" onclick="showTypeContent('documentInfoContent','btnDocumentInfo')" id="documentInfo_show_content-tab btnDocumentInfo" data-toggle="tab" 
		        		aria-controls="documentInfo_show_content" aria-expanded="true" role="tab" >资料信息</a>
		        </li>
		    </ul>
	 	</div><br>
	 	<form name="openCustomFrom" id="openCustomFrom" method="post" action="" >
		<div class="page-body">
			<!-- 基本信息 -->
			<div id="baseInfoContent" class="typeContent">
				<table class="table table-bordered">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody id="querytable">
						<tr>
							<th rowspan="99">查询条件</th>
							<td>开户类别：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="selOpenType" id="selOpenType" class='form-control select2_width'>
										<option value="1">个人开户</option>
										<option value="2" selected>机构开户</option>
										<option value="3">机构备案开户</option>
									</select>
								</div>
							</td>
							<td class="white-bg" colspan="2">
								<button id="btnQuery" type="button" class="btn btn-primary btn-save" value="查询" name='btnQuery'>查询</button>
							</td>
						</tr>
						<tr id="querytable1">
							<td>基金账号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='inputQueryFundAcc' id='inputQueryFundAcc' />
								</div>
							</td>
							<td>交易账号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner" style="line-height: 32px;">
									<input type='text' style="float: left;width: 54%;" class='form-control' name='inputQueryTradeAcc' id='inputQueryTradeAcc' />
									<a href="#" style="color: blue;margin-left: 10px;text-decoration: underline;" onclick="querybalance('inputQueryFundAcc')">基金余额查询</a>
								</div>
							</td>
						</tr>
						<tr id="querytable2" style="display: none;">
							<td>基金账号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='inputQueryFundAcc1' id='inputQueryFundAcc1' />
								</div>
							</td>
							<td>交易账号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner" style="line-height: 32px;">
									<input type='text' style="float: left;width: 54%;" class='form-control' name='inputQueryTradeAcc1' id='inputQueryTradeAcc1' />
									<a href="#" style="color: blue;margin-left: 10px;text-decoration: underline;" onclick="querybalance('inputQueryFundAcc1')">基金余额查询</a>
								</div>
							</td>
						</tr>
					
						<tr id="querytable3" style="display: none;">
							<td>托管人：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="seltgr" id="seltgr" class='form-control select2_width'></select>
								</div>
							</td>
							<td>投资管理人：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="seltzglr" id="seltzglr" class='form-control select2_width'></select>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<table class="table table-bordered">
				<colgroup>
				  	<col width="10%">
				  	<col width="20%">
				  	<col width="25%">
				  	<col width="20%">
				  	<col width="25%">
				</colgroup>
				<tbody id=tabtainfo>
					<tr>
						<th rowspan="2">TA选择</th>
						<td>开户类别：</td>
						<td class="white-bg">
							<span class="col-sm-12">
								<label><input type="radio" onclick="changeFundaccstatus(true)" name="opentype" id ="opentype" value="001" checked>新开户</input></label>
								<label><input type="radio" onclick="changeFundaccstatus(false)" name="opentype" id ="opentype" value="008">已有基金账号开户</input></label>
							</span>
						</td>
						<td>基金账号：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='fundacc' id='fundacc' disabled/>
							</div>
						</td>
					</tr>
					
					<tr>
						<td>委托方式：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="trusttp" id="trusttp" class='form-control select2_width'>
									<c:forEach var="item" items="${trustTypeArray}">
										<option value="${item.PMCO}" ${item.PMCO == '3' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
									</c:forEach>
								</select>
							</div>
						</td>
						<td><font color="red">*</font>TA类型：</td>
						<td class="white-bg">
							<div class="col-sm-10 form-inner">
								<label><input class="i-checks" type="checkbox" id="17" name="talist" >17 招商基金注册登记系统</label><br>
								<label><input class="i-checks" type="checkbox" id="98" name="talist" >98 LOFTA</label><br>
							</div>
						</td>
					</tr>
				</tbody>
				
				<tbody id="mandatorTable">
					<!-- 一对一 投资者信息带入 start -->
					<tr id="mandatorTable">
						<th rowspan="1">证件信息</th>
						<td>一对一投资者名称：</td>
						<td class="white-bg" colspan="3">
							<div class="col-sm-11 form-inner">
								<!-- <input type='text' class='form-control' name='qOffLineInvestName' id='qOffLineInvestName' onblur="qInvNameBlurFunc(this);" style="float: left;position: sticky;z-index: 1"/>
								<button id="btnOffLineInvestName" type="button" onclick="queryWaspOffInvestInfo()" class="btn btn-primary btn-query" value="查询" name='btnOffLineInvestName'>查询</button>
								<br> -->
								<select name="qOffLineInvestName" id="qOffLineInvestName" onchange="offLineInvNameClick(this);" class='form-control'>
									<option value="">--</option>
								</select>
								<input name="offInvSerialno" id="offInvSerialno" type="hidden" />
							</div>
						</td>
					</tr>
				</tbody>
					<!-- 一对一 投资者信息带入 End -->
			</table>
			<div style="height: 20px">
				<font id="ANTIMONEYLAUN_BLACKLIST" color="green">
				</font>
			</div>
			<!-- 个人开户 -->
			<table class="table table-bordered" style="margin-bottom: 0px;">
				<colgroup>
					<col width="10%">
				  	<col width="20%">
				  	<col width="25%">
				  	<col width="20%">
				  	<col width="25%">
				</colgroup>
				<tbody id="tabPslBaseInfo">
					<tr>
						<th rowspan="2">证件信息</th>
						<td><font color="red">*</font>投资者名称：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' style="float: left;" class='form-control' name='pslInvNm' id='pslInvNm'/>
							</div>
						</td>
						<td><font color="red">*</font>注册登记证件类型：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="pslInvIdtp" id="pslInvIdtp" class='form-control select2_width' onchange="antiMoneyLaunValid()">
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
								<input type='text' maxlength="30" class='form-control' name='pslInvIdno' id='pslInvIdno' onblur="antiMoneyLaunValid()"/>
							</div>
						</td>
						<td><font color="red">*</font>注册登记证件有效期：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' style="float: left;" class='form-control' name='pslInvIdValidate' id='pslInvIdValidate' />
								<label style="line-height: 34px;margin: 0;">
									<input class="i-checks" id="changeorgInvIdvalidate" type="checkbox"
									   onclick="changetime('changeorgInvIdvalidate','pslInvIdValidate');">长期
								</label><br>
							</div>
						</td>
					</tr>
					
					<tr>
						<td rowspan="3">银行信息</td>
						<td><font color="red">*</font>开户银行：</td>
						<td class="white-bg" colspan="3">
							<div class="col-sm-11 form-inner">
								<select name="selbankno" id="selbankno" class='form-control select2_width'>
									<c:forEach var="item" items="${bankBaseList}">
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
								<select name="openbankcity" id="openbankcity" class='form-control select_addr2'>
									<option value="">--</option>
								</select>
								<input type="hidden" name="hopenbankcity" id="hopenbankcity" />
							</div>
						</td>
					</tr>
					
					<tr>
						<td><font color="red">*</font>预留银行户名：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='bankacconm' id='bankacconm' />
							</div>
						</td>
						<td><font color="red">*</font>预留银行账号：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='bankacco' id='bankacco' />
							</div>
						</td>
					</tr>
					
					<tr>
						<td rowspan="6">其他信息</td>
						<td>办公电话：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvOfficeTel' id='pslInvOfficeTel' />
							</div>
						</td>
						<td>住宅电话：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvHomeTel' id='pslInvHomeTel' />
							</div>
						</td>
					</tr>
					
					<tr>
						<td>移动电话：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvMobile' id='pslInvMobile' />
							</div>
						</td>
						<td>传真号码：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvFax' id='pslInvFax' />
							</div>
						</td>
					</tr>
					
					<tr>
						<td>传真委托：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<select name="plsFaxDelegate" id="plsFaxDelegate" class='form-control select2_width'>
									<option value="1">是</option>
									<option value="0">否</option>
								</select>
							</div>
						</td>
						<td>电子邮件：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslInvEmail' id='pslInvEmail' />
							</div>
						</td>
					</tr>
					
					<tr>
						<td><font color="red">*</font>通讯地址：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslAddr' id='pslAddr' />
							</div>
						</td>
						<td><font color="red">*</font>邮政编码：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslPostCode' id='pslPostCode' maxlength="6"/>
							</div>
						</td>
					</tr>
					
					<tr>
						<td>上交所股东代码：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslShsecacc' id='pslShsecacc' maxlength="10"/>
							</div>
						</td>
						<td>深交所股东代码：</td>
						<td class="white-bg">
							<div class="col-sm-11 form-inner">
								<input type='text' class='form-control' name='pslSzsecacc' id='pslSzsecacc' maxlength="10"/>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			
			
			<!-- 机构/机构备案开户 -->
			<table class="table table-bordered" id="tabOrgBaseInfo">
					<colgroup>
						<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody id="idinfo">
						<tr>
							<td rowspan="3">证件信息</td>
							<td><font color="red">*</font>投资者名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgInvNm' id='orgInvNm' onchange = "queryOpenUserInfo();" style="float: left;"/>
								</div>
							</td>
							<td class="white-bg" colspan="2">
								<button id="btnShow" type="button" onclick="queryWaspUserBankInfo();" class="btn btn-primary btn-query" value="查询" name='btnShow'>查询</button>
							</td>
							</td></td>
						</tr>
						<tr>
							<td><font color="red">*</font>注册登记证件类型：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner" style="line-height: 32px;">
									<select name="orgInvIdtp" id="orgInvIdtp" class='form-control select2_width' onchange="antiMoneyLaunValid()">
										<c:forEach var="item" items="${seatidtpArray}">
											<option value="${item.PMCO}" ${item.PMCO == '11' ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								
								<!-- 隐藏，用于添加其他证件信息时使用 -->
								<div class="orgInvIdtpDiv" style="display: none;">
									<div class="col-sm-11 form-inner" style="line-height: 32px;">
										<select name="orgInvIdtp" id="ooidtp" class='form-control orgInvIdtp'>
											<c:forEach var="item" items="${seatidtpArray}">
												<option value="${item.PMCO}" ${item.PMCO == '11' ?"selected": "" } >${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>注册登记证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='orgInvIdno' id='orgInvIdno' onblur="antiMoneyLaunValid()" />
								</div>
							</td>
							<td><font color="red">*</font>注册登记证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgInvIdvalidate' id='orgInvIdvalidate' />
									<label style="line-height: 34px;margin: 0;">
									<input class="i-checks" id="changepslInvIdValidate" type="checkbox" onclick="changetime('changepslInvIdValidate','orgInvIdvalidate');">长期</label><br>
								</div>
							</td>
						</tr>
						
						<tr>
							<td rowspan="3">银行信息</td>
							<td><font color="red">*</font>开户银行：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner" style="line-height: 32px;">
									<select name="orgselbankno" id="orgselbankno" class='form-control select2_width'>
										<c:forEach var="item" items="${bankBaseList}">
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
									<input type='text' class='form-control' name='orgopenname' id='orgopenname'/>
								</div>
							</td>
							<td><font color="red">*</font>预留银行开户地：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgopenlocation" id="orgopenlocation" class='form-control select_addr1' onchange="getCitys(this.value,'0')">
										<option value="">--</option>
										<c:forEach var="item" items="${provinces}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="orgopenbankcity" id="orgopenbankcity" class='form-control select_addr2'>
										<option value="">--</option>
									</select>
									<input type="hidden" name="horgopenbankcity" id="horgopenbankcity" />
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>预留银行户名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgbankacconm' id='orgbankacconm' />
								</div>
							</td>
							<td><font color="red">*</font>预留银行账号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgbankacco' id='orgbankacco' />
								</div>
							</td>
						</tr>
						</tbody>
						<tbody >
						<tr>
							<td rowspan="3">法人信息</td>
							<td><font color="red">*</font>法定代表人姓名：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgInstrepnm' id='orgInstrepnm' onKeyUp="changeInstrep(this,'orgPrincipalname',false)"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>法定代表人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgInstrepnation" id="orgInstrepnation" class='form-control select2_width' onchange="changeInstrep(this,'orgPrincipalnation',true)">
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td><font color="red">*</font>法定代表人证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgInstrepidtp" id="orgInstrepidtp" class='form-control select2_width' onchange="changeInstrep(this,'orgPrincipalidtp',true)">
										<c:forEach var="item" items="${idtpArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>法定代表人证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="18" class='form-control' name='orgInstrepidno' id='orgInstrepidno'
										onKeyUp="changeInstrep(this,'orgPrincipalidno',false);"/>
								</div>
							</td>
							<td><font color="red">*</font>法定代表人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgInstrepidvalidate' id='orgInstrepidvalidate' onchange="changeInstrep(this,'orgPrincipalidvalidate',false)"/>
									<label style="line-height: 34px;margin: 0;">
										<input class="i-checks" id="changeorgInstrepidvalidate" type="checkbox"  
											   onclick="changetime('changeorgInstrepidvalidate','orgInstrepidvalidate');">长期
									</label><br>
								</div>
							</td>
						</tr>
						
						<tr>
							<td rowspan="3">机构负责人信息</td>
							<td><font color="red">*</font>机构负责人姓名：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgPrincipalname' id='orgPrincipalname'/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>机构负责人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgPrincipalnation" id="orgPrincipalnation" class='form-control select2_width'>
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == '156' ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td><font color="red">*</font>机构负责人证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgPrincipalidtp" id="orgPrincipalidtp" class='form-control select2_width'>
										<c:forEach var="item" items="${idtpArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>机构负责人证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="18" class='form-control' name='orgPrincipalidno' id='orgPrincipalidno'/>
								</div>
							</td>
							<td><font color="red">*</font>机构负责人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgPrincipalidvalidate' id='orgPrincipalidvalidate' />
									<label style="line-height: 34px;margin: 0;">
										<input class="i-checks" id="changeorgPrincipalidvalidate" type="checkbox" 
											   onclick="changetime('changeorgPrincipalidvalidate','orgPrincipalidvalidate');"/>长期
									</label><br>
								</div>
							</td>
						</tr>
					</tbody>
					
					<tbody id="continfo">						
						<tr>
							<th rowspan="6">经办人员信息</th>
							<td><font color="red">*</font>经办人授权范围：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgContactright" id="orgContactright" class='form-control select2_width'>
										<c:forEach var="item" items="${contprivArray}">
											<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								<div style="display: none;" class="orgContactrightDiv">
									<div class="col-sm-11 form-inner">
										<select name="orgContactright" id="oocontactgrant" class='form-control orgContactright'>
											<c:forEach var="item" items="${contprivArray}">
												<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</td>
							<td><font color="red">*</font>经办人姓名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="18" class='form-control' name='orgContnm' id='orgContnm'/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>经办人国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgContnation" id="orgContnation" class='form-control select2_width'>
										<c:forEach var="item" items="${nationArray}">
											<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								<div class="orgContnationDiv" style="display: none;">
									<div class="col-sm-11 form-inner">
										<select name="orgContnation" class="orgContnation" class='form-control'>
											<c:forEach var="item" items="${nationArray}">
												<option value="${item.PMCO}" ${item.PMCO == '156' ? "selected = selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</td>
							<td><font color="red">*</font>经办人证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgContidtp" id="orgContidtp" class='form-control select2_width'>
										<c:forEach var="item" items="${idtpArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
								<div class="orgContidtpDiv" style="display: none;">
									<div class="col-sm-11 form-inner">
										<select name="orgContidtp" class="orgContidtp" class='form-control'>
											<c:forEach var="item" items="${idtpArray}">
												<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
											</c:forEach>
										</select>
									</div>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>经办人证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="18" class='form-control' name='orgContidno' id='orgContidno' />
								</div>
							</td>
							<td><font color="red">*</font>经办人证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgContidvalidate' id='orgContidvalidate' />
									<label style="line-height: 34px;margin: 0;">
										<input class="i-checks" id="changeorgContidvalidate" type="checkbox" 
											   onclick="changetime('changeorgContidvalidate','orgContidvalidate');"/>长期
									</label><br>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>经办人办公电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContphone' id='orgContphone' />
								</div>
							</td>
							<td><font color="red">*</font>经办人传真号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContfax' id='orgContfax' />
								</div>
							</td>
						</tr>
						
						<tr>
							<td>经办人手机号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContmobile' id='orgContmobile' />
								</div>
							</td>
							<td>经办人电子邮件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContemail' id='orgContemail' />
								</div>
							</td>
						</tr>
						
						<tr>
							<td>经办人通讯地址：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContAddr' id='orgContAddr' />
								</div>
							</td>
							<td>经办人邮政编码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgContPostcode' id='orgContPostcode' maxlength="6"/>
								</div>
							</td>
						</tr>
					</tbody>
					
					<tbody>
						<tr>
							<td rowspan="4">其他信息</td>
							<td>办公电话：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgInvOfficeTel' id='orgInvOfficeTel' />
								</div>
							</td>
							<td>传真号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgInvFax' id='orgInvFax' />
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>办公地址：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgAddr' id='orgAddr' />
								</div>
							</td>
							<td><font color="red">*</font>邮政编码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgPostcode' id='orgPostcode' maxlength="6"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>上交所股东代码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgShsecacc' id='orgShsecacc' maxlength="10"/>
								</div>
							</td>
							<td>深交所股东代码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgSzsecacc' id='orgSzsecacc' maxlength="10"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>机构类型：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="organType" id="organType" class='form-control select2_width'>
										<c:forEach var="item" items="${organType}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
			<!-- 分类信息 -->
			<div id="categoryInfoContent" class="typeContent" style="display: none;">
				<table class="table table-bordered maintable" id="categoryInfo">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody id="investClassInfo">
						<tr>
							<td rowspan="8" id="classInfoTh">分类信息</td>
							<td>客户简称：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="custsimpnm" id="custsimpnm" class='form-control select2-q' onchange="getSecond(this.value);">
										<option value="">--</option>
										<c:forEach var="item" items="${custFirst}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="instrepcode" id="instrepcode" class='form-control select2-q'>
										<option value="">--</option>
									</select>
									<input type="hidden" name="hinstrepcode" id="hinstrepcode" />
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>投资者类型：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="invprtp" id="invprtp" onchange="interactInvProInfo()" class='form-control select2_width'>
										<option value="0">专业投资者</option>
										<option value="1" selected>普通投资者</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>业务类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="businesstp" id="businesstp" class='form-control select2_width'>
										<c:forEach var="item" items="${businesstpArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>公司类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="corptype" id="corptype" class='form-control select2_width'>
										<c:forEach var="item" items="${cmptpArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>地域类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="regioncode" id="regioncode" class='form-control select2_width'>
										<c:forEach var="item" items="${regiontpArray}">
											<option value="${item.PMCO}" ${item.PMCO == '0755' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>反洗钱类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="fxqtype" id="fxqtype" class='form-control select2_width'>
										<c:forEach var="item" items="${fxqArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>反洗钱备注：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='fxqdesc' id='fxqdesc'/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>税收居民身份：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="taxType" id="taxType" onchange="taxTypeChange(this);" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${taxTypeArray}">
											<option value="${item.PMCO}" ${item.PMCO == '1' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<!-- <tr class="otherTaxTypeInfoBg">
							<td>税收居民身份声明：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="taxTypeDecl" id="taxTypeDecl" class='form-control select2_width'>
										<option value="">--</option>
										<option value="1">是</option>
										<option value="0">否</option>
									</select>
								</div>
							</td>
						</tr> -->
						
						<tr class="instNotResidentBg">
							<td>消极非金融机构：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="negativeNotFinaInst" id="negativeNotFinaInst" onchange="negativeNotFinaChange(this);" class='form-control select2_width'>
										<option value="">--</option>
										<option value="1">居民消极非金融机构</option>
										<option value="2">非居民消极非金融机构</option>
										<option value="3">其它机构</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr class="negativeNotFinaInstBg">
							<td>存在非居民控制人标识：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="controlPerTaxDecl" id="controlPerTaxDecl" onchange="controllerPerTaxDecl(this);" class='form-control select2_width'>
										<option value="">--</option>
										<option value="1">是</option>
										<option value="0" selected="selected">否</option>
									</select>
								</div>
							</td>
						</tr>
					</tbody>
					
					<tbody class="instInvestPro" id="instInvestPro">
						<tr>
							<td id="instInvestProTh" rowspan="10">专业投资者信息</td>
							<td>机构类型：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="investProInstType" id="investProInstType" class='form-control select_addr1' onchange="getInvestProInstSecond(this);">
										<c:forEach var="item" items="${investProInstArray}">
											<option value="${item.PMCO}" ${item.PMCO == '1' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="investProInstSecond" id="investProInstSecond" class='form-control select_addr2'>
										<option value="">--</option>
									</select>
									<input type="hidden" name="hiInvestProInstSecond" id="hiInvestProInstSecond" />
								</div>
							</td>
						</tr>
						
						<tr class="instInvestProInfoBg">
							<td>近1年末净资产：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="oneYearEndNetAsset" id="oneYearEndNetAsset" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${oneYearEndNetAssetArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr class="instInvestProInfoBg">
							<td>近1年末金融资产：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="oneYearEndFinAsset" id="oneYearEndFinAsset" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${oneYearEndFinAssetArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr class="instInvestProInfoBg">
							<td>投资经历：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="investExperience" id="investExperience" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${investExperienceArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
					</tbody>
					
					<!-- 个人专业投资者 -->
					<tbody class="personInstInvestPro" id="personInstInvestPro">
						<tr>
							<td rowspan="10">专业投资者信息</td>
							<td>近三年年均收入：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="threeAnnualIncome" id="threeAnnualIncome" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${threeAnnualIncomeArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							
							<td>金融资产：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="financialAsset" id="financialAsset" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${financialAssetArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>投资经历：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="indInvExperience" id="indInvExperience" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${indInvExperienceArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							
							<td>相关工作经历：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="relatedWorkExp" id="relatedWorkExp" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${relatedWorkExpArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>金融职业：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<select name="finProfessions" id="finProfessions" class='form-control select2_width'>
										<option value="">--</option>
										<c:forEach var="item" items="${finProfessionsArray}">
											<option value="${item.PMCO}" ${item.PMCO == 3 ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				
				<table class="table table-bordered maintable" id="residentType">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody id="investClassInfo">
						<tr>
							<td rowspan="100" id="residentTitle">税收居民信息</td>
							<td>中文姓名：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' disabled="disabled" name='userCustName' id='userCustName' />
								</div>
							</td>
						</tr>
						<tr class="englishNameDiv"></tr>
						<tr class="sexAndbirth">
							<td><font color="red">*</font>性别：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="sex" id="sex" class='form-control select2_width'>
										<option value="">请选择</option>
										<option value="1">男</option>
										<option value="0">女</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr class="sexAndbirth">
							<td><font color="red">*</font>出生日期</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="birthDate" class="form-control" id="birthDate">
								</div>
							</td>
						</tr>
						<tr class="birthAddress">
							<td><font color="red">*</font>出生地：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner" style="float: left;">
									<select name="birth_nation" id="birth_nation" class='form-control select_addr1' onchange="changeNation(this)">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="birth_region" id="birth_region" class='form-control select_addr2' style="float: left;" >
										<option value="">--</option>
									</select>
								</div>
								<div class="col-sm-11 form-inner" style="float: left;margin-top: -34px;margin-left: 265px;">
									<input type="text" name="birth_address" id="birth_address" class="form-control">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font><span class="resideNation">现居国家</span>：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="reside_nation" class='form-control select_addr1' onchange="changeNation(this)";>
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select class='form-control select_addr2' name="reside_region">
										<option value="">--</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font><span class="resideAddress">现居地址</span>：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="reside_address" class='form-control' id="reside_address" >
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>Present Address：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="reside_address_english" class='form-control' id="reside_address_english">
								</div>
							</td>
						</tr>
						<tr class="taxNationality">
							<td><font color="red">*</font>税收居民国(地区)</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="taxNationality" class='form-control select_addr1' onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="taxArea" class='form-control select_addr2'>
										<option value="">--</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>纳税人识别号：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="taxpayerCode" class='form-control' style="float: left;">
									<label style="line-height: 34px;margin: 0;">
										<input class="i-checks isTaxpayerEvent" type="checkbox" onclick="isTaxpayer(this)">无<i class="residentEach"></i>
									</label>
								</div>
							</td>
						</tr>
						<tr style="display: none;">
							<td><font color="red">*</font>无识别号的原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="notCodeCause" class='form-control select2_width' onchange="notCodeCauseEvent(this)">
										<option value="">--</option>
										<option value="1">居民国（地区）不发放纳税人识别号</option>
										<option value="2">账号持有人未能取得纳税人识别号</option>
									</select>
								</div>
							</td>
						</tr>
						<tr style="display: none;" class="causeText">
							<td><font color="red">*</font>未取得原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="notGetCause" class="form-control">
								</div>
							</td>
						</tr>
						<tr class="since" style="display: none;">
							<td><font color="red">*</font>税收居民国(地区)</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select class='form-control select_addr1' name="taxNationality" onchange="changeNation(this)">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="taxArea" class='form-control select_addr2'>
										<option value="">--</option>
									</select>
									<span class="addResident" onclick="addResident();">+</span>
								</div>
							</td>
						</tr>
						<tr class="since" style="display: none;">
							<td><font color="red">*</font>纳税人识别号：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="taxpayerCode" class="form-control" style="float: left;">
									<label style="line-height: 34px;margin: 0;">
										<input name="isTaxpayerEvent" class="i-checks isTaxpayerEvent" type="checkbox" onclick="isTaxpayer(this)">无<i class="residentEach"></i>
									</label>
								</div>
							</td>
						</tr>
						<tr class="since sinceNotCodeCause" style="display: none;">
							<td><font color="red">*</font>无识别号的原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="notCodeCause" class='form-control select2_width' onchange="notCodeCauseEvent(this);">
										<option value="">--</option>
										<option value="1">居民国（地区）不发放纳税人识别号</option>
										<option value="2">账号持有人未能取得纳税人识别号</option>
									</select>
								</div>
							</td>
						</tr>
						<tr class="since sinceNotCodeCause" style="display: none;">
							<td><font color="red">*</font>未取得原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="notGetCause" class="form-control">
								</div>
							</td>
						</tr>
					</tbody>
					<input type="hidden" name="taxresident" />
				</table>
				<!-----------------------------非居民信息与非居民信息E-------------------------->
				
				<!-----------------------------非居民控制人S-------------------------->
				
				<table class="table table-bordered maintable" id="controllerInfo">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody id="controllerClassInfo">
						<tr>
							<th rowspan="1000" id="residentTitle">非居民控制人信息</th>
							<td><font color="red">*</font>控制人中文姓名：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="ChineseName2" id="ChineseName2">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人英文姓：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="EnglishFamliyName3" id="EnglishFamliyName3">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人英文名：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="EnglishFirstName3" id="EnglishFirstName3">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人类型</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="ControllerType" class='form-control select2_width'>
										<option value="">--</option>
										<option value="001">法人控制人-所有权</option>
										<option value="002">法人控制人-其他</option>
										<option value="003">法人控制人-高管人员</option>
										<option value="004">信托-委托人</option>
										<option value="005">信托-受托人</option>
										<option value="006">信托-监察人</option>
										<option value="007">信托-受益人</option>
										<option value="008">信托-其他控制人</option>
										<option value="009">其他-等同于委托人</option>
										<option value="010">其他-等同于受托人</option>
										<option value="011">其他-等同于监察人</option>
										<option value="012">其他-等同于受益人</option>
										<option value="013">其他-等同于其他控制人</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人非居民标识：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="ConNonResiFlag" readonly="readonly" id="ConNonResiFlag">
								</div>
							</td>
						</tr>
						<tr>
							<td>控制人持股比例：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="ConShareRatio" id="ConShareRatio">
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人现居国家：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="LivingCountry2" class='form-control select_addr1' onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="LivingCountry21" class='form-control select_addr2'><option value="">--</option></select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人现居地址：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="LivingAddress5" id="LivingAddress5">
								</div>		
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人现居地址英文：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="LivingAddress7" id="LivingAddress7">
								</div>		
							</td>
						</tr>
						<tr>
							<td>控制人国籍：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="RegRegionCode2" class='form-control select_addr1' onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="RegRegionCode21" class='form-control select_addr2'>
										<option value="">--</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人出生日期：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="BirthDate2" id="BirthDate2"/>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人出生国家：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="BirthCountry2" class='form-control select_addr1' onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="BirthCountry21" class='form-control select_addr2'>
										<option value="">--</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人出生城市英文：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="BirthCity2" id="BirthCity2">
								</div>		
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>控制人税收居民国</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="TaxCountry2" class='form-control select_addr1' onchange="changeNation(this);">
										<option value="">--</option>
										<option value="1">中国</option>
										<option value="2">中国香港</option>
										<option value="3">中国澳门</option>
										<option value="4">中国台湾</option>
										<option value="5">海外</option>
									</select>
									<select name="TaxCountry21" class='form-control select_addr2'>
										<option value="">--</option>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>纳税人识别号：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" name="TaxID2" class="form-control" style="float: left;">
									<label style="line-height: 34px;margin: 0;">
										<input name="isTaxpayerEvent2" class="i-checks isTaxpayerEvent" type="checkbox" onclick="isTaxpayer(this)">无
									</label>
								</div>
							</td>
						</tr>
						<tr style="display: none;">
							<td><font color="red">*</font>无识别号的原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="notCodeCause2" class='form-control select2_width' onchange="notCodeCauseEvent(this);">
										<option value="">--</option>
										<option value="1">居民国（地区）不发放纳税人识别号</option>
										<option value="2">账号持有人未能取得纳税人识别号</option>
									</select>
								</div>
							</td>
						</tr>
						<tr style="display: none;">
							<td><font color="red">*</font>未取得原因：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<input type="text" class='form-control' name="Specification2">
								</div>	
							</td>
						</tr>
					</tbody>
				</table>
				<!-----------------------------非居民控制人E-------------------------->
				
			</div>
			<!-- 附加信息 -->
			<div id="extInfoContent" class="typeContent" style="display: none;">
				
				<table class="table table-bordered maintable" id="tabPslExtInfo">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody>
						<tr>
							<td rowspan="3" id="extInfoOtherTh">其他信息</td>
							<td>性别：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="plsSex" id="plsSex" class='form-control select2_width'>
										<c:forEach var="item" items="${sexArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>国籍：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="plsInvNation" id="plsInvNation" class='form-control select2_width'>
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
									<select name="plsInvEducation" id="plsInvEducation" class='form-control select2_width'>
										<c:forEach var="item" items="${edlevelArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>职业：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="plsInvJob" id="plsInvJob" class='form-control select2_width'>
										<c:forEach var="item" items="${vacodeArray}">
											<option value="${item.PMCO}" ${item.PMCO == '15' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>年收入：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="plsInvIncome" id="plsInvIncome" class='form-control select2_width'>
										<c:forEach var="item" items="${incomeArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>风险承受能力：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="plsInvRisk" id="plsInvRisk" class='form-control select2_width' style="float: left;">
										<c:forEach var="item" items="${custrisklevelArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<label style="margin: 0;">
										<input id="indSpecRiskLevel" class="i-checks" type="checkbox" name="indSpecRiskLevel" onclick="changeSpecRiskLevel(this,'plsInvRisk');" >最低
									</label>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			
				<table class="table table-bordered maintable" id="tabOrgExtInfo" style="display: none;">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					<tbody id="oidinfo">
						<tr>
							<th rowspan="1">其他证件信息</th>
							<td class="white-bg" colspan="4">
								<button id="btnAddId" type="button" class="btn btn-primary btn-add" value="增加">增加</button>
							</td>
						</tr>
					</tbody>
					
					<tbody id="ocontInfo">
						<tr>
							<th rowspan="1">其他经办人信息</th>
							<td class="white-bg" colspan="4">
								<button id="btnAddCont" type="button" class="btn btn-primary btn-add" value="增加">增加</button>
							</td>
						</tr>
					</tbody>
					
					<tbody>
						<tr>
							<th rowspan="5">其他信息</th>
							<td>实际控制人或控股股东名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgHoldingname' id='orgHoldingname' />
								</div>
							</td>
							<td>控股股东证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="orgHoldingidtp" id="orgHoldingidtp" class='form-control select2_width'>
										<c:forEach var="item" items="${seatidtpArray}">
											<option value="${item.PMCO}" ${item.PMCO == '11' ?"selected": "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>控股股东证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='orgHoldingidno' id='orgHoldingidno' />
								</div>
							</td>
							<td>控股股东证件有效期：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' style="float: left;" class='form-control' name='orgHoldingidvalidate' id='orgHoldingidvalidate' />
									<label style="line-height: 34px;margin: 0">
										<input class="i-checks" id="changeorgHoldingidvalidate" type="checkbox" 
											   onclick="changetime('changeorgHoldingidvalidate','orgHoldingidvalidate');"/>长期
									</label><br>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>基金投资受益人名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='orgBeneficiarynm' id='orgBeneficiarynm' />
								</div>
							</td>
							<td>客户风险承受能力：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner" style="width: 100%;">
									<select name="orgRiskLevel" id="orgRiskLevel" class='form-control select2_width' style="float: left;">
										<c:forEach var="item" items="${custrisklevelArray}">
											<option value="${item.PMCO}">${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<label style="margin: 0;">
										<input id="instSpecRiskLevel" class="i-checks" type="checkbox" name="instSpecRiskLevel" 
											   onclick="changeSpecRiskLevel(this,'orgRiskLevel');"
										/>最低
									</label>
								</div>
							</td>
						</tr>
						
					</tbody>
				</table>
			</div>
			<!-- 资料信息 -->
			<div id="documentInfoContent" class="typeContent" style="display: none;">
				<table class="table table-bordered maintable" id="documentInfo">
					<colgroup>
					  	<col width="20%">
					  	<col width="30%">
					  	<col width="20%">
					  	<col width="30%">
					</colgroup>
					
					<tbody>
						<tr>
							<td>业务类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="docbusinesstp" id="docbusinesstp" class='form-control select2_width'>
										<c:forEach var="item" items="${docbusitpArray}">
											<option value="${item.PMCO}" ${item.PMCO == 'AP_01' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>客户资料是否齐全：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isalldoc" id="isalldoc" class='form-control select2_width'>
										<option value="1">是</option>
										<option value="0" selected>否</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>客户资料是否原件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isoriginal" id="isoriginal" class='form-control select2_width'>
										<option value="1">是</option>
										<option value="0" selected>否</option>
									</select>
								</div>
							</td>
							<td>客户资料是否归档：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="issaved" id="issaved" class='form-control select2_width'>
										<option value="1">是</option>
										<option value="0" selected>否</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>是否上传附件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isupload" id="isupload" class='form-control select2_width'>
										<option value="1">是</option>
										<option value="0" selected>否</option>
									</select>
								</div>
							</td>
							<td>文件编号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='fileno' id='fileno' disabled="disabled"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>是否扫描：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isscan" id="isscan" class='form-control select2_width'>
										<option value="Y">是</option>
										<option value="N" selected>否</option>
									</select>
								</div>
							</td>
							<td>存档位置：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='keepaddress' id='keepaddress' disabled="disabled"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>所属客户经理：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='salesaccmanager' id='salesaccmanager'/>
								</div>
							</td>
						</tr>
						
						<tr id="documentinfo">
							<td id="contractListTh">资料列表：</td>
						</tr>
						<tr>
							<td>资料信息备注：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<textarea rows="3" cols="50" class='form-control'  name="remarkinfo" id="remarkinfo"></textarea>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<input type=hidden id="tano" name="tano" />
			</div>
		</div>
		<div class="page-footer" style="margin-top: 20px;text-align: center;">
			<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提交" name='btnSubmit'>提交</button>
			<button id="claseBtn" name="claseBtn" type="button" class="btn btn-link" data-dismiss="modal" value="重置">重置</button>
			<button id="reset" name="reset" type="reset" class="btn btn-link" style="display: none;" value="重置">重置</button>
		</div>
		
		<!-- 点击查询按钮查询综合业务平台银信息 -->
		<div class="modal fade" id="choose-box-wrapper" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">  
		    <div class="modal-dialog" role="document">  
		        <div class="modal-content">  
		            <div class="modal-header">  
		                <button type="button" class="close" data-dismiss="modal" aria-label="Close">  
		                    <span aria-hidden="true">&times;</span>  
		                </button>  
		                <h4 class="modal-title" id="myModalLabel">预留银行信息</h4>  
		            </div>  
		            <div class="modal-body">
		            	<div class="jqGrid_wrapper fullscreen-wrapper" style="width: 520px;">
						    <table id="cbpBankInfoList"></table>
						    <div id="cbpBankInfoPage"></div>
						</div>
		                <!-- <div id="choose-a-main" style="height: 300px;overflow-y:auto;"></div> -->
		            </div></br>
		            <div class="modal-footer" style="text-align: center;">  
		                <button type="button" class="btn btn-primary" onclick="checkradio();">确 认</button>
		                <button type="button" class="btn btn-default" id="closeModel" data-dismiss="modal">取 消</button> 
		            </div>  
		        </div>  
		    </div>  
		</div> 	
		
		
		</form>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/custAccountMgr/openAccount.js?v="<%=dateStr%>></script>
</body>	
</html>