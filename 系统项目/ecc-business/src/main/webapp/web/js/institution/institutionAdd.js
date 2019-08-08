/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var instAddFunc = {
		submitAddForm : function(){
			//提交信息
			//校验表单
			var flag = $("#instAddForm").valid();
			if(flag){
				//提交表单
				SubmitAndPreventSecond("instAddForm",true);
			}
		}
};

$(document).ready(function(){
	WASP_WIDGET.initializeSelectVal("instId");
	WASP_WIDGET.triggerInstitutionSelect("instId",false);
	triggerValidOnSelectChange("instId");
	var method = $("input[name=method]:eq(0)").val();
	if("update" == method){
		$("#instId").attr("readonly","readonly").attr("disabled","disabled");
	}
	validateAddFormInput();
});

function validateAddFormInput(){
    
	$("#instAddForm").validate({
		rules:{
			instId : {
				required:true,
				remote: {
				    url: PRIMARY_PATH + "/matchInstIsNotExist.do" ,     //后台处理程序
				    type: "post",               //数据发送方式
				    dataType: "json",           //接受数据格式   
				    async : false,
				    data: {                     //要传递的数据
				    	instId: function() {
				            return $("select[name=instId]:eq(0)").val();
				        }
				    }
				}
			},
			instAddress : {
				maxlength4Byte : 100
			},
			businessLicenceCode : {
				maxlength4Byte : 50
			}
		},
		messages :{
			instId :{
				remote : "该机构已存在系统"
			}
		}
		
	});
};
