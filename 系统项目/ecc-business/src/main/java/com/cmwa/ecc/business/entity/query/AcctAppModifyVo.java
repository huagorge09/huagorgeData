/**
* @Title: AcctAppModifyVo.java  
* @author ex-wuh2  
* @date 2018年6月7日  
* @version V1.0  
 */
package com.cmwa.ecc.business.entity.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author ex-wuh2
 *	客户账户修改
 */
@Alias("acctAppModifyVo")
public class AcctAppModifyVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String apdt;//申请日期
	private String fundacct;//基金帐号
	private String invnm;//投资者姓名
	private String idtp;//证件类型
	private String idtpName;//证件类型
	private String idno;//证件号码
	private String bankno;//银行代码
	private String banklongname;//银行全称
	private String bankacname;//银行户名
	private String bankacco;//银行账号
	private String mobileno;//移动电话
	private String addr;//地址
	private String postcode;//邮政编码
	private String email;//电子邮箱
	private String faxno;//传真号码
	private String hometel;//家庭电话
	private String officetel;//办公室电话
	private String delivertype;//对帐单寄送方式
	private String brokername;//经办人
	private String brokeridtp;//经办人证件类型
	private String brokeridno;//经办人证件号码
	private String brokerofficetel;//经办人办公电话
	private String brokerfax;//经办人传真
	private String brokervalidate;//经办人证件有效期
	private String brokermobile;//经办人移动电话
	private String brokeremail;//经办人电子邮箱
	private String brokernation;//经办人国籍
	private String instreprname;//法人姓名
	private String instrepridtp;//法人证件类型
	private String instrepridno;//法人证件号码
	private String instreprnation;//法人国籍
	private String instreprdate;//法人证件有效期
	private String principalname;//机构负责人姓名
	private String principalnation;//机构负责人国籍
	private String principaltype;//机构负责人证件类型
	private String principalno;//机构负责人证件号码
	private String principalvalidate;//机构负责人证件有效期
	private String tradeacco;//交易帐号
	private String apkind;//业务类型
	private String custtp;//客户类型
	private String invtp;//投资者类型
	private String opid;//操作员代码
	private String regioncode;//区域代码
	private String netPoint;
	public String getNetPoint() {
		return netPoint;
	}
	public void setNetPoint(String netPoint) {
		this.netPoint = netPoint;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getIdtpName() {
		return idtpName;
	}
	public void setIdtpName(String idtpName) {
		this.idtpName = idtpName;
	}
	public String getApdt() {
		return apdt;
	}
	public void setApdt(String apdt) {
		this.apdt = apdt;
	}
	public String getFundacct() {
		return fundacct;
	}
	public void setFundacct(String fundacct) {
		this.fundacct = fundacct;
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
	public String getBankno() {
		return bankno;
	}
	public void setBankno(String bankno) {
		this.bankno = bankno;
	}
	public String getBanklongname() {
		return banklongname;
	}
	public void setBanklongname(String banklongname) {
		this.banklongname = banklongname;
	}
	public String getBankacname() {
		return bankacname;
	}
	public void setBankacname(String bankacname) {
		this.bankacname = bankacname;
	}
	public String getBankacco() {
		return bankacco;
	}
	public void setBankacco(String bankacco) {
		this.bankacco = bankacco;
	}
	public String getMobileno() {
		return mobileno;
	}
	public void setMobileno(String mobileno) {
		this.mobileno = mobileno;
	}
	public String getAddr() {
		return addr;
	}
	public void setAddr(String addr) {
		this.addr = addr;
	}
	public String getPostcode() {
		return postcode;
	}
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFaxno() {
		return faxno;
	}
	public void setFaxno(String faxno) {
		this.faxno = faxno;
	}
	public String getHometel() {
		return hometel;
	}
	public void setHometel(String hometel) {
		this.hometel = hometel;
	}
	public String getOfficetel() {
		return officetel;
	}
	public void setOfficetel(String officetel) {
		this.officetel = officetel;
	}
	public String getDelivertype() {
		return delivertype;
	}
	public void setDelivertype(String delivertype) {
		this.delivertype = delivertype;
	}
	public String getBrokername() {
		return brokername;
	}
	public void setBrokername(String brokername) {
		this.brokername = brokername;
	}
	public String getBrokeridtp() {
		return brokeridtp;
	}
	public void setBrokeridtp(String brokeridtp) {
		this.brokeridtp = brokeridtp;
	}
	public String getBrokeridno() {
		return brokeridno;
	}
	public void setBrokeridno(String brokeridno) {
		this.brokeridno = brokeridno;
	}
	public String getBrokerofficetel() {
		return brokerofficetel;
	}
	public void setBrokerofficetel(String brokerofficetel) {
		this.brokerofficetel = brokerofficetel;
	}
	public String getBrokerfax() {
		return brokerfax;
	}
	public void setBrokerfax(String brokerfax) {
		this.brokerfax = brokerfax;
	}
	public String getBrokervalidate() {
		return brokervalidate;
	}
	public void setBrokervalidate(String brokervalidate) {
		this.brokervalidate = brokervalidate;
	}
	public String getBrokermobile() {
		return brokermobile;
	}
	public void setBrokermobile(String brokermobile) {
		this.brokermobile = brokermobile;
	}
	public String getBrokeremail() {
		return brokeremail;
	}
	public void setBrokeremail(String brokeremail) {
		this.brokeremail = brokeremail;
	}
	public String getBrokernation() {
		return brokernation;
	}
	public void setBrokernation(String brokernation) {
		this.brokernation = brokernation;
	}
	public String getInstreprname() {
		return instreprname;
	}
	public void setInstreprname(String instreprname) {
		this.instreprname = instreprname;
	}
	public String getInstrepridtp() {
		return instrepridtp;
	}
	public void setInstrepridtp(String instrepridtp) {
		this.instrepridtp = instrepridtp;
	}
	public String getInstrepridno() {
		return instrepridno;
	}
	public void setInstrepridno(String instrepridno) {
		this.instrepridno = instrepridno;
	}
	public String getInstreprnation() {
		return instreprnation;
	}
	public void setInstreprnation(String instreprnation) {
		this.instreprnation = instreprnation;
	}
	public String getInstreprdate() {
		return instreprdate;
	}
	public void setInstreprdate(String instreprdate) {
		this.instreprdate = instreprdate;
	}
	public String getPrincipalname() {
		return principalname;
	}
	public void setPrincipalname(String principalname) {
		this.principalname = principalname;
	}
	public String getPrincipalnation() {
		return principalnation;
	}
	public void setPrincipalnation(String principalnation) {
		this.principalnation = principalnation;
	}
	public String getPrincipaltype() {
		return principaltype;
	}
	public void setPrincipaltype(String principaltype) {
		this.principaltype = principaltype;
	}
	public String getPrincipalno() {
		return principalno;
	}
	public void setPrincipalno(String principalno) {
		this.principalno = principalno;
	}
	public String getPrincipalvalidate() {
		return principalvalidate;
	}
	public void setPrincipalvalidate(String principalvalidate) {
		this.principalvalidate = principalvalidate;
	}
	public String getTradeacco() {
		return tradeacco;
	}
	public void setTradeacco(String tradeacco) {
		this.tradeacco = tradeacco;
	}
	public String getApkind() {
		return apkind;
	}
	public void setApkind(String apkind) {
		this.apkind = apkind;
	}
	public String getCusttp() {
		return custtp;
	}
	public void setCusttp(String custtp) {
		this.custtp = custtp;
	}
	public String getInvtp() {
		return invtp;
	}
	public void setInvtp(String invtp) {
		this.invtp = invtp;
	}
	public String getOpid() {
		return opid;
	}
	public void setOpid(String opid) {
		this.opid = opid;
	}
	public String getRegioncode() {
		return regioncode;
	}
	public void setRegioncode(String regioncode) {
		this.regioncode = regioncode;
	}
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}
}
