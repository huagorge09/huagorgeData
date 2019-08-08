package com.cmwa.ec.weixin.dao;


import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.CmwaWxUserInfoDto;
import com.cmwa.ec.weixin.dto.UserInfoDto;


/**
 * @描述: 微信用户处理
 * @版权: Copyright (c) 2013
 * @公司:  
 * @作者: niedc
 * @版本: 1.0
 * @创建日期:2014-11-17
 */
public interface UserInfoDao
{
	
	/** 
	 *更新一条用户基本信息
	 *@return
	 */
	public int updateUserInfo(@Param("dto")UserInfoDto dto);
	
	/***
	 * 查询用户信息
	 * @param openid
	 * @param status
	 * @return
	 */
	public UserInfoDto queryByOpenIdAndStatus(@Param("openid")String openid, @Param("status")String status);
	
	public UserInfoDto queryByOpenId(@Param("openid")String openid);
	
	/***
	 * 更新用户关注状态
	 * @param dto
	 */
	public void updateUserInfoStatus(@Param("openid")String  openid,@Param("status")String  status);
	
	/**
	 * 插入一条userinfo
	 * @param userId
	 * @return
	 */
	public int createUserInfo(@Param("dto") UserInfoDto dto) throws Exception;
	

	/** 
	 *更新一条用户基本信息
	 *@return
	 */
	public int updateUserCode(@Param("openId")String openId,@Param("md5Code")String md5Code);
	
	
	public int queryValidUserCode(@Param("openId")String openId, @Param("md5Code")String md5Code); 
	
	public String queryUserCode(@Param("openId")String openId);
	
	/**
	 * 根据用户状态获取用户信息  如果状态为空默认查询已关注（1）的微信用户
	 * <!-- 未关注：0 -->
	 * <!-- 已关注：1 -->
	 * <!-- 取消关注：2 -->
	 * <!-- 黑名单：3 -->
	 */
	public List<UserInfoDto> queryUserInfoByStatus(@Param("status")String status);
	
	/**
	 * 通过openId查询该用户在新用户表内是否关注
	 * @param openId
	 * @return
	 */
	public CmwaWxUserInfoDto queryIsSubscribeByOpenIdOnCmwaWxUserInfo(@Param("openId")String openId); 

	
	public String queryCustserIdByOpenid(@Param("openid")String openId); 
	
	public int updateCustserIdByOpenid(@Param("openid") String openid,@Param("custserId") String custserId);
//	public int updateUserInfoToUnsubscribe(@Param("openId") String openId);

//	public int updateUserInfoTosubscribe(@Param("openId") String openId);

}
