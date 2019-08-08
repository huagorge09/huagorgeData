package com.cmwa.ecc.business.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

/**
 * @author ex-wuh2
 *	TA交易Vo
 */
@Alias("tradeAckTodayVo")
public class TradeAckTodayVo implements Serializable {
    private String ackdt;
    private String ackno;
    private String retcode;
    private String retmsg;
    private String apdt;
    private String serialno;
    private String appno;
    private String fundid;
    private String apkind;
    private String apkindName;
    private String fundacct;
    private String tradeacco;
    private String bankno;
    private String bankacco;
    private String invnm;
    private String invtp;
    private String invtpName;
    private BigDecimal subamt;
    private BigDecimal ackamt;
    private String currencytype;
    private BigDecimal subquty;
    private BigDecimal ackquty;
    private BigDecimal acknav;
    private BigDecimal commro;
    private BigDecimal fee;
    private BigDecimal agencyfee;
    private BigDecimal transfermfee;
    private BigDecimal otherfee1;
    private BigDecimal balance;
    private String oseatno;
    private String ofundacct;
    private String ofundid;
    private String melonmd;
    private String melonmdName;
	private BigDecimal dividendrate;
    private String frozencause;
    private String netpoint;
    public String getNetpoint() {
		return netpoint;
	}

	public void setNetpoint(String netpoint) {
		this.netpoint = netpoint;
	}

	public String getApkindName() {
		return apkindName;
	}

	public void setApkindName(String apkindName) {
		this.apkindName = apkindName;
	}

	public String getInvtpName() {
		return invtpName;
	}

