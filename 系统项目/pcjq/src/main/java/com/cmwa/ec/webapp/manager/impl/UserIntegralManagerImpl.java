package com.cmwa.ec.webapp.manager.impl;

import com.cmwa.ec.message.facade.dto.BusinessTypeDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.dto.mail.MailConfig;
import com.cmwa.ec.message.facade.dto.mail.MailMessage;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.system.ParameterDto;
import com.cmwa.ec.query.facade.model.consultant.CustserviceInfoDTO;
import com.cmwa.ec.query.facade.model.integral.IntegralOperationDTO;
import com.cmwa.ec.trade.facade.model.TradeResult;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserIntegralDetailDTO;
import com.cmwa.ec.user.facade.dto.UserIntegralInfoDTO;
import com.cmwa.ec.user.facade.model.UserResult;
import com.cmwa.ec.webapp.client.MessageServiceClient;
import com.cmwa.ec.webapp.client.QueryServiceClient;
import com.cmwa.ec.webapp.client.TradeServiceClient;
import com.cmwa.ec.webapp.client.UserServiceClient;
import com.cmwa.ec.webapp.manager.QueryManager;
import com.cmwa.ec.webapp.manager.UserIntegralManager;
import com.cmwa.ec.webapp.model.integral.IntegralDetailVO;
import com.cmwa.ec.webapp.util.ECConstants;
import com.cmwa.ec.webapp.util.RequestHelper;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * 用户积分业务处理类
 *
 * @author ex-chent@cmfchina.com
 */
public class UserIntegralManagerImpl implements UserIntegralManager {

    /**
     * slf4jLogger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserIntegralManagerImpl.class);

    /**
     * 用户服务客户端
     */
    @Autowired
    private UserServiceClient userServiceClient;

    /**
     * 查询服务客户端
     */
    @Autowired
    private QueryServiceClient queryServiceClient;

    /**
     * 消息服务客户端
     */
    @Autowired
    private MessageServiceClient msgServiceClient;

    /**
     * 交易服务客户端
     */
    @Autowired
    private TradeServiceClient tradeServiceClient;

    @Autowired
    private QueryManager queryManager;

    @Autowired
    private MessageServiceClient messageServiceClient;


    @Override
    public JSONObject getUserIntegralInfo(HttpServletRequest request, String cmfUserId) {
        JSONObject result = ECConstants.ERR_JSON_RESULT;
        try {
            UserResult<?> userResult = userServiceClient.getUserIntegralInfo(cmfUserId);
            UserBaseInfoDto userBaseInfo = RequestHelper.getSessionUserBaseInfo(request);
            // 取到积分信息以及SESSION中的用户信息
            if (null != userBaseInfo && userResult.isSuccess()) {
                UserIntegralInfoDTO integralInfo = (UserIntegralInfoDTO) userResult.getData();
                result = new JSONObject().accumulate(ECConstants.ERR_CODE, ECConstants.COMMON_SUCCESS)
                    .accumulate("mobile", userBaseInfo.getMobile())
                    .accumulate("integral", integralInfo.getIntegral())
                    .accumulate("noPurchase", integralInfo.getNoPurchase())
                    .accumulate("alreadyPurchase", integralInfo.getAlreadyPurchase());
            } else {
                LOGGER.error("getUserIntegralAndMobile_获取用户积分信息失败_errCode:{}, errMsg:{}",
                    userResult.getErrorCode(), userResult.getErrorMsg());
            }
        } catch (Exception omg) {
            LOGGER.error("getUserIntegralAndMobile_获取用户积分信息异常_", omg);
        }
        return result;
    }

