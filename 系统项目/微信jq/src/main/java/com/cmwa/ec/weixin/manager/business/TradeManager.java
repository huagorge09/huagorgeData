package com.cmwa.ec.weixin.manager.business;

import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.dto.user.CmwaQualifiedUserInfoDto;
import com.cmwa.ec.trade.facade.dto.OrderResult;
import com.cmwa.ec.trade.facade.dto.fund.FundTradeDto;
import com.cmwa.ec.trade.facade.dto.fund.SignEContractDto;
import com.cmwa.ec.trade.facade.dto.user.UserAcctDto;
import com.cmwa.ec.trade.facade.dto.user.UserRiskLevelDto;
import com.cmwa.ec.user.facade.dto.UserBaseInfoDto;


/**
 * 交易模块管理接口
 * @author liury
 *
 */
public interface TradeManager {

	/**
	 * 鉴权开户
	 * @param context
	 * @param request
	 * @param userAcctDto
	 * @return
	 */
	public JSONObject openAccount(Context context, HttpServletRequest request,UserAcctDto userAcctDto);
	
	/**
	 * 用户添加银行卡
	 * @param context
	 * @param userAcctDto
	 * @return 
	 */
	public JSONObject addBankNumber(Context context, HttpServletRequest request,UserAcctDto userAcctDto);
	
	
	/**
	 * 解除银行卡绑定
	 * @param context
	 * @param dto
	 * @return
	 */
	public JSONObject cancelBindBankCard(Context context,String tradeAcco, String ecCustNo);
	
	/**
	 * 用户预下单
	 * @param context
	 * @param dto
	 * @return
	 */
	public JSONObject fundAppoint(Context context ,FundTradeDto dto);
	
	
	/**
	* 下交易单
	* @param context	会话信息
	* @param dto		购买要素
	* @param ecDto		电子合同
	* @other PayType=(0,1), apkind=A2T
	* @return			接口，data.type = FundTradeDto 	
	* @throws 
	* @author wudb
	*/
	public JSONObject fundTrade(Context context,FundTradeDto dto,SignEContractDto elDto);

	/**
	 * 风险测评
	 * @param context
	 * @param userRiskLevel
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	public JSONObject riskRating(Context context, UserRiskLevelDto userRiskLevel,String userType);

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
	public OrderResult addReportReadRecord(Context context, String cmfUserId, String fundId, String reportId);

	/**
	 * 取消订单
	 * @param context
	 * @param fundTrade
	 * @return
	 * 			OrderResult
	 * @author maj
	 */
	public OrderResult cancelAppointRequest(Context context, FundTradeDto fundTradeDto);

	/**
	 * 修改订单金额
	 * @param context
	 * @param fundTradeDto
	 * @return
	 * 			OrderResult
	 * @author maj
	 */
	public OrderResult modifyAppointRequest(Context context, FundTradeDto fundTradeDto);

	/**
	 * 下排队单
	 * @param context
	 * @param fundTradeDto
	 * @param elDto
	 * @return
	 * 			JSONObject
	 * @author maj
	 */
	public OrderResult fundTradeLineUp(Context context, FundTradeDto fundTradeDto, SignEContractDto elDto);
	
	/**
	 * 身份鉴权（找回交易密码）
	 */
	public JSONObject authenticate(Context context, UserAcctDto userAcctDto);
	
	/**
	   * 扫码参与活动
	   * @param context
	   * @param openId 用户微信openId
	   * @param activityId 活动id
	   * @param md5Info 活动码MD5值
	   * @return 
	   * 返回0000，表示参与有效，即二维码绑定成功
	   * 返回0010，表示扫描的二维码无效
	   * 返回0020,表示当前用户绑定的二维码已使用（本人使用）
	   */
	public String activityScan(Context context, String openId, String activityId,String md5Info);
	/**
	 * 用户注册后，更新对应参加活动的cmfUserId
	 * @param context
	 * @param openId
	 * @param cmfUserId
	 * @param activityId
	 * @return 0：更新失败  1：更新成功
	 */
	public int updateCmfUserId4Activity(Context context,  String openId,String cmfUserId, String activityId);
	
	/**
	    * 根据用户openId和cmfUserId查询用户参与的活动
	    * @param context
	    * @param openId
	    * @param cmfUserId
	    * @return
	    */
	public String queryActivityByUserInfo(Context context, String openId, String cmfUserId);

	/**
	 * 公告已读保存
	 * @param context
	 * @param cmfUserId
	 * @param articleId
	 * @return
	 * 			OrderResult
	 * @author maj
	 */
	public OrderResult addArticleReadRecord(Context context, String cmfUserId, String articleId);

	/**
	 * 
	* 接口的功能说明：设置用户风险等级
	* @param context
	* @param userRiskLevelDto
	* @return
	* @throws 
	 */
	public OrderResult appConversionUserInvtp(Context context, HttpServletRequest request);
	
	public OrderResult appConversionUserInvtp(Context context, HttpServletRequest request,UserRiskLevelDto userRiskLevelDto);
	
	/**
	 * 
	* 接口的功能说明： 删除请求数据
	* @param context
	* @param userRiskLevelDto
	* @return
	* @throws 
	 */
	public OrderResult clearAppcvInvp(Context context,HttpServletRequest request);
	
	
	/**
	 * 解析风险问卷 答案
	 * @param oldEvalAnswer
	 * @return
	 */
	public String parseEvalAnswerBySetUserRiskLevel(String oldEvalAnswer);
	
	/**
	 * 取消申请专业投资者
	 * @param cmfUserId
	 * @param custNo
	 */
	public OrderResult cancelAppConversionUserInvtp(Context context,String cmfUserId,String custNo);
	
	/**
	 * 通过知识测评，修改投资者类型为专业投资者
	 * @param cmfUserId
	 * @param custNo
	 */
	public OrderResult passTestUpdateUserInvtp(Context context,String cmfUserId,String custNo,String invprtpScore);
	
	/**
	 * 修改客户产品到期分配方式
	 * @param context
	 * @param serialno
	 * @param renew
	 * @return
	 */
	public JSONObject updateAppointRequest(Context context, HttpServletRequest request);

	/**
	 * 赎回订单
	 * @param context
	 * @param request
	 * @param fundTradeDto 
	 * @param cmfUserId 
	 * @return
	 */
	public JSONObject redemptionOrder(Context context, HttpServletRequest request, FundTradeDto fundTradeDto, String cmfUserId);

	/** 
	 * 活动开户接口 （招行卡银联鉴权失败不走招行鉴权接口）
	 * @param context
	 * @param request
	 * @param userAcctDto
	 * @return
	 */
	public JSONObject openAccountForActivity(Context context,
			HttpServletRequest request, UserAcctDto userAcctDto);

	/**
	 * 添加赎回操作日志 
	 * @param string
	 */
	public void addOpLog(String string);
	
	/**
	 * 更新订单赎回份额
	 * @param fundTrade
	 * @return
	 */
	public OrderResult updateOrderRedemptionShare(FundTradeDto fundTradeDto);

	/**
	 * 添加合格投资者信息
	 * @param param
	 * @return
	 */
    JSONObject addAccreditedInvestorInfo(CmwaQualifiedUserInfoDto param,UserBaseInfoDto currentUserInfo);

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
			String updateStatus, UserBaseInfoDto currentUserInfo);
	
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
	 * 撤销赎回订单
	 * @param context
	 * @param serialNo
	 * @return
	 */
	JSONObject revokeRedeemOrder(Context context, String serialNo, String custNo);
}
