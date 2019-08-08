package com.cmwa.ecc.business.service.param;

import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONObject;
import org.springframework.web.servlet.ModelAndView;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface ParameterManagerService {
	/**
	 * 获取主参类型
	 * @param sp
	 * @return
	 */
	Page<ParameterVo> queryAllParameter(SearchParam sp);
	
	/**
	 * 业务管理-参数管理
	 * 限制查询
	 * @param sp
	 * @return
	 */
	Page<ParameterVo> queryBusinParameterListPage(SearchParam sp);
	
	/**
	 * 获取参数子集
	 * @param sp
	 * @return
	 */
	Page<ParameterVo> querySunParameterListPage(SearchParam sp);

	ModelAndView openDialog(HttpServletRequest request,String pmnm,String ppmnm);
	
	
	/**
	 * 新增参数
	 * @param parameterVo
	 * @return
	 */
	JSONObject addParameter(ParameterVo parameterVo);

	/**
	 * 修改参数
	 * @param parameterVo
	 * @return
	 */
	JSONObject modParameter(ParameterVo parameterVo);
	
	/**
	 * 删除参数
	 * @param parameterVo
	 * @return
	 */
	JSONObject delParameter(ParameterVo parameterVo);
	

}
