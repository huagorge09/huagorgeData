var shareTimeLine = false;
var shareAppMessage = false;
var shareQQ = false;
var shareWeibo = false;
var appId="";
var timestamp="";
var nonceStr="";
var signature="";
$(document).ready(function(e) {
	$.ajax({
		async : false,
		url : "/WeixinService/activity/fatherDayShare.xhtml",
	    data: {"url": window.location.href},
	    dataType: "json",
	    cache: false,
	    type:"post",
	    error : function(textStatus, errorThrown) {  
	    	/*网络繁忙*/
	    }, 
	    success : function (data){
			appId = data.appId;
			timestamp = data.timestamp;
			nonceStr = data.nonceStr;
			signature = data.signature;

			wx.config({
					debug: false, /* 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。*/
					appId: appId, /* 必填，公众号的唯一标识*/
					timestamp: timestamp, /* 必填，生成签名的时间戳*/
					nonceStr: nonceStr, /* 必填，生成签名的随机串*/
					signature: signature,/* 必填，签名，见附录1*/
					jsApiList: [
								'checkJsApi',
								'onMenuShareTimeline',
								'onMenuShareAppMessage',
								'onMenuShareQQ',
								'onMenuShareWeibo' 
								] /* 必填，需要使用的JS接口列表，所有JS接口列表见附录2*/
				});
	    }
	});
});

wx.ready(function () {
    /* 1 判断当前版本是否支持指定 JS 接口，支持批量判断*/
    wx.checkJsApi({
      jsApiList: [
        'onMenuShareTimeline',
        'onMenuShareAppMessage',
        'onMenuShareQQ',
        'onMenuShareWeibo' 
      ],
      success: function (res) {
          /* alert(JSON.stringify(res));json对象转String*/
          shareTimeLine = res.checkResult.onMenuShareTimeline;
          shareAppMessage = res.checkResult.onMenuShareAppMessage;
          if(shareTimeLine==true && shareAppMessage==true){
                share();
          }
      }
    });
});


function share(){
    var titlePI = "给你一个机会，约他一起看电影！";
    var descPI = "节日到了，你的礼物准备好了么？招财好礼等你取";
    var linkPI = "https://wx.cmwachina.com/WeixinService/activity/fartherDayActivity/fartherDayActivity.shtml";
    var imgUrlPI = "https://mmbiz.qlogo.cn/mmbiz/lXt9zyAjmtuLFHlkofJQDr7Hx2gLOG5S561bib6XIE7iaSPtAh4KWZGkb9abrtbkKeEDChdr2qpMUyTgzE4nAsiaQ/0?wx_fmt=jpeg";
    
    /* 2.页面加载就注册分享监听事件 ，分享给朋友*/
    wx.onMenuShareAppMessage({
        title: titlePI, /* 分享标题*/
        desc: descPI, /* 分享描述*/
        link: linkPI, /* 分享链接*/
        imgUrl: imgUrlPI, /* 分享图标*/
        type: 'link', /* 分享类型,music、video或link，不填默认为link*/
        dataUrl: '', /* 如果type是music或video，则要提供数据链接，默认为空*/
        success: function () { 
            /* 用户确认分享后执行的回调函数*/
            /* shareSuccess(titlePI,descPI,"1");/*1:wx, 2:mobileQQ,*/
			/* 3:Qzone ,4:weibo*/
            /* window.location.href='http://www.baidu.com';*/
        },
        cancel: function () { 
        	/*用户点击取消时的回调函数，仅部分有用户取消操作的api才会用到。*/
        },
        complete:function(){
        	/*接口调用完成时执行的回调函数，无论成功或失败都会执行。*/
        },
        fail:function(){
        	/*接口调用失败时执行的回调函数。*/
        }
        
    });
    
    /* 3.页面加载就注册分享监听事件 ，分享到朋友圈*/
    wx.onMenuShareTimeline({
        title: titlePI, /* 分享标题*/
        link: linkPI, /* 分享链接*/
        imgUrl: imgUrlPI, /* 分享图标*/
        success: function () { 
            /*用户确认分享后执行的回调函数*/
        },
        cancel: function () { 
        	/*用户点击取消时的回调函数，仅部分有用户取消操作的api才会用到。*/
        },
        complete:function(){
        	/*接口调用完成时执行的回调函数，无论成功或失败都会执行。*/
        },
        fail:function(){
        	/*接口调用失败时执行的回调函数。*/
        }
    });
    /*分享到qq*/
    wx.onMenuShareQQ({
        title: titlePI, /* 分享标题*/
        desc: descPI, /* 分享描述*/
        link: linkPI, /* 分享链接*/
        imgUrl: imgUrlPI, /* 分享图标*/
        success: function () { 
           /* 用户确认分享后执行的回调函数*/
        },
        cancel: function () { 
           /* 用户取消分享后执行的回调函数*/
        }
    });
    /*分享到腾讯微博*/
    wx.onMenuShareWeibo({
    	title: titlePI, /* 分享标题*/
        desc: descPI, /* 分享描述*/
        link: linkPI, /* 分享链接*/
        imgUrl: imgUrlPI, /* 分享图标*/
        success: function () { 
           /* 用户确认分享后执行的回调函数*/
        },
        cancel: function () { 
            /* 用户取消分享后执行的回调函数*/
        }
    });
}
