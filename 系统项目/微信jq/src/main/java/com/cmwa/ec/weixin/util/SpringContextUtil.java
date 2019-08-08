package com.cmwa.ec.weixin.util;

import java.lang.reflect.Method;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.PropertyResourceConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.support.PropertiesLoaderSupport;

public class SpringContextUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	private static Properties properties=new Properties(); 
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext)
	     throws BeansException{
		this.applicationContext  = applicationContext;
	        try{  
	            String[] postProcessorNames = applicationContext  
	                    .getBeanNamesForType(BeanFactoryPostProcessor.class,true,true);  
	              
	            for (String ppName : postProcessorNames) {  
	                BeanFactoryPostProcessor beanProcessor=  
	                		applicationContext.getBean(ppName, BeanFactoryPostProcessor.class);  
	                if(beanProcessor instanceof PropertyResourceConfigurer){  
	                    PropertyResourceConfigurer propertyResourceConfigurer = (PropertyResourceConfigurer) beanProcessor;  
	                    Method mergeProperties=PropertiesLoaderSupport.class.getDeclaredMethod("mergeProperties");  
	                    mergeProperties.setAccessible(true);  
	                    Properties props=(Properties) mergeProperties.  
	                    invoke(propertyResourceConfigurer);  
	                    Method convertProperties=PropertyResourceConfigurer.class.  
	                    getDeclaredMethod("convertProperties", Properties.class);  
	                    convertProperties.setAccessible(true);  
	                    convertProperties.invoke(propertyResourceConfigurer, props);  
	                    properties.putAll(props);  
	                }  
	            }  
	              
	        }catch(Exception e){  
	            throw new RuntimeException(e);  
	        }  
	    }  
	
	public static Object getBean(String name){
		return applicationContext.getBean(name);
	}
	
	public static String getProperty(String propertyName){  
        return properties.getProperty(propertyName);  
    }  

}
