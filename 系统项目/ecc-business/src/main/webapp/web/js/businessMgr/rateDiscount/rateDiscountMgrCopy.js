/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

$(function(){
	initWidget();
});

function doSubmit(){
	var formFlag = false;
	var souProductId = $("#souProductId").val();
	var tarProductId = $("#tarProductId").val();
	
	if(souProductId == tarProductId){
		ctools.alert("源商户产品ID(基金代码)不能与目的商户产品ID(基金代码)一致","","error");
	}else{
		formFlag = true;
	}
	//校验表单
	if($("#rateDiscountCopyForm").valid() && formFlag){
		//提交表单
		SubmitAndPreventSecond("rateDiscountCopyForm",true);
	}
}

function initWidget(){
	WASP_WIDGET.triggerFundInfoListSelect("souProductId",false,"源商户产品ID(基金代码)");
	WASP_WIDGET.triggerFundInfoListSelect("tarProductId",false,"目的商户产品ID(基金代码)");
	
	triggerValidOnSelectChange("souProductId","tarProductId");
}

function initValidate(){
	 $("#rateDiscountCopyForm").validate({
		 onFocusOut: true,
		 //focusInvalid : true,
		 rules:{
			 souProductId :{
				 required :true
			 },
			 tarProductId:{
				 required :true
			 }
		 },
		 messages:{
			 souProductId :{
				 required :"请输入源商户产品ID(基金代码)"
			 },
			 tarProductId:{
				 required :"请输入目的商户产品ID(基金代码)"
			 }
		 }
	 });
}