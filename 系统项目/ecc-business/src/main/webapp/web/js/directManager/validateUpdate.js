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
	initDefaultValue();
	initWidget();
});

function doSubmit(){
	var idvalidate = $("#showIdvalidate").val();
	if(!idvalidate){
		ctools.alert("请输入有效期","请完善信息","warning");
		return ;
	}
	$("input[name=idvalidate]").val(idvalidate);
//	ctools.confirm("是否确认提交该笔申请？",function(isConfirm){
		SubmitAndPreventSecond("validateUpdateForm",true);
//	});
}


function initWidget(){
	WASP_WIDGET.triggerDateStyleWithYMD("showIdvalidate");
	WASP_WIDGET.triggerICheck("#defaultEndDat",defaultEndDatChecked,defaultEndDatUnChecked,true);
}

function initDefaultValue(){
	var hidIdvalidate = $("input[name=hidIdvalidate]").val();
	if(!!hidIdvalidate){
		$("input[name=showIdvalidate]").val(hidIdvalidate);
	}
}
function defaultEndDatChecked(){
	$("input[name=showIdvalidate]").val("2099-12-31");
	$("input[name=showIdvalidate]").attr("disabled",true);
}

function defaultEndDatUnChecked(){
	$("input[name=showIdvalidate]").val("");
	$("input[name=showIdvalidate]").attr("disabled",false);
}