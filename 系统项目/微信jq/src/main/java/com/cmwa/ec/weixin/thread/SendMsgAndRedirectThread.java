package com.cmwa.ec.weixin.thread;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.cmwa.ec.base.util.SpringUtil;
import com.cmwa.ec.message.facade.dto.BusinessTypeDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.wsadapter.DateUtil;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.dao.ParameterDao;
import com.cmwa.ec.weixin.dao.activity.ActParameterDao;
import com.cmwa.ec.weixin.dao.activity.DrainageUserInfoDao;
import com.cmwa.ec.weixin.dao.activity.SinoActAwardDao;
import com.cmwa.ec.weixin.dao.activity.SinoActPhoneCardDao;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.dto.SinoActAwardDto;
import com.cmwa.ec.weixin.dto.SinoActPhoneCardDto;
import com.cmwa.ec.weixin.dto.SinoUserInfoDto;
import com.cmwa.ec.weixin.manager.business.MessageManager;
import com.cmwa.ec.weixin.model.MobileMessagePushModel;
import com.cmwa.ec.weixin.model.SinoActAwardInfoModel;
import com.cmwa.ec.weixin.model.SinoActChargeInfoModel;
import com.cmwa.ec.weixin.util.DESUtils;
import com.cmwa.ec.weixin.util.StringHelper;

public class SendMsgAndRedirectThread implements Runnable {

	private ParameterDao parameterDao;
	
	private MessageServiceClient serviceClient;
	
	private SinoActAwardDao sinoAwardDao;
	
	private SinoActPhoneCardDao phoneCardDao;
	
	private String tradeRequestUrl;
	
	private String queryRequestUrl;
	
	private String sinoRedirectUrl;
	
	private String appKey;
	
	private String appId;
	
	private String decryptKey;
	
	
	private String secrect;
	
	private String agtPhone;
	
	private String tradePwd;
	
	
	/**
	 * 发送短信参数
	 */
	private MobileMessagePushModel param;
	
	private SinoUserInfoDto dto;
	
	private RestTemplate rest;

	private DrainageUserInfoDao drainageUserInfoDao;

	private QueryServiceClient queryServiceClient;
	
	private MessageManager messageManager;
	
	private ActParameterDao actParameterDao;
	
	//25 100 69 50
	private static final String TRADE_REQUEST_PARAM = "{\"agtPhone\": \"{agtPhone}\",\"productCode\": {productCode},\"reqStreamId\": \"{reqStreamIdParam}\",\"amount\": 1,\"tradePwd\": \"{tradePwd}\"}";
	
	private static final String QUERY_REQUEST_PARAM = "{\"agtPhone\": \"{agtPhone}\",\"reqStreamId\": \"{reqStreamIdParam}\",\"tradePwd\": \"{tradePwd}\"}";
	
	private static final String CHARGE_PARAM = "{\"agtPhone\":\"{agtPhone}\",\"reqStreamId\":\"{reqStreamId}\",\"chargeAddr\":\"{chargePhone}\",\"chargeType\":\"1\",\"chargeMoney\":{chargeMoney},\"tradePwd\": \"{tradePwd}\"}";
	
	private static final Logger logger = LoggerFactory.getLogger(SendMsgAndRedirectThread.class);
	
	
	public SendMsgAndRedirectThread(){
		
	}
	
	public SendMsgAndRedirectThread(MobileMessagePushModel param,
			SinoUserInfoDto dto, RestTemplate rest) {
		super();
		this.queryRequestUrl = SpringUtil.getProperty("cmwa.wx.sino.queryRequestUrl");
		this.tradeRequestUrl = SpringUtil.getProperty("cmwa.wx.sino.tradeRequestUrl");
		this.sinoRedirectUrl = SpringUtil.getProperty("cmwa.wx.sino.redirectUrl");
		this.appKey = SpringUtil.getProperty("cmwa.wx.sino.appKey");
		this.appId = SpringUtil.getProperty("cmwa.wx.sino.appId");
		this.decryptKey = SpringUtil.getProperty("cmwa.wx.sino.award.decryptKey");  
		this.secrect = SpringUtil.getProperty("cmwa.wx.sion.redirect.secret");
		this.agtPhone = SpringUtil.getProperty("cmwa.wx.sino.award.agtPhone");
		this.tradePwd = SpringUtil.getProperty("cmwa.wx.sino.award.tradePwd");
		this.serviceClient = SpringUtil.getBean(MessageServiceClient.class);
		this.sinoAwardDao = SpringUtil.getBean(SinoActAwardDao.class);
		this.phoneCardDao = SpringUtil.getBean(SinoActPhoneCardDao.class);
		this.parameterDao = SpringUtil.getBean(ParameterDao.class);
		this.drainageUserInfoDao = SpringUtil.getBean(DrainageUserInfoDao.class);
		this.queryServiceClient = SpringUtil.getBean(QueryServiceClient.class);
		this.messageManager = SpringUtil.getBean(MessageManager.class);
		this.actParameterDao = SpringUtil.getBean(ActParameterDao.class);
		this.param = param;
		this.dto = dto;
		this.rest = rest;
	}

	
	/**
	 * @return the serviceClient
	 */
	public MessageServiceClient getServiceClient() {
		return serviceClient;
	}

