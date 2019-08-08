package com.cmwa.ec.webapp.manager;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;

/**
 * 对公业务(信批)
 * 接口/类的功能说明：
 * @author maj
 * @version 1.0
 * @see 
 *
 * <p>History</p>
 * 2015年11月16日
 */
public interface CompanyManager {

	/**
	 * 对公用户 登录
	 * @param context
	 * @param branchName 营业执照名称
	 * @param fundAcco 基金账号
	 * @param branchLicense 营业执照注册号登录
	 * @return
	 * 			QueryMessageDto crmCustNo(queryMessage.getData(data))
	 * @author maj
	 */
	public QueryMessageDto branchUserLogin(Context context, String branchName, String fundAcco, String branchLicense);
	
	/**
	 * abs白名单用户登录
	 * @param context
	 * @param branchName
	 * @param fundAcco
	 * @param branchLicense
	 * @return 
	 * 			QueryMessageDto crmCustNo(queryMessage.getData(data))
	 */
	public QueryMessageDto absUserLogin(Context context, String branchName, String type, String branchLicense);
	
	/**
	 * 查询用户产品信息列表  对公  信披
	 * @param context
	 * @param fundAcco 
	 * @param beginIdx 开始条数
	 * @param amount 每页显示条数
	 * @param totalAmount 总条数，第一次为-1，之后页面传值
	 * @return
	 * 			QueryMessageDto list<FundReportsDto>
	 * @author maj
	 */
	public JSONObject queryUserMessageListForCompany(Context context, HttpServletRequest request);

	/**
	 * 查询用户产品信息详情  对公  信披
	 * @param context
	 * @param fundAcco
	 * @param msgId
	 * @return
	 * 			QueryMessageDto list<FundReportsDto>
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageForCompany(Context context, String fundAcco, String msgId,String msgType);

	/**
	 * 查询用户的 最新公告  对公  信披
	 * @param context
	 * @param fundAcco
	 * @param beginIdx	哪一条开始，固定只查7条 ，所以值为1
	 * @param amount	查多少条 值为7
	 * @return
	 * @returnCode:0000---成功,9000---关键参数为空,9999---系统错误
	 * 			QueryMessageDto list<FundReportsDto>
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageListByNewTimeForCompany(Context context, String fundAcco, int beginIdx, int amount);

	/**
	 * 根据产品id查询该产品的相关公告   对公
	 * @param context
	 * @param fundAcco
	 * @param fundId 	产品id
	 * @param beginIdx	哪一条开始，固定只查7条 ，所以值为1
	 * @param amount	查多少条 值为7
	 * @return 0000---成功,QRY-U005---当前用户未持有该产品, 9000---关键参数为空,9999---系统错误
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageListByFundIdForCompany(Context context, String fundAcco, String fundId, int beginIdx, int amount);

}
