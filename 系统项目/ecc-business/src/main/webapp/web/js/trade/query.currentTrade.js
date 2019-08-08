﻿var PRIMARY_PATH = "";
var BASE_PATH = "";



function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var currentTradeInfoList = $('#currentTradeInfoList');

/* 当前交易表格分页 */
function initCurrentTradeInfoListGrid() {
	currentTradeInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryCurrentTrade.xhtml',
				caption : '当前交易申请流水列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				colNames : ["申请受理单", "交易确认回单(T+1)", "申请日期", "基金账号", "投资者名称", "交易账号",
						"银行账号","交易渠道", "网点代码","业务类型","基金代码","申请金额",
						"申请份额","基金名称","折扣率","对方基金代码","对方基金名称","分红方式",
						"分红比例","投资者类型","原申请合同号","对方销售机构代码","对方销售机构名称","经办人名称",
						"经办人办公电话","经办人传真号码","是否资金复核通过",'申请流水号','申请编号',"申请状态",''],
				colModel : [ {
					name : 'aTag1',
					index : 'aTag1',
					align : 'left',
					hidden : false,
					key : true,
					sortable : false
				}, {
					name : 'aTag2',
					index : 'aTag2',
					align : 'left',
					hidden : false,
					resizable : true,
					sortable : false
				}, {
					name : 'apdt',
					index : 'apdt',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'fundacct',
					index : 'fundacct',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'invnm',
					index : 'invnm',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'tradeacco',
					index : 'tradeacco',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'bankacco',
					index : 'bankacco',
					resizable : true,
					align : 'left',
					sortable : false,
					width:200
				}, {
					name : 'bankno',
					index : 'bankno',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'netpoint',
					index : 'netpoint',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'apkindName',
					index : 'apkindName',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'fundid',
					index : 'fundid',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'subamt',
					index : 'subamt',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'subquty',
					index : 'subquty',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'fundname',
					index : 'fundname',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'commro',
					index : 'commro',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'ofundid',
					index : 'ofundid',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'ofundname',
					index : 'ofundname',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'melonmdName',
					index : 'melonmdName',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'dividendrate',
					index : 'dividendrate',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'invtpName',
					index : 'invtpName',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'oldappno',
					index : 'oldappno',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'oseatno',
					index : 'oseatno',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'oseatnm',
					index : 'oseatnm',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'broker',
					index : 'broker',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'brokertel',
					index : 'brokertel',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'brokerfax',
					index : 'brokerfax',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'checkflag',
					index : 'checkflag',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'serialno',
					index : 'serialno',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'appno',
					index : 'appno',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					width:200
				},{
					name : 'applyst',
					index : 'applyst',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'apkind',
					index : 'apkind',
					hidden : true,
					resizable : true,
					alisgn : 'left',
					sortable : false
				}],
				rowNum : 10,
				rowList : [ 10, 30, 50 ],
				rownumbers : true,
				rownumWidth : 50,
				prmNames : {
					search : "search",
					page : "pageNo",
					rows : "limit"
				},
				height : 'auto',
				width : false,
				//autowidth : true,
				shrinkToFit : false,
				editurl : '',
				viewrecords : true,
				cellEdit : false,
				grouping : false,
				autoScroll: true,
				jsonReader : {
					root : "items", // 结果集
					records : "total", // 总记录数
					total : "pageCount", // 总页数
					page : "pageNo", // 当前页
					repeatitems : false
				// (4)
				},
				loadError : function(xhr, status, error) {
					switch (status) {
					case 403:
						sweetAlert("对不起，您无此权限！", "", "error");
						break;
					case 404:
						sweetAlert("对不起，无此页面！", "", "error");
						break;
					case 500:
						sweetAlert("内部错误，请联系管理员！", "", "error");
						break;
					case 504:
						sweetAlert("超时，请联系管理员！", "", "error");
						break;
					}
				},
				loadComplete : function(data) {
					if (data.ResultCode == '8000') {
						swal({
							title:'请重新登录',
							type : "warning",
							showCancelButton : true,
							confirmButtonColor : '#DD6B55',
							confirmButtonText : "确定",
							cancelButtonText : "取消",
							closeOnConfirm : false,
							closeOnCancel : true
						},function(isConfirm){
							if(isConfirm){
								window.location.href = '/service/login.jsp';
							}
						})
						return;
					}
				},
				pager : "#currentTradeInfoPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var ids = currentTradeInfoList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = currentTradeInfoList.jqGrid('getRowData', id);
						var apkind = rowData.apkind;
						var appno = rowData.appno;
						var appDate = $("#appDate").val();
						var be = '';
						var ce = '';
						var url = PRIMARY_PATH + "/getDsReportLink.xhtml?";
						//申请受理
						if(apkind == '020' || apkind == '021'){
								be = "<a href= ' " + url + "permissionId=8825" 
										+ "&PI_APPNO=" + appno
										+ "&PI_APSDT=" + appDate
										+ "&PI_APEDT=" + appDate
										+ " ' style='color:blue;' target = '_blank' "  
										+ ">认购受理回单</a>";
						}else if(apkind == '022' || apkind == '023'){
								be = "<a href= ' " + url + "permissionId=8824" 
										+ "&PI_APPNO=" + appno
										+ "&PI_APSDT=" + appDate
										+ "&PI_APEDT=" + appDate
										+ " ' style='color:blue;' target = '_blank' "  
										+ ">申购受理回单</a>";
						}else if(apkind == '024' || apkind == '025'){
								be = "<a href= ' " + url + "permissionId=8822" 
										+ "&PI_APPNO=" + appno
										+ "&PI_APSDT=" + appDate
										+ "&PI_APEDT=" + appDate
										+ " ' style='color:blue;' target = '_blank' "  
										+ ">赎回受理回单</a>";
						}else if(apkind == '036' || apkind == '037' || apkind == '038'){
								be = "<a href= ' " + url + "permissionId=8820" 
								+ "&PI_APPNO=" + appno
								+ "&PI_APSDT=" + appDate
								+ "&PI_APEDT=" + appDate
								+ " ' style='color:blue;' target = '_blank' "  
								+ ">转换受理回单</a>";
						}else if(apkind == '029'){
								be = "<a href= ' " + url + "permissionId=8830" 
								+ "&PI_APPNO=" + appno
								+ "&PI_APSDT=" + appDate
								+ "&PI_APEDT=" + appDate
								+ " ' style='color:blue;' target = '_blank' "  
								+ ">分红变更受理回单</a>";
						}else{
								be = "";
						}
						//交易确认回单
						
						if(apkind == '022' || apkind == '023'){
							ce = "<a href= ' " + url + "permissionId=8888" 
									+ "&PI_APPNO=" + appno
									+ " ' style='color:blue;' target = '_blank' "  
									+ ">申购确认单</a>";
						}else if(apkind == '024' || apkind == '025'){
							ce = "<a href= ' " + url + "permissionId=8889" 
									+ "&PI_APPNO=" + appno
									+ " ' style='color:blue;' target = '_blank' "  
									+ ">赎回确认单</a>";
						
						}else if(apkind == '036' || apkind == '037' || apkind == '038'){
							ce = "<a href= ' " + url + "permissionId=8890" 
									+ "&PI_APPNO=" + appno
									+ " ' style='color:blue;' target = '_blank' "  
									+ ">转换确认单</a>";
						}else{
								ce = "";
						}
						
						currentTradeInfoList.jqGrid('setRowData', id, {
							aTag1 : be ,
							aTag2 : ce 
						});
					}
				},
				subGrid : false
			});

	currentTradeInfoList.navGrid('#currentTradeInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	currentTradeInfoList.jqGrid('setFrozenColumns');
	jqGridResize(currentTradeInfoList);

};
