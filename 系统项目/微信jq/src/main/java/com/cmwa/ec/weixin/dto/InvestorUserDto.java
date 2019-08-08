package com.cmwa.ec.weixin.dto;

import java.util.Date;

/**
 * 投资者教育活动
 * 参与活动用户信息
 * @author ex-wangz2
 *
 */
public class InvestorUserDto {

	private String openId;
	private Date createDate;
	private String userFullName;
	private String mobile;
	private String address;
	
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public String getUserFullName() {
		return userFullName;
	}
	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	@Override
	public String toString() {
		return "InvestorUserDto [openId=" + openId + ", createDate=" + createDate + ", userFullName=" + userFullName
				+ ", mobile=" + mobile + ", address=" + address + "]";
	}
}
