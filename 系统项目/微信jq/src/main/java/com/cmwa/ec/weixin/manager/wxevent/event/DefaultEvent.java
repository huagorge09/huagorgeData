package com.cmwa.ec.weixin.manager.wxevent.event;

import org.apache.log4j.Logger;

import com.cmf.weixin.message.dto.BaseMsgDto;
import com.cmf.weixin.message.dto.res.TextResMsgDto;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.util.WeixinUtil;
import com.cmwa.ec.weixin.util.cache.ParameterCache;




public class DefaultEvent extends BaseEvent{
	
	private Logger logger = Logger.getLogger(DefaultEvent.class);
	public DefaultEvent(){
		
	}
	
	@Override
	public String getRes(BaseMsgDto reqDto, ParameterDto parDto){
		
		// 没有绑定查询默认回复
		String answer = ParameterCache.getValue(WXConstants.PMST_MESSAGE, 
				WXConstants.MESSAGE_PMKY_BIND_ACC, 
				WXConstants.MESSAGE_PMCO_NOBIND);
		// 查询绑定账号查询链接
		String acctBindingUrl = WeixinUtil.getBindAccUrl();
		// 如果有动态 (动态链接参数)
		if (answer != null && answer.trim().length()>0) {
			answer = answer.replaceAll(WXConstants.URL_BINDACC, acctBindingUrl);
		}
		
		TextResMsgDto textRes = new TextResMsgDto(reqDto, answer);
		try {
			answer = textRes.toDocument().asXML();
		} catch (Exception e) {
			logger.error("回复消息异常：",e);
		}
		return answer;
	}
 
}
