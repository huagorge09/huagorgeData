package com.cmwa.ecc.business.dao.menu;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.MenuVo;
import com.cmwa.ecc.business.exception.RepositoryException;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface MenuDao extends BaseDao<MenuVo> {

	/**
	 * 查询菜单列表数据
	 * @param sp
	 * @return
	 */
	List<MenuVo> menuListPage(SearchParam sp);


	/**
	 * 保存菜单
	 * @param menu
	 * @throws RepositoryException
	 */
	void saveMenu(MenuVo menu)throws RepositoryException ;

	/**
	 * 根据ID查询菜单
	 * @param menuId
	 * @return
	 */
	MenuVo queryMenuById(@Param("menuId") String menuId);
	
	MenuVo queryMenuByUrl(@Param("url") String url);
	
	/**
	 *  查询包含三级的菜单列表数据
	 * @return
	 */
	List<MenuVo> queryThreeLevelMenuList();

	/**
	 * 查询全部的菜单
	 * @return
	 */
	List<MenuVo> queryAllMenu();

	/**
	 * 查询最大序列号加一
	 * @return
	 */
	Integer queryNextSequence();

	/**
	 * 根据名称，查询匹配的菜单列表
	 * @param menuNM
	 * @return
	 */
	List<MenuVo> queryMatchMenuList(@Param("menuNM") String menuNM);

	/**
	 * 根据名称查询存在同名菜单的个数
	 * @param menuNM
	 * @param menuId 
	 * @return
	 */
	Integer queryRepeatMenuNMCount(@Param("menuNM")String menuNM,  @Param("menuId")String menuId);

	/**
	 * 
	 * @param buildOracleSQLIn
	 * @return
	 */
	List<MenuVo> queryAllButtonMenuByMenuIds(@Param("inSQL")String inSQL);

	/**
	 * 根据用户ID查询 用户-菜单关联表中的菜单
	 * @param empId
	 * @return
	 */
	List<MenuVo> queryMenuListByEmp(@Param("empId")String empId);

	/**
	 * 根据用户ID查找用户所在的岗位中， 岗位-菜单关联表中的菜单 
	 * @param empId
	 * @return
	 */
	List<MenuVo> queryMenuListByOrmember(@Param("empId")String empId);

	/**
	 * 更新保存菜单数据
	 * @param menu
	 */
	void updateMenu(MenuVo menu);

	/**
	 * 根据岗位信息，查询默认选中的按钮菜单
	 * @param orgId
	 * @param titleId
	 * @return
	 */
	List<MenuVo> queryOrmemberDefaultSelectedButtonMenu(@Param("orgId")String orgId, @Param("titleId")String titleId);

	/**
	 * 根据用户ID，查询默认选中的按钮菜单
	 * @param empId
	 * @return
	 */
	List<MenuVo> queryEmpDefaultSelectedButtonMenu(@Param("empId")String empId);

	/**
	 * 是否有子类菜单
	 * @param parentId
	 * @return
	 */
	Integer querySubMenuCount(@Param("parentId")String parentId);

	/**
	 * 逻辑删除菜单
	 * @param menuId
	 */
	void deleteMenuWithStatus(@Param("menuId")String menuId);

	/**
	 * 根据代码查询按钮菜单
	 * @param code
	 * @return
	 */
	MenuVo queryMenuByCode(@Param("code")String code);

	/**
	 * 检查菜单URL是否存在
	 * @param url
	 * @param menuId 
	 * @return
	 */
	int queryRepeatURL(@Param("url")String url, @Param("menuId")String menuId);



	/**
	 * 根据菜单ID,查询按钮菜单列表
	 * @param buildOracleSQLIn
	 * @return
	 */
	List<MenuVo> queryButtonListByMenuIds(@Param("inSQL")String inSQL);


}
