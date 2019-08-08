package com.cmwa.ec.weixin.manager.wxevent;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cmf.weixin.message.dto.BaseMsgDto;
import com.cmf.weixin.message.dto.req.EventReqMsgDto;
import com.cmf.weixin.message.dto.req.TextReqMsgDto;

public abstract class ActivityEventHandler {

	private String activityId; 
	/**
	 * 用户登陆绑定事件
	 */
	public abstract boolean bind(Map<String, String>params);
	
	/**
	 * 用户注册事件
	 */
	public abstract boolean register(Map<String, String>params);
	
	/**
	 * 用户鉴权事件
	 */
	public abstract boolean authenticate(Map<String, String>params);
	
	/**
	 * 未关注用户关注事件
	 */
	public abstract String wxSubscribe(EventReqMsgDto eventReqDto) throws Exception;
	
	/**
	 * 已关注用户扫码进入公众号事件
	 */
	public abstract String wxScan(EventReqMsgDto eventReqDto) throws Exception;
	
	/**
	 * 用户取消关注事件
	 */
	public abstract String wxUnsubscribe(EventReqMsgDto eventReqDto,HttpServletRequest request) throws Exception;
	
	/**
	 * 用户点击菜单的按钮事件
	 */
	public abstract String wxClickMenu(EventReqMsgDto eventReqDto) throws Exception;
	
	/**
	 * 用户回复事件
	 */
	public abstract String wxReply(TextReqMsgDto eventReqDto) throws Exception;
	
	/**
	 * 微信事件，该接口不要轻易重写
	 */
	public abstract String handlerWxEvent(BaseMsgDto wxMsg,HttpServletRequest request) ;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}
	
	
	
}
