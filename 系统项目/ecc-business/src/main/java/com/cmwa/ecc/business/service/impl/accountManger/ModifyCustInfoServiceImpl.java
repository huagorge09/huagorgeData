package com.cmwa.ecc.business.service.impl.accountManger;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ec.base.util.Sequences;
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ecc.business.dao.accountManger.CommonQueryDao;
import com.cmwa.ecc.business.dao.accountManger.ModifyCustInfoDao;
import com.cmwa.ecc.business.dao.accountManger.OpenAccountDao;
import com.cmwa.ecc.business.dao.widget.WidgetDao;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.UserTaxInfoDto;
import com.cmwa.ecc.business.service.accountManger.ModifyCustInfoService;
import com.cmwa.ecc.business.service.accountManger.OpenAccountService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.PageUtil;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 客户资料管理实现类
 * @author ex-chenbq
 */
@Service
public class ModifyCustInfoServiceImpl implements ModifyCustInfoService {

	private Logger logger = Logger.getLogger(ModifyCustInfoServiceImpl.class.getName());
	
	@Autowired
	private CommonQueryDao commonQueryDao;
	
	@Autowired
	private OpenAccountDao openAccountDao;
	
	@Autowired
	private OpenAccountService openAccountService;
	
	@Autowired
	private ModifyCustInfoDao modifyCustInfoDao;
	
	@Autowired
	private WidgetDao widgetDao;
	
