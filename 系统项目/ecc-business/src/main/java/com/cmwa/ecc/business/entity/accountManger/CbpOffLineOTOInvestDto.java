package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

/**
 * 从CBP获取的线下一对一 客户资料信息
 * @author ex-liuy
 *
 */
public class CbpOffLineOTOInvestDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public CbpOffLineOTOInvestDto() {
		super();
	}
	
	private String offInvSerialno; //委托人流水号
	private String offInvName; //委托人名称
	private String offInvType;//委托人类型
	private String offInvIdtp;//委托人证件类型
	private String offInvIdno;//委托人证件号码
	private String offCounterIdNo;//线下柜台证件号码
	private String attr1;//预留字段1
	private String attr2;//预留字段2
	private String attr3;//预留字段3

	
	public String getOffCounterIdNo() {
		return offCounterIdNo;
	}


	public void setOffCounterIdNo(String offCounterIdNo) {
		this.offCounterIdNo = offCounterIdNo;
	}


	public String getOffInvSerialno() {
		return offInvSerialno;
	}


	public void setOffInvSerialno(String offInvSerialno) {
		this.offInvSerialno = offInvSerialno;
	}


	public String getOffInvName() {
		return offInvName;
	}


	public void setOffInvName(String offInvName) {
		this.offInvName = offInvName;
	}


	public String getOffInvType() {
		return offInvType;
	}


	public void setOffInvType(String offInvType) {
		this.offInvType = offInvType;
	}


	public String getOffInvIdtp() {
		return offInvIdtp;
	}


	public void setOffInvIdtp(String offInvIdtp) {
		this.offInvIdtp = offInvIdtp;
	}


	public String getOffInvIdno() {
		return offInvIdno;
	}


	public void setOffInvIdno(String offInvIdno) {
		this.offInvIdno = offInvIdno;
	}


	public String getAttr1() {
		return attr1;
	}


	public void setAttr1(String attr1) {
		this.attr1 = attr1;
	}


	public String getAttr2() {
		return attr2;
	}


	public void setAttr2(String attr2) {
		this.attr2 = attr2;
	}


	public String getAttr3() {
		return attr3;
	}


	public void setAttr3(String attr3) {
		this.attr3 = attr3;
	}


	public String toString() {
		return "CbpOffLineInvestDto [offInvSerialno=" + offInvSerialno
				+ ", offInvName=" + offInvName + ", offInvType=" + offInvType
				+ ", offInvIdtp=" + offInvIdtp + ", offInvIdno=" + offInvIdno
				+ ", attr1=" + attr1 + ", attr2=" + attr2 + ", attr3=" + attr3
				+ "]";
	}
}
