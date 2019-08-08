package com.cmwa.ec.weixin.thread;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
import com.cmwa.ec.message.facade.wsadapter.DateUtil;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.dao.activity.ActParameterDao;
import com.cmwa.ec.weixin.dao.activity.DrainageUserInfoDao;
import com.cmwa.ec.weixin.dao.activity.SinoActAwardDao;
import com.cmwa.ec.weixin.dto.DrainageUserInfoDto;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.dto.SinoActAwardDto;
import com.cmwa.ec.weixin.manager.business.MessageManager;
import com.cmwa.ec.weixin.model.SinoActAwardInfoModel;
import com.cmwa.ec.weixin.util.DESUtils;
import com.cmwa.ec.weixin.util.StringHelper;

/**
 * 
 * 针对未获取到京东卡的信息进行重新发送
 * @author ex-hezk
 *
 */
public class ResendExceptionAwardThread extends Thread implements InitializingBean{

	private SinoActAwardDao sinoActAwardDao;
	
	private DrainageUserInfoDao drainageUserInfoDao;
	
	private MessageServiceClient serviceClient;
	
	private static final Logger logger = LoggerFactory.getLogger(ResendExceptionAwardThread.class);
	
	private static final String TRADE_REQUEST_PARAM = "{\"agtPhone\": \"{agtPhone}\",\"productCode\": {productCode},\"reqStreamId\": \"{reqStreamIdParam}\",\"amount\": 1,\"tradePwd\": \"{tradePwd}\"}";
	
	private static final String QUERY_REQUEST_PARAM = "{\"agtPhone\": \"{agtPhone}\",\"reqStreamId\": \"{reqStreamIdParam}\",\"tradePwd\": \"{tradePwd}\"}";
	
	private String tradeRequestUrl;
	
	private String queryRequestUrl;
	
	private String appKey;
	
	private String appId;
	
	private String decryptKey;
	
	private String agtPhone;
	
	private String tradePwd;
	
	private RestTemplate rest;
	
	private QueryServiceClient queryServiceClient;
	
	private MessageManager messageManager;
	
	private ActParameterDao actParameterDao;
	
