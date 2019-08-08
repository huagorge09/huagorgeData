package com.cmwa.ecc.business.service.impl.changeRecord;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.changeRecord.CustRisklvelInvprtpDetailDao;
import com.cmwa.ecc.business.entity.changeRecord.CustRisklvelInvprtpDetail;
import com.cmwa.ecc.business.service.changeRecord.CustRisklvelInvprtpDetailService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.PageUtil;
import com.cmwa.ecc.business.utils.SearchParam;

@Service
public class CustRisklvelInvprtpDetailServiceImpl implements CustRisklvelInvprtpDetailService{

	private Logger logger = Logger.getLogger(CustRisklvelInvprtpDetailServiceImpl.class.getName());
	
	@Autowired
	private CustRisklvelInvprtpDetailDao custRisklvelInvprtpDetailDao;
	
	@Override
	public Page<CustRisklvelInvprtpDetail> queryChangeRecord(SearchParam sp) {
		List<CustRisklvelInvprtpDetail> changeRecordListPage = new ArrayList<CustRisklvelInvprtpDetail>();
		PageUtil<CustRisklvelInvprtpDetail> page = new PageUtil<CustRisklvelInvprtpDetail>();
		List<CustRisklvelInvprtpDetail> resultList = new ArrayList<CustRisklvelInvprtpDetail>();
		try {
			changeRecordListPage = custRisklvelInvprtpDetailDao.queryChangeRecordByCustNo(sp);
			resultList = page.getPageList(sp, changeRecordListPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return Page.create(resultList,  sp.getStart(), sp.getLimit(), changeRecordListPage.size());
	}

}
