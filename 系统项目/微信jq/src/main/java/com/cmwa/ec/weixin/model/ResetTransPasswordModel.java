package com.cmwa.ec.weixin.model;

import java.io.Serializable;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 修改交易密码实体model
 * @author ex-liuy
 *
 */
public class ResetTransPasswordModel implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -8296223085298049343L;
	
	@Pattern(regexp="^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,}$",message="交易密码为8位以上数字+字母组成")
	private String tPassWord;//新交易密码

	public String gettPassWord() {
		return tPassWord;
	}

	public void settPassWord(String tPassWord) {
		this.tPassWord = tPassWord;
	}
	
	
}
