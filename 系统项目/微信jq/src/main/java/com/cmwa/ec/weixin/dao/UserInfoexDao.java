package com.cmwa.ec.weixin.dao;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.query.facade.dto.user.UserInfoExtendDto;
import com.cmwa.ec.weixin.dto.UserInfoexDto;

public interface UserInfoexDao {
	
	/***
	 * 根据OPENid 查询用户扩展信息
	 * @param openId
	 * @return
	 */
	public UserInfoexDto queryByOpenId(@Param("openId")String openId,@Param("stat")String stat);
	
	
	/***
	 * 根据cmfUserId 查询用户扩展信息
	 * @param cmfUserId
	 * @return
	 */
	public UserInfoexDto queryByCmfUserId(@Param("cmfUserId")String openId,@Param("stat")String stat);
	
	
	/***
	 * 新增用户信息扩展
	 * @param dto
	 * @return
	 */
	public void insert(@Param("dto")UserInfoexDto dto);
	
	/***
	 * 更新用户信息
	 * @param dto
	 */
	public void updatecmfUserid(@Param("openid")String  openid,@Param("cmfuserid")String  cmfuserid,@Param("stat")String  stat);
	
	/**
	 * 更新用户客户号和基金账号
	 * @param dto
	 */
	public void updatecmfCustNoAndEcCustNo(@Param("dto")UserInfoexDto dto);
	
	
	/**
	 * 根据cmfUserId查询已关注的和绑定状态正常的openId
	 * @param cmfUserId
	 * @return
	 */
	public String queryOpenIdByCmfUserId(@Param("cmfUserId")String cmfUserId);
	
	/**
	 * 通过openId来判断用户是否做过适当性
	 * @param dto
	 * @return
	 */
	public UserInfoExtendDto queryRiskLevel(@Param("dto")UserInfoExtendDto dto);
	
}
