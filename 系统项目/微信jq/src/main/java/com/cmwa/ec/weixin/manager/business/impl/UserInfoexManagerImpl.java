package com.cmwa.ec.weixin.manager.business.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.query.facade.dto.user.UserCommonDto;
import com.cmwa.ec.query.facade.dto.user.UserInfoExtendDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.client.UserServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dao.CustServiceInfoDao;
import com.cmwa.ec.weixin.dao.UserInfoDao;
import com.cmwa.ec.weixin.dao.UserInfoexDao;
import com.cmwa.ec.weixin.dto.UserInfoexDto;
import com.cmwa.ec.weixin.manager.business.SynchronousDataManager;
import com.cmwa.ec.weixin.manager.business.TradeManager;
import com.cmwa.ec.weixin.manager.business.UserInfoexManager;
import com.cmwa.ec.weixin.manager.wxevent.ActivityEventHandler;
import com.cmwa.ec.weixin.manager.wxevent.impl.ActivityEventHandlerFactory;
import com.cmwa.ec.weixin.util.ContextUtils;
import com.cmwa.ec.weixin.util.MD5;
import com.cmwa.ec.weixin.util.RequestHelper;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.StringUtils;
import com.cmwa.ec.weixin.util.cache.ParameterCache;
import com.cmwa.ec.weixin.util.socket.SocketUtil;


/***
 * 用户扩展信息管理
 * @author liury
 *
 */
public class UserInfoexManagerImpl  implements UserInfoexManager{

	  
	private static Logger logger = Logger.getLogger(UserInfoexManagerImpl.class.getName());
	
    @Autowired
	private UserInfoexDao userInfoexDao;
    
    @Autowired
	private UserServiceClient userServiceClient;
    
    @Autowired
    private MessageServiceClient messageServiceClient;
    
    @Autowired
    private TradeManager tradeManager;
    
    @Autowired
	private UserInfoDao userInfoDao;
    
    @Autowired
    private CustServiceInfoDao custServiceInfoDao;
    
    @Autowired
    private SynchronousDataManager synchronousDataManager;
    
    /**
     * 用户登录
     * @param Context 日志信息
	 * @param loginChannel [必填] 登陆通道（默认基金易Web）   
		通道标识： 01-官网，02-小企业e家，03-淘宝，04-通联，05-京东，90-其他
	 * @param loginMark [必填] 登陆来源标示（默认基金易）  标示：01-WEB，02-APP，03-WEIXIN
	 * @return UserServiceMessage 返回码\返回信息\用户基本信息Dto，含密码错误次数\用户账户对应Dto 
		返回码如下
	 * 用户输入信息验证通过		0000
	 * 引导绑定手机号码		USR-A003
	 * 提示是否绑定代销资料		USR-A004
	 * 用户登录已锁定			USR-A005
	 * 系统根据用户输入查找出两个以上的用户	USR-A006
	 * 登录用户未注册		USR-A007
	 * 用户类型不在“10-注册用户，20-查询用户，30-交易用户”之中		USR-A008
	 * 密码错误，但未达到错误次数上限	USR-A009
	 * 无效ID		USR-A010
	 * 系统运行时不可知异常	 	USR-8000
	 * LoginNo/LPassword/LoginChannel/LoginMark必填		USR-B001
     */
    @Override
	public JSONObject login(HttpServletRequest request, Context context, String loginChannel, String loginMark) {
		
		JSONObject jsonObject = new JSONObject(); 
		
		String loginNo = request.getParameter("userName");
		String lPassword = request.getParameter("passWord");
		
		String loginType = WXConstants.LOGIN_TYPE_X;
		//长度大于11 是身份证号码登录
		if(loginNo.length()==15 || loginNo.length()==18){
			loginType = WXConstants.LOGIN_TYPE_0;
		}
		
		MD5 md5 = new MD5();
		if(lPassword != null){
			lPassword = md5.getMD5ofStr(lPassword);
		}
		
		String requestPath = null;
		
		logger.info("UserInfoexManagerImpl类【login】开始>>>mobile:" + loginNo  + ">>>timestamp:" + System.currentTimeMillis());
		
		UserServiceMessage userServiceMsg = userServiceClient.login(context,loginNo, lPassword, loginType, WXConstants.LOGIN_CHANEL_04 , WXConstants.LOGIN_NMARK_04);
 
		String returnCode="";
		//返回的信息
		String returnMsg="";
		if(userServiceMsg != null){
		
			returnCode = userServiceMsg.getReturnCode();
		 
			if(WXConstants.COMMON_SUCCESS.equals(returnCode)){
				
				//更新session中的用户信息
				UserBaseInfoDto userBaseInfo = userServiceMsg.getUserBaseInfoDto();
//				request.getSession(true).setAttribute("reqChannel", "WEIXIN");
				if(userBaseInfo!=null){
					String isSetTradePassword = "1"; //已设置或修改初始的支付密码
					if(userBaseInfo.getLPassword().equals(userBaseInfo.getTPassword())){
						isSetTradePassword = "0";//未设置或修改初始的支付密码
					}
					request.getSession(true).setAttribute(SessionValue.SESSION_ISSETTRADEPASSWORD, isSetTradePassword);
					request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
					request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userServiceMsg.getUserAccoRlaDto());
					String cmfUserId = userBaseInfo.getCmfUserId();
					request.getSession(true).setAttribute(SessionValue.SESSION_CMFUSERID, cmfUserId);
					//获取跳转到登录页面前的请求页面地址，然后将其从session中删除
					requestPath = (String)request.getSession(true).getAttribute(SessionValue.SESSION_REQUESTPATH);
					/*request.getSession(true).removeAttribute(SessionValue.SESSION_REQUESTPATH);
					requestPath = "WeixinService/business/user/queryAccount.shtml";
					jsonObject.put(SessionValue.SESSION_REQUESTPATH, "/WeixinService/business/user/queryAccount.shtml");
					request.getSession(true).setAttribute(SessionValue.SESSION_REQUESTPATH, requestPath);*/
				}else{
					//userBaseInfo为空
					returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
				}
				
			}else {
				returnMsg=userServiceMsg.getReturnMsg();	
			}
	
			
		}
	 
