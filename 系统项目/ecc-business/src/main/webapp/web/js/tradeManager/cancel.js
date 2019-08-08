var PRIMARY_PATH = "";
var BASE_PATH = "";
var queryValidate = null;
//主管工号 数组
var MANAGERARRAY = [];
function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
$(function(){
	$("#accountType").change();
	$('.select2').select2({allowClear: false,minimumResultsForSearch:Infinity});
	loadData();
    validateForm();
    addFromValidate();
    initGrid();
});

/**
 * 选择查询类型
 * @param accountType
 */
function setType(accountType){
	if(!!queryValidate){
		queryValidate.resetForm();
	}
	if(accountType == 'TRADEACCO'){		//交易账号查询
		$(".changeText").val("");
		$("#fundacco").attr("disabled",true);
		$("#tradeacco").attr("disabled",false);
	} else if(accountType == 'FUNDACCO'){	//基金账号查询
		$(".changeText").val("");
		$("#fundacco").attr("disabled",false);
		$("#tradeacco").attr("disabled",true);
	}
}

var queryDetailList=$('#cancelInfoList');
function initGrid(){
	$("#userInfoTable").hide();
	$(".invnm").html("");
	$(".invtp").html("");
	$(".idtpNm").html("");
	$(".idno").html("");
	queryDetailList.jqGrid({
		url : BASE_PATH +'capitalService/server/cancelQryListPage.xhtml',
		caption : '',
		datatype : "json",
		colNames : ['','','','','','','','基金账号','交易账号','申请日期','申请编号','业务名称','基金名称','申请金额','申请份额','状态','经办人','经办人证件','经办人号码','操作'],
		colModel : [ 
					 {name : 'invnm',index : 'invnm',hidden : true, key : false,sortable : false},
					 {name : 'invtpnm',index : 'invtpnm',hidden : true, key : false,sortable : false},
					 {name : 'idtpnm',index : 'idtpnm',hidden : true, key : false,sortable : false},
					 {name : 'idno',index : 'idno',hidden : true, key : false,sortable : false},
					 {name : 'applyst',index : 'applyst',hidden : true, key : false,sortable : false},
					 {name : 'operatorId',index : 'operatorId',hidden : true, key : false,sortable : false},
					 {name : 'custno',index : 'custno',hidden : true, key : false,sortable : false},
		             {name : 'fundacco',index : 'fundacco', key : false,sortable : false},
		             {name : 'tradeacco',index : 'tradeacco',hidden : false, key : false,sortable : false}, 
		             {name : 'workdate',index : 'workdate',hidden : false, key : false,sortable : false},
		             {name : 'serialno',index : 'serialno',hidden : false, key : true,sortable : false},
		             {name : 'dsapkindnm',width:'60',index : 'dsapkindnm',hidden : false, key : false,sortable : false},
		             {name : 'fundnm',index : 'fundnm',hidden : false, key : false,sortable : false},
		             {name : 'subAmt',index : 'subAmt',hidden : false, key : false,sortable : false},
		             {name : 'subQuty',index : 'subQuty',hidden : false, key : false,sortable : false},
		             {name : 'applystnm',width:'60',index : 'applystnm',hidden : false, key : false,sortable : false},
		             {name : 'broker',width:'100',index : 'broker',hidden : false, key : false,sortable : false},
		             {name : 'contidtpnm',width:'100',index : 'contidtpnm',hidden : false, key : false,sortable : false},
		             {name : 'contidno',index : 'contidno',width:'120', hidden : false, key : false,sortable : false},
		             {name : 'option', index: 'option',width:'120', hidden : false, sortable: false } 
		           ],
		rowNum : 10,
		rowList : [ 10 ],
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
		pager : "#cancelInfoPage",
        viewrecords: true,
		gridComplete : function() {
		   var ids = queryDetailList.jqGrid('getDataIDs');
		   var operatorId = $("#operatorId").val();
           for (var i = 0; i < ids.length; i++) {
        	   var id = ids[i];					 						    
        	   var rowData = queryDetailList.jqGrid('getRowData', id);
        	   var invnm = rowData.invnm;
        	   var invtpnm = rowData.invtpnm;
        	   var idtpnm = rowData.idtpnm;
        	   var idno = rowData.idno;
        	   var applyst = rowData.applyst;
        	   var opId = rowData.operatorId;
        	   var custno = rowData.custno;
        	   var tradeacco = rowData.tradeacco;
        	   var serialno = rowData.serialno;
        	   var be = "";
        	   
        	   var isManageCreated =$.inArray(opId,MANAGERARRAY);
        	   
        	   if(applyst == "N" || applyst == "K"){
        		   //added ex-liuy 增加如果是主管工号为创建人的 也可以不需判断只能是本人才能撤单
        		   if(opId == operatorId || isManageCreated >= 0 ){
        			   be = '<input type="radio" style="margin-left: 0px;" name="cancelRadio" value="'+custno+'|'+tradeacco+'|'+serialno+'" onclick="setRadioValue(this.value,\''+opId+'\');" />';
        		   }
        	   }else if(applyst == "C"){
        		   be = '<input type="radio" title="已撤单" disabled style="margin-left: 0px;" name="cancelRadio" value="" onclick="" />';
        	   }else if(applyst == "F"){
        		   be = '<input type="radio" title="已作废" disabled style="margin-left: 0px;" name="cancelRadio" value="" onclick="" />';
        	   }
        	   queryDetailList.jqGrid('setRowData',ids[i],{option: be });
        	   $(".invnm").html(invnm);
        	   $(".invtp").html(invtpnm);
        	   $(".idtpNm").html(idtpnm);
        	   $(".idno").html(idno);
        	   $("#userInfoTable").show();
           }
		}
	});
	queryDetailList.navGrid('#cancelInfoPage', {
		edit : false,
		add : false,
		del : false,
		search : false,
		refreshstate : 'current'
	});
	queryDetailList.jqGrid('setFrozenColumns');
	jqGridResize(queryDetailList);
}

