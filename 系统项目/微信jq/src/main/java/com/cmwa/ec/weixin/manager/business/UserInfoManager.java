package com.cmwa.ec.weixin.manager.business;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmf.weixin.message.dto.req.EventReqMsgDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
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
public interface UserInfoManager {
	  /***
	   * 用户关注微信
	   * @param openid
	   * @return
	   */
	  public boolean syncUserInfo(String openid);
	  
	  /**
	   * 更新用户信息
	   * @param userInfo
	   * @return
	   */
	  public  int updateUserInfo(UserInfoDto userInfo);
	  
	  /***
	   * 查询用户信息
	   * @param openid
	   * @param status
	   * @return
	   */
	  public UserInfoDto queryByOpenIdAndStatus(String openid,String status);
	  
	  public UserInfoDto queryByOpenId(String openid);
	  
	  
	  public void updateUserInfoStatus(String openid,String status);
	  
	  /**
		 * 方法说明：此方法用于用户关注公众号时，执行的操作
		 * 如果用户在pc交易平台注册并鉴权，然后关注我们的公众号
		 * 这个时候，要根据用户类型，修改对应分组
		 * @param openid
		 * @param seqId
		 * @return
		 */
	public String modifyUserGroupByUserType(String openid,String seqId) throws Exception;

	
	public void updateUserCode(String openid,String md5Code);
	
	
	public int queryValidUserCode(String openid,String md5Code);
	
	
	public String queryUserCode(String openid);
	
	/**
	 * 推送template消息或者service消息
	 * @param msg
	 * @param openId
	 * @param msgType
	 */
	public void pushWXMessage(String msg,String openId,String msgType);
	
	
	/**
	 * 插入用户关注渠道
	 * @param openId
	 * @param channel
	 */
	public int insertUserChannel(String openId,String channel,String eventType);
	
	/**
	 * 根据用户状态获取用户信息  如果状态为空默认查询已关注（1）的微信用户
	 * <!-- 未关注：0 -->
	 * <!-- 已关注：1 -->
	 * <!-- 取消关注：2 -->
	 * <!-- 黑名单：3 -->
	 */
	public List<UserInfoDto> queryUserInfoByStatus(@Param("status")String status);
//	/**
//	 * 用户取消关注时更改新用户表状态
//	 * @param openId
//	 * @return
//	 */
//	public int updateUserInfoToUnsubscribe(String openId);
	/**
	 * 用户关注时更新用户表状态
	 * @param openId
	 * @return
	 */
	public void saveOrUpdateCmwaWxUserInfo(EventReqMsgDto eventReqDto,String status);
	
	/**
	 * 查询新表用户户是否关注逻辑
 	 * @param openId
	 */
	public CmwaWxUserInfoDto queryIsSubscribeByOpenIdOnCmwaWxUserInfo(String openId);

	public UserServiceMessage queryUserAndAccoRlaById(String cmfUserId);
	
	
}
