package com.cmwa.ecc.business.dao.fundInfoManager;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.entity.fundinfo.FundManagerVo;
import com.cmwa.ecc.business.entity.fundinfo.FundStopVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface FundInfoManagerDao {
	/**
	 * 增加基金信息
	 * @param Vo 基金信息Vo
	 * @return void
	 * @throws Exception
	 */
	void addFundInfo(FundManagerVo Vo) throws Exception;
	
	/**
	 * 删除一个基金
	 * @param fundId 基金代码
	 * @return void
	 * @throws Exception
	 */
	void delFundInfo(@Param("fundId")String fundId) throws Exception;
	
	/**
	 * 修改基金信息
	 * @param Vo 基金信息Vo
	 * @return void
	 * @throws Exception
	 */
	void modifyFundInfo(FundManagerVo Vo) throws Exception;
	
	/**
	 * 获取所有的基金信息
	 * @return List
	 * @throws Exception
	 */
	List<FundManagerVo> getAllFundInfo() throws Exception;
	
	/**
	 * 获取专户产品的基金信息
	 * @return
	 * @throws Exception
	 */
	List<FundManagerVo> getSpecProFundInfo() throws Exception;
	
	/**
	 * added by zengxy 20131121
	 * 获取所有的基金信息
	 * @return List
	 * @throws Exception
	 */
	List<FundManagerVo> getAllFundInfoListPage(SearchParam sp) throws Exception;
	
	/**
	 * 获取指定基金代码的基金信息
	 * @param fundId 基金代码
	 * @return FundInfoVo 基金信息Vo
	 * @throws Exception
	 */
	FundManagerVo getFundInfo(@Param("fundId")String fundId) throws Exception;


	/**
	 * 获取所有基金（缓存用）
	 * @return List
	 * @throws Exception
	 */
	List<FundManagerVo> getCachedFunds() throws Exception;
	
	/**
	 * 20110419 wangdw add
	 * 新增基金级暂停交易数据
	 * 
	 */
	public void addFundStop(FundStopVo Vo);
	/**
	 * 20110419 wangdw add
	 * 基金级暂停交易复核
	 * 
	 */
	public void checkFundStop(FundStopVo Vo);
	/**
	 * 20110419 wangdw add
	 * 基金级暂停交易删除
	 * 
	 */
	public void deleteFundStop(FundStopVo Vo);
	/**
	 * 20110420 wangdw add
	 * 基金级暂停交易修改
	 * 
	 */
	public void updateFundStop(FundStopVo Vo);
	/**
	 * 获取所有基金(包括限制基金)
	 * @return List
	 * @throws Exception
	 */
	public List<FundManagerVo> getAllSubFundsArray(@Param("fundst")String fundst,@Param("tano")String tano) throws Exception;
	
    /**
     * 同步成立日期
     * @param fundid  基金代码
     * @return void
     */
    public void syschonizeSetUpDate(SearchParam sp) throws Exception;
    
	/**
	 * 获取所有待复核的基金信息
	 * @return List
	 * @throws Exception
	 */
	public List<FundManagerVo> getAllChkFundInfoListPage(SearchParam sp) throws Exception;
	
	/**
	 * added by zengxy 20131121
	 * 获取所有待复核的基金信息
	 * @return List
	 * @throws Exception
	 */
	public List<FundManagerVo> getAllChkFundInfo(@Param("fundid")String fundid,@Param("fundname")String fundname) throws Exception;
	
    /**
     * 增加未复核的基金信息
     * @param Vo 基金信息Vo
     * @return void
     */
    public void addChkFundInfo(FundManagerVo Vo)throws Exception;

    /**
     * 删除一个未复核的基金
     * @param fundId 基金代码
     * @return void
     */
    public void delChkFundInfo(@Param("fundId")String fundId, @Param("processor")String processor)throws Exception;

    /**
     * 修改未复核的基金信息
     * @param Vo 基金信息Vo
     * @return void
     */
    public void modifyChkFundInfo(FundManagerVo Vo)throws Exception;

    /**
     * 获取指定基金代码的未复核的基金信息
     * @param fundId 基金代码
     * @return FundInfoVo 基金信息Vo
     */
    public FundManagerVo getChkFundInfo(@Param("fundId")String fundId)throws Exception;
    
    /**
     * 基金信息复核
     * @param fundId 基金代码
     * @param checker 复核人
     * @param checkstatus 复核状态   Y:复核通过   N:复核未通过    I:未复核
     * 
     * @return String[]   [0]:返回码    [1]:返回信息
     */
    public void checkFundInfo(SearchParam sp)throws Exception;
    
    
    /**
     * 短期理财产品余额查询
     * @param custno
     * @param tradeAcco
     * @param quryType
     * @return
     */
    public void getFinanceBalance(SearchParam sp);
}
