package com.cmwa.ecc.business.dao.resource;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.ResourceRoleRelationsVo;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * @TODO	资源-角色
 * @author ex-liuy
 * @createDate 2017年6月6日
 */
@MybatisDao
public interface ResourceRoleRelationsDao extends BaseDao<ResourceRoleRelationsVo>{
	/**
	 * 批量新增资源角色关系数据
	 * @author ex-liuy
	 * @param param
	 */
	public void batchInsert(SearchParam param);
	/**
	 * @TODO	根据参数查询数据
	 * @author ex-liuy
	 * @return
	 */
	public List<ResourceRoleRelationsVo> queryAllResRoleList(SearchParam sp);
	
	/**
	 * 批量新增资源角色关系数据
	 * @author ex-liuy
	 * @param param
	 */
	public void batchInsertByResIds(SearchParam param);
	
	/**
	 * @TODO	删除资源角色关系
	 * @author ex-liuy
	 */
	public void deleteResourceRoleRelationsByResOrRoleId(@Param("resId")String resId,@Param("roleId")String roleId);
}