	/**
	 * @param serviceClient the serviceClient to set
	 */
	public void setServiceClient(MessageServiceClient serviceClient) {
		this.serviceClient = serviceClient;
	}

	/**
	 * @return the sinoAwardDao
	 */
	public SinoActAwardDao getSinoAwardDao() {
		return sinoAwardDao;
	}

	/**
	 * @param sinoAwardDao the sinoAwardDao to set
	 */
	public void setSinoAwardDao(SinoActAwardDao sinoAwardDao) {
		this.sinoAwardDao = sinoAwardDao;
	}

	/**
	 * @return the param
	 */
	public MobileMessagePushModel getParam() {
		return param;
	}

	/**
	 * @param param the param to set
	 */
	public void setParam(MobileMessagePushModel param) {
		this.param = param;
	}

	/**
	 * @return the dto
	 */
	public SinoUserInfoDto getDto() {
		return dto;
	}

	/**
	 * @param dto the dto to set
	 */
	public void setDto(SinoUserInfoDto dto) {
		this.dto = dto;
	}

	/**
	 * @return the rest
	 */
	public RestTemplate getRest() {
		return rest;
	}

	/**
	 * @param rest the rest to set
	 */
	public void setRest(RestTemplate rest) {
		this.rest = rest;
	}

	public ParameterDao getParameterDao() {
		return parameterDao;
	}

