$(function(){
	$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
})

var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var operator = $("#operatorId").val();

var bnkCustomerCheckInfoList = $('#bnkCustomerCheckInfoList');
$(function() {
	initGrid();
    WASP_WIDGET.registerResetClearEvent();
})
function initGrid() {
	bnkCustomerCheckInfoList.jqGrid({
				url : PRIMARY_PATH + '/bakCustDtoCheckListPage.xhtml',
				caption : '备案客户复核信息列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
		        postData :queryPostData(),
				colNames :[ "客户编号", "客户名称", "证件类型", "证件号码", "经办人名称", "经办人证件类型",
						"经办人证件号码",'操作类型','复核状态',"操作",''],
				colModel : [{
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
					name : 'opertp',
					index : 'opertp',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'checkflag',
					index : 'checkflag',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'option',
					index : 'option',
					resizable : true,
					align : 'left',
					sortable : false
				},  {
					name : 'operatorId',
					index : 'operatorId',
					hidden : true,
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
				pager : "#bnkCustomerCheckInfoPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var ids = bnkCustomerCheckInfoList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = bnkCustomerCheckInfoList.jqGrid('getRowData', id);
						var opertp = rowData.opertp;
						var chkFlag = rowData.checkflag;
						var custno=rowData.custno;
						var operatorId = rowData.operatorId;
						if(operator == operatorId){
							var be = '<a href="javaScript:void(0);" class="btn btn-link btn-jqgrid" title = "无复核权限" disabled><i class="iconfont icon-wodeshenpi-copy"></i></a>';
						}else{
							var be = '<a href="javaScript:void(0);" class="btn btn-link btn-jqgrid" onclick="openCheckDialog(\''+custno+'\');" title = "复核" ><i class="iconfont icon-wodeshenpi-copy"></i></a>';
						}
						var checkflag = "";
				      	 if(chkFlag == 'Y'){
				      	 	checkflag = "复核通过";
				      	 }else if(chkFlag == 'N'){
				      	 	checkflag = "未复核";
				      	 }
				      	 var optp = "";
				      	 if(opertp == 'I'){
				      	 	optp = "新增";
				      	 }else if(opertp == 'U'){
				      	 	optp = "修改";
				      	 }else if(opertp == 'D'){
				      	 	optp = "删除";
				      	 }
						bnkCustomerCheckInfoList.jqGrid('setRowData', id, {
							option : be ,opertp : optp , checkflag : checkflag
						});
					}
				},
				subGrid : false
			});

	bnkCustomerCheckInfoList.navGrid('#bnkCustomerCheckInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	bnkCustomerCheckInfoList.jqGrid('setFrozenColumns');
	jqGridResize(bnkCustomerCheckInfoList);
};
// 对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function() {
	var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
	toggleFullScreen(document.documentElement);
	// 全屏的时候将几个模态框放到下面去
	$('.modal[role="dialog"]').appendTo($wrapper);
});

function queryByCondtion(flag){
	var postData = bnkCustomerCheckInfoList.jqGrid("getGridParam", "postData");
    $.extend(postData,queryPostData());
    if (flag) {
    	bnkCustomerCheckInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	bnkCustomerCheckInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}
function queryPostData(){
	var postData={};
	postData.sp={};
	postData.sp.custno = $("#custno").val();
	postData.sp.opertp = $("#opertp").val();
	postData.sp.checkflag = 'N';
	return postData;
}


function openCheckDialog(custno){
	var action= PRIMARY_PATH + "/openDialog.xhtml?method=check&custno=" + custno;
	openDialog(action);
}
