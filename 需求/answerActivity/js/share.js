$(function() {
    wxShareData();
    function wxShareData() {
        $.post(config.service.wxForward, {url:location.href.split('#')[0]}, function (res) {
            var data=JSON.parse(res);
           // alert("appid:"+data.data.appId)
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
            title:config.share.title,
            desc:config.share.desc, // 分享描述
            link:config.share.link, // 分享链接
            imgUrl:config.share.imgUrl, // 分享图标,绝对地址
            success: function () {
                //userShare()
            }, //成功回调
            cancel: function () {
            	alert("分享失败")
            }, //失败回调
        });  
        //分享到朋友圈  
        wx.onMenuShareTimeline({  
            title:config.share.title,
            desc:config.share.desc, // 分享描述
            link:config.share.link, // 分享链接
            imgUrl:config.share.imgUrl, // 分享图标,绝对地址
            success: function () {
            	userShare()
            }, //成功回调
            cancel: function () {
            	alert("分享失败")
            }, //失败回调
        }); 
      
    });
    

    function userShare(){  //用户分享转发回调
    	$.ajax({
			url:config.service.userForward,
			type:"post",
			data:{
				"openId":openId
			},
			dataType:"json",
			success:function(res){
				if(res.returnCode!="0"){
					alert("服务器异常")
				}
			}
		})
    }
    
    
});
