package com.cmwa.ec.weixin.manager.business;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.cmwa.ec.query.facade.dto.system.ParameterDto;
import com.cmwa.ec.query.facade.model.consultant.CustserviceInfoDTO;

import net.sf.json.JSONObject;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;
import com.cmwa.ec.user.facade.dto.UserServiceMessage;

public interface QueryManager {
	/**
	 * 查询用户基本信息和关联信息
	 * 
	 * @param context
	 * @param cmfUserId
	 * @return
	 */
	public UserServiceMessage queryUserInfoByCmfUserId(Context context, String cmfUserId);

	/**
	 * 查询产品列表
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public JSONObject queryFundList(Context context);

	/**
	 * 查询热销产品列表
	 * 
	 * @param request
	 * @param context
	 * @return
	 */
	public JSONObject queryHotFundList(Context context);

	public JSONObject queryFundInfo(Context context, String fundId, String period);

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
	public JSONObject queryFeeRateList(HttpServletRequest request, String money, Context context, String fundId, List<String> channelNoList, String custLevel);

	/**
	 * 根据产品id查询产品的合同， 新的合同查询接口，所有产品统一只有一个合同
	 * 
	 * @param context
	 * @param fundId
	 * @param type
	 *            操作类型
	 * @return returnCode:0000---成功, 9000---关键参数为空,9999---系统错误 <br>
	 *         date: 2015-8-24
	 */
	public JSONObject queryFundContractById(Context context, String fundId, String type, String period);

	/**
	 * 查询我的银行卡信息
	 * 
	 * @param context
	 * @param ecCustNo
	 * @return QueryMessageDto
	 * @author liury
	 * @param b
	 */
	public JSONObject getTradeAcctInfoByCustno(Context context, String ecCustNo, boolean b, String fundid);

	/**
	 * 根据银行卡号查找该卡号的具体银行信息 即卡号校验
	 * 
	 * @param context
	 * @param bankNumber
	 * @return
	 */
	public JSONObject queryBankInfoByBankNumber(Context context, String bankNumber);

	/**
	 * 查询资产和总收益
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param userBaseInfo
	 * @param sessionId
	 * @return JSONObject
	 * @author maj
	 */
	public JSONObject queryTotalFundBalance(Context context, String cmfUserId, UserBaseInfoDto userBaseInfo, String sessionId);

	/**
	 * 查询支持的银行卡列表
	 * 
	 * @param context
	 * @return
	 */
	public JSONObject getSupportBankDesc(Context context);

	/**
	 * 查询用户订单列表
	 * 
	 * @param context
	 * @param custno
	 * @return JSONObject
	 * @author maj
	 */
	public QueryMessageDto queryTradeInfoList(Context context, String custno, String fundid, String applyst);
	
	/**
	 * 查询用户产品持仓
	 * @param context
	 * @param cmfUserId
	 * @param fundCode
	 * @return
	 */
	JSONObject queryCustTradeInfo(Context context, String cmfUserId , String  fundCode,String tradeAcco);
	
	/**
	 * 查询是否新客户
	 * @param context
	 * @param cmfUserId
	 * @param fundCode
	 * @return
	 */
	JSONObject queryNewCustomer(Context context, String cmfUserId , String  fundCode);
	
	/**
	 * 查询用户未读信批条数
	 * 
	 * @param context
	 * @param cmfUserId
	 * @return JSONObject
	 * @author maj
	 */
	public JSONObject queryUnReadFundReportsCount(Context context, String cmfUserId);

	/**
	 * 信批 查询“我的消息”
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
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageList(Context context, String cmfUserId, int beginIdx, int amount, int totalAmount);

	/**
	 * 信批 详情查询
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param msgId
	 * @param msgType
	 * @return QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryUserMessage(Context context, String cmfUserId, String msgId, String msgType);

	/**
	 * 信批 详情查询 PDF
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param msgId
	 * @param msgType
	 * @return QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageByPDF(Context context, String cmfUserId, String msgId, String msgType);

	/**
	 * 查询历史交易信息 对账单
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param startDate
	 * @param endDate
	 * @return QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryFundTradeInfo(Context context, String cmfUserId, String startDate, String endDate);

	/**
	 * 查询订单详情
	 * 
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author ls
	 */
	public JSONObject queryTradeInfoByTradeNo(Context context, HttpServletRequest request);

