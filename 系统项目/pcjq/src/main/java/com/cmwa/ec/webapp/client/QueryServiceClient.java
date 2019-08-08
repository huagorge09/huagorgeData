package com.cmwa.ec.webapp.client;

import java.util.List;

import com.cmwa.ec.query.facade.*;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.webapp.util.ECConstants;

import java.util.HashMap;
import java.util.Map;

public class QueryServiceClient {

	private FundQueryService fundQueryService;
	private AccountQueryService accountQueryService;
	private ChannelQueryService channelQueryService;
	private WebQueryService webQueryService;
	private UserQueryService userQueryService;


	public void setFundQueryService(FundQueryService fundQueryService) {
		this.fundQueryService = fundQueryService;
	}
	
	public void setAccountQueryService(AccountQueryService accountQueryService) {
		this.accountQueryService = accountQueryService;
	}
	
	public void setChannelQueryService(ChannelQueryService channelQueryService) {
		this.channelQueryService = channelQueryService;
	}

	public void setWebQueryService(WebQueryService webQueryService) {
		this.webQueryService = webQueryService;
	}

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
	 * 根据产品id查询产品的合同， 新的合同查询接口，所有产品统一只有一个合同
	 * @param context
	 * @param fundId
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryFundContractById(Context context, String fundId) {
		QueryMessageDto queryMessageDto = fundQueryService.queryFundContractById(context,fundId);
		return queryMessageDto;
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
	 * 查询用户交易在账号列表
	 * @param context
	 * @param ecCustNo
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryUserTradeAcctInfoList(Context context, String ecCustNo, String fundid) {
		QueryMessageDto queryMessageDto = accountQueryService.queryUserTradeAcctInfoList(context, ecCustNo, fundid);
		return queryMessageDto;
	}

	/**
	 * 查询单个产品 返回json对象
	 * @param context
	 * @param fundid
	 * @param fundname
	 * @param string
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	/*public QueryMessageDto queryFundsAndQuestion(Context context, String fundid, String fundname, String string) {
		QueryMessageDto queryMessageDto = fundQueryService.queryFundsAndQuestion(context, fundid,fundname,string);
		return queryMessageDto;
	}*/

	/**
	 * 查询单个产品
	 * @param context
	 * @param fundId
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryFund(Context context, String fundId,int period) {
		QueryMessageDto queryMessageDto = fundQueryService.queryFund(context, fundId,period);
		return queryMessageDto;
	}
	
	public QueryMessageDto queryFund(Context context, String fundId) {
		QueryMessageDto queryMessageDto = fundQueryService.queryFund(context, fundId);
		return queryMessageDto;
	}
	
	/**
	 * 查询热销产品列表， 和产品展示特性有关， 不为空。 ishot = 1 && state > 0 取第一个
	 * @param context
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryHotFundList(Context context) {
		QueryMessageDto queryMessageDto = fundQueryService.queryHotFundList(context);
		return queryMessageDto;
	}
	
	/**
	 * 查询所有可展示的产品列表
	 * @param context
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryFundList(Context context) {
		QueryMessageDto queryMessageDto = fundQueryService.queryFundList(context);
		return queryMessageDto;
	}
	
	/**
	 * 查询费率和折扣
	 * @param context
	 * @param fundId
	 * @param channelNoList
	 * @param custLevel
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryFeeRateList(Context context, String fundId, List<String> channelNoList, String custLevel) {
		QueryMessageDto queryMessageDto = fundQueryService.queryFeeRateList(context, fundId, channelNoList, custLevel);
		return queryMessageDto;
	}

	/**
	 * 查询产品净值（净值类产品）
	 * @param context
	 * @param cmfUserId
	 * @param fundId
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryEstimateByFundId(Context context, String fundId,Integer dateTime) {
		QueryMessageDto queryMessageDto = fundQueryService.queryEstimateByFundId(context, fundId, dateTime);
		return queryMessageDto;
	}

	/**
	 * 查询产品的元素， 复用为查QA
	 * @param context
	 * @param fundId
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryFundElementList(Context context, String fundId) {
		QueryMessageDto queryMessageDto = fundQueryService.queryFundElementList(context, fundId);
		return queryMessageDto;
	}

	/**
	 * 根据客户号，交易编号查询订单详情
	 * @param context
	 * @param custno
	 * @param tradeNo
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryTradeInfoByTradeNo(Context context, String custno, String tradeNo,int period) {
		QueryMessageDto queryMessageDto=null;
		if (period==1) {
			 queryMessageDto = accountQueryService.queryTradeInfoByTradeNo(context, custno, tradeNo);
		}else{
			 queryMessageDto = accountQueryService.queryTradeInfoByTradeNoAndPeriod(context, custno, tradeNo,period);
		}
		return queryMessageDto;
	}
	
	public QueryMessageDto queryTradeInfoByCustNo(Context context, String custno, String fundid) {
		return accountQueryService.queryTradeInfoByCustNo(context, custno, fundid);
	}
	/*================以上是1.0版本代码=================*/
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
	 * 查询信批列表 
	 * @param context
	 * @param cmfUserId
	 * @param beginIdx 开始条数 哪一条开始   第1条为0
	 * @param amount 每页展示条数 
	 * @param totalAmount 总记录条数
	 * @return
	 * 			QueryMessageDto
	 * @author luos
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
	 * @author luos
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
	 * @author luos
	 */
	public QueryMessageDto queryUserMessageByPDF(Context context, String cmfUserId, String msgId, String msgType) {
		return fundQueryService.queryUserMessagePDF(context, cmfUserId, msgId, msgType);
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
	 * 查询用户订单列表
	 * @param context
	 * @param custno
	 * @return
	 * 			QueryMessageDto
	 * @author luos
	 */
	public QueryMessageDto queryTradeInfoList(Context context, String custno, String fundid, String applyst) {
		return accountQueryService.queryTradeInfoList(context, custno, fundid, applyst);
	}
	
	/**
	 * 查询用户产品持仓
	 * @param context
	 * @param custno
	 * @param fundCode
	 * @return
	 */
	public QueryMessageDto queryCustTradeInfo(Context context, String cmfUserId ,String fundCode,String tradeAcco) {
		return fundQueryService.queryTradeInfoByFundCode(context, cmfUserId, fundCode ,tradeAcco);
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
     * 查询用户的总资产、总收益
     * @param context		
     * @param cmfUserId     
     * @param source          报文来源
     * @return QueryMessageDto 返回码\返回信息 
     * 返回码如下 
	 * 成功		0000
	 * 返回的实体为  QueryMessageDto
	 */
	public QueryMessageDto queryTotalFundBalance(Context context,String cmfUserId){
		return accountQueryService.queryTotalFundBalance(context, cmfUserId);
	}

	/**
	 * 查询用户最近12个月收益
	 * @param context
	 * @param cmfUserId
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryMonthProfitBill (Context context,String cmfUserId){
		QueryMessageDto queryMessageDto = accountQueryService.queryMonthProfitBill(context, cmfUserId);
		return queryMessageDto;
	}

	/**
	 * 新闻动态   列表查询   分页
	 * 带查询条件 新闻名称
	 * @param context
	 * @param cmfUserId
	 * @param beginrow
	 * @param endrow
	 * @param name 
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryNewsArticle(Context context, String cmfUserId, int beginrow, int endrow,String name) {
		return webQueryService.queryNewsArticle(context, cmfUserId, beginrow, endrow, name);
	}

	/**
	 * 新闻动态总数查询
	 * 带查询条件 新闻名称
	 * @param context
	 * @param cmfUserId
	 * @param name 
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryNewsArticleCount(Context context, String cmfUserId,String name) {
		return webQueryService.queryNewsArticleCount(context, cmfUserId, name);
	}

	/**
	 * 公司公告    列表查询   分页
	 * 带查询条件 公告名称
	 * @param context
	 * @param cmfUserId
	 * @param beginrow
	 * @param endrow
	 * @param name 
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryCompanyArticle(Context context, String cmfUserId, int beginrow, int endrow,String name) {
		return webQueryService.queryCompanyArticle(context, cmfUserId, beginrow, endrow, name);
	}

	/**
	 * 公司公告总数查询
	 * 带查询条件 公告名称
	 * @param context
	 * @param cmfUserId
	 * @param name 
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryCompanyArticleCount(Context context, String cmfUserId,String name) {
		return webQueryService.queryCompanyArticleCount(context, cmfUserId, name);
	}

	/**
	 * 查询详情 新闻动态、公司公告
	 * @param context
	 * @param articleId
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryArticleContent(Context context, String articleId) {
		return webQueryService.queryArticleContent(context, articleId);
	}

	/**
	 * 根据fundTypeId查询指定类型的最新产品
	 * @param context
	 * @param type
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryHotFundByType(Context context, String type) {
		return fundQueryService.queryHotFundByType(context, type);
	}
	/****
     * 查询最近到期日的
     * @param custno 客户编号
     * @return
     * @author luos
     */
	public QueryMessageDto queryTradeInfoLately(Context context, String cmfUserId) {
		return accountQueryService.queryTradeInfoLately(context, cmfUserId);
	}

	/**
	 * 查询职位类别
	 * @param context
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryPositionType(Context context) {
		return webQueryService.queryPositionType(context);
	}

	/**
	 * 查询工作地点
	 * @param context
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryPlace(Context context) {
		return webQueryService.queryPlace(context);
	}

	/**
	 * 查询社会招聘信息
	 * @param context
	 * @param positionId 职位类型ID
	 * @param placeId 工作地点ID
	 * @param beginrow
	 * @param endrow
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto querySocialRecruitment(Context context, String positionId, String placeId, int beginrow, int endrow) {
		return webQueryService.querySocialRecruitment(context, positionId, placeId, beginrow, endrow);
	}

	/**
	 * 查询社会招聘信息记录数
	 * @param context
	 * @param positionId 职位类型ID
	 * @param placeId 工作地点ID
	 * @param beginrow
	 * @param endrow
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto querySocialRecruitmentCount(Context context, String positionId, String placeId) {
		return webQueryService.querySocialRecruitmentCount(context, positionId, placeId);
	}

	/**
     * 查询校园招聘信息
	 * @param context
	 * @param positionId 职位类型ID
	 * @param placeId 工作地点ID
	 * @param beginrow
	 * @param endrow
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryCampusRecruitment(Context context, String positionId, String placeId, int beginrow, int endrow) {
		return webQueryService.queryCampusRecruitment(context, positionId, placeId, beginrow, endrow);
	}

	/**
	 * 查询校园招聘信息记录数
	 * @param context
	 * @param positionId 职位类型ID
	 * @param placeId 工作地点ID
	 * @param placeId
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryCampusRecruitmentCount(Context context, String positionId, String placeId) {
		return webQueryService.queryCampusRecruitmentCount(context, positionId, placeId);
	}

	/**
	 * 查询校园招聘信息
	 * @param context
	 * @param positionId 职位类型ID
	 * @param placeId 工作地点ID
	 * @param beginrow
	 * @param endrow
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryTraineeRecruitment(Context context, String positionId, String placeId, int beginrow, int endrow) {
		return webQueryService.queryTraineeRecruitment(context, positionId, placeId, beginrow, endrow);
	}

	/**
	 * 查询校园招聘信息记录数
	 * @param context
	 * @param positionId 职位类型ID
	 * @param placeId 工作地点ID
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryTraineeRecruitmentCount(Context context, String positionId, String placeId) {
		return webQueryService.queryTraineeRecruitmentCount(context, positionId, placeId);
	}

	/**
	 * 招聘信息详情查询
	 * @param context
	 * @param informationId
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryRecruitmentDetail(Context context, String informationId) {
		return webQueryService.queryRecruitmentDetail(context, informationId);
	}

	/**
	 * 查询校园宣讲列表   带条件   支持分页
	 * @param context
	 * @param campusName 校园id
	 * @param beginrow
	 * @param endrow
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryCampusTalk(Context context, String campusName, int beginrow, int endrow) {
		return webQueryService.queryCampusTalk(context, campusName, beginrow, endrow);
	}

	/**
	 * 查询校园宣讲总数
	 * @param context
	 * @param campusName
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryCampusTalkCount(Context context, String campusName) {
		return webQueryService.queryCampusTalkCount(context, campusName);
	}

	/**
	 * 查询校园宣讲学校列表
	 * @param context
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto querySchoolList(Context context) {
		return webQueryService.querySchoolList(context);
	}

	public QueryMessageDto campusTalkDownLoad(Context context, String campusId) {
		return webQueryService.queryCampusTalkDetail(context, campusId);
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
	 * 查询常见问题
	 * @param context
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryWenTiByCommon(Context context) {
		return webQueryService.queryWenTiByCommon(context);
	}

	/**
	 * 查询问题列表
	 * @param context
	 * @param title
	 * @param beginrow
	 * @param endrow
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryWenTiList(Context context, String title, int beginrow, int endrow) {
		return webQueryService.queryWenTiList(context, title, beginrow, endrow);
	}

	/**
	 * 查询问题总数
	 * @param context
	 * @param title
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryWenTiCount(Context context, String title) {
		return webQueryService.queryWenTiCount(context, title);
	}

	public QueryMessageDto queryAdvert(Context context,String type) {
		return webQueryService.queryAdvert(context,type);
	}

	/**
	 * 查询用户代销机构订单列表
	 * @param context
	 * @param cmfUserId
	 * @return
	 * 			QueryMessageDto
	 * @author luos
	 */
	public QueryMessageDto queryAgentFundInfo(Context context, String cmfUserId) {
		return fundQueryService.queryAgentFundInfo(context, cmfUserId);
	}

	public QueryMessageDto queryCooperation(Context context) {
		return webQueryService.queryCooperation(context);
	}

	public QueryMessageDto queryConsignment(Context context) {
		return webQueryService.queryConsignment(context);
	}

	/**
	 * 下载中心查询 
	 * @param context
	 * @param fileId 为""查列表；不为空查详情
	 * @return QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryFileDownloadCenter(Context context,String fileId, String fileTypeIds) {
		return webQueryService.queryFileDownloadCenter(context, fileId, fileTypeIds);
	}
	/**
	 * 查询代销产品详情
	 * @param context
	 * @param 
	 * @return QueryMessageDto
	 * @author luos
	 */
	public QueryMessageDto queryOtherDetailOrder(Context context,String serialno,String cmfUserId,String fundId,String channleNo) {
		return fundQueryService.queryOtherDetailOrder(context, serialno, cmfUserId, fundId, channleNo);
	}
	/**
	 * 查询用户未读信批条数
	 * @param context
	 * @param cmfUserId
	 * @return
	 * 			QueryMessageDto getData<int>
	 * @author 
	 */
	public QueryMessageDto queryUnReadFundReportsCountForCompany(Context context, String cmfUserId) {
		return fundQueryService.queryUnReadFundReportsCountForCompany(context, cmfUserId);
	}

	public QueryMessageDto getSupportPayBankDesc(Context context) {
		return accountQueryService.getSupportPayBankDesc(context);
	}

	public QueryMessageDto queryFundContractByIdWithPeirod(Context context,
			String fundId, String period) {
		return fundQueryService.queryFundContractByIdWithPeriod(context,fundId,period);
	}
	
	public QueryMessageDto queryHomeAddressIsWordWithLinkage(Context context,String pmst,String pmky,
			String pmco,String pmv1){
		
		QueryMessageDto queryParameter = webQueryService.queryParameter(context, pmst, pmky, pmco, pmv1);
		
		return queryParameter;
	}
	
	public QueryMessageDto queryEducationV2(String catalogIds,int beginrow,int page,int endrow,String name){
		 QueryMessageDto queryParameter= webQueryService.queryEducationV2(catalogIds, beginrow, page, endrow, name);
		 return queryParameter;
	}
	
	
	public QueryMessageDto queryEducationCount(Context context,String catalogIds,String name){
		QueryMessageDto queryParameter= webQueryService.queryEducationCount(context, catalogIds,name);
		return queryParameter;
	}

	public QueryMessageDto queryInvestorArtivleByFileType(String fileType,
			int beginrow, int maxPages, int endrow, String name) {
		QueryMessageDto result = new QueryMessageDto();
		result  = webQueryService.queryInvestorArtivleByFileType(fileType, beginrow,maxPages,endrow,name);
		return result;
	}

	public QueryMessageDto queryInvestorArtivleByFileTypeCount(Context context,
			String fileType,String name) {
		QueryMessageDto result = webQueryService.queryInvestorArtivleByFileTypeCount(context,fileType,name);
		return result;
	}

	public QueryMessageDto queryMgmArticle(Context context, String cmfUserId,
			int beginrow, int endrow, String catalogId) {
		return webQueryService.queryMgmArticle(context, cmfUserId, beginrow, endrow,catalogId);
	}

	public QueryMessageDto queryMgmArticleCount(Context context,
			String cmfUserId, String catalogId) {
		return webQueryService.queryMgmArticleCount(context, cmfUserId,catalogId);
	}

	/**
	 * 查询机构用户产品列表
	 * @param fundAcco
	 * @param crmCustNo
	 * @param branchName
	 * @return
	 */
	public QueryMessageDto queryCompanyUserProduct(String fundAcco, String crmCustNo,
			String branchName) {
		return fundQueryService.queryCompanyUserProduct(fundAcco,crmCustNo,branchName);
	}

	/**
	 * 查询机构用户总资产
	 * @param fundAcco
	 * @param crmCustNo
	 * @param branchName
	 * @return
	 */
	public QueryMessageDto queryCompanyUserFundTotal(String fundAcco, String crmCustNo, String branchName) {
		return accountQueryService.queryCompanyTotalFundBalance(fundAcco,crmCustNo,branchName);
	}
	
	/**
	 * 查询机构用户产品详情
	 * @param serialno
	 * @param fundId
	 * @param channelNo
	 * @param fundAcco
	 * @param crmCustNo
	 * @param branchName
	 * @return
	 */
	public QueryMessageDto queryCompanyOtherDetailOrder(String serialno,
			String fundId,String channelNo, String fundAcco, String crmCustNo, String branchName){
		return fundQueryService.queryCompanyOtherDetailOrder(serialno, fundId,channelNo,fundAcco, crmCustNo, branchName);
	}

	
	
	/**
	 * 查询机构用户产品净值
	 * @param fundId
	 * @return
	 */
	public QueryMessageDto queryCompanyUserFundNetValue(String fundId,int page) {
		return fundQueryService.queryCompanyUserFundNetValue(fundId,page);
	}

	/**
	 * 查询机构用户产品详情界面净值走势图
	 * @param fundId
	 * @param dateTime
	 * @return
	 */
	public QueryMessageDto queryCompanyUserFundNetValueByImgTable(
			String fundId, int dateTime) {
		return fundQueryService.queryCompanyUserFundNetValueByImgTable(fundId,dateTime);
	}


	public String queryUserFundAccoByCmfUserId(String cmfUserId) {
		return (String) fundQueryService.getFundAccByCmfUserId(cmfUserId).getData();
	}

	public QueryMessageDto queryEstimateByFundId(List<String> asList) {
		return fundQueryService.queryEstimateByFundId(asList);
	}
	
	public QueryMessageDto QueryAppointRequestList(String serialno){
		return fundQueryService.queryAppointRequestList(serialno);
	}
	
	/**
	 * 原创文章  列表查询   分页
	 * 带查询条件 文章名称
	 * @param context
	 * @param cmfUserId
	 * @param beginrow
	 * @param endrow
	 * @param name 
	 * @return
	 * 			QueryMessageDto
	 * @author tuoy
	 */
	public QueryMessageDto queryOriginalArticle(Context context, String cmfUserId, int beginrow, int endrow,String name) {
		return webQueryService.queryOriginalArticle(context, cmfUserId, beginrow, endrow, name);
	}

	/**
	 * 原创文章总数查询
	 * 带查询条件 文章名称
	 * @param context
	 * @param cmfUserId
	 * @param name 
	 * @return
	 * 			QueryMessageDto
	 * @author tuoy
	 */
	public QueryMessageDto queryOriginalArticleCount(Context context, String cmfUserId,String name) {
		return webQueryService.queryOriginalArticleCount(context, cmfUserId, name);
	}

    /**
     * 查询指定页数展示的官网债券投资人员信息未下架文章
	 *
     * @param context
     * @param cmfUserId
     * @param beginrow
     * @param endrow
     * @param name
     * @return
     */
    public QueryMessageDto queryBondInvestorArticle(Context context, String cmfUserId, int beginrow, int endrow, String name) {
        return webQueryService.queryBondInvestorArticle(context, cmfUserId, beginrow, endrow, name);
    }

    /**
     * 查询债券投资人员信息未下架文章总数
	 *
     * @param context
     * @param cmfUserId
     * @param name
     * @return
     */
	public QueryMessageDto queryBondInvestorCount(Context context, String cmfUserId, String name) {
		return webQueryService.queryBondInvestorCount(context, cmfUserId, name);
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
	public QueryMessageDto queryQualifiedUserInfoByIdno(String idno) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("idno",idno);
		return userQueryService.queryAllQualifiedUserInfo(map);
    }

    public QueryMessageDto queryAllQualifiedUserInfo(Map<String, Object> queryParameter) {
		return userQueryService.queryAllQualifiedUserInfo(queryParameter);
    }

	public QueryMessageDto queryQualifiedUserInfoByCustno(String custno) {
		return userQueryService.queryQualifiedUserInfoByCustno(custno);
	}

	public QueryMessageDto queryAccreditedInvestorConditions(String custno) {
		return userQueryService.queryAccreditedInvestorConditions(custno);
	}

	/**
	 * 当远程调用失败构建的系统错误消息传输对象
	 */
	private static QueryMessageDto errResult = new QueryMessageDto(ECConstants.RETURN_CODE_9001,
		ECConstants.RETURN_MSG_9001);

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
		return (null == result ? errResult : result);
	}

	/**
	 * 获取可用积分兑换的商品列表
	 *
	 * @return
	 */
	public QueryMessageDto listIntegralGoods() {
		QueryMessageDto result = integralFunctionService.listIntegralGoods();
		return (null == result ? errResult : result);
	}

	/**
	 * 判断是否新用户并且该用户未绑定邀请人
	 *
	 * @param cmfUserId
	 * @return
	 */
	public QueryMessageDto isNewUserNotInvited(String cmfUserId) {
		QueryMessageDto result = integralFunctionService.isNewUserNotInvited(cmfUserId);
		return (null == result ? errResult : result);
	}

	/**
	 * 获取积分规则配置需展示的热门产品
	 *
	 * @return
	 */
	public QueryMessageDto listExhibitionFund() {
		QueryMessageDto result = integralFunctionService.listExhibitionFund();
		return (null == result ? errResult : result);
	}

	/**
	 * 获取用户的专属顾问信息
	 *
	 * @param cmfUserId 用户编号
	 * @return
	 */
	public QueryMessageDto getConsultantInfo(String cmfUserId) {
		QueryMessageDto result = userQueryService.getConsultantInfo(cmfUserId);
		return (null == result ? errResult : result);
	}

	/**
	 * 获取积分操作类型列表
	 *
	 * @return
	 */
	public QueryMessageDto listIntegralOperation() {
		QueryMessageDto result = integralFunctionService.listIntegralOperation();
		return (null == result ? errResult : result);
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
