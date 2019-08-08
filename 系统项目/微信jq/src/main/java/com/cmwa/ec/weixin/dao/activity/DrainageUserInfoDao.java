package com.cmwa.ec.weixin.dao.activity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.DrainageUserInfoDto;

public interface DrainageUserInfoDao {

	/**
	 * 查询所有引流用户信息
	 * @return
	 */
	public List<DrainageUserInfoDto> queryAllUserInfo();
	
	
	/**
	 * 查询所有引流用户信息
	 * @return
	 */
	public List<DrainageUserInfoDto> queryUserInfoByMobile();
	
	/**
	 * 根据userid 查询引流用户信息
	 * @param userId
	 * @return
	 */
	public DrainageUserInfoDto queryUserInfoByUserId(@Param("userId")String userId);

	/**
	 * 插入信息
	 * @param dto
	 * @return
	 */
	public int insertUserInfo(DrainageUserInfoDto dto) ;

	/**
	 * 根据身份证和姓名查询引流用户信息
	 * @param idcard
	 * @param ownerName
	 * @return
	 */
	public DrainageUserInfoDto queryUserInfoByIdCardAndOwnerName(@Param("idCard")String idCard,
			@Param("ownerName")String ownerName);

	
	
	/**
	 * 根据身份证+姓名+appId回写回调信息
	 * @param idCard
	 * @param ownerName
	 * @param appId
	 * @param string
	 * @param msg
	 * @return
	 */
	public int updateRedirectInfo(@Param("idCard")String idCard, @Param("ownerName")String ownerName,
			@Param("appId")String appId, @Param("status")String status, @Param("msg")String msg,@Param("updatedUser") String updatedUser);
	
	
	/**
	 * 根据身份证查询引流用户信息
	 * @param idCard
	 * @return
	 */
	public DrainageUserInfoDto queryUserInfoByIdCard(@Param("idCard")String idCard);
}
