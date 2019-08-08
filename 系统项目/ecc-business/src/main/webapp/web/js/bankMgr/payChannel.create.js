
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function() {
	WASP_WIDGET.triggerSelectOnBankBase('thirdChannel');
	WASP_WIDGET.triggerSelectOnBankBase('bankNO');
	$('#payMode').select2({ placeholder: '请选择'});
	$('#status').select2({ placeholder: '请选择'});
	$('#mipFlag').select2({ placeholder: '请选择'});
	$('#recommend').select2({ placeholder: '请选择'});
	initValidateForm();
})


function initValidateForm() {
	$("#payChannelForm").validate({
		focusCleanup : true,
		rules : {
			'thirdChannel' : {
				required : true,
				maxlength: 3,
				isFundID:true
			},
			'status' : {
				required : true
			},
			'bankNO' : {
				required : true
			}
		},
		messages : {
			'thirdChannel' :{
				required:'第三方支付渠道代码不能为空'
			},
			'status':'状态不能为空！',
			'bankNO':'支持银行不能为空！'
			
		}
	});
}

function doSubmit(method){
	
	var form = $('#payChannelForm');
	if(false === form.valid()){
		toastr.warning("请完善基本信息");
		return;
	}
	var data = $('#payChannelForm').serializeArray();
	if (data == null || !data) {
				return;
	}
	
	var bankName=$("#bankNO").find("option:selected").text()
	
	var attachName={"name":"bankName","value":bankName};
	data.push(attachName);
	var actionUrl= PRIMARY_PATH +"/add.xhtml";
	$.ajax({
		type: 'POST',
		url: actionUrl,
		dataType:'json',
		data:data,
		success: function(data){
			if(data.ResultCode=='0000'){
				window.location.href=BASE_PATH +  "service/jsp/hint/success.jsp";
			}else{
				swal('新增失败',data.ResultDesc,"error")
			}
		},
		error:function(xhr){
			switch(xhr.status){
				case 403:sweetAlert("对不起，您无此权限！","","error");break;
				case 404:sweetAlert("对不起，无此页面！", "","error");break;
				case 500:sweetAlert("内部错误，请联系管理员！","","error");break;  
				case 504:sweetAlert("超时，请联系管理员！", "","error");break;  
				case 417:sweetAlert("内部错误，请联系管理员！", "","error");break;  
			}
		}
	});
}