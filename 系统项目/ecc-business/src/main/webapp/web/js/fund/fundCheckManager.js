var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
});

var queryDetailList=$('#fundCheckInfoList');

function initGrid(colNames){
	queryDetailList.jqGrid({
		url :PRIMARY_PATH +  '/getAllChkFundInfoListPage.xhtml',
		caption : '<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
		datatype : "json",
		colNames : colNames,
		colModel : [ 
		             {name : 'fundSt',index : 'fundSt',hidden : true, key : false,sortable : false},
		             {name : 'fundType',index : 'fundType',hidden : true, key : false,sortable : false},
		             {name : 'currencyType',index : 'currencyType',hidden : true, key : false,sortable : false},
		             {name : 'fundId',index : 'fundId',width : 100,hidden : false,key : true,sortable : false},
		             {name : 'fundNm',index : 'fundNm',hidden : false, key : false,sortable : false}, 
		             {name : 'fundStNm',index : 'fundStNm',hidden : false, key : false,sortable : false},
		             {name : 'fundTypeNm',index : 'fundTypeNm',hidden : false, key : false,sortable : false},
		             {name : 'taNo',index : 'taNo',hidden : false, key : false,sortable : false},
		             {name : 'currencyTypeNm',index : 'currencyTypeNm',hidden : false, key : false,sortable : false},
		             {name : 'updateTime',index : 'updateTime',hidden : false, key : false,sortable : false},
		             {name: 'option', index: 'option', width: 80, resizable:true, resizable: true, sortable: false }
		           ],
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
		width: false,
        autowidth:true,
        shrinkToFit:true,
        autoScroll : true,
        editurl: '',
        viewrecords: true,
        cellEdit: false,
        grouping: false,
		jsonReader : {
			root : "items", // 结果集
			records : "total", // 总记录数
			total : "pageCount", // 总页数
			page : "pageNo", // 当前页
			repeatitems : false// (4)
		},loadError : function(xhr, status, error) {
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
		pager : "#fundCheckInfoPage",
		multiselect: false,
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete : function() {
		   var ids = queryDetailList.jqGrid('getDataIDs');
           for (var i = 0; i < ids.length; i++) {
        	   var id = ids[i];					 						    
        	   var rowData = queryDetailList.jqGrid('getRowData', id);	
			   var fundId = rowData.fundId;
        	   var se = '<a href="javascript:void(0)" class="btn btn-link btn-jqgrid" onclick="checkFundInfo(\''+fundId+'\',\'Y\')" title="复核通过"><i class="iconfont icon-wodeshenpi-copy"></i></a>';
        	   var be = '<a href="javascript:void(0)" class="btn btn-link btn-jqgrid" onclick="checkFundInfo(\''+fundId+'\',\'N\')" title="复核驳回"><i class="fa fa-reply"></i></a>'; 
        	   queryDetailList.jqGrid('setRowData',ids[i],{option: se+be });
           }
		}
	});
	queryDetailList.navGrid('#fundCheckInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	queryDetailList.jqGrid('setFrozenColumns');
	jqGridResize(queryDetailList);
}

/**
 * 查询基金列表数据
 * @param flag
 */
function queryByCondtion(flag){
	var fundid = $("#fundid").val();
	var fundname= $("#fundname").val();
    var postData = queryDetailList.jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[fundid]': fundid,
    	'sp[fundname]': fundname,
    });
    if (flag) {
    	queryDetailList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	queryDetailList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

/**
 * 打开复核基金页面
 * @param fundId
 * @param type
 */
function checkFundInfo(fundId,type){
	var actionUrl = PRIMARY_PATH +  "/addOrUpdateFundPage.xhtml?fundId="+fundId+"&method=check&status="+type;
	openDialog(actionUrl);
}