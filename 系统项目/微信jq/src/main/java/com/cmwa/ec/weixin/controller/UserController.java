package com.cmwa.ec.weixin.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.message.facade.dto.MsgParameterDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.trade.AppointRequestDto;
import com.cmwa.ec.user.facade.dto.AppointInfoDto;
import com.cmwa.ec.user.facade.dto.BuriedDataDto;
import com.cmwa.ec.user.facade.dto.CustInfoDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserOperateLogDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.user.facade.dto.UserTaxInfoDto;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.client.UserServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dto.InvectorUserInfoexDto;
import com.cmwa.ec.weixin.interceptor.TracingInfo;
import com.cmwa.ec.weixin.manager.business.ActivityManager;
import com.cmwa.ec.weixin.manager.business.MessageManager;
import com.cmwa.ec.weixin.manager.business.QueryManager;
import com.cmwa.ec.weixin.manager.business.UserInfoexManager;
import com.cmwa.ec.weixin.model.ModifyTransPasswordModel;
import com.cmwa.ec.weixin.model.ResetLoginPasswordModel;
import com.cmwa.ec.weixin.model.ResetTransPasswordModel;
import com.cmwa.ec.weixin.util.ContextUtils;
import com.cmwa.ec.weixin.util.HttpPostUtil;
import com.cmwa.ec.weixin.util.MD5;
import com.cmwa.ec.weixin.util.RequestHelper;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.StringHelper;
import com.cmwa.ec.weixin.util.StringUtils;
import com.cmwa.ec.weixin.util.ValidationUtils;
import com.cmwa.ec.weixin.util.cache.ParameterCache;
import com.cmwa.ec.weixin.validator.UserValidator;

/**
 * 类说明：查询模块请求处理
 * 
 * @author liury
 * 
 */
@Controller("UserController")
@RequestMapping(value = "/WeixinService")
public class UserController {

    private static Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserInfoexManager userInfoexManager;

    @Autowired
    private MessageManager messageManager;
    @Autowired
    private QueryManager queryManager;
    @Autowired
    private UserServiceClient serviceClient;
    @Autowired
    private UserServiceClient userServiceClient;
    @Autowired
    private ActivityManager activityManager;
    @Autowired
    private QueryServiceClient queryServiceClient;

    /**
     * 
     */
    @Value("wx.config.wxHost")
    private String wxhost;
    
    /**
     * 用户注册 非微信浏览器
     * 
     * @param response
     * @param request
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             void
     * @author maj
     */
    @RequestMapping(value = "/setUp/webRegister.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String webRegister(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String seqId = request.getSession().getId();
        String busiChannel = RequestHelper.verfiyIEChannel(request);// 请求来源
                                                                    // 微信浏览器还是其他浏览器

        String mobile = request.getParameter("mobile");
        String passWord = request.getParameter("passWord");
        String sessionID = request.getParameter("sessionID");
        String smsCode = request.getParameter("smsCode");

        logger.debug("【UserController】webRegister()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>timestamp" + System.currentTimeMillis());
        logger.debug("【UserController】webRegister()获取前台参数>>>mobile=" + mobile + ">>>passWord+" + passWord + ">>>sessionID+" + sessionID + ">>>smsCode" + smsCode);

        JSONObject returnJsonObject = new JSONObject();

        // 验证发送手机短信验证码是保存在session中的手机号码
        String smsMobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_USERMOBILE);
        if (smsMobile == null) {
            returnJsonObject.put("returnCode", "9998");// 未获取到发送验证码时，session中保存的手机号码
            return returnJsonObject.toString();
        } else if (!smsMobile.equals(mobile)) {
            returnJsonObject.put("returnCode", "9997");// 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致
            return returnJsonObject.toString();
        }

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        // 验证短信验证码
        String returnStr = messageManager.checkVrfCode(context, sessionID, mobile, smsCode);

        if (returnStr == null || !WXConstants.COMMON_SUCCESS.equals(returnStr)) {
            returnJsonObject.put("returnCode", "9996");// 手机短信验证码验证失败
            return returnJsonObject.toString();
        }

        MD5 md5 = new MD5();
        if (passWord != null) {
            passWord = md5.getMD5ofStr(passWord);
        }

        returnJsonObject = userInfoexManager.registerNormalUserWithT(request, context, mobile, "1", passWord, WXConstants.LOGIN_CHANEL_04, WXConstants.LOGIN_NMARK_04);

        String returnCode = returnJsonObject.getString("returnCode");
        if ("0000".equals(returnCode)) {// 删除session中保存的发送过手机短信验证码的手机号码
            request.getSession(true).removeAttribute(SessionValue.SESSION_USERMOBILE);
        }

        logger.debug("【UserController】webRegister()结束>>>returnJsonObject=" + returnJsonObject.toString() + ">>>timestamp" + System.currentTimeMillis());

