package com.cmwa.ec.weixin.dto;

/**
 * 引流用户dto类 
 * @author ex-hezk
 *
 */
public class SinoUserInfoDto implements Cloneable {

	/**
	 * 目前固定为 HABX
	 */
	private String appId;
	
	/**
	 * 车牌
	 */
	private String plateNo;
	
	/**
	 * 身份证
	 */
	private String idCard;
	
	/**
	 * 车主姓名
	 */
	private String ownerName;
	
	/**
	 * 品牌型号
	 */
	private String brandName;
	
	/**
	 * 新车购值价
	 */
	private String purchasePrice;
	
	/**
	 * 推荐人ID
	 */
	private String sinoUserId;
	
	/**
	 * 时间戳
	 */
	private long timestamp;
	
	/**
	 * 由 车牌+车主身份证+车主姓名+车辆品牌型号+新车购值价+推荐人id+时间戳+招商财富分配的密钥得出的MD5值
	 */
	private String sign;
	
	/**
	 * 投保省份 
	 */
	private String province;

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the plateNo
	 */
	public String getPlateNo() {
		return plateNo;
	}

	/**
	 * @param plateNo the plateNo to set
	 */
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
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

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * @return the brandName
	 */
	public String getBrandName() {
		return brandName;
	}

	/**
	 * @param brandName the brandName to set
	 */
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * @return the purchasePrice
	 */
	public String getPurchasePrice() {
		return purchasePrice;
	}

	/**
	 * @param purchasePrice the purchasePrice to set
	 */
	public void setPurchasePrice(String purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}

	/**
	 * @return the sinoUserId
	 */
	public String getSinoUserId() {
		return sinoUserId;
	}

	/**
	 * @param sinoUserId the sinoUserId to set
	 */
	public void setSinoUserId(String sinoUserId) {
		this.sinoUserId = sinoUserId;
	}

	
	
	/**
	 * @return the province
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * @param province the province to set
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SinoUserInfoDto [appId=" + appId + ", plateNo=" + plateNo
				+ ", idCard=" + idCard + ", ownerName=" + ownerName
				+ ", brandName=" + brandName + ", purchasePrice="
				+ purchasePrice + ", sinoUserId=" + sinoUserId + ", timestamp="
				+ timestamp + ", sign=" + sign + ", province=" + province + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	@Override
	public SinoUserInfoDto clone() throws CloneNotSupportedException {
		return (SinoUserInfoDto) super.clone();
	}

	
	
}
