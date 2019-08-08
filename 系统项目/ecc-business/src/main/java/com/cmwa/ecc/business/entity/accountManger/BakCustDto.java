package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;
import java.util.List;

import org.apache.ibatis.type.Alias;

/**
 * 备案客户资料信息实体类
 * @author ex-chenbq
 *
 */
@Alias("bakCustDto")
public class BakCustDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String operatorId; // 操作员代码
	private String permissionId; // 权限代码
	private String serialno; // 流水号

	private String invnm; // 客户姓名
	private String idtp; // 证件类型
	private String idtpnm; // 证件类型
	private String idno; // 证件号码
	private String idvalidate; // 证件有效期

	private String postcode; // 邮政编码
	private String addr; // 地址

	private String instrepnm; // 法人姓名
	private String instrepidtp; // 法人证件类型
	private String instrepidno; // 法人证件号码
	private String instrepvalidate; // 法人证件有效期
	private String instrepnation; // 法人国籍

	private String principalname; // 负责人名称
	private String principalidtp; // 负责人证件类型
	private String principalidno; // 负责人证件号码
	private String principalvalidt; // 负责人证件有效期
	private String principalnation; // 负责人国籍

	private String opertp; // 操作类型 M ('I':新增,'U':修改,'D':删除)
	private String custno; // 备案客户编号 O
	private String rcdcustrole; // 备案客户角色 M

	private String contactgrant; // 经办人授权
	private String contact; // 经办人
	private String contactnation; // 经办人国籍
	private String contidtp; // 经办人证件类型
	private String contidno; // 经办人证件号码
	private String contvalidate; // 经办人证件有效期
	private String contphone; // 经办人电话
	private String contfax; // 经办人传真
	private String contmobile; // 经办人手机
	private String contemail; // 经办人Email
	private String contAddr;//经办人地址
	private String contPostcode;//经办人邮编

	private String ocontactgrant; // 其他经办人授权
	private String ocontact; // 其他经办人
	private String ocontactnation; // 其他经办人国籍
	private String ocontidtp; // 其他经办人证件类型
	private String ocontidno; // 其他经办人证件号码
	private String ocontvalidate; // 其他经办人证件有效期
	private String ocontphone; // 其他经办人电话
	private String ocontfax; // 其他经办人传真
	private String ocontmobile; // 其他经办人手机
	private String ocontemail; // 其他经办人Email
	private String ocontAddr;//其他经办人联系地址
	private String ocontPostcode;//其他经办人邮编

	private String oidtp; // 其他证件类型
	private String oidno; // 其他证件号码
	private String oidvalidate; // 其他证件有效期
	
	private String stroid;   //其他证件
	private String strcontact;//其他经办人
	
	private String riskLevel;//客户风险承受能力
	private String checker;//复核员
	private String checkflag;//复核标记
	
	private String status;//状态
	private String selAppType;
	private String contidnm;
	
	private List<IDInfoDto> oidlist;
	private List<ContactInfoDto> ocontactlist;



	private String errcode; // 错误代码
	private String errmsg; // 错误信息
	private String newOcontStr;
	private String newOidStr;
	
	public BakCustDto() {

	}

	public String getNewOcontStr() {
		return newOcontStr;
	}

	public void setNewOcontStr(String newOcontStr) {
		this.newOcontStr = newOcontStr;
	}

	public String getNewOidStr() {
		return newOidStr;
	}

	public void setNewOidStr(String newOidStr) {
		this.newOidStr = newOidStr;
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

	public String getInvnm() {
		return invnm;
	}

	public void setInvnm(String invnm) {
		this.invnm = invnm;
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

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getInstrepnm() {
		return instrepnm;
	}

	public void setInstrepnm(String instrepnm) {
		this.instrepnm = instrepnm;
	}

	public String getInstrepidtp() {
		return instrepidtp;
	}

	public void setInstrepidtp(String instrepidtp) {
		this.instrepidtp = instrepidtp;
	}

	public String getInstrepidno() {
		return instrepidno;
	}

	public void setInstrepidno(String instrepidno) {
		this.instrepidno = instrepidno;
	}

	public String getInstrepvalidate() {
		return instrepvalidate;
	}

	public void setInstrepvalidate(String instrepvalidate) {
		this.instrepvalidate = instrepvalidate;
	}

	public String getInstrepnation() {
		return instrepnation;
	}

	public void setInstrepnation(String instrepnation) {
		this.instrepnation = instrepnation;
	}

	public String getPrincipalname() {
		return principalname;
	}

	public void setPrincipalname(String principalname) {
		this.principalname = principalname;
	}

	public String getPrincipalidtp() {
		return principalidtp;
	}

	public void setPrincipalidtp(String principalidtp) {
		this.principalidtp = principalidtp;
	}

	public String getPrincipalidno() {
		return principalidno;
	}

	public void setPrincipalidno(String principalidno) {
		this.principalidno = principalidno;
	}

	public String getPrincipalvalidt() {
		return principalvalidt;
	}

	public void setPrincipalvalidt(String principalvalidt) {
		this.principalvalidt = principalvalidt;
	}

	public String getPrincipalnation() {
		return principalnation;
	}

	public void setPrincipalnation(String principalnation) {
		this.principalnation = principalnation;
	}

	public String getOpertp() {
		return opertp;
	}

	public void setOpertp(String opertp) {
		this.opertp = opertp;
	}

	public String getCustno() {
		return custno;
	}

	public void setCustno(String custno) {
		this.custno = custno;
	}

	public String getRcdcustrole() {
		return rcdcustrole;
	}

	public void setRcdcustrole(String rcdcustrole) {
		this.rcdcustrole = rcdcustrole;
	}

	public String getContactgrant() {
		return contactgrant;
	}

	public void setContactgrant(String contactgrant) {
		this.contactgrant = contactgrant;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getContactnation() {
		return contactnation;
	}

	public void setContactnation(String contactnation) {
		this.contactnation = contactnation;
	}

	public String getContidtp() {
		return contidtp;
	}

	public void setContidtp(String contidtp) {
		this.contidtp = contidtp;
	}

	public String getContidno() {
		return contidno;
	}

	public void setContidno(String contidno) {
		this.contidno = contidno;
	}

	public String getContvalidate() {
		return contvalidate;
	}

	public void setContvalidate(String contvalidate) {
		this.contvalidate = contvalidate;
	}

	public String getContphone() {
		return contphone;
	}

	public void setContphone(String contphone) {
		this.contphone = contphone;
	}

	public String getContfax() {
		return contfax;
	}

	public void setContfax(String contfax) {
		this.contfax = contfax;
	}

	public String getContmobile() {
		return contmobile;
	}

	public void setContmobile(String contmobile) {
		this.contmobile = contmobile;
	}

	public String getContemail() {
		return contemail;
	}

	public void setContemail(String contemail) {
		this.contemail = contemail;
	}

	public String getOcontactgrant() {
		return ocontactgrant;
	}

	public void setOcontactgrant(String ocontactgrant) {
		this.ocontactgrant = ocontactgrant;
	}

	public String getOcontact() {
		return ocontact;
	}

	public void setOcontact(String ocontact) {
		this.ocontact = ocontact;
	}

	public String getOcontactnation() {
		return ocontactnation;
	}

	public void setOcontactnation(String ocontactnation) {
		this.ocontactnation = ocontactnation;
	}

	public String getOcontidtp() {
		return ocontidtp;
	}

	public void setOcontidtp(String ocontidtp) {
		this.ocontidtp = ocontidtp;
	}

	public String getOcontidno() {
		return ocontidno;
	}

	public void setOcontidno(String ocontidno) {
		this.ocontidno = ocontidno;
	}

	public String getOcontvalidate() {
		return ocontvalidate;
	}

	public void setOcontvalidate(String ocontvalidate) {
		this.ocontvalidate = ocontvalidate;
	}

	public String getOcontphone() {
		return ocontphone;
	}

	public void setOcontphone(String ocontphone) {
		this.ocontphone = ocontphone;
	}

	public String getOcontfax() {
		return ocontfax;
	}

	public void setOcontfax(String ocontfax) {
		this.ocontfax = ocontfax;
	}

	public String getOcontmobile() {
		return ocontmobile;
	}

	public void setOcontmobile(String ocontmobile) {
		this.ocontmobile = ocontmobile;
	}

	public String getOcontemail() {
		return ocontemail;
	}

	public void setOcontemail(String ocontemail) {
		this.ocontemail = ocontemail;
	}

	public String getOidtp() {
		return oidtp;
	}

	public void setOidtp(String oidtp) {
		this.oidtp = oidtp;
	}

	public String getOidno() {
		return oidno;
	}

	public void setOidno(String oidno) {
		this.oidno = oidno;
	}

	public String getOidvalidate() {
		return oidvalidate;
	}

	public void setOidvalidate(String oidvalidate) {
		this.oidvalidate = oidvalidate;
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

	public String getStroid() {
		return stroid;
	}

	public void setStroid(String stroid) {
		this.stroid = stroid;
	}

	public String getStrcontact() {
		return strcontact;
	}

	public void setStrcontact(String strcontact) {
		this.strcontact = strcontact;
	}

	public List<IDInfoDto> getOidlist() {
		return oidlist;
	}

	public void setOidlist(List<IDInfoDto> oidlist) {
		this.oidlist = oidlist;
	}

	public List<ContactInfoDto> getOcontactlist() {
		return ocontactlist;
	}

	public void setOcontactlist(List<ContactInfoDto> ocontactlist) {
		this.ocontactlist = ocontactlist;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getContAddr() {
		return contAddr;
	}

	public void setContAddr(String contAddr) {
		this.contAddr = contAddr;
	}

	public String getContPostcode() {
		return contPostcode;
	}

	public void setContPostcode(String contPostcode) {
		this.contPostcode = contPostcode;
	}

	public String getOcontAddr() {
		return ocontAddr;
	}

	public void setOcontAddr(String ocontAddr) {
		this.ocontAddr = ocontAddr;
	}

	public String getOcontPostcode() {
		return ocontPostcode;
	}

	public void setOcontPostcode(String ocontPostcode) {
		this.ocontPostcode = ocontPostcode;
	}

	public String getRiskLevel() {
		return riskLevel;
	}

	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String getCheckflag() {
		return checkflag;
	}

	public void setCheckflag(String checkflag) {
		this.checkflag = checkflag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSelAppType() {
		return selAppType;
	}

	public void setSelAppType(String selAppType) {
		this.selAppType = selAppType;
	}

	public String getContidnm() {
		return contidnm;
	}

	public void setContidnm(String contidnm) {
		this.contidnm = contidnm;
	}
	public String getIdtpnm() {
		return idtpnm;
	}

	public void setIdtpnm(String idtpnm) {
		this.idtpnm = idtpnm;
	}
}
