package com.cmwa.ecc.business.service.menu;

import java.util.List;
import java.util.Map;

import com.cmwa.ecc.business.commonVo.MenuVo;
import com.cmwa.ecc.business.exception.RepositoryException;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface MenuService {

	/**
	 * 列表页面数据
	 * 
	 * @param sp
	 * @return
	 */
	Page<MenuVo> menuListPage(SearchParam sp);

	void saveMenu(MenuVo menu) throws RepositoryException;

	MenuVo queryMenuById(String menuId);
	
	MenuVo queryMenuByUrl(String url);

	/**
	 * 查询包含三级的菜单列表数据
	 * 
	 * @return
	 */
	List<MenuVo> queryThreeLevelMenuList();

	/**
	 * 查询全部的菜单
	 * 
	 * @return
	 */
	List<MenuVo> queryAllMenu();

	/**
	 * 查询最大序列号加一
	 * 
	 * @return
	 */
	Integer queryNextSequence();

	/**
	 * 根据名称，查询匹配的菜单列表
	 * 
	 * @param menuNM
	 * @return
	 */
	List<MenuVo> queryMatchMenuList(String menuNM);

	/**
	 * 检查名称是否在在
	 * 
	 * @param menuNM
	 * @param menuId
	 * @return
	 */
	boolean checkRepeatMenuNM(String menuNM, String menuId);

	/**
	 * 根据选择的菜单ID，查询这些菜单之下的全部按钮级别的菜单列表
	 * 
	 * @param menuIds
	 * @return
	 */
	List<MenuVo> queryAllButtonMenuByMenuIds(String menuIds);

	/**
	 * 
	 * @param menuIds
	 * @return
	 */
	public List<MenuVo> queryButtonListByMenuIds(String menuIds);

	/**
	 * 根据用户ID查询 用户-菜单关联表中的菜单
	 * 
	 * @param empId
	 * @return
	 */
	List<MenuVo> queryMenuListByEmp(String empId);

	/**
	 * 根据用户ID查找用户所在的岗位中， 岗位-菜单关联表中的菜单
	 * 
	 * @param id
	 * @return
	 */
	List<MenuVo> queryMenuListByOrmember(String empId);

	/**
	 * 查询全部菜单，排除按钮级别的菜单
	 * 
	 * @return
	 */
	Map<String, MenuVo> queryAllRealMenu();

	/**
	 * 更新保存菜单数据
	 * 
	 * @param menu
	 */
	void updateMenu(MenuVo menu);

	/**
	 * 根据用户ID，查询默认选中的按钮菜单
	 * 
	 * @param checkResolving
	 * @return
	 */
	List<MenuVo> queryEmpDefaultSelectedButtonMenu(String checkResolving);

	/**
	 * 根据岗位信息，查询默认选中的按钮菜单
	 * 
	 * @param checkResolving
	 * @return
	 */
	List<MenuVo> queryOrmemberDefaultSelectedButtonMenu(String checkResolving);

	/**
	 * 是否有子类菜单
	 * 
	 * @param parentId
	 * @return
	 */
	boolean hasSubMenu(String parentId);

	/**
	 * 逻辑删除菜单
	 * 
	 * @param menuId
	 */
	void deleteMenuWithStatus(String menuId);

	/**
	 * 按钮列表
	 * 
	 * @param sp
	 * @return
	 */
	Page<MenuVo> buttonListPage(SearchParam sp);

	/**
	 * 根据代码查询按钮菜单
	 * 
	 * @param code
	 * @return
	 */
	MenuVo queryMenuByCode(String code);

	/**
	 * 检查菜单URL是否存在
	 * 
	 * @param url
	 * @param menuId
	 * @return
	 */
	boolean checkRepeatURL(String url, String menuId);
	
}
