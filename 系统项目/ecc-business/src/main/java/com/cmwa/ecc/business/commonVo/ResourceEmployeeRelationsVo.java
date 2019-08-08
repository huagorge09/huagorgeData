package com.cmwa.ecc.business.commonVo;

import org.apache.ibatis.type.Alias;

/**
 * 资源-员工关系实体
 * @TODO	
 * @author ex-liuy
 * @createDate 2017年6月6日
 */
@Alias("resourceEmployeeRelationsVo")
public class ResourceEmployeeRelationsVo extends BaseVo {
	/**
	 * 资源id
	 */
	private String resId;
	/**
	 * 员工id
	 */
	private String empId;
	
	public ResourceEmployeeRelationsVo() {
		super();
		// TODO Auto-generated constructor stub
	}
	public String getResId() {
		return resId;
	}
	public void setResId(String resId) {
		this.resId = resId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	
}
