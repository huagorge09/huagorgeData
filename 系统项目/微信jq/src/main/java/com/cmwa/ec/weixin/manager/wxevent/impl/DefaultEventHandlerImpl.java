package com.cmwa.ec.weixin.manager.wxevent.impl;

import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import com.alibaba.fastjson.JSONArray;
import com.cmf.weixin.message.dto.BaseMsgDto;
import com.cmf.weixin.message.dto.req.EventReqMsgDto;
import com.cmf.weixin.message.dto.req.TextReqMsgDto;
import com.cmf.weixin.message.dto.res.NewsResMsgDto;
import com.cmf.weixin.message.dto.res.TextResMsgDto;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.dto.UserInfoexDto;
import com.cmwa.ec.weixin.manager.business.UserInfoManager;
import com.cmwa.ec.weixin.manager.business.UserInfoexManager;
import com.cmwa.ec.weixin.manager.wxevent.ActivityEventHandler;
import com.cmwa.ec.weixin.manager.wxevent.event.EventFactory;
import com.cmwa.ec.weixin.util.DateUtils;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.SpringContextUtil;
import com.cmwa.ec.weixin.util.StringUtils;
import com.cmwa.ec.weixin.util.WeixinUtil;
import com.cmwa.ec.weixin.util.cache.ParameterCache;
import com.cmwa.ec.weixin.util.socket.SocketUtil;

public class DefaultEventHandlerImpl extends ActivityEventHandler{
	private Logger logger = Logger.getLogger(DefaultEventHandlerImpl.class);
	
	
	@Override
	public String handlerWxEvent(BaseMsgDto wxMsg,HttpServletRequest request) {
		String responseString = null;
		logger.info("...begin微信事件处理，输入参数BaseMsgDto=" + JSONArray.toJSONString(wxMsg));
		try {
			String msgType = wxMsg.getMsgType();
			if (WXConstants.MSGTYPE_REQUEST_EVENT.equals(msgType)) {// 微信事件消息
				logger.info("微信事件消息处理...");
				
				EventReqMsgDto eventReqDto = new EventReqMsgDto();
				BeanUtils.copyProperties(wxMsg, eventReqDto);
				String event = eventReqDto.getEvent();
				
				if ("subscribe".equalsIgnoreCase(event)) {// 关注
					logger.info("未关注用户：匹配关注事件...");
					responseString = wxSubscribe(eventReqDto);
					/* 更新表cmwa_wx_user_info用户的关注状态 */
					UserInfoManager userInfoManager = (UserInfoManager) SpringContextUtil.getBean("userInfoManager");
					userInfoManager.saveOrUpdateCmwaWxUserInfo(eventReqDto, "1");
					
				} else if ("SCAN".equalsIgnoreCase(event)) { //已关注用户扫码进入公众号事件
					responseString = wxScan(eventReqDto);
				} else if ("unsubscribe".equalsIgnoreCase(event)) { // 取消关注
					logger.info("匹配取消关注事件...openid:"+eventReqDto.getFromUserName());
					wxUnsubscribe(eventReqDto,request);
					/* 更新表cmwa_wx_user_info用户的关注状态 */
					UserInfoManager userInfoManager = (UserInfoManager) SpringContextUtil.getBean("userInfoManager");
					userInfoManager.saveOrUpdateCmwaWxUserInfo(eventReqDto, "0");
				} else if ("CLICK".equalsIgnoreCase(event)) {
					logger.info("匹配点击按钮事件...");
					responseString =  wxClickMenu(eventReqDto);// eventMsgHandler(eventReqDto);
				}
			} else {//微信普通消息
				TextReqMsgDto dto = new TextReqMsgDto();
				BeanUtils.copyProperties(wxMsg, dto);
				responseString = wxReply(dto);
			}
		} catch(Exception e) {
			logger.error("微信事件处理异常",e);
		}
		logger.info("...end微信事件处理，返回responseString=" + responseString);
		return responseString;
	}
	
