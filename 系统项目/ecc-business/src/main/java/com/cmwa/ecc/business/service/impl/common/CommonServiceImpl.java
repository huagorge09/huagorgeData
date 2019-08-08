package com.cmwa.ecc.business.service.impl.common;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.common.CommonDao;
import com.cmwa.ecc.business.entity.dsbanks.DSBanksVo;
import com.cmwa.ecc.business.entity.paramvo.ParameterVo;
import com.cmwa.ecc.business.entity.workdate.WorkDateVo;
import com.cmwa.ecc.business.service.common.CommonService;
import com.cmwa.ecc.business.utils.SearchParam;
@Service
public class CommonServiceImpl implements CommonService {
	
	@Autowired
	private CommonDao commonDao;

	@Override
	public List<ParameterVo> getParameterListPage(SearchParam sp) {
		return commonDao.getParameterListPage(sp);
	}

	@Override
	public List<ParameterVo> getParameterListPage(String paramPmstSystem,String string) {
		SearchParam sp = new SearchParam();
		sp.setSp(new HashMap<String,Object>());
		sp.getSp().put("pmst", paramPmstSystem);
		sp.getSp().put("allPmky", string);
		return  commonDao.getParameterListPage(sp);
	}


	@Override
	public void getWorkDates(WorkDateVo workDateVo) {
		commonDao.getWorkDates(workDateVo);
	}



	@Override
	public int getParameterListTotal(SearchParam sp) {
		return commonDao.getParameterListTotal(sp);
	}


	@Override
	public List<DSBanksVo> getBankDetail(SearchParam sp) {
		return commonDao.getBankDetail(sp);
	}


	@Override
	public List<ParameterVo> queryAllParameterListPage(SearchParam sp) {
		return commonDao.queryAllParameterListPage(sp);
	}


	@Override
	public int queryAllParameterTotal(SearchParam sp) {
		return commonDao.queryAllParameterTotal(sp);
	}


	@Override
	public void addParameter(ParameterVo parameterVo) {
		commonDao.addParameter(parameterVo);
	}


	@Override
	public void updateParameter(ParameterVo parameterVo) {
		commonDao.updateParameter(parameterVo);
	}


	@Override
	public void delParameter(ParameterVo parameterVo) {
		commonDao.delParameter(parameterVo);
	}


	@Override
	public List<ParameterVo> queryBusinParameterListPage(SearchParam sp) {
		return commonDao.queryBusinParameterListPage(sp);
	}

	@Override
	public List<ParameterVo> getParameterAllList(SearchParam sp) {
		return commonDao.getParameterListPage(sp);
	}

	@Override
	public List<ParameterVo> getParameterAllList(String paramPmstSystem,
			String string) {
		SearchParam sp = new SearchParam();
		sp.setSp(new HashMap<String,Object>());
		sp.getSp().put("pmst", paramPmstSystem);
		sp.getSp().put("allPmky", string);
		return  commonDao.getParameterAllList(sp);
	}

}
