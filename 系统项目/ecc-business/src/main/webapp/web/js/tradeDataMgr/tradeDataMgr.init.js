var PRIMARY_PATH = "";
var BASE_PATH = "";

/*日期控件 单个日期选择  */
WASP_WIDGET.triggerDateStyleWithYMD("startDate");
WASP_WIDGET.triggerDateStyleWithYMD("endDate");

$(function(){
	$('.use-select2').select2({allowClear: false,minimumResultsForSearch:Infinity}); 
});

$(function() {
	var invnm = GetQueryString("invnm");
	if(!!invnm){
		$("#invnm").val(invnm);
	}
	var strDate = GetQueryString("strDate");
	if(!!strDate){
		$("#startDate").val(strDate);
	}
	
	var endDate = GetQueryString("endDate");
	if(!!endDate){
		$("#endDate").val(endDate);
	}
	initGrid();
	contractAppend();
})
//导出函数
function exportDetail(){
	//开始时间
	var startDate = $("#startDate").val();
	//结束时间
	var endDate = $("#endDate").val();
	//文件编号
	var fileno = $("#fileno").val();
	//客户名称
	var invnm = $("#invnm").val();
	//产品名称
	var fundnm = $("#fundnm").val();
	//交易金额
	var tradeAmt = $("#tradeAmt").val();
	//合同签署
	var contract = $("#contract").val();
	//是否移交
	var contractdevolve = $("#isChange").val();
	//是否原件
	var isOriginal = $("#isOriginal").val();
	//是否归档
	var isFiled = $("#isFiled").val();
	//导出表格
	var exportForm = $("#exportForm");
	
	var url = PRIMARY_PATH + "/exportTradeData.xhtml?";
	var params = 
			'sp[startDate]=' + startDate +
			'&sp[endDate]=' + endDate + 
			'&sp[fileno]=' + fileno + 
			'&sp[fundnm]=' + fundnm + 
			'&sp[contractsign]=' + contract + 
			'&sp[contractdevolve]=' + contractdevolve + 
			'&sp[subamt]=' + tradeAmt + 
			'&sp[filed]=' + isFiled + 
			'&sp[isoriginal]=' + isOriginal + 
			'&sp[invnm]=' + invnm;
	var actionUrl = url + params;
	exportForm.prop("action",actionUrl);
	exportForm.submit();
}


function resetQueryCondtion(){
	$("#startDate").val($("#hiddenStartDate").val());
	$("#endDate").val($("#hiddenEndDate").val());
	$("#fileno").val(null);
	$("#invnm").val(null);
	$("#fundnm").val(null);
	$("#tradeAmt").val(null);
	$("#contract").val(null);
	$("#isChange").val(null);
	$("#isOriginal").val(null);
	$("#isFiled").val(null);
	$("#contract").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isOriginal").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isChange").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isFiled").select2({allowClear: false,minimumResultsForSearch:Infinity});
}

function goTradeDataTab(invnm){
	var statrDate = $("#startDate").val();
	var endDate = $("#endDate").val();
	var info = {
			url: '#!'+BASE_PATH+'service/custDataManager/custDataManagerIndexView.do?invnm='+invnm+'&startDate='+startDate+'&endDate='+endDate,
            title: '客户资料管理'
		};
	window.parent.pubsub.publish("goPageTab",info);
} 

