var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

/*自定义清空按钮函数*/
function resetSearchVal(){
	var selBankVal = $("#selBankName").val();
	//起止日期
	$("#startEnd").val($("#hiddenStartEnd").val());
	//申请日期
	$("#appDate").val($("#hiddenAppDate").val());
	//基金账号
	var fundacco = $("#fundacco").val(null);
	//交易方式
	var queryWay = $("#queryWay").val(null);
	//业务类型1
	var selApkindOne = $("#selApkindOne").val(null);
	//业务类型2
	var selApkindTwo = $("#selApkindTwo").val(null);
	//投资者名称
	var custname = $("#custname").val(null);
	//产品代码
	var fundCode = $("#fundCode").val(null);
	//产品名称
	var fundName = $("#fundName").val(null);
	//TA代码
	var tano = $("#tano").val(null);
	//网点代码
	var netPoint = $("#netPoint").val(null);
	switch(selBankVal){
			case '001' :
						$("#selApkindOne").select2({allowClear: false,minimumResultsForSearch:Infinity});
						break;
			case '002' :
						$("#selApkindOne").select2({allowClear: false,minimumResultsForSearch:Infinity});
						break;
			case '003' :
						$("#selApkindTwo").select2({allowClear: false,minimumResultsForSearch:Infinity});
						break;
			case '004' : 
						$("#selApkindTwo").select2({allowClear: false,minimumResultsForSearch:Infinity});
						break;
			case '007' :
						$("#selApkindOne").select2({allowClear: false,minimumResultsForSearch:Infinity});
						break;
			case '008' : 
						$("#selApkindTwo").select2({allowClear: false,minimumResultsForSearch:Infinity});
						break;
	}
	$("#queryWay").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#tano").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#netPoint").select2({allowClear: false,minimumResultsForSearch:Infinity});
}

//导出函数
function exportDetail(){
	/*查询下拉框*/
	var selBankVal = $("#selBankName").val();
	//起止日期
	var startEnd = $("#startEnd").val();
	//申请日期
	var appDate = $("#appDate").val();
	//基金账号
	var fundacco = $("#fundacco").val();
	//交易方式
	var queryWay = $("#queryWay").val();
	//开始日期
	var startDate =  $("#startEnd").getDateRangeValue()[0];
	//结束日期
	var endDate = $("#startEnd").getDateRangeValue()[1];
	//业务类型1
	var selApkindOne = $("#selApkindOne").val();
	//业务类型2
	var selApkindTwo = $("#selApkindTwo").val();
	//投资者名称
	var custname = $("#custname").val();
	//产品代码
	var fundCode = $("#fundCode").val();
	//产品名称
	var fundName = $("#fundName").val();
	//TA代码
	var tano = $("#tano").val();
	//网点代码
	var netpoint = $("#netPoint").val();
	//导出表单
	var exportForm = $("#exportForm");
	
	switch(selBankVal){
		//当前账户
		case  '001' : 
					var url = PRIMARY_PATH + "/exportTradeAppToday.xhtml?";
					var params = 
						  'sp[appDate]=' + appDate
						+ '&sp[fundacct]=' + fundacco
						+ '&sp[fundcode]=' + fundCode
						+ '&sp[fundname]=' + fundName
						+ '&sp[invnm]=' + custname
						+ '&sp[qway]=' + queryWay
						+ '&sp[selApkindOne]=' + selApkindOne
						+ '&sp[tano]=' + tano
						+ '&sp[netpoint]=' + netpoint;
					var actionUrl = url + params;
					exportForm.prop("action",actionUrl);
					exportForm.submit();
					break;
		//历史交易
		case  '007' :
					var	url = PRIMARY_PATH + "/exportTradeAppHistory.xhtml?";
					var params = 
						'sp[startDate]=' + startDate 
						+ '&sp[endDate]=' + endDate 
						+ '&sp[fundacct]=' + fundacco 
						+ '&sp[fundcode]=' + fundCode
						+ '&sp[fundname]=' + fundName 
						+ '&sp[invnm]=' + custname
						+ '&sp[qway]=' + queryWay
						+ '&sp[selApkindOne]=' + selApkindOne
						+ '&sp[tano]=' + tano
						+ '&sp[netpoint]=' + netpoint;
					var actionUrl = url + params;
					exportForm.prop("action",actionUrl);
					exportForm.submit();
					break;
		//历史二级清算交易
		case  '008' :
					var	url = PRIMARY_PATH + "/exportTradeClearHis.xhtml?";
					var params =
						'sp[startDate]=' + startDate
						+ '&sp[endDate]=' + endDate
						+ '&sp[fundacct]=' + fundacco 
						+ '&sp[invnm]=' + custname
						+ '&sp[qway]=' + queryWay
						+ '&sp[tano]=' + tano
						+ '&sp[selApkindTwo]=' + selApkindTwo
						+ '&sp[netpoint]=' + netpoint
						;
					var actionUrl = url + params;
					exportForm.prop("action",actionUrl);
					exportForm.submit();
					break;
	}
	
}

