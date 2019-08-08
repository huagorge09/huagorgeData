package com.cmwa.ecc.business.entity.multiple;

import java.io.Serializable;
import java.math.BigDecimal;

import org.apache.ibatis.type.Alias;

@Alias("fundNavVo")
public class FundNavVo implements Serializable{
	private String fundid;
    private String fundnm;
    private BigDecimal nav;
    private BigDecimal facevalue;
    private String navdt;
    public String getFundid() {
        return fundid;
    }

    public void setFundid(String fundid) {
        this.fundid = fundid;
    }

    public String getFundnm() {
        return fundnm;
    }

    public void setFundnm(String fundnm) {
        this.fundnm = fundnm;
    }

    public BigDecimal getNav() {
        return nav;
    }

    public void setNav(BigDecimal nav) {
        this.nav = nav;
    }

    public BigDecimal getFacevalue() {
        return facevalue;
    }

    public void setFacevalue(BigDecimal facevalue) {
        this.facevalue = facevalue;
    }

    public String getNavdt() {
        return navdt;
    }

    public void setNavdt(String navdt) {
        this.navdt = navdt;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof FundNavVo))
            return false;
        FundNavVo that = (FundNavVo) obj;
        if (!(that.fundid == null ? this.fundid == null :
              that.fundid.equals(this.fundid)))
            return false;
        if (!(that.fundnm == null ? this.fundnm == null :
              that.fundnm.equals(this.fundnm)))
            return false;
        if (!(that.nav == null ? this.nav == null : that.nav.equals(this.nav)))
            return false;
        if (!(that.facevalue == null ? this.facevalue == null :
              that.facevalue.equals(this.facevalue)))
            return false;
        if (!(that.navdt == null ? this.navdt == null :
              that.navdt.equals(this.navdt)))
            return false;
        return true;
    }

    public int hashCode() {
        int result = 17;
        result = 37 * result + this.fundid.hashCode();
        result = 37 * result + this.fundnm.hashCode();
        result = 37 * result + this.nav.hashCode();
        result = 37 * result + this.facevalue.hashCode();
        result = 37 * result + this.navdt.hashCode();
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(160);
        returnStringBuffer.append("[");
        returnStringBuffer.append("fundid:").append(fundid);
        returnStringBuffer.append("fundnm:").append(fundnm);
        returnStringBuffer.append("nav:").append(nav);
        returnStringBuffer.append("facevalue:").append(facevalue);
        returnStringBuffer.append("navdt:").append(navdt);
        returnStringBuffer.append("]");
        return returnStringBuffer.toString();
    }
}

