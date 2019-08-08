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

public class AjaxPostEncodingFilter implements Filter {
	
	 /** ajax post请求的默认content type */  
    public static final String AJAX_POST_CONTENT_TYPE_DEFAULT = "application/x-www-form-urlencoded";  
      
    /** ajax post请求的编码，W3C标准为UTF-8 */  
    public static final String AJAX_POST_ENCODE = "UTF-8";  
      
    private String ajaxPostContentType;  
    
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
		 	HttpServletRequest request = (HttpServletRequest)req;  
		 	HttpServletResponse resp = (HttpServletResponse)res;
//	        String requestedWith = request.getHeader("x-requested-with");
//	        String contentType = request.getContentType();  
	          
	        // 表明是一个Ajax的post请求，并且不是使用隐藏的iframe实现的
//	        if("XMLHttpRequest".equalsIgnoreCase(requestedWith) && null != contentType  
//	                && contentType.toLowerCase().startsWith(ajaxPostContentType.toLowerCase())){
	              
	            request.setCharacterEncoding(AJAX_POST_ENCODE);  
	              
	            /* 
	             * 调用getParameter方法可以让在此之后调用的setCharacterEncoding方法失效， 
	             * 参数可以为任何值（can be any thing） 
	             * 可以防止WEB框架或用户程序再次将其设置回GBK等其它编码， 
	             * 因为有些情况下，用户的要求的页面编码不是UTF-8而是GBK或其它的 
	             */  
	            request.getParameter("can be anything");
//	        }  
            resp.setHeader("Access-Control-Allow-Origin","*");
            resp.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS");
	        resp.setHeader("Access-Control-Allow-Headers", "Content-Type,XFILENAME,XFILECATEGORY,XFILESIZE");
	        chain.doFilter(request, resp);  

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		 ajaxPostContentType = arg0.getInitParameter("ajaxPostContentType");  
         
        if(null == ajaxPostContentType){  
            ajaxPostContentType = AJAX_POST_CONTENT_TYPE_DEFAULT;  
        }  

	}

}
