package com.cmwa.ecc.business.service.synchronization.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.cmwa.dtp.facade.util.DataTransferUtil;
import com.cmwa.ecc.business.service.synchronization.SynEmpDataService;

@Service("synEmpDataServiceImpl")
public class SynEmpDataServiceImpl implements SynEmpDataService{

	private Logger logger =LoggerFactory.getLogger(SynEmpDataServiceImpl.class);

	@Override
	public Map<String, Object> synEmpDataInfo() {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			map = DataTransferUtil.sendData("CBP-EMP", "WAEC-EMP", "DTP", "WAT-DTP", "", true);
			logger.info("定时同步用户，角色，部门以及关联数据信息结果："+map.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
