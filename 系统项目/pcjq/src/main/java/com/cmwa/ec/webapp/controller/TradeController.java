package com.cmwa.ec.webapp.controller;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

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
import com.cmwa.ec.trade.facade.dto.bank.BocSignParaDto;
import com.cmwa.ec.trade.facade.dto.fund.FundTradeDto;
import com.cmwa.ec.trade.facade.dto.fund.SignEContractDto;
import com.cmwa.ec.trade.facade.dto.log.OpLogDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.webapp.client.MessageServiceClient;
import com.cmwa.ec.webapp.client.QueryServiceClient;
import com.cmwa.ec.webapp.client.UserServiceClient;
import com.cmwa.ec.webapp.manager.MessageManager;
import com.cmwa.ec.webapp.manager.TradeManager;
import com.cmwa.ec.webapp.manager.UserManager;
import com.cmwa.ec.webapp.util.ContextUtils;
import com.cmwa.ec.webapp.util.DateUtils;
import com.cmwa.ec.webapp.util.ECConstants;
import com.cmwa.ec.webapp.util.GetIPUtils;
import com.cmwa.ec.webapp.util.MD5;
import com.cmwa.ec.webapp.util.RequestHelper;
import com.cmwa.ec.webapp.util.SessionValue;
import com.cmwa.ec.webapp.util.StringHelper;

@Controller("TradeController")
@RequestMapping(value = "/AppService")
public class TradeController {

	private static Logger logger = Logger.getLogger(TradeController.class.getName());
	
	@Autowired
	private TradeManager tradeManager;
	
	@Autowired
	private MessageManager messageManager;
	
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private MessageServiceClient messageServiceClient;
	
	@Autowired
	private UserServiceClient userServiceClient;
	
	@Autowired
	private QueryServiceClient queryServiceClient;
	
