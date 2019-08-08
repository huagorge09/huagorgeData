package com.cmwa.ec.webapp.client;

import com.cmwa.ec.base.dto.Context;
import com.cmwa.ec.query.facade.FundQueryService;
import com.cmwa.ec.query.facade.dto.common.QueryMessageDto;

public class CompanyClient {

	private FundQueryService fundQueryService;
	
	public void setFundQueryService(FundQueryService fundQueryService) {
		this.fundQueryService = fundQueryService;
	}

	/**
	 * 对公用户 登录 
	 * @param context
	 * @param branchName 营业执照名称
	 * @param fundAcco 基金账号
	 * @param branchLicense 营业执照注册号登录
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto branchUserLogin(Context context, String branchName, String fundAcco, String branchLicense) {
		QueryMessageDto queryMessageDto = fundQueryService.branchUserLogin(branchName, fundAcco, branchLicense);
		return queryMessageDto;
	}
	
	/**
	 * ABS白名单用户登录
	 * @param context
	 * @param branchName
	 * @param type
	 * @param branchLicense
	 * @return
	 */
	public QueryMessageDto absUserLogin(Context context, String branchName, String type, String branchLicense) {
		QueryMessageDto queryMessageDto = fundQueryService.absUserLogin(branchName, type, branchLicense);
		return queryMessageDto;
	}
	
	/**
	 * 查询用户产品信息列表	信披
	 * @param context
	 * @param fundAcco
	 * @param beginIdx 开始条数
	 * @param amount 每页显示条数
	 * @param totalAmount 总条数，第一次为-1，之后页面传值
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageListForCompany(Context context, String fundAcco, int beginIdx, int amount, int totalAmount, String type) {
		QueryMessageDto queryMessageDto = fundQueryService.queryUserMessageListForCompany(context, fundAcco, beginIdx, amount, totalAmount, type);
		return queryMessageDto;
	}

	/**
	 * 查询用户产品信息详情 信披
	 * @param context
	 * @param fundAcco
	 * @param msgId
	 * @param msgType
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageForCompany(Context context, String fundAcco, String msgId,String msgType) {
		QueryMessageDto queryMessageDto = fundQueryService.queryUserMessageForCompany(context, fundAcco, msgId, msgType);
		return queryMessageDto;
	}

	/**
	 * 查询用户的 最新公告		信披
	 * @param context
	 * @param fundAcco
	 * @param beginIdx	哪一条开始，固定只查7条 ，所以值为1
	 * @param amount	查多少条 值为7
	 * @return
	 * @returnCode:0000---成功,9000---关键参数为空,9999---系统错误
	 * 			QueryMessageDto list<FundReportsDto>
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageListByNewTimeForCompany(Context context, String fundAcco, int beginIdx, int amount) {
		QueryMessageDto queryMessageDto = fundQueryService.queryUserMessageListByNewTimeForCompany(context, fundAcco, beginIdx, amount);
		return queryMessageDto;
	}

	/**
	 * 根据产品id查询该产品的相关公告
	 * @param context
	 * @param fundAcco
	 * @param fundId 	产品id
	 * @param beginIdx	哪一条开始，固定只查7条 ，所以值为1
	 * @param amount	查多少条 值为7
	 * @return 0000---成功,QRY-U005---当前用户未持有该产品, 9000---关键参数为空,9999---系统错误
	 * 			QueryMessageDto list<FundReportsDto>
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageListByFundIdForCompany(Context context, String fundAcco, String fundId, int beginIdx, int amount) {
		QueryMessageDto queryMessageDto = fundQueryService.queryUserMessageListByFundIdForCompany(context, fundAcco, fundId, beginIdx, amount);
		return queryMessageDto;
	}
	
	/**
	 * 查询pdf公告  信批   对公
	 * @param context
	 * @param cmfUserId
	 * @param msgId
	 * @param msgType
	 * @return
	 * 			QueryMessageDto
	 * @author maj
	 */
	public QueryMessageDto queryUserMessageInfoPDFForCompany(Context context, String fundAcco, String msgId,String msgType){
		QueryMessageDto queryMessageDto = fundQueryService.queryUserMessageForCompany(context, fundAcco, msgId, msgType);
		return queryMessageDto;
	}

}
