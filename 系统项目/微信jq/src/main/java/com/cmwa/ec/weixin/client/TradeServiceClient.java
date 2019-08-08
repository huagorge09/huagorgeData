package com.cmwa.ec.weixin.client;

import java.util.List;
import java.util.Map;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoDto;
import com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoStatusDto;
import com.cmwa.ec.trade.facade.ActivityService;
import com.cmwa.ec.trade.facade.AmazonS3Service;
import com.cmwa.ec.trade.facade.CmwaAccreditedInvestorService;
import com.cmwa.ec.trade.facade.FundTradeService;
import com.cmwa.ec.trade.facade.IntegralTradeService;
import com.cmwa.ec.trade.facade.UserAcctTradeService;
import com.cmwa.ec.trade.facade.dto.AmazonResult;
import com.cmwa.ec.trade.facade.dto.CmfWXPushmessageDto;
import com.cmwa.ec.trade.facade.dto.OrderResult;
import com.cmwa.ec.trade.facade.dto.fund.FundTradeDto;
import com.cmwa.ec.trade.facade.dto.fund.SignEContractDto;
import com.cmwa.ec.trade.facade.dto.log.CmwaFileUploadRecordDto;
import com.cmwa.ec.trade.facade.dto.user.UserAcctDto;
import com.cmwa.ec.trade.facade.dto.user.UserRiskLevelDto;
import com.cmwa.ec.trade.facade.model.TradeErrCode;
import com.cmwa.ec.trade.facade.model.TradeResult;


/**
 * 交易模块客户端服务类
 * @author liury
 *
 */

public class TradeServiceClient {

	
	private FundTradeService fundTradeService; 

	private UserAcctTradeService userAcctTradeService;
	
	private ActivityService activityService;
	
	private AmazonS3Service amazonS3Service;
	
	private CmwaAccreditedInvestorService cmwaAccreditedInvestorService;
	
	public FundTradeService getFundTradeService() {
		return fundTradeService;
	}
	public void setFundTradeService(FundTradeService fundTradeService) {
		this.fundTradeService = fundTradeService;
	}
	
	public UserAcctTradeService getUserAcctTradeService() {
		return userAcctTradeService;
	}

	public void setUserAcctTradeService(UserAcctTradeService userAcctTradeService) {
		this.userAcctTradeService = userAcctTradeService;
	}

	public ActivityService getActivityService() {
		return activityService;
	}

	public void setActivityService(ActivityService activityService) {
		this.activityService = activityService;
	}
	
	/**
	 * @return the amazonS3Service
	 */
	public AmazonS3Service getAmazonS3Service() {
		return amazonS3Service;
	}
	/**
	 * @param amazonS3Service the amazonS3Service to set
	 */
	public void setAmazonS3Service(AmazonS3Service amazonS3Service) {
		this.amazonS3Service = amazonS3Service;
	}
	/**
	 * @return the cmwaAccreditedInvestorService
	 */
	public CmwaAccreditedInvestorService getCmwaAccreditedInvestorService() {
		return cmwaAccreditedInvestorService;
	}
	/**
	 * @param cmwaAccreditedInvestorService the cmwaAccreditedInvestorService to set
	 */
	public void setCmwaAccreditedInvestorService(
			CmwaAccreditedInvestorService cmwaAccreditedInvestorService) {
		this.cmwaAccreditedInvestorService = cmwaAccreditedInvestorService;
	}
	/**
	 * 用户鉴权信息，开通银行账户
	 * @param context
	 * @param userAcctDto
	 * @return
	 */
	public OrderResult openAccount(Context context, UserAcctDto userAcctDto){
		
		return userAcctTradeService.openAccount(context, userAcctDto);
	}
	
	
	/**
	 * 用户添加银行卡，开通银行账户
	 * @param context
	 * @param userAcctDto
	 * @return
	 */
	public OrderResult addBankNumber(Context context, UserAcctDto userAcctDto){
		
		return userAcctTradeService.addTradeAccount(context, userAcctDto);
	}
	
	/**
	 * 撤销已绑定的银行卡
	 * @param context
	 * @param userAcctDto
	 * @return
	 */
	public OrderResult cancelBindBankCard(Context context, UserAcctDto userAcctDto){
		
		OrderResult orderResult =userAcctTradeService.cancelTradeAccount(context, userAcctDto);
		return orderResult;
	}
	
	/**
	* 下预约单
	* @param context	会话信息
	* @param dto		购买要素
	* 
	* apkind(交易类型)  字典表：
	* 						020	产品认购
	* 						022	产品申购
	* 						920	产品预认购
	* 						922	产品预申购
	* 						A2T	产品预约转为正式购买
	* 						UPT	修改产品预约信息，目前只修改金额
	* 						CNL	取消当前预约单
	* 						APO	预下单
	* @return			接口，data.type = FundTradeDto 	
	* @throws 
	* @author wudb
	 */
	public OrderResult fundAttention(Context context, FundTradeDto dto) {
		OrderResult orderResult =fundTradeService.fundAppointV2(context, dto);
		return orderResult;
	}
	
	
	/**
	* 下交易单
	* @param context	会话信息
	* @param dto		购买要素
	* @param ecDto		电子合同
	* @other PayType=(0,1), apkind=A2T
	* @return 接口，data.type = FundTradeDto 	
	* @throws 
	* @author wudb
    */
	public OrderResult fundTrade(Context context, FundTradeDto dto, SignEContractDto ecDto){
		OrderResult orderResult =fundTradeService.fundAppointConfirm(context, dto, ecDto);
		return orderResult;
	}

	/**
	 * 设置用户风险评测级别
	 * @param context
	 * @param userAcctDto
	 * @return
	 */
	public OrderResult riskRating(Context context, UserRiskLevelDto userRiskLevelDto){
		return userAcctTradeService.setUserRiskLevel(context, userRiskLevelDto);
	}

	/**
	  * 设置用户风险等级-注册前的风险评测
	  * @param context
	  * @param userRiskLevelDto
	  * @return
	 */
	public OrderResult setCustRiskLevelBeforeOpenAccount(Context context, UserRiskLevelDto userAcctDto){
		return userAcctTradeService.setCustRiskLevelBeforeOpenAccount(context, userAcctDto);
	}
	/**
	 * 信批已读保存
	 * @param context
	 * @param cmfUserId
	 * @param fundId
	 * @param reportId
	 * @return
	 * 			OrderResult
	 * @author maj
	 */
	public OrderResult addReportReadRecord(Context context, String cmfUserId, String fundId, String reportId) {
		return fundTradeService.addReportReadRecord(context, cmfUserId, fundId, reportId);
	}
	
	/**
	 * 取消订单
	 * @param context
	 * @param fundTradeDto
	 * @return
	 * 			OrderResult
	 * @author maj
	 */
	public OrderResult cancelAppointRequest(Context context, FundTradeDto fundTradeDto) {
		return fundTradeService.cancelAppointRequest(context, fundTradeDto);
	}
	
	/**
	 * 修改订单金额
	 * @param context
	 * @param fundTradeDto
	 * @return
	 * 			OrderResult
	 * @author maj
	 */
	public OrderResult modifyAppointRequest(Context context, FundTradeDto fundTradeDto) {
		return fundTradeService.modifyAppointRequest(context, fundTradeDto);
	}
	
	/**
	 * 下排队单
	 * @param context
	 * @param fundTradeDto
	 * @param elDto
	 * @return
	 * 			OrderResult
	 * @author maj
	 */
	public OrderResult fundTradeLineUp(Context context, FundTradeDto fundTradeDto, SignEContractDto elDto) {
		return fundTradeService.fundTradeLineUp(context, fundTradeDto, elDto);
	}
	
	/**
	 * 银行鉴权接口（找回交易密码）
	 * @param context
	 * @param userAcctDto
	 * @return
	 */
	public OrderResult authenticate(Context context, UserAcctDto userAcctDto){
		
		OrderResult orderResult =userAcctTradeService.authenticate(context, userAcctDto);
		return orderResult;
	}
	
	
	/**
	   * 扫码参与活动
	   * 返回0000，表示参与有效，即二维码绑定成功
	   * 返回0010，表示扫描的二维码无效
	   * @param context
	   * @param openId 用户微信openId
	   * @param activityId 活动id
	   * @param md5Info 活动码MD5值
	   * @return
	   */
   public OrderResult activityScan(Context context, String openId, String activityId,String md5Info){
	   OrderResult orderResult = activityService.activityScan(context, openId, activityId, md5Info);
	   return orderResult;
   }
   /**
    * 登录后更新cmfUserId，返回1标示更新成功，0就是失败
    * @param context
    * @param openId
    * @param cmfUserId
    * @param activityId
    * @return
    */
   public int updateCmfUserId4Activity(Context context, String openId, String cmfUserId,String activityId){
	   int i = activityService.updateCmfUserId4Activity(context, openId, cmfUserId, activityId);
	   return i;
   }

   /**
    * 根据用户openId和cmfUserId查询用户参与的活动
    * @param context
    * @param openId
    * @param cmfUserId
    * @return
    */
   public OrderResult queryActivityByUserInfo(Context context, String openId, String cmfUserId){
	   OrderResult orderResult = activityService.queryActivityIdByUserInfo(context, openId, cmfUserId);
	   return orderResult;
   }

	/**
	 * 公告已读保存
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param articleId
	 * @return OrderResult
	 * @author maj
	 */
	public OrderResult addArticleReadRecord(Context context, String cmfUserId, String articleId) {
		return fundTradeService.addArticleReadRecord(context, cmfUserId, articleId);
	}
	/**
	 * 父亲节领取电影券
	 * @param context
	 * @param openId
	 * @param activityId
	 * @param md5Info
	 * @return
	 * @author luos
	 */
	public OrderResult activityGet(Context context, String openId, String activityId,String md5Info){
		return activityService.activityGet(context, openId, activityId, md5Info);
	}
	
	
	public OrderResult appConversionUserInvtp(Context context,
			UserRiskLevelDto userRiskLevelDto) {
		return userAcctTradeService.appConversionUserInvtp(context,userRiskLevelDto);
	}
	
	public OrderResult cancelAppConversionUserInvtp(Context context,String cmfUserId,String custNo) {
		return userAcctTradeService.cancelAppConversionUserInvtp(context, cmfUserId, custNo);
	}
	
	public OrderResult passTestUpdateUserInvtp(Context context,String cmfUserId,String custNo,String invprtpScore) {
		return userAcctTradeService.passTestUpdateUserInvtp(context, cmfUserId, custNo,invprtpScore);
	}
	
	/**
	 * 
	* 接口的功能说明： 删除请求数据
	* @param context
	* @param userRiskLevelDto
	* @return
	* @throws 
	 */
	public OrderResult clearAppcvInvp(Context context, UserRiskLevelDto userRiskLevelDto){
		return userAcctTradeService.clearAppcvInvp(context, userRiskLevelDto);
	}
	
	
	/**
	 * 选择银行卡支付
	 * 更新预下单 折扣率
	 * @param fundTradeDto
	 */
	public void updateAppointReqCommro(FundTradeDto fundTradeDto){
		fundTradeService.updateAppointReqCommro(fundTradeDto);
	}
	
	/**
	 * 更新用户订单到期分配方式
	 * @param param
	 * @return
	 */
	public Integer updateAppointRequest(String param){
		return fundTradeService.updateAppointRequest(param);
	}
	
	/**
	 * 保存用户操作记录
	 * @param param
	 */
	public void addOplog(String param){
		fundTradeService.addOpLog(param);
	}


	/**
	 * 赎回订单
	 * @param context
	 * @param fundTradeDto
	 * @return
	 */
	public OrderResult redemptionOrder(Context context, FundTradeDto fundTradeDto) {
		return fundTradeService.redemptionOrder(context, fundTradeDto);
	}


	public void addCmfWXPushmessage(CmfWXPushmessageDto dto) {
		fundTradeService.addCmfWXPushmessage(dto);
	}
	
	
	/**
	 * 活动用户鉴权信息，开通银行账户
	 * @param context
	 * @param userAcctDto
	 * @return
	 */
	public OrderResult openAccountForActivity(Context context, UserAcctDto userAcctDto){
		
		return userAcctTradeService.openAccountForActivity(context, userAcctDto);
	}
	
