var PRIMARY_PATH = "";
var BASE_PATH = "";


function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
	$("#apkindack").hide();
	$("#apkindack").select2('destroy');
	$("#appDate").val($("#hiddenAppDate").val());
	$("#startEnd").val($("#hiddenStartEnd").val());
	apkindAppend();
})

/*业务类型下拉框数据*/
function apkindAppend(){
	var apkind = $("#apkindapp");
	$.ajax({
		url: PRIMARY_PATH + "/queryApkindParameter.xhtml",
		type:"POST",
		data: 
		{
			'sp[pmst]' : 'SYSTEM',
			'sp[pmky]' : 'APKIND', 
			'sp[pmv2]' : 'A',
		},
		success: function(data){
			for(var i = 0; i < data.length; i++){
				var html = "<option value =" + data[i].pmco + ">" + data[i].pmco +"  " + data[i].pmnm  + "</option>";
				apkind.append(html);
			}
		},
		dataType: 'json'
		}
	)
}


/*自定义清空按钮函数*/
function resetSearchVal(){
	$("#appDate").val($("#hiddenAppDate").val());
	$("#startEnd").val($("#hiddenStartEnd").val());	
	$("#txtFundAcct").val(null);
	$("#txtIdNo").val(null);
	$("#txtInvName").val(null);
	$("#queryWay").val(null);
	$("#apkindapp").val(null);
	$("#apkindack").val(null);
	$("#tano").val(null);
	$("#netPoint").val(null);
	var selBankVal = $("#selBankName").val();
	switch(selBankVal){
		case '001' :
					$("#apkindapp").select2({allowClear: false,minimumResultsForSearch:Infinity});
					break;
		case '002' :
					$("#apkindack").select2({allowClear: false,minimumResultsForSearch:Infinity});
					break;
		case '003' : 
					$("#apkindack").select2({allowClear: false,minimumResultsForSearch:Infinity});
					break;
		case '004' : 
					$("#apkindapp").select2({allowClear: false,minimumResultsForSearch:Infinity});
					break;
		case '005' : 
					$("#apkindack").select2({allowClear: false,minimumResultsForSearch:Infinity});
					break;
	}
	$("#queryWay").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#tano").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#netPoint").select2({allowClear: false,minimumResultsForSearch:Infinity});
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
	//业务类型
	var apkind = $("#apkindapp").val();
	//业务类型
	var apkindack = $("#apkindack").val();
	//TA代码
	var tano = $("#tano").val();
	//网点代码
	var netpoint = $("#netPoint").val();
	
	var postData={};
	
	postData.sp={};
	postData.sp.startDate =  startDate;
	postData.sp.endDate =  endDate;
	postData.sp.appDate = appDate;
	postData.sp.fundacct = fundacct;
	postData.sp.idno = idno;
	postData.sp.invnm = invnm;
	postData.sp.qway = qway;
	postData.sp.apkind = apkind;
	postData.sp.apkindack = apkindack;
	postData.sp.tano = tano;
	postData.sp.netpoint = netpoint;
	return postData;
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
	//业务类型
	var apkind = $("#apkindapp").val();
	//业务类型
	var apkindack = $("#apkindack").val();
	//TA代码
	var tano = $("#tano").val();
	//网点代码
	var netpoint = $("#netPoint").val();
	
	//导出按钮
	var exportForm = $("#exportForm");
	
	switch(selBankVal){
		//当前账户
		case  '001' : 
					var url = PRIMARY_PATH + "/exportAcctAppToday.xhtml?";
					var params = 
							'sp[appDate]=' + appDate +
							'&sp[fundacct]=' + fundacct + 
							'&sp[idno]=' + idno +
							'&sp[invnm]=' + invnm + 
							'&sp[qway]=' + qway + 
							'&sp[apkind]=' + apkind +
							'&sp[tano]=' + tano + 
							'&sp[netpoint]=' + netpoint;
					var actionUrl = url + params;
					exportForm.prop("action",actionUrl);
					exportForm.submit();
					break;
		//历史账户
		case  '004' :
					var	url = PRIMARY_PATH + "/exportAcctAppHistory.xhtml?";
					var params = 
							'sp[fundacct]=' + fundacct + 
							'&sp[idno]=' + idno +
							'&sp[invnm]=' + invnm +
							'&sp[qway]=' + qway +
							'&sp[apkind]=' + apkind +
							'&sp[tano]=' + tano +
							'&sp[startDate]='+ startDate +
							'&sp[endDate]=' + endDate +
							'&sp[netpoint]=' + netpoint;
					var actionUrl = url + params;
					exportForm.prop("action",actionUrl);
					exportForm.submit();
					break;
		//历史二级清算账户
		case  '005' :
					var	url = PRIMARY_PATH + "/exportAcctClearHistory.xhtml?";
					var params =
							'sp[fundacct]=' + fundacct +
							'&sp[idno]=' + idno +
							'&sp[invnm]=' + invnm +
							'&sp[qway]=' + qway +
							'&sp[tano]=' + tano +
							'&sp[startDate]='+ startDate +
							'&sp[endDate]='+ endDate +
							'&sp[apkindack]='+ apkindack +
							'&sp[netpoint]='+ netpoint
							;
					var actionUrl = url + params;
					exportForm.prop("action",actionUrl);
					exportForm.submit();
					break;
	}
	
}

