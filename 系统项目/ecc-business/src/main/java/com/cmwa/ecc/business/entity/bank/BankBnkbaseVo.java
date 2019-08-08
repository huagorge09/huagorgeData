package com.cmwa.ecc.business.entity.bank;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 银行基本信息DTO
 * @author ex-liuy
 *
 */
@Alias("bankBnkbaseVo")
public class BankBnkbaseVo implements Serializable {

    /**
	 * 
	 */
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
    private String disOrder; //展示顺序
    private String disFlg; //展示标志
    private String cTime; //创建时间
    private String cMan; //创建人
    private String eTime; //修改时间
    private String eMan; //修改人
    private String status; //状态

    public String getDisOrder() {
		return disOrder;
	}

	public void setDisOrder(String disOrder) {
		this.disOrder = disOrder;
	}

	private String resultCode; //返回代码
    private String resultMessage; //返回信息
    private String bnkNmAbbr;//银行简称

    
    public String getcTime() {
		return cTime;
	}

	public void setcTime(String cTime) {
		this.cTime = cTime;
	}

	public String getcMan() {
		return cMan;
	}

	public void setcMan(String cMan) {
		this.cMan = cMan;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String geteMan() {
		return eMan;
	}

	public void seteMan(String eMan) {
		this.eMan = eMan;
	}

	public String getBnkNmAbbr() {
		return bnkNmAbbr;
	}

	public void setBnkNmAbbr(String bnkNmAbbr) {
		this.bnkNmAbbr = bnkNmAbbr;
	}

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

	@Override
	public String toString() {
		return "BankBnkbaseDto [bnkNo=" + bnkNo + ", bnkNm=" + bnkNm
				+ ", webSite=" + webSite + ", ebnkUrl=" + ebnkUrl
				+ ", balanceUrl=" + balanceUrl + ", telBnk=" + telBnk
				+ ", linkMan=" + linkMan + ", linkManTel=" + linkManTel
				+ ", linkManFax=" + linkManFax + ", linkManEmail="
				+ linkManEmail + ", disOrder=" + disOrder + ", disFlg="
				+ disFlg + ", cTime=" + cTime + ", cMan=" + cMan + ", eTime="
				+ eTime + ", eMan=" + eMan + ", status=" + status
				+ ", resultCode=" + resultCode + ", resultMessage="
				+ resultMessage + ", bnkNmAbbr=" + bnkNmAbbr + "]";
	}
}
