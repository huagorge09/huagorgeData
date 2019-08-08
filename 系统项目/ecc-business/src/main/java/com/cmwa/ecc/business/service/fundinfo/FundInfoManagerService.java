package com.cmwa.ecc.business.service.fundinfo;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.cmwa.ecc.business.entity.fundinfo.FundBalanceVo;
import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.entity.fundinfo.FundManagerVo;
import com.cmwa.ecc.business.entity.fundinfo.FundStopVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface FundInfoManagerService {
	/**
	 * 增加基金信息
	 * @param Vo 基金信息Vo
	 * @return boolean
	 */
	boolean addFundInfo(FundManagerVo vo);
	
	/**
	 * 删除一个基金
	 * @param fundId 基金代码
	 * @return boolean
	 */
	boolean delFundInfo(String fundId);
	
	/**
	 * 修改基金信息
	 * @param Vo 基金信息Vo
	 * @return boolean
	 */
	boolean modifyFundInfo(FundManagerVo Vo);
	
	/**
	 * 获取所有的基金信息
	 * @return List<FundInfoVo>
	 */
	List<FundManagerVo> getAllFundInfo();
	
	/**
	 * 获取专户产品的基金信息
	 * @return
	 * @throws Exception
	 */
	List<FundManagerVo> getSpecProFundInfo();
	
	/**
	 * 获取指定基金代码的基金信息
	 * @param fundId 基金代码
	 * @return FundInfoVo 基金信息Vo
	 */
	FundManagerVo getFundInfo(String fundId);

    
    //缓存接口
    /**
     * 获取所有基金（缓存用）
     * @return List
     */
    public abstract List<FundManagerVo> getCachedFunds();

    /**
	 * 20110419 wangdw add
	 * 新增基金级暂停交易数据
	 * 
	 */
	public String[] addFundStop(FundStopVo Vo);
	/**
	 * 20110419 wangdw add
	 * 基金级暂停交易复核
	 * 
	 */
	public String[] checkFundStop(FundStopVo Vo);
	/**
	 * 20110419 wangdw add
	 * 基金级暂停交易删除
	 * 
	 */
	public String[] deleteFundStop(FundStopVo Vo);
	/**
	 * 20110420 wangdw add
	 * 基金级暂停交易修改
	 * 
	 */
	public String[] updateFundStop(FundStopVo Vo);
	/**
	 * 直销系统查询基金（包括限制的基金）
	 * @param fundst
	 * @param tano
	 * @return
	 */
	public List<FundManagerVo> getAllSubFundsArray(String fundst,String tano); 
	
    /**
     * 同步成立日期
     * @param fundid  基金代码
     * @return boolean
     */
    public SearchParam syschonizeSetUpDate(String fundid);
    
    
    /**
     * 增加未复核的基金信息
     * @param Vo 基金信息Vo
     * @return boolean
     */
    public boolean addChkFundInfo(FundManagerVo Vo);

    /**
     * 删除一个未复核的基金
     * @param fundId 基金代码
     * @return boolean
     */
    public boolean delChkFundInfo(String fundId, String processor);

    /**
     * 修改未复核的基金信息
     * @param Vo 基金信息Vo
     * @return boolean
     */
    public boolean modifyChkFundInfo(FundManagerVo Vo);

    /**
     * 获取指定基金代码的未复核的基金信息
     * @param fundId 基金代码
     * @return FundInfoVo 基金信息Vo
     */
    public FundManagerVo getChkFundInfo(String fundId);
    
    /**
     * 基金信息复核
     * @param fundId 基金代码
     * @param checker 复核人
     * @param checkstatus 复核状态   Y:复核通过   N:复核未通过    I:未复核
     * 
     * @return String[]   [0]:返回码    [1]:返回信息
     */
    public String[] checkFundInfo(String fundId, String checker, String checkstatus);
    
    /**
	 * added by zengxy 20131121
	 * 获取所有待复核的基金信息
	 * @return List<FundInfoVo>
	 * @throws Exception
	 */
	public Page<FundManagerVo> getAllChkFundInfoListPage(SearchParam sp);
	/**
	 * added by zengxy 20131121
	 * 获取所有的基金信息
	 * @return List<FundInfoVo>
	 * @throws Exception
	 */
	Page<FundManagerVo> getAllFundInfoListPage(SearchParam sp);
	
	/**
     * 短期理财产品余额查询
     * @param custno
     * @param tradeAcco
     * @param quryType
     * @return
     */
    public List<FundBalanceVo> getFinanceBalance(String custno, String tradeAcco, String cycleenddt, String fundId, String quryType);
}
