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
		deleteTradeAcco();
	});
	
	$("#seltradeacco").change(function() {
		var seltradeacco = $("#seltradeacco").val();
		fillTaInfo(seltradeacco);
	});
	
	initValidata();
});

function fillTaInfo(obj)
{
   if(obj=='noselect')
   {
      $("#tano").empty();    
      $("#taname").empty();   
      $("#tradeacco").val(""); 
   }
   else
   {
      var dataObj=eval("("+obj+")");
      $("#tano").empty().append(dataObj.tano);  
      $("#taname").empty().append(dataObj.tanm); 
      $("#tradeacco").val(dataObj.fundacco);       
   } 
}

function handleQuery(object)
{
   var errcode = object.errcode;
   if(errcode == "0000")
   {
	   $("#inputTable").show();
	   $("#showPersonalTable").show();
	   $("#submitTable").show();
	   fillData(object);
   }else{
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

function fillData(object)
{
   $("#custno").val(object.custno);
   $("#idtp").empty().append(object.idtp);
   $("#idno").empty().append(object.idno);
   $("#custnm").empty().append(object.invnm);
   var fundacco = object.fundAccoList;
   
   $("#seltradeacco").empty();
   $("#seltradeacco").append("<option value='noselect' selected>&nbsp;&nbsp;&nbsp;&nbsp;--------&nbsp;&nbsp;</option>");
   if(Number(fundacco.length)>0)
   {
      for(var i=0;i<Number(fundacco.length);i++)
      {
         $("#seltradeacco").append('<option value="'+JsonToStr(fundacco[i])+'">'+fundacco[i].tano+"  "+fundacco[i].fundacco+"</option>");
      }
   }
   $("#seltradeacco").change();
   var custtp = object.invtp;
   $("#continfo").empty();
   if(custtp == '0'){
	   var contact = object.contact == null ? "" : object.contact;
	   var contidtpnm = object.contidtpnm == null ? "" : object.contidtpnm;
	   var contidno = object.contidno == null ? "" : object.contidno;
	   var contphone = object.contphone == null ? "" : object.contphone;
	   var contfax = object.contfax == null ? "" : object.contfax;
	   var html = "";
	   html+='<tr><td>经办人名称：</td><td class="white-bg" colspan="3"><span class="col-sm-12"><span>'+contact+'</span></span></td></tr>';
	   html+='<tr><td>经办人证件类型：</td><td class="white-bg"><span class="col-sm-12"><span>'+contidtpnm+'</span></span></td>';
	   html+='<td>经办人证件号码：</td><td class="white-bg"><span class="col-sm-12"><span>'+contidno+'</span></span></td></tr>';
	   html+='<tr><td>经办人电话：</td><td class="white-bg"><span class="col-sm-12"><span>'+contphone+'</span></span></td>';
	   html+='<td>经办人传真：</td><td class="white-bg"><span class="col-sm-12"><span>'+contfax+'</span></span></td></tr>';
	   $("#continfo").append(html);
	   var ocontactlist = object.ocontactlist;
	   if(Number(ocontactlist.length)>0){//以下带出其他经办人
		   for(var j=0;j<ocontactlist.length;j++){
			   
			   var contact = ocontactlist[j].contact==null?'':ocontactlist[j].contact;
			   var contidtpnm = ocontactlist[j].contidtpnm==null?'':ocontactlist[j].contidtpnm;
			   var contidno = ocontactlist[j].contidno==null?'':ocontactlist[j].contidno;
			   var contphone = ocontactlist[j].contphone==null?'':ocontactlist[j].contphone;
			   var contfax = ocontactlist[j].contfax==null?'':ocontactlist[j].contfax;
			   
			   var htmlStr = "";
			   htmlStr+='<tr><td>经办人名称：</td><td class="white-bg" colspan="3"><span class="col-sm-12"><span>'+contact+'</span></span></td></tr>';
			   htmlStr+='<tr><td>经办人证件类型：</td><td class="white-bg"><span class="col-sm-12"><span>'+contidtpnm+'</span></span></td>';
			   htmlStr+='<td>经办人证件号码：</td><td class="white-bg"><span class="col-sm-12"><span>'+contidno+'</span></span></td></tr>';
			   htmlStr+='<tr><td>经办人电话：</td><td class="white-bg"><span class="col-sm-12"><span>'+contphone+'</span></span></td>';
			   htmlStr+='<td>经办人传真：</td><td class="white-bg"><span class="col-sm-12"><span>'+contfax+'</span></span></td></tr>';
			   $("#continfo").append(htmlStr);
		   }
	   }
   }
   $('.select2_init').select2({
		allowClear : true,
		minimumResultsForSearch : Infinity
	});
}

function queryInfo() // 查询信息
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

function deleteTradeAcco() {
	var qParam = {};
	qParam.tradeacco = $("#tradeacco").val();
	qParam.trustType = $("#trustType").val();
	qParam.permissionId = $("#permissionId").val();
	qParam.operatorId = $("#operatorId").val();
	
	$.ajax({
		url: ACCOUNT_PATH+"deleteFundAccount.do",  
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