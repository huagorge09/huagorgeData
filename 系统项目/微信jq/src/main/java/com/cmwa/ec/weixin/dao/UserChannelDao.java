package com.cmwa.ec.weixin.dao;

import org.apache.ibatis.annotations.Param;

public interface UserChannelDao {
	
	public int queryUserChannelCountById(@Param("openId")String openId);
	
	public int insertUserChannel(@Param("openId")String openId,@Param("channel")String channel,@Param("eventType")String eventType);
	
	public int updateUserChannelById(@Param("openId")String openId,@Param("channel")String channel,@Param("eventType")String eventType);
}
