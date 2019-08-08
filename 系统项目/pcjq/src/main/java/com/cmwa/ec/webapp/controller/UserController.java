package com.cmwa.ec.webapp.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.dto.MsgParameterDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.system.ParameterDto;
import com.cmwa.ec.query.facade.dto.trade.AppointRequestDto;
import com.cmwa.ec.user.facade.dto.AppointInfoDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserOperateLogDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.user.facade.dto.UserTaxInfoDto;
import com.cmwa.ec.webapp.client.QueryServiceClient;
import com.cmwa.ec.webapp.client.UserServiceClient;
import com.cmwa.ec.webapp.dto.WxTrackAddReqModel;
import com.cmwa.ec.webapp.manager.MessageManager;
import com.cmwa.ec.webapp.manager.QueryManager;
import com.cmwa.ec.webapp.manager.UserManager;
import com.cmwa.ec.webapp.util.ContextUtils;
import com.cmwa.ec.webapp.util.DateUtils;
import com.cmwa.ec.webapp.util.ECConstants;
import com.cmwa.ec.webapp.util.HttpPostUtil;
import com.cmwa.ec.webapp.util.MD5;
import com.cmwa.ec.webapp.util.RequestHelper;
import com.cmwa.ec.webapp.util.SessionValue;
import com.cmwa.ec.webapp.util.StringHelper;
import com.cmwa.ec.webapp.util.StringUtils;
import com.cmwa.ec.webapp.util.cache.ParameterCache;

@Controller("UserController")
@RequestMapping(value = "/AppService")
public class UserController<V> {

    private static Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserManager userManager;
    
    @Autowired
    private MessageManager messageManager;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private QueryServiceClient queryServiceClient;

