package com.cmwa.ec.weixin.manager.business;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.dto.user.UserInfoExtendDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.weixin.dto.UserInfoexDto;


public interface UserInfoexManager {
	
	/**
	 * 用户登录
	 * @param request
	 * @param context
	 * @param loginChannel
	 * @param loginMark
	 * @return
	 */
	public JSONObject login(HttpServletRequest request, Context context, String loginChannel, String loginMark);
	
	
	/**
	 * 根据微信账号，查询绑定关系
	 * @param openid 微信账号
	 * @return 
	 */
	public UserInfoexDto queryUserinfoexQueryRelation(String openid,String stat);
	
	/****
	 * 修改
	 * @param dto
	 */
	public void updatecmfUserid(String openid ,String cmfuserid,String stat);
	
	/**
	 * 修改用户分组信息
	 * @param openId
	 * @param groupId
	 * @return
	 */
	public String modifyUserGroup(String openId,String groupId);
	
	
	/**
	 * 设置/修改/验证支付密码
	 * 
	 * @param cmfUserId [必填]用户ID
	 * @param tpassword [必填]新交易密码（密文）
	 * @param oldTpassword 旧交易密码（密文）
	 * @param manageType [必填]操作类别（M-修改；R-重置；A-新增；V-验证）
	 * @param tradeChannel 交易通道 	通道标识： 01-官网，02-小企业e家，03-淘宝，04-通联，05-京东，90-其他
	 * @param tradeMark 交易标识	最近交易标识: (01-WEB 02-APP 03-WEIXIN)
	 * @return
	 * 返回码\返回信息 返回码如下
	 * 操作交易成功	USR-1I00
	 * 用户已锁定	USR-1I01
	 * 旧密码错误，但未达到错误次数上限	USR-1I02
	 * 只能输入 M /R/A/V	USR-1I03
	 * 非交易用户	USR-1I95
	 * 修改失败	USR-1I96
	 * 系统运行时不可知异常	USR-1I99
	 * 无效ID	USR-1I97
	 * userId/tpassword/ oprType必填	USR-1I98
	 * 
	 * @author wudb
	 */
	public JSONObject manageTpassword(Context context, String cmfUserId,
			String tpassword, String oldTpassword, String manageType, String tradeChannel, String tradeMark);

	/**
	 * 验证手机号码
	 * @param context
	 * @param mobile 
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	public JSONObject verifyMobile(Context context, String mobile);

	/**
	 * 获取验证码
	 * </br>
	 * 验证手机号码的业务类型 0、注册 1、交易 2、绑定 3、账户手机验证 4、修改密码
	 * @param context
	 * @param mobile
	 * @param type  获取验证码类型 
	 * @param ip	ip地址
	 * @param seqId	sessionId
	 * @return json
	 * @author liury
	 * </br>
	 * date:2015-02-05
	 */
	public JSONObject verifyMobileAndGetVerifyCode(Context context, String mobile, String mOBILE_TYPE_0, String ipAddr, String seqId);

	/**
	 * 用户注册
	 * 
	 * @author zhangsk
	 * @version 1.0
	 * @param request
	 * @param context
	 * @param mobile		手机号码
	 * @param lPassword    登陆密码（密文）
	 * @param mobileStatus 手机号码验证状态
	 * @param loginChannel 登陆通道 lOGIN_CHANEL_04
	 * @param loginMark    登陆来源标示 lOGIN_NMARK_04
	 * @return JSONObject
	 * 
	 * 业务处理成功						0000
	 * 参数错误						USR-B003
	 * 手机已被使用 待绑定的手机号码已被使用		USR-A015
	 * @author maj
	 */
	public JSONObject registerNormalUserWithT(HttpServletRequest request,Context context,
			String mobile, String mobileStatus, String lPassword,String loginChannel, String loginMark);

	/**
	 * 用户鉴权，验证身份证号是否被使用
	 */
	public JSONObject checkIdNoByBankAuthentication(Context context,
			String cmfUserId, String idNo, String idType);

	/**
	 * 重置登录密码
	 * @param context
	 * @param mobile
	 * @param passWord
	 * @return
	 * 			JSONObject
	 * 返回如下 
	 * 成功					0000
	 * 手机号码未注册  找不到用户		USR-A024	
	 * 修改失败				USR-A022 		
	 * 参数错误
	 * @author maj
	 */
	public JSONObject resetUserPassword(Context context, String mobile, String passWord);
	
	/**
	 * 获取验证码,不到账户服务验证手机号码 鉴权/交易
	 * @param context
	 * @param mobile
	 * @param type
	 * @param bankNumber
	 * @param money
	 * @param bankName
	 * @return
	 */
	public JSONObject getVerifyCode(Context context,String mobile,String type,String bankNumber,String money,String bankName);
	
	/**
	 * 绑定微信，即微信端登录绑定
	 * @author liury
	 */
	public String bindWeixin(HttpServletRequest request,Context context);

	/**
	 * 修改用户手机号码
	 * @param context
	 * @param cmfUserId
	 * @param mobile
	 * @param lPassword
	 * @param operatorType
	 * @param channel 
	 * @return
	 * 			UserServiceMessage
	 * @author maj
	 */
	public UserServiceMessage modifyRegMobile(Context context, String cmfUserId, String mobile, String lPassword, String operatorType, String channel);
	
	/**
	 * 修改用户基本信息
	 * @param context
	 * @param cmfUserId 
	 * @param NATION   国籍()
	 * @param province 省份()
	 * @param city 城市()
	 * @param addr 详细地址()
	 * @param voccode 职业代码
	 * @return
	 */
	public UserServiceMessage updateCmfUserBaseInfo(Context context, String cmfUserId, String nation, String province, String city, String addr, String voccode, String dateOfBirth, String taxResidentType, String otherVocation);
	
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


	void subscribleOrUnSubUpdateUserState(String openid, String cmfuserid, String stat);
	
	/**
	 * 通过openId来判断用户是否做过适当性
	 * @param dto
	 * @return
	 */
	public UserInfoExtendDto queryRiskLevel(UserInfoExtendDto dto);

	public void syncCustserviceInfo(String openid,String cmfuserid);
	
	/**
     * 根据客户号更新证件过期日
     * @param custno
     * @param idExpireDate
     * @return
     */
    public UserServiceMessage updateIdExpireDateByCustNo(String custno, String idExpireDate);
}
