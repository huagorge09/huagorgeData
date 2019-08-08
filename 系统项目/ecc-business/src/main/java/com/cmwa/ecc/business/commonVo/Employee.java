package com.cmwa.ecc.business.commonVo;

public class Employee {

	/**
	 * 员工ID
	 */
	private String ID;
	
	/**
	 * 员工登陆名
	 */
	private String loginName;
	
	/**
	 * 真实姓名
	 */
	private String name;

	/**
	 * 顶级部门ID
	 */
	private String topOrganid;
	
	/**
	 * 是否中后台角色
	 */
	private String isMiddleBack;

	public String getID() {
	    return ID;
	}

	public void setID(String iD) {
	    ID = iD;
	}

	public String getLoginName() {
	    return loginName;
	}

	public void setLoginName(String loginName) {
	    this.loginName = loginName;
	}

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

	public String getTopOrganid() {
	    return topOrganid;
	}

	public void setTopOrganid(String topOrganid) {
	    this.topOrganid = topOrganid;
	}

	public String getIsMiddleBack() {
		return isMiddleBack;
	}

	public void setIsMiddleBack(String isMiddleBack) {
		this.isMiddleBack = isMiddleBack;
	}
}
