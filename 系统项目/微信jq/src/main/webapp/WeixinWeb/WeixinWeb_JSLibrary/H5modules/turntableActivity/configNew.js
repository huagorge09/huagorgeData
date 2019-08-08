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
var activityId = "E83BF320190507E101D1ACD3E399D1CD";

/**
 * 奖品id
 */
// var awardId="807F97B01AE57885E050A8C0C9085A77"
/**
 * 服务器API接口配置
 */
var version="20190524"
var config = {
    version:version?version:parseInt(new Date().getTime()/3600/1000),
    service: {
        queryAllWinningCount:host+apiPath+"/WeixinService/gdbmw/queryAllWinningCount.xhtml", //查询已参加抽奖人数
        queryAwardList: host + apiPath + "/WeixinService/gdbmw/queryAwardList.xhtml",  //查询奖品列表
        getMobileVerifyCodeForHABX:host+apiPath+"/WeixinService/setUp/getMobileVerifyCodeForHABX.xhtml", //获取验证码信息
        turnDraw: host + apiPath + "/WeixinService/gdbmw/turnDraw.xhtml", //用户抽奖接口
        wxForward: host + apiPath + "/auth/getShareSignature.html",  //微信转发
        queryShareInfo: host + apiPath + "/WeixinService/queryShareInfo.xhtml", //微信转发获取转发数据信息
        sendTemplateMsg: host + apiPath + "/WeixinService/activity/sendTemplateMsg.xhtml", //点击领奖按钮调用后台模板信息
        queryParameterInfo: host + apiPath +"/WeixinService/fartherDayActivity/queryParameterInfo.xhtml", //查询用户参数
        getSubscribeState: host + apiPath +"/WeixinService/getSubscribeState.xhtml", // 判断用户是否关注公主号
    }
}
