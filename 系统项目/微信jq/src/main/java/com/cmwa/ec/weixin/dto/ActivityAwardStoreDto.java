package com.cmwa.ec.weixin.dto;

import java.util.Date;

/**
 * 
 *
 */
public class ActivityAwardStoreDto {

	/**
	 * 主键id
	 */
	private String storeId;
	
	/**
	 * 活动id
	 */
	private String activityId;
	
	/**
	 * 奖品id
	 */
	private String awardId;
	
	/**
	 * 奖品账号
	 */
	private String awardNum;
	
	/**
	 * 奖品密码
	 */
	private String awardPwd;
	
	/**
	 * 奖品状态
	 */
	private String awardState;
	
	
	/**
	 * 创建时间
	 */
	private Date createdDate;
	
	/**
	 * 创建用户
	 */
	private String createdUser;
	
	/**
	 * 修改时间
	 */
	private Date updatedDate;
	
	/**
	 * 修改用户
	 */
	private String updatedUser;
	
	/**
	 * 乐观锁用版本号
	 */
	private String version;

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
	 * @return the activityId
	 */
	public String getActivityId() {
		return activityId;
	}

	/**
	 * @param activityId the activityId to set
	 */
	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	/**
	 * @return the awardId
	 */
	public String getAwardId() {
		return awardId;
	}

	/**
	 * @param awardId the awardId to set
	 */
	public void setAwardId(String awardId) {
		this.awardId = awardId;
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

	/**
	 * @return the awardPwd
	 */
	public String getAwardPwd() {
		return awardPwd;
	}

	/**
	 * @param awardPwd the awardPwd to set
	 */
	public void setAwardPwd(String awardPwd) {
		this.awardPwd = awardPwd;
	}

	/**
	 * @return the awardState
	 */
	public String getAwardState() {
		return awardState;
	}

	/**
	 * @param awardState the awardState to set
	 */
	public void setAwardState(String awardState) {
		this.awardState = awardState;
	}

	/**
	 * @return the createdDate
	 */
	public Date getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * @return the createdUser
	 */
	public String getCreatedUser() {
		return createdUser;
	}

	/**
	 * @param createdUser the createdUser to set
	 */
	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	/**
	 * @return the updatedDate
	 */
	public Date getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	/**
	 * @return the updatedUser
	 */
	public String getUpdatedUser() {
		return updatedUser;
	}

	/**
	 * @param updatedUser the updatedUser to set
	 */
	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * @param version the version to set
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ActivityAwardStoreDto [storeId=" + storeId + ", activityId="
				+ activityId + ", awardId=" + awardId + ", awardNum="
				+ awardNum + ", awardPwd=" + awardPwd + ", awardState="
				+ awardState + ", createdDate=" + createdDate + 
				", createdUser="+ createdUser + ", updatedDate=" + updatedDate
				+ ", updatedUser=" + updatedUser + ", version=" + version + "]";
	}
	
}