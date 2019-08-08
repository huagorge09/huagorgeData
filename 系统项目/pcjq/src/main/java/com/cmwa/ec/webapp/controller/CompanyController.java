package com.cmwa.ec.webapp.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.fund.FundReportsDto;
import com.cmwa.ec.trade.facade.dto.OrderResult;
import com.cmwa.ec.webapp.manager.CompanyManager;
import com.cmwa.ec.webapp.manager.QueryManager;
import com.cmwa.ec.webapp.manager.TradeManager;
import com.cmwa.ec.webapp.util.ContextUtils;
import com.cmwa.ec.webapp.util.ECConstants;
import com.cmwa.ec.webapp.util.RequestHelper;
 

/**
 * 
 * 接口/类的功能说明：对公业务(信批)controller
 * @author maj
 * @version 1.0
 * @see 
 *
 * <p>History</p>
 * 2015年11月16日
 */
@Controller("CompanyController")
@RequestMapping(value = "/AppService")
public class CompanyController {
	
	private static Logger logger = Logger.getLogger(CompanyController.class.getName());
	
	@Autowired
	private CompanyManager companyManager;
	@Autowired
	private QueryManager queryManager;
	@Autowired
	private TradeManager tradeManager;
	/**
	 * 对公用户 登录  根据营业执照名称、基金账号、营业执照注册号登录
	 * @param response
	 * @param request
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/setUp/branchUserLogin.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String branchUserLogin(HttpServletResponse response,HttpServletRequest request) throws UnsupportedEncodingException, IOException  {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		String crmCustNo = "";
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
		//正常登录清除session中的type
		request.getSession().removeAttribute("type");
	    logger.info("CompanyController类【branchUserLogin】开始>>>cmfUserId:"+cmfUserId+">>>seqId:"+seqId+">>>timestamp:"+System.currentTimeMillis());
		String branchName = request.getParameter("branchName");
		if(branchName != null && !branchName.equals("")){
			branchName = URLDecoder.decode(branchName, "utf-8");
		}
		String fundAcco = request.getParameter("fundAcco");
		String branchLicense = request.getParameter("branchLicense");
		if(branchLicense != null && !branchLicense.equals("")){
			branchLicense = URLDecoder.decode(branchLicense, "utf-8");
		}
		String vrfCode = request.getParameter("vrfCode");
	    logger.info("CompanyController类【branchUserLogin】获取前台数据>>>branchName:"+branchName+">>>fundAcco:"+fundAcco+">>>branchLicense:"+branchLicense);
	    //日志信息
	    Context context=ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		/********************验证图片验证码 B********************************/
		Map<String, String> map = VerifyCodeController.verifyRandom(request, vrfCode);
		if(map == null || !map.get("returnCode").equals(ECConstants.RETURN_CODE_0000)){
			returnCode = map.get("returnCode");
			returnMsg = map.get("returnMsg");
			jsonObject.put("returnCode", returnCode);
			jsonObject.put("returnMsg", returnMsg);
			return jsonObject.toString();
		}
		/********************验证图片验证码 E********************************/
	    QueryMessageDto queryMessageDto = companyManager.branchUserLogin(context,branchName,fundAcco,branchLicense);
		if(queryMessageDto!=null){
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			Object obj = queryMessageDto.getData();
			if(returnCode != null && returnCode.equals("0000")){
				crmCustNo = (String) obj;
				// 将fundAcco和crmCustNo存放到session中
				request.getSession(true).setAttribute("fundAcco", fundAcco);
				request.getSession(true).setAttribute("crmCustNo", crmCustNo);
				request.getSession(true).setAttribute("branchName", branchName);
			}
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("CompanyController类【branchUserLogin】结束>>>returnJsonObject="+jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * ABS白名单用户:营业执照注册号登录
	 * @param response
	 * @param request
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/setUp/absUserLogin.xhtml", produces="text/html;charset=UTF-8" ,method = {RequestMethod.POST})
	@ResponseBody
	public String absUserLogin(HttpServletResponse response,HttpServletRequest request) throws UnsupportedEncodingException, IOException  {
		JSONObject jsonObject = new JSONObject();
		String returnCode = "";
		String returnMsg = "";
		String crmCustNo = "";
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);
	    logger.info("CompanyController类【absUserLogin】开始>>>cmfUserId:"+cmfUserId+">>>seqId:"+seqId+">>>timestamp:"+System.currentTimeMillis());
		String branchName = request.getParameter("branchName");
		if(branchName != null && !branchName.equals("")){
			branchName = URLDecoder.decode(branchName, "utf-8");
		}
		String branchLicense = request.getParameter("branchLicense");
		String type = request.getParameter("type");
		if(branchLicense != null && !branchLicense.equals("")){
			branchLicense = URLDecoder.decode(branchLicense, "utf-8");
		}
		String vrfCode = request.getParameter("vrfCode");
	    logger.info("CompanyController类【absUserLogin】获取前台数据>>>branchName:"+branchName+">>>branchLicense:"+branchLicense);
	    //日志信息
	    Context context=ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		/********************验证图片验证码 B********************************/
		Map<String, String> map = VerifyCodeController.verifyRandom(request, vrfCode);
		if(map == null || !map.get("returnCode").equals(ECConstants.RETURN_CODE_0000)){
			returnCode = map.get("returnCode");
			returnMsg = map.get("returnMsg");
			jsonObject.put("returnCode", returnCode);
			jsonObject.put("returnMsg", returnMsg);
			return jsonObject.toString();
		}
		/********************验证图片验证码 E********************************/
	    QueryMessageDto queryMessageDto = companyManager.absUserLogin(context,branchName,type,branchLicense);
		if(queryMessageDto!=null){
			returnCode = queryMessageDto.getResultCode();
			returnMsg = queryMessageDto.getResultMsg();
			if(returnCode != null && returnCode.equals("0000")){
				request.getSession(true).setAttribute("branchName", branchName);
				request.getSession(true).setAttribute("type", type);
				request.getSession(true).setAttribute("branchLicense", branchLicense);
			}
		}
		jsonObject.put("returnCode", returnCode);
		jsonObject.put("returnMsg", returnMsg);
		logger.info("CompanyController类【absUserLogin】结束>>>returnJsonObject="+jsonObject.toString());
		return jsonObject.toString();
	}
	
	/**
	 * 查询用户产品信息列表 信披
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             String
	 * @author luos
	 */
	@RequestMapping(value = "/setUp/queryUserMessageListForCompany.xhtml", produces="text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String queryUserMessageListForCompany(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String type = (String) request.getSession(true).getAttribute("type");
		String seqId = RequestHelper.getSeqId(request);
		String fundAcco = (String) request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String) request.getSession(true).getAttribute("crmCustNo");
		JSONObject returnJsonObject = new JSONObject();
		if(!ECConstants.SESSION_USERTYPE.equals(type)){
			if(fundAcco == null || fundAcco.equals("") || crmCustNo == null || crmCustNo.equals("")){
				returnJsonObject.put("returnCode", "8000");
				returnJsonObject.put("returnMsg", "请先登录。");
				return returnJsonObject.toString();
			}
		}
		String tempPage = request.getParameter("page");
		int page = 1;
		if (tempPage != null && !tempPage.equals("")) {
			page = Integer.parseInt(tempPage);
		}
		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		returnJsonObject = companyManager.queryUserMessageListForCompany(context, request);
		returnJsonObject.put("currentPage", page);
		return returnJsonObject.toString();
	}
	/**
	 * 查询用户是否已经登录
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             String
	 * @author luos
	 */
	@RequestMapping(value = "/setUp/checkCompanyUser.xhtml", produces="text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET })
	@ResponseBody
	public String checkCompanyUser(HttpServletResponse response, HttpServletRequest request) throws Exception {
		String fundAcco = (String) request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String) request.getSession(true).getAttribute("crmCustNo");
		String branchName = (String) request.getSession(true).getAttribute("branchName");
		String type = (String) request.getSession(true).getAttribute("type");
		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		String fundAccoEncry ="";
		if((fundAcco == null || fundAcco.equals("") || crmCustNo == null || crmCustNo.equals("")) && !ECConstants.SESSION_USERTYPE.equalsIgnoreCase(type)){
			resultCode = "9000";
			resultMsg = "未登录";
		}else{
			resultCode = "0000";
			resultMsg = "已登录";
			fundAccoEncry = "您好！"+RequestHelper.getEncryptPublicUserName(branchName);
		}
		returnJsonObject.put("fundAccoEncry", fundAccoEncry);
		returnJsonObject.put("returnCode", resultCode);
		returnJsonObject.put("returnMsg", resultMsg);
		returnJsonObject.put("type", type);
		return returnJsonObject.toString();
	}
	/**
	 * 查询用户产品信息详情 信披
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author maj
	 */
	@RequestMapping(value = "/setUp/queryUserMessageForCompany.xhtml", method = { RequestMethod.POST, RequestMethod.GET })
	public void queryUserMessageForCompany(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);

