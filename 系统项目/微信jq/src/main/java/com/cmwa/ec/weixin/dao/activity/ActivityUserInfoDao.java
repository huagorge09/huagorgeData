package com.cmwa.ec.weixin.dao.activity;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ec.weixin.dto.ActivityUserInfoDto;

/**
 * 
 * 微信活动-用户兑奖信息
 * 
 * @author ex-hezk
 * 
 */
public interface ActivityUserInfoDao {

    /**
     * 保存用户信息
     * 
     * @param dto
     * @return
     */
    public int saveUserInfo(ActivityUserInfoDto dto);

    /**
     * 查询用户信息
     * 
     * @param openId
     * @param activityId
     * @return
     */
    public List<ActivityUserInfoDto> queryUserInfo(@Param("openId") String openId, @Param("activityId") String activityId);

    /**
     * 
     * @Title: queryUserInfoByCondition
     * @Description: 按条件查询用户信息
     * @param activityId
     * @param mobile
     * @param itemKey
     * @return List<ActivityUserInfoDto>
     */
    public List<ActivityUserInfoDto> queryUserInfoByCondition(@Param("activityId") String activityId, @Param("mobile") String mobile, @Param("itemKey") int itemKey);

    /**
     * 修改用户兑奖信息
     * 
     * @param dto
     * @return
     */
    public int updateUserInfo(ActivityUserInfoDto dto);

    /**
     * 在cmwa_wx_user_info查询用户昵称
     * 
     * @param openid
     * @return
     */
    public List<Map<String, Object>> queryUserNameOnCmwaWxUserInfo(List<String> openid);

    /**
     * 根据手机号查询客户是否已预约
     * 
     * @param activityId
     * @param mobile
     * @return
     */
    public int queryUserInfoByMobileCount(@Param("activityId") String activityId, @Param("mobile") String mobile);

    /**
     * 根据手机号查询对应的itemKey的条数
     * 
     * @param activityId
     * @param mobile
     * @param itemKey
     * @return
     */
    public int queryUserInfoByMobileAndItemKeyCount(@Param("activityId") String activityId, @Param("mobile") String mobile, @Param("itemKey") int itemKey);

}
