package com.cmwa.ec.weixin.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ec.query.facade.dto.account.AgentFundDto;
import com.cmwa.ec.query.facade.dto.account.TradeAcctInfoDto;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.fund.FundReportsDto;
import com.cmwa.ec.query.facade.dto.fund.FundReportsPDFDto;
import com.cmwa.ec.query.facade.dto.fund.FundTradeInfoDto;
import com.cmwa.ec.query.facade.dto.trade.AppointRequestDto;
import com.cmwa.ec.query.facade.dto.web.ArticleDtoV2;
import com.cmwa.ec.query.facade.model.consultant.CustserviceInfoDTO;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.interceptor.TracingInfo;
import com.cmwa.ec.weixin.interceptor.VerifyUserInfoFilter;
import com.cmwa.ec.weixin.manager.business.QueryManager;
import com.cmwa.ec.weixin.manager.business.UserInfoexManager;
import com.cmwa.ec.weixin.util.ContextUtils;
import com.cmwa.ec.weixin.util.DateUtils;
import com.cmwa.ec.weixin.util.RequestHelper;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.upload.jspsmart.SmartUpload;

/**
 * 类说明：查询模块请求处理
 * 
 * @author liury
 * 
 */
@Controller("QueryController")
@RequestMapping(value = "/WeixinService")
public class QueryController {

    private static Logger logger = Logger.getLogger(QueryController.class.getName());

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private UserInfoexManager userManager;

