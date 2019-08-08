package com.cmwa.ecc.business.service.instfeemana;

import java.util.List;

import net.sf.json.JSONObject;

import com.cmwa.ecc.business.entity.ratediscmgr.InstDiscounVo;
import com.cmwa.ecc.business.utils.MagicMap;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 财富柜台 费率设置 后端接口
 * @author ex-liuy
 *
 */
public interface InstFeeSetService {

	/**
	 * 查询柜台 费率设置列表
	 * @param sp
	 * @return
	 */
	public Page<InstDiscounVo> queryInstDiscountListPage(SearchParam sp);
	
	/**
	 * 查询柜台 费率设置列表 
	 * -无分页
	 * @param sp
	 * @return
	 */
	public List<InstDiscounVo> queryInstDiscountAllList(SearchParam sp);
	
	/**
	 * 新增 柜台费率设置
	 * @param 
	 */
	public JSONObject addInstFeeSetInfo(InstDiscounVo instDiscounVo);
	
	/**
	 * 复核 柜台费率设置
	 * @param 
	 */
	public JSONObject checkInstFeeSetInfo(InstDiscounVo instDiscounVo);
	
	/**
	 * 修改 柜台费率设置
	 * @param 
	 */
	public JSONObject updateInstFeeSetInfo(InstDiscounVo instDiscounVo);
	
	/**
	 * 删除 柜台费率设置
	 * @param 
	 */
	public JSONObject delInstFeeSetInfo(InstDiscounVo instDiscounVo);
}
