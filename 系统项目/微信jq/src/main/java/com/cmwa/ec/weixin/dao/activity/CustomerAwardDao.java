package com.cmwa.ec.weixin.dao.activity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.CustomerAwardDto;
import com.cmwa.ec.weixin.dto.QueryUserAwardDto;
import com.cmwa.ec.weixin.dto.SystemNoticeDto;
import com.cmwa.ec.weixin.dto.ShareConfigDto;
import com.cmwa.ec.weixin.dto.ShareInfoDto;

/**
 * 微信活动-用户中奖信息
 * @author ex-chenhq
 *
 */
public interface CustomerAwardDao {
	
	/**
	 * 查询用户中奖信息列表
	 * @param map
	 * @return
	 */
	public List<CustomerAwardDto> queryCustomerAwardList(@Param("map")Map map);
	
	/**
	 * 新增用户中奖记录
	 * @param dto
	 * @return
	 */
	public int createCustomerAward(@Param("dto") CustomerAwardDto dto);
	
	/**
	 * 修改用户奖品状态
	 * @param cusAwardId
	 * @param getState
	 * @return
	 */
	public int updateCustomerAward(@Param("cusAwardId")String cusAwardId,@Param("getState")String getState);

	/**
	 * 查询用户当天签到记录
	 * @param openId
	 * @return
	 */
	public List<CustomerAwardDto> checkUserCanSignIn(@Param("openId")String openId);
	
	/**
	 * 查询用户中奖纪录
	 * @param param
	 * @return
	 */
	public List<QueryUserAwardDto> queryUserAwardRecordOnFatherDay(@Param("param")Map<String, Object> param);
	
	/**
	 * 查询该奖品当天发放总数
	 * @param map
	 * @return
	 */
	public int querySameDayCount(@Param("map")Map map);

	
	/**
	 * 根据奖品id查询奖品信息
	 * @param openId 
	 * @param CusAwardId
	 * @return
	 */
	public CustomerAwardDto queryUserAwardByCusAwardId(@Param("cusAwardId")String cusAwardId, @Param("openId")String openId);
	/**
	 * 根据活动id查询分享信息
	 * @param activityId 
	 * @param openId 
	 * @return
	 */
	public ShareConfigDto queryShareInfo(@Param("activityId")String activityId, @Param("channel")String channel);
	
	/**
	 * 根据活动id查询分享信息
	 * @param activityId 
	 * @param openId 
	 * @return
	 */
	public ShareInfoDto getShareInfo(@Param("activityId")String activityId, @Param("openId")String openId);
	
	/**
	 * 创建用户分享记录
	 * @param shareInfoDto 
	 * @return
	 */
	public int createShareRecored(@Param("dto")ShareInfoDto dto);

	/**
	 * 查询系统通知
	 * @return
	 */
	public List<SystemNoticeDto> querySystemNotice();

	/**
	 * 查询多个用户中奖记录
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryUserAwardRecordsOnFatherDay(@Param("param")Map<String, Object> map);
	
	/**
	 * 查询好友中奖记录
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> queryAllUserAwardInfo(@Param("param")Map<String, Object> map);
	
	
	/**
	 * 查询好友中奖记录总数
	 * @param map
	 * @return
	 */
	public int queryAllUserAwardInfoCount(@Param("param")Map<String, Object> map);
	
	/**
	 * 查询所有中奖纪录总数
	 * @param map
	 * @return
	 */
	public int queryAllWinningCount(@Param("param")Map<String, Object> map);
	
	/**
	 * 查询手机号码中奖记录
	 * @param map
	 * @return
	 */
	public List<CustomerAwardDto> queryUserAwardByTelephone(@Param("param")Map<String, Object> map);
}
