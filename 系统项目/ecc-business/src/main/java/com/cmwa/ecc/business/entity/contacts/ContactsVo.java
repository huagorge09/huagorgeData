package com.cmwa.ecc.business.entity.contacts;

import org.apache.ibatis.type.Alias;

import com.cmwa.ecc.business.commonVo.BaseVo;

@Alias("contactsVo")
public class ContactsVo extends BaseVo{
	
	/**
	 * 联系人 主键
	 */
	private String conId;
	
	/**
	 * 联系人名称
	 */
	private String conName;
	/**
	 *	公司 即 机构 
	 */
	private String conCompany;
	/**
	 * 公司名称
	 */
	private String conCompanyName;
	
	/**
	 * 联系人 部门
	 */
	private String conDept;
	
	/**
	 * 职务
	 */
	private String conPost;
	
	/**
	 * 手机
	 */
	private String conPhone;
	
	/**
	 * 电话
	 */
	private String conTel;
	
	/**
	 * QQ或者微信
	 */
	private String conWeChat;
	
	/**
	 * 邮箱地址 
	 */
	private String conEmail ;
	/**
	 * 扩展属性
	 */
	private String attr1;
	private String attr2;
	private String attr3;
	/**
	 * 创建人姓名
	 */
	private String createName;
	/**
	 * 修改人姓名
	 */
	private String modifyName;
	
	public ContactsVo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getConCompany() {
		return conCompany;
	}

	public void setConCompany(String conCompany) {
		this.conCompany = conCompany;
	}

	public String getConCompanyName() {
		return conCompanyName;
	}

	public void setConCompanyName(String conCompanyName) {
		this.conCompanyName = conCompanyName;
	}

	public String getConId() {
		return conId;
	}

	public void setConId(String conId) {
		this.conId = conId;
	}

	public String getConName() {
		return conName;
	}

	public void setConName(String conName) {
		this.conName = conName;
	}

	public String getConDept() {
		return conDept;
	}

	public void setConDept(String conDept) {
		this.conDept = conDept;
	}

	public String getConPost() {
		return conPost;
	}

	public void setConPost(String conPost) {
		this.conPost = conPost;
	}

	public String getConPhone() {
		return conPhone;
	}

	public void setConPhone(String conPhone) {
		this.conPhone = conPhone;
	}

	public String getConTel() {
		return conTel;
	}

	public void setConTel(String conTel) {
		this.conTel = conTel;
	}

	public String getConWeChat() {
		return conWeChat;
	}

	public void setConWeChat(String conWeChat) {
		this.conWeChat = conWeChat;
	}

	public String getConEmail() {
		return conEmail;
	}

	public void setConEmail(String conEmail) {
		this.conEmail = conEmail;
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

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getModifyName() {
		return modifyName;
	}

	public void setModifyName(String modifyName) {
		this.modifyName = modifyName;
	}
	
}
