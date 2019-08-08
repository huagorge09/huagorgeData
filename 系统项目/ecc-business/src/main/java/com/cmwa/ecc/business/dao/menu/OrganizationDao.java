package com.cmwa.ecc.business.dao.menu;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.OrganizationVo;
import com.cmwa.ecc.business.commonVo.RoleVo;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;


@MybatisDao
public interface OrganizationDao extends BaseDao<RoleVo> {

	/**
	 * 根据父ID查询组织子列表
	 * @param orgId
	 * @return
	 */
	List<OrganizationVo> queryOrganizationListByParentId(@Param("parentId")String parentId);

	
	/**
	 * 根据组织ID查询岗位列表
	 * @param orgId
	 * @return
	 */
	List<OrganizationVo> queryStationListByOrgId(@Param("orgId")String orgId);


	/**
	 * 查询用户的组织
	 * @param empId
	 * @return
	 */
	List<OrganizationVo> queryOrgByEmpId(@Param("empId")String empId);
	
	
}
