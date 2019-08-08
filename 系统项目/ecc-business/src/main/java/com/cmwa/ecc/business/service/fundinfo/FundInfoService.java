package com.cmwa.ecc.business.service.fundinfo;

import java.util.List;

import com.cmwa.ecc.business.entity.accountManger.FundBalanceDto;
import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.entity.fundinfo.ProductInfoDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 产品-操作接口
 * @author ex-liuy
 *
 */
public interface FundInfoService {
	/**
	 * 页面插件-查询所有产品
	 * @param sp
	 * @return
	 */
	public List<FundInfoVo> queryMatchFundInfoList(SearchParam sp);
	
	/**
	 * 直销系统查询基金（包括限制的基金）
	 * @param fundst
	 * @param tano
	 * @return
	 */
	public List<FundInfoVo> getAllSubFundsArray(String fundst,String tano); 
	
	/**
	 * 获取指定基金代码的基金信息
	 * @param fundId 基金代码
	 * @return FundInfoVo 基金信息DTO
	 */
	FundInfoVo getFundInfo(String fundId);
	
	/**
     * 短期理财产品余额查询
     * @param custno
     * @param tradeAcco
     * @param quryType
     * @return
     */
    public List<FundBalanceDto> getFinanceBalance(String custno, String tradeAcco, String cycleenddt, String fundId, String quryType);
    
    /**
	 * 获取所有的基金信息
	 * @return List<FundInfoVo>
	 */
	List<FundInfoVo> getAllFundInfo();
	
	/**
	 * 下单
	 * 获取产品详情列表
	 * @param sp
	 * @return
	 */
	public Page<ProductInfoDto> getOrderFundInfoList(SearchParam sp);
	
	/**
     * 短期理财产品余额查询（临时）
     * @param custno
     * @param tradeAcco
     * @param quryType
     * @return
     */
    public List getFinanceBalcons(String custno, String tradeAcco, String cycleenddt, String fundId, String quryType);
}
