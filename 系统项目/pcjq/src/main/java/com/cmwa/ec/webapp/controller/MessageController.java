package com.cmwa.ec.webapp.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.webapp.manager.MessageManager;
import com.cmwa.ec.webapp.util.ContextUtils;
import com.cmwa.ec.webapp.util.ECConstants;
import com.cmwa.ec.webapp.util.RequestHelper;

@Controller("MessageController")
@RequestMapping(value = "/AppService")
public class MessageController {
	
	private static Logger logger = Logger.getLogger(MessageController.class.getName());
	
	@Autowired
	private MessageManager messageManager;
	
	/***
	 * 获取手机短信验证码   在线支付的时候验证银行预留手机号码
	 * 将手机号码保存在session中
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	@RequestMapping(value = "/business/getVerifyCodeForTrade.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String getVerifyCodeForTrade(HttpServletResponse response,HttpServletRequest request) throws UnsupportedEncodingException, IOException  {

		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		//手机号码
		String money = request.getParameter("money");// 支付金额
		String mobile = request.getParameter("mobile");//银行预留手机号码
		String bankcardNo = request.getParameter("bankcardNo");
		String bankName = request.getParameter("bankName");
		String bnsType = request.getParameter("bnsType");
		
		if(money != null && !money.equals("")){
			money = URLDecoder.decode(money, "UTF-8");
		}
		if(bankName != null && !bankName.equals("")){
			bankName = URLDecoder.decode(bankName, "UTF-8");
		}
		
		JSONObject returnJsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		String sessionID = "";

		logger.info("MessageController类【getVerifyCodeForTrade】交易发送手机验证码开始>>>cmfUserId:"+cmfUserId+">>>mobile:"+mobile
				+">>>money="+money+">>>bankcardNo="+bankcardNo+">>>bankName="+bankName+">>>bnsType="+bnsType
				+">>>timestamp:"+System.currentTimeMillis());

		//日志信息
		Context context=ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
	  		
    	MsgServiceMessageDto msgServicedto = messageManager.applyVrfCode(context, mobile, bnsType, bankcardNo, money, bankName, seqId);
    	if(msgServicedto != null){
			returnCode=msgServicedto.getReturnCode();
			returnMsg = msgServicedto.getReturnMsg();
		 
			if(ECConstants.COMMON_SUCCESS.equals(returnCode)){
				sessionID = msgServicedto.getSessionID();
				request.getSession(true).setAttribute("mobileByMsg", mobile);
			}
			logger.info("MessageController类【getVerifyCodeForTrade】交易发送手机验证码>>>returnCode:"+returnCode+">>>returnMsg:"+returnMsg
					+">>>sessionID:"+sessionID+">>>timestamp:"+System.currentTimeMillis());
		}
    	returnJsonObject.put("returnCode",returnCode);
    	returnJsonObject.put("returnMsg",returnMsg);
    	returnJsonObject.put("sessionID",sessionID);
    	
    	logger.info("MessageController类【getVerifyCodeForTrade】交易发送手机验证码结束>>>returnJsonObject:"+returnJsonObject+">>>timestamp:"+System.currentTimeMillis());
    	
		return returnJsonObject.toString();
	 
	}
}
