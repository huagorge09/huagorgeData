package com.cmwa.ecc.business.service.impl.roleinfo;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.roleinfo.AuthorityGroupDao;
import com.cmwa.ecc.business.dao.roleinfo.RoleInfoDao;
import com.cmwa.ecc.business.service.roleinfo.AuthorityGroupService;
import com.cmwa.ecc.business.utils.SearchParam;


/**
 * 分享权限组业务实现类
 * @author ex-liuy
 * @createDate 2017年6月2日
 */
@Service
public class AuthorityGroupServiceImpl implements AuthorityGroupService {
	
	@Resource
	private AuthorityGroupDao authorityGroupDao;
	@Autowired
	private RoleInfoDao roleInfoDao;
	
	/**
	 * 批量新增角色分享表数据
	 * @author ex-liuy
	 * @createDate 2017年6月2日
	 * @param param
	 */
	@Override
	public void batchInsert(SearchParam param) {
		authorityGroupDao.batchInsert(param);
	}
	
	/**
	 * 根据角色id得到共享数量
	 * @author ex-liuy
	 * @createDate 2017年6月2日
	 * @param param
	 */
	@Override
	public int queryShareCountByRoleId(String roleId) {
		return authorityGroupDao.queryShareCountByRoleId(roleId);
	}
	
	/**
	 * 根据角色id删除共享权限
	 * @author ex-liuy
	 * @createDate 2017年6月2日
	 * @param param
	 */
	@Override
	public void deleteByRoleId(String roleId) {
		authorityGroupDao.deleteByRoleId(roleId);
	}
	
	
	
}
