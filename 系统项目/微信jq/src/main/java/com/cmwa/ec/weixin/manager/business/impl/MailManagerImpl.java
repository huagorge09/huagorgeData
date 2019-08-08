package com.cmwa.ec.weixin.manager.business.impl;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.cmwa.ec.message.facade.dto.mail.MailConfig;
import com.cmwa.ec.message.facade.dto.mail.MailMessage;
import com.cmwa.ec.weixin.client.MessageServiceClient;
import com.cmwa.ec.weixin.manager.business.MailManager;
import com.cmwa.ec.weixin.util.DateUtils;

public class MailManagerImpl implements MailManager {
    private static Logger logger = LoggerFactory.getLogger(MailManagerImpl.class.getName());

    @Autowired
    MessageServiceClient messageServiceClient;

    @Override
    public void sendMail(List<?> targetList, String configId, String[] titles, String[] targetFields) {
        // 设置邮件的发送人，抄送人
        MailMessage mailMessage = setMailConfig(configId);
        if (mailMessage == null) {
            logger.info("配置邮件配置或邮件配置路径失败");
            return;
        }

        // 设置邮件的主题
        mailMessage.setSubject(mailMessage.getSubject().replace("T", DateUtils.formatDate(new Date(), "yyyy-MM-dd")));

        if (targetList == null || targetList.isEmpty()) {
            logger.info("查询数据为空,不发送邮件");
            return;
        }

        // 设置内容
        String mailContent = setMailContent(targetList, titles, targetFields);
        mailMessage.setContent(mailContent);
        // 发送带Excel文件的邮件
        try {
            messageServiceClient.sendMail(mailMessage);
        } catch (Exception e) {
            logger.error("发送邮件失败", e);
        }
        return;
    }

    /**
     * 
     * @Title: setMailContent
     * @Description: 设置邮件内容
     * @param targetList
     *            要设置到邮件的的list数据
     * @param titles
     *            邮件内容的表头
     * @param targetFields
     *            邮件dto对应的字段名
     * @return String 返回邮件内容
     */
    public String setMailContent(List<?> targetList, String[] titles, String[] targetFields) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html><html><head><meta charset='UTF-8'><meta http-equiv=content-type content='text/html;charset=utf-8'>")
                .append("</head><body><table style='width: 890px;border-color: #666666;font-family: verdana,arial,sans-serif;font-size:15px;border-collapse: collapse;text-align: center;cellpadding='0' cellspacing='0'>")
                .append("<td style='height: 40px;border: 1px solid #a5a5a5;'>序号</td>");
        for (int i = 0; i < titles.length; i++) {
            sb.append("<td style='height: 40px;border: 1px solid #a5a5a5;'>").append(titles[i]).append("</td>");
        }
        sb.append("</tr>");
        // 每一行的序号
        int index = 1;
        for (Object obj : targetList) {
            sb.append("<tr>");
            Class clazz = null;
            clazz = (Class) obj.getClass();
            Field[] fields = null;
            fields = clazz.getDeclaredFields();
            sb.append("<td style='height: 40px;border: 1px solid #a5a5a5;'>" + index + "</td>");
            for (int j = 0; j < targetFields.length; j++) {
                for (int i = 0; i < fields.length; i++) {
                    Field field = fields[i];
                    field.setAccessible(true);
                    Object value = new Object();
                    try {
                        value = field.get(obj);
                        // 设置对应单元格的值
                        if (targetFields[j].equals(field.getName())) {
                            sb.append("<td style='height: 40px;border: 1px solid #a5a5a5;'>").append(String.valueOf(value)).append("</td>");
                            break;
                        }
                    } catch (Exception e) {
                        logger.error("setMailContent失败", e);
                    }
                }
            }
            sb.append("</tr>");
            index++;
        }
        sb.append("</table></body></html>");
        return String.valueOf(sb);
    }

    /**
     * 
     * @Title: setMailConfig
     * @Description: 设置邮件配置
     * @param configId
     *            配置id
     * @return MailMessage
     */
    public MailMessage setMailConfig(String configId) {
        MailConfig mailConfig = null;
        try {
            mailConfig = messageServiceClient.queryMailConfig(configId);
        } catch (Exception e) {
            logger.info("查询邮件配置失败", e);
        }// 查询邮件配置
        if (mailConfig == null) {
            logger.warn("未查询到邮件配置，请确认是否配置邮件 configId:" + configId);
            return null;
        }
        String mailPath = messageServiceClient.queryMailPathConfig(configId);// 查询手动配置抄送人地址
        String[] mailcc = StringUtils.isBlank(mailPath) ? new String[] {} : mailPath.split(",");
        String[] tos = messageServiceClient.queryMailAppendList(configId, "to");// 收件人
        String[] ccs = messageServiceClient.queryMailAppendList(configId, "cc");// 抄送人
        String[] tccs = new String[ccs.length + mailcc.length];// 合并抄送人地址
        System.arraycopy(mailcc, 0, tccs, 0, mailcc.length);
        System.arraycopy(ccs, 0, tccs, mailcc.length, ccs.length);
        MailMessage mailMessage = new MailMessage();
        mailMessage.setTo(tos);
        mailMessage.setCc(tccs);
        mailMessage.setConfId(configId);
        mailMessage.setSubject(mailConfig.getHead());
        return mailMessage;
    }

}
