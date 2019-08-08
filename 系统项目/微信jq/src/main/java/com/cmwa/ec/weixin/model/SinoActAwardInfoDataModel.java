package com.cmwa.ec.weixin.model;

public class SinoActAwardInfoDataModel {
	/**
	 * 卡号
	 */
	private String cardNum;
	
	/**
	 * 卡密
	 */
	private String cardPwd;
	
	/**
	 * 截至有效期
	 */
	private String expiretime;

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
	 * @return the expiretime
	 */
	public String getExpiretime() {
		return expiretime;
	}

	/**
	 * @param expiretime the expiretime to set
	 */
	public void setExpiretime(String expiretime) {
		this.expiretime = expiretime;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SinoActAwardInfoDataModel [cardNum=" + cardNum + ", cardPwd="
				+ cardPwd + ", expiretime=" + expiretime + "]";
	}
	
	
}