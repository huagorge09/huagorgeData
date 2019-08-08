package com.cmwa.ecc.business.commonVo;

import org.apache.commons.lang.StringUtils;

import com.cmwa.ecc.business.utils.cached.EmployeeCached;

/** 
 * @author      wangzy 
 * @createDate  2015年4月20日 下午1:42:15
 * @desc        实体公用类
 */
public class BaseVo {
	
	/**
	 * 数据状态
	 */
	protected String status;
	
	/**
	 * 数据状态中文名称
	 */
	protected String statusName;
	
	/**
	 * 创建人ID
	 */
	protected String createId;
	
	/**
	 * 创建人名称
	 */
	protected String createName;
	
	/**
	 * 创建时间
	 */
	protected String createTime;
	
	/**
	 * 修改人ID
	 */
	protected String modifyId;
	
	/**
	 * 修改人名称
	 */
	protected String modifyName;
	
	/**
	 * 修改时间
	 */
	protected String modifyTime;
	
	/**
	 * 复核人ID
	 */
	protected String checkId;
	
	/**
	 * 复核人名称
	 */
	protected String checkName;

	/**
	 * 复核时间
	 */
	protected String checkTime;
	
	/**
	 * 当前操作人（权限相关）
	 */
	protected String empId;
	
	
	
	
	
	
	/**
	 * 数据状态
	 */
	public String getStatus() {
		return status;
	}
	
	/**
	 * 数据状态
	 */
	public void setStatus(String status) {
		this.status = status;
	}
	
	/**
	 * 数据状态中文名称
	 */
	public String getStatusName() {
		return statusName;
	}
	
	/**
	 * 数据状态中文名称
	 */
	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	
	/**
	 * 创建人ID
	 */
	public String getCreateId() {
		return createId;
	}
	
	/**
	 * 创建人ID
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}
	
	/**
	 * 创建人名称
	 */
	public String getCreateName() {
		if (StringUtils.isNotBlank(createId)) {
			return EmployeeCached.getName(createId);
		}
		return createName;
	}

	/**
	 * 创建人名称
	 */
	public void setCreateName(String createName) {
		this.createName = createName;
	}

	/**
	 * 创建时间
	 */
	public String getCreateTime() {
		return createTime;
	}
	
	/**
	 * 创建时间
	 */
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	/**
	 * 修改人ID
	 */
	public String getModifyId() {
		return modifyId;
	}
	
	/**
	 * 修改人ID
	 */
	public void setModifyId(String modifyId) {
		this.modifyId = modifyId;
	}
	
	/**
	 * 修改人名称
	 */
	public String getModifyName() {
		if (StringUtils.isNotBlank(modifyId)) {
			return EmployeeCached.getName(modifyId);
		}
		return modifyName;
	}

	/**
	 * 修改人名称
	 */
	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}

	/**
	 * 修改时间
	 */
	public String getModifyTime() {
		return modifyTime;
	}
	
	/**
	 * 修改时间
	 */
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	/**
	 * 复核人ID
	 */
	public String getCheckId() {
		return checkId;
	}
	
	/**
	 * 复核人ID
	 */
	public void setCheckId(String checkId) {
		this.checkId = checkId;
	}
	
	/**
	 * 复核人名称
	 */
	public String getCheckName() {
		if (StringUtils.isNotBlank(checkId)) {
			return EmployeeCached.getName(checkId);
		}
		return checkName;
	}

	/**
	 * 复核人名称
	 */
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	/**
	 * 复核时间
	 */
	public String getCheckTime() {
		return checkTime;
	}
	
	/**
	 * 复核时间
	 */
	public void setCheckTime(String checkTime) {
		this.checkTime = checkTime;
	}

	/**
	 * 当前操作人（权限相关）
	 */
	public String getEmpId() {
		return empId;
	}

	/**
	 * 当前操作人（权限相关）
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
}
