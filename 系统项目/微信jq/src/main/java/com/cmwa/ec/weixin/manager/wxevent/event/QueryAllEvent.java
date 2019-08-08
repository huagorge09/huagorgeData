package com.cmwa.ec.weixin.manager.wxevent.event;

import java.util.Date;

import org.apache.log4j.Logger;

import com.cmf.weixin.message.dto.BaseMsgDto;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.util.WeixinUtil;

public class QueryAllEvent extends BaseEvent {
	
	public QueryAllEvent(){
		
	}
	
	private static Logger logger = Logger.getLogger(QueryAllEvent.class.getName());
	
	@Override
	public String getRes(BaseMsgDto reqDto, ParameterDto parDto) {
		String openid = reqDto.getFromUserName();
		String weixinUserName = WeixinUtil.getWeiXinUserName();
		String answer = "";
		
		//根据具体的业务，查询具体的数据，替换cmf_weixin_parameter表中PMV2字段中的变量
		
		
		answer = "<xml><ToUserName>{openid}</ToUserName><FromUserName>{weiXinUserName}</FromUserName><CreateTime>{timestamp}</CreateTime><MsgType>text</MsgType><Content>系统正在维护中，请稍后再试！</Content></xml>";
		answer = answer.replace("{openid}", openid);
		answer = answer.replace("{weiXinUserName}", weixinUserName);
		answer = answer.replace("{timestamp}", ""+(new Date()).getTime()/1000);
		
		return answer;
	}

}
