package com.cmwa.ecc.business.service.impl.mail;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.dao.fundinfo.FundInfoDao;
import com.cmwa.ecc.business.dao.mail.MailDao;
import com.cmwa.ecc.business.dao.workdays.WorkdaysDao;
import com.cmwa.ecc.business.entity.mail.MailAppend;
import com.cmwa.ecc.business.entity.mail.MailAppendDetail;
import com.cmwa.ecc.business.entity.mail.MailConfPath;
import com.cmwa.ecc.business.entity.mail.MailConfig;
import com.cmwa.ecc.business.entity.mail.MailSendRecord;
import com.cmwa.ecc.business.service.mail.MailService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.SearchParam;

@Service
public class MailServiceImpl implements MailService {
	private Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@Autowired
	private MailDao	mailDao;
	
	@Autowired
	private WorkdaysDao workdaysDao;
	
	@Autowired
	private FundInfoDao  fundInfoDao;
	
	@Override
	public Page<MailConfig> queryMailConfigListPage(SearchParam sp) {
		List<MailConfig> items =mailDao.queryMailConfigListPage(sp);
		return Page.create(items, sp.getStart(), sp.getLimit(), sp.getTotal());
	}
	
	@Override
	public String addRecord(MailSendRecord record) throws Exception {
		mailDao.addRecord(record);
		return record.getMsrId();
	}
	
	
	@Override
	public void setResult(String id, String result) throws Exception {
		mailDao.setResult(id, result);
	}
	
	
	@Override
	public void updMailConfig(MailConfig config, List<MailAppend> appends) throws Exception {
		mailDao.deleteAppend(config.getMcId());
		mailDao.updConfig(config);
		if(appends.size() > 0 ){
			mailDao.addAppend(appends);
		}
	}
	
	
	@Override
	public MailConfig queryMailConfig(String id) throws Exception {
		return mailDao.queryMailConfig(id);
	}
	
	
	@Override
	public List<MailAppendDetail> queryMailAppendList(String configId, String category) throws Exception {
		List<MailAppendDetail> list = mailDao.queryMailAppendList(configId, category);
		return list == null?new LinkedList<MailAppendDetail>() : list;
	}

	@Override
	public void mailStatusOperate(MailConfig maillConfig) {
		mailDao.mailStatusOperate(maillConfig);
	}

	@Override
	public MailConfPath queryMailPath(String confId) throws Exception {
		MailConfPath mailConfPath = mailDao.queryMailPath(confId);
		return mailConfPath;
	}

	@Override
	public void addMailPath(MailConfPath mailConfPath) {
		try {
			mailDao.addMailPath(mailConfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void updateMailPath(MailConfPath mailConfPath) {
		try {
			mailDao.updateMailPath(mailConfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	@Override
	public void deleteMailPath(MailConfPath mailConfPath) {
		try {
			mailDao.deleteMailPath(mailConfPath);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	

	
	
	
	


}