	@Override
	public boolean bind(Map<String, String> params) {
		//----推送绑定成功图文消息   start----
		String serviceMessage = ParameterCache.getValue(WXConstants.PMST_MESSAGE, WXConstants.PMKY_BIND_ACC, WXConstants.PMCO_BIND_SERVICEMESSAGE);
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
			logger.error("发送socket请求发送模板消息，抛出异常，resStr:"+resStr,e);
		}
		return bool;
	}

	@Override
	public boolean register(Map<String, String> params) {
		return false;
	}

	@Override
	public boolean authenticate(Map<String, String> params) {
		return false;
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
		
		key = WXConstants.PMST_MESSAGE + "#" + WXConstants.PMKY_PUBLIC + "#"+WXConstants.PMCO_SERVICWELCOME;
		ParameterDto paramDto = (ParameterDto) ParameterCache.getData().get(key);
		serviceMessage =  paramDto.getPmv1();
		serviceMessage = serviceMessage.replace("{openid}", openid);
		serviceMessage = serviceMessage.replace("{weiXinUserName}", weixinUserName);
		serviceMessage = serviceMessage.replace("{timestamp}", ""+(new Date()).getTime()/1000);
		
		try {
			//将eventKey插入到 cmf_weixin_activity_channel
			if(!"".equals(eventKey) && eventKey!=null){//访问渠道统计
				logger.info(" 未关注用户：回复关注事件 ,记录eeeeeventKey, eventKey="+eventKey+" 。");
				userInfoManager.insertUserChannel(openid, eventKey,"0"); 
			}
		} catch (Exception e) {
			logger.error(" 未关注用户：回复关注事件 ,记录eeeeeventKey出错 eventKey="+eventKey+" 。", e);
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
		
		key = WXConstants.PMST_MESSAGE + "#" + WXConstants.PMKY_PUBLIC + "#"+WXConstants.PMCO_SERVICWELCOME;
		ParameterDto paramDto = (ParameterDto) ParameterCache.getData().get(key);
		serviceMessage =  paramDto.getPmv1();
		serviceMessage = serviceMessage.replace("{openid}", openid);
		serviceMessage = serviceMessage.replace("{weiXinUserName}", weixinUserName);
		serviceMessage = serviceMessage.replace("{timestamp}", ""+(new Date()).getTime()/1000);
		
		try {
			//将eventKey插入到 cmf_weixin_activity_channel
			if(!"".equals(eventKey) && eventKey!=null){//访问渠道统计
				logger.info(" 已关注用户：回复关注事件 ,记录eeeeeventKey, eventKey="+eventKey+" 。");
				UserInfoManager userInfoManager=(UserInfoManager) SpringContextUtil.getBean("userInfoManager");
				userInfoManager.insertUserChannel(openid, eventKey,"1"); 
				/*20180514微信关注更新新表*/
				userInfoManager.saveOrUpdateCmwaWxUserInfo(eventReqDto, "1");
			}
		} catch (Exception e) {
			logger.error(" 已关注用户：回复关注事件 ,记录eeeeeventKey出错 eventKey="+eventKey+" 。",e);
		}
		
		logger.info("普通关注：回复关注事件，处理后的pmv1："+serviceMessage);
		
		return serviceMessage;
	}

	@Override
	public String wxUnsubscribe(EventReqMsgDto eventReqDto,HttpServletRequest request) throws Exception {
		//取消关注后 将用户绑定状态改为“C”
		UserInfoexManager userInfoexManager = (UserInfoexManager) SpringContextUtil.getBean("userInfoexManager");
		userInfoexManager.subscribleOrUnSubUpdateUserState(eventReqDto.getFromUserName(), "", "C");
		request.getSession().removeAttribute(SessionValue.SESSION_USERBASEINFO);
		request.getSession(true).invalidate();
		return "";
	}

	@Override
	public String wxClickMenu(EventReqMsgDto eventReqDto) throws Exception {
		String responseString = null;		
		logger.info("匹配自定义事件开始...");
		String eventKey = eventReqDto.getEventKey();
		logger.info("eventKey: " + eventKey);
		
		Map<String, Object> map = ParameterCache.judgeMenu(eventKey);
		ParameterDto paramDto = (ParameterDto)map.get("paramDto");
		int type = Integer.parseInt(map.get("type").toString());
		if(type >= 4) {
			if(isBind(paramDto, eventReqDto)){//已绑定 ，或者不需要验证绑定
				switch(type)
				{
					case 1:
						// 静态文本
						String pmv1 = paramDto.getPmv1();
						TextResMsgDto textResDto = new TextResMsgDto(eventReqDto, pmv1);
						responseString = textResDto.toDocument().getRootElement().asXML();
						logger.info("匹配静态文本回复：" + responseString);
						break;
						
					case 2:
						// 静态图文
						NewsResMsgDto newsResMsgDto = new NewsResMsgDto(eventReqDto);
						responseString =  newsResMsgDto.toDocument(paramDto.getPmv1()).getRootElement().asXML();
						logger.info("匹配静态图文消息回复：" + responseString);
						break;
						
					case 3:
						// 菜单click操作处理
						responseString = getEventRes(eventReqDto,paramDto);
						logger.info("匹配操作回复：" + responseString);
						break;
					case 4:
						// 多客服
						responseString = getCustServiceEventRes(eventReqDto);
						logger.info("菜单事件接入，匹配操作回复(多客服)：" + responseString);
						break;
					case 5:
						//上报地理位置事件
						logger.info("上报地理位置事件：未开发" );
						break;
					case 6:
						//点击菜单跳转链接事件
						logger.info("上报地理位置事件：未开发" );
						break;
					case 7:
						//无匹配
						logger.info("自定义事件匹配： 无匹配！" );
						break;
				}
				logger.info("匹配自定义事件结束");
				
			} else{//已验证 ，未绑定
				responseString = getNoBindRes(eventReqDto.getFromUserName(), eventReqDto);
			}
		} else {
			logger.warn("匹配type异常，type=" + type);
		}
		
		return responseString; 
	}

	@Override
	public String wxReply(TextReqMsgDto eventReqDto) throws Exception {
		String msgType = eventReqDto.getMsgType();
		logger.info("微信普通消息处理...");
		String responseString = null;
		if(WXConstants.MSGTYPE_REQUEST_TEXT.equals(msgType)) {//文本消息请求
			logger.info("文本消息请求...");
			responseString = textMsgHandler(eventReqDto);
		}else if(WXConstants.MSGTYPE_REQUEST_IMAGE.equals(msgType)){//图片
			logger.info("请求消息类型【图片】...");
		}else if(WXConstants.MSGTYPE_REQUEST_VOICE.equals(msgType)){//音频
			logger.info("请求消息类型【语音】...");
		}else if(WXConstants.MSGTYPE_REQUEST_VIDEO.equals(msgType)){//视频
			logger.info("请求消息类型【视频】...");
		}else if(WXConstants.MSGTYPE_REQUEST_SHORTVIDEO.equals(msgType)){//小视频
			logger.info("请求消息类型【小视频】...");
		}else if(WXConstants.MSGTYPE_REQUEST_LOCATION.equals(msgType)){//地址位置
			logger.info("请求消息类型【地址位置】...");
		}else if(WXConstants.MSGTYPE_REQUEST_LINK.equals(msgType)){//链接
			logger.info("请求消息类型【链接】...");
		}else{
			logger.info("请求消息类型【未知】...");
		}
		return responseString;
	}

	
	
	
	
	
	
	/**
	 * 微信文本消息处理
	 * @return
	 * @throws Exception
	 */
	private String textMsgHandler(BaseMsgDto wxMsg) throws Exception {
		String responseString = null;
		
		TextReqMsgDto textReqDto = (TextReqMsgDto) wxMsg;
		String content = textReqDto.getContent();
		
		Map<String, Object> map = ParameterCache.judgeText(content);
		int type = Integer.parseInt(map.get("type").toString());
		ParameterDto paramDto = (ParameterDto)map.get("paramDto");
		
		if(isBind(paramDto, wxMsg)){//已绑定 ，或者不需要验证绑定
			if(type==1){
				// 静态回复
				TextResMsgDto resDto = new TextResMsgDto(textReqDto, paramDto.getPmv1());
				responseString = resDto.toDocument().getRootElement().asXML();
				logger.info("匹配静态文本回复：" + responseString);
			}else if(type==2){
				// 多客服
				responseString = getCustServiceEventRes(wxMsg);
				logger.info("文本接入多客服，匹配操作回复：" + responseString);
			}else if(type == 10){
				//回复之后推送服务
				String openid = wxMsg.getFromUserName();
				String weixinUserName = WeixinUtil.getWeiXinUserName();
				responseString =  paramDto.getPmv1();
				responseString = responseString.replace("{openid}", openid);
				responseString = responseString.replace("{weiXinUserName}", weixinUserName);
				responseString = responseString.replace("{timestamp}", ""+(new Date()).getTime()/1000);
				logger.info("回复图文消息，匹配操作回复："+responseString);
			}else{
				//无匹配
				logger.info("文本消息请求，--->无匹配！");
			}
		}else{
			responseString = getNoBindRes(wxMsg.getFromUserName(), wxMsg);
		}
		
		return responseString; 
	}

	private boolean isBind(ParameterDto paramDto, BaseMsgDto wxMsg){
		boolean bool = true;
		
		String yesOrNo = null;
		if(null!=paramDto){
			yesOrNo = paramDto.getPmv3();
		}
		logger.info("是否需要验证绑定 -- pmv3:"+yesOrNo);
		if(("YES").equals(yesOrNo)){//需要验证是否绑定
			
			String openId = wxMsg.getFromUserName();
			UserInfoexManager userInfoexManager=(UserInfoexManager)SpringContextUtil.getBean("userInfoexManager");
			// 查找是否已经存在绑定关系
			UserInfoexDto userInfoexdto = userInfoexManager.queryUserinfoexQueryRelation(openId,WXConstants.USERINFOEX_BINDSTAT_R);
			if(userInfoexdto==null){
				bool = false;
			}
		}
		return bool;
	}
	
	private String getCustServiceEventRes(BaseMsgDto wxMsg) throws Exception {
		//如果在工作时间
		if(DateUtils.isWorkTime()){
			BaseMsgDto resDto=new BaseMsgDto(wxMsg);
			resDto.setCreateTime(resDto.getCreateTime()/1000);
			logger.info("toUserName:"+resDto.getToUserName()+";fromUserName:"+resDto.getFromUserName());
			resDto.setMsgType("transfer_customer_service");
			return resDto.toDocument().getRootElement().asXML();			
        //如果在非工作时间	
		}else{
			String answer = ParameterCache.getValue(WXConstants.PMST_MENU, WXConstants.PMKY_CUST_SERVICE,"line_service");
			TextResMsgDto resDto = new TextResMsgDto(wxMsg, answer);
			return resDto.toDocument().getRootElement().asXML();
		}
	}
	
	private String getEventRes(EventReqMsgDto eventReqDto,ParameterDto paramDto) throws Exception {
		String operCode = paramDto.getPmv1();
		//得到事件处理类的包名加类名
		String eventObjectName = paramDto.getPmv4();
		logger.info("operCode:" + operCode+"，eventObjectName:" + eventObjectName);
		String resText = EventFactory.create(eventObjectName).getRes(eventReqDto, paramDto);
		return resText;
	}

	private String getNoBindRes(String weixinId, BaseMsgDto wxMsg){
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
		
		TextResMsgDto textRes = new TextResMsgDto(wxMsg, answer);
		try {
			answer = textRes.toDocument().asXML();
		} catch (Exception e) {
			logger.error("没有绑定查询默认回复异常：",e);
		}
		return answer;
	}
	
}
