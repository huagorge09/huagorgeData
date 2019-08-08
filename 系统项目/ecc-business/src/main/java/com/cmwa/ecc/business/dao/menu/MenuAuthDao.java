package com.cmwa.ecc.business.dao.menu;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.MenuAuthVo;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;

@MybatisDao
public interface MenuAuthDao extends BaseDao<MenuAuthVo> {

	/**
	 * 根据联合主键， 删除岗位和菜单的关联关系
	 * 
	 * @param orgId
	 * @param titleId
	 */
	void deleteOrmemberMenuByUnionId(@Param("orgId") String orgId, @Param("titleId") String titleId);

	/**
	 * 根据员工ID， 删除员工和菜单的关联关系
	 * 
	 * @param empId
	 */
	void deleteEmpMenuByEmpId(@Param("empId") String empId);

	/**
	 * 批量保存岗位和菜单的关联关系
	 * 
	 * @param list
	 */
	void insertBatchOrmemberMenu(List<MenuAuthVo> list);

	/**
	 * 批量保存员工和菜单的关联关系
	 * 
	 * @param list
	 */
	void insertBatchEmpMenu(List<MenuAuthVo> list);

	/**
	 * 查询默认选中的岗位菜单
	 * 
	 * @param orgId
	 * @param titleId
	 * @return
	 */
	List<String> queryOrmemberDefaultSelectedMenu(@Param("orgId") String orgId, @Param("titleId") String titleId);

	/**
	 * 查询默认选中的人员菜单
	 * 
	 * @param empId
	 * @return
	 */
	List<String> queryEmpDefaultSelectedMenu(@Param("empId") String empId);

	/**
	 * 根据请求路径 查询用户关联权限是否存在，查询权限数量
	 * 
	 * @param empId
	 * @param requestPath
	 * @return
	 */
	Integer queryEmpMenuCountByPath(@Param("empId") String empId,@Param("requestPath") String requestPath);

	/**
	 * 根据请求路径 查询用户岗位关联的权限是否存在，查询权限数量
	 * @param empId
	 * @param requestPath
	 * @return
	 */
	Integer queryOrmemberMenuCountByPath(@Param("empId") String empId,@Param("requestPath") String requestPath);

}
