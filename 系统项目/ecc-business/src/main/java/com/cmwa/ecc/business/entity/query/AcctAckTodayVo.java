/**
* @Title: AcctAckTodayVo.java  
* @author ex-wuh2  
* @date 2018年6月7日  
* @version V1.0  
 */
package com.cmwa.ecc.business.entity.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author ex-wuh2
 *	TA账户
 */
@Alias("acctAckTodayVo")
public class AcctAckTodayVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String ackdt;
    private String ackno;
    private String serialno;
    private String appno;
    private String fundacct;
    private String tradeacco;
    private String invnm;
    private String invtp;
    private String invtpName;
    private String idtp;
    private String idtpName;
    private String idno;
    private String apkind;
    private String apkindName;
    private String melonmd;
	private String melonmdName;
    private String multiacctflag;
    private String retcode;
    private String retmsg;
    private String netPoint;
    public String getNetPoint() {
		return netPoint;
	}

	public void setNetPoint(String netPoint) {
		this.netPoint = netPoint;
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

    public String getTradeacco() {
        return tradeacco;
    }

    public void setTradeacco(String tradeacco) {
        this.tradeacco = tradeacco;
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

    public String getApkind() {
        return apkind;
    }

    public void setApkind(String apkind) {
        this.apkind = apkind;
    }

    public String getMelonmd() {
        return melonmd;
    }

    public void setMelonmd(String melonmd) {
        this.melonmd = melonmd;
    }

    public String getMultiacctflag() {
        return multiacctflag;
    }

    public void setMultiacctflag(String multiacctflag) {
        this.multiacctflag = multiacctflag;
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
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AcctAckTodayVo))
            return false;
        AcctAckTodayVo that = (AcctAckTodayVo) obj;
        if (!(that.ackdt == null ? this.ackdt == null :
              that.ackdt.equals(this.ackdt)))
            return false;
        if (!(that.ackno == null ? this.ackno == null :
              that.ackno.equals(this.ackno)))
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
        if (!(that.tradeacco == null ? this.tradeacco == null :
              that.tradeacco.equals(this.tradeacco)))
            return false;
        if (!(that.invnm == null ? this.invnm == null :
              that.invnm.equals(this.invnm)))
            return false;
        if (!(that.invtp == null ? this.invtp == null :
              that.invtp.equals(this.invtp)))
            return false;
        if (!(that.idtp == null ? this.idtp == null :
              that.idtp.equals(this.idtp)))
            return false;
        if (!(that.idno == null ? this.idno == null :
              that.idno.equals(this.idno)))
            return false;
        if (!(that.apkind == null ? this.apkind == null :
              that.apkind.equals(this.apkind)))
            return false;
        if (!(that.melonmd == null ? this.melonmd == null :
              that.melonmd.equals(this.melonmd)))
            return false;
        if (!(that.multiacctflag == null ? this.multiacctflag == null :
              that.multiacctflag.equals(this.multiacctflag)))
            return false;
        if (!(that.retcode == null ? this.retcode == null :
              that.retcode.equals(this.retcode)))
            return false;
        if (!(that.retmsg == null ? this.retmsg == null :
              that.retmsg.equals(this.retmsg)))
            return false;
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.ackdt.hashCode();
        result = 37 * result + this.ackno.hashCode();
        result = 37 * result + this.serialno.hashCode();
        result = 37 * result + this.appno.hashCode();
        result = 37 * result + this.fundacct.hashCode();
        result = 37 * result + this.tradeacco.hashCode();
        result = 37 * result + this.invnm.hashCode();
        result = 37 * result + this.invtp.hashCode();
        result = 37 * result + this.idtp.hashCode();
        result = 37 * result + this.idno.hashCode();
        result = 37 * result + this.apkind.hashCode();
        result = 37 * result + this.melonmd.hashCode();
        result = 37 * result + this.multiacctflag.hashCode();
        result = 37 * result + this.retcode.hashCode();
        result = 37 * result + this.retmsg.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(480);
        returnStringBuffer.append("[");
        returnStringBuffer.append("ackdt:").append(ackdt);
        returnStringBuffer.append("ackno:").append(ackno);
        returnStringBuffer.append("serialno:").append(serialno);
        returnStringBuffer.append("appno:").append(appno);
        returnStringBuffer.append("fundacct:").append(fundacct);
        returnStringBuffer.append("tradeacco:").append(tradeacco);
        returnStringBuffer.append("invnm:").append(invnm);
        returnStringBuffer.append("invtp:").append(invtp);
        returnStringBuffer.append("idtp:").append(idtp);
        returnStringBuffer.append("idno:").append(idno);
        returnStringBuffer.append("apkind:").append(apkind);
        returnStringBuffer.append("melonmd:").append(melonmd);
        returnStringBuffer.append("multiacctflag:").append(multiacctflag);
        returnStringBuffer.append("retcode:").append(retcode);
        returnStringBuffer.append("retmsg:").append(retmsg);
        returnStringBuffer.append("]");
        return returnStringBuffer.toString();
    }
}
