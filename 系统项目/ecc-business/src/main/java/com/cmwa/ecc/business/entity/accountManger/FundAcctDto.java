package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

/**
 * 基金账户实体
 * @author ex-chenbq
 *
 */
@Alias("fundAcctDto")
public class FundAcctDto implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -643758459660033065L;

	/*基金账号*/
    private String fundacco; 
    
    /*注册结构编号*/
    private String tano;
    
    /*注册机构名称*/
    private String tanm;
    
    /*账户类型*/
    private String fundAcctFlag;
    
    /*状态*/
    private String status;

	public String getFundacco()
	{
		return fundacco;
	}

	public void setFundacco(String fundacco)
	{
		this.fundacco = fundacco;
	}

	public String getTano()
	{
		return tano;
	}

	public void setTano(String tano)
	{
		this.tano = tano;
	}

	public String getTanm()
	{
		return tanm;
	}

	public void setTanm(String tanm)
	{
		this.tanm = tanm;
	}

	public String getFundAcctFlag()
	{
		return fundAcctFlag;
	}

	public void setFundAcctFlag(String fundAcctFlag)
	{
		this.fundAcctFlag = fundAcctFlag;
	}

	public String getStatus()
	{
		return status;
	}

	public void setStatus(String status)
	{
		this.status = status;
	}

	public String toString()
	{
		return "FundAcctDto [fundacco=" + fundacco + ", tano=" + tano
				+ ", tanm=" + tanm + ", fundAcctFlag=" + fundAcctFlag
				+ ", status=" + status + "]";
	} 
}
