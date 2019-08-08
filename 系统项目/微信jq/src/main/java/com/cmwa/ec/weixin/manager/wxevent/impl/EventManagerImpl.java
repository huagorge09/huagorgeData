package com.cmwa.ec.weixin.manager.wxevent.impl;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.cmf.weixin.auth.AuthCenter;
import com.cmf.weixin.message.MsgDtoAnalyzer;
import com.cmf.weixin.message.dto.BaseMsgDto;
import com.cmf.weixin.message.dto.req.EventReqMsgDto;
import com.cmf.weixin.message.dto.res.TextResMsgDto;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.manager.wxevent.ActivityEventHandler;
import com.cmwa.ec.weixin.manager.wxevent.EventManager;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.StringUtils;
import com.cmwa.ec.weixin.util.WeixinUtil;
import com.cmwa.ec.weixin.util.cache.ParameterCache;

/**
 * 微信请求处理实现类，事情处理总入口，
 * 根据请求类型及事件类型，分发给不同的事件处理类
 * @author liury
 *
 */

public class EventManagerImpl implements EventManager {

	private static Logger logger = Logger.getLogger(EventManagerImpl.class.getName());
	
	@Override
	public String handlerRequest(HttpServletRequest request) {
		String responseString = "";
		// 请求方式
		String method = request.getMethod();
		
		// 如果是GET过来，当成网址接入，微信服务器回调
		if (method.equalsIgnoreCase("GET")) {
			responseString = linkIn(request);
		}else{
			
			try {
				String requestString = readRequest(request);
				logger.info("请求串requestString>>>" + requestString);
				logger.info("ip======>"+getIpAddr(request));

				BaseMsgDto baseMsgDto = MsgDtoAnalyzer.analyzeReq(requestString);

			 	responseString = handler(request,baseMsgDto);
				//记录响应消息字段消息日志明细
				//baseMsgDto = MsgDtoAnalyzer.analyzeRes(responseString);
				//outLogDelegate.logOutDetail(baseMsgDto);
			} catch (Exception e) {
				logger.error("handlerWXRequest error:",e);
			}
			
		}
		
		return responseString;
	}

	
	
	
	/**
	 * 处理请求
	 * 
	 * @param reqDto
	 * @return
	 */
	public String handler(HttpServletRequest request,BaseMsgDto reqDto) throws Exception {
		String responseString = null;
		String msgType = reqDto.getMsgType();
		String openId = reqDto.getFromUserName();
		logger.debug("RequestHandler.msgType>>>"+msgType);
		// 白名单验证
		boolean isWhite = ParameterCache.isWhite(openId);
		
		//added by zengxy 20140113 白名单验证暂时去掉
		isWhite = true;
		 
		if(!isWhite){
			responseString = ParameterCache.getValue("MESSAGE", "PUBLIC", "NOTWHITELIST");
			TextResMsgDto textRes = new TextResMsgDto(reqDto, responseString);
			responseString = textRes.toDocument().asXML();
		
		}else{
			// 如果不是白名单
			//根据活动id，用对应活动处理类处理事件,并且将活动id放入session
			String activityId = getActivityId(reqDto);
			request.getSession(true).setAttribute(SessionValue.SESSION_ACTIVITYID, activityId);
			
			ActivityEventHandler eventHandler = ActivityEventHandlerFactory.getEventHanlder(activityId);
			if(eventHandler==null){
				request.getSession(true).setAttribute(SessionValue.SESSION_ACTIVITYID, WXConstants.DEFAULT_EVENT_MANAGER);
				eventHandler = ActivityEventHandlerFactory.getEventHanlder(WXConstants.DEFAULT_EVENT_MANAGER);
			}
			responseString = eventHandler.handlerWxEvent(reqDto,request);
			
		}
		
		//默认回复
		if (responseString == null || responseString.trim().length() == 0) {
			TextResMsgDto textRes = new TextResMsgDto(reqDto, ParameterCache.getDefaultRes());
			responseString = textRes.toDocument().asXML();
		}
		return responseString;
	}
	
	
	
