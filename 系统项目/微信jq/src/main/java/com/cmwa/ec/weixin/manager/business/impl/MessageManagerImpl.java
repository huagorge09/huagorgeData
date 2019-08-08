package com.cmwa.ec.weixin.manager.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.dto.MsgParameterDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.dto.mail.MailConfig;
import com.cmwa.ec.message.facade.dto.mail.MailMessage;
import com.cmwa.ec.message.facade.wsadapter.DateUtil;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.manager.business.MessageManager;
import com.cmwa.ec.weixin.util.HttpPostUtil;
import com.cmwa.ec.weixin.util.SpringContextUtil;
import com.cmwa.ec.weixin.util.WeixinUtil;

public class MessageManagerImpl implements MessageManager {

    private static Logger logger = Logger.getLogger(MessageManagerImpl.class.getName());

    @Autowired
    private MessageServiceClient messageServiceClient;

    /***
     * @param Context
     *            日志信息
     * @param bnsType
     *            业务类型. （0、注册 1、交易 2、绑定 3、账户手机验证 4、修改密码）
     * @param mobile
     *            手机号.
     * @param carMantissa
     *            银行卡号尾数 （可为空）
     * @param amount
     *            金额（可为空）
     * @param bankName
     *            银行（可为空）
     * @param ip
     *            ip地址
     * @return ReturnMsg对象,包括验证码申请成功与否,已经与返回码关联的描述,sessionID 返回码 0000 成功
     */
    @Override
    public MsgServiceMessageDto applyVrfCode(Context context, String mobile, String bnsType, String carMantissa, String amount, String bankName, String ip) {
        try {
            return messageServiceClient.applyVrfCode(context, mobile, bnsType, carMantissa, amount, bankName);
        } catch (Exception e) {
            logger.error("MessageManagerImpl.applyVrfCode异常", e);
        }
        return null;
    }

    /**
     * @param Context
     *            日志信息
     * @param sessionId
     *            与验证码申请关联的sessionID.
     * @param mobile
     *            手机号.
     * @param vrfCode
     *            用户填入的验证码.
     * @return ReturnMsg对象,包括验证码申请成功与否,已经与返回码关联的描述. 返回码 0000 成功
     */
    @Override
    public String checkVrfCode(Context context, String sessionID, String mobile, String vrfCode) {

        String returnCode = "";
        // 返回的信息
        String returnMsg = "";
        /**
         * 关键参数为空 USR-5001 验证失败，验证码错误 USR-5201 验证失败,会话失效 USR-5202 验证失败，会话已验证
         * USR-5203 系统错误 9999
         */
        logger.info("MessageManagerImpl类【ajaxcheckVrfCode】开始>>>sessionID:" + sessionID + ">>>mobile:" + mobile + ">>>" + vrfCode + ">>>timestamp:" + System.currentTimeMillis());
        MsgServiceMessageDto msgServicedto = null;
        try {
            msgServicedto = messageServiceClient.checkVrfCode(context, sessionID, mobile, vrfCode);
        } catch (Exception e) {
            logger.error("MessageManagerImpl.checkVrfCode异常", e);
        }
        if (msgServicedto != null) {
            returnCode = msgServicedto.getReturnCode();
            returnMsg = msgServicedto.getReturnMsg();
        }

        logger.info("MessageManagerImpl类【ajaxcheckVrfCode】结束>>>returnCode:" + returnCode + ">>>>returnMsg:" + returnMsg + ">>>timestamp:" + System.currentTimeMillis());

        return returnCode;
    }

    @Override
    public JSONObject sendSmsMsg(Context context, MsgParameterDto msgParameter, MsgSmsRecordDto msgRecord) {

        String returnCode = "9999";
        String returnMsg = "系统异常";
        logger.info("MessageManagerImpl类【sendSmsMsg】开始>>>msgDto:" + msgParameter.toString() + ">>>timestamp:" + System.currentTimeMillis());
        try {
            MsgServiceMessageDto msgServicedto = messageServiceClient.sendSmsMsg(context, msgParameter, msgRecord);
            if (msgServicedto != null) {
                returnCode = msgServicedto.getReturnCode();
                returnMsg = msgServicedto.getReturnMsg();
            }
        } catch (Exception e) {
            logger.error("MessageManagerImpl类【sendSmsMsg】异常", e);
        }
        logger.info("MessageManagerImpl类【sendSmsMsg】结束>>>returnCode:" + returnCode + ">>>>returnMsg:" + returnMsg + ">>>timestamp:" + System.currentTimeMillis());
        JSONObject returnJsonObject = new JSONObject();
        returnJsonObject.put("returnCode", returnCode);
        returnJsonObject.put("returnMsg", returnMsg);
        return returnJsonObject;

    }

