package com.cmwa.ec.weixin.dto;

import java.io.Serializable;


/**
 * 用户签到信息dto
 *
 */
public class UserSignInDto implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4049061068725192060L;
	/** 签到次数 */
	private Integer signTime;
	/** 是否可以签到 */
	private boolean isCanSignIn;
	/** 下一次签到时间*/
	private String nextSignTime;

	public Integer getSignTime() {
		return signTime;
	}

	public void setSignTime(Integer signTime) {
		this.signTime = signTime;
	}

	public boolean isCanSignIn() {
		return isCanSignIn;
	}

	public void setCanSignIn(boolean isCanSignIn) {
		this.isCanSignIn = isCanSignIn;
	}

	public String getNextSignTime() {
		return nextSignTime;
	}

	public void setNextSignTime(String nextSignTime) {
		this.nextSignTime = nextSignTime;
	}

	@Override
	public String toString() {
		return "UserSignInDto [signTime=" + signTime + ", isCanSignIn="
				+ isCanSignIn + ", nextSignTime=" + nextSignTime
				+ ", getSignTime()=" + getSignTime() + ", isCanSignIn()="
				+ isCanSignIn() + ", getNextSignTime()=" + getNextSignTime()
				+ ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	
	
	
}
