package com.cmwa.ec.weixin.manager.business;

import java.util.List;

public interface MailManager {
    /**
     * 
     * @Title: sendMail
     * @Description: 发送邮件
     * @param targetList
     *            目标数据List
     * @param configId
     *            邮件配置编号
     * @param titles
     *            邮件标题
     * @param targetFields
     *            要显示的字段
     */
    public void sendMail(List<?> targetList, String configId, String[] titles, String[] targetFields);
}
