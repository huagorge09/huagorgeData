<%@ page language="java" isELIgnored="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<html>

<head>
<!-- 这里引入导航信息header.tpl -->
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>主消息配置详情页</title>
</head>

<body class="sub-page">
	<!-- 系列产品查看模态窗 -->
	<div class="modal-content">
		<div class="modal-header">
			<h4 class="modal-title">主消息配置详情页</h4>
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
							<td>标题</td>
							<td colspan="3" class="white-bg"><span>${msgConf.category}</span>
							</td>
						</tr>
						<tr>
							<td>排序号</td>
							<td colspan="3" class="white-bg"><span>${msgConf.mcId}</span></td>
						</tr>
						<tr>
							<td>EMAIL标题</td>
							<td colspan="3" class="white-bg">
								<span>${msgConf.head}</span>
							</td>
						</tr>
						<tr>
							<td>EMAIL内容</td>
							<td colspan="3" class="white-bg">
								<div>
									<textarea cols="80" rows="10" readonly="readonly">${msgConf.body}</textarea>
								</div>
							</td>
						</tr>
						<tr>
							<td>备注</td>
							<td colspan="3" class="white-bg">
								<div>
									<textarea cols="80" rows="10" readonly="readonly">${msgConf.remark}</textarea>
								</div>
							</td>
						</tr>
						<tr>
							<td>创建时间</td>
							<td colspan="3" class="white-bg"><span>${msgConf.createDate}</span></td>
						</tr>
						<tr>
							<td>修改时间</td>
							<td colspan="3" class="white-bg"><span>${msgConf.modifiDate}</span></td>
						</tr>
						<tr>
							<td colspan="4" class="white-bg"></td>
						</tr>
						<tr>
							<td colspan="4"><strong>收件人</strong></td>
						</tr>
						<tr>
							<td>用户名称</td>
							<td colspan="3" class="white-bg"><sapn>&nbsp;&nbsp;&nbsp;&nbsp;${tosNm}</sapn></td>
						</tr>
						<tr>
							<td colspan="4" class="white-bg"></td>
						</tr>
						<tr>
							<td colspan="4"><strong>抄送人</strong></td>
						</tr>
						<tr>
							<td>用户名称</td>
							<td colspan="3" class="white-bg"><span>&nbsp;&nbsp;&nbsp;&nbsp;${ccsNm}</span>
							</td>
						</tr>
						<tr>
							<td>手动配置邮件地址</td>
							<td colspan="3" class="white-bg"><span>&nbsp;&nbsp;&nbsp;&nbsp;${ccs1}</span>
							</td>
						</tr>
						
						<tr>
							<td colspan="4"><strong>密件抄送人</strong></td>
						</tr>
						<tr>
							<td>用户名称</td>
							<td colspan="3" class="white-bg"><span>&nbsp;&nbsp;&nbsp;&nbsp;${sccsNm}</span>
							</td>
						</tr>
						
						<tr>
							<td>手动配置邮件地址</td>
							<td colspan="3" class="white-bg"><span>&nbsp;&nbsp;&nbsp;&nbsp;${sccs1}</span>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</body>