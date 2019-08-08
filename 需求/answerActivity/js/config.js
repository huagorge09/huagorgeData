/**	
 * 主机域名
 */
var host=window.location.protocol+"//"+window.location.host;
/**
 * 接口上下文 ，不是nginx需置空
 */
var apiPath="";
/**
 * 来源页面url
 */
var sourceUrl=document.referrer;


/**
 * 当前url
 */
var link="https://wxtest1.cmwachina.com/auth/proxy.html?target_url=https://wxtest1.cmwachina.com/WeixinService/activity/answerActivity/index.html";  
/**	
 * 服务器API接口配置
 */
var config={
	activeEnd:"2018-05-10", //活动结束日期
	share:{            //分享配置
		title:"投资者教育抽奖活动test11111111111",     
		desc:"投资者教育抽奖活动test222222222",
		link:link,
		imgUrl:"http://192.168.8.183:17846/AppWeb/AppWeb_Images/images/logo-_img.png"
	},
	service:{
		queryUserInfoByUerId:host+apiPath+"/WeixinService/investor/queryUserInfoByUerId.xhtml",  //获取用户openId接口及用户信息
		wxForward:host+apiPath+"/WeixinService/investor/wxForward.xhtml", //微信转发接口
	    checkUserIsCanLuckDraw:host+apiPath+"/WeixinService/investor/checkUserIsCanLuckDraw.xhtml",  //判断用户是否可以抽奖
	    userForward:host+apiPath+"/WeixinService/investor/userForward.xhtml",  //转发领取图书
	    userLuckDraw:host+apiPath+"/WeixinService/investor/userLuckDraw.xhtml", //用户抽奖
	    userAnswer:host+apiPath+"/WeixinService/investor/userAnswer.xhtml",  // 用户答题记录保存
	    queryUserAward:host+apiPath+"/WeixinService/investor/queryUserAward.xhtml"  // 查询我的书架
	    userGetAward:host+apiPath+"/WeixinService/investor/userGetAward.xhtml" //	用户兑奖
	}
}
