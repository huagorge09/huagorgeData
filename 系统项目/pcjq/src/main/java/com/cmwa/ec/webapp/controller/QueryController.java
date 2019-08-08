package com.cmwa.ec.webapp.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmwa.ec.query.facade.model.consultant.CustserviceInfoDTO;
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
import com.cmwa.ec.query.facade.dto.account.TradeAcctDto;
import com.cmwa.ec.query.facade.dto.account.TradeAcctInfoDto;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.fund.*;
import com.cmwa.ec.query.facade.dto.merchant.UserInfoDto;
import com.cmwa.ec.query.facade.dto.system.ParameterDto;
import com.cmwa.ec.query.facade.dto.trade.AppointRequestDto;
import com.cmwa.ec.query.facade.dto.web.AdvertDto;
import com.cmwa.ec.query.facade.dto.web.CampusTalkDto;
import com.cmwa.ec.query.facade.dto.web.FileDownloadCenterDto;
import com.cmwa.ec.query.facade.dto.web.QuestionDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.webapp.manager.QueryManager;
import com.cmwa.ec.webapp.manager.UserManager;
import com.cmwa.ec.webapp.util.*;
import com.cmwa.ec.webapp.util.upload.jspsmart.SmartUpload;

@Controller("QueryController")
@RequestMapping(value = "/AppService")
public class QueryController {

	private static Logger logger = Logger.getLogger(QueryController.class.getName());
	
	@Autowired
	private QueryManager queryManager;
	@Autowired
	private UserManager userManager;
	
