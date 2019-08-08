var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var bankBaseInfoList = $('#bankBaseInfoList');
$(function() {
	initGrid();
    WASP_WIDGET.registerResetClearEvent();
})
function initGrid() {
	bankBaseInfoList.jqGrid({
				url : PRIMARY_PATH + '/baseinfo.xhtml',
				caption : '基本信息列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
		        postData :queryPostData(),
				colNames :[ "银行代码", "银行名称", "电话银行", "联系人", "联系人电话", "联系人电子邮件",
						"展示顺序","展示标志", "状态","操作" ],
				colModel : [ {
					name : 'bnkNo',
					index : 'bnkNo',
					align : 'left',
					hidden : false,
					key : true,
					sortable : false
				}, {
					name : 'bnkNm',
					index : 'bnkNm',
					align : 'left',
					hidden : false,
					resizable : true,
					sortable : false
				}, {
					name : 'telBnk',
					index : 'telBnk',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'linkMan',
					index : 'linkMan',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'linkManTel',
					index : 'linkManTel',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'linkManEmail',
					index : 'linkManEmail',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'disOrder',
					index : 'disOrder',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'disFlg',
					index : 'disFlg',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					formatter:function(colValue){
						if(colValue=='Y')
							return '展示';
						if(colValue=='N')
							return '不展示';
						return '';
					}
				},{
					name : 'status',
					index : 'status',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					formatter:function(colValue){
						if(colValue=='Y')
							return '正常';
						if(colValue=='N')
							return '禁用';
						return '';
					}
				},{
					name : 'option',
					index : 'option',
					resizable : true,
					alisgn : 'left',
					sortable : false
				}],
				rowNum : 20,
				rowList : [ 20, 30, 50 ],
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
				shrinkToFit : true,
				editurl : '',
				viewrecords : true,
				cellEdit : false,
				shrinkToFit : true,
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
				pager : "#bankBaseInfoPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var ids = bankBaseInfoList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = bankBaseInfoList.jqGrid('getRowData', id);
						
						var bnkNo=rowData.bnkNo;
						var be = '<a href="#" class="btn btn-link btn-jqgrid"  title="删除" disabled><i class="fa fa-trash-o"></i></a>';
						var se = '<a href="#" class="btn btn-link btn-jqgrid" title="修改" disabled><i class="fa fa-pencil-square-o"></i></a>';
						var ze = '<a href="#" class="btn btn-link btn-jqgrid" title="新建系列产品" disabled><i class="fa fa-plus-circle"></i></a>';
						be = "<a href='javascript:void(0)' class='btn btn-link btn-jqgrid' onclick='delBankBase(\""
								+ bnkNo
								+ "\")' title='删除'><i class='fa fa-trash-o'></i></a>";
						se = "<a href='javascript:void(0)' class='btn btn-link btn-jqgrid' onclick='" +
								"openEditDialog(\""
								+ bnkNo
								+ "\")' title='修改'><i class='fa fa-pencil-square-o'></i></a>";
						bankBaseInfoList.jqGrid('setRowData', id, {
							option : be + se
						});
					}
				},
				subGrid : false
			});

	bankBaseInfoList.navGrid('#bankBaseInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	bankBaseInfoList.jqGrid('setFrozenColumns');
	jqGridResize(bankBaseInfoList);
};
// 对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function() {
	var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
	toggleFullScreen(document.documentElement);
	// 全屏的时候将几个模态框放到下面去
	$('.modal[role="dialog"]').appendTo($wrapper);
});

/*function queryByCondtion(flag){
	bankBaseInfoList.trigger("reloadGrid");
}*/

function queryByCondtion(flag){
	var postData = bankBaseInfoList.jqGrid("getGridParam", "postData");
    $.extend(postData,queryPostData());
    if (flag) {
    	bankBaseInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	bankBaseInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}
function queryPostData(){
	var postData={};
	postData.sp={};
	postData.sp.bnkNo = $("#bankno").val();
	postData.sp.bankName = $("#banknm").val();
	return postData;
}


function delBankBase(bnkNo){
	ctools.confirm({title:'确定要执行此操作吗?',text:'删除后无法恢复'},function(confirm){
		if(confirm){
			var data={
					'bnkNo':bnkNo
			}
			var actionUrl=PRIMARY_PATH + "/del.xhtml";
			$.ajax({
				type: 'POST',
				url: actionUrl,
				dataType:'json',
				data:data,
				success: function(data){
					if(data.ResultCode=='0000'){
						swal('删除成功','','success');
						bankBaseInfoList.trigger("reloadGrid");
					}else{
						swal(data.ResultCode,data.ResultDesc,"error")
					}
				},
				error:function(xhr){
					switch(xhr.status){
						case 403:sweetAlert("对不起，您无此权限！","","error");break;
						case 404:sweetAlert("对不起，无此页面！", "","error");break;
						case 500:sweetAlert("内部错误，请联系管理员！","","error");break;  
						case 504:sweetAlert("超时，请联系管理员！", "","error");break;  
						case 417:sweetAlert("内部错误，请联系管理员！", "","error");break;  
					}
				}
			});
		}
	})
}

function openAddDialog(){
	var action= PRIMARY_PATH + "/openDialog.xhtml?method=add";
	openDialog(action);
}
function openEditDialog(bnkNo){
	var action= PRIMARY_PATH + "/openDialog.xhtml?method=update&bnkNo="+bnkNo;
	openDialog(action);
}