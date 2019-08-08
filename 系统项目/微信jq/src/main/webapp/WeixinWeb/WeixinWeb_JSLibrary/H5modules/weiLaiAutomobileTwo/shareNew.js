var host = window.location.protocol + "//" + window.location.host;

$(function() {
    var wxUrl='';
    var hostUrl ='';
    var srcDev="https://wxtest1.cmwachina.com/WeixinService/H5modules/H5Content/weiLaiAutomobile/index.html";
    var srcPrd="https://wx.cmwachina.com/WeixinService/H5modules/H5Content/weiLaiAutomobile/index.html";

    if(host.indexOf("wxtest1")>-1){
        wxUrl = srcDev
        hostUrl = 'https://wxtest1.cmwachina.com';
    }else{
        wxUrl =srcPrd
        hostUrl = 'https://wx.cmwachina.com';
    }
    wxShareData()
    var title="招商财富·蔚来汽车专题活动";  //标题
    var desc="《中美贸易战，谁来守护我的家庭财富》大咖教授现场测试你的理性程度，解读你的家庭财富健康指数！";  //描述
    var link= wxUrl;   //转发链接
    var imgUrl=hostUrl+"/WeixinWeb/WeixinWeb_Images/H5modules/weiLaiAutomobile/wxfx.jpg"  //转发图标
    function wxShareData() {
        $.get("/auth/getShareSignature.html", {url:location.href.split('#')[0]}, function (res) {
            var data=JSON.parse(res);
            wx.config({
                debug: false,
                appId: data.appId,
                timestamp: data.timestamp,
                nonceStr: data.nonceStr,
                signature: data.signature,
                jsApiList: [
                    'onMenuShareTimeline',  // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
                    'onMenuShareAppMessage',
                    'onMenuShareQQ',
			        'onMenuShareWeibo'
                ] 
            });
        })
    } 
    wx.ready(function () { 
        //分享给朋友  
        wx.onMenuShareAppMessage({  
            debug: true,
            title:title,
            desc:desc, // 分享描述
            link:link, // 分享链接
            imgUrl:imgUrl, // 分享图标,绝对地址
            success: function () {
            	
            },
            error:function(){
             
            }
        });  
        //分享到朋友圈  
		wx.onMenuShareTimeline({  
		    debug: true,
            title:title,
            desc:desc, // 分享描述
            link:link, // 分享链接
            imgUrl:imgUrl, // 分享图标,绝对地址
            success: function () { 
            },
            error:function(){
               
            }
        });  
    });
});