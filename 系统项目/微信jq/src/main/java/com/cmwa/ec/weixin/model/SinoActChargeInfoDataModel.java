package com.cmwa.ec.weixin.model;

public class SinoActChargeInfoDataModel {

	private String reqStreamId;
	private String orderNo;
	private String balance;
	private String applyTime;
	private String chargeNumBalance;
	private String checkTime;
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
	 * @return the balance
	 */
	public String getBalance() {
		return balance;
	}
	/**
	 * @param balance the balance to set
	 */
	public void setBalance(String balance) {
		this.balance = balance;
	}
	/**
	 * @return the applyTime
	 */
	public String getApplyTime() {
		return applyTime;
	}
	/**
	 * @param applyTime the applyTime to set
	 */
	public void setApplyTime(String applyTime) {
		this.applyTime = applyTime;
	}
	/**
	 * @return the chargeNumBalance
	 */
	public String getChargeNumBalance() {
		return chargeNumBalance;
	}
	/**
	 * @param chargeNumBalance the chargeNumBalance to set
	 */
	public void setChargeNumBalance(String chargeNumBalance) {
		this.chargeNumBalance = chargeNumBalance;
	}
	/**
	 * @return the checkTime
	 */
	public String getCheckTime() {
		return checkTime;
	}
	/**
	 * @param checkTime the checkTime to set
	 */
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SinoActChargeInfoDataModel [reqStreamId=" + reqStreamId + ", orderNo=" + orderNo + ", balance="
				+ balance + ", applyTime=" + applyTime + ", chargeNumBalance=" + chargeNumBalance + ", checkTime="
				+ checkTime + "]";
	}
	
}
