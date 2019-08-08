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
var activityId = "E83BF3E7C0A8E3E101D1ACD3E399D1CD";
/**
 * 服务器API接口配置
 */
var config = {
    service: {
        isLottery: host + apiPath + "/WeixinService/activity/isLottery.xhtml",  //判断用户是否已经抽奖
        queryAllCustService: host + apiPath + "/WeixinService/activity/queryAllCustService.xhtml",  //查询专属顾问信息
        queryAwardNum: host + apiPath + "/WeixinService/activity/queryAwardNum.xhtml",  //查询活动奖品数量
        lotteryLogic: host + apiPath + "/WeixinService/activity/lotteryLogic.xhtml",  //用户抽奖
        queryUserAward: host + apiPath + "/WeixinService/queryUserAward.xhtml", //查询用户奖品
        queryParameterInfo: host + apiPath +"/WeixinService/fartherDayActivity/queryParameterInfo.xhtml", //查询用户参数
        isHighAuth: host + apiPath +"/WeixinService/activity/isHighAuth.xhtml", //判断用户是否做高级授权
        sendTemplateMsg: host + apiPath +"/WeixinService/activity/sendTemplateMsg.xhtml" //发送模板消息
    }
}
