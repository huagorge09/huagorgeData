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
	var workDate = $("#workDate").val();
	var batId = $("#batId").val();
	
	if(!billdate){
		swal("请输入日期!", "", "warning");
		return;
	}
	var param  ="?workDate="+workDate+"&batId="+batId;
	openDialog(PRIMARY_PATH+"/expVoucherData.do"+param);
}

function initWidget(){
	WASP_WIDGET.triggerDateStyleWithYMD("workDate");
	$("#batId").select2();
}