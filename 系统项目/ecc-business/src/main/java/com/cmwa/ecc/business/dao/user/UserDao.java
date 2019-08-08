package com.cmwa.ecc.business.dao.user;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.user.User;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
@MybatisDao
public interface UserDao {
	

	/**
	 * 获取单个用户信息
	 * @param user
	 */
	public void getUserById(User user);
	
	
	/**
	 * 用户权限校验
	 * @param opid
	 * @param permissionId
	 * @return
	 */
	public int userHavePermission(@Param("userId")String userId,@Param("permissId")String permissId);
}
