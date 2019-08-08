<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改第三方支付渠道支持银行</title>
<%

//String permissionId = TransCodeConstant.TRANS_CODE_9320;//本页面操作权限代码

SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
Date nowDateTime = new Date();
String nowDate = sdfDate.format(nowDateTime);
String nowTime = sdfTime.format(nowDateTime);
Employee emp = SessionUtils.getEmployee();
%>
</head>
<body class="sub-page">
<input id="transCode" type='hidden' value="9320" />
<form id="payChannelForm" role="form" class="form-horizontal" method="post" action="">
 <!-- 如果将表单禁用，就给fieldset添加disabled属性 -->
 <fieldset>

<div class="page-content">
    <div class="page-header">
        <h4 class="page-title">修改第三方支付渠道支持银行</h4>
    </div>
    <div class="page-body">
    	<input type="hidden" name="modifyOpid" id="modifyOpid" value="${payChannelVo.modifyOpid}">
    	<input type="hidden" name="createOpid" id="createOpid" value="${payChannelVo.createOpid}">
    	<input type="hidden" name="oldBankNo" id="oldBankNo" value="${payChannelVo.bankNO}">
    	<input type="hidden" name="thirdChannel" id="thirdChannel" value="${payChannelVo.thirdChannel}">
        <!-- 产品名称 -->
         <div class="form-group">
                <label class="col-sm-2 control-label">第三方支付渠道代码<span class="text-danger">*</span></label>
                <div class="col-sm-4 form-inner">
                	<input type="text" placeholder="第三方支付渠道代码" class="form-control"  readonly name="thirdChannelName" id="thirdChannelName" value="${payChannelVo.thirdChannelName}" />
               </div>
                <label class="col-sm-2 control-label">第三方支付银行代码</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="第三方支付银行代码" class="form-control" name="thirdBankNO" id="thirdBankNO" value="${payChannelVo.thirdBankNO}" />
               </div>
          </div>
          <div class="form-group">
                <label class="col-sm-2 control-label">支持银行<span class="text-danger">*</span></label>
                <div class="col-sm-4 form-inner">
                  <select id="bankNO" name="bankNO"  class="form-control select2"   val="${payChannelVo.bankNO}" text="${payChannelVo.bankName}">
                  </select>
               </div>
                <label class="col-sm-2 control-label">扣款方式</label>
                <div class="col-sm-4 form-inner">
                  <select id='payMode' name='payMode'  class="form-control select2" val="${payChannelVo.payMode}" text="${payChannelVo.payModeName}" >
						<option value="0" <c:if test ="${payChannelVo.payMode == '0'}">selected</c:if> >B2B委托代扣</option>
						<option value="1" <c:if test ="${payChannelVo.payMode == '1'}">selected</c:if>>B2C网银支付</option>
                  </select>
               </div>
          </div>
          <div class="form-group">
                <label class="col-sm-2 control-label">状态<span class="text-danger">*</span></label>
                <div class="col-sm-4 form-inner">
                  <select id='status' name='status'  class="form-control select2" val="${payChannelVo.status}" text="${payChannelVo.statusName}">
						<option value="Y" <c:if test ="${payChannelVo.status == 'Y'}">selected</c:if>>有效</option>
					     <option value="N" <c:if test ="${payChannelVo.status == 'N'}">selected</c:if>>无效</option>
                  </select>
               </div>
                <label class="col-sm-2 control-label">是否支持定投</label>
                <div class="col-sm-4 form-inner">
                  <select id='mipFlag' name='mipFlag'  class="form-control select2" val="${payChannelVo.mipFlag}" text="${payChannelVo.mipFlagName}">
						<option value="Y" <c:if test ="${payChannelVo.mipFlag == 'Y'}">selected</c:if>>是</option>
						<option value="N" <c:if test ="${payChannelVo.mipFlag == 'N'}">selected</c:if>>否</option>
                  </select>
               </div>
          </div>
          <div class="form-group">
               <label class="col-sm-2 control-label">创建日期</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="创建日期" class="form-control" name="createDate" id="createDate" value="${payChannelVo.createDate}" readonly />
               </div>
                <label class="col-sm-2 control-label">创建时间</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="创建时间" class="form-control" name="createTime" id="createTime" value="${payChannelVo.createTime}" readonly/>
               </div>
          </div>
          <div class="form-group">
               <label class="col-sm-2 control-label">修改日期</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="修改日期" class="form-control" name="modifyDate" id="modifyDate" value="${payChannelVo.modifyDate}" readonly />
               </div>
                <label class="col-sm-2 control-label">修改时间</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="修改时间" class="form-control" name="modifyTime" id="modifyTime" value="${payChannelVo.modifyTime}" readonly/>
               </div>
          </div>
          <div class="form-group">
               <label class="col-sm-2 control-label">创建人</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="创建人" class="form-control" name="createOpName" id="createOpName" value="${payChannelVo.createOpName}" readonly />
               </div>
                <label class="col-sm-2 control-label">修改人</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="修改人" class="form-control" name="modifyOpName" id="modifyOpName" value="${payChannelVo.modifyOpName}" readonly/>
               </div>
          </div>
           <div class="form-group">
            <label class="col-sm-2 control-label">开户演示链接</label>
            <div class="col-sm-10 form-inner">
              <input type="text" class="form-control"  placeholder="开户演示链接" name="openDemo" id="openDemo" value="${payChannelVo.openDemo}" size="84">
            </div>
          </div>
          
           <div class="form-group">
            <label class="col-sm-2 control-label">开户注意事项链接</label>
            <div class="col-sm-10 form-inner">
              <input type="text" class="form-control"  placeholder="开户注意事项链接" name="openObseRev" id="openObseRev" value="${payChannelVo.openObseRev}" size="84">
            </div>
          </div>
           <div class="form-group">
                <label class="col-sm-2 control-label">是否推荐</label>
                <div class="col-sm-4 form-inner">
                  <select id='recommend' name='recommend'  class="form-control select2" val="${payChannelVo.recommend}" >
						<option value="Y" <c:if test ="${payChannelVo.recommend == '是'}">selected</c:if>>是</option>
					     <option value="N" <c:if test ="${payChannelVo.recommend == '否'}">selected</c:if>>否</option>
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
<script type="text/javascript" src="<%=context%>web/js/common/widget.js?20180602"></script>
<script type="text/javascript" src="<%=context%>web/js/bankMgr/payChannel.update.js?20180627"></script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/payChannelManager";
	setPath(primaryPath,basePath);
</script>
</body>
</html>