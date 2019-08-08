package com.cmwa.ec.weixin.manager.business.impl;



import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.dto.MsgParameterDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.dto.mail.MailConfig;
import com.cmwa.ec.message.facade.dto.mail.MailMessage;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.message.RedeemContentDto;
import com.cmwa.ec.query.facade.dto.system.ParameterDto;
import com.cmwa.ec.query.facade.dto.trade.AppointRequestDto;
import com.cmwa.ec.query.facade.dto.user.CmwaQualifiedUserInfoDto;
import com.cmwa.ec.query.facade.dto.user.CmwaQualifiedUserInfoStatusDto;
import com.cmwa.ec.query.facade.dto.user.QualifiedUserInfoDto;
import com.cmwa.ec.query.facade.dto.user.UserTermsDto;
import com.cmwa.ec.trade.facade.dto.AmazonResult;
import com.cmwa.ec.trade.facade.dto.CmfWXPushmessageDto;
import com.cmwa.ec.trade.facade.dto.OrderResult;
import com.cmwa.ec.trade.facade.dto.bank.CmbSignParaDto;
import com.cmwa.ec.trade.facade.dto.fund.FundTradeDto;
import com.cmwa.ec.trade.facade.dto.fund.SignEContractDto;
import com.cmwa.ec.trade.facade.dto.log.CmwaFileUploadRecordDto;
import com.cmwa.ec.trade.facade.dto.user.UserAcctDto;
import com.cmwa.ec.trade.facade.dto.user.UserRiskLevelDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserOperateLogDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.client.TradeServiceClient;
import com.cmwa.ec.weixin.client.UserServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dao.CustServiceInfoDao;
import com.cmwa.ec.weixin.dao.TemplateMsgtoMECCDao;
import com.cmwa.ec.weixin.dao.activity.ActParameterDao;
import com.cmwa.ec.weixin.dto.RemindFailureRedeemDto;
import com.cmwa.ec.weixin.manager.business.AmazonS3Manager;
import com.cmwa.ec.weixin.manager.business.MessageManager;
import com.cmwa.ec.weixin.manager.business.TradeManager;
import com.cmwa.ec.weixin.util.DateUtils;
import com.cmwa.ec.weixin.util.RequestHelper;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.StringUtils;
import com.cmwa.ec.weixin.util.UUIDKeyGenerator;
import com.cmwa.ec.weixin.util.WeixinUtil;

/**
 * 交易模块管理类
 * @author liury
 *
 */
public class TradeManagerImpl implements TradeManager {
	
	
	private static Logger logger = Logger.getLogger(TradeManagerImpl.class.getName());
	@Autowired
	private TradeServiceClient tradeServiceClient;
	
	@Autowired
	private UserServiceClient userServiceClient;
	
	@Autowired
	private QueryServiceClient queryServiceClient;
	
	@Autowired
	private MessageManager messageManager;
	
	@Autowired
	private TemplateMsgtoMECCDao templateMsgtoMECCDao;
	
	@Autowired
	private MessageServiceClient messageServiceClient;
	
	@Autowired
	private CustServiceInfoDao custServiceInfoDao;
	
	@Autowired
	private AmazonS3Manager amazonS3Manager;
	
	@Autowired
	private ActParameterDao actParameterDao;
	
	/**
	 * 用户信息鉴权/开户
	 */
	@Override
	public JSONObject openAccount(Context context, HttpServletRequest request,UserAcctDto userAcctDto) {
		
		String returnCode = null;
		String returnMsg = null;
		JSONObject returnJsonObject = new JSONObject();
		JSONObject userAcctJson = null;
		
		logger.info("调用 ---openAccount()--start ---  接口");
		OrderResult orderResult = tradeServiceClient.openAccount(context, userAcctDto);
		logger.info("调用 ---openAccount()--end ---  接口");
		
		if(null!=orderResult){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
			logger.info("调用用户信息鉴权，returnCode："+returnCode+", returnMsg:"+returnMsg);
			//修改或者插入成功
			if("0000".equals(orderResult.getResultCode())){
				
				//将session中用户账户对应表中的ecCustNo更新
				UserAccoRlaDto userAccoRla = (UserAccoRlaDto)request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
				UserBaseInfoDto userBaseInfo = (UserBaseInfoDto)request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
				Object obj = orderResult.getData();
				if(obj!=null){
					UserAcctDto temp = (UserAcctDto)obj;
					logger.info("用户信息鉴权 返回数据 userName:"+temp.getInvName());
					logger.info("用户信息鉴权 返回数据 eccustNo:"+temp.getEcCustNo());
					logger.info("用户信息鉴权 返回数据:"+temp.toString());
					
					if(temp.getEcCustNo()!=null && !"".equals(temp.getEcCustNo())){
						if(userAccoRla!=null){
							userAccoRla.setEcCustNo(temp.getEcCustNo());
						}else{
							userAccoRla = new UserAccoRlaDto();
							userAccoRla.setCmfUserId(userAcctDto.getCmfUserId());
							userAccoRla.setEcCustNo(temp.getEcCustNo());
						}
						request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userAccoRla);
					}
					
					if(temp.getInvName()!=null && !"".equals(temp.getInvName())){
						if(userBaseInfo!=null){
							userBaseInfo.setCustName(temp.getInvName());
							userBaseInfo.setIdNo(temp.getIdNo());
							userBaseInfo.setUserType("30");
							request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
						}
					}
					
					userAcctJson = JSONObject.fromObject(temp);
					userAcctJson.put("ecCustNo","");
				}
				
			}
			if("CMB01".equals(orderResult.getResultCode())){
				Object obj = orderResult.getData();
				if (obj != null) {
					CmbSignParaDto cmb = (CmbSignParaDto)obj;
					returnJsonObject.put("cmbDto", cmb);
				}
			}
			if("9129".equals(orderResult.getResultCode())){//若返回代码为9129，则是重复开户:查询账户信息，补充session，返回成功。
				returnCode=queryUserAccoRla(context,userAcctDto.getCmfUserId(),request);
			}
		}else{
			returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
		}
		
