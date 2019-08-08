package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;

/**
 * 分享权限组表
 * @author ex-liuy
 * @createDate 2017年6月2日
 */
@Alias("authorityGroupVo")
public class AuthorityGroupVo extends BaseVo {
	
	/**
	 * 分享权限id
	 */
	private String authId;
	
	/**
	 * 角色ID 
	 */
	private String roleId;
	
	/**
	 * 分享角色id
	 */
	private String shareId;

	public String getAuthId() {
		return authId;
	}

	public void setAuthId(String authId) {
		this.authId = authId;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getShareId() {
		return shareId;
	}

	public void setShareId(String shareId) {
		this.shareId = shareId;
	}


	
	
	
	
}
