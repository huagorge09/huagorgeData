<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>

<head>
 <!-- 这里引入导航信息header.tpl -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>修改主消息配置</title>
<script type="text/javascript">
var prdPath = '<%=context%>';
var WIDGET_PATH_PREFIX = "<%=context%>service/widget/"
//默认加载
$(function(){
	//把访问路径传到js
	setPathPrefix(prdPath);
});
</script>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/msg/msgConf.update.js"></script>
</head>

<body class="sub-page">
	<title>修改主消息配置</title>
	<form  name="updateForm" id="updateForm"  role="form"   method="post" class="form-horizontal" action="<%=context%>service/mailManager/msgConfUpdate.do"  >
 		<fieldset>
 			<div class="page-content">
    			<div class="page-header">
        			<h4 class="page-title">修改主消息配置</h4>
    			</div>
    		<div class="page-body">
            	<div class="form-group">
                	<label class="col-sm-2 control-label">标题<span class="text-danger">*</span></label>
                	<div class="col-sm-10">
                		<input type="hidden" name="mcId" id="mcId" value="${msgConf.mcId}"/>
                		<input type="text"  name="category" id="category" value="${msgConf.category}" placeholder="请输入标题" class="form-control" >
                	</div>
            	</div>
            	
            	<div class="form-group">
                	<label class="col-sm-2 control-label">排序号<span class="text-danger">*</span></label>
                	<div class="col-sm-10">
                		<input class="form-control" type="text" name="sortNo" id="sortNo" value="${msgConf.mcId}" 
						onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" 
						onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"/>
                	</div>
            	</div>
            	
            	<div class="form-group">
                	<label class="col-sm-2 control-label">EMAIL标题</label>
                	<div class="col-sm-10">
                		<input type="text"  name="head" id="head" value="${msgConf.head}"  class="form-control" >
                	</div>
            	</div>
            	
            	<div class="form-group">
                	<label class="col-sm-2 control-label">EMAIL内容</label>
                	<div class="col-sm-10">
                    	<textarea name="body" id="body" class="form-control" rows="10">${msgConf.body}</textarea>
                	</div>
            	</div>
            	
            	<div class="form-group">
                	<label class="col-sm-2 control-label">备注</label>
                	<div class="col-sm-10">
                    	<textarea name="remark" id="remark" class="form-control" rows="10">${msgConf.remark}</textarea>
                	</div>
            	</div>
            	
    			<div class="page-body">
            	<div class="page-header">
        			<h4 class="page-title">收件人</h4>
    			</div>
            	<div class="form-group">
                	<label class="col-sm-2 control-label">用户名称</label>
                	<div class="col-sm-10 form-inner">
                  		<select name="toList" id="toList" placeholder="收件人" multiple="multiple"  val="${tos}" text="${tosNm}"></select>
                	</div>
            	</div>
            	
            	
    			<div class="page-body">
            	<div class="page-header">
        			<h4 class="page-title">抄送人</h4>
    			</div>
            	<div class="form-group">
                	<label class="col-sm-2 control-label">用户名称</label>
                	<div class="col-sm-10 form-inner">
                  		<select name="ccList" id="ccList"  placeholder="抄送人" multiple="multiple"  val="${ccs}" text="${ccsNm}"></select>
                	</div>
            	</div>
            	<div class="form-group">
                	<label class="col-sm-2 control-label">手动配置邮件地址<br>(多个地址中间用逗号分隔)</label>
                	<div class="col-sm-10 form-inner">
                		<input type="text"  name="ccListAddr" id="ccListAddr"  class="form-control" value="${ccs1}"/>
                		<span id="msg" style="color: red; display: none;"  >请确认输入邮箱地址正确!</span>
                	</div>
            	</div>
    		</div>
    		<div class="page-body">
	    		<div class="page-header">
	        			<h4 class="page-title">密件抄送人</h4>
	    		</div>
	            <div class="form-group">
	                	<label class="col-sm-2 control-label">用户名称</label>
		              	<div class="col-sm-10 form-inner">
		                  		<select name="sccList" id="sccList"  placeholder="密件抄送人" multiple="multiple"  val="${sccs}" text="${sccsNm}"></select>
		               	</div>
	            </div>
	            
	            <div class="form-group">
                	<label class="col-sm-2 control-label">手动配置邮件地址<br>(多个地址中间用逗号分隔)</label>
                	<div class="col-sm-10 form-inner">
                		<input type="text"  name="sccListAddr" id="sccListAddr"  class="form-control" value="${sccs1}"/>
                		<span id="smsg" style="color: red; display: none;"  >请确认输入邮箱地址正确!</span>
                	</div>
            	</div>
	            
    	   </div>	
    <div class="page-footer">
         <button type="button" name="commitBtn" id="commitBtn" class="btn btn-primary" onClick="submitCheck();">提交</button>
         <button type="button" name="cancelBtn" id="cancelBtn" class="btn btn-link" data-dismiss="modal"  onclick="window.close();" >取消</button>
    </div>
</div>
</fieldset>
 </form>
</body>
</html>