function queryPostData(){
	var startEnd = $("#startEnd").val();
	//申请日期
	var appDate = $("#appDate").val();
	//基金账号
	var fundacco = $("#fundacco").val();
	//交易方式
	var queryWay = $("#queryWay").val();
	//开始日期
	var startDate =  $("#startEnd").getDateRangeValue()[0];
	//结束日期
	var endDate = $("#startEnd").getDateRangeValue()[1];
	//业务类型1
	var selApkindOne = $("#selApkindOne").val();
	//业务类型2
	var selApkindTwo = $("#selApkindTwo").val();
	//投资者名称
	var custname = $("#custname").val();
	//产品代码
	var fundCode = $("#fundCode").val();
	//产品名称
	var fundName = $("#fundName").val();
	//TA代码
	var tano = $("#tano").val();
	//网点代码
	var netpoint = $("#netPoint").val();
	
	var postData={};
	
	postData.sp={};
	postData.sp.startDate =  startDate;
	postData.sp.endDate =  endDate;
	postData.sp.appDate = appDate;
	postData.sp.fundacct = fundacco;
	postData.sp.qway = queryWay;
	postData.sp.selApkindOne = selApkindOne;
	postData.sp.selApkindTwo = selApkindTwo;
	postData.sp.invnm = custname;
	postData.sp.tano = tano;
	postData.sp.fundcode = fundCode;
	postData.sp.tano = tano;
	postData.sp.netpoint = netpoint;
	postData.sp.fundname = fundName;
	return postData;
}

function queryByCondtion(flag){
	/*查询下拉框*/
	var selBankVal = $("#selBankName").val();
	//起止日期
	var startEnd = $("#startEnd").val();
	//申请日期
	var appDate = $("#appDate").val();
	//基金账号
	var fundacco = $("#fundacco").val();
	//交易方式
	var queryWay = $("#queryWay").val();
	//开始日期
	var startDate =  $("#startEnd").getDateRangeValue()[0];
	//结束日期
	var endDate = $("#startEnd").getDateRangeValue()[1];
	//业务类型1
	var selApkindOne = $("#selApkindOne").val();
	//业务类型2
	var selApkindTwo = $("#selApkindTwo").val();
	//投资者名称
	var custname = $("#custname").val();
	//产品代码
	var fundCode = $("#fundCode").val();
	//产品名称
	var fundName = $("#fundName").val();
	//TA代码
	var tano = $("#tano").val();
	//网点代码
	var netpoint = $("#netPoint").val();
	switch(selBankVal){
		//当前交易
			case '001' : 
					var postData = currentTradeInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData, queryPostData());
					if (flag) {
						currentTradeInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						currentTradeInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//资金流水
		case '002' :
					var postData = fundTradeInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData, queryPostData());
					if (flag) {
						fundTradeInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						fundTradeInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//二级清算交易
		case '003' :
					var postData = secondCptTradeInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData, queryPostData());
					if (flag) {
						secondCptTradeInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						secondCptTradeInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//TA交易查询
		case '004' :
					var postData = TATradeInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData, queryPostData());
					if (flag) {
						TATradeInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						TATradeInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//当天分红
		case '005' :
					var postData = currentDayMelonmdInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData, queryPostData());
					if (flag) {
						currentDayMelonmdInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						currentDayMelonmdInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
		//历史分红
		case '006' : 
			var postData = historyMelonmdInfoList.jqGrid("getGridParam", "postData");
			$.extend(postData, queryPostData());
			if (flag) {
				historyMelonmdInfoList.trigger("reloadGrid", [ {
					page : 1
				} ]); // 重新载入Grid表格
			} else {
				historyMelonmdInfoList.trigger("reloadGrid"); // 重新载入Grid表格
			}
			break;
		//历史交易	
		case '007' : 
			var postData = historyTradeInfoList.jqGrid("getGridParam", "postData");
			$.extend(postData, queryPostData());
			if (flag) {
				historyTradeInfoList.trigger("reloadGrid", [ {
					page : 1
				} ]); // 重新载入Grid表格
			} else {
				historyTradeInfoList.trigger("reloadGrid"); // 重新载入Grid表格
			}
			break;
		//历史二级交易
		case '008' : 
					var postData = history2ndTradeInfoList.jqGrid("getGridParam", "postData");
					$.extend(postData, queryPostData());
					if (flag) {
						history2ndTradeInfoList.trigger("reloadGrid", [ {
							page : 1
						} ]); // 重新载入Grid表格
					} else {
						history2ndTradeInfoList.trigger("reloadGrid"); // 重新载入Grid表格
					}
					break;
	
	}
}