	public void setParameterDao(ParameterDao parameterDao) {
		this.parameterDao = parameterDao;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		try {
			// 先查询该客户是否已经领取过奖品
			int exists1 = sinoAwardDao.countAwardInfoByIdCard(param.getIdcard());
			int exists2 = phoneCardDao.countByIdCard(param.getIdcard());
			if(exists1 >= 1 || exists2 >= 1) {
				logger.info("当前客户已经领取过奖品,当前客户身份证为：{}",param.getIdcard());
				return;
			}
			logger.info("领取的奖品类型 {}", param.getAwardType());
			if("phoneCard".equals(param.getAwardType())) {
				assignPhoneCard(); // 电话充值卡
			}else {
				logger.info("开始执行异步发送短信任务,需要入奖品库的电话号码:{}身份证:{}",param.getMobile(),param.getIdcard());
				SinoActAwardDto dto = new SinoActAwardDto();
				SinoActAwardInfoModel errorModel = new SinoActAwardInfoModel();
				errorModel.setReqStreamId(UUID.randomUUID().toString().replace("-",""));
				// 获取awardStock 京东卡库存
				List<ParameterDto> queryParameter1 = queryServiceClient.queryParameter("SYSTEM", "SINOAWARDSTOCK", null, null);
				List<ParameterDto> queryParameter2 = queryServiceClient.queryParameter("SYSTEM", "SINOTHRESHOLD", null, null);
				String awardStock = (queryParameter1 == null || queryParameter1.size() == 0) ? null : queryParameter1.get(0).getPmco();
				String sinoThreshold = (queryParameter2 == null || queryParameter2.size() == 0) ? null : queryParameter2.get(0).getPmco();
				
				// 获取京东卡库存
				if(awardStock == null) {
					logger.warn("未获取到配置的京东卡库存扣减代码，不执行京东卡库存扣减操作");
					insertErrorInfo(dto, errorModel);
					return;  //待确认
				}
				
				if(sinoThreshold == null) {
					logger.warn("未获取到配置的京东卡库存阈值，不执行京东卡库存扣减操作");
					insertErrorInfo(dto, errorModel);
					return;  //待确认
				}
				int jdCardStock = Integer.parseInt(awardStock);
				if(jdCardStock<=0){
					logger.warn("京东卡库存不足！");
					insertErrorInfo(dto, errorModel);
					return;
				}	

			
				// 初始化变量
				String format = "yyyyMMddHHmmss";
				Long reqStreamId = Long.parseLong(DateUtil.date2Str(new Date(),format));
				Random random = new Random();
				Integer randomNum = random.nextInt(100000)+899999;
				SinoActAwardInfoModel model = null;
				
				Map<String,Object> map = new HashMap<String,Object>();
				String reqStreamIdParam = String.valueOf(reqStreamId).concat(randomNum.toString());
				
				// 获取productCode
				List<ParameterDto> queryParameter = queryServiceClient.queryParameter("SYSTEM", "SINOPRODUCTCODE", null, null);
				String productCode = (queryParameter == null || queryParameter.size() == 0) ? null : queryParameter.get(0).getPmco();
				boolean getCardfailed = false;
				// 获取京东卡
				if(productCode == null) {
					logger.warn("未获取到配置的产品代码，不执行获取京东卡操作");
					insertErrorInfo(dto, errorModel);
					getCardfailed = true;
				}
				try {
					map.put("appId", appId);
					map.put("param", DESUtils.encrypt(TRADE_REQUEST_PARAM.replace("{reqStreamIdParam}",reqStreamIdParam).replace("{agtPhone}", agtPhone).replace("{tradePwd}", tradePwd).replace("{productCode}", productCode),appKey));
					logger.info("发出请求参数为:{}",map);
					model = doService(random, reqStreamId, map, tradeRequestUrl,0,reqStreamIdParam);
				} catch (Exception e) {
					logger.error("发出请求获取京东卡时，捕获异常，",e);
					logger.warn("当前实名用户数据为身份证为：{}，电话号码为：{}，用户名为：{}，因程序异常导致未发送京东卡",this.dto.getIdCard(),dto.getAssignMobile(),this.dto.getOwnerName());
					insertErrorInfo(dto, errorModel);
					getCardfailed = true;
				}
				if(model == null && getCardfailed == false) {
					logger.warn("当前实名用户数据为身份证为：{}，电话号码为：{}，用户名为：{}，因未获取到京东卡信息未发送京东卡",this.dto.getIdCard(),dto.getAssignMobile(),this.dto.getOwnerName());
					insertErrorInfo(dto, errorModel);
					getCardfailed = true;
				}
				// 如果获取京东卡失败则将卡号卡密留为空入库由另一线程轮询重复获取京东卡
				if(getCardfailed == false && !"1000".equals(model.getStatus())) {
					insertErrorInfo(dto, errorModel);
					getCardfailed = true;
				}
				
				if(!getCardfailed) {
					// 京东卡入库
					try{
						logger.info("获取到的京东卡数据为：{}",model);
						// 插入数据
						dto.setReqStreamId(model.getReqStreamId());
						dto.setExpireDate(model.getData().get(0).getExpiretime());
						dto.setCardNum(model.getData().get(0).getCardNum());
						dto.setCardPwd(model.getData().get(0).getCardPwd());
						dto.setAssignOpenid(param.getOpenid());
						dto.setCreatedUser(this.getClass().getSimpleName());
						dto.setUpdatedUser(this.getClass().getSimpleName());
						dto.setAssignChannel(this.dto.getAppId());
						dto.setAssignIdcard(param.getIdcard());
						dto.setAssignMobile(param.getMobile());
						int insert = sinoAwardDao.insert(dto);
						if(insert <= 0) {
							logger.error("获取到的京东卡入库失败，入库数据： {}",dto);
							return;
						}
						logger.info("京东卡入库成功！");			
					}catch(Exception e){
						logger.error("获取到的京卡数据入库时捕获异常，",e);
						return;
					}
					actParameterDao.updatePmcoByPmky(queryParameter1.get(0));
					//扣减后库存小于阀值就发送邮件
					if(jdCardStock-1<= Integer.valueOf(sinoThreshold)){//当京东卡数量小于20时，发送邮件提示
						messageManager.sendJDStockMsgMail(jdCardStock-1, "18");
						logger.info("京东卡低于阀值，当前库存：{}",jdCardStock-1);
					}
					
					// 发送短信回写
					logger.info("开始发送短信,发送号码为：{},卡号为：{}",param.getMobile(),model.getData().get(0).getCardNum());
					try{
						BusinessTypeDto businessTypeDto = serviceClient.queryBusinessType("42");
						String sendMsg = businessTypeDto == null ? null : businessTypeDto.getMsgTemplate();
						if(!StringHelper.isBlank(sendMsg)) {
							sendMsg = sendMsg.replace("{account}", model.getData().get(0).getCardNum())
									.replace("{password}",DESUtils.decrypt(model.getData().get(0).getCardPwd(),decryptKey));
							MsgSmsRecordDto msgRecord = getMsgSmsRecord(businessTypeDto, sendMsg);
							MsgServiceMessageDto result = serviceClient.justSendMsg(msgRecord);
							if(result != null && "0000".equals(result.getReturnCode())) {
								logger.info("发送短信发送成功，开始回写状态");
								// 回写状态
								dto.setUpdatedUser(this.getClass().getSimpleName());
								int rewriteStatus = sinoAwardDao.rewriteStatus(dto);
								if(rewriteStatus <= 0) {
									logger.error("回写状态失败，影响列为0，回写数据为：{}",dto);
								}
								logger.info("回写状态成功，开始回调华安保险接口");
							}
						} else {
							logger.warn("查询短信发送模板为空,不发送短信");
						}
					} catch(Exception e) {
						logger.error("获取到的京东卡入库或发送短信时或回写时捕获异常，",e);
						sinoAwardDao.rewriteStatusError(dto);
					}
				}
			}
			// 回调华安接口
			try{
				SinoSendDataInfo sendData = prepareSendData(this.dto);
				sendData.setTimestamp(System.currentTimeMillis());
				sendData.setSign(sign(sendData));
				logger.info("回调华安保险接口生成的签名为：{}",sendData.getSign());
				
				UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(sinoRedirectUrl);
				builder.queryParam("appId",sendData.getAppId());
				builder.queryParam("haUserId",sendData.getHaUserId());
				builder.queryParam("idCard",sendData.getIdCard());
				builder.queryParam("plateNo",sendData.getPlateNo());
				builder.queryParam("ownerName",sendData.getOwnerName());
				builder.queryParam("timestamp",sendData.getTimestamp());
				builder.queryParam("sign", sendData.getSign());
				
				String realRequestUrl = builder.build().toString();
				logger.info("回调url生成为：{}",realRequestUrl);
				ResponseEntity<SinoRedirectInfo> redirectResult = rest.exchange(realRequestUrl, HttpMethod.GET, getOnlySetCharSetRequestEntity(), new ParameterizedTypeReference<SinoRedirectInfo>() {
				});
				
				if(redirectResult.getStatusCode().equals(HttpStatus.OK)) {
					SinoRedirectInfo redirectInfo = redirectResult.getBody();
					if(redirectInfo == null) {
						logger.warn("华安保险接口返回数据为空！");
						return;
					}
					logger.info("华安保险接口回调信息为：{}",redirectInfo);
					if(!"success".equals(redirectInfo.getStatus())) {
						 logger.warn("回调华安保险接口返回状态为：{},提示信息为：{},回调失败",redirectInfo.getStatus(),redirectInfo.getMsg());
						 int updateRecord = drainageUserInfoDao.updateRedirectInfo(this.dto.getIdCard(),this.dto.getOwnerName(),this.dto.getAppId(),"F",redirectInfo.getMsg(),this.getClass().getSimpleName());
						 logger.info("回调之后修改用户回调状态影响列为 {},参数为：idCard:{},ownerName:{},appId:{},status:{},msg:{}",updateRecord,this.dto.getIdCard(),this.dto.getOwnerName(),this.dto.getAppId(),"F",redirectInfo.getMsg());
					} else {
						logger.info("回调华安保险接口成功，返回状态为：{},提示信息为：{}",redirectInfo.getStatus(),redirectInfo.getMsg());
						int updateRecord = drainageUserInfoDao.updateRedirectInfo(this.dto.getIdCard(),this.dto.getOwnerName(),this.dto.getAppId(),"Y",redirectInfo.getMsg(),this.getClass().getSimpleName());
						logger.info("回调之后修改用户回调状态影响列为 {},参数为：idCard:{},ownerName:{},appId:{},status:{},msg:{}",updateRecord,this.dto.getIdCard(),this.dto.getOwnerName(),this.dto.getAppId(),"Y",redirectInfo.getMsg());
					}
				} else {
					logger.warn("回调华安保险接口http请求失败， http状态码为：{}",redirectResult.getStatusCode().toString());
				}
			}catch(Exception e) {
				logger.error("回调华安保险接口时捕获异常，",e);
				drainageUserInfoDao.updateRedirectInfo(this.dto.getIdCard(),this.dto.getOwnerName(),this.dto.getAppId(),"E",e.getMessage(),this.getClass().getSimpleName());
				return;
			}
		} catch (Exception e) {
			logger.error("华安回掉异步线程处理任务异常",e);
		}
	}

