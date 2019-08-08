package com.cmwa.ec.weixin.dto;

public class BankLogoDto {

	private String bankNo;
	private String bankName;
	private byte[] bankLogo;
	private String bankLogoCode;
	private String createdDate;
	private String updatedDate;
	
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public byte[] getBankLogo() {
		return bankLogo;
	}
	public void setBankLogo(byte[] bankLogo) {
		this.bankLogo = bankLogo;
	}
	public String getBankLogoCode() {
		return bankLogoCode;
	}
	public void setBankLogoCode(String bankLogoCode) {
		this.bankLogoCode = bankLogoCode;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getUpdatedDate() {
		return updatedDate;
	}
	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}
	
	@Override
	public String toString() {
		return "BankLogoDto [bankNo=" + bankNo + ", bankName=" + bankName + ", createdDate=" + createdDate
				+ ", updatedDate=" + updatedDate + "]";
	}
	
	
}
