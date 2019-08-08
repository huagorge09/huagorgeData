package com.cmwa.ecc.business.entity.bank;

import java.io.Serializable;
import java.sql.Date;

import org.apache.ibatis.type.Alias;

@Alias("dsBankBnkbaseVo")
public class DsBankBnkbaseVo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String bnkNo; //银行代码
    private String bnkNm; //银行名称
    private String webSite; //网址
    private String ebnkUrl; //网上银行地址
    private String balanceUrl; //余额查询地址
    private String telBnk; //电话银行
    private String linkMan; //联系人
    private String linkManTel; //联系人电话
    private String linkManFax; //联系人传真
    private String linkManEmail; //联系人电子邮件
    private Integer disOrder; //展示顺序
    private String disFlg; //展示标志
    private Date cTime; //创建时间
    public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public String getcMan() {
		return cMan;
	}

	public void setcMan(String cMan) {
		this.cMan = cMan;
	}

	public Date geteTime() {
		return eTime;
	}

	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}

	public String geteMan() {
		return eMan;
	}

	public void seteMan(String eMan) {
		this.eMan = eMan;
	}

	private String cMan; //创建人
    private Date eTime; //修改时间
    private String eMan; //修改人
    private String status; //状态
    /*
     * 以下是对公账户表信息
     */
    private String accoId;//账户编号
    private String accoName;//账户名称
    private String accoType;//账户类型
    private String bankAcco;//银行账户
    private String nameInBank;//银行账户名
    private String regionCode;//区域代码
    private String netNo;//网点代码
    private String centerNo;//分中心
    private String direct;//划款方向
    private String capitalModel;//资金方式

    private String resultCode; //返回代码
    private String resultMessage; //返回信息

    /**
     * 取得银行代码
     * @return String 返回银行代码
     */
    public String getBnkNo() {
        return bnkNo;
    }

    /**
     * 赋值银行代码
     * @param bnkNo 银行代码
     */
    public void setBnkNo(String bnkNo) {
        if (bnkNo == null) {
            bnkNo = "";
        }
        this.bnkNo = bnkNo;
    }

    /**
     * 取得银行名称
     * @return String 返回银行名称
     */
    public String getBnkNm() {
        return bnkNm;
    }

    /**
     * 赋值银行名称
     * @param bnkNo 银行名称
     */
    public void setBnkNm(String bnkNm) {
        if (bnkNm == null) {
            bnkNm = "";
        }
        this.bnkNm = bnkNm;
    }

    /**
     * 取得网址
     * @return String 返回网址
     */
    public String getWebSite() {
        return webSite;
    }

    /**
     * 赋值网址
     * @param webSite 网址
     */
    public void setWebSite(String webSite) {
        if (webSite == null) {
            webSite = "";
        }
        this.webSite = webSite;
    }

    /**
     * 取得网上银行地址
     * @return String 网上银行地址
     */
    public String getEbnkUrl() {
        return ebnkUrl;
    }

    /**
     * 赋值网上银行地址
     * @param ebnkUrl 网上银行地址
     */
    public void setEbnkUrl(String ebnkUrl) {
        if (ebnkUrl == null) {
            ebnkUrl = "";
        }
        this.ebnkUrl = ebnkUrl;
    }

    /**
     * 取得余额查询地址
     * @return String 返回余额查询地址
     */
    public String getBalanceUrl() {
        return balanceUrl;
    }

    /**
     * 赋值余额查询地址
     * @param balanceUrl 余额查询地址
     */
    public void setBalanceUrl(String balanceUrl) {
        if (balanceUrl == null) {
            balanceUrl = "";
        }
        this.balanceUrl = balanceUrl;
    }

    /**
     * 取得电话银行
     * @return String 返回电话银行
     */
    public String getTelBnk() {
        return telBnk;
    }

    /**
     * 赋值电话银行
     * @param telBnk 电话银行
     */
    public void setTelBnk(String telBnk) {
        if (telBnk == null) {
            telBnk = "";
        }
        this.telBnk = telBnk;
    }

    /**
     * 取得联系人
     * @return String 返回联系人
     */
    public String getLinkMan() {
        return linkMan;
    }

    /**
     * 赋值联系人
     * @param linkMan 联系人
     */
    public void setLinkMan(String linkMan) {
        if (linkMan == null) {
            linkMan = "";
        }
        this.linkMan = linkMan;
    }

    /**
     * 取得联系人电话
     * @return String 返回联系人电话
     */
    public String getLinkManTel() {
        return linkManTel;
    }

    /**
     * 赋值联系人电话
     * @param linkManTel 联系人电话
     */
    public void setLinkManTel(String linkManTel) {
        if (linkManTel == null) {
            linkManTel = "";
        }
        this.linkManTel = linkManTel;
    }

    /**
     * 取得联系人传真
     * @return String 返回联系人传真
     */
    public String getLinkManFax() {
        return linkManFax;
    }

    /**
     * 赋值联系人传真
     * @param linkManFax 联系人传真
     */
    public void setLinkManFax(String linkManFax) {
        if (linkManFax == null) {
            linkManFax = "";
        }
        this.linkManFax = linkManFax;
    }

    /**
     * 取得联系人电子邮件
     * @return String 返回联系人电子邮件
     */
    public String getLinkManEmail() {
        return linkManEmail;
    }

    /**
     * 赋值联系人电子邮件
     * @param linkManEmail 联系人电子邮件
     */
    public void setLinkManEmail(String linkManEmail) {
        if (linkManEmail == null) {
            linkManEmail = "";
        }
        this.linkManEmail = linkManEmail;
    }

    /**
     * 取得展示顺序
     * @return Integer 返回展示顺序
     */
    public Integer getDisOrder() {
        return disOrder;
    }

    /**
     * 赋值展示顺序
     * @param disOrder 展示顺序
     */
    public void setDisOrder(Integer disOrder) {
        if (disOrder == null) {
            disOrder = new Integer(0);
        }
        this.disOrder = disOrder;
    }

    /**
     * 取得展示标志
     * @return String 返回展示标志
     */
    public String getDisFlg() {
        return disFlg;
    }

    /**
     * 赋值展示标志
     * @param disFlg 展示标志
     */
    public void setDisFlg(String disFlg) {
        if (disFlg == null) {
            disFlg = "";
        }
        this.disFlg = disFlg;
    }

    /**
     * 取得创建时间
     * @return String 返回创建时间
     */
    public Date getCTime() {
        return cTime;
    }

    /**
     * 赋值创建时间
     * @param cTime 创建时间
     */
    public void setCTime(Date cTime) {
        this.cTime = cTime;
    }

    /**
     * 取得创建人
     * @return String 返回创建人
     */
    public String getCMan() {
        return cMan;
    }

    /**
     * 赋值创建人
     * @param cMan 创建人
     */
    public void setCMan(String cMan) {
        if (cMan == null) {
            cMan = "";
        }
        this.cMan = cMan;
    }

    /**
     * 取得修改时间
     * @return String 返回修改时间
     */
    public Date getETime() {
        return eTime;
    }

    /**
     * 赋值修改时间
     * @param eTime 修改时间
     */
    public void setETime(Date eTime) {
        this.eTime = eTime;
    }

    /**
     * 取得修改人
     * @return String 返回修改人
     */
    public String getEMan() {
        return eMan;
    }

    /**
     * 赋值修改人
     * @param eMan 修改人
     */
    public void setEMan(String eMan) {
        if (eMan == null) {
            eMan = "";
        }
        this.eMan = eMan;
    }

    /**
     * 取得状态
     * @return String 返回状态
     */
    public String getStatus() {
        return status;
    }

    /**
     * 赋值状态
     * @param status 状态
     */
    public void setStatus(String status) {
        if (status == null) {
            status = "";
        }
        this.status = status;
    }


    /**
     * 取值返回代码
     * @return resultCode
     */
    public String getResultCode() {
        return this.resultCode;
    }

    /**
     * 赋值返回代码
     * @param resultCode 返回代码
     */
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * 取值返回消息
     * @return resultMessage
     */
    public String getResultMessage() {
        return this.resultMessage;
    }

    /**
     * 赋值返回消息
     * @param resultMessage 返回消息
     */
    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DsBankBnkbaseVo)) {
            return false;
        }
        DsBankBnkbaseVo that = (DsBankBnkbaseVo) obj;
        if (!(that.bnkNo == null ? this.bnkNo == null :
              that.bnkNo.equals(this.bnkNo))) {
            return false;
        }
        if (!(that.bnkNm == null ? this.bnkNm == null :
              that.bnkNm.equals(this.bnkNm))) {
            return false;
        }
        if (!(that.webSite == null ? this.webSite == null :
              that.webSite.equals(this.webSite))) {
            return false;
        }
        if (!(that.ebnkUrl == null ? this.ebnkUrl == null :
              that.ebnkUrl.equals(this.ebnkUrl))) {
            return false;
        }
        if (!(that.balanceUrl == null ? this.balanceUrl == null :
              that.balanceUrl.equals(this.balanceUrl))) {
            return false;
        }
        if (!(that.telBnk == null ? this.telBnk == null :
              that.telBnk.equals(this.telBnk))) {
            return false;
        }
        if (!(that.linkMan == null ? this.linkMan == null :
              that.linkMan.equals(this.linkMan))) {
            return false;
        }
        if (!(that.linkManTel == null ? this.linkManTel == null :
              that.linkManTel.equals(this.linkManTel))) {
            return false;
        }
        if (!(that.linkManFax == null ? this.linkManFax == null :
              that.linkManFax.equals(this.linkManFax))) {
            return false;
        }
        if (!(that.linkManEmail == null ? this.linkManEmail == null :
              that.linkManEmail.equals(this.linkManEmail))) {
            return false;
        }
        if (!(that.disOrder == null ? this.disOrder == null :
              that.disOrder.equals(this.disOrder))) {
            return false;
        }
        if (!(that.disFlg == null ? this.disFlg == null :
              that.disFlg.equals(this.disFlg))) {
            return false;
        }
        if (!(that.cTime == null ? this.cTime == null :
              that.cTime.equals(this.cTime))) {
            return false;
        }
        if (!(that.cMan == null ? this.cMan == null :
              that.cMan.equals(this.cMan))) {
            return false;
        }
        if (!(that.eTime == null ? this.eTime == null :
              that.eTime.equals(this.eTime))) {
            return false;
        }
        if (!(that.eMan == null ? this.eMan == null :
              that.eMan.equals(this.eMan))) {
            return false;
        }
        if (!(that.status == null ? this.status == null :
              that.status.equals(this.status))) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int result = 17;
        /*result = 37 * result + this.bnkNo.hashCode();
                 result = 37 * result + this.bnkNm.hashCode();
                 result = 37 * result + this.webSite.hashCode();
                 result = 37 * result + this.ebnkUrl.hashCode();
                 result = 37 * result + this.balanceUrl.hashCode();
                 result = 37 * result + this.telBnk.hashCode();
                 result = 37 * result + this.linkMan.hashCode();
                 result = 37 * result + this.linkManTel.hashCode();
                 result = 37 * result + this.linkManFax.hashCode();
                 result = 37 * result + this.linkManEmail.hashCode();
                 result = 37 * result + this.disOrder.hashCode();
                 result = 37 * result + this.disFlg.hashCode();
                 result = 37 * result + this.cTime.hashCode();
                 result = 37 * result + this.cMan.hashCode();
                 result = 37 * result + this.eTime.hashCode();
                 result = 37 * result + this.eMan.hashCode();
                 result = 37 * result + this.status.hashCode();*/
        return result;
    }

    public String toString() {
        StringBuffer returnStringBuffer = new StringBuffer(544);
        returnStringBuffer.append("[");
        returnStringBuffer.append("bnkNo:").append(bnkNo);
        returnStringBuffer.append("bnkNm:").append(bnkNm);
        returnStringBuffer.append("webSite:").append(webSite);
        returnStringBuffer.append("ebnkUrl:").append(ebnkUrl);
        returnStringBuffer.append("balanceUrl:").append(balanceUrl);
        returnStringBuffer.append("telBnk:").append(telBnk);
        returnStringBuffer.append("linkMan:").append(linkMan);
        returnStringBuffer.append("linkManTel:").append(linkManTel);
        returnStringBuffer.append("linkManFax:").append(linkManFax);
        returnStringBuffer.append("linkManEmail:").append(linkManEmail);
        returnStringBuffer.append("disOrder:").append(disOrder);
        returnStringBuffer.append("disFlg:").append(disFlg);
        returnStringBuffer.append("cTime:").append(cTime);
        returnStringBuffer.append("cMan:").append(cMan);
        returnStringBuffer.append("eTime:").append(eTime);
        returnStringBuffer.append("eMan:").append(eMan);
        returnStringBuffer.append("status:").append(status);
        returnStringBuffer.append("]");
        return returnStringBuffer.toString();
    }

	public String getAccoId() {
		return accoId;
	}

	public void setAccoId(String accoId) {
		this.accoId = accoId;
	}

	public String getAccoName() {
		return accoName;
	}

	public void setAccoName(String accoName) {
		this.accoName = accoName;
	}

	public String getAccoType() {
		return accoType;
	}

	public void setAccoType(String accoType) {
		this.accoType = accoType;
	}

	public String getBankAcco() {
		return bankAcco;
	}

	public void setBankAcco(String bankAcco) {
		this.bankAcco = bankAcco;
	}

	public String getNameInBank() {
		return nameInBank;
	}

	public void setNameInBank(String nameInBank) {
		this.nameInBank = nameInBank;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getNetNo() {
		return netNo;
	}

	public void setNetNo(String netNo) {
		this.netNo = netNo;
	}

	public String getCenterNo() {
		return centerNo;
	}

	public void setCenterNo(String centerNo) {
		this.centerNo = centerNo;
	}

	public String getDirect() {
		return direct;
	}

	public void setDirect(String direct) {
		this.direct = direct;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCapitalModel() {
		return capitalModel;
	}

	public void setCapitalModel(String capitalModel) {
		this.capitalModel = capitalModel;
	}
}
