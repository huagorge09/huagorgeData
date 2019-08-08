package com.cmwa.ec.weixin.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dto.UserInfoDto;
import com.cmwa.ec.weixin.dto.WeixinInfoUserDto;
import com.cmwa.ec.weixin.util.WeixinUtil;
import com.cmwa.ec.weixin.util.cache.ParameterCache;


/**
 * 拦截器
 * 
 * @author jouislu
 * 
 */

public class PageVerificationInterceptor implements HandlerInterceptor{
	private static Logger logger = Logger.getLogger(PageVerificationInterceptor.class.getName());
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		 
		
	}

	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
			Object handler) throws Exception {
		
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		Method method=handlerMethod.getMethod();
		TracingModel model=InterceptorUtil.extractTracingModel(method);
		/*---- 编码设置-----*/
        if(model.getCharset()!=null&&!"".equals(model.getCharset().trim())){
        	req.setCharacterEncoding(model.getCharset());
        }
        /*-----参数校验-----*/
        boolean flag=InterceptorUtil.extractPramas(model, req);

        if(!flag){
        	resp.sendRedirect(model.getRedirectUrl());
        	return false;
        }
       
     
    
        /**
         * 登陆校验
         * 1、检查cookie是否有openId，如果有，直接返回
         * 2、302到微信获取code
         * 3、通过code获取openId
         * */
        if(model.isNeedLogin()){
        	return loginHandle(req,resp);
        }
		return true;
	}
	
	private boolean loginHandle(HttpServletRequest req, HttpServletResponse resp){
		boolean bool=false;
		String cookieOpenId="";
    	//获取cookie
		Cookie[] cookies=req.getCookies();
		if(cookies!=null){
			for(Cookie c:cookies){
				if(c.getName().equals("openId")){
					logger.info("cookie openId【   name=["+c.getName()+"]      value=["+c.getValue()+"]   】");
					bool=true;
					cookieOpenId=c.getValue().trim();
					break;
				}
			}
		}
		
    	//判断openID是否存在于cookie中
		if(bool){
			req.setAttribute("openId", cookieOpenId);
			
			UserInfoDto userInfoPo = new UserInfoDto();
			try {
				//userInfoPo = userInfoManager.queryUserInfoByOpenId(cookieOpenId);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("拦截器查询userid异常",e);
			}
			req.setAttribute("userId", userInfoPo.getUserid());
			
		}else{
			//做302跳转
	    	String code=req.getParameter("code");
	    	logger.info("code----:"+code);
	    	if(code==null){
	    		String wxHost = ParameterCache.getValue(WXConstants.PMST_CONFIG, WXConstants.PMKY_PUBLIC, "wxHost");
	    		
	    		String redirectUrl = wxHost+req.getRequestURI();
	    		logger.info("loginHandle#redirectUrl:"+redirectUrl);
	    		String wxAuthorUrl= WeixinUtil.wrapRedirectUrl(redirectUrl);
	    
	
	    		try {
	    			resp.sendRedirect(wxAuthorUrl);
	    			return false;
	
	    		}  catch (Exception e) {
	    			e.printStackTrace();
	
	    		}
	    	}else{
	    		
	    		WeixinInfoUserDto weixinUserInfo = WeixinUtil.getPageAuthor(code);
	    		logger.info("loginHandle#openid:"+weixinUserInfo.getOpenid());
	    		if(weixinUserInfo!=null&&weixinUserInfo.getOpenid()!=null){
	    			req.setAttribute("openId", weixinUserInfo.getOpenid());
	    			UserInfoDto userInfoPo = new UserInfoDto();
					try {
						//userInfoPo = userInfoManager.queryUserInfoByOpenId(weixinUserInfo.getOpenid());
					} catch (Exception e) {
						logger.error("拦截器查询userid异常",e);
					}
	    			req.setAttribute("userId", userInfoPo.getUserid());
	    		}
	    	}
		}
    	return true;
	}
 
}
