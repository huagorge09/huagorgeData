package com.cmwa.ecc.business.service.customerDataManager;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ecc.business.entity.accountManger.RiskLevelDto;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

public interface CustomerDataManagerService {
	
	public Page<RiskLevelDto> getRiskLevelInfoListPage(SearchParam sp) throws Exception;
	
	public ModelAndView openDialog(String method,String custNo);
	
}
