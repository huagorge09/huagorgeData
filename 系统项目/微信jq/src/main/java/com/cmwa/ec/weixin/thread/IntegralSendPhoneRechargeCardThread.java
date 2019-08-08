package com.cmwa.ec.weixin.thread;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.model.MsgResult;
import com.cmwa.ec.message.facade.wsadapter.DateUtil;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.model.user.IntegralAwardUserInfoDTO;
import com.cmwa.ec.trade.facade.model.TradeResult;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.client.TradeServiceClient;
import com.cmwa.ec.weixin.dao.activity.SinoActPhoneCardDao;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.dto.SinoActPhoneCardDto;
import com.cmwa.ec.weixin.model.SinoActChargeInfoModel;
import com.cmwa.ec.weixin.util.DESUtils;

/**
 * 积分兑换话费卡轮询发放线程
 *
 * @author ex-chent@cmfchina.com
 */
public class IntegralSendPhoneRechargeCardThread extends Thread implements InitializingBean {

    /**
     * slf4jLogger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(IntegralSendPhoneRechargeCardThread.class);
    /**
     *
     */
    private TradeServiceClient tradeServiceClient;
    /**
     *
     */
    private QueryServiceClient queryServiceClient;
    /**
     *
     */
    private MessageServiceClient serviceClient;
    /**
     * phoneCardDao
     */
    private SinoActPhoneCardDao phoneCardDao;
    /**
     * 读取线程执行开关
     */
    public final boolean executeFlag = "ON".equals(SpringUtil.getProperty("cmwa.wx.sino.resendthred.excute"));
    /**
     * 向第三方请求的参数
     */
    private HashMap<String, Object> requestParameterMap;
    /**
     *
     */
    private Map<String, String> callChargeParameter;
    /**
     * 短信模板
     */
    private BusinessTypeDto callChargeCardTemplate;
    /**
     * 话费卡发送成功操作类型编号
     */
    private int callChargeSuccessOperation;
    /**
     *
     */
    private String callChargeCardMsg;
    /**
     * 充值话费请求参数
     */
    private String chargeParam;
    /**
     * RestTemplate
     */
    private RestTemplate rest;
    /**
     * 第三方平台账号
     */
    private String agtPhone;
    /**
     * tradePassword
     */
    private String tradePwd;
    /**
     * appKey
     */
    private String appKey;
    /**
     *
     */
    private Random random;
    /**
     * appId
     */
    private String appId;


    @Override
    public void afterPropertiesSet() {
        String ipAddress = "unknown";
        try {
            ipAddress = InetAddress.getLocalHost().toString();
        } catch (UnknownHostException omg) {
            LOGGER.error("afterPropertiesSet_积分兑换的奖品轮询发放线程中获取主机地址异常_", omg);
        }
        // 判断线程执行开关
        if (executeFlag) {
            LOGGER.info("afterPropertiesSet_本机IP地址为: {}, 本机线程执行配置为: ON, 将执行积分兑换轮询发放线程", ipAddress);
            // 加载所需参数
            try {
                loadParameter();
            } catch (Exception omg) {
                LOGGER.error("afterPropertiesSet_加载所需参数异常, 线程将不会启动_", omg);
                return;
            }
            try {
                // 启动线程
                this.start();
            } catch (Exception omg) {
                LOGGER.error("a Thread exception test...", omg);
            }
        } else {
            LOGGER.info("afterPropertiesSet_本机IP地址为: {}, 本机线程执行配置为: OFF, 不执行积分兑换轮询发放线程", ipAddress);
        }
        LOGGER.info("testThread...");
    }

