package com.cmwa.ec.weixin.dto;


/**
 * 华安保险奖品dto类
 * @author ex-hezk
 *
 */
public class SinoActAwardDto {

	/**
	 * 订单号
	 */
	private String reqStreamId;
	
	/**
	 * 卡号
	 */
	private String cardNum;
	
	/**
	 * 卡密
	 */
	private String cardPwd;
	
	/**
	 * 发放用户手机
	 */
	private String assignMobile;
	
	/**
	 * 发放用户openid
	 */
	private String assignOpenid;
	
	/**
	 * 发放用户身份证
	 */
	private String assignIdcard;
	
	/**
	 * 截至有效期
	 */
	private String expireDate;
	
	/**
	 * 发送状态
	 */
	private String sendStatus;
	
	/**
	 * 发送时间
	 */
	private String sendDate;
	
	/**
	 * 
	 */
	private String createdDate;
	
	/**
	 * 创建用户
	 */
	private String createdUser;
	
	/**
	 * 创建时间
	 */
	private String updatedDate;
	
	/**
	 * 修改用户
	 */
	private String updatedUser;
	
	/**
	 * 发放渠道 
	 */
	private String assignChannel;

	/**
	 * @return the reqStreamId
	 */
	public String getReqStreamId() {
		return reqStreamId;
	}

	/**
	 * @param reqStreamId the reqStreamId to set
	 */
	public void setReqStreamId(String reqStreamId) {
		this.reqStreamId = reqStreamId;
	}

	/**
	 * @return the cardNum
	 */
	public String getCardNum() {
		return cardNum;
	}

	/**
	 * @param cardNum the cardNum to set
	 */
	public void setCardNum(String cardNum) {
		this.cardNum = cardNum;
	}

	/**
	 * @return the cardPwd
	 */
	public String getCardPwd() {
		return cardPwd;
	}

	/**
	 * @param cardPwd the cardPwd to set
	 */
	public void setCardPwd(String cardPwd) {
		this.cardPwd = cardPwd;
	}

	/**
	 * @return the assignMobile
	 */
	public String getAssignMobile() {
		return assignMobile;
	}

	/**
	 * @param assignMobile the assignMobile to set
	 */
	public void setAssignMobile(String assignMobile) {
		this.assignMobile = assignMobile;
	}

	/**
	 * @return the assignOpenid
	 */
	public String getAssignOpenid() {
		return assignOpenid;
	}

	/**
	 * @param assignOpenid the assignOpenid to set
	 */
	public void setAssignOpenid(String assignOpenid) {
		this.assignOpenid = assignOpenid;
	}

	/**
	 * @return the assignIdcard
	 */
	public String getAssignIdcard() {
		return assignIdcard;
	}

	/**
	 * @param assignIdcard the assignIdcard to set
	 */
	public void setAssignIdcard(String assignIdcard) {
		this.assignIdcard = assignIdcard;
	}

	/**
	 * @return the expireDate
	 */
	public String getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate the expireDate to set
	 */
	public void setExpireDate(String expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * @return the sendStatus
	 */
	public String getSendStatus() {
		return sendStatus;
	}

	/**
	 * @param sendStatus the sendStatus to set
	 */
	public void setSendStatus(String sendStatus) {
		this.sendStatus = sendStatus;
	}

	/**
	 * @return the sendDate
	 */
	public String getSendDate() {
		return sendDate;
	}

	/**
	 * @param sendDate the sendDate to set
	 */
	public void setSendDate(String sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * @return the createdDate
	 */
	public String getCreatedDate() {
		return createdDate;
	}

	/**
	 * @param createdDate the createdDate to set
	 */
	public void setCreatedDate(String createdDate) {
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
	public String getUpdatedDate() {
		return updatedDate;
	}

	/**
	 * @param updatedDate the updatedDate to set
	 */
	public void setUpdatedDate(String updatedDate) {
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
	 * @return the assignChannel
	 */
	public String getAssignChannel() {
		return assignChannel;
	}

	/**
	 * @param assignChannel the assignChannel to set
	 */
	public void setAssignChannel(String assignChannel) {
		this.assignChannel = assignChannel;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SinoActAwardDto [reqStreamId=" + reqStreamId + ", cardNum="
				+ cardNum + ", cardPwd=" + cardPwd + ", assignMobile="
				+ assignMobile + ", assignOpenid=" + assignOpenid
				+ ", assignIdcard=" + assignIdcard + ", expireDate="
				+ expireDate + ", sendStatus=" + sendStatus + ", sendDate="
				+ sendDate + ", createdDate=" + createdDate + ", createdUser="
				+ createdUser + ", updatedDate=" + updatedDate
				+ ", updatedUser=" + updatedUser + "]";
	}
	
	
}
