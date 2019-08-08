package com.cmwa.ec.weixin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.query.facade.dto.user.UserInfoExtendDto;
import com.cmwa.ec.weixin.manager.business.UserInfoexManager;
import com.cmwa.ec.weixin.util.SessionValue;

@Controller("MarketingPlatformController")
@RequestMapping(value = "/WeixinService")
public class MarketingPlatformController {
	
	private static Logger logger = Logger.getLogger(MarketingPlatformController.class.getName());
	
	@Autowired
	private UserInfoexManager userInfoexManager;

	@RequestMapping(value="/queryNewTableUserInfo.xhtml", produces="text/html;charset=UTF-8" ,  method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public JSONObject queryNewTableUserInfo(HttpServletResponse response,HttpServletRequest request)throws Exception{
		JSONObject object=new JSONObject();
		String openId=(String) request.getSession().getAttribute(SessionValue.SESSION_OPENID);
		logger.info("<<<<<<queryRiskLevel>>>>>>方法获取openId"+openId);
		if(openId!=null || openId!=""){
			UserInfoExtendDto dto=new UserInfoExtendDto();
			dto.setOpenId(openId);
			UserInfoExtendDto d=userInfoexManager.queryRiskLevel(dto);
			object.put("data", d);
		}
		return object;
	}
}
