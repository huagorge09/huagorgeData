package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 
 * <p>
 * Title: 客户基本信息DTO
 * </p>
 * 
 * <p>
 * Description: 为AccountDto的关联类，一个AccountDto关联一个InvestorInfoDto
 * </p>
 * 
 * <p>
 * Copyright: Copyright (c) 2008
 * </p>
 * 
 * <p>
 * Company: Legion Technology
 * </p>
 * 
 * @author xuhw
 * @version 1.0
 */
@Alias("investorInfoDto")
public class InvestorInfoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String invnm; // 投资人
	private String sex; // 性别
	private String birthday; // 出生日期
	private String marriage; // 婚姻状况，Y--已婚，N--未婚
	private String vocation; // 职业
	private String income; // 年收入
	private String education; // 受教育状况
	private String invest; // 有无证券投资经历,Y--有，N--无
	private AddressDto address; // 客户通讯信息
	private String idnoLimit; // 证件有效期
	private String nation; // 国籍

	public InvestorInfoDto() {
	}

	/**
	 * 设置姓名
	 * 
	 * @param invnm
	 *            String
	 */
	public void setInvnm(String invnm) {
		this.invnm = invnm;
	}

	/**
	 * 设置性别
	 * 
	 * @param sex
	 *            String
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * 设置生日
	 * 
	 * @param birthday
	 *            String
	 */
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	/**
	 * 设置婚姻状况
	 * 
	 * @param marriage
	 *            String
	 */
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}

	/**
	 * 设置职业
	 * 
	 * @param vocation
	 *            String
	 */
	public void setVocation(String vocation) {
		this.vocation = vocation;
	}

	/**
	 * 设置年收入
	 * 
	 * @param income
	 *            String
	 */
	public void setIncome(String income) {
		this.income = income;
	}

	/**
	 * 设置学历
	 * 
	 * @param education
	 *            String
	 */
	public void setEducation(String education) {
		this.education = education;
	}

	/**
	 * 设置投资经验
	 * 
	 * @param invest
	 *            String
	 */
	public void setInvest(String invest) {
		this.invest = invest;
	}

	/**
	 * 设置通讯信息
	 * 
	 * @param address
	 *            AddressDto
	 */
	public void setAddress(AddressDto address) {
		this.address = address;
	}

	public String getInvnm() {
		return invnm;
	}

	public String getSex() {
		return sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public String getMarriage() {
		return marriage;
	}

	public String getVocation() {
		return vocation;
	}

	public String getIncome() {
		return income;
	}

	public String getEducation() {
		return education;
	}

	public String getInvest() {
		return invest;
	}

	public AddressDto getAddress() {
		return address;
	}

	public String getIdnoLimit() {
		return idnoLimit;
	}

	public void setIdnoLimit(String idnoLimit) {
		this.idnoLimit = idnoLimit;
	}

	public String getNation() {
		return nation;
	}

	public void setNation(String nation) {
		this.nation = nation;
	}
}
