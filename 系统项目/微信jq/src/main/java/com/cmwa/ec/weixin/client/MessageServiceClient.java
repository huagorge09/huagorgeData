package com.cmwa.ec.weixin.client;

import com.cmwa.ec.message.facade.MailService;
import com.cmwa.ec.message.facade.MessageService;
import com.cmwa.ec.message.facade.dto.BusinessTypeDto;
import com.cmwa.ec.message.facade.dto.MsgParameterDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.dto.mail.MailConfig;
import com.cmwa.ec.message.facade.dto.mail.MailMessage;
import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.model.MsgResult;

/**
 * 消息服务客户端服务类
 * @author  
 *
 */
public class MessageServiceClient {

	private MessageService messageService;
	
	private MailService mailService;
	
	 
	public MessageService getMessageService() {
		return messageService;
	}


	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}

	public MailService getMailService() {
		return mailService;
	}


	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}


	/**
	 * 发送短信验证码
	 * @param seqId 前端传过来的唯一的seqId
	 * @param mobile
	 * @param bnsType
	 * @param mantissa
	 * @param money
	 * @param bankName
	 * @return
	 * @throws Exception
	 */
	public MsgServiceMessageDto applyVrfCode(Context context,String mobile,String bnsType,String mantissa,String money,String bankName)throws Exception{
		return messageService.applyVrfCode(context,mobile,bnsType,mantissa,money,bankName);
	}
	/**
	 * 交易短信验证码
	 * @param seqId 前端传过来的唯一的seqId
	 * @param sessionid
	 * @param phoneNo
	 * @param vrfcode
	 * @return
	 * @throws Exception
	 */
	public MsgServiceMessageDto checkVrfCode(Context context,String sessionid, String phoneNo, String vrfcode)throws Exception{
		return messageService.checkVrfCode(context,sessionid, phoneNo, vrfcode);
	}

	 
	/**
	 * 给用户发送通知信息
	 * @param context
	 * @param msgParameter
	 * @return
	 * @throws Exception
	 */
	public MsgServiceMessageDto sendSmsMsg(Context context,MsgParameterDto msgParameter,MsgSmsRecordDto msgRecord)throws Exception{
		return messageService.sendMessage(context, msgParameter, msgRecord);
	}
	
	
	
	/**
	 * 发送通知消息(没有任何变量参数)
	 * @param mobile 发送到的手机号码 
	 * @param msgType 消息编号
	 * @return	ReturnMsg对象,包括消息发送成功与否,已经与返回码关联的描述.
	 * <pre>
	 * 返回码
	 * 成功							0000
	 * 关键参数为空						USR-5001
	 * 未配置消息模板					USR-5103
	 * 操作数据库失败					USR-5205
	 * 系统错误						9999
	 * </pre>
	 */
	public MsgServiceMessageDto sendTextMessage(Context context, MsgSmsRecordDto msgRecord){
		return messageService.sendTextMessage(context, msgRecord);
	}
	/**
	 * 发送短信<br>
	 * 具体是将记录插入到短信发送表里,由调用的第三方接口自动发送
	 * 
	 * @param mobile
	 *            手机号
	 * @param content
	 *            发送内容
	 * @return 插入成功与否
	 */
	public MsgServiceMessageDto justSendMsg(MsgSmsRecordDto msgRecord){
		return messageService.justSendMsg(msgRecord);
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


	public MailConfig queryMailConfig(String configId)throws Exception{
		MailConfig dto = null;
		try {
			dto =  mailService.queryMailConfig(configId);
		} catch (Exception e) {
			throw e;
		}
		return dto;
	}


	public String queryMailPathConfig(String configId) {
		String mailPath =  mailService.queryMailPathConfig(configId);
		return mailPath;
	}
	
	public String[] queryMailAppendList(String configId, String type) {
		return mailService.queryMailAppendList(configId, type);	 
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
	 * 发送邮件带附件
	 * @param mailMessage 
	 * @param fileName
	 * @return
	 * @throws Exception
	 */
	public MsgServiceMessageDto sendMailWithFile(MailMessage mailMessage,String fileName)throws Exception{
		MsgServiceMessageDto sendMailWithExcel = mailService.sendMailWithExcel(mailMessage, mailMessage.getFileStream(),fileName);
		return sendMailWithExcel;
	}
	
	public MsgServiceMessageDto callMessToSendSmsMsg(MsgSmsRecordDto msgRecord)throws Exception{
		MsgServiceMessageDto msgDto = messageService.justSendMsg(msgRecord);
		return msgDto;
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
	 * 根据短信模板id返回模板实体
	 * @param bnstype
	 * @return
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
