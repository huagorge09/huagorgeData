<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易撤单</title>
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
	width: 200px !important;
}
.select2-container{
	width: 200px !important;
}

.form-multi-col-panel .form-field{
	width: 80px !important;
}

.form-item-wid{
	width: 100% !important;
}
</style>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	<form action="" id="cancelFrom" name="cancelFrom">
  	<div class="form-item-group form-horizontal" style="width: auto;max-width: 88%;" srole="form">
        <div class="form-item">
        	<span class="form-field">委托方式：</span>
            <span class="form-input">
            	<select name="trustType" id="trustType" placeholder="全部"  class="select2 use-select2 form-input clearText"></select>
            </span>
        </div>
        <div class="form-item">
         	<span class="form-field">账户类型：</span>
            <span class="form-input">
            	<select name="accountType" id="accountType" class="select2" onchange="setType(this.value);">
					<option value="TRADEACCO">交易账号</option>
					<option value="FUNDACCO" selected>基金账号</option>
				</select>
            </span>
        </div>
        <div class="form-item">
         	<span class="form-field">交易账号：</span>
            <span class="form-input">
            	<input type="text" placeholder="交易账号" class="form-control changeText" id="tradeacco" name="tradeacco">
            </span>
        </div>
   		<div class="form-item">
   		 	<span class="form-field">基金账号：</span>
            <span class="form-input">
            	<input type="text" placeholder="基金账号" class="form-control changeText" id="fundacco" name="fundacco">
            </span>
   		</div>
   		<div class="form-item"></div>
        <div class="form-item form-item-wid" style="width: 100%;">
        	<span class="form-field">客户简称：</span>
            <span class="form-input">
            	<select name="firstCustGroup" id="firstCustGroup" placeholder="全部"  class="select2 use-select2 form-input" onchange="loadSecondCustGroup(this.value);"></select>
            </span>
            <span class="form-field"> - </span>
            <span class="form-input">
            	<select name="secondCustGroup" id="secondCustGroup" placeholder="全部"  class="select2 use-select2 form-input"></select>
            </span>
        </div>
    </div>
    </form>
	<div class="form-action text-right">
		<button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondition(true);"><i class="fa fa-search"></i>&nbsp;查询</button>
	</div>
</div>

<form action="" id="cancelSubmitFrom" name="cancelSubmitFrom">
<table id="userInfoTable" class="table table-bordered" style="margin-top: 30px;display: none;">
	<colgroup>
        <col width="20%">
        <col width="30%">
        <col width="20%">
        <col width="30%">
    </colgroup>
    <tbody style="border-top: 0;">
    	<tr style="display: none;">
    		<td>
    			<input type="hidden" id="queryflag" name="queryflag" value="N" />
    			<input type="hidden" name="permissionId" id="permissionId" value="8032" />
    			<input type="hidden" name="operatorId" id="operatorId" value="" />
    			<input type="hidden" id="custno" name="custno"/>
				<input type="hidden" id="hidtradeacco" name="hidtradeacco"/>
				<input type="hidden" id="oldserialno" name="oldserialno"/>
				<input type="hidden" class="trustType" name="trustType"/>
    		</td>
    	</tr>
		<tr>
	  		<td>客户名称：</td>
	  		<td class="white-bg form-inner">
	  			<span class="invnm"></span>
	  		</td>
	  		<td>客户类型：</td>
	  		<td class="white-bg form-inner">
	  			<span class="invtp"></span>
	  		</td>
  		</tr>
	  	<tr>
	  		<td>证件类型：</td>
	  		<td class="white-bg form-inner">
	  			<span class="idtpNm"></span>
	  		</td>
	  		<td>证件号码：</td>
	  		<td class="white-bg form-inner">
	  			<span class="idno"></span>
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
</form>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="cancelInfoList"></table>
    <div id="cancelInfoPage"></div>
</div>
<table class="table table-bordered" style="margin-top:10px; border: 0px;">
	<colgroup>
        <col width="20%">
        <col width="30%">
        <col width="20%">
        <col width="30%">
    </colgroup>
    <tbody style="border-top: 0;">
    	<tr>
			<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
				<button type="button" class="btn btn-primary btn-w-xs" id="audit" name="audit" onclick="doAudit();">授权</button>
				<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="subBtn" name="subBtn" onclick="checkSubmit();">提交</button>
	   		</td>
	 	</tr>
	 </tbody>
</table>
</body>
<script type="text/javascript" src="<%=context%>web/js/tradeManager/cancel.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>capitalService/server";
		setPath(primaryPath,basePath);
</script>
</html>