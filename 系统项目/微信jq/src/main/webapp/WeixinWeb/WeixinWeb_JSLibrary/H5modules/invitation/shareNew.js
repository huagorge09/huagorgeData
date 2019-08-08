var host = window.location.protocol + "//" + window.location.host;
$(function() {
    wxShareData();
    var title="招商财富「把握宏观·赢在未来」主题沙龙邀请函";  //标题
    var desc="清华大学经济学家孔英博士、我司投资管理部执行总经理吴江为您后市投资指点迷津！";  //描述
    var link=host+"/WeixinService/H5modules/H5Content/invitation/index.html";   //转发链接
    var imgUrl=host+"/WeixinWeb/WeixinWeb_Images/H5modules/invitation/logo2.png"  //转发图标
    function wxShareData() {
        $.get(host+"/auth/getShareSignature.html", {url:location.href.split('#')[0]}, function (res) {
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
        //alert(userId)
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