    @Override
    public List<IntegralDetailVO> listUserIntegralDetail(String cmfUserId) {
        List<IntegralDetailVO> resultList = new ArrayList<IntegralDetailVO>();
        try {
            UserResult<?> userResult = userServiceClient.listUserIntegralDetail(cmfUserId);
            QueryMessageDto queryResult = queryServiceClient.listIntegralOperation();

            if (userResult.isSuccess() && ECConstants.COMMON_SUCCESS.equals(queryResult.getReturnCode())) {
                //noinspection unchecked
                List<IntegralOperationDTO> operationList = (List<IntegralOperationDTO>) queryResult.getData();
                HashMap<Integer, String> operationMap = new HashMap<Integer, String>(15);
                for (IntegralOperationDTO operation : operationList) {
                    operationMap.put(operation.getOperationId(), operation.getTypeDetail());
                }
                //noinspection unchecked
                List<UserIntegralDetailDTO> detailList = (List<UserIntegralDetailDTO>) userResult.getData();
                // 被推荐人cmfUserId
                HashSet<String> recommended = new HashSet<String>();
                for (UserIntegralDetailDTO detail : detailList) {
                    IntegralDetailVO vo = new IntegralDetailVO();
                    BeanUtils.copyProperties(detail, vo);
                    vo.setContent(operationMap.get(vo.getOperationid()));
                    // 从上往下处理
                    switch (vo.getOperationid()) {
                        case 100007:
                            if (!recommended.contains(detail.getRelatedRecord())) {
                                vo.setContent(vo.getContent() + detail.getRecommendedName());
                                resultList.add(vo);
                            }
                            break;
                        case 100004:
                        case 100006:
                            vo.setDisplayState(1);
                            resultList.add(vo);
                            break;
                        case 100008:
                            vo.setDisplayState(1);
                            recommended.add(detail.getRelatedRecord());
                            vo.setContent(vo.getContent() + detail.getRecommendedName());
                            resultList.add(vo);
                            break;
                        case 100001:
                        case 100003:
                        case 100005:
                            resultList.add(vo);
                            break;
                        default:
                    }
                }
            } else {
                LOGGER.error("listUserIntegralDetail_获取积分流水失败_errCode: {}, errMsg: {}",
                    userResult.getErrorCode(), userResult.getErrorMsg());
            }
        } catch (Exception omg) {
            LOGGER.error("listUserIntegralDetail_获取积分流水异常", omg);
        }
        // 数据获取失败将会返回空集合
        Collections.sort(resultList);
        return resultList;
    }

    @Override
    public List listIntegralGoods() {
        List resultList = null;
        try {
            QueryMessageDto queryResult = queryServiceClient.listIntegralGoods();
            if (ECConstants.COMMON_SUCCESS.equals(queryResult.getReturnCode())) {
                resultList = (List) queryResult.getData();
            } else {
                LOGGER.error("listIntegralGoods_获取可用积分兑换的商品列表失败_errCode: {}, errMsg: {}",
                    queryResult.getReturnCode(), queryResult.getReturnMsg());
            }
        } catch (Exception omg) {
            LOGGER.error("listIntegralGoods_获取可用积分兑换的商品列表异常_", omg);
        }
        // 数据获取失败将会返回null
        return resultList;
    }

    @Override
    public boolean isNewUserNotInvited(String cmfUserId) {
        boolean newUserNotInvited = false;
        QueryMessageDto queryResult = queryServiceClient.isNewUserNotInvited(cmfUserId);
        if (ECConstants.COMMON_SUCCESS.equals(queryResult.getReturnCode())) {
            newUserNotInvited = (Boolean) queryResult.getData();
        } else {
            LOGGER.error("isNewUserNotInvited_判断是否新用户并且该用户未绑定邀请人失败_errCode: {}, errMsg: {}",
                queryResult.getReturnCode(), queryResult.getReturnMsg());
            // TODO throws Exception
        }
        return newUserNotInvited;
    }

    @Override
    public boolean enableIntegral() {
        boolean enableIntegral = false;
        try {
            QueryMessageDto queryResult = queryServiceClient
                .queryParameter(null, "SYSTEM", "enableIntegral", "", "");
            if (ECConstants.COMMON_SUCCESS.equals(queryResult.getResultCode())) {
                //noinspection unchecked
                List<ParameterDto> parameter = (List<ParameterDto>) queryResult.getData();
                enableIntegral = (1 == Integer.valueOf(parameter.get(0).getPmco()));
            } else {
                LOGGER.error("enableIntegral_查询积分开关失败_errCode: {}, errMsg: {}",
                    queryResult.getResultCode(), queryResult.getResultMsg());
            }
        } catch (Exception omg) {
            LOGGER.error("enableIntegral_查询积分开关异常, 将返回false_", omg);
            enableIntegral = false;
        }
        // 只有积分开关启用时才返回true
        return enableIntegral;
    }

