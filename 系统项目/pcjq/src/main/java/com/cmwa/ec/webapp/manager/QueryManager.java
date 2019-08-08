package com.cmwa.ec.webapp.manager;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cmwa.ec.query.facade.dto.system.ParameterDto;
import com.cmwa.ec.query.facade.model.consultant.CustserviceInfoDTO;
import net.sf.json.JSONObject;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.user.facade.dto.UserAccoRlaDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;

public interface QueryManager {
	
	public QueryMessageDto queryFundContractById(Context context, String fundId);

	/**
	 * 根据产品id查询产品的合同， 新的合同查询接口，所有产品统一只有一个合同
	 * 
	 * @param context
	 * @param fundId
	 * @return QueryMessageDto
	 * @author maj
	 * @param period 
	 */
	QueryMessageDto queryFundContractById(Context context, String fundId,
			String period);

	/**
	 * 查询用户交易账号列表
	 * 
	 * @param context
	 * @param ecCustNo
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto queryUserTradeAcctInfoList(Context context, String ecCustNo, String fundid);

	/**
	 * 查询单个产品 返回json对象 （老   要删除）
	 * 
	 * @param context
	 * @param fundid
	 * @param fundname
	 * @param string
	 * @return QueryMessageDto
	 * @author maj
	 */
	// QueryMessageDto queryFundsAndQuestion(Context context, String fundid,
	// String fundname, String string);

	/**
	 * 查询单个产品
	 * 
	 * @param context
	 * @param fundId
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto queryFund(Context context, String fundId,int period);
	
	/**
	 * 查询热销产品列表， 和产品展示特性有关， 不为空。 ishot = 1 && state > 0    取第一个
	 * 
	 * @param context
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto queryHotFundList(Context context);

	/**
	 * 查询所有可展示的产品列表
	 * 
	 * @param context
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto queryFundList(Context context);

	/**
	 * 查询费率和折扣
	 * 
	 * @param context
	 * @param fundId
	 * @param channelNoList
	 * @param custLevel
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto queryFeeRateList(Context context, String fundId,
			List<String> channelNoList, String custLevel);

	/**
	 * 查询产品净值（净值类产品）
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param fundId
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto queryEstimateByFundId(Context context, String fundId,
			Integer dateTime);

	/**
	 * 查询产品的元素， 复用为查QA
	 * 
	 * @param context
	 * @param fundId
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto queryFundElementList(Context context, String fundId);

	/**
	 * 根据客户号，交易编号查询订单详情
	 * 
	 * @param context
	 * @param custno
	 * @param tradeNo
	 * @param period
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto queryTradeInfoByTradeNo(Context context, String custno,
			String tradeNo, int period);
	
	/**
	 * 根据用户编号跟基金代码查询订单详情
	 * 
	 * @param context
	 * @param custno
	 * @param fundid
	 * @return JSONObject
	 */
	public JSONObject queryTradeInfoByCustNo(Context context, String custno,
			String fundid, String subquty);

	/*================以上是1.0版本代码=================*/
	
	/**
	 * 查询支持的银行卡列表
	 * 
	 * @param context
	 * @return
	 * @author luos
	 */
	public JSONObject getSupportBankDesc(Context context);

	/**
	 * /** 根据银行卡号查找该卡号的具体银行信息 即卡号校验
	 * 
	 * @param context
	 * @param bankCardNo
	 * @return
	 * @author luos
	 */
	JSONObject queryBankInfoByBankNumber(Context context, String bankCardNo);

	/**
	 * 查询用户的银行卡信息
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author luos
	 */
	JSONObject getTradeAcctInfoByCustno(Context context, String cmfUserId,
			HttpServletRequest request);

	/**
	 * 查询信批列表 
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param beginIdx
	 *            开始条数 哪一条开始 第1条为0
	 * @param amount
	 *            每页展示条数
	 * @param totalAmount
	 *            总记录条数
	 * @return QueryMessageDto
	 * @author luos
	 */
	JSONObject queryUserMessageList(Context context, String cmfUserId,
			int beginIdx, int amount, int parseInt);
	
