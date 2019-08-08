package com.cmwa.ecc.business.service.impl.accountManger;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmwa.ec.base.util.Sequences;
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ecc.business.commonVo.ParameterDto;
import com.cmwa.ecc.business.dao.accountManger.CommonQueryDao;
import com.cmwa.ecc.business.dao.accountManger.OpenAccountDao;
import com.cmwa.ecc.business.dao.widget.WidgetDao;
import com.cmwa.ecc.business.entity.accountManger.AccountDto;
import com.cmwa.ecc.business.entity.accountManger.AddressDto;
import com.cmwa.ecc.business.entity.accountManger.AppR1BlotterDto;
import com.cmwa.ecc.business.entity.accountManger.BakCustDto;
import com.cmwa.ecc.business.entity.accountManger.CbpOffLineOTOInvestDto;
import com.cmwa.ecc.business.entity.accountManger.ContactInfoDto;
import com.cmwa.ecc.business.entity.accountManger.FundAcctDto;
import com.cmwa.ecc.business.entity.accountManger.FundBalanceDto;
import com.cmwa.ecc.business.entity.accountManger.IDInfoDto;
import com.cmwa.ecc.business.entity.accountManger.InvestorInfoDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountExcelDto;
import com.cmwa.ecc.business.entity.accountManger.TaInfoDto;
import com.cmwa.ecc.business.entity.accountManger.TradeAccDto;
import com.cmwa.ecc.business.entity.accountManger.UserTaxInfoDto;
import com.cmwa.ecc.business.service.accountManger.OpenAccountService;
import com.cmwa.ecc.business.utils.Encrypt;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.PageUtil;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 账户开户接口类
 * @author ex-chenbq
 */
@Service
public class OpenAccountServiceImpl implements OpenAccountService {

	private Logger logger = Logger.getLogger(OpenAccountServiceImpl.class.getName());
	
	@Autowired
	private CommonQueryDao commonQueryDao;
	
	@Autowired
	private OpenAccountDao openAccountDao;
	
	@Autowired
	private WidgetDao widgetDao;
	
	@Override
	public String getMaxFileNo() {
		String fileNo = "";
		try {
			fileNo = commonQueryDao.getMaxFileNo();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询最大的文件编号异常：",e);
		}
		return fileNo;
	}

	@Override
	public String getMaxKeepAddress() {
		String address = "";
		try {
			address = commonQueryDao.getMaxKeepAddress();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询最大的存档位置异常：",e);
		}
		return address;
	}

