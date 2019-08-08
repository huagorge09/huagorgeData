package com.cmwa.ec.weixin.controller;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ec.message.facade.dto.MsgParameterDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.dto.mail.MailMessage;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.trade.AppointRequestDto;
import com.cmwa.ec.query.facade.dto.user.CmwaQualifiedUserInfoDto;
import com.cmwa.ec.trade.facade.dto.OrderResult;
import com.cmwa.ec.trade.facade.dto.fund.FundTradeDto;
import com.cmwa.ec.trade.facade.dto.fund.SignEContractDto;
import com.cmwa.ec.trade.facade.dto.log.OpLogDto;
import com.cmwa.ec.trade.facade.dto.user.UserAcctDto;
import com.cmwa.ec.trade.facade.dto.user.UserRiskLevelDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.client.UserServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.interceptor.TracingInfo;
import com.cmwa.ec.weixin.manager.business.ActivityManager;
import com.cmwa.ec.weixin.manager.business.MessageManager;
import com.cmwa.ec.weixin.manager.business.TradeManager;
import com.cmwa.ec.weixin.manager.business.UserInfoexManager;
import com.cmwa.ec.weixin.util.ContextUtils;
import com.cmwa.ec.weixin.util.DateUtils;
import com.cmwa.ec.weixin.util.MD5;
import com.cmwa.ec.weixin.util.RequestHelper;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.WeixinUtil;

import net.sf.json.JSONObject;

/**
 * 类说明：交易模块请求处理
 * @author wudb
 *
 */
@Controller("TradeController")
@RequestMapping(value="/WeixinService")
public class TradeController {

	private static Logger logger = Logger.getLogger(TradeController.class.getName());
	
	@Autowired
	private TradeManager tradeManager;
	
	@Autowired
	private MessageManager messageManager;
	 @Autowired
	private UserServiceClient userServiceClient;
	@Autowired
	private UserInfoexManager userInfoexManager;
	@Autowired
	private ActivityManager activityManager;
	@Autowired
	private MessageServiceClient messageServiceClient;
	
	@Autowired
	private QueryServiceClient queryServiceClient;
	
	/**
	 * 用户下预约单
	 */
	@RequestMapping(value="/business/fundAppoint.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST,RequestMethod.GET})
	@TracingInfo(authority="30")
	@ResponseBody  
	public String fundAppoint(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String mobile = null;
		String custNo = null;
		JSONObject returnJsonObject = null;
		String fundId = request.getParameter("fundId");
		String apkind = request.getParameter("apkind");
		String tradeAmt = request.getParameter("tradeAmt");//支付金额
		tradeAmt = URLDecoder.decode(tradeAmt, "utf-8");
		String fee = request.getParameter("fee");//认购费
		String commro = request.getParameter("commro");//折扣
		String renew = request.getParameter("renew");//续投方式
		String channelId = WXConstants.TRADE_CHANEL_03;
		
		apkind = FundTradeDto.getApkind(apkind.charAt(0));
		
		UserBaseInfoDto userBaseInfo = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		if(userBaseInfo == null || userAccoRla == null){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
			returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
			return returnJsonObject.toString();
		}else{
			custNo = userAccoRla.getEcCustNo();
			mobile = userBaseInfo.getMobile();
		}
		
		
		String busiChannel=RequestHelper.verfiyIEChannel(request);
		String sessionId = request.getSession().getId();
		String cmfUserId = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}
		
		logger.info("TradeController.class的fundAttention()中>>>cmfUserId="+cmfUserId+">>>System.currentTimeMillis()="+cmfUserId+System.currentTimeMillis());
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		
		FundTradeDto fundTradeDto = new FundTradeDto();
	    fundTradeDto.setCustNo(custNo); 
	    fundTradeDto.setApkind(apkind); 
	    fundTradeDto.setFundId(fundId);
	    fundTradeDto.setTradeAmt(tradeAmt);
	    fundTradeDto.setPayType("2"); //2:预下单  ， 1：线下   ， 0：线上
	    fundTradeDto.setMobileNo(mobile);
	    fundTradeDto.setChannelId(channelId);
	    fundTradeDto.setFee(fee);
	    fundTradeDto.setCommro(commro);
	    fundTradeDto.setRenew(renew);
		//调用交易Manager
	    returnJsonObject = tradeManager.fundAppoint(context, fundTradeDto);
	    
	    return returnJsonObject.toString();
	}
	
	
	/**
	 * 下交易单
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author wudb
	 */
	@RequestMapping(value="/business/fundTrade.xhtml" , produces="text/html;charset=UTF-8" , method = {RequestMethod.POST,RequestMethod.GET})
	@TracingInfo(authority="30")
	@ResponseBody
	public String fundTrade(HttpServletResponse response,HttpServletRequest request)throws Exception{
		String cmfUserId = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}
		
		String sessionId = request.getSession().getId();
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		logger.info("TradeController类【fundTrade】开始>>>cmfUserId:"+cmfUserId+">>>timestamp:"+System.currentTimeMillis());
		
		String custNo = null;
		String mobile = null;
		JSONObject returnJsonObject = null;
		// 验证支付密码成功后保存的标识符
		//String checkedTpassword = (String) request.getSession(true).getAttribute("checkedTpassword");
		UserBaseInfoDto userBaseInfo = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		if(userBaseInfo == null || userAccoRla == null){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
			returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
			return returnJsonObject.toString();
		}else{
			custNo = userAccoRla.getEcCustNo();
			mobile = userBaseInfo.getMobile();
		}
		String payMobile = mobile;
		String tradeAcco = request.getParameter("tradeAcco");
		String tpassword = request.getParameter("tPassword");
		String fundId = request.getParameter("fundId");
		String serialNo = request.getParameter("serialno");
		String apkind = request.getParameter("apkind");
		String tradeAmt = request.getParameter("tradeAmt");
		tradeAmt = URLDecoder.decode(tradeAmt, "utf-8");
		String fee = request.getParameter("fee");
		String commro = request.getParameter("commro");
		String feeMode = request.getParameter("feeMode");
		String payType = request.getParameter("payType");
		String channelId = WXConstants.TRADE_CHANEL_03;
		String contractVer = request.getParameter("contractVer");
		String rvrfcode = request.getParameter("verifyCode");
		String sessionID = request.getParameter("sessionID");
		String renew = request.getParameter("renew");
		
		/**
		 * 如果serialNo的不为空，则代表该订单是预约的订单跳转支付，将该订单作为预约订单处理。
		 * 即apkind = "A2T"（从页面传过来）
		 * 
		 * 否则：直接处理该订单
		 * 即apkind = FundTradeDto.getApkind(apkind.charAt(0));
		 */
		if("".equals(apkind)){
			apkind = "A2T";
		}
		
		//关键参数 非空
		if(StringUtils.isEmpty(tradeAmt) || !StringUtils.isNumeric(tradeAmt) || StringUtils.isEmpty(tradeAcco) || StringUtils.isEmpty(contractVer) || StringUtils.isEmpty(apkind)){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("resultCode", WXConstants.RETURN_CODE_9008);
			returnJsonObject.put("resultMsg", WXConstants.RETURN_MSG_9008);
			logger.info("TradeController类【fundTrade】中参数不正确》》》入参：sessionID"+sessionID+"》》》cmfUserId"+cmfUserId+"》》》mobile"+mobile+"》》》tradeAmt"+tradeAmt+"》》》返回信息：returnJsonObject="+returnJsonObject);
			return returnJsonObject.toString();
		}
		
		MD5 md5 = new MD5();
		if(tpassword != null && !tpassword.equals("")){
			tpassword = md5.getMD5ofStr(tpassword);
		}
		
