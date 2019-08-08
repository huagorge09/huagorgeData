package com.cmwa.ec.webapp.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebMethod;

import com.cmwa.ec.user.facade.UserIntegralService;
import com.cmwa.ec.user.facade.model.UserErrCode;
import com.cmwa.ec.user.facade.model.UserResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.base.util.Sequences;
import com.cmwa.ec.query.facade.WebQueryService;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.system.ParameterDto;
import com.cmwa.ec.user.facade.UserService;
import com.cmwa.ec.user.facade.dto.AppR1BlotterDto;
import com.cmwa.ec.user.facade.dto.AppointInfoDto;
import com.cmwa.ec.user.facade.dto.BuriedDataDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserOperateLogDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;
import com.cmwa.ec.user.facade.dto.UserTaxInfoDto;

public class UserServiceClient {
	
	private static Logger logger = Logger.getLogger(UserServiceClient.class.getName());
	
	private UserService userService;
	private WebQueryService webQueryService;
	
	public void setWebQueryService(WebQueryService webQueryService) {
		this.webQueryService = webQueryService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	/**
	 * 用户积分具体业务逻辑接口
	 */
	private UserIntegralService userIntegralService;

	public void setUserIntegralService(UserIntegralService userIntegralService) {
		this.userIntegralService = userIntegralService;
	}

	/**
	 * 查询登录用户信息
     * 
	 * @param context
	 * @param cmfUserId
     * @return UserServiceMessage
	 * @author maj
	 */
	public UserServiceMessage queryUserAndAccoRlaById(Context context, String cmfUserId) {
		UserServiceMessage userServiceMessage = userService.queryUserAndAccoRlaById(context, cmfUserId);
		return userServiceMessage;
	}

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
	public UserServiceMessage login(Context context, String loginNo, String lPassword, String loginType, String loginChannel, String loginMark) {
		UserServiceMessage userServiceMessage = userService.login(context, loginNo, lPassword, loginType, loginChannel, loginMark);
		return userServiceMessage;
	}

	@WebMethod
	public UserServiceMessage queryMeccLogin(Context context,String loginNo,String codeReCord,String loginChannel, String loginMark,String MeccuserId){
		UserServiceMessage userServiceMessage = userService.queryMeccLogin(context, loginNo,codeReCord,loginChannel,loginMark,MeccuserId);
		return userServiceMessage;
	}

	/**
	 * 管理支付密码
     * 
	 * @param context
	 * @param cmfUserId
	 * @param tpassword
	 * @param string
	 * @param manageType
	 * @param string2
	 * @param string3
     * @return UserServiceMessage
	 * @author maj
	 */
	public UserServiceMessage manageTpassword(Context context, String cmfUserId, String tpassword, String oldTpassword, String manageType, String tradeChannel, String tradeMark) {
		UserServiceMessage userServiceMessage = userService.manageTpassword(context, cmfUserId, tpassword, oldTpassword, manageType, tradeChannel, tradeMark);
		return userServiceMessage;
	}

	public UserServiceMessage registerNormalUserWithT(Context context, String mobile, String mobileStatus, String lPassword, String loginChannel, String loginMark) {
		return userService.registerNormalUserWithT(context, mobile, mobileStatus, lPassword, loginChannel, loginMark);
	}

	/***
	 * 验证手机号码
	 * 
     * 可用 0000 注册用户 USR-A000 已占用 USR-A017 系统运行时不可知异常 USR-8000 mobile必填 USR-B004
	 * 
	 * @param mobile
	 * @return
	 */
	public UserServiceMessage verifyMobile(Context context,String mobile){
		UserServiceMessage userServiceMessage = userService.verifyMobile(context,mobile);
		return userServiceMessage;
	}

	/***
	 * 证件验证
     * 
	 * @param 
	 * @return
	 */
	public UserServiceMessage identityCerification(Context context, String idNo, String idType) {
		return userService.identityCerification(context,idNo, idType);
	}

	/**
	 * 重置密码
     * 
	 * @param context
	 * @param mobile
	 * @param password
     * @return UserServiceMessage
	 * @author maj
	 */
	public UserServiceMessage resetUserPassword(Context context,String mobile,String password){
		UserServiceMessage userServiceMessage = userService.resetUserPassword(context, mobile, password);
		return userServiceMessage;
	}

	/**
	 * 修改手机号码
     * 
	 * @param context
	 * @param cmfUserId
	 * @param mobile
	 * @param lPassword
	 * @param operatorType
	 * @param tRADE_CHANEL_03
     * @return UserServiceMessage
	 * @author maj
	 */
	public UserServiceMessage modifyRegMobile(Context context, String cmfUserId, String mobile, String lPassword, String operatorType, String channel) {
		UserServiceMessage userServiceMessage = userService.modifyRegMobile(context, cmfUserId, mobile, lPassword, operatorType, channel);
		return userServiceMessage;
	}
	
	/**
	 * 修改用户基本信息
     * 
	 * @param context
	 * @param cmfUserId 
     * @param nation
     *            国籍
     * @param province
     *            省份
     * @param city
     *            城市
     * @param addr
     *            详细地址
     * @param voccode
     *            职业代码
     * @param dateOfBirth
     *            出生日期
     * @param taxResidentType
     *            税收居民类型
     * @param otherVocation
     *            其他职业
	 * @return
	 */
	public UserServiceMessage updateCmfUserBaseInfo(Context context, String cmfUserId, String nation, String province, String city, String addr, String voccode, String dateOfBirth, String taxResidentType, String otherVocation){
		UserServiceMessage userServiceMessage = userService.updateCmfUserBaseInfo(context, cmfUserId, nation, province, city, addr, voccode, dateOfBirth, taxResidentType, otherVocation);
		return userServiceMessage;
	}
	
	/**
	 * 新增 用户税收居民信息
     * 
	 * @param userTaxInfoDto
	 * @return
	 */
	public UserServiceMessage createUserTaxInfo(UserTaxInfoDto userTaxInfoDto){
		UserServiceMessage userServiceMessage = userService.createUserTaxInfo(userTaxInfoDto);
		return userServiceMessage;
	};

	/**
	 * 删除 用户税收居民信息
     * 
	 * @param userTaxInfoDto
	 * @return
	 */
	public UserServiceMessage delUserTaxInfo(UserTaxInfoDto userTaxInfoDto){
		UserServiceMessage userServiceMessage = userService.delUserTaxInfo(userTaxInfoDto);
		return userServiceMessage;
	};
	
	/**
	 * 将 用户的 税收居民信息 备份至 历史表
     * 
	 * @param userTaxInfoDto
	 * @return
	 */
	public UserServiceMessage createUserTaxInfoByBackUp(UserTaxInfoDto userTaxInfoDto){
		UserServiceMessage userServiceMessage = userService.createUserTaxInfoByBackUp(userTaxInfoDto);
		return userServiceMessage;
	};
	
	/**
	 * 用户税收居民信息保存入库
     * 
	 * @param userTaxInfoDto
	 * @return
	 */
	@SuppressWarnings({ "static-access", "unused" })
	public void saveUserTaxInfo(String custNo,String cmfUserId,String taxResidentType, JSONArray jsonArray){
		Context context = new Context();
		UserTaxInfoDto userTaxInfoDto = new UserTaxInfoDto();
		userTaxInfoDto.setCustNo(custNo);
		userTaxInfoDto.setCmfUserId(cmfUserId);
		userService.createUserTaxInfoByBackUp(userTaxInfoDto);
		userService.delUserTaxInfo(userTaxInfoDto);
		userService.deleteAppR1BlotterInfo(cmfUserId);
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			userTaxInfoDto = new UserTaxInfoDto();
			userTaxInfoDto = (UserTaxInfoDto)jsonObj.toBean(jsonObj, UserTaxInfoDto.class);
			userTaxInfoDto.setCmfUserId(cmfUserId);
			userTaxInfoDto.setCustNo(custNo);
			userService.createUserTaxInfo(userTaxInfoDto);
			saveAppR1BlotterInfo(cmfUserId, taxResidentType, userTaxInfoDto);
		}
	};
	
