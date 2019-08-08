package com.cmwa.ec.weixin.manager.pushmessage;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cmwa.ec.weixin.util.socket.SocketUtil;

/**
 * 消息推送抽象类
 * @author liury
 *
 */
public abstract class PushMessageManager {

	private static Logger logger = Logger.getLogger(PushMessageManager.class.getName());
	
	
	//消息参数  （ messageId,cmfUserId,openId，messageType，templateCode，jsonData,messageText）
	protected Map<String,String> messageMap;
	
	protected Map<String,String> returnMap;
	
	
	public void init(Map<String, String> m){
		setMessageMap(m);
		returnMap = new HashMap<String, String>();
	}

	
	//推送消息
	public Map<String,String> sendMessage(){
		
		try {
			if(before() && getMessageText()){
				send(messageMap.get("messageText"));
			}
		} catch (Exception e) {
			returnMap.put("returnCode", "9999");
			returnMap.put("returnMessage", "system error");
			logger.error("PushMessageManager.sendMessage异常",e);
		}
		
		return returnMap;
	}
	
	public boolean before(){
		
		return true;
	}
	
	//组装消息
	public abstract boolean getMessageText();
	
	//发送
	public void send(String messageText){
		
		String returnCode = null;
		String returnMessage = null;
		logger.info("要发送socket消息的messageText："+messageText);
		String resStr = null;
		try {
			resStr = SocketUtil.sendSocketMessage(messageText);
			logger.info("发送socket请求发送模板消息，resStr："+resStr);
		} catch (Exception e) {
			logger.info("发送socket请求发送模板消息，抛出异常，resStr："+resStr,e);
		}
		
		if("0".equals(resStr)){
			returnCode = "0000";
			returnMessage = "success";
		}else{
			returnCode = "9998";
			returnMessage = "messageText error";
		}
		returnMap.put("returnCode", returnCode);
		returnMap.put("returnMessage", returnMessage);
	}

	
	public Map<String, String> getMessageMap() {
		return messageMap;
	}

	public void setMessageMap(Map<String, String> messageMap) {
		this.messageMap = messageMap;
	}


	public Map<String, String> getReturnMap() {
		return returnMap;
	}

	public void setReturnMap(Map<String, String> returnMap) {
		this.returnMap = returnMap;
	}
	
	
}
