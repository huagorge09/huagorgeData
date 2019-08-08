
var eventId="";
var pageSource=""
var userId = "";
var userid = getUrlSearchParams("userid") ? getUrlSearchParams("userid") : localStorage.getItem("userid");
var openId = "";
var host = window.location.protocol + "//" + window.location.host;
initPage();
localStorage.setItem("type", "fatherDay")

function initPage() {
    localStorage.setItem("target", "fatherDay/guide");
    if (!userid) { //判断用户有没有静默授权
        addCookie("eventId", getUrlSearchParams("eventId"))
        addCookie("pageSource", getUrlSearchParams("pageSource"))
        addCookie("toUserId", getUrlSearchParams("toUserId"))
        location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host +"/WeixinService/H5modules/H5Content/" + localStorage.getItem("target") + ".html";
    } else {
        eventId=getUrlSearchParams("eventId") ? getUrlSearchParams("eventId") : getCookie("eventId")
        pageSource=getUrlSearchParams("pageSource") ? getUrlSearchParams("pageSource") : getCookie("pageSource")
        toUserId=getUrlSearchParams("toUserId") ? getUrlSearchParams("toUserId") : getCookie("toUserId")
        // if(eventId !="" && pageSource !=""){
        //     operatingRecord(pageSource, eventId, pageId, "", "", "", "")
        // }
        if(toUserId !=''){
            operatingRecord(eventId, toUserId, pageId, "", "", "", "")
        }else{
            if(pageSource != '' && eventId != ''){
                operatingRecord(pageSource, eventId, pageId, "", "", "", "")
            }
            
           
        }
        // operatingRecord(pageSource, eventId, pageId, "", "", "", "")
    }
}
$(function(){
    cmwaOnkeyup("userName;mobilePhone",checkAppoInfoParam);
    queryUserInfo();
    checkUserBind();
    elemBind();
    queryBusiness();
    isActivityEnd();
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
                if (data.resp.returnCode == "Y") {  //已绑定
                    if(location.href.indexOf("index")<0&&location.href.indexOf("bussinessCard")<0&&location.href.indexOf("detail1")<0&&location.href.indexOf("detail2")<0){
                        location.href = "/WeixinService/H5modules/H5Content/fatherDay/index.html?eventId="+eventId+"&pageSource="+pageSource;
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
                    userId = res.resp.userId;
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

	$.ajax({
			async : false,
			url:apiHost + "/api/insertAppointInfo",
			type : "post",
			dataType : 'json',
			data : {
				"userName":userName,
				"mobilePhone":mobilePhone,
				"productType":"fatherDay",
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
					

					
				    operatingRecord(pageSource,"event_fatherDay_subscribeId",pageId,"","","","")
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
    //父亲节
	$(".once1").click(function(){
		operatingRecord(pageSource,"event_fatherDay_buyId",pageId,"","","","")
		redirectUrl("/WeixinService/business/query/fundInfo.shtml?fundId="+fundId+"&period="+period);
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
        $("body,html").css({"overflow":"auto","height":"100%"});
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
	var type="event_fatherDay_interestId";

     operatingRecord(pageSource,type,pageId,"","","","")
	 $(".reserves").removeClass("hide");
	 $("body,html").css({"overflow":"auto","height":"100%"});
}
/**
 *我的顾问
 */
function myAdviser(){
	location.href="/WeixinService/H5modules/H5Content/fatherDay/bussinessCard.html?pageSource="+pageSource+"&eventId=event_fatherDay_adviserId"
}
function seeProduct(){
	location.href="/WeixinService/H5modules/H5Content/fatherDay/index.html?pageSource="+pageSource+"&eventId=event_fatherDay_h5Id"
}






//  document.getElementById("myBtn1").addEventListener("touchstart", function() {
//  	$("#myBtn1").addClass("active")
//  })
//  document.getElementById("myBtn1").addEventListener("touchend", function() {
//  	$("#myBtn1").removeClass("active")
//  })
//  document.getElementById("myBtn2").addEventListener("touchstart", function() {
//  	$("#myBtn2").addClass("active")
//  })
//  document.getElementById("myBtn2").addEventListener("touchend", function() {
//  	$("#myBtn2").removeClass("active")
//  })


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



	$(document).ready(function(){
	    var u = navigator.userAgent, app = navigator.appVersion
	    var isIOS = !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/); //ios终端
		$("input").blur(function(){
			if (isIOS) {
				blurAdjust()
				// alert("1231321233")
			}
		});
	});
	// 解决苹果不回弹页面
	function blurAdjust(e){
		setTimeout(()=>{
			// alert("1231321233")
			if(document.activeElement.tagName == 'INPUT' || document.activeElement.tagName == 'TEXTAREA'){
				return
			}
			let result = 'pc';
			if(/(iPhone|iPad|iPod|iOS)/i.test(navigator.userAgent)) { //判断iPhone|iPad|iPod|iOS
					result = 'ios'
			}else if(/(Android)/i.test(navigator.userAgent)) {  //判断Android
					result = 'android'
			}
			
			if( result = 'ios' ){
				document.activeElement.scrollIntoViewIfNeeded(true);
			}
		},100)
	}

//分享平台
function platform(){
	$("#mask").show()
}
$("#mask").click(function(){
    $("#mask").hide()
})


$('.share-start').click(function(){
    platform();
});

// 去注册
$('.register-start').click(function(){
    operatingRecord(pageSource,"event_fatherDay_registered",pageId,"","","","")
    var host = window.location.protocol + "//" + window.location.host;
    location.href=host+'/WeixinService/otherIELogin/otherIERegister.shtml'
});


/**
* 判断活动是否结束
*/

function isActivityEnd(){
    // var data = queryParamList("SYSTEM", "ACTIVITY_FQJ", "END_TIME"); //活动结束时间
    var data = queryParamList("SYSTEM", "ACTIVITY_DT", "END_TIME"); //活动结束时间
    try {
        if (data.data.length > 0) {
            var plan = data.data[0].pmnm;
            // var beginTime = this.getNowFormatDate() //当前时间
            var beginTime = new Date().getTime(); //当前时间
            // var endTime = plan.substring(0, 4) + "/" + plan.substring(4, 6) + "/" + plan.substring(6, 8)+" "+plan.substring(8, 10)+":"+plan.substring(10, 12)+":"+plan.substring(12, 14)
            var endTime = new Date( plan.substring(0, 4) + "/" + plan.substring(4, 6) + "/" + plan.substring(6, 8)).getTime();
        //     var dateDiff =(Date.parse(endTime)-Date.parse(beginTime))/3600/1000;
            var dateDiff =endTime-beginTime
            if (dateDiff >0) {
                return true;
            } else {
                $('#overActivity').show();
                $('.share-start,.register-start,.but-start').hide();
                $('.shart-over,.register-over,.but-over').show();
                
                return false;
            }
        }
    }
    catch (e) {
        console.log(e)
    }
}


/**
 * 查询数据时间字段
 * @param {Object} paramType
 * @param {Object} paramKey
 * @param {Object} pmValueOne
 */

function queryParamList(paramType, paramKey, pmValueOne) {
    var data = null;
    $.ajax({
        async: false,
        url: '/WeixinService/fartherDayActivity/queryParameterInfo.xhtml',
        type: "get",
        data: {
            pmst: paramType,
            pmky: paramKey,
            pmco: pmValueOne
        },
        dataType: "json",
        success: function (res) {
            data = res;
        }
    })
    return data;
}


$('.shut-down').click(function(){
    $('#overActivity').hide();
});

$('.shart-over').click(function(){
    $('#overActivity').show();
});
$('.register-over').click(function(){
    $('#overActivity').show();
});

/**************************************/
var toUserId = getUrlSearchParams("toUserId") ? getUrlSearchParams("toUserId") : decodeURI(getCookie("toUserId"))  //被分享人userId




/*
 * 获取链接参数
 */
function getUrlSearchParams(_name) {
    var name, value = '';
    var str = window.location.href;
    var num = str.indexOf("?");
    str = str.substr(num + 1);
    var arr = str.split("&");
    for (var i = 0; i < arr.length; i++) {
        num = arr[i].indexOf("=");
        if (num > 0) {
            name = arr[i].substring(0, num);
            if (name.replace(/^\s+|\s+$/g, "") == _name) {
                value = arr[i].substr(num + 1);
                break;
            }
        }
    }
    return decodeURI(value);
}