/**
 * 
 */

var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

 function doSubmit(checkStatus){
	 var currEmpId = $("input[name=currEmpId]").val();
	 var createId  = $("input[name=createId]").val();
	 var serialNo = $("input[name=serialNo]").val();
	 if(currEmpId == createId){
		 ctools.alert("创建人与复核人不能是同一人","","error");
		 return ;
	 }
	 if(!serialNo){
		 ctools.alert("关键参数为空，请刷新后重试！","","error");
		 return ;
	 }
	 
	 $("input[name=checkStatus]").val(checkStatus);
	 SubmitAndPreventSecond("instFeeSetCheckForm",true);
 }