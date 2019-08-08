var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var payChannelList = $('#payChannelList');
$(function() {
	initGrid();
	WASP_WIDGET.triggerSelectOnBankBase('selBankNm');
	WASP_WIDGET.initializeSelectVal("selBankNm",'007','招商银行');
	WASP_WIDGET.registerResetClearEvent();
})
function initGrid() {
	payChannelList.jqGrid({
				url :PRIMARY_PATH +  '/paychannel.xhtml',
				postData:{'sp[thirdChannel]':'007'},	
				caption : '支付渠道列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				mtype:"GET",
				colNames :[ "支付渠道代码", "支持银行代码", "银行名称", "第三方支付银行代码", "开户演示连接", "开户注意事项连接",
						"扣款方式","状态", "是否支持定投","操作" ],
				colModel : [ {
					name : 'thirdChannel',
					index : 'thirdChannel',
					align : 'left',
					hidden : false,
					sortable : false
				}, {
					name : 'bankNO',
					index : 'bankNO',
					align : 'left',
					hidden : false,
					resizable : true,
					sortable : false
				}, {
					name : 'bankName',
					index : 'bankName',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'thirdBankNO',
					index : 'thirdBankNO',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'openDemo',
					index : 'openDemo',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'openObseRev',
					index : 'openObseRev',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'payMode',
					index : 'payMode',
					resizable : true,
					align : 'left',
					sortable : false,
					formatter:function(colValue){
						if(colValue=='0')
							return 'B2B委托代扣';
						if(colValue=='1')
							return 'B2C网银支付';
						return '';
					}
				}, {
					name : 'status',
					index : 'status',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					formatter:function(colValue){
						if(colValue=='Y')
							return '有效';
						if(colValue=='N')
							return '无效';
						return '';
					}
				},{
					name : 'mipFlag',
					index : 'mipFlag',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					formatter:function(colValue){
						if(colValue=='Y')
							return '是';
						if(colValue=='N')
							return '否';
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
								window.location.href = '/mecc/login.jsp';
							}
						})
						return;
					}
				},
				pager : "#payChannelPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var ids = payChannelList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = payChannelList.jqGrid('getRowData', id);
						
						var thirdChannel=rowData.thirdChannel;
						var bankNO=rowData.bankNO;
						var be = '<a href="#" class="btn btn-link btn-jqgrid"  title="删除" disabled><i class="fa fa-trash-o"></i></a>';
						var se = '<a href="#" class="btn btn-link btn-jqgrid" title="修改" disabled><i class="fa fa-pencil-square-o"></i></a>';
						var ze = '<a href="#" class="btn btn-link btn-jqgrid" title="新建系列产品" disabled><i class="fa fa-plus-circle"></i></a>';
						be = "<a href='javascript:void(0)' class='btn btn-link btn-jqgrid' onclick='delBankBase(\""
								+ bankNO
								+ "\",\""
								+ thirdChannel
								+ "\")' title='删除'><i class='fa fa-trash-o'></i></a>";
						se = "<a href='javascript:void(0)' class='btn btn-link btn-jqgrid' onclick='" +
								"openEditDialog(\""
								+ bankNO
								+ "\",\""
								+ thirdChannel
								+ "\")' title='修改'><i class='fa fa-pencil-square-o'></i></a>";
						payChannelList.jqGrid('setRowData', id, {
							option : be + se
						});
					}
				},
				subGrid : false
			});

	payChannelList.navGrid('#payChannelPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	payChannelList.jqGrid('setFrozenColumns');
	jqGridResize(payChannelList);
};
// 对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function() {
	var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
	toggleFullScreen(document.documentElement);
	// 全屏的时候将几个模态框放到下面去
	$('.modal[role="dialog"]').appendTo($wrapper);
});

function queryByCondtion(flag){
	payChannelList.trigger("reloadGrid");
}
function queryByCondtion(flag) {
    var thirdChannel = $("#selBankNm").val();
    var postData = payChannelList.jqGrid("getGridParam", "postData");
    $.extend(postData, {
        'sp[thirdChannel]':thirdChannel
    });
    if (flag) {
    	payChannelList.trigger("reloadGrid", [{ page: 1 }]); //重新载入Grid表格
    } else {
    	payChannelList.trigger("reloadGrid"); //重新载入Grid表格
    }
};

function openAddDialog(){
	var action= PRIMARY_PATH + "/openDialog.xhtml?method=add";
	openDialog(action);
}

function openEditDialog(bankNO,thirdChannel){
	var action= PRIMARY_PATH + "/openDialog.xhtml?method=update&bankNO="+bankNO+"&thirdChannel="+thirdChannel;
	openDialog(action);
}


function delBankBase(bankNO,thirdChannel){
	ctools.confirm({title:'确定要执行此操作吗?',text:'删除后无法恢复'},function(confirm){
		if(confirm){
			var data={
					'bankNO':bankNO,
					'thirdChannel':thirdChannel
			}
			var actionUrl= PRIMARY_PATH + "/del.xhtml";
			$.ajax({
				type: 'POST',
				url: actionUrl,
				dataType:'json',
				data:data,
				success: function(data){
					if(data.ResultCode=='0000'){
						swal('删除成功','','success');
						payChannelList.trigger("reloadGrid");
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