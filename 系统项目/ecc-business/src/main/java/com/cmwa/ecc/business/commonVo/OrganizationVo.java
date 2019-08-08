package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;

@Alias("organizationVo")
public class OrganizationVo {
	/**父机构ID*/
	private String reOrgId;
	
	/**父机构名称*/
	private String reOrgName;
	
	/**机构ID*/
	private String orgId;
	
	/**机构名称*/
	private String orgName;
	
	/**二级机构id*/
	private String secondOrgId;
	
	/**二级机构名称*/
	private String secondOrgName;
	
	/**岗位ID*/
	private String titleId;
	
	/**岗位名称*/
	private String titleName;

	public String getReOrgId() {
		return reOrgId;
	}

	public void setReOrgId(String reOrgId) {
		this.reOrgId = reOrgId;
	}

	public String getReOrgName() {
		return reOrgName;
	}

	public void setReOrgName(String reOrgName) {
		this.reOrgName = reOrgName;
	}

	public String getOrgId() {
		return orgId;
	}

	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}

	public String getOrgName() {
		return orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public String getSecondOrgId() {
		return secondOrgId;
	}

	public void setSecondOrgId(String secondOrgId) {
		this.secondOrgId = secondOrgId;
	}

	public String getSecondOrgName() {
		return secondOrgName;
	}

	public void setSecondOrgName(String secondOrgName) {
		this.secondOrgName = secondOrgName;
	}

	public String getTitleId() {
		return titleId;
	}

	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}

	public String getTitleName() {
		return titleName;
	}

	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}
	
	
	
	
}