    private void loadParameter() {
        tradeServiceClient = SpringUtil.getBean(TradeServiceClient.class);
        queryServiceClient = SpringUtil.getBean(QueryServiceClient.class);
        serviceClient = SpringUtil.getBean(MessageServiceClient.class);
        phoneCardDao = SpringUtil.getBean(SinoActPhoneCardDao.class);
        // 充值话费请求参数
        chargeParam = "{\"agtPhone\":\"{agtPhone}\",\"reqStreamId\":\"{reqStreamId}\",\"chargeAddr\":" +
            "\"{chargePhone}\",\"chargeType\":\"1\",\"chargeMoney\":{chargeMoney},\"tradePwd\": \"{tradePwd}\"}";
        // 第三方平台账号
        agtPhone = SpringUtil.getProperty("cmwa.wx.sino.award.agtPhone");
        // tradePassword
        tradePwd = SpringUtil.getProperty("cmwa.wx.sino.award.tradePwd");
        // appKey
        appKey = SpringUtil.getProperty("cmwa.wx.sino.appKey");
        // appId
        appId = SpringUtil.getProperty("cmwa.wx.sino.appId");
        requestParameterMap = new HashMap<String, Object>(3);
        // put请求参数appId
        requestParameterMap.put("appId", appId);
        // 话费卡发送成功操作类型编号
        callChargeSuccessOperation = 100006;
        // RestTemplate
        rest = new RestTemplate();
        random = new Random();
        // 初始化发送话费卡短信模板
        callChargeCardTemplate = serviceClient.queryBusinessType("57");
        if (callChargeCardTemplate != null) {
            callChargeCardMsg = callChargeCardTemplate.getMsgTemplate();
        } else {
            LOGGER.error("loadParameter_初始化发送话费卡短信模板失败");
        }
    }

    @Override
    public void run() {
        while (true) {
        	try {
        		try {
                    Thread.sleep(120000);
                } catch (InterruptedException omg) {
                    LOGGER.error("afterPropertiesSet_积分兑话费轮询发放线程睡觉的时候做噩梦了_", omg);
                }
                // 加载MECC配置的系统参数
                callChargeParameter = getCallChargeCardParameter();
                if (callChargeParameter == null) {
                    LOGGER.warn("run_系统参数加载失败");
                    continue;
                }
                // 获取需发放充值卡的用户
                QueryMessageDto queryResp = queryServiceClient.listNeedSendCallChargeCard();
                if (null == queryResp.getData()) {
                    LOGGER.warn("run_读取需发放充值卡的用户失败");
                    continue;
                }
                @SuppressWarnings("unchecked") 
                List<IntegralAwardUserInfoDTO> sendList = (List<IntegralAwardUserInfoDTO>) queryResp.getData();
                LOGGER.info("run_查询到话费充值待处理数据量: " + sendList.size());

                for (IntegralAwardUserInfoDTO userInfo : sendList) {
                    LOGGER.info("run_发放充值卡开始_info: {}", userInfo);
                    switch (recharge(userInfo, callChargeParameter)) {
                        case 1000:
                            // 发短信通知用户
                            boolean sendSuccess = sendCallChargeSuccessMsg(userInfo.getPhoneNumber(), userInfo.getUserName());
                            if (sendSuccess) {
                                LOGGER.info("run_短信发送成功");
                            } else {
                                LOGGER.warn("run_run_短信发送失败");
                            }
                        case 1011:
                            if (writeBackStatus(userInfo.getSerialNo())) {
                                LOGGER.info("run_发放话费卡结束_info: {}", userInfo);
                            } else {
                                // 回写状态失败
                                LOGGER.warn("run_发放话费卡回写状态失败!_info: {}", userInfo);
                            }
                            break;
                        default:
                    }
                }
			} catch (Exception e) {
				LOGGER.error("开始执行MGM积分兑换电话卡在线充值处理轮询线程任务", e);
			}
        }
    }

    /**
     * 充值话费
     *
     * @param userInfo
     * @param callChargeParameter
     * @return
     */
    private int recharge(IntegralAwardUserInfoDTO userInfo, Map<String, String> callChargeParameter) {
        int rechargeStatus = 9999;
        HashMap<String, Object> requestMap = null;
        try {
            // 获取请求参数
            requestMap = buildRequestParameters(userInfo.getPhoneNumber(),
                callChargeParameter.get("chargeMoney"));
        } catch (Exception omg) {
            LOGGER.error("recharge_获取请求参数时加密报文异常_userInfo: {}, callChargeParameter: {}",
                userInfo, callChargeParameter, omg);
        }
        if (null != requestMap) {
            try {
                // 调用第三方进行充值
                rechargeStatus = getCallChargeFromThirdParty(requestMap, callChargeParameter.get("chargeUrl"),
                    userInfo.getPhoneNumber(), userInfo.getIdNo());
            } catch (Exception omg) {
                LOGGER.error("recharge_调用第三方进行话费充值异常, 当前用户信息: {}, requestMap: {}, rechargeParameter: {}",
                    userInfo, requestMap, callChargeParameter, omg);
            }
            // 充值未立即成功
            if (rechargeStatus == 1000) {
                 LOGGER.info("recharge_充值成功");
            } else {
                LOGGER.info("recharge_充值未立即成功, 将由另一条线程检查并更新状态_info: {}, requestMap: {}", userInfo, requestMap);
            }
        }
        return rechargeStatus;
    }

