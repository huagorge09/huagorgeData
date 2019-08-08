<%@ page language="java" pageEncoding="UTF-8" contentType="text/html;charset=UTF-8" %>
<%
	String context = request.getContextPath();
	context = context.equals("/")? context : context + "/";
	response.sendRedirect(context + "service/index/indexView.xhtml");
%>