package com.cmwa.ecc.business.entity.mail;

public class MailAppendDetail extends MailAppend {
	
	private String	val;
	
	/**
	 * 中文
	 */
	private String valNm;
	
	/**
	 * 邮箱地址
	 */
	private String mailPath;
	
	public String getVal() {
		return val;
	}
	
	
	public void setVal(String val) {
		this.val = val;
	}
	
	public String getValNm() {
		return valNm;
	}


	public void setValNm(String valNm) {
		this.valNm = valNm;
	}

	public String getMailPath() {
		return mailPath;
	}


	public void setMailPath(String mailPath) {
		this.mailPath = mailPath;
	}

	@Override
	public String toString() {
		return "MailAppendDetail [val=" + val + ", valNm=" + valNm
				+ ", mailPath=" + mailPath + "]";
	}
	
}
