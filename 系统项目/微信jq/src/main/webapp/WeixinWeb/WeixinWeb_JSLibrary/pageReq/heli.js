//和利界面js
var pageSource = 'wx_HL02Id',pageId = 'wx_HL02Id',eventId = 'event_wx_HL02Id';
$(function(){
	cmwaOnkeyup("userName;mobilePhone",checkAppoInfoParam);
	elemBind();
	//模板消息和图文消息
	var urlParams = getUrlParams();
	pageSource = urlParams['pageSource']?urlParams['pageSource']:pageSource;
	if(pageSource){
		//进入和利页面记录
		recordOperation({eventId:eventId,pageId:pageId,pageSource:pageSource});
	}
	
});


/* 验证输入框的有效性  */
function checkAppoInfoParam(){
	var userName = $("#userName").val();
	var mobilePhone = $("#mobilePhone").val();
	if(userName == null || userName == ""){
		$("#saveBtn").addClass("disabled").attr("onclick","");
		return;
	}else if(mobilePhone == null || mobilePhone == ""){
		$("#saveBtn").addClass("disabled").attr("onclick","");
		return;
	}else{
		$("#saveBtn").removeClass("disabled").attr("onclick","saveAppointInfo()");
	}
}
/*
 * 用户信息提交
 */
function userDateSumbit(userName,mobilePhone){
	$.ajax({
			async : false,
			url:'/WeixinService/business/insertAppointInfo.xhtml',
			type : "post",
			dataType : 'json',
			data : {
				"userName":userName,
				"mobilePhone":mobilePhone,
				"productType":"HL02"
			},
			success : function(data) {
				if(data && data.success){
					$(".reserves").addClass("hide")
					$(".success").removeClass("hide")
					$("input[name=userName]").val('');
					$("input[name=mobilePhone]").val('');
					$("#saveBtn").addClass("disabled").attr("onclick","");
				}else{
					errorRemark(data.msg);
				}
			},
			error : function() {
				errorRemark("网络繁忙，请稍后再试。");
			},
		});
}
/**
 * 元素事件绑定
 */
function elemBind(){
	//立即咨询
	$(".once").click(function(){
		recordOperation({eventId:'event_wx_advisoryId',pageId:pageId,pageSource:pageSource});
		$(".reserves").removeClass("hide");
                var h=$(window).height(); 
                $("body,html").css({"overflow":"hidden","height":h+"px"});
	});
    //鼠标键盘弹起
	$("input").keyup(function(){
		if($("input[name=userName]").val()!==""&&$("input[name=mobilePhone]").val()!==""){
			$("button").removeClass("disabled");
		}
	});
     //输入框光标事件
	$("input").focus(function(){
		$(this).parents("div").addClass("foucs");
		$(this).parents("li").find("p").hide().text("")
	});
	$("input").blur(function(){
		$(this).parents("div").removeClass("foucs");
	});
	
	
	//弹窗关闭
	$(".close").click(function(){
		$(".cover-bg").addClass("hide");
		$("input[name=userName]").val('');
		$("input[name=mobilePhone]").val('');
        $(".reserves li p").hide();
        $("body,html").css({"overflow":"auto","height":"auto"}); 
        $("#saveBtn").addClass("disabled").attr("onclick","");
	})
	
	$('ul.item li').click(function(){
		recordOperation({eventId:$(this).attr('eventId'),pageId:pageId,pageSource:pageSource});
		redirectUrl("/WeixinService/business/query/fundInfo.shtml?"+$(this).attr('params'));
	});
}

//点击信息提交
function saveAppointInfo(){
		var userName=$.trim($("input[name=userName]").val());
		var mobilePhone=$.trim($("input[name=mobilePhone]").val());
		var flag=true;
		if(userName==""||!ischinese(userName)){
			$("input[name=userName]").parents("li").find("p").show().text("请输入2-10字中文姓名");
			flag=false;
		}	
		if (!isMobile(mobilePhone)) {
			$("input[name=mobilePhone]").parents("li").find("p").show().text("请输入正确的手机号码");
			flag=false;
		}
		
		if(flag){
			userDateSumbit(userName,mobilePhone);
		}
}


function ischinese(name){
    return /^[\u2E80-\u9FFF]{2,10}$/.test(name)
}  

 //验证手机号
 function  isMobile(phone) {
        return /^0?(13[0-9]|15[012356789]|18[012356789]|14[57]|17[03678])[0-9]{8}$/.test(phone);
}