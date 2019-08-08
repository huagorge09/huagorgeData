<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>增加第三方支付渠道支持银行</title>
<%

//String permissionId = TransCodeConstant.TRANS_CODE_9320;//本页面操作权限代码

SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
Date nowDateTime = new Date();
String nowDate = sdfDate.format(nowDateTime);
String nowTime = sdfTime.format(nowDateTime);
Employee emp  = SessionUtils.getEmployee();
%>
</head>
<input id="transCode" type='hidden' value="9320" />
<body class="sub-page">
<form id="payChannelForm" role="form" class="form-horizontal" method="post" action="">
 <!-- 如果将表单禁用，就给fieldset添加disabled属性 -->
 <fieldset>

<div class="page-content">
    <div class="page-header">
        <h4 class="page-title">增加第三方支付渠道支持银行</h4>
    </div>
    <div class="page-body">
        <!-- 产品名称 -->
         <div class="form-group">
                <label class="col-sm-2 control-label">第三方支付渠道代码<span class="text-danger">*</span></label>
                <div class="col-sm-4 form-inner">
                  <select id="thirdChannel" name="thirdChannel"  class="form-control select2" >
                  </select>
               </div>
                <label class="col-sm-2 control-label">第三方支付银行代码</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="第三方支付银行代码" class="form-control" name="thirdBankNo" id="thirdBankNo" value="" />
               </div>
          </div>
          <div class="form-group">
                <label class="col-sm-2 control-label">支持银行<span class="text-danger">*</span></label>
                <div class="col-sm-4 form-inner">
                  <select id="bankNO" name="bankNO"  class="form-control select2" >
                  </select>
               </div>
                <label class="col-sm-2 control-label">扣款方式</label>
                <div class="col-sm-4 form-inner">
                  <select id='payMode' name='payMode'  class="form-control select2" >
						<option value="0" >B2B委托代扣</option>
						<option value="1" >B2C网银支付</option>
                  </select>
               </div>
          </div>
          <div class="form-group">
                <label class="col-sm-2 control-label">状态<span class="text-danger">*</span></label>
                <div class="col-sm-4 form-inner">
                  <select id='status' name='status'  class="form-control select2" >
						<option value="Y" >有效</option>
					     <option value="N" >无效</option>
                  </select>
               </div>
                <label class="col-sm-2 control-label">是否支持定投</label>
                <div class="col-sm-4 form-inner">
                  <select id='mipFlag' name='mipFlag'  class="form-control select2" >
						<option value="Y" >是</option>
						<option value="N" >否</option>
                  </select>
               </div>
          </div>
          <div class="form-group">
               <label class="col-sm-2 control-label">创建日期</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="创建日期" class="form-control" name="createDate" id="createDate" value="<%=nowDate %>" readonly />
               </div>
                <label class="col-sm-2 control-label">创建时间</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="创建时间" class="form-control" name="createTime" id="createTime" value="<%=nowTime %>" readonly/>
               </div>
          </div>
          <div class="form-group">
               <label class="col-sm-2 control-label">修改日期</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="修改日期" class="form-control" name="modifyDate" id="modifyDate" value="<%=nowDate %>" readonly />
               </div>
                <label class="col-sm-2 control-label">修改时间</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="修改时间" class="form-control" name="modifyTime" id="modifyTime" value="<%=nowTime %>" readonly/>
               </div>
          </div>
          <div class="form-group">
               <label class="col-sm-2 control-label">创建人</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="创建人" class="form-control" name="createOpid" id="createOpid" value="<%= emp.getName() %>" readonly />
               </div>
                <label class="col-sm-2 control-label">修改人</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="修改时间" class="form-control" name="modifyOpid" id="modifyOpid" value="<%= emp.getName() %>" readonly/>
               </div>
          </div>
           <div class="form-group">
            <label class="col-sm-2 control-label">开户演示链接</label>
            <div class="col-sm-10 form-inner">
              <input type="text" class="form-control"  placeholder="开户演示链接" name="openDemo" id="openDemo" value="" size="84">
            </div>
          </div>
          
           <div class="form-group">
            <label class="col-sm-2 control-label">开户注意事项链接</label>
            <div class="col-sm-10 form-inner">
              <input type="text" class="form-control"  placeholder="开户注意事项链接" name="openObserve" id="openObserve" value="" size="84">
            </div>
          </div>
           <div class="form-group">
                <label class="col-sm-2 control-label">是否推荐</label>
                <div class="col-sm-4 form-inner">
                  <select id='recommend' name='recommend'  class="form-control select2" >
						<option value="Y" >是</option>
					     <option value="N" >否</option>
                  </select>
               </div>
          </div>
    <div class="page-footer">
         <button type="button" class="btn btn-primary btn-save"  data-loading-text="保存中..." id="submitBtn" name="submitBtn" onclick="doSubmit('add')">保存</button>
          <button type="button" class="btn btn-link"  onclick="window.close();"  id="closeBtn" name="closeBtn">取消</button>
    </div>
</div>
</fieldset>
</form>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bankMgr/payChannel.create.js?20180627"></script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/payChannelManager";
	setPath(primaryPath,basePath);
</script></body>
</html>