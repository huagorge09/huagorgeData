package com.cmwa.ecc.business.entity.trade;

import org.apache.ibatis.type.Alias;


/**
 * 订单dto
 * @author ex-huangbl
 *
 */
@Alias("tradeOrderDto")
public class TradeOrderDto {
	private String custNo; //客户号
    private String custName;//客户姓名
    private String tradeAcco; //交易账号
    private String invtp;
	private String idno;
    private String idtp;
    private String fundacct;
    private String createtime;
    private String mobileNo;
    private String fundid;
    private String fundname;
    private String period;
    private String serialno;
    private String oldappno;
    private String typeId;
    private String subamt;
   
	
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getCustName() {
		return custName;
	}
	public void setCustName(String custName) {
		this.custName = custName;
	}
	public String getTradeAcco() {
		return tradeAcco;
	}
	public void setTradeAcco(String tradeAcco) {
		this.tradeAcco = tradeAcco;
	}
	public String getInvtp() {
		return invtp;
	}
	public void setInvtp(String invtp) {
		this.invtp = invtp;
	}
	public String getIdno() {
		return idno;
	}
	public void setIdno(String idno) {
		this.idno = idno;
	}
	public String getIdtp() {
		return idtp;
	}
	public void setIdtp(String idtp) {
		this.idtp = idtp;
	}
	public String getFundacct() {
		return fundacct;
	}
	public void setFundacct(String fundacct) {
		this.fundacct = fundacct;
	}
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public String getMobileNo() {
		return mobileNo;
	}
	public void setMobileNo(String mobileNo) {
		this.mobileNo = mobileNo;
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
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getSerialno() {
		return serialno;
	}
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
	public String getOldappno() {
		return oldappno;
	}
	public void setOldappno(String oldappno) {
		this.oldappno = oldappno;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getSubamt() {
		return subamt;
	}
	public void setSubamt(String subamt) {
		this.subamt = subamt;
	}
	@Override
	public String toString() {
		return "TradeOrderDto [custNo=" + custNo + ", custName=" + custName
				+ ", tradeAcco=" + tradeAcco + ", invtp=" + invtp + ", idno="
				+ idno + ", idtp=" + idtp + ", fundacct=" + fundacct
				+ ", createtime=" + createtime + ", mobileNo=" + mobileNo
				+ ", fundid=" + fundid + ", fundname=" + fundname + ", period="
				+ period + ", serialno=" + serialno + ", oldappno=" + oldappno
				+ ", typeId=" + typeId + ", subamt=" + subamt + "]";
	}
	@Override
	public boolean equals(Object obj) {
		if (this==obj) {
			return true;
		}
		if (obj==null || this==null) {
			return false;
		}
		TradeOrderDto objs=(TradeOrderDto) obj;
		String idno2 = this.getIdno();
		
		if (idno2.equals(objs.getIdno()) && this.getIdtp().equals(objs.getIdtp())) {
			return true;
		}
		if (this.getFundacct().equals(objs.getFundacct())) {
			return true;
		}
//		if(this.getFundid().equals(objs.getFundid()) && this.getSubamt().equals(objs.getSubamt())){
//			return true;
//		}
//		
		return false;
	}
    
}