    @Override
    public boolean bindingReferrer(String cmfUserId, String invitationCode) {
        boolean successfulBinding = false;
        try {
            UserResult<?> userResult = userServiceClient.updateUserReferrer(cmfUserId, invitationCode);
            if (userResult.isSuccess()) {
                successfulBinding = true;
            } else {
                LOGGER.error("bindingReferrer_绑定推荐人失败_errCode: {}, errMsg: {}",
                    userResult.getErrorCode(), userResult.getErrorMsg());
            }
        } catch (Exception omg) {
            LOGGER.error("bindingReferrer_绑定推荐人异常_", omg);
            successfulBinding = false;
        }
        return successfulBinding;
    }

    @Override
    public boolean bindingReferrerByPhoneNumber(String cmfUserId, String phoneNumber) {
        boolean successfulBinding = false;
        try {
            UserResult<?> userResult = userServiceClient.updateUserReferrerByPhoneNumber(cmfUserId, phoneNumber);
            if (userResult.isSuccess()) {
                successfulBinding = true;
            } else {
                LOGGER.warn("bindingReferrerByPhoneNumber_绑定推荐人失败_errCode: {}, errMsg: {}",
                    userResult.getErrorCode(), userResult.getErrorMsg());
            }
        } catch (Exception omg) {
            LOGGER.error("bindingReferrer_绑定推荐人异常_", omg);
            successfulBinding = false;
        }
        return successfulBinding;
    }

    @Override
    public int shareConsultant(String customerName, String customerMobile, String cmfUserId, String referrer,
                               String consultant, String consultantMobile) {
        int result;
        UserResult<?> userResult = userServiceClient.insertDelayIntegralInfo(customerName, customerMobile, cmfUserId);
        if (!userResult.isSuccess()) {
            // 调用User服务异常
            return 4;
        }
        int resultStatus = (Integer) userResult.getData();
        switch (resultStatus) {
            // 被推荐人未注册, 可推荐
            case 0:
                boolean sentSuccessful = notificationConsultant(customerName, customerMobile, cmfUserId, referrer,
                    consultant, consultantMobile);
                result = sentSuccessful ? 0 : 3;
                break;
            // 被推荐人未注册, 但已推荐过
            case 1:
                result = 1;
                break;
            // 被推荐人已是注册用户
            case 2:
            	UserResult<?> isRepeatResult = userServiceClient.isRepeatRecommRegisterUser(cmfUserId, customerMobile);
            	if(!isRepeatResult.isSuccess()) {
            		LOGGER.error("调用user服务查询是否重复推荐失败---errorCode:{},errorMsg:{}", isRepeatResult.getErrorCode(), isRepeatResult.getErrorMsg());
            		return 4;
            	}
            	boolean isRepeat = (Boolean) isRepeatResult.getData();
            	// 已推荐过，不再通知
            	if(isRepeat) {
            		LOGGER.info("重复推荐注册用户，不再通知顾问---推荐人cmfuserid:{},被推荐人手机号:{}", cmfUserId, customerMobile);
            		return 2;
            	}
                boolean sentSuccess = notificationConsultant(customerName, customerMobile, cmfUserId, referrer,
                    consultant, consultantMobile);
                result = sentSuccess ? 2 : 3;
                break;
            // 系统异常
            default:
                result = 3;
        }
        return result;
    }

    @Override
    public boolean goodsExchange(String cmfUserId, int goodsId, long integralBeforeChange, long integralChange) {
        if (!(integralBeforeChange > 0L && integralChange <= integralBeforeChange)) {
            return false;
        }
        TradeResult<?> result = tradeServiceClient.goodsExchange(cmfUserId, goodsId, integralBeforeChange, integralChange);
        if (!result.isSuccess()) {
            LOGGER.error("goodsExchange_兑换物品发生异常_errCode:{}, errMsg:{}, cmfUserId:{}, goodsId:{}, " +
                "integralBeforeChange:{}, integralChange:{}", result.getErrorCode(), result.getErrorMsg(), cmfUserId,
                goodsId, integralBeforeChange, integralChange);
        }
        return true;
    }

    @Override
    public List listExhibitionFund() {
        List fundList = null;
        QueryMessageDto queryResult = queryServiceClient.listExhibitionFund();
        if (ECConstants.COMMON_SUCCESS.equals(queryResult.getReturnCode())) {
            fundList = (List) queryResult.getData();
        }
        return fundList;
    }