	/**
	 * 根据产品id查询产品的合同， 新的合同查询接口，所有产品统一只有一个合同
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryFundContractById.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryFundContractById(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);

		logger.info("QueryController类【queryFundContractById】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:"
				+ System.currentTimeMillis());

		String fundId = request.getParameter("fundId");
		String period = request.getParameter("period");
		if (period==null || "".equals(period)) {
			period="1";
		}

		logger.debug("QueryController类【queryFundContractById】获取页面传递参数>>>fundId=" + fundId);

		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
				cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		
		if(!StringUtils.isEmpty(period) && !StringUtils.isNumeric(period)){
			returnJsonObject.put("resultCode", ECConstants.RETURN_CODE_9008);
			returnJsonObject.put("resultMsg", ECConstants.RETURN_MSG_9008);
			return returnJsonObject.toString();
		}
		
		QueryMessageDto queryMessageDto = queryManager.queryFundContractById(context, fundId,period);

		FundContractDto fundContractDto = null;

		if (queryMessageDto != null) {
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();
			fundContractDto = (FundContractDto) queryMessageDto.getData();
		}

		returnJsonObject.put("resultCode", resultCode);
		returnJsonObject.put("resultMsg", resultMsg);
		returnJsonObject.put("fundContractDto", fundContractDto);

		logger.info("QueryController类【queryFundContractById】结束>>>resultCode=" + resultCode + ">>>resultMsg=" + resultMsg);
		
		return returnJsonObject.toString();

	}
	
	/**
	 * 查询用户交易账号列表  手机号码脱敏
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryUserTradeAcctInfoList.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryUserTradeAcctInfoList(HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		logger.info("QueryController类【queryUserTradeAcctInfoList】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:"
				+ System.currentTimeMillis());

		JSONObject returnJsonObject = new JSONObject();
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		if (userAccoRla == null) {
			logger.info("该用户没有实名无法查询订单");
			returnJsonObject.put("returnCode", 9000);
			returnJsonObject.put("returnMsg", "该用户没有实名无法查询订单");
			return returnJsonObject.toString();
		}
		String ecCustNo = userAccoRla.getEcCustNo();	
		if(StringUtil.isEmpty(ecCustNo)){
			logger.info("缺少关键参数--ecCustNo客户编号");
			returnJsonObject.put("returnCode", 9000);
			returnJsonObject.put("returnMsg", "缺少关键参数--ecCustNo客户编号");
			return returnJsonObject.toString();
		}
		
		logger.info("QueryController类【queryUserTradeAcctInfoList】获取到用户数据ecCustNo = " + ecCustNo);
		
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
				cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		
		String resultCode = "";
		String resultMsg = "";
		
		QueryMessageDto queryMessageDto = queryManager.queryUserTradeAcctInfoList(context, ecCustNo, null);
		
		List<TradeAcctInfoDto> tradeAcctList = null;
		List<TradeAcctInfoDto> encryptList = new ArrayList<TradeAcctInfoDto>();
		if (queryMessageDto != null) {
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();
			tradeAcctList = (List<TradeAcctInfoDto>) queryMessageDto.getData();
			for (TradeAcctInfoDto tradeAcctInfoDto : tradeAcctList) {
				tradeAcctInfoDto.setBankAccoDisplay(RequestHelper.getEncryptBankCard(tradeAcctInfoDto.getBankAccoDisplay()));
				tradeAcctInfoDto.setMobile(RequestHelper.getEncryptMobile(tradeAcctInfoDto.getMobile()));
				encryptList.add(tradeAcctInfoDto);
			}
		}
		
		returnJsonObject.put("resultCode", resultCode);
		returnJsonObject.put("resultMsg", resultMsg);
		returnJsonObject.put("tradeAcctList", encryptList);
		
		logger.info("QueryController类【queryUserTradeAcctInfoList】结束>>>resultCode=" + resultCode + ">>>resultMsg=" + resultMsg);
		
		return returnJsonObject.toString();
		
	}
	
	
	/**
	 * 查询用户交易账号列表  手机号码脱敏
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryUserTradeAcctInfo.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryUserTradeAcctInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		logger.info("QueryController类【queryUserTradeAcctInfo】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:"
				+ System.currentTimeMillis());

		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		String tradeNo = request.getParameter("tradeNo");
		
		JSONObject returnJsonObject = new JSONObject();
		if (userAccoRla == null) {
			logger.info("该用户没有实名无法查询订单");
			returnJsonObject.put("returnCode", 9000);
			returnJsonObject.put("returnMsg", "该用户没有实名无法查询订单");
			return returnJsonObject.toString();
		}
		String ecCustNo = userAccoRla.getEcCustNo();			
		if(StringUtil.isEmpty(ecCustNo)){
			logger.info("缺少关键参数--ecCustNo客户编号");
			returnJsonObject.put("returnCode", 9000);
			returnJsonObject.put("returnMsg", "缺少关键参数--ecCustNo客户编号");
			return returnJsonObject.toString();
		}
		
		logger.info("QueryController类【queryUserTradeAcctInfo】获取到用户数据ecCustNo = " + ecCustNo);
		
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
				cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		
		String resultCode = "";
		String resultMsg = "";
		
		QueryMessageDto queryMessageDto = queryManager.queryUserTradeAcctInfo(context, ecCustNo,tradeNo,null);
		
		List<TradeAcctInfoDto> tradeAcctList = null;
		List<TradeAcctInfoDto> encryptList = new ArrayList<TradeAcctInfoDto>();
		if (queryMessageDto != null) {
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();
			tradeAcctList = (List<TradeAcctInfoDto>) queryMessageDto.getData();
			for (TradeAcctInfoDto tradeAcctInfoDto : tradeAcctList) {
				tradeAcctInfoDto.setBankAccoDisplay(RequestHelper.getEncryptBankCard(tradeAcctInfoDto.getBankAccoDisplay()));
				tradeAcctInfoDto.setMobile(tradeAcctInfoDto.getMobile());
				encryptList.add(tradeAcctInfoDto);
			}
		}
		
		returnJsonObject.put("resultCode", resultCode);
		returnJsonObject.put("resultMsg", resultMsg);
		returnJsonObject.put("tradeAcctList", encryptList);
		
		logger.info("QueryController类【queryUserTradeAcctInfo】结束>>>resultCode=" + resultCode + ">>>resultMsg=" + resultMsg);
		
		return returnJsonObject.toString();
		
	}
	/**
	 * 查询用户交易账号列表  手机号码完全显示   不脱敏
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryUserTradeAcctInfoList2.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryUserTradeAcctInfoList2(HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		String fundid = request.getParameter("fundid");
		
		logger.info("QueryController类【queryUserTradeAcctInfoList】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:"
				+ System.currentTimeMillis());

		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		String ecCustNo = "";
		if(userAccoRla != null){
			ecCustNo = userAccoRla.getEcCustNo();
		}
		
		logger.info("QueryController类【queryUserTradeAcctInfoList】获取到用户数据ecCustNo = " + ecCustNo);
		
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
				cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		
		QueryMessageDto queryMessageDto = queryManager.queryUserTradeAcctInfoList(context, ecCustNo, fundid);
		
		List<TradeAcctInfoDto> tradeAcctList = null;
		List<TradeAcctInfoDto> encryptList = new ArrayList<TradeAcctInfoDto>();
		if (queryMessageDto != null) {
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();
			tradeAcctList = (List<TradeAcctInfoDto>) queryMessageDto.getData();
			for (TradeAcctInfoDto tradeAcctInfoDto : tradeAcctList) {
				tradeAcctInfoDto.setBankAccoDisplay(RequestHelper.getEncryptBankCard(tradeAcctInfoDto.getBankAccoDisplay()));
				encryptList.add(tradeAcctInfoDto);
			}
		}
		
		returnJsonObject.put("resultCode", resultCode);
		returnJsonObject.put("resultMsg", resultMsg);
		returnJsonObject.put("tradeAcctList", encryptList);
		
		logger.info("QueryController类【queryUserTradeAcctInfoList】结束>>>resultCode=" + resultCode + ">>>resultMsg=" + resultMsg);
		
		return returnJsonObject.toString();
		
	}
	
	/**
	 * 查询单个产品信息
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryFund.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryFund(HttpServletResponse response, HttpServletRequest request) {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);

		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
				cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

		logger.info("QueryController类【queryFund 】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:"
				+ System.currentTimeMillis());
		String fundId = request.getParameter("fundId");

		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		
		if(fundId == null || fundId.equals("")){
			returnJsonObject.put("resultCode", "9000");
			returnJsonObject.put("resultMsg", "找不到该产品！");
			return returnJsonObject.toString();
		}

		logger.info("QueryController类【queryFund 】获取前台的数据>>>fundid=" + fundId);
		String period = request.getParameter("period");
		if (period==null || "".equals(period)) {
			//不传期数则默认第一期
			period="1";
		}
		try {
			QueryMessageDto queryMessageDto = queryManager.queryFund(context, fundId,Integer.parseInt(period));

			FundInfoDtoV2 fundInfoDto = null;
			List<QuestionDto> quesList = null;
			if (queryMessageDto != null) {
				resultCode = queryMessageDto.getResultCode();
				resultMsg = queryMessageDto.getResultMsg();
				returnJsonObject.put("resultCode", resultCode);
				returnJsonObject.put("resultMsg", resultMsg);
				fundInfoDto = (FundInfoDtoV2) queryMessageDto.getData();
				byte[] pic = null;
				String content = null;
				if(fundInfoDto != null){
					List<FundElementDto> elementList = fundInfoDto.getElementList();
					if(elementList != null && elementList.size() > 0){
						saveImageToDisk(elementList);
					}
				}
				
				quesList = (List<QuestionDto>) queryMessageDto.getOtherData();
				if (fundInfoDto != null) {
					returnJsonObject.put("fundInfoDto", fundInfoDto);
					logger.info("QueryController类【queryFund】结束>>>resultCode:" + resultCode + ">>>resultMsg:" + resultMsg + ">>>timestamp:" + System.currentTimeMillis());
				}else{
					logger.info("QueryController类【queryFund】结束>>>fundInfoDto:[fundInfoDto is null]>>>timestamp:" + System.currentTimeMillis());
				}
				
				if(quesList != null && quesList.size() > 0){
					returnJsonObject.put("quesList", quesList);
				}else{
					logger.info("QueryController类【queryFund】结束>>>fundInfoDto:[quesList is null || quesList.size == 0]>>>timestamp:" + System.currentTimeMillis());
				}
			}
		} catch (Exception e) {
			logger.info("QueryController类【queryFund】查询产品异常"+QueryMessageDto.O_FUND_FAIL.getResultMsg());
		}

		logger.info("QueryController类【queryFund】结束>>>resultCode:" + resultCode + ">>>resultMsg:" + resultMsg + ">>>timestamp:" + System.currentTimeMillis());
		
		return returnJsonObject.toString();
		
	}


	private void saveImageToDisk(List<FundElementDto> elementList) {
		byte[] pic;
		String content;
		for (FundElementDto fundElementDto : elementList) {
			if(fundElementDto.getType() == 230){
				pic = fundElementDto.getPic();// 文件二进制
				content = fundElementDto.getContent();// 文件名称
				
				String preUrl = SpringUtil.getProperty("ecFundElePicPre");
				String url = SpringUtil.getProperty("ecFundElePic");
				if(!FileUtil.checkHasFile(preUrl+url, content)){// 将二进制数据写入到指定路径中
					FileUtil.writeFile(preUrl+url, content, pic);
					
					fundElementDto.setPicUrl(url+content);
					fundElementDto.setPic(null);
				}else{
					fundElementDto.setPicUrl(url+content);
					fundElementDto.setPic(null);
				}
				
				continue;
			}
		}
	}
	
	
	
	/**
	 * 查询热销产品列表， 和产品展示特性有关， 不为空。 ishot = 1 && state > 0    取第一个
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryHotFundList.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryHotFundList(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
				cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		logger.info("QueryController类【queryHotFundList 】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());
		
		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		
		QueryMessageDto queryMessageDto = queryManager.queryHotFundList(context);
		
		FundInfoDtoV2 hotFundDto = null;
		if (queryMessageDto != null) {
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();
			returnJsonObject.put("resultCode", resultCode);
			returnJsonObject.put("resultMsg", resultMsg);
			
			hotFundDto = (FundInfoDtoV2) queryMessageDto.getData();
			if (hotFundDto != null) {
				returnJsonObject.put("hotFundList", hotFundDto);
				logger.info("QueryController类【queryHotFundList】结束>>>resultCode:" + resultCode + ">>>resultMsg:" + resultMsg + ">>>timestamp:" + System.currentTimeMillis());
			}else{
				logger.info("QueryController类【queryHotFundList】结束>>>hotFundList:[hotFundList is null]>>>timestamp:" + System.currentTimeMillis());
			}
		}
		
		return returnJsonObject.toString();
		
	}
	
	/**
	 * 查询所有可展示的产品列表
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryFundList.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryFundList(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		long startMills = System.currentTimeMillis();
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
				cmfUserId, startMills , startMills , seqId, "");

		String type = request.getParameter("type");
		logger.info("QueryController类【queryFundList 】开始>>>cmfUserId:" + cmfUserId + ">>>广告类型type:" + type);
		
		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		
		QueryMessageDto queryMessageDto = queryManager.queryFundList(context);
		
		List<FundInfoDtoV2> fundInfoDtoList = new ArrayList<FundInfoDtoV2>();
		if (queryMessageDto != null) {
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();
			returnJsonObject.put("resultCode", resultCode);
			returnJsonObject.put("resultMsg", resultMsg);
			
			fundInfoDtoList = (List<FundInfoDtoV2>) queryMessageDto.getData();
			if (fundInfoDtoList != null) {
				returnJsonObject.put("fundInfoDtoList", fundInfoDtoList);
				logger.info("QueryController类【queryFundList】结束>>>resultCode:" + resultCode + ">>>resultMsg:" + resultMsg + ">>>timestamp:" + System.currentTimeMillis());
			}else{
				logger.info("QueryController类【queryFundList】结束>>>fundInfoDtoList:[fundInfoDtoList is null]>>>timestamp:" + System.currentTimeMillis());
			}
		}
		
		QueryMessageDto queryAdvert = queryManager.queryAdvert(context, type);
		List<AdvertDto> advertDtoList = null;
		if(queryAdvert != null){
			advertDtoList = (List<AdvertDto>) queryAdvert.getData();
		}
		if(advertDtoList != null && advertDtoList.size() > 0){
			returnJsonObject.put("advertDtoList", advertDtoList);
		}
		logger.info("QueryController类【queryFundList 】查询结束>>>cmfUserId:" + cmfUserId + ">>>广告类型type:" + type + "总共耗时：" 
				+ (System.currentTimeMillis() - startMills) + "ms");
		
		return returnJsonObject.toString();
		
	}
	
	/**
	 * 查询费率和折扣
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryFeeRateList.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryFeeRateList(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);

		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

		logger.info("QueryController类【queryFeeRateList 】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());
		
		String fundId = request.getParameter("fundId");
		String channelNo = request.getParameter("channelNoList");
		String custLevel = request.getParameter("custLevel");
		
		String money = request.getParameter("money");
		
		logger.info("QueryController类【queryFeeRateList 】获取前台参数>>>fundId:" + fundId + ">>>channelNo:" + channelNo + ">>>custLevel:" + custLevel);
		
		// 获得渠道List
		List<String> channelNoList = new ArrayList<String>();
		if(channelNo != null && !channelNo.equals("")){
			if(channelNo.endsWith(",")){
				channelNo = channelNo.substring(0, channelNo.length() - 1);
			}
			String[] channelNoTemp = channelNo.split(",");
			for (int i = 0; i < channelNoTemp.length; i++) {
				channelNoList.add(channelNoTemp[i]);
			}
		}
		
		if(custLevel == null){
			custLevel = "";
		}
		
		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "0000";
		String resultMsg = "成功";
		QueryMessageDto queryMessageDto = new QueryMessageDto();
		Map<String, List<FeeRateDto>> map = new HashMap<String, List<FeeRateDto>>();
		queryMessageDto = queryManager.queryFeeRateList(context, fundId, channelNoList, custLevel);
		if(null != queryMessageDto){
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();
			map = (Map<String, List<FeeRateDto>>) queryMessageDto.getData();
		}
		double rate = 0.0;
		double commro = 0.0;
		double feeMode = 0;
		if(map != null){
			Map<String, Double> returnMap = QueryFeeRateUtils.queryFeeRate(map, fundId, channelNoList, money);
			if(returnMap != null){
				rate = returnMap.get("rate");
				commro = returnMap.get("commro");
				feeMode = returnMap.get("feeMode");
			}
		}
		returnJsonObject.put("resultCode", resultCode);
		returnJsonObject.put("resultMsg", resultMsg);
		returnJsonObject.put("rate", rate);
		returnJsonObject.put("commro", commro);
		returnJsonObject.put("feeMode", feeMode);
		return returnJsonObject.toString();
	}
	
	/**
	 * 查询产品净值（净值类产品） 无分页 有时间条件
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryEstimateByFundId.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryEstimateByFundId(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);

		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

		logger.info("QueryController类【queryEstimateByFundId 】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());
		
		String fundId = request.getParameter("fundId");
		String dateTimeStr = request.getParameter("dateTime");
		
		Integer dateTime = 0;
		if(dateTimeStr != null && !dateTimeStr.equals("")){
			dateTime = Integer.parseInt(dateTimeStr);
		}
		
		logger.info("QueryController类【queryEstimateByFundId 】获取前台参数>>>fundId:" + fundId + ">>>dateTime=" + dateTime);
		
		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "0000";
		String resultMsg = "成功";
		
		QueryMessageDto queryMessageDto = queryManager.queryEstimateByFundId(context, fundId, dateTime);
		List<ProductEstimate> productEstimateList = new ArrayList<ProductEstimate>();
		
		resultCode = queryMessageDto.getResultCode();
		resultMsg = queryMessageDto.getResultMsg();
		
		if(queryMessageDto != null){
			productEstimateList = (List<ProductEstimate>) queryMessageDto.getData();
		}
		
		returnJsonObject.put("resultCode", resultCode);
		returnJsonObject.put("resultMsg", resultMsg);
		returnJsonObject.put("productEstimateList", productEstimateList);

		logger.info("QueryController类【queryEstimateByFundId 】结束>>>resultCode:" + resultCode + ">>>resultMsg:" + resultMsg + ">>>timestamp:" + System.currentTimeMillis());

		return returnJsonObject.toString();
	}
	
	/**
	 * 查询产品净值（净值类产品） 有分页 无时间条件
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryEstimateByFundIdByPage.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryEstimateByFundIdByPage(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		logger.info("QueryController类【queryEstimateByFundIdByPage 】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());
		
		String fundId = request.getParameter("fundId");
		String pages = request.getParameter("page");
		
		Integer dateTime = 0;
		
//		int beginIdx = 0;// 每页展示的开始的条数
		int rowCount = Integer.parseInt(ECConstants.PAGE_LIST_NETVAL);// 每页展示的条数
//		int count = -1;// 总条数
		int maxPages = 1;// 总页数
		int page = 1;// 当前页
		
		if(pages != null && !pages.equals("")){
			page = Integer.parseInt(pages);
		}

		logger.info("QueryController类【queryEstimateByFundIdByPage 】获取前台参数>>>fundId:" + fundId + ">>>page=" + page);
		
		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "0000";
		String resultMsg = "成功";
		
		QueryMessageDto queryMessageDto = queryManager.queryEstimateByFundId(context, fundId, dateTime);
		List<ProductEstimate> productEstimateList = new ArrayList<ProductEstimate>();
		
		List<ProductEstimate> list =  new ArrayList<ProductEstimate>();
		
		resultCode = queryMessageDto.getResultCode();
		resultMsg = queryMessageDto.getResultMsg();
		
		if(queryMessageDto != null){
			productEstimateList = (List<ProductEstimate>) queryMessageDto.getData();
		}
		
		if (productEstimateList != null) {
			int i = 1;
			for (ProductEstimate dto : productEstimateList) {
				if (dto == null)
					break;
				if (rowCount * (page - 1) + 1 <= i && i <= rowCount * page) {// 取指定页数的数据
					list.add(dto);
				} else if (i > rowCount * page) {
					break;
				}
				i++;
			}
			// 计算总页数
			maxPages = productEstimateList.size() % rowCount == 0 ? productEstimateList.size() / rowCount : productEstimateList.size() / rowCount + 1;
//			count = productEstimateList.size();
		}
		
		returnJsonObject.put("resultCode", resultCode);
		returnJsonObject.put("resultMsg", resultMsg);
		returnJsonObject.put("page", page);
		returnJsonObject.put("maxPages", maxPages);
		returnJsonObject.put("productEstimates", list);
		
		logger.info("QueryController类【queryEstimateByFundIdByPage 】结束>>>resultCode:" + resultCode + ">>>resultMsg:" + resultMsg + ">>>timestamp:" + System.currentTimeMillis());
		
		return returnJsonObject.toString();
	}

	
	/**
	 * 查询产品的元素， 复用为查QA
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryFundElementList.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryFundElementList(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		String fundId = "PAGEQA";
		
		logger.info("QueryController类【queryFundElementList 】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());
		
		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		
		QueryMessageDto queryMessageDto = queryManager.queryFundElementList(context,fundId);
		
		List<FundElementDto> fundElementDtoList = new ArrayList<FundElementDto>();
		if (queryMessageDto != null) {
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();
			returnJsonObject.put("resultCode", resultCode);
			returnJsonObject.put("resultMsg", resultMsg);
			
			fundElementDtoList = (List<FundElementDto>) queryMessageDto.getData();
			if (fundElementDtoList != null) {
				returnJsonObject.put("fundElementDtoList", fundElementDtoList);
				logger.info("QueryController类【queryFundElementList】结束>>>resultCode:" + resultCode + ">>>resultMsg:" + resultMsg + ">>>timestamp:" + System.currentTimeMillis());
			}else{
				logger.info("QueryController类【queryFundElementList】结束>>>fundInfoDtoList:[fundInfoDtoList is null]>>>timestamp:" + System.currentTimeMillis());
			}
		}
		
		return returnJsonObject.toString();
		
	}
	
	/**
	 * 根据客户号，交易编号查询订单详情
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryTradeInfoByTradeNo.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryTradeInfoByTradeNo(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		String serialno = request.getParameter("serialno");
		String period = request.getParameter("period");
		
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");	
				
		JSONObject jsonObject = new JSONObject();
		
		if (userAccoRla == null) {
			logger.info("该用户没有实名无法查询订单");
			jsonObject.put("returnCode", 9000);
			jsonObject.put("returnMsg", "该用户没有实名无法查询订单");
			return jsonObject.toString();
		}
		String custno = userAccoRla.getEcCustNo();	
		if(StringUtil.isEmpty(custno)){
			logger.info("缺少关键参数--ecCustNo客户编号");
			jsonObject.put("returnCode", 9000);
			jsonObject.put("returnMsg", "缺少关键参数--ecCustNo客户编号");
			return jsonObject.toString();
		}
		
		logger.info("QueryController类【queryTradeInfoByTradeNo 】开始>>>cmfUserId:" + cmfUserId + ">>>serialno:" + serialno+ ">>>custno:" + custno+ ">>>timestamp:" + System.currentTimeMillis());
		String resultCode = "";
		String resultMsg = "";
		QueryMessageDto queryMessageDto=null;
		if (period!=null&&!"".equals(period)) {
			queryMessageDto = queryManager.queryTradeInfoByTradeNo(context, custno, serialno,Integer.parseInt(period)+1);
		}else{
			queryMessageDto = queryManager.queryTradeInfoByTradeNo(context, custno, serialno,1);
		}
		QueryMessageDto tradeMessage = null;
		
		AppointRequestDto appointRequestDto = new AppointRequestDto();
		
		if (queryMessageDto != null) {
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();
			jsonObject.put("resultCode", resultCode);
			jsonObject.put("resultMsg", resultMsg);
			appointRequestDto.getFundInfoDto();
			appointRequestDto = (AppointRequestDto) queryMessageDto.getData();
			List<QuestionDto> questionDtoList = (List<QuestionDto>) queryMessageDto.getOtherData();
			if (appointRequestDto != null) {
				//添加图片地址
				List<FundElementDto> elementList = appointRequestDto.getFundInfoDtoV2().getElementList();
				if ( null!= elementList && elementList.size()>0 ) {
					saveImageToDisk(elementList);
				}
				appointRequestDto.getFundInfoDtoV2().setElementList(elementList);
				jsonObject.put("appointRequestDto", appointRequestDto);
				logger.info("QueryController类【queryTradeInfoByTradeNo】结束>>>resultCode:" + resultCode + ">>>resultMsg:" + resultMsg + ">>>timestamp:" + System.currentTimeMillis());
				
				// 根据tradeAcco获取交易账号
				tradeMessage = queryManager.getTradeAcctInfoByTradeAcco(context, appointRequestDto.getTradeacco());
			}else{
				logger.info("QueryController类【queryTradeInfoByTradeNo】结束>>>fundInfoDtoList:[appointRequestDto is null]>>>timestamp:" + System.currentTimeMillis());
			}
			if(questionDtoList != null && questionDtoList.size() > 0){
				jsonObject.put("questionDtoList", questionDtoList);
			}
		}
//		if (appointRequestDto != null) {
//			jsonObject.put("dto", appointRequestDto);
//		}
		List<TradeAcctDto> tradeList = null;
		List<TradeAcctDto> encryptTradeList = new ArrayList<TradeAcctDto>();
		if (tradeMessage != null) {	
			tradeList = (List<TradeAcctDto>) tradeMessage.getData();
			for (TradeAcctDto tradeAcctDto : tradeList) {
				tradeAcctDto.setBankacco(RequestHelper.getEncryptBankCard(tradeAcctDto.getBankacco()));
				tradeAcctDto.setBankaccodisplay(RequestHelper.getEncryptBankCard(tradeAcctDto.getBankaccodisplay()));
				tradeAcctDto.setIdno(RequestHelper.getEncryptIdNo(tradeAcctDto.getIdno()));
				tradeAcctDto.setMobile(RequestHelper.getEncryptMobile(tradeAcctDto.getIdno()));
				tradeAcctDto.setBankacnm(RequestHelper.getEncryptUserName(tradeAcctDto.getBankacnm()));
				encryptTradeList.add(tradeAcctDto);
			}
		}

		if (tradeList != null && tradeList.size() > 0) {
			jsonObject.put("tradeList", encryptTradeList);
		}
		return jsonObject.toString();
		
	}
	
	/**
	 * 查询支持的银行卡列表
	 * @author luos
	 */
	@RequestMapping(value = "/business/getSupportBankDesc.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String getSupportBankDesc(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String sessionId = RequestHelper.getSeqId(request);
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, "", cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		JSONObject jsonObject = queryManager.getSupportBankDesc(context);
		return jsonObject.toString();
	}
	/**
	 * 根据银行卡号查找该卡号的具体银行信息 即卡号校验
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 * @author luos
	 */
	@RequestMapping(value = "/business/queryBankInfoByBankNumber.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryBankInfoByBankNumber(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String bankNumber = request.getParameter("bankNumber");
		String sessionId = RequestHelper.getSeqId(request);
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		JSONObject returnJsonObject = queryManager.queryBankInfoByBankNumber(context, bankNumber);
		return returnJsonObject.toString();
	}
	/***
	 * 验证证件号码
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @author luos
	 */
	@RequestMapping(value = "/business/checkIdNoByBankAuthentication.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String checkIdNoByBankAuthentication(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
		JSONObject returnJsonObject = null;
		String seqId = RequestHelper.getSeqId(request);
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		returnJsonObject = userManager.checkIdNoByBankAuthentication(context, request);
		return returnJsonObject.toString();
	}
	/**
	 * 查询用户的银行卡信息
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author luos
	 */
	@RequestMapping(value = "/business/queryMyBankCardNo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryMyBankCardNo(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject returnJsonObject = null;
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String sessionId = RequestHelper.getSeqId(request);
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		returnJsonObject = queryManager.getTradeAcctInfoByCustno(context,cmfUserId, request);
		return returnJsonObject.toString();
	}

