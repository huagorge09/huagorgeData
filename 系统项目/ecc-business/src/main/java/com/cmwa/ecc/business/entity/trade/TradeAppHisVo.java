package com.cmwa.ecc.business.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

/**
 * @author ex-wuh2
 *	历史交易
 */
@Alias("tradeAppHisVo")
public class TradeAppHisVo  implements Serializable {
    private String apdt;
    private String serialno;
    private String appno;
    private String fundacct;
    private String invtp;
    private String invtpName;
    private String invnm;
    private String tradeacco;
    private String bankno;
    private String netpoint;
    private String bankacco;
    private String apkind;
    private String apkindName;
    private String fundid;
    private BigDecimal subquty;
    private BigDecimal subamt;
    private String currencytype;
    private BigDecimal commro;
	private String oseatno;
    private String ofundacct;//对方基金账号
    private String otradeacco;//对方交易账号
    private String ofundid;
    private String ofundname;
    private String fundname;
    private String melonmd;
    private String melonmdName;
    private BigDecimal dividendrate;
	private String frozencause;//冻结原因
    private String largeredeemflag;
    private String oldappno;
    private String paytype;
    private String payst;
    private String capitalno;
    private String applyst;
    private String transst;
    
  //2010-08-11 wdw add
    private String broker;//经办人名称
    private String brokeridtp;//经办人证件类型
    private String brokeridno;//经办人证件号码
    private String brokertel;//经办人办公电话
    private String brokerfax;//经办人传真
    private String checkflag;//资金复核状态
    private String bookingdt;//预约日期
    private String validays;//有效天数
    private String specredeemflag;//指定赎回标志
    private String specreadeemdt;//指定赎回日期
    private String delaydt;//顺延日期
    private String oseatnm;//申请编号
    public String getOseatnm() {
		return oseatnm;
	}

	public void setOseatnm(String oseatnm) {
		this.oseatnm = oseatnm;
	}
    public String getOfundname() {
		return ofundname;
	}

	public void setOfundname(String ofundname) {
		this.ofundname = ofundname;
	}

	public String getFundname() {
		return fundname;
	}

