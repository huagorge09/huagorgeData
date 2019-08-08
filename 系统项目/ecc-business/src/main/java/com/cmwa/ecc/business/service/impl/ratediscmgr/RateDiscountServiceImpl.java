package com.cmwa.ecc.business.service.impl.ratediscmgr;

import java.util.List;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmwa.ecc.business.dao.ratediscmgr.RateDiscountDao;
import com.cmwa.ecc.business.entity.ratediscmgr.BankDiscountVo;
import com.cmwa.ecc.business.service.ratediscmgr.RateDiscountService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.ResultConstant;
import com.cmwa.ecc.business.utils.SearchParam;
/**
 * 直销柜台-业务管理-费率折扣管理
 * 业务接口
 * @author ex-liuy
 *
 */
@Service
public class RateDiscountServiceImpl implements RateDiscountService {
	private static Logger logger = LoggerFactory.getLogger(RateDiscountServiceImpl.class);
	@Autowired
	private RateDiscountDao rateDiscountDao;
	
	@Override
	public Page<BankDiscountVo> rateDiscountListPage(SearchParam sp) {
		List<BankDiscountVo> retaDiscList = rateDiscountDao.rateDiscountListPage(sp);
		return Page.create(retaDiscList, sp.getStart(), sp.getLimit(), sp.getTotal());
	}
	
	@Override
	public List<BankDiscountVo> rateDiscountAllList(SearchParam sp) {
		List<BankDiscountVo> retaDiscList = rateDiscountDao.rateDiscountAllList(sp);
		return retaDiscList;
	}
	@Override
	public JSONObject delRateDiscountByParam(BankDiscountVo bankDiscountVo) {
		JSONObject result = new JSONObject();
		try{
			rateDiscountDao.delRateDiscountByParam(bankDiscountVo);
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_SUCCESS);
			result.put(ResultConstant.C_RESULT_MSG, ResultConstant.resultMap.get(ResultConstant.C_RESULT_SUCCESS));
		}catch(Exception e){
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG, "操作异常："+e.getMessage());
			logger.error("----RateDiscountServiceImpl-delRateDiscountByParam-Exception:"+bankDiscountVo.toString(),e);
		}
		return result;
	}

	@Override
	public JSONObject addRateDiscountByParam(BankDiscountVo bankDiscountVo) {
		JSONObject result = new JSONObject();
		try{
			bankDiscountVo.setStrDate(bankDiscountVo.getStrDate().replaceAll("-", ""));
			bankDiscountVo.setEndDate(bankDiscountVo.getEndDate().replaceAll("-", ""));
			rateDiscountDao.addRateDiscount(bankDiscountVo);
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_SUCCESS);
			result.put(ResultConstant.C_RESULT_MSG, ResultConstant.resultMap.get(ResultConstant.C_RESULT_SUCCESS));
		}catch(Exception e){
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG, "操作异常："+e.getMessage());
			logger.error("----RateDiscountServiceImpl-addRateDiscountByParam-Exception:"+bankDiscountVo.toString(),e);
		}
		return result;
	}

	@Override
	public JSONObject updateRateDiscountByParam(BankDiscountVo bankDiscountVo) {
		JSONObject result = new JSONObject();
		try{
			bankDiscountVo.setStrDate(bankDiscountVo.getStrDate().replaceAll("-", ""));
			bankDiscountVo.setEndDate(bankDiscountVo.getEndDate().replaceAll("-", ""));
			rateDiscountDao.updateRateDiscountByParam(bankDiscountVo);
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_SUCCESS);
			result.put(ResultConstant.C_RESULT_MSG, ResultConstant.resultMap.get(ResultConstant.C_RESULT_SUCCESS));
		}catch(Exception e){
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG, "操作异常："+e.getMessage());
			logger.error("----RateDiscountServiceImpl-updateRateDiscountByParam-Exception:"+bankDiscountVo.toString(),e);
		}
		return result;
	}

	@Override
	public JSONObject copyRateDiscountByParam(SearchParam sp) {
		JSONObject result = new JSONObject();
		try{
			//获取 源 目的 产品ID
			String souProductId = (String)sp.getSp().get("souProductId");
			String tarProductId = (String)sp.getSp().get("tarProductId");
			String createId = (String)sp.getSp().get("createId");
			
			//参数校验 不能为空 且 不能一致
			if(StringUtils.isEmpty(souProductId) || StringUtils.isEmpty(tarProductId) || souProductId.equalsIgnoreCase(tarProductId)){
				result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
				result.put(ResultConstant.C_RESULT_MSG, "参数错误：源商户产品ID(基金代码)或者目的商户产品ID(基金代码)不能为空且不能一致");
				return result;
			}
			
			SearchParam querySp = new SearchParam();
			querySp.getSp().put("productId", souProductId);
			//查询原产品所有的费率设置 并遍历
			List<BankDiscountVo> souRateDiscList = rateDiscountDao.rateDiscountAllList(querySp);
			StringBuffer stringBuffer = new StringBuffer();
			for (BankDiscountVo souBankDiscountVo : souRateDiscList) {
				querySp.getSp().clear();
				querySp.getSp().put("bankName", souBankDiscountVo.getBnkNo());
				querySp.getSp().put("productId", tarProductId);
				querySp.getSp().put("apkind", souBankDiscountVo.getApkind());
				//根据源产品ID查询 目的产品ID
				List<BankDiscountVo> tarRateDiscList = rateDiscountDao.rateDiscountAllList(querySp);
				if(tarRateDiscList.size() >= 1){
					//如有相同设置则提示页面
					BankDiscountVo tarRateDisc = tarRateDiscList.get(0);
					stringBuffer.append("</br>"+tarRateDisc.getBnkName()+"中"+tarRateDisc.getProductName()+"的"+tarRateDisc.getApkindName()+"费率已经存在，不允许重复添加。请核对后再执行此操作。");
				}else{
					//其他的数据 进行 保存
					BankDiscountVo saveTarRateDiscVo = souBankDiscountVo;
					saveTarRateDiscVo.setProductId(tarProductId);
					saveTarRateDiscVo.setCreateId(createId);
					rateDiscountDao.addRateDiscount(saveTarRateDiscVo);
				}
			}
			if(stringBuffer.length() > 0){
				result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
				result.put(ResultConstant.C_RESULT_MSG, stringBuffer.toString());
			}else{
				result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_SUCCESS);
				result.put(ResultConstant.C_RESULT_MSG, ResultConstant.resultMap.get(ResultConstant.C_RESULT_SUCCESS));
			}
		}catch(Exception e){
			result.put(ResultConstant.C_RESULT_CODE, ResultConstant.C_RESULT_FAILE);
			result.put(ResultConstant.C_RESULT_MSG, "操作异常："+e.getMessage());
			logger.error("----RateDiscountServiceImpl-copyRateDiscountByParam-Exception:"+sp.toString(),e);
		}
		return result;
	}

	
}
