package com.cmwa.ec.weixin.dto;

import java.io.Serializable;


/**
 * 
 * 短连接dto类
 * @author ex-hezk
 *
 */
public class ShortUrlDto extends ActivityBaseDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 键
	 */
	private String keys;
	
	/**
	 * 值
	 */
	private String urlValues;

	/**
	 * @return
	 */
	public String getKeys() {
		return keys;
	}

	/**
	 * @param keys
	 */
	public void setKeys(String keys) {
		this.keys = keys;
	}

	/**
	 * @return
	 */
	public String getUrlValues() {
		return urlValues;
	}

	/**
	 * @param urlValues
	 */
	public void setUrlValues(String urlValues) {
		this.urlValues = urlValues;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ShortUrlDto [keys=" + keys + ", urlValues=" + urlValues + "]";
	}
	
	
	
	
	
}
