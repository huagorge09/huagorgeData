var PRIMARY_PATH = "";
var BASE_PATH = "";



function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var accountInfoList = $('#accountInfoList');

/* 账户信息表格分页 */
function initAccountInfoListGrid() {
	accountInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryFundCustInfo.xhtml',
				caption : '账户信息列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				colNames :[ "银行名称", "基金账号", "交易账号",'网点代码',"客户类型", "证件类型", "证件号码",
						"投资者类型","投资者姓名", "分红方式","交易账户状态","基金账户状态","开户日期"],
				colModel : [ {
					name : 'bankname',
					index : 'bankName',
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
					name : 'custtpName',
					index : 'custtpName',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'idtpName',
					index : 'idtpName',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'idno',
					index : 'idno',
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
					name : 'melonmdName',
					index : 'melonmdName',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'tradeaccost',
					index : 'tradeaccost',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					formatter:function(colValue){
						if(colValue =='Y'){
							return '正常';
						}else if(colValue == 'N'){
							return '无效';
						}else{
							return '';
						}
						
					}
				},{
					name : 'fdacstName',
					index : 'fdacstName',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'opendt',
					index : 'opendt',
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
				pager : "#accountInfoPage",
				viewrecords : true,
				hidegrid : false,
				subGrid : false
			});

	accountInfoList.navGrid('#accountInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	accountInfoList.jqGrid('setFrozenColumns');
	jqGridResize(accountInfoList);

};
