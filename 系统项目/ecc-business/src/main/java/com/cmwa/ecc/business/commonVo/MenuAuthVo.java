package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;
/**
 * {@link T_SYS_CBP_ORMEMBER_MENU}
 * {@link T_SYS_CBP_EMP_MENU}
 */
@Alias("menuAuthVo")
public class MenuAuthVo {
	private String menuId;
	private String empId;
	private String titleId;
	private String orgId;
	private String status;
	
	
	
	public String getMenuId() {
		return menuId;
	}
	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getTitleId() {
		return titleId;
	}
	public void setTitleId(String titleId) {
		this.titleId = titleId;
	}
	public String getOrgId() {
		return orgId;
	}
	public void setOrgId(String orgId) {
		this.orgId = orgId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	

}
