package com.cmwa.ec.weixin.dto;

/**
 * 用户奖品dto
 *
 */
public class QueryUserAwardDto {
	/** 奖品状态*/
	private String awardState;
	
	/**
	 * 奖品价值
	 */
	private String awardPrice;
	
	/**
	 * 过期时间
	 */
	private String invalidDate;

	/**
	 * 中奖记录id
	 */
	private String cusAwardId;

	
	/**
	 * 奖品名称
	 */
	private String awardName;
	
	/**
	 * 奖品Id
	 */
	private String awardId;
	
	/**
	 * 关联cmwa_wx_act_award_store.store_id
	 */
	private String storeId;
	
	/**
	 * 奖品账号
	 */
	private String awardNum;
	
	public String getAwardState() {
		return awardState;
	}

	public void setAwardState(String awardState) {
		this.awardState = awardState;
	}

	public String getAwardPrice() {
		return awardPrice;
	}

	public void setAwardPrice(String awardPrice) {
		this.awardPrice = awardPrice;
	}

	public String getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(String invalidDate) {
		this.invalidDate = invalidDate;
	}

	public String getCusAwardId() {
		return cusAwardId;
	}

	public void setCusAwardId(String cusAwardId) {
		this.cusAwardId = cusAwardId;
	}

	public String getAwardName() {
		return awardName;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	public String getAwardId() {
		return awardId;
	}

	public void setAwardId(String awardId) {
		this.awardId = awardId;
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

	/**
	 * @return the awardNum
	 */
	public String getAwardNum() {
		return awardNum;
	}

	/**
	 * @param awardNum the awardNum to set
	 */
	public void setAwardNum(String awardNum) {
		this.awardNum = awardNum;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "QueryUserAwardDto [awardState=" + awardState + ", awardPrice="
				+ awardPrice + ", invalidDate=" + invalidDate + ", cusAwardId="
				+ cusAwardId + ", awardName=" + awardName + ", awardId="
				+ awardId + ", storeId=" + storeId + ", awardNum=" + awardNum
				+ "]";
	}

	
	
}
