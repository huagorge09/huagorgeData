package com.cmwa.ec.weixin.manager.wxevent.impl;

import java.util.Date;

import org.apache.log4j.Logger;

import com.cmf.weixin.message.dto.req.EventReqMsgDto;
import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.manager.business.UserInfoManager;
import com.cmwa.ec.weixin.util.ContextUtils;
import com.cmwa.ec.weixin.util.SpringContextUtil;
import com.cmwa.ec.weixin.util.StringUtils;
import com.cmwa.ec.weixin.util.WeixinUtil;
import com.cmwa.ec.weixin.util.cache.ParameterCache;

public class MagpieFestivalHandlerImpl extends DefaultEventHandlerImpl {
	private Logger logger = Logger.getLogger(MagpieFestivalHandlerImpl.class);
	
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
		key = WXConstants.PMST_MESSAGE + "#" + WXConstants.MAGPIEFESTIVAL + "#"+WXConstants.MAGPIEFESTIVAL_TICKET;
		serviceMessage = getMagpieFestivalWelcomeMsg(key,openid, weixinUserName);
		
		try {
			//将eventKey插入到 cmf_weixin_activity_channel
			if(!"".equals(eventKey) && eventKey!=null){//访问渠道统计
				logger.info(" 未关注用户：回复关注事件 ,记录eeeeeventKey, eventKey="+eventKey+" 。");
				userInfoManager.insertUserChannel(openid, eventKey,"0"); 
			}
		} catch (Exception e) {
			e.printStackTrace();
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
		key = WXConstants.PMST_MESSAGE + "#" + WXConstants.MAGPIEFESTIVAL + "#"+WXConstants.MAGPIEFESTIVAL_TICKET;
		serviceMessage = getMagpieFestivalWelcomeMsg(key,openid, weixinUserName);
		try {
			//将eventKey插入到 cmf_weixin_activity_channel
			if(!"".equals(eventKey) && eventKey!=null){//访问渠道统计
				logger.info(" 已关注用户：回复关注事件 ,记录eeeeeventKey, eventKey="+eventKey+" 。");
				UserInfoManager userInfoManager=(UserInfoManager) SpringContextUtil.getBean("userInfoManager");
				userInfoManager.insertUserChannel(openid, eventKey,"1"); 
			}
		} catch (Exception e) {
			logger.error(" 已关注用户：回复关注事件 ,记录eeeeeventKey出错 eventKey="+eventKey+" 。",e);
		}
		
		logger.info("普通关注：回复关注事件，处理后的pmv1："+serviceMessage);
		
		return serviceMessage;
	}
	
	
	/**
	 * 返回七夕节扫码关注 欢迎推送消息
	 * @param openId
	 * @param weixinUserName
	 * @return
	 */
	private String getMagpieFestivalWelcomeMsg(String key ,String openId,String weixinUserName){
		ParameterDto paramDto = (ParameterDto) ParameterCache.getData().get(key);
		String serviceMessage =  paramDto.getPmv1();
		serviceMessage =  paramDto.getPmv1();
		serviceMessage = serviceMessage.replace("{openid}", openId);
		serviceMessage = serviceMessage.replace("{weiXinUserName}", weixinUserName);
		serviceMessage = serviceMessage.replace("{timestamp}", ""+(new Date()).getTime()/1000);
		//String xmlPost = StringUtils.toXmlMessage("service", serviceMessage);
		return serviceMessage;
	}
	
	@SuppressWarnings("unused")
	private String getDefaultServiceWelcomeMsg(String key,String openId,String weixinUserName){
		ParameterDto paramDto = (ParameterDto) ParameterCache.getData().get(key);
		String serviceMessage =  paramDto.getPmv1();
		serviceMessage = serviceMessage.replace("{openid}", openId);
		serviceMessage = serviceMessage.replace("{weiXinUserName}", weixinUserName);
		serviceMessage = serviceMessage.replace("{timestamp}", ""+(new Date()).getTime()/1000);
		return serviceMessage;
	}
}
