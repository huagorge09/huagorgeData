package com.cmwa.ec.weixin.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cmwa.ec.query.facade.*;

import net.sf.json.JSONArray;
import net.sf.json.JsonConfig;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.AccountQueryService;
import com.cmwa.ec.query.facade.ChannelQueryService;
import com.cmwa.ec.query.facade.FundQueryService;
import com.cmwa.ec.query.facade.UserQueryService;
import com.cmwa.ec.query.facade.WebQueryService;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.query.facade.dto.user.UserCommonDto;
import com.cmwa.ec.query.facade.dto.user.UserOrderDto;
import com.cmwa.ec.weixin.constants.WXConstants;
import com.cmwa.ec.weixin.dto.ParameterDto;
import com.cmwa.ec.weixin.util.ContextUtils;

/**
 * 调用后台查询模块接口
 * @author liury
 *
 */
public class QueryServiceClient {
	
	private FundQueryService fundQueryService;
	private AccountQueryService accountQueryService;
	private ChannelQueryService channelQueryService;
	private WebQueryService webQueryService;
	private UserQueryService userQueryService;

	public WebQueryService getWebQueryService() {
		return webQueryService;
	}

	public void setWebQueryService(WebQueryService webQueryService) {
		this.webQueryService = webQueryService;
	}
	
	public FundQueryService getFundQueryService() {
		return fundQueryService;
	}

	public void setFundQueryService(FundQueryService fundQueryService) {
		this.fundQueryService = fundQueryService;
	}

	
	public AccountQueryService getAccountQueryService() {
		return accountQueryService;
	}

	public void setAccountQueryService(AccountQueryService accountQueryService) {
		this.accountQueryService = accountQueryService;
	}

	public ChannelQueryService getChannelQueryService() {
		return channelQueryService;
	}

	public void setChannelQueryService(ChannelQueryService channelQueryService) {
		this.channelQueryService = channelQueryService;
	}

	/**
	 * @return the userQueryService
	 */
	public UserQueryService getUserQueryService() {
		return userQueryService;
	}

	/**
	 * @param userQueryService the userQueryService to set
	 */
	public void setUserQueryService(UserQueryService userQueryService) {
		this.userQueryService = userQueryService;
	}

	/**
	 * 积分功能相关查询业务逻辑类
	 */
	private IntegralFunctionService integralFunctionService;

	public void setIntegralFunctionService(IntegralFunctionService integralFunctionService) {
		this.integralFunctionService = integralFunctionService;
	}

	/**
	 * 查询产品列表
	 * @param context
	 * @return
	 */
	public QueryMessageDto queryFundList(Context context){
		return fundQueryService.queryFundList(context);
	}
	/**
	 * 查询产品列表
	 * @param context
	 * @return
	 */
	public QueryMessageDto queryHotFundList(Context context){
		return fundQueryService.queryHotFundList(context);
	}
	/**
	 * 查询产品详情
	 * @param context
	 * @param fundId
	 * @return
	 */
	public QueryMessageDto queryFund(Context context,String fundId,int period){
		
		return fundQueryService.queryFund(context, fundId,period);
	}
	/**
	 * 查询产品详情
	 * @param context
	 * @param fundId
	 * @return
	 */
	public QueryMessageDto queryFund(Context context,String fundId){
		
		return fundQueryService.queryFund(context, fundId);
	}
	
	/**
	 * 查询费率
	 * @param context
	 * @param fundId	产品ID，不可为空
	 * @param channelNo	用户购买的渠道号，如果还没有获取，可以传空串
	 * @param custLevel	客户等级，目前全部传空串
	 * @return QueryMessageDto.data = Map<ChannelNo, List<FeeRateDto>>
	 */
	public QueryMessageDto queryFeeRateList(Context context, String fundId, List<String> channelNoList, String custLevel){
	
		return fundQueryService.queryFeeRateList(context,fundId,channelNoList,custLevel);
	}
	
	
	/**
	 * 根据产品id查询产品的合同， 新的合同查询接口，所有产品统一只有一个合同
	 * @param context
	 * @param fundId
	 * @return
	 * 	QueryMessageDto
	 * @author wudb
	 */
	public QueryMessageDto queryFundContractById(Context context, String fundId) {
		
		return fundQueryService.queryFundContractById(context,fundId);
	}
	
