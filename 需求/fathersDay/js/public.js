var openId = "";   //用户openid
var imgUrl = "";   //用户头像
var subscribe = ""; //是否关注
var nickName = "";  //昵称
var groupId = "";  //分组id
var userIdTrack = "";   //未加密的userid
var userId = "";   //
var groupMdId = ""  //埋点
var countdownNum = "" //倒计时
var toUserId = getUrlSearchParams("toUserId") ? getUrlSearchParams("toUserId") : decodeURI(getCookie("toUserId"))  //被分享人userId


var subscribeChannel = getUrlSearchParams("subscribeChannel") ? getUrlSearchParams("subscribeChannel") : "cmwa"  //渠道

var eventId = getUrlSearchParams("eventId") ? getUrlSearchParams("eventId") : getCookie("eventId")
var pageSource = getUrlSearchParams("pageSource") ? getUrlSearchParams("pageSource") : getCookie("pageSource")

//时间格式化
function format(d) {
    d = new Date(d);
    return d.getFullYear() + "/" + (d.getMonth() + 1) + "/" + d.getDate();
}
//验证中文姓名
function ischinese(name) {
    return /^[\u2E80-\u9FFF]{2,10}$/.test(name)
}
//验证手机号
function isMobile(phone) {
    return /^0?(13[0-9]|15[012356789]|18[012356789]|14[57]|17[03678])[0-9]{8}$/.test(phone);
}
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
        url: config.service.queryParameterInfo,
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

/**
 * 活动时间判断判断
 */
function activeTime() {
    var data = queryParamList("SYSTEM", "ACTIVITY_FQJ", "END_TIME"); //活动结束时间
    if (data.data.length > 0) {
        var plan = data.data[0].pmnm;
        var nowTime = new Date() //当前时间
        var endTime = plan.substring(0, 4) + "-" + plan.substring(4, 6) + "-" + plan.substring(6, 8);
        if (new Date(format(endTime)) > new Date(format(nowTime))) {
            return true;
        } else {
            return false;
        }
    }
}
joinPage()
//活动结束走静默授权
function joinPage() {
    if (activeTime()) {
        $("body").show()
        queryUserInfo();
    } else {
        if (!getCookie("userId") && !getUrlSearchParams("userid")) {
            location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host + "/activity/fathersDay/index.html"
        } else {
            queryUserInfo();
            if (location.href.indexOf("end") < 0) {
                location.href = "end.html"
            }
        }
    }
}

//queryUserInfo();
/**
 * 获取查询时间间隔
 */
function intervalTime() {
    var time = queryParamList("SYSTEM", "FDACTPOLLING", ""); //查询时间间隔
    var flag = false;
    if (time.data.length > 0) {
        if (time.data[0].pmnm == "true") {
            flag = true
        }
    }
    return flag;
}


/**
 * 数据埋点
 * @param {Object} buried_PageSource
 * @param {Object} buried_PageId
 * @param {Object} buried_EventId
 * @param {Object} buried_GroupId
 */

function operatingRecord(pageSource, event, page, openid, group, unionid, data) {
    var parm = {
        'data': data,
        'event': event,
        'group': group,
        'openid': groupId,
        'pageSource': pageSource,
        'page': page,
        'trackDate': getNowFormatDate(),
        'trackMillis': new Date().getTime(),
        'unionid': unionid,
        'userId': userIdTrack
    }
    $.ajax({
        url: config.service.buriedData,
        data: JSON.stringify(parm),
        dataType: "json",
        contentType: "application/json",
        cache: false,
        type: "POST",
        success: function (n) {
        }

    });
}


/**
 * 查询用户信息
 */
function queryUserInfo() {
    if (!getCookie("userId") && !getUrlSearchParams("userid")) {
        //alert("授权")
        addCookie("toUserId", toUserId)
        addCookie("subscribeChannel", subscribeChannel)
        addCookie("eventId", eventId)
        addCookie("pageSource", pageSource)
        if (subscribeChannel == "ctrip") {
            location.href = host + "/auth/proxy.html?scope=snsapi_userinfo&target_url=" + host + "/activity/fathersDayctrip/index.html"
        } else {
            location.href = host + "/auth/proxy.html?scope=snsapi_userinfo&target_url=" + host + "/activity/fathersDay/index.html"
        }

    } else {
        //alert(getCookie("userId"))
        //alert("不授权")
        var userInfoId = getUrlSearchParams("userid") ? getUrlSearchParams("userid") : decodeURI(getCookie("userId"))
        deleteCookie("toUserId");
        if (userInfoId != "" && userInfoId != undefined && userInfoId != "undefined") {  //如果没有userid
            $.ajax({
                url: config.service.queryUserInfoByUerId,
                async: false,
                type: "post",
                data: {
                    userid: userInfoId
                },
                dataType: "json",
                success: function (res) {
                    if (res.returnCode == "0") {
                        if (res.data != "") {
                            subscribe = res.data.subscribe;
                            imgUrl = res.data.imgUrl.replace("http", "https");
                            nickName = res.data.nickName;
                            userIdTrack = res.data.userId;
                            userId = userInfoId
                            subscribeChannel = res.data.subscribeChannel ? res.data.subscribeChannel : subscribeChannel;
                            addCookie("subscribeChannel", subscribeChannel)
                            addCookie("userId", userInfoId, "", 190)
                            $("#headUrlImg").attr("imgUrl", imgUrl)
                        }
                    }
                }
            })
        }
    }

}


/**
 * 生成二维码图片
 */
