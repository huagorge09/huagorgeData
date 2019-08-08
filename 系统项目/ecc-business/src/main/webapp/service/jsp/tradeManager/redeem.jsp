<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored ="false" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@ include file="/service/jsp/wasp/ResourceGlobal.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>基金赎回</title>
<style type="text/css">
.modal-content{
	border: 0px solid #e2e2e2;
    box-shadow: 0 0px 0px rgba(0, 0, 0, 0.3);
}
.sub-page{
	background-clip: padding-box;
    background-color: #f5f8f8;
    border: 1px solid #e2e2e2;
    box-shadow: 0 0px 0px rgba(0, 0, 0, 0.3);
}
.form-control{
	width: 250px !important;
}
.select2-container{
	width: 250px !important;
}
</style>
</head>
<body class="sub-page">
	<div class="modal-content">
		<div>
	        <h4 class="modal-title">基金赎回</h4>
	    </div>
	    <div class="modal-body">
	    	<div class="table-responsive">
	    	<form id="redeemQuery" action="" name="redeemQuery"  method="post" class="form-horizontal">
	           	<table class="table table-bordered">
	    			<colgroup>
	                    <col width="20%">
	                    <col width="30%">
	                    <col width="20%">
	                    <col width="30%">
	                </colgroup>
	                <tbody>
	                	<tr>
	                		<td>委托方式：</td>
	                		<td class="white-bg form-inner">
	                			<select name="trustType" id="trustType" class="select2"></select>
	                		</td>
	                		<td>账户类型：</td>
	                		<td class="white-bg form-inner">
	                			<select name="accountType" id="accountType" class="select2" onchange="setType(this.value);">
	                				<option value="TRADEACCO">交易账号</option>
	    							<option value="FUNDACCO" selected>基金账号</option>
	                			</select>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>交易账号：</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="交易账号" class="form-control changeText" id="tradeacco" name="tradeacco" onkeyup="clearQueryFlag();">
	                		</td>
	                		<td>基金账号：</td>
	                		<td class="white-bg form-inner">
	                			<input type="text" placeholder="基金账号" class="form-control changeText" id="fundacco" name="fundacco" onkeyup="clearQueryFlag();">
	                		</td>
	                	</tr>
	                </tbody>
	                <tbody style="border: 0;">
	                	<tr>
	                		<td colspan="4" style="text-align: right;">
	                			<button type="button" class="btn btn-primary" id=doQuery name="doQuery" onclick="queryClientInfo();">查询</button>
	                		</td>
	                	</tr>
	                </tbody>
	            </table>
	           	</form>
	           	<form id="redeem" action="/capitalService/server/addOrUpdateFundInfo.xhtml" name="redeem"  method="post" class="form-horizontal">
	            <table class="table table-bordered">
	            	<input type="hidden" name="permissionId" value="8023" />
	            	<input type="hidden" name="queryflag" id="queryflag" value="N" />
					<input type="hidden" name="hidriskflag" class="hidriskflag" value="Y" /><!-- 风险匹配标志：Y,匹配；N，不匹配。默认匹配。 -->
					<input type="hidden" name="hidcontact" />
					<input type="hidden" name="hidcontidno" />
					<input type="hidden" name="hidcontidtp" />
					<input type="hidden" name="hidcontvalidate" id="hidcontvalidate"/>
					<input type="hidden" class="custno" name="custno" value="" />
					<input type="hidden" class="hidtradeacco" name="hidtradeacco" value="" />
					<input type="hidden" name="tano" id="tano"/>
					<input type="hidden" name="queryFundInfo" id="queryFundInfo"/>
					<input type="hidden" class="custname" name="custname" value=""/>
					<input type="hidden" class="hidfundacco" name="hidfundacco" value=""/>
					<input type="hidden" class="invtpNm" name="invtpNm" value=""/>
					<input type="hidden" name="fundinfoex" class="fundinfoex">
					<input type="hidden" name="invprtp" class="invprtp">
					<input type="hidden" name="custrisklevl" class="custrisklevl">
					<input type="hidden" name="available" class="available">
					<input type="hidden" name="trustType" class="trustType">
					<input type="hidden" name="hidfundtp"  id="hidfundtp" value=""/>
                    <input type="hidden" name="hidworkdate"  id="hidworkdate" value=""/>
	            	<colgroup>
	                    <col width="20%">
	                    <col width="30%">
	                    <col width="20%">
	                    <col width="30%">
	                </colgroup>
	                <tbody style="border-top: 0;">
	                	<tr>
	                		<td><font color="red">*</font>基金名称：</td>
	                		<td class="white-bg form-inner">
	                			<select name="fundid" id="fundid" class="select2" onchange="queryFundInfoData(this.value);queryDiscount();"></select>
	                		</td>
	                		<td>巨额赎回：</td>
	                		<td class="white-bg form-inner">
	                			<select name="largeflag" id="largeflag" class="select2"></select>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td><font color="red">*</font>赎回份额：</td>
	                		<td class="white-bg form-inner" colspan="3">
	                			<input type="text" placeholder="赎回份额" class="form-control" id="subAmt" name="subAmt" onKeyUp="onMoneyChange();queryDiscount();" onchange="onMoneyChange();queryDiscount();" onfocus="onMoneyChange();queryDiscount();" style="float: left;"/>
						    	<span style="line-height: 33px;">
						    		<input type="checkbox" name="allAmt" id="allAmt" style="float: left;margin: 10px 5px 0 5px;" onclick="redeemAll();">
						    		<span style="float: left;">&nbsp;全额赎回&nbsp;&nbsp;</span>
						    	</span>
						    	<span id="CapMoneyqianfenwei" style="float: left;line-height: 33px;padding: 0 15px;"></span>&nbsp;&nbsp;
						    	<span id="CapMoney" style="float: left;line-height: 33px;">&nbsp;</span>
	                		</td>
	                	</tr>
	                	
	                	<tr>
	                		<td>经办人：</td>
	                		<td class="white-bg form-inner" colspan="3">
	                			<select name="contact" id="contact" class="select2" onchange="contset(this.value);"></select>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>折扣率：</td>
	                		<td class="white-bg form-inner">
	                			<div id='discountDiv'></div>
	                		</td>
	                		<td>交易表单是否原件：</td>
	                		<td class="white-bg form-inner">
	                			<select name="istradeform" id="istradeform" class="select2">
	                				<option value="N">否</option>
	    		 					<option value="Y">是</option>
	                			</select>
	                		</td>
	                	</tr>
	                	<tr>
					    	<td colspan=4></td>
					  	</tr>
					  	<tr>
	                		<td>客户名称：</td>
	                		<td class="white-bg form-inner">
	                			<span class="invnm"></span>
	                		</td>
	                		<td>客户类型：</td>
	                		<td class="white-bg form-inner">
	                			<span class="invtp"></span>
	                		</td>
	                	</tr>
	                	<tr>
	                		<td>证件类型：</td>
	                		<td class="white-bg form-inner">
	                			<span class="idtpNm"></span>
	                		</td>
	                		<td>证件号码：</td>
	                		<td class="white-bg form-inner">
	                			<span class="idno"></span>
	                		</td>
	                	</tr>
	                	<tr id="appendContact">
	                		<td>基金名称：</td>
	                		<td class="white-bg form-inner">
	                			<span class="fundName"></span>
	                		</td>
	                		<td style="font-weight: bold;">可用份额：</td>
	                		<td class="white-bg form-inner">
	                			<span class="avaliableDis"></span>
	                		</td>
	                	</tr>
	                	
	                	<tr id="checknoTr">
							<td>主管工号：</td>
							<td class="white-bg form-inner">
								<input style="display: none;" type="text" id="checkno" name="checkno"/>
								<span class="checkno"></span>
							</td>
							<td>主管密码：</td>
							<td class="white-bg form-inner">
								<input style="display: none;" type="text" id="checkpwd" name="checkpwd"/>
								<span class="checkpwd"></span>
							</td>
						</tr>
	                </tbody>
	            </table>
				<table class="table">
                	<tbody>
						<tr>
							<td style="border: 0px;" class="form-table-td-button" colspan="4" align="center">
								<button type="button" class="btn btn-primary btn-w-xs" id="audit" name="audit" onclick="doAudit();">授权</button>
								<button type="button" class="btn btn-primary btn-save btn-loading btn-w-xs" id="subBtn" name="subBtn" onclick="checkSubmit();">提交</button>
					   		</td>
			   			</tr>
	   				</tbody>
				</table>
			</form>
		</div>
	</div>
</div>
<script type="text/javascript" src="<%=context%>web/js/tradeManager/redeem.js"></script>
<script type="text/javascript">
		var basePath = "<%=context%>";
		var primaryPath = "<%=context%>capitalService/server";
		setPath(primaryPath,basePath);
</script>
</body>
</html>