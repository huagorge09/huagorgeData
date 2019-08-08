package com.cmwa.ec.weixin.model;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 修改交易密码实体model
 * @author caolp
 *
 */
public class ModifyTransPasswordModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -384444508671772375L;

	@Length(min=11,max=11,message="手机号码长度必须为11位")
	@NotBlank(message="手机号码为空")
	private String mobile;//手机号
	
	@Length(min=8,message="交易密码长度大于等于8位字符")
	@NotBlank(message="旧交易密码为空")
	private String oldTpassWord;//旧交易密码
	
	@Pattern(regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,}$",message="交易密码为8位以上数字+字母组成")
	private String tPassWord;//新交易密码

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOldTpassWord() {
		return oldTpassWord;
	}

	public void setOldTpassWord(String oldTpassWord) {
		this.oldTpassWord = oldTpassWord;
	}

	public String gettPassWord() {
		return tPassWord;
	}

	public void settPassWord(String tPassWord) {
		this.tPassWord = tPassWord;
	}

}