	@Override
	public int getFileNo(String fileNo) {
		int fileCount = 0;
		try {
			fileCount = commonQueryDao.getFileNo(fileNo);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("根据文件编号查询文件数量异常：",e);
		}
		return fileCount;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<BakCustDto> queryBakCust(SearchParam param) {
		List<BakCustDto> rList = new ArrayList<BakCustDto>();
		try {
			commonQueryDao.queryBakCust(param);
			if(param.getSp().get("errcode").equals("0000")){
				List<BakCustDto> bakCustList = (List<BakCustDto>) param.getSp().get("bakCustList");
				List<ContactInfoDto> contactList = (List<ContactInfoDto>) param.getSp().get("contactList");
				rList = bakCustList;
				if(null != bakCustList && bakCustList.size() == 1){
					BakCustDto bdto = (BakCustDto) bakCustList.get(0);
					if(null != contactList && contactList.size() > 0){
						bakCustList.remove(0);
						List<IDInfoDto> oidlist = new ArrayList<IDInfoDto>();
						List<ContactInfoDto> ocontactlist = new ArrayList<ContactInfoDto>();
						for (int i = 0; i < contactList.size(); i++) {
							ContactInfoDto dto = contactList.get(i);
							if(dto.getInfoflag().equals("I")){
								IDInfoDto idinfo = new IDInfoDto();
								idinfo.setIdtp(dto.getIdtp());
								idinfo.setIdno(dto.getIdno());
								idinfo.setIdvalidate(dto.getIdvalidate());
								if(idinfo.getIdvalidate() != null && idinfo.getIdvalidate().length()>=8){
									StringBuffer sb = new StringBuffer();
									sb.append(idinfo.getIdvalidate().substring(0,4)+"-"+idinfo.getIdvalidate().substring(4,6)+"-"+idinfo.getIdvalidate().substring(6,8));
									idinfo.setIdvalidate(sb.toString());
								}
								oidlist.add(idinfo);
							}else{
								if("0".equals(dto.getMainbrokerflag())){
									if(dto.getContvalidate() != null && dto.getContvalidate().length()>=8){
										StringBuffer sb = new StringBuffer();
										sb.append(dto.getContvalidate().substring(0,4)+"-"+dto.getContvalidate().substring(4,6)+"-"+dto.getContvalidate().substring(6,8));
										dto.setContvalidate(sb.toString());
									}
									ocontactlist.add(dto);
								}
							}
						}
						bdto.setOidlist(oidlist);
						bdto.setOcontactlist(ocontactlist);
						rList.add(bdto);
					}
				}
				
				if(null != rList){
					for (int i = 0; i < rList.size(); i++) {
						BakCustDto dto = rList.get(i);
						if(dto.getContvalidate() != null && dto.getContvalidate().length()>=8){
					    	StringBuffer contvalidate = new StringBuffer();
					      	contvalidate.append(dto.getContvalidate().substring(0,4)+"-"+dto.getContvalidate().substring(4,6)+"-"+dto.getContvalidate().substring(6,8));
					      	dto.setContvalidate(contvalidate.toString());
					    }
					    if(dto.getInstrepvalidate() != null && dto.getInstrepvalidate().length()>=8){
					      	StringBuffer ivalidate = new StringBuffer();
					      	ivalidate.append(dto.getInstrepvalidate().substring(0,4)+"-"+dto.getInstrepvalidate().substring(4,6)+"-"+dto.getInstrepvalidate().substring(6,8));
					      	dto.setInstrepvalidate(ivalidate.toString());
					    }
					    if(dto.getPrincipalvalidt() != null && dto.getPrincipalvalidt().length()>=8){
					      	StringBuffer pvalidate = new StringBuffer();
					      	pvalidate.append(dto.getPrincipalvalidt().substring(0,4)+"-"+dto.getPrincipalvalidt().substring(4,6)+"-"+dto.getPrincipalvalidt().substring(6,8));
					      	dto.setPrincipalvalidt(pvalidate.toString()); 
					    }
					    SearchParam seatidtpSp = new SearchParam();
						seatidtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
						seatidtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
						seatidtpSp.getSp().put("pmco", dto.getIdtp());
						List<Map<String, Object>> result = widgetDao.queryMatchApkindList(seatidtpSp);
					    dto.setIdtpnm(result.get(0).get("PMNM")+"");
					    SearchParam contidSp = new SearchParam();
					    contidSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
					    contidSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
					    contidSp.getSp().put("pmco", dto.getContidtp());
					    List<Map<String, Object>> contidResult = widgetDao.queryMatchApkindList(contidSp);
					    dto.setContidnm(contidResult.get(0).get("PMNM")+"");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询备案客户信息异常：",e);
		}
		return rList;
	}

	@SuppressWarnings("unchecked")
	@Override
	public OpenAccountDto queryAccoByfundAcc(SearchParam param) {
		OpenAccountDto dto = new OpenAccountDto();
		try {
			param.getSp().put("custno", "");
			commonQueryDao.queryAccoByfundAcc(param);
			dto.setErrcode((String) param.getSp().get("errcode"));
			dto.setErrmsg((String) param.getSp().get("errmsg"));
			if(param.getSp().get("errcode").equals("0000")){
				//客户基本信息
				List<OpenAccountDto> custInfo = (List<OpenAccountDto>) param.getSp().get("custInfo");
				//客户基本扩展信息
				List<OpenAccountDto> custInfoexList = (List<OpenAccountDto>) param.getSp().get("custInfoex");
				//客户经办人信息
				List<ContactInfoDto> contInfo = (List<ContactInfoDto>) param.getSp().get("contInfo");
				//客户资料信息
				List<OpenAccountDto> docInfo = (List<OpenAccountDto>) param.getSp().get("docInfo");
				if(null != custInfo && custInfo.size() > 0){
					dto = custInfo.get(0);
					if(null != custInfoexList && custInfoexList.size() > 0){
						OpenAccountDto custInfoex = custInfoexList.get(0);
						dto = combineSydwCore(dto, custInfoex);
						dto.setIdvalidate(formatDate(custInfoex.getIdvalidate()));
						dto.setInstrepvalidate(formatDate(custInfoex.getInstrepvalidate()));
						dto.setPrincipalvalidt(formatDate(custInfoex.getPrincipalvalidt()));
						dto.setHoldingvalidate(formatDate(custInfoex.getHoldingvalidate()));
					}
					if (contInfo != null && contInfo.size() > 0) {
						List<IDInfoDto> oidlist = new ArrayList<IDInfoDto>();
						List<ContactInfoDto> ocontlist = new ArrayList<ContactInfoDto>();
						for (int i = 0; i < contInfo.size(); i++) {
							ContactInfoDto contDto = new ContactInfoDto();
							IDInfoDto idDto = new IDInfoDto();
							ContactInfoDto infoDto = contInfo.get(i);
							if ("I".equals(infoDto.getInfoflag())) {
								idDto.setIdtp(infoDto.getIdtp());
								idDto.setIdno(infoDto.getIdno());
								idDto.setIdvalidate(formatDate(infoDto.getIdvalidate()));
								oidlist.add(idDto);
							} else { // 主经办人
								if ("1".equals(infoDto.getMainbrokerflag()))
								{
									dto.setContactgrant(infoDto.getContactgrant());
									dto.setContact(infoDto.getContact());
									dto.setContactnation(infoDto.getContactnation());
									dto.setContidtp(infoDto.getContidtp());
									dto.setContidno(infoDto.getContidno());
									dto.setContvalidate(formatDate(infoDto.getContvalidate()));    
									dto.setContphone(infoDto.getContphone());
									dto.setContfax(infoDto.getContfax());
									dto.setContmobile(infoDto.getContmobile());
									dto.setContemail(infoDto.getContemail());
									dto.setContAddr(infoDto.getContAddr());
									dto.setContPostcode(infoDto.getContPostcode());
								} else {// 其他经办人
									contDto.setContactgrant(infoDto.getContactgrant());
									contDto.setContact(infoDto.getContact());
									contDto.setContactnation(infoDto.getContactnation());
									contDto.setContidtp(infoDto.getContidtp());
									contDto.setContidno(infoDto.getContidno());
									contDto.setContvalidate(formatDate(infoDto.getContvalidate()));
									contDto.setContphone(infoDto.getContphone());
									contDto.setContfax(infoDto.getContfax());
									contDto.setContmobile(infoDto.getContmobile());
									contDto.setContemail(infoDto.getContemail());
									contDto.setContAddr(infoDto.getContAddr());
									contDto.setContPostcode(infoDto.getContPostcode());
									ocontlist.add(contDto);
								}
							}
						}
						dto.setOcontactlist(ocontlist);
						dto.setOidlist(oidlist);
					}
					
					if (docInfo != null){
						List<OpenAccountDto> documentlist = new ArrayList<OpenAccountDto>();
						for (int i = 0; i < docInfo.size(); i++) {
							OpenAccountDto docDto = new OpenAccountDto();
							docDto = docInfo.get(i);
							if(i == 0){
								dto = combineSydwCore(dto, docDto);
							}
							OpenAccountDto documentDto = new OpenAccountDto();
							documentDto.setCustdocument(docDto.getCustdocument());//资料编号
							documentDto.setExistsflag(docDto.getExistsflag());//资料是否存在
							documentDto.setBusinesstp(docDto.getBusinesstp());//业务类型
							documentDto.setDocbusinesstp(docDto.getDocbusinesstp());
							documentlist.add(documentDto);
						}
						dto.setDocumentlist(documentlist);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			logger.error("查询开户信息异常：",e);
		}
		return dto;
	}
	
	
	/**
     * 	该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，那么sourceBean中的值会覆盖tagetBean重点的值
     * @param sourceBean    被提取的对象bean
     * @param targetBean    用于合并的对象bean
     * @return targetBean,合并后的对象
     */
    @SuppressWarnings("rawtypes")
	public OpenAccountDto combineSydwCore(OpenAccountDto sourceBean,OpenAccountDto targetBean){
        Class sourceBeanClass = sourceBean.getClass();
        Class targetBeanClass = targetBean.getClass();
        
        Field[] sourceFields = sourceBeanClass.getDeclaredFields();
        Field[] targetFields = targetBeanClass.getDeclaredFields();
        for(int i=0; i<sourceFields.length; i++){
            Field sourceField = sourceFields[i]; 
            Field targetField = targetFields[i];  
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            try {
                if( !(sourceField.get(sourceBean) == null) &&  !"serialVersionUID".equals(sourceField.getName().toString())){
                    targetField.set(targetBean,sourceField.get(sourceBean));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return targetBean;
    }
    
    /**
     * 		八位数字日期格式化成YYYY-MM-DD
     * @param date
     * @return
     */
    public String formatDate(String date){
    	String formatData = "";
    	if(!StringUtil.isEmpty(date) && date.length()>=8)
		{
			StringBuffer sb = new StringBuffer();
			sb.append(date.substring(0, 4)).append("-").append(date.substring(4, 6)).append("-").append(date.substring(6, 8));
			formatData = sb.toString();
		}
    	return formatData;
    }

	@Override
	public List<OpenAccountDto> queryWaspUserBankInfo(String accName) {
		List<OpenAccountDto> list = new ArrayList<OpenAccountDto>();
		try {
			list = commonQueryDao.queryWaspUserBankInfo(accName);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询综合业务平台银行信息异常：",e);
		}
		return list;
	}

	@Override
	public List<OpenAccountDto> queryOpenUserInfo(SearchParam param) {
		List<OpenAccountDto> list = new ArrayList<OpenAccountDto>();
		try {
			list = commonQueryDao.queryOpenUserInfo(param);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询客户信息异常：",e);
		}
		return list;
	}

	@Override
	public List<CbpOffLineOTOInvestDto> queryCbpOffLineOTOInvInfo(SearchParam param) {
		List<CbpOffLineOTOInvestDto> list = new ArrayList<CbpOffLineOTOInvestDto>();
		try {
			list = commonQueryDao.queryCbpOffLineOTOInvInfo(param);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询线下一对一 客户资料信息异常：",e);
		}
		return list;
	}

	@Override
	public OpenAccountDto openCustom(OpenAccountDto dto) {
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
			
            //开户时密码blowfish加密
            String originalPwd = "";
            if (dto != null) {
                originalPwd = dto.getPassword();
                if ((originalPwd != null) && (originalPwd.trim().length() != 0)) {
                    Encrypt encrypt = new Encrypt();
                    String encryptPassword = encrypt.passwordEncrypt(originalPwd);
                    dto.setPassword(encryptPassword);
                }
            }
            
            dto.setIdvalidate(StringUtil.isEmpty(dto.getIdvalidate()) ? "" : dto.getIdvalidate().replaceAll("-", "") );
            dto.setInstrepvalidate(StringUtil.isEmpty(dto.getInstrepvalidate()) ? "" : dto.getInstrepvalidate().replaceAll("-", "") );
            dto.setPrincipalvalidt(StringUtil.isEmpty(dto.getPrincipalvalidt()) ? "" : dto.getPrincipalvalidt().replaceAll("-", "") );
            dto.setHoldingvalidate(StringUtil.isEmpty(dto.getHoldingvalidate()) ? "" : dto.getHoldingvalidate().replaceAll("-", "") );
            
            
            try {
				openAccountDao.openCustom(dto);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("账户开户异常：", e);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("开户数据解析出错！");
		}
		
		if(dto.getErrcode().equals("0000")){
			String taxResidentData=dto.getTaxresident(); //税收居民数据
			//保存税收居民信息到数据库
			if(null != taxResidentData && !taxResidentData.equals("")){
				saveUserTaxInfo(dto.getCustno(), dto, taxResidentData);
			}else{
				deleteTaxInfo(dto.getCustno());
			}
		}
		return dto;
	}
	
	@Override
	public void saveUserTaxInfo(String custNo, OpenAccountDto dto , String taxResidentData){
		UserTaxInfoDto userTaxInfoDto = new UserTaxInfoDto();
		JSONArray jsonArray = new JSONArray();
		try{
			jsonArray = JSONArray.parseArray(taxResidentData);
			deleteTaxInfo(custNo);
			for (int i = 0; i < jsonArray.size(); i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				userTaxInfoDto = new UserTaxInfoDto();
				userTaxInfoDto = (UserTaxInfoDto)JSONObject.toJavaObject(jsonObj, UserTaxInfoDto.class);
				userTaxInfoDto.setCustNo(custNo);
				String birthdate2 = userTaxInfoDto.getBirthDate2();
	            if(null != birthdate2){
	            	birthdate2 = birthdate2.replaceAll("-", "");
	            }
	            userTaxInfoDto.setBirthDate2(birthdate2);
				openAccountDao.createUserTaxInfo(userTaxInfoDto);
				saveAppR1BlotterInfo(custNo,dto.getTaxType(),userTaxInfoDto);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	
	//备份并删除税收信息
	
	@Override
	public void deleteTaxInfo(String custno){
		//备份税收居民信息至历史表
		openAccountDao.createUserTaxInfoByBackUp(custno);
		//删除税收居民信息
		openAccountDao.delUserTaxInfo(custno);
		//删除涉税信息接口业务表数据
		openAccountDao.delAppR1BlotterInfo(custno);
	}
	
	/**
	 * 税收居民类型保存到非居民涉税信息申请流水业务表
	 * @param cmfUserId
	 * @param taxResidentType
	 * @param userTaxInfoDto
	 */
	public void saveAppR1BlotterInfo(String custno,String taxResidentType,UserTaxInfoDto userTaxInfoDto){
		AppR1BlotterDto appR1BlotterDto = new AppR1BlotterDto();
			appR1BlotterDto = openAccountDao.queryUserTaxInfo(custno);
			if(null != appR1BlotterDto){
				if(taxResidentType.equals("2")){
					appR1BlotterDto.setNonresiflag("1");
				}else if(taxResidentType.equals("3")){
					appR1BlotterDto.setNonresiflag("2");
				}else{
					appR1BlotterDto.setNonresiflag("0");
				}
				appR1BlotterDto.setAppsheetserialno(Sequences.getPK());
				appR1BlotterDto.setEnglishfamliyname2(userTaxInfoDto.getFirstName());
				appR1BlotterDto.setEnglishfirstname2(userTaxInfoDto.getEnglishName());
				appR1BlotterDto.setEnglishname(userTaxInfoDto.getEnglishName());
				appR1BlotterDto.setSex(userTaxInfoDto.getSex());
				String birthDay = userTaxInfoDto.getBirthDate();
				if(null != birthDay && !birthDay.equals("")){
					birthDay = birthDay.replaceAll("-", "");
				}
				appR1BlotterDto.setBirthdate(birthDay);
				String taxBirthNation = userTaxInfoDto.getTaxBirthNation();
				String taxBirthRegion = "";
				if(taxBirthNation.equals("1")){
					taxBirthRegion = "156";
				}else if(taxBirthNation.equals("2")){
					taxBirthRegion = "156-2";
				}else if(taxBirthNation.equals("3")){
					taxBirthRegion = "156-3";
				}else if(taxBirthNation.equals("4")){
					taxBirthRegion = "156-4";
				}else if(taxBirthNation.equals("5")){
					taxBirthRegion = userTaxInfoDto.getTaxBirthRegion();
				}
				if(!StringUtil.isEmpty(taxBirthRegion)){
					ParameterDto parameterDto = widgetDao.getParameterByPmstPmkyPmco("DS", "DS_NATION_CODE", taxBirthRegion);
					if(null != parameterDto){
						//出生地国家
						appR1BlotterDto.setBirthcountry(parameterDto.getPmv1());
					}
				}
		       
				
				//现居国家
				if(!StringUtil.isEmpty(userTaxInfoDto.getTaxResideRegion())){
					ParameterDto parameterDto2 = widgetDao.getParameterByPmstPmkyPmco("DS", "DS_NATION_CODE", userTaxInfoDto.getTaxResideRegion());
					if(null != parameterDto2){
						//出生地国家
						appR1BlotterDto.setLivingcountry(parameterDto2.getPmv1());
					}
				}
				
				//现居地址
				appR1BlotterDto.setLivingaddress(userTaxInfoDto.getTaxResideAddress());
				//现居英文地址
				appR1BlotterDto.setLivingaddress3(userTaxInfoDto.getTaxResideAddressEnglish());
				
				if(!StringUtil.isEmpty(userTaxInfoDto.getTaxArea())){
					ParameterDto parameterDto3 = widgetDao.getParameterByPmstPmkyPmco("DS", "DS_NATION_CODE", userTaxInfoDto.getTaxArea());
					if(null != parameterDto3){
						//税收居民国
						appR1BlotterDto.setTaxcountry(parameterDto3.getPmv1());
					}
				}
				//纳税人识别号
				appR1BlotterDto.setTaxid(userTaxInfoDto.getTaxPayerCode());
				//无识别号原因
				appR1BlotterDto.setSpecification(userTaxInfoDto.getTaxNotCodeCause().equals("1")?"0":userTaxInfoDto.getTaxnotGetCause());
				
				//调查规则  0：新开账户 1：存量账户
				String surveymethodVal = appR1BlotterDto.getOriginalappdate();
				String surveymethod  = "";
				try {
					if(Integer.parseInt(surveymethodVal) > Integer.parseInt("20170701")){
						surveymethod   = "0";
					}else{
						surveymethod = "1";
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				//控制人信息
				
				//消极非金融机构标识
				appR1BlotterDto.setPassivenonfinflag(userTaxInfoDto.getPassivenonfinflag());
				//存在非居民控制人标识
				appR1BlotterDto.setHavenonresconflag(userTaxInfoDto.getHavenonresconflag());
				
				//控制人中文姓名
				appR1BlotterDto.setChinesename2(userTaxInfoDto.getChineseName2());
				//控制人英文姓
				appR1BlotterDto.setEnglishfamliyname3(userTaxInfoDto.getEnglishFamliyName3());
				//控制人英文名
				appR1BlotterDto.setEnglishfirstname3(userTaxInfoDto.getEnglishFirstName3());
				//控制人类型
				appR1BlotterDto.setControllertype(userTaxInfoDto.getControllerType());
				//控制人非居民标识
				appR1BlotterDto.setConnonresiflag(userTaxInfoDto.getHavenonresconflag());
				//控制人持股比例
				appR1BlotterDto.setConshareratio(userTaxInfoDto.getConShareRatio());
				//控制人现居国家
				
				if(!StringUtil.isEmpty(userTaxInfoDto.getLivingCountry2())){
					ParameterDto parameterDto4 = widgetDao.getParameterByPmstPmkyPmco("DS", "DS_NATION_CODE", userTaxInfoDto.getLivingCountry2());
					if(null != parameterDto4){
						appR1BlotterDto.setLivingcountry2(parameterDto4.getPmv1());
					}
				}
				//控制人现居地址
				appR1BlotterDto.setLivingaddress5(userTaxInfoDto.getLivingAddress5());
				//控制人现居地址（英文地址）
				appR1BlotterDto.setLivingaddress7(userTaxInfoDto.getLivingAddress7());
				//控制人国籍
				if(!StringUtil.isEmpty(userTaxInfoDto.getRegRegionCode2())){
					ParameterDto parameterDto5 = widgetDao.getParameterByPmstPmkyPmco("DS", "DS_NATION_CODE", userTaxInfoDto.getRegRegionCode2());
					if(null != parameterDto5){
						appR1BlotterDto.setRegregioncode2(parameterDto5.getPmv1());
					}
				}
				//控制人出生日期
				String birthDate2 = userTaxInfoDto.getBirthDate2();
				if(null != birthDate2){
					birthDate2 = birthDate2.replaceAll("-", "");
				}
				appR1BlotterDto.setBirthdate2(birthDate2);
				//控制人出生国家
				
				if(!StringUtil.isEmpty(userTaxInfoDto.getBirthCountry2())){
					ParameterDto parameterDto6 = widgetDao.getParameterByPmstPmkyPmco("DS", "DS_NATION_CODE", userTaxInfoDto.getBirthCountry2());
					if(null != parameterDto6){
						appR1BlotterDto.setBirthcountry2(parameterDto6.getPmv1());
					}
				}
				//控制人出生城市
				appR1BlotterDto.setBirthcity2(userTaxInfoDto.getBirthCity2());
				//控制人税收居民国
				
				if(!StringUtil.isEmpty(userTaxInfoDto.getTaxCountry2())){
					ParameterDto parameterDto7 = widgetDao.getParameterByPmstPmkyPmco("DS", "DS_NATION_CODE", userTaxInfoDto.getTaxCountry2());
					if(null != parameterDto7){
						appR1BlotterDto.setTaxcountry2(parameterDto7.getPmv1());
					}
				}
				//控制人纳税人识别号。若有则必填
				appR1BlotterDto.setTaxid2(userTaxInfoDto.getTaxID2());
				//若有控制人但未提供控制人纳税识别号则此字段必填。填写‘0’表示‘控制人居民国不发放纳税人识别号’；否则填写无法提供纳税识别号码原因。
				String specificationCode = userTaxInfoDto.getSpecificationCode();
				if(null == specificationCode){
					specificationCode = "";
				}
				appR1BlotterDto.setSpecification2(specificationCode.equals("1")?"0":userTaxInfoDto.getSpecification2());
				appR1BlotterDto.setSurveymethod(surveymethod);
				appR1BlotterDto.setCustno(custno);
				appR1BlotterDto.setAddresstype("1");
				openAccountDao.insertAppR1BlotterInfo(appR1BlotterDto);
		}
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
	
	/**
	 * 开户Json字符串数据转换成实体
	 * @param str
	 * @return
	 */
	@Override
	public OpenAccountDto convertBean(String str){
		OpenAccountDto dto = new OpenAccountDto();
		JSONObject jsonObject = null;
		String custtp = "";
		try {
			jsonObject = JSONObject.parseObject(str);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("开户数据信息转换异常");
			return dto;
		}
		
		if(null != jsonObject ){
			String serialNo = Sequences.getPK();//流水号    
			dto.setSerialno(serialNo);
			custtp = jsonObject.getString("custTp");
			dto.setInvtp(custtp);
			dto.setInvprtp(jsonObject.getString("invprtype"));
			dto.setPermissionId(jsonObject.getString("permissionId"));
			dto.setOperatorId(jsonObject.getString("operatorId"));
			dto.setTrustType(jsonObject.getString("trustTp"));
			dto.setTano(jsonObject.getString("tano"));

			dto.setOpentype(jsonObject.getString("opentype"));
			dto.setFundacc(jsonObject.getString("fundacc"));
			dto.setTaxType(jsonObject.getString("taxType"));
			dto.setTaxTypeDecl(jsonObject.getString("taxTypeDecl"));

			// 基本->证件信息
			dto.setInvnm(jsonObject.getString("invNm"));
			dto.setIdtp(jsonObject.getString("invIdtp"));
			dto.setIdno(jsonObject.getString("invIdno"));
			dto.setIdvalidate(jsonObject.getString("invIdValidate"));
			dto.setAttr1(jsonObject.getString("offInvSerialno"));

			// 基本->银行信息
			dto.setBnkNo(jsonObject.getString("bankno")); // 银行编号
			dto.setOpenName(jsonObject.getString("openname")); // 开户银行名称
			dto.setOpenAddr(jsonObject.getString("openlocation")); // 开户银行地址
			dto.setOpenBankCity(jsonObject.getString("openbankcity"));// 开户银行地址（市）
			dto.setBankAccoNm(jsonObject.getString("bankacconm")); // 银行账户名称
			dto.setBankAcco(jsonObject.getString("bankacco")); // 银行账号

			// 基本->其他信息
			dto.setPostcode(jsonObject.getString("postcode")); // 地址
			dto.setAddr(jsonObject.getString("addr")); // 邮编
			dto.setShsecacc(jsonObject.getString("shsecacc")); // 上交所股东代码
			dto.setSzsecacc(jsonObject.getString("szsecacc")); // 深交所股东代码

			// 分类信息
			dto.setCustabbrcode(jsonObject.getString("custsimpnm")); // 客户简称
			dto.setInstrepcode(jsonObject.getString("instrepcode")); // 法人代码
			dto.setBusinesstp(jsonObject.getString("businesstp")); // 业务类型
			dto.setCompanytp(jsonObject.getString("corptype")); // 公司类型
			dto.setRegiontp(jsonObject.getString("regioncode")); // 地域代码
			dto.setAmlrisktype(jsonObject.getString("fxqtype")); // 反洗钱风险等级
			dto.setFxqremark(jsonObject.getString("fxqdesc")); // 反洗钱备注

		   //资料信息      
		   dto.setDocbusinesstp(jsonObject.getString("docbusinesstp")); //资料业务类型   
		   dto.setIfalldocument(jsonObject.getString("isalldoc"));             //资料是否齐全  
		   dto.setIfOriginal(jsonObject.getString("iforiginal"));       //资料是否原件
		   dto.setIfsaved(jsonObject.getString("ifsaved"));             //资料是否归档
		   dto.setIsupload(jsonObject.getString("isupload"));			//是否上传附件
		   dto.setCustdocument(jsonObject.getString("document"));       //资料名称列表
		   dto.setRemarkinfo(jsonObject.getString("remarkinfo"));	//资料信息备注
		   dto.setSpecriskLevel(jsonObject.getString("specriskLevel")); //特殊用户风险等级
		   //机构客户
			if ("0".equals(custtp)) {
				// 基本-->其他信息
				dto.setTel(jsonObject.getString("orgInvOfficeTel")); // 电话
				dto.setFax(jsonObject.getString("orgInvFax")); // 传真
				dto.setOrganType(jsonObject.getString("organType")); // 机构类型
				System.out.println("openccountAction::tel:" + dto.getTel());
				// 基本->法人信息
				dto.setInstrepnm(jsonObject.getString("orgInstrepnm"));
				dto.setInstrepnation(jsonObject
						.getString("orgInstrepnation"));
				dto.setInstrepidtp(jsonObject.getString("orgInstrepidtp"));
				dto.setInstrepidno(jsonObject.getString("orgInstrepidno"));
				dto.setInstrepvalidate(jsonObject.getString("orgInstrepidvalidate"));

				// 基本->机构负责人信息
				dto.setPrincipalname(jsonObject.getString("orgPrincipalname"));
				dto.setPrincipalnation(jsonObject.getString("orgPrincipalnation"));
				dto.setPrincipalidtp(jsonObject.getString("orgPrincipalidtp"));
				dto.setPrincipalidno(jsonObject.getString("orgPrincipalidno"));
				dto.setPrincipalvalidt(jsonObject.getString("orgPrincipalidvalidate"));

				// 基本->经办人员信息
				dto.setContactgrant(jsonObject.getString("orgContactright"));
				dto.setContact(jsonObject.getString("orgContnm"));
				dto.setContactnation(jsonObject.getString("orgContnation"));
				dto.setContidtp(jsonObject.getString("orgContidtp"));
				dto.setContidno(jsonObject.getString("orgContidno"));
				dto.setContvalidate(jsonObject.getString("orgContidvalidate"));
				dto.setContphone(jsonObject.getString("orgContphone"));
				dto.setContfax(jsonObject.getString("orgContfax"));
				dto.setContmobile(jsonObject.getString("orgContmobile"));
				dto.setContemail(jsonObject.getString("orgContemail"));
				dto.setContAddr(jsonObject.getString("orgContAddr"));
				dto.setContPostcode(jsonObject.getString("orgContPostcode"));

				// 附加->其他信息
				dto.setHoldingname(jsonObject.getString("orgHoldingname"));
				dto.setHoldingidtp(jsonObject.getString("orgHoldingidtp"));
				dto.setHoldingidno(jsonObject.getString("orgHoldingidno"));
				dto.setHoldingvalidate(jsonObject.getString("orgHoldingidvalidate"));
				dto.setBeneficiary(jsonObject.getString("orgBeneficiarynm"));
				dto.setCustrisklevl(jsonObject.getString("orgRiskLevel"));
				// 附加->其他证件信息
				dto.setOidtp(jsonObject.getString("oidtpStr"));
				dto.setOidno(jsonObject.getString("oidnoStr"));
				dto.setOidvalidate(jsonObject.getString("oidvalidateStr"));

				// 附加->其他经办人信息
				dto.setOcontactgrant(jsonObject.getString("oOrgContactright"));
				dto.setOcontact(jsonObject.getString("oOrgContact"));
				dto.setOcontactnation(jsonObject.getString("oOrgContactnation"));
				dto.setOcontidtp(jsonObject.getString("oOrgContactidtp"));
				dto.setOcontidno(jsonObject.getString("oOrgContactidno"));
				dto.setOcontvalidate(jsonObject.getString("oOrgContactidvalidate"));
				dto.setOcontphone(jsonObject.getString("oOrgContacttel"));
				dto.setOcontfax(jsonObject.getString("oOrgContactfax"));
				dto.setOcontmobile(jsonObject.getString("oOrgContactmobile"));
				dto.setOcontemail(jsonObject.getString("oOrgContactemail"));
				dto.setOcontAddr(jsonObject.getString("oOrgContactAddr"));
				dto.setOcontPostcode(jsonObject.getString("oOrgContactPostcode"));
				// 投资者适当性信息
				dto.setInvestProInstType(jsonObject.getString("investProInstType"));
				dto.setInvestProInstSecond(jsonObject.getString("investProInstSecond"));
				dto.setOneYearEndNetAsset(jsonObject.getString("oneYearEndNetAsset"));
				dto.setOneYearEndFinAsset(jsonObject.getString("oneYearEndFinAsset"));
				dto.setInvestExperience(jsonObject.getString("investExperience"));
				dto.setNegativeNotFinaInst(jsonObject.getString("negativeNotFinaInst"));
				dto.setControlPerTaxDecl(jsonObject.getString("controlPerTaxDecl"));
			} else {
				// 基本->其他信息
				dto.setTel(jsonObject.getString("pslInvOfficeTel"));
				dto.setHousetel(jsonObject.getString("pslInvHomeTel"));
				dto.setMobile(jsonObject.getString("pslInvMobile"));
				dto.setFax(jsonObject.getString("pslInvFax"));
				dto.setFaxdelegate(jsonObject.getString("plsFaxDelegate"));
				dto.setEmail(jsonObject.getString("pslInvEmail"));

				// 附加信息->其他
				dto.setSex(jsonObject.getString("plsSex"));
				dto.setNationalitycode(jsonObject.getString("plsInvNation"));
				dto.setEdlevel(jsonObject.getString("plsInvEducation"));
				dto.setVoccode(jsonObject.getString("plsInvJob"));
				dto.setIncome(jsonObject.getString("plsInvIncome"));
				dto.setCustrisklevl(jsonObject.getString("plsInvRisk"));
				// 投资者适当性信息
				dto.setThreeAnnualIncome(jsonObject.getString("threeAnnualIncome"));
				dto.setFinancialAsset(jsonObject.getString("financialAsset"));
				dto.setIndInvExperience(jsonObject.getString("indInvExperience"));
				dto.setRelatedWorkExp(jsonObject.getString("relatedWorkExp"));
				dto.setFinProfessions(jsonObject.getString("finProfessions"));
			}

			dto.setIsscan(jsonObject.getString("isscan"));
			dto.setKeepaddress(jsonObject.getString("keepaddress"));
			dto.setSalesaccmanager(jsonObject.getString("salesaccmanager"));
			dto.setFileno(jsonObject.getString("fileno"));
			dto.setTaxresident(jsonObject.getString("taxresident"));
			dto.setErrcode("0000");
		}
		return dto;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Page<FundBalanceDto> queryFundBalance(SearchParam param) {
		List<FundBalanceDto> list = new ArrayList<FundBalanceDto>();
		PageUtil<FundBalanceDto> page = new PageUtil<FundBalanceDto>();
		List<FundBalanceDto> resultList = new ArrayList<FundBalanceDto>();
		try {
			commonQueryDao.queryFundBalance(param);
			if(param.getSp().get("errcode").equals("0000")){
				list = (List<FundBalanceDto>) param.getSp().get("fundBalanceList");
			}
			resultList = page.getPageList(param, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("基金余额查询异常：",e);
		}
		return Page.create(resultList, param.getStart(), param.getLimit(), list.size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public OpenAccountDto tradeAccoQuery(SearchParam param) {
		OpenAccountDto accountDto = new OpenAccountDto();
		try {
			commonQueryDao.tradeaccoQry(param);
			if(param.getSp().get("errcode").equals("0000")){
				
				List<OpenAccountDto> custInfoList = (List<OpenAccountDto>) param.getSp().get("custInfoList");
				List<OpenAccountDto> custInfoexList = (List<OpenAccountDto>) param.getSp().get("custInfoexList");
				List<FundAcctDto> fundInfoList = (List<FundAcctDto>) param.getSp().get("fundInfoList");
				List<TradeAccDto> tradeInfoList = (List<TradeAccDto>) param.getSp().get("tradeInfoList");
				List<ContactInfoDto> contInfoList = (List<ContactInfoDto>) param.getSp().get("contInfoList");
				List<OpenAccountDto> docInfoList = (List<OpenAccountDto>) param.getSp().get("docInfoList");
				List<OpenAccountDto> categoryInfoList = (List<OpenAccountDto>) param.getSp().get("categoryInfoList");
				if (null != custInfoList && custInfoList.size() > 0) {
					accountDto = custInfoList.get(0);
				}
				if(null != custInfoexList && custInfoexList.size() > 0){
					accountDto = combineSydwCore(accountDto, custInfoexList.get(0));
					accountDto.setInstrepvalidate(formatDate(accountDto.getInstrepvalidate()));
					accountDto.setPrincipalvalidt(formatDate(accountDto.getPrincipalvalidt()));
					accountDto.setIdvalidate(formatDate(accountDto.getIdvalidate()));
					accountDto.setHoldingvalidate(formatDate(accountDto.getHoldingvalidate()));
				}
				if (null != fundInfoList && fundInfoList.size() > 0) {
					List<FundAcctDto> fundList = new ArrayList<FundAcctDto>();
					for (int i = 0; i < fundInfoList.size(); i++) {
						FundAcctDto fundAcctDto = fundInfoList.get(i);
						fundList.add(fundAcctDto);
					}
					if (fundList.size() == 1) 
					{
						FundAcctDto fundDto = (FundAcctDto) fundList.get(0);
						accountDto.setFundacct(fundDto.getFundacco());
						accountDto.setTano(fundDto.getTano());						
					}
					accountDto.setFundAccoList(fundList);
				}
				if (null != tradeInfoList && tradeInfoList.size() > 0) {
					List<TradeAccDto> tradeList = new ArrayList<TradeAccDto>();
					for (int i = 0; i < tradeInfoList.size(); i++) {
						ParameterDto openAddrDto = widgetDao.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_DS_PROVINCE, tradeInfoList.get(i).getOpenAddr());
						if(null != openAddrDto){
							tradeInfoList.get(i).setOpenAddrNm(openAddrDto.getPmnm());
						}
						ParameterDto cityDto = widgetDao.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_DS_CITYCODE, tradeInfoList.get(i).getOpenBankCity());
						if(null != cityDto){
							tradeInfoList.get(i).setOpenBankCityNm(cityDto.getPmnm());
						}
						tradeList.add(tradeInfoList.get(i));
					}
					if (tradeList.size() >= 1) 
					{
						TradeAccDto tradeDto = (TradeAccDto) tradeList.get(0);
						accountDto.setTradeacco(tradeDto.getTradeAcco());
						accountDto.setBnkNo(tradeDto.getBankNo());
						accountDto.setBankAccoNm(tradeDto.getBankAcNm());
						accountDto.setBankAcco(tradeDto.getBankAcco());
						accountDto.setOpenAddr(tradeDto.getOpenAddr());
						accountDto.setOpenBankCity(tradeDto.getOpenBankCity());
						accountDto.setOpenName(tradeDto.getOpenName());
					}
					accountDto.setTradeAccoList(tradeList);
				}
				if (null != contInfoList && contInfoList.size() > 0) {
					List<ContactInfoDto> ocontlist = new ArrayList<ContactInfoDto>();
					List<IDInfoDto> oidlist = new ArrayList<IDInfoDto>();
					for (int i = 0; i < contInfoList.size(); i++) {
						ContactInfoDto continfo = contInfoList.get(i);
						if (!StringUtil.isEmpty(continfo.getContidtp()) ) {
							ParameterDto dto = widgetDao.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_SYSTEM,ParameterConstant.PARAM_PMKY_IDTP, continfo.getContidtp());
							continfo.setContidtpnm(dto.getPmnm());
						}
						//经办人信息
						if("B".equals(continfo.getInfoflag()))
						{
							if("1".equals(continfo.getMainbrokerflag())){//主经办人
								accountDto.setContactgrant(continfo.getContactgrant());
								accountDto.setContact(continfo.getContact());
								accountDto.setContactnation(continfo.getContactnation());
								accountDto.setContidtp(continfo.getContidtp());
								accountDto.setContidtpnm(continfo.getContidtpnm());
								accountDto.setContidno(continfo.getContidno());
								accountDto.setContvalidate(formatDate(continfo.getContvalidate()));    
								accountDto.setContphone(continfo.getContphone());
								accountDto.setContfax(continfo.getContfax());
								accountDto.setContmobile(continfo.getContmobile());
								accountDto.setContemail(continfo.getContemail());
								accountDto.setContAddr(continfo.getContAddr());
								accountDto.setContPostcode(continfo.getContPostcode());
							} else {// 其他经办人
								continfo.setContvalidate(formatDate(continfo.getContvalidate()));    
								ocontlist.add(continfo);
							}
						}						
						else
						{//证件信息
							IDInfoDto idDto = new IDInfoDto();
							idDto.setIdtp(continfo.getIdtp());
							idDto.setIdno(continfo.getIdno());												
							idDto.setIdvalidate((formatDate(continfo.getIdvalidate())).equals("") ? null : formatDate(continfo.getIdvalidate()));    
							//if (!StringUtil.isEmpty(idDto.getIdtp()) && !StringUtil.isEmpty(idDto.getIdno()) && !StringUtil.isEmpty(idDto.getIdvalidate())) {
								oidlist.add(idDto);
							//}
						}
					}
					accountDto.setOcontactlist(ocontlist);
					accountDto.setOidlist(oidlist.size() > 0 ? oidlist : null);
					
					if (null != docInfoList && docInfoList.size() > 0) {
						List<OpenAccountDto> documentlist = new ArrayList<OpenAccountDto>();
						for (int i = 0; i < docInfoList.size(); i++) {
							OpenAccountDto docDto = new OpenAccountDto();
							if(i == 0){
								docDto = docInfoList.get(i);
								accountDto = combineSydwCore(accountDto, docDto);
							}else{
								docDto = docInfoList.get(i);
							}
							OpenAccountDto documentDto = new OpenAccountDto();
							documentDto.setCustdocument(docDto.getCustdocument());//资料编号
							documentDto.setExistsflag(docDto.getExistsflag());//资料是否存在
							documentDto.setBusinesstp(docDto.getDocbusinesstp());//业务类型
							documentlist.add(documentDto);
						}
						accountDto.setDocumentlist(documentlist);
					}
					
					//分类信息
					if (null != categoryInfoList && categoryInfoList.size() > 0) {
						OpenAccountDto categoryDto = categoryInfoList.get(0);
						accountDto = combineSydwCore(accountDto, categoryDto);
					}
				}
			}
			
			if(!StringUtil.isEmpty(accountDto.getIdtp())){
				ParameterDto idtpDto = widgetDao.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP, accountDto.getIdtp());
				accountDto.setIdtpnm(idtpDto.getPmnm());
			}
			
			if(!StringUtil.isEmpty(accountDto.getInvtp())){
				ParameterDto invtpDto = widgetDao.getParameterByPmstPmkyPmco(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_INVTP, accountDto.getInvtp());
				accountDto.setInvtpnm(invtpDto.getPmnm());
			}
			
			accountDto.setErrcode((String)param.getSp().get("errcode"));
			accountDto.setErrmsg((String)param.getSp().get("errmsg"));
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("客户信息查询异常：",e);
			accountDto.setErrcode("9999");
			accountDto.setErrmsg("客户信息查询异常");
		}
		return accountDto;
	}

	@Override
	public OpenAccountDto tradeAccountAdd(OpenAccountDto dto) {
		try {
			openAccountDao.tradeAccountAdd(dto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("增开交易账号：", e);
		}
		return dto;
	}

	@Override
	public String getCustnoByIdnoAndFundAcco(String idno, String fundacct) {
		String custNo = "";
		try {
			custNo = commonQueryDao.getCustnoByIdnoAndFundAcco(idno, fundacct);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("通过基金账号，证件号查询客户号异常：",e);
		}
		return custNo;
	}

	@Override
	public AccountDto getAccountBycustNo(SearchParam param) {
		AccountDto accountDto = new AccountDto();
		try {
			String custNo = commonQueryDao.getCustnoByIdnoAndFundAcco((String)param.getSp().get("idno"), (String)param.getSp().get("fundacct"));
			param.getSp().put("custno", custNo);
			commonQueryDao.getAccountBycustNo(param);
			String errcode = (String) param.getSp().get("errcode");
			if (errcode.equals("0000")) {
				InvestorInfoDto investorInfo = new InvestorInfoDto();
				AddressDto addrDto = new AddressDto();
				String invnm = (String) param.getSp().get("invnm");
				String idtp = (String) param.getSp().get("idtp");
				String idno = (String) param.getSp().get("idno");
				String mobileno = (String) param.getSp().get("mobileno");
				String hometel = (String) param.getSp().get("hometel");
				String faxno = (String) param.getSp().get("faxno");
				String email = (String) param.getSp().get("email");
				String postcode = (String) param.getSp().get("postcode");
				String addr = (String) param.getSp().get("addr");
				String provicecode = (String) param.getSp().get("provicecode");
				String cityname = (String) param.getSp().get("cityname");
				String birthday = (String) param.getSp().get("birthday");
				String marriage = (String) param.getSp().get("marriage");
				String voccode = (String) param.getSp().get("voccode");
				String income = (String) param.getSp().get("income");
				String edlevel = (String) param.getSp().get("edlevel");
				String exprience = (String) param.getSp().get("exprience");
				String idnoLimit = (String) param.getSp().get("idnoLimit"); // 证件有效期 by kouyd
				// 20090603
				String sex = (String) param.getSp().get("sex"); // 性别 by kouy 20090608
				String registertime = (String) param.getSp().get("registertime");
				String nation = (String) param.getSp().get("nation");

				addrDto.setName(invnm);
				addrDto.setProvince(provicecode);
				addrDto.setCity(cityname);
				addrDto.setPostcode(postcode);
				addrDto.setMobile(mobileno);
				addrDto.setFax(faxno);
				addrDto.setEmail(email);
				addrDto.setTel(hometel);
				addrDto.setAddr(addr);

				investorInfo.setInvnm(invnm);
				investorInfo.setBirthday(birthday);
				investorInfo.setMarriage(marriage);
				investorInfo.setVocation(voccode);
				investorInfo.setIncome(income);
				investorInfo.setEducation(edlevel);
				investorInfo.setInvest(exprience);
				investorInfo.setAddress(addrDto);
				investorInfo.setSex(sex);
				investorInfo.setIdnoLimit(idnoLimit);
				investorInfo.setNation(nation);

				accountDto.setCustno(custNo);
				accountDto.setInvtp("");
				accountDto.setInvnm(invnm);
				accountDto.setIdtp(idtp);
				accountDto.setIdno(idno);
				accountDto.setMobileno(mobileno);
				accountDto.setEmail(email);
				accountDto.setRegistertime(registertime);
				accountDto.setInvestorinfo(investorInfo);
				accountDto.setErrcode(errcode);
			}
			
			List<FundAcctDto> fundAcctDtos = commonQueryDao.getFundAccount(custNo);
			if (null != fundAcctDtos && fundAcctDtos.size() > 0) {
				FundAcctDto dto = (FundAcctDto)fundAcctDtos.get(0);
    			if(dto != null){
    				accountDto.addFundacco(dto);
    			}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("帐户基本信息查询异常：", e);
		}
		return accountDto;
	}

	@Override
	public List<TaInfoDto> queryTaInfo(String tano) {
		List<TaInfoDto> list = new ArrayList<TaInfoDto>();
		try {
			list = commonQueryDao.queryTaInfo(tano);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询Ta信息异常：" , e);
		}
		return list;
	}

	@Override
	public OpenAccountDto openFundAccount(OpenAccountDto dto) {
		try {
			openAccountDao.openFundAccount(dto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("增开基金账号异常：", e);
		}
		return dto;
	}

	@Override
	public OpenAccountDto destroyTradeacco(OpenAccountDto dto) {
		try {
			openAccountDao.destroyTradeacco(dto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("交易账号销户异常：", e);
		}
		return dto;
	}

	@Override
	public OpenAccountDto destroyFundacco(OpenAccountDto dto) {
		try {
			openAccountDao.destroyFundacco(dto);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("基金账号销户异常：", e);
		}
		return dto;
	}

	@Override
	public OpenAccountDto custModifyToBean(String str) {
		OpenAccountDto dto = new OpenAccountDto();
		JSONObject jsonObject = null;
		String custtp = "";
		try {
			jsonObject = JSONObject.parseObject(str);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("客户资料修改数据信息转换异常");
			return dto;
		}
		
		if(null != jsonObject ){
			   String serialNo = Sequences.getPK();//流水号 
			   dto.setSerialno(serialNo);
			   custtp = jsonObject.getString("custTp");
			   dto.setInvtp(custtp);
			   dto.setPermissionId(jsonObject.getString("permissionId"));
			   dto.setOperatorId(jsonObject.getString("operatorId"));   
			   dto.setCustno(jsonObject.getString("custnos"));
			   dto.setTrustType(jsonObject.getString("trusttp"));
			   dto.setModifylist(jsonObject.getString("modifylist"));
			   
			   //资料信息      
			   dto.setDocbusinesstp(jsonObject.getString("docbusinesstp"));
			   dto.setCustdocument(jsonObject.getString("document"));
			   dto.setIfalldocument(jsonObject.getString("isalldoc"));
			   dto.setIfOriginal(jsonObject.getString("iforiginal"));
			   dto.setIfsaved(jsonObject.getString("ifsaved"));
			   dto.setIsupload(jsonObject.getString("isupload"));//资料是否上传
			   dto.setRemarkinfo(jsonObject.getString("remarkinfo"));//资料信息备注
			   
			   dto.setShsecacc(jsonObject.getString("shsecacc"));//上交所股东代码
			   dto.setSzsecacc(jsonObject.getString("szsecacc"));//深交所股东代码
			   //原风险等级
			   dto.setPreRisklevel(jsonObject.getString("preRisklevel"));
			   dto.setInvprtp(jsonObject.getString("preInvprtp"));
			   //机构客户
			   if("0".equals(custtp))
			   {
			   	  //基本-->其他信息
			   	  dto.setTel(jsonObject.getString("orgInvOfficeTel")); //电话
			      dto.setFax(jsonObject.getString("orgInvFax"));		//传真
				  dto.setOrganType(jsonObject.getString("orgType"));//机构类型
			      //基本->证件信息
			      dto.setInvnm(jsonObject.getString("orgInvNm"));
			      dto.setIdtp(jsonObject.getString("orgInvIdtp"));
			      dto.setIdno(jsonObject.getString("orgInvIdno"));
			      dto.setIdvalidate(jsonObject.getString("orgInvIdvalidate"));
			        
			      //基本->法人信息
			      dto.setInstrepnm(jsonObject.getString("orgInstrepnm"));
			      dto.setInstrepnation(jsonObject.getString("orgInstrepnation"));
			      dto.setInstrepidtp(jsonObject.getString("orgInstrepidtp"));
			      dto.setInstrepidno(jsonObject.getString("orgInstrepidno"));
			      dto.setInstrepvalidate(jsonObject.getString("orgInstrepidvalidate"));
			      
			      //基本->机构负责人信息
			      dto.setPrincipalname(jsonObject.getString("orgPrincipalname"));      
			      dto.setPrincipalnation(jsonObject.getString("orgPrincipalnation"));
			      dto.setPrincipalidtp(jsonObject.getString("orgPrincipalidtp"));
			      dto.setPrincipalidno(jsonObject.getString("orgPrincipalidno"));
			      dto.setPrincipalvalidt(jsonObject.getString("orgPrincipalidvalidate"));
			      
			      //基本->经办人员信息
			      dto.setContactgrant(jsonObject.getString("orgContactright"));
			      dto.setContact(jsonObject.getString("orgContnm"));
			      dto.setContactnation(jsonObject.getString("orgContnation"));
			      dto.setContidtp(jsonObject.getString("orgContidtp"));
			      dto.setContidno(jsonObject.getString("orgContidno"));
			      dto.setContvalidate(jsonObject.getString("orgContidvalidate"));
			      dto.setContphone(jsonObject.getString("orgContphone"));
			      dto.setContfax(jsonObject.getString("orgContfax"));
			      dto.setContmobile(jsonObject.getString("orgContmobile"));
			      dto.setContemail(jsonObject.getString("orgContemail"));
			      dto.setContAddr(jsonObject.getString("orgContAddr"));
			      dto.setContPostcode(jsonObject.getString("orgContPostcode"));
			      //基本->其他信息 
			      dto.setAddr(jsonObject.getString("orgAddr"));
			      dto.setPostcode(jsonObject.getString("orgPostcode"));      
			      //附加->其他信息 
			      dto.setHoldingname(jsonObject.getString("orgHoldingname"));
			      dto.setHoldingidtp(jsonObject.getString("orgHoldingidtp"));
			      dto.setHoldingidno(jsonObject.getString("orgHoldingidno"));
			      dto.setHoldingvalidate(jsonObject.getString("orgHoldingidvalidate"));
			      dto.setBeneficiary(jsonObject.getString("orgBeneficiarynm"));
			      dto.setCustrisklevl(jsonObject.getString("orgRiskLevel"));
			      dto.setSpecriskLevel(jsonObject.getString("specriskLevel")); //特殊用户风险等级
			      //附加->其他证件信息
			      dto.setOidtp(jsonObject.getString("oidtpStr"));
			      dto.setOidno(jsonObject.getString("oidnoStr"));
			      dto.setOidvalidate(jsonObject.getString("oidvalidateStr"));  
			      //附加->其他经办人信息         
			      dto.setOcontactgrant(jsonObject.getString("oOrgContactright"));
			      dto.setOcontact(jsonObject.getString("oOrgContact"));
			      dto.setOcontactnation(jsonObject.getString("oOrgContactnation"));
			      dto.setOcontidtp(jsonObject.getString("oOrgContactidtp"));
			      dto.setOcontidno(jsonObject.getString("oOrgContactidno"));
			      dto.setOcontvalidate(jsonObject.getString("oOrgContactidvalidate"));
			      dto.setOcontphone(jsonObject.getString("oOrgContacttel"));
			      dto.setOcontfax(jsonObject.getString("oOrgContactfax"));
			      dto.setOcontmobile(jsonObject.getString("oOrgContactmobile"));
			      dto.setOcontemail(jsonObject.getString("oOrgContactemail"));
			      dto.setOcontAddr(jsonObject.getString("oOrgContactAddr"));
			      dto.setOcontPostcode(jsonObject.getString("oOrgContactPostcode"));   

			      
			   }
			   else
			   {
			      //基本->证件信息
			      dto.setInvnm(jsonObject.getString("pslInvNm"));
			      dto.setIdtp(jsonObject.getString("pslInvIdtp"));
			      dto.setIdno(jsonObject.getString("pslInvIdno"));
			      dto.setIdvalidate(jsonObject.getString("pslInvIdValidate"));   
			            
			      //基本->其他信息
			      dto.setTel(jsonObject.getString("pslInvOfficeTel"));
			      dto.setHousetel(jsonObject.getString("pslInvHomeTel"));
			      dto.setMobile(jsonObject.getString("pslInvMobile"));
			      dto.setFax(jsonObject.getString("pslInvFax"));
			      dto.setFaxdelegate(jsonObject.getString("plsFaxDelegate"));
			      dto.setEmail(jsonObject.getString("pslInvEmail"));
			      //dto.setSendingroute(jsonObject.getString("plsBillDelivery"));
			      dto.setPostcode(jsonObject.getString("plsPostCode"));
			      dto.setAddr(jsonObject.getString("plsAddr"));
			      
			      //附加信息->其他
			      dto.setSex(jsonObject.getString("plsSex"));
			      dto.setNationalitycode(jsonObject.getString("plsInvNation"));
			      dto.setEdlevel(jsonObject.getString("plsInvEducation"));
			      dto.setVoccode(jsonObject.getString("plsInvJob"));
			      dto.setIncome(jsonObject.getString("plsInvIncome"));
			      dto.setCustrisklevl(jsonObject.getString("plsInvRisk"));
			      dto.setSpecriskLevel(jsonObject.getString("specriskLevel")); //特殊用户风险等级
			   }
			   dto.setIsscan( jsonObject.getString("isscan"));
			   dto.setKeepaddress( jsonObject.getString("keepaddress"));
			   dto.setSalesaccmanager( jsonObject.getString("salesaccmanager"));
			   dto.setFileno( jsonObject.getString("fileno"));
			   dto.setErrcode("0000");
		}
		return dto;
	}

	@Override
	public Map<String, String> antiMoneyLaunValid(SearchParam map) {
		Map<String, String> result = new HashMap<String, String>();
		try{
			Map<String, String> resp = openAccountDao.antiMoneyLaunValid(map);
			if(null == resp){
				result.put("isPass", "0000");
			}else{
				result.put("isPass", "9999");
			}
		}catch(Exception e){
			logger.error("----OpenAccountServiceImpl-antiMoneyLaunValid-Exception-",e);	
		}
		return result;
	}
	
	public static void main(String[] args) {
		for (int i = 0; i < 200; i++) {
			System.out.println("第"+i+"个序列号："+Sequences.getPK());
		}
	}

	@Override
	public OpenAccountExcelDto batchOpenAccountSave(OpenAccountExcelDto dto) {
		try {
			// 经办人信息
			Object[] ocontactinfo = new Object[1];
			// 主经办人信息
			String[] mresult = new String[13];
			
			mresult[0] = "0";
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
			
			//其他经办人信息********************
			String newOcontStr = getStringByArrays(ocontactinfo,"||;","||,");
			dto.setBroke(newOcontStr);
            
            try {
				openAccountDao.batchOpenAccountSave(dto);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("账户开户异常：", e);
			}
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrCode("9999");
			dto.setErrMsg("开户数据解析出错！");
		}
		return dto;
	}
}