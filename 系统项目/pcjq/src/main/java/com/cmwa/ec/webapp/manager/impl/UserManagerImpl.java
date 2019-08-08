package com.cmwa.ec.webapp.manager.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.user.facade.dto.CustInfoDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.user.facade.dto.UserTaxInfoDto;
import com.cmwa.ec.webapp.client.MessageServiceClient;
import com.cmwa.ec.webapp.client.UserServiceClient;
import com.cmwa.ec.webapp.common.category.CategoryDelegate;
import com.cmwa.ec.webapp.common.category.CategoryDto;
import com.cmwa.ec.webapp.controller.VerifyCodeController;
import com.cmwa.ec.webapp.manager.MessageManager;
import com.cmwa.ec.webapp.manager.UserManager;
import com.cmwa.ec.webapp.util.ContextUtils;
import com.cmwa.ec.webapp.util.ECConstants;
import com.cmwa.ec.webapp.util.GetIPUtils;
import com.cmwa.ec.webapp.util.MD5;
import com.cmwa.ec.webapp.util.RequestHelper;
import com.cmwa.ec.webapp.util.SessionValue;

public class UserManagerImpl implements UserManager{
	private static Logger logger = Logger.getLogger(UserManagerImpl.class.getName());
	@Autowired
	private UserServiceClient userServiceClient;
	@Autowired
	private MessageServiceClient messageServiceClient;
	@Autowired
	private MessageManager messageManager;
	@Override
	public UserServiceMessage queryUserAndAccoRlaById(Context context, String cmfUserId) {
		UserServiceMessage userServiceMessage = userServiceClient.queryUserAndAccoRlaById(context,cmfUserId);
		return userServiceMessage;
	}

	@Override
	public UserServiceMessage login(Context context, String loginNo, String lPassword, String loginType, String loginChannel, String loginMark) {
		UserServiceMessage userServiceMessage = userServiceClient.login(context, loginNo, lPassword, loginType, loginChannel, loginMark);
		return userServiceMessage;
	}

	@Override
	public UserServiceMessage manageTpassword(Context context, String cmfUserId, String tpassword, String oldTpassword, String manageType, String tradeChannel, String tradeMark) {
		UserServiceMessage userServiceMessage = userServiceClient.manageTpassword(context, cmfUserId, tpassword, oldTpassword, manageType, tradeChannel, tradeMark);
		return userServiceMessage;
	}
	/*==================================以上是1.0版本，以后会删掉========================================*/
	// TODO 以上是1.0版本，以后会删掉
	@Override
	public JSONObject registerNormalUserWithT(HttpServletRequest request, Context context) {
		String mobile = request.getParameter("mobile");
		String passWord = request.getParameter("passWord");
		String sessionID = request.getParameter("sessionID");
		String smsCode = request.getParameter("smsCode");
		logger.info("UserManagerImpl类【registerNormalUserWithT】开始>>>mobile:"+mobile+">>>>>smsCode:"+smsCode+">timestamp:" + System.currentTimeMillis());
		JSONObject returnJsonObject = new JSONObject();
		// 验证发送手机短信验证码是保存在session中的手机号码
		String smsMobile = (String) request.getSession(true).getAttribute(ECConstants.SESSION_USERMOBILE);
		if (smsMobile == null) {
			returnJsonObject.put("returnCode", "9998");// 未获取到发送验证码时，session中保存的手机号码
			return returnJsonObject;
		} else if (!smsMobile.equals(mobile)) {
			returnJsonObject.put("returnCode", "9997");// 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致
			return returnJsonObject;
		}
		// 验证短信验证码
		MsgServiceMessageDto msgServiceMessageDto = messageManager.checkVrfCode(context, sessionID, mobile, smsCode);
		if (msgServiceMessageDto != null) {
			if (msgServiceMessageDto.getReturnCode() == null || !msgServiceMessageDto.getReturnCode().equals("0000")) {
				if(msgServiceMessageDto.getReturnCode().equals("USR-5202")){
					returnJsonObject.put("returnCode", "9995");// 错误次数过多
					return returnJsonObject;
				}else{
					returnJsonObject.put("returnCode", "9996");// 手机短信验证码验证失败
					return returnJsonObject;
				}
			}
		}
		MD5 md5 = new MD5();
		if (passWord != null) {
			passWord = md5.getMD5ofStr(passWord);
		}
		// 注册成功 0000
		// 待绑定的手机号码已被使用 USR-A015
		// 参数错误 USR-B003
		UserServiceMessage userServiceMsg = userServiceClient.registerNormalUserWithT(context, mobile, "1", passWord, ECConstants.LOGIN_CHANEL_01, ECConstants.LOGIN_NMARK_01);
		String returnCode = "";
		// 返回的信息
		String returnMsg = "";
		String cmfUserId = "";
		if (userServiceMsg != null) {
			returnCode = userServiceMsg.getReturnCode();
			if (ECConstants.COMMON_SUCCESS.equals(returnCode)) {
				// 更新session中的用户信息
				UserBaseInfoDto userBaseInfo = userServiceMsg.getUserBaseInfoDto();
				if (userBaseInfo != null) {
					cmfUserId = userBaseInfo.getCmfUserId();
					request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
					request.getSession(true).setAttribute(SessionValue.SESSION_CMFUSERID, cmfUserId);
				} else {
					returnCode = "9999";
				}
			} else {
				returnMsg = userServiceMsg.getReturnMsg();
			}
		}
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		returnJsonObject.put("temp", cmfUserId);
		logger.info("UserManagerImpl类【registerNormalUserWithT】结束>>>returnJsonObject:" +returnJsonObject+ ">>>timestamp:" + System.currentTimeMillis());
		return returnJsonObject;
	}
	
