package com.cmwa.ec.weixin.manager.activity;

import net.sf.json.JSONObject;

import com.cmwa.ec.weixin.dto.DrainageUserInfoDto;

public interface DrainageUserInfoManager {

	/**
	 * 添加一个用户信息
	 * @param dto
	 * @return
	 */
	public JSONObject insertUserInfo(DrainageUserInfoDto dto) throws Exception;

	public DrainageUserInfoDto queryUserInfo(String userId);

	/**
	 * @param idcard
	 * @param ownerName
	 * @return
	 * @throws Exception 
	 */
	public DrainageUserInfoDto queryUserInfoByIdCardAndOwnerName(String idcard,
			String ownerName) throws Exception;
}
