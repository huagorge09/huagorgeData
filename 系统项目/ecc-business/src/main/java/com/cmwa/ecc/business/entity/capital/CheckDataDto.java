package com.cmwa.ecc.business.entity.capital;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * <p>Title: 后台资金管理</p>
 * <p>Description: 凭证导出DTO</p>
 * <p>Copyright: Copyright (c) 2011</p>
 * <p>Company:  </p>
 * @author   ex-liuy
 * @version 1.0
 * @CreateDate: 2018年6月13日17:18:10
 * @UpdateDate:
 */
@Alias("checkDataDto")
public class CheckDataDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String billdate; //发生日期
    private String centerno; //分中心
    private String invnm; 	//客户名称
    private String fundid; //基金代码
    private String fundAcct; //基金帐号
    private String tradeAcco; //交易帐号
    private double amt; 	//发生金额
    private String capiflag; //发生额标志
    private String bankno; //收款人银行编号
    private String nminbank; //收款人账户名称
    private String banknm; //收款人银行名称
    private String bankacco; //收款方银行账号
    private String appno; //申请编号
    private String moneytype; //币种
    private String netno; //网点编号
    private String capitalmode; //资金方式
    private String branchbank; //联行号
    private String accoid; //银行账户编号
    private String merchinedate; //机器日期yyyMMdd
    private String merchinetime; //机器时间HHmmss
    private String invtp; //客户类别
    private String assist; //业务辅助代码
    private String state; //划款状态
    private String spec; //保留字段
    private String billtype;
    private String ourbankno;
    //added by ouyz 20110531 凭证导出新增显示字段  begin
    private String bankAddr;//银行开户地省份
    private String bankAddrCity;//银行开户地市县
  //added by ouyz 20110531 凭证导出新增显示字段  end

    public String getBilltype() {
		return billtype;
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

	public void setBilltype(String billtype) {
		this.billtype = billtype;
	}

	public String getOurbankno() {
		return ourbankno;
	}

	public void setOurbankno(String ourbankno) {
		this.ourbankno = ourbankno;
	}

	private String errcode; //返回代码

    public String getAccoid() {
		return accoid;
	}

	public void setAccoid(String accoid) {
		this.accoid = accoid;
	}

	public String getBankacco() {
		return bankacco;
	}

	public void setBankacco(String bankacco) {
		this.bankacco = bankacco;
	}

	public String getBankno() {
		return bankno;
	}

	public void setBankno(String bankno) {
		this.bankno = bankno;
	}

	public String getBilldate() {
		return billdate;
	}

	public void setBilldate(String billdate) {
		this.billdate = billdate;
	}

	public String getBranchbank() {
		return branchbank;
	}

	public void setBranchbank(String branchbank) {
		this.branchbank = branchbank;
	}

	public String getCapitalmode() {
		return capitalmode;
	}

	public void setCapitalmode(String capitalmode) {
		this.capitalmode = capitalmode;
	}

	public String getCenterno() {
		return centerno;
	}

	public void setCenterno(String centerno) {
		this.centerno = centerno;
	}

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getFundAcct() {
		return fundAcct;
	}

	public void setFundAcct(String fundAcct) {
		this.fundAcct = fundAcct;
	}

	public String getFundid() {
		return fundid;
	}

	public void setFundid(String fundid) {
		this.fundid = fundid;
	}

	public String getInvnm() {
		return invnm;
	}

	public void setInvnm(String invnm) {
		this.invnm = invnm;
	}

	public String getInvtp() {
		return invtp;
	}

	public void setInvtp(String invtp) {
		this.invtp = invtp;
	}

	public String getBanknm() {
		return banknm;
	}

	public void setBanknm(String banknm) {
		this.banknm = banknm;
	}

	public String getMerchinetime() {
		return merchinetime;
	}

	public void setMerchinetime(String merchinetime) {
		this.merchinetime = merchinetime;
	}

	public String getNminbank() {
		return nminbank;
	}

	public void setNminbank(String nminbank) {
		this.nminbank = nminbank;
	}

	public double getAmt() {
		return amt;
	}

	public void setAmt(double amt) {
		this.amt = amt;
	}

	public String getAppno() {
		return appno;
	}

	public void setAppno(String appno) {
		this.appno = appno;
	}

	public String getAssist() {
		return assist;
	}

	public void setAssist(String assist) {
		this.assist = assist;
	}

	public String getCapiflag() {
		return capiflag;
	}

	public void setCapiflag(String capiflag) {
		this.capiflag = capiflag;
	}

	public String getMoneytype() {
		return moneytype;
	}

	public void setMoneytype(String moneytype) {
		this.moneytype = moneytype;
	}

	public String getNetno() {
		return netno;
	}

	public void setNetno(String netno) {
		this.netno = netno;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getTradeAcco() {
		return tradeAcco;
	}

	public void setTradeAcco(String tradeAcco) {
		this.tradeAcco = tradeAcco;
	}

	public CheckDataDto() {
    }

	public String getMerchinedate() {
		return merchinedate;
	}

	public void setMerchinedate(String merchinedate) {
		this.merchinedate = merchinedate;
	}

	public String getSpec() {
		return spec;
	}

	public void setSpec(String spec) {
		this.spec = spec;
	}

	@Override
	public String toString() {
		return "CheckDataDto [billdate=" + billdate + ", centerno=" + centerno
				+ ", invnm=" + invnm + ", fundid=" + fundid + ", fundAcct="
				+ fundAcct + ", tradeAcco=" + tradeAcco + ", amt=" + amt
				+ ", capiflag=" + capiflag + ", bankno=" + bankno
				+ ", nminbank=" + nminbank + ", banknm=" + banknm
				+ ", bankacco=" + bankacco + ", appno=" + appno
				+ ", moneytype=" + moneytype + ", netno=" + netno
				+ ", capitalmode=" + capitalmode + ", branchbank=" + branchbank
				+ ", accoid=" + accoid + ", merchinedate=" + merchinedate
				+ ", merchinetime=" + merchinetime + ", invtp=" + invtp
				+ ", assist=" + assist + ", state=" + state + ", spec=" + spec
				+ ", billtype=" + billtype + ", ourbankno=" + ourbankno
				+ ", bankAddr=" + bankAddr + ", bankAddrCity=" + bankAddrCity
				+ ", errcode=" + errcode + "]";
	}
}
