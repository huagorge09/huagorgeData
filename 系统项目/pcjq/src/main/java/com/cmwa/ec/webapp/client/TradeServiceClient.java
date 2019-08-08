package com.cmwa.ec.webapp.client;

import java.util.List;
import java.util.Map;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoDto;
import com.cmwa.ec.trade.facade.dto.CmwaQualifiedUserInfoStatusDto;
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
import com.cmwa.ec.trade.facade.model.TradeResult;
import com.cmwa.ec.trade.facade.model.TradeErrCode;

public class TradeServiceClient {

	private FundTradeService fundTradeService;
	private UserAcctTradeService userAcctTradeService;
	private CmwaAccreditedInvestorService cmwaAccreditedInvestorService;
	/**
	 * amazon s3 service服务类
	 */
	private AmazonS3Service amazonS3Service;

	public void setAmazonS3Service(AmazonS3Service amazonS3Service) {
		this.amazonS3Service = amazonS3Service;
	}

	public void setUserAcctTradeService(UserAcctTradeService userAcctTradeService) {
		this.userAcctTradeService = userAcctTradeService;
	}

	public void setFundTradeService(FundTradeService fundTradeService) {
		this.fundTradeService = fundTradeService;
	}

	public void setCmwaAccreditedInvestorService(CmwaAccreditedInvestorService cmwaAccreditedInvestorService) {
		this.cmwaAccreditedInvestorService = cmwaAccreditedInvestorService;
	}

	/**
	 * 下预约单（预下单）
	 * @param context
	 * @param fundTradeDto
	 * @return
	 * 			OrderResult
	 * @author maj
	 */
	public OrderResult fundAppoint(Context context, FundTradeDto fundTradeDto) {
		OrderResult orderResult = fundTradeService.fundAppointV2(context, fundTradeDto);
		return orderResult;
	}

