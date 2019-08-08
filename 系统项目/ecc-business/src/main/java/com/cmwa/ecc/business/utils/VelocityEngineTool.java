package com.cmwa.ecc.business.utils;

import java.util.Properties;

import javax.servlet.ServletContext;

import org.apache.velocity.app.VelocityEngine;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.WebApplicationContext;

@Component
public class VelocityEngineTool  {
	private VelocityEngine velocityEngine;

	public void initVelocityEngine() throws Exception {
		WebApplicationContext webApplicationContext = ContextLoaderListener.getCurrentWebApplicationContext();
		ServletContext servletContext = webApplicationContext.getServletContext();
		String templatePath = servletContext.getRealPath("/service/velocity/");
		Properties properties = new Properties();
		// 设置模板引擎初始化参数
		properties.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		properties.setProperty("file.resource.loader.description", "Velocity File Resource Loader");
		properties.setProperty("file.resource.loader.path", templatePath);
		properties.setProperty("file.resource.loader.cache", "true");
		properties.setProperty("file.resource.loader.modificationCheckInterval", "30");
		properties.setProperty("runtime.log.logsystem.log4j.category", "velocity.log");
		properties.setProperty("runtime.log.logsystem.class", "org.apache.velocity.runtime.log.Log4JLogChute");
		properties.setProperty("runtime.log.logsystem.log4j.logger", "org.apache.velocity");
		properties.setProperty("directive.set.null.allowed", "true");
		properties.setProperty("runtime.log.info.stacktrace", "false");
		properties.setProperty("runtime.log.warn.stacktrace", "false");
		// 创建引擎
		velocityEngine = new VelocityEngine();
		velocityEngine.init(properties);
	}

	public VelocityEngine getVelocityEngine() throws Exception {
		if (velocityEngine==null) {
			initVelocityEngine();
		}
		return velocityEngine;
	}
}
