var pageSource = '',eventId = '';
$(function(){
	cmwaOnkeyup("userName;mobilePhone",checkAppoInfoParam);
	elemBind();
	//模板消息和图文消息
    eventId=getUrlSearchParams("eventId")?getUrlSearchParams("eventId"):getCookie("eventId")
    pageSource=getUrlSearchParams("pageSource")?getUrlSearchParams("pageSource"):getCookie("pageSource")
 	if(eventId && pageSource){ 
 		if(location.href.indexOf("index")<0){
 			operatingRecord(pageSource,eventId,pageId,"","","","")
 		}
	}
    localStorage.setItem("type","cfb");
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
			url:apiHost + "/api/insertAppointInfo",
			type : "post",
			dataType : 'json',
			data : {
				"userName":userName,
				"mobilePhone":mobilePhone,
				"productType":"cfb",
				"cmfUserId":localStorage.getItem("cmfUserId")
			},
			success : function(data) {
				if(data.success){
					$(".reserves").addClass("hide")
					$(".success").removeClass("hide")
					$("input[name=userName]").val('');
					$("input[name=mobilePhone]").val('');
					$("#saveBtn").addClass("disabled").attr("onclick","");
				    operatingRecord(pageSource,"event_wxCfb91No10_consultId",pageId,"","","","")
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
	var params="fundId=110113&period=1"
	$(".once").click(function(){
		operatingRecord(pageSource,"event_wxCfb91No10_proDetailBtnH5Id","","","","","")
		redirectUrl("/WeixinService/business/query/fundInfo.shtml?"+params);
	});
		
	$('ul.item li').click(function(){
		operatingRecord(pageSource,"event_wxCfb91No10_proDetailAreaH5Id","","","","","")
		redirectUrl("/WeixinService/business/query/fundInfo.shtml?"+params);
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

	goToFund()
	//弹窗关闭
	$(".close").click(function(){
		$(".cover-bg").addClass("hide");
		$("input[name=userName]").val('');
		$("input[name=mobilePhone]").val('');
        $(".reserves li p").hide();
        $("body,html").css({"overflow":"auto","height":"auto"}); 
        $("#saveBtn").addClass("disabled").attr("onclick","");
	})

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
/**
 * 预约
 */
function bussiness(){	
     operatingRecord(pageSource,"event_wxCfb91No10_interestedId",pageId,"","","","")
	 $(".reserves").removeClass("hide");
	 $("body,html").css({"overflow":"auto","height":"100%"}); 
}
/**
 *我的顾问
 */
function myAdviser(){
	location.href="/WeixinService/H5modules/H5Content/cfb/bussinessCard.html?pageSource="+pageSource+"&eventId=event_wxCfb91No10_myAdviserId"
}
function seeProduct(){
	location.href="/WeixinService/H5modules/H5Content/cfb/index.html?pageSource="+pageSource+"&eventId=event_wxCfb91No10_h5Id"
}
 document.getElementById("myBtn1").addEventListener("touchstart", function() {
 	$("#myBtn1").addClass("active")
 })
 document.getElementById("myBtn1").addEventListener("touchend", function() {
 	$("#myBtn1").removeClass("active")
 })
 document.getElementById("myBtn2").addEventListener("touchstart", function() {
 	$("#myBtn2").addClass("active")
 })
 document.getElementById("myBtn2").addEventListener("touchend", function() {
 	$("#myBtn2").removeClass("active")
 })