		//日志信息
		/***************************** 线上支付   需要验证短信验证码 S ****************************************************/
		if(payType != null && payType.equals("0")){
			returnJsonObject = new JSONObject();
			String mobileByMsg = (String) request.getSession(true).getAttribute("mobileByMsg");
			if(mobileByMsg.equals(payMobile)){// 验证短信验证码
				if(rvrfcode != null && !rvrfcode.equals("")){
					rvrfcode = rvrfcode.trim();
				}
				Context context=ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, busiChannel,cmfUserId, System.currentTimeMillis(), 
						System.currentTimeMillis(), sessionId, "");
				String msgCode=messageManager.checkVrfCode(context,sessionID, mobile, rvrfcode);
				if(msgCode==null || msgCode ==""){
					returnJsonObject.put("returnCode", "9999");
					returnJsonObject.put("returnMsg", "验证短信验证码失败");
					logger.info("TradeController类【fundTrade】中验证短信验证码失败》》》入参：sessionID"+sessionID+"》》》mobile"+mobile+"》》》rvrfcode"+rvrfcode+"》》》返回信息：returnJsonObject="+returnJsonObject);
					return returnJsonObject.toString();
				}if(msgCode!=null && msgCode !="" && !WXConstants.COMMON_SUCCESS.equals(msgCode)){
					returnJsonObject.put("returnCode", "6001");
					returnJsonObject.put("returnMsg", "验证短信验证码失败");
					logger.info("TradeController类【fundTrade】中验证短信验证码失败》》》入参：sessionID"+sessionID+"》》》mobile"+mobile+"》》》rvrfcode"+rvrfcode+"》》》返回信息：returnJsonObject="+returnJsonObject);
					return returnJsonObject.toString();
				} 
			}else{
				returnJsonObject.put("returnCode", "0099");
				returnJsonObject.put("returnMsg", "请重新获取并验证手机短信验证码");
				logger.info("TradeController类【fundTrade】中验证短信验证码失败：没有获取到支付手机号码与该获取验证码的手机号不一致");
				return returnJsonObject.toString();
			}
		}
		/***************************** 线上支付   需要验证短信验证码 E ****************************************************/
		
		
		/***************************** 验证支付密码 E ****************************************************/
		FundTradeDto fundTradeDto = new FundTradeDto();
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
	    fundTradeDto.setFeeMode(feeMode);
	    
		String appDt = DateUtils.formatDate(new Date(),DateUtils.yyyyMMdd);
		SignEContractDto elDto = new SignEContractDto();
		String ip = RequestHelper.getIpAddr(request);
		elDto.setCustNo(custNo);// 客户号
		elDto.setTradeAcco(tradeAcco);// 交易账号
		elDto.setFundId(fundId);// 基金代码
		elDto.setAppDt(appDt);// 签署日期
		elDto.setContractVer(contractVer); // 合同版本
		elDto.setContractTp("1");// 合同类型
		elDto.setSignChannel("2");// 合同签署途径
		elDto.setSignMachine(ip);// 合同签署机器
		logger.info("TradeController类【fundTrade】中传给服务端的参数>>>fundTradeDto="+fundTradeDto.toString()
				+"SignEContractDto="+elDto);
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), 
				System.currentTimeMillis(), sessionId, "");
		returnJsonObject= tradeManager.fundTrade(context, fundTradeDto,elDto);
		// 购买成功，则删除session中已保存的【验证支付密码标识】的状态
		if(returnJsonObject !=null && WXConstants.COMMON_SUCCESS.equals(returnJsonObject.get("returnCode"))){
			request.getSession(true).removeAttribute("checkedTpassword");
			logger.info("TradeController类【fundTrade】中付款验证支付密码成功，删除session");
		}
		
		logger.info("TradeController类【fundTrade】结束>>>returnJsonObject="+returnJsonObject.toString());
		return returnJsonObject.toString();
	
	}
	
	/**
	 * 下排队单
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author wudb
	 */
	@RequestMapping(value="/business/fundTradeLineUp.xhtml" , produces="text/html;charset=UTF-8" , method = {RequestMethod.POST,RequestMethod.GET})
	@TracingInfo(authority="30")
	@ResponseBody
	public String fundTradeLineUp(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String cmfUserId = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}
		
		String seqId = request.getSession().getId();
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		logger.info("TradeController类【fundTradeLineUp】开始>>>cmfUserId:"+cmfUserId+">>>timestamp:"+System.currentTimeMillis());
		
		String custNo = null;
		String mobile = null;
		JSONObject jsonObject = null;

		UserBaseInfoDto userBaseInfo = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		if(userBaseInfo == null || userAccoRla == null){
			jsonObject = new JSONObject();
			jsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
			jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
			return jsonObject.toString();
		}else{
			custNo = userAccoRla.getEcCustNo();
			mobile = userBaseInfo.getMobile();
			
		}
		String payMobile = mobile;
		String tradeAcco = request.getParameter("tradeAcco");
		String tpassword = request.getParameter("tPassword");
		String fundId = request.getParameter("fundId");
		String serialNo = request.getParameter("serialno");
		String apkind = request.getParameter("apkind");
		String tradeAmt = request.getParameter("tradeAmt");
		tradeAmt = URLDecoder.decode(tradeAmt, "utf-8");
		String fee = request.getParameter("fee");
		String commro = request.getParameter("commro");
		String payType = request.getParameter("payType");
		String channelId = WXConstants.TRADE_CHANEL_03;
		String contractVer = request.getParameter("contractVer");
		String rvrfcode = request.getParameter("verifyCode");
		String sessionID = request.getParameter("sessionID");
		String renew = request.getParameter("renew");
		
		
		/**
		 * 如果serialNo的不为空，则代表该订单是预约的订单跳转支付，将该订单作为预约订单处理。
		 * 即apkind = "A2T"（从页面传过来）
		 * 
		 * 否则：直接处理该订单
		 * 即apkind = FundTradeDto.getApkind(apkind.charAt(0));
		 */
		apkind = FundTradeDto.getApkind(apkind.charAt(0));
		
		MD5 md5 = new MD5();
		if(tpassword != null && !tpassword.equals("")){
			tpassword = md5.getMD5ofStr(tpassword);
		}
		
		/***************************** 线上支付   需要验证短信验证码 S ****************************************************/
		if(payType != null && payType.equals("0")){
			jsonObject = new JSONObject();
			String mobileByMsg = (String) request.getSession(true).getAttribute("mobileByMsg");
			if(mobileByMsg.equals(payMobile)){// 验证短信验证码
				if(rvrfcode != null && !rvrfcode.equals("")){
					rvrfcode = rvrfcode.trim();
				}
				Context context = ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
				String msgCode = messageManager.checkVrfCode(context, sessionID, mobile, rvrfcode);
				if (msgCode == null || msgCode == "") {
					jsonObject.put("returnCode", "9999");
					jsonObject.put("returnMsg", "验证短信验证码失败");
					logger.info("TradeController类【fundTradeLineUp】中验证短信验证码失败》》》入参：sessionID" + sessionID + "》》》mobile" + mobile + "》》》rvrfcode" + rvrfcode + "》》》返回信息：returnJsonObject=" + jsonObject);
					return jsonObject.toString();
				}
				if (msgCode != null && msgCode != "" && !WXConstants.COMMON_SUCCESS.equals(msgCode)) {
					jsonObject.put("returnCode", "6001");
					jsonObject.put("returnMsg", "验证短信验证码失败");
					logger.info("TradeController类【fundTradeLineUp】中验证短信验证码失败》》》入参：sessionID" + sessionID + "》》》mobile" + mobile + "》》》rvrfcode" + rvrfcode + "》》》返回信息：returnJsonObject=" + jsonObject);
					return jsonObject.toString();
				}
			}else{
				jsonObject.put("returnCode", "0099");
				jsonObject.put("returnMsg", "请重新获取并验证手机短信验证码");
				logger.info("TradeController类【fundTradeLineUp】中验证短信验证码失败：没有获取到支付手机号码与该获取验证码的手机号不一致");
				return jsonObject.toString();
			}
		}
		/***************************** 线上支付   需要验证短信验证码 E ****************************************************/
		
		
		/***************************** 验证支付密码 S ****************************************************/
		{
			Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
			String manageType = "V";
			jsonObject = userInfoexManager.manageTpassword(context, cmfUserId, tpassword, "", manageType, "90", WXConstants.TRADE_CHANEL_03);
			//支付密码正确则更新session信息，将支付密码是否正确的标识符放入到session中
			if(!("USR-1I00").equals(jsonObject.get("returnCode"))){
				//密码验证未通过
				return jsonObject.toString();
			}
		}
		
		/***************************** 验证支付密码 E ****************************************************/
		
		FundTradeDto fundTradeDto = new FundTradeDto();
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
		String appDt = DateUtils.formatDate(new Date(),DateUtils.yyyyMMdd);
		SignEContractDto elDto = new SignEContractDto();
		String ip = RequestHelper.getIpAddr(request);
		elDto.setCustNo(custNo);// 客户号
		elDto.setTradeAcco(tradeAcco);// 交易账号
		elDto.setFundId(fundId);// 基金代码
		elDto.setAppDt(appDt);// 签署日期
		elDto.setContractVer(contractVer); // 合同版本
		elDto.setContractTp("1");// 合同类型
		elDto.setSignChannel("2");// 合同签署途径
		elDto.setSignMachine(ip);// 合同签署机器
		logger.info("TradeController类【fundTradeLineUp】中传给服务端的参数>>>fundTradeDto=" + fundTradeDto.toString() + "SignEContractDto="+elDto);
		
		Context context = ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		OrderResult orderResult = tradeManager.fundTradeLineUp(context, fundTradeDto,elDto);
		FundTradeDto resultFundTradeDto = null;
		String returnCode = "";
		String returnMsg = "";
		
		if (orderResult != null) {
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
			if (WXConstants.COMMON_SUCCESS.equals(returnCode)) {
				resultFundTradeDto = (FundTradeDto) orderResult.getData();
			}
			if (resultFundTradeDto != null) {
				resultFundTradeDto.setCustNo("");
			}
		}
		
		if(resultFundTradeDto != null){
			jsonObject.put("fundTradeDto", resultFundTradeDto);
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		// 购买成功，则删除session中已保存的【验证支付密码标识】的状态
		if(returnCode != null && "0000".equals(returnCode)){
			request.getSession(true).removeAttribute("checkedTpassword");
			logger.info("TradeController类【fundTradeLineUp】中付款验证支付密码成功，删除session");
		}
		
		logger.info("TradeController类【fundTrade】结束>>>returnJsonObject="+jsonObject.toString());
		return jsonObject.toString();
		
	}
	
	
	/**
	 * 用户信息鉴权，开户
	 */
	@RequestMapping(value="/business/openAccount.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@TracingInfo(authority="10")
	@ResponseBody
	public String openAccount(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		JSONObject returnJsonObject = null;
		
		Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		Object obj2 = request.getSession(true).getAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE);
		
		if(obj == null){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
			returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
			return returnJsonObject.toString();
		}
		
		if(obj2==null){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_ILLEGALREQCODE);
			returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_ILLEGALREQMSG);
			return returnJsonObject.toString();
		}
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,"", System.currentTimeMillis(), System.currentTimeMillis(), "", "");
		UserBaseInfoDto userInfo = (UserBaseInfoDto)obj;
		String cmfUserId = userInfo.getCmfUserId();
		String mobile = obj2.toString();
		String userName = request.getParameter("name");
		userName = URLDecoder.decode(userName, "utf-8");
		String idNo = request.getParameter("idNo");
		String idType = "0";//证件类型 ：身份证 0
		String bankNumber = request.getParameter("bankNumber");
		String bankName = request.getParameter("bankName");
		bankName = URLDecoder.decode(bankName, "utf-8");
		String channelNo = request.getParameter("channelNo");//渠道代码
		String bankNo = request.getParameter("bankNo");//银行代码
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
			userAcctDto.setVoccode(WXConstants.VOCCODE_15);
		}
		userAcctDto.setIdExpireDate(StringUtils.isBlank(idExpireDate) ? null : idExpireDate.replaceAll("-", "").trim());
		returnJsonObject = tradeManager.openAccount(context, request,userAcctDto);
		
		Object returnCode = returnJsonObject.get("returnCode");
		if(returnCode!=null && WXConstants.COMMON_SUCCESS.equals(returnCode.toString())){
			//鉴权成功后，删除获取验证码和验证验证码时往session中存放的mobile
			request.getSession(true).removeAttribute(SessionValue.SESSION_AUTHMOBILE);
			request.getSession(true).removeAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE);
			
			//验证是否是微信浏览器发起请求
			String ieType = request.getHeader("user-agent").toLowerCase();
			if(ieType.indexOf("micromessenger")<0){//其他浏览器
				logger.info("openAccount()方法，其他浏览器发起鉴权请求，不做分组处理");
			}else{//微信浏览器
				
				Object obj1 = request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
				if(obj1!=null){
					//修改用户分组为已鉴权分组
					userInfoexManager.modifyUserGroup(obj1.toString(), WXConstants.AUTHORITYGROUPID);
				}
				
			}
		}
		
		return returnJsonObject.toString();
	}
	
	
	/**
	 * 添加银行卡
	 */
	@RequestMapping(value="/business/addBank.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@TracingInfo(authority="30")
	@ResponseBody
	public String addBank(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		JSONObject returnJsonObject = null;
		
		Object obj2 = request.getSession(true).getAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE);
		Object obj3 = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		Object obj4 = request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		if(obj3 == null || obj4==null){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
			returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
			return returnJsonObject.toString();
		}
		
		if(obj2==null){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_ILLEGALREQCODE);
			returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_ILLEGALREQMSG);
			return returnJsonObject.toString();
		}
		
		UserBaseInfoDto userInfo = (UserBaseInfoDto)obj3;
		UserAccoRlaDto userAcc = (UserAccoRlaDto)obj4;
		String ecCustNo = userAcc.getEcCustNo();
		String cmfUserId = userInfo.getCmfUserId();
		String userName = userInfo.getCustName();
		String idNo = userInfo.getIdNo();
		
		String mobile = obj2.toString();
		String idType = "0";//证件类型 ：身份证 0
		String bankNumber = request.getParameter("bankNumber");
		String bankName = request.getParameter("bankName");
		bankName = URLDecoder.decode(bankName, "utf-8");
		String channelNo = request.getParameter("channelNo");//渠道代码
		String bankNo = request.getParameter("bankNo");//银行代码
		
		//银行卡号只能是数字类型
		if(!StringUtils.isNumeric(bankNumber)){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", WXConstants.RETURN_CODE_9008);
			returnJsonObject.put("returnMsg", WXConstants.RETURN_MSG_9008);
			return returnJsonObject.toString();
		}
		
		//银行名称 如非空 且 不是全中文
		String bankNameReplace = bankName.replaceAll("[\\u4e00-\\u9fa5]", "**");
		if(!StringUtils.isEmpty(bankName) && ((bankName.length() * 2 - bankNameReplace.length()) > 0)){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", WXConstants.RETURN_CODE_9008);
			returnJsonObject.put("returnMsg", WXConstants.RETURN_MSG_9008);
			return returnJsonObject.toString();
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
		
		String sessionId = request.getSession().getId();
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		
		returnJsonObject = tradeManager.addBankNumber(context, request,userAcctDto);
		
		Object returnCode = returnJsonObject.get("returnCode");
		if(returnCode!=null && WXConstants.COMMON_SUCCESS.equals(returnCode.toString())){
			//鉴权成功后，删除获取验证码和验证验证码时往session中存放的mobile,保存鉴权成功的标志
			request.getSession(true).removeAttribute(SessionValue.SESSION_AUTHMOBILE);
			request.getSession(true).removeAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE);
		}
		
		return returnJsonObject.toString();
	}
	
	/**
	 * 风险测评
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			ModelAndView
	 * @author maj
	 */
	@RequestMapping(value="/business/riskRating.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	@TracingInfo(authority="10")
	public String riskRating(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		String seqId = request.getSession().getId();

		logger.info("【TradeController】riskRating()开始>>>seqId="+seqId+">>>busiChannel="+busiChannel+">>>timestamp="+System.currentTimeMillis());
		
		UserBaseInfoDto userBaseInfo = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		
		String returnCode = "";
		String returnMsg = "";
		String cmfUserId = "";
	    String userType="";
		if (userBaseInfo!=null) {
			cmfUserId=userBaseInfo.getCmfUserId();
			userType=userBaseInfo.getUserType();
		}
		String ecCustNo = "";
		if(userBaseInfo == null){
			returnCode = "9001";
			returnMsg = "用户 未登录";
		}
		if(userAccoRla == null){
			returnCode = "9002";
			returnMsg = "用户 未鉴权";
		}else{
			ecCustNo = userAccoRla.getEcCustNo();
		}
		
		
		String score = request.getParameter("score");
		String num = request.getParameter("num");
		num = tradeManager.parseEvalAnswerBySetUserRiskLevel(num);
		int scoreCount = Integer.parseInt((score==null||"".equals(score))?"0":score);
		
		String riskLevel = "";
		String scoreName = "";
		if(scoreCount >= 24 && scoreCount<= 34){
			scoreName = "C1-保守型";
			riskLevel = "1";
		}else if(scoreCount >= 35 && scoreCount<= 48){
			scoreName = "C2-稳健型";
			riskLevel = "2";
		}else if(scoreCount >= 49 && scoreCount<= 62){
			scoreName = "C3-平衡型";
			riskLevel = "3";
		}else if(scoreCount >= 63 && scoreCount<= 80){
			scoreName = "C4-成长型";
			riskLevel = "4";
		}else if(scoreCount >= 81 ){
			scoreName = "C5-积极型";
			riskLevel = "5";
		}else{
			scoreName = "C1-保守型";
			riskLevel = "1";
		}
		
		SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat format2 = new SimpleDateFormat("HHmmss");
		SimpleDateFormat format3 = new SimpleDateFormat("yyyy年MM月dd日");
		SimpleDateFormat format4 = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		UserRiskLevelDto userRiskLevel = new UserRiskLevelDto();
		Date nowDate = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(nowDate);
		calendar.add(Calendar.YEAR, 1);
		String evalDispDateTime = "";
		String evalDateTimeToSession="";
		String evalValiDate = "";
		
		//初始化 数据
		evalDispDateTime = format3.format(nowDate);
		evalDateTimeToSession = format4.format(nowDate);
		evalValiDate = format3.format(calendar.getTime());
		userRiskLevel.setCustNo(ecCustNo);
		userRiskLevel.setCmfUserId(cmfUserId);
		userRiskLevel.setEvalFormno(score);
		userRiskLevel.setEvalDate(format1.format(nowDate));
		userRiskLevel.setEvalTime(format2.format(nowDate));
		userRiskLevel.setRiskLevel(riskLevel);
		userRiskLevel.setChannelCode("NET");
		userRiskLevel.setTerminalInfo(RequestHelper.getIpAddr(request));
		userRiskLevel.setEvalType("M");
		userRiskLevel.setStatus("N");
		userRiskLevel.setEvalAnswer(num);
		logger.info("NUM:"+num);
		logger.info("userRiskLevel:"+userRiskLevel);
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		JSONObject json = tradeManager.riskRating(context, userRiskLevel,userType);
		
		returnCode = json.getString("returnCode");
		returnMsg = json.getString("returnMsg");
		riskLevel = json.getString("riskLevel");
		
		userBaseInfo.setRiskLevel(riskLevel);
		request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfo);
		request.getSession(true).setAttribute("RISKLEVEL",riskLevel);
		request.getSession(true).setAttribute("RISKEVALDATE",evalDateTimeToSession);
		
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		jsonObject.put("scoreCount", score);
		jsonObject.put("scoreName", scoreName);
		jsonObject.put("riskLevelDto", json.get("userRiskLevelDto"));
		
		jsonObject.put("riskLevel", riskLevel);
		jsonObject.put("riskEvalDate", evalDispDateTime);
		jsonObject.put("evalValiDate", evalValiDate);

		logger.info("【TradeController】riskRating()结束>>>jsonObject="+jsonObject.toString()+">>>timestamp="+System.currentTimeMillis());
		String apptp = request.getParameter("apptp");
		if("1".equals(apptp)){
			userRiskLevel.setApptp(apptp);
			tradeManager.appConversionUserInvtp(context, request,userRiskLevel);
		}
		return jsonObject.toString();
	}
	/**
	 * 赎回订单
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/business/redemptionOrder.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String redemptionOrder(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		// 日志封装类
		Context context = ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, "", cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		String custNo = "";
		String tradeAcco = request.getParameter("tradeAcco");
		String serialNo = request.getParameter("serialNo");
		String fundid = request.getParameter("fundid");
		String mobile = request.getParameter("mobile");
		String money = request.getParameter("money");
		if(userAccoRla != null){
			custNo = userAccoRla.getEcCustNo();
		}
		if(StringUtils.isEmpty(custNo)){
			custNo = request.getParameter("custno");
		}
		FundTradeDto fundTradeDto = new FundTradeDto();
		fundTradeDto.setSerialNo(serialNo);
		fundTradeDto.setTradeAcco(tradeAcco);
		fundTradeDto.setFundId(fundid);
		fundTradeDto.setMobileNo(mobile);
		fundTradeDto.setTradeAmt(money);
		fundTradeDto.setChannelId("04");
		fundTradeDto.setCustNo(custNo);
		JSONObject jsonObject = tradeManager.redemptionOrder(context, request,fundTradeDto,cmfUserId);
		return jsonObject.toString();
	}
	/**
	 * 解除绑定银行卡
	 */
	@RequestMapping(value="/business/canalBindBankCard.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@TracingInfo(authority="30")
	@ResponseBody
	public String canalBindBankCard(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		
		String tradeAcc = request.getParameter("tradeAcco");
		JSONObject json = null;
		Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(obj == null){
			json = new JSONObject();
			json.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
			json.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
			return json.toString();
		}
		String custNo = "";
        UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
        if(obj != null && userAccoRla != null){
            custNo = userAccoRla.getEcCustNo();
        }
		if(tradeAcc == null || "".equals(tradeAcc)){
			json = new JSONObject();
			json.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
			json.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
			return json.toString();
		}
		UserBaseInfoDto userInfo = (UserBaseInfoDto)obj;
		String cmfUserId = userInfo.getCmfUserId();
		String sessionId = request.getSession().getId();
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		json = tradeManager.cancelBindBankCard(context, tradeAcc, custNo);
		
		return json.toString();
	}
	
	/**
	 * 信批已读保存
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value="/business/addReportReadRecord.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@TracingInfo(authority="10")
	@ResponseBody
	public String addReportReadRecord(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String seqId = request.getSession(true).getId();
		String busiChannel = RequestHelper.verfiyIEChannel(request);

		logger.info("【TradeController】addReportReadRecord()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());
		
		String fundId = request.getParameter("fundId");
		String reportId = request.getParameter("reportId");
		
		String cmfUserId = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}
		
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0000";
		String returnMsg = "成功";
		
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		tradeManager.addReportReadRecord(context, cmfUserId, fundId, reportId);
		
		/* 不需要返回任何数据 */
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("【TradeController】addReportReadRecord()结束>>>jsonObject=" + jsonObject );
		
		return jsonObject.toString();
	}
	
	/**
	 * 取消订单
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value="/business/cancelAppointRequest.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@TracingInfo(authority="30")
	@ResponseBody
	public String cancelAppointRequest(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String seqId = request.getSession(true).getId();
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		String cmfUserId = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}
		
		logger.info("【TradeController】cancelAppointRequest()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());
		
		String serialno = request.getParameter("serialno");
		String tradeAcco = request.getParameter("tradeAcco");
		
		logger.info("【TradeController】cancelAppointRequest()获取前台参数>>>serialno=" + serialno + ">>>tradeAcco=" + tradeAcco);
		String custNo = "";
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		if(userBaseInfoDto != null && userAccoRla != null){
			custNo = userAccoRla.getEcCustNo();
		}
		FundTradeDto fundTradeDto = new FundTradeDto();
		fundTradeDto.setSerialNo(serialno);
		fundTradeDto.setTradeAcco(tradeAcco);
		fundTradeDto.setCustNo(custNo);
		
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		int tradeAcoCount = userServiceClient.queryTradeaccoExistCount(tradeAcco); 
		if(null != ""+tradeAcoCount && tradeAcoCount <= 0){
			jsonObject.put("returnCode", "9999");
			jsonObject.put("returnMsg", "交易账号不存在！");
			return jsonObject.toString();
		}
		
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		OrderResult orderResult = tradeManager.cancelAppointRequest(context, fundTradeDto);
		
		if(orderResult != null){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
		}
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("【TradeController】cancelAppointRequest()结束>>>jsonObject=" + jsonObject + ">>>timestamp=" + System.currentTimeMillis());
		
		return jsonObject.toString();
	}
	
	/**
	 * 修改订单金额
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value="/business/modifyAppointRequest.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@TracingInfo(authority="30")
	@ResponseBody
	public String modifyAppointRequest(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String seqId = request.getSession(true).getId();
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		String cmfUserId = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}
		String custNo = "";
        UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
        if(userBaseInfoDto != null && userAccoRla != null){
            custNo = userAccoRla.getEcCustNo();
        }
		logger.info("【TradeController】modifyAppointRequest()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());
		
		String serialno = request.getParameter("serialno");
		String tradeAcco = request.getParameter("tradeAcco");
		String tradeAmt = request.getParameter("tradeAmt");
		String tpassword = request.getParameter("tPassWord");
		String randomCode = request.getParameter("randomCode");
		String fee = request.getParameter("fee");
		String mobile = request.getParameter("mobile");
		String renew = request.getParameter("renew");
		
		logger.info("【TradeController】modifyAppointRequest()获取前台参数>>>serialno=" + serialno + ">>>tradeAcco=" + tradeAcco + ">>>tradeAmt=" + tradeAmt + ">>>randomCode=" + randomCode + ">>>fee=" + fee);

		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
			/****************** 验证图片验证码 S *****************************************************************/
			{
				Map<String, String> map = VerifyCodeController.verifyRandom(request, randomCode);
				if (map == null || !(map.get("returnCode").equals("0000"))) {
					jsonObject.put("returnCode", map.get("returnCode"));
					jsonObject.put("returnMsg", map.get("returnMsg"));
					return jsonObject.toString();
				}
			}
			/****************** 验证图片验证码 E *****************************************************************/
			
			
			/****************** 验证支付密码 S *****************************************************************/
			{
				MD5 md5 = new MD5();
				if(tpassword != null && !tpassword.equals("")){
					tpassword = md5.getMD5ofStr(tpassword);
				}
				
				String manageType = "V";
				jsonObject = userInfoexManager.manageTpassword(context, cmfUserId, tpassword, "", manageType, "90", WXConstants.TRADE_CHANEL_03);
				if (!("USR-1I00").equals(jsonObject.get("returnCode"))) {
					// 密码验证未通过
					return jsonObject.toString();
				}
			}
			/****************** 验证支付密码 E *****************************************************************/
		
		
		FundTradeDto fundTradeDto = new FundTradeDto();
		fundTradeDto.setSerialNo(serialno);
		fundTradeDto.setTradeAcco(tradeAcco);
		fundTradeDto.setTradeAmt(tradeAmt);
		fundTradeDto.setFee(fee);
		fundTradeDto.setCustNo(custNo);
		fundTradeDto.setRenew(renew);
		
		
		OrderResult orderResult = tradeManager.modifyAppointRequest(context, fundTradeDto);
		
		if(orderResult != null){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
		}
		
		if (null!=returnCode && "0000".equals(returnCode) && null!=renew && !"".equals(renew)) {
		    //日志信息
		    Context contexts = ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, WXConstants.SERVICE_CHANNEL_WEIXIN,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
			String renewDesc="";
			if ("Y".equals(renew)) {
				renewDesc="到期自动续投";
			}else{
				renewDesc="到期自动赎回";
			}
		    MsgParameterDto msgParameter = new MsgParameterDto();
		    UserBaseInfoDto userBaseInfo = RequestHelper.getSessionUserBaseInfo(request);
			if(userBaseInfo == null){
				
			}else{
				mobile = userBaseInfo.getMobile();
			}
		    if (mobile!=null) {
				msgParameter.setMobile(mobile);
				msgParameter.setMsgType("24");
			    msgParameter.setMsgPar_str1(serialno);
			    msgParameter.setMsgPar_str2(renewDesc);
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
				messageManager.sendSmsMsg(contexts, msgParameter, msgRecord);
		    }
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("【TradeController】modifyAppointRequest()结束>>>jsonObject=" + jsonObject + ">>>timestamp=" + System.currentTimeMillis());
		
		return jsonObject.toString();
	}
	
	/**
	 * 修改用户注册手机号码 首先验证手机短信验证码
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value="/business/modifyUserMobile.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@TracingInfo(authority="30")
	@ResponseBody
	public String modifyUserMobile(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String seqId = request.getSession(true).getId();
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		String cmfUserId = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}
		
		logger.info("【TradeController】modifyUserMobile()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());
		
		String mobile = request.getParameter("mobile");
    	String smsCode = request.getParameter("smsCode");
    	String sessionID = request.getParameter("sessionID");
		String lPassword = request.getParameter("sessionID");
		
		logger.info("【TradeController】modifyUserMobile()获取前台参数>>>mobile=" + mobile + ">>>smsCode=" + smsCode + ">>>sessionID=" + sessionID);
		
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

		/****************** 验证短信验证码 S *****************************************************************/
		String userMobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_USERMOBILE);
		if(userMobile == null || !userMobile.equals(mobile)){
			jsonObject.put("returnCode", "9000");
			jsonObject.put("returnMsg", "未获取短信验证码");
			logger.info("【TradeController】modifyUserMobile()中验证短信验证码失败：没有获取到支付手机号码与该获取验证码的手机号不一致");
			return jsonObject.toString();
		}
		
		if(smsCode != null && !smsCode.equals("")){
			smsCode = smsCode.trim();
		}
		
		String msgCode = messageManager.checkVrfCode(context,sessionID, mobile, smsCode);
