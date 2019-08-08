
var eventId="";
var pageSource=""
var userId = "";
var userid = getUrlSearchParams("userid") ? getUrlSearchParams("userid") : localStorage.getItem("userid");
var openId = "";
var host = window.location.protocol + "//" + window.location.host;
initPage();
localStorage.setItem("type", "cfbAll")

function initPage() {
    localStorage.setItem("target", "cfbAll/guide");
    if (!userid) { //判断用户有没有静默授权
        addCookie("eventId", getUrlSearchParams("eventId"))
        addCookie("pageSource", getUrlSearchParams("pageSource"))
        location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host +"/WeixinService/H5modules/H5Content/" + localStorage.getItem("target") + ".html";
    } else {
        eventId=getUrlSearchParams("eventId") ? getUrlSearchParams("eventId") : getCookie("eventId")
        pageSource=getUrlSearchParams("pageSource") ? getUrlSearchParams("pageSource") : getCookie("pageSource")
        operatingRecord(pageSource, eventId, pageId, "", "", "", "")
    }
}
$(function(){
    cmwaOnkeyup("userName;mobilePhone",checkAppoInfoParam);
    queryUserInfo()
    checkUserBind();
    elemBind()
    queryBusiness()
})

function checkUserBind() {
    $.ajax({
        async: false,
        url: apiHost + "/api/userAutoLogin",
        type: "get",
        dataType: 'json',
        data: {
            "userId": userId
        },
        success: function (data) {
            if (data.success) {
                var type = localStorage.getItem("type");
                if (data.response.returnCode == "Y") {  //已绑定
                    if(location.href.indexOf("index")<0&&location.href.indexOf("bussinessCard")<0){
                        location.href = "/WeixinService/H5modules/H5Content/cfbAll/index.html?eventId="+eventId+"&pageSource="+pageSource;
					}
                }
            }
        },
        error: function () {
            errorRemark("接口获取用户信息服务器异常，请稍后再试。");
        }
    });
}

function queryUserInfo() {

    if (!!userid) {  //如果有userid
        $.ajax({
            url: apiHost + "/api/wxuserinfo",
            async: false,
            type: "get",
            data: {
                userId: userid
            },
            dataType: "json",
            success: function (res) {
                //alert(userid)
                if (res.success) {
                    userId = res.response.userId;
                    localStorage.setItem("userId", userId);
                    localStorage.setItem("userid", userid);
                } else {
                    //errorRemark("解密失败");
                    localStorage.removeItem("userId", userId);
                    localStorage.removeItem("userid", userid);
                    location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host + "/WeixinService/H5modules/H5Content/" + localStorage.getItem("target") + ".html";
                }
            },
            error: function () {
                errorRemark("接口获取userid服务器异常，请稍后再试。");
            }
        })
    }

}



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
    var typeId=$("input[name=typeId]").val();
    if(!typeId){
        typeId="cfb182"
    }
    $("input[name=typeId]").val("")
	$.ajax({
			async : false,
			url:apiHost + "/api/insertAppointInfo",
			type : "post",
			dataType : 'json',
			data : {
				"userName":userName,
				"mobilePhone":mobilePhone,
				"productType":typeId,
				"userId":userId,
				"cmfUserId":localStorage.getItem("cmfUserId")
			},
			success : function(data) {
				if(data.success){
					$(".reserves").addClass("hide")
					$(".success").removeClass("hide")
					$("input[name=userName]").val('');
					$("input[name=mobilePhone]").val('');
					$("#saveBtn").addClass("disabled").attr("onclick","");
				    operatingRecord(pageSource,"event_cfbAll_subscribeId",pageId,"","","","")
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
 * 查询专属顾问
 */
function queryBusiness() {
    $.ajax({
        async : false,
        url: "/WeixinService/activity/queryCustService.xhtml",
        type : "post",
        dataType : 'json',
        data : {
            "userId":userId
        },
        success : function(data) {
            if(data.returnCode=="0"){
            	$(".erweima").attr("src",data.data.object.custserPersonalQrCodeStr);
            	$(".t3 a").attr("href","tel:"+data.data.object.custserMobile)
            }
        }
    });
}

/**
 * 元素事件绑定
 */
function elemBind(){
	var fundId=queryParamList1("SYSTEM","H5ProUrlFundId","")[0].pmco;
    var period=queryParamList1("SYSTEM","H5ProUrlPeriod","")[0].pmco;
	
	var fundId2=queryParamList1("SYSTEM","H5ProUrlFundId2","")[0].pmco;
    var period2=queryParamList1("SYSTEM","H5ProUrlPeriod2","")[0].pmco;
    queryProductInfo(fundId,period)

	$(".once1").click(function(){
		operatingRecord(pageSource,"event_cfb182_buyId","","","","","")
		redirectUrl("/WeixinService/business/query/fundInfoNew.shtml?fundId="+fundId+"&period="+period);
    });
    $(".once2").click(function(){
		operatingRecord(pageSource,"event_cfb126_buyId","","","","","")
		redirectUrl("/WeixinService/business/query/fundInfoNew.shtml?fundId="+fundId2+"&period="+period2);
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
        return /^0?(13[0-9]|15[0123456789]|18[0123456789]|14[0123456789]|17[0123456789])[0-9]{8}$/.test(phone);
}
/**
 * 预约
 */
function bussiness(){
     operatingRecord(pageSource,"event_cfbAll_interestId",pageId,"","","","")
	 $(".reserves").removeClass("hide");
	 $("body,html").css({"overflow":"auto","height":"100%"});
}
/**
 *我的顾问
 */
function myAdviser(){
	location.href="/WeixinService/H5modules/H5Content/cfbAll/bussinessCard.html?pageSource="+pageSource+"&eventId=event_cfbAll_adviserId"
}
function seeProduct(){
	location.href="/WeixinService/H5modules/H5Content/cfbAll/index.html?pageSource="+pageSource+"&eventId=event_cfbAll_h5Id"
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


function queryProductInfo(fundId,period) {
    $.ajax({
        async : false,
        url: "/WeixinService/queryFundInfo.xhtml",
        type : "post",
        dataType : 'json',
        data : {
            "fundId":fundId,
            "period":period
        },
        success : function(data) {
            if(data.returnCode=="0000"){
            	var money=data.fundInfo.money;
                $("#startBuy").text(numDiv(money,10000));
            }
        },
        error : function() {
            errorRemark("网络繁忙，请稍后再试。");
        },
    });
}

function queryParamList1(paramType,paramKey,pmValueOne){
    var data = null;
    $.ajax({
        async: !1,
        url: "/WeixinService/setUp/queryParamList.xhtml",
        data: {
            paramType :paramType,
            paramKey : paramKey,
            pmValueOne : pmValueOne
        },
        dataType: "json",
        cache: !1,
        type: "POST",
        error: function() {
            errorRemark("网络繁忙，请稍后再试。");
        },
        success: function(n) {
            if(n != null && n.resultCode =="0000"){
                data = n.data;
            }
        }
    });
    return data;
}
