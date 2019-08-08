package com.cmwa.ec.weixin.dao.activity;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.ActivityAwardStoreDto;

/**
 * 
 *  cmwa_wx_activity_store表对应dao
 */
public interface ActivityAwardStoreDao {

	
	/**
	 * 根据条件查询存在cmwa_wx_act_award_store的奖品
	 * @param param 参数
	 * @param random 是否根据参数随机取记录
	 * @return
	 */
	List<ActivityAwardStoreDto> listAwardOnStore(@Param("param") ActivityAwardStoreDto param,@Param("random")boolean random);

	/**
	 * 更新奖品记录信息
	 * @param activityAwardStoreDto
	 * @param writeBack 是否是回写操作
	 * @return
	 */
	int updateAwardOnStore(@Param("param") ActivityAwardStoreDto activityAwardStoreDto,@Param("writeBack") boolean writeBack);

	/**
	 * 查询已经发放的奖品数量
	 * @param activityId
	 * @param awardId
	 * @return
	 */
	int countUsedAward(@Param("activityId") String activityId, @Param("awardId")String awardId);
	
}