    private boolean notificationConsultant(String customerName, String customerMobile, String cmfUserId, String referrer,
                                           String consultant, String consultantMobile) {
        int noticeType = 0;
        // 查询通知方式
        QueryMessageDto queryResult = queryServiceClient
            .queryParameter(null, "SYSTEM", "shareNotice", "", "");
        if (ECConstants.COMMON_SUCCESS.equals(queryResult.getResultCode())) {
            //noinspection unchecked
            List<ParameterDto> parameter = (List<ParameterDto>) queryResult.getData();
            noticeType = Integer.parseInt(parameter.get(0).getPmco());
        } else {
            LOGGER.error("enableIntegral_查询通知方式系统参数失败_errCode: {}, errMsg: {}",
                queryResult.getReturnCode(), queryResult.getReturnMsg());
        }
        switch (noticeType) {
            // 邮件通知
            case 0:
                return emailNotification(cmfUserId, buildConsultantSMS(consultant, customerName,
                    referrer, consultantMobile, customerMobile, 0).getContent());
            // 短信通知
            case 1:
                return smsNotification(cmfUserId, consultant, customerName, referrer,
                    consultantMobile, customerMobile);
            // 邮件加短信通知
            case 2:
                return emailNotification(cmfUserId, buildConsultantSMS(consultant, customerName,
                    referrer, consultantMobile, customerMobile, 0).getContent())
                    && smsNotification(cmfUserId, consultant, customerName, referrer, consultantMobile, customerMobile);
            default:
                return false;
        }
    }

    /**
     *
     * @return
     */
    private boolean emailNotification(String cmfUserId, String noticeContent) {
        CustserviceInfoDTO consultantInfo = queryManager.getConsultantInfo(cmfUserId);

        String returnCode=null;
        MsgServiceMessageDto msgServicedto = new MsgServiceMessageDto();
/*            List<ParameterDto> listParaCc = (List<ParameterDto>) queryParamListCc.getData();
            ParameterDto parameterCc =listParaCc.get(0);
            // 抄送人
            String[] tccs = parameterCc.getPmco().split(",");*/

        if (null == consultantInfo || StringUtils.isBlank(consultantInfo.getMail())) {
            LOGGER.error("emailNotification_顾问邮箱为空");
            return false;
        }

        String[] ccListAddr = null;
		try {
			MailConfig mailConfig = messageServiceClient.queryMailAll("25");	 //查询MGM推荐好友邮件提醒
			if(mailConfig != null && !StringUtils.isEmpty(mailConfig.getCcListAddr())) {
				String[] ccs = mailConfig.getCcListAddr().trim().split(",");
				ccListAddr = new String [ccs.length];
				for(int i = 0; i < ccs.length; i++){
					if(!StringUtils.isEmpty(ccs[i])) {
						ccListAddr[i] = ccs[i].trim();
					}
				}
				LOGGER.info("查询邮件配置==============" + new Date()+":"+mailConfig.toString());
			}
			
		} catch (Exception e1) {
			LOGGER.error("查询邮件配置 Exception.......................", e1);
		}
		
        //邮件内容
        StringBuilder content = new StringBuilder()
            .append("<!DOCTYPE html><html><head><meta charset='UTF-8'>")
            .append("<meta http-equiv=content-type content='text/html;charset=utf-8'></head>")
            .append("<body><table style='width: 890px;border-color: #666666;font-family: verdana,arial,sans-serif;")
            .append("font-size:15px;border-collapse: collapse;text-align: center;cellpadding='0' cellspacing='0'>")
            .append("<tr style='background: #d3d3d3;'>")
            .append("<td style='height: 40px;border: 1px solid #a5a5a5;'>顾问姓名</td>")
            .append("<td style='height: 40px;border: 1px solid #a5a5a5;'>通知内容</td></tr><tr>")
            .append("<td style='height: 40px;border: 1px solid #a5a5a5;'>")
            .append(consultantInfo.getCustserName())
            .append("</td><td style='height: 40px;border: 1px solid #a5a5a5;'>")
            .append(noticeContent)
            .append("</td></tr></table></body></html>");
        MailMessage mailMessage = new MailMessage();
        boolean sendSuccess = false;
        try {
            // 接收人
            mailMessage.setTo(consultantInfo.getMail().trim());
            //抄送
            if(ccListAddr != null && ccListAddr.length > 0) {
            	mailMessage.setCc(ccListAddr); // 抄送的地址+手动输入的地址
            }
            mailMessage.setContent(content.toString());
            // 邮件主题
            mailMessage.setSubject("好消息! 特大好消息! 您被客户推荐了!");
            mailMessage.setConfId("");
            msgServicedto = messageServiceClient.sendMail(mailMessage);
            LOGGER.info("发送邮件结果：" + msgServicedto.getErrCode());
        } catch (Exception omg) {
            LOGGER.error("发送邮件异常：" + omg.getMessage());
            sendSuccess = false;
        }
        if(msgServicedto!=null){
            sendSuccess = ECConstants.COMMON_SUCCESS.equals(msgServicedto.getResultCode());
        }
        return sendSuccess;
    }

