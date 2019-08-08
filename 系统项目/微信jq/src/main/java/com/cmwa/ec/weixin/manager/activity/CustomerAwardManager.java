package com.cmwa.ec.weixin.manager.activity;

import java.util.Map;

import net.sf.json.JSONObject;

/**
 * 微信活动-业务层-用户中奖纪录
 * 
 * @author ex-chenhq
 * 
 */
public interface CustomerAwardManager {

    /**
     * 获取碎片规则
     * 
     * @param openid
     * @param activityId
     * @return
     */
    public JSONObject obtainFragmentLogic(String json);

    /**
     * 活动抽奖逻辑
     * 
     * @param json
     * @return
     */
    public JSONObject luckDrawLogic(String json);

    /**
     * 检查用户是否可以签到
     * 
     * @param openId
     * @return
     */
    public JSONObject checkUserCanSignIn(String openId);

    /**
     * 用户签到
     * 
     * @param openId
     * @param activityId
     * @param awardFrom
     * @return
     */
    public Object userSignIn(String openId, String activityId, String toOpenId);

    /**
     * 查询用户中奖信息
     * 
     * @param userId
     * @param activityId
     * @param awardType
     * @param awardName
     * @return
     */
    public String queryUserAward(String openId, String activityId, String awardName, String awardType);

    /**
     * 查询用户碎片列表信息
     * 
     * @param json
     * @return
     */
    public JSONObject shardListLogic(String json);

    /**
     * 保存用户兑奖信息
     * 
     * @param string
     * @return
     */
    public JSONObject saveUserAwardInfo(String jsonParam);

    /**
     * 
     * 查询用户兑奖信息
     * 
     * @param string
     * @return
     */
    public JSONObject queryUserAwardInfo(String jsonParam);

    /**
     * 
     * 更新用户兑奖信息
     * 
     * @param string
     * @return
     */
    public JSONObject updateUserAwardInfo(String jsonParam);

    /**
     * 查询系统通知
     * 
     * @return
     */
    public JSONObject querySystemNotice();

    /**
     * 查询用户通知
     * 
     * @param openId
     * @return
     */
    public JSONObject queryUserNotice(String openId, String activityId);

    /**
     * 查询分享信息
     * 
     * @param activityId
     * @param openId
     * @return
     */
    public JSONObject queryShareInfo(String activityId, String openId);

    /**
     * 查询用户的助力信息
     * 
     * @param activityId
     * @param openId
     * @param pageIndex
     * @return
     */
    public JSONObject getSystemMessList(String json);

    /**
     * 查询用户的助力信息
     * 
     * @param activityId
     * @param openId
     * @return
     */
    public JSONObject getShareInfo(String activityId, String openId);

    /**
     * 查询所有用户中奖信息
     * 
     * @param openId
     * @param endIdx
     * @param beginIdx
     * @return
     */
    public JSONObject queryAllUserAwardInfo(String activityId, int beginIdx, int endIdx);

    /**
     * 查询奖品库存数量
     * 
     * @param str1
     * @param str2
     * @param str3
     * @return
     */
    public JSONObject queryAwardNum(String str1, String str2, String str3);

    /**
     * Logic method of public lottery
     * 
     * @param openId
     * @param activityId
     * @return
     */
    public JSONObject lotteryLogic(String openId, String activityId);

    /**
     * 粤马会签到领取洗牙券
     * 
     * @param paramMap
     * @return
     */
    public JSONObject gdBmwGetCard(Map<String, Object> paramMap);

    /**
     * 查询中奖名单
     * 
     * @param activityId
     * @param awardFrom
     * @return
     */
    public JSONObject queryAllWinningCount(String activityId, String awardFrom);

    /**
     * 查询奖品列表
     * 
     * @param activityId
     * @return
     */
    public JSONObject queryAwardList(String activityId);

    /**
     * 大转盘抽奖方法
     * 
     * @param paramMap
     * @return
     */
    public JSONObject turnDraw(String activityId, String openId, String telePhone, String name);

    /**
     * 判断用户是否关注
     * 
     * @param openId
     * @return
     */
    public boolean getSubscribeState(String openId);

    /**
     * 保存用户预约信息
     * 
     * @param activityId
     * @param openId
     * @param mobile
     * @param name
     * @param state
     * @return
     */
    public JSONObject saveEnrolmentUser(String activityId, String openId, String mobile, String name, String state);

    /**
     * 发送短信
     * 
     * @param mobile
     * @param msgType
     * @return
     */
    public JSONObject sendMsg(String mobile, String msgType);

    /**
     * 
     * @Title: saveUserLotteryNum
     * @Description: 保存用户抽奖号码
     * @param activityId
     * @param openId
     * @param mobile
     * @param name
     * @param state
     * @return JSONObject
     * @throws
     */
    public JSONObject saveUserLotteryNum(String activityId, String openId, String mobile, String name);
}
