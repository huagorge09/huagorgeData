/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";
var custDataList = $('#custDataList');

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	$("#strDate").val($("input[name=hidStrDate]").val());
	$("#endDate").val($("input[name=hidEndDate]").val());
	var invnm = GetQueryString("invnm");
	if(!!invnm){
		$("#invnm").val(invnm);
	}
	
	var strDate = GetQueryString("strDate");
	if(!!strDate){
		$("#strDate").val(strDate);
	}
	
	var endDate = GetQueryString("endDate");
	if(!!endDate){
		$("#endDate").val(endDate);
	}
	
	initGrid();
	initWidget();
});

var custDataFunc = {
		custDataExport : function(){
			var url = PRIMARY_PATH+'/queryExportCustDataInfo.do';
			var data = JSON.stringify(queryPostData());
			data = encodeURI(data);
			window.open(url+"?data="+data);
		},
		custDataUpdateView:function(appserialno,fundacct,custno){
			openDialog(PRIMARY_PATH+'/updateCustDataManagerView.do?appserialno='+appserialno+"&fundacct="+fundacct+"&custno="+custno);
		},
		goTradeDataTab : function(invnm){
			var strDate = $("#strDate").val();
			var endDate = $("#endDate").val();
			var info = {
					url: '#!'+BASE_PATH+'service/tradeDataManager/tradeDataMgr.do?invnm='+invnm+'&strDate='+strDate+'&endDate='+endDate,
		            title: '交易资料管理'
				};
			window.parent.pubsub.publish("goPageTab",info);
		}
};

