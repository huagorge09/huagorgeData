/**
 * h5活动页面公共js
 */
var userId = "";
var userid = getUrlSearchParams("userid") ? getUrlSearchParams("userid") : localStorage.getItem("userid");
var openId = "";
var host = window.location.protocol + "//" + window.location.host;
initPage();

function initPage() {
    var type = localStorage.getItem("type");
    switch (type) {
        case "cfb10":
            localStorage.setItem("target", "cfb10/index");
            break;
        case "yh":
            localStorage.setItem("target", "yh/index");
            break;
        case "heli":
            localStorage.setItem("target", "heli/index");
            break;
        case "cfbp2":
            localStorage.setItem("target", "cfbp2/index");
            break;
        case "heli6":
            localStorage.setItem("target", "heli6/index");
            break;
        case "cfb273":
            localStorage.setItem("target", "cfb273/index");
            break;
        case "7DaysPro":
            localStorage.setItem("target", "sevenDayProduct/index");
            break;
        case "currentPro":
            localStorage.setItem("target", "currentProduct/index");
        case "fatherDay":
            localStorage.setItem("target", "fatherDay/index");
    }
    queryUserInfo();

    if (!userid) {  //判断用户有没有静默授权
        addCookie("eventId", getUrlSearchParams("eventId"))
        addCookie("pageSource", getUrlSearchParams("pageSource"))
        location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host + "/WeixinService/H5modules/H5Content/" + localStorage.getItem("target") + ".html";
    } else {
        var eventId = getUrlSearchParams("eventId") ? getUrlSearchParams("eventId") : getCookie("eventId")
        var pageSource = getUrlSearchParams("pageSource") ? getUrlSearchParams("pageSource") : getCookie("pageSource")
        operatingRecord(pageSource, eventId, pageId, "", "", "", "")
        checkUserBind()
    }

}

/*
 * 根据openid判断用户是否绑定，如果绑定则返回所有用户信息，如实名信息，适当性信息
 */
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
                    localStorage.setItem("cmfUserId", data.response.userInfoExtend.cmfuserid);
                    if (data.response.userInfoExtend.riskEvalStatus == "Y") {//做le适当性测评
                        var formatDate = data.response.userInfoExtend.lastRiskEvalDate.replace("-", "/").replace("-", "/");
                        var nowDate = new Date();
                        var date = new Date(formatDate);
                        var dataDiff = (nowDate - date) / 86400000;
                        if (dataDiff >= 365) {  //时间已过 则 提示过期
                            $('#reRisk').click(function () {
                                if (type == "7DaysPro") {
                                    operatingRecord(getCookie("pageSource"), "event_7DaysPro_riskExpireId", pageId, "", "", "", "")
                                } else if (type == "currentPro") {
                                    operatingRecord(getCookie("pageSource"), "event_currentPro_riskExpireId", pageId, "", "", "", "")
                                }

                                checkUserBaseInfoIsExist('riskLevel');
                            });
                            $("#noOperation").show();
                            $("#reRiskContent").show();
                        }
                    } else { //没有做适当性
                        $('#goRisk').click(function () {
                            if (type == "7DaysPro") {
                                operatingRecord(getCookie("pageSource"), "event_7DaysPro_noRiskRatingId", pageId, "", "", "", "")
                            } else if (type == "currentPro") {
                                operatingRecord(getCookie("pageSource"), "event_currentPro_noRiskRatingId", pageId, "", "", "", "")
                            }
                            checkUserBaseInfoIsExist('fundModRiskHouseAddr');
                        });
                        //展示风险测评框
                        $("#noOperation").show()
                        $("#riskContent").show();
                    }
                } else {
                    location.href = "/WeixinService/H5modules/login.html"
                }
            } else {
                //errorRemark("接口获取用户信息异常，请稍后再试。");
                localStorage.removeItem("userid")
                localStorage.removeItem("userId")
                location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host + "/WeixinService/H5modules/H5Content/" + localStorage.getItem("target") + ".html";
            }
        },
        error: function () {
            errorRemark("接口获取用户信息服务器异常，请稍后再试。");
        }
    });
}

