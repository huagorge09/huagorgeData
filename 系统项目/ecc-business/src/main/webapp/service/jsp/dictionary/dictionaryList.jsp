<%@page import="com.cmwa.ecc.business.utils.WaConstants"%>
<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>

<head>
 <!-- 这里引入导航信息header.tpl -->
 <meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>招商基金知识管理系统</title>
<script type="text/javascript">
var dictPath = '<%=context%>' +'service/dictionary/';
//默认加载
$(function(){
	//把访问路径传到js
	setPathPrefix(dictPath);
});
</script>
</head>

<body class="fixed-nav gray-bg">

<div class="clearfix form-multi-col-panel">
    <div class="form-search-group">
        <a class="btn btn-primary" id="dictionaryAddViewBtn" name="dictionaryAddViewBtn" href="javascript:javascript:WASP_DICTIONARY.dictionaryAddView();"><i class="fa fa-plus"></i>&nbsp新增</a>
        <a class="btn btn-primary" id="dictionaryRefreshBtn" name="dictionaryRefreshBtn" href="javascript:javascript:WASP_DICTIONARY.dictionaryRefresh();"><i class="fa fa-refresh"></i>&nbsp刷新缓存</a>
    </div>
     <div class="hr-line-dotted"></div>
  <div class="form-item-group form-horizontal" role="form">
    
        <div class="form-item ">
            <span class="form-field">字典根类型：</span>
            <span class="form-input">
            	<input type="text" placeholder="字典根类型" class="form-control" name="q-txtDictRootType" id="q-txtDictRootType" value="">
            </span>
        </div>
         <div class="form-item ">
            <span class="form-field">字典父类型：</span>
            <span class="form-input">
            	<input type="text" placeholder="字典父类型" class="form-control" name="q-txtDictFathType" id="q-txtDictFathType" value="">
            </span>
        </div>
         <div class="form-item ">
            <span class="form-field">字典子类型：</span>
            <span class="form-input">
            	<input type="text" placeholder="字典子类型" class="form-control" name="q-txtDictLeftType" id="q-txtDictLeftType" value="">
            </span>
        </div>
         <div class="form-item ">
            <span class="form-field">字典名：</span>
            <span class="form-input">
            	<input type="text" placeholder="字典名" class="form-control" name="q-txtDictName" id="q-txtDictName" value="">
            </span>
        </div>
        <div class="form-item"></div>
    </div>
    
    <div class="form-action text-right">
        <button class="btn btn-primary" type="submit"  id="queryBtn" name="queryBtn"  onclick="queryByCondtion(true)"><i class="fa fa-search"></i>&nbsp查询</button>
        <button class="btn btn-outline btn-primary" id="resetBtn" name="resetBtn" type="reset">清空</button>
    </div>
</div>

<div class="jqGrid_wrapper fullscreen-wrapper">
    <table id="dictionaryList"></table>
    <div id="dictionaryPage"></div>
</div>

<input type="hidden" name="delDictionary" id="delDictionary" value="">
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/dictionary.js"></script>
</body>

</html>
