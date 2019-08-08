package com.cmwa.ec.webapp.manager.impl;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.dto.MsgParameterDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.dto.mail.MailMessage;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.webapp.client.MessageServiceClient;
import com.cmwa.ec.webapp.client.UserServiceClient;
import com.cmwa.ec.webapp.manager.MessageManager;
import com.cmwa.ec.webapp.util.ECConstants;
import com.cmwa.ec.webapp.util.RequestHelper;
import com.cmwa.ec.webapp.util.SessionValue;

public class MessageManagerImpl implements MessageManager{
	private static Logger logger = Logger.getLogger(MessageManagerImpl.class.getName());
	@Autowired
	private MessageServiceClient messageServiceClient;
	
	@Autowired
	private UserServiceClient userServiceClient;
	
	@Override
	public MsgServiceMessageDto applyVrfCode(Context context,String mobile, String bnsType,
			String carMantissa, String amount, String bankName, String ip) {
		try {
			MsgServiceMessageDto msgServiceMessageDto = messageServiceClient.applyVrfCode(context, mobile, bnsType, carMantissa, amount, bankName);
			return msgServiceMessageDto;
		} catch (Exception e) {
			logger.error("发送验证短信异常", e);
		}
		return null;
	}

	/**
	 * @param Context 日志信息
	 * @param sessionId 与验证码申请关联的sessionID.
	 * @param mobile 手机号.
	 * @param vrfCode 用户填入的验证码.
	 * @return ReturnMsg对象,包括验证码申请成功与否,已经与返回码关联的描述.
		返回码
		0000  成功
	 */
	@Override
	public MsgServiceMessageDto checkVrfCode(Context context,String sessionID, String mobile,String rvrfcode) {
		MsgServiceMessageDto msgServiceMessageDto = messageServiceClient.checkVrfCode(context, sessionID, mobile, rvrfcode);
		return msgServiceMessageDto;
	}

