var host = window.location.protocol + "//" + window.location.host;
$(function() {
    wxShareData();
    var title="传承齿科×招商财富 牙齿护理专题讲座签到";  //标题
    var desc="久等了！这可能是最关心您牙齿健康的资产管理公司了！";  //描述
    var link=host+"/WeixinService/H5modules/H5Content/signIn/index.html";   //转发链接
    var imgUrl=host+"/WeixinWeb/WeixinWeb_Images/H5modules/signIn/logo.jpg"  //转发图标
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
