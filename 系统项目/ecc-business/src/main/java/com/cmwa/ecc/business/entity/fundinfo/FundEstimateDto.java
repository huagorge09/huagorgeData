package com.cmwa.ecc.business.entity.fundinfo;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("fundEstimateDto")
public class FundEstimateDto  implements Serializable
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String funcode; // 产品代码
	private String theDate; // 落地日期
	private String estimateDate; // 估值日期
	private String totalValue; // 总净值
	private String netValue; // 单位净值
	private String accNetValue; // 累计单位净值
	private String faShare; // 实收资本
	private String reviewBy; // 复核人
	private String reviewDate; // 复核时间
	private String isValid;
	private String createdUser; // 创建人
	private String createdDate; // 创建时间
	private String updatedUser; // 更新人
	private String updatedDate; // 更新时间
	private String theScale; // 当期总规模
	private String theOutBonus; // 当期总分红

	private String oldTheDate; // 落地日期，与funcode建立联合主键

	public String getTheDate() {
		return theDate;
	}

	public void setTheDate(String theDate) {
		this.theDate = theDate;
	}

	public String getEstimateDate() {
		return estimateDate;
	}

	public void setEstimateDate(String estimateDate) {
		this.estimateDate = estimateDate;
	}

	public String getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(String totalValue) {
		this.totalValue = totalValue;
	}

	public String getNetValue() {
		return netValue;
	}

	public void setNetValue(String netValue) {
		this.netValue = netValue;
	}

	public String getAccNetValue() {
		return accNetValue;
	}

	public void setAccNetValue(String accNetValue) {
		this.accNetValue = accNetValue;
	}

	public String getFaShare() {
		return faShare;
	}

	public void setFaShare(String faShare) {
		this.faShare = faShare;
	}

	public String getReviewBy() {
		return reviewBy;
	}

	public void setReviewBy(String reviewBy) {
		this.reviewBy = reviewBy;
	}

	public String getReviewDate() {
		return reviewDate;
	}

	public void setReviewDate(String reviewDate) {
		this.reviewDate = reviewDate;
	}

	public String getIsValid() {
		return isValid;
	}

	public void setIsValid(String isValid) {
		this.isValid = isValid;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedUser() {
		return updatedUser;
	}

	public void setUpdatedUser(String updatedUser) {
		this.updatedUser = updatedUser;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getTheScale() {
		return theScale;
	}

	public void setTheScale(String theScale) {
		this.theScale = theScale;
	}

	public String getTheOutBonus() {
		return theOutBonus;
	}

	public void setTheOutBonus(String theOutBonus) {
		this.theOutBonus = theOutBonus;
	}

	public String getFuncode() {
		return funcode;
	}

	public void setFuncode(String funcode) {
		this.funcode = funcode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "FundEstimateDto [funcode=" + funcode + ", theDate=" + theDate
				+ ", estimateDate=" + estimateDate + ", totalValue="
				+ totalValue + ", netValue=" + netValue + ", accNetValue="
				+ accNetValue + ", faShare=" + faShare + ", reviewBy="
				+ reviewBy + ", reviewDate=" + reviewDate + ", isValid="
				+ isValid + ", createdUser=" + createdUser + ", createdDate="
				+ createdDate + ", updatedUser=" + updatedUser
				+ ", updatedDate=" + updatedDate + ", theScale=" + theScale
				+ ", theOutBonus=" + theOutBonus + ", getTheDate()="
				+ getTheDate() + ", getEstimateDate()=" + getEstimateDate()
				+ ", getTotalValue()=" + getTotalValue() + ", getNetValue()="
				+ getNetValue() + ", getAccNetValue()=" + getAccNetValue()
				+ ", getFaShare()=" + getFaShare() + ", getReviewBy()="
				+ getReviewBy() + ", getReviewDate()=" + getReviewDate()
				+ ", getIsValid()=" + getIsValid() + ", getCreatedUser()="
				+ getCreatedUser() + ", getCreatedDate()=" + getCreatedDate()
				+ ", getUpdatedUser()=" + getUpdatedUser()
				+ ", getUpdatedDate()=" + getUpdatedDate() + ", getTheScale()="
				+ getTheScale() + ", getTheOutBonus()=" + getTheOutBonus()
				+ ", getFuncode()=" + getFuncode() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}

	public String getOldTheDate() {
		return oldTheDate;
	}

	public void setOldTheDate(String oldTheDate) {
		this.oldTheDate = oldTheDate;
	}

	
}
