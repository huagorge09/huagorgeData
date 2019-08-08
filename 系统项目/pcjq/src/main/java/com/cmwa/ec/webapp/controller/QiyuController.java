package com.cmwa.ec.webapp.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.system.ParameterDto;
import com.cmwa.ec.webapp.client.QueryServiceClient;

/**
 * 查找七鱼网站接入url的Controller
 * 
 * @author qinwf
 * 
 */
@Controller("QiyuController")
@RequestMapping(value = "/AppService")
public class QiyuController {
	
	private static Logger logger = Logger.getLogger(QiyuController.class.getName());
	
	@Autowired
	private QueryServiceClient queryServiceClient;
	
	@RequestMapping(value = "/kefu/queryQiyuURL.xhtml",  method = {RequestMethod.GET})
	@ResponseBody
	public String queryQiyuURL(HttpServletResponse response,HttpServletRequest request) {
		try {
			 QueryMessageDto queryParamListQiyuURL = queryServiceClient.queryHomeAddressIsWordWithLinkage(new Context(), "SYSTEM", "QIYUURL", "", "");
			 List<ParameterDto> listParaCc = (List<ParameterDto>) queryParamListQiyuURL.getData();
			 if(null != listParaCc && listParaCc.size() > 0) { 
				 return listParaCc.get(0).getPmco();
			 }
		} catch (Exception e) {
			logger.error("查询网易七鱼链接异常", e);
			return null;
		}
		return null;
	
	}

}
 