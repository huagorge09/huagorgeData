var PRIMARY_PATH = "";
var BASE_PATH = "";



function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var fundClearInfoList = $('#fundClearInfoList');

/*基金净值表格分页 */
function initFundClearInfoListGrid() {
	fundClearInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryFundNav.xhtml',
				caption : '基金净值列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				colNames :[ "基金代码", "基金名称", "基金净值", "净值日期"],
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
					name : 'nav',
					index : 'nav',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'navdt',
					index : 'navdt',
					align : 'left',
					resizable : true,
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
				autowidth : true,
				//shrinkToFit : false,
				editurl : '',
				viewrecords : true,
				cellEdit : false,
				grouping : false,
				//autoScroll: true,
				jsonReader : {
					root : "items", // 结果集
					records : "total", // 总记录数
					total : "pageCount", // 总页数
					page : "pageNo", // 当前页
					repeatitems : false
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
				pager : "#fundClearInfoPage",
				viewrecords : true,
				hidegrid : false,
				subGrid : false
			});

	fundClearInfoList.navGrid('#fundClearInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	fundClearInfoList.jqGrid('setFrozenColumns');
	jqGridResize(fundClearInfoList);

};
