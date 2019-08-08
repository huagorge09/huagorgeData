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
import javax.servlet.http.HttpSession;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.utils.SessionUtils;

public class GlobalFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
	    HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		// 设置应用路径
		request.setAttribute("path", request.getContextPath());
		// 从Session中获取登录用户信息，并将用户信息放到当前线程中
		HttpSession session = request.getSession();
		Employee employee = (Employee)session.getAttribute(SessionUtils.SESSION_EMPLOYEE);
		SessionUtils.setEmployee(employee);
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}

}
