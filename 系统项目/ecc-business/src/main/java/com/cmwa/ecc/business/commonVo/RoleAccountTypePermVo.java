package com.cmwa.ecc.business.commonVo;

/**
 * @date 2016.06.12
 *角色账户类型权限表对应Vo
 */

public class RoleAccountTypePermVo extends BaseVo{
	private int roleId; // 角色ID
	private String accTypeId; // 账户类型ID
	private String editPerm; // 编辑权限(1：可编辑，0：不可编辑)
	private String referPerm; // 授权查看权限(1：可查看，0：不可查看)
	private String stat; // 状态
	public int getRoleId() {
		return roleId;
	}
	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}
	public String getAccTypeId() {
		return accTypeId;
	}
	public void setAccTypeId(String accTypeId) {
		this.accTypeId = accTypeId;
	}
	public String getEditPerm() {
		return editPerm;
	}
	public void setEditPerm(String editPerm) {
		this.editPerm = editPerm;
	}
	public String getReferPerm() {
		return referPerm;
	}
	public void setReferPerm(String referPerm) {
		this.referPerm = referPerm;
	}
	public String getStat() {
		return stat;
	}
	public void setStat(String stat) {
		this.stat = stat;
	}
} 
