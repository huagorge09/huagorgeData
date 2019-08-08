var PRIMARY_PATH = "";
var BASE_PATH = "";



function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var castFailureInfoList = $('#castFailureInfoList');

/* 定投扣款失败表格分页 */
function initCastFailureInfoListGrid() {
	castFailureInfoList.jqGrid({
				//url : PRIMARY_PATH + '/queryCurrentAcc.xhtml',
				caption : '定投扣款失败信息列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				postData : queryPostData(),
				colNames :[ "银行名称", "银行代码", "错误笔数", "错误信息"],
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
				editurl : '',
				viewrecords : true,
				cellEdit : false,
				grouping : false,
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
				pager : "#castFailureInfoPage",
				viewrecords : true,
				hidegrid : false,
				subGrid : false
			});
	castFailureInfoList.navGrid('#castFailureInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	castFailureInfoList.jqGrid('setFrozenColumns');
	jqGridResize(castFailureInfoList);
};
