package com.cmwa.ecc.business.entity.mail;

/**
 * 配置类型
 * 
 * @author ex-luoxy@cmfchina.com
 */
public enum MailCategory {
	
	/**
	 * 收件人
	 */
	TO("to"),
	
	/**
	 * 抄送人
	 */
	CC("cc"),
	
	/**
	 * 密送人
	 */
	SCC("scc");
	
	private String	val;
	
	
	private MailCategory(String val) {
		this.val = val;
	}
	
	
	public String getVal() {
		return val;
	}
	
}
