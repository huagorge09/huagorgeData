$(function(){
	WASP_ROLE.showShareInfo();//分享权限列表的数据加载
	initValidate();//初始化表单校验
	WASP_WIDGET.triggerSelectOnManagerIdNonStyle("empIds");//员工姓名
	//回显员工姓名的值
	WASP_WIDGET.initializeSelectVal('empIds');
	//初始化单选框
	initRadios();
	$('#roleType').select2({
		placeholder : '角色类型'
	});
	//select2改变值的时候要进行及时表单校验
	triggerValidOnSelectChange("roleType","empIds");
});	

/**
 * 本模块业务校验：验证角色名称是否已经存在
 */
jQuery.validator.methods["checkIsExists"]=(function(value,element){
	//验证ID是否重复，pass为false代表重复，校验不通过
	var pass = false;
	var roleName = $("#roleName").val();
	var roleType = $("#roleType").val();
	var roleId = $("#roleId").val();
	if(!roleName)
		return true;
	
	$.ajax({
		  url: PATH_PREFIX+"queryRoleCount.do",
		  dataType: "json",
		  type: "POST",
		  cache: false,
		  async: false,
		  data: {
			  roleName:roleName,roleType:roleType,roleId:roleId
		  },
		  success: function( data ){
				if(data > 0){
					pass = false;
					return;
				}
				pass=true;
		  },
		  error: function(jqXHR, textStatus, errorThrown){
				ctools.alert("查询失败" + textStatus);
				pass = false;
		  }
	});
	return this.optional(element) || pass;
});

function initValidate(){
	 $("#roleUpdateForm").validate({
	        focusCleanup: true,
	        rules: {
	        	roleName:{
	        		required:true,
	        		checkIsExists:true
	        	},
	        	roleType:{
	        		required:true,
	        		checkIsExists:true
	        	},
	        	empIds:{
	        		required:true
	        	}
	        },
	        messages: {
	        	roleName:{
	        		required:"请填写角色名称",
	        		checkIsExists:"该类型存在该角色名称，请重新填写"
	        	},
	        	roleType:{
	        		required:"请选择角色类型",
	        		checkIsExists:"该类型存在该角色名称，请重新选择"
	        	},
	        	empIds:{
	        		required:"请选择员工"
	        	}
	        }
	    });
	
}


function submitUpdateForm(){	
//	$("#roleUpdateForm").submit();
	SubmitAndPreventSecond("roleUpdateForm",function(){
		$("#roleUpdateForm").attr("action",PATH_PREFIX+"updateRole.do");
	});//防止表单二次提交
}