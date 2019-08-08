<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<head>
<!-- 这里引入导航信息header.tpl -->
<meta charset="utf-8">
<title>招商财富-财富直销柜台</title>
<link rel="icon" href="../../favicon.ico" mce_href="favicon.ico" type="image/x-icon">  
<link rel="shortcut icon" href="../../favicon.ico" mce_href="favicon.ico" type="image/x-icon">
<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/css/index.css" />
<link rel="stylesheet" type="text/css" href="<%=context%>web/css/bootstrap/css/indexMsgView.css" />
<style type="text/css">
.default-bg{
    background-image: url('<%=context%>web/images/coin.png');
    background-repeat: no-repeat;
    background-size: 20%;
    background-position: center;
}
.setHeight{
	height: auto !important;
}
</style>
</head>
<script type="text/javascript">
	var msgPath = '<%=context%>service/msgView/';
	var loginPath =  '<%=context%>service/login';
	var BASE_PATH = '<%=context%>';
</script>
<body class="fixed-nav">
	<div id="wrapper">
		<!-- 这里引入导航信息nav.tpl -->
		<nav class="navbar-default navbar-static-side" role="navigation">
			<div class="sidebar-collapse">
				<wasp:menu  menuMap="${menuMap}"></wasp:menu>
			</div>
		</nav>
		
		<!-- 详细内容信息 -->
		<div id="page-wrapper" class="gray-bg  default-bg">
			<!-- 这里引入顶部导航信息topbar.tpl -->
			<div class="row">
				<nav class="navbar navbar-fixed-top row" role="navigation">
					<div class="navbar-header">
						<img class="logo-element" src="<%=context%>web/css/bootstrap/css/img/LOGO-1.jpg">
						<a class="navbar-minimalize minimalize-styl-2 btn btn-primary " href="#">
							<i class="fa fa-bars"></i>
						</a>
						<!-- <div class="navbar-form-custom">
							<input type="text" placeholder="搜索菜单..." class="form-control" name="top-search" id="top-search" autocomplete="off">
						</div> -->
					</div>
					<div class="systemInfo" id="eccSystemInfo"> <!-- 休市增加hughCity-->
				         <span>
				            <span class="status"></span>当前系统状态
				             <span id="eccSysStatusName" class="stautsText"></span>
				         </span>
				         <span class="sysTime">当前工作日期：<span id="eccSysWorkDate"></span></span>
			       	</div>
					<ul class="nav navbar-top-links navbar-right">
						<li class="dropdown index-dropdown-msg">
		                    <a id="index-msg-list-switch" class="dropdown-toggle count-info index-dropdown-switch" data-toggle="dropdown" href="#" aria-expanded="false">
		                        <i class="fa fa-envelope fa-lg"></i>  <span id="index-msg-number" class="label label-warning">0</span>
		                    </a>
		                    <ul class="dropdown-menu dropdown-messages" >
		                    	<li>
			                    	<ul id="dropdown-messages"></ul>
		                        </li>
		                        <li class="dropdown-messages-link clearfix">
		                            <div class="text-center link-block pull-left index-my-msg">
		                                <a id="btn-my-msg">
		                                    <i class="fa fa-envelope text-warning"></i> <strong class="text-warning">查看全部通知</strong>
		                                </a>
		                            </div>
		                            <div class="pull-left index-msg-flag-query text-center link-block">
		                            	<a><i class="fa fa-flag text-warning"></i></a>
		                            </div>
		                        </li>
		                    </ul>
		                </li>
						<li class="dropdown">
							<a class="dropdown-toggle" data-toggle="dropdown" href="#"> 
								<img id="headImage" alt="image" class="img-circle" src="<%=context %>web/css/bootstrap/css/img/profile_small.jpg" width="30" height="30" />
								&nbsp;&nbsp;${userName}
							</a>
							<ul class="dropdown-menu animated fadeInRight m-t-xs">
								<li><a id="loginOutBtn">登出</a></li>
							</ul>
						</li>
					</ul>
				</nav>
			</div>
            <li class="dropdown-messages-item dropdown-messages-item-templete">
                <div class="dropdown-messages-box clearfix" data-id="">
                	<div class="pull-left index-msg-content">
                		<strong class="index-msg-title"></strong>
                	</div>
                	<div class="pull-left">
                		<small class="text-muted index-msg-date"></small>
                	</div>
                	<div class="pull-left index-msg-operate text-center">
                		<span>
                			<a class="index-msg-flag" title="标记"><i class="fa fa-flag-o text-warning"></i></a>
                			<a class="index-msg-stat" title="已读"><i class="fa fa-remove text-warning"></i></a>
                		</span>
                	</div>
                </div>
            </li>

			<!-- 这里内容 -->
			<div class="wrapper wrapper-content page-tabs">
				<div class="border-bottom white-bg page-tabs-header">
					<ul class="nav-page-tabs">
					</ul>
				</div>
				<div class="nav-page-container"></div>
			</div>
			<!-- 这里引入脚本信息footer.tpl -->
			<div class="footer">
				<div class="pull-right"><strong>&copy; <script type="text/javascript"> document.write(new Date().getFullYear());</script> 招商财富资产管理有限公司</strong></div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/plugins/pace/pace.min.js"></script>
 	<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/index.js"></script>
 	<script type="text/javascript" src="<%=context%>web/js/bootstrap/js/indexMsgView.js"></script>
 	<script type="text/javascript">
	 	$("#side-menu a").mouseover(function(){
			$(".nav-second-level").addClass("setHeight");
		});
		$("#side-menu a").mouseout(function(){
			$(".nav-second-level").removeClass("setHeight");
		});
 	</script>
</html>