/**
 * 查询基金列表数据
 * @param flag
 */
function queryByCondition(flag){
	if($("#cancelFrom").valid()){
		$("#userInfoTable").hide();
		$(".invnm").html("");
		$(".invtp").html("");
		$(".idtpNm").html("");
		$(".idno").html("");
		var fundacco = $("#fundacco").val();
		var tradeacco= $("#tradeacco").val();
		var firstCustGroup= $("#firstCustGroup").val();
		var secondCustGroup= $("#secondCustGroup").val();
	    var postData = queryDetailList.jqGrid("getGridParam", "postData");
	    $.extend(postData,{
	    	'sp[fundacco]' : fundacco,
			'sp[tradeacco]' : tradeacco,
			'sp[firstCustGroup]' : firstCustGroup,
			'sp[secondCustGroup]' : secondCustGroup
	    });
	    if (flag) {
	    	queryDetailList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
	    } else {
	    	queryDetailList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
	    }
	    $("#queryflag").val("Y");
	}
}

/**
 * 加载一级客户数据
 * @param fundId
 */
function loadData(){
	$.ajax({
		url:BASE_PATH + 'capitalService/server/loadCustFirstInfo.xhtml',   
		dataType: "json",
		type: "POST",
		cache: false,
		async: false,
		success: function(data) {
			$("#operatorId").val(data.operatorId);
			var trustTypeArray = data.trustTypeArray;
			
			var custFirstHtml = "";
			custFirstHtml+='<option value="">全部</option>';
			var custFirst = data.custFirst;
			for (var m = 0; m < custFirst.length; m++) {
				var temp = custFirst[m];
				custFirstHtml+="<option value="+temp.pmco+">"+temp.pmnm+"</option>";
			}
			$("#firstCustGroup").html(custFirstHtml);
			$('#firstCustGroup').select2();
			var trustTypeHtml = "";
			for (var j = 0; j < trustTypeArray.length; j++) {
				var trustType = trustTypeArray[j];
				trustTypeHtml+="<option value="+trustType.pmco+">"+trustType.pmco+"　"+trustType.pmnm+"</option>";
			}
			$("#trustType").html(trustTypeHtml);
			$("#trustType").val("3");
			$("#trustType").select2({allowClear: false,minimumResultsForSearch:Infinity});
			
			//主管工号
			var managerArray = data.managerArray;
			for (var i = 0; i < managerArray.length; i++) {
				var manager = managerArray[i];
				if(!!manager && !!manager.pmco){
					MANAGERARRAY.push(manager.pmco);
				}
			}
		}
	});
}

/**
 * 加载二级客户数据
 * @param fundId
 */
function loadSecondCustGroup(value){
	if(value == "" || value == null){
		$("#secondCustGroup").html("");
		$('#secondCustGroup').select2();
	}else{
		$.ajax({
			url: BASE_PATH +'capitalService/server/loadSecondCustGroup.xhtml',   
			dataType: "json",
			type: "POST",
			data:{
				'pmco' : value
			},
			cache: false,
			async: true,
			success: function(data) {
				var secondCustHtml = "";
				secondCustHtml+='<option value="">全部</option>';
				var secondCust = data.secondCust;
				for (var m = 0; m < secondCust.length; m++) {
					var temp = secondCust[m];
					secondCustHtml+="<option value="+temp.pmco+">"+temp.pmnm+"</option>";
				}
				$("#secondCustGroup").html(secondCustHtml);
				$('#secondCustGroup').select2();
			}
		});
	}
}