	/**
	 * 下预约单（预下单）
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value="/business/fundAppoint.xhtml" , produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String fundAppoint(HttpServletResponse response,HttpServletRequest request)throws Exception{

		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		logger.info("TradeController类【fundAppoint】开始>>>cmfUserId:"+cmfUserId+">>>timestamp:"+System.currentTimeMillis());
		
		//日志信息
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		String custNo = "";
		if(userAccoRla != null){
			custNo = userAccoRla.getEcCustNo();
		}
		String payType = request.getParameter("payType");
		
		String fundState = request.getParameter("fundState");
//		String serType = request.getParameter("serType");
		
		String mobile = request.getParameter("mobile");
		
		String fee = request.getParameter("fee");
		String commro = request.getParameter("commro");
		String renew = request.getParameter("renew");

		FundTradeDto fundTradeDto = new FundTradeDto();
		
		String apkind = "";
//		if(serType != null && !serType.equals("")){//预下单   apkind="APO"
//			apkind = serType;
//		}else if(fundState != null && !fundState.equals("")){
		apkind = FundTradeDto.getApkind(fundState.charAt(0));
//		}
//		if(serType != null && serType.equals("A2T")){//预约类型订单选择支付方式和银行卡
//			apkind = "A2T";
//		}else if(serType != null && serType.equals("UPT")){//预约类型订单修改订单金额
//			apkind = "UPT";
//		    fundTradeDto.setSerialNo(serialno);// 当前情况   只有预约类型的订单修改订单金额才会将serialno传给服务端，而预约的不会
//		}else{
//			apkind = FundTradeDto.getApkind(fundState.charAt(0));
//		}
		logger.info("TradeController类【fundAppoint】转换之后的：apkind="+apkind);
		
		String fundId = request.getParameter("fundId");
		
		String tradeAmt = request.getParameter("money");
		tradeAmt = URLDecoder.decode(tradeAmt, "utf-8");
		String channelId = ECConstants.TRADE_CHANEL_01;
		
	    fundTradeDto.setCustNo(custNo); 
	    fundTradeDto.setApkind(apkind); 
	    fundTradeDto.setFundId(fundId);
	    fundTradeDto.setTradeAmt(tradeAmt);
	    fundTradeDto.setPayType(payType); 
	    fundTradeDto.setChannelId(channelId);
	    fundTradeDto.setMobileNo(mobile);
	    fundTradeDto.setFee(fee);
	    fundTradeDto.setCommro(commro);
	    fundTradeDto.setRenew(renew);
	    
	    
		logger.info("TradeController类【fundAppoint】中>>>fundTradeDto=" + fundTradeDto);
	    
	    String resultCode = "";
	    String resultMsg = "";
	    
	    FundTradeDto resultFundTradeDto = new FundTradeDto();
	    OrderResult orderResult = tradeManager.fundAppoint(context, fundTradeDto);
	    if(orderResult != null){
	    	resultCode = orderResult.getResultCode();
	    	resultMsg = orderResult.getResultMsg();
	    	String errCode = orderResult.getErrCode();
	    	String errMsg = orderResult.getErrMsg();
	    	resultFundTradeDto = (FundTradeDto) orderResult.getData();// 返回的结果DTO
	    	
	    	if(resultFundTradeDto != null){
	    		resultFundTradeDto.setCustNo("");// 将返回到页面的custno置空
	    	}
	    }
	    JSONObject returnJsonObject = new JSONObject();
	    returnJsonObject.put("resultCode", resultCode);
	    returnJsonObject.put("resultMsg", resultMsg);
	    returnJsonObject.put("resultFundTradeDto", resultFundTradeDto);
	    
	    logger.info("TradeController类【fundAppoint】结束>>>returnJsonObject=【"+returnJsonObject.toString()+"】");
	    
	    return returnJsonObject.toString();
	}
	
	/**
	 * 下交易单
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value="/business/fundTrade.xhtml" , produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String fundTrade(HttpServletResponse response,HttpServletRequest request)throws Exception{

		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		logger.info("TradeController类【fundTrade】开始>>>cmfUserId:"+cmfUserId+">>>timestamp:"+System.currentTimeMillis());
		
		//日志信息
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		String custNo = "";
		if(userAccoRla != null){
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
		String feeMode  = request.getParameter("feeMode");
		String payType = request.getParameter("payType");
		String channelId = ECConstants.TRADE_CHANEL_01;
		
		String contractVer = request.getParameter("contractVer");
		
		String rvrfcode = request.getParameter("verifyCode");
		String sessionID = request.getParameter("sessionID");
		String renew = request.getParameter("renew");
		
		String apkind = "";
		
		/**
		 * 当页面传递过来的serType有效，且：
		 * serType == "A2T",apkind = "A2T"
		 * serType == "UPT",apkind = "UPT"
		 * 
		 * 否则
		 * apkind = FundTradeDto.getApkind(apkind.charAt(0));
		 */
		if(serType != null && serType.equals("A2T")){//预约类型订单选择支付方式和银行卡
			apkind = "A2T";
		}else if(serType != null && serType.equals("UPT")){//预约类型订单修改订单金额
			apkind = "UPT";
		}else if(serType != null && serType.equals("CNL")){
			apkind = "CNL";
		}else{
			apkind = FundTradeDto.getApkind(fundState.charAt(0));
		}
		
		JSONObject returnJsonObject = new JSONObject();
		//关键参数 非空
		if(StringUtils.isEmpty(tradeAmt) || !StringUtils.isNumeric(tradeAmt) || StringUtils.isEmpty(tradeAcco) || StringUtils.isEmpty(contractVer) || StringUtils.isEmpty(apkind)){
			returnJsonObject.put("resultCode", ECConstants.RETURN_CODE_9008);
			returnJsonObject.put("resultMsg", ECConstants.RETURN_MSG_9008);
			logger.info("TradeController类【fundTrade】中参数不正确》》》入参：sessionID"+sessionID+"》》》cmfUserId"+cmfUserId+"》》》mobile"+mobile+"》》》tradeAmt"+tradeAmt+
					"》》》tradeAcco:"+tradeAcco+"》》》contractVer："+contractVer+"》apkind："+apkind+
					"》》》返回信息：returnJsonObject="+returnJsonObject);
			return returnJsonObject.toString();
		}
		
		String reg = "\\d+(\\.\\d+)?";
		//关键参数 非数字格式
		if(!commro.matches(reg) || !fee.matches(reg)){
			returnJsonObject.put("resultCode", ECConstants.RETURN_CODE_9008);
			returnJsonObject.put("resultMsg", ECConstants.RETURN_MSG_9008);
			logger.info("TradeController类【fundTrade】中参数不正确》》》入参：sessionID"+sessionID+"》》》cmfUserId"+cmfUserId+"》》》mobile"+mobile+"》》》commro："+commro+
					"》》》fee："+fee+"》》》返回信息：returnJsonObject="+returnJsonObject);
			return returnJsonObject.toString();
		}
		
