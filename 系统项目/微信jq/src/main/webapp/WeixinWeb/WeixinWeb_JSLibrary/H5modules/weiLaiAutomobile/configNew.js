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
        wxForward: host + apiPath + "/WeixinService/investor/wxForward.xhtml",  //微信转发
        queryShareInfo: host + apiPath + "/WeixinService/queryShareInfo.xhtml"  //微信转发
    }
}
