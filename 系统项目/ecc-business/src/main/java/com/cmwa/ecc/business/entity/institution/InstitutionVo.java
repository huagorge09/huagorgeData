package com.cmwa.ecc.business.entity.institution;

import org.apache.ibatis.type.Alias;

import com.cmwa.ecc.business.commonVo.BaseVo;


/**
 * @author liuy
 * @date 创建时间：2017年5月9日 下午3:24:41
 * @description 机构信息表Vo
 */
@Alias("institutionVo")
public class InstitutionVo extends BaseVo {

	/**
	 * 机构ID
	 */
	private String instId;
	
	/**
	 * 机构简称
	 */
	private String instSName;
	
	/**
	 * 机构全称  
	 */	

	private String instLName;
	/**
	 * 机构类型
	 */
	private String instType;
	
	/**
	 * 机构类型名称
	 */
	private String instTypeNM;
	
	/**
	 * 父级机构
	 */
	private String instParentId;
	
	/**			附加信息			*/
	private String addId;
	/**
	 * 地址
	 */
	private String instAddress;
	
	/**
	 * 营业执照编码
	 */
	private String businessLicenceCode;
	
	/**
	 * 扩展属性
	 */
	private String attr1;
	
	/**
	 * 扩展属性
	 */
	private String attr2;
	
	/**
	 * 扩展属性
	 */
	private String attr3;

	public InstitutionVo() {
		super();
	}
	
	public String getInstTypeNM() {
		return instTypeNM;
	}

	public void setInstTypeNM(String instTypeNM) {
		this.instTypeNM = instTypeNM;
	}

	public String getAddId() {
		return addId;
	}

	public void setAddId(String addId) {
		this.addId = addId;
	}

	public String getInstParentId() {
		return instParentId;
	}

	public void setInstParentId(String instParentId) {
		this.instParentId = instParentId;
	}

	public String getInstId() {
		return instId;
	}

	public void setInstId(String instId) {
		this.instId = instId;
	}

	public String getInstSName() {
		return instSName;
	}

	public void setInstSName(String instSName) {
		this.instSName = instSName;
	}

	public String getInstLName() {
		return instLName;
	}

	public void setInstLName(String instLName) {
		this.instLName = instLName;
	}

	public String getInstType() {
		return instType;
	}

	public void setInstType(String instType) {
		this.instType = instType;
	}

	public String getInstAddress() {
		return instAddress;
	}

	public void setInstAddress(String instAddress) {
		this.instAddress = instAddress;
	}

	public String getBusinessLicenceCode() {
		return businessLicenceCode;
	}

	public void setBusinessLicenceCode(String businessLicenceCode) {
		this.businessLicenceCode = businessLicenceCode;
	}

	public String getAttr1() {
		return attr1;
	}

	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}

	public String getAttr2() {
		return attr2;
	}

	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}

	public String getAttr3() {
		return attr3;
	}

	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}
	
}
