package com.cmwa.ec.webapp.manager;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.dto.user.CmwaQualifiedUserInfoDto;
import com.cmwa.ec.trade.facade.dto.OrderResult;
import com.cmwa.ec.trade.facade.dto.fund.FundTradeDto;
import com.cmwa.ec.trade.facade.dto.fund.SignEContractDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;

import net.sf.json.JSONObject;

import javax.servlet.http.HttpServletRequest;

public interface TradeManager {

	/**
	 * 下预约单（预下单）
	 * @param context
	 * @param fundTradeDto
	 * @return
	 * 			OrderResult
	 * @author maj
	 */
	OrderResult fundAppoint(Context context, FundTradeDto fundTradeDto);

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
	OrderResult fundTrade(Context context, FundTradeDto fundTradeDto, SignEContractDto elDto);

	/**
	 * 用户鉴权开户
	 * @param context
	 * @param request
	 * @param userAcctDto
	 * @return
	 * @author luos
	 */
	JSONObject openAccount(Context context, HttpServletRequest request);

	/**
	 * 取消订单
	 * @param context
	 * @param fundTradeDto
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	JSONObject cancelAppointRequest(Context context, HttpServletRequest request);
	
	/**
	 * 赎回订单
	 * @param context
	 * @param request
	 * @return
	 */
	JSONObject redemptionOrder(Context context, HttpServletRequest request,FundTradeDto fundTradeDto);
	
	/**
	 * 撤销已绑定的银行卡
	 * @param context
	 * @param userAcctDto
	 * @return JSONObject
	 */
	JSONObject cancelBindBankCard(Context context, HttpServletRequest request, String tradeAcc);
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
	OrderResult addReportReadRecord(Context context, String cmfUserId, String fundId, String reportId);

	/**
	 * 下排队单
	 * @param context
	 * @param request
	 * @return
	 * 			JSONObject
	 * @author maj
	 * @throws Exception 
	 */
	JSONObject fundTradeLineUp(Context context, HttpServletRequest request) throws Exception;
	/**
	 * 添加银行卡
	 * @return
	 * 			JSONObject
	 * @author luos
	 */
	JSONObject addBankNumber(Context context, HttpServletRequest request);
	/**
	 * 修改订单金额
	 * @param context
	 * @param fundTradeDto
	 * @return
	 * 			OrderResult
	 * @author maj
	 */
	OrderResult modifyAppointRequest(Context context, FundTradeDto fundTradeDto);

	/**
	 * 风险测评  评级
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject setUserRiskLevel(Context context, HttpServletRequest request);

	/**
	 * 银行支付签约接口
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject contractSign(Context context, HttpServletRequest request);

	/**
	 * 银行指令状态查询（主要用于查询工行签约结果，其他场景依情况调用）
	 * @param context
	 * @param request
	 * @return JSONObject
	 * @author maj
	 */
	JSONObject queryCommandByBankAccoNo(Context context, HttpServletRequest request);
	
	/**
	 * 修改客户产品到期分配方式
	 * @param context
	 * @param serialno
	 * @param renew
	 * @return
	 */
	public JSONObject updateAppointRequest(Context context, HttpServletRequest request);

	/**
	 * 
	* 接口的功能说明：设置用户风险等级
	* @param context
	* @param userRiskLevelDto
	* @return
	* @throws 
	 */
	public OrderResult appConversionUserInvtp(Context context, HttpServletRequest request);
	
	/**
	 * 通过知识测评，修改投资者类型为专业投资者
	 * @param cmfUserId
	 * @param custNo
	 */
	public OrderResult passTestUpdateUserInvtp(Context context,String cmfUserId,String custNo,String invprtpScore);
	
	/**
	 * 取消申请专业投资者
	 * @param cmfUserId
	 * @param custNo
	 */
	public OrderResult cancelAppConversionUserInvtp(Context context,String cmfUserId,String custNo); 
	
	/**
	 * 添加用户操作日志
	 * @param strObject
	 */
	public void addOpLog(String strObject);
	/**
	 * 更新客户订单的赎回份额
	 * @param dto
	 * @return
	 */
	public OrderResult updateOrderRedemptionShare(FundTradeDto dto);

	/**
	 * 添加合格投资者信息
	 * @param param
	 * @return
	 */
    JSONObject addAccreditedInvestorInfo(CmwaQualifiedUserInfoDto param,UserBaseInfoDto dto);

	/**
	 * 修改已提交的合格投资者申请文件信息
	 * @param key
	 * @param custno
	 * @param operatorType
	 * @param fileName
	 * @param fileType
	 * @param currentUser
	 * @return
	 */
    JSONObject updateSubmittedInfo(String key, String custno, String operatorType, String fileName, String fileType, String currentUser);
    
    /**
     * 删除文件上传记录
     * @param recordId
     * @return
     */
	JSONObject deleteFileUploadRecord(String recordId);

	/**
	 * 修改合格投资者申请状态
	 * @param custno
	 * @param updateStatus
	 * @param currentUserInfo
	 * @return
	 */
	JSONObject updatedAccreditedInvestorRequestStatus(String custno,
			String updateStatus,UserBaseInfoDto currentUserInfo);
	/**
	 * 保存用户风险揭示函勾选记录信息
	 * @param context
	 * @param cmfUserId
	 * @param fundId
	 * @param period
	 * @param ids
	 * @return
	 */
	JSONObject saveUserTermsInfo(Context context,String cmfUserId,String fundId,String period,String ids,String type);
	
	/**
	 * 修改订单申请状态
	 * @param dto
	 * @return
	 */
	public OrderResult updateOrderApplyst(FundTradeDto dto);
}
