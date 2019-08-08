/**
* @Title: AcctAppHisVo.java  
* @author ex-wuh2  
* @date 2018年6月7日  
* @version V1.0  
 */
package com.cmwa.ecc.business.entity.query;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * @author ex-wuh2
 *	历史账户流水Vo
 */
@Alias("acctAppHisVo")
public class AcctAppHisVo implements Serializable {

	private static final long serialVersionUID = 1L;
	private String serialno;
    private String appno;
    private String fundacct;
    private String tradeacco;
    private String apkind;
    private String apkindName;
    private String custtp;
    private String invtp;
    private String invtpName;
    private String invnm;
    private String accountabbr;
    private String idtp;
    private String idtpName;
    private String idno;
    private String telno;
    private String mobileno;
    private String addr;
    private String postcode;
    private String email;
    private String faxno;
    private String delivertype;
	private String melonmd;
    private String melonmdName;
    private String apdt;
    
  //2010-08-09 WANGDW ADD
    private String regioncode;//区域代码
    private String regioncodeName;//区域代码
	private String netPoint;//网点代码
	private String instrepname;//法人名称
    private String instrepidtp;//法人证件类型
    private String instrepidno;//法人证件号码
    private String brokername;//经办人名称
    private String brokeridtp;//经办人证件类型
    private String brokeridno;//经办人证件号码
    private String hometel;//家庭电话
    private String officetel;//办公室电话
    private String operatorcode;//操作员代码
    private String custsimpnm;//客户一级简称
    private String instrepcode;//客户二级简称
    private String invname;//客户二级简称
    public String getInvname() {
		return invname;
	}

