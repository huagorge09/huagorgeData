package com.cmwa.ecc.business.service.menu;

import java.util.List;

import com.cmwa.ecc.business.commonVo.OrganizationVo;


public interface OrganizationService {

	/**
	 * 根据父ID查询组织子列表
	 * @param orgId
	 * @return
	 */
	List<OrganizationVo> queryOrganizationListByParentId(String orgId);

	/**
	 *
	 */
	List<OrganizationVo> queryStationListByOrgId(String orgId);

	/**
	 * 查询用户的组织
	 * @param empId
	 * @return
	 */
	OrganizationVo queryOrgByEmpId(String empId);

}
