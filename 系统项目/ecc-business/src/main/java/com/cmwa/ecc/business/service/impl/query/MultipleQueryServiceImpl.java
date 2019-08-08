package com.cmwa.ecc.business.service.impl.query;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.query.MultipleQueryDao;
import com.cmwa.ecc.business.entity.multiple.FundBalanceSrhVo;
import com.cmwa.ecc.business.entity.multiple.FundCostFalseVo;
import com.cmwa.ecc.business.entity.multiple.FundCustInfoVo;
import com.cmwa.ecc.business.entity.multiple.FundNavVo;
import com.cmwa.ecc.business.service.query.MultipleQueryService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

@Service
public class MultipleQueryServiceImpl implements MultipleQueryService{
	
	@Autowired
	private MultipleQueryDao multipleQueryDao;
	
	@Override
	public Page<FundCustInfoVo> queryFundCustInfoListPage(SearchParam sp) throws Exception {
		List<FundCustInfoVo> queryFundCustInfoListPage =multipleQueryDao.queryFundCustInfoListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(multipleQueryDao.queryFundCustInfoListTotal(sp));
		return Page.create(queryFundCustInfoListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	@Override
	public Page<FundNavVo> queryFundNavListPage(SearchParam sp) throws Exception {
		sp = changeDatePattern(sp);
		List<FundNavVo> queryFundNavListPage =multipleQueryDao.queryFundNavListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(multipleQueryDao.queryFundNavListTotal(sp));
		return Page.create(queryFundNavListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	@Override
	public Page<FundBalanceSrhVo> queryFundBalanceListPage(SearchParam sp) throws Exception {
		List<FundBalanceSrhVo> queryFundBalanceListPage =multipleQueryDao.queryFundBalanceListPage(sp);
		sp.setStart((sp.getPageNo() - Page.FIRST_PAGE) * sp.getLimit());
		sp.setEnd(sp.getStart() + sp.getLimit());
		sp.setTotal(multipleQueryDao.queryFundBalanceListTotal(sp));
		return Page.create(queryFundBalanceListPage, sp.getStart(), sp.getLimit(),
				sp.getTotal());
	}

	@Override
	public Page<FundCostFalseVo> queryFundCostFasleListPage(SearchParam sp) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	
	/* 起止日期更换格式 */
	public SearchParam  changeDatePattern(SearchParam sp) {
		//起止日期更换格式
		String startDate = null;
		String endDate = null;
		if(sp.getSp().get("startDate") != null) {
			startDate =  sp.getSp().get("startDate").toString().replace("-", "");
			sp.getSp().put("startDate", startDate);
		}
		if(sp.getSp().get("endDate") != null) {
			endDate =  sp.getSp().get("endDate").toString().replace("-", "");
			sp.getSp().put("endDate", endDate);
		}
		return sp;
	}

}
