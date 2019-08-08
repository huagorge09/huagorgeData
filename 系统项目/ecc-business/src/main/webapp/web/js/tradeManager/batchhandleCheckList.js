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
	//注册清空事件
   /* WASP_WIDGET.registerResetClearEvent();*/
	WASP_WIDGET.triggerDateStyleWithYMD("begindate");
	WASP_WIDGET.triggerDateStyleWithYMD("enddate");
});

function initGrid(){
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
		url : BASE_PATH + 'capitalService/server/queryBatchList.xhtml',
		caption : '',
		datatype : "json",
		colNames : ['','','','','基金代码','基金名称','业务名称','交易笔数','申请金额','申请份额','申请日期','操作员','操作'],
		colModel : [ 
		             {name : 'serialno',index : 'serialno',hidden : true, key : true,sortable : false},
		             {name : 'dsapkind',index : 'dsapkind',hidden : true, key : false,sortable : false},
		             {name : 'chkflag',index : 'chkflag',hidden : true, key : false,sortable : false},
		             {name : 'nopcount',index : 'nopcount',hidden : true, key : false,sortable : false},
		             {name : 'fundid',index : 'fundid',width:'180px',hidden : false, key : false,sortable : false},
		             {name : 'fundnm',index : 'fundnm',hidden : false,width:'160px', key : false,sortable : false},
		             {name : 'dsapkindnm',index : 'dsapkindnm',hidden : false, key : false,sortable : false},
		             {name : 'totalRow',index : 'totalRow',width:'200px',hidden : false, fasle : true,sortable : false},
		             {name : 'sumsubamt',index : 'sumsubamt',hidden : false, key : false,sortable : false},
		             {name : 'sumsubquty',index : 'sumsubquty',hidden : false, key : false,sortable : false},
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
				   var fundid = rowData.fundid;
				   var checkst = rowData.chkflag;
				   if(checkst == "" || checkst == null){
					   checkst = "N";
				   }
				   var operator = rowData.operatorId;
				   var dsapkind = rowData.dsapkind;
				   var nopcount = rowData.nopcount;
				   if(nopcount == null || nopcount.length == 0){
					   nopcount = "0";
				   }
				   var be = "";
				  /* if("0" == nopcount && "N" == checkst){
					   continue;
				   }*/
				   
				   //按钮置灰 batTradeDetailBtn batTradeReCheckBtn
				   $(".permissionBtn a").attr("disabled", "disabled");
	               $(".permissionBtn a").removeAttr("onclick");
	               
				   if("0" == nopcount){
					   	//初始化 查看 按钮
						$("#batTradeDetailBtn a").removeAttr("disabled");
						$("#batTradeDetailBtn a").attr("onClick", "tradeQry(\'"+dsapkind+"\',\'"+fundid+"\');");
						be = $("#batTradeDetailBtn").html();
				   }else{
						//初始化 查看 按钮
						$("#batTradeReCheckBtn a").removeAttr("disabled");
						$("#batTradeReCheckBtn a").attr("onClick", "tradeQry(\'"+dsapkind+"\',\'"+fundid+"\');");
						be = $("#batTradeReCheckBtn").html();
				   }
	        	   $('#tradeQryList').jqGrid('setRowData',ids[i],{option: be });
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
		url:BASE_PATH + 'capitalService/server/loadTradeQryData.xhtml',   
		dataType: "json",
		type: "POST",
		cache: false,
		async: false,
		success: function(data) {
			$("#currentUserId").val(data.currentUserId);
			$("#begindate").val(data.workDateDto.workDate);
		    $("#enddate").val(data.workDateDto.workDate);
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
 * 打开交易复核窗口页面
 */
function openCheckTradePage(serialno){
	var actionUrl =  BASE_PATH +"capitalService/server/openTradeCheckDetailQryPage.xhtml?serialno="+serialno;
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

function tradeQry(dsapkind,fundid){
	var begindate = $("#begindate").val();
	var enddate = $("#enddate").val();
	var url =BASE_PATH + "capitalService/server/openTradeBatchQryPage.xhtml?dsapkind="+dsapkind+"&fundid="+fundid+"&trustType=3&begindate="+begindate+"&enddate="+enddate;
	window.open(url);
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

/**
 * 生成随机数
 * @param n
 * @returns {String}
 */
function generateMixed(n) {
	 var chars = ['0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z'];
     var res = "";
     for(var i = 0; i < n ; i ++) {
         var id = Math.ceil(Math.random()*35);
         res += chars[id];
     }
     return res;
}