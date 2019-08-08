/**
 * h5活动页面公共js
 */
var userId = "";
var userid = getUrlSearchParams("userid") ? getUrlSearchParams("userid") : localStorage.getItem("userid");
var openId = "";
var host = window.location.protocol + "//" + window.location.host;
queryUserInfo();
checkUserBind();
initPage()


function initPage() {
    var type = localStorage.getItem("type");
    switch (type) {
        case "cfb10":
            localStorage.setItem("target", "cfb10/index");
            break;
        case "cfb126":
            localStorage.setItem("target", "cfb126/index");
            break;
        case "annualBonus":
            localStorage.setItem("target", "annualBonus/index");
            break;
        case "cfb28":
            localStorage.setItem("target", "cfb28/index");
        case "cfbAll":
            localStorage.setItem("target", "cfbAll/index");
        case "cfb203":
            localStorage.setItem("target", "cfb203/index");
        case "cfbQun":
            localStorage.setItem("target", "cfbQun/index");
        case "cfb207":
            localStorage.setItem("target", "cfb207/index");
        case "cfb209":
           localStorage.setItem("target", "cfb209/index");
        case "fatherDay":
            localStorage.setItem("target", "fatherDay/index");
        case "chenli":
            localStorage.setItem("target", "chenli/index");
            break;
        case "chenrui":
            localStorage.setItem("target", "chenrui/index");
            break;  
        case "cfbAll2":
            localStorage.setItem("target", "cfbAll2/index");    
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
                if (data.resp.returnCode == "Y") { //已绑定
				    
                    localStorage.setItem("cmfUserId", data.resp.userInfoExtend.cmfuserid);
                    if (data.resp.userInfoExtend.riskEvalStatus == "Y") { //做le适当性测评
                        var formatDate = data.resp.userInfoExtend.lastRiskEvalDate.replace("-", "/").replace("-", "/");
                        var nowDate = new Date();
                        var date = new Date(formatDate);
                        var dataDiff = (nowDate - date) / 86400000;
                        if (dataDiff >= 365) { //时间已过 则 提示过期
                            $('#reRisk').click(function () {
                                if (type == "7DaysPro") {
                                    operatingRecord(getCookie("pageSource"), "event_7DaysPro_riskExpireId", pageId, "", "", "", "")
                                } else if (type == "currentPro") {
                                    operatingRecord(getCookie("pageSource"), "event_currentPro_riskExpireId", pageId, "", "", "", "")
                                } else if (type == "cfb10") {
                                    operatingRecord(getCookie("pageSource"), "event_cfb10_riskExpireId", pageId, "", "", "", "")
                                } else if (type == "cfb126") {
                                    operatingRecord(getCookie("pageSource"), "event_cfb126_riskExpireId", pageId, "", "", "", "")
                                } else if (type == "annualBonus") {
                                    operatingRecord(getCookie("pageSource"), "event_annualBonus_riskExpireId", pageId, "", "", "", "")
                                } else if (type == "cfb28") {
                                    operatingRecord(getCookie("pageSource"), "event_cfb28_riskExpireId", pageId, "", "", "", "")
                                } else if (type == "cfbAll") {
                                    operatingRecord(getCookie("pageSource"), "event_cfbAll_riskExpireId", pageId, "", "", "", "")
                                } else if (type == "cfb203") {
                                    operatingRecord(getCookie("pageSource"), "event_cfb203_riskExpireId", pageId, "", "", "", "")
                                }else if (type == "cfbQun") {
                                    operatingRecord(getCookie("pageSource"), "event_cfbQun_riskExpireId", pageId, "", "", "", "")
                                }else if (type == "cfb207") {
                                    operatingRecord(getCookie("pageSource"), "event_cfb207_riskExpireId", pageId, "", "", "", "")
                                }else if(type == "cfb209") {
                                    operatingRecord(getCookie("pageSource"), "event_cfb209_riskExpireId", pageId, "", "", "", "")
                                }else if(type == "fatherDay") {
                                    operatingRecord(getCookie("pageSource"), "event_fatherDay_riskExpireId", pageId, "", "", "", "")
                                }else if(type == "chenli") {
                                    operatingRecord(getCookie("pageSource"), "event_chenli_riskExpireId", pageId, "", "", "", "")
                                }else if(type == "chenrui") {
                                    operatingRecord(getCookie("pageSource"), "event_chenrui_riskExpireId", pageId, "", "", "", "")
                                }else if(type == "cfbAll2") {
                                    operatingRecord(getCookie("pageSource"), "event_cfbAll2_riskExpireId", pageId, "", "", "", "")
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
                            } else if (type == "cfb10") {
                                operatingRecord(getCookie("pageSource"), "event_cfb10_riskExpireId", pageId, "", "", "", "")
                            } else if (type == "cfb126") {
                                operatingRecord(getCookie("pageSource"), "event_cfb126_riskExpireId", pageId, "", "", "", "")
                            } else if (type == "annualBonus") {
                                operatingRecord(getCookie("pageSource"), "event_annualBonus_riskExpireId", pageId, "", "", "", "")
                            } else if (type == "cfb28") {
                                operatingRecord(getCookie("pageSource"), "event_cfb28_riskExpireId", pageId, "", "", "", "")
                            }else if (type == "cfbAll") {
                                operatingRecord(getCookie("pageSource"), "event_cfbAll_riskExpireId", pageId, "", "", "", "")
                            }else if (type == "cfb203") {
                                operatingRecord(getCookie("pageSource"), "event_cfb203_riskExpireId", pageId, "", "", "", "")
                            }else if (type == "cfbQun") {
                                operatingRecord(getCookie("pageSource"), "event_cfbQun_riskExpireId", pageId, "", "", "", "")
                            }else if (type == "cfb207") {
                                operatingRecord(getCookie("pageSource"), "event_cfb207_riskExpireId", pageId, "", "", "", "")
                            }else if(type == "cfb209"){
                                operatingRecord(getCookie("pageSource"), "event_cfb209_riskExpireId", pageId, "", "", "", "")
                            }else if(type == "fatherDay"){
                                operatingRecord(getCookie("pageSource"), "event_fatherDay_riskExpireId", pageId, "", "", "", "")
                            }else if(type == "chenli"){
                                operatingRecord(getCookie("pageSource"), "event_chenli_riskExpireId", pageId, "", "", "", "")
                            }else if(type == "chenrui"){
                                operatingRecord(getCookie("pageSource"), "event_chenrui_riskExpireId", pageId, "", "", "", "")
                            }else if(type == "cfbAll2") {
                                 operatingRecord(getCookie("pageSource"), "event_cfbAll2_riskExpireId", pageId, "", "", "", "")
                            }
                            checkUserBaseInfoIsExist('fundModRiskHouseAddr');
                        });
                        //展示风险测评框
                        $("#noOperation").show()
                        $("#riskContent").show();
                    }
                } else {
					//alert("没做绑定跳转页面")
                    location.href = "/WeixinService/H5modules/login.html"
                }
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

    if (!!userid) { //如果有userid
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

        param += "nation=" + nation + "&nationNM=" + encodeURI(nationNM) + "&province=" + province + "&provinceNM=" + encodeURI(provinceNM) +
            "&city=" + city + "&cityNM=" + encodeURI(cityNM) + "&address=" + encodeURI(address) + "&vocCode=" + vocCode + "&vocName=" + encodeURI(vocName) +
            "&taxResidentType=" + taxResidentType + "&taxResidentTypeNM=" + taxResidentTypeNm + "&dateOfBirth=" + birthDate +
            "&eventId=" + eventId + "&pageSource=" + pageSource;
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
        data: {
            "cmfUserId": localStorage.getItem("cmfUserId")
        },
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