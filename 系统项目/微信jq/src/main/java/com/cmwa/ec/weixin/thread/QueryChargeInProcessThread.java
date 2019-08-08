package com.cmwa.ec.weixin.thread;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmwa.ec.message.facade.model.MsgResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.message.facade.dto.BusinessTypeDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.dao.activity.DrainageUserInfoDao;
import com.cmwa.ec.weixin.dao.activity.SinoActPhoneCardDao;
import com.cmwa.ec.weixin.dto.DrainageUserInfoDto;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.dto.SinoActPhoneCardDto;
import com.cmwa.ec.weixin.model.SinoActChargeInfoModel;
import com.cmwa.ec.weixin.util.DESUtils;
import com.cmwa.ec.weixin.util.StringHelper;

/**
 * 此线程用于查询正在处理中的充值订单
 *
 * 03132019MGM需求新增对积分兑换渠道的重发处理 by ex-chent
 *
 * @author ex-wangz2
 *
 */
public class QueryChargeInProcessThread extends Thread implements InitializingBean {

	private static final Logger logger = LoggerFactory.getLogger(QueryChargeInProcessThread.class);
	
	private static final String QUERYORDER_PARAM = "{\"agtPhone\": \"{agtPhone}\",\"reqStreamId\": \"{reqStreamId}\"}";
	
	private String appId;
	
	private String appKey;
	
	private String agtPhone;
	
	private String executeFlag;
	
	private RestTemplate rest;
	
	private SinoActPhoneCardDao sinoActPhoneCardDao;
	
	private DrainageUserInfoDao drainageUserInfoDao;
	
	private QueryServiceClient queryServiceClient;
	
	private MessageServiceClient messageServiceClient;
	
