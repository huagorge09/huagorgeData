$(function() {
    wxGetShare();
    wxShareData()
    var title="";  //标题
    var desc="";  //描述
    var link="";   //转发链接
    var imgUrl=""  //转发图标
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