function addFromValidate(){
	// 在键盘按下并释放及提交后验证提交表单
	$("#cancelSubmitFrom").validate({
	    rules: {
     	    checkno: {
     	    	checkNo: true
     	    },
     	    checkpwd: {
     	    	checkPwd: true
     	    }
	    },
	    messages: {
	        checkno: {
	        	checkNo: "主管工号：必须填写！"
	        },
	        checkpwd: {
	        	checkPwd: "主管密码：必须填写！"
	        }
	    }
	});
};

function validateForm(){
	// 在键盘按下并释放及提交后验证提交表单
	queryValidate = $("#cancelFrom").validate({
		rules: {
			tradeacco: {
				checkTradeacco: true
			},
			fundacco: {
				checkFundacco: true
			}
		},
		messages: {
			tradeacco: {
				checkTradeacco: "交易账号不能为空！"
			},
			fundacco: {
				checkFundacco: "基金账号不能为空！"
			}
		}
	});
};

/**
 *  验证交易账号是否为空
 */
jQuery.validator.methods["checkTradeacco"]=(function(value,element){
	var accountType = $("#accountType").val();
	var tradeacco = $("#tradeacco").val();
	var success=false;
	if(accountType == 'TRADEACCO' && tradeacco == ''){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证基金账号是否为空
 */
jQuery.validator.methods["checkFundacco"]=(function(value,element){
	var accountType = $("#accountType").val();
	var fundacco = $("#fundacco").val();
	var success=false;
	if(accountType == 'FUNDACCO' && fundacco == ''){
		success = false
	}else{
		success = true
	}
	return success;
});


/**
 *  验证主管工号是否为空
 */
jQuery.validator.methods["checkNo"]=(function(value,element){
	var success=false;
	var checkno = $("#checkno").val();
	if(isAudit == 'Y' && checkno == ""){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证主管密码不能为空
 */
jQuery.validator.methods["checkPwd"]=(function(value,element){
	var success=false;
	var checkpwd = $("#checkpwd").val();
	if(isAudit == 'Y' && checkpwd == ""){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 * 隐藏域赋值
 * @param radioValue
 * @param opid
 */
function setRadioValue(radioValue,opid){
	var result = radioValue.split('|');
	$("#custno").val(result[0]);
	$("#hidtradeacco").val(result[1]);
	$("#oldserialno").val(result[2]);
}

/**
 * 验证并提交
 */
function checkSubmit(){
	var queryflag = $("#queryflag").val();
	if(queryflag == "N"){
		ctools.alert("请先查询！","","warning");
        return;
	}
	if(isAudit == "N"){
		ctools.alert("请先授权！","","warning");
        return;
	}
	
	if($("#cancelSubmitFrom").valid()){
		var checkno = $("#checkno").val(); 
		var checkpwd = $("#checkpwd").val();
		var flag = true;
		if(isAudit == 'Y'){
			$.ajax({
				type : "POST",
				async: false, 
				dataType : "json",
				url :BASE_PATH + 'capitalService/server/checkPermission.xhtml',
				data:{
					'checkno':checkno,
					'checkpwd':checkpwd
				},
				success : function(data) {
					if(data.result == "0"){
						ctools.alert_sweet('授权失败：主管工号或密码错误！', "error", "");
						flag = false;
						return false;
					}
				}
			});
		}
		if(!flag){
			return;
		}
		
		var len = $("input[name=cancelRadio]:checked").length;
		if(len < 1){
			ctools.alert("请选择相关项！","","warning");
	        return;
		}
		duSubmit();
	}
}

/**
 * 提交分红方式设置数据
 */
function duSubmit(){
	ctools.confirm({text:"是否确认提交该笔申请？"},function(isConfirm){
		if(isConfirm){
			$(".trustType").val($("#trustType").val());
			$.ajax({
				type : "POST",
				async: false, 
				dataType : "json",
				url :BASE_PATH + 'capitalService/server/tradeCancel.xhtml',
				data:$('#cancelSubmitFrom').serialize(),// 你的formid
				success : function(data) {
					if(data.errcode == "0000"){
						ctools.alert_sweet('提交成功！', "success", "申请编号："+data.serialno , function(){
							$("#queryBtn").click();
							$("#audit").click();
						});
					}else{
						ctools.alert_sweet('提交失败！', "error", "申请编号："+data.serialno+"\n失败原因："+data.errmsg);
					}
				}
			});
		}
	});
}