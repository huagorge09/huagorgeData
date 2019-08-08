package com.cmwa.ecc.business.utils.cached;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.cmwa.ecc.business.commonVo.MenuVo;
import com.cmwa.ecc.business.service.menu.MenuService;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class MenuCached {
	@Resource
	private MenuService menuService;
	private List<MenuVo> menuList = new CopyOnWriteArrayList<MenuVo>();

	/**
	 * 
	 */
	public void loadAllMenuCached() {
		menuList.clear();
		List<MenuVo> list = menuService.queryAllMenu();
		menuList.addAll(list);
	}

	/**
	 * 找到匹配的菜单URL
	 * 
	 * @param url
	 * @return
	 */
	public String findMatchUrlMenu(HttpServletRequest request) {
		if (menuList.size() == 0) {
			loadAllMenuCached();
		}
		String url = request.getRequestURI();
		if (null != url && !("").equals(url)) {
			if(url.indexOf("server") >= 0){
				url = url.substring(url.indexOf("server"),url.length());
			}else{
				url = url.substring(url.indexOf("service"),url.length());
			}
		}
		String lastFoundId = null;
		MenuVo menu = null;
		for (int i = 0; i < menuList.size(); i++) {
			menu = menuList.get(i);
			if (menu.getUrl() != null && menu.getUrl().startsWith(url)) {
				String qstring = request.getQueryString();
				if (StringUtils.isNotBlank(qstring) && isNotStrictEqual(url, qstring, menu)) {
					lastFoundId = menu.getMenuId();
					continue;
				}
				return menu.getMenuId();
			}
		}
		if (lastFoundId != null) {
			return lastFoundId;
		}
		return null;
	}

	private boolean isNotStrictEqual(String url, String qstr, MenuVo menu) {
		url += "?" + qstr;
		if (!menu.getUrl().equals(url)) {
			return true;
		}
		return false;
	}
}
