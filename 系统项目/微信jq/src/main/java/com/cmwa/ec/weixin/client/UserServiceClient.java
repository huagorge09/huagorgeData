package com.cmwa.ec.weixin.client;

import java.util.ArrayList;
import java.util.List;

import javax.jws.WebMethod;

import com.cmwa.ec.user.facade.UserIntegralService;
import com.cmwa.ec.user.facade.model.UserErrCode;
import com.cmwa.ec.user.facade.model.UserResult;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
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

/**
 * 账户服务客户端服务类
 * 
 * @author liury
 * 
 */
public class UserServiceClient {
	
	private static Logger logger = Logger.getLogger(UserServiceClient.class);
	private UserService userService;

	/**
	 * 用户积分具体业务逻辑接口
	 */
	private UserIntegralService userIntegralService;

	public void setUserIntegralService(UserIntegralService userIntegralService) {
		this.userIntegralService = userIntegralService;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
	private WebQueryService webQueryService;

	public void setWebQueryService(WebQueryService webQueryService) {
		this.webQueryService = webQueryService;
	}

	/***
	 * 验证证件
     * 
     * @param idNo
     *            证件号码
     * @param idType
     *            证件类型
	 * @return
	 */
	public UserServiceMessage identityCerification(Context context,String idNo, String idType) {
       return userService.identityCerification(context,idNo, idType);
	}
	
	/***
	 * 验证手机号码
     * 
	 * @param mobile
	 * @return
	 */
	public UserServiceMessage verifyMobile(Context context,String mobile){
	  return userService.verifyMobile(context,mobile);
	}
	
	/***
	 * 
     * @param loginNo
     *            [必填] 登陆账号
     * @param lPassword
     *            [必填] 登陆密码（密文）
     * @param loginType
     *            [非必填] 登陆类型
     * @param loginChannel
     *            [必填] 登陆通道（默认基金易Web） 通道标识：
     *            01-官网，02-小企业e家，03-淘宝，04-通联，05-京东，90-其他
     * @param loginMark
     *            [必填] 登陆来源标示（默认基金易） 标示：01-WEB，02-APP，03-WEIXIN
     * @return UserServiceMessage 返回码\返回信息\用户基本信息Dto，含密码错误次数\用户账户对应Dto 返回码如下
     *         用户输入信息验证通过 0000 引导绑定手机号码 USR-A003 提示是否绑定代销资料 USR-A004 用户登录已锁定
     *         USR-A005 系统根据用户输入查找出两个以上的用户 USR-A006 登录用户未注册 USR-A007
     *         用户类型不在“10-注册用户，20-查询用户，30-交易用户”之中 USR-A008 密码错误，但未达到错误次数上限
     *         USR-A009 无效ID USR-A010 系统运行时不可知异常 USR-8000
	 * LoginNo/LPassword/LoginChannel/LoginMark必填		USR-B001
     */
    public UserServiceMessage login(Context context, String loginNo, String lPassword, String loginType, String loginChannel, String loginMark) {
		// TODO Auto-generated method stub
		return userService.login(context,loginNo, lPassword, loginType, loginChannel, loginMark);
	}
	
	/**
	 * 类的功能说明：用户注册,仅仅登记手机号码，
	 * 
	 * @author zhangsk
	 * @version 1.0
     * @param lPassword
     *            登陆密码（密文）
     * @param mobile
     *            手机号码
     * @param mobileStatus
     *            手机号码验证状态
     * @param loginChannel
     *            登陆通道（默认基金易Web）
     * @param loginMark
     *            登陆来源标示（默认基金易）
	 * @return UserServiceMessage
	 * 
     *         业务处理成功 0000 参数错误 USR-B003 手机已被使用 待绑定的手机号码已被使用 USR-A015
	 */
	@WebMethod
    public UserServiceMessage registerNormalUserWithT(Context context, String mobile, String mobileStatus, String lPassword, String loginChannel, String loginMark) {
		return userService.registerNormalUserWithT(context, mobile, mobileStatus, lPassword, loginChannel, loginMark);
	}
	
	/**
	 * 类的功能说明：10用户更新姓名，证件类型、证件号码等信息， 改方法会促发更新用户为 20 用户，
	 * 
	 * @author zhangsk
	 * @version 1.0
     * @param cmfUserId
     *            用户的CMFUSERID
     * @param custName
     *            用户姓名
     * @param idType
     *            用户证件类型
     * @param idNo
     *            用户证件号码
     * @param isUpt2Qry
     *            是否升级为查询用户
	 * @return UserServiceMessage
	 * 
     *         返回如下 成功 0000 已注册用户提示 找到匹配的已注册用户，提示“已注册，请登录" USR-A014 数据异常1
     *         通过三要素找到两个以上的未注册代销用户 数据异常2 通过三要素找到两个以上的已注册代销或已注册直销用户 USR-A016
	 */
	@WebMethod
    public UserServiceMessage updateNormalUserIDInfosWithT(Context context, String cmfUserId, String custName, String idType, String idNo, boolean isUpt2Qry) {
		return userService.updateNormalUserIDInfosWithT(context, cmfUserId, custName, idType, idNo, isUpt2Qry);
	}
	
	/**
	 * 修改登录密码
	 * 
	 * @param context 
	 * 				[必填]日志工具类（日志打印基本信息）
	 * @param cmfUserId
	 *            [必填]用户id（密文）
	 * @param lpassword
	 *            [必填] 登录密码（密文）
	 * @param oldLpassword
	 *            旧登录密码
	 * @param oprType
	 *            [必填]操作类别（M-修改；R-重置）
	 * @return UserServiceMessage 返回码\返回信息 返回码如下
	 * 
	 *         <pre>
	 * 
	 * 修改成功						0000
	 * 无效ID							USR-A019
	 * 用户已锁定						USR-A020
	 * 旧密码错误，但未达到错误次数上限		USR-A021
	 * 修改失败						USR-A022
	 * 只能输入 M /R					USR-A023
	 * userId/lpassword/oprType必填	USR-B006
	 * 原始密码不能为空					USR-B007
	 * 系统运行时不可知异常				USR-8000
	 * </pre>
	 */
	public UserServiceMessage updateUserLPassword(Context context,String cmfUserId,String lpassword,String oldLpassword,String oprType){
		
		return userService.updateUserPwd(context, cmfUserId, lpassword, oldLpassword, oprType);
	}
	
	/**
	 * 类的功能说明：根据手机号码修改用户登陆密码
	 * 
	 * @author liury
	 * @version 1.0
     * @param mobile
     *            用户注册时提供的手机号码
     * @param password
     *            新登陆密码
	 * @return UserServiceMessage
	 * 
     *         返回如下 成功 0000 手机号码未注册 找不到用户 USR-A024 修改失败 USR-A022 参数错误 USR-B006
	 */
	public UserServiceMessage resetUserPassword(Context context,String mobile,String password){
		
		return userService.resetUserPassword(context, mobile, password);
	}

	/**
	 * 设置/修改/验证支付密码
	 * 
     * @param cmfUserId
     *            [必填]用户ID
     * @param tpassword
     *            [必填]新交易密码（密文）
     * @param oldTpassword
     *            旧交易密码（密文）
     * @param manageType
     *            [必填]操作类别（M-修改；R-重置；A-新增；V-验证）
     * @param tradeChannel
     *            交易通道 通道标识： 01-官网，02-小企业e家，03-淘宝，04-通联，05-京东，90-其他
     * @param tradeMark
     *            交易标识 最近交易标识: (01-WEB 02-APP 03-WEIXIN)
     * @return UserServiceMessage
	 * 
     *         返回码\返回信息 返回码如下 操作交易成功 USR-1I00 用户已锁定 USR-1I01 旧密码错误，但未达到错误次数上限
     *         USR-1I02 只能输入 M /R/A/V USR-1I03 非交易用户 USR-1I95 修改失败 USR-1I96
     *         系统运行时不可知异常 USR-1I99 无效ID USR-1I97 userId/tpassword/ oprType必填
     *         USR-1I98
	 * 
	 * @author maj
	 */
	public UserServiceMessage manageTpassword(Context context, String cmfUserId, String tpassword, String oldTpassword, String manageType, String tradeChannel, String tradeMark) {
		UserServiceMessage userServiceMessage = userService.manageTpassword(context, cmfUserId, tpassword, oldTpassword, manageType, tradeChannel, tradeMark);
		return userServiceMessage;
	}
	
	/**
	 * 修改用户注册手机号码
     * 
	 * @param context
     * @param cmfUserId
     *            用户id（必填）
     * @param newMobile
     *            更换的新手机号码（必填）
     * @param lPassword
     *            新密码 （Type为2时不可为空）
     * @param operatorType
     *            操作类型 1：修改手机号码未被使用，直接修改手机号码 2：修改的手机号码已被注册，但未鉴权，修改手机号码且修改登陆密码
     * @param channel
     *            操作渠道(01-WEB 02-APP 03-WEIXIN)（必填）
	 * @return	UserServiceMessage 返回码\返回信息 返回码如下
	 * 
	 * <pre>
	 * 	操作交易成功	0000
	 * 	手机号码已被使用	USR-A017
	 *  用户不存在，根据cmfUserId未找到用户  USR-A026
	 * 	参数错误  		USR-B001
	 * 	系统运行时异常	USR-8000，USR-A099
	 * </pre>
	 */
	@WebMethod
    public UserServiceMessage modifyRegMobile(Context context, String cmfUserId, String newMobile, String lPassword, String operatorType, String channel) {
		UserServiceMessage userServiceMessage = userService.modifyRegMobile(context, cmfUserId, newMobile,lPassword,operatorType,channel);
		return userServiceMessage;
	}
	
	/**
	 * mecc通过登陆账号登陆
     * 
	 * @param context
     * @param MeccuserId
     *            mecc登陆ID(必填)
     * @param loginNo
     *            登陆账号(必填)
     * @param codeReCord
     *            code代码记录(必填)
     * @param loginChannel
     *            登陆渠道(必填)
     * @param loginMark
     *            登陆来源标示(必填)
	 * @return
	 * 
     *         返回码如下 用户输入信息验证通过 0000 用户登录已锁定 USR-A005 系统根据用户输入查找出两个以上的用户
     *         USR-A006 登录用户未注册 USR-A007 用户类型不在“10-注册用户，20-查询用户，30-交易用户”之中
     *         USR-A008 无效ID USR-A010 系统运行时不可知异常 USR-8000 loginNo必填 USR-B004
	 */
	@WebMethod
	public UserServiceMessage queryMeccLogin(Context context,String loginNo,String codeReCord,String loginChannel, String loginMark,String MeccuserId){
		UserServiceMessage userServiceMessage = userService.queryMeccLogin(context, loginNo,codeReCord,loginChannel,loginMark,MeccuserId);
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
    public UserServiceMessage updateCmfUserBaseInfo(Context context, String cmfUserId, String nation, String province, String city, String addr, String voccode,
            String dateOfBirth, String taxResidentType, String otherVocation) {
		UserServiceMessage userServiceMessage = userService.updateCmfUserBaseInfo(context, cmfUserId, nation, province, city, addr, voccode, dateOfBirth, taxResidentType, otherVocation);
		return userServiceMessage;
	}
	
	/**
	* 方法说明：根据用户cmfUserId查询用户最新测评时间
     * 
     * @param cmfUserId
     *            [必填]用户ID
     * @return UserServiceMessage 返回码\返回信息\用户基本信息Dto 返回码如下 查询成功 0000 userId必填
     *         USR-B005 无效ID USR-A018 系统运行时不可知异常 USR-8000
	*/
	public UserServiceMessage queryUserRiskEvalDateByCmfUserId(String cmfUserId){
		return userService.queryUserRiskEvalDateByCmfUserId(new Context(), cmfUserId);
	}
	
	/**
	 * 更新专业投资者消息标志
     * 
	 * @param cmfUserId
	 * @return
	 */
	public UserServiceMessage updateUserInvprtpAlert(String cmfUserId){
		return userService.updateUserInvprtpAlert(cmfUserId);
	}
	
	/**
	 * 查询用户保存的 税后居民 类型
     * 
	 * @param cmfUserId
	 * @return
	 */
	public List<UserTaxInfoDto> queryUserTaxInfoListByUserId(String cmfUserId){
		return userService.queryUserTaxInfoListByUserId(cmfUserId);
	}
	
	public void saveTaxInfoByUserRiskLevel(String custNo,String cmfUserId,String taxResidentType,String taxResidentData){
		//类型为空 或者 类型非为仅中国并且数据为空 
		if(StringUtils.isEmpty(taxResidentType) || StringUtils.isEmpty(taxResidentData) ||(!"1".equals(taxResidentType) && StringUtils.isEmpty(taxResidentData))){
			return ;
		}
		JSONArray jsonArray = new JSONArray();
		try{
			jsonArray = JSONArray.fromObject(taxResidentData);
		}catch(Exception e){
			logger.error("----UserServiceClient-saveTaxInfoByUserInvtp-JSONArray.fromObject-Exception-cmfUserId:"+cmfUserId+";taxResidentType="+taxResidentType);
			logger.error("----UserServiceClient-saveTaxInfoByUserInvtp-JSONArray.fromObject-Exception:"+taxResidentData);
			logger.error("----UserServiceClient-saveTaxInfoByUserInvtp-JSONArray.fromObject-Exception:",e);
			e.printStackTrace();
			return;
		}
		if(jsonArray.size() <= 0){
			return ;
		}
		try{
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
		}catch(Exception e){
			logger.error("----UserServiceClient-saveTaxInfoByUserRiskLevel-Exception:",e);
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除非居民涉税信息申请流水业务表数据
     * 
	 * @param cmfUserId
	 */
	public void deleteAppR1BlotterInfo(String cmfUserId){
		userService.deleteAppR1BlotterInfo(cmfUserId);
	}
	
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
                QueryMessageDto messageDto2 = webQueryService.queryParameter(context, "DS", "DS_NATION_CODE", userTaxInfoDto.getTaxResideRegion().equals("1") ? "156"
                        : userTaxInfoDto.getTaxResideRegion(), "");
				List<ParameterDto> listPara2 = (List<ParameterDto>) messageDto2.getData();
				if(null != listPara2 && listPara2.size() > 0){
					//出生地国家
					appR1BlotterDto.setLivingcountry(listPara2.get(0).getPmv1());
				}
				
				//现居地址
				appR1BlotterDto.setLivingaddress(userTaxInfoDto.getTaxResideAddress());
				//现居英文地址
				appR1BlotterDto.setLivingaddress3(userTaxInfoDto.getTaxResideAddressEnglish());
				
                QueryMessageDto queryMessageDto = webQueryService.queryParameter(context, "DS", "DS_NATION_CODE",
                        userTaxInfoDto.getTaxArea().equals("1") ? "156" : userTaxInfoDto.getTaxArea(), "");
				List<ParameterDto> list = (List<ParameterDto>) queryMessageDto.getData();
				if(null != list && list.size() > 0){
					//税收居民国
					appR1BlotterDto.setTaxcountry(list.get(0).getPmv1());
				}
				//纳税人识别号
				appR1BlotterDto.setTaxid(userTaxInfoDto.getTaxPayerCode());
				//无识别号原因
				String taxNotCodeCause = userTaxInfoDto.getTaxNotCodeCause();
				taxNotCodeCause = taxNotCodeCause ==null ? "" : taxNotCodeCause;
				appR1BlotterDto.setSpecification(taxNotCodeCause.equals("1") ? "0" : userTaxInfoDto.getTaxnotGetCause());
				
				//调查规则  0：新开账户 1：存量账户
				String surveymethodVal = appR1BlotterDto.getOriginalappdate();
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
	public UserServiceMessage syncInvprtpToEcc(Context context, String cmfUserId,String custNo, String invprtpScore){
		return userService.syncInvprtpToEcc(context, cmfUserId, custNo,invprtpScore);
	} 
	
	/**
	 * 实名认证后 同步柜台专业投资者至电商 
     * 
	 * @param cmfUserId
	 * @return
	 */
	public UserServiceMessage realNameAfterUpdateInvprtpByEccType(String cmfUserId){
		return userService.realNameAfterUpdateInvprtpByEccType(cmfUserId);
	}
	
	/**
     * 风险测评后 将 是否控制关系 受益人 诚信记录 更新至用户表 方便购买产品时 限制 不用每次购买都需要解析 答案
     * 
	 * @param userBaseInfoDto
	 */
	public UserServiceMessage updateUserProperInfoByRiskLevel(UserBaseInfoDto userBaseInfoDto){
		return userService.updateUserProperInfoByRiskLevel(userBaseInfoDto);
	}
	
	/**
	 * 风险测评后 更新特殊用户风险等级
     * 
	 * @param userBaseInfoDto
	 */
	public UserServiceMessage updateUserSpecialRiskLevelInfo(UserBaseInfoDto userBaseInfoDto){
		return userService.updateUserSpecialRiskLevelInfo(userBaseInfoDto);
	}
	
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
     * 查询用户 专业投资者 申请的记录数 目前只支持 客户号 查询
     * 
	 * @param cmfUserId 
	 * @param custNo
	 * @return
	 */
	public int queryApplyInvtpCount(String cmfUserId ,String custNo){
		return userService.queryApplyInvtpCount(cmfUserId,custNo);
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
	 * 新增预约信息记录
     * 
	 * @param dto
	 * @throws Exception
	 */
	public JSONObject insertAppointInfo(AppointInfoDto dto) {
		JSONObject rst = new JSONObject();
		rst.put("success", true);
		rst.put("msg", "您已成功预约");
		AppointInfoDto infoDtoDB = userService.queryAppointInfo(dto);
		if (null == infoDtoDB) {
			try {
				userService.insertAppointInfo(dto);
			} catch (Exception e) {
				rst.put("success", false);
				rst.put("msg", "新增预约信息记录出错了");
				logger.error("新增预约信息记录出错了...{}",e);
				e.printStackTrace();
			}
		}
		return rst;
	}

	public UserServiceMessage queryUserAndAccoRlaById(Context context, String cmfUserId) {
		UserServiceMessage queryUserAndAccoRlaById = userService.queryUserAndAccoRlaById(context, cmfUserId);
		return queryUserAndAccoRlaById;
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
	 * 通过手机号查询用户信息
	 * @param mobile
	 * @return
	 */
	public UserServiceMessage queryUserInfoByMobile(String mobile) {
		return userService.queryUserInfoByMobile(mobile);
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
