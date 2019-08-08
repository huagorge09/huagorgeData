package com.cmwa.ecc.business.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * 获取spring 容器的上下文
 * 
 * @author Jiang Zhihui
 * @date 2015年5月13日
 * @time 下午6:26:48
 */
@Component
@Lazy(false)
public class SpringApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	@Override
	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		applicationContext = ac;
	}

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public static Object getBean(String name) {
		return getApplicationContext().getBean(name);
	}

	public static <T> T getBean(Class<T> arg0) {
		return getApplicationContext().getBean(arg0);
	}

}
