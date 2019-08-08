package com.cmwa.ecc.business.service.directmanager;

import net.sf.json.JSONObject;

import com.cmwa.ecc.business.entity.accountManger.ValidateDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 过期证件管理 接口
 * @author ex-liuy
 *
 */
public interface ValidateService {
	/**
	 * 查询过期证件客户信息
	 * @param sp
	 * @return
	 */
	public Page<ValidateDto> queryValidateList(SearchParam sp);
	/**
	 * 证件有效期修改
	 * @param dto
	 * @return
	 */
	public JSONObject updateValidate(ValidateDto dto);
	
	/**
	 * 证件有效期修改-
	 * 查询客户信息返回页面
	 * @param dto
	 * @return
	 */
	public ValidateDto queryValidateDtoToUpdateView(SearchParam sp);
}
