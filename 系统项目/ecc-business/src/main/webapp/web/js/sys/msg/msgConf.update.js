$(function() {
	// 初始化表单验证 
	initValidateAndSubmit();
	
	// 初始化
	WASP_WIDGET.initializeSelectVal('toList');
	WASP_WIDGET.initializeSelectVal('ccList');
	WASP_WIDGET.initializeSelectVal('sccList');

	// 绑定select2 项目经理 控件
	WASP_WIDGET.triggerSelectOnManagerIdNonStyle("toList");
	WASP_WIDGET.triggerSelectOnManagerIdNonStyle("ccList");
	WASP_WIDGET.triggerSelectOnManagerIdNonStyle("sccList");
	$('#subType').select2({placeholder: '子类型'});
	//绑定ICheck样式
	WASP_WIDGET.triggerICheck();
	//对select控件绑定验证事件
//	triggerValidOnSelectChange('toList');
});

var PATH_PREFIX = "";
function setPathPrefix(path) {
	PATH_PREFIX = path;
}

function triggerSelectOnName(publicEmailId) {
	$('#' + publicEmailId).select2({
		multiple : true,
		ajax : {
			url : url = PATH_PREFIX + "msgpublicemail/selectPublicEmail.do",
			type : "POST",
			data : function(params) {
				var query;
				if (params.term == null || params.term == '' || params.term == 'undefind') {
					query = {
							emailName : ""
					};
				} else {
					query = {
							emailName : params.term
					};
				}
				return query;
			},
			dataType : "json",
			processResults : function(data) {
				var results = $.map(data, function(obj) {
					return {
						id : obj.id,
						text : obj.emailName
					}
				});
				return {
					results : results
				};
			}
		},
		cache : true,
		delay : 3000,
	});
}


function selectPack(text, hidden) {
	var url = PATH_PREFIX + "msgpack/msgPackListView.do";
	openDialog(url);
}

//内容模板不为空验证
jQuery.validator.methods["nullModeCheck"]=(function(value,element){
	var flag =0;
	var kmTitleTemplate = $("#kmTitleTemplate").val();
	var kmContentTemplate =$("#kmContentTemplate").val(); 
	var emailTitleTemplate =$("#emailTitleTemplate").val(); 
	var emailContentTemplate =$("#emailContentTemplate").val();
	var smsContentTemplate =$("#smsContentTemplate").val();
	if ((kmTitleTemplate == null || kmTitleTemplate == '') && (kmContentTemplate == null || kmContentTemplate == '') && (emailTitleTemplate == null || emailTitleTemplate == '') && (emailContentTemplate == null || emailContentTemplate == '') && (smsContentTemplate == null || smsContentTemplate == '')) {
		flag=1;
	}
	if(flag==1){
		return false;
	}else{
		return true;
	}
});



function initValidateAndSubmit(){
		
	  $("#updateForm").validate({
	        focusCleanup: true,
	        rules: {
	        	title: {
	        		required: true	  
	        	},
	        	sortNo: {
	        		required: true	  
	        	},
	            triggerDate:{
	            	required: true
	            },
	            sendType:{
	            	required: true
	            },
	            smsContentTemplate:{
	            	nullModeCheck: true
	            }
	        },
	        messages: {
	        	title: {
	        		required: '标题不能为空'	  
	        	},
	        	sortNo: {
	        		required: '排序号不能为空'	  
	        	},
	            triggerDate:{
	            	required: '触发条件不能为空'
	            },
	            sendType:{
	            	required: '发送类型不能为空'
	            },
	            smsContentTemplate:{
	            	nullModeCheck: 'KM、EMAIL、SMS模板不能都为空'
	            }
	        }
	    });
	  $("select[name='msgConfEmpVoR.empIds']").rules("add",{required : true,messages:{required:"收件人用户名称不能为空"}});
	  $("input[name='sendType']").rules("add",{required : true,messages:{required:"请选择发送类型"}});
}


//修改表单提交
function submitCheck() {
	SubmitAndPreventSecond("updateForm",null);
}


$(function() {
 	//页面加载完成之后执行
	$('#ccListAddr').change(function(){
		var testStr=$('#ccListAddr').val();
		if(testStr != "" && null != testStr){
			var mailArr=new Array();
			mailArr=testStr.split(",");
			for (var i = 0; i < mailArr.length; i++) {
				validateMail(mailArr[i]);
			} 
		}else{
			 $('#msg').hide();
		}
		
	}) 
	
});
function validateMail(str) {
	var search_str = /^[\w\-\.]+@[\w\-\.]+(\.\w+)+$/;
	 var email_val =str;
	 if(!search_str.test(email_val)){ 
		 $('#msg').show();
	     $('#ccListAddr').focus();
	     return false;
	 }else{
		 $('#msg').hide();
	 }
}

$(function() {
 	//页面加载完成之后执行
	$('#sccListAddr').change(function(){
		var testStr=$('#sccListAddr').val();
		if(testStr != "" && null != testStr){
			var mailArr=new Array();
			mailArr=testStr.split(",");
			for (var i = 0; i < mailArr.length; i++) {
				validateMail_v(mailArr[i]);
			} 
		}else{
			 $('#smsg').hide();
		}
		
	}) 
	
});

function validateMail_v(str) {
	var search_str = /^[\w\-\.]+@[\w\-\.]+(\.\w+)+$/;
	 var email_val =str;
	 if(!search_str.test(email_val)){ 
		 $('#smsg').show();
	     $('#sccListAddr').focus();
	     return false;
	 }else{
		 $('#smsg').hide();
	 }
}
