<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>交易类业务复核查询</title>
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

.act_count{
	width: 100%;
    height: 50px;
    margin-bottom: 15px;
    background-color: #D6D4D4;
}
.pv_con{
	width: 15%;
    float: left;
    background-color: #D6D4D4;
    color: #000;
    text-align: center;
    height: 50px;	
}

</style>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
	<div class="permissionBtn" style="display: none;" id="tradeReCheckBtn">
		<a href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="进行复核" disabled ><i class="iconfont icon-wodeshenpi-copy"></i></a>
	</div>
	
  	<div class="form-item-group form-horizontal" style="width: auto;max-width: 1123px;" srole="form">
        <div class="form-item">
         	<span class="form-field">交易账号：</span>
            <span class="form-input">
            	<input type="hidden" id="currentUserId" value=""/>
            	<input type="text" placeholder="交易账号" class="form-control changeText clearText" id="tradeacco" name="tradeacco">
            </span>
        </div>
   		<div class="form-item">
   		 	<span class="form-field">基金账号：</span>
            <span class="form-input">
            	<input type="text" placeholder="基金账号" class="form-control changeText clearText" id="fundacco" name="fundacco">
            </span>
   		</div>
   		<div class="form-item">
   		 	<span class="form-field">申请编号：</span>
            <span class="form-input">
            	<input type="text" placeholder="申请编号" class="form-control changeText clearText" id="serialno" name="serialno">
            </span>
   		</div>
        <div class="form-item">
        	<span class="form-field">委托方式：</span>
            <span class="form-input">
            	<select name="trustType" id="trustType" placeholder="全部"  class="select2 use-select2 form-input clearText"></select>
            </span>
        </div>
        <div class="form-item">
        	<span class="form-field">基金名称：</span>
            <span class="form-input">
            	<select name="fundid" id="fundid" placeholder="全部"  class="select2 use-select2 form-input clearText"></select>
            </span>
        </div>
        <div class="form-item">
        	<span class="form-field">业务名称：</span>
            <span class="form-input">
            	<select name="dsapkind" id="dsapkind" placeholder="全部"  class="select2 use-select2 form-input clearText"></select>
            </span>
        </div>
        <div class="form-item">
        	<span class="form-field">复核状态：</span>
            <span class="form-input">
            	<select name="checkst" id="checkst" placeholder="全部"  class="select2 use-select2 form-input clearText"></select>
            </span>
        </div>
        <div class="form-item">
         	<span class="form-field">开始日期：</span>
            <span class="form-input">
            	<input type="text" placeholder="开始日期" class="form-control changeText" id="begindate" name="begindate">
            </span>
        </div>
        <div class="form-item">
         	<span class="form-field">结束日期：</span>
            <span class="form-input">
            	<input type="text" placeholder="结束日期" class="form-control changeText" id="enddate" name="enddate">
            </span>
        </div>
    </div>
	<div class="form-action text-right">
		<button class="btn btn-primary" type="submit"  id="queryBtn"  onclick="queryByCondition(true);"><i class="fa fa-search"></i>&nbsp;查询</button>
		<button class="btn btn-outline btn-primary" onclick="clearValue();" id="resetBtn" type="reset">清空</button>
	</div>
</div>
<div class="pv">
   <div class="act_count">
       <div class="pv_con left">
           <h4>复核作废</h4>
           <span class="pv_con_count" id="fcount">0</span>
       </div>
       <div class="pv_con left">
           <h4>未处理</h4>
           <span class="pv_con_count" id="ncount">0</span>
       </div>
       <div class="pv_con left">
           <h4>复核驳回</h4>
           <span class="pv_con_count" id="ccount">0</span>
       </div>
       <div class="pv_con left">
           <h4>复核成功</h4>
           <span class="pv_con_count" id="ycount">0</span>
       </div>
       <div class="pv_con left">
           <h4>撤单</h4>
           <span class="pv_con_count" id="cancelcount">0</span>
       </div>
       <div class="pv_con left">
           <h4>总数</h4>
           <span class="pv_con_count" id="totalcount">0</span>
       </div>
   </div>
</div>
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="tradeQryList"></table>
    <div id="tradeQryPage"></div>
</div>
</body>
<script type="text/javascript" src="<%=context%>web/js/tradeManager/tradeQry.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>capitalService/server";
		setPath(primaryPath,basePath);
</script>
</html>