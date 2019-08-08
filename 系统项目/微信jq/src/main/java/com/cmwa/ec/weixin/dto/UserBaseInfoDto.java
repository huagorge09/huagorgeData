package com.cmwa.ec.weixin.dto;

import java.io.Serializable;

/**
 * 类说明-user_baseinfo对应类
 * @author ex-chenhq
 *
 */
public class UserBaseInfoDto implements Serializable {

	private static final long serialVersionUID = 2754479156925324383L;
	private String cmfUserId;
	private String custName;
	private String idType;
	private String idNo;
	private String mobile;
	private String mobileStatus;
	private String email;
	private String userType;
	public String getCmfUserId() {
		return cmfUserId;
	}
	public void setCmfUserId(String cmfUserId) {
		this.cmfUserId = cmfUserId;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getIdType() {
		return idType;
	}
	public void setIdType(String idType) {
		this.idType = idType;
	}
	public String getIdNo() {
		return idNo;
	}
	public void setIdNo(String idNo) {
		this.idNo = idNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getMobileStatus() {
		return mobileStatus;
	}
	public void setMobileStatus(String mobileStatus) {
		this.mobileStatus = mobileStatus;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
}
