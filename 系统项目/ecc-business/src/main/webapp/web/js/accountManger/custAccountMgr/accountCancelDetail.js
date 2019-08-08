var PRIMARY_PATH = "";
var ACCOUNT_PATH = "";
var PROJEC_TPATH = "";

function setPath(projectPath,path,accountPath){
	PROJEC_TPATH = projectPath;
	PRIMARY_PATH = path;
	ACCOUNT_PATH = accountPath;
}

$(function(){
	$('.select2_width').select2({allowClear: false,minimumResultsForSearch:Infinity});
	$("#btnCancel").mousedown(function() {
		doSubmit();
	});
	$("#audit").mousedown(function() {
		doAudit();
	});
});

function doSubmit()
{
	if("N"==isAudit){
		ctools.alert_sweet('请先授权！', "error", "");
		return false;
	}
	var checkno =  $("#checkno").val();
	var checkpwd =  $("#checkpwd").val();
	$.ajax({
		type : "POST",
		async: false, 
		dataType : "json",
		url : PROJEC_TPATH+'capitalService/server/checkPermission.xhtml',
		data:{
			'checkno':checkno,
			'checkpwd':checkpwd
		},
		success : function(data) {
			if(data.result == "0"){
				ctools.alert_sweet('授权失败：主管工号或密码错误！', "error", "");
			}else{
				var qParam = {}; 
			    qParam.oserialno =  $("#oserialno").val();
			    qParam.tradeacco =  $("#tradeacco").val();    
			    qParam.trustType =  $("#trustType").val();
			    qParam.grantid =  $("#checkno").val();
			    qParam.permissionId = $("#permissionId").val();
			    qParam.operatorId = $("#operatorId").val();
			    $.ajax({
					url: ACCOUNT_PATH+"accountCancel.do",  
					dataType: "json",
					type: "POST",
					data:  {
						"tradeInfo" : JSON.stringify(qParam)
					},
					cache: false,
					async: false,
					success: function(data) {
						var errcode = data.errcode;
					    var errMsg =data.errmsg;
					    var serialNo = data.serialno;
					    if(errcode == "0000"){
					    	ctools.alert_sweet('申请提交成功！', "success", "申请编号："+serialNo , function(){
					    		window.opener.queryByCondtion(true);
					    		window.close();
							});
					    }else{
					    	ctools.alert_sweet('申请提交失败！', "error", "失败原因："+errMsg);
					    }
					}
				});
			}
		}
	});
}

function doBack() {
	window.close();
}
