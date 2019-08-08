$(function(){
	elemBind();
	userAction();
	queryUserinfo();
	var height = $(window).height();
	$(window).scroll(function() {
		if ($(window).scrollTop() > height-350) {
			$(".footer-yuyue-bar").removeClass("footer-yuyue-tip-active");
			$("body").removeClass("shrink");
			$(".yy-top-button-text").text("收起预约");
		} else {
			$(".footer-yuyue-bar").addClass("footer-yuyue-tip-active");
			$("body").addClass("shrink");
			$(".yy-top-button-text").text("展开预约");
		}
	}); 
	pageId = 'cfbId';
	pageSourceId = getUrlParameter("pageSourceId");
})

/*
 * 用户信息提交
 */
function userDateSumbit(userName,mobilePhone){
	$.ajax({
			async : false,
			url : "/AppService/business/insertAppointInfo.xhtml",
			type : "post",
			dataType : 'json',
			data : {
				"userName":userName,
				"mobilePhone":mobilePhone
			},
			success : function(data) {
				if(data.msg){
					$(".reserves").hide();
					$(".succTips").show();
				}else{
					show_tips("网络繁忙，请稍后再试。");
				}
			},
			error : function() {
				show_tips("网络繁忙，请稍后再试。");
			},
		});
}
/**
 * 元素事件绑定
 */
function elemBind(){
	//立即咨询
	$(".once").click(function(){
		$(".reserves").show();
		var eventId = $(this).attr('data-eventId');
		var pageSourceId=getUrlParameter("pageSourceId");
		var pageId="cfbId";
		operatingRecord(pageSourceId,pageId,eventId,"");
	})
    //鼠标键盘弹起
	$("input").keyup(function(){
		if($("input[name=userName]").val()!==""&&$("input[name=mobilePhone]").val()!==""){
			$("button").removeClass("disabled");
		}
	})
    //输入框光标事件
	$("input").focus(function(){
		$(this).parents("div").addClass("foucs");
		$(this).parents("li").find("p").text("")
	})
	$("input").blur(function(){
		$(this).parents("div").removeClass("foucs");
	})
	//点击信息提交
	$("button").on("click",function(){
		if(!$(this).hasClass("disabled")){
			var userName=$("input[name=userName]").val();
			var mobilePhone=$("input[name=mobilePhone]").val();
			var flag=true;
			if(!ischinese(userName)){
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
		
	})
	
	$(".info").click(function(){
	    // 事件Id
		var eventId = $(this).attr("data-id");
		// 来源页面Id
		var pageSourceId = getUrlParameter("pageSourceId");
		// 记录埋点数据
		var pageId="cfbId"
		operatingRecord(pageSourceId,pageId,eventId,"");
		if(eventId=="event_01cfbId"){
			location.href="https://direct.cmwachina.com/AppService/business/fund/fundDetail.shtml?fundid=17D911&period=1";
		}else if(eventId=="event_02cfbId"){
			location.href="https://direct.cmwachina.com/AppService/business/fund/fundDetail.shtml?fundid=17D814&period=3";
		}
		
	})
	
	
	//弹窗关闭
	$(".close").click(function(){
		$(".cover_bg").hide();
		$("input").val('');
	    $(".reserves li p").html("");
	    $("button").addClass("disabled");
	})
	
	$(".yy-top-button-text").click(function(){
		if($(".footer-yuyue-bar").hasClass("footer-yuyue-tip-active")){
			$(".footer-yuyue-bar").removeClass("footer-yuyue-tip-active");
			$("body").removeClass("shrink");
			$(".yy-top-button-text").text("收起预约");
		}else{
			$(".footer-yuyue-bar").addClass("footer-yuyue-tip-active");
			$("body").addClass("shrink");
			$(".yy-top-button-text").text("展开预约");
		}
	})
	
	$("#yy-footer-submit-btn").on("click",function(){
		 var eventId = "event_advisoryId";
		 var pageSourceId=getUrlParameter("pageSourceId");
		 var pageId="cfbId";
		 operatingRecord(pageSourceId,pageId,eventId,"");
		 
		 var userName=$("input[name=userName2]").val();
	     var mobilePhone=$("input[name=mobilePhone2]").val();
	     var flag=true
	     if(userName==""||!ischinese(userName)){
	     	$(".infoTips").show().find("p").text("请输入2-10字中文姓名");
	     	flag=false;
	     }else if(mobilePhone==""||!isMobile(mobilePhone)){
	     	$(".infoTips").show().find("p").text("请输入11位手机号码");
	     	flag=false;
	     }
	     if(flag){
	     	userDateSumbit(userName,mobilePhone);
	     }
	     
	})
}

//验证手机号
function  isMobile(phone) {
        return /^0?(13[0-9]|15[012356789]|18[012356789]|14[57]|17[03678])[0-9]{8}$/.test(phone);
}
// 验证姓名
function ischinese(name){
	    return /^[\u2E80-\u9FFF]{2,10}$/.test(name)
} 

function userAction(){
	//当前页面pageId
	var pageId = "cfbId";
	//来源页面ID
	var pageSourceId = "";
	//事件Id
	var eventId = "";
	
	$(document).ready(function(e){
		//来源页面ID
		pageSourceId = getUrlParameter("pageSourceId");
		if(null == pageSourceId || pageSourceId == ""){
			pageSourceId = pageId;
		}
		eventId = getUrlParameter("eventId");
		if(null == eventId || eventId == ""){
			eventId = pageId;
		}
		if(!!pageSourceId && !!eventId){
		    operatingRecord(pageSourceId,pageId,eventId,"");
		}
	})
}