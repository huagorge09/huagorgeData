var PRIMARY_PATH = "";
var BASE_PATH = "";



function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var fundLastInfoList = $('#fundLastInfoList');

/* 基金余额表格分页 */
function initFundLastInfoListGrid() {
	fundLastInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryFundBalance.xhtml',
				caption : '基金余额列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				colNames :[ "基金代码", "基金名称", "银行代码",'网点代码', "基金账号", "交易账号", "投资者姓名",
						"实际份额","可用份额", "未上传申请冻结份额","已上传申请的冻结份额","异常冻结份额","基金净值"],
				colModel : [ {
					name : 'fundid',
					index : 'fundid',
					align : 'left',
					hidden : false,
					key : true,
					sortable : false
				}, {
					name : 'fundnm',
					index : 'fundnm',
					align : 'left',
					hidden : false,
					resizable : true,
					sortable : false
				}, {
					name : 'bankno',
					index : 'bankno',
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
					name : 'fundacct',
					index : 'fundacct',
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
					name : 'invnm',
					index : 'invnm',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'balance',
					index : 'balance',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'available',
					index : 'available',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'frozen',
					index : 'frozen',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'hfrozen',
					index : 'hfrozen',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'abnmfrozen',
					index : 'abnmfrozen',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'nav',
					index : 'nav',
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
				pager : "#fundLastInfoPage",
				viewrecords : true,
				hidegrid : false,
				
				subGrid : false
			});

	fundLastInfoList.navGrid('#fundLastInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	fundLastInfoList.jqGrid('setFrozenColumns');
	jqGridResize(fundLastInfoList);
};
