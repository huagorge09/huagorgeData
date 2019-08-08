package com.cmwa.ecc.business.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmwa.ec.base.util.SpringUtil;

public class AjaxSendRedirectFilter implements Filter{
	
	public static final String AJAX_REQUEST_HEADER = "X-Requested-With";
	
	private String sendRedirectUrl;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		sendRedirectUrl = SpringUtil.getProperty("loginUrl");
		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		String reqUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		if(!"/".equals(contextPath) && !reqUri.startsWith("/")){
			contextPath = contextPath+"/";
		}
//		Employee emp = SessionUtils.getEmployee(request);
		String type = request.getHeader(AJAX_REQUEST_HEADER);// XMLHttpRequest
		if(type != null && "XMLHttpRequest".equalsIgnoreCase(type) && reqUri.indexOf("loginView.action") > 0){
			response.setHeader("ajaxRedirectFlag", "302");
			response.setHeader("sendRedirectUrl", reqUri);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
