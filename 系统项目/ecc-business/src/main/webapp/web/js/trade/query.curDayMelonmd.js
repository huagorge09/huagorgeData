﻿var PRIMARY_PATH = "";
var BASE_PATH = "";



function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var currentDayMelonmdInfoList = $('#currentDayMelonmdInfoList');

/*当天分红表格分页*/
function initcurrentDayMelonmdInfoListGrid() {
	currentDayMelonmdInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryTradeDividendDetail.xhtml',
				caption : '当天分红明细列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				colNames :[ "报表功能", "基金账号", "交易账户", '网点代码',"红利发放日", "投资人类型", "投资人姓名",
						"处理标志","基金代码", "业务类型","红利红股基数","应发红利金额","红利再投金额",
						"冻结红股份额","实发红利金额","实际份额","可用份额","基金净值","分红方式",
						"分红比率","返回代码","备注","确认流水号",''],
				colModel : [ {
					name : 'aTag',
					index : 'aTag',
					align : 'left',
					hidden : false,
					key : true,
					sortable : false
				}, {
					name : 'fundacct',
					index : 'fundacct',
					align : 'left',
					hidden : false,
					resizable : true,
					sortable : false
				}, {
					name : 'tradeacco',
					index : 'tradeacco',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'netpoint',
					index : 'netpoint',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'ackdt',
					index : 'ackdt',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'invtpName',
					index : 'invtpName',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'invnm',
					index : 'invnm',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'applyst',
					index : 'applyst',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'fundid',
					index : 'fundid',
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
					name : 'subquty',
					index : 'subquty',
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
					name : 'ackquty',
					index : 'ackquty',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'frozenbalance',
					index : 'frozenbalance',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'ackamt',
					index : 'ackamt',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'balance',
					index : 'balance',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'available',
					index : 'available',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'acknav',
					index : 'acknav',
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
					name : 'retcode',
					index : 'retcode',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'retmsg',
					index : 'retmsg',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'ackno',
					index : 'ackno',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					width:200
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
				pager : "#currentDayMelonmdPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var ids = currentDayMelonmdInfoList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = currentDayMelonmdInfoList.jqGrid('getRowData', id);
						var apkind = rowData.apkind;
						var appno = rowData.appno;
						var be = '';
						var url = PRIMARY_PATH + "/getDsReportLink.xhtml?";
						if(apkind == '029'){
								be = "<a href= ' " + url + "permissionId=8830" 
										+ "&PI_APPNO=" + ackno
										+ " ' style='color:blue;' target = '_blank' "  
										+ ">分红变更受理回单</a>";
						}else{
								be = "";
						}	
						currentDayMelonmdInfoList.jqGrid('setRowData', id, {
							aTag : be
						});
					}
				},
				subGrid : false
			});

		currentDayMelonmdInfoList.navGrid('#currentDayMelonmdPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	currentDayMelonmdInfoList.jqGrid('setFrozenColumns');
	jqGridResize(currentDayMelonmdInfoList);
};
