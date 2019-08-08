package com.cmwa.ecc.business.service.impl.workdays;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.workdays.WorkdaysDao;
import com.cmwa.ecc.business.entity.workdays.WorkdaysVo;
import com.cmwa.ecc.business.service.workdays.WorkdaysService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.workdays.EBankParamConstant;
@Service
public class WorkdaysServiceImpl implements WorkdaysService {
	 private static final Log log = LogFactory.getLog(WorkdaysServiceImpl.class.getName());

	    @Autowired
	    private WorkdaysDao workdaysDao;
	    
		@Override
		public WorkdaysVo create(String operator, WorkdaysVo workdaysVo)
				throws Exception {
			if (operator == null || workdaysVo == null) {
	            workdaysVo.setResultCode(EBankParamConstant.PM_CO_EBANK$ERR_CODE$9999);
	            return workdaysVo;
	        }
			WorkdaysVo oldWorkdaysVo = getWorkDate(workdaysVo.getFundid(), workdaysVo.getWorkdate());
			if(oldWorkdaysVo == null){
				workdaysDao.create(workdaysVo);
				workdaysVo.setResultCode(EBankParamConstant.PM_CO_EBANK$ERR_CODE$0000);
			}else {
				oldWorkdaysVo.setWorkflag(workdaysVo.getWorkflag());
	        	workdaysVo = update(operator, oldWorkdaysVo);
			}
			return workdaysVo;
		}

		@Override
		public WorkdaysVo update(String operator, WorkdaysVo workdaysVo)
				throws Exception {
			if (operator == null || workdaysVo == null) {
	            workdaysVo.setResultCode(EBankParamConstant.PM_CO_EBANK$ERR_CODE$9999);
	            return workdaysVo;
	        }
			try {
				workdaysDao.update(workdaysVo);
				workdaysVo.setResultCode(EBankParamConstant.PM_CO_EBANK$ERR_CODE$0000);
			} catch (Exception e) {
				log.error("修改工作日异常",e);
				workdaysVo.setResultCode(EBankParamConstant.PM_CO_EBANK$ERR_CODE$9999);
			}
			return workdaysVo;
		}

		@Override
		public WorkdaysVo delete(String operator, String fundid, String workdate)
				throws Exception {
			WorkdaysVo workdaysVo = new WorkdaysVo();
			if (operator == null || fundid == null || workdate == null) {
	            workdaysVo.setResultCode(EBankParamConstant.PM_CO_EBANK$ERR_CODE$9999);
	            return workdaysVo;
	        }
			try {
				workdaysDao.delete(operator, fundid, workdate);
				workdaysVo.setResultCode(EBankParamConstant.PM_CO_EBANK$ERR_CODE$0000);
			} catch (Exception e) {
				log.error("删除工作日异常",e);
				workdaysVo.setResultCode(EBankParamConstant.PM_CO_EBANK$ERR_CODE$9999);
			}
			return workdaysVo;
		}

		@Override
		public WorkdaysVo getWorkDate(String fundid, String workdate)
				throws Exception {
			WorkdaysVo workdaysVo = new WorkdaysVo();
			if (fundid == null || workdate == null) {
	            return null;
	        }
			try {
				workdaysVo = workdaysDao.getWorkDate(fundid, workdate);
				if(workdaysVo!=null){
					workdaysVo.setResultCode(EBankParamConstant.PM_CO_EBANK$ERR_CODE$0000);
				}
			} catch (Exception e) {
				log.error("获取单个工作日信息异常",e);
				workdaysVo.setResultCode(EBankParamConstant.PM_CO_EBANK$ERR_CODE$9999);
			}
			return workdaysVo;
		}

		@Override
		public Page<WorkdaysVo> getWorkDateListPage(SearchParam sp) throws Exception {
			List<WorkdaysVo> list = new ArrayList<WorkdaysVo>();
			try {
				list = workdaysDao.getWorkDateListPage(sp);
			} catch (Exception e) {
				log.error("获取所有工作日信息异常",e);
			}
			return Page.create(list, sp.getStart(), sp.getLimit(), sp.getTotal() , false);
		}

		@Override
		public WorkdaysVo getWorkDateNow(String apkind) throws Exception {
			WorkdaysVo workdaysVo = new WorkdaysVo();
			SearchParam sp = new SearchParam();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("apkind", apkind);
			sp.setSp(map);
			workdaysDao.getWorkDateNow(sp);
			if (sp.getSp().get("resultCode").equals("0000")) {
				workdaysVo.setResultCode(sp.getSp().get("resultCode").toString());
				workdaysVo.setWorkdate(sp.getSp().get("workdate").toString());
			}
			return workdaysVo;
		}

		@Override
		public WorkdaysVo getWorkDateByDate(String apkind, String date, String time)
				throws Exception {
			return workdaysDao.getWorkDateByDate(apkind, date, time);
		}

		@Override
		public WorkdaysVo getOtherWorkDate(String now, String datenum,
				String type, String fundid) throws Exception {
			WorkdaysVo workdaysVo = new WorkdaysVo();
			SearchParam sp = new SearchParam();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("now", now);
			map.put("datenum", datenum);
			map.put("type", type);
			map.put("fundid", fundid);
			sp.setSp(map);
			workdaysDao.getOtherWorkDate(sp);
			if (sp.getSp().get("resultCode").equals("0000")) {
				workdaysVo.setResultCode(sp.getSp().get("resultCode").toString());
				workdaysVo.setWorkdate(sp.getSp().get("workdate").toString());
			}
			return workdaysVo;
		}
	   
}
