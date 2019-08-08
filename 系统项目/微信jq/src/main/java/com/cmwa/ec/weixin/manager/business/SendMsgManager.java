package com.cmwa.ec.weixin.manager.business;

import javax.servlet.http.HttpServletRequest;
import com.cmwa.ec.weixin.dto.BatchSendMsgDto;
import net.sf.json.JSONObject;

public interface SendMsgManager {
	
	/**
	 * 批量发送短信
	 * @param dto
	 * @param request
	 * @return
	 */
	public JSONObject batchSendMsg(BatchSendMsgDto dto, HttpServletRequest request);

}
