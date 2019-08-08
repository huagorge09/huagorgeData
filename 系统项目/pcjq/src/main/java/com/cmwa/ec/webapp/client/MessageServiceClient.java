package com.cmwa.ec.webapp.client;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.MailService;
import com.cmwa.ec.message.facade.MessageService;
import com.cmwa.ec.message.facade.dto.BusinessTypeDto;
import com.cmwa.ec.message.facade.dto.MsgParameterDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.dto.mail.MailConfig;
import com.cmwa.ec.message.facade.dto.mail.MailMessage;
import com.cmwa.ec.message.facade.model.MsgResult;

public class MessageServiceClient {

	private MessageService messageService;
	
	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
	
	private MailService mailService;
	
	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
	
	/**
	 * 发送短信
	 * @param context
	 * @param mobile
	 * @param bnsType
	 * @param carMantissa
	 * @param amount
	 * @param bankName
	 * @return
	 * 			MsgServiceMessageDto
	 * @author maj
	 */
	public MsgServiceMessageDto applyVrfCode(Context context, String mobile, String bnsType, String carMantissa, String amount, String bankName) {
		MsgServiceMessageDto msgServiceMessageDto = messageService.applyVrfCode(context, mobile, bnsType, carMantissa, amount, bankName);
		return msgServiceMessageDto;
	}

	/**
	 * 验证短信验证码
	 * @param context
	 * @param sessionID
	 * @param mobile
	 * @param rvrfcode
	 * @return
	 * 			MsgServiceMessageDto
	 * @author maj
	 */
	public MsgServiceMessageDto checkVrfCode(Context context, String sessionID, String mobile, String rvrfcode) {
		MsgServiceMessageDto msgServiceMessageDto = messageService.checkVrfCode(context, sessionID, mobile, rvrfcode);
		return msgServiceMessageDto;
	}
	
	/**
	 * 发送通知消息/短信
	 * @param context
	 * @param msgParameterDto
	 * @return
	 * 			MsgServiceMessageDto
	 * @author maj
	 */
	public MsgServiceMessageDto sendMessage(Context context, MsgParameterDto msgParameterDto, MsgSmsRecordDto msgSmsRecord) {
		MsgServiceMessageDto msgServiceMessageDto = messageService.sendMessage(context, msgParameterDto, msgSmsRecord);
		return msgServiceMessageDto;
	}

	/**
	 * 发送短信   通知短信
	 * @param context
	 * @param mobile
	 * @param msgType
	 * @return
	 * 			MsgServiceMessageDto
	 * @author maj
	 */
	public MsgServiceMessageDto sendTextMessage(Context context, MsgSmsRecordDto msgRecord) {
		MsgServiceMessageDto msgServiceMessageDto = messageService.sendTextMessage(context, msgRecord);
		return msgServiceMessageDto;
	}

	/**
	 * 给用户发送通知信息
	 * @param context
	 * @param msgParameter
	 * @return
	 * @throws Exception
	 */
	public MsgServiceMessageDto sendSmsMsg(Context context,MsgParameterDto msgParameter, MsgSmsRecordDto msgRecord)throws Exception{
		return messageService.sendMessage(context, msgParameter ,msgRecord);
	}

	/**
	 * 给用户发送通知信息
	 * @param context
	 * @param msgParameter
	 * @return
	 * @throws Exception
	 */
	public MsgServiceMessageDto sendMail(Context context,MailMessage mailMessage)throws Exception{
		MsgServiceMessageDto dto =  mailService.sendMail(mailMessage);
		return dto;
	}
	
	/**
	 * 发送邮件包含附件流
	 * @param mailMessage
	 * @param fileStream
	 * @return
	 * @throws Exception
	 */
	public MsgServiceMessageDto sendMail(MailMessage mailMessage,byte[] fileStream)throws Exception{
		MsgServiceMessageDto dto =  mailService.sendMail(mailMessage,fileStream);
		return dto;
	}
	
	/**
	 * 查询收件人抄送人列表
	 * @param context
	 * @param configId
	 * @param category
	 * @return
	 * @throws Exception
	 */
	public String[] queryMailToOrCcInfo(String configId,String category)throws Exception{
		return mailService.queryMailAppendList(configId,category);
	}
	
	/**
	 * 根据邮件配置id查询邮件配置信息
	 * @param config
	 * @return
	 */
	public MailConfig queryMailConfig(String configIdy)throws Exception{
		return mailService.queryMailConfig(configIdy);
	}
	
	/**
	 * 根据邮件配置id查询邮件配置信息
	 * @param configId
	 * @return
	 * @throws Exception
	 */
	public MailConfig queryMailAll(String configId)throws Exception{
		MailConfig dto = null;
		try {
			dto =  mailService.queryMailAll(configId);
		} catch (Exception e) {
			throw e;
		}
		return dto;
	}
	
	/**
	 * 发送邮件
	 * @param context
	 * @param mailMessage
	 * @return
	 * @throws Exception
	 */
	public MsgServiceMessageDto sendMail(MailMessage mailMessage)throws Exception{
		MsgServiceMessageDto dto =  mailService.sendMail(mailMessage);
		return dto;
	}

	/**
	 * 发送短信<br>
	 * 具体是将记录插入到短信发送表里,由调用的第三方接口自动发送
	 *
	 * @return MsgServiceMessageDto
	 */
	public MsgServiceMessageDto justSendMsg(MsgSmsRecordDto msgRecord){
		return messageService.justSendMsg(msgRecord);
	}

	/**
	 * 根据短信模板id返回模板实体
	 *
	 * @param msgType
	 * @return BusinessTypeDto
	 */
	public BusinessTypeDto queryBusinessType(String msgType) {
		return messageService.queryBusinessType(msgType);
	}

	/**
	 * 发送营销类短信
	 *
	 * @param msgRecord
	 * @return
	 */
	public MsgResult sendMarketMsg(MsgSmsRecordDto msgRecord) {
		return messageService.sendMarketMsg(msgRecord);
	}
}
