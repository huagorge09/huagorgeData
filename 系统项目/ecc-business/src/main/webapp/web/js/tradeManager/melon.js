var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	$("#accountType").change();
	$('.select2').select2({allowClear: false,minimumResultsForSearch:Infinity});
	loadData();
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
    initGrid();
});

/**
 * 选择查询类型
 * @param accountType
 */
function setType(accountType){
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

var queryDetailList=$('#melonInfoList');
function initGrid(){
	queryDetailList.jqGrid({
		url : BASE_PATH +'capitalService/server/melonListQry.xhtml',
		caption : '',
		datatype : "json",
		colNames : ['客户号','基金账号','交易账号','客户名称','客户类型'],
		colModel : [ 
		             {name : 'tradeacco',index : 'tradeacco',hidden : true, key : true,sortable : false}, 
		             {name : 'fundacco',index : 'fundacco',hidden : false, key : false,sortable : false}, 
		             {name : 'tradeacco',index : 'tradeacco',hidden : false, key : false,sortable : false},
		             {name : 'invnm',index : 'invnm',hidden : false, key : false,sortable : false},
		             {name : 'invtp',index : 'invtp',hidden : false, key : false,sortable : false},
		             /*{name : 'option', index: 'option', width: 80, resizable:true, resizable: true, sortable: false }*/
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
		pager : "#melonInfoPage",
		multiselect: true,
        viewrecords: true,
        hidegrid: false,
		subGrid: false
	});
	queryDetailList.navGrid('#melonInfoPage', {
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
}

/**
 * 加载一级客户数据
 * @param fundId
 */
function loadData(){
	$.ajax({
		url: BASE_PATH +'capitalService/server/loadCustFirstInfo.xhtml',   
		dataType: "json",
		type: "POST",
		cache: false,
		async: true,
		success: function(data) {
			var custFirstHtml = "";
			custFirstHtml+='<option value="">全部</option>';
			var custFirst = data.custFirst;
			for (var m = 0; m < custFirst.length; m++) {
				var temp = custFirst[m];
				custFirstHtml+="<option value="+temp.pmco+">"+temp.pmnm+"</option>";
			}
			$("#firstCustGroup").html(custFirstHtml);
			$('#firstCustGroup').select2();
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

/**
 * 打开设置分红方式页面
 */
function goUpdateMelonPage(){
	var trdIds = $("#melonInfoList").jqGrid('getGridParam','selarrrow');//选择多选
	if(0 == trdIds.length) {
		ctools.alert("请至少选择一条数据！","","warning");
        return;
	}
	trdIds = trdIds.join(",");
	var actionUrl =  BASE_PATH +"service/tradeManager/melonmdSetView.do?tradeAccos="+trdIds;
	openDialog(actionUrl);
}

/**
 * 表单验证
 */
function addFromValidate(){
	// 在键盘按下并释放及提交后验证提交表单
	$("#melonmdSet").validate({
	    rules: {
	    	trustType: {
    	        checkTrustType: true
    	    },
    	    melonmd: {
    	    	melonmdCheck: true
     	    },
     	    melonmdpercent: {
		    	required: true,
		    	checkMelonmd:true
		    },
		    checkno: {
     	    	checkNo: true
     	    },
     	    checkpwd: {
     	    	checkPwd: true
     	    }
	    },
	    messages: {
	    	trustType: {
	    		checkTrustType: "请选择委托方式！"
	        },
	        melonmd: {
	        	melonmdCheck: "请选择分红方式！"
	        },
	        melonmdpercent: {
	        	required:  "分红比例不能为空！",
	        	checkMelonmd: "分红比例请输入数字！"
	        },
	        checkno: {
	        	checkNo: "主管工号：必须填写！"
	        },
	        checkpwd: {
	        	checkPwd: "主管密码：必须填写！"
	        }
	    }
	});
};

/**
 *  验证委托类型
 */
jQuery.validator.methods["checkTrustType"]=(function(value,element){
	var success=false;
	var trustType = $("#trustType").val();
	if(trustType == "--" || trustType == ""){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证分红方式
 */
jQuery.validator.methods["melonmdCheck"]=(function(value,element){
	var success=false;
	var melonmd = $("#melonmd").val();
	if(melonmd == "" || melonmd == "--"){
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
 *  验证申购金额是否合法
 */
jQuery.validator.methods["checkMelonmd"]=(function(value,element){
	var melonmdpercent = $("#melonmdpercent").val();
	var success=false;
	if(!(melonmdpercent).match(/^(\d{1,}(|(\.{1}\d{0,2})))$|^(\.{1}\d{1,2})$/) && melonmdpercent != ""){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 * 全选
 */
function selectAll(){
	var isChecked = $("#checkAll").is(":checked");
	if(isChecked){
		$(".productInput").prop("checked",true);
	}else{
		$(".productInput").removeAttr('checked');
	}
}

/**
 * 验证并提交
 */
function checkSubmit(){
	var fundIds = $(".productInput:checked").length;
	if(1 > fundIds) {
		ctools.alert("请至少选择一个基金！","","warning");
        return;
	}
	
	if($("#melonmdSet").valid()){
		var checkno = $("#checkno").val(); 
		var checkpwd = $("#checkpwd").val();
		var flag = true;
		if(isAudit == 'Y'){
			$.ajax({
				type : "POST",
				async: false, 
				dataType : "json",
				url : BASE_PATH +'capitalService/server/checkPermission.xhtml',
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
		var fundIdArray = new Array();
		$(".productInput:checked").each(function(i){
		    fundIdArray[i] = $(this).val();
		});
		var findIds = fundIdArray.join(",");
		$(".findIds").val(findIds);
		duSubmit();
	}
}

/**
 * 提交分红方式设置数据
 */
function duSubmit(){
	$.ajax({
		type : "POST",
		async: false, 
		dataType : "json",
		url : BASE_PATH +'capitalService/server/setMelonmd.xhtml',
		data:$('#melonmdSet').serialize(),// 你的formid
		success : function(data) {
			if(data.errCode == "0000"){
				ctools.alert_sweet('提交成功！', "success", "申请编号："+data.serialno , function(){
					window.close();
				});
			}else{
				ctools.alert_sweet('提交失败！', "error", "申请编号："+data.serialno+"\n失败原因："+data.errMsg);
			}
		}
	});
}