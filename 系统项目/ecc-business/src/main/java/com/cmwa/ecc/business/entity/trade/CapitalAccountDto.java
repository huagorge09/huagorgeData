package com.cmwa.ecc.business.entity.trade;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("capitalAccountDto")
public class CapitalAccountDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String tradeDay;	//交易日
	private String tradeTime;	//交易时间
	private String bankTradeDay;//银行方交易日
	private String bankTradeTime;//银行方交易时间
	private String bankAmount;//银行方交易金额
	private String seriaNo;	//自动生成的流水号
	private String bankNo;	//银行代码
	private String bankName;//银行名称
	private String bankAcct;//银行账号
	private String oBankAcct;//对方账号
	private String amount;	//交易金额
	private String summary;//摘要
	private String ackfOpId;//操作员
	private String insertTimeStamp;//插入时间戳
	private String updateTimeStamp;//更新时间戳
	private String checkFlag;//对账标记
	private String bankUser;//银行户名
	private String actFlag;//确认标记
	private String actDay;	//确认日期
	private String actTime;//确认时间
	private String flag;//工商银行“借”“贷”
	private String tradeAcco;//交易账号
	private String bankSeriaNo;//银行流水号
	private String fundacco;//基金账号
	private String refserialno;//交易参考流水号
	private String paystnm;//支付状态名称
	private String payst;//支付状态
	private String fundid;//基金代码
	private String fundname;//基金名称
	private String fundBankacco;//基金方银行账号
	
	private String operator;//操作员
	private String transcd;//操作代码
	private int saveCount;	//成功记录条数
	private String returnCode;//返回代码
	private String returnMsg;//返回信息
	
	private int currOder;//当日下单数
	private int accountCount;//对账记录数
	private int accountSuccess;//对账成功数
	private int accountFail;//对账失败数
	private int ackSuccess;//确认成功数
	private int ackFail;//确认失败数
	
	
	public String getTradeDay() {
		return tradeDay;
	}
	public void setTradeDay(String tradeDay) {
		this.tradeDay = tradeDay;
	}
	public String getTradeTime() {
		return tradeTime;
	}
	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}
	public String getSeriaNo() {
		return seriaNo;
	}
	public void setSeriaNo(String seriaNo) {
		this.seriaNo = seriaNo;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankAcct() {
		return bankAcct;
	}
	public void setBankAcct(String bankAcct) {
		this.bankAcct = bankAcct;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAckfOpId() {
		return ackfOpId;
	}
	public void setAckfOpId(String ackfOpId) {
		this.ackfOpId = ackfOpId;
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
	public String getCheckFlag() {
		return checkFlag;
	}
	public void setCheckFlag(String checkFlag) {
		this.checkFlag = checkFlag;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public String getBankUser() {
		return bankUser;
	}
	public void setBankUser(String bankUser) {
		this.bankUser = bankUser;
	}
	public int getSaveCount() {
		return saveCount;
	}
	public void setSaveCount(int saveCount) {
		this.saveCount = saveCount;
	}
	public String getOBankAcct() {
		return oBankAcct;
	}
	public void setOBankAcct(String bankAcct) {
		oBankAcct = bankAcct;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getActFlag() {
		return actFlag;
	}
	public void setActFlag(String actFlag) {
		this.actFlag = actFlag;
	}
	public String getActDay() {
		return actDay;
	}
	public void setActDay(String actDay) {
		this.actDay = actDay;
	}
	public String getActTime() {
		return actTime;
	}
	public void setActTime(String actTime) {
		this.actTime = actTime;
	}
	public String getTradeAcco() {
		return tradeAcco;
	}
	public void setTradeAcco(String tradeAcco) {
		this.tradeAcco = tradeAcco;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getBankSeriaNo() {
		return bankSeriaNo;
	}
	public void setBankSeriaNo(String bankSeriaNo) {
		this.bankSeriaNo = bankSeriaNo;
	}
	public String getBankTradeDay() {
		return bankTradeDay;
	}
	public void setBankTradeDay(String bankTradeDay) {
		this.bankTradeDay = bankTradeDay;
	}
	public String getBankTradeTime() {
		return bankTradeTime;
	}
	public void setBankTradeTime(String bankTradeTime) {
		this.bankTradeTime = bankTradeTime;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getTranscd() {
		return transcd;
	}
	public void setTranscd(String transcd) {
		this.transcd = transcd;
	}
	public String getBankAmount() {
		return bankAmount;
	}
	public void setBankAmount(String bankAmount) {
		this.bankAmount = bankAmount;
	}
	public String getFundacco() {
		return fundacco;
	}
	public void setFundacco(String fundacco) {
		this.fundacco = fundacco;
	}
	public String getRefserialno() {
		return refserialno;
	}
	public void setRefserialno(String refserialno) {
		this.refserialno = refserialno;
	}
	public String getPaystnm() {
		return paystnm;
	}
	public void setPaystnm(String paystnm) {
		this.paystnm = paystnm;
	}
	public String getPayst() {
		return payst;
	}
	public void setPayst(String payst) {
		this.payst = payst;
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
	public String getFundBankacco() {
		return fundBankacco;
	}
	public void setFundBankacco(String fundBankacco) {
		this.fundBankacco = fundBankacco;
	}
	public int getCurrOder() {
		return currOder;
	}
	public void setCurrOder(int currOder) {
		this.currOder = currOder;
	}
	public int getAccountCount() {
		return accountCount;
	}
	public void setAccountCount(int accountCount) {
		this.accountCount = accountCount;
	}
	public int getAccountSuccess() {
		return accountSuccess;
	}
	public void setAccountSuccess(int accountSuccess) {
		this.accountSuccess = accountSuccess;
	}
	public int getAccountFail() {
		return accountFail;
	}
	public void setAccountFail(int accountFail) {
		this.accountFail = accountFail;
	}
	public int getAckSuccess() {
		return ackSuccess;
	}
	public void setAckSuccess(int ackSuccess) {
		this.ackSuccess = ackSuccess;
	}
	public int getAckFail() {
		return ackFail;
	}
	public void setAckFail(int ackFail) {
		this.ackFail = ackFail;
	}
	@Override
	public String toString() {
		return "CapitalAccountDto [tradeDay=" + tradeDay + ", tradeTime="
				+ tradeTime + ", bankTradeDay=" + bankTradeDay
				+ ", bankTradeTime=" + bankTradeTime + ", bankAmount="
				+ bankAmount + ", seriaNo=" + seriaNo + ", bankNo=" + bankNo
				+ ", bankName=" + bankName + ", bankAcct=" + bankAcct
				+ ", oBankAcct=" + oBankAcct + ", amount=" + amount
				+ ", summary=" + summary + ", ackfOpId=" + ackfOpId
				+ ", insertTimeStamp=" + insertTimeStamp + ", updateTimeStamp="
				+ updateTimeStamp + ", checkFlag=" + checkFlag + ", bankUser="
				+ bankUser + ", actFlag=" + actFlag + ", actDay=" + actDay
				+ ", actTime=" + actTime + ", flag=" + flag + ", tradeAcco="
				+ tradeAcco + ", bankSeriaNo=" + bankSeriaNo + ", fundacco="
				+ fundacco + ", refserialno=" + refserialno + ", paystnm="
				+ paystnm + ", payst=" + payst + ", fundid=" + fundid
				+ ", fundname=" + fundname + ", fundBankacco=" + fundBankacco
				+ ", operator=" + operator + ", transcd=" + transcd
				+ ", saveCount=" + saveCount + ", returnCode=" + returnCode
				+ ", returnMsg=" + returnMsg + ", currOder=" + currOder
				+ ", accountCount=" + accountCount + ", accountSuccess="
				+ accountSuccess + ", accountFail=" + accountFail
				+ ", ackSuccess=" + ackSuccess + ", ackFail=" + ackFail + "]";
	}
	
}
