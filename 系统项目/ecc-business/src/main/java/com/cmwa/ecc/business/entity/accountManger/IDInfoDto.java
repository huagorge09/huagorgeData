package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("IDInfoDto")
public class IDInfoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String idtp; // 证件类型
	private String idno; // 证件号码
	private String idvalidate; // 证件有效期

	public IDInfoDto() {}

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

}