        return returnJsonObject.toString();
    }

    /**
     * 用户注册 微信浏览器
     * 
     * @param response
     * @param request
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             void
     * @author liury
     */
    @RequestMapping(value = "/user/weixinRegister.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String weixinRegister(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String seqId = request.getSession().getId();
        String busiChannel = RequestHelper.verfiyIEChannel(request);// 请求来源
                                                                    // 微信浏览器还是其他浏览器

        String mobile = request.getParameter("mobile");
        String passWord = request.getParameter("passWord");
        String sessionID = request.getParameter("sessionID");
        String smsCode = request.getParameter("smsCode");

        logger.debug("【UserController】weixinRegister()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>timestamp" + System.currentTimeMillis());
        logger.debug("【UserController】weixinRegister()获取前台参数>>>mobile=" + mobile + ">>>passWord+" + passWord + ">>>sessionID+" + sessionID + ">>>smsCode" + smsCode);

        JSONObject returnJsonObject = new JSONObject();

        // 验证发送手机短信验证码是保存在session中的手机号码
        String smsMobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_USERMOBILE);
        if (smsMobile == null) {
            returnJsonObject.put("errorCode", "9998");// 未获取到发送验证码时，session中保存的手机号码
            return returnJsonObject.toString();
        } else if (!smsMobile.equals(mobile)) {
            returnJsonObject.put("errorCode", "9997");// 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致
            return returnJsonObject.toString();
        }

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        // 验证短信验证码
        String returnStr = messageManager.checkVrfCode(context, sessionID, mobile, smsCode);

        if (returnStr == null || !WXConstants.COMMON_SUCCESS.equals(returnStr)) {
            returnJsonObject.put("errorCode", "9996");// 手机短信验证码验证失败
            return returnJsonObject.toString();
        }

        MD5 md5 = new MD5();
        if (passWord != null) {
            passWord = md5.getMD5ofStr(passWord);
        }

        returnJsonObject = userInfoexManager.registerNormalUserWithT(request, context, mobile, "1", passWord, WXConstants.LOGIN_CHANEL_03, WXConstants.LOGIN_NMARK_03);

        String returnCode = returnJsonObject.getString("returnCode");
        if ("0000".equals(returnCode)) {// 删除session中保存的发送过手机短信验证码的手机号码
            request.getSession(true).removeAttribute(SessionValue.SESSION_USERMOBILE);

            if (busiChannel.equals(WXConstants.WEIXIN_CHANNEL)) {
                String msg = userInfoexManager.bindWeixin(request, context);
                logger.info("注册成功后，绑定微信，结果：" + msg);
            }
        }

        logger.debug("【UserController】weixinRegister()结束>>>returnJsonObject=" + returnJsonObject.toString() + ">>>timestamp" + System.currentTimeMillis());

        return returnJsonObject.toString();
    }

    /**
     * 账号登录
     * 
     * @param response
     * @param request
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             void
     * @author maj
     */
    @RequestMapping(value = "/setUp/webLogin.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String webLogin(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        logger.info("【UserController】webLogin(),otherIE端登录....");
        String busiChannel = RequestHelper.verfiyIEChannel(request);

        String seqId = request.getSession().getId();

        String randomCode = request.getParameter("randomCode");

        logger.debug("【UserController】webLogin()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>randomCode=" + randomCode + ">>>timestamp"
                + System.currentTimeMillis());

        // 验证图片验证码
        Map<String, String> map = VerifyCodeController.verifyRandom(request, randomCode);

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        JSONObject json = null;

        // 登录
        if (map != null && map.get("returnCode").equals("0000")) {
            json = userInfoexManager.login(request, context, WXConstants.LOGIN_CHANEL_04, WXConstants.LOGIN_NMARK_04);
        } else {
            json = JSONObject.fromObject(map);
        }

        logger.info("【UserController】webLogin()结束>>>returnStr=" + json.toString() + ">>>timestamp" + randomCode + System.currentTimeMillis());

        return json.toString();
    }

    /**
     * 账号登录,微信端
     * 
     * @param response
     * @param request
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             void
     * @author liury
     */
    @RequestMapping(value = "/user/weixinLogin.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String weixinLogin(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        logger.info("【UserController】weixinLogin(),微信端登录....");
        String busiChannel = RequestHelper.verfiyIEChannel(request);

        String seqId = request.getSession().getId();

        String randomCode = request.getParameter("randomCode");

        // 验证图片验证码
        Map<String, String> map = VerifyCodeController.verifyRandom(request, randomCode);

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        JSONObject json = null;

        // 登录
        if (map != null && map.get("returnCode").equals("0000")) {
            json = userInfoexManager.login(request, context, WXConstants.LOGIN_CHANEL_03, WXConstants.LOGIN_NMARK_03);
        } else {
            json = JSONObject.fromObject(map);
        }

        // 绑定微信，并且推送绑定成功消息
        if (json != null) {
            String returnCode = (String) json.get("returnCode");
            if (returnCode != null && WXConstants.COMMON_SUCCESS.equals(returnCode) && busiChannel.equals(WXConstants.WEIXIN_CHANNEL)) {
                String msg = userInfoexManager.bindWeixin(request, context);
                logger.info("登录成功后，绑定微信，结果：" + msg);
            }
        }

        logger.info("【UserController】weixinLogin()结束>>>returnStr=" + json.toString() + ">>>timestamp" + randomCode + System.currentTimeMillis());

        return json.toString();
    }

    /***
     * 验证是否绑定
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    @RequestMapping(value = "/setUp/checkLogin.xhtml", method = { RequestMethod.POST })
    @ResponseBody
    public String ajaxCheckBind(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        JSONObject json = new JSONObject();
        String returnCode = null;
        Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (null != obj) {
            returnCode = WXConstants.COMMON_SUCCESS;
        } else {
            returnCode = WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE;
        }

        json.put("returnCode", returnCode);

        return json.toString();

    }

    /***
     * 发送购买产品通知消息
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    @RequestMapping(value = "/setUp/sendSmsMsg.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String sendSmsMsg(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String busiChannel = RequestHelper.verfiyIEChannel(request);

        JSONObject returnJsonObject = new JSONObject();

        // 手机号码
        String mobile = request.getParameter("mobile");

        UserBaseInfoDto userBaseInfo = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfo == null) {
            returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
            returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
            return returnJsonObject.toString();
        } else {
            mobile = userBaseInfo.getMobile();

        }

        String bankNumber = request.getParameter("bankNumber");
        bankNumber = (bankNumber != null && !"".equals(bankNumber)) ? bankNumber.trim() : bankNumber;

        String bankName = request.getParameter("bankName");
        bankName = (bankName != null && !"".equals(bankName)) ? bankName.trim() : bankName;
        bankName = URLDecoder.decode(bankName, "UTF-8");

        String money = request.getParameter("money");
        money = (money != null && !"".equals(money)) ? money.trim() : money;

        String fundName = request.getParameter("fundName");
        fundName = (fundName != null && !"".equals(fundName)) ? fundName.trim() : fundName;
        fundName = URLDecoder.decode(fundName, "UTF-8");

        String msgType = request.getParameter("msgType");
        msgType = (msgType != null && !"".equals(msgType)) ? msgType.trim() : msgType;

        String appointEndDate = request.getParameter("appointEndDate");
        appointEndDate = (appointEndDate != null && !"".equals(appointEndDate)) ? appointEndDate.trim() : appointEndDate;
        appointEndDate = URLDecoder.decode(appointEndDate, "UTF-8");

        MsgParameterDto msg = new MsgParameterDto();

        msg.setMobile(mobile);
        msg.setMsgType(msgType);
        msg.setMsgPar_amount(money);
        msg.setMsgPar_bankCard(bankNumber);
        msg.setMsgPar_bankName(bankName);
        msg.setMsgPar_fundName(fundName);
        msg.setMsgPar_str1(appointEndDate);// 预约截止日

        String seqId = request.getSession().getId();
        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        String serialNo = request.getParameter("serialNo");
        QueryMessageDto queryMessageDto = queryServiceClient.QueryAppointRequestList(serialNo);
        List<AppointRequestDto> appointRequestList = (List<AppointRequestDto>) queryMessageDto.getData();
        AppointRequestDto appointRequestDto = appointRequestList == null || appointRequestList.isEmpty() ? null : appointRequestList.get(0);
        MsgSmsRecordDto msgRecord = new MsgSmsRecordDto();
        msgRecord.setMethod("A");
        msgRecord.setSendUser("system");
        if (appointRequestDto != null) {
            msgRecord.setClientName(appointRequestDto.getCustName());
            msgRecord.setFundCode(appointRequestDto.getFundid());
            msgRecord.setProductName(appointRequestDto.getFundAdName());
        }
        returnJsonObject = messageManager.sendSmsMsg(context, msg, msgRecord);

        return returnJsonObject.toString();
    }

    /**
     * 注册页面，验证手机号码是否被注册
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/setUp/verifyMobile.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String verifyMobile(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String busiChannel = RequestHelper.verfiyIEChannel(request);

        // 手机号码
        String mobile = request.getParameter("mobile");

        String seqId = request.getSession().getId();

        logger.debug("【UserController】verifyMobile()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>mobile=" + mobile + ">>>timestamp" + System.currentTimeMillis());

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        JSONObject returnJsonObject = userInfoexManager.verifyMobile(context, mobile);

        logger.debug("【UserController】verifyMobile()结束>>>returnJsonObject=" + returnJsonObject.toString() + ">>>timestamp" + System.currentTimeMillis());

        return returnJsonObject.toString();
    }

    /**
     * 验证手机号码和返回手机验证码 注册页面发送手机短信验证码
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/setUp/getMobileVerifyCode.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String getMobileVerifyCode(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String busiChannel = RequestHelper.verfiyIEChannel(request);

        long systemTime = System.currentTimeMillis() / 1000;

        Object tempStr = request.getSession(true).getAttribute(SessionValue.SESSION_TIMESTAMP);

        String timeStamp = "";

        if (null != tempStr) {
            timeStamp = tempStr.toString();
        }

        // 手机号码
        String mobile = request.getParameter("mobile").trim();

        String seqId = request.getSession().getId();

        logger.debug("【UserController】getMobileVerifyCode()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>mobile=" + mobile + ">>>timestamp"
                + System.currentTimeMillis());

        JSONObject returnJsonObject = new JSONObject();
        // session中的timeStamp为空，说明不是通过图片验证码页面跳转过来的
        // 则返回到图片验证码页面
        if (null == timeStamp || "".equals(timeStamp)) {
            logger.info("获取手机验证码，图片验证码session时间不存在");
            returnJsonObject.put("sessionTime", "null");
            return returnJsonObject.toString();
        }

        long sessionTime = (Long) request.getSession(true).getAttribute(SessionValue.SESSION_TIMESTAMP);
        // 如果当前系统秒数大于session中存取的系统时间秒数 则说明操作时间已经超过5分钟
        // 则超时，返回到第一个验证图片验证码页面
        if (systemTime > sessionTime) {
            logger.info("获取手机验证码，图片验证码session时间不存在或者过期");
            returnJsonObject.put("sessionTime", "timeOut");
            return returnJsonObject.toString();
        }
        String randomChannel = (String) request.getSession(true).getAttribute(SessionValue.SESSION_RANDOMCHANNEL);
        if (randomChannel == null || !randomChannel.equals("regRandom")) {
            logger.info("获取手机验证码，未验证图片验证码");
            returnJsonObject.put("sessionTime", "timeOut");
            return returnJsonObject.toString();
        }

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        returnJsonObject = userInfoexManager.verifyMobileAndGetVerifyCode(context, mobile, WXConstants.MOBILE_TYPE_0, RequestHelper.getIpAddr(request), seqId);

        String returnCode = returnJsonObject.getString("errorCode");

        // 验证码发送成功，删除验证图片验证码时保存的randomChannel，
        // 将用户手机号码保存到session中
        if (returnCode != null && returnCode.equals("0000")) {
            request.getSession(true).setAttribute(SessionValue.SESSION_USERMOBILE, mobile);
        }

        logger.debug("【UserController】getMobileVerifyCode()结束>>>returnJsonObject=" + returnJsonObject.toString() + ">>>timestamp" + System.currentTimeMillis());

        return returnJsonObject.toString();

    }

    /**
     * 验证手机号码和返回手机验证码 找回登录密码页面发送手机短信验证码
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/setUp/getVerifyCodeByResetLpsw.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String getVerifyCodeByResetLpsw(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String busiChannel = RequestHelper.verfiyIEChannel(request);

        long systemTime = System.currentTimeMillis() / 1000;

        Object tempStr = request.getSession(true).getAttribute(SessionValue.SESSION_TIMESTAMP);
        // 手机号码
        String mobile = request.getParameter("mobile");

        String seqId = request.getSession().getId();

        Integer resetLPWGapTime = Integer.parseInt(SpringUtil.getProperty("resetLPWGapTime"));

        logger.debug("【UserController】getVerifyCodeByResetLpsw()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>mobile=" + mobile + ">>>timestamp"
                + System.currentTimeMillis());

        String timeStamp = "";

        if (null != tempStr) {
            timeStamp = tempStr.toString();
        }

        JSONObject returnJsonObject = new JSONObject();

        if (StringUtils.isEmptyString(mobile)) {
            returnJsonObject.put("errorCode", WXConstants.RETURN_CODE_9008);
            returnJsonObject.put("errorMsg", WXConstants.RETURN_MSG_9008);
            return returnJsonObject.toString();
        }

        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);

        boolean isMatch = UserValidator.isMatchMobileWithResetLoginPwd(userBaseInfoDto != null ? userBaseInfoDto.getMobile() : "", mobile);
        if (!isMatch) {// 当前登录手机号与修改密码手机号不一致
            returnJsonObject.put("errorCode", WXConstants.MODIFY_LOGIN_PWD_MOBILE_NOT_MATCH);
            returnJsonObject.put("errorMsg", "当前登录手机号与修改密码手机号不一致");
            return returnJsonObject.toString();
        }

        // session中的timeStamp为空，说明不是通过图片验证码页面跳转过来的
        // 则返回到图片验证码页面
        if (null == timeStamp || "".equals(timeStamp)) {
            logger.info("获取手机验证码，图片验证码session时间不存在");
            returnJsonObject.put("sessionTime", "null");
            return returnJsonObject.toString();
        }

        long sessionTime = (Long) request.getSession(true).getAttribute(SessionValue.SESSION_TIMESTAMP);
        // 如果当前系统秒数大于session中存取的系统时间秒数 则说明操作时间已经超过5分钟
        // 则超时，返回到第一个验证图片验证码页面
        if (systemTime > sessionTime) {
            logger.info("获取手机验证码，图片验证码session时间不存在或者过期");
            returnJsonObject.put("sessionTime", "timeOut");
            return returnJsonObject.toString();
        }

        String randomChannel = (String) request.getSession(true).getAttribute(SessionValue.SESSION_RANDOMCHANNEL);
        if (randomChannel == null || !randomChannel.equals("resetRandom")) {
            logger.info("获取手机验证码，未验证图片验证码");
            returnJsonObject.put("sessionTime", "timeOut");
            return returnJsonObject.toString();
        }

        // 验证 图形验证码通过标识
        String resetLPWFlag = (String) request.getSession(true).getAttribute(SessionValue.SESSION_RESETLPWVERFLAG);
        if (null == resetLPWFlag || (null != resetLPWFlag && !WXConstants.COMMON_SUCCESS.equals(resetLPWFlag))) {
            returnJsonObject.put("errorCode", WXConstants.COMMON_ERROR_ILLEGALREQCODE);
            returnJsonObject.put("errorMsg", WXConstants.COMMON_ERROR_ILLEGALREQMSG);
            return returnJsonObject.toString();
        }

        // 验证发送短信时间间隔
        Long lastSendTime = (Long) request.getSession(true).getAttribute(SessionValue.SESSION_RESETLPWMSGTIME);
        if (null != lastSendTime) {
            Long nowTime = new Date().getTime();
            Long interval = (lastSendTime - nowTime) / 1000;// 取秒数

            // 未到 间隔时间提示 操作太频繁
            if (interval > 0) {
                returnJsonObject.put("errorCode", WXConstants.RETURN_CODE_9007);
                returnJsonObject.put("errorMsg", WXConstants.RETURN_MSG_9007);
                return returnJsonObject.toString();
            }
        }

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        returnJsonObject = userInfoexManager.verifyMobileAndGetVerifyCode(context, mobile, WXConstants.MOBILE_TYPE_4, RequestHelper.getIpAddr(request), seqId);

        String returnCode = returnJsonObject.getString("errorCode");

        // 验证码发送成功，删除验证图片验证码时保存的randomChannel，
        // 将用户手机号码保存到session中
        if (returnCode != null && returnCode.equals("0000")) {
            // 发送成功则 记录 下次时间 下次验证时 必须超过这个时间
            Calendar gapCalendar = Calendar.getInstance();
            gapCalendar.setTime(new Date());
            gapCalendar.add(Calendar.SECOND, resetLPWGapTime);
            Long diffTime = gapCalendar.getTime().getTime();
            request.getSession(true).setAttribute(SessionValue.SESSION_RESETLPWMSGTIME, diffTime);
            // request.getSession(true).removeAttribute(SessionValue.SESSION_RANDOMCHANNEL);
            request.getSession(true).setAttribute(SessionValue.SESSION_USERMOBILE, mobile);
            returnJsonObject.put("resetLPWMsgTime", diffTime);
        }

        logger.debug("【UserController】getVerifyCodeByResetLpsw()结束>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>mobile=" + mobile + ">>>timestamp"
                + System.currentTimeMillis());

        return returnJsonObject.toString();

    }

    /**
     * 验证手机号码和返回手机验证码 需判断是否验证支付密码 修改用户注册手机号码 发送新手机号码短信验证码
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/setUp/getVerifyCodeByModifyMobile.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String getVerifyCodeByModifyMobile(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String busiChannel = RequestHelper.verfiyIEChannel(request);

        // 手机号码
        String mobile = request.getParameter("mobile");

        String seqId = request.getSession().getId();

        logger.debug("【UserController】getVerifyCodeByModifyMobile()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>mobile=" + mobile + ">>>timestamp"
                + System.currentTimeMillis());

        JSONObject jsonObject = new JSONObject();

        String hasCheckedTpsw = (String) request.getSession(true).getAttribute(SessionValue.SESSION_HASCHECKEDTPSW);
        if (hasCheckedTpsw == null || !hasCheckedTpsw.equals("1")) {
            logger.debug("【UserController】getVerifyCodeByModifyMobile()中没有验证支付密码");
            jsonObject = new JSONObject();
            jsonObject.put("returnCode", "9000");
            jsonObject.put("returnMsg", "请先验证支付密码");
            return jsonObject.toString();
        }

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        String returnCode = "";
        JSONObject returnJsonObject = userInfoexManager.verifyMobile(context, mobile);
        String hasSetLPsw = "";
        if ("0000".equals(returnJsonObject.getString("returnCode")) || "USR-A000".equals(returnJsonObject.getString("returnCode"))) {

            if ("USR-A000".equals(returnJsonObject.getString("returnCode"))) {// 注册用户
                hasSetLPsw = "Y";// 是否需要设置登陆密码
                // request.getSession(true).setAttribute(SessionValue.SESSION_HASSETLPSW,
                // "Y");

            } else if ("0000".equals(returnJsonObject.getString("returnCode"))) {
                hasSetLPsw = "N";// 是否需要设置登陆密码
                // request.getSession(true).setAttribute(SessionValue.SESSION_HASSETLPSW,
                // "N");
            } else {
                return returnJsonObject.toString();
            }
            jsonObject = userInfoexManager.verifyMobileAndGetVerifyCode(context, mobile, WXConstants.MOBILE_TYPE_6, RequestHelper.getIpAddr(request), seqId);

            returnCode = jsonObject.getString("errorCode");

            // 将用户手机号码保存到session中
            if (returnCode != null && returnCode.equals("0000")) {
                // request.getSession(true).removeAttribute(SessionValue.SESSION_HASCHECKEDTPSW);
                jsonObject.put("hasSetLPsw", hasSetLPsw);
                request.getSession(true).setAttribute(SessionValue.SESSION_USERMOBILE, mobile);
                request.getSession(true).setAttribute(SessionValue.SESSION_HASSETLPSW, hasSetLPsw);
            }
        } else {
            return returnJsonObject.toString();
        }

        logger.debug("【UserController】getVerifyCodeByModifyMobile()结束>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>mobile=" + mobile + ">>>timestamp"
                + System.currentTimeMillis());

        return jsonObject.toString();

    }

    /**
     * 验证手机短信验证码 找回登录密码/修改支付密码
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/setUp/checkSmsCode.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String checkSmsCode(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String busiChannel = RequestHelper.verfiyIEChannel(request);

        // 手机号码
        String mobile = request.getParameter("mobile");
        String sessionID = request.getParameter("sessionID");
        String smsCode = request.getParameter("smsCode");

        String seqId = request.getSession().getId();

        logger.debug("【UserController】checkSmsCode()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>timestamp" + System.currentTimeMillis());
        logger.debug("【UserController】checkSmsCode()获取前台参数>>>mobile=" + mobile + ">>>sessionID+" + sessionID + ">>>smsCode" + smsCode);

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        JSONObject returnJsonObject = new JSONObject();

        // 验证发送手机短信验证码是保存在session中的手机号码
        String smsMobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_USERMOBILE);
        if (smsMobile == null) {
            returnJsonObject.put("returnCode", "9998");// 未获取到发送验证码时，session中保存的手机号码
            return returnJsonObject.toString();
        } else if (!smsMobile.equals(mobile)) {
            returnJsonObject.put("returnCode", "9997");// 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致
            return returnJsonObject.toString();
        }

        // 验证短信验证码
        String returnStr = messageManager.checkVrfCode(context, sessionID, mobile, smsCode);

        if (returnStr != null && returnStr.equals(WXConstants.COMMON_SUCCESS)) {
            returnJsonObject.put("returnCode", returnStr);
            returnJsonObject.put("returnMsg", "成功");

            request.getSession(true).removeAttribute(SessionValue.SESSION_USERMOBILE);// 删除session中发送短信验证码时保存的手机号码
            request.getSession(true).setAttribute(SessionValue.SESSION_MSGMOBILE, mobile);// 手机短信验证码验证通过，将手机号码保存在session中
            request.getSession(true).removeAttribute(SessionValue.SESSION_RESETLPWVERFLAG);
            request.getSession(true).removeAttribute(SessionValue.SESSION_RESETLPWMSGTIME);
        } else if (returnStr.equals("USR-5001")) {
            returnJsonObject.put("returnCode", returnStr);
            returnJsonObject.put("returnMsg", "关键参数为空");
        } else if (returnStr.equals("USR-5201")) {
            returnJsonObject.put("returnCode", returnStr);
            returnJsonObject.put("returnMsg", "验证失败，验证码错误");
        } else if (returnStr.equals("USR-5202")) {
            returnJsonObject.put("returnCode", returnStr);
            returnJsonObject.put("returnMsg", "验证失败,会话失效");
        } else if (returnStr.equals("USR-5203")) {
            returnJsonObject.put("returnCode", returnStr);
            returnJsonObject.put("returnMsg", "验证失败，会话已验证");
        } else {
            returnJsonObject.put("returnCode", returnStr);
            returnJsonObject.put("returnMsg", "验证失败");
        }

        logger.debug("【UserController】checkSmsCode()结束>>>returnJsonObject=" + returnJsonObject.toString() + ">>>timestamp" + System.currentTimeMillis());

        return returnJsonObject.toString();
    }

    /**
     * 重置登录密码 根据手机号码
     * 
     * @param response
     * @param request
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             void
     * @author maj
     */
    @RequestMapping(value = "/setUp/resetLPassWord.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String resetPassword(HttpServletResponse response, HttpServletRequest request, ResetLoginPasswordModel resetLoginPasswordModel) throws Exception {
        JSONObject returnJsonObject = new JSONObject();
        // 校验参数
        if (ValidationUtils.validate(resetLoginPasswordModel) != null) {
            returnJsonObject.put("returnCode", WXConstants.PARAMETER_CHECK_NOT_PASS);
            returnJsonObject.put("message", ValidationUtils.validate(resetLoginPasswordModel));
            return returnJsonObject.toString();
        }

        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);

        boolean isMatch = UserValidator.isMatchMobileWithResetLoginPwd(userBaseInfoDto != null ? userBaseInfoDto.getMobile() : "", resetLoginPasswordModel.getMobile());
        if (!isMatch) {// 当前登录手机号与修改密码手机号不一致
            returnJsonObject.put("returnCode", WXConstants.MODIFY_LOGIN_PWD_MOBILE_NOT_MATCH);
            return returnJsonObject.toString();
        }

        String mobile = resetLoginPasswordModel.getMobile();
        String passWord = resetLoginPasswordModel.getPassWord();

        logger.debug("【UserController】resetLPassWord()开始>>>seqId=" + request.getSession().getId() + ">>>busiChannel+" + RequestHelper.verfiyIEChannel(request) + ">>>mobile+"
                + mobile + ">>>passWord+" + passWord + ">>>timestamp" + System.currentTimeMillis());

        String msgMobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_MSGMOBILE);
        if (!UserValidator.isPassSmsVerify(mobile, msgMobile)) {
            returnJsonObject.put("returnCode", WXConstants.SMS_VERIFY_CODE_FAIL);// 未验证手机短信验证码或者验证的手机短信验证码的手机号码和传递过来的手机号码不一致
            return returnJsonObject.toString();
        }

        passWord = new MD5().getMD5ofStr(passWord);
        returnJsonObject = userInfoexManager.resetUserPassword(null, mobile, passWord);

        String returnCode = returnJsonObject.getString("returnCode");

        if ("0000".equals(returnCode)) {
            request.getSession(true).removeAttribute(SessionValue.SESSION_MSGMOBILE);// 删除保存在session中验证手机短信验证码成功的手机号码
            /* 20180529新增用户修改密码成功之后跳转到登陆页面并将session清空 */
            Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
            request.getSession(true).invalidate();
            String openId = "";
            if (obj != null) { // 为避免过滤器自动登陆生效
                openId = obj.toString();
                userInfoexManager.updatecmfUserid(openId, "", "C");// C:解除绑定
                                                                   // R：正常绑定
            }
        }

        logger.debug("【UserController】resetLPassWord()结束>>>returnJsonObject=" + returnJsonObject.toString() + ">>>timestamp" + System.currentTimeMillis());

        return returnJsonObject.toString();
    }

    /**
     * 验证支付密码 返回用户手机号码
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     *             String
     * @author maj
     */
    @RequestMapping(value = "/business/checkTpassword.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "30")
    @ResponseBody
    public String checkTpassword(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String seqId = request.getSession().getId();
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String tpassword = request.getParameter("tPassWord");// 支付密码

        MD5 md5 = new MD5();
        tpassword = md5.getMD5ofStr(tpassword);

        String manageType = "V";
        String tradeChannel = WXConstants.TRADE_CHANEL_03;
        String tradeMark = WXConstants.TRADE_CHANEL_03;

        String sessionId = request.getSession().getId();

        logger.info("【UserController】checkTpassword()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>System.currentTimeMillis()=" + System.currentTimeMillis());
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

        String returnCode = "";
        String returnMsg = "";

        JSONObject returnJsonObject = new JSONObject();

        returnJsonObject = userInfoexManager.manageTpassword(context, cmfUserId, tpassword, "", manageType, tradeChannel, tradeMark);
        if (returnJsonObject != null) {
            returnCode = returnJsonObject.getString("returnCode");
            returnMsg = returnJsonObject.getString("returnMsg");
        }

        // 支付密码正确则更新session信息，将支付密码是否正确的标识符放入到session中
        if (returnCode.equals("USR-1I00")) {
            request.getSession(true).setAttribute(SessionValue.SESSION_HASCHECKEDTPSW, "1");
            String mobile = "";
            UserBaseInfoDto userBaseInfo = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
            if (userBaseInfo != null) {
                mobile = userBaseInfo.getMobile();
            }
            if (mobile != null && !mobile.equals("")) {
                returnJsonObject.put("mobile", mobile);
            }
        }
        returnJsonObject.put("returnCode", returnCode);
        returnJsonObject.put("returnMsg", returnMsg);

        logger.info("【UserController】checkTpassword()结束>>>returnJsonObject=" + returnJsonObject.toString());

        return returnJsonObject.toString();
    }

    /**
     * 验证手机号码和返回手机验证码 修改支付密码页面发送手机短信验证码
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/business/getSmsByModifyTPsw.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "30")
    @ResponseBody
    public String getSmsByModifyTPsw(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String seqId = request.getSession().getId();

        JSONObject returnJsonObject = null;
        // 手机号码
        String mobile = request.getParameter("mobile");

        Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);

        if (obj == null) {
            // 用户没有登录
            returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
            returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
            return returnJsonObject.toString();
        }
        mobile = ((UserBaseInfoDto) obj).getMobile();

        logger.debug("【UserController】getSmsByModifyTPsw()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>mobile=" + mobile + ">>>timestamp"
                + System.currentTimeMillis());

        String hasCheckedTpsw = (String) request.getSession(true).getAttribute(SessionValue.SESSION_HASCHECKEDTPSW);
        if (hasCheckedTpsw == null || !hasCheckedTpsw.equals("1")) {
            logger.debug("【UserController】getSmsByModifyTPsw()中没有验证支付密码");
            returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", "9000");
            returnJsonObject.put("returnMsg", "没有验证支付密码");
            return returnJsonObject.toString();
        }

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        returnJsonObject = userInfoexManager.verifyMobileAndGetVerifyCode(context, mobile, WXConstants.MOBILE_TYPE_4, RequestHelper.getIpAddr(request), seqId);

        String returnCode = returnJsonObject.getString("errorCode");

        // 将用户手机号码保存到session中
        if (returnCode != null && returnCode.equals("0000")) {
            request.getSession(true).setAttribute(SessionValue.SESSION_USERMOBILE, mobile);
            // request.getSession(true).removeAttribute(SessionValue.SESSION_HASCHECKEDTPSW);//
            // 将保存在session中的验证支付密码标志位删除
        }

        logger.debug("【UserController】getSmsByModifyTPsw()结束>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>mobile=" + mobile + ">>>timestamp"
                + System.currentTimeMillis());

        return returnJsonObject.toString();

    }

    /**
     * 修改支付密码
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     *             String
     * @author maj
     */
    @RequestMapping(value = "/business/modifyTPassWord.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "30")
    @ResponseBody
    public String modifyTPassWord(HttpServletResponse response, HttpServletRequest request, ModifyTransPasswordModel modifyTransPasswordModel) throws Exception {
        JSONObject returnJsonObject = new JSONObject();
        if (ValidationUtils.validate(modifyTransPasswordModel) != null) {
            returnJsonObject.put("returnCode", WXConstants.PARAMETER_CHECK_NOT_PASS);
            returnJsonObject.put("message", ValidationUtils.validate(modifyTransPasswordModel));
            return returnJsonObject.toString();
        }

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String seqId = request.getSession().getId();

        logger.info("【UserController】modifyTPassWord()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>System.currentTimeMillis()=" + System.currentTimeMillis());

        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String tpassword = StringUtils.isEmptyString(modifyTransPasswordModel.gettPassWord()) ? request.getParameter("tPassWord") : modifyTransPasswordModel.gettPassWord();// 新支付密码
        String oldTpassword = StringUtils.isEmptyString(modifyTransPasswordModel.getOldTpassWord()) ? request.getParameter("oldTpassWord") : modifyTransPasswordModel
                .getOldTpassWord();// 原支付密码
        String mobile = StringUtils.isEmptyString(modifyTransPasswordModel.getMobile()) ? request.getParameter("mobile") : modifyTransPasswordModel.getMobile();// 手机号

        MD5 md5 = new MD5();
        if (tpassword != null && !tpassword.equals("")) {
            tpassword = md5.getMD5ofStr(tpassword);
        }
        if (oldTpassword != null && !oldTpassword.equals("")) {
            oldTpassword = md5.getMD5ofStr(oldTpassword);
        }

        String manageType = "M";
        String tradeChannel = WXConstants.TRADE_CHANEL_03;
        String tradeMark = WXConstants.TRADE_CHANEL_03;

        String sessionId = request.getSession().getId();

        String msgMobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_MSGMOBILE);
        if (msgMobile == null || !msgMobile.equals(mobile)) {

            returnJsonObject.put("returnCode", "9000");
            returnJsonObject.put("returnMsg", "");
            return returnJsonObject.toString();
        }

        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

        String returnCode = "";
        String returnMsg = "";

        returnJsonObject = userInfoexManager.manageTpassword(context, cmfUserId, tpassword, oldTpassword, manageType, tradeChannel, tradeMark);
        if (returnJsonObject != null) {
            returnCode = returnJsonObject.getString("returnCode");
            returnMsg = returnJsonObject.getString("returnMsg");
        }

        // 支付密码正确则更新session信息，将支付密码是否正确的标识符放入到session中
        if (returnCode.equals("USR-1I00")) {
            request.getSession(true).removeAttribute(SessionValue.SESSION_MSGMOBILE);// 删除保存在session中的验证短信验证码通过的手机号码
            userBaseInfoDto.setTPassword(tpassword);
            request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfoDto);
        }
        returnJsonObject.put("returnCode", returnCode);
        returnJsonObject.put("returnMsg", returnMsg);

        logger.info("【UserController】modifyTPassWord()结束>>>returnJsonObject=" + returnJsonObject.toString());

        return returnJsonObject.toString();
    }

    /***
     * 获取手机验证码 不去到账户模块验证手机号码 信息鉴权/支付
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    @RequestMapping(value = "/setUp/getVerifyCodeByAuthAndPay.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String getVerifyCode(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String busiChannel = RequestHelper.verfiyIEChannel(request);

        JSONObject returnJsonObject = null;

        // 手机号码
        String mobile = request.getParameter("mobile");
        String bankNumber = request.getParameter("bankNumber");
        String bankName = request.getParameter("bankName");
        bankName = URLDecoder.decode(bankName, "UTF-8");
        String money = request.getParameter("money");// 鉴权时可以为空
        String msgType = request.getParameter("msgType");

        if (StringUtils.isEmptyString(mobile) || StringUtils.isEmptyString(bankNumber) || StringUtils.isEmptyString(bankName) || StringUtils.isEmptyString(msgType)) {
            returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
            returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            return returnJsonObject.toString();
        }

        String seqId = request.getSession().getId();
        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        returnJsonObject = userInfoexManager.getVerifyCode(context, mobile, msgType, bankNumber, money, bankName);

        String returnCode = (String) returnJsonObject.get("returnCode");

        // 将用户手机号码保存到session中
        if (returnCode != null && returnCode.equals("0000")) {
            request.getSession(true).setAttribute(SessionValue.SESSION_AUTHMOBILE, mobile);
        }

        return returnJsonObject.toString();
    }

    /***
     * 验证验证码是否正确 鉴权
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    @RequestMapping(value = "/setUp/authCheckVrfCode.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String authCheckVrfCode(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_AUTHMOBILE);
        String inputMobile = request.getParameter("mobile");

        String mobile = null;
        if (obj != null) {
            mobile = obj.toString();
            if (!mobile.equals(inputMobile)) {
                // 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致
                JSONObject returnJsonObject = new JSONObject();
                returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_ERRORMOBILECODE);
                returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_ERRORMOBILEMSG);
                return returnJsonObject.toString();
            }
        } else {
            // 用户没有获取验证码，非法请求验证验证码
            JSONObject returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_ILLEGALREQCODE);
            returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_ILLEGALREQMSG);
            return returnJsonObject.toString();
        }

        // 申请验证码 返回的sessionID
        String sessionID = request.getParameter("sessionID");

        // 输入的验证码
        String rvrfcode = request.getParameter("rvrfcode");

        String seqId = request.getSession().getId();
        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.MESSAGE_SERVICE_801, busiChannel, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        String returnStr = messageManager.checkVrfCode(context, sessionID, mobile, rvrfcode);

        JSONObject returnJsonObject = new JSONObject();

        returnJsonObject.put("returnCode", returnStr);

        if ("0000".equals(returnStr)) {
            request.getSession(true).setAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE, mobile);
        }

        return returnJsonObject.toString();
    }

    /**
     * 设置支付密码
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author liury
     */
    @RequestMapping(value = "/business/setTpassword.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "30")
    @ResponseBody
    public String setTpassword(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        JSONObject returnJsonObject = null;
        Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (obj == null) {
            // 用户没有登录
            returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
            returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
            return returnJsonObject.toString();
        }
        UserBaseInfoDto user = (UserBaseInfoDto) obj;
        if (!"30".equals(user.getUserType())) {
            // 用户还未鉴权
            returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_NOTAUTHCODE);
            returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_NOTAUTHMSG);
            return returnJsonObject.toString();
        }
        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String tpassword = request.getParameter("tPassword");// 支付密码

        MD5 md5 = new MD5();
        tpassword = md5.getMD5ofStr(tpassword);

        String oldTpassword = "";
        String manageType = "A";
        String tradeChannel = WXConstants.TRADE_CHANEL_03;
        String tradeMark = WXConstants.TRADE_CHANEL_03;

        String sessionId = request.getSession().getId();
        logger.info("设置支付密码>>>cmfUserId=" + cmfUserId + ",System.currentTimeMillis()=" + System.currentTimeMillis());
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

        returnJsonObject = userInfoexManager.manageTpassword(context, cmfUserId, tpassword, oldTpassword, manageType, tradeChannel, tradeMark);
        Object code = returnJsonObject.get("returnCode");
        // 设置支付密码成功则更新session中的信息
        if (code != null && ("USR-1I00").equals(code.toString())) {
            request.getSession(true).setAttribute(SessionValue.SESSION_ISSETTRADEPASSWORD, "1");// 支付密码是否设置标志位：
                                                                                                // 1：已设置或修改初始的支付密码/0：未设置或修改初始的支付密码
            userBaseInfoDto.setUserType("30");
            userBaseInfoDto.setTPassword(tpassword);
            request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfoDto);
            // 设置支付密码成功 且 用户类型 不为 10 则同步修改信息至 柜台
            try {
                userInfoexManager.realNameAfterUpdateInvprtpByEccType(cmfUserId);
            } catch (Exception e) {
                logger.error("----setTpassword-realNameAfterUpdateInvprtpByEccType-Exception：", e);
                e.printStackTrace();
            }
        }
        logger.info("设置支付密码>>>returnJsonObject=" + returnJsonObject.toString());

        // 将税收居民信息上传TA
        try {
            UserBaseInfoDto baseInfoDto = userServiceClient.queryUserAndAccoRlaById(context, cmfUserId).getUserBaseInfoDto();
            String taxResidentType = baseInfoDto.getTaxResidentType();
            if (!"1".equals(taxResidentType) && ("USR-1I00").equals(code)) {
                userServiceClient.deleteAppR1BlotterInfo(cmfUserId);
                List<UserTaxInfoDto> taxInfoDtos = userServiceClient.queryUserTaxInfoListByUserId(cmfUserId);
                if (null != taxInfoDtos && taxInfoDtos.size() > 0) {
                    for (int i = 0; i < taxInfoDtos.size(); i++) {
                        UserTaxInfoDto userTaxInfoDto = taxInfoDtos.get(i);
                        userServiceClient.saveAppR1BlotterInfo(cmfUserId, taxResidentType, userTaxInfoDto);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("----税收居民信息保存至非居民涉税信息申请流水业务表-Exception：", e);
            e.printStackTrace();
        }

        return returnJsonObject.toString();

    }

    /**
     * 重置支付密码
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author liury
     */
    @RequestMapping(value = "/business/resetTpassword.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "30")
    @ResponseBody
    public String resetTpassword(HttpServletResponse response, HttpServletRequest request, ResetTransPasswordModel resetTransPasswordModel) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        JSONObject returnJsonObject = null;
        Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        Object obj2 = request.getSession(true).getAttribute(SessionValue.SESSION_BANKAUTHSTATUS);
        if (obj == null) {
            // 用户没有登录
            returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
            returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
            return returnJsonObject.toString();
        }
        if (obj2 == null) {
            // 用户没有验证银行卡
            returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_ILLEGALREQCODE);
            returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_ILLEGALREQMSG);
            return returnJsonObject.toString();
        }

        UserBaseInfoDto user = (UserBaseInfoDto) obj;
        if (!"30".equals(user.getUserType())) {
            // 用户还未鉴权
            returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", WXConstants.COMMON_ERROR_NOTAUTHCODE);
            returnJsonObject.put("returnMsg", WXConstants.COMMON_ERROR_NOTAUTHMSG);
            return returnJsonObject.toString();
        }

        String cmfUserId = user.getCmfUserId();
        String tpassword = request.getParameter("tPassword");// 支付密码

        if (ValidationUtils.validate(resetTransPasswordModel) != null) {
            returnJsonObject = new JSONObject();
            returnJsonObject.put("returnCode", WXConstants.PARAMETER_CHECK_NOT_PASS);
            returnJsonObject.put("returnMsg", ValidationUtils.validate(resetTransPasswordModel));
            return returnJsonObject.toString();
        }

        MD5 md5 = new MD5();
        tpassword = md5.getMD5ofStr(tpassword);

        String oldTpassword = "";
        String manageType = "A";
        String tradeChannel = WXConstants.TRADE_CHANEL_03;
        String tradeMark = WXConstants.TRADE_CHANEL_03;

        String sessionId = request.getSession().getId();
        logger.info("重置支付密码>>>cmfUserId=" + cmfUserId + ",System.currentTimeMillis()=" + System.currentTimeMillis());
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), sessionId, "");

        returnJsonObject = userInfoexManager.manageTpassword(context, cmfUserId, tpassword, oldTpassword, manageType, tradeChannel, tradeMark);
        Object code = returnJsonObject.get("returnCode");
        // 设置支付密码成功则更新session中的信息
        if (code != null && ("USR-1I00").equals(code.toString())) {
            request.getSession(true).setAttribute(SessionValue.SESSION_ISSETTRADEPASSWORD, "1");// 支付密码是否设置标志位：
                                                                                                // 1：已设置或修改初始的支付密码/0：未设置或修改初始的支付密码
            user.setUserType("30");
            user.setTPassword(tpassword);
            request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, user);
            // 删除session中保存的已通过鉴权的状态
            request.getSession(true).removeAttribute(SessionValue.SESSION_BANKAUTHSTATUS);
        }
        logger.info("重置支付密码>>>returnJsonObject=" + returnJsonObject.toString());

        return returnJsonObject.toString();

    }

    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/business/queryIsNeedTest.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "10")
    public String queryIsNeedTest(HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        String RISKLEVEL = (String) request.getSession(true).getAttribute("RISKLEVEL");
        String RISKEVALDATE = (String) request.getSession(true).getAttribute("RISKEVALDATE");
        if (RISKLEVEL != null && RISKEVALDATE != null) {
            jsonObject.put("riskLevel", RISKLEVEL);
            jsonObject.put("riskEvalDate", RISKEVALDATE);
        } else {
            String cmdUserid = RequestHelper.getSessionCmfUserId(request);
            UserBaseInfoDto userBaseInfoDto = queryManager.queryUserInfoByCmfUserId(new Context(), cmdUserid).getUserBaseInfoDto();
            UserBaseInfoDto userRiskDateDto = serviceClient.queryUserRiskEvalDateByCmfUserId(cmdUserid).getUserBaseInfoDto();
            String riskLevel = "";
            String riskEvalDate = "";
            if (userBaseInfoDto != null) {
                riskLevel = userBaseInfoDto.getRiskLevel();// 风险等级
            }
            if (null != userRiskDateDto) {
                riskEvalDate = userRiskDateDto.getRiskEvalDate();// 测评时间
            }

            request.getSession(true).setAttribute("RISKLEVEL", riskLevel);
            request.getSession(true).setAttribute("RISKEVALDATE", riskEvalDate);
            jsonObject.put("riskLevel", riskLevel);
            jsonObject.put("riskEvalDate", riskEvalDate);
        }
        return jsonObject.toString();
    }

    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/business/queryUserinfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "10")
    public String queryUserinfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        logger.info("UserController类【queryUserinfo】开始>>>timestamp:" + System.currentTimeMillis());
		String cmfUserId = RequestHelper.getSessionCmfUserId(request);
		JSONObject jsonObject = new JSONObject();
		if(StringUtils.isNotBlank(cmfUserId)){
			//重新从数据库获取一遍
			UserServiceMessage userServiceMessage = userServiceClient.queryUserAndAccoRlaById(null, cmfUserId);
			UserBaseInfoDto userBaseInfoDto = userServiceMessage != null ? userServiceMessage.getUserBaseInfoDto() : null;
			UserAccoRlaDto userAccoRlaDto = userServiceMessage != null ? userServiceMessage.getUserAccoRlaDto() : null;
			CustInfoDto custInfoDto = userServiceMessage != null ? userServiceMessage.getCustInfoDto() : null;
			if (userBaseInfoDto != null) {
				String mobile = userBaseInfoDto.getMobile();
				mobile = StringUtils.isBlank(mobile) ? "" : mobile.substring(0, 3) + "****" + mobile.substring(7);
				String userName = StringUtils.isBlank(userBaseInfoDto.getCustName()) ? "" : userBaseInfoDto.getCustName();// 姓名
				String temp = "";
				for (int i = 0; userName.length() > 0 && i < userName.length() - 1; i++) {
					temp += "*";
				}
				userName = userName.length() > 0 ? userName.charAt(0) + temp : userName;
				String idNo = userBaseInfoDto.getIdNo();
				idNo = StringUtils.isBlank(idNo) ? "" : idNo.substring(0, 4) + "**********" + idNo.substring(idNo.length() - 4);
				jsonObject.put("cmfUserId", cmfUserId);
				jsonObject.put("mobile", mobile);//手机号
				jsonObject.put("userName", userName);//脱敏客户名
				jsonObject.put("idNo", idNo);//脱敏身份证
				jsonObject.put("idType", StringUtils.isBlank(userBaseInfoDto.getIdType()) ? "" : userBaseInfoDto.getIdType());//证件类型
				jsonObject.put("riskLevel", StringUtils.isBlank(userBaseInfoDto.getRiskLevel()) ? "" : userBaseInfoDto.getRiskLevel());//风险等级
				jsonObject.put("userType", StringUtils.isBlank(userBaseInfoDto.getUserType()) ? "" : userBaseInfoDto.getUserType());//用户等级
				jsonObject.put("idSetPassword", StringUtils.isBlank(userBaseInfoDto.getLPassword()) ? "N" : "Y");//是否设置密码
				jsonObject.put("nation", StringUtils.isBlank(userBaseInfoDto.getNation()) ? "" : userBaseInfoDto.getNation());//国籍代码
				jsonObject.put("nationNM", StringUtils.isBlank(userBaseInfoDto.getNationNM()) ? "" : userBaseInfoDto.getNationNM());//国籍
				jsonObject.put("province", StringUtils.isBlank(userBaseInfoDto.getProvince()) ? "" : userBaseInfoDto.getProvince());//省份代码
				jsonObject.put("provinceNM", StringUtils.isBlank(userBaseInfoDto.getProvinceNM()) ? "" : userBaseInfoDto.getProvinceNM());//省份
				jsonObject.put("city", StringUtils.isBlank(userBaseInfoDto.getCity()) ? "" : userBaseInfoDto.getCity());//城市代码
				jsonObject.put("cityNM", StringUtils.isBlank(userBaseInfoDto.getCityNM()) ? "" : userBaseInfoDto.getCityNM());//城市
				jsonObject.put("addr", StringUtils.isBlank(userBaseInfoDto.getAddr()) ? "" : userBaseInfoDto.getAddr());//详细地址
				jsonObject.put("invprtp", StringUtils.isBlank(userBaseInfoDto.getInvprtp()) ? "" : userBaseInfoDto.getInvprtp());//申请类型 0 转换专业用户 1 转换普通用户
				jsonObject.put("appst", userAccoRlaDto != null ? StringUtils.isNotBlank(userAccoRlaDto.getAppst()) ? userAccoRlaDto.getAppst() : "" : "");//申请状态 N 待处理  I 处理中 Y 申请成功 C 申请失败
				jsonObject.put("vocCode", StringUtils.isBlank(userBaseInfoDto.getVocCode()) ? "" : userBaseInfoDto.getVocCode());//职业代码
				jsonObject.put("vocCodeNM", StringUtils.isBlank(userBaseInfoDto.getVocCodeNM()) ? "" : userBaseInfoDto.getVocCodeNM());//职业
				jsonObject.put("birthDate", StringUtils.isBlank(userBaseInfoDto.getBirthDate()) ? "" : userBaseInfoDto.getBirthDate());//出生日期 yyyy-MM-dd
				jsonObject.put("birthDateNm", StringUtils.isBlank(userBaseInfoDto.getBirthDateNm()) ? "" : userBaseInfoDto.getBirthDateNm());//出生日期 yyyy年MM月dd日
				jsonObject.put("taxResidentType", StringUtils.isBlank(userBaseInfoDto.getTaxResidentType()) ? "" : userBaseInfoDto.getTaxResidentType());//税收居民类型
				jsonObject.put("taxResidentTypeNm", StringUtils.isBlank(userBaseInfoDto.getTaxResidentTypeNm()) ? "" : userBaseInfoDto.getTaxResidentTypeNm());//税收居民类型描述
				jsonObject.put("invprtpScore", userBaseInfoDto.getInvprtpScore() == null ? "0" : userBaseInfoDto.getInvprtpScore());//专业投资知识问卷分数
				jsonObject.put("syncInvprtpAlert", StringUtils.isBlank(userBaseInfoDto.getSyncInvprtpAlert()) ? "" : userBaseInfoDto.getSyncInvprtpAlert());//同步专业投资者提醒标志
				jsonObject.put("isControl", StringUtils.isBlank(userBaseInfoDto.getIsControl()) ? "" : userBaseInfoDto.getIsControl());//是否存在控制关系
				jsonObject.put("isNotBeneficiary", StringUtils.isBlank(userBaseInfoDto.getIsNotBeneficiary()) ? "" : userBaseInfoDto.getIsNotBeneficiary());//是否不是实际受益人
				jsonObject.put("isBadHonesty", StringUtils.isBlank(userBaseInfoDto.getIsBadHonesty()) ? "" : userBaseInfoDto.getIsBadHonesty());//是否有不良诚信
				jsonObject.put("specialRiskLevel", StringUtils.isBlank(userBaseInfoDto.getSpecialRiskLevel()) ? "" : userBaseInfoDto.getSpecialRiskLevel());//特殊用户风险等级
				jsonObject.put("isSetTradePassword", RequestHelper.checkHasSetTPsw(request));//是否设置支付密码
				jsonObject.put("custName", StringUtils.isBlank(userBaseInfoDto.getCustName()) ? "" : userBaseInfoDto.getCustName());//客户名
				jsonObject.put("otherVocation", StringUtils.isBlank(userBaseInfoDto.getOtherVocation()) ? "" : userBaseInfoDto.getOtherVocation());//其他职业
				jsonObject.put("idExpireDate", custInfoDto != null ? StringUtils.isNotBlank(custInfoDto.getIdExpireDate()) ? custInfoDto.getIdExpireDate() : "" : "");//证件过期日
				jsonObject.put("returnCode", WXConstants.COMMON_SUCCESS);
				jsonObject.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
			} else {
				jsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
				jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
			}
		}else {
			jsonObject.put("returnCode", WXConstants.COMMON_ERROR_LOGINTIMEOUTCODE);
			jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_LOGINTIMEOUTMSG);
		}
		logger.info("UserController类【queryUserinfo】结束>>>timestamp:" + System.currentTimeMillis());
		return jsonObject.toString();
    }

    /**
     * 退出登录
     */
    @RequestMapping(value = "/business/exit.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    @TracingInfo(authority = "10")
    public String exit(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String ieType = request.getHeader("user-agent").toLowerCase();

        String url = "/WeixinService/index.shtml";

        if (ieType.indexOf("micromessenger") < 0) {// 其他浏览器
            logger.info(">>其他浏览器点击退出，跳转到其他浏览器登录页面");

            request.getSession(true).invalidate();
            url = "/WeixinService/otherIELogin/otherIELogin.shtml";

        } else {// 微信浏览器
            logger.info(">>微信浏览器点击退出，跳转到其他浏览器登录页面");
            Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
            String openId = "";
            if (obj != null) {
                openId = obj.toString();
                userInfoexManager.updatecmfUserid(openId, "", "C");// C:解除绑定
                                                                   // R：正常绑定
            }
            request.getSession(true).invalidate();
            url = "/WeixinService/weixinLogin/login.shtml";
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("url", url);

        return jsonObject.toString();
    }

    /**
     * 验证支付密码 修改手机号码界面 将验证支付密码标志保存在session中
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     *             String
     * @author maj
     */
    @RequestMapping(value = "/business/checkTPswByModifyMobile.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String checkTPswByModifyMobile(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String seqId = request.getSession().getId();

        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
        }

        String tpassword = request.getParameter("tPassWord");// 支付密码

        MD5 md5 = new MD5();
        tpassword = md5.getMD5ofStr(tpassword);

        String manageType = "V";
        String tradeChannel = WXConstants.TRADE_CHANEL_03;
        String tradeMark = WXConstants.TRADE_CHANEL_03;

        logger.info("【UserController】checkTPswByModifyMobile()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>System.currentTimeMillis()="
                + System.currentTimeMillis());
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        String returnCode = "";
        String returnMsg = "";

        JSONObject returnJsonObject = new JSONObject();

        returnJsonObject = userInfoexManager.manageTpassword(context, cmfUserId, tpassword, "", manageType, tradeChannel, tradeMark);
        if (returnJsonObject != null) {
            returnCode = returnJsonObject.getString("returnCode");
            returnMsg = returnJsonObject.getString("returnMsg");
        }

        // 支付密码正确则更新session信息，将支付密码是否正确的标识符放入到session中
        if (returnCode.equals("USR-1I00")) {
            request.getSession(true).setAttribute(SessionValue.SESSION_HASCHECKEDTPSW, "1");
        }
        returnJsonObject.put("returnCode", returnCode);
        returnJsonObject.put("returnMsg", returnMsg);

        logger.info("【UserController】checkTPswByModifyMobile()结束>>>returnJsonObject=" + returnJsonObject.toString());

        return returnJsonObject.toString();
    }

    /**
     * 判断用户是否设置支付密码
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     *             String
     * @author maj
     */
    @RequestMapping(value = "/business/checkHasSetTpassword.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @TracingInfo(authority = "10")
    @ResponseBody
    public String checkHasSetTpassword(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String seqId = request.getSession().getId();

        String cmfUserId = "";
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        String userType = "";
        if (userBaseInfoDto != null) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
            userType = userBaseInfoDto.getUserType();
        }
        String returnCode = "0000";
        String returnMsg = "已设置支付密码。";
        if (userType != null && !userType.equals("30")) {
            returnCode = "9001";
            returnMsg = "用户未鉴权";
        }

        logger.info("【UserController】checkHasSetTpassword()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>System.currentTimeMillis()=" + System.currentTimeMillis());
        // 日志封装类
        Context context = ContextUtils.setContext(WXConstants.TRADE_SERVICE_601, busiChannel, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        JSONObject returnJsonObject = new JSONObject();

        String hasSetTPsw = RequestHelper.checkHasSetTPsw(request);
        if (!hasSetTPsw.equals("Y")) {
            returnCode = "9000";
            returnMsg = "未修改支付密码";
        }
        returnJsonObject.put("returnCode", returnCode);
        returnJsonObject.put("returnMsg", returnMsg);

        logger.info("【UserController】checkHasSetTpassword()结束>>>returnJsonObject=" + returnJsonObject.toString());

        return returnJsonObject.toString();
    }

    /**
     * 检测用户登录状态 是否登录
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     *             String
     * @author maj
     */
    @RequestMapping(value = "/setUp/checkUserIsLogin.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String checkUserIsLogin(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String busiChannel = RequestHelper.verfiyIEChannel(request);
        String seqId = request.getSession().getId();

        Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        String returnCode = "0000";
        String returnMsg = "已登录。";
        if (obj == null) {
            returnCode = "9000";
            returnMsg = "未登录";
        }

        logger.info("【UserController】checkUserIsLogin()开始>>>seqId=" + seqId + ">>>busiChannel+" + busiChannel + ">>>System.currentTimeMillis()=" + System.currentTimeMillis());

        JSONObject returnJsonObject = new JSONObject();

        returnJsonObject.put("returnCode", returnCode);
        returnJsonObject.put("returnMsg", returnMsg);

        logger.info("【UserController】checkUserIsLogin()结束>>>returnJsonObject=" + returnJsonObject.toString());

        return returnJsonObject.toString();
    }

    @RequestMapping(value = "/business/updateCmfUserBaseInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String updateCmfUserBaseInfo(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        UserServiceMessage userServiceMessage = new UserServiceMessage();
        UserBaseInfoDto userBaseInfoDto = (UserBaseInfoDto) request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, WXConstants.SERVICE_CHANNEL_WEIXIN, cmfUserId, System.currentTimeMillis(),
                System.currentTimeMillis(), seqId, "");
        String custNo = "";
        String nation = request.getParameter("nation");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        if (("").equals(nation) || nation == null) {
            nation = userBaseInfoDto.getNation();
            province = userBaseInfoDto.getProvince();
            city = userBaseInfoDto.getCity();
        }
        String addr = request.getParameter("addr");
        String voccode = request.getParameter("voccode");
        String dateOfBirth = request.getParameter("birthDate");
        String taxResidentType = request.getParameter("taxResidentType");
        String taxResidentData = request.getParameter("taxResidentData");
        String otherVocation = request.getParameter("otherVocation");
        if (dateOfBirth != null) {
            dateOfBirth = dateOfBirth.replaceAll("-", "");
        }

        // 参数 全为空
        if ((StringUtils.isEmptyString(nation) && StringUtils.isEmptyString(province) && StringUtils.isEmptyString(city) && StringUtils.isEmptyString(addr)
                && StringUtils.isEmptyString(voccode) && StringUtils.isEmptyString(dateOfBirth) && StringUtils.isEmptyString(taxResidentType)
                && StringUtils.isEmptyString(taxResidentData)) || (WXConstants.VOCCODE_15.equals(voccode) && StringUtils.isBlank(otherVocation))) {
            logger.info("---updateCmfUserBaseInfo-传入的参数不合法！-cmfUserId:" + cmfUserId);
            userServiceMessage.setReturnCode(WXConstants.RETURN_CODE_9008);
            userServiceMessage.setReturnMsg(WXConstants.RETURN_MSG_9008);
            JSONObject jsonObject = JSONObject.fromObject(userServiceMessage);
            return jsonObject.toString();
        }

        Hashtable<String, Object> ParameterData = ParameterCache.getData();
        if (null == ParameterData) {
            ParameterData = new Hashtable<String, Object>();
        }
        boolean flag = true;
        // 校验国家 是否 有效
        if (!StringUtils.isEmptyString(nation)) {
            String nationKey = "DS#DS_NATION#" + nation;
            if (!ParameterData.containsKey(nationKey)) {
                flag = false;
            }
        }

        // 校验省份 是否 有效
        if (!StringUtils.isEmptyString(province)) {
            String provinceKey = "SYSTEM#DS_PROVINCE#" + province;
            if (!ParameterData.containsKey(provinceKey)) {
                flag = false;
            }
        }

        // 校验城市 是否 有效
        if (!StringUtils.isEmptyString(city)) {
            String cityKey = "SYSTEM#DS_CITYCODE#" + city;
            if (!ParameterData.containsKey(cityKey)) {
                flag = false;
            }
        }

        // 校验职业 是否 有效
        if (!StringUtils.isEmptyString(voccode)) {
            String voccodeKey = "SYSTEM#VOCCODE#" + voccode;
            if (!ParameterData.containsKey(voccodeKey)) {
                flag = false;
            }
        }

        // 校验税收居民类型 是否 有效
        if (!StringUtils.isEmptyString(taxResidentType)) {
            String taxTypeKey = "SYSTEM#INVESTTAXTYPE#" + taxResidentType;
            if (!ParameterData.containsKey(taxTypeKey)) {
                flag = false;
            }
        }

        // 校验 日期 能够被 转换 则为有效日期
        if (!StringUtils.isEmptyString(dateOfBirth)) {
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            sf.setLenient(false);// 设置不宽松校验日期
            try {
                sf.parse(dateOfBirth);
            } catch (ParseException e1) {
                flag = false;
            } catch (Exception e) {
                flag = false;
            }
        }

        // 参数校验 提示
        if (!flag) {
            logger.info("---updateCmfUserBaseInfo-传入的参数无效！-cmfUserId:" + cmfUserId);
            userServiceMessage.setReturnCode(WXConstants.RETURN_CODE_9008);
            userServiceMessage.setReturnMsg(WXConstants.RETURN_MSG_9008);
            JSONObject jsonObject = JSONObject.fromObject(userServiceMessage);
            return jsonObject.toString();
        }

        userServiceMessage = userInfoexManager.updateCmfUserBaseInfo(context, cmfUserId, nation, province, city, addr, voccode, dateOfBirth, taxResidentType, otherVocation);
        if (WXConstants.COMMON_SUCCESS.equals(userServiceMessage.getReturnCode())) {
            // 风险测评完成后 保存 用户 税收居民 信息
            // type空值 且 data 不为空
            // type为其他类型 data为空
            if ((!StringUtils.isEmptyString(taxResidentData) && StringUtils.isEmptyString(taxResidentType))
                    || (StringUtils.isEmptyString(taxResidentData) && (!StringUtils.isEmptyString(taxResidentType) && !"1".equals(taxResidentType)))) {
                logger.info("---updateCmfUserBaseInfo-传入的参数不合法！-cmfUserId:" + cmfUserId);
                userServiceMessage.setReturnCode(WXConstants.RETURN_CODE_9008);
                userServiceMessage.setReturnMsg(WXConstants.RETURN_MSG_9008);
                JSONObject jsonObject = JSONObject.fromObject(userServiceMessage);
                return jsonObject.toString();
            }

            // 这里需要 data 不为空才进行保存操作
            if (!StringUtils.isEmptyString(taxResidentData)) {
                // data不为空 校验是否json格式
                try {
                    JSONArray.fromObject(taxResidentData);
                } catch (Exception e) {
                    e.printStackTrace();
                    logger.error("----updateCmfUserBaseInfo-JSONObject.fromObject(taxResidentData)-Exception：", e);
                    logger.info("---updateCmfUserBaseInfo-传入的参数不合法！-cmfUserId:" + cmfUserId);
                    userServiceMessage.setReturnCode(WXConstants.RETURN_CODE_9008);
                    userServiceMessage.setReturnMsg(WXConstants.RETURN_MSG_9008);
                    JSONObject jsonObject = JSONObject.fromObject(userServiceMessage);
                    return jsonObject.toString();
                }

                try {
                    UserAccoRlaDto userAccoRla = serviceClient.queryUserAndAccoRlaById(context, cmfUserId).getUserAccoRlaDto();
                    if (userAccoRla != null) {
                        custNo = userAccoRla.getEcCustNo();
                    }
                    serviceClient.saveTaxInfoByUserRiskLevel(custNo, cmfUserId, taxResidentType, taxResidentData);
                } catch (Exception e) {
                    logger.error("----saveTaxInfoByUserRiskLevel-Exception:", e);
                    e.printStackTrace();
                }
            }
        }

        JSONObject jsonObject = JSONObject.fromObject(userServiceMessage);
        return jsonObject.toString();
    }

    /**
     * 更新专业投资者消息标志 返回json数组
     */
    @RequestMapping(value = "/business/updateUserInvprtpAlert.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String updateUserInvprtpAlert(HttpServletResponse response, HttpServletRequest request) {
        UserServiceMessage userServiceMessage = new UserServiceMessage();
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        userServiceMessage = serviceClient.updateUserInvprtpAlert(cmfUserId);
        return userServiceMessage.getReturnCode();
    }

    /**
     * 查询用户税收居民信息 返回json数组
     */
    @RequestMapping(value = "/business/queryUserTaxInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryUserTaxInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        JSONArray jsonArray = new JSONArray();
        try {
            List<UserTaxInfoDto> userTaxList = new ArrayList<UserTaxInfoDto>();
            userTaxList = serviceClient.queryUserTaxInfoListByUserId(cmfUserId);
            jsonArray = JSONArray.fromObject(userTaxList);
        } catch (Exception e) {
            logger.error("----queryUserTaxInfo-queryUserTaxInfoListByUserId-Exception:", e);
            e.printStackTrace();
        }
        return jsonArray.toString();
    }

    /**
     * 查询用户风险评测历史记录 返回json数组
     */
    @RequestMapping(value = "/business/queryUserRiskHistory.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryUserRiskHistory(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String userRiskInfo = userInfoexManager.queryUserRiskHistory(request);
        return userRiskInfo;
    }

    /**
     * 埋点
     * 
     * @param request
     * @throws Exception
     */
    @RequestMapping(value = "/buriedData.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public void insertBuriedData(HttpServletRequest request) {
        try {
            String cmfUserId = RequestHelper.getSessionCmfUserId(request);
            logger.info("埋点获取到当前用户Id==" + cmfUserId);
            String sessionId = request.getSession().getId();
            logger.info("埋点获取到当前sessionId===" + sessionId);
            String url = request.getServletPath();
            logger.info("埋点获取到的当前的url===" + url);
            String eventId = request.getParameter("eventId");
            String groupId = request.getParameter("groupId");
            String pageId = request.getParameter("pageId");
            String pageSource = request.getParameter("pageSource");
            Object openId = request.getSession().getAttribute(SessionValue.SESSION_OPENID);
            BuriedDataDto buried = new BuriedDataDto();
            buried.setBuriedUserId(cmfUserId);
            buried.setBuriedEventId(eventId);
            if (null != openId) {
                buried.setBuriedOpenId(String.valueOf(openId));
            }
            buried.setBuriedSourceChannel("WX");
            buried.setBuriedGroupId(groupId);
            buried.setBuriedPageSource(pageSource);
            buried.setBuriedPageId(pageId);
            serviceClient.insertBuriedData(buried);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("WXUserController:insertBuriedData方法异常" + e);
        }
    }

    @RequestMapping(value = "/business/insertAppointInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String insertAppointInfo(AppointInfoDto dto, HttpServletRequest request) {
        JSONObject rst = new JSONObject();

        if (null == dto) {
            rst.put("msg", "参数为空");
            rst.put("success", false);
            logger.info("insertAppointInfo param dto  is null");
        } else {
            String cmfUserId = RequestHelper.getSessionCmfUserId(request);
            String openId = (String) request.getSession().getAttribute(SessionValue.SESSION_OPENID);
            dto.setOpenId(openId);
            dto.setCmfUserId(cmfUserId);
            dto.setChannelSource("WX");
            logger.info("insertAppointInfo param dto is " + dto.toString());

            rst = serviceClient.insertAppointInfo(dto);
        }

        return rst.toString();
    }

    /**
     * 查询用户的专属顾问
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/queryUserCustService.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String queryUserCustService(HttpServletRequest request) {
        String openid = (String) request.getSession().getAttribute(SessionValue.SESSION_OPENID);
        String userId = request.getParameter("userId");
        if (StringUtils.isEmptyString(openid)) {
            String retStr = setOpenIdInSession(userId, request);
            if ("-1".equals(retStr)) {
                JSONObject resp = new JSONObject();
                resp.put("returnCode", "1");
                resp.put("returnMsg", "userId解密失败");
                return resp.toString();
            }
        }
        openid = (String) request.getSession().getAttribute(SessionValue.SESSION_OPENID);
        return queryManager.queryUserCustService(openid).toString();
    }

    /**
     * 提供于h5登陆之后绑定专属顾问
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/bindUserCustService.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public void bindUserCustService(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        String cmfuserid = request.getParameter("cmfuserid");
        String openid = (String) request.getSession().getAttribute(SessionValue.SESSION_OPENID);
        if (org.apache.commons.lang.StringUtils.isBlank(cmfuserid) || org.apache.commons.lang.StringUtils.isBlank(openid)) {
            result.put("returnCode", "9999");
            result.put("returnMsg", "关键参数为空");
        }
        userInfoexManager.syncCustserviceInfo(openid, cmfuserid);
    }

    /**
     * 设置openId到session内
     * 
     * @param userId
     * @param request
     */
    private String setOpenIdInSession(String userId, HttpServletRequest request) {
        try {
            userId = URLDecoder.decode(userId, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("decoder userid捕获异常,", e);
        }
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("encrypted", userId));
        String entity = HttpPostUtil.sendPost("decrypt", param, "");
        if (StringUtils.isEmptyString(entity)) {
            logger.info("调用远程api解密userId失败");
            return "-1";
        }
        JSONObject fromObject = JSONObject.fromObject(entity);
        boolean flag = (Boolean) fromObject.get("success");
        if (flag) {
            String decryptUserId = String.valueOf(fromObject.get("resp"));
            InvectorUserInfoexDto resultDto = activityManager.queryUserInfoByUerId(decryptUserId);
            if (resultDto != null) {
                request.getSession(true).setAttribute(SessionValue.SESSION_OPENID, resultDto.getOpenId());
            } else {
                logger.info("通过解密userId查询用户信息失败");
                return "-1";
            }
        } else {
            logger.info("调用远程api解密userId返回状态为success：{}" + fromObject.get("success"));
            return "-1";
        }
        return "0";
    }

    @RequestMapping(value = "/business/insertUserOperateLog.xhtml", method = { RequestMethod.POST })
    public void insertUserOperateLog(HttpServletRequest request, @RequestBody(required = false) UserOperateLogDto userOperateLogDto) {
        String cmfuserid = (String) request.getSession().getAttribute(SessionValue.SESSION_CMFUSERID);
        logger.info("UserController--insertUserOperateLog 入参UserOperateLogDto：" + userOperateLogDto + " cmfuserid:" + cmfuserid);
        if (StringHelper.isBlank(cmfuserid) || userOperateLogDto == null || StringHelper.isBlank(userOperateLogDto.getLoginChannel())
                || StringHelper.isBlank(userOperateLogDto.getType())) {
            logger.info("关键参数缺失");
            return;
        }
        userOperateLogDto.setCmfuserid(cmfuserid);
        userServiceClient.insertUserOperateLog(userOperateLogDto);
    }
    
    @RequestMapping(value = "/log/insertUserOperateLog.xhtml", method = { RequestMethod.POST })
    public void insertUserOperateLogForH5(HttpServletRequest request, @RequestBody(required = false) UserOperateLogDto userOperateLogDto) {
        logger.info("UserController--insertUserOperateLogForH5 入参UserOperateLogDto：" + userOperateLogDto);
        if (StringHelper.isBlank(userOperateLogDto.getCmfuserid())) {
            String cmfuserid = (String) request.getSession().getAttribute(SessionValue.SESSION_CMFUSERID);
            userOperateLogDto.setCmfuserid(cmfuserid);
        }
        if (userOperateLogDto == null || StringHelper.isBlank(userOperateLogDto.getLoginChannel()) || StringHelper.isBlank(userOperateLogDto.getType())
                || StringHelper.isBlank(userOperateLogDto.getCmfuserid())) {
            logger.info("关键参数缺失");
            return;
        }
        userServiceClient.insertUserOperateLog(userOperateLogDto);
    }
	
    @RequestMapping(value = "/business/updateIdExpireDateByCustNo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String updateIdExpireDateByCustNo(HttpServletResponse response, HttpServletRequest request) {
    	logger.info("UserController--updateIdExpireDateByCustNo--更新客户证件过期日");
    	JSONObject resultJson = new JSONObject();
    	String custno = RequestHelper.getSessionCustNo(request);
    	if(StringUtils.isBlank(custno)) {
    		logger.info("未在session中获取到客户号 custno=" + custno);
    		resultJson.put("resultCode", "9999");
    		resultJson.put("resultMsg", "登陆超时");
    		return resultJson.toString();
    	}
    	String idExpireDate = request.getParameter("idExpireDate");
    	if(StringUtils.isBlank(idExpireDate)) {
    		logger.info("无效参数 idExpireDate=" + idExpireDate);
    		resultJson.put("resultCode", "9999");
    		resultJson.put("resultMsg", "非法参数");
    		return resultJson.toString();
    	}
    	UserServiceMessage userServiceMessage = userInfoexManager.updateIdExpireDateByCustNo(custno, idExpireDate.replaceAll("-", "").trim());
    	resultJson.put("resultCode", userServiceMessage.getResultCode());
		resultJson.put("resultMsg", userServiceMessage.getResultMsg());
		return resultJson.toString();
    }
    
    /**
     * 退出接口
     * <b>该接口只提供给API系统使用，只针对session进行清空并做跳转操作</b>
     * @param request
     * @param response
     * @param targetUrl
     */
    @RequestMapping(value = "/logoutAndRedirect.xhtml",produces = "text/html;charset=UTF-8", method = { RequestMethod.POST,RequestMethod.GET})
    public void logoutAndRedirect(HttpServletRequest request,HttpServletResponse response,@RequestParam("targetUrl") String targetUrl) {
    		Enumeration<String> headerNames = request.getHeaderNames();
    		while(headerNames.hasMoreElements()) {
    			String nextElement = headerNames.nextElement();
    			logger.info("request header:" + nextElement + ":" + request.getHeader(request.getHeader(nextElement)));
    		}
	    	HttpSession session = request.getSession(true);
	    	String id = session.getId();
	    	logger.info("logoutAndRedirect -> jsessionid" + id +",targetUrl:"+ targetUrl);
	    	session.invalidate();
			try {
				response.sendRedirect(targetUrl);
			} catch (IOException e) {
				logger.error("发起重定向时捕获异常：",e);
			}
    }
}
