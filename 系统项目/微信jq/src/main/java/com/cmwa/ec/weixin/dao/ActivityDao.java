package com.cmwa.ec.weixin.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.InvectorUserInfoexDto;
import com.cmwa.ec.weixin.dto.InvestorAnswerDto;
import com.cmwa.ec.weixin.dto.InvestorAwardDto;
import com.cmwa.ec.weixin.dto.InvestorForwardDto;
import com.cmwa.ec.weixin.dto.InvestorUserDto;

/**
 * 活动处理
 * @author ex-wangz2
 *
 */
public interface ActivityDao {
	/**
	 * 投资者教育活动
	 * 根据userid查询活动用户记录
	 * @param userId
	 * @return
	 */
	public InvectorUserInfoexDto queryUserInfoByUerId(@Param("userId")String userId);
	
	/**
	 * 投资者教育活动
	 * 创建一个活动用户记录
	 * @param openId
	 * @return
	 */
	public int createActivityUserRecord(@Param("userDto")InvestorUserDto investorUserDto);
	
	/**
	 * 投资者教育活动
	 * 查询用户答题记录
	 * @param openId
	 * @return
	 */
	public InvestorAnswerDto queryUserAnswerRecord(@Param("openId")String openId);
	
	/**
	 * 投资者教育活动
	 * 查询用户当天的抽奖记录
	 * @param openId
	 * @return
	 */
	public InvestorAwardDto queryUserTodayLuckDrawRecord(@Param("openId")String openId);
	
	/**
	 * 投资者教育活动
	 * 创建用户转发记录
	 * @param openId
	 * @return
	 */
	public int createUserForwardRecord(@Param("openId")String openId,@Param("type")String type);
	
	/**
	 * 投资者教育活动
	 * 更新用户转发记录
	 * @param openId
	 * @return
	 */
	public int updateUserForwardRecord(@Param("openId")String openId,@Param("type")String type);
	
	/**
	 * 投资者教育活动
	 * 查询用户转发记录
	 * @param openId
	 * @return
	 */
	public InvestorForwardDto queryUserForwardRecord(@Param("openId")String openId);
	
	/**
	 * 投资者教育活动
	 * 统计当天获奖记录
	 * @return
	 */
	public int countTodayAwardRecord();
	
	/**
	 * 投资者教育活动
	 * 创建用户获奖记录
	 * @param investorAwardDto
	 * @return
	 */
	public int createUserAwardRecord(@Param("openId")String openId,@Param("status")String status);
	
	/**
	 * 投资者教育活动
	 * 创建用户答题记录
	 * @param openId
	 * @param score
	 * @return
	 */
	public int createUserAnswerRecord(@Param("openId")String openId,@Param("score")Integer score);
	
	/**
	 * 投资者教育活动
	 * 更新用户答题记录
	 * @param openId
	 * @param score
	 * @return
	 */
	public int updateUserAnswerRecord(@Param("openId")String openId,@Param("score")Integer score);
	
	/**
	 * 投资者教育活动
	 * 更新用户获奖记录
	 * @param openId
	 * @param flowId
	 * @return
	 */
	public int updateUserAwardRecord(@Param("openId")String openId,@Param("status")String status);
	
	/**
	 * 投资者教育活动
	 * 查询用户的获奖记录
	 * @param openId
	 * @return
	 */
	public InvestorAwardDto queryUserAwardRecord(@Param("openId")String openId);
	
	
	/**
	 * 
	 * 根据openId查询活动用户记录
	 * @param openId
	 * @return
	 */
	public InvectorUserInfoexDto queryUserInfoByOpenId(@Param("openId")String openId);
	
	/**
	 * 更新用户的专属顾问信息
	 * @param map
	 * @return
	 */
	public int updateUserInfoCustser(@Param("map")Map map);

}
