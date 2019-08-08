/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var conAddFunc = {
		submitAddForm : function(){
			//提交信息
			//校验表单
			if($("#contactsAddForm").valid()){
				//提交表单
				SubmitAndPreventSecond("contactsAddForm",true);
			}
		}
};

$(document).ready(function(){
	var hiConCompany = $("input[name=hiConCompany]:eq(0)").val();
	var hiConCompanyName = $("input[name=hiConCompanyName]:eq(0)").val();
	
	WASP_WIDGET.initializeSelectVal("conCompany",hiConCompany,hiConCompanyName);
	WASP_WIDGET.triggerInstitutionSelect("conCompany",true);
	
	triggerValidOnSelectChange("conCompany");
	
	var method = $("input[name=method]:eq(0)").val();
	if("update" == method){
		$("input[name=conName]:eq(0)").attr("readonly","readonly").attr("disabled","disabled");
		//显示修改时间:leo
		/*$("#dateInfo").show();*/
	}
	
	$.validator.addMethod("validTelePhone",function(value,element,params){  
		var reg = /^(\d{3,4}(-)?)?\d{7,8}$/;
		if(reg.test(value) || !value){
			return true;
		}else{
			return false;
		}
	},"固定电话格式不正确");
	
	validateAddFormInput();
});

function validateAddFormInput(){
	$("#contactsAddForm").validate({
		rules:{
			conName : {
				required:true,
				maxlength4Byte : 20/*,
				remote: {
				    url: PRIMARY_PATH + "/matchContactsIsNotExist.do" ,     //后台处理程序
				    type: "post",               //数据发送方式
				    dataType: "json",           //接受数据格式   
				    async : false,
				    data: {                     //要传递的数据
				    	conName: function() {
				            return $("input[name=conName]:eq(0)").val();
				        }
				    }
				}*/
			},
			conCompany : {
				required:true
			},
			conDept : {
				maxlength4Byte : 100
			},
			conPost : {
				maxlength4Byte : 100
			},
			conPhone : {
				isNum : true,
				maxlength: 20
				/*,minlength: 11*/
			},
			conTel : {
				validTelePhone :true,
				//maxlength4Byte : 20,
				required : function(){
					var conPhone = $("input[name=conPhone]:eq(0)").val();
					if(!conPhone){
						return true;
					}
					return false;
				}
			},
			conWeChat : {
				maxlength4Byte : 50
			},
			conEmail : {
				maxlength4Byte : 50,
				email:true
			}
		},
		messages :{
			conName :{
				remote : "该联系人已存在系统"
			},
			conTel :{
				required :"手机号码或者固定电话至少填一项"
			}
		}
		
	});
}