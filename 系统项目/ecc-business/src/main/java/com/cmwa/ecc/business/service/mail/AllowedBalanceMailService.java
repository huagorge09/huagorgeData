package com.cmwa.ecc.business.service.mail;

public interface AllowedBalanceMailService {
	
	/**
	 * 发送管家产品提醒可赎回份额邮件方法
	 * 
	 * @param context
	 * @param mailMessage
	 * @return
	 */
	public void remindRedeemSendMail() throws Exception;

}
