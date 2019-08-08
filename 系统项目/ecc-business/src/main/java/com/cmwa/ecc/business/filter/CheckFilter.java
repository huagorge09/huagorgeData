package com.cmwa.ecc.business.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 * 过滤器，配置到web.xml，所有页面均需先经过此filter检查URL所带参数的合法性
 *
 *
 */
public class CheckFilter implements Filter {
    protected String invalidCharSet;

    /**
     * filter初始化
     *
     * @author songyb
     * @param FilterConfig
     *
     */
    public void init(FilterConfig filterConfig) {}

    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
    	chain.doFilter(new XssHttpServletRequestWrapper((HttpServletRequest) request), response); 
    }

    public void destroy() {}
}