//		*	验证失败，验证码错误				USR-5201
//			验证失败,会话失效					USR-5202
//		 *	验证失败，会话已验证				USR-5203
		if(msgCode == null || msgCode.equals("")){
			jsonObject.put("returnCode", "9999");
			jsonObject.put("returnMsg", "验证短信验证码失败");
			logger.info("【TradeController】modifyUserMobile()中验证短信验证码失败>>>jsonObject="+jsonObject);
			return jsonObject.toString();
		}else if("USR-5202".equals(msgCode) || "USR-5203".equals(msgCode)){
			jsonObject.put("returnCode", "6002");
			jsonObject.put("returnMsg", "验证失败，会话已验证");
			logger.info("【TradeController】modifyUserMobile()中验证短信验证码失败》》》jsonObject="+jsonObject);
			return jsonObject.toString();
		}else if(!"0000".equals(msgCode)){
			jsonObject.put("returnCode", "6001");
			jsonObject.put("returnMsg", "验证短信验证码失败");
			logger.info("【TradeController】modifyUserMobile()中验证短信验证码失败》》》jsonObject="+jsonObject);
			return jsonObject.toString();
		}
		/****************** 验证短信验证码 E *****************************************************************/
		String hasSetLPsw = (String) request.getSession(true).getAttribute(SessionValue.SESSION_HASSETLPSW);
		String operatorType = "1";
		if(hasSetLPsw != null && hasSetLPsw.equals("Y")){
			jsonObject.put("returnCode", msgCode);
			return jsonObject.toString();
		}

		/****************** 修改用户注册手机号码 S *****************************************************************/
		UserServiceMessage userServiceMessage = userInfoexManager.modifyRegMobile(context, cmfUserId, mobile, lPassword, operatorType, WXConstants.TRADE_CHANEL_03);
		
		if(userServiceMessage != null){
			returnCode = userServiceMessage.getReturnCode();
			returnMsg = userServiceMessage.getReturnMsg();
		}
		
		if(returnCode != null && returnCode.equals("0000")){
			request.getSession(true).invalidate();
		}
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("【TradeController】modifyUserMobile()结束>>>jsonObject=" + jsonObject + ">>>timestamp=" + System.currentTimeMillis());
		
		return jsonObject.toString();
	}
	
	/**
	 * 修改用户注册手机号码 首先验证手机短信验证码
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value="/business/modifyMobile.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@TracingInfo(authority="30")
	@ResponseBody
	public String modifyMobile(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String seqId = request.getSession(true).getId();
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		String cmfUserId = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}
		
		logger.info("【TradeController】modifyUserMobile()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());
		
		String mobile = request.getParameter("mobile");
		String lPassword = request.getParameter("lPassWord");
		
		logger.info("【TradeController】modifyUserMobile()获取前台参数>>>mobile=" + mobile);
		
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		String msgMobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_MSGMOBILE);
		if(msgMobile == null || !msgMobile.equals(mobile)){
			jsonObject.put("returnCode", "9000");
			jsonObject.put("returnCode", "手机号码不一致");
			return jsonObject.toString();
		}
		
		String hasSetLPsw = (String) request.getSession(true).getAttribute(SessionValue.SESSION_HASSETLPSW);
		String operatorType = "2";
		if(hasSetLPsw != null && hasSetLPsw.equals("N")){
			jsonObject.put("returnCode", "9001");
			jsonObject.put("returnCode", "不需要设置登录密码");
			return jsonObject.toString();
		}
		
		MD5 md5 = new MD5();
		if(lPassword != null && !lPassword.equals("")){
			lPassword = md5.getMD5ofStr(lPassword);
		}
		
		UserServiceMessage userServiceMessage = userInfoexManager.modifyRegMobile(context, cmfUserId, mobile, lPassword, operatorType, WXConstants.TRADE_CHANEL_03);
		
		if(userServiceMessage != null){
			returnCode = userServiceMessage.getReturnCode();
			returnMsg = userServiceMessage.getReturnMsg();
		}
		if(returnCode != null && returnCode.equals("0000")){
			request.getSession(true).invalidate();
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("【TradeController】modifyUserMobile()结束>>>jsonObject=" + jsonObject + ">>>timestamp=" + System.currentTimeMillis());
		
		return jsonObject.toString();
	}
	
	
	/**
	 * 身份鉴权（找回交易密码）
	 */
	@RequestMapping(value="/business/bankAuthenticationByResetTPass.xhtml" ,produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@TracingInfo(authority="30")
	@ResponseBody
	public String bankAuthenticationByResetTPass(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		
		Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		Object obj2 = request.getSession(true).getAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE);
		
		JSONObject returnJsonObject = null;
		UserBaseInfoDto userBaseInfo = null;
		if(obj==null){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
			returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
			return returnJsonObject.toString();
		}
		
		if(obj2==null){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_ILLEGALREQCODE);
			returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_ILLEGALREQMSG);
			return returnJsonObject.toString();
		}
		
		String mobile = obj2.toString();
		userBaseInfo = (UserBaseInfoDto)obj;
		String cmfUserId = userBaseInfo.getCmfUserId();
		String userName = userBaseInfo.getCustName();
		String idNo = userBaseInfo.getIdNo();
		String idType = "0";//证件类型 ：身份证 0
		String bankNumber = request.getParameter("bankNumber");
		String bankName = request.getParameter("bankName");
		bankName = URLDecoder.decode(bankName, "utf-8");
		String channelNo = request.getParameter("channelNo");//渠道代码
		String bankNo = request.getParameter("bankNo");//银行代码
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
		userAcctDto.setApkind(WXConstants.APKIND_RESETTPASS);
		String sessionId = request.getSession().getId();
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		
		returnJsonObject = tradeManager.authenticate(context,userAcctDto);
		String returnCode = (String)returnJsonObject.get("returnCode");
		if("0000".equals(returnCode)){
			request.getSession(true).setAttribute(SessionValue.SESSION_BANKAUTHSTATUS,"Y");
		}
		
		return returnJsonObject.toString();
	}
	
	/**
	 * 公告已读保存
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value="/article/addArticleReadRecord.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String addArticleReadRecord(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String seqId = request.getSession(true).getId();
		String busiChannel = RequestHelper.verfiyIEChannel(request);

		logger.info("【TradeController】addArticleReadRecord()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());
		
		String articleId = request.getParameter("articleId");
		
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0000";
		String returnMsg = "成功";
		
		String cmfUserId = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}else{
			jsonObject.put("returnCode", returnCode);
			jsonObject.put("returnMsg", returnMsg);
			return jsonObject.toString();
		}
		
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		tradeManager.addArticleReadRecord(context, cmfUserId, articleId);
		
		/* 不需要返回任何数据 */
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("【TradeController】addArticleReadRecord()结束>>>jsonObject=" + jsonObject + ">>>timestamp=" + System.currentTimeMillis());
		
		return jsonObject.toString();
	}
	/**
	 * 父亲节领取电影券
	 * @author luos
	 */
	@RequestMapping(value = "/activity/fatherDayDrawTicket.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String fatherDayDrawTicket(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject = activityManager.fatherDayDrawTicket(request);
		return jsonObject.toString();
	}
	/**
	 * 父亲节分享参数
	 * @author luos
	 */
	@RequestMapping(value = "/activity/fatherDayShare.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String fatherDayShare(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject jsonObject=new JSONObject();
		String ticket = WeixinUtil.getJsapiTicket();
        String url = request.getParameter("url");
        int tempIndex = url.indexOf("#");
        if(tempIndex!=-1){
            url = url.substring(0, tempIndex);
        }
        Map<String, String> map = WeixinUtil.sign(ticket, url);
        String appId = map.get("appId");
        String timestamp = map.get("timestamp");
        String nonceStr = map.get("nonceStr");
        String signature = map.get("signature");
        jsonObject.put("ticket", ticket);
        jsonObject.put("url", url);
        jsonObject.put("appId", appId);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("nonceStr", nonceStr);
        jsonObject.put("signature", signature);
        return jsonObject.toString();
	}
	
	/**
	 * 春节分享参数
	 * @author luos
	 */
	@RequestMapping(value = "/activity/springFestivalShare.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String springFestivalShare(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject jsonObject=new JSONObject();
		String ticket = WeixinUtil.getJsapiTicket();
        String url = request.getParameter("url");
        int tempIndex = url.indexOf("#");
        if(tempIndex!=-1){
            url = url.substring(0, tempIndex);
        }
        Map<String, String> map = WeixinUtil.sign(ticket, url);
        String appId = map.get("appId");
        String timestamp = map.get("timestamp");
        String nonceStr = map.get("nonceStr");
        String signature = map.get("signature");
        jsonObject.put("ticket", ticket);
        jsonObject.put("url", url);
        jsonObject.put("appId", appId);
        jsonObject.put("timestamp", timestamp);
        jsonObject.put("nonceStr", nonceStr);
        jsonObject.put("signature", signature);
        return jsonObject.toString();
	}
	
	
	
	/**
	 * 七夕节答题
	 * @author luos
	 */
	@RequestMapping(value = "/activity/magpieFestivalQA.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String magpieFestivalQA(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject = activityManager.magpieFestivalQA(request);
		return jsonObject.toString();
	}
	/**
	 * 七夕节答题领券
	 * @author luos
	 */
	@RequestMapping(value = "/activity/magpieFestivalDrawTicket.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String magpieFestivalDrawTicket(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject = activityManager.magpieFestivalDrawTicket(request);
		return jsonObject.toString();
	}
	/**
	 * 国庆节答题
	 * @author luos
	 */
	@RequestMapping(value = "/activity/nationalDayQA.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String nationalDayQA(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		jsonObject = activityManager.nationalDayQA(request);
		return jsonObject.toString();
	}
	
	
	/**
	 * 设置用户风险等级
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/business/appConversionUserInvtp.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String appConversionUserInvtp(HttpServletResponse response,HttpServletRequest request){
		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		UserBaseInfoDto baseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
		
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, WXConstants.SERVICE_CHANNEL_WEIXIN,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		OrderResult orderResult = tradeManager.appConversionUserInvtp(context, request);
		JSONObject jsonObject = JSONObject.fromObject(orderResult);
		
		
		String apptp = request.getParameter("apptp");
		
		if(!apptp.equals("1")){
			//日志信息
		    Context contextMsg=ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, WXConstants.SERVICE_CHANNEL_WEIXIN,"", System.currentTimeMillis(), System.currentTimeMillis(), "", "");
			MailMessage mailMessage = new MailMessage();
			String toMailPath = SpringUtil.getProperty("toMailPath");
			String[] tos = {};
			if(!StringUtil.isEmpty(toMailPath)){
				tos = toMailPath.split(",");
			}
			String ccMailPath = SpringUtil.getProperty("ccMailPath");
			String[] ccs = {};
			if(!StringUtil.isEmpty(ccMailPath)){
				ccs = ccMailPath.split(",");
			}
			mailMessage.setTo(tos);
			mailMessage.setCc(ccs);
			mailMessage.setContent("<!DOCTYPE html><html><head><meta charset='UTF-8'><meta http-equiv=content-type content='text/html;charset=utf-8'>"
					+"</head><body><table style='width: 600px;border-color: #666666;font-family: verdana,arial,sans-serif;font-size:15px;border-collapse: collapse;text-align: center;cellpadding='0' cellspacing='0'>"
					+"<tr style='background: #d3d3d3;'>"
					+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>姓名</td>"
					+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>电话</td>"
					+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>事项</td></tr>"
					+"<tr>"
					+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+baseInfoDto.getCustName()+"</td>"
					+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+baseInfoDto.getMobile()+"</td>"
					+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>申请转为专业投资者</td></tr>"
					+"</table></body></html>");
			mailMessage.setSubject("电商客户"+baseInfoDto.getCustName()+"专业投资者申请");
			mailMessage.setConfId("1");
			try {
				messageServiceClient.sendMail(contextMsg, mailMessage );
			} catch (Exception e) {
				logger.error("TradeController.appConversionUserInvtp异常",e);
			}
		}
		
		
		return jsonObject.toString();
	}
	
	/**
	 * 设置用户风险等级
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/business/clearAppcvInvp.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String clearAppcvInvp(HttpServletResponse response,HttpServletRequest request){
		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, WXConstants.SERVICE_CHANNEL_WEIXIN,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		OrderResult orderResult = tradeManager.clearAppcvInvp(context, request);
		JSONObject jsonObject = JSONObject.fromObject(orderResult);
		return jsonObject.toString();
	}
	
	/**
	 * 取消申请专业投资者
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/business/cancelAppConversionUserInvtp.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String cancelAppConversionUserInvtp(HttpServletResponse response,HttpServletRequest request){
		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		UserBaseInfoDto baseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, WXConstants.SERVICE_CHANNEL_WEIXIN,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		String custNo = "";
		if(userAccoRla != null){
			custNo = userAccoRla.getEcCustNo();
		}
		
		OrderResult orderResult = tradeManager.cancelAppConversionUserInvtp(context, cmfUserId, custNo);
		JSONObject jsonObject = JSONObject.fromObject(orderResult);
		
		//日志信息
	    Context contextMsg=ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, WXConstants.SERVICE_CHANNEL_WEIXIN,"", System.currentTimeMillis(), System.currentTimeMillis(), "", "");
		MailMessage mailMessage = new MailMessage();
		String toMailPath = SpringUtil.getProperty("toMailPath");
		String[] tos = {};
		if(!StringUtil.isEmpty(toMailPath)){
			tos = toMailPath.split(",");
		}
		String ccMailPath = SpringUtil.getProperty("ccMailPath");
		String[] ccs = {};
		if(!StringUtil.isEmpty(ccMailPath)){
			ccs = ccMailPath.split(",");
		}
		mailMessage.setTo(tos);
		mailMessage.setCc(ccs);
		mailMessage.setContent("<!DOCTYPE html><html><head><meta charset='UTF-8'><meta http-equiv=content-type content='text/html;charset=utf-8'>"
				+"</head><body><table style='width: 600px;border-color: #666666;font-family: verdana,arial,sans-serif;font-size:15px;border-collapse: collapse;text-align: center;cellpadding='0' cellspacing='0'>"
				+"<tr style='background: #d3d3d3;'>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>姓名</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>电话</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>事项</td></tr>"
				+"<tr>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+baseInfoDto.getCustName()+"</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>"+baseInfoDto.getMobile()+"</td>"
				+ "<td style='height: 40px;border: 1px solid #a5a5a5;'>取消专业投资者申请</td></tr>"
				+"</table></body></html>");
		try {
			String subject ="电商客户"+baseInfoDto.getCustName()+"取消专业投资者申请";
			mailMessage.setSubject(subject);
			mailMessage.setConfId("1");
			messageServiceClient.sendMail(contextMsg, mailMessage );
		} catch (Exception e) {
			logger.error("----cancelAppConversionUserInvtp-Email-Exception:",e);
		}
		return jsonObject.toString();
	}
	
	/**
	 * 通过知识测评，修改投资者类型为专业投资者
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/business/passTestUpdateUserInvtp.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String passTestUpdateUserInvtp(HttpServletResponse response,HttpServletRequest request,@RequestParam("invprtpScore")String invprtpScore){
		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, WXConstants.SERVICE_CHANNEL_WEIXIN,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		String custNo = "";
		if(userAccoRla != null){
			custNo = userAccoRla.getEcCustNo();
		}
		OrderResult orderResult = tradeManager.passTestUpdateUserInvtp(context, cmfUserId, custNo,invprtpScore);
		JSONObject jsonObject = JSONObject.fromObject(orderResult);
		
		return jsonObject.toString();
	}
	
	/**
	 * 7天14天理财管家-修改客户产品到期分配方式
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/business/updateDistributionType.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String updateDistributionType(HttpServletResponse response,HttpServletRequest request){
		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, WXConstants.SERVICE_CHANNEL_WEIXIN,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		JSONObject resJson = tradeManager.updateAppointRequest(context ,request);
		return resJson.toString();
	}

	/**
	 * 添加用户操作日志接口
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/business/addOpLog.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String addOpLog(HttpServletResponse response,HttpServletRequest request){
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		JSONObject jsonObject=new JSONObject();
		if(null==request.getParameter("custNo")||StringUtils.isEmpty(request.getParameter("custNo"))){
			jsonObject.put("returnCode", "");
			jsonObject.put("returnMsg", "关键参数custNo为空");
			return jsonObject.toString();
		}
		UserServiceMessage usermessage = userServiceClient.queryUserAndAccoRlaById(null, cmfUserId);
		String custNo=request.getParameter("custNo");
		String custName=usermessage.getUserBaseInfoDto().getCustName();
		String optType=request.getParameter("optType");
		String custMobile=usermessage.getUserBaseInfoDto().getMobile();
		OpLogDto oplog=new OpLogDto();
		oplog.setCustMobile(custMobile);
		oplog.setCustIp(RequestHelper.getIpAddr(request));
		oplog.setOptType(optType);//主动赎回
		oplog.setSourceType("01");//官网操作
		oplog.setCustNo(custNo);
		oplog.setCustName(custName);
		
		jsonObject= JSONObject.fromObject(oplog);
		tradeManager.addOpLog(jsonObject.toString());
		JSONObject outJson = new JSONObject();
		String returnCode = "0000";
		String returnMsg = "成功";
		/* 不需要返回任何数据 */
		outJson.put("returnCode", returnCode);
		outJson.put("returnMsg", returnMsg);
		logger.info("【TradeController】addOpLog()结束>>>jsonObject=" + outJson + ">>>timestamp=" + System.currentTimeMillis());
		return outJson.toString();
	}
	
	
	/**
	 * 更新财富宝赎回份额
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/business/updateOrderRedemptionShare.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String updateOrderRedemptionShare(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String seqId = request.getSession(true).getId();
		String busiChannel = RequestHelper.verfiyIEChannel(request);
		String cmfUserId = "";
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}
		
		logger.info("【TradeController】updateOrderRedemptionShare()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());
		
		String serialNo = request.getParameter("serialno");
		String tradeAmt = request.getParameter("tradeamt");
		String custNo = "";
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		if(userBaseInfoDto != null && userAccoRla != null){
			custNo = userAccoRla.getEcCustNo();
		}
		
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		if(StringUtils.isBlank(custNo) || StringUtils.isBlank(serialNo) || StringUtils.isBlank(tradeAmt)) {
			logger.info("关键请求参数为空");
			jsonObject.put("resultCode", "9999");
			jsonObject.put("resultMsg", "参数为空");
			return jsonObject.toString();
		}
		
		FundTradeDto fundTradeDto = new FundTradeDto();
		fundTradeDto.setSerialNo(serialNo);
		fundTradeDto.setCustNo(custNo);
		fundTradeDto.setTradeAmt(tradeAmt);
		
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		OrderResult orderResult = tradeManager.updateOrderRedemptionShare(fundTradeDto);
		
		if(orderResult != null){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
		}
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("【TradeController】updateOrderRedemptionShare()结束>>>jsonObject=" + jsonObject + ">>>timestamp=" + System.currentTimeMillis());
		
		return jsonObject.toString();
	}
	
	/**
	 * 添加合格投资者信息
	 * @param request
	 * @param reponse
	 * @return
	 */
	@RequestMapping(value = "/business/addAccreditedInvestorInfo.xhtml",method = {RequestMethod.POST,RequestMethod.GET},produces="text/html;charset=UTF-8")
	@ResponseBody
	public String addAccreditedInvestorInfo(HttpServletRequest request,HttpServletResponse reponse) {
		UserAccoRlaDto userAccoRlaDto = (UserAccoRlaDto) request.getSession().getAttribute(SessionValue.SESSION_USERACCORLA);
		if(userAccoRlaDto == null) {
			JSONObject obj = new JSONObject();
			obj.put("returnCode", "9005");
			obj.put("returnMsg", "请实名之后再进行合格投资者认定");
			return obj.toString();
		}
		UserBaseInfoDto currentUserInfo = (UserBaseInfoDto) request.getSession().getAttribute(SessionValue.SESSION_USERBASEINFO);
		CmwaQualifiedUserInfoDto param = new CmwaQualifiedUserInfoDto();
		param.setCmfuserid(userAccoRlaDto.getCmfUserId());
		param.setCustno(userAccoRlaDto.getEcCustNo());
		param.setUpdatedUser("WXSystem");
		param.setCreatedUser("WXSystem");
		return tradeManager.addAccreditedInvestorInfo(param,currentUserInfo).toString();
	}

	/**
	 * 修改用户提交的文件
	 * @param key 文件存放在s3的值
	 * @param fileName 文件名
	 * @param custno custno
	 * @param operatorType 操作类型
	 * @Param fileType 文件类型
	 * @return
	 */
	@RequestMapping(value="/business/updateSubmittedInfo.xhtml",method = {RequestMethod.POST,RequestMethod.GET},produces="text/html;charset=UTF-8")
	@ResponseBody
	public String updateSubmittedInfo(@RequestParam("key") String key,
										  @RequestParam("operatorType") String operatorType,
										  @RequestParam(value = "fileName",required = false) String fileName,
										  @RequestParam(value = "fileType",required = false) String fileType,
										  HttpServletRequest request) {
		String currentUser = "WxSystem";
		UserAccoRlaDto userAccoRlaDto = (UserAccoRlaDto) request.getSession().getAttribute(SessionValue.SESSION_USERACCORLA);
		if(userAccoRlaDto == null){
			JSONObject obj = new JSONObject();
			obj.put("returnCode","9005");
			obj.put("returnMsg","请实名之后再进行合格投资者认定");
			return obj.toString();
		}
		return tradeManager.updateSubmittedInfo(key,userAccoRlaDto.getEcCustNo(),operatorType,fileName,fileType,currentUser).toString();
	}

	
	/**
     * 删除文件上传记录
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/business/deleteFileUploadRecord.xhtml",method = {RequestMethod.POST,RequestMethod.GET},produces="text/html;charset=UTF-8")
    @ResponseBody
    public String deleteFileUploadRecord(HttpServletRequest request,HttpServletResponse response) {
            String recordId  = request.getParameter("recordId");
            return tradeManager.deleteFileUploadRecord(recordId).toString();
    }
    
    /**
     * 修改合格投资者申请状态
     * @param updatedStatus
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/business/modifyAccreditedInvestorInfoStatus.xhtml",method = {RequestMethod.POST,RequestMethod.GET},produces="text/html;charset=UTF-8")
    @ResponseBody
    public String modifyAccreditedInvestorInfoStatus(@RequestParam("updateStatus") String updatedStatus,HttpServletRequest request,HttpServletResponse response) {
    	UserAccoRlaDto userAccoRlaDto = (UserAccoRlaDto) request.getSession().getAttribute(SessionValue.SESSION_USERACCORLA);
		if(userAccoRlaDto == null){
			JSONObject obj = new JSONObject();
			obj.put("returnCode","9005");
			obj.put("returnMsg","请实名之后再进行合格投资者认定");
			return obj.toString();
		}
		UserBaseInfoDto currentUserInfo = (UserBaseInfoDto) request.getSession().getAttribute(SessionValue.SESSION_USERBASEINFO);
		return tradeManager.updatedAccreditedInvestorRequestStatus(userAccoRlaDto.getEcCustNo(), updatedStatus,currentUserInfo).toString();
    }
	

    @RequestMapping(value="/business/sendFileTemplateMail.xhtml",method = {RequestMethod.POST,RequestMethod.GET},produces="text/html;charset=UTF-8")
    @ResponseBody
    public String sendFileTemplateMail(@RequestParam("mail") String mail,HttpServletRequest request) throws Exception {
    	MailMessage message = new MailMessage();
    	String filePath = "WeixinWeb" + File.separator + "WeixinWeb_Images" + File.separator + "images" + File.separator + "qualified" + File.separator + "incomeCertificate.doc";
    	String path = request.getSession().getServletContext().getRealPath("/").concat(filePath);
    	byte[] byteArray = IOUtils.toByteArray(new FileInputStream(new File(path)));
    	message.setTo(mail);
    	message.setSubject("招商财富合格投资者收入证明模板");
    	message.setContent("尊敬的投资者，请按照模板填写相应内容并加盖公章，谢谢您的支持。");
    	message.setConfId("");
    	message.setFileStream(byteArray);
    	MsgServiceMessageDto sendMail = messageServiceClient.sendMailWithFile(message, "个人收入模板.doc");
    	logger.info("发送邮件结果为："+sendMail+",发送邮箱为："+mail+",操作用户cmfuserid为："+((UserBaseInfoDto) request.getSession().getAttribute(SessionValue.SESSION_USERBASEINFO)).getCmfUserId());
    	JSONObject obj = new JSONObject();
    	obj.put("returnCode", sendMail.getResultCode());
    	obj.put("returnMsg", sendMail.getResultMsg());
    	return obj.toString();
    }
    
    /**
     * 保存用户风险揭示函勾选记录信息
     * @param request
     * @param response
     * @param data
     * @return
     */
    @RequestMapping(value="/business/saveUserTermsInfo.xhtml",method = {RequestMethod.POST,RequestMethod.GET},produces="text/html;charset=UTF-8")
    @ResponseBody
    public String saveUserTermsInfo(HttpServletRequest request,HttpServletResponse response){
		String cmfUserId = "";
		String busiChannel=RequestHelper.verfiyIEChannel(request);
		String sessionId = request.getSession().getId();
		UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userBaseInfoDto != null){
			cmfUserId = userBaseInfoDto.getCmfUserId();
		}
		//日志封装类
		Context context=ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		String fundId = request.getParameter("fundId");
		String period = request.getParameter("period");
		String ids = request.getParameter("ids");
		String type = request.getParameter("type");
		if(StringUtils.isEmpty(period)){
			period = "1";
		}
		if(StringUtils.isEmpty(fundId) || StringUtils.isEmpty(ids)){
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("returnCode",WXConstants.COMMON_ERROR_PARAMISNULLCODE);
			jsonObject.put("returnMsg",WXConstants.COMMON_ERROR_PARAMISNULLMSG);
			return jsonObject.toString();
		}	
		return tradeManager.saveUserTermsInfo(context,cmfUserId,fundId,period,ids,type).toString(); 
    }
    
    /**
     * 
     * @param request
     * @param response
     * @return
     */
   @RequestMapping(value="/business/revokeRedeemOrder.xhtml",method = {RequestMethod.POST,RequestMethod.GET},produces="text/html;charset=UTF-8")
   @ResponseBody
   public String revokeRedeemOrder(HttpServletRequest request,HttpServletResponse response){
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		// 日志封装类
		Context context = ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, "", cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		String serialNo = request.getParameter("serialNo");
		String custno = request.getParameter("custno");
		JSONObject resultJson = new JSONObject();
		if(StringUtils.isEmpty(serialNo) || StringUtils.isEmpty(custno)){
			resultJson.put("returnCode",WXConstants.COMMON_ERROR_PARAMISNULLCODE);
			resultJson.put("returnMsg",WXConstants.COMMON_ERROR_PARAMISNULLMSG);
			return resultJson.toString();
		}
	    return tradeManager.revokeRedeemOrder(context, serialNo, custno).toString();
   }
}
