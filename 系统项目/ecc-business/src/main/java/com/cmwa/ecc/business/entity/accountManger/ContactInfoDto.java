package com.cmwa.ecc.business.entity.accountManger;

import org.apache.ibatis.type.Alias;

@Alias("contactInfoDto")
public class ContactInfoDto {
	private String contactgrant; // 经办人授权
	private String contact; // 经办人
	private String contactnation; // 经办人国籍
	private String contidtp; // 经办人证件类型
	private String contidtpnm; // 经办人证件类型
	private String contidno; // 经办人证件号码
	private String contvalidate; // 经办人证件有效期
	private String contphone; // 经办人电话
	private String contfax; // 经办人传真
	private String contmobile; // 经办人手机
	private String contemail; // 经办人Email
	private String contactflag; // 主经办人标志
	private String contAddr;		//经办人联系地址
	private String contPostcode;	//经办人邮编
	private String infoflag;
	
	private String mainbrokerflag;
	
	private String idtp; // 证件类型
	private String idno; // 证件号码
	private String idvalidate; // 证件有效期

	public String getIdtp() {
		return idtp;
	}

	public void setIdtp(String idtp) {
		this.idtp = idtp;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getIdvalidate() {
		return idvalidate;
	}

	public void setIdvalidate(String idvalidate) {
		this.idvalidate = idvalidate;
	}

	public String getContactgrant() {
		return contactgrant;
	}

	public void setContactgrant(String contactgrant) {
		this.contactgrant = contactgrant;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactnation() {
		return contactnation;
	}

	public void setContactnation(String contactnation) {
		this.contactnation = contactnation;
	}

	public String getContidtp() {
		return contidtp;
	}

	public void setContidtp(String contidtp) {
		this.contidtp = contidtp;
	}

	public String getContidno() {
		return contidno;
	}

	public void setContidno(String contidno) {
		this.contidno = contidno;
	}

	public String getContvalidate() {
		return contvalidate;
	}

	public void setContvalidate(String contvalidate) {
		this.contvalidate = contvalidate;
	}

	public String getContphone() {
		return contphone;
	}

	public void setContphone(String contphone) {
		this.contphone = contphone;
	}

	public String getContfax() {
		return contfax;
	}

	public void setContfax(String contfax) {
		this.contfax = contfax;
	}

	public String getContmobile() {
		return contmobile;
	}

	public void setContmobile(String contmobile) {
		this.contmobile = contmobile;
	}

	public String getContemail() {
		return contemail;
	}

	public void setContemail(String contemail) {
		this.contemail = contemail;
	}

	public String getContactflag() {
		return contactflag;
	}

	public void setContactflag(String contactflag) {
		this.contactflag = contactflag;
	}

	public ContactInfoDto() {

	}

	public String getContAddr() {
		return contAddr;
	}

	public void setContAddr(String contAddr) {
		this.contAddr = contAddr;
	}

	public String getContPostcode() {
		return contPostcode;
	}

	public void setContPostcode(String contPostcode) {
		this.contPostcode = contPostcode;
	}

	public String getInfoflag() {
		return infoflag;
	}

	public void setInfoflag(String infoflag) {
		this.infoflag = infoflag;
	}

	public String getMainbrokerflag() {
		return mainbrokerflag;
	}

	public void setMainbrokerflag(String mainbrokerflag) {
		this.mainbrokerflag = mainbrokerflag;
	}

	public String getContidtpnm() {
		return contidtpnm;
	}

	public void setContidtpnm(String contidtpnm) {
		this.contidtpnm = contidtpnm;
	}
}
