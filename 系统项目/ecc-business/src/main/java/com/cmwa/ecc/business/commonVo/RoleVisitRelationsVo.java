package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;

/**
 * 角色-拜访历史关系
 * @author ex-liuy
 * @createDate 2017年6月2日
 */
@Alias("roleVisitRelationsVo")
public class RoleVisitRelationsVo extends BaseVo {
	
	/**
	 * 角色ID 
	 */
	private String roleId;
	
	/**
	 * 拜访历史id
	 */
	private String visId;
	
	/**
	 * 状态
	 */
	private String stat;
	/**
	 * 来源描述
	 */
	private String sourceDesc;

	public RoleVisitRelationsVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getSourceDesc() {
		return sourceDesc;
	}

	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getVisId() {
		return visId;
	}

	public void setVisId(String visId) {
		this.visId = visId;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}
	
}