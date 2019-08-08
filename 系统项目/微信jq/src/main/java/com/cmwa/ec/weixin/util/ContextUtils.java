package com.cmwa.ec.weixin.util;

import com.cmwa.ec.base.dto.Context;

/***
 *  日志工具类
 * @author niedc
 *
 */
public class ContextUtils {
	
	
	/***
	 * 设置日志信息
	 * @param apkind 接口编码  001 账户接口   701 查询服务 801 消息服务
	 * @param busiChannel 通道
	 * @param cmfUserId
	 * @param reqTime 请求时间
	 * @param resTime 响应时间
	 * @param sessionId
	 * @param subApkind
	 * @return
	 */
	public static Context setContext(String apkind,String busiChannel,String cmfUserId,
			long reqTime,long resTime,String sessionId,String subApkind){
		Context context=new Context();
		context.setApkind(apkind);//接口编码
		context.setBusiChannel(busiChannel);//通道
		context.setCmfUserId(cmfUserId);
		context.setReqTime(reqTime);//请求时间
		context.setResTime(resTime);//响应时间
		context.setSessionId(sessionId);
		context.setSubApkind(subApkind);
		return context;
	}

}
