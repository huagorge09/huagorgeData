package com.cmwa.ecc.business.entity.accountManger;

import org.apache.ibatis.type.Alias;

@Alias("taInfoDto")
public class TaInfoDto{
	
    private String tano; //登记机构代码
    private String tanm; //登记机构名称

    public TaInfoDto() {
		super();
	}

	public TaInfoDto(String tano, String tanm) {
		super();
		this.tano = tano;
		this.tanm = tanm;
	}

	public void setTano(String tano) {
		this.tano = tano;
	}

	/**
     * 构造函数
     * @param tano String 登记机构代码
     */
    public TaInfoDto(String tano) {
        this.tano = tano;
    }

    /**
     * 设置登记机构名称
     * @param tanm String
     */
    public void setTanm(String tanm) {
        this.tanm = tanm;
    }

    /**
     * 获取登记机构代码
     * @return String
     */
    public String getTano() {
        return tano;
    }

    /**
     * 获取登记机构名称
     * @return String
     */
    public String getTanm() {
        return tanm;
    }

    public String toString() {
        StringBuffer strb = new StringBuffer();
        strb.append("TADto:" + "\n");
        strb.append("tano: " + tano + "\n");
        strb.append("tanm: " + tanm + "\n");
        return strb.toString();
    }
}