	/**
	 * 根据消息标题模糊查询
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param title
	 * @return
	 */
	JSONObject queryUserMsgLikeTitle(Context context, String cmfUserId,
			String title);

	/**
	 * 查询信批详情
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param msgId
	 * @param msgType
	 * @return JSONObject
	 * @author luos
	 */
	JSONObject queryUserMessage(Context context, String cmfUserId,
			String msgId, String msgType);

	/**
	 * 查询信批详情 PDF
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param msgId
	 * @param msgType
	 * @return QueryMessageDto
	 * @author luos
	 */
	QueryMessageDto queryUserMessageByPDF(Context context, String cmfUserId,
			String msgId, String msgType);

	/**
	 * 查询信批未读条数
	 * 
	 * @param context
	 * @param cmfUserId
	 * @return
	 * @author luos
	 */
	JSONObject queryUnReadFundReportsCount(Context context, String cmfUserId);

	/**
	 * 查询历史交易信息   对账单
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param startDate
	 * @param endDate
	 * @return JSONObject
	 * @author luos
	 */
	JSONObject queryFundTradeInfo(Context context, String cmfUserId,
			String startDate, String endDate);

	/**
	 * 查询用户订单列表
	 * 
	 * @param context
	 * @param custno
	 * @return JSONObject
	 * @author luos
	 */
	JSONObject queryTradeInfoList(Context context, String custno, String fundid, String applyst);
	
	/**
	 * 查询用户产品持仓
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param fundCode
	 * @return
	 */
	JSONObject queryCustTradeInfo(Context context, String cmfUserId,
			String fundCode, String tradeAcco);
	
	/**
	 * 查询是否新客户
	 * @param context
	 * @param cmfUserId
	 * @param fundCode
	 * @return
	 */
	JSONObject queryNewCustomer(Context context, String cmfUserId , String  fundCode);
	
	/**
	 * 查询用户资产和信息
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param userBaseInfo
	 * @param seqId
	 * @param accoRlaDto 
	 * @return
	 */
	JSONObject queryTotalFundBalance(Context context, String cmfUserId,
			UserBaseInfoDto userBaseInfo, String seqId,
			UserAccoRlaDto accoRlaDto);

	/**
	 * 新闻动态列表查询 支持分页 查询指定页数展示的新闻和新闻总数 带查询条件 新闻名称
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param page
	 * @param name
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryNewsArticleAndCount(Context context, String cmfUserId,
			int page, String name);

	/**
	 * 公司公告列表查询 支持分页 查询指定页数展示的公司公告和公告总数 带查询条件 公告名称
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param page
	 * @param name
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryCompanyArticleAndCount(Context context, String cmfUserId,
			int page, String name);

	/**
	 * 查询详情     新闻动态、公司公告
	 * 
	 * @param context
	 * @param articleId
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryArticleContent(Context context, String articleId);

	/**
	 * 根据fundTypeId查询指定类型的最新产品
	 * 
	 * @param context
	 * @param type
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryHotFundByType(Context context, String type);

	/**
	 * 社会招聘查询   带分页
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject querySocialRecruitment(Context context,
			HttpServletRequest request);

	/**
	 * 校园招聘查询   带分页
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryCampusRecruitment(Context context,
			HttpServletRequest request);

	/**
	 * 实习生招聘查询   带分页
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryTraineeRecruitment(Context context,
			HttpServletRequest request);

	/**
	 * 招聘信息详情查询
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryRecruitmentDetail(Context context,
			HttpServletRequest request);

	/**
	 * 校园宣讲查询
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryCampusTalk(Context context, HttpServletRequest request);

	/**
	 * 校园宣讲详情查询、下载
	 * 
	 * @param context
	 * @param campusId
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto campusTalkDownLoad(Context context, String campusId);

	/**
	 * 根据tradeAcco获取交易账号
	 * 
	 * @param context
	 * @param tradeacco
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto getTradeAcctInfoByTradeAcco(Context context,
			String tradeacco);

	/**
	 * 查询常见问题
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryWenTiByCommon(Context context, HttpServletRequest request);

	/**
	 * 查询常见问题  分页
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 * @throws UnsupportedEncodingException 
	 */
	JSONObject queryWenTiList(Context context, HttpServletRequest request)
			throws UnsupportedEncodingException;

