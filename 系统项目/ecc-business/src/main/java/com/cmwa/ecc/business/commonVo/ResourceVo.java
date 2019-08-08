package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;

/**
 * @TODO	资源实体
 * @author ex-liuy
 * @createDate 2017年6月6日
 */
@Alias("resourceVo")
public class ResourceVo extends BaseVo {
	/**
	 * 资源id
	 */
	private String resId;
	/**
	 * 资源名称
	 */
	private String resName;
	/**
	 * 资源所有者
	 */
	private String resOwner;
	/**
	 * 资源员工ids
	 */
	private String empIds;
	/**
	 * 资源角色ids
	 */
	private String roleIds;
	
	public ResourceVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public String getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(String roleIds) {
		this.roleIds = roleIds;
	}

	public String getEmpIds() {
		return empIds;
	}

	public void setEmpIds(String empIds) {
		this.empIds = empIds;
	}

	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getResName() {
		return resName;
	}
	public void setResName(String resName) {
		this.resName = resName;
	}
	public String getResOwner() {
		return resOwner;
	}
	public void setResOwner(String resOwner) {
		this.resOwner = resOwner;
	}
	
}
