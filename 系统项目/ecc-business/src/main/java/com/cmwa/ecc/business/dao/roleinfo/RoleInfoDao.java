package com.cmwa.ecc.business.dao.roleinfo;

import java.util.List;

import com.cmwa.ecc.business.commonVo.RoleVo;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface RoleInfoDao extends BaseDao<RoleVo>{
	/**
	 * @TODO	查询所有角色 分页
	 * @author ex-liuy
	 * @param sp
	 * @return
	 */
	public List<RoleVo> queryRoleInfoListPage(SearchParam sp);
	
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
	public void insertRoleInfoByRole(RoleVo roleVo);
	/**
	 * @TODO	查询角色是否存在
	 * @author ex-liuy
	 * @param roleVo
	 * @return
	 */
	public int queryRoleCount(RoleVo roleVo);
	
	/**
	 * @TODO	查找角色
	 * @author ex-liuy
	 * @param role
	 * @return
	 */
	public List<RoleVo> queryRole(RoleVo role);
	/**
	 * @TODO	传入角色并修改至表
	 * @author ex-liuy
	 * @param roleVo
	 */
	public void updateRoleInfoByRole(RoleVo roleVo);
	
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
	 * 分享闭环校验
	 * @TODO	
	 * @author ex-liuy
	 * @param roleId
	 * @return
	 */
	public String getClShareNamesByRoleId(String roleId);
}
