/**
 * 
 */
var PRIMARY_PATH = "";
var WIDGET_PATH = "";
var $accountInfoList = $('#accountInfoList');

function setPath(widgetPath,path){
	PRIMARY_PATH = path;
	WIDGET_PATH  = widgetPath;
}

$(function() {
	$('.select2_init').select2({
		allowClear : true,
		minimumResultsForSearch : Infinity});
	
	$('.select2_cust').select2({
		allowClear : true,
		minimumResultsForSearch : Infinity,width: '180px'
	});
	
	showQuery();
	initGrid();

	$("#selCustType").change(function() {
		showQuery();
	});
	
	$("#btnQuery").mousedown(function() {
		queryByCondtion(true);
	});
	
	$("#resetBtn").mousedown(function() {
		showQuery();
	});
	
	$("#btnUpdate").mousedown(function() {
		var ids = $accountInfoList.jqGrid('getGridParam', 'selarrrow');
		var invtp = "";
		var custnoArr = new Array();
		var tradeAccoArr = new Array();
		if(ids.length == 0){
			ctools.alert("请至少选择一项进行修改！","","error");
		}else{
			for (var i = 0; i < ids.length; i++) {
				var rowData = $accountInfoList.jqGrid('getRowData', ids[i]);
				custnoArr[i] = rowData.custno;
				tradeAccoArr[i] = rowData.tradeacco;
				invtp = rowData.invtp;
			}
			
			var custnos = custnoArr.join(",");
			var tradeAccos = tradeAccoArr.join(",");
			updateCustInfo(custnos,tradeAccos, invtp);
		}
	});

});


function showQuery() {
	if ($("#selCustType").val() == 1) {
		$("#querytable1 input").val("");
		$("#querytable1").show();
		$("#querytable2").hide();
	} else {
		$("#querytable2 input").val("");
		$("#querytable2 select").val("--");
		$('.select2_cust').select2({
			allowClear : true,
			minimumResultsForSearch : Infinity,
			width: '180px'
		});
		$("#querytable1").hide();
		$("#querytable2").show();
	}
}


function initGrid(){
	$accountInfoList.jqGrid({
		url: PRIMARY_PATH+'/accountModifyQuery.do',
        datatype: "local",
        colNames: ["客户号","客户类型","客户名称","证件类型","证件号码","交易账号","基金账号","操作"],
        colModel: [
            { name: 'custno', index: 'custno', resizable:true, hidden: true, key: false, sortable: false },
            { name: 'invtp', index: 'invtp', resizable:true, hidden: true, key: false, sortable: false },
            { name: 'invnm', index: 'invnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'idtpnm', index: 'idtpnm', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'idno', index: 'idno', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'tradeacco', index: 'tradeacco', resizable:true, hidden: false, key: true, sortable: false },
            { name: 'fundacct', index: 'fundacct', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'option', index: 'option', resizable:true, hidden: false, key: false, sortable: false}
        ],
        rowNum: 15,
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
        multiselect : true,
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
			var ids = $accountInfoList.jqGrid('getDataIDs');
			for (var i = 0; i < ids.length; i++) {
                var menuId = ids[i];
                var rowData = $accountInfoList.jqGrid('getRowData', menuId);
	            var  se = '<a  id="modifi-' + menuId + '" href="javascript:void(0);" class="btn btn-link btn-jqgrid" title="修改" onclick="updateCustInfo(\''+rowData.custno+'\',\''+rowData.tradeacco+'\',\''+rowData.invtp+'\');" data-target="#modal-edit"><i class="fa fa-pencil-square-o"></i></a>';
	            $accountInfoList.jqGrid('setRowData', ids[i], { option: se });
			}
		},
		loadComplete:function(data){
			if(data.items != undefined){
				if (data.items.length == 0) {
					ctools.alert("客户资料不存在！","","warin");
				}
			}
		}
    });
	$accountInfoList.navGrid('#accountInfoPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
    $accountInfoList.jqGrid('setFrozenColumns');
    jqGridResize($accountInfoList);
}

function queryByCondtion(flag){
	var custType = $("#selCustType").val();
	var inputQueryFundAcc = $("#inputQueryFundAcc").val();
	var inputQueryFundAcc1 = $("#inputQueryFundAcc1").val();
	var custsimpnm = $("#custsimpnm").val();
	var instrepcode = $("#instrepcode").val();
	
	if ("--" == custsimpnm) {
		custsimpnm = "";
	}
	if ("--" == instrepcode) {
		instrepcode = "";
	}

	if ("1" == custType) {
		fundAcc = inputQueryFundAcc;
		custsimpnm = "";
		instrepcode = "";
	} else {
		fundAcc = inputQueryFundAcc1;
	}

	var postData = $accountInfoList.jqGrid("getGridParam", "postData");
	$.extend(postData, {
		'sp[custType]' : custType,
		'sp[fundAcco]' : fundAcc,
		'sp[option]' : '1',
		'sp[custsimpnm]' : custsimpnm,
		'sp[instrepcode]' : instrepcode
	});

    if (flag) {
    	$accountInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$accountInfoList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

//获取客户二级分组
function getSecond(id,pmco) {
	var html = "<option value='--'>--</option>";
	$("#"+id).html(html);
	if(pmco == ""){
		$("#"+id).select2({allowClear : true,minimumResultsForSearch : Infinity,width: '180px'}).trigger('change');
		return;
	}
	$.ajax({
		type : "post",
		url : WIDGET_PATH+"queryMatchParamList.do?pmst=DSCUSTGROUP&pmky=SECONDGROUP&pmv1="+pmco,
		dataType : "json",
		contentType : 'application/json;charset=utf-8',
		success : function(data) {
			if (!!data) {
				for (var i = 0; i < data.length; i++) {
					var item = data[i];
					html+="<option value=\""+item.PMCO+"\">"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
				}
				$("#"+id).html(html);
			}
		},
		error : function() {
			ctools.alert("客户二级分组查询失败！","","error");
		}
	});
	$("#"+id).select2({allowClear : true,minimumResultsForSearch : Infinity,width: '180px'}).trigger('change');
}

function updateCustInfo(custno,tradeacco,invtp){
	var method = $("#method").val();
	if(method == "baseInfo"){
		window.open(PRIMARY_PATH+"modifyCustBaseInfoView.do?custno=" + custno+ "&tradeAcco="+tradeacco+"&invtp="+invtp);
	}else if(method == "bankInfo"){
		window.open(PRIMARY_PATH+"modifyCustBankInfoView.do?custno=" + custno+ "&tradeAcco="+tradeacco+"&invtp="+invtp);
	}else if(method == "categoryInfo"){
		window.open(PRIMARY_PATH+"modifyCategoryInfoView.do?custno=" + custno+ "&tradeAcco="+tradeacco+"&invtp="+invtp);
	}else if(method == "synthesizeInfo"){
		window.open(PRIMARY_PATH+"modifySyntheSizeInfoView.do?custno=" + custno+ "&tradeAcco="+tradeacco+"&invtp="+invtp);
	}
}