//查询
function queryByCondtion(flag){
    var postData = tradeDataInfoList.jqGrid("getGridParam", "postData");
    $.extend(postData,queryPostData());
    if (flag) {
    	tradeDataInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	tradeDataInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}




function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var tradeDataInfoList = $('#tradeDataInfoList');

function contractAppend(){
	var contract = $("#contract");
	$.ajax({
		url: PRIMARY_PATH + "/queryContractParameter.xhtml",
		type:"POST",
		data: 
		{
			'sp[pmst]' : 'SYSTEM',
			'sp[pmky]' : 'TRADESIGN', 
		},
		success: function(data){
			for(var i = 0; i < data.length; i++){
				var html = "<option value =" + data[i].pmco + ">" + data[i].pmnm  + "</option>";
				contract.append(html);
			}
		},
		dataType: 'json'
		}
	)
}

function initGrid() {
	tradeDataInfoList.jqGrid({
				url : PRIMARY_PATH + '/queryDsTradeInfo.xhtml',
				caption : '交易资料管理列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
				datatype : "json",
				postData : queryPostData(),
				mtype:"GET",
				colNames :[ "客户资料", "文件编号", "申请时间", "客户名称", "投资者类型", "产品名称",
						"交易金额","交易份额", "合同签署","合同是否移交",'合同是否原件','交易表单是否原件',
						'是否归档','二次风险揭示是否签署','录音文件编号','备注','操作',''],
				colModel : [ {
					name : 'aTag',
					index : 'aTag',
					align : 'left',
					hidden : false,
					key : true,
					sortable : false
				}, {
					name : 'fileno',
					index : 'fileno',
					align : 'left',
					hidden : false,
					resizable : true,
					sortable : false
				}, {
					name : 'apdt',
					index : 'apdt',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'invnm',
					index : 'invnm',
					align : 'left',
					resizable : true,
					sortable : false
				}, {
					name : 'invprtp',
					index : 'invprtp',
					resizable : true,
					align : 'left',
					sortable : false,
					formatter:function(colValue){
					if(colValue=='0')
						return '专业投资者';
					if(colValue=='1')
						return '普通投资者';
					return '';
					}
				}, {
					name : 'fundname',
					index : 'fundname',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'subamt',
					index : 'subamt',
					resizable : true,
					align : 'left',
					sortable : false
				}, {
					name : 'subquty',
					index : 'subquty',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'contractsignName',
					index : 'contractsignName',
					resizable : true,
					alisgn : 'left',
					sortable : false
					
				},{
					name : 'contractdevolve',
					index : 'contractdevolve',
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
					name : 'isoriginal',
					index : 'isoriginal',
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
					name : 'istradeform',
					index : 'istradeform',
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
					name : 'filed',
					index : 'filed',
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
					name : 'risksign',
					index : 'risksign',
					resizable : true,
					alisgn : 'left',
					sortable : false,
					formatter:function(colValue){
						if(colValue=='0')
							return '无需';
						if(colValue=='1')
							return '已签署';
						if(colValue=='2')
							return '未签署';
						return '';
						}
				},{
					name : 'voicerecord',
					index : 'voicerecord',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'remark',
					index : 'remark',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'option',
					index : 'option',
					resizable : true,
					alisgn : 'left',
					sortable : false
				},{
					name : 'serialno',
					index : 'serialno',
					hidden : true,
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
				//autowidth : true,
				shrinkToFit : true,
				autoScroll: true,
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
				pager : "#tradeDataInfoPage",
				viewrecords : true,
				hidegrid : false,
				gridComplete : function() {
					var ids = tradeDataInfoList.jqGrid('getDataIDs');
					for (var i = 0; i < ids.length; i++) {
						var id = ids[i];
						var rowData = tradeDataInfoList.jqGrid('getRowData', id);
						var invnm = rowData.invnm;
						var serialno=rowData.serialno;
						var se = '<a href="#" class="btn btn-link btn-jqgrid" title="修改" disabled><i class="fa fa-pencil-square-o"></i></a>';
						se = "<a href='javascript:void(0)' class='btn btn-link btn-jqgrid' onclick='" +
								"openEditDialog(\""
								+ serialno
								+ "\")' title='修改'><i class='fa fa-pencil-square-o'></i></a>";
						var be = "<a href='javascript:void(0)' class='btn btn-link btn-jqgrid' onclick='" +
						"goTradeDataTab(\""
						+ invnm
						+ "\")' title='修改'>客户资料</a>";
						tradeDataInfoList.jqGrid('setRowData', id, {
							option : se,
							aTag : be
						});
					}
				},
				subGrid : false
			});

	tradeDataInfoList.navGrid('#tradeDataInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	tradeDataInfoList.jqGrid('setFrozenColumns');
	jqGridResize(tradeDataInfoList);
};

function queryPostData(){
	
	var postData={};
	postData.sp={};
	postData.sp.startDate =  $("#startDate").val();
	postData.sp.endDate = $("#endDate").val();
	postData.sp.fileno = $("#fileno").val();
	postData.sp.invnm = $("#invnm").val();
	postData.sp.contractsign = $("#contract").val();
	postData.sp.contractdevolve = $("#isChange").val();
	postData.sp.fundnm = $("#fundnm").val();
	postData.sp.subamt = $("#tradeAmt").val();
	postData.sp.isoriginal = $("#isOriginal").val();
	postData.sp.filed = $("#isFiled").val();
	return postData;
}



function openEditDialog(serialno){
	var action= PRIMARY_PATH + "/openEditDialog.xhtml?method=update&serialno="+serialno;
	openDialog(action);
}