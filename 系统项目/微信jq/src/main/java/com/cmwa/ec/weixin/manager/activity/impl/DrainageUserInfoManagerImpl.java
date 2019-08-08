package com.cmwa.ec.weixin.manager.activity.impl;

import java.util.UUID;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.weixin.dao.activity.DrainageUserInfoDao;
import com.cmwa.ec.weixin.dto.DrainageUserInfoDto;
import com.cmwa.ec.weixin.manager.activity.DrainageUserInfoManager;

public class DrainageUserInfoManagerImpl implements DrainageUserInfoManager {

	@Autowired
	private DrainageUserInfoDao userInfoDao;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.cmwa.ec.weixin.manager.business.DrainageUserInfoManager#insertUserInfo
	 * (com.cmwa.ec.weixin.dto.DrainageUserInfoDto)
	 */
	@Override
	public JSONObject insertUserInfo(DrainageUserInfoDto dto) throws Exception{
		JSONObject returnObj = new JSONObject();
		try{
			boolean isSuccess = true;
			logger.info("添加引流用户信息开始");
			if (StringUtils.isBlank(dto.getAppid())) {
				logger.info("引流用户渠道丢失");
				isSuccess = false;
			}
			if (StringUtils.isBlank(dto.getUserName())) {
				logger.info("用户名称为空");
				isSuccess = false;
			}
			if (StringUtils.isBlank(dto.getUserIdcard())) {
				logger.info("用户身份证为空");
				isSuccess = false;
			}
			if (!isSuccess) {
				returnObj.put("returnCode", "9999");
				returnObj.put("returnMsg", "关键参数丢失");
				return returnObj;
			}
			DrainageUserInfoDto existsUser = queryUserInfoByIdCardAndOwnerName(dto.getUserIdcard(), dto.getUserName());
			if(existsUser != null) {
				logger.info("已经存在的引流用户,不添加");
				returnObj.put("returnCode", "0000");
				returnObj.put("returnMsg", "添加成功！");
				return returnObj;
			}
			String randomUserId = UUID.randomUUID().toString();
			dto.setUserId(randomUserId);
			dto.setCreatedUser("DrainageUserInfoManager");
			dto.setUpdatedUser("DrainageUserInfoManager");
			int recordCount = userInfoDao.insertUserInfo(dto);
			if (recordCount >= 1) {
				returnObj.put("returnCode", "0000");
				returnObj.put("returnMsg", "添加成功！");
			} else {
				returnObj.put("returnCode", "9999");
				returnObj.put("returnMsg", "未知原因，添加失败");
			}
			return returnObj;
		}catch(Exception e) {
			logger.info("添加引流用户信息时，捕获异常，",e);
			returnObj.put("returnCode", "9999");
			returnObj.put("returnMsg", "系统内部异常");
			return returnObj;
		}
	}

	/* (non-Javadoc)
	 * @see com.cmwa.ec.weixin.manager.activity.DrainageUserInfoManager#queryUserInfo(java.lang.String)
	 */
	@Override
	public DrainageUserInfoDto queryUserInfo(String userId) {
		return userInfoDao.queryUserInfoByUserId(userId);
	}

	@Override
	public DrainageUserInfoDto queryUserInfoByIdCardAndOwnerName(String idcard,
			String ownerName) throws Exception{
		return userInfoDao.queryUserInfoByIdCardAndOwnerName(idcard,ownerName);
	}


}
