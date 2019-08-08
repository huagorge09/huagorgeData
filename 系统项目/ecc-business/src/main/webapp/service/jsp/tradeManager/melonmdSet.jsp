<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分红方式设置</title>
<style type="text/css">
.modal-content{
	border: 0px solid #e2e2e2;
    box-shadow: 0 0px 0px rgba(0, 0, 0, 0.3);
}
.sub-page{
	background-clip: padding-box;
    background-color: #f5f8f8;
    border: 1px solid #e2e2e2;
    box-shadow: 0 0px 0px rgba(0, 0, 0, 0.3);
}
.form-control{
	width: 250px !important;
}
.select2-container{
	width: 250px !important;
}

.productDiv{
	line-height: 20px;
    width: 100%;
    min-width: 100%;
    height: 30px;
}

.productInput{
	float: left;
    margin: 10px 5px 0 5px;
    width: 35px;
}

.productText{
	width: 90%;
}
</style>
</head>
<body class="sub-page">
	<div class="modal-content">
		<div>
	        <h4 class="modal-title">分红方式设置</h4>
	    </div>
	    <div class="modal-body">
	    	<div class="table-responsive">
	         <form id="melonmdSet" action="" name="melonmdSet"  method="post" class="form-horizontal">
	            <table class="table table-bordered">
	            	<colgroup>
	                    <col width="20%">
	                    <col width="30%">
	                    <col width="20%">
	                    <col width="30%">
	                </colgroup>
	                <tbody style="border-top: 0;">
	                	<tr style="display: none;">
	                		<td>
	                			<input type="hidden" name="permissionId" value="8025">
	                			<input type="hidden" name="hidtradeacco" value="${tradeAccos}"/>
	                			<input type="hidden" name="hidfundid" class="findIds"/>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td><font color="red">*</font>委托方式：</td>
	                		<td class="white-bg form-inner">
	                			<select name="trustType" id="trustType" class="select2">
		                			<option value="--">- -</option>
	                			<c:forEach var="trustType" items="${trustTypeArray}">
	                			<c:if test="${trustType.pmco == '3'}">
	                				<option value="${trustType.pmco}" selected="selected">${trustType.pmco}　${trustType.pmnm}</option>
	                			</c:if>
								<c:if test="${trustType.pmco != '3'}">
									<option value="${trustType.pmco}">${trustType.pmco}　${trustType.pmnm}</option>
								</c:if>	
								</c:forEach>
	                			</select>
	                		</td>
	                		<td class="white-bg form-inner" colspan="2">
	                			<span style="line-height: 33px;">
						    		<input type="checkbox" name="checkAll" id="checkAll" style="float: left;margin: 10px 5px 0 5px;" onclick="selectAll();">
						    		<span style="float: left;">&nbsp;全选&nbsp;&nbsp;</span>
						    	</span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td><font color="red">*</font>基金名称：</td>
	                		<td class="white-bg form-inner">
	                		<c:forEach var="fundNm1" items="${fundNmArray1}">
								<div class="productDiv">
						    		<input class="productInput" type="checkbox" name="fundid" class="fundid" value="${fundNm1.fundId}"/>
						    		<span class="productText">${fundNm1.fundId}　${fundNm1.fundShortNm}</span>
						    	</div>
							</c:forEach>
	                		</td>
	                		<td class="white-bg form-inner" colspan="2">
	                			<c:forEach var="fundNm2" items="${fundNmArray2}">
								<div class="productDiv">
						    		<input class="productInput" type="checkbox" name="fundid" class="fundid" value="${fundNm2.fundId}"/>
						    		<span class="productText">${fundNm2.fundId}　${fundNm2.fundShortNm}</span>
						    	</div>
							</c:forEach>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td><font color="red">*</font>分红方式：</td>
	                		<td class="white-bg form-inner">
	                			<select name="melonmd" id="melonmd" class="select2">
	                			<option value="--">- -</option>
                				<c:forEach var="melonmd" items="${melonmdArray}">
									<option value="${melonmd.pmco}">${melonmd.pmco}　${melonmd.pmnm}</option>
								</c:forEach>
	                			</select>
	                		</td>
	                		<td><font color="red">*</font>分红比例：</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" class="form-control" id="melonmdpercent" name="melonmdpercent" value="1.0"/>
	                		</td>
	                	</tr>
	                	<tr id="checknoTr">
							<td>主管工号：</td>
							<td class="white-bg form-inner">
								<input style="display: none;" type="text" id="checkno" name="checkno"/>
								<span class="checkno"></span>
							</td>
							<td>主管密码：</td>
							<td class="white-bg form-inner">
								<input style="display: none;" type="text" id="checkpwd" name="checkpwd"/>
								<span class="checkpwd"></span>
							</td>
						</tr>
	                </tbody>
	            </table>
				<table class="table">
                	<tbody>
						<tr>
							<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
								<button type="button" class="btn btn-primary btn-w-xs" id="audit" name="audit" onclick="doAudit();">授权</button>
								<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="subBtn" name="subBtn" onclick="checkSubmit();">提交</button>
								<button type="button" class="btn btn-link btn-w-xs" id="btnClear" name="btnClear"onclick="window.close();">取消</button>
					   		</td>
			   			</tr>
	   				</tbody>
				</table>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/tradeManager/melon.js"></script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>capitalService/server";
	setPath(primaryPath,basePath);
	addFromValidate();
</script>
</body>
</html>