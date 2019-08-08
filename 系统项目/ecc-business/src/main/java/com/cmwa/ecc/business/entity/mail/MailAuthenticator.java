package com.cmwa.ecc.business.entity.mail;

/**
 * 发件人
 *
 * @author ex-luoxy@cmfchina.com
 *
 */
public class MailAuthenticator {
	
	/**
	 * @param host
	 * @param from
	 * @param username
	 * @param password
	 */
	public MailAuthenticator(String host, String from, String username, String password) {
		super();
		this.host = host;
		this.from = from;
		this.username = username;
		this.password = password;
	}
	
	private String				host;
	
	private String				from;
	
	/**
	 * 用户名（登录邮箱）
	 */
	private String				username;
	
	/**
	 * 密码
	 */
	private transient String	password;
	
	
	public String getHost() {
		return host;
	}
	
	
	public void setHost(String host) {
		this.host = host;
	}
	
	
	public String getUsername() {
		return username;
	}
	
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	
	public String getPassword() {
		return password;
	}
	
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	public String getFrom() {
		return from;
	}
	
	
	public void setFrom(String from) {
		this.from = from;
	}
	
	
	@Override
	public String toString() {
		return "MailAuthenticator [host=" + host + ", from=" + from + ", username=" + username + ", password=****** ]";
	}
}