function queryByCondtion(flag){
	var selBankVal = $("#selBankName").val();
	switch(selBankVal){
		//当前账户查询
		case '001' : 
					var postData = currentAccInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData,queryPostData());
					if (flag) {
						currentAccInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						currentAccInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//TA账户查询
		case '002' :
					var postData = TAAccInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData,queryPostData());
					if (flag) {
						TAAccInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						TAAccInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//二级清算账户查询
		case '003' :
					var postData = secondCptAccInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData,queryPostData());
					if (flag) {
						secondCptAccInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						secondCptAccInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//历史账户查询
		case '004' :
					var postData = historyAccInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData,queryPostData());
					if (flag) {
						historyAccInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						historyAccInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//历史二级清算账户查询
		case '005' :
					var postData = history2ndAccInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData,queryPostData());
					if (flag) {
						history2ndAccInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						history2ndAccInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//客户账户资料修改查询
		case '006' : 
					var postData = customerAccInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData,queryPostData());
					if (flag) {
						customerAccInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						customerAccInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
	
	}
}


WASP_WIDGET.triggerDateRangeStyle("startEnd");
/*日期控件 单个日期选择  */
WASP_WIDGET.triggerDateStyleWithYMD("appDate");

