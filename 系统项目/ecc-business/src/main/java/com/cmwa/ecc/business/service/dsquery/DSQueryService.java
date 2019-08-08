package com.cmwa.ecc.business.service.dsquery;

import net.sf.json.JSONObject;


public interface DSQueryService {
	String getUrlAndEncrypt(String permissionId,String opId);
	/**
	 * 查询直销柜台系统状态等
	 * @return
	 */
	JSONObject queryEccSystemInfo();
}
