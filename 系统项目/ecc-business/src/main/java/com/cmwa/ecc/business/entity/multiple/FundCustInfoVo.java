package com.cmwa.ecc.business.entity.multiple;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("fundCustInfoVo")
public class FundCustInfoVo implements Serializable {
    private String fundacct;
    private String invtp;
    private String invtpName;
    private String invnm;
	private String idtp;
    private String idtpName;
    private String idno;
    private String fdacst;
    private String fdacstName;
    private String opendt;
    private String custtp;
    private String custtpName;
	private String tradeacco;
	private String bankno;
	private String bankname;
	private String bankacco;
	private String tradeaccost;
    private String tradeaccostName;
	private String melonmd;
    private String melonmdName;
    private String netpoint;
    public String getNetpoint() {
		return netpoint;
	}

	public void setNetpoint(String netpoint) {
		this.netpoint = netpoint;
	}

	public String getTradeaccostName() {
		return tradeaccostName;
	}

	public void setTradeaccostName(String tradeaccostName) {
		this.tradeaccostName = tradeaccostName;
	}
    public String getFdacstName() {
		return fdacstName;
	}

	public void setFdacstName(String fdacstName) {
		this.fdacstName = fdacstName;
	}
    public String getCusttpName() {
		return custtpName;
	}
	public void setCusttpName(String custtpName) {
		this.custtpName = custtpName;
	}
    public String getBankname() {
		return bankname;
	}
	public void setBankname(String bankname) {
		this.bankname = bankname;
	}
    public String getInvtpName() {
		return invtpName;
	}
	public void setInvtpName(String invtpName) {
		this.invtpName = invtpName;
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

    public String getFdacst() {
        return fdacst;
    }

    public void setFdacst(String fdacst) {
        this.fdacst = fdacst;
    }

    public String getOpendt() {
        return opendt;
    }

    public void setOpendt(String opendt) {
        this.opendt = opendt;
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

    public String getTradeaccost() {
        return tradeaccost;
    }

    public void setTradeaccost(String tradeaccost) {
        this.tradeaccost = tradeaccost;
    }

    public String getMelonmd() {
        return melonmd;
    }

    public void setMelonmd(String melonmd) {
        this.melonmd = melonmd;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof FundCustInfoVo))
            return false;
        FundCustInfoVo that = (FundCustInfoVo) obj;
        if (!(that.fundacct == null ? this.fundacct == null :
              that.fundacct.equals(this.fundacct)))
            return false;
        if (!(that.invtp == null ? this.invtp == null :
              that.invtp.equals(this.invtp)))
            return false;
        if (!(that.invnm == null ? this.invnm == null :
              that.invnm.equals(this.invnm)))
            return false;
        if (!(that.idtp == null ? this.idtp == null :
              that.idtp.equals(this.idtp)))
            return false;
        if (!(that.idno == null ? this.idno == null :
              that.idno.equals(this.idno)))
            return false;
        if (!(that.fdacst == null ? this.fdacst == null :
              that.fdacst.equals(this.fdacst)))
            return false;
        if (!(that.opendt == null ? this.opendt == null :
              that.opendt.equals(this.opendt)))
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
        if (!(that.tradeaccost == null ? this.tradeaccost == null :
              that.tradeaccost.equals(this.tradeaccost)))
            return false;
        if (!(that.melonmd == null ? this.melonmd == null :
              that.melonmd.equals(this.melonmd)))
            return false;
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.fundacct.hashCode();
        result = 37 * result + this.invtp.hashCode();
        result = 37 * result + this.invnm.hashCode();
        result = 37 * result + this.idtp.hashCode();
        result = 37 * result + this.idno.hashCode();
        result = 37 * result + this.fdacst.hashCode();
        result = 37 * result + this.opendt.hashCode();
        result = 37 * result + this.custtp.hashCode();
        result = 37 * result + this.tradeacco.hashCode();
        result = 37 * result + this.bankno.hashCode();
        result = 37 * result + this.bankacco.hashCode();
        result = 37 * result + this.tradeaccost.hashCode();
        result = 37 * result + this.melonmd.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(416);
        returnStringBuffer.append("[");
        returnStringBuffer.append("fundacct:").append(fundacct);
        returnStringBuffer.append("invtp:").append(invtp);
        returnStringBuffer.append("invnm:").append(invnm);
        returnStringBuffer.append("idtp:").append(idtp);
        returnStringBuffer.append("idno:").append(idno);
        returnStringBuffer.append("fdacst:").append(fdacst);
        returnStringBuffer.append("opendt:").append(opendt);
        returnStringBuffer.append("custtp:").append(custtp);
        returnStringBuffer.append("tradeacco:").append(tradeacco);
        returnStringBuffer.append("bankno:").append(bankno);
        returnStringBuffer.append("bankacco:").append(bankacco);
        returnStringBuffer.append("tradeaccost:").append(tradeaccost);
        returnStringBuffer.append("melonmd:").append(melonmd);
        returnStringBuffer.append("]");
        return returnStringBuffer.toString();
    }
}