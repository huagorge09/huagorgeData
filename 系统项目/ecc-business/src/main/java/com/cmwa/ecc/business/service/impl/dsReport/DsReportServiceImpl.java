package com.cmwa.ecc.business.service.impl.dsReport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ec.base.util.Encrypt;
import com.cmwa.ecc.business.dao.dsReport.DsReportDao;
import com.cmwa.ecc.business.service.dsReport.DsReportService;

/**
 * @author ex-wuh2
 *
 */
@Service
public class DsReportServiceImpl implements DsReportService {
	
	private static Logger logger = Logger.getLogger(DsReportServiceImpl.class);	
	
	@Autowired
	private DsReportDao dsReportDao;
	
	@Override
	public String getUrlByCode(String opCode) {
		return dsReportDao.getUrlByCode(opCode);
	}

	@Override
	public String getSSOKey() {
		return dsReportDao.getSSOKey();
	}

	@Override
	public String getCustId(String opid) {
		return dsReportDao.getCustId(opid);
	}
	@Override
	public String getUrlAndEncrypt(String opCode,String loginid,String src,String curtime,String SSOKey){
		String returnUrl = "";
		StringBuffer sbClrRptEncryString = new StringBuffer();
		String encryptString = "";
		try{
			String url = getUrlByCode(opCode);//取URL
			Encrypt encrypt = new Encrypt();
			if(url != null && !url.equals(""))
			{
				int bosidIndex = url.indexOf("rptid=");
				if(bosidIndex > 0){
					String bosid = url.substring(bosidIndex+6,url.length());
					sbClrRptEncryString.append(bosid);
				}
			}
			sbClrRptEncryString.append(loginid);
			sbClrRptEncryString.append(src);
			sbClrRptEncryString.append(curtime);
			sbClrRptEncryString.append(SSOKey);
			encryptString = encrypt.passwordEncrypt(sbClrRptEncryString.toString());//MD5加密
			
			returnUrl = url+"&loginid="+loginid+"&src="+src+"&curtime="+curtime+"&encryString="+encryptString;
		}catch(Exception e){
			logger.error("DSReportDelegate.getUrlAndEncrypt异常："+e);
			e.printStackTrace();
		}
		return returnUrl;
	}

}