	/**
	 * 根据ecCustNo查询银行卡列表
	 * @param context
	 * @param ecCustNo
	 * @return
	 */
	public QueryMessageDto getTradeAcctInfoByCustno(Context context, String ecCustNo, String fundid) {
		
		return accountQueryService.queryUserTradeAcctInfoList(context, ecCustNo, fundid);
	}
	
	
	/**
	 * 获取交易账号信息
	 * @param context
	 * @param custNo
	 * @param tradeNo
	 * @return
	 */
	public QueryMessageDto queryUserTradeAcctInfo(Context context,String custNo,String tradeNo,String fundid){
		return accountQueryService.queryUserTradeAcctInfo(context,custNo,tradeNo,fundid);
	}
	
	/**
	 * 根据银行卡号查找该卡号的具体银行信息  即卡号校验
	 * @param context
	 * @param bankCardNo  银行卡号
	 * @return
	 * @author liury
	 */
	public QueryMessageDto queryBankInfoByBankNumber(Context context,String bankCardNo){
		
		return channelQueryService.getChannelInfoByCardNo(context, bankCardNo);
	}

	/**
     * 查询用户的总资产、总收益
     * @param context		
     * @param cmfUserId     
     * @param source          报文来源 (weixin)
     * @return QueryMessageDto 返回码\返回信息 
     * 返回码如下 
	 * 成功		0000
	 * 返回的实体为  TotalBillInfoDto
	 */
	public QueryMessageDto queryTotalFundBalance(Context context,String cmfUserId){
		return accountQueryService.queryTotalFundBalance(context, cmfUserId);
	}
	
	/**
	 * 查询所支持的银行列表
	 * @param context
	 * @return
	 */
	public QueryMessageDto getSupportBankDesc(Context context){
		
		QueryMessageDto queryMessageDto = accountQueryService.getSupportBankDesc(context);
		return queryMessageDto;
	}

	/**
	 * 查询用户订单列表
	 * @param context
	 * @param custno
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryTradeInfoList(Context context, String custno, String fundid, String applyst) {
		return accountQueryService.queryTradeInfoList(context, custno, fundid, applyst);
	}
	
	/**
	 * 查询用户产品持仓
	 * @param context
	 * @param cmfUserId
	 * @param fundCode
	 * @return
	 */
	public QueryMessageDto queryCustTradeInfo(Context context, String cmfUserId ,String fundCode,String tradeAcco) {
		return fundQueryService.queryTradeInfoByFundCode(context, cmfUserId, fundCode,tradeAcco);
	}
	
	/**
	 * 查询用户是否占用购买额度
	 * @param context
	 * @param cmfUserId
	 * @param fundCode
	 * @return
	 */
	public QueryMessageDto queryNewCustomer(Context context, String cmfUserId ,String fundCode) {
		return fundQueryService.queryNewCustomer(context, cmfUserId, fundCode);
	}

	
	/**
	 * 查询用户未读信批条数
	 * @param context
	 * @param cmfUserId
	 * @return
	 * 			QueryMessageDto getData<int>
	 * @author maj
	 */
	public QueryMessageDto queryUnReadFundReportsCount(Context context, String cmfUserId) {
		return fundQueryService.queryUnReadFundReportsCount(context, cmfUserId);
	}

