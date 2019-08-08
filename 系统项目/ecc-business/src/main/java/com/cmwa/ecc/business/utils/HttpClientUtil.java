package com.cmwa.ecc.business.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.EntityEnclosingMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtil {
	private static int connectTimeout = 20000;
	private static int readTimeout = 5*60*1000;
	private static String charset = "GBK";
	private static Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	
	public static String postMsg(String url, String msg) throws Exception
	{
		PostMethod post = new PostMethod(url);
		post.setRequestHeader("Content-type", "text/xml; charset=GBK");
		post.setRequestContentLength(EntityEnclosingMethod.CONTENT_LENGTH_CHUNKED);
		post.setRequestBody(msg);
		
		HttpClient httpclient = new HttpClient();//创建 HttpClient 的实例
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTimeout);
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(readTimeout);
		
		int result = httpclient.executeMethod(post);
		
		
		byte body[]  = post.getResponseBody();
		String resultMsg = new String(body);
		post.releaseConnection();
		return resultMsg;
	}
	
	public static String postForm(String url, Map<String, String> formDatas, int connectTime, int readTime, String charset) throws Exception
	{
		PostMethod post = new PostMethod(url);
		logger.info("postForm url="+url);
		post.addRequestHeader("Content-type", "application/x-www-form-urlencoded,charset=utf-8");
		post.getParams().setContentCharset("utf-8");
		
		List<NameValuePair> paramList = new ArrayList<NameValuePair>();
		Iterator<String> formKeys = formDatas.keySet().iterator();
		while(formKeys.hasNext())
		{
			String key = formKeys.next();
			String value = formDatas.get(key);
			logger.info("formDatas:"+key+"="+value);
			paramList.add(new NameValuePair(key,value));
			
		}
		
		NameValuePair[] tmpArrays = new NameValuePair[paramList.size()];
		paramList.toArray(tmpArrays);
        post.setRequestBody(tmpArrays);
		
		
		HttpClient httpclient = new HttpClient();//创建 HttpClient 的实例
		httpclient.getHttpConnectionManager().getParams().setConnectionTimeout(connectTime);
		httpclient.getHttpConnectionManager().getParams().setSoTimeout(readTime);
		int result = httpclient.executeMethod(post);
		logger.info("postMsg ret: "+result);
		
		byte body[]  = post.getResponseBody();
		String resultMsg = new String(body, charset);
		logger.info("postMsg msg: "+resultMsg);
		post.releaseConnection();
		return resultMsg;
	}
	
	public static String postForm(String url, Map<String, String> formDatas) throws Exception
	{
		return postForm(url, formDatas, connectTimeout, readTimeout, charset);
	}
	
	public static String postForm(String url, Map<String, String> formDatas, String charset) throws Exception
	{
		return postForm(url, formDatas, connectTimeout, readTimeout, charset);
	}
	
	public static String postForm(String url, Map<String, String> formDatas, int connectTime, int readTime) throws Exception
	{
		return postForm(url, formDatas, connectTime, readTime, charset);
	}
}