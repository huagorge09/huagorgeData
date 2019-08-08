package com.cmwa.ecc.business.service.impl.cached;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.exception.CachedException;
import com.cmwa.ecc.business.service.cached.ShareCachedService;
import com.cmwa.ecc.business.utils.DepartmentUtil;
import com.cmwa.ecc.business.utils.UserInfoUtil;
import com.cmwa.ecc.business.utils.cached.DepartmentCached;
import com.cmwa.ecc.business.utils.cached.DictionaryCached;
import com.cmwa.ecc.business.utils.cached.EmployeeCached;

@Service("shareCachedService")
public class ShareCachedServiceImpl implements ShareCachedService {
	private static final Log log = LogFactory.getLog(ShareCachedService.class);

	@Resource
	private DepartmentCached departmentCached;
	@Resource
	private DictionaryCached dictionaryCached;
	@Resource
	private EmployeeCached employeeCached;
	@Resource
	private UserInfoUtil userInfoUtil;
	@Resource
	private DepartmentUtil departmentUtil;

	/**
	 * 执行公共缓存的初始化，初始化加载所有的数据到缓存
	 */
	@Override
	public void loadMappingCached() throws CachedException {
		loadKVMappingCached();
		loadUtilCached();
	}

	/**
	 * 加载用于映射缓存
	 * 
	 * @throws CachedException
	 */
	public void loadKVMappingCached() throws CachedException {
		long start = System.currentTimeMillis();
		departmentCached.loadMappingCached();
		dictionaryCached.loadMappingCached();
		employeeCached.loadMappingCached();
		long end = System.currentTimeMillis();
		log.info("loadMappingCached spend " + (end - start) + "Millis");
	}

	/**
	 * 加载util包下常用的缓存
	 * 
	 * @throws CachedException
	 */
	public void loadUtilCached() throws CachedException {
		long start = System.currentTimeMillis();
		userInfoUtil.reload();
		departmentUtil.reload();
		long end = System.currentTimeMillis();
		log.info("loadUtilCached spend " + (end - start) + "Millis");
	}

}
