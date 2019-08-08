$(function() {
    var wxUrl='';
    var hostUrl ='';
    var srcDev="https://wxtest1.cmwachina.com/WeixinService/H5modules/H5Content/turntableActivity/index.html";
    var srcPrd="https://wx.cmwachina.com/WeixinService/H5modules/H5Content/turntableActivity/index.html";

    if(host.indexOf("wxtest1")>-1){
        wxUrl = srcDev
        hostUrl = 'https://wxtest1.cmwachina.com';
    }else{
        wxUrl =srcPrd
        hostUrl = 'https://wx.cmwachina.com';
    }
    // wxGetShare();
    wxShareData()
    var title="粤马会车主独家福利，0元抽奖华为P30";  //标题
    var desc="奖品丰厚，速来参加！";  //描述
    var link= wxUrl;   //转发链接
    var imgUrl= hostUrl+"/WeixinWeb/WeixinWeb_Images/H5modules/turntableActivity/wxfx.jpg"  //转发图标
    function wxShareData() {
        $.get(config.service.wxForward, {url:location.href.split('#')[0]}, function (res) {
            var data=JSON.parse(res);
            wx.config({
                debug: false,
                appId: data.appId,
                timestamp: data.timestamp,
                nonceStr: data.nonceStr,
                signature: data.signature,
                jsApiList: [
                    'onMenuShareTimeline',  // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			        'onMenuShareAppMessage'
                ] 
            });
        })
    } 
    /*
     * 获取转发自定义信息
     */
    function wxGetShare(){
    	$.ajax({
			url:config.service.queryShareInfo,
			type:"post",
			dataType:'json',
			data : {
				"activityId":activityId,
				"channel":"BMW"
			},
			success:function(data) {
				if(data.returnCode=="0"){
					title=data.data.shareTitle;
					desc=data.data.shareDesc;
					link=data.data.shareLink;
					imgUrl=data.data.shareImgUrl;
				}
			}
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
