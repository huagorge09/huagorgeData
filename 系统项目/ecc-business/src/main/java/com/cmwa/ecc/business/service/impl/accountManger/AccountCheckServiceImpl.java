package com.cmwa.ecc.business.service.impl.accountManger;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ec.base.util.Sequences;
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ecc.business.dao.accountManger.AccountCheckDao;
import com.cmwa.ecc.business.dao.accountManger.CommonQueryDao;
import com.cmwa.ecc.business.entity.accountManger.ContactInfoDto;
import com.cmwa.ecc.business.entity.accountManger.IDInfoDto;
import com.cmwa.ecc.business.entity.accountManger.OpenAccountDto;
import com.cmwa.ecc.business.entity.accountManger.TradeDto;
import com.cmwa.ecc.business.service.accountManger.AccountCheckService;
import com.cmwa.ecc.business.service.accountManger.OpenAccountService;
import com.cmwa.ecc.business.utils.Page;
import com.cmwa.ecc.business.utils.PageUtil;
import com.cmwa.ecc.business.utils.SearchParam;

/**
 * 账户复核及驳回修改业务层
 * @author ex-chenbq
 *
 */
@Service
public class AccountCheckServiceImpl implements AccountCheckService {

	private Logger logger = Logger.getLogger(AccountCheckServiceImpl.class.getName());
	
	@Autowired
	private AccountCheckDao accountCheckDao;
	
	@Autowired
	private CommonQueryDao commonQueryDao;
	
	@Autowired
	private OpenAccountService openAccountService;

