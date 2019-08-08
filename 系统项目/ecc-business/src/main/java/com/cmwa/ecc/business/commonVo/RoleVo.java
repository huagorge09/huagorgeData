package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;
import org.springframework.util.StringUtils;

import com.cmwa.ecc.business.utils.Cached;
import com.cmwa.ecc.business.utils.WaConstants;
import com.cmwa.ecc.business.utils.cached.DictionaryCached;
import com.cmwa.ecc.business.utils.cached.EmployeeCached;

/**
 * 角色实体类
 * @author ex-weicb
 * @Date 2016年4月26日 下午5:56:30
 * @Description
 */
@Alias("roleVo")
public class RoleVo extends BaseVo {
	
	/**
	 * 角色ID  编码规则：6位流水号
	 */
	private String roleId;
	
	/**
	 * 角色名称
	 */
	private String roleName;
	
	/**
	 * 角色类型(ROLE_XXX_TYPE)
	 */
	private String roleType;
	
	/**
	 * 状态
	 */
	private String stat;
	
	/**
	 * 权限类型(1:普通,2:特殊)
	 */
	private String authority;
	
	/**
	 * 状态名称
	 */
	@Cached
	private String statName;
	
	/**
	 * 角色类型编码code
	 */
	private String roleTypeCode;
	
	private String authorityType;
	
	@Cached
	private String empsName;
	
	private String empIds;
	
	private String shareRoleIds;
	
	private String resourceName;
	
	private String emName;
	
	@Cached
	private String roleTypeName;
	/**
	 * 来源描述
	 */
	private String sourceDesc;
	
	/**
	 * 是否发送邮件
	 */
	private String isSendMail;
	
	
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

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleType() {
		return roleType;
	}

	public void setRoleType(String roleType) {
		this.roleType = roleType;
	}

	public String getStat() {
		return stat;
	}

	public void setStat(String stat) {
		this.stat = stat;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	public String getStatName() {
		if (!StringUtils.isEmpty(stat)) {
			return DictionaryCached.getDictName(WaConstants.DCTTYPE_DAT_CHK_SAT, stat);
		}
		return statName;
	}

	public void setStatName(String statName) {
		this.statName = statName;
	}

	public String getRoleTypeCode() {
		return roleTypeCode;
	}

	public void setRoleTypeCode(String roleTypeCode) {
		this.roleTypeCode = roleTypeCode;
	}

	public String getAuthorityType() {
		return authorityType;
	}

	public void setAuthorityType(String authorityType) {
		this.authorityType = authorityType;
	}

	public String getEmpsName() {
		if (!StringUtils.isEmpty(empIds)) {
			return EmployeeCached.getAllName(empIds);
		}
		return empsName;
	}

	public void setEmpsName(String empsName) {
		this.empsName = empsName;
	}

	public String getEmpIds() {
		return empIds;
	}

	public void setEmpIds(String empIds) {
		this.empIds = empIds;
	}

	public String getShareRoleIds() {
		return shareRoleIds;
	}

	public void setShareRoleIds(String shareRoleIds) {
		this.shareRoleIds = shareRoleIds;
	}

	public String getRoleTypeName() {
		if (!StringUtils.isEmpty(roleType)) {
			return DictionaryCached.getDictName(WaConstants.DCTTYPE_ROLE_XXX_TYPE, roleType);
		}
		return roleTypeName;
	}

	public void setRoleTypeName(String roleTypeName) {
		this.roleTypeName = roleTypeName;
	}
	
	public String getCreateName() {
		if(!StringUtils.isEmpty(createId)){
			return EmployeeCached.getName(createId);
		}
		return createName;
	}
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getEmName() {
		return emName;
	}

	public void setEmName(String emName) {
		this.emName = emName;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getModifyName() {
		if(!StringUtils.isEmpty(modifyId)){
			return EmployeeCached.getName(modifyId);
		}
		return modifyName;
	}
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}

	public String getIsSendMail() {
		return isSendMail;
	}

	public void setIsSendMail(String isSendMail) {
		this.isSendMail = isSendMail;
	}
	
}
