var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
});


var queryDetailList=$('#fundInfoList');

function initGrid(colNames){
	queryDetailList.jqGrid({
		url :PRIMARY_PATH + '/getAllFundInfoListPage.xhtml',
		caption : '<button type="button" class="btn btn-default btn-sm btn-small btn-fullscreen" title="" tabindex="-1"><i class="fa fa-arrows-alt icon-fullscreen"></i></button>',
		datatype : "json",
		colNames : colNames,
		colModel : [ 
		             {name : 'fundSt',index : 'fundSt',hidden : true, key : false,sortable : false},
		             {name : 'fundType',index : 'fundType',hidden : true, key : false,sortable : false},
		             {name : 'currencyType',index : 'currencyType',hidden : true, key : false,sortable : false},
		             {name : 'fundId',index : 'fundId',width : 100,hidden : false,key : true,sortable : false},
		             {name : 'fundNm',index : 'fundNm',hidden : false, key : false,sortable : false}, 
		             {name : 'fundStNm',index : 'fundStNm',hidden : false, key : false,sortable : false},
		             {name : 'fundTypeNm',index : 'fundTypeNm',hidden : false, key : false,sortable : false},
		             {name : 'taNo',index : 'taNo',hidden : false, key : false,sortable : false},
		             {name : 'currencyTypeNm',index : 'currencyTypeNm',hidden : false, key : false,sortable : false},
		             {name : 'updateTime',index : 'updateTime',hidden : false, key : false,sortable : false},
		             {name: 'option', index: 'option', width: 80, resizable:true, resizable: true, sortable: false }
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
		pager : "#fundInfoPage",
		multiselect: false,
        viewrecords: true,
        hidegrid: false,
		subGrid: false,
		gridComplete : function() {
		   var ids = queryDetailList.jqGrid('getDataIDs');
           for (var i = 0; i < ids.length; i++) {
        	   var id = ids[i];					 						    
        	   var rowData = queryDetailList.jqGrid('getRowData', id);	
			   var fundId = rowData.fundId;
        	   var se = '<a href="javascript:void(0)" class="btn btn-link btn-jqgrid" onclick="updateFundPage(\''+fundId+'\')" title="修改"><i class="fa fa-pencil-square-o"></i></a>';
        	   var be = '<a href="javascript:void(0)" class="btn btn-link btn-jqgrid" onclick="deleteFundInfo(\''+fundId+'\')" title="删除"><i class="fa fa-trash-o"></i></a>'; 
        	   queryDetailList.jqGrid('setRowData',ids[i],{option: se+be });
           }
		}
	});
	queryDetailList.navGrid('#fundInfoPage', {
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
function queryByCondtion(flag){
	var fundid = $("#fundid").val();
	var fundname= $("#fundname").val();
    var postData = queryDetailList.jqGrid("getGridParam", "postData");
    $.extend(postData,{
    	'sp[fundid]': fundid,
    	'sp[fundname]': fundname,
    });
    if (flag) {
    	queryDetailList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid",[{page:1}]);//重新载入Grid表格
    } else {
    	queryDetailList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
    }
}

/**
 * 删除基金
 * @param fundId
 */
function deleteFundInfo(fundId){
	ctools.confirm("您确认要删除该数据吗？",function(){
		$.ajax({
			url:PRIMARY_PATH +  '/deleteFundInfo.xhtml',   
			dataType: "json",
			type: "POST",
			scriptCharset:"UTF-8",
			data:{
				'fundId':fundId
				},
			cache: false,
			async: true,
			success: function(data) {
				if(data.resultCode=='0000'){
					ctools.alert_sweet('删除成功！', "success", "");
			    	queryDetailList.jqGrid("setGridParam",{"datatype": "json"}).trigger("reloadGrid");//重新载入Grid表格
					queryByCondtion(true);
				}else{
					ctools.alert_sweet('删除失败！', "error", "");
				}
			}
		});
	});
}

/**
 * 打开修改基金页面
 * @param fundid
 * @param workdate
 */
function updateFundPage(fundId){
	var actionUrl = PRIMARY_PATH +  "/addOrUpdateFundPage.xhtml?fundId="+fundId+"&method=update";
	openDialog(actionUrl);
}

/**
 * 打开新增基金页面
 * @param fundid
 * @param workdate
 */
function openAddFundPage(){
	var actionUrl = PRIMARY_PATH +  "/addOrUpdateFundPage.xhtml?fundId=&method=add";
	openDialog(actionUrl);
}

/**
 * 加载页面所需数据
 * @param fundId
 */
function loadData(){
	$.ajax({
		url:PRIMARY_PATH +'/loadData.xhtml',   
		dataType: "json",
		type: "POST",
		cache: false,
		async: true,
		scriptCharset:"UTF-8",
		success: function(data) {
			var txtBITaNO = $(".txtBITaNO").val();
			var isUnFund = $(".isUnFund").val();
			var selBICurrencyType = $(".selBICurrencyType").val();
			var selBIInverstDirect = $(".selBIInverstDirect").val();
			var selISNavFracMode = $(".selISNavFracMode").val();
			var selISFundType = $(".selISFundType").val();
			var selISFundRiskLevel = $(".selISFundRiskLevel").val();
			var selISFundSt = $(".selISFundSt").val();
			var selFundDispTp = $(".selFundDispTp").val();
			var parmList = data.parmList;
			var currencyTypeList = data.currencyTypeList;
			var inverstList = data.inverstList;
			var fundTpList = data.fundTpList;
			var fundRiskLevelList = data.fundRiskLevelList;
			var fundStList = data.fundStList;
			var fundDispTpList = data.fundDispTpList;
			var navFracModeList = data.navFracModeList;
			var html = "";
			var currencyTypeHtml = "";
			var inverstHtml = "";
			var fundTpHtml = "";
			var fundRiskLevelHtml = "";
			var fundStHtml = "";
			var fundDispTpHtml = "";
			var navFracModeHtml = "";
			for (var i = 0; i < parmList.length; i++) {
				var parm = parmList[i];
				html+="<option value="+parm.pmco+">"+parm.pmnm+"</option>";
			}
			for (var j = 0; j < currencyTypeList.length; j++) {
				var currencyType = currencyTypeList[j];
				currencyTypeHtml+="<option value="+currencyType.pmco+">"+currencyType.pmnm+"</option>";
			}
			for (var k = 0; k < inverstList.length; k++) {
				var inverst = inverstList[k];
				inverstHtml+="<option value="+inverst.pmco+">"+inverst.pmnm+"</option>";
			}
			for (var l = 0; l < fundTpList.length; l++) {
				var fundTp = fundTpList[l];
				fundTpHtml+="<option value="+fundTp.pmco+">"+ fundTp.pmnm+"</option>";
			}
			for (var m = 0; m < fundRiskLevelList.length; m++) {
				var fundRiskLevel = fundRiskLevelList[m];
				fundRiskLevelHtml+="<option value="+fundRiskLevel.pmco+">"+fundRiskLevel.pmnm+"</option>";
			}
			for (var n = 0; n < fundStList.length; n++) {
				var fundSt = fundStList[n];
				fundStHtml+="<option value="+fundSt.pmco+">"+fundSt.pmnm+"</option>";
			}
			for (var s = 0; s < fundDispTpList.length; s++) {
				var fundDispTp = fundDispTpList[s];
				fundDispTpHtml+="<option value="+fundDispTp.pmco+">"+fundDispTp.pmnm+"</option>";
			}
			for (var t = 0; t < navFracModeList.length; t++) {
				var navFracMode = navFracModeList[t];
				navFracModeHtml+="<option value="+navFracMode.pmco+">"+navFracMode.pmnm+"</option>";
			}
			$("#txtBITaNO").html(html);
			$("#selBICurrencyType").html(currencyTypeHtml);
			$("#selBIInverstDirect").html(inverstHtml);
			$("#selISFundType").html(fundTpHtml);
			$("#selISFundRiskLevel").html(fundRiskLevelHtml);
			$("#selISFundSt").html(fundStHtml);
			$("#selFundDispTp").html(fundDispTpHtml);
			$("#selISNavFracMode").html(navFracModeHtml);
			
			if(txtBITaNO != "" && txtBITaNO != null){
				$("#txtBITaNO").val(txtBITaNO);
			}
			if(isUnFund != "" && isUnFund != null){
				$("#isUnFund").val(isUnFund);
			}
			if(selBICurrencyType != "" && selBICurrencyType != null){
				$("#selBICurrencyType").val(selBICurrencyType);
			}
			if(selBIInverstDirect != "" && selBIInverstDirect != null){
				$("#selBIInverstDirect").val(selBIInverstDirect);
			}
			if(selISNavFracMode != "" && selISNavFracMode != null){
				$("#selISNavFracMode").val(selISNavFracMode);
			}
			if(selISFundType != "" && selISFundType != null){
				$("#selISFundType").val(selISFundType);
			}
			if(selISFundRiskLevel != "" && selISFundRiskLevel != null){
				$("#selISFundRiskLevel").val(selISFundRiskLevel);
			}
			if(selISFundSt != "" && selISFundSt != null){
				$("#selISFundSt").val(selISFundSt);
			}
			if(selFundDispTp != "" && selFundDispTp != null){
				$("#selFundDispTp").val(selFundDispTp);
			}
			$('.select2').select2({allowClear: false,minimumResultsForSearch:Infinity});
			$("#selISFundType").change();
		}
	});
}
function addFromValidate(){
	// 在键盘按下并释放及提交后验证提交表单
	$("#fundInfoForm").validate({
	    rules: {
	    	txtBINav:{
	    		checkBINav:true
	    	},
	    	txtISManagerRates:{
	    		checkManagerRates:true
	    	},
	    	txtBIFundId: {
    	        required: true,
    	        maxlength: 6,
    	    	minlength: 6,
    	    	checkFundId: true
    	    },
    	    txtBIFundNm: {
     	        required: true,
     	        maxlength: 20
     	    },
		    txtISNavFracNum: {
		    	required: true,
		    	digits:true
		    },
		    txtCyclelen: {
		    	required: true,
		    	digits:true
		    },
		    txtPFundid: {
		    	maxlength: 6,
    	    	minlength: 6
		    },
		    txtProductTimeLimit: {
		    	digits:true
		    },
		    txtLSRedeemToAcctDays: {
		    	required: true,
		    	digits:true,
		    	maxlength:1
		    },
		    txtMelonDays: {
		    	required: true,
		    	digits:true,
		    	maxlength:1
		    },
		    txtSubDays: {
		    	required: true,
		    	digits:true,
		    	maxlength:1
		    },
		    txtBIFundDenomina:{
		    	checkFundDenomina:true
		    },
		    txtISIssuePrice:{
		    	checkIssuePrice:true
		    }
	    },
	    messages: {
	    	txtBINav:{
	    		checkBINav:"基金单位净值最多只能为3位整数4位小数！"
	    	},
	    	txtISManagerRates:{
	    		checkManagerRates:"管理费率最多只能为1位整数4位小数！"
	    	},
	    	txtBIFundId: {
	            required: "基金代码不能为空！",
	            maxlength: "基金代码必须为6位！",
		    	minlength: "基金代码必须为6位！",
		    	checkFundId: "基金代码已存在！"
	        },
	        txtBIFundNm: {
	            required: "基金名称不能为空！",
	            maxlength: "基金名称不能超过20位！"
	        },
	        txtISNavFracNum: {
	        	required:  "净值小数位数不能为空！",
	        	digits:    "净值小数位数请输入数字！"
	        },
	        txtCyclelen: {
	        	required:  "周期长度不能为空！",
	        	digits:    "周期长度请输入数字！"
	        },
	        txtPFundid: {
	        	maxlength: "母基金代码必须为6位！",
		    	minlength: "母基金代码必须为6位！"
	        },
	        txtProductTimeLimit: {
	        	digits:    "产品期限请输入数字！"
	        },
	        txtLSRedeemToAcctDays: {
	        	required:  "赎回款到账日期不能为空！",
	        	digits:    "赎回款到账日期必须为整数！",
		    	maxlength:"赎回款到账日期只能为1位整数！"
	        },
	        txtMelonDays: {
	        	required:  "分红划款日期不能为空！",
	        	digits:    "分红划款日期必须为整数！",
		    	maxlength:"分红划款日期只能为1位整数！"

	        },
	        txtSubDays: {
	        	required:  "认申购划款日期不能为空！",
	        	digits:    "认申购划款日期必须为整数！",
		    	maxlength:"认申购划款日期只能为1位整数！"
	        },
	        txtBIFundDenomina:{
		    	checkFundDenomina:"基金面值最多只能为3位整数4位小数！"
		    },
		    txtISIssuePrice:{
		    	checkIssuePrice:"发行价格最多只能为3位整数4位小数！"
		    }
	    }
	});
};

function addFromValidate_upd(){
	// 在键盘按下并释放及提交后验证提交表单
	$("#fundInfoForm").validate({
	    rules: {
	    	txtBINav:{
	    		checkBINav:true
	    	},
	    	txtISManagerRates:{
	    		checkManagerRates:true
	    	},
	    	txtBIFundId: {
    	        required: true,
    	        maxlength: 6,
    	    	minlength: 6
    	    },
    	    txtBIFundNm: {
     	        required: true,
     	        maxlength: 20
     	    },
		    txtISNavFracNum: {
		    	required: true,
		    	digits:true
		    },
		    txtCyclelen: {
		    	required: true,
		    	digits:true
		    },
		    txtPFundid: {
		    	maxlength: 6,
    	    	minlength: 6
		    },
		    txtProductTimeLimit: {
		    	digits:true
		    },
		    txtLSRedeemToAcctDays: {
		    	required: true,
		    	digits:true,
		    	maxlength:1
		    },
		    txtMelonDays: {
		    	required: true,
		    	digits:true,
		    	maxlength:1
		    },
		    txtSubDays: {
		    	required: true,
		    	digits:true,
		    	maxlength:1
		    },
		    txtBIFundDenomina:{
		    	checkFundDenomina:true
		    },
		    txtISIssuePrice:{
		    	checkIssuePrice:true
		    }
	    },
	    messages: {
	    	txtBINav:{
	    		checkBINav:"基金单位净值最多只能为3位整数4位小数！"
	    	},
	    	txtISManagerRates:{
	    		checkManagerRates:"管理费率最多只能为1位整数4位小数！"
	    	},
	    	txtBIFundId: {
	            required: "基金代码不能为空！",
	            maxlength: "基金代码必须为6位！",
		    	minlength: "基金代码必须为6位！"
	        },
	        txtBIFundNm: {
	            required: "基金名称不能为空！",
	            maxlength: "基金名称不能超过20位！"
	        },
	        txtISNavFracNum: {
	        	required:  "净值小数位数不能为空！",
	        	digits:    "净值小数位数请输入数字！"
	        },
	        txtCyclelen: {
	        	required:  "周期长度不能为空！",
	        	digits:    "周期长度请输入数字！"
	        },
	        txtPFundid: {
	        	maxlength: "母基金代码必须为6位！",
		    	minlength: "母基金代码必须为6位！"
	        },
	        txtProductTimeLimit: {
	        	digits:    "产品期限请输入数字！"
	        },
	        txtLSRedeemToAcctDays: {
	        	required:  "赎回款到账日期不能为空！",
	        	digits:    "赎回款到账日期必须为整数！",
		    	maxlength:"赎回款到账日期只能为1位整数！"

	        },
	        txtMelonDays: {
	        	required:  "分红划款日期不能为空！",
	        	digits:    "分红划款日期必须为整数！",
		    	maxlength:"分红划款日期只能为1位整数！"
	        },
	        txtSubDays: {
	        	required:  "认申购划款日期不能为空！",
	        	digits:    "认申购划款日期必须为整数！",
		    	maxlength:"认申购划款日期只能为1位整数！"
	        },
	        txtBIFundDenomina:{
		    	checkFundDenomina:"基金面值最多只能为3位整数4位小数！"
		    },
		    txtISIssuePrice:{
		    	checkIssuePrice:"发行价格提示最多只能为3位整数4位小数！"
		    }
	    }
	});
};

/**
 *  检查菜单URL是否存在
 */

//校验基金单位净值

jQuery.validator.methods["checkBINav"]=(function(value,element){
	//基金面额
	var txtBINav = $("#txtBINav").val();
	var reg = /^\d{1,3}(?:\.\d{1,4})?$/;
	var success = false;
	if(txtBINav == null || txtBINav == ''){
		success = true;
	}else{
		if(reg.test(txtBINav)){
			success = true;
		}
	}
	return success;
});

//校验管理费率

jQuery.validator.methods["checkManagerRates"]=(function(value,element){
	var txtISManagerRates = $("#txtISManagerRates").val();
	var reg = /^\d{1}(?:\.\d{1,4})?$/;
	var success = false;
	if(txtISManagerRates == null || txtISManagerRates == ''){
		success = true;
	}else{
		if(reg.test(txtISManagerRates)){
			success = true;
		}
	}
	return success;
});

jQuery.validator.methods["checkFundId"]=(function(value,element){
	var txtBIFundId=$("#txtBIFundId").val();
	var success=false;
	$.ajax({
		type : "POST",
		async: false, 
		dataType : "json",
		scriptCharset:"UTF-8",
		url : PRIMARY_PATH + '/getChkFundInfo.xhtml',
		data:{'fundId':txtBIFundId},
		success : function(data) {
			if (data.resultCode=='0000') {
				//不存在同名才提交
				success= true;
			}else{
				success= false;
			}
		},
		error : function(data) {
			success= false;
		}
	});
	return success;
});
//基金面额校验
jQuery.validator.methods["checkFundDenomina"]=(function(value,element){
	//基金面额
	var txtBIFundDenomina = $("#txtBIFundDenomina").val();
	var reg = /^\d{1,3}(?:\.\d{1,4})?$/;
	var success = false;
	if(txtBIFundDenomina == null || txtBIFundDenomina == ''){
		success = true;
	}else{
		if(reg.test(txtBIFundDenomina)){
			success = true;
		}
	}
	return success;
});
//发行价格校验
jQuery.validator.methods["checkIssuePrice"]=(function(value,element){
	//发行价格
	var txtISIssuePrice = $("#txtISIssuePrice").val();
	var reg = /^\d{1,3}(?:\.\d{1,4})?$/;
	var success=false;
	if(txtISIssuePrice == null || txtISIssuePrice == ''){
		success = true;
	}else{
		if(reg.test(txtISIssuePrice)){
			success = true;
		}
	}
	return success;
});

function setFinanceCycle(ele){
	var selCycletp = $(".selCycletp").val();
	var txtCyclelen = $(".txtCyclelen").val();
	var method = $("#method").val();
	if(ele.value == "5"){
		var html = "";
		html+='<td>周期类型</td>';
		html+='<td class="white-bg form-inner">';
		html+='<select name="selCycletp" id="selCycletp" class="select2">';
		html+='<option value="0">月</option>';
		html+='<option value="1">日</option>';
		html+='</select>';
		html+='</td>';
		html+='<td>周期长度</td>';
		html+='<td class="white-bg form-inner">';
		html+='<input type="text" name="txtCyclelen"  class="form-control" id="txtCyclelen" value="">';
		html+='</td>';
		$("#financeTR").html(html);
		$("#financeTR").show();
		$("#syschonizeTD").show();
		if(selCycletp != "" && selCycletp != null){
			$("#selCycletp").val(selCycletp);
		}
		$("#txtCyclelen").val(txtCyclelen);
		if(method=="check"){
			$("#selCycletp").attr("disabled",true);
			$("#txtCyclelen").attr("readonly",true);
		}
		$('#selCycletp').select2({allowClear: false,minimumResultsForSearch:Infinity});
	}else{
		$("#financeTR").hide();
		$("#financeTR").html("");
		$("#syschonizeTD").hide();
	}
}


function syschonizeSetupDate(){ //同步成立日期
	var txtBIFundId = $("#txtBIFundId").val();
	var txtISSetUpDate = $("#txtISSetUpDate").val();
	if(txtBIFundId == ""){
		toastr.warning('', '基金代码不能为空！');
		return;
	}
	if(txtISSetUpDate == ""){
		toastr.warning('', '基金成立日期不能为空！');
		return;
	}
	$.ajax({
		url: PRIMARY_PATH + '/syschonizeSetupDate.xhtml',   
		dataType: "json",
		type: "POST",
		scriptCharset:"UTF-8",
		data:$('#fundInfoForm').serialize(),// 你的formid
		cache: false,
		async: true,
		success: function(data) {
			if(data.resultCode=='0000'){
				toastr.success('', '成立日期同步成功！');
			}else{
				ctools.alert_sweet('同步失败！', "error", data.resultMessage);
			}
		}
	});
}

function addOrUpdateFundInfo(){
	var txtBIFundId = $("#txtBIFundId").val();
	var txtBIFundNm = $("#txtBIFundNm").val();
	var txtISNavFracNum = $("#txtISNavFracNum").val();
	var txtLSRedeemToAcctDays = $("#txtLSRedeemToAcctDays").val();
	var txtMelonDays = $("#txtMelonDays").val();
	var txtSubDays = $("#txtSubDays").val();
	var reg = /^\d{1,3}(?:\.\d{1,4})?$/;
	var reg2 = /^\d{1}(?:\.\d{1,4})?$/;
	var txtBINav = $("#txtBINav").val();
	var txtBIFundDenomina = $("#txtBIFundDenomina").val();
	var txtISIssuePrice = $("#txtISIssuePrice").val();
	var txtISManagerRates = $("#txtISManagerRates").val();
	if((txtBIFundId == null || txtBIFundId == '') || (txtBIFundNm == null || txtBIFundNm == '') || 
			(!reg.test(txtBIFundDenomina) && txtBIFundDenomina != '') || (!reg.test(txtBINav) && txtBINav != '')){
		
		showDiv('divFundBaseInfo');
		
	}else if(txtISNavFracNum == null || txtISNavFracNum == '' || (!reg.test(txtISIssuePrice) && txtISIssuePrice != '') || 
			(!reg2.test(txtISManagerRates) && txtISManagerRates != '')){
		
		showDiv('divFundIssueSet');
		
	}else if((txtLSRedeemToAcctDays == null || txtLSRedeemToAcctDays == '') || (txtMelonDays == null
			|| txtMelonDays == '') || (txtSubDays == null || txtSubDays == '')){
		
		showDiv('divFundLimitSet');
		
	}
	if($("#fundInfoForm").valid()){
		//提交表单
		$.ajax({
			url: PRIMARY_PATH + '/addOrUpdateFundInfo.xhtml',   
			dataType: "json",
			type: "POST",
			scriptCharset:"UTF-8",
			data:$('#fundInfoForm').serialize(),// 你的formid
			cache: false,
			async: true,
			success: function(data) {
				if(data.resultCode=='0000'){
					window.location.href= BASE_PATH +  "service/jsp/hint/success.jsp";
				}else{
					ctools.alert_sweet('操作失败', "error", "");
				}
			}
		});
	}
}

function showDiv(divId){
	$(".fundDiv").hide();
	$("#"+divId).show();
}

/**
 * 复核基金信息
 */
function checkFundInfo(){
	var txtBIFundId = $("#txtBIFundId").val();
	var status = $("#status").val();
	$.ajax({
		url:PRIMARY_PATH +  '/checkFundInfo.xhtml',
		scriptCharset:"UTF-8",
		dataType: "json",
		type: "POST",
		data:{
			'fundId' : txtBIFundId, 
			'status' : status
		},// 你的formid
		cache: false,
		async: true,
		success: function(data) {
			if(data.resultCode=='0000'){
				window.location.href= BASE_PATH +  "service/jsp/hint/success.jsp";
			}else{
				ctools.alert_sweet(data.resultMessage, "error", "");
			}
		}
	});
}