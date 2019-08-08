package com.cmwa.ec.webapp.manager;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.message.facade.dto.MsgParameterDto;
import com.cmwa.ec.message.facade.dto.MsgServiceMessageDto;
import com.cmwa.ec.message.facade.dto.MsgSmsRecordDto;
import com.cmwa.ec.message.facade.dto.mail.MailMessage;

public interface MessageManager {

	/**
	 * 发送短信
	 * @param context
	 * @param mobile
	 * @param bnsType
	 * @param bankcardNo
	 * @param money
	 * @param bankName
	 * @param seqId
	 * @return
	 * 			MsgServiceMessageDto
	 * @author maj
	 */
	MsgServiceMessageDto applyVrfCode(Context context, String mobile, String bnsType, String bankcardNo, String money, String bankName, String seqId);
	/**
	 * 验证短信验证码
	 * @param context
	 * @param sessionID
	 * @param mobile
	 * @param rvrfcode
	 * @return
	 * 			MsgServiceMessageDto
	 * @author luos
	 */
	MsgServiceMessageDto checkVrfCode(Context context,String sessionID, String mobile,String rvrfcode);
	/**
	 * 注册验证短信验证码
	 * @param context
	 * @param sessionID
	 * @param obj
	 * @param rvrfcode
	 * @param inputMobile
	 * @return
	 * 			MsgServiceMessageDto
	 * @author luos
	 */
	JSONObject checkVrfCodeResgist(Context context, String sessionID, String mobile, String rvrfcode, String inputMobile);
	
	/**
	 * 验证短信验证码  
	 * 不需要其他要素，只在session中获取手机号码(SessionValue.SESSION_USERMOBILE)
	 * @param context
	 * @param sessionID
	 * @param mobile
	 * @param rvrfcode
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	JSONObject checkMobileVrfCode(Context context, String sessionID, String mobile, String rvrfcode);
	
	/**
	 * 验证短信验证码--修改手机号码
	 * 需验证：获取手机短信验证码时保存在session中的手机号码SESSION_USERMOBILE；是否需要设置登录密码SESSION_HASCHECKSETLPASSWORD；
	 * 需删除：获取手机短信验证码时保存在session中的手机号码SESSION_USERMOBILE；是否需要设置登录密码SESSION_HASCHECKSETLPASSWORD；是否验证支付密码SESSION_CHECKEDTPASSWORD；
	 * @param context
	 * @param request
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	JSONObject checkVrfCodeByModifyMobile(Context context, HttpServletRequest request);
	/**
	 * 给用户发送通知信息
	 * @param context
	 * @param msgParameter
	 * @return ReturnMsg对象,包括验证码申请成功与否,已经与返回码关联的描述.
	 */
	public JSONObject sendSmsMsg(Context context,MsgParameterDto msgParameter,MsgSmsRecordDto msgSmsRecord);
	
	/**
	 * 发送通知消息(没有任何变量参数)
	 */
	public JSONObject sendTextMessage(Context context, MsgSmsRecordDto msgRecord);
	
	/**
	 * 发送邮件方法
	 * 
	 * 返回码
	 * 成功							0000
	 * 系统错误						9999
	 * @param context
	 * @param mailMessage
	 * @return
	 */
	public JSONObject sendMail(Context context,MailMessage mailMessage);
	
}
