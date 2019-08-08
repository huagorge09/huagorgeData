package com.cmwa.ec.weixin.manager.business;

import java.util.List;

import com.cmwa.ec.query.facade.dto.user.UserCommonDto;
import com.cmwa.ec.query.facade.dto.user.UserInfoDto;
import com.cmwa.ec.query.facade.dto.user.UserInfoexDto;
import com.cmwa.ec.query.facade.dto.user.UserOrderDto;

public interface SynchronousDataManager {
	/**
	 * 同步微信扩展信息表
	 * @param dto
	 * @return
	 */
	public int synchUserInfoExtend(UserCommonDto dto, String channel);
	
	/**
	 * 同步订单信息表
	 * @param dto
	 * @return
	 */
	public int synchUserOrderInfo(UserOrderDto dto);
	
	/**
	 * 同步微信用户信息
	 * @return
	 */
	public int synchOldUseInfo(UserInfoDto dto);
	
	public List<UserInfoexDto> queryInfoex(String cmfUserId);
	
}
