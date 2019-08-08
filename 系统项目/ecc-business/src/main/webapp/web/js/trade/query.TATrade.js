﻿var PRIMARY_PATH = "";
var BASE_PATH = "";



function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var TATradeInfoList = $('#TATradeInfoList');

/*TA交易表格分页 */
function initTATradeInfoListGrid() {
	TATradeInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryTATrade.xhtml',
				caption : 'TA交易确认流水列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				colNames :[ "申请流水号", "确认流水号", "基金代码", "交易账户",'网点代码', "申请流水号", "确认日期",
						"申请日期","确认金额", "业务类型","投资者类型","投资者名称","基金账号",
						"申请金额","申请份额","基金净值","折扣率","手续费","对方销售机构代码",
						"对方基金账户","对方基金代码","分红方式","分红比率","冻结原因","返回代码",
						"备注"],
				colModel : [ {
					name : 'appno',
					index : 'appno',
					align : 'left',
					hidden : false,
					key : true,
					sortable : false,
					width:200
				}, {
					name : 'ackno',
					index : 'ackno',
					align : 'left',
					hidden : false,
					resizable : true,
					sortable : false,
					width:200
				}, {
					name : 'fundid',
					index : 'fundid',
					align : 'left',
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
					name : 'serialno',
					index : 'serialno',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'ackdt',
					index : 'ackdt',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'apdt',
					index : 'apdt',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'ackamt',
					index : 'ackamt',
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
					name : 'invtpName',
					index : 'invtpName',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'invnm',
					index : 'invnm',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'fundacct',
					index : 'fundacct',
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
					name : 'acknav',
					index : 'acknav',
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
					name : 'fee',
					index : 'fee',
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
					name : 'ofundacct',
					index : 'ofundacct',
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
					name : 'frozencause',
					index : 'frozencause',
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
				pager : "#TATradeInfoPage",
				viewrecords : true,
				hidegrid : false,
				subGrid : false
			});

	TATradeInfoList.navGrid('#TATradeInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	TATradeInfoList.jqGrid('setFrozenColumns');
	jqGridResize(TATradeInfoList);

};