/**
 * 查询用户信息
 */
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

/*
 * 点击适当性提示 后去操作的页面
 */
function checkUserBaseInfoIsExist(target) {
    var resultData = queryUserinfo();
    if ("riskLevel" == target) {
        redirectUrl("/WeixinService/H5modules/riskAssessment/riskLevel.html?viewType=prodcut");
    } else {
        //选择地址 带入默认值
        var param = "?";
        var nation = resultData.nation ? resultData.nation : "";
        var nationNM = resultData.nationNM ? resultData.nationNM : "";
        var province = resultData.province ? resultData.province : "";
        var provinceNM = resultData.provinceNM ? resultData.provinceNM : "";
        var city = resultData.city ? resultData.city : "";
        var cityNM = resultData.cityNM ? resultData.cityNM : "";
        var vocCode = resultData.vocCode ? resultData.vocCode : "";
        var vocName = resultData.vocName ? resultData.vocName : "";
        var address = resultData.addr ? resultData.addr : "";
        var taxResidentType = resultData.taxResidentType ? resultData.taxResidentType : "";
        var taxResidentTypeNm = resultData.taxResidentTypeNm ? resultData.taxResidentTypeNm : "";
        var birthDate = resultData.birthDate ? resultData.birthDate : "";

        param += "nation=" + nation + "&nationNM=" + encodeURI(nationNM) + "&province=" + province + "&provinceNM=" + encodeURI(provinceNM)
            + "&city=" + city + "&cityNM=" + encodeURI(cityNM) + "&address=" + encodeURI(address) + "&vocCode=" + vocCode + "&vocName=" + encodeURI(vocName)
            + "&taxResidentType=" + taxResidentType + "&taxResidentTypeNM=" + taxResidentTypeNm + "&dateOfBirth=" + birthDate
            + "&eventId=" + eventId + "&pageSource=" + pageSource;
        redirectUrl('/WeixinService/H5modules/riskAssessment/fundModRiskHouseAddr.html' + param);
    }
}

/*
 * 查询用户信息
 */
function queryUserinfo() {
    var resultData = {};
    $.ajax({
        async: false,
        url: apiHost + "/api/queryUserinfo",
        data: {"cmfUserId": localStorage.getItem("cmfUserId")},
        dataType: "json",
        cache: false,
        type: "get",
        error: function (textStatus, errorThrown) {
            errorRemark("网络繁忙，请稍后再试。");
        },
        success: function (data) {
            if (data.success) {

                if ((data.resp.userType == null || data.resp.userType != "30")) {
                    isRealName = false;
                }

                if (data.resp.returnCode != null && data.resp.returnCode == "0000") {
                    var flag = false;
                    if (!data.resp.nation || !data.resp.vocCode || !data.resp.taxResidentType || !data.resp.birthDate) {
                        flag = true;
                    }
                    resultData.nation = data.resp.nation;
                    resultData.nationNM = data.resp.nationNM;
                    resultData.province = data.resp.province;
                    resultData.provinceNM = data.resp.provinceNM;
                    resultData.city = data.resp.city;
                    resultData.cityNM = data.resp.cityNM;
                    resultData.vocCode = data.resp.vocCode;
                    resultData.vocName = data.resp.vocCodeNM;
                    resultData.flag = flag;
                    resultData.addr = data.resp.addr;
                    resultData.taxResidentType = data.resp.taxResidentType;
                    resultData.taxResidentTypeNm = data.resp.taxResidentTypeNm;
                    resultData.birthDate = data.resp.birthDate;
                    resultData.syncInvprtpAlert = data.resp.syncInvprtpAlert;
                    resultData.cmfUserId = data.resp.cmfUserId;
                }

            }
        }
    });

    return resultData;
}
