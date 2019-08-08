var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var parameterList = $('#parameterList');
$(function() {
	initGrid();
	WASP_WIDGET.registerResetClearEvent();
})

function initGrid() {
	parameterList
			.jqGrid({
				url :  PRIMARY_PATH + '/businParameterListPage.xhtml',
				mtype:"GET",
				caption : '参数列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				colNames : [ "参数类型","参数类型", "参数",  "操作" ],
				colModel : [
				 {
					name : 'pmst',
					index : 'pmst',
					hidden : true,
					sortable : false
				},{
					name : 'pmnm',
					index : 'pmnm',
					sortable : false
				}, {
					name : 'pmky',
					index : 'pmky',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'option',
					index : 'option',
					resizable : true,
					alisgn : 'left',
					sortable : false
				} ],
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
				pager : "#parameterPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var operatorId = $('#operatorId').val();
					var ids = parameterList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = parameterList.jqGrid('getRowData', id);
					    var pmky=rowData.pmky;
					    var pmst=rowData.pmst;
					    var pmnm=rowData.pmnm;
					    
						var se = '<a date-funcode="'
									+ pmky
									+ '" href="#" class="btn btn-link btn-jqgrid" title="新增" data-toggle="modal" onclick="openSmallAddDialog(\''
									+ pmst
									+ '\',\''
									+ pmky
									+ '\',\''
									+ pmnm
									+ '\')"><i class="fa fa-plus-circle"></i></a>';
						var be = '<a date-startupid="'
								+ id
								+ '" href="#" class="btn btn-link btn-jqgrid" title="点击展开参数列表" data-toggle="modal" onclick="expandSpecialMatter(\''
								+ id
								+ '\');" data-target="#modal-info"><i class="fa fa-eject fa-rotate-180"></i></a>'
						parameterList.jqGrid('setRowData', id, {
							option : be + se
						});
					}
				},
				subGrid : false,
				subGrid : true,
				subGridRowExpanded : subGridRowExpanded,
				subGridRowColapsed : function(subgrid_id, row_id) {
					$("#subGridTBId").val("");
				},
				ondblClickRow : function(startupId) {
					expandSpecialMatter(startupId);
				}
			});

	parameterList.navGrid('#parameterPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	parameterList.jqGrid('setFrozenColumns');
	jqGridResize(parameterList);
};

// 点击展开特殊事项列表
function expandSpecialMatter(rowid) {
	var ids = parameterList.jqGrid('getDataIDs');
	for (var i = 0; i < ids.length; i++) {
		var id = ids[i];
		if (id == rowid) {
			parameterList.jqGrid('toggleSubGridRow', id);
		} else {
			parameterList.jqGrid('collapseSubGridRow', id);
		}
	}
}
/* 特殊事项列 */
function subGridRowExpanded(subgrid_id, row_id) {
	var rowData = parameterList.jqGrid('getRowData', row_id);
	var pmky=rowData.pmky;
	var pmst=rowData.pmst;
	var pmnm=rowData.pmnm;
	var subgrid_table_id = subgrid_id + "_t";
	$("#subGridTBId").val(subgrid_table_id);
	var pager_id = "p_" + subgrid_table_id;
	var qpmco = trimString($('#qpmco').val());
	var qpmnm = trimString($('#qpmnm').val());
	$("#" + subgrid_id).html(
			"<table id='" + subgrid_table_id
					+ "' class='scroll'></table><div id='" + pager_id
					+ "' class='scroll'></div>");
	var parameter = $("#" + subgrid_table_id);
	parameter.jqGrid({
				caption :'当前参数类型:'+pmnm,
				url : PRIMARY_PATH + '/getParameter.xhtml',
				mtype:'GET',
				datatype : "json",
				postData : {
					'sp[pmst]' : pmst,
					'sp[pmky]' : pmky,
					'sp[pmco]' : qpmco,
					'sp[pmnm]' : qpmnm
				},
				colNames : [ "参数键","参数类型","参数名", "参数值",  "操作" ],
				colModel : [ {
					name : 'pmky',
					index : 'pmky',
					hidden : true,
					sortable : false
				},{
					name : 'pmst',
					index : 'pmst',
					hidden : true,
					sortable : false
				},{
					name : 'pmnm',
					index : 'pmnm',
					sortable : false
				}, {
					name : 'pmco',
					index : 'pmco',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'option',
					index : 'option',
					resizable : true,
					alisgn : 'left',
					sortable : false
				} ],
				rowNum : 20,
				rowList : [ 10, 20, 30 ],
				rownumbers : true,
				rownumWidth : 50,
				prmNames : {
					search : "search",
					page : "pageNo",
					rows : "limit"
				},
				sortname : 'affairId',
				autowidth : true,
				height : 'auto',
				width : false,
				jsonReader : {
					root : "items", // 结果集
					records : "total", // 总记录数
					total : "pageCount", // 总页数
					page : "pageNo", // 当前页
					repeatitems : false
				// (4)
				},
				pager : "#" + pager_id,
				viewrecords : true,
				rownumbers : true,
				gridComplete : function() {
					var ids = parameter.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = parameter.jqGrid('getRowData', id);
						var ppmco=rowData.pmco;
						var ppmnm=rowData.pmnm;	
						
						var se = '<a href="javascript:void(0)"  class="btn btn-link btn-jqgrid" onclick="delParameter(\''
								+ pmst
								+ '\',\''
								+ pmky
								+ '\',\''
								+ ppmco
								+ '\',\''
								+ subgrid_table_id
								+ '\')" title="删除参数" ><i class="fa fa-trash-o"></i></a>'
						var be = '<a href="javascript:void(0)" class="btn btn-link btn-jqgrid" onclick="openSmallUpdateDialog(\''
									+ pmst
									+ '\',\''
									+ pmky
									+ '\',\''
									+ pmnm
									+ '\',\''
									+ ppmnm
									+ '\',\''
									+ ppmco
									+ '\')" title="修改"><i class="fa fa-pencil-square-o"></i></a>';
						parameter.jqGrid('setRowData', id, {
							option : se+be
						});
					}

				}
			});

	parameter.navGrid('#' + pager_id, {
		edit : false,
		add : false,
		del : false,
		search : false
	});
}

