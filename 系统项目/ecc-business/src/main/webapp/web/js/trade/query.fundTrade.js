var PRIMARY_PATH = "";
var BASE_PATH = "";



function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var fundTradeInfoList = $('#fundTradeInfoList');

/* 资金流水表格分页 */
function initFundTradeInfoListGrid() {
	fundTradeInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryTradeCapitalBlotter.xhtml',
				caption : '资金流水列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				//29
				colNames :[ "基金账号", "业务类型", "交易账户",'网点代码', "申请日期", "基金代码", "资金流水号",
						"投资人姓名","确认金额", "资金账户","发生金额","申请金额","返回代码",
						"返回信息"],
				colModel : [ {
					name : 'fundacct',
					index : 'fundacct',
					align : 'left',
					hidden : false,
					key : true,
					sortable : false
				}, {
					name : 'apkindName',
					index : 'apkindName',
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
					name : 'apdt',
					index : 'apdt',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'fundid',
					index : 'fundid',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'capitalno',
					index : 'capitalno',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'invnm',
					index : 'invnm',
					resizable : true,
					align : 'left',
					sortable : false,
					width : 200
				}, {
					name : 'ackamt',
					index : 'ackamt',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'bankacco',
					index : 'bankacco',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'debit',
					index : 'debit',
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
					name : 'paymentflag',
					index : 'paymentflag',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'paymentmsg',
					index : 'paymentmsg',
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
				pager : "#fundTradeInfoPage",
				viewrecords : true,
				hidegrid : false,
				
				subGrid : false
			});

	fundTradeInfoList.navGrid('#fundTradeInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	fundTradeInfoList.jqGrid('setFrozenColumns');
	jqGridResize(fundTradeInfoList);

};
