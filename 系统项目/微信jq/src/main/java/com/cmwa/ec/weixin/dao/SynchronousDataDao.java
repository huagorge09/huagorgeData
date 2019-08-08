package com.cmwa.ec.weixin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.query.facade.dto.user.UserCommonDto;
import com.cmwa.ec.query.facade.dto.user.UserInfoDto;
import com.cmwa.ec.query.facade.dto.user.UserInfoExtendDto;
import com.cmwa.ec.query.facade.dto.user.UserInfoexDto;
import com.cmwa.ec.query.facade.dto.user.UserOrderDto;
import com.cmwa.ec.query.facade.dto.user.WxUserInfoDto;

public interface SynchronousDataDao {

	/**
	 * 同步微信用户扩展信息
	 * @param dto
	 */
	public void synchUserInfoExtend(@Param("dto")UserInfoExtendDto dto);
	
	/**
	 * 更新微信用户扩展信息
	 * @param dto
	 */
	public void updateUserInfoExtend(@Param("dto")UserInfoExtendDto dto);
	
	/**
	 * 同步用户订单信息
	 * @param dto
	 * @return
	 */
	public int synchUserOrderInfo(@Param("dto")UserOrderDto dto);
	
	/**
	 * 更新用户订单信息
	 * @param dto
	 * @return
	 */
	public int updateUserOrderInfo(@Param("dto")UserOrderDto dto);
	
	/**
	 * 同步微信用户信息
	 * @return
	 */
	public int synchWxUserInfo(@Param("dto")WxUserInfoDto dto);
	
	/**
	 * 更新微信用户信息
	 * @param dto
	 * @return
	 */
	public int updateWxUserInfo(@Param("dto")WxUserInfoDto dto);
	
	/**
	 * 通过cmfUserId 查询 CMWA_WX_USER_INFO_EXTEND 数据是否存在
	 * @param dto
	 * @return
	 */
	public int queryInfoexCount(String cmfUserId);
	
	/**
	 * 通过serialNo 查询 CMWA_WX_USER_ORDER 数据是否存在
	 * @param serialNo
	 * @return
	 */
	public int queryUserOrderCount(String serialNo);
	
	/**
	 * 通过openId 查询 CMWA_WX_USER_INFO 数据是否存在
	 * @return
	 */
	public int queryWxInfoCount(String openId);
	
	/**
	 * 以userInfoex为主表
	 * @param dto
	 * @return
	 */
	public List<UserInfoexDto> queryInfoex(String cmfUserId);
	
	/**
	 *全量捞出微信用户扩展信息表的cmfUserId和OpenId
	 * @return
	 */
	public List<UserInfoExtendDto> getUserAndOpenId(@Param("dto")UserOrderDto dto);
	
	/**
	 * 查询旧表用户信息
	 * @return
	 */
	public List<UserInfoDto> queryWeiXinUserInfo(@Param("dto")UserInfoDto dto);
	
}
