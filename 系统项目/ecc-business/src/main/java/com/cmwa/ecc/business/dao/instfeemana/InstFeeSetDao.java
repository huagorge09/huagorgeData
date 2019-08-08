package com.cmwa.ecc.business.dao.instfeemana;

import java.util.List;

import com.cmwa.ecc.business.entity.ratediscmgr.InstDiscounVo;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.MagicMap;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 财富柜台费率设置 数据操作接口
 * @author ex-liuy
 *
 */
@MybatisDao
public interface InstFeeSetDao {
	/**
	 * 查询柜台 费率设置列表
	 * @param sp
	 * @return
	 */
	public List<InstDiscounVo> queryInstDiscountListPage(SearchParam sp);
	
	/**
	 * 查询柜台 费率设置列表 
	 * -无分页
	 * @param sp
	 * @return
	 */
	public List<InstDiscounVo> queryInstDiscountAllList(SearchParam sp);
	
	/**
	 * 新增 柜台费率设置
	 * @param vo
	 */
	public void operateInstFeeSetInfo(MagicMap paramMap);
}
