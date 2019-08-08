package com.cmwa.ecc.business.dao.resource;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.ResourceVo;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
/**
 * 用户资源信息 数据层
 * @TODO	
 * @author ex-liuy
 * @createDate 2017年6月6日
 */
@MybatisDao
public interface ResourceDao extends BaseDao<ResourceVo> {
	
	/**
	 * @TODO	查询资源
	 * @author ex-liuy
	 * @param resourceVo
	 * @return
	 */
	public ResourceVo queryResourceInfoByParam(ResourceVo resourceVo);
	
	/**
	 * 保存资源信息
	 * @TODO	
	 * @author ex-liuy
	 * @param resourceVo
	 * @return
	 */
	public void saveResource(ResourceVo resourceVo);
	
	/**
	 * 根据角色id 查询 待分配资源列表
	 * @TODO	资源列表 待选区
	 * @author ex-liuy
	 * @param roleId
	 * @return
	 */
	public List<ResourceVo> queryNotAuthResListByRoleEmps(String roleId);
	
	/**
	 * 根据角色id 查询 已分配资源列表
	 * @TODO	资源列表 已选区
	 * @author ex-liuy
	 * @param roleId
	 * @return
	 */
	public List<ResourceVo> queryResourceListByRoleEmps(String roleId);
	
	/**
	 * 
	 * @TODO	删除资源关系
	 * resId or roleId  
	 * @author ex-liuy
	 * @param resId
	 * @param roleId
	 */
	public void deleteResourceByResOrRole(@Param("resId")String resId,@Param("roleId")String roleId);
	/**
	 * 查询所有资源信息
	 * @TODO	
	 * @author ex-liuy
	 * @return
	 */
	public List<ResourceVo> queryAllResourceList();
}
