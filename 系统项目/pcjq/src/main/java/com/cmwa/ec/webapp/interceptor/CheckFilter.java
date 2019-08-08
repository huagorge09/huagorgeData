package com.cmwa.ec.webapp.interceptor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.webapp.util.StringUtils;


/**
 * 过滤器，配置到web.xml，所有页面均需先经过此filter检查URL所带参数的合法性
 *
 *
 */
public class CheckFilter implements Filter {
    protected String invalidCharSet;
    private String errorPage;

    /**
     * filter初始化
     *
     * @author songyb
     * @param FilterConfig
     *
     */
    public void init(FilterConfig filterConfig) {
        System.out.println("CheckFilter初始化开始...！");
        
        this.setInvalidCharSet(SpringUtil.getProperty("invalid_char_set"));
        this.setErrorPage(SpringUtil.getProperty("error_page"));
        
        System.out.println(this.getInvalidCharSet());
        System.out.println(this.getErrorPage());
        
        if (invalidCharSet == null) {
            invalidCharSet = "\'_;_$_%_\"_+_\\_<iframe_<script_script>_iframe>_alert(_+or+_+-_-+_null%2B_%3Ciframe_%3Cscript_script%3E_iframe%3E";
        }
        if (errorPage == null) {
            errorPage = "/login/login.shtml"; // 默认情况使用根目录的error.jsp
        }
        
        System.out.println(this.getInvalidCharSet());
        System.out.println(this.getErrorPage());
        System.out.println("CheckFilter初始化成功！");
    }

    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        HttpServletRequest hreq = (HttpServletRequest) req;
        HttpServletResponse resp = (HttpServletResponse) res;
        java.util.Enumeration e = hreq.getParameterNames();
        String paramString = "";
        
        resp.setHeader("Host", "https://direct.cmwachina.com");

        if (e == null) {
            chain.doFilter(req, res);
        }

        // 对所有参数进行循环
        while (e.hasMoreElements()) {
            // 得到参数名
            String name = (String) e.nextElement();
            // 得到这个参数的所有值
            String[] value = hreq.getParameterValues(name);
            if (value != null) {
                for (int i = 0; i < value.length; i++) {
                    paramString = paramString + value[i];
                }
            } else {
                paramString = errorPage;
            }
        }
        // System.out.println(paramString);
        // 非空并且包含非法字符
        if (paramString != null && !"".equals(paramString)
            && StringUtils.checkStr(paramString, invalidCharSet)) {
            System.out.println("参数值存在注入!请求参数串为：" + paramString + " 请求用户为："
                               + req.getRemoteHost());
            resp.sendRedirect(errorPage);
        } else {
            chain.doFilter(req, res);
        }
    }

    public void destroy() {
        System.out.println("CheckFilter成功摧毁！");
    }

	public String getInvalidCharSet() {
		return invalidCharSet;
	}

	public void setInvalidCharSet(String invalidCharSet) {
		this.invalidCharSet = invalidCharSet;
	}

	public String getErrorPage() {
		return errorPage;
	}

	public void setErrorPage(String errorPage) {
		this.errorPage = errorPage;
	}
    
    

}