    private boolean smsNotification(String cmfUserId, String consultant, String customerName, String referrer,
                                    String consultantMobile, String customerMobile) {
        MsgSmsRecordDto consultantSmsRecord = buildConsultantSMS(consultant, customerName, referrer,
            consultantMobile, customerMobile, 1);
        MsgSmsRecordDto recommendedSmsRecord = buildRecommendedSMS(consultant, customerName, referrer,
            consultantMobile, customerMobile);
        boolean sentSuccessful = msgServiceClient.sendMarketMsg(consultantSmsRecord).isSuccess()
            && msgServiceClient.sendMarketMsg(recommendedSmsRecord).isSuccess();
        if (sentSuccessful) {
            return true;
        } else {
            LOGGER.error("SMSNotification_推荐顾问时发送短信失败_cmfUserId: {}, customerName: {}, " +
                    "customerMobile: {}, referrer: {}, consultant: {}, consultantMobile: {}",
                cmfUserId, customerName, customerMobile, referrer, consultant, consultantMobile);
            return false;
        }
    }

    private MsgSmsRecordDto buildRecommendedSMS(String consultant, String customerName, String referrer,
                                                String consultantMobile, String customerMobile) {
        // 构建发送给被推荐人的短信
        BusinessTypeDto recommendedMsgType = msgServiceClient.queryBusinessType("70");
        String recommendedMsg = recommendedMsgType.getMsgTemplate()
            .replace("{customerName}", customerName).replace("{referrer}", referrer)
            .replace("{consultant}", consultant).replace("{phoneNumber}", consultantMobile);
        MsgSmsRecordDto recommendedSmsRecord= new MsgSmsRecordDto();
        recommendedSmsRecord.setMsgTypeDesc(recommendedMsgType.getBnsTypeDesc());
        recommendedSmsRecord.setMsgType(recommendedMsgType.getBnsType());
        recommendedSmsRecord.setClientName(customerName);
        recommendedSmsRecord.setContent(recommendedMsg);
        recommendedSmsRecord.setMobile(customerMobile);
        recommendedSmsRecord.setSendUser("system");
        recommendedSmsRecord.setMethod("A");
        return recommendedSmsRecord;
    }

    private MsgSmsRecordDto buildConsultantSMS(String consultant, String customerName, String referrer,
                                               String consultantMobile, String customerMobile, int contentType) {
        // 构建发送给专属顾问的短信
        BusinessTypeDto consultantMsgType = msgServiceClient.queryBusinessType("71");
        String consultantMsg;
        if (0 == contentType) {
            consultantMsg = consultantMsgType.getMsgTemplate()
                .replace("{consultant}", "<font style=\"color: green;\">" + consultant + "</font>")
                .replace("{referrer}", "<font style=\"color: green;\">" + referrer + "</font>")
                .replace("{customerName}", "<font style=\"color: green;\">" + customerName + "</font>")
                .replace("{phoneNumber}", "<font style=\"color: green;\">" + customerMobile + "</font>");
        } else {
            consultantMsg = consultantMsgType.getMsgTemplate()
                .replace("{consultant}", consultant).replace("{referrer}", referrer)
                .replace("{customerName}", customerName).replace("{phoneNumber}", customerMobile);
        }
        MsgSmsRecordDto consultantSmsRecord = new MsgSmsRecordDto();
        consultantSmsRecord.setMsgTypeDesc(consultantMsgType.getBnsTypeDesc());
        consultantSmsRecord.setMsgType(consultantMsgType.getBnsType());
        consultantSmsRecord.setMobile(consultantMobile);
        consultantSmsRecord.setClientName(consultant);
        consultantSmsRecord.setContent(consultantMsg);
        consultantSmsRecord.setSendUser("system");
        consultantSmsRecord.setMethod("A");
        return consultantSmsRecord;
    }

}