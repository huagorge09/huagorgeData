
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var TAAccInfoList = $('#TAAccInfoList');

/*TA账户查询分页 */

function initTAAccInfoListGrid() {
	TAAccInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryTAAcc.xhtml',
				caption : 'TA账户确认流水列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				colNames :[ "确认流水号", "申请流水号", "基金账号", "申请编号",'网点代码', "交易账号", "确认日期",
						"返回代码","业务类型", "投资者类型","投资者名称","证件类型","证件号码",
						"分红方式","备注"],
				colModel : [ {
					name : 'ackno',
					index : 'ackno',
					align : 'left',
					hidden : false,
					key : true,
					sortable : false
				}, {
					name : 'serialno',
					index : 'serialno',
					align : 'left',
					hidden : false,
					resizable : true,
					sortable : false
				}, {
					name : 'fundacct',
					index : 'fundacct',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'appno',
					index : 'appno',
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
					name : 'tradeacco',
					index : 'tradeacco',
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
					name : 'retcode',
					index : 'retcode',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
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
					name : 'melonmdName',
					index : 'melonmdName',
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
				pager : "#TAAccInfoPage",
				viewrecords : true,
				hidegrid : false,
				subGrid : false
			});

	TAAccInfoList.navGrid('#TAAccInfoListPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	TAAccInfoList.jqGrid('setFrozenColumns');
	jqGridResize(TAAccInfoList);

};


