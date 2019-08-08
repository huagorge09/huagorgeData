package com.cmwa.ec.weixin.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信活动-活动信息
 * @author ex-chenhq
 *
 */
public class ActivityInfoDto extends ActivityBaseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6116410061345874220L;
	
	/**
	 * 活动ID
	 */
	private String activityId;
	
	/**
	 * 活动名称
	 */
	private String activityName;
	
	/**
	 * 活动状态(0正常)
	 */
	private String activityState;
	
	/**
	 * 活动开始时间
	 */
	private Date startTime;
	
	/**
	 * 活动截止时间
	 */
	private String endTime;
	
	/**
	 * 父活动Id
	 */
	private String parentId;
	
	/**
	 * 父活动名
	 */
	private String parentName;

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getActivityState() {
		return activityState;
	}

	public void setActivityState(String activityState) {
		this.activityState = activityState;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	@Override
	public String toString() {
		return "ActivityInfoDto [activityId=" + activityId + ", activityName="
				+ activityName + ", activityState=" + activityState
				+ ", startTime=" + startTime + ", endTime=" + endTime
				+ ", parentId=" + parentId + ", parentName=" + parentName + "]";
	}

}
