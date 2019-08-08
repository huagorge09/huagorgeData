package com.cmwa.ecc.business.service.impl.customerDataManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmwa.ecc.business.controller.account.TradeManagerController;
import com.cmwa.ecc.business.dao.customerDataMgr.CustTradeDao;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.RiskLevelDto;
import com.cmwa.ecc.business.service.customerDataManager.CustTradeService;

@Service
public class CustTradeServiceImpl implements CustTradeService {
	
	private static Logger logger = LoggerFactory.getLogger(TradeManagerController.class);
	
	@Autowired
	private CustTradeDao custTradeDao;
	
	@Override
	public OpenAccountDto modifyCategoryV2(OpenAccountDto dto) throws Exception {
		try {
			OpenAccountDto rtrnDto = new OpenAccountDto();
			if (dto == null) {
				rtrnDto = new OpenAccountDto();
				logger.debug("(DS-NULL)参数为空");
				rtrnDto.setErrcode("9998");
				return rtrnDto;
			}
			String custnos = dto.getCustno();
			if (custnos == null) {
				rtrnDto = new OpenAccountDto();
				logger.debug("(DS-NULL)参数为空");
				rtrnDto.setErrcode("9998");
				return rtrnDto;
			}
			dto = fixedOpenAccountDtoInfo(dto);
			String instrepvalidate = dto.getInstrepvalidate();
			String idvalidate = dto.getIdvalidate();
			String priciplevalidt = dto.getPrincipalvalidt();
			String holdingvalidate = dto.getHoldingvalidate();
			if(instrepvalidate == null) {
				instrepvalidate = "";
			}else {
				instrepvalidate = instrepvalidate.replace("-", "");
			}
			if(idvalidate == null) {
				idvalidate = "";
			}else {
				idvalidate = idvalidate.replace("-", "");
			}
			if(priciplevalidt == null) {
				priciplevalidt = "";
			}else {
				priciplevalidt = priciplevalidt.replace("-", "");
			}
			if(holdingvalidate == null) {
				holdingvalidate = "";
			}else {
				holdingvalidate = holdingvalidate.replace("-", "");
			}
			dto.setInstrepvalidate(instrepvalidate);
			dto.setIdvalidate(idvalidate);
			dto.setPrincipalvalidt(priciplevalidt);
			dto.setHoldingvalidate(holdingvalidate);
			custTradeDao.modifyCategoryV2(dto);
			rtrnDto = dto;
			System.out.println(rtrnDto.getErrcode() + "===" + rtrnDto.getErrmsg());
			return rtrnDto;
		} catch (Exception e) {
			OpenAccountDto oad = new OpenAccountDto();
			oad.setErrcode("9999");
			oad.setErrmsg("更新失败");
			logger.info("modifyCategoryV2" +e);
			return oad;
		}
	}

	@Override
	public RiskLevelDto updateVoiceRecord(RiskLevelDto dto) throws Exception {
		RiskLevelDto rld = new RiskLevelDto();
		try {
			custTradeDao.updateVoiceRecord(dto);
			
			
			rld  = dto;
		} catch (Exception e) {
			logger.info("updateVoiceRecord" +e);
		}
		return rld;
	}