	/**
	 * 查询信批列表 
	 * @param context
	 * @param cmfUserId
	 * @param beginIdx 开始条数 哪一条开始   第1条为0
	 * @param amount 每页展示条数 
	 * @param totalAmount 总记录条数
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageList(Context context, String cmfUserId, int beginIdx, int amount, int totalAmount) {
		return fundQueryService.queryUserMessageList(context, cmfUserId, beginIdx, amount, totalAmount);
	}

	/**
	 * 查询信批详情
	 * @param context
	 * @param cmfUserId
	 * @param msgId
	 * @param msgType
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryUserMessage(Context context, String cmfUserId, String msgId, String msgType) {
		return fundQueryService.queryUserMessage(context, cmfUserId, msgId, msgType);
	}

	/**
	 * 查询信批详情 PDF
	 * @param context
	 * @param cmfUserId
	 * @param msgId
	 * @param msgType
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageByPDF(Context context, String cmfUserId, String msgId, String msgType) {
		return fundQueryService.queryUserMessagePDF(context, cmfUserId, msgId, msgType);
	}

	/**
	 * 查询历史交易信息   对账单
	 * @param context
	 * @param cmfUserId
	 * @param startDate
	 * @param endDate
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */  
	public QueryMessageDto queryFundTradeInfo(Context context, String cmfUserId, String startDate, String endDate) {
		return accountQueryService.queryFundTradeInfoV2(context, cmfUserId, startDate, endDate);
	}

	/**
	 * 查询订单详情
	 * @param context
	 * @param custno
	 * @param serialno
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryTradeInfoByTradeNo(Context context, String custno, String serialno,int period) {
		QueryMessageDto queryMessageDto=null;
		if (period==1) {
			 queryMessageDto = accountQueryService.queryTradeInfoByTradeNo(context, custno, serialno);
		}else{
			 queryMessageDto = accountQueryService.queryTradeInfoByTradeNoAndPeriod(context, custno, serialno,period);
		}
		return queryMessageDto;
	}

	/**
	 * 根据tradeAcco获取交易账号
	 * @param context
	 * @param tradeacco
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto getTradeAcctInfoByTradeAcco(Context context, String tradeacco) {
		return accountQueryService.getTradeAcctInfoByTradeAcco(context, tradeacco);
	}

	/**
	 * 查询用户代销机构订单列表
	 * @param context
	 * @param cmfUserId
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryAgentFundInfo(Context context, String cmfUserId) {
		return fundQueryService.queryAgentFundInfo(context, cmfUserId);
	}

	/**
	 * 查询公司公告（分页）
	 * @param context
	 * @param cmfUserId
	 * @param beginrow
	 * @param endrow
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryCompanyArticle(Context context, String cmfUserId, int beginrow, int endrow) {
		return webQueryService.queryCompanyArticle(context, cmfUserId, beginrow, endrow);
	}
	
	/**
	 * 查询公司公告(总数)
	 * @param context
	 * @param cmfUserId
	 * @param beginrow
	 * @param endrow
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryCompanyArticleCount(Context context, String cmfUserId) {
		return webQueryService.queryCompanyArticleCount(context, cmfUserId);
	}

	/**
	 * 最新动态、公司公告  详情
	 * @param context
	 * @param cmfUserId
	 * @param articleId
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryArticleContent(Context context, String articleId) {
		return webQueryService.queryArticleContent(context, articleId);
	}

	/**
	 * 查询新闻动态公告（分页）
	 * @param context
	 * @param cmfUserId
	 * @param beginrow
	 * @param endrow
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryNewsArticle(Context context, String cmfUserId, int beginrow, int endrow) {
		return webQueryService.queryNewsArticle(context, cmfUserId, beginrow, endrow);
	}

	/**
	 * 查询新闻动态公告(总数)
	 * @param context
	 * @param cmfUserId
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryNewsArticleCount(Context context, String cmfUserId) {
		return webQueryService.queryNewsArticleCount(context, cmfUserId);
	}
	/**
	 * 查询代销产品详情
	 * @param context
	 * @param 
	 * @return QueryMessageDto
	 * @author luos
	 */
	public QueryMessageDto queryOtherDetailOrder(Context context,String serialno,String cmfUserId,String fundId,String channelNo) {
		return fundQueryService.queryOtherDetailOrder(context, serialno, cmfUserId, fundId,channelNo);
	}
	/**
	 * 查询微信banner图片列表
	 * @param context
	 * @param type
	 * @return
	 * @author luos
	 */
	public QueryMessageDto queryBanner(Context context,String type){
		return webQueryService.queryAdvert(context, type);
	}

