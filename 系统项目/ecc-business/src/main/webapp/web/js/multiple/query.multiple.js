var PRIMARY_PATH = "";
var BASE_PATH = "";


function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
})

/*自定义清空按钮函数*/
function resetSearchVal(){
	
	$("#appDate").val($("#hiddenAppDate").val());
	var fundacct = $("#txtFundAcct").val(null);
	$("#startEnd").val($("#hiddenStartEnd").val());
	var idno = $("#txtIdNo").val(null);
	var invnm = $("#txtInvName").val(null);
	var qway = $("#queryWay").val(null);
	var tano = $("#tano").val(null);
	var customerst = $("#customerst").val(null);
	$("#netPoint").val(null);
	$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
}

//导出函数
function exportDetail(){
	
	/*查询下拉框*/
	var selBankVal = $("#selBankName").val();
	//申请日期
	var appDate = $("#appDate").val();
	//基金账号
	var fundacct = $("#txtFundAcct").val();
	//开始日期
	var startDate =  $("#startEnd").getDateRangeValue()[0];
	//结束日期
	var endDate = $("#startEnd").getDateRangeValue()[1];
	//证件号码
	var idno = $("#txtIdNo").val();
	//姓名
	var invnm = $("#txtInvName").val();
	//交易方式
	var qway = $("#queryWay").val();
	//TA代码
	var tano = $("#tano").val();
	//客户状态
	var custst = $("#customerst").val();
	//导出按钮
	var exportForm = $("#exportForm");
	//网点代码
	var netpoint = $("#netPoint").val();
	
	switch(selBankVal){
		//账户信息
		case  '001' : 
					var url = PRIMARY_PATH + "/exportAccountInfo.xhtml?";
					var params = 
							'sp[fundacct]=' + fundacct +
							'&sp[idno]=' + idno + 
							'&sp[invnm]=' + invnm + 
							'&sp[qway]=' + qway + 
							'&sp[custst]=' + custst + 
							'&sp[netpoint]=' + netpoint 
							;
					var actionUrl = url + params;
					exportForm.prop("action",actionUrl);
					exportForm.submit();
					break;
		//基金余额
		case  '003' :
					var	url = PRIMARY_PATH + "/exportFundBalance.xhtml?";
					var params = 
							'sp[fundacct]=' + fundacct + 
							'&sp[invnm]=' + invnm +
							'&sp[qway]=' + qway +
							'&sp[tano]=' + tano + 
							'&sp[netpoint]=' + netpoint 
							;
					var actionUrl = url + params;
					exportForm.prop("action",actionUrl);
					exportForm.submit();
					break;
	}
	
}

function queryPostData(){
	//申请日期
	var appDate = $("#appDate").val();
	//基金账号
	var fundacct = $("#txtFundAcct").val();
	//开始日期
	var startDate =  $("#startEnd").getDateRangeValue()[0];
	//结束日期
	var endDate = $("#startEnd").getDateRangeValue()[1];
	//证件号码
	var idno = $("#txtIdNo").val();
	//姓名
	var invnm = $("#txtInvName").val();
	//交易方式
	var qway = $("#queryWay").val();
	//TA代码
	var tano = $("#tano").val();
	//客户状态
	var custst = $("#customerst").val();
	//网点代码
	var netpoint = $("#netPoint").val();
	
	var postData={};
	
	postData.sp={};
	postData.sp.startDate =  startDate;
	postData.sp.endDate =  endDate;
	postData.sp.appDate = appDate;
	postData.sp.idno = idno;
	postData.sp.fundacct = fundacct;
	postData.sp.qway = qway;
	postData.sp.invnm = invnm;
	postData.sp.tano = tano;
	postData.sp.custst = custst;
	postData.sp.netpoint = netpoint;
	return postData;
}

function queryByCondtion(flag){
	/*查询下拉框*/
	var selBankVal = $("#selBankName").val();
	//申请日期
	var appDate = $("#appDate").val();
	//基金账号
	var fundacct = $("#txtFundAcct").val();
	//开始日期
	var startDate =  $("#startEnd").getDateRangeValue()[0];
	//结束日期
	var endDate = $("#startEnd").getDateRangeValue()[1];
	//证件号码
	var idno = $("#txtIdNo").val();
	//姓名
	var invnm = $("#txtInvName").val();
	//交易方式
	var qway = $("#queryWay").val();
	//TA代码
	var tano = $("#tano").val();
	//客户状态
	var custst = $("#customerst").val();
	switch(selBankVal){
		//账户信息
		case '001' : 
				/*	基金账号
					证件号码
					姓名
					交易方式
					客户状态*/
					var postData = accountInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData, queryPostData());
					if (flag) {
						accountInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						accountInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//基金净值查询
		case '002' :
					/*起止日期*/
					var postData = fundClearInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData, queryPostData());
					if (flag) {
						fundClearInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						fundClearInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//基金余额查询
		case '003' :
						/*基金账号
						姓名
						交易方式
						TA代码*/
					var postData = fundLastInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData, queryPostData());
					if (flag) {
						fundLastInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						fundLastInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//定投扣款失败查询
		case '004' :
					var postData = castFailureInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData, queryPostData());
					if (flag) {
						castFailureInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						castFailureInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
	}
}


