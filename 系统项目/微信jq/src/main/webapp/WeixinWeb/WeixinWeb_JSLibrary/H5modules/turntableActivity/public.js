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
 * 活动时间判断判断
 */
function activeTime() {
    var data = queryParamList("SYSTEM", "ACTIVITY_DT", "END_TIME"); //活动结束时间
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
        success: function (n) {}

    });
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
    cookieString += "; path=" + (path ? path : "/");
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
        if (arr[0] == name) return arr[1];
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


function outputdollars(number) {
    if (number.length <= 3)
        return (number == '' ? '0' : number);
    else {
        var mod = number.length % 3;
        var output = (mod == 0 ? '' : (number.substring(0, mod)));
        for (i = 0; i < Math.floor(number.length / 3); i++) {
            if ((mod == 0) && (i == 0))
                output += number.substring(mod + 3 * i, mod + 3 * i + 3);
            else
                output += ',' + number.substring(mod + 3 * i, mod + 3 * i + 3);
        }
        return (output);
    }
}

//时间格式化
function format(d) {
    d = new Date(d);
    return d.getFullYear() + "/" + (d.getMonth() + 1) + "/" + d.getDate();
}
/**
 * 倒计时
 * @param {*} object
 */
function countDown(object) { //验证码倒计时
    var elem = object.elem;
    var disable = object.disable;
    var time = parseInt(object.time);
    var txt = $(elem).text();
    if (!$(elem).hasClass(disable)) {
        $(elem).addClass(disable);
        timeDowm()
        var delay = setInterval(timeDowm, 1000);

        function timeDowm() {
            time--;
            $(elem).text(time + "s");
            if (time < 1) {
                $(elem).text(txt);
                $(elem).removeClass(disable);
                clearInterval(delay);
            }
        }
    }
}

/**
 * 验证手机号码
 * @param {*} str
 * @returns
 */
function isMobile(str) {
    return /^0?(13[0-9]|10[0123456789]|11[0123456789]|12[0123456789]|15[0123456789]|18[0123456789]|14[0123456789]|16[0123456789]|17[0123456789]|19[0123456789])[0-9]{8}$/.test(str);
}

/**
 *
 * @param {*} name
 * @returns
 */
function ischinese(name) {
    return /^[\u2E80-\u9FFF]{2,10}$/.test(name)
}