package com.cmwa.ecc.business.service.impl.roleinfo;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.commonVo.RoleEmployeeRelationsVo;
import com.cmwa.ecc.business.commonVo.RoleVo;
import com.cmwa.ecc.business.dao.roleinfo.RoleEmployeeRelationsDao;
import com.cmwa.ecc.business.service.roleinfo.RoleEmployeeRelationsService;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 角色员工关系业务实现类
 * @author ex-liuy
 * @createDate 2017年6月2日
 */
@Service
public class RoleEmployeeRelationsServiceImpl implements RoleEmployeeRelationsService {
	
	@Resource
	private RoleEmployeeRelationsDao roleEmployeeRelationsDao;
	
	/**
	 * 批量新增角色员工关系数据
	 * @author ex-weicb
	 * @createDate 2016年4月28日 下午2:34:23
	 * @param param
	 */
	public void batchInsert(SearchParam param){
		roleEmployeeRelationsDao.batchInsert(param);
	}
	
	/**
	 * 根据角色id删除角色员工关系数据
	 * @author ex-weicb
	 * @createDate 2016年4月29日 上午10:40:43
	 * @param roleId
	 */
	public void deleteByRoleId(String roleId){
		roleEmployeeRelationsDao.deleteByRoleId(roleId);
	}

	/**
	 * 根据员工id查询角色员工关系数据
	 * @author ex-qiuzw
	 * @createDate 2016年10月13日 上午14:18:28
	 * @param empId
	 */
	public List<RoleEmployeeRelationsVo> queryRelationsByEmpId(String empId) {
		return roleEmployeeRelationsDao.queryRelationsByEmpId(empId);
	}

	@Override
	public List<RoleEmployeeRelationsVo> queryShareRoleByEmpAllRole(String empId) {
		return roleEmployeeRelationsDao.queryShareRoleByEmpAllRole(empId);
	}

	@Override
	public String queryRoleEmpRelByRoleId(List<RoleVo> roleList) {
		return roleEmployeeRelationsDao.queryRoleEmpRelByRoleId(roleList);
	}

	@Override
	public List<RoleEmployeeRelationsVo> queryRoleEmpRelByParam(SearchParam sp) {
		return roleEmployeeRelationsDao.queryRoleEmpRelByParam(sp);
	}
	
	@Override
	public RoleEmployeeRelationsVo queryRoleRelationsByEmpId(String empId) {
		return roleEmployeeRelationsDao.queryRoleRelationsByEmpId(empId);
	}
	@Override
	public List<RoleEmployeeRelationsVo> queryDeptAllRoleByEmpId(String orgId) {
		return roleEmployeeRelationsDao.queryDeptAllRoleByEmpId(orgId);
	}
}
