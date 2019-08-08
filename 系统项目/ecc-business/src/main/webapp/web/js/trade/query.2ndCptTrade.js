﻿var PRIMARY_PATH = "";
var BASE_PATH = "";



function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var secondCptTradeInfoList = $('#secondCptTradeInfoList');

/* 二级清算交易表格分页 */
function init2ndCptTradeInfoListGrid() {
	secondCptTradeInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryTradeAckClear.xhtml',
				caption : '二级清算交易确认流水列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				colNames :[ "报表功能", "基金账号", "交易账户",'网点代码', "确认日期", "申请日期", "投资人类型",
						"投资人姓名","确认金额", "基金代码","业务类型","申请份额","申请金额",
						"确认份额","基金净值","折扣率","收费方式","手续费","对方销售机构代码",
						"对方销售机构名称","对方基金代码","对方基金账户","对方交易账户","原申请流水号","分红方式",
						"分红比率","冻结原因","返回代码","备注","申请流水号",'确认流水号',''],
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
					hidden : false,
					resizable : true,
					sortable : false
				}, {
					name : 'netpoint',
					index : 'netpoint',
					align : 'left',
					hidden : false,
					resizable : true,
					sortable : false
				}, {
					name : 'ackdt',
					index : 'ackdt',
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
					name : 'ackamt',
					index : 'ackamt',
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
					name : 'sharetype',
					index : 'sharetype',
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
					name : 'oseatnm',
					index : 'oseatnm',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'ofundid',
					index : 'ofundid',
					resizable : true,
					alisgn : 'left',
					sortable : false
					/*formatter:function(colValue){
						if(colValue=='9999')
							return '网上';
						if(colValue=='0001')
							return '网下';
						return '';
					}*/
				},{
					name : 'ofundacct',
					index : 'ofundacct',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'otradeacco',
					index : 'otradeacco',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'oldappno',
					index : 'oldappno',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					width:200
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
				},{
					name : 'serialno',
					index : 'serialno',
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
				pager : "#secondCptTradeInfoPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var ids = secondCptTradeInfoList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = secondCptTradeInfoList.jqGrid('getRowData', id);
						var apkind = rowData.apkind;
						var ackno = rowData.ackno;
						var appDate = $("#appDate").val();
						var be = '';
						var url = PRIMARY_PATH + "/getDsReportLink.xhtml?";
						if(apkind == '120' || apkind == '121'){
								/*be = "<a href= ' " + url + "permissionId=8826" 
										+ "&PI_ACKNO=" + ackno
										+ "&PI_ACKSDT=" + appDate
										+ "&PI_ACKEDT=" + appDate
										+ " ' style='color:blue;' target = '_blank' "  
										+ ">认购确认回单</a>";*/
								be = "";
						}else if(apkind == '122' || apkind == '123'){
								be = "<a href= ' " + url + "permissionId=8823" 
										+ "&PI_ACKNO=" + ackno
										+ "&PI_ACKSDT=" + appDate
										+ "&PI_ACKEDT=" + appDate
										+ " ' style='color:blue;' target = '_blank' "  
										+ ">申购确认回单</a>";
						}else if(apkind == '124' || apkind == '125'){
							be = "<a href= ' " + url + "permissionId=8821" 
									+ "&PI_ACKNO=" + ackno
									+ "&PI_ACKSDT=" + appDate
									+ "&PI_ACKEDT=" + appDate
									+ " ' style='color:blue;' target = '_blank' "  
								+ ">赎回确认回单</a>";
						}else if(apkind == '136' || apkind == '137'){
							be = "<a href= ' " + url + "permissionId=8819" 
									+ "&PI_ACKNO=" + ackno
									+ "&PI_ACKSDT=" + appDate
									+ "&PI_ACKEDT=" + appDate
									+ " ' style='color:blue;' target = '_blank' "  
									+ ">转换确认回单</a>";
						}else if(apkind == '129' ){
							be = "<a href= ' " + url + "permissionId=8829" 
									+ "&PI_ACKNO=" + ackno
									+ "&PI_ACKSDT=" + appDate
									+ "&PI_ACKEDT=" + appDate
									+ " ' style='color:blue;' target = '_blank' "  
									+ ">分红变更确认回单</a>";
						}else if(apkind == '143'){
							be = "<a href= ' " + url + "permissionId=8828" 
									+ "&PI_ACKNO=" + ackno
									+ "&PI_ACKSDT=" + appDate
									+ "&PI_ACKEDT=" + appDate
							+ " ' style='color:blue;' target = '_blank' "  
							+ ">红利发放确认回单</a>";
						}else{
								be = "";
						}
						secondCptTradeInfoList.jqGrid('setRowData', id, {
							aTag : be
						});
					}
				},
				subGrid : false
			});

		secondCptTradeInfoList.navGrid('#secondCptTradeInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	secondCptTradeInfoList.jqGrid('setFrozenColumns');
	jqGridResize(secondCptTradeInfoList);

};
