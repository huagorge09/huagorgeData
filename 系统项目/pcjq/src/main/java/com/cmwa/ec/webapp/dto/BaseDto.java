package com.cmwa.ec.webapp.dto;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;


public class BaseDto {
	 
	private String errorCode;
	private String errorMsg;
	private String subErrCode;
	private String subErrMsg;
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	 
	public String getSubErrCode() {
		return subErrCode;
	}
	public void setSubErrCode(String subErrCode) {
		this.subErrCode = subErrCode;
	}
	public String getSubErrMsg() {
		return subErrMsg;
	}
	public void setSubErrMsg(String subErrMsg) {
		this.subErrMsg = subErrMsg;
	}
	public static void main(String[] args) {
		ObjectMapper mapper = new ObjectMapper();
		BaseDto dto = new BaseDto();
		dto.setErrorCode("aaaa");
		dto.setErrorMsg("cccc");
		try {
			System.out.println(mapper.writeValueAsString(dto));
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
	}
	@Override
	public String toString() {
		return "BaseDto [errorCode=" + errorCode + ", errorMsg=" + errorMsg + ", subErrCode=" + subErrCode + ", subErrMsg=" + subErrMsg + "]";
	}	 
	

}
