package com.cmwa.ec.weixin.dto;

import java.util.Date;

public class InvestorForwardDto {

	private String openId;
	private Date firstDate;
	private Date lastDate;
	private Integer friendCount;
	private Integer circleCount;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getFirstDate() {
		return firstDate;
	}
	public void setFirstDate(Date firstDate) {
		this.firstDate = firstDate;
	}
	public Date getLastDate() {
		return lastDate;
	}
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}
	public Integer getFriendCount() {
		return friendCount;
	}
	public void setFriendCount(Integer friendCount) {
		this.friendCount = friendCount;
	}
	public Integer getCircleCount() {
		return circleCount;
	}
	public void setCircleCount(Integer circleCount) {
		this.circleCount = circleCount;
	}
	
	@Override
	public String toString() {
		return "InvestorForwardDto [openId=" + openId + ", firstDate=" + firstDate + ", lastDate=" + lastDate
				+ ", friendCount=" + friendCount + ", circleCount=" + circleCount + "]";
	}
	
}