	/**
	 * 信批 查询“我的消息”
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author luos
	 */
	@RequestMapping(value = "/business/queryUserMessageList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryUserMessageList(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		String tempPage = request.getParameter("page");
		int page = 1;
		if (tempPage != null && !tempPage.equals("") && StringUtils.isNumeric(tempPage)) {
			page = Integer.parseInt(tempPage);
		}
		String totalAmount = RequestHelper.getSessionAttr(request, SessionValue.SESSION_TOTALAMOUNT);
		if (totalAmount == null || "".equals(totalAmount) || !StringUtils.isNumeric(tempPage)) {
			totalAmount = "-1";
		}
		int amount = ECConstants.MESSAGE_AMOUNT;// 每页展示条数
		int beginIdx = (page - 1) * amount;
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		JSONObject jsonObject = queryManager.queryUserMessageList(context, cmfUserId, beginIdx, amount, Integer.parseInt(totalAmount));
		if (jsonObject.get("totalAmount") != null && Integer.parseInt(jsonObject.getString("totalAmount")) <= 0) {
			request.getSession(true).setAttribute(SessionValue.SESSION_TOTALAMOUNT, totalAmount);
		}
		jsonObject.put("currentPage", page);
		return jsonObject.toString();
	}
	
	/**
	 * 根据消息标题匹配信披信息
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/business/queryUserMsgLikeTitle.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryUserMsgLikeTitle(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		long startTime = System.currentTimeMillis();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), "", cmfUserId);
		Cookie cookieTip = getCookieByName(request, "isShowTips");
		if(null != cookieTip){
			jsonObject.put("isShowTips", cookieTip.getValue());
		}else{
			QueryMessageDto queryParamList = queryManager.queryHomeAddressIsWordWithLinkage(new Context(), "SYSTEM", "QUARTERMSGKEY", null, "");
			List<ParameterDto> listPara = (List<ParameterDto>) queryParamList.getData();
			if(null != listPara && listPara.size() > 0){
				JSONObject msgJsonObject = queryManager.queryUserMsgLikeTitle(context, cmfUserId, listPara.get(0).getPmco());
				String isShowTips = (String) msgJsonObject.get("isShowTips");
				Cookie cookie= new Cookie("isShowTips",isShowTips);
				cookie.setPath("/");
				response.addCookie(cookie);
				jsonObject.put("isShowTips", isShowTips);
			}
		}
		long costTimeForMills = System.currentTimeMillis() - startTime;
		logger.info("queryUserMsgLikeTitle method total cost " + costTimeForMills + " ms");
		return jsonObject.toString();
	}

	/**
	 * 根据名字获取cookie
	 * @param request
	 * @param name cookie名字
	 * @return
	 */
	public static Cookie getCookieByName(HttpServletRequest request,String name){
	    Map<String,Cookie> cookieMap = ReadCookieMap(request);
	    if(cookieMap.containsKey(name)){
	        Cookie cookie = (Cookie)cookieMap.get(name);
	        return cookie;
	    }else{
	        return null;
	    }   
	}
	 
