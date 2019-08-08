package com.cmwa.ecc.business.service.impl.bakCustomer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cmwa.ec.base.util.Sequences;
import com.cmwa.ec.base.util.StringUtil;
import com.cmwa.ecc.business.commonVo.Employee;
import com.cmwa.ecc.business.commonVo.ParameterDto;
import com.cmwa.ecc.business.dao.bakCustomer.BakCustomerDao;
import com.cmwa.ecc.business.dao.common.CommonDao;
import com.cmwa.ecc.business.dao.widget.WidgetDao;
import com.cmwa.ecc.business.entity.accountManger.BakCustDto;
import com.cmwa.ecc.business.entity.accountManger.ContactInfoDto;
import com.cmwa.ecc.business.entity.accountManger.IDInfoDto;
import com.cmwa.ecc.business.service.accountManger.OpenAccountService;
import com.cmwa.ecc.business.service.bakCustomer.BakCustomerService;
import com.cmwa.ecc.business.service.widget.WidgetService;
import com.cmwa.ecc.business.utils.ParameterConstant;
import com.cmwa.ecc.business.utils.SearchParam;
import com.cmwa.ecc.business.utils.SessionUtils;

@Service
public class BakCustomerServiceImpl implements BakCustomerService {

	private Logger logger = Logger.getLogger(BakCustomerServiceImpl.class.getName());

	@Autowired
	private BakCustomerDao bakCustomerDao;

	@Autowired
	private WidgetService widgetService;
	
	@Autowired
	private CommonDao commonDao;
	
	@Autowired
	private OpenAccountService openAccountService;
	
	@Autowired
	private WidgetDao widgetDao;
	
	@Override
	public ModelAndView openDialog(HttpServletRequest request) {
		String method = request.getParameter("method");
		ModelAndView model = new ModelAndView();
		if (null == method || "".equals(method)) {
			model.setViewName("jsp/404");
			return model;
		}
		//证件类型
		SearchParam idtpSp = new SearchParam();
		idtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		idtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		idtpSp.getSp().put("pmv1", "1");
		List<Map<String, Object>> idtpArray = widgetService.queryMatchApkindList(idtpSp);
		model.addObject("idtpArray", idtpArray);
		
		//国籍代码
		List<Map<String, Object>> nationArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION);
		model.addObject("nationArray", nationArray);
		
