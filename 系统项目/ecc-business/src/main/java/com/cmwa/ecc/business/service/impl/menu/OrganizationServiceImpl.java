package com.cmwa.ecc.business.service.impl.menu;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.OrganizationVo;
import com.cmwa.ecc.business.dao.menu.OrganizationDao;
import com.cmwa.ecc.business.service.menu.OrganizationService;

@Service
public class OrganizationServiceImpl implements OrganizationService {
	@Resource
	private OrganizationDao organizationDao;

	@Override
	public List<OrganizationVo> queryOrganizationListByParentId(String orgId) {
		if (StringUtils.isBlank(orgId)) {
			orgId = "0";
		}
		return organizationDao.queryOrganizationListByParentId(orgId);
	}

	@Override
	public List<OrganizationVo> queryStationListByOrgId(String orgId) {
		return organizationDao.queryStationListByOrgId(orgId);
	}

	@Override
	public OrganizationVo queryOrgByEmpId(String empId) {
		List<OrganizationVo> orgList = organizationDao.queryOrgByEmpId(empId);
		if (orgList != null && !orgList.isEmpty()) {
			return orgList.get(0);
		}
		return null;
	}

}
