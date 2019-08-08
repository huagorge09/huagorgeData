package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("brokerInfoDto")
public class BrokerInfoDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String operatorId; 		//操作员代码
	private String permissionId; 	//权限代码
	
	private String serialno; 		//流水号
	private String trustType; 		//委托方式
	private String custno; 			//客户号
	private String custnm; 			//客户号
	private String cltmno; 			//客户号
	private String invnm; 			//客户名称
	private String invtp; 			//客户类别
	private String brokercode; 		//客户经理代码
	private double percent; 		//分成比例
	private String percentModify; 	//分成比例-修改传入
	private String checkno; 		//主管工号
	private String tradeacco; 		//交易账号
	private String fundacco; 		//基金账号
	private String begindate;//未处理数
	private String enddate;//未处理数
	
	
	private String errcode;			//错误代码
	private String errmsg;			//错误信息
	
	public BrokerInfoDto() {
	}

	public String getCustno() {
		return custno;
	}

	public void setCustno(String custno) {
		this.custno = custno;
	}

	public String getInvnm() {
		return invnm;
	}

	public void setInvnm(String invnm) {
		this.invnm = invnm;
	}

	public String getBrokercode() {
		return brokercode;
	}

	public void setBrokercode(String brokercode) {
		this.brokercode = brokercode;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getInvtp() {
		return invtp;
	}

	public void setInvtp(String invtp) {
		this.invtp = invtp;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}

	public String getTrustType() {
		return trustType;
	}

	public void setTrustType(String trustType) {
		this.trustType = trustType;
	}

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getPercentModify() {
		return percentModify;
	}

	public void setPercentModify(String percentModify) {
		this.percentModify = percentModify;
	}

	public String getCheckno() {
		return checkno;
	}

	public void setCheckno(String checkno) {
		this.checkno = checkno;
	}

	public String getTradeacco() {
		return tradeacco;
	}

	public void setTradeacco(String tradeacco) {
		this.tradeacco = tradeacco;
	}

	public String getFundacco() {
		return fundacco;
	}

	public void setFundacco(String fundacco) {
		this.fundacco = fundacco;
	}

	public String getBegindate() {
		return begindate;
	}

	public void setBegindate(String begindate) {
		this.begindate = begindate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getCustnm() {
		return custnm;
	}

	public void setCustnm(String custnm) {
		this.custnm = custnm;
	}

	public String getCltmno() {
		return cltmno;
	}

	public void setCltmno(String cltmno) {
		this.cltmno = cltmno;
	}
}
