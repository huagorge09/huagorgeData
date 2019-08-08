package com.cmwa.ecc.business.service.impl.customerDataManager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.cmwa.ecc.business.dao.customerDataMgr.CustomerDataManagerDao;
import com.cmwa.ecc.business.entity.accountManger.RiskLevelDto;
import com.cmwa.ecc.business.service.customerDataManager.CustomerDataManagerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
@Service
public class CustomerDataManagerServiceImpl implements CustomerDataManagerService {

	@Autowired
	private CustomerDataManagerDao customerDataManagerDao;
	
	
	@Override
	public Page<RiskLevelDto> getRiskLevelInfoListPage(SearchParam sp) throws Exception {
		sp = changeDatePatternForCustData(sp);
		List<RiskLevelDto> getRiskLevelInfoListPage = customerDataManagerDao.getRiskLevelInfoListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(customerDataManagerDao.getRiskLevelInfoListTotal(sp));
		return Page.create(getRiskLevelInfoListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}
	
	/* 起止日期更换格式 */
	public SearchParam  changeDatePatternForCustData(SearchParam sp) {
		//起止日期更换格式
		String startDate = null;
		String endDate = null;
		String executedStartDate = "";
		String executedEndDate = "";
		String spStartDate = "";
		String spEndDate = "";
		int syear = 0;
		int iyear = 0;
		if(sp.getSp().get("startDate") != null) {
			spStartDate = sp.getSp().get("startDate").toString();
		}
		if(sp.getSp().get("endDate") != null) {
			spEndDate = sp.getSp().get("endDate").toString();
		}
		if(sp.getSp().get("startDate") != null && !"".equals(spStartDate)) {
			startDate =  sp.getSp().get("startDate").toString().replace("-", "");
			executedStartDate = startDate.substring(0,4);
			syear =Integer.parseInt(executedStartDate) - 1;
			String beginDate = syear + startDate.substring(4);
			sp.getSp().put("startDate", beginDate);
		}
		if(sp.getSp().get("endDate") != null && !"".equals(spEndDate)) {
			endDate =  sp.getSp().get("endDate").toString().replace("-", "");
			executedEndDate = endDate.substring(0,4);
			iyear =Integer.parseInt(executedEndDate) - 1;
			String eDate = iyear + endDate.substring(4);
			sp.getSp().put("endDate", eDate);
		}
		return sp;
	}

	@Override
	public ModelAndView openDialog(String method,String custNo) {
		ModelAndView view = new ModelAndView();
		RiskLevelDto rld = customerDataManagerDao.getRiskLevelInfoByCustNo(custNo);
		view.addObject("riskLevelDto",rld);
		if("update".equals(method)) {
			view.setViewName("jsp/customerDataMgr/updateDataManager");
		}else if ("detail".equals(method)) {
			view.setViewName("jsp/customerDataMgr/customerDataManagerDetail");
		}
		return view;
	}

	
}
