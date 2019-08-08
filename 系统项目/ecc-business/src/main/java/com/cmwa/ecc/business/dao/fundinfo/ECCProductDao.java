package com.cmwa.ecc.business.dao.fundinfo;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.entity.fundinfo.ECCProductProDto;
import com.cmwa.ecc.business.entity.fundinfo.FundElementDto;
import com.cmwa.ecc.business.entity.fundinfo.FundEstimateDto;
import com.cmwa.ecc.business.entity.fundinfo.ProductInfoDto;
import com.cmwa.ecc.business.entity.fundinfo.QuestionDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

@MybatisDao
public interface ECCProductDao {
	
	
	/**
	 * 获取产品系列列表(CMWA_PRODUCT_DEPARTMENT)
	 */
	public List<ECCProductProDto> getProductProList(SearchParam sp);

	public int getProductProCount();

	
	/**
	 * 删除产品系列(CMWA_PRODUCT_DEPARTMENT)
	 */
	public void deleteProdcutPro(SearchParam sp);
	
	
	/**
	 * 修改产品系列(CMWA_PRODUCT_DEPARTMENT)
	 */
	public void modProductName(SearchParam sp);

	/**
	 * 新增产品系列(CMWA_PRODUCT_DEPARTMENT)
	 */
	public void addProductPro(SearchParam sp);
	
	
	
	
	/**
	 * 查询新增产品列表(CMWA_FUND_REVIEW)
	 */
	public List<ProductInfoDto> getProductList(SearchParam sp);
	
	/**
	 * 根据条件查询产品数量
	 * @param sp
	 * @return
	 */
	public int getProductInfoCount(SearchParam sp);
	
	/**
	 * 确认TA额度
	 * @param sp
	 * @return
	 */
	public int queryTAConfrim(SearchParam sp);
	
	
	/**
	 * 获取附加信息列表
	 * @param sp
	 * @return
	 */
	public List<QuestionDto> getProductQuestionList(SearchParam sp);
	
	
	/**
	 * 删除产品问题
	 * @param funcode
	 */
	public void delFunQuestion(@Param("funcode")String funcode,@Param("period")String period);
	
	/**
	 * 删除产品要素
	 * @param funcode
	 * @param string 
	 */
	public void delFundelement(@Param("funcode")String funcode,@Param("period")String period);
	
	/**
	 * 删除新增产品(CMWA_FUND_REVIEW)
	 * @param funcode
	 * @return
	 */
	public int delProductReview(@Param("funcode")String funcode,@Param("period")String period);
	
	
	/**
	 * 复核新增产品
	 * @param map
	 */
	public void checkProductReview(Map<String,Object> map);
	
	/**
	 * 复核通过
	 * @param map
	 */
	public void addCmwaFund(Map<String,Object> map);
	
	/**
	 * 查询产品列表数量
	 * @param sp
	 * @return
	 */
	public int getCmwaFundCount(SearchParam sp);
	
	/**
	 * 插入产品要素
	 * @param list
	 */
	public void insertFundElement(List<FundElementDto> list);


	/**
	 * 插入问题信息
	 * @param question
	 */
	public void insertFundQuestion(QuestionDto question);

	/**
	 * 增加产品(CMWA_FUND_REVIEW)
	 * @param product
	 */
	public void addFundReview(ProductInfoDto product);
	
	/**
	 * 查看产品详情列表(CMWA_FUND)
	 * @param sp
	 * @return
	 */
	public List<ProductInfoDto> getFundList(SearchParam sp);
	
	
	public int getFundListTotal(SearchParam sp);
	
	/**
	 * 客服下单产品详情列表
	 * 查看产品详情列表(CMWA_FUND)
	 * @param sp
	 * @return
	 */
	public List<ProductInfoDto> getOrderFundList(SearchParam sp);
	
	public int getOrderFundListTotal(SearchParam sp);

	/**
	 * 修改新增产品
	 * @param product
	 */
	public void updateProductReview(ProductInfoDto product);

	/**
	 * 修改产品详情
	 * @param product
	 */
	public void updateFundDetail(ProductInfoDto product);

	/**
	 * 获取产品估值列表
	 * @param sp
	 * @return
	 */
	public List<FundEstimateDto> queryFundEstimateList(SearchParam sp);

	public int getFundEstimateCount(SearchParam sp);

	/**
	 * 修改产品估值列表
	 * @param product
	 */
	public void updateFundEstimate(FundEstimateDto product);

	/**
	 * 删除产品估值列表
	 * @param funcode
	 * @param theDate
	 */
	public void deleteFundEstimate(@Param("funcode")String funcode,@Param("estimateDate")String theDate);

	/**
	 * 获取匹配产品估值数量
	 * @param funcode
	 * @return
	 */
	public List<FundEstimateDto> getMatchFundEstimateList(@Param("funcode")String funcode);

	/**
	 * 
	 * @param funcode
	 * @return
	 */
	public List<ProductInfoDto> getFundInfoList(@Param("funcode")String funcode);

	public void addFundEstimate(FundEstimateDto product);

	public List<ProductInfoDto> getFunType(@Param("funcode")String funcode);
	
	/**
	 * 获取电子合同对应产品ID
	 * @param fundid
	 * @return
	 */
	public List<ProductInfoDto> getMatchEleContractFundInfo(@Param("funcode")String fundid);

	
	/**
	 * 获取产品基本信息
	 * @param funcode
	 */
	public List<FundElementDto> queryFundElement(@Param("fundid")String funcode,@Param("type")String type,@Param("period")int period);
	
	public List<FundElementDto> test(@Param("fundid")String fundid,@Param("period")int period);

	
	public FundElementDto getFundElementPng(@Param("fundid")String funcode,
			@Param("sortno")int sortno, @Param("eletype")int eletype);

	
	/**
	 * 获取财富宝产品
	 * @return
	 */
	public List<ProductInfoDto> getFundWithSell();
	
	/**
	 * 查询柜台 产品列表
	 * @param sp
	 * @return
	 */
	public List<ProductInfoDto> getDsFundInfoList(SearchParam sp);
}
