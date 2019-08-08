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
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.utils.Client;
import com.cmwa.ecc.business.utils.ClientManager;
import com.cmwa.ecc.business.utils.SessionUtils;
import com.cmwa.ecc.business.utils.SysConf;

/**
 * 单点登录拦截器
 * @author ex-chenbq
 *
 */
public class CookieSSOFilter implements Filter {

	private String loginUrl;
	
	private static final Log logger = LogFactory.getLog(CookieSSOFilter.class);

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		loginUrl = SysConf.get("loginUrl");
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		// restful 接口暂不需要验证
		String path = request.getServletPath();
		boolean isExcludedPage = path.startsWith("/restful/");
		// boolean isExcludedPage = true;

		if (isExcludedPage) {
			chain.doFilter(request, response);
			return;
		}
		
		HttpSession session = request.getSession();
		Employee employee = (Employee) session.getAttribute(SessionUtils.SESSION_EMPLOYEE);
		if (employee != null && !StringUtils.isEmpty(employee.getID())) {
			chain.doFilter(request, response);
			return;
		}else{
			employee = new Employee();
		}
		
		/*String empId = request.getParameter("empId");
		String empName =request.getParameter("empName");
		
		if(!StringUtils.isEmpty(empId) && !StringUtils.isEmpty(empName)){
			employee.setID(empId);
			employee.setLoginName(empName);
			employee.setName(empName);
		}
		*/
		String contextPath = "/";
		if(!"/".equalsIgnoreCase(request.getContextPath())){
			contextPath = request.getContextPath()+"/";
		}
		
		logger.error("===========开始登录校验(session无用户, sessionId=" + session.getId() + ") login start===========");
		if (StringUtils.isEmpty(employee.getID())) {
			logger.error("===========登录校验:cookie 无 CmwaEmpid，跳转登录页, sessionId:" + session.getId() + "  ===========");
			//employee.setID("2171029"); //郭一楠
			//employee.setID("2179131");//黄娜
			response.sendRedirect(contextPath+loginUrl);
			return;
		}
		logger.error("===========登录校验：cookie 获取成功 success, sessionId:" + session.getId() + " ===========");
//		session.setAttribute(SessionUtils.SESSION_EMPLOYEE, employee);
		SessionUtils.setEmployee(request,employee);
		// 客户端菜单管理的缓存类
		logger.error("===========登录校验：初始化客户端菜单管理的缓存类Client开始 client init start, sessionId:" + session.getId() + " ===========");
		Client client = new Client();
		client.setEmployee(employee);
		ClientManager.getInstance().addClinet(session.getId(), client);
		logger.error("===========登录校验：初始化客户端菜单管理的缓存类Client完成 client init end, sessionId:" + session.getId() + " ===========");
		chain.doFilter(request, response);
	}

	@Override
	public void destroy() {

	}
	
}
