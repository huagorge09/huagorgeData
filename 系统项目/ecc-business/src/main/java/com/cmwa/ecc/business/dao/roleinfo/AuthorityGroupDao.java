package com.cmwa.ecc.business.dao.roleinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.AuthorityGroupVo;
import com.cmwa.ecc.business.mybatis.annotation.BaseDao;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 分享权限组dao层接口
 * @author ex-liuy
 * @createDate 2017年6月2日
 */
@MybatisDao
public interface AuthorityGroupDao extends BaseDao<AuthorityGroupVo> {

	/**
	 * 批量新增角色分享表数据
	 * @author ex-liuy
	 * @param param
	 */
	public void batchInsert(SearchParam param);

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
	
	/**
	 * 根据角色ID查询分享角色ID
	 * @author ex-liuy
	 * @param roleId
	 */
	public List<String> getShareIds(@Param("dataType") String dataType,@Param("roleId") String roleId);

	
}
