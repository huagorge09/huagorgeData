var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";
var PROJEC_TPATH = "";

function setPath(projectPath,path,accountPath){
	PROJEC_TPATH = projectPath;
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

var $accountInfoList = $("#accountInfoList");

$(function() {
	initGrid();
	queryByCondtion(true);
	$("#resetBtn").click(function(){
		$(".form-horizontal input").val("");
	});
});

function initGrid(){
	$accountInfoList.jqGrid({
		url: ACCOUNT_PATH+'/accountCancelQuery.do',
        datatype: "local",
        colNames: ["","基金帐号","交易帐号","申请编号","申请日期","业务类型","申请状态","","操作"],
        colModel: [
            { name: 'serialno', index: 'serialno', resizable:true, hidden: true, key: true, sortable: false},
            { name: 'fundacco', index: 'fundacco', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'tradeacco', index: 'tradeacco', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'serialno', index: 'serialno', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'workdate', index: 'workdate', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'dsapkindnm', index: 'dsapkindnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'checkFlag', index: 'checkFlag', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'applyst', index: 'applyst', resizable:true, hidden: true, key: false, sortable: false },
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
                var rowData = $accountInfoList.jqGrid('getRowData', serialno);
             	if(rowData.applyst == 'C'){
             	 	checkFlag = "<a href='#' title='撤单' class='btn btn-link btn-jqgrid' disabled><i class='fa fa-reply'></i></a>";
             	 	checkState = "已撤单";
             	 }else if(rowData.applyst == 'N'){
             		checkFlag = "<a href='#' title='撤单' class='btn btn-link btn-jqgrid' onclick=\"goCancelPage('"+serialno+"');\"><i class='fa fa-reply'></i></a>";
             	 	checkState = "正常";
             	 }
	            $accountInfoList.jqGrid('setRowData', ids[i], { option: checkFlag,checkFlag : checkState});
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

	var postData = $accountInfoList.jqGrid("getGridParam", "postData");
	$.extend(postData, {
		'sp[custno]' : '',
		'sp[operatorId]' : operatorId,
		'sp[operatorType]' : '1',
		'sp[tradeAcco]' : txtTradeAcc,
		'sp[fundAcco]' : txtFundAcc,
		'sp[appType]' : "",
		'sp[checkStatus]' : 'Y',
		'sp[type]' : '2',
		'sp[serialno]' : ''
	});

    if (flag) {
    	$accountInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$accountInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

function goCancelPage(serialno){
	var operatorId = $("#operatorId").val();
	openDialog(ACCOUNT_PATH+"accountCancelView.do?serialno=" + serialno+"&operatorId="+operatorId);
}
