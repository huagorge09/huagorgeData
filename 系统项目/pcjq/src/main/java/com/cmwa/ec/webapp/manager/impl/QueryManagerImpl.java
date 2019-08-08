package com.cmwa.ec.webapp.manager.impl;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.cmwa.ec.query.facade.model.consultant.CustserviceInfoDTO;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ec.query.facade.dto.account.AgentFundDto;
import com.cmwa.ec.query.facade.dto.account.BalanceBillDto;
import com.cmwa.ec.query.facade.dto.account.TotalBillInfoDto;
import com.cmwa.ec.query.facade.dto.account.TradeAcctDto;
import com.cmwa.ec.query.facade.dto.account.TradeAcctInfoDto;
import com.cmwa.ec.query.facade.dto.channel.ChannelInfoDto;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.fund.FeeRateDto;
import com.cmwa.ec.query.facade.dto.fund.FundInfoDtoV2;
import com.cmwa.ec.query.facade.dto.fund.FundReportsDto;
import com.cmwa.ec.query.facade.dto.fund.FundReportsPDFDto;
import com.cmwa.ec.query.facade.dto.fund.FundTradeInfoDto;
import com.cmwa.ec.query.facade.dto.system.ParameterDto;
import com.cmwa.ec.query.facade.dto.trade.AppointRequestDto;
import com.cmwa.ec.query.facade.dto.trade.RedemmBalanceDto;
import com.cmwa.ec.query.facade.dto.user.CmwaQualifiedUserInfoDto;
import com.cmwa.ec.query.facade.dto.user.CmwaQualifiedUserInfoStatusDto;
import com.cmwa.ec.query.facade.dto.user.QualifiedUserInfoDto;
import com.cmwa.ec.query.facade.dto.user.RiskTermsDto;
import com.cmwa.ec.query.facade.dto.user.UserTermsDto;
import com.cmwa.ec.query.facade.dto.web.ArticleDtoV2;
import com.cmwa.ec.query.facade.dto.web.CampusTalkDto;
import com.cmwa.ec.query.facade.dto.web.ConsignmentDto;
import com.cmwa.ec.query.facade.dto.web.CooperationDto;
import com.cmwa.ec.query.facade.dto.web.FileDownloadCenterDto;
import com.cmwa.ec.query.facade.dto.web.InvestorFileDto;
import com.cmwa.ec.query.facade.dto.web.PlaceDto;
import com.cmwa.ec.query.facade.dto.web.PositionTypeDto;
import com.cmwa.ec.query.facade.dto.web.QuestionDto;
import com.cmwa.ec.query.facade.dto.web.RecruitmentDto;
import com.cmwa.ec.query.facade.dto.web.SchoolDto;
import com.cmwa.ec.trade.facade.dto.log.CmwaFileUploadRecordDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.webapp.client.QueryServiceClient;
import com.cmwa.ec.webapp.client.TradeServiceClient;
import com.cmwa.ec.webapp.client.UserServiceClient;
import com.cmwa.ec.webapp.manager.QueryManager;
import com.cmwa.ec.webapp.util.DateUtils;
import com.cmwa.ec.webapp.util.ECConstants;
import com.cmwa.ec.webapp.util.RequestHelper;
import com.cmwa.ec.webapp.util.SessionValue;

public class QueryManagerImpl implements QueryManager{
	private static Logger logger = Logger.getLogger(QueryManagerImpl.class.getName());
	
	@Autowired
	private QueryServiceClient queryServiceClient;
	
	@Autowired
	private UserServiceClient userServiceClient;

	@Autowired
	private TradeServiceClient tradeServiceClient;
	
