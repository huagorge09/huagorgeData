var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";
var PROJEC_TPATH = "";

function setPath(projectPath,path,accountPath){
	PROJEC_TPATH = projectPath;
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

var $accountInfoList = $("#accountInfoList");
var parameters = "";
var isQuery = false;

$(function() {
	// 注册清空事件
	$('.select2_init').select2({allowClear : true,minimumResultsForSearch : Infinity});
	initGrid();
	queryByCondtion(true);
	$("#resetBtn").click(function(){
		$(".form-horizontal input").val("");
	});
});

function initGrid(){
	$accountInfoList.jqGrid({
		url: ACCOUNT_PATH+'accountCheckQuery.do',
        datatype: "local",
        colNames: ["主键",/*"报表功能",*/"TA代码","客户名称","证件类型","证件号码","经办人名称","经办人证件类型","经办人证件号码","业务类型","复核状态","申请状态","","","操作"],
        colModel: [
            { name: 'serialno', index: 'serialno', resizable:true, hidden: true, key: true, sortable: false},
            /*{ name: 'custno', index: 'custno', resizable:true, hidden: false, key: false, sortable: false },*/
            { name: 'tano', index: 'tano', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'invnm', index: 'invnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'idtpnm', index: 'idtpnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'idno', index: 'idno', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'contact', index: 'contact', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'contidtp', index: 'contidtp', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'contidno', index: 'contidno', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'dsapkindnm', index: 'dsapkindnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'checkState', index: 'checkState', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'applyst', index: 'applyst', resizable:true, hidden: true, key: false, sortable: false },
            { name: 'operatorId', index: 'operatorId', resizable:true, hidden: true, key: false, sortable: false },
            { name: 'checkFlag', index: 'checkFlag', resizable:true, hidden: true, key: false, sortable: false },
            { name: 'option', index: 'option', resizable:true, hidden: false, key: false, sortable: false}
        ],
        rowNum: 10,
        rowList: [10, 20, 30, 50],
        rownumWidth: 50,
        rownumbers: true,
        prmNames: {search:"search",page:"pageNo",rows:"limit"},
        height: 'auto',
        width: false,
        autowidth:true,
        shrinkToFit:true,
        viewrecords: true,
        cellEdit: false,
        shrinkToFit: true,
        grouping: true,
        jsonReader: {
            root: "items", //结果集
            records: "total", //总记录数 
            total: "pageCount", //总页数
            page: "pageNo", //当前页 
            repeatitems: false // (4) 
        },
        pager: "#accountInfoPage",
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete: function() {
			var checkFlag = "";
		   	var checkState = "";
			
			var ids = $accountInfoList.jqGrid('getDataIDs');
			for (var i = 0; i < ids.length; i++) {
                var serialno = ids[i];
					var rowData = $accountInfoList.jqGrid('getRowData',serialno);
					if (rowData.checkFlag == 'Y') {
						checkFlag = "<a href='' class='btn btn-link btn-jqgrid' title='修改' disabled='disabled'><i class='fa fa-pencil-square-o'></i></a>";
						checkState = "复核成功";
					} else if (rowData.checkFlag == 'C') {
						checkFlag = "<a href='' class='btn btn-link btn-jqgrid' title='修改' disabled='disabled'><i class='fa fa-pencil-square-o'></i></a>";
						checkState = "复核放弃";
					} else if (rowData.checkFlag == 'R') {
						checkFlag = '<a href="javascript:;" class="btn btn-link btn-jqgrid" title="修改" onclick="goCheckPage(\''+serialno+'\');\"><i class="fa fa-pencil-square-o"></i></a>';
						checkState = "驳回修改";
					} else {
						checkFlag = "<a href='' class='btn btn-link btn-jqgrid' title='修改' disabled='disabled'><i class='fa fa-pencil-square-o'></i></a>";
						checkState = "未复核";
					}
	            $accountInfoList.jqGrid('setRowData', ids[i], { option: checkFlag,checkState : checkState});
			}
		},
		loadComplete:function(data){
			if(isQuery){
				if(data.items != undefined){
					if (data.items.length == 0) {
						ctools.alert("没有可操作的数据！","","warin");
						isQuery = false;
					}
				}
			}
		}
    });
	$accountInfoList.navGrid('#accountInfoPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $accountInfoList.jqGrid('setFrozenColumns');
    jqGridResize($accountInfoList);
}

function queryByCondtion(flag){
	var operatorId = $("#operatorId").val();
	var txtTradeAcc = $("#txtTradeAcc").val();
	var txtFundAcc = $("#txtFundAcc").val();
	var selAppType = $("#selAppType").val();
	var selCheckStatus = $("#selCheckStatus").val();
	
	parameters = "&tradeacco="+txtTradeAcc+"&fundacco="+txtFundAcc+"&apptype="+selAppType+"&checkstatus="+selCheckStatus;
	
	if ("--" == selAppType) {
		selAppType = "";
	}
	if ("--" == selCheckStatus) {
		selCheckStatus = "";
	}

	var postData = $accountInfoList.jqGrid("getGridParam", "postData");
	$.extend(postData, {
		'sp[custno]' : '',
		'sp[operatorId]' : operatorId,
		'sp[operatorType]' : '0',
		'sp[tradeAcco]' : txtTradeAcc,
		'sp[fundAcco]' : txtFundAcc,
		'sp[appType]' : selAppType,
		'sp[checkStatus]' : selCheckStatus,
		'sp[type]' : '0',
		'sp[serialno]' : ''
	});
	
    if (flag) {
    	$accountInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$accountInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

function queryData(){
	isQuery = true;
	queryByCondtion(true);
}

function goCheckPage(serialno){
	window.open(ACCOUNT_PATH+"accountRejectChangeView.do?serialno=" + serialno);
}
