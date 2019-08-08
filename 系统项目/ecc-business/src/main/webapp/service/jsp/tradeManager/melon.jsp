<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>设置分红方式</title>
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
</style>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
  	<div class="form-item-group form-horizontal" style="width: auto;max-width: 1123px;" srole="form">
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
        <div class="form-item" style="width: 100%;">
        	<span class="form-field">客户简称：</span>
            <span class="form-input">
            	<select name="firstCustGroup" id="firstCustGroup" placeholder="全部"  class="select2 use-select2 form-input" onchange="loadSecondCustGroup(this.value);"></select>
            </span>
            <span class="form-field">　　</span>
            <span class="form-input">
            	<select name="secondCustGroup" id="secondCustGroup" placeholder="全部"  class="select2 use-select2 form-input"></select>
            </span>
        </div>
    </div>
    
	<div class="form-action text-right">
		<button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondition(true);"><i class="fa fa-search"></i>&nbsp;查询</button>
		<button class="btn btn-outline btn-primary" id="resetBtn" type="reset">清空</button>
		<button class="btn btn-primary" type="button" onclick="goUpdateMelonPage();">&nbsp;修改</button>
	</div>
</div>

<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="melonInfoList"></table>
    <div id="melonInfoPage"></div>
</div>
</body>
<script type="text/javascript" src="<%=context%>web/js/tradeManager/melon.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>capitalService/server";
		setPath(primaryPath,basePath);
</script>
</html>