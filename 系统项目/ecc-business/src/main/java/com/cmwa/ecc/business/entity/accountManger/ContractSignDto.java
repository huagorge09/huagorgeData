package com.cmwa.ecc.business.entity.accountManger;

import org.apache.ibatis.type.Alias;

@Alias("contractSignDto")
public class ContractSignDto {
	private String serialNo;     //序列号（获取方法参照认购下单）
	private String custNo;       //客户号
	private String tradeAcco;    //交易账号
	private String fundId;       //基金代码
	private String appdt;        //签署日期
	private String contractVer;  //合同版本
	private String contractTp;   //合同类型
	private String signChannel;  //合同签署途径
	private String signMachine;  //合同签署机器
	
	private String errCode;      //返回码
	private String errMsg;       //返回信息
	
	public String getSerialNo() {
		return serialNo;
	}
	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}
	public String getAppdt() {
		return appdt;
	}
	public void setAppdt(String appdt) {
		this.appdt = appdt;
	}
	public String getCustNo() {
		return custNo;
	}
	public void setCustNo(String custNo) {
		this.custNo = custNo;
	}
	public String getTradeAcco() {
		return tradeAcco;
	}
	public void setTradeAcco(String tradeAcco) {
		this.tradeAcco = tradeAcco;
	}
	public String getFundId() {
		return fundId;
	}
	public void setFundId(String fundId) {
		this.fundId = fundId;
	}
	public String getContractVer() {
		return contractVer;
	}
	public void setContractVer(String contractVer) {
		this.contractVer = contractVer;
	}
	public String getContractTp() {
		return contractTp;
	}
	public void setContractTp(String contractTp) {
		this.contractTp = contractTp;
	}
	public String getSignChannel() {
		return signChannel;
	}
	public void setSignChannel(String signChannel) {
		this.signChannel = signChannel;
	}
	public String getSignMachine() {
		return signMachine;
	}
	public void setSignMachine(String signMachine) {
		this.signMachine = signMachine;
	}
	public String getErrCode() {
		return errCode;
	}
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}
	public String getErrMsg() {
		return errMsg;
	}
	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}
}
