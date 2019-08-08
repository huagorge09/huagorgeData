<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<% 
	String operatorId = SessionUtils.getEmployee().getID();
%>

<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
#pageTitle{
	text-align:center;
	margin-bottom : 20px;
}
#brokerModifyForm tbody label{
	font-weight: normal;
}
</style>
<title>修改客户评估数据</title>
</head>
<body class="fixed-nav gray-bg">
	<div class="modal-content">
		<div align="center">
		<h4 class="modal-title" id="pageTitle">客户风险等级管理</h4>
		<form name="brokerModifyForm" id ="brokerModifyForm" class="form-horizontal" action="" method="post">
			<input type="hidden" id="hidregioncode" name="hidregioncode" value="${riskLevelDto.regioncode}" /> 
			<input type="hidden" id="risklevel" name="risklevel" value="${riskLevelDto.risklevel}" /> 
			<input type="hidden" id="fileno" name="fileno" value="${riskLevelDto.voicerecord }" /> 
			<input type="hidden" id="hidinvtp" name="hidinvtp" value="${riskLevelDto.invtp}" /> 
			<input type="hidden" id="evalScope" name="evalScope"  />
			<input type="hidden" id="evalAnswer" name="evalAnswer"  /> 
			<input type="hidden" id="dtoAnswer" name="dtoAnswer" value="${riskLevelDto.answer}"  /> 
			<input type="hidden" id="specriskLevel" name="specriskLevel"  />
			<input type="hidden" id="hidrisklevel" name="hidrisklevel"  /> 
			<input  type="hidden" id="invprtp" name="invprtp" value="${riskLevelDto.invprtp}" /> 
			<input type="hidden" id="operatorId" name="operatorId" value="<%=operatorId%>" />
			<input type="hidden" id="hidappst" name="hidappst" value="${riskLevelDto.appst}" /> 
			<input type="hidden" id="hidapptp" name="hidapptp" value="${riskLevelDto.apptp}" />
			<input type="hidden" id="hidcustno" name="hidcustno" value="${riskLevelDto.custno}" />
			
			<table class="table table-bordered">
				<colgroup><col width="10%"><col width="20%"><col width="25%"><col width="20%"><col width="25%"></colgroup>
				<tbody>
				<tr>
					<th rowspan="3" style="text-align:center;">基本信息</th>
					<td width="20%">客户名称：</td>
					<td width="30%" >${riskLevelDto.invnm}</td>
					<td width="20%" >基金账号：</td>
					<td width="30%">${riskLevelDto.fundacc}</td>
				</tr>	
				<tr>
					<td width="20%" class="inputTd">客户类型：</td>
					<td width="30%">${riskLevelDto.invtpName}</td>
				<td width="20%" id="riskTd">风险承受能力：</td>
			    <td width="30%" id="riskSelect">
			  		${riskLevelDto.risklevelName}
			    </td>
				</tr>
				<tr>
					<td width="20%">客户专业类型：</td>
					<td width="30%" class="white-bg">
					<select style="WIDTH: 150px" name="hidinvprtp" id="hidinvprtp" class="form-control use-select2">
							<option value="1" <c:if test="${riskLevelDto.invprtp eq '1'}">selected</c:if> >普通投资者</option>
							<option value="0" <c:if test="${riskLevelDto.invprtp eq '0'}">selected</c:if>>专业投资者</option> 
					</select></td>
					<td id="fileTd" WIDTH="20%" <%-- style="<%=%>"  --%>>录音文件编号：<br></td>
					<td id="fileTdx" <%-- style="<%=%>" --%> class="white-bg"><input type="text" name="hidfileno"
						id="hidfileno"  value="${riskLevelDto.voicerecord}" class="form-control"/></td>
				</tr>
				</tbody>
				<tbody id="personUser">
					<tr>
						<th rowspan="15" WIDTH="4%" align="center" style="text-align:center;">个人投资者<br />风险调查问卷</th>
						<td WIDTH="20%">1、您目前的主要收入来源是：</td>
						<td COLSPAN="3" class="white-bg"><label><input type="radio" value="2" id="pq1_1"
							name="pq1"> 无固定收入（2分）</label><br/><label><input type="radio" value="4"
							id="pq1_2" name="pq1"> 退休金（4分）</label><br/><label><input type="radio"
							value="6" id="pq1_3" name="pq1">工资、劳务报酬（6分）</label><br/><label><input
							type="radio" value="8" id="pq1_4" name="pq1">
							个体经营或财产收入（房租、股权收入等）（8分）</label><br/><label><input type="radio" value="10" id="pq1_5"
							name="pq1"> 企业经营（10分）</label><br></td>
					</tr>
					<tr>
						<td WIDTH="20%" >2、您的可支配资产是多少（不包含不动产）（折合人民币）：</td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="2" id="pq2_1" name="pq2">100万元以下（2分）</label> <br/>
							<label><input type="radio" value="4" id="pq2_2" name="pq2">100万元至200万元（4分） </label><br/>
							<label><input type="radio" value="6" id="pq2_3" name="pq2">200万元至500万元（6分）</label><br> 
							<label><input type="radio" value="8" id="pq2_4" name="pq2">500万元至1000万元（8分）</label> <br/>
							<label><input type="radio" value="10" id="pq2_5" name="pq2">1000万元以上（10分）</label></td>
					</tr>
	
					<tr>
						<td WIDTH="20%" >3、在您每年的家庭可支配收入中，可用于金融投资（储蓄存款除外）的比例为：</td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="2" id="pq3_1" name="pq3">小于10%（2分）</label> <br/>
							<label><input type="radio" value="4" id="pq3_2" name="pq3">10%至25%（4分）</label> <br> 
							<label><input type="radio" value="6" id="pq3_3" name="pq3">25%至50%（6分）</label> <br> 
							<label><input type="radio" value="8" id="pq3_4" name="pq3">50%至80%（8分） </label> <br/>
							<label><input type="radio" value="10" id="pq3_5" name="pq3">80%以上（10分）</label> <br>
						</td>
					</tr>
					<tr>
						<td WIDTH="20%" >4、 您的资产负债情况属于以下哪一种：
						</td>
						<td COLSPAN="3" class="white-bg"><label><input type="radio" value="2" id="pq4_1"
							name="pq4">有较大数额未到期负债（2分）</label><br><label> <input type="radio" value="4"
							id="pq4_2" name="pq4"> 收入和支出相抵（4分）</label><br /><label> <input type="radio"
							value="6" id="pq4_3" name="pq4"> 有一定积蓄（6分）</label><br>
							<label><input type="radio" value="8" id="pq4_4" name="pq4">
							有较为丰厚的积蓄并有一定的投资（8分）</label><br><label> <input type="radio" value="10" id="pq4_5"
							name="pq4"> 比较富裕且有相当的投资（10分）</label></td>
					</tr>
					<tr>
						<td WIDTH="20%">5、 您的投资知识可描述为：</td>
						<td  COLSPAN="3" class="white-bg"><label><input type="radio" value="2" id="pq5_1" name="pq5">
							无：没有金融产品方面的知识（2分）</label> <br/><label><input type="radio" value="4" id="pq5_2"
							name="pq5"> 有限：理解部分金融产品及其相关风险的概念，但十分有限（4分）</label> <br/><label><input
							type="radio" value="6" id="pq5_3" name="pq5">
							一般：对金融产品及其相关风险具有基本的知识和理解（6分）</label> <br/><label><input type="radio" value="8"
							id="pq5_4" name="pq5"> 丰富：对金融产品及其相关风险具有丰富的知识和理解（8分） </label><br/><label><input
							type="radio" value="10" id="pq5_5" name="pq5">
							专业：对金融产品及其相关风险具有专业的知识和深入的理解（10分）</label></td>
					</tr>
	
					<tr>
						<td WIDTH="20%">6、 您的投资经验可描述为：</td>
						<td  COLSPAN="3" class="white-bg"><label><input type="radio" value="2" id="pq6_1" name="pq6">
							基本无投资经验（2分）</label> <br/><label><input type="radio" value="4" id="pq6_2"
							name="pq6"> 大部分投资于存款、国债等（4分）</label> <br/><label><input type="radio"
							value="6" id="pq6_3" name="pq6"> 大部分投资于银行理财产品、保险理财产品等（6分） </label><br/><label><input
							type="radio" value="8" id="pq6_4" name="pq6">
							大部分投资于基金、股票等（8分）</label> <br/><label><input type="radio" value="10" id="pq6_5"
							name="pq6"> 大部分投资于创业板股票、外汇、期货等高风险产品（10分）</label></td>
					</tr>
	
	
	
					<tr>
						<td WIDTH="20%">7、
							您有多少年投资基金、股票、信托、私募证券或金融衍生产品等风险投资品的经验：</td>
						<td  COLSPAN="3" class="white-bg"><label><input type="radio" value="2" id="pq7_1" name="pq7">
							1年以下（2分）</label> <br/><label><input type="radio" value="4" id="pq7_2" name="pq7">
							1至2年（4分）</label> <br/><label><input type="radio" value="6" id="pq7_3" name="pq7">
							2至5年（6分） </label><br/><label><input type="radio" value="8" id="pq7_4" name="pq7">
							5至10年（8分）</label> <br/><label><input type="radio" value="10" id="pq7_5" name="pq7">
							10年以上（10分）</label></td>
					</tr>
	
	
	
	
					<tr>
						<td WIDTH="20%">8、 您计划中的投资期限是多长：</td>
						<td  COLSPAN="3" class="white-bg"><label><input type="radio" value="2" id="pq8_1" name="pq8">
							少于1年（2分）</label> <br/><label><input type="radio" value="4" id="pq8_2" name="pq8">
							1-3年（4分）</label><br/><label><input type="radio" value="6" id="pq8_3" name="pq8">
							3-5年（6分）</label> <br/><label><input type="radio" value="8" id="pq8_4" name="pq8">
							5-8年（8分）</label> <br/><label><input type="radio" value="10" id="pq8_5" name="pq8">
							8年以上（10分）</label></td>
					</tr>
	
	
	
	
					<tr>
						<td WIDTH="20%">9、 您打算重点投资于哪些种类的投资品种：</td>
						<td  COLSPAN="3" class="white-bg"><label><input type="radio" value="2" id="pq9_1" name="pq9">
							短债、货币市场基金等主要投资于货币工具的基金（2分）</label> <br/><label><input type="radio" value="4" id="pq9_2"
							name="pq9"> 债券型基金等主要投资于固定收益类资产的基金（4分）  </label><br/><label><input
							type="radio" value="6" id="pq9_3" name="pq9">
							混合型基金等投资于权益类与固定收益类资产的基金（6分）</label> <br/><label><input type="radio" value="8"
							id="pq9_4" name="pq9"> 股票型基金等主要投资于权益类资产的基金（8分）</label> <br/><label><input
							type="radio" value="10" id="pq9_5" name="pq9">
							分级基金等其它复杂或高风险基金（10分）</label></td>
					</tr>
	
	
	
	
					<tr>
						<td WIDTH="20%">10、 以下哪项描述最符合您的投资态度：</td>
						<td  COLSPAN="3" class="white-bg">
							<label><input type="radio" value="2" id="pq10_1" name="pq10">厌恶风险，能够承担极小的风险（2分）</label><br/>
							<label><input type="radio" value="4" id="pq10_2" name="pq10">虽然厌恶风险但愿意承担一些风险（4分）</label><br/>
							<label><input type="radio" value="6" id="pq10_3" name="pq10">在深思熟虑后愿意承担一定的风险（6分） </label><br/>
							<label><input type="radio" value="8" id="pq10_4" name="pq10">敢冒风险，比较激进（8分）</label><br/>
							<label><input type="radio" value="10" id="pq10_5" name="pq10">爱好风险，相当激进（10分）</label></td>
					</tr>
					<tr>
						<td WIDTH="20%">11.以下几种投资模式，您更偏好哪种模式：</td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="2" id="pq11_1" name="pq11">收益只有5%，但可能亏损不超过5%（2分）</label> <br/>
							<label><input type="radio" value="4" id="pq11_2" name="pq11">收益15%，但可能亏损5%（4分） </label><br/>
							<label><input type="radio" value="6" id="pq11_3" name="pq11">收益是30%，但可能亏损15%（6分）</label> <br/>
							<label><input type="radio" value="8" id="pq11_4" name="pq11">收益50%，但可能亏损30%（8分）</label> <br/>
							<label><input type="radio" value="10" id="pq11_5" name="pq11">收益100%，但可能亏损60%（10分）</label>
						</td>
					</tr>
					<tr>
						<td WIDTH="20%">12、 您认为自己能承受的最大投资损失是多少： 
						</td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="2" id="pq12_1" name="pq12">5%以内（2分）</label> <br/>
							<label><input type="radio" value="4" id="pq12_2" name="pq12">5%-10%（4分）</label> <br/>
							<label><input type="radio" value="6" id="pq12_3" name="pq12">10%-30%（6分）</label> <br/>
							<label><input type="radio" value="8" id="pq12_4" name="pq12">30%-50%（8分） </label><br/>
							<label><input type="radio" value="10" id="pq12_5" name="pq12">超过50%（10分）</label>
						</td>
					</tr>
					<tr style="color: red;">
						<td WIDTH="20%">温馨提示：</td>
						<td  COLSPAN="3">以下问题适用于判定您是否属于最低风险承受能力的投资者，请您仔细阅读并勾选。 </td>
					</tr>
					<tr>
						<td WIDTH="20%">13、 请问您是否具有完全民事行为能力？</td>
						<td  COLSPAN="3" class="white-bg">
							<label><input type="radio" value="0" id="pq13_1" name="pq13" data-value="N">是</label> <br/>
							<label><input type="radio" value="0" id="pq13_2" name="pq13" data-value="Y" >否</label><br/>
						</td>
					</tr>
					<tr>
						<td WIDTH="20%">14、 请问您是否没有风险容忍度或者不愿承受任何投资损失？</td>
						<td  COLSPAN="3" class="white-bg">
							<label><input type="radio" value="0" id="pq14_1" name="pq14" data-value="Y">是 </label><br/>
							<label><input type="radio" value="0" id="pq14_2" name="pq14" data-value="N">否</label><br/>
						</td>
					</tr>
				</tbody>
				<tbody id="orgUser" >
					<tr>
						<th rowspan=14 WIDTH="4%" align="center" >机构投资者<br />风险调查问卷</th>
						<td WIDTH="20%" >1、贵机构的主要投资资金来源是？</td>
						<td COLSPAN="3" class="white-bg">
						<label><input type="radio" value="5" id="oq1_1" name="oq1"> 
						 其他大型机构投资者</label><br/>
						<label><input type="radio" value="4" id="oq1_2" name="oq1"> 
						 其他小型机构投资者   </label><br/>
						<label><input type="radio" value="3" id="oq1_3" name="oq1"> 
						 个人投资者  </label> <br/>
						<label><input  type="radio" value="2" id="oq1_4" name="oq1">
						 自有资金</label> <br/>
						<label><input type="radio" value="1" id="oq1_5"  name="oq1"> 
						 银行借款  </label>
						</td>
					</tr>
	
					<tr>
						<td WIDTH="20%" >2、贵机构的净资产规模为：</td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="5" id="oq2_1" name="oq2"> 3亿以上</label> <br/>
							<label><input type="radio" value="4" id="oq2_2" name="oq2"> 1亿（含）-3亿</label> <br/>
							<label><input type="radio" value="3" id="oq2_3" name="oq2"> 3000万元（含）-1亿元 </label> <br/>
							<label><input type="radio" value="2" id="oq2_4" name="oq2"> 2000万元（含）-3000万元</label>  <br/>
							<label><input type="radio"  value="1" id="oq2_5" name="oq2"> 1000万元（含）-2000万元 </label> 
						</td>
					</tr>
					<tr>
						<td WIDTH="20%">3、贵机构用于证券投资的大部分资金不会用作其它用途的时间段为：</td>
						<td COLSPAN="3" class="white-bg">
						<label><input type="radio" value="5" id="oq3_1" name="oq3"> 
						长期——7年以上</label> <br/>
						<label><input type="radio" value="4" id="oq3_2" name="oq3"> 
						中长期——5到7年</label> <br/>
						<label><input type="radio" value="3" id="oq3_3" name="oq3">
						中期——3到5年</label><br/>
						<label><input type="radio" value="2" id="oq3_4" name="oq3"> 
						中短期——1-3年  </label><br/>
						<label><input type="radio"  value="1" id="oq3_5" name="oq3"> 
						短期——0到1年  </label>
						</td>
					</tr>
					<tr>
						<td WIDTH="20%" >
						4、 贵机构账户近期金融资产持有总规模是多大？</td>
						<td COLSPAN="3" class="white-bg">
						<label><input type="radio" value="5" id="oq4_1" name="oq4"> 
						大于1 亿元</label>  <br/>
						<label><input type="radio" value="4" id="oq4_2" name="oq4"> 
						3000 万元到1亿元 </label> <br/>
						<label><input type="radio" value="3" id="oq4_3" name="oq4">
						500 万元到3000 万元</label>  <br/>
						<label><input type="radio" value="2" id="oq4_4" name="oq4">    
						300 -500万元以下</label>  <br/>
						<label><input type="radio"  value="1" id="oq4_5" name="oq4">   
						300 万元以下</label> <br/>
						<span style="font-color:red;">注：金融资产是指银行存款、股票、债券、基金份额、资管计划、银行理财、信托计划、保险产品、期货及其他衍生品</span>
						</td>
					</tr>
					<tr>
						<td WIDTH="20%">
						5、贵机构是否有尚未清偿的数额较大的债务？如有，主要是：</td>
						<td COLSPAN="3" class="white-bg">
						<label><input type="radio" value="5" id="oq5_1" name="oq5"> 
						没有数额较大的债务</label> <br/>
						<label><input type="radio" value="4" id="oq5_2" name="oq5"> 
						民间借贷</label>
						 <br/>
						<label><input type="radio" value="3" id="oq5_3" name="oq5">
						通过担保公司等中介机构募集的借款</label>
						 <br/>
						<label><input type="radio" value="2" id="oq5_4" name="oq5">    
						公司债券或企业债券</label><br/>
						<label><input type="radio"  value="1" id="oq5_5" name="oq5">   
						银行贷款</label>
						</td>
					</tr>
					<tr>
						<td WIDTH="20%">6、贵机构的投资经验可以被概括为：</td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="5" id="oq6_1" name="oq6"> 极富：本单位对于投资极富经验，参与过复杂金融产品或其他产品的交易</label><br/>
							<label><input type="radio" value="4" id="oq6_2" name="oq6"> 深入：本单位对于投资非常有经验，参与过权证、期货或创业板等高风险产品的交易</label><br/>
							<label><input type="radio" value="3" id="oq6_3" name="oq6"> 丰富：本单位具有相当投资经验，参与过股票、基金等产品的交易，并倾向于自己做出投资决策</label><br/>
							<label><input type="radio" value="2" id="oq6_4" name="oq6"> 一般：除银行活期账户和定期存款外，购买过基金、保险等理财产品，但还需要进一步的指导 </label><br/>
							<label><input type="radio"  value="1" id="oq6_5" name="oq6"> 有限：除银行活期账户和定期存款外，基本没有其他投资经验</label>
						</td>
					</tr>
					<tr>
						<td WIDTH="20%">7、贵机构有多少年的证券投资经验？ </td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="5" id="oq7_1" name="oq7"> 15 年以上</label><br/>
							<label><input type="radio" value="4" id="oq7_2" name="oq7"> 10 到15 年</label> <br/>
							<label><input type="radio" value="3" id="oq7_3" name="oq7"> 5 到10 年 </label> <br/>
							<label><input type="radio" value="2" id="oq7_4" name="oq7"> 1 到5 年</label> <br/>
							<label><input type="radio"  value="1" id="oq7_5" name="oq7"> 1 年以内</label>
						</td>
					</tr>
					<tr>
						<td WIDTH="20%">8、贵机构的投资研究团队人员的平均从业经验是多少年？</td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="5" id="oq8_1" name="oq8"> 10 年以上</label><br/>
							<label><input type="radio" value="4" id="oq8_2" name="oq8"> 5 到10 年</label> <br/>
							<label><input type="radio" value="3" id="oq8_3" name="oq8"> 3 到5 年</label><br/>
							<label><input type="radio" value="2" id="oq8_4" name="oq8"> 1 到3 年 </label><br/>
							<label><input type="radio"  value="1" id="oq8_5" name="oq8"> 1 年以下</label>
						</td>
					</tr>
					<tr>
						<td WIDTH="20%">9、 贵机构的投资目标是什么？</td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="5" id="oq9_1" name="oq9"> 实现资产大幅增长，愿意承担很大的投资风险</label><br/>
							<label><input type="radio" value="4" id="oq9_2" name="oq9"> 产生较多的收益，可以承担一定的投资风险</label> <br/>
							<label><input type="radio" value="3" id="oq9_3" name="oq9"> 产生稳定中等的收益，可以承担小幅度的投资风险 </label><br/>
							<label><input type="radio" value="2" id="oq9_4" name="oq9"> 尽可能保证本金安全，不在乎收益率比较低</label> <br/>
							<label><input type="radio"  value="1" id="oq9_5" name="oq9"> 资产保值，我不愿意承担任何投资风险</label>
						</td>
					</tr>
					<tr>
						<td WIDTH="20%">10、贵机构计划的投资期限是多久？</td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="5" id="oq10_1" name="oq10"> 10年以上</label><br/>
							<label><input type="radio" value="4" id="oq10_2" name="oq10"> 5-10 年 </label><br/>
							<label><input type="radio" value="3" id="oq10_3" name="oq10"> 3-5 年</label><br/>
							<label><input type="radio" value="2" id="oq10_4" name="oq10"> 1-3 年</label><br/>
							<label><input type="radio"  value="1" id="oq10_5" name="oq10"> 1年以下</label>
						</td>
					</tr>
					
					
					<tr>
						<td WIDTH="20%">11、贵机构打算重点投资于哪个种类的投资品种？</td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="5" id="oq11_1" name="oq11"> 复杂金融产品</label> <br/>
							<label><input type="radio" value="4" id="oq11_2" name="oq11"> 期货、融资融券 </label><br/>
							<label><input type="radio" value="3" id="oq11_3" name="oq11"> 股票、混合型基金、偏股型基金、股票型基金等权益类投资品种 </label><br/>
							<label><input type="radio" value="2" id="oq11_4" name="oq11"> 债券、债券基金等固定收益类投资品种 </label><br/>
							<label><input type="radio"  value="1" id="oq11_5" name="oq11"> 银行存款、现金管理类基金等高流动性低风险的投资品种</label>
						</td>
					</tr>
					<tr>
						<td WIDTH="20%">12、 下面哪一种描述最符合贵机构对于金融产品投资表现的态度？</td>
						<td COLSPAN="3" class="white-bg">
							<label><input type="radio" value="5" id="oq12_1" name="oq12"> 若因市场原因，投资期内任何产品出现任何程度的亏损，我们都不会介意</label> <br/>
							<label><input type="radio" value="4" id="oq12_2" name="oq12"> 我们可以容忍亏损，但是如果亏损幅度超过70%，我们会比较介意</label> <br/>
							<label><input type="radio" value="3" id="oq12_3" name="oq12"> 若亏损幅度高于50%，我们会感到担心 </label> <br/>
							<label><input type="radio" value="2" id="oq12_4" name="oq12"> 我们只能容忍少于30%的短期亏损 </label><br/>
							<label><input type="radio"  value="1" id="oq12_5" name="oq12"> 我们难以忍受任何亏损</label>
						</td>
					</tr>
	
					<tr>
						<td WIDTH="20%">
						13、如果贵机构的一笔投资在6 至9 个月内市值下降了20%，而研究团队的研究表明投资被低估，但不确定何时能回归应有价值，贵机构会如何处理这笔投资？ </td>
						<td COLSPAN="3" class="white-bg">
						<label><input type="radio" value="5" id="oq13_1" name="oq13"> 
						 购买更多的同类资产</label> <br/>
						<label><input type="radio" value="4" id="oq13_2" name="oq13"> 
						保留现有资产不动 </label> <br/>
						<label><input type="radio" value="3" id="oq13_3" name="oq13">
						卖掉少量该类资产 </label>  <br/>
						<label><input type="radio" value="2" id="oq13_4" name="oq13">    
						卖掉大部分该类资产</label> <br/>
						<label><input type="radio"  value="1" id="oq13_5" name="oq13">   
						全部卖掉该类资产</label>
						</td>
					</tr>
					
					<tr>
						<td WIDTH="20%">
						14、假设有两种不同的投资：投资A预期获得5%的收益，有可能承担非常小的损失；投资B预期获得20%的收益，但有可能面临25%甚至更高的亏损。您将您的投资资产分配为：</td>
						<td COLSPAN="3" class="white-bg">
						<label><input type="radio" value="5" id="oq14_1" name="oq14"> 
						全部投资于B </label> <br/>
						<label><input type="radio" value="4" id="oq14_2" name="oq14"> 
						大部分投资于B</label> <br/>
						<label><input type="radio" value="3" id="oq14_3" name="oq14">
						两种投资各一半</label>  <br/>
						<label><input type="radio" value="2" id="oq14_4" name="oq14">    
						大部分投资于A </label><br/>
						<label><input type="radio"  value="1" id="oq14_5" name="oq14">   
						全部投资于A</label>
						</td>
					</tr>
				</tbody>
		
		
				</table>
				<div></div>
				<br>
				<table  align="center" border="0" cellpadding="0"
					cellspacing="0">
					<tr>
						<td align="center">
						<button  class="btn btn-primary btn-save" type="button"
							name="dopass" id="dopass" onclick="doSubmit()" >提交</button>&nbsp;&nbsp; <button
							 type="button" class="btn btn-outline btn-primary" name="cancel" id="riskResetButton"
							 >清空</button>&nbsp;&nbsp;<button type="button" 
							 class="btn btn-link btn-w-xs" 
							 id="btnClear" name="btnClear" 
							 onclick="window.close();">取消</button>
							</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
<script type="text/javascript" src="<%=context%>web/js/common/common.js?20180605"></script>
<script type="text/javascript" src="<%=context%>web/js/customerDataManager/customerDataManager.update.js?20180716"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>service/customerDataManager";
		setPath(primaryPath,basePath);
</script>
</body>
</html>