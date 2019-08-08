<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<title>修改银行信息</title>
<%
	Employee emp = SessionUtils.getEmployee();
//String permissionId = TransCodeConstant.TRANS_CODE_9320;//本页面操作权限代码
%>
</head>

<input id="transCode" type='hidden' value="9320" />
<body class="sub-page">
<form id="baseInfoForm" role="form" class="form-horizontal" method="post" action="">
 <!-- 如果将表单禁用，就给fieldset添加disabled属性 -->
 <fieldset>

<div class="page-content">
    <div class="page-header">
        <h4 class="page-title">修改银行基本信息</h4>
    </div>
    <div class="page-body">
        <!-- 产品名称 -->
         <div class="form-group">
                <label class="col-sm-2 control-label">银行代码<span class="text-danger">*</span></label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="银行代码" class="form-control" name="bnkNo" readonly id="bnkNo" value="${bankbaseVo.bnkNo}" />
               </div>
                <label class="col-sm-2 control-label">银行名称<span class="text-danger">*</span></label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="银行名称" class="form-control" name="bnkNm" id="bnkNm" value="${bankbaseVo.bnkNm}" />
               </div>
          </div>
          
           <div class="form-group">
                <label class="col-sm-2 control-label">银行简称<span class="text-danger">*</span></label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="银行简称" class="form-control" name="bnkNmAbbr" id="bnkNmAbbr" value="${bankbaseVo.bnkNmAbbr}" />
               </div>
                <label class="col-sm-2 control-label">联系人传真</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="联系人传真" class="form-control" name="linkManFax" id="linkManFax" value="${bankbaseVo.linkManFax}" />
               </div>
          </div>
  			<div class="form-group">
  				  <label class="col-sm-2 control-label">电话银行</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="电话银行" class="form-control" name="telBnk" id="telBnk" value="${bankbaseVo.telBnk}" />
               </div>
  			</div>
           <div class="form-group">
                <label class="col-sm-2 control-label">创建人</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="创建人" class="form-control" name="cMan" id="cMan" value="${userVo.usernm}" readonly/>
               </div>
                <label class="col-sm-2 control-label">创建时间</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="创建人" class="form-control" name="cTime" id="cTime" value="${bankbaseVo.cTime}" readonly/>
               </div>
          </div>
          <div class="form-group">
                <label class="col-sm-2 control-label">编辑人</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="创建人" class="form-control" name="eMan" id="eMan" value="<%=emp.getName() %>" readonly/>
               </div>
                <label class="col-sm-2 control-label">编辑时间</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="创建人" class="form-control" name="eTime" id="eTime" value="${eTime}" readonly/>
               </div>
          </div>
          
          
           <div class="form-group">
                <label class="col-sm-2 control-label">联系人</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="联系人" class="form-control" name="linkMan" id="linkMan" value="${bankbaseVo.linkMan}" />
               </div>
                <label class="col-sm-2 control-label">联系人电话</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="联系人电话" class="form-control" name="linkManTel" id="linkManTel" value="${bankbaseVo.linkManTel}" />
               </div>
          </div>
          
           <div class="form-group">
                <label class="col-sm-2 control-label">联系人电子邮件</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="联系人电子邮件" class="form-control" name="linkManEmail" id="linkManEmail" value="${bankbaseVo.linkManEmail}" />
               </div>
                <label class="col-sm-2 control-label">展示顺序</label>
                <div class="col-sm-4 form-inner">
                  <input type="text" placeholder="展示顺序" class="form-control" name="disOrder" id="disOrder" value="${bankbaseVo.disOrder}" />
               </div>
          </div>
          
          
           <div class="form-group">
                <label class="col-sm-2 control-label">展示标志</label>
                <div class="col-sm-4 form-inner">
                   <select id="disFlg" name="disFlg" val="${bankbaseVo.disFlg}" class="form-control select2" >
								<option value="Y">展示</option>
								<option value="N">不展示</option>
					</select>
               </div>
                <label class="col-sm-2 control-label">状态</label>
                <div class="col-sm-4 form-inner">
                        <select id="status" name="status"  val="${bankbaseVo.status}" class="form-control select2" >
								<option value="Y">正常</option>
								<option value="N">禁用</option>
							</select>
               </div>
          </div>
          <div class="form-group">
            <label class="col-sm-2 control-label">银行网址</label>
            <div class="col-sm-10 form-inner">
              <input type="text" class="form-control" placeholder="银行网址" name="webSite" id="webSite" value="${bankbaseVo.webSite}" size="84">
            </div>
          </div>
          
          <div class="form-group">
            <label class="col-sm-2 control-label">余额查询网址</label>
            <div class="col-sm-10 form-inner">
              <input type="text" class="form-control"   placeholder="余额查询网址" name="balanceUrl" id="balanceUrl" value="${bankbaseVo.balanceUrl}" size="84">
            </div>
          </div>
          
          <div class="form-group">
            <label class="col-sm-2 control-label">网上银行网址</label>
            <div class="col-sm-10 form-inner">
              <input type="text" class="form-control"  placeholder="网上银行网址" name="ebnkUrl" id="ebnkUrl" value="${bankbaseVo.ebnkUrl}" size="84">
            </div>
          </div>
          
    <div class="page-footer">
         <button type="button" class="btn btn-primary btn-save"  data-loading-text="保存中..." id="submitBtn" name="submitBtn" onclick="doSubmit('update')">保存</button>
          <button type="button" class="btn btn-link"  onclick="window.close();"  id="closeBtn" name="closeBtn">取消</button>
    </div>
</div>
</fieldset>
</form>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bankMgr/bnkBaseInfo.update.js?20180530"></script>
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/bankManager";
	setPath(primaryPath,basePath);
</script>
</body>
</html>