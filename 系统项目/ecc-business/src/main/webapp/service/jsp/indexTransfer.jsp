<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta HTTP-EQUIV="pragma" CONTENT="no-cache"> 
<meta HTTP-EQUIV="Cache-Control" CONTENT="no-cache, must-revalidate"> 
<meta HTTP-EQUIV="expires" CONTENT="0">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>-直销柜台</title>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
</head>
<body class="fixed-nav gray-bg">
<script type="text/javascript">
	var basePath = "<%=context%>";
	var primaryPath = "<%=context%>service/index";
	var prev = "${requestUrl}";
	if(!!prev){
		console.log(prev);
		window.location.href = prev;
	}else{
		window.location.href = primaryPath+"/indexView.xhtml";
	}
</script>
</body>
</html>