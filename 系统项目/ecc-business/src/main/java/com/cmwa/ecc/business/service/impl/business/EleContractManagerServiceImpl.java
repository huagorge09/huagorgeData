package com.cmwa.ecc.business.service.impl.business;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.business.EleContractDao;
import com.cmwa.ecc.business.entity.trade.EleContractDto;
import com.cmwa.ecc.business.service.business.EleContractManagerService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

@Service
public class EleContractManagerServiceImpl implements EleContractManagerService {
	private static Logger logger = Logger.getLogger(EleContractManagerServiceImpl.class);
	
	@Autowired
	private EleContractDao eleContractDao;
	
	@Override
	public Page<EleContractDto> getEleContractList(SearchParam sp) {
		List<EleContractDto> queryContrantList = eleContractDao.queryContractListPage(sp);
		return Page.create(queryContrantList, sp.getStart(), sp.getLimit(),sp.getTotal(),true);
	}

}
