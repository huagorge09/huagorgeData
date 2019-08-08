<% 
	/*********************************************************************
	* @author :chenbq
	* @createTime: 2016-06-27
	*Remark: 定时任务管理
	*********************************************************************/
%>
<%@page import="com.cmwa.ecc.business.utils.WaConstants"%>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>

<head>
 <!-- 这里引入导航信息header.tpl -->
 <meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>综合营销平台</title>
<script type="text/javascript">
var prdPath = '<%=context%>service/schedule/';
//默认加载
$(function(){
	//把访问路径传到js
	setPathPrefix(prdPath);
});
</script>
</head>
<body class="fixed-nav gray-bg">
<div class="clearfix form-multi-col-panel">
    <div class="form-item-group form-horizontal"> 
	        <div class="form-item">
	        	<span class="form-field">定时任务名称：</span>
            	<span class="form-input">
	           		<input class="form-control" placeholder="定时任务名称" id="q-name" name="q-name">
	           	</span>
	        </div>
	        <div class="form-item">
	        	<span class="form-field">状态：</span>
            	<span class="form-input">
	           		<select class="form-control use-select2" id="q-state" name="q-state">
		               	<option value=''>全部</option>
						<option value='1'>激活</option>
						<option value='0'>未激活</option>
	           		</select>
	           	</span>
	        </div>
	         <div class="form-item"></div>
	         <div class="form-item"></div>
	         <div class="form-item"></div>
	    </div>
	    <div class="form-action text-right">
          <button class="btn btn-primary" type="submit"  id="queryBtn" name="queryBtn" onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp;查询</button>
          <button class="btn btn-outline btn-primary" id="resetBtn" name="resetBtn" type="reset">清空</button>
      </div>
</div>
	       
<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="scheduleList"></table>
    <div id="schedulePage"></div>
    <input type="hidden" name="hidCurrentEmpid" id="hidCurrentEmpid" value="<%=SessionUtils.getEmployee().getID() %>"/>
</div>


<!-- updateCronExp  -->
<div class="modal inmodal in" id="updateCronExpPanel" role="dialog" data-backdrop="static" aria-hidden="true">
    <div class="modal-dialog" style="width: 30%;height: 40%">
        <div class="modal-content" style="width: 100%;height: 100%">
            <div class="modal-header">
                <button type="button" onclick="checkSalesCancel()" class="close" data-dismiss="modal">
                    <span aria-hidden="true">×</span>
                    <span class="sr-only">Close</span>
                </button>
                <h4 class="modal-title">修改运行周期</h4>
            </div>
            <div class="modal-body" style="width: 100%;height: 95%">
	            <form action="" id="checkform" style="width: 90%;height: 100%">
	              <table width="100%" height="90%" border="0" align="center" cellpadding="0" cellspacing="0" >
		              <tr>
				  			<td  style="padding-bottom: 5px;padding-top: 5px;width: 30%;">
				  			 	运行周期
				  			</td>
				  			<td  style="padding-bottom: 5px;padding-top: 5px;width: 60%;">
				  				<input type="hidden" name="taskId" id="taskId" class='form-control'/>
								<input type="hidden" name="jobName" id="jobName" class=form-control/>
								<input type="hidden" name="jobGroup" id="jobGroup" class='form-control'/> 
				  				<input type="text" name="cronExp" id="cronExp" style="width: 100%;height: 34px;" class="form-control" />
				  			</td>
				  		</tr>
				  		<tr>
			  			<td colspan="2">
			  				<input class="btn btn-primary" type="button" value="确认" onclick="submitRealTime()"/>
			  				<input type="button" class="btn btn-outline btn-primary" value="取消" onclick="checkSalesCancel()"/>
			  			</td>
			  		</tr>
				</table>
	          </form>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/base/schedule.js"></script>
<script type="text/javascript" src="<%=context%>web/js/base/schedule.search.js"></script>
 
</body>
</html>