package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("validateDto")
public class ValidateDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String operatorId; 		//操作员代码
	private String operatorType; 	//操作类型
	private String permissionId; 	//权限代码
	private String custno;
	private String serialno; 		//流水号
	private String idnm; 			//证件持有人名称
	private String invtp; 			//客户类型
	private String invtpName; 			//客户类型
	private String idtp; 			//证件类型
	private String idtpName; 			//证件类型
	private String idno; 			//证件号码
	private String idvalidate; 		//证件有效期
	private String tel; 			//电话号码
	private String mobile; 			//手机
	private String fax; 			//传真
	private String email;			//邮件
	private String addr;			//详细地址
	private String tradeacco; 		//交易账号
	private String fundacco; 		//基金账号
	private String begindate;//未处理数
	private String enddate;//未处理数
	
	private String errcode;			//错误代码
	private String errmsg;			//错误信息
	
	public ValidateDto() {
	}

	public String getCustno() {
		return custno;
	}

	public void setCustno(String custno) {
		this.custno = custno;
	}

	public String getInvtpName() {
		return invtpName;
	}

	public void setInvtpName(String invtpName) {
		this.invtpName = invtpName;
	}

	public String getIdtpName() {
		return idtpName;
	}

	public void setIdtpName(String idtpName) {
		this.idtpName = idtpName;
	}

	public String getIdnm() {
		return idnm;
	}

	public void setIdnm(String idnm) {
		this.idnm = idnm;
	}

	public String getInvtp() {
		return invtp;
	}

	public void setInvtp(String invtp) {
		this.invtp = invtp;
	}

	public String getIdtp() {
		return idtp;
	}

	public void setIdtp(String idtp) {
		this.idtp = idtp;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getIdvalidate() {
		return idvalidate;
	}

	public void setIdvalidate(String idvalidate) {
		this.idvalidate = idvalidate;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
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

	public String getSerialno() {
		return serialno;
	}

	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}

	public String getOperatorType() {
		return operatorType;
	}

	public void setOperatorType(String operatorType) {
		this.operatorType = operatorType;
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

	@Override
	public String toString() {
		return "ValidateDto [operatorId=" + operatorId + ", operatorType="
				+ operatorType + ", permissionId=" + permissionId
				+ ", serialno=" + serialno + ", idnm=" + idnm + ", invtp="
				+ invtp + ", idtp=" + idtp + ", idno=" + idno + ", idvalidate="
				+ idvalidate + ", tel=" + tel + ", mobile=" + mobile + ", fax="
				+ fax + ", email=" + email + ", addr=" + addr + ", tradeacco="
				+ tradeacco + ", fundacco=" + fundacco + ", begindate="
				+ begindate + ", enddate=" + enddate + ", errcode=" + errcode
				+ ", errmsg=" + errmsg + "]";
	}
}
