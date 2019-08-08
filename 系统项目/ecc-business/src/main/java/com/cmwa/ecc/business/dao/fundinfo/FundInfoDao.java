package com.cmwa.ecc.business.dao.fundinfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import com.cmwa.ecc.business.entity.fundinfo.FundInfoVo;
import com.cmwa.ecc.business.entity.fundinfo.FundStopVo;
import com.cmwa.ecc.business.entity.fundinfo.ProductInfoDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 产品管理
 * 数据操作接口
 * @author ex-liuy
 *
 */
@MybatisDao
public interface FundInfoDao {
	/**
	 * 页面插件-查询所有产品
	 * @param sp
	 * @return
	 */
	public List<FundInfoVo> queryMatchFundInfoList(SearchParam sp);
	
	/**
	 * 增加基金信息
	 * @param dto 基金信息DTO
	 * @return void
	 * @throws Exception
	 */
	void addFundInfo(FundInfoVo dto) throws Exception;
	
	/**
	 * 删除一个基金
	 * @param fundId 基金代码
	 * @return void
	 * @throws Exception
	 */
	void delFundInfo(@Param("fundId")String fundId) throws Exception;
	
	/**
	 * 修改基金信息
	 * @param dto 基金信息DTO
	 * @return void
	 * @throws Exception
	 */
	void modifyFundInfo(FundInfoVo dto) throws Exception;
	
	/**
	 * 获取所有的基金信息
	 * @return List
	 * @throws Exception
	 */
	List<FundInfoVo> getAllFundInfo() throws Exception;
	
	/**
	 * 获取专户产品的基金信息
	 * @return
	 * @throws Exception
	 */
	List<FundInfoVo> getSpecProFundInfo() throws Exception;
	
	/**
	 * added by zengxy 20131121
	 * 获取所有的基金信息
	 * @return List
	 * @throws Exception
	 */
	List<FundInfoVo> getAllFundInfoListPage(SearchParam sp) throws Exception;
	
	/**
	 * 获取指定基金代码的基金信息
	 * @param fundId 基金代码
	 * @return FundInfoVo 基金信息DTO
	 * @throws Exception
	 */
	FundInfoVo getFundInfo(@Param("fundId")String fundId) throws Exception;


	/**
	 * 获取所有基金（缓存用）
	 * @return List
	 * @throws Exception
	 */
	List<FundInfoVo> getCachedFunds() throws Exception;
	
	/**
	 * 20110419 wangdw add
	 * 新增基金级暂停交易数据
	 * 
	 */
	public void addFundStop(FundStopVo dto);
	/**
	 * 20110419 wangdw add
	 * 基金级暂停交易复核
	 * 
	 */
	public void checkFundStop(FundStopVo dto);
	/**
	 * 20110419 wangdw add
	 * 基金级暂停交易删除
	 * 
	 */
	public void deleteFundStop(FundStopVo dto);
	/**
	 * 20110420 wangdw add
	 * 基金级暂停交易修改
	 * 
	 */
	public void updateFundStop(FundStopVo dto);
	/**
	 * 获取所有基金(包括限制基金)
	 * @return List
	 * @throws Exception
	 */
	public List<FundInfoVo> getAllSubFundsArray(@Param("fundst")String fundst,@Param("tano")String tano) throws Exception;
	
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
	public List<FundInfoVo> getAllChkFundInfoListPage(SearchParam sp) throws Exception;
	
	/**
	 * added by zengxy 20131121
	 * 获取所有待复核的基金信息
	 * @return List
	 * @throws Exception
	 */
	public List<FundInfoVo> getAllChkFundInfo(@Param("fundid")String fundid,@Param("fundname")String fundname) throws Exception;
	
    /**
     * 增加未复核的基金信息
     * @param dto 基金信息DTO
     * @return void
     */
    public void addChkFundInfo(FundInfoVo dto)throws Exception;

    /**
     * 删除一个未复核的基金
     * @param fundId 基金代码
     * @return void
     */
    public void delChkFundInfo(@Param("fundId")String fundId, @Param("processor")String processor)throws Exception;

    /**
     * 修改未复核的基金信息
     * @param dto 基金信息DTO
     * @return void
     */
    public void modifyChkFundInfo(FundInfoVo dto)throws Exception;

    /**
     * 获取指定基金代码的未复核的基金信息
     * @param fundId 基金代码
     * @return FundInfoVo 基金信息DTO
     */
    public FundInfoVo getChkFundInfo(@Param("fundId")String fundId)throws Exception;
    
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
    
    /**
	 * 客服下单产品详情列表
	 * 查看产品详情列表(CMWA_FUND)
	 * @param sp
	 * @return
	 */
	public List<ProductInfoDto> getOrderFundList(SearchParam sp);
	
	public int getOrderFundListTotal(SearchParam sp);
	
	/**
     * 短期理财产品余额查询
     * @param custno
     * @param tradeAcco
     * @param quryType
     * @return
     */
    public void getFinanceBalcons(SearchParam sp);

    /**
     * 短期理财产品指定一天可赎回份额查询
     * @param allowedRedeemDt
     * @return
     */
    
    public List<Map>  queryAllowedBalcons(Map sp);
    
    /**
     * 短期理财产品某天全部可赎回份额查询
     * @param allowedRedeemDt
     * @return
     */
    public List<Map>  queryAllBalconsByDate(Map sp);  
    
    /**
     * 查询直销柜台和ta份额的差异数据
     * @return
     */
	public List<Map> queryBalanceDifference();
    

}
