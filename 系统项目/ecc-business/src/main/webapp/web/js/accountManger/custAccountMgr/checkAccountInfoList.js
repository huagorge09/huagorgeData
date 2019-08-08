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
var url = "";
$(function() {
	// 注册清空事件
	$('.select2_init').select2({allowClear : true,minimumResultsForSearch : Infinity});
	initGrid();
	queryByCondtion(true);
	$("#resetBtn").click(function(){
		$(".form-horizontal input").val("");
	});
	//获取报表链接
	url = PROJEC_TPATH + "service/accountSearch/getDsReportLink.xhtml?";
});

function initGrid(){
	$accountInfoList.jqGrid({
		url: ACCOUNT_PATH+'accountCheckQuery.do',
        datatype: "local",
        colNames: ["主键","报表功能","TA代码","客户名称","证件类型","证件号码","经办人名称","经办人证件类型","经办人证件号码","业务类型","复核状态","申请状态","","","","","操作"],
        colModel: [
            { name: 'serialno', index: 'serialno', resizable:true, hidden: true, key: true, sortable: false},
            { name: 'report', index: 'report', resizable:true, hidden: false, key: false, sortable: false },
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
            { name: 'dsapkind', index: 'dsapkind', resizable:true, hidden: true, key: false, sortable: false },
            { name: 'oldserialno', index: 'oldserialno', resizable:true, hidden: true, key: false, sortable: false },
            { name: 'checkFlag', index: 'checkFlag', resizable:true, hidden: true, key: false, sortable: false },
            { name: 'option', index: 'option', resizable:true, hidden: false, key: false, sortable: false},
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
		toolbar: [true, "top"],
		gridComplete: function() {
			var checkFlag = "";
		   	var checkState = "";
		   	var operatorId = $("#operatorId").val();
			
			var ids = $accountInfoList.jqGrid('getDataIDs');
			for (var i = 0; i < ids.length; i++) {
                var serialno = ids[i];
                var rowData = $accountInfoList.jqGrid('getRowData', serialno);
				if (rowData.applyst == 'C') {
					checkFlag = "<a href='#' class='btn btn-link btn-jqgrid' title='复核' disabled><i class='iconfont icon-wodeshenpi-copy'></i></a>";
					checkState = "已撤单";
				} else {
					if (rowData.operatorId == operatorId) {
						if (rowData.checkFlag == 'Y') {
							checkFlag = "<a href='#' class='btn btn-link btn-jqgrid' title='复核' disabled><i class='iconfont icon-wodeshenpi-copy'></i></a>";
							checkState = "复核成功";
						} else if (rowData.checkFlag == 'C') {
							checkFlag = "<a href='#' class='btn btn-link btn-jqgrid' title='复核' disabled><i class='iconfont icon-wodeshenpi-copy'></i></a>";
							checkState = "复核作废";
						} else if (rowData.checkFlag == 'R') {
							checkFlag = "<a href='#' class='btn btn-link btn-jqgrid' title='复核' disabled><i class='iconfont icon-wodeshenpi-copy'></i></a>";
							checkState = "复核驳回";
						} else if (rowData.checkFlag == 'N') {
							checkFlag = "<a href='#' class='btn btn-link btn-jqgrid' title='复核' disabled><i class='iconfont icon-wodeshenpi-copy'></i></a>";
							checkState = "未复核";
						}
					} else {
						if (rowData.checkFlag == 'Y') {
							checkFlag = "<a href='#' class='btn btn-link btn-jqgrid' title='复核' disabled><i class='iconfont icon-wodeshenpi-copy'></i></a>";
							checkState = "复核成功";
						} else if (rowData.checkFlag == 'C') {
							checkFlag = "<a href='#' class='btn btn-link btn-jqgrid' title='复核' disabled><i class='iconfont icon-wodeshenpi-copy'></i></a>";
							checkState = "复核作废";
						} else if (rowData.checkFlag == 'R') {
							checkFlag = "<a href='#' class='btn btn-link btn-jqgrid' title='复核' disabled><i class='iconfont icon-wodeshenpi-copy'></i></a>";
							checkState = "复核驳回";
						} else if (rowData.checkFlag == 'N') {
							checkFlag = "<a href='#' class='btn btn-link btn-jqgrid' title='复核' onclick=\"goCheckPage('"+serialno+"');\"><i class='iconfont icon-wodeshenpi-copy'></i></a>";
							checkState = "未复核";
						}
					}
				}
				var dsapkind = rowData.dsapkind;
				var oldserialno = rowData.oldserialno;
				var reporturl = "";
				if (dsapkind == '001') {
					var fundopenUrl = url + "permissionId=8817&PI_APPNO="+ oldserialno;
					reporturl = "<a href='#' onclick='openReportUrl(\""+fundopenUrl+"\")' style='color:blue'>账户开户受理回单</a>";
				} else if (dsapkind == '002') {
					var fundcloseUrl = url + "permissionId=8817&PI_APPNO="+ oldserialno;
					reporturl = "<a href='#' onclick='openReportUrl(\""+fundcloseUrl+"\")' style='color:blue'>账户销户受理回单</a>";
				} else if (dsapkind == '003' || dsapkind == '0B1') {
					var fundupdateUrl = url + "permissionId=8815&PI_APPNO="+ oldserialno;
					reporturl = "<a href='#' onclick='openReportUrl(\""+fundupdateUrl+"\")' style='color:blue'>账户变更受理回单</a>";
				}
	            $accountInfoList.jqGrid('setRowData', ids[i], { option: checkFlag,checkState : checkState,report : reporturl});
			}
		}
    });
	$accountInfoList.navGrid('#accountInfoPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $accountInfoList.jqGrid('setFrozenColumns');
    jqGridResize($accountInfoList);
    tableToolbar();
}

function openReportUrl(url){
	window.open(url);
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
		'sp[operatorType]' : '1',
		'sp[tradeAcco]' : txtTradeAcc,
		'sp[fundAcco]' : txtFundAcc,
		'sp[appType]' : selAppType,
		'sp[checkStatus]' : selCheckStatus,
		'sp[type]' : '1',
		'sp[serialno]' : ''
	});

    if (flag) {
    	$accountInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$accountInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
    tableToolbar();
}

function goCheckPage(serialno){
	window.open(ACCOUNT_PATH+"accountCheckView.do?serialno=" + serialno);
}

function tableToolbar(){
	$.ajax({
		url: ACCOUNT_PATH+"getTradeCount.do",  
		dataType: "json",
		type: "POST",
		cache: false,
		async: false,
		success: function(data) {
			if(!!data){
				$("#ncount").html(data.ncount);
				$("#ccount").html(data.fcount);
				$("#ycount").html(data.ycount);
				$("#fcount").html(data.ccount);
				$("#cancelcount").html(data.cancelcount);
				$("#totalcount").html(data.totalcount);
			}
		}
	});
	
	var html = '<table id="tabPslBaseInfo" class="table table-bordered" style="margin-top: -1px;">';
		html+= '<colgroup><col width="16.5%"><col width="16.5%"><col width="16.5%"><col width="16.5%"><col width="16.5%"><col width="16.5%"></colgroup>';
		html+= '<tbody><tr>';
		html+= '<td>复核作废：<span id="fcount">0</span></td>';
		html+= '<td>未处理：    <span id="ncount">0</span></td>';
		html+= '<td>复核驳回：<span id="ccount">0</span></td>';
		html+= '<td>复核成功：<span id="ycount">0</span></td>';
		html+= '<td>已撤单：<span id="cancelcount">0</span></td>';
		html+= '<td>总笔数：<span id="totalcount">0</span></td>';
		html+= '</tr></tbody></table>';
	$("#t_accountInfoList").append(html);
	$("#t_accountInfoList").css({"height": "35px","background-color": "#f5f8f8"});
}
