package com.cmwa.ec.webapp.util;

import java.util.List;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * http post请求工具类
 * @author ex-tuoy
 * 
 */
public class HttpPostUtil {
	
	private final static Logger logger = LoggerFactory.getLogger(HttpPostUtil.class);
	
	public static String sendPost(String methodUrl,List<NameValuePair> param,String contentType) {
		String response = "";
		// 创建连接对象
		HttpClient client = new DefaultHttpClient();
		HttpPost post = null;
		try {
			// 建立连接url
			post = new HttpPost(""+SpringContextUtil.getProperty("API_URL") +methodUrl);
			post.setEntity(new UrlEncodedFormEntity(param));
			
			// 获得请求后的响应
			HttpResponse httpResponse = client.execute(post);
			// 判断返回的状态值
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				// 如果请求成功，获取报文的实体
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity);
			}
		} catch (Exception e1) {
			logger.error("1......远程api调用异常",e1);
		} finally {
			try {
				//释放连接资源
				if (post != null) 
					post.getEntity().getContent().close();
			} catch (Exception e) {
				logger.info("1......httplient释放资源异常: ",e);
			}
			logger.info("1......HttpPostUtil调用api服务post接口返回结果：{}",response);
		}
		return response;
	}
	
	public static String sendPostJsonFormat(String methodUrl,String contentType,String json) {
		String response = "";
		// 创建连接对象
		HttpClient client = new DefaultHttpClient();
		HttpPost post = null;
		try {
			// 建立连接url
			post = new HttpPost(""+SpringContextUtil.getProperty("API_URL") +methodUrl);
			//设置content-type
			if(!StringUtils.isEmptyString(contentType)){
				post.addHeader("Content-Type",contentType);
				// 模拟请求
				post.setEntity(new StringEntity(json));
			}
			
			// 获得请求后的响应
			HttpResponse httpResponse = client.execute(post);
			// 判断返回的状态值
			if(httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK){
				// 如果请求成功，获取报文的实体
				HttpEntity entity = httpResponse.getEntity();
				response = EntityUtils.toString(entity);
			}
		} catch (Exception e1) {
			logger.error("2......远程api调用异常",e1);
		} finally {
			try {
				//释放连接资源
				if (post != null) 
					post.getEntity().getContent().close();
			} catch (Exception e) {
				logger.info("2......httplient释放资源异常: ",e);
			}
			logger.info("2......HttpPostUtil调用api服务post接口返回结果：{}",response);
		}
		return response;
	}
}