	public void initParam() {
		this.queryRequestUrl = SpringUtil.getProperty("cmwa.wx.sino.queryRequestUrl");
		this.tradeRequestUrl = SpringUtil.getProperty("cmwa.wx.sino.tradeRequestUrl");
		this.appKey = SpringUtil.getProperty("cmwa.wx.sino.appKey");
		this.appId = SpringUtil.getProperty("cmwa.wx.sino.appId");
		this.decryptKey = SpringUtil.getProperty("cmwa.wx.sino.award.decryptKey");  
		this.agtPhone = SpringUtil.getProperty("cmwa.wx.sino.award.agtPhone");
		this.tradePwd = SpringUtil.getProperty("cmwa.wx.sino.award.tradePwd");
		this.serviceClient = (MessageServiceClient) SpringUtil.getBean("messageServiceClient");
		this.sinoActAwardDao = SpringUtil.getBean(SinoActAwardDao.class);
		this.drainageUserInfoDao = SpringUtil.getBean(DrainageUserInfoDao.class);
		this.queryServiceClient = SpringUtil.getBean(QueryServiceClient.class);
		this.messageManager = SpringUtil.getBean(MessageManager.class);
		this.actParameterDao = SpringUtil.getBean(ActParameterDao.class);
		rest = new RestTemplate();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		
		String executeFlag = SpringUtil.getProperty("cmwa.wx.sino.resendthred.excute");
		if(!executeFlag.equalsIgnoreCase("ON")) {
			logger.info("本机线程执行配置为：{},不执行重发线程",executeFlag);
			return;
		}
		while(true) {
			try {
				// 获取productCode
				ParameterDto productCodeParam = new ParameterDto();
				productCodeParam.setPmst("SYSTEM");
				productCodeParam.setPmky("SINOPRODUCTCODE");
				List<ParameterDto> queryParameter = queryServiceClient.queryParameter("SYSTEM", "SINOPRODUCTCODE", null, null);
				String productCode = (queryParameter == null || queryParameter.size() == 0)? null : queryParameter.get(0).getPmco();
				if(productCode == null) {
					logger.warn("未查询到配置的产品代码，等待下一次执行");
					continue;
				}
				// 获取awardStock 京东卡库存
				List<ParameterDto> queryParameter1 = queryServiceClient.queryParameter("SYSTEM", "SINOAWARDSTOCK", null, null);
				List<ParameterDto> queryParameter2 = queryServiceClient.queryParameter("SYSTEM", "SINOTHRESHOLD", null, null);
				String awardStock = (queryParameter1 == null || queryParameter1.size() == 0) ? null : queryParameter1.get(0).getPmco();
				String sinoThreshold = (queryParameter2 == null || queryParameter2.size() == 0) ? null : queryParameter2.get(0).getPmco();
				
				// 获取京东卡库存
				if(awardStock == null) {
					logger.warn("未获取到配置的京东卡库存扣减代码，不执行京东卡库存扣减操作");
					continue;  //待确认
				}
				
				if(sinoThreshold == null) {
					logger.warn("未获取到配置的京东卡库存阈值，不执行京东卡库存扣减操作");
					continue;  //待确认
				}
				int jdCardStock = Integer.parseInt(awardStock);
				if(jdCardStock<=0){
					logger.warn("京东卡库存不足！");
					continue;
				}	

				
				// 初始化变量
				List<SinoActAwardDto> exceptionList = sinoActAwardDao.queryExceptionActAwardInfo();
				String format = "yyyyMMddHHmmss";
				Long reqStreamId = Long.parseLong(DateUtil.date2Str(new Date(),format));
				Random random = new Random();
				Integer randomNum = 0;
				SinoActAwardInfoModel model = null;
				Map<String,Object> map = new HashMap<String,Object>();
				String reqStreamIdParam = "";
				SinoActAwardDto dto = new SinoActAwardDto();
				BusinessTypeDto businessTypeDto = serviceClient.queryBusinessType("42");
				String sendMsg = businessTypeDto == null ? null : businessTypeDto.getMsgTemplate();
				if(exceptionList == null || exceptionList.size() == 0) {
					logger.info("未查询到有异常活动奖品信息，等待下一次执行.");
				}  else {
					logger.info("查询到有 {} 条异常活动奖品信息，开始重新获取京东卡",exceptionList.size());
					for (int i = 0; i < exceptionList.size(); i++) {
						// added 2018/12/18 by hezk checked user has award
						SinoActAwardDto exceptionInfo = exceptionList.get(i);
						int exists = sinoActAwardDao.querySendAwardInfoByIdCardAndUserName(exceptionInfo);
						if(exists > 0) {
							logger.info("当前用户已经发送京东卡，不进行发送,用户信息为：{}",exceptionInfo);
							continue;
						}
						randomNum = random.nextInt(100000)+899999;
						reqStreamIdParam = String.valueOf(reqStreamId).concat(randomNum.toString());
						logger.info("当前异常用户为身份信息为：idcard:{},mobile:{},channel:{}",exceptionInfo.getAssignIdcard(),exceptionInfo.getAssignMobile(),exceptionInfo.getAssignChannel());
						try {
							map.put("appId", appId);
							map.put("param", DESUtils.encrypt(TRADE_REQUEST_PARAM.replace("{reqStreamIdParam}",
									reqStreamIdParam).replace("{agtPhone}", agtPhone)
									.replace("{tradePwd}", tradePwd)
									.replace("{productCode}", productCode),appKey));
						} catch (Exception e) {
							logger.error("获取京东卡 加密报文时捕获异常",e);
							logger.warn("当前用户数据:身份证为：{}，电话号码为：{}，因程序异常重发京东卡失败",exceptionInfo.getAssignIdcard(),exceptionInfo.getAssignMobile());
							continue;
						}
						logger.info("发出请求参数为:{}",map);
						try{
							model = doService(random, reqStreamId, map, tradeRequestUrl,0,reqStreamIdParam);
						}catch(Exception e) {
							logger.error("发出请求获取京东卡时，捕获异常，",e);
							logger.warn("当前用户数据:身份证为：{}，电话号码为：{}，因程序异常重发京东卡失败",exceptionInfo.getAssignIdcard(),exceptionInfo.getAssignMobile());
							continue;
						}
						if(model == null) {
							logger.warn("当前用户数据:身份证为：{}，电话号码为：{}，因程序异常重发京东卡失败",exceptionInfo.getAssignIdcard(),exceptionInfo.getAssignMobile());
							continue;
						}
						if(!"1000".equals(model.getStatus())) {
							logger.info("获取京东卡失败！订单号为：{}，用户身份证为：{}，用户电话为：{}",model.getReqStreamId(),exceptionInfo.getAssignIdcard(),exceptionInfo.getAssignMobile());
							continue;
						}
						try{
							logger.info("获取到的京东卡数据为：{}",model);
							// 插入数据
							dto.setReqStreamId(model.getReqStreamId());
							dto.setExpireDate(model.getData().get(0).getExpiretime());
							dto.setCardNum(model.getData().get(0).getCardNum());
							dto.setCardPwd(model.getData().get(0).getCardPwd());
							dto.setUpdatedUser(this.getClass().getSimpleName());
							dto.setAssignChannel(exceptionInfo.getAssignChannel());
							dto.setAssignIdcard(exceptionInfo.getAssignIdcard());
							dto.setAssignMobile(exceptionInfo.getAssignMobile());
							int insert = sinoActAwardDao.updateExceptionInfo(dto,exceptionInfo.getReqStreamId());
							if(insert <= 0) {
								logger.error("获取到的京东卡入库失败，入库数据： {}",dto);
								continue;
							}
							logger.info("京东卡入库成功！");
						}catch(Exception e){
							logger.error("获取到的京卡数据入库时捕获异常，",e);
							continue;
						}
						actParameterDao.updatePmcoByPmky(queryParameter1.get(0));		
						if(jdCardStock-1<= Integer.valueOf(sinoThreshold)){//当京东卡数量小于阀值时，发送邮件提示
							messageManager.sendJDStockMsgMail(jdCardStock-1, "18");
							logger.info("京东卡低于阀值，当前库存：{}",jdCardStock-1);
						}
						logger.info("开始发送短信,发送号码为：{},卡号为：{}",exceptionInfo.getAssignMobile(),model.getData().get(0).getCardNum());
						try{
							if(!StringHelper.isBlank(sendMsg)) {
								String content = sendMsg.replace("{account}", model.getData().get(0).getCardNum())
										.replace("{password}",DESUtils.decrypt(model.getData().get(0).getCardPwd(),decryptKey));
								DrainageUserInfoDto userInfoDto = drainageUserInfoDao.queryUserInfoByIdCard(exceptionInfo.getAssignIdcard());
								String userName = userInfoDto == null ? null : userInfoDto.getUserName();
								MsgSmsRecordDto msgRecord = new MsgSmsRecordDto();
								msgRecord.setMobile(dto.getAssignMobile());
								msgRecord.setMsgType(businessTypeDto.getBnsType());
								msgRecord.setMsgTypeDesc(businessTypeDto.getBnsTypeDesc());
								msgRecord.setContent(content);
								msgRecord.setMethod("A");
								msgRecord.setSendUser("system");
								msgRecord.setClientName(userName);
								MsgServiceMessageDto result = serviceClient.justSendMsg(msgRecord);
								if(result != null && "0000".equals(result.getReturnCode())) {
									logger.info("发送短信发送成功，开始回写状态");
									// 回写状态
									dto.setUpdatedUser(this.getClass().getSimpleName());
									int rewriteStatus = sinoActAwardDao.rewriteStatus(dto);
									if(rewriteStatus <= 0) {
										logger.error("回写状态失败，影响列为0，回写数据为：{}",dto);
									}
									logger.info("回写状态成功!");
								}
							} else {
								logger.warn("查询短信发送模板为空，不发送短信");
							}
						} catch(Exception e) {
							logger.error("获取到的京东卡入库或发送短信时或回写时捕获异常，",e);
							sinoActAwardDao.rewriteStatusError(dto);
						}
						
					}
				}
				Thread.sleep(60*15*1000);
			} catch (Exception e) {
				logger.error("未获取到京东卡的流水处理任务执行异常",e);
			}
		}
	}

