package com.cmwa.ecc.business.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.MenuVo;
import com.cmwa.ecc.business.service.menu.MenuService;
import com.cmwa.ecc.business.utils.Client;
import com.cmwa.ecc.business.utils.ClientManager;
import com.cmwa.ecc.business.utils.NumberComparator;
import com.cmwa.ecc.business.utils.SessionUtils;
import com.cmwa.ecc.business.utils.SessionValue;


@Controller("indexController")
@RequestMapping(value="/service/index")
public class IndexController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(IndexController.class);
	
	private String BUTTON_MENU_TYPE = "1";
	@Autowired
	private MenuService menuService;
	
	/**
	 * 跳转至系统首页页面
	 * @return
	 */
	@RequestMapping(value = "/indexView.xhtml")
	public String goPage(ModelMap model) {
		Employee user = SessionUtils.getEmployee();
		logger.info("获取session："+user);
		model.addAttribute("currentEmpId", user.getID());
		model.addAttribute("menuMap", buildMenuMap(user));
		model.addAttribute("userName", StringUtils.isEmpty(user.getName())? "":user.getName());
		return "jsp/index";
	};
	
	/**
	 * 域登陆 - 跳转至系统首页中转
	 * @return
	 */
	@RequestMapping(value = "/indexTransfer.do")
	public String goIndexTransferPage(ServletRequest req , ModelMap model) {
		HttpServletRequest request = (HttpServletRequest)req;
		HttpSession session = request.getSession();
		model.put(SessionValue.SESSION_REQUESTURL, session.getAttribute(SessionValue.SESSION_REQUESTURL));
		
		Employee user = SessionUtils.getEmployee();
		if(null != user){
			model.addAttribute("currentEmpId", user.getID());
			model.addAttribute("menuMap", buildMenuMap(user));
		}
		return "jsp/indexTransfer";
	};
	
	/**
	 * 提示无权限页面
	 * 
	 */
	@RequestMapping("/nonPrivilegedView.xhtml")
	public String nonPrivilegedView() {
		return "jsp/hint/nonPrivileged";
	}
	
	/**
	 * 提示无权限页面
	 * 
	 */
	@RequestMapping("/errorPage.action")
	public String nonPrivilegedPage() {
		return "jsp/hint/nonPrivileged";
	}

	/**
	 * 
	 * @param flag
	 * @param model
	 * @return
	 */
	@RequestMapping("/welcomeView.xhtml")
	public String welcomeView(@RequestParam(value = "load", defaultValue = "false") Boolean load,@RequestParam(value = "redirectUrl", defaultValue = "") String redirectUrl, ModelMap model) {
		Employee user = SessionUtils.getEmployee();
		//初始化加载菜单权限
		if (load) {
			buildMenuMap(user);
		} 
		if(StringUtils.isNotBlank(redirectUrl)){
			return "redirect:"+redirectUrl;
		}
		return "jsp/wasp/defaultPage";
	}
	
	/**
	 * 构建当前用户左边菜单分级列表
	 * 
	 * @param user
	 * @param ctxPath
	 * @return
	 */
	private Map<Integer, List<MenuVo>> buildMenuMap(Employee user) {
		//Client client = ClientManager.getInstance().getClient("2017050800001");
		Client client = ClientManager.getInstance().getClient();
		if (null != client && (client.getMenuMap() == null || client.getMenuMap().size() == 0)) {
			Map<Integer, List<MenuVo>> menuMap = new HashMap<Integer, List<MenuVo>>();
			Map<String, MenuVo> loginMenuMap = getUserMenu(user);
			// 操作按钮菜单
			Map<String, MenuVo> buttonMenuMap = new HashMap<String, MenuVo>();
			if (loginMenuMap.size() > 0) {
				Collection<MenuVo> allMenus = loginMenuMap.values();
				for (MenuVo menu : allMenus) {
					if (BUTTON_MENU_TYPE.equals(menu.getType())) {
						buttonMenuMap.put(menu.getCode(), menu);
						// 如果为表单或者弹出 不显示在系统菜单里面
						continue;
					}
					if (!menuMap.containsKey(menu.getLevel() + 0)) {
						menuMap.put(menu.getLevel() + 0, new ArrayList<MenuVo>());
					}
					// 构建完整的URL
					if (isVaildURL(menu.getUrl())) {
						menu.setUrl(menu.getUrl());
					}
					menuMap.get(menu.getLevel() + 0).add(menu);
				}
				// 菜单栏排序
				Collection<List<MenuVo>> c = menuMap.values();
				for (List<MenuVo> list : c) {
					Collections.sort(list, new NumberComparator());
				}
			}
			// 加入操作按钮菜单缓存列表
			client.setButtonMenus(buttonMenuMap);
			// 加入菜单缓存列表
			client.setMenuMap(menuMap);
		}
		if(null == client){
			client = new Client();
		}
		return client.getMenuMap();
	}

	/**
	 * 检查是否有效的URL
	 * @param url
	 * @return
	 */
	private boolean isVaildURL(String url) {
		if (StringUtils.isNotBlank(url) && !"0".equals(url)) {
			return true;
		}
		return false;
	}

	/**
	 * 获取当前用户拥有全部菜单， 如果 员工-权限 表，没有员工A的数据，则A的权限是其岗位的权限（即 岗位-权限 表的数据）； 如果 员工-权限 表
	 * 有A的数据，则A的权限 为 员工-权限 表 中的数据
	 * 
	 * @param user
	 * @return
	 */
	private Map<String, MenuVo> getUserMenu(Employee user) {
		Client client = ClientManager.getInstance().getClient();
		if (client.getMenus() == null || client.getMenus().size() == 0) {
			Map<String, MenuVo> loginMenuMap = new HashMap<String, MenuVo>();
			Map<String, MenuVo> allMenu = menuService.queryAllRealMenu();
			// 查询员工-权限 表中，当前员工A的菜单数据
			List<MenuVo> empMenuList = menuService.queryMenuListByEmp(user.getID());
			if (empMenuList.isEmpty()) {
				// 没有当前员工A的数据，则A的权限是其岗位的权限
				List<MenuVo> ormemberMenuList = menuService.queryMenuListByOrmember(user.getID());
				// 构建当前员工A的菜单数据
				for (MenuVo menuVo : ormemberMenuList) {
					buildAssociatedMenu(allMenu, loginMenuMap, menuVo);
				}
			} else {
				// 构建当前员工A的菜单数据
				for (MenuVo menuVo : empMenuList) {
					buildAssociatedMenu(allMenu, loginMenuMap, menuVo);
				}
			}
			// 加入菜单缓存列表
			client.setMenus(loginMenuMap);
		}
		return client.getMenus();
	}

	/**
	 * 构建一个完整的树，从叶子节点开始，得到这个叶子节点所有的父节点数据
	 * 
	 * @param allMenu
	 * @param loginMenuList
	 * @param menuVo
	 */
	public void buildAssociatedMenu(Map<String, MenuVo> allMenu, Map<String, MenuVo> loginMenuList, MenuVo menuVo) {
		associatedMenu(allMenu, loginMenuList, menuVo);
		loginMenuList.put(menuVo.getMenuId(), menuVo);
	}

	/**
	 * 递归取得树上的父节点的数据
	 * 
	 * @param allMenu
	 * @param loginMenuList
	 * @param menuVo
	 */
	public void associatedMenu(Map<String, MenuVo> allMenu, Map<String, MenuVo> loginMenuList, MenuVo menuVo) {
		if (allMenu.containsKey(menuVo.getParentId())) {
			MenuVo menu = allMenu.get(menuVo.getParentId());
			loginMenuList.put(menu.getMenuId(), menu);
			associatedMenu(allMenu, loginMenuList, menu);
		} else {
			return;
		}
	}

}
