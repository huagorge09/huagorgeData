package com.cmwa.ec.weixin.dao.activity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.AwardInfoDto;

/**
 * 微信活动-奖品信息
 * @author ex-chenhq
 *
 */
public interface AwardInfoDao {

	/**
	 * 查询活动中所有奖品信息列表
	 * @param channel
	 * @return
	 */
	public List<AwardInfoDto> queryAwardList(@Param("map")Map map);
	
	/**
	 * 根据奖品Id查询奖品信息
	 * @param prizeId
	 * @return
	 */
	public AwardInfoDto getAwardDto(@Param("awardId")String awardId);
	
	/**
	 * 根据奖品Id修改奖品数量
	 * @param awardId
	 * @return
	 */
	public int updateAwardNum(@Param("awardId")String awardId);

}
