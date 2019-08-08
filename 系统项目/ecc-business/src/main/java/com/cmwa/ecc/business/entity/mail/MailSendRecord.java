package com.cmwa.ecc.business.entity.mail;

import java.util.Arrays;

import org.springframework.util.StringUtils;

import com.cmwa.ecc.business.utils.NetUtil;

/**
 * 邮件发送记录
 * 
 * @author ex-luoxy@cmfchina.com
 */
public class MailSendRecord {
	
	/**
	 * ID
	 */
	private String	msrId;
	
	private String	confId;
	
	/**
	 * 发件人
	 */
	private String	from;
	
	/**
	 * 收件人
	 */
	private String	to;
	
	/**
	 * 抄送人
	 */
	private String	cc;
	
	/**
	 * 主题
	 */
	private String	subject;
	
	/**
	 * 内容
	 */
	private String	content;
	
	/**
	 * 附件
	 */
	private String	attachs;
	
	/**
	 * 服务ip
	 */
	private String	serverIp;
	
	/**
	 * 地址
	 */
	private String	serverName;
	
	/**
	 * 日期
	 */
	private String	time;
	
	/**
	 * 创建时间
	 */
	private String	createDate;
	
	/**
	 * 结果(是否成功)
	 */
	private String	result;
	
	/**
	 * 密件抄送人
	 */
	private String	scc;
	
	
	

	public MailSendRecord(MailMessage mailMessage) throws Exception {
		this.confId = StringUtils.isEmpty(mailMessage.getConfId())?"DEF" : mailMessage.getConfId();
		this.from = mailMessage.getFrom();
		this.to = Arrays.toString(mailMessage.getTo());
		this.cc = Arrays.toString(mailMessage.getCc());
		this.subject = mailMessage.getSubject();
		this.content = mailMessage.getContent();
		this.attachs = mailMessage.getAffixList().toString();
		this.serverIp = NetUtil.getLocalHostIp();
		this.serverName = NetUtil.getLocalHostName();
		this.scc = Arrays.toString(mailMessage.getScc() == null ? new String[]{} : mailMessage.getScc());
	}
	
	
	public String getMsrId() {
		return msrId;
	}
	
	
	public void setMsrId(String msrId) {
		this.msrId = msrId;
	}
	
	
	public String getConfId() {
		return confId;
	}
	
	
	public void setConfId(String confId) {
		this.confId = confId;
	}
	
	
	public String getFrom() {
		return from;
	}
	
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	
	public String getTo() {
		return to;
	}
	
	
	public void setTo(String to) {
		this.to = to;
	}
	
	
	public String getCc() {
		return cc;
	}
	
	
	public void setCc(String cc) {
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
	
	
	public String getAttachs() {
		return attachs;
	}
	
	
	public void setAttachs(String attachs) {
		this.attachs = attachs;
	}
	
	
	public String getServerIp() {
		return serverIp;
	}
	
	
	public void setServerIp(String serverIp) {
		this.serverIp = serverIp;
	}
	
	
	public String getServerName() {
		return serverName;
	}
	
	
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}
	
	
	public String getTime() {
		return time;
	}
	
	
	public void setTime(String time) {
		this.time = time;
	}
	
	
	public String getCreateDate() {
		return createDate;
	}
	
	
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	
	public String getResult() {
		return result;
	}
	
	
	public void setResult(String result) {
		this.result = result;
	}
	public String getScc() {
		return scc;
	}


	public void setScc(String scc) {
		this.scc = scc;
	}

	
	
	@Override
	public String toString() {
		return "MailSendRecord [msrId=" + msrId + ", confId=" + confId + ", from=" + from + ", to=" + to + ", cc=" + cc + ",scc=" + scc +  ", subject=" + subject + ", content=" + content + ", attachs=" + attachs + ", serverIp=" + serverIp + ", serverName=" + serverName + ", time=" + time + ", createDate=" + createDate + ", result=" + result + "]";
	}
	
}
