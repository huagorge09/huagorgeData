package com.cmwa.ecc.business.service.roleinfo;

import java.util.List;

import com.cmwa.ecc.business.commonVo.RoleVo;
import com.cmwa.ecc.business.exception.ValidateFailedException;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface RoleInfoService {
	/**
	 * @TODO	查询所有角色 分页
	 * @author ex-liuy
	 * @param sp
	 * @return
	 */
	public Page<RoleVo> queryRoleInfoListPage(SearchParam sp);
	
	/**
	 * @TODO	根据参数查询角色 无分页
	 * @author ex-liuy
	 * @param sp
	 * @return
	 */
	public List<RoleVo> queryRoleInfoListByParam(SearchParam sp);
	
	/**
	 * @TODO	根据id查询角色
	 * @author ex-liuy
	 * @param roleId
	 * @return
	 */
	public RoleVo queryRoleInfoById(String roleId);
	/**
	 * @TODO	根据参数查询角色
	 * @author ex-liuy
	 * @param sp
	 * @return
	 */
	public RoleVo queryRoleInfoByParam(RoleVo roleVo);
	/**
	 * @TODO	传入角色并新增至表
	 * @author ex-liuy
	 * @param roleVo
	 */
	public void insertRoleInfoByRole(RoleVo roleVo) throws ValidateFailedException;
	
	/**
	 * @TODO	传入角色并修改至表
	 * @author ex-liuy
	 * @param roleVo
	 */
	public void updateRoleInfoByRole(RoleVo roleVo) throws ValidateFailedException;
	
	/**
	 * @TODO	传入角色并新增至表
	 * 会根据传入的角色名不存在则新增
	 * @author ex-liuy
	 * @param roleVo
	 */
	public void insertRoleInfoByLoginEmpAndRoleIsNotExist();
	
	/**
	 * @TODO	查找角色
	 * @author ex-liuy
	 * @param role
	 * @return
	 */
	public List<RoleVo> queryRole(RoleVo role);
	
	/**
	 * @TODO	查找角色 验证用
	 * @author ex-liuy
	 * @param role
	 * @return
	 */
	public List<RoleVo> matchRoleInfoIsNotExist(RoleVo role);
	
	/**
	 * @TODO	根据角色id查询所有分享角色
	 * @author ex-liuy
	 * @param role
	 * @return
	 */
	public List<RoleVo> queryShareRoleListByRoleId(RoleVo role);
	/**
	 * @TODO	递归查询传入角色所有的分享角色
	 * @author ex-liuy
	 * @param role
	 * @return
	 */
	public List<RoleVo> queryShareRoleListByRoleIdAndRecursion(RoleVo role);
	
	
}
