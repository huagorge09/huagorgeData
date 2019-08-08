var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";

function setPath(path,accountPath){
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

$(function(){
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
    
    $('.select2').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$('.select_addr1').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
	$('.select_addr2').select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
    
    
    $("#btnQuery").mousedown(function()
    { 
    	queryInfo();
    });   
    $("#btnSubmit").mousedown(function()
    { 
    	addTradeAcco();
    });
    
    initValidata();
});

function queryInfo() //查询信息
{
	var fundAcc = trim($("#inputQueryFundAcc").val());
	var tradeAcc = trim($("#inputQueryTradeAcc").val());
	if (false === $("#query_account").valid()) {
		return false;
	}
	$.ajax({
		type : "post",
		url : ACCOUNT_PATH+"tradeAccoQuery.do",
		dataType : "json",
		data:{
			'fundAcc' : fundAcc,
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


//存放交易账号信息
function handleQuery(object)
{
   if(object==null)
   {
	   ctools.alert("没有找到对应的客户资料信息！","","warin");
   }
   else
   {
      var errcode = object.errcode;
      if(errcode == "0000")
      {
    	  $("#custNm").empty().append(object.invnm);
    	  $("#custTp").empty().append(object.invtpnm);
    	  $("#idTp").empty().append(object.idtp);
    	  $("#idNo").empty().append(object.idno);
    	  $("#custno").val(object.custno);
    	  $("#addr").empty().append(object.addr);
    	  $("#postcode").empty().append(object.postcode);
		 
    	  $("#bnkNo").val(object.bnkNo);
    	  $("#openName").empty().val(object.openName);
    	  $("#openAddr").val(object.openAddr);
    	  $("#openbankcity").val(object.openBankCity);
    	  $("#bankAcco").empty().val(object.bankAcco);
    	  $("#bankAccoNm").empty().val(object.bankAccoNm);
    	  //加载城市
    	  $("#hiddencity").val(object.openBankCity);
    	  
    	  $("#tano").select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');
    	  $("#trusttp").select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');
    	  $("#bnkNo").select2({allowClear: false,minimumResultsForSearch:Infinity}).trigger('change');
    	  $("#openAddr").select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"}).trigger('change');
    	  
    	  getCitys(object.openAddr);
      }else{
    	  ctools.alert(""+object.errmsg+"","","warin");
      }
	}
}


//获取城市集合
function getCitys(pmco) {
	var city = $("#openbankcity");
	var html = "<option value=''>--</option>";
	if(pmco == ""){
		$(city).html(html);
		$(city).select2("destroy");
		$(city).select2({allowClear: false,minimumResultsForSearch:Infinity,width: "123px"});
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
					if(item.PMCO == $("#hiddencity").val()){
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


function addTradeAcco() {
	var qParam = {};
	qParam.custNo = $("#custno").val();
	qParam.openName = $("#openName").val();
	qParam.bankAccoNm = $("#bankAccoNm").val();
	qParam.openAddr = $("#openAddr").val();
	qParam.openBankCity = $("#openbankcity").val();
	qParam.bnkNo = $("#bnkNo").val();
	qParam.bankAcco = $("#bankAcco").val();
	qParam.permissionId = $("#permissionId").val();
	qParam.operatorId = $("#operatorId").val();
	qParam.trustType = $("#trusttp").val();
	qParam.tano = $("#tano").val();
	if (qParam.tano == "") {
		ctools.alert("请选择TA代码!","","error");
		$("#tano").focus();
		return false;
	}
	
	$.ajax({
		url: ACCOUNT_PATH+"addTradeAccount.do",  
		dataType: "json",
		type: "POST",
		data:  {
			"tradeInfo" : JSON.stringify(qParam)
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