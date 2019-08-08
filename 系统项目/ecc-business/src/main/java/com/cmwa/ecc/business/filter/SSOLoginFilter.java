package com.cmwa.ecc.business.filter;

import java.io.IOException;
import java.util.List;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.client.util.AssertionHolder;
import org.jasig.cas.client.validation.Assertion;
import org.springframework.beans.factory.annotation.Autowired;
import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.UserInfoVo;
import com.cmwa.ecc.business.service.userInfo.UserInfoService;
import com.cmwa.ecc.business.utils.Client;
import com.cmwa.ecc.business.utils.ClientManager;
import com.cmwa.ecc.business.utils.SessionUtils;

/**  
*    
*  
* @author ex-liuy 
* @date 2018年5月15日  
*/
public class SSOLoginFilter implements Filter {
    
	private static final Log log = LogFactory.getLog(SSOLoginFilter.class);
	@Autowired
	private UserInfoService userInfoService;
	@Override
	public void destroy() {

		
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest  request = (HttpServletRequest)req;
		HttpServletResponse response =(HttpServletResponse)res;
		HttpSession session = request.getSession(); 
		Employee sessionEmp =  (Employee) session.getAttribute(SessionUtils.SESSION_EMPLOYEE);
		 //从Cas服务器获取登录账户的用户名 
		if(null == sessionEmp || (null != sessionEmp && StringUtil.isEmpty(sessionEmp.getID()))){

			String userName = "";
			try {
				Assertion assertion = AssertionHolder.getAssertion();
				userName = assertion.getPrincipal().getName();
				log.info("获取CAS单点登录用户信息成功"+userName);
			} catch (Exception e) {
				log.error("获取CAS单点登录用户信息失败"+e.getMessage());
				chain.doFilter(request, response); //单点登录异常走正常登录页面
				return;
			}
			
			UserInfoVo userInfo = new UserInfoVo();
			userInfo.setEmpSName(userName);
			if(null == userInfoService){
				userInfoService = SpringUtil.getBean(UserInfoService.class);
			}
			List<UserInfoVo> userList = userInfoService.queryUserInfoByLoginName(userInfo);
			if(userList.size() >= 1){
				userInfo = userList.get(0);
				Employee employee = new Employee();
				employee.setID(userInfo.getEmpID());
				employee.setLoginName(userInfo.getEmpSName());
				employee.setName(userInfo.getEmpName());
//				session.setAttribute(SessionUtils.SESSION_EMPLOYEE, employee);
				//将用户信息放到当前线程中
				SessionUtils.setEmployee(request,employee);
				// 客户端菜单管理的缓存类
				Client client = new Client();
				client.setEmployee(employee);
				ClientManager.getInstance().addClinet(session.getId(), client);		
			}
		}
		chain.doFilter(request, response);  
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		
	}

}
  