		String userType = ((UserBaseInfoDto)request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO)).getUserType();
		String isSetTradePassword = (String)request.getSession(true).getAttribute(SessionValue.SESSION_ISSETTRADEPASSWORD);
		returnJsonObject.put("cmfUserIdIsNull", "no");
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		returnJsonObject.put("userType", userType);
		returnJsonObject.put("tradePasswordStatus", isSetTradePassword);
		returnJsonObject.put("userAcctDto", userAcctJson);
		return returnJsonObject;
	}
	
	//查询用户已鉴权,但是没有往session里没有值
	private String queryUserAccoRla(Context context,String cmfUserId,HttpServletRequest request){
		//根据cmfUserId查询用户信息，确认用户类型  （注册用户、鉴权用户）
		UserServiceMessage userServiceMessage = userServiceClient.queryUserAndAccoRlaById(context, cmfUserId);
		String retrunCode="-1";//返回码
		if(null!=userServiceMessage && "0000".equals(userServiceMessage.getReturnCode())){
			//将session中用户账户对应表中的ecCustNo更新
			UserAccoRlaDto userAccoRla = (UserAccoRlaDto)request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
			UserBaseInfoDto userBaseInfo = (UserBaseInfoDto)request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
			if(null!=userServiceMessage.getUserAccoRlaDto()){
				UserAccoRlaDto acctRlaDto=userServiceMessage.getUserAccoRlaDto();
				UserBaseInfoDto baseInfoDto=userServiceMessage.getUserBaseInfoDto();
				//用户账户
				logger.info("再次鉴权返回数据 eccustNo:"+acctRlaDto.getEcCustNo());
				if(null !=acctRlaDto.getEcCustNo() && !"".equals(acctRlaDto.getEcCustNo())){
					if(null !=userAccoRla){//判断session的值
						userAccoRla.setEcCustNo(acctRlaDto.getEcCustNo());
					}else{
						userAccoRla = new UserAccoRlaDto();
						userAccoRla.setCmfUserId(cmfUserId);
						userAccoRla.setEcCustNo(acctRlaDto.getEcCustNo());
					}
					request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userAccoRla);
				}
				
				if(null!=baseInfoDto){//用户信息
					logger.info("再次鉴权返回数据 userName:"+baseInfoDto.getCustName());
					if(null !=baseInfoDto.getCustName() && !"".equals(baseInfoDto.getCustName())){
						if(userBaseInfo!=null){//判断session的值
							userBaseInfo.setCustName(baseInfoDto.getCustName());
							userBaseInfo.setIdNo(baseInfoDto.getIdNo());;
							userBaseInfo.setUserType("30");
							request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
						}
					}
				}
				retrunCode="0000";
			}
			
		}
		return retrunCode;
	}
	
	
	
	/**
	 * 用户添加银行卡
	 */
	@Override
	public JSONObject addBankNumber(Context context, HttpServletRequest request,UserAcctDto userAcctDto) {
		
		String returnCode = null;
		String returnMsg = null;
		JSONObject returnJsonObject = new JSONObject();
		JSONObject userAcctJson = null;
		
		logger.info("调用添加银行卡接口start --- ");
		OrderResult orderResult = tradeServiceClient.addBankNumber(context, userAcctDto);
		logger.info("调用添加银行卡接口end --- ");
		
		if(null!=orderResult){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
			logger.info("调用用户添加银行卡，returnCode："+returnCode+", returnMsg:"+returnMsg);
			//修改或者插入成功
			if("0000".equals(returnCode)){
				UserAcctDto tempUserAcctDto = null;
				if(orderResult.getData()!=null){
					tempUserAcctDto = (UserAcctDto)orderResult.getData();
				}
				userAcctJson = JSONObject.fromObject(tempUserAcctDto);
				userAcctJson.put("ecCustNo","");
				userAcctJson.put("bankAcct",WeixinUtil.getEncryptBankCard(userAcctJson.getString("bankAcct")));
				userAcctJson.put("bankMobile",WeixinUtil.getEncryptMobile(userAcctJson.getString("bankMobile")));
			}
			if("CMB01".equals(orderResult.getResultCode())){
				Object obj = orderResult.getData();
				if (obj != null) {
					CmbSignParaDto cmb = (CmbSignParaDto)obj;
					returnJsonObject.put("cmbDto", cmb);
				}
			}
		}else{
			returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
		}
		
		String userType = ((UserBaseInfoDto)request.getSession(true).getAttribute("userBaseInfo")).getUserType();
		String isSetTradePassword = (String)request.getSession(true).getAttribute("isSetTradePassword");
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		returnJsonObject.put("userType", userType);
		returnJsonObject.put("tradePasswordStatus", isSetTradePassword);
		returnJsonObject.put("userAcctDto", userAcctJson);
		return returnJsonObject;
	}
	
	/**
	 * 解除绑定银行卡
	 */
	@Override
	public JSONObject cancelBindBankCard(Context context, String tradeAcco, String ecCustNo) {
		
		JSONObject returnJsonObject = new JSONObject();
		String returnCode = null;
		String returnMsg = null;
		
		UserAcctDto userAcctDto = new UserAcctDto();
		userAcctDto.setTradeAcct(tradeAcco);
		userAcctDto.setEcCustNo(ecCustNo);
		logger.info("调用 ---cancelBindBankCard()--start ---  接口 ,tradeAcco = "+tradeAcco);
		OrderResult orderResult = tradeServiceClient.cancelBindBankCard(context, userAcctDto);
		logger.info("调用 ---cancelBindBankCard()--end ---  接口");
		
		if(null!=orderResult){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
			logger.info("解除绑定银行卡，returnCode："+returnCode+", returnMsg:"+returnMsg);
			
		}else{
			returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
		}
		
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		
		return returnJsonObject;
	}
	
	/**
	 * 用户预下单
	 */
	@Override
	public JSONObject fundAppoint(Context context, FundTradeDto dto) {
		JSONObject returnJsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		try {
			OrderResult orderResult = tradeServiceClient.fundAttention(context, dto);
			FundTradeDto resultFundTradeDto = null;
			if(orderResult != null){
				returnCode = orderResult.getResultCode();
				returnMsg = orderResult.getResultMsg();
				if(WXConstants.COMMON_SUCCESS.equals(returnCode)){
					resultFundTradeDto = (FundTradeDto) orderResult.getData();// 返回的结果DTO
				}if(resultFundTradeDto!=null){
					resultFundTradeDto.setCustNo("");
				}
			}else{
				returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
				returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
			}
			returnJsonObject.put("returnCode", returnCode);
			returnJsonObject.put("returnMsg", returnMsg);
			returnJsonObject.put("resultFundTradeDto", resultFundTradeDto);
			logger.info("TradeManagerImpl.class的fundAttention()结束>>>returnJsonObject=【"+returnJsonObject.toString()+"】"
					+"returnCode>>>"+returnCode+"returnMsg>>>"+returnMsg);
		} catch (Exception e) {
			logger.error("TradeManagerImpl.class的fundAttention异常 错误 returnCode>>>"+returnCode+"returnMsg>>>"+returnMsg);
			e.printStackTrace();
		}
		
		return returnJsonObject;
	}

	
	
	/**
	 * 下交易单
	 */
	@Override
	public JSONObject fundTrade(Context context, FundTradeDto dto,
			SignEContractDto eclDto) {
		
		JSONObject returnJsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		try {
			OrderResult orderResult = tradeServiceClient.fundTrade(context, dto, eclDto);
			//更新费率
			tradeServiceClient.updateAppointReqCommro(dto);
			
			FundTradeDto resultFundTradeDto = null;
			if(orderResult != null){
				returnCode = orderResult.getResultCode();
				returnMsg = orderResult.getResultMsg();
				if(WXConstants.COMMON_SUCCESS.equals(returnCode)){
					resultFundTradeDto=(FundTradeDto)orderResult.getData();
				}
				if(resultFundTradeDto!=null){
					resultFundTradeDto.setCustNo("");
				}
			}else{
				returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
				returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
			}
			returnJsonObject.put("returnCode", returnCode);
			returnJsonObject.put("returnMsg", returnMsg);
			returnJsonObject.put("resultFundTradeDto", resultFundTradeDto);
			logger.info("TradeManagerImpl.class的fundAttention()结束>>>returnJsonObject=【"+returnJsonObject.toString()+"】"
					+"returnCode>>>"+returnCode+"returnMsg>>>"+returnMsg);
		} catch (Exception e) {
			logger.error("TradeManagerImpl.class的fundAttention异常 错误 fundTrade>>>"+returnCode+"returnMsg>>>"+returnMsg,e);
		}
		
		return returnJsonObject;
	}
	
	/**
	 * 设置用户风险评测级别
	 * @param context
	 * @param userAcctDto
	 * @return
	 */
	@Override
	public JSONObject riskRating(Context context, UserRiskLevelDto userRiskLevelDto,String userType){
		Map<String, Object> map = new HashMap<String, Object>();
		JSONObject returnJsonObject = new JSONObject();
		String returnCode = null;
		String returnMsg = null;
		String riskLevel = "-1";
		logger.info("调用 ---riskRating()--start ---  接口");
		OrderResult orderResult=null;
		if (userType!=null && "10".equals(userType)) {
			orderResult=tradeServiceClient.setCustRiskLevelBeforeOpenAccount(context, userRiskLevelDto);
		}else{
			orderResult = tradeServiceClient.riskRating(context, userRiskLevelDto);
		}
		logger.info("调用 ---riskRating()--end ---  接口");
		
		UserRiskLevelDto risk = new UserRiskLevelDto();
		if(null!=orderResult){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
			logger.info("设置用户风险评测级别，returnCode："+returnCode+", returnMsg:"+returnMsg);
			if(returnCode.equals("0000")){
				Object obj = orderResult.getData();
				if(obj!=null){
					risk = (UserRiskLevelDto)obj;
					riskLevel = risk.getRiskLevel();
				}
				//更新 用户购买时适当性要求 
				this.updateUserProperInfoByRiskLevel(userRiskLevelDto);
				//测评完成 更新特殊用户等级
				this.updateUserSpecialRiskLevelInfo(userRiskLevelDto);
			}
		}else{
			returnCode = "-1";
			returnMsg = "系统繁忙，请稍后再试";
		}
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		returnJsonObject.put("riskLevel", riskLevel);
		returnJsonObject.put("userRiskLevelDto", risk);
		logger.info(returnJsonObject.toString());
		return returnJsonObject;
	}
	@Override
	public OrderResult addReportReadRecord(Context context, String cmfUserId, String fundId, String reportId) {
		OrderResult orderResult = tradeServiceClient.addReportReadRecord(context, cmfUserId, fundId, reportId);
		return orderResult;
	}

	@Override
	public OrderResult cancelAppointRequest(Context context, FundTradeDto fundTradeDto) {
		OrderResult orderResult = tradeServiceClient.cancelAppointRequest(context, fundTradeDto);
		return orderResult;
	}

	@Override
	public OrderResult modifyAppointRequest(Context context, FundTradeDto fundTradeDto) {
		OrderResult orderResult = tradeServiceClient.modifyAppointRequest(context, fundTradeDto);
		return orderResult;
	}

	@Override
	public OrderResult fundTradeLineUp(Context context, FundTradeDto fundTradeDto, SignEContractDto elDto) {
		System.out.println("---------------");
		OrderResult orderResult = tradeServiceClient.fundTradeLineUp(context, fundTradeDto, elDto);
		return orderResult;
	}
	
	
	/**
	 * 身份鉴权（找回交易密码）
	 */
	@Override
	public JSONObject authenticate(Context context, UserAcctDto userAcctDto) {
		String returnCode = null;
		String returnMsg = null;
		JSONObject returnJsonObject = new JSONObject();
		
		logger.info("调用 ---authenticate()--start ---  接口");
		OrderResult orderResult = tradeServiceClient.authenticate(context, userAcctDto);
		logger.info("调用 ---authenticate()--end ---  接口");
		
		if(null!=orderResult){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
			logger.info("调用身份鉴权（找回交易密码），returnCode："+returnCode+", returnMsg:"+returnMsg);
		}else{
			returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
		}
		
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		return returnJsonObject;
	}

	/**
	 * 活动扫码
	 */
	@Override
	public String activityScan(Context context, String openId,
			String activityId, String md5Info) {
		
		String returnCode = null;
		OrderResult orderResult = null;
		try {
			orderResult = tradeServiceClient.activityScan(context, openId, activityId, md5Info);
		} catch (Exception e) {
			logger.error("TradeManagerImpl.activityScan异常",e);
			logger.error("调用活动扫码接口异常，openId="+openId);
		}
		
		if(orderResult!=null){
			returnCode = orderResult.getResultCode();
		}else{
			returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
		}
		
		return returnCode;
	}

	/**
	 * 更新用户参与活动的cmfUserId
	 */
	@Override
	public int updateCmfUserId4Activity(Context context, String openId,
			String cmfUserId, String activityId) {
		int i = 0;
		
		try {
			i = tradeServiceClient.updateCmfUserId4Activity(context, openId, cmfUserId, activityId);
		} catch (Exception e) {
			logger.error("TradeManagerImpl.updateCmfUserId4Activity异常",e);
			logger.error("更新用户参与活动的cmfUserId,cmfUserId="+cmfUserId);
		}
		
		return i;
	}

	/**
	 * 根据用户的openId或者cmfUserId查询用户当前参与的活动列表的id
	 * 返回最新的活动id,若当前用户未参加任何活动，则返回默认活动id 000000
	 */
	@Override
	public String queryActivityByUserInfo(Context context, String openId,
			String cmfUserId) {
		String activityId = null;
		List<Map<String, String>> list = null;
		Map<String, String> map = null;
		OrderResult orderResult = null;
		try {
			orderResult = tradeServiceClient.queryActivityByUserInfo(context, openId, cmfUserId);
			if(orderResult!=null && WXConstants.COMMON_SUCCESS.equals(orderResult.getResultCode())){
				list = (List<Map<String, String>>)orderResult.getData();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			logger.error("查询用户参与的活动的活动id异常,openId="+openId+" ,cmfUserId="+cmfUserId);
		}
		
		if(list!=null && list.size()>0){
			map = list.get(0);
			
			String falg = map.get("FLAG");
			if(!StringUtils.isEmptyString(falg) && !"0".equals(falg)){
				activityId = map.get("ACTIVITYID");
			}
		}
		
		if(StringUtils.isEmptyString(activityId)){
			activityId = WXConstants.DEFAULT_EVENT_MANAGER;
		}
		
		return activityId;
	}

	@Override
	public OrderResult addArticleReadRecord(Context context, String cmfUserId, String articleId) {
		OrderResult orderResult = tradeServiceClient.addArticleReadRecord(context, cmfUserId, articleId);
		return orderResult;
	}

	@Override
	public OrderResult appConversionUserInvtp(Context context,HttpServletRequest request) {
		OrderResult orderResult = null;
		UserRiskLevelDto userRiskLevelDto = new UserRiskLevelDto();
		String apptp = request.getParameter("apptp");
		userRiskLevelDto.setApptp(apptp);
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		if(userAccoRla != null){
			userRiskLevelDto.setCustNo(userAccoRla.getEcCustNo());
	    }
		orderResult = tradeServiceClient.appConversionUserInvtp(context, userRiskLevelDto);
		if("0000".equals(orderResult.getErrCode())){
			//更新 用户购买时适当性要求 
			this.updateUserProperInfoByRiskLevel(userRiskLevelDto);
			//测评完成 更新特殊用户等级
			this.updateUserSpecialRiskLevelInfo(userRiskLevelDto);
		}
		return orderResult;
	}

	@Override
	public OrderResult appConversionUserInvtp(Context context,
			HttpServletRequest request, UserRiskLevelDto userRiskLevelDto) {
		OrderResult orderResult = null;
		orderResult = tradeServiceClient.appConversionUserInvtp(context, userRiskLevelDto);
		if("0000".equals(orderResult.getErrCode())){
			//更新 用户购买时适当性要求 
			this.updateUserProperInfoByRiskLevel(userRiskLevelDto);
			//测评完成 更新特殊用户等级
			this.updateUserSpecialRiskLevelInfo(userRiskLevelDto);
			//客户专业转换普通投资者成功记录到H_CUSTINVPRTP表
			Map<String, String> map = new HashMap<String, String>();
			map.put("custNo", userRiskLevelDto.getCustNo());
			map.put("invprtp", "1");
			map.put("oldInvprtp", "0");
			map.put("status", "Y");
			map.put("channelCode", "NET");
			tradeServiceClient.insertCustInvprtp(map);
		}
		return orderResult;
	}

	@Override
	public OrderResult clearAppcvInvp(Context context,HttpServletRequest request) {
		OrderResult orderResult = null;
		UserRiskLevelDto userRiskLevelDto = new UserRiskLevelDto();
		UserAccoRlaDto userAccoRla =  (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		if(userAccoRla != null){
			userRiskLevelDto.setCustNo(userAccoRla.getEcCustNo());
	    }
		orderResult = tradeServiceClient.clearAppcvInvp(context, userRiskLevelDto);
		return orderResult;
	}

	
	@Override
	public String parseEvalAnswerBySetUserRiskLevel(String oldEvalAnswer) {
		List<String> returnEvalAnswer = new ArrayList<String>();
		String returnString = oldEvalAnswer;
		try{
			String[] answers = oldEvalAnswer.split(",");
			for (int i = 0; i < answers.length; i++) {
				String answer = answers[i];
				StringBuffer newAnswer = new StringBuffer();
				String[] options = answer.split(":");
				if(options.length > 0 ){
					String score = options[0];
					if(null != score){
						score = score.replace('2', 'A').replace('4', 'B').replace('6', 'C').replace('8', 'D').replace("10", "E");
						newAnswer.append(score);
					}
				}else{
					newAnswer.append(":");
				}
				if(options.length > 1 ){
					newAnswer.append(":"+options[1]);
				}else{
					newAnswer.append(":");
				}
				
				if(options.length > 2 ){
					newAnswer.append(":"+options[2]);
				}else{
					newAnswer.append(":");
				}
				returnEvalAnswer.add(newAnswer.toString());
			}
			returnString = org.apache.commons.lang.StringUtils.join(returnEvalAnswer.toArray(), ",");
		}catch(Exception e){
			logger.error("----parseEvalAnswerBySetUserRiskLevel-Exception:",e);
		}
		return returnString;
	}
	
	@Override
	public OrderResult cancelAppConversionUserInvtp(Context context,
			String cmfUserId, String custNo) {
		OrderResult orderResult = tradeServiceClient.cancelAppConversionUserInvtp(context, cmfUserId, custNo);
		return orderResult;
	}

	@Override
	public OrderResult passTestUpdateUserInvtp(Context context,
			String cmfUserId, String custNo, String invprtpScore) {
		OrderResult orderResult =  tradeServiceClient.passTestUpdateUserInvtp(context, cmfUserId, custNo, invprtpScore);
		if("0000".equals(orderResult.getResultCode())){
			userServiceClient.syncInvprtpToEcc(context, cmfUserId, custNo,invprtpScore);
		}
		return orderResult;
	}
	
	/**
	 * 风险测评后 将 是否控制关系 受益人 诚信记录 更新至用户表
	 * 方便购买产品时 限制 不用每次购买都需要解析 答案
	 * @param userBaseInfoDto
	 */
	private void updateUserProperInfoByRiskLevel(UserRiskLevelDto userRiskLevelDto){
		try{
			String evalAnswerData= userRiskLevelDto.getEvalAnswer();
			if(StringUtils.isEmptyString(evalAnswerData)){
				logger.info("----updateUserProperInfoByRiskLevel-evalAnswerData-isNull"+userRiskLevelDto.toString());
				return ;
			}
			String[] evalAnswers = evalAnswerData.split(",");
			String isControl = "";//是否存在控制关系
			String isNotBeneficiary = "" ;//是否不是实际受益人
			String isBadHonesty  = "";//是否有不良诚信
			
			//数据为 2:N:xxx,4:N:xxx,6:N:xxx 格式
			if(evalAnswers.length > 14){
				String[] isControls = evalAnswers[13].split(":");
				if(isControls.length > 2){
					isControl = isControls[1];
				}
			}
			
			if(evalAnswers.length > 15){
				String[] isNotBeneficiarys = evalAnswers[14].split(":");
				if(isNotBeneficiarys.length > 2){
					isNotBeneficiary = isNotBeneficiarys[1];
				}
			}
			
			if(evalAnswers.length > 16){
				String[] isBadHonestys = evalAnswers[15].split(":");
				if(isBadHonestys.length > 2){
					isBadHonesty = isBadHonestys[1];
				}
			}
			
			UserBaseInfoDto userBaseInfoDto = new UserBaseInfoDto();
			userBaseInfoDto.setCmfUserId(userRiskLevelDto.getCmfUserId());
			userBaseInfoDto.setIsControl(isControl);
			userBaseInfoDto.setIsNotBeneficiary(isNotBeneficiary);
			userBaseInfoDto.setIsBadHonesty(isBadHonesty);
			userServiceClient.updateUserProperInfoByRiskLevel(userBaseInfoDto);
		}catch(Exception e){
			logger.error("----updateUserProperInfoByRiskLevel-Exception",e);
			logger.error("----updateUserProperInfoByRiskLevel-Exception-userRiskLevelDto:"+userRiskLevelDto.toString());
		}
	}
	
	private void updateUserSpecialRiskLevelInfo(UserRiskLevelDto userRiskLevelDto){
		UserBaseInfoDto userBaseInfoDto = new UserBaseInfoDto();
		if("0".equals(userRiskLevelDto.getEvalFormno()) && "1".equals(userRiskLevelDto.getRiskLevel())){
			userBaseInfoDto.setSpecialRiskLevel("1");
		}else{
			userBaseInfoDto.setSpecialRiskLevel("");
		}
		userBaseInfoDto.setCmfUserId(userRiskLevelDto.getCmfUserId());
		userServiceClient.updateUserSpecialRiskLevelInfo(userBaseInfoDto);
	}

	@Override
	public JSONObject updateAppointRequest(Context context,HttpServletRequest request) {
		JSONObject json = new JSONObject();
		JSONObject dataJson = new JSONObject();
		String serialno = request.getParameter("serialno");
		String renew = request.getParameter("flag");
		if(StringUtils.isEmptyString(serialno) || StringUtils.isEmptyString(renew)){
			json.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
			json.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
			return json;
		}
		try {
			logger.info("调用queryServiceClient.QueryAppointRequestList()方法查询订单信息---start,入参：serialno = "+ serialno);
			QueryMessageDto result =  queryServiceClient.QueryAppointRequestList(serialno);
			logger.info("调用queryServiceClient.QueryAppointRequestList()方法查询订单信息---end,出参：list = " + JSONArray.fromObject(result.getData()));
			if("0000".equals(result.getResultCode())){
				List<AppointRequestDto> list = (List<AppointRequestDto>)result.getData();
				if(list != null && list.size() >0){
					AppointRequestDto dto = list.get(0);
					if(renew.equals(dto.getRenew())){
						json.put("returnCode", WXConstants.COMMON_SUCCESS);
						json.put("returnMsg","成功");
						return json;
					}
					dataJson.put("renew", renew);
					dataJson.put("serialno", serialno);
					logger.info("调用tradeServiceClient.updateAppointRequest()方法修改订单分配方式---start,入参： "+ dataJson.toString());
					Integer i = tradeServiceClient.updateAppointRequest(dataJson.toString());
					logger.info("调用tradeServiceClient.updateAppointRequest()方法修改订单分配方式---end,出参： "+ i);
					dataJson.put("custIp",RequestHelper.getIpAddr(request));
					dataJson.put("custNo", dto.getCustno());
					dataJson.put("custName", dto.getCustName());
					dataJson.put("custMobile", dto.getMobile());
					dataJson.put("optType", "Y".equals(renew)?"2":"1");
					dataJson.put("sourceType","02");
					dataJson.put("remark", i);
					logger.info("调用tradeServiceClient.addOplog()方法保存用户操作记录---start,入参： "+ dataJson.toString());
					tradeServiceClient.addOplog(dataJson.toString());
					json.put("returnCode", WXConstants.COMMON_SUCCESS);
					json.put("returnMsg","成功");
				}else{
					json.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
					json.put("returnMsg", "获取不到对应的订单信息,请重试！");
				}
			}else{
				json.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
				json.put("returnMsg", "该请求的订单信息异常,请重试！");
			}
		} catch (Exception e) {
			logger.error("updateAppointRequest 发生异常信息,"+e);
			json.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
			json.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
		}
		return json;
	}

	public static String cn2unicode(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]);   //转换为16进制整型字符串
              if (hexB.length() <= 2) {
                  hexB = "00" + hexB;   
             }
             unicodeBytes = unicodeBytes + "\\u" + hexB;   
        }   
        return unicodeBytes;   
    }

	@SuppressWarnings("unchecked")
	@Override
	public JSONObject redemptionOrder(Context context,
			HttpServletRequest request, FundTradeDto fundTradeDto,String cmfUserId) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		String fundname = request.getParameter("fundname")==null?"":request.getParameter("fundname");		//产品名称
		String fourAfterbankCard=request.getParameter("bankAcco");	//银行卡号后四位
		String paymentinter = request.getParameter("paymentinter");
		String money = request.getParameter("money");
		String mobile= "";
		if(!StringUtils.isEmptyString(fourAfterbankCard)){
			fourAfterbankCard=fourAfterbankCard.substring(fourAfterbankCard.length()-4);
		} else {
			fourAfterbankCard="";
		}
		logger.info("TradeManagerImpl类【redemptionOrder】开始>>>cmfUserId:" + context.getCmfUserId() + ">>>serialno:" + fundTradeDto.getSerialNo() + ">>>timestamp:" + System.currentTimeMillis());
		try {
			mobile=custServiceInfoDao.queryMobileNoByCmfUserId(cmfUserId);
		} catch (Exception e) {
			logger.error("查询用户注册表user_baseinfo失败,{}",e);
		}
		// 此处返回的data为赎回单流水号
		OrderResult orderResult = tradeServiceClient.redemptionOrder(context, fundTradeDto);
		if (orderResult != null) {
			if("0000".equals(orderResult.getResultCode())){  
				com.alibaba.fastjson.JSONObject data = (com.alibaba.fastjson.JSONObject)orderResult.getData();
				//预约赎回单不发送短信及邮件
				String apkind = data.getString("apkind");
				if("024".equals(apkind)){
					MsgParameterDto msgParameter = new MsgParameterDto();
				    if (!org.apache.commons.lang.StringUtils.isBlank(mobile)) {
						msgParameter.setMobile(mobile);
						msgParameter.setMsgType("40");
						msgParameter.setMsgPar_fundName(fundname);
					    msgParameter.setMsgPar_bankCard(fourAfterbankCard);
					    msgParameter.setMsgPar_str1(paymentinter);
					    String serialno = fundTradeDto.getSerialNo();
						// added at 2019/03/27 by ex-hezk针对同一活期产品多笔订单的处理
					    if(serialno.length() > 16) {
					    	serialno = serialno.split(",")[0];
					    }
						QueryMessageDto queryMessageDto = queryServiceClient.QueryAppointRequestList(serialno);
					    List<AppointRequestDto> appointRequestList = (List<AppointRequestDto>) queryMessageDto.getData();
					    AppointRequestDto appointRequestDto = appointRequestList == null || appointRequestList.isEmpty() ? null : appointRequestList.get(0);
					    MsgSmsRecordDto msgRecord = new MsgSmsRecordDto();
					    msgRecord.setMethod("A");
					    msgRecord.setSendUser("system");
					    if(appointRequestDto != null) {
					    	msgRecord.setClientName(appointRequestDto.getCustName());
					    	msgRecord.setFundCode(appointRequestDto.getFundid());
					    	msgRecord.setProductName(appointRequestDto.getFundAdName());
					    }
						messageManager.sendSmsMsg(context, msgParameter, msgRecord);
				    }
				    try {
				    	logger.info("活期产品微信端赎回成功时模板消息插入mecc》》》start");		    		
				    	QueryMessageDto queryParamList = queryServiceClient.queryHomeAddressIsWordWithLinkage(context, "SYSTEM", "REDEMPTPUSHMSG", null, null);
				    	List<ParameterDto> listPara = (List<ParameterDto>) queryParamList.getData();
				    	ParameterDto param = null;
				    	if(null != listPara && listPara.size() >0){
				    		param = listPara.get(0);
						    String content = param.getPmv1().
						    		replace("#APDT",DateUtils.formatDate(new Date(), "yyyy年MM月dd日")).
						    		replace("#T", paymentinter).replace("#FUNDNAME", fundname).replace("#SERIALNO", data.getString("serialNo"));
				    		CmfWXPushmessageDto dto = new CmfWXPushmessageDto();
				    		dto.setMessageId(data.getString("serialNo"));
							dto.setCmfUserId(context.getCmfUserId());
							dto.setState("0");
							dto.setMessageType("templateMessage");
							dto.setTemplate_id(param.getPmco());
							dto.setContent(content);
						    tradeServiceClient.addCmfWXPushmessage(dto);
					    	logger.info("活期产品微信端赎回成功时模板消息插入mecc》》》end");
					    	logger.info("活期产品微信端赎回成功时发送邮件》》》start");
							QueryMessageDto resultMap = queryServiceClient.queryRedeemContentList(data.getString("serialNo"));
							if("0000".equals(resultMap.getResultCode())){
								List<RedeemContentDto> redeemList = (List<RedeemContentDto>)resultMap.getData();
								logger.info("MailManagerImpl类调用【queryRedeemContentList】查询邮件正文消息，返回值："+JSONArray.fromObject(redeemList));
								if(null != redeemList && redeemList.size() >0){
									JSONObject paramJson = this.sendSuccessMail(redeemList.get(0));
									if (paramJson.get("returnCode").equals("0000")) {
										logger.info("MailManagerImpl【sendSuccessMail】赎回成功的提醒邮件发送成功>>>");
									} else {
										logger.info("MailManagerImpl【sendSuccessMail】赎回成功的提醒邮件发送失败>>>");
									}
								}
							}else{
								logger.info("MailManagerImpl类调用【queryRedeemContentList】查询邮件正文消息错误，错误信息："+resultMap.getResultMsg());
							}
					    	logger.info("活期产品微信端赎回成功时发送邮件》》》end");
				    	}

				    } catch (Exception e) {
						logger.error("插入mecc消息模板失败:{}",e);
					}
				}
			} else {
				try {
					// added at 2019/03/27 by ex-hezk针对同一活期产品多笔订单的处理
					String serialno = fundTradeDto.getSerialNo();
					if(serialno.length() > 16) {
						fundTradeDto.setSerialNo(serialno.split(",")[0]);
				    }
					//活期产品微信端赎回失败时发送邮件给指定人员，SELECT * FROM PARAMETER T WHERE T.PMKY='REDEMPTFAILURE'
					RemindFailureRedeemDto rfr = templateMsgtoMECCDao.queryRemindFailureRedeem(fundTradeDto);
					mobile=mobile==null?"":mobile;
					rfr.setMobileNo(mobile);
					rfr.setSubamt(money);
					rfr.setSubquty(money);
					JSONObject json = sendFailureRedeemMail(context, rfr);
					logger.info("发送结果为: " + json.get("returnCode"));
				} catch (Exception e) {
					logger.error("活期产品赎回失败时发送提醒邮件异常: {}", e);
				}
			}
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("TradeManagerImpl类【redemptionOrder】结束>>>jsonObject:" + jsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}
	/**
	 * 发送赎回失败邮件给指定人员
	 * @param balcons
	 * @return
	 */
	private  JSONObject  sendFailureRedeemMail(Context context,RemindFailureRedeemDto rfr){
		JSONObject returnJsonObject = new JSONObject();
		String returnCode=null;
		MsgServiceMessageDto msgServicedto = new MsgServiceMessageDto();
		MailMessage mailMessage = new MailMessage();
		//收件人和抄送人
		QueryMessageDto queryParamListCc = queryServiceClient.queryHomeAddressIsWordWithLinkage(context, "SYSTEM", "REDEMPTFAIL_CC", null, null);
		QueryMessageDto queryParamListTo = queryServiceClient.queryHomeAddressIsWordWithLinkage(context, "SYSTEM", "REDEMPTFAIL_TO", null, null);
		
		List<ParameterDto> listParaCc = (List<ParameterDto>) queryParamListCc.getData();
		ParameterDto parameterCc =listParaCc.get(0);
		String[] tccs = parameterCc.getPmco().split(",");   //抄送 人
		
		List<ParameterDto> listParaTo = (List<ParameterDto>) queryParamListTo.getData();
		ParameterDto parameterTo =listParaTo.get(0);
		String[] tos = parameterTo.getPmco().split(",");   //收件人
		
		//邮件内容
		String content = "<!DOCTYPE html><html><head><meta charset='UTF-8'><meta http-equiv=content-type content='text/html;charset=utf-8'>"
				+"</head><body><table style='width: 890px;border-color: #666666;font-family: verdana,arial,sans-serif;font-size:15px;border-collapse: collapse;text-align: center;cellpadding='0' cellspacing='0'>"
				+"<tr style='background: #d3d3d3;'>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>客户姓名</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>手机号</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>购买产品</td>"		
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>失败订单</td>"	
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>失败金额</td>"	
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>失败份额</td>"	
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>失败时间</td></tr>";
		
				if(null != rfr){
						 content +=  " <tr>"				
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+rfr.getInvnm()+"</td>"
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+rfr.getMobileNo()+"</td>"
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+rfr.getFundnm()+"</td>"
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+rfr.getSerialno()+"</td>"
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+rfr.getSubamt()+"</td>"
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+rfr.getSubquty()+"</td>"
							        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+rfr.getDate1()+"</td>"		        
							        + "</tr> ";
				}	
				content += "</table></body></html>";
		try {
			mailMessage.setCc(tccs.length == 0 ? null : tccs); // 抄送的地址+手动输入的地址
			mailMessage.setTo(tos.length == 0 ? null : tos); 	// 接收人，接收人输入框没有的话取配置文件中的地址
			mailMessage.setContent(content);
			mailMessage.setSubject("活期产品赎回失败提醒");// 主题
			mailMessage.setConfId("");
			mailMessage.setPrefix("custom2");
			msgServicedto = messageServiceClient.sendMail(mailMessage);
			logger.info("发送邮件结果：" + msgServicedto.getErrCode());
		} catch (Exception e) {
			logger.error("发送邮件异常：" + e.getMessage());
		}
		if(msgServicedto!=null){
			returnCode=msgServicedto.getResultCode();
		}
		returnJsonObject.put("returnCode", returnCode);			
		return returnJsonObject;
	}

	/* (non-Javadoc)
	 * @see com.cmwa.ec.weixin.manager.business.TradeManager#openAccountForActivity(com.cmwa.ec.base.dto.Context, javax.servlet.http.HttpServletRequest, com.cmwa.ec.trade.facade.dto.user.UserAcctDto)
	 */
	@Override
	public JSONObject openAccountForActivity(Context context,
			HttpServletRequest request, UserAcctDto userAcctDto) {
		String returnCode = null;
		String returnMsg = null;
		JSONObject returnJsonObject = new JSONObject();
		JSONObject userAcctJson = null;
		
		logger.info("调用 ---openAccount()--start ---  接口");
		OrderResult orderResult = tradeServiceClient.openAccountForActivity(context, userAcctDto);
		logger.info("调用 ---openAccount()--end ---  接口");
		
		if(null!=orderResult){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
			logger.info("调用用户信息鉴权，returnCode："+returnCode+", returnMsg:"+returnMsg);
			//修改或者插入成功
			if("0000".equals(orderResult.getResultCode())){
				
				//将session中用户账户对应表中的ecCustNo更新
				UserAccoRlaDto userAccoRla = (UserAccoRlaDto)request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
				UserBaseInfoDto userBaseInfo = (UserBaseInfoDto)request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
				Object obj = orderResult.getData();
				if(obj!=null){
					UserAcctDto temp = (UserAcctDto)obj;
					logger.info("用户信息鉴权 返回数据 userName:"+temp.getInvName());
					logger.info("用户信息鉴权 返回数据 eccustNo:"+temp.getEcCustNo());
					logger.info("用户信息鉴权 返回数据:"+temp.toString());
					
					if(temp.getEcCustNo()!=null && !"".equals(temp.getEcCustNo())){
						if(userAccoRla!=null){
							userAccoRla.setEcCustNo(temp.getEcCustNo());
						}else{
							userAccoRla = new UserAccoRlaDto();
							userAccoRla.setCmfUserId(userAcctDto.getCmfUserId());
							userAccoRla.setEcCustNo(temp.getEcCustNo());
						}
						request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userAccoRla);
					}
					
					if(temp.getInvName()!=null && !"".equals(temp.getInvName())){
						if(userBaseInfo!=null){
							userBaseInfo.setCustName(temp.getInvName());
							userBaseInfo.setIdNo(temp.getIdNo());
							userBaseInfo.setUserType("30");
							request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
						}
					}
					
					userAcctJson = JSONObject.fromObject(temp);
					userAcctJson.put("ecCustNo","");
				}
				
			}
			if("9129".equals(orderResult.getResultCode())){//若返回代码为9129，则是重复开户:查询账户信息，补充session，返回成功。
				returnCode=queryUserAccoRla(context,userAcctDto.getCmfUserId(),request);
			}
		}else{
			returnCode = WXConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = WXConstants.COMMON_ERROR_SYSERRMSG;
		}
		
		String userType = ((UserBaseInfoDto)request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO)).getUserType();
		String isSetTradePassword = (String)request.getSession(true).getAttribute(SessionValue.SESSION_ISSETTRADEPASSWORD);
		returnJsonObject.put("cmfUserIdIsNull", "no");
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		returnJsonObject.put("userType", userType);
		returnJsonObject.put("tradePasswordStatus", isSetTradePassword);
		returnJsonObject.put("userAcctDto", userAcctJson);
		return returnJsonObject;
	}	
	
	/**
	 * 发送赎回成功邮件给指定人员
	 * @param dto
	 * @return
	 */
	private  JSONObject  sendSuccessMail(RedeemContentDto dto){
		JSONObject returnJsonObject = new JSONObject();
		String returnCode=null;
		MsgServiceMessageDto msgServicedto = new MsgServiceMessageDto();
		MailMessage mailMessage = new MailMessage();
		MailConfig mailConfig = null;
		try {
			mailConfig = messageServiceClient.queryMailAll("10");	
			logger.info("查询邮件配置==============" + new Date()+":"+mailConfig.toString());
		} catch (Exception e1) {
			logger.error("查询邮件配置 Exception.......................", e1);
		}
		//邮件内容
		String content = "<!DOCTYPE html><html><head><meta charset='UTF-8'><meta http-equiv=content-type content='text/html;charset=utf-8'>"
				+"</head><body><table style='width: 890px;border-color: #666666;font-family: verdana,arial,sans-serif;font-size:15px;border-collapse: collapse;text-align: center;cellpadding='0' cellspacing='0'>"
				+"<tr style='background: #d3d3d3;'>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>客户姓名</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>赎回时间 </td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>基金名称</td>"		
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'> 基金代码</td>"	
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>赎回份额</td>"	
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>期数</td>"	
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>客户经理</td></tr>";
		
				
		 content +=  " <tr>"				
			        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+dto.getInvnm()+"</td>"
			        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+dto.getRedeemDate()+"</td>"
			        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+dto.getFundName()+"</td>"
			        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+dto.getFundId()+"</td>"
			        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+dto.getSubQuty()+"</td>"
			        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+dto.getPeriod()+"</td>"
			        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+dto.getCustManager()+"</td>"		  
			        + "</tr> ";

				content += "</table></body></html>";
		try {
			mailMessage.setTo(mailConfig.getToList().split(",")); // 接收人，接收人输入框没有的话取配置文件中的地址
			mailMessage.setCc(mailConfig.getCcList().split(",")); // 抄送的地址+手动输入的地址
			mailMessage.setContent(content);
			mailMessage.setSubject(String.format(mailConfig.getHead()));// 主题
			mailMessage.setConfId("10");
			mailMessage.setPrefix("custom2");
			msgServicedto = messageServiceClient.sendMail(mailMessage);
			logger.info("发送邮件结果：" + msgServicedto.getErrCode());
		} catch (Exception e) {
			logger.error("发送邮件异常：" + e.getMessage());
		}
		if(msgServicedto!=null){
			returnCode=msgServicedto.getResultCode();
		}
		returnJsonObject.put("returnCode", returnCode);			
		return returnJsonObject;
	}

	@Override
	public void addOpLog(String string) {
		tradeServiceClient.addOplog(string);
	}

	@Override
	public OrderResult updateOrderRedemptionShare(FundTradeDto fundTradeDto) {
		OrderResult orderResult = tradeServiceClient.updateOrderRedemptionShare(fundTradeDto);
		return orderResult;

	}
	@Override
	public JSONObject addAccreditedInvestorInfo(CmwaQualifiedUserInfoDto param,UserBaseInfoDto currentUserInfo) {
		// 声明query服务需要用到的参数
		Map<String,Object> queryParameter = new HashMap<String,Object>();
		queryParameter.put("cmfuserid",param.getCmfuserid());
		logger.info("开始添加合格投资者申请，参数为："+param);
		//返回对象
		JSONObject returnObject = new JSONObject();
		try {
			returnObject.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
			// 查询是否已经有合格投资者申请
			QueryMessageDto queryServiceResult = queryServiceClient.queryAllQualifiedUserInfo(queryParameter);
			if (queryServiceResult != null && WXConstants.COMMON_SUCCESS.equals(queryServiceResult.getResultCode())) {
				List<QualifiedUserInfoDto> result = (List<QualifiedUserInfoDto>) queryServiceResult.getData();
				if (result != null && result.size() == 0) {
					// 声明查询文件上传记录的参数
					CmwaFileUploadRecordDto fileUploadRecordQueryParam = new CmwaFileUploadRecordDto();
					fileUploadRecordQueryParam.setCmfuserid(param.getCmfuserid());
					// 查询该用户是否上传文件
					List<CmwaFileUploadRecordDto> cmwaFileUploadRecordDtos = tradeServiceClient.queryFileUploadRecord(fileUploadRecordQueryParam);
					boolean resultIsNullFlag = cmwaFileUploadRecordDtos == null || cmwaFileUploadRecordDtos.size() == 0;
					boolean userSatisfyConditionFlag = checkUserSatisfyCondition(param.getCustno());
					if(resultIsNullFlag && !userSatisfyConditionFlag) {
						logger.info("用户无文件上传记录且不满足合格投资者条件，不执行添加");
						returnObject.put("returnMsg","无上传文件记录");
					} else {
						logger.info("查询到用户上传文件记录为："+cmwaFileUploadRecordDtos+",合格投资者条件判断为："+userSatisfyConditionFlag);
						// 如果有上传文件记录则根据不同fileType进行循环，拼接出预期的json格式的字符串
						JSONObject extendInfo = getExtendInfoByFileUploadRecord(cmwaFileUploadRecordDtos);
						param.setExtendInfo(extendInfo.toString());
						logger.info("添加参数为："+param);
						// 进行添加
						String infoId = UUID.randomUUID().toString();								
						logger.info("生成infoId为:"+infoId+",param:"+param);
						param.setInfoId(infoId);
						int executeResult = tradeServiceClient.insertAccreditedInvestorInfo(getTradeQualifiedDto(param));
						if(executeResult == 1) {
							logger.info("添加成功！");
							this.updatedAccreditedInvestorRequestStatus(param.getCustno(),WXConstants.QULIFIEDUSER_STATUS_N,currentUserInfo);
							returnObject.put("returnCode",WXConstants.COMMON_SUCCESS);
							returnObject.put("returnMsg","添加成功");
							return returnObject;
						} else {
							logger.info("添加失败,param:"+param);
							returnObject.put("returnMsg","添加失败");
						}
					}
				}
			} else {
				logger.info("调用query服务失败，返回code为：" + (queryServiceResult == null ? "null" : queryServiceResult.getErrCode()));
				returnObject.put("returnMsg","添加失败");
			}
		} catch (Exception e) {
			logger.error("添加合格投资者申请时捕获异常:",e);
			returnObject.put("returnMsg","系统异常");
		}
		return returnObject;
	}

	
	/* (non-Javadoc)
	 * @see com.cmwa.ec.weixin.manager.business.TradeManager#updateSubmittedInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject updateSubmittedInfo(String key, String custno, String operatorType, String fileName, String fileType, String currentUser) {
		JSONObject returnObj = new JSONObject();
        if (StringUtils.isBlank(key) || StringUtils.isBlank(custno) || StringUtils.isBlank(operatorType)){
            returnObj.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
            returnObj.put("returnMsg","关键参数丢失");
            return returnObj;
        }
        if(!"insert".equalsIgnoreCase(operatorType) && !"delete".equalsIgnoreCase(operatorType)) {
            returnObj.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
            returnObj.put("returnMsg","操作类型错误");
            return returnObj;
        }
        if("insert".equalsIgnoreCase(operatorType) && (StringUtils.isBlank(fileType) || StringUtils.isBlank(fileName))) {
        	returnObj.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
            returnObj.put("returnMsg","关键参数丢失");
            return returnObj;
        }
        try{
            JSONObject userExtendInfo = new JSONObject();
            // 查询需要处理的json数据
            QualifiedUserInfoDto qualifiedUserInfoDto = queryNeedHandleExtendInfo(custno,userExtendInfo,returnObj,fileType);
            if(StringUtils.isNotBlank((String) returnObj.get("returnCode"))) {
                return returnObj;
            }
            userExtendInfo = JSONObject.fromObject(qualifiedUserInfoDto.getInfoRecord().getExtendInfo());
            logger.info("查询到需修改的申请信息上传文件数据为：" + userExtendInfo+",custno:"+custno);
            if(!userExtendInfo.containsKey(fileType)) {
            	userExtendInfo.put(fileType, new JSONArray());
            }
            JSONArray waitHandleInfo = userExtendInfo.getJSONArray(fileType);
            CmwaQualifiedUserInfoDto innerParam = new CmwaQualifiedUserInfoDto();
            innerParam.setInfoId(qualifiedUserInfoDto.getInfoId());
            innerParam.setUpdatedUser(currentUser);
            if ("insert".equalsIgnoreCase(operatorType)) {
            	logger.info("开始将key值为："+key+",文件名为："+fileName+"，文件类型为："+fileType+"的文件的数据添加到json数组中,custno:"+custno);
            	insertExtendInfo(waitHandleInfo, key, fileName);
            	logger.info("添加之后的json数组数据为："+userExtendInfo+",custno:"+custno);
                innerParam.setExtendInfo(userExtendInfo.toString());
            } else if ("delete".equalsIgnoreCase(operatorType)) {
                // 对extendInfo 中的json格式对象进行删除操作
                if (deleteExtendInfo(key, waitHandleInfo) != null){
                     // 删除s3 service上的文件
                     AmazonResult<String, String> amazonResult = amazonS3Manager.removeObjectByKey(key);
                     logger.info("删除文件结果为："+amazonResult);
                     
                     // 删除文件上传记录
                     CmwaFileUploadRecordDto parameter = new CmwaFileUploadRecordDto();
                     parameter.setCmfuserid(qualifiedUserInfoDto.getCmfuserid());
                     List<CmwaFileUploadRecordDto> queryFileUploadRecord = tradeServiceClient.queryFileUploadRecord(parameter);
                     
                     // 循环匹配文件上传记录 如果有匹配到的记录就删除掉
                     if(queryFileUploadRecord != null && queryFileUploadRecord.size() >= 1) {
                     	boolean isFindMatchesFile = false;
                     	for (int i = 0; i < queryFileUploadRecord.size(); i++) {
                     		CmwaFileUploadRecordDto temp = queryFileUploadRecord.get(i);
							if(key.equals(temp.getFileKey())) {
								isFindMatchesFile = true;
								this.deleteFileUploadRecord(temp.getRecordId());
							}
						}
                     	if(!isFindMatchesFile) {
                     		logger.warn("未找到对应的文件上传记录!!!");
                     	}
                     }
                	innerParam.setExtendInfo(userExtendInfo.toString());
                } else {
                	logger.info("未查询到对应文件信息");
                	returnObj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
                	returnObj.put("returnMsg", "未查询到对应文件信息");
                	return returnObj;
                }
            }
            
            // 更新数据库内对应信息
            logger.info("经过"+operatorType+"操作后，需要对更新至数据库的内容为:"+innerParam.getExtendInfo());
            int result = tradeServiceClient.updateQualifiedUserInfo(getTradeQualifiedDto(innerParam));
            logger.info("更新影响列为:"+result);
            if(result <= 0) {
            	logger.info("修改影响列异常！");
            	returnObj.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
            	returnObj.put("returnMsg","修改失败");
            } else { 
            	logger.info("修改成功！");
            	returnObj.put("returnCode",WXConstants.COMMON_SUCCESS);
            	returnObj.put("returnMsg","修改成功");
            }
            return returnObj;
        }catch(Exception e) {
            logger.error("修改已提交的合格投资者信息时捕获异常",e);
            returnObj.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
            returnObj.put("returnMsg","系统内部异常");
            return returnObj;
        }
	}
	
	
	private void insertExtendInfo(JSONArray waitHandleInfo, String key,
			String fileName) {
		boolean isExists = false;
		for (int i = 0; i < waitHandleInfo.size(); i++) {
			JSONObject object = waitHandleInfo.getJSONObject(i);
			if(key.equals(object.get("key"))) {
				isExists = true;
				break;
			}
		}
		if(isExists) {
			logger.info("已存在相同名称的文件，不对json数据进行添加操作");
		} else {
			JSONObject obj = new JSONObject();
			obj.put("key",key);
			obj.put("filename",fileName);
			logger.info("添加文件至json数组，参数为:"+obj);
			waitHandleInfo.add(obj);
		}
	}
	

	private JSONObject deleteExtendInfo(String key, JSONArray waitHandleInfo) {
        for (int i = 0;i<waitHandleInfo.size();i++ ) {
            JSONObject jsonObject = JSONObject.fromObject(String.valueOf(waitHandleInfo.get(i)));
            if(key.equals(String.valueOf(jsonObject.get("key")))) {
                waitHandleInfo.remove(i);
                return jsonObject;
            }
        }
        return null;
    }

	private QualifiedUserInfoDto queryNeedHandleExtendInfo(String custno,JSONObject userExtendInfo,JSONObject returnObj, String fileType) {
        QualifiedUserInfoDto queryResult = queryAccreditedInvestorInfoByCustno(custno);
        if(queryResult == null) {
            returnObj.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
            returnObj.put("returnMsg","未查询到合格投资者申请信息");
        } else{
            // 如果状态为U或者为R 则可以修改用户的上传文件
        	// 新增逻辑 如果是上传身份证照片且当前状态不为N则也可以修改用户的上传文件 wangz
        	String currentStatus = queryResult.getStatusRecord().getStatus();
        	if(WXConstants.QULIFIEDUSER_STATUS_U.equalsIgnoreCase(currentStatus) || WXConstants.QULIFIEDUSER_STATUS_R.equalsIgnoreCase(currentStatus)) {
                return queryResult;
            } else if((WXConstants.QUALIFIED_FILETYPE_IDCARDPORTRAIT.equalsIgnoreCase(fileType) 
            		|| WXConstants.QUALIFIED_FILETYPE_IDCARDNATIONALEMBLEM.equalsIgnoreCase(fileType)) 
            		&& !WXConstants.QULIFIEDUSER_STATUS_N.equalsIgnoreCase(currentStatus)) {
            	return queryResult;
            } else {
                logger.info("当前用户状态为:"+queryResult.getStatusRecord().getStatus()+"不执行更改操作");
                returnObj.put("returnCode","9999");
                returnObj.put("returnMsg","当前申请已经无法更改，请重新提交申请之后再进行修改");
                return null;
            }
        }
        return null;
    }
	
    
    /**
     * 通过custno查询合格投资者申请信息
     * @param custno
     * @return
     */
	private QualifiedUserInfoDto queryAccreditedInvestorInfoByCustno(String custno) {
        try{
            QueryMessageDto queryMessageDto = queryServiceClient.queryQualifiedUserInfoByCustno(custno);
            if(queryMessageDto == null) {
                logger.info("根据custno调用query服务查询返回结果为空! custno:"+custno);
            } else if(!WXConstants.COMMON_SUCCESS.equals(queryMessageDto.getResultCode())) {
                logger.info("根据custno调用query服务查询失败,custno:"+custno+",errCode:"+queryMessageDto.getErrCode()+",errMsg:"+queryMessageDto.getErrMsg());
            } else {
                logger.info("根据custno调用query服务成功！返回值为："+queryMessageDto);
                QualifiedUserInfoDto result = (QualifiedUserInfoDto) queryMessageDto.getData();
                return result;
            }
        }catch(Exception e) {
            logger.error("根据custno查询合格投资者信息时捕获异常：",e);
        }
        return null;
	}
	
	/**
	 * 通过文件上传记录获取一个初始的json数组
	 * @param list
	 * @return
	 */
	private JSONObject getExtendInfoByFileUploadRecord(List<CmwaFileUploadRecordDto> list) {
		JSONObject root = new JSONObject();
		boolean isNewArray = false;
		for (CmwaFileUploadRecordDto data : list) {
			String fileType = data.getFileType();
			JSONArray fileTypeArray = null;
			if(root.get(fileType) != null) {
				fileTypeArray = root.getJSONArray(fileType);
			} else {
				fileTypeArray = new JSONArray();
				isNewArray = true;
			}
			if(null == fileTypeArray) {
				fileTypeArray = new JSONArray();
				root.put(fileType,fileTypeArray);
			}
			JSONObject content = new JSONObject();
			content.put("key",data.getFileKey());
			content.put("filename",data.getFileName());
			fileTypeArray.add(content);
			if(isNewArray) {
				root.put(fileType, fileTypeArray);
			}
		}
		return root;
	}

	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.TradeManager#deleteFileUploadRecord(java.lang.String)
	 */
	@Override
	public JSONObject deleteFileUploadRecord(String recordId) {
		JSONObject jsonObject = new JSONObject();
        try{
            if(StringUtils.isBlank(recordId)) {
                jsonObject.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
                jsonObject.put("returnMsg","参数丢失");
                return jsonObject;
            }
            jsonObject.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
            CmwaFileUploadRecordDto parameter = new CmwaFileUploadRecordDto();
            parameter.setRecordId(recordId);
            List<CmwaFileUploadRecordDto> cmwaFileUploadRecordDtos = tradeServiceClient.queryFileUploadRecord(parameter);
            if(cmwaFileUploadRecordDtos != null && cmwaFileUploadRecordDtos.size() > 0) {
                CmwaFileUploadRecordDto cmwaFileUploadRecordDto = cmwaFileUploadRecordDtos.get(0);
                int result = tradeServiceClient.deleteFileUploadRecord(recordId);
                logger.info("删除影响列为:"+result+",recordId:"+recordId);
                if(result == 1) {
                    jsonObject.put("returnCode", WXConstants.COMMON_SUCCESS);
                    jsonObject.put("returnMsg", "删除成功");
                    AmazonResult amazonResult = tradeServiceClient.removeObjectByKey(cmwaFileUploadRecordDto.getFileKey());
                    logger.info("删除amazon服务器上对应文件的返回结果为：" + amazonResult);
                } else {
                    logger.info("删除影响列异常，请检查数据，recordId:"+recordId+",影响列为："+result);
                    jsonObject.put("returnMsg","删除失败");
                }
            } else {
                logger.info("未查询到对应记录，不执行删除");
                jsonObject.put("returnMsg","未查询到对应记录");
            }
            return jsonObject;
        } catch(Exception e) {
            logger.error("调用trade服务删除文件上传记录时捕获异常:",e);
            jsonObject.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
            jsonObject.put("returnMsg","系统内部异常");
        }
        return jsonObject;
	}
	
	
	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.TradeManager#updatedAccertiedInvestorRequestStatus(java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject updatedAccreditedInvestorRequestStatus(String custno,String updateStatus,UserBaseInfoDto currentUserInfo) {
		JSONObject obj = new JSONObject();
		if(StringUtils.isBlank(custno) || (StringUtils.isBlank(updateStatus) || updateStatus.length() != 1)) {
			obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
			obj.put("returnMsg", WXConstants.RETURN_MSG_9008);
			return obj;
		}
		if(!WXConstants.QULIFIEDUSER_STATUS_N.equals(updateStatus) && !WXConstants.QULIFIEDUSER_STATUS_U.equals(updateStatus) && !WXConstants.QULIFIEDUSER_STATUS_R.equals(updateStatus)) {
			obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
			obj.put("returnMsg", "错误的状态码");
			return obj;
		}
		QualifiedUserInfoDto accfreditedInvestorInfo = this.queryAccreditedInvestorInfoByCustno(custno);
		if (accfreditedInvestorInfo == null) {
			obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
			obj.put("returnMsg", "未查询到合格投资者申请");
		} else {
			String currentStatus = accfreditedInvestorInfo.getStatusRecord().getStatus();
			logger.info("当前用户状态为："+currentStatus+",custno:"+custno+",需要修改的状态为："+updateStatus);
			// 针对需要修改的状态做判断
			if(WXConstants.QULIFIEDUSER_STATUS_N.equals(updateStatus)) {
				// 客户状态由未提交至确认状态 U -> N(确认提交)
				if(WXConstants.QULIFIEDUSER_STATUS_U.equals(currentStatus) || WXConstants.QULIFIEDUSER_STATUS_R.equals(currentStatus)
						|| WXConstants.QULIFIEDUSER_STATUS_S.equals(currentStatus)) {
					executeUpdateStatus(custno, updateStatus, obj,
							accfreditedInvestorInfo,currentUserInfo);
				} else {
					logger.info("当前用户状态为："+currentStatus+",不可转变为需要修改的状态:"+updateStatus);
					obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
					obj.put("returnMsg", "状态不匹配");
				}
			} else if (WXConstants.QULIFIEDUSER_STATUS_U.equals(updateStatus) || WXConstants.QULIFIEDUSER_STATUS_R.equals(updateStatus)) {
				// 客户状态由审核失败至未确认状态 F -> U (重新审核)
				if(WXConstants.QULIFIEDUSER_STATUS_F.equals(currentStatus)) {
					executeUpdateStatus(custno, updateStatus, obj,accfreditedInvestorInfo,currentUserInfo);
				} else {
					logger.info("当前用户状态为："+currentStatus+",不可转变为需要修改的状态:"+updateStatus);
					obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
					obj.put("returnMsg", "状态不匹配");
				}
			}
		}
		return obj;
	}
	private void executeUpdateStatus(String custno, String updateStatus,
			JSONObject obj, QualifiedUserInfoDto accfreditedInvestorInfo,UserBaseInfoDto currentUserInfo) {
		int executeResult;
		try {
			executeResult = tradeServiceClient.updateQualifiedUserInfoStatus(getTradeQualifiedStatusDto(accfreditedInvestorInfo.getStatusRecord()),updateStatus, custno);
		
			if(executeResult <= 0) {
				logger.info("修改与影响列异常，修改失败! custno:"+custno+",statusInfo:"+accfreditedInvestorInfo.getStatusRecord()+",updateStatus:"+updateStatus);
			} else {
				obj.put("returnCode", WXConstants.COMMON_SUCCESS);
				obj.put("returnMsg", "修改成功");
				String operatorType = "";
				String currentStatus = accfreditedInvestorInfo.getStatusRecord().getStatus();
				
				if(WXConstants.QULIFIEDUSER_STATUS_U.equals(currentStatus) && WXConstants.QULIFIEDUSER_STATUS_N.equals(updateStatus)) {
					// 如果用户当前状态为U转向N 则操作类型为确认申请
					operatorType = "04";
				} else if (WXConstants.QULIFIEDUSER_STATUS_F.equals(currentStatus) && WXConstants.QULIFIEDUSER_STATUS_R.equals(updateStatus)) {
					// 如果用户当前状态为F转向R 则操作类型为重新填写资料
					operatorType = "05";
				} else if (WXConstants.QULIFIEDUSER_STATUS_R.equals(currentStatus) && WXConstants.QULIFIEDUSER_STATUS_N.equals(updateStatus)) {
					// 如果用户当前状态为R转向N 则操作类型为重新审核提交
					operatorType = "06";
				} else if (WXConstants.QULIFIEDUSER_STATUS_S.equals(currentStatus) && WXConstants.QULIFIEDUSER_STATUS_N.equals(updateStatus)) {
					// 如果用户当前状态为S转向N 则操作类型为重新审核
					operatorType = "08";
				}
				userServiceClient.insertUserOperateLog(getOperateLogDto(currentUserInfo, operatorType));
			}
		} catch (Exception e) {
			logger.error("修改用户合格投资者状态时捕获异常：",e);
			obj.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
			obj.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
		}
	}
	
	public UserOperateLogDto getOperateLogDto(UserBaseInfoDto userInfo,String operatorType) {
		UserOperateLogDto dto = new UserOperateLogDto();
		dto.setCmfuserid(userInfo.getCmfUserId());
		dto.setLoginChannel(WXConstants.LOGIN_NMARK_03);
		dto.setType(operatorType);
		return dto;
	}
	
	private boolean checkUserSatisfyCondition(String custno) {
		QueryMessageDto result = queryServiceClient.queryAccreditedInvestorConditions(custno);
		if(result == null || !WXConstants.COMMON_SUCCESS_CODE.equals(result.getResultCode())) {
			return false;
		}
		Map<String,Object> conditionCheckResult = (Map<String,Object>) result.getOtherData();
		if(conditionCheckResult == null) {
			return false;
		}
		boolean financialCertificate = Boolean.valueOf(String.valueOf(conditionCheckResult.get("financialCertificate")));
		boolean investCertificate = Boolean.valueOf(String.valueOf(conditionCheckResult.get("investCertificate")));
		if(financialCertificate && investCertificate) {
			return true;
		} else {
			return false;
		}
	}
	
	private com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoStatusDto getTradeQualifiedStatusDto (CmwaQualifiedUserInfoStatusDto source) throws IllegalAccessException, InvocationTargetException {
		com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoStatusDto target = new com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoStatusDto();
		BeanUtils.copyProperties(target, source);
		return target;
	}
	
	private com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoDto getTradeQualifiedDto (CmwaQualifiedUserInfoDto source) throws IllegalAccessException, InvocationTargetException {
		com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoDto target = new com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoDto();
		BeanUtils.copyProperties(target, source);
		return target;
	}
	
	@Override
	public JSONObject saveUserTermsInfo(Context context,String cmfUserId,String fundId,String period,String ids,String type) {
		logger.info("调用saveUserTermsInfo保存客户风险揭示书勾选纪录信息>>>start>>>入参:[cmfUserId:" +cmfUserId + ",fundId:" +fundId + ",period:" +period + ",ids:" + ids + ",type:" + type + "]");
		JSONObject jsonObject = new JSONObject();
        try{
        	List<String> list = Arrays.asList(ids.split(","));
        	if(null != list && !list.isEmpty()){
        		actParameterDao.deleteUserTerms(fundId, period, cmfUserId);
        		List<UserTermsDto> terms = new ArrayList<UserTermsDto>();
        		for(String id : list){
        			UserTermsDto term = new UserTermsDto();
        			UUIDKeyGenerator u = new UUIDKeyGenerator();
        			term.setId(u.getRandomNum(8));
        			term.setFundCode(fundId);
        			term.setCmfUserId(cmfUserId);
        			term.setPeriod(Integer.valueOf(period));
        			term.setState("1");
        			term.setTermsId(id);
        			terms.add(term);
        		}
        		actParameterDao.insertUserTerms(terms);
        		if("submit".equals(type)){
        			Map<String, Object> map = new HashMap<String, Object>();
        			map.put("cmfuserid", cmfUserId);
        			map.put("loginChannel", "04");
        			map.put("type", "07");
        			map.put("fundId", fundId);
        			map.put("period", period);
        			actParameterDao.insertUserOperateLog(map);
        		}
        		jsonObject.put("returnCode",WXConstants.COMMON_SUCCESS);
                jsonObject.put("returnMsg",WXConstants.COMMON_SUCCESS_MSG);
        	}else{
        		 jsonObject.put("returnCode",WXConstants.RETURN_CODE_9008);
                 jsonObject.put("returnMsg",WXConstants.RETURN_MSG_9008);
        	}
        } catch(Exception e) {
            logger.error("调用trade服务保存客户风险揭示函信息记录时捕获异常:",e);
            jsonObject.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
            jsonObject.put("returnMsg",WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        logger.info("调用saveUserTermsInfo保存客户风险揭示书勾选纪录信息>>>end>>>出参:" + jsonObject.toString());
        return jsonObject;
	}

	@Override
	public JSONObject revokeRedeemOrder(Context context, String serialNo, String custNo) {
		logger.info("调用revokeRedeemOrder撤销赎回订单方法>>>start>>>入参:[serialNo:" +serialNo +",custNo"+ custNo +"]");
		JSONObject jsonObject = new JSONObject();
		try {
			FundTradeDto dto = new FundTradeDto();
			dto.setSerialNo(serialNo);
			dto.setCustNo(custNo);
			dto.setApplyst("W");
			OrderResult result = tradeServiceClient.revokeRedeemOrder(dto);
			jsonObject.put("returnCode",result.getResultCode());
         	jsonObject.put("returnMsg",result.getResultMsg());
		} catch (Exception e) {
			logger.error("调用revokeRedeemOrder撤销赎回订单方法捕获异常:",e);
            jsonObject.put("returnCode",WXConstants.COMMON_ERROR_SYSERRCODE);
            jsonObject.put("returnMsg",WXConstants.COMMON_ERROR_SYSERRMSG);
		}
		logger.info("调用revokeRedeemOrder撤销赎回订单方法>>>start>>>入参:[serialNo:" +serialNo +"]");
		return jsonObject;
	}
}
