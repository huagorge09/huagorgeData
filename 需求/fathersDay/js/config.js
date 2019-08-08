/**
 * 主机域名
 */
var host = window.location.protocol + "//" + window.location.host;
/**
 * 接口上下文 ，不是nginx需置空
 */
var apiPath = "";///
/**
 * 来源页面url
 */
var sourceUrl = document.referrer;

/**
 * 当前url
 */
var link = location.href;
/*
 * 活动编码
 */
var activityId = "b0baa38fbb61471bb93f09e06ae34cbe";
/**
 * 服务器API接口配置
 */
var config = {
    service: {
        queryUserInfoByUerId: host + apiPath + "/WeixinService/investor/queryUserInfoByUerId.xhtml",  //获取用户openid及关注
        wxForward: host + apiPath + "/WeixinService/investor/wxForward.xhtml",  //微信转发
        queryShareInfo: host + apiPath + "/WeixinService/queryShareInfo.xhtml",  //微信转发
        buriedData: host + apiPath + "/api/wxtrack", //数据埋点
        checkUserCanSignIn: host + apiPath + "/WeixinService/fatherDayActivity/checkUserCanSignIn.xhtml", //判断用户是否签到
        userSignIn: host + apiPath + "/WeixinService/fatherDayActivity/userSignIn.xhtml",  //用户签到
        queryUserAward: host + apiPath + "/WeixinService/queryUserAward.xhtml", //查询用户奖品列表
        saveUserAwardInfo: host + apiPath + "/WeixinService/saveUserAwardInfo.xhtml", //用户兑奖奖品
        shardList: host + apiPath + "/WeixinService/fartherDayActivity/shardList.xhtml", //查询用户碎片
        luckDraw: host + apiPath + "/WeixinService/fartherDayActivity/luckDraw.xhtml",//用户抽奖
        updateUserAwardInfo: host + apiPath + "/WeixinService/updateUserAwardInfo.xhtml", //更新用户兑奖信息
        queryUserAwardInfo: host + apiPath + "/WeixinService/queryUserAwardInfo.xhtml", //查询用户兑奖信息
        querySystemNotice: host + apiPath + "/WeixinService/fatherDayActivity/querySystemNotice.xhtml", //查询系统通知
        queryUserNotice: host + apiPath + "/WeixinService/fatherDayActivity/queryUserNotice.xhtml", //查询用户自己的通知
        queryParameterInfo: host + apiPath + "/WeixinService/fartherDayActivity/queryParameterInfo.xhtml", // 查询字典信息
        getUrl: host + apiPath + "/WeixinService/u.xhtml",  //生成短连接给二维码
        obtainMessageList: host + apiPath + "/WeixinService/fartherDayActivity/obtainMessageList.xhtml",  //查询好友助力消息列表接口
        queryAllUserAwardInfo: host + apiPath + "/WeixinService/fatherDayActivity/queryAllUserAwardInfo.xhtml"  //查询所有用户中奖信息
    }
}