    /**
     * 发送话费卡充值成功通知
     *
     * @param phoneNumber
     * @param userName
     * @return
     */
    private boolean sendCallChargeSuccessMsg(String phoneNumber, String userName) {
        // 金额, 客服电话
        String content = callChargeCardMsg
            .replace("{amount}", callChargeParameter.get("chargeMoney"))
            .replace("{mobile}", "0755-23988886");

        MsgSmsRecordDto msgRecord = new MsgSmsRecordDto();
        msgRecord.setMsgTypeDesc(callChargeCardTemplate.getBnsTypeDesc());
        msgRecord.setMsgType(callChargeCardTemplate.getBnsType());
        msgRecord.setClientName(userName);
        msgRecord.setMobile(phoneNumber);
        msgRecord.setSendUser("system");
        msgRecord.setContent(content);
        msgRecord.setMethod("A");
        MsgResult msgResult = serviceClient.sendMarketMsg(msgRecord);
        return msgResult != null && msgResult.isSuccess();
    }

    /**
     * 回写状态
     *
     * @param serialNo
     */
    private boolean writeBackStatus(String serialNo) {
        boolean flag = false;
        // 更改积分流水表此条流水记录为已发送
        TradeResult<?> tradeResult = tradeServiceClient.updateIntegralDetailOperation(serialNo, callChargeSuccessOperation);
        if (tradeResult.isSuccess()) {
            LOGGER.info("sendCallChargeCard_流水记录修改成功_serialNo: {}, operationId: {}",
                serialNo, callChargeSuccessOperation);
            flag = true;
        } else {
            LOGGER.error("sendCallChargeCard_流水记录修改失败_serialNo: {}, operationId: {}",
                serialNo, callChargeSuccessOperation);
        }
        return flag;
    }

    /**
     * 调用第三方平台充值话费
     *
     * @param
     * @return
     */
    private int getCallChargeFromThirdParty(Map<String, Object> requestMap, String requestUrl, String phoneNumber,
                                                String idNo) throws Exception {
        // 请求第三方
        ResponseEntity<SinoActChargeInfoModel> response = rest.exchange(requestUrl, HttpMethod.POST,
            getRequestEntity(requestMap), new ParameterizedTypeReference<SinoActChargeInfoModel>() {});
        int rechargeStatus = 9999;
        SinoActChargeInfoModel body;

        if (response != null) {
            body = response.getBody();
            LOGGER.info("getCallChargeFromThirdParty_收到第三方平台结果回应为: {}, requestMap: {}", body, requestMap);
            int status = Integer.parseInt(body.getStatus());
            switch (status) {
                case 1000:
                    // success
                    // 将此状态入库
                    insertSinoActPhoneCard(body, "S", phoneNumber, idNo);
                    rechargeStatus = status;
                    LOGGER.info("getCallChargeFromThirdParty_充值话费成功，第三方平台返回码1000, responseBody: {}", body);
                    break;
                case 1003:
                case 1011:
                case 1012:
                case 1014:
                    // 将此状态入库
                    insertSinoActPhoneCard(body, "I", phoneNumber, idNo);
                    rechargeStatus = 1011;
                    LOGGER.info("getCallChargeFromThirdParty_电话卡充值处理中, responseBody: {}", body);
                    break;
                default:
                    // 将此状态入库
                    insertSinoActPhoneCard(body, "F", phoneNumber, idNo);
                    rechargeStatus = status;
                    LOGGER.info("getCallChargeFromThirdParty_电话卡充值失败, responseBody: {}", body);
            }
        } else {
            LOGGER.error("getCallChargeFromThirdParty_请求第三方平台无响应, response为null, requestUrl: {}, requestMap: {}",
                requestUrl, requestMap);
        }
        return rechargeStatus;
    }

