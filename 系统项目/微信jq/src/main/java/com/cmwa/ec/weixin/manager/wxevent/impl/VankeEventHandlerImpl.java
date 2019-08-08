package com.cmwa.ec.weixin.manager.wxevent.impl;

import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cmf.weixin.message.dto.req.EventReqMsgDto;
import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.manager.business.TradeManager;
import com.cmwa.ec.weixin.manager.business.UserInfoManager;
import com.cmwa.ec.weixin.util.ContextUtils;
import com.cmwa.ec.weixin.util.SpringContextUtil;
import com.cmwa.ec.weixin.util.StringUtils;
import com.cmwa.ec.weixin.util.WeixinUtil;
import com.cmwa.ec.weixin.util.cache.ParameterCache;
import com.cmwa.ec.weixin.util.socket.SocketUtil;

public class VankeEventHandlerImpl extends DefaultEventHandlerImpl {
	private Logger logger = Logger.getLogger(VankeEventHandlerImpl.class);
	@Override
	public boolean bind(Map<String, String> params) {

		String serviceMessage = ParameterCache.getValue(WXConstants.PMST_MESSAGE, WXConstants.PMKY_BIND_ACC, WXConstants.PMCO_BIND_SERVICEMESSAGE_WK);
		logger.info("---------->>serviceMessage:"+serviceMessage);
		serviceMessage = serviceMessage.replace("{openid}", params.get("openId"));
		logger.info("---------->>serviceMessage_new:"+serviceMessage);
		String resStr = null;
		boolean bool = true;
		try {
			String xmlPost = StringUtils.toXmlMessage(params.get("msgType"), serviceMessage);
			resStr = SocketUtil.sendSocketMessage(xmlPost);
			logger.info("发送socket请求发送模板消息，resStr："+resStr);
		} catch (Exception e) {
			bool = false;
			logger.error("发送socket请求发送模板消息，抛出异常，resStr："+resStr,e);
		}
		return bool;
	}
	
	@Override
	public boolean register(Map<String, String> params) {
		return super.register(params);
	}
	
	
	@Override
	public String wxSubscribe(EventReqMsgDto eventReqDto) throws Exception {
		
		//关注后， 同步用户信息到本地
		UserInfoManager userInfoManager=(UserInfoManager) SpringContextUtil.getBean("userInfoManager");
		userInfoManager.syncUserInfo(eventReqDto.getFromUserName());
		String eventResult = eventReqDto.getEventKey();
		String eventKey = eventResult == null || "".equals(eventResult) ? "00000" : eventResult;
		
		String key = null;
		String serviceMessage =  null;
		String openid = eventReqDto.getFromUserName();
		String weixinUserName = WeixinUtil.getWeiXinUserName();
		
		String productMD5Code = null;
		if(!StringUtils.isEmptyString(eventKey)){
			String temp[] = eventKey.split("_");
			if(temp.length==3){
				productMD5Code = temp[2];
			}
		}
		
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, WXConstants.WEIXIN_CHANNEL,"", System.currentTimeMillis(), System.currentTimeMillis(), "", "");
		
		TradeManager trade = (TradeManager)SpringContextUtil.getBean("tradeManager");
		String returnCode = trade.activityScan(context, openid, this.getActivityId(), productMD5Code);
		
		if("0000".equals(returnCode)){
			key = WXConstants.PMST_MESSAGE + "#" + WXConstants.PMKY_PUBLIC + "#"+WXConstants.PMCO_SERVICWELCOME_WK;
			serviceMessage = getWKServiceWelcomeMsg(key,openid, weixinUserName);
		}else if("0010".equals(returnCode)){
			key = WXConstants.PMST_MESSAGE + "#" + WXConstants.PMKY_PUBLIC + "#"+WXConstants.PMCO_SERVICWELCOME_WK_NO;
			serviceMessage = getWKServiceWelcomeMsg(key,openid, weixinUserName);
		}else if("0020".equals(returnCode)){
			key = WXConstants.PMST_MESSAGE + "#" + WXConstants.PMKY_PUBLIC + "#"+WXConstants.PMCO_SERVICWELCOME_WK_NO;
			serviceMessage = getWKServiceWelcomeMsg(key,openid, weixinUserName);
		}else{
			key = WXConstants.PMST_MESSAGE + "#" + WXConstants.PMKY_PUBLIC + "#"+WXConstants.PMCO_SERVICWELCOME;
			serviceMessage = getDefaultServiceWelcomeMsg(key, openid, weixinUserName);
		}
		
		try {
			//将eventKey插入到 cmf_weixin_activity_channel
			if(!"".equals(eventKey) && eventKey!=null){//访问渠道统计
				logger.info(" 未关注用户：回复关注事件 ,记录eeeeeventKey, eventKey="+eventKey+" 。");
				userInfoManager.insertUserChannel(openid, eventKey,"0"); 
			}
		} catch (Exception e) {
			logger.error(" 未关注用户：回复关注事件 ,记录eeeeeventKey出错 eventKey="+eventKey+" 。",e);
		}
		
		logger.info("普通关注：回复关注事件，处理后的pmv1："+serviceMessage);
		
