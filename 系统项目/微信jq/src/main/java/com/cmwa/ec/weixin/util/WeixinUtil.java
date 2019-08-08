package com.cmwa.ec.weixin.util;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.cmwa.ec.query.facade.dto.fund.ProductEstimate;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dto.WeixinInfoUserDto;
import com.cmwa.ec.weixin.util.cache.ParameterCache;
import com.cmwa.ec.weixin.util.socket.SocketUtil;

public class WeixinUtil {

    private static final Logger logger = Logger.getLogger(WeixinUtil.class);

    /**
     * 获取token
     * 
     * @return
     */
    public static String getToken() {
        return ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_PUBLIC, "token");
    }

    public static String getWeiXinUserName() {
        return ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_PUBLIC, "weiXinUserName");
    }

    // 后续需要增加加密
    public static String getKeyStorePassword() {
        return ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_PUBLIC, "keyStorePassword");
    }

    public static String getKeyStoreFile() {
        return ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_PUBLIC, "keyStoreFile");
    }

    /**
     * 获取会话TOKEN,实时查询
     * 
     * @return
     */
    public static String getAccessToken() {
        /*
         * return ParameterCache.getValue(WXConstants.PMST_CONFIG,
         * WXConstants.PMKY_PUBLIC, "accessToken");
         */

        return "";
    }

    /**
     * 获取会话TOKEN
     * 
     * @return
     */
    public static int getInvalidTime() {
        String strInvalid = ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_PUBLIC, "invalidTime");
        int invalidSeconds = 300000;// 默认5分钟
        if (strInvalid == null)
            return invalidSeconds;

        try {
            invalidSeconds = Integer.parseInt(strInvalid);
        } catch (Exception e) {
            logger.error("获取回话token异常:", e);
        }
        return invalidSeconds;
    }

    /**
     * 获取会话TOKEN
     * 
     * @return
     */
    public static String getTradeSignKey() {
        return ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_PUBLIC, "tradeSignKey");
    }

    public static String getCreateMenuUrl() {
        return getUrl(WXConstants.URL_CREATEMENU);
    }

    public static String getDeleteMenuUrl() {
        return getUrl(WXConstants.URL_DELETEMENU);
    }

    public static String getQueryMenuUrl() {
        return getUrl(WXConstants.URL_QUERYMENU);
    }

    public static String getAccessTokenUrl() {
        return getUrl(WXConstants.URL_ACCESSTOKEN);
    }

    public static String getBindAccUrl() {
        return getUrl(WXConstants.URL_BINDACC);
    }

    public static String getUrlRegister() {
        return getUrl(WXConstants.URL_REGISTER);
    }

    public static String getUnBindAccUrl() {
        return getUrl(WXConstants.URL_UNBINDACC);
    }

    public static String getOpenTradeUrl() {
        return getUrl(WXConstants.URL_OPENTRADE);
    }

    public static String getCloseTradeUrl() {
        return getUrl(WXConstants.URL_CLOSETRADE);
    }

    public static String getPurchurseUrl() {
        return getUrl(WXConstants.URL_PURCHURSE);
    }

    public static String getRedeemUrl() {
        return getUrl(WXConstants.URL_REDEEM);
    }

    public static String getPurchurseNoticeUrl() {
        return getUrl(WXConstants.URL_PURCHURSENOTICE);
    }

    public static String getRedeemNoticeUrl() {
        return getUrl(WXConstants.URL_REDEEMNOTICE);
    }

    /**
     * 获取微信分配的Appid
     * 
     * @return
     */
    public static String getAppid() {
        return ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_PUBLIC, "appid");
    }

    /**
     * 获取微信分配的Secret
     * 
     * @return
     */
    public static String getSecret() {
        return ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_PUBLIC, "secret");
    }

    /**
     * 获取返回文本消息最大程度
     * 
     * @return
     */
    public static int getMaxTextLength() {
        return 1024;
    }

    private static String getUrl(String key) {
        return ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_URL, key.replaceAll("[<|>]", ""));
    }

    public static WeixinInfoUserDto getPageAuthor(String code) {
        try {
            String appid = WeixinUtil.getAppid();
            String appSecret = WeixinUtil.getSecret();
            String authorUrl = ParameterCache.getUrl(WXConstants.AUTHOR2_URL_OPENID);
            String url = authorUrl + "?appid=" + appid + "&secret=" + appSecret + "&code=" + code + "&grant_type=authorization_code";
            String reqXml = StringUtils.toXmlMessage("author2.0", url);
            System.out.println("+++++++++++++:" + reqXml);
            String resStr = SocketUtil.sendSocketMessage(reqXml);

            WeixinInfoUserDto wxDto = new ObjectMapper().readValue(resStr, WeixinInfoUserDto.class);
            if (wxDto != null) {
                return wxDto;
            } else {
                return null;
            }
        } catch (Exception e) {
            logger.error("getPageAuthor异常：", e);
        }
        return null;
    }

    public static String getOpenId(HttpServletRequest request, HttpServletResponse response, String redirectUrl) {
        String code = request.getParameter("code");
        String status = request.getParameter("state");
        logger.info("------------code:" + code + ";state:" + status + ";redirectUrl:" + redirectUrl);

        if (code == null || code.equals("")) {
            try {
                String callBackUrl = URLEncoder.encode(redirectUrl);
                String appid = WeixinUtil.getAppid();
                String authorUrl = ParameterCache.getUrl(WXConstants.AUTHOR2_URL_KEY);
                String wxAuthor = authorUrl + "?appid=" + appid + "&redirect_uri=" + callBackUrl + "&response_type=code&scope=snsapi_base&state=sdfsfd#wechat_redirect";
                logger.info("wxAuthor:" + wxAuthor);
                response.sendRedirect(wxAuthor);

            } catch (Exception e) {
                logger.error("authorAPI:", e);
            }
        }
        WeixinInfoUserDto weixinUserInfo = WeixinUtil.getPageAuthor(code);
        if (weixinUserInfo == null) {
            logger.info("GetOpenID null!code:" + code);
            return null;
        }
        String openId = weixinUserInfo.getOpenid();
        logger.info("=========openid:" + openId);
        return null;
    }

    public static void sendRedirect(HttpServletResponse response, String redirectUrl) {

        try {
            String wxAuthor = wrapRedirectUrl(redirectUrl);
            logger.info("sendRedirect url：" + wxAuthor);
            response.sendRedirect(wxAuthor);
        } catch (Exception e) {
            logger.error("sendRedirect error：" + "跳转发生异常", e);
        }
    }

    public static String wrapRedirectUrl(String redirectUrl) {
        String callBackUrl = URLEncoder.encode(redirectUrl);
        String appid = WeixinUtil.getAppid();
        String authorUrl = ParameterCache.getUrl(WXConstants.AUTHOR2_URL_KEY);
        String wxAuthor = authorUrl + "?appid=" + appid + "&redirect_uri=" + callBackUrl + "&response_type=code&scope=snsapi_base&state=CMWA#wechat_redirect";
        logger.info("wrapRedirectUrl#wxAuthor" + wxAuthor);
        return wxAuthor;
    }

    public static String wrapRedirectUrl(String redirectUrl, String state) {
        String callBackUrl = URLEncoder.encode(redirectUrl);
        String encodeState = URLEncoder.encode(state);
        String appid = WeixinUtil.getAppid();
        String authorUrl = ParameterCache.getUrl(WXConstants.AUTHOR2_URL_KEY);
        String wxAuthor = authorUrl + "?appid=" + appid + "&redirect_uri=" + callBackUrl + "&response_type=code&scope=snsapi_base&state=" + encodeState + "#wechat_redirect";
        logger.info("wrapRedirectUrl#wxAuthor" + wxAuthor);
        return wxAuthor;
    }

    /**
     * 从代理服务器获取jsapi_ticket
     * 
     * @return
     */
    public static String getJsapiTicket() {
        String ticket = null;

        try {
            String xmlPost = StringUtils.toXmlMessage("getJsapiTicket", "getJsapiTicket");
            logger.info("xmlPost : " + xmlPost);
            ticket = SocketUtil.sendSocketMessage(xmlPost);
            logger.info("发送socket请求获取jsapi_ticket，resStr：" + ticket);
        } catch (Exception e) {
            logger.info("发送socket请求获取jsapi_ticket，抛出异常，resStr：" + ticket, e);
        }

        return ticket;
    }

    /**
     * 调用微信 JS SDK ，签名生成方法 签名生成规则如下：参与签名的字段包括noncestr（随机字符串）, 有效的jsapi_ticket,
     * timestamp（时间戳）, url（当前网页的URL，不包含#及其后面部分）
     * 
     * @param jsapi_ticket
     * @param url
     * @return
     */
    public static Map<String, String> sign(String jsapi_ticket, String url) {
        Map<String, String> ret = new HashMap<String, String>();
        String nonce_str = create_nonce_str();
        String timestamp = create_timestamp();
        String string1;
        String signature = "";

        // 注意这里参数名必须全部小写，且必须有序
        string1 = "jsapi_ticket=" + jsapi_ticket + "&noncestr=" + nonce_str + "&timestamp=" + timestamp + "&url=" + url;
        System.out.println("生成调用微信JSSDK的签名，加密之前字符串：" + string1);

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(string1.getBytes("UTF-8"));
            signature = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            logger.error("不支持的算法：", e);
        } catch (UnsupportedEncodingException e) {
            logger.error("不支持的字符集：", e);
        }

        ret.put("url", url);
        ret.put("appId", getAppid());
        ret.put("jsapi_ticket", jsapi_ticket);
        ret.put("nonceStr", nonce_str);
        ret.put("timestamp", timestamp);
        ret.put("signature", signature);

        return ret;
    }

    /**
     * 从代理服务器获取ticket 获取创建永久二维码所需的ticket
     * 
     * @return
     */
    public static String getTicketByQrCode(String secId) {
        String ticket = null;

        try {
            String xmlPost = StringUtils.toXmlMessage("createQrCode", secId);
            logger.info("xmlPost : " + xmlPost);
            ticket = SocketUtil.sendSocketMessage(xmlPost);
            logger.info("发送socket请求获取创建永久二维码所需的ticket，resStr：" + ticket);
        } catch (Exception e) {
            logger.info("发送socket请求获取创建永久二维码所需的ticket，抛出异常，resStr：" + ticket, e);
        }

        return ticket;
    }

    /**
     * 从代理服务器获取永久二维码，根据ticket
     * 
     * @param titket
     * @return
     */
    public static String getQrCode(String ticket) {

        String returnStr = null;

        try {
            String xmlPost = StringUtils.toXmlMessage("getQrCode", ticket);
            logger.info("xmlPost : " + xmlPost);
            returnStr = SocketUtil.sendSocketMessage(xmlPost);
            logger.info("发送socket请求获取永久二维码，resStr：" + returnStr);
        } catch (Exception e) {
            logger.info("发送socket请求获取永久二维码，抛出异常", e);
        }

        return returnStr;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }

    private static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }

    public static void main(String args[]) {
        String redirectUrl = "http://59.37.11.118/WeixinService/Faccount/author2.0.jsp";
        System.out.println(URLEncoder.encode(redirectUrl));
    }

    /*
     * 格式化身份证
     */
    public static String getEncryptIdNo(String idNo) {
        String str = "";
        if (!"".equals(idNo) && idNo != null) {
            str = idNo.replace(idNo.substring(4, idNo.length() - 4), "**********");
        }
        return str;
    }

    /*
     * 格式化银行卡
     */
    public static String getEncryptBankCard(String bankCard) {
        String str = "";
        if (!"".equals(bankCard) && bankCard != null) {
            str = bankCard.trim().replace(bankCard.substring(4, bankCard.length() - 4), " **** **** ");
        }
        return str;
    }

    /*
     * 格式化手机号
     */
    public static String getEncryptMobile(String mobile) {
        String str = "";
        if (!"".equals(mobile) && mobile != null) {
            str = mobile.trim().replace(mobile.substring(3, mobile.length() - 4), "****");
        }
        return str;
    }

    /*
     * 格式化用户名
     */
    public static String getEncryptUserName(String name) {
        String str = "";
        String replaceStr = "";
        if (!"".equals(name) && name != null) {
            for (int i = 1; i < name.length(); i++) {
                replaceStr += "*";
            }
            str = name.trim().replace(name.substring(1, name.length()), replaceStr);
        }
        return str;
    }

    /**
     * 七夕答题得到精准的答案分数
     * 
     * @return
     */
    public static double getExactitudeMark() {
        BigDecimal sum = new BigDecimal(100);
        BigDecimal num = new BigDecimal(8);
        BigDecimal bigDecimal = new BigDecimal(4);
        return sum.divide(num).divide(bigDecimal).doubleValue();
    }

    /**
     * 计算净值浮动率
     * 
     * @param i
     * @param list
     * @return String
     * @author maj
     */
    public static String addAndDiv(int i, List<ProductEstimate> list) {
        String returnStr = "0";
        if (list == null) {
            returnStr = "0";
        } else {
            if (i == list.size() - 1) {
                returnStr = "0";
            } else {
                returnStr = ((Float.parseFloat(list.get(i).getNetValue()) - Float.parseFloat(list.get(i + 1).getNetValue())) / Float.parseFloat(list.get(i + 1).getNetValue()))
                        + "";
            }
        }
        return returnStr;
    }

    /**
     * @author ex-niebh
     * @createdt 20190104
     * @param param
     * @return
     */
    public static String genTempLateUtil(String param) {
        JSONObject json = new JSONObject();
        JSONObject dataJson = new JSONObject();
        json.put("touser", "{{toUserId}}");
        json.put("template_id", "{{templateId}}");
        json.put("url", "{{url}}");
        json.put("touser", "{{toUserId}}");
        String[] params = param.split(",");
        for (int i = 0; i < params.length; i++) {
            String[] keys = params[i].split(":");
            JSONObject key = new JSONObject();
            key.put("value", keys[1].split("&")[0]);
            key.put("color", keys[1].split("&")[1]);
            dataJson.put(keys[0], key);
        }
        json.put("data", dataJson);
        return json.toString();
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
}