	/**
	 * 根据tradeAcco获取交易账号
	 * 
	 * @param context
	 * @param tradeacco
	 * @return QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto getTradeAcctInfoByTradeAcco(Context context, String tradeacco);

	/**
	 * 查询用户代销机构订单列表
	 * 
	 * @param context
	 * @param cmfUserId
	 * @return QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryAgentFundInfo(Context context, String cmfUserId);

	/**
	 * 最新公告列表查询 分页
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param beginrow
	 *            开始条数
	 * @param endrow
	 *            结束条数
	 * @return QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryCompanyArticle(Context context, String cmfUserId, int beginrow, int endrow);

	/**
	 * 查询公司公告(总数)
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param beginrow
	 *            开始条数
	 * @param endrow
	 *            结束条数
	 * @return QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryCompanyArticleCount(Context context, String cmfUserId);

	/**
	 * 最新动态、公司公告
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param articleId
	 * @return QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryArticleContent(Context context, String articleId);

	/**
	 * 查询新闻动态公告 分页
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param beginrow
	 * @param endrow
	 * @return QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryNewsArticle(Context context, String cmfUserId, int beginrow, int endrow);

	/**
	 * 查询新闻动态公告(总数)
	 * 
	 * @param context
	 * @param cmfUserId
	 * @return QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryNewsArticleCount(Context context, String cmfUserId);

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
	 * 查询单个产品
	 * 
	 * @param context
	 * @param fundId
	 * @return QueryMessageDto
	 * @author luos
	 */
	QueryMessageDto queryFund(Context context, String fundId);

	/**
	 * 查询微信banner图片列表
	 * 
	 * @param context
	 * @param request
	 * @return
	 */
	public JSONObject queryBanner(Context context, String type);

	/**
	 * 查询产品净值
	 * 
	 * @param context
	 * @param request
	 * @return
	 */
	public JSONObject queryEstimateByFundId(Context context, HttpServletRequest request);

	/**
	 * 查询产品净值（净值类产品） 有分页 无时间条件
	 * 
	 * @param request
	 * @author luos
	 */
	public JSONObject queryEstimateByFundIdByPage(Context context, HttpServletRequest request);


	/**
	 * 查询用户交易账号列表 手机号码脱敏
	 * 
	 * @param response
	 * @param request
	 * @throws Exception
	 *             void
	 * @author maj
	 */
	public QueryMessageDto queryUserTradeAcctInfo(Context context, String ecCustNo, String tradeNo, String fundid);

	/**
	 * 查询参数信息
	 * 
	 * @param context
	 * @param pmst
	 * @param pmky
	 * @param pmco
	 * @param pmv1
	 * @return
	 */
	QueryMessageDto queryParamList(Context context, String pmst, String pmky, String pmco, String pmv1);

	/**
	 * 方法说明：根据用户cmfUserId查询用户最新测评时间
	 * 
	 * @param cmfUserId
	 *            [必填]用户ID
	 * @return UserServiceMessage 返回码\返回信息\用户基本信息Dto 返回码如下 查询成功 0000 userId必填
	 *         USR-B005 无效ID USR-A018 系统运行时不可知异常 USR-8000
	 */
	public UserServiceMessage queryUserRiskEvalDateByCmfUserId(String cmfUserId);

	/**
	 * 根据消息标题模糊查询
	 * 
	 * @param context
	 * @param cmfUserId
	 * @param title
	 * @return
	 */
	public JSONObject queryUserMsgLikeTitle(Context context, String cmfUserId, String title);

	/**
	 * 查询用户专属顾问
	 * @param openid
	 * @return
	 */
	public JSONObject queryUserCustService(String openid);

	public JSONObject queryProfitByFundCode(Context context, String cmfUserId,
			String fundCode);

	public JSONObject queryTradeInfoByCustNo(Context context, String custno,
			String fundid, String subquty);

	/**
	 * 根据产品id来查询可赎回的份额及银行卡信息
	 * @param fundid
	 * @param custno
	 * @return
	 */
	public JSONObject queryCanRedeemBankInfoByFundCode(String fundid, String custno);

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
	 * 根据身份证号查询合格投资者信息
	 * @param idcard
	 * @return
	 */
	public JSONObject queryQualifiedUserInfoByIdno(String idNo);

	/**
	 * 查询文件上传记录
	 * @param cmfUserId
	 * @return
	 */
	public JSONObject queryFileUploadRecord(String cmfUserId);

	
	/**
	 * 通过custno查询合格投资者条件（金融资产+投资经历
	 * 
	 * @param custno
	 * @param cmfuserid
	 * @return
	 */
	JSONObject queryAccreditedInvestorConditions(String cmfuserid,String custno);
	
	/**
	 * 获取风险揭示函
	 * @param context
	 * @param cmfUserId
	 * @param fundId
	 * @param period
	 * @return
	 */
	JSONObject queryRiskTermList(Context context,String cmfUserId, String fundId, String period);
    
    public UserServiceMessage queryUserInfoByMobile(String mobile);
}