	/**
	 * 获取当前用户参与的活动ID，若没参与活动，则返回默认活动ID
	 * 1.先从请求消息中，判断当前消息带有和活动ID相关的信息
	 * 2.再从数据库中查询当前用户是否参加了活动 --暂未加这一块判断
	 * 3.如果没有获取到活动ID，则返回默认的活动ID，000000
	 * @param reqDto
	 * @return
	 */
	public String getActivityId(BaseMsgDto reqDto){
		
		String activityId = null;
		String msgType = reqDto.getMsgType();
		
		//String openId = reqDto.getFromUserName();
		if (WXConstants.MSGTYPE_REQUEST_TEXT.equals(msgType)) {// 文本消息请求
			//TextReqMsgDto textReqDto = (TextReqMsgDto) reqDto;
			
		} else if (WXConstants.MSGTYPE_REQUEST_EVENT.equals(msgType)) {// 事件消息
			EventReqMsgDto eventReqDto = (EventReqMsgDto) reqDto;
			String eventKey = eventReqDto.getEventKey();
			//微信事件消息
			String event = eventReqDto.getEvent();
			if ("subscribe".equals(event)) {// 未关注关注
				if(!StringUtils.isEmptyString(eventKey)){
					String temp[] = eventKey.split("_");
					if(temp.length==3||temp.length==2){
						activityId = temp[1];
						activityId = activityId.replace("+", "");
					}
				}
			} else if ("SCAN".equals(event)) { // 已关注关注
				if(!StringUtils.isEmptyString(eventKey)){
					String temp[] = eventKey.split("_");
					if(temp.length==2||temp.length==1){
						activityId = temp[0];
						activityId = activityId.replace("+","");
					}
				}
			} else if ("unsubscribe".equals(event)) { // 取消关注
				
			} else if ("CLICK".equals(event)) {
				
			}
			
		}
		
		/*
		if(StringUtils.isEmptyString(activityId)){
			//根据openId判断是否有参加的活动，获取对应活动ID---》没法获取到活动id
		}
		*/
		
		if(StringUtils.isEmptyString(activityId)){
			activityId = WXConstants.DEFAULT_EVENT_MANAGER;
		}
		
		return activityId;
	}
	
	/**
	 * 网址接入，微信服务器回调
	 * @param request
	 * @return
	 */
	public String linkIn(HttpServletRequest request){
		
		String responseString = "";
		
		logger.info("******Begin process get request******");
		// 获取请求参数
		String signature = request.getParameter("signature");// 签名串
		String timestamp = request.getParameter("timestamp");// 时间戳
		String nonce = request.getParameter("nonce");// 随机数
		String echostr = request.getParameter("echostr");// 随机字符串

		logger.info("signature=" + signature + ", timestamp=" + timestamp + ", nonce=" + nonce + ",echostr="
				+ echostr);

		AuthCenter authCenter = new AuthCenter();
		// 读取配置的token
		String cmftoken = WeixinUtil.getToken();
		boolean checkFlag = false;
		checkFlag = authCenter.checkSigNature(signature, timestamp, nonce, cmftoken);
		// 如果验签失败，返回ERROR，验签成功，原样返回请求过来的随机字符串 echostr
		if (checkFlag) {
			logger.info("******网址接入认证 成功success******");
			responseString = echostr;
		} else {

			logger.info("******网址接入认证错误error******");
			// 验证失败返回错误
			responseString = "ERROR";
		}
		return responseString;
	}
	
	private String readRequest(HttpServletRequest request) throws Exception {
		BufferedInputStream bis = new BufferedInputStream(request.getInputStream());
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int count = 0;
		while ((count = bis.read()) != -1) {
			bos.write(count);
		}
		return new String(bos.toByteArray(), "UTF-8");
	}

	
	public String getIpAddr(HttpServletRequest request) {
		/***********************************************************************
		 * 获取客户端地址 开始
		 **********************************************************************/
		String serverName = request.getServerName();
		String serverPort = "";
		String serverNamePort = "";

		String chalid = "";

		int iServerPort = request.getServerPort();

		if (serverName != null)
			serverName = serverName.toLowerCase();

		serverPort = String.valueOf(iServerPort);

		serverNamePort = serverName + ":" + serverPort;

		/**
		 * 以下为获取http头中的ip信息 Added by zhanxb 2007-4-6
		 */
		StringBuffer sbWlClientIp = new StringBuffer(); // WL-Proxy-Client-IP
		// StringBuffer sbClientIp = new StringBuffer(); // Proxy-Client-IP
		// StringBuffer sbXForwardedFor = new StringBuffer(); // X-Forwarded-For
		// StringBuffer sbRemoteAddr = new StringBuffer(); // getRemoteAddr
		StringBuffer sbClientContent = new StringBuffer();

		sbWlClientIp.append(request.getHeader("WL-Proxy-Client-IP")); // WL-Proxy-Client-IP

		sbClientContent.append(sbWlClientIp);

		chalid = sbClientContent.toString();
		return chalid==null?"":chalid;
	}
	
}
