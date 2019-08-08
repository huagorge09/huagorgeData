package com.cmwa.ec.weixin.interceptor;

public class TracingModel {
	
	private boolean needCheckParams=false;
	
	private String chekPrams="";
	
	private String redirectUrl="";
	
	private String charset="";
	
	private boolean needLogin=false;
	
	private String authority = "";
	

	public boolean isNeedLogin() {
		return needLogin;
	}

	public void setNeedLogin(boolean needLogin) {
		this.needLogin = needLogin;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}

	public String getChekPrams() {
		return chekPrams;
	}

	public void setChekPrams(String chekPrams) {
		this.chekPrams = chekPrams;
	}

	public boolean isNeedCheckParams() {
		return needCheckParams;
	}

	public void setNeedCheckParams(boolean needCheckParams) {
		this.needCheckParams = needCheckParams;
	}

	public String getAuthority() {
		return authority;
	}

	public void setAuthority(String authority) {
		this.authority = authority;
	}

	
	
}
