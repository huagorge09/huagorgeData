package com.cmwa.ec.weixin.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.weixin.manager.wxevent.EventManager;

/**
 * 微信消息入口controller
 * 
 * @author jouislu
 * 
 */

@Controller("wxEventController")
@RequestMapping(value = "/WeixinService")
public class WXEventController {
	
	@Autowired
	private EventManager eventManager;
	
	private static Logger logger = Logger.getLogger(WXEventController.class.getName());

	@RequestMapping(value = "/main.xhtml", method = {RequestMethod.POST,RequestMethod.GET})
	@ResponseBody
	public void messageFileHandler(HttpServletResponse response,HttpServletRequest request) throws UnsupportedEncodingException, IOException  {
		
   	 	String responseString = eventManager.handlerRequest(request);
   	 	logger.info("MainServlet#responseString:"+responseString);
   	 	response.getOutputStream().write(responseString.getBytes("UTF-8"));
	}
	
}
