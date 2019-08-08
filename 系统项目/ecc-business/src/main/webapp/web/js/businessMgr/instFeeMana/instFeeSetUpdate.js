/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

function doSubmit(){
	//校验表单
	if($("#instFeeUpdateForm").valid()){
		//提交表单
		SubmitAndPreventSecond("instFeeUpdateForm",true);
	}
}

$(function(){
	initWidget();
	initValidate();
});

function initWidget(){
	var instType = $("input[name=instType]").val();
	var instTypeName = $("input[name=instTypeName]").val();
	var fundId = $("input[name=fundId]").val();
	var fundName = $("input[name=fundName]").val();
	var apkind = $("input[name=apkind]").val();
	var apkindName = $("input[name=apkindName]").val();
	var hidStatus = $("input[name=hidStatus]").val();
	var hidStatusName = $("input[name=hidStatusName]").val();

	WASP_WIDGET.initializeSelectVal("showInstType",instType,instTypeName);
	WASP_WIDGET.initializeSelectVal("showFundId",fundId,fundName);
	WASP_WIDGET.initializeSelectVal("showApkind",apkind,apkindName);
	WASP_WIDGET.initializeSelectVal("status",hidStatus,hidStatusName);
	
	WASP_WIDGET.triggerParamListSelect("status",false,"状态");
	triggerValidOnSelectChange("status");
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
}

function initValidate(){
	 $("#instFeeUpdateForm").validate({
		 onFocusOut: true,
		 //focusInvalid : true,
		 rules:{
			 instType : {
				 required :true
			 },
			 fundId :{
				 required :true
			 },
			 apkind:{
				 required :true
			 },
			 discount : {
				 required :true,
				 number : true,
				 max : 1
			 },
			 strAmt :{
				 required :true,
				 number : true,
				 strAmtLessEnd : true
			 },
			 endAmt :{
				 required :true,
				 number : true,
				 endAmtGreatStr:true
			 },
			 status : {
				 required :true
			 }
		 },
		 messages:{
			 instType : {
				 required :"请输入机构类型"
			 },
			 fundId :{
				 required :"请输入基金名称"
			 },
			 apkind:{
				 required :"请输入业务类型"
			 },
			 discount : {
				 required :"请输入费率折扣",
				 number:"请输入数字格式",
				 max : "费率折扣必须小于1"
			 },
			 strAmt :{
				 required :"请输入起始金额",
				 number:"请输入数字格式"
			 },
			 endAmt :{
				 required :"请输入结束金额",
				 number:"请输入数字格式"
			 },
			 status : {
				 required :"请输入状态"
			 }
		 }
	 });
}

jQuery.validator.addMethod("strAmtLessEnd", function(value, element) {
	var flag = false;
	var endElement = $("#endAmt").val();
	if(parseInt(value) <= parseInt(endElement)){
		flag = true;
	}
   return this.optional(element) || flag;
}, "起始金额必须小于等于结束金额");

jQuery.validator.addMethod("endAmtGreatStr", function(value, element) {
	var flag = false;
	var strElement = $("#strAmt").val();
	if(parseInt(value) >= parseInt(strElement)){
		flag = true;
	}
   return this.optional(element) || flag;
}, "结束金额必须大于等于起始金额");  