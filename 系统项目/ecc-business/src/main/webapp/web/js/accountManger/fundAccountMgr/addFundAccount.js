var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";

function setPath(path,accountPath){
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

$(function() {
	// 注册清空事件
	WASP_WIDGET.registerResetClearEvent();

	$('.select2_init').select2({
		allowClear : true,
		minimumResultsForSearch : Infinity
	});

	$("#btnQuery").mousedown(function() {
		queryInfo();
	});
	
	$("#btnSubmit").mousedown(function() {
		addFundAcco();
	});
	
	$("#seltradeacco").change(function() {
		var seltradeacco = $("#seltradeacco").val();
		fillBankInfo(seltradeacco);
	});
	
	initValidata();
});

function fillBankInfo(object) {

	if (object == '1') {
		$("#pbankAccoNm").empty();
		$("#pbankAcco").empty();
		$("#pbnkNo").empty();
		$("#popenName").empty();
		$("#popenAddr").empty();
		$("#tradeacco").val("");
	} else if (object == '0') {
		$("#obankAccoNm").empty();
		$("#obankAcco").empty();
		$("#obnkNo").empty();
		$("#oopenName").empty();
		$("#oopenAddr").empty();
		$("#tradeacco").val("");
	} else {
		var dataObj = eval("(" + object + ")");
		var invtp = dataObj.invtp;
		if (invtp == '1') {
			$("#pbankAccoNm").empty().append(dataObj.bankAcNm);
			$("#pbankAcco").empty().append(dataObj.bankAcco);
			$("#pbnkNo").empty().append(dataObj.bankNo);
			$("#popenName").empty().append(dataObj.openName);
			$("#popenAddr").empty().append(dataObj.openAddrNm);
		} else {
			$("#obankAccoNm").empty().append(dataObj.bankAcNm);
			$("#obankAcco").empty().append(dataObj.bankAcco);
			$("#obnkNo").empty().append(dataObj.bankNo);
			$("#oopenName").empty().append(dataObj.openName);
			$("#oopenAddr").empty().append(dataObj.openAddrNm);
		}
		$("#tradeacco").val(dataObj.tradeAcco);
	}
}

function fillData(object) {
	var invtp = object.invtp;
	if (invtp == '1') {
		$("#pinvnm").empty().append(object.invnm);
		$("#pinvtp").empty().append('个人投资者');
		$("#pidtp").empty().append(object.idtp);
		$("#pidno").empty().append(object.idno);
		$("#pidvalidate").empty().append(object.idvalidate);
	} else {
		$("#oinvnm").empty().append(object.invnm);
		$("#oinvtp").empty().append('机构投资者');
		$("#oidtp").empty().append(object.idtp);
		$("#oidno").empty().append(object.idno);
		$("#oidvalidate").empty().append(object.idvalidate);

		$("#oinstrepnm").empty().append(object.instrepnm);
		$("#oinstrepnation").empty().append(object.instrepnation);
		$("#oinstrepidtp").empty().append(object.instrepidtp);
		$("#oinstrepidno").empty().append(object.instrepidno);
		$("#oinstrepvalidate").empty().append(object.instrepvalidate);

		$("#oprincipalname").empty().append(object.principalname);
		$("#oprincipalnation").empty().append(object.principalnation);
		$("#oprincipalidtp").empty().append(object.principalidtp);
		$("#oprincipalidno").empty().append(object.principalidno);
		$("#oprincipalvalidt").empty().append(object.principalvalidt);

		$("#ocontactgrant").empty().append(object.contactgrant);
		$("#ocontact").empty().append(object.contact);
		$("#ocontactnation").empty().append(object.contactnation);
		$("#ocontidtp").empty().append(object.contidtp);
		$("#ocontidno").empty().append(object.contidno);
		$("#ocontvalidate").empty().append(object.contvalidate);
		$("#ocontphone").empty().append(object.contphone);
		$("#ocontfax").empty().append(object.contfax);
		$("#ocontmobile").empty().append(object.contmobile);
		$("#ocontemail").empty().append(object.contemail);
	}

	var tradeacco = object.tradeAccoList;
	$("#seltradeacco").empty();// 清除原有记录
	if (invtp == '1') {
		$("#seltradeacco").append("<option value='1' selected>&nbsp;&nbsp;&nbsp;&nbsp;--------&nbsp;&nbsp;</option>");
	} else {
		$("#seltradeacco").empty().append("<option value='0' selected>&nbsp;&nbsp;&nbsp;&nbsp;--------&nbsp;&nbsp;</option>");
	}
	if (Number(tradeacco.length) > 0) {
		for (var i = 0; i < Number(tradeacco.length); i++) {
			tradeacco[i].invtp = invtp;
			$("#seltradeacco").append('<option value="' + JsonToStr(tradeacco[i]) + '">' + tradeacco[i].tradeAcco + "</option>");
		}
	}
	$('.select2_init').select2({
		allowClear : true,
		minimumResultsForSearch : Infinity
	});
}

function queryInfo() // 查询信息
{
	var funcAcc = trim($("#inputQueryFundAcc").val());
    var tradeAcc = trim($("#inputQueryTradeAcc").val());
    if (false === $("#query_account").valid()) {
		return false;
	}
	$.ajax({
		type : "post",
		url : ACCOUNT_PATH+"tradeAccoQuery.do",
		dataType : "json",
		data:{
			'fundAcc' : funcAcc,
			'tradeAcc' : tradeAcc
		},
		success : function(data) {
			handleQuery(data);
		},
		error : function() {
			ctools.alert("客户资料信息查询失败！","","error");
		}
	});
}

function handleQuery(object) {
	var errcode = object.errcode;
	if (errcode == "0000") {
		var invtp = object.invtp;
		if (invtp == '1') {
			$("#inputTable").show();
			$("#showPersonalTable").show();
			$("#showOrgTable").hide();
			$("#submitTable").show();
			fillData(object);
		} else {
			$("#inputTable").show();
			$("#showPersonalTable").hide();
			$("#showOrgTable").show();
			$("#submitTable").show();
			fillData(object);
		}
	} else {
		ctools.alert(""+object.errmsg+"","","warin");
	}
}


function JsonToStr(o) {
	var arr = [];
	var fmt = function(s) {
		if (typeof s == 'object' && s != null)
			return JsonToStr(s);
		return /^(string|number)$/.test(typeof s) ? "'" + s + "'" : s;
	};
	for ( var i in o)
		arr.push("'" + i + "':" + fmt(o[i]));
	return '{' + arr.join(',') + '}';
};


function addFundAcco()
{
   var qParam = {}; 
   qParam.tradeacco =  $("#tradeacco").val();
   qParam.tano = $("#tano").val();
   qParam.trustType = $("#trustType").val();
   qParam.permissionId = $("#permissionId").val();
   qParam.operatorId = $("#operatorId").val();
   
   $.ajax({
		url: ACCOUNT_PATH+"addFundAccount.do",  
		dataType: "json",
		type: "POST",
		data:  {
			"fundInfo" : JSON.stringify(qParam)
		},
		cache: false,
		async: false,
		success: function(data) {
			var errcode = data.errcode;
		    var errMsg =data.errmsg;
		    var serialNo = data.serialno;
		    if(errcode == "0000"){
		    	ctools.alert_sweet('申请提交成功！', "success", "申请编号："+serialNo , function(){
					window.location.reload();
				});
		    }else{
		    	ctools.alert_sweet('申请提交失败！', "error", "失败原因："+errMsg);
		    }
		}
	});
}

//表单校验
function initValidata() {
	$("#query_account").validate({
		focusCleanup : false,
		groups : {
			username : "inputQueryTradeAcc inputQueryFundAcc"
		},
		errorPlacement : function(error, element) { // 错误提示在什么地方
			error.insertAfter(element);
		},
		rules : {
			inputQueryTradeAcc : {
				required : {
					depends : function() { // 二选一
						return ($('input[name=inputQueryFundAcc]').val().length <= 0);
					}
				}
			},
			inputQueryFundAcc : {
				required : {
					depends : function() { // 二选一
						return ($('input[name=inputQueryTradeAcc]').val().length <= 0);
					}
				}
			}
		},
		messages : { // 提示报错
			inputQueryTradeAcc : "请输入交易账号或者基金账号！",
			inputQueryFundAcc : "请输入交易账号或者基金账号！"
		},
		debug : true
	});	
};