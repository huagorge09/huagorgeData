package com.cmwa.ecc.business.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回类型 常量
 * @author ex-liuy
 *
 */
public class ResultConstant {
	public final static Map<String, Object> resultMap = new HashMap<String, Object>();
	public static final String C_RESULT_CODE = "resultCode";
	public static final String C_RESULT_MSG = "resultMsg";
	/** 		返回编码 		 	*/
	//成功
	public static final String C_RESULT_SUCCESS = "0000";
	//出现异常
	public static final String C_RESULT_FAILE = "9999";
	
	
	/** 		返回信息 		 	*/
	static {  
		resultMap.put(C_RESULT_SUCCESS, "处理成功");
		resultMap.put(C_RESULT_FAILE, "操作出现异常");
	}  
}