	public SinoActAwardInfoModel doService(Random random,Long reqStreamId,Map<String,Object> param,String url,int count,String useReqStreamId) throws Exception {
		logger.info("开始发送请求至第三方平台，获取京东卡账号");
		if (5 < count) {
			logger.error("doService_递归达到5层, 结束执行");
			return null;
		}
		SinoActAwardInfoModel returnModel = null;
		ResponseEntity<SinoActAwardInfoModel> response = rest.exchange(url, HttpMethod.POST, getRequestEntity(param), new ParameterizedTypeReference<SinoActAwardInfoModel>() {
		});
		SinoActAwardInfoModel body = response.getBody();
		logger.info("收到第三方平台结果回应为：{}",body == null ? null : body);
		Integer integer = Integer.parseInt(body.getStatus());
		switch(integer) {
			case 1000 :
				returnModel = body;
				break;
			case 1001:
				logger.info("获取京东卡失败，第三方平台返回状态码为1001");
				returnModel = body;
				break;
			case 1003:
				logger.info("第三方平台返回状态码为1003，需调取查询接口");
				param.put("param", DESUtils.encrypt(QUERY_REQUEST_PARAM.replace("{reqStreamIdParam}",useReqStreamId).replace("{agtPhone}", agtPhone).replace("{tradePwd}",tradePwd), appKey));
				returnModel = doService(random,reqStreamId,param,queryRequestUrl,++count,useReqStreamId);
				break;
			case 1008:
				logger.info("获取京东卡失败，第三方平台返回状态码为1008");
				returnModel = body;
				break;
		}
		return returnModel;
	}
	
	protected <T> HttpEntity<T> getRequestEntity(T body) {
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
		headers.setContentType(mediaType);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));
		HttpEntity<T> requestEntity = new HttpEntity<T>(body, headers);
		return requestEntity;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		logger.info("Spring容器加载完毕");
		logger.info("启动重发送线程");
		this.initParam();
		logger.info("线程初始化完毕");
		String executeFlag = SpringUtil.getProperty("cmwa.wx.sino.resendthred.excute");
		if(!executeFlag.equalsIgnoreCase("ON")) {
			logger.info("本机线程执行配置为：{},不执行重发线程",executeFlag);
			return;
		} else {
			this.start();
			logger.info("线程启动完毕");
		}
	}

	
	
}
