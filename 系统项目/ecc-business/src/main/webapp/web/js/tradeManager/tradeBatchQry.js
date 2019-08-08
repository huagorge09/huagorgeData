var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	initGrid();
	getTradeCheckCount();
	//注册清空事件
   /* WASP_WIDGET.registerResetClearEvent();*/
	WASP_WIDGET.triggerDateStyleWithYMD("begindate");
	WASP_WIDGET.triggerDateStyleWithYMD("enddate");
});

function initGrid(){
	var dsapkind = $("#dsapkind").val();
	var fundid = $("#fundid").val();
	var trustType = $("#trustType").val();
	var begindate = $("#begindate").val();
	var enddate = $("#enddate").val();
	var opid = $("#opid").val();
	var operatorId = $("#operatorId").val();
	
	$('#tradeQryList').jqGrid({
		url : BASE_PATH+'capitalService/server/queryTradeDate.xhtml',
		caption : '',
		datatype : "json",
		colNames : ['','','','','报表功能','基金账号','交易账号','客户名称','银行账户','基金代码','基金名称','业务名称','申请编号','复核状态id','复核状态','申请金额','申请份额','申请日期','操作员','操作'],
		colModel : [ 
		             {name : 'applyst',index : 'applyst',hidden : true, key : false,sortable : false},
		             {name : 'appno',index : 'appno',hidden : true, key : false,sortable : false},
		             {name : 'dsapkind',index : 'dsapkind',hidden : true, key : false,sortable : false}, 
		             {name : 'chkflag',index : 'chkflag',hidden : true, key : false,sortable : false}, 
		             {name : 'statement',index : 'statement',hidden : false, key : false,sortable : false},
		             {name : 'fundacco',index : 'fundacco',width:'180px',hidden : false, key : false,sortable : false},
		             {name : 'tradeacco',index : 'tradeacco',hidden : false, key : false,sortable : false},
		             {name : 'invnm',index : 'invnm',hidden : false, key : false,sortable : false},
		             {name : 'bankacco',index : 'bankacco',width:'200px', hidden : false, key : false,sortable : false},
		             {name : 'fundid',index : 'fundid',hidden : false, key : false,sortable : false},
		             {name : 'fundnm',index : 'fundnm',hidden : false,width:'160px', key : false,sortable : false},
		             {name : 'dsapkindnm',index : 'dsapkindnm',hidden : false, key : false,sortable : false},
		             {name : 'serialno',index : 'serialno',width:'200px',hidden : false, key : true,sortable : false},
		             {name : 'chkflag',index : 'chkflag',hidden : true, key : false,sortable : false},
		             {name : 'chkflagnm',index : 'chkflagnm',hidden : false, key : false,sortable : false},
		             {name : 'subamt',index : 'subamt',hidden : false, key : false,sortable : false},
		             {name : 'subquty',index : 'subquty',hidden : false, key : false,sortable : false},
		             {name : 'workdate',index : 'workdate',hidden : false, key : false,sortable : false},
		             {name : 'operatorId',index : 'operatorId',hidden : false, key : false,sortable : false},
		             {name : 'option',index : 'option',hidden : false, key : false,sortable : false} 
		           ],
		rowNum : 10,
		rowList : [10, 20, 30, 50 ],
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
        multiselect: true,//复选框 
        postData: {    
        	'sp[dsapkind]' : dsapkind,
    		'sp[fundid]' : fundid,
    		'sp[trustType]' : trustType,
    		'sp[begindate]' : begindate,
    		'sp[enddate]' : enddate,
    		'sp[operatorType]' : "3"
        },
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
		pager : "#tradeQryPage",
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete : function($this) {
			   var ids = $('#tradeQryList').jqGrid('getDataIDs');
			   var operatorId = $("#currentUserId").val();
	           for (var i = 0; i < ids.length; i++) {
	        	   var id = ids[i];					 						    
	        	   var rowData = $('#tradeQryList').jqGrid('getRowData', id);
				   var applyst = rowData.applyst;
				   var checkst = rowData.chkflag;
				   var operator = rowData.operatorId;
				   var dsapkind = rowData.dsapkind;
				   var serialno = rowData.serialno;
				   var appno = rowData.appno;
				   
				   //按钮置灰
				   $(".permissionBtn a").attr("disabled", "disabled");
	               $(".permissionBtn a").removeAttr("onclick");
	               
	               $("#operationBtn a").attr("title","进行复核");
	               $("#operationBtn a i").attr("class","iconfont icon-wodeshenpi-copy");
	               
				   //operationBtn
				   var tradeReCheckTitle = $("#operationBtn a").attr("title");
				   var be = "";
				   if(applyst == "C"){
					   	tradeReCheckTitle = "已撤单";
//						$("td[title="+appno+"]").parent().find("td:eq(1)").html("");
					}else{
						if(checkst == "N" ){
							if(operator == operatorId){
								tradeReCheckTitle = '无复核权限';
//								$("td[title="+appno+"]").parent().find("td:eq(1)").html("");
							}else{
								//初始化 复核 按钮
								$("#operationBtn a").removeAttr("disabled");
								$("#operationBtn a").attr("onClick", "openCheckTradePage('"+serialno+"');");
							}
						}else if(checkst == "Y"){
							tradeReCheckTitle = "已复核通过";
//							$("td[title="+appno+"]").parent().find("td:eq(1)").html("");
						}else if(checkst == "R"){
							if(applyst == "F"){
								tradeReCheckTitle = "已驳回放弃";
//								$("td[title="+appno+"]").parent().find("td:eq(1)").html("");
							}else{
								//初始化 修改 按钮
								tradeReCheckTitle ="进行修改";
								$("#operationBtn a i").attr("class", "fa fa-pencil-square-o");
								$("#operationBtn a").removeAttr("disabled");
								$("#operationBtn a").attr("onClick", "openModifyTradePage('"+serialno+"');");
//								$("td[title="+appno+"]").parent().find("td:eq(1)").html("");
							}
						} else if(checkst == "C"){
							tradeReCheckTitle = "已复核作废";
//							$("td[title="+appno+"]").parent().find("td:eq(1)").html("");
						}
					}
				   	
				   	$("#operationBtn a").attr("title",tradeReCheckTitle);
				   	var be = $("#operationBtn").html();
				   
	        	    $('#tradeQryList').jqGrid('setRowData',ids[i],{option: be });
	        	    
	    			if(dsapkind == ("020") || dsapkind == ("021")){
	    				var se = '<a href="javaScript:void(0);" onclick="subscribeUrl(\''+appno+'\')"; style="color:blue" target="_black">认购受理回单</a>';
	    			}else if(dsapkind == ("022") || dsapkind == ("023")){
	    				var se = '<a href="javaScript:void(0);" onclick="purchaseUrl(\''+appno+'\')"; style="color:blue" target="_black">申购受理回单</a>';
	    			}else if(dsapkind == ("024") || dsapkind == ("025")){
	    				var se = '<a href="javaScript:void(0);" onclick="redeemUrl(\''+appno+'\')"; style="color:blue" target="_black">赎回受理回单</a>';
	    			}else if(dsapkind == ("036") || dsapkind == ("037") || dsapkind == ("038")){
	    				var se = '<a href="javaScript:void(0);" onclick="fundconverUrl(\''+appno+'\')"; style="color:blue" target="_black">转换受理回单</a>';
	    			}else if(dsapkind == ("029")){
	    				var se = '<a href="javaScript:void(0);" onclick="melonUrl(\''+appno+'\')"; style="color:blue" target="_black">分红变更受理回单</a>';
	    			}	
	        	    $('#tradeQryList').jqGrid('setRowData',ids[i],{statement: se });
	           }
			}
	});
	$('#tradeQryList').navGrid('#tradeQryPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	$('#tradeQryList').jqGrid('setFrozenColumns');
	jqGridResize($('#tradeQryList'));
}

/**
 * 查询基金列表数据
 * @param flag
 */
function queryByCondition(flag){
	var dsapkind = $("#dsapkind").val();
	var fundid = $("#fundid").val();
	var trustType = $("#trustType").val();
	var begindate = $("#begindate").val();
	var enddate = $("#enddate").val();
	var opid = $("#opid").val();
	var operatorId = $("#operatorId").val();
    var postData = $('#tradeQryList').jqGrid("getGridParam", "postData");
    $.extend(postData,{
		'sp[dsapkind]' : dsapkind,
		'sp[fundid]' : fundid,
		'sp[trustType]' : trustType,
		'sp[begindate]' : begindate,
		'sp[enddate]' : enddate,
		'sp[operatorType]' : "3"
    });
    if (flag) {
    	$('#tradeQryList').jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	$('#tradeQryList').jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
    getTradeCheckCount();
}

function clearValue(){
	$(".clearText").val("");
	$("select.clearText").val("all");
	$('.select2').select2({allowClear: false,minimumResultsForSearch:Infinity});
}


/**
 * 交易类复核记录数查询
 */
function getTradeCheckCount(){
	var fundid = $("#fundid").val();
	$.ajax({
		url: BASE_PATH+'capitalService/server/getBatchTradeCheckCount.xhtml',   
		dataType: "json",
		data:{
			"sp[fundid]" : fundid
		},
		type: "POST",
		cache: false,
		async: true,
		success: function(data) {
			$("#rfcount").html(data.rfcount);
			$("#rccount").html(data.rccount);
			$("#fcount").html(data.fcount);
			$("#ncount").html(data.ncount);
			$("#ccount").html(data.ccount);
			$("#ycount").html(data.ycount);
			$("#cancelcount").html(data.cancelcount);
			$("#totalcount").html(parseInt(data.fcount) + parseInt(data.ncount) + parseInt(data.ccount) + parseInt(data.ycount) + parseInt(data.cancelcount));
		}
	});
}

/**
 * 打开交易复核窗口页面
 */
function openCheckTradePage(serialno){
	var actionUrl =  BASE_PATH+"capitalService/server/openTradeCheckDetailQryPage.xhtml?serialno="+serialno;
	openDialog(actionUrl);
}

function openModifyTradePage(serialno){
	var actionUrl =  BASE_PATH+"capitalService/server/tradeModifyDetailPage.xhtml?serialno="+serialno;
	openDialog(actionUrl);
}

/**
 * 提交分红方式设置数据
 */
function doSubmit(status,title){
	var trdIds = $('#tradeQryList').jqGrid('getGridParam','selarrrow');//选择多选
	for (var i = 0; i < trdIds.length; i++) {
		var len = $("tr[id="+trdIds[i]+"]").find("input[role=checkbox]").length;
		if(len < 1){
			trdIds.splice(0,1);
		}
	}
	if(0 == trdIds.length) {
		ctools.alert("请至少选择一条数据！","","warning");
        return;
	}
	var successMsg = "";
	var errorMsg = "";
	ctools.confirm({text:"是否"+title+"所选的数据？"},function(isConfirm){
		if(isConfirm){
			for (var i = 0; i < trdIds.length; i++) {
				$.ajax({
					type : "POST",
					async: false, 
					dataType : "json",
					url : BASE_PATH+'capitalService/server/tradeCheck.xhtml',
					data:{
						'serialno' : trdIds[i],
						'checkst' : status,
						'permissionId' : '8030'
					},
					success : function(data) {
						if(data.errcode == "0000"){
							successMsg+="申请编号："+data.serialno+" 提交成功！\n";
						}else{
							errorMsg+="申请编号："+data.serialno+"　失败原因："+data.errmsg+"\n";
						}
					}
				});
			}
			if(successMsg != ""){
				ctools.alert_sweet('提交成功！', "success", successMsg , function(){
					window.opener.queryByCondition(false);
					getTradeCheckCount();
			    	$('#tradeQryList').jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
				});
			}else if(errorMsg != ""){
				ctools.alert_sweet('提交失败！', "error", errorMsg);
				$(".sweet-alert").css({
					'width': 'auto',
			    	'min-width': '480px',
			        'padding': '16px 40px'
				});
		    		$('#tradeQryList').jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格

			}
		}
	});
}

/**
 * 认购受理回单
 * @param appno
 */
function subscribeUrl(appno){
	var url = BASE_PATH+"service/system/dsquery/sendRedirectUrl.xhtml?permissionId=8825&PI_APPNO="+appno;
	window.open(url);
}

/**
 * 申购受理回单
 * @param appno
 */
function purchaseUrl(appno){
	var url = BASE_PATH+"service/system/dsquery/sendRedirectUrl.xhtml?permissionId=8824&PI_APPNO="+appno;
	window.open(url);
}

/**
 * 赎回受理回单
 * @param appno
 */
function redeemUrl(appno){
	var url = BASE_PATH+"service/system/dsquery/sendRedirectUrl.xhtml?permissionId=8822&PI_APPNO="+appno;
	window.open(url);
}

/**
 * 转换受理回单
 * @param appno
 */
function fundconverUrl(appno){
	var url = BASE_PATH+"service/system/dsquery/sendRedirectUrl.xhtml?permissionId=8820&PI_APPNO="+appno;
	window.open(url);
}

/**
 * 分红变更受理回单
 * @param appno
 */
function melonUrl(appno){
	var url = BASE_PATH+"service/system/dsquery/sendRedirectUrl.xhtml?permissionId=8830&PI_APPNO="+appno;
	window.open(url);
}

/**
 * 关闭当前页面
 */
function closePage(){
	window.opener=null;
	window.open('','_self');
	queryByCondition(false);
	window.close();
}