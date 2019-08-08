package com.cmwa.ecc.business.service.widget;

import java.util.List;
import java.util.Map;

import com.cmwa.ecc.business.commonVo.ParameterDto;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 页面插件
 * 公共插件-交互接口
 * @author ex-liuy
 *
 */
public interface WidgetService {
	/**
	 * 查询 业务类型
	 * @param sp
	 * @return
	 */
	public List<Map<String, Object>> queryMatchApkindList(SearchParam sp);
	
	/**
	 * 查询 业务类型
	 * @param sp
	 * @return
	 */
	public List<Map<String, Object>> queryParamListByStAndKy(String pmst , String pmky);
	
	/**
     * 根据参数类型、参数键和参数码取得系统参数信息
     * @param pmst 参数类型
     * @param pmky 参数键
     * @param pmco 参数码
     * @return ParameterDto 参数信息
     * @throws Exception
     */
    public ParameterDto getParameterByPmstPmkyPmco(String pmst, String pmky, String pmco);
}
