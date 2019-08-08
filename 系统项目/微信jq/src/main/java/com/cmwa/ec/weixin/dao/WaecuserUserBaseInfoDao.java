package com.cmwa.ec.weixin.dao;

import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;

/**
 * waecuiser.userbaseinfo dao
 * @author ex-hezk
 *
 */
public interface WaecuserUserBaseInfoDao {

	/**
	 * 根据注册号码查询用户信息
	 * @param mobile
	 * @return
	 */
	public UserBaseInfoDto queryUserBaseInfoByMobile(String mobile);
	
}