function generateShare(load) {
    var codeUrl = ""
    var longUrl = "";
    var realImg = ""
    if (subscribeChannel == "ctrip") {
        url = host + "/activity/fathersDayctrip/index.html?subscribeChannel=" + subscribeChannel + "&toUserId=" + userId + "&eventId=event_wx_fdScanShareQrcode&pageSource=" + pageId
    } else {
        url = host + "/activity/fathersDay/index.html?subscribeChannel=" + subscribeChannel + "&toUserId=" + userId + "&eventId=event_wx_fdScanShareQrcode&pageSource=" + pageId
    }
    $.ajax({
        url: config.service.getUrl,
        async: false,
        type: "post",
        data: {
            url: url,
            userId: encodeURI(userIdTrack)
        },
        dataType: "json",
        success: function (res) {
            try {
                codeUrl = host + "/WeixinService/u.xhtml?k=" + encodeURI(res.data.k)
                var img = new imgCompose("imgDiv", {
                    imgUrlBg: "images/invate.jpg",
                    logoUrl: "images/logoIcon.jpg",
                    load: load,
                    nickName: nickName,
                    headUrl: "images/default.png",
                    correctLevel: QRCode.CorrectLevel.H,
                    text: codeUrl
                });
            } catch (e) {
                var img = new imgCompose("imgDiv", {
                    imgUrlBg: "images/invate.jpg",
                    logoUrl: "images/logoIcon.jpg",
                    load: load,
                    nickName: nickName,
                    headUrl: "images/default.png",
                    correctLevel: QRCode.CorrectLevel.H,
                    text: host + "/activity/fathersDay/index.html"
                });
            }

        }
    })
    console.log("abc")
}
/**
 *点击显示二维码
 */
function showMyInvestor(load) {
    generateShare(load);
}

/**
 * 添加cookies
 * @param {Object} name
 * @param {Object} value
 * @param {Object} path
 * @param {Object} expiresHours
 */
function addCookie(name, value, path, expiresHours) {
    var cookieString = name + "=" + escape(value);
    //判断是否设置过期时间
    if (expiresHours > 0) {
        var date = new Date();
        date.setTime(date.getTime() + expiresHours * 3600 * 1000);
        cookieString = cookieString + "; expires=" + date.toGMTString();
    }
    cookieString += "; path=" + ( path ? path : "/");
    document.cookie = cookieString;
}
/**
 * 获取cookies
 * @param {Object} name
 */
function getCookie(name) {
    var strCookie = document.cookie;
    var arrCookie = strCookie.split("; ");
    for (var i = 0; i < arrCookie.length; i++) {
        var arr = arrCookie[i].split("=");
        if (arr[0] == name)return arr[1];
    }
    return "";
}
/**
 * 删除cookies
 * @param {Object} name
 */
function deleteCookie(name) {
    var exp = new Date();
    exp.setTime(exp.getTime() - 1);
    var cval = getCookie(name);
    if (cval != null)
        document.cookie = name + "=" + cval + ";expires=" + exp.toGMTString();
}

/**
 * 倒计时
 */

function countTime() {
    //获取当前时间  
    var date = new Date();
    var now = date.getTime();
    //设置截止时间  
    var endDate = new Date(countdownNum);
    var end = endDate.getTime();
    //时间差  
    var leftTime = end - now;
    //定义变量 d,h,m,s保存倒计时的时间  
    var h, m, s;
    if (leftTime >= 0) {
        h = Math.floor(leftTime / 1000 / 60 / 60 % 24);
        m = Math.floor(leftTime / 1000 / 60 % 60);
        s = Math.floor(leftTime / 1000 % 60);
    }
    h = h > 9 ? h : "0" + h
    m = m > 9 ? m : "0" + m
    s = s > 9 ? s : "0" + s
    var hs = h.toString();
    var ms = m.toString();
    var ss = s.toString();
    var hms = hs + ms + ss;
    var html = ""

    if (hms.indexOf("undefined") < 0) {
        var hmsArr = hms.split("");
        for (var i = 0; i < hmsArr.length; i++) {
            if (i == 2 || i == 4) {
                html += '<i>:</i>'
            }
            html += '<span>' + hmsArr[i] + '</span>'
        }
        $("#countdown em").html(html)
        setTimeout(countTime, 1000);
    } else {
        location.reload()
    }

}

/*
 * 弹窗提示信息
 */
var msgTip = {
    autoBox: function (object) {
        object = object || {};
        var random = new Date().getTime();
        var contain = object.contain || "这是一个示例内容";
        var time = object.time || 1000;
        var autoBoxHtml = '<div class="tipBox autoBox" id="autoBox" data-id="autoBox' + random + '">' + contain + '</div>';
        if ($("#autoBox").length > 0) {
            $("#autoBox").remove();
        }
        $("body").append(autoBoxHtml);
        setTimeout(function () {
            $("[data-id=autoBox" + random + "]").css("opacity", 0);
        }, time);
        setTimeout(function () {
            $("[data-id=autoBox" + random + "]").remove();
        }, time + 500);
    },
    loadingAdd: function (text) {
        var loadHtml = '<div class="load">' +
            '<div class="mask"></div>' +
            '<div class="maskLoad center">' +
            '<p><span class="loadIcon2"></span>' +
            '<p>' + text + '</p>'
        '</div>' +
        '</div>'
        $("body").append(loadHtml);
    },
    loadingRemove: function () {
        $(".load").remove();
    }
}

/**
 * 获取系统当前时间
 */
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var seperator2 = ":";
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = date.getFullYear() + seperator1 + month + seperator1 + strDate
        + " " + date.getHours() + seperator2 + date.getMinutes()
        + seperator2 + date.getSeconds();
    return currentdate;
}