		if(WXConstants.USER_CODE_A009.equals(returnCode)){
			returnMsg="error password!";
		}
		logger.info("UserInfoexManagerImpl类【login】结束>>>returnCode:" + returnCode + ">>>returnMsg:" + returnMsg + ">>>timestamp:"+System.currentTimeMillis());
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		jsonObject.put(SessionValue.SESSION_REQUESTPATH, requestPath);
		//登录后  跳转至 个人账号页面
		//jsonObject.put(SessionValue.SESSION_REQUESTPATH, "/WeixinService/business/user/queryAccount.shtml");
		return jsonObject;
		
	}
    
    /**
	 * 绑定微信，即微信端登录绑定
	 * @author liury
	 */
    @Override
	public String bindWeixin(HttpServletRequest request,Context context) {
		String openId = (String)request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
		Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		Object obj1 = request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
    	String returnCode = WXConstants.COMMON_SUCCESS;
		
		if(openId!=null && obj!=null){
			UserBaseInfoDto userInfo = (UserBaseInfoDto)obj;
			UserInfoexDto userInfoexDto = userInfoexDao.queryByOpenId(openId,"");
			UserInfoexDto dto=new UserInfoexDto();
			dto.setOpenid(openId);
			
			if(obj1!=null){
				dto.setCrmCustno(((UserAccoRlaDto)obj1).getCrmCustNo());
				dto.setEcCustno(((UserAccoRlaDto)obj1).getEcCustNo());
			}
			dto.setCmfuserid(userInfo.getCmfUserId());
			dto.setBindstat(WXConstants.USERINFOEX_BINDSTAT_R);
			
			if(userInfoexDto==null){
				userInfoexDao.insert(dto);
			}else{
				userInfoexDao.updatecmfUserid(openId,userInfo.getCmfUserId(),WXConstants.USERINFOEX_BINDSTAT_R); 
			}
			
			/** 20190419 新增同步绑定更新 CMWA_WX_USER_INFO_EXTEND表 */
			UserCommonDto userCommonDto = new UserCommonDto();
			userCommonDto.setOpenId(openId);
			userCommonDto.setCmfUserId(userInfo.getCmfUserId());
			synchronousDataManager.synchUserInfoExtend(userCommonDto, "weixinbind");
			/** 20190419 新增同步绑定更新 CMWA_WX_USER_INFO_EXTEND表 */
			
			String activityId = (String)request.getSession(true).getAttribute(SessionValue.SESSION_ACTIVITYID);
			
			//session中为空，则从数据库查询，查到以后将活动id放入session
			if(StringUtils.isEmptyString(activityId)){
				Context contexts = ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, WXConstants.WEIXIN_CHANNEL, userInfo.getCmfUserId(), System.currentTimeMillis(), System.currentTimeMillis(), request.getSession().getId(), "");
				activityId = tradeManager.queryActivityByUserInfo(contexts, openId, userInfo.getCmfUserId());
				request.getSession(true).setAttribute(SessionValue.SESSION_ACTIVITYID, activityId);
			}
			
			if(activityId!=null && !activityId.equals(WXConstants.DEFAULT_EVENT_MANAGER)){
				tradeManager.updateCmfUserId4Activity(context, openId, userInfo.getCmfUserId(), activityId);
			}
			
			
			//使用活动对应的事件处理器处理
			ActivityEventHandler eventHandler = ActivityEventHandlerFactory.getEventHanlder(activityId);
			if(eventHandler==null){
				request.getSession(true).setAttribute(SessionValue.SESSION_ACTIVITYID, WXConstants.DEFAULT_EVENT_MANAGER);
				eventHandler = ActivityEventHandlerFactory.getEventHanlder(WXConstants.DEFAULT_EVENT_MANAGER);
			}
			
			Map<String, String> params = new HashMap<String, String>();
			params.put("openId", openId);
			params.put("msgType", "service");
			boolean bool = eventHandler.bind(params);
			if(!bool){
				logger.info("消息推送失败，activityId="+activityId+",openId="+openId);
			}
			
			//根据用户类型（已注册用户、已鉴权用户）给用户分组
			if(userInfo.getUserType()!=null && userInfo.getUserType().equals("30")){
				logger.info("已鉴权过的用户。。。");
				//鉴权用户
				modifyUserGroup(openId, WXConstants.AUTHORITYGROUPID);
			}else{
				logger.info("已注册过的用户。。。");
				//注册用户
				modifyUserGroup(openId, WXConstants.REGISTERGROUPID);
			}
			syncCustserviceInfo(openId, userInfo.getCmfUserId());
		}else{
			returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
		}
		
    	return returnCode;
	}
    
    /***
     * 根据openid、stat查询用户扩展信息
     */
	@Override
	public UserInfoexDto queryUserinfoexQueryRelation(String openid,String stat) {
		
		return userInfoexDao.queryByOpenId(openid,stat);
	}
	
	@Override
	public void updatecmfUserid(String openid ,String cmfuserid,String stat) {
		logger.info("更新旧表cmf_weixin_user_infoex表中BINDSTAT字段为C，未绑定");
		userInfoexDao.updatecmfUserid(openid,cmfuserid,stat);
	}
	
	@Override
	public void subscribleOrUnSubUpdateUserState(String openid ,String cmfuserid,String stat) {
		logger.info("更新旧表cmf_weixin_user_info表中status字段为2，取消关注");
		userInfoDao.updateUserInfoStatus(openid, "2");
		logger.info("更新旧表cmf_weixin_user_infoex表中BINDSTAT字段为C，未绑定");
		userInfoexDao.updatecmfUserid(openid,cmfuserid,stat);
	}
	
	/**
	 * 修改用户分组信息
	 * @param openId
	 * @param groupId
	 * @return
	 */
	@Override
	public String modifyUserGroup(String openId,String groupId){
		
		String changeGroupStr = ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_GROUP, WXConstants.PMCO_GROUP_MODIRYUSERGROUP);
		logger.info("---------->>changeGroupStr:"+changeGroupStr);
		changeGroupStr = changeGroupStr.replace("{openId}", openId);
		changeGroupStr = changeGroupStr.replace("{groupId}", groupId);
		logger.info("---------->>changeGroupStr_new:"+changeGroupStr);
		String resStr = null;
		try {
			String xmlPost = StringUtils.toXmlMessage("changeGroup", changeGroupStr);
			logger.info("xmlPost : "+xmlPost);
			resStr = SocketUtil.sendSocketMessage(xmlPost);
			logger.info("发送socket请求修改用户分组，resStr："+resStr);
		} catch (Exception e) {
			logger.error("发送socket请求修改用户分组，抛出异常，resStr："+resStr,e);
		}
		
		return resStr;
	}
	
	/**
	 * 支付密码管理
	 */
	public JSONObject manageTpassword(Context context, String cmfUserId,
			String tpassword, String oldTpassword, String manageType, String tradeChannel, String tradeMark){
		JSONObject returnJsonObject=null;
		UserServiceMessage userServiceMessage=null;
		
		try {
			returnJsonObject = new JSONObject();
			String returnCode = "";
			String returnMsg = "";
			userServiceMessage=userServiceClient.manageTpassword(context,cmfUserId, tpassword, oldTpassword, manageType, tradeChannel, tradeMark);
			logger.info("验证支付密码manageTpassword： returnCode"+userServiceMessage.getReturnCode()
					+"returnMsg"+userServiceMessage.getReturnMsg());
			if(userServiceMessage!=null){
				returnCode=userServiceMessage.getReturnCode();
				returnMsg=userServiceMessage.getReturnMsg();
				returnJsonObject.put("returnCode", returnCode);
				returnJsonObject.put("returnMsg", returnMsg);
				
				int tPwdErrCount = 0;// 记录支付密码错误次数
				UserBaseInfoDto userBaseInfoDto = userServiceMessage.getUserBaseInfoDto();
				if(userBaseInfoDto != null){
					tPwdErrCount = userBaseInfoDto.getTPwdErrCount();
					returnJsonObject.put("tPwdErrCount", tPwdErrCount);
				}
			}
		} catch (Exception e) {
			logger.error("验证支付密码manageTpassword：错误"+userServiceMessage.getResultCode(),e);
		}
		
		return returnJsonObject;
	
	}
	
	@Override
	public JSONObject verifyMobile(Context context,String mobile) {
		
		JSONObject returnJsonObject = new JSONObject();
	    logger.info("UserInfoexManagerImpl类【verifyMobile】开始>>>mobile:"+mobile+">>>timestamp:"+System.currentTimeMillis());
	    UserServiceMessage userServiceMsg=userServiceClient.verifyMobile(context,mobile);
		
		String returnCode="";
		String returnMsg="";
		if(userServiceMsg!=null){
			returnCode=userServiceMsg.getReturnCode();
			returnMsg=userServiceMsg.getReturnMsg();
			logger.info("UserInfoexManagerImpl类【verifyMobile】验证手机号码>>>returnCode:"+returnCode+",returnMsg:"+returnMsg+">>>timestamp:"+System.currentTimeMillis());
			
			if(WXConstants.COMMON_SUCCESS.equals(returnCode)){
				
			}else if("USR-A017".equals(returnCode)){//返回手机已使用信息
				returnMsg="手机号码已被使用";
			}else{
				returnMsg="验证手机号码错误！";
			}
			returnMsg=userServiceMsg.getReturnMsg();
		}
		
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
			
		logger.info("UserInfoexManagerImpl类【verifyMobile】结束>>>"+returnJsonObject.toString()+">>>timestamp:"+System.currentTimeMillis());
		
		return returnJsonObject;
	}
	
	@Override
	public JSONObject verifyMobileAndGetVerifyCode(Context context,String mobile,String type,String ip,String seqId){
		
		JSONObject returnJsonObject = new JSONObject();
	    logger.info("UserInfoexManagerImpl类【verifyMobileAndGetVerifyCode】开始>>>mobile:"+mobile+">>>timestamp:"+System.currentTimeMillis());
		
		String returnCode="";
		//返回的信息
		String returnMsg="";
		//申请成功后返回的字符串
		String sessionID="nullId";
		
		MsgServiceMessageDto msgServicedto = null;
		try {
			msgServicedto = messageServiceClient.applyVrfCode(context,mobile,type, "","", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(msgServicedto!=null){
			returnCode=msgServicedto.getReturnCode();
			if(WXConstants.COMMON_SUCCESS.equals(returnCode)){
				sessionID=msgServicedto.getSessionID();
			}
			returnMsg=msgServicedto.getReturnMsg();
			logger.info("UserInfoexManagerImpl类【verifyMobileAndGetVerifyCode】手机验证通过>>>returnCode:"+returnCode+">>>returnMsg:"+returnMsg
					+">>>sessionID:"+sessionID+">>>timestamp:"+System.currentTimeMillis());
		}
		
		returnJsonObject.put("sessionTime", "ok");
		returnJsonObject.put("errorCode", returnCode);
		returnJsonObject.put("errorMsg", returnMsg);
		returnJsonObject.put("sessionID", sessionID);
			
		logger.info("UserInfoexManagerImpl类【verifyMobileAndGetVerifyCode】结束>>>"+returnJsonObject.toString()+">>>timestamp:"+System.currentTimeMillis());
		
		return returnJsonObject;
	}
	
	@Override
	public JSONObject registerNormalUserWithT(HttpServletRequest request,Context context,
			String mobile, String mobileStatus, String lPassword,
			String loginChannel, String loginMark) {
		
		String openid=request.getParameter("openid");
		//请求来源 微信浏览器还是其他浏览器
		String channel = RequestHelper.verfiyIEChannel(request);
		
		logger.info("UserInfoexManagerImpl类【registerByMobile】开始>>>mobile:"+mobile+">>>openid:"+openid+">>>timestamp:"+System.currentTimeMillis());
		//注册成功		0000
		//待绑定的手机号码已被使用		USR-A015
		//参数错误					USR-B003
		UserServiceMessage userServiceMsg = userServiceClient.registerNormalUserWithT(context, mobile, mobileStatus, lPassword, loginChannel, loginMark);
 
		String returnCode="";
		//返回的信息
		String returnMsg="";
		String cmfUserId = "";
		
		if(userServiceMsg!=null){
			returnCode=userServiceMsg.getReturnCode();
	 
			if(WXConstants.COMMON_SUCCESS.equals(returnCode)){
				//更新session中的用户信息
				UserBaseInfoDto userBaseInfo = userServiceMsg.getUserBaseInfoDto();
				request.getSession(true).setAttribute(SessionValue.SESSION_REQCHANNEL, "WEIXIN");
				
				if(userBaseInfo!=null){
					cmfUserId = userBaseInfo.getCmfUserId();
					
					String isSetTradePassword = "1"; //已设置或修改初始的支付密码
					if(userBaseInfo.getLPassword().equals(userBaseInfo.getTPassword())){
						isSetTradePassword = "0";//未设置或修改初始的支付密码
					}
					request.getSession(true).setAttribute(SessionValue.SESSION_ISSETTRADEPASSWORD, isSetTradePassword);
					request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
					request.getSession(true).setAttribute(SessionValue.SESSION_CMFUSERID, cmfUserId);
					request.getSession(true).setAttribute(SessionValue.SESSION_SECID, (new MD5()).getMD5ofStr(cmfUserId==null?"":cmfUserId));
					
				}else{
					returnCode = "9999";
				}
				
			}else {
				returnMsg=userServiceMsg.getReturnMsg();	
			}
			
		}
		
		JSONObject returnJsonObject = new JSONObject();
		
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		returnJsonObject.put("temp", cmfUserId);
		
		logger.info("UserInfoexManagerImpl类【registerByMobile】结束>>>returnCode:"+returnCode+">>>returnMsg:"+returnMsg
				+">>>timestamp:"+System.currentTimeMillis());
		
		return returnJsonObject;
	}
	
	


	/**
	 * 验证证件-银行卡鉴权页面
	 */
	@Override
	public JSONObject checkIdNoByBankAuthentication(Context context,
			String cmfUserId, String idNo, String idType) {
		
		JSONObject json = new JSONObject();
		
		String returnCode="";
		String returnMsg="";
		logger.info("UserInfoexManagerImpl类【checkIdNoByBankAuthentication】开始>>>cmfUserId:"+cmfUserId+"idtp:"+idType+">>>"+"idno:"+idNo+">>>timestamp:"+System.currentTimeMillis());
		UserServiceMessage userServiceMsg=userServiceClient.identityCerification(context,idNo, idType);
		if(userServiceMsg!=null){
			returnCode=userServiceMsg.getReturnCode();
			returnMsg=userServiceMsg.getReturnMsg();
			UserBaseInfoDto userInfo = userServiceMsg.getUserBaseInfoDto();
			/**
			 *  *  用户已注册		0000
			 * 代销/直销/母公司系统注册，本交易系统未注册		USR-A001
			 * 用户未注册		USR-A002
			 * 参数错误 idNo/idType必填  USR-B002
			 * 系统运行时不可知异常 	USR-8000
			 */
			//当返回 0000 时，判断返回的userBaseInfo是否是当前用户自己的信息
			//如果cmfUserId比较相等的话，则验证通过  设置returnCode 为USR-A003
			//反之，证件号码已被注册
			if("0000".equals(returnCode)){
				logger.info("【checkIdNoByBankAuthentication】---userInfo:"+userInfo!=null?userInfo.toString():null);
				if(userInfo!=null){
					if(userInfo.getCmfUserId().equals(cmfUserId)){
						returnCode = "USR-A003";
					}
				}
			}
		}else{
			returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
		}
		logger.info("UserInfoexManagerImpl类【checkIdNoByBankAuthentication】结束>>>returnCode:"+returnCode+">>>timestamp:"+System.currentTimeMillis());
		
		json.put("returnCode", returnCode);
		json.put("returnMsg", returnMsg);
		return json;
	}
	
	@Override
	public JSONObject resetUserPassword(Context context, String mobile, String password) {
		
		logger.info("UserInfoexManagerImpl类【resetPassword】开始>>>mobile:" + mobile + ">>>timestamp:"+System.currentTimeMillis());
		/**
		 * 返回如下 
		 * 成功					0000
		 * 手机号码未注册  找不到用户		USR-A024	
		 * 修改失败				USR-A022 		
		 * 参数错误				USR-B006
		 */
		UserServiceMessage userServiceMsg = userServiceClient.resetUserPassword(context, mobile, password);
 
		String returnCode="";
		String returnMsg="";
		if(userServiceMsg!=null){
			returnCode = userServiceMsg.getErrCode();
			returnMsg = userServiceMsg.getErrMsg();
		}else {
			returnCode = "9999";
			returnMsg = "";	
		}
		JSONObject returnJsonObject = new JSONObject();
		
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		
		logger.info("UserInfoexManagerImpl类【resetPassword】结束>>>returnCode:" + returnCode + ">>>returnMsg:" + returnMsg + ">>>timestamp:"+System.currentTimeMillis());
		
		return returnJsonObject;
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
	public JSONObject getVerifyCode(Context context,String mobile,String type,String bankNumber,String money,String bankName){
		JSONObject returnJsonObject = new JSONObject();
		
		String returnCode="";
		//返回的信息
		String returnMsg="";
		//申请成功后返回的字符串
		String sessionID="nullId";
		MsgServiceMessageDto msgServicedto = null;
		try {
			msgServicedto = messageServiceClient.applyVrfCode(context,mobile,type, bankNumber,money, bankName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(msgServicedto!=null){
			returnCode=msgServicedto.getReturnCode();
			returnMsg=msgServicedto.getReturnMsg();
			if(WXConstants.COMMON_SUCCESS.equals(returnCode)){
				sessionID=msgServicedto.getSessionID();
			}
			logger.info("获取验证码,不到账户服务验证手机号码，mobile:"+mobile+">>>returnCode:"+returnCode+">>>returnMsg:"+returnMsg
					+">>>sessionID:"+sessionID+">>>timestamp:"+System.currentTimeMillis());
		}
		
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		returnJsonObject.put("sessionID", sessionID);
		
		if(type.equals("1")){
			logger.info("信息鉴权获取手机验证码："+returnJsonObject.toString());
		}else if(type.equals("5")){
			logger.info("交易支付获取手机验证码："+returnJsonObject.toString());
		}
		return returnJsonObject;
	}

	@Override
	public UserServiceMessage modifyRegMobile(Context context, String cmfUserId, String mobile, String lPassword, String operatorType, String channel) {
		UserServiceMessage userServiceMessage = userServiceClient.modifyRegMobile(context,cmfUserId,mobile , lPassword,operatorType,WXConstants.TRADE_CHANEL_03);
		return userServiceMessage;
	}

	@Override
	public UserServiceMessage updateCmfUserBaseInfo(Context context,
			String cmfUserId, String nation, String province, String city,
			String addr, String voccode, String dateOfBirth, String taxResidentType, String otherVocation) {
		UserServiceMessage userServiceMessage = userServiceClient.updateCmfUserBaseInfo(context, cmfUserId, nation, province, city, addr, voccode, dateOfBirth, taxResidentType, otherVocation);
		return userServiceMessage;
	}

	@Override
	public String queryUserRiskHistory(HttpServletRequest request) {
		JSONArray jsonArray = new JSONArray();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		String custno = "";
		if(userAccoRla != null){
			custno = userAccoRla.getEcCustNo();
		}
		List<UserBaseInfoDto> baseInfoDto = new ArrayList<UserBaseInfoDto>();
		baseInfoDto = userServiceClient.queryUserRiskHistory(cmfUserId, custno);
		jsonArray = JSONArray.fromObject(baseInfoDto);
		return jsonArray.toString();
	}
	
	/**
	 * 实名认证后 同步柜台专业投资者至电商 
	 * @param cmfUserId
	 * @return
	 */
	public UserServiceMessage realNameAfterUpdateInvprtpByEccType(String cmfUserId){
		return userServiceClient.realNameAfterUpdateInvprtpByEccType(cmfUserId);
	}

	@Override
	public UserInfoExtendDto queryRiskLevel(UserInfoExtendDto dto) {
		UserInfoExtendDto d=null;
		try {
			 d= userInfoexDao.queryRiskLevel(dto);
		} catch (Exception e) {
			logger.error("查询用户是否做过适当性数据异常",e);
		}
		return d;
	}

	@Override
	public void syncCustserviceInfo(String openid, String cmfuserid) {
		try{
			logger.info("开始同步两端顾问信息 ==================");
			logger.info("opeid : "+openid+",cmfuserid:"+cmfuserid);
			/* 20180913新增 在进行登陆绑定时 如果两边都有绑定顾问 则以mecc为准  反之互补*/
			// 微信端顾问id
			String wxUserCustserId = userInfoDao.queryCustserIdByOpenid(openid);
			String onlineUserCustserId = custServiceInfoDao.queryCustServiceByCmfUserId(cmfuserid);
			logger.info("查询到wx端绑定顾问id为："+wxUserCustserId + ",mecc端绑定顾问id为:" + onlineUserCustserId);
			int result = -1;
			if(StringUtils.isEmptyString(wxUserCustserId) && StringUtils.isEmptyString(onlineUserCustserId)) {
				logger.info("该用户未绑定顾问，不做同步处理");
			} else if(!StringUtils.isEmptyString(wxUserCustserId) && !StringUtils.isEmptyString(onlineUserCustserId)) {
				logger.info("该用户wx与mecc均有绑定顾问，以mecc端为准");
				result = userInfoDao.updateCustserIdByOpenid(openid, onlineUserCustserId);
			} else if (StringUtils.isEmptyString(wxUserCustserId)) { // 如果wx用户信息表内顾问id为空
				logger.info("该用户只有mecc端有绑定顾问，以mecc端为准");
				result = userInfoDao.updateCustserIdByOpenid(openid, onlineUserCustserId);
			} else { // 如果user_baseinfo内顾问信息为空
				logger.info("该用户只有wx端有绑定顾问，以wx端为准");
				result = custServiceInfoDao.updateCustServiceByCmfUserId(cmfuserid, wxUserCustserId);
			}
			logger.info("修改影响列为：" + result);
		}catch(Exception e) {
			logger.error("同步两端顾问信息时捕获异常，"+e);
		}
	}

	@Override
	public UserServiceMessage updateIdExpireDateByCustNo(String custno, String idExpireDate) {
		return userServiceClient.updateIdExpireDateByCustNo(custno, idExpireDate);
	}
	
}
