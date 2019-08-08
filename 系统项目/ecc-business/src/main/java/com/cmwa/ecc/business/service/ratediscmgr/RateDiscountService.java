package com.cmwa.ecc.business.service.ratediscmgr;

import java.util.List;
import net.sf.json.JSONObject;
import com.cmwa.ecc.business.entity.ratediscmgr.BankDiscountVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
/**
 * 直销柜台-业务管理-费率折扣管理
 * 业务接口
 * @author ex-liuy
 *
 */
public interface RateDiscountService {
	/**
	 * 查询费率管理
	 * @param sp
	 * @return
	 */
	public Page<BankDiscountVo> rateDiscountListPage(SearchParam sp);
	
	/**
	 * 删除费率设置
	 * @param bankDiscountVo
	 */
	public JSONObject delRateDiscountByParam(BankDiscountVo bankDiscountVo);
	
	/**
	 * 新增费率设置
	 * @param bankDiscountVo
	 */
	public JSONObject addRateDiscountByParam(BankDiscountVo bankDiscountVo);
	
	/**
	 * 修改费率设置
	 * @param bankDiscountVo
	 */
	public JSONObject updateRateDiscountByParam(BankDiscountVo bankDiscountVo);
	
	/**
	 * 查询费率管理 无分页
	 * @param sp
	 * @return
	 */
	public List<BankDiscountVo> rateDiscountAllList(SearchParam sp);
	
	/**
	 * 复制费率设置
	 * @param searchParam
	 */
	public JSONObject copyRateDiscountByParam(SearchParam sp);
}
