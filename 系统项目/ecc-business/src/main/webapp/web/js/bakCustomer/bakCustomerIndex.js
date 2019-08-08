var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var bnkCustomerInfoList = $('#bnkCustomerInfoList');
$(function() {
	initGrid();
    WASP_WIDGET.registerResetClearEvent();
})
function initGrid() {
	bnkCustomerInfoList.jqGrid({
				url : PRIMARY_PATH + '/bakCustDtoListPage.xhtml',
				caption : '备案客户基本信息列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
		        postData :queryPostData(),
				colNames :[ "客户编号", "客户名称", "证件类型", "证件号码", "经办人名称", "经办人证件类型",
						"经办人证件号码",'操作'],
				colModel : [ {
					name : 'custno',
					index : 'custno',
					align : 'left',
					hidden : false,
					key : true,
					sortable : false
				}, {
					name : 'invnm',
					index : 'invnm',
					align : 'left',
					hidden : false,
					resizable : true,
					sortable : false
				}, {
					name : 'idtpnm',
					index : 'idtpnm',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'idno',
					index : 'idno',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'contact',
					index : 'contact',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'contidnm',
					index : 'contidnm',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'contidno',
					index : 'contidno',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'option',
					index : 'option',
					resizable : true,
					align : 'left',
					sortable : false
				}],
				rowNum :20,
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
				pager : "#bnkCustomerInfoPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var ids = bnkCustomerInfoList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = bnkCustomerInfoList.jqGrid('getRowData', id);
						
						var custno=rowData.custno;
						var be = '<a href="#" class="btn btn-link btn-jqgrid"  title="删除" disabled><i class="fa fa-trash-o"></i></a>';
						var se = '<a href="#" class="btn btn-link btn-jqgrid" title="修改" disabled><i class="fa fa-pencil-square-o"></i></a>';
						be = "<a href='javascript:void(0)' class='btn btn-link btn-jqgrid' onclick='delBakCust(\""
								+ custno
								+ "\")' title='删除'><i class='fa fa-trash-o'></i></a>";
						se = "<a href='javascript:void(0)' class='btn btn-link btn-jqgrid' onclick='" +
								"openEditDialog(\""
								+ custno
								+ "\")' title='修改'><i class='fa fa-pencil-square-o'></i></a>";
						bnkCustomerInfoList.jqGrid('setRowData', id, {
							option : se + be
						});
					}
				},
				subGrid : false
			});

	bnkCustomerInfoList.navGrid('#bnkCustomerInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	bnkCustomerInfoList.jqGrid('setFrozenColumns');
	jqGridResize(bnkCustomerInfoList);
};
// 对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function() {
	var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
	toggleFullScreen(document.documentElement);
	// 全屏的时候将几个模态框放到下面去
	$('.modal[role="dialog"]').appendTo($wrapper);
});

function queryByCondtion(flag){
	var postData = bnkCustomerInfoList.jqGrid("getGridParam", "postData");
    $.extend(postData,queryPostData());
    if (flag) {
    	bnkCustomerInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	bnkCustomerInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}
function queryPostData(){
	var postData={};
	postData.sp={};
	postData.sp.custno = $("#custno").val();
	return postData;
}



function delBakCust(custno){
	var baseInfo = {};
	baseInfo.custno = custno;
	baseInfo.permissionId = '8061';
	baseInfo.opertp = 'D';
	ctools.confirm({title:'确定要执行此操作吗?',text:'删除后无法恢复'},function(confirm){
		if(confirm){
			var data=baseInfo;
			var actionUrl=PRIMARY_PATH + "/delBakCustomer.xhtml";
			$.ajax({
				type: 'POST',
				url: actionUrl,
				dataType:'json',
				data:{
					"baseInfo" : JSON.stringify(baseInfo)
				},
				success: function(data){
					if(data.errcode=='0000'){
						swal('删除成功','','success');
						bnkCustomerInfoList.trigger("reloadGrid");
					}else{
						swal(data.errcode,data.errmsg,"error")
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
function openEditDialog(custno){
	var action= PRIMARY_PATH + "/openDialog.xhtml?method=update&custno="+custno;
	openDialog(action);
}