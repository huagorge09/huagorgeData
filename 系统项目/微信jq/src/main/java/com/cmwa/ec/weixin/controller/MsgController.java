package com.cmwa.ec.weixin.controller;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.weixin.dto.BatchSendMsgDto;
import com.cmwa.ec.weixin.manager.business.SendMsgManager;
import com.cmwa.ec.weixin.util.StringUtils;

/**
 * 类说明-短信发送请求处理类
 * @author ex-chenhq
 *
 */
@Controller("MsgController")
@RequestMapping(value = "/WeixinService")
public class MsgController {
	
	@Autowired
	private SendMsgManager sendMsgManager;
	
	/**
	 * 批量发送短信
	 * @param fileInputOne
	 * @param fileInputTwo
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/batchSendMsg.xhtml",produces="text/html;charset=UTF-8",method=RequestMethod.POST)
	@ResponseBody
	public String sendMsg(BatchSendMsgDto dto,HttpServletRequest request) {
		JSONObject result = new JSONObject();
		String sendType = dto.getSendType();
		if("1".equals(sendType)){//手动输入号码发送
			if(StringUtils.isEmptyString(dto.getOtherMobile())){
				result.put("resultCode","9999");
				result.put("resultMsg", "手机号码不能为空");
				return result.toString();
			}
		}else if("3".equals(sendType)){//excel文件导入号码发送
			if(dto.getSendFile() == null){
				result.put("resultCode","9999");
				result.put("resultMsg", "excel文件不能为空");
				return result.toString();
			}
		}
		result = sendMsgManager.batchSendMsg(dto,request);
		return result.toString();
	}

}
