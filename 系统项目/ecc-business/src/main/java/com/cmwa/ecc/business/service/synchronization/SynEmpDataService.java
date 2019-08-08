package com.cmwa.ecc.business.service.synchronization;

import java.util.Map;


public interface SynEmpDataService {
	
	/**
	 * 同步用户，部门，角色信息
	 */
	public Map<String, Object> synEmpDataInfo();
}
