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
var activityId = "807F97B01ACD7885E050A8C0C9085A77";

/**
 * 奖品id
 */
var awardId="807F97B01AE57885E050A8C0C9085A77"
/**
 * 服务器API接口配置
 */
var version="20190129"
var config = {
    version:version?version:parseInt(new Date().getTime()/3600/1000),
    service: {
        queryUserAward:host+apiPath+"/WeixinService/queryUserAward.xhtml", //查询用户券号码
        getCard: host + apiPath + "/WeixinService/gdbmw/getCard.xhtml",  //用户领取洗牙券
        getMobileVerifyCode:host+apiPath+"/WeixinService/setUp/getMobileVerifyCodeForHABX.xhtml", //获取验证码信息
        wxForward: host + apiPath + "/auth/getShareSignature.html",  //微信转发
        queryShareInfo: host + apiPath + "/WeixinService/queryShareInfo.xhtml", //微信转发获取转发数据信息
        insertAppointInfo:host + apiPath + "/api/insertAppointInfo" /*预约信息资料提交*/
    }
}
