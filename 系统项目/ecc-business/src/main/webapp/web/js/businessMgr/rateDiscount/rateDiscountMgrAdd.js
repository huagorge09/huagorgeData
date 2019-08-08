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
	
	if(!$("#rateDiscountAddForm").valid()){
		return ;
	}
	var formFlag = false;
	var data = {};
	data.pageNo = 1 ;
	data.sp ={};
	data.sp.bankName = $("#bnkNo").val();
	data.sp.productId = $("#productId").val();
	data.sp.apkind = $("#apkind").val();
	$.ajax({
    	async:false,
		url:PRIMARY_PATH +'/rateDiscListPage.do',
		type:"post",
		dataType:'json',
		data:data,
		error:function(a,b,c){
			ctools.alert("网络繁忙，请稍后再试。","","error");
		},
		success:function(data, textStatus){
			if(!!data && data.itemSize <= 0){
				formFlag = true;
			}else{
				ctools.alert("费率折扣数据重复","","error");
			}
		}
	});
	
	//校验表单
	if($("#rateDiscountAddForm").valid() && formFlag){
		//提交表单
		SubmitAndPreventSecond("rateDiscountAddForm",true);
	}
}

$(function(){
	initWidget();
	initValidate();
});

function initWidget(){
	WASP_WIDGET.initializeSelectVal("apkind","020","认购");
	WASP_WIDGET.initializeSelectVal("status","Y","正常");
	WASP_WIDGET.triggerDateStyleWithYMD("strDate");
	WASP_WIDGET.triggerDateStyleWithYMD("endDate");
	WASP_WIDGET.triggerFundInfoListSelect("productId",false,"商户产品ID(基金代码)");
	WASP_WIDGET.triggerBnkBaseListSelect("bnkNo",false,"银行名称");
	WASP_WIDGET.triggerParamListSelect("apkind",false,"业务类型");
	WASP_WIDGET.triggerParamListSelect("status",false,"状态");
	
	triggerValidOnSelectChange("productId","bnkNo","apkind","status","strDate","endDate");
	
	//注册清空事件
    WASP_WIDGET.registerResetClearEvent();
}
//validator.resetForm();
function initValidate(){
	 $("#rateDiscountAddForm").validate({
		 onFocusOut: true,
		 ignore:":hidden",
		 //focusInvalid : true,
		 rules:{
			 bnkNo : {
				 required :true
			 },
			 productId :{
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
			 strDate :{
				 required :true,
				 dateISO:true,
				 strDateLessEnd : true
			 },
			 endDate :{
				 required :true,
				 dateISO:true,
				 endDateGreatStr : true
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
			 bnkNo : {
				 required :"请输入银行名称"
			 },
			 productId :{
				 required :"请输入商户产品ID(基金代码)"
			 },
			 apkind:{
				 required :"请输入业务类型"
			 },
			 discount : {
				 required :"请输入费率折扣",
				 number:"请输入数字格式",
				 max : "费率折扣必须小于1"
			 },
			 strDate :{
				 required :"请输入起始日期",
				 dateISO :"日期格式不正确"
			 },
			 endDate :{
				 required :"请输入结束日期",
				 dateISO :"日期格式不正确"
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

function dateChange(){
	$("#strDate").valid();
	$("#endDate").valid();
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

jQuery.validator.addMethod("strDateLessEnd", function(value, element) {
	var flag = false;
	var endElement = $("#endDate").val().replace(/-/g,"");
	value = value.replace(/-/g,"");
	if(!!endElement && !!value && value <= endElement){
		flag = true;
	}
    return this.optional(element) || flag;
}, "起始日期必须小于等于结束日期");

jQuery.validator.addMethod("endDateGreatStr", function(value, element) {
	var flag = false;
	var endElement = $("#strDate").val().replace(/-/g,"");
	value = value.replace(/-/g,"");
	if(!!endElement && !!value && value >= endElement){
		flag = true;
	}
    return this.optional(element) || flag;
}, "起始日期必须大于等于结束日期");