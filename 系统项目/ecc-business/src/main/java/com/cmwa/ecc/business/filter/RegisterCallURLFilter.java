package com.cmwa.ecc.business.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.cmwa.ecc.business.utils.SessionValue;

public class RegisterCallURLFilter implements Filter {
	
	private String excludeURL;
    
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		excludeURL =filterConfig.getInitParameter("excludeURL");
		if(null == excludeURL){
			excludeURL ="service/index/indexTransfer.do";
		}
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request; 
		HttpSession session = req.getSession();
		String requestedWith = req.getHeader("x-requested-with");
		String requestUrl = req.getRequestURL().toString();
		//不是ajax请求 且不包含 排除的地址
		if(null ==requestedWith && !requestUrl.contains(excludeURL)){
			session.setAttribute(SessionValue.SESSION_REQUESTURL,requestUrl);
		}
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