//默认调取currentAcc
$(function() {
	initCurrentAccInfoListGrid();
})
/*下拉框切换查询菜单栏*/
function Process(){
		//查询选择value值
		var selBankVal = $("#selBankName").val();
		//起止日期
		var startEndDate = $("#startEndDate");
		//申请日期
		var appDatetd = $("#appDatetd");
		//基金账号
		var fundAcctKey = $("#fundAcctKey");
		//证件号码
		var idNoKey =$("#idNoKey");
		//姓名
		var invNameKey = $("#invNameKey");
		//交易方式
		var selQueryWay = $("#selQueryWay");
		//业务类型
		var apkindkey = $("#apkindkey");
		//业务类型(当前账户、历史账户)
		var apkindapp = $("#apkindapp");
		//业务类型2(TA账户、二级清算、历史二级)
		var apkindack = $("#apkindack");
		//TA代码
		var tanokey = $("#tanokey");
		//网点代码
		var netpiontInfo = $("#netpiontInfo");
		//查询
		var queryBtn = $("#queryBtn");
		//导出
		var exportBtn = $("#exportBtn");
		//清空
		var resetBtn = $("#resetBtn");
		//当前账户表格
		var currentAcc = $("#currentAcc");
		//TA账户表格
		var TAAcc = $("#TAAcc");
		//二级清算账户表格
		var secondCptAcc = $("#secondCptAcc");
		//历史账户
		var historyAcc = $("#historyAcc");
		//历史二级清算账户
		var history2ndAcc = $("#history2ndAcc");
		//客户账户
		var customerAcc = $("#customerAcc");
		
		
		
		switch(selBankVal){
			case '001':/*
					当前账户查询菜单显示内容
						申请日期 
						查询 
						导出
						基金账号
						证件号码
						姓名
						交易方式
						业务类型
						TA代码
						网点代码
			*/		   
						startEndDate.css("display","none");
						appDatetd.css("display","block");
						fundAcctKey.css("display","block");
						idNoKey.css("display","block");
						invNameKey.css("display","block");
						selQueryWay.css("display","block");
						apkindkey.css("display","block");
						apkindapp.css("display","block");
						$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
						apkindack.css("display","none");
						$("#apkindack").select2('destroy');
						tanokey.css("display","block");
						netpiontInfo.css("display","block");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","inline-block");
						resetBtn.css("display","inline-block");
						TAAcc.css("display","none");
						secondCptAcc.css("display","none");
						historyAcc.css("display","none");
						history2ndAcc.css("display","none");
						customerAcc.css("display","none");
						currentAcc.css("display","block");
						$(function() {
							initCurrentAccInfoListGrid();
						})
						
						break;
			case '002':/*
						
						TA账户查询菜单显示内容
						查询
						基金账号
						证件号码
						姓名
						交易方式
						业务类型
						TA代码
							*/
						startEndDate.css("display","none");
						appDatetd.css("display","none");
						fundAcctKey.css("display","block");
						idNoKey.css("display","block");
						invNameKey.css("display","block");
						selQueryWay.css("display","block");
						apkindkey.css("display","block");
						
						$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
						apkindack.css("display","block");
						apkindapp.css("display","none");
						$("#apkindapp").select2('destroy');
						tanokey.css("display","block");
						netpiontInfo.css("display","block");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","none");
						resetBtn.css("display","inline-block");
						TAAcc.css("display","block");
						secondCptAcc.css("display","none");
						historyAcc.css("display","none");
						history2ndAcc.css("display","none");
						customerAcc.css("display","none");
						currentAcc.css("display","none");
						$(function() {
							initTAAccInfoListGrid();
						})
		               break;
			case '003':/*
						二级账户查询菜单显示内容
						查询
						基金账号
						证件号码
						姓名
						交易方式
						业务类型
						TA代码
					*/
						startEndDate.css("display","none");
						appDatetd.css("display","none");
						fundAcctKey.css("display","block");
						idNoKey.css("display","block");
						invNameKey.css("display","block");
						selQueryWay.css("display","block");
						apkindkey.css("display","block");
						$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
						apkindack.css("display","block");
						apkindapp.css("display","none");
						$("#apkindapp").select2('destroy');
						tanokey.css("display","block");
						netpiontInfo.css("display","block");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","none");
						resetBtn.css("display","inline-block");
						TAAcc.css("display","none");
						secondCptAcc.css("display","block");
						historyAcc.css("display","none");
						history2ndAcc.css("display","none");
						customerAcc.css("display","none");
						currentAcc.css("display","none");
						$(function() {
							init2ndCptAccInfoListGrid();
						})
						break;
			case '004':/* 
						历史账户查询菜单显示内容
						查询、导出
						起止日期
						基金账号
						证件号码
						姓名
						交易方式
						业务类型
						TA代码
						网点代码
						*/
						startEndDate.css("display","block");
						appDatetd.css("display","none");
						fundAcctKey.css("display","block");
						idNoKey.css("display","block");
						invNameKey.css("display","block");
						selQueryWay.css("display","block");
						apkindkey.css("display","block");
						$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
						apkindapp.css("display","block");
						apkindack.css("display","none");
						$("#apkindack").select2('destroy');
						tanokey.css("display","block");
						netpiontInfo.css("display","block");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","inline-block");
						resetBtn.css("display","inline-block");
						TAAcc.css("display","none");
						secondCptAcc.css("display","none");
						historyAcc.css("display","block");
						history2ndAcc.css("display","none");
						customerAcc.css("display","none");
						currentAcc.css("display","none");
						$(function() {
							initHistoryAccInfoListGrid();
						})
						break;
			case '005':/* 
							历史二级账户查询菜单显示内容
							查询、导出
							起止日期
							基金账号
							证件号码
							姓名
							交易方式
							业务类型
							TA代码
						*/
						startEndDate.css("display","block");
						appDatetd.css("display","none");
						fundAcctKey.css("display","block");
						idNoKey.css("display","block");
						invNameKey.css("display","block");
						selQueryWay.css("display","block");
						apkindkey.css("display","block");
						$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
						apkindack.css("display","block");
						apkindapp.css("display","none");
						$("#apkindapp").select2('destroy');
						tanokey.css("display","block");
						netpiontInfo.css("display","block");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","inline-block");
						resetBtn.css("display","inline-block");
						TAAcc.css("display","none");
						secondCptAcc.css("display","none");
						historyAcc.css("display","none");
						history2ndAcc.css("display","block");
						customerAcc.css("display","none");
						currentAcc.css("display","none");
						$(function() {
							initHistory2ndAccInfoListGrid();
						})
						break;
		    case '006':
		    				/*
		    				 *  客户账户查询菜单显示内容
		    				 *  查询 
		    				 *  起止日期 
		    				 */  
	    				startEndDate.css("display","block");
						appDatetd.css("display","none");
						fundAcctKey.css("display","none");
						idNoKey.css("display","none");
						invNameKey.css("display","none");
						selQueryWay.css("display","none");
						apkindkey.css("display","none");
						apkindapp.css("display","none");
						apkindack.css("display","none");
						tanokey.css("display","none");
						netpiontInfo.css("display","block");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","none");
						resetBtn.css("display","inline-block");
						apkindkey.css("display","none");
						TAAcc.css("display","none");
						secondCptAcc.css("display","none");
						historyAcc.css("display","none");
						history2ndAcc.css("display","none");
						customerAcc.css("display","block");
						currentAcc.css("display","none");
						$(function() {
							initCustomerAccInfoListGrid();
						})
						break;
		}
	}