WASP_WIDGET.triggerDateRangeStyle("startEnd");
/*日期控件 单个日期选择  */
WASP_WIDGET.triggerDateStyleWithYMD("appDate");

//默认调取currentAcc
$(function() {
	$("#appDate").val($("#hiddenAppDate").val());
	$("#startEnd").val($("#hiddenStartEnd").val());
	$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
	initAccountInfoListGrid();
})

/*下拉框切换查询菜单栏*/
function Process(){
		//查询选择value值
		var selBankVal = $("#selBankName").val();
		//起止日期
		var startEndDate = $("#startEndDate");
		//查询日期
		var appDatetd = $("#appDatetd");
		//基金账号
		var fundAcctKey = $("#fundAcctKey");
		//证件号码
		var idNoKey =$("#idNoKey");
		//姓名
		var invNameKey = $("#invNameKey");
		//交易方式
		var selQueryWay = $("#selQueryWay");
		//客户状态
		var customerStatusDiv = $("#customerStatusDiv");
		//TA代码
		var tanokey = $("#tanokey");
		//查询
		var queryBtn = $("#queryBtn");
		//导出
		var exportBtn = $("#exportBtn");
		//清空
		var resetBtn = $("#resetBtn");
		//账户信息表格
		var accountInfo = $("#accountInfo");
		//基金净值表格
		var fundClear = $("#fundClear");
		//定投扣款失败表格
		var castFailure = $("#castFailure");
		//基金余额
		var fundLast = $("#fundLast");
		//网点代码
		var netpiontInfo = $("#netpiontInfo");

		switch(selBankVal){
			case '001':/*
					账户信息查询菜单显示内容
						查询 
						导出
						基金账号
						证件号码
						姓名
						交易方式
						客户状态
			*/		   
						startEndDate.css("display","none");
						appDatetd.css("display","none");
						fundAcctKey.css("display","block");
						idNoKey.css("display","block");
						invNameKey.css("display","block");
						customerStatusDiv.css("display","block");
						selQueryWay.css("display","block");
						tanokey.css("display","none");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","inline-block");
						resetBtn.css("display","inline-block");
						accountInfo.css("display","block");
						fundClear.css("display","none");
						fundLast.css("display","none");
						castFailure.css("display","none");
						netpiontInfo.css("display","block");
						$(function() {
							initAccountInfoListGrid();
						})
						break;
			case '002':/*
						
						基金净值查询菜单显示内容
						查询
						起止日期
							*/
						startEndDate.css("display","block");
						appDatetd.css("display","none");
						fundAcctKey.css("display","none");
						idNoKey.css("display","none");
						customerStatusDiv.css("display","none");
						invNameKey.css("display","none");
						selQueryWay.css("display","none");
						tanokey.css("display","none");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","none");
						resetBtn.css("display","inline-block");
						accountInfo.css("display","none");
						fundClear.css("display","block");
						fundLast.css("display","none");
						castFailure.css("display","none");
						netpiontInfo.css("display","none");
						$(function() {
							initFundClearInfoListGrid();
						})
		               break;
			case '003':/*
						基金余额查询菜单显示内容
						查询、导出
						基金账号
						姓名
						交易方式
						TA代码
					*/
						startEndDate.css("display","none");
						appDatetd.css("display","none");
						fundAcctKey.css("display","block");
						idNoKey.css("display","none");
						invNameKey.css("display","block");
						selQueryWay.css("display","block");
						tanokey.css("display","block");
						customerStatusDiv.css("display","none");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","inline-block");
						resetBtn.css("display","inline-block");
						accountInfo.css("display","none");
						fundClear.css("display","none");
						fundLast.css("display","block");
						castFailure.css("display","none");
						netpiontInfo.css("display","block");
						$(function() {
							initFundLastInfoListGrid();
						})
						break;
			case '004':/* 
						定投扣款失败查询菜单显示内容
						查询
						查询日期
						*/
						startEndDate.css("display","none");
						appDatetd.css("display","block");
						fundAcctKey.css("display","none");
						idNoKey.css("display","none");
						invNameKey.css("display","none");
						selQueryWay.css("display","none");
						tanokey.css("display","none");
						customerStatusDiv.css("display","none");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","none");
						resetBtn.css("display","inline-block");
						accountInfo.css("display","none");
						fundClear.css("display","none");
						fundLast.css("display","none");
						castFailure.css("display","block");
						netpiontInfo.css("display","none");
						$(function() {
							initCastFailureInfoListGrid();
						})
						break;
			
		}
	}
