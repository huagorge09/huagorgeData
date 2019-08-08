/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";
var $instFeeSetList = $('#instFeeSetList');

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	initGrid();
	initWidget();
});

var instFeeFunc = {
		addInstFeeSetView : function(){
			openDialog(PRIMARY_PATH+"/instFeeSetAddView.do");
		},
		checkInstFeeSetView : function(serialNo){
			openDialog(PRIMARY_PATH+"/instFeeSetCheckView.do?serialNo="+serialNo);
		},
		updateInstFeeSetView : function(serialNo){
			openDialog(PRIMARY_PATH+"/instFeeSetUpdateView.do?serialNo="+serialNo);
		},
		detailInstFeeSetView : function(serialNo){
			openDialog(PRIMARY_PATH+"/instFeeSetDetailView.do?serialNo="+serialNo);
		},
		delInstFeeSetInfo : function (serialNo){
			ctools.confirm("确认删除该数据？",function(isConfirm){
				$.ajax({
			    	async:false,
					url:PRIMARY_PATH+'/delInstFeeSetInfo.do',
					type:"post",
					dataType:'json',
					data:{
						"serialNo" : serialNo
					},
					error:function(a,b,c){
						swal("调用失败!", "", "error");
					},
					success:function(data, textStatus){
						if(!!data && "0000" == data.resultCode){
							swal("删除成功!", "", "success");
							queryByCondtion(false);
						}else{
							swal(data.resultMsg, "", "success");
						}
					}
				});
			});
		}
};

function initGrid(){
	$instFeeSetList.jqGrid({
		url: PRIMARY_PATH+'/instFeeSetListPage.do',
        caption: '财富柜台费率管理列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "json",
        colNames: ["流水号","机构类型Id","机构类型","基金名称id","基金名称","业务类型id","业务类型","起始金额","结束金额","折扣率","状态id","状态","复核状态id","复核状态","创建人id","操作"],
        colModel: [
            { name: 'serialNo', index: 'serialNo',  hidden: true, key: true, sortable: false},
            { name: 'instType', index: 'instType',  hidden: true, key: false, sortable: false},
            { name: 'instTypeName', index: 'instTypeName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'fundId', index: 'fundId',  hidden: true, key: false, sortable: false},
            { name: 'fundName', index: 'fundName', width:200, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'apkind', index: 'apkind',  hidden: true, key: false, sortable: false},
            { name: 'apkindName', index: 'apkindName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'strAmt', index: 'strAmt', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'endAmt', index: 'endAmt', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'discount', index: 'discount', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'status', index: 'status',  hidden: true, key: false, sortable: false},
            { name: 'statusName', index: 'statusName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'checkStatus', index: 'checkStatus',  hidden: true, key: false, sortable: false},
            { name: 'checkStatusName', index: 'checkStatusName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'createId', index: 'createId',  hidden: true, key: false, sortable: false},
            { name: 'option', index:'option',width:100, align:'left', resizable:true, resizable: true, sortable: false }
        ],
        rowNum: 20,
        rowList: [10, 20, 30, 50],
        rownumbers: true,
        rownumWidth: 50,
        prmNames: {search:"search",page:"pageNo",rows:"limit"},
        height: 'auto',
        width: false,
        autowidth:true,
        shrinkToFit: true,
        editurl: '',
        viewrecords: true,
        cellEdit: false,
        grouping: false,
        jsonReader: {
            root: "items", //结果集
            records: "total", //总记录数 
            total: "pageCount", //总页数
            page: "pageNo", //当前页 
            repeatitems: false // (4) 
        },
        multiselect: false,
        pager: "#instFeeSetPage",
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete: function() {
			var ids = $instFeeSetList.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var id = ids[i];
				var rowData = $instFeeSetList.jqGrid('getRowData', id);	
				var serialNo = rowData.serialNo;
				var status = rowData.status;
				var checkStatus = rowData.checkStatus;
				var createId = rowData.createId;
				var currEmpId = $("input[name=currEmpId]").val();
				//按钮置灰
				$(".permissionBtn a").attr("disabled", "disabled");
                $(".permissionBtn a").removeAttr("onclick");
                //instFeeInfoBtn，instFeeUpdateBtn，instFeeCheckBtn，instFeeDelBtn
                
                //初始化 费率管理 修改 删除
                
                $("#instFeeInfoBtn a").removeAttr("disabled");
            	$("#instFeeInfoBtn a").attr("onClick", "instFeeFunc.detailInstFeeSetView('"+serialNo+"');");
            	
            	if("N" != checkStatus || "Y" != status){
            		$("#instFeeUpdateBtn a").removeAttr("disabled");
            		$("#instFeeUpdateBtn a").attr("onClick", "instFeeFunc.updateInstFeeSetView('"+serialNo+"');");
            		
            		$("#instFeeDelBtn a").removeAttr("disabled");
            		$("#instFeeDelBtn a").attr("onClick", "instFeeFunc.delInstFeeSetInfo('"+serialNo+"');");
            	}
            	
            	if("N" == checkStatus && createId != currEmpId && "Y" == status){
            		$("#instFeeCheckBtn a").removeAttr("disabled");
            		$("#instFeeCheckBtn a").attr("onClick", "instFeeFunc.checkInstFeeSetView('"+serialNo+"');");
            	}
            	
            	var ifib = $("#instFeeInfoBtn").html();
            	var ifub = $("#instFeeUpdateBtn").html();
            	var ifcb = $("#instFeeCheckBtn").html();
            	var ifdb = $("#instFeeDelBtn").html();
            	
            	$instFeeSetList.jqGrid('setRowData',ids[i],{option: ifib+ifub+ifcb+ifdb });
			}
		}
    });

	$instFeeSetList.navGrid('#instFeeSetPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
	$instFeeSetList.jqGrid('setFrozenColumns');
    jqGridResize($instFeeSetList);
}

function initWidget(){
	WASP_WIDGET.triggerParamListSelect("q-instType",false,"机构类型");
	WASP_WIDGET.triggerFundInfoListSelect("q-fundId",false,"基金名称");
	//WASP_WIDGET.triggerParamListSelect("q-apkind",false,"业务类型");
	$("#q-apkind").select2({
		placeholder: "业务类型"
	});
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
}

function queryByCondtion(flag){
	var instType = $("#q-instType").val();
	var fundId = $("#q-fundId").val();
	var apkind = $("#q-apkind").val();
	
    var postData = $instFeeSetList.jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[instType]': instType,
    	'sp[fundId]': fundId,
    	'sp[apkind]': apkind
    });

    if (flag) {
    	$instFeeSetList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$instFeeSetList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

//对按钮绑定全屏事件
$(document).on('click', '.btn-fullscreen', function(){
    var $wrapper = $(this).parents('.fullscreen-wrapper')[0];
    toggleFullScreen(document.documentElement);
    //全屏的时候将几个模态框放到下面去
    $('.modal[role="dialog"]').appendTo($wrapper);
});
