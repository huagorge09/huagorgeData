package com.cmwa.ec.webapp.manager.impl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.dto.MsgParameterDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.dto.mail.MailConfig;
import com.cmwa.ec.message.facade.dto.mail.MailMessage;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.fund.FundInfoDtoV2;
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
import com.cmwa.ec.trade.facade.dto.bank.BocSignParaDto;
import com.cmwa.ec.trade.facade.dto.bank.CmbSignParaDto;
import com.cmwa.ec.trade.facade.dto.bank.IcbcSignParaDto;
import com.cmwa.ec.trade.facade.dto.fund.FundTradeDto;
import com.cmwa.ec.trade.facade.dto.fund.SignEContractDto;
import com.cmwa.ec.trade.facade.dto.log.CmwaFileUploadRecordDto;
import com.cmwa.ec.trade.facade.dto.user.UserAcctDto;
import com.cmwa.ec.trade.facade.dto.user.UserRiskLevelDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserOperateLogDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.webapp.client.MessageServiceClient;
import com.cmwa.ec.webapp.client.QueryServiceClient;
import com.cmwa.ec.webapp.client.TradeServiceClient;
import com.cmwa.ec.webapp.client.UserServiceClient;
import com.cmwa.ec.webapp.dao.ParameterDao;
import com.cmwa.ec.webapp.manager.AmazonS3Manager;
import com.cmwa.ec.webapp.manager.MessageManager;
import com.cmwa.ec.webapp.manager.QueryManager;
import com.cmwa.ec.webapp.manager.TradeManager;
import com.cmwa.ec.webapp.util.DateUtils;
import com.cmwa.ec.webapp.util.ECConstants;
import com.cmwa.ec.webapp.util.GetIPUtils;
import com.cmwa.ec.webapp.util.MD5;
import com.cmwa.ec.webapp.util.RequestHelper;
import com.cmwa.ec.webapp.util.SessionValue;
import com.cmwa.ec.webapp.util.UUIDKeyGenerator;
import com.cmwa.ec.webapp.util.cache.ParameterCache;

public class TradeManagerImpl implements TradeManager{
	private static Logger logger = Logger.getLogger(TradeManagerImpl.class.getName());
	@Autowired
	private TradeServiceClient tradeServiceClient;
	@Autowired
	private UserServiceClient userServiceClient;
	@Autowired
	private MessageServiceClient messageServiceClient;
	@Autowired
	private QueryServiceClient queryServiceClient;
	@Autowired
	private MessageManager messageManager;
	@Autowired
	private QueryManager queryManager;
	
	@Autowired
	private ParameterDao parameterDao;
	
	@Autowired
	private AmazonS3Manager amazonS3Manager;
	
