package com.cmwa.ecc.business.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

/**
 * @author ex-wuh2
 *	当天交易流水
 */
@Alias("tradeAppTodayVo")
public class TradeAppTodayVo implements Serializable {
    private String serialno;
    private String fundacct;
    private String tradeacco;
    private String invtp;
    private String invtpName;
    private String invnm;
    private String ofundname;
	private String fundname;
	private String apkind;
    private String apkindName;
    private String fundid;
    private BigDecimal subamt;
    private String currencytype;
    private BigDecimal subquty;
    private BigDecimal commro;
    private String oseatno;
    private String ofundid;
    private String melonmd;
    private String melonmdName;
    private BigDecimal dividendrate;
    private String largeredeemflag;
    private String oldappno;
    private String workdate;
    private String apdt;
    private String aptm;

	private String bankno;
    private String bankacco;
    private String netpoint;
    private String applyst;
    private String regioncode;
    private String broker;//经办人名称
    private String brokeridtp;//经办人证件类型
    private String brokeridno;//经办人证件号码
    private String brokertel;//经办人办公电话
	private String brokerfax;//经办人传真
    private String checkflag;//资金复核标志
    private String frozencause;//冻结原因
    private String ofundacct;//对方基金账号
    private String otradeacco;//对方交易账号
    private String bookingdt;//预约日期
    private String validays;//有效天数
    private String specredeemflag;//指定赎回标志
    private String specreadeemdt;//指定赎回日期
    private String delaydt;//顺延日期
    private String appno;//申请编号
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
    public String getRegioncode() {
		return regioncode;
	}

	public void setRegioncode(String regioncode) {
		this.regioncode = regioncode;
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

    public BigDecimal getSubquty() {
        return subquty;
    }

    public void setSubquty(BigDecimal subquty) {
        this.subquty = subquty;
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

    public String getWorkdate() {
        return workdate;
    }

    public void setWorkdate(String workdate) {
        this.workdate = workdate;
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

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof TradeAppTodayVo))
            return false;
        TradeAppTodayVo that = (TradeAppTodayVo) obj;
        if (!(that.serialno == null ? this.serialno == null :
              that.serialno.equals(this.serialno)))
            return false;
        if (!(that.fundacct == null ? this.fundacct == null :
              that.fundacct.equals(this.fundacct)))
            return false;
        if (!(that.tradeacco == null ? this.tradeacco == null :
              that.tradeacco.equals(this.tradeacco)))
            return false;
        if (!(that.invtp == null ? this.invtp == null :
              that.invtp.equals(this.invtp)))
            return false;
        if (!(that.invnm == null ? this.invnm == null :
              that.invnm.equals(this.invnm)))
            return false;
        if (!(that.apkind == null ? this.apkind == null :
              that.apkind.equals(this.apkind)))
            return false;
        if (!(that.fundid == null ? this.fundid == null :
              that.fundid.equals(this.fundid)))
            return false;
        if (!(that.subamt == null ? this.subamt == null :
              that.subamt.equals(this.subamt)))
            return false;
        if (!(that.currencytype == null ? this.currencytype == null :
              that.currencytype.equals(this.currencytype)))
            return false;
        if (!(that.subquty == null ? this.subquty == null :
              that.subquty.equals(this.subquty)))
            return false;
        if (!(that.commro == null ? this.commro == null :
              that.commro.equals(this.commro)))
            return false;
        if (!(that.oseatno == null ? this.oseatno == null :
              that.oseatno.equals(this.oseatno)))
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
        if (!(that.largeredeemflag == null ? this.largeredeemflag == null :
              that.largeredeemflag.equals(this.largeredeemflag)))
            return false;
        if (!(that.oldappno == null ? this.oldappno == null :
              that.oldappno.equals(this.oldappno)))
            return false;
        if (!(that.workdate == null ? this.workdate == null :
              that.workdate.equals(this.workdate)))
            return false;
        if (!(that.apdt == null ? this.apdt == null :
              that.apdt.equals(this.apdt)))
            return false;
        if (!(that.aptm == null ? this.aptm == null :
              that.aptm.equals(this.aptm)))
            return false;
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.serialno.hashCode();
        result = 37 * result + this.fundacct.hashCode();
        result = 37 * result + this.tradeacco.hashCode();
        result = 37 * result + this.invtp.hashCode();
        result = 37 * result + this.invnm.hashCode();
        result = 37 * result + this.apkind.hashCode();
        result = 37 * result + this.fundid.hashCode();
        result = 37 * result + this.subamt.hashCode();
        result = 37 * result + this.currencytype.hashCode();
        result = 37 * result + this.subquty.hashCode();
        result = 37 * result + this.commro.hashCode();
        result = 37 * result + this.oseatno.hashCode();
        result = 37 * result + this.ofundid.hashCode();
        result = 37 * result + this.melonmd.hashCode();
        result = 37 * result + this.dividendrate.hashCode();
        result = 37 * result + this.largeredeemflag.hashCode();
        result = 37 * result + this.oldappno.hashCode();
        result = 37 * result + this.workdate.hashCode();
        result = 37 * result + this.apdt.hashCode();
        result = 37 * result + this.aptm.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(640);
        returnStringBuffer.append("[");
        returnStringBuffer.append("serialno:").append(serialno);
        returnStringBuffer.append("fundacct:").append(fundacct);
        returnStringBuffer.append("tradeacco:").append(tradeacco);
        returnStringBuffer.append("invtp:").append(invtp);
        returnStringBuffer.append("invnm:").append(invnm);
        returnStringBuffer.append("apkind:").append(apkind);
        returnStringBuffer.append("fundid:").append(fundid);
        returnStringBuffer.append("subamt:").append(subamt);
        returnStringBuffer.append("currencytype:").append(currencytype);
        returnStringBuffer.append("subquty:").append(subquty);
        returnStringBuffer.append("commro:").append(commro);
        returnStringBuffer.append("oseatno:").append(oseatno);
        returnStringBuffer.append("ofundid:").append(ofundid);
        returnStringBuffer.append("melonmd:").append(melonmd);
        returnStringBuffer.append("dividendrate:").append(dividendrate);
        returnStringBuffer.append("largeredeemflag:").append(largeredeemflag);
        returnStringBuffer.append("oldappno:").append(oldappno);
        returnStringBuffer.append("workdate:").append(workdate);
        returnStringBuffer.append("apdt:").append(apdt);
        returnStringBuffer.append("aptm:").append(aptm);
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

	public String getFrozencause() {
		return frozencause;
	}

	public void setFrozencause(String frozencause) {
		this.frozencause = frozencause;
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

	public String getAppno() {
		return appno;
	}

	public void setAppno(String appno) {
		this.appno = appno;
	}

	public String getBankno() {
		return bankno;
	}

	public void setBankno(String bankno) {
		this.bankno = bankno;
	}

	public String getNetpoint() {
		return netpoint;
	}

	public void setNetpoint(String netpoint) {
		this.netpoint = netpoint;
	}

	public String getApplyst() {
		return applyst;
	}

	public void setApplyst(String applyst) {
		this.applyst = applyst;
	}

	public String getBankacco() {
		return bankacco;
	}

	public void setBankacco(String bankacco) {
		this.bankacco = bankacco;
	}
}
