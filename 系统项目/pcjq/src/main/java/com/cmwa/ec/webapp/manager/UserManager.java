package com.cmwa.ec.webapp.manager;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;

public interface UserManager {

	/**
	 * 查询登录用户信息
	 * 
	 * @param context
	 * @param cmfUserId
	 * @return UserServiceMessage
	 * @author maj
	 */
	UserServiceMessage queryUserAndAccoRlaById(Context context, String cmfUserId);

	/**
	 * 用户登录
	 * 
	 * @param context
	 * @param loginNumber
	 * @param password
	 * @param loginType
	 * @param lOGIN_CHANEL_01
	 * @param lOGIN_NMARK_01
	 * @return UserServiceMessage
	 * @author maj
	 */
	UserServiceMessage login(Context context, String loginNo, String lPassword, String loginType, String loginChannel, String loginMark);

	/**
	 * 验证支付密码
	 * @param context
	 * @param cmfUserId
	 * @param tpassword
	 * @param string
	 * @param manageType
	 * @param string2
	 * @param string3
	 * @return
	 * 			UserServiceMessage
	 * @author maj
	 */
	UserServiceMessage manageTpassword(Context context, String cmfUserId, String tpassword, String oldTpassword, String manageType, String tradeChannel, String tradeMark);
	/*==================================以上是1.0版本，以后会删掉========================================*/
	// TODO 以上是1.0版本，以后会删掉
	/**
	 * 用户注册
	 * @param context
	 * @param cmfUserId
	 * @param tpassword
	 * @param string
	 * @param manageType
	 * @param string2
	 * @param string3
	 * @return
	 * 			UserServiceMessage
	 * @author luos
	 */
	public JSONObject registerNormalUserWithT(HttpServletRequest request,Context context);
	/**
	 * 验证手机号码是否使用过
	 * @param context
	 * @param mobile
	 * @return
	 */
	public JSONObject verifyMobile(Context context,String mobile);

	
	/***
	 * 验证手机号码和图片验证码
	 * 找回登录密码页面
	 * @param response
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @author maj
	 */
	public JSONObject checkVrfCodeAndMobile(Context context,HttpServletRequest request, String mobile, String rvrfcode);

	/**
	 * 验证证件-银行卡鉴权页面
	 */
	
	public JSONObject checkIdNoByBankAuthentication(Context context, HttpServletRequest request);
	/***
	 * 获取手机验证码  不去到账户模块验证手机号码    信息鉴权/支付
	 * @param response
	 * @param request
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public JSONObject getVerifyCode(Context context, String mobile, String msgType, String bankNumber, String money, String bankName);
	/**
	 * 验证手机号码和返回手机验证码（新） 注册
	 * @param context
	 * @param request
	 * @return
	 * @author luos
	 */
	public JSONObject getMobileVerifyCode(Context context, HttpServletRequest request);

	/**
	 * 获取短信验证码-找回登录密码
	 * 需要验证图片验证码
	 * @param context
	 * @param mobile
	 * @param msgType
	 * @param bankNumber
	 * @param money
	 * @param bankName
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	public JSONObject getMsgByPsw(Context context, HttpServletRequest request);

	/**
	 * 重置登录密码
	 * @param context
	 * @param request
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	public JSONObject resetUserPassword(Context context, HttpServletRequest request);
	/**
	 * 管理支付密码
	 * @param context
	 * @param request
	 * @return
	 * @author luos
	 */
	JSONObject manageTpasswordResgist(Context context, HttpServletRequest request);

	/**
	 * 个人用户登录
	 * @param context
	 * @param request
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	public JSONObject login(Context context, HttpServletRequest request);
	/**
	 * 获取用户信息
	 * @param 
	 * @return
	 * @author luos
	 */
	JSONObject queryUserinfo(HttpServletRequest request);