	private void assignPhoneCard() {
		logger.info("客户领取电话充值卡奖品,身份证:{}电话号码:{}",param.getIdcard(),param.getMobile());
		try {
			Long dateTime = Long.parseLong(DateUtil.date2Str(new Date(),"yyyyMMddHHmmss"));
			Random random = new Random();
			Integer randomNum = random.nextInt(100000)+899999;
			String reqStreamId = String.valueOf(dateTime).concat(randomNum.toString());
			SinoActChargeInfoModel model = doCharge(reqStreamId, param.getMobile());
			logger.info("返回结果 responseBody:{}", model);
			if(model == null) {
				logger.warn("请求电话充值卡接口没有返回结果，请求失败");
				model = new SinoActChargeInfoModel();
				model.getData().setReqStreamId(reqStreamId);
				insertSinoActPhoneCard(model, "F");
				return;
			}
			String status = model.getStatus();
			if("1000".equals(status)) {
				logger.info("电话卡充值成功，给客户发送短信通知");
				insertSinoActPhoneCard(model, "S");
				BusinessTypeDto businessTypeDto = serviceClient.queryBusinessType("41");
				String sendMsg = businessTypeDto == null ? null : businessTypeDto.getMsgTemplate();
				List<ParameterDto> sinoChargeMoney = queryServiceClient.queryParameter("SYSTEM", "SINOCHARGEMONEY", null, null);
				String chargeMoney = (sinoChargeMoney == null || sinoChargeMoney.isEmpty()) ? null : sinoChargeMoney.get(0).getPmco();
				if(StringHelper.isBlank(sendMsg) || StringHelper.isBlank(chargeMoney)) {
					logger.warn("短信或者充值金额配置内容为空，不发送短信通知");
					return;
				}
				sendMsg = sendMsg.replace("{chargeMoney}", chargeMoney);
				MsgSmsRecordDto msgRecord = getMsgSmsRecord(businessTypeDto, sendMsg);
				MsgServiceMessageDto msgResult = serviceClient.justSendMsg(msgRecord);
				if(msgResult != null && "0000".equals(msgResult.getReturnCode())) {
					logger.info("短信发送成功");
				}else {
					logger.warn("短信发送失败 moblie:{}", param.getMobile());
				}
			}else if("1003".equals(status) || "1011".equals(status)
					|| "1012".equals(status) || "1014".equals(status)) {
				logger.info("电话卡充值处理中");
				insertSinoActPhoneCard(model, "I");
			}else {
				logger.info("电话卡充值失败");
				insertSinoActPhoneCard(model, "F");
			}
		} catch (Exception e) {
			logger.error("客户领取电话充值卡奖品异常", e);
		}
	}
	
