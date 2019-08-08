package com.cmwa.ecc.business.service.roleinfo;

import com.cmwa.ecc.business.exception.ValidateFailedException;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 分享权限组业务接口
 * @author ex-liuy
 * @createDate 2017年6月2日
 */
public interface AuthorityGroupService {

	/**
	 * 批量新增角色分享表数据
	 * @author ex-liuy
	 * @param param
	 */
	
	public void batchInsert(SearchParam param) ;

	/**
	 * 根据角色id得到共享数量
	 * @author ex-liuy
	 * @param roleId
	 * @return
	 */
	public int queryShareCountByRoleId(String roleId);

	/**
	 * 根据角色id删除共享权限
	 * @author ex-liuy
	 * @param roleId
	 */
	public void deleteByRoleId(String roleId);

	
}
