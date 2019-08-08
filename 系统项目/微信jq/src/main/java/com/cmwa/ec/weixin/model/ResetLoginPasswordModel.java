package com.cmwa.ec.weixin.model;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 重置登录密码实体model
 * @author caolp
 *
 */
public class ResetLoginPasswordModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -384444508671772375L;

	@Length(min=11,max=11,message="手机号码长度必须为11位")
	@NotBlank(message="手机号码为非空")
	private String mobile;//手机号
	
	@Pattern(regexp="[0-9]{6,16}",message="密码长度为6至16位长度数字")
	private String passWord;//密码

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}
	
	
}