	private void insertErrorInfo(SinoActAwardDto dto,
			SinoActAwardInfoModel model) {
		try{
			dto.setReqStreamId(model.getReqStreamId());
			dto.setCreatedUser(this.getClass().getSimpleName());
			dto.setUpdatedUser(this.getClass().getSimpleName());
			dto.setAssignChannel(this.dto.getAppId());
			dto.setAssignIdcard(param.getIdcard());
			dto.setAssignMobile(param.getMobile());
			dto.setAssignChannel(this.dto.getAppId());
			dto.setAssignOpenid(param.getOpenid());
			dto.setSendStatus("E");
			int insert = sinoAwardDao.insert(dto);
			if(insert >= 1) {
				logger.info("未获取京东卡用户信息入库成功！订单号为：{}",dto.getReqStreamId());
			} else {
				logger.warn("未获取京东卡用户信息入库失败！身份证为：{}，电话号码为：{},用户名为：{}",this.dto.getOwnerName(),dto.getAssignMobile(),this.dto.getOwnerName());
			}
		}catch(Exception e) {
			logger.error("未获取京东卡用户信息入库时捕获异常：",e);
		}
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
	
	protected <T> HttpEntity<T> getOnlySetCharSetRequestEntity() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<T> requestEntity = new HttpEntity<T>(headers);
		return requestEntity;
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
				param.put("param", DESUtils.encrypt(QUERY_REQUEST_PARAM.replace("{reqStreamIdParam}",useReqStreamId).replace("{agtPhone}", agtPhone).replace("{tradePwd}", tradePwd), appKey));
				returnModel = doService(random,reqStreamId,param,queryRequestUrl,++count,useReqStreamId);
				break;
			case 1008:
				logger.info("获取京东卡失败，第三方平台返回状态码为1008");
				returnModel = body;
				break;
		}
		return returnModel;
	}
	
	public String sign(SinoSendDataInfo data) throws Exception { 
		String charSet = "UTF-8";
		StringBuffer signBuffer = new StringBuffer();
		signBuffer.append(URLEncoder.encode(data.getPlateNo(),charSet));
		signBuffer.append(URLEncoder.encode(data.getIdCard(),charSet));
		signBuffer.append(URLEncoder.encode(data.getOwnerName(),charSet));
		signBuffer.append(URLEncoder.encode(data.getHaUserId(),charSet));
		signBuffer.append(URLEncoder.encode(""+data.getTimestamp(),charSet));
		signBuffer.append(this.secrect);
		MessageDigest digest = MessageDigest.getInstance("MD5");
		try {
			digest.update(signBuffer.toString().getBytes(charSet));
		} catch (UnsupportedEncodingException e) {
			logger.error("生成签名加密时捕获异常",e);
			return "";
		}
		return DESUtils.bytes2Hex(digest.digest());
	}
	
	private SinoSendDataInfo prepareSendData(SinoUserInfoDto data){
		SinoSendDataInfo result = new SinoSendDataInfo();
		result.setAppId(data.getAppId());
		result.setHaUserId(data.getSinoUserId());
		result.setIdCard(data.getIdCard());
		result.setOwnerName(data.getOwnerName());
		result.setPlateNo(data.getPlateNo());
		result.setSign(data.getSign());
		result.setTimestamp(data.getTimestamp());
		return result;
	}
	
	private SinoActChargeInfoModel doCharge(String reqStreamId, String chargePhone) {
		logger.info("准备请求电话充值卡接口 请求流水号:{},充值电话:{}", reqStreamId, chargePhone);
		try {
			List<ParameterDto> sinoChargeUrl = queryServiceClient.queryParameter("SYSTEM", "SINOCHARGEURL", null, null);
			String chargeUrl = (sinoChargeUrl == null || sinoChargeUrl.isEmpty()) ? null : sinoChargeUrl.get(0).getPmco();
			List<ParameterDto> sinoChargeMoney = queryServiceClient.queryParameter("SYSTEM", "SINOCHARGEMONEY", null, null);
			String chargeMoney = (sinoChargeMoney == null || sinoChargeMoney.isEmpty()) ? null : sinoChargeMoney.get(0).getPmco();
			if(StringHelper.isBlank(chargeUrl) || StringHelper.isBlank(chargeMoney)) {
				logger.warn("充值URL或者充值金额配置内容为空，不请求充值接口");
				return null;
			}
			String chargeParam = CHARGE_PARAM.replace("{agtPhone}", agtPhone).replace("{reqStreamId}", reqStreamId)
					.replace("{chargePhone}", chargePhone).replace("{chargeMoney}", chargeMoney).replace("{tradePwd}", tradePwd);
			logger.info("请求参数 chargeParam:{}", chargeParam);
			chargeParam = DESUtils.encrypt(chargeParam, appKey);
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("appId", appId);
			map.put("param", chargeParam);
			ResponseEntity<SinoActChargeInfoModel> response = rest.exchange(chargeUrl, HttpMethod.POST, getRequestEntity(map), 
					new ParameterizedTypeReference<SinoActChargeInfoModel>() {});
			return response.getBody();
		} catch (Exception e) {
			logger.error("请求电话充值卡接口时异常 请求流水号:{},充值电话:{}", reqStreamId, chargePhone, e);
		}
		return null;
	}
	
	private void insertSinoActPhoneCard(SinoActChargeInfoModel model, String assignStatus) {
		try {
			SinoActPhoneCardDto dto = new SinoActPhoneCardDto();
			dto.setReqStreamId(model.getData().getReqStreamId());
			dto.setOrderNo(model.getData().getOrderNo());
			dto.setAssignMobile(param.getMobile());
			dto.setIdCard(param.getIdcard());
			dto.setAssignStatus(assignStatus);
			dto.setAssignDate(model.getData().getApplyTime());
			dto.setCreatedUser(this.getClass().getSimpleName());
			dto.setUpdatedUser(this.getClass().getSimpleName());
			dto.setAssignChannel(this.dto.getAppId());
			dto.setOpenid(param.getOpenid());
			logger.info("准备将客户领取电话充值卡信息入库 info:{}", dto);
			phoneCardDao.insertRecord(dto);
			logger.info("客户领取电话充值卡信息入库成功"); 
		} catch (Exception e) {
			logger.error("客户领取电话充值卡信息入库异常", e);
		}
	}
	
	private MsgSmsRecordDto getMsgSmsRecord(BusinessTypeDto businessTypeDto, String msg) {
		MsgSmsRecordDto msgRecord = new MsgSmsRecordDto();
		msgRecord.setMobile(param.getMobile());
		msgRecord.setMsgType(businessTypeDto.getBnsType());
		msgRecord.setMsgTypeDesc(businessTypeDto.getBnsTypeDesc());
		msgRecord.setContent(msg);
		msgRecord.setMethod("A");
		msgRecord.setSendUser("system");
		msgRecord.setClientName(dto.getOwnerName());
		return msgRecord;
	}
	
}

