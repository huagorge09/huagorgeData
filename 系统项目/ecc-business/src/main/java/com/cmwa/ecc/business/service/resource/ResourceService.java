package com.cmwa.ecc.business.service.resource;

import java.util.List;

import com.cmwa.ecc.business.commonVo.ResourceVo;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * @TODO	用户资源操作接口
 * @author ex-liuy
 * @createDate 2017年6月6日
 */
public interface ResourceService {
	/**
	 * 插入资源
	 * @TODO	
	 * @author ex-liuy
	 * @param resourceVo
	 */
	public void saveResource(ResourceVo resourceVo);
	/**
	 * 插入当前登录人资源
	 * @TODO	
	 * @author ex-liuy
	 */
	public void insertResourceByLoginEmpAndResIsNotExist();
	
	/**
	 * 根据传入角色 设置 资源
	 * @TODO	
	 * @author ex-liuy
	 */
	public void insertResourceByRoleAndResIsNotExist(SearchParam sp);
	
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
	 * @TODO	定时任务设置员工-资源表数据
	 * @author ex-liuy
	 */
	public void quartzResetResourceEmpDataByRoleList();
	/**
	 * 
	 * @TODO	删除资源关系
	 * resId or roleId  
	 * @author ex-liuy
	 * @param resId
	 * @param roleId
	 */
	public void deleteResourceByResOrRole(String resId,String roleId);
}
