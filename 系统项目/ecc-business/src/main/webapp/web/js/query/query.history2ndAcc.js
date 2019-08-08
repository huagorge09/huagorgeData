
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var history2ndAccInfoList = $("#history2ndAccInfoList");
function initHistory2ndAccInfoListGrid() {
	history2ndAccInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryAppClearHisAcc.xhtml',
				caption : '历史二级清算账户确认流水列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				colNames :[ "报表功能", "基金账号", "交易账号", "网点代码",'证件类型', "证件号码", "确认日期",
						"投资人类型", "投资人名称","业务类型","分红方式","返回代码",
						"备注","交易方式","投资者简称","经办人名称","经办人办公室电话","经办人传真号码",
						"申请流水号","确认流水号",'','' ],
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
					name : 'netPoint',
					index : 'netPoint',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'idtpName',
					index : 'idtpName',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'idno',
					index : 'idno',
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
					name : 'invtpName',
					index : 'invtpName',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'invnm',
					index : 'invnm',
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
					name : 'melonmdName',
					index : 'melonmdName',
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
					name : 'regioncode',
					index : 'regioncode',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					formatter:function(colValue){
						if(colValue=='9999')
							return '网上';
						if(colValue=='0001')
							return '网下';
						return '';
					}
				},{
					name : 'custabbrcode',
					index : 'custabbrcode',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'brokername',
					index : 'brokername',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'brokerofficetel',
					index : 'brokerofficetel',
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
					name : 'appno',
					index : 'appno',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'ackno',
					index : 'ackno',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					width : 200
				},{
					name : 'apkind',
					index : 'apkind',
					resizable : true,
					hidden : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'invname',
					index : 'invname',
					resizable : true,
					hidden : true,
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
				pager : "#history2ndAccInfoPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var ids = history2ndAccInfoList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = history2ndAccInfoList.jqGrid('getRowData', id);
						var ackno = rowData.ackno;
						var apkind = rowData.apkind;
						var invname = rowData.invname;
						var url = PRIMARY_PATH + "/getDsReportLink.xhtml?";
						//开始日期
						var startDate =  $("#startEnd").getDateRangeValue()[0];
						//结束日期
						var endDate = $("#startEnd").getDateRangeValue()[1];
						var be = '';
						if(apkind == '101'){
								be = "<a href= ' " + url 
										+ "permissionId=8818" 
										+ "&PI_ACKNO=" + ackno
										+ "&PI_ACKSDT=" + startDate
										+ "&PI_ACKEDT=" + endDate
										+ " ' style='color:blue;' target = '_blank' "  
										+ ">账户开户确认回单</a>";
						}else if(apkind == '102' || apkind == '109'){
								be = "<a href= ' " + url 
										+ "permissionId=8814" 
										+ "&PI_ACKNO=" + ackno
										+ "&PI_ACKSDT=" + startDate
										+ "&PI_ACKEDT=" + endDate
										+ " ' style='color:blue;' target = '_blank' "  
										+ ">账户销户确认回单</a>";
						}else if(apkind == '103'){
								be = "<a href= ' " + url 
										+ "permissionId=8816" 
										+ "&PI_ACKNO=" + ackno
										+ "&PI_ACKSDT=" + startDate
										+ "&PI_ACKEDT=" + endDate
										+ " ' style='color:blue;' target = '_blank' "  
										+ ">账户变更确认回单</a>";
						}else{
								be = "";
						}
						history2ndAccInfoList.jqGrid('setRowData', id, {
							aTag : be , custabbrcode : invname
						});
					}
				},
				subGrid : false
			});
	
		history2ndAccInfoList.navGrid('#history2ndAccInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
		history2ndAccInfoList.jqGrid('setFrozenColumns');
		jqGridResize(history2ndAccInfoList);
	};