	@Override
	public QueryMessageDto queryFundContractById(Context context, String fundId,String period) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryFundContractByIdWithPeirod(context, fundId,period);
		return queryMessageDto;
	}
	@Override
	public QueryMessageDto queryFundContractById(Context context, String fundId) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryFundContractById(context, fundId);
		return queryMessageDto;
	}

	@Override
	public QueryMessageDto queryUserTradeAcctInfoList(Context context, String ecCustNo, String fundid) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryUserTradeAcctInfoList(context, ecCustNo, fundid);
		return queryMessageDto;
	}

	/*@Override
	public QueryMessageDto queryFundsAndQuestion(Context context, String fundid, String fundname, String string) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryFundsAndQuestion(context, fundid,fundname,string);
		return queryMessageDto;
	}*/

	@Override
	public QueryMessageDto queryFund(Context context, String fundId,int period) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryFund(context, fundId,period);
		return queryMessageDto;
	}

	@Override
	public QueryMessageDto queryHotFundList(Context context) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryHotFundList(context);
		List<FundInfoDtoV2> hotFundList = new ArrayList<FundInfoDtoV2>();
		if(queryMessageDto != null){
			hotFundList = (List<FundInfoDtoV2>) queryMessageDto.getData();
			if(hotFundList != null){
				queryMessageDto.setData(hotFundList.get(0));
			}
		}
		return queryMessageDto;
	}

	@Override
	public QueryMessageDto queryFundList(Context context) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryFundList(context);
		List<FundInfoDtoV2> list = new ArrayList<FundInfoDtoV2>();
		
		String currentWorkdate = "";
		
		if(queryMessageDto != null){
			list = (List<FundInfoDtoV2>) queryMessageDto.getData();
		}
		
		if(list != null && list.size() > 0){// 获取当前工作日
			currentWorkdate = list.get(0).getCurrentWorkdate();
		}else{
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			currentWorkdate = df.format(new Date());
		}
		
		List<FundInfoDtoV2> tempList = new ArrayList<FundInfoDtoV2>();
		
		List<String> keyList = new ArrayList<String>();
		Map<String, List<FundInfoDtoV2>> map = new HashMap<String, List<FundInfoDtoV2>>();
		
		
		for (int i = 0; i < list.size(); i++) {// 取出所有的在售期的产品
			if(StringUtils.isEmpty(list.get(i).getDisplayLimit()) || "0".equals(list.get(i).getDisplayLimit())){//过滤掉线上展示额度为0的产品
				list.remove(i);
				i--;
				continue;
			}
			if(ECConstants.FUND_TYPE_0500.equals(list.get(i).getTypeId())){//活期理财产品不走传统方法
				tempList.add(list.get(i));
				continue;
			}
			if(Long.parseLong(list.get(i).getSubdeadLine().replaceAll("-", "")) >= Long.parseLong(currentWorkdate.replaceAll("-", "")) 
					&& Long.parseLong(list.get(i).getDisplayLimit()) > 0){
				tempList.add(list.get(i));
			}
		}
		
		for(int i = 0;i < tempList.size();i++){// 取出所有在售产品的groupId,将其放入keyList中  后面作为map的key，已保证map的顺序
			if(!keyList.contains(tempList.get(i).getGroupId())){
				keyList.add(tempList.get(i).getGroupId());
			}
		}
		
		for (int i = 0; i < list.size(); i++) {// 将所有查询出的产品groupId放入到keyList中,确保产品的正确排序顺序以及产品系列的正确排序顺序   后面作为map的key，已保证map的顺序
			if(!keyList.contains(list.get(i).getGroupId())){
				keyList.add(list.get(i).getGroupId());
			}
		}
		
		for (int i = 0; i < keyList.size(); i++) {// 将keyList中的值作为map的key，new ArrayList作为value存放到map中
			map.put(keyList.get(i), new ArrayList<FundInfoDtoV2>());
		}
		
		for (int i = 0; i < tempList.size(); i++) {// 将在售产品按groupId分组存放到map中
				map.get(tempList.get(i).getGroupId()).add(tempList.get(i));
		}
		
		// 若某一产品系列没有或不够2个产品产品，补齐最新的2个产品
		for (String key : map.keySet()) {
			if(map.get(key).size() == 0){
				int tempCount = 0;
				for (int i = 0; i < list.size(); i++) {
					if(tempCount < 2){// 补齐2个产品
						if(list.get(i).getGroupId().equals(key)){
							map.get(key).add(list.get(i));
							tempCount ++;
						}
					}else {
						continue;
					}
				}
			}else if(map.get(key).size() == 1){
				int tempCount = 0;
				for (int i = 0; i < list.size(); i++) {
					if(tempCount < 1){// 补齐2个产品
						if(list.get(i).getGroupId().equals(key) && !list.get(i).getFundId().equals(map.get(key).get(0).getFundId())){
							map.get(key).add(list.get(i));
							tempCount ++;
						}else{
							continue;
						}
					}
				}
			}
		}
		
		List<FundInfoDtoV2> returnList = new ArrayList<FundInfoDtoV2>();
		
		for (int i = 0; i < keyList.size(); i++) {
			returnList.addAll(map.get(keyList.get(i)));
		}
		
		queryMessageDto.setData(returnList);
		return queryMessageDto;
	}
	
	@Override
	public QueryMessageDto queryFeeRateList(Context context, String fundId, List<String> channelNoList, String custLevel) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryFeeRateList(context, fundId, channelNoList, custLevel);
		return queryMessageDto;
	}

	@Override
	public QueryMessageDto queryEstimateByFundId(Context context, String fundId,Integer dateTime) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryEstimateByFundId(context, fundId,dateTime);
		return queryMessageDto;
	}

	@Override
	public QueryMessageDto queryFundElementList(Context context, String fundId) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryFundElementList(context,fundId);
		return queryMessageDto;
	}

	@Override
	public QueryMessageDto queryTradeInfoByTradeNo(Context context, String custno, String tradeNo,int period) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryTradeInfoByTradeNo(context,custno, tradeNo,period);
		return queryMessageDto;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public JSONObject queryTradeInfoByCustNo(Context context, String custno, String fundid,String subquty) {
		JSONObject json = new JSONObject();
		StringBuilder stringBuilder = new StringBuilder();
		try {
			logger.info("QueryManagerImpl类【queryTradeInfoByCustNo】开始>>>>>>timestamp:" + System.currentTimeMillis());
			if(StringUtils.isEmpty(subquty) || StringUtils.isEmpty(custno) || StringUtils.isEmpty(fundid)){
				json.put("resultCode", "9999");
				json.put("resultMsg", "关键参数为空");
			}
			QueryMessageDto queryMessageDto = queryServiceClient.queryTradeInfoByCustNo(context,custno, fundid);
			if("0000".equals(queryMessageDto.getResultCode())){
				List<AppointRequestDto> resultDto =(List<AppointRequestDto>)queryMessageDto.getData();
				double sum = 0;
				for(int i=0;i<resultDto.size();i++){
					sum += Double.valueOf(resultDto.get(i).getSubquty());
					stringBuilder.append(resultDto.get(i).getSerialno());
					if(sum >= Double.valueOf(subquty.replace(",","")).doubleValue()){
						break;
					}
					stringBuilder.append(",");
				}
				AppointRequestDto dto = resultDto.get(0);
				dto.setSerialno(stringBuilder.toString());
				json.put("data",dto);
			}
			json.put("resultCode", "0000");
			json.put("resultMsg", "成功！");
		} catch (Exception e) {
			logger.error("QueryManagerImpl类【queryTradeInfoByCustNo】异常>>>>>>异常信息:" + e);
			json.put("resultCode", "500");
			json.put("resultMsg", "系统异常");
		}
		return json;
	}
	/*================以上是1.0版本代码=================*/
	// TODO 以上是1.0版本代码
	@Override
	public JSONObject getSupportBankDesc(Context context) {
		logger.info("QueryManagerImpl类【getSupportBankDesc】开始>>>>>>timestamp:" + System.currentTimeMillis());
		String returnCode = null;
		String returnMsg = null;
		JSONObject returnObject = new JSONObject();

		QueryMessageDto message = queryServiceClient.getSupportBankDesc(context);

		logger.info("QueryManagerImpl类【getSupportBankDesc】调用 ---查询支持的银行卡列表--end ---  接口");
		String bankListStr = null;
		if (message != null) {
			returnCode = message.getResultCode();
			returnMsg = message.getResultMsg();
			logger.info("returnCode:" + returnCode + ",  returnMsg:" + returnMsg);

			if ("0000".equals(returnCode)) {
				bankListStr = (String) message.getData();
			}
		} else {
			returnCode = ECConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = ECConstants.COMMON_ERROR_SYSERRMSG;
		}

		returnObject.put("returnCode", returnCode);
		returnObject.put("returnMsg", returnMsg);
		returnObject.put("bankListStr", bankListStr);

		logger.info("QueryManagerImpl类【getSupportBankDesc】结束>>>>>returnObject:"+returnObject.toString()+">timestamp:" + System.currentTimeMillis());
		return returnObject;
	}
	/**
	 * /** 根据银行卡号查找该卡号的具体银行信息 即卡号校验
	 * 
	 * @param context
	 * @param bankCardNo
	 * @return
	 */
	@Override
	public JSONObject queryBankInfoByBankNumber(Context context, String bankCardNo) {
		logger.info("QueryManagerImpl类【queryBankInfoByBankNumber】开始>>>bankCardNo:"+bankCardNo+">>>timestamp:" + System.currentTimeMillis());
		JSONObject returnJson = new JSONObject();
		String returnCode = null;
		String returnMsg = null;
		String listIsNull = "yes";
		String status = "N";
		ChannelInfoDto channelInfoDto = null;
		QueryMessageDto queryMessageDto = queryServiceClient.queryBankInfoByBankNumber(context, bankCardNo);

		if (queryMessageDto != null) {
			logger.info("QueryManagerImpl类【queryBankInfoByBankNumber】银行卡号校验，查询卡号具体信息--returnCode：" + queryMessageDto.getResultCode() + ", returnMsg:" + queryMessageDto.getResultMsg());
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			if ("0000".equals(queryMessageDto.getResultCode())) {
				Object obj = queryMessageDto.getData();
				if (obj != null) {
					List<ChannelInfoDto> list = (List<ChannelInfoDto>) obj;
					// list为空，则说明银行卡号输入不正确
					if (list.size() > 0) {
						logger.info("list.size():" + list.size());
						listIsNull = "no";
						boolean bool = true;
						ChannelInfoDto temp = null;
						// 如果列表不为空，则判断list中dto的status，
						// 如果有状态为Y的，则取第一个sataus为Y的dto，表示验证成功
						// 如果没有状态为Y的，则表示是不支持的银行卡号
						for (int i = 0; i < list.size() && bool; i++) {
							temp = list.get(i);
							if (temp != null && temp.getStatus().equals("Y")) {
								status = "Y";
								channelInfoDto = temp;
								logger.info(channelInfoDto.toString());
								bool = false;
							}
						}
						if (status.equals("N")) {
							returnMsg = "抱歉，暂不支持此银行绑卡业务";
						}
					} else {
						returnMsg = "请您输入正确的银行卡号";
					}
				} else {
					returnMsg = "请您输入正确的银行卡号";
				}
			}
		} else {
			returnCode = "-1";
		}
		returnJson.put("status", status);
		returnJson.put("listIsNull", listIsNull);
		returnJson.put("returnCode", returnCode);
		returnJson.put("returnMsg", returnMsg);
		returnJson.put("bankInfoDto", channelInfoDto);
		logger.info("QueryManagerImpl类【queryBankInfoByBankNumber】结束>>>returnJson:"+returnJson+">>>timestamp:" + System.currentTimeMillis());
		return returnJson;
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
	@Override
	public JSONObject getTradeAcctInfoByCustno(Context context,String cmfUserId, HttpServletRequest request) {
		logger.info("QueryManagerImpl类【getTradeAcctInfoByCustno】开始>>timestamp:" + System.currentTimeMillis());
		JSONObject returnJsonObject = new JSONObject();
		String fundid = request.getParameter("fundid");
		UserBaseInfoDto userBaseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		String ecCustNo = "";
		String userType = null;
		if (userBaseInfoDto == null) {
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_8000);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_8000);
			return returnJsonObject;
		} else{
			if (userAccoRla != null) {
				ecCustNo = userAccoRla.getEcCustNo();
			}
			if(StringUtil.isEmpty(ecCustNo)){
				logger.info("查询userAccoRla，getTradeAcctInfoByCustno 1  ecCustNo=" + ecCustNo);
				UserServiceMessage usermessage = userServiceClient.queryUserAndAccoRlaById(context, cmfUserId);
				userBaseInfoDto = usermessage.getUserBaseInfoDto();
				if(usermessage.getUserAccoRlaDto()!=null){
					logger.info("查询userAccoRla，getTradeAcctInfoByCustno 2  ecCustNo=" + ecCustNo);
					userAccoRla = usermessage.getUserAccoRlaDto();
					ecCustNo = userAccoRla.getEcCustNo();
					
					if(StringUtil.isEmpty(ecCustNo)){
						logger.info("缺少关键参数--ecCustNo客户编号");
						returnJsonObject.put("returnCode", 9000);
						returnJsonObject.put("returnMsg", "缺少关键参数--ecCustNo客户编号");
						return returnJsonObject;
					}
					
					request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userAccoRla);
				}else{
					returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_9005);
					returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_9005);
					return returnJsonObject;
				}
				logger.info("查询userAccoRla，getTradeAcctInfoByCustno 3  ecCustNo=" + ecCustNo);
			}
			userType = userBaseInfoDto.getUserType();
		}
		List<TradeAcctInfoDto> tradeAcctDtoList = null;
		String returnCode = null;
		String returnMsg = null;
		QueryMessageDto messageDto = queryServiceClient.queryUserTradeAcctInfoList(context, ecCustNo, fundid);
		if (messageDto != null) {
			returnCode = messageDto.getResultCode();
			returnMsg = messageDto.getResultMsg();
			logger.info("查询银行卡号列表，returnCode=" + returnCode);
			if (returnCode != null && ECConstants.COMMON_SUCCESS.equals(returnCode)) {
				tradeAcctDtoList = (List<TradeAcctInfoDto>) messageDto.getData();
				List<TradeAcctInfoDto> encryptList = new ArrayList<TradeAcctInfoDto>();
				for (TradeAcctInfoDto tradeAcctInfoDto : tradeAcctDtoList) {
					tradeAcctInfoDto.setBankAccoDisplay(RequestHelper.getEncryptBankCard(tradeAcctInfoDto.getBankAccoDisplay()));
					tradeAcctInfoDto.setMobile(RequestHelper.getEncryptMobile(tradeAcctInfoDto.getMobile()));
					encryptList.add(tradeAcctInfoDto);
				}
				returnJsonObject.put("tradeAcctlist", encryptList);
			}
		} else {
			returnCode = ECConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = ECConstants.COMMON_ERROR_SYSERRMSG;
		}

		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		returnJsonObject.put("userType", userType);
		logger.info("QueryManagerImpl类【getTradeAcctInfoByCustno】结束>>>returnCode:" + returnCode + ">>>>returnMsg:" + returnMsg + ">>>>>timestamp:" + System.currentTimeMillis());
		return returnJsonObject;
	}
	/**
	 * 查询信批列表 
	 * @param context
	 * @param cmfUserId
	 * @param beginIdx 开始条数 哪一条开始   第1条为0
	 * @param amount 每页展示条数 
	 * @param totalAmount 总记录条数
	 * @return
	 * 			JSONObject
	 * @author luos
	 */
	@Override
	public JSONObject queryUserMessageList(Context context, String cmfUserId, int beginIdx, int amount, int totalAmount) {
		logger.info("QueryManagerImpl类【queryUserMessageList】开始>>cmfUserId:"+cmfUserId+">>beginIdx:"+beginIdx+">>amount:"+amount+">>>>>totalAmount:"+totalAmount+">>>>timestamp:" + System.currentTimeMillis());
		QueryMessageDto queryMessageDto = queryServiceClient.queryUserMessageList(context, cmfUserId, beginIdx, amount, totalAmount);
		List<FundReportsDto> list = new ArrayList<FundReportsDto>();
		String returnCode = "";
		String returnMsg = "";
		Integer totalNum = 0;
		JSONObject jsonObject = new JSONObject();
		if (queryMessageDto != null) {
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			list = (List<FundReportsDto>) queryMessageDto.getData();
			totalNum = NumberUtils.stringToInt(queryMessageDto.getOtherData() == null ? "0" : queryMessageDto.getOtherData().toString(), 0);
		}
		int maxPages = totalNum % amount == 0 ? totalNum / amount : totalNum / amount + 1;
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		jsonObject.put("totalAmount", totalNum);
		jsonObject.put("maxPages", maxPages);
		if (list != null) {
			jsonObject.put("list", list);
		}
		logger.info("QueryManagerImpl类【queryUserMessageList】结束>>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}
	/**
	 * 查询信批详情
	 * @param context
	 * @param cmfUserId
	 * @param msgId
	 * @param msgType
	 * @return
	 * 			JSONObject
	 * @author luos
	 */
	@Override
	public JSONObject queryUserMessage(Context context, String cmfUserId, String msgId, String msgType) {
		logger.info("QueryManagerImpl类【queryUserMessage】开始>>cmfUserId:"+cmfUserId+">>msgId:"+msgId+">>msgType:"+msgType+">>>>timestamp:" + System.currentTimeMillis());
		QueryMessageDto queryMessageDto = queryServiceClient.queryUserMessage(context, cmfUserId, msgId, msgType);
		List<FundReportsDto> list = new ArrayList<FundReportsDto>();
		FundReportsPDFDto fundReportsPDFDto = new FundReportsPDFDto();
		String returnCode = "";
		String returnMsg = "";
		JSONObject jsonObject = new JSONObject();
		if (queryMessageDto != null) {
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();

			if (msgType != null && msgType.equals("1")) {// html类型
				list = (List<FundReportsDto>) queryMessageDto.getData();
				if (list != null && list.size() > 0) {
					jsonObject.put("fundReportsDto", list.get(0));
				}
			} else {// pdf类型
				fundReportsPDFDto = (FundReportsPDFDto) queryMessageDto.getData();
				if (fundReportsPDFDto != null) {
					fundReportsPDFDto.setContent(null);// 将附件内容置空(里面为pdf文件的数据)，否则返回到前端比较大
					jsonObject.put("fundReportsPDFDto", fundReportsPDFDto);
				}
			}
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("QueryManagerImpl类【queryUserMessage】结束>>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}
	/**
	 * 查询信批详情 PDF
	 * @param context
	 * @param cmfUserId
	 * @param msgId
	 * @param msgType
	 * @return
	 * 			JSONObject
	 * @author luos
	 */
	@Override
	public QueryMessageDto queryUserMessageByPDF(Context context, String cmfUserId, String msgId, String msgType) {
		logger.info("QueryManagerImpl类【queryUserMessageByPDF】开始>>>msgId:"+msgId+">>>msgType:"+msgType+">>>timestamp:" + System.currentTimeMillis());
		QueryMessageDto queryMessageDto = queryServiceClient.queryUserMessageByPDF(context, cmfUserId, msgId, msgType);
		logger.info("QueryManagerImpl类【queryUserMessageByPDF】结束>>>timestamp:" + System.currentTimeMillis());
		return queryMessageDto;
	}
	@Override
	public JSONObject queryUnReadFundReportsCount(Context context, String cmfUserId) {
		logger.info("QueryManagerImpl类【queryUnReadFundReportsCount】开始>>>cmfUserId:"+cmfUserId+">>>timestamp:" + System.currentTimeMillis());
		QueryMessageDto message = queryServiceClient.queryUnReadFundReportsCount(context, cmfUserId);
		Integer count = 0;
		String returnCode = "";
		String returnMsg = "";
		JSONObject countJson = new JSONObject();
		if (message != null) {
			returnCode = message.getResultCode();
			returnMsg = message.getResultMsg();
			count = (Integer) message.getData();
		}
		countJson.put("returnCode", returnCode);
		countJson.put("returnMsg", returnMsg);
		countJson.put("count", count);
		logger.info("QueryManagerImpl类【queryUnReadFundReportsCount】结束>>>countJson:"+countJson+">>>timestamp:" + System.currentTimeMillis());
		return countJson;
	}
	/**
	 * 查询历史交易信息   对账单
	 * @param context
	 * @param cmfUserId
	 * @param startDate
	 * @param endDate
	 * @return
	 * 			JSONObject
	 * @author luos
	 */
	@Override
	public JSONObject queryFundTradeInfo(Context context, String cmfUserId, String startDate, String endDate) {
		logger.info("QueryManagerImpl类【queryFundTradeInfo】开始>>>cmfUserId:"+cmfUserId+">>>startDate:"+startDate+">>>timestamp:" + System.currentTimeMillis());
		QueryMessageDto queryMessageDto = queryServiceClient.queryFundTradeInfo(context, cmfUserId, startDate, endDate);
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		List<FundTradeInfoDto> list = new ArrayList<FundTradeInfoDto>();
		if (queryMessageDto != null) {
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			list = (List<FundTradeInfoDto>) queryMessageDto.getData();
		}
		if (list != null && list.size() > 0) {
			jsonObject.put("list", list);
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("QueryManagerImpl类【queryFundTradeInfo】结束>>>jsonObject:"+jsonObject+">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}
	/**
	 * 查询用户订单列表
	 * @param context
	 * @param custno
	 * @return
	 * 			JSONObject
	 * @author luos
	 */
	@Override
	public JSONObject queryTradeInfoList(Context context, String custno, String fundid, String applyst) {
		logger.info("QueryManagerImpl类【queryFundTradeInfo】开始>>>custno:"+custno);
		QueryMessageDto queryMessageDto = queryServiceClient.queryTradeInfoList(context, custno, fundid, applyst);
		JSONObject returnJsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		List<AppointRequestDto> list = new ArrayList<AppointRequestDto>();// 全部订单
		List<AppointRequestDto> uList = new ArrayList<AppointRequestDto>();// 未支付订单-待确认/排队中/待付款
		List<AppointRequestDto> pList = new ArrayList<AppointRequestDto>();// 已支付订单
		List<AppointRequestDto> gList = new ArrayList<AppointRequestDto>();// 存续中订单
		List<AppointRequestDto> zList = new ArrayList<AppointRequestDto>();// 已到期订单
		if (queryMessageDto != null) {
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			list = (List<AppointRequestDto>) queryMessageDto.getData();
		}
		List<TradeAcctDto> tradeList = null;
		List<TradeAcctDto> encryptTradeList = new ArrayList<TradeAcctDto>();
		if (list != null && list.size() > 0) {
			for (AppointRequestDto appointRequestDto : list) {
				QueryMessageDto tradeMessage = null;
				if (appointRequestDto != null) {
					// 根据tradeAcco获取交易账号
					tradeMessage = getTradeAcctInfoByTradeAcco(context, appointRequestDto.getTradeacco());
				}
				if (tradeMessage != null) {	
					tradeList = (List<TradeAcctDto>) tradeMessage.getData();
				}
				if (appointRequestDto.getOrderGroup() != null && appointRequestDto.getOrderGroup().equals("U")) {
					uList.add(appointRequestDto);
				} else if (appointRequestDto.getOrderGroup() != null && appointRequestDto.getOrderGroup().equals("P")) {
					pList.add(appointRequestDto);
				} else if (appointRequestDto.getOrderGroup() != null && appointRequestDto.getOrderGroup().equals("G")) {
					gList.add(appointRequestDto);
				} else if (appointRequestDto.getOrderGroup() != null && appointRequestDto.getOrderGroup().equals("Z")) {
					zList.add(appointRequestDto);
				}
			}
		}
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		if (tradeList != null && tradeList.size() > 0) {
			for (TradeAcctDto tradeAcctDto : tradeList) {
				tradeAcctDto.setBankacco(RequestHelper.getEncryptBankCard(tradeAcctDto.getBankacco()));
				tradeAcctDto.setBankaccodisplay(RequestHelper.getEncryptBankCard(tradeAcctDto.getBankaccodisplay()));
				tradeAcctDto.setIdno(RequestHelper.getEncryptIdNo(tradeAcctDto.getIdno()));
				tradeAcctDto.setMobile(RequestHelper.getEncryptMobile(tradeAcctDto.getIdno()));
				tradeAcctDto.setBankacnm(RequestHelper.getEncryptUserName(tradeAcctDto.getBankacnm()));
				encryptTradeList.add(tradeAcctDto);
			}
			returnJsonObject.put("tradeList", encryptTradeList);
		}
		if (list != null && list.size() > 0) {
			returnJsonObject.put("list", list);
		}
		if (uList != null && uList.size() > 0) {
			returnJsonObject.put("uList", uList);
		}
		if (pList != null && pList.size() > 0) {
			returnJsonObject.put("pList", pList);
		}
		if (gList != null && gList.size() > 0) {
			returnJsonObject.put("gList", gList);
		}
		if (zList != null && zList.size() > 0) {
			returnJsonObject.put("zList", zList);
		}
		logger.info("QueryManagerImpl类【queryFundTradeInfo】结束>>>>timestamp:" + System.currentTimeMillis());
		return returnJsonObject;
	}
	
	@Override
	public JSONObject queryCustTradeInfo(Context context, String cmfUserId,
			String fundCode ,String tradeAcco) {
		logger.info("QueryManagerImpl类【queryCustTradeInfo】开始>>>cmfUserId:"+cmfUserId + ",fundCode:"+fundCode+",tradeAcco:"+tradeAcco);
		QueryMessageDto queryMessageDto = queryServiceClient.queryCustTradeInfo(context,cmfUserId,fundCode,tradeAcco);
		JSONObject returnJsonObject = new JSONObject();
		BalanceBillDto dto = null;
		String returnCode = "";
		String returnMsg = "";
		if (queryMessageDto != null) {
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			dto = (BalanceBillDto) queryMessageDto.getData();
		}
		if(null != dto && !"0".equals(dto.getBalance())){
			returnJsonObject.put("buyState","Y");
		}else{
			returnJsonObject.put("buyState","N");
		}
		returnJsonObject.put("data", dto);
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		return returnJsonObject;
	}
	@Override
	public JSONObject queryNewCustomer(Context context, String cmfUserId,
			String fundCode) {
		logger.info("queryNewCustomer类【queryNewCustomer】开始>>>cmfUserId:"+cmfUserId + ",fundCode:"+fundCode);
		QueryMessageDto queryMessageDto = queryServiceClient.queryNewCustomer(context,cmfUserId,fundCode);
		JSONObject returnJsonObject = new JSONObject();
		Integer number = (Integer)queryMessageDto.getData();
		returnJsonObject.put("number", number);
		returnJsonObject.put("returnCode", queryMessageDto.getResultCode());
		returnJsonObject.put("returnMsg", queryMessageDto.getResultMsg());
		return returnJsonObject;
	}
	/**
	 * 查询用户的总资产、总收益
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param source
	 *            报文来源 
	 * @return JSONObject 
	 */
	@Override
	public JSONObject queryTotalFundBalance(Context context, String cmfUserId, UserBaseInfoDto userBaseInfo, String sessionId,UserAccoRlaDto accoRlaDto) {
		QueryMessageDto totalFundBalanceMessage = null;
		TotalBillInfoDto totalBillInfoDto = null;
		JSONObject returnJsonObject = null;
		JSONObject jsonAsstes_InCome = null;
		String totalFundBalanceCode = null;
		String totalFundBalanceMsg = null;
		returnJsonObject = new JSONObject();
		jsonAsstes_InCome = new JSONObject();
		String userName = null;
		String mobile = null;
		String idNo = null;
		String userType = null;
		if (null != userBaseInfo) {
			userName = userBaseInfo.getCustName();
			String temp = "";
			if (userName != null && !userName.equals("")) {
				for (int i = 0; i < userName.length() - 1; i++) {
					temp += "*";
				}
			}
			userName = userName == null || userName.equals("") ? "" : userName.charAt(0) + temp;
			mobile = userBaseInfo.getMobile();
			idNo = userBaseInfo.getIdNo();
			userType = userBaseInfo.getUserType();
		}
		logger.info("QueryManagerImpl类【queryTotalFundBalance】我的账单页面开始>>>>用户名userName：" + userName + " ,  手机号mobile:" + mobile + "  ,  证件号码idNo:" + idNo + "  ,  用户类型userType:" + userType);
		if (userName == null || userName.equals("") || idNo == null || idNo.equals("")) {
			userName = "您好！";
		} else {
			if (Integer.parseInt(idNo.substring(16, 17)) % 2 == 0) {
				userName = "您好！" + userName + "女士";
			} else {
				userName = "您好！" + userName + "先生";
			}
		}
		StringBuffer sbfu = new StringBuffer();
		if (null != mobile && !("").equals(mobile)) {
			sbfu.append(mobile.substring(0, 3));
			sbfu.append("****");
			sbfu.append(mobile.substring(mobile.length() - 4, mobile.length()));
		} else {
			sbfu.append("***********");
		}
		// 调用 --查询用户的总资产、总收益-- 接口
		logger.info("QueryManagerImpl类【queryTotalFundBalance】调用 ---查询用户的总资产、总收益--start ---  接口");
		totalFundBalanceMessage = queryServiceClient.queryTotalFundBalance(context, cmfUserId);
		logger.info("QueryManagerImpl类【queryTotalFundBalance】调用 ---查询用户的总资产、总收益--end ---  接口");
		logger.info("QueryManagerImpl类【queryTotalFundBalance】方法：resultCode:" + totalFundBalanceMessage == null ? "totalFundBalanceMessage is null" : totalFundBalanceMessage.getResultCode());
		totalFundBalanceCode = "0000";
		totalFundBalanceMsg = "success";
		totalBillInfoDto = (TotalBillInfoDto) totalFundBalanceMessage.getData();
		// 0000 接口调用成功
		if ("0000".equals(totalFundBalanceMessage.getResultCode()) && totalBillInfoDto != null) {
			logger.info("QueryManagerImpl类【queryTotalFundBalance】方法：总资产:" + totalBillInfoDto.getTotalbill());
			logger.info("QueryManagerImpl类【queryTotalFundBalance】方法：总收益:" + totalBillInfoDto.getTotalprofit());
			String asstes = totalBillInfoDto.getTotalbill();
			String inCome = totalBillInfoDto.getTotalprofit();
			String totalValue = totalBillInfoDto.getTotalValue();
			if (null == asstes || "".equals(asstes) || null == inCome || "".equals(inCome) || StringUtils.isEmpty(totalValue)) {
				jsonAsstes_InCome.put("asstes", "0.00");
				jsonAsstes_InCome.put("inCome", "0.00");
				jsonAsstes_InCome.put("totalValue", "0.00");
			} else {
				NumberFormat numberFormat = new DecimalFormat(",###.##");
				jsonAsstes_InCome.put("asstes", numberFormat.format(new BigDecimal(asstes)));
				jsonAsstes_InCome.put("inCome", numberFormat.format(new BigDecimal(inCome)));
				jsonAsstes_InCome.put("totalValue", numberFormat.format(new BigDecimal(totalValue)));
			}
		} else {
			// QRY-U001 没有查到用户信息
			if ("QRY-U001".equals(totalFundBalanceMessage.getResultCode())) {
				jsonAsstes_InCome.put("name", "");
			} else {
				jsonAsstes_InCome.put("name", totalBillInfoDto.getName());
			}
			jsonAsstes_InCome.put("asstes", "0.00");
			jsonAsstes_InCome.put("inCome", "0.00");
			jsonAsstes_InCome.put("totalValue", "0.00");
			totalFundBalanceCode = totalFundBalanceMessage.getResultCode();
			totalFundBalanceMsg = totalFundBalanceMessage.getResultMsg();
		}
		QueryMessageDto queryMessageDto = queryServiceClient.queryMonthProfitBill(context, cmfUserId);
		jsonAsstes_InCome.put("userInfo", queryMessageDto != null ? queryMessageDto.getData() : "");
		jsonAsstes_InCome.put("userName", userName);
		jsonAsstes_InCome.put("mobile", sbfu.toString());
		jsonAsstes_InCome.put("idNo", RequestHelper.getEncryptIdNo(idNo));
		jsonAsstes_InCome.put("userType", userType);
		if(accoRlaDto!=null){
			
			if(StringUtil.isEmpty(accoRlaDto.getEcCustNo())){
				logger.info("缺少关键参数--ecCustNo客户编号");
				returnJsonObject.put("returnCode", 9000);
				returnJsonObject.put("returnMsg", "缺少关键参数--ecCustNo客户编号");
				
			}else {
				
				QueryMessageDto queryDto = queryServiceClient.queryTradeInfoLately(context, accoRlaDto.getEcCustNo());
				jsonAsstes_InCome.put("fundNext", queryDto == null ? ECConstants.RETURN_CODE_9006 : queryDto.getData());
			}
		}
		// 总资产和总收益 结果
		returnJsonObject.put("totalFundBalanceCode", totalFundBalanceCode);
		returnJsonObject.put("totalFundBalanceMsg", totalFundBalanceMsg);
		returnJsonObject.put("asstesInCome", jsonAsstes_InCome);
		logger.info("QueryManagerImpl类【queryTotalFundBalance】结束>>>returnJsonObject:" + returnJsonObject);
		return returnJsonObject;
	}

	@Override
	public JSONObject queryNewsArticleAndCount(Context context, String cmfUserId, int page,String name) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		int amount = ECConstants.MESSAGE_AMOUNT;// 每页展示条数
		int beginrow = (page - 1) * amount;// 开始条数
		int endrow = beginrow + amount;
		int maxPages = 1;

		logger.info("QueryManagerImpl类【queryNewsArticleAndCount】开始>>>amount:"+amount+">>>beginrow:" + beginrow+">>>endrow:" + endrow+">>>timestamp:" + System.currentTimeMillis());
		
		// 查列表
		QueryMessageDto queryMessageDto = queryServiceClient.queryNewsArticle(context, cmfUserId, beginrow, endrow, name);
		// 查总数
		QueryMessageDto articleCount = queryServiceClient.queryNewsArticleCount(context, cmfUserId, name);
		
		List<ArticleDtoV2> list = new ArrayList<ArticleDtoV2>();
		int count = 0;
		
		if(queryMessageDto != null){
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
			list = (List<ArticleDtoV2>) queryMessageDto.getData();
		}
		if(articleCount != null && articleCount.getReturnCode().equals(ECConstants.RETURN_CODE_0000)){
			count = (Integer) articleCount.getData();
		}

		maxPages = count % amount == 0 ? count / amount : count / amount + 1;
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		if(list != null){
			jsonObject.put("list", list);
		}
		if(count > 0){
			jsonObject.put("count", count);// 总记录数
			jsonObject.put("maxPages", maxPages);// 总页数
		}
		jsonObject.put("currentPage", page);// 当前页数

		logger.info("QueryManagerImpl类【queryNewsArticleAndCount】结束>>>returnCode:"+returnCode+">>>returnMsg:" + returnMsg+">>>count:" + count+">>>maxPages:" + maxPages+">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}

	@Override
	public JSONObject queryCompanyArticleAndCount(Context context, String cmfUserId, int page,String name) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		int amount = ECConstants.MESSAGE_AMOUNT;// 每页展示条数
		int beginrow = (page - 1) * amount;// 开始条数
		int endrow = beginrow + amount;
		int maxPages = 1;

		logger.info("QueryManagerImpl类【queryCompanyArticleAndCount】开始>>>amount:"+amount+">>>beginrow:" + beginrow+">>>endrow:" + endrow+">>>timestamp:" + System.currentTimeMillis());
		
		// 查列表
		QueryMessageDto queryMessageDto = queryServiceClient.queryCompanyArticle(context, cmfUserId, beginrow, endrow, name);
		// 查总数
		QueryMessageDto articleCount = queryServiceClient.queryCompanyArticleCount(context, cmfUserId, name);
		
		List<ArticleDtoV2> list = new ArrayList<ArticleDtoV2>();
		int count = 0;
		
		if(queryMessageDto != null){
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
			list = (List<ArticleDtoV2>) queryMessageDto.getData();
		}
		if(articleCount != null && articleCount.getReturnCode().equals(ECConstants.RETURN_CODE_0000)){
			count = (Integer) articleCount.getData();
		}

		maxPages = count % amount == 0 ? count / amount : count / amount + 1;
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		if(list != null){
			jsonObject.put("list", list);
		}
		if(count > 0){
			jsonObject.put("count", count);// 总记录数
			jsonObject.put("maxPages", maxPages);// 总页数
		}
		jsonObject.put("currentPage", page);// 当前页数

		logger.info("QueryManagerImpl类【queryCompanyArticleAndCount】结束>>>returnCode:"+returnCode+">>>returnMsg:" + returnMsg+">>>count:" + count+">>>maxPages:" + maxPages+">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}

	@Override
	public JSONObject queryArticleContent(Context context, String articleId) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";

		logger.info("QueryManagerImpl类【queryArticleContent】开始>>>articleId:"+articleId + ">>>timestamp:" + System.currentTimeMillis());
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		// 查列表
		QueryMessageDto queryMessageDto = queryServiceClient.queryArticleContent(context, articleId);
		if (queryMessageDto != null) {
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			map = (Map<String, Object>) queryMessageDto.getData();
		}
		
		if (map != null) {
			if (map.containsKey("CONTENT")) {
				jsonObject.put("content", map.get("CONTENT"));
			}
			if (map.containsKey("TITLE")) {
				jsonObject.put("title", map.get("TITLE"));
			}
			if (map.containsKey("PUBLISHDATE")) {
				jsonObject.put("publishDate", map.get("PUBLISHDATE"));
			}
		}

		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);

		logger.info("QueryManagerImpl类【queryArticleContent】结束>>>returnCode:"+returnCode + ">>>returnMsg:" + returnMsg + ">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}

	@Override
	public JSONObject queryHotFundByType(Context context, String type) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";

		logger.info("QueryManagerImpl类【queryHotFundByType】开始>>>timestamp:" + System.currentTimeMillis());
		
		QueryMessageDto queryMessageDto = queryServiceClient.queryHotFundByType(context, type);
		FundInfoDtoV2 fundInfoDtoV2 = null;
		
		if(queryMessageDto != null){
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
			fundInfoDtoV2 = (FundInfoDtoV2) queryMessageDto.getData();
		}
		
		if(fundInfoDtoV2 != null){
			jsonObject.put("fundInfoDtoV2", fundInfoDtoV2);
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		return jsonObject;
	}

	@Override
	public JSONObject querySocialRecruitment(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		String positionId = request.getParameter("positionId");/*职位类型ID*/
		String placeId = request.getParameter("placeId");/*工作地点ID*/
		
		String tempPage = request.getParameter("page");
		int page = 1;
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}
		int rowCount = ECConstants.RECRUITMENT_AMOUNT;// 每页展示条数
		int beginrow = (page - 1) * rowCount;// 开始条数
		int endrow = beginrow + rowCount;
		int maxPages = 1;
		
		logger.info("QueryManagerImpl类【querySocialRecruitment】开始>>>positionId:"+positionId+">>>placeId："+placeId+">>>beginrow："+beginrow+">>>endrow："+endrow+">>>timestamp:" + System.currentTimeMillis());
		
		/****************** 查询社会招聘信息 S******************/
		List<RecruitmentDto> recruitmentDtoList = null;
		QueryMessageDto queryRecruit = queryServiceClient.querySocialRecruitment(context, positionId, placeId, beginrow, endrow);
		if(queryRecruit != null){
			recruitmentDtoList = (List<RecruitmentDto>) queryRecruit.getData();
			returnCode = queryRecruit.getReturnCode();
			returnMsg = queryRecruit.getReturnMsg();
		}
		if(recruitmentDtoList != null && recruitmentDtoList.size() > 0){
			jsonObject.put("recruitmentDtoList", recruitmentDtoList);
		}
		/****************** 查询社会招聘信息 E******************/

		
		/****************** 查询社会招聘信息记录数 S******************/
		QueryMessageDto queryRecruitCount = queryServiceClient.querySocialRecruitmentCount(context, positionId, placeId); 
		int count = 0;
		if(queryRecruitCount != null){
			count = (Integer) queryRecruitCount.getData();
		}
		if(count > 0){
			maxPages = count % rowCount == 0 ? count / rowCount : count / rowCount + 1;
		}
		/****************** 查询社会招聘信息记录数 E******************/
		
		
		/****************** 查询职位类别 S******************/
		List<PlaceDto> placeDtoList = null;
		QueryMessageDto queryPlace = queryServiceClient.queryPlace(context);
		if(queryPlace != null){
			placeDtoList = (List<PlaceDto>) queryPlace.getData();
		}
		if(placeDtoList != null && placeDtoList.size() > 0){
			jsonObject.put("placeDtoList", placeDtoList);
		}
		/****************** 查询职位类别 E******************/

		
		/****************** 查询工作地点 S******************/
		List<PositionTypeDto> positionTypeDtoList = null;
		QueryMessageDto queryPositionType = queryServiceClient.queryPositionType(context);
		if(queryPositionType != null){
			positionTypeDtoList = (List<PositionTypeDto>) queryPositionType.getData();
		}
		if(positionTypeDtoList != null && positionTypeDtoList.size() > 0){
			jsonObject.put("positionTypeDtoList", positionTypeDtoList);
		}
		/****************** 查询工作地点 E******************/
		
		
		jsonObject.put("currentPage", page);
		jsonObject.put("count", count);
		jsonObject.put("maxPages", maxPages);
		jsonObject.put("currentPositionId", positionId);	
		jsonObject.put("currentPlaceId", placeId);
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);

		logger.info("QueryManagerImpl类【querySocialRecruitment】结束>>>currentPage:"+page+">>>count："+count+">>>maxPages："+maxPages+">>>returnCode："+returnCode
				+">>>returnMsg："+returnMsg+">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}

	@Override
	public JSONObject queryCampusRecruitment(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";

		String positionId = request.getParameter("positionId");/*职位类型ID*/
		String placeId = request.getParameter("placeId");/*工作地点ID*/
		
		String tempPage = request.getParameter("page");
		int page = 1;
		if(!StringUtils.isNumeric(tempPage)){
			tempPage = "1";
		}
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}
		int rowCount = ECConstants.RECRUITMENT_AMOUNT;// 每页展示条数
		int beginrow = (page - 1) * rowCount;// 开始条数
		int endrow = beginrow + rowCount;
		int maxPages = 1;
		
		logger.info("QueryManagerImpl类【queryCampusRecruitment】开始>>>positionId:"+positionId+">>>placeId："+placeId+">>>beginrow："+beginrow+">>>endrow："+endrow+">>>timestamp:" + System.currentTimeMillis());
		
		/****************** 查询社会招聘信息 S******************/
		List<RecruitmentDto> recruitmentDtoList = null;
		QueryMessageDto queryRecruit = queryServiceClient.queryCampusRecruitment(context, positionId, placeId, beginrow, endrow);
		if(queryRecruit != null){
			recruitmentDtoList = (List<RecruitmentDto>) queryRecruit.getData();
			returnCode = queryRecruit.getReturnCode();
			returnMsg = queryRecruit.getReturnMsg();
		}
		if(recruitmentDtoList != null && recruitmentDtoList.size() > 0){
			jsonObject.put("recruitmentDtoList", recruitmentDtoList);
		}
		/****************** 查询社会招聘信息 E******************/

		
		/****************** 查询社会招聘信息记录数 S******************/
		QueryMessageDto queryRecruitCount = queryServiceClient.queryCampusRecruitmentCount(context, positionId, placeId); 
		int count = 0;
		if(queryRecruitCount != null){
			count = (Integer) queryRecruitCount.getData();
		}
		if(count > 0){
			maxPages = count % rowCount == 0 ? count / rowCount : count / rowCount + 1;
		}
		/****************** 查询社会招聘信息记录数 E******************/
		

		/****************** 查询工作地点 S******************/
		List<PlaceDto> placeDtoList = null;
		QueryMessageDto queryPlace = queryServiceClient.queryPlace(context);
		if(queryPlace != null){
			placeDtoList = (List<PlaceDto>) queryPlace.getData();
		}
		if(placeDtoList != null && placeDtoList.size() > 0){
			jsonObject.put("placeDtoList", placeDtoList);
		}
		/****************** 查询工作地点 E******************/

		
		/****************** 查询职位类别 S******************/
		List<PositionTypeDto> positionTypeDtoList = null;
		QueryMessageDto queryPositionType = queryServiceClient.queryPositionType(context);
		if(queryPositionType != null){
			positionTypeDtoList = (List<PositionTypeDto>) queryPositionType.getData();
		}
		if(positionTypeDtoList != null && positionTypeDtoList.size() > 0){
			jsonObject.put("positionTypeDtoList", positionTypeDtoList);
		}
		/****************** 查询职位类别 E******************/
		
		
		jsonObject.put("currentPage", page);
		jsonObject.put("count", count);
		jsonObject.put("maxPages", maxPages);
		jsonObject.put("currentPositionId", positionId);	
		jsonObject.put("currentPlaceId", placeId);
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);

		logger.info("QueryManagerImpl类【queryCampusRecruitment】结束>>>currentPage:"+page+">>>count："+count+">>>maxPages："+maxPages+">>>returnCode："+returnCode
				+">>>returnMsg："+returnMsg+">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}
	
	@Override
	public JSONObject queryTraineeRecruitment(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		String positionId = request.getParameter("positionId");/*职位类型ID*/
		String placeId = request.getParameter("placeId");/*工作地点ID*/
		
		String tempPage = request.getParameter("page");
		int page = 1;
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}
		int rowCount = ECConstants.RECRUITMENT_AMOUNT;// 每页展示条数
		int beginrow = (page - 1) * rowCount;// 开始条数
		int endrow = beginrow + rowCount;
		int maxPages = 1;
		
		logger.info("QueryManagerImpl类【queryTraineeRecruitment】开始>>>positionId:"+positionId+">>>placeId："+placeId+">>>beginrow："+beginrow+">>>endrow："+endrow+">>>timestamp:" + System.currentTimeMillis());
		
		/****************** 查询社会招聘信息 S******************/
		List<RecruitmentDto> recruitmentDtoList = null;
		QueryMessageDto queryRecruit = queryServiceClient.queryTraineeRecruitment(context, positionId, placeId, beginrow, endrow);
		if(queryRecruit != null){
			recruitmentDtoList = (List<RecruitmentDto>) queryRecruit.getData();
			returnCode = queryRecruit.getReturnCode();
			returnMsg = queryRecruit.getReturnMsg();
		}
		if(recruitmentDtoList != null && recruitmentDtoList.size() > 0){
			jsonObject.put("recruitmentDtoList", recruitmentDtoList);
		}
		/****************** 查询社会招聘信息 E******************/
		
		
		/****************** 查询社会招聘信息记录数 S******************/
		QueryMessageDto queryRecruitCount = queryServiceClient.queryTraineeRecruitmentCount(context, positionId, placeId); 
		int count = 0;
		if(queryRecruitCount != null){
			count = (Integer) queryRecruitCount.getData();
		}
		if(count > 0){
			maxPages = count % rowCount == 0 ? count / rowCount : count / rowCount + 1;
		}
		/****************** 查询社会招聘信息记录数 E******************/
		
		
		/****************** 查询职位类别 S******************/
		List<PlaceDto> placeDtoList = null;
		QueryMessageDto queryPlace = queryServiceClient.queryPlace(context);
		if(queryPlace != null){
			placeDtoList = (List<PlaceDto>) queryPlace.getData();
		}
		if(placeDtoList != null && placeDtoList.size() > 0){
			jsonObject.put("placeDtoList", placeDtoList);
		}
		/****************** 查询职位类别 E******************/
		
		
		/****************** 查询工作地点 S******************/
		List<PositionTypeDto> positionTypeDtoList = null;
		QueryMessageDto queryPositionType = queryServiceClient.queryPositionType(context);
		if(queryPositionType != null){
			positionTypeDtoList = (List<PositionTypeDto>) queryPositionType.getData();
		}
		if(positionTypeDtoList != null && positionTypeDtoList.size() > 0){
			jsonObject.put("positionTypeDtoList", positionTypeDtoList);
		}
		/****************** 查询工作地点 E******************/
		
		
		jsonObject.put("currentPage", page);
		jsonObject.put("count", count);
		jsonObject.put("maxPages", maxPages);
		jsonObject.put("currentPositionId", positionId);	
		jsonObject.put("currentPlaceId", placeId);
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("QueryManagerImpl类【queryTraineeRecruitment】结束>>>currentPage:"+page+">>>count："+count+">>>maxPages："+maxPages+">>>returnCode："+returnCode
				+">>>returnMsg："+returnMsg+">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}

	@Override
	public JSONObject queryRecruitmentDetail(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		String informationId = request.getParameter("informationId");/*职位类型ID*/

		logger.info("QueryManagerImpl类【queryRecruitmentDetail】开始>>>informationId:"+informationId+">>>timestamp:" + System.currentTimeMillis());
		
		RecruitmentDto dto = null;
		QueryMessageDto queryMessageDto = queryServiceClient.queryRecruitmentDetail(context,informationId);
		if(queryMessageDto != null){
			dto = (RecruitmentDto) queryMessageDto.getData();
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
		}else{
			returnCode = ECConstants.RETURN_CODE_9001;
			returnMsg = ECConstants.RETURN_MSG_9001;
		}
		
		if(dto != null){
			jsonObject.put("dto", dto);
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);

		logger.info("QueryManagerImpl类【queryRecruitmentDetail】结束>>>returnCode:"+returnCode+">>>returnMsg:" + returnMsg+">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}

	@Override
	public JSONObject queryCampusTalk(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		String campusName = request.getParameter("campusName");/*校园ID*/
		
		String tempPage = request.getParameter("page");
		int page = 1;
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}
		int rowCount = ECConstants.CAMPUSTALK_AMOUNT;// 每页展示条数
		int beginrow = (page - 1) * rowCount;// 开始条数
		int endrow = beginrow + rowCount;
		int maxPages = 1;
		
		logger.info("QueryManagerImpl类【queryCampusTalk】开始>>>campusName:"+campusName+">>>beginrow："+beginrow+">>>endrow："+endrow+">>>timestamp:" + System.currentTimeMillis());
		/****************** 查询校园宣讲信息 S******************/
		List<CampusTalkDto> campusTalkDtoList = null;
		QueryMessageDto queryCampusTalk = queryServiceClient.queryCampusTalk(context, campusName, beginrow, endrow);
		if(queryCampusTalk != null){
			campusTalkDtoList = (List<CampusTalkDto>) queryCampusTalk.getData();
			returnCode = queryCampusTalk.getReturnCode();
			returnMsg = queryCampusTalk.getReturnMsg();
		}
		if(campusTalkDtoList != null && campusTalkDtoList.size() > 0){
			jsonObject.put("campusTalkDtoList", campusTalkDtoList);
		}
		/****************** 查询校园宣讲信息 E******************/
		
		
		/****************** 查询校园宣讲信息记录数 S******************/
		QueryMessageDto queryCampusTalkCount = queryServiceClient.queryCampusTalkCount(context, campusName); 
		int count = 0;
		if(queryCampusTalkCount != null){
			count = (Integer) queryCampusTalkCount.getData();
		}
		if(count > 0){
			maxPages = count % rowCount == 0 ? count / rowCount : count / rowCount + 1;
		}
		/****************** 查询校园宣讲信息记录数 E******************/
		
		
		/****************** 查询校园列表信息 S******************/
		List<SchoolDto> schoolDtoList = null;
		QueryMessageDto querySchool = queryServiceClient.querySchoolList(context);
		if(querySchool != null){
			schoolDtoList = (List<SchoolDto>) querySchool.getData();
		}
		if(schoolDtoList != null && schoolDtoList.size() > 0){
			jsonObject.put("schoolDtoList", schoolDtoList);
		}
		/****************** 查询校园列表信息 E******************/
		
		
		jsonObject.put("currentPage", page);
		jsonObject.put("count", count);
		jsonObject.put("maxPages", maxPages);
		jsonObject.put("currentCampusId", campusName);	
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("QueryManagerImpl类【queryCampusTalk】结束>>>currentPage:"+page+">>>count："+count+">>>maxPages："+maxPages+">>>returnCode："+returnCode
				+">>>returnMsg："+returnMsg+">>>timestamp:" + System.currentTimeMillis());
		
		return jsonObject;
	}

	@Override
	public QueryMessageDto campusTalkDownLoad(Context context, String campusId) {
		return queryServiceClient.campusTalkDownLoad(context, campusId);
	}
	@Override
	public QueryMessageDto getTradeAcctInfoByTradeAcco(Context context, String tradeacco) {
		QueryMessageDto queryMessageDto = queryServiceClient.getTradeAcctInfoByTradeAcco(context, tradeacco);
		return queryMessageDto;
	}

	@Override
	public JSONObject queryWenTiByCommon(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";

		logger.info("QueryManagerImpl类【queryWenTiByCommon】开始>>>timestamp:" + System.currentTimeMillis());
		/****************** 查询常见问题 S******************/
		List<QuestionDto> list = null;
		QueryMessageDto queryMessageDto = queryServiceClient.queryWenTiByCommon(context);
		if(queryMessageDto != null){
			list = (List<QuestionDto>) queryMessageDto.getData();
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
		}
		if(list != null && list.size() > 0){
			jsonObject.put("campusTalkDtoList", list);
		}
		/****************** 查询常见问题 E******************/
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);

		logger.info("QueryManagerImpl类【queryWenTiByCommon】结束>>>returnCode"+returnCode+">>>returnMsg:" + returnMsg+">>>list.size():" + list.size()+">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}

	@Override
	public JSONObject queryWenTiList(Context context, HttpServletRequest request) throws UnsupportedEncodingException {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		String tempPage = request.getParameter("page");
		int page = 1;
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}
		int rowCount = ECConstants.FAQ_AMOUNT;// 每页展示条数
		int beginrow = (page - 1) * rowCount;// 开始条数
		int endrow = beginrow + rowCount;
		int maxPages = 1;
		String title = request.getParameter("title");
		if(title != null && !title.equals("")){
			title = URLDecoder.decode(title, "utf-8");
		}
		logger.info("QueryManagerImpl类【queryWenTiList】开始>>>title:"+title+">>>beginrow:"+beginrow+">>>endrow:"+endrow+">>>timestamp:" + System.currentTimeMillis());
		/****************** 查询问题列表 S******************/
		List<QuestionDto> list = null;
		QueryMessageDto queryMessageDto = queryServiceClient.queryWenTiList(context, title, beginrow, endrow);
		if(queryMessageDto != null){
			list = (List<QuestionDto>) queryMessageDto.getData();
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
		}
		if(list != null && list.size() > 0){
			jsonObject.put("list", list);
		}
		/****************** 查询问题列表 E******************/
		
		/****************** 查询问题总数 S******************/
		int count = 0;
		QueryMessageDto queryMessageDtoCount = queryServiceClient.queryWenTiCount(context, title);
		if(queryMessageDtoCount != null){
			count = (Integer) queryMessageDtoCount.getData();
		}
		if(count > 0){
			maxPages = count % rowCount == 0 ? count / rowCount : count / rowCount + 1;
		}
		/****************** 查询问题总数 E******************/
		
		jsonObject.put("currentPage", page);
		jsonObject.put("count", count);
		jsonObject.put("maxPages", maxPages);
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("QueryManagerImpl类【queryWenTiByCommon】结束>>>returnCode"+returnCode+">>>returnMsg:" + returnMsg+">>>count:" + count+">>>currentPage:" + page
				+">>>maxPages:" + maxPages+">>>list.size():" + list.size()+">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}
	/**
	 * 查询费率
	 * 
	 * @param context
	 * @param fundId
	 *            产品ID，不可为空
	 * @param channelNo
	 *            用户购买的渠道号，如果还没有获取，可以传空串
	 * @param custLevel
	 *            客户等级，目前全部传空串
	 * @return QueryMessageDto.data = Map<ChannelNo, List<FeeRateDto>>
	 */
	@Override
	public JSONObject queryFeeRateList(HttpServletRequest request, String money, Context context, String fundId, List<String> channelNoList, String custLevel) {

		JSONObject resultJson = new JSONObject();
		String returnCode = null;
		String returnMsg = null;
		if (fundId == null || "".equals(fundId)) { // 关键值为空
			returnCode = ECConstants.RETURN_CODE_9000;
			returnMsg = ECConstants.RETURN_MSG_9000;
			resultJson.put("returnCode", returnCode);
			resultJson.put("returnCode", returnMsg);
			return resultJson;
		}

		Object sessionQueryFeeRateMap = request.getSession(true).getAttribute(SessionValue.SESSION_QUERYFEERATEMAP);
		Object sessionfundId = request.getSession(true).getAttribute(SessionValue.SESSION_SESSIONFUNDID);
		String fundIdSession = "";
		Map<String, List<FeeRateDto>> map = new HashMap<String, List<FeeRateDto>>();
		// session中存在值
		if (sessionQueryFeeRateMap != null && sessionfundId != null) {

			returnCode = "0000";
			returnMsg = "成功";

			fundIdSession = (String) sessionfundId;
			if (fundIdSession.equals(fundId)) {// 同一个fundId则不去查询数据库
				map = (Map<String, List<FeeRateDto>>) sessionQueryFeeRateMap;
			} else {
				// 调用查询费率
				QueryMessageDto messageDto = queryServiceClient.queryFeeRateList(context, fundId, channelNoList, custLevel);
				returnCode = messageDto.getResultCode();
				returnMsg = messageDto.getResultMsg();
				if (messageDto != null) {
					map = (Map<String, List<FeeRateDto>>) messageDto.getData();
					request.getSession(true).setAttribute(SessionValue.SESSION_QUERYFEERATEMAP, map);
					request.getSession(true).setAttribute(SessionValue.SESSION_SESSIONFUNDID, fundId);
				}
			}
		} else {// session中不存在值
				// 调用查询费率
			QueryMessageDto messageDto = queryServiceClient.queryFeeRateList(context, fundId, channelNoList, custLevel);
			if (messageDto != null) {
				returnCode = messageDto.getResultCode();
				returnMsg = messageDto.getResultMsg();
				if (returnCode != null && ECConstants.COMMON_SUCCESS.equals(returnCode)) {
					map = (Map<String, List<FeeRateDto>>) messageDto.getData();
					request.getSession(true).setAttribute(SessionValue.SESSION_QUERYFEERATEMAP, map);
					request.getSession(true).setAttribute(SessionValue.SESSION_SESSIONFUNDID, fundId);
				}
			} else {
				returnCode = ECConstants.COMMON_ERROR_SYSERRCODE;
				returnMsg = ECConstants.COMMON_ERROR_SYSERRMSG;
			}
		}
		double rateValue = 0.0;
		double commro = 0.0;
		if (map != null) {
			// rateValue = queryFeeRate(map, fundId, channelNoList, money);
			Map<String, Double> mapData = queryFeeRate(map, fundId, channelNoList, money);
			if (mapData != null) {
				rateValue = mapData.get("rateData");
				commro = mapData.get("commroData");
			}
		}
		resultJson.put("returnCode", returnCode);
		resultJson.put("returnMsg", returnMsg);
		resultJson.put("rate", rateValue);
		resultJson.put("commro", commro);
		logger.info("QueryController类【queryFeeRateList】结束：>>>resultJson=" + "returnCode>>>" + returnCode + ",returnMsg>>>" + returnMsg + ",rate>>>" + rateValue + ",commro>>>" + commro);
		return resultJson;
	}

		/**
	 * 计算费率
	 * 
	 * @param map
	 * @param fundId
	 * @param channelNoList
	 * @param money
	 * @return
	 */
	public Map<String, Double> queryFeeRate(Map<String, List<FeeRateDto>> map, String fundId, List<String> channelNoList, String money) {
		double rate = 0.0;
		Map<String, Double> returnData = null;
		double dMoney = Double.parseDouble(money);
		String channelNo = "";
		if (channelNoList != null && channelNoList.size() > 0) {
			channelNo = channelNoList.get(0);
		} else {
			channelNo = "*";
		}
		List<FeeRateDto> list = map.get(channelNo);// 获取一种渠道号
		FeeRateDto dto = null;
		for (int i = 0; i < list.size(); i++) {
			dto = list.get(i);
			if (fundId.equals(dto.getFundId()) && channelNo.equals(dto.getChannelNo()) && dMoney >= dto.getStrAmt() && dMoney < dto.getEndAmt()) {
				returnData = new HashMap<String, Double>();
				if (dto.getFeeMode() == '0') {// 费率类型 0：按rate比例收费；
					rate = dMoney * dto.getBaseRate() * dto.getCommro(); // 折扣，产品认购费= 认购金额*认购费用*折扣 或 单次费用*折扣
				} else if (dto.getFeeMode() == '1') {// 费率类型 1 按次收费，每次收rate元；
					rate = dto.getSingleFee() * dto.getCommro();// 暂时 按次收费  没有折扣
				}
				returnData.put("rateData", rate);
				returnData.put("commroData", dto.getCommro());// 折扣
				break; // 一个金额只有一种区间,匹配了就没有必要循环了
			}
		}
		return returnData;
	}

		@Override
		public QueryMessageDto queryAdvert(Context context,String type) {
			return queryServiceClient.queryAdvert(context,type);
		}

		@Override
		public JSONObject queryBankInfoByTradeAcco(Context context, String tradeacco) {
			JSONObject jsonObject = new JSONObject();
			QueryMessageDto tradeMessage = null;
			// 根据tradeAcco获取交易账号
			tradeMessage = getTradeAcctInfoByTradeAcco(context, tradeacco);
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
				jsonObject.put("bankacco", encryptTradeList.get(0).getBankacco());
				jsonObject.put("realBankName", encryptTradeList.get(0).getRealBankName());
				jsonObject.put("returnCode", ECConstants.RETURN_CODE_0000);
				jsonObject.put("returnMsg", ECConstants.RETURN_MSG_0000);
			}else{
				jsonObject.put("returnCode", ECConstants.RETURN_CODE_9001);
				jsonObject.put("returnMsg", ECConstants.RETURN_MSG_9001);
			}
			return jsonObject;
		}

	@Override
	public JSONObject queryAgentFundInfo(Context context, String cmfUserId) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryAgentFundInfo(context, cmfUserId);
		JSONObject returnJsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		List<AgentFundDto> list = new ArrayList<AgentFundDto>();// 全部订单
		List<AgentFundDto> gList = new ArrayList<AgentFundDto>();// 存续中订单
		List<AgentFundDto> zList = new ArrayList<AgentFundDto>();// 已到期订单
		List<AgentFundDto> wList = new ArrayList<AgentFundDto>();
		
		if (queryMessageDto != null) {
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			list = (List<AgentFundDto>) queryMessageDto.getData();
		}
		if (list != null && list.size() > 0) {
			if (list.get(list.size() - 1) != null && list.get(list.size() - 1).getFundnm().equals("合计(人民币)")) {
				list.remove(list.size() - 1);
			}
			for (AgentFundDto agentFundDto : list) {
				if (agentFundDto.getApplySt() != null && agentFundDto.getApplySt().equals("G")) {
					gList.add(agentFundDto);
				} else if (agentFundDto.getApplySt() != null && agentFundDto.getApplySt().equals("Z")) {
					zList.add(agentFundDto);
				}
			}
		}
		
		String fundAcco = queryServiceClient.queryUserFundAccoByCmfUserId(cmfUserId);
		if(list == null) {
			list = new ArrayList<AgentFundDto>();
		}
		checkUserIsInWhiteList(list, wList, fundAcco);
		
		
		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		
		if (list != null && list.size() > 0) {
			returnJsonObject.put("list", list);
		}
		if (gList != null && gList.size() > 0) {
			returnJsonObject.put("gList", gList);
		}
		if (zList != null && zList.size() > 0) {
			returnJsonObject.put("zList", zList);
		}
		if(wList != null && wList.size() > 0) {
			returnJsonObject.put("wList", wList);
		}
		return returnJsonObject;
	}

	private void checkUserIsInWhiteList(List<AgentFundDto> list,
			List<AgentFundDto> wList, String fundAcco) {
		/* 白名单检测    S*/
		logger.info("开始进行对当前登陆用户进行白名单检测. 当前登陆用户fundAcco为"+fundAcco);
		QueryMessageDto fundAccoDto = queryHomeAddressIsWordWithLinkage(new Context(), "SYSTEM", "NVWL01", null, null);
		List<ParameterDto> fundAccoParamList= (List<ParameterDto>) fundAccoDto.getData();
		
		if(fundAccoParamList == null || fundAccoParamList.size() == 0){
			logger.info("基金账号白名单未做配置，不进行追加");
			return;
		}
		ParameterDto fundAccoParam = fundAccoParamList.get(0);
		if(fundAccoParam == null) {
			logger.info("基金账号白名单未做配置，不进行追加");
			return;
		}
		String fundAccoStr = fundAccoParam.getPmco();
		if(StringUtils.isBlank(fundAccoStr)) {
			logger.info("基金账号白名单未做配置，不进行追加");
			return;
		}
		
		logger.info("查询出来的用户基金账号白名单为" +fundAccoStr);
		List<String> fundAccoList = Arrays.asList(fundAccoStr.split(","));
		if(fundAccoList.contains(fundAcco)) {
			logger.info("当前登陆用户命中白名单,开始查询需追加的产品id 当前登录用户fundAcco为 "+fundAcco);
			QueryMessageDto fundIdDto = queryHomeAddressIsWordWithLinkage(new Context(), "SYSTEM", "NVWL02", null, null);
			List<ParameterDto> fundIdParamList= (List<ParameterDto>) fundIdDto.getData();
			
			if(fundIdParamList == null || fundIdParamList.size() == 0) {
				logger.info("产品白名单未做配置，不进行追加");
				return;
			}
			ParameterDto fundIdParam = fundIdParamList.get(0);
			if(fundIdParam == null) {
				logger.info("产品白名单未做配置，不进行追加");
				return;
			}
			String fundIdStr = fundIdParam.getPmco();
			if(StringUtils.isBlank(fundIdStr)) {
				logger.info("产品白名单未做配置，不进行追加");
				return;
			}
			
			logger.info("查询出需追加的产品id为 "+fundIdStr +"   当前登陆用户fundAcco为"+fundAcco);
			addWhiteListFund(list,wList,Arrays.asList(fundIdStr.split(",")));
		} else {
			logger.info("当前登陆用户未命中白名单，不进行追加 当前登录用户fundAcoo为"+fundAcco);
		}
	}
	
	
	@Override
	public JSONObject queryCooperation(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		logger.info("QueryManagerImpl类【queryCooperation】开始>>>timestamp:" + System.currentTimeMillis());
		
		/****************** 合作机构查询  S******************/
		List<CooperationDto> cooperationDtoList = null;
		QueryMessageDto queryCooperationDto = queryServiceClient.queryCooperation(context);
		if(queryCooperationDto != null){
			cooperationDtoList = (List<CooperationDto>) queryCooperationDto.getData();
			returnCode = queryCooperationDto.getReturnCode();
			returnMsg = queryCooperationDto.getReturnMsg();
		}
		if(cooperationDtoList != null && cooperationDtoList.size() > 0){
			jsonObject.put("cooperationDtoList", cooperationDtoList);
		}
		/****************** 合作机构查询  E******************/
		
		/****************** 代销机构查询 S******************/
		List<ConsignmentDto> consignmentDtoList = null;
		QueryMessageDto queryConsignmentDto = queryServiceClient.queryConsignment(context);
		if(queryConsignmentDto != null){
			consignmentDtoList = (List<ConsignmentDto>) queryConsignmentDto.getData();
		}
		if(consignmentDtoList != null && consignmentDtoList.size() > 0){
			jsonObject.put("consignmentDtoList", consignmentDtoList);
		}
		/****************** 代销机构查询 E******************/
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		logger.info("QueryManagerImpl类【queryCooperation】结束>>>returnCode"+returnCode+">>>returnMsg:" + returnMsg+">>>cooperationDtoList.size():" + cooperationDtoList.size()
				+">>>consignmentDtoList.size():" + consignmentDtoList.size()+">>>timestamp:" + System.currentTimeMillis());
		return jsonObject;
	}
	/**
	 * 查询用户的原始银行卡信息(未经过加密处理)
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author luos
	 */
	@Override
	public JSONObject getTradeOriginalAcctInfoByCustno(Context context, HttpServletRequest request) {
		logger.info("QueryManagerImpl类【getTradeOriginalAcctInfoByCustno】开始>>timestamp:" + System.currentTimeMillis());
		JSONObject returnJsonObject = new JSONObject();
		UserBaseInfoDto userBaseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
		UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);
		String ecCustNo = "";
		String userType = null;
		if (userBaseInfoDto == null) {
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_8000);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_8000);
			return returnJsonObject;
		} else if(userAccoRla==null){
			returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_9005);
			returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_9005);
			return returnJsonObject;
		}else{
			if (userAccoRla != null) {
				ecCustNo = userAccoRla.getEcCustNo();
			}
			userType = userBaseInfoDto.getUserType();
		}
		List<TradeAcctInfoDto> tradeAcctDtoList = null;
		String returnCode = null;
		String returnMsg = null;
		QueryMessageDto messageDto = queryServiceClient.queryUserTradeAcctInfoList(context, ecCustNo, null);
		if (messageDto != null) {
			returnCode = messageDto.getResultCode();
			returnMsg = messageDto.getResultMsg();
			logger.info("查询银行卡号列表，returnCode=" + returnCode);
			if (returnCode != null && ECConstants.COMMON_SUCCESS.equals(returnCode)) {
				tradeAcctDtoList = (List<TradeAcctInfoDto>) messageDto.getData();
				returnJsonObject.put("tradeAcctlist", tradeAcctDtoList);
			}
		} else {
			returnCode = ECConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = ECConstants.COMMON_ERROR_SYSERRMSG;
		}

		returnJsonObject.put("returnCode", returnCode);
		returnJsonObject.put("returnMsg", returnMsg);
		returnJsonObject.put("userType", userType);
		logger.info("QueryManagerImpl类【getTradeOriginalAcctInfoByCustno】结束>>>returnCode:" + returnCode + ">>>>returnMsg:" + returnMsg + ">>>>>timestamp:" + System.currentTimeMillis());
		return returnJsonObject;
	}

	@Override
	public JSONObject queryFileDownloadCenter(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		String fileTypeIds = "('1','2')";
		logger.info("QueryManagerImpl类【queryFileDownloadCenter】开始>>cmfUserId:"+cmfUserId+"seqId:" + seqId+"timestamp:" + System.currentTimeMillis());
		
		QueryMessageDto queryMessageDto = queryServiceClient.queryFileDownloadCenter(context,"", fileTypeIds);
		List<FileDownloadCenterDto> list = null;
		if(queryMessageDto != null){
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
			list = (List<FileDownloadCenterDto>) queryMessageDto.getData();
		}
		if(list != null && list.size() > 0){
			jsonObject.put("list", list);
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		return jsonObject;
	}

	@Override
	public QueryMessageDto queryFileDownloadCenterDetail(Context context, String fileId, String fileTypeIds) {
		return queryServiceClient.queryFileDownloadCenter(context, fileId, fileTypeIds);
	}

	@Override
	public JSONObject queryOpenAccountProcess(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		String fileTypeIds = "('3')";
		logger.info("QueryManagerImpl类【queryOpenAccountProcess】开始>>cmfUserId:"+cmfUserId+"seqId:" + seqId+"timestamp:" + System.currentTimeMillis());
		
		QueryMessageDto queryMessageDto = queryServiceClient.queryFileDownloadCenter(context,"", fileTypeIds);
		List<FileDownloadCenterDto> list = null;
		if(queryMessageDto != null){
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
			list = (List<FileDownloadCenterDto>) queryMessageDto.getData();
		}
		if(list != null && list.size() > 0){
			jsonObject.put("list", list);
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		
		return jsonObject;
	}
	@Override
	public JSONObject queryOtherDetailOrder(Context context, HttpServletRequest request) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";

		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String serialno = request.getParameter("serialno");
		String fundId = request.getParameter("fundId");
		String channleNo =  request.getParameter("channleNo");
		
		if(serialno == null){
			serialno = "";
		}
		if(fundId == null){
			fundId = "";
		}
		if(channleNo == null){
			channleNo = "";
		}
		
		logger.info("QueryManagerImpl类【queryOtherDetailOrder】开始>>>serialno:"+serialno+">>>cmfUserId:" + cmfUserId + ">>>fundId:" + fundId + ">>>channleNo:" + channleNo +">>>timestamp:" + System.currentTimeMillis());
		
		
		QueryMessageDto queryMessageDto = queryServiceClient.queryOtherDetailOrder(context, serialno, cmfUserId, fundId, channleNo);
		AgentFundDto agentFundDto = null;
		if(queryMessageDto != null){
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
			agentFundDto = (AgentFundDto) queryMessageDto.getData();
		}
		if(agentFundDto!=null){
			jsonObject.put("agentFundDto", agentFundDto);
			QueryMessageDto fundDetail = queryFund(context, agentFundDto.getFundid(),1);
			FundInfoDtoV2 fundInfoDtoV2 = null;
			List<QuestionDto> questionDtoList = null;
			if(fundDetail!=null){
				returnCode = queryMessageDto.getReturnCode();
				returnMsg = queryMessageDto.getReturnMsg();
				fundInfoDtoV2 =(FundInfoDtoV2)fundDetail.getData();
				
				questionDtoList = (List<QuestionDto>) fundDetail.getOtherData();//产品相关问题
			}
			if(fundInfoDtoV2 != null){
				jsonObject.put("fundInfoDtoV2", fundInfoDtoV2);
			}
			if(questionDtoList != null && questionDtoList.size() > 0){
				jsonObject.put("questionDtoList", questionDtoList);
			}
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		return jsonObject;
	}

	@Override
	public JSONObject queryUnReadFundReportsCountForCompany(Context context, String cmfUserId) {
		logger.info("QueryManagerImpl类【queryUnReadFundReportsCountForCompany】开始>>>cmfUserId:"+cmfUserId+">>>timestamp:" + System.currentTimeMillis());
		QueryMessageDto message = queryServiceClient.queryUnReadFundReportsCountForCompany(context, cmfUserId);
		Integer count = 0;
		String returnCode = "";
		String returnMsg = "";
		JSONObject countJson = new JSONObject();
		if (message != null) {
			returnCode = message.getResultCode();
			returnMsg = message.getResultMsg();
			count = (Integer) message.getData();
		}
		countJson.put("returnCode", returnCode);
		countJson.put("returnMsg", returnMsg);
		countJson.put("count", count);
		logger.info("QueryManagerImpl类【queryUnReadFundReportsCountForCompany】结束>>>countJson:"+countJson+">>>timestamp:" + System.currentTimeMillis());
		return countJson;
	}

	@Override
	public JSONObject getSupportPayBankDesc(Context context) {
		logger.info("QueryManagerImpl类【getSupportPayBankDesc】开始>>>>>>timestamp:" + System.currentTimeMillis());
		String returnCode = null;
		String returnMsg = null;
		JSONObject returnObject = new JSONObject();

		QueryMessageDto queryMessageDto = queryServiceClient.getSupportPayBankDesc(context);

		logger.info("QueryManagerImpl类【getSupportPayBankDesc】调用 ---查询支持的银行卡列表--end ---  接口");
		String payList = null;
		if (queryMessageDto != null) {
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();

			if ("0000".equals(returnCode)) {
				payList = (String) queryMessageDto.getData();
			}
		} else {
			returnCode = ECConstants.COMMON_ERROR_SYSERRCODE;
			returnMsg = ECConstants.COMMON_ERROR_SYSERRMSG;
		}

		returnObject.put("returnCode", returnCode);
		returnObject.put("returnMsg", returnMsg);
		returnObject.put("payList", payList);

		logger.info("QueryManagerImpl类【getSupportPayBankDesc】结束>>>>>returnObject:"+returnObject.toString()+">timestamp:" + System.currentTimeMillis());
		return returnObject;
	}

	@Override
	public QueryMessageDto queryUserTradeAcctInfo(Context context,
			String ecCustNo, String tradeNo, String fundid) {
		QueryMessageDto queryMessageDto = queryServiceClient.queryUserTradeAcctInfo(context, ecCustNo,tradeNo,fundid);
		return queryMessageDto;
	}

	@Override
	public QueryMessageDto queryHomeAddressIsWordWithLinkage(Context context,
			String pmst, String pmky, String pmco, String pmv1) {
		 QueryMessageDto queryMessageDto = queryServiceClient.queryHomeAddressIsWordWithLinkage(context, pmst, 
				 pmky, pmco, pmv1);
		return queryMessageDto;
	}

	/**
	 * 根据消息标题模糊查询
	 */
	@Override
	@SuppressWarnings("unchecked")
	public JSONObject queryUserMsgLikeTitle(Context context,String cmfUserId,String title) {
		logger.info("QueryManagerImpl类【queryUserMsgLikeTitle】开始>>cmfUserId:"+cmfUserId+">>>>timestamp:" + System.currentTimeMillis());
		QueryMessageDto queryMessageDto = queryServiceClient.queryUserMessageList(context, cmfUserId, 0, 999, 999);
		List<FundReportsDto> list = new ArrayList<FundReportsDto>();
		String returnCode = "";
		String returnMsg = "";
		Integer totalNum = 0;
		JSONObject jsonObject = new JSONObject();
		if (queryMessageDto != null) {
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			list = (List<FundReportsDto>) queryMessageDto.getData();
			totalNum = NumberUtils.stringToInt(queryMessageDto.getOtherData() == null ? "0" : queryMessageDto.getOtherData().toString(), 0);
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		jsonObject.put("totalAmount", totalNum);
		String isShowTips = "N";
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				FundReportsDto dto = list.get(i);
				if(dto.getFundLName().indexOf(title) != -1){
					isShowTips = "Y";
					break;
				}
			}
		}
		jsonObject.put("isShowTips", isShowTips);
		return jsonObject;
	}

	@Override
	public JSONObject queryEducationV2(Context context, String catalogIds, int page,String name) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		int amount = ECConstants.MESSAGE_AMOUNT;// 每页展示条数
		int beginrow = (page - 1) * amount;// 开始条数
		int endrow = beginrow + amount;
		int maxPages = 1;
		logger.info("QueryManagerImpl类【queryEducationV2】开始>>>amount:"+amount+">>>beginrow:" + beginrow+">>>endrow:" + endrow+">>>timestamp:" + System.currentTimeMillis());
		// 查列表
		QueryMessageDto queryMessageDto = queryServiceClient.queryEducationV2(catalogIds, beginrow, maxPages, endrow, name);
		// 查总数
		QueryMessageDto articleCount = queryServiceClient.queryEducationCount(context, catalogIds,name);
		
		List<ArticleDtoV2> list = new ArrayList<ArticleDtoV2>();
		int count = 0;
		if(queryMessageDto != null){
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
			list = (List<ArticleDtoV2>) queryMessageDto.getData();
		}
		if(articleCount != null && articleCount.getReturnCode().equals(ECConstants.RETURN_CODE_0000)){
			count = (Integer) articleCount.getData();
		}
		maxPages = count % amount == 0 ? count / amount : count / amount + 1;
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		if(list != null){
			jsonObject.put("list", list);
		}
		if(count > 0){
			jsonObject.put("count", count);// 总记录数
			jsonObject.put("maxPages", maxPages);// 总页数
		}
		jsonObject.put("currentPage", page);// 当前页数
		return jsonObject;
	}
	
	/**
	 * 查询投资者教育附件信息及附件信息总数
	 */
	@Override
	public JSONObject queryInvestorArtivleByFileType(Context context,String fileType, int page, String name) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		int amount = ECConstants.MESSAGE_AMOUNT;// 每页展示条数
		int beginrow = (page - 1) * amount;// 开始条数
		int endrow = beginrow + amount;
		int maxPages = 1;
		logger.info("QueryManagerImpl类【queryInvestorArtivleByFileType】开始>>>amount:"+amount+">>>beginrow:" + beginrow+">>>endrow:" + endrow+">>>timestamp:" + System.currentTimeMillis());
		// 查列表
		QueryMessageDto queryMessageDto = queryServiceClient.queryInvestorArtivleByFileType(fileType, beginrow, maxPages, endrow, name);
		// 查总数
		QueryMessageDto articleCount = queryServiceClient.queryInvestorArtivleByFileTypeCount(context, fileType,name);
		
		List<InvestorFileDto> list = new ArrayList<InvestorFileDto>();
		int count = 0;
		if(queryMessageDto != null){
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
			list = (List<InvestorFileDto>) queryMessageDto.getData();
		}
		if(articleCount != null && articleCount.getReturnCode().equals(ECConstants.RETURN_CODE_0000)){
			count = (Integer) articleCount.getData();
		}
		maxPages = count % amount == 0 ? count / amount : count / amount + 1;
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		if(list != null){
			jsonObject.put("list", list);
		}
		if(count > 0){
			jsonObject.put("count", count);// 总记录数
			jsonObject.put("maxPages", maxPages);// 总页数
		}
		jsonObject.put("currentPage", page);// 当前页数
		return jsonObject;
	}

	@Override
	public JSONObject queryMgmArticleAndCount(Context context, String cmfUserId, int page,String catalogId) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		int amount = ECConstants.MESSAGE_AMOUNT;// 每页展示条数
		int beginrow = (page - 1) * amount;// 开始条数
		int endrow = beginrow + amount;
		int maxPages = 1;

		logger.info("QueryManagerImpl类【queryMgmArticleAndCount】开始>>>amount:"+amount+">>>beginrow:" + beginrow+">>>endrow:" + endrow);
		
		// 查列表
		QueryMessageDto queryMessageDto = queryServiceClient.queryMgmArticle(context, cmfUserId, beginrow, endrow,catalogId);
		// 查总数
		QueryMessageDto articleCount = queryServiceClient.queryMgmArticleCount(context, cmfUserId,catalogId);
		
		List<ArticleDtoV2> list = new ArrayList<ArticleDtoV2>();
		int count = 0;
		
		if(queryMessageDto != null){
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
			list = (List<ArticleDtoV2>) queryMessageDto.getData();
		}
		if(articleCount != null && articleCount.getReturnCode().equals(ECConstants.RETURN_CODE_0000)){
			count = (Integer) articleCount.getData();
		}

		maxPages = count % amount == 0 ? count / amount : count / amount + 1;
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		if(list != null){
			jsonObject.put("list", list);
		}
		if(count > 0){
			jsonObject.put("count", count);// 总记录数
			jsonObject.put("maxPages", maxPages);// 总页数
		}
		jsonObject.put("currentPage", page);// 当前页数

		logger.info("QueryManagerImpl类【queryMgmArticleAndCount】结束>>>returnCode:"+returnCode+">>>returnMsg:" + returnMsg+">>>count:" + count+">>>maxPages:" + maxPages);
		
		return jsonObject;
	}

	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.QueryManager#queryCompanyUserProduct(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject queryCompanyUserProduct(String fundAcco,
			String crmCustNo, String branchName) {
		JSONObject result = new JSONObject();
		logger.info("查询机构用户产品开始,接收到参数:fundAcco "+fundAcco+",crmCustNo "+crmCustNo+",branchName "+branchName);
		QueryMessageDto resultDto = queryServiceClient.queryCompanyUserProduct(fundAcco,crmCustNo,branchName);
		String returnCode = "";
		String returnMsg = "";
		List<AgentFundDto> list = null;// 全部订单
		List<AgentFundDto> gList = new ArrayList<AgentFundDto>();// 存续中订单
		List<AgentFundDto> zList = new ArrayList<AgentFundDto>();// 已到期订单
		List<AgentFundDto> wList = new ArrayList<AgentFundDto>();// 白名单产品
		if (resultDto != null) {
			returnCode = "0000".equals(resultDto.getResultCode()) ||"QRY-9003".equals(resultDto.getResultCode()) ?"0000":resultDto.getResultCode();
			returnMsg = resultDto.getResultMsg();
			list = (List<AgentFundDto>) resultDto.getData();
		}
		if (list != null && list.size() > 0) {
			if (list.get(list.size() - 1) != null && list.get(list.size() - 1).getFundnm().equals("合计(人民币)")) {
				list.remove(list.size() - 1);
			}
			for (AgentFundDto agentFundDto : list) {
				if (agentFundDto.getApplySt() != null && agentFundDto.getApplySt().equals("G")) {
					gList.add(agentFundDto);
				} else if (agentFundDto.getApplySt() != null && agentFundDto.getApplySt().equals("Z")) {
					zList.add(agentFundDto);
				}
			}
		}
		if(list == null) {
			list = new ArrayList<AgentFundDto>();
		}
		checkUserIsInWhiteList(list, wList, fundAcco);
		result.put("returnCode", returnCode);
		result.put("returnMsg", returnMsg);
		if (list != null && list.size() > 0) {
			result.put("list", list);
		}
		if (gList != null && gList.size() > 0) {
			result.put("gList", gList);
		}
		if (zList != null && zList.size() > 0) {
			result.put("zList", zList);
		}
		if(wList != null && wList.size() > 0) {
			result.put("wList", wList);
		}
		return result;
	}

	/**
	 * 进行追加
	 * @param gList
	 * @param asList
	 */
	private void addWhiteListFund(List<AgentFundDto> list,List<AgentFundDto> gList, List<String> asList) {
		if(asList == null || asList.size() == 0){
			return;
		}
		QueryMessageDto resultDto = queryServiceClient.queryEstimateByFundId(asList);
		List<Map<String,Object>> estimateList = null;
		if(resultDto != null && "0000".equals(resultDto.getReturnCode())) {
			estimateList = (List<Map<String, Object>>) resultDto.getData();
		}
		if(estimateList == null) {
			estimateList = new ArrayList<Map<String,Object>>();
		}
		
		String formatDate = DateUtils.formatDate(new Date(),DateUtils.yyyy_MM_dd);
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)+1);
		String nextDay = DateUtils.formatDate(calendar.getTime(),DateUtils.yyyy_MM_dd);
		boolean isExists = false;
		for (String fundId : asList) {
			for(int i = 0;i < list.size(); i++) {
				AgentFundDto dto = list.get(i);
				if(fundId.equals(dto.getFundid())){
					isExists = true;
					break;
				}
			}
			if(!isExists) {
				String nav = "1.00";
				for (int i = 0; i < estimateList.size(); i++) {
					Map<String, Object> map = estimateList.get(i);
					if(fundId.equals(String.valueOf(map.get("FUNCODE")))) {
						nav = (String) map.get("NET_VALUE");
					}
				}
				AgentFundDto temp = new AgentFundDto();
				temp.setApplySt("G");
				temp.setBugAmt("1.00");
				temp.setBuyDate(formatDate);
				temp.setChannleName("招商银行");
				temp.setChannleNo("");
				temp.setCycleenddt(formatDate);
				temp.setFee("");
				temp.setFundid(fundId);
				temp.setFundnm(fundId);
				temp.setHasDetail("Y");
				temp.setMaturitydate(nextDay);
				temp.setNav(nav);
				temp.setProfit("0.00");
				temp.setTypeName("whiteList");
				gList.add(temp);
				list.add(temp);
			}
			isExists = false;
		}
	}

	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.QueryManager#queryCompanyUserFundTotal(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject queryCompanyUserFundTotal(String fundAcco,
			String crmCustNo, String branchName) {
		JSONObject returnObject = new JSONObject();
		
		QueryMessageDto result = queryServiceClient.queryCompanyUserFundTotal(fundAcco,crmCustNo,branchName);
		if(result != null) {
			returnObject.put("returnCode", result.getReturnCode());
			returnObject.put("returnMsg", result.getReturnMsg());
			returnObject.put("data", result.getData());
		} else {
			returnObject.put("returnCode", "9999");
			returnObject.put("returnMsg", "系统内部错误");
		}

		return returnObject;
	}

	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.QueryManager#queryCompanyOtherDetailOrder(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject queryCompanyOtherDetailOrder(String serialno,
			String fundId,String channelNo, String fundAcco, String crmCustNo, String branchName) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		if(serialno == null){
			serialno = "";
		}
		if(fundId == null){
			fundId = "";
		}
		if(channelNo == null){
			channelNo = "";
		}
		
		logger.info("QueryManagerImpl类【queryCompanyOtherDetailOrder】开始>>>serialno:"+serialno+">>>fundAcco:" +fundAcco  + ">>>fundId:" + fundId + ">>>channelNo:" + channelNo +">>>timestamp:" + System.currentTimeMillis());
		
		
		QueryMessageDto queryMessageDto = queryServiceClient.queryCompanyOtherDetailOrder(serialno, fundId, channelNo,fundAcco, crmCustNo, branchName);
		AgentFundDto agentFundDto = null;
		if(queryMessageDto != null){
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			agentFundDto = (AgentFundDto) queryMessageDto.getData();
		}
		if(agentFundDto!=null){
			jsonObject.put("agentFundDto", agentFundDto);
			QueryMessageDto fundDetail = queryFund(null, agentFundDto.getFundid(),1);
			FundInfoDtoV2 fundInfoDtoV2 = null;
			List<QuestionDto> questionDtoList = null;
			if(fundDetail!=null){
				returnCode = queryMessageDto.getReturnCode();
				returnMsg = queryMessageDto.getReturnMsg();
				fundInfoDtoV2 =(FundInfoDtoV2)fundDetail.getData();
				
				questionDtoList = (List<QuestionDto>) fundDetail.getOtherData();//产品相关问题
			}
			if(fundInfoDtoV2 == null) {
				fundInfoDtoV2 = new FundInfoDtoV2();
				fundInfoDtoV2.setFundId(agentFundDto.getFundid());
				
			}
			if(fundInfoDtoV2 != null){
				jsonObject.put("fundInfoDtoV2", fundInfoDtoV2);
			}
			if(questionDtoList != null && questionDtoList.size() > 0){
				jsonObject.put("questionDtoList", questionDtoList);
			}
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		return jsonObject;
	}

	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.QueryManager#queryCompanyUserFundNetValue(java.lang.String, int)
	 */
	@Override
	public JSONObject queryCompanyUserFundNetValue(String fundId, int page) {
		JSONObject obj = new JSONObject();
		QueryMessageDto result = queryServiceClient.queryCompanyUserFundNetValue(fundId,page);
		if(result != null && "0000".equals(result.getResultCode())){
			obj.put("returnCode", result.getResultCode());
			obj.put("returnMsg", result.getResultMsg());
			obj.put("productEstimates", result.getData());
			obj.put("maxPages", result.getOtherData());
		} else {
			obj.put("returnCode", "9999");
			obj.put("returnMsg", "系统内部错误");
		}
		return obj;
	}

	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.QueryManager#queryCompanyUserFundNetValueByImgTable(java.lang.String, int)
	 */
	@Override
	public JSONObject queryCompanyUserFundNetValueByImgTable(String fundId,int dateTime) {
		JSONObject obj = new JSONObject();
		QueryMessageDto queryMessageDto = queryServiceClient.queryCompanyUserFundNetValueByImgTable(fundId,dateTime);
		if(queryMessageDto != null && "0000".equals(queryMessageDto.getResultCode())){
			obj.put("productEstimateList", queryMessageDto.getData());
			obj.put("returnCode", "0000");
			obj.put("returnMsg", "查询成功");
		} else {
			obj.put("returnCode", "9999");
			obj.put("returnMsg", "系统内部错误");
		}
		return obj;
	}

	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.QueryManager#queryInWhiteListFundNetValue(java.lang.String, java.lang.String, int)
	 */
	@Override
	public JSONObject queryInWhiteListFundNetValue(String paramOne,
			int type,String fundId,int page) {
		JSONObject obj = new JSONObject();
		String typeName = type==0?"机构":"个人";
		logger.info("查询白名单内产品净值开始....查询基金id为："+fundId+"当前登陆用户类型为"+typeName+"当前用户的"+(type==0?"fundAcco":"cfmUserId")+"为"+paramOne);
		String fundAcco = paramOne;
		if(type == 1){
			fundAcco = queryServiceClient.queryUserFundAccoByCmfUserId(paramOne);
			logger.info("根据当前用户的cmfuserid查询出基金账号为"+fundAcco);
			if(StringUtils.isBlank(fundAcco)) {
				logger.info("当前用户基金账号为空，不进行查询");
				obj.put("returnCode", "9998");
				obj.put("returnMsg", "当前用户基金账号为空");
				return obj;
			}
		}
		logger.info("开始根据当前登陆用户类型判断是否命中白名单，当前登陆用户类型为："+typeName + ",fundAcco为:"+fundAcco);
		
		obj = checkUserIsInWhiteListByQueryNetValue(fundId,fundAcco,1,page);
		return obj;
	}
	
	
	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.QueryManager#queryInWhiteListFundNetValue(java.lang.String, java.lang.String, int)
	 */
	@Override
	public JSONObject queryInWhiteListFundNetValueByImgTable(String paramOne,int type,String fundId,int dateTime) {
		JSONObject obj = new JSONObject();
		String typeName = type==0?"机构":"个人";
		logger.info("查询白名单内产品净值开始....查询基金id为："+fundId+"当前登陆用户类型为"+typeName+"当前用户的"+(type==0?"fundAcco":"cfmUserId")+"为"+paramOne);
		String fundAcco = paramOne;
		if(type == 1){
			fundAcco = queryServiceClient.queryUserFundAccoByCmfUserId(paramOne);
			logger.info("根据当前用户的cmfuserid查询出基金账号为"+fundAcco);
			if(StringUtils.isBlank(fundAcco)) {
				logger.info("当前用户基金账号为空，不进行查询");
				obj.put("returnCode", "9998");
				obj.put("returnMsg", "当前用户基金账号为空");
				return obj;
			}
		}
		logger.info("开始根据当前登陆用户类型判断是否命中白名单，当前登陆用户类型为："+typeName + ",fundAcco为:"+fundAcco);
		
		obj = checkUserIsInWhiteListByQueryNetValue(fundId,fundAcco,0,dateTime);
		return obj;
	}

	private JSONObject checkUserIsInWhiteListByQueryNetValue(String fundId,String fundAcco,int flag,int param) {
		JSONObject obj = new JSONObject();
		/* 白名单检测    S*/
		logger.info("开始进行对当前登陆用户进行白名单检测.");
		QueryMessageDto fundAccoDto = queryHomeAddressIsWordWithLinkage(new Context(), "SYSTEM", "NVWL01", null, null);
		List<ParameterDto> fundAccoParamList= (List<ParameterDto>) fundAccoDto.getData();
		if(fundAccoParamList == null || fundAccoParamList.size() == 0) {
			logger.info("基金账号白名单未做配置，不进行查询");
			obj.put("returnCode", "9999");
			obj.put("returnMsg","当前用户未配置在白名单内");
			return obj;
		}
		ParameterDto fundAccoParam = fundAccoParamList.get(0);
		if(fundAccoParam == null) {
			logger.info("基金账号白名单未做配置，不进行查询");
			obj.put("returnCode", "9999");
			obj.put("returnMsg","当前用户未配置在白名单内");
			return obj;
		}
		String fundAccoStr = fundAccoParam.getPmco();
		logger.info("查询出来的基金账号白名单为" +fundAccoStr);
		if(StringUtils.isBlank(fundAccoStr)) {
			logger.info("基金账号白名单未做配置，不进行查询");
			obj.put("returnCode", "9999");
			obj.put("returnMsg","当前用户未配置在白名单内");
			return obj;
		}
		List<String> fundAccoList = Arrays.asList(fundAccoStr.split(","));
		if(fundAccoList.contains(fundAcco)) {
			logger.info("当前登陆用户命中白名单,开始判断fundId是否存在对应白名单内 当前登录用户fundAcoo为 "+fundAcco +",需判断的fundId为："+fundId);
			QueryMessageDto fundIdDto = queryHomeAddressIsWordWithLinkage(new Context(), "SYSTEM", "NVWL02", null, null);
			List<ParameterDto> fundIdParamist= (List<ParameterDto>) fundIdDto.getData();
			
			if(fundIdParamist == null || fundIdParamist.size() == 0) {
				logger.info("基金代码名单未做配置，不进行查询");
				obj.put("returnCode", "9999");
				obj.put("returnMsg","当前查询基金未配置在白名单内");
				return obj;
			}
			ParameterDto fundIdParam = fundIdParamist.get(0);
			if(fundIdParam == null) {
				logger.info("基金代码名单未做配置，不进行查询");
				obj.put("returnCode", "9999");
				obj.put("returnMsg","当前查询基金未配置在白名单内");
				return obj;
			}
			String fundIdStr = fundIdParam.getPmco();
			if(StringUtils.isBlank(fundIdStr)) {
				logger.info("基金代码名单未做配置，不进行查询");
				obj.put("returnCode", "9999");
				obj.put("returnMsg","当前查询基金未配置在白名单内");
				return obj;
			}
			
			logger.info("查询出白名单产品列表id为 "+fundIdStr +"需要进行净值查询的fundId为："+fundId);
			List<String> fundIdList = Arrays.asList(fundIdStr.split(","));
			if(fundIdList.contains(fundId)) {
				logger.info("需查询净值的fundId命中白名单，开始查询净值");
				QueryMessageDto resultDto = null;
				if(flag == 0) {
					resultDto = queryServiceClient.queryCompanyUserFundNetValueByImgTable(fundId, param);
					if(resultDto != null && "0000".equals(resultDto.getResultCode())){
						obj.put("productEstimateList", resultDto.getData());
						obj.put("returnCode", "0000");
						obj.put("returnMsg", "查询成功");
					} else {
						obj.put("returnCode", "9999");
						obj.put("returnMsg", "系统内部错误");
					}
				} else {
					resultDto = queryServiceClient.queryCompanyUserFundNetValue(fundId, param);
					if(resultDto != null && "0000".equals(resultDto.getResultCode())){
						obj.put("returnCode",resultDto.getResultCode());
						obj.put("returnMsg", resultDto.getResultMsg());
						obj.put("productEstimates", resultDto.getData());
						obj.put("maxPages", resultDto.getOtherData());
						obj.put("page", param);
					} else {
						obj.put("returnCode", "9999");
						obj.put("returnMsg", "系统内部错误");
					}
				}
			} else {
				logger.info("需查询净值的fundId为命中白名单，不查询净值 ，当前登陆用户的fundAcco为"+fundAcco+",需查询净值的fundId为:"+fundId +"，净值白名单产品列表值为:"+fundIdStr);
				obj.put("returnCode", "9998");
				obj.put("returnMsg", "当前用户查询产品未在白名单内");
			}
		} else {
			logger.info("当前登陆用户未命中白名单，不进行查询 当前登录用户fundAcoo为"+fundAcco+",净值白名单基金账号列表为:"+fundAccoStr);
			obj.put("returnCode", "9997");
			obj.put("returnMsg", "当前用户未在白名单内");
		}
		return obj;
	}
	@Override
	public JSONObject queryOriginalArticleAndCount(Context context, String cmfUserId, int page,String name) {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		
		int amount = ECConstants.MESSAGE_AMOUNT;// 每页展示条数
		int beginrow = (page - 1) * amount;// 开始条数
		int endrow = beginrow + amount;
		int maxPages = 1;

		logger.info("QueryManagerImpl类【queryOriginalArticleAndCount】开始>>>amount:"+amount+">>>beginrow:" + beginrow+">>>endrow:" + endrow);
		
		// 查列表
		QueryMessageDto queryMessageDto = queryServiceClient.queryOriginalArticle(context, cmfUserId, beginrow, endrow, name);
		// 查总数
		QueryMessageDto articleCount = queryServiceClient.queryOriginalArticleCount(context, cmfUserId, name);
		
		List<ArticleDtoV2> list = new ArrayList<ArticleDtoV2>();
		int count = 0;
		
		if(queryMessageDto != null){
			returnCode = queryMessageDto.getReturnCode();
			returnMsg = queryMessageDto.getReturnMsg();
			list = (List<ArticleDtoV2>) queryMessageDto.getData();
		}
		if(articleCount != null && articleCount.getReturnCode().equals(ECConstants.RETURN_CODE_0000)){
			count = (Integer) articleCount.getData();
		}

		maxPages = count % amount == 0 ? count / amount : count / amount + 1;
		
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		if(list != null){
			jsonObject.put("list", list);
		}
		if(count > 0){
			jsonObject.put("count", count);// 总记录数
			jsonObject.put("maxPages", maxPages);// 总页数
		}
		jsonObject.put("currentPage", page);// 当前页数

		logger.info("QueryManagerImpl类【queryOriginalArticleAndCount】结束>>>returnCode:"+returnCode+">>>returnMsg:" + returnMsg+">>>count:" + count+">>>maxPages:" + maxPages);
		
		return jsonObject;
	}
	
	/* (non-Javadoc)
	 * @see com.cmwa.ec.weixin.manager.business.QueryManager#queryCanRedeemBankInfoByFundCode(java.lang.String, java.lang.String)
	 */
	@Override
	public JSONObject queryCanRedeemBankInfoByFundCode(String fundid,
			String custno) {
		JSONObject json = new JSONObject();
		try {
			QueryMessageDto dto = queryServiceClient.queryCanRedeemBankInfoByFundCode(fundid,custno);
			logger.info("queryCanRedeemBankInfoByFundCode -> query服务返回值为："+dto);
			if(dto != null && "0000".equals(dto.getErrCode())) {
				List<RedemmBalanceDto> data = (List<RedemmBalanceDto>) dto.getData();
				// 隐藏卡号
				if(data != null) {
					for (RedemmBalanceDto content : data) {
						content.setBankaccodisplay(com.cmwa.ec.webapp.util.StringUtils.replaceToNo(content.getBankaccodisplay(), "****", 4, 4));
					}
				}
				json.put("data", dto.getData());
				json.put("resultCode", "0000");
				json.put("resultMsg", "成功！");
			} else {
				json.put("returnCode", dto.getErrCode());
				json.put("returnMsg",dto.getErrMsg());
			}
		} catch (Exception e) {
			logger.error("queryCanRedeemBankInfoByFundCode -> 捕获到异常:" + e);
			json.put("resultCode", "9999");
			json.put("resultMsg", "系统异常");
		}
		return json;
	}

    @Override
    public JSONObject queryBondInvestorArticleAndCount(Context context, String cmfUserId, int page, String name) {
        JSONObject jsonObject = new JSONObject();
        String returnCode = "";
        String returnMsg = "";
        // 每页展示条数
        int amount = ECConstants.MESSAGE_AMOUNT;
        // 开始条数
        int beginrow = (page - 1) * amount;
        int endrow = beginrow + amount;
        int maxPages = 1;

        logger.info("QueryManagerImpl类【queryBondInvestorArticleAndCount】开始>>>amount:" + amount + ">>>beginrow:" +
            beginrow + ">>>endrow:" + endrow + ">>>timestamp:" + System.currentTimeMillis());

        // 查列表
        QueryMessageDto queryMessageDto = queryServiceClient.queryBondInvestorArticle(context, cmfUserId, beginrow, endrow, name);
        // 查总数
        QueryMessageDto articleCount = queryServiceClient.queryBondInvestorCount(context, cmfUserId, name);

        List<ArticleDtoV2> list = new ArrayList<ArticleDtoV2>();

        if (null != queryMessageDto) {
            returnCode = queryMessageDto.getReturnCode();
            returnMsg = queryMessageDto.getReturnMsg();
            list = (List<ArticleDtoV2>) queryMessageDto.getData();
        }

        int count = 0;
        if (null != articleCount && ECConstants.RETURN_CODE_0000.equals(articleCount.getReturnCode())) {
            count = (Integer) articleCount.getData();
        }

        maxPages = count % amount == 0 ? count / amount : count / amount + 1;

        jsonObject.put("returnCode", returnCode);
        jsonObject.put("returnMsg", returnMsg);

        if (null != list) {
            jsonObject.put("list", list);
        }

        if (count > 0) {
            // 总记录数
            jsonObject.put("count", count);
            // 总页数
            jsonObject.put("maxPages", maxPages);
        }
        // 当前页数
        jsonObject.put("currentPage", page);

        logger.info("QueryManagerImpl类【queryBondInvestorArticleAndCount】结束>>>returnCode:" + returnCode + ">>>returnMsg:" + returnMsg + ">>>count:" + count + ">>>maxPages:" + maxPages + ">>>timestamp:" + System.currentTimeMillis());

        return jsonObject;
    }

    
	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.QueryManager#queryQualifiedUserInfoByIdcard(java.lang.String)
	 */
	@Override
	public JSONObject queryQualifiedUserInfoByIdno(String idno) {
		JSONObject obj = new JSONObject();
		if(StringUtils.isBlank(idno)) {
			obj.put("returnCode","9999");
			obj.put("returnMsg",ECConstants.RETURN_MSG_9008);
		} else {
			try{
				logger.info("根据身份证号查询合格投资者信息开始. idno:"+idno);
				QueryMessageDto queryMessageDto = queryServiceClient.queryQualifiedUserInfoByIdno(idno);
				obj.put("returnCode",ECConstants.RETURN_CODE_9999);
				if(queryMessageDto != null) {
					logger.info("调用query服务查询合格投资者信息成功，resultData:"+queryMessageDto);
					if(queryMessageDto == null || !ECConstants.RETURN_CODE_0000.equals(queryMessageDto.getResultCode())) {
						logger.info("query服务返回结果异常，不返回数据");
						obj.put("returnMsg","服务调用异常");
					} else {
						@SuppressWarnings("unchecked")
						List<QualifiedUserInfoDto> result = (List<QualifiedUserInfoDto>) queryMessageDto.getData();
						logger.info("根据idno："+idno+"查询到合格投资者信息为:"+result);
						obj.put("returnCode",ECConstants.RETURN_CODE_0000);
						obj.put("returnMsg",ECConstants.RETURN_MSG_0000);
						obj.put("data",result);
					}
				} else {
					logger.info("调用query服务查询合格投资者信息失败，返回值为空!");
					obj.put("returnMsg",ECConstants.RETURN_MSG_9999);
				}
			} catch(Exception e) {
				logger.error("查询合格投资者信息时捕获异常：",e);
				obj.put("returnMsg","服务器内部异常");
			}
		}
		return obj;
	}
	
	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.QueryManager#queryFileUploadRecord(java.lang.String)
	 */
	@Override
	public JSONObject queryFileUploadRecord(String cmfUserId) {
		try{
			// 声明参数
			CmwaFileUploadRecordDto parameter = new CmwaFileUploadRecordDto();
			parameter.setCmfuserid(cmfUserId);
			List<CmwaFileUploadRecordDto> queryFileUploadRecord = tradeServiceClient.queryFileUploadRecord(parameter);
			JSONObject returnObj = new JSONObject();
			returnObj.put("returnCode",ECConstants.RETURN_CODE_0000);
			returnObj.put("returnMsg",ECConstants.RETURN_MSG_0000);
			returnObj.put("data",queryFileUploadRecord);
			return returnObj;
		} catch(Exception e) {
			logger.error("查询文件上传记录时捕获异常:",e);
			JSONObject returnObj = new JSONObject();
			returnObj.put("returnCode",ECConstants.RETURN_CODE_9999);
			returnObj.put("returnMsg",ECConstants.RETURN_MSG_9999);
			return returnObj;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.cmwa.ec.webapp.manager.QueryManager#queryAccreditedInvestorConditions(java.lang.String)
	 */
	@Override
	public JSONObject queryAccreditedInvestorConditions(String cmfuserid,String custno) {
		JSONObject returnObj = new JSONObject();
		try{
			QueryMessageDto result = queryServiceClient.queryAccreditedInvestorConditions(custno);
			if(result == null || ECConstants.RETURN_CODE_9999.equals(result.getResultCode())) {
				logger.info("调用query服务查询合格投资者条件失败，custno:"+custno);
				returnObj.put("returnCode",ECConstants.RETURN_CODE_9999);
				returnObj.put("returnMsg",ECConstants.RETURN_MSG_9999);
			} else {
				logger.info("调用query服务查询合格投资者条件成功，custno:"+custno);
				returnObj.put("returnCode",ECConstants.RETURN_CODE_0000);
				returnObj.put("returnMsg",ECConstants.RETURN_MSG_0000);
				returnObj.put("data", result.getOtherData());
				autoHandleData((Map<String, Object>) result.getOtherData(),custno,cmfuserid);
			}
		}catch(Exception e) {
			logger.error("调用query服务查询合格投资者条件捕获异常：",e);
			returnObj.put("returnCode",ECConstants.RETURN_CODE_9999);
			returnObj.put("returnMsg",ECConstants.RETURN_MSG_9999);
		}
		return returnObj;
	}
	
	private void autoHandleData(Map<String,Object> otherData,String custno,String cmfuserid) {
		try{
		    boolean financialCertificate = Boolean.valueOf(String.valueOf(otherData.get("financialCertificate")));
		    boolean investCertificate = Boolean.valueOf(String.valueOf(otherData.get("investCertificate")));
		    if(investCertificate && financialCertificate) {
		    	QueryMessageDto serviceResult = this.queryServiceClient.queryQualifiedUserInfoByCustno(custno);
		    	if(serviceResult != null && ECConstants.RETURN_CODE_0000.equals(serviceResult.getResultCode())) {
		    		if(serviceResult.getData() == null) {
		    			logger.info("判断到用户已经满足合格投资者条件，为用户添加一条成功的合格投资者申请， custno:"+custno);
		    			CmwaQualifiedUserInfoDto innerRecord = new CmwaQualifiedUserInfoDto();
				    	innerRecord.setInfoId(UUID.randomUUID().toString());
				    	innerRecord.setCmfuserid(cmfuserid);
				    	innerRecord.setCustno(custno);
				    	innerRecord.setCreatedUser(this.getClass().getSimpleName());
				    	innerRecord.setUpdatedUser(this.getClass().getSimpleName());
				    	logger.info("为custno:"+custno+"的用户生成的添加参数为:"+innerRecord);
				    	int addQualifiedUserInfo = this.tradeServiceClient.insertAccreditedInvestorInfo(getTradeQualifiedDto(innerRecord), "S");
				    	logger.info("custno:"+custno+"用户的合格投资者申请记录返回结果为："+addQualifiedUserInfo);
		    		} else {
		    			logger.info("判断到用户已经满足合格投资者条件，将用户当前合格投资者申请修改为成功， custno:"+custno);
		    			CmwaQualifiedUserInfoStatusDto status = new CmwaQualifiedUserInfoStatusDto();
		    			QualifiedUserInfoDto qualifiedUserInfoDto = (QualifiedUserInfoDto) serviceResult.getData();
		    			status.setInfoId(qualifiedUserInfoDto.getInfoId());
		    			int updateQualifiedUserInfoStatus = this.tradeServiceClient.updateQualifiedUserInfoStatus(getTradeQualifiedStatusDto(status), "S", this.getClass().getSimpleName());
		    			logger.info("custno:"+custno+"用户的合格投资者申请记录返回结果为："+updateQualifiedUserInfoStatus);			    			
		    		}
		    	} else {
		    		logger.error("调用query服务失败，返回数据为："+serviceResult);
		    	}
		    }
		} catch (Exception e) {
			logger.error("自动修改用户合格投资者申请状态时捕获异常：",e);
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
	public List<ParameterDto> queryParameter(Context context, String pmst, String pmky, String pmco, String pmv1) {
		try {
			QueryMessageDto queryResult = queryServiceClient.queryParameter(context, pmst, pmky, pmco, pmv1);
			if (ECConstants.COMMON_SUCCESS.equals(queryResult.getReturnCode())) {
				// noinspection unchecked
				return (List<ParameterDto>) queryResult.getData();
			} else {
				logger.error("queryParameter_开关查询失败_errCode: " + queryResult.getReturnCode()
					+ ", errMsg: " + queryResult.getReturnMsg());
			}
		} catch (Exception omg) {
			logger.error("queryParameter_开关查询异常_", omg);
		}
		return null;
	}

	@Override
	public CustserviceInfoDTO getConsultantInfo(String cmfUserId) {
		CustserviceInfoDTO custserviceInfoDTO = null;
		try {
			QueryMessageDto queryResult = queryServiceClient.getConsultantInfo(cmfUserId);
			if (ECConstants.COMMON_SUCCESS.equals(queryResult.getResultCode())) {
				custserviceInfoDTO = (CustserviceInfoDTO) queryResult.getData();
			} else {
				logger.error("getConsultantInfo_获取用户的专属顾问信息失败_errCode: " + queryResult.getReturnCode()
					+ ", errMsg: " + queryResult.getReturnMsg());
			}
		} catch (Exception omg) {
			logger.error("getConsultantInfo_获取用户的专属顾问信息时异常_", omg);
		}

		return custserviceInfoDTO;
	}
	@Override
	public JSONObject queryRiskTermList(Context context, String cmfUserId,
			String fundId, String period) {
		JSONObject returnObj = new JSONObject();
		try{
			QueryMessageDto result = queryServiceClient.queryHomeAddressIsWordWithLinkage(context, ECConstants.PMST,ECConstants.RISK_TERMS , null, null);
			if(null != result && ECConstants.COMMON_SUCCESS.equals(result.getResultCode())) {
				logger.info("调用query服务查询风险揭示函成功");
				List<ParameterDto> list = (List<ParameterDto>)result.getData();
				if(null != list && !list.isEmpty()){
					List<RiskTermsDto> termsList = new ArrayList<RiskTermsDto>();
					for(ParameterDto dto : list){
						RiskTermsDto term = new RiskTermsDto();
						term.setId(dto.getPmnm());
						term.setContent(dto.getPmco());
						for(UserTermsDto d : queryUserTermList(cmfUserId,fundId,period)){
							if(dto.getPmnm().equals(d.getTermsId())){
								term.setState(d.getState());
								break;
							}
						}
						termsList.add(term);
					}
					Collections.sort(termsList, new Comparator<RiskTermsDto>(){
						@Override
						public int compare(RiskTermsDto arg0, RiskTermsDto arg1) {
							return Integer.valueOf(arg0.getId()) - Integer.valueOf(arg1.getId());
						}
					});
					returnObj.put("returnCode",ECConstants.RETURN_CODE_0000);
					returnObj.put("returnMsg",ECConstants.RETURN_MSG_0000);
					returnObj.put("data", termsList);
				}else{
					logger.info("调用query服务查询结果异常，未查询到可用数据");
					returnObj.put("returnCode",ECConstants.RETURN_CODE_9001);
					returnObj.put("returnMsg",ECConstants.RETURN_MSG_9001);
				}
			} else {
				logger.info("调用query服务查询风险揭示函失败");
				returnObj.put("returnCode",result.getErrCode());
				returnObj.put("returnMsg",result.getErrMsg());
			}
		}catch(Exception e) {
			logger.error("查询风险揭示函异常：",e);
			returnObj.put("returnCode",ECConstants.RETURN_CODE_9999);
			returnObj.put("returnMsg",ECConstants.RETURN_MSG_9999);
		}
		return returnObj;
	}
	
	public List<UserTermsDto> queryUserTermList(String cmfUserId, String fundId ,String period){
		try{
			QueryMessageDto result = queryServiceClient.queryUserTermList(null, cmfUserId, fundId, StringUtils.isEmpty(period)?1:Integer.valueOf(period),"1");
			if(null != result && ECConstants.COMMON_SUCCESS.equals(result.getResultCode())) {
				logger.info("调用query服务查询用户风险揭示函勾选列表成功");
				return (List<UserTermsDto>)result.getData();
			} else {
				logger.info("调用query服务查询用户风险揭示函勾选列表失败");
				return null;
			}
		}catch(Exception e) {
			logger.error("调用query服务查询用户风险揭示函勾选列表失败异常：",e);
			return null;
		}
	}
}
	

