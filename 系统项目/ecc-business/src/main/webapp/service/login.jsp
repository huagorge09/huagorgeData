<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html lang="zh-CN">
<head>
	<meta charset="UTF-8">
	<meta name="renderer" content="webkit">
	<script type="text/javascript" charset="utf-8" src="<%=context%>web/js/login.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=context%>web/css/login.css">
	<title>招商财富-财富直销柜台</title>
	<script type="text/javascript">
		var indexPath =  '<%=context%>service/index';
		var loginPath =  '<%=context%>service/login';
	</script>
</head>
<body>
<div id="container">
	<div id="login-panel">
		<div class="panel-content" id="login-form">
			<table class="table table-form">
				<tbody>
					<tr>
						<th>用户名</th>
						<td><input class="form-control" type="text" name="loginName" id="loginName" value=""></td>
					</tr>
					<tr>
						<th>密码</th>
						<td><input class="form-control" type="password" name="loginPwd" id="loginPwd" value=""></td>
					</tr>
					<tr>
						<th>验证码</th>
						<td><input class="form-control" id="verifyCode" name="verifyCode" type="text" placeholder="验证码" style="width: 120px;float: left;margin-right: 20px;">
							<span class="indentifyimg"> 
								<img src="<%=context%>service/verifyCode/verifyCode.action" id="rondomCodeImg" onclick="getRandomCode();" height="30" width="60">
							</span>
						</td>
					</tr>
					<tr>
						<th></th>
						<td>
							<button type="button" id="submit_btn" class="btn btn-primary" data-loading="稍候...">登录</button>
							<div class="message_div">
								<span class="message_text"></span>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
</body>
</html>