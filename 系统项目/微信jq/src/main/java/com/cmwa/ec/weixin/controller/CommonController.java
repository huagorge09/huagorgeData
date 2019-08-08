package com.cmwa.ec.weixin.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dao.ActivityDao;
import com.cmwa.ec.weixin.dao.activity.ActParameterDao;
import com.cmwa.ec.weixin.dto.CustomerServiceDto;
import com.cmwa.ec.weixin.dto.InvectorUserInfoexDto;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.manager.activity.CustomerAwardManager;
import com.cmwa.ec.weixin.manager.business.ActivityManager;
import com.cmwa.ec.weixin.manager.business.MessageManager;
import com.cmwa.ec.weixin.util.ContextUtils;
import com.cmwa.ec.weixin.util.HttpPostUtil;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.SpringContextUtil;
import com.cmwa.ec.weixin.util.WeixinUtil;

@Controller
@RequestMapping(value = "/WeixinService")
public class CommonController {

    @Autowired
    private CustomerAwardManager customerAwardManager;

    @Autowired
    private ActParameterDao actParameterDao;

    @Autowired
    private ActivityManager activityManager;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private MessageManager messageManager;

    private final static Logger logger = LoggerFactory.getLogger(CommonController.class);

    /**
     * 查询字典表信息
     */
    @RequestMapping(value = "/fartherDayActivity/queryParameterInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String queryParameterInfo(HttpServletResponse response, HttpServletRequest request) {
        logger.info("查询字典表信息>>>>>queryParameterInfo>>>start>>>");
        String pmst = request.getParameter("pmst");
        String pmky = request.getParameter("pmky");
        String pmco = request.getParameter("pmco");
        if (StringUtil.isEmpty(pmst) || StringUtil.isEmpty(pmky)) {
            JSONObject obj = new JSONObject();
            obj.put("returnCode", WXConstants.COMMON_ERROR_PARAMISNULLCODE);
            obj.put("returnMsg", WXConstants.COMMON_ERROR_PARAMISNULLMSG);
            return obj.toString();
        }
        JSONObject jsonObject = new JSONObject();
        ParameterDto dto = new ParameterDto();
        dto.setPmst(pmst);
        dto.setPmky(pmky);
        dto.setPmco(pmco);
        try {
            List<ParameterDto> list = actParameterDao.getParameter(dto);
            logger.info(">>>>>>>>>查询字典表信息queryParameterInfo>>>出参: list：" + JSONArray.fromObject(list).toString());
            jsonObject.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
            jsonObject.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
            jsonObject.put("data", list);
        } catch (Exception e) {
            logger.error("查询字典表信息>>>>queryParameterInfo>>>>>发生异常,异常信息:", e);
            jsonObject.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            jsonObject.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        logger.info("调用判断活动状态接口>>>queryParameterInfo>>>end>>>>出参：" + jsonObject.toString());
        return jsonObject.toString();
    }

    /**
     * 父亲节活动-碎片列表接口
     */
    @RequestMapping(value = "/fartherDayActivity/shardList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String shardList(HttpServletResponse response, HttpServletRequest request) {
        logger.info("调用父亲节碎片列表接口>>>>>shardList>>>start>>>");
        JSONObject json = new JSONObject();
        String userId = request.getParameter("userId");
        String openId = String.valueOf(request.getSession(true).getAttribute(SessionValue.SESSION_OPENID));
        if (StringUtils.isBlank(openId) || "null".equals(openId)) {
            setOpenIdInSession(userId, request);
        }
        openId = String.valueOf(request.getSession(true).getAttribute(SessionValue.SESSION_OPENID));
        json.put("activityId", request.getParameter("activityId"));
        json.put("openid", openId);
        JSONObject jsonObject = customerAwardManager.shardListLogic(json.toString());
        logger.info("调用父亲节碎片列表接口>>>>>shardList>>>end>>>出参：" + jsonObject.toString());
        return jsonObject.toString();
    }

    /**
     * 父亲节活动-抽奖接口
     */
    @RequestMapping(value = "/fartherDayActivity/luckDraw.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String luckDraw(HttpServletResponse response, HttpServletRequest request) {
        logger.info("调用父亲节抽奖接口>>>>>luckDraw>>>start>>>");
        JSONObject json = new JSONObject();
        JSONObject jsonObject = new JSONObject();
        String userId = request.getParameter("userId");
        String openId = String.valueOf(request.getSession(true).getAttribute(SessionValue.SESSION_OPENID));
        if (StringUtils.isBlank(openId) || "null".equals(openId)) {
            setOpenIdInSession(userId, request);
        }
        openId = String.valueOf(request.getSession(true).getAttribute(SessionValue.SESSION_OPENID));
        json.put("activityId", request.getParameter("activityId"));
        json.put("boxState", request.getParameter("boxState"));
        json.put("openid", openId);
        jsonObject = customerAwardManager.luckDrawLogic(json.toString());
        logger.info("调用父亲节抽奖接口>>>>>luckDraw>>>end>>>出参：" + jsonObject.toString());
        return jsonObject.toString();
    }

    /**
     * 父亲节活动-获取好友助力消息列表接口
     */
    @RequestMapping(value = "/fartherDayActivity/obtainMessageList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String obtainMessageList(HttpServletResponse response, HttpServletRequest request) {
        logger.info("调用获取好友助力消息列表接口>>>>>obtainMessageList>>>start>>>");
        JSONObject json = new JSONObject();
        String userId = request.getParameter("userId");
        String openId = String.valueOf(request.getSession(true).getAttribute(SessionValue.SESSION_OPENID));
        if (StringUtils.isBlank(openId) || "null".equals(openId)) {
            setOpenIdInSession(userId, request);
        }
        openId = String.valueOf(request.getSession(true).getAttribute(SessionValue.SESSION_OPENID));
        json.put("activityId", request.getParameter("activityId"));
        json.put("pageIndex", request.getParameter("pageIndex"));
        json.put("type", request.getParameter("type"));
        json.put("openid", openId);
        JSONObject jsonObject = customerAwardManager.getSystemMessList(json.toString());
        logger.info("调用获取好友助力消息列表接口>>>>>obtainMessageList>>>end>>>出参：" + jsonObject.toString());
        return jsonObject.toString();
    }

    /**
     * 父亲节活动 检查用户是否可以签到
     * 
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fatherDayActivity/checkUserCanSignIn.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    public String checkUserCanSignIn(HttpServletRequest request) {
        String userId = request.getParameter("userId");

        if (StringUtil.isEmpty((String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID))) {
            setOpenIdInSession(userId, request);
        }
        String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        if (StringUtils.isBlank(openId)) {
            logger.error("调用远程api失败");
            JSONObject obj = new JSONObject();
            obj.put("returnCode", "1");
            obj.put("returnMsg", "系统发生内部错误");
            return obj.toString();
        }

        return customerAwardManager.checkUserCanSignIn(openId).toString();
    }

    /**
     * 父亲节活动 用户签到
     * 
     * @param userId
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fatherDayActivity/userSignIn.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    public String userSignIn(HttpServletRequest request) {

        String activityId = request.getParameter("activityId");
        String userId = request.getParameter("userId");

        if (StringUtil.isEmpty((String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID))) {
            setOpenIdInSession(userId, request);
        }

        String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        if (StringUtils.isBlank(openId)) {
            logger.error("查询用户openId失败，{}", userId);
            JSONObject obj = new JSONObject();
            obj.put("returnCode", "1");
            obj.put("returnMsg", "系统发生内部错误");
            return obj.toString();
        }

        String toUserId = request.getParameter("toUserId");

        if (StringUtils.isBlank(userId) || StringUtils.isBlank(activityId)) {
            JSONObject obj = new JSONObject();
            obj.put("returnCode", "1");
            obj.put("returnMsg", "关键参数缺失");
            return obj.toString();
        }

        String toOpenId = "";// 获取被分享用户openId
        if (!StringUtils.isBlank(toUserId) && !"null".equals(toUserId)) {
            try {
                toUserId = URLDecoder.decode(toUserId, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error("decode解码异常", e);
            }
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("encrypted", toUserId));
            String entity = HttpPostUtil.sendPost("decrypt", param, "");
            if (StringUtils.isBlank(entity)) {
                logger.info("调用远程api解密toUserId失败");
                JSONObject obj = new JSONObject();
                obj.put("returnCode", "1");
                obj.put("returnMsg", "系统发生内部错误");
                return obj.toString();
            }
            JSONObject fromObject = JSONObject.fromObject(entity);
            boolean flag = (Boolean) fromObject.get("success");
            if (flag) {
                String decryptUserId = String.valueOf(fromObject.get("resp"));
                InvectorUserInfoexDto resultDto = activityManager.queryUserInfoByUerId(decryptUserId);
                toOpenId = resultDto == null ? "" : resultDto.getOpenId();
                if (StringUtils.isBlank(toOpenId)) {
                    logger.error("查询用户toOpenId失败，{}", toUserId);
                    JSONObject obj = new JSONObject();
                    obj.put("returnCode", "1");
                    obj.put("returnMsg", "系统发生内部错误");
                    return obj.toString();
                }
            } else {
                logger.info("调用远程api解密toUserId失败");
                JSONObject obj = new JSONObject();
                obj.put("returnCode", "1");
                obj.put("returnMsg", "系统发生内部错误");
                return obj.toString();
            }
        }

        return customerAwardManager.userSignIn(openId, activityId, toOpenId).toString();
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
        if (StringUtils.isBlank(entity)) {
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
            logger.info("调用远程api解密userId返回状态为success：{}", fromObject.get("success"));
            return "-1";
        }
        return "0";
    }

    /**
     * 查询用户中奖记录
     * 
     * @param userId
     * @param activityId
     * @param awardName
     * @param awardType
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryUserAward.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    public String queryUserAward(HttpServletRequest request, @RequestParam(value = "userId", required = true) String userId,
            @RequestParam(value = "activityId", required = true) String activityId, String awardName, String awardType) {
        String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        if (openId == null) {
            String executeStatus = setOpenIdInSession(userId, request);
            if ("-1".equals(executeStatus)) {
                JSONObject returnObj = new JSONObject();
                returnObj.put("returnCode", "-1");
                return returnObj.toString();
            }
            openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        }
        String queryUserAward = customerAwardManager.queryUserAward(openId, activityId, awardName, awardType);
        logger.info("查询用户中奖记录返回信息为：" + queryUserAward);
        return queryUserAward;
    }

    /**
     * 
     * 用户兑奖接口
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/saveUserAwardInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    public String saveUserAwardInfo(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        if (openId == null) {
            setOpenIdInSession(userId, request);
        }
        openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String userFullName = request.getParameter("userFullName");
        String cusAwardId = request.getParameter("cusAwardId");
        String mobile = request.getParameter("mobile");
        String address = request.getParameter("address");
        String activityId = request.getParameter("activityId");
        JSONObject param = new JSONObject();
        param.put("openid", openId);
        param.put("userFullName", userFullName);
        param.put("cusAwardId", cusAwardId);
        param.put("mobile", mobile);
        param.put("address", address);
        param.put("activityId", activityId);
        JSONObject saveUserAwardMsg = customerAwardManager.saveUserAwardInfo(param.toString());
        logger.info("用户兑奖接口返回信息为：" + saveUserAwardMsg);
        return saveUserAwardMsg.toString();
    }

    /**
     * 
     * 查询用户兑奖信息
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryUserAwardInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    public String queryUserAwardInfo(HttpServletRequest request) {
        String userId = request.getParameter("userId");
        String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        if (openId == null) {
            setOpenIdInSession(userId, request);
        }
        openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String activityId = request.getParameter("activityId");
        JSONObject param = new JSONObject();
        param.put("openid", openId);
        param.put("activityId", activityId);
        JSONObject saveUserAwardMsg = customerAwardManager.queryUserAwardInfo(param.toString());
        logger.info("用户兑奖接口返回信息为：" + saveUserAwardMsg);
        return saveUserAwardMsg.toString();
    }

    /**
     * 
     * 修改用户兑奖信息
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/updateUserAwardInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    public String updateUserAwardInfo(HttpServletRequest request) {
        JSONObject param = new JSONObject();
        String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String userId = request.getParameter("userId");
        if (openId == null) {
            setOpenIdInSession(userId, request);
        }
        openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String activityId = request.getParameter("activityId");
        String userFullName = request.getParameter("userFullName");
        String mobile = request.getParameter("mobile");
        String address = request.getParameter("address");
        String userinfoId = request.getParameter("userinfoId");
        param.put("openid", openId);
        param.put("userFullName", userFullName);
        param.put("mobile", mobile);
        param.put("address", address);
        param.put("activityId", activityId);
        param.put("userinfoId", userinfoId);
        JSONObject result = customerAwardManager.updateUserAwardInfo(param.toString());
        logger.info("修改用户兑奖信息返回信息:" + result);
        return result.toString();
    }

    /**
     * 查询系统通知接口
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fatherDayActivity/querySystemNotice.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String querySystemNotice() {
        JSONObject obj = customerAwardManager.querySystemNotice();
        return obj.toString();
    }

    /**
     * 
     * 查询分享信息接口
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/queryShareInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    public String queryShareInfo(HttpServletRequest request) {
        String activityId = request.getParameter("activityId");
        String channel = request.getParameter("channel");
        if (StringUtil.isEmpty(activityId) || StringUtil.isEmpty(channel)) {
            JSONObject obj = new JSONObject();
            obj.put("returnCode", "1");
            obj.put("returnMsg", "关键参数缺失");
            return obj.toString();
        }
        return customerAwardManager.queryShareInfo(activityId, channel).toString();
    }

    /**
     * 
     * 查询用户通知接口
     * 
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fatherDayActivity/queryUserNotice.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String queryUserNotice(@RequestParam("userId") String userId, @RequestParam("activityId") String activityId, HttpServletRequest request) {
        String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        if (openId == null) {
            setOpenIdInSession(userId, request);
        }
        openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        JSONObject obj = customerAwardManager.queryUserNotice(openId, activityId);
        return obj.toString();
    }

    /**
     * 查询所有用户的中奖信息
     * 
     * @param userId
     * @param activityId
     * @param request
     * @param beginIdx
     * @param endIdx
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/fatherDayActivity/queryAllUserAwardInfo.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String queryAllUserAwardInfo(@RequestParam("activityId") String activityId, HttpServletRequest request, String beginIdx, String endIdx) {
        int tempBeginIdx = 0;
        int tempEndIdx = 10;
        if (!StringUtils.isBlank(beginIdx)) {
            try {
                tempBeginIdx = Integer.parseInt(beginIdx);
            } catch (NumberFormatException e) {
                logger.error("传入的beginIdx值格式不对,", e);
                tempBeginIdx = 0;
            }
        }
        if (!StringUtils.isBlank(endIdx)) {
            try {
                tempEndIdx = Integer.parseInt(endIdx);
            } catch (NumberFormatException e) {
                logger.error("传入的endIdx值格式不对,", e);
                tempEndIdx = 10;
            }
        }
        JSONObject obj = customerAwardManager.queryAllUserAwardInfo(activityId, tempBeginIdx, tempEndIdx);
        return obj.toString();
    }

    /**
     * Judging whether the user has already drawn a lottery Suitable for lottery
     * only once
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/activity/isLottery.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String isLottery(HttpServletRequest request) {
        logger.info("查询用户是否已抽奖方法>>>>>isLottery>>>start>>>入参userId:" + request.getParameter("userId") + ",activityId:" + request.getParameter("activityId"));
        JSONObject resp = new JSONObject();
        JSONObject dataJson = new JSONObject();
        String userId = request.getParameter("userId");
        String retStr = setOpenIdInSession(userId, request);
        if ("-1".equals(retStr)) {
            resp.put("returnCode", "1");
            resp.put("returnMsg", "userId解密失败");
            return resp.toString();
        }
        String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String activityId = request.getParameter("activityId");
        String ret = customerAwardManager.queryUserAward(openId, activityId, null, null);
        JSONObject retJson = JSONObject.fromObject(ret);
        String state = "0";
        if ("0".equals(retJson.getString("returnCode"))) {
            if (retJson.containsKey("data") && ((List<?>) retJson.get("data")).size() > 0) {
                state = "1";
            }
            resp.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
            resp.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
            dataJson.put("state", state);
            resp.put("data", dataJson);
        } else {
            resp.put("returnCode", retJson.get("returnCode"));
            resp.put("returnMsg", retJson.get("returnMsg"));
        }
        return resp.toString();
    }

    /**
     * Query the number of prizes for the event
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/activity/queryAwardNum.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String queryAwardNum(HttpServletRequest request) {
        logger.info("查询活动奖品数量>>>>>queryAwardNum>>>start>>>入参pmst:" + request.getParameter("pmst") + ",pmky:" + request.getParameter("pmky") + ",awardId:"
                + request.getParameter("awardId"));
        JSONObject resp = new JSONObject();
        JSONObject dataJson = new JSONObject();
        String pmst = request.getParameter("pmst");
        String pmky = request.getParameter("pmky");
        String awardId = request.getParameter("awardId");
        JSONObject ret = customerAwardManager.queryAwardNum(pmst, pmky, awardId);
        if ("0".equals(ret.getString("returnCode"))) {
            dataJson.put("num", ret.get("num"));
        }
        resp.put("data", dataJson);
        resp.put("returnCode", ret.get("returnCode"));
        resp.put("returnMsg", ret.get("returnMsg"));
        return resp.toString();
    }

    /**
     * Logic method of public lottery
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/activity/lotteryLogic.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String lotteryLogic(HttpServletRequest request) {
        logger.info("抽奖方法>>>>>lotteryLogic>>>start>>>入参userId:" + request.getParameter("userId") + ",activityId:" + request.getParameter("activityId"));
        String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String userId = request.getParameter("userId");
        if (openId == null) {
            setOpenIdInSession(userId, request);
        }
        openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String activityId = request.getParameter("activityId");
        return customerAwardManager.lotteryLogic(openId, activityId).toString();
    }

    /**
     * Inquiry for customer manager information
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/activity/queryAllCustService.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String queryAllCustService(HttpServletRequest request) {
        logger.info("查询专属顾问信息>>>>>queryAllCustService>>>start>>>入参userId：" + request.getParameter("userId") + ",activityId:" + request.getParameter("activityId"));
        JSONObject resp = new JSONObject();
        JSONObject dataJson = new JSONObject();
        String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String userId = request.getParameter("userId");
        if (openId == null) {
            setOpenIdInSession(userId, request);
        }
        openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        CustomerServiceDto customer = null;
        try {
            // 查询用户是否已绑定专属顾问
            InvectorUserInfoexDto userInfo = activityDao.queryUserInfoByOpenId(openId);
            if (null != userInfo) {
                if (StringUtils.isEmpty(userInfo.getCustserid())) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("custserType", "0");
                    List<CustomerServiceDto> list = actParameterDao.queryCustServiceInfo(map);
                    customer = list.get((int) (Math.random() * list.size()));
                    map = new HashMap<String, Object>();
                    map.put("openId", openId);
                    map.put("custserid", customer.getCustserId());
                    // 更新用户的专属顾问信息
                    activityDao.updateUserInfoCustser(map);
                } else {
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("custserId", userInfo.getCustserid());
                    List<CustomerServiceDto> list = actParameterDao.queryCustServiceInfo(paramMap);
                    if (null != list && list.size() > 0) {
                        customer = list.get(0);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("custserType", "0");
                        list = actParameterDao.queryCustServiceInfo(map);
                        customer = list.get((int) (Math.random() * list.size()));
                        map = new HashMap<String, Object>();
                        map.put("openId", openId);
                        map.put("custserid", customer.getCustserId());
                        // 更新用户的专属顾问信息
                        activityDao.updateUserInfoCustser(map);
                    }
                }
            } else {
                logger.info("根据openid未查询到用户信息");
                resp.put("returnCode", "1");
                resp.put("returnMsg", "用户信息为空");
            }
            dataJson.put("object", customer);
            resp.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
            resp.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
            resp.put("data", dataJson);
        } catch (Exception e) {
            logger.error("查询专属顾问信息>>>>>queryAllCustService>>>>出现异常，异常信息：", e);
            resp.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            resp.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }

        logger.info("查询专属顾问信息>>>>>queryAllCustService>>>end>>>");
        return resp.toString();

    }

    /**
     * 判断用户是否已做高级授权
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/activity/isHighAuth.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String isHighAuth(HttpServletRequest request) {
        logger.info("判断用户是否已做高级授权>>>>>isHighAuth>>>start>>>入参userId:" + request.getParameter("userId"));
        JSONObject resp = new JSONObject();
        try {
            boolean flag = false;
            String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
            String userId = request.getParameter("userId");
            if (openId == null) {
                setOpenIdInSession(userId, request);
            }
            openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
            InvectorUserInfoexDto dto = activityDao.queryUserInfoByOpenId(openId);
            if (null != dto) {
                if (!StringUtils.isEmpty(dto.getNickName())) {
                    flag = true;
                }
            }
            resp.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
            resp.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
            JSONObject dataJson = new JSONObject();
            dataJson.put("flag", flag);
            resp.put("data", dataJson);
        } catch (Exception e) {
            logger.error("调用isHighAuth方法发生异常,异常信息:", e);
            resp.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            resp.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        return resp.toString();
    }

    /**
     * 发送模板消息
     * 
     * @param dto
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/activity/sendTemplateMsg.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String sendTempleteMsg(HttpServletResponse response, HttpServletRequest request) {
        logger.info("发送模板消息>>>>>sendTempleteMsg>>>start>>>入参userId：" + request.getParameter("userId") + ",tempType:" + request.getParameter("tempType"));
        JSONObject resp = new JSONObject();
        String data = "";
        String openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String userId = request.getParameter("userId");
        if (StringUtils.isBlank(openId)) {
            String executeStatus = setOpenIdInSession(userId, request);
            if ("-1".equals(executeStatus)) {// 根据加密的userid获取openid失败，直接返回
                logger.warn("根据userid获取openid异常");
                resp.put("returnCode", executeStatus);
                return resp.toString();
            } else {// 获取到，则从session中获取openid
                openId = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
            }
        }
        String tempType = request.getParameter("tempType");
        if (StringUtils.isEmpty(tempType)) {
            tempType = "TEMPLATE_CONF";
        }
        try {
            logger.info("调用front发送模板消息");
            String url = "" + SpringContextUtil.getProperty("FRONTSERVICE_URL") + "/sendTemplateMsg.html";
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            param.add(new BasicNameValuePair("toUserId", openId));
            ParameterDto dto = new ParameterDto();
            dto.setPmst("SYSTEM");
            dto.setPmky(tempType);
            List<ParameterDto> list = actParameterDao.getParameter(dto);
            for (ParameterDto d : list) {
                if (d.getPmco().contains("TEMPID"))
                    param.add(new BasicNameValuePair("templateId", d.getPmnm()));
                if (d.getPmco().contains("TEMPURL"))
                    param.add(new BasicNameValuePair("url", d.getPmnm()));
                if (d.getPmco().contains("TEMPDATA"))
                    data = d.getPmnm();

            }
            param.add(new BasicNameValuePair("filter", "5"));
            param.add(new BasicNameValuePair("content", cn2unicode(WeixinUtil.genTempLateUtil(data.replace("{awardtime}", new SimpleDateFormat("yyyy-MM-dd").format(new Date()))))));
            resp.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
            resp.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
            resp.put("data", JSONObject.fromObject(HttpPostUtil.sendPostToFront(url, param, null)));
        } catch (Exception e) {
            logger.error("调用front发送微信模板消息异常", e);
            resp.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            resp.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        return resp.toString();
    }

    public static String cn2unicode(final String gbString) {
        char[] utfBytes = gbString.toCharArray();
        String unicodeBytes = "";
        for (int byteIndex = 0; byteIndex < utfBytes.length; byteIndex++) {
            String hexB = Integer.toHexString(utfBytes[byteIndex]); // 转换为16进制整型字符串
            if (hexB.length() <= 2) {
                hexB = "00" + hexB;
            }
            unicodeBytes = unicodeBytes + "\\u" + hexB;
        }
        return unicodeBytes;
    }

    /**
     * Inquiry for customer manager information
     * 
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/activity/queryCustService.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.GET, RequestMethod.POST })
    public String queryCustService(HttpServletRequest request) {
        logger.info("查询专属顾问信息>>>>>queryCustService>>>start>>>入参userId：" + request.getParameter("userId"));
        JSONObject resp = new JSONObject();
        try {
            String userId = request.getParameter("userId");
            InvectorUserInfoexDto userInfo = activityManager.queryUserInfoByUerId(userId);
            if (null != userInfo) {
                CustomerServiceDto customer = null;
                if (StringUtils.isEmpty(userInfo.getCustserid())) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("state", "1");
                    List<CustomerServiceDto> list = actParameterDao.queryCustServiceInfo(map);
                    customer = list.get(0);
                } else {
                    Map<String, Object> paramMap = new HashMap<String, Object>();
                    paramMap.put("custserId", userInfo.getCustserid());
                    List<CustomerServiceDto> list = actParameterDao.queryCustServiceInfo(paramMap);
                    if (null != list && list.size() > 0) {
                        customer = list.get(0);
                    } else {
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("state", "1");
                        list = actParameterDao.queryCustServiceInfo(map);
                        customer = list.get(0);
                    }
                }
                customer.setCustserPersonalQrCodeStr(byte2Str(customer.getCustserPersonalQrCode()));
                JSONObject dataJson = new JSONObject();
                dataJson.put("object", customer);
                resp.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
                resp.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
                resp.put("data", dataJson);
            } else {
                logger.info("根据userid未查询到用户信息");
                resp.put("returnCode", "1");
                resp.put("returnMsg", "用户信息为空");
            }
        } catch (Exception e) {
            logger.error("查询专属顾问信息>>>>>queryCustService>>>>出现异常，异常信息：", e);
            resp.put("returnCode", WXConstants.COMMON_ERROR_SYSERRCODE);
            resp.put("returnMsg", WXConstants.COMMON_ERROR_SYSERRMSG);
        }
        logger.info("查询专属顾问信息>>>>>queryCustService>>>end>>>");
        return resp.toString();
    }

    public String byte2Str(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return null;
        } else {
            return new String(bytes);
        }
    }

    @RequestMapping(value = "/gdbmw/getCard.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String gdBmwGetCard(HttpServletRequest request) {
        JSONObject returnObj = new JSONObject();
        String activityId = request.getParameter("activityId");
        String openid = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String encryptedUserId = request.getParameter("userId");
        if (StringUtils.isBlank(activityId)) {
            logger.info("活动id为空，返回错误信息");
            returnObj.put("returnCode", WXConstants.RETURN_CODE_9008);
            returnObj.put("returnMsg", WXConstants.RETURN_MSG_9008);
            return returnObj.toString();
        }
        if (StringUtils.isBlank(openid)) {
            String executeStatus = setOpenIdInSession(encryptedUserId, request);
            if ("-1".equals(executeStatus)) {// 根据加密的userid获取openid失败，直接返回
                logger.warn("根据userid获取openid异常");
                returnObj.put("returnCode", executeStatus);
                return returnObj.toString();
            } else {// 获取到，则从session中获取openid
                openid = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
            }
        }

        String userFullName = request.getParameter("userFullName");
        String userMobile = request.getParameter("userMobile");
        String sessionID = request.getParameter("sessionID");
        String smsCode = request.getParameter("smsCode");
        String awardId = request.getParameter("awardId");
        String seqId = request.getSession().getId();
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userFullName", userFullName);
        paramMap.put("userMobile", userMobile);
        paramMap.put("sessionID", sessionID);
        paramMap.put("smsCode", smsCode);
        paramMap.put("seqId", seqId);
        paramMap.put("openid", openid);
        paramMap.put("activityId", activityId);
        paramMap.put("awardId", awardId);
        paramMap.put("openid", openid);
        // 验证发送手机短信验证码是保存在session中的手机号码
        String smsMobile = (String) request.getSession(true).getAttribute(SessionValue.SESSION_USERMOBILE);
        if (smsMobile == null) {
            returnObj.put("returnCode", "9998");// 未获取到发送验证码时，session中保存的手机号码
            return returnObj.toString();
        } else if (!smsMobile.equals(userMobile)) {
            returnObj.put("returnCode", "9997");// 用户验证手机验证码，提交的手机号码和获取短信验证码的手机号码不一致
            return returnObj.toString();
        }

        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, WXConstants.WEIXIN_CHANNEL, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");

        // 验证短信验证码
        String returnStr = messageManager.checkVrfCode(context, sessionID, userMobile, smsCode);
        if (returnStr == null || !WXConstants.COMMON_SUCCESS.equals(returnStr)) {
            returnObj.put("returnCode", "9996");// 手机短信验证码验证失败
            return returnObj.toString();
        }
        return customerAwardManager.gdBmwGetCard(paramMap).toString();
    }

    /**
     * 查询已参加活动人数
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/gdbmw/queryAllWinningCount.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String queryAllWinningCount(HttpServletRequest request) {
        return customerAwardManager.queryAllWinningCount(request.getParameter("activityId"), "2").toString();
    }

    /**
     * 查询奖品列表
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/gdbmw/queryAwardList.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String queryAwardList(HttpServletRequest request) {
        return customerAwardManager.queryAwardList(request.getParameter("activityId")).toString();
    }

    /**
     * 转盘抽奖方法
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/gdbmw/turnDraw.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String turnDraw(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String smsCode = request.getParameter("smsCode");
        String activityId = request.getParameter("activityId");
        String openid = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String encryptedUserId = request.getParameter("userId");
        String telePhone = request.getParameter("telePhone");
        String name = request.getParameter("name");
        String seqId = request.getSession().getId();
        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, WXConstants.WEIXIN_CHANNEL, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        // 验证短信验证码
        String returnStr = messageManager.checkVrfCode(context, sessionID, telePhone, smsCode);
        JSONObject json = new JSONObject();
        if (returnStr != null && returnStr.equals("0000")) {
            if (StringUtils.isBlank(openid)) {
                String executeStatus = setOpenIdInSession(encryptedUserId, request);
                if ("-1".equals(executeStatus)) {// 根据加密的userid获取openid失败，直接返回
                    logger.warn("根据userid获取openid异常");
                    json.put("returnCode", executeStatus);
                    return json.toString();
                } else {// 获取到，则从session中获取openid
                    openid = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
                }
            }
            json = customerAwardManager.turnDraw(activityId, openid, telePhone, name);
        } else {
            json.put("returnCode", "9996");
        }
        return json.toString();
    }

    /**
     * 查询用户关注状态
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/getSubscribeState.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String getSubscribeState(HttpServletRequest request) {
        String openid = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String encryptedUserId = request.getParameter("userId");
        JSONObject json = new JSONObject();
        if (StringUtils.isBlank(openid)) {
            String executeStatus = setOpenIdInSession(encryptedUserId, request);
            if ("-1".equals(executeStatus)) {// 根据加密的userid获取openid失败，直接返回
                logger.warn("根据userid获取openid异常");
                json.put("returnCode", executeStatus);
                return json.toString();
            } else {// 获取到，则从session中获取openid
                openid = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
            }
        }
        json.put("returnCode", WXConstants.COMMON_SUCCESS_CODE);
        json.put("returnMsg", WXConstants.COMMON_SUCCESS_MSG);
        json.put("data", customerAwardManager.getSubscribeState(openid));
        return json.toString();

    }

    /**
     * 保存用户预约信息
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/wlqc/saveEnrolmentUser.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String saveEnrolmentUser(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String smsCode = request.getParameter("smsCode");
        String activityId = request.getParameter("activityId");
        String openid = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String encryptedUserId = request.getParameter("userId");
        String mobile = request.getParameter("mobile");
        String name = request.getParameter("name");
        String state = request.getParameter("state");
        String seqId = request.getSession().getId();
        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, WXConstants.WEIXIN_CHANNEL, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        // 验证短信验证码
        String returnStr = messageManager.checkVrfCode(context, sessionID, mobile, smsCode);
        JSONObject json = new JSONObject();
        if (returnStr != null && returnStr.equals("0000")) {
            if (StringUtils.isBlank(openid)) {
                String executeStatus = setOpenIdInSession(encryptedUserId, request);
                if ("-1".equals(executeStatus)) {// 根据加密的userid获取openid失败，直接返回
                    logger.warn("根据userid获取openid异常");
                    json.put("returnCode", executeStatus);
                    return json.toString();
                } else {// 获取到，则从session中获取openid
                    openid = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
                }
            }
            json = customerAwardManager.saveEnrolmentUser(activityId, openid, mobile, name, state);
        } else {
            json.put("returnCode", "9996");
        }
        return json.toString();
    }

    /**
     * 活动推送短信
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/activity/sendMessage.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String sendMessage(HttpServletRequest request) {
        String mobile = request.getParameter("mobile");
        String msgType = request.getParameter("msgType");
        return customerAwardManager.sendMsg(mobile, msgType).toString();
    }

    /**
     * 保存用户抽奖号码信息
     * 
     * @param request
     * @return
     */
    @RequestMapping(value = "/wlqc/saveUserLotteryNum.xhtml", produces = "text/html;charset=UTF-8", method = { RequestMethod.POST, RequestMethod.GET })
    @ResponseBody
    public String saveUserLotteryNum(HttpServletRequest request) {
        String sessionID = request.getParameter("sessionID");
        String smsCode = request.getParameter("smsCode");
        String activityId = request.getParameter("activityId");
        String openid = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
        String encryptedUserId = request.getParameter("userId");
        String mobile = request.getParameter("mobile");
        String name = request.getParameter("name");
        String seqId = request.getSession().getId();
        // 日志信息
        Context context = ContextUtils.setContext(WXConstants.USER_SERVICE_001, WXConstants.WEIXIN_CHANNEL, "", System.currentTimeMillis(), System.currentTimeMillis(), seqId, "");
        // 验证短信验证码
        String returnStr = messageManager.checkVrfCode(context, sessionID, mobile, smsCode);
        JSONObject json = new JSONObject();
        if (returnStr != null && returnStr.equals("0000")) {
            if (StringUtils.isBlank(openid)) {
                String executeStatus = setOpenIdInSession(encryptedUserId, request);
                if ("-1".equals(executeStatus)) {// 根据加密的userid获取openid失败，直接返回
                    logger.warn("根据userid获取openid异常");
                    json.put("returnCode", executeStatus);
                    return json.toString();
                } else {// 获取到，则从session中获取openid
                    openid = (String) request.getSession(true).getAttribute(SessionValue.SESSION_OPENID);
                }
            }
            // 查询用户是否抽奖了
            json = customerAwardManager.saveUserLotteryNum(activityId, openid, mobile, name);
        } else {
            json.put("returnCode", "9996");
        }
        return json.toString();
    }
}
