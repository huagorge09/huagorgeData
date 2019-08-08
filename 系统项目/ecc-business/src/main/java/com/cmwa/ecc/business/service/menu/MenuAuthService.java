package com.cmwa.ecc.business.service.menu;

import java.util.Set;

import com.cmwa.ecc.business.exception.RepositoryException;

public interface MenuAuthService {

	/**
	 * 保存岗位与菜单关联关系
	 * 
	 * @param stationId
	 * @param menuIds
	 */
	void saveOrmemberMenu(String unionId, String menuIds) throws RepositoryException;

	/**
	 * 保存员工和菜单的关联关系
	 * 
	 * @param empId
	 * @param menuIds
	 */
	void saveEmpMenu(String empId, String menuIds) throws RepositoryException;

	/**
	 * 根据联合主键， 删除岗位和菜单的关联关系
	 * 
	 * @param orgId
	 * @param titleId
	 */
	void deleteOrmemberMenuByUnionId(String orgId, String titleId);

	/**
	 * 根据员工ID， 删除员工和菜单的关联关系
	 * 
	 * @param empId
	 */
	void deleteEmpMenuByEmpId(String empId);

	/**
	 * 查询默认选中的人员菜单
	 * 
	 * @param empId
	 * @return
	 */
	Set<String> queryEmpDefaultSelectedMenu(String empId);

	/**
	 * 查询默认选中的岗位菜单
	 * 
	 * @param unionId
	 * @return
	 */
	Set<String> queryOrmemberDefaultSelectedMenu(String unionId);

	/**
	 * 查询选中的岗位菜单，支持合并原岗位菜单，以逗号（,）分开。
	 * @param srcUnionId
	 * @param destUnionId
	 * @param merge
	 * @return
	 */
	String queryOrmemberDefaultSelectedMenuWithComma(String srcUnionId, String destUnionId, Boolean merge);

	/**
	 * 根据请求路径 查询用户关联权限是否存在，查询权限数量
	 * @param id
	 * @param requestPath
	 * @return
	 */
	Integer queryEmpMenuCountByPath(String empId, String requestPath);

	/**
	 *  根据请求路径 查询用户岗位关联的权限是否存在，查询权限数量
	 * @param id
	 * @param requestPath
	 * @return
	 */
	Integer queryOrmemberMenuCountByPath(String id, String requestPath);

	/**
	 *  查询选中的岗位菜单，支持合并原用户菜单，以逗号（,）分开。
	 * @param srcUnionId
	 * @param empId
	 * @param merge
	 * @return
	 */
	String queryOrmemberDefaultSelectedMergeEmpMenuWithComma(String srcUnionId, String empId, Boolean merge);

	


}
