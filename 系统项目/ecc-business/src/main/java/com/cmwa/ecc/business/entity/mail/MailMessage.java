package com.cmwa.ecc.business.entity.mail;

import java.io.Serializable;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * 邮件封装
 * 
 * @author ex-luoxy@cmfchina.com
 */
public class MailMessage implements Serializable {
	
	/**
	 * @param to
	 * @param cc
	 * @param subject
	 * @param content
	 * @param affixList
	 * @param confId
	 */
	public MailMessage(String[] to, String[] cc, String subject, String content, List<SimpleAttachEntry> affixList, String confId) {
		super();
		this.to = to;
		this.cc = cc;
		this.subject = subject;
		this.content = content;
		this.affixList = affixList;
		this.confId = confId;
	}
	
	
	/**
	 * @param to
	 * @param cc
	 * @param subject
	 * @param content
	 * @param confId
	 */
	public MailMessage(String[] to, String[] cc, String subject, String content, String confId) {
		super();
		this.to = to;
		this.cc = cc;
		this.subject = subject;
		this.content = content;
		this.confId = confId;
	}
	
	
	/**
	 * @param to
	 * @param cc
	 * @param scc
	 * @param subject
	 * @param content
	 * @param confId
	 */
	public MailMessage(String[] to, String[] cc,String[] scc, String subject, String content, String confId) {
		super();
		this.to = to;
		this.cc = cc;
		this.scc= scc;
		this.subject = subject;
		this.content = content;
		this.confId = confId;
	}
	
	/**
	 * @param to
	 * @param cc
	 * @param scc
	 * @param subject
	 * @param content
	 * @param affixList
	 * @param confId
	 */
	public MailMessage(String[] to, String[] cc,String[] scc, String subject, String content, List<SimpleAttachEntry> affixList, String confId) {
		super();
		this.to = to;
		this.cc = cc;
		this.scc = scc;
		this.subject = subject;
		this.content = content;
		this.affixList = affixList;
		this.confId = confId;
	}
	
	/**
	 * 
	 */
	public MailMessage() {
		super();
	}
	
	private static final long		serialVersionUID	= 1L;
	/**
	 * 发件人(此项系统已默认)
	 */
	private String					from;
	/**
	 * 收件人
	 */
	private String[]				to = new String[0];
	/**
	 * 抄送人
	 */
	private String[]				cc = new String[0];
	/**
	 * 主题
	 */
	private String					subject;
	/**
	 * 主体内容
	 */
	private String					content;
	/**
	 * 鉴权(改变发件人需要)
	 */
	private MailAuthenticator		authenticator;
	/**
	 * 邮件写的路径
	 */
	private String					mailFilePath;
	/**
	 * 附件
	 */
	private List<SimpleAttachEntry>	affixList			= new LinkedList<SimpleAttachEntry>();
	/**
	 * 配置表
	 */
	private String					confId;
	
	private String prefix;
	
	/**
	 * 密送人
	 */
	private String[]				scc = new String[0];
	
	
	public String[] getScc() {
		return scc;
	}


	public void setScc(String[] scc) {
		this.scc = scc;
	}


	public String getPrefix() {
		return prefix;
	}


	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}


	public String getFrom() {
		return from;
	}
	
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	
	public String[] getTo() {
		return to;
	}
	
	
	public void setTo(String to) {
		this.to = new String [] {to};
	}
	
	
	public void setTo(String[] to) {
		this.to = to;
	}
	
	
	public String[] getCc() {
		return cc;
	}
	
	
	public void setCc(String cc) {
		this.cc = new String [] {cc};
	}
	
	
	public void setCc(String[] cc) {
		this.cc = cc;
	}
	
	
	public String getSubject() {
		return subject;
	}
	
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	
	public String getContent() {
		return content;
	}
	
	
	public void setContent(String content) {
		this.content = content;
	}
	
	
	public MailAuthenticator getAuthenticator() {
		return authenticator;
	}
	
	
	public void setAuthenticator(MailAuthenticator authenticator) {
		this.authenticator = authenticator;
	}
	
	
	public String getMailFilePath() {
		return mailFilePath;
	}
	
	
	public void setMailFilePath(String mailFilePath) {
		this.mailFilePath = mailFilePath;
	}
	
	
	public List<SimpleAttachEntry> getAffixList() {
		return affixList;
	}
	
	
	public void setAffixList(List<SimpleAttachEntry> affixList) {
		this.affixList = affixList;
	}
	
	
	public String getConfId() {
		return confId;
	}
	
	
	public void setConfId(String confId) {
		this.confId = confId;
	}
	
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "MailMessage [from=" + from + ", to=" + Arrays.toString(to) + ", cc=" + Arrays.toString(cc) + ", scc="  + Arrays.toString(scc) + ", subject=" + subject + ", content=" + content + ", authenticator=" + authenticator + ", mailFilePath=" + mailFilePath + ", affixList=" + affixList + ", confId=" + confId + "]";
	}
	
}
