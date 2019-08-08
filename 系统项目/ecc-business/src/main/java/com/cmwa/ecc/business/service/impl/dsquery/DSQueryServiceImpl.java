package com.cmwa.ecc.business.service.impl.dsquery;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.cmwa.ecc.business.dao.dsquery.DSQueryDao;
import com.cmwa.ecc.business.service.dsquery.DSQueryService;
import com.cmwa.ecc.business.utils.MD5;

@Service
public class DSQueryServiceImpl implements DSQueryService{
	Logger logger = Logger.getLogger(DSQueryServiceImpl.class);
	@Autowired
	DSQueryDao dsQueryDao;
	@Override
	public String getUrlAndEncrypt(String permissionId, String opId) {
		String loginId = dsQueryDao.getCustId(opId);
		String SSOKey = dsQueryDao.querySSOKey();
		String url = dsQueryDao.queryUrlByCode(permissionId);
		logger.info("DSQueryServiceImpl.loginId:"+loginId);
		logger.info("DSQueryServiceImpl.SSOKey:"+SSOKey);
		logger.info("DSQueryServiceImpl.url:"+url);
		StringBuffer sbClrRptEncryString = new StringBuffer();
		String src = "ECC";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMyyHHmmss");
		String curtime = sdf.format(new Date());
		String returnStr = "";
		MD5 md5 = new MD5();
		
		if(url != null && !url.equals(""))
		{
			int bosidIndex = url.indexOf("bosid=");
			if(bosidIndex > 0){
				String bosid = url.substring(bosidIndex+6,url.length());
				logger.info("DSQueryServiceImpl.bosid:"+bosid);
				sbClrRptEncryString.append(bosid);
			}
		}
		sbClrRptEncryString.append(loginId);
		sbClrRptEncryString.append(src);
		sbClrRptEncryString.append(curtime);
		sbClrRptEncryString.append(SSOKey);
		logger.info("DSQueryServiceImpl.sbClrRptEncryString.toString:"+sbClrRptEncryString.toString());
		returnStr = md5.getMD5ofStr(sbClrRptEncryString.toString());
		logger.info("DSQueryServiceImpl.returnStr:"+returnStr);
		returnStr = url+"&loginid="+loginId+"&src="+src+"&curtime="+curtime+"&encryString="+returnStr;
		return returnStr;
	}
	@Override
	public JSONObject queryEccSystemInfo() {
		JSONObject result = new JSONObject();
		Map<String, Object> sysMap = dsQueryDao.queryEccSystemInfo();
		result = JSONObject.fromObject(sysMap);
		return result;
	}



}
