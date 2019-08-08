package com.cmwa.ec.weixin.dto;

import java.io.Serializable;

/**
 * 微信活动-用户中奖记录信息
 * @author ex-chenhq
 *
 */
public class CustomerAwardDto extends ActivityBaseDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8845887545886148934L;
	/**
	 * 记录ID
	 */
	private String cusAwardId;
	
	/**
	 * 奖品ID
	 */
	private String awardId;
	
	/**
	 * 活动编码
	 */
	private String activityId;
	
	/**
	 * 用户openId
	 */
	private String openId;
	
	/**
	 * 奖品名称
	 */
	private String awardName;
	
	/**
	 * 奖品领取状态(0:未领取 1:已经领取)
	 */
	private String getState;
	
	/**
	 * 用户中奖IP
	 */
	private String customerIp;

	/**
	 * 中奖来源(0:本人签到获取,1:好友助力 2:开箱子奖励)
	 */
	private String awardFrom;
	
	/**
	 * 关联有数量限制的奖品表 cmwa_wx_act_award_store
	 */
	private String storeId; 
	
	private String telePhone;
	
	private String userName;
	
	
	public String getCusAwardId() {
		return cusAwardId;
	}

	public void setCusAwardId(String cusAwardId) {
		this.cusAwardId = cusAwardId;
	}

	public String getAwardId() {
		return awardId;
	}

	public void setAwardId(String awardId) {
		this.awardId = awardId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getAwardName() {
		return awardName;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	public String getGetState() {
		return getState;
	}

	public void setGetState(String getState) {
		this.getState = getState;
	}

	public String getCustomerIp() {
		return customerIp;
	}

	public void setCustomerIp(String customerIp) {
		this.customerIp = customerIp;
	}

	public String getAwardFrom() {
		return awardFrom;
	}

	public void setAwardFrom(String awardFrom) {
		this.awardFrom = awardFrom;
	}
	
	/**
	 * @return the storeId
	 */
	public String getStoreId() {
		return storeId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getTelePhone() {
		return telePhone;
	}

	public void setTelePhone(String telePhone) {
		this.telePhone = telePhone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerAwardDto [cusAwardId=" + cusAwardId + ", awardId="
				+ awardId + ", activityId=" + activityId + ", openId=" + openId
				+ ", awardName=" + awardName + ", getState=" + getState
				+ ", customerIp=" + customerIp + ", awardFrom=" + awardFrom
				+ ", storeId=" + storeId + ", telePhone=" + telePhone
				+ ", userName=" + userName + "]";
	}
}
