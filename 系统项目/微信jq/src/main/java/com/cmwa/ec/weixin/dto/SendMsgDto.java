package com.cmwa.ec.weixin.dto;

import java.io.Serializable;

public class SendMsgDto implements Serializable{

	private static final long serialVersionUID = 3364382714519152955L;
	private String custNo;
    private String tradeacco;
	private String msgtemplateid;
	private String msgtemplatename;
	private String sendcont;
	private String fundid;
	private String fundname;
	private String username;
	private String bhands;
	private String tel;
	private String serialno;
	private String bhandsname;
	public String getBhandsname() {
		return bhandsname;
	}
	public void setBhandsname(String bhandsname) {
		this.bhandsname = bhandsname;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getTradeacco() {
		return tradeacco;
	}
	public void setTradeacco(String tradeacco) {
		this.tradeacco = tradeacco;
	}
	public String getMsgtemplateid() {
		return msgtemplateid;
	}
	public void setMsgtemplateid(String msgtemplateid) {
		this.msgtemplateid = msgtemplateid;
	}
	public String getSendcont() {
		return sendcont;
	}
	public void setSendcont(String sendcont) {
		this.sendcont = sendcont;
	}
	public String getFundid() {
		return fundid;
	}
	public void setFundid(String fundid) {
		this.fundid = fundid;
	}
	public String getFundname() {
		return fundname;
	}
	public void setFundname(String fundname) {
		this.fundname = fundname;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getBhands() {
		return bhands;
	}
	public void setBhands(String bhands) {
		this.bhands = bhands;
	}
	public String getMsgtemplatename() {
		return msgtemplatename;
	}
	public void setMsgtemplatename(String msgtemplatename) {
		this.msgtemplatename = msgtemplatename;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getSerialno() {
		return serialno;
	}
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
	
	@Override
	public String toString() {
		return "SendMsgDto [custNo=" + custNo + ", tradeacco=" + tradeacco
				+ ", msgtemplateid=" + msgtemplateid + ", msgtemplatename="
				+ msgtemplatename + ", sendcont=" + sendcont + ", fundid="
				+ fundid + ", fundname=" + fundname + ", username=" + username
				+ ", bhands=" + bhands + ", tel=" + tel + ", serialno="
				+ serialno + ", bhandsname=" + bhandsname + "]";
	}
}
