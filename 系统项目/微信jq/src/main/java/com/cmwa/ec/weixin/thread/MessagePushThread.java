package com.cmwa.ec.weixin.thread;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cmwa.ec.weixin.dto.UserInfoDto;

import net.sf.json.JSONObject;

public class MessagePushThread extends Thread{
	//日志
	private static final Logger logger = LoggerFactory.getLogger(MessagePushThread.class);
	
	private static final String syncUserInfoXml = "<xml><MsgType>syncuser</MsgType><MsgContent>{{MsgContent}}</MsgContent></xml>";
	
	Map<String, Object> map = new HashMap<String, Object>();
	
	public MessagePushThread(Map<String, Object> paramMap){
		this.map = paramMap;
	}
	
	@Override
	public void run() {
		try {
			msgPush(map);
		} catch (Exception e) {
			logger.error("消息处理线程异步任务执行异常",e);
		}
	}
	
	/**
	 * 模板消息发送
	 * @param map
	 */
	public void msgPush(Map<String, Object> map){
		int unSubscribeCount = 0;
		int successCount = 0;
		@SuppressWarnings("unchecked")
		List<UserInfoDto> userInfoDtos = (List<UserInfoDto>) map.get("userInfoDtos");
		String toUserId = (String) map.get("toUserId");
		String postXML = (String) map.get("postXML");
		String toUserName = (String) map.get("toUserName");
		String fromUserName = (String) map.get("fromUserName");
		String templateId = (String) map.get("templateId");
		String url = (String) map.get("url");
		String frontEndIp = (String) map.get("frontEndIp");
		String frontEndPort = (String) map.get("frontEndPort");
	    String[] toUserIds = null;
		String openId[] = new String[userInfoDtos.size()];
		if(null != toUserId && !toUserId.equals("")){
			toUserIds = toUserId.split(",");
		}else{
			for (int i = 0; i < userInfoDtos.size(); i++) {
				openId[i] = userInfoDtos.get(i).getOpenid();
			}
			toUserIds = openId;
		}
		for (int i = 0; i < toUserIds.length; i++) {
			String temp  =getUserInfo(syncUserInfoXml.replace("{{MsgContent}}", toUserIds[i]),frontEndIp,frontEndPort);
			if(temp == null || "error".equalsIgnoreCase(temp)) {
				logger.info("未拉取到用户信息，不做处理");
				unSubscribeCount++;
				continue;
			}
			temp = temp.substring(temp.indexOf("{"),temp.length());
			JSONObject obj = JSONObject.fromObject(temp);
			logger.info("拉取到"+toUserIds[i] +"用户的信息为"+obj);
			if(obj != null && "1".equals(String.valueOf(obj.get("subscribe")))) {
				logger.info(toUserIds[i]+"用户为关注状态，开始推送");
				try {
					sendATemplateMsg(postXML.replace("{{toUserName}}", toUserName)
							.replace("{{fromUserName}}", fromUserName)
							.replace("{{toUserId}}", toUserIds[i])
							.replace("{{createTime}}",String.valueOf(new Date().getTime()))
							.replace("{{templateId}}", templateId)
							.replace("{{url}}",url), frontEndIp, frontEndPort);
					Thread.sleep(100);
					successCount++;
				} catch (InterruptedException e) {
					logger.error("【WXEventController】pushTemplateMessage()>>>推送消息异常："+ e +">>>>>>timestamp"+System.currentTimeMillis());
				}
			} else {
				logger.info(toUserIds[i]+"用户为未关注状态，不推送");
				unSubscribeCount++;
			}
		}
		logger.info("【WXEventController】pushTemplateMessage()>>>推送消息结果：Send template message successfully.>>>>>>timestamp"+System.currentTimeMillis()+"未关注的用户有"+unSubscribeCount+"个,发送给前置机的有"+successCount+"个");
	}
	
	/**
	 * 拉取用户信息
	 */
	static String getUserInfo(String postXml,String frontEndIp,String frontEndPort) {
		Socket client = null;
		DataOutputStream out = null;
		try {
			String ip  = frontEndIp;
			int port = Integer.parseInt(frontEndPort);
			client = new Socket(ip, port);
			out = new DataOutputStream(client.getOutputStream());
			out.writeUTF(postXml);
			client.shutdownOutput();
			InputStream inputStream = client.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			String str = null;
			while(((str = reader.readLine())!=null)) {
				return new String(str.getBytes("GBK"),"UTF-8");
			}
		} catch (IOException e) {
			logger.error("拉取用户信息IO流处理异常",e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception ex) {
				logger.error("IO流关闭异常",ex);
			}
			try {
				if (client != null) {
					client.close();
				}
			} catch (Exception ex) {
				logger.error("IO流关闭异常",ex);
			}
		}
		return "error";
	}
	
	static void sendATemplateMsg(String postXml,String frontEndIp,String frontEndPort) {
		Socket client = null;
		DataOutputStream out = null;
		try {
			String ip  = frontEndIp;
			int port = Integer.parseInt(frontEndPort);
			client = new Socket(ip, port);
			out = new DataOutputStream(client.getOutputStream());
			out.writeUTF(postXml);
		} catch (IOException e) {
			logger.error("IO流异常",e);
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (Exception ex) {
				logger.error("IO流关闭异常",ex);
			}
			try {
				if (client != null) {
					client.close();
				}
			} catch (Exception ex) {
				logger.error("IO流关闭异常",ex);
			}
		}
	}
	
}