	/**
	 * 注册页面验证短信验证码
	 * 
	 * @param context
	 * @param sessionID
	 * @param obj
	 * @param rvrfcode
	 * @param inputMobile
	 * @return
	 */
	@Override
	public JSONObject checkVrfCodeResgist(Context context, String sessionID, String mobile, String rvrfcode, String inputMobile) {
		logger.info("MessageManagerImpl类【checkVrfCodeResgist】开始>>>sessionID:" + sessionID + ">>>mobile:" + mobile + ">>>rvrfcode:" + rvrfcode + ">>>inputMobile:" + inputMobile + ">>>timestamp:" + System.currentTimeMillis());
		JSONObject returnJsonObject = new JSONObject();
		if (mobile == null || "".equals(mobile) || !mobile.equals(inputMobile)) {
			// 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_9002);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_9002);
			return returnJsonObject;
		}
		MsgServiceMessageDto msgServiceMessageDto = messageServiceClient.checkVrfCode(context, sessionID, mobile, rvrfcode);
		returnJsonObject.put("returnCode", msgServiceMessageDto.getReturnCode());
		returnJsonObject.put("returnMsg", msgServiceMessageDto.getReturnMsg());
		logger.info("MessageManagerImpl类【checkVrfCodeResgist】开始>>>returnJsonObject:" + returnJsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
		return returnJsonObject;
	}
	
	@Override
	public JSONObject checkMobileVrfCode(Context context, String sessionID, String mobile, String rvrfcode) {
		logger.info("MessageManagerImpl类【checkVrfCodeResgist】开始>>>sessionID:" + sessionID + ">>>mobile:" + mobile + ">>>rvrfcode:" + rvrfcode + ">>>timestamp:" + System.currentTimeMillis());
		JSONObject returnJsonObject = new JSONObject();
		if (mobile == null || "".equals(mobile)) {
			// 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_9002);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_9002);
			return returnJsonObject;
		}
		MsgServiceMessageDto msgServiceMessageDto = messageServiceClient.checkVrfCode(context, sessionID, mobile, rvrfcode);
		returnJsonObject.put("returnCode", msgServiceMessageDto.getReturnCode());
		returnJsonObject.put("returnMsg", msgServiceMessageDto.getReturnMsg());
		
		logger.info("MessageManagerImpl类【checkVrfCodeResgist】开始>>>returnJsonObject:" + returnJsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
		return returnJsonObject;
	}

	@Override
	public JSONObject checkVrfCodeByModifyMobile(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		String mobile = request.getParameter("mobile");
		String vrfCode = request.getParameter("vrfCode");
		String sessionID = request.getParameter("sessionID");
		
		String userMobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_USERMOBILE);
		UserBaseInfoDto userBaseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
		
		String regMobile = "";
		if(userBaseInfoDto != null){
			regMobile = userBaseInfoDto.getMobile();
		}
		String hasCheckSetLPassword = (String) request.getSession(true).getAttribute(SessionValue.SESSION_HASCHECKSETLPASSWORD);
		
		String lPassword = request.getParameter("sessionID");
		String operatorType = "1";// 1：修改手机号码未被使用，直接修改手机号码	
		if(hasCheckSetLPassword != null && hasCheckSetLPassword.equals("Y")){
			operatorType = "2";// 2：修改的手机号码已被注册，但未鉴权，修改手机号码且修改登录密码
		}
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);

		logger.info("MessageManagerImpl类【checkVrfCodeByModifyMobile】开始>>>cmfUserId:" + cmfUserId + ">>>mobile:" + mobile + ">>>vrfCode:" + vrfCode + ">>>sessionID:" + sessionID
				 + ">>>mobile:" + mobile + ">>>hasCheckSetLPassword:" + hasCheckSetLPassword + ">>>operatorType:" + operatorType + ">>>timestamp:" + System.currentTimeMillis());
		
		if(mobile == null || mobile.equals("") || vrfCode == null || vrfCode.equals("") || sessionID == null || sessionID.equals("")){
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_9000);
			jsonObject.put("returnMsg", ECConstants.RETURN_MSG_9000);
			return jsonObject;
		}else if(userMobile == null || !userMobile.equals(mobile)){
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_9002);
			jsonObject.put("returnMsg", ECConstants.RETURN_MSG_9002);
			return jsonObject;
		}else if(userMobile.equals(regMobile)){
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_9003);
			jsonObject.put("returnMsg", "和原手机号码一致");
			return jsonObject;
		}
		MsgServiceMessageDto msgServiceMessageDto = messageServiceClient.checkVrfCode(context, sessionID, mobile, vrfCode);
		if(msgServiceMessageDto != null){
			returnCode = msgServiceMessageDto.getReturnCode();
			returnMsg = msgServiceMessageDto.getReturnMsg();
		}
		if (returnCode != null && returnCode.equals(ECConstants.RETURN_CODE_0000)) {
			jsonObject.put("hasCheckSetLPassword", hasCheckSetLPassword);
			request.getSession(true).removeAttribute(SessionValue.SESSION_CHECKEDTPASSWORD);
			request.getSession(true).removeAttribute(SessionValue.SESSION_USERMOBILE);
			request.getSession(true).removeAttribute(SessionValue.SESSION_HASCHECKSETLPASSWORD);
			
			
			if(returnCode != null && returnCode.equals("0000")){
				if(hasCheckSetLPassword != null && hasCheckSetLPassword.equals("Y")){// 要设置登录密码
					request.getSession(true).setAttribute(SessionValue.SESSION_CANSETLPASSWORD, "Y");
					request.getSession(true).setAttribute(SessionValue.SESSION_VERIFYMOBILE, mobile);
				}else{
					/********************修改手机号码 S***********************************/
					UserServiceMessage userServiceMessage = userServiceClient.modifyRegMobile(context, cmfUserId, mobile, lPassword, operatorType, ECConstants.USER_CHANEL_01);
					
					if(userServiceMessage != null){
						returnCode = userServiceMessage.getReturnCode();
						returnMsg = userServiceMessage.getReturnMsg();
						if(returnCode != null && returnCode.equals(ECConstants.RETURN_CODE_0000)){
							request.getSession(true).invalidate();// 修改手机号码成功不需要设置登录密码   直接注销
						}
					}
					/********************修改手机号码 E***********************************/
					
				}
			}
		}

		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("MessageManagerImpl类【checkVrfCodeByModifyMobile】结束>>>jsonObject:" + jsonObject.toString() + System.currentTimeMillis());
		
		return jsonObject;
	}
	@Override
	public JSONObject sendSmsMsg(Context context, MsgParameterDto msgParameter, MsgSmsRecordDto msgRecord) {
		
		String returnCode = "9999";
		String returnMsg = "系统异常";
		logger.info("MessageManagerImpl类【sendSmsMsg】开始>>>msgDto:"+msgParameter.toString()+">>>timestamp:"+System.currentTimeMillis());
		try {
			MsgServiceMessageDto msgServicedto = messageServiceClient.sendSmsMsg(context, msgParameter, msgRecord);
			if(msgServicedto!=null){
				returnCode = msgServicedto.getReturnCode();
				returnMsg = msgServicedto.getReturnMsg();
			}
		} catch (Exception e) {
			logger.error("MessageManagerImpl类【sendSmsMsg】异常", e);
		}
		logger.info("MessageManagerImpl类【sendSmsMsg】结束>>>returnCode:"+returnCode+">>>>returnMsg:"+returnMsg+">>>timestamp:"+System.currentTimeMillis());
		JSONObject returnJsonObject = new JSONObject();
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		return returnJsonObject;
		
	}
	
	
	
	/**
	 * 发送通知消息(没有任何变量参数)
	 * @param mobile 发送到的手机号码 
	 * @param msgType 消息编号
	 * @return	ReturnMsg对象,包括消息发送成功与否,已经与返回码关联的描述.
	 * <pre>
	 * 返回码
	 * 成功							0000
	 * 关键参数为空						USR-5001
	 * 未配置消息模板					USR-5103
	 * 操作数据库失败					USR-5205
	 * 系统错误						9999
	 * </pre>
	 */
	@Override
	public JSONObject sendTextMessage(Context context, MsgSmsRecordDto msgRecord) {
		
		JSONObject returnJsonObject = new JSONObject();
		String returnCode=null;
		//返回的信息
		String returnMsg=null;
		
		String mobile = msgRecord == null ? null : msgRecord.getMobile();
		String msgType = msgRecord == null ? null : msgRecord.getMsgType();
		
		logger.info("MessageManagerImpl类【sendTextMessage】开始>>>mobile:"+mobile+">>>msgType:"+msgType+">>>timestamp:"+System.currentTimeMillis());
		MsgServiceMessageDto msgServicedto = messageServiceClient.sendTextMessage(context, msgRecord);
		
		if(msgServicedto!=null){
			returnCode=msgServicedto.getReturnCode();
			returnMsg=msgServicedto.getReturnMsg();
		}
 
		logger.info("MessageManagerImpl类【sendTextMessage】结束>>>returnCode:"+returnCode+">>>>returnMsg:"+returnMsg+">>>timestamp:"+System.currentTimeMillis());
		
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		
		return returnJsonObject;
	}

	@Override
	public JSONObject sendMail(Context context, MailMessage mailMessage) {
		JSONObject returnJsonObject = new JSONObject();
		String returnCode=null;
		
		logger.info("MessageManagerImpl类【sendMail】开始>>>timestamp:"+System.currentTimeMillis());
		MsgServiceMessageDto msgServicedto = new MsgServiceMessageDto();
		try {
			msgServicedto = messageServiceClient.sendMail(context, mailMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(msgServicedto!=null){
			returnCode=msgServicedto.getResultCode();
		}
 
		logger.info("MessageManagerImpl类【sendMail】结束>>>timestamp:"+System.currentTimeMillis());
		
		returnJsonObject.put("returnCode", returnCode);
		
		return returnJsonObject;
	}
}
