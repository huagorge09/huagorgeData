/**	
 * 主机域名
 */
var host=window.location.protocol+"//"+window.location.host;
/**
 * 接口上下文 ，不是nginx需置空  nginx需要配置proxy_pass地址
 */
var apiPath="api";
/**
 * 来源页面url
 */
var sourceUrl=document.referrer;
/**	
 * 服务器API接口配置
 */
var config={
    /*
     *活动接口
     */
	service:{
		module:host+"",  //
	}

}