	@Override
	public RiskLevelDto auditCustRiskLevelV2(RiskLevelDto dto) throws Exception {
		RiskLevelDto rld = new RiskLevelDto();
		try {
			custTradeDao.auditCustRiskLevelV2(dto);
			rld = dto;
		} catch (Exception e) {
			logger.info("auditCustRiskLevelV2" +e);
		}
		return rld;
	}
	
public OpenAccountDto fixedOpenAccountDtoInfo(OpenAccountDto dto) {
	
		String custnos = dto.getCustno();
		String[] custnoArray = custnos.split(",");
		String newCustno = "";
		for (int i = 0; i < custnoArray.length; i++) {
			System.out.println("***********************************************************");
			System.out.println(custnoArray[i]);
			System.out.println("***********************************************************");
			if(i==custnoArray.length-1){
				newCustno += custnoArray[i];
			}else{
				newCustno += custnoArray[i] + ",";
			}
		}
		dto.setNewCustno(newCustno);
		String oidtp = dto.getOidtp();
		String oidno = dto.getOidno();
		String oidvalidate = dto.getOidvalidate();

		if (oidtp == null) {
			oidtp = "";
		}
		if (oidno == null) {
			oidno = "";
		}
		if (oidvalidate == null) {
			oidvalidate = "";
		}

		String[] oidtpArray = oidtp.split(",");
		String[] oidnoArray = oidno.split(",");
		String[] oidvalidateArray = oidvalidate.split(",");

		Object[] oidinfo = new Object[oidtpArray.length];
		for (int i = 0; i < oidtpArray.length; i++) {
			if (i < oidtpArray.length && i < oidnoArray.length
					&& i < oidvalidateArray.length) {
				String[] result = new String[3];
				if (oidtpArray[i] == null || "$".equals(oidtpArray[i])) {
					result[0] = "";
				} else {
					result[0] = oidtpArray[i];
				}
				if (oidnoArray[i] == null || "$".equals(oidnoArray[i])) {
					result[1] = "";
				} else {
					result[1] = oidnoArray[i];
				}
				if (oidvalidateArray[i] == null
						|| "$".equals(oidvalidateArray[i])) {
					result[2] = "";
				} else {
					result[2] = oidvalidateArray[i].replaceAll("-", "");
				}
				oidinfo[i] = result;
			}
		}
		//其他证件信息********************
		String newOidStr = getStringByArrays(oidinfo,"||;","||,");
		logger.debug("其他证件信息newOidStr:::"+newOidStr);
		dto.setNewOidStr(newOidStr);

		// 资料信息
		String document = dto.getCustdocument();
		if (document == null) {
			document = "";
		}
		String[] docArr = document.split(",");

		Object[] docinfo = new Object[docArr.length];
		for (int i = 0; i < docArr.length; i++) 
		{
			String[] result = new String[2];
			if (docArr[i] == null || "$".equals(docArr[i])) 
			{
				result[0] = "";
				result[1] = "";
			} 
			else 
			{
				String[] s2 = docArr[i].split("-");
				if (s2.length == 2) 
				{
					result[0] = s2[0];
					result[1] = s2[1];
				}
			}
			docinfo[i] = result;
		}
		//资料信息********************
		String newDocStr = getStringByArrays(docinfo,"||;","||,");
		logger.debug("资料信息newDocStr:::"+newDocStr);

		// 其他经办人
		String ocontactgrant = dto.getOcontactgrant();
		String ocontact = dto.getOcontact();
		String ocontactnation = dto.getOcontactnation();
		String ocontidtp = dto.getOcontidtp();
		String ocontidno = dto.getOcontidno();
		String ocontvalidate = dto.getOcontvalidate();
		String ocontphone = dto.getOcontphone();
		String ocontfax = dto.getOcontfax();
		String ocontmobile = dto.getOcontmobile();
		String ocontemail = dto.getOcontemail();
		String ocontAddr = dto.getOcontAddr();
		String ocontPostcode = dto.getOcontPostcode();
		

		if (ocontactgrant == null) {
			ocontactgrant = "";
		}
		if (ocontact == null) {
			ocontact = "";
		}
		if (ocontactnation == null) {
			ocontactnation = "";
		}
		if (ocontidtp == null) {
			ocontidtp = "";
		}
		if (ocontidno == null) {
			ocontidno = "";
		}
		if (ocontvalidate == null) {
			ocontvalidate = "";
		}
		if (ocontphone == null) {
			ocontphone = "";
		}
		if (ocontfax == null) {
			ocontfax = "";
		}
		if (ocontmobile == null) {
			ocontmobile = "";
		}
		if (ocontemail == null) {
			ocontemail = "";
		}
		if(ocontAddr == null){
			ocontAddr = "";
		}
		if(ocontPostcode == null){
			ocontPostcode = "";
		}
		String[] ocontactgrantArray = ocontactgrant.split(",");
		String[] ocontactArray = ocontact.split(",");
		String[] ocontactnationArray = ocontactnation.split(",");
		String[] ocontidtpArray = ocontidtp.split(",");
		String[] ocontidnoArray = ocontidno.split(",");
		String[] ocontvalidateArray = ocontvalidate.split(",");
		String[] ocontphoneArray = ocontphone.split(",");
		String[] ocontfaxArray = ocontfax.split(",");
		String[] ocontmobileArray = ocontmobile.split(",");
		String[] ocontemailArray = ocontemail.split(",");
		String[] ocontAddrArray = ocontAddr.split(",");
		String[] ocontPostcodeArray = ocontPostcode.split(",");

		// 经办人信息
		Object[] ocontactinfo = new Object[ocontactgrantArray.length + 1];
		// 主经办人信息
		String[] mresult = new String[13];
		
		logger.info("ocontactinfo====="+ocontactinfo.length);
		
		if (dto.getContactgrant() == null) {
			mresult[0] = "";
		} else {
			mresult[0] = dto.getContactgrant();
		}
		if (dto.getContact() == null) {
			mresult[1] = "";
		} else {
			mresult[1] = dto.getContact();
		}
		if (dto.getContactnation() == null)

		{
			mresult[2] = "";
		} else {
			mresult[2] = dto.getContactnation();
		}
		if (dto.getContidtp() == null) {
			mresult[3] = "";
		} else {
			mresult[3] = dto.getContidtp();
		}
		if (dto.getContidno() == null) {
			mresult[4] = "";
		} else {
			mresult[4] = dto.getContidno();
		}
		if (dto.getContvalidate() == null) {
			mresult[5] = "";
		} else {
			mresult[5] = dto.getContvalidate().replaceAll("-", "");
		}
		if (dto.getContphone() == null) {
			mresult[6] = "";
		} else {
			mresult[6] = dto.getContphone();
		}
		if (dto.getContfax() == null) {
			mresult[7] = "";
		} else {
			mresult[7] = dto.getContfax();
		}
		if (dto.getContmobile() == null) {
			mresult[8] = "";
		} else {
			mresult[8] = dto.getContmobile();
		}
		if (dto.getContemail() == null) {
			mresult[9] = "";
		} else {
			mresult[9] = dto.getContemail();
		}
		mresult[10] = "1";
		if (dto.getContAddr() == null) {
			mresult[11] = "";
		} else {
			mresult[11] = dto.getContAddr();
		}
		if (dto.getContPostcode() == null) {
			mresult[12] = "";
		} else {
			mresult[12] = dto.getContPostcode();
		}
		logger.info("mresult======"+mresult.length);
		ocontactinfo[0] = mresult;
		
		// 其他经办人信息
		for (int i = 0; i < ocontactgrantArray.length; i++) {
			if (i < ocontactgrantArray.length && i < ocontactArray.length
					&& i < ocontactnationArray.length
					&& i < ocontidtpArray.length && i < ocontidnoArray.length
					&& i < ocontvalidateArray.length
					&& i < ocontphoneArray.length && i < ocontfaxArray.length
					&& i < ocontmobileArray.length
					&& i < ocontemailArray.length) {
				String[] result = new String[13];
				if (ocontactgrantArray[i] == null
						|| "$".equals(ocontactgrantArray[i])) {
					result[0] = "";
				} else {
					result[0] = ocontactgrantArray[i];
				}
				if (ocontactArray[i] == null || "$".equals(ocontactArray[i])) {
					result[1] = "";
				} else {
					result[1] = ocontactArray[i];
				}
				if (ocontactnationArray[i] == null
						|| "$".equals(ocontactnationArray[i])) {
					result[2] = "";
				} else {
					result[2] = ocontactnationArray[i];
				}
				if (ocontidtpArray[i] == null || "$".equals(ocontidtpArray[i])) {
					result[3] = "";
				} else {
					result[3] = ocontidtpArray[i];
				}
				if (ocontidnoArray[i] == null || "$".equals(ocontidnoArray[i])) {
					result[4] = "";
				} else {
					result[4] = ocontidnoArray[i];
				}
				if (ocontvalidateArray[i] == null
						|| "$".equals(ocontvalidateArray[i])) {
					result[5] = "";
				} else {
					result[5] = ocontvalidateArray[i].replaceAll("-", "");
				}
				if (ocontphoneArray[i] == null
						|| "$".equals(ocontphoneArray[i])) {
					result[6] = "";
				} else {
					result[6] = ocontphoneArray[i];
				}
				if (ocontfaxArray[i] == null || "$".equals(ocontfaxArray[i])) {
					result[7] = "";
				} else {
					result[7] = ocontfaxArray[i];
				}
				if (ocontmobileArray[i] == null
						|| "$".equals(ocontmobileArray[i])) {
					result[8] = "";
				} else {
					result[8] = ocontmobileArray[i];
				}
				if (ocontemailArray[i] == null
						|| "$".equals(ocontemailArray[i])) {
					result[9] = "";
				} else {
					result[9] = ocontemailArray[i];
				}
				result[10] = "0";
				if (ocontAddrArray[i] == null
						|| "$".equals(ocontAddrArray[i])) {
					result[11] = "";
				} else {
					result[11] = ocontAddrArray[i];
				}
				if (ocontPostcodeArray[i] == null
						|| "$".equals(ocontPostcodeArray[i])) {
					result[12] = "";
				} else {
					result[12] = ocontPostcodeArray[i];
				}
				ocontactinfo[i + 1] = result;
			}
		}
		//其他经办人信息********************
		String newOcontStr = getStringByArrays(ocontactinfo,"||;","||,");
		dto.setNewOcontStr(newOcontStr);
		return dto;
	}
	
	public String getStringByArrays(Object[] arrays,String maxDivision,String minDivision){
		String retStr = "";
		try{
			for(int i=0;i<arrays.length;i++){
				String[] s2 = (String[])arrays[i];
				if(s2 != null && s2.length>0){
					for(int j=0;j<s2.length;j++){
						if(j==s2.length-1){
							retStr += s2[j];
						}else{
							retStr += s2[j] + minDivision;
						}
					}
				}
				if(i<arrays.length-1){
					retStr += maxDivision;
				}
			}
			if(retStr.equals("null||,null")){
				retStr = "";
			}
			logger.debug("::getStringByArrays：："+retStr);
		}catch(Exception e){
			logger.error(e.toString());
		}
		return retStr;
	}
}