		return serviceMessage;
	}
	
	
	@Override
	public String wxScan(EventReqMsgDto eventReqDto) throws Exception {
		String eventKey = eventReqDto.getEventKey();
		
		String key = null;
		String serviceMessage =  null;
		String openid = eventReqDto.getFromUserName();
		String weixinUserName = WeixinUtil.getWeiXinUserName();
		
		String productMD5Code = null;
		if(!StringUtils.isEmptyString(eventKey)){
			String temp[] = eventKey.split("_");
			if(temp.length==2){
				productMD5Code = temp[1];
			}
		}
		 
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, WXConstants.WEIXIN_CHANNEL,"", System.currentTimeMillis(), System.currentTimeMillis(), "", "");
		
		TradeManager trade = (TradeManager)SpringContextUtil.getBean("tradeManager");
		String returnCode = trade.activityScan(context, openid, this.getActivityId(), productMD5Code);
		
		if("0000".equals(returnCode)){
			key = WXConstants.PMST_MESSAGE + "#" + WXConstants.PMKY_PUBLIC + "#"+WXConstants.PMCO_SERVICWELCOME_WK;
			serviceMessage = getWKServiceWelcomeMsg(key,openid, weixinUserName);
		}else if("0010".equals(returnCode)){
			key = WXConstants.PMST_MESSAGE + "#" + WXConstants.PMKY_PUBLIC + "#"+WXConstants.PMCO_SERVICWELCOME_WK_NO;
			serviceMessage = getWKServiceWelcomeMsg(key,openid, weixinUserName);
		}else if("0020".equals(returnCode)){
			key = WXConstants.PMST_MESSAGE + "#" + WXConstants.PMKY_PUBLIC + "#"+WXConstants.PMCO_SERVICWELCOME_WK_NO;
			serviceMessage = getWKServiceWelcomeMsg(key,openid, weixinUserName);
		}else{
			key = WXConstants.PMST_MESSAGE + "#" + WXConstants.PMKY_PUBLIC + "#"+WXConstants.PMCO_SERVICWELCOME;
			serviceMessage = getDefaultServiceWelcomeMsg(key, openid, weixinUserName);
		}
		
		try {
			//将eventKey插入到 cmf_weixin_activity_channel
			if(!"".equals(eventKey) && eventKey!=null){//访问渠道统计
				logger.info(" 已关注用户：回复关注事件 ,记录eeeeeventKey, eventKey="+eventKey+" 。");
				UserInfoManager userInfoManager=(UserInfoManager) SpringContextUtil.getBean("userInfoManager");
				userInfoManager.insertUserChannel(openid, eventKey,"1"); 
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(" 已关注用户：回复关注事件 ,记录eeeeeventKey出错 eventKey="+eventKey+" 。",e);
		}
		
		logger.info("普通关注：回复关注事件，处理后的pmv1："+serviceMessage);
		
		return serviceMessage;
	}
	
	
	@Override
	public String wxClickMenu(EventReqMsgDto eventReqDto) throws Exception 
	{
		//按照key值选出自己要处理的业务逻辑
//		if(eventReqDto.getEventKey() = "KEYXXOO")
//		{
//			//XXOO
//		}
//		else 
		return super.wxClickMenu(eventReqDto);
	}
	
	
	/**
	 * 返回万科扫码关注 欢迎推送消息
	 * @param openId
	 * @param weixinUserName
	 * @return
	 */
	private String getWKServiceWelcomeMsg(String key ,String openId,String weixinUserName){
		String loginUrl = SpringContextUtil.getProperty("wx.config.bindAccUrl");
		String registerUrl = SpringContextUtil.getProperty("wx.config.registerUrl");
		String fundCenterUrl = SpringContextUtil.getProperty("wx.config.fundCenterUrl");
		ParameterDto paramDto = (ParameterDto) ParameterCache.getData().get(key);
		String serviceMessage =  paramDto.getPmv1();
		serviceMessage = serviceMessage.replace("{openid}", openId);
		serviceMessage = serviceMessage.replace("{weiXinUserName}", weixinUserName);
		serviceMessage = serviceMessage.replace("{timestamp}", ""+(new Date()).getTime()/1000);
		serviceMessage = serviceMessage.replaceAll(WXConstants.URL_REGISTER, registerUrl);
		serviceMessage = serviceMessage.replaceAll(WXConstants.URL_BINDACC,loginUrl);
		serviceMessage = serviceMessage.replaceAll(WXConstants.URL_FUNDCENTER,fundCenterUrl);
		return serviceMessage;
	}
	
	private String getDefaultServiceWelcomeMsg(String key,String openId,String weixinUserName){
		ParameterDto paramDto = (ParameterDto) ParameterCache.getData().get(key);
		String serviceMessage =  paramDto.getPmv1();
		serviceMessage = serviceMessage.replace("{openid}", openId);
		serviceMessage = serviceMessage.replace("{weiXinUserName}", weixinUserName);
		serviceMessage = serviceMessage.replace("{timestamp}", ""+(new Date()).getTime()/1000);
		
		return serviceMessage;
	}
}
