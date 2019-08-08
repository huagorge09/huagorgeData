package com.cmwa.ecc.business.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

/**
 * @author ex-wuh2
 *	资金流水
 */
@Alias("tradeCapitalBlotterVo")
public class TradeCapitalBlotterVo implements Serializable {
    private String transferdt;
    private String capitalno;
    private String fundacct;
    private String invnm;
    private String tradeacco;
    private String bankno;
    private String bankacco;
    private String capitaltp;
    private String serialno;
    private String apdt;
    private String apkind;
    private String apkindName;
	private String fundid;
    private String currencytype;
    private BigDecimal subamt;
    private BigDecimal ackamt;
    private BigDecimal debit;
    private String dcflag;
    private String chkflag;
    private String teller;
    private String checker;
    private String paymentno;
    private String paymentflag;
    private String paymentmsg;
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
    public String getTransferdt() {
        return transferdt;
    }

    public void setTransferdt(String transferdt) {
        this.transferdt = transferdt;
    }

    public String getCapitalno() {
        return capitalno;
    }

    public void setCapitalno(String capitalno) {
        this.capitalno = capitalno;
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

    public String getCapitaltp() {
        return capitaltp;
    }

    public void setCapitaltp(String capitaltp) {
        this.capitaltp = capitaltp;
    }

    public String getSerialno() {
        return serialno;
    }

    public void setSerialno(String serialno) {
        this.serialno = serialno;
    }

    public String getApdt() {
        return apdt;
    }

    public void setApdt(String apdt) {
        this.apdt = apdt;
    }

    public String getApkind() {
        return apkind;
    }

    public void setApkind(String apkind) {
        this.apkind = apkind;
    }

    public String getFundid() {
        return fundid;
    }

    public void setFundid(String fundid) {
        this.fundid = fundid;
    }

    public String getCurrencytype() {
        return currencytype;
    }

    public void setCurrencytype(String currencytype) {
        this.currencytype = currencytype;
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

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public String getDcflag() {
        return dcflag;
    }

    public void setDcflag(String dcflag) {
        this.dcflag = dcflag;
    }

    public String getChkflag() {
        return chkflag;
    }

    public void setChkflag(String chkflag) {
        this.chkflag = chkflag;
    }

    public String getTeller() {
        return teller;
    }

    public void setTeller(String teller) {
        this.teller = teller;
    }

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getPaymentno() {
        return paymentno;
    }

    public void setPaymentno(String paymentno) {
        this.paymentno = paymentno;
    }

    public String getPaymentflag() {
        return paymentflag;
    }

    public void setPaymentflag(String paymentflag) {
        this.paymentflag = paymentflag;
    }

    public String getPaymentmsg() {
        return paymentmsg;
    }

    public void setPaymentmsg(String paymentmsg) {
        this.paymentmsg = paymentmsg;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TradeCapitalBlotterVo))
            return false;
        TradeCapitalBlotterVo that = (TradeCapitalBlotterVo) obj;
        if (!(that.transferdt == null ? this.transferdt == null :
              that.transferdt.equals(this.transferdt)))
            return false;
        if (!(that.capitalno == null ? this.capitalno == null :
              that.capitalno.equals(this.capitalno)))
            return false;
        if (!(that.fundacct == null ? this.fundacct == null :
              that.fundacct.equals(this.fundacct)))
            return false;
        if (!(that.invnm == null ? this.invnm == null :
              that.invnm.equals(this.invnm)))
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
        if (!(that.capitaltp == null ? this.capitaltp == null :
              that.capitaltp.equals(this.capitaltp)))
            return false;
        if (!(that.serialno == null ? this.serialno == null :
              that.serialno.equals(this.serialno)))
            return false;
        if (!(that.apdt == null ? this.apdt == null :
              that.apdt.equals(this.apdt)))
            return false;
        if (!(that.apkind == null ? this.apkind == null :
              that.apkind.equals(this.apkind)))
            return false;
        if (!(that.fundid == null ? this.fundid == null :
              that.fundid.equals(this.fundid)))
            return false;
        if (!(that.currencytype == null ? this.currencytype == null :
              that.currencytype.equals(this.currencytype)))
            return false;
        if (!(that.subamt == null ? this.subamt == null :
              that.subamt.equals(this.subamt)))
            return false;
        if (!(that.ackamt == null ? this.ackamt == null :
              that.ackamt.equals(this.ackamt)))
            return false;
        if (!(that.debit == null ? this.debit == null :
              that.debit.equals(this.debit)))
            return false;
        if (!(that.dcflag == null ? this.dcflag == null :
              that.dcflag.equals(this.dcflag)))
            return false;
        if (!(that.chkflag == null ? this.chkflag == null :
              that.chkflag.equals(this.chkflag)))
            return false;
        if (!(that.teller == null ? this.teller == null :
              that.teller.equals(this.teller)))
            return false;
        if (!(that.checker == null ? this.checker == null :
              that.checker.equals(this.checker)))
            return false;
        if (!(that.paymentno == null ? this.paymentno == null :
              that.paymentno.equals(this.paymentno)))
            return false;
        if (!(that.paymentflag == null ? this.paymentflag == null :
              that.paymentflag.equals(this.paymentflag)))
            return false;
        if (!(that.paymentmsg == null ? this.paymentmsg == null :
              that.paymentmsg.equals(this.paymentmsg)))
            return false;
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.transferdt.hashCode();
        result = 37 * result + this.capitalno.hashCode();
        result = 37 * result + this.fundacct.hashCode();
        result = 37 * result + this.invnm.hashCode();
        result = 37 * result + this.tradeacco.hashCode();
        result = 37 * result + this.bankno.hashCode();
        result = 37 * result + this.bankacco.hashCode();
        result = 37 * result + this.capitaltp.hashCode();
        result = 37 * result + this.serialno.hashCode();
        result = 37 * result + this.apdt.hashCode();
        result = 37 * result + this.apkind.hashCode();
        result = 37 * result + this.fundid.hashCode();
        result = 37 * result + this.currencytype.hashCode();
        result = 37 * result + this.subamt.hashCode();
        result = 37 * result + this.ackamt.hashCode();
        result = 37 * result + this.debit.hashCode();
        result = 37 * result + this.dcflag.hashCode();
        result = 37 * result + this.chkflag.hashCode();
        result = 37 * result + this.teller.hashCode();
        result = 37 * result + this.checker.hashCode();
        result = 37 * result + this.paymentno.hashCode();
        result = 37 * result + this.paymentflag.hashCode();
        result = 37 * result + this.paymentmsg.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(736);
        returnStringBuffer.append("[");
        returnStringBuffer.append("transferdt:").append(transferdt);
        returnStringBuffer.append("capitalno:").append(capitalno);
        returnStringBuffer.append("fundacct:").append(fundacct);
        returnStringBuffer.append("invnm:").append(invnm);
        returnStringBuffer.append("tradeacco:").append(tradeacco);
        returnStringBuffer.append("bankno:").append(bankno);
        returnStringBuffer.append("bankacco:").append(bankacco);
        returnStringBuffer.append("capitaltp:").append(capitaltp);
        returnStringBuffer.append("serialno:").append(serialno);
        returnStringBuffer.append("apdt:").append(apdt);
        returnStringBuffer.append("apkind:").append(apkind);
        returnStringBuffer.append("fundid:").append(fundid);
        returnStringBuffer.append("currencytype:").append(currencytype);
        returnStringBuffer.append("subamt:").append(subamt);
        returnStringBuffer.append("ackamt:").append(ackamt);
        returnStringBuffer.append("debit:").append(debit);
        returnStringBuffer.append("dcflag:").append(dcflag);
        returnStringBuffer.append("chkflag:").append(chkflag);
        returnStringBuffer.append("teller:").append(teller);
        returnStringBuffer.append("checker:").append(checker);
        returnStringBuffer.append("paymentno:").append(paymentno);
        returnStringBuffer.append("paymentflag:").append(paymentflag);
        returnStringBuffer.append("paymentmsg:").append(paymentmsg);
        returnStringBuffer.append("]");
        return returnStringBuffer.toString();
    }
}

