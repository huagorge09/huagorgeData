<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
</head>
<body class="sub-page">
 <div class="modal-content">
 <input type="hidden" name="menuId"  id="menuId" value="${menuVo.menuId}"/> 
    <div class="modal-header">
        <h4 class="modal-title">查看菜单基本信息</h4>
    </div>
    <div class="modal-body">
        <div class="table-responsive">
            <table class="table table-bordered">
                <colgroup>
                    <col width="50%">
                    <col width="50%">
                </colgroup>
                <tbody>
                    <tr>
                        <td>菜单名称</td>
                        <td  class="white-bg">
                          <span>${menuVo.name}</span>
                        </td>
                    </tr>
                    <tr>
                        <td>菜单链接</td>
                        <td  class="white-bg">
                          <span>${menuVo.url}</span>
                        </td>
                    </tr>
                    <tr>
                        <td>菜单图标</td>
                        <td  class="white-bg">
                          <span>${menuVo.icon}</span>
                        </td>
                    </tr>
                    <tr>
                        <td>菜单序号</td>
                        <td  class="white-bg">
                          <span>${menuVo.sequence}</span>
                        </td>
                    </tr>
                     <tr>
                        <td>菜单类型</td>
                        <td  class="white-bg">
                          <span>
                            <c:choose>
								<c:when test="${menuVo.type=='0'}">普通菜单</c:when>
								<c:otherwise>普通按钮</c:otherwise>
							</c:choose>
						  </span>
                        </td>
                    </tr>
                    
                    <tr>
                        <td>所属系统</td>
                        <td class="white-bg">
                          <span>
                            <c:choose>
								<c:when test="${menuVo.systemClassify == 'BUSINESS'}">直销柜台(business)</c:when>
								<c:otherwise>清算平台(capital)</c:otherwise>
							</c:choose>
						  </span>
                        </td>
                    </tr>
                    
                    <tr>
                        <td>父菜单</td>
                        <td  class="white-bg">
                          <span>
                            <c:if test="${parentVo !=null}">
								${parentVo.name}
							</c:if>
						  </span>
                        </td>
                    </tr>
                    
                   <!--  <tr>
                        <td colspan="2"><strong>按钮列表</strong></td>
                    </tr>
                    <tr>
                        <td colspan="2" class="white-bg">
                            <div class="jqGrid_wrapper">
                                <table id="buttonList"></table>
                            </div>
                        </td>
                    </tr> -->
                    
                </tbody>
            </table>
        </div>
    </div>
</div>
<script type="text/javascript" src="<%=context%>web/js/common/widget.js"></script>
<script type="text/javascript" src="<%=context%>web/js/sys/menu/menu.detail.js"></script>
</body>