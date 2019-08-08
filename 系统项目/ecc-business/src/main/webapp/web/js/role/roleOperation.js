/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var roleAddFunc = {
		submitAddForm : function(){
			//提交信息
			var method = $("input[name=method]:eq(0)").val();
			
			var actionUrl = PRIMARY_PATH + "/roleInfoSave.do";
			
			if("add" != method){
				actionUrl = PRIMARY_PATH + "/roleInfoUpdate.do"
			}
			
			//校验表单
			if($("#roleAddForm").valid()){
				//提交表单
				SubmitAndPreventSecond("roleAddForm",function(){
					$("#roleAddForm").attr("action",actionUrl);
				});
			}
		},
		showShareInfo : function(){
			var roleID = $("#roleId").val()||"";//当前记录ID，需要排除
			var actionUrl = PRIMARY_PATH+"/queryRole.do";
			var container = $("#shareInfoContainer");//页面容器
			
			$.ajax({
	             url: actionUrl,
	             cache: false,
	             error : function(textStatus, errorThrown) {  
	                 ctools.alert("系统ajax交互错误: " + textStatus);  
	             }, 
	             async:false,
	             success : function (data)
	             {
	            	 container.html('');
	             	 if(data){
	             		for(var i=0;i<data.length;i++){
	            			 var shareItem = data[i];
	            			 
	            			 if(roleID == shareItem['roleId']){continue;}//分享待选中排除自己
	            			 
	            			 var uiItem = $('<div>');
	            			 var uiChkbox = $('<input type="checkbox" name="shareRoleIds"/>');
	            			 
	            			 if(window.shareInfoIni && shareInfoIni[shareItem['roleId']] != null){
	            				 uiChkbox.attr('checked','checked');
	            			 }
	            			 uiChkbox.attr('id','shareRoleIds_'+shareItem['roleId']);
	            			 uiChkbox.val(shareItem['roleId']+'');//设置checkbox的值
	            			 uiItem.append(uiChkbox);
	            			 uiItem.append(' '+shareItem['roleName']);
	            			 container.append(uiItem);
	            			 //绑定ICheck样式
	            			 WASP_WIDGET.triggerICheck();
	            		 }
	             	 }else{
	             		container.html("没有数据！");
	            	 }
	             }
	         });
		}
};

$(document).ready(function(){
	roleAddFunc.showShareInfo();
	var hiEmpIds = $("input[name=hiEmpIds]").val();
	var hiEmpsName = $("input[name=hiEmpsName]").val();
	var hiIsSendMail = $("input[name=hiIsSendMail]").val();
	WASP_WIDGET.initializeSelectVal("empIds",hiEmpIds,hiEmpsName);
    WASP_WIDGET.triggerEmployeeSelect("empIds",true);
    //select2改变值的时候要进行及时表单校验
    triggerValidOnSelectChange("empIds");
	var method = $("input[name=method]:eq(0)").val();
	validateAddFormInput();
	/*if("update" == method){
		$("input[name=roleName]").attr("disabled","disabled").attr("readonly","readonly");
	}else */
	
	if(hiIsSendMail == "Y"){
		$("#isSendMail").attr('checked','checked');
		//绑定ICheck样式
		WASP_WIDGET.triggerICheck();
	}
	
	if("detail" == method){
		$("input[name=roleName]").attr("disabled","disabled").attr("readonly","readonly");
		
		$('#empIds').select2("destroy");
		$('#empIds').removeClass('select2');
		$("#empIds").attr("disabled","disabled").attr("readonly","readonly");
		WASP_WIDGET.triggerEmployeeSelect("empIds",true,"");
		
		$("input[name=shareRoleIds]").attr("disabled","disabled").attr("readonly","readonly");
		$("#isSendMail").attr("disabled","disabled").attr("readonly","readonly");
		$("tr[class=modal-buttDiv]").remove();
	}
});


function validateAddFormInput(){
	$("#roleAddForm").validate({
		rules:{
			roleName : {
				required:true,
				maxlength4Byte : 100,
				remote: {
				    url: PRIMARY_PATH + "/matchRoleInfoIsNotExist.do" ,     //后台处理程序
				    type: "post",               //数据发送方式
				    dataType: "json",           //接受数据格式   
				    async : false,
				    data: {                     //要传递的数据
				    	roleName: function() {
				            return $("input[name=roleName]:eq(0)").val();
				        },
						roleId :function(){
							return $("input[name=roleId]:eq(0)").val();
						}
				    }
				}
			},
			empIds : {
				required:true
			}
		},
		messages :{
			roleName :{
				remote : "该角色已存在系统"
			}
		}
		
	});
}
