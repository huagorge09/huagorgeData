package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;

@Alias("userInfoVo")
public class UserInfoVo {
	
     
	/**用户id*/
	private String empID; 
	
	/**用户名*/
	private String empName;

	/**父机构ID*/
	private String reorgId;
	
	/**父机构名称*/
	private String reOrgName;
	
	/**机构ID*/
	private String orgId;
	
	/**机构名称*/
	private String orgName;
	
	/**二级机构id*/
	private String secondOrgid;
	
	/**二级机构名称*/
	private String secondOrgname;
	
	/**岗位ID*/
	private String titleId;
	
	/**岗位名称*/
	private String titleName;
	/**用户简称**/
	private String empSName;
	
	/**
	 * 用户状态, STAR启用
	 */
	private String empStat;
	
	/**
	 * 岗位状态
	 */
	private String ormemberStat;
	
	/** 用户邮箱 */
	private String empEmail;

	public String getEmpID() {
		return empID;
	}

	public void setEmpID(String empID) {
		this.empID = empID;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public String getReorgId() {
		return reorgId;
	}

	public void setReorgId(String reorgId) {
		this.reorgId = reorgId;
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

	public String getSecondOrgid() {
		return secondOrgid;
	}

	public void setSecondOrgid(String secondOrgid) {
		this.secondOrgid = secondOrgid;
	}

	public String getSecondOrgname() {
		return secondOrgname;
	}

	public void setSecondOrgname(String secondOrgname) {
		this.secondOrgname = secondOrgname;
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

	public String getEmpSName() {
		return empSName;
	}

	public void setEmpSName(String empSName) {
		this.empSName = empSName;
	}

	public String getEmpStat() {
		return empStat;
	}

	public void setEmpStat(String empStat) {
		this.empStat = empStat;
	}

	public String getOrmemberStat() {
		return ormemberStat;
	}

	public void setOrmemberStat(String ormemberStat) {
		this.ormemberStat = ormemberStat;
	}
	

	public String getEmpEmail() {
		return empEmail;
	}

	public void setEmpEmail(String empEmail) {
		this.empEmail = empEmail;
	}

	@Override
	public String toString() {
		return "UserInfoVo [empID=" + empID + ", empName=" + empName
				+ ", reorgId=" + reorgId + ", reOrgName=" + reOrgName
				+ ", orgId=" + orgId + ", orgName=" + orgName
				+ ", secondOrgid=" + secondOrgid + ", secondOrgname="
				+ secondOrgname + ", titleId=" + titleId + ", titleName="
				+ titleName + ", empEmail=" + empEmail + "]";
	}
	
	

	
	
	
	
	
	
	

}
