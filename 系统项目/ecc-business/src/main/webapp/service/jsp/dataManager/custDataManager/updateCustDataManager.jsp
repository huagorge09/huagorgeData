<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta charset="utf-8"/>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>修改客户资料-资料管理-直销柜台</title>
</head>
<body class="fixed-nav gray-bg">
	<div class="modal-header">
		<h4 class="modal-title">修改客户资料</h4>
	</div>
	<div class="modal-content" style="border: 0px;">
	 	<form name="updateCustDataFrom" id="updateCustDataFrom" method="post" action="<%=context%>service/custDataManager/updateCustDataManagerInfo.do">
	 	<input type="hidden" name="serialno" value="${dto.serialno }"/>
	 	<input type="hidden" name="appserialno" value="${dto.appserialno }"/>
	 	<input type="hidden" name="hidCustinfoid" value="${dto.custinfoid }"/>
	 	<input type="hidden" name="custinfoid"/>
	 	<input type="hidden" name="hidAptype" value="${dto.aptype }"/>
	 	<input type="hidden" name="hidAptypeName" value="${dto.aptypename }"/>
	 	<input type="hidden" name="hidIfalldocument" value="${dto.ifalldocument }"/>
	 	<input type="hidden" name="hidIfOriginal" value="${dto.ifOriginal }"/>
	 	<input type="hidden" name="hidIfsaved" value="${dto.ifsaved }"/>
	 	<input type="hidden" name="hidIsupload" value="${dto.isupload }"/>
	 	<input type="hidden" name="hidIsscan" value="${dto.isscan }"/>
	 	<input type="hidden" name="custno" value="${dto.custno }"/>
	 	<input type="hidden" name="fundacct" value="${dto.fundacct}"/>
		<div class="page-body">
			<!-- 资料信息 -->
			<div id="documentInfoContent" class="typeContent">
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
									<select name="aptype" id="docbusinesstp" class='form-control user-select2' param='{"pmst":"DS","pmky":"APTYPE"}' onchange="docChange()"></select>
								</div>
							</td>
							<td>客户资料是否齐全：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="ifalldocument" id="isalldoc" class='form-control select2_width'>
										<option value="0">否</option>
										<option value="1">是</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>客户资料是否原件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="ifOriginal" id="isoriginal" class='form-control select2_width'>
										<option value="0">否</option>
										<option value="1">是</option>
									</select>
								</div>
							</td>
							<td>客户资料是否归档：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="ifsaved" id="issaved" class='form-control select2_width'>
										<option value="0">否</option>
										<option value="1">是</option>
									</select>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>是否上传附件：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isupload" id="isupload" class='form-control select2_width'>
										<option value="0">否</option>
										<option value="1">是</option>
									</select>
								</div>
							</td>
							<td>文件编号：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input name="fileno" type="hidden" value="${dto.fileno}" />
									<input type='text' class='form-control' name='filenoText' id='fileno' disabled="disabled" value="${dto.fileno }"/>
								</div>
							</td>
						</tr>
						
						<tr>
							<td>是否扫描：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<select name="isscan" id="isscan" class='form-control select2_width' value="${dto.isscan }">
										<option value="N">否</option>
										<option value="Y">是</option>
									</select>
								</div>
							</td>
							<td>存档位置：</td>
							<td class="white-bg">
								<div class="col-sm-11 form-inner">
									<input name="keepaddress" type="hidden" value="${dto.keepaddress }" />
									<input type='text' class='form-control' name='keepaddressText' id='keepaddress' disabled="disabled" value="${dto.keepaddress }"/>
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
						
					<tbody id="documentinfo">
						<tr>
							<td id="docListTh">资料列表：</td>
						</tr>
					</tbody>
					<tbody>
						<tr>
							<td>资料信息备注：</td>
							<td class="white-bg" colspan="3">
								<div class="col-sm-11 form-inner">
									<textarea rows="3" cols="50" class='form-control' name="remarkinfo" id="remarkinfo">${dto.remarkinfo }</textarea>
								</div>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<div class="page-footer" style="margin-top: 20px;text-align: center;">
			<button id="btnUpload" type="button" class="btn btn-primary btn-save" value="资料上传" name='btnUpload' onclick="doUpload()">资料上传</button>
			<button id="btnSubmit" type="button" class="btn btn-primary btn-save" value="提交" name='btnSubmit' onclick="doSubmit()">提交</button>
			<button type="button" class="btn btn-link" onclick="window.close();" id="closeBtn" name="closeBtn">取消</button>
		</div>
		</form>
	</div>
<script type="text/javascript" src="<%=context%>web/js/dataManager/custDataManager/updateCustDataManager.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/custDataManager";
		setPath(primaryPath,basePath);
</script>
</body>	
</html>