    /**
     * 发送通知消息(没有任何变量参数)
     * 
     * @param mobile
     *            发送到的手机号码
     * @param msgType
     *            消息编号
     * @return ReturnMsg对象,包括消息发送成功与否,已经与返回码关联的描述.
     * 
     *         <pre>
     * 返回码
     * 成功							0000
     * 关键参数为空						USR-5001
     * 未配置消息模板					USR-5103
     * 操作数据库失败					USR-5205
     * 系统错误						9999
     * </pre>
     */
    @Override
    public JSONObject sendTextMessage(Context context, MsgSmsRecordDto msgRecord) {

        JSONObject returnJsonObject = new JSONObject();
        String returnCode = null;
        // 返回的信息
        String returnMsg = null;

        String mobile = msgRecord == null ? null : msgRecord.getMobile();
        String msgType = msgRecord == null ? null : msgRecord.getMsgType();

        logger.info("MessageManagerImpl类【sendTextMessage】开始>>>mobile:" + mobile + ">>>msgType:" + msgType + ">>>timestamp:" + System.currentTimeMillis());
        MsgServiceMessageDto msgServicedto = messageServiceClient.sendTextMessage(context, msgRecord);

        if (msgServicedto != null) {
            returnCode = msgServicedto.getReturnCode();
            returnMsg = msgServicedto.getReturnMsg();
        }

        logger.info("MessageManagerImpl类【sendTextMessage】结束>>>returnCode:" + returnCode + ">>>>returnMsg:" + returnMsg + ">>>timestamp:" + System.currentTimeMillis());

        returnJsonObject.put("returnCode", returnCode);
        returnJsonObject.put("returnMsg", returnMsg);

        return returnJsonObject;
    }

    public JSONObject sendJDStockMsgMail(int jdCardCount, String mailType) {
        JSONObject returnJsonObject = new JSONObject();
        String returnCode = null;
        MsgServiceMessageDto msgServicedto = new MsgServiceMessageDto();
        MailMessage mailMessage = new MailMessage();
        MailConfig mailConfig = null;
        try {
            mailConfig = messageServiceClient.queryMailConfig(mailType);//
            logger.info("查询邮件配置==============" + new Date() + ":" + mailConfig.toString());
        } catch (Exception e1) {
            logger.error("查询邮件配置 Exception.......................", e1);
        }
        // 邮件内容
        String content = "<!DOCTYPE html><html><head><meta charset='UTF-8'><meta http-equiv=content-type content='text/html;charset=utf-8'>"
                + "</head><body><table cellpadding='0' cellspacing='0' width='100%'>" + "<tr style='background: #d3d3d3;'>"
                + "<td style='height: 40px;border: 1px solid #a5a5a5;' width='70%' align='center'>京东卡剩余数目</td>"
                + "<td style='height: 40px;border: 1px solid #a5a5a5;' width='30%' align='center'>当前时间</td></tr>";

        String format = "yyyyMMddHHmmss";
        String currentTime = DateUtil.date2Str(new Date(), format);

        content += "<tr>" + "<td style='height: 40px;border: 1px solid #a5a5a5;' align='center'>" + jdCardCount + "</td>"
                + "<td style='height: 40px;border: 1px solid #a5a5a5;' align='center'>" + currentTime + "</td>" + "</tr> ";

        content += "</table></body></html>";

        try {
            String[] ccs = messageServiceClient.queryMailAppendList(mailType, "cc");
            String[] tos = messageServiceClient.queryMailAppendList(mailType, "to");
            mailMessage.setTo(tos); // 接收人，接收人输入框没有的话取配置文件中的地址
            mailMessage.setCc(ccs); // 抄送的地址+手动输入的地址
            mailMessage.setContent(content);
            mailMessage.setSubject(String.format(mailConfig.getHead()));// 主题
            mailMessage.setConfId(mailType);
            msgServicedto = messageServiceClient.sendMail(mailMessage);
            logger.info("发送邮件结果：" + msgServicedto.getErrCode());
        } catch (Exception e) {
            logger.error("发送邮件异常：" + e.getMessage());
        }
        if (msgServicedto != null) {
            returnCode = msgServicedto.getResultCode();
        }
        returnJsonObject.put("returnCode", returnCode);
        return returnJsonObject;
    }

    /**
     * 
     * @Title: sendTemplateMsg
     * @Description: 发送模板消息
     * @param openId
     * @param list
     * @param data
     */
    public void sendTemplateMsg(String openId, List<ParameterDto> list, String data) {
        try {
            logger.info("调用front发送模板消息");
            String url = "" + SpringContextUtil.getProperty("FRONTSERVICE_URL") + "/sendTemplateMsg.html";
            List<NameValuePair> param = new ArrayList<NameValuePair>();
            logger.info("openId = " + openId);
            logger.info("data = " + data);
            param.add(new BasicNameValuePair("toUserId", openId));
            for (ParameterDto d : list) {
                if (d.getPmco().contains("TEMPID")) {
                    param.add(new BasicNameValuePair("templateId", d.getPmnm()));
                }
                if (d.getPmco().contains("TEMPURL")) {
                    param.add(new BasicNameValuePair("url", d.getPmnm()));
                }
            }
            param.add(new BasicNameValuePair("filter", "5"));
            try {
                String genTempLateStr = WeixinUtil.genTempLateUtil(data);
                param.add(new BasicNameValuePair("content", WeixinUtil.cn2unicode(genTempLateStr)));
            } catch (Exception e) {
                logger.info("读取配置对应的模板消息data,解析失败", e);
                return;
            }
            HttpPostUtil.sendPostToFront(url, param, null);
        } catch (Exception e) {
            logger.error("发送模板消息失败", e);
        }
    }
}
