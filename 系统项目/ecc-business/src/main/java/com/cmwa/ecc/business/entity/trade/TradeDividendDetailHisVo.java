package com.cmwa.ecc.business.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

/**
 * @author ex-wuh2
 *	历史分红
 */
@Alias("tradeDividendDetailHisVo")
public class TradeDividendDetailHisVo implements Serializable {
    private String ackdt;
    private String ackno;
    private String retcode;
    private String retmsg;
    private String fundacct;
    private String tradeacco;
    private String bankno;
    private String bankacco;
    private String invtp;
    private String invtpName;
    private String invnm;
    private String fundid;
    private String apkind;
    private String apkindName;
    private String currencytype;
    private BigDecimal subquty;
    private BigDecimal subamt;
    private BigDecimal ackquty;
    private BigDecimal frozenbalance;
    private BigDecimal ackamt;
    private BigDecimal balance;
    private BigDecimal available;
    private BigDecimal acknav;
    private String melonmd;
    private String melonmdName;
    private BigDecimal dividendrate;
	private String frozencause;
    private String applyst;
    private String netpoint;
    public String getNetpoint() {
		return netpoint;
	}

	public void setNetpoint(String netpoint) {
		this.netpoint = netpoint;
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

	public String getMelonmdName() {
		return melonmdName;
	}

	public void setMelonmdName(String melonmdName) {
		this.melonmdName = melonmdName;
	}
    public String getAckdt() {
        return ackdt;
    }

    public void setAckdt(String ackdt) {
        this.ackdt = ackdt;
    }

    public String getAckno() {
        return ackno;
    }

    public void setAckno(String ackno) {
        this.ackno = ackno;
    }

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public String getRetmsg() {
        return retmsg;
    }

    public void setRetmsg(String retmsg) {
        this.retmsg = retmsg;
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

    public String getFundid() {
        return fundid;
    }

    public void setFundid(String fundid) {
        this.fundid = fundid;
    }

    public String getApkind() {
        return apkind;
    }

    public void setApkind(String apkind) {
        this.apkind = apkind;
    }

    public String getCurrencytype() {
        return currencytype;
    }

    public void setCurrencytype(String currencytype) {
        this.currencytype = currencytype;
    }

    public BigDecimal getSubquty() {
        return subquty;
    }

    public void setSubquty(BigDecimal subquty) {
        this.subquty = subquty;
    }

    public BigDecimal getSubamt() {
        return subamt;
    }

    public void setSubamt(BigDecimal subamt) {
        this.subamt = subamt;
    }

    public BigDecimal getAckquty() {
        return ackquty;
    }

    public void setAckquty(BigDecimal ackquty) {
        this.ackquty = ackquty;
    }

    public BigDecimal getFrozenbalance() {
        return frozenbalance;
    }

    public void setFrozenbalance(BigDecimal frozenbalance) {
        this.frozenbalance = frozenbalance;
    }

    public BigDecimal getAckamt() {
        return ackamt;
    }

    public void setAckamt(BigDecimal ackamt) {
        this.ackamt = ackamt;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getAcknav() {
        return acknav;
    }

    public void setAcknav(BigDecimal acknav) {
        this.acknav = acknav;
    }

    public String getMelonmd() {
        return melonmd;
    }

    public void setMelonmd(String melonmd) {
        this.melonmd = melonmd;
    }

    public BigDecimal getDividendrate() {
        return dividendrate;
    }

    public void setDividendrate(BigDecimal dividendrate) {
        this.dividendrate = dividendrate;
    }

    public String getFrozencause() {
        return frozencause;
    }

    public void setFrozencause(String frozencause) {
        this.frozencause = frozencause;
    }

    public String getApplyst() {
        return applyst;
    }

    public void setApplyst(String applyst) {
        this.applyst = applyst;
    }
    
	public BigDecimal getAvailable() {
		return available;
	}

	public void setAvailable(BigDecimal available) {
		this.available = available;
	}

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TradeDividendDetailHisVo))
            return false;
        TradeDividendDetailHisVo that = (TradeDividendDetailHisVo) obj;
        if (!(that.ackdt == null ? this.ackdt == null :
              that.ackdt.equals(this.ackdt)))
            return false;
        if (!(that.ackno == null ? this.ackno == null :
              that.ackno.equals(this.ackno)))
            return false;
        if (!(that.retcode == null ? this.retcode == null :
              that.retcode.equals(this.retcode)))
            return false;
        if (!(that.retmsg == null ? this.retmsg == null :
              that.retmsg.equals(this.retmsg)))
            return false;
        if (!(that.fundacct == null ? this.fundacct == null :
              that.fundacct.equals(this.fundacct)))
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
        if (!(that.invtp == null ? this.invtp == null :
              that.invtp.equals(this.invtp)))
            return false;
        if (!(that.invnm == null ? this.invnm == null :
              that.invnm.equals(this.invnm)))
            return false;
        if (!(that.fundid == null ? this.fundid == null :
              that.fundid.equals(this.fundid)))
            return false;
        if (!(that.apkind == null ? this.apkind == null :
              that.apkind.equals(this.apkind)))
            return false;
        if (!(that.currencytype == null ? this.currencytype == null :
              that.currencytype.equals(this.currencytype)))
            return false;
        if (!(that.subquty == null ? this.subquty == null :
              that.subquty.equals(this.subquty)))
            return false;
        if (!(that.subamt == null ? this.subamt == null :
              that.subamt.equals(this.subamt)))
            return false;
        if (!(that.ackquty == null ? this.ackquty == null :
              that.ackquty.equals(this.ackquty)))
            return false;
        if (!(that.frozenbalance == null ? this.frozenbalance == null :
              that.frozenbalance.equals(this.frozenbalance)))
            return false;
        if (!(that.ackamt == null ? this.ackamt == null :
              that.ackamt.equals(this.ackamt)))
            return false;
        if (!(that.balance == null ? this.balance == null :
              that.balance.equals(this.balance)))
            return false;
        if (!(that.available == null ? this.available == null :
            that.available.equals(this.available)))
          return false;        
        if (!(that.acknav == null ? this.acknav == null :
              that.acknav.equals(this.acknav)))
            return false;
        if (!(that.melonmd == null ? this.melonmd == null :
              that.melonmd.equals(this.melonmd)))
            return false;
        if (!(that.dividendrate == null ? this.dividendrate == null :
              that.dividendrate.equals(this.dividendrate)))
            return false;
        if (!(that.frozencause == null ? this.frozencause == null :
              that.frozencause.equals(this.frozencause)))
            return false;
        if (!(that.applyst == null ? this.applyst == null :
              that.applyst.equals(this.applyst)))
            return false;
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.ackdt.hashCode();
        result = 37 * result + this.ackno.hashCode();
        result = 37 * result + this.retcode.hashCode();
        result = 37 * result + this.retmsg.hashCode();
        result = 37 * result + this.fundacct.hashCode();
        result = 37 * result + this.tradeacco.hashCode();
        result = 37 * result + this.bankno.hashCode();
        result = 37 * result + this.bankacco.hashCode();
        result = 37 * result + this.invtp.hashCode();
        result = 37 * result + this.invnm.hashCode();
        result = 37 * result + this.fundid.hashCode();
        result = 37 * result + this.apkind.hashCode();
        result = 37 * result + this.currencytype.hashCode();
        result = 37 * result + this.subquty.hashCode();
        result = 37 * result + this.subamt.hashCode();
        result = 37 * result + this.ackquty.hashCode();
        result = 37 * result + this.frozenbalance.hashCode();
        result = 37 * result + this.ackamt.hashCode();
        result = 37 * result + this.balance.hashCode();
        result = 37 * result + this.available.hashCode();
        result = 37 * result + this.acknav.hashCode();
        result = 37 * result + this.melonmd.hashCode();
        result = 37 * result + this.dividendrate.hashCode();
        result = 37 * result + this.frozencause.hashCode();
        result = 37 * result + this.applyst.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(768);
        returnStringBuffer.append("[");
        returnStringBuffer.append("ackdt:").append(ackdt);
        returnStringBuffer.append("ackno:").append(ackno);
        returnStringBuffer.append("retcode:").append(retcode);
        returnStringBuffer.append("retmsg:").append(retmsg);
        returnStringBuffer.append("fundacct:").append(fundacct);
        returnStringBuffer.append("tradeacco:").append(tradeacco);
        returnStringBuffer.append("bankno:").append(bankno);
        returnStringBuffer.append("bankacco:").append(bankacco);
        returnStringBuffer.append("invtp:").append(invtp);
        returnStringBuffer.append("invnm:").append(invnm);
        returnStringBuffer.append("fundid:").append(fundid);
        returnStringBuffer.append("apkind:").append(apkind);
        returnStringBuffer.append("currencytype:").append(currencytype);
        returnStringBuffer.append("subquty:").append(subquty);
        returnStringBuffer.append("subamt:").append(subamt);
        returnStringBuffer.append("ackquty:").append(ackquty);
        returnStringBuffer.append("frozenbalance:").append(frozenbalance);
        returnStringBuffer.append("ackamt:").append(ackamt);
        returnStringBuffer.append("balance:").append(balance);
        returnStringBuffer.append("available:").append(available);
        returnStringBuffer.append("acknav:").append(acknav);
        returnStringBuffer.append("melonmd:").append(melonmd);
        returnStringBuffer.append("dividendrate:").append(dividendrate);
        returnStringBuffer.append("frozencause:").append(frozencause);
        returnStringBuffer.append("applyst:").append(applyst);
        returnStringBuffer.append("]");
        return returnStringBuffer.toString();
    }
}