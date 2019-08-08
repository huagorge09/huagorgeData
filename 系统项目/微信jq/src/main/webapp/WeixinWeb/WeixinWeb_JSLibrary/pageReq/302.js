$(document).ready(function(e){
	var url = getUrlParameter('state');
	var code = getUrlParameter('code');
	
	$("#code").val(code);
	$("#frmLogin").attr("action",url);
	
	frmLogin.submit();
});

function getUrlParameter(name){
	var myUrl = location.href;
	var par = myUrl.split("?")[1];
	var returnVal = "";
	if( typeof(par) != 'undefined' && par != null && par != "" ){
		var tempList = par.split("&");
		for (var i = 0; i < tempList.length; i++) {
			if ((tempList[i].split("=")[0]) == name) {
				returnVal = tempList[i].split("=")[1];
			}
		}
	}
	if (returnVal == undefined || returnVal == null || returnVal == "") {
		returnVal = "";
	}
	return returnVal;
}