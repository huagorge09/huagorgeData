package com.cmwa.ecc.business.utils;

import java.util.List;
import java.util.Map;

import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.MenuVo;

public class Client {

	/**
	 * 当前用户
	 */
	private Employee employee;

	/**
	 * 当前用户全部的菜单列表（包括操作按钮和菜单）
	 */
	private Map<String, MenuVo> menus;
	/**
	 * 当前用户左边菜单栏中的分级的菜单列表
	 */
	private Map<Integer, List<MenuVo>> menuMap;
	/**
	 * 当前用户全部操作按钮菜单
	 */
	private Map<String, MenuVo> buttonMenus;

	
	/**
	 * 清空数据
	 */
	public void clearAll() {
		if (menus != null) {
			menus.clear();
		}
		if (menuMap != null) {
			menuMap.clear();
		}
		if (buttonMenus != null) {
			buttonMenus.clear();
		}
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Map<String, MenuVo> getMenus() {
		return menus;
	}

	public void setMenus(Map<String, MenuVo> menus) {
		this.menus = menus;
	}

	public Map<Integer, List<MenuVo>> getMenuMap() {
		return menuMap;
	}

	public void setMenuMap(Map<Integer, List<MenuVo>> menuMap) {
		this.menuMap = menuMap;
	}

	public Map<String, MenuVo> getButtonMenus() {
		return buttonMenus;
	}

	public void setButtonMenus(Map<String, MenuVo> buttonMenus) {
		this.buttonMenus = buttonMenus;
	}

}