	@SuppressWarnings("unchecked")
	@Override
	public Page<TradeDto> accountCheckQry(SearchParam param) {
		List<TradeDto> list = new ArrayList<TradeDto>();
		PageUtil<TradeDto> page = new PageUtil<TradeDto>();
		List<TradeDto> resultList = new ArrayList<TradeDto>();
		try {
			accountCheckDao.accountCheckQry(param);
			list = (List<TradeDto>) param.getSp().get("tradeInfoList");
			resultList = page.getPageList(param, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("账户类复核查询异常：",e);
		}
		return Page.create(resultList, param.getStart(), param.getLimit(), list.size());
	}

	@SuppressWarnings("unchecked")
	@Override
	public OpenAccountDto accountCheckDetailQry(SearchParam param) {
		OpenAccountDto dto = new OpenAccountDto();
		try {
			accountCheckDao.accountCheckDetailQry(param);
			dto.setErrcode((String) param.getSp().get("errcode"));
			dto.setErrmsg((String) param.getSp().get("errmsg"));
			if(param.getSp().get("errcode").equals("0000")){
				
				//交易申请信息
	    		List<OpenAccountDto> appList = (List<OpenAccountDto>) param.getSp().get("appList");
	    		//客户基本信息
				List<OpenAccountDto> custInfo = (List<OpenAccountDto>) param.getSp().get("custinfoList");
				//客户基本扩展信息
				List<OpenAccountDto> custInfoexList = (List<OpenAccountDto>) param.getSp().get("custInfoexList");
				//账户基本信息
				List<OpenAccountDto> tradeAccoInfoList = (List<OpenAccountDto>) param.getSp().get("tradeAccoInfoList");
				//客户经办人信息
				List<ContactInfoDto> contInfoList = (List<ContactInfoDto>) param.getSp().get("contInfoList");
				//客户资料信息
				List<OpenAccountDto> statInfoList = (List<OpenAccountDto>) param.getSp().get("statInfoList");
				
				String regiontp = "";
				
				if(null != appList && appList.size() > 0){
					OpenAccountDto appDto = appList.get(0);
					converJavaBean(dto, appDto);
				}
				
				if(null != custInfo && custInfo.size() > 0){
					converJavaBean(dto, custInfo.get(0));
					regiontp = custInfo.get(0).getRegiontp();
				}
				if(null != custInfoexList && custInfoexList.size() > 0){
					OpenAccountDto custInfoex = custInfoexList.get(0);
					converJavaBean(dto, custInfoex);
					dto.setIdvalidate(formatDate(custInfoex.getIdvalidate()));
					dto.setInstrepvalidate(formatDate(custInfoex.getInstrepvalidate()));
					dto.setPrincipalvalidt(formatDate(custInfoex.getPrincipalvalidt()));
					dto.setHoldingvalidate(formatDate(custInfoex.getHoldingvalidate()));
				}
				if (null != tradeAccoInfoList && tradeAccoInfoList.size() > 0 ) {
					dto.setSendingroute(tradeAccoInfoList.get(0).getSendingroute());
				}
				
				if (contInfoList != null && contInfoList.size() > 0) {
					List<IDInfoDto> oidlist = new ArrayList<IDInfoDto>();
					List<ContactInfoDto> ocontlist = new ArrayList<ContactInfoDto>();
					for (int i = 0; i < contInfoList.size(); i++) {
						ContactInfoDto contDto = new ContactInfoDto();
						IDInfoDto idDto = new IDInfoDto();
						ContactInfoDto infoDto = contInfoList.get(i);
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
					
				if (statInfoList != null){
					List<OpenAccountDto> documentlist = new ArrayList<OpenAccountDto>();
					for (int i = 0; i < statInfoList.size(); i++) {
						OpenAccountDto docDto = new OpenAccountDto();
						docDto = statInfoList.get(i);
						if(i == 0){
							converJavaBean(dto, docDto);
						}
						OpenAccountDto documentDto = new OpenAccountDto();
						documentDto.setCustdocument(docDto.getCustdocument());//资料编号
						documentDto.setExistsflag(docDto.getExistsflag());//资料是否存在
						documentDto.setBusinesstp(docDto.getBusinesstp());//业务类型
						documentlist.add(documentDto);
					}
					dto.setDocumentlist(documentlist);
				}
				dto.setRegiontp(regiontp);
			}
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			logger.error("账户类复核明细查询异常：",e);
		}
		return dto;
	}

	/**
	 * 该方法是用于相同对象不同属性值的合并，如果两个相同对象中同一属性都有值，那么sourceBean中的值会覆盖tagetBean重点的值
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
	            if( (!(sourceField.get(sourceBean) == null) && !(sourceField.get(sourceBean).equals(""))) &&  !"serialVersionUID".equals(sourceField.getName().toString())){
	                targetField.set(targetBean,sourceField.get(sourceBean));
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
	    return targetBean;
	}

	/**
	 * 八位数字日期格式化成YYYY-MM-DD
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

	/**
	 * 
	 * <p>
	 * 
	 * @description 转换javabean ,将class2中的属性值赋值给class1，如果class1属性有值，则不覆盖
	 *              ，前提条件是有相同的属性名
	 *              </p>
	 * @param class1
	 *            基准类,被赋值对象
	 * @param class2
	 *            提供数据的对象
	 * @throws Exception
	 * @author ex_dingyongbiao
	 * @see
	 */
	public static void converJavaBean(Object class1, Object class2) {
		try {
			Class<?> clazz1 = class1.getClass();
			Class<?> clazz2 = class2.getClass();
			// 得到method方法
			Method[] method1 = clazz1.getMethods();
			Method[] method2 = clazz2.getMethods();

			int length1 = method1.length;
			int length2 = method2.length;
			if (length1 != 0 && length2 != 0) {
				// 创建一个get方法数组，专门存放class2的get方法。
				Method[] get = new Method[length2];
				for (int i = 0, j = 0; i < length2; i++) {
					if (method2[i].getName().indexOf("get") == 0) {
						Object object =  method2[i].invoke(class2, null);
						if (null != object && !object.equals("")) {
							get[j] = method2[i];
							++j;
						}
					}
				}

				for (int i = 0; i < get.length; i++) {
					if (get[i] == null)// 数组初始化的长度多于get方法，所以数组后面的部分是null
						continue;
					// 得到get方法的值，判断时候为null，如果为null则进行下一个循环
					Object value = get[i].invoke(class2, new Object[] {});
					if (null == value || value.equals(""))
						continue;
					// 得到get方法的名称 例如：getXxxx
					String getName = get[i].getName();
					// 得到set方法的时候传入的参数类型，就是get方法的返回类型
					Class<?> paramType = get[i].getReturnType();
					Method getMethod = null;
					try {
						// 判断在class1中时候有class2中的get方法，如果没有则抛异常继续循环
						getMethod = clazz1.getMethod(getName, new Class[] {});
					} catch (NoSuchMethodException e) {
						continue;
					}
					// class1的get方法不为空并且class1中get方法得到的值为空，进行赋值，如果class1属性原来有值，则跳过
					if (null == getMethod
							|| null != getMethod
									.invoke(class1, new Object[] {}))
						continue;
					// 通过getName 例如getXxxx 截取后得到Xxxx，然后在前面加上set，就组装成set的方法名
					String setName = "set" + getName.substring(3);
					// 得到class1的set方法，并调用
					Method setMethod = clazz1.getMethod(setName, paramType);
					if (null != value && !value.equals("")) {
						setMethod.invoke(class1, value);
					}
					
				}
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	@Override
	public OpenAccountDto accountInfoCheck(SearchParam param) {
		OpenAccountDto dto = new OpenAccountDto();
		try {
			accountCheckDao.accountInfoCheck(param);
			String serialNo =(String) param.getSp().get("serialno");
			String checkst =(String) param.getSp().get("checkst");
			dto.setErrcode((String)param.getSp().get("errcode"));
			dto.setErrmsg((String)param.getSp().get("errmsg"));
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("账户类复核异常："+e);
		}
		return dto;
	}
	
	@Override
	public OpenAccountDto accountRejectModify(OpenAccountDto dto) {
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
			
			if (!StringUtil.isEmpty(dto.getIdvalidate())) {
				dto.setIdvalidate(dto.getIdvalidate().replaceAll("-", ""));
			}
			
			if (!StringUtil.isEmpty(dto.getInstrepvalidate())) {
				dto.setInstrepvalidate(dto.getInstrepvalidate().replaceAll("-", ""));
			}
			
			if (!StringUtil.isEmpty(dto.getPrincipalvalidt())) {
				dto.setPrincipalvalidt(dto.getPrincipalvalidt().replaceAll("-", ""));
			}
			
			if (!StringUtil.isEmpty(dto.getHoldingvalidate())) {
				dto.setHoldingvalidate(dto.getHoldingvalidate().replaceAll("-", ""));
			}
			
			dto.setFlag("Y");
			
			accountCheckDao.accountRejectModify(dto);
			
			if(dto.getErrcode().equals("0000")){
    			String taxresident=dto.getTaxresident(); //税收居民数据
    			//保存税收居民信息到数据库
    			if(null != taxresident && !taxresident.equals("")){
    				openAccountService.saveUserTaxInfo(dto.getCustno(), dto, taxresident);
    			}else{
    				openAccountService.deleteTaxInfo(dto.getCustno());
    			}
    		}
			
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("账户类驳回修改异常："+e);
		}
		return dto;
	}
	

	/**
	 * 开户Json字符串数据转换成实体
	 * @param str
	 * @return
	 */
	@Override
	public OpenAccountDto convertBean(String str) {
		OpenAccountDto dto = new OpenAccountDto();
		JSONObject jsonObject = null;
		String custtp = "";
		try {
			jsonObject = JSONObject.parseObject(str);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("账户类驳回修改数据转换异常");
			return dto;
		}

		if (null != jsonObject) {
			String serialNo = Sequences.getPK();// 流水号
			custtp = jsonObject.getString("custtp");
			dto.setSerialno(serialNo);
			dto.setCustno(jsonObject.getString("custno"));
			dto.setInvtp(custtp);
			dto.setPermissionId(jsonObject.getString("permissionId"));
			dto.setOperatorId(jsonObject.getString("operatorId"));
			dto.setTrustType(jsonObject.getString("trustTp"));
			dto.setTano(jsonObject.getString("tano"));
			dto.setOserialno(jsonObject.getString("oserialno"));
			
			//原风险等级及专业类型
			
			dto.setPreRisklevel(jsonObject.getString("preInvprtp"));
			dto.setPreInvprtp(jsonObject.getString("preInvprtp"));
			
			// 基本->证件信息
			dto.setInvnm(jsonObject.getString("invNm"));
			dto.setIdtp(jsonObject.getString("invIdtp"));

			dto.setIdno(jsonObject.getString("invIdno"));
			dto.setIdvalidate(jsonObject.getString("invIdValidate"));

			// 基本->银行信息
			dto.setBnkNo(jsonObject.getString("bankno")); // 银行编号
			dto.setOpenName(jsonObject.getString("openname")); // 开户银行名称
			dto.setOpenAddr(jsonObject.getString("openlocation")); // 开户银行地址
			dto.setOpenBankCity(jsonObject.getString("openbankcity")); // 开户银行地址（市）
			dto.setBankAccoNm(jsonObject.getString("bankacconm")); // 银行账户名称
			dto.setBankAcco(jsonObject.getString("bankacco")); // 银行账号

			// 基本->其他信息
			dto.setPostcode(jsonObject.getString("postcode"));
			dto.setAddr(jsonObject.getString("addr"));
			dto.setShsecacc(jsonObject.getString("shsecacc")); // 上交所股东代码
			dto.setSzsecacc(jsonObject.getString("szsecacc")); // 深交所股东代码

			// 资料信息
			dto.setDocbusinesstp(jsonObject.getString("docbusinesstp")); // 资料业务类型
			dto.setCustdocument(jsonObject.getString("document")); // 资料名称列表
			dto.setIfOriginal(jsonObject.getString("iforiginal")); // 资料是否原件
			dto.setIfsaved(jsonObject.getString("ifsaved")); // 资料是否归档
			dto.setIsupload(jsonObject.getString("isupload")); // 是否上传附件
			dto.setIfalldocument(jsonObject.getString("isalldoc")); // 资料是否完整
			dto.setRemarkinfo(jsonObject.getString("remarkinfo")); // 资料信息备注

			// 分类信息
			dto.setBusinesstp(jsonObject.getString("businesstp")); // 业务类型
			dto.setCompanytp(jsonObject.getString("corptype")); // 公司类型
			dto.setRegiontp(jsonObject.getString("regioncode")); // 地域代码
			dto.setAmlrisktype(jsonObject.getString("fxqtype")); // 反洗钱风险等级
			dto.setFxqremark(jsonObject.getString("fxqdesc")); // 反洗钱备注
			dto.setInvprtp(jsonObject.getString("invprtp")); // 投资者类型
			dto.setSpecriskLevel(jsonObject.getString("specriskLevel")); // 特殊用户风险等级
			dto.setTaxType(jsonObject.getString("taxType")); // 税收居民信息
			dto.setTaxTypeDecl(jsonObject.getString("taxTypeDecl")); // 税收居民信息
			// 机构客户
			if ("0".equals(custtp)) {
				// 基本->法人信息
				dto.setInstrepnm(jsonObject.getString("orgInstrepnm"));
				dto.setInstrepnation(jsonObject.getString("orgInstrepnation"));
				dto.setInstrepidtp(jsonObject.getString("orgInstrepidtp"));
				dto.setInstrepidno(jsonObject.getString("orgInstrepidno"));
				dto.setInstrepvalidate(jsonObject
						.getString("orgInstrepidvalidate"));

				// 基本->机构负责人信息
				dto.setPrincipalname(jsonObject.getString("orgPrincipalname"));
				dto.setPrincipalnation(jsonObject
						.getString("orgPrincipalnation"));
				dto.setPrincipalidtp(jsonObject.getString("orgPrincipalidtp"));
				dto.setPrincipalidno(jsonObject.getString("orgPrincipalidno"));
				dto.setPrincipalvalidt(jsonObject
						.getString("orgPrincipalidvalidate"));

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
				dto.setHoldingvalidate(jsonObject
						.getString("orgHoldingidvalidate"));
				dto.setBeneficiary(jsonObject.getString("orgBeneficiarynm"));
				dto.setCustrisklevl(jsonObject.getString("orgRiskLevel"));
				dto.setOrganType(jsonObject.getString("orgType"));
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
				dto.setOcontvalidate(jsonObject
						.getString("oOrgContactidvalidate"));
				dto.setOcontphone(jsonObject.getString("oOrgContacttel"));
				dto.setOcontfax(jsonObject.getString("oOrgContactfax"));
				dto.setOcontmobile(jsonObject.getString("oOrgContactmobile"));
				dto.setOcontemail(jsonObject.getString("oOrgContactemail"));
				dto.setOcontAddr(jsonObject.getString("oOrgContactAddr"));
				dto.setOcontPostcode(jsonObject
						.getString("oOrgContactPostcode"));

				// 分类信息
				dto.setAcctabbr(jsonObject.getString("custsimpnm")); // 客户简称
				dto.setInstrepcode(jsonObject.getString("instrepcode")); // 法人代码

				dto.setTel(jsonObject.getString("tel"));
				dto.setFax(jsonObject.getString("fax"));

				// 投资者适当性信息
				dto.setInvestProInstType(jsonObject
						.getString("investProInstType"));
				dto.setInvestProInstSecond(jsonObject
						.getString("investProInstSecond"));
				dto.setOneYearEndNetAsset(jsonObject
						.getString("oneYearEndNetAsset"));
				dto.setOneYearEndFinAsset(jsonObject
						.getString("oneYearEndFinAsset"));
				dto.setInvestExperience(jsonObject
						.getString("investExperience"));
				dto.setNegativeNotFinaInst(jsonObject
						.getString("negativeNotFinaInst"));
				dto.setControlPerTaxDecl(jsonObject
						.getString("controlPerTaxDecl"));
			} else {
				// 基本->其他信息
				dto.setTel(jsonObject.getString("ptel"));
				dto.setHousetel(jsonObject.getString("phousetel"));
				dto.setMobile(jsonObject.getString("pmobile"));
				dto.setFax(jsonObject.getString("pfax"));
				dto.setFaxdelegate(jsonObject.getString("pfaxdelegate"));
				dto.setEmail(jsonObject.getString("pemail"));

				// 附加信息->其他
				dto.setSex(jsonObject.getString("psex"));
				dto.setNationalitycode(jsonObject.getString("pnationalitycode"));
				dto.setEdlevel(jsonObject.getString("pedlevel"));
				dto.setVoccode(jsonObject.getString("pvoccode"));
				dto.setIncome(jsonObject.getString("pincome"));
				dto.setCustrisklevl(jsonObject.getString("pcustrisklevl"));
				// 投资者适当性信息
				dto.setThreeAnnualIncome(jsonObject
						.getString("threeAnnualIncome"));
				dto.setFinancialAsset(jsonObject.getString("financialAsset"));
				dto.setIndInvExperience(jsonObject
						.getString("indInvExperience"));
				dto.setRelatedWorkExp(jsonObject.getString("relatedWorkExp"));
				dto.setFinProfessions(jsonObject.getString("finProfessions"));
			}
			dto.setIsscan(jsonObject.getString("isscan"));
			dto.setKeepaddress(jsonObject.getString("keepaddress"));
			dto.setSalesaccmanager(jsonObject.getString("salesaccmanager"));
			dto.setTaxresident(jsonObject.getString("taxresident"));
			dto.setErrcode("0000");
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
	public TradeDto getTradeCount() {
		TradeDto dto = new TradeDto();
		try {
			dto = accountCheckDao.getTradeCount();
			dto.setErrcode("0000");
			dto.setErrmsg("获取账户复核记录数成功！");
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("获取账户复核记录数异常："+e);
		}
		return dto;
	}

	@Override
	public String getOpenFileNo(String custno) {
		String fileNo = "";
		try {
			fileNo = commonQueryDao.getOpenFileNo(custno);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询开户文件编号异常：",e);
		}
		return fileNo;
	}

	@Override
	public String getCustFileInfo(String custno) {
		String fileNo = "";
		try {
			fileNo = commonQueryDao.getCustFileInfo(custno);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("查询客户的文件编号异常：",e);
		}
		return fileNo;
	}
}