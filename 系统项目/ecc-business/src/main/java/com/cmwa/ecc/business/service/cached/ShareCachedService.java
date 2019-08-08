package com.cmwa.ecc.business.service.cached;

import com.cmwa.ecc.business.exception.CachedException;

/**
 * 共享缓存接口，用于缓存数据库表中常用的字段映射值
 * 
 */
public interface ShareCachedService {
	/**
	 * 初始化加载所有的数据到缓存
	 */
	public void loadMappingCached() throws CachedException;

}