    /***
     * 查询登录用户信息
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    @RequestMapping(value = "/business/queryUserAndAccoRlaById.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryUserAndAccoRlaById(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");

        logger.info("UserController类【queryUserAndAccoRlaById】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());

        String returnCode = "";
        String returnMsg = "";

        UserServiceMessage userMessage = userManager.queryUserAndAccoRlaById(context, cmfUserId);
        UserBaseInfoDto userInfo = null;
        if (userMessage != null) {
            userInfo = userMessage.getUserBaseInfoDto();
            if (userInfo == null) {
                userInfo = new UserBaseInfoDto();
            }
            returnCode = userMessage.getReturnCode();
            returnMsg = userMessage.getReturnMsg();

            if (userInfo.getLPassword() != null && !userInfo.getLPassword().equals("")) {
                // 保存在session中的是否已设置登录密码的标志 1：已设置 0：未设置
                request.getSession(true).setAttribute("isSetUserLpassword", "1");
            }

            // 将用户不需要传递到页面的数据置空
            userInfo.setCmfUserId("");
            userInfo.setEmail("");
            userInfo.setLPassword("");
            userInfo.setTPassword("");
            userInfo.setqPasswd_Postfix("");
            userInfo.setLLastChannel("");
            userInfo.setLLastMark("");
            userInfo.setTLastChannel("");
            userInfo.setTLastMark("");
            userInfo.setLStatus("");
            userInfo.setTStatus("");
            userInfo.setCustStatus("");
            userInfo.setMemo("");
            userInfo.setSpareString1("");
            userInfo.setSpareString2("");
            userInfo.setSpareString3("");

        }
        // 保存在session中的是否已设置支付密码的标志 1：已设置 0：未设置
        String isSetTradePassword = (String) request.getSession(true).getAttribute("isSetTradePassword");
        String isSetUserLpassword = (String) request.getSession(true).getAttribute("isSetUserLpassword");
        JSONObject returnJsonObject = new JSONObject();

        returnJsonObject.put("userInfo", userInfo);
        returnJsonObject.put("returnCode", returnCode);
        returnJsonObject.put("returnMsg", returnMsg);
        returnJsonObject.put("isSetTradePassword", isSetTradePassword);
        returnJsonObject.put("isSetUserLpassword", isSetUserLpassword);

        logger.info("UserController类【queryUserAndAccoRlaById】结束>>>timestamp:" + System.currentTimeMillis());
        return returnJsonObject.toString();

    }

    /**
     * 用户登录
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/setUp/ajaxlogin.xhtml", method = { RequestMethod.POST })
    public String ajaxlogin(HttpServletResponse response, HttpServletRequest request) throws Exception {

        // 手机号码/身份证号
        String loginNumber = request.getParameter("mobile");
        // 密码
        String password = request.getParameter("password");

        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);

        MD5 md5 = new MD5();
        if (loginNumber != null && !loginNumber.equals("")) {
            loginNumber = loginNumber.trim();
        }
        if (password != null && !password.equals("")) {
            password = md5.getMD5ofStr(password.trim());
        }

        logger.info("UserController类【ajaxlogin】开始>>>mobile:" + loginNumber + ">>>seqId:" + seqId + ">>>timestamp:" + System.currentTimeMillis());

        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");

        JSONObject returnJsonObject = new JSONObject();
        // 返回的信息
        String returnCode = "";
        String returnMsg = "";

        Long timeStampTemp = (Long) request.getSession(true).getAttribute(SessionValue.SESSION_TIMESTAMP);
        String timeStamp = "";
        if (timeStampTemp != null && !timeStampTemp.equals("")) {
            timeStamp = Long.toString(timeStampTemp);
        }
        if (timeStamp == null || timeStamp.equals("") || System.currentTimeMillis() / 1000 > Long.parseLong(timeStamp)) {
            returnCode = ECConstants.LOGIN_RETURNCODE;
            returnMsg = ECConstants.LOGIN_RETURNMSG;
            // response.getOutputStream().print(StringUtils.toJson(returnCode,returnMsg));

            returnJsonObject.put("errorCode", returnCode);
            returnJsonObject.put("errorMsg", returnMsg);
            return returnJsonObject.toString();
        }

        String loginType = ECConstants.LOGIN_TYPE_X;
        // 长度为15位或者18位 是身份证号码登录
        if (loginNumber.length() == 15 || loginNumber.length() == 18) {
            loginType = "0";
        }

        UserServiceMessage userServiceMsg = userManager.login(context, loginNumber, password, loginType, ECConstants.LOGIN_CHANEL_01, ECConstants.LOGIN_NMARK_01);

        String userSex = "1";
        if (userServiceMsg != null) {
            logger.info("UserController类【ajaxlogin】>>>userServiceMsg.getErrCode()=" + userServiceMsg.getErrCode() + ">>>userServiceMsg.getErrMsg()=" + userServiceMsg.getErrMsg());
            returnCode = userServiceMsg.getReturnCode();
            returnMsg = userServiceMsg.getReturnMsg();
            if (returnCode != null && returnCode.equals("0000")) {

                String requestUrl = (String) request.getSession(true).getAttribute(SessionValue.SESSION_REQUESTURL);
                request.getSession(true).removeAttribute(SessionValue.SESSION_REQUESTURL);
                logger.info("【【【【【【【【登录接口调用成功并登录成功，获取到session中url的值并返回到页面去】】】】】】】】");
                returnJsonObject.put("requestUrl", requestUrl);

                String custName = "";
                String idNo = "";
                String temp = "";
                if (userServiceMsg.getUserBaseInfoDto() != null) {
                    custName = userServiceMsg.getUserBaseInfoDto().getCustName();
                }
                if (custName == null || custName.equals("")) {
                    custName = "尊敬的用户";
                } else {
                    idNo = userServiceMsg.getUserBaseInfoDto().getIdNo();
                    logger.info("AccountController类【ajaxlogin】>>>idNo=" + idNo);
                    try {
                        Integer i = (int) idNo.charAt(16);
                        if (i % 2 == 0) {
                            temp = "女士";
                            userSex = "0";
                        } else {
                            temp = "先生";
                            userSex = "1";
                        }
                        custName = userServiceMsg.getUserBaseInfoDto().getCustName() + temp;
                    } catch (Exception e) {
                        logger.info("UserController类【ajaxlogin】>>>获取到的idNo非法");
                    }
                }
                logger.info("UserController类【ajaxlogin】>>>custName=" + custName);

                request.getSession(true).setAttribute("userSex", userSex);
                request.getSession(true).setAttribute("userinfo", "您好！" + custName);
                request.getSession(true).setAttribute("userinfoDto", userServiceMsg.getUserBaseInfoDto());
                request.getSession(true).setAttribute(SessionValue.SESSION_CMFUSERID, userServiceMsg.getUserBaseInfoDto().getCmfUserId());
                request.getSession(true).setAttribute("cmfUserName", userServiceMsg.getUserBaseInfoDto().getCustName());
                request.getSession(true).setAttribute("lasttime", DateUtils.formatDate(userServiceMsg.getUserBaseInfoDto().getLLastTime()));
                request.getSession(true).setAttribute("userAccoRla", userServiceMsg.getUserAccoRlaDto());
                logger.info("UserController类【ajaxlogin】>>>登录成功，存放对应的值到session中。");
                if (!userServiceMsg.getUserBaseInfoDto().getLPassword().equals(userServiceMsg.getUserBaseInfoDto().getTPassword())) {
                    request.getSession(true).setAttribute("isSetTradePassword", "1");// 支付密码是否设置标志位：
                                                                                     // 1：已设置或修改初始的支付密码/0：未设置或修改初始的支付密码
                } else {
                    request.getSession(true).setAttribute("isSetTradePassword", "0");// 支付密码是否设置标志位：
                                                                                     // 1：已设置或修改初始的支付密码/0：未设置或修改初始的支付密码
                }
            }
        } else {
            logger.info("UserController类【ajaxlogin】>>>userServiceMsg == null");
        }
        logger.info("UserController类【ajaxlogin】结束>>>returnJsonObject:" + returnJsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
        returnJsonObject.put("errorCode", returnCode);
        returnJsonObject.put("errorMsg", returnMsg);
        return returnJsonObject.toString();

    }

    /**
     * 退出登录
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/setUp/checkOutLogin.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String checkOutLogin(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");

        logger.info("UserController类【checkOutLogin】开始>>>cmfUserId:" + cmfUserId + ">>>timestamp:" + System.currentTimeMillis());

        String returnCode = "";
        String returnMsg = "";

        if (cmfUserId != null && !cmfUserId.equals("")) {
            try {
                request.getSession().invalidate();
                returnCode = "0000";
                returnMsg = "退出登录成功";
                logger.info("UserController类【checkOutLogin】>>>删除session，退出登录");
            } catch (Exception e) {
                returnCode = "9999";
                returnMsg = "退出登录失败";
            }
        } else {
            returnCode = "1111";
            returnMsg = "未登录，不需要退出";
        }

        logger.info("UserController类【checkOutLogin】结束>>>returnCode:" + returnCode + ">>>timestamp:" + System.currentTimeMillis());
        return StringUtils.toJson(returnCode, returnMsg);

    }

    /**
     * 公共页面获取session中的值，展示在页面
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/setUp/getSessionByPage.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String getSessionByPage(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String userinfo = RequestHelper.getSessionAttr(request, "userinfo");
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");

        logger.info("UserController类【getSessionByPage】开始>>>cmfUserId:" + cmfUserId + ">>>userinfo:" + userinfo + ">>>timestamp:" + System.currentTimeMillis());

        JSONObject returnJsonObject = new JSONObject();
        String returnCode = "0000";
        String returnMsg = "成功";

        if (!cmfUserId.equals("") && !userinfo.equals("")) {
            returnJsonObject.put("cmfUserId", cmfUserId);
            returnJsonObject.put("userinfo", userinfo);
        } else {
            returnJsonObject.put("cmfUserId", "");
            returnJsonObject.put("userinfo", "");
        }

        returnJsonObject.put("returnCode", returnCode);
        returnJsonObject.put("returnMsg", returnMsg);

        logger.info("UserController类【getSessionByPage】结束>>>returnJsonObject:" + returnJsonObject.toString() + ">>>timestamp:" + System.currentTimeMillis());
        return returnJsonObject.toString();

    }

    /*
     * ==================================以上是1.0版本代码，以后删掉==========================
     * =================
     */
    // TODO 以上是1.0版本代码，以后删掉
    /***
     * 验证手机号码是否已经使用 注册/重置密码界面
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @author luos
     */
    @RequestMapping(value = "/setUp/getMobile.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String getMobile(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        // 手机号码
        String mobile = request.getParameter("mobile");
        if (mobile != null && !mobile.equals("")) {
            mobile = mobile.trim();
        }
        String cmfId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");
        JSONObject returnJson = userManager.verifyMobile(context, mobile);
        String returnStr = returnJson.toString() == null ? "" : returnJson.toString();
        return returnStr;
    }

