package com.cmwa.ecc.business.service.fundinfo;

import java.util.List;
import com.cmwa.ecc.business.entity.fundinfo.ECCProductProDto;
import com.cmwa.ecc.business.entity.fundinfo.ProductInfoDto;
import com.cmwa.ecc.business.entity.fundinfo.QuestionDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import net.sf.json.JSONObject;

public interface ECCProductManager {
	/**
	 *  获取产品系列列表
	 *  @return
	 */
	public Page<ECCProductProDto> getProductProList(SearchParam sp);

	
	/**
	 * 删除产品系列
	 * @param id
	 * @return
	 */
	public JSONObject deleteProductProById(String id);


	/**
	 * 修改产品系列
	 * @param id
	 * @param dName
	 * @return
	 */
	public JSONObject modProductPro(String id, String dName);

	/**
	 * 增加产品系列
	 * @param dName
	 * @return
	 */
	public JSONObject addProductPro(String dName);

	/**
	 * 获取新增产品列表
	 * @param sp
	 * @return
	 */
	public Page<ProductInfoDto> getProductInfoList(SearchParam sp);

	/**
	 * 查看新增产品信息
	 * @param funcode
	 * @return
	 */
	public List<ProductInfoDto> viewProductReviewInfo(String funcode,String period);

	
	/**
	 * 获取产品附加信息
	 * @param period 
	 * @param sp
	 * @return
	 */
	public List<QuestionDto> getProductQuestion(String funcode, String period);

	
	/**
	 * 删除新增产品
	 * @param funcode
	 * @return
	 */
	public JSONObject delProductReviewByFuncode(String funcode,String period);


	public JSONObject checkProductReview(String funcode, String opertorId,
			String checkType, String returnCode,String period);
}
