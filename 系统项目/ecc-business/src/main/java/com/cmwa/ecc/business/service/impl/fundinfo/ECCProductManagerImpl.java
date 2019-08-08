package com.cmwa.ecc.business.service.impl.fundinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.common.AttachDao;
import com.cmwa.ecc.business.dao.common.CommonDao;
import com.cmwa.ecc.business.dao.fundinfo.ECCProductDao;
import com.cmwa.ecc.business.entity.fundinfo.ECCProductProDto;
import com.cmwa.ecc.business.entity.fundinfo.ProductInfoDto;
import com.cmwa.ecc.business.entity.fundinfo.QuestionDto;
import com.cmwa.ecc.business.service.fundinfo.ECCProductManager;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

@Service
public class ECCProductManagerImpl implements ECCProductManager {
	

	private static Logger logger = Logger.getLogger(ECCProductManagerImpl.class
			.getName());
	@Autowired
	private ECCProductDao productDao;

	@Autowired
	public CommonDao commonDao;
	
	//产品净值数据移植
	@Autowired
	private AttachDao attachDao;
	
	
	@Override
	public Page<ECCProductProDto> getProductProList(SearchParam sp) {
		List<ECCProductProDto> productProList = productDao
				.getProductProList(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setAutoTotal(false);
		sp.setTotal(productDao.getProductProCount());
		return Page.create(productProList, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	@Override
	public JSONObject deleteProductProById(String id) {
		JSONObject result = new JSONObject();
		try {
			SearchParam sp = new SearchParam();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			sp.setSp(map);
			productDao.deleteProdcutPro(sp);
			result.put("ResultCode", "0000");
			result.put("ResultDesc", "删除成功");
		} catch (Exception e) {
			logger.error(e);
			result.put("ResultCode", "9999");
			result.put("ResultDesc", "未知错误");
		}
		return result;
	}

	@Override
	public JSONObject modProductPro(String id, String dName) {
		JSONObject result = new JSONObject();
		try {
			SearchParam sp = new SearchParam();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", id);
			map.put("dName", dName);
			sp.setSp(map);
			productDao.modProductName(sp);
			result.put("ResultCode", "0000");
			result.put("ResultDesc", "修改成功");
		} catch (Exception e) {
			logger.error(e);
			result.put("ResultCode", "9999");
			result.put("ResultDesc", "未知错误");
		}
		return result;
	}

	@Override
	public JSONObject addProductPro(String dName) {
		JSONObject result = new JSONObject();
		try {
			SearchParam sp = new SearchParam();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("dName", dName);
			sp.setSp(map);
			productDao.addProductPro(sp);
			result.put("ResultCode", "0000");
			result.put("ResultDesc", "新增成功");
		} catch (Exception e) {
			logger.error(e);
			result.put("ResultCode", "9999");
			result.put("ResultDesc", "未知错误");
		}
		return result;
	}

	@Override
	public Page<ProductInfoDto> getProductInfoList(SearchParam sp) {
		List<ProductInfoDto> productList = productDao.getProductList(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setAutoTotal(false);
		sp.setTotal(productDao.getProductInfoCount(sp));
		return Page.create(productList, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	@Override
	public List<ProductInfoDto> viewProductReviewInfo(String funcode,String period) {
		SearchParam sp = new SearchParam();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("fundid", funcode);
		map.put("period", period);
		sp.setSp(map);
		return productDao.getProductList(sp);
	}

	@Override
	public List<QuestionDto> getProductQuestion(String funcode,String period) {
		SearchParam sp = new SearchParam();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("funcode", funcode);
		map.put("period", period);
		sp.setSp(map);
		return productDao.getProductQuestionList(sp);
	}

	@Override
	public JSONObject delProductReviewByFuncode(String funcode,String peirod) {
		JSONObject result = new JSONObject();
		try{
			
		//删除产品问题
//	 	delegate.delQuestion(ids[i]);
//	 	//删除产品要素
//	 	delegate.delFundelement(ids[i]);
//		boolean flag = delegate.delProductReview(ids[i]);
		productDao.delFunQuestion(funcode,peirod);
		productDao.delFundelement(funcode,peirod);
		int delProductReview = productDao.delProductReview(funcode,peirod);
		if (delProductReview!=1) {
			result.put("ResultCode", "9999");
			result.put("ResultDesc", "失败");
		}else{
			result.put("ResultCode", "0000");
			result.put("ResultDesc", "成功");
		}
		}catch(Exception e){
			logger.error(e);
			result.put("ResultCode", "9999");
			result.put("ResultDesc", e.toString());
		}
		return result;
	}

	@Override
	public JSONObject checkProductReview(String funcode, String operatorId,
			String checkType, String returnCode,String period) {
		
		JSONObject result=new JSONObject();
	try{
		
		
		if("Y".equals(checkType)){
			SearchParam sp = new SearchParam();
			Map<String, Object> map=new HashMap<String,Object>();
			map.put("fundid", funcode);
			map.put("period", period);
			sp.setSp(map);
			int productInfoCount = productDao.getCmwaFundCount(sp);
			if (0 < productInfoCount) {
				result.put("ResultCode", "9201");
				result.put("ResultDesc", "产品已存在");
				return result;
			}
			if("N".equals(returnCode)){ 
				int queryTAConfrim = productDao.queryTAConfrim(sp);
				if (0==queryTAConfrim) {
					//修改逻辑，未确认TA额度也可以提交
					result.put("ResultCode", "9202");
					result.put("ResultDesc", "未确认TA额度,是否确认额度");
					return result;
				}
			}
		}
		
		Map<String,Object> map=new HashMap<String,Object>(); 
		map.put("fundid", funcode);
		map.put("operatorId", operatorId);
		map.put("status", checkType);
		map.put("period", period);
		productDao.checkProductReview(map);
		if("Y".equals(checkType)){
			productDao.addCmwaFund(map);
		}
		result.put("ResultCode","0000");
		result.put("ResultDesc", "成功");
		} catch (Exception e) {
			logger.error(e);
			result.put("ResultCode","9999");
			result.put("ResultDesc", "未知错误,请联系管理员");
		}
		return result;
	}

}
