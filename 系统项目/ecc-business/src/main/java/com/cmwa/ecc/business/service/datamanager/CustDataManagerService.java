package com.cmwa.ecc.business.service.datamanager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 资料管理 后端接口
 * @author ex-liuy
 *
 */
public interface CustDataManagerService {
	/**
	 * 查询客户资料列表
	 * @param sp
	 * @return
	 */
	public Page<OpenAccountDto> queryCustDataInfoListPage(SearchParam sp);
	
	/**
	 * 查询-导出客户资料
	 * @param sp
	 * @return
	 */
	public JSONObject queryExportCustDataInfo(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 根据APPSERIALNO查询客户资料信息
	 * @param sp
	 * @return
	 */
	public OpenAccountDto queryCustDataInfoByCondtion(SearchParam sp);
	
	/**
	 * 更新客户资料
	 * @param sp
	 */
	public JSONObject updateCustDataManagerInfo(OpenAccountDto openAccountDto);
}