	public void initParam() {
		this.appId = SpringUtil.getProperty("cmwa.wx.sino.appId");
		this.appKey = SpringUtil.getProperty("cmwa.wx.sino.appKey");
		this.agtPhone = SpringUtil.getProperty("cmwa.wx.sino.award.agtPhone");
		this.executeFlag = SpringUtil.getProperty("cmwa.wx.sino.resendthred.excute");
		this.sinoActPhoneCardDao = SpringUtil.getBean(SinoActPhoneCardDao.class);
		this.drainageUserInfoDao = SpringUtil.getBean(DrainageUserInfoDao.class);
		this.queryServiceClient = SpringUtil.getBean(QueryServiceClient.class);
		this.messageServiceClient = SpringUtil.getBean(MessageServiceClient.class);
		this.rest = new RestTemplate();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		while (true) {
			try {
				BusinessTypeDto integralTypeDto = messageServiceClient.queryBusinessType("57");
				String integralMsgContent = integralTypeDto == null ? null : integralTypeDto.getMsgTemplate()
					.replace("{amount}", "100")
					.replace("{mobile}", "0755-23988886");
				logger.info("查询处理中的充值订单线程休眠5分钟");
				Thread.sleep(1000*60*5);
				List<ParameterDto> sinoQueryUrl = queryServiceClient.queryParameter("SYSTEM", "SINOQUERYURL", null, null);
				String queryUrl = (sinoQueryUrl == null || sinoQueryUrl.isEmpty()) ? null : sinoQueryUrl.get(0).getPmco();
				BusinessTypeDto businessTypeDto = messageServiceClient.queryBusinessType("41");
				String sendMsg = businessTypeDto == null ? null : businessTypeDto.getMsgTemplate();
				List<ParameterDto> sinoChargeMoney = queryServiceClient.queryParameter("SYSTEM", "SINOCHARGEMONEY", null, null);
				String chargeMoney = (sinoChargeMoney == null || sinoChargeMoney.isEmpty()) ? null : sinoChargeMoney.get(0).getPmco();
				if(StringHelper.isBlank(queryUrl)) {
					logger.warn("查询充值订单URL配置内容为空，请留意配置");
					continue;
				}
				if(StringHelper.isBlank(sendMsg) || StringHelper.isBlank(chargeMoney)) {
					logger.warn("短信或者充值金额配置内容为空，请留意配置");
					continue;
				}
				sendMsg = sendMsg.replace("{chargeMoney}", chargeMoney);
				List<SinoActPhoneCardDto> list = sinoActPhoneCardDao.queryInProcess();
				if(list.isEmpty()) {
					logger.info("没有正在处理中的充值订单，等待下一轮");
					continue;
				}
				for (SinoActPhoneCardDto dto : list) {
					logger.info("即将要查询的充值订单 reqStreamId:{}", dto.getReqStreamId());
					String param = QUERYORDER_PARAM.replace("{agtPhone}", agtPhone).replace("{reqStreamId}", dto.getReqStreamId());
					param = DESUtils.encrypt(param, appKey);
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("appId", appId);
					map.put("param", param);
					ResponseEntity<SinoActChargeInfoModel> response = rest.exchange(queryUrl, HttpMethod.POST, getRequestEntity(map), 
							new ParameterizedTypeReference<SinoActChargeInfoModel>() {});
					SinoActChargeInfoModel model = response.getBody();
					logger.info("查询返回结果 responseBody:{}", model);
					if(model == null) {
						logger.warn("请求查询接口没有返回结果，查询失败");
						continue;
					}
					String status = model.getStatus();
					if("1000".equals(status)) {
						logger.info("电话卡充值成功，准备给客户发送短信提醒");
						// 查询到的充值结果为成功
						updateQueryResult(model,"S");
						DrainageUserInfoDto userInfoDto = drainageUserInfoDao.queryUserInfoByIdCard(dto.getIdCard());
						String userName = userInfoDto == null ? null : userInfoDto.getUserName();
						MsgSmsRecordDto msgRecord = new MsgSmsRecordDto();
						msgRecord.setMobile(dto.getAssignMobile());
						msgRecord.setMsgType(businessTypeDto.getBnsType());
						msgRecord.setMsgTypeDesc(businessTypeDto.getBnsTypeDesc());
                        if ("Integral".equals(dto.getAssignChannel())) {
							msgRecord.setMsgType(integralTypeDto.getBnsType());
							msgRecord.setMsgTypeDesc(integralTypeDto.getBnsTypeDesc());
                            msgRecord.setContent(integralMsgContent);
                            msgRecord.setMethod("A");
                            msgRecord.setSendUser("system");
                            msgRecord.setClientName(userName);
                            MsgResult msgResult = messageServiceClient.sendMarketMsg(msgRecord);
                            if (null != msgResult && msgResult.isSuccess()) {
                                logger.info("短信发送成功");
                            } else {
                                logger.error("短信发送失败_Mobile:{}", dto.getAssignMobile());
                            }
                        } else {
                            msgRecord.setContent(sendMsg);
                            msgRecord.setMethod("A");
                            msgRecord.setSendUser("system");
                            msgRecord.setClientName(userName);
                            MsgServiceMessageDto msgResult = messageServiceClient.justSendMsg(msgRecord);
                            if(msgResult != null && "0000".equals(msgResult.getReturnCode())) {
                                logger.info("短信发送成功");
                            }else {
                                logger.warn("短信发送失败 moblie:{}", dto.getAssignMobile());
                            }
                        }
					} else if("1001".equals(status) || "1015".equals(status)
							|| "1018".equals(status)) {
						if("1015".equals(status) && StringHelper.isBlank(dto.getQueryStatus()) 
								&& StringHelper.isBlank(dto.getQueryMsg())) {
							logger.info("电话卡充值处理中");
							// 失败结果 1015 如果是第一次查询先不做失败处理
							updateQueryResult(model,"I");
						}else {
							logger.info("电话卡充值失败");
							// 查询到的充值结果为失败
							updateQueryResult(model,"F");
						}
					}else {
						logger.info("电话卡充值处理中");
						// 其他结果仍当做处理中
						updateQueryResult(model,"I");
					}
				}
			} catch (Exception e) {
				logger.error("正在处理中的充值订单处理线程执行异常",e);
			}
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("查询处理中的充值订单线程准备初始化");
		this.initParam();
		logger.info("线程初始化完毕");
		if(!this.executeFlag.equalsIgnoreCase("ON")) {
			logger.info("本机线程启动配置为：{},不启动查询处理中充值订单线程",executeFlag);
			return;
		} else {
			logger.info("启动查询处理中的充值订单线程");
			this.start();
			logger.info("线程启动完毕");
		}
	}
	
	private void updateQueryResult(SinoActChargeInfoModel model,String assignStatus) {
		try {
			SinoActPhoneCardDto dto = new SinoActPhoneCardDto();
			dto.setReqStreamId(model.getData().getReqStreamId());
			dto.setAssignStatus(assignStatus);
			dto.setQueryStatus(model.getStatus());
			dto.setQueryMsg(model.getMsg());
			dto.setUpdatedUser(this.getClass().getSimpleName());
			logger.info("准备将查询到的充值结果更新入库 info:{}", dto);
			sinoActPhoneCardDao.updateQueryResult(dto);
			logger.info("查询到的充值结果更新入库成功");
		} catch (Exception e) {
			logger.error("查询到的充值结果更新入库出现异常", e);
		}
	}
	
	private <T> HttpEntity<T> getRequestEntity(T body) {
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
		headers.setContentType(mediaType);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));
		HttpEntity<T> requestEntity = new HttpEntity<T>(body, headers);
		return requestEntity;
	}
}