	@SuppressWarnings("unchecked")
	@Override
	public Page<OpenAccountDto> accountModifyQuery(SearchParam param) {
		List<OpenAccountDto> list = new ArrayList<OpenAccountDto>();
		List<OpenAccountDto> resultList = new ArrayList<OpenAccountDto>();
		PageUtil<OpenAccountDto> page = new PageUtil<OpenAccountDto>();
		try {
			commonQueryDao.accountModifyQuery(param);
			if(param.getSp().get("errcode").equals("0000")){
				list = (List<OpenAccountDto>) param.getSp().get("custInfoList");
			}
			resultList = page.getPageList(param, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("账户类修改查询异常：",e);
		}
		return Page.create(resultList, param.getStart(), param.getLimit(), list.size());
	}

	@Override
	public OpenAccountDto modifyAccount(OpenAccountDto dto) {
		try {
			// 其他证件信息
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
			dto.setNewDocStr(newDocStr);

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
            
			dto.setIdvalidate(!StringUtil.isEmpty(dto.getIdvalidate()) ? dto.getIdvalidate().replaceAll("-", "") : "");
			
			dto.setInstrepvalidate(!StringUtil.isEmpty(dto.getInstrepvalidate()) ? dto.getInstrepvalidate().replaceAll("-", "") : "");
			
			dto.setPrincipalvalidt(!StringUtil.isEmpty(dto.getPrincipalvalidt()) ? dto.getPrincipalvalidt().replaceAll("-", "") : "");

			dto.setHoldingvalidate(!StringUtil.isEmpty(dto.getHoldingvalidate()) ? dto.getHoldingvalidate().replaceAll("-", "") : "");
            try {
            	dto.setDsapkind("A");
            	modifyCustInfoDao.modifyAccount(dto);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("个人/机构资料修改异常：", e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("个人/机构资料数据解析出错！");
		}
		return dto;
	}
	
	/**
	 * 20101101 wdw add 处理二维数组，转换为字符串
	 */
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

	@Override
	public OpenAccountDto modifyBankInfo(OpenAccountDto dto) {
		try {
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
			dto.setNewDocStr(newDocStr);
            
			dto.setIdvalidate(!StringUtil.isEmpty(dto.getIdvalidate()) ? dto.getIdvalidate().replaceAll("-", "") : "");
			
			dto.setInstrepvalidate(!StringUtil.isEmpty(dto.getInstrepvalidate()) ? dto.getInstrepvalidate().replaceAll("-", "") : "");
			
			dto.setPrincipalvalidt(!StringUtil.isEmpty(dto.getPrincipalvalidt()) ? dto.getPrincipalvalidt().replaceAll("-", "") : "");

			dto.setHoldingvalidate(!StringUtil.isEmpty(dto.getHoldingvalidate()) ? dto.getHoldingvalidate().replaceAll("-", "") : "");
			
            try {
				modifyCustInfoDao.modifyBankInfo(dto);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("银行资料修改异常：", e);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("银行资料修改数据解析出错！");
		}
		return dto;
	}

	@Override
	public List<UserTaxInfoDto> queryUserTaxInfoByList(String custno) {
		List<UserTaxInfoDto> list = new ArrayList<UserTaxInfoDto>();
		try {
			list = commonQueryDao.queryUserTaxInfoByList(custno);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询税收居民信息异常：",e);
		}
		return list;
	}

	@Override
	public OpenAccountDto modifyCategoryInfo(JSONObject jsonObject) {
		OpenAccountDto accoDto = new OpenAccountDto();
		try {
			String custnos = "";
			String businessTp = "";
			String companyTp = "";
			String regionTp = "";
			String fxqTp = "";
			String fxqDesc = "";
			String permissionId = "";
			String operatorId = "";
			String custsimpnm = "";
			String instrepcode = "";
			String invprtp = "";
			String taxType = "";
			String taxTypeDecl = "";
			String threeAnnualIncome = "";
			String financialAsset = "";
			String indInvExperience = "";
			String relatedWorkExp = "";
			String finProfessions = "";
			String investProInstType = "";
			String investProInstSecond = "";
			String oneYearEndNetAsset = "";
			String oneYearEndFinAsset = "";
			String investExperience = "";
			String negativeNotFinaInst = "";
			String controlPerTaxDecl = "";
			String taxResidentData = "";
			String preInvprtp = jsonObject.getString("preInvprtp");
			if (jsonObject != null) {
				custnos = jsonObject.getString("custnos");
				businessTp = jsonObject.getString("businessTp");
				companyTp = jsonObject.getString("companyTp");
				regionTp = jsonObject.getString("regionTp");
				fxqTp = jsonObject.getString("fxqTp");
				fxqDesc = jsonObject.getString("fxqDesc");
				permissionId = jsonObject.getString("permissionId");
				operatorId = jsonObject.getString("operatorId");
				invprtp = jsonObject.getString("invprtp");

				taxType = jsonObject.getString("taxType");
				taxTypeDecl = jsonObject.getString("taxTypeDecl");
				investProInstType = jsonObject.getString("investProInstType");
				investProInstSecond = jsonObject
						.getString("investProInstSecond");
				oneYearEndNetAsset = jsonObject.getString("oneYearEndNetAsset");
				oneYearEndFinAsset = jsonObject.getString("oneYearEndFinAsset");
				investExperience = jsonObject.getString("investExperience");
				negativeNotFinaInst = jsonObject
						.getString("negativeNotFinaInst");
				controlPerTaxDecl = jsonObject.getString("controlPerTaxDecl");
				threeAnnualIncome = jsonObject.getString("threeAnnualIncome");
				financialAsset = jsonObject.getString("financialAsset");
				indInvExperience = jsonObject.getString("indInvExperience");
				relatedWorkExp = jsonObject.getString("relatedWorkExp");
				finProfessions = jsonObject.getString("finProfessions");

				custsimpnm = jsonObject.getString("custsimpnm");
				instrepcode = jsonObject.getString("instrepcode");
				taxResidentData = jsonObject.getString("taxresident"); //税收居民数据
			}
			
			String serialNo = Sequences.getPK();// 流水号
			if (custnos != null && custnos.length() > 0) {
				accoDto.setCustno(custnos);
				accoDto.setBusinesstp(businessTp);
				accoDto.setCompanytp(companyTp);
				accoDto.setRegiontp(regionTp);
				accoDto.setAmlrisktype(fxqTp);
				accoDto.setFxqremark(fxqDesc);
				accoDto.setInvprtp(invprtp);
				accoDto.setSerialno(serialNo);

				accoDto.setPermissionId(permissionId);
				accoDto.setOperatorId(operatorId);

				accoDto.setAcctabbr(custsimpnm);
				accoDto.setInstrepcode(instrepcode);

				accoDto.setTaxType(taxType);
				accoDto.setTaxTypeDecl(taxTypeDecl);
				accoDto.setInvestProInstType(investProInstType);
				accoDto.setInvestProInstSecond(investProInstSecond);
				accoDto.setOneYearEndNetAsset(oneYearEndNetAsset);
				accoDto.setOneYearEndFinAsset(oneYearEndFinAsset);
				accoDto.setInvestExperience(investExperience);
				accoDto.setNegativeNotFinaInst(negativeNotFinaInst);
				accoDto.setControlPerTaxDecl(controlPerTaxDecl);
				accoDto.setThreeAnnualIncome(threeAnnualIncome);
				accoDto.setFinancialAsset(financialAsset);
				accoDto.setIndInvExperience(indInvExperience);
				accoDto.setRelatedWorkExp(relatedWorkExp);
				accoDto.setFinProfessions(finProfessions);
				accoDto.setTaxresident(taxResidentData);
			}
        	modifyCustInfoDao.modifyCategoryInfo(accoDto);
        	
        	if(accoDto.getErrcode().equals("0000")){
    			String taxresident=accoDto.getTaxresident(); //税收居民数据
    			//保存税收居民信息到数据库
    			if(null != taxresident && !taxresident.equals("")){
    				openAccountService.saveUserTaxInfo(accoDto.getCustno(), accoDto, taxResidentData);
    			}else{
    				openAccountService.deleteTaxInfo(accoDto.getCustno());
    			}
    		}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("客户分类信息修改异常：", e);
		}
		return accoDto;
	}

	@Override
	public OpenAccountDto modifySyntheSizeInfo(JSONObject jsonObject) {
		OpenAccountDto dto = new OpenAccountDto();
		String errcode="0000";
		String errmsg="";
		
		dto.setErrcode(errcode);
		dto.setErrmsg("综合资料信息成功！");
		
		// 第一部分
		JSONObject jObject = (JSONObject) jsonObject.get("baseInfo");
		String custtp = "";

		if (jObject != null) {
			custtp = jObject.getString("custTp");
		}
		String serialNo = Sequences.getPK();// 流水号

		dto.setSerialno(serialNo);
		dto.setInvtp(jObject.getString("custTp"));
		dto.setPermissionId(jObject.getString("permissionId"));
		dto.setOperatorId(jObject.getString("operatorId"));
		dto.setCustno(jObject.getString("custnos"));
		dto.setTrustType(jObject.getString("trusttp"));
		dto.setModifylist(jObject.getString("modifylist"));

		// 资料信息
		dto.setDocbusinesstp(jObject.getString("docbusinesstp"));
		dto.setCustdocument(jObject.getString("document"));
		dto.setIfalldocument(jObject.getString("isalldoc"));
		dto.setIfOriginal(jObject.getString("iforiginal"));
		dto.setIfsaved(jObject.getString("ifsaved"));
		dto.setIsupload(jObject.getString("isupload"));// 资料是否上传
		dto.setRemarkinfo(jObject.getString("remarkinfo"));// 资料信息备注

		dto.setShsecacc(jObject.getString("shsecacc"));// 上交所股东代码
		dto.setSzsecacc(jObject.getString("szsecacc"));// 深交所股东代码

		dto.setIsscan(jObject.getString("isscan"));
		dto.setKeepaddress(jObject.getString("keepaddress"));
		dto.setSalesaccmanager(jObject.getString("salesaccmanager"));
		dto.setFileno(jObject.getString("fileno"));
		//原风险等级
		String preRisklevel = jObject.getString("preRisklevel");
		String preInvprtp = jObject.getString("preInvprtp");
		// 机构客户
		if ("0".equals(custtp)) {
			// 基本-->其他信息
			dto.setTel(jObject.getString("orgInvOfficeTel")); // 电话
			dto.setFax(jObject.getString("orgInvFax")); // 传真
			dto.setOrganType(jObject.getString("orgType"));// 机构类型
			// 基本->证件信息
			dto.setInvnm(jObject.getString("orgInvNm"));
			dto.setIdtp(jObject.getString("orgInvIdtp"));
			dto.setIdno(jObject.getString("orgInvIdno"));
			dto.setIdvalidate(jObject.getString("orgInvIdvalidate"));

			// 基本->法人信息
			dto.setInstrepnm(jObject.getString("orgInstrepnm"));
			dto.setInstrepnation(jObject.getString("orgInstrepnation"));
			dto.setInstrepidtp(jObject.getString("orgInstrepidtp"));
			dto.setInstrepidno(jObject.getString("orgInstrepidno"));
			dto.setInstrepvalidate(jObject.getString("orgInstrepidvalidate"));

			// 基本->机构负责人信息
			dto.setPrincipalname(jObject.getString("orgPrincipalname"));
			dto.setPrincipalnation(jObject.getString("orgPrincipalnation"));
			dto.setPrincipalidtp(jObject.getString("orgPrincipalidtp"));
			dto.setPrincipalidno(jObject.getString("orgPrincipalidno"));
			dto.setPrincipalvalidt(jObject.getString("orgPrincipalidvalidate"));

			// 基本->经办人员信息
			dto.setContactgrant(jObject.getString("orgContactright"));
			dto.setContact(jObject.getString("orgContnm"));
			dto.setContactnation(jObject.getString("orgContnation"));
			dto.setContidtp(jObject.getString("orgContidtp"));
			dto.setContidno(jObject.getString("orgContidno"));
			dto.setContvalidate(jObject.getString("orgContidvalidate"));
			dto.setContphone(jObject.getString("orgContphone"));
			dto.setContfax(jObject.getString("orgContfax"));
			dto.setContmobile(jObject.getString("orgContmobile"));
			dto.setContemail(jObject.getString("orgContemail"));
			dto.setContAddr(jObject.getString("orgContAddr"));
			dto.setContPostcode(jObject.getString("orgContPostcode"));
			// 基本->其他信息
			dto.setAddr(jObject.getString("orgAddr"));
			dto.setPostcode(jObject.getString("orgPostcode"));
			// 附加->其他信息
			dto.setHoldingname(jObject.getString("orgHoldingname"));
			dto.setHoldingidtp(jObject.getString("orgHoldingidtp"));
			dto.setHoldingidno(jObject.getString("orgHoldingidno"));
			dto.setHoldingvalidate(jObject.getString("orgHoldingidvalidate"));
			dto.setBeneficiary(jObject.getString("orgBeneficiarynm"));
			dto.setCustrisklevl(jObject.getString("orgRiskLevel"));
			// 附加->其他证件信息
			dto.setOidtp(jObject.getString("oidtpStr"));
			dto.setOidno(jObject.getString("oidnoStr"));
			dto.setOidvalidate(jObject.getString("oidvalidateStr"));
			// 附加->其他经办人信息
			dto.setOcontactgrant(jObject.getString("oOrgContactright"));
			dto.setOcontact(jObject.getString("oOrgContact"));
			dto.setOcontactnation(jObject.getString("oOrgContactnation"));
			dto.setOcontidtp(jObject.getString("oOrgContactidtp"));
			dto.setOcontidno(jObject.getString("oOrgContactidno"));
			dto.setOcontvalidate(jObject.getString("oOrgContactidvalidate"));
			dto.setOcontphone(jObject.getString("oOrgContacttel"));
			dto.setOcontfax(jObject.getString("oOrgContactfax"));
			dto.setOcontmobile(jObject.getString("oOrgContactmobile"));
			dto.setOcontemail(jObject.getString("oOrgContactemail"));
			dto.setOcontAddr(jObject.getString("oOrgContactAddr"));
			dto.setOcontPostcode(jObject.getString("oOrgContactPostcode"));

		} else {
			// 基本->证件信息
			dto.setInvnm(jObject.getString("pslInvNm"));
			dto.setIdtp(jObject.getString("pslInvIdtp"));
			dto.setIdno(jObject.getString("pslInvIdno"));
			dto.setIdvalidate(jObject.getString("pslInvIdValidate"));

			// 基本->其他信息
			dto.setTel(jObject.getString("pslInvOfficeTel"));
			dto.setHousetel(jObject.getString("pslInvHomeTel"));
			dto.setMobile(jObject.getString("pslInvMobile"));
			dto.setFax(jObject.getString("pslInvFax"));
			dto.setFaxdelegate(jObject.getString("plsFaxDelegate"));
			dto.setEmail(jObject.getString("pslInvEmail"));
			// dto.setSendingroute(jObject.getString("plsBillDelivery"));
			dto.setPostcode(jObject.getString("plsPostCode"));
			dto.setAddr(jObject.getString("plsAddr"));

			// 附加信息->其他
			dto.setSex(jObject.getString("plsSex"));
			dto.setNationalitycode(jObject.getString("plsInvNation"));
			dto.setEdlevel(jObject.getString("plsInvEducation"));
			dto.setVoccode(jObject.getString("plsInvJob"));
			dto.setIncome(jObject.getString("plsInvIncome"));
			dto.setCustrisklevl(jObject.getString("plsInvRisk"));
		}
		
		try {
			// 其他证件信息
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
			dto.setNewDocStr(newDocStr);

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
            
			dto.setIdvalidate(!StringUtil.isEmpty(dto.getIdvalidate()) ? dto.getIdvalidate().replaceAll("-", "") : "");
			
			dto.setInstrepvalidate(!StringUtil.isEmpty(dto.getInstrepvalidate()) ? dto.getInstrepvalidate().replaceAll("-", "") : "");
			
			dto.setPrincipalvalidt(!StringUtil.isEmpty(dto.getPrincipalvalidt()) ? dto.getPrincipalvalidt().replaceAll("-", "") : "");

			dto.setHoldingvalidate(!StringUtil.isEmpty(dto.getHoldingvalidate()) ? dto.getHoldingvalidate().replaceAll("-", "") : "");
		
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("综合资料信息修改异常："+ e);
			return dto;
		}
		
		try {
			dto.setDsapkind("A");
			modifyCustInfoDao.modifyAccount(dto);
			if (!"0000".equals(dto.getErrcode())) {
				errcode = dto.getErrcode();
				errmsg += dto.getErrmsg();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("个人/机构资料修改异常：",e);
			dto.setErrcode("9999");
			dto.setErrmsg("综合资料信息修改异常："+ e);
			return dto;
		}
		// 银行信息保存部分
		JSONObject bankObject = (JSONObject) jsonObject.get("bankInfo");

		String tradeaccos = "";
		String openName = "";
		String bankAccoNm = "";
		String openAddr = "";
		String openbankcity = "";
		String bnkNo = "";
		String bankAcco = "";

		String permissionId = "";
		String operatorId = "";
		String trustType = "";

		if (jObject != null) {
			tradeaccos = bankObject.getString("tradeaccos");
			openName = bankObject.getString("openName");
			bankAccoNm = bankObject.getString("bankAccoNm");
			openAddr = bankObject.getString("openAddr");
			openbankcity = bankObject.getString("openbankcity");
			bnkNo = bankObject.getString("bnkNo");
			bankAcco = bankObject.getString("bankAcco");

			permissionId = bankObject.getString("permissionId");
			operatorId = bankObject.getString("operatorId");
			trustType = bankObject.getString("trustType");
		}

		OpenAccountDto accoDto = new OpenAccountDto();
		if (tradeaccos != null && tradeaccos.length() > 0) {
			accoDto.setOpenName(openName);
			accoDto.setBankAccoNm(bankAccoNm);
			accoDto.setOpenAddr(openAddr);
			accoDto.setOpenBankCity(openbankcity);
			accoDto.setBnkNo(bnkNo);
			accoDto.setBankAcco(bankAcco);
			accoDto.setTradeacco(tradeaccos);

			accoDto.setPermissionId(permissionId);
			accoDto.setOperatorId(operatorId);
			accoDto.setTrustType(trustType);
		}

		accoDto.setFlag("N");
		try {
        	modifyCustInfoDao.modifyBankInfo(accoDto);
        	if (!"0000".equals(accoDto.getErrcode())) {
				errcode = accoDto.getErrcode();
				errmsg += "，"+accoDto.getErrmsg();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("银行资料修改异常：", e);
			dto.setErrcode("9999");
			dto.setErrmsg("综合资料信息修改异常："+ e);
			return dto;
		}

		//分类信息修改
		JSONObject category = (JSONObject) jsonObject.get("categoryInfo");

		String custnos = "";
		String businessTp = "";
		String companyTp = "";
		String regionTp = "";
		String fxqTp = "";
		String fxqDesc = "";

		String permissionId1 = "";
		String operatorId1 = "";
		String custsimpnm = "";
		String instrepcode = "";

		if (jObject != null) {
			custnos = category.getString("custnos");
			businessTp = category.getString("businessTp");
			companyTp = category.getString("companyTp");
			regionTp = category.getString("regionTp");
			fxqTp = category.getString("fxqTp");
			fxqDesc = category.getString("fxqDesc");
			permissionId1 = category.getString("permissionId");
			operatorId1 = category.getString("operatorId");

			custsimpnm = category.getString("custsimpnm");
			instrepcode = category.getString("instrepcode");
		}
		OpenAccountDto categoryDto = new OpenAccountDto();
		String serialNoString = Sequences.getPK();// 流水号
		if (custnos != null && custnos.length() > 0) {
			categoryDto.setCustno(custnos);
			categoryDto.setBusinesstp(businessTp);
			categoryDto.setCompanytp(companyTp);
			categoryDto.setRegiontp(regionTp);
			categoryDto.setAmlrisktype(fxqTp);
			categoryDto.setFxqremark(fxqDesc);

			categoryDto.setSerialno(serialNoString);

			categoryDto.setPermissionId(permissionId1);
			categoryDto.setOperatorId(operatorId1);

			categoryDto.setAcctabbr(custsimpnm);
			categoryDto.setInstrepcode(instrepcode);
		}

		try {
        	modifyCustInfoDao.modifyCategoryInfo(categoryDto);
        	if (!"0000".equals(categoryDto.getErrcode())) {
				errcode = categoryDto.getErrcode();
				errmsg += "，"+accoDto.getErrmsg();
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("分类信息修改异常：", e);
			dto.setErrcode("9999");
			dto.setErrmsg("综合资料信息修改异常："+ e);
			return dto;
		}
		dto.setErrcode(errcode);
		dto.setErrmsg(errmsg);
		return dto;
	}
}