		MD5 md5 = new MD5();
		if(tpassword != null && !tpassword.equals("")){
			tpassword = md5.getMD5ofStr(tpassword);
		}
		
		/***************************** 线上支付   需要验证短信验证码 S ****************************************************/
		if(payType != null && payType.equals("0")){
			String mobileByMsg = (String) request.getSession(true).getAttribute("mobileByMsg");
			if(mobileByMsg.equals(payMobile)){// 验证短信验证码
				if(rvrfcode != null && !rvrfcode.equals("")){
					rvrfcode = rvrfcode.trim();
				}
				MsgServiceMessageDto msgServicedto=messageManager.checkVrfCode(context,sessionID, mobile, rvrfcode);
				if(msgServicedto!=null){
					if(msgServicedto.getReturnCode() == null || !msgServicedto.getReturnCode().equals("0000")){
//						returnJsonObject.put("resultCode", msgServicedto.getReturnCode());
						returnJsonObject.put("resultCode", "6001");
						returnJsonObject.put("resultMsg", msgServicedto.getReturnMsg());
						logger.info("TradeController类【fundTrade】中验证短信验证码失败》》》入参：sessionID"+sessionID+"》》》mobile"+mobile+"》》》rvrfcode"+rvrfcode+"》》》返回信息：returnJsonObject="+returnJsonObject);
						return returnJsonObject.toString();
					}
				}else{
					returnJsonObject.put("resultCode", "9999");
					logger.info("TradeController类【fundTrade】中验证短信验证码失败》》》入参：sessionID"+sessionID+"》》》mobile"+mobile+"》》》rvrfcode"+rvrfcode+"》》》返回信息：returnJsonObject="+returnJsonObject);
					return returnJsonObject.toString();
				}
				
			}else{
				returnJsonObject.put("resultCode", "0099");
				returnJsonObject.put("resultMsg", "请重新获取并验证手机短信验证码");
				logger.info("TradeController类【fundTrade】中验证短信验证码失败：没有获取到验证手机号码");
				return returnJsonObject.toString();
			}
		}
		/***************************** 线上支付   需要验证短信验证码 E ****************************************************/
		
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
	    fundTradeDto.setFeeMode(feeMode);
		logger.info("TradeController类【fundTrade】中传给服务端的参数>>>fundTradeDto="+fundTradeDto.toString());
		
		String appDt = DateUtils.formatDate(new Date(),DateUtils.yyyyMMdd);
		
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
		
		OrderResult orderResult = tradeManager.fundTrade(context, fundTradeDto,elDto);
		if(orderResult != null){
			resultCode = orderResult.getResultCode();
			resultMsg = orderResult.getResultMsg();
			logger.info("TradeController类【fundTrade】中调用后台服务接口成功，返回数据为：resultCode="+resultCode+";resultMsg="+resultMsg);
			resultFundTradeDto = (FundTradeDto) orderResult.getData();
			bocSignParaDto = (BocSignParaDto) orderResult.getOtherData();
			
			if(resultFundTradeDto != null){
				resultFundTradeDto.setCustNo("");// 将返回到页面的custno置空
			}
			
		}
		
		returnJsonObject.put("resultCode", resultCode);
		returnJsonObject.put("resultMsg", resultMsg);
		returnJsonObject.put("resultFundTradeDto", resultFundTradeDto);
		
		if(bocSignParaDto != null){
			returnJsonObject.put("bocSignParaDto", bocSignParaDto);
		}
		
		// 购买成功，则删除session中已保存的【验证支付密码标识】的状态
		if(resultCode != null && resultCode.equals("0000")){
			request.getSession(true).removeAttribute("checkedTpassword");
			logger.info("TradeController类【fundTrade】中选择线上付款验证支付密码成功，删除session");
		}
		
		logger.info("TradeController类【fundTrade】结束>>>returnJsonObject="+returnJsonObject.toString());
		