	/**
	 * 将cookie封装到Map里面
	 * @param request
	 * @return
	 */
	private static Map<String,Cookie> ReadCookieMap(HttpServletRequest request){  
	    Map<String,Cookie> cookieMap = new HashMap<String,Cookie>();
	    Cookie[] cookies = request.getCookies();
	    if(null!=cookies){
	        for(Cookie cookie : cookies){
	            cookieMap.put(cookie.getName(), cookie);
	        }
	    }
	    return cookieMap;
	}
	
	/**
	 * 信批 详情
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author luos
	 */
	@RequestMapping(value = "/business/queryUserMessage.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryUserMessage(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		String msgId = request.getParameter("msgId");
		String msgType = request.getParameter("msgType");
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		JSONObject jsonObject = queryManager.queryUserMessage(context, cmfUserId, msgId, msgType);
		return jsonObject.toString();
	}

	/**
	 * 信批 pdf格式下载/打开
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author luos
	 */
	@RequestMapping(value = "/business/queryUserMessageByPDF.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryUserMessageByPDF(HttpServletResponse response, HttpServletRequest request) throws Exception {
		response.setCharacterEncoding("gbk");
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		logger.info("【QueryController】queryUserMessageByPDF()开始>>>seqId=" + seqId + ">>>timestamp=" + System.currentTimeMillis());
		String msgId = request.getParameter("msgId");
		String msgType = request.getParameter("msgType");
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		QueryMessageDto queryMessageDto = queryManager.queryUserMessageByPDF(context, cmfUserId, msgId, msgType);
		FundReportsPDFDto fundReportsPDFDto = new FundReportsPDFDto();
		String returnCode = "";
		String returnMsg = "";
		JSONObject jsonObject = new JSONObject();
		if (queryMessageDto != null) {
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			fundReportsPDFDto = (FundReportsPDFDto) queryMessageDto.getData();
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("【QueryController】queryUserMessageByPDF()获取调用后台的返回值>>>returnCode=" + returnCode + ">>>returnMsg=" + returnMsg + ">>>timestamp" + System.currentTimeMillis());
		if (fundReportsPDFDto != null) {
			try {
				SmartUpload smart = new SmartUpload();
				smart.service(request, response);
				smart.setContentDisposition(null);
				logger.info("--开始下载文件");
				String fileName = fundReportsPDFDto.getFileName();
				logger.info("【【【【【【【【【【[fileName-0]" + fileName + "】】】】】】】】】】");
				if ("2".equals(msgType) || "3".equals(msgType)) {
					fileName = fileName == null ? "产品文档.pdf" : fileName;
					if(fileName.indexOf(".pdf")<0 && fileName.indexOf(".mht")<0 && fileName.indexOf(".doc")<0
							&& fileName.indexOf(".PDF")<0 && fileName.indexOf(".html")<0
							&& fileName.indexOf(".txt")<0 && fileName.indexOf(".xls")<0){
						fileName = fileName + ".pdf";
					}
				} else if ("5".equals(msgType)) {
					fileName = fileName == null ? "产品文档.pdf" : fileName;
					if(fileName.indexOf(".pdf")<0 && fileName.indexOf(".mht")<0 && fileName.indexOf(".doc")<0
							&& fileName.indexOf(".PDF")<0 && fileName.indexOf(".html")<0
							&& fileName.indexOf(".txt")<0 && fileName.indexOf(".xls")<0){
						fileName = fileName + ".pdf";
					}
				}
				logger.info("【【【【【【【【【【[fileName-1]" + fileName + "】】】】】】】】】】");
				smart.downloadFile(fundReportsPDFDto.getContent(), "application/octet-stream", fileName);
			} catch (Exception e) {
				logger.info("--pdf文件下载失败");
				response.getOutputStream().print("下载失败，请稍后再试");
				e.printStackTrace();
			}
		} else {
			logger.info("--pdf文件不存在");
			response.getOutputStream().print("文件不存在");
		}
	}

	/**
	 * 信批 未读
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author luos
	 */
	@RequestMapping(value = "/business/queryUnReadMsg.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryUnReadMsg(HttpServletResponse response, HttpServletRequest request) {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		JSONObject returnJsonObject = queryManager.queryUnReadFundReportsCount(context, cmfUserId);
		if (returnJsonObject != null && returnJsonObject.getString("returnCode").equals("0000")) {
			returnJsonObject.put("unRead", returnJsonObject.getString("count"));
		} else {
			returnJsonObject.put("unRead", "0");
		}
		return returnJsonObject.toString();
	}

	/**
	 * 查询历史交易信息 对账单
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author luos
	 */
	@RequestMapping(value = "/business/queryFundTradeInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryFundTradeInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		Date date = new Date();// 当前日期
		Calendar calendar = Calendar.getInstance();// 日历对象
		calendar.setTime(date);// 设置当前日期
		calendar.add(Calendar.YEAR, -1);// 年份减一
		String startDate = "";
		// 当前时间
		String endDate = DateUtils.formatDate(date, "yyyyMMdd");
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		JSONObject jsonObject = queryManager.queryFundTradeInfo(context, cmfUserId, startDate, endDate);
		return jsonObject.toString();
	}
	/**
	 * 查询订单列表
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 *             String
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryTradeInfoList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryTradeInfoList(HttpServletResponse response, HttpServletRequest request) throws Exception {
		long startTimeForMills = System.currentTimeMillis();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		UserAccoRlaDto userAccoRlaDto = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);

		JSONObject returnJsonObject = new JSONObject();
		if (userAccoRlaDto == null) {
			logger.info("该用户没有实名无法查询订单");
			returnJsonObject.put("returnCode", 9000);
			returnJsonObject.put("returnMsg", "该用户没有实名无法查询订单");
			return returnJsonObject.toString();
		}
		String custno = userAccoRlaDto.getEcCustNo();	
		if(StringUtil.isEmpty(custno)){
			logger.info("缺少关键参数--ecCustNo客户编号");
			returnJsonObject.put("returnCode", 9000);
			returnJsonObject.put("returnMsg", "缺少关键参数--ecCustNo客户编号");
			return returnJsonObject.toString();
		}
		String fundid = request.getParameter("fundid");
		String applyst = request.getParameter("applyst");
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		returnJsonObject = queryManager.queryTradeInfoList(context, custno, fundid, applyst);
		logger.info("查询我的订单耗时：" +(System.currentTimeMillis() - startTimeForMills) + "ms");
		return returnJsonObject.toString();
	}
	
	/**
	 * 查询用户产品持仓
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/business/queryCustTradeInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryCustTradeInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
		long startTimeForMills = System.currentTimeMillis();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		String fundCode = request.getParameter("fundCode");
		String tradeAcco = request.getParameter("tradeAcco");
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		JSONObject returnJsonObject = queryManager.queryCustTradeInfo(context,cmfUserId,fundCode,tradeAcco);
		logger.info("查询用户产品持仓耗时：" +(System.currentTimeMillis() - startTimeForMills) + "ms");
		return returnJsonObject.toString();
	}
	
	/**
	 * 查询用户持有基金订单
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/business/queryUserHasProOreder.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryUserHasProOreder(HttpServletResponse response, HttpServletRequest request) throws Exception {
		long startTimeForMills = System.currentTimeMillis();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		String fundCode = request.getParameter("fundCode");
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		JSONObject returnJsonObject = queryManager.queryNewCustomer(context,cmfUserId,fundCode);
		logger.info("查询用户持有基金订单耗时：" +(System.currentTimeMillis() - startTimeForMills) + "ms");
		return returnJsonObject.toString();
	}
	/**
	 * 查询资产和总收益 以及用户信息
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 *             String
	 * @author luos
	 */
	@RequestMapping(value = "/business/queryAccount.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryAccount(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		logger.info("用户cmfuserid:" + cmfUserId);
		String seqId = RequestHelper.getSeqId(request);
		JSONObject returnJsonObject = new JSONObject();
		UserBaseInfoDto userBaseInfo = RequestHelper.getSessionUserBaseInfo(request);
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		try {
			UserServiceMessage userServiceMessage = userManager.queryUserAndAccoRlaById(context, cmfUserId);
			logger.info("查询登录用户信息，得到值为：returncode=" + userServiceMessage.getReturnCode());
			UserAccoRlaDto newAccoRlaDto = userServiceMessage != null ? userServiceMessage.getUserAccoRlaDto() : null;
			
			returnJsonObject = queryManager.queryTotalFundBalance(context, cmfUserId, userBaseInfo, seqId, newAccoRlaDto);
			logger.info("查询用户资产和信息返回结果：totalFundBalanceCode=" + returnJsonObject.get("totalFundBalanceCode") 
				+ ",totalFundBalanceMsg=" + returnJsonObject.get("totalFundBalanceMsg"));
			
			JSONObject conutJsonObject = queryManager.queryUnReadFundReportsCount(context, cmfUserId);
			logger.info("返回码returnCode=" + conutJsonObject.getString("returnCode") + 
					",得到信批未读条数count=" + conutJsonObject.getString("count"));
			if (conutJsonObject != null && conutJsonObject.getString("returnCode").equals("0000")) {
				returnJsonObject.put("unRead", conutJsonObject.getString("count"));
			} else {
				returnJsonObject.put("unRead", "0");
			}
		} catch (Exception e) {
			logger.error("查询信批未读条数 Exception：",e);
		}
		return returnJsonObject.toString();
	}

	
	/**
	 * 新闻动态列表查询    支持分页
	 * 查询指定页数展示的新闻和新闻总数
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryNewsArticle.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryNewsArticle(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		String tempPage = request.getParameter("page");
		int page = 1;
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}

		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
			name = URLDecoder.decode(name, "utf-8");
		}
		
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.queryNewsArticleAndCount(context, cmfUserId, page, name);
		
		return jsonObject.toString();
	}
	
	/**
	 * 公司公告列表查询    支持分页
	 * 查询指定页数展示的公司公告和公告总数
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryCompanyArticle.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryCompanyArticle(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		
		String tempPage = request.getParameter("page");
		int page = 1;
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}

		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
			name = URLDecoder.decode(name, "utf-8");
		}
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.queryCompanyArticleAndCount(context, cmfUserId, page, name);
		
		return jsonObject.toString();
	}
	
	/**
	 * 查询详情     新闻动态、公司公告
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryArticleContent.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryArticleContent(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		
		String articleId = request.getParameter("articleId");
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.queryArticleContent(context, articleId);
		
		return jsonObject.toString();
	}
	
	/**
	 * 根据fundTypeId查询指定类型的最新产品
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryHotFundByType.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryHotFundByType(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		
		String type = "0100";
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.queryHotFundByType(context, type);
		
		return jsonObject.toString();
	}
	
	/**
	 * 社会招聘查询   带分页
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value = "/article/querySocialRecruitment.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String querySocialRecruitment(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.querySocialRecruitment(context, request);
		
		return jsonObject.toString();
	}
	
	/**
	 * 校园招聘查询   带分页
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryCampusRecruitment.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryCampusRecruitment(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.queryCampusRecruitment(context, request);
		
		return jsonObject.toString();
	}
	
	/**
	 * 实习生招聘查询   带分页
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryTraineeRecruitment.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryTraineeRecruitment(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.queryTraineeRecruitment(context, request);
		
		return jsonObject.toString();
	}
	
	/**
	 * 招聘信息详情查询
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryRecruitmentDetail.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryRecruitmentDetail(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.queryRecruitmentDetail(context, request);
		
		return jsonObject.toString();
	}
	
	/**
	 * 校园宣讲查询
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryCampusTalk.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryCampusTalk(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.queryCampusTalk(context, request);
		
		return jsonObject.toString();
	}
	
	/**
	 * 查询校园宣讲详细内容 并下载
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/article/campusTalkDownLoad.xhtml", method = { RequestMethod.POST,RequestMethod.GET})
	public void campusTalkDownLoad(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		String campusId = request.getParameter("campusId");
		
		logger.info("【QueryController】campusTalkDownLoad()开始>>>seqId=" + seqId + ">>>cmfUserId=" + cmfUserId + ">>>campusId=" + campusId + ">>>timestamp=" + System.currentTimeMillis());

		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		QueryMessageDto queryMessageDto = queryManager.campusTalkDownLoad(context, campusId);
		
		CampusTalkDto campusTalkDto = null;
		
		if(queryMessageDto != null){
			campusTalkDto = (CampusTalkDto) queryMessageDto.getData();
		}
		
		if(campusTalkDto == null){//附件未找到！
			response.getOutputStream().print("文件不存在");
			return;
		}
		
		try {
			SmartUpload su = new SmartUpload();
			su.service(request, response);
			su.setContentDisposition(null);
			logger.info("开始下载文件");
			String filename = campusTalkDto.getSchoolName();
			if(campusTalkDto.getCampusContent() != null && campusTalkDto.getCampusContent().split("\\.").length > 1){
				filename = filename+"."+campusTalkDto.getCampusContent().split("\\.")[1];
			}
			
			su.downloadFile(campusTalkDto.getContent(),"APPLICATION/OCTET-STREAM",filename);
		} catch (Exception e) {
			logger.info("--pdf文件下载失败");
			response.getOutputStream().print("下载失败，请稍后再试");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 查询常见问题
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryWenTiByCommon.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryWenTiByCommon(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.queryWenTiByCommon(context, request);
		
		return jsonObject.toString();
	}
	
	/**
	 * 查询问题列表   分页
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryWenTiList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryWenTiList(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.queryWenTiList(context, request);
		
		return jsonObject.toString();
	}
	/**
	 * 查询费率
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/business/queryFeeRates.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryFeeRates(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String fundId = request.getParameter("fundId");
		String money = request.getParameter("money");
		if (money != null && money.length() > 0) {
			money = URLDecoder.decode(money, "utf-8");
		}
		String sessionId = request.getSession().getId();
		List<String> channelNoList = new ArrayList<String>();
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		JSONObject resultJson = queryManager.queryFeeRateList(request, money, context, fundId, channelNoList, "");
		return resultJson.toString();
	}
	/**
	 * 根据交易账号查询银行卡信息
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/business/queryBankInfoByTradeAcco.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryBankInfoByTradeAcco(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String tradeacco = request.getParameter("tradeacco");
		String sessionId = request.getSession().getId();
		List<String> channelNoList = new ArrayList<String>();
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		JSONObject resultJson = queryManager.queryBankInfoByTradeAcco(context,tradeacco);
		return resultJson.toString();
	}
	/**
	 * 查询用户代销机构订单列表
	 * 
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 *             String
	 * @author maj
	 */
	@RequestMapping(value = "/business/queryAgentFundInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryAgentFundInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
		long startTimeMills = System.currentTimeMillis();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		JSONObject returnJsonObject =new JSONObject();
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		returnJsonObject = queryManager.queryAgentFundInfo(context, cmfUserId);
		logger.info("根据cmfuserid="+ cmfUserId + "查询PC官网，其它订单查询，耗时" + (System.currentTimeMillis() - startTimeMills) + "ms");
		return returnJsonObject.toString();
	}

	/**
	 * 查询合作机构和代销机构
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryCooperation.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryCooperation(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		JSONObject jsonObject =new JSONObject();
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		jsonObject = queryManager.queryCooperation(context, request);
		return jsonObject.toString();
	}
	
	/**
	 * 查询用户的银行卡信息
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author luos
	 */
	@RequestMapping(value = "/business/queryMyOriginalBankCardNo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryMyOriginalBankCardNo(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject returnJsonObject = null;
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String sessionId = RequestHelper.getSeqId(request);
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		returnJsonObject = queryManager.getTradeOriginalAcctInfoByCustno(context, request);
		return returnJsonObject.toString();
	}
	
	
	
	/**
	 * 下载中心查询
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryFileDownloadCenter.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryFileDownloadCenter(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = null;
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		jsonObject = queryManager.queryFileDownloadCenter(context, request);
		return jsonObject.toString();
	}
	
	/**
	 * 查询下载中心详细内容 并下载
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryFileDownloadCenterDetail.xhtml", method = { RequestMethod.POST,RequestMethod.GET})
	public void queryFileDownloadCenterDetail(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		String fileId = request.getParameter("fileId");
		
		logger.info("【QueryController】queryFileDownloadCenterDetail()开始>>>seqId=" + seqId + ">>>cmfUserId=" + cmfUserId + ">>>fileId=" + fileId + ">>>timestamp=" + System.currentTimeMillis());

		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		QueryMessageDto queryMessageDto = queryManager.queryFileDownloadCenterDetail(context, fileId, "");
		
		List<FileDownloadCenterDto> list = null;
		FileDownloadCenterDto dto = null;
		
		if(queryMessageDto != null){
			list = (List<FileDownloadCenterDto>) queryMessageDto.getData();
		}
		
		if(list != null && list.size() > 0){
			dto = list.get(0);
		}
		
		if(dto == null){//附件未找到！
			response.getOutputStream().print("文件不存在");
			return;
		}
		
		try {
			SmartUpload su = new SmartUpload();
			su.service(request, response);
			su.setContentDisposition(null);
			logger.info("开始下载文件");
			String filename = dto.getFileName();
			if(dto.getFileDownload() != null && dto.getFileDownload().split("\\.").length > 1){
				filename = filename+"."+dto.getFileDownload().split("\\.")[1];
			}
			
			su.downloadFile(dto.getContent(),"APPLICATION/OCTET-STREAM",filename);
		} catch (Exception e) {
			logger.info("--pdf文件下载失败");
			response.getOutputStream().print("下载失败，请稍后再试");
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 其它业务流程 查询
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception String
	 * @author maj
	 */
	@RequestMapping(value = "/article/queryOpenAccountProcess.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryOpenAccountProcess(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = null;
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		jsonObject = queryManager.queryOpenAccountProcess(context, request);
		return jsonObject.toString();
	}
	/**
	 * 查询代销产品订单详情
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception String
	 * @author luos
	 */
	@RequestMapping(value = "/business/queryOtherDetailOrder.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryOtherDetailOrder(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = null;
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		jsonObject = queryManager.queryOtherDetailOrder(context, request);
		return jsonObject.toString();
	}
	/**
	 * 信批 pdf格式下载/打开
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author luos
	 */
	@RequestMapping(value = "/setUp/queryUserMessageByPDFForCompany.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryUserMessageByPDFForCompany(HttpServletResponse response, HttpServletRequest request) throws Exception {
		response.setCharacterEncoding("GBK");
		String cmfUserId = (String) request.getSession().getAttribute("fundAcco");
		String type = (String) request.getSession().getAttribute("type");
		if(ECConstants.SESSION_USERTYPE.equals(type)){
			cmfUserId = ECConstants.SESSION_USERTYPE;
		}
		String seqId = RequestHelper.getSeqId(request);
		logger.info("【QueryController】queryUserMessageByPDF()开始>>>seqId=" + seqId + ">>>timestamp=" + System.currentTimeMillis());
		String msgId = request.getParameter("msgId");
		String msgType = request.getParameter("msgType");
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		QueryMessageDto queryMessageDto = queryManager.queryUserMessageByPDF(context, cmfUserId, msgId, msgType);
		FundReportsPDFDto fundReportsPDFDto = new FundReportsPDFDto();
		String returnCode = "";
		String returnMsg = "";
		JSONObject jsonObject = new JSONObject();
		if (queryMessageDto != null) {
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			fundReportsPDFDto = (FundReportsPDFDto) queryMessageDto.getData();
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("【QueryController】queryUserMessageByPDF()获取调用后台的返回值>>>returnCode=" + returnCode + ">>>returnMsg=" + returnMsg + ">>>timestamp" + System.currentTimeMillis());
		if (fundReportsPDFDto != null) {
			try {
				SmartUpload smart = new SmartUpload();
				smart.service(request, response);
				smart.setContentDisposition(null);
				logger.info("--开始下载文件");
				String fileName = fundReportsPDFDto.getFileName();
				logger.info("【【【【【【【【【【[fileName-0]" + fileName + "】】】】】】】】】】");
				if ("2".equals(msgType) || "3".equals(msgType)) {
					fileName = fileName == null ? "产品文档.pdf" : fileName + ".pdf";
					if(fileName.indexOf(".pdf")<0 && fileName.indexOf(".mht")<0 && fileName.indexOf(".doc")<0
							&& fileName.indexOf(".PDF")<0 && fileName.indexOf(".html")<0
							&& fileName.indexOf(".txt")<0 && fileName.indexOf(".xls")<0){
						fileName = fileName + ".pdf";
					}
				} else if ("5".equals(msgType)) {
					fileName = fileName == null ? "产品文档.pdf" : fileName;
					if(fileName.indexOf(".pdf")<0 && fileName.indexOf(".mht")<0 && fileName.indexOf(".doc")<0
							&& fileName.indexOf(".PDF")<0 && fileName.indexOf(".html")<0
							&& fileName.indexOf(".txt")<0 && fileName.indexOf(".xls")<0){
						fileName = fileName + ".pdf";
					}
				}
				logger.info("【【【【【【【【【【[fileName-1]" + fileName + "】】】】】】】】】】");
				smart.downloadFile(fundReportsPDFDto.getContent(), "application/octet-stream", fileName);
			} catch (Exception e) {
				logger.info("--pdf文件下载失败");
				response.getOutputStream().print("下载失败，请稍后再试");
				e.printStackTrace();
			}
		} else {
			logger.info("--pdf文件不存在");
			response.getOutputStream().print("文件不存在");
		}
	}
	
	/**
	 * 查询首页banner
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/setUp/queryIndexBanner.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryIndexBanner(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
				cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

		String type = request.getParameter("type");
		logger.info("QueryController类【queryIndexBanner 】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());
		
		JSONObject jsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		
		QueryMessageDto queryMessageDto = queryManager.queryAdvert(context, type);
		List<AdvertDto> advertDtoList = null;
		if(queryMessageDto != null){
			advertDtoList = (List<AdvertDto>) queryMessageDto.getData();
		}
		if(advertDtoList != null && advertDtoList.size() > 0){
			jsonObject.put("advertDtoList", advertDtoList);
		}
		
		return jsonObject.toString();
		
	}
	
	/**
	 * 查询支持的在线支付的银行列表
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception String
	 * @author maj
	 */
	@RequestMapping(value = "/business/getSupportPayBankDesc.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String getSupportPayBankDesc(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String sessionId = RequestHelper.getSeqId(request);
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, "", cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		JSONObject jsonObject = queryManager.getSupportPayBankDesc(context);
		return jsonObject.toString();
	}
	
	/**
	 * 招行鉴权回调
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/afterCmbSign/gotoMyBanklist.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public void gotoMyBanklist(HttpServletResponse response, HttpServletRequest request) throws Exception {
		
		response.sendRedirect("/AppService/applicationGroups.shtml?mainCatId=MY_ACCOUNT&thirdCatId=ACCOUNT_BANK");
	}
	
	/**家庭住址
	 * 查询国籍-省份等地区信息 
	 * 联动
	 * @param context
	 * @param pmst
	 * @param pmky
	 * @param pmco
	 * @param pmv1
	 * @return
	 */
	@RequestMapping(value = "/business/queryParamList.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String queryParamList(HttpServletResponse response, HttpServletRequest request,
			@RequestParam(value = "paramType",required = false)String paramType,@RequestParam(value = "paramKey" , required = false)String paramKey,@RequestParam(value = "pmValueOne" ,required = false)String pmValueOne
			){
		QueryMessageDto queryParamList = queryManager.queryHomeAddressIsWordWithLinkage(new Context(), paramType, paramKey, null, pmValueOne);
		JSONObject returnObject = new JSONObject();
		returnObject = returnObject.fromObject(queryParamList);
		return returnObject.toString();
	};
	
	
	public static void main(String[] args) {
		UserInfoDto dto = new UserInfoDto();
		dto.setCustIdNo("123");
		JSONObject jsonObject = JSONObject.fromObject(dto);
		@SuppressWarnings("unchecked")
		Map<String, Object> map = jsonObject;
		System.out.println(map.get("custIdNo").toString());
		
	}
	
	@RequestMapping(value = "/article/queryInvestorArticle.xhtml")
	@ResponseBody
	public JSONObject queryInvestorArticle(HttpServletRequest request,HttpServletResponse reponse) throws Exception{
		String tempPage = request.getParameter("page");
		int page = 1;
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}
		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
			name = URLDecoder.decode(name, "utf-8");
		}		
		JSONObject jsonObject = new JSONObject();
		String cataLog = request.getParameter("cataLog");
		String sessionId = RequestHelper.getSeqId(request);
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		jsonObject = queryManager.queryEducationV2(context, cataLog, page, name);
		return jsonObject;	
	}
	
