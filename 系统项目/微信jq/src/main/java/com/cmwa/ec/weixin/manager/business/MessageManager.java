package com.cmwa.ec.weixin.manager.business;

import java.util.List;

import net.sf.json.JSONObject;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.dto.MsgParameterDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.weixin.dto.ParameterDto;

public interface MessageManager {

    /***
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
    public MsgServiceMessageDto applyVrfCode(Context context, String mobile, String bnsType, String carMantissa, String amount, String bankName, String ip);

    /**
     * @param sessionId
     *            与验证码申请关联的sessionID.
     * @param mobile
     *            手机号.
     * @param vrfCode
     *            用户填入的验证码.
     * @return ReturnMsg对象,包括验证码申请成功与否,已经与返回码关联的描述. 返回码 0000 成功
     */
    public String checkVrfCode(Context context, String sessionId, String mobile, String vrfCode);

    /**
     * 给用户发送通知信息
     * 
     * @param context
     * @param msgParameter
     * @return ReturnMsg对象,包括验证码申请成功与否,已经与返回码关联的描述.
     */
    public JSONObject sendSmsMsg(Context context, MsgParameterDto msgParameter, MsgSmsRecordDto msgRecord);

    /**
     * 发送通知消息(没有任何变量参数)
     */
    public JSONObject sendTextMessage(Context context, MsgSmsRecordDto msgRecord);

    /**
     * 华安京东卡达到阈值时，放松邮件到指定客户
     * 
     * @Title: sendJDStockMsgMail
     * @Description: TODO
     * @param @param jdCardCount
     * @param @param mailType
     * @param @return
     * @return JSONObject
     * @throws
     */
    public JSONObject sendJDStockMsgMail(int jdCardCount, String mailType);

    /**
     * 
     * @Title: sendTemplateMsg
     * @Description: 发送模板消息
     * @param openId
     * @param list
     * @param data
     *            模板消息的内容
     */
    public void sendTemplateMsg(String openId, List<ParameterDto> list, String data);

}
