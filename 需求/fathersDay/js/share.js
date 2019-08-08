$(function() {
    wxShareData();
    wxGetShare();
    var title="";  //标题
    var desc="";  //描述
    var link="";   //转发链接
    var imgUrl=""  //转发图标
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
				"channel":subscribeChannel?subscribeChannel:"cmwa"
			},
			success:function(data) {
				if(data.returnCode=="0"){
					title=data.data.shareTitle.replace("（昵称）",nickName);
					desc=data.data.shareDesc.replace("（昵称）",nickName);
					link=data.data.shareLink;
					imgUrl=data.data.shareImgUrl;
				}
			}
    	})
    	
    }
    wx.ready(function () { 
        //分享给朋友  
        //alert(userId)
        wx.onMenuShareAppMessage({  
            debug: true,
            title:title,
            desc:desc, // 分享描述
            link:link+"?toUserId="+userId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageSource+"&eventId=event_wx_fdClickShareLink", // 分享链接
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
            link:link+"?toUserId="+userId+"&subscribeChannel="+subscribeChannel+"&pageSource="+pageSource+"&eventId=event_wx_fdClickShareCLink", // 分享链接
            imgUrl:imgUrl, // 分享图标,绝对地址
            success: function () {
            	 
            },
            error:function(){
               
            }
		}); 

      
    });
});
