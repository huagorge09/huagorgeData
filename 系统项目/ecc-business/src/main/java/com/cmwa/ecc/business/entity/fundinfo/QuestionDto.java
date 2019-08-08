package com.cmwa.ecc.business.entity.fundinfo;

import org.apache.ibatis.type.Alias;

@Alias("questionDto")
public class QuestionDto {
	
	private String wentId;
	private String id;
	private String wenTi;
	private String daAn;
	private String daAn_div;
	private String fundcode;
	private int period;
	
	public String getFundcode() {
		return fundcode;
	}
	
	public String getDaAn_div() {
		return daAn_div;
	}

	public void setDaAn_div(String daAn_div) {
		this.daAn_div = daAn_div;
	}

	public void setFundcode(String fundcode) {
		this.fundcode = fundcode;
	}
	public String getWentId() {
		return wentId;
	}
	public void setWentId(String wentId) {
		this.wentId = wentId;
	}
	public String getWenTi() {
		return wenTi;
	}
	public void setWenTi(String wenTi) {
		this.wenTi = wenTi;
	}
	public String getDaAn() {
		return daAn;
	}
	public void setDaAn(String daAn) {
		this.daAn = daAn;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "QuestionDto [wentId=" + wentId + ", id=" + id + ", wenTi="
				+ wenTi + ", daAn=" + daAn + ", daAn_div=" + daAn_div
				+ ", fundcode=" + fundcode + "]";
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}
	
	

}
