var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}
var dsBankBnkbaseInfoList = $('#dsBankBnkbaseInfoList');
$(function(){
	$('#disFlg').select2({ placeholder: '请选择',allowClear: false});
	$('#status').select2({ placeholder: '请选择',allowClear: false});
	initValidateForm();
})
function initValidateForm() {
	$("#dsBankBnkbaseInfoForm").validate({
		focusCleanup : true,
		rules : {
			'bnkNo' : {
				required : true,
				maxlength: 3,
				isFundID:true
			},
			'bnkNm' : {
				required : true
			},
			'bnkNmAbbr' : {
				required : true
			},
			'disOrder' : {
				isInt : true
			}
		},
		messages : {
			'bnkNo' :{
				required:'银行代码不能为空，且只能为3位字符',
				isFundID:'银行代码只能为字母与数字的组合',
				maxlength: '银行代码最多只能为3位字符'
			},
			'bnkNm':'银行名称不能为空！',
			'bnkNmAbbr':'银行简称不能为空！',
			'disOrder':{
				isInt:'展示顺序只能为正整数'
			}
		}
	});
}

function doSubmit(method){
	
	var form = $('#dsBankBnkbaseInfoForm');
	if(false === form.valid()){
		toastr.warning("请完善基本信息");
		return;
	}
	var data = $('#dsBankBnkbaseInfoForm').serializeArray();
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
				window.location.href=BASE_PATH +  "service/jsp/hint/success.jsp";
				dsBankBnkbaseInfoList.trigger("reloadGrid");
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