
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

function doSubmit(checkst)
{
	var permissionId = $("#permissionId").val();
	var operatorId = $("#operatorId").val();
	var custno = $("#custno").val();
	ctools.confirm({title : "是否确认提交该笔申请？",text:''},
	function(isConfirm){
		if(isConfirm){
			$.ajax({
				url: PRIMARY_PATH + "/checkBakCustomer.xhtml",  
				dataType: "json",
				type: "POST",
				data:  {
					"sp[permissionId]" : permissionId,
					"sp[custno]" : custno,
					"sp[checkflag]" : checkst,
					"sp[operatorId]" : operatorId
				},
				cache: false,
				async: false,
				success: function(data) {
					var errcode = data.errcode;
				    var errMsg =data.errmsg;
				    if(errcode == "0000"){
				    	ctools.alert_sweet('复核操作成功！', "success", "" , function(){
				    		window.opener.queryByCondtion(true);
				    		window.close();
						});
				    }else{
				    	ctools.alert_sweet('复核操作失败！', "error", "失败原因："+errMsg);
				    }
				}
			});
		}
	});
}