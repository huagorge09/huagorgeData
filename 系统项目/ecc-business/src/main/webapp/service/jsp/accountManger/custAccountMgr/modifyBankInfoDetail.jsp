<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>银行资料修改</title>
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
	max-width: 115px !important;
	float: left;
	margin-right: 12px;
}
.select_addr2{
	max-width: 115px !important;
	float: left;
}
.table > tbody + tbody{
	border-top: 0 !important;
}
.table{
	margin-bottom: 0 !important;
}
</style>
<script type="text/javascript">
var projectPath = '<%=context%>';
var widgetPath = '<%=context%>service/widget/';
var accountPath = '<%=context%>service/custInfoManager/';
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
		<input id="invtp" 	 type="hidden" value="${ invtp }"/>
		<input id="invnm" 	 type="hidden" value="${ dto.invnm }"/>
		<input id="idno" 	 type="hidden" value="${ dto.idno }"/>
		<input id="hiapType" value="${ dto.docbusinesstp }" type="hidden"/>
		<input id="specriskLevel" value="${dto.custrisklevl }" type="hidden" />
		<input id="invprtp" value="${dto.invprtp }" type="hidden" />
		<input id="oidlist" value='${dto.oidlist }' type="hidden" />
		<input id="ocontactlist" value='${dto.ocontactlist }' type="hidden" />
		<input id="documentlist" value='${dto.documentlist }' type="hidden" />
		<input type=hidden id="tradeaccos" name="tradeaccos" value="${ tradeAcco }"/>
		<div class="page-header" style="margin: 0;">
			<h4 class="page-title">银行资料修改</h4>
		</div>
		<div class="clearfix">
		    <ul class="nav nav-tabs" role="tablist">
		   		<li role="presentation" class="active">
		        	<a href="#baseInfo_show_content" onClick="showTypeContent('baseInfoContent','btnBaseInfo')" id="baseInfo_show_content-tab btnBaseInfo" data-toggle="tab" 
		        		aria-controls="baseInfo_show_content">银行信息</a>
		        </li>
		        <li role="presentation">
		        	<a href="#documentInfo_show_content" onclick="showTypeContent('documentInfoContent','btnDocumentInfo')" id="documentInfo_show_content-tab btnDocumentInfo" data-toggle="tab" 
		        		aria-controls="documentInfo_show_content" aria-expanded="true" role="tab" >资料信息</a>
		        </li>
		    </ul>
	 	</div><br>
	 	<form name="form1" id="form1" method="post" action="" >
		<div class="page-body">
			<!-- 基本信息 -->
			<div id="baseInfoContent" class="typeContent">
			<c:if test="${isMultiple == '1' }">
				<table class="table table-bordered" id="querytable1">
					<colgroup>
					  	<col width="20%">
					  	<col width="30%">
					  	<col width="20%">
					  	<col width="30%">
					</colgroup>
					<tbody>
						<tr>
							<td>委托方式：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="trustType" id="trustType" class='form-control select2_width'>
										<c:forEach var="item" items="${trustTypeArray}">
											<option value="${item.PMCO}" ${item.PMCO == '3' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>银行编号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="bnkNo" id="bnkNo" class='form-control select2_width'>
										<c:forEach var="item" items="${bankArray}">
											<option value="${item.bnkNo}" ${item.bnkNo == dto.bnkNo ? "selected" : "" }>${item.bnkNo}&nbsp;&nbsp;${ item.bnkNm }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td><font color="red">*</font>开户银行：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='openName' id='openName' value=""/>
								</div>
							</td>
							<td><font color="red">*</font>开户地：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="openAddr" id="openAddr" class='form-control select_addr1' onchange="getCitys(this.value,'1')">
										<option value="--" selected="selected">--</option>
										<c:forEach var="item" items="${provinces}">
											<option value="${item.PMCO}" ${item.PMCO == dto.openAddr ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="openbankcity" id="openbankcity" class='form-control select_addr2'><option value="" selected="selected">--</option></select>
									<input type="hidden" name="hiddencity" id="hiddencity" value="${dto.openBankCity }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>银行户名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='bankAccoNm' id='bankAccoNm'  value=""/>
								</div>
							</td>
							<td><font color="red">*</font>银行账号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='bankAcco' id='bankAcco'  value=""/>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</c:if>
			<c:if test="${isMultiple == '0' }">
				<table class="table table-bordered" id="querytable1">
					<colgroup>
					  	<col width="20%">
					  	<col width="30%">
					  	<col width="20%">
					  	<col width="30%">
					</colgroup>
					<tbody>
						<tr>
							<td>委托方式：</td>
							<td class="white-bg" colspan="4">
								<div class="col-sm-11 form-inner">
									<select name="trustType" id="trustType" class='form-control select2_width'>
										<c:forEach var="item" items="${trustTypeArray}">
											<option value="${item.PMCO}" ${item.PMCO == '3' ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td>客户名称：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									${dto.invnm }
								</div>
							</td>
							<td>客户类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									${dto.invtpnm }
								</div>
							</td>
						</tr>
						<tr>
							<td>证件类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									${dto.idtpnm }
								</div>
							</td>
							<td>证件号码：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									${dto.idno }
								</div>
							</td>
						</tr>
					</tbody>
					<tbody>
						<tr>
							<td><font color="red">*</font>银行编号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="bnkNo" id="bnkNo" class='form-control select2_width'>
										<c:forEach var="item" items="${bankArray}">
											<option value="${item.bnkNo}" ${item.bnkNo == dto.bnkNo ? "selected" : "" }>${item.bnkNo}&nbsp;&nbsp;${ item.bnkNm }</option>
										</c:forEach>
									</select>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>银行户名：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='bankAccoNm' id='bankAccoNm'  value="${dto.bankAccoNm }"/>
								</div>
							</td>
							<td><font color="red">*</font>银行账号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='bankAcco' id='bankAcco'  value="${dto.bankAcco }"/>
								</div>
							</td>
						</tr>
						<tr>
							<td><font color="red">*</font>开户银行：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' maxlength="30" class='form-control' name='openName' id='openName' value="${dto.openName }"/>
								</div>
							</td>
							<td><font color="red">*</font>开户地：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="openAddr" id="openAddr" class='form-control select_addr1' onchange="getCitys(this.value,'1')">
										<option value="" selected="selected">--</option>
										<c:forEach var="item" items="${provinces}">
											<option value="${item.PMCO}" ${item.PMCO == dto.openAddr ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
									<select name="openbankcity" id="openbankcity" class='form-control select_addr2'></select>
									<input type="hidden" name="hiddencity" id="hiddencity" value="${dto.openBankCity }"/>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</c:if>	
			</div>
			<!-- 资料信息 -->
			<div id="documentInfoContent" class="typeContent" style="display: none;">
				<table class="table table-bordered maintable" id="documentInfo">
					<colgroup>
					  	<col width="10%">
					  	<col width="20%">
					  	<col width="25%">
					  	<col width="20%">
					  	<col width="25%">
					</colgroup>
					
					<tbody>
						<tr>
							<td rowspan="99">资料信息</td>
							<td>业务类型：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="docbusinesstp" id="docbusinesstp" class='form-control select2_width'>
										<c:forEach var="item" items="${docbusitpArray}">
											<option value="${item.PMCO}" ${item.PMCO == dto.docbusinesstp ? "selected" : "" }>${item.PMCO}&nbsp;&nbsp;${ item.PMNM }</option>
										</c:forEach>
									</select>
								</div>
							</td>
							<td>客户资料是否齐全：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isalldoc" id="isalldoc" class='form-control select2_width'>
										<option value="1" ${dto.ifalldocument == '1' ? "selected" : "" }>是</option>
										<option value="0" ${dto.ifalldocument == '0' ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>客户资料是否原件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isoriginal" id="isoriginal" class='form-control select2_width'>
										<option value="1" ${dto.ifOriginal == '1' ? "selected" : "" }>是</option>
										<option value="0" ${dto.ifOriginal == '0' ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
							<td>客户资料是否归档：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="issaved" id="issaved" class='form-control select2_width'>
										<option value="1" ${dto.ifsaved == '1' ? "selected" : "" }>是</option>
										<option value="0" ${dto.ifsaved == '0' ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>是否上传附件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isupload" id="isupload" class='form-control select2_width'>
										<option value="1" ${dto.isupload == '1' ? "selected" : "" }>是</option>
										<option value="0" ${dto.isupload == '0' ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
							<td>文件编号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='fileno' id='fileno' disabled="disabled" value="${dto.fileno }"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>是否扫描：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isscan" id="isscan" class='form-control select2_width'>
										<option value="Y" ${dto.isscan == 'Y' ? "selected" : "" }>是</option>
										<option value="N" ${dto.isscan == 'N' ? "selected" : "" }>否</option>
									</select>
								</div>
							</td>
							<td>存档位置：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='keepaddress' id='keepaddress' disabled="disabled" value="${dto.keepaddress }"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>所属客户经理：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<input type='text' class='form-control' name='salesaccmanager' id='salesaccmanager' value="${dto.salesaccmanager }"/>
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
									<textarea rows="3" cols="50" class='form-control'  name="remarkinfo" id="remarkinfo"> ${dto.remarkinfo }</textarea>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
				<input type=hidden id="tano" name="tano" />
			</div>
			
			<div class="page-footer" style="margin-top: 20px;text-align: center;">
				<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提交" name='btnSubmit'>提交</button>
				<button id="claseBtn" name="claseBtn" type="button" class="btn btn-link" data-dismiss="modal" value="重置">重置</button>
				<button id="reset" name="reset" type="reset" class="btn btn-link" style="display: none;" value="重置">重置</button>
			</div>
		</div>
		</form>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/accountManger/custAccountMgr/modifyBankInfoDetail.js?v="<%=dateStr%>></script>
</body>	
</html>