    /**
     * 查询产品列表
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/business/queryFundList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryFundList(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String sessionId = request.getSession().getId();
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

        JSONObject resultJson = queryManager.queryFundList(context);

        return resultJson.toString();
    }

    /**
     * 查询用户交易账号列表 手机号码脱敏
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/business/queryUserTradeAcctInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryUserTradeAcctInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        logger.info("QueryController类【queryUserTradeAcctInfo】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());
        UserAccoRlaDto userAccoRla = (UserAccoRlaDto) request.getSession(true).getAttribute("userAccoRla");
        String ecCustNo = "";
        String tradeNo = request.getParameter("tradeNo");

        if (userAccoRla != null) {
            ecCustNo = userAccoRla.getEcCustNo();
        }

        logger.info("QueryController类【queryUserTradeAcctInfo】获取到用户数据ecCustNo  = " + ecCustNo);

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, "WXAPP", cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        JSONObject returnJsonObject = new JSONObject();
        String resultCode = "";
        String resultMsg = "";

        QueryMessageDto queryMessageDto = queryManager.queryUserTradeAcctInfo(context, ecCustNo, tradeNo, null);

        List<TradeAcctInfoDto> tradeAcctList = null;
        List<TradeAcctInfoDto> encryptList = new ArrayList<TradeAcctInfoDto>();
        if (queryMessageDto != null) {
            resultCode = queryMessageDto.getResultCode();
            resultMsg = queryMessageDto.getResultMsg();
            tradeAcctList = (List<TradeAcctInfoDto>) queryMessageDto.getData();
            for (TradeAcctInfoDto tradeAcctInfoDto : tradeAcctList) {
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
     * 查询热销产品列表
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/business/queryHotFundList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryHotFundList(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String sessionId = request.getSession().getId();
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

        JSONObject resultJson = queryManager.queryHotFundList(context);

        return resultJson.toString();
    }

    /**
     * 查询产品详情
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/business/queryFundInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryFundInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject resultJson = null;
        String fundId = request.getParameter("fundId");
        String period = request.getParameter("period");
        if (period == null || "".equals(period)) {
            // 不传期数则默认第一期
            period = "1";
        }
        if (fundId != null || !"".equals(fundId)) {
            String busiChannel = RequestHelper.verfiyIEChannel(request);
            String sessionId = request.getSession().getId();
            String cmfUserId = "";
            UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
            if (userBaseInfoDto != null) {
                cmfUserId = userBaseInfoDto.getCmfUserId();
            }
            // 日志封装类
            Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
            resultJson = queryManager.queryFundInfo(context, fundId, period);
        } else {
            resultJson = new JSONObject();
            resultJson.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
            resultJson.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
        }

        return resultJson.toString();
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
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryFeeRates(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String fundId = request.getParameter("fundId");
        String channelId = request.getParameter("channelId");
        String money = request.getParameter("money");
        if (money != null && money.length() > 0) {
            money = URLDecoder.decode(money, "utf-8");
        }
        logger.info("TradeController.class的queryFeeRates()中>>>cmfUserId=" + cmfUserId + ">>>System.currentTimeMillis()=" + cmfUserId + System.currentTimeMillis() + "fundId>>>"
                + fundId + "money>>>" + money);
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String sessionId = request.getSession().getId();
        List<String> channelNoList = new ArrayList<String>();
        // 获取渠道id
        if (channelId != null && !channelId.equals("")) {
            if (channelId.endsWith(",")) {
                channelId = channelId.substring(0, channelId.length() - 1);
            }
            String[] channelNoTemp = channelId.split(",");
            for (int i = 0; i < channelNoTemp.length; i++) {
                channelNoList.add(channelNoTemp[i]);
            }
        }
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
        JSONObject resultJson = queryManager.queryFeeRateList(request, money, context, fundId, channelNoList, "");

        return resultJson.toString();
    }

    /**
     * 根据产品id查询产品的合同， 新的合同查询接口，所有产品统一只有一个合同 清除合同具体内容
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author wudb
     */
    @RequestMapping(value = "/business/queryFundContractById.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryFundContractById(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String sessionId = request.getSession().getId();
        String fundId = request.getParameter("fundId");
        String period = request.getParameter("period");
        if (period == null || "".equals(period)) {
            period = "1";
        }
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        logger.info("QueryController类【queryFundContractById】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis() + "fundId>>>" + fundId);

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

        JSONObject resultJson = queryManager.queryFundContractById(context, fundId, "L", period);

        return resultJson.toString();
    }

    /**
     * 根据产品id查询产品的合同， 新的合同查询接口，所有产品统一只有一个合同 完整合同信息 只用于合同详情
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author wudb
     */
    @RequestMapping(value = "/business/queryFundContractDetail.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryFundContractDetail(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }
        String period = request.getParameter("period");
        if (period == null || "".equals(period)) {
            // 不传期数则默认第一期
            period = "1";
        }
        String sessionId = request.getSession().getId();
        String fundId = request.getParameter("fundId");
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        logger.info("QueryController类【queryFundContractById】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis() + "fundId>>>" + fundId);

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

        JSONObject resultJson = queryManager.queryFundContractById(context, fundId, "D", period);

        return resultJson.toString();
    }

    /**
     * 根据产品id查询产品的合同， 新的合同查询接口，合同详情页面
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author wudb
     */
    @RequestMapping(value = "/business/queryFundContractByIdDetail.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryFundContractByIdDetail(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String sessionId = request.getSession().getId();
        String fundId = request.getParameter("fundId");
        String period = request.getParameter("period");
        if (period == null || "".equals(period)) {
            // 不传期数则默认第一期
            period = "1";
        }
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        logger.info("QueryController类【queryFundContractByIdDetail】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis() + "fundId>>>" + fundId);

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

        JSONObject resultJson = queryManager.queryFundContractById(context, fundId, "D", "period");

        return resultJson.toString();
    }

    /**
     * 查询用户的银行卡信息
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author liury
     */
    @RequestMapping(value = "/business/queryMyBankCardNo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryMyBankCardNo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String fundid = request.getParameter("fundid");
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        JSONObject returnJsonObject = null;
        String cmfUserId = "";
        String sessionId = request.getSession().getId();
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }
        UserServiceMessage usermessage = queryManager.queryUserInfoByCmfUserId(context, cmfUserId);
        // 进入我的银行卡页面，重新获取银行信息；用于：异步鉴权的银行渠道 20161101
        UserAccoRlaDto userAccoRla = usermessage.getUserAccoRlaDto();
        userBaseInfoDto = usermessage.getUserBaseInfoDto();
        String ecCustNo = "";
        String userType = null;
        if (userBaseInfoDto == null) {
            returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
            returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
            return returnJsonObject.toString();
        } else {
            if (userAccoRla != null) {
                ecCustNo = userAccoRla.getEcCustNo();
            } else {
                returnJsonObject = new JSONObject();
                returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
                returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
                return returnJsonObject.toString();
            }
            userType = userBaseInfoDto.getUserType();
            request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfoDto);
            request.getSession(true).setAttribute(SessionValue.SESSION_USERACCORLA, userAccoRla);
        }
        logger.info("QueryController.class的queryMyBankCardNo()中：>>>cmfUserId=" + cmfUserId + ">>>ecCustNo=" + ecCustNo + ">>>System.currentTimeMillis()="
                + System.currentTimeMillis());
        returnJsonObject = queryManager.getTradeAcctInfoByCustno(context, ecCustNo, "Y".equals(request.getParameter("cleartext")), fundid);
        returnJsonObject.put("userType", userType);
        return returnJsonObject.toString();
    }

    /**
     * 根据银行卡号查找该卡号的具体银行信息 即卡号校验
     * 
     * @param response
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/business/queryBankInfoByBankNumber.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryBankInfoByBankNumber(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String bankNumber = request.getParameter("bankNumber");
        String sessionId = request.getSession().getId();
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

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
     */
    @RequestMapping(value = "/business/checkIdNoByBankAuthentication.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String checkIdNoByBankAuthentication(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String busiChannel = RequestHelper.verfiyIEChannel(request);

        JSONObject returnJsonObject = null;
        // 证件类型
        String idtp = request.getParameter("idtp");
        // 证件号码
        String idno = request.getParameter("idno");
        UserBaseInfoDto userInfo = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);

        String cmfUserId = "";
        if (userInfo != null) {
            cmfUserId = userInfo.getCmfUserId();
        } else {
            returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
            returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
            return returnJsonObject.toString();
        }

        idno = idno != null ? idno.toUpperCase() : idno;
        String seqId = request.getSession().getId();
        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        returnJsonObject = userManager.checkIdNoByBankAuthentication(context, cmfUserId, idno, idtp);

        return returnJsonObject.toString();

    }