	@RequestMapping(value = "/article/queryInvestorArticleByFileType.xhtml")
	@ResponseBody
	public JSONObject queryInvestorArticleByFileType(HttpServletRequest request,HttpServletResponse response) throws Exception {
		String tempPage = request.getParameter("page");
		int page = 1;
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}
		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
			name = URLDecoder.decode(name, "utf-8");
		}		
		JSONObject jsonObject = new JSONObject();
		String fileType = request.getParameter("fileType");
		String sessionId = RequestHelper.getSeqId(request);
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		jsonObject = queryManager.queryInvestorArtivleByFileType(context, fileType, page, name);
		return jsonObject;	
	}
	@RequestMapping(value = "/article/queryMgmArticle.xhtml")
	@ResponseBody
	public JSONObject queryMgmArticle(HttpServletRequest request,HttpServletResponse reponse) throws Exception{
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String sessionId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		String tempPage = request.getParameter("page");
		String catalogId = request.getParameter("catalog_id");
		int page = 1;
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}

		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
		
		jsonObject = queryManager.queryMgmArticleAndCount(context, cmfUserId, page,catalogId);
		
		return jsonObject;
	}
	
	/**
	 * 查询白名单内产品净值表
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/setUp/queryInWhiteListFundNetValue.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String queryInWhiteListFundNetValue(HttpServletRequest request,HttpServletResponse response){
		JSONObject returnObject = new JSONObject();
		// 校验登陆
		UserBaseInfoDto userBaseInfo = (UserBaseInfoDto) request.getSession().getAttribute(SessionValue.SESSION_USERBASEINFO);
		String fundAcco = (String)request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String)request.getSession(true).getAttribute("crmCustNo");
		String branchName = (String)request.getSession(true).getAttribute("branchName");
		String paramOne = null;
		String paramTwo = null;
		String paramThree = null;
		int type = -1;
		if(userBaseInfo == null && StringUtils.isBlank(fundAcco) && StringUtils.isBlank(branchName) && StringUtils.isBlank(crmCustNo)) {
			returnObject.put("returnCode", "9999");
			returnObject.put("returnMsg", "登陆失效");
			return returnObject.toString();
		} else if (userBaseInfo != null) {
			paramOne = userBaseInfo.getCmfUserId();
			type = 1;
			paramThree = userBaseInfo.getCmfUserId();
		} else if (!StringUtils.isBlank(fundAcco) && !StringUtils.isBlank(branchName) && !StringUtils.isBlank(crmCustNo)) {
			paramOne = fundAcco;
			type = 0;
		}
		String fundId = request.getParameter("fundId");
		String temp = request.getParameter("page");
		int page = 0;
		try{
			page = Integer.parseInt(temp);
		} catch(NumberFormatException e){
			logger.debug("字符串转换数字时出现错误,"+e);
		}
		returnObject = queryManager.queryInWhiteListFundNetValue(paramOne,type,fundId,page);
		returnObject.put("loginType", type);
		return returnObject.toString();
	}

	/**
	 * 查询白名单内产品净值表走势图
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/setUp/queryInWhiteListFundNetValueByImgTable.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String queryInWhiteListFundNetValueByImgTable(HttpServletRequest request,HttpServletResponse response){
		JSONObject returnObject = new JSONObject();
		// 校验登陆
		UserBaseInfoDto userBaseInfo = (UserBaseInfoDto) request.getSession().getAttribute(SessionValue.SESSION_USERBASEINFO);
		String fundAcco = (String)request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String)request.getSession(true).getAttribute("crmCustNo");
		String branchName = (String)request.getSession(true).getAttribute("branchName");
		String paramOne = null;
		String paramTwo = null;
		String paramThree = null;
		int type = -1;
		if(userBaseInfo == null && StringUtils.isBlank(fundAcco) && StringUtils.isBlank(branchName) && StringUtils.isBlank(crmCustNo)) {
			returnObject.put("returnCode", "9999");
			returnObject.put("returnMsg", "登陆失效");
			return returnObject.toString();
		} else if (userBaseInfo != null) {
			paramOne = userBaseInfo.getCmfUserId();
			type = 1;
			paramThree = userBaseInfo.getCmfUserId();
		} else if (!StringUtils.isBlank(fundAcco) && !StringUtils.isBlank(branchName) && !StringUtils.isBlank(crmCustNo)) {
			paramOne = fundAcco;
			type = 0;
		}
		String fundId = request.getParameter("fundId");
		String temp = request.getParameter("dateTime");
		int dateTime = 0;
		try{
			dateTime = Integer.parseInt(temp);
		} catch(NumberFormatException e){
			logger.debug("字符串转换数字时出现错误,"+e);
		}
		returnObject = queryManager.queryInWhiteListFundNetValueByImgTable(paramOne,type,fundId,dateTime);
		returnObject.put("loginType", type);
		return returnObject.toString();
	}
	/**
	 * 原创文章查询
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/article/queryOriginalArticleAndCount.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryOriginalArticleAndCount(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		
		JSONObject jsonObject = new JSONObject();
		String tempPage = request.getParameter("page");
		int page = 1;
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}

		String name = request.getParameter("name");
		if(name != null && !name.equals("")){
			name = URLDecoder.decode(name, "utf-8");
		}
		
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		
		jsonObject = queryManager.queryOriginalArticleAndCount(context, cmfUserId, page, name);
		
		return jsonObject.toString();
	}
	
	/**
	 * 根据用户编号跟基金代码查询订单详情
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/business/queryTradeInfoByCustNo.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryTradeInfoByCustNo(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		String tradeAcco = request.getParameter("tradeAcco");
		String fundid = request.getParameter("fundid");
		String subquty = request.getParameter("subquty");
		logger.info("QueryController类【queryTradeInfoByCustNo 】开始>>>fundid:" + fundid + ">>>tradeAcco:" + tradeAcco+ ">>>subquty:"+ subquty +">>>timestamp:" + System.currentTimeMillis());
		JSONObject jsonObject = queryManager.queryTradeInfoByCustNo(context, tradeAcco, fundid,subquty);
		return jsonObject.toString();
	}

	/**
	 * 查询指定页数展示的官网债券投资人员公示信息
	 *
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
    @RequestMapping(value = "/article/queryBondInvestorArticle.xhtml", produces = "text/html;charset=UTF-8",
        method = {RequestMethod.POST})
    @ResponseBody
    public String queryBondInvestorArticle(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        String tempPage = request.getParameter("page");
        JSONObject jsonObject = new JSONObject();

        int page = 1;
        if (null != tempPage && !"".equals(tempPage)) {
            page = Integer.parseInt(tempPage);
        }

        String name = request.getParameter("name");
        if (name != null && !name.equals("")) {
            name = URLDecoder.decode(name, "utf-8");
        }

        Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
			cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        jsonObject = queryManager.queryBondInvestorArticleAndCount(context, cmfUserId, page, name);

        return jsonObject.toString();
    }
	
	/**
	 * 根据产品id来查询可赎回的份额及银行卡信息
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/business/queryCanRedeemBankInfoByFundCode.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String queryCanRedeemBankInfoByFundCode(HttpServletResponse response, HttpServletRequest request) throws Exception {
		JSONObject obj = new JSONObject();
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		// 日志信息
		String fundid = request.getParameter("fundCode");
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
		if(userAccoRla == null) {
			obj.put("returnCode", "9999");
			obj.put("returnMsg", "关键参数丢失");
			logger.info("queryCanRedeemBankInfoByFundCode -> session内未获取到userAccoRla记录");
			return obj.toString();
		}
		String custno = userAccoRla.getEcCustNo();
		logger.info("queryCanRedeemBankInfoByFundCode -> 获取到参数 cmfuserid:"+cmfUserId+",seqId:"+seqId+",fundid:"+fundid+",custno:"+custno+"");
		obj = queryManager.queryCanRedeemBankInfoByFundCode(fundid,custno);
		return obj.toString();
	}


	/**
	 * 根据身份证查询合格投资者信息
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/business/queryQualifiedUserInfoByIdno.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String queryQualifiedUserInfoByIdno(HttpServletResponse response, HttpServletRequest request) throws Exception {
		UserBaseInfoDto sessionUserInfo = (UserBaseInfoDto) request.getSession().getAttribute(SessionValue.SESSION_USERBASEINFO);
		JSONObject returnObj = new JSONObject();
		if(sessionUserInfo == null) {
			returnObj.put("returnCode","9999");
			returnObj.put("returnMsg","请重新登陆");
			return returnObj.toString();
		}
		if(StringUtils.isBlank(sessionUserInfo.getIdNo())) {
			returnObj.put("returnCode","9999");
			returnObj.put("returnMsg","请实名之后再进行操作");
			return returnObj.toString();
		}
		return queryManager.queryQualifiedUserInfoByIdno(sessionUserInfo.getIdNo()).toString();
	}
	
	/**
	 * 根据身份证查询合格投资者信息
	 * @param response
	 * @param request
	 */
	@RequestMapping(value = "/business/queryFileUploadRecord.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String queryFileUploadRecord(HttpServletResponse response, HttpServletRequest request) throws Exception {
		UserBaseInfoDto sessionUserInfo = (UserBaseInfoDto) request.getSession().getAttribute(SessionValue.SESSION_USERBASEINFO);
		JSONObject returnObj = new JSONObject();
		if(sessionUserInfo == null) {
			returnObj.put("returnCode","9999");
			returnObj.put("returnMsg","请重新登陆");
			return returnObj.toString();
		}
		return queryManager.queryFileUploadRecord(sessionUserInfo.getCmfUserId()).toString();
	}
	
	
	@RequestMapping(value = "/business/queryAccreditedInvestorConditions.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public String queryAccreditedInvestorConditions(HttpServletRequest request,HttpServletResponse response) {
		UserAccoRlaDto sessionUserInfo = (UserAccoRlaDto) request.getSession().getAttribute(SessionValue.SESSION_USERACCORLA);
		if(sessionUserInfo == null) {
			JSONObject returnObj = new JSONObject();
			returnObj.put("returnCode",ECConstants.RETURN_CODE_9005);
			returnObj.put("returnMsg","请进行实名验证");
			return returnObj.toString();
		}
		return queryManager.queryAccreditedInvestorConditions(sessionUserInfo.getCmfUserId(),sessionUserInfo.getEcCustNo()).toString();
	}
	/**
	 * 获取当前用户的顾问信息
	 *
	 * @param response
	 * @param request
	 * @return json
	 */
	@RequestMapping(value = "/business/getConsultantInfo.xhtml", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
	@ResponseBody
	String getConsultantInfo(HttpServletResponse response, HttpServletRequest request) {
		JSONObject result = ECConstants.ERR_JSON_RESULT;
		String sessionCmfUserId = RequestHelper.getSessionCmfUserId(request);
		if (StringUtils.isNotBlank(sessionCmfUserId)) {
			CustserviceInfoDTO consultantInfo = queryManager.getConsultantInfo(sessionCmfUserId);
			if (null != consultantInfo) {
				result = new JSONObject().accumulate("consultantInfo", consultantInfo)
					.accumulate(ECConstants.ERR_CODE, ECConstants.COMMON_SUCCESS);
			}
		}
		return result.toString();
	}
	
	/**
	 *  查询风险揭示函条款列表
	 * @param response
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/business/queryRiskTermList.xhtml", produces = "text/html;charset=UTF-8", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	String queryRiskTermList(HttpServletResponse response, HttpServletRequest request) {
		JSONObject result = ECConstants.ERR_JSON_RESULT;
		String sessionCmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, sessionCmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		if (StringUtils.isNotBlank(sessionCmfUserId)) {
			String fundId = request.getParameter("fundId");
			String period = request.getParameter("period");
			result = queryManager.queryRiskTermList(context, sessionCmfUserId, fundId, period);
		}
		return result.toString();
	}
	
}