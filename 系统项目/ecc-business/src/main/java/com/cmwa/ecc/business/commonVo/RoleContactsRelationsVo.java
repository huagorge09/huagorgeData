package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;

/**
 * 角色联系人关系
 * @author ex-liuy
 * @createDate 2017年6月2日
 */
@Alias("roleContactsRelationsVo")
public class RoleContactsRelationsVo extends BaseVo {
	
	/**
	 * 角色ID 
	 */
	private String roleId;
	
	/**
	 * 联系人id
	 */
	private String conId;
	
	/**
	 * 状态
	 */
	private String stat;

	public RoleContactsRelationsVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}
	
}