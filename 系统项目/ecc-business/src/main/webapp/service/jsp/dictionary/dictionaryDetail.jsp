<%@ page language="java" isELIgnored="false"
	contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>

<head>
<!-- 这里引入导航信息header.tpl -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>查看系统字典</title>
<%-- <script type="text/javascript" src="<%=context%>resources/modules/system/product/prepare/projectStartup.detail.js"></script>
 --%>
</head>

<body>
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">查看系统字典</h4>
		</div>
		<div class="modal-body">
			<div class="table-responsive">
				<table class="table table-bordered">
					<colgroup>
						<col width="20%">
						<col width="30%">
						<col width="20%">
						<col width="30%">
					</colgroup>
					<tbody>
						<tr>
							<td>字典根类型</td>
							<td class="white-bg"><span>${dictionaryVo.dctRootType}</span></td>
							<td>字典父类型</td>
							<td class="white-bg"><span>${dictionaryVo.dctFathType}</span>
							</td>
						</tr>
						<tr>
							<td>字典子类型</td>
							<td class="white-bg"><span>${dictionaryVo.dctLeftType}</span></td>
							<td>字典名</td>
							<td class="white-bg"><span>${dictionaryVo.dctName}</span></td>
						</tr>
						<tr>
							<td>字典值</td>
							<td class="white-bg"><span>${dictionaryVo.dctValue}</span></td>
							<td>排序号</td>
							<td class="white-bg"><span>${dictionaryVo.dctSortNo}</span>
							</td>
						</tr>
						<tr>
							<td>是否叶子</td>
							<td class="white-bg"><span>${dictionaryVo.dctIsLeafNM}</span></td>
							<td>是否生效</td>
							<td class="white-bg"><span>${dictionaryVo.dctStatNM}</span></td>
						</tr>
						<tr>
							<td>创建人名称</td>
							<td class="white-bg"><span>${dictionaryVo.createNm}</span></td>
							<td>创建时间</td>
							<td class="white-bg"><span>${dictionaryVo.createTime}</span>
							</td>
						</tr>
						<tr>
							<td>状态 </td>
							<td colspan="3" class="white-bg"><span>${dictionaryVo.statNM}</span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>
</html>