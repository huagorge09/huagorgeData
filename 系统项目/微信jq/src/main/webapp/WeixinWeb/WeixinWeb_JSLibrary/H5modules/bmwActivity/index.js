var index = {
    userId: "",
    userAward: "",
    initPageData: function () { /*初始化页面数据*/
        var _self = this;
        this.userId = getUrlSearchParams("userid") ? getUrlSearchParams("userid") : decodeURI(localStorage.getItem("userid"));
        if (this.userId == null || this.userId == "" || this.userId == "null") {
            location.href = host + "/auth/proxy.html?scope=snsapi_userinfo&target_url=" + host + "/WeixinService/H5modules/H5Content/bmwActivity/index.html";
        } else {
            localStorage.setItem("userid", this.userId);
        }
        this.isAlreadyOffer();
        this.pageEvent();
    },
    isAlreadyOffer: function () { /*判断用户是否已经领取过洗牙券*/
        var _self = this;
        $.ajax({
            type: "post",
            async: false,
            url: config.service.queryUserAward,
            dataType: "json",
            data: {
                userId: _self.userId,
                activityId: activityId
            },
            success: function (data) {
                if (data.returnCode == "0") {
                    if (data.data.length > 0) { /** 该用户已经领取过优惠券*/
                        $("#offerId").html(data.data[0].awardNum)
                        if (location.href.indexOf("success") < 0) {
                            location.href = "/WeixinService/H5modules/H5Content/bmwActivity/success.html";
                        }
                    }else{
                        if (location.href.indexOf("index") < 0) {
                          location.href = "/WeixinService/H5modules/H5Content/bmwActivity/index.html";
                        }
                    }
                } else if (data.returnCode == "-1") { /**解密失败*/
                    location.href = host + "/auth/proxy.html?scope=snsapi_userinfo&target_url=" + host + "/WeixinService/H5modules/H5Content/bmwActivity/index.html";
                } else {
                    $(document).dialog({
                        type: "notice",
                        infoText: data.returnMsg,
                        autoClose: 1500,
                        position: "center"
                    });
                }
            },
            error: function () {
                $(document).dialog({
                    type: "notice",
                    infoText: "服务器异常",
                    autoClose: 1500,
                    position: "center"
                });
            }
        });
    },
    checkAppoInfoParam: function () { /*为空检验*/
        var userName = $("#userName").val();
        var mobilePhone = $("#mobilePhone").val();
        if (userName == null || userName == "") {
            $("#saveBtn").addClass("disabled").attr("onclick", "");
            return;
        } else if (mobilePhone == null || mobilePhone == "") {
            $("#saveBtn").addClass("disabled").attr("onclick", "");
            return;
        } else {
            $("#saveBtn").removeClass("disabled").attr("onclick", "index.saveAppointInfo()");
        }
    },
    saveAppointInfo: function () { /*预约信息验证 */
        var userName = $.trim($("input[name=userName]").val());
        var mobilePhone = $.trim($("input[name=mobilePhone]").val());
        var flag = true;
        var _self = this;
        if (userName == "" || !ischinese(userName)) {
            $("input[name=userName]").parents("li").find("p").show().text("请输入2-10字中文姓名");
            flag = false;
        }
        if (!isMobile(mobilePhone)) {
            $("input[name=mobilePhone]").parents("li").find("p").show().text("请输入正确的手机号码");
            flag = false;
        }
        if (flag) {
            _self.appointDate(userName, mobilePhone);
        }
    },
    appointDate: function (userName, mobilePhone) { /**提交预约信息 */
        $.ajax({
            async: true,
            url: config.service.insertAppointInfo,
            data: {
                "userName": userName,
                "mobilePhone": mobilePhone,
                "productType": "BMW"
            },
            dataType: "json",
            type: "POST",
            success: function (data) {
                $(".reserves").hide()
                $(".success").show()
                setTimeout(function(){
                    $(".success").fadeOut() 
                },1000)
                $("input[name=userName]").val('');
                $("input[name=mobilePhone]").val('');
            },
            error: function () {
                $(document).dialog({
                    type: "notice",
                    infoText: "服务器异常，请稍后再试",
                    autoClose: 1500,
                    position: "center"
                });
            }
        });
    },
    pageEvent: function () {
        var _self = this;
        var oneSrcDev="/WeixinWeb/WeixinWeb_Images/H5modules/bmwActivity/oneWxtestQrcode-unlimit.jpg";
        var onesrcPrd="/WeixinWeb/WeixinWeb_Images/H5modules/bmwActivity/oneWxPrdQrcode-unlimit.jpg";
        var twoSrcDev="/WeixinWeb/WeixinWeb_Images/H5modules/bmwActivity/twoWxtestQrcode-unlimit.jpg";
        var twosrcPrd="/WeixinWeb/WeixinWeb_Images/H5modules/bmwActivity/twoWxPrdQrcode-unlimit.jpg";
        if(host.indexOf("wxtest1")>-1){
            $("#codeImg").attr("src",oneSrcDev)
            $("#successCodeImg").attr("src",twoSrcDev)
        }else{
            $("#codeImg").attr("src",onesrcPrd)
            $("#successCodeImg").attr("src",twosrcPrd)
        }
        $("#qrcode").click(function (e) {
            $(".qrcodeMask").hide();
        });
        $(".reserves input").keyup(function (e) {
            _self.checkAppoInfoParam()
        });
        $(".reserves input").focus(function () {
            $(this).parents("div").addClass("foucs");
            $(this).parents("li").find("p").hide().text("")
        });
        $(".reserves input").blur(function () {
            $(this).parents("div").removeClass("foucs");
        });
        $("#understand").click(function (e) {
            $(".qrcodeMask").show()
        });
        $("#consultation").click(function (e) {
            $(".reserves").show();
        });
        $(".reserves .close").click(function (e) {
            $(".reserves").hide();
            $("input[name=userName]").val('');
            $("input[name=mobilePhone]").val('');
            $(".reserves li p").hide();
            $("#saveBtn").addClass("disabled").attr("onclick", "");
        });
        $(".receive").click(function (e) {
            location.href = "/WeixinService/H5modules/H5Content/bmwActivity/receive.html"
        });
        $(".success .close").click(function (e) {
            e.preventDefault();
            $(".success").hide();
        });
    }
};
index.initPageData();