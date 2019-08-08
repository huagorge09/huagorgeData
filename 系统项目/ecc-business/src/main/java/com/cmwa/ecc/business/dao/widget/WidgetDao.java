package com.cmwa.ecc.business.dao.widget;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.cmwa.ecc.business.commonVo.ParameterDto;
import com.cmwa.ecc.business.mybatis.annotation.MybatisDao;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 页面插件
 * 公共插件数据交互
 * @author ex-liuy
 *
 */
@MybatisDao
public interface WidgetDao {
	/**
	 * 查询 业务类型
	 * @param sp
	 * @return
	 */
	public List<Map<String, Object>> queryMatchApkindList(SearchParam sp);
	
	/**
     * 根据参数类型、参数键和参数码取得系统参数信息
     * @param pmst 参数类型
     * @param pmky 参数键
     * @param pmco 参数码
     * @return ParameterDto 参数信息
     * @throws Exception
     */
    public ParameterDto getParameterByPmstPmkyPmco(@Param("pmst")String pmst, @Param("pmky")String pmky, @Param("pmco")String pmco);
	
	/**
	 * 查询 数据状态
	 * @param sp
	 * @return
	 */
	public List<Map<String, Object>> queryMatchDataSTList(SearchParam sp);
}