	public void setInvtpName(String invtpName) {
		this.invtpName = invtpName;
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

    public String getApdt() {
        return apdt;
    }

    public void setApdt(String apdt) {
        this.apdt = apdt;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getAppno() {
        return appno;
    }

    public void setAppno(String appno) {
        this.appno = appno;
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

    public BigDecimal getSubamt() {
        return subamt;
    }

    public void setSubamt(BigDecimal subamt) {
        this.subamt = subamt;
    }

    public BigDecimal getAckamt() {
        return ackamt;
    }

    public void setAckamt(BigDecimal ackamt) {
        this.ackamt = ackamt;
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

    public BigDecimal getAckquty() {
        return ackquty;
    }

    public void setAckquty(BigDecimal ackquty) {
        this.ackquty = ackquty;
    }

    public BigDecimal getAcknav() {
        return acknav;
    }

    public void setAcknav(BigDecimal acknav) {
        this.acknav = acknav;
    }

    public BigDecimal getCommro() {
        return commro;
    }

    public void setCommro(BigDecimal commro) {
        this.commro = commro;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getAgencyfee() {
        return agencyfee;
    }

    public void setAgencyfee(BigDecimal agencyfee) {
        this.agencyfee = agencyfee;
    }

    public BigDecimal getTransfermfee() {
        return transfermfee;
    }

    public void setTransfermfee(BigDecimal transfermfee) {
        this.transfermfee = transfermfee;
    }

    public BigDecimal getOtherfee1() {
        return otherfee1;
    }

    public void setOtherfee1(BigDecimal otherfee1) {
        this.otherfee1 = otherfee1;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getOseatno() {
        return oseatno;
    }

    public void setOseatno(String oseatno) {
        this.oseatno = oseatno;
    }

    public String getOfundacct() {
        return ofundacct;
    }

    public void setOfundacct(String ofundacct) {
        this.ofundacct = ofundacct;
    }

    public String getOfundid() {
        return ofundid;
    }

    public void setOfundid(String ofundid) {
        this.ofundid = ofundid;
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

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TradeAckTodayVo))
            return false;
        TradeAckTodayVo that = (TradeAckTodayVo) obj;
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
        if (!(that.apdt == null ? this.apdt == null :
              that.apdt.equals(this.apdt)))
            return false;
        if (!(that.serialno == null ? this.serialno == null :
              that.serialno.equals(this.serialno)))
            return false;
        if (!(that.appno == null ? this.appno == null :
              that.appno.equals(this.appno)))
            return false;
        if (!(that.fundid == null ? this.fundid == null :
              that.fundid.equals(this.fundid)))
            return false;
        if (!(that.apkind == null ? this.apkind == null :
              that.apkind.equals(this.apkind)))
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
        if (!(that.invnm == null ? this.invnm == null :
              that.invnm.equals(this.invnm)))
            return false;
        if (!(that.invtp == null ? this.invtp == null :
              that.invtp.equals(this.invtp)))
            return false;
        if (!(that.subamt == null ? this.subamt == null :
              that.subamt.equals(this.subamt)))
            return false;
        if (!(that.ackamt == null ? this.ackamt == null :
              that.ackamt.equals(this.ackamt)))
            return false;
        if (!(that.currencytype == null ? this.currencytype == null :
              that.currencytype.equals(this.currencytype)))
            return false;
        if (!(that.subquty == null ? this.subquty == null :
              that.subquty.equals(this.subquty)))
            return false;
        if (!(that.ackquty == null ? this.ackquty == null :
              that.ackquty.equals(this.ackquty)))
            return false;
        if (!(that.acknav == null ? this.acknav == null :
              that.acknav.equals(this.acknav)))
            return false;
        if (!(that.commro == null ? this.commro == null :
              that.commro.equals(this.commro)))
            return false;
        if (!(that.fee == null ? this.fee == null : that.fee.equals(this.fee)))
            return false;
        if (!(that.agencyfee == null ? this.agencyfee == null :
              that.agencyfee.equals(this.agencyfee)))
            return false;
        if (!(that.transfermfee == null ? this.transfermfee == null :
              that.transfermfee.equals(this.transfermfee)))
            return false;
        if (!(that.otherfee1 == null ? this.otherfee1 == null :
              that.otherfee1.equals(this.otherfee1)))
            return false;
        if (!(that.balance == null ? this.balance == null :
              that.balance.equals(this.balance)))
            return false;
        if (!(that.oseatno == null ? this.oseatno == null :
              that.oseatno.equals(this.oseatno)))
            return false;
        if (!(that.ofundacct == null ? this.ofundacct == null :
              that.ofundacct.equals(this.ofundacct)))
            return false;
        if (!(that.ofundid == null ? this.ofundid == null :
              that.ofundid.equals(this.ofundid)))
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
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.ackdt.hashCode();
        result = 37 * result + this.ackno.hashCode();
        result = 37 * result + this.retcode.hashCode();
        result = 37 * result + this.retmsg.hashCode();
        result = 37 * result + this.apdt.hashCode();
        result = 37 * result + this.serialno.hashCode();
        result = 37 * result + this.appno.hashCode();
        result = 37 * result + this.fundid.hashCode();
        result = 37 * result + this.apkind.hashCode();
        result = 37 * result + this.fundacct.hashCode();
        result = 37 * result + this.tradeacco.hashCode();
        result = 37 * result + this.bankno.hashCode();
        result = 37 * result + this.bankacco.hashCode();
        result = 37 * result + this.invnm.hashCode();
        result = 37 * result + this.invtp.hashCode();
        result = 37 * result + this.subamt.hashCode();
        result = 37 * result + this.ackamt.hashCode();
        result = 37 * result + this.currencytype.hashCode();
        result = 37 * result + this.subquty.hashCode();
        result = 37 * result + this.ackquty.hashCode();
        result = 37 * result + this.acknav.hashCode();
        result = 37 * result + this.commro.hashCode();
        result = 37 * result + this.fee.hashCode();
        result = 37 * result + this.agencyfee.hashCode();
        result = 37 * result + this.transfermfee.hashCode();
        result = 37 * result + this.otherfee1.hashCode();
        result = 37 * result + this.balance.hashCode();
        result = 37 * result + this.oseatno.hashCode();
        result = 37 * result + this.ofundacct.hashCode();
        result = 37 * result + this.ofundid.hashCode();
        result = 37 * result + this.melonmd.hashCode();
        result = 37 * result + this.dividendrate.hashCode();
        result = 37 * result + this.frozencause.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(1056);
        returnStringBuffer.append("[");
        returnStringBuffer.append("ackdt:").append(ackdt);
        returnStringBuffer.append("ackno:").append(ackno);
        returnStringBuffer.append("retcode:").append(retcode);
        returnStringBuffer.append("retmsg:").append(retmsg);
        returnStringBuffer.append("apdt:").append(apdt);
        returnStringBuffer.append("serialno:").append(serialno);
        returnStringBuffer.append("appno:").append(appno);
        returnStringBuffer.append("fundid:").append(fundid);
        returnStringBuffer.append("apkind:").append(apkind);
        returnStringBuffer.append("fundacct:").append(fundacct);
        returnStringBuffer.append("tradeacco:").append(tradeacco);
        returnStringBuffer.append("bankno:").append(bankno);
        returnStringBuffer.append("bankacco:").append(bankacco);
        returnStringBuffer.append("invnm:").append(invnm);
        returnStringBuffer.append("invtp:").append(invtp);
        returnStringBuffer.append("subamt:").append(subamt);
        returnStringBuffer.append("ackamt:").append(ackamt);
        returnStringBuffer.append("currencytype:").append(currencytype);
        returnStringBuffer.append("subquty:").append(subquty);
        returnStringBuffer.append("ackquty:").append(ackquty);
        returnStringBuffer.append("acknav:").append(acknav);
        returnStringBuffer.append("commro:").append(commro);
        returnStringBuffer.append("fee:").append(fee);
        returnStringBuffer.append("agencyfee:").append(agencyfee);
        returnStringBuffer.append("transfermfee:").append(transfermfee);
        returnStringBuffer.append("otherfee1:").append(otherfee1);
        returnStringBuffer.append("balance:").append(balance);
        returnStringBuffer.append("oseatno:").append(oseatno);
        returnStringBuffer.append("ofundacct:").append(ofundacct);
        returnStringBuffer.append("ofundid:").append(ofundid);
        returnStringBuffer.append("melonmd:").append(melonmd);
        returnStringBuffer.append("dividendrate:").append(dividendrate);
        returnStringBuffer.append("frozencause:").append(frozencause);
        returnStringBuffer.append("]");
        return returnStringBuffer.toString();
    }
}
