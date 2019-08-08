package com.cmwa.ecc.business.entity.mail;

/**
 * 邮件附加信息
 * 
 * @author ex-luoxy@cmfchina.com
 */
public class MailAppend {
	
	/**
	 * 
	 */
	public MailAppend() {
		super();
	}
	
	protected String	appendId;
	
	protected String	sourceId;   //源(目前指的用户ID)
	
	protected String	category;	// to cc (收件人|抄送人)
									
	protected String	configId;   //配置表的ID
	
	
	public String getAppendId() {
		return appendId;
	}
	
	
	public void setAppendId(String appendId) {
		this.appendId = appendId;
	}
	
	
	public String getSourceId() {
		return sourceId;
	}
	
	
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	
	
	public String getCategory() {
		return category;
	}
	
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	
	public String getConfigId() {
		return configId;
	}
	
	
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	
	
	@Override
	public String toString() {
		return "MailAppend [appendId=" + appendId + ", sourceId=" + sourceId + ", category=" + category + ", configId=" + configId + "]";
	}
	
}
