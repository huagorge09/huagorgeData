package com.cmwa.ec.weixin.interceptor;

import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.util.SessionValue;
import com.cmwa.ec.weixin.util.StringUtils;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

/**
 * 2019-06-04 Modified by MA Xiaoqiang
 */
public class CommonFilter {

    /**
     *
     */
    private static Logger logger = Logger.getLogger(CommonFilter.class.getName());

    /**
     * 302跳转去获取openId
     *
     * @param request  http request
     * @param response http response
     * @return openId
     * @throws Exception
     */
    public void redirectToWxAuth(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.info("302跳转去做微信静默授权，获取当前用户的openId信息");
        String code = request.getParameter("code");
        logger.info("code:" + code);
        String domain = SpringUtil.getProperty("wx.config.wxHost");
        String requestURI = request.getRequestURI();
        if (requestURI.startsWith("/")) {
            requestURI = requestURI.substring(1);
        }
        // 参数处理开始
        String parameters;
        try {
            StringBuffer parameter = new StringBuffer();
            Enumeration parameterNames = request.getParameterNames();
            if (parameterNames.hasMoreElements()) {
                parameter.append("?");
                while (parameterNames.hasMoreElements()) {
                    String parameterName = parameterNames.nextElement().toString();
                    parameter.append(parameterName).append("=");
                    parameter.append(request.getParameter(parameterName)).append("&");
                }
            }
            parameters = parameter.toString();
            if (parameters.length() > 0) {
                parameters = parameters.substring(0, parameters.length() - 1);
                parameters = parameters.replaceAll("&", "%26");
            }
        } catch (Exception e) {
            logger.error("CommonFilter, 处理URL参数时捕获异常", e);
            parameters = "";
        }
        // 参数处理结束
        domain = domain.concat("auth/proxy-silent.html?target_url=" + domain + requestURI + parameters);
        response.sendRedirect(domain);
    }

    //获取session中的channel
    public String getChannel(HttpServletRequest request) {

        HttpSession session = request.getSession(true);

        String channel = (String) session.getAttribute(SessionValue.SESSION_CHANNEL);

        if (StringUtils.isEmptyString(channel)) {
            String ieType = request.getHeader("user-agent").toLowerCase();
            channel = WXConstants.WEIXIN_CHANNEL;//默认为微信浏览器浏览
            if (ieType.indexOf("micromessenger") < 0) {//其他浏览器
                channel = WXConstants.OTHER_CHANNEL;
            }
            logger.info("CommonFilter--浏览渠道：channel=" + channel);
            session.setAttribute(SessionValue.SESSION_CHANNEL, channel);
        }

        return channel;
    }

}