    /***
     * 验证手机号码和返回手机验证码（新） 注册 手机号码从session中取得
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @author luos
     */
    @RequestMapping(value = "/setUp/getMobileVerifyCode.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String getMobileVerifyCode(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        JSONObject returnJsonObject = new JSONObject();
        String cmfId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, cmfId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");
        returnJsonObject = userManager.getMobileVerifyCode(context, request);
        return returnJsonObject.toString();

    }

    /**
     * 用户注册 PC
     * 
     * @param response
     * @param request
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             void
     * @author luos
     */
    @RequestMapping(value = "/setUp/userRegister.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String userRegister(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId,
                "");
        JSONObject returnJsonObject = new JSONObject();
        returnJsonObject = userManager.registerNormalUserWithT(request, context);
        String returnCode = returnJsonObject.getString("returnCode");
        if ("0000".equals(returnCode)) {// 删除session中保存的发送过手机短信验证码的手机号码
            request.getSession(true).removeAttribute(ECConstants.SESSION_USERMOBILE);
        }
        return returnJsonObject.toString();
    }

    /***
     * 验证手机号码和图片验证码 找回登录密码页面
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @author maj
     */
    @RequestMapping(value = "/setUp/checkVrfCodeAndMobile.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String checkVrfCodeAndMobile(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String mobile = request.getParameter("mobile");
        String rvrfcode = request.getParameter("rvrfcode");
        JSONObject jsonObject = new JSONObject();
        UserBaseInfoDto userBaseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
        String seqId = RequestHelper.getSeqId(request);
        String cmfUserId = "";
        String userMobile = "";

        // 登录之后 找回登录密码 手机号码 只能是 登录人的手机号
        // 防止前台页面修改 手机号码 修改其他人密码
        // 且 登录之后的找回密码页面 手机号 脱敏后也不能直接使用
        if (null != userBaseInfoDto) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
            userMobile = userBaseInfoDto.getMobile();
            mobile = userBaseInfoDto.getMobile();
        }

        if (null != userBaseInfoDto && !userMobile.equals(mobile)) {
            jsonObject.put("returnCode", "9003");
            jsonObject.put("returnMsg", "当前手机号码不是该用户的手机号码");
            return jsonObject.toString();
        }

        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");
        jsonObject = userManager.checkVrfCodeAndMobile(context, request, mobile, rvrfcode);
        return jsonObject.toString();

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
            returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_9000);
            returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_9000);
            return returnJsonObject.toString();
        }
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, "", System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");
        returnJsonObject = userManager.getVerifyCode(context, mobile, msgType, bankNumber, money, bankName);
        String returnCode = returnJsonObject.getString("returnCode");
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
        String mobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_AUTHMOBILE);
        String inputMobile = request.getParameter("mobile");
        // 申请验证码 返回的sessionID
        String sessionID = request.getParameter("sessionID");
        // 输入的验证码
        String rvrfcode = request.getParameter("rvrfcode");
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, "", System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");
        JSONObject returnJson = messageManager.checkVrfCodeResgist(context, sessionID, mobile, rvrfcode, inputMobile);
        if ("0000".equals(returnJson.getString("returnCode"))) {
            request.getSession(true).setAttribute(SessionValue.SESSION_AUTHVERIFYMOBILE, mobile);
        }
        return returnJson.toString();
    }

    /**
     * 获取短信验证码 找回登录密码界面
     * 
     * @param response
     * @param request
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             void
     * @author maj
     */

    @RequestMapping(value = "/setUp/getMsgByPsw.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String getMsgByPsw(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        JSONObject jsonObject = new JSONObject();
        String seqId = RequestHelper.getSeqId(request);
        UserBaseInfoDto userBaseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
        String cmfUserId = "";
        String mobile = request.getParameter("mobile");
        String userMobile = "";

        // 登录之后 找回登录密码 手机号码 只能是 登录人的手机号
        // 防止前台页面修改 手机号码 修改其他人密码
        // 且 登录之后的找回密码页面 手机号 脱敏后也不能直接使用
        if (null != userBaseInfoDto) {
            cmfUserId = userBaseInfoDto.getCmfUserId();
            userMobile = userBaseInfoDto.getMobile();
            mobile = userBaseInfoDto.getMobile();
        }

        if (null != userBaseInfoDto && !userMobile.equals(mobile)) {
            jsonObject.put("returnCode", "9003");
            jsonObject.put("returnMsg", "当前手机号码不是该用户的手机号码");
            return jsonObject.toString();
        }

        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(),
                System.currentTimeMillis(), seqId, "");
        jsonObject = userManager.getMsgByPsw(context, request);

        return jsonObject.toString();

    }

    /**
     * 验证短信验证码--找回登录密码
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/setUp/checkVrfCode.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String checkVrfCode(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String mobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_USERMOBILE);
        String inputMobile = request.getParameter("mobile");
        // 申请验证码 返回的sessionID
        String sessionID = request.getParameter("sessionID");
        // 输入的验证码
        String rvrfcode = request.getParameter("rvrfcode");
        String seqId = RequestHelper.getSeqId(request);

        UserBaseInfoDto userBaseInfoDto = RequestHelper.getSessionUserBaseInfo(request);

        // 登录用户不为空 只能是 登录人的手机号码
        if (null != userBaseInfoDto && !StringUtils.isEmptyString(userBaseInfoDto.getCmfUserId())) {
            inputMobile = userBaseInfoDto.getMobile();
        }

        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, "", System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");
        JSONObject jsonObject = messageManager.checkVrfCodeResgist(context, sessionID, mobile, rvrfcode, inputMobile);
        if (ECConstants.RETURN_CODE_0000.equals(jsonObject.getString("returnCode"))) {
            request.getSession(true).setAttribute(SessionValue.SESSION_VERIFYMOBILE, mobile);
            request.getSession(true).removeAttribute(SessionValue.SESSION_RESETLPWVERFLAG);
            request.getSession(true).removeAttribute(SessionValue.SESSION_RESETLPWMSGTIME);
        }
        return jsonObject.toString();
    }

    /**
     * 重置登录密码
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author maj
     */
    @RequestMapping(value = "/setUp/resetLPws.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String resetLPws(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);

        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");

        JSONObject jsonObject = userManager.resetUserPassword(context, request);
        return jsonObject.toString();

    }

    /**
     * 设置支付密码
     * 
     * @param response
     * @param request
     * @throws Exception
     *             void
     * @author luos
     */
    @RequestMapping(value = "/business/setTpassword.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String setTpassword(HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject returnJsonObject = null;
        String sessionId = RequestHelper.getSeqId(request);
        UserBaseInfoDto userBaseInfoDto = RequestHelper.getSessionUserBaseInfo(request);
        String tpassword = request.getParameter("tPassword");// 支付密码
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        // 日志封装类
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(),
                sessionId, "");
        returnJsonObject = userManager.manageTpasswordResgist(context, request);
        Object code = returnJsonObject.get("returnCode");
        // 设置支付密码成功则更新session中的信息
        if (code != null && ("USR-1I00").equals(code.toString())) {
            userBaseInfoDto.setTPassword(tpassword);
            request.getSession(true).setAttribute(SessionValue.SESSION_USERBASEINFO, userBaseInfoDto);
            // 设置支付密码成功 且 用户类型 不为 10 则同步修改信息至 柜台
            try {
                if ("10" != userBaseInfoDto.getUserType()) {
                    userManager.realNameAfterUpdateInvprtpByEccType(cmfUserId);
                } else {
                    logger.error("----setTpassword-realNameAfterUpdateInvprtpByEccType-userType error :" + userBaseInfoDto.getUserType());
                }
            } catch (Exception e) {
                logger.error("----setTpassword-realNameAfterUpdateInvprtpByEccType-Exception：", e);
                e.printStackTrace();
            }
        }

        // 将税收居民信息上传TA
        try {
            UserBaseInfoDto baseInfoDto = userServiceClient.queryUserAndAccoRlaById(context, cmfUserId).getUserBaseInfoDto();
            String taxResidentType = baseInfoDto.getTaxResidentType();
            if (!taxResidentType.equals("1") && code.equals("USR-1I00")) {
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
     * 用户登录 个人
     * 
     * @param response
     * @param request
     * @return
     * @throws Exception
     *             String
     * @author maj
     */
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/setUp/login.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String login(HttpServletResponse response, HttpServletRequest request) throws Exception {

        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);

        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");

        JSONObject jsonObject = userManager.login(context, request);

        // 登录成功之后将活动关键字查询信息放入session
        if (jsonObject.get("returnCode").equals("0000")) {
            QueryMessageDto queryParamList = queryManager.queryHomeAddressIsWordWithLinkage(new Context(), "SYSTEM", "QUARTERMSGKEY", null, "");
            List<ParameterDto> listPara = (List<ParameterDto>) queryParamList.getData();
            if (null != listPara && listPara.size() > 0) {
                String userId = RequestHelper.getSessionCmfUserId(request);
                JSONObject msgJsonObject = queryManager.queryUserMsgLikeTitle(context, userId, listPara.get(0).getPmco());
                String isShowTips = (String) msgJsonObject.get("isShowTips");
                Cookie cookie = new Cookie("isShowTips", isShowTips);
                cookie.setPath("/");
                response.addCookie(cookie);
            }
        }
        return jsonObject.toString();
    }

    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/business/queryUserinfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryUserinfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject = userManager.queryUserinfo(request);
        return jsonObject.toString();
    }

    /**
     * 产品超市-风险测评
     */
    @RequestMapping(value = "/business/queryIsNeedTest.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryIsNeedTest(HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject = userManager.queryIsNeedTest(request);
        return jsonObject.toString();
    }

    /**
     * 查询公告头部所需信息
     */
    @RequestMapping(value = "/setUp/headerInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String headerInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject = userManager.headerInfo(request);
        return jsonObject.toString();
    }

    /**
     * 获取短信验证码 找回登录密码界面
     * 
     * @param response
     * @param request
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             void
     * @author maj
     */

    @RequestMapping(value = "/setUp/sendMsg.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String sendMsg(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);

        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(),
                System.currentTimeMillis(), seqId, "");

        JSONObject jsonObject = userManager.sendMsg(context, request);

        return jsonObject.toString();

    }

    /**
     * 验证短信验证码 不需要其他要素，只在session中获取手机号码(SessionValue.SESSION_USERMOBILE)
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/setUp/checkMobileVrfCode.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String checkMobileVrfCode(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String mobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_USERMOBILE);
        // 申请验证码 返回的sessionID
        String sessionID = request.getParameter("sessionID");
        // 输入的验证码
        String rvrfcode = request.getParameter("rvrfcode");
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, "", System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");
        JSONObject jsonObject = messageManager.checkMobileVrfCode(context, sessionID, mobile, rvrfcode);
        if (ECConstants.RETURN_CODE_0000.equals(jsonObject.getString("returnCode"))) {
            request.getSession(true).setAttribute(SessionValue.SESSION_VERIFYMOBILE, mobile);
        }
        return jsonObject.toString();
    }

    /**
     * 修改支付密码
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/business/modifyTPassword.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String modifyTPassword(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {

        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");
        JSONObject jsonObject = userManager.userManager(context, request);
        return jsonObject.toString();
    }

    /**
     * 获取菜单详情
     * 
     * @param response
     * @param request
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @author luos
     */
    @RequestMapping(value = "/setUp/getMenu.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String getMenu(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        JSONObject returnJson = userManager.getMenu(request);
        return returnJson.toString();
    }

    /**
     * 登录后设置支付密码 30用户没设置支付密码去设置支付密码
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/business/setTPassword.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String setTPassword(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");
        JSONObject jsonObject = userManager.setTpassword(context, request);

        return jsonObject.toString();
    }

    /**
     * 验证支付密码 并将已验证手机号码保存到session中 manageType M-修改；R-重置；A-新增；V-验证
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/business/checkTPassword.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String checkTPassword(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.USER_SERVICE_001, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");

        JSONObject jsonObject = userManager.checkTPassword(context, request);

        return jsonObject.toString();
    }

    /***
     * 验证手机号码和返回手机短信验证码 需判断是否验证支付密码 修改用户注册手机号码页面
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     * @author maj
     */
    @RequestMapping(value = "/business/getVrfCode.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String getVrfCode(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);

        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(),
                System.currentTimeMillis(), seqId, "");

        JSONObject jsonObject = userManager.getVrfCode(context, request);

        return jsonObject.toString();

    }

    /**
     * 验证短信验证码--修改手机号码 需验证：获取手机短信验证码时保存在session中的手机号码SESSION_USERMOBILE；
     * 是否需要设置登录密码SESSION_HASCHECKSETLPASSWORD；
     * 需删除：获取手机短信验证码时保存在session中的手机号码SESSION_USERMOBILE
     * ；是否需要设置登录密码SESSION_HASCHECKSETLPASSWORD；是否验证支付密码SESSION_CHECKEDTPASSWORD；
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/business/checkVrfCodeByModifyMobile.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String checkVrfCodeByModifyMobile(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);

        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(),
                System.currentTimeMillis(), seqId, "");

        JSONObject jsonObject = messageManager.checkVrfCodeByModifyMobile(context, request);
        return jsonObject.toString();
    }

    /**
     * 修改手机号码并设置登录密码
     * 
     * @param response
     * @param request
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     *             String
     * @author maj
     */
    @RequestMapping(value = "/business/modifyRegMobileAndSetPsw.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String modifyRegMobileAndSetPsw(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);

        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(),
                System.currentTimeMillis(), seqId, "");

        JSONObject jsonObject = userManager.modifyRegMobileAndSetPsw(context, request);
        return jsonObject.toString();
    }

    /**
     * 查询用户信息，原始信息，没有加密
     */
    @RequestMapping(value = "/business/queryOriginalUserinfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryOriginalUserinfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        jsonObject = userManager.queryOriginalUserinfo(request);
        return jsonObject.toString();
    }

    /**
     * 查询用户信息
     */
    @RequestMapping(value = "/setUp/queryUserinfoCheckLogin.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryUserinfoCheckLogin(HttpServletResponse response, HttpServletRequest request) throws Exception {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = userManager.queryUserinfo(request);
        } catch (Exception e) {
            logger.error("查询用户信息 Exception：" + e.getMessage());
            e.printStackTrace();
        }
        return jsonObject.toString();
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
    @ResponseBody
    public String sendSmsMsg(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        JSONObject returnJsonObject = new JSONObject();
        // 手机号码
        String mobile = request.getParameter("mobile");
        UserBaseInfoDto userBaseInfo = RequestHelper.getSessionUserBaseInfo(request);
        if (userBaseInfo == null) {
            returnJsonObject.put("returnCode", ECConstants.RETURN_CODE_8000);
            returnJsonObject.put("returnMsg", ECConstants.RETURN_MSG_8000);
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
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, "", System.currentTimeMillis(), System.currentTimeMillis(),
                seqId, "");
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

    @RequestMapping(value = "/business/updateCmfUserBaseInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String updateCmfUserBaseInfo(HttpServletResponse response, HttpServletRequest request) throws UnsupportedEncodingException, IOException {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        String seqId = RequestHelper.getSeqId(request);
        // 日志信息
        Context context = ContextUtils.setContext(ECConstants.MESSAGE_SERVICE_801, ECConstants.SERVICE_CHANNEL_APP, cmfUserId, System.currentTimeMillis(),
                System.currentTimeMillis(), seqId, "");
        UserServiceMessage userServiceMessage = new UserServiceMessage();
        JSONObject jsonObject = new JSONObject();
        String nation = request.getParameter("nation");
        String province = request.getParameter("province");
        String city = request.getParameter("city");
        String addr = request.getParameter("addr");
        String voccode = request.getParameter("voccode");
        String dateOfBirth = request.getParameter("birthDate");
        String taxResidentType = request.getParameter("taxResidentType");
        String taxResidentData = request.getParameter("taxResidentData"); // 税收居民数据
        String otherVocation = request.getParameter("otherVocation");//客户输入其他职业
        if (dateOfBirth != null) {
            dateOfBirth = dateOfBirth.replaceAll("-", "");
        }
        // 参数 全为空
        if ((StringUtils.isEmptyString(nation) && StringUtils.isEmptyString(province) && StringUtils.isEmptyString(city) && StringUtils.isEmptyString(addr)
                && StringUtils.isEmptyString(voccode) && StringUtils.isEmptyString(dateOfBirth) && StringUtils.isEmptyString(taxResidentType)
                && StringUtils.isEmptyString(taxResidentData)) || (ECConstants.VOCCODE_15.equals(voccode) && StringHelper.isBlank(otherVocation))) {
            logger.info("---updateCmfUserBaseInfo-传入的参数不合法！-cmfUserId:" + cmfUserId);
            userServiceMessage.setReturnCode(ECConstants.RETURN_CODE_9008);
            userServiceMessage.setReturnMsg(ECConstants.RETURN_MSG_9008);
            jsonObject = JSONObject.fromObject(userServiceMessage);
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
            userServiceMessage.setReturnCode(ECConstants.RETURN_CODE_9008);
            userServiceMessage.setReturnMsg(ECConstants.RETURN_MSG_9008);
            jsonObject = JSONObject.fromObject(userServiceMessage);
            return jsonObject.toString();
        }

        userServiceMessage = userManager.updateCmfUserBaseInfo(context, cmfUserId, nation, province, city, addr, voccode, dateOfBirth, taxResidentType, otherVocation);
        jsonObject = JSONObject.fromObject(userServiceMessage);

        try {
            if (!StringUtils.isEmptyString(taxResidentData)) {
                taxResidentData = URLDecoder.decode(taxResidentData, "utf-8");
            }
        } catch (UnsupportedEncodingException e) {
            logger.info("TradeController类【setUserRiskLevel】获取taxResidentData异常>>>", e);
            e.printStackTrace();
        }

        // 税收居民数据
        if (ECConstants.COMMON_SUCCESS.equals(jsonObject.get("errCode"))) {
            // type空值 且 data 不为空
            // type为其他类型 data为空
            if ((!StringUtils.isEmptyString(taxResidentData) && StringUtils.isEmptyString(taxResidentType))
                    || (StringUtils.isEmptyString(taxResidentData) && (!StringUtils.isEmptyString(taxResidentType) && !"1".equals(taxResidentType)))) {
                logger.info("---updateCmfUserBaseInfo-传入的参数不合法！-cmfUserId:" + cmfUserId);
                userServiceMessage.setReturnCode(ECConstants.RETURN_CODE_9008);
                userServiceMessage.setReturnMsg(ECConstants.RETURN_MSG_9008);
                jsonObject = JSONObject.fromObject(userServiceMessage);
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
                    userServiceMessage.setReturnCode(ECConstants.RETURN_CODE_9008);
                    userServiceMessage.setReturnMsg(ECConstants.RETURN_MSG_9008);
                    jsonObject = JSONObject.fromObject(userServiceMessage);
                    return jsonObject.toString();
                }

                userManager.saveTaxInfoByUserInvtp("", cmfUserId, taxResidentType, taxResidentData);
            }
        }
        return jsonObject.toString();
    }

    /**
     * 查询用户税收居民信息 返回json数组
     */
    @RequestMapping(value = "/business/queryUserTaxInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryUserTaxInfo(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String userTaxInfo = userManager.queryUserTaxInfoListByUserId(request);
        return userTaxInfo;
    }

    /**
     * 查询用户税收居民信息 返回json数组
     */
    @RequestMapping(value = "/business/updateUserInvprtpAlert.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String updateUserInvprtpAlert(HttpServletResponse response, HttpServletRequest request) {
        UserServiceMessage userServiceMessage = new UserServiceMessage();
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        userServiceMessage = userManager.updateUserInvprtpAlert(cmfUserId);
        return userServiceMessage.getReturnCode();
    }

    /**
     * 查询用户风险评测历史记录 返回json数组
     */
    @RequestMapping(value = "/business/queryUserRiskHistory.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String queryUserRiskHistory(HttpServletResponse response, HttpServletRequest request) throws Exception {
        String userRiskInfo = userManager.queryUserRiskHistory(request);
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
    public void insertBuriedData(HttpServletRequest request) throws Exception {
        String cmfUserId = RequestHelper.getSessionCmfUserId(request);
        logger.info("埋点获取到当前用户Id==" + cmfUserId);
        String sessionId = request.getSession().getId();
        logger.info("埋点获取到当前sessionId===" + sessionId);
        String url = request.getServletPath();
        logger.info("埋点获取到的当前的url===" + url);
        String eventId = request.getParameter("eventId");
        String pageId = request.getParameter("pageId");
        String pageSource = request.getParameter("pageSource");
        // 调用api的addTrack方法埋点cmwa_wx_track
        String date = DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss");
        WxTrackAddReqModel wxTrackAddReqModel = new WxTrackAddReqModel(null, null, date, 0L, null, pageId, eventId, null, pageSource, cmfUserId);
        String wxTrack = JSONObject.fromObject(wxTrackAddReqModel).toString();
        String entity = HttpPostUtil.sendPostJsonFormat("wxtrack", "application/json", wxTrack);
        if (!StringUtils.isEmptyString(entity)) {
            JSONObject result = JSONObject.fromObject(entity);
            String errorMsg = String.valueOf(result.get("errorMsg"));
            if (!StringUtils.isEmptyString(errorMsg)) {
                logger.warn("调用API埋点失败,errorMsg为： " + errorMsg);
            } else {
                String uuid = String.valueOf(result.get("response"));
                logger.info("埋点记录uuid为: " + uuid);
            }
        } else {
            logger.warn("调用远程埋点api接口返回为空");
        }
    }

    /**
     * 保存用户预约咨询记录
     */
    @RequestMapping(value = "/business/insertAppointInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String insertAppointInfo(HttpServletRequest request) {
        String cfmUserId = RequestHelper.getSessionCmfUserId(request);
        logger.info("insertAppointInfo方法获取到当前用户Id==" + cfmUserId);
        String sessionId = request.getSession().getId();
        logger.info("insertAppointInfo方法获取到当前SessionId==" + sessionId);
        String url = request.getServletPath();
        logger.info("insertAppointInfo方法获取到当前url==" + url);
        String userName = request.getParameter("userName");
        String mobilePhone = request.getParameter("mobilePhone");
        String productType = request.getParameter("productType");
        AppointInfoDto dto = new AppointInfoDto();
        dto.setChannelSource("PC");
        dto.setCmfUserId(cfmUserId);
        dto.setMobilePhone(mobilePhone);
        dto.setUserName(userName);
        dto.setProductType(productType);
        return userServiceClient.insertAppointInfo(dto).toString();
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
    
    @RequestMapping(value = "/business/updateIdExpireDateByCustNo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST })
    @ResponseBody
    public String updateIdExpireDateByCustNo(HttpServletResponse response, HttpServletRequest request) {
    	logger.info("UserController--updateIdExpireDateByCustNo--更新客户证件过期日");
    	JSONObject resultJson = new JSONObject();
    	String custno = RequestHelper.getSessionCustNo(request);
    	if(StringHelper.isBlank(custno)) {
    		logger.info("未在session中获取到客户号 custno=" + custno);
    		resultJson.put("resultCode", "9999");
    		resultJson.put("resultMsg", "登陆超时");
    		return resultJson.toString();
    	}
    	String idExpireDate = request.getParameter("idExpireDate");
    	if(StringHelper.isBlank(idExpireDate)) {
    		logger.info("无效参数 idExpireDate=" + idExpireDate);
    		resultJson.put("resultCode", "9999");
    		resultJson.put("resultMsg", "非法参数");
    		return resultJson.toString();
    	}
    	UserServiceMessage userServiceMessage = userManager.updateIdExpireDateByCustNo(custno, idExpireDate.replaceAll("-", "").trim());
    	resultJson.put("resultCode", userServiceMessage.getResultCode());
		resultJson.put("resultMsg", userServiceMessage.getResultMsg());
		return resultJson.toString();
    }
}