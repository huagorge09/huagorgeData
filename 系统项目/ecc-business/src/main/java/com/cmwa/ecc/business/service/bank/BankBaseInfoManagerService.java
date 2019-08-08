package com.cmwa.ecc.business.service.bank;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ecc.business.entity.bank.BankBnkbaseVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface BankBaseInfoManagerService {
	
	/**
	 * 获取银行基本信息列表
	 * @param sp
	 * @return
	 */
	public Page<BankBnkbaseVo> queryBankBaseInfoListPage(SearchParam sp);

	
	/**
	 * 删除银行基本信息
	 * @param request
	 * @return
	 */
	public JSONObject delBankBase(HttpServletRequest request);

	
	/**
	 * 增加/修改页面分发
	 * @param request
	 * @return
	 */
	public ModelAndView openDialog(HttpServletRequest request);

	/**
	 * 新增银行基本信息管理
	 * @param bankBaseInfo
	 * @return
	 */
	public JSONObject addBankBaseInfo(BankBnkbaseVo bankBaseInfo);


	/**
	 * 修改银行基本信息管理
	 * @param bankBaseInfo
	 * @return
	 */
	public JSONObject modBankBaseInfo(BankBnkbaseVo bankBaseInfo);


}
