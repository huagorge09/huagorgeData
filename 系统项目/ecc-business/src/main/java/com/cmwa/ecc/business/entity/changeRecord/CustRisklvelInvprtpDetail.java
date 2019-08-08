package com.cmwa.ecc.business.entity.changeRecord;

import org.apache.ibatis.type.Alias;

@Alias("custRisklvelInvprtpDetail")
public class CustRisklvelInvprtpDetail {
	
	/**
	 * 变动序列号
	 */
	private String changeId;
	
	/**
	 * 变动客户号
	 */
	private String custNo;
	
	/**
	 * 变动项目
	 */
	private String changeProject;
	
	/**
	 * 变动方式
	 */
	private String changeWay;
	
	/**
	 * 变动时间
	 */
	private String updateTime;
	
	/**
	 * 变动说明
	 */
	private String changeDesc;

	public String getChangeId() {
		return changeId;
	}

	public void setChangeId(String changeId) {
		this.changeId = changeId;
	}

	public String getCustNo() {
		return custNo;
	}

	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}

	public String getChangeProject() {
		return changeProject;
	}

	public void setChangeProject(String changeProject) {
		this.changeProject = changeProject;
	}

	public String getChangeWay() {
		return changeWay;
	}

	public void setChangeWay(String changeWay) {
		this.changeWay = changeWay;
	}

	public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}

	public String getChangeDesc() {
		return changeDesc;
	}

	public void setChangeDesc(String changeDesc) {
		this.changeDesc = changeDesc;
	}
	
}
