package com.cmwa.ecc.business.entity.fundinfo;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("fundBalanceVo")
public class FundBalanceVo implements Serializable {
    private String fundacct;
    private String invnm;
    private String custtp;
    private String custno;
    private String cycleenddt;
    private String quryType;
    private String tradeacco;
    private String bankno;
    private String bankacco;
    private String bankacnm;
    private String fundid;
    private String fundnm;
    private String fundst;
    private double balance;
    private double available;
    private double frozen;
    private double hfrozen;
    private double abnmfrozen;
    private double nav;
    private double balamt;
    

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

	public String getCusttp() {
		return custtp;
	}

	public void setCusttp(String custtp) {
		this.custtp = custtp;
	}

	public String getTradeacco() {
		return tradeacco;
	}

	public void setTradeacco(String tradeacco) {
		this.tradeacco = tradeacco;
	}

	public String getBankno() {
		return bankno;
	}

	public void setBankno(String bankno) {
		this.bankno = bankno;
	}

	public String getBankacco() {
		return bankacco;
	}

	public void setBankacco(String bankacco) {
		this.bankacco = bankacco;
	}

	public String getBankacnm() {
		return bankacnm;
	}

	public void setBankacnm(String bankacnm) {
		this.bankacnm = bankacnm;
	}

	public String getFundid() {
		return fundid;
	}

	public void setFundid(String fundid) {
		this.fundid = fundid;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getAvailable() {
		return available;
	}

	public void setAvailable(double available) {
		this.available = available;
	}

	public double getFrozen() {
		return frozen;
	}

	public void setFrozen(double frozen) {
		this.frozen = frozen;
	}

	public double getHfrozen() {
		return hfrozen;
	}

	public void setHfrozen(double hfrozen) {
		this.hfrozen = hfrozen;
	}

	public double getAbnmfrozen() {
		return abnmfrozen;
	}

	public void setAbnmfrozen(double abnmfrozen) {
		this.abnmfrozen = abnmfrozen;
	}

	public double getNav() {
		return nav;
	}

	public void setNav(double nav) {
		this.nav = nav;
	}

	public double getBalamt() {
		return balamt;
	}

	public void setBalamt(double balamt) {
		this.balamt = balamt;
	}

	public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof FundBalanceVo))
            return false;
        FundBalanceVo that = (FundBalanceVo) obj;
        if (!(that.fundacct == null ? this.fundacct == null :
              that.fundacct.equals(this.fundacct)))
            return false;
        if (!(that.invnm == null ? this.invnm == null :
              that.invnm.equals(this.invnm)))
            return false;
        if (!(that.custtp == null ? this.custtp == null :
              that.custtp.equals(this.custtp)))
            return false;
        if (!(that.tradeacco == null ? this.tradeacco == null :
              that.tradeacco.equals(this.tradeacco)))
            return false;
        if (!(that.bankno == null ? this.bankno == null :
              that.bankno.equals(this.bankno)))
            return false;
        if (!(that.bankacco == null ? this.bankacco == null :
              that.bankacco.equals(this.bankacco)))
            return false;
        if (!(that.bankacnm == null ? this.bankacnm == null :
              that.bankacnm.equals(this.bankacnm)))
            return false;
        if (!(that.fundid == null ? this.fundid == null :
              that.fundid.equals(this.fundid)))
            return false;
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.fundacct.hashCode();
        result = 37 * result + this.invnm.hashCode();
        result = 37 * result + this.custtp.hashCode();
        result = 37 * result + this.tradeacco.hashCode();
        result = 37 * result + this.bankno.hashCode();
        result = 37 * result + this.bankacco.hashCode();
        result = 37 * result + this.bankacnm.hashCode();
        result = 37 * result + this.fundid.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(448);
        returnStringBuffer.append("[");
        returnStringBuffer.append("fundacct:").append(fundacct);
        returnStringBuffer.append("invnm:").append(invnm);
        returnStringBuffer.append("custtp:").append(custtp);
        returnStringBuffer.append("tradeacco:").append(tradeacco);
        returnStringBuffer.append("bankno:").append(bankno);
        returnStringBuffer.append("bankacco:").append(bankacco);
        returnStringBuffer.append("bankacnm:").append(bankacnm);
        returnStringBuffer.append("fundid:").append(fundid);
        returnStringBuffer.append("balance:").append(balance);
        returnStringBuffer.append("available:").append(available);
        returnStringBuffer.append("frozen:").append(frozen);
        returnStringBuffer.append("hfrozen:").append(hfrozen);
        returnStringBuffer.append("abnmfrozen:").append(abnmfrozen);
        returnStringBuffer.append("nav:").append(nav);
        returnStringBuffer.append("]");
        return returnStringBuffer.toString();
    }

	public String getFundst() {
		return fundst;
	}

	public void setFundst(String fundst) {
		this.fundst = fundst == null ? "" : fundst;
	}

	public String getCustno() {
		return custno;
	}

	public void setCustno(String custno) {
		this.custno = custno;
	}

	public String getCycleenddt() {
		return cycleenddt;
	}

	public void setCycleenddt(String cycleenddt) {
		this.cycleenddt = cycleenddt;
	}

	public String getQuryType() {
		return quryType;
	}

	public void setQuryType(String quryType) {
		this.quryType = quryType;
	}

	public String getFundnm() {
		return fundnm;
	}

	public void setFundnm(String fundnm) {
		this.fundnm = fundnm;
	}
}