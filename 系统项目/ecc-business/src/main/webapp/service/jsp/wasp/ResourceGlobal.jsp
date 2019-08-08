<%@page import="com.cmwa.ecc.business.commonVo.Employee"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@page import="com.cmwa.ecc.business.utils.SessionUtils" %>
<%@page import="com.cmwa.ecc.business.utils.Utils"%>
<%@ taglib uri="/wasp-tags" prefix="wasp" %>
<meta http-equiv="nocache" content="no-cache">

<% 
	String context = request.getContextPath();
	context = context.equals("/")? context : context + "/";
	Employee employee = SessionUtils.getEmployee();
	String userName = employee.getName();
	String userId = employee.getID();
	String dateStr = Utils.getNowTime();
	String nowdate = Utils.getNowDateStr2();
	response.setHeader("P3P","CP='IDCDSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT'");
	response.setHeader("P3P","CP=\"CURaADMa DEVa PSAo PSDo OUR BUS UNI PUR INT DEM STA PRE COM NAV OTC NOI DSPCOR\"");
%> 

<script>
	var WIDGET_PATH_PREFIX = "<%=context%>service/widget/";
	var projectPath = '<%=context%>';
	var nowDate = '<%=nowdate%>';
</script>

<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/font-awesome/css/font-awesome.min.css" />
<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/iconfont/iconfont.css" />
<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/css/animate.css" />
<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/css/plugins/jQueryUI/jquery-ui-1.10.4.custom.min.css" />
<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/css/plugins/jqGrid/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/css/plugins/select2/select2.min.css" />
<link rel="stylesheet" 	type="text/css" href="<%=context%>web/css/bootstrap/css/plugins/datepicker/bootstrap-datepicker.css" />
<link rel="stylesheet" 	type="text/css" href="<%=context%>web/css/bootstrap/css/plugins/datetimepicker/bootstrap-datetimepicker.css" />
<link rel="stylesheet" 	type="text/css" href="<%=context%>web/css/bootstrap/css/plugins/daterangepicker/daterangepicker.css" />
<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/css/style.css" />
<link rel="stylesheet"  type="text/css" href="<%=context%>web/css/bootstrap/css/plugins/toastr/toastr.min.css" />
<link rel="stylesheet" 	type="text/css" href="<%=context%>web/css/bootstrap/css/plugins/iCheck/custom.css" />
<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/css/plugins/sweetalert/sweetalert.css" />
<link rel="stylesheet" type="text/css" href="<%=context%>web/js/themes/default/default.css" />

<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/jquery-2.1.1.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/jquery-ui-1.10.4.min.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/inspinia.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/jqGrid/jquery.jqGrid.min.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/jqGrid/i18n/grid.locale-cn.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/select2/select2.full.min.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/select2/i18n/zh-CN.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/validate/jquery.validate.min.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/toastr/toastr.min.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/fullcalendar/moment.min.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/fullcalendar/moment.zh-cn.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/datepicker/bootstrap-datepicker.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/datepicker/bootstrap-datepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/datetimepicker/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/datetimepicker/bootstrap-datetimepicker.zh-CN.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/daterangepicker/daterangepicker.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/iCheck/icheck.min.js"></script>
<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/sweetalert/sweetalert.min.js"></script>
<script type="text/javascript" src="<%=context%>web/js/common/common.js"></script>
<script type="text/javascript" src="<%=context%>web/js/common/validate.methods.js"></script>
<script type="text/javascript" src="<%=context%>web/js/kindeditor-all.js"></script>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>

