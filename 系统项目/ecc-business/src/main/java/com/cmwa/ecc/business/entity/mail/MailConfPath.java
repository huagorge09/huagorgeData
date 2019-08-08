package com.cmwa.ecc.business.entity.mail;

import java.sql.Date;

public class MailConfPath {
	private String mailId;    //'主键（序列号：SEQ_T_MAIL_CONF_MAILPATH_ID）'                 
	private String confId;    //'收件人配置表ID';   
	private String mailTo;    //'手动增加收件人';    
	private String mailCc;    //'手动增加抄送人';    
	private Date createDate;  // '创建时间';   
	private Date modifyDate;  //'修改时间';   
	private String mailScc;    //'手动增加密件抄送人';    
	                                           
	public String getMailScc() {
		return mailScc;
	}
	public void setMailScc(String mailScc) {
		this.mailScc = mailScc;
	}
	public String getMailId() {
		return mailId;
	}
	public void setMailId(String mailId) {
		this.mailId = mailId;
	}
	public String getConfId() {
		return confId;
	}
	public void setConfId(String confId) {
		this.confId = confId;
	}
	public String getMailTo() {
		return mailTo;
	}
	public void setMailTo(String mailTo) {
		this.mailTo = mailTo;
	}
	public String getMailCc() {
		return mailCc;
	}
	public void setMailCc(String mailCc) {
		this.mailCc = mailCc;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	
	
}
