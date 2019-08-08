package com.cmwa.ec.weixin.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * 微信活动-奖品信息
 * @author ex-chenhq
 *
 */
public class AwardInfoDto extends ActivityBaseDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4505325603441326685L;
	/**
	 * 奖品Id
	 */
	private String awardId;
	
	/**
	 * 活动编码
	 */
	private String activityId;
	
	/**
	 * 奖品名称
	 */
	private String awardName;
	
	/**
	 * 奖品类型(0:虚拟物品 1:实物 2:特殊物品)
	 */
	private String awardType;
	
	/**
	 * 奖品排序
	 */
	private Integer awardSort;
	
	/**
	 * 奖品价值
	 */
	private Integer awardPrice;
	
	/**
	 * 奖品状态(0:正常抽取 1:存货不足 2:奖品已下架 默认0)
	 */
	private String awardState;
	
	/**
	 * 奖品生效开始时间
	 */
	private Date validDate;
	
	/**
	 * 奖品过期时间
	 */
	private Date invalidDate;
	
	/**
	 * 父奖品Id
	 */
	private String awardPid; 
	
	/**
	 * 父奖品名称
	 */
	private String awardPname;
	
	/**
	 * 奖品概率
	 */
	private Double probability;
	
	/**
	 *奖品数量 
	 */
	private Integer factnum;

	public String getAwardId() {
		return awardId;
	}

	public void setAwardId(String awardId) {
		this.awardId = awardId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getAwardName() {
		return awardName;
	}

	public void setAwardName(String awardName) {
		this.awardName = awardName;
	}

	public String getAwardType() {
		return awardType;
	}

	public void setAwardType(String awardType) {
		this.awardType = awardType;
	}

	public Integer getAwardSort() {
		return awardSort;
	}

	public void setAwardSort(Integer awardSort) {
		this.awardSort = awardSort;
	}

	public Integer getAwardPrice() {
		return awardPrice;
	}

	public void setAwardPrice(Integer awardPrice) {
		this.awardPrice = awardPrice;
	}

	public String getAwardState() {
		return awardState;
	}

	public void setAwardState(String awardState) {
		this.awardState = awardState;
	}

	public Date getValidDate() {
		return validDate;
	}

	public void setValidDate(Date validDate) {
		this.validDate = validDate;
	}

	public Date getInvalidDate() {
		return invalidDate;
	}

	public void setInvalidDate(Date invalidDate) {
		this.invalidDate = invalidDate;
	}

	public String getAwardPid() {
		return awardPid;
	}

	public void setAwardPid(String awardPid) {
		this.awardPid = awardPid;
	}

	public String getAwardPname() {
		return awardPname;
	}

	public void setAwardPname(String awardPname) {
		this.awardPname = awardPname;
	}
	
	

	public Double getProbability() {
		return probability;
	}

	public void setProbability(Double probability) {
		this.probability = probability;
	}

	public Integer getFactnum() {
		return factnum;
	}

	public void setFactnum(Integer factnum) {
		this.factnum = factnum;
	}

	@Override
	public String toString() {
		return "AwardInfoDto [awardId=" + awardId + ", activityId="
				+ activityId + ", awardName=" + awardName + ", awardType="
				+ awardType + ", awardSort=" + awardSort + ", awardPrice="
				+ awardPrice + ", awardState=" + awardState + ", validDate="
				+ validDate + ", invalidDate=" + invalidDate + ", awardPid="
				+ awardPid + ", awardPname=" + awardPname + ", probability="
				+ probability + ", factnum=" + factnum + "]";
	}

}
