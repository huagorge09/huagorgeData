package com.cmwa.ec.weixin.dto;


public class RemindFailureRedeemDto {
	private String invnm;	//用户名称
	private String mobileNo;  //用户手机
	private String fundnm;	//产品名称
	private String fundId;	//失败订单
	private String subamt;	//失败金额
	private String subquty;	//失败份额
	private String date1;
	private String serialno;
	public String getInvnm() {
		return invnm;
	}
	public String getSerialno() {
		return serialno;
	}
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
	public void setInvnm(String invnm) {
		this.invnm = invnm;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
	}
	public String getFundnm() {
		return fundnm;
	}
	public void setFundnm(String fundnm) {
		this.fundnm = fundnm;
	}
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getSubamt() {
		return subamt;
	}
	public void setSubamt(String subamt) {
		this.subamt = subamt;
	}
	public String getSubquty() {
		return subquty;
	}
	public void setSubquty(String subquty) {
		this.subquty = subquty;
	}
	public String getDate1() {
		return date1;
	}
	public void setDate1(String date) {
		this.date1 = date;
	}
	@Override
	public String toString() {
		return "RemindFailureRedeemDto [invnm=" + invnm + ", mobileNo="
				+ mobileNo + ", fundnm=" + fundnm + ", fundId=" + fundId
				+ ", subamt=" + subamt + ", subquty=" + subquty + ", date1="
				+ date1 + ", serialno=" + serialno + "]";
	} 
}
