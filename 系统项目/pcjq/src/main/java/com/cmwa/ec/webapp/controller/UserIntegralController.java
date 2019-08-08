package com.cmwa.ec.webapp.controller;

import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.webapp.manager.UserIntegralManager;
import com.cmwa.ec.webapp.model.integral.IntegralDetailVO;
import com.cmwa.ec.webapp.util.ECConstants;
import com.cmwa.ec.webapp.util.RequestHelper;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 积分功能请求处理类
 *
 * @author ex-chent@cmfchina.com
 */
@Controller("UserIntegralController")
@RequestMapping("/AppService")
public class UserIntegralController {

    /** slf4jLogger */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserIntegralController.class);

    /** 用户积分相关功能业务处理类 */
    @Autowired
    private UserIntegralManager userIntegralManager;


    /**
     * 获取用户积分信息
     */
    @RequestMapping(value = "/business/integral/getIntegralInfo.xhtml", produces = "text/html;charset=UTF-8",
        method = RequestMethod.GET)
    @ResponseBody
    String getUserIntegralInfo(HttpServletResponse response, HttpServletRequest request) {
        String sessionCmfUserId = RequestHelper.getSessionCmfUserId(request);
        JSONObject result = ECConstants.ERR_JSON_RESULT;
        if (isAuthenticationUser(request) && StringUtils.isNotBlank(sessionCmfUserId)) {
            result = userIntegralManager.getUserIntegralInfo(request, sessionCmfUserId);
        } else {
            LOGGER.warn("getUserIntegralInfo_session中数据异常_cmfUserId: {}", sessionCmfUserId);
        }
        return result.toString();
    }

    /**
     * 获取用户积分流水
     */
    @RequestMapping(value = "/business/integral/listIntegralDetail.xhtml", produces = "text/html;charset=UTF-8",
        method = RequestMethod.GET)
    @ResponseBody
    String getUserIntegralDetailList(HttpServletResponse response, HttpServletRequest request) {
        String sessionCmfUserId = RequestHelper.getSessionCmfUserId(request);
        JSONObject result = ECConstants.ERR_JSON_RESULT;
        // TODO check
        if (isAuthenticationUser(request) && StringUtils.isNotBlank(sessionCmfUserId)) {
            try {
                List<IntegralDetailVO> list = userIntegralManager.listUserIntegralDetail(sessionCmfUserId);
                result = new JSONObject().accumulate("data", list)
                    .accumulate(ECConstants.ERR_CODE, ECConstants.COMMON_SUCCESS);
            } catch (Exception omg) {
                LOGGER.error("getUserIntegralDetailList_获取积分流水失败_", omg);
            }
        }
        return result.toString();
    }

    /**
     * 推荐顾问
     */
    @RequestMapping(value = "/business/integral/shareConsultant.xhtml", produces = "text/html;charset=UTF-8",
        method = RequestMethod.POST)
    @ResponseBody
    String shareConsultant(HttpServletResponse response, HttpServletRequest request) {
        String consultantMobile = request.getParameter("consultantMobile");
        String sessionCmfUserId = RequestHelper.getSessionCmfUserId(request);
        String customerMobile = request.getParameter("customerMobile");
        String customerName = request.getParameter("customerName");
        String consultant = request.getParameter("consultant");
        String referrer = request.getParameter("referrer");

        JSONObject result = ECConstants.ERR_JSON_RESULT;
        // TODO
        if (isAuthenticationUser(request) && StringUtils.isNotBlank(sessionCmfUserId)
            && StringUtils.isNotBlank(consultantMobile) && StringUtils.isNotBlank(customerMobile)
            && StringUtils.isNotBlank(customerName) && StringUtils.isNotBlank(consultant)
            && StringUtils.isNotBlank(referrer)) {
            try {
                int status = userIntegralManager.shareConsultant(customerName, customerMobile, sessionCmfUserId,
                    referrer, consultant, consultantMobile);
                result = getSuccessResult().accumulate("status", status);
            } catch (Exception omg) {
                LOGGER.error("shareConsultant_推荐顾问时异常cmfUserId: {}, customerName: {}, " +
                        "customerMobile: {}, referrer: {}, consultant: {}, consultantMobile: {}",
                    sessionCmfUserId, customerName, customerMobile, referrer, consultant, consultantMobile, omg);
            }
        } else {
            LOGGER.info("shareConsultant_推荐顾问时参数异常cmfUserId: {}, customerName: {}, " +
                    "customerMobile: {}, referrer: {}, consultant: {}, consultantMobile: {}",
                sessionCmfUserId, customerName, customerMobile, referrer, consultant, consultantMobile);
        }
        return result.toString();
    }

    /**
     * 可用积分兑换的物品信息
     */
    @RequestMapping(value = "/business/integral/getIntegralGoods.xhtml", produces = "text/html;charset=UTF-8",
        method = RequestMethod.GET)
    @ResponseBody
    String getIntegralGoods(HttpServletResponse response, HttpServletRequest request) {
        List list = userIntegralManager.listIntegralGoods();
        JSONObject result = ECConstants.ERR_JSON_RESULT;
        if (list != null) {
            result = new JSONObject().accumulate("data", list)
                .accumulate(ECConstants.ERR_CODE, ECConstants.COMMON_SUCCESS);
        }

        return result.toString();
    }

    /**
     * 是否展示邀请码输入框
     */
    @RequestMapping(value = "/business/integral/recommendInputBox.xhtml", produces = "text/html;charset=UTF-8",
        method = RequestMethod.GET)
    @ResponseBody
    String showRecommendInputBox(HttpServletResponse response, HttpServletRequest request) {
        JSONObject result = ECConstants.ERR_JSON_RESULT;
        String sessionCmfUserId = RequestHelper.getSessionCmfUserId(request);
        if (isAuthenticationUser(request) && StringUtils.isNotBlank(sessionCmfUserId)) {
            try {
                boolean newUserNotInvited = userIntegralManager.isNewUserNotInvited(sessionCmfUserId);
                result = new JSONObject().accumulate("show", newUserNotInvited ? "1" : "0")
                    .accumulate(ECConstants.ERR_CODE, ECConstants.COMMON_SUCCESS);
            } catch (Exception omg) {
                LOGGER.error("showRecommendInputBox_", omg);
            }
        } else {
            // session中没有当前用户的ID或ID异常将展示邀请码输入框
            result = new JSONObject().accumulate("show", "1")
                .accumulate(ECConstants.ERR_CODE, ECConstants.COMMON_SUCCESS);
        }
        return result.toString();
    }

    /**
     * 积分物品兑换
     */
    @RequestMapping(value = "/business/integral/goodsExchange.xhtml", produces = "text/html;charset=UTF-8",
        method = RequestMethod.POST)
    @ResponseBody
    String goodsExchange(HttpServletResponse response, HttpServletRequest request) {
        JSONObject result = ECConstants.ERR_JSON_RESULT;
        String integralBeforeChange = request.getParameter("integralBeforeChange");
        String sessionCmfUserId = RequestHelper.getSessionCmfUserId(request);
        String integralChange = request.getParameter("integralChange");
        String goodsId = request.getParameter("goodsId");

        boolean correctParameters = StringUtils.isNotBlank(goodsId) && StringUtils.isNotBlank(sessionCmfUserId)
            && StringUtils.isNotBlank(integralChange) && StringUtils.isNotBlank(integralBeforeChange);

        if (isAuthenticationUser(request) && correctParameters) {
            LOGGER.info("goodsExchange_积分物品兑换_goodsId: " + goodsId + "客户端IP: " + request.getHeader("x-forwarded-for"));
            try {
                boolean successful = userIntegralManager.goodsExchange(sessionCmfUserId, Integer.parseInt(goodsId),
                    Long.parseLong(integralBeforeChange), Long.parseLong(integralChange));
                result = new JSONObject().accumulate("data", successful ? "1" : "0")
                    .accumulate(ECConstants.ERR_CODE, ECConstants.COMMON_SUCCESS);
            } catch (Exception omg) {
                LOGGER.error("goodsExchange_", omg);
            }
        }

        return result.toString();
    }

    /**
     * 通过邀请码绑定推荐人
     */
    @RequestMapping(value = "/business/integral/bindingReferrer.xhtml", produces = "text/html;charset=UTF-8",
        method = RequestMethod.POST)
    @ResponseBody
    String bindingReferrer(HttpServletResponse response, HttpServletRequest request) {
        String sessionCmfUserId = RequestHelper.getSessionCmfUserId(request);
        String invitationCode = request.getParameter("invitationCode");
        JSONObject result;

        if (StringUtils.isNotBlank(sessionCmfUserId) && StringUtils.isNotBlank(invitationCode)) {
            boolean bindingSuccess = userIntegralManager.bindingReferrer(sessionCmfUserId, invitationCode);
            result = new JSONObject().accumulate("data", bindingSuccess ? "1" : "0")
                .accumulate(ECConstants.ERR_CODE, ECConstants.COMMON_SUCCESS);
        } else {
            result = ECConstants.ERR_JSON_RESULT;
        }
        return result.toString();
    }

    /**
     * 通过手机号绑定推荐人
     */
    @RequestMapping(value = "/business/integral/bindingReferrerByPhoneNumber.xhtml", produces = "text/html;charset=UTF-8",
        method = RequestMethod.POST)
    @ResponseBody
    String bindingReferrerByPhoneNumber(HttpServletResponse response, HttpServletRequest request) {
        String sessionCmfUserId = RequestHelper.getSessionCmfUserId(request);
        String phoneNumber = request.getParameter("phoneNumber");
        JSONObject result;

        if (StringUtils.isNotBlank(sessionCmfUserId) && StringUtils.isNotBlank(phoneNumber)) {
            boolean bindingSuccess = userIntegralManager.bindingReferrerByPhoneNumber(sessionCmfUserId, phoneNumber);
            result = new JSONObject().accumulate("data", bindingSuccess ? "1" : "0")
                .accumulate(ECConstants.ERR_CODE, ECConstants.COMMON_SUCCESS);
        } else {
            result = ECConstants.ERR_JSON_RESULT;
        }
        return result.toString();
    }

    /**
     * 热门产品
     */
    @RequestMapping(value = "/business/integral/exhibitionFund.xhtml", produces = "text/html;charset=UTF-8",
        method = RequestMethod.GET)
    @ResponseBody
    String exhibitionFund(HttpServletResponse response, HttpServletRequest request) {
        JSONObject result;
        List fundList = null;
        try {
            fundList = userIntegralManager.listExhibitionFund();
        } catch (Exception omg) {
            LOGGER.error("exhibitionFund_exhibitionFund", omg);
        }
        if (null != fundList) {
            result = new JSONObject().accumulate("fundList", fundList)
                .accumulate(ECConstants.ERR_CODE, ECConstants.COMMON_SUCCESS);
        } else {
            result = ECConstants.ERR_JSON_RESULT;
        }
        return result.toString();
    }

    @RequestMapping(value = "/integral/enableIntegral.xhtml", produces = "text/html;charset=UTF-8",
        method = RequestMethod.GET)
    @ResponseBody
    String enableIntegral(HttpServletResponse response, HttpServletRequest request) {
        JSONObject result = ECConstants.ERR_JSON_RESULT;
        result = getSuccessResult().accumulate("data", userIntegralManager.enableIntegral() ? 1 : 0);
        return result.toString();
    }

    /**
     * 判断用户是否实名鉴权
     *
     * @param request HttpServletRequest
     * @return <tt>true</tt> 已实名鉴权
     */
    private boolean isAuthenticationUser(HttpServletRequest request) {
        UserBaseInfoDto sessionUserBaseInfo = RequestHelper.getSessionUserBaseInfo(request);
        return null != sessionUserBaseInfo && "30".equals(sessionUserBaseInfo.getUserType());
    }

    /**
     * 通用成功返回
     */
    private JSONObject getSuccessResult() {
        return new JSONObject().accumulate(ECConstants.ERR_CODE, ECConstants.COMMON_SUCCESS);
    }

}


