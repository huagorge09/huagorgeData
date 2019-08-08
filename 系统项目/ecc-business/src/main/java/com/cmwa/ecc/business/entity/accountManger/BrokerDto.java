package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;
@Alias("brokerDto")
public class BrokerDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String operatorId; 		//操作员代码
	private String permissionId; 	//权限代码
	
	private String serialno; 		//流水号
	private String brokerno; 		//客户经理代码
	private String brokernm; 		//客户经理名称
	private String orgcode; 		//所属机构代码
	private String regioncode; 		//所属区域代码
	private String mobileno; 		//手机
	private String telno; 			//电话
	private String email; 			//email
	
	private String workdate;		//操作日期
	
	private String errcode;			//错误代码
	private String errmsg;			//错误信息
	
	public BrokerDto() {
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

	public String getBrokerno() {
		return brokerno;
	}

	public void setBrokerno(String brokerno) {
		this.brokerno = brokerno;
	}

	public String getBrokernm() {
		return brokernm;
	}

	public void setBrokernm(String brokernm) {
		this.brokernm = brokernm;
	}

	public String getOrgcode() {
		return orgcode;
	}

	public void setOrgcode(String orgcode) {
		this.orgcode = orgcode;
	}

	public String getRegioncode() {
		return regioncode;
	}

	public void setRegioncode(String regioncode) {
		this.regioncode = regioncode;
	}

	public String getMobileno() {
		return mobileno;
	}

	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}

	public String getTelno() {
		return telno;
	}

	public void setTelno(String telno) {
		this.telno = telno;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getWorkdate() {
		return workdate;
	}

	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}

	@Override
	public String toString() {
		return "BrokerDto [operatorId=" + operatorId + ", permissionId="
				+ permissionId + ", serialno=" + serialno + ", brokerno="
				+ brokerno + ", brokernm=" + brokernm + ", orgcode=" + orgcode
				+ ", regioncode=" + regioncode + ", mobileno=" + mobileno
				+ ", telno=" + telno + ", email=" + email + ", workdate="
				+ workdate + ", errcode=" + errcode + ", errmsg=" + errmsg
				+ "]";
	}
}
