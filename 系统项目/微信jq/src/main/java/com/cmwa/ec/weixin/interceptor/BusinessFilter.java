package com.cmwa.ec.weixin.interceptor;

import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dto.InvectorUserInfoexDto;
import com.cmwa.ec.weixin.manager.business.ActivityManager;
import com.cmwa.ec.weixin.util.HttpPostUtil;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.SpringContextUtil;
import com.cmwa.ec.weixin.util.StringUtils;
import net.sf.json.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 将请求的页面做302跳转
 * 只实现获取OPENID功能
 * <p>
 * 若openId获取失败，则直接进入登陆页面
 *
 * @author liury 20160326
 */
public class BusinessFilter extends CommonFilter implements Filter {

    private static Logger logger = Logger.getLogger(BusinessFilter.class.getName());

    @Override
    public void destroy() {
        logger.debug("BusinessFilter destroy");
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(true);
        //如果是其他浏览器端，则直接往下走
        if (WXConstants.OTHER_CHANNEL.equals(getChannel(request))) {
            logger.info("来自其他浏览器请求...");
            chain.doFilter(request, response);
            return;
        }
        String url = request.getRequestURL().toString();
        logger.info("来自微信浏览器请求页面地址: " + url);
        String openId = (String) session.getAttribute(SessionValue.SESSION_OPENID);
        logger.info("获取当前用户Session中的openId信息: " + openId);
        if (!StringUtils.isEmptyString(openId)) {
            // 获取到OpenId返回
            logger.info("用户Session中openId存在，用户已经成功登录过，因此将请求流转处理");
            chain.doFilter(request, response);
            return;
        } else {
            logger.info("openId does not exist.");
        }
        // 检查用户是否是从FRONT重定向回来的, 检查cookie中是否有userid
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            logger.info("Request Cookies count: " + cookies.length);
            for (Cookie cookie : request.getCookies()) {
                logger.info("Cookie name: " + cookie.getName());
                if (cookie.getName().equalsIgnoreCase(SessionValue.SESSION_USERID)) {
                    // 找到FRONT返回的userid
                    String userId = cookie.getValue();
                    logger.info("Get userId from cookie " + userId);
                    if (userId != null && !userId.isEmpty()) {
                        // 设置openId
                        logger.info("Get openId by userId");
                        String newOpenId = getOpenId(userId);
                        logger.info("newOpenId " + newOpenId);
                        if (newOpenId == null || newOpenId.trim().isEmpty()) {
                            //goToLogin(request, response);
                            cookie.setMaxAge(0);
                            cookie.setDomain("cmwachina.com");
                            cookie.setPath("/");
                            response.addCookie(cookie);
                            logger.info("The userid in cookie is invalid, remove it.");
                        } else {
                            // 用户授权成功
                            session.setAttribute(SessionValue.SESSION_OPENID, newOpenId);
                            // 成功后清除cookie
                            cookie.setMaxAge(0);
                            cookie.setDomain("cmwachina.com");
                            cookie.setPath("/");
                            response.addCookie(cookie);
                            logger.info("Delete the cookie after auth.");
                            chain.doFilter(request, response);
                            return;
                        }
                    } else {
                        logger.info("userId为空，让客户去登录 ");
                        goToLogin(request, response);
                        return;
                    }
                }
            }
        } else {
            logger.info("Doesn't get cookie from the request object.");
        }
        try {
            //做302跳转 获取用户openID
            redirectToWxAuth(request, response);
            return;
        } catch (Exception e) {
            logger.error("获取openId异常", e);
            goToLogin(request, response);
        }
    }

    /**
     * 获取openId
     *
     * @param userId userId
     * @return openId
     */
    private String getOpenId(String userId) {
        ActivityManager activityManager = (ActivityManager) SpringContextUtil.getBean("activityManager");
        // 调用api接口获取openid
        logger.info("获取到API服务的加密userId为：" + userId);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        param.add(new BasicNameValuePair("encrypted", userId));
        String entity = HttpPostUtil.sendPost("decrypt", param, "");
        logger.info("userId解密API返回值为:" + entity);
        if (!StringUtils.isEmptyString(entity)) {
            JSONObject result = JSONObject.fromObject(entity);
            Boolean isSuccess = Boolean.valueOf((String.valueOf(result.get("success"))));
            if (isSuccess) {
                String decryptedUserId = String.valueOf(result.get("resp"));
                logger.info("获取到解密userId为" + decryptedUserId);
                InvectorUserInfoexDto dto = activityManager.queryUserInfoByUerId(decryptedUserId);
                logger.info("查询数据库获取的用户信息为:" + JSONObject.fromObject(dto).toString());
                if (dto != null) {
                    return dto.getOpenId();
                } else {
                    logger.warn("查询不到用户信息");
                }
            } else {
                logger.warn("调用api服务返回success：" + isSuccess);
            }
        } else {
            logger.error("调用api服务失败 api无返回值");
        }
        return null;
    }

    /**
     * 获取openId出错或openId获取为空， 将session中的channel改为OTHERIE，跳转到其他ie登陆页面
     *
     * @param request
     * @param response
     */
    private void goToLogin(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(true);
        String method = request.getMethod();
        session.setAttribute(SessionValue.SESSION_CHANNEL, WXConstants.OTHER_CHANNEL);
        try {
            //如果是POST过来，则是Ajax异步请求
            if (method.equalsIgnoreCase("POST")) {
                JSONObject returnJsonObject = new JSONObject();
                returnJsonObject.put("cmfUserIdIsNull_F", "yes");
                returnJsonObject.put("channel_F", "otherIE");
                response.getOutputStream().print(returnJsonObject.toString());
            } else {
                //网址输入或者页面url跳转 跳转到登陆页面
                request.getRequestDispatcher("/WeixinService/otherIELogin/otherIELogin.shtml")
                        .forward(request, response);
                return;
            }
        } catch (Exception e) {
            logger.error("BusinessFilter,goToLogin方法抛出异常", e);
        }

    }

    @Override
    public void init(FilterConfig arg0) throws ServletException {
        logger.debug("BusinessFilter init");
    }

}
