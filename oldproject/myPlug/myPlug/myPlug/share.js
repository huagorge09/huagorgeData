$(function() {
    wxShareData();
    function wxShareData() {
        $.post(config.service.wxForward, {url:location.href.split('#')[0]}, function (res) {
            var data=JSON.parse(res);
            wx.config({
                debug: false,
                appId: data.data.appId,
                timestamp: data.data.timestamp,
                nonceStr: data.data.nonceStr,
                signature: data.data.signature,
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
            title:param.title,
            desc:param.desc, // 分享描述
            link:host+"/activity/answerActivity/index.html?groupId="+userId+"&eventId=event_wx_clickFromShareFdId&pageSourceId="+pageId, // 分享链接
            imgUrl:param.imgUrl, // 分享图标,绝对地址
            success: function () {
               
            }, //成功回调
            cancel: function () {
            	
            } //失败回调
        });  
        //分享到朋友圈  
		wx.onMenuShareTimeline({  
		    title:param.title,
		    desc:param.desc, // 分享描述
		    link:host+"/activity/answerActivity/index.html?groupId="+userId+"&eventId=event_wx_clickFromShareMomentsId&pageSourceId="+pageId, // 分享链接
		    imgUrl:param.imgUrl, // 分享图标,绝对地址
		    success: function () {
		    	
		    }, //成功回调
		    cancel: function () {
		    	
		    } //失败回调
		}); 
      
    });
   

   
});
