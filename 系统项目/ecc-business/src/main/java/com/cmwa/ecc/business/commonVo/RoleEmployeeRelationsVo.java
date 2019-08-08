package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;

/**
 * 角色员工关系
 * @author ex-liuy
 * @createDate 2017年6月2日
 */
@Alias("roleEmployeeRelationsVo")
public class RoleEmployeeRelationsVo extends BaseVo {
	
	/**
	 * 角色ID 
	 */
	private String roleId;
	
	/**
	 * 员工id
	 */
	private String empId;
	
	
	/**
	 * 状态
	 */
	private String stat;
	
	/**
	 * 拓展属性
	 */
	private String attr1;
	private String attr2;
	private String attr3;
	
	private String orgId;
	private String emName;
	private String titleName;
	private String orgName;
	
	public String getAttr1() {
		return attr1;
	}


	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}


	public String getAttr2() {
		return attr2;
	}


	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}


	public String getAttr3() {
		return attr3;
	}


	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}


	public String getRoleId() {
		return roleId;
	}


	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}


	public String getEmpId() {
		return empId;
	}


	public void setEmpId(String empId) {
		this.empId = empId;
	}


	public String getStat() {
		return stat;
	}


	public void setStat(String stat) {
		this.stat = stat;
	}


	public String getOrgId() {
		return orgId;
	}


	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}


	public String getEmName() {
		return emName;
	}


	public void setEmName(String emName) {
		this.emName = emName;
	}


	public String getTitleName() {
		return titleName;
	}


	public void setTitleName(String titleName) {
		this.titleName = titleName;
	}


	public String getOrgName() {
		return orgName;
	}


	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}
	
	
	
}