	/**
	 * 查询费率
	 * 
	 * @param context
	 * @param fundId
	 *            产品ID，不可为空
	 * @param channelNo
	 *            用户购买的渠道号，如果还没有获取，可以传空串
	 * @param custLevel
	 *            客户等级，目前全部传空串
	 * @return QueryMessageDto.data = Map<ChannelNo, List<FeeRateDto>>
	*/	
	JSONObject queryFeeRateList(HttpServletRequest request, String money,
			Context context, String fundId, List<String> channelNoList,
			String string);

	/**
	 * 广告查询
	 * 
	 * @param context
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto queryAdvert(Context context,String type);

	/**
	 * 根据交易账号查询银行信息
	 * 
	 * @param context
	 * @param tradeacco
	 * @return
	 */
	JSONObject queryBankInfoByTradeAcco(Context context, String tradeacco);

	/**
	 * 查询用户代销机构订单列表
	 * 
	 * @param context
	 * @param cmfUserId
	 * @return QueryMessageDto
	 * @author luos
	 */
	JSONObject queryAgentFundInfo(Context context, String cmfUserId);

	/**
	 * 查询合作机构和代销机构
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryCooperation(Context context, HttpServletRequest request);

	/**
	 * 查询用户的原始银行卡信息(未经过加密处理)
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author luos
	 */
	JSONObject getTradeOriginalAcctInfoByCustno(Context context,
			HttpServletRequest request);

	/**
	 * 下载中心查询
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryFileDownloadCenter(Context context,
			HttpServletRequest request);

	/**
	 * 查询下载中心详细内容 并下载
	 * 
	 * @param context
	 * @param fileId
	 * @return QueryMessageDto
	 * @author maj
	 */
	QueryMessageDto queryFileDownloadCenterDetail(Context context,
			String fileId, String fileTypeIds);

	/**
	 * 其它业务流程 查询
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryOpenAccountProcess(Context context,
			HttpServletRequest request);

	/**
	 * 查询特殊代销产品的订单详情
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author luos
	 */
	JSONObject queryOtherDetailOrder(Context context, HttpServletRequest request);

	/**
	 * 查询公司公告
	 * 
	 * @param context
	 * @param cmfUserId
	 * @return
	 */
	JSONObject queryUnReadFundReportsCountForCompany(Context context,
			String cmfUserId);

	/**
	 * 查询支持的在线支付的银行列表
	 * 
	 * @param context
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject getSupportPayBankDesc(Context context);
	
	QueryMessageDto queryUserTradeAcctInfo(Context context, String ecCustNo,
			String tradeNo, String fundid);
	
	/**
	 * 家庭住址 查询国籍-省份等地区信息 联动
	 * 
	 * @param context
	 * @param pmst
	 * @param pmky
	 * @param pmco
	 * @param pmv1
	 * @return
	 */
	QueryMessageDto queryHomeAddressIsWordWithLinkage(Context context,
			String pmst, String pmky, String pmco, String pmv1);

	/**
	 * 查询投资者教育信息
	 * 
	 * @param page
	 * @param name
	 * @param fileType
	 * @param cataLog
	 * @return
	 */
	public JSONObject queryEducationV2(Context context, String cmfUserId,
			int page, String name);

	public JSONObject queryInvestorArtivleByFileType(Context context,
			String fileType, int page, String name);

	/**
	 * 查询Mgm资料
	 * 
	 * @param context
	 * @param cataLog
	 * @param page
	 * @param catalogId 
	 * @return
	 */
	JSONObject queryMgmArticleAndCount(Context context, String cmfUserId,
			int page, String catalogId);