WASP_WIDGET.triggerDateRangeStyle("startEnd");
/*日期控件 单个日期选择  */
WASP_WIDGET.triggerDateStyleWithYMD("appDate");

//默认调取currentAcc
$(function() {
	$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
	$("#selApkindTwo").select2('destroy');
	$("#appDate").val($("#hiddenAppDate").val());
	$("#startEnd").val($("#hiddenStartEnd").val());
	initCurrentTradeInfoListGrid();
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
		var fundaccoid = $("#fundaccoid");
		//交易方式
		var selQueryWay = $("#selQueryWay");
		//业务类型
		var apkindkey = $("#apkindkey");
		//业务类型1
		var selApkindOne = $("#selApkindOne");
		//业务类型2
		var selApkindTwo = $("#selApkindTwo");
		//投资者名称
		var custnameid = $("#custnameid");
		//产品代码
		var productCode = $("#productCode");
		//产品名称
		var productName = $("#productName");
		//TA代码
		var seltano = $("#seltano");
		//网点代码
		var netpiontInfo = $("#netpiontInfo");
		//查询
		var queryBtn = $("#queryBtn");
		//导出
		var exportBtn = $("#exportBtn");
		//清空
		var resetBtn = $("#resetBtn");
		//当前交易表格
		var currentTrade = $("#currentTrade");
		//资金流水表格
		var fundTrade = $("#fundTrade");
		//二级清算交易表格
		var secondCptTrade = $("#secondCptTrade");
		//TA交易表格
		var TATrade = $("#TATrade");
		//当天分红表格
		var curDayMelonmd = $("#curDayMelonmd");
		//历史分红表格
		var hisMelonmd = $("#hisMelonmd");
		//历史交易表格
		var hisTrade = $("#hisTrade");
		//历史二级交易账户
		var his2ndTrade = $("#his2ndTrade");
		switch(selBankVal){
			case '001':/*
						当前交易申请流水菜单栏
						申请日期 
						查询 
						导出
						基金账号
						投资者名称
						交易方式
						业务类型
						产品代码
						产品名称
						TA代码
						网点代码
			*/		   
						startEndDate.css("display","none");
						appDatetd.css("display","block");
						fundaccoid.css("display","block");
						custnameid.css("display","block");
						selQueryWay.css("display","block");
						apkindkey.css("display","block");
						$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
						selApkindOne.css("display","block");
						selApkindTwo.css("display","none");
						$("#selApkindTwo").select2('destroy');
						seltano.css("display","block");
						netpiontInfo.css("display","block");
						productCode.css("display","block");
						productName.css("display","block");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","inline-block");
						resetBtn.css("display","inline-block");
						
						
						his2ndTrade.css("display","none");
						hisTrade.css("display","none");
						hisMelonmd.css("display","none");
						curDayMelonmd.css("display","none");
						TATrade.css("display","none");
						secondCptTrade.css("display","none");
						fundTrade.css("display","none");
						currentTrade.css("display","block");
						
						//表格初始化
						$(function() {
							initCurrentTradeInfoListGrid();
						})
						
						break;
			case '002':/*
						资金流水查询菜单显示内容
						查询
						起始日期
						业务类型
						基金账号
						投资者名称
						TA代码
							*/
						startEndDate.css("display","block");
						appDatetd.css("display","none");
						fundaccoid.css("display","block");
						custnameid.css("display","block");
						selQueryWay.css("display","none");
						apkindkey.css("display","block");
						$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
						selApkindOne.css("display","block");
						selApkindTwo.css("display","none");
						$("#selApkindTwo").select2('destroy');
						seltano.css("display","block");
						netpiontInfo.css("display","block");
						productCode.css("display","none");
						productName.css("display","none");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","none");
						resetBtn.css("display","inline-block");
						
						fundTrade.css("display","block");
						currentTrade.css("display","none");
						secondCptTrade.css("display","none");
						TATrade.css("display","none");
						curDayMelonmd.css("display","none");
						hisMelonmd.css("display","none");
						hisTrade.css("display","none");
						his2ndTrade.css("display","none");
						
						$(function() {
							initFundTradeInfoListGrid();
						})
		               break;
			case '003':/*
						二级清算交易确认查询菜单显示内容
						查询
						基金账号
						业务类型
						投资者名称
						TA代码
					*/
						startEndDate.css("display","none");
						appDatetd.css("display","none");
						fundaccoid.css("display","block");
						custnameid.css("display","block");
						selQueryWay.css("display","none");
						apkindkey.css("display","block");
						$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
						selApkindTwo.css("display","block");
						selApkindOne.css("display","none");
						$("#selApkindOne").select2('destroy');
						seltano.css("display","block");
						netpiontInfo.css("display","block");
						productCode.css("display","none");
						productName.css("display","none");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","none");
						resetBtn.css("display","inline-block");
						currentTrade.css("display","none");
						secondCptTrade.css("display","block");
						fundTrade.css("display","none");
						TATrade.css("display","none");
						curDayMelonmd.css("display","none");
						hisMelonmd.css("display","none");
						hisTrade.css("display","none");
						his2ndTrade.css("display","none");
						
						$(function() {
							init2ndCptTradeInfoListGrid();
						})
						break;
			case '004':/* 
						TA交易查询菜单显示内容
						查询
						基金账号
						投资者名称
						交易方式
						业务类型
						TA代码
						*/
						startEndDate.css("display","none");
						appDatetd.css("display","none");
						fundaccoid.css("display","block");
						custnameid.css("display","block");
						selQueryWay.css("display","block");
						apkindkey.css("display","block");
						$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
						selApkindTwo.css("display","block");
						selApkindOne.css("display","none");
						$("#selApkindOne").select2('destroy');
						seltano.css("display","block");
						netpiontInfo.css("display","block");
						productCode.css("display","none");
						productName.css("display","none");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","none");
						resetBtn.css("display","inline-block");
						
						currentTrade.css("display","none");
						fundTrade.css("display","none");
						TATrade.css("display","block");
						secondCptTrade.css("display","none");
						curDayMelonmd.css("display","none");
						hisMelonmd.css("display","none");
						hisTrade.css("display","none");
						his2ndTrade.css("display","none");
						$(function() {
							initTATradeInfoListGrid();
						})
						break;
			case '005':/* 
							当天分红查询菜单显示内容
							查询
							基金账号
							投资者名称
							交易方式
							TA代码
						*/
						startEndDate.css("display","none");
						appDatetd.css("display","none");
						fundaccoid.css("display","block");
						custnameid.css("display","block");
						selQueryWay.css("display","block");
						apkindkey.css("display","none");
						selApkindOne.css("display","none");
						selApkindTwo.css("display","none");
						seltano.css("display","block");
						netpiontInfo.css("display","block");
						productCode.css("display","none");
						productName.css("display","none");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","none");
						resetBtn.css("display","inline-block");
				
						his2ndTrade.css("display","none");
						hisTrade.css("display","none");
						hisMelonmd.css("display","none");
						curDayMelonmd.css("display","block");
						TATrade.css("display","none");
						secondCptTrade.css("display","none");
						fundTrade.css("display","none");
						currentTrade.css("display","none");
						$(function() {
							initcurrentDayMelonmdInfoListGrid();
						})
						break;
		    case '006':
		    				/*
		    				 *  历史分红明细账户查询菜单显示内容
		    				 *  查询 
		    				 *  起止日期 
		    				 *  基金账号
		    				 *  投资者名称
		    				 *  交易方式
		    				 *  TA代码
		    				 */  
				    	startEndDate.css("display","block");
						appDatetd.css("display","none");
						fundaccoid.css("display","block");
						custnameid.css("display","block");
						selQueryWay.css("display","block");
						apkindkey.css("display","none");
						selApkindOne.css("display","none");
						selApkindTwo.css("display","none");
						seltano.css("display","block");
						netpiontInfo.css("display","block");
						productCode.css("display","none");
						productName.css("display","none");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","none");
						resetBtn.css("display","inline-block");
						
						TATrade.css("display","none");
						his2ndTrade.css("display","none");
						hisTrade.css("display","none");
						curDayMelonmd.css("display","none");
						hisMelonmd.css("display","block");
						secondCptTrade.css("display","none");
						fundTrade.css("display","none");
						currentTrade.css("display","none");
						$(function() {
							initHistoryMelonmdInfoListGrid();
						})
						break;
		    case '007':
						/*
						 *  历史交易申请流水查询菜单显示内容
						 *  查询 、导出
						 *  起止日期 
						 *  基金账号
						 *  投资者名称
						 *  业务类型
						 *  交易方式
						 *  TA代码
						 *  产品代码
						 *  产品名称
						 *  网点代码
						 */  
				    	startEndDate.css("display","block");
						appDatetd.css("display","none");
						fundaccoid.css("display","block");
						custnameid.css("display","block");
						selQueryWay.css("display","block");
						apkindkey.css("display","block");
						$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
						selApkindOne.css("display","block");
						selApkindTwo.css("display","none");
						$("#selApkindTwo").select2('destroy');
						seltano.css("display","block");
						netpiontInfo.css("display","block");
						productCode.css("display","block");
						productName.css("display","block");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","inline-block");
						resetBtn.css("display","inline-block");
						
						TATrade.css("display","none");
						secondCptTrade.css("display","none");
						fundTrade.css("display","none");
						currentTrade.css("display","none");
						hisTrade.css("display","block");
						his2ndTrade.css("display","none");
						hisMelonmd.css("display","none");
						curDayMelonmd.css("display","none");
						$(function() {
							initHistoryTradeInfoListGrid();
						})
						break;
		    case '008':
						/*
						 *  历史二级清算交易申请流水查询菜单显示内容
						 *  查询 、导出
						 *  起止日期 
						 *  基金账号
						 *  投资者名称
						 *  业务类型
						 *  交易方式
						 *  TA代码
						 */  
				    	startEndDate.css("display","block");
						appDatetd.css("display","none");
						fundaccoid.css("display","block");
						custnameid.css("display","block");
						selQueryWay.css("display","block");
						apkindkey.css("display","block");
						$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
						selApkindTwo.css("display","block");
						selApkindOne.css("display","none");
						$("#selApkindOne").select2('destroy');
						seltano.css("display","block");
						netpiontInfo.css("display","block");
						productCode.css("display","none");
						productName.css("display","none");
						queryBtn.css("display","inline-block");
						exportBtn.css("display","inline-block");
						resetBtn.css("display","inline-block");
						
						hisMelonmd.css("display","none");
						hisTrade.css("display","none");
						curDayMelonmd.css("display","none");
						TATrade.css("display","none");
						his2ndTrade.css("display","block");
						secondCptTrade.css("display","none");
						fundTrade.css("display","none");
						currentTrade.css("display","none");
						$(function() {
							initHistory2ndTradeInfoListGrid();
						})
						break;
		}
	}
