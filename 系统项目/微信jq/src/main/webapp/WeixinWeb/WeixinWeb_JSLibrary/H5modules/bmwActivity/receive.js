var receive={
      sessionID:"",
      userId:localStorage.getItem("userid"),
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
                            if (location.href.indexOf("success") < 0) {
                                location.href = "/WeixinService/H5modules/H5Content/bmwActivity/success.html";
                            }
                        }
                    } else if (data.returnCode == "-1") { /**解密失败*/
                        location.href = host + "/auth/proxy-silent.html?scope=snsapi_base&target_url=" + host + "/WeixinService/H5modules/H5Content/bmwActivity/index.html";
                    } else {
                        $(document).dialog({
                            type: "notice",
                            infoText: data.returnMsg,
                            autoClose: 3000,
                            position: "center"
                        });
                    }
                },
                error: function () {
                    $(document).dialog({
                        type: "notice",
                        infoText: "服务器异常",
                        autoClose: 3000,
                        position: "center"
                    });
                }
            });
       },
      sendMessage:function (obj) {
        var _self=this;
        if(!$(obj).hasClass("disabled")){
            if($("input[name=telPhone]").val()==""){
                $(document).dialog({type : 'notice',infoText: '请输入手机号码',autoClose: 3000,position: 'center'});
            }else if(!isMobile($("input[name=telPhone]").val())){
                $(document).dialog({type : 'notice',infoText: '手机号码格式不正确',autoClose: 3000,position: 'center'});
            }else{
                _self.getCaptcha()
            }
        }
      },
      getCaptcha: function() {        /*发送验证码*/
        var _self=this;
        var toast = $(document).dialog({
            type: "notice",
            content:'<img class="info-icon" src="/WeixinWeb/WeixinWeb_Images/H5modules/huaanInsurance/loading.gif" alt="" /><span class="info-text">正在发送中</span>'
        });
        $.ajax({
          type: "post",
          async: false,
          url: config.service.getMobileVerifyCode,
          data: {
            mobile: $("input[name=telPhone]").val()
          },
          success: function(res) {
            toast.close();
            var data = JSON.parse(res);
            if (data.returnCode == "0000") {
             _self.sessionID=data.sessionID;
              $(document).dialog({ type: "notice",infoText: "验证码发送成功", autoClose: 3000,position: "center"});
              countDown({
                elem: "#getCaptcha",
                disable: "disabled",
                time: 60
              });
            } else {
              $(document).dialog({type: "notice", infoText: "验证码发送失败，请稍后再试",autoClose: 3000, position: "center"});
            }
          },
          error: function() {
            toast.close();
            $(document).dialog({type: "notice",infoText: "服务器异常",autoClose: 3000,position: "center"});
          }
        });
      },
      submitBoothDate: function() {   /*提交用户数据领取洗牙券*/
        var _self=this; 
        var userName = $("input[name=userName]").val();
        var telPhone = $("input[name=telPhone]").val();
        var captcha = $("input[name=captcha]").val();
        if(userName==""){
            $(document).dialog({ type: "notice", infoText: "请输入您的姓名",autoClose: 3000,position: "center"});
        }else if(telPhone==""){
            $(document).dialog({ type: "notice", infoText: "请输入您的手机号码",autoClose: 3000,position: "center"});
        } else if (!isMobile(telPhone)) {
            $(document).dialog({ type: "notice", infoText: "手机号码格式不正确",autoClose: 3000,position: "center"});
        } else if (captcha=="") {
            $(document).dialog({ type: "notice", infoText: "验证码不能为空",autoClose: 3000,position: "center"});
        }   else {
          var toast = $(document).dialog({
            type: "toast",
            infoIcon: "/WeixinWeb/WeixinWeb_Images/H5modules/huaanInsurance/loading.gif",
            infoText: "领取中"
          });
          $.ajax({
            async: true,
            url: config.service.getCard,
            data: {
              "userFullName": userName,
              "userMobile": telPhone,
              "smsCode": captcha,
              "sessionID":_self.sessionID,
              "userId":_self.userId,
              "activityId":activityId,
              "awardId":awardId
            },
            dataType: "json",
            type: "POST",
            success: function(data) {
              if (data.returnCode == "0000") {
                location.href = "/WeixinService/H5modules/H5Content/bmwActivity/success.html";
              } else if (data.returnCode == "9998") {
                $(document).dialog({ type: "notice",infoText: "请点击获取验证码",autoClose: 3000, position: "center" });
              } else if (data.returnCode == "9997") {
                $(document).dialog({type: "notice",infoText: "领取的手机号码与短信验证码不一致",autoClose: 3000,position: "center"});
              } else if (data.returnCode == "9996") {
                $(document).dialog({type: "notice", infoText: "短信验证码验证失败",autoClose: 3000, position: "center" });
              }else{
                $(document).dialog({ type: "notice", infoText:data.returnMsg,autoClose: 3000, position: "center"});
              }
              toast.close();
            },
            error: function() {
              toast.close();
              $(document).dialog({ type: "notice", infoText: "服务器异常，请稍后再试",autoClose: 3000, position: "center"});
            }
          });
        }
      }
}
receive.isAlreadyOffer()