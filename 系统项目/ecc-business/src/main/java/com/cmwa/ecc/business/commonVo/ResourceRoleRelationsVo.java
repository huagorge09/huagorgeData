package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;

/**
 * 资源-角色关系实体
 * @TODO	
 * @author ex-liuy
 * @createDate 2017年6月6日
 */
@Alias("resourceRoleRelationsVo")
public class ResourceRoleRelationsVo extends BaseVo {
	/**
	 * 资源id
	 */
	private String resId;
	/**
	 * 角色id
	 */
	private String roleId;
	
	public ResourceRoleRelationsVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	
	
}
