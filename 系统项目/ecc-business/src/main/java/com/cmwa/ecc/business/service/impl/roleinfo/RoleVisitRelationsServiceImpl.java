package com.cmwa.ecc.business.service.impl.roleinfo;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.RoleVisitRelationsVo;
import com.cmwa.ecc.business.dao.roleinfo.RoleVisitRelationsDao;
import com.cmwa.ecc.business.service.roleinfo.RoleVisitRelationsService;
import com.cmwa.ecc.business.utils.SearchParam;
@Service
public class RoleVisitRelationsServiceImpl implements RoleVisitRelationsService {
	
	private static final Log logger = LogFactory.getLog(RoleVisitRelationsServiceImpl.class);
	
	@Autowired
	private RoleVisitRelationsDao roleVisitRelationsDao;
	
	@Override
	public void batchInsert(SearchParam param) {
		roleVisitRelationsDao.batchInsert(param);
	}

	@Override
	public void deleteByRoleId(String roleId, String visId) {
		roleVisitRelationsDao.deleteByRoleId(roleId, visId);
	}

	@Override
	public List<RoleVisitRelationsVo> queryRelationsByVisId(String visId) {
		return roleVisitRelationsDao.queryRelationsByVisId(visId);
	}

}
