package com.cmwa.ecc.business.interceptor;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.MenuVo;
import com.cmwa.ecc.business.service.menu.MenuAuthService;
import com.cmwa.ecc.business.service.menu.MenuService;
import com.cmwa.ecc.business.utils.Client;
import com.cmwa.ecc.business.utils.ClientManager;
import com.cmwa.ecc.business.utils.SessionUtils;
import com.cmwa.ecc.business.utils.SysConf;
import com.cmwa.ecc.business.utils.cached.MenuCached;

/**
 * 注： 如果 员工-权限 表，没有员工A的数据，则A的权限是其岗位的权限（即 岗位-权限 表的数据）； 如果 员工-权限 表 有A的数据，则A的权限 为
 * 员工-权限 表 中的数据 即员工-权限优先原则
 * 
 * @author pangtf
 * 
 */
public class AuthInterceptor implements HandlerInterceptor {
	private List<String> excludeUrls;
	private String welcomeUrl;
	@Resource
	private MenuCached menuCached;
	@Resource
	private MenuAuthService menuAuthService;
	
	@Autowired
	private MenuService menuService;

	public List<String> getExcludeUrls() {
		return excludeUrls;
	}
	public void setExcludeUrls(List<String> excludeUrls) {
		this.excludeUrls = excludeUrls;
	}
	public void setWelcomeUrl(String welcomeUrl) {
		this.welcomeUrl = welcomeUrl;
	}

	/**
	 * 在调用controller方法前拦截
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestPath = request.getRequestURI();// 用户访问的资源地址
		String token = request.getParameter("token");
		String context = request.getContextPath();
		context = context.equals("/")? context : context + "/";
		if (!"".equals(token) && token != null) {
			Employee user = SessionUtils.getEmployee();
			String visitMenuId = menuCached.findMatchUrlMenu(request);
			System.out.println("得到菜单ID：visitMenuId："+visitMenuId);
			// 查询员工-权限 表中，当前员工A的菜单数据
			List<MenuVo> empMenuList = menuService.queryMenuListByEmp(user.getID());
			List<MenuVo> ormemberMenuList = menuService.queryMenuListByOrmember(user.getID());
			if (!empMenuList.isEmpty()) {
				boolean flag = false;
				for (int i = 0; i < empMenuList.size(); i++) {
					MenuVo menuVo = empMenuList.get(i);
					if (visitMenuId.equals(menuVo.getMenuId())) {
						flag = true;
					}
				}
				if(flag == false){
					String errorPage = SysConf.get("errorPage");
					response.sendRedirect(errorPage);
					return false;
				}else{
					return flag;
				}
			}else if (!ormemberMenuList.isEmpty()) {
				boolean flag = false;
				for (int i = 0; i < ormemberMenuList.size(); i++) {
					MenuVo menuVo = ormemberMenuList.get(i);
					if (visitMenuId.equals(menuVo.getMenuId())) {
						flag = true;
					}
				}
				if(flag == false){
					String errorPage = SysConf.get("errorPage");
					response.sendRedirect(errorPage);
					return false;
				}else{
					return flag;
				}
			}else{
				String errorPage = SysConf.get("errorPage");
				response.sendRedirect(errorPage);
				return false;
			}
		}else{
			if (excludeUrls.contains(requestPath)) {
				// 如果该请求不在拦截范围内，直接返回true
				return true;
			} else {
				int status = checkMenuAuth(request);
				if (status == -1) {
					//状态为-1，说明用户没有权限，或还没登录
					String loginUrl = SysConf.get("nonPrivilegedUrl");
					response.sendRedirect(context+loginUrl);
					return false;
				} else if (status == 0) {
					//状态为0，说明用户已经登录过了，但是缓存中的权限已经被清空了
					String redirect = makeRedirectRUL(request);
					response.sendRedirect(context+redirect);
					return false;
				}
				return true;
			}
		}
	}
	
	/**
	 * 构建重定向链接，
	 * 重定向welcomeUrl,初始化授权，再重定向到目的页面
	 * @param request
	 * @return
	 */
	private String makeRedirectRUL(HttpServletRequest request) {
		String redirect = welcomeUrl;
		/*String qstring = request.getQueryString();
		String currentUrl = request.getRequestURL().toString() + (StringUtils.isNotBlank(qstring) ? "?" + qstring : "");
		if (redirect.indexOf("?") != -1) {
			redirect += "&redirectUrl=" + currentUrl;
		} else {
			redirect += "?redirectUrl=" + currentUrl;
		}*/
		return redirect;
	}

	/**
	 * 判断是否有菜单权限
	 * 
	 * @param request
	 * @return
	 */
	private int checkMenuAuth(HttpServletRequest request) {
		int status = 1;
		// 是否是菜单表中管理的url
		String visitMenuId = menuCached.findMatchUrlMenu(request);

		// 不在管理中的URL默认是有权限访问
		if (visitMenuId == null) {
			return status;
		}
		// 从缓存中获取用户拥有的权限菜单
		Client client = ClientManager.getInstance().getClient();
		if (client != null && client.getMenus() != null) {
			if (client.getMenus().size() != 0) {
				status = client.getMenus().containsKey(visitMenuId) ? 1 : -1;
			} else {
				//状态为0，说明用户已经登录过了，但是缓存中的权限已经被清空了
				status = 0;
			}
		} else {
			status = -1;
		}
		return status;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
	}

}