		return returnJsonObject.toString();
	
	}
	
	/**
	 * 用户信息鉴权，开户
	 */
	@RequestMapping(value="/business/openAccount.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String openAccount(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject returnJsonObject = null;
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String sessionId = RequestHelper.getSeqId(request);
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, "", cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		returnJsonObject = tradeManager.openAccount(context, request);
		Object returnCode = returnJsonObject.get("returnCode");
		if (returnCode != null && ECConstants.COMMON_SUCCESS.equals(returnCode.toString())) {
			// 鉴权成功后，删除获取验证码和验证验证码时往session中存放的mobile
			request.getSession(true).removeAttribute(SessionValue.SESSION_AUTHMOBILE);
			request.getSession(true).removeAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE);
		}
		return returnJsonObject.toString();
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
	@ResponseBody
	public String cancelAppointRequest(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);

		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, "", cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		JSONObject jsonObject = tradeManager.cancelAppointRequest(context, request);

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
		Context context = ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, "", cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		UserBaseInfoDto userBaseInfo = RequestHelper.getSessionUserBaseInfo(request);
		String custNo = "";
		String tradeAcco = request.getParameter("tradeAcco");
		String serialNo = request.getParameter("serialNo");
		String fundid = request.getParameter("fundid");
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
		fundTradeDto.setTradeAmt(money);
		if(null != userBaseInfo){
			fundTradeDto.setMobileNo(userBaseInfo.getMobile());
		}
		fundTradeDto.setChannelId("04");
		fundTradeDto.setCustNo(custNo);
		JSONObject jsonObject = tradeManager.redemptionOrder(context, request,fundTradeDto);
		return jsonObject.toString();
	}
	/**
	 * 解除绑定银行卡
	 */
	@RequestMapping(value="/business/canalBindBankCard.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String canalBindBankCard(HttpServletResponse response,HttpServletRequest request)throws Exception{
		String tradeAcc = request.getParameter("tradeAcco");
		JSONObject json = null;
		UserBaseInfoDto userInfo = RequestHelper.getSessionUserBaseInfo(request);
		String sessionId = RequestHelper.getSeqId(request);
		if(userInfo == null){
			json = new JSONObject();
			json.put("returnCode", ECConstants.RETURN_CODE_8000);
			json.put("returnMsg", ECConstants.RETURN_MSG_8000);
			return json.toString();
		}
		if(tradeAcc == null || "".equals(tradeAcc)){
			json = new JSONObject();
			json.put("returnCode", ECConstants.RETURN_CODE_9000);
			json.put("returnMsg", ECConstants.RETURN_MSG_9000);
			return json.toString();
		}
		String cmfUserId = userInfo.getCmfUserId();
		//日志封装类
		Context context=ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		json = tradeManager.cancelBindBankCard(context, request, tradeAcc);
		return json.toString();
	}
	/**
	 * 信批已读保存
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author luos
	 */
	@RequestMapping(value="/business/addReportReadRecord.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String addReportReadRecord(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String fundId = request.getParameter("fundId");
		String reportId = request.getParameter("reportId");
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		JSONObject jsonObject = new JSONObject();
		String returnCode = "0000";
		String returnMsg = "成功";
		//日志封装类
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		OrderResult orderResult = tradeManager.addReportReadRecord(context, cmfUserId, fundId, reportId);
		/* 不需要返回任何数据 */
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("【TradeController】addReportReadRecord()结束>>>jsonObject=" + jsonObject + ">>>timestamp=" + System.currentTimeMillis());
		return jsonObject.toString();
	}
	
	/**
	 * 下排队单
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value="/business/fundTradeLineUp.xhtml" , produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String fundTradeLineUp(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		
		String type = "0100";
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = tradeManager.fundTradeLineUp(context, request);
		
		return jsonObject.toString();
	}
	/**
	 * 添加银行卡
	 */
	@RequestMapping(value="/business/addBank.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String addBank(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		JSONObject returnJsonObject = null;
		
		Object obj2 = request.getSession(true).getAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE);
		UserBaseInfoDto userbaseinfo = RequestHelper.getSessionUserBaseInfo(request);
		Object obj4 = request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		if(userbaseinfo == null || obj4==null){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_8000);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_8000);
			return returnJsonObject.toString();
		}
		if(obj2==null){
			returnJsonObject = new JSONObject();
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_9003);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_9003);
			return returnJsonObject.toString();
		}
		String sessionId = request.getSession().getId();
		//日志封装类
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,userbaseinfo.getCmfUserId(), System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		returnJsonObject = tradeManager.addBankNumber(context, request);
		Object returnCode = returnJsonObject.get("returnCode");
		if(returnCode!=null && ECConstants.COMMON_SUCCESS.equals(returnCode.toString())){
			//鉴权成功后，删除获取验证码和验证验证码时往session中存放的mobile,保存鉴权成功的标志
			request.getSession(true).removeAttribute(SessionValue.SESSION_AUTHMOBILE);
			request.getSession(true).removeAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE);
		}
		return returnJsonObject.toString();
	}
	/**
	 * 修改订单金额
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author luos
	 */
	@RequestMapping(value="/business/modifyAppointRequest.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String modifyAppointRequest(HttpServletResponse response,HttpServletRequest request)throws Exception{
		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String serialno = request.getParameter("serialno");
		String tradeAcco = request.getParameter("tradeAcco");
		String tradeAmt = request.getParameter("tradeAmt");
		String tpassword = request.getParameter("tPassWord");
		String randomCode = request.getParameter("randomCode");
		String renew = request.getParameter("renew");
		String fee = request.getParameter("fee");
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		
		//日志封装类
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
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
				jsonObject = userManager.manageTpasswordNew(context, cmfUserId, tpassword, "", manageType, "90", ECConstants.TRADE_CHANEL_03);
				if (!("USR-1I00").equals(jsonObject.get("returnCode"))) {
					// 密码验证未通过
					jsonObject.put("returnCode", jsonObject.get("resultCode"));
					jsonObject.put("returnMsg", jsonObject.get("resultMsg"));
					return jsonObject.toString();
				}
			}
		/****************** 验证支付密码 E *****************************************************************/
		
		String custno = "";
		UserAccoRlaDto userAccoRlaDto = null;
		Object obj = RequestHelper.getSessionAttribute(request, SessionValue.SESSION_USERACCORLA);
		if(obj != null){
			userAccoRlaDto = (UserAccoRlaDto) obj;
		}
		if(userAccoRlaDto != null){
			custno = userAccoRlaDto.getEcCustNo();
		}
		
		FundTradeDto fundTradeDto = new FundTradeDto();
		fundTradeDto.setSerialNo(serialno);
		fundTradeDto.setTradeAcco(tradeAcco);
		fundTradeDto.setTradeAmt(tradeAmt);
		fundTradeDto.setFee(fee);
		fundTradeDto.setRenew(renew);
		fundTradeDto.setCustNo(custno);
		OrderResult orderResult = tradeManager.modifyAppointRequest(context, fundTradeDto);
		if(orderResult != null){
			returnCode = orderResult.getResultCode();
			returnMsg = orderResult.getResultMsg();
		}
		/* 修改成功，修改续投方式则发送短信*/
		if (null!=returnCode && "0000".equals(returnCode) && null!=renew && !"".equals(renew)) {
		    //日志信息
		    Context contexts = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
			String renewDesc="";
			if ("Y".equals(renew)) {
				renewDesc="到期自动续投";
			}else{
				renewDesc="到期自动赎回";
			}
		    MsgParameterDto msgParameter = new MsgParameterDto();
		    String mobile= request.getParameter("mobile");
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
		return jsonObject.toString();
	}
	
	/**
	 * 风险测评  评级
	 * @param response
	 * @param request
	 * @return String
	 * @author maj
	 */
	@RequestMapping(value="/business/setUserRiskLevel.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String setUserRiskLevel(HttpServletResponse response,HttpServletRequest request){
		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		
		Context context=ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject = tradeManager.setUserRiskLevel(context, request);
		
		try{
			String nationality = request.getParameter("nationality");
			String province = request.getParameter("province");
			String city = request.getParameter("city");
			String detailAddress = request.getParameter("detailAddress");
			String profId = request.getParameter("profId");
			String dateOfBirth = request.getParameter("dateOfBirth");
			String taxResidentType = request.getParameter("taxResidentType");
			String otherVocation = request.getParameter("otherVocation");//客户输入其他职业
			if(dateOfBirth != null){
				dateOfBirth = dateOfBirth.replaceAll("-", "");
			}
			//cmfUserId非空 && nationality、dateOfBirth、taxResidentType、profId只要有一个不为null则调用
			if (StringHelper.isNotBlank(cmfUserId) && 
					(StringHelper.isNotBlank(nationality) || StringHelper.isNotBlank(dateOfBirth)
							|| StringHelper.isNotBlank(taxResidentType) 
							|| StringHelper.isNotBlank(profId)) && (!ECConstants.VOCCODE_15.equals(profId) || StringHelper.isNotBlank(otherVocation))) {
				logger.info("....风险测评-评级调用更新用户信息....");
				userManager.updateCmfUserBaseInfo(context, cmfUserId, nationality, province, city, 
						detailAddress, profId, dateOfBirth, taxResidentType, otherVocation);
			}
		}catch (Exception e) {
			logger.error("----TradeController-setUserRiskLevel-saveUserAddress-Except:",e);
		}
		return jsonObject.toString();
	}
	
	/**
	 * 银行支付签约接口
	 * @param response
	 * @param request
	 * @return String
	 * @author maj
	 */
	@RequestMapping(value="/business/contractSign.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String contractSign(HttpServletResponse response,HttpServletRequest request){
		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		
		Context context=ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject = tradeManager.contractSign(context, request);
		
		return jsonObject.toString();
	}
	
	/**
	 * 银行指令状态查询（主要用于查询工行签约结果，其他场景依情况调用）
	 * @param response
	 * @param request
	 * @return String
	 * @author maj
	 */
	@RequestMapping(value="/business/queryCommandByBankAccoNo.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String queryCommandByBankAccoNo(HttpServletResponse response,HttpServletRequest request){
		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		
		Context context=ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject = tradeManager.queryCommandByBankAccoNo(context, request);
		
		return jsonObject.toString();
	}
	/**
	 * 银行指令状态查询（主要用于查询中行支付结果，其他场景依情况调用）
	 * @param response
	 * @param request
	 * @return String
	 * @author luos
	 */
	@RequestMapping(value="/business/queryBocStatusByBankAccoNo.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String queryBocStatusByBankAccoNo(HttpServletResponse response,HttpServletRequest request){
		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		
		Context context=ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		JSONObject jsonObject = new JSONObject();
		jsonObject = tradeManager.queryCommandByBankAccoNo(context, request);
		
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
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		OrderResult orderResult = tradeManager.appConversionUserInvtp(context, request);
		JSONObject jsonObject = JSONObject.fromObject(orderResult);
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat format4 = new SimpleDateFormat("yyyy年MM月dd日");
		String evalValiDate= "";
		String evalDispDateTime = "";
	    evalDispDateTime = format4.format(calendar.getTime());
	    calendar.add(Calendar.YEAR, 1);
		evalValiDate = format4.format(calendar.getTime());
		jsonObject.put("evalValiDate", evalValiDate);
		jsonObject.put("evalDispDateTime", evalDispDateTime);
		
		UserBaseInfoDto baseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
		String apptp = request.getParameter("apptp");
		
		if(!apptp.equals("1")){
			//日志信息
		    Context contextMsg=ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP,"", System.currentTimeMillis(), System.currentTimeMillis(), "", "");
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
			//String[] tos ={"chengt@cmwachina.com","tianmh@cmwachina.com","tongj@cmwachina.com"};
			//String[] tos ={"cmwa@cmwachina.com"};
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
				e.printStackTrace();
			}
		}
		logger.info("申请转换专业投资者结果:" + jsonObject);
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
		JSONObject jsonObject = new JSONObject();
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		JSONObject userBaseInfo =  userManager.queryUserinfo(request);
		if(null != userBaseInfo && userBaseInfo.containsKey("invprtp")){
			String invprtp = (String)userBaseInfo.get("invprtp");
			Integer userInvprtpScore = 0;
			if(userBaseInfo.containsKey("invprtpScore")){
				userInvprtpScore = new Integer(String.valueOf(userBaseInfo.get("invprtpScore")));
			}
			//专业投资者 且 分数大于等于 60
			if("0".equals(invprtp) && userInvprtpScore >= 60 ){
				OrderResult result = new OrderResult("9999","已是专业投资者");
				result.setErrCode("9999");
				result.setErrMsg("已是专业投资者");
				result.setResultCode("9999");
				result.setResultMsg("已是专业投资者");
				jsonObject = JSONObject.fromObject(result);
				return jsonObject.toString();
			}
		}
		
		String custNo = "";
		if(userAccoRla != null){
			custNo = userAccoRla.getEcCustNo();
		}
		OrderResult orderResult = tradeManager.passTestUpdateUserInvtp(context, cmfUserId, custNo,invprtpScore);
		jsonObject = JSONObject.fromObject(orderResult);
		
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
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		String custNo = "";
		if(userAccoRla != null){
			custNo = userAccoRla.getEcCustNo();
		}
		OrderResult orderResult = tradeManager.cancelAppConversionUserInvtp(context, cmfUserId, custNo);
		JSONObject jsonObject = JSONObject.fromObject(orderResult);
		

		
		//日志信息
	    Context contextMsg=ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP,"", System.currentTimeMillis(), System.currentTimeMillis(), "", "");
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
		//String[] tos ={"chengt@cmwachina.com","tianmh@cmwachina.com","tongj@cmwachina.com"};
		//String[] tos ={"cmwa@cmwachina.com"};
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
		mailMessage.setSubject("电商客户"+baseInfoDto.getCustName()+"取消专业投资者申请");
		mailMessage.setConfId("1");
		try {
			messageServiceClient.sendMail(contextMsg, mailMessage );
		} catch (Exception e) {
			e.printStackTrace();
		}
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
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		JSONObject resJson = tradeManager.updateAppointRequest(context ,request);
		return resJson.toString();
	}
	
	/**
	 * 添加用户操作日志接口
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/business/addOpLog.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String addOpLog(HttpServletResponse response,HttpServletRequest request){
		String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		JSONObject jsonObject=new JSONObject();
		//日志封装类
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		if(null==request.getParameter("custNo")||StringUtils.isEmpty(request.getParameter("custNo"))){
			jsonObject.put("returnCode", "");
			jsonObject.put("returnMsg", "关键参数custNo为空");
			return jsonObject.toString();
		}
		UserServiceMessage usermessage = userServiceClient.queryUserAndAccoRlaById(context, cmfUserId);
		String custNo=request.getParameter("custNo");
		String custName=usermessage.getUserBaseInfoDto().getCustName();
		String optType=request.getParameter("optType");
		String custMobile=usermessage.getUserBaseInfoDto().getMobile();
		OpLogDto oplog=new OpLogDto();
		oplog.setCustMobile(custMobile);
		oplog.setCustIp(GetIPUtils.getIpAddr(request));
		oplog.setOptType(optType);//主动赎回
		oplog.setSourceType("01");//官网操作
		oplog.setCustNo(custNo);
		oplog.setCustName(custName);
		logger.info("[TradeController]appOpLog() 接受参数："+oplog);
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
	
	@RequestMapping(value="/business/updateOrderRedemptionShare.xhtml", produces="text/html;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	public String updateOrderRedemptionShare(HttpServletResponse response,HttpServletRequest request){
		logger.info("【TradeController】updateOrderRedemptionShare()开始》》》用户修改订单赎回份额");
		JSONObject jsonObject = new JSONObject();
		String custNo = RequestHelper.getSessionCustNo(request);
		String serialNo = request.getParameter("serialNo");
		String tradeAmt = request.getParameter("tradeAmt");
		logger.info("请求参数 custNo=" + custNo + ",serialNo=" + serialNo + ",tradeAmt=" + tradeAmt);
		if(StringUtils.isBlank(custNo) || StringUtils.isBlank(serialNo) || StringUtils.isBlank(tradeAmt)) {
			logger.info("关键请求参数为空");
			jsonObject.put("resultCode", "9999");
			jsonObject.put("resultMsg", "参数为空");
			return jsonObject.toString();
		}
		FundTradeDto fundTradeDto = new FundTradeDto();
		fundTradeDto.setCustNo(custNo);
		fundTradeDto.setSerialNo(serialNo);
		fundTradeDto.setTradeAmt(tradeAmt);
		OrderResult orderResult = tradeManager.updateOrderRedemptionShare(fundTradeDto);
		jsonObject.put("resultCode", orderResult.getResultCode());
		jsonObject.put("resultMsg", orderResult.getResultMsg());
		logger.info("【TradeController】updateOrderRedemptionShare()结束》》》结果：" + jsonObject);
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
			obj.put("returnCode", ECConstants.RETURN_CODE_9005);
			obj.put("returnMsg", "请实名之后再进行合格投资者认定");
			return obj.toString();
		}
		UserBaseInfoDto currentUserInfo = (UserBaseInfoDto) request.getSession().getAttribute(SessionValue.SESSION_USERBASEINFO);
		CmwaQualifiedUserInfoDto param = new CmwaQualifiedUserInfoDto();
		param.setCmfuserid(userAccoRlaDto.getCmfUserId());
		param.setCustno(userAccoRlaDto.getEcCustNo());
		param.setUpdatedUser("EcSystem");
		param.setCreatedUser("EcSystem");
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
		String currentUser = "EcSystem";
		UserAccoRlaDto userAccoRlaDto = (UserAccoRlaDto) request.getSession().getAttribute(SessionValue.SESSION_USERACCORLA);
		if(userAccoRlaDto == null){
			JSONObject obj = new JSONObject();
			obj.put("returnCode",ECConstants.RETURN_CODE_9005);
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
    	UserBaseInfoDto currentUserInfo = (UserBaseInfoDto) request.getSession().getAttribute(SessionValue.SESSION_USERBASEINFO);
		if(userAccoRlaDto == null){
			JSONObject obj = new JSONObject();
			obj.put("returnCode",ECConstants.RETURN_CODE_9005);
			obj.put("returnMsg","请实名之后再进行合格投资者认定");
			return obj.toString();
		}
		return tradeManager.updatedAccreditedInvestorRequestStatus(userAccoRlaDto.getEcCustNo(), updatedStatus,currentUserInfo).toString();
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
    	String seqId = request.getSession(true).getId();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		//日志封装类
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		String fundId = request.getParameter("fundId");
		String period = request.getParameter("period");
		String ids = request.getParameter("ids");
		String type = request.getParameter("type");
		if(StringUtils.isEmpty(period)){
			period = "1";
		}
		if(StringUtils.isEmpty(fundId) || StringUtils.isEmpty(ids)){
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("returnCode",ECConstants.RETURN_CODE_9000);
			jsonObject.put("returnMsg",ECConstants.RETURN_MSG_9000);
			return jsonObject.toString();
		}	
		return tradeManager.saveUserTermsInfo(context,cmfUserId,fundId,period,ids,type).toString(); 
    }
    
    @RequestMapping(value="/business/updateOrderApplyst.xhtml", produces="text/html;charset=UTF-8", method = {RequestMethod.POST})
	@ResponseBody
	public String updateOrderApplyst(HttpServletResponse response,HttpServletRequest request){
		logger.info("【TradeController】updateOrderApplyst()开始》》》用户修改订单申请状态");
		JSONObject jsonObject = new JSONObject();
		String custNo = RequestHelper.getSessionCustNo(request);
		String serialNo = request.getParameter("serialNo");
		String applyst = request.getParameter("applyst");
		logger.info("请求参数 custNo:" + custNo + ",serialNo:" + serialNo + ",applyst:" + applyst);
		if(StringUtils.isBlank(custNo) || StringUtils.isBlank(serialNo) || StringUtils.isBlank(applyst)) {
			logger.info("关键请求参数为空");
			jsonObject.put("resultCode", ECConstants.RETURN_CODE_9000);
			jsonObject.put("resultMsg", ECConstants.RETURN_MSG_9000);
			return jsonObject.toString();
		}
		FundTradeDto fundTradeDto = new FundTradeDto();
		fundTradeDto.setCustNo(custNo);
		fundTradeDto.setSerialNo(serialNo);
		fundTradeDto.setApplyst(applyst);
		OrderResult orderResult = tradeManager.updateOrderApplyst(fundTradeDto);
		jsonObject.put("resultCode", orderResult.getResultCode());
		jsonObject.put("resultMsg", orderResult.getResultMsg());
		logger.info("【TradeController】updateOrderApplyst()结束》》》结果：" + jsonObject);
		return jsonObject.toString();
	}
}
