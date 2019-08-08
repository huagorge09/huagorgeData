package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;
@Alias("bankAccoInfoDto")
public class BankAccoInfoDto implements Serializable{
	  private static final long serialVersionUID = 1L;
	  private String tradeacco;           //交易账号
	  private String custNo;              //客户编号
	  private String bankNo;              //银行编号
	  private String bankLongName;        //银行开户行名称
	  private String bankAcco;            //银行帐号      
	  private String bankAcnm;            //银行户名
	  private String bankIdtp;            //银行证件类型
	  private String bankIdno;            //银行证件号码
	  private String bankAccoDisplay;     //银行提示账户号
	  private String provinceCode;        //省份代码
	  private String insertTimeStamp;     //记录时间戳
	  private String updateTimeStamp;     //更新时间戳
	  private String confidentialDoc_Del; //密函编号
	  private String bankAddr;            //开户地(省)
	  private String bankAddrCity;        //开户地(市)
	  private String realBankNo;          //实际支付银行代码
	  private String mobile;			  //绑定手机号
	  private String fdacst;			  //交易账户状态
	  private String opendt;			  //交易账号开户日期
	   
	  
	public String getTradeacco() {
		return tradeacco;
	}
	public void setTradeacco(String tradeacco) {
		this.tradeacco = tradeacco;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankLongName() {
		return bankLongName;
	}
	public void setBankLongName(String bankLongName) {
		this.bankLongName = bankLongName;
	}
	public String getBankAcco() {
		return bankAcco;
	}
	public void setBankAcco(String bankAcco) {
		this.bankAcco = bankAcco;
	}
	public String getBankAcnm() {
		return bankAcnm;
	}
	public void setBankAcnm(String bankAcnm) {
		this.bankAcnm = bankAcnm;
	}
	public String getBankIdtp() {
		return bankIdtp;
	}
	public void setBankIdtp(String bankIdtp) {
		this.bankIdtp = bankIdtp;
	}
	public String getBankIdno() {
		return bankIdno;
	}
	public void setBankIdno(String bankIdno) {
		this.bankIdno = bankIdno;
	}
	public String getBankAccoDisplay() {
		return bankAccoDisplay;
	}
	public void setBankAccoDisplay(String bankAccoDisplay) {
		this.bankAccoDisplay = bankAccoDisplay;
	}
	public String getProvinceCode() {
		return provinceCode;
	}
	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}
	public String getInsertTimeStamp() {
		return insertTimeStamp;
	}
	public void setInsertTimeStamp(String insertTimeStamp) {
		this.insertTimeStamp = insertTimeStamp;
	}
	public String getUpdateTimeStamp() {
		return updateTimeStamp;
	}
	public void setUpdateTimeStamp(String updateTimeStamp) {
		this.updateTimeStamp = updateTimeStamp;
	}
	public String getConfidentialDoc_Del() {
		return confidentialDoc_Del;
	}
	public void setConfidentialDoc_Del(String confidentialDoc_Del) {
		this.confidentialDoc_Del = confidentialDoc_Del;
	}
	public String getBankAddr() {
		return bankAddr;
	}
	public void setBankAddr(String bankAddr) {
		this.bankAddr = bankAddr;
	}
	public String getBankAddrCity() {
		return bankAddrCity;
	}
	public void setBankAddrCity(String bankAddrCity) {
		this.bankAddrCity = bankAddrCity;
	}
	public String getRealBankNo() {
		return realBankNo;
	}
	public void setRealBankNo(String realBankNo) {
		this.realBankNo = realBankNo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getFdacst() {
		return fdacst;
	}
	public void setFdacst(String fdacst) {
		this.fdacst = fdacst;
	}
	public String getOpendt() {
		return opendt;
	}
	public void setOpendt(String opendt) {
		this.opendt = opendt;
	}
	
}
