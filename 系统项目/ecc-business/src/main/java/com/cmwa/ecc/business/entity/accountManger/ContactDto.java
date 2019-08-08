package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 经办人信息实体类
 * @author ex-chenbq
 *
 */
@Alias("contactDto")
public class ContactDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String contact; 		//经办人
	private String contphone; 		//经办人电话
	private String contidtp; 		//经办人证件类型
	private String contidtpName; 		//经办人证件类型
	private String contidno; 		//经办人证件号码
	private String contvalidate; 	//经办人证件有效期
	private String contfax;			//经办人传真

    /**
     * 构造函数
     * @param brokerno String 经济人代码
     */
    public ContactDto() {
    }

	public String getContidtpName() {
		return contidtpName;
	}

	public void setContidtpName(String contidtpName) {
		this.contidtpName = contidtpName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact == null ? "" : contact;
	}

	public String getContphone() {
		return contphone;
	}

	public void setContphone(String contphone) {
		this.contphone = contphone == null ? "" : contphone;
	}

	public String getContidtp() {
		return contidtp;
	}

	public void setContidtp(String contidtp) {
		this.contidtp = contidtp == null ? "" : contidtp;
	}

	public String getContidno() {
		return contidno;
	}

	public void setContidno(String contidno) {
		this.contidno = contidno == null ? "" : contidno;
	}

	public String getContvalidate() {
		return contvalidate;
	}

	public void setContvalidate(String contvalidate) {
		this.contvalidate = contvalidate == null ? "" : contvalidate;
	}

	public String getContfax() {
		return contfax;
	}

	public void setContfax(String contfax) {
		this.contfax = contfax == null ? "" : contfax;
	}

}
