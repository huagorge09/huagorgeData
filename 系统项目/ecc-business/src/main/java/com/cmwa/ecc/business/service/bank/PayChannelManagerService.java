package com.cmwa.ecc.business.service.bank;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.entity.bank.BankPayChannelVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface PayChannelManagerService {
	
	public Page<BankPayChannelVo> queryPayChannelListPage(SearchParam sp);

	public ModelAndView openDialog(HttpServletRequest request);

	
	/**
	 * 新增支付渠道
	 * @param payChannelVo
	 * @return
	 */
	public JSONObject addPayChannel(BankPayChannelVo payChannelVo);
	
	/**
	 * 删除支付渠道
	 * @param request
	 * @return
	 */
	public JSONObject delPayChannel(HttpServletRequest request);

	/**
	 * 修改支付渠道
	 * @param payChannelVo
	 * @return
	 */
	public JSONObject updatePayChannel(BankPayChannelVo payChannelVo);

}
