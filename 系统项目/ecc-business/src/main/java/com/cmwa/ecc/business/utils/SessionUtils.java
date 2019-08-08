package com.cmwa.ecc.business.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import com.cmwa.ecc.business.commonVo.Employee;

public class SessionUtils {

	public static final String SESSION_EMPLOYEE = "_session_employee_";
	
	/**
	 * token
	 */
	public static final String SESSION_TOKEN = "_SESSION_TOKEN_";
	
	/**
	 * token登陆成功码
	 */
	public static final String TOKEN_CODE_SUCCESS = "T00000";
	
	/**
	 * 用户登陆方式:3 token
	 */
	public static final String USER_LOGIN_TYPE_3 = "3";

	
	/**
	 * 验证码
	 */
	public static final String SESSION_VERIFYCODE = "session_verifycode";
	
	//是否能连通CAS session值标识
	public static final String SESSION_CASAVAILBLE_ID ="_CAS_LOGIN_URL_AVAILBLE_";

	private static final ThreadLocal<Employee> Employees = new ThreadLocal<Employee>();

	/**
	 * 从当前线程中取登录用户信息
	 * 
	 * @return
	 */
	public static Employee getEmployee() {

		Employee employee = Employees.get();
		if (employee == null)
			employee = new Employee();
		return employee;
	}

	public static void setEmployee(Employee employee) {
		Employees.set(employee);
	}

	
	/**
	 * 从session中取登录用户信息
	 * 
	 * @return
	 */
	public static Employee getEmployee(HttpServletRequest request) {
		Employee employee = Employees.get();
	
		if (employee == null){
			employee = (Employee)request.getSession().getAttribute(SESSION_EMPLOYEE);
		}
		if (employee == null){
			employee = new Employee();
		}
		return employee;
	}

	public static void setEmployee(HttpServletRequest request,Employee employee) {
		Employees.set(employee);
		request.getSession().setAttribute(SESSION_EMPLOYEE, employee);
	}
	
	/**
	 * 设置CAS连通标识
	 * @param request
	 */
	public static void setCasAvailble(HttpServletRequest request,Boolean flag) {
		request.getSession().setAttribute(SESSION_CASAVAILBLE_ID, flag);
	}
	
	/**
	 * 获取CAS连通标识
	 * @param request
	 * @return
	 */
	public static Boolean getCasAvailble(HttpServletRequest request) {
		Boolean resultFlag = null;
		try{
			HttpSession session = request.getSession();
			Boolean sessionFlag = (Boolean)session.getAttribute(SESSION_CASAVAILBLE_ID);
			if(null != sessionFlag){
				resultFlag = new Boolean(sessionFlag) ;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return resultFlag;
	}
}
