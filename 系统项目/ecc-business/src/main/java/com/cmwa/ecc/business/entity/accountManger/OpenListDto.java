package com.cmwa.ecc.business.entity.accountManger;

import java.io.Serializable;

import org.apache.ibatis.type.Alias;

@Alias("openListDto")
public class OpenListDto implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
    private String name;

    /**
     * 构造函数
     * @param brokerno String 经济人代码
     */
    public OpenListDto() {
    }

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "OpenListDto [code=" + code + ", name=" + name + "]";
	}
}
