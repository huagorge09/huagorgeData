var index = {
    userId: "",
    initPageData: function () { /*初始化页面数据*/
        var _self = this;
        this.userId = getUrlSearchParams("userid") ? getUrlSearchParams("userid") : decodeURI(localStorage.getItem("userid"));
        if (this.userId == null || this.userId == "" || this.userId == "null") {
            location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host + "/WeixinService/H5modules/H5Content/bmwActivity/rules.html";
        } else {
            localStorage.setItem("userid", this.userId);
        }
        this.isAlreadyOffer();
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
                    }else{
                        if (location.href.indexOf("index") < 0) {
                          location.href = "/WeixinService/H5modules/H5Content/bmwActivity/index.html";
                        }
                    }
                } else if (data.returnCode == "-1") { /**解密失败*/
                    location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host + "/WeixinService/H5modules/H5Content/bmwActivity/rules.html";
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
    }
};
index.initPageData();