	@Override
	public OrderResult fundAppoint(Context context, FundTradeDto fundTradeDto) {
		OrderResult orderResult = tradeServiceClient.fundAppoint(context,fundTradeDto);
		return orderResult;
	}
	@Override
	public OrderResult fundTrade(Context context, FundTradeDto fundTradeDto, SignEContractDto elDto) {
		OrderResult orderResult = tradeServiceClient.fundTrade(context,fundTradeDto,elDto);
		tradeServiceClient.updateAppointReqCommro(fundTradeDto);
		return orderResult;
	}
	/**
	 * 用户信息鉴权/开户
	 */
	@Override
	public JSONObject openAccount(Context context, HttpServletRequest request) {
		JSONObject returnJsonObject = new JSONObject();
		JSONObject userAcctJson = null;
		UserBaseInfoDto userInfo = RequestHelper.getSessionUserBaseInfo(request);
		Object obj2 = request.getSession(true).getAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE);
		if (userInfo == null) {
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_8000);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_8000);
			return returnJsonObject;
		}
		if (obj2 == null) {
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_9003);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_9003);
			return returnJsonObject;
		}
		String cmfUserId = userInfo.getCmfUserId();
		String mobile = obj2.toString();
		String userName = request.getParameter("name");
		String idNo = request.getParameter("idNo");
		String idType = "0";// 证件类型 ：身份证 0
		String bankNumber = request.getParameter("bankNumber");
		String bankName = request.getParameter("bankName");
		logger.info("TradeManagerImpl类【openAccount】开始>>>cmfUserId:" + cmfUserId + ">>>mobile:" + mobile + ">>>userName:" + userName + ">>>idNo:" + idNo + ">>>idType:" + idType + ">>>bankNumber:" + bankNumber + ">>>bankName:" + bankName + ">>>timestamp:" + System.currentTimeMillis());
		try {
			userName = URLDecoder.decode(userName, "utf-8");
			bankName = URLDecoder.decode(bankName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			logger.error("TradeManagerImpl类【openAccount】编码转换出现错误>>>>>>>TradeManagerImpl>>>>>>>>>openAccount");
		}
		String channelNo = request.getParameter("channelNo");// 渠道代码
		String bankNo = request.getParameter("bankNo");// 银行代码
		String idExpireDate = request.getParameter("idExpireDate");//证件过期日
		UserServiceMessage queryUserAndAccoRlaById = userServiceClient.queryUserAndAccoRlaById(context, cmfUserId);
		UserBaseInfoDto userBaseInfoDto = queryUserAndAccoRlaById.getUserBaseInfoDto();
		
		UserAcctDto userAcctDto = new UserAcctDto();
		userAcctDto.setCmfUserId(cmfUserId);
		userAcctDto.setInvName(userName);
		userAcctDto.setBankMobile(mobile);
		userAcctDto.setIdNo(idNo);
		userAcctDto.setIdType(idType);
		userAcctDto.setBankName(bankName);
		userAcctDto.setBankAcct(bankNumber);
		userAcctDto.setChannelNo(channelNo);
		userAcctDto.setBankNo(bankNo);
		userAcctDto.setAddr(userBaseInfoDto.getNationNM()==null?"":userBaseInfoDto.getNationNM()+userBaseInfoDto.getProvinceNM()==null?"":userBaseInfoDto.getProvinceNM()+userBaseInfoDto.getCityNM()==null?"":userBaseInfoDto.getCityNM()+userBaseInfoDto.getAddr()==null?"":userBaseInfoDto.getAddr());
		if (userAcctDto.getAddr()==null || "".equals(userAcctDto.getAddr().trim())) {
			userAcctDto.setAddr("**");
		}
		userAcctDto.setVoccode(userBaseInfoDto.getVocCode());
		if (userAcctDto.getVoccode()==null || "".equals(userAcctDto.getVoccode().trim())) {
			userAcctDto.setVoccode(ECConstants.VOCCODE_15);
		}
		userAcctDto.setIdExpireDate(StringUtils.isBlank(idExpireDate) ? null : idExpireDate.replaceAll("-", "").trim());
		String returnCode = null;
		String returnMsg = null;
		logger.info("TradeManagerImpl类【openAccount】调用 ---openAccount()--start ---  接口");
		OrderResult orderResult = tradeServiceClient.openAccount(context, userAcctDto);
		logger.info("TradeManagerImpl类【openAccount】调用 ---openAccount()--end ---  接口");
		if (null != orderResult) {
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
			logger.info("TradeManagerImpl类【openAccount】调用用户信息鉴权，returnCode：" + returnCode + ", returnMsg:" + returnMsg);
			// 修改或者插入成功
			if ("0000".equals(orderResult.getResultCode())) {
				// 将session中用户账户对应表中的ecCustNo更新
				UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
				UserBaseInfoDto userBaseInfo = RequestHelper.getSessionUserBaseInfo(request);
				Object obj = orderResult.getData();
				if (obj != null) {
					UserAcctDto temp = (UserAcctDto) obj;
					logger.info("TradeManagerImpl类【openAccount】用户信息鉴权 返回数据 userName:" + temp.getInvName());
					logger.info("TradeManagerImpl类【openAccount】用户信息鉴权 返回数据 eccustNo:" + temp.getEcCustNo());
					logger.info("TradeManagerImpl类【openAccount】用户信息鉴权 返回数据:" + temp.toString());
					if (temp.getEcCustNo() != null && !"".equals(temp.getEcCustNo())) {
						if (userAccoRla != null) {
							userAccoRla.setEcCustNo(temp.getEcCustNo());
						} else {
							userAccoRla = new UserAccoRlaDto();
							userAccoRla.setCmfUserId(userAcctDto.getCmfUserId());
							userAccoRla.setEcCustNo(temp.getEcCustNo());
						}
						request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userAccoRla);
					}
					if (temp.getInvName() != null && !"".equals(temp.getInvName())) {
						if (userBaseInfo != null) {
							userBaseInfo.setCustName(temp.getInvName());
							userBaseInfo.setIdNo(temp.getIdNo());
							userBaseInfo.setUserType("30");
							request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
						}
					}
					userAcctJson = JSONObject.fromObject(temp);
					userAcctJson.put("ecCustNo", "");
				}
			}
			if("CMB01".equals(orderResult.getResultCode())){
				Object obj = orderResult.getData();
				if (obj != null) {
					CmbSignParaDto cmb = (CmbSignParaDto)obj;
					returnJsonObject.put("cmbDto", cmb);
				}
			}
			if ("9129".equals(orderResult.getResultCode())) {// 若返回代码为9129，则是重复开户:查询账户信息，补充session，返回成功。
				returnCode = queryUserAccoRla(context, userAcctDto.getCmfUserId(), request);
			}
		} else {
			returnCode = ECConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = ECConstants.COMMON_ERROR_SYSERRMSG;
		}
		String userType = RequestHelper.getSessionUserBaseInfo(request).getUserType();
		String isSetTradePassword = RequestHelper.getTPsdStatus((UserBaseInfoDto)RequestHelper.getSessionUserBaseInfo(request));
		returnJsonObject.put("cmfUserIdIsNull", "no");
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		returnJsonObject.put("userType", userType);
		returnJsonObject.put("tradePasswordStatus", isSetTradePassword);
		returnJsonObject.put("userAcctDto", userAcctJson);
		logger.info("TradeManagerImpl类【openAccount】结束>>>returnJsonObject:" + returnJsonObject + ">>>timestamp:" + System.currentTimeMillis());
		return returnJsonObject;
	}
	
	//查询用户已鉴权,但是没有往session里没有值
	private String queryUserAccoRla(Context context, String cmfUserId, HttpServletRequest request) {
		logger.info("TradeManagerImpl类【queryUserAccoRla】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());
		// 根据cmfUserId查询用户信息，确认用户类型 （注册用户、鉴权用户）
		UserServiceMessage userServiceMessage = userServiceClient.queryUserAndAccoRlaById(context, cmfUserId);
		String retrunCode = "-1";// 返回码
		if (null != userServiceMessage && "0000".equals(userServiceMessage.getReturnCode())) {
			// 将session中用户账户对应表中的ecCustNo更新
			UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
			UserBaseInfoDto userBaseInfo = RequestHelper.getSessionUserBaseInfo(request);
			if (null != userServiceMessage.getUserAccoRlaDto()) {
				UserAccoRlaDto acctRlaDto = userServiceMessage.getUserAccoRlaDto();
				UserBaseInfoDto baseInfoDto = userServiceMessage.getUserBaseInfoDto();
				// 用户账户
				logger.info("再次鉴权返回数据 eccustNo:" + acctRlaDto.getEcCustNo());
				if (null != acctRlaDto.getEcCustNo() && !"".equals(acctRlaDto.getEcCustNo())) {
					if (null != userAccoRla) {// 判断session的值
						userAccoRla.setEcCustNo(acctRlaDto.getEcCustNo());
					} else {
						userAccoRla = new UserAccoRlaDto();
						userAccoRla.setCmfUserId(cmfUserId);
						userAccoRla.setEcCustNo(acctRlaDto.getEcCustNo());
					}
					request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userAccoRla);
				}

				if (null != baseInfoDto) {// 用户信息
					logger.info("再次鉴权返回数据 userName:" + baseInfoDto.getCustName());
					if (null != baseInfoDto.getCustName() && !"".equals(baseInfoDto.getCustName())) {
						if (userBaseInfo != null) {// 判断session的值
							userBaseInfo.setCustName(baseInfoDto.getCustName());
							userBaseInfo.setIdNo(baseInfoDto.getIdNo());
							;
							userBaseInfo.setUserType("30");
							request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
						}
					}
				}
				retrunCode = "0000";
			}

		}
		logger.info("TradeManagerImpl类【queryUserAccoRla】结束>>>retrunCode:" + retrunCode + ">>>timestamp:" + System.currentTimeMillis());
		return retrunCode;
	}

	@Override
	public JSONObject cancelAppointRequest(Context context, HttpServletRequest request) {

		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";

		String serialno = request.getParameter("serialno");
		String tradeacco = request.getParameter("tradeacco");

		logger.info("TradeManagerImpl类【cancelAppointRequest】开始>>>cmfUserId:" + context.getCmfUserId() + ">>>serialno:" + serialno + ">>>tradeacco:" + tradeacco + ">>>timestamp:" + System.currentTimeMillis());

		String custno = "";
		UserAccoRlaDto userAccoRlaDto = null;
		Object obj = RequestHelper.getSessionAttribute(request, SessionValue.SESSION_USERACCORLA);
		if(obj != null){
			userAccoRlaDto = (UserAccoRlaDto) obj;
		}
		if(userAccoRlaDto != null){
			custno = userAccoRlaDto.getEcCustNo();
		}
		
		int tradeAccCount = userServiceClient.queryTradeaccoExistCount(tradeacco);
		
		if(null != ""+tradeAccCount && tradeAccCount <= 0){
			jsonObject.put("returnCode", "9999");
			jsonObject.put("returnMsg", "交易账号不存在！");
			return jsonObject;
		}
		
		FundTradeDto fundTradeDto = new FundTradeDto();
		fundTradeDto.setSerialNo(serialno);
		fundTradeDto.setTradeAcco(tradeacco);
		fundTradeDto.setCustNo(custno);
		
		OrderResult orderResult = tradeServiceClient.cancelAppointRequest(context, fundTradeDto);
		if (orderResult != null) {
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);

		logger.info("TradeManagerImpl类【cancelAppointRequest】结束>>>jsonObject:" + jsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());

		return jsonObject;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject redemptionOrder(Context context,
			HttpServletRequest request,FundTradeDto fundTradeDto) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "9999";
		String returnMsg = "系统异常";
		String fundname = request.getParameter("fundname");
		String paymentinter = request.getParameter("paymentinter");
		String bankCardNo = request.getParameter("bankCardNo");
		logger.info("TradeManagerImpl类【redemptionOrder】开始>>>cmfUserId:" + context.getCmfUserId() + ">>>serialno:" + fundTradeDto.getSerialNo() + ">>>timestamp:" + System.currentTimeMillis());
		OrderResult orderResult = tradeServiceClient.redemptionOrder(context, fundTradeDto);
		if (orderResult != null && "0000".equals(orderResult.getResultCode())) {
			JSONObject data = JSONObject.fromObject(orderResult.getData());
			// 正常赎回才发送成功短信和邮件
			if(ECConstants.APKIND_024.equals(data.getString("apkind"))){
				String redSerialno = data.getString("serialNo");
			    MsgParameterDto msgParameter = new MsgParameterDto();
			    msgParameter.setMobile(fundTradeDto.getMobileNo());
				msgParameter.setMsgType("40");
				msgParameter.setMsgPar_bankCard(bankCardNo);
				msgParameter.setMsgPar_fundName(fundname);
			    msgParameter.setMsgPar_str1(paymentinter);
			    String serialno = fundTradeDto.getSerialNo();
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
			    
			    //活期产品微信端赎回成功时模板消息插入mecc
			    try {
			    	logger.info("活期产品微信端赎回成功时模板消息插入mecc》》》start");
			    	QueryMessageDto queryParamList = queryManager.queryHomeAddressIsWordWithLinkage(context, "SYSTEM", "REDEMPTPUSHMSG", null, null);
			    	List<ParameterDto> listPara = (List<ParameterDto>) queryParamList.getData();
			    	ParameterDto param = null;
			    	if(null != listPara && listPara.size() >0){
			    		param = listPara.get(0);
					    String content = param.getPmv1().
					    		replace("#APDT",DateUtils.formatDate(new Date(), "yyyy年MM月dd日")).
					    		replace("#T", paymentinter).replace("#FUNDNAME", fundname).replace("#SERIALNO", redSerialno);
			    		CmfWXPushmessageDto dto = new CmfWXPushmessageDto();
			    		dto.setMessageId(redSerialno);
						dto.setCmfUserId(context.getCmfUserId());
						dto.setState("0");
						dto.setMessageType("templateMessage");
						dto.setTemplate_id(param.getPmco());
						dto.setContent(content);
					    tradeServiceClient.addCmfWXPushmessage(dto);
					    logger.info("活期产品微信端赎回成功时模板消息插入mecc》》》end");
					    logger.info("活期产品微信端赎回成功时发送邮件》》》start");
						QueryMessageDto resultMap = queryServiceClient.queryRedeemContentList(redSerialno);
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
					logger.error("插入mecc消息模板失败",e);
				}
			}
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
		}else{
			try {
				AppointRequestDto appoint = new  AppointRequestDto();
				String serialno = fundTradeDto.getSerialNo().split(",")[0];
				appoint.setApdt(DateUtils.formatDate(new Date(), "yyyy年MM月dd日"));
				QueryMessageDto queryParams =  queryServiceClient.QueryAppointRequestList(serialno);
				List<AppointRequestDto> appList = (List<AppointRequestDto>)queryParams.getData();
				if(null != appList && appList.size() > 0){
					FundInfoDtoV2  dto = new FundInfoDtoV2();
					dto.setAdname(fundname);
					appoint = appList.get(0);
					appoint.setMobile(fundTradeDto.getMobileNo());
					appoint.setSubamt(fundTradeDto.getTradeAmt());
					appoint.setSubquty(fundTradeDto.getTradeAmt());
					appoint.setFundInfoDtoV2(dto);
					appoint.setApdt(DateUtils.formatDate(new Date(), "yyyy年MM月dd日"));
				}
				sendFundTradeMail(context,appoint);
			} catch (Exception e) {
				logger.error("操作表变化时发送邮件提醒用户审批异常",e);
			}
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("TradeManagerImpl类【redemptionOrder】结束>>>jsonObject:" + jsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}
	/**
	 * 撤销已绑定的银行卡
	 * @param context
	 * @param userAcctDto
	 * @return
	 */
	@Override
	public JSONObject cancelBindBankCard(Context context, HttpServletRequest request, String tradeAcco) {
		JSONObject returnJsonObject = new JSONObject();
		String returnCode = null;
		String returnMsg = null;
		
		String custno = "";
		UserAccoRlaDto userAccoRlaDto = null;
		Object obj = RequestHelper.getSessionAttribute(request, SessionValue.SESSION_USERACCORLA);
		if(obj != null){
			userAccoRlaDto = (UserAccoRlaDto) obj;
		}
		if(userAccoRlaDto != null){
			custno = userAccoRlaDto.getEcCustNo();
		}
		
		UserAcctDto userAcctDto = new UserAcctDto();
		userAcctDto.setTradeAcct(tradeAcco);
		userAcctDto.setEcCustNo(custno);
		logger.info("调用 ---cancelBindBankCard()--start ---  接口 ,tradeAcco = "+tradeAcco);
		OrderResult orderResult = tradeServiceClient.cancelBindBankCard(context, userAcctDto);
		logger.info("调用 ---cancelBindBankCard()--end ---  接口");
		
		if(null!=orderResult){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
			logger.info("解除绑定银行卡，returnCode："+returnCode+", returnMsg:"+returnMsg);
		}else{
			returnCode = ECConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = ECConstants.COMMON_ERROR_SYSERRMSG;
		}
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		return returnJsonObject;
	}

	@Override
	public OrderResult addReportReadRecord(Context context, String cmfUserId, String fundId, String reportId) {
		OrderResult orderResult = tradeServiceClient.addReportReadRecord(context, cmfUserId, fundId, reportId);
		return orderResult;
	}

	@Override
	public JSONObject fundTradeLineUp(Context context, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		String custNo = "";
		if (userAccoRla != null) {
			custNo = userAccoRla.getEcCustNo();
		}
		String tradeAcco = request.getParameter("tradeAcco");
		String payMobile = request.getParameter("payMobile");
		String mobile = request.getParameter("mobile");
		String tpassword = request.getParameter("tPassword");

		String fundId = request.getParameter("fundId");
		String serialNo = request.getParameter("serialno");
		String fundState = request.getParameter("fundState");
		String serType = request.getParameter("serType");

		String tradeAmt = request.getParameter("tradeAmt");
		tradeAmt = URLDecoder.decode(tradeAmt, "utf-8");
		String fee = request.getParameter("fee");
		String commro = request.getParameter("commro");
		String renew = request.getParameter("renew");

		String payType = request.getParameter("payType");
		String channelId = ECConstants.TRADE_CHANEL_01;

		String contractVer = request.getParameter("contractVer");

		String rvrfcode = request.getParameter("verifyCode");
		String sessionID = request.getParameter("sessionID");

		String apkind = "";

		/**
		 * 当页面传递过来的serType有效，且： serType == "A2T",apkind = "A2T" serType ==
		 * "UPT",apkind = "UPT"
		 * 
		 * 否则 apkind = FundTradeDto.getApkind(apkind.charAt(0));
		 */
		if (serType != null && serType.equals("A2T")) {// 预约类型订单选择支付方式和银行卡
			apkind = "A2T";
		} else if (serType != null && serType.equals("UPT")) {// 预约类型订单修改订单金额
			apkind = "UPT";
		} else if (serType != null && serType.equals("CNL")) {
			apkind = "CNL";
		} else {
			apkind = FundTradeDto.getApkind(fundState.charAt(0));
		}

		logger.info("TradeManagerImpl类【fundTradeLineUp】开始>>>cmfUserId:" + cmfUserId + ">>>custNo:" + custNo + ">>>tradeAcco:" + tradeAcco + ">>>mobile:" + mobile + ">>>fundId:" + fundId
				 + ">>>serialNo:" + serialNo + ">>>fundState:" + fundState + ">>>serType:" + serType + ">>>tradeAmt:" + tradeAmt+ ">>>fee:" + fee + ">>>commro:" + commro
				 + ">>>payType:" + payType + ">>>channelId:" + channelId + ">>>contractVer:" + contractVer + ">>>rvrfcode:" + rvrfcode+ ">>>sessionID:" + sessionID + ">>>apkind:" + apkind
				 + ">>>timestamp:" + System.currentTimeMillis());

		MD5 md5 = new MD5();
		if (tpassword != null && !tpassword.equals("")) {
			tpassword = md5.getMD5ofStr(tpassword);
		}

		/***************************** 线上支付 需要验证短信验证码 S ****************************************************/
		if (payType != null && payType.equals("0")) {
			String mobileByMsg = (String) request.getSession(true).getAttribute("mobileByMsg");
			if (mobileByMsg.equals(payMobile)) {// 验证短信验证码
				if (rvrfcode != null && !rvrfcode.equals("")) {
					rvrfcode = rvrfcode.trim();
				}
				MsgServiceMessageDto msgServicedto = messageServiceClient.checkVrfCode(context, sessionID, mobile, rvrfcode);
				if (msgServicedto != null) {
					if (msgServicedto.getReturnCode() == null || !msgServicedto.getReturnCode().equals("0000")) {
						jsonObject.put("resultCode", "6001");
						jsonObject.put("resultMsg", msgServicedto.getReturnMsg());
						logger.info("TradeController类【fundTrade】中验证短信验证码失败》》》入参：sessionID" + sessionID + "》》》mobile" + mobile + "》》》rvrfcode" + rvrfcode + "》》》返回信息：returnJsonObject=" + jsonObject);
						return jsonObject;
					}
				} else {
					jsonObject.put("resultCode", "9999");
					logger.info("TradeController类【fundTrade】中验证短信验证码失败》》》入参：sessionID" + sessionID + "》》》mobile" + mobile + "》》》rvrfcode" + rvrfcode + "》》》返回信息：returnJsonObject=" + jsonObject);
					return jsonObject;
				}

			} else {
				jsonObject.put("resultCode", "0099");
				jsonObject.put("resultMsg", "请重新获取并验证手机短信验证码");
				logger.info("TradeController类【fundTrade】中验证短信验证码失败：没有获取到验证手机号码");
				return jsonObject;
			}
		}
		/***************************** 线上支付 需要验证短信验证码 E ****************************************************/

		/***************************** 验证支付密码 S ****************************************************/
		String manageType = "V";
		UserServiceMessage userServiceMessage = userServiceClient.manageTpassword(context, cmfUserId, tpassword, "", manageType, "01", "01");

		UserBaseInfoDto userBaseInfoDto = null;
		Integer tPwdErrCount = 0;
		if (userServiceMessage != null) {
			String resultCode = userServiceMessage.getReturnCode();
			logger.info("【【【【【userServiceMessage】】】】】" + userServiceMessage.getUserBaseInfoDto() + "-----" + userServiceMessage.getReturnCode() + "-----" + userServiceMessage.getResultMsg());
			;

			if (resultCode != null && resultCode.equals("USR-1I00")) {

			} else {
				jsonObject.put("resultCode", resultCode);
				if (resultCode.equals("USR-1I01")) {// 用户已锁定--
													// 您输入密码错误次数过多，请3个小时之后重试
					jsonObject.put("resultMsg", "您输入密码错误次数过多，请3个小时之后重试");
				} else if (resultCode.equals("USR-1I02")) {// 旧密码错误，但未达到错误次数上限
					jsonObject.put("resultMsg", "旧密码错误，但未达到错误次数上限");
				} else {// 您输入的支付密码有误，请重新输入
					jsonObject.put("resultMsg", "您输入的支付密码有误，请重新输入");
					jsonObject.put("resultCode", "6000");
				}

				userBaseInfoDto = userServiceMessage.getUserBaseInfoDto();
				if (userBaseInfoDto != null) {// 支付密码错误次数
					tPwdErrCount = userBaseInfoDto.getTPwdErrCount();
				}
				// returnJsonObject.put("resultMsg",
				// userServiceMessage.getReturnMsg());
				jsonObject.put("tPwdErrCount", tPwdErrCount);
				logger.info("TradeController类【fundTrade】中支付密码验证失败》》》returnJsonObject=" + jsonObject.toString());
				return jsonObject;
			}
		} else {
			jsonObject.put("resultCode", "9999");
			logger.info("TradeController类【fundTrade】中验证支付密码失败》》》返回信息：returnJsonObject=" + jsonObject);
			return jsonObject;
		}
		/***************************** 验证支付密码 E ****************************************************/

		FundTradeDto fundTradeDto = new FundTradeDto();
		SignEContractDto elDto = new SignEContractDto();

		fundTradeDto.setSerialNo(serialNo);
		fundTradeDto.setCustNo(custNo);
		fundTradeDto.setTradeAcco(tradeAcco);
		fundTradeDto.setMobileNo(mobile);
		fundTradeDto.setFundId(fundId);
		fundTradeDto.setApkind(apkind);
		fundTradeDto.setTradeAmt(tradeAmt);
		fundTradeDto.setPayType(payType);
		fundTradeDto.setChannelId(channelId);
		fundTradeDto.setFee(fee);
		fundTradeDto.setCommro(commro);
		fundTradeDto.setRenew(renew);

		logger.info("TradeController类【fundTrade】中传给服务端的参数>>>fundTradeDto=" + fundTradeDto.toString());

		String appDt = DateUtils.formatDate(new Date(), DateUtils.yyyyMMdd);

		String ip = GetIPUtils.getIpAddr(request);

		elDto.setCustNo(custNo);// 客户号
		elDto.setTradeAcco(tradeAcco);// 交易账号
		elDto.setFundId(fundId);// 基金代码
		elDto.setAppDt(appDt);// 签署日期
		elDto.setContractVer(contractVer); // 合同版本
		elDto.setContractTp("1");// 合同类型
		elDto.setSignChannel("2");// 合同签署途径
		elDto.setSignMachine(ip);// 合同签署机器

		String resultCode = "";
		String resultMsg = "";
		FundTradeDto resultFundTradeDto = new FundTradeDto();
		BocSignParaDto bocSignParaDto = null;

		OrderResult orderResult = tradeServiceClient.fundTradeLineUp(context, fundTradeDto, elDto);
		if (orderResult != null) {
			resultCode = orderResult.getResultCode();
			resultMsg = orderResult.getResultMsg();
			
			logger.info("TradeController类【fundTrade】中调用后台服务接口成功，返回数据为：resultCode=" + resultCode + ";resultMsg=" + resultMsg);
			resultFundTradeDto = (FundTradeDto) orderResult.getData();
			bocSignParaDto = (BocSignParaDto) orderResult.getOtherData();

			if (resultFundTradeDto != null) {
				resultFundTradeDto.setCustNo("");// 将返回到页面的custno置空
			}
		}

		jsonObject.put("resultCode", resultCode);
		jsonObject.put("resultMsg", resultMsg);
		jsonObject.put("resultFundTradeDto", resultFundTradeDto);
		if(bocSignParaDto != null){
			jsonObject.put("bocSignParaDto", bocSignParaDto);
		}

		logger.info("TradeController类【fundTrade】结束>>>returnJsonObject=" + jsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());

		return jsonObject;
	}

	@Override
	public JSONObject addBankNumber(Context context, HttpServletRequest request) {
		JSONObject returnJsonObject = new JSONObject();
		UserBaseInfoDto userInfo = (UserBaseInfoDto)RequestHelper.getSessionUserBaseInfo(request);
		Object obj2 = request.getSession(true).getAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE);
		UserAccoRlaDto userAcc = (UserAccoRlaDto)request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		String ecCustNo = userAcc.getEcCustNo();
		String cmfUserId = userInfo.getCmfUserId();
		String userName = userInfo.getCustName();
		String idNo = userInfo.getIdNo();
		String mobile = obj2.toString();
		String idType = "0";//证件类型 ：身份证 0
		String bankNumber = request.getParameter("bankNumber");
		String bankName = request.getParameter("bankName");
		try {
			bankName = URLDecoder.decode(bankName, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String channelNo = request.getParameter("channelNo");//渠道代码
		String bankNo = request.getParameter("bankNo");//银行代码
		
		//银行卡号只能是数字类型
		if(!StringUtils.isNumeric(bankNumber)){
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_9008);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_9008);
			return returnJsonObject;
		}
		
		//银行名称 如非空 且 不是全中文
		String bankNameReplace = bankName.replaceAll("[\\u4e00-\\u9fa5]", "**");
		if(!StringUtils.isEmpty(bankName) && ((bankName.length() * 2 - bankNameReplace.length()) > 0)){
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_9008);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_9008);
			return returnJsonObject;
		}
		
		UserAcctDto userAcctDto = new UserAcctDto();
		userAcctDto.setEcCustNo(ecCustNo);
		userAcctDto.setCmfUserId(cmfUserId);
		userAcctDto.setInvName(userName);
		userAcctDto.setBankMobile(mobile);
		userAcctDto.setIdNo(idNo);
		userAcctDto.setIdType(idType);
		userAcctDto.setBankName(bankName);
		userAcctDto.setBankAcct(bankNumber);
		userAcctDto.setChannelNo(channelNo);
		userAcctDto.setBankNo(bankNo);
		String returnCode = null;
		String returnMsg = null;
		
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
			}
			if("CMB01".equals(orderResult.getResultCode())){
				Object obj = orderResult.getData();
				if (obj != null) {
					CmbSignParaDto cmb = (CmbSignParaDto)obj;
					returnJsonObject.put("cmbDto", cmb);
				}
			}
		}else{
			returnCode = ECConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = ECConstants.COMMON_ERROR_SYSERRMSG;
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

	@Override
	public OrderResult modifyAppointRequest(Context context, FundTradeDto fundTradeDto) {
		OrderResult orderResult = tradeServiceClient.modifyAppointRequest(context, fundTradeDto);
		return orderResult;
	}

	@Override
	public JSONObject setUserRiskLevel(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";

		String seqId = request.getSession(true).getId();
		UserBaseInfoDto sessionUserBaseInfo = RequestHelper.getSessionUserBaseInfo(request);
		String cmfUserId = "";
	    String userType="";
		if (sessionUserBaseInfo!=null) {
			cmfUserId=sessionUserBaseInfo.getCmfUserId();
			userType=sessionUserBaseInfo.getUserType();
		}
				

		logger.info("TradeController类【setUserRiskLevel】开始>>>seqId=" + seqId + ">>>cmfUserId=" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());
		
		String custNo = "";			//	客户号
	    String evalDate = "";		//评测日期20150414
	    String evalTime = "";		//评测时间205700
	    String evalDispDateTime = "";
	    String riskLevel = request.getParameter("riskLevel");		//等级：风险级别 1-安益型,2-保守型,3-稳健型,4-积极型,5-激进型(0-20:2;21-60:3;61-100:4)
	    String channelCode = request.getParameter("channelCode");		//渠道代码 NET-网站，IVR-自动语音，DIR-直销  (网上交易填写 NET) 
	    String terminalInfo = "";	//终端信息 发起方信息，IP地址/电话号码等,网上交易填写IP
	    String evalType = request.getParameter("evalType");		//测评类型 T-问卷测评，M-客户自评,网上交易填写 M
	    String evalFormno = request.getParameter("evalFormno");		//填写用户分数
	    String status = request.getParameter("status");			//填写N
	    String taxResidentType = request.getParameter("taxResidentType"); //税收居民类型
	    String taxResidentData=request.getParameter("taxResidentData"); //税收居民数据
	    String evalDateTimeToSession="";
	    String evalValiDate="";
	    if(taxResidentData != null){
	    	try {
				taxResidentData = URLDecoder.decode(taxResidentData,"utf-8");
			} catch (UnsupportedEncodingException e) {
				logger.info("TradeController类【setUserRiskLevel】获取taxResidentData异常>>>",e);
				e.printStackTrace();
			} //税收居民数据
	    }
	    String evalAnswer = request.getParameter("evalAnswer");
	    //重新解析答案
	    evalAnswer = parseEvalAnswerBySetUserRiskLevel(evalAnswer);
	    UserAccoRlaDto userAccoRla =  (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
	    if(userAccoRla != null){
	    	custNo = userAccoRla.getEcCustNo();
	    }
	    Date date= new Date();
	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    calendar.add(Calendar.YEAR, 1);
	    SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat("HHmmss");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat format4 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		evalDate = format1.format(date);
		evalTime = format2.format(date);
		evalDispDateTime = format3.format(date);
		evalDateTimeToSession =  format4.format(date);
		evalValiDate = format3.format(calendar.getTime());
		terminalInfo = GetIPUtils.getIpAddr(request);
		
		Hashtable<String, Object> ParameterData = ParameterCache.getData();
		//获取不到缓存对象时创建一个防止 异常
		if(null==ParameterData){
			ParameterData = new Hashtable<String, Object>();
		}
		boolean flag = false;
		//校验渠道编码 是否有效
		String channelCodeKey = "CUSTRISKCFM#CHANNELCODE#"+channelCode;
		if(StringUtils.isEmpty(channelCode) || !ParameterData.containsKey(channelCodeKey)){
			flag = true;
		}
		
		//校验测评类型 是否有效
		String evalTypeKey = "SYSTEM#RISKEVALTYPE#"+evalType;
		if(StringUtils.isEmpty(evalType) || !ParameterData.containsKey(evalTypeKey)){
			flag = true;
		}
		
		//校验测评状态 是否有效
		String statusKey = "SYSTEM#RISKSTATUS#"+status;
		if(!StringUtils.isEmpty(status) && !ParameterData.containsKey(statusKey)){
			flag = true;
		}
		
		//校验 分数不为空且非数字
		if(!StringUtils.isEmpty(evalFormno) && !StringUtils.isNumeric(evalFormno)){
			flag = true;
		}
		
		if(flag){
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_9008);
			jsonObject.put("returnMsg", ECConstants.RETURN_MSG_9008);
			return jsonObject;
		}
		
		UserRiskLevelDto userRiskLevelDto = new UserRiskLevelDto();// 传递到后台的DTO
		userRiskLevelDto.setCustNo(custNo);
		userRiskLevelDto.setCmfUserId(cmfUserId);
		userRiskLevelDto.setEvalDate(evalDate);
		userRiskLevelDto.setEvalTime(evalTime);
		userRiskLevelDto.setRiskLevel(riskLevel);
		userRiskLevelDto.setChannelCode(channelCode);
		userRiskLevelDto.setTerminalInfo(terminalInfo);
		userRiskLevelDto.setEvalType(evalType);
		userRiskLevelDto.setEvalFormno(evalFormno);
		userRiskLevelDto.setStatus(status);
		userRiskLevelDto.setEvalAnswer(evalAnswer);
		String RISKLEVEL= (String)request.getSession(true).getAttribute("RISKLEVEL");
		logger.info("TradeController类【setUserRiskLevel】中传给服务端的参数>>>userRiskLevelDto=" + userRiskLevelDto.toString());
		
		OrderResult orderResult = null;
		if (userType!=null && "10".equals(userType)) {
			orderResult = tradeServiceClient.setCustRiskLevelBeforeOpenAccount(context, userRiskLevelDto);
		}else{
			orderResult=tradeServiceClient.setUserRiskLevel(context, userRiskLevelDto);
		}
		UserRiskLevelDto userRiskDto = null;
		if(orderResult != null){
			returnCode = orderResult.getErrCode();
			returnMsg = orderResult.getErrMsg();
			userRiskDto = (UserRiskLevelDto) orderResult.getData();
			if(userRiskDto != null){
				userRiskDto.setCustNo("");
			}
			request.getSession(true).setAttribute("RISKLEVEL", riskLevel);
			request.getSession(true).setAttribute("riskLevel", riskLevel);
			request.getSession(true).setAttribute("riskEvalDate", evalDateTimeToSession);   
			jsonObject.put("userRiskLevelDto", userRiskDto);
		}
		if(ECConstants.COMMON_SUCCESS.equals(returnCode.trim())){
			//风险测评完成后 保存 用户 税收居民 信息
			this.saveTaxInfoByUserInvtp(custNo, cmfUserId, taxResidentType, taxResidentData);
			//更新 用户购买时适当性要求 
			this.updateUserProperInfoByRiskLevel(userRiskLevelDto);
			//测评完成 更新特殊用户等级
			this.updateUserSpecialRiskLevelInfo(userRiskLevelDto);
			try{
				jsonObject.put("evalDispDateTime", evalDispDateTime);
				Map<String, String> param= new HashMap<String, String>();
				param.put("riskLevel", riskLevel);
				param = userServiceClient.queryParamByUserRiskLevelSucc(param);
				String custRiskLevel = "";
				String fundRiskLevel = "";
				String bearAbility = "";
				if(param.containsKey("CUSTRISKLEVEL")){
					custRiskLevel = (String) param.get("CUSTRISKLEVEL").replaceAll(",", "、");
				}
				if(param.containsKey("FUNDRISKLEVEL")){
					String[] fundRiskLevels = param.get("FUNDRISKLEVEL").split(",");
					String fundData = fundRiskLevels[fundRiskLevels.length-1];
					if(fundRiskLevels.length <= 1){
						fundRiskLevel = fundData + "产品";
					}else{
						fundRiskLevel = fundData + "及以下产品";
					}
					//风险承受能力为 建议产品等级 最后一级
					bearAbility = "("+ fundData +"承受能力)";
				}
				jsonObject.put("custRiskLevel", custRiskLevel);
				jsonObject.put("fundRiskLevel", fundRiskLevel);
				jsonObject.put("bearAbility", bearAbility);
				jsonObject.put("evalValiDate", evalValiDate);
			}catch(Exception e){
				logger.error("----TradeManagerImpl-setUserRiskLevel-测评完成后封装用户建议信息异常Except:",e);
				e.printStackTrace();
			}
		}
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
	    UserBaseInfoDto baseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
	    baseInfoDto.setRiskLevel(riskLevel);
	    request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, baseInfoDto);
		return jsonObject;
	}

	@Override
	public JSONObject contractSign(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";

		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);

		String tradeAcct = request.getParameter("tradeAcct");
		String bankNo = request.getParameter("bankNo");
		String channelNo = request.getParameter("channelNo");
		
		logger.info("TradeController类【contractSign】开始>>>seqId=" + seqId + ">>>cmfUserId=" + cmfUserId + ">>>tradeAcct=" + tradeAcct + ">>>bankNo=" + bankNo + ">>>timestamp:" + System.currentTimeMillis());
		
		UserAcctDto userAcctDto = new UserAcctDto();
		userAcctDto.setTradeAcct(tradeAcct);
		userAcctDto.setBankNo(bankNo);
		userAcctDto.setChannelNo(channelNo);
		
		OrderResult orderResult = tradeServiceClient.contractSign(context, userAcctDto);
		IcbcSignParaDto icbcSignParaDto = null;
		if(orderResult != null){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
			if(returnCode != null && returnCode.equals(ECConstants.RETURN_CODE_0000)){
				icbcSignParaDto = (IcbcSignParaDto) orderResult.getData();
			}
		}
		if(icbcSignParaDto != null){
			jsonObject.put("icbcSignParaDto", icbcSignParaDto);
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);

		logger.info("TradeController类【contractSign】结束>>>jsonObject=" + jsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}

	@Override
	public JSONObject queryCommandByBankAccoNo(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";

		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);

		String tradeAcct = request.getParameter("tradeAcct");
		String bankNo = request.getParameter("bankNo");
		String channelNo = request.getParameter("channelNo");
		
		logger.info("TradeController类【queryCommandByBankAccoNo】开始>>>seqId=" + seqId + ">>>cmfUserId=" + cmfUserId + ">>>tradeAcct=" + tradeAcct + ">>>bankNo=" + bankNo + ">>>timestamp:" + System.currentTimeMillis());
		
		UserAcctDto userAcctDto = new UserAcctDto();
		userAcctDto.setTradeAcct(tradeAcct);
		userAcctDto.setBankNo(bankNo);
		userAcctDto.setChannelNo(channelNo);
		
		OrderResult orderResult = tradeServiceClient.queryCommandByBankAccoNo(context, userAcctDto);
		if(orderResult != null){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);

		logger.info("TradeController类【queryCommandByBankAccoNo】结束>>>jsonObject=" + jsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}

	@Override
	public OrderResult appConversionUserInvtp(Context context,HttpServletRequest request) {
		OrderResult orderResult = null;
		UserRiskLevelDto userRiskLevelDto = new UserRiskLevelDto();
		String apptp = request.getParameter("apptp");
		userRiskLevelDto.setApptp(apptp);
		UserAccoRlaDto userAccoRla =  (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		if(userAccoRla != null){
			userRiskLevelDto.setCustNo(userAccoRla.getEcCustNo());
	    }
		logger.info("申请转换专业投资者客户信息：" + JSONObject.fromObject(userAccoRla));
		if("1".equals(apptp)){
			String seqId = request.getSession(true).getId();
			UserBaseInfoDto sessionUserBaseInfo = RequestHelper.getSessionUserBaseInfo(request);
			String cmfUserId = "";
		    String userType="";
			if (sessionUserBaseInfo!=null) {
				cmfUserId=sessionUserBaseInfo.getCmfUserId();
				userType=sessionUserBaseInfo.getUserType();
			}
			logger.info("TradeController类【setUserRiskLevel】开始>>>seqId=" + seqId + ">>>cmfUserId=" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());
			String custNo = "";			//	客户号
		    String evalDate = "";		//评测日期20150414
		    String evalTime = "";		//评测时间205700
		    String riskLevel = request.getParameter("riskLevel");		//等级：风险级别 1-安益型,2-保守型,3-稳健型,4-积极型,5-激进型(0-20:2;21-60:3;61-100:4)
		    String channelCode = request.getParameter("channelCode");		//渠道代码 NET-网站，IVR-自动语音，DIR-直销  (网上交易填写 NET) 
		    String terminalInfo = "";	//终端信息 发起方信息，IP地址/电话号码等,网上交易填写IP
		    String evalType = request.getParameter("evalType");		//测评类型 T-问卷测评，M-客户自评,网上交易填写 M
		    String evalFormno = request.getParameter("evalFormno");		//填写用户分数
		    String status = request.getParameter("status");			//填写N
		    String evalAnswer = request.getParameter("evalAnswer");
		    evalAnswer = this.parseEvalAnswerBySetUserRiskLevel(evalAnswer);
		    String evalDateTimeToSession="";
		    if(userAccoRla != null){
		    	custNo = userAccoRla.getEcCustNo();
		    }
		    SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat format2 = new SimpleDateFormat("HHmmss");
			SimpleDateFormat format3 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date nowDate = new Date();
			evalDate = format1.format(nowDate);
			evalTime = format2.format(nowDate);
			evalDateTimeToSession = format3.format(nowDate);
			terminalInfo = GetIPUtils.getIpAddr(request);
			
			Hashtable<String, Object> ParameterData = ParameterCache.getData();
			//获取不到缓存对象时创建一个防止 异常
			if(null==ParameterData){
				ParameterData = new Hashtable<String, Object>();
			}
			boolean flag = false;
			//校验渠道编码 是否有效
			String channelCodeKey = "CUSTRISKCFM#CHANNELCODE#"+channelCode;
			if(StringUtils.isEmpty(channelCode) || !ParameterData.containsKey(channelCodeKey)){
				flag = true;
			}
			
			//校验测评类型 是否有效
			String evalTypeKey = "SYSTEM#RISKEVALTYPE#"+evalType;
			if(StringUtils.isEmpty(evalType) || !ParameterData.containsKey(evalTypeKey)){
				flag = true;
			}
			
			//校验测评状态 是否有效
			String statusKey = "SYSTEM#RISKSTATUS#"+status;
			if(!StringUtils.isEmpty(status) && !ParameterData.containsKey(statusKey)){
				flag = true;
			}
			
			//校验 分数不为空是且非数字
			if(!StringUtils.isEmpty(evalFormno) && !StringUtils.isNumeric(evalFormno)){
				flag = true;
			}
			
			if(flag){
				orderResult = new OrderResult();
				orderResult.setErrCode(ECConstants.RETURN_CODE_9008);
				orderResult.setErrMsg(ECConstants.RETURN_MSG_9008);
				orderResult.setResultCode(ECConstants.RETURN_CODE_9008);
				orderResult.setResultMsg(ECConstants.RETURN_MSG_9008);
				return orderResult;
			}
			
			userRiskLevelDto.setCustNo(custNo);
			userRiskLevelDto.setCmfUserId(cmfUserId);
			userRiskLevelDto.setEvalDate(evalDate);
			userRiskLevelDto.setEvalTime(evalTime);
			userRiskLevelDto.setRiskLevel(riskLevel);
			userRiskLevelDto.setChannelCode(channelCode);
			userRiskLevelDto.setTerminalInfo(terminalInfo);
			userRiskLevelDto.setEvalType(evalType);
			userRiskLevelDto.setEvalFormno(evalFormno);
			userRiskLevelDto.setStatus(status);
			userRiskLevelDto.setEvalAnswer(evalAnswer);
			logger.info("TradeController类【setUserRiskLevel】中传给服务端的参数>>>userRiskLevelDto=" + userRiskLevelDto.toString());
			
			if (userType!=null && "10".equals(userType)) {
				orderResult = tradeServiceClient.setCustRiskLevelBeforeOpenAccount(context, userRiskLevelDto);
			}else{
				orderResult=tradeServiceClient.setUserRiskLevel(context, userRiskLevelDto);
			}
			UserRiskLevelDto userRiskDto = null;
			if(orderResult != null){
				userRiskDto = (UserRiskLevelDto) orderResult.getData();
				if(userRiskDto != null){
					userRiskDto.setCustNo("");
				}
				request.getSession(true).setAttribute("RISKLEVEL", riskLevel);
				request.getSession(true).setAttribute("riskLevel", riskLevel);
				request.getSession(true).setAttribute("riskEvalDate", evalDateTimeToSession);
			}
			//风险测评成功后操作
			if(null!=orderResult && "0000".equals(orderResult.getErrCode())){
				//更新 用户购买时适当性要求 
				this.updateUserProperInfoByRiskLevel(userRiskLevelDto);
				//测评完成 更新特殊用户等级
				this.updateUserSpecialRiskLevelInfo(userRiskLevelDto);
				//转普通投资者测评完成 更新投资者类型至 ecc 柜台
				userServiceClient.syncInvprtpToEcc(context, cmfUserId, custNo, "0");
				//客户专业转换普通投资者成功记录到H_CUSTINVPRTP表
				Map<String, String> map = new HashMap<String, String>();
				map.put("custNo", custNo);
				map.put("invprtp", "1");
				map.put("oldInvprtp", "0");
				map.put("status", "Y");
				map.put("channelCode", "NET");
				tradeServiceClient.insertCustInvprtp(map);
			}
			
		    UserBaseInfoDto baseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
		    baseInfoDto.setRiskLevel(riskLevel);
		    request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, baseInfoDto);
		}
		orderResult = tradeServiceClient.appConversionUserInvtp(context, userRiskLevelDto);
		return orderResult;
	}
	
	public void saveTaxInfoByUserInvtp(String custNo,String cmfUserId,String taxResidentType,String taxResidentData) {
		//类型为空 或者 类型非为仅中国并且数据为空 
		if(StringUtils.isEmpty(taxResidentType) || StringUtils.isEmpty(taxResidentData) ||(!"1".equals(taxResidentType) && StringUtils.isEmpty(taxResidentData))){
			return ;
		}
		JSONArray jsonArray = new JSONArray();
		try{
			jsonArray = JSONArray.fromObject(taxResidentData);
		}catch(Exception e){
			logger.error("----TradeManagerImpl-saveTaxInfoByUserInvtp-JSONArray.fromObject-Exception-cmfUserId:"+cmfUserId+";taxResidentType="+taxResidentType);
			logger.error("----TradeManagerImpl-saveTaxInfoByUserInvtp-JSONArray.fromObject-Exception:"+taxResidentData);
			logger.error("----TradeManagerImpl-saveTaxInfoByUserInvtp-JSONArray.fromObject-Exception:",e);
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
			logger.error("----TradeManagerImpl-saveTaxInfoByUserInvtp-Exception-cmfUserId:"+cmfUserId+";taxResidentType="+taxResidentType);
			logger.error("----TradeManagerImpl-saveTaxInfoByUserInvtp-Exception:",e);
			e.printStackTrace();
		}
	}

	@Override
	public OrderResult passTestUpdateUserInvtp(Context context,
			String cmfUserId, String custNo,String invprtpScore) {
		OrderResult orderResult = tradeServiceClient.passTestUpdateUserInvtp(context, cmfUserId, custNo,invprtpScore);
		if("0000".equals(orderResult.getResultCode())){
			userServiceClient.syncInvprtpToEcc(context, cmfUserId, custNo,invprtpScore);
		}
		return orderResult;
	}

	@Override
	public OrderResult cancelAppConversionUserInvtp(Context context,
			String cmfUserId, String custNo) {
		OrderResult orderResult = tradeServiceClient.cancelAppConversionUserInvtp(context, cmfUserId, custNo);
		return orderResult;
	}
	
	/**
	 * 用户提交风险问卷
	 * @param oldEvalAnswer
	 * @return
	 */
	private String parseEvalAnswerBySetUserRiskLevel(String oldEvalAnswer){
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
			returnString = StringUtils.join(returnEvalAnswer.toArray(), ",");
		}catch(Exception e){
			logger.error("----parseEvalAnswerBySetUserRiskLevel-Exception:",e);
			e.printStackTrace();
		}
		return returnString;
	}
	
	/**
	 * 风险测评后 将 是否控制关系 受益人 诚信记录 更新至用户表
	 * 方便购买产品时 限制 不用每次购买都需要解析 答案
	 * @param userBaseInfoDto
	 */
	private void updateUserProperInfoByRiskLevel(UserRiskLevelDto userRiskLevelDto){
		try{
			String evalAnswerData= userRiskLevelDto.getEvalAnswer();
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
		}catch(Exception e ){
			logger.error("----updateUserProperInfoByRiskLevel-Exception:",e);
			e.printStackTrace();
		}
		
	}
	
	private void updateUserSpecialRiskLevelInfo(UserRiskLevelDto userRiskLevelDto){
		UserBaseInfoDto userBaseInfoDto = new UserBaseInfoDto();
		userBaseInfoDto.setCmfUserId(userRiskLevelDto.getCmfUserId());
		if("0".equals(userRiskLevelDto.getEvalFormno()) && "1".equals(userRiskLevelDto.getRiskLevel())){
			userBaseInfoDto.setSpecialRiskLevel("1");
		}else{
			userBaseInfoDto.setSpecialRiskLevel("");
		}
		userServiceClient.updateUserSpecialRiskLevelInfo(userBaseInfoDto);
	}

	@Override
	public JSONObject updateAppointRequest(Context context, HttpServletRequest request) {
		JSONObject json = new JSONObject();
		JSONObject dataJson = new JSONObject();
		String serialno = request.getParameter("serialno");
		String renew = request.getParameter("flag");
		if(StringUtils.isEmpty(serialno) || StringUtils.isEmpty(renew)){
			json.put("returnCode", ECConstants.RETURN_CODE_9000);
			json.put("returnMsg", ECConstants.RETURN_MSG_9000);
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
						json.put("returnCode", ECConstants.RETURN_CODE_0000);
						json.put("returnMsg", ECConstants.RETURN_MSG_0000);
						return json;
					}
					if("G".equals(dto.getApplyst()) && "8".equals(dto.getPayst())){
						dataJson.put("renew", renew);
						dataJson.put("serialno", serialno);
						logger.info("调用tradeServiceClient.updateAppointRequest()方法修改订单分配方式---start,入参： "+ dataJson.toString());
						Integer i = tradeServiceClient.updateAppointRequest(dataJson.toString());
						logger.info("调用tradeServiceClient.updateAppointRequest()方法修改订单分配方式---end,出参： "+ i);
						dataJson.put("custIp", GetIPUtils.getIpAddr(request));
						dataJson.put("custNo", dto.getCustno());
						dataJson.put("custName", dto.getCustName());
						dataJson.put("custMobile", dto.getMobile());
						dataJson.put("optType", "Y".equals(renew)?"2":"1");
						dataJson.put("sourceType","01");
						dataJson.put("remark", i);
						logger.info("调用tradeServiceClient.addOplog()方法保存用户操作记录---start,入参： "+ dataJson.toString());
						tradeServiceClient.addOplog(dataJson.toString());
						json.put("returnCode", ECConstants.RETURN_CODE_0000);
						json.put("returnMsg", ECConstants.RETURN_MSG_0000);
					}else{
						json.put("returnCode", ECConstants.RETURN_CODE_9000);
						json.put("returnMsg", "该订单状态不是存续中，请确认！");
					}
				}else{
					json.put("returnCode", ECConstants.RETURN_CODE_9000);
					json.put("returnMsg", "获取不到对应的订单信息,请重试！");
				}
			}else{
				json.put("returnCode", ECConstants.RETURN_CODE_9000);
				json.put("returnMsg", "该请求的订单信息异常,请重试！");
			}
		} catch (Exception e) {
			logger.error("updateAppointRequest 发生异常信息,"+e);
			json.put("returnCode", ECConstants.COMMON_ERROR_SYSERRCODE);
			json.put("returnMsg", ECConstants.COMMON_ERROR_SYSERRMSG);
		}
		return json;
	}
	/**
	 * 发送邮件
	 * @param rfr
	 * @return
	 */
	private  void  sendFundTradeMail(Context context,AppointRequestDto appoint){
		MailMessage mailMessage = new MailMessage();
		try {
			//收件人
			QueryMessageDto queryParamListCc = queryServiceClient.queryHomeAddressIsWordWithLinkage(context, "SYSTEM", "REDEMPTFAIL_CC", null, null);
			QueryMessageDto queryParamListTo = queryServiceClient.queryHomeAddressIsWordWithLinkage(context, "SYSTEM", "REDEMPTFAIL_TO", null, null);
			List<ParameterDto> listParaCc = (List<ParameterDto>) queryParamListCc.getData();
			List<ParameterDto> listParaTo = (List<ParameterDto>) queryParamListTo.getData();
	    	if(null != listParaCc && listParaCc.size() >0 && null != listParaTo && listParaTo.size() >0){
	    		String[] tccs = listParaCc.get(0).getPmco().split(",");   //抄送 人
	    		String[] tos = listParaTo.get(0).getPmco().split(",");   //收件人
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

	    		content +=  " <tr>"				
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+appoint.getFundInfoDtoV2().getAdname()+"</td>"
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+appoint.getMobile()+"</td>"
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+appoint.getCustName()+"</td>"
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+appoint.getSerialno()+"</td>"
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+appoint.getSubamt()+"</td>"
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+appoint.getSubquty()+"</td>"
						        + "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+appoint.getApdt()+"</td>"		        
						        + "</tr> ";

				content += "</table></body></html>";
				mailMessage.setCc(tccs.length == 0 ? null : tccs); // 抄送的地址+手动输入的地址
				mailMessage.setTo(tos.length == 0 ? null : tos); 	// 接收人，接收人输入框没有的话取配置文件中的地址
				mailMessage.setContent(content);
				mailMessage.setSubject("活期产品赎回失败提醒");// 主题
				mailMessage.setConfId("");
				mailMessage.setPrefix("custom2");
				messageServiceClient.sendMail(context,mailMessage);
	    	}
		} catch (Exception e) {
			logger.error("发送邮件异常：" + e.getMessage());
		}
	}

	@Override
	public void addOpLog(String strObject) {
		tradeServiceClient.addOplog(strObject);
	}	
	
	@Override
	public JSONObject addAccreditedInvestorInfo(CmwaQualifiedUserInfoDto param,UserBaseInfoDto dto) {
		// 声明query服务需要用到的参数
		Map<String,Object> queryParameter = new HashMap<String,Object>();
		queryParameter.put("cmfuserid",param.getCmfuserid());
		logger.info("开始添加合格投资者申请，参数为："+param);
		//返回对象
		JSONObject returnObject = new JSONObject();
		try {
			returnObject.put("returnCode",ECConstants.RETURN_CODE_9999);
			// 查询是否已经有合格投资者申请
			QueryMessageDto queryServiceResult = queryServiceClient.queryAllQualifiedUserInfo(queryParameter);
			if (queryServiceResult != null && "0000".equals(queryServiceResult.getResultCode())) {
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
							this.updatedAccreditedInvestorRequestStatus(param.getCustno(),ECConstants.QULIFIEDUSER_STATUS_N,dto);
							returnObject.put("returnCode",ECConstants.RETURN_CODE_0000);
							returnObject.put("returnMsg","添加成功");
							return returnObject;
						} else {
							logger.info("添加失败,param:"+param);
							returnObject.put("returnMsg","添加失败");
						}
					}
				} else {
					logger.info("未查询到当前用户的合格投资者申请信息，用户信息为:"+dto);
					returnObject.put("returnMsg", "未查询到申请信息");
				}
			} else {
				logger.info("调用query服务失败，返回code为：" + (queryServiceResult == null ? "null" : queryServiceResult.getResultCode()));
				returnObject.put("returnMsg","添加失败");
			}
		} catch (Exception e) {
			logger.error("添加合格投资者申请时捕获异常:",e);
			returnObject.put("returnMsg","系统异常");
		}
		return returnObject;
	}

	
	/**
	 * 修改已提交的合格投资者申请文件信息
	 *
	 * @param key
	 * @param custno
	 * @param operatorType
	 * @param fileName
	 * @param fileType
	 * @param currentUser
	 * @return
	 */
	@Override
	public JSONObject updateSubmittedInfo(String key, String custno, String operatorType, String fileName, String fileType, String currentUser) {
		JSONObject returnObj = new JSONObject();
        if (StringUtils.isBlank(key) || StringUtils.isBlank(custno) || StringUtils.isBlank(operatorType)){
            returnObj.put("returnCode",ECConstants.RETURN_CODE_9999);
            returnObj.put("returnMsg","关键参数丢失");
            return returnObj;
        }
        if(!"insert".equalsIgnoreCase(operatorType) && !"delete".equalsIgnoreCase(operatorType)) {
            returnObj.put("returnCode",ECConstants.RETURN_CODE_9999);
            returnObj.put("returnMsg","操作类型错误");
            return returnObj;
        }
        if("insert".equalsIgnoreCase(operatorType) && (StringUtils.isBlank(fileType) || StringUtils.isBlank(fileName))) {
        	returnObj.put("returnCode",ECConstants.RETURN_CODE_9999);
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
                     AmazonResult amazonResult = amazonS3Manager.removeObjectByKey(key);
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
                	returnObj.put("returnCode", ECConstants.RETURN_CODE_9999);
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
            	returnObj.put("returnCode",ECConstants.RETURN_CODE_9999);
            	returnObj.put("returnMsg","修改失败");
            } else { 
            	logger.info("修改成功！");
            	returnObj.put("returnCode",ECConstants.RETURN_CODE_0000);
            	returnObj.put("returnMsg","修改成功");
            }
            return returnObj;
        }catch(Exception e) {
            logger.error("修改已提交的合格投资者信息时捕获异常",e);
            returnObj.put("returnCode",ECConstants.RETURN_CODE_9999);
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
            returnObj.put("returnCode",ECConstants.RETURN_CODE_9999);
            returnObj.put("returnMsg","未查询到合格投资者申请信息");
        } else{
        	// 如果状态为U或者为R 则可以修改用户的上传文件
        	// 新增逻辑 如果是上传身份证照片且当前状态不为N则也可以修改用户的上传文件 wangz
        	String currentStatus = queryResult.getStatusRecord().getStatus();
            if(ECConstants.QULIFIEDUSER_STATUS_U.equalsIgnoreCase(currentStatus) || ECConstants.QULIFIEDUSER_STATUS_R.equalsIgnoreCase(currentStatus)) {
                return queryResult;
            } else if((ECConstants.QUALIFIED_FILETYPE_IDCARDPORTRAIT.equalsIgnoreCase(fileType) 
            		|| ECConstants.QUALIFIED_FILETYPE_IDCARDNATIONALEMBLEM.equalsIgnoreCase(fileType)) 
            		&& !ECConstants.QULIFIEDUSER_STATUS_N.equalsIgnoreCase(currentStatus)) {
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
            } else if(!ECConstants.RETURN_CODE_0000.equals(queryMessageDto.getResultCode())) {
                logger.info("根据custno调用query服务查询失败,custno:"+custno+",errCode:"+queryMessageDto.getResultCode()+",errMsg:"+queryMessageDto.getResultMsg());
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
	public OrderResult updateOrderRedemptionShare(FundTradeDto dto) {
		OrderResult orderResult = tradeServiceClient.updateOrderRedemptionShare(dto);
		return orderResult;
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
                jsonObject.put("returnCode",ECConstants.RETURN_CODE_9999);
                jsonObject.put("returnMsg","参数丢失");
                return jsonObject;
            }
            jsonObject.put("returnCode",ECConstants.RETURN_CODE_9999);
            CmwaFileUploadRecordDto parameter = new CmwaFileUploadRecordDto();
            parameter.setRecordId(recordId);
            List<CmwaFileUploadRecordDto> cmwaFileUploadRecordDtos = tradeServiceClient.queryFileUploadRecord(parameter);
            if(cmwaFileUploadRecordDtos != null && cmwaFileUploadRecordDtos.size() > 0) {
                CmwaFileUploadRecordDto cmwaFileUploadRecordDto = cmwaFileUploadRecordDtos.get(0);
                int result = tradeServiceClient.deleteFileUploadRecord(recordId);
                logger.info("删除影响列为:"+result+",recordId:"+recordId);
                if(result == 1) {
                    jsonObject.put("returnCode", ECConstants.RETURN_CODE_0000);
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
            jsonObject.put("returnCode",ECConstants.RETURN_CODE_9999);
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
			obj.put("returnCode", ECConstants.RETURN_CODE_9999);
			obj.put("returnMsg", ECConstants.RETURN_MSG_9008);
			return obj;
		}
		
		if (!ECConstants.QULIFIEDUSER_STATUS_N.equals(updateStatus)
				&& !ECConstants.QULIFIEDUSER_STATUS_U.equals(updateStatus)
				&& !ECConstants.QULIFIEDUSER_STATUS_R.equals(updateStatus)) {
			obj.put("returnCode", ECConstants.RETURN_CODE_9999);
			obj.put("returnMsg", "错误的状态码");
			return obj;
		}
		
		QualifiedUserInfoDto accfreditedInvestorInfo = this.queryAccreditedInvestorInfoByCustno(custno);
		if (accfreditedInvestorInfo == null) {
			obj.put("returnCode", ECConstants.RETURN_CODE_9999);
			obj.put("returnMsg", "未查询到合格投资者申请");
		} else {
			String currentStatus = accfreditedInvestorInfo.getStatusRecord().getStatus();
			logger.info("当前用户状态为："+currentStatus+",custno:"+custno+",需要修改的状态为："+updateStatus);
			
			// 针对需要修改的状态做判断
			if(ECConstants.QULIFIEDUSER_STATUS_N.equals(updateStatus)) {
				// 客户状态由未提交至确认状态 U -> N(确认提交) 新增已是合格投资者
				if(ECConstants.QULIFIEDUSER_STATUS_U.equals(currentStatus) || ECConstants.QULIFIEDUSER_STATUS_R.equals(currentStatus)
						|| ECConstants.QULIFIEDUSER_STATUS_S.equals(currentStatus)) {
					executeUpdateStatus(custno, updateStatus, obj,accfreditedInvestorInfo,currentUserInfo);
				} else {
					logger.info("当前用户状态为："+currentStatus+",不可转变为需要修改的状态:"+updateStatus);
					obj.put("returnCode", ECConstants.RETURN_CODE_9999);
					obj.put("returnMsg", "状态不匹配");
				}
			} else if (ECConstants.QULIFIEDUSER_STATUS_U.equals(updateStatus) || ECConstants.QULIFIEDUSER_STATUS_R.equals(updateStatus)) {
				// 客户状态由审核失败至未确认状态 F|R -> U (重新审核)
				if(ECConstants.QULIFIEDUSER_STATUS_F.equals(currentStatus)) {
					executeUpdateStatus(custno, updateStatus, obj,accfreditedInvestorInfo,currentUserInfo);
				} else {
					logger.info("当前用户状态为："+currentStatus+",不可转变为需要修改的状态:"+updateStatus);
					obj.put("returnCode", ECConstants.RETURN_CODE_9999);
					obj.put("returnMsg", "状态不匹配");
				}
			}
		}
		return obj;
	}
	
	private void executeUpdateStatus(String custno, String updateStatus,
			JSONObject obj, QualifiedUserInfoDto accfreditedInvestorInfo,UserBaseInfoDto currentUserInfo) {
		try{
			int executeResult = tradeServiceClient.updateQualifiedUserInfoStatus(getTradeQualifiedStatusDto(accfreditedInvestorInfo.getStatusRecord()),updateStatus, custno);
			if(executeResult <= 0) {
				logger.info("修改与影响列异常，修改失败! custno:"+custno+",statusInfo:"+accfreditedInvestorInfo.getStatusRecord()+",updateStatus:"+updateStatus);
			} else {
				obj.put("returnCode", ECConstants.RETURN_CODE_0000);
				obj.put("returnMsg", "修改成功");
				String operatorType = "";
				String currentStatus = accfreditedInvestorInfo.getStatusRecord().getStatus();
				
				if(ECConstants.QULIFIEDUSER_STATUS_U.equals(currentStatus) && ECConstants.QULIFIEDUSER_STATUS_N.equals(updateStatus)) {
					// 如果用户当前状态为U转向N 则操作类型为确认申请
					operatorType = "04";
				} else if (ECConstants.QULIFIEDUSER_STATUS_F.equals(currentStatus) && ECConstants.QULIFIEDUSER_STATUS_R.equals(updateStatus)) {
					// 如果用户当前状态为F转向R 则操作类型为重新填写资料
					operatorType = "05";
				} else if (ECConstants.QULIFIEDUSER_STATUS_R.equals(currentStatus) && ECConstants.QULIFIEDUSER_STATUS_N.equals(updateStatus)) {
					// 如果用户当前状态为R转向N 则操作类型为重新审核提交
					operatorType = "06";
				} else if (ECConstants.QULIFIEDUSER_STATUS_S.equals(currentStatus) && ECConstants.QULIFIEDUSER_STATUS_N.equals(updateStatus)) {
					// 如果用户当前状态为S转向N 则操作类型为重新审核
					operatorType = "08";
				}
				userServiceClient.insertUserOperateLog(getOperateLogDto(currentUserInfo, operatorType));
			}
		} catch (Exception e) {
			logger.error("修改用户合格投资者申请时捕获异常：",e);
			obj.put("returnCode", ECConstants.RETURN_CODE_9999);
			obj.put("returnMsg", ECConstants.RETURN_MSG_9999);
		}
	}
	
	public UserOperateLogDto getOperateLogDto(UserBaseInfoDto userInfo,String operatorType) {
		UserOperateLogDto dto = new UserOperateLogDto();
		dto.setCmfuserid(userInfo.getCmfUserId());
		dto.setLoginChannel(ECConstants.LOGIN_NMARK_01);
		dto.setType(operatorType);
		return dto;
	}
	
	private boolean checkUserSatisfyCondition(String custno) {
		QueryMessageDto result = queryServiceClient.queryAccreditedInvestorConditions(custno);
		if(result == null || !ECConstants.RETURN_CODE_0000.equals(result.getResultCode())) {
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
		BeanUtils.copyProperties(source,target);
		return target;
	}
	
	private com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoDto getTradeQualifiedDto (CmwaQualifiedUserInfoDto source) throws IllegalAccessException, InvocationTargetException {
		com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoDto target = new com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoDto();
		BeanUtils.copyProperties(source,target);
		return target;
	}
	@Override
	public JSONObject saveUserTermsInfo(Context context,String cmfUserId,String fundId,String period,String ids,String type) {
		logger.info("调用saveUserTermsInfo保存客户风险揭示书勾选纪录信息>>>start>>>入参:[cmfUserId:" +cmfUserId + ",fundId:" +fundId + ",period:" +period + ",ids:" + ids + ",type:" + type + "]");
		JSONObject jsonObject = new JSONObject();
        try{
        	List<String> list = Arrays.asList(ids.split(","));
        	if(null != list && !list.isEmpty()){
        		parameterDao.deleteUserTerms(fundId, period, cmfUserId);
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
        		parameterDao.insertUserTerms(terms);
        		if("submit".equals(type)){
        			Map<String, Object> map = new HashMap<String, Object>();
        			map.put("cmfuserid", cmfUserId);
        			map.put("loginChannel", "01");
        			map.put("type", "07");
        			map.put("fundId", fundId);
        			map.put("period", period);
        			parameterDao.insertUserOperateLog(map);
        		}
        		jsonObject.put("returnCode",ECConstants.RETURN_CODE_0000);
                jsonObject.put("returnMsg",ECConstants.RETURN_MSG_0000);
        	}else{
        		 jsonObject.put("returnCode",ECConstants.RETURN_CODE_9008);
                 jsonObject.put("returnMsg",ECConstants.RETURN_MSG_9008);
        	}
        } catch(Exception e) {
            logger.error("调用trade服务保存客户风险揭示函信息记录时捕获异常:",e);
            jsonObject.put("returnCode",ECConstants.RETURN_CODE_9999);
            jsonObject.put("returnMsg",ECConstants.RETURN_MSG_9999);
        }
        logger.info("调用saveUserTermsInfo保存客户风险揭示书勾选纪录信息>>>end>>>出参:" + jsonObject.toString());
        return jsonObject;
	}
	
	@Override
	public OrderResult updateOrderApplyst(FundTradeDto dto) {
		return tradeServiceClient.updateOrderApplyst(dto);
	}
}