// 对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function() {
	var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
	toggleFullScreen(document.documentElement);
	// 全屏的时候将几个模态框放到下面去
	$('.modal[role="dialog"]').appendTo($wrapper);
});

// 取消订单
function cancelFundOrder(apmst,apmky,delpmco,subgrid_table_id) {
	var param = {
		'pmst' : apmst,
		'pmky' : apmky,
		'pmco' : delpmco
	}
	$.ajax({
		type : 'POST',
		url : PRIMARY_PATH + "/del.xhtml",
		datatype : 'json',
		data : param,
		success : function(data) {
			var parameter = $("#" + subgrid_table_id);
			if (data.ResultCode != '0000') {
				swal(data.ResultCode, data.ResultDesc, "error");
				parameter.trigger("reloadGrid");
				return;
			}
			swal("删除成功!",'',"success");
			parameter.trigger("reloadGrid");
		},
		error : function(xhr) {
			switch (xhr.status) {
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
		}
	});
}

/**
 * 初始化删除立项事件
 */
function delParameter(apmst,apmky,delpmco,subgrid_table_id) {
	swal({
		title : "确定要删除该参数吗?",
		text : "删除后不可恢复!",
		type : "warning",
		showCancelButton : true,
		confirmButtonColor : "#fc6821",
		confirmButtonText : "确定",
		cancelButtonText : "取消",
		closeOnConfirm : false,
		closeOnCancel :   true
	}, function(isConfirm) {
		if (isConfirm) {
			cancelFundOrder(apmst,apmky,delpmco,subgrid_table_id);
		}
	});
}

function queryByCondtion(flag) {
	var allPmky = trimString($("#allPmky").val());
	var allPmnm = trimString($('#allPmnm').val());
	var qpmco = trimString($('#qpmco').val());
	var qpmnm = trimString($('#qpmnm').val());
	var postData = parameterList.jqGrid("getGridParam", "postData");
	$.extend(postData, {
		'sp[allPmky]' : allPmky,
		'sp[allPmnm]' : allPmnm,
		'sp[qpmco]' : qpmco,
		'sp[qpmnm]' : qpmnm
	});
	if (flag) {
		parameterList.trigger("reloadGrid", [ {
			page : 1
		} ]); // 重新载入Grid表格
	} else {
		parameterList.trigger("reloadGrid"); // 重新载入Grid表格
	}
};
function querySubByCondtion(flag,sub_gridid){
	var allPmky = trimString($("#allPmky").val());
	var allPmnm = trimString($('#allPmnm').val());
	var qpmco = trimString($('#qpmco').val());
	var qpmnm = trimString($('#qpmnm').val());
	var postData = $('#sub_gridid').jqGrid("getGridParam", "postData");
	$.extend(postData, {
		'sp[pmco]' : qpmco,
		'sp[pmnm]' : qpmnm
	});
	$('#sub_gridid').trigger("reloadGrid");
}

function openSmallAddDialog(pmst,pmky,pmnm){
	var actionUrl=PRIMARY_PATH + "/openDialog.xhtml?method=add&pmst="+pmst+"&pmky="+pmky+"&pmnm="+pmnm;
	openSmallDialog(actionUrl);
}
function openSmallUpdateDialog(pmst,pmky,pmnm,ppmnm,pmco){
	var actionUrl=PRIMARY_PATH + "/openDialog.xhtml?method=update&pmst="+pmst+"&pmky="+pmky+"&pmco="+pmco+"&pmnm="+pmnm+"&ppmnm="+ppmnm;
		openSmallDialog(actionUrl);
}
