$(function() {
	WASP_WIDGET.triggerSelectOnMenu("srcMenuId");
	$('#parentId').select2();
	$('#type').select2();
	$('#systemClassify').select2();
	triggerValidOnSelectChange("parentId","type","systemClassify"); 
	initJqValidate();
	//初始化默认事件
	initDefaultEvent();
	
});



function initDefaultEvent(){
	$('#descGroup').hide();
	$('#codeGroup').hide();
	$("#type").change( function() {
		var $this=$(this);
		if($this.val()=='1'){
			$('#descGroup').show();
			$('#codeGroup').show();
		}else{
			$('#descGroup').hide();
			$('#codeGroup').hide();
		}
		$this.valid && $this.valid();
	});
	$("#parentId").change( function() {
		var $type=$("#type");
		$type.valid && $type.valid();
	});
}

/**
 * 复制菜单
 * @returns {Boolean}
 */
function copyMenu() {
	var srcMenuId = $('#srcMenuId').find("option:selected").val();
	srcMenuId = (srcMenuId == undefined ? "" : srcMenuId);
	if (srcMenuId == "") {
		alert("请选择要复制的菜单");
		return false;
	}
	var $parent = $(this).closest('form');
	$parent.find('input, textarea, select').val(null);

	$.ajax({
		type : "POST",
		dataType : "json",
		url : PATH_PREFIX+'copyMenu.do?menuId=' + srcMenuId,
		success : function(data) {
			if (data.succeed) {
				if (data.result) {
					$("#name").val(data.result.name); 
					$("#url").val(data.result.url); 
				}
			} else {
				alert("复制菜单数据错误：" + data.errmsg);
			}
		},
		error : function(jqXHR, textStatus, errorThrown) {
			alert("复制菜单数据错误" + textStatus);
		}
	});

}



/**
 * 检查菜单名称是否存在
 */
jQuery.validator.methods["checkRepeatMenuNM"]=(function(value,element){
	var menuNM=$("#name").val();
	var success=false;
	$.ajax({
		type : "POST",
		async: false, 
		dataType : "json",
		url : PATH_PREFIX+'checkRepeatMenuNM.do',
		data:{'menuNM':menuNM},
		success : function(data) {
			if (data.result===false) {
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

/**
 *  检查菜单URL是否存在
 */
jQuery.validator.methods["checkRepeatURL"]=(function(value,element){
	var url=$("#url").val();
	var success=false;
	$.ajax({
		type : "POST",
		async: false, 
		dataType : "json",
		url : PATH_PREFIX+'checkRepeatURL.do',
		data:{'url':url},
		success : function(data) {
			if (data.result===false) {
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

/**
 * 当选择类型是按钮菜单类型，则必须挂在最终的叶子节点上
 */
jQuery.validator.methods["checkType"]=(function(value,element){
	var type = $("#type").val();
	var parentId = $('#parentId').find("option:selected").val();
	parentId = (parentId == undefined ? "0" : parentId);
	var success = true;
	if (type == '1') {
		$.ajax({
			type : "POST",
			async : false,
			dataType : "json",
			url : '/cbp/sys/menu/hasSubMenu.do',
			data : {
				'parentId' : parentId
			},
			success : function(data) {
				if (data.result === true) {
					// 没有挂在最终的叶子节点，则不符合
					success = false;
				} else {
					success = true;
				}
			},
			error : function(data) {
				success = false;
			}
		});
	}
	return success;
});


function initJqValidate() {
	$("#menuAddForm").validate({
		focusCleanup : true,
		rules : {
			name : {
				required : true,
				checkRepeatMenuNM: false
			},
			url : {
				required : true,
				checkRepeatURL:true
			},
			sequence : {
				required : true,
				isNumber : true,
				max		 : 999999
			},
			type : {
				checkType : true,
				required : true
			},
			parentId:{
				required : true
			},
			systemClassify:{
				required : true
			}
		},
		messages : {
			name : {
				    required:'请输入菜单名称',
				    checkRepeatMenuNM: '菜单名称已存在，请更换'
			       },
			url : {
					required : '请输入菜单链接',
					checkRepeatURL : '菜单链接已存在，请更换'
				},
			sequence : {
				required : '请输入序列号,必须为数值',
				max 	 : "输入序列号有误，最大6位数字"
			},
			type : {
				checkType : '按钮菜单类型，则父菜单必须是最终的叶子节点',
				required : '请选择菜单类型'
			},
			parentId : {
				required : '请选择父菜单'
			},
			systemClassify : {
				required : '请选择所属系统'
			}
		}
	});
}

function dynamicJqValidate(type) {
	if ('0' == type) {
		$("#desc").rules("remove");
		$("#code").rules("remove");
	} else{
		$("#desc").rules("add", {
			required : true,messages:{required:"请输入按钮描述"}
		});
		$("#code").rules("add", {
			required : true,messages:{required:"请输入按钮代码"}
		});
	
	}
}

function submitMenuAddForm(){
	SubmitAndPreventSecond("menuAddForm",function(){
		var type=$("#type").val();
		dynamicJqValidate(type);
	});
}


