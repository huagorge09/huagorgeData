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
	WASP_WIDGET.triggerDateStyleWithYMD("begindate");
	WASP_WIDGET.triggerDateStyleWithYMD("enddate");
    addFromValidate();
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
		url : BASE_PATH +'capitalService/server/queryTradeDate.xhtml',
		caption : '',
		datatype : "json",
		colNames : ['','','','','基金账号','交易账号','客户名称','银行账户','基金代码','基金名称','业务名称','申请编号','复核状态','申请金额','申请份额','申请日期','操作员','操作'],
		colModel : [ 
		             {name : 'applyst',index : 'applyst',hidden : true, key : false,sortable : false},
		             {name : 'appno',index : 'appno',hidden : true, key : true,sortable : false},
		             {name : 'dsapkind',index : 'dsapkind',hidden : true, key : false,sortable : false}, 
		             {name : 'chkflag',index : 'chkflag',hidden : true, key : false,sortable : false}, 
		             {name : 'fundacco',index : 'fundacco',width:'180px',hidden : false, key : false,sortable : false},
		             {name : 'tradeacco',index : 'tradeacco',hidden : false, key : false,sortable : false},
		             {name : 'invnm',index : 'invnm',hidden : false, key : false,sortable : false},
		             {name : 'bankacco',index : 'bankacco',width:'200px', hidden : true, key : false,sortable : false},
		             {name : 'fundid',index : 'fundid',hidden : true, key : false,sortable : false},
		             {name : 'fundnm',index : 'fundnm',hidden : false,width:'160px', key : false,sortable : false},
		             {name : 'dsapkindnm',index : 'dsapkindnm',hidden : false, key : false,sortable : false},
		             {name : 'serialno',index : 'serialno',width:'200px',hidden : false, key : true,sortable : false},
		             {name : 'chkflagnm',index : 'chkflagnm',hidden : false, key : false,sortable : false},
		             {name : 'subamt',index : 'subamt',hidden : false, key : false,sortable : false},
		             {name : 'subquty',index : 'subquty',hidden : false, key : false,sortable : false},
		             {name : 'workdate',index : 'workdate',hidden : false, key : false,sortable : false},
		             {name : 'operatorId',index : 'operatorId',hidden : true, key : false,sortable : false},
		             {name : 'option',index : 'option',hidden : false, key : false,sortable : false}, 
		           ],
		rowNum : 10,
		rowList : [ 10, 20, 30, 50 ],
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
    		'sp[operatorType]' : "0"   
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
				   var tradeUpdateBtnTitle = $("#tradeUpdateBtn a").attr("title");
				   
				   if(checkst == "R" && (applyst == "N" || applyst == "K")){
					   if(operator != operatorId){
						   tradeUpdateBtnTitle = "无修改权限";
					   }else{
						   $("#tradeUpdateBtn a").removeAttr("disabled");
						   $("#tradeUpdateBtn a").attr("onClick", "openTradeModifyDetailPage(\'"+serialno+"\');");
					   }
				   }else if(checkst == "N"){
					   tradeUpdateBtnTitle = "未复核";
				   }else if(checkst == "Y"){
					   tradeUpdateBtnTitle = "已复核通过";
				   }else if(checkst == "C"){
					   tradeUpdateBtnTitle = "已复核放弃";
				   }else if(checkst == "R" && applyst == "F"){
					   tradeUpdateBtnTitle = "已驳回放弃";
				   }
				   
				   //初始化 修改 按钮
				   $("#tradeUpdateBtn a").attr("title", tradeUpdateBtnTitle);
				   var be = $("#tradeUpdateBtn").html();
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
		'sp[operatorType]' : "0"
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
			$("#checkst").val("R");
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
 * 打开交易类驳回修改窗口
 */
function openTradeModifyDetailPage(serialno){
	var actionUrl =  BASE_PATH +"capitalService/server/tradeModifyDetailPage.xhtml?serialno="+serialno;
	openDialog(actionUrl);
}

/**
 * 交易类驳回修改
 */
function doSubmit(status){
	$("#modifytype").val(status);
	if($("#tradeModifyDetailForm").valid()){
		ctools.confirm({text:"是否确认提交该笔申请？"},function(isConfirm){
			if(isConfirm){
				$.ajax({
					type : "POST",
					async: false, 
					dataType : "json",
					url : BASE_PATH +'capitalService/server/tradeModify.xhtml',
					data: $('#tradeModifyDetailForm').serialize(),// 你的formid
					success : function(data) {
						if(data.errCode == "0000"){
							ctools.alert_sweet('提交成功！', "success", "申请编号："+data.serialno , function(){
								window.opener.queryByCondition(false);
								window.close();
							});
						}else{
							ctools.alert_sweet('提交失败！', "error", "申请编号："+data.serialno+"\n失败原因："+data.errMsg);
						}
					}
				});
			}
		});
	}
}

