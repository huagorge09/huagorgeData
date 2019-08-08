package com.cmwa.ec.weixin.controller;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.weixin.dao.activity.ShortUrlDao;
import com.cmwa.ec.weixin.dto.ShortUrlDto;

import net.sf.json.JSONObject;

@RequestMapping("/WeixinService")
@Controller
public class ShortUrlController {

	@Autowired
	private ShortUrlDao shortUrlDao;

	private final static Logger logger = LoggerFactory
			.getLogger(ShortUrlController.class);

	private static MessageDigest me = null;
	static {
		try {
			me = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/u.xhtml", produces = "text/html;charset=UTF-8", method = {
			RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public String getShortUrl(String url, String k, HttpServletRequest request,
			HttpServletResponse response) {
		JSONObject obj = new JSONObject();
		// 认为是获取短连接的
		if (StringUtils.isBlank(k)) {
			try{
				me.update(url.getBytes());
				byte[] result = me.digest();
				String encode = bytesToHex(result);
				ShortUrlDto prepareDto = prepareDto();
				prepareDto.setKeys(encode);
				prepareDto.setUrlValues(url);
				logger.debug("查询是否有重复的key值 生成的key值为："+encode);
				ShortUrlDto resultDto = shortUrlDao.queryShortUrlByPrimaryKey(encode);
				int insertResult = -1;
				if(resultDto != null) {
					logger.info("查询到key值已存在于数据库，返回当前存在的key值为："+resultDto.getKeys()+"存入的连接为："+resultDto.getUrlValues());
					obj.put("returnCode", "0");
					obj.put("returnMsg", "success");
					Map<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("url", SpringUtil.getProperty("wx.config.wxHost")
							+ "WeixinService/u.xhtml?k=" + resultDto.getKeys());
					hashMap.put("k", resultDto.getKeys());
					obj.put("data", hashMap);
					return obj.toString();
				} else {
					try{
						 insertResult = shortUrlDao.insertShortUrl(prepareDto);
					}catch(Exception e){
						obj.put("returnCode", "1");
						obj.put("returnMsg", "添加短连接映射失败");
						logger.error("添加短连接映射失败 :"+e);
						return obj.toString();
					}
					if(insertResult<=0){
						obj.put("returnCode", "1");
						obj.put("returnMsg", "添加短连接映射失败");
						return obj.toString();
					}
					obj.put("returnCode", "0");
					obj.put("returnMsg", "success");
					Map<String, Object> hashMap = new HashMap<String, Object>();
					hashMap.put("url", SpringUtil.getProperty("wx.config.wxHost")
							+ "WeixinService/u.xhtml?k=" + encode);
					hashMap.put("k", encode);
					obj.put("data", hashMap);
					return obj.toString();
				}
			}catch(Exception e){
				obj.put("returnCode", "1");
				obj.put("returnMsg", "添加短连接映射失败");
				logger.error("添加短连接映射失败 :"+e);
			}
			return obj.toString();
		} else {
			// 认为是做跳转的
			logger.info("接收到参数key值为:"+k);
			ShortUrlDto dto = shortUrlDao.queryShortUrlByPrimaryKey(k);
			if(dto!=null){
				logger.info("通过key值查询到原链接为"+dto.getUrlValues());
				String urlValuse = dto.getUrlValues();
				try {
					logger.info("将用户重定向到"+dto.getUrlValues());
					response.sendRedirect(urlValuse);
				} catch (IOException e) {
					logger.error("重定向失败，将用户重定向至活动首页"+e);
					try {
						response.sendRedirect(SpringUtil.getProperty("wx.config.wxHost")+"activity/fathersDay/index.html");
					} catch (IOException e1) {
						logger.error("重定向失败，尝试将用户转发至活动页面",e);
						try {
							request.getRequestDispatcher("/activity/fathersDay/index.html").forward(request, response);
						} catch (Exception e2){
							logger.error("转发失败....");
						}
					}
				}
			} else {
				try {
					logger.info("未查询到连接信息，重定向到活动首页");
					response.sendRedirect(SpringUtil.getProperty("wx.config.wxHost")+"activity/fathersDay/index.html");
				} catch (IOException e) {
					logger.error("重定向失败，尝试将用户转发至活动页面",e);
					try {
						request.getRequestDispatcher("/activity/fathersDay/index.html").forward(request, response);
					} catch (Exception e2){
						logger.error("转发失败....");
					}
				}
			}
		}
		return null;
	}

	public static String bytesToHex(byte[] bytes) {
		StringBuffer md5str = new StringBuffer();
		// 把数组每一字节换成16进制连成md5字符串
		int digital;
		for (int i = 0; i < bytes.length; i++) {
			digital = bytes[i];

			if (digital < 0) {
				digital += 256;
			}
			if (digital < 16) {
				md5str.append("0");
			}
			md5str.append(Integer.toHexString(digital));
		}
		return md5str.toString().toUpperCase();
	}

	
	/**
	 * @return
	 */
	public ShortUrlDto prepareDto() {
		ShortUrlDto dto = new ShortUrlDto();
		dto.setCreatedDate(new Date());
		dto.setUpdatedDate(new Date());
		dto.setCreatedBy("ShortUrlController");
		dto.setUpdatedBy("ShortUrlController");
		return dto;
	}
	
}
