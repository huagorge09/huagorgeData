package com.cmwa.ec.weixin.manager.business.impl;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.ibatis.annotations.Param;
import org.apache.log4j.Logger;
import org.jfree.util.Log;

import com.cmwa.ec.weixin.dao.ParameterDao;
import com.cmwa.ec.weixin.dao.activity.ActParameterDao;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.manager.business.ParameterManager;




public class ParameterManagerImpl implements ParameterManager
{
	private Logger logger = Logger.getLogger(ParameterManagerImpl.class);
	
	private ParameterDao parameterDao;
	
	private ActParameterDao actParameterDao;
	
	public static final Lock lock = new ReentrantLock();
	
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

	@Override
	public void updateJdCardStock(@Param("dto") ParameterDto dto) {	
		lock.lock();
		try {
			actParameterDao.updatePmcoByPmky(dto);
		} catch (Exception e) {
			logger.error("更新京东卡库存失败"+e);
		}finally{
			lock.unlock();
		}
	}	
}
