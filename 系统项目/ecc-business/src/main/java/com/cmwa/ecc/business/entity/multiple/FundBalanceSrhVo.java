package com.cmwa.ecc.business.entity.multiple;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

import com.cmwa.ecc.business.entity.fundinfo.FundBalanceVo;


@Alias("fundBalanceVoSrh")
public class FundBalanceSrhVo implements Serializable {
	    private String fundacct;
	    private String invnm;
	    private String custtp;
	    private String tradeacco;
	    private String bankno;
	    private String bankacco;
	    private String bankacnm;
	    private String fundid;
	    private String fundnm;
		private String fundst;
	    private BigDecimal balance;
	    private BigDecimal available;
	    private BigDecimal frozen;
	    private BigDecimal hfrozen;
	    private BigDecimal abnmfrozen;
	    private BigDecimal nav;
	    private String netpoint;
	    public String getNetpoint() {
			return netpoint;
		}

		public void setNetpoint(String netpoint) {
			this.netpoint = netpoint;
		}
	    public String getFundnm() {
			return fundnm;
		}

		public void setFundnm(String fundnm) {
			this.fundnm = fundnm;
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

	    public BigDecimal getBalance() {
	        return balance;
	    }

	    public void setBalance(BigDecimal balance) {
	        this.balance = balance;
	    }

	    public BigDecimal getAvailable() {
	        return available;
	    }

	    public void setAvailable(BigDecimal available) {
	        this.available = available;
	    }

	    public BigDecimal getFrozen() {
	        return frozen;
	    }

	    public void setFrozen(BigDecimal frozen) {
	        this.frozen = frozen;
	    }

	    public BigDecimal getHfrozen() {
	        return hfrozen;
	    }

	    public void setHfrozen(BigDecimal hfrozen) {
	        this.hfrozen = hfrozen;
	    }

	    public BigDecimal getAbnmfrozen() {
	        return abnmfrozen;
	    }

	    public void setAbnmfrozen(BigDecimal abnmfrozen) {
	        this.abnmfrozen = abnmfrozen;
	    }

	    public BigDecimal getNav() {
	        return nav;
	    }

	    public void setNav(BigDecimal nav) {
	        this.nav = nav;
	    }

	    public boolean equals(Object obj) {
	        if (this == obj)
	            return true;
	        if (!(obj instanceof FundBalanceSrhVo))
	            return false;
	        FundBalanceSrhVo that = (FundBalanceSrhVo) obj;
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
	        if (!(that.balance == null ? this.balance == null :
	              that.balance.equals(this.balance)))
	            return false;
	        if (!(that.available == null ? this.available == null :
	              that.available.equals(this.available)))
	            return false;
	        if (!(that.frozen == null ? this.frozen == null :
	              that.frozen.equals(this.frozen)))
	            return false;
	        if (!(that.hfrozen == null ? this.hfrozen == null :
	              that.hfrozen.equals(this.hfrozen)))
	            return false;
	        if (!(that.abnmfrozen == null ? this.abnmfrozen == null :
	              that.abnmfrozen.equals(this.abnmfrozen)))
	            return false;
	        if (!(that.nav == null ? this.nav == null : that.nav.equals(this.nav)))
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
	        result = 37 * result + this.balance.hashCode();
	        result = 37 * result + this.available.hashCode();
	        result = 37 * result + this.frozen.hashCode();
	        result = 37 * result + this.hfrozen.hashCode();
	        result = 37 * result + this.abnmfrozen.hashCode();
	        result = 37 * result + this.nav.hashCode();
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
			this.fundst = fundst;
		}
}

