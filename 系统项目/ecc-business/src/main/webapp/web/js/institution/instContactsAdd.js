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
			if($("#instContactsAddForm").valid()){
				//提交表单
				SubmitAndPreventSecond("instContactsAddForm",true);
			};
		}
};

$(document).ready(function(){
	WASP_WIDGET.initializeSelectVal("instId");
	WASP_WIDGET.triggerInstitutionSelect("instId",false);
	WASP_WIDGET.initializeSelectVal("conId");
	WASP_WIDGET.triggerContactsByAuthSelect("conId",false);
	
	triggerValidOnSelectChange("instId","conId");
	
	$("#instContactsAddForm").validate({
		rules : {
			instId : {
				required:true
			},
			conId : {
				required:true,
				remote: {
				    url: PRIMARY_PATH + "/matchInstConIsNotExist.do" ,     //后台处理程序
				    type: "post",               //数据发送方式
				    dataType: "json",           //接受数据格式   
				    async : false,
				    data: {                     //要传递的数据
				    	instId: function() {
				            return $("select[name=instId]:eq(0)").val();
				        },
				        conId : function(){
				        	return $("select[name=conId]:eq(0)").val();
				        }
				    }
				}
			}
		},
		messages : {
			conId : {
				remote : "该机构已存在该联系人"
			}
		}
	});
});

