package com.cmwa.ecc.business.utils;



import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取配置信息
 */
public class SysConf {
	private static final Logger logger = LoggerFactory.getLogger(SysConf.class);
	
	private static final String appHome = System.getProperty("appHome");
	
	private static final String PATH = appHome + "/config.properties";
	
	private static final String EMPTY_VAL = "";
	
	private static PropertiesConfiguration properties = null;

	static {
		try {
			properties = new PropertiesConfiguration(PATH);
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * <p>
	 * 得到一个整数属性
	 * 
	 * @param key
	 *            String
	 * @return Integer
	 * */
	public static Integer getInt(String key) {
		return properties.getInt(key);
	}

	/**
	 * <p>
	 * 得到一个长整数属性
	 * 
	 * @param key
	 *            String
	 * @return Integer
	 * */
	public static Long getLong(String key) {
		return properties.getLong(key);
	}

	/**
	 * <p>
	 * 从property中得到一个属性
	 * 
	 * @param key
	 *            String
	 * @return String
	 */
	public static String get(String key) {
		return properties.getString(key);
	}
	

	/**
	 * Default return null
	 * @param key
	 * @see #get(String)
	 * @see #getFailToError(String)
	 * @return
	 */
	public static String getFailToEmpty(String key) {
		properties.setThrowExceptionOnMissing(true);
		try {
			return properties.getString(key);
		} catch (Exception e) {
			properties.setThrowExceptionOnMissing(false);
			logger.error(
					String.format("%s not found in %s !", new Object[]{key,PATH}), 
					e
					);
			e.printStackTrace();
			return EMPTY_VAL;
		}
	}
	
	/**
	 * Unable to find key,will throw <B>Error<B>!
	 * @param key
	 * @see #get(String)
	 * @see #getFailToEmpty(String)
	 * @return
	 */
	public static String getFailToError(String key) {
		properties.setThrowExceptionOnMissing(true);
		try {
			properties.setThrowExceptionOnMissing(true);
			return properties.getString(key);
		} catch (Exception e) {
			logger.error(
					String.format("%s not found in %s !", new Object[]{key,PATH}), 
					e
					);
			throw new Error(
					String.format("%s not found in %s !", new Object[]{key,PATH})
					);
		}
	}

	/**
	 * <p>
	 * 从property中得到一个属性
	 * 
	 * @param param
	 *            String
	 * @param String
	 *            default value
	 * @return String
	 */
	public String get(String key, String defaultValue) {
		return properties.getString(key, defaultValue);
	}

}