		String msgId = request.getParameter("reportId");
		String msgType = "1";

		logger.info("CompanyController类【queryUserMessageForCompany 】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:"
				+ System.currentTimeMillis());

		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
				cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

		logger.info("CompanyController类【queryUserMessageForCompany 】获取页面传递数据>>>msgId:" + msgId + ">>>msgType:" + msgType);

		String fundAcco = (String) request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String) request.getSession(true).getAttribute("crmCustNo");
		
		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		
		if(fundAcco == null || fundAcco.equals("") || crmCustNo == null || crmCustNo.equals("")){
			
			returnJsonObject.put("returnCode", "9000");
			returnJsonObject.put("returnMsg", "请先登录。");
			
			response.getOutputStream().print(returnJsonObject.toString());
			return ;
		}

		QueryMessageDto queryMessageDto = companyManager.queryUserMessageForCompany(context, fundAcco, msgId, msgType);
		List<FundReportsDto> fundReportsDtoList = new ArrayList<FundReportsDto>();
		if (queryMessageDto != null) {
			fundReportsDtoList = (List<FundReportsDto>) queryMessageDto.getData();
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();

			if (resultCode != null && resultCode.equals("0000")) {
				returnJsonObject.put("fundReportsDtoList", fundReportsDtoList);
			}
		}

		returnJsonObject.put("resultCode", resultCode);
		returnJsonObject.put("resultMsg", resultMsg);

		logger.info("CompanyController类【queryUserMessageForCompany 】结束>>>returnJsonObject=" + returnJsonObject.toString());
		response.getOutputStream().print(returnJsonObject.toString());
	}

	/**
	 * 查询用户最新公告
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author maj
	 */
	@RequestMapping(value = "/setUp/queryUserMessageListByNewTimeForCompany.xhtml", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void queryUserMessageListByNewTimeForCompany(HttpServletResponse response, HttpServletRequest request)
			throws Exception {

		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);

		logger.info("CompanyController类【queryUserMessageListByNewTimeForCompany 】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:"
				+ System.currentTimeMillis());

		int beginIdx = 0;
		int amount = Integer.parseInt(ECConstants.PAGE_NEWS_AMOUNT);

		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
				cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

		String fundAcco = (String) request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String) request.getSession(true).getAttribute("crmCustNo");
		
		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		
		if(fundAcco == null || fundAcco.equals("") || crmCustNo == null || crmCustNo.equals("")){
			
			
			returnJsonObject.put("returnCode", "9000");
			returnJsonObject.put("returnMsg", "请先登录。");
			
			response.getOutputStream().print(returnJsonObject.toString());
			return ;
		}

		QueryMessageDto queryMessageDto = companyManager.queryUserMessageListByNewTimeForCompany(context, fundAcco, beginIdx,
				amount);
		List<FundReportsDto> fundReportsDtoList = new ArrayList<FundReportsDto>();
		if (queryMessageDto != null) {
			fundReportsDtoList = (List<FundReportsDto>) queryMessageDto.getData();
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();
			if (resultCode != null && resultCode.equals("0000")) {
				returnJsonObject.put("fundReportsDtoList", fundReportsDtoList);
			}
		}

		returnJsonObject.put("resultCode", resultCode);
		returnJsonObject.put("resultMsg", resultMsg);

		logger.info("CompanyController类【queryUserMessageListByNewTimeForCompany 】结束>>>returnJsonObject="
				+ returnJsonObject.toString());
		response.getOutputStream().print(returnJsonObject.toString());
	}

	/**
	 * 根据产品id查询该产品的相关公告
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author maj
	 */
	@RequestMapping(value = "/setUp/queryUserMessageListByFundIdForCompany.xhtml", method = { RequestMethod.POST,
			RequestMethod.GET })
	public void queryUserMessageListByFundIdForCompany(HttpServletResponse response, HttpServletRequest request) throws Exception {

		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		String seqId = RequestHelper.getSeqId(request);

		logger.info("CompanyController类【queryUserMessageListByFundIdForCompany 】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:"
				+ System.currentTimeMillis());

		int beginIdx = 0;
		int amount = Integer.parseInt(ECConstants.PAGE_USER_PRODUCT_AMOUNT);
		String fundId = request.getParameter("fundId");

		logger.info("CompanyController类【queryUserMessageListByFundIdForCompany 】传递到后台的参数>>>beginIdx:" + beginIdx + ">>>amount:"
				+ amount + ">>>fundId:" + fundId);

		// 日志信息
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP,
				cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

		String fundAcco = (String) request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String) request.getSession(true).getAttribute("crmCustNo");
		
		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		
		if(fundAcco == null || fundAcco.equals("") || crmCustNo == null || crmCustNo.equals("")){
			
			
			returnJsonObject.put("returnCode", "9000");
			returnJsonObject.put("returnMsg", "请先登录。");
			
			response.getOutputStream().print(returnJsonObject.toString());
			return ;
		}

		QueryMessageDto queryMessageDto = companyManager.queryUserMessageListByFundIdForCompany(context, fundAcco, fundId,
				beginIdx, amount);
		List<FundReportsDto> fundReportsDtoList = new ArrayList<FundReportsDto>();
		if (queryMessageDto != null) {
			fundReportsDtoList = (List<FundReportsDto>) queryMessageDto.getData();
			resultCode = queryMessageDto.getResultCode();
			resultMsg = queryMessageDto.getResultMsg();
			if (resultCode != null && resultCode.equals("0000")) {
				returnJsonObject.put("fundReportsDtoList", fundReportsDtoList);
			}
		}

		returnJsonObject.put("resultCode", resultCode);
		returnJsonObject.put("resultMsg", resultMsg);

		logger.info("CompanyController类【queryUserMessageListByFundIdForCompany 】结束>>>returnJsonObject="
				+ returnJsonObject.toString());
		response.getOutputStream().print(returnJsonObject.toString());
	}
	
	/**
	 * 退出登录
	 * @param response
	 * @param request
	 * @throws Exception
	 * 			void
	 * @author maj
	 */
	@RequestMapping(value = "/setUp/logoutForCompany.xhtml", method = {RequestMethod.POST,RequestMethod.GET})
	public String logoutForCompany(HttpServletResponse response,HttpServletRequest request) throws Exception  {

		JSONObject returnJsonObject = new JSONObject();
		String resultCode = "";
		String resultMsg = "";
		
		try {
			/*request.getSession(true).invalidate();*/
			request.getSession(true).removeAttribute("fundAcco");
			request.getSession(true).removeAttribute("crmCustNo");
			request.getSession(true).removeAttribute("branchName");
			resultCode = "0000";
			resultMsg = "退出登录成功";
		} catch (Exception e) {
			resultCode = "9999";
			resultMsg = "退出登录失败";
		}
		returnJsonObject.put("returnCode", resultCode);
		returnJsonObject.put("returnMsg", resultMsg);
		return returnJsonObject.toString();
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
	@RequestMapping(value = "/setUp/queryUnReadForCompany.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
	@ResponseBody
	public String queryUnReadForCompany(HttpServletResponse response, HttpServletRequest request) {
		String cmfUserId = (String) request.getSession().getAttribute("fundAcco");
		String seqId = RequestHelper.getSeqId(request);
		// 日志封装类
		Context context = ContextUtils.setContext(ECConstants.QUERY_SERVICE_701, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		JSONObject returnJsonObject = queryManager.queryUnReadFundReportsCountForCompany(context, cmfUserId);
		if (returnJsonObject != null && returnJsonObject.getString("returnCode").equals("0000")) {
			returnJsonObject.put("unRead", returnJsonObject.getString("count"));
		} else {
			returnJsonObject.put("unRead", "0");
		}
		return returnJsonObject.toString();
	}
	/**
	 * 对公信批已读保存
	 * @param response
	 * @param request
	 * @return
	 * @throws Exception
	 * 			String
	 * @author luos
	 */
	@RequestMapping(value="/setUp/addReportReadRecordForCompany.xhtml", produces="text/html;charset=UTF-8" , method = {RequestMethod.POST})
	@ResponseBody
	public String addReportReadRecordForCompany(HttpServletResponse response,HttpServletRequest request)throws Exception{
		
		String fundId = request.getParameter("fundId");
		String reportId = request.getParameter("reportId");
		String cmfUserId = (String) request.getSession().getAttribute("fundAcco");
		String type = (String) request.getSession().getAttribute("type");
		String seqId = RequestHelper.getSeqId(request);
		JSONObject jsonObject = new JSONObject();
		if(cmfUserId==null && !ECConstants.SESSION_USERTYPE.equals(type)){
			jsonObject.put("returnCode", ECConstants.RETURN_CODE_8000);
			jsonObject.put("returnMsg", ECConstants.RETURN_MSG_8000);
			return jsonObject.toString();
		}
		//日志封装类
		Context context=ContextUtils.setContext(ECConstants.TRADE_SERVICE_601, ECConstants.SERVICE_CHANNEL_APP,cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
		OrderResult orderResult = tradeManager.addReportReadRecord(context, cmfUserId, fundId, reportId);
		/* 不需要返回任何数据 */
		jsonObject.put("returnCode", "0000");
		jsonObject.put("returnMsg", "成功");
		logger.info("【TradeController】addReportReadRecord()结束>>>jsonObject=" + jsonObject + ">>>timestamp=" + System.currentTimeMillis());
		return jsonObject.toString();
	}
	
	
	/**
	 * 查询机构用户的非官网产品
	 * @param request
	 */
	@RequestMapping( value = "/setUp/queryCompanyUserProduct.xhtml",method = {RequestMethod.GET,RequestMethod.POST}, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String queryCompanyUserProduct(HttpServletRequest request){
		/* 因登陆之后会把三要素存放至session 故可直接从session内取值 */
		String fundAcco = (String)request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String)request.getSession(true).getAttribute("crmCustNo");
		String branchName = (String)request.getSession(true).getAttribute("branchName");
		JSONObject result = new JSONObject();
		if(StringUtils.isBlank(fundAcco) || StringUtils.isBlank(crmCustNo) || StringUtils.isBlank(branchName)) {
			result.put("returnCode", "9999");
			result.put("returnMsg", "登陆失效");
			return result.toString();
		}
		result = queryManager.queryCompanyUserProduct(fundAcco,crmCustNo,branchName);
		logger.info("查询机构用户产品信息结束，返回值为:" +result.toString());
		return result.toString();
	}
	
	/**
	 * 查询机构用户的产品总资产
	 * @param request
	 */
	@RequestMapping( value = "/setUp/queryCompanyUserFundTotal.xhtml",method = {RequestMethod.GET,RequestMethod.POST}, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String queryCompanyUserFundTotal(HttpServletRequest request){
		/* 因登陆之后会把三要素存放至session 故可直接从session内取值 */
		String fundAcco = (String)request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String)request.getSession(true).getAttribute("crmCustNo");
		String branchName = (String)request.getSession(true).getAttribute("branchName");
		JSONObject result = new JSONObject();
		if(StringUtils.isBlank(fundAcco) || StringUtils.isBlank(crmCustNo) || StringUtils.isBlank(branchName)) {
			result.put("returnCode", "9999");
			result.put("returnMsg", "登陆失效");
			return result.toString();
		}
		result = queryManager.queryCompanyUserFundTotal(fundAcco,crmCustNo,branchName);
		logger.info("查询机构用户产品信息结束，返回值为:" +result.toString());
		return result.toString();
	}
	
	/**
	 * 查询机构用户购买基金净值
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setUp/queryCompanyUserFundNetValue.xhtml",method = {RequestMethod.GET,RequestMethod.POST}, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String queryCompanyUserFundNetValue(HttpServletRequest request) {
		JSONObject result = new JSONObject();
		/* 因登陆之后会把三要素存放至session 故可直接从session内取值 */
		String fundAcco = (String)request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String)request.getSession(true).getAttribute("crmCustNo");
		String branchName = (String)request.getSession(true).getAttribute("branchName");
		if(StringUtils.isBlank(fundAcco) || StringUtils.isBlank(crmCustNo) || StringUtils.isBlank(branchName)) {
			result.put("returnCode", "9999");
			result.put("returnMsg", "登陆失效");
			return result.toString();
		}
		String fundId = request.getParameter("fundId");
		String temp = request.getParameter("page");
		int page = 1;
		if(!StringUtils.isBlank(temp)) {
			try{
				page = Integer.parseInt(temp);
			} catch (NumberFormatException e) {
				logger.error("字符串转换为数字时，捕获异常");
			}
		}
		result = queryManager.queryCompanyUserFundNetValue(fundId,page);
		result.put("page", page);
		return result.toString();
	}
	
	/**
	 * 查询机构用户产品详情
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setUp/queryCompanyOtherDetailOrder.xhtml",method = {RequestMethod.GET,RequestMethod.POST}, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String queryCompanyOtherDetailOrder(HttpServletRequest request){
		JSONObject result = new JSONObject();
		/* 因登陆之后会把三要素存放至session 故可直接从session内取值 */
		String fundAcco = (String)request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String)request.getSession(true).getAttribute("crmCustNo");
		String branchName = (String)request.getSession(true).getAttribute("branchName");
		if(StringUtils.isBlank(fundAcco) || StringUtils.isBlank(crmCustNo) || StringUtils.isBlank(branchName)) {
			result.put("returnCode", "9999");
			result.put("returnMsg", "登陆失效");
			return result.toString();
		}
		String fundId = request.getParameter("fundId");
		String serialno = request.getParameter("serialno");
		String channelNo = request.getParameter("channleNo");
		if(StringUtils.isBlank(fundId) && StringUtils.isBlank(serialno)) {
			result.put("returnCode", ECConstants.RETURN_CODE_9000);
			result.put("returnMsg", ECConstants.RETURN_MSG_9000);
			return result.toString();
		}
		return queryManager.queryCompanyOtherDetailOrder(serialno, fundId, channelNo, fundAcco, crmCustNo, branchName).toString();
	}
	
	/**
	 * 查询机构用户产品详情界面净值走势图
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setUp/queryCompanyUserFundNetValueByImgTable.xhtml",method = {RequestMethod.GET,RequestMethod.POST}, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String queryCompanyUserFundNetValueByImgTable(HttpServletRequest request) {
		JSONObject result = new JSONObject();
		/* 因登陆之后会把三要素存放至session 故可直接从session内取值 */
		String fundAcco = (String)request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String)request.getSession(true).getAttribute("crmCustNo");
		String branchName = (String)request.getSession(true).getAttribute("branchName");
		if(StringUtils.isBlank(fundAcco) || StringUtils.isBlank(crmCustNo) || StringUtils.isBlank(branchName)) {
			result.put("returnCode", "9999");
			result.put("returnMsg", "登陆失效");
			return result.toString();
		}
		String temp = request.getParameter("dateTime");
		String fundId = request.getParameter("fundId");
		if(StringUtils.isBlank(fundId)){
			result.put("returnCode", ECConstants.RETURN_CODE_9000);
			result.put("returnMsg", ECConstants.RETURN_MSG_9000);
			return result.toString();
		}
		int datetime = 1;
		if(!StringUtils.isBlank(temp)) {
			try{
				datetime = Integer.parseInt(temp);
			} catch (NumberFormatException e) {
				logger.error("字符串转换为数字时，捕获异常");
			}
		}
		result = queryManager.queryCompanyUserFundNetValueByImgTable(fundId,datetime);
		return result.toString();
	}
	
	/**
	 * 机构用户查询基金合同信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/setUp/queryCompanyUserFundEcontract.xhtml",method = {RequestMethod.GET,RequestMethod.POST}, produces="text/html;charset=UTF-8")
	@ResponseBody
	public String queryCompanyUserFundEcontract(HttpServletRequest request) {
		JSONObject result = new JSONObject();
		/* 因登陆之后会把三要素存放至session 故可直接从session内取值 */
		String fundAcco = (String)request.getSession(true).getAttribute("fundAcco");
		String crmCustNo = (String)request.getSession(true).getAttribute("crmCustNo");
		String branchName = (String)request.getSession(true).getAttribute("branchName");
		if(StringUtils.isBlank(fundAcco) || StringUtils.isBlank(crmCustNo) || StringUtils.isBlank(branchName)) {
			result.put("returnCode", "9999");
			result.put("returnMsg", "登陆失效");
			return result.toString();
		}
		String fundId = request.getParameter("fundId");
		String period = request.getParameter("period");
		if (period==null || "".equals(period)) {
			period="1";
		}
		QueryMessageDto dto = queryManager.queryFundContractById(null, fundId,period);
		if("0000".equals(dto.getReturnCode())) {
			result.put("returnCode",dto.getReturnCode() );
			result.put("returnMsg", dto.getReturnMsg());
			result.put("fundContractDto",  dto.getData());
		} else {
			result.put("returnCode",dto.getReturnCode() );
			result.put("returnMsg", dto.getReturnMsg());
		}
		return result.toString();
	}

	
}
