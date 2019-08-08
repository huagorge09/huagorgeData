var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
    loadData();
	loadFundNameList();
	initGrid();
	getTradeCheckCount();
	//注册清空事件
   /* WASP_WIDGET.registerResetClearEvent();*/
	WASP_WIDGET.triggerDateStyleWithYMD("begindate");
	WASP_WIDGET.triggerDateStyleWithYMD("enddate");
});

function initGrid(){
	var tradeacco= $("#tradeacco").val();
	var fundacco = $("#fundacco").val();
	var serialno= $("#serialno").val();
	var dsapkind= $("#dsapkind").val();
	if(dsapkind == "all"){
		dsapkind = "";
	}
	var checkst= $("#checkst").val();
	if(checkst == "all"){
		checkst = "";
	}
	var begindate= $("#begindate").val();
	var enddate= $("#enddate").val();
	var trustType= $("#trustType").val();
	if(trustType == "all"){
		trustType = "";
	}
	var fundid= $("#fundid").val();
	if(fundid == "all"){
		fundid = "";
	}
	$('#tradeQryList').jqGrid({
		url :BASE_PATH + 'capitalService/server/queryTradeDate.xhtml',
		caption : '',
		datatype : "json",
		colNames : ['','','','','报表功能','基金账号','交易账号','客户名称','银行账户','基金代码','基金名称','业务名称','申请编号','复核状态','申请金额','申请份额','申请日期','操作员','操作'],
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
		             {name : 'chkflagnm',index : 'chkflagnm',hidden : false, key : false,sortable : false},
		             {name : 'subamt',index : 'subamt',hidden : false, key : false,sortable : false},
		             {name : 'subquty',index : 'subquty',hidden : false, key : false,sortable : false},
		             {name : 'workdate',index : 'workdate',hidden : false, key : false,sortable : false},
		             {name : 'operatorId',index : 'operatorId',hidden : false, key : false,sortable : false},
		             {name : 'option',index : 'option',hidden : false, key : false,sortable : false} 
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
        postData: {    
        	'sp[tradeacco]' : tradeacco,
    		'sp[fundacco]' : fundacco,
    		'sp[serialno]' : serialno,
    		'sp[dsapkind]' : dsapkind,
    		'sp[checkst]' : checkst,
    		'sp[begindate]' : begindate,
    		'sp[enddate]' : enddate,
    		'sp[checkno]' : "",
    		'sp[trustType]' : trustType,
    		'sp[fundid]' : fundid,
    		'sp[operatorType]' : "1"   
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
		gridComplete : function() {
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
	               
				   if(applyst == "C"){
						$("#tradeReCheckBtn a").attr("title", "已撤单");
					}else{
						if(checkst == "N" ){
							if(operator == operatorId){
								$("#tradeReCheckBtn a").attr("title", "无复核权限");
							}else{
								//初始化 复核 按钮
								$("#tradeReCheckBtn a").removeAttr("disabled");
								$("#tradeReCheckBtn a").attr("onClick", "openCheckTradePage('"+serialno+"');");
							}
						}else if(checkst == "Y"){
							$("#tradeReCheckBtn a").attr("title", "已复核通过");
						}else if(checkst == "R"){
							if(applyst == "F"){
								$("#tradeReCheckBtn a").attr("title", "已驳回放弃");
							}else{
								$("#tradeReCheckBtn a").attr("title", "已复核驳回");
							}
						} else if(checkst == "C"){
							$("#tradeReCheckBtn a").attr("title", "已复核作废");
						}
					}
				   
				   	var be = $("#tradeReCheckBtn").html();
				   	
	        	    $('#tradeQryList').jqGrid('setRowData',ids[i],{option: be });
	        	    var se = '';
	    			if(dsapkind == ("020") || dsapkind == ("021")){
	    				se = '<a href="javaScript:void(0);" onclick="subscribeUrl(\''+appno+'\')"; style="color:blue" target="_black">认购受理回单</a>';
	    			}else if(dsapkind == ("022") || dsapkind == ("023")){
	    				se = '<a href="javaScript:void(0);" onclick="purchaseUrl(\''+appno+'\')"; style="color:blue" target="_black">申购受理回单</a>';
	    			}else if(dsapkind == ("024") || dsapkind == ("025")){
	    				se = '<a href="javaScript:void(0);" onclick="redeemUrl(\''+appno+'\')"; style="color:blue" target="_black">赎回受理回单</a>';
	    			}else if(dsapkind == ("036") || dsapkind == ("037") || dsapkind == ("038")){
	    				se = '<a href="javaScript:void(0);" onclick="fundconverUrl(\''+appno+'\')"; style="color:blue" target="_black">转换受理回单</a>';
	    			}else if(dsapkind == ("029")){
	    				se = '<a href="javaScript:void(0);" onclick="melonUrl(\''+appno+'\')"; style="color:blue" target="_black">分红变更受理回单</a>';
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
	var tradeacco= $("#tradeacco").val();
	var fundacco = $("#fundacco").val();
	var serialno= $("#serialno").val();
	var dsapkind= $("#dsapkind").val();
	if(dsapkind == "all"){
		dsapkind = "";
	}
	var checkst= $("#checkst").val();
	if(checkst == "all"){
		checkst = "";
	}
	var begindate= $("#begindate").val();
	var enddate= $("#enddate").val();
	var trustType= $("#trustType").val();
	if(trustType == "all"){
		trustType = "";
	}
	var fundid= $("#fundid").val();
	if(fundid == "all"){
		fundid = "";
	}
	if(begindate == ""){
		toastr.warning('', '开始日期不能为空！');
		return;
	}
	if(enddate == ""){
		toastr.warning('', '结束日期不能为空！');
		return;
	}
	if(enddate < begindate){
		toastr.warning('', '开始日期不能大于结束日期！');
		return;
	}
    var postData = $('#tradeQryList').jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[tradeacco]' : tradeacco,
		'sp[fundacco]' : fundacco,
		'sp[serialno]' : serialno,
		'sp[dsapkind]' : dsapkind,
		'sp[checkst]' : checkst,
		'sp[begindate]' : begindate,
		'sp[enddate]' : enddate,
		'sp[checkno]' : "",
		'sp[trustType]' : trustType,
		'sp[fundid]' : fundid,
		'sp[operatorType]' : "1"
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
 * 加载数据
 */
function loadData(){
	$.ajax({
		url: BASE_PATH +'capitalService/server/loadTradeQryData.xhtml',   
		dataType: "json",
		type: "POST",
		cache: false,
		async: false,
		success: function(data) {
			$("#currentUserId").val(data.currentUserId);
			$("#begindate").val(data.workDateDto.workDate);
		    $("#enddate").val(data.workDateDto.workDate);
			var trustTypeHtml = "";
			trustTypeHtml+='<option value="all">全部</option>';
			var trustTypeArray = data.trustTypeArray;
			for (var m = 0; m < trustTypeArray.length; m++) {
				var temp = trustTypeArray[m];
				trustTypeHtml+="<option value="+temp.pmco+">"+temp.pmco+"　"+temp.pmnm+"</option>";
			}
			$("#trustType").html(trustTypeHtml);
			$("#trustType").val("3");
			
			var dsapkindHtml = "";
			dsapkindHtml+='<option value="all">全部</option>';
			var dsapkindArray = data.dsapkindArray;
			for (var m = 0; m < dsapkindArray.length; m++) {
				var temp = dsapkindArray[m];
				dsapkindHtml+="<option value="+temp.pmco+">"+temp.pmco+"　"+temp.pmnm+"</option>";
			}
			$("#dsapkind").html(dsapkindHtml);
			
			var checkstHtml = "";
			checkstHtml+='<option value="all">全部</option>';
			var checkstArray = data.checkstArray;
			for (var m = 0; m < checkstArray.length; m++) {
				var temp = checkstArray[m];
				checkstHtml+="<option value="+temp.pmco+">"+temp.pmco+"　"+temp.pmnm+"</option>";
			}
			$("#checkst").html(checkstHtml);
			$("#checkst").val("N");
			$('.select2').select2({allowClear: false,minimumResultsForSearch:Infinity});
		}
	});
}

/**
 * 加载基金数据
 */
function loadFundNameList(){
	$.ajax({
		url: BASE_PATH +'capitalService/server/loadFundNameList.xhtml',   
		dataType: "json",
		type: "POST",
		cache: false,
		async: false,
		success: function(data) {
			var fundNameHtml = "";
			fundNameHtml+='<option value="all">全部</option>';
			var fundNameList = data;
			for (var m = 0; m < fundNameList.length; m++) {
				var temp = fundNameList[m];
				fundNameHtml+="<option value="+temp.fundId+">"+temp.fundId+"　"+temp.fundShortNm+"</option>";
			}
			$("#fundid").html(fundNameHtml);
			$('#fundid').select2({'placeholder':"全部"});
		}
	});
}

/**
 * 交易类复核记录数查询
 */
function getTradeCheckCount(){
	$.ajax({
		url:BASE_PATH + 'capitalService/server/getTradeCheckCount.xhtml',   
		dataType: "json",
		type: "POST",
		cache: false,
		async: true,
		success: function(data) {
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
	var actionUrl = BASE_PATH + "capitalService/server/openTradeCheckDetailQryPage.xhtml?serialno="+serialno;
	openDialog(actionUrl);
}

/**
 * 提交分红方式设置数据
 */
function doSubmit(status){
	var serialno = $("#serialno").val();
	ctools.confirm({text:"是否确认提交该笔申请？"},function(isConfirm){
		if(isConfirm){
			$.ajax({
				type : "POST",
				async: false, 
				dataType : "json",
				url : BASE_PATH +'capitalService/server/tradeCheck.xhtml',
				data:{
					'serialno' : serialno,
					'checkst' : status,
					'permissionId' : '8030'
				},
				success : function(data) {
					if(data.errcode == "0000"){
						ctools.alert_sweet('提交成功！', "success", "申请编号："+data.serialno , function(){
							window.opener.queryByCondition(false);
							window.close();
						});
					}else{
						ctools.alert_sweet('提交失败！', "error", "申请编号："+data.serialno+"\n失败原因："+data.errmsg);
					}
				}
			});
		}
	});
}

/**
 * 认购受理回单
 * @param appno
 */
function subscribeUrl(appno){
	var url = BASE_PATH +"service/system/dsquery/sendRedirectUrl.xhtml?permissionId=8825&PI_APPNO="+appno;
	window.open(url);
}

/**
 * 申购受理回单
 * @param appno
 */
function purchaseUrl(appno){
	var url = BASE_PATH +"service/system/dsquery/sendRedirectUrl.xhtml?permissionId=8824&PI_APPNO="+appno;
	window.open(url);
}

/**
 * 赎回受理回单
 * @param appno
 */
function redeemUrl(appno){
	var url = BASE_PATH +"service/system/dsquery/sendRedirectUrl.xhtml?permissionId=8822&PI_APPNO="+appno;
	window.open(url);
}

/**
 * 转换受理回单
 * @param appno
 */
function fundconverUrl(appno){
	var url = BASE_PATH +"service/system/dsquery/sendRedirectUrl.xhtml?permissionId=8820&PI_APPNO="+appno;
	window.open(url);
}

/**
 * 分红变更受理回单
 * @param appno
 */
function melonUrl(appno){
	var url = BASE_PATH +"service/system/dsquery/sendRedirectUrl.xhtml?permissionId=8830&PI_APPNO="+appno;
	window.open(url);
}


