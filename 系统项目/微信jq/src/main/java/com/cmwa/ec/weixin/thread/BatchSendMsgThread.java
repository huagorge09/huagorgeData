package com.cmwa.ec.weixin.thread;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.dto.SendMsgDto;
import com.cmwa.ec.weixin.util.StringUtils;

public class BatchSendMsgThread extends Thread{
	
	private MessageServiceClient serviceClient;
	
	private static final String ERROR_CODE = "9999";
	
	private static final String DBERR_CODE = "USR-5205";
	
	private static Logger logger = Logger.getLogger(BatchSendMsgThread.class);
	
	private Map<String,Object> map;
	
	public BatchSendMsgThread(Map<String,Object> map){
		this.map = map;
		this.serviceClient = (MessageServiceClient) SpringUtil.getBean("messageServiceClient");
	}
	
	@Override
	public void run(){
		sendMsg(map);
	}
	
	public void sendMsg(Map<String,Object> map){
		MsgSmsRecordDto msgRecord = null;
		int errCount = 0;
		for (String string : map.keySet()) {
			msgRecord = new MsgSmsRecordDto();
			msgRecord.setMobile(string);
			msgRecord.setContent(String.valueOf(map.get(string)));
			msgRecord.setMethod("A");
			msgRecord.setSendUser("system");
			try {
				Thread.sleep(100);
				MsgServiceMessageDto result = serviceClient.callMessToSendSmsMsg(msgRecord);
				if(result.getErrCode().equals(ERROR_CODE) || result.getErrCode().equals(DBERR_CODE) ){
					logger.error("BatchSendMsgThread.sendMsg-----号码"+string+"发送短信时失败-----errCode:"+result.getErrCode()+"-----errMsg:"+result.getErrMsg());
					errCount++;
				}
			} catch (Exception e) {
				logger.error("BatchSendMsgThread.sendMsg-----号码"+string+"发送短信时异常----",e);
				errCount++;
			}
		}
		logger.info("BatchSendMsgThread.sendMsg()>>>>>>批量发送短信执行完毕！需要发送的数量有："+map.size()+"条,发送成功的有:"+(map.size()-errCount)+"条,发送失败的有："+errCount+"条");
	}

}
