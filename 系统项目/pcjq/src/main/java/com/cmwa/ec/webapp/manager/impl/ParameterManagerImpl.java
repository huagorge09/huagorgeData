package com.cmwa.ec.webapp.manager.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.cmwa.ec.webapp.dao.ParameterDao;
import com.cmwa.ec.webapp.dto.ParameterDto;
import com.cmwa.ec.webapp.manager.ParameterManager;

public class ParameterManagerImpl implements ParameterManager{
	
	private Logger logger = Logger.getLogger(ParameterManagerImpl.class);
	
	private ParameterDao parameterDao;
	
	
	/**
	 * 查询所有数据字典
	 * @param pmky
	 * @return List<DataRow>
	 */
	public List list(){
		
		return parameterDao.list();
	}
	
	
	public ParameterDto queryAccessToken()
	{
		return parameterDao.queryByAccessToken();
	}


	public ParameterDao getParameterDao() {
		return parameterDao;
	}


	public void setParameterDao(ParameterDao parameterDao) {
		this.parameterDao = parameterDao;
	}
	
	
}