	/**
	 * 查询公告头部所需信息
	 * @param request
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	public JSONObject headerInfo(HttpServletRequest request);

	/**
	 * 发送短信验证码    
	 * 从session中获取用户注册手机号码
	 * 不再需要其他验证(不需要验证图片验证码)
	 * @param context
	 * @param request
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	public JSONObject sendMsg(Context context, HttpServletRequest request);

	/**
	 * 修改支付密码
	 * @param context
	 * @param request
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	JSONObject userManager(Context context, HttpServletRequest request);
	/**
	 * 得到菜单
	 * @return
	 */
	JSONObject getMenu(HttpServletRequest request);

	/**
	 * 登录后未设置支付密码用户去设置支付密码
	 * @param context
	 * @param request
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	JSONObject setTpassword(Context context, HttpServletRequest request);

	/**
	 * 验证支付密码   并将已验证手机号码保存到session中
	 * manageType  M-修改；R-重置；A-新增；V-验证
	 * @param context
	 * @param request
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	JSONObject checkTPassword(Context context, HttpServletRequest request);

	/**
	 * 验证手机号码和返回手机短信验证码     需判断是否验证支付密码
	 * 修改用户注册手机号码页面
	 * @param context
	 * @param request
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	JSONObject getVrfCode(Context context, HttpServletRequest request);

	/**
	 * 修改手机号码并设置登录密码
	 * @param context
	 * @param request
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	JSONObject modifyRegMobileAndSetPsw(Context context, HttpServletRequest request);
	/**
	 * 验证支付密码
	 * @param context
	 * @param cmfUserId
	 * @param tpassword
	 * @param string
	 * @param manageType
	 * @param string2
	 * @param string3
	 * @return
	 * 			UserServiceMessage
	 * @author luos
	 */
	JSONObject manageTpasswordNew(Context context, String cmfUserId, String tpassword, String oldTpassword, String manageType, String tradeChannel, String tradeMark);
	/**
	 * 查询原始用户信息没有加密过得
	 * @param request
	 * @return
	 */
	JSONObject queryOriginalUserinfo(HttpServletRequest request);
	
	/**
	 * 查询用户是否需要进行风险测评
	 * @param request
	 * @return
	 */
	JSONObject queryIsNeedTest(HttpServletRequest request);

	/**
	 * 修改用户基本信息
	 * @param context
	 * @param cmfUserId 
	 * @param nation   国籍
	 * @param province 省份
	 * @param city 城市
	 * @param addr 详细地址
	 * @param voccode 职业代码
	 * @param dateOfBirth 出生日期
	 * @param taxResidentType 税收居民类型
	 * @param otherVocation 其他职业
	 * @return
	 */
	public UserServiceMessage updateCmfUserBaseInfo(Context context, String cmfUserId, String nation, String province, String city, String addr, String voccode, String dateOfBirth, String taxResidentType, String otherVocation);
	
	/**
	 * 查询用户保存的 税后居民 类型
	 * @param cmfUserId
	 * @return
	 */
	public String queryUserTaxInfoListByUserId(HttpServletRequest request);
	
	public void saveTaxInfoByUserInvtp(String custNo,String cmfUserId,String taxResidentType,String taxResidentData);

	/**
	 * 更新专业投资者消息标志
	 * @param cmfUserId
	 * @return
	 */
	public UserServiceMessage updateUserInvprtpAlert(String cmfUserId);
	
	/**
	 * 查询用户风险评测历史记录
	 * @param cmfUserId
	 * @param custno
	 * @return
	 */
	public String queryUserRiskHistory(HttpServletRequest request);
	
	/**
	 * 实名认证后 同步柜台专业投资者至电商 
	 * @param cmfUserId
	 * @return
	 */
	public UserServiceMessage realNameAfterUpdateInvprtpByEccType(String cmfUserId);
	
	/**
     * 根据客户号更新证件过期日
     * @param custno
     * @param idExpireDate
     * @return
     */
    public UserServiceMessage updateIdExpireDateByCustNo(String custno, String idExpireDate);
}
