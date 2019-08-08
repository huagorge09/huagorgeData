package com.cmwa.ecc.business.service.mail;

public interface TotalBalanceMailService  {
	/**
	 * 发送管家产品邮件发送T+7日最大赎回金额，邮件发送时间是T+1日
	 * 
	 * @param context
	 * @param mailMessage
	 * @return
	 */
	
	public void remindAllBalconsSendMail() throws Exception;

}
