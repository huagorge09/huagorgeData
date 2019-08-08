package com.cmwa.ecc.business.service.impl.roleinfo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.RoleContactsRelationsVo;
import com.cmwa.ecc.business.dao.roleinfo.RoleContactsRelationsDao;
import com.cmwa.ecc.business.service.roleinfo.RoleContactsRelationsService;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 角色联系人关系业务实现类
 * @author ex-liuy
 * @createDate 2017年6月2日
 */
@Service
public class RoleContactsRelationsServiceImpl implements RoleContactsRelationsService {
	
	@Autowired
	private RoleContactsRelationsDao roleContactsRelationsDao;
	
	/**
	 * 批量新增角色联系人关系数据
	 * @author ex-liuy
	 * @createDate 2017年6月2日
	 * @param param
	 */
	public void batchInsert(SearchParam param){
		roleContactsRelationsDao.batchInsert(param);
	}
	
	/**
	 * 根据角色id删除角色联系人关系数据
	 * 传入 角色或者联系人id 即可删除数据
	 * @author ex-liuy
	 * @createDate 2017年6月2日
	 * @param roleId
	 */
	public void deleteByRoleId(String roleId,String conId){
		roleContactsRelationsDao.deleteByRoleId(roleId,conId);
	}

	/**
	 * 根据联系人id查询角色联系人关系数据
	 * @author ex-liuy
	 * @createDate 2017年6月2日
	 * @param conId
	 */
	public List<RoleContactsRelationsVo> queryRelationsByConId(String conId) {
		return roleContactsRelationsDao.queryRelationsByConId(conId);
	}
}
