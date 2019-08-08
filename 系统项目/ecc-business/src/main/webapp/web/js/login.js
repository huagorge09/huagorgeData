var i = 0;

$(function(){
	$("#loginName").focus();
	window.docObj = {};
	window.login = {
		init : function(){
			docObj.checkLoginNameAndPwd();
		},
		/**
		 * 验证密码
		 */
		checkLoginNameAndPwd:function(){
			var btn = $("#submit_btn");
			btn.click(function(){
				$(".message_text").text("");
				getUserInfoByIdAndPwd();
			});
		}
	};
	docObj = $.extend({},login);
	docObj.init();

	$(document).keydown(function(event) {
		if (event.keyCode == 13) {
			$("#submit_btn").click();
		}
	}); 
});


function getUserInfoByIdAndPwd(){
	var loginName = $("#loginName").val();
	var	loginPwd = $("#loginPwd").val();
	var	verifyCode = $("#verifyCode").val();
	if(loginName == ''){
		$(".message_text").text("请输入用户名！");
		$("#loginName").focus();
		return;
	}
	if(loginPwd == ''){
		$(".message_text").text("请输入密码！");
		$("#loginPwd").focus();
		return;
	}
	if(verifyCode == ''){
		$(".message_text").text("请输入验证码！");
		$("#verifyCode").focus();
		return;
	}
	if(loginName != '' && loginPwd != '' && verifyCode != ''){
		$.ajax({
			url : loginPath + "/loginManager.action",
			type : "post",
	        dataType : "json",
	        data:{
				'loginName'	: loginName,
				'loginPwd'	: loginPwd,
				'verifyCode': verifyCode,
				},
			cache: false,
			async: true,
			success : function(data) {
	        	if(data.resultCode == '0000'){
	        		$(".message_text").text(data.resultMsg);
	        		$(".message_text").css("color",'green');
	        		window.location.href=indexPath+"/indexView.xhtml";
	        	}else{
	        		$(".message_text").text(data.resultMsg);
	        		getRandomCode();
	        	}
	        },
	        error : function(err) {
	        	$(".message_text").text("登录异常："+err);
	        	getRandomCode();
	        }
		});
	}
}

function getRandomCode() {
	$("#verifyCode").val("");
	$("#rondomCodeImg").attr("src", projectPath+"service/verifyCode/verifyCode.action?count=" + i);
	i++;
}