	public void setInvname(String invname) {
		this.invname = invname;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	private String checker;//复核员
    private String checkflag;//复核标志
    private String checkflagName;//复核标志
    private String brokertel;//经办人办公电话
    private String brokerfax;//经办人传真号码
    public String getRegioncodeName() {
  		return regioncodeName;
  	}

  	public void setRegioncodeName(String regioncodeName) {
  		this.regioncodeName = regioncodeName;
  	}
    public String getCheckflagName() {
		return checkflagName;
	}

	public void setCheckflagName(String checkflagName) {
		this.checkflagName = checkflagName;
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

    public String getApkind() {
        return apkind;
    }

    public void setApkind(String apkind) {
        this.apkind = apkind;
    }

    public String getCusttp() {
        return custtp;
    }

    public void setCusttp(String custtp) {
        this.custtp = custtp;
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

    public String getAccountabbr() {
        return accountabbr;
    }

    public void setAccountabbr(String accountabbr) {
        this.accountabbr = accountabbr;
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

    public String getTelno() {
        return telno;
    }

    public void setTelno(String telno) {
        this.telno = telno;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFaxno() {
        return faxno;
    }

    public void setFaxno(String faxno) {
        this.faxno = faxno;
    }

    public String getDelivertype() {
        return delivertype;
    }

    public void setDelivertype(String delivertype) {
        this.delivertype = delivertype;
    }

    public String getMelonmd() {
        return melonmd;
    }

    public void setMelonmd(String melonmd) {
        this.melonmd = melonmd;
    }

    public String getApdt() {
        return apdt;
    }

    public void setApdt(String apdt) {
        this.apdt = apdt;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof AcctAppHisVo))
            return false;
        AcctAppHisVo that = (AcctAppHisVo) obj;
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
        if (!(that.apkind == null ? this.apkind == null :
              that.apkind.equals(this.apkind)))
            return false;
        if (!(that.custtp == null ? this.custtp == null :
              that.custtp.equals(this.custtp)))
            return false;
        if (!(that.invtp == null ? this.invtp == null :
              that.invtp.equals(this.invtp)))
            return false;
        if (!(that.invnm == null ? this.invnm == null :
              that.invnm.equals(this.invnm)))
            return false;
        if (!(that.accountabbr == null ? this.accountabbr == null :
              that.accountabbr.equals(this.accountabbr)))
            return false;
        if (!(that.idtp == null ? this.idtp == null :
              that.idtp.equals(this.idtp)))
            return false;
        if (!(that.idno == null ? this.idno == null :
              that.idno.equals(this.idno)))
            return false;
        if (!(that.telno == null ? this.telno == null :
              that.telno.equals(this.telno)))
            return false;
        if (!(that.mobileno == null ? this.mobileno == null :
              that.mobileno.equals(this.mobileno)))
            return false;
        if (!(that.addr == null ? this.addr == null :
              that.addr.equals(this.addr)))
            return false;
        if (!(that.postcode == null ? this.postcode == null :
              that.postcode.equals(this.postcode)))
            return false;
        if (!(that.email == null ? this.email == null :
              that.email.equals(this.email)))
            return false;
        if (!(that.faxno == null ? this.faxno == null :
              that.faxno.equals(this.faxno)))
            return false;
        if (!(that.delivertype == null ? this.delivertype == null :
              that.delivertype.equals(this.delivertype)))
            return false;
        if (!(that.melonmd == null ? this.melonmd == null :
              that.melonmd.equals(this.melonmd)))
            return false;
        if (!(that.apdt == null ? this.apdt == null :
              that.apdt.equals(this.apdt)))
            return false;
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.serialno.hashCode();
        result = 37 * result + this.appno.hashCode();
        result = 37 * result + this.fundacct.hashCode();
        result = 37 * result + this.tradeacco.hashCode();
        result = 37 * result + this.apkind.hashCode();
        result = 37 * result + this.custtp.hashCode();
        result = 37 * result + this.invtp.hashCode();
        result = 37 * result + this.invnm.hashCode();
        result = 37 * result + this.accountabbr.hashCode();
        result = 37 * result + this.idtp.hashCode();
        result = 37 * result + this.idno.hashCode();
        result = 37 * result + this.telno.hashCode();
        result = 37 * result + this.mobileno.hashCode();
        result = 37 * result + this.addr.hashCode();
        result = 37 * result + this.postcode.hashCode();
        result = 37 * result + this.email.hashCode();
        result = 37 * result + this.faxno.hashCode();
        result = 37 * result + this.delivertype.hashCode();
        result = 37 * result + this.melonmd.hashCode();
        result = 37 * result + this.apdt.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(640);
        returnStringBuffer.append("[");
        returnStringBuffer.append("serialno:").append(serialno);
        returnStringBuffer.append("appno:").append(appno);
        returnStringBuffer.append("fundacct:").append(fundacct);
        returnStringBuffer.append("tradeacco:").append(tradeacco);
        returnStringBuffer.append("apkind:").append(apkind);
        returnStringBuffer.append("custtp:").append(custtp);
        returnStringBuffer.append("invtp:").append(invtp);
        returnStringBuffer.append("invnm:").append(invnm);
        returnStringBuffer.append("accountabbr:").append(accountabbr);
        returnStringBuffer.append("idtp:").append(idtp);
        returnStringBuffer.append("idno:").append(idno);
        returnStringBuffer.append("telno:").append(telno);
        returnStringBuffer.append("mobileno:").append(mobileno);
        returnStringBuffer.append("addr:").append(addr);
        returnStringBuffer.append("postcode:").append(postcode);
        returnStringBuffer.append("email:").append(email);
        returnStringBuffer.append("faxno:").append(faxno);
        returnStringBuffer.append("delivertype:").append(delivertype);
        returnStringBuffer.append("melonmd:").append(melonmd);
        returnStringBuffer.append("apdt:").append(apdt);
        returnStringBuffer.append("]");
        return returnStringBuffer.toString();
    }

	public String getRegioncode() {
		return regioncode;
	}

	public void setRegioncode(String regioncode) {
		this.regioncode = regioncode;
	}

	public String getNetPoint() {
		return netPoint;
	}

	public void setNetPoint(String netPoint) {
		this.netPoint = netPoint;
	}

	public String getInstrepname() {
		return instrepname;
	}

	public void setInstrepname(String instrepname) {
		this.instrepname = instrepname;
	}

	public String getInstrepidtp() {
		return instrepidtp;
	}

	public void setInstrepidtp(String instrepidtp) {
		this.instrepidtp = instrepidtp;
	}

	public String getInstrepidno() {
		return instrepidno;
	}

	public void setInstrepidno(String instrepidno) {
		this.instrepidno = instrepidno;
	}

	public String getBrokername() {
		return brokername;
	}

	public void setBrokername(String brokername) {
		this.brokername = brokername;
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

	public String getHometel() {
		return hometel;
	}

	public void setHometel(String hometel) {
		this.hometel = hometel;
	}

	public String getOfficetel() {
		return officetel;
	}

	public void setOfficetel(String officetel) {
		this.officetel = officetel;
	}

	public String getOperatorcode() {
		return operatorcode;
	}

	public void setOperatorcode(String operatorcode) {
		this.operatorcode = operatorcode;
	}

	public String getCustsimpnm() {
		return custsimpnm;
	}

	public void setCustsimpnm(String custsimpnm) {
		this.custsimpnm = custsimpnm;
	}

	public String getInstrepcode() {
		return instrepcode;
	}

	public void setInstrepcode(String instrepcode) {
		this.instrepcode = instrepcode;
	}

	public String getChecker() {
		return checker;
	}

	public void setChecker(String checker) {
		this.checker = checker;
	}

	public String getCheckflag() {
		return checkflag;
	}

	public void setCheckflag(String checkflag) {
		this.checkflag = checkflag;
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
}
