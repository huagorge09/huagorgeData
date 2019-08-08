package com.cmwa.ec.weixin.util;

import org.springframework.beans.factory.FactoryBean;

public class EncryptFactoryBean implements FactoryBean<String> {
	private String secretContent;

	private String secretKey;

	public String getObject() throws Exception {
		String afterDecStr = "";
		try {
			afterDecStr = new String(AesDecoder.decrypt(AesDecoder.parseHexStr2Byte(secretContent), secretKey));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return afterDecStr;
	}

	@Override
	public Class<?> getObjectType() {
		return String.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public String getSecretContent() {
		return secretContent;
	}

	public void setSecretContent(String secretContent) {
		this.secretContent = secretContent;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

}