function onMoneyChange(className){
	var subAmt = $("."+className).val();
	var subAmtFmt = qianfenwei(subAmt);
	$("#CapMoneyqianfenwei").html(subAmtFmt);
	$("#CapMoney").html(capMoneyNoCheck($("."+className).val(),"subAmt"));
}


function qianfenwei(objvalue){
	if(objvalue == ""){
		return "";
    }
	if ( !objvalue.match(/^(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/) )
	{
		return ""; 
	} else if(objvalue.length>0 && objvalue.substr(0,1) == 0){		//第一位为0
		return ""; 
	}
	var num = new Number(objvalue);
	num = num.toFixed(2);
	var re=/(\d{1,3})(?=(\d{3})+(?:$|\.))/g;//转换为千分位
	return num.replace(re,"$1,");
}

function capMoneyNoCheck(xxje,amtNM) {

	var low	;				
	var i,k,j, l_xx1; 			
	var cap = "", dxnr, xx1, unit, lastDigit = "", endUnit ="", lastUnit = "" ; 	
	
	var digits = "零壹贰叁肆伍陆柒捌玖"; 
	var units = "分角元拾佰仟万拾佰仟亿拾佰仟"; 
		
	if(xxje == ""){
		return "";
	}
	if ( !xxje.match(/^(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/) )
	{
		return "<font color=red>您输入的金额格式不正确！</font>"; 
	} else if(xxje.length>0 && xxje.substr(0,1) == 0){		//第一位为0
		return "<font color=red>您输入的金额格式不正确！</font>"; 
	}
	low = parseFloat(xxje);	
	//if (isNaN(low)) return "输入无效"; 

	xx1 = Math.round(low * 100.0) + "" ;
	l_xx1 = xx1.length; 
	
	for (i=0; i<l_xx1; i++) { 
		j = l_xx1 -1 - i; 
		unit = units.substr(j, 1); 			
		k = parseInt(xx1.substr(i, 1)); 
		digit = digits.substr(k, 1);			
		cap = cap + digit + unit;
	}	
	
	cap = cap.replace( /零分|零角|零拾|零佰|零仟/g, "零");
	cap = cap.replace( /零+/g, "零");
	cap = cap.replace( /零亿/g, "亿");
	cap = cap.replace( /零万/g, "万");
	cap = cap.replace( /零元/g, "元");
	cap = cap.replace( /亿万/g, "亿");
	cap = cap.replace( /^壹拾/, "拾");
	cap = cap.replace( /零$/, "整");
	
	if (cap == "整") cap = "零元整";
	
	cap="<font color=red>"+ cap +"</font>";
	return cap; 
}



function addFromValidate(){
	// 在键盘按下并释放及提交后验证提交表单
	$("#tradeModifyDetailForm").validate({
	    rules: {
    	    subamt: {
     	    	requiredSubAmt : true,
    	    	checkSubAmt: true
    	    },
    	    subquty: {
    	    	requiredSubQuty : true,
    	    	checkSubQuty: true
    	    }
	    },
	    messages: {
	        subamt: {
	        	requiredSubAmt : "申请金额：必须填写！",
	        	checkSubAmt: "您填写的金额格式不对！"
	        },
	        subquty: {
	        	requiredSubQuty : "申请份额：必须填写！",
	        	checkSubQuty: "您填写的份额格式不对！"
	        }
	    }
	});
};

/**
 *  验证认购金额是否合法
 */
jQuery.validator.methods["requiredSubAmt"]=(function(value,element){
	var subAmt = $(".subamt").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if(subAmt == "" || subAmt == null){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证认购金额是否合法
 */
jQuery.validator.methods["checkSubAmt"]=(function(value,element){
	var subAmt = $(".subamt").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if(!(subAmt).match(/^(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/)){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证转出份额是否合法
 */
jQuery.validator.methods["requiredSubQuty"]=(function(value,element){
	var subQuty = $(".subquty").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if(subQuty == "" || subQuty == null){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证转出份额是否合法
 */
jQuery.validator.methods["checkSubQuty"]=(function(value,element){
	var subQuty = $(".subquty").val();
	var success=false;
	var queryflag = $("#queryflag").val();
	if(!(subQuty).match(/^(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/) && subQuty != ""){
		success = false
	}else{
		success = true
	}
	return success;
});