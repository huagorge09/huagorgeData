/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

var todoAddFuns = {
		saveVisitHistoryInfo : function(){
			//提交信息
			//校验表单
			if($("#todoAddForm").valid()){
				//提交表单
				SubmitAndPreventSecond("todoAddForm",true);
			}else{
				ctools.alert("请完善信息","","warning");
			}
		}
};

$(document).ready(function(){
	
//	WASP_WIDGET.initializeSelectVal("todoReceiveId");
//	WASP_WIDGET.triggerContactsSelect("todoReceiveId",false,"接收人员");
	
	var hiTodoReceiveId = $("input[name=hiTodoReceiveId]:eq(0)").val();
	var hiTodoReceiveName = $("input[name=hiTodoReceiveName]:eq(0)").val();
	
	$('#todoReceiveId').select2();
	WASP_WIDGET.initializeSelectVal("todoReceiveId",hiTodoReceiveId,hiTodoReceiveName);
    WASP_WIDGET.triggerEmployeeSelect("todoReceiveId",false,"接收人员");
    
	WASP_WIDGET.triggerDateStyleWithYMD("todoEndDate");
	
	//初始化select2控件  
    $('#isComplete').select2({ placeholder: '是否完成'});
    triggerValidOnSelectChange("todoEndDate","isComplete","todoReceiveId");
    
	var method =$("input[name=method]:eq(0)").val();
	
	if("detail" == method){ //disabled="disabled" readonly="readonly"
		$("#todoReceiveId").attr("disabled","disabled");
		$("#todoEndDate").attr("disabled","disabled");
		$("#isComplete").attr("disabled","disabled");
		$("textarea[name=todoContent]:eq(0)").attr("disabled","disabled");
		$("#saveBtn").remove();
		$("#cancelBtn").remove();
//		$("#cancelBtn").attr("class","btn btn-primary btn-save btn-loading btn-w-xs").val("关闭") ;
	}
	validateAddFormInput();
});

function validateAddFormInput(){
	$("#todoAddForm").validate({
		rules : {
			todoReceiveId : {
				required : true
			},
			todoEndDate : {
				required : true
			},
			isComplete : {
				required : true
			}
		}
	});
}