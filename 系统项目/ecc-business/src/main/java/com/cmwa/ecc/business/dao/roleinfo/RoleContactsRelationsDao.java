package com.cmwa.ecc.business.dao.roleinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.RoleContactsRelationsVo;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 角色联系人关系dao层接口
 * @author ex-liuy
 * @createDate 2017年6月2日
 */
@MybatisDao
public interface RoleContactsRelationsDao extends BaseDao<RoleContactsRelationsVo> {
	/**
	 * 批量新增角色联系人关系数据
	 * @author ex-liuy
	 * @param param
	 */
	public void batchInsert(SearchParam param);

	/**
	 * 根据角色id删除角色联系人关系数据
	 * @author ex-liuy
	 * @param roleId
	 */
	public void deleteByRoleId(@Param("roleId")String roleId,@Param("conId")String conId);
	
	/**
	 * 根据联系人id查询角色联系人关系数据
	 * @author ex-liuy
	 * @param conId
	 * @return
	 */
	public List<RoleContactsRelationsVo> queryRelationsByConId(String conId);
}