		//风险等级
		List<Map<String, Object>> riskLevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_FUNDRISKLEVEL);
		model.addObject("riskLevelArray", riskLevelArray);
		
		//证件类别或CMF证件类别
		SearchParam seatidtpSp = new SearchParam();
		seatidtpSp.getSp().put("pmst", ParameterConstant.PARAM_PMST_SYSTEM);
		seatidtpSp.getSp().put("pmky", ParameterConstant.PARAM_PMKY_IDTP);
		seatidtpSp.getSp().put("pmv1", "0");
		List<Map<String, Object>> seatidtpArray = widgetService.queryMatchApkindList(seatidtpSp);
		model.addObject("seatidtpArray", seatidtpArray);
		
		//客户风险承受能力
		List<Map<String, Object>> custrisklevelArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_CUSTRISKLEVEL);
		model.addObject("custrisklevelArray", custrisklevelArray);
		
		//经办人授权范围
		List<Map<String, Object>> contprivArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BROKERPRIV);
		model.addObject("contprivArray", contprivArray);
		
		//查询客户分组信息
		List<Map<String, Object>> custFirst = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DSCUSTGROUP,ParameterConstant.PARAM_PMKY_FIRSTGROUP);
		model.addObject("custFirst", custFirst);
		
		//备案客户角色
		List<Map<String,Object>> custRoleArray = widgetService.queryParamListByStAndKy(ParameterConstant.PARAM_PMST_DS, "UCCUSTROLE");
		model.addObject("custRoleArray", custRoleArray);
		Employee employee = SessionUtils.getEmployee();
		model.addObject("permissionId", "8061");
		model.addObject("operatorId", null != employee ? employee.getID() : "");
		if (null != method && "add".equals(method)) {
			model.setViewName("jsp/bakCustomer/bakCustomerAdd");
		}

		if (null != method && "update".equals(method)) {
			model.setViewName("jsp/bakCustomer/bakCustomerUpdate");
			SearchParam sp = new SearchParam();
			String custno = request.getParameter("custno");
			String role = request.getParameter("role");
			BakCustDto bakCustDto = new BakCustDto();
			sp.setSp(new HashMap<String, Object>());
			sp.getSp().put("custno", custno);
			sp.getSp().put("role", role);
			try {
				bakCustDto = openAccountService.queryBakCust(sp).get(0);
				if(bakCustDto.getIdvalidate() != null && bakCustDto.getIdvalidate().length()>=8){
					StringBuffer sb = new StringBuffer();
					sb.append(bakCustDto.getIdvalidate().substring(0,4)+"-"+bakCustDto.getIdvalidate().substring(4,6)+"-"+bakCustDto.getIdvalidate().substring(6,8));
					bakCustDto.setIdvalidate(sb.toString());
				}
			} catch (Exception e) {
				logger.error("BakCustomerServiceImpl -- openDialog -- queryBakCust 获取备案客户信息异常");
			}
			JSONObject dto = (JSONObject) JSONObject.toJSON(bakCustDto);
			model.addObject("dto", dto);
		}
		if (null != method && "check".equals(method)) {
			BakCustDto dto = new BakCustDto();
			model.setViewName("jsp/bakCustomer/bakCustomerCheckDetail");
			SearchParam sp = new SearchParam();
			String custno = request.getParameter("custno");
			sp.getSp().put("custno", custno);
			List<BakCustDto> list = null;
			try {
				list = checkBakCustomerListPage(sp);
			} catch (Exception e) {
				logger.warn("BakCustomerServiceImpl -- openDialog -- checkBakCustomerListPage " + e);
			}
			if(list != null && list.size()>0) {
				dto = list.get(0);	
				//证件类型
				if(dto.getIdvalidate() != null && dto.getIdvalidate().length()>=8){
					StringBuffer sb = new StringBuffer();
					sb.append(dto.getIdvalidate().substring(0,4)+"-"+dto.getIdvalidate().substring(4,6)+"-"+dto.getIdvalidate().substring(6,8));
					dto.setIdvalidate(sb.toString());
				}
				dto.setIdtpnm(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,dto.getIdtp()));//客户证件类型
				dto.setPrincipalidtp(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,dto.getPrincipalidtp()));//机构负责人证件类型
				dto.setContidtp(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,dto.getContidtp()));//经办人证件类型
				dto.setInstrepidtp(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,dto.getInstrepidtp()));//法人证件类型
				//风险等级
				dto.setRiskLevel(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_CUSTRISKLEVEL, dto.getRiskLevel()));
				//授权范围
				dto.setContactgrant(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BROKERPRIV, dto.getContactgrant()));
				//国籍
				dto.setContactnation(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,dto.getContactnation()));//经办人国籍
				dto.setPrincipalnation(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,dto.getPrincipalnation()));//机构负责人国籍
				dto.setInstrepnation(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,dto.getInstrepnation()));//法人国籍
				dto.setRcdcustrole(queryParameter(ParameterConstant.PARAM_PMST_DS,"UCCUSTROLE",dto.getRcdcustrole()));//备案客户角色
				List<ContactInfoDto> infoDtos = dto.getOcontactlist();
				if (null != infoDtos && infoDtos.size() > 0) {
					for (int i = 0; i < infoDtos.size(); i++) {
						ContactInfoDto cid = infoDtos.get(i);
						cid.setContactnation(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_NATION,cid.getContactnation()));//经办人国籍
						cid.setContidtp(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,cid.getContidtp()));//经办人证件类型
						cid.setContactgrant(queryParameter(ParameterConstant.PARAM_PMST_DS, ParameterConstant.PARAM_PMKY_BROKERPRIV, cid.getContactgrant()));
					}
				}
				List<IDInfoDto> idTos = dto.getOidlist();
				if (null != idTos && idTos.size() > 0) {
					for (int i = 0; i < idTos.size(); i++) {
						IDInfoDto id = idTos.get(i);
						id.setIdtp(queryParameter(ParameterConstant.PARAM_PMST_SYSTEM, ParameterConstant.PARAM_PMKY_IDTP,id.getIdtp()));//经办人国籍
					}
				}

			}
			model.addObject("dto", dto);
		}
		return model;
	}
	
	@Override
	public BakCustDto addBakCustomer(BakCustDto dto) {
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
			dto.setIdvalidate(StringUtil.isEmpty(dto.getIdvalidate()) ? "" : dto.getIdvalidate().replaceAll("-", "") );
            dto.setInstrepvalidate(StringUtil.isEmpty(dto.getInstrepvalidate()) ? "" : dto.getInstrepvalidate().replaceAll("-", "") );
            dto.setPrincipalvalidt(StringUtil.isEmpty(dto.getPrincipalvalidt()) ? "" : dto.getPrincipalvalidt().replaceAll("-", "") );
            dto.setContvalidate(StringUtil.isEmpty(dto.getContvalidate()) ? "" : dto.getContvalidate().replaceAll("-", "") );
			bakCustomerDao.manageBakCustomer(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("备案客户开户数据解析出错！");
		}
		return dto;
	}
	
	@Override
	public BakCustDto convertBean(String str) {
		BakCustDto dto = new BakCustDto();
		JSONObject jsonObject = null;
	    String opertp = "";
		try {
			jsonObject = JSONObject.parseObject(str);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("备案客户开户数据信息转换异常");
			return dto;
		}
		if(jsonObject!=null)
	    {
	       opertp = jsonObject.getString("opertp");   // 操作类型 M ('I':新增,'U':修改,'D':删除)
	    }
	    String serialNo = Sequences.getPK();//流水号    
	    dto.setSerialno(serialNo);   
	    dto.setPermissionId(jsonObject.getString("permissionId"));
	    dto.setOperatorId(SessionUtils.getEmployee().getID()); 
	    
	    dto.setOpertp(opertp);   // 操作类型 M ('I':新增,'U':修改,'D':删除)
	    dto.setCustno(jsonObject.getString("custno"));   // 备案客户编号 O   
	    if(!("D".equals(opertp)))
	    {
	       //基本->证件信息
	       dto.setInvnm(jsonObject.getString("invNm"));
	       dto.setIdtp(jsonObject.getString("invIdtp"));
	       dto.setIdno(jsonObject.getString("invIdno"));
	       dto.setIdvalidate(jsonObject.getString("invIdValidate")); 
	       dto.setAddr(jsonObject.getString("addr"));
	       dto.setPostcode(jsonObject.getString("postcode")); 
	       dto.setRcdcustrole(jsonObject.getString("bakCustRole"));  // 备案客户角色 M    
	       dto.setRiskLevel(jsonObject.getString("riskLevel"));//客户风险承受能力
	       
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
	    dto.setErrcode("0000");
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

	@Override
	public BakCustDto delBakCustomer(BakCustDto dto) {
		try {
			bakCustomerDao.manageBakCustomer(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("备案客户信息异常,删除失败！");
		}
		return dto;
	}

	@Override
	public BakCustDto updateBakCustomer(BakCustDto dto) {
		try {// 其他证件信息
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
			dto.setIdvalidate(StringUtil.isEmpty(dto.getIdvalidate()) ? "" : dto.getIdvalidate().replaceAll("-", "") );
            dto.setInstrepvalidate(StringUtil.isEmpty(dto.getInstrepvalidate()) ? "" : dto.getInstrepvalidate().replaceAll("-", "") );
            dto.setPrincipalvalidt(StringUtil.isEmpty(dto.getPrincipalvalidt()) ? "" : dto.getPrincipalvalidt().replaceAll("-", "") );
			bakCustomerDao.manageBakCustomer(dto);
		} catch (Exception e) {
			e.printStackTrace();
			dto.setErrcode("9999");
			dto.setErrmsg("备案客户信息异常,更新失败！");
		}
		return dto;
	}

	@Override
	public List<BakCustDto> checkBakCustomerListPage(SearchParam sp) {
		List<BakCustDto> rList = new ArrayList<BakCustDto>();
		try {
			bakCustomerDao.queryBakCustomerCheck(sp);
			if(sp.getSp().get("errcode").equals("0000")){
				List<BakCustDto> bakCustList = (List<BakCustDto>) sp.getSp().get("bakCustList");
				List<ContactInfoDto> contactList = (List<ContactInfoDto>) sp.getSp().get("contactList");
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
	
	public String queryParameter(String pmst, String pmky ,String pmco){
		String pmnm = "";
		if(!StringUtil.isEmpty(pmco)){
			ParameterDto parameterDto = widgetService.getParameterByPmstPmkyPmco(pmst, pmky , pmco);
			if(null != parameterDto){
				pmnm = parameterDto.getPmnm();
			}
		}
		return pmnm;
	}

	@Override
	public BakCustDto checkBakCustomer(SearchParam sp) {
		BakCustDto dto = new BakCustDto();
		try {
			bakCustomerDao.checkBakCustomer(sp);
			dto.setErrcode(sp.getSp().get("errcode").toString());
			dto.setErrmsg(sp.getSp().get("errmsg").toString());
			System.out.println(dto.getErrcode());
			System.out.println(dto.getErrmsg());
		}catch (Exception e) {
			dto.setErrcode("9999");
			dto.setErrmsg("复核备案客户异常！");
			logger.warn("BakCustomerServiceImpl -- checkBakCustomer" + e);
		}
		return dto;
	}
	
}