	/**
	 * 查询机构用户的非官网产品
	 * 
	 * @param fundAcco
	 * @param crmCustNo
	 * @param branchName
	 * @return
	 */
	public JSONObject queryCompanyUserProduct(String fundAcco,
			String crmCustNo, String branchName);

	/**
	 * 查询机构用户的产品总资产
	 * 
	 * @param fundAcco
	 * @param crmCustNo
	 * @param branchName
	 * @return
	 */
	public JSONObject queryCompanyUserFundTotal(String fundAcco,
			String crmCustNo, String branchName);
	
	/**
	 * 
	 * 查询机构用户的其他订单详情
	 * 
	 * @param serialno
	 * @param fundId
	 * @param fundAcco
	 * @param crmCustNo
	 * @param branchName
	 * @return
	 */
	public JSONObject queryCompanyOtherDetailOrder(String serialno,
			String fundId, String channelNo, String fundAcco, String crmCustNo,
			String branchName);

	/**
	 * 查询机构用户净值
	 * 
	 * @param fundId
	 * @return
	 */
	public JSONObject queryCompanyUserFundNetValue(String fundId,int page);

	/**
	 * 查询机构用户产品详情界面净值走势图
	 * 
	 * @param fundId 
	 * @param datetime
	 * @return
	 */
	public JSONObject queryCompanyUserFundNetValueByImgTable(String fundId,
			int datetime);

	/**
	 * 查询白名单内产品净值
	 * 
	 * @param paramOne
	 * @param paramTwo
	 * @param type
	 * @return
	 */
	public JSONObject queryInWhiteListFundNetValue(String paramOne, int type,
			String fundId, int page);

	/**
	 * 查询白名单内产品净值走势图
	 * 
	 * @param paramOne
	 * @param paramTwo
	 * @param paramThree
	 * @param type
	 * @param fundId
	 * @param dateTime
	 * @return
	 */
	public JSONObject queryInWhiteListFundNetValueByImgTable(String paramOne,
			int type, String fundId, int dateTime);

	/**
	 * 原创文章查询
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param page
	 * @param name
	 * @return
	 */
	JSONObject queryOriginalArticleAndCount(Context context, String cmfUserId,
			int page, String name);
	
	/**
	 * 根据产品id来查询可赎回的份额及银行卡信息
	 * 
	 * @param fundid
	 * @param custno
	 * @return
	 */
	public JSONObject queryCanRedeemBankInfoByFundCode(String fundid,
			String custno);

	/**
	 * 查询 指定页数展示的官网债券投资人员信息未下架文章和文章总数
	 *
	 * @param context
	 * @param cmfUserId
	 * @param page
	 * @param name
	 * @return
	 */
	JSONObject queryBondInvestorArticleAndCount(Context context,
			String cmfUserId, int page, String name);

	/**
	 * 根据身份证号查询合格投资者信息
	 * 
	 * @param idcard
	 * @return
	 */
	JSONObject queryQualifiedUserInfoByIdno(String idcard);

	/**
	 * 查询文件上传记录
	 * 
	 * @param cmfUserId
	 * @return
	 */
	JSONObject queryFileUploadRecord(String cmfUserId);

	/**
	 * 通过custno查询合格投资者条件（金融资产+投资经历
	 * 
	 * @param custno
	 * @return
	 */
	JSONObject queryAccreditedInvestorConditions(String cmfuserid,String custno);


	/**
	 * 开关(系统参数)查询
	 *
	 * @param context
	 * @param pmst
	 * @param pmky
	 * @param pmco
	 * @param pmv1
	 * @return
	 */
	List<ParameterDto> queryParameter(Context context, String pmst, String pmky, String pmco, String pmv1);

	/**
	 * 获取用户的专属顾问信息
	 *
	 * @param cmfUserId 用户编号
	 * @return
	 */
	CustserviceInfoDTO getConsultantInfo(String cmfUserId);
	
	/**
	 * 获取风险揭示函
	 * @param context
	 * @param cmfUserId
	 * @param fundId
	 * @param period
	 * @return
	 */
	JSONObject queryRiskTermList(Context context,String cmfUserId, String fundId, String period);

}
