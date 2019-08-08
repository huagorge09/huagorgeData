package com.cmwa.ec.webapp.interceptor;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cmwa.ec.user.facade.dto.BuriedDataDto;
import com.cmwa.ec.webapp.client.UserServiceClient;
import com.cmwa.ec.webapp.util.SessionValue;
import com.cmwa.ec.webapp.util.SpringContextUtil;

/**
 * 将请求的JSP页面做302跳转 获取OPENID
 * 
 * @author niedc 20141203
 * 
 */
public class PageFilter implements Filter {

	private static Logger logger = Logger.getLogger(PageFilter.class.getName());
	
	private UserServiceClient userServiceClient;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		logger.debug("VerifyOpenIdFilter destroy");

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		// 因使用autowired无法注入该类
		userServiceClient = (UserServiceClient)SpringContextUtil.getBean("userServiceClient");
		String temp = request.getRequestURI();
		logger.info("【【【【【【进入了过滤器，此时请求的地址为----"+temp+"】】】】】】");
		
		String method = request.getMethod();
		Object obj = request.getSession(true).getAttribute(SessionValue.SESSION_USERBASEINFO);
		
		String pageSourceId = request.getParameter("pageSourceId");
		String eventId = request.getParameter("eventId");
		if(obj == null){
			if(pageSourceId != null && !"".equals(pageSourceId) && eventId != null && !"".equals(eventId)){
				BuriedDataDto buriedDataDto = new BuriedDataDto();
				buriedDataDto.setBuriedPageSource(pageSourceId);
				buriedDataDto.setBuriedPageId(pageSourceId);
				buriedDataDto.setBuriedEventId(eventId);
				userServiceClient.insertBuriedData(buriedDataDto);
				response.sendRedirect("/login/login.shtml?pageSourceId="+pageSourceId);
				return;
			}
			
			JSONObject returnJsonObject = new JSONObject();
			returnJsonObject.put("isLogin", "no");
			String requestUrl = request.getRequestURI().toString();// 获取请求的url
			String requestParam = request.getQueryString();
			if(requestParam != null && !requestParam.equals("")){
				requestUrl = requestUrl + "?" + requestParam;
			}
			
			// 不包含xhtml，即shtml的，将地址保存到session
			if(!requestUrl.contains("xhtml")){
				request.getSession(true).setAttribute(SessionValue.SESSION_REQUESTURL, requestUrl);
			}
			if(method.equalsIgnoreCase("POST")){
				logger.info("obj == null 且 请求方式为POST请求，返回json到页面");
				returnJsonObject.put("isLogin", "no");
				response.getOutputStream().print(returnJsonObject.toString());
				return ;
			}else{
				logger.info("obj == null 且 请求方式为GET请求，直接重定向到登录页面");
				response.sendRedirect("/login/login.shtml");
				return ;
			}
		}else{
			chain.doFilter(request, res);
		}
		
		
		
		
		
		
		/*if (RequestHelper.getSessionCmfUserId(request) == null) {

			request.getSession(true).setAttribute(SessionValue.SESSION_REQUESTURL, requestUrl);// 未登录状态，将url参数存放到session中
			logger.info("【cmfUserId == null && 不是访问首页      将用户请求页面放入session中】");
			
			logger.info("过滤器中：request.getSession(true).getAttribute('cmfUserId') == null");

			if(method.equalsIgnoreCase("GET")){
				logger.info("cmfUserId == null 且 请求方式为GET请求，直接重定向到登录页面");
				response.sendRedirect("/login/login.shtml");
				return ;
			}else if(method.equalsIgnoreCase("POST")){
				
				logger.info("cmfUserId == null 且 请求方式为POST请求，返回json到页面");
				JSONObject returnJsonObject = new JSONObject();
				returnJsonObject.put("isLogin", "no");
				response.getOutputStream().print(returnJsonObject.toString());
				return ;
			}else{
				logger.info("【cmfUserId == null && 不是访问首页       的时候进入了过滤器】");
				response.sendRedirect("/login/login.shtml");
			}
			
			return ;
		} else {
			String cmfUserId = request.getSession(true).getAttribute("cmfUserId").toString();
			logger.info("过滤器中：cmfUserId = " + cmfUserId);
			if ("".equals(cmfUserId)) {

				request.getSession(true).setAttribute(SessionValue.SESSION_REQUESTURL, requestUrl);// 未登录状态，将url参数存放到session中
				logger.info("【cmfUserId == ''为空值 && 不是访问首页      将用户请求页面放入session中】");
				
				if(method.equalsIgnoreCase("GET")){
					logger.info("cmfUserId == '' 且 请求方式为GET，直接重定向到登录页面");
					response.sendRedirect("/login/login.shtml");
					return ;
				}else if(method.equalsIgnoreCase("POST")){
					
					logger.info("cmfUserId == '' 且 请求方式为POST请求，返回json到页面");
					JSONObject returnJsonObject = new JSONObject();
					returnJsonObject.put("isLogin", "no");
					response.getOutputStream().print(returnJsonObject.toString());
					return ;
				}else{
					logger.info("cmfUserId == '' && 不是访问首页    的时候进入了过滤器】");
					response.sendRedirect("/login/login.shtml");
				}

				return ;
			} else {
				chain.doFilter(request, res);
			}
		}*/

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		logger.debug(" init");

	}

}
