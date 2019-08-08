package com.cmwa.ecc.business.dao.roleinfo;

import java.util.List;

import com.cmwa.ecc.business.commonVo.RoleEmployeeRelationsVo;
import com.cmwa.ecc.business.commonVo.RoleVo;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 角色员工关系dao层接口
 * @author ex-liuy
 * @createDate 2017年6月2日
 */
@MybatisDao
public interface RoleEmployeeRelationsDao extends BaseDao<RoleEmployeeRelationsVo> {

	/**
	 * 批量新增角色员工关系数据
	 * @author ex-liuy
	 * @param param
	 */
	public void batchInsert(SearchParam param);

	/**
	 * 根据角色id删除角色员工关系数据
	 * @author ex-liuy
	 * @param roleId
	 */
	public void deleteByRoleId(String roleId);
	
	/**
	 * 根据员工id查询角色员工关系数据
	 * @author ex-liuy
	 * @param empId
	 * @return
	 */
	public List<RoleEmployeeRelationsVo> queryRelationsByEmpId(String empId);
	
	/**
	 * 根据员工id查询员工所有角色的分享角色
	 * @author ex-liuy
	 * @param empId
	 * @return
	 */
	public List<RoleEmployeeRelationsVo> queryShareRoleByEmpAllRole(String empId);
	
	/**
	 * @TODO	根据传入角色集合 查询角色对应员工ids
	 * @author ex-liuy
	 * @param roleId
	 * @return
	 */
	public String queryRoleEmpRelByRoleId(List<RoleVo> roleList);
	
	/**
	 * 根据参数查询
	 * @author ex-liuy
	 * @param empId
	 * @return
	 */
	public List<RoleEmployeeRelationsVo> queryRoleEmpRelByParam(SearchParam sp);
	/**
	 * 根据参数查询
	 * @author 
	 * @param 
	 * @return
	 */
	public RoleEmployeeRelationsVo queryRoleRelationsByEmpId(String empId);
	/**
	 * 根据参数查询
	 * @author 
	 * @param 
	 * @return
	 */
	public List<RoleEmployeeRelationsVo> queryDeptAllRoleByEmpId(String orgId);
}
