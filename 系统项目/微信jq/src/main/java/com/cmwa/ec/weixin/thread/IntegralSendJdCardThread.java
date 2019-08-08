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
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.wsadapter.DateUtil;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.model.user.IntegralAwardUserInfoDTO;
import com.cmwa.ec.trade.facade.model.TradeResult;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.client.QueryServiceClient;
import com.cmwa.ec.weixin.client.TradeServiceClient;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dao.activity.ActParameterDao;
import com.cmwa.ec.weixin.dao.activity.SinoActAwardDao;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.dto.SinoActAwardDto;
import com.cmwa.ec.weixin.manager.business.MessageManager;
import com.cmwa.ec.weixin.model.SinoActAwardInfoModel;
import com.cmwa.ec.weixin.util.DESUtils;

/**
 * 积分兑换京东卡轮询发放线程
 *
 * @author ex-chent@cmfchina.com
 */
public class IntegralSendJdCardThread extends Thread implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntegralSendJdCardThread.class);

    private SinoActAwardDao sinoActAwardDao;

    private TradeServiceClient tradeServiceClient;

    private QueryServiceClient queryServiceClient;

    private MessageServiceClient serviceClient;
    /**
     * 获取京东卡请求参数
     */
    private String tradeRequestParam;
    /**
     * 京东卡订单查询请求参数
     */
    private String queryRequestParam;
    /**
     * 第三方平台交易地址
     */
    private String tradeRequestUrl;
    /**
     * 第三方平台查询地址
     */
    private String queryRequestUrl;
    /**
     * key
     */
    private String appKey;
    /**
     * id
     */
    private String appId;
    /**
     * decryptKey
     */
    private String decryptKey;
    /**
     * 第三方平台账号?
     */
    private String agtPhone;

    private String tradePwd;

    private RestTemplate rest;

    private MessageManager messageManager;

    private ActParameterDao actParameterDao;

    /**
     * 每一次发卡唯一的订单序列号
     * 用于1003异常重发时携带相同的序列号
     */
    private String requestStreamId;

    private String jdCardMsg;

    private BusinessTypeDto jdCardTemplate;

    /**
     * 京东卡发送成功操作类型编号
     */
    private int jdCardSentSuccessOperation;

    /**
     * 读取线程执行开关
     */
    public boolean executeFlag = "ON".equals(SpringUtil.getProperty("cmwa.wx.sino.resendthred.excute"));

    /**
     * 请求第三方平台时递归情况的深度
     */
    private int recursiveDepth;

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
                this.start();
            } catch (Exception e) {
                LOGGER.error("a Thread exception test...", e);
            }
        } else {
            LOGGER.info("afterPropertiesSet_本机IP地址为: {}, 本机线程执行配置为: OFF, 不执行积分兑换轮询发放线程", ipAddress);
        }
        LOGGER.info("testThread...");
    }

    private void loadParameter() {
        sinoActAwardDao = SpringUtil.getBean(SinoActAwardDao.class);

        tradeServiceClient = SpringUtil.getBean(TradeServiceClient.class);

        queryServiceClient = SpringUtil.getBean(QueryServiceClient.class);

        serviceClient = SpringUtil.getBean(MessageServiceClient.class);

        actParameterDao = SpringUtil.getBean(ActParameterDao.class);

        messageManager = SpringUtil.getBean(MessageManager.class);

        // 获取京东卡请求参数
        tradeRequestParam
            = "{\"agtPhone\": \"{agtPhone}\",\"productCode\": {productCode},\"reqStreamId\": \"{reqStreamIdParam}\"," +
            "\"amount\": 1,\"tradePwd\": \"{tradePwd}\"}";
        // 京东卡订单查询请求参数
        queryRequestParam
            = "{\"agtPhone\": \"{agtPhone}\",\"reqStreamId\": \"{reqStreamIdParam}\",\"tradePwd\": \"{tradePwd}\"}";
        // 第三方平台交易地址
        tradeRequestUrl = SpringUtil.getProperty("cmwa.wx.sino.tradeRequestUrl");
        // 第三方平台查询地址
        queryRequestUrl = SpringUtil.getProperty("cmwa.wx.sino.queryRequestUrl");
        // key
        appKey = SpringUtil.getProperty("cmwa.wx.sino.appKey");
        // id
        appId = SpringUtil.getProperty("cmwa.wx.sino.appId");
        // decryptKey
        decryptKey = SpringUtil.getProperty("cmwa.wx.sino.award.decryptKey");
        // 第三方平台账号?
        agtPhone = SpringUtil.getProperty("cmwa.wx.sino.award.agtPhone");

        tradePwd = SpringUtil.getProperty("cmwa.wx.sino.award.tradePwd");
        parameterMap.put("appId", appId);

        rest = new RestTemplate();
        // 京东卡发送成功操作类型编号
        jdCardSentSuccessOperation = 100004;
        recursiveDepth = 7;
    }

    @Override
    public void run() {
        while (true) {
            try {
                // 初始化发送京东卡短信模板
                jdCardTemplate = serviceClient.queryBusinessType("56");
                if (jdCardTemplate != null) {
                    jdCardMsg = jdCardTemplate.getMsgTemplate();
                } else {
                    LOGGER.error("run_初始化发送京东卡短信模板失败");
                }
                try {
                    Thread.sleep(120000);
                } catch (InterruptedException omg) {
                    LOGGER.error("run_积分兑换轮询发放线程睡觉的时候做噩梦了_", omg);
                }
                QueryMessageDto queryResp = queryServiceClient.listNeedSendJdCard();
                // 未获取到需发卡的用户
                if (null == queryResp.getData()) {
                    LOGGER.warn("run_读取需发放京东卡的用户失败");
                    continue;
                }

                @SuppressWarnings("unchecked")
                List<IntegralAwardUserInfoDTO> phoneNumberList = (List<IntegralAwardUserInfoDTO>) queryResp.getData();
                LOGGER.info("run_查询到京东卡待处理数据量: " + phoneNumberList.size());
                for (IntegralAwardUserInfoDTO userInfo : phoneNumberList) {
                    LOGGER.info("run_发放京东卡开始_info: {}", userInfo);
                    Map<String, String> jdCardParameter = getJdCardParameter();
                    if (jdCardParameter == null) {
                        // 读取参数失败
                        continue;
                    }

                    HashMap<String, Object> requestMap = buildRequestParameters(jdCardParameter.get("productCode"));
                    SinoActAwardInfoModel jdCardInfo;
                    jdCardInfo = getJdCardFromThirdParty(requestMap, tradeRequestUrl, 0);
                    if (jdCardInfo == null) {
                        // 拿卡失败
                        continue;
                    }
                    SinoActAwardDto sinoActAwardDto = insertJdCardForIntegral(jdCardInfo, userInfo.getPhoneNumber(),
                        userInfo.getIdNo());
                    if (null == sinoActAwardDto) {
                        // 京东卡入库失败
                        continue;
                    }
                    // 减少系统参数中的京东卡库存
                    reduceInventoryOfJdCard();
                    // 回写兑换状态, 如果状态未回写成功就还会为客户重发卡
                    if (writeBackStatus(sinoActAwardDto, userInfo.getSerialNo())) {
                        LOGGER.info("run_状态回写成功_info: {}", userInfo);
                        // 发京东卡密短信给用户
                        boolean sendSuccess = sendJdCard(jdCardInfo, userInfo.getPhoneNumber(), userInfo.getUserName());
                        if (sendSuccess) {
                            LOGGER.info("run_京东卡短信发送成功_info: {}, cardInfo: {}", userInfo, jdCardInfo);
                        } else {
                            LOGGER.warn("run_京东卡短信发送失败_info: {}, cardInfo: {}", userInfo, jdCardInfo);
                        }
                    } else {
                        LOGGER.warn("run_状态回写失败_info: {}", userInfo);
                    }
                }

            } catch (Exception e) {
                LOGGER.error("开始执行MGM积分兑换京东卡处理轮询线程任务", e);
            }
        }
    }

    /**
     * 发送京东卡信息通知短信
     *
     * @param cardInfo
     * @param phoneNumber
     * @param userName
     * @return
     */
    private boolean sendJdCard(SinoActAwardInfoModel cardInfo, String phoneNumber, String userName) {
        boolean sentSuccess = false;
        String content = null;
        try {
            // 卡号, 卡密, 金额, 客服电话
            content = jdCardMsg
                .replace("{cardNumber}", cardInfo.getData().get(0).getCardNum())
                .replace("{cardPassword}", DESUtils.decrypt(cardInfo.getData().get(0).getCardPwd(), decryptKey))
                .replace("{amount}", "100")
                .replace("{mobile}", "0755-23988886");
        } catch (Exception omg) {
            LOGGER.error("sendJdCard_发送京东卡信息通知短信时解密失败_", omg);
        }
        MsgSmsRecordDto msgRecord = new MsgSmsRecordDto();
        msgRecord.setMsgTypeDesc(jdCardTemplate.getBnsTypeDesc());
        msgRecord.setMsgType(jdCardTemplate.getBnsType());
        msgRecord.setClientName(userName);
        msgRecord.setMobile(phoneNumber);
        msgRecord.setSendUser("system");
        msgRecord.setContent(content);
        msgRecord.setMethod("A");
        MsgServiceMessageDto msgResult = serviceClient.justSendMsg(msgRecord);
        if (msgResult != null && WXConstants.COMMON_SUCCESS.equals(msgResult.getReturnCode())) {
            sentSuccess = true;
        }
        return sentSuccess;
    }

    /**
     * 回写状态
     *
     * @param sinoActAwardDto
     * @param serialNo
     */
    private boolean writeBackStatus(SinoActAwardDto sinoActAwardDto, String serialNo) {
        boolean flag = false;
        // 回写WAECWEIXINUSER.CMWA_WX_SINOACT_AWARD表状态
        sinoActAwardDto.setUpdatedUser(this.getClass().getSimpleName());
        int affectedRows = sinoActAwardDao.rewriteStatus(sinoActAwardDto);
        if (1 == affectedRows) {
            LOGGER.info("sendJdCard_回写状态成功!_sinoActAwardDto: {}, serialNo: {}", sinoActAwardDto, serialNo);
            // 更改积分流水表此条流水记录为已发送
            TradeResult<?> tradeResult = tradeServiceClient.updateIntegralDetailOperation(serialNo, jdCardSentSuccessOperation);
            if (tradeResult.isSuccess()) {
                LOGGER.info("sendJdCard_流水记录修改成功_serialNo: {}, operationId: {}, sinoActAwardDto: {}",
                    serialNo, jdCardSentSuccessOperation, sinoActAwardDto);
                flag = true;
            } else {
                LOGGER.error("sendJdCard_流水记录修改失败_serialNo: {}, operationId: {}, sinoActAwardDto: {}",
                    serialNo, jdCardSentSuccessOperation, sinoActAwardDto);
            }
        } else {
            LOGGER.error("sendJdCard_回写状态失败，回写数据为: {}, serialNo: {}", sinoActAwardDto, serialNo);
        }
        return flag;
    }

    /**
     * 获取京东卡
     *
     * @param requestMap
     * @return SinoActAwardInfoModel, 获取不成功都会返回NULL
     */
    private SinoActAwardInfoModel getJdCardFromThirdParty(Map<String, Object> requestMap, String requestUrl,
                                                          int counter) throws Exception {
        LOGGER.info("getJdCardFromThirdParty_开始发送请求至第三方平台，获取京东卡账号_requestMap: {}, requestUrl: {}", requestMap, requestUrl);
        if (recursiveDepth < counter) {
            LOGGER.info("getJdCardFromThirdParty_事不过三啊, 哥们");
            return null;
        }
        // 请求第三方
        ResponseEntity<SinoActAwardInfoModel> response = rest.exchange(requestUrl, HttpMethod.POST,
            getRequestEntity(requestMap), new ParameterizedTypeReference<SinoActAwardInfoModel>() {
            });

        SinoActAwardInfoModel body;

        if (response != null) {
            body = response.getBody();
            LOGGER.info("收到第三方平台结果回应为: {}", body);

            int status = Integer.parseInt(body.getStatus());
            switch (status) {
                case 1000:
                    // success
                    LOGGER.info("获取京东卡成功，第三方平台返回状态码1000, responseBody: {}", body);
                    break;
                case 1001:
                    LOGGER.info("获取京东卡失败，第三方平台返回状态码1001, responseBody: {}", body);
                    body = null;
                    break;
                case 1003:
                    // query
                    LOGGER.info("第三方平台返回状态码为1003，需调取查询接口, responseBody: {}", body);
                    String queryRequestParameter = queryRequestParam
                        .replace("{reqStreamIdParam}", requestStreamId)
                        .replace("{agtPhone}", agtPhone)
                        .replace("{tradePwd}", tradePwd);
                    String encryptedRequest = DESUtils.encrypt(queryRequestParameter, appKey);
                    parameterMap.put("param", encryptedRequest);

                    body = getJdCardFromThirdParty(parameterMap, queryRequestUrl, ++counter);
                    break;
                case 1008:
                    LOGGER.info("获取京东卡失败，第三方平台返回状态码1008, responseBody:");
                    body = null;
                    break;
                default:
                    LOGGER.info("getJdCardFromThirdParty_第三方平台返回未知的返回码: {}, responseBody: {}", status, body);
                    body = null;
            }
        } else {
            LOGGER.error("getJdCardFromThirdParty_请求第三方平台无响应, response为null, requestUrl: {}, requestMap: {}",
                requestUrl, requestMap);
            body = null;
        }
        return body;
    }

    /**
     * 入库京东卡信息
     *
     * @param cardInfo
     * @param phoneNumber
     * @return
     */
    private SinoActAwardDto insertJdCardForIntegral(SinoActAwardInfoModel cardInfo, String phoneNumber, String idNo) {
        SinoActAwardDto sinoActAwardDto = new SinoActAwardDto();
        sinoActAwardDto.setExpireDate(cardInfo.getData().get(0).getExpiretime());
        sinoActAwardDto.setCardNum(cardInfo.getData().get(0).getCardNum());
        sinoActAwardDto.setCardPwd(cardInfo.getData().get(0).getCardPwd());
        sinoActAwardDto.setReqStreamId(cardInfo.getReqStreamId());
        sinoActAwardDto.setUpdatedUser(this.getClass().getSimpleName());
        sinoActAwardDto.setAssignChannel("Integral");
        sinoActAwardDto.setAssignIdcard(idNo);
        sinoActAwardDto.setAssignMobile(phoneNumber);
        int affectedRows = sinoActAwardDao.insert(sinoActAwardDto);
        if (1 == affectedRows) {
            LOGGER.info("insertJdCardForIntegral_京东卡入库成功");
        } else {
            LOGGER.error("insertJdCardForIntegral_获取到的京东卡入库失败，入库数据： {}", sinoActAwardDto);
            sinoActAwardDto = null;
        }
        return sinoActAwardDto;
    }

    private HashMap<String, Object> parameterMap = new HashMap<String, Object>();

    /**
     * 构建获取京东卡POST请求参数
     *
     * @return
     */
    private HashMap<String, Object> buildRequestParameters(String productCode) {
        try {
            // 清空Map
            requestStreamId = getRequestStreamId();
            String tradeRequestParameter = tradeRequestParam
                .replace("{reqStreamIdParam}", requestStreamId)
                .replace("{productCode}", productCode)
                .replace("{agtPhone}", agtPhone)
                .replace("{tradePwd}", tradePwd);
            LOGGER.info("请求参数: {}", tradeRequestParameter);
            String encryptedRequest = DESUtils.encrypt(tradeRequestParameter, appKey);
            parameterMap.put("param", encryptedRequest);
        } catch (Exception omg) {
            LOGGER.error("BuildRequestParameters_加密报文时异常_", omg);
        }

        return parameterMap;
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
        Random random = new Random();
        int randomNum = random.nextInt(100000) + 899999;
        Long requestStreamId = Long.parseLong(DateUtil.date2Str(new Date(), "yyyyMMddHHmmss"));
        return String.valueOf(requestStreamId).concat("" + randomNum);
    }

    /**
     * 减少京东卡库存
     */
    private void reduceInventoryOfJdCard() {
    	try {
    		// 获取awardStock 京东卡库存
            List<ParameterDto> queryAwardStock = queryServiceClient
                .queryParameter("SYSTEM", "SINOAWARDSTOCK", null, null);
            if (queryAwardStock != null) {
                // 京东卡库存减一
                actParameterDao.updatePmcoByPmky(queryAwardStock.get(0));
                // 获取阈值
                List<ParameterDto> querySinoThreshold = queryServiceClient.queryParameter("SYSTEM",
                    "SINOTHRESHOLD", null, null);
                if (querySinoThreshold != null) {
                    int threshold = Integer.parseInt(querySinoThreshold.get(0).getPmco());
                    int jdCardStock = Integer.parseInt(queryAwardStock.get(0).getPmco());
                    if (--jdCardStock <= threshold) {
                        // 当京东卡数量小于阀值时，发送邮件提示
                        messageManager.sendJDStockMsgMail(jdCardStock - 1, "18");
                        LOGGER.info("京东卡低于阀值，当前库存: {}", --jdCardStock);
                    }
                } else {
                    LOGGER.info("reduceInventoryOfJdCard_未获取到配置的京东卡库存阈值");
                }
            } else {
                LOGGER.info("reduceInventoryOfJdCard_未获取到配置的京东卡库存");
            }
		} catch (Exception e) {
			LOGGER.error("减少京东卡库存操作异常",e);
		}
    }


    /**
     * 获取发京东卡相关参数
     *
     * @return HashMap<String, Integer>
     */
    private Map<String, String> getJdCardParameter() {
    	HashMap<String, String> jdCardParameter = null;
    	try {
            // 获取产品代码
            List<ParameterDto> queryProductCode = queryServiceClient
                .queryParameter("SYSTEM", "SINOPRODUCTCODE", null, null);
            String productCode = (queryProductCode == null || 0 == queryProductCode.size())
                ? null : queryProductCode.get(0).getPmco();

            if (productCode != null) {
                // 获取awardStock 京东卡库存
                List<ParameterDto> queryAwardStock = queryServiceClient
                    .queryParameter("SYSTEM", "SINOAWARDSTOCK", null, null);
                String awardStock = (queryAwardStock == null || 0 == queryAwardStock.size())
                    ? null : queryAwardStock.get(0).getPmco();

                if (awardStock != null) {
                    if (0 >= Integer.parseInt(awardStock)) {
                        LOGGER.info("getJdCardParameter_京东卡库存不足！");
                    } else {
                        // 获取阈值
                        List<ParameterDto> querySinoThreshold = queryServiceClient.queryParameter("SYSTEM",
                            "SINOTHRESHOLD", null, null);
                        String sinoThreshold = (querySinoThreshold == null || 0 == querySinoThreshold.size())
                            ? null : querySinoThreshold.get(0).getPmco();
                        if (sinoThreshold != null) {
                            jdCardParameter = new HashMap<String, String>(5);
                            jdCardParameter.put("sinoThreshold", sinoThreshold);
                            jdCardParameter.put("productCode", productCode);
                            jdCardParameter.put("awardStock", awardStock);

                        } else {
                            LOGGER.warn("getJdCardParameter_未获取到配置的京东卡库存阈值");
                        }
                    }
                } else {
                    LOGGER.warn("getJdCardParameter_未获取到配置的京东卡库存");
                }
            } else {
                LOGGER.warn("getJdCardParameter_未查询到配置的产品代码");
            }
		} catch (Exception e) {
			LOGGER.error("获取发京东卡相关参数异常", e);
		}
        return jdCardParameter;
    }

}