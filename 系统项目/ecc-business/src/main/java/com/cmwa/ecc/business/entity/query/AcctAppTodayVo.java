package com.cmwa.ecc.business.entity.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author ex-wuh2
 */
@Alias("acctAppTodayVo")
public class AcctAppTodayVo implements Serializable{
	private static final long serialVersionUID = 1L;
	private String fundacct;
    private String tradeacco;
    private String apkind;
    private String apkindName;
    private String invtp;
    private String invtpName;
    private String invnm;
    private String idtp;
    private String idtpName;
    private String idno;
    private String telno;
    private String mobileno;
	private String addr;
    private String postcode;
	private String email;
    private String faxno;
    private String delivertype;
    private String melonmd;
    private String melonmdName;
    private String workdate;
    private String applyst;
    private String apdt;
    private String aptm;
    private String regioncode;//区域代码
    private String regioncodeName;//区域代码
	private String netPoint;//网点代码
    private String instrepname;//法人名称
    private String instrepidtp;//法人证件类型
    private String instrepidno;//法人证件号码
    private String brokername;//经办人名称
    private String brokeridtp;//经办人证件类型
    private String brokeridno;//经办人证件号码
    private String brokerfax;//经办人传真号码
    private String hometel;//家庭电话
    private String officetel;//办公室电话
    private String operatorcode;//操作员代码
    private String serialno;
    private String custsimpnm;//客户一级简称
    private String instrepcode;//客户二级简称
    private String invname;//客户二级简称
	private String custtp;//客户类别
    private String checker;//复核员
    private String checkflag;//复核标志
    private String checkflagName;//复核标志
    private String brokertel;//经办人办公电话
    private String accountabbr;
    private String appno;//申请编号
    
    public String getInvname() {
		return invname;
	}
	public void setInvname(String invname) {
		this.invname = invname;
	}
	public String getRegioncodeName() {
		return regioncodeName;
	}
	public void setRegioncodeName(String regioncodeName) {
		this.regioncodeName = regioncodeName;
	}
    public String getInvtpName() {
		return invtpName;
	}
	public void setInvtpName(String invtpName) {
		this.invtpName = invtpName;
	}
    public String getApkindName() {
		return apkindName;
	}
	public void setApkindName(String apkindName) {
		this.apkindName = apkindName;
	}
	public String getIdtpName() {
		return idtpName;
	}
	public void setIdtpName(String idtpName) {
		this.idtpName = idtpName;
	}
	public String getMelonmdName() {
		return melonmdName;
	}
	public void setMelonmdName(String melonmdName) {
		this.melonmdName = melonmdName;
	}
	public String getCheckflagName() {
		return checkflagName;
	}
	public void setCheckflagName(String checkflagName) {
		this.checkflagName = checkflagName;
	}
    
    public String getSerialno() {
		return serialno;
	}
	public void setSerialno(String serialno) {
		this.serialno = serialno;
	}
	public String getFundacct() {
		return fundacct;
	}
	public void setFundacct(String fundacct) {
		this.fundacct = fundacct;
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
	public String getInvtp() {
		return invtp;
	}
	public void setInvtp(String invtp) {
		this.invtp = invtp;
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
	public String getTelno() {
		return telno;
	}
	public void setTelno(String telno) {
		this.telno = telno;
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
	public String getDelivertype() {
		return delivertype;
	}
	public void setDelivertype(String delivertype) {
		this.delivertype = delivertype;
	}
	public String getMelonmd() {
		return melonmd;
	}
	public void setMelonmd(String melonmd) {
		this.melonmd = melonmd;
	}
	public String getWorkdate() {
		return workdate;
	}
	public void setWorkdate(String workdate) {
		this.workdate = workdate;
	}
	public String getApplyst() {
		return applyst;
	}
	public void setApplyst(String applyst) {
		this.applyst = applyst;
	}
	public String getApdt() {
		return apdt;
	}
	public void setApdt(String apdt) {
		this.apdt = apdt;
	}
	public String getAptm() {
		return aptm;
	}
	public void setAptm(String aptm) {
		this.aptm = aptm;
	}
	public String getRegioncode() {
		return regioncode;
	}
	public void setRegioncode(String regioncode) {
		this.regioncode = regioncode;
	}
	public String getNetPoint() {
		return netPoint;
	}
	public void setNetPoint(String netPoint) {
		this.netPoint = netPoint;
	}
	public String getInstrepname() {
		return instrepname;
	}
	public void setInstrepname(String instrepname) {
		this.instrepname = instrepname;
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
	public String getBrokerfax() {
		return brokerfax;
	}
	public void setBrokerfax(String brokerfax) {
		this.brokerfax = brokerfax;
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
	public String getOperatorcode() {
		return operatorcode;
	}
	public void setOperatorcode(String operatorcode) {
		this.operatorcode = operatorcode;
	}
	public String getCustsimpnm() {
		return custsimpnm;
	}
	public void setCustsimpnm(String custsimpnm) {
		this.custsimpnm = custsimpnm;
	}
	public String getInstrepcode() {
		return instrepcode;
	}
	public void setInstrepcode(String instrepcode) {
		this.instrepcode = instrepcode;
	}
	public String getCusttp() {
		return custtp;
	}
	public void setCusttp(String custtp) {
		this.custtp = custtp;
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
	public String getBrokertel() {
		return brokertel;
	}
	public void setBrokertel(String brokertel) {
		this.brokertel = brokertel;
	}
	public String getAccountabbr() {
		return accountabbr;
	}
	public void setAccountabbr(String accountabbr) {
		this.accountabbr = accountabbr;
	}
	public String getAppno() {
		return appno;
	}
	public void setAppno(String appno) {
		this.appno = appno;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
