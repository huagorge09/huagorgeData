
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function() {
	initValidateForm();
})



function initValidateForm() {
	$("#parameterForm").validate({
		focusCleanup : true,
		rules : {
			'pmco' : {
				required : true
			},
			'pmnm' : {
				required : true
			}
		},
		messages : {
			'pmco' :{
				required:'参数值不能为空'
			},
			'pmnm':'参数名不能为空！'
		}
	});
}

function doSubmit(method){
	
	var form = $('#parameterForm');
	if(false === form.valid()){
		toastr.warning("请完善参数信息");
		return;
	}
	var data = $('#parameterForm').serializeArray();
	if (data == null || !data) {
				return;
	}
	var actionUrl= PRIMARY_PATH + "/add.xhtml";
	$.ajax({
		type: 'POST',
		url: actionUrl,
		dataType:'json',
		data:data,
		success: function(data){
			if(data.ResultCode=='0000'){
				window.location.href= BASE_PATH + "service/jsp/hint/success.jsp";
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