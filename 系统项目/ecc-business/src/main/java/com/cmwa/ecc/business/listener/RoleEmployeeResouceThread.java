package com.cmwa.ecc.business.listener;

import org.apache.log4j.Logger;

import com.cmwa.ecc.business.service.resource.ResourceService;
import com.cmwa.ecc.business.utils.SpringApplicationContextUtil;
/**
 * @TODO	角色员工-资源关系更新
 * @author ex-liuy
 * @createDate 2017年8月9日
 */
public class RoleEmployeeResouceThread extends Thread {
	private final Logger logger = Logger.getLogger(RoleEmployeeResouceThread.class);
	
	@Override
	public void run() {
		try{   
			 //权限更新
			ResourceService resourceService = SpringApplicationContextUtil.getBean(ResourceService.class);
			resourceService.quartzResetResourceEmpDataByRoleList();
		}catch(Exception e){
			logger.error("----RoleEmployeeResouceThread--ResetRes-Exception：",e);
		}
	}
	
}
