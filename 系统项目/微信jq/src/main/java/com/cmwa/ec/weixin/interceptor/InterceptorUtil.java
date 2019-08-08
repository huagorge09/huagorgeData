package com.cmwa.ec.weixin.interceptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;



public class InterceptorUtil {
	private static Logger logger = Logger.getLogger(InterceptorUtil.class.getName());
    public static TracingModel extractTracingModel(Method method) {
    	TracingModel model = new TracingModel();
    	try{
			Annotation annotation = method.getAnnotation(TracingInfo.class);
	        if (null != annotation && annotation instanceof TracingInfo) {
	            model.setNeedCheckParams(((TracingInfo) annotation).needCheckParams());
	            model.setChekPrams(((TracingInfo) annotation).chekPrams());
	            model.setRedirectUrl(((TracingInfo)annotation).redirectUrl());
	            model.setCharset(((TracingInfo)annotation).charset());
	            model.setNeedLogin(((TracingInfo) annotation).needLogin());
	            model.setAuthority(((TracingInfo)annotation).authority());
	        }
    	}catch(Exception ex){
    		logger.error("InterceptorUtil.extractTracingModel“Ï≥£",ex);
    	}
        return model;
    }
    public static boolean extractPramas(TracingModel model,HttpServletRequest req) {
        if(model.isNeedCheckParams()){
        	String[] parms=model.getChekPrams().split(",");
        	for(String param:parms){
        		if(req.getParameter(param)==null){
        			return false;
        		}
        	}
        }
        return true;
    }
}