	/**
	 * 查询产品净值（净值类产品）
	 * @param context
	 * @param cmfUserId
	 * @param fundId
	 * @return
	 * 			QueryMessageDto
	 * @author luos
	 */
	public QueryMessageDto queryEstimateByFundId(Context context, String fundId,Integer dateTime) {
		QueryMessageDto queryMessageDto = fundQueryService.queryEstimateByFundId(context, fundId, dateTime);
		return queryMessageDto;
	}
	
	
	public QueryMessageDto queryFundContractByIdWithPeirod(Context context,
			String fundId, String period) {
		// TODO Auto-generated method stub
		return fundQueryService.queryFundContractByIdWithPeriod(context,fundId,period);
	}
	
	public QueryMessageDto queryHomeAddressIsWordWithLinkage(Context context,String pmst,String pmky,
			String pmco,String pmv1){
		
		QueryMessageDto queryParameter = webQueryService.queryParameter(context, pmst, pmky, pmco, pmv1);
		
		return queryParameter;
	}
	
	public QueryMessageDto QueryAppointRequestList(String serialno){
		return fundQueryService.queryAppointRequestList(serialno);
	}
	
	public List<ParameterDto> queryParameter(String pmst,String pmky,
			String pmco,String pmv1){
		// 日志封装类
		Context context = ContextUtils.setContext(WXConstants.QUERY_SERVICE_701, "", "", System.currentTimeMillis(), System.currentTimeMillis(), "", "");
		QueryMessageDto queryParameter = webQueryService.queryParameter(context, pmst, pmky, pmco, pmv1);
		List<ParameterDto> parameterDtos = new ArrayList<ParameterDto>(); 
		if(null != queryParameter){
			JSONArray jsonArray = new JSONArray();
			jsonArray = JSONArray.fromObject(queryParameter.getData());
			parameterDtos = JSONArray.toList(jsonArray, new ParameterDto(), new JsonConfig());
		}
		return parameterDtos;
	}

	public QueryMessageDto queryProfitByFundCode(Context context,
			String cmfUserId, String fundCode) {
		return fundQueryService.queryProfitByFundCode(context, cmfUserId, fundCode);
	}

	public QueryMessageDto queryTradeInfoByCustNo(Context context,
			String custno, String fundid) {
		return accountQueryService.queryTradeInfoByCustNo(context, custno, fundid);
	}
	/**
	 * 查询赎回通知邮件正文
	 * @param serialno
	 * @return
	 */
	public QueryMessageDto queryRedeemContentList(String serialno){
		return fundQueryService.queryRedeemContentList(serialno);
	}

	/**
	 * 根据产品id来查询可赎回的份额及银行卡信息
	 * @param fundid
	 * @param custno
	 * @return
	 */
	public QueryMessageDto queryCanRedeemBankInfoByFundCode(String fundid, String custno) {
		return fundQueryService.queryCanRedeemBankInfoByFundCode(fundid,custno);
	}


	/**
	 * 当远程调用失败构建的系统错误消息传输对象
	 */
	private static QueryMessageDto errResult = new QueryMessageDto(WXConstants.COMMON_ERROR_REDATAISNULLCODE,
		WXConstants.COMMON_ERROR_REDATAISNULLMSG);

	private QueryMessageDto convertNull(QueryMessageDto dto) {
		return (null == dto ? errResult : dto);
	}

