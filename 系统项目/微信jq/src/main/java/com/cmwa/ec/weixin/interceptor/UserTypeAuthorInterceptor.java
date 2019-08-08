package com.cmwa.ec.weixin.interceptor;

import java.io.IOException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.weixin.util.SessionValue;


/**
 * 用户类型权限 拦截器
 * @author liury
 *
 */
public class UserTypeAuthorInterceptor implements HandlerInterceptor{
	
	private static Logger logger = Logger.getLogger(UserTypeAuthorInterceptor.class.getName());
	
	@Override
	public void afterCompletion(HttpServletRequest arg0,
			HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {
		
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
			Object arg2, ModelAndView arg3) throws Exception {
		
	}
	
	/**
	 * 此处拦截会先经过 VerifyCmfUserIdFilter过滤器，然后才进入到该拦截器
	 */
	@Override
	public boolean preHandle(HttpServletRequest req, HttpServletResponse resp,
			Object handler) throws Exception {
		
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		
		HttpSession session = request.getSession(true);
		
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		Method method=handlerMethod.getMethod();
		TracingModel model=InterceptorUtil.extractTracingModel(method);
        
        //用户类型权限验证，
		//若当前用户类型小于被访问请求方法的注解用户类型，则跳转到非法请求页面，
		//若大于或等于被请求方法的注解用户类型，则通过此处拦截器，正常访问
        if(model.getAuthority()!=null && !"".equals(model.getAuthority().trim())){
        	
        	String reqType = request.getMethod();
        	
        	String userType = model.getAuthority();
        	
        	
        	Object obj = session.getAttribute(SessionValue.SESSION_USERBASEINFO);
        	String reqChannel = (String)request.getSession(true).getAttribute(SessionValue.SESSION_REQCHANNEL);
        	
        	if(obj!=null){
        		// session中保存的渠道来源不是"MECC" 即 不是从mecc跳转过来的 则不需要判断该用户权限
        		if(reqChannel == null || !reqChannel.equals("MECC")){
        			return true;
        		}
        		UserBaseInfoDto user = (UserBaseInfoDto)obj;
        		String sUserType = user.getUserType();
        		if(sUserType!=null && !"".equals(sUserType)){
        			try {
						if(Integer.parseInt(sUserType)<Integer.parseInt(userType)){
							logger.info("访问权限不足，请求地址："+request.getRequestURI());
							logger.info("被请求方法需要的用户权限："+userType);
							logger.info("请求用户信息：name="+user.getCustName()+",cmfUserId="+user.getCmfUserId()+",userType="+user.getUserType()+",mobile="+user.getMobile());

							JSONObject returnJsonObject = new JSONObject();
							returnJsonObject.put("signError_F", "yes");
							returnJsonObject.put("channel_F", "WXIE");
							String url = "/WeixinService/public/signError.shtml";
							errorSkip(response, reqType, returnJsonObject, url);
							
							return false;
						}
					} catch (Exception e) {
						logger.error("当前用户类型："+sUserType,e);
						logger.error("当前请求的方法："+request.getRequestURI());
						logger.error("访问该方法需要的用户类型："+userType);
					}
        		}else{
        			logger.info("当前用户的用户类型为空！");
        			
        			JSONObject returnJsonObject = new JSONObject();
					returnJsonObject.put("signError_F", "yes");
					returnJsonObject.put("channel_F", "WXIE");
					String url = "/WeixinService/public/signError.shtml";
					errorSkip(response, reqType, returnJsonObject, url);
        			
					return false;
        		}
        	}else{
        		//微信端和pc端都跳转到登录页面，让用户重新登陆
        		logger.info("当前用户session已过期，请求重定向到登录页面。");

        		JSONObject returnJsonObject = new JSONObject();
				returnJsonObject.put("cmfUserIdIsNull_F", "yes");
				returnJsonObject.put("channel_F", "otherIE");
				String url = "/WeixinService/otherIELogin/otherIELoginOrRegister.shtml";
				errorSkip(response, reqType, returnJsonObject, url);
				return false;
        	}
        	
        }
        
		return true;
	}
	
	
	
	/**
	 * 说明：请求跳转判断，若是Ajax post请求，则根据返回相应的json数据，如果是get请求，则重定向到相应页面
	 * @param response
	 * @param method 请求方式    GET,POST	
	 * @param returnJsonObject Ajax（post）请求 返回的json值
	 * @param url get请求重定向的url
	 * @throws Exception
	 */
	private void errorSkip(HttpServletResponse response,String method,JSONObject returnJsonObject,String url){
		try {
			// 如果是POST过来，则是Ajax异步请求
			if (method.equalsIgnoreCase("POST")) {
				response.getOutputStream().print(returnJsonObject.toString());
			}else{//网址输入或者页面url跳转
				response.sendRedirect(url);
			}
		} catch (IOException e) {
			logger.info(">>VerifyCmfUserIdFilter,errorSkip方法抛出异常",e);
		}
	}
	
	
	
 
}
