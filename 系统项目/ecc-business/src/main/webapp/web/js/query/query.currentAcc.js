var PRIMARY_PATH = "";
var BASE_PATH = "";



function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var currentAccInfoList = $('#currentAccInfoList');

/* 当前账户表格分页 */
function initCurrentAccInfoListGrid() {
	currentAccInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryCurrentAcc.xhtml',
				caption : '当前账户申请流水列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				colNames :[ "报表功能", "基金账号", "交易账号", "网点代码", "业务类型", "申请日期",
						"申请时间","投资人类型", "投资人姓名","证件类型","证件号码","手机号",
						"地址","邮政编码","电子信箱","传真","对账单寄送选择方式","分红方式",
						"交易方式","投资者简称","经办人名称","经办人办公室电话","经办人传真号码","是否有效",
						"操作员代码","复核员","申请流水号","申请编号",'',''],
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
					name : 'apkindName',
					index : 'apkindName',
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
					name : 'aptm',
					index : 'aptm',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
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
					name : 'idtpName',
					index : 'idtpName',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'idno',
					index : 'idno',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'mobileno',
					index : 'mobileno',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'addr',
					index : 'addr',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'postcode',
					index : 'postcode',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'email',
					index : 'email',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'faxno',
					index : 'faxno',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'delivertype',
					index : 'delivertype',
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
					name : 'custsimpnm',
					index : 'custsimpnm',
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
					name : 'officetel',
					index : 'officetel',
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
					sortable : false,
				},{
					name : 'operatorcode',
					index : 'operatorcode',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'checker',
					index : 'checker',
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
					width : 200
				},{
					name : 'apkind',
					index : 'apkind',
					hidden : true,
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'invname',
					index : 'invname',
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
				pager : "#currentAccInfoPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var ids = currentAccInfoList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = currentAccInfoList.jqGrid('getRowData', id);
						var apkind = rowData.apkind;
						var appno = rowData.appno;
						var invname = rowData.invname;
						var appDate = $("#appDate").val();
						var be = '';
						var url = PRIMARY_PATH + "/getDsReportLink.xhtml?";
						if(apkind == '001'){
								be = "<a href= ' " + url + "permissionId=8817" 
										+ "&PI_APPNO=" + appno
										+ "&PI_APSDT=" + appDate
										+ "&PI_APEDT=" + appDate
										+ "' style='color:blue;' target = '_blank' "  
										+ ">账户开户受理回单</a>";
						}else if(apkind == '002' || apkind == '009'){
								be = "<a href= ' " + url + "permissionId=8813" 
										+ "&PI_APPNO=" + appno
										+ "&PI_APSDT=" + appDate
										+ "&PI_APEDT=" + appDate
										+ " ' style='color:blue;' target = '_blank' "  
										+ ">账户销户受理回单</a>";
						}else if(apkind == '003' || apkind == '0B1'){
								be = "<a href= ' " + url + "permissionId=8815" 
										+ "&PI_APPNO=" + appno
										+ "&PI_APSDT=" + appDate
										+ "&PI_APEDT=" + appDate
										+ " ' style='color:blue;' target = '_blank' "  
										+ ">账户变更受理回单</a>";
						}else{
								be = "";
						}
						currentAccInfoList.jqGrid('setRowData', id, {
							aTag : be , custsimpnm : invname
						});
					}
				},
				subGrid : false
			});

	currentAccInfoList.navGrid('#currentAccInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	currentAccInfoList.jqGrid('setFrozenColumns');
	jqGridResize(currentAccInfoList);

};
