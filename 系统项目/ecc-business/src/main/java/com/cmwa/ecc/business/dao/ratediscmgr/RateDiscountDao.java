package com.cmwa.ecc.business.dao.ratediscmgr;

import java.util.List;
import com.cmwa.ecc.business.entity.ratediscmgr.BankDiscountVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;
/**
 * 直销柜台-业务管理-费率折扣管理
 * 数据操作接口
 * @author ex-liuy
 *
 */
@MybatisDao
public interface RateDiscountDao {
	/**
	 * 查询费率管理
	 * @param sp
	 * @return
	 */
	public List<BankDiscountVo> rateDiscountListPage(SearchParam sp);
	
	/**
	 * 删除费率设置
	 * @param bankDiscountVo
	 */
	public void delRateDiscountByParam(BankDiscountVo bankDiscountVo);
	
	/**
	 * 修改费率设置
	 * @param bankDiscountVo
	 */
	public void updateRateDiscountByParam(BankDiscountVo bankDiscountVo);
	
	/**
	 * 新增费率设置
	 * @param bankDiscountVo
	 */
	public void addRateDiscount(BankDiscountVo bankDiscountVo);
	
	/**
	 * 查询费率管理 无分页
	 * @param sp
	 * @return
	 */
	public List<BankDiscountVo> rateDiscountAllList(SearchParam sp);
}