	public void setFundname(String fundname) {
		this.fundname = fundname;
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

    public String getFundacct() {
        return fundacct;
    }

    public void setFundacct(String fundacct) {
        this.fundacct = fundacct;
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

    public String getCurrencytype() {
        return currencytype;
    }

    public void setCurrencytype(String currencytype) {
        this.currencytype = currencytype;
    }

    public BigDecimal getCommro() {
        return commro;
    }

    public void setCommro(BigDecimal commro) {
        this.commro = commro;
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

    public String getOtradeacco() {
        return otradeacco;
    }

    public void setOtradeacco(String otradeacco) {
        this.otradeacco = otradeacco;
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

    public String getLargeredeemflag() {
        return largeredeemflag;
    }

    public void setLargeredeemflag(String largeredeemflag) {
        this.largeredeemflag = largeredeemflag;
    }

    public String getOldappno() {
        return oldappno;
    }

    public void setOldappno(String oldappno) {
        this.oldappno = oldappno;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public String getPayst() {
        return payst;
    }

    public void setPayst(String payst) {
        this.payst = payst;
    }

    public String getCapitalno() {
        return capitalno;
    }

    public void setCapitalno(String capitalno) {
        this.capitalno = capitalno;
    }

    public String getApplyst() {
        return applyst;
    }

    public void setApplyst(String applyst) {
        this.applyst = applyst;
    }

    public String getTransst() {
        return transst;
    }

    public void setTransst(String transst) {
        this.transst = transst;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TradeAppHisVo))
            return false;
        TradeAppHisVo that = (TradeAppHisVo) obj;
        if (!(that.apdt == null ? this.apdt == null :
              that.apdt.equals(this.apdt)))
            return false;
        if (!(that.serialno == null ? this.serialno == null :
              that.serialno.equals(this.serialno)))
            return false;
        if (!(that.appno == null ? this.appno == null :
              that.appno.equals(this.appno)))
            return false;
        if (!(that.fundacct == null ? this.fundacct == null :
              that.fundacct.equals(this.fundacct)))
            return false;
        if (!(that.invtp == null ? this.invtp == null :
              that.invtp.equals(this.invtp)))
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
        if (!(that.apkind == null ? this.apkind == null :
              that.apkind.equals(this.apkind)))
            return false;
        if (!(that.fundid == null ? this.fundid == null :
              that.fundid.equals(this.fundid)))
            return false;
        if (!(that.subquty == null ? this.subquty == null :
              that.subquty.equals(this.subquty)))
            return false;
        if (!(that.subamt == null ? this.subamt == null :
              that.subamt.equals(this.subamt)))
            return false;
        if (!(that.currencytype == null ? this.currencytype == null :
              that.currencytype.equals(this.currencytype)))
            return false;
        if (!(that.commro == null ? this.commro == null :
              that.commro.equals(this.commro)))
            return false;
        if (!(that.oseatno == null ? this.oseatno == null :
              that.oseatno.equals(this.oseatno)))
            return false;
        if (!(that.ofundacct == null ? this.ofundacct == null :
              that.ofundacct.equals(this.ofundacct)))
            return false;
        if (!(that.otradeacco == null ? this.otradeacco == null :
              that.otradeacco.equals(this.otradeacco)))
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
        if (!(that.largeredeemflag == null ? this.largeredeemflag == null :
              that.largeredeemflag.equals(this.largeredeemflag)))
            return false;
        if (!(that.oldappno == null ? this.oldappno == null :
              that.oldappno.equals(this.oldappno)))
            return false;
        if (!(that.paytype == null ? this.paytype == null :
              that.paytype.equals(this.paytype)))
            return false;
        if (!(that.payst == null ? this.payst == null :
              that.payst.equals(this.payst)))
            return false;
        if (!(that.capitalno == null ? this.capitalno == null :
              that.capitalno.equals(this.capitalno)))
            return false;
        if (!(that.applyst == null ? this.applyst == null :
              that.applyst.equals(this.applyst)))
            return false;
        if (!(that.transst == null ? this.transst == null :
              that.transst.equals(this.transst)))
            return false;
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.apdt.hashCode();
        result = 37 * result + this.serialno.hashCode();
        result = 37 * result + this.appno.hashCode();
        result = 37 * result + this.fundacct.hashCode();
        result = 37 * result + this.invtp.hashCode();
        result = 37 * result + this.invnm.hashCode();
        result = 37 * result + this.tradeacco.hashCode();
        result = 37 * result + this.bankno.hashCode();
        result = 37 * result + this.bankacco.hashCode();
        result = 37 * result + this.apkind.hashCode();
        result = 37 * result + this.fundid.hashCode();
        result = 37 * result + this.subquty.hashCode();
        result = 37 * result + this.subamt.hashCode();
        result = 37 * result + this.currencytype.hashCode();
        result = 37 * result + this.commro.hashCode();
        result = 37 * result + this.oseatno.hashCode();
        result = 37 * result + this.ofundacct.hashCode();
        result = 37 * result + this.otradeacco.hashCode();
        result = 37 * result + this.ofundid.hashCode();
        result = 37 * result + this.melonmd.hashCode();
        result = 37 * result + this.dividendrate.hashCode();
        result = 37 * result + this.frozencause.hashCode();
        result = 37 * result + this.largeredeemflag.hashCode();
        result = 37 * result + this.oldappno.hashCode();
        result = 37 * result + this.paytype.hashCode();
        result = 37 * result + this.payst.hashCode();
        result = 37 * result + this.capitalno.hashCode();
        result = 37 * result + this.applyst.hashCode();
        result = 37 * result + this.transst.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(928);
        returnStringBuffer.append("[");
        returnStringBuffer.append("apdt:").append(apdt);
        returnStringBuffer.append("serialno:").append(serialno);
        returnStringBuffer.append("appno:").append(appno);
        returnStringBuffer.append("fundacct:").append(fundacct);
        returnStringBuffer.append("invtp:").append(invtp);
        returnStringBuffer.append("invnm:").append(invnm);
        returnStringBuffer.append("tradeacco:").append(tradeacco);
        returnStringBuffer.append("bankno:").append(bankno);
        returnStringBuffer.append("bankacco:").append(bankacco);
        returnStringBuffer.append("apkind:").append(apkind);
        returnStringBuffer.append("fundid:").append(fundid);
        returnStringBuffer.append("subquty:").append(subquty);
        returnStringBuffer.append("subamt:").append(subamt);
        returnStringBuffer.append("currencytype:").append(currencytype);
        returnStringBuffer.append("commro:").append(commro);
        returnStringBuffer.append("oseatno:").append(oseatno);
        returnStringBuffer.append("ofundacct:").append(ofundacct);
        returnStringBuffer.append("otradeacco:").append(otradeacco);
        returnStringBuffer.append("ofundid:").append(ofundid);
        returnStringBuffer.append("melonmd:").append(melonmd);
        returnStringBuffer.append("dividendrate:").append(dividendrate);
        returnStringBuffer.append("frozencause:").append(frozencause);
        returnStringBuffer.append("largeredeemflag:").append(largeredeemflag);
        returnStringBuffer.append("oldappno:").append(oldappno);
        returnStringBuffer.append("paytype:").append(paytype);
        returnStringBuffer.append("payst:").append(payst);
        returnStringBuffer.append("capitalno:").append(capitalno);
        returnStringBuffer.append("applyst:").append(applyst);
        returnStringBuffer.append("transst:").append(transst);
        returnStringBuffer.append("]");
        return returnStringBuffer.toString();
    }

	public String getBroker() {
		return broker;
	}

	public void setBroker(String broker) {
		this.broker = broker;
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

	public String getBrokertel() {
		return brokertel;
	}

	public void setBrokertel(String brokertel) {
		this.brokertel = brokertel;
	}

	public String getBrokerfax() {
		return brokerfax;
	}

	public void setBrokerfax(String brokerfax) {
		this.brokerfax = brokerfax;
	}

	public String getBookingdt() {
		return bookingdt;
	}

	public void setBookingdt(String bookingdt) {
		this.bookingdt = bookingdt;
	}

	public String getValidays() {
		return validays;
	}

	public void setValidays(String validays) {
		this.validays = validays;
	}

	public String getSpecredeemflag() {
		return specredeemflag;
	}

	public void setSpecredeemflag(String specredeemflag) {
		this.specredeemflag = specredeemflag;
	}

	public String getSpecreadeemdt() {
		return specreadeemdt;
	}

	public void setSpecreadeemdt(String specreadeemdt) {
		this.specreadeemdt = specreadeemdt;
	}

	public String getDelaydt() {
		return delaydt;
	}

	public void setDelaydt(String delaydt) {
		this.delaydt = delaydt;
	}

	public String getCheckflag() {
		return checkflag;
	}

	public void setCheckflag(String checkflag) {
		this.checkflag = checkflag;
	}

	public String getNetpoint() {
		return netpoint;
	}

	public void setNetpoint(String netpoint) {
		this.netpoint = netpoint;
	}
}
