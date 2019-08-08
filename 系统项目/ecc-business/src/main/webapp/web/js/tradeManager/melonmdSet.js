/**
 * 
 */
var PRIMARY_PATH = "";
var BASE_PATH = "";

function setPath(path,basePath){
	PRIMARY_PATH = path;
	BASE_PATH = basePath;
}

/**
 * 表单验证
 */
function addFromValidate(){
	// 在键盘按下并释放及提交后验证提交表单
	$("#melonmdSet").validate({
	    rules: {
	    	trustType: {
    	        checkTrustType: true
    	    },
    	    melonmd: {
    	    	melonmdCheck: true
     	    },
     	    melonmdpercent: {
		    	required: true,
		    	checkMelonmd:true
		    },
		    checkno: {
     	    	checkNo: true
     	    },
     	    checkpwd: {
     	    	checkPwd: true
     	    }
	    },
	    messages: {
	    	trustType: {
	    		checkTrustType: "请选择委托方式！"
	        },
	        melonmd: {
	        	melonmdCheck: "请选择分红方式！"
	        },
	        melonmdpercent: {
	        	required:  "分红比例不能为空！",
	        	checkMelonmd: "分红比例请输入数字！"
	        },
	        checkno: {
	        	checkNo: "主管工号：必须填写！"
	        },
	        checkpwd: {
	        	checkPwd: "主管密码：必须填写！"
	        }
	    }
	});
};


/**
 *  验证委托类型
 */
jQuery.validator.methods["checkTrustType"]=(function(value,element){
	var success=false;
	var trustType = $("#trustType").val();
	if(trustType == "--" || trustType == ""){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证分红方式
 */
jQuery.validator.methods["melonmdCheck"]=(function(value,element){
	var success=false;
	var melonmd = $("#melonmd").val();
	if(melonmd == "" || melonmd == "--"){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证主管工号是否为空
 */
jQuery.validator.methods["checkNo"]=(function(value,element){
	var success=false;
	var checkno = $("#checkno").val();
	if(isAudit == 'Y' && checkno == ""){
		success = false
	}else{
		success = true
	}
	return success;
});

/**
 *  验证主管密码不能为空
 */
jQuery.validator.methods["checkPwd"]=(function(value,element){
	var success=false;
	var checkpwd = $("#checkpwd").val();
	if(isAudit == 'Y' && checkpwd == ""){
		success = false
	}else{
		success = true
	}
	return success;
});
