package com.cmwa.ecc.business.utils;

public class ResCodeConstant {
	
	 public static class SystemConstant{
		 /**
		  * 获取银行卡号失败 "7101"
		  */
		 public static final String CODE7101= "7101";
		 
		 /**
		  * 银行代码已存在 "7102"
		  */
		 public static final String CODE7102= "7102";
		 
		 
		 /**
		  * 支付渠道管理-获取第三方支付失败 "7401"
		  */
		 public static final String CODE7401="7401";
		 
		 /**
		  * 支付渠道管理-获取支持银行失败 "7402"
		  */
		 public static final String CODE7402="7402";


		 /**
		  * 支付渠道管理-重复支付渠道代码 "7403"
		  */	
		public static final Object CODE7403 = "7403";
		
		/**
		 * 参数管理-重复参数值
		 */
		public static final Object CODE7501 = null;
	 }
	 
	 public static class BusinessConstant{
		 
		 /**
		  *  直销明细 -重复数据
		  */
		 public static final String CODE6001="6001";
		 /**
		  *  直销明细 -重复数据
		  */
		 public static final String CODE6002="6002";
	 }
	 
	 /**
	  * 成功返回码 "0000"
	  */
	public static final String SUCCESS_CODE = "0000";
	/**
	 * 失败返回码
	 */
	public static final Object FAIL_CODE = "0001";
}
