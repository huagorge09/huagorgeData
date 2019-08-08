var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";
var custNo ;
function setPath(path,accountPath){
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

$(function(){
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
    
    $('.select2_init').select2({allowClear: false,minimumResultsForSearch:Infinity});
    $('.select2_bank').select2();
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
    
    
    $("#btnQuery").mousedown(function()
    { 
    	queryInfo();
    });
    
    initValidata();
});


function queryInfo(){
	var fundacco = $("#inputQueryFundAcc").val();
	var idno = $("#inputQueryIdno").val();
	if (false === $("#query_account").valid()) {
		return false;
	}
	$.ajax({
		type : "post",
		url : ACCOUNT_PATH+"getAccountInfo.do",
		dataType : "json",
		data:{
			'idno' : idno,
			'fundacct' : fundacco
		},
		success : function(data) {
			handleQuery(data);
		},
		error : function() {
			ctools.alert("客户资料信息查询失败！","","error");
		}
	});
}


function handleQuery(account){
	custNo = account.custno; 
	$("#pslInvNm").val(account.invnm);
	$("#pslInvIdno").val(account.idno);
	if(account.investorinfo != undefined && account.investorinfo != ""){
		$("#pslInvIdValidate").val(account.investorinfo.idnoLimit);
	}else{
		ctools.alert("客户信息不存在！","","warin");
		$("#pslInvIdValidate").val("");
	}
	$("#pslInvIdtp").val(account.idtp);
	
	if($("#inputQueryFundAcc").val() != ""){
		$("#fundacc").val($("#inputQueryFundAcc").val());
	}else{
		var fundaccostr = "";
		var fundAccos = account.fundaccolist;
		$(fundAccos).each(function(index,obj){
			fundaccostr = fundaccostr + obj.fundacco + ",";
		});
		if(fundaccostr.length > 0){
			fundaccostr = fundaccostr.substring(0,fundaccostr.length - 1);
		}
		$("#fundacc").val(fundaccostr);
	}
	
	
	if(account.investorinfo != undefined && account.investorinfo != "" 
		&& account.investorinfo.address != undefined && account.investorinfo.address != ""){
	
		$("#pslInvOfficeTel").val(account.investorinfo.address.tel);
		$("#pslInvHomeTel").val(account.investorinfo.address.tel);
		$("#pslInvMobile").val(account.investorinfo.address.mobile);
		$("#pslInvFax").val(account.investorinfo.address.fax);
		$("#pslInvEmail").val(account.investorinfo.address.email);
		$("#pslAddr").val(account.investorinfo.address.addr);
		$("#pslPostCode").val(account.investorinfo.address.postcode);
	}else{
		$("#pslInvOfficeTel").val("");
		$("#pslInvHomeTel").val("");
		$("#pslInvMobile").val("");
		$("#pslInvFax").val("");
		$("#pslInvEmail").val("");
		$("#pslAddr").val("");
		$("#pslPostCode").val("");
	}
	
	if(account.investorinfo != undefined && account.investorinfo != "" ){
		if(!!account.investorinfo.sex && account.investorinfo.sex != null  && trim(account.investorinfo.sex) != '0'){
			$("#plsSex").val(account.investorinfo.sex);
		}
		if(!!account.investorinfo.nation && account.investorinfo.nation != null && trim(account.investorinfo.nation) != '0'){
			$("#plsInvNation").val(account.investorinfo.nation);
		}
		if(!!account.investorinfo.education && account.investorinfo.education != null && trim(account.investorinfo.education) != '0'){
			$("#plsInvEducation").val(account.investorinfo.education);
		}
		if(!!account.investorinfo.vocation && account.investorinfo.vocation != null && trim(account.investorinfo.vocation) != '0'){
			$("#plsInvJob").val(account.investorinfo.vocation);
		}
		if(!!account.investorinfo.income && account.investorinfo.income != null && trim(account.investorinfo.income) != '0'){
			$("#plsInvIncome").val(account.investorinfo.income);
		}
	}
	
	if(!!account.riskLevel){
		$("#plsInvRisk").val(account.riskLevel);
	}
	$("#bankacconm").val(account.invnm);
	
	$('.select2_bank').select2("destroy");
	$('.select2_bank').select2().trigger('change');
	$('.select_addr1').select2("destroy");
	$('.select_addr2').select2("destroy");
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"}).trigger('change');
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"}).trigger('change');
	
	$('.select2_init').select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');
}

$("#btnSubmit").click(function(){
	var bnkNo = $("#selbankno").val(); 
	if(bnkNo == "other"){
		bnkNo =  $("#other").val();
	}
	var reqData = {
		trustType:$("#trusttp").val(),
		permissionId:$("#permissionId").val(),
		operatorId:$("#operatorId").val(),
		openAddr:$("#openlocation").val(),
		openbankcity:$("#openbankcity").val(),
		custNo:custNo,
		openName:$("#openname").val(),
		bankAccoNm:$("#bankacconm").val(),
		bnkNo:bnkNo,
		bankAcco:$("#bankacco").val(),
		tano: $("#tano").val(),
	};
	if(checkData(reqData)){
	    $.ajax({
			url: ACCOUNT_PATH+"addTradeAccount.do",  
			dataType: "json",
			type: "POST",
			data:  {
				"tradeInfo" : JSON.stringify(reqData)
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
});


function checkData(reqData){
	if(reqData.custNo == undefined || reqData.custNo == ""){
		ctools.alert("查询信息有误，请重新输入条件查询！","","error");
		return false;
	}
	if(reqData.tano == undefined || reqData.tano == ""){
		ctools.alert("请选择TA类型！","","error");
		return false;
	}
	if(reqData.bnkNo == undefined || reqData.bnkNo == ""){
		ctools.alert("请选择开户银行！","","error");
		return false;
	}
	if(reqData.openName == undefined || reqData.openName == ""){
		ctools.alert("请填写预留银行全称！","","error");
		return false;
	}
	if(reqData.bankAccoNm == undefined || reqData.bankAccoNm == ""){
		ctools.alert("请填写预留银行户名！","","error");
		return false;
	}
	if(reqData.bankAcco == undefined || reqData.bankAcco == ""){
		ctools.alert("请填写预留银行账号！","","error");
		return false;
	}
	if((reqData.openlocation == undefined || reqData.openlocation == "") && reqData.openbankcity == undefined || reqData.openbankcity == ""){
		ctools.alert("请选择预留银行开户地！","","error");
		return false;
	}
	return true;
}

$("#btnReset").click(function(){
	$("#form1")[0].reset();
	$("#other").remove();
	custNo = "";
	$('.select2_bank').select2("destroy");
	$('.select2_bank').select2().trigger('change');
	$('.select_addr1').select2("destroy");
	$('.select_addr2').select2("destroy");
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"}).trigger('change');
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"}).trigger('change');
	$('.select2_init').select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');
});

//获取城市集合
function getCitys(pmco, flag) {
	if (flag == '1') {
		city = $("#openbankcity");
		selectCity = "#hopenbankcity";
	} else {
		city = $("#orgopenbankcity");
		selectCity = "#horgopenbankcity";
	}
	var html = "<option value=''>--</option>";
	if(pmco == ""){
		$(city).html(html);
		$(city).change();
		return;
	}
	
	$.ajax({
		type : "post",
		url : PRIMARY_PATH+"queryMatchParamList.do?pmst=SYSTEM&pmky=DS_CITYCODE&pmv1="+pmco,
		dataType : "json",
		contentType : 'application/json;charset=utf-8',
		success : function(data) {
			if (!!data) {
				for (var i = 0; i < data.length; i++) {
					var item = data[i];
					if(item.PMCO == $(selectCity).val()){
						html+="<option value=\""+item.PMCO+"\" selected>"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}else{
						html+="<option value=\""+item.PMCO+"\">"+item.PMCO+"&nbsp;&nbsp;"+item.PMNM+"</option>";
					}
				}
				$(city).html(html);
				$(city).select2("destroy");
				$(city).select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
			}
		},
		error : function() {
			ctools.alert("城市信息查询失败！","","error");
		}
	});
}

//表单校验
function initValidata() {
	$("#query_account").validate({
		focusCleanup : false,
		groups : {
			username : "inputQueryFundAcc inputQueryIdno"
		},
		errorPlacement : function(error, element) { // 错误提示在什么地方
			error.insertAfter(element);
		},
		rules : {
			inputQueryFundAcc : {
				required : {
					depends : function() { // 二选一
						return ($('input[name=inputQueryIdno]').val().length <= 0);
					}
				}
			},
			inputQueryIdno : {
				required : {
					depends : function() { // 二选一
						return ($('input[name=inputQueryFundAcc]').val().length <= 0);
					}
				}
			}
		},
		messages : { // 提示报错
			inputQueryFundAcc : "请输入基金账号或者证件号码！",
			inputQueryIdno : "请输入基金账号或者证件号码！"
		},
		debug : true
	});	
};