	/**
	 * 删除非居民涉税信息申请流水业务表数据
     * 
	 * @param cmfUserId
	 */
	public void deleteAppR1BlotterInfo(String cmfUserId){
		userService.deleteAppR1BlotterInfo(cmfUserId);
	};
	
	/**
	 * 税收居民类型保存到非居民涉税信息申请流水业务表
     * 
	 * @param cmfUserId
	 * @param taxResidentType
	 * @param userTaxInfoDto
	 */
	@SuppressWarnings("unchecked")
	public void saveAppR1BlotterInfo(String cmfUserId,String taxResidentType,UserTaxInfoDto userTaxInfoDto){
		Context context = new Context();
		if(!taxResidentType.equals("1")){
			AppR1BlotterDto appR1BlotterDto = new AppR1BlotterDto();
			appR1BlotterDto = userService.queryUserTaxInfo(cmfUserId);
			if(null != appR1BlotterDto){
				if(taxResidentType.equals("2")){
					appR1BlotterDto.setNonresiflag("1");
				}else if(taxResidentType.equals("3")){
					appR1BlotterDto.setNonresiflag("2");
				}
				appR1BlotterDto.setAppsheetserialno(Sequences.getPK());
				
				appR1BlotterDto.setEnglishfamliyname2(userTaxInfoDto.getEnglishSurname());
				appR1BlotterDto.setEnglishfirstname2(userTaxInfoDto.getEnglishName());
				
				String taxBirthNation = userTaxInfoDto.getTaxBirthNation();
				String taxBirthRegion = "";
				if(taxBirthNation.equals("1")){
					taxBirthRegion = "156";
				}else if(taxBirthNation.equals("2")){
					taxBirthRegion = "156-2";
				}else if(taxBirthNation.equals("3")){
					taxBirthRegion = "156-3";
				}else if(taxBirthNation.equals("4")){
					taxBirthRegion = "156-4";
				}else if(taxBirthNation.equals("5")){
					taxBirthRegion = userTaxInfoDto.getTaxBirthRegion();
				}
				QueryMessageDto messageDto = webQueryService.queryParameter(context  , "DS", "DS_NATION_CODE", taxBirthRegion, "");
				List<ParameterDto> listPara = (List<ParameterDto>) messageDto.getData();
				if(null != listPara && listPara.size() > 0){
					//出生地国家
					appR1BlotterDto.setBirthcountry(listPara.get(0).getPmv1());
				}
				
				//现居国家
				QueryMessageDto messageDto2 = webQueryService.queryParameter(context , "DS", "DS_NATION_CODE", userTaxInfoDto.getTaxResideRegion(), "");
				List<ParameterDto> listPara2 = (List<ParameterDto>) messageDto2.getData();
				if(null != listPara2 && listPara2.size() > 0){
					//出生地国家
					appR1BlotterDto.setLivingcountry(listPara2.get(0).getPmv1());
				}
				
				//现居地址
				appR1BlotterDto.setLivingaddress(userTaxInfoDto.getTaxResideAddress());
				//现居英文地址
				appR1BlotterDto.setLivingaddress3(userTaxInfoDto.getTaxResideAddressEnglish());
				
				QueryMessageDto queryMessageDto = webQueryService.queryParameter(context , "DS", "DS_NATION_CODE", userTaxInfoDto.getTaxArea(), "");
				List<ParameterDto> list = (List<ParameterDto>) queryMessageDto.getData();
				if(null != list && list.size() > 0){
					//税收居民国
					appR1BlotterDto.setTaxcountry(list.get(0).getPmv1());
				}
				//纳税人识别号
				appR1BlotterDto.setTaxid(userTaxInfoDto.getTaxPayerCode());
				//无识别号原因
				String taxNotCodeCause = userTaxInfoDto.getTaxNotCodeCause();
				taxNotCodeCause = taxNotCodeCause == null ? "" : taxNotCodeCause;
				appR1BlotterDto.setSpecification(taxNotCodeCause.equals("1")?"0":userTaxInfoDto.getTaxnotGetCause());
				
				//调查规则  0：新开账户 1：存量账户
				String surveymethodVal = appR1BlotterDto.getOriginalappdate();
				if(null == surveymethodVal || surveymethodVal.equals("")){
					surveymethodVal = "0";
				}
				String surveymethod  = "";
				try {
					if(Integer.parseInt(surveymethodVal) > Integer.parseInt("20170701")){
						surveymethod   = "0";
					}else{
						surveymethod = "1";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				appR1BlotterDto.setSurveymethod(surveymethod);
				userService.insertAppR1BlotterInfo(appR1BlotterDto);
			}
		}
	}
	
	/**
	 * 用户风险测评后 获取 用户 信息
     * 
	 * @param dataMap
	 * @return
	 */
	public Map<String, String> queryParamByUserRiskLevelSucc(Map<String, String> dataMap){
		Map<String, String> resultMap = new HashMap<String, String>();
		resultMap = userService.queryParamByUserRiskLevelSucc(dataMap);
		return resultMap;
	};
	
	/**
	 * 查询用户保存的 税后居民 类型
     * 
	 * @param cmfUserId
	 * @return
	 */
	public List<UserTaxInfoDto> queryUserTaxInfoListByUserId(String cmfUserId){
		return userService.queryUserTaxInfoListByUserId(cmfUserId);
	};
	
	/**
	* 方法说明：根据用户cmfUserId查询用户最新测评时间
     * 
     * @param context
     *            日志工具类（日志打印基本信息）
     * @param cmfUserId
     *            [必填]用户ID
     * @return UserServiceMessage 返回码\返回信息\用户基本信息Dto 返回码如下 查询成功 0000 userId必填
     *         USR-B005 无效ID USR-A018 系统运行时不可知异常 USR-8000
	*/
	public UserServiceMessage queryUserRiskEvalDateByCmfUserId(Context context, String cmfUserId){
		return userService.queryUserRiskEvalDateByCmfUserId(context, cmfUserId);
	}
	
	/**
	 * 方法说明：同步专业投资者到 柜台表
	 * 
	 * @author ex-liuy
	 * @version 1.0
	 * @param context 
	 * 				日志工具类（日志打印基本信息）
	 * @param cmfUserId
	 *            [必填]用户ID
	 * @return UserServiceMessage 返回码\返回信息\用户基本信息Dto 返回码如下
	 * 
	 * 
     *         查询成功 0000 </pre>
	 */
	public UserServiceMessage syncInvprtpToEcc(Context context, String cmfUserId,String custNo,String invprtpScore){
		return userService.syncInvprtpToEcc(context, cmfUserId, custNo,invprtpScore);
	} 

	/**
	 * 更新专业投资者消息标志
     * 
	 * @param cmfUserId
	 * @return
	 */
	public UserServiceMessage updateUserInvprtpAlert(String cmfUserId){
		return userService.updateUserInvprtpAlert(cmfUserId);
	};

	/**
	 * 查询用户风险评测历史记录
     * 
	 * @param cmfUserId
	 * @param custno
	 * @return
	 */
	public List<UserBaseInfoDto> queryUserRiskHistory(String cmfUserId,String custno){
		List<UserBaseInfoDto> baseInfoDto = new ArrayList<UserBaseInfoDto>();
		baseInfoDto = userService.queryUserRiskHistory(cmfUserId, custno);
		return baseInfoDto;
	}
	
	/**
	 * 实名认证后 同步柜台专业投资者至电商 
     * 
	 * @param cmfUserId
	 * @return
	 */
	public UserServiceMessage realNameAfterUpdateInvprtpByEccType(String cmfUserId){
		return userService.realNameAfterUpdateInvprtpByEccType(cmfUserId);
	};
	
	/**
     * 风险测评后 将 是否控制关系 受益人 诚信记录 更新至用户表 方便购买产品时 限制 不用每次购买都需要解析 答案
     * 
	 * @param userBaseInfoDto
	 */
	public UserServiceMessage updateUserProperInfoByRiskLevel(UserBaseInfoDto userBaseInfoDto){
		return userService.updateUserProperInfoByRiskLevel(userBaseInfoDto);
	};
	
	/**
	 * 风险测评后 更新特殊用户风险等级
     * 
	 * @param userBaseInfoDto
	 */
	public UserServiceMessage updateUserSpecialRiskLevelInfo(UserBaseInfoDto userBaseInfoDto){
		return userService.updateUserSpecialRiskLevelInfo(userBaseInfoDto);
	};
	
	/**
	 * 查询交易账号记录 - 是否存在
     * 
	 * @param tradeacco
	 * @return
	 */
	public int queryTradeaccoExistCount(String tradeacco){
		return userService.queryTradeaccoExistCount(tradeacco);
	}
	
	/**
	 * 埋点
     * 
	 * @param buriedDataDto
	 */
	public boolean insertBuriedData(BuriedDataDto buriedDataDto){
		boolean flag=false;
		try {
			flag=userService.insertBuriedData(buriedDataDto);
		} catch (Exception e) {
			logger.error(e);
		}
		return	flag;
			
	}
	
	/**
	 * 保存用户预约咨询信息
     * 
	 * @param dto
	 * @return
	 */
	public JSONObject insertAppointInfo(AppointInfoDto dto){
		boolean flag = false;
		JSONObject returnObj = new JSONObject(); 
		try{
			userService.insertAppointInfo(dto);
			flag = true;
		} catch (Exception e){
			logger.error("UserServiceClient:insertAppointInfo方法异常:"+e);
		}
		returnObj.put("msg", flag);
		return returnObj;
	}

    /**
     * 
     * @Title: insertUserOperateLog
     * @Description: 插入用户操作日志
     * @param userOperateLogDto
     */
    public void insertUserOperateLog(UserOperateLogDto userOperateLogDto) {
        try {
            userService.insertUserOperateLog(userOperateLogDto);
        } catch (Exception e) {
            logger.error("----insertUserOperateLog->>>插入用户操作日志时异常", e);



        }
    }
	/**
	 * 当远程调用失败构建的系统错误消息传输对象
	 */
	private static UserResult errUserResult = UserResult.failed(UserErrCode.ERR_1004);

	/**
	 * 获取用户积分相关信息
	 *
	 * @param cmfUserId
	 * @return
	 */
	public UserResult<?> getUserIntegralInfo(String cmfUserId) {
		UserResult<?> result = userIntegralService.getUserIntegralInfo(cmfUserId);
		return (null == result ? errUserResult : result);
	}

	/**
	 * 获取用户是否有推荐人
	 *
	 * @param cmfUserId
	 * @return
	 */
	public UserResult<?> haveReferrer(String cmfUserId) {
		UserResult<?> result = userIntegralService.haveReferrer(cmfUserId);
		return (null == result ? errUserResult : result);
	}

	/**
	 * 获取用户积分详细信息(流水)
	 *
	 * @param cmfUserId
	 * @return
	 */
	public UserResult<?> listUserIntegralDetail(String cmfUserId) {
		UserResult<?> result = userIntegralService.listUserIntegralDetail(cmfUserId);
		return (null == result ? errUserResult : result);
	}

	/**
	 * 为用户设置推荐人
	 *
	 * @param userCmfUserId
	 * @param invitationCode
	 * @return
	 */
	public UserResult<?> updateUserReferrer(String userCmfUserId, String invitationCode) {
		UserResult<?> result = userIntegralService.updateUserReferrer(userCmfUserId, invitationCode);
		return (null == result ? errUserResult : result);
	}

	/**
	 * 通过手机号为用户设置推荐人
	 *
	 * @param userCmfUserId
	 * @param phoneNumber
	 * @return
	 */
	public UserResult<?> updateUserReferrerByPhoneNumber(String userCmfUserId, String phoneNumber) {
		UserResult<?> result = userIntegralService.updateUserReferrerByPhoneNumber(userCmfUserId, phoneNumber);
		return (null == result ? errUserResult : result);
	}

	/**
	 * 通过姓名和手机号创建一条延时绑定的积分信息
	 *
	 * @param customerName
	 * @param customerMobile
	 * @param referrerCmfUserId
	 * @return
	 */
	public UserResult<?> insertDelayIntegralInfo(String customerName, String customerMobile, String referrerCmfUserId) {
		UserResult<?> result = userIntegralService.insertDelayIntegralInfo(customerName, customerMobile, referrerCmfUserId);
		return (null == result ? errUserResult : result);
	}

	/**
	 * 通过A用户的id和已注册用户B的手机号查询是否重复推荐
	 * @param referrerCmfUserId
	 * @param customerMobile
	 * @return
	 */
	public UserResult<?> isRepeatRecommRegisterUser(String referrerCmfUserId, String customerMobile) {
		UserResult<?> result = userIntegralService.isRepeatRecommRegisterUser(referrerCmfUserId, customerMobile);
		return (null == result ? errUserResult : result);
	}
	
	/**
     * 根据客户号更新证件过期日
     * @param custno
     * @param idExpireDate
     * @return
     */
    public UserServiceMessage updateIdExpireDateByCustNo(String custno, String idExpireDate) {
    	return userService.updateIdExpireDateByCustNo(custno, idExpireDate);
    }
}
