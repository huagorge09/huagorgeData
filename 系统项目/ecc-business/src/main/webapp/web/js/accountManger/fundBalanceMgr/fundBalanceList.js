/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";
var $fundBalanceList = $('#fundBalanceList');

function setPath(basePath,path){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	initGrid();
	queryByCondtion(true);
});


function initGrid(){
	$fundBalanceList.jqGrid({
		url: PRIMARY_PATH+'/queryFundBalance.do',
        datatype: "local",
        colNames: ["基金代码","基金名称","银行代码","基金账号","交易账号","投资者姓名","实际份额","可用份额","未上传申请冻结份额","已上传申请的冻结份额","异常冻结份额","基金净值"],
        colModel: [
            { name: 'fundid', index: 'fundid', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'fundnm', index: 'fundnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'bankno', index: 'bankno', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'fundacct', index: 'fundacct', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'tradeacco', index: 'tradeacco', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'invnm', index: 'invnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'balance', index: 'balance', resizable:true, hidden: false, key: false, sortable: false , formatter: 'currency'},
            { name: 'available', index: 'available', resizable:true, hidden: false, key: false, sortable: false , formatter: 'currency'},
            { name: 'frozen', index: 'frozen', resizable:true, hidden: false, key: false, sortable: false , formatter: 'currency'},
            { name: 'hfrozen', index: 'hfrozen', resizable:true, hidden: false, key: false, sortable: false , formatter: 'currency'},
            { name: 'abnmfrozen', index: 'abnmfrozen', resizable:true, hidden: false, key: false, sortable: false , formatter: 'currency'},
            { name: 'nav', index: 'nav', resizable:true, hidden: false, key: false, sortable: false , formatter: 'currency'}
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
        grouping: false,
        jsonReader: {
            root: "items", //结果集
            records: "total", //总记录数 
            total: "pageCount", //总页数
            page: "pageNo", //当前页 
            repeatitems: false // (4) 
        },
        pager: "#fundBalancePage",
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete: function() {}
    });

	$fundBalanceList.navGrid('#fundBalancePage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $fundBalanceList.jqGrid('setFrozenColumns');
    jqGridResize($fundBalanceList);
}

function queryByCondtion(flag){
	var fundacct = $("#fundacct").val();
	
    var postData = $fundBalanceList.jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[fundacct]': fundacct
    });

    if (flag) {
    	$fundBalanceList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$fundBalanceList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}