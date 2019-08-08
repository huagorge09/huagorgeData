package com.cmwa.ec.weixin.manager.business.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ec.query.facade.dto.user.UserCommonDto;
import com.cmwa.ec.query.facade.dto.user.UserInfoDto;
import com.cmwa.ec.query.facade.dto.user.UserInfoExtendDto;
import com.cmwa.ec.query.facade.dto.user.UserInfoexDto;
import com.cmwa.ec.query.facade.dto.user.UserOrderDto;
import com.cmwa.ec.query.facade.dto.user.WxUserInfoDto;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.dao.SynchronousDataDao;
import com.cmwa.ec.weixin.manager.business.SynchronousDataManager;

public class SynchronousDataManagerImpl implements SynchronousDataManager {

	private static Logger logger = LoggerFactory.getLogger(SynchronousDataManagerImpl.class);
	@Autowired
	private SynchronousDataDao synchronousDataDao;
	@Autowired
	private QueryServiceClient queryServiceClient;

	@Override
	public int synchUserInfoExtend(UserCommonDto dto, String channel) {
		if(dto != null) {
			try {
				List<UserCommonDto> commList = queryServiceClient.queryUserInfo(dto);
				if (commList != null && !commList.isEmpty()) {
					UserCommonDto commDto = commList.get(0);
					UserInfoExtendDto extendDto = new UserInfoExtendDto();
					extendDto.setOpenId(dto.getOpenId());
					extendDto.setRegistrStatus("Y");
					extendDto.setRealNameStatus("30".equals(commDto.getUserType()) ? "Y" : "N");
					extendDto.setRiskEvalStatus("0".equals(commDto.getRiskLevel()) ? "N" : "Y");
					extendDto.setRiskCode(commDto.getRiskLevel());
					if ("8".equals(commDto.getPaySt())) {
						extendDto.setPurchaseStatus("Y");
						extendDto.setBuyOnLine("Y");
					} else {
						extendDto.setPurchaseStatus("N");
						extendDto.setBuyOnLine("N");
					}
					extendDto.setLastRiskEvalDate(commDto.getLastEvalDate());
					extendDto.setUpdateBy(channel);
					extendDto.setCmfUserId(dto.getCmfUserId());
					extendDto.setCustNo(commDto.getEcCustNo());
					extendDto.setBuyOffLine("N");
					if (synchronousDataDao.queryInfoexCount(dto.getCmfUserId()) > 0) {
						synchronousDataDao.updateUserInfoExtend(extendDto);
					} else {
						extendDto.setCreatedBy(channel);
						synchronousDataDao.synchUserInfoExtend(extendDto);
					}
					logger.info("同步CMWA_WX_USER_INFO_EXTEND表成功");
					return 1;
				}
			} catch (Exception e) {
				logger.error("数据同步异常", e);
			}
		}
		return 0;
	}

	@Override
	public int synchUserOrderInfo(UserOrderDto dto) {
		int count = 0;
		List<UserInfoExtendDto> extendList = synchronousDataDao.getUserAndOpenId(dto);// 如果custNo为空 就全量捞出微信用户信息扩展表
		for (int i = 0; i < extendList.size(); i++) {
			UserInfoExtendDto extendDto = extendList.get(i);// 获取到的是新表(微信用户扩展信息表)信息
			String custNo = extendDto.getCustNo();
			if (!StringUtil.isEmpty(custNo)) {
				dto.setCustNo(custNo);
				List<UserOrderDto> orderList = queryServiceClient.queryUserOrderInfo(dto);// 新的订单表通过微信用户信息表的custNo去捞取数据
				for (int j = 0; j < orderList.size(); j++) {
					try {
						UserOrderDto orderDto = orderList.get(j);
						orderDto.setOpenId(extendDto.getOpenId());
						orderDto.setCmfUserId(extendDto.getCmfUserId());
						String nowDateStr = new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date());
						orderDto.setUpdateDate(nowDateStr);
						orderDto.setUpdateBy("synchronous");
						if (synchronousDataDao.queryUserOrderCount(orderDto.getSerialNo()) > 0) {
							synchronousDataDao.updateUserOrderInfo(orderDto);
						} else {
							orderDto.setCreateDate(nowDateStr);
							orderDto.setCreateBy("synchronous");
							synchronousDataDao.synchUserOrderInfo(orderDto);
						}
						count++;
					} catch (Exception e) {
						logger.error("数据同步异常", e);
					}
				}
			}
		}
		return count;
	}

	@Override
	public int synchOldUseInfo(UserInfoDto dto) { // 同步微信用户信息表
		int count = 0;
		List<UserInfoDto> userList = synchronousDataDao.queryWeiXinUserInfo(dto);
		for (int i = 0; i < userList.size(); i++) {
			try {
				UserInfoDto userDto = userList.get(i);
				WxUserInfoDto wxUserDto = new WxUserInfoDto();
				wxUserDto.setOpenId(userDto.getOpenid());
				wxUserDto.setCity(userDto.getCity());
				wxUserDto.setLanguage(userDto.getLanguage());
				wxUserDto.setHeadImgUrl(userDto.getHeadimgurl());
				wxUserDto.setNickName(userDto.getNickname());
				wxUserDto.setProvince(userDto.getProvince());
				wxUserDto.setCountry(userDto.getCountry());
				wxUserDto.setSex(userDto.getSex());
				String nowDateStr = new SimpleDateFormat("yyyyMMdd hh:mm:ss").format(new Date());
				wxUserDto.setUpdateDate(nowDateStr);
				wxUserDto.setUpdateBy("synchronous");
				if (synchronousDataDao.queryWxInfoCount(userDto.getOpenid()) > 0) {
					synchronousDataDao.updateWxUserInfo(wxUserDto);
				} else {
					wxUserDto.setUserId(UUID.randomUUID().toString());// 和api服务保持一致
					wxUserDto.setSubscribeTime(userDto.getSubscribe_time());
					wxUserDto.setSubscribe("1".equals(userDto.getStatus()) ? "1" : "0");
					wxUserDto.setCreateDate(nowDateStr);
					wxUserDto.setCreateBy("synchronous");
					synchronousDataDao.synchWxUserInfo(wxUserDto);
				}
				count++;
			} catch (Exception e) {
				logger.error("数据同步异常", e);
			}
		}
		return count;
	}

	@Override
	public List<UserInfoexDto> queryInfoex(String cmfUserId) {
		return synchronousDataDao.queryInfoex(cmfUserId);
	}

}
