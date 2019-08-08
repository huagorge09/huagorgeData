package com.cmwa.ec.webapp.util.cache;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.webapp.dto.ParameterDto;
import com.cmwa.ec.webapp.manager.ParameterManager;
import com.cmwa.ec.webapp.util.SpringContextUtil;


public class ParameterCache {


	private static ParameterCache instance;
	private static Hashtable cache;
	private static ParameterDto tokenDto;
	
	@Autowired
	private ParameterManager delegate;
	
	private ParameterCache(){
		init();
	}
	
	public static ParameterCache getInstance() {
		if (instance == null) {
			instance = new ParameterCache();
			
		}
		return instance;
	}
	
	public static void refresh(){
		instance = new ParameterCache();
		
	}

	public void clear() {
		cache = new Hashtable();
	}
	
	public static Hashtable getData(){
		return getInstance().getCache();
	}
	
	public Hashtable getCache(){
		return cache;
	}
	
	public ParameterDto getToken(){
		return tokenDto;
	}
	
	public void setToken(ParameterDto tokenDto){
		this.tokenDto = tokenDto;
	}
	
	/**
	 * 初始化字典表记录
	 */
	private void init() {		 
		delegate = (ParameterManager)SpringContextUtil.getBean("parameterManager");
		cache = new Hashtable();
		try {
			List parameterList  = delegate.list();
			HashMap map = null;
			for(int i = 0; i < parameterList.size(); i++)
			{				
				ParameterDto dto = (ParameterDto)parameterList.get(i);				
				String cacheKey = dto.getPmst()  + "#" + dto.getPmky() +"#" + dto.getPmco() ;
				cache.put(cacheKey , dto);
			}
			
			System.out.println("初始化参数表 cache.size() = " +cache.size());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println("initDictionaryList 初始化数据字典失败"+e.getMessage());
		}
		
		tokenDto  = delegate.queryAccessToken();
	}
	

}