function initGrid(){
	custDataList.jqGrid({
		url: PRIMARY_PATH+'/queryCustDataInfoListPage.do',
        caption: '客户资料管理列表<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
        datatype: "json",
        postData :queryPostData(),
        colNames: ["申请编号","交易资料","申请时间","文件编号","录音文件编号","基金账号","custno","客户名称","业务类型id","业务类型",
                   "投资者类型id","投资者类型","是否原件","是否齐全","是否扫描","是否归档","归档位置","是否上传","所属客户经理","备注","操作"],
        colModel: [
            { name: 'appserialno', index: 'appserialno',  hidden: true, key: true, sortable: false},
            { name: 'tradeData', index:'tradeData',width:100, align:'left', resizable:true, resizable: true, sortable: false },
            { name: 'apdt', index: 'apdt', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },       
            { name: 'fileno', index: 'fileno', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },       
            { name: 'voiceRecord', index: 'voiceRecord', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'fundacct', index: 'fundacct', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'custno', index: 'custno',  hidden: true, key: false, sortable: false},
            { name: 'invnm', index: 'invnm', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'apkind', index: 'apkind',  hidden: true, key: false, sortable: false},
            { name: 'apkindName', index: 'apkindName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'invprtp', index: 'invprtp',  hidden: true, key: false, sortable: false},
            { name: 'invprtpName', index: 'invprtpName', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'ifOriginal', index: 'ifOriginal', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'ifalldocument', index: 'ifalldocument', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'isscan', index: 'isscan', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'ifsaved', index: 'ifsaved', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'keepaddress', index: 'keepaddress', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'isupload', index: 'isupload', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'salesaccmanager', index: 'salesaccmanager', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'remarkinfo', index: 'remarkinfo', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false },
            { name: 'option', index: 'option', width:100, align:'left', resizable:true, hidden: false, key: false, sortable: false }
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
        pager: "#custDataPage",
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete: function() {
			var ids = custDataList.jqGrid('getDataIDs');
			for(var i=0;i < ids.length;i++){
				var id = ids[i];
				var rowData = custDataList.jqGrid('getRowData', id);
				var appserialno = rowData.appserialno;
				var custno = rowData.custno;
				var fundacct = rowData.fundacct;
				var isoriginal= rowData.ifOriginal;
				var isalldoc= rowData.ifalldocument;
				var isscan= rowData.isscan;
				var issaved= rowData.ifsaved;
				var isupload= rowData.isupload;
				var invprtp = rowData.invprtp;
				var invnm = rowData.invnm;
				
				var isoriginalName="";
				var isalldocName="";
				var isscanName="";
				var issavedName="";
				var isuploadName="";
				var invprtpName ="";
				
				isoriginalName = "1" == isoriginal ? "是" : "否";
				isalldocName = "1" == isalldoc ? "是" : "否";
				isscanName = "Y" == isscan ? "是" : "否";
				issavedName = "1" == issaved ? "是" : "否";
				isuploadName = "1" == isupload ? "是" : "否";
				invprtpName = "0" == invprtp ? "专业投资者" : "普通投资者";
				
				//按钮置灰
				$(".permissionBtn a").attr("disabled", "disabled");
                $(".permissionBtn a").removeAttr("onclick");
                //初始化修改按钮
                $("#custDataUpdateBtn a").removeAttr("disabled");
            	$("#custDataUpdateBtn a").attr("onClick", "custDataFunc.custDataUpdateView('"+appserialno+"','"+fundacct+"','"+custno+"');");
            	
            	$("#goTradeDataBtn a").removeAttr("disabled");
         		$("#goTradeDataBtn a").attr("onClick", "custDataFunc.goTradeDataTab('"+invnm+"');");
            	
            	
            	var cdb = $("#custDataUpdateBtn").html();
            	var gtd = $("#goTradeDataBtn").html();
            	
            	custDataList.jqGrid('setRowData',ids[i],{tradeData:gtd,ifOriginal:isoriginalName,ifalldocument:isalldocName,isscan:isscanName,ifsaved:issavedName,isupload:isuploadName,invprtpName:invprtpName,option:cdb});
			}
		}
    });

	custDataList.navGrid('#custDataPage', { edit: false, add: false, del: false, search: false, refreshstate: 'current' });
	custDataList.jqGrid('setFrozenColumns');
    jqGridResize(custDataList);
}

function queryPostData(){
	var postData={};
	postData.sp={};
	postData.sp.strDate = $("#strDate").val();
	postData.sp.endDate = $("#endDate").val();
	postData.sp.fileno = $("#fileno").val();
	postData.sp.fundacco = $("#fundacco").val();
	postData.sp.invnm = $("#invnm").val();
	postData.sp.keepaddress = $("#keepaddress").val();
	postData.sp.salesaccmanager = $("#salesaccmanager").val();
	postData.sp.apkind = $("#apkind").val();
	postData.sp.isoriginal = $("#isoriginal").val();
	postData.sp.isalldoc = $("#isalldoc").val();
	postData.sp.isscan = $("#isscan").val();
	postData.sp.issaved = $("#issaved").val();
	postData.sp.isupload = $("#isupload").val();
	return postData;
}

/**
 * 初始化控件
 */
function initWidget(){
	WASP_WIDGET.triggerDateStyleWithYMD("strDate");
	WASP_WIDGET.triggerDateStyleWithYMD("endDate");
	$("#apkind").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isoriginal").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isalldoc").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isscan").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#issaved").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isupload").select2({allowClear: false,minimumResultsForSearch:Infinity});
}

/**
 * 查询按钮
 * 根据条件查询数据
 * @param flag
 */
function queryByCondtion(flag){
    var postData = custDataList.jqGrid("getGridParam", "postData");
    $.extend(postData,queryPostData());
    if (flag) {
    	custDataList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	custDataList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

/**
 * 重写清空事件
 * */
function resetQueryCondtion(){
	$("#strDate").val($("input[name=hidStrDate]").val());
	$("#endDate").val($("input[name=hidEndDate]").val());
	WASP_WIDGET.triggerDateStyleWithYMD("strDate");
	WASP_WIDGET.triggerDateStyleWithYMD("endDate");
	$("#fileno").val(null);
	$("#fundacco").val(null);
	$("#invnm").val(null);
	$("#apkind").val(null);
	$("#isoriginal").val(null);
	$("#isalldoc").val(null);
	$("#isscan").val(null);
	$("#issaved").val(null);
	$("#keepaddress").val(null);
	$("#isupload").val(null);
	$("#salesaccmanager").val(null);
	
	$("#apkind").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isoriginal").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isalldoc").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isscan").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#issaved").select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#isupload").select2({allowClear: false,minimumResultsForSearch:Infinity});
}