class SinoRedirectInfo {
	
	/**
	 * success | error
	 */
	private String status;
	
	/**
	 * 提示信息
	 */
	private String msg;
	
	/**
	 * md5加密字符串
	 */
	@JsonIgnore
	private String md5str;
	
	/**
	 * 签名字符串 
	 */
	@JsonIgnore
	private String md5strsign;

	/**
	 * 
	 */
	@JsonIgnore
	private String url;
	
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the md5str
	 */
	public String getMd5str() {
		return md5str;
	}

	/**
	 * @param md5str the md5str to set
	 */
	public void setMd5str(String md5str) {
		this.md5str = md5str;
	}

	/**
	 * @return the md5strsign
	 */
	public String getMd5strsign() {
		return md5strsign;
	}

	/**
	 * @param md5strsign the md5strsign to set
	 */
	public void setMd5strsign(String md5strsign) {
		this.md5strsign = md5strsign;
	}

	
	
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "SinoRedirectInfo [status=" + status + ", msg=" + msg
				+ ", md5str=" + md5str + ", md5strsign=" + md5strsign
				+ ", url=" + url + "]";
	}

}

class SinoSendDataInfo {
	
	private String appId;
	
	private String plateNo;
	
	private String idCard;
	
	private String ownerName;
	
	private String haUserId;
	
	private long timestamp;
	
	private String sign;

	/**
	 * @return the appId
	 */
	public String getAppId() {
		return appId;
	}

	/**
	 * @param appId the appId to set
	 */
	public void setAppId(String appId) {
		this.appId = appId;
	}

	/**
	 * @return the plateNo
	 */
	public String getPlateNo() {
		return plateNo;
	}

	/**
	 * @param plateNo the plateNo to set
	 */
	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	/**
	 * @return the idCard
	 */
	public String getIdCard() {
		return idCard;
	}

	/**
	 * @param idCard the idCard to set
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	/**
	 * @return the ownerName
	 */
	public String getOwnerName() {
		return ownerName;
	}

	/**
	 * @param ownerName the ownerName to set
	 */
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	/**
	 * @return the haUserId
	 */
	public String getHaUserId() {
		return haUserId;
	}

	/**
	 * @param haUserId the haUserId to set
	 */
	public void setHaUserId(String haUserId) {
		this.haUserId = haUserId;
	}

	/**
	 * @return the timestamp
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @param timestamp the timestamp to set
	 */
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the sign
	 */
	public String getSign() {
		return sign;
	}

	/**
	 * @param sign the sign to set
	 */
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	 
	
}