	/**
	 * 查询参数(开关)
	 *
	 * @param context
	 * @param pmst
	 * @param pmky
	 * @param pmco
	 * @param pmv1
	 * @return
	 */
	public QueryMessageDto queryParameter(Context context, String pmst, String pmky, String pmco, String pmv1) {
		QueryMessageDto result = webQueryService.queryParameter(context, pmst, pmky, pmco, pmv1);
		return convertNull(result);
	}
	public QueryMessageDto queryQualifiedUserInfoByIdno(String idno) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("idno",idno);
		return userQueryService.queryAllQualifiedUserInfo(map);
}

	/**
	 * 获取可用积分兑换的商品列表
	 *
	 * @return
	 */
	public QueryMessageDto listIntegralGoods() {
		QueryMessageDto result = integralFunctionService.listIntegralGoods();
		return convertNull(result);
	}

	/**
	 * 判断是否新用户并且该用户未绑定邀请人
	 *
	 * @param cmfUserId
	 * @return
	 */
	public QueryMessageDto isNewUserNotInvited(String cmfUserId) {
		QueryMessageDto result = integralFunctionService.isNewUserNotInvited(cmfUserId);
		return convertNull(result);
	}

	/**
	 * 获取积分规则配置需展示的热门产品
	 *
	 * @return
	 */
	public QueryMessageDto listExhibitionFund() {
		QueryMessageDto result = integralFunctionService.listExhibitionFund();
		return convertNull(result);
	}

	/**
	 * 获取用户的专属顾问信息
	 *
	 * @param cmfUserId 用户编号
	 * @return
	 */
	public QueryMessageDto getConsultantInfo(String cmfUserId) {
		QueryMessageDto result = userQueryService.getConsultantInfo(cmfUserId);
		return convertNull(result);
	}

	/**
	 * 获取需发送京东卡的用户手机号
	 *
	 * @return
	 */
	public QueryMessageDto listNeedSendJdCard() {
		QueryMessageDto result = integralFunctionService.listNeedSendJdCard();
		return convertNull(result);
	}

	/**
	 * 获取需发送话费卡的用户手机号
	 *
	 * @return
	 */
	public QueryMessageDto listNeedSendCallChargeCard() {
		QueryMessageDto result = integralFunctionService.listNeedSendPhoneRechargeCard();
		return convertNull(result);
	}

	/**
	 * 获取积分操作类型列表
	 *
	 * @return
	 */
	public QueryMessageDto listIntegralOperation() {
		QueryMessageDto result = integralFunctionService.listIntegralOperation();
		return convertNull(result);
	}

	public QueryMessageDto queryAllQualifiedUserInfo(
			Map<String, Object> queryParameter) {
		return userQueryService.queryAllQualifiedUserInfo(queryParameter);
	}

	public QueryMessageDto queryQualifiedUserInfoByCustno(String custno) {
		return userQueryService.queryQualifiedUserInfoByCustno(custno);
	}

	public QueryMessageDto queryAccreditedInvestorConditions(String custno) {
		return userQueryService.queryAccreditedInvestorConditions(custno);
	}
	
	public List<UserCommonDto> queryUserInfo(UserCommonDto dto){
		List<UserCommonDto> list=userQueryService.queryUserInfo(dto);
		return list;
	}
	
	public List<UserOrderDto> queryUserOrderInfo(UserOrderDto dto){
		List<UserOrderDto> list=userQueryService.queryUserOrderInfo(dto);
		return list;
	}
    /**
	 * 查询风险揭示函
	 * @param context
	 * @param cmfUserId
	 * @param fundId
	 * @param period
	 * @param state
	 * @return
	 */
	public QueryMessageDto queryUserTermList(Context context, String cmfUserId, String fundId ,int period ,String state){
		return  accountQueryService.queryUserTermList(context, cmfUserId, fundId, period,state);
	}
}
