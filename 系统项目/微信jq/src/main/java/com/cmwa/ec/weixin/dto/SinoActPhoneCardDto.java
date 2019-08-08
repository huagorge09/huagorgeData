package com.cmwa.ec.weixin.dto;

/**
 * 华安保险充值电话卡dto类
 * @author ex-wangz2
 *
 */
public class SinoActPhoneCardDto {

	/**
	 * 请求流水号
	 */
	private String reqStreamId;
	/**
	 * 平台订单号
	 */
	private String orderNo;
	/**
	 * 充值手机号
	 */
	private String assignMobile;
	/**
	 * 充值状态
	 */
	private String assignStatus;
	/**
	 * 充值时间
	 */
	private String assignDate;
	/**
	 * 创建用户
	 */
	private String createdUser;
	/**
	 * 创建时间
	 */
	private String createdDate;
	/**
	 * 修改用户
	 */
	private String updatedUser;
	/**
	 * 修改时间
	 */
	private String updatedDate;
	/**
	 * 发放渠道
	 */
	private String assignChannel;
	/**
	 * 查询状态
	 */
	private String queryStatus;
	/**
	 * 查询信息
	 */
	private String queryMsg;
	/**
	 * 用户openid
	 */
	private String openid;
	/**
	 * 用户身份证
	 */
	private String idCard;
	
	
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
	 * @return the orderNo
	 */
	public String getOrderNo() {
		return orderNo;
	}
	/**
	 * @param orderNo the orderNo to set
	 */
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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
	 * @return the assignStatus
	 */
	public String getAssignStatus() {
		return assignStatus;
	}
	/**
	 * @param assignStatus the assignStatus to set
	 */
	public void setAssignStatus(String assignStatus) {
		this.assignStatus = assignStatus;
	}
	/**
	 * @return the assignDate
	 */
	public String getAssignDate() {
		return assignDate;
	}
	/**
	 * @param assignDate the assignDate to set
	 */
	public void setAssignDate(String assignDate) {
		this.assignDate = assignDate;
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
	/**
	 * @return the queryStatus
	 */
	public String getQueryStatus() {
		return queryStatus;
	}
	/**
	 * @param queryStatus the queryStatus to set
	 */
	public void setQueryStatus(String queryStatus) {
		this.queryStatus = queryStatus;
	}
	/**
	 * @return the queryMsg
	 */
	public String getQueryMsg() {
		return queryMsg;
	}
	/**
	 * @param queryMsg the queryMsg to set
	 */
	public void setQueryMsg(String queryMsg) {
		this.queryMsg = queryMsg;
	}
	/**
	 * @return the openid
	 */
	public String getOpenid() {
		return openid;
	}
	/**
	 * @param openid the openid to set
	 */
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	/**
	 * @return the idCard
	 */
	public String getIdCard() {
		return idCard;
	}
	/**
	 * @param idCard the idCard to set
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SinoActPhoneCardDto [reqStreamId=" + reqStreamId + ", orderNo=" + orderNo + ", assignMobile="
				+ assignMobile + ", assignStatus=" + assignStatus + ", assignDate=" + assignDate + ", createdUser="
				+ createdUser + ", createdDate=" + createdDate + ", updatedUser=" + updatedUser + ", updatedDate="
				+ updatedDate + ", assignChannel=" + assignChannel + ", queryStatus=" + queryStatus + ", queryMsg="
				+ queryMsg + ", openid=" + openid + ", idCard=" + idCard + "]";
	}
	
}