    /**
     * 查询用户产品持仓
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/business/queryCustTradeInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryCustTradeInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        long startTimeForMills = System.currentTimeMillis();
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        String fundCode = request.getParameter("fundCode");
        String tradeAcco = request.getParameter("tradeAcco");
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, WXConstants.SERVICE_CHANNEL_WEIXIN, cmfUserId, System.currentTimeMillis(),
                System.currentTimeMillis(), seqId, "");
        JSONObject returnJsonObject = queryManager.queryCustTradeInfo(context, cmfUserId, fundCode, tradeAcco);
        logger.info("查询用户产品持仓耗时：" + (System.currentTimeMillis() - startTimeForMills) + "ms");
        return returnJsonObject.toString();
    }

    @RequestMapping(value = "/business/queryUserHasProOreder.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryUserHasProOreder(HttpServletResponse response, HttpServletRequest request) throws Exception {
        long startTimeForMills = System.currentTimeMillis();
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        String fundCode = request.getParameter("fundCode");
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, WXConstants.SERVICE_CHANNEL_WEIXIN, cmfUserId, System.currentTimeMillis(),
                System.currentTimeMillis(), seqId, "");
        JSONObject returnJsonObject = queryManager.queryNewCustomer(context, cmfUserId, fundCode);
        logger.info("查询用户产品持仓耗时：" + (System.currentTimeMillis() - startTimeForMills) + "ms");
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
     * @author maj
     */
    @RequestMapping(value = "/business/queryAccount.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "10")
    public String queryAccount(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String seqId = request.getSession(true).getId();

        JSONObject returnJsonObject = new JSONObject();

        logger.info("【QueryController】queryAccount()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());

        UserBaseInfoDto userBaseInfo = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        returnJsonObject = queryManager.queryTotalFundBalance(context, cmfUserId, userBaseInfo, seqId);

        JSONObject conutJsonObject = queryManager.queryUnReadFundReportsCount(context, cmfUserId);
        if (conutJsonObject != null && conutJsonObject.getString("returnCode").equals("0000")) {
            returnJsonObject.put("unRead", conutJsonObject.getString("count"));
        } else {
            returnJsonObject.put("unRead", "0");
        }

        logger.info("【QueryController】queryAccount()结束>>>returnJsonObject=" + returnJsonObject + ">>>timestamp=" + System.currentTimeMillis());

        return returnJsonObject.toString();

    }

    /**
     * 查询支持的银行卡列表
     */
    @RequestMapping(value = "/business/getSupportBankDesc.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "10")
    public String getSupportBankDesc(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String sessionId = request.getSession().getId();
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

        JSONObject jsonObject = queryManager.getSupportBankDesc(context);

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
    @TracingInfo(authority = "10")
    public String queryTradeInfoList(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String cmfUserId = "";
        JSONObject returnJsonObject = new JSONObject();
        String returnCode = "";
        String returnMsg = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);

        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }
        String seqId = request.getSession(true).getId();

        UserAccoRlaDto userAccoRlaDto = (UserAccoRlaDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERACCORLA);

        if (userAccoRlaDto == null) {
            logger.info("【QueryController】queryTradeInfoList(),userAccoRlaDto=" + userAccoRlaDto + "该用户没有实名无法查询订单");
            returnJsonObject.put("returnCode", 9000);
            returnJsonObject.put("returnMsg", "该用户没有实名无法查询订单");
            return returnJsonObject.toString();
        }
        String custno = userAccoRlaDto.getEcCustNo();
        if (StringUtil.isEmpty(custno)) {
            logger.info("【QueryController】queryTradeInfoList(),custno" + custno + "缺少关键参数--ecCustNo客户编号");
            returnJsonObject.put("returnCode", 9000);
            returnJsonObject.put("returnMsg", "缺少关键参数--ecCustNo客户编号");
            return returnJsonObject.toString();
        }

        String fundId = request.getParameter("fundId");
        String applyst = request.getParameter("applyst");

        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        QueryMessageDto queryMessageDto = queryManager.queryTradeInfoList(context, custno, fundId, applyst);
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

        if (list != null && list.size() > 0) {
            for (AppointRequestDto appointRequestDto : list) {
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
        return returnJsonObject.toString();

    }

    /**
     * 查询订单详情
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     *             String
     * @author maj
     */
    @RequestMapping(value = "/business/queryTradeInfoByTradeNo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "30")
    public String queryTradeInfoByTradeNo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }
        String seqId = request.getSession(true).getId();
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        JSONObject jsonObject = queryManager.queryTradeInfoByTradeNo(context, request);
        return jsonObject.toString();
    }

    /**
     * 信批 查询“我的消息”
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/business/queryUserMessageList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "10")
    public String queryUserMessageList(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String seqId = request.getSession(true).getId();

        logger.info("【QueryController】queryUserMessageList()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());

        String tempPage = request.getParameter("page");
        int page = 1;
        if (tempPage != null && !tempPage.equals("")) {
            page = Integer.parseInt(tempPage);
        }

        String totalAmount = (String) request.getSession(true).getAttribute(SessionValue.SESSION_TOTALAMOUNT);
        if (totalAmount == null || "".equals(totalAmount)) {
            totalAmount = "-1";
        }

        int amount = WXConstants.MESSAGE_AMOUNT;// 每页展示条数
        int beginIdx = (page - 1) * amount;

        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        QueryMessageDto queryMessageDto = queryManager.queryUserMessageList(context, cmfUserId, beginIdx, amount, Integer.parseInt(totalAmount));

        List<FundReportsDto> list = new ArrayList<FundReportsDto>();
        String returnCode = "";
        String returnMsg = "";
        JSONObject jsonObject = new JSONObject();
        if (queryMessageDto != null) {
            returnCode = queryMessageDto.getResultCode();
            returnMsg = queryMessageDto.getResultMsg();
            list = (List<FundReportsDto>) queryMessageDto.getData();
            totalAmount = (String) queryMessageDto.getOtherData();
        }
        jsonObject.put("returnCode", returnCode);
        jsonObject.put("returnMsg", returnMsg);
        jsonObject.put("totalAmount", totalAmount);

        if (list != null) {
            jsonObject.put("list", list);
        }
        if (totalAmount != null && Integer.parseInt(totalAmount) <= 0) {
            request.getSession(true).setAttribute(SessionValue.SESSION_TOTALAMOUNT, totalAmount);
        }

        logger.info("【QueryController】queryUserMessageList()结束>>>returnCode=" + returnCode + ">>>returnMsg=" + returnMsg + ">>>timestamp=" + System.currentTimeMillis());

        return jsonObject.toString();
    }

    /**
     * 信批 详情
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/business/queryUserMessage.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "10")
    public String queryUserMessage(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String seqId = request.getSession(true).getId();

        logger.info("【QueryController】queryUserMessage()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());

        String msgId = request.getParameter("msgId");
        String msgType = request.getParameter("msgType");

        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        QueryMessageDto queryMessageDto = queryManager.queryUserMessage(context, cmfUserId, msgId, msgType);

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

        logger.info("【QueryController】queryUserMessage()结束>>>returnCode=" + returnCode + ">>>returnMsg=" + returnMsg + ">>>timestamp=" + System.currentTimeMillis());

        return jsonObject.toString();
    }

    /**
     * 信批 pdf格式下载/打开
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/business/queryUserMessageByPDF.xhtml", method = { RequestMethod.POST, RequestMethod.GET })
    @TracingInfo(authority = "10")
    public void queryUserMessageByPDF(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String seqId = request.getSession(true).getId();

        logger.info("【QueryController】queryUserMessageByPDF()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());

        String msgId = request.getParameter("msgId");
        String msgType = request.getParameter("msgType");

        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

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
                    if (fileName.indexOf(".pdf") < 0 && fileName.indexOf(".mht") < 0 && fileName.indexOf(".doc") < 0 && fileName.indexOf(".PDF") < 0
                            && fileName.indexOf(".html") < 0 && fileName.indexOf(".txt") < 0 && fileName.indexOf(".xls") < 0) {
                        fileName = fileName + ".pdf";
                    }
                } else if ("5".equals(msgType)) {
                    fileName = fileName == null ? "产品文档.pdf" : fileName;
                    if (fileName.indexOf(".pdf") < 0 && fileName.indexOf(".mht") < 0 && fileName.indexOf(".doc") < 0 && fileName.indexOf(".PDF") < 0
                            && fileName.indexOf(".html") < 0 && fileName.indexOf(".txt") < 0 && fileName.indexOf(".xls") < 0) {
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
            logger.warn("--pdf文件不存在");
            response.getOutputStream().print("文件不存在");
        }

    }

    /**
     * 查询历史交易信息 对账单
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/business/queryFundTradeInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "10")
    public String queryFundTradeInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String seqId = request.getSession(true).getId();

        logger.info("【QueryController】queryFundTradeInfo()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());

        Date date = new Date();// 当前日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 格式化对象
        Calendar calendar = Calendar.getInstance();// 日历对象
        calendar.setTime(date);// 设置当前日期
        calendar.add(Calendar.YEAR, -1);// 年份减一
        // 获取当前时间减一年的时间
        // String startDate = sdf.format(calendar.getTime());
        String startDate = "";
        // 当前时间
        String endDate = DateUtils.formatDate(date, "yyyyMMdd");

        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        QueryMessageDto queryMessageDto = queryManager.queryFundTradeInfo(context, cmfUserId, startDate, endDate);

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

        logger.info("【QueryController】queryFundTradeInfo()结束>>>returnCode=" + returnCode + ">>>returnMsg=" + returnMsg + ">>>list.size()=" + list != null ? list.size() + "" : 0
                + "" + ">>>timestamp=" + System.currentTimeMillis());
        return jsonObject.toString();
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
    @TracingInfo(authority = "10")
    public String queryAgentFundInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);

        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String seqId = request.getSession(true).getId();

        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        QueryMessageDto queryMessageDto = queryManager.queryAgentFundInfo(context, cmfUserId);

        JSONObject returnJsonObject = new JSONObject();
        String returnCode = "";
        String returnMsg = "";
        List<AgentFundDto> list = new ArrayList<AgentFundDto>();// 全部订单
        List<AgentFundDto> gList = new ArrayList<AgentFundDto>();// 存续中订单
        List<AgentFundDto> zList = new ArrayList<AgentFundDto>();// 已到期订单

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

        return returnJsonObject.toString();

    }

    /**
     * 查询公司公告列表查询 分页
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/article/queryCompanyArticle.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryCompanyArticle(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        } else {// 自动登录
            VerifyUserInfoFilter autoLogin = new VerifyUserInfoFilter();
            autoLogin.autoLogin(request);
            userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
            if (null != userBaseInfoDto) {
                cmfUserId = userBaseInfoDto.getCmfUserId();
            }
        }

        String seqId = request.getSession(true).getId();

        logger.info("【QueryController】queryCompanyArticle()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());

        String tempPage = request.getParameter("page");
        int page = 1;
        if (tempPage != null && !tempPage.equals("")) {
            page = Integer.parseInt(tempPage);
        }

        int amount = WXConstants.MESSAGE_AMOUNT;// 每页展示条数
        int beginrow = (page - 1) * amount;// 开始条数
        int endrow = beginrow + amount;

        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        QueryMessageDto queryMessageDto = queryManager.queryCompanyArticle(context, cmfUserId, beginrow, endrow);
        QueryMessageDto articleCount = queryManager.queryCompanyArticleCount(context, cmfUserId);

        List<ArticleDtoV2> list = new ArrayList<ArticleDtoV2>();
        int count = 0;

        String returnCode = "";
        String returnMsg = "";
        JSONObject jsonObject = new JSONObject();
        if (queryMessageDto != null) {
            returnCode = queryMessageDto.getResultCode();
            returnMsg = queryMessageDto.getResultMsg();
            list = (List<ArticleDtoV2>) queryMessageDto.getData();
        }
        if (articleCount != null && articleCount.getReturnCode().equals("0000")) {
            count = (Integer) articleCount.getData();
        }
        jsonObject.put("returnCode", returnCode);
        jsonObject.put("returnMsg", returnMsg);
        if (list != null) {
            jsonObject.put("list", list);
        }
        if (count > 0) {
            jsonObject.put("count", count);
        }

        logger.info("【QueryController】queryCompanyArticle()结束>>>returnCode=" + returnCode + ">>>returnMsg=" + returnMsg + ">>>timestamp=" + System.currentTimeMillis());

        return jsonObject.toString();
    }

    /**
     * 最新动态、公司公告 详情
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/article/queryArticleContent.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryArticleContent(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        } else {// 自动登录
            VerifyUserInfoFilter autoLogin = new VerifyUserInfoFilter();
            autoLogin.autoLogin(request);
            userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
            if (null != userBaseInfoDto) {
                cmfUserId = userBaseInfoDto.getCmfUserId();
            }
        }

        String seqId = request.getSession(true).getId();

        logger.info("【QueryController】queryArticleContent()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());

        String articleId = request.getParameter("articleId");

        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        QueryMessageDto queryMessageDto = queryManager.queryArticleContent(context, articleId);

        String returnCode = "";
        String returnMsg = "";
        // String articleContent = "";
        Map<String, Object> map = new HashMap<String, Object>();
        JSONObject jsonObject = new JSONObject();

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

        logger.info("【QueryController】queryArticleContent()结束>>>returnCode=" + returnCode + ">>>returnMsg=" + returnMsg + ">>>timestamp=" + System.currentTimeMillis());

        return jsonObject.toString();
    }

    /**
     * 查询新闻动态公告 分页
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/article/queryNewsArticle.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryNewsArticle(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        } else {// 自动登录
            VerifyUserInfoFilter autoLogin = new VerifyUserInfoFilter();
            autoLogin.autoLogin(request);
            userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
            if (null != userBaseInfoDto) {
                cmfUserId = userBaseInfoDto.getCmfUserId();
            }
        }

        String seqId = request.getSession(true).getId();

        logger.info("【QueryController】queryNewsArticle()开始>>>seqId=" + seqId + ">>>busiChannel=" + busiChannel + ">>>timestamp=" + System.currentTimeMillis());

        String tempPage = request.getParameter("page");
        int page = 1;
        if (tempPage != null && !tempPage.equals("")) {
            page = Integer.parseInt(tempPage);
        }

        int amount = WXConstants.MESSAGE_AMOUNT;// 每页展示条数
        int beginrow = (page - 1) * amount;// 开始条数
        int endrow = beginrow + amount;

        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        QueryMessageDto queryMessageDto = queryManager.queryNewsArticle(context, cmfUserId, beginrow, endrow);
        QueryMessageDto articleCount = queryManager.queryNewsArticleCount(context, cmfUserId);

        List<ArticleDtoV2> list = new ArrayList<ArticleDtoV2>();
        int count = 0;

        String returnCode = "";
        String returnMsg = "";
        JSONObject jsonObject = new JSONObject();
        if (queryMessageDto != null) {
            returnCode = queryMessageDto.getResultCode();
            returnMsg = queryMessageDto.getResultMsg();
            list = (List<ArticleDtoV2>) queryMessageDto.getData();
        }
        if (articleCount != null && articleCount.getReturnCode().equals("0000")) {
            count = (Integer) articleCount.getData();
        }
        jsonObject.put("returnCode", returnCode);
        jsonObject.put("returnMsg", returnMsg);
        if (list != null) {
            jsonObject.put("list", list);
        }
        if (count > 0) {
            jsonObject.put("count", count);
        }

        logger.info("【QueryController】queryNewsArticle()结束>>>returnCode=" + returnCode + ">>>returnMsg=" + returnMsg + ">>>timestamp=" + System.currentTimeMillis());

        return jsonObject.toString();
    }

    /*------------------------微信新版本需求更改8-12----------------------------*/
    /**
     * 查询代销产品订单详情
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     *             String
     * @author luos
     */
    @RequestMapping(value = "/business/queryOtherDetailOrder.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "30")
    public String queryOtherDetailOrder(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        JSONObject jsonObject = null;
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        jsonObject = queryManager.queryOtherDetailOrder(context, request);
        return jsonObject.toString();
    }

    /*------------------------微信新版本需求更改8-23----------------------------*/
    /**
     * 查询微信banner图片
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/business/queryBanner.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "30")
    public String queryBanner(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        JSONObject jsonObject = null;
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        jsonObject = queryManager.queryBanner(context, "34");
        return jsonObject.toString();
    }

    /**
     * 查询产品净值（净值类产品） 无分页 有时间条件
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author luos
     */
    @RequestMapping(value = "/business/queryEstimateByFundId.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "30")
    public String queryEstimateByFundId(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        JSONObject jsonObject = queryManager.queryEstimateByFundId(context, request);
        return jsonObject.toString();
    }

    /**
     * 查询产品净值（净值类产品） 有分页 无时间条件
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author luos
     */
    @RequestMapping(value = "/business/queryEstimateByFundIdByPage.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "30")
    public String queryEstimateByFundIdByPage(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        JSONObject jsonObject = queryManager.queryEstimateByFundIdByPage(context, request);
        return jsonObject.toString();
    }

    /**
     * 招行鉴权回调
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/afterCmbSign/gotoMyBanklist.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public void gotoMyBanklist(HttpServletResponse response, HttpServletRequest request) throws Exception {

        response.sendRedirect("/WeixinService/business/bank/bankList.shtml");
    }

    /**
     * 家庭住址 查询国籍-省份等地区信息 联动
     * 
     * @param context
     * @param pmst
     * @param pmky
     * @param pmco
     * @param pmv1
     * @return
     */
    @RequestMapping(value = "/business/queryParamList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String queryParamList(HttpServletResponse response, HttpServletRequest request, @RequestParam("paramType") String paramType, @RequestParam("paramKey") String paramKey,
            @RequestParam("pmValueOne") String pmValueOne) {
        QueryMessageDto queryParamList = queryManager.queryParamList(new Context(), paramType, paramKey, null, pmValueOne);
        JSONObject returnObject = new JSONObject();
        returnObject = JSONObject.fromObject(queryParamList);
        return returnObject.toString();
    };

    /**
     * 根据消息标题匹配信披信息
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/business/queryUserMsgLikeTitle.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryUserMsgLikeTitle(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        String busiChannel = RequestHelper.verfiyIEChannel(request);

        String title = request.getParameter("title");
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        JSONObject jsonObject = queryManager.queryUserMsgLikeTitle(context, cmfUserId, title);
        return jsonObject.toString();
    }

    /**
     * 查询产品七日年化和万份收益
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     */

    @RequestMapping(value = "/business/queryProfitByFundCode.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryProfitByFundCode(HttpServletResponse response, HttpServletRequest request) throws Exception {
        long startTimeForMills = System.currentTimeMillis();
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        String fundCode = request.getParameter("fundCode");
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, WXConstants.SERVICE_CHANNEL_WEIXIN, cmfUserId, System.currentTimeMillis(),
                System.currentTimeMillis(), seqId, "");
        JSONObject returnJsonObject = queryManager.queryProfitByFundCode(context, cmfUserId, fundCode);
        logger.info("查询用户年化收益和万份收益时：" + (System.currentTimeMillis() - startTimeForMills) + "ms");
        return returnJsonObject.toString();
    }

    /**
     * 根据用户编号跟基金代码查询订单详情
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/business/queryTradeInfoByCustNo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryTradeInfoByCustNo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, WXConstants.SERVICE_CHANNEL_WEIXIN, cmfUserId, System.currentTimeMillis(),
                System.currentTimeMillis(), seqId, "");
        String tradeAcco = request.getParameter("tradeAcco");
        String fundid = request.getParameter("fundid");
        String subquty = request.getParameter("subquty");
        logger.info("QueryController类【queryTradeInfoByCustNo 】开始>>>fundid:" + fundid + ">>>tradeAcco:" + tradeAcco + ">>>subquty:" + subquty);
        JSONObject jsonObject = queryManager.queryTradeInfoByCustNo(context, tradeAcco, fundid, subquty);
        return jsonObject.toString();
    }

    /**
     * 根据产品id来查询可赎回的份额及银行卡信息
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/business/queryCanRedeemBankInfoByFundCode.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryCanRedeemBankInfoByFundCode(HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject obj = new JSONObject();
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        String fundid = request.getParameter("fundCode");
        UserAccoRlaDto rla = RequestHelper.getSessionUserAccoRla(request);
        if (rla == null) {
            obj.put("returnCode", "9999");
            obj.put("returnMsg", "关键参数丢失");
            logger.info("queryCanRedeemBankInfoByFundCode -> session内未获取到userAccoRla记录");
            return obj.toString();
        }
        String custno = rla.getEcCustNo();
        logger.info("queryCanRedeemBankInfoByFundCode -> 获取到参数 cmfuserid:" + cmfUserId + ",seqId:" + seqId + ",fundid:" + fundid + ",custno:" + custno + "");
        obj = queryManager.queryCanRedeemBankInfoByFundCode(fundid, custno);
        return obj.toString();
    }

    /**
     * 查询产品详情
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/queryFundInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String queryFundInfoForH5(HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject resultJson = null;
        String fundId = request.getParameter("fundId");
        String period = request.getParameter("period");
        if (period == null || "".equals(period)) {
            // 不传期数则默认第一期
            period = "1";
        }
        if (fundId != null || !"".equals(fundId)) {
            String busiChannel = RequestHelper.verfiyIEChannel(request);
            String sessionId = request.getSession().getId();
            String cmfUserId = "";
            UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
            if (userBaseInfoDto != null) {
                cmfUserId = userBaseInfoDto.getCmfUserId();
            }
            // 日志封装类
            Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
            resultJson = queryManager.queryFundInfo(context, fundId, period);
        } else {
            resultJson = new JSONObject();
            resultJson.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
            resultJson.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
        }

        return resultJson.toString();
    }

    /**
     * 提供给H5页面查询开关
     * 
     * @param pmst
     * @param pmky
     * @param pmco
     * @param pmv1
     * @return
     */
    @RequestMapping(value = "/setUp/queryParamList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String queryParamListForH5(HttpServletResponse response, HttpServletRequest request, @RequestParam("paramType") String paramType,
            @RequestParam("paramKey") String paramKey, @RequestParam("pmValueOne") String pmValueOne) {
        QueryMessageDto queryParamList = queryManager.queryParamList(new Context(), paramType, paramKey, null, pmValueOne);
        JSONObject returnObject = new JSONObject();
        returnObject = JSONObject.fromObject(queryParamList);
        return returnObject.toString();
    };

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
        JSONObject result = WXConstants.ERR_JSON_RESULT;
        String sessionCmfUserId = RequestHelper.getSessionCmfUserId(request);
        if (StringUtils.isNotBlank(sessionCmfUserId)) {
            CustserviceInfoDTO consultantInfo = queryManager.getConsultantInfo(sessionCmfUserId);
            if (null != consultantInfo) {
                result = new JSONObject().accumulate("consultantInfo", consultantInfo).accumulate(WXConstants.ERR_CODE, WXConstants.COMMON_SUCCESS);
            }
        }

        return result.toString();
    }

    /**
     * 根据身份证查询合格投资者信息
     * 
     * @param response
     * @param request
     */
    @RequestMapping(value = "/business/queryQualifiedUserInfoByIdno.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String queryQualifiedUserInfoByIdno(HttpServletResponse response, HttpServletRequest request) throws Exception {
        UserBaseInfoDto sessionUserInfo = (UserBaseInfoDto) request.getSession().getAttribute(SessionValue.SESSION_USERBASEINFO);
        JSONObject returnObj = new JSONObject();
        if (sessionUserInfo == null) {
            returnObj.put("returnCode", "9999");
            returnObj.put("returnMsg", "请重新登陆");
            return returnObj.toString();
        }
        if (StringUtils.isBlank(sessionUserInfo.getIdNo())) {
            returnObj.put("returnCode", "9005");
            returnObj.put("returnMsg", "请进行实名验证");
            return returnObj.toString();
        }
        return queryManager.queryQualifiedUserInfoByIdno(sessionUserInfo.getIdNo()).toString();
    }

    /**
     * 根据身份证查询合格投资者信息
     * 
     * @param response
     * @param request
     */
    @RequestMapping(value = "/business/queryFileUploadRecord.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String queryFileUploadRecord(HttpServletResponse response, HttpServletRequest request) throws Exception {
        UserBaseInfoDto sessionUserInfo = (UserBaseInfoDto) request.getSession().getAttribute(SessionValue.SESSION_USERBASEINFO);
        JSONObject returnObj = new JSONObject();
        if (sessionUserInfo == null) {
            returnObj.put("returnCode", "9999");
            returnObj.put("returnMsg", "请重新登陆");
            return returnObj.toString();
        }
        return queryManager.queryFileUploadRecord(sessionUserInfo.getCmfUserId()).toString();
    }

    @RequestMapping(value = "/business/queryAccreditedInvestorConditions.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String queryAccreditedInvestorConditions(HttpServletRequest request, HttpServletResponse response) {
        UserAccoRlaDto sessionUserInfo = (UserAccoRlaDto) request.getSession().getAttribute(SessionValue.SESSION_USERACCORLA);
        if (sessionUserInfo == null) {
            JSONObject returnObj = new JSONObject();
            returnObj.put("returnCode", "9005");
            returnObj.put("returnMsg", "请进行实名验证");
            return returnObj.toString();
        }
        return queryManager.queryAccreditedInvestorConditions(sessionUserInfo.getCmfUserId(), sessionUserInfo.getEcCustNo()).toString();
    }

    @RequestMapping(value = "/queryUserinfoByMoblie.xhtml", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String queryUserinfoByMoblie(HttpServletResponse response, HttpServletRequest request) {
        String mobile = request.getParameter("mobile");
        if (StringUtils.isBlank(mobile)) {
            logger.info("通过手机号查询用户信息参数为空---mobile:" + mobile);
            return WXConstants.PARAMISNULL_JSON_RESULT.toString();
        }
        UserServiceMessage userServiceMessage = queryManager.queryUserInfoByMobile(mobile);
        if (userServiceMessage != null) {
            UserBaseInfoDto userBaseInfoDto = userServiceMessage.getUserBaseInfoDto();
            return new JSONObject().accumulate(WXConstants.ERR_CODE, userServiceMessage.getResultCode()).accumulate(WXConstants.ERR_MSG, userServiceMessage.getResultMsg())
                    .accumulate("userBaseInfo", userBaseInfoDto).toString();
        } else {
            logger.warn("通过手机号查询用户信息失败---mobile:" + mobile);
            return WXConstants.ERR_JSON_RESULT.toString();
        }
    }

    @RequestMapping(value = "/getConsultantInfoByCmfuserId.xhtml", produces = "text/html;charset=UTF-8", method = RequestMethod.GET)
    @ResponseBody
    public String getConsultantInfoByCmfuserId(HttpServletResponse response, HttpServletRequest request) {
        String cmfuserid = request.getParameter("cmfuserid");
        if (StringUtils.isBlank(cmfuserid)) {
            logger.info("通过cmfuserid查询顾问信息参数为空---cmfuserid:" + cmfuserid);
            return WXConstants.PARAMISNULL_JSON_RESULT.toString();
        }
        CustserviceInfoDTO consultantInfo = queryManager.getConsultantInfo(cmfuserid);
        if (null != consultantInfo) {
            return new JSONObject().accumulate(WXConstants.ERR_CODE, WXConstants.COMMON_SUCCESS).accumulate(WXConstants.ERR_MSG, WXConstants.COMMON_SUCCESS_MSG)
                    .accumulate("consultantInfo", consultantInfo).toString();
        } else {
            logger.warn("通过cmfuserid查询顾问信息失败---cmfuserid:" + cmfuserid);
            return WXConstants.ERR_JSON_RESULT.toString();
        }
    }

    /**
     * 查询风险揭示函条款列表
     * 
     * @param response
     * @param request
     * @return
     */
    @RequestMapping(value = "/business/queryRiskTermList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    String queryRiskTermList(HttpServletResponse response, HttpServletRequest request) {
        JSONObject result = WXConstants.ERR_JSON_RESULT;
        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String sessionId = request.getSession().getId();
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");
        if (StringUtils.isNotBlank(sessionId)) {
            String fundId = request.getParameter("fundId");
            String period = request.getParameter("period");
            result = queryManager.queryRiskTermList(context, cmfUserId, fundId, period);
        }
        return result.toString();
    }
}