	/**
	 * 客户成功转换投资者类型后记录客户专业类型变动记录表 H_CUSTINVPRTP
	 * @param map
	 * @return
	 */
	public OrderResult insertCustInvprtp(Map<String, String> map) {
		return userAcctTradeService.insertCustInvprtp(map);
	}

	/**
	 * 积分相关交易业务逻辑类
	 */
	private IntegralTradeService integralTradeService;

	public void setIntegralTradeService(IntegralTradeService integralTradeService) {
		this.integralTradeService = integralTradeService;
	}


	public AmazonResult<byte[], String> download(String key) {
		return amazonS3Service.downloadByKey(key);
	}

	/**
	 * 当远程调用失败构建的系统错误消息传输对象
	 */
	private static TradeResult errTradeResult = TradeResult.failed(TradeErrCode.ERR_1004);

	/**
	 * 更新积分流水操作类型
	 *
	 * @param serialNo
	 * @param operationId
	 * @return
	 */
	public TradeResult<?> updateIntegralDetailOperation(String serialNo, int operationId) {
		TradeResult<?> result = integralTradeService.updateIntegralDetailOperation(serialNo, operationId);
		return (null == result ? errTradeResult : result);
	}

	/**
	 * 物品兑换
	 * 在积分流水表创建一条需兑换的记录, 由WXApp轮询发送, 发送后会修改记录操作类型为已发送
	 *
	 * @param cmfUserId
	 * @param goodsId
	 * @return
	 */
	public TradeResult<?> goodsExchange(String cmfUserId, int goodsId, long integralBeforeChange, long integralChange) {
		TradeResult<?> result = integralTradeService.goodsExchange(cmfUserId, goodsId, integralBeforeChange, integralChange);
		return (null == result ? errTradeResult : result);
	}

	public AmazonResult<String, String> upload(String key, byte[] bytes,
			String contentType, CmwaFileUploadRecordDto record) {
		return amazonS3Service.upload(key, bytes, contentType, record);
	}


	public AmazonResult<String, String> removeObjectByKey(String key) {
		return amazonS3Service.removeObjectByKey(key);
	}


	/**
	 * 查询文件上传记录
	 * @param fileUploadRecordQueryParam
	 * @return
	 */
	public List<CmwaFileUploadRecordDto> queryFileUploadRecord(CmwaFileUploadRecordDto fileUploadRecordQueryParam) {
		return cmwaAccreditedInvestorService.queryFileUploadRecord(fileUploadRecordQueryParam);
	}


	public int insertAccreditedInvestorInfo(CmwaQualifiedUserInfoDto param) {
		return cmwaAccreditedInvestorService.insertAccreditedInvestorInfo(param);
	}

	public int insertAccreditedInvestorInfo(
			CmwaQualifiedUserInfoDto innerRecord, String status) {
		return cmwaAccreditedInvestorService.insertAccreditedInvestorInfo(innerRecord,status);
	}

	public int updateQualifiedUserInfo(CmwaQualifiedUserInfoDto innerParam) {
		return cmwaAccreditedInvestorService.updateQualifiedUserInfo(innerParam);
	}


	public int deleteFileUploadRecord(String recordId) {
		return cmwaAccreditedInvestorService.deleteFileUploadRecord(recordId);
	}


	public int updateQualifiedUserInfoStatus(
			CmwaQualifiedUserInfoStatusDto statusRecord, String updateStatus,
			String currentUser) {
		return cmwaAccreditedInvestorService.modifyQualifiedUserInfoRequestStatus(statusRecord, updateStatus, currentUser);
	}
	/**
	 * 更新赎回份额
	 * @param fundTradeDto
	 * @return
	 */
	public OrderResult updateOrderRedemptionShare(FundTradeDto fundTradeDto) {
		return fundTradeService.updateOrderRedemptionShare(fundTradeDto);
	}
	
	/**
	 * 撤销赎回订单
	 * @param serialNo
	 * @return updateOrderApplyst
	 */
	public OrderResult revokeRedeemOrder(FundTradeDto dto){
		return fundTradeService.updateOrderApplyst(dto);
	}
}
