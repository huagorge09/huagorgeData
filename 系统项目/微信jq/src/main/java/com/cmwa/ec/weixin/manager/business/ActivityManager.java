package com.cmwa.ec.weixin.manager.business;

import javax.servlet.http.HttpServletRequest;

import com.cmwa.ec.weixin.dto.InvestorUserDto;
import com.cmwa.ec.weixin.dto.InvectorUserInfoexDto;

import net.sf.json.JSONObject;

/**
 * 活动管理接口
 * @author luos
 *
 */
public interface ActivityManager {
	/**
	 * 根据userid查询用户信息
	 * @param openId
	 */
	public InvectorUserInfoexDto queryUserInfoByUerId(String userid);
	/**
	 * 奖品领取成功后推送图文消息
	 * @param openId
	 * @param resourceName 奖品名称
	 * @param resourceType 奖品类型 
	 * @param info	奖品扩展信息
	 * @param userName	获奖人名称
	 * @param mobile	手机号码
	 */
	public void pushRefflePrizeSuccMsg(String openId, String ticket1, String ticket2);
	/**
	 * 父亲节领取电影券
	 * @param request
	 * @param context 
	 * @author luos
	 * @return 
	 */
	public JSONObject fatherDayDrawTicket(HttpServletRequest request);
	/**
     * 自动登录，用于不需要登录的页面，进入页面自动登录 仅限于微信端
     * 
     * @param openId
     * @param request
     *            void
     * @author maj
     */
    public void autoLogin(HttpServletRequest request);
    /**
     * 七夕问答提交
     * @param request
     * @return
     */
	public JSONObject magpieFestivalQA(HttpServletRequest request);
	/**
	 * 七夕答题领券
	 * @param request
	 * @return
	 */
	public JSONObject magpieFestivalDrawTicket(HttpServletRequest request);
	/**
	 * 七夕节推送领券成功信息
	 * @param openId
	 * @param ticket
	 */
	void magpieFestivalDrawTicketPushRefflePrizeSuccMsg(String openId, String ticket1,String ticket2);
	/**
     * 国庆问答提交
     * @param request
     * @return
     */
	public JSONObject nationalDayQA(HttpServletRequest request);
	
	/**
	 * 投资者教育活动
	 * 用户转发
	 * @param openId
	 * @return
	 */
	public JSONObject userForward(String openId,String type);
	
	/**
	 * 投资者教育活动
	 * 用户答题
	 * @param openId
	 * @param score
	 * @return
	 */
	public JSONObject userAnswer(String openId,String score);
	
	/**
	 * 投资者教育活动
	 * 用户抽奖
	 * @param openId
	 * @return
	 */
	public JSONObject userLuckDraw(String openId);
	
	/**
	 * 投资者教育活动
	 * 用户兑奖
	 * @param investorUserDto
	 * @return
	 */
	public JSONObject userGetAward(InvestorUserDto investorUserDto);
	
	/**
	 * 投资者教育活动
	 * 查询用户奖品
	 * @param openId
	 * @return
	 */
	public JSONObject queryUserAward(String openId);
	
	/**
	 * 投资者教育活动
	 * 验证用户是否可以抽奖
	 * @param openId
	 * @return
	 */
	public JSONObject checkUserIsCanLuckDraw(String openId);
	
	/**
	 * 投资者教育活动
	 * 查询投教活动的开关配置
	 * @param pmst
	 * @param pmky
	 * @param pmnm
	 * @return
	 */
	public String queryInvestorEduActParameter(String pmst,String pmky,String pmnm);
	
	/**
	 * 查询银行徽标
	 * @return
	 */
	public JSONObject queryBankLogoList();
	
}