    /**
     * 话费充值信息入库
     *
     * @param cardInfo
     * @param assignStatus
     * @param phoneNumber
     * @param idCard
     */
    private void insertSinoActPhoneCard(SinoActChargeInfoModel cardInfo, String assignStatus, String phoneNumber,
                                        String idCard) {
        SinoActPhoneCardDto dto = new SinoActPhoneCardDto();
        try {
            dto.setReqStreamId(cardInfo.getData().getReqStreamId());
            dto.setOrderNo(cardInfo.getData().getOrderNo());
            dto.setAssignMobile(phoneNumber);
            dto.setIdCard(idCard);
            dto.setAssignStatus(assignStatus);
            dto.setAssignDate(cardInfo.getData().getApplyTime());
            dto.setCreatedUser("IntegralPhoneRechargeThread");
            dto.setUpdatedUser("IntegralPhoneRechargeThread");
            dto.setAssignChannel("Integral");
            dto.setQueryStatus(cardInfo.getStatus());
            dto.setQueryMsg(cardInfo.getMsg());
            LOGGER.info("insertSinoActPhoneCard_准备将客户领取电话充值卡信息入库_SinoActPhoneCardDto: {}, cardInfo: {}, " +
                "assignStatus: {}, phoneNumber: {}, idCard: {}", dto, cardInfo, assignStatus, phoneNumber, idCard);
            phoneCardDao.insertRecord(dto);
            LOGGER.info("insertSinoActPhoneCard_客户领取电话充值卡信息入库成功");
        } catch (Exception omg) {
            LOGGER.error("insertSinoActPhoneCard_客户领取电话充值卡信息入库异常_SinoActPhoneCardDto: {}, cardInfo: {}, " +
                "assignStatus: {}, phoneNumber: {}, idCard: {}", dto, cardInfo, assignStatus, phoneNumber, idCard, omg);
        }
    }

    /**
     * 构建话费卡充值POST请求参数
     *
     * @return
     */
    private HashMap<String, Object> buildRequestParameters(String chargePhone, String chargeMoney) throws Exception {
        // 清空Map
        String chargeParam = this.chargeParam
            .replace("{agtPhone}", agtPhone)
            .replace("{reqStreamId}", getRequestStreamId())
            .replace("{chargePhone}", chargePhone)
            .replace("{chargeMoney}", chargeMoney)
            .replace("{tradePwd}", tradePwd);
        LOGGER.info("请求参数 chargeParam:{}", chargeParam);
        String encryptedRequest = DESUtils.encrypt(chargeParam, appKey);
        requestParameterMap.put("param", encryptedRequest);
        return requestParameterMap;
    }

    /**
     * 获取请求实体
     *
     * @param body
     * @param <T>
     * @return
     */
    private <T> HttpEntity<T> getRequestEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        MediaType mediaType = new MediaType("application", "json", Charset.forName("UTF-8"));
        headers.setContentType(mediaType);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setAcceptCharset(Arrays.asList(Charset.forName("UTF-8")));
        HttpEntity<T> requestEntity = new HttpEntity<T>(body, headers);
        return requestEntity;
    }

    /**
     * 请求编号
     *
     * @return
     */
    private String getRequestStreamId() {
        int randomNum = random.nextInt(100000)+899999;
        Long requestStreamId = Long.parseLong(DateUtil.date2Str(new Date(),"yyyyMMddHHmmss"));
        return String.valueOf(requestStreamId).concat("" + randomNum);
    }

    /**
     * 获取发话费卡相关参数
     *
     * @return HashMap<String, Integer>
     */
    private Map<String, String> getCallChargeCardParameter() {
        HashMap<String, String> callChargeCardParameter = null;
        List<ParameterDto> chargeUrlParameter = queryServiceClient
            .queryParameter("SYSTEM", "SINOCHARGEURL", null, null);
        String chargeUrl = (chargeUrlParameter == null || chargeUrlParameter.isEmpty())
            ? null : chargeUrlParameter.get(0).getPmco();

        if (null != chargeUrl) {
            List<ParameterDto> chargeMoneyParameter = queryServiceClient
                .queryParameter("SYSTEM", "SINOCHARGEMONEY",
                    null, null);
            String chargeMoney = (chargeMoneyParameter == null || chargeMoneyParameter.isEmpty())
                ? null : chargeMoneyParameter.get(0).getPmco();

            if (null != chargeMoney) {
                callChargeCardParameter = new HashMap<String, String>(3);
                callChargeCardParameter.put("chargeMoney", chargeMoney);
                callChargeCardParameter.put("chargeUrl", chargeUrl);
            } else {
                LOGGER.error("getCallChargeCardParameter_未获取到话费卡充值金额");
            }
        } else {
            LOGGER.error("getCallChargeCardParameter_未获取到话费卡充值URL");
        }

        return callChargeCardParameter;
    }

}
