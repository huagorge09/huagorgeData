package com.cmwa.ecc.business.entity.accountManger;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.ibatis.type.Alias;

@Alias("custInfoDto")
public class CustInfoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String custno;
	private String invtp;
	private String invnm;
	private String idtp;
	private String idno;
	private String mobile;
	private String email;
	private String custst;
	private String passwd;
	private Date date;
	private Integer passwderr;
	private String opendt;
	private Timestamp inserttimestamp;
	private Timestamp updatetimestamp;
	private String fundacct;// 基金账号
	
	public String getFundacct() {
		return fundacct;
	}
	public void setFundacct(String fundacct) {
		this.fundacct = fundacct;
	}
	public String getCustno() {
		return custno;
	}
	public void setCustno(String custno) {
		this.custno = custno;
	}
	public String getInvtp() {
		return invtp;
	}
	public void setInvtp(String invtp) {
		this.invtp = invtp;
	}
	public String getInvnm() {
		return invnm;
	}
	public void setInvnm(String invnm) {
		this.invnm = invnm;
	}
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
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCustst() {
		return custst;
	}
	public void setCustst(String custst) {
		this.custst = custst;
	}
	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getPasswderr() {
		return passwderr;
	}
	public void setPasswderr(Integer passwderr) {
		this.passwderr = passwderr;
	}
	public String getOpendt() {
		return opendt;
	}
	public void setOpendt(String opendt) {
		this.opendt = opendt;
	}
	public Timestamp getInserttimestamp() {
		return inserttimestamp;
	}
	public void setInserttimestamp(Timestamp inserttimestamp) {
		this.inserttimestamp = inserttimestamp;
	}
	public Timestamp getUpdatetimestamp() {
		return updatetimestamp;
	}
	public void setUpdatetimestamp(Timestamp updatetimestamp) {
		this.updatetimestamp = updatetimestamp;
	}
	
	
}