	@Override
	public JSONObject verifyMobile(Context context,String mobile) {
		JSONObject jsonObject = new JSONObject();
		logger.info("UserManagerImpl类【verifyMobile】开始>>>mobile:" +mobile+ ">>>timestamp:" + System.currentTimeMillis());
		UserServiceMessage userServiceMessage = userServiceClient.verifyMobile(context,mobile);
		if(userServiceMessage!=null){
			jsonObject.put("returnCode", userServiceMessage.getReturnCode());
			jsonObject.put("returnMsg", userServiceMessage.getReturnMsg());
		}
		logger.info("UserManagerImpl类【verifyMobile】结束>>>jsonObject：" +jsonObject+ ">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}

	@Override
	public JSONObject checkVrfCodeAndMobile(Context context, HttpServletRequest request, String mobile, String rvrfcode) {
		logger.info("UserManagerImpl类【checkVrfCodeAndMobile】结束>>>mobile：" + mobile + ">>>rvrfcode" + rvrfcode + ">>>>>timestamp:" + System.currentTimeMillis());
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		Map<String, String> map = VerifyCodeController.verifyRandom(request, rvrfcode);
		//仅记录验证结果
		if(null != map && map.containsKey("returnCode")){
			request.getSession(true).setAttribute(SessionValue.SESSION_RESETLPWVERFLAG, map.get("returnCode"));
		}else{
			request.getSession(true).setAttribute(SessionValue.SESSION_RESETLPWVERFLAG, ECConstants.RETURN_CODE_9999);
		}
		if(map == null || !map.get("returnCode").equals(ECConstants.RETURN_CODE_0000)){
			returnCode = map.get("returnCode");
			returnMsg = map.get("returnMsg");

			jsonObject.put("returnCode", returnCode);
			jsonObject.put("returnMsg", returnMsg);
			return jsonObject;
		}
		
		/**
		 * 可用 0000<br>
		 * 注册用户 USR-A000<br>
		 * 已占用 USR-A017<br>
		 * 系统运行时不可知异常 USR-8000<br>
		 * mobile必填 USR-B004
		 */
		UserServiceMessage userServiceMessage = userServiceClient.verifyMobile(context, mobile);
		if (userServiceMessage != null) {
			returnCode = userServiceMessage.getReturnCode();
			returnMsg = userServiceMessage.getReturnMsg();
		}
		// 已注册用户  验证图片验证码后将手机号码保存在session中
		if(returnCode != null && (returnCode.equals("USR-A000") || returnCode.equals("USR-A017"))){
			request.getSession(true).setAttribute(SessionValue.SESSION_MSGMOBILE, mobile);
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("UserManagerImpl类【checkVrfCodeAndMobile】结束>>>jsonObject：" +jsonObject+ ">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;

	}
	/**
	 * 验证证件-银行卡鉴权页面
	 */
	@Override
	public JSONObject checkIdNoByBankAuthentication(Context context,HttpServletRequest request) {
		JSONObject json = new JSONObject();
		// 证件类型
		String idtp = request.getParameter("idtp");
		// 证件号码
		String idno = request.getParameter("idno");
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String authmobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_AUTHMOBILE);
		String inputMobile = request.getParameter("mobile");
		logger.info("UserManagerImpl类【checkIdNoByBankAuthentication】开始>>>idtp：" + idtp + ">>>idno：" + idno + ">>>cmfUserId：" + cmfUserId + ">>>authmobile：" + authmobile + ">>>inputMobile：" + inputMobile + ">>>timestamp:" + System.currentTimeMillis());
		if (authmobile != null) {
			if (!authmobile.equals(inputMobile)) {
				// 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致
				json.put("returnCode", ECConstants.RETURN_CODE_9002);
				json.put("returnMsg", ECConstants.RETURN_MSG_9002);
				return json;
			}
		} else {
			// 用户没有获取验证码，非法请求验证验证码
			json.put("returnCode", ECConstants.RETURN_CODE_9003);
			json.put("returnMsg", ECConstants.RETURN_MSG_9003);
			return json;
		}
		if ("".equals(cmfUserId)) {
			json.put("returnCode", ECConstants.RETURN_CODE_8000);
			json.put("returnMsg", ECConstants.RETURN_MSG_8000);
			return json;
		}
		idno = idno != null ? idno.toUpperCase() : idno;
		String returnCode = "";
		String returnMsg = "";
		UserServiceMessage userServiceMsg = userServiceClient.identityCerification(context, idtp, idno);
		if (userServiceMsg != null) {
			returnCode = userServiceMsg.getReturnCode();
			returnMsg = userServiceMsg.getReturnMsg();
			UserBaseInfoDto userInfo = userServiceMsg.getUserBaseInfoDto();
			/**
			 * * 用户已注册 0000 代销/直销/母公司系统注册，本交易系统未注册 USR-A001 用户未注册 USR-A002 参数错误
			 * idNo/idType必填 USR-B002 系统运行时不可知异常 USR-8000
			 */
			// 当返回 0000 时，判断返回的userBaseInfo是否是当前用户自己的信息
			// 如果cmfUserId比较相等的话，则验证通过 设置returnCode 为USR-A003
			// 反之，证件号码已被注册
			if ("0000".equals(returnCode)) {
				logger.info("UserManagerImpl类【checkIdNoByBankAuthentication】---userInfo:" + userInfo != null ? userInfo.toString() : null);
				if (userInfo != null) {
					if (userInfo.getCmfUserId().equals(cmfUserId)) {
						returnCode = "USR-A003";
					}
				}
			}
		} else {
			returnCode = ECConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = ECConstants.COMMON_ERROR_SYSERRMSG;
		}
		json.put("returnCode", returnCode);
		json.put("returnMsg", returnMsg);
		logger.info("UserManagerImpl类【checkIdNoByBankAuthentication】结束>>>json:" + json + ">>>timestamp:" + System.currentTimeMillis());
		return json;
	}

	/**
	 * 获取验证码,不到账户服务验证手机号码
	 * </br>
	 * 验证手机号码的业务类型 0、注册 1、交易 2、绑定 3、账户手机验证 4、修改密码 5、银行卡号，身份证，手机号，姓名 鉴权（开户）
	 * @param context
	 * @param mobile
	 * @param type  获取验证码类型 
	 * @param bankNumber	银行卡号后四位
	 * @param bankName	银行名称
	 * @param ip	ip地址
	 * @param seqId	sessionId
	 * @return json
	 * @author liury
	 * </br>
	 * date:2015-02-05
	 */
	public JSONObject getVerifyCode(Context context, String mobile, String type, String bankNumber, String money, String bankName) {
		JSONObject returnJsonObject = new JSONObject();
		logger.info("UserManagerImpl类【getVerifyCode】结束>>>mobile:" + mobile + ">>>type:" + type + ">>>bankNumber:" + bankNumber + ">>>money:" + money + ">>>bankName:" + bankName + ">>>timestamp:" + System.currentTimeMillis());
		String returnCode = "";
		// 返回的信息
		String returnMsg = "";
		// 申请成功后返回的字符串
		String sessionID = "";
		MsgServiceMessageDto msgServicedto = null;
		try {
			msgServicedto = messageServiceClient.applyVrfCode(context, mobile, type, bankNumber, money, bankName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (msgServicedto != null) {
			returnCode = msgServicedto.getReturnCode();
			returnMsg = msgServicedto.getReturnMsg();
			if (ECConstants.COMMON_SUCCESS.equals(returnCode)) {
				sessionID = msgServicedto.getSessionID();
			}
			logger.info("UserManagerImpl类【getVerifyCode】获取验证码,不到账户服务验证手机号码，mobile:" + mobile + ">>>returnCode:" + returnCode + ">>>returnMsg:" + returnMsg + ">>>sessionID:" + sessionID + ">>>timestamp:" + System.currentTimeMillis());
		}

		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		returnJsonObject.put("sessionID", sessionID);

		if (type.equals("1")) {
			logger.info("UserManagerImpl类【getVerifyCode】信息鉴权获取手机短信验证码：" + returnJsonObject.toString());
		} else if (type.equals("5")) {
			logger.info("UserManagerImpl类【getVerifyCode】交易支付获取手机短信验证码：" + returnJsonObject.toString());
		} else if (type.equals("4")) {
			logger.info("UserManagerImpl类【getVerifyCode】找回密码获取手机短信验证码：" + returnJsonObject.toString());
		}
		logger.info("UserManagerImpl类【getVerifyCode】结束returnJsonObject：" + returnJsonObject.toString()+ ">>>timestamp:" + System.currentTimeMillis());
		return returnJsonObject;
	}
	/**
	 * 验证手机号码和返回手机验证码（新） 注册
	 */
	@Override
	public JSONObject getMobileVerifyCode(Context context, HttpServletRequest request) {
		String cmfId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		String inputMobile=request.getParameter("mobile");
		logger.info("UserManagerImpl类【getMobileVerifyCode】开始>>>cmfId:" + cmfId + ">>>seqId:" + seqId + ">>>inputMobile:" + inputMobile + ">>>timestamp:" + System.currentTimeMillis());
		JSONObject returnJsonObject = new JSONObject();
		// 手机号码，获取session中的验证图片验证码时候存放到session中的手机号码
		String mobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_MSGMOBILE);
		String bnsType = request.getParameter("bnsType");
		JSONObject returnJson = verifyMobile(context, mobile);
		//如果图片验证码验证通过后保存的手机号和输入的手机号一致,进行后续操作，否则，返回错误信息
		if(inputMobile.equals(mobile)){
			String returnCode = returnJson.getString("returnCode");
			// 返回的信息
			String returnMsg = returnJson.getString("returnMsg");
			// 申请成功后返回的字符串
			String sessionID = "nullId";

			logger.info("UserManagerImpl类【getMobileVerifyCode】验证手机号码>>>returnCode:" + returnCode + ">>>timestamp:" + System.currentTimeMillis());
			if (ECConstants.COMMON_SUCCESS.equals(returnCode)) {
				// 日志信息
				Context contexts = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, cmfId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
				MsgServiceMessageDto msgServicedto = messageManager.applyVrfCode(contexts, mobile, bnsType, "", "", "", GetIPUtils.getIpAddr(request));
				if (msgServicedto != null) {
					returnCode = msgServicedto.getReturnCode();
					if (ECConstants.COMMON_SUCCESS.equals(returnCode)) {
						sessionID = msgServicedto.getSessionID();
						request.getSession(true).setAttribute(ECConstants.SESSION_USERMOBILE, mobile);
					}
					returnMsg = msgServicedto.getReturnMsg();
					logger.info("UserManagerImpl类【getMobileVerifyCode】手机验证通过>>>returnCode:" + returnCode + ">>>returnMsg:" + returnMsg + ">>>sessionID:" + sessionID + ">>>timestamp:" + System.currentTimeMillis());
				}
			} else if (ECConstants.CODE_MOBILE_USE.equals(returnCode)) {// 返回手机已使用信息
				returnMsg = ECConstants.MSG_MOBILE_USE;
			} else {
				returnMsg = ECConstants.MSG_MOBILE_ERROR;
			}
			returnJsonObject.put("returnCode", returnCode);
			returnJsonObject.put("returnMsg", returnMsg);
			returnJsonObject.put("sessionID", sessionID);
		}else{
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_9002);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_9002);
		}
		logger.info("UserManagerImpl类【getMobileVerifyCode】结束>>>returnJsonObject:" + returnJsonObject + ">>>timestamp:" + System.currentTimeMillis());
		return returnJsonObject;
	}
	/**
	 * 获取短信验证码-找回登录密码 需要验证图片验证码
	 */
	@Override
	public JSONObject getMsgByPsw(Context context, HttpServletRequest request) {

		String inputMobile = "";
		inputMobile = request.getParameter("mobile");
		//
		if(StringUtils.isEmpty(inputMobile) || !StringUtils.isNumeric(inputMobile.trim())){
			UserBaseInfoDto userBaseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
			if(null != userBaseInfoDto){
				inputMobile = userBaseInfoDto.getMobile();
			}
		}
		
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		// 手机号码，获取session中的验证图片验证码时候存放到session中的手机号码
		String mobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_MSGMOBILE);
		String bnsType = request.getParameter("bnsType");
		//配置的间隔时间
		Integer resetLPWGapTime = Integer.parseInt(SpringUtil.getProperty("resetLPWGapTime"));
		logger.info("UserManagerImpl类【getMsgByPsw】获取短信验证码开始>>>mobile:" + mobile + ">>>inputMobile:" + inputMobile + ">>>bnsType:" + bnsType + ">>>timestamp:" + System.currentTimeMillis());
		
		//验证 图形验证码通过标识
		String resetLPWFlag = (String) request.getSession(true).getAttribute(SessionValue.SESSION_RESETLPWVERFLAG);
		if(null == resetLPWFlag || (null != resetLPWFlag && !ECConstants.RETURN_CODE_0000.equals(resetLPWFlag))){
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_9003);
			jsonObject.put("returnMsg", ECConstants.RETURN_MSG_9003);
			return jsonObject;
		}
		
		//验证发送短信时间间隔
		Long lastSendTime = (Long)request.getSession(true).getAttribute(SessionValue.SESSION_RESETLPWMSGTIME);
		if(null != lastSendTime ){
			Long nowTime = new Date().getTime();
			Long interval = (lastSendTime - nowTime) / 1000;//取秒数
			
			//未到 间隔时间提示 操作太频繁
			if(interval > 0 ){
				jsonObject.put("returnCode", ECConstants.RETURN_CODE_9007);
				jsonObject.put("returnMsg", ECConstants.RETURN_MSG_9007);
				return jsonObject;
			}
		}
		
		JSONObject returnJson = verifyMobile(context, mobile);
		// 如果图片验证码验证通过后保存的手机号和输入的手机号一致,进行后续操作，否则，返回错误信息
		if (inputMobile != null && inputMobile.equals(mobile)) {
			returnCode = returnJson.getString("returnCode");
			// 返回的信息
			returnMsg = returnJson.getString("returnMsg");
			// 申请成功后返回的字符串
			String sessionID = "";

			if (ECConstants.RETURN_CODE_USRA000.equals(returnCode) || ECConstants.RETURN_CODE_USRA017.equals(returnCode)) {
				MsgServiceMessageDto msgServicedto = messageManager.applyVrfCode(context, mobile, bnsType, "", "", "", GetIPUtils.getIpAddr(request));
				if (msgServicedto != null) {
					returnCode = msgServicedto.getReturnCode();
					if (ECConstants.RETURN_CODE_0000.equals(returnCode)) {
						//发送成功则 记录 下次时间 下次验证时 必须超过这个时间
						Calendar gapCalendar =  Calendar.getInstance();
						gapCalendar.setTime(new Date());
						gapCalendar.add(Calendar.SECOND, resetLPWGapTime);
						Long diffTime =gapCalendar.getTime().getTime();
						request.getSession(true).setAttribute(SessionValue.SESSION_RESETLPWMSGTIME,diffTime);
						sessionID = msgServicedto.getSessionID();
						request.getSession(true).setAttribute(SessionValue.SESSION_USERMOBILE, mobile);
						jsonObject.put("resetLPWMsgTime", diffTime);
						jsonObject.put("sessionID", sessionID);
					}
					returnMsg = msgServicedto.getReturnMsg();
				}
			}else if(ECConstants.RETURN_CODE_0000.equals(returnCode)){
				returnCode = ECConstants.RETURN_CODE_9300;
				returnCode = ECConstants.RETURN_MSG_9300;
			}
			
		} else {
			returnCode = ECConstants.RETURN_CODE_9002;
			returnMsg = ECConstants.RETURN_MSG_9002;
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("UserManagerImpl类【getMsgByPsw】获取短信验证码结束>>>jsonObject:" + jsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}
	
	@Override
	public JSONObject resetUserPassword(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		// 手机号码，直接获取session中保存的手机号码
		String mobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_VERIFYMOBILE);
		// 密码
		String password = request.getParameter("password");
		
		String txtMobile = request.getParameter("txtMobile");

		logger.info("UserManagerImpl类【resetUserPassword】重置登录密码开始>>>mobile:" + mobile + ">>>timestamp:" + System.currentTimeMillis());
		UserBaseInfoDto userBaseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
		
		//登录用户不为空 只能是 登录人的手机号码
		if(null != userBaseInfoDto && !StringUtils.isEmpty(userBaseInfoDto.getCmfUserId())){
			txtMobile = userBaseInfoDto.getMobile();
		}
		
		//支付密码校验
		Pattern pattern=Pattern.compile("^[0-9]{6,16}$"); 
        Matcher matcher = pattern.matcher(password);
        Boolean lPasswordMat = matcher.matches();
		if(!lPasswordMat){
			// 登录密码不符合规则
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_USRA019);
			jsonObject.put("returnMsg", ECConstants.RETURN_MSG_USRA019);
			return jsonObject;
		}
		
		MD5 md5 = new MD5();
		if (password != null && !password.equals("")) {
			password = md5.getMD5ofStr(password.trim());
		}
		
		String returnCode = "";
		String returnMsg = "";

		if (mobile == null || mobile.equals("") || !mobile.equals(txtMobile)) {
			returnCode = "USR-A099";// session中获取用户手机号码为null或者""
			returnMsg = "会话失效，请重新获取验证码";
		} else {

			UserServiceMessage userServiceMessage = userServiceClient.resetUserPassword(context, mobile, password);

			if (userServiceMessage != null) {
				returnCode = userServiceMessage.getReturnCode();
				returnMsg = userServiceMessage.getReturnMsg();
			}
			if (returnCode != null && returnCode.equals("0000")) {
				request.getSession(true).invalidate();// 重置登录密码成功，清除session
			}
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);

		logger.info("UserManagerImpl类【resetUserPassword】重置登录密码结束>>>jsonObject:" + jsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}
	/**
	 * 支付密码管理
	 */
	@Override
	public JSONObject manageTpasswordResgist(Context context,HttpServletRequest request) {
		JSONObject returnJsonObject = null;
		UserServiceMessage userServiceMessage = null;
		Object obj = RequestHelper.getSessionUserBaseInfo(request);
		if (obj == null) {
			// 用户没有登录
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_8000);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_8000);
			return returnJsonObject;
		}
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		logger.info("UserManagerImpl类【manageTpasswordResgist】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());
		String tpassword = request.getParameter("tPassword");// 支付密码
		String oldTpassword = "";
		String manageType = "A";
		String tradeChannel = ECConstants.TRADE_CHANEL_03;
		String tradeMark = ECConstants.TRADE_CHANEL_03;
		logger.info("UserManagerImpl类【manageTpasswordResgist】设置支付密码>>>cmfUserId=" + cmfUserId + ",System.currentTimeMillis()=" + System.currentTimeMillis());
		try {
			//支付密码格式校验
			Pattern pattern=Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$"); 
	        Matcher matcher = pattern.matcher(tpassword);
	        Boolean tPasswordMat = matcher.matches();
			if(!tPasswordMat){
				// 支付密码不符合规则
				returnJsonObject = new JSONObject();
				returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_USRA018);
				returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_USRA018);
				return returnJsonObject;
			}
			returnJsonObject = new JSONObject();
			String returnCode = "";
			String returnMsg = "";
			MD5 md5 = new MD5();
			tpassword = md5.getMD5ofStr(tpassword);
			userServiceMessage = userServiceClient.manageTpassword(context, cmfUserId, tpassword, oldTpassword, manageType, tradeChannel, tradeMark);
			logger.info("UserManagerImpl类【manageTpasswordResgist】验证支付密码manageTpassword： returnCode" + userServiceMessage.getReturnCode() + "returnMsg" + userServiceMessage.getReturnMsg());
			if (userServiceMessage != null) {
				returnCode = userServiceMessage.getReturnCode();
				returnMsg = userServiceMessage.getReturnMsg();
				returnJsonObject.put("returnCode", returnCode);
				returnJsonObject.put("returnMsg", returnMsg);
			}
		} catch (Exception e) {
			logger.error("UserManagerImpl类【manageTpasswordResgist】验证支付密码manageTpassword：错误" + userServiceMessage.getResultCode());
			e.printStackTrace();
		}
		logger.info("UserManagerImpl类【manageTpasswordResgist】结束>>>returnJsonObject:" + returnJsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
		return returnJsonObject;
	}
	
	@Override
	public JSONObject login(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		String loginNumber = request.getParameter("mobile");
		String password = request.getParameter("password");
		String rvfcode = request.getParameter("rvfcode");
		logger.info("UserManagerImpl类【login】开始>>>loginNumber:" + loginNumber + ">>>rvfcode:" + rvfcode + ">>>timestamp:" + System.currentTimeMillis());
		/***********************验证图片验证码 B*******************************************/
		Map<String, String> map = VerifyCodeController.verifyRandom(request, rvfcode);
		if(map == null || !map.get("returnCode").equals(ECConstants.RETURN_CODE_0000)){
			returnCode = map.get("returnCode");
			returnMsg = map.get("returnMsg");

			jsonObject.put("returnCode", returnCode);
			jsonObject.put("returnMsg", returnMsg);
			return jsonObject;
		}
		/***********************验证图片验证码 E*******************************************/
		
		MD5 md5 = new MD5();
		if (loginNumber != null && !loginNumber.equals("")) {
			loginNumber = loginNumber.trim();
		}
		if (password != null && !password.equals("")) {
			password = md5.getMD5ofStr(password.trim());
		}
		String loginType = ECConstants.LOGIN_TYPE_X;
		// 长度为15位或者18位 是身份证号码登录
		if (loginNumber.length() == 15 || loginNumber.length() == 18) {
			loginType = ECConstants.LOGIN_TYPE_0;
		}
		
		String loginChannel = ECConstants.LOGIN_CHANEL_01;
		String loginMark = ECConstants.LOGIN_NMARK_01;
		
		UserServiceMessage userServiceMessage = userServiceClient.login(context, loginNumber, password, loginType, loginChannel, loginMark);
		if(userServiceMessage != null){
			returnCode=userServiceMessage.getReturnCode();
			returnMsg=userServiceMessage.getReturnMsg();	
			if((ECConstants.COMMON_SUCCESS).equals(userServiceMessage.getReturnCode())){
				/*String requestUrl = (String) request.getSession(true).getAttribute(SessionValue.SESSION_REQUESTURL);*/
				request.getSession(true).removeAttribute(SessionValue.SESSION_REQUESTURL);
				logger.info("UserManagerImpl类【login】【【【【【【【【登录接口调用成功并登录成功，获取到session中url的值并返回到页面去】】】】】】】】");
				jsonObject.put("requestUrl", "/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=RICHES_INFO");
				UserBaseInfoDto userbaseInfo = userServiceMessage.getUserBaseInfoDto();
				UserAccoRlaDto userAccoRlaDto = userServiceMessage.getUserAccoRlaDto();
				if(userbaseInfo != null){
					request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userbaseInfo);
					request.getSession(true).setAttribute(SessionValue.SESSION_CMFUSERID, userbaseInfo.getCmfUserId());
					request.getSession(true).setAttribute("cmfUserName", userbaseInfo.getCustName());
				}
				if(userAccoRlaDto != null){
					request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userAccoRlaDto);
				}
			}
		}
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("UserManagerImpl类【login】结束>>>jsonObject:" + jsonObject + ">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}

	@Override
	public JSONObject queryUserinfo(HttpServletRequest request) {
		logger.info("UserManagerImpl类【queryUserinfo】开始>>>timestamp:" + System.currentTimeMillis());
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		JSONObject jsonObject = new JSONObject();
		if(StringUtils.isNotBlank(cmfUserId)){
			//重新从数据库获取一遍
			UserServiceMessage userServiceMessage = userServiceClient.queryUserAndAccoRlaById(null, cmfUserId);
			UserBaseInfoDto userBaseInfoDto = userServiceMessage != null ? userServiceMessage.getUserBaseInfoDto() : null;
			UserAccoRlaDto userAccoRlaDto = userServiceMessage != null ? userServiceMessage.getUserAccoRlaDto() : null;
			CustInfoDto custInfoDto = userServiceMessage != null ? userServiceMessage.getCustInfoDto() : null;
			if (userBaseInfoDto != null) {
				String mobile = userBaseInfoDto.getMobile();
				mobile = StringUtils.isBlank(mobile) ? "" : mobile.substring(0, 3) + "****" + mobile.substring(7);
				String userName = StringUtils.isBlank(userBaseInfoDto.getCustName()) ? "" : userBaseInfoDto.getCustName();// 姓名
				String temp = "";
				for (int i = 0; userName.length() > 0 && i < userName.length() - 1; i++) {
					temp += "*";
				}
				userName = userName.length() > 0 ? userName.charAt(0) + temp : userName;
				String idNo = userBaseInfoDto.getIdNo();
				idNo = StringUtils.isBlank(idNo) ? "" : idNo.substring(0, 4) + "**********" + idNo.substring(idNo.length() - 4);
				jsonObject.put("cmfUserId", cmfUserId);
				jsonObject.put("mobile", mobile);//手机号
				jsonObject.put("userName", userName);//脱敏客户名
				jsonObject.put("idNo", idNo);//脱敏身份证
				jsonObject.put("idType", StringUtils.isBlank(userBaseInfoDto.getIdType()) ? "" : userBaseInfoDto.getIdType());//证件类型
				jsonObject.put("riskLevel", StringUtils.isBlank(userBaseInfoDto.getRiskLevel()) ? "" : userBaseInfoDto.getRiskLevel());//风险等级
				jsonObject.put("userType", StringUtils.isBlank(userBaseInfoDto.getUserType()) ? "" : userBaseInfoDto.getUserType());//用户等级
				jsonObject.put("idSetPassword", StringUtils.isBlank(userBaseInfoDto.getLPassword()) ? "N" : "Y");//是否设置密码
				jsonObject.put("nation", StringUtils.isBlank(userBaseInfoDto.getNation()) ? "" : userBaseInfoDto.getNation());//国籍代码
				jsonObject.put("nationNM", StringUtils.isBlank(userBaseInfoDto.getNationNM()) ? "" : userBaseInfoDto.getNationNM());//国籍
				jsonObject.put("province", StringUtils.isBlank(userBaseInfoDto.getProvince()) ? "" : userBaseInfoDto.getProvince());//省份代码
				jsonObject.put("provinceNM", StringUtils.isBlank(userBaseInfoDto.getProvinceNM()) ? "" : userBaseInfoDto.getProvinceNM());//省份
				jsonObject.put("city", StringUtils.isBlank(userBaseInfoDto.getCity()) ? "" : userBaseInfoDto.getCity());//城市代码
				jsonObject.put("cityNM", StringUtils.isBlank(userBaseInfoDto.getCityNM()) ? "" : userBaseInfoDto.getCityNM());//城市
				jsonObject.put("addr", StringUtils.isBlank(userBaseInfoDto.getAddr()) ? "" : userBaseInfoDto.getAddr());//详细地址
				jsonObject.put("invprtp", StringUtils.isBlank(userBaseInfoDto.getInvprtp()) ? "" : userBaseInfoDto.getInvprtp());//申请类型 0 转换专业用户 1 转换普通用户
				jsonObject.put("appst", userAccoRlaDto != null ? StringUtils.isNotBlank(userAccoRlaDto.getAppst()) ? userAccoRlaDto.getAppst() : "" : "");//申请状态 N 待处理  I 处理中 Y 申请成功 C 申请失败
				jsonObject.put("vocCode", StringUtils.isBlank(userBaseInfoDto.getVocCode()) ? "" : userBaseInfoDto.getVocCode());//职业代码
				jsonObject.put("vocCodeNM", StringUtils.isBlank(userBaseInfoDto.getVocCodeNM()) ? "" : userBaseInfoDto.getVocCodeNM());//职业
				jsonObject.put("birthDate", StringUtils.isBlank(userBaseInfoDto.getBirthDate()) ? "" : userBaseInfoDto.getBirthDate());//出生日期 yyyy-MM-dd
				jsonObject.put("birthDateNm", StringUtils.isBlank(userBaseInfoDto.getBirthDateNm()) ? "" : userBaseInfoDto.getBirthDateNm());//出生日期 yyyy年MM月dd日
				jsonObject.put("taxResidentType", StringUtils.isBlank(userBaseInfoDto.getTaxResidentType()) ? "" : userBaseInfoDto.getTaxResidentType());//税收居民类型
				jsonObject.put("taxResidentTypeNm", StringUtils.isBlank(userBaseInfoDto.getTaxResidentTypeNm()) ? "" : userBaseInfoDto.getTaxResidentTypeNm());//税收居民类型描述
				jsonObject.put("invprtpScore", userBaseInfoDto.getInvprtpScore() == null ? "0" : userBaseInfoDto.getInvprtpScore());//专业投资知识问卷分数
				jsonObject.put("syncInvprtpAlert", StringUtils.isBlank(userBaseInfoDto.getSyncInvprtpAlert()) ? "" : userBaseInfoDto.getSyncInvprtpAlert());//同步专业投资者提醒标志
				jsonObject.put("isControl", StringUtils.isBlank(userBaseInfoDto.getIsControl()) ? "" : userBaseInfoDto.getIsControl());//是否存在控制关系
				jsonObject.put("isNotBeneficiary", StringUtils.isBlank(userBaseInfoDto.getIsNotBeneficiary()) ? "" : userBaseInfoDto.getIsNotBeneficiary());//是否不是实际受益人
				jsonObject.put("isBadHonesty", StringUtils.isBlank(userBaseInfoDto.getIsBadHonesty()) ? "" : userBaseInfoDto.getIsBadHonesty());//是否有不良诚信
				jsonObject.put("specialRiskLevel", StringUtils.isBlank(userBaseInfoDto.getSpecialRiskLevel()) ? "" : userBaseInfoDto.getSpecialRiskLevel());//特殊用户风险等级
				jsonObject.put("isSetTradePassword", RequestHelper.getTPsdStatus(userBaseInfoDto));//是否设置支付密码
				jsonObject.put("custName", StringUtils.isBlank(userBaseInfoDto.getCustName()) ? "" : userBaseInfoDto.getCustName());//客户名
				jsonObject.put("otherVocation", StringUtils.isBlank(userBaseInfoDto.getOtherVocation()) ? "" : userBaseInfoDto.getOtherVocation());//其他职业
				jsonObject.put("idExpireDate", custInfoDto != null ? StringUtils.isNotBlank(custInfoDto.getIdExpireDate()) ? custInfoDto.getIdExpireDate() : "" : "");//证件过期日
				jsonObject.put("returnCode", ECConstants.RETURN_CODE_0000);
				jsonObject.put("returnMsg", ECConstants.RETURN_MSG_0000);
			} else {
				jsonObject.put("returnCode", ECConstants.RETURN_CODE_8000);
				jsonObject.put("returnMsg", ECConstants.RETURN_MSG_8000);
			}
		}else {
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_8000);
			jsonObject.put("returnMsg", ECConstants.RETURN_MSG_8000);
		}
		logger.info("UserManagerImpl类【queryUserinfo】结束>>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}

	@Override
	public JSONObject headerInfo(HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		
		UserBaseInfoDto userBaseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
		String name = "";
		String idNo = "";
		String cmfUserId = "";
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
			name = RequestHelper.getEncryptUserName(userBaseInfoDto.getCustName());
			if(StringUtils.isBlank(name)){
				Context context = new Context();
				UserServiceMessage usermessage = userServiceClient.queryUserAndAccoRlaById(context, cmfUserId);
				userBaseInfoDto = usermessage.getUserBaseInfoDto();
				name = RequestHelper.getEncryptUserName(userBaseInfoDto.getCustName());
			}
			idNo = userBaseInfoDto.getIdNo();
			
			if(name != null && !name.equals("")){
				name = "您好！"+ name;
				Integer i = (int) idNo.charAt(16);
				if(i%2 == 0){
					name = name + "女士";
				}else{
					name = name + "先生";
				}
			}else{
				name = "您好！尊敬的用户";
			}
			jsonObject.put("returnCode", ECConstants.COMMON_SUCCESS);
			jsonObject.put("returnMsg", "操作成功");
			jsonObject.put("name", name);
			jsonObject.put("cmfUserId", cmfUserId);
		}else{
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_8000);
			jsonObject.put("returnMsg", ECConstants.RETURN_MSG_8000);
		}
		return jsonObject;
	}
	
	@Override
	public JSONObject sendMsg(Context context, HttpServletRequest request) {

		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		// 手机号码，获取session中的验证图片验证码时候存放到session中的手机号码
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		String mobile = "";
		if (userBaseInfoDto != null) {
			// 从session中获取用户的手机号码
			mobile = userBaseInfoDto.getMobile();
		} else {
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_9000);
			jsonObject.put("returnMsg", "请先登录");
			logger.info("UserManagerImpl类【sendMsg】获取短信验证码-session中没找到该用户登录信息>>>returnCode = 9000;>>>timestamp:" + System.currentTimeMillis());
			return jsonObject;
		}
		String bnsType = request.getParameter("bnsType");

		logger.info("UserManagerImpl类【sendMsg】获取短信验证码开始>>>mobile:" + mobile + ">>>bnsType:" + bnsType + ">>>timestamp:" + System.currentTimeMillis());

		String sessionID = "";

		MsgServiceMessageDto msgServicedto = messageManager.applyVrfCode(context, mobile, bnsType, "", "", "", GetIPUtils.getIpAddr(request));
		if (msgServicedto != null) {
			returnCode = msgServicedto.getReturnCode();
			if (ECConstants.RETURN_CODE_0000.equals(returnCode)) {
				sessionID = msgServicedto.getSessionID();
				request.getSession(true).setAttribute(SessionValue.SESSION_USERMOBILE, mobile);

				jsonObject.put("sessionID", sessionID);
			}
			returnMsg = msgServicedto.getReturnMsg();
		}

		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("UserManagerImpl类【getMsgByPsw】获取短信验证码结束>>>jsonObject:" + jsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}

	@Override
	public JSONObject userManager(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";

		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String oldTpassword = request.getParameter("tPassword");
		String tpassword = request.getParameter("newTPassword");
		String mobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_USERMOBILE);

		logger.info("UserManagerImpl类【userManager】修改支付密码开始>>>cmfUserId:" + cmfUserId + ">>>mobile:" + mobile + ">>>timestamp:" + System.currentTimeMillis());
		
		String manageType = "M";
		String tradeChannel = ECConstants.TRADE_CHANEL_01;
		String tradeMark = ECConstants.TRADE_NMARK_01;
		
		UserBaseInfoDto userBaseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
		String hasSetTradePassword = RequestHelper.getTPsdStatus(userBaseInfoDto);
		if(hasSetTradePassword != null && hasSetTradePassword.equals("Y")){
			
		}else{
			logger.info("UserManagerImpl类【userManager】修改支付密码中从还未设置支付密码，hasSetTradePassword="+hasSetTradePassword);
			returnCode = "9333";
			returnMsg = "还未设置支付密码，不能修改";
			jsonObject.put("returnCode", returnCode);
			jsonObject.put("returnMsg", returnMsg);
			return jsonObject;
		}
		if (mobile == null || mobile.equals("")) {
			returnCode = "USR-A099";// session中获取用户手机号码为null或者""
			returnMsg = "会话失效，请重新获取验证码";
			logger.info("UserManagerImpl类【userManager】修改支付密码中从session中没有获取到手机号码，会话失效>>>timestamp:" + System.currentTimeMillis());
			jsonObject.put("returnCode", returnCode);
			jsonObject.put("returnMsg", returnMsg);
			return jsonObject;
		}
		
		//支付密码校验
		Pattern pattern=Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,16}$"); 
        Matcher matcher = pattern.matcher(tpassword);
        Boolean tPasswordMat = matcher.matches();
		if(!tPasswordMat){
			// 支付密码不符合规则
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_USRA018);
			jsonObject.put("returnMsg", ECConstants.RETURN_MSG_USRA018);
			return jsonObject;
		}

		MD5 md5 = new MD5();
		if (tpassword != null && !tpassword.equals("")) {
			tpassword = md5.getMD5ofStr(tpassword);
		}
		if (oldTpassword != null && !oldTpassword.equals("")) {
			oldTpassword = md5.getMD5ofStr(oldTpassword);
		}

		UserServiceMessage userServiceMessage = userServiceClient.manageTpassword(context, cmfUserId, tpassword, oldTpassword, manageType, tradeChannel, tradeMark);

		if(userServiceMessage != null){
			returnCode = userServiceMessage.getReturnCode();
			returnMsg = userServiceMessage.getReturnMsg();
		}
		
		//修改支付密码成功则更新session中的信息
		if(returnCode.equals("USR-1I00")){
			// 更新保存在session中的用户信息
			request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO,userServiceMessage.getUserBaseInfoDto());
			// 删除session中保存的手机号码
			request.getSession(true).removeAttribute(SessionValue.SESSION_USERMOBILE);
		}
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("UserManagerImpl类【userManager】修改支付密码结束>>>jsonObject:" + jsonObject.toString() + System.currentTimeMillis());
		
		return jsonObject;
	}
	/**
	 * 得到用户中心菜单栏目
	 */
	@Override
	public JSONObject getMenu(HttpServletRequest request) {
		JSONObject returnJson = new JSONObject();
		String path = this.getClass().getResource("/config-properties/category.xml").getPath();
		logger.info("UserManagerImpl类【getMenu】开始>>>path:" + path + ">>timestamp:" + System.currentTimeMillis());
		CategoryDelegate cDelegate = new CategoryDelegate(path);
		//根据用户类型展现菜单
		CategoryDto rootCat = cDelegate.getRootCat();
		logger.info("UserManagerImpl类【getMenu】获取根菜单");
		//获取主栏目(二级)
		CategoryDto[] mainCats = null;
		if (rootCat != null) {
			mainCats = rootCat.getSubCats();
			logger.info("UserManagerImpl类【getMenu】获取主菜单");
		}
		//获取当前主栏目
		String currentMainCatId = RequestHelper.getString(request,"mainCatId"); //当前主栏目
		String defaultMainCatId = null;
		if (rootCat != null) {
			defaultMainCatId = rootCat.getDefaultId(); //根栏目默认子栏目
			logger.info("UserManagerImpl类【getMenu】获取当前选中的主菜单");
		}
		//请求参数为空的时候，将根栏目默认子栏目做为当前主栏目
		if (currentMainCatId == null || "".equals(currentMainCatId)) {
			if(currentMainCatId == null || "".equals(currentMainCatId.trim())){
				currentMainCatId = defaultMainCatId;
			}
		}
		CategoryDto currentMainCat = cDelegate.getCat(currentMainCatId);
		//获取当前主栏目的三级栏目
		CategoryDto[] thirdCats = cDelegate.getSubCats(currentMainCatId);
		
		//获取当前三级栏目
		String currentThirdCatId = RequestHelper.getString(request,"thirdCatId");
		if (currentThirdCatId == null || "".equals(currentThirdCatId)) {
			if(currentThirdCatId == null || "".equals(currentThirdCatId.trim())){
				if (currentMainCat != null) {
					currentThirdCatId = currentMainCat.getDefaultId(); //当前主栏目默认子栏目
					logger.info("UserManagerImpl类【getMenu】获取子菜单--3级菜单currentThirdCatId="+currentThirdCatId);
				}
			}
		}
		CategoryDto currentThirdCat = cDelegate.getCat(currentThirdCatId);
		String currentThirdCatUrl = currentThirdCat.getUrl();
		returnJson.put("thirdCats", thirdCats);
		returnJson.put("mainCats", mainCats);
		returnJson.put("currentThirdCatId", currentThirdCatId);
		returnJson.put("currentThirdCat", currentThirdCat);
		returnJson.put("currentThirdCatUrl", currentThirdCatUrl);
		returnJson.put("currentMainCatId", currentMainCatId);
		returnJson.put("defaultMainCatId", thirdCats);
		logger.info("UserManagerImpl类【getMenu】结束>>>returnJson:" + returnJson + ">>timestamp:" + System.currentTimeMillis());
		return returnJson;
	}

	@Override
	public JSONObject setTpassword(Context context, HttpServletRequest request) {
		
		logger.info("UserManagerImpl类【setTpassword】开始>>>timestamp:" + System.currentTimeMillis());
		
		UserBaseInfoDto userBaseInfo = RequestHelper.getSessionUserBaseInfo(request);
		String userMobile = "";// 用户注册手机号
		if(userBaseInfo != null){
			userMobile = userBaseInfo.getMobile();
		}
		
		// 用户验证手机号
		String mobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_VERIFYMOBILE);
		JSONObject jsonObject = new JSONObject();
		
		if(mobile == null || mobile.equals("") || !mobile.equals(userMobile)){
			logger.info("UserManagerImpl类【setTpassword】用户注册手机号码和短信验证码手机号码不一致。>>>userMobile=" + userMobile + ">>>mobile=" + mobile);
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_9002);
			jsonObject.put("returnMsg", ECConstants.RETURN_MSG_9002);
			return jsonObject;
		}
		jsonObject = manageTpasswordResgist(context, request);
		
		return jsonObject;
	}

	@Override
	public JSONObject checkTPassword(Context context, HttpServletRequest request) {
		
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String tpassword = request.getParameter("tpassword");

		logger.info("UserManagerImpl类【checkTPassword】开始>>>cmfUserId:"+cmfUserId+">>>timestamp:" + System.currentTimeMillis());
		
		MD5 md5 = new MD5();
		if(tpassword != null && !tpassword.equals("")){
			tpassword = md5.getMD5ofStr(tpassword);
		}else{
			returnCode = ECConstants.RETURN_CODE_9000;
			returnMsg = "请输入支付密码";
			jsonObject.put("returnCode", returnCode);
			jsonObject.put("returnMsg", returnMsg);
			return jsonObject;
		}
		
		String oldTpassword = "";
		String manageType = "V";
		String tradeChannel = ECConstants.TRADE_CHANEL_01;
		String tradeMark = ECConstants.TRADE_MARK_01;
		
		
		UserServiceMessage userServiceMessage = userServiceClient.manageTpassword(context, cmfUserId, tpassword, oldTpassword, manageType, tradeChannel, tradeMark);
		
		UserBaseInfoDto userBaseInfoDto = new UserBaseInfoDto();
		Integer tPwdErrCount = -1;
		
		if(userServiceMessage != null){
			returnCode = userServiceMessage.getReturnCode();
			returnMsg = userServiceMessage.getReturnMsg();
			
			userBaseInfoDto = userServiceMessage.getUserBaseInfoDto();
			if(userBaseInfoDto != null){// 获取支付密码错误次数
				tPwdErrCount = userBaseInfoDto.getTPwdErrCount();
			}
			jsonObject.put("returnCode", returnCode);
			jsonObject.put("returnMsg", returnMsg);
			jsonObject.put("tPwdErrCount", tPwdErrCount);
		}
		
		//支付密码正确则更新session信息，将支付密码是否正确的标识符放入到session中
		if(returnCode.equals("USR-1I00")){
			request.getSession(true).setAttribute(SessionValue.SESSION_CHECKEDTPASSWORD,"Y");
		}

		logger.info("UserManagerImpl类【checkTPassword】结束>>>cmfUserId:"+cmfUserId+">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}

	@Override
	public JSONObject getVrfCode(Context context, HttpServletRequest request) {
		String mobile = request.getParameter("mobile");

		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		String bnsType = "6";// 修改注册手机号码验证
		
		logger.info("UserManagerImpl类【getMsgByPsw】获取短信验证码开始>>>mobile:" + mobile + ">>>bnsType:" + bnsType + ">>>timestamp:" + System.currentTimeMillis());
		
		String checkedTpassword = (String) request.getSession(true).getAttribute(SessionValue.SESSION_CHECKEDTPASSWORD);
		if(checkedTpassword == null || checkedTpassword.equals("")){
			returnCode = ECConstants.RETURN_CODE_9003;
			returnMsg = "请先验证支付密码";
			jsonObject.put("returnCode", returnCode);
			jsonObject.put("returnMsg", returnMsg);
			return jsonObject;
		}
		
		JSONObject returnJson = verifyMobile(context, mobile);
		returnCode = returnJson.getString("returnCode");
		// 返回的信息
		returnMsg = returnJson.getString("returnMsg");
		// 申请成功后返回的字符串
		String sessionID = "";
		
		/**
		 * 验证手机号码
		 * 可用 0000	注册用户 USR-A000	已占用 USR-A017	系统运行时不可知异常 USR-8000	mobile必填 USR-B004
		 */
		if (ECConstants.RETURN_CODE_USRA000.equals(returnCode) || ECConstants.RETURN_CODE_0000.equals(returnCode)) {
			if(ECConstants.RETURN_CODE_0000.equals(returnCode)){// 未注册用户
				request.getSession(true).setAttribute(SessionValue.SESSION_HASCHECKSETLPASSWORD, "N");
			}else{// 注册用户
				request.getSession(true).setAttribute(SessionValue.SESSION_HASCHECKSETLPASSWORD, "Y");
			}
			MsgServiceMessageDto msgServicedto = messageManager.applyVrfCode(context, mobile, bnsType, "", "", "", GetIPUtils.getIpAddr(request));
			if (msgServicedto != null) {
				returnCode = msgServicedto.getReturnCode();
				if (ECConstants.RETURN_CODE_0000.equals(returnCode)) {
					sessionID = msgServicedto.getSessionID();
					request.getSession(true).setAttribute(SessionValue.SESSION_USERMOBILE, mobile);

					jsonObject.put("sessionID", sessionID);
				}
				returnMsg = msgServicedto.getReturnMsg();
			}
		}
			
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("UserManagerImpl类【getMsgByPsw】获取短信验证码结束>>>jsonObject:" + jsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}

	@Override
	public JSONObject modifyRegMobileAndSetPsw(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		String verifyMobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_VERIFYMOBILE);// 短信验证码验证成功保存手机号码
		String canSetLPassword = (String) request.getSession(true).getAttribute(SessionValue.SESSION_CANSETLPASSWORD);// 保存在session中  修改手机号码时 是否需要设置登录密码

		logger.info("UserManagerImpl类【modifyRegMobileAndSetPsw】开始>>>verifyMobile:" + verifyMobile + ">>>timestamp:" + System.currentTimeMillis());
		
		String lPassword = request.getParameter("password");
		String operatorType = "1";// 1：修改手机号码未被使用，直接修改手机号码	
		if(canSetLPassword != null && canSetLPassword.equals("Y")){
			operatorType = "2";// 2：修改的手机号码已被注册，但未鉴权，修改手机号码且修改登录密码
		}
	
		if (lPassword != null && !lPassword.equals("")) {
			MD5 md5 = new MD5();
			lPassword = md5.getMD5ofStr(lPassword);
		}
		
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		
		UserServiceMessage userServiceMessage = userServiceClient.modifyRegMobile(context, cmfUserId, verifyMobile, lPassword, operatorType, ECConstants.USER_CHANEL_01);
		
		if(userServiceMessage != null){
			returnCode = userServiceMessage.getReturnCode();
			returnMsg = userServiceMessage.getReturnMsg();
			if(returnCode != null && returnCode.equals(ECConstants.RETURN_CODE_0000)){
				request.getSession(true).invalidate();// 修改手机号码成功不需要设置登录密码   直接注销
			}
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("UserManagerImpl类【modifyRegMobileAndSetPsw】结束>>>returnCode:" + returnCode + ">>>returnMsg:" + returnMsg + ">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}
	@Override
	public JSONObject manageTpasswordNew(Context context, String cmfUserId, String tpassword, String oldTpassword, String manageType, String tradeChannel, String tradeMark) {
		UserServiceMessage userServiceMessage = userServiceClient.manageTpassword(context, cmfUserId, tpassword, oldTpassword, manageType, tradeChannel, tradeMark);
		UserBaseInfoDto userBaseInfoDto = null;
		Integer tPwdErrCount = 0;
		JSONObject returnJsonObject=new JSONObject();
		if(userServiceMessage != null){
			String resultCode = userServiceMessage.getReturnCode();
			logger.info("【【【【【userServiceMessage】】】】】"+userServiceMessage.getUserBaseInfoDto() + "-----"+userServiceMessage.getReturnCode() + "-----"+userServiceMessage.getResultMsg());;
			
			if(resultCode != null && resultCode.equals("USR-1I00")){
				returnJsonObject.put("returnCode", resultCode);
				returnJsonObject.put("resultMsg", userServiceMessage.getResultMsg());
			}else{
				returnJsonObject.put("resultCode", resultCode);
				if(resultCode.equals("USR-1I01")){// 用户已锁定-- 您输入密码错误次数过多，请3个小时之后重试
					returnJsonObject.put("resultMsg", "您输入密码错误次数过多，请3个小时之后重试");
				}else if(resultCode.equals("USR-1I02")){// 旧密码错误，但未达到错误次数上限
					returnJsonObject.put("resultMsg", "旧密码错误，但未达到错误次数上限");
				}else{// 您输入的支付密码有误，请重新输入
					returnJsonObject.put("resultMsg", "您输入的支付密码有误，请重新输入");
					returnJsonObject.put("resultCode", "6000");
				}
				
				userBaseInfoDto = userServiceMessage.getUserBaseInfoDto();
				if(userBaseInfoDto != null){// 支付密码错误次数
					tPwdErrCount = userBaseInfoDto.getTPwdErrCount();
				}
//				returnJsonObject.put("resultMsg", userServiceMessage.getReturnMsg());
				returnJsonObject.put("tPwdErrCount", tPwdErrCount);
				logger.info("TradeController类【fundTrade】中支付密码验证失败》》》returnJsonObject="+returnJsonObject.toString());
				return returnJsonObject;
			}
		}else{
			returnJsonObject.put("resultCode", "9999");
			logger.info("TradeController类【fundTrade】中验证支付密码失败》》》返回信息：returnJsonObject="+returnJsonObject);
			return returnJsonObject;
		}
		return returnJsonObject;
	}
	@Override
	public JSONObject queryOriginalUserinfo(HttpServletRequest request) {
		logger.info("UserManagerImpl类【queryUserinfo】开始>>>timestamp:" + System.currentTimeMillis());
		String cmdUserid = RequestHelper.getSessionCmfUserId(request);
		//重新从数据库获取一遍
		UserBaseInfoDto userBaseInfoDto = userServiceClient.queryUserAndAccoRlaById(null, cmdUserid).getUserBaseInfoDto();
		JSONObject jsonObject = new JSONObject();
		String mobile = "";
		String userName = "";
		String idType = "";
		String idNo = "";
		String riskLevel = "";
		String userType = "";
		String returnCode = "";
		String returnMsg = "";
		String isSetPassword = "";
		String isSetTradePassword = "";
		String mobileOriginal ="";
		String userNameOriginal ="";
		String idNoOriginal ="";
		String invprtp ="";
		Integer invprtpScore = 0;
		String isControl = "";//是否存在控制关系
		String isNotBeneficiary = "" ;//是否不是实际受益人
		String isBadHonesty  = "";//是否有不良诚信
		String specialRiskLevel ="";//
		if (userBaseInfoDto != null) {
			mobile = userBaseInfoDto.getMobile();// 手机号
			userName = userBaseInfoDto.getCustName();// 姓名
			idType = userBaseInfoDto.getIdType();// 证件类型
			idNo = userBaseInfoDto.getIdNo();// 身份证号码
			riskLevel = userBaseInfoDto.getRiskLevel();// 风险等级
			userType = userBaseInfoDto.getUserType();// 用户等级
			mobile = mobile == null || mobile.equals("") ? "" : mobile.substring(0, 3) + "****" + mobile.substring(7);
			isSetPassword = userBaseInfoDto.getLPassword() == null || "".equals(userBaseInfoDto.getLPassword()) ? "N" : "Y";
			invprtp = userBaseInfoDto.getInvprtp();
			invprtpScore=  userBaseInfoDto.getInvprtpScore();
			isControl = userBaseInfoDto.getIsControl() ;
			isNotBeneficiary = userBaseInfoDto.getIsNotBeneficiary();
			isBadHonesty = userBaseInfoDto.getIsBadHonesty();
			specialRiskLevel = userBaseInfoDto.getSpecialRiskLevel();
			String temp = "";
			if (userName != null && !userName.equals("")) {
				for (int i = 0; i < userName.length() - 1; i++) {
					temp += "*";
				}
			}
			userName = userName == null || userName.equals("") ? "" : userName.charAt(0) + temp;
			idNo = idNo == null || idNo.equals("") ? "" : idNo.substring(0, 4) + "**********" + idNo.substring(idNo.length() - 4);
			returnCode = ECConstants.COMMON_SUCCESS;
			isSetTradePassword = RequestHelper.getTPsdStatus(userBaseInfoDto);
			mobileOriginal =userBaseInfoDto.getMobile(); 
			userNameOriginal = userBaseInfoDto.getCustName();
			idNoOriginal=userBaseInfoDto.getIdNo();
		} else {
			returnCode = ECConstants.RETURN_CODE_8000;
			returnMsg = ECConstants.RETURN_MSG_8000;
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		jsonObject.put("mobile", mobile);
		jsonObject.put("userName", userName);
		jsonObject.put("idType", idType);
		jsonObject.put("idNo", idNo);
		jsonObject.put("riskLevel", riskLevel);
		jsonObject.put("userType", userType);
		jsonObject.put("isSetTradePassword", isSetTradePassword);
		jsonObject.put("idSetPassword", isSetPassword);
		jsonObject.put("mobileOriginal", mobileOriginal);
		jsonObject.put("userNameOriginal", userNameOriginal);
		jsonObject.put("idNoOriginal", idNoOriginal);
		jsonObject.put("invprtp", invprtp);
		jsonObject.put("invprtpScore", invprtpScore);
		jsonObject.put("isControl",isControl);
		jsonObject.put("isNotBeneficiary",isNotBeneficiary);
		jsonObject.put("isBadHonesty",isBadHonesty);
		jsonObject.put("specialRiskLevel",specialRiskLevel);
		
		//密码置空不能传入前台
		logger.info("UserManagerImpl类【queryUserinfo】结束>>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}

	@Override
	public JSONObject queryIsNeedTest(HttpServletRequest request) {
		String RISKLEVEL= (String)request.getSession(true).getAttribute("riskLevel");
		String RISKEVALDATE= (String)request.getSession(true).getAttribute("riskEvalDate");
		JSONObject jsonObject = new JSONObject();
		// 等级和时间都存在 session
		if (null != RISKLEVEL && null != RISKEVALDATE) {
			jsonObject.put("riskLevel", RISKLEVEL);
			jsonObject.put("riskEvalDate", RISKEVALDATE);
		}else{
			String cmdUserid = RequestHelper.getSessionCmfUserId(request);
			UserBaseInfoDto userBaseInfoDto = userServiceClient.queryUserAndAccoRlaById(null, cmdUserid).getUserBaseInfoDto();
			UserBaseInfoDto userRiskDateDto = userServiceClient.queryUserRiskEvalDateByCmfUserId(null, cmdUserid).getUserBaseInfoDto();
			
			String userType = userBaseInfoDto.getUserType();
			String riskLevel = "";
			String riskEvalDate = "";
			if (userBaseInfoDto != null) {
				riskLevel = userBaseInfoDto.getRiskLevel();// 风险等级
			}
			if(userRiskDateDto != null ){//测评时间
				riskEvalDate = userRiskDateDto.getRiskEvalDate();
				jsonObject.put("riskEvalDate", riskEvalDate);
			}
			request.getSession(true).setAttribute("USERTYPE",userType);
			request.getSession(true).setAttribute("RISKLEVEL",riskLevel);
			request.getSession(true).setAttribute("riskLevel",riskLevel);
			request.getSession(true).setAttribute("riskEvalDate",riskEvalDate);
			jsonObject.put("riskLevel", riskLevel);
		}
		return jsonObject;
	}

	@Override
	public UserServiceMessage updateCmfUserBaseInfo(Context context,
			String cmfUserId, String nation, String province, String city,
			String addr, String voccode, String dateOfBirth, String taxResidentType, String otherVocation) {
		UserServiceMessage userServiceMessage = userServiceClient.updateCmfUserBaseInfo(context, cmfUserId, nation, province, city, addr, voccode, dateOfBirth, taxResidentType, otherVocation);
		return userServiceMessage;
	}

	@Override
	public String queryUserTaxInfoListByUserId(HttpServletRequest request) {
		logger.info("UserManagerImpl类【queryUserTaxInfoListByUserId】开始>>>timestamp:" + System.currentTimeMillis());
		List<UserTaxInfoDto> userTaxInfoList = new ArrayList<UserTaxInfoDto>();
		JSONArray jsonArray = new JSONArray();
		String resultString ="";
		try{
			String cmfUserId = RequestHelper.getSessionCmfUserId(request);
			userTaxInfoList = userServiceClient.queryUserTaxInfoListByUserId(cmfUserId);
			jsonArray = JSONArray.fromObject(userTaxInfoList);
			resultString = jsonArray.toString();
		}catch(Exception e){
			logger.error("----UserManagerImpl-queryUserTaxInfoListByUserId-Exception:",e);
			e.printStackTrace();
		}
		return resultString;
	}
	
	@Override
	public void saveTaxInfoByUserInvtp(String custNo,String cmfUserId,String taxResidentType,String taxResidentData) {
		//类型为空 或者 类型非为仅中国并且数据为空 
		if(StringUtils.isEmpty(taxResidentType) || StringUtils.isEmpty(taxResidentData) ||(!"1".equals(taxResidentType) && StringUtils.isEmpty(taxResidentData))){
			return ;
		}
		JSONArray jsonArray = new JSONArray();
		try{
			jsonArray = JSONArray.fromObject(taxResidentData);
		}catch(Exception e){
			logger.error("----UserManagerImpl-saveTaxInfoByUserInvtp-JSONArray.fromObject-Exception-cmfUserId:"+cmfUserId+";taxResidentType="+taxResidentType);
			logger.error("----UserManagerImpl-saveTaxInfoByUserInvtp-JSONArray.fromObject-Exception:"+taxResidentData);
			logger.error("----UserManagerImpl-saveTaxInfoByUserInvtp-JSONArray.fromObject-Exception:",e);
			e.printStackTrace();
			return;
		}
		if(jsonArray.size() <= 0){
			return ;
		}
		try{
			//用户税收居民信息保存入库
			userServiceClient.saveUserTaxInfo(custNo, cmfUserId, taxResidentType, jsonArray);
		}catch(Exception e){
			logger.error("----UserManagerImpl-saveTaxInfoByUserInvtp-Exception:",e);
			e.printStackTrace();
		}
	}

	@Override
	public UserServiceMessage updateUserInvprtpAlert(String cmfUserId) {
		logger.info("UserManagerImpl类【updateUserInvprtpAlert】开始>>>timestamp:" + System.currentTimeMillis());
		UserServiceMessage userServiceMessage = new UserServiceMessage();
		try{
			userServiceMessage = userServiceClient.updateUserInvprtpAlert(cmfUserId);
		}catch(Exception e){
			logger.error("----UserManagerImpl-updateUserInvprtpAlert-Exception:",e);
			e.printStackTrace();
		}
		return userServiceMessage;
	}

	@Override
	public String queryUserRiskHistory(HttpServletRequest request) {
		JSONArray jsonArray = new JSONArray();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		String custno = "";
		if(userAccoRla != null){
			custno = userAccoRla.getEcCustNo();
		}
		List<UserBaseInfoDto> baseInfoDto = new ArrayList<UserBaseInfoDto>();
		baseInfoDto = userServiceClient.queryUserRiskHistory(cmfUserId, custno);
		jsonArray = JSONArray.fromObject(baseInfoDto);
		return jsonArray.toString();
	}

	@Override
	public UserServiceMessage realNameAfterUpdateInvprtpByEccType(String cmfUserId) {
		return userServiceClient.realNameAfterUpdateInvprtpByEccType(cmfUserId);
	}

	@Override
	public UserServiceMessage updateIdExpireDateByCustNo(String custno, String idExpireDate) {
		return userServiceClient.updateIdExpireDateByCustNo(custno, idExpireDate);
	}
}