	/**
	 * 下交易单
	 * @param context
	 * @param fundTradeDto 购买要素
	 * @param elDto 电子合同
	 * @other PayType=(0,1), apkind=A2T
	 * @return
	 * 			OrderResult
	 * @author maj
	 */
	public OrderResult fundTrade(Context context, FundTradeDto fundTradeDto, SignEContractDto elDto) {
		OrderResult orderResult = fundTradeService.fundAppointConfirm(context, fundTradeDto, elDto);
		return orderResult;
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
	 * 赎回订单
	 * @param context
	 * @param fundTradeDto
	 * @return
	 */
	public OrderResult redemptionOrder(Context context, FundTradeDto fundTradeDto) {
		return fundTradeService.redemptionOrder(context, fundTradeDto);
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
	 * 信批已读保存
	 * @param context
	 * @param cmfUserId
	 * @param fundId
	 * @param reportId
	 * @return
	 * 			OrderResult
	 * @author luos
	 */
	public OrderResult addReportReadRecord(Context context, String cmfUserId, String fundId, String reportId) {
		return fundTradeService.addReportReadRecord(context, cmfUserId, fundId, reportId);
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
	 * 用户添加银行卡，开通银行账户
	 * @param context
	 * @param userAcctDto
	 * @return
	 */
	public OrderResult addBankNumber(Context context, UserAcctDto userAcctDto){
		
		return userAcctTradeService.addTradeAccount(context, userAcctDto);
	}

	/**
	 * 修改订单金额
	 * @param context
	 * @param fundTradeDto
	 * @return
	 * 			OrderResult
	 * @author luos
	 */
	public OrderResult modifyAppointRequest(Context context, FundTradeDto fundTradeDto) {
		return fundTradeService.modifyAppointRequest(context, fundTradeDto);
	}

	/**
	 * 风险测评  评级
	 * @param context
	 * @param userRiskLevelDto
	 * @return OrderResult
	 * @author maj
	 */
	public OrderResult setUserRiskLevel(Context context, UserRiskLevelDto userRiskLevelDto) {
		return userAcctTradeService.setUserRiskLevel(context, userRiskLevelDto);
	}

	/**
	 * 银行支付签约接口
	 * @param context
	 * @param userAcctDto	UserAcctDto.tradeAcct 交易账号/UserAcctDto.bankNo 银行代码
	 * @return OrderResult
	 * @author maj
	 */
	public OrderResult contractSign(Context context, UserAcctDto userAcctDto){
		return userAcctTradeService.contractSign(context, userAcctDto);
	}
	
	/**
	 * 银行指令状态查询（主要用于查询工行签约结果，其他场景依情况调用）
	 * @param context
	 * @param userAcctDto	UserAcctDto.tradeAcct 交易账号/UserAcctDto.bankNo 银行代码
	 * @return OrderResult
	 * @author maj
	 */
	public OrderResult queryCommandByBankAccoNo(Context context, UserAcctDto userAcctDto){
		return userAcctTradeService.queryCommandByBankAccoNo(context, userAcctDto);
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
	
	public OrderResult appConversionUserInvtp(Context context,
			UserRiskLevelDto userRiskLevelDto) {
		return userAcctTradeService.appConversionUserInvtp(context,userRiskLevelDto);
	}
	
	
	public OrderResult passTestUpdateUserInvtp(Context context,String cmfUserId,String custNo,String invprtpScore) {
		return userAcctTradeService.passTestUpdateUserInvtp(context, cmfUserId, custNo,invprtpScore);
	}
	
	public OrderResult cancelAppConversionUserInvtp(Context context,String cmfUserId,String custNo) {
		return userAcctTradeService.cancelAppConversionUserInvtp(context, cmfUserId, custNo);
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
	 * 保存微信模板消息纪录
	 * @param param
	 */
	public void addCmfWXPushmessage(CmfWXPushmessageDto dto){
		fundTradeService.addCmfWXPushmessage(dto);
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
	 * 更新客户订单的赎回份额
	 * @param dto
	 * @return
	 */
	public OrderResult updateOrderRedemptionShare(FundTradeDto dto) {
		return fundTradeService.updateOrderRedemptionShare(dto);
	}

	/**
	 * 下载
	 * @param key 存放在 amazon s3 service上文件的key
	 * @return
	 */
	public AmazonResult<byte[],String> download(String key) {
		return amazonS3Service.downloadByKey(key);
	}

	/**
	 * 上传
	 * @param key 上传时使用的key
	 * @param bytes 文件的字节类型数据
	 * @param contentType 文件类型
	 * @return
	 */
	public AmazonResult<String,String> upload(String key, byte[] bytes, String contentType,CmwaFileUploadRecordDto record) {
		return amazonS3Service.upload(key,bytes,contentType,record);
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
    
    public int insertAccreditedInvestorInfo(CmwaQualifiedUserInfoDto param,String status) {
    	return cmwaAccreditedInvestorService.insertAccreditedInvestorInfo(param,status);
	}

	public int deleteFileUploadRecord(String recordId) {
		return cmwaAccreditedInvestorService.deleteFileUploadRecord(recordId);
	}

	public AmazonResult<String,String> removeObjectByKey(String fileKey) {
		return amazonS3Service.removeObjectByKey(fileKey);
	}

	public int updateQualifiedUserInfo(CmwaQualifiedUserInfoDto innerParam) {
		return cmwaAccreditedInvestorService.updateQualifiedUserInfo(innerParam);
	}
	
	public int updateQualifiedUserInfoStatus(CmwaQualifiedUserInfoStatusDto status,String updateStatus,String currentUser){
		return cmwaAccreditedInvestorService.modifyQualifiedUserInfoRequestStatus(status, updateStatus, currentUser);
	}

/**
	 * 积分相关交易业务逻辑类
	 */
	private IntegralTradeService integralTradeService;

	public void setIntegralTradeService(IntegralTradeService integralTradeService) {
		this.integralTradeService = integralTradeService;

	}

	/**
	 * 当远程调用失败构建的系统错误消息传输对象
	 */
	private static TradeResult errTradeResult = TradeResult.failed(TradeErrCode.ERR_1004);

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
	
	/**
	 * 修改订单申请状态
	 * @param dto
	 * @return
	 */
	public OrderResult updateOrderApplyst(FundTradeDto dto) {
		return fundTradeService.updateOrderApplyst(dto);
	}
}
