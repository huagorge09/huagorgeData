package com.cmwa.ecc.business.entity.trade;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 合同
 * @author ex-liuy
 *
 */
@Alias("eleContractDto")
public class EleContractDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2056410076064074625L;
	
	private String contractID;// 合同号
	private String contractName;// 合同名称
	private String contractAttach;// 合同附件号
	private String contractversion;// 合同版本号
	private String fundID;// 基金代码
	private String Processor;// 经办人
	private String processTime;// 经办时间
	private String Contractst;// 启用状态
	private String fundName; // 产品名称
	private String errCode; // 返回码
	private String errMsg;// 返回信息

	public String getContractversion() {
		return contractversion;
	}

	public void setContractversion(String contractversion) {
		this.contractversion = contractversion;
	}

	public String getContractID() {
		return contractID;
	}

	public void setContractID(String contractID) {
		this.contractID = contractID;
	}

	public String getContractName() {
		return contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	

	public void setContractAttach(String contractAttach) {
		this.contractAttach = contractAttach;
	}

	public String getFundID() {
		return fundID;
	}

	public void setFundID(String fundID) {
		this.fundID = fundID;
	}

	public String getProcessor() {
		return Processor;
	}

	public void setProcessor(String processor) {
		Processor = processor;
	}

	public String getProcessTime() {
		return processTime;
	}

	public void setProcessTime(String processTime) {
		this.processTime = processTime;
	}

	public String getContractst() {
		return Contractst;
	}

	public void setContractst(String contractst) {
		Contractst = contractst;
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

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "EleContractDto [contractID=" + contractID + ", contractName="
				+ contractName + ", contractAttach=" + contractAttach
				+ ", contractversion=" + contractversion + ", fundID=" + fundID
				+ ", Processor=" + Processor + ", processTime=" + processTime
				+ ", Contractst=" + Contractst + ", fundName=" + fundName
				+ ", errCode=" + errCode + ", errMsg=" + errMsg + "]";
	}

	public String getContractAttach() {
		return contractAttach;
	}

}
