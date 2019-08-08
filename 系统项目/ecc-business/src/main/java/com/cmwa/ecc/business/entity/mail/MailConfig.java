package com.cmwa.ecc.business.entity.mail;

/**
 * 邮件配置
 * 
 * @author ex-luoxy@cmfchina.com
 */
public class MailConfig {
	
	/**
	 * 
	 */
	public MailConfig() {
		super();
	}
	/**
	 * SEQ_MAIL_CONFIG
	 */
	private String	mcId;
	/**
	 * 分类
	 */
	private String	category;
	/**
	 * 头部
	 */
	private String	head;
	/**
	 * 邮件体
	 */
	private String	body;
	/**
	 * 创建时间
	 */
	private String	createDate;
	/**
	 * 修改时间
	 */
	private String	modifiDate;
	/**
	 * 备注
	 */
	private String	remark;
	
	/**
	 * 结果
	 */
	private String result;
	
	/**
	 * 收件人
	 */
	private String toList;
	
	/**
	 * 抄送人
	 */
	private String ccList;
	/**
	 * 运行状态
	 */
	private String runStatus;
	/**
	 * 运行状态 名称
	 */
	private String runStatusNM;
	/**
	 * 手动设置邮箱地址
	 */
	private String ccListAddr;
	
	/**
	 * 密件抄送人
	 */
    private String sccList;
    
    /**
	 * 手动设置密件抄送人邮箱地址
	 */
	private String sccListAddr;
	
	public String getSccListAddr() {
		return sccListAddr;
	}

	public void setSccListAddr(String sccListAddr) {
		this.sccListAddr = sccListAddr;
	}

	public String getSccList() {
		return sccList;
	}

	public void setSccList(String sccList) {
		this.sccList = sccList;
	}

	public String getRunStatus() {
		return runStatus;
	}

	public void setRunStatus(String runStatus) {
		this.runStatus = runStatus;
	}

	public String getRunStatusNM() {
		return runStatusNM;
	}

	public void setRunStatusNM(String runStatusNM) {
		this.runStatusNM = runStatusNM;
	}

	public String getMcId() {
		return mcId;
	}
	
	public void setMcId(String mcId) {
		this.mcId = mcId;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setCategory(String category) {
		this.category = category;
	}
	
	public String getHead() {
		return head;
	}
	
	public void setHead(String head) {
		this.head = head;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	
	public String getCreateDate() {
		return createDate;
	}
	
	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}
	
	public String getModifiDate() {
		return modifiDate;
	}
	
	public void setModifiDate(String modifiDate) {
		this.modifiDate = modifiDate;
	}
	
	public String getRemark() {
		return remark;
	}
	
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getToList() {
		return toList;
	}

	public void setToList(String toList) {
		this.toList = toList;
	}

	public String getCcList() {
		return ccList;
	}

	public void setCcList(String ccList) {
		this.ccList = ccList;
	}

	@Override
	public String toString() {
		return "MailConfig [mcId=" + mcId + ", category=" + category
				+ ", head=" + head + ", body=" + body + ", createDate="
				+ createDate + ", modifiDate=" + modifiDate + ", remark="
				+ remark + ", result=" + result + ", toList=" + toList
				+ ", ccList=" + ccList + "]";
	}


	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCcListAddr() {
		return ccListAddr;
	}

	public void setCcListAddr(String ccListAddr) {
		this.ccListAddr = ccListAddr